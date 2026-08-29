package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

// =====================================================================
// NOTE ON DESIGN
// =====================================================================
// This version follows the same architecture as
// BRRS_M_TOP_100_BORROWER_ReportService (the file you supplied as a
// reference), because the new BRRS_SLS_WORKING_* tables have the same
// two structural issues that service already solves for:
//
//   1. The old single SUMMARYTABLE-style table has been split into two
//      physical tables (BRRS_SLS_WORKING_TABLE1 covering rows R1-R70,
//      BRRS_SLS_WORKING_TABLE2 covering rows R71-R92; same split for
//      the archival summary as ARCHIVALTABLE1 / ARCHIVALTABLE2). The
//      reference service handles this exact pattern by querying each
//      table separately and exposing them to the view as
//      reportsummary / reportsummary1 (T1Master / T2Master) rather
//      than trying to force a single-table JOIN across tables that
//      don't share a natural row-level key.
//
//   2. The new tables use generic column names (R1_COLUMN_A ... N)
//      instead of hand-named business columns (R11_PRODUCT, R11_DAY1,
//      etc.), so a hand-written RowMapper per column - as the OLD
//      version of this file used - is not practical (988 + 316
//      columns across the two summary tables alone). The reference
//      file solves the equivalent problem (900+ columns) with ONE
//      generic, reflection-based row mapper (mapRow/queryEntities)
//      that matches ResultSet column labels to entity field names
//      (or @Column(name=...) when present). That same helper is
//      ported here unchanged and reused for all six tables.
//
// SCOPE OF THIS FILE: the summary view (getSLSView) and detail view
// (getSLScurrentDtl) read paths are fully rebuilt against the correct
// tables below. Excel/PDF export and insert/update/promote logic from
// the original 17k-line file were NOT ported in this pass - they all
// depend on the same 908-field-style entities and can be rebuilt with
// insertEntityReflective/updateEntityReflective (also ported from the
// reference, ready to use) the same way the reference file does, on
// request.
//
// TABLE -> ENTITY MAP:
//   BRRS_SLS_WORKING_TABLE1                -> SLS_WORKING_Summary_Entity1          (R1-R70)
//   BRRS_SLS_WORKING_TABLE2                -> SLS_WORKING_Summary_Entity2          (R71-R92)
//   BRRS_SLS_WORKING_ARCHIVALTABLE1        -> SLS_WORKING_Archival_Summary_Entity1 (R1-R70)
//   BRRS_SLS_WORKING_ARCHIVALTABLE2        -> SLS_WORKING_Archival_Summary_Entity2 (R71-R92)
//   BRRS_SLS_WORKING_DETAILTABLE           -> SLS_WORKING_Detail_Entity
//   BRRS_SLS_WORKING_ARCHIVALTABLE_DETAIL  -> SLS_WORKING_Archival_Detail_Entity
// =====================================================================

@Component
@Service
public class BRRS_SLS_WORKING_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_SLS_WORKING_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ------------------------------
	// Parses a date string robustly supporting multiple formats
	// ------------------------------
	private Date parseDateRobustly(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		try {
			return dateformat.parse(dateStr);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
			} catch (ParseException ex) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				} catch (ParseException ex2) {
					logger.error("Failed to parse date: {}", dateStr);
					return null;
				}
			}
		}
	}

	// ------------------------------
	// Finds the highest REPORT_VERSION already archived for a given date.
	// TABLE1 is used as the canonical source since REPORT_VERSION is
	// duplicated identically across TABLE1 and TABLE2 for a given report.
	// ------------------------------
	private BigDecimal findMaxVersion(Date reportDate) {
		BigDecimal maxVersion = jdbcTemplate.queryForObject(
				"SELECT MAX(REPORT_VERSION) FROM BRRS_SLS_WORKING_ARCHIVALTABLE1 WHERE REPORT_DATE = ?",
				new Object[] { reportDate }, BigDecimal.class);
		return maxVersion != null ? maxVersion : BigDecimal.ZERO;
	}

	// =====================================================================
	// GENERIC REFLECTION-BASED HELPERS (ported from
	// BRRS_M_TOP_100_BORROWER_ReportService). Works unmodified for any
	// entity below, whether it uses @Column(name=...) or relies on
	// field-name == column-name.
	// =====================================================================

	private String resolveColumnName(Field field) {
		Column columnAnn = field.getAnnotation(Column.class);
		return (columnAnn != null && !columnAnn.name().isEmpty()) ? columnAnn.name() : field.getName();
	}

	private <T> T mapRow(ResultSet rs, Class<T> clazz) throws SQLException {
		try {
			T instance = clazz.getDeclaredConstructor().newInstance();
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();

			for (int i = 1; i <= colCount; i++) {
				String colName = meta.getColumnLabel(i);
				for (Field field : clazz.getDeclaredFields()) {
					if (Modifier.isStatic(field.getModifiers()))
						continue;
					if (!resolveColumnName(field).equalsIgnoreCase(colName))
						continue;

					field.setAccessible(true);
					Object value = rs.getObject(i);
					if (value == null) {
						field.set(instance, null);
					} else if (field.getType().isAssignableFrom(value.getClass())) {
						field.set(instance, value);
					} else if (field.getType() == BigDecimal.class && value instanceof Number) {
						field.set(instance, BigDecimal.valueOf(((Number) value).doubleValue()));
					} else if (field.getType() == Double.class && value instanceof Number) {
						field.set(instance, ((Number) value).doubleValue());
					} else if (field.getType() == Date.class && value instanceof java.sql.Timestamp) {
						field.set(instance, new Date(((java.sql.Timestamp) value).getTime()));
					} else if (field.getType() == Date.class && value instanceof java.sql.Date) {
						field.set(instance, new Date(((java.sql.Date) value).getTime()));
					} else if (field.getType() == String.class) {
						field.set(instance, value.toString());
					}
					break;
				}
			}
			return instance;
		} catch (ReflectiveOperationException e) {
			throw new SQLException("Unable to map row to " + clazz.getSimpleName(), e);
		}
	}

	private <T> List<T> queryEntities(String sql, Class<T> clazz, Object... args) {
		return jdbcTemplate.query(sql, args, (rs, rowNum) -> mapRow(rs, clazz));
	}

	// ------------------------------
	// Generic reflection-based INSERT - builds the column list/placeholders
	// from the entity's declared fields (via resolveColumnName). Ready to
	// use for any of the six entities below once insert flows are ported.
	// ------------------------------
	private void insertEntityReflective(String tableName, Object entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder values = new StringBuilder(" VALUES (");
		List<Object> params = new ArrayList<>();
		Field[] fields = entity.getClass().getDeclaredFields();
		boolean first = true;
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (!first) {
				sql.append(", ");
				values.append(", ");
			}
			sql.append(resolveColumnName(field));
			values.append("?");
			try {
				field.setAccessible(true);
				params.add(field.get(entity));
			} catch (Exception e) {
				params.add(null);
			}
			first = false;
		}
		sql.append(")").append(values).append(")");
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// ------------------------------
	// Generic reflection-based UPDATE ... SET col1 = ?, col2 = ?, ... WHERE
	// whereColumn = ?, built from the entity's declared fields.
	// ------------------------------
	private void updateEntityReflective(String tableName, Object entity, String whereColumn, Object whereValue) {
		StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
		List<Object> params = new ArrayList<>();
		Field[] fields = entity.getClass().getDeclaredFields();
		boolean first = true;
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String colName = resolveColumnName(field);
			if (colName.equalsIgnoreCase(whereColumn)) {
				continue;
			}
			if (!first) {
				sql.append(", ");
			}
			sql.append(colName).append(" = ?");
			try {
				field.setAccessible(true);
				params.add(field.get(entity));
			} catch (Exception e) {
				params.add(null);
			}
			first = false;
		}
		sql.append(" WHERE ").append(whereColumn).append(" = ?");
		params.add(whereValue);
		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// =====================================================================
	// READ PATHS
	// =====================================================================

	// ------------------------------
	// Retrieves the SLS_WORKING report summary view.
	// CURRENT: queries TABLE1 (R1-R70) and TABLE2 (R71-R92) separately.
	// ARCHIVAL: queries ARCHIVALTABLE1 and ARCHIVALTABLE2 separately.
	// Both halves are exposed to the view so it can render the full R1-R92
	// row range; see reportsummary / reportsummary1 below (same pattern as
	// the reference service's reportsummary / reportsummary1 / reportsummary2).
	// ------------------------------
	public ModelAndView getSLSView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);

		Session hs = sessionFactory.getCurrentSession();

		Date parsedDate = parseDateRobustly(todate);

		if ("ARCHIVAL".equalsIgnoreCase(type)) {
			if (version == null && parsedDate != null) {
				version = findMaxVersion(parsedDate);
				logger.info("Auto-detected version inside service: {}", version);
			}

			List<SLS_WORKING_Archival_Summary_Entity1> T1Master = new ArrayList<>();
			List<SLS_WORKING_Archival_Summary_Entity2> T2Master = new ArrayList<>();
			if (version != null && parsedDate != null) {
				T1Master = queryEntities(
						"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
						SLS_WORKING_Archival_Summary_Entity1.class, parsedDate, version);
				T2Master = queryEntities(
						"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
						SLS_WORKING_Archival_Summary_Entity2.class, parsedDate, version);
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		} else {
			List<SLS_WORKING_Summary_Entity1> T1Master = new ArrayList<>();
			List<SLS_WORKING_Summary_Entity2> T2Master = new ArrayList<>();
			if (parsedDate != null) {
				T1Master = queryEntities("SELECT * FROM BRRS_SLS_WORKING_TABLE1 WHERE REPORT_DATE = ?",
						SLS_WORKING_Summary_Entity1.class, parsedDate);
				T2Master = queryEntities("SELECT * FROM BRRS_SLS_WORKING_TABLE2 WHERE REPORT_DATE = ?",
						SLS_WORKING_Summary_Entity2.class, parsedDate);
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		mv.setViewName("BRRS/SLS_WORKING");
		mv.addObject("displaymode", "summary");
		mv.addObject("reportdate", todate);
		mv.addObject("reportid", reportId);
		mv.addObject("asondate", todate);
		mv.addObject("fromdate", fromdate);
		mv.addObject("todate", todate);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version);

		return mv;
	}

	// ------------------------------
	// Retrieves the SLS_WORKING report details view.
	// CURRENT -> BRRS_SLS_WORKING_DETAILTABLE
	// ARCHIVAL -> BRRS_SLS_WORKING_ARCHIVALTABLE_DETAIL
	// ------------------------------
	public ModelAndView getSLScurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version, HttpServletRequest req1,
			Model md) {
		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);

		Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = parseDateRobustly(todate);

			String rowId = null;
			String columnId = null;

			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if ("ARCHIVAL".equalsIgnoreCase(type)) {
				if ((version == null || version.isEmpty() || "null".equalsIgnoreCase(version)) && parsedDate != null) {
					BigDecimal maxVer = findMaxVersion(parsedDate);
					if (maxVer != null) {
						version = maxVer.toString();
					}
				}

				List<SLS_WORKING_Archival_Detail_Entity> T1Dt1 = new ArrayList<>();
				if (version != null && parsedDate != null) {
					if (rowId != null && columnId != null) {
						T1Dt1 = queryEntities(
								"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE_DETAIL WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
								SLS_WORKING_Archival_Detail_Entity.class, rowId, columnId, parsedDate, version);
					} else {
						T1Dt1 = queryEntities(
								"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
								SLS_WORKING_Archival_Detail_Entity.class, parsedDate, version);
					}
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				logger.info("ARCHIVAL COUNT: {}", T1Dt1 != null ? T1Dt1.size() : 0);

			} else {
				List<SLS_WORKING_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = queryEntities(
							"SELECT * FROM BRRS_SLS_WORKING_DETAILTABLE WHERE ROW_ID = ? AND COLUMN_ID = ? AND REPORT_DATE = ?",
							SLS_WORKING_Detail_Entity.class, rowId, columnId, parsedDate);
				} else {
					T1Dt1 = queryEntities(
							"SELECT * FROM BRRS_SLS_WORKING_DETAILTABLE WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY",
							SLS_WORKING_Detail_Entity.class, parsedDate, currentPage * pageSize, pageSize);
					totalPages = jdbcTemplate.queryForObject(
							"SELECT COUNT(*) FROM BRRS_SLS_WORKING_DETAILTABLE WHERE REPORT_DATE = ?",
							new Object[] { parsedDate }, Integer.class);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				logger.info("LISTCOUNT: {}", T1Dt1 != null ? T1Dt1.size() : 0);
			}

		} catch (Exception e) {
			logger.error("Error loading SLS_WORKING details", e);
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		mv.setViewName("BRRS/SLS_WORKING");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		mv.addObject("totalPages", pageSize > 0 ? (int) Math.ceil((double) totalPages / pageSize) : 0);
		mv.addObject("reportid", reportId);
		mv.addObject("asondate", todate);
		mv.addObject("fromdate", fromdate);
		mv.addObject("todate", todate);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version);

		return mv;
	}

	// =====================================================================
	// ARCHIVAL LIST / VERSION LOOKUP
	// Ported from BRRS_SLS_INPUT_SHT_ReportService.getSLSArchival(). Uses
	// ARCHIVALTABLE1 as the canonical source of REPORT_DATE/REPORT_VERSION
	// pairs (identical values are duplicated onto ARCHIVALTABLE2).
	// =====================================================================
	public List<Object> getSLSArchival() {
		List<Object> SLSArchivallist = new ArrayList<>();
		try {
			SLSArchivallist = jdbcTemplate.query(
					"select REPORT_DATE, REPORT_VERSION from BRRS_SLS_WORKING_ARCHIVALTABLE1 order by REPORT_VERSION DESC",
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION"), null });
			logger.info("SLS archival version count: {}", SLSArchivallist.size());
		} catch (Exception e) {
			logger.error("Error fetching SLS_WORKING Archival data: {}", e.getMessage(), e);
		}
		return SLSArchivallist;
	}

	// =====================================================================
	// VIEW / EDIT PAGE
	// Ported from BRRS_SLS_INPUT_SHT_ReportService.getViewOrEditPage().
	// =====================================================================
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/SLS_WORKING");

		if (acctNo != null) {
			List<SLS_WORKING_Detail_Entity> list = queryEntities(
					"SELECT * FROM BRRS_SLS_WORKING_DETAILTABLE WHERE ACCT_NUMBER = ?",
					SLS_WORKING_Detail_Entity.class, acctNo);
			SLS_WORKING_Detail_Entity detailEntity = list.isEmpty() ? null : list.get(0);
			if (detailEntity != null && detailEntity.getREPORT_DATE() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(detailEntity.getREPORT_DATE());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("mpllData", detailEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// =====================================================================
	// DETAIL UPDATE
	// Ported from BRRS_SLS_INPUT_SHT_ReportService.updateDetailEdit(). The
	// summary-recompute procedure name is assumed to follow the same naming
	// convention as the original (BRRS_SLS_INPUT_SHT_SUMMARY_PROCEDURE ->
	// BRRS_SLS_WORKING_SUMMARY_PROCEDURE); confirm the actual DB procedure
	// name if it differs.
	// =====================================================================
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("provision");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			List<SLS_WORKING_Detail_Entity> list = queryEntities(
					"SELECT * FROM BRRS_SLS_WORKING_DETAILTABLE WHERE ACCT_NUMBER = ?",
					SLS_WORKING_Detail_Entity.class, acctNo);
			SLS_WORKING_Detail_Entity existing = list.isEmpty() ? null : list.get(0);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getACCT_NAME() == null || !existing.getACCT_NAME().equals(acctName)) {
					existing.setACCT_NAME(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getPROVISION() == null || existing.getPROVISION().compareTo(newProvision) != 0) {
					existing.setPROVISION(newProvision);
					isChanged = true;
					logger.info("Provision updated to {}", newProvision);
				}
			}

			if (isChanged) {
				jdbcTemplate.update("UPDATE BRRS_SLS_WORKING_DETAILTABLE SET ACCT_NAME = ?, PROVISION = ? WHERE ACCT_NUMBER = ?",
						existing.getACCT_NAME(), existing.getPROVISION(), existing.getACCT_NUMBER());
				logger.info("Record updated successfully for account {}", acctNo);

				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed - calling BRRS_SLS_WORKING_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_SLS_WORKING_SUMMARY_PROCEDURE(?); END;", formattedDate);
							logger.info("Procedure executed successfully after commit.");
						} catch (Exception e) {
							logger.error("Error executing procedure after commit", e);
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			} else {
				logger.info("No changes detected for ACCT_NO: {}", acctNo);
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating SLS_WORKING record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// =====================================================================
	// SUMMARY EXCEL EXPORT (template-based)
	// Ported from BRRS_SLS_INPUT_SHT_ReportService.getSLSExcel() /
	// getExcelSLSARCHIVAL(). The original wrote 11 named "day-bucket"
	// columns per row (DAY1, DAY2_7 ... OVER5Y) via reflection into a
	// fixed Excel template. The new schema no longer has those business
	// names - only generic R{n}_COLUMN_A..N.
	//
	// !!! ASSUMPTION - NEEDS BUSINESS CONFIRMATION !!!
	// This port assumes the 11 day-bucket values occupy COLUMN_B through
	// COLUMN_L in the same left-to-right order as the original 11
	// fieldSuffixes (COLUMN_A is treated as the row's label/product column
	// and is not written into these numeric cells; COLUMN_M/COLUMN_N are
	// left unmapped since the original only had 11 numeric buckets + a
	// separately-handled TOTAL). This is a POSITIONAL GUESS, not a
	// confirmed mapping - verify against the actual template/business
	// spec before relying on the exported figures.
	//
	// Row range: rows 11-85 (rowIndex 10-84) span BOTH summary tables
	// (TABLE1 = R1-R70, TABLE2 = R71-R92), so this pulls the correct
	// table's entity depending on which side of R70 the row falls.
	// =====================================================================
	private static final char[] SLS_EXCEL_BUCKET_LETTERS = { 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L' };
	private static final int[] SLS_EXCEL_SKIP_ROWS = { 40, 41, 42, 43, 44, 45, 74, 75, 76, 77, 78, 79, 80, 82, 85 };

	private boolean isSlsExcelSkipRow(int rowIndex) {
		for (int skip : SLS_EXCEL_SKIP_ROWS) {
			if (rowIndex == skip) {
				return true;
			}
		}
		return false;
	}

	private void writeSlsExcelBody(Sheet sheet, CellStyle textStyle, Object table1Record, Object table2Record) {
		for (int rowIndex = 10; rowIndex < 85; rowIndex++) {
			if (isSlsExcelSkipRow(rowIndex)) {
				continue;
			}
			int rowNum = rowIndex + 1; // R{rowNum}
			Object record = (rowNum <= 70) ? table1Record : table2Record;
			if (record == null) {
				continue;
			}

			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}

			for (int colIndex = 0; colIndex < SLS_EXCEL_BUCKET_LETTERS.length; colIndex++) {
				String fieldName = "R" + rowNum + "_COLUMN_" + SLS_EXCEL_BUCKET_LETTERS[colIndex];
				Cell cell = row.getCell(colIndex + 3);
				if (cell == null) {
					continue;
				}
				try {
					Field field = record.getClass().getDeclaredField(fieldName);
					field.setAccessible(true);
					Object value = field.get(record);
					if (value instanceof BigDecimal) {
						cell.setCellValue(((BigDecimal) value).doubleValue());
					} else {
						cell.setCellValue(0.00);
					}
				} catch (NoSuchFieldException | IllegalAccessException e) {
					cell.setCellValue("");
					cell.setCellStyle(textStyle);
					logger.warn("Field not found or inaccessible: {}", fieldName);
				}
			}
		}
	}

	// ------------------------------
	// Generates Excel report for BRRS_SLS_WORKING current (non-archival) summary
	// ------------------------------
	public byte[] getSLSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			return getExcelSLSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		Date parsedDate = parseDateRobustly(todate);
		List<SLS_WORKING_Summary_Entity1> dataList1 = queryEntities(
				"SELECT * FROM BRRS_SLS_WORKING_TABLE1 WHERE REPORT_DATE = ?",
				SLS_WORKING_Summary_Entity1.class, parsedDate);
		List<SLS_WORKING_Summary_Entity2> dataList2 = queryEntities(
				"SELECT * FROM BRRS_SLS_WORKING_TABLE2 WHERE REPORT_DATE = ?",
				SLS_WORKING_Summary_Entity2.class, parsedDate);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for SLS_WORKING report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			Row daterow = sheet.getRow(4);
			Cell datecell = daterow.getCell(3);
			datecell.setCellValue(parsedDate);

			SLS_WORKING_Summary_Entity1 record1 = dataList1.get(0);
			SLS_WORKING_Summary_Entity2 record2 = dataList2 != null && !dataList2.isEmpty() ? dataList2.get(0) : null;

			writeSlsExcelBody(sheet, textStyle, record1, record2);

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	// ------------------------------
	// Generates Excel report for BRRS_SLS_WORKING archival summary
	// ------------------------------
	public byte[] getExcelSLSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting archival Excel generation process in memory.");

		Date parsedDate = parseDateRobustly(todate);
		List<SLS_WORKING_Archival_Summary_Entity1> dataList1 = queryEntities(
				"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				SLS_WORKING_Archival_Summary_Entity1.class, parsedDate, version);
		List<SLS_WORKING_Archival_Summary_Entity2> dataList2 = queryEntities(
				"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?",
				SLS_WORKING_Archival_Summary_Entity2.class, parsedDate, version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for SLS_WORKING archival report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			Row daterow = sheet.getRow(4);
			Cell datecell = daterow.getCell(3);
			datecell.setCellValue(parsedDate);

			SLS_WORKING_Archival_Summary_Entity1 record1 = dataList1.get(0);
			SLS_WORKING_Archival_Summary_Entity2 record2 = dataList2 != null && !dataList2.isEmpty() ? dataList2.get(0)
					: null;

			writeSlsExcelBody(sheet, textStyle, record1, record2);

			workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			workbook.write(out);

			logger.info("Service: Archival Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	// =====================================================================
	// DETAIL EXCEL EXPORT (flat row-per-account, no template)
	// Ported from BRRS_SLS_INPUT_SHT_ReportService.getSLSDetailExcel() /
	// getDetailExcelARCHIVAL(). These use only generic detail-table columns
	// (no business-semantic ambiguity), so they're a direct 1:1 port.
	// =====================================================================
	public byte[] getSLSDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for SLS_WORKING Details...");

			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				return getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SLSDetails");

			BorderStyle border = BorderStyle.THIN;

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "REPORT LABEL",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(i == 3 ? rightAlignedHeaderStyle : headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			Date parsedToDate = parseDateRobustly(todate);
			List<SLS_WORKING_Detail_Entity> reportData = queryEntities(
					"SELECT * FROM BRRS_SLS_WORKING_DETAILTABLE WHERE REPORT_DATE = ?",
					SLS_WORKING_Detail_Entity.class, parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SLS_WORKING_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());

					Cell balanceCell = row.createCell(3);
					balanceCell.setCellValue(item.getPROVISION() != null ? item.getPROVISION().doubleValue() : 0);
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getROW_ID());
					row.createCell(5).setCellValue(item.getCOLUMN_ID());
					row.createCell(6).setCellValue(item.getREPORT_DATE() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
							: "");

					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for SLS_WORKING - only header will be written.");
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SLS_WORKING Excel", e);
			return new byte[0];
		}
	}

	// ------------------------------
	// Generates Excel report for BRRS_SLS_WORKING archival details
	// ------------------------------
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for SLS_WORKING ARCHIVAL Details...");

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("SLSDetails");

			BorderStyle border = BorderStyle.THIN;

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerStyle.setFont(headerFont);
			headerStyle.setAlignment(HorizontalAlignment.LEFT);
			headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderTop(border);
			headerStyle.setBorderBottom(border);
			headerStyle.setBorderLeft(border);
			headerStyle.setBorderRight(border);

			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "REPORT LABEL",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(i == 3 ? rightAlignedHeaderStyle : headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			Date parsedToDate = parseDateRobustly(todate);
			List<SLS_WORKING_Archival_Detail_Entity> reportData = queryEntities(
					"SELECT * FROM BRRS_SLS_WORKING_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?",
					SLS_WORKING_Archival_Detail_Entity.class, parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (SLS_WORKING_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCUST_ID());
					row.createCell(1).setCellValue(item.getACCT_NUMBER());
					row.createCell(2).setCellValue(item.getACCT_NAME());

					Cell balanceCell = row.createCell(3);
					balanceCell.setCellValue(item.getPROVISION() != null ? item.getPROVISION().doubleValue() : 0);
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getROW_ID());
					row.createCell(5).setCellValue(item.getCOLUMN_ID());
					row.createCell(6).setCellValue(item.getREPORT_DATE() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(item.getREPORT_DATE())
							: "");

					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for SLS_WORKING - only header will be written.");
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating SLS_WORKING archival Excel", e);
			return new byte[0];
		}
	}

	// =====================================================================
	// ENTITY CLASSES
	// Field names are the literal DB column names (R{n}_COLUMN_{A..N}, plus
	// the trailer/report-metadata columns), matching the style used by the
	// reference service's Summary_Entity classes. No @Column annotation is
	// needed for these since field name == column name (case-insensitive
	// match in mapRow/resolveColumnName above handles it).
	// =====================================================================

	public static class SLS_WORKING_Summary_Entity1 {

	private String R1_COLUMN_A;
	private BigDecimal R1_COLUMN_B;
	private BigDecimal R1_COLUMN_C;
	private BigDecimal R1_COLUMN_D;
	private BigDecimal R1_COLUMN_E;
	private BigDecimal R1_COLUMN_F;
	private BigDecimal R1_COLUMN_G;
	private BigDecimal R1_COLUMN_H;
	private BigDecimal R1_COLUMN_I;
	private BigDecimal R1_COLUMN_J;
	private BigDecimal R1_COLUMN_K;
	private BigDecimal R1_COLUMN_L;
	private BigDecimal R1_COLUMN_M;
	private BigDecimal R1_COLUMN_N;
	private String R2_COLUMN_A;
	private BigDecimal R2_COLUMN_B;
	private BigDecimal R2_COLUMN_C;
	private BigDecimal R2_COLUMN_D;
	private BigDecimal R2_COLUMN_E;
	private BigDecimal R2_COLUMN_F;
	private BigDecimal R2_COLUMN_G;
	private BigDecimal R2_COLUMN_H;
	private BigDecimal R2_COLUMN_I;
	private BigDecimal R2_COLUMN_J;
	private BigDecimal R2_COLUMN_K;
	private BigDecimal R2_COLUMN_L;
	private BigDecimal R2_COLUMN_M;
	private BigDecimal R2_COLUMN_N;
	private String R3_COLUMN_A;
	private BigDecimal R3_COLUMN_B;
	private BigDecimal R3_COLUMN_C;
	private BigDecimal R3_COLUMN_D;
	private BigDecimal R3_COLUMN_E;
	private BigDecimal R3_COLUMN_F;
	private BigDecimal R3_COLUMN_G;
	private BigDecimal R3_COLUMN_H;
	private BigDecimal R3_COLUMN_I;
	private BigDecimal R3_COLUMN_J;
	private BigDecimal R3_COLUMN_K;
	private BigDecimal R3_COLUMN_L;
	private BigDecimal R3_COLUMN_M;
	private BigDecimal R3_COLUMN_N;
	private String R4_COLUMN_A;
	private BigDecimal R4_COLUMN_B;
	private BigDecimal R4_COLUMN_C;
	private BigDecimal R4_COLUMN_D;
	private BigDecimal R4_COLUMN_E;
	private BigDecimal R4_COLUMN_F;
	private BigDecimal R4_COLUMN_G;
	private BigDecimal R4_COLUMN_H;
	private BigDecimal R4_COLUMN_I;
	private BigDecimal R4_COLUMN_J;
	private BigDecimal R4_COLUMN_K;
	private BigDecimal R4_COLUMN_L;
	private BigDecimal R4_COLUMN_M;
	private BigDecimal R4_COLUMN_N;
	private String R5_COLUMN_A;
	private BigDecimal R5_COLUMN_B;
	private BigDecimal R5_COLUMN_C;
	private BigDecimal R5_COLUMN_D;
	private BigDecimal R5_COLUMN_E;
	private BigDecimal R5_COLUMN_F;
	private BigDecimal R5_COLUMN_G;
	private BigDecimal R5_COLUMN_H;
	private BigDecimal R5_COLUMN_I;
	private BigDecimal R5_COLUMN_J;
	private BigDecimal R5_COLUMN_K;
	private BigDecimal R5_COLUMN_L;
	private BigDecimal R5_COLUMN_M;
	private BigDecimal R5_COLUMN_N;
	private String R6_COLUMN_A;
	private BigDecimal R6_COLUMN_B;
	private BigDecimal R6_COLUMN_C;
	private BigDecimal R6_COLUMN_D;
	private BigDecimal R6_COLUMN_E;
	private BigDecimal R6_COLUMN_F;
	private BigDecimal R6_COLUMN_G;
	private BigDecimal R6_COLUMN_H;
	private BigDecimal R6_COLUMN_I;
	private BigDecimal R6_COLUMN_J;
	private BigDecimal R6_COLUMN_K;
	private BigDecimal R6_COLUMN_L;
	private BigDecimal R6_COLUMN_M;
	private BigDecimal R6_COLUMN_N;
	private String R7_COLUMN_A;
	private BigDecimal R7_COLUMN_B;
	private BigDecimal R7_COLUMN_C;
	private BigDecimal R7_COLUMN_D;
	private BigDecimal R7_COLUMN_E;
	private BigDecimal R7_COLUMN_F;
	private BigDecimal R7_COLUMN_G;
	private BigDecimal R7_COLUMN_H;
	private BigDecimal R7_COLUMN_I;
	private BigDecimal R7_COLUMN_J;
	private BigDecimal R7_COLUMN_K;
	private BigDecimal R7_COLUMN_L;
	private BigDecimal R7_COLUMN_M;
	private BigDecimal R7_COLUMN_N;
	private String R8_COLUMN_A;
	private BigDecimal R8_COLUMN_B;
	private BigDecimal R8_COLUMN_C;
	private BigDecimal R8_COLUMN_D;
	private BigDecimal R8_COLUMN_E;
	private BigDecimal R8_COLUMN_F;
	private BigDecimal R8_COLUMN_G;
	private BigDecimal R8_COLUMN_H;
	private BigDecimal R8_COLUMN_I;
	private BigDecimal R8_COLUMN_J;
	private BigDecimal R8_COLUMN_K;
	private BigDecimal R8_COLUMN_L;
	private BigDecimal R8_COLUMN_M;
	private BigDecimal R8_COLUMN_N;
	private String R9_COLUMN_A;
	private BigDecimal R9_COLUMN_B;
	private BigDecimal R9_COLUMN_C;
	private BigDecimal R9_COLUMN_D;
	private BigDecimal R9_COLUMN_E;
	private BigDecimal R9_COLUMN_F;
	private BigDecimal R9_COLUMN_G;
	private BigDecimal R9_COLUMN_H;
	private BigDecimal R9_COLUMN_I;
	private BigDecimal R9_COLUMN_J;
	private BigDecimal R9_COLUMN_K;
	private BigDecimal R9_COLUMN_L;
	private BigDecimal R9_COLUMN_M;
	private BigDecimal R9_COLUMN_N;
	private String R10_COLUMN_A;
	private BigDecimal R10_COLUMN_B;
	private BigDecimal R10_COLUMN_C;
	private BigDecimal R10_COLUMN_D;
	private BigDecimal R10_COLUMN_E;
	private BigDecimal R10_COLUMN_F;
	private BigDecimal R10_COLUMN_G;
	private BigDecimal R10_COLUMN_H;
	private BigDecimal R10_COLUMN_I;
	private BigDecimal R10_COLUMN_J;
	private BigDecimal R10_COLUMN_K;
	private BigDecimal R10_COLUMN_L;
	private BigDecimal R10_COLUMN_M;
	private BigDecimal R10_COLUMN_N;
	private String R11_COLUMN_A;
	private BigDecimal R11_COLUMN_B;
	private BigDecimal R11_COLUMN_C;
	private BigDecimal R11_COLUMN_D;
	private BigDecimal R11_COLUMN_E;
	private BigDecimal R11_COLUMN_F;
	private BigDecimal R11_COLUMN_G;
	private BigDecimal R11_COLUMN_H;
	private BigDecimal R11_COLUMN_I;
	private BigDecimal R11_COLUMN_J;
	private BigDecimal R11_COLUMN_K;
	private BigDecimal R11_COLUMN_L;
	private BigDecimal R11_COLUMN_M;
	private BigDecimal R11_COLUMN_N;
	private String R12_COLUMN_A;
	private BigDecimal R12_COLUMN_B;
	private BigDecimal R12_COLUMN_C;
	private BigDecimal R12_COLUMN_D;
	private BigDecimal R12_COLUMN_E;
	private BigDecimal R12_COLUMN_F;
	private BigDecimal R12_COLUMN_G;
	private BigDecimal R12_COLUMN_H;
	private BigDecimal R12_COLUMN_I;
	private BigDecimal R12_COLUMN_J;
	private BigDecimal R12_COLUMN_K;
	private BigDecimal R12_COLUMN_L;
	private BigDecimal R12_COLUMN_M;
	private BigDecimal R12_COLUMN_N;
	private String R13_COLUMN_A;
	private BigDecimal R13_COLUMN_B;
	private BigDecimal R13_COLUMN_C;
	private BigDecimal R13_COLUMN_D;
	private BigDecimal R13_COLUMN_E;
	private BigDecimal R13_COLUMN_F;
	private BigDecimal R13_COLUMN_G;
	private BigDecimal R13_COLUMN_H;
	private BigDecimal R13_COLUMN_I;
	private BigDecimal R13_COLUMN_J;
	private BigDecimal R13_COLUMN_K;
	private BigDecimal R13_COLUMN_L;
	private BigDecimal R13_COLUMN_M;
	private BigDecimal R13_COLUMN_N;
	private String R14_COLUMN_A;
	private BigDecimal R14_COLUMN_B;
	private BigDecimal R14_COLUMN_C;
	private BigDecimal R14_COLUMN_D;
	private BigDecimal R14_COLUMN_E;
	private BigDecimal R14_COLUMN_F;
	private BigDecimal R14_COLUMN_G;
	private BigDecimal R14_COLUMN_H;
	private BigDecimal R14_COLUMN_I;
	private BigDecimal R14_COLUMN_J;
	private BigDecimal R14_COLUMN_K;
	private BigDecimal R14_COLUMN_L;
	private BigDecimal R14_COLUMN_M;
	private BigDecimal R14_COLUMN_N;
	private String R15_COLUMN_A;
	private BigDecimal R15_COLUMN_B;
	private BigDecimal R15_COLUMN_C;
	private BigDecimal R15_COLUMN_D;
	private BigDecimal R15_COLUMN_E;
	private BigDecimal R15_COLUMN_F;
	private BigDecimal R15_COLUMN_G;
	private BigDecimal R15_COLUMN_H;
	private BigDecimal R15_COLUMN_I;
	private BigDecimal R15_COLUMN_J;
	private BigDecimal R15_COLUMN_K;
	private BigDecimal R15_COLUMN_L;
	private BigDecimal R15_COLUMN_M;
	private BigDecimal R15_COLUMN_N;
	private String R16_COLUMN_A;
	private BigDecimal R16_COLUMN_B;
	private BigDecimal R16_COLUMN_C;
	private BigDecimal R16_COLUMN_D;
	private BigDecimal R16_COLUMN_E;
	private BigDecimal R16_COLUMN_F;
	private BigDecimal R16_COLUMN_G;
	private BigDecimal R16_COLUMN_H;
	private BigDecimal R16_COLUMN_I;
	private BigDecimal R16_COLUMN_J;
	private BigDecimal R16_COLUMN_K;
	private BigDecimal R16_COLUMN_L;
	private BigDecimal R16_COLUMN_M;
	private BigDecimal R16_COLUMN_N;
	private String R17_COLUMN_A;
	private BigDecimal R17_COLUMN_B;
	private BigDecimal R17_COLUMN_C;
	private BigDecimal R17_COLUMN_D;
	private BigDecimal R17_COLUMN_E;
	private BigDecimal R17_COLUMN_F;
	private BigDecimal R17_COLUMN_G;
	private BigDecimal R17_COLUMN_H;
	private BigDecimal R17_COLUMN_I;
	private BigDecimal R17_COLUMN_J;
	private BigDecimal R17_COLUMN_K;
	private BigDecimal R17_COLUMN_L;
	private BigDecimal R17_COLUMN_M;
	private BigDecimal R17_COLUMN_N;
	private String R18_COLUMN_A;
	private BigDecimal R18_COLUMN_B;
	private BigDecimal R18_COLUMN_C;
	private BigDecimal R18_COLUMN_D;
	private BigDecimal R18_COLUMN_E;
	private BigDecimal R18_COLUMN_F;
	private BigDecimal R18_COLUMN_G;
	private BigDecimal R18_COLUMN_H;
	private BigDecimal R18_COLUMN_I;
	private BigDecimal R18_COLUMN_J;
	private BigDecimal R18_COLUMN_K;
	private BigDecimal R18_COLUMN_L;
	private BigDecimal R18_COLUMN_M;
	private BigDecimal R18_COLUMN_N;
	private String R19_COLUMN_A;
	private BigDecimal R19_COLUMN_B;
	private BigDecimal R19_COLUMN_C;
	private BigDecimal R19_COLUMN_D;
	private BigDecimal R19_COLUMN_E;
	private BigDecimal R19_COLUMN_F;
	private BigDecimal R19_COLUMN_G;
	private BigDecimal R19_COLUMN_H;
	private BigDecimal R19_COLUMN_I;
	private BigDecimal R19_COLUMN_J;
	private BigDecimal R19_COLUMN_K;
	private BigDecimal R19_COLUMN_L;
	private BigDecimal R19_COLUMN_M;
	private BigDecimal R19_COLUMN_N;
	private String R20_COLUMN_A;
	private BigDecimal R20_COLUMN_B;
	private BigDecimal R20_COLUMN_C;
	private BigDecimal R20_COLUMN_D;
	private BigDecimal R20_COLUMN_E;
	private BigDecimal R20_COLUMN_F;
	private BigDecimal R20_COLUMN_G;
	private BigDecimal R20_COLUMN_H;
	private BigDecimal R20_COLUMN_I;
	private BigDecimal R20_COLUMN_J;
	private BigDecimal R20_COLUMN_K;
	private BigDecimal R20_COLUMN_L;
	private BigDecimal R20_COLUMN_M;
	private BigDecimal R20_COLUMN_N;
	private String R21_COLUMN_A;
	private BigDecimal R21_COLUMN_B;
	private BigDecimal R21_COLUMN_C;
	private BigDecimal R21_COLUMN_D;
	private BigDecimal R21_COLUMN_E;
	private BigDecimal R21_COLUMN_F;
	private BigDecimal R21_COLUMN_G;
	private BigDecimal R21_COLUMN_H;
	private BigDecimal R21_COLUMN_I;
	private BigDecimal R21_COLUMN_J;
	private BigDecimal R21_COLUMN_K;
	private BigDecimal R21_COLUMN_L;
	private BigDecimal R21_COLUMN_M;
	private BigDecimal R21_COLUMN_N;
	private String R22_COLUMN_A;
	private BigDecimal R22_COLUMN_B;
	private BigDecimal R22_COLUMN_C;
	private BigDecimal R22_COLUMN_D;
	private BigDecimal R22_COLUMN_E;
	private BigDecimal R22_COLUMN_F;
	private BigDecimal R22_COLUMN_G;
	private BigDecimal R22_COLUMN_H;
	private BigDecimal R22_COLUMN_I;
	private BigDecimal R22_COLUMN_J;
	private BigDecimal R22_COLUMN_K;
	private BigDecimal R22_COLUMN_L;
	private BigDecimal R22_COLUMN_M;
	private BigDecimal R22_COLUMN_N;
	private String R23_COLUMN_A;
	private BigDecimal R23_COLUMN_B;
	private BigDecimal R23_COLUMN_C;
	private BigDecimal R23_COLUMN_D;
	private BigDecimal R23_COLUMN_E;
	private BigDecimal R23_COLUMN_F;
	private BigDecimal R23_COLUMN_G;
	private BigDecimal R23_COLUMN_H;
	private BigDecimal R23_COLUMN_I;
	private BigDecimal R23_COLUMN_J;
	private BigDecimal R23_COLUMN_K;
	private BigDecimal R23_COLUMN_L;
	private BigDecimal R23_COLUMN_M;
	private BigDecimal R23_COLUMN_N;
	private String R24_COLUMN_A;
	private BigDecimal R24_COLUMN_B;
	private BigDecimal R24_COLUMN_C;
	private BigDecimal R24_COLUMN_D;
	private BigDecimal R24_COLUMN_E;
	private BigDecimal R24_COLUMN_F;
	private BigDecimal R24_COLUMN_G;
	private BigDecimal R24_COLUMN_H;
	private BigDecimal R24_COLUMN_I;
	private BigDecimal R24_COLUMN_J;
	private BigDecimal R24_COLUMN_K;
	private BigDecimal R24_COLUMN_L;
	private BigDecimal R24_COLUMN_M;
	private BigDecimal R24_COLUMN_N;
	private String R25_COLUMN_A;
	private BigDecimal R25_COLUMN_B;
	private BigDecimal R25_COLUMN_C;
	private BigDecimal R25_COLUMN_D;
	private BigDecimal R25_COLUMN_E;
	private BigDecimal R25_COLUMN_F;
	private BigDecimal R25_COLUMN_G;
	private BigDecimal R25_COLUMN_H;
	private BigDecimal R25_COLUMN_I;
	private BigDecimal R25_COLUMN_J;
	private BigDecimal R25_COLUMN_K;
	private BigDecimal R25_COLUMN_L;
	private BigDecimal R25_COLUMN_M;
	private BigDecimal R25_COLUMN_N;
	private String R26_COLUMN_A;
	private BigDecimal R26_COLUMN_B;
	private BigDecimal R26_COLUMN_C;
	private BigDecimal R26_COLUMN_D;
	private BigDecimal R26_COLUMN_E;
	private BigDecimal R26_COLUMN_F;
	private BigDecimal R26_COLUMN_G;
	private BigDecimal R26_COLUMN_H;
	private BigDecimal R26_COLUMN_I;
	private BigDecimal R26_COLUMN_J;
	private BigDecimal R26_COLUMN_K;
	private BigDecimal R26_COLUMN_L;
	private BigDecimal R26_COLUMN_M;
	private BigDecimal R26_COLUMN_N;
	private String R27_COLUMN_A;
	private BigDecimal R27_COLUMN_B;
	private BigDecimal R27_COLUMN_C;
	private BigDecimal R27_COLUMN_D;
	private BigDecimal R27_COLUMN_E;
	private BigDecimal R27_COLUMN_F;
	private BigDecimal R27_COLUMN_G;
	private BigDecimal R27_COLUMN_H;
	private BigDecimal R27_COLUMN_I;
	private BigDecimal R27_COLUMN_J;
	private BigDecimal R27_COLUMN_K;
	private BigDecimal R27_COLUMN_L;
	private BigDecimal R27_COLUMN_M;
	private BigDecimal R27_COLUMN_N;
	private String R28_COLUMN_A;
	private BigDecimal R28_COLUMN_B;
	private BigDecimal R28_COLUMN_C;
	private BigDecimal R28_COLUMN_D;
	private BigDecimal R28_COLUMN_E;
	private BigDecimal R28_COLUMN_F;
	private BigDecimal R28_COLUMN_G;
	private BigDecimal R28_COLUMN_H;
	private BigDecimal R28_COLUMN_I;
	private BigDecimal R28_COLUMN_J;
	private BigDecimal R28_COLUMN_K;
	private BigDecimal R28_COLUMN_L;
	private BigDecimal R28_COLUMN_M;
	private BigDecimal R28_COLUMN_N;
	private String R29_COLUMN_A;
	private BigDecimal R29_COLUMN_B;
	private BigDecimal R29_COLUMN_C;
	private BigDecimal R29_COLUMN_D;
	private BigDecimal R29_COLUMN_E;
	private BigDecimal R29_COLUMN_F;
	private BigDecimal R29_COLUMN_G;
	private BigDecimal R29_COLUMN_H;
	private BigDecimal R29_COLUMN_I;
	private BigDecimal R29_COLUMN_J;
	private BigDecimal R29_COLUMN_K;
	private BigDecimal R29_COLUMN_L;
	private BigDecimal R29_COLUMN_M;
	private BigDecimal R29_COLUMN_N;
	private String R30_COLUMN_A;
	private BigDecimal R30_COLUMN_B;
	private BigDecimal R30_COLUMN_C;
	private BigDecimal R30_COLUMN_D;
	private BigDecimal R30_COLUMN_E;
	private BigDecimal R30_COLUMN_F;
	private BigDecimal R30_COLUMN_G;
	private BigDecimal R30_COLUMN_H;
	private BigDecimal R30_COLUMN_I;
	private BigDecimal R30_COLUMN_J;
	private BigDecimal R30_COLUMN_K;
	private BigDecimal R30_COLUMN_L;
	private BigDecimal R30_COLUMN_M;
	private BigDecimal R30_COLUMN_N;
	private String R31_COLUMN_A;
	private BigDecimal R31_COLUMN_B;
	private BigDecimal R31_COLUMN_C;
	private BigDecimal R31_COLUMN_D;
	private BigDecimal R31_COLUMN_E;
	private BigDecimal R31_COLUMN_F;
	private BigDecimal R31_COLUMN_G;
	private BigDecimal R31_COLUMN_H;
	private BigDecimal R31_COLUMN_I;
	private BigDecimal R31_COLUMN_J;
	private BigDecimal R31_COLUMN_K;
	private BigDecimal R31_COLUMN_L;
	private BigDecimal R31_COLUMN_M;
	private BigDecimal R31_COLUMN_N;
	private String R32_COLUMN_A;
	private BigDecimal R32_COLUMN_B;
	private BigDecimal R32_COLUMN_C;
	private BigDecimal R32_COLUMN_D;
	private BigDecimal R32_COLUMN_E;
	private BigDecimal R32_COLUMN_F;
	private BigDecimal R32_COLUMN_G;
	private BigDecimal R32_COLUMN_H;
	private BigDecimal R32_COLUMN_I;
	private BigDecimal R32_COLUMN_J;
	private BigDecimal R32_COLUMN_K;
	private BigDecimal R32_COLUMN_L;
	private BigDecimal R32_COLUMN_M;
	private BigDecimal R32_COLUMN_N;
	private String R33_COLUMN_A;
	private BigDecimal R33_COLUMN_B;
	private BigDecimal R33_COLUMN_C;
	private BigDecimal R33_COLUMN_D;
	private BigDecimal R33_COLUMN_E;
	private BigDecimal R33_COLUMN_F;
	private BigDecimal R33_COLUMN_G;
	private BigDecimal R33_COLUMN_H;
	private BigDecimal R33_COLUMN_I;
	private BigDecimal R33_COLUMN_J;
	private BigDecimal R33_COLUMN_K;
	private BigDecimal R33_COLUMN_L;
	private BigDecimal R33_COLUMN_M;
	private BigDecimal R33_COLUMN_N;
	private String R34_COLUMN_A;
	private BigDecimal R34_COLUMN_B;
	private BigDecimal R34_COLUMN_C;
	private BigDecimal R34_COLUMN_D;
	private BigDecimal R34_COLUMN_E;
	private BigDecimal R34_COLUMN_F;
	private BigDecimal R34_COLUMN_G;
	private BigDecimal R34_COLUMN_H;
	private BigDecimal R34_COLUMN_I;
	private BigDecimal R34_COLUMN_J;
	private BigDecimal R34_COLUMN_K;
	private BigDecimal R34_COLUMN_L;
	private BigDecimal R34_COLUMN_M;
	private BigDecimal R34_COLUMN_N;
	private String R35_COLUMN_A;
	private BigDecimal R35_COLUMN_B;
	private BigDecimal R35_COLUMN_C;
	private BigDecimal R35_COLUMN_D;
	private BigDecimal R35_COLUMN_E;
	private BigDecimal R35_COLUMN_F;
	private BigDecimal R35_COLUMN_G;
	private BigDecimal R35_COLUMN_H;
	private BigDecimal R35_COLUMN_I;
	private BigDecimal R35_COLUMN_J;
	private BigDecimal R35_COLUMN_K;
	private BigDecimal R35_COLUMN_L;
	private BigDecimal R35_COLUMN_M;
	private BigDecimal R35_COLUMN_N;
	private String R36_COLUMN_A;
	private BigDecimal R36_COLUMN_B;
	private BigDecimal R36_COLUMN_C;
	private BigDecimal R36_COLUMN_D;
	private BigDecimal R36_COLUMN_E;
	private BigDecimal R36_COLUMN_F;
	private BigDecimal R36_COLUMN_G;
	private BigDecimal R36_COLUMN_H;
	private BigDecimal R36_COLUMN_I;
	private BigDecimal R36_COLUMN_J;
	private BigDecimal R36_COLUMN_K;
	private BigDecimal R36_COLUMN_L;
	private BigDecimal R36_COLUMN_M;
	private BigDecimal R36_COLUMN_N;
	private String R37_COLUMN_A;
	private BigDecimal R37_COLUMN_B;
	private BigDecimal R37_COLUMN_C;
	private BigDecimal R37_COLUMN_D;
	private BigDecimal R37_COLUMN_E;
	private BigDecimal R37_COLUMN_F;
	private BigDecimal R37_COLUMN_G;
	private BigDecimal R37_COLUMN_H;
	private BigDecimal R37_COLUMN_I;
	private BigDecimal R37_COLUMN_J;
	private BigDecimal R37_COLUMN_K;
	private BigDecimal R37_COLUMN_L;
	private BigDecimal R37_COLUMN_M;
	private BigDecimal R37_COLUMN_N;
	private String R38_COLUMN_A;
	private BigDecimal R38_COLUMN_B;
	private BigDecimal R38_COLUMN_C;
	private BigDecimal R38_COLUMN_D;
	private BigDecimal R38_COLUMN_E;
	private BigDecimal R38_COLUMN_F;
	private BigDecimal R38_COLUMN_G;
	private BigDecimal R38_COLUMN_H;
	private BigDecimal R38_COLUMN_I;
	private BigDecimal R38_COLUMN_J;
	private BigDecimal R38_COLUMN_K;
	private BigDecimal R38_COLUMN_L;
	private BigDecimal R38_COLUMN_M;
	private BigDecimal R38_COLUMN_N;
	private String R39_COLUMN_A;
	private BigDecimal R39_COLUMN_B;
	private BigDecimal R39_COLUMN_C;
	private BigDecimal R39_COLUMN_D;
	private BigDecimal R39_COLUMN_E;
	private BigDecimal R39_COLUMN_F;
	private BigDecimal R39_COLUMN_G;
	private BigDecimal R39_COLUMN_H;
	private BigDecimal R39_COLUMN_I;
	private BigDecimal R39_COLUMN_J;
	private BigDecimal R39_COLUMN_K;
	private BigDecimal R39_COLUMN_L;
	private BigDecimal R39_COLUMN_M;
	private BigDecimal R39_COLUMN_N;
	private String R40_COLUMN_A;
	private BigDecimal R40_COLUMN_B;
	private BigDecimal R40_COLUMN_C;
	private BigDecimal R40_COLUMN_D;
	private BigDecimal R40_COLUMN_E;
	private BigDecimal R40_COLUMN_F;
	private BigDecimal R40_COLUMN_G;
	private BigDecimal R40_COLUMN_H;
	private BigDecimal R40_COLUMN_I;
	private BigDecimal R40_COLUMN_J;
	private BigDecimal R40_COLUMN_K;
	private BigDecimal R40_COLUMN_L;
	private BigDecimal R40_COLUMN_M;
	private BigDecimal R40_COLUMN_N;
	private String R41_COLUMN_A;
	private BigDecimal R41_COLUMN_B;
	private BigDecimal R41_COLUMN_C;
	private BigDecimal R41_COLUMN_D;
	private BigDecimal R41_COLUMN_E;
	private BigDecimal R41_COLUMN_F;
	private BigDecimal R41_COLUMN_G;
	private BigDecimal R41_COLUMN_H;
	private BigDecimal R41_COLUMN_I;
	private BigDecimal R41_COLUMN_J;
	private BigDecimal R41_COLUMN_K;
	private BigDecimal R41_COLUMN_L;
	private BigDecimal R41_COLUMN_M;
	private BigDecimal R41_COLUMN_N;
	private String R42_COLUMN_A;
	private BigDecimal R42_COLUMN_B;
	private BigDecimal R42_COLUMN_C;
	private BigDecimal R42_COLUMN_D;
	private BigDecimal R42_COLUMN_E;
	private BigDecimal R42_COLUMN_F;
	private BigDecimal R42_COLUMN_G;
	private BigDecimal R42_COLUMN_H;
	private BigDecimal R42_COLUMN_I;
	private BigDecimal R42_COLUMN_J;
	private BigDecimal R42_COLUMN_K;
	private BigDecimal R42_COLUMN_L;
	private BigDecimal R42_COLUMN_M;
	private BigDecimal R42_COLUMN_N;
	private String R43_COLUMN_A;
	private BigDecimal R43_COLUMN_B;
	private BigDecimal R43_COLUMN_C;
	private BigDecimal R43_COLUMN_D;
	private BigDecimal R43_COLUMN_E;
	private BigDecimal R43_COLUMN_F;
	private BigDecimal R43_COLUMN_G;
	private BigDecimal R43_COLUMN_H;
	private BigDecimal R43_COLUMN_I;
	private BigDecimal R43_COLUMN_J;
	private BigDecimal R43_COLUMN_K;
	private BigDecimal R43_COLUMN_L;
	private BigDecimal R43_COLUMN_M;
	private BigDecimal R43_COLUMN_N;
	private String R44_COLUMN_A;
	private BigDecimal R44_COLUMN_B;
	private BigDecimal R44_COLUMN_C;
	private BigDecimal R44_COLUMN_D;
	private BigDecimal R44_COLUMN_E;
	private BigDecimal R44_COLUMN_F;
	private BigDecimal R44_COLUMN_G;
	private BigDecimal R44_COLUMN_H;
	private BigDecimal R44_COLUMN_I;
	private BigDecimal R44_COLUMN_J;
	private BigDecimal R44_COLUMN_K;
	private BigDecimal R44_COLUMN_L;
	private BigDecimal R44_COLUMN_M;
	private BigDecimal R44_COLUMN_N;
	private String R45_COLUMN_A;
	private BigDecimal R45_COLUMN_B;
	private BigDecimal R45_COLUMN_C;
	private BigDecimal R45_COLUMN_D;
	private BigDecimal R45_COLUMN_E;
	private BigDecimal R45_COLUMN_F;
	private BigDecimal R45_COLUMN_G;
	private BigDecimal R45_COLUMN_H;
	private BigDecimal R45_COLUMN_I;
	private BigDecimal R45_COLUMN_J;
	private BigDecimal R45_COLUMN_K;
	private BigDecimal R45_COLUMN_L;
	private BigDecimal R45_COLUMN_M;
	private BigDecimal R45_COLUMN_N;
	private String R46_COLUMN_A;
	private BigDecimal R46_COLUMN_B;
	private BigDecimal R46_COLUMN_C;
	private BigDecimal R46_COLUMN_D;
	private BigDecimal R46_COLUMN_E;
	private BigDecimal R46_COLUMN_F;
	private BigDecimal R46_COLUMN_G;
	private BigDecimal R46_COLUMN_H;
	private BigDecimal R46_COLUMN_I;
	private BigDecimal R46_COLUMN_J;
	private BigDecimal R46_COLUMN_K;
	private BigDecimal R46_COLUMN_L;
	private BigDecimal R46_COLUMN_M;
	private BigDecimal R46_COLUMN_N;
	private String R47_COLUMN_A;
	private BigDecimal R47_COLUMN_B;
	private BigDecimal R47_COLUMN_C;
	private BigDecimal R47_COLUMN_D;
	private BigDecimal R47_COLUMN_E;
	private BigDecimal R47_COLUMN_F;
	private BigDecimal R47_COLUMN_G;
	private BigDecimal R47_COLUMN_H;
	private BigDecimal R47_COLUMN_I;
	private BigDecimal R47_COLUMN_J;
	private BigDecimal R47_COLUMN_K;
	private BigDecimal R47_COLUMN_L;
	private BigDecimal R47_COLUMN_M;
	private BigDecimal R47_COLUMN_N;
	private String R48_COLUMN_A;
	private BigDecimal R48_COLUMN_B;
	private BigDecimal R48_COLUMN_C;
	private BigDecimal R48_COLUMN_D;
	private BigDecimal R48_COLUMN_E;
	private BigDecimal R48_COLUMN_F;
	private BigDecimal R48_COLUMN_G;
	private BigDecimal R48_COLUMN_H;
	private BigDecimal R48_COLUMN_I;
	private BigDecimal R48_COLUMN_J;
	private BigDecimal R48_COLUMN_K;
	private BigDecimal R48_COLUMN_L;
	private BigDecimal R48_COLUMN_M;
	private BigDecimal R48_COLUMN_N;
	private String R49_COLUMN_A;
	private BigDecimal R49_COLUMN_B;
	private BigDecimal R49_COLUMN_C;
	private BigDecimal R49_COLUMN_D;
	private BigDecimal R49_COLUMN_E;
	private BigDecimal R49_COLUMN_F;
	private BigDecimal R49_COLUMN_G;
	private BigDecimal R49_COLUMN_H;
	private BigDecimal R49_COLUMN_I;
	private BigDecimal R49_COLUMN_J;
	private BigDecimal R49_COLUMN_K;
	private BigDecimal R49_COLUMN_L;
	private BigDecimal R49_COLUMN_M;
	private BigDecimal R49_COLUMN_N;
	private String R50_COLUMN_A;
	private BigDecimal R50_COLUMN_B;
	private BigDecimal R50_COLUMN_C;
	private BigDecimal R50_COLUMN_D;
	private BigDecimal R50_COLUMN_E;
	private BigDecimal R50_COLUMN_F;
	private BigDecimal R50_COLUMN_G;
	private BigDecimal R50_COLUMN_H;
	private BigDecimal R50_COLUMN_I;
	private BigDecimal R50_COLUMN_J;
	private BigDecimal R50_COLUMN_K;
	private BigDecimal R50_COLUMN_L;
	private BigDecimal R50_COLUMN_M;
	private BigDecimal R50_COLUMN_N;
	private String R51_COLUMN_A;
	private BigDecimal R51_COLUMN_B;
	private BigDecimal R51_COLUMN_C;
	private BigDecimal R51_COLUMN_D;
	private BigDecimal R51_COLUMN_E;
	private BigDecimal R51_COLUMN_F;
	private BigDecimal R51_COLUMN_G;
	private BigDecimal R51_COLUMN_H;
	private BigDecimal R51_COLUMN_I;
	private BigDecimal R51_COLUMN_J;
	private BigDecimal R51_COLUMN_K;
	private BigDecimal R51_COLUMN_L;
	private BigDecimal R51_COLUMN_M;
	private BigDecimal R51_COLUMN_N;
	private String R52_COLUMN_A;
	private BigDecimal R52_COLUMN_B;
	private BigDecimal R52_COLUMN_C;
	private BigDecimal R52_COLUMN_D;
	private BigDecimal R52_COLUMN_E;
	private BigDecimal R52_COLUMN_F;
	private BigDecimal R52_COLUMN_G;
	private BigDecimal R52_COLUMN_H;
	private BigDecimal R52_COLUMN_I;
	private BigDecimal R52_COLUMN_J;
	private BigDecimal R52_COLUMN_K;
	private BigDecimal R52_COLUMN_L;
	private BigDecimal R52_COLUMN_M;
	private BigDecimal R52_COLUMN_N;
	private String R53_COLUMN_A;
	private BigDecimal R53_COLUMN_B;
	private BigDecimal R53_COLUMN_C;
	private BigDecimal R53_COLUMN_D;
	private BigDecimal R53_COLUMN_E;
	private BigDecimal R53_COLUMN_F;
	private BigDecimal R53_COLUMN_G;
	private BigDecimal R53_COLUMN_H;
	private BigDecimal R53_COLUMN_I;
	private BigDecimal R53_COLUMN_J;
	private BigDecimal R53_COLUMN_K;
	private BigDecimal R53_COLUMN_L;
	private BigDecimal R53_COLUMN_M;
	private BigDecimal R53_COLUMN_N;
	private String R54_COLUMN_A;
	private BigDecimal R54_COLUMN_B;
	private BigDecimal R54_COLUMN_C;
	private BigDecimal R54_COLUMN_D;
	private BigDecimal R54_COLUMN_E;
	private BigDecimal R54_COLUMN_F;
	private BigDecimal R54_COLUMN_G;
	private BigDecimal R54_COLUMN_H;
	private BigDecimal R54_COLUMN_I;
	private BigDecimal R54_COLUMN_J;
	private BigDecimal R54_COLUMN_K;
	private BigDecimal R54_COLUMN_L;
	private BigDecimal R54_COLUMN_M;
	private BigDecimal R54_COLUMN_N;
	private String R55_COLUMN_A;
	private BigDecimal R55_COLUMN_B;
	private BigDecimal R55_COLUMN_C;
	private BigDecimal R55_COLUMN_D;
	private BigDecimal R55_COLUMN_E;
	private BigDecimal R55_COLUMN_F;
	private BigDecimal R55_COLUMN_G;
	private BigDecimal R55_COLUMN_H;
	private BigDecimal R55_COLUMN_I;
	private BigDecimal R55_COLUMN_J;
	private BigDecimal R55_COLUMN_K;
	private BigDecimal R55_COLUMN_L;
	private BigDecimal R55_COLUMN_M;
	private BigDecimal R55_COLUMN_N;
	private String R56_COLUMN_A;
	private BigDecimal R56_COLUMN_B;
	private BigDecimal R56_COLUMN_C;
	private BigDecimal R56_COLUMN_D;
	private BigDecimal R56_COLUMN_E;
	private BigDecimal R56_COLUMN_F;
	private BigDecimal R56_COLUMN_G;
	private BigDecimal R56_COLUMN_H;
	private BigDecimal R56_COLUMN_I;
	private BigDecimal R56_COLUMN_J;
	private BigDecimal R56_COLUMN_K;
	private BigDecimal R56_COLUMN_L;
	private BigDecimal R56_COLUMN_M;
	private BigDecimal R56_COLUMN_N;
	private String R57_COLUMN_A;
	private BigDecimal R57_COLUMN_B;
	private BigDecimal R57_COLUMN_C;
	private BigDecimal R57_COLUMN_D;
	private BigDecimal R57_COLUMN_E;
	private BigDecimal R57_COLUMN_F;
	private BigDecimal R57_COLUMN_G;
	private BigDecimal R57_COLUMN_H;
	private BigDecimal R57_COLUMN_I;
	private BigDecimal R57_COLUMN_J;
	private BigDecimal R57_COLUMN_K;
	private BigDecimal R57_COLUMN_L;
	private BigDecimal R57_COLUMN_M;
	private BigDecimal R57_COLUMN_N;
	private String R58_COLUMN_A;
	private BigDecimal R58_COLUMN_B;
	private BigDecimal R58_COLUMN_C;
	private BigDecimal R58_COLUMN_D;
	private BigDecimal R58_COLUMN_E;
	private BigDecimal R58_COLUMN_F;
	private BigDecimal R58_COLUMN_G;
	private BigDecimal R58_COLUMN_H;
	private BigDecimal R58_COLUMN_I;
	private BigDecimal R58_COLUMN_J;
	private BigDecimal R58_COLUMN_K;
	private BigDecimal R58_COLUMN_L;
	private BigDecimal R58_COLUMN_M;
	private BigDecimal R58_COLUMN_N;
	private String R59_COLUMN_A;
	private BigDecimal R59_COLUMN_B;
	private BigDecimal R59_COLUMN_C;
	private BigDecimal R59_COLUMN_D;
	private BigDecimal R59_COLUMN_E;
	private BigDecimal R59_COLUMN_F;
	private BigDecimal R59_COLUMN_G;
	private BigDecimal R59_COLUMN_H;
	private BigDecimal R59_COLUMN_I;
	private BigDecimal R59_COLUMN_J;
	private BigDecimal R59_COLUMN_K;
	private BigDecimal R59_COLUMN_L;
	private BigDecimal R59_COLUMN_M;
	private BigDecimal R59_COLUMN_N;
	private String R60_COLUMN_A;
	private BigDecimal R60_COLUMN_B;
	private BigDecimal R60_COLUMN_C;
	private BigDecimal R60_COLUMN_D;
	private BigDecimal R60_COLUMN_E;
	private BigDecimal R60_COLUMN_F;
	private BigDecimal R60_COLUMN_G;
	private BigDecimal R60_COLUMN_H;
	private BigDecimal R60_COLUMN_I;
	private BigDecimal R60_COLUMN_J;
	private BigDecimal R60_COLUMN_K;
	private BigDecimal R60_COLUMN_L;
	private BigDecimal R60_COLUMN_M;
	private BigDecimal R60_COLUMN_N;
	private String R61_COLUMN_A;
	private BigDecimal R61_COLUMN_B;
	private BigDecimal R61_COLUMN_C;
	private BigDecimal R61_COLUMN_D;
	private BigDecimal R61_COLUMN_E;
	private BigDecimal R61_COLUMN_F;
	private BigDecimal R61_COLUMN_G;
	private BigDecimal R61_COLUMN_H;
	private BigDecimal R61_COLUMN_I;
	private BigDecimal R61_COLUMN_J;
	private BigDecimal R61_COLUMN_K;
	private BigDecimal R61_COLUMN_L;
	private BigDecimal R61_COLUMN_M;
	private BigDecimal R61_COLUMN_N;
	private String R62_COLUMN_A;
	private BigDecimal R62_COLUMN_B;
	private BigDecimal R62_COLUMN_C;
	private BigDecimal R62_COLUMN_D;
	private BigDecimal R62_COLUMN_E;
	private BigDecimal R62_COLUMN_F;
	private BigDecimal R62_COLUMN_G;
	private BigDecimal R62_COLUMN_H;
	private BigDecimal R62_COLUMN_I;
	private BigDecimal R62_COLUMN_J;
	private BigDecimal R62_COLUMN_K;
	private BigDecimal R62_COLUMN_L;
	private BigDecimal R62_COLUMN_M;
	private BigDecimal R62_COLUMN_N;
	private String R63_COLUMN_A;
	private BigDecimal R63_COLUMN_B;
	private BigDecimal R63_COLUMN_C;
	private BigDecimal R63_COLUMN_D;
	private BigDecimal R63_COLUMN_E;
	private BigDecimal R63_COLUMN_F;
	private BigDecimal R63_COLUMN_G;
	private BigDecimal R63_COLUMN_H;
	private BigDecimal R63_COLUMN_I;
	private BigDecimal R63_COLUMN_J;
	private BigDecimal R63_COLUMN_K;
	private BigDecimal R63_COLUMN_L;
	private BigDecimal R63_COLUMN_M;
	private BigDecimal R63_COLUMN_N;
	private String R64_COLUMN_A;
	private BigDecimal R64_COLUMN_B;
	private BigDecimal R64_COLUMN_C;
	private BigDecimal R64_COLUMN_D;
	private BigDecimal R64_COLUMN_E;
	private BigDecimal R64_COLUMN_F;
	private BigDecimal R64_COLUMN_G;
	private BigDecimal R64_COLUMN_H;
	private BigDecimal R64_COLUMN_I;
	private BigDecimal R64_COLUMN_J;
	private BigDecimal R64_COLUMN_K;
	private BigDecimal R64_COLUMN_L;
	private BigDecimal R64_COLUMN_M;
	private BigDecimal R64_COLUMN_N;
	private String R65_COLUMN_A;
	private BigDecimal R65_COLUMN_B;
	private BigDecimal R65_COLUMN_C;
	private BigDecimal R65_COLUMN_D;
	private BigDecimal R65_COLUMN_E;
	private BigDecimal R65_COLUMN_F;
	private BigDecimal R65_COLUMN_G;
	private BigDecimal R65_COLUMN_H;
	private BigDecimal R65_COLUMN_I;
	private BigDecimal R65_COLUMN_J;
	private BigDecimal R65_COLUMN_K;
	private BigDecimal R65_COLUMN_L;
	private BigDecimal R65_COLUMN_M;
	private BigDecimal R65_COLUMN_N;
	private String R66_COLUMN_A;
	private BigDecimal R66_COLUMN_B;
	private BigDecimal R66_COLUMN_C;
	private BigDecimal R66_COLUMN_D;
	private BigDecimal R66_COLUMN_E;
	private BigDecimal R66_COLUMN_F;
	private BigDecimal R66_COLUMN_G;
	private BigDecimal R66_COLUMN_H;
	private BigDecimal R66_COLUMN_I;
	private BigDecimal R66_COLUMN_J;
	private BigDecimal R66_COLUMN_K;
	private BigDecimal R66_COLUMN_L;
	private BigDecimal R66_COLUMN_M;
	private BigDecimal R66_COLUMN_N;
	private String R67_COLUMN_A;
	private BigDecimal R67_COLUMN_B;
	private BigDecimal R67_COLUMN_C;
	private BigDecimal R67_COLUMN_D;
	private BigDecimal R67_COLUMN_E;
	private BigDecimal R67_COLUMN_F;
	private BigDecimal R67_COLUMN_G;
	private BigDecimal R67_COLUMN_H;
	private BigDecimal R67_COLUMN_I;
	private BigDecimal R67_COLUMN_J;
	private BigDecimal R67_COLUMN_K;
	private BigDecimal R67_COLUMN_L;
	private BigDecimal R67_COLUMN_M;
	private BigDecimal R67_COLUMN_N;
	private String R68_COLUMN_A;
	private BigDecimal R68_COLUMN_B;
	private BigDecimal R68_COLUMN_C;
	private BigDecimal R68_COLUMN_D;
	private BigDecimal R68_COLUMN_E;
	private BigDecimal R68_COLUMN_F;
	private BigDecimal R68_COLUMN_G;
	private BigDecimal R68_COLUMN_H;
	private BigDecimal R68_COLUMN_I;
	private BigDecimal R68_COLUMN_J;
	private BigDecimal R68_COLUMN_K;
	private BigDecimal R68_COLUMN_L;
	private BigDecimal R68_COLUMN_M;
	private BigDecimal R68_COLUMN_N;
	private String R69_COLUMN_A;
	private BigDecimal R69_COLUMN_B;
	private BigDecimal R69_COLUMN_C;
	private BigDecimal R69_COLUMN_D;
	private BigDecimal R69_COLUMN_E;
	private BigDecimal R69_COLUMN_F;
	private BigDecimal R69_COLUMN_G;
	private BigDecimal R69_COLUMN_H;
	private BigDecimal R69_COLUMN_I;
	private BigDecimal R69_COLUMN_J;
	private BigDecimal R69_COLUMN_K;
	private BigDecimal R69_COLUMN_L;
	private BigDecimal R69_COLUMN_M;
	private BigDecimal R69_COLUMN_N;
	private String R70_COLUMN_A;
	private BigDecimal R70_COLUMN_B;
	private BigDecimal R70_COLUMN_C;
	private BigDecimal R70_COLUMN_D;
	private BigDecimal R70_COLUMN_E;
	private BigDecimal R70_COLUMN_F;
	private BigDecimal R70_COLUMN_G;
	private BigDecimal R70_COLUMN_H;
	private BigDecimal R70_COLUMN_I;
	private BigDecimal R70_COLUMN_J;
	private BigDecimal R70_COLUMN_K;
	private BigDecimal R70_COLUMN_L;
	private BigDecimal R70_COLUMN_M;
	private BigDecimal R70_COLUMN_N;
	private Date REPORT_DATE;
	private BigDecimal REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public String getR1_COLUMN_A() {
		return R1_COLUMN_A;
	}

	public void setR1_COLUMN_A(String R1_COLUMN_A) {
		this.R1_COLUMN_A = R1_COLUMN_A;
	}

	public BigDecimal getR1_COLUMN_B() {
		return R1_COLUMN_B;
	}

	public void setR1_COLUMN_B(BigDecimal R1_COLUMN_B) {
		this.R1_COLUMN_B = R1_COLUMN_B;
	}

	public BigDecimal getR1_COLUMN_C() {
		return R1_COLUMN_C;
	}

	public void setR1_COLUMN_C(BigDecimal R1_COLUMN_C) {
		this.R1_COLUMN_C = R1_COLUMN_C;
	}

	public BigDecimal getR1_COLUMN_D() {
		return R1_COLUMN_D;
	}

	public void setR1_COLUMN_D(BigDecimal R1_COLUMN_D) {
		this.R1_COLUMN_D = R1_COLUMN_D;
	}

	public BigDecimal getR1_COLUMN_E() {
		return R1_COLUMN_E;
	}

	public void setR1_COLUMN_E(BigDecimal R1_COLUMN_E) {
		this.R1_COLUMN_E = R1_COLUMN_E;
	}

	public BigDecimal getR1_COLUMN_F() {
		return R1_COLUMN_F;
	}

	public void setR1_COLUMN_F(BigDecimal R1_COLUMN_F) {
		this.R1_COLUMN_F = R1_COLUMN_F;
	}

	public BigDecimal getR1_COLUMN_G() {
		return R1_COLUMN_G;
	}

	public void setR1_COLUMN_G(BigDecimal R1_COLUMN_G) {
		this.R1_COLUMN_G = R1_COLUMN_G;
	}

	public BigDecimal getR1_COLUMN_H() {
		return R1_COLUMN_H;
	}

	public void setR1_COLUMN_H(BigDecimal R1_COLUMN_H) {
		this.R1_COLUMN_H = R1_COLUMN_H;
	}

	public BigDecimal getR1_COLUMN_I() {
		return R1_COLUMN_I;
	}

	public void setR1_COLUMN_I(BigDecimal R1_COLUMN_I) {
		this.R1_COLUMN_I = R1_COLUMN_I;
	}

	public BigDecimal getR1_COLUMN_J() {
		return R1_COLUMN_J;
	}

	public void setR1_COLUMN_J(BigDecimal R1_COLUMN_J) {
		this.R1_COLUMN_J = R1_COLUMN_J;
	}

	public BigDecimal getR1_COLUMN_K() {
		return R1_COLUMN_K;
	}

	public void setR1_COLUMN_K(BigDecimal R1_COLUMN_K) {
		this.R1_COLUMN_K = R1_COLUMN_K;
	}

	public BigDecimal getR1_COLUMN_L() {
		return R1_COLUMN_L;
	}

	public void setR1_COLUMN_L(BigDecimal R1_COLUMN_L) {
		this.R1_COLUMN_L = R1_COLUMN_L;
	}

	public BigDecimal getR1_COLUMN_M() {
		return R1_COLUMN_M;
	}

	public void setR1_COLUMN_M(BigDecimal R1_COLUMN_M) {
		this.R1_COLUMN_M = R1_COLUMN_M;
	}

	public BigDecimal getR1_COLUMN_N() {
		return R1_COLUMN_N;
	}

	public void setR1_COLUMN_N(BigDecimal R1_COLUMN_N) {
		this.R1_COLUMN_N = R1_COLUMN_N;
	}

	public String getR2_COLUMN_A() {
		return R2_COLUMN_A;
	}

	public void setR2_COLUMN_A(String R2_COLUMN_A) {
		this.R2_COLUMN_A = R2_COLUMN_A;
	}

	public BigDecimal getR2_COLUMN_B() {
		return R2_COLUMN_B;
	}

	public void setR2_COLUMN_B(BigDecimal R2_COLUMN_B) {
		this.R2_COLUMN_B = R2_COLUMN_B;
	}

	public BigDecimal getR2_COLUMN_C() {
		return R2_COLUMN_C;
	}

	public void setR2_COLUMN_C(BigDecimal R2_COLUMN_C) {
		this.R2_COLUMN_C = R2_COLUMN_C;
	}

	public BigDecimal getR2_COLUMN_D() {
		return R2_COLUMN_D;
	}

	public void setR2_COLUMN_D(BigDecimal R2_COLUMN_D) {
		this.R2_COLUMN_D = R2_COLUMN_D;
	}

	public BigDecimal getR2_COLUMN_E() {
		return R2_COLUMN_E;
	}

	public void setR2_COLUMN_E(BigDecimal R2_COLUMN_E) {
		this.R2_COLUMN_E = R2_COLUMN_E;
	}

	public BigDecimal getR2_COLUMN_F() {
		return R2_COLUMN_F;
	}

	public void setR2_COLUMN_F(BigDecimal R2_COLUMN_F) {
		this.R2_COLUMN_F = R2_COLUMN_F;
	}

	public BigDecimal getR2_COLUMN_G() {
		return R2_COLUMN_G;
	}

	public void setR2_COLUMN_G(BigDecimal R2_COLUMN_G) {
		this.R2_COLUMN_G = R2_COLUMN_G;
	}

	public BigDecimal getR2_COLUMN_H() {
		return R2_COLUMN_H;
	}

	public void setR2_COLUMN_H(BigDecimal R2_COLUMN_H) {
		this.R2_COLUMN_H = R2_COLUMN_H;
	}

	public BigDecimal getR2_COLUMN_I() {
		return R2_COLUMN_I;
	}

	public void setR2_COLUMN_I(BigDecimal R2_COLUMN_I) {
		this.R2_COLUMN_I = R2_COLUMN_I;
	}

	public BigDecimal getR2_COLUMN_J() {
		return R2_COLUMN_J;
	}

	public void setR2_COLUMN_J(BigDecimal R2_COLUMN_J) {
		this.R2_COLUMN_J = R2_COLUMN_J;
	}

	public BigDecimal getR2_COLUMN_K() {
		return R2_COLUMN_K;
	}

	public void setR2_COLUMN_K(BigDecimal R2_COLUMN_K) {
		this.R2_COLUMN_K = R2_COLUMN_K;
	}

	public BigDecimal getR2_COLUMN_L() {
		return R2_COLUMN_L;
	}

	public void setR2_COLUMN_L(BigDecimal R2_COLUMN_L) {
		this.R2_COLUMN_L = R2_COLUMN_L;
	}

	public BigDecimal getR2_COLUMN_M() {
		return R2_COLUMN_M;
	}

	public void setR2_COLUMN_M(BigDecimal R2_COLUMN_M) {
		this.R2_COLUMN_M = R2_COLUMN_M;
	}

	public BigDecimal getR2_COLUMN_N() {
		return R2_COLUMN_N;
	}

	public void setR2_COLUMN_N(BigDecimal R2_COLUMN_N) {
		this.R2_COLUMN_N = R2_COLUMN_N;
	}

	public String getR3_COLUMN_A() {
		return R3_COLUMN_A;
	}

	public void setR3_COLUMN_A(String R3_COLUMN_A) {
		this.R3_COLUMN_A = R3_COLUMN_A;
	}

	public BigDecimal getR3_COLUMN_B() {
		return R3_COLUMN_B;
	}

	public void setR3_COLUMN_B(BigDecimal R3_COLUMN_B) {
		this.R3_COLUMN_B = R3_COLUMN_B;
	}

	public BigDecimal getR3_COLUMN_C() {
		return R3_COLUMN_C;
	}

	public void setR3_COLUMN_C(BigDecimal R3_COLUMN_C) {
		this.R3_COLUMN_C = R3_COLUMN_C;
	}

	public BigDecimal getR3_COLUMN_D() {
		return R3_COLUMN_D;
	}

	public void setR3_COLUMN_D(BigDecimal R3_COLUMN_D) {
		this.R3_COLUMN_D = R3_COLUMN_D;
	}

	public BigDecimal getR3_COLUMN_E() {
		return R3_COLUMN_E;
	}

	public void setR3_COLUMN_E(BigDecimal R3_COLUMN_E) {
		this.R3_COLUMN_E = R3_COLUMN_E;
	}

	public BigDecimal getR3_COLUMN_F() {
		return R3_COLUMN_F;
	}

	public void setR3_COLUMN_F(BigDecimal R3_COLUMN_F) {
		this.R3_COLUMN_F = R3_COLUMN_F;
	}

	public BigDecimal getR3_COLUMN_G() {
		return R3_COLUMN_G;
	}

	public void setR3_COLUMN_G(BigDecimal R3_COLUMN_G) {
		this.R3_COLUMN_G = R3_COLUMN_G;
	}

	public BigDecimal getR3_COLUMN_H() {
		return R3_COLUMN_H;
	}

	public void setR3_COLUMN_H(BigDecimal R3_COLUMN_H) {
		this.R3_COLUMN_H = R3_COLUMN_H;
	}

	public BigDecimal getR3_COLUMN_I() {
		return R3_COLUMN_I;
	}

	public void setR3_COLUMN_I(BigDecimal R3_COLUMN_I) {
		this.R3_COLUMN_I = R3_COLUMN_I;
	}

	public BigDecimal getR3_COLUMN_J() {
		return R3_COLUMN_J;
	}

	public void setR3_COLUMN_J(BigDecimal R3_COLUMN_J) {
		this.R3_COLUMN_J = R3_COLUMN_J;
	}

	public BigDecimal getR3_COLUMN_K() {
		return R3_COLUMN_K;
	}

	public void setR3_COLUMN_K(BigDecimal R3_COLUMN_K) {
		this.R3_COLUMN_K = R3_COLUMN_K;
	}

	public BigDecimal getR3_COLUMN_L() {
		return R3_COLUMN_L;
	}

	public void setR3_COLUMN_L(BigDecimal R3_COLUMN_L) {
		this.R3_COLUMN_L = R3_COLUMN_L;
	}

	public BigDecimal getR3_COLUMN_M() {
		return R3_COLUMN_M;
	}

	public void setR3_COLUMN_M(BigDecimal R3_COLUMN_M) {
		this.R3_COLUMN_M = R3_COLUMN_M;
	}

	public BigDecimal getR3_COLUMN_N() {
		return R3_COLUMN_N;
	}

	public void setR3_COLUMN_N(BigDecimal R3_COLUMN_N) {
		this.R3_COLUMN_N = R3_COLUMN_N;
	}

	public String getR4_COLUMN_A() {
		return R4_COLUMN_A;
	}

	public void setR4_COLUMN_A(String R4_COLUMN_A) {
		this.R4_COLUMN_A = R4_COLUMN_A;
	}

	public BigDecimal getR4_COLUMN_B() {
		return R4_COLUMN_B;
	}

	public void setR4_COLUMN_B(BigDecimal R4_COLUMN_B) {
		this.R4_COLUMN_B = R4_COLUMN_B;
	}

	public BigDecimal getR4_COLUMN_C() {
		return R4_COLUMN_C;
	}

	public void setR4_COLUMN_C(BigDecimal R4_COLUMN_C) {
		this.R4_COLUMN_C = R4_COLUMN_C;
	}

	public BigDecimal getR4_COLUMN_D() {
		return R4_COLUMN_D;
	}

	public void setR4_COLUMN_D(BigDecimal R4_COLUMN_D) {
		this.R4_COLUMN_D = R4_COLUMN_D;
	}

	public BigDecimal getR4_COLUMN_E() {
		return R4_COLUMN_E;
	}

	public void setR4_COLUMN_E(BigDecimal R4_COLUMN_E) {
		this.R4_COLUMN_E = R4_COLUMN_E;
	}

	public BigDecimal getR4_COLUMN_F() {
		return R4_COLUMN_F;
	}

	public void setR4_COLUMN_F(BigDecimal R4_COLUMN_F) {
		this.R4_COLUMN_F = R4_COLUMN_F;
	}

	public BigDecimal getR4_COLUMN_G() {
		return R4_COLUMN_G;
	}

	public void setR4_COLUMN_G(BigDecimal R4_COLUMN_G) {
		this.R4_COLUMN_G = R4_COLUMN_G;
	}

	public BigDecimal getR4_COLUMN_H() {
		return R4_COLUMN_H;
	}

	public void setR4_COLUMN_H(BigDecimal R4_COLUMN_H) {
		this.R4_COLUMN_H = R4_COLUMN_H;
	}

	public BigDecimal getR4_COLUMN_I() {
		return R4_COLUMN_I;
	}

	public void setR4_COLUMN_I(BigDecimal R4_COLUMN_I) {
		this.R4_COLUMN_I = R4_COLUMN_I;
	}

	public BigDecimal getR4_COLUMN_J() {
		return R4_COLUMN_J;
	}

	public void setR4_COLUMN_J(BigDecimal R4_COLUMN_J) {
		this.R4_COLUMN_J = R4_COLUMN_J;
	}

	public BigDecimal getR4_COLUMN_K() {
		return R4_COLUMN_K;
	}

	public void setR4_COLUMN_K(BigDecimal R4_COLUMN_K) {
		this.R4_COLUMN_K = R4_COLUMN_K;
	}

	public BigDecimal getR4_COLUMN_L() {
		return R4_COLUMN_L;
	}

	public void setR4_COLUMN_L(BigDecimal R4_COLUMN_L) {
		this.R4_COLUMN_L = R4_COLUMN_L;
	}

	public BigDecimal getR4_COLUMN_M() {
		return R4_COLUMN_M;
	}

	public void setR4_COLUMN_M(BigDecimal R4_COLUMN_M) {
		this.R4_COLUMN_M = R4_COLUMN_M;
	}

	public BigDecimal getR4_COLUMN_N() {
		return R4_COLUMN_N;
	}

	public void setR4_COLUMN_N(BigDecimal R4_COLUMN_N) {
		this.R4_COLUMN_N = R4_COLUMN_N;
	}

	public String getR5_COLUMN_A() {
		return R5_COLUMN_A;
	}

	public void setR5_COLUMN_A(String R5_COLUMN_A) {
		this.R5_COLUMN_A = R5_COLUMN_A;
	}

	public BigDecimal getR5_COLUMN_B() {
		return R5_COLUMN_B;
	}

	public void setR5_COLUMN_B(BigDecimal R5_COLUMN_B) {
		this.R5_COLUMN_B = R5_COLUMN_B;
	}

	public BigDecimal getR5_COLUMN_C() {
		return R5_COLUMN_C;
	}

	public void setR5_COLUMN_C(BigDecimal R5_COLUMN_C) {
		this.R5_COLUMN_C = R5_COLUMN_C;
	}

	public BigDecimal getR5_COLUMN_D() {
		return R5_COLUMN_D;
	}

	public void setR5_COLUMN_D(BigDecimal R5_COLUMN_D) {
		this.R5_COLUMN_D = R5_COLUMN_D;
	}

	public BigDecimal getR5_COLUMN_E() {
		return R5_COLUMN_E;
	}

	public void setR5_COLUMN_E(BigDecimal R5_COLUMN_E) {
		this.R5_COLUMN_E = R5_COLUMN_E;
	}

	public BigDecimal getR5_COLUMN_F() {
		return R5_COLUMN_F;
	}

	public void setR5_COLUMN_F(BigDecimal R5_COLUMN_F) {
		this.R5_COLUMN_F = R5_COLUMN_F;
	}

	public BigDecimal getR5_COLUMN_G() {
		return R5_COLUMN_G;
	}

	public void setR5_COLUMN_G(BigDecimal R5_COLUMN_G) {
		this.R5_COLUMN_G = R5_COLUMN_G;
	}

	public BigDecimal getR5_COLUMN_H() {
		return R5_COLUMN_H;
	}

	public void setR5_COLUMN_H(BigDecimal R5_COLUMN_H) {
		this.R5_COLUMN_H = R5_COLUMN_H;
	}

	public BigDecimal getR5_COLUMN_I() {
		return R5_COLUMN_I;
	}

	public void setR5_COLUMN_I(BigDecimal R5_COLUMN_I) {
		this.R5_COLUMN_I = R5_COLUMN_I;
	}

	public BigDecimal getR5_COLUMN_J() {
		return R5_COLUMN_J;
	}

	public void setR5_COLUMN_J(BigDecimal R5_COLUMN_J) {
		this.R5_COLUMN_J = R5_COLUMN_J;
	}

	public BigDecimal getR5_COLUMN_K() {
		return R5_COLUMN_K;
	}

	public void setR5_COLUMN_K(BigDecimal R5_COLUMN_K) {
		this.R5_COLUMN_K = R5_COLUMN_K;
	}

	public BigDecimal getR5_COLUMN_L() {
		return R5_COLUMN_L;
	}

	public void setR5_COLUMN_L(BigDecimal R5_COLUMN_L) {
		this.R5_COLUMN_L = R5_COLUMN_L;
	}

	public BigDecimal getR5_COLUMN_M() {
		return R5_COLUMN_M;
	}

	public void setR5_COLUMN_M(BigDecimal R5_COLUMN_M) {
		this.R5_COLUMN_M = R5_COLUMN_M;
	}

	public BigDecimal getR5_COLUMN_N() {
		return R5_COLUMN_N;
	}

	public void setR5_COLUMN_N(BigDecimal R5_COLUMN_N) {
		this.R5_COLUMN_N = R5_COLUMN_N;
	}

	public String getR6_COLUMN_A() {
		return R6_COLUMN_A;
	}

	public void setR6_COLUMN_A(String R6_COLUMN_A) {
		this.R6_COLUMN_A = R6_COLUMN_A;
	}

	public BigDecimal getR6_COLUMN_B() {
		return R6_COLUMN_B;
	}

	public void setR6_COLUMN_B(BigDecimal R6_COLUMN_B) {
		this.R6_COLUMN_B = R6_COLUMN_B;
	}

	public BigDecimal getR6_COLUMN_C() {
		return R6_COLUMN_C;
	}

	public void setR6_COLUMN_C(BigDecimal R6_COLUMN_C) {
		this.R6_COLUMN_C = R6_COLUMN_C;
	}

	public BigDecimal getR6_COLUMN_D() {
		return R6_COLUMN_D;
	}

	public void setR6_COLUMN_D(BigDecimal R6_COLUMN_D) {
		this.R6_COLUMN_D = R6_COLUMN_D;
	}

	public BigDecimal getR6_COLUMN_E() {
		return R6_COLUMN_E;
	}

	public void setR6_COLUMN_E(BigDecimal R6_COLUMN_E) {
		this.R6_COLUMN_E = R6_COLUMN_E;
	}

	public BigDecimal getR6_COLUMN_F() {
		return R6_COLUMN_F;
	}

	public void setR6_COLUMN_F(BigDecimal R6_COLUMN_F) {
		this.R6_COLUMN_F = R6_COLUMN_F;
	}

	public BigDecimal getR6_COLUMN_G() {
		return R6_COLUMN_G;
	}

	public void setR6_COLUMN_G(BigDecimal R6_COLUMN_G) {
		this.R6_COLUMN_G = R6_COLUMN_G;
	}

	public BigDecimal getR6_COLUMN_H() {
		return R6_COLUMN_H;
	}

	public void setR6_COLUMN_H(BigDecimal R6_COLUMN_H) {
		this.R6_COLUMN_H = R6_COLUMN_H;
	}

	public BigDecimal getR6_COLUMN_I() {
		return R6_COLUMN_I;
	}

	public void setR6_COLUMN_I(BigDecimal R6_COLUMN_I) {
		this.R6_COLUMN_I = R6_COLUMN_I;
	}

	public BigDecimal getR6_COLUMN_J() {
		return R6_COLUMN_J;
	}

	public void setR6_COLUMN_J(BigDecimal R6_COLUMN_J) {
		this.R6_COLUMN_J = R6_COLUMN_J;
	}

	public BigDecimal getR6_COLUMN_K() {
		return R6_COLUMN_K;
	}

	public void setR6_COLUMN_K(BigDecimal R6_COLUMN_K) {
		this.R6_COLUMN_K = R6_COLUMN_K;
	}

	public BigDecimal getR6_COLUMN_L() {
		return R6_COLUMN_L;
	}

	public void setR6_COLUMN_L(BigDecimal R6_COLUMN_L) {
		this.R6_COLUMN_L = R6_COLUMN_L;
	}

	public BigDecimal getR6_COLUMN_M() {
		return R6_COLUMN_M;
	}

	public void setR6_COLUMN_M(BigDecimal R6_COLUMN_M) {
		this.R6_COLUMN_M = R6_COLUMN_M;
	}

	public BigDecimal getR6_COLUMN_N() {
		return R6_COLUMN_N;
	}

	public void setR6_COLUMN_N(BigDecimal R6_COLUMN_N) {
		this.R6_COLUMN_N = R6_COLUMN_N;
	}

	public String getR7_COLUMN_A() {
		return R7_COLUMN_A;
	}

	public void setR7_COLUMN_A(String R7_COLUMN_A) {
		this.R7_COLUMN_A = R7_COLUMN_A;
	}

	public BigDecimal getR7_COLUMN_B() {
		return R7_COLUMN_B;
	}

	public void setR7_COLUMN_B(BigDecimal R7_COLUMN_B) {
		this.R7_COLUMN_B = R7_COLUMN_B;
	}

	public BigDecimal getR7_COLUMN_C() {
		return R7_COLUMN_C;
	}

	public void setR7_COLUMN_C(BigDecimal R7_COLUMN_C) {
		this.R7_COLUMN_C = R7_COLUMN_C;
	}

	public BigDecimal getR7_COLUMN_D() {
		return R7_COLUMN_D;
	}

	public void setR7_COLUMN_D(BigDecimal R7_COLUMN_D) {
		this.R7_COLUMN_D = R7_COLUMN_D;
	}

	public BigDecimal getR7_COLUMN_E() {
		return R7_COLUMN_E;
	}

	public void setR7_COLUMN_E(BigDecimal R7_COLUMN_E) {
		this.R7_COLUMN_E = R7_COLUMN_E;
	}

	public BigDecimal getR7_COLUMN_F() {
		return R7_COLUMN_F;
	}

	public void setR7_COLUMN_F(BigDecimal R7_COLUMN_F) {
		this.R7_COLUMN_F = R7_COLUMN_F;
	}

	public BigDecimal getR7_COLUMN_G() {
		return R7_COLUMN_G;
	}

	public void setR7_COLUMN_G(BigDecimal R7_COLUMN_G) {
		this.R7_COLUMN_G = R7_COLUMN_G;
	}

	public BigDecimal getR7_COLUMN_H() {
		return R7_COLUMN_H;
	}

	public void setR7_COLUMN_H(BigDecimal R7_COLUMN_H) {
		this.R7_COLUMN_H = R7_COLUMN_H;
	}

	public BigDecimal getR7_COLUMN_I() {
		return R7_COLUMN_I;
	}

	public void setR7_COLUMN_I(BigDecimal R7_COLUMN_I) {
		this.R7_COLUMN_I = R7_COLUMN_I;
	}

	public BigDecimal getR7_COLUMN_J() {
		return R7_COLUMN_J;
	}

	public void setR7_COLUMN_J(BigDecimal R7_COLUMN_J) {
		this.R7_COLUMN_J = R7_COLUMN_J;
	}

	public BigDecimal getR7_COLUMN_K() {
		return R7_COLUMN_K;
	}

	public void setR7_COLUMN_K(BigDecimal R7_COLUMN_K) {
		this.R7_COLUMN_K = R7_COLUMN_K;
	}

	public BigDecimal getR7_COLUMN_L() {
		return R7_COLUMN_L;
	}

	public void setR7_COLUMN_L(BigDecimal R7_COLUMN_L) {
		this.R7_COLUMN_L = R7_COLUMN_L;
	}

	public BigDecimal getR7_COLUMN_M() {
		return R7_COLUMN_M;
	}

	public void setR7_COLUMN_M(BigDecimal R7_COLUMN_M) {
		this.R7_COLUMN_M = R7_COLUMN_M;
	}

	public BigDecimal getR7_COLUMN_N() {
		return R7_COLUMN_N;
	}

	public void setR7_COLUMN_N(BigDecimal R7_COLUMN_N) {
		this.R7_COLUMN_N = R7_COLUMN_N;
	}

	public String getR8_COLUMN_A() {
		return R8_COLUMN_A;
	}

	public void setR8_COLUMN_A(String R8_COLUMN_A) {
		this.R8_COLUMN_A = R8_COLUMN_A;
	}

	public BigDecimal getR8_COLUMN_B() {
		return R8_COLUMN_B;
	}

	public void setR8_COLUMN_B(BigDecimal R8_COLUMN_B) {
		this.R8_COLUMN_B = R8_COLUMN_B;
	}

	public BigDecimal getR8_COLUMN_C() {
		return R8_COLUMN_C;
	}

	public void setR8_COLUMN_C(BigDecimal R8_COLUMN_C) {
		this.R8_COLUMN_C = R8_COLUMN_C;
	}

	public BigDecimal getR8_COLUMN_D() {
		return R8_COLUMN_D;
	}

	public void setR8_COLUMN_D(BigDecimal R8_COLUMN_D) {
		this.R8_COLUMN_D = R8_COLUMN_D;
	}

	public BigDecimal getR8_COLUMN_E() {
		return R8_COLUMN_E;
	}

	public void setR8_COLUMN_E(BigDecimal R8_COLUMN_E) {
		this.R8_COLUMN_E = R8_COLUMN_E;
	}

	public BigDecimal getR8_COLUMN_F() {
		return R8_COLUMN_F;
	}

	public void setR8_COLUMN_F(BigDecimal R8_COLUMN_F) {
		this.R8_COLUMN_F = R8_COLUMN_F;
	}

	public BigDecimal getR8_COLUMN_G() {
		return R8_COLUMN_G;
	}

	public void setR8_COLUMN_G(BigDecimal R8_COLUMN_G) {
		this.R8_COLUMN_G = R8_COLUMN_G;
	}

	public BigDecimal getR8_COLUMN_H() {
		return R8_COLUMN_H;
	}

	public void setR8_COLUMN_H(BigDecimal R8_COLUMN_H) {
		this.R8_COLUMN_H = R8_COLUMN_H;
	}

	public BigDecimal getR8_COLUMN_I() {
		return R8_COLUMN_I;
	}

	public void setR8_COLUMN_I(BigDecimal R8_COLUMN_I) {
		this.R8_COLUMN_I = R8_COLUMN_I;
	}

	public BigDecimal getR8_COLUMN_J() {
		return R8_COLUMN_J;
	}

	public void setR8_COLUMN_J(BigDecimal R8_COLUMN_J) {
		this.R8_COLUMN_J = R8_COLUMN_J;
	}

	public BigDecimal getR8_COLUMN_K() {
		return R8_COLUMN_K;
	}

	public void setR8_COLUMN_K(BigDecimal R8_COLUMN_K) {
		this.R8_COLUMN_K = R8_COLUMN_K;
	}

	public BigDecimal getR8_COLUMN_L() {
		return R8_COLUMN_L;
	}

	public void setR8_COLUMN_L(BigDecimal R8_COLUMN_L) {
		this.R8_COLUMN_L = R8_COLUMN_L;
	}

	public BigDecimal getR8_COLUMN_M() {
		return R8_COLUMN_M;
	}

	public void setR8_COLUMN_M(BigDecimal R8_COLUMN_M) {
		this.R8_COLUMN_M = R8_COLUMN_M;
	}

	public BigDecimal getR8_COLUMN_N() {
		return R8_COLUMN_N;
	}

	public void setR8_COLUMN_N(BigDecimal R8_COLUMN_N) {
		this.R8_COLUMN_N = R8_COLUMN_N;
	}

	public String getR9_COLUMN_A() {
		return R9_COLUMN_A;
	}

	public void setR9_COLUMN_A(String R9_COLUMN_A) {
		this.R9_COLUMN_A = R9_COLUMN_A;
	}

	public BigDecimal getR9_COLUMN_B() {
		return R9_COLUMN_B;
	}

	public void setR9_COLUMN_B(BigDecimal R9_COLUMN_B) {
		this.R9_COLUMN_B = R9_COLUMN_B;
	}

	public BigDecimal getR9_COLUMN_C() {
		return R9_COLUMN_C;
	}

	public void setR9_COLUMN_C(BigDecimal R9_COLUMN_C) {
		this.R9_COLUMN_C = R9_COLUMN_C;
	}

	public BigDecimal getR9_COLUMN_D() {
		return R9_COLUMN_D;
	}

	public void setR9_COLUMN_D(BigDecimal R9_COLUMN_D) {
		this.R9_COLUMN_D = R9_COLUMN_D;
	}

	public BigDecimal getR9_COLUMN_E() {
		return R9_COLUMN_E;
	}

	public void setR9_COLUMN_E(BigDecimal R9_COLUMN_E) {
		this.R9_COLUMN_E = R9_COLUMN_E;
	}

	public BigDecimal getR9_COLUMN_F() {
		return R9_COLUMN_F;
	}

	public void setR9_COLUMN_F(BigDecimal R9_COLUMN_F) {
		this.R9_COLUMN_F = R9_COLUMN_F;
	}

	public BigDecimal getR9_COLUMN_G() {
		return R9_COLUMN_G;
	}

	public void setR9_COLUMN_G(BigDecimal R9_COLUMN_G) {
		this.R9_COLUMN_G = R9_COLUMN_G;
	}

	public BigDecimal getR9_COLUMN_H() {
		return R9_COLUMN_H;
	}

	public void setR9_COLUMN_H(BigDecimal R9_COLUMN_H) {
		this.R9_COLUMN_H = R9_COLUMN_H;
	}

	public BigDecimal getR9_COLUMN_I() {
		return R9_COLUMN_I;
	}

	public void setR9_COLUMN_I(BigDecimal R9_COLUMN_I) {
		this.R9_COLUMN_I = R9_COLUMN_I;
	}

	public BigDecimal getR9_COLUMN_J() {
		return R9_COLUMN_J;
	}

	public void setR9_COLUMN_J(BigDecimal R9_COLUMN_J) {
		this.R9_COLUMN_J = R9_COLUMN_J;
	}

	public BigDecimal getR9_COLUMN_K() {
		return R9_COLUMN_K;
	}

	public void setR9_COLUMN_K(BigDecimal R9_COLUMN_K) {
		this.R9_COLUMN_K = R9_COLUMN_K;
	}

	public BigDecimal getR9_COLUMN_L() {
		return R9_COLUMN_L;
	}

	public void setR9_COLUMN_L(BigDecimal R9_COLUMN_L) {
		this.R9_COLUMN_L = R9_COLUMN_L;
	}

	public BigDecimal getR9_COLUMN_M() {
		return R9_COLUMN_M;
	}

	public void setR9_COLUMN_M(BigDecimal R9_COLUMN_M) {
		this.R9_COLUMN_M = R9_COLUMN_M;
	}

	public BigDecimal getR9_COLUMN_N() {
		return R9_COLUMN_N;
	}

	public void setR9_COLUMN_N(BigDecimal R9_COLUMN_N) {
		this.R9_COLUMN_N = R9_COLUMN_N;
	}

	public String getR10_COLUMN_A() {
		return R10_COLUMN_A;
	}

	public void setR10_COLUMN_A(String R10_COLUMN_A) {
		this.R10_COLUMN_A = R10_COLUMN_A;
	}

	public BigDecimal getR10_COLUMN_B() {
		return R10_COLUMN_B;
	}

	public void setR10_COLUMN_B(BigDecimal R10_COLUMN_B) {
		this.R10_COLUMN_B = R10_COLUMN_B;
	}

	public BigDecimal getR10_COLUMN_C() {
		return R10_COLUMN_C;
	}

	public void setR10_COLUMN_C(BigDecimal R10_COLUMN_C) {
		this.R10_COLUMN_C = R10_COLUMN_C;
	}

	public BigDecimal getR10_COLUMN_D() {
		return R10_COLUMN_D;
	}

	public void setR10_COLUMN_D(BigDecimal R10_COLUMN_D) {
		this.R10_COLUMN_D = R10_COLUMN_D;
	}

	public BigDecimal getR10_COLUMN_E() {
		return R10_COLUMN_E;
	}

	public void setR10_COLUMN_E(BigDecimal R10_COLUMN_E) {
		this.R10_COLUMN_E = R10_COLUMN_E;
	}

	public BigDecimal getR10_COLUMN_F() {
		return R10_COLUMN_F;
	}

	public void setR10_COLUMN_F(BigDecimal R10_COLUMN_F) {
		this.R10_COLUMN_F = R10_COLUMN_F;
	}

	public BigDecimal getR10_COLUMN_G() {
		return R10_COLUMN_G;
	}

	public void setR10_COLUMN_G(BigDecimal R10_COLUMN_G) {
		this.R10_COLUMN_G = R10_COLUMN_G;
	}

	public BigDecimal getR10_COLUMN_H() {
		return R10_COLUMN_H;
	}

	public void setR10_COLUMN_H(BigDecimal R10_COLUMN_H) {
		this.R10_COLUMN_H = R10_COLUMN_H;
	}

	public BigDecimal getR10_COLUMN_I() {
		return R10_COLUMN_I;
	}

	public void setR10_COLUMN_I(BigDecimal R10_COLUMN_I) {
		this.R10_COLUMN_I = R10_COLUMN_I;
	}

	public BigDecimal getR10_COLUMN_J() {
		return R10_COLUMN_J;
	}

	public void setR10_COLUMN_J(BigDecimal R10_COLUMN_J) {
		this.R10_COLUMN_J = R10_COLUMN_J;
	}

	public BigDecimal getR10_COLUMN_K() {
		return R10_COLUMN_K;
	}

	public void setR10_COLUMN_K(BigDecimal R10_COLUMN_K) {
		this.R10_COLUMN_K = R10_COLUMN_K;
	}

	public BigDecimal getR10_COLUMN_L() {
		return R10_COLUMN_L;
	}

	public void setR10_COLUMN_L(BigDecimal R10_COLUMN_L) {
		this.R10_COLUMN_L = R10_COLUMN_L;
	}

	public BigDecimal getR10_COLUMN_M() {
		return R10_COLUMN_M;
	}

	public void setR10_COLUMN_M(BigDecimal R10_COLUMN_M) {
		this.R10_COLUMN_M = R10_COLUMN_M;
	}

	public BigDecimal getR10_COLUMN_N() {
		return R10_COLUMN_N;
	}

	public void setR10_COLUMN_N(BigDecimal R10_COLUMN_N) {
		this.R10_COLUMN_N = R10_COLUMN_N;
	}

	public String getR11_COLUMN_A() {
		return R11_COLUMN_A;
	}

	public void setR11_COLUMN_A(String R11_COLUMN_A) {
		this.R11_COLUMN_A = R11_COLUMN_A;
	}

	public BigDecimal getR11_COLUMN_B() {
		return R11_COLUMN_B;
	}

	public void setR11_COLUMN_B(BigDecimal R11_COLUMN_B) {
		this.R11_COLUMN_B = R11_COLUMN_B;
	}

	public BigDecimal getR11_COLUMN_C() {
		return R11_COLUMN_C;
	}

	public void setR11_COLUMN_C(BigDecimal R11_COLUMN_C) {
		this.R11_COLUMN_C = R11_COLUMN_C;
	}

	public BigDecimal getR11_COLUMN_D() {
		return R11_COLUMN_D;
	}

	public void setR11_COLUMN_D(BigDecimal R11_COLUMN_D) {
		this.R11_COLUMN_D = R11_COLUMN_D;
	}

	public BigDecimal getR11_COLUMN_E() {
		return R11_COLUMN_E;
	}

	public void setR11_COLUMN_E(BigDecimal R11_COLUMN_E) {
		this.R11_COLUMN_E = R11_COLUMN_E;
	}

	public BigDecimal getR11_COLUMN_F() {
		return R11_COLUMN_F;
	}

	public void setR11_COLUMN_F(BigDecimal R11_COLUMN_F) {
		this.R11_COLUMN_F = R11_COLUMN_F;
	}

	public BigDecimal getR11_COLUMN_G() {
		return R11_COLUMN_G;
	}

	public void setR11_COLUMN_G(BigDecimal R11_COLUMN_G) {
		this.R11_COLUMN_G = R11_COLUMN_G;
	}

	public BigDecimal getR11_COLUMN_H() {
		return R11_COLUMN_H;
	}

	public void setR11_COLUMN_H(BigDecimal R11_COLUMN_H) {
		this.R11_COLUMN_H = R11_COLUMN_H;
	}

	public BigDecimal getR11_COLUMN_I() {
		return R11_COLUMN_I;
	}

	public void setR11_COLUMN_I(BigDecimal R11_COLUMN_I) {
		this.R11_COLUMN_I = R11_COLUMN_I;
	}

	public BigDecimal getR11_COLUMN_J() {
		return R11_COLUMN_J;
	}

	public void setR11_COLUMN_J(BigDecimal R11_COLUMN_J) {
		this.R11_COLUMN_J = R11_COLUMN_J;
	}

	public BigDecimal getR11_COLUMN_K() {
		return R11_COLUMN_K;
	}

	public void setR11_COLUMN_K(BigDecimal R11_COLUMN_K) {
		this.R11_COLUMN_K = R11_COLUMN_K;
	}

	public BigDecimal getR11_COLUMN_L() {
		return R11_COLUMN_L;
	}

	public void setR11_COLUMN_L(BigDecimal R11_COLUMN_L) {
		this.R11_COLUMN_L = R11_COLUMN_L;
	}

	public BigDecimal getR11_COLUMN_M() {
		return R11_COLUMN_M;
	}

	public void setR11_COLUMN_M(BigDecimal R11_COLUMN_M) {
		this.R11_COLUMN_M = R11_COLUMN_M;
	}

	public BigDecimal getR11_COLUMN_N() {
		return R11_COLUMN_N;
	}

	public void setR11_COLUMN_N(BigDecimal R11_COLUMN_N) {
		this.R11_COLUMN_N = R11_COLUMN_N;
	}

	public String getR12_COLUMN_A() {
		return R12_COLUMN_A;
	}

	public void setR12_COLUMN_A(String R12_COLUMN_A) {
		this.R12_COLUMN_A = R12_COLUMN_A;
	}

	public BigDecimal getR12_COLUMN_B() {
		return R12_COLUMN_B;
	}

	public void setR12_COLUMN_B(BigDecimal R12_COLUMN_B) {
		this.R12_COLUMN_B = R12_COLUMN_B;
	}

	public BigDecimal getR12_COLUMN_C() {
		return R12_COLUMN_C;
	}

	public void setR12_COLUMN_C(BigDecimal R12_COLUMN_C) {
		this.R12_COLUMN_C = R12_COLUMN_C;
	}

	public BigDecimal getR12_COLUMN_D() {
		return R12_COLUMN_D;
	}

	public void setR12_COLUMN_D(BigDecimal R12_COLUMN_D) {
		this.R12_COLUMN_D = R12_COLUMN_D;
	}

	public BigDecimal getR12_COLUMN_E() {
		return R12_COLUMN_E;
	}

	public void setR12_COLUMN_E(BigDecimal R12_COLUMN_E) {
		this.R12_COLUMN_E = R12_COLUMN_E;
	}

	public BigDecimal getR12_COLUMN_F() {
		return R12_COLUMN_F;
	}

	public void setR12_COLUMN_F(BigDecimal R12_COLUMN_F) {
		this.R12_COLUMN_F = R12_COLUMN_F;
	}

	public BigDecimal getR12_COLUMN_G() {
		return R12_COLUMN_G;
	}

	public void setR12_COLUMN_G(BigDecimal R12_COLUMN_G) {
		this.R12_COLUMN_G = R12_COLUMN_G;
	}

	public BigDecimal getR12_COLUMN_H() {
		return R12_COLUMN_H;
	}

	public void setR12_COLUMN_H(BigDecimal R12_COLUMN_H) {
		this.R12_COLUMN_H = R12_COLUMN_H;
	}

	public BigDecimal getR12_COLUMN_I() {
		return R12_COLUMN_I;
	}

	public void setR12_COLUMN_I(BigDecimal R12_COLUMN_I) {
		this.R12_COLUMN_I = R12_COLUMN_I;
	}

	public BigDecimal getR12_COLUMN_J() {
		return R12_COLUMN_J;
	}

	public void setR12_COLUMN_J(BigDecimal R12_COLUMN_J) {
		this.R12_COLUMN_J = R12_COLUMN_J;
	}

	public BigDecimal getR12_COLUMN_K() {
		return R12_COLUMN_K;
	}

	public void setR12_COLUMN_K(BigDecimal R12_COLUMN_K) {
		this.R12_COLUMN_K = R12_COLUMN_K;
	}

	public BigDecimal getR12_COLUMN_L() {
		return R12_COLUMN_L;
	}

	public void setR12_COLUMN_L(BigDecimal R12_COLUMN_L) {
		this.R12_COLUMN_L = R12_COLUMN_L;
	}

	public BigDecimal getR12_COLUMN_M() {
		return R12_COLUMN_M;
	}

	public void setR12_COLUMN_M(BigDecimal R12_COLUMN_M) {
		this.R12_COLUMN_M = R12_COLUMN_M;
	}

	public BigDecimal getR12_COLUMN_N() {
		return R12_COLUMN_N;
	}

	public void setR12_COLUMN_N(BigDecimal R12_COLUMN_N) {
		this.R12_COLUMN_N = R12_COLUMN_N;
	}

	public String getR13_COLUMN_A() {
		return R13_COLUMN_A;
	}

	public void setR13_COLUMN_A(String R13_COLUMN_A) {
		this.R13_COLUMN_A = R13_COLUMN_A;
	}

	public BigDecimal getR13_COLUMN_B() {
		return R13_COLUMN_B;
	}

	public void setR13_COLUMN_B(BigDecimal R13_COLUMN_B) {
		this.R13_COLUMN_B = R13_COLUMN_B;
	}

	public BigDecimal getR13_COLUMN_C() {
		return R13_COLUMN_C;
	}

	public void setR13_COLUMN_C(BigDecimal R13_COLUMN_C) {
		this.R13_COLUMN_C = R13_COLUMN_C;
	}

	public BigDecimal getR13_COLUMN_D() {
		return R13_COLUMN_D;
	}

	public void setR13_COLUMN_D(BigDecimal R13_COLUMN_D) {
		this.R13_COLUMN_D = R13_COLUMN_D;
	}

	public BigDecimal getR13_COLUMN_E() {
		return R13_COLUMN_E;
	}

	public void setR13_COLUMN_E(BigDecimal R13_COLUMN_E) {
		this.R13_COLUMN_E = R13_COLUMN_E;
	}

	public BigDecimal getR13_COLUMN_F() {
		return R13_COLUMN_F;
	}

	public void setR13_COLUMN_F(BigDecimal R13_COLUMN_F) {
		this.R13_COLUMN_F = R13_COLUMN_F;
	}

	public BigDecimal getR13_COLUMN_G() {
		return R13_COLUMN_G;
	}

	public void setR13_COLUMN_G(BigDecimal R13_COLUMN_G) {
		this.R13_COLUMN_G = R13_COLUMN_G;
	}

	public BigDecimal getR13_COLUMN_H() {
		return R13_COLUMN_H;
	}

	public void setR13_COLUMN_H(BigDecimal R13_COLUMN_H) {
		this.R13_COLUMN_H = R13_COLUMN_H;
	}

	public BigDecimal getR13_COLUMN_I() {
		return R13_COLUMN_I;
	}

	public void setR13_COLUMN_I(BigDecimal R13_COLUMN_I) {
		this.R13_COLUMN_I = R13_COLUMN_I;
	}

	public BigDecimal getR13_COLUMN_J() {
		return R13_COLUMN_J;
	}

	public void setR13_COLUMN_J(BigDecimal R13_COLUMN_J) {
		this.R13_COLUMN_J = R13_COLUMN_J;
	}

	public BigDecimal getR13_COLUMN_K() {
		return R13_COLUMN_K;
	}

	public void setR13_COLUMN_K(BigDecimal R13_COLUMN_K) {
		this.R13_COLUMN_K = R13_COLUMN_K;
	}

	public BigDecimal getR13_COLUMN_L() {
		return R13_COLUMN_L;
	}

	public void setR13_COLUMN_L(BigDecimal R13_COLUMN_L) {
		this.R13_COLUMN_L = R13_COLUMN_L;
	}

	public BigDecimal getR13_COLUMN_M() {
		return R13_COLUMN_M;
	}

	public void setR13_COLUMN_M(BigDecimal R13_COLUMN_M) {
		this.R13_COLUMN_M = R13_COLUMN_M;
	}

	public BigDecimal getR13_COLUMN_N() {
		return R13_COLUMN_N;
	}

	public void setR13_COLUMN_N(BigDecimal R13_COLUMN_N) {
		this.R13_COLUMN_N = R13_COLUMN_N;
	}

	public String getR14_COLUMN_A() {
		return R14_COLUMN_A;
	}

	public void setR14_COLUMN_A(String R14_COLUMN_A) {
		this.R14_COLUMN_A = R14_COLUMN_A;
	}

	public BigDecimal getR14_COLUMN_B() {
		return R14_COLUMN_B;
	}

	public void setR14_COLUMN_B(BigDecimal R14_COLUMN_B) {
		this.R14_COLUMN_B = R14_COLUMN_B;
	}

	public BigDecimal getR14_COLUMN_C() {
		return R14_COLUMN_C;
	}

	public void setR14_COLUMN_C(BigDecimal R14_COLUMN_C) {
		this.R14_COLUMN_C = R14_COLUMN_C;
	}

	public BigDecimal getR14_COLUMN_D() {
		return R14_COLUMN_D;
	}

	public void setR14_COLUMN_D(BigDecimal R14_COLUMN_D) {
		this.R14_COLUMN_D = R14_COLUMN_D;
	}

	public BigDecimal getR14_COLUMN_E() {
		return R14_COLUMN_E;
	}

	public void setR14_COLUMN_E(BigDecimal R14_COLUMN_E) {
		this.R14_COLUMN_E = R14_COLUMN_E;
	}

	public BigDecimal getR14_COLUMN_F() {
		return R14_COLUMN_F;
	}

	public void setR14_COLUMN_F(BigDecimal R14_COLUMN_F) {
		this.R14_COLUMN_F = R14_COLUMN_F;
	}

	public BigDecimal getR14_COLUMN_G() {
		return R14_COLUMN_G;
	}

	public void setR14_COLUMN_G(BigDecimal R14_COLUMN_G) {
		this.R14_COLUMN_G = R14_COLUMN_G;
	}

	public BigDecimal getR14_COLUMN_H() {
		return R14_COLUMN_H;
	}

	public void setR14_COLUMN_H(BigDecimal R14_COLUMN_H) {
		this.R14_COLUMN_H = R14_COLUMN_H;
	}

	public BigDecimal getR14_COLUMN_I() {
		return R14_COLUMN_I;
	}

	public void setR14_COLUMN_I(BigDecimal R14_COLUMN_I) {
		this.R14_COLUMN_I = R14_COLUMN_I;
	}

	public BigDecimal getR14_COLUMN_J() {
		return R14_COLUMN_J;
	}

	public void setR14_COLUMN_J(BigDecimal R14_COLUMN_J) {
		this.R14_COLUMN_J = R14_COLUMN_J;
	}

	public BigDecimal getR14_COLUMN_K() {
		return R14_COLUMN_K;
	}

	public void setR14_COLUMN_K(BigDecimal R14_COLUMN_K) {
		this.R14_COLUMN_K = R14_COLUMN_K;
	}

	public BigDecimal getR14_COLUMN_L() {
		return R14_COLUMN_L;
	}

	public void setR14_COLUMN_L(BigDecimal R14_COLUMN_L) {
		this.R14_COLUMN_L = R14_COLUMN_L;
	}

	public BigDecimal getR14_COLUMN_M() {
		return R14_COLUMN_M;
	}

	public void setR14_COLUMN_M(BigDecimal R14_COLUMN_M) {
		this.R14_COLUMN_M = R14_COLUMN_M;
	}

	public BigDecimal getR14_COLUMN_N() {
		return R14_COLUMN_N;
	}

	public void setR14_COLUMN_N(BigDecimal R14_COLUMN_N) {
		this.R14_COLUMN_N = R14_COLUMN_N;
	}

	public String getR15_COLUMN_A() {
		return R15_COLUMN_A;
	}

	public void setR15_COLUMN_A(String R15_COLUMN_A) {
		this.R15_COLUMN_A = R15_COLUMN_A;
	}

	public BigDecimal getR15_COLUMN_B() {
		return R15_COLUMN_B;
	}

	public void setR15_COLUMN_B(BigDecimal R15_COLUMN_B) {
		this.R15_COLUMN_B = R15_COLUMN_B;
	}

	public BigDecimal getR15_COLUMN_C() {
		return R15_COLUMN_C;
	}

	public void setR15_COLUMN_C(BigDecimal R15_COLUMN_C) {
		this.R15_COLUMN_C = R15_COLUMN_C;
	}

	public BigDecimal getR15_COLUMN_D() {
		return R15_COLUMN_D;
	}

	public void setR15_COLUMN_D(BigDecimal R15_COLUMN_D) {
		this.R15_COLUMN_D = R15_COLUMN_D;
	}

	public BigDecimal getR15_COLUMN_E() {
		return R15_COLUMN_E;
	}

	public void setR15_COLUMN_E(BigDecimal R15_COLUMN_E) {
		this.R15_COLUMN_E = R15_COLUMN_E;
	}

	public BigDecimal getR15_COLUMN_F() {
		return R15_COLUMN_F;
	}

	public void setR15_COLUMN_F(BigDecimal R15_COLUMN_F) {
		this.R15_COLUMN_F = R15_COLUMN_F;
	}

	public BigDecimal getR15_COLUMN_G() {
		return R15_COLUMN_G;
	}

	public void setR15_COLUMN_G(BigDecimal R15_COLUMN_G) {
		this.R15_COLUMN_G = R15_COLUMN_G;
	}

	public BigDecimal getR15_COLUMN_H() {
		return R15_COLUMN_H;
	}

	public void setR15_COLUMN_H(BigDecimal R15_COLUMN_H) {
		this.R15_COLUMN_H = R15_COLUMN_H;
	}

	public BigDecimal getR15_COLUMN_I() {
		return R15_COLUMN_I;
	}

	public void setR15_COLUMN_I(BigDecimal R15_COLUMN_I) {
		this.R15_COLUMN_I = R15_COLUMN_I;
	}

	public BigDecimal getR15_COLUMN_J() {
		return R15_COLUMN_J;
	}

	public void setR15_COLUMN_J(BigDecimal R15_COLUMN_J) {
		this.R15_COLUMN_J = R15_COLUMN_J;
	}

	public BigDecimal getR15_COLUMN_K() {
		return R15_COLUMN_K;
	}

	public void setR15_COLUMN_K(BigDecimal R15_COLUMN_K) {
		this.R15_COLUMN_K = R15_COLUMN_K;
	}

	public BigDecimal getR15_COLUMN_L() {
		return R15_COLUMN_L;
	}

	public void setR15_COLUMN_L(BigDecimal R15_COLUMN_L) {
		this.R15_COLUMN_L = R15_COLUMN_L;
	}

	public BigDecimal getR15_COLUMN_M() {
		return R15_COLUMN_M;
	}

	public void setR15_COLUMN_M(BigDecimal R15_COLUMN_M) {
		this.R15_COLUMN_M = R15_COLUMN_M;
	}

	public BigDecimal getR15_COLUMN_N() {
		return R15_COLUMN_N;
	}

	public void setR15_COLUMN_N(BigDecimal R15_COLUMN_N) {
		this.R15_COLUMN_N = R15_COLUMN_N;
	}

	public String getR16_COLUMN_A() {
		return R16_COLUMN_A;
	}

	public void setR16_COLUMN_A(String R16_COLUMN_A) {
		this.R16_COLUMN_A = R16_COLUMN_A;
	}

	public BigDecimal getR16_COLUMN_B() {
		return R16_COLUMN_B;
	}

	public void setR16_COLUMN_B(BigDecimal R16_COLUMN_B) {
		this.R16_COLUMN_B = R16_COLUMN_B;
	}

	public BigDecimal getR16_COLUMN_C() {
		return R16_COLUMN_C;
	}

	public void setR16_COLUMN_C(BigDecimal R16_COLUMN_C) {
		this.R16_COLUMN_C = R16_COLUMN_C;
	}

	public BigDecimal getR16_COLUMN_D() {
		return R16_COLUMN_D;
	}

	public void setR16_COLUMN_D(BigDecimal R16_COLUMN_D) {
		this.R16_COLUMN_D = R16_COLUMN_D;
	}

	public BigDecimal getR16_COLUMN_E() {
		return R16_COLUMN_E;
	}

	public void setR16_COLUMN_E(BigDecimal R16_COLUMN_E) {
		this.R16_COLUMN_E = R16_COLUMN_E;
	}

	public BigDecimal getR16_COLUMN_F() {
		return R16_COLUMN_F;
	}

	public void setR16_COLUMN_F(BigDecimal R16_COLUMN_F) {
		this.R16_COLUMN_F = R16_COLUMN_F;
	}

	public BigDecimal getR16_COLUMN_G() {
		return R16_COLUMN_G;
	}

	public void setR16_COLUMN_G(BigDecimal R16_COLUMN_G) {
		this.R16_COLUMN_G = R16_COLUMN_G;
	}

	public BigDecimal getR16_COLUMN_H() {
		return R16_COLUMN_H;
	}

	public void setR16_COLUMN_H(BigDecimal R16_COLUMN_H) {
		this.R16_COLUMN_H = R16_COLUMN_H;
	}

	public BigDecimal getR16_COLUMN_I() {
		return R16_COLUMN_I;
	}

	public void setR16_COLUMN_I(BigDecimal R16_COLUMN_I) {
		this.R16_COLUMN_I = R16_COLUMN_I;
	}

	public BigDecimal getR16_COLUMN_J() {
		return R16_COLUMN_J;
	}

	public void setR16_COLUMN_J(BigDecimal R16_COLUMN_J) {
		this.R16_COLUMN_J = R16_COLUMN_J;
	}

	public BigDecimal getR16_COLUMN_K() {
		return R16_COLUMN_K;
	}

	public void setR16_COLUMN_K(BigDecimal R16_COLUMN_K) {
		this.R16_COLUMN_K = R16_COLUMN_K;
	}

	public BigDecimal getR16_COLUMN_L() {
		return R16_COLUMN_L;
	}

	public void setR16_COLUMN_L(BigDecimal R16_COLUMN_L) {
		this.R16_COLUMN_L = R16_COLUMN_L;
	}

	public BigDecimal getR16_COLUMN_M() {
		return R16_COLUMN_M;
	}

	public void setR16_COLUMN_M(BigDecimal R16_COLUMN_M) {
		this.R16_COLUMN_M = R16_COLUMN_M;
	}

	public BigDecimal getR16_COLUMN_N() {
		return R16_COLUMN_N;
	}

	public void setR16_COLUMN_N(BigDecimal R16_COLUMN_N) {
		this.R16_COLUMN_N = R16_COLUMN_N;
	}

	public String getR17_COLUMN_A() {
		return R17_COLUMN_A;
	}

	public void setR17_COLUMN_A(String R17_COLUMN_A) {
		this.R17_COLUMN_A = R17_COLUMN_A;
	}

	public BigDecimal getR17_COLUMN_B() {
		return R17_COLUMN_B;
	}

	public void setR17_COLUMN_B(BigDecimal R17_COLUMN_B) {
		this.R17_COLUMN_B = R17_COLUMN_B;
	}

	public BigDecimal getR17_COLUMN_C() {
		return R17_COLUMN_C;
	}

	public void setR17_COLUMN_C(BigDecimal R17_COLUMN_C) {
		this.R17_COLUMN_C = R17_COLUMN_C;
	}

	public BigDecimal getR17_COLUMN_D() {
		return R17_COLUMN_D;
	}

	public void setR17_COLUMN_D(BigDecimal R17_COLUMN_D) {
		this.R17_COLUMN_D = R17_COLUMN_D;
	}

	public BigDecimal getR17_COLUMN_E() {
		return R17_COLUMN_E;
	}

	public void setR17_COLUMN_E(BigDecimal R17_COLUMN_E) {
		this.R17_COLUMN_E = R17_COLUMN_E;
	}

	public BigDecimal getR17_COLUMN_F() {
		return R17_COLUMN_F;
	}

	public void setR17_COLUMN_F(BigDecimal R17_COLUMN_F) {
		this.R17_COLUMN_F = R17_COLUMN_F;
	}

	public BigDecimal getR17_COLUMN_G() {
		return R17_COLUMN_G;
	}

	public void setR17_COLUMN_G(BigDecimal R17_COLUMN_G) {
		this.R17_COLUMN_G = R17_COLUMN_G;
	}

	public BigDecimal getR17_COLUMN_H() {
		return R17_COLUMN_H;
	}

	public void setR17_COLUMN_H(BigDecimal R17_COLUMN_H) {
		this.R17_COLUMN_H = R17_COLUMN_H;
	}

	public BigDecimal getR17_COLUMN_I() {
		return R17_COLUMN_I;
	}

	public void setR17_COLUMN_I(BigDecimal R17_COLUMN_I) {
		this.R17_COLUMN_I = R17_COLUMN_I;
	}

	public BigDecimal getR17_COLUMN_J() {
		return R17_COLUMN_J;
	}

	public void setR17_COLUMN_J(BigDecimal R17_COLUMN_J) {
		this.R17_COLUMN_J = R17_COLUMN_J;
	}

	public BigDecimal getR17_COLUMN_K() {
		return R17_COLUMN_K;
	}

	public void setR17_COLUMN_K(BigDecimal R17_COLUMN_K) {
		this.R17_COLUMN_K = R17_COLUMN_K;
	}

	public BigDecimal getR17_COLUMN_L() {
		return R17_COLUMN_L;
	}

	public void setR17_COLUMN_L(BigDecimal R17_COLUMN_L) {
		this.R17_COLUMN_L = R17_COLUMN_L;
	}

	public BigDecimal getR17_COLUMN_M() {
		return R17_COLUMN_M;
	}

	public void setR17_COLUMN_M(BigDecimal R17_COLUMN_M) {
		this.R17_COLUMN_M = R17_COLUMN_M;
	}

	public BigDecimal getR17_COLUMN_N() {
		return R17_COLUMN_N;
	}

	public void setR17_COLUMN_N(BigDecimal R17_COLUMN_N) {
		this.R17_COLUMN_N = R17_COLUMN_N;
	}

	public String getR18_COLUMN_A() {
		return R18_COLUMN_A;
	}

	public void setR18_COLUMN_A(String R18_COLUMN_A) {
		this.R18_COLUMN_A = R18_COLUMN_A;
	}

	public BigDecimal getR18_COLUMN_B() {
		return R18_COLUMN_B;
	}

	public void setR18_COLUMN_B(BigDecimal R18_COLUMN_B) {
		this.R18_COLUMN_B = R18_COLUMN_B;
	}

	public BigDecimal getR18_COLUMN_C() {
		return R18_COLUMN_C;
	}

	public void setR18_COLUMN_C(BigDecimal R18_COLUMN_C) {
		this.R18_COLUMN_C = R18_COLUMN_C;
	}

	public BigDecimal getR18_COLUMN_D() {
		return R18_COLUMN_D;
	}

	public void setR18_COLUMN_D(BigDecimal R18_COLUMN_D) {
		this.R18_COLUMN_D = R18_COLUMN_D;
	}

	public BigDecimal getR18_COLUMN_E() {
		return R18_COLUMN_E;
	}

	public void setR18_COLUMN_E(BigDecimal R18_COLUMN_E) {
		this.R18_COLUMN_E = R18_COLUMN_E;
	}

	public BigDecimal getR18_COLUMN_F() {
		return R18_COLUMN_F;
	}

	public void setR18_COLUMN_F(BigDecimal R18_COLUMN_F) {
		this.R18_COLUMN_F = R18_COLUMN_F;
	}

	public BigDecimal getR18_COLUMN_G() {
		return R18_COLUMN_G;
	}

	public void setR18_COLUMN_G(BigDecimal R18_COLUMN_G) {
		this.R18_COLUMN_G = R18_COLUMN_G;
	}

	public BigDecimal getR18_COLUMN_H() {
		return R18_COLUMN_H;
	}

	public void setR18_COLUMN_H(BigDecimal R18_COLUMN_H) {
		this.R18_COLUMN_H = R18_COLUMN_H;
	}

	public BigDecimal getR18_COLUMN_I() {
		return R18_COLUMN_I;
	}

	public void setR18_COLUMN_I(BigDecimal R18_COLUMN_I) {
		this.R18_COLUMN_I = R18_COLUMN_I;
	}

	public BigDecimal getR18_COLUMN_J() {
		return R18_COLUMN_J;
	}

	public void setR18_COLUMN_J(BigDecimal R18_COLUMN_J) {
		this.R18_COLUMN_J = R18_COLUMN_J;
	}

	public BigDecimal getR18_COLUMN_K() {
		return R18_COLUMN_K;
	}

	public void setR18_COLUMN_K(BigDecimal R18_COLUMN_K) {
		this.R18_COLUMN_K = R18_COLUMN_K;
	}

	public BigDecimal getR18_COLUMN_L() {
		return R18_COLUMN_L;
	}

	public void setR18_COLUMN_L(BigDecimal R18_COLUMN_L) {
		this.R18_COLUMN_L = R18_COLUMN_L;
	}

	public BigDecimal getR18_COLUMN_M() {
		return R18_COLUMN_M;
	}

	public void setR18_COLUMN_M(BigDecimal R18_COLUMN_M) {
		this.R18_COLUMN_M = R18_COLUMN_M;
	}

	public BigDecimal getR18_COLUMN_N() {
		return R18_COLUMN_N;
	}

	public void setR18_COLUMN_N(BigDecimal R18_COLUMN_N) {
		this.R18_COLUMN_N = R18_COLUMN_N;
	}

	public String getR19_COLUMN_A() {
		return R19_COLUMN_A;
	}

	public void setR19_COLUMN_A(String R19_COLUMN_A) {
		this.R19_COLUMN_A = R19_COLUMN_A;
	}

	public BigDecimal getR19_COLUMN_B() {
		return R19_COLUMN_B;
	}

	public void setR19_COLUMN_B(BigDecimal R19_COLUMN_B) {
		this.R19_COLUMN_B = R19_COLUMN_B;
	}

	public BigDecimal getR19_COLUMN_C() {
		return R19_COLUMN_C;
	}

	public void setR19_COLUMN_C(BigDecimal R19_COLUMN_C) {
		this.R19_COLUMN_C = R19_COLUMN_C;
	}

	public BigDecimal getR19_COLUMN_D() {
		return R19_COLUMN_D;
	}

	public void setR19_COLUMN_D(BigDecimal R19_COLUMN_D) {
		this.R19_COLUMN_D = R19_COLUMN_D;
	}

	public BigDecimal getR19_COLUMN_E() {
		return R19_COLUMN_E;
	}

	public void setR19_COLUMN_E(BigDecimal R19_COLUMN_E) {
		this.R19_COLUMN_E = R19_COLUMN_E;
	}

	public BigDecimal getR19_COLUMN_F() {
		return R19_COLUMN_F;
	}

	public void setR19_COLUMN_F(BigDecimal R19_COLUMN_F) {
		this.R19_COLUMN_F = R19_COLUMN_F;
	}

	public BigDecimal getR19_COLUMN_G() {
		return R19_COLUMN_G;
	}

	public void setR19_COLUMN_G(BigDecimal R19_COLUMN_G) {
		this.R19_COLUMN_G = R19_COLUMN_G;
	}

	public BigDecimal getR19_COLUMN_H() {
		return R19_COLUMN_H;
	}

	public void setR19_COLUMN_H(BigDecimal R19_COLUMN_H) {
		this.R19_COLUMN_H = R19_COLUMN_H;
	}

	public BigDecimal getR19_COLUMN_I() {
		return R19_COLUMN_I;
	}

	public void setR19_COLUMN_I(BigDecimal R19_COLUMN_I) {
		this.R19_COLUMN_I = R19_COLUMN_I;
	}

	public BigDecimal getR19_COLUMN_J() {
		return R19_COLUMN_J;
	}

	public void setR19_COLUMN_J(BigDecimal R19_COLUMN_J) {
		this.R19_COLUMN_J = R19_COLUMN_J;
	}

	public BigDecimal getR19_COLUMN_K() {
		return R19_COLUMN_K;
	}

	public void setR19_COLUMN_K(BigDecimal R19_COLUMN_K) {
		this.R19_COLUMN_K = R19_COLUMN_K;
	}

	public BigDecimal getR19_COLUMN_L() {
		return R19_COLUMN_L;
	}

	public void setR19_COLUMN_L(BigDecimal R19_COLUMN_L) {
		this.R19_COLUMN_L = R19_COLUMN_L;
	}

	public BigDecimal getR19_COLUMN_M() {
		return R19_COLUMN_M;
	}

	public void setR19_COLUMN_M(BigDecimal R19_COLUMN_M) {
		this.R19_COLUMN_M = R19_COLUMN_M;
	}

	public BigDecimal getR19_COLUMN_N() {
		return R19_COLUMN_N;
	}

	public void setR19_COLUMN_N(BigDecimal R19_COLUMN_N) {
		this.R19_COLUMN_N = R19_COLUMN_N;
	}

	public String getR20_COLUMN_A() {
		return R20_COLUMN_A;
	}

	public void setR20_COLUMN_A(String R20_COLUMN_A) {
		this.R20_COLUMN_A = R20_COLUMN_A;
	}

	public BigDecimal getR20_COLUMN_B() {
		return R20_COLUMN_B;
	}

	public void setR20_COLUMN_B(BigDecimal R20_COLUMN_B) {
		this.R20_COLUMN_B = R20_COLUMN_B;
	}

	public BigDecimal getR20_COLUMN_C() {
		return R20_COLUMN_C;
	}

	public void setR20_COLUMN_C(BigDecimal R20_COLUMN_C) {
		this.R20_COLUMN_C = R20_COLUMN_C;
	}

	public BigDecimal getR20_COLUMN_D() {
		return R20_COLUMN_D;
	}

	public void setR20_COLUMN_D(BigDecimal R20_COLUMN_D) {
		this.R20_COLUMN_D = R20_COLUMN_D;
	}

	public BigDecimal getR20_COLUMN_E() {
		return R20_COLUMN_E;
	}

	public void setR20_COLUMN_E(BigDecimal R20_COLUMN_E) {
		this.R20_COLUMN_E = R20_COLUMN_E;
	}

	public BigDecimal getR20_COLUMN_F() {
		return R20_COLUMN_F;
	}

	public void setR20_COLUMN_F(BigDecimal R20_COLUMN_F) {
		this.R20_COLUMN_F = R20_COLUMN_F;
	}

	public BigDecimal getR20_COLUMN_G() {
		return R20_COLUMN_G;
	}

	public void setR20_COLUMN_G(BigDecimal R20_COLUMN_G) {
		this.R20_COLUMN_G = R20_COLUMN_G;
	}

	public BigDecimal getR20_COLUMN_H() {
		return R20_COLUMN_H;
	}

	public void setR20_COLUMN_H(BigDecimal R20_COLUMN_H) {
		this.R20_COLUMN_H = R20_COLUMN_H;
	}

	public BigDecimal getR20_COLUMN_I() {
		return R20_COLUMN_I;
	}

	public void setR20_COLUMN_I(BigDecimal R20_COLUMN_I) {
		this.R20_COLUMN_I = R20_COLUMN_I;
	}

	public BigDecimal getR20_COLUMN_J() {
		return R20_COLUMN_J;
	}

	public void setR20_COLUMN_J(BigDecimal R20_COLUMN_J) {
		this.R20_COLUMN_J = R20_COLUMN_J;
	}

	public BigDecimal getR20_COLUMN_K() {
		return R20_COLUMN_K;
	}

	public void setR20_COLUMN_K(BigDecimal R20_COLUMN_K) {
		this.R20_COLUMN_K = R20_COLUMN_K;
	}

	public BigDecimal getR20_COLUMN_L() {
		return R20_COLUMN_L;
	}

	public void setR20_COLUMN_L(BigDecimal R20_COLUMN_L) {
		this.R20_COLUMN_L = R20_COLUMN_L;
	}

	public BigDecimal getR20_COLUMN_M() {
		return R20_COLUMN_M;
	}

	public void setR20_COLUMN_M(BigDecimal R20_COLUMN_M) {
		this.R20_COLUMN_M = R20_COLUMN_M;
	}

	public BigDecimal getR20_COLUMN_N() {
		return R20_COLUMN_N;
	}

	public void setR20_COLUMN_N(BigDecimal R20_COLUMN_N) {
		this.R20_COLUMN_N = R20_COLUMN_N;
	}

	public String getR21_COLUMN_A() {
		return R21_COLUMN_A;
	}

	public void setR21_COLUMN_A(String R21_COLUMN_A) {
		this.R21_COLUMN_A = R21_COLUMN_A;
	}

	public BigDecimal getR21_COLUMN_B() {
		return R21_COLUMN_B;
	}

	public void setR21_COLUMN_B(BigDecimal R21_COLUMN_B) {
		this.R21_COLUMN_B = R21_COLUMN_B;
	}

	public BigDecimal getR21_COLUMN_C() {
		return R21_COLUMN_C;
	}

	public void setR21_COLUMN_C(BigDecimal R21_COLUMN_C) {
		this.R21_COLUMN_C = R21_COLUMN_C;
	}

	public BigDecimal getR21_COLUMN_D() {
		return R21_COLUMN_D;
	}

	public void setR21_COLUMN_D(BigDecimal R21_COLUMN_D) {
		this.R21_COLUMN_D = R21_COLUMN_D;
	}

	public BigDecimal getR21_COLUMN_E() {
		return R21_COLUMN_E;
	}

	public void setR21_COLUMN_E(BigDecimal R21_COLUMN_E) {
		this.R21_COLUMN_E = R21_COLUMN_E;
	}

	public BigDecimal getR21_COLUMN_F() {
		return R21_COLUMN_F;
	}

	public void setR21_COLUMN_F(BigDecimal R21_COLUMN_F) {
		this.R21_COLUMN_F = R21_COLUMN_F;
	}

	public BigDecimal getR21_COLUMN_G() {
		return R21_COLUMN_G;
	}

	public void setR21_COLUMN_G(BigDecimal R21_COLUMN_G) {
		this.R21_COLUMN_G = R21_COLUMN_G;
	}

	public BigDecimal getR21_COLUMN_H() {
		return R21_COLUMN_H;
	}

	public void setR21_COLUMN_H(BigDecimal R21_COLUMN_H) {
		this.R21_COLUMN_H = R21_COLUMN_H;
	}

	public BigDecimal getR21_COLUMN_I() {
		return R21_COLUMN_I;
	}

	public void setR21_COLUMN_I(BigDecimal R21_COLUMN_I) {
		this.R21_COLUMN_I = R21_COLUMN_I;
	}

	public BigDecimal getR21_COLUMN_J() {
		return R21_COLUMN_J;
	}

	public void setR21_COLUMN_J(BigDecimal R21_COLUMN_J) {
		this.R21_COLUMN_J = R21_COLUMN_J;
	}

	public BigDecimal getR21_COLUMN_K() {
		return R21_COLUMN_K;
	}

	public void setR21_COLUMN_K(BigDecimal R21_COLUMN_K) {
		this.R21_COLUMN_K = R21_COLUMN_K;
	}

	public BigDecimal getR21_COLUMN_L() {
		return R21_COLUMN_L;
	}

	public void setR21_COLUMN_L(BigDecimal R21_COLUMN_L) {
		this.R21_COLUMN_L = R21_COLUMN_L;
	}

	public BigDecimal getR21_COLUMN_M() {
		return R21_COLUMN_M;
	}

	public void setR21_COLUMN_M(BigDecimal R21_COLUMN_M) {
		this.R21_COLUMN_M = R21_COLUMN_M;
	}

	public BigDecimal getR21_COLUMN_N() {
		return R21_COLUMN_N;
	}

	public void setR21_COLUMN_N(BigDecimal R21_COLUMN_N) {
		this.R21_COLUMN_N = R21_COLUMN_N;
	}

	public String getR22_COLUMN_A() {
		return R22_COLUMN_A;
	}

	public void setR22_COLUMN_A(String R22_COLUMN_A) {
		this.R22_COLUMN_A = R22_COLUMN_A;
	}

	public BigDecimal getR22_COLUMN_B() {
		return R22_COLUMN_B;
	}

	public void setR22_COLUMN_B(BigDecimal R22_COLUMN_B) {
		this.R22_COLUMN_B = R22_COLUMN_B;
	}

	public BigDecimal getR22_COLUMN_C() {
		return R22_COLUMN_C;
	}

	public void setR22_COLUMN_C(BigDecimal R22_COLUMN_C) {
		this.R22_COLUMN_C = R22_COLUMN_C;
	}

	public BigDecimal getR22_COLUMN_D() {
		return R22_COLUMN_D;
	}

	public void setR22_COLUMN_D(BigDecimal R22_COLUMN_D) {
		this.R22_COLUMN_D = R22_COLUMN_D;
	}

	public BigDecimal getR22_COLUMN_E() {
		return R22_COLUMN_E;
	}

	public void setR22_COLUMN_E(BigDecimal R22_COLUMN_E) {
		this.R22_COLUMN_E = R22_COLUMN_E;
	}

	public BigDecimal getR22_COLUMN_F() {
		return R22_COLUMN_F;
	}

	public void setR22_COLUMN_F(BigDecimal R22_COLUMN_F) {
		this.R22_COLUMN_F = R22_COLUMN_F;
	}

	public BigDecimal getR22_COLUMN_G() {
		return R22_COLUMN_G;
	}

	public void setR22_COLUMN_G(BigDecimal R22_COLUMN_G) {
		this.R22_COLUMN_G = R22_COLUMN_G;
	}

	public BigDecimal getR22_COLUMN_H() {
		return R22_COLUMN_H;
	}

	public void setR22_COLUMN_H(BigDecimal R22_COLUMN_H) {
		this.R22_COLUMN_H = R22_COLUMN_H;
	}

	public BigDecimal getR22_COLUMN_I() {
		return R22_COLUMN_I;
	}

	public void setR22_COLUMN_I(BigDecimal R22_COLUMN_I) {
		this.R22_COLUMN_I = R22_COLUMN_I;
	}

	public BigDecimal getR22_COLUMN_J() {
		return R22_COLUMN_J;
	}

	public void setR22_COLUMN_J(BigDecimal R22_COLUMN_J) {
		this.R22_COLUMN_J = R22_COLUMN_J;
	}

	public BigDecimal getR22_COLUMN_K() {
		return R22_COLUMN_K;
	}

	public void setR22_COLUMN_K(BigDecimal R22_COLUMN_K) {
		this.R22_COLUMN_K = R22_COLUMN_K;
	}

	public BigDecimal getR22_COLUMN_L() {
		return R22_COLUMN_L;
	}

	public void setR22_COLUMN_L(BigDecimal R22_COLUMN_L) {
		this.R22_COLUMN_L = R22_COLUMN_L;
	}

	public BigDecimal getR22_COLUMN_M() {
		return R22_COLUMN_M;
	}

	public void setR22_COLUMN_M(BigDecimal R22_COLUMN_M) {
		this.R22_COLUMN_M = R22_COLUMN_M;
	}

	public BigDecimal getR22_COLUMN_N() {
		return R22_COLUMN_N;
	}

	public void setR22_COLUMN_N(BigDecimal R22_COLUMN_N) {
		this.R22_COLUMN_N = R22_COLUMN_N;
	}

	public String getR23_COLUMN_A() {
		return R23_COLUMN_A;
	}

	public void setR23_COLUMN_A(String R23_COLUMN_A) {
		this.R23_COLUMN_A = R23_COLUMN_A;
	}

	public BigDecimal getR23_COLUMN_B() {
		return R23_COLUMN_B;
	}

	public void setR23_COLUMN_B(BigDecimal R23_COLUMN_B) {
		this.R23_COLUMN_B = R23_COLUMN_B;
	}

	public BigDecimal getR23_COLUMN_C() {
		return R23_COLUMN_C;
	}

	public void setR23_COLUMN_C(BigDecimal R23_COLUMN_C) {
		this.R23_COLUMN_C = R23_COLUMN_C;
	}

	public BigDecimal getR23_COLUMN_D() {
		return R23_COLUMN_D;
	}

	public void setR23_COLUMN_D(BigDecimal R23_COLUMN_D) {
		this.R23_COLUMN_D = R23_COLUMN_D;
	}

	public BigDecimal getR23_COLUMN_E() {
		return R23_COLUMN_E;
	}

	public void setR23_COLUMN_E(BigDecimal R23_COLUMN_E) {
		this.R23_COLUMN_E = R23_COLUMN_E;
	}

	public BigDecimal getR23_COLUMN_F() {
		return R23_COLUMN_F;
	}

	public void setR23_COLUMN_F(BigDecimal R23_COLUMN_F) {
		this.R23_COLUMN_F = R23_COLUMN_F;
	}

	public BigDecimal getR23_COLUMN_G() {
		return R23_COLUMN_G;
	}

	public void setR23_COLUMN_G(BigDecimal R23_COLUMN_G) {
		this.R23_COLUMN_G = R23_COLUMN_G;
	}

	public BigDecimal getR23_COLUMN_H() {
		return R23_COLUMN_H;
	}

	public void setR23_COLUMN_H(BigDecimal R23_COLUMN_H) {
		this.R23_COLUMN_H = R23_COLUMN_H;
	}

	public BigDecimal getR23_COLUMN_I() {
		return R23_COLUMN_I;
	}

	public void setR23_COLUMN_I(BigDecimal R23_COLUMN_I) {
		this.R23_COLUMN_I = R23_COLUMN_I;
	}

	public BigDecimal getR23_COLUMN_J() {
		return R23_COLUMN_J;
	}

	public void setR23_COLUMN_J(BigDecimal R23_COLUMN_J) {
		this.R23_COLUMN_J = R23_COLUMN_J;
	}

	public BigDecimal getR23_COLUMN_K() {
		return R23_COLUMN_K;
	}

	public void setR23_COLUMN_K(BigDecimal R23_COLUMN_K) {
		this.R23_COLUMN_K = R23_COLUMN_K;
	}

	public BigDecimal getR23_COLUMN_L() {
		return R23_COLUMN_L;
	}

	public void setR23_COLUMN_L(BigDecimal R23_COLUMN_L) {
		this.R23_COLUMN_L = R23_COLUMN_L;
	}

	public BigDecimal getR23_COLUMN_M() {
		return R23_COLUMN_M;
	}

	public void setR23_COLUMN_M(BigDecimal R23_COLUMN_M) {
		this.R23_COLUMN_M = R23_COLUMN_M;
	}

	public BigDecimal getR23_COLUMN_N() {
		return R23_COLUMN_N;
	}

	public void setR23_COLUMN_N(BigDecimal R23_COLUMN_N) {
		this.R23_COLUMN_N = R23_COLUMN_N;
	}

	public String getR24_COLUMN_A() {
		return R24_COLUMN_A;
	}

	public void setR24_COLUMN_A(String R24_COLUMN_A) {
		this.R24_COLUMN_A = R24_COLUMN_A;
	}

	public BigDecimal getR24_COLUMN_B() {
		return R24_COLUMN_B;
	}

	public void setR24_COLUMN_B(BigDecimal R24_COLUMN_B) {
		this.R24_COLUMN_B = R24_COLUMN_B;
	}

	public BigDecimal getR24_COLUMN_C() {
		return R24_COLUMN_C;
	}

	public void setR24_COLUMN_C(BigDecimal R24_COLUMN_C) {
		this.R24_COLUMN_C = R24_COLUMN_C;
	}

	public BigDecimal getR24_COLUMN_D() {
		return R24_COLUMN_D;
	}

	public void setR24_COLUMN_D(BigDecimal R24_COLUMN_D) {
		this.R24_COLUMN_D = R24_COLUMN_D;
	}

	public BigDecimal getR24_COLUMN_E() {
		return R24_COLUMN_E;
	}

	public void setR24_COLUMN_E(BigDecimal R24_COLUMN_E) {
		this.R24_COLUMN_E = R24_COLUMN_E;
	}

	public BigDecimal getR24_COLUMN_F() {
		return R24_COLUMN_F;
	}

	public void setR24_COLUMN_F(BigDecimal R24_COLUMN_F) {
		this.R24_COLUMN_F = R24_COLUMN_F;
	}

	public BigDecimal getR24_COLUMN_G() {
		return R24_COLUMN_G;
	}

	public void setR24_COLUMN_G(BigDecimal R24_COLUMN_G) {
		this.R24_COLUMN_G = R24_COLUMN_G;
	}

	public BigDecimal getR24_COLUMN_H() {
		return R24_COLUMN_H;
	}

	public void setR24_COLUMN_H(BigDecimal R24_COLUMN_H) {
		this.R24_COLUMN_H = R24_COLUMN_H;
	}

	public BigDecimal getR24_COLUMN_I() {
		return R24_COLUMN_I;
	}

	public void setR24_COLUMN_I(BigDecimal R24_COLUMN_I) {
		this.R24_COLUMN_I = R24_COLUMN_I;
	}

	public BigDecimal getR24_COLUMN_J() {
		return R24_COLUMN_J;
	}

	public void setR24_COLUMN_J(BigDecimal R24_COLUMN_J) {
		this.R24_COLUMN_J = R24_COLUMN_J;
	}

	public BigDecimal getR24_COLUMN_K() {
		return R24_COLUMN_K;
	}

	public void setR24_COLUMN_K(BigDecimal R24_COLUMN_K) {
		this.R24_COLUMN_K = R24_COLUMN_K;
	}

	public BigDecimal getR24_COLUMN_L() {
		return R24_COLUMN_L;
	}

	public void setR24_COLUMN_L(BigDecimal R24_COLUMN_L) {
		this.R24_COLUMN_L = R24_COLUMN_L;
	}

	public BigDecimal getR24_COLUMN_M() {
		return R24_COLUMN_M;
	}

	public void setR24_COLUMN_M(BigDecimal R24_COLUMN_M) {
		this.R24_COLUMN_M = R24_COLUMN_M;
	}

	public BigDecimal getR24_COLUMN_N() {
		return R24_COLUMN_N;
	}

	public void setR24_COLUMN_N(BigDecimal R24_COLUMN_N) {
		this.R24_COLUMN_N = R24_COLUMN_N;
	}

	public String getR25_COLUMN_A() {
		return R25_COLUMN_A;
	}

	public void setR25_COLUMN_A(String R25_COLUMN_A) {
		this.R25_COLUMN_A = R25_COLUMN_A;
	}

	public BigDecimal getR25_COLUMN_B() {
		return R25_COLUMN_B;
	}

	public void setR25_COLUMN_B(BigDecimal R25_COLUMN_B) {
		this.R25_COLUMN_B = R25_COLUMN_B;
	}

	public BigDecimal getR25_COLUMN_C() {
		return R25_COLUMN_C;
	}

	public void setR25_COLUMN_C(BigDecimal R25_COLUMN_C) {
		this.R25_COLUMN_C = R25_COLUMN_C;
	}

	public BigDecimal getR25_COLUMN_D() {
		return R25_COLUMN_D;
	}

	public void setR25_COLUMN_D(BigDecimal R25_COLUMN_D) {
		this.R25_COLUMN_D = R25_COLUMN_D;
	}

	public BigDecimal getR25_COLUMN_E() {
		return R25_COLUMN_E;
	}

	public void setR25_COLUMN_E(BigDecimal R25_COLUMN_E) {
		this.R25_COLUMN_E = R25_COLUMN_E;
	}

	public BigDecimal getR25_COLUMN_F() {
		return R25_COLUMN_F;
	}

	public void setR25_COLUMN_F(BigDecimal R25_COLUMN_F) {
		this.R25_COLUMN_F = R25_COLUMN_F;
	}

	public BigDecimal getR25_COLUMN_G() {
		return R25_COLUMN_G;
	}

	public void setR25_COLUMN_G(BigDecimal R25_COLUMN_G) {
		this.R25_COLUMN_G = R25_COLUMN_G;
	}

	public BigDecimal getR25_COLUMN_H() {
		return R25_COLUMN_H;
	}

	public void setR25_COLUMN_H(BigDecimal R25_COLUMN_H) {
		this.R25_COLUMN_H = R25_COLUMN_H;
	}

	public BigDecimal getR25_COLUMN_I() {
		return R25_COLUMN_I;
	}

	public void setR25_COLUMN_I(BigDecimal R25_COLUMN_I) {
		this.R25_COLUMN_I = R25_COLUMN_I;
	}

	public BigDecimal getR25_COLUMN_J() {
		return R25_COLUMN_J;
	}

	public void setR25_COLUMN_J(BigDecimal R25_COLUMN_J) {
		this.R25_COLUMN_J = R25_COLUMN_J;
	}

	public BigDecimal getR25_COLUMN_K() {
		return R25_COLUMN_K;
	}

	public void setR25_COLUMN_K(BigDecimal R25_COLUMN_K) {
		this.R25_COLUMN_K = R25_COLUMN_K;
	}

	public BigDecimal getR25_COLUMN_L() {
		return R25_COLUMN_L;
	}

	public void setR25_COLUMN_L(BigDecimal R25_COLUMN_L) {
		this.R25_COLUMN_L = R25_COLUMN_L;
	}

	public BigDecimal getR25_COLUMN_M() {
		return R25_COLUMN_M;
	}

	public void setR25_COLUMN_M(BigDecimal R25_COLUMN_M) {
		this.R25_COLUMN_M = R25_COLUMN_M;
	}

	public BigDecimal getR25_COLUMN_N() {
		return R25_COLUMN_N;
	}

	public void setR25_COLUMN_N(BigDecimal R25_COLUMN_N) {
		this.R25_COLUMN_N = R25_COLUMN_N;
	}

	public String getR26_COLUMN_A() {
		return R26_COLUMN_A;
	}

	public void setR26_COLUMN_A(String R26_COLUMN_A) {
		this.R26_COLUMN_A = R26_COLUMN_A;
	}

	public BigDecimal getR26_COLUMN_B() {
		return R26_COLUMN_B;
	}

	public void setR26_COLUMN_B(BigDecimal R26_COLUMN_B) {
		this.R26_COLUMN_B = R26_COLUMN_B;
	}

	public BigDecimal getR26_COLUMN_C() {
		return R26_COLUMN_C;
	}

	public void setR26_COLUMN_C(BigDecimal R26_COLUMN_C) {
		this.R26_COLUMN_C = R26_COLUMN_C;
	}

	public BigDecimal getR26_COLUMN_D() {
		return R26_COLUMN_D;
	}

	public void setR26_COLUMN_D(BigDecimal R26_COLUMN_D) {
		this.R26_COLUMN_D = R26_COLUMN_D;
	}

	public BigDecimal getR26_COLUMN_E() {
		return R26_COLUMN_E;
	}

	public void setR26_COLUMN_E(BigDecimal R26_COLUMN_E) {
		this.R26_COLUMN_E = R26_COLUMN_E;
	}

	public BigDecimal getR26_COLUMN_F() {
		return R26_COLUMN_F;
	}

	public void setR26_COLUMN_F(BigDecimal R26_COLUMN_F) {
		this.R26_COLUMN_F = R26_COLUMN_F;
	}

	public BigDecimal getR26_COLUMN_G() {
		return R26_COLUMN_G;
	}

	public void setR26_COLUMN_G(BigDecimal R26_COLUMN_G) {
		this.R26_COLUMN_G = R26_COLUMN_G;
	}

	public BigDecimal getR26_COLUMN_H() {
		return R26_COLUMN_H;
	}

	public void setR26_COLUMN_H(BigDecimal R26_COLUMN_H) {
		this.R26_COLUMN_H = R26_COLUMN_H;
	}

	public BigDecimal getR26_COLUMN_I() {
		return R26_COLUMN_I;
	}

	public void setR26_COLUMN_I(BigDecimal R26_COLUMN_I) {
		this.R26_COLUMN_I = R26_COLUMN_I;
	}

	public BigDecimal getR26_COLUMN_J() {
		return R26_COLUMN_J;
	}

	public void setR26_COLUMN_J(BigDecimal R26_COLUMN_J) {
		this.R26_COLUMN_J = R26_COLUMN_J;
	}

	public BigDecimal getR26_COLUMN_K() {
		return R26_COLUMN_K;
	}

	public void setR26_COLUMN_K(BigDecimal R26_COLUMN_K) {
		this.R26_COLUMN_K = R26_COLUMN_K;
	}

	public BigDecimal getR26_COLUMN_L() {
		return R26_COLUMN_L;
	}

	public void setR26_COLUMN_L(BigDecimal R26_COLUMN_L) {
		this.R26_COLUMN_L = R26_COLUMN_L;
	}

	public BigDecimal getR26_COLUMN_M() {
		return R26_COLUMN_M;
	}

	public void setR26_COLUMN_M(BigDecimal R26_COLUMN_M) {
		this.R26_COLUMN_M = R26_COLUMN_M;
	}

	public BigDecimal getR26_COLUMN_N() {
		return R26_COLUMN_N;
	}

	public void setR26_COLUMN_N(BigDecimal R26_COLUMN_N) {
		this.R26_COLUMN_N = R26_COLUMN_N;
	}

	public String getR27_COLUMN_A() {
		return R27_COLUMN_A;
	}

	public void setR27_COLUMN_A(String R27_COLUMN_A) {
		this.R27_COLUMN_A = R27_COLUMN_A;
	}

	public BigDecimal getR27_COLUMN_B() {
		return R27_COLUMN_B;
	}

	public void setR27_COLUMN_B(BigDecimal R27_COLUMN_B) {
		this.R27_COLUMN_B = R27_COLUMN_B;
	}

	public BigDecimal getR27_COLUMN_C() {
		return R27_COLUMN_C;
	}

	public void setR27_COLUMN_C(BigDecimal R27_COLUMN_C) {
		this.R27_COLUMN_C = R27_COLUMN_C;
	}

	public BigDecimal getR27_COLUMN_D() {
		return R27_COLUMN_D;
	}

	public void setR27_COLUMN_D(BigDecimal R27_COLUMN_D) {
		this.R27_COLUMN_D = R27_COLUMN_D;
	}

	public BigDecimal getR27_COLUMN_E() {
		return R27_COLUMN_E;
	}

	public void setR27_COLUMN_E(BigDecimal R27_COLUMN_E) {
		this.R27_COLUMN_E = R27_COLUMN_E;
	}

	public BigDecimal getR27_COLUMN_F() {
		return R27_COLUMN_F;
	}

	public void setR27_COLUMN_F(BigDecimal R27_COLUMN_F) {
		this.R27_COLUMN_F = R27_COLUMN_F;
	}

	public BigDecimal getR27_COLUMN_G() {
		return R27_COLUMN_G;
	}

	public void setR27_COLUMN_G(BigDecimal R27_COLUMN_G) {
		this.R27_COLUMN_G = R27_COLUMN_G;
	}

	public BigDecimal getR27_COLUMN_H() {
		return R27_COLUMN_H;
	}

	public void setR27_COLUMN_H(BigDecimal R27_COLUMN_H) {
		this.R27_COLUMN_H = R27_COLUMN_H;
	}

	public BigDecimal getR27_COLUMN_I() {
		return R27_COLUMN_I;
	}

	public void setR27_COLUMN_I(BigDecimal R27_COLUMN_I) {
		this.R27_COLUMN_I = R27_COLUMN_I;
	}

	public BigDecimal getR27_COLUMN_J() {
		return R27_COLUMN_J;
	}

	public void setR27_COLUMN_J(BigDecimal R27_COLUMN_J) {
		this.R27_COLUMN_J = R27_COLUMN_J;
	}

	public BigDecimal getR27_COLUMN_K() {
		return R27_COLUMN_K;
	}

	public void setR27_COLUMN_K(BigDecimal R27_COLUMN_K) {
		this.R27_COLUMN_K = R27_COLUMN_K;
	}

	public BigDecimal getR27_COLUMN_L() {
		return R27_COLUMN_L;
	}

	public void setR27_COLUMN_L(BigDecimal R27_COLUMN_L) {
		this.R27_COLUMN_L = R27_COLUMN_L;
	}

	public BigDecimal getR27_COLUMN_M() {
		return R27_COLUMN_M;
	}

	public void setR27_COLUMN_M(BigDecimal R27_COLUMN_M) {
		this.R27_COLUMN_M = R27_COLUMN_M;
	}

	public BigDecimal getR27_COLUMN_N() {
		return R27_COLUMN_N;
	}

	public void setR27_COLUMN_N(BigDecimal R27_COLUMN_N) {
		this.R27_COLUMN_N = R27_COLUMN_N;
	}

	public String getR28_COLUMN_A() {
		return R28_COLUMN_A;
	}

	public void setR28_COLUMN_A(String R28_COLUMN_A) {
		this.R28_COLUMN_A = R28_COLUMN_A;
	}

	public BigDecimal getR28_COLUMN_B() {
		return R28_COLUMN_B;
	}

	public void setR28_COLUMN_B(BigDecimal R28_COLUMN_B) {
		this.R28_COLUMN_B = R28_COLUMN_B;
	}

	public BigDecimal getR28_COLUMN_C() {
		return R28_COLUMN_C;
	}

	public void setR28_COLUMN_C(BigDecimal R28_COLUMN_C) {
		this.R28_COLUMN_C = R28_COLUMN_C;
	}

	public BigDecimal getR28_COLUMN_D() {
		return R28_COLUMN_D;
	}

	public void setR28_COLUMN_D(BigDecimal R28_COLUMN_D) {
		this.R28_COLUMN_D = R28_COLUMN_D;
	}

	public BigDecimal getR28_COLUMN_E() {
		return R28_COLUMN_E;
	}

	public void setR28_COLUMN_E(BigDecimal R28_COLUMN_E) {
		this.R28_COLUMN_E = R28_COLUMN_E;
	}

	public BigDecimal getR28_COLUMN_F() {
		return R28_COLUMN_F;
	}

	public void setR28_COLUMN_F(BigDecimal R28_COLUMN_F) {
		this.R28_COLUMN_F = R28_COLUMN_F;
	}

	public BigDecimal getR28_COLUMN_G() {
		return R28_COLUMN_G;
	}

	public void setR28_COLUMN_G(BigDecimal R28_COLUMN_G) {
		this.R28_COLUMN_G = R28_COLUMN_G;
	}

	public BigDecimal getR28_COLUMN_H() {
		return R28_COLUMN_H;
	}

	public void setR28_COLUMN_H(BigDecimal R28_COLUMN_H) {
		this.R28_COLUMN_H = R28_COLUMN_H;
	}

	public BigDecimal getR28_COLUMN_I() {
		return R28_COLUMN_I;
	}

	public void setR28_COLUMN_I(BigDecimal R28_COLUMN_I) {
		this.R28_COLUMN_I = R28_COLUMN_I;
	}

	public BigDecimal getR28_COLUMN_J() {
		return R28_COLUMN_J;
	}

	public void setR28_COLUMN_J(BigDecimal R28_COLUMN_J) {
		this.R28_COLUMN_J = R28_COLUMN_J;
	}

	public BigDecimal getR28_COLUMN_K() {
		return R28_COLUMN_K;
	}

	public void setR28_COLUMN_K(BigDecimal R28_COLUMN_K) {
		this.R28_COLUMN_K = R28_COLUMN_K;
	}

	public BigDecimal getR28_COLUMN_L() {
		return R28_COLUMN_L;
	}

	public void setR28_COLUMN_L(BigDecimal R28_COLUMN_L) {
		this.R28_COLUMN_L = R28_COLUMN_L;
	}

	public BigDecimal getR28_COLUMN_M() {
		return R28_COLUMN_M;
	}

	public void setR28_COLUMN_M(BigDecimal R28_COLUMN_M) {
		this.R28_COLUMN_M = R28_COLUMN_M;
	}

	public BigDecimal getR28_COLUMN_N() {
		return R28_COLUMN_N;
	}

	public void setR28_COLUMN_N(BigDecimal R28_COLUMN_N) {
		this.R28_COLUMN_N = R28_COLUMN_N;
	}

	public String getR29_COLUMN_A() {
		return R29_COLUMN_A;
	}

	public void setR29_COLUMN_A(String R29_COLUMN_A) {
		this.R29_COLUMN_A = R29_COLUMN_A;
	}

	public BigDecimal getR29_COLUMN_B() {
		return R29_COLUMN_B;
	}

	public void setR29_COLUMN_B(BigDecimal R29_COLUMN_B) {
		this.R29_COLUMN_B = R29_COLUMN_B;
	}

	public BigDecimal getR29_COLUMN_C() {
		return R29_COLUMN_C;
	}

	public void setR29_COLUMN_C(BigDecimal R29_COLUMN_C) {
		this.R29_COLUMN_C = R29_COLUMN_C;
	}

	public BigDecimal getR29_COLUMN_D() {
		return R29_COLUMN_D;
	}

	public void setR29_COLUMN_D(BigDecimal R29_COLUMN_D) {
		this.R29_COLUMN_D = R29_COLUMN_D;
	}

	public BigDecimal getR29_COLUMN_E() {
		return R29_COLUMN_E;
	}

	public void setR29_COLUMN_E(BigDecimal R29_COLUMN_E) {
		this.R29_COLUMN_E = R29_COLUMN_E;
	}

	public BigDecimal getR29_COLUMN_F() {
		return R29_COLUMN_F;
	}

	public void setR29_COLUMN_F(BigDecimal R29_COLUMN_F) {
		this.R29_COLUMN_F = R29_COLUMN_F;
	}

	public BigDecimal getR29_COLUMN_G() {
		return R29_COLUMN_G;
	}

	public void setR29_COLUMN_G(BigDecimal R29_COLUMN_G) {
		this.R29_COLUMN_G = R29_COLUMN_G;
	}

	public BigDecimal getR29_COLUMN_H() {
		return R29_COLUMN_H;
	}

	public void setR29_COLUMN_H(BigDecimal R29_COLUMN_H) {
		this.R29_COLUMN_H = R29_COLUMN_H;
	}

	public BigDecimal getR29_COLUMN_I() {
		return R29_COLUMN_I;
	}

	public void setR29_COLUMN_I(BigDecimal R29_COLUMN_I) {
		this.R29_COLUMN_I = R29_COLUMN_I;
	}

	public BigDecimal getR29_COLUMN_J() {
		return R29_COLUMN_J;
	}

	public void setR29_COLUMN_J(BigDecimal R29_COLUMN_J) {
		this.R29_COLUMN_J = R29_COLUMN_J;
	}

	public BigDecimal getR29_COLUMN_K() {
		return R29_COLUMN_K;
	}

	public void setR29_COLUMN_K(BigDecimal R29_COLUMN_K) {
		this.R29_COLUMN_K = R29_COLUMN_K;
	}

	public BigDecimal getR29_COLUMN_L() {
		return R29_COLUMN_L;
	}

	public void setR29_COLUMN_L(BigDecimal R29_COLUMN_L) {
		this.R29_COLUMN_L = R29_COLUMN_L;
	}

	public BigDecimal getR29_COLUMN_M() {
		return R29_COLUMN_M;
	}

	public void setR29_COLUMN_M(BigDecimal R29_COLUMN_M) {
		this.R29_COLUMN_M = R29_COLUMN_M;
	}

	public BigDecimal getR29_COLUMN_N() {
		return R29_COLUMN_N;
	}

	public void setR29_COLUMN_N(BigDecimal R29_COLUMN_N) {
		this.R29_COLUMN_N = R29_COLUMN_N;
	}

	public String getR30_COLUMN_A() {
		return R30_COLUMN_A;
	}

	public void setR30_COLUMN_A(String R30_COLUMN_A) {
		this.R30_COLUMN_A = R30_COLUMN_A;
	}

	public BigDecimal getR30_COLUMN_B() {
		return R30_COLUMN_B;
	}

	public void setR30_COLUMN_B(BigDecimal R30_COLUMN_B) {
		this.R30_COLUMN_B = R30_COLUMN_B;
	}

	public BigDecimal getR30_COLUMN_C() {
		return R30_COLUMN_C;
	}

	public void setR30_COLUMN_C(BigDecimal R30_COLUMN_C) {
		this.R30_COLUMN_C = R30_COLUMN_C;
	}

	public BigDecimal getR30_COLUMN_D() {
		return R30_COLUMN_D;
	}

	public void setR30_COLUMN_D(BigDecimal R30_COLUMN_D) {
		this.R30_COLUMN_D = R30_COLUMN_D;
	}

	public BigDecimal getR30_COLUMN_E() {
		return R30_COLUMN_E;
	}

	public void setR30_COLUMN_E(BigDecimal R30_COLUMN_E) {
		this.R30_COLUMN_E = R30_COLUMN_E;
	}

	public BigDecimal getR30_COLUMN_F() {
		return R30_COLUMN_F;
	}

	public void setR30_COLUMN_F(BigDecimal R30_COLUMN_F) {
		this.R30_COLUMN_F = R30_COLUMN_F;
	}

	public BigDecimal getR30_COLUMN_G() {
		return R30_COLUMN_G;
	}

	public void setR30_COLUMN_G(BigDecimal R30_COLUMN_G) {
		this.R30_COLUMN_G = R30_COLUMN_G;
	}

	public BigDecimal getR30_COLUMN_H() {
		return R30_COLUMN_H;
	}

	public void setR30_COLUMN_H(BigDecimal R30_COLUMN_H) {
		this.R30_COLUMN_H = R30_COLUMN_H;
	}

	public BigDecimal getR30_COLUMN_I() {
		return R30_COLUMN_I;
	}

	public void setR30_COLUMN_I(BigDecimal R30_COLUMN_I) {
		this.R30_COLUMN_I = R30_COLUMN_I;
	}

	public BigDecimal getR30_COLUMN_J() {
		return R30_COLUMN_J;
	}

	public void setR30_COLUMN_J(BigDecimal R30_COLUMN_J) {
		this.R30_COLUMN_J = R30_COLUMN_J;
	}

	public BigDecimal getR30_COLUMN_K() {
		return R30_COLUMN_K;
	}

	public void setR30_COLUMN_K(BigDecimal R30_COLUMN_K) {
		this.R30_COLUMN_K = R30_COLUMN_K;
	}

	public BigDecimal getR30_COLUMN_L() {
		return R30_COLUMN_L;
	}

	public void setR30_COLUMN_L(BigDecimal R30_COLUMN_L) {
		this.R30_COLUMN_L = R30_COLUMN_L;
	}

	public BigDecimal getR30_COLUMN_M() {
		return R30_COLUMN_M;
	}

	public void setR30_COLUMN_M(BigDecimal R30_COLUMN_M) {
		this.R30_COLUMN_M = R30_COLUMN_M;
	}

	public BigDecimal getR30_COLUMN_N() {
		return R30_COLUMN_N;
	}

	public void setR30_COLUMN_N(BigDecimal R30_COLUMN_N) {
		this.R30_COLUMN_N = R30_COLUMN_N;
	}

	public String getR31_COLUMN_A() {
		return R31_COLUMN_A;
	}

	public void setR31_COLUMN_A(String R31_COLUMN_A) {
		this.R31_COLUMN_A = R31_COLUMN_A;
	}

	public BigDecimal getR31_COLUMN_B() {
		return R31_COLUMN_B;
	}

	public void setR31_COLUMN_B(BigDecimal R31_COLUMN_B) {
		this.R31_COLUMN_B = R31_COLUMN_B;
	}

	public BigDecimal getR31_COLUMN_C() {
		return R31_COLUMN_C;
	}

	public void setR31_COLUMN_C(BigDecimal R31_COLUMN_C) {
		this.R31_COLUMN_C = R31_COLUMN_C;
	}

	public BigDecimal getR31_COLUMN_D() {
		return R31_COLUMN_D;
	}

	public void setR31_COLUMN_D(BigDecimal R31_COLUMN_D) {
		this.R31_COLUMN_D = R31_COLUMN_D;
	}

	public BigDecimal getR31_COLUMN_E() {
		return R31_COLUMN_E;
	}

	public void setR31_COLUMN_E(BigDecimal R31_COLUMN_E) {
		this.R31_COLUMN_E = R31_COLUMN_E;
	}

	public BigDecimal getR31_COLUMN_F() {
		return R31_COLUMN_F;
	}

	public void setR31_COLUMN_F(BigDecimal R31_COLUMN_F) {
		this.R31_COLUMN_F = R31_COLUMN_F;
	}

	public BigDecimal getR31_COLUMN_G() {
		return R31_COLUMN_G;
	}

	public void setR31_COLUMN_G(BigDecimal R31_COLUMN_G) {
		this.R31_COLUMN_G = R31_COLUMN_G;
	}

	public BigDecimal getR31_COLUMN_H() {
		return R31_COLUMN_H;
	}

	public void setR31_COLUMN_H(BigDecimal R31_COLUMN_H) {
		this.R31_COLUMN_H = R31_COLUMN_H;
	}

	public BigDecimal getR31_COLUMN_I() {
		return R31_COLUMN_I;
	}

	public void setR31_COLUMN_I(BigDecimal R31_COLUMN_I) {
		this.R31_COLUMN_I = R31_COLUMN_I;
	}

	public BigDecimal getR31_COLUMN_J() {
		return R31_COLUMN_J;
	}

	public void setR31_COLUMN_J(BigDecimal R31_COLUMN_J) {
		this.R31_COLUMN_J = R31_COLUMN_J;
	}

	public BigDecimal getR31_COLUMN_K() {
		return R31_COLUMN_K;
	}

	public void setR31_COLUMN_K(BigDecimal R31_COLUMN_K) {
		this.R31_COLUMN_K = R31_COLUMN_K;
	}

	public BigDecimal getR31_COLUMN_L() {
		return R31_COLUMN_L;
	}

	public void setR31_COLUMN_L(BigDecimal R31_COLUMN_L) {
		this.R31_COLUMN_L = R31_COLUMN_L;
	}

	public BigDecimal getR31_COLUMN_M() {
		return R31_COLUMN_M;
	}

	public void setR31_COLUMN_M(BigDecimal R31_COLUMN_M) {
		this.R31_COLUMN_M = R31_COLUMN_M;
	}

	public BigDecimal getR31_COLUMN_N() {
		return R31_COLUMN_N;
	}

	public void setR31_COLUMN_N(BigDecimal R31_COLUMN_N) {
		this.R31_COLUMN_N = R31_COLUMN_N;
	}

	public String getR32_COLUMN_A() {
		return R32_COLUMN_A;
	}

	public void setR32_COLUMN_A(String R32_COLUMN_A) {
		this.R32_COLUMN_A = R32_COLUMN_A;
	}

	public BigDecimal getR32_COLUMN_B() {
		return R32_COLUMN_B;
	}

	public void setR32_COLUMN_B(BigDecimal R32_COLUMN_B) {
		this.R32_COLUMN_B = R32_COLUMN_B;
	}

	public BigDecimal getR32_COLUMN_C() {
		return R32_COLUMN_C;
	}

	public void setR32_COLUMN_C(BigDecimal R32_COLUMN_C) {
		this.R32_COLUMN_C = R32_COLUMN_C;
	}

	public BigDecimal getR32_COLUMN_D() {
		return R32_COLUMN_D;
	}

	public void setR32_COLUMN_D(BigDecimal R32_COLUMN_D) {
		this.R32_COLUMN_D = R32_COLUMN_D;
	}

	public BigDecimal getR32_COLUMN_E() {
		return R32_COLUMN_E;
	}

	public void setR32_COLUMN_E(BigDecimal R32_COLUMN_E) {
		this.R32_COLUMN_E = R32_COLUMN_E;
	}

	public BigDecimal getR32_COLUMN_F() {
		return R32_COLUMN_F;
	}

	public void setR32_COLUMN_F(BigDecimal R32_COLUMN_F) {
		this.R32_COLUMN_F = R32_COLUMN_F;
	}

	public BigDecimal getR32_COLUMN_G() {
		return R32_COLUMN_G;
	}

	public void setR32_COLUMN_G(BigDecimal R32_COLUMN_G) {
		this.R32_COLUMN_G = R32_COLUMN_G;
	}

	public BigDecimal getR32_COLUMN_H() {
		return R32_COLUMN_H;
	}

	public void setR32_COLUMN_H(BigDecimal R32_COLUMN_H) {
		this.R32_COLUMN_H = R32_COLUMN_H;
	}

	public BigDecimal getR32_COLUMN_I() {
		return R32_COLUMN_I;
	}

	public void setR32_COLUMN_I(BigDecimal R32_COLUMN_I) {
		this.R32_COLUMN_I = R32_COLUMN_I;
	}

	public BigDecimal getR32_COLUMN_J() {
		return R32_COLUMN_J;
	}

	public void setR32_COLUMN_J(BigDecimal R32_COLUMN_J) {
		this.R32_COLUMN_J = R32_COLUMN_J;
	}

	public BigDecimal getR32_COLUMN_K() {
		return R32_COLUMN_K;
	}

	public void setR32_COLUMN_K(BigDecimal R32_COLUMN_K) {
		this.R32_COLUMN_K = R32_COLUMN_K;
	}

	public BigDecimal getR32_COLUMN_L() {
		return R32_COLUMN_L;
	}

	public void setR32_COLUMN_L(BigDecimal R32_COLUMN_L) {
		this.R32_COLUMN_L = R32_COLUMN_L;
	}

	public BigDecimal getR32_COLUMN_M() {
		return R32_COLUMN_M;
	}

	public void setR32_COLUMN_M(BigDecimal R32_COLUMN_M) {
		this.R32_COLUMN_M = R32_COLUMN_M;
	}

	public BigDecimal getR32_COLUMN_N() {
		return R32_COLUMN_N;
	}

	public void setR32_COLUMN_N(BigDecimal R32_COLUMN_N) {
		this.R32_COLUMN_N = R32_COLUMN_N;
	}

	public String getR33_COLUMN_A() {
		return R33_COLUMN_A;
	}

	public void setR33_COLUMN_A(String R33_COLUMN_A) {
		this.R33_COLUMN_A = R33_COLUMN_A;
	}

	public BigDecimal getR33_COLUMN_B() {
		return R33_COLUMN_B;
	}

	public void setR33_COLUMN_B(BigDecimal R33_COLUMN_B) {
		this.R33_COLUMN_B = R33_COLUMN_B;
	}

	public BigDecimal getR33_COLUMN_C() {
		return R33_COLUMN_C;
	}

	public void setR33_COLUMN_C(BigDecimal R33_COLUMN_C) {
		this.R33_COLUMN_C = R33_COLUMN_C;
	}

	public BigDecimal getR33_COLUMN_D() {
		return R33_COLUMN_D;
	}

	public void setR33_COLUMN_D(BigDecimal R33_COLUMN_D) {
		this.R33_COLUMN_D = R33_COLUMN_D;
	}

	public BigDecimal getR33_COLUMN_E() {
		return R33_COLUMN_E;
	}

	public void setR33_COLUMN_E(BigDecimal R33_COLUMN_E) {
		this.R33_COLUMN_E = R33_COLUMN_E;
	}

	public BigDecimal getR33_COLUMN_F() {
		return R33_COLUMN_F;
	}

	public void setR33_COLUMN_F(BigDecimal R33_COLUMN_F) {
		this.R33_COLUMN_F = R33_COLUMN_F;
	}

	public BigDecimal getR33_COLUMN_G() {
		return R33_COLUMN_G;
	}

	public void setR33_COLUMN_G(BigDecimal R33_COLUMN_G) {
		this.R33_COLUMN_G = R33_COLUMN_G;
	}

	public BigDecimal getR33_COLUMN_H() {
		return R33_COLUMN_H;
	}

	public void setR33_COLUMN_H(BigDecimal R33_COLUMN_H) {
		this.R33_COLUMN_H = R33_COLUMN_H;
	}

	public BigDecimal getR33_COLUMN_I() {
		return R33_COLUMN_I;
	}

	public void setR33_COLUMN_I(BigDecimal R33_COLUMN_I) {
		this.R33_COLUMN_I = R33_COLUMN_I;
	}

	public BigDecimal getR33_COLUMN_J() {
		return R33_COLUMN_J;
	}

	public void setR33_COLUMN_J(BigDecimal R33_COLUMN_J) {
		this.R33_COLUMN_J = R33_COLUMN_J;
	}

	public BigDecimal getR33_COLUMN_K() {
		return R33_COLUMN_K;
	}

	public void setR33_COLUMN_K(BigDecimal R33_COLUMN_K) {
		this.R33_COLUMN_K = R33_COLUMN_K;
	}

	public BigDecimal getR33_COLUMN_L() {
		return R33_COLUMN_L;
	}

	public void setR33_COLUMN_L(BigDecimal R33_COLUMN_L) {
		this.R33_COLUMN_L = R33_COLUMN_L;
	}

	public BigDecimal getR33_COLUMN_M() {
		return R33_COLUMN_M;
	}

	public void setR33_COLUMN_M(BigDecimal R33_COLUMN_M) {
		this.R33_COLUMN_M = R33_COLUMN_M;
	}

	public BigDecimal getR33_COLUMN_N() {
		return R33_COLUMN_N;
	}

	public void setR33_COLUMN_N(BigDecimal R33_COLUMN_N) {
		this.R33_COLUMN_N = R33_COLUMN_N;
	}

	public String getR34_COLUMN_A() {
		return R34_COLUMN_A;
	}

	public void setR34_COLUMN_A(String R34_COLUMN_A) {
		this.R34_COLUMN_A = R34_COLUMN_A;
	}

	public BigDecimal getR34_COLUMN_B() {
		return R34_COLUMN_B;
	}

	public void setR34_COLUMN_B(BigDecimal R34_COLUMN_B) {
		this.R34_COLUMN_B = R34_COLUMN_B;
	}

	public BigDecimal getR34_COLUMN_C() {
		return R34_COLUMN_C;
	}

	public void setR34_COLUMN_C(BigDecimal R34_COLUMN_C) {
		this.R34_COLUMN_C = R34_COLUMN_C;
	}

	public BigDecimal getR34_COLUMN_D() {
		return R34_COLUMN_D;
	}

	public void setR34_COLUMN_D(BigDecimal R34_COLUMN_D) {
		this.R34_COLUMN_D = R34_COLUMN_D;
	}

	public BigDecimal getR34_COLUMN_E() {
		return R34_COLUMN_E;
	}

	public void setR34_COLUMN_E(BigDecimal R34_COLUMN_E) {
		this.R34_COLUMN_E = R34_COLUMN_E;
	}

	public BigDecimal getR34_COLUMN_F() {
		return R34_COLUMN_F;
	}

	public void setR34_COLUMN_F(BigDecimal R34_COLUMN_F) {
		this.R34_COLUMN_F = R34_COLUMN_F;
	}

	public BigDecimal getR34_COLUMN_G() {
		return R34_COLUMN_G;
	}

	public void setR34_COLUMN_G(BigDecimal R34_COLUMN_G) {
		this.R34_COLUMN_G = R34_COLUMN_G;
	}

	public BigDecimal getR34_COLUMN_H() {
		return R34_COLUMN_H;
	}

	public void setR34_COLUMN_H(BigDecimal R34_COLUMN_H) {
		this.R34_COLUMN_H = R34_COLUMN_H;
	}

	public BigDecimal getR34_COLUMN_I() {
		return R34_COLUMN_I;
	}

	public void setR34_COLUMN_I(BigDecimal R34_COLUMN_I) {
		this.R34_COLUMN_I = R34_COLUMN_I;
	}

	public BigDecimal getR34_COLUMN_J() {
		return R34_COLUMN_J;
	}

	public void setR34_COLUMN_J(BigDecimal R34_COLUMN_J) {
		this.R34_COLUMN_J = R34_COLUMN_J;
	}

	public BigDecimal getR34_COLUMN_K() {
		return R34_COLUMN_K;
	}

	public void setR34_COLUMN_K(BigDecimal R34_COLUMN_K) {
		this.R34_COLUMN_K = R34_COLUMN_K;
	}

	public BigDecimal getR34_COLUMN_L() {
		return R34_COLUMN_L;
	}

	public void setR34_COLUMN_L(BigDecimal R34_COLUMN_L) {
		this.R34_COLUMN_L = R34_COLUMN_L;
	}

	public BigDecimal getR34_COLUMN_M() {
		return R34_COLUMN_M;
	}

	public void setR34_COLUMN_M(BigDecimal R34_COLUMN_M) {
		this.R34_COLUMN_M = R34_COLUMN_M;
	}

	public BigDecimal getR34_COLUMN_N() {
		return R34_COLUMN_N;
	}

	public void setR34_COLUMN_N(BigDecimal R34_COLUMN_N) {
		this.R34_COLUMN_N = R34_COLUMN_N;
	}

	public String getR35_COLUMN_A() {
		return R35_COLUMN_A;
	}

	public void setR35_COLUMN_A(String R35_COLUMN_A) {
		this.R35_COLUMN_A = R35_COLUMN_A;
	}

	public BigDecimal getR35_COLUMN_B() {
		return R35_COLUMN_B;
	}

	public void setR35_COLUMN_B(BigDecimal R35_COLUMN_B) {
		this.R35_COLUMN_B = R35_COLUMN_B;
	}

	public BigDecimal getR35_COLUMN_C() {
		return R35_COLUMN_C;
	}

	public void setR35_COLUMN_C(BigDecimal R35_COLUMN_C) {
		this.R35_COLUMN_C = R35_COLUMN_C;
	}

	public BigDecimal getR35_COLUMN_D() {
		return R35_COLUMN_D;
	}

	public void setR35_COLUMN_D(BigDecimal R35_COLUMN_D) {
		this.R35_COLUMN_D = R35_COLUMN_D;
	}

	public BigDecimal getR35_COLUMN_E() {
		return R35_COLUMN_E;
	}

	public void setR35_COLUMN_E(BigDecimal R35_COLUMN_E) {
		this.R35_COLUMN_E = R35_COLUMN_E;
	}

	public BigDecimal getR35_COLUMN_F() {
		return R35_COLUMN_F;
	}

	public void setR35_COLUMN_F(BigDecimal R35_COLUMN_F) {
		this.R35_COLUMN_F = R35_COLUMN_F;
	}

	public BigDecimal getR35_COLUMN_G() {
		return R35_COLUMN_G;
	}

	public void setR35_COLUMN_G(BigDecimal R35_COLUMN_G) {
		this.R35_COLUMN_G = R35_COLUMN_G;
	}

	public BigDecimal getR35_COLUMN_H() {
		return R35_COLUMN_H;
	}

	public void setR35_COLUMN_H(BigDecimal R35_COLUMN_H) {
		this.R35_COLUMN_H = R35_COLUMN_H;
	}

	public BigDecimal getR35_COLUMN_I() {
		return R35_COLUMN_I;
	}

	public void setR35_COLUMN_I(BigDecimal R35_COLUMN_I) {
		this.R35_COLUMN_I = R35_COLUMN_I;
	}

	public BigDecimal getR35_COLUMN_J() {
		return R35_COLUMN_J;
	}

	public void setR35_COLUMN_J(BigDecimal R35_COLUMN_J) {
		this.R35_COLUMN_J = R35_COLUMN_J;
	}

	public BigDecimal getR35_COLUMN_K() {
		return R35_COLUMN_K;
	}

	public void setR35_COLUMN_K(BigDecimal R35_COLUMN_K) {
		this.R35_COLUMN_K = R35_COLUMN_K;
	}

	public BigDecimal getR35_COLUMN_L() {
		return R35_COLUMN_L;
	}

	public void setR35_COLUMN_L(BigDecimal R35_COLUMN_L) {
		this.R35_COLUMN_L = R35_COLUMN_L;
	}

	public BigDecimal getR35_COLUMN_M() {
		return R35_COLUMN_M;
	}

	public void setR35_COLUMN_M(BigDecimal R35_COLUMN_M) {
		this.R35_COLUMN_M = R35_COLUMN_M;
	}

	public BigDecimal getR35_COLUMN_N() {
		return R35_COLUMN_N;
	}

	public void setR35_COLUMN_N(BigDecimal R35_COLUMN_N) {
		this.R35_COLUMN_N = R35_COLUMN_N;
	}

	public String getR36_COLUMN_A() {
		return R36_COLUMN_A;
	}

	public void setR36_COLUMN_A(String R36_COLUMN_A) {
		this.R36_COLUMN_A = R36_COLUMN_A;
	}

	public BigDecimal getR36_COLUMN_B() {
		return R36_COLUMN_B;
	}

	public void setR36_COLUMN_B(BigDecimal R36_COLUMN_B) {
		this.R36_COLUMN_B = R36_COLUMN_B;
	}

	public BigDecimal getR36_COLUMN_C() {
		return R36_COLUMN_C;
	}

	public void setR36_COLUMN_C(BigDecimal R36_COLUMN_C) {
		this.R36_COLUMN_C = R36_COLUMN_C;
	}

	public BigDecimal getR36_COLUMN_D() {
		return R36_COLUMN_D;
	}

	public void setR36_COLUMN_D(BigDecimal R36_COLUMN_D) {
		this.R36_COLUMN_D = R36_COLUMN_D;
	}

	public BigDecimal getR36_COLUMN_E() {
		return R36_COLUMN_E;
	}

	public void setR36_COLUMN_E(BigDecimal R36_COLUMN_E) {
		this.R36_COLUMN_E = R36_COLUMN_E;
	}

	public BigDecimal getR36_COLUMN_F() {
		return R36_COLUMN_F;
	}

	public void setR36_COLUMN_F(BigDecimal R36_COLUMN_F) {
		this.R36_COLUMN_F = R36_COLUMN_F;
	}

	public BigDecimal getR36_COLUMN_G() {
		return R36_COLUMN_G;
	}

	public void setR36_COLUMN_G(BigDecimal R36_COLUMN_G) {
		this.R36_COLUMN_G = R36_COLUMN_G;
	}

	public BigDecimal getR36_COLUMN_H() {
		return R36_COLUMN_H;
	}

	public void setR36_COLUMN_H(BigDecimal R36_COLUMN_H) {
		this.R36_COLUMN_H = R36_COLUMN_H;
	}

	public BigDecimal getR36_COLUMN_I() {
		return R36_COLUMN_I;
	}

	public void setR36_COLUMN_I(BigDecimal R36_COLUMN_I) {
		this.R36_COLUMN_I = R36_COLUMN_I;
	}

	public BigDecimal getR36_COLUMN_J() {
		return R36_COLUMN_J;
	}

	public void setR36_COLUMN_J(BigDecimal R36_COLUMN_J) {
		this.R36_COLUMN_J = R36_COLUMN_J;
	}

	public BigDecimal getR36_COLUMN_K() {
		return R36_COLUMN_K;
	}

	public void setR36_COLUMN_K(BigDecimal R36_COLUMN_K) {
		this.R36_COLUMN_K = R36_COLUMN_K;
	}

	public BigDecimal getR36_COLUMN_L() {
		return R36_COLUMN_L;
	}

	public void setR36_COLUMN_L(BigDecimal R36_COLUMN_L) {
		this.R36_COLUMN_L = R36_COLUMN_L;
	}

	public BigDecimal getR36_COLUMN_M() {
		return R36_COLUMN_M;
	}

	public void setR36_COLUMN_M(BigDecimal R36_COLUMN_M) {
		this.R36_COLUMN_M = R36_COLUMN_M;
	}

	public BigDecimal getR36_COLUMN_N() {
		return R36_COLUMN_N;
	}

	public void setR36_COLUMN_N(BigDecimal R36_COLUMN_N) {
		this.R36_COLUMN_N = R36_COLUMN_N;
	}

	public String getR37_COLUMN_A() {
		return R37_COLUMN_A;
	}

	public void setR37_COLUMN_A(String R37_COLUMN_A) {
		this.R37_COLUMN_A = R37_COLUMN_A;
	}

	public BigDecimal getR37_COLUMN_B() {
		return R37_COLUMN_B;
	}

	public void setR37_COLUMN_B(BigDecimal R37_COLUMN_B) {
		this.R37_COLUMN_B = R37_COLUMN_B;
	}

	public BigDecimal getR37_COLUMN_C() {
		return R37_COLUMN_C;
	}

	public void setR37_COLUMN_C(BigDecimal R37_COLUMN_C) {
		this.R37_COLUMN_C = R37_COLUMN_C;
	}

	public BigDecimal getR37_COLUMN_D() {
		return R37_COLUMN_D;
	}

	public void setR37_COLUMN_D(BigDecimal R37_COLUMN_D) {
		this.R37_COLUMN_D = R37_COLUMN_D;
	}

	public BigDecimal getR37_COLUMN_E() {
		return R37_COLUMN_E;
	}

	public void setR37_COLUMN_E(BigDecimal R37_COLUMN_E) {
		this.R37_COLUMN_E = R37_COLUMN_E;
	}

	public BigDecimal getR37_COLUMN_F() {
		return R37_COLUMN_F;
	}

	public void setR37_COLUMN_F(BigDecimal R37_COLUMN_F) {
		this.R37_COLUMN_F = R37_COLUMN_F;
	}

	public BigDecimal getR37_COLUMN_G() {
		return R37_COLUMN_G;
	}

	public void setR37_COLUMN_G(BigDecimal R37_COLUMN_G) {
		this.R37_COLUMN_G = R37_COLUMN_G;
	}

	public BigDecimal getR37_COLUMN_H() {
		return R37_COLUMN_H;
	}

	public void setR37_COLUMN_H(BigDecimal R37_COLUMN_H) {
		this.R37_COLUMN_H = R37_COLUMN_H;
	}

	public BigDecimal getR37_COLUMN_I() {
		return R37_COLUMN_I;
	}

	public void setR37_COLUMN_I(BigDecimal R37_COLUMN_I) {
		this.R37_COLUMN_I = R37_COLUMN_I;
	}

	public BigDecimal getR37_COLUMN_J() {
		return R37_COLUMN_J;
	}

	public void setR37_COLUMN_J(BigDecimal R37_COLUMN_J) {
		this.R37_COLUMN_J = R37_COLUMN_J;
	}

	public BigDecimal getR37_COLUMN_K() {
		return R37_COLUMN_K;
	}

	public void setR37_COLUMN_K(BigDecimal R37_COLUMN_K) {
		this.R37_COLUMN_K = R37_COLUMN_K;
	}

	public BigDecimal getR37_COLUMN_L() {
		return R37_COLUMN_L;
	}

	public void setR37_COLUMN_L(BigDecimal R37_COLUMN_L) {
		this.R37_COLUMN_L = R37_COLUMN_L;
	}

	public BigDecimal getR37_COLUMN_M() {
		return R37_COLUMN_M;
	}

	public void setR37_COLUMN_M(BigDecimal R37_COLUMN_M) {
		this.R37_COLUMN_M = R37_COLUMN_M;
	}

	public BigDecimal getR37_COLUMN_N() {
		return R37_COLUMN_N;
	}

	public void setR37_COLUMN_N(BigDecimal R37_COLUMN_N) {
		this.R37_COLUMN_N = R37_COLUMN_N;
	}

	public String getR38_COLUMN_A() {
		return R38_COLUMN_A;
	}

	public void setR38_COLUMN_A(String R38_COLUMN_A) {
		this.R38_COLUMN_A = R38_COLUMN_A;
	}

	public BigDecimal getR38_COLUMN_B() {
		return R38_COLUMN_B;
	}

	public void setR38_COLUMN_B(BigDecimal R38_COLUMN_B) {
		this.R38_COLUMN_B = R38_COLUMN_B;
	}

	public BigDecimal getR38_COLUMN_C() {
		return R38_COLUMN_C;
	}

	public void setR38_COLUMN_C(BigDecimal R38_COLUMN_C) {
		this.R38_COLUMN_C = R38_COLUMN_C;
	}

	public BigDecimal getR38_COLUMN_D() {
		return R38_COLUMN_D;
	}

	public void setR38_COLUMN_D(BigDecimal R38_COLUMN_D) {
		this.R38_COLUMN_D = R38_COLUMN_D;
	}

	public BigDecimal getR38_COLUMN_E() {
		return R38_COLUMN_E;
	}

	public void setR38_COLUMN_E(BigDecimal R38_COLUMN_E) {
		this.R38_COLUMN_E = R38_COLUMN_E;
	}

	public BigDecimal getR38_COLUMN_F() {
		return R38_COLUMN_F;
	}

	public void setR38_COLUMN_F(BigDecimal R38_COLUMN_F) {
		this.R38_COLUMN_F = R38_COLUMN_F;
	}

	public BigDecimal getR38_COLUMN_G() {
		return R38_COLUMN_G;
	}

	public void setR38_COLUMN_G(BigDecimal R38_COLUMN_G) {
		this.R38_COLUMN_G = R38_COLUMN_G;
	}

	public BigDecimal getR38_COLUMN_H() {
		return R38_COLUMN_H;
	}

	public void setR38_COLUMN_H(BigDecimal R38_COLUMN_H) {
		this.R38_COLUMN_H = R38_COLUMN_H;
	}

	public BigDecimal getR38_COLUMN_I() {
		return R38_COLUMN_I;
	}

	public void setR38_COLUMN_I(BigDecimal R38_COLUMN_I) {
		this.R38_COLUMN_I = R38_COLUMN_I;
	}

	public BigDecimal getR38_COLUMN_J() {
		return R38_COLUMN_J;
	}

	public void setR38_COLUMN_J(BigDecimal R38_COLUMN_J) {
		this.R38_COLUMN_J = R38_COLUMN_J;
	}

	public BigDecimal getR38_COLUMN_K() {
		return R38_COLUMN_K;
	}

	public void setR38_COLUMN_K(BigDecimal R38_COLUMN_K) {
		this.R38_COLUMN_K = R38_COLUMN_K;
	}

	public BigDecimal getR38_COLUMN_L() {
		return R38_COLUMN_L;
	}

	public void setR38_COLUMN_L(BigDecimal R38_COLUMN_L) {
		this.R38_COLUMN_L = R38_COLUMN_L;
	}

	public BigDecimal getR38_COLUMN_M() {
		return R38_COLUMN_M;
	}

	public void setR38_COLUMN_M(BigDecimal R38_COLUMN_M) {
		this.R38_COLUMN_M = R38_COLUMN_M;
	}

	public BigDecimal getR38_COLUMN_N() {
		return R38_COLUMN_N;
	}

	public void setR38_COLUMN_N(BigDecimal R38_COLUMN_N) {
		this.R38_COLUMN_N = R38_COLUMN_N;
	}

	public String getR39_COLUMN_A() {
		return R39_COLUMN_A;
	}

	public void setR39_COLUMN_A(String R39_COLUMN_A) {
		this.R39_COLUMN_A = R39_COLUMN_A;
	}

	public BigDecimal getR39_COLUMN_B() {
		return R39_COLUMN_B;
	}

	public void setR39_COLUMN_B(BigDecimal R39_COLUMN_B) {
		this.R39_COLUMN_B = R39_COLUMN_B;
	}

	public BigDecimal getR39_COLUMN_C() {
		return R39_COLUMN_C;
	}

	public void setR39_COLUMN_C(BigDecimal R39_COLUMN_C) {
		this.R39_COLUMN_C = R39_COLUMN_C;
	}

	public BigDecimal getR39_COLUMN_D() {
		return R39_COLUMN_D;
	}

	public void setR39_COLUMN_D(BigDecimal R39_COLUMN_D) {
		this.R39_COLUMN_D = R39_COLUMN_D;
	}

	public BigDecimal getR39_COLUMN_E() {
		return R39_COLUMN_E;
	}

	public void setR39_COLUMN_E(BigDecimal R39_COLUMN_E) {
		this.R39_COLUMN_E = R39_COLUMN_E;
	}

	public BigDecimal getR39_COLUMN_F() {
		return R39_COLUMN_F;
	}

	public void setR39_COLUMN_F(BigDecimal R39_COLUMN_F) {
		this.R39_COLUMN_F = R39_COLUMN_F;
	}

	public BigDecimal getR39_COLUMN_G() {
		return R39_COLUMN_G;
	}

	public void setR39_COLUMN_G(BigDecimal R39_COLUMN_G) {
		this.R39_COLUMN_G = R39_COLUMN_G;
	}

	public BigDecimal getR39_COLUMN_H() {
		return R39_COLUMN_H;
	}

	public void setR39_COLUMN_H(BigDecimal R39_COLUMN_H) {
		this.R39_COLUMN_H = R39_COLUMN_H;
	}

	public BigDecimal getR39_COLUMN_I() {
		return R39_COLUMN_I;
	}

	public void setR39_COLUMN_I(BigDecimal R39_COLUMN_I) {
		this.R39_COLUMN_I = R39_COLUMN_I;
	}

	public BigDecimal getR39_COLUMN_J() {
		return R39_COLUMN_J;
	}

	public void setR39_COLUMN_J(BigDecimal R39_COLUMN_J) {
		this.R39_COLUMN_J = R39_COLUMN_J;
	}

	public BigDecimal getR39_COLUMN_K() {
		return R39_COLUMN_K;
	}

	public void setR39_COLUMN_K(BigDecimal R39_COLUMN_K) {
		this.R39_COLUMN_K = R39_COLUMN_K;
	}

	public BigDecimal getR39_COLUMN_L() {
		return R39_COLUMN_L;
	}

	public void setR39_COLUMN_L(BigDecimal R39_COLUMN_L) {
		this.R39_COLUMN_L = R39_COLUMN_L;
	}

	public BigDecimal getR39_COLUMN_M() {
		return R39_COLUMN_M;
	}

	public void setR39_COLUMN_M(BigDecimal R39_COLUMN_M) {
		this.R39_COLUMN_M = R39_COLUMN_M;
	}

	public BigDecimal getR39_COLUMN_N() {
		return R39_COLUMN_N;
	}

	public void setR39_COLUMN_N(BigDecimal R39_COLUMN_N) {
		this.R39_COLUMN_N = R39_COLUMN_N;
	}

	public String getR40_COLUMN_A() {
		return R40_COLUMN_A;
	}

	public void setR40_COLUMN_A(String R40_COLUMN_A) {
		this.R40_COLUMN_A = R40_COLUMN_A;
	}

	public BigDecimal getR40_COLUMN_B() {
		return R40_COLUMN_B;
	}

	public void setR40_COLUMN_B(BigDecimal R40_COLUMN_B) {
		this.R40_COLUMN_B = R40_COLUMN_B;
	}

	public BigDecimal getR40_COLUMN_C() {
		return R40_COLUMN_C;
	}

	public void setR40_COLUMN_C(BigDecimal R40_COLUMN_C) {
		this.R40_COLUMN_C = R40_COLUMN_C;
	}

	public BigDecimal getR40_COLUMN_D() {
		return R40_COLUMN_D;
	}

	public void setR40_COLUMN_D(BigDecimal R40_COLUMN_D) {
		this.R40_COLUMN_D = R40_COLUMN_D;
	}

	public BigDecimal getR40_COLUMN_E() {
		return R40_COLUMN_E;
	}

	public void setR40_COLUMN_E(BigDecimal R40_COLUMN_E) {
		this.R40_COLUMN_E = R40_COLUMN_E;
	}

	public BigDecimal getR40_COLUMN_F() {
		return R40_COLUMN_F;
	}

	public void setR40_COLUMN_F(BigDecimal R40_COLUMN_F) {
		this.R40_COLUMN_F = R40_COLUMN_F;
	}

	public BigDecimal getR40_COLUMN_G() {
		return R40_COLUMN_G;
	}

	public void setR40_COLUMN_G(BigDecimal R40_COLUMN_G) {
		this.R40_COLUMN_G = R40_COLUMN_G;
	}

	public BigDecimal getR40_COLUMN_H() {
		return R40_COLUMN_H;
	}

	public void setR40_COLUMN_H(BigDecimal R40_COLUMN_H) {
		this.R40_COLUMN_H = R40_COLUMN_H;
	}

	public BigDecimal getR40_COLUMN_I() {
		return R40_COLUMN_I;
	}

	public void setR40_COLUMN_I(BigDecimal R40_COLUMN_I) {
		this.R40_COLUMN_I = R40_COLUMN_I;
	}

	public BigDecimal getR40_COLUMN_J() {
		return R40_COLUMN_J;
	}

	public void setR40_COLUMN_J(BigDecimal R40_COLUMN_J) {
		this.R40_COLUMN_J = R40_COLUMN_J;
	}

	public BigDecimal getR40_COLUMN_K() {
		return R40_COLUMN_K;
	}

	public void setR40_COLUMN_K(BigDecimal R40_COLUMN_K) {
		this.R40_COLUMN_K = R40_COLUMN_K;
	}

	public BigDecimal getR40_COLUMN_L() {
		return R40_COLUMN_L;
	}

	public void setR40_COLUMN_L(BigDecimal R40_COLUMN_L) {
		this.R40_COLUMN_L = R40_COLUMN_L;
	}

	public BigDecimal getR40_COLUMN_M() {
		return R40_COLUMN_M;
	}

	public void setR40_COLUMN_M(BigDecimal R40_COLUMN_M) {
		this.R40_COLUMN_M = R40_COLUMN_M;
	}

	public BigDecimal getR40_COLUMN_N() {
		return R40_COLUMN_N;
	}

	public void setR40_COLUMN_N(BigDecimal R40_COLUMN_N) {
		this.R40_COLUMN_N = R40_COLUMN_N;
	}

	public String getR41_COLUMN_A() {
		return R41_COLUMN_A;
	}

	public void setR41_COLUMN_A(String R41_COLUMN_A) {
		this.R41_COLUMN_A = R41_COLUMN_A;
	}

	public BigDecimal getR41_COLUMN_B() {
		return R41_COLUMN_B;
	}

	public void setR41_COLUMN_B(BigDecimal R41_COLUMN_B) {
		this.R41_COLUMN_B = R41_COLUMN_B;
	}

	public BigDecimal getR41_COLUMN_C() {
		return R41_COLUMN_C;
	}

	public void setR41_COLUMN_C(BigDecimal R41_COLUMN_C) {
		this.R41_COLUMN_C = R41_COLUMN_C;
	}

	public BigDecimal getR41_COLUMN_D() {
		return R41_COLUMN_D;
	}

	public void setR41_COLUMN_D(BigDecimal R41_COLUMN_D) {
		this.R41_COLUMN_D = R41_COLUMN_D;
	}

	public BigDecimal getR41_COLUMN_E() {
		return R41_COLUMN_E;
	}

	public void setR41_COLUMN_E(BigDecimal R41_COLUMN_E) {
		this.R41_COLUMN_E = R41_COLUMN_E;
	}

	public BigDecimal getR41_COLUMN_F() {
		return R41_COLUMN_F;
	}

	public void setR41_COLUMN_F(BigDecimal R41_COLUMN_F) {
		this.R41_COLUMN_F = R41_COLUMN_F;
	}

	public BigDecimal getR41_COLUMN_G() {
		return R41_COLUMN_G;
	}

	public void setR41_COLUMN_G(BigDecimal R41_COLUMN_G) {
		this.R41_COLUMN_G = R41_COLUMN_G;
	}

	public BigDecimal getR41_COLUMN_H() {
		return R41_COLUMN_H;
	}

	public void setR41_COLUMN_H(BigDecimal R41_COLUMN_H) {
		this.R41_COLUMN_H = R41_COLUMN_H;
	}

	public BigDecimal getR41_COLUMN_I() {
		return R41_COLUMN_I;
	}

	public void setR41_COLUMN_I(BigDecimal R41_COLUMN_I) {
		this.R41_COLUMN_I = R41_COLUMN_I;
	}

	public BigDecimal getR41_COLUMN_J() {
		return R41_COLUMN_J;
	}

	public void setR41_COLUMN_J(BigDecimal R41_COLUMN_J) {
		this.R41_COLUMN_J = R41_COLUMN_J;
	}

	public BigDecimal getR41_COLUMN_K() {
		return R41_COLUMN_K;
	}

	public void setR41_COLUMN_K(BigDecimal R41_COLUMN_K) {
		this.R41_COLUMN_K = R41_COLUMN_K;
	}

	public BigDecimal getR41_COLUMN_L() {
		return R41_COLUMN_L;
	}

	public void setR41_COLUMN_L(BigDecimal R41_COLUMN_L) {
		this.R41_COLUMN_L = R41_COLUMN_L;
	}

	public BigDecimal getR41_COLUMN_M() {
		return R41_COLUMN_M;
	}

	public void setR41_COLUMN_M(BigDecimal R41_COLUMN_M) {
		this.R41_COLUMN_M = R41_COLUMN_M;
	}

	public BigDecimal getR41_COLUMN_N() {
		return R41_COLUMN_N;
	}

	public void setR41_COLUMN_N(BigDecimal R41_COLUMN_N) {
		this.R41_COLUMN_N = R41_COLUMN_N;
	}

	public String getR42_COLUMN_A() {
		return R42_COLUMN_A;
	}

	public void setR42_COLUMN_A(String R42_COLUMN_A) {
		this.R42_COLUMN_A = R42_COLUMN_A;
	}

	public BigDecimal getR42_COLUMN_B() {
		return R42_COLUMN_B;
	}

	public void setR42_COLUMN_B(BigDecimal R42_COLUMN_B) {
		this.R42_COLUMN_B = R42_COLUMN_B;
	}

	public BigDecimal getR42_COLUMN_C() {
		return R42_COLUMN_C;
	}

	public void setR42_COLUMN_C(BigDecimal R42_COLUMN_C) {
		this.R42_COLUMN_C = R42_COLUMN_C;
	}

	public BigDecimal getR42_COLUMN_D() {
		return R42_COLUMN_D;
	}

	public void setR42_COLUMN_D(BigDecimal R42_COLUMN_D) {
		this.R42_COLUMN_D = R42_COLUMN_D;
	}

	public BigDecimal getR42_COLUMN_E() {
		return R42_COLUMN_E;
	}

	public void setR42_COLUMN_E(BigDecimal R42_COLUMN_E) {
		this.R42_COLUMN_E = R42_COLUMN_E;
	}

	public BigDecimal getR42_COLUMN_F() {
		return R42_COLUMN_F;
	}

	public void setR42_COLUMN_F(BigDecimal R42_COLUMN_F) {
		this.R42_COLUMN_F = R42_COLUMN_F;
	}

	public BigDecimal getR42_COLUMN_G() {
		return R42_COLUMN_G;
	}

	public void setR42_COLUMN_G(BigDecimal R42_COLUMN_G) {
		this.R42_COLUMN_G = R42_COLUMN_G;
	}

	public BigDecimal getR42_COLUMN_H() {
		return R42_COLUMN_H;
	}

	public void setR42_COLUMN_H(BigDecimal R42_COLUMN_H) {
		this.R42_COLUMN_H = R42_COLUMN_H;
	}

	public BigDecimal getR42_COLUMN_I() {
		return R42_COLUMN_I;
	}

	public void setR42_COLUMN_I(BigDecimal R42_COLUMN_I) {
		this.R42_COLUMN_I = R42_COLUMN_I;
	}

	public BigDecimal getR42_COLUMN_J() {
		return R42_COLUMN_J;
	}

	public void setR42_COLUMN_J(BigDecimal R42_COLUMN_J) {
		this.R42_COLUMN_J = R42_COLUMN_J;
	}

	public BigDecimal getR42_COLUMN_K() {
		return R42_COLUMN_K;
	}

	public void setR42_COLUMN_K(BigDecimal R42_COLUMN_K) {
		this.R42_COLUMN_K = R42_COLUMN_K;
	}

	public BigDecimal getR42_COLUMN_L() {
		return R42_COLUMN_L;
	}

	public void setR42_COLUMN_L(BigDecimal R42_COLUMN_L) {
		this.R42_COLUMN_L = R42_COLUMN_L;
	}

	public BigDecimal getR42_COLUMN_M() {
		return R42_COLUMN_M;
	}

	public void setR42_COLUMN_M(BigDecimal R42_COLUMN_M) {
		this.R42_COLUMN_M = R42_COLUMN_M;
	}

	public BigDecimal getR42_COLUMN_N() {
		return R42_COLUMN_N;
	}

	public void setR42_COLUMN_N(BigDecimal R42_COLUMN_N) {
		this.R42_COLUMN_N = R42_COLUMN_N;
	}

	public String getR43_COLUMN_A() {
		return R43_COLUMN_A;
	}

	public void setR43_COLUMN_A(String R43_COLUMN_A) {
		this.R43_COLUMN_A = R43_COLUMN_A;
	}

	public BigDecimal getR43_COLUMN_B() {
		return R43_COLUMN_B;
	}

	public void setR43_COLUMN_B(BigDecimal R43_COLUMN_B) {
		this.R43_COLUMN_B = R43_COLUMN_B;
	}

	public BigDecimal getR43_COLUMN_C() {
		return R43_COLUMN_C;
	}

	public void setR43_COLUMN_C(BigDecimal R43_COLUMN_C) {
		this.R43_COLUMN_C = R43_COLUMN_C;
	}

	public BigDecimal getR43_COLUMN_D() {
		return R43_COLUMN_D;
	}

	public void setR43_COLUMN_D(BigDecimal R43_COLUMN_D) {
		this.R43_COLUMN_D = R43_COLUMN_D;
	}

	public BigDecimal getR43_COLUMN_E() {
		return R43_COLUMN_E;
	}

	public void setR43_COLUMN_E(BigDecimal R43_COLUMN_E) {
		this.R43_COLUMN_E = R43_COLUMN_E;
	}

	public BigDecimal getR43_COLUMN_F() {
		return R43_COLUMN_F;
	}

	public void setR43_COLUMN_F(BigDecimal R43_COLUMN_F) {
		this.R43_COLUMN_F = R43_COLUMN_F;
	}

	public BigDecimal getR43_COLUMN_G() {
		return R43_COLUMN_G;
	}

	public void setR43_COLUMN_G(BigDecimal R43_COLUMN_G) {
		this.R43_COLUMN_G = R43_COLUMN_G;
	}

	public BigDecimal getR43_COLUMN_H() {
		return R43_COLUMN_H;
	}

	public void setR43_COLUMN_H(BigDecimal R43_COLUMN_H) {
		this.R43_COLUMN_H = R43_COLUMN_H;
	}

	public BigDecimal getR43_COLUMN_I() {
		return R43_COLUMN_I;
	}

	public void setR43_COLUMN_I(BigDecimal R43_COLUMN_I) {
		this.R43_COLUMN_I = R43_COLUMN_I;
	}

	public BigDecimal getR43_COLUMN_J() {
		return R43_COLUMN_J;
	}

	public void setR43_COLUMN_J(BigDecimal R43_COLUMN_J) {
		this.R43_COLUMN_J = R43_COLUMN_J;
	}

	public BigDecimal getR43_COLUMN_K() {
		return R43_COLUMN_K;
	}

	public void setR43_COLUMN_K(BigDecimal R43_COLUMN_K) {
		this.R43_COLUMN_K = R43_COLUMN_K;
	}

	public BigDecimal getR43_COLUMN_L() {
		return R43_COLUMN_L;
	}

	public void setR43_COLUMN_L(BigDecimal R43_COLUMN_L) {
		this.R43_COLUMN_L = R43_COLUMN_L;
	}

	public BigDecimal getR43_COLUMN_M() {
		return R43_COLUMN_M;
	}

	public void setR43_COLUMN_M(BigDecimal R43_COLUMN_M) {
		this.R43_COLUMN_M = R43_COLUMN_M;
	}

	public BigDecimal getR43_COLUMN_N() {
		return R43_COLUMN_N;
	}

	public void setR43_COLUMN_N(BigDecimal R43_COLUMN_N) {
		this.R43_COLUMN_N = R43_COLUMN_N;
	}

	public String getR44_COLUMN_A() {
		return R44_COLUMN_A;
	}

	public void setR44_COLUMN_A(String R44_COLUMN_A) {
		this.R44_COLUMN_A = R44_COLUMN_A;
	}

	public BigDecimal getR44_COLUMN_B() {
		return R44_COLUMN_B;
	}

	public void setR44_COLUMN_B(BigDecimal R44_COLUMN_B) {
		this.R44_COLUMN_B = R44_COLUMN_B;
	}

	public BigDecimal getR44_COLUMN_C() {
		return R44_COLUMN_C;
	}

	public void setR44_COLUMN_C(BigDecimal R44_COLUMN_C) {
		this.R44_COLUMN_C = R44_COLUMN_C;
	}

	public BigDecimal getR44_COLUMN_D() {
		return R44_COLUMN_D;
	}

	public void setR44_COLUMN_D(BigDecimal R44_COLUMN_D) {
		this.R44_COLUMN_D = R44_COLUMN_D;
	}

	public BigDecimal getR44_COLUMN_E() {
		return R44_COLUMN_E;
	}

	public void setR44_COLUMN_E(BigDecimal R44_COLUMN_E) {
		this.R44_COLUMN_E = R44_COLUMN_E;
	}

	public BigDecimal getR44_COLUMN_F() {
		return R44_COLUMN_F;
	}

	public void setR44_COLUMN_F(BigDecimal R44_COLUMN_F) {
		this.R44_COLUMN_F = R44_COLUMN_F;
	}

	public BigDecimal getR44_COLUMN_G() {
		return R44_COLUMN_G;
	}

	public void setR44_COLUMN_G(BigDecimal R44_COLUMN_G) {
		this.R44_COLUMN_G = R44_COLUMN_G;
	}

	public BigDecimal getR44_COLUMN_H() {
		return R44_COLUMN_H;
	}

	public void setR44_COLUMN_H(BigDecimal R44_COLUMN_H) {
		this.R44_COLUMN_H = R44_COLUMN_H;
	}

	public BigDecimal getR44_COLUMN_I() {
		return R44_COLUMN_I;
	}

	public void setR44_COLUMN_I(BigDecimal R44_COLUMN_I) {
		this.R44_COLUMN_I = R44_COLUMN_I;
	}

	public BigDecimal getR44_COLUMN_J() {
		return R44_COLUMN_J;
	}

	public void setR44_COLUMN_J(BigDecimal R44_COLUMN_J) {
		this.R44_COLUMN_J = R44_COLUMN_J;
	}

	public BigDecimal getR44_COLUMN_K() {
		return R44_COLUMN_K;
	}

	public void setR44_COLUMN_K(BigDecimal R44_COLUMN_K) {
		this.R44_COLUMN_K = R44_COLUMN_K;
	}

	public BigDecimal getR44_COLUMN_L() {
		return R44_COLUMN_L;
	}

	public void setR44_COLUMN_L(BigDecimal R44_COLUMN_L) {
		this.R44_COLUMN_L = R44_COLUMN_L;
	}

	public BigDecimal getR44_COLUMN_M() {
		return R44_COLUMN_M;
	}

	public void setR44_COLUMN_M(BigDecimal R44_COLUMN_M) {
		this.R44_COLUMN_M = R44_COLUMN_M;
	}

	public BigDecimal getR44_COLUMN_N() {
		return R44_COLUMN_N;
	}

	public void setR44_COLUMN_N(BigDecimal R44_COLUMN_N) {
		this.R44_COLUMN_N = R44_COLUMN_N;
	}

	public String getR45_COLUMN_A() {
		return R45_COLUMN_A;
	}

	public void setR45_COLUMN_A(String R45_COLUMN_A) {
		this.R45_COLUMN_A = R45_COLUMN_A;
	}

	public BigDecimal getR45_COLUMN_B() {
		return R45_COLUMN_B;
	}

	public void setR45_COLUMN_B(BigDecimal R45_COLUMN_B) {
		this.R45_COLUMN_B = R45_COLUMN_B;
	}

	public BigDecimal getR45_COLUMN_C() {
		return R45_COLUMN_C;
	}

	public void setR45_COLUMN_C(BigDecimal R45_COLUMN_C) {
		this.R45_COLUMN_C = R45_COLUMN_C;
	}

	public BigDecimal getR45_COLUMN_D() {
		return R45_COLUMN_D;
	}

	public void setR45_COLUMN_D(BigDecimal R45_COLUMN_D) {
		this.R45_COLUMN_D = R45_COLUMN_D;
	}

	public BigDecimal getR45_COLUMN_E() {
		return R45_COLUMN_E;
	}

	public void setR45_COLUMN_E(BigDecimal R45_COLUMN_E) {
		this.R45_COLUMN_E = R45_COLUMN_E;
	}

	public BigDecimal getR45_COLUMN_F() {
		return R45_COLUMN_F;
	}

	public void setR45_COLUMN_F(BigDecimal R45_COLUMN_F) {
		this.R45_COLUMN_F = R45_COLUMN_F;
	}

	public BigDecimal getR45_COLUMN_G() {
		return R45_COLUMN_G;
	}

	public void setR45_COLUMN_G(BigDecimal R45_COLUMN_G) {
		this.R45_COLUMN_G = R45_COLUMN_G;
	}

	public BigDecimal getR45_COLUMN_H() {
		return R45_COLUMN_H;
	}

	public void setR45_COLUMN_H(BigDecimal R45_COLUMN_H) {
		this.R45_COLUMN_H = R45_COLUMN_H;
	}

	public BigDecimal getR45_COLUMN_I() {
		return R45_COLUMN_I;
	}

	public void setR45_COLUMN_I(BigDecimal R45_COLUMN_I) {
		this.R45_COLUMN_I = R45_COLUMN_I;
	}

	public BigDecimal getR45_COLUMN_J() {
		return R45_COLUMN_J;
	}

	public void setR45_COLUMN_J(BigDecimal R45_COLUMN_J) {
		this.R45_COLUMN_J = R45_COLUMN_J;
	}

	public BigDecimal getR45_COLUMN_K() {
		return R45_COLUMN_K;
	}

	public void setR45_COLUMN_K(BigDecimal R45_COLUMN_K) {
		this.R45_COLUMN_K = R45_COLUMN_K;
	}

	public BigDecimal getR45_COLUMN_L() {
		return R45_COLUMN_L;
	}

	public void setR45_COLUMN_L(BigDecimal R45_COLUMN_L) {
		this.R45_COLUMN_L = R45_COLUMN_L;
	}

	public BigDecimal getR45_COLUMN_M() {
		return R45_COLUMN_M;
	}

	public void setR45_COLUMN_M(BigDecimal R45_COLUMN_M) {
		this.R45_COLUMN_M = R45_COLUMN_M;
	}

	public BigDecimal getR45_COLUMN_N() {
		return R45_COLUMN_N;
	}

	public void setR45_COLUMN_N(BigDecimal R45_COLUMN_N) {
		this.R45_COLUMN_N = R45_COLUMN_N;
	}

	public String getR46_COLUMN_A() {
		return R46_COLUMN_A;
	}

	public void setR46_COLUMN_A(String R46_COLUMN_A) {
		this.R46_COLUMN_A = R46_COLUMN_A;
	}

	public BigDecimal getR46_COLUMN_B() {
		return R46_COLUMN_B;
	}

	public void setR46_COLUMN_B(BigDecimal R46_COLUMN_B) {
		this.R46_COLUMN_B = R46_COLUMN_B;
	}

	public BigDecimal getR46_COLUMN_C() {
		return R46_COLUMN_C;
	}

	public void setR46_COLUMN_C(BigDecimal R46_COLUMN_C) {
		this.R46_COLUMN_C = R46_COLUMN_C;
	}

	public BigDecimal getR46_COLUMN_D() {
		return R46_COLUMN_D;
	}

	public void setR46_COLUMN_D(BigDecimal R46_COLUMN_D) {
		this.R46_COLUMN_D = R46_COLUMN_D;
	}

	public BigDecimal getR46_COLUMN_E() {
		return R46_COLUMN_E;
	}

	public void setR46_COLUMN_E(BigDecimal R46_COLUMN_E) {
		this.R46_COLUMN_E = R46_COLUMN_E;
	}

	public BigDecimal getR46_COLUMN_F() {
		return R46_COLUMN_F;
	}

	public void setR46_COLUMN_F(BigDecimal R46_COLUMN_F) {
		this.R46_COLUMN_F = R46_COLUMN_F;
	}

	public BigDecimal getR46_COLUMN_G() {
		return R46_COLUMN_G;
	}

	public void setR46_COLUMN_G(BigDecimal R46_COLUMN_G) {
		this.R46_COLUMN_G = R46_COLUMN_G;
	}

	public BigDecimal getR46_COLUMN_H() {
		return R46_COLUMN_H;
	}

	public void setR46_COLUMN_H(BigDecimal R46_COLUMN_H) {
		this.R46_COLUMN_H = R46_COLUMN_H;
	}

	public BigDecimal getR46_COLUMN_I() {
		return R46_COLUMN_I;
	}

	public void setR46_COLUMN_I(BigDecimal R46_COLUMN_I) {
		this.R46_COLUMN_I = R46_COLUMN_I;
	}

	public BigDecimal getR46_COLUMN_J() {
		return R46_COLUMN_J;
	}

	public void setR46_COLUMN_J(BigDecimal R46_COLUMN_J) {
		this.R46_COLUMN_J = R46_COLUMN_J;
	}

	public BigDecimal getR46_COLUMN_K() {
		return R46_COLUMN_K;
	}

	public void setR46_COLUMN_K(BigDecimal R46_COLUMN_K) {
		this.R46_COLUMN_K = R46_COLUMN_K;
	}

	public BigDecimal getR46_COLUMN_L() {
		return R46_COLUMN_L;
	}

	public void setR46_COLUMN_L(BigDecimal R46_COLUMN_L) {
		this.R46_COLUMN_L = R46_COLUMN_L;
	}

	public BigDecimal getR46_COLUMN_M() {
		return R46_COLUMN_M;
	}

	public void setR46_COLUMN_M(BigDecimal R46_COLUMN_M) {
		this.R46_COLUMN_M = R46_COLUMN_M;
	}

	public BigDecimal getR46_COLUMN_N() {
		return R46_COLUMN_N;
	}

	public void setR46_COLUMN_N(BigDecimal R46_COLUMN_N) {
		this.R46_COLUMN_N = R46_COLUMN_N;
	}

	public String getR47_COLUMN_A() {
		return R47_COLUMN_A;
	}

	public void setR47_COLUMN_A(String R47_COLUMN_A) {
		this.R47_COLUMN_A = R47_COLUMN_A;
	}

	public BigDecimal getR47_COLUMN_B() {
		return R47_COLUMN_B;
	}

	public void setR47_COLUMN_B(BigDecimal R47_COLUMN_B) {
		this.R47_COLUMN_B = R47_COLUMN_B;
	}

	public BigDecimal getR47_COLUMN_C() {
		return R47_COLUMN_C;
	}

	public void setR47_COLUMN_C(BigDecimal R47_COLUMN_C) {
		this.R47_COLUMN_C = R47_COLUMN_C;
	}

	public BigDecimal getR47_COLUMN_D() {
		return R47_COLUMN_D;
	}

	public void setR47_COLUMN_D(BigDecimal R47_COLUMN_D) {
		this.R47_COLUMN_D = R47_COLUMN_D;
	}

	public BigDecimal getR47_COLUMN_E() {
		return R47_COLUMN_E;
	}

	public void setR47_COLUMN_E(BigDecimal R47_COLUMN_E) {
		this.R47_COLUMN_E = R47_COLUMN_E;
	}

	public BigDecimal getR47_COLUMN_F() {
		return R47_COLUMN_F;
	}

	public void setR47_COLUMN_F(BigDecimal R47_COLUMN_F) {
		this.R47_COLUMN_F = R47_COLUMN_F;
	}

	public BigDecimal getR47_COLUMN_G() {
		return R47_COLUMN_G;
	}

	public void setR47_COLUMN_G(BigDecimal R47_COLUMN_G) {
		this.R47_COLUMN_G = R47_COLUMN_G;
	}

	public BigDecimal getR47_COLUMN_H() {
		return R47_COLUMN_H;
	}

	public void setR47_COLUMN_H(BigDecimal R47_COLUMN_H) {
		this.R47_COLUMN_H = R47_COLUMN_H;
	}

	public BigDecimal getR47_COLUMN_I() {
		return R47_COLUMN_I;
	}

	public void setR47_COLUMN_I(BigDecimal R47_COLUMN_I) {
		this.R47_COLUMN_I = R47_COLUMN_I;
	}

	public BigDecimal getR47_COLUMN_J() {
		return R47_COLUMN_J;
	}

	public void setR47_COLUMN_J(BigDecimal R47_COLUMN_J) {
		this.R47_COLUMN_J = R47_COLUMN_J;
	}

	public BigDecimal getR47_COLUMN_K() {
		return R47_COLUMN_K;
	}

	public void setR47_COLUMN_K(BigDecimal R47_COLUMN_K) {
		this.R47_COLUMN_K = R47_COLUMN_K;
	}

	public BigDecimal getR47_COLUMN_L() {
		return R47_COLUMN_L;
	}

	public void setR47_COLUMN_L(BigDecimal R47_COLUMN_L) {
		this.R47_COLUMN_L = R47_COLUMN_L;
	}

	public BigDecimal getR47_COLUMN_M() {
		return R47_COLUMN_M;
	}

	public void setR47_COLUMN_M(BigDecimal R47_COLUMN_M) {
		this.R47_COLUMN_M = R47_COLUMN_M;
	}

	public BigDecimal getR47_COLUMN_N() {
		return R47_COLUMN_N;
	}

	public void setR47_COLUMN_N(BigDecimal R47_COLUMN_N) {
		this.R47_COLUMN_N = R47_COLUMN_N;
	}

	public String getR48_COLUMN_A() {
		return R48_COLUMN_A;
	}

	public void setR48_COLUMN_A(String R48_COLUMN_A) {
		this.R48_COLUMN_A = R48_COLUMN_A;
	}

	public BigDecimal getR48_COLUMN_B() {
		return R48_COLUMN_B;
	}

	public void setR48_COLUMN_B(BigDecimal R48_COLUMN_B) {
		this.R48_COLUMN_B = R48_COLUMN_B;
	}

	public BigDecimal getR48_COLUMN_C() {
		return R48_COLUMN_C;
	}

	public void setR48_COLUMN_C(BigDecimal R48_COLUMN_C) {
		this.R48_COLUMN_C = R48_COLUMN_C;
	}

	public BigDecimal getR48_COLUMN_D() {
		return R48_COLUMN_D;
	}

	public void setR48_COLUMN_D(BigDecimal R48_COLUMN_D) {
		this.R48_COLUMN_D = R48_COLUMN_D;
	}

	public BigDecimal getR48_COLUMN_E() {
		return R48_COLUMN_E;
	}

	public void setR48_COLUMN_E(BigDecimal R48_COLUMN_E) {
		this.R48_COLUMN_E = R48_COLUMN_E;
	}

	public BigDecimal getR48_COLUMN_F() {
		return R48_COLUMN_F;
	}

	public void setR48_COLUMN_F(BigDecimal R48_COLUMN_F) {
		this.R48_COLUMN_F = R48_COLUMN_F;
	}

	public BigDecimal getR48_COLUMN_G() {
		return R48_COLUMN_G;
	}

	public void setR48_COLUMN_G(BigDecimal R48_COLUMN_G) {
		this.R48_COLUMN_G = R48_COLUMN_G;
	}

	public BigDecimal getR48_COLUMN_H() {
		return R48_COLUMN_H;
	}

	public void setR48_COLUMN_H(BigDecimal R48_COLUMN_H) {
		this.R48_COLUMN_H = R48_COLUMN_H;
	}

	public BigDecimal getR48_COLUMN_I() {
		return R48_COLUMN_I;
	}

	public void setR48_COLUMN_I(BigDecimal R48_COLUMN_I) {
		this.R48_COLUMN_I = R48_COLUMN_I;
	}

	public BigDecimal getR48_COLUMN_J() {
		return R48_COLUMN_J;
	}

	public void setR48_COLUMN_J(BigDecimal R48_COLUMN_J) {
		this.R48_COLUMN_J = R48_COLUMN_J;
	}

	public BigDecimal getR48_COLUMN_K() {
		return R48_COLUMN_K;
	}

	public void setR48_COLUMN_K(BigDecimal R48_COLUMN_K) {
		this.R48_COLUMN_K = R48_COLUMN_K;
	}

	public BigDecimal getR48_COLUMN_L() {
		return R48_COLUMN_L;
	}

	public void setR48_COLUMN_L(BigDecimal R48_COLUMN_L) {
		this.R48_COLUMN_L = R48_COLUMN_L;
	}

	public BigDecimal getR48_COLUMN_M() {
		return R48_COLUMN_M;
	}

	public void setR48_COLUMN_M(BigDecimal R48_COLUMN_M) {
		this.R48_COLUMN_M = R48_COLUMN_M;
	}

	public BigDecimal getR48_COLUMN_N() {
		return R48_COLUMN_N;
	}

	public void setR48_COLUMN_N(BigDecimal R48_COLUMN_N) {
		this.R48_COLUMN_N = R48_COLUMN_N;
	}

	public String getR49_COLUMN_A() {
		return R49_COLUMN_A;
	}

	public void setR49_COLUMN_A(String R49_COLUMN_A) {
		this.R49_COLUMN_A = R49_COLUMN_A;
	}

	public BigDecimal getR49_COLUMN_B() {
		return R49_COLUMN_B;
	}

	public void setR49_COLUMN_B(BigDecimal R49_COLUMN_B) {
		this.R49_COLUMN_B = R49_COLUMN_B;
	}

	public BigDecimal getR49_COLUMN_C() {
		return R49_COLUMN_C;
	}

	public void setR49_COLUMN_C(BigDecimal R49_COLUMN_C) {
		this.R49_COLUMN_C = R49_COLUMN_C;
	}

	public BigDecimal getR49_COLUMN_D() {
		return R49_COLUMN_D;
	}

	public void setR49_COLUMN_D(BigDecimal R49_COLUMN_D) {
		this.R49_COLUMN_D = R49_COLUMN_D;
	}

	public BigDecimal getR49_COLUMN_E() {
		return R49_COLUMN_E;
	}

	public void setR49_COLUMN_E(BigDecimal R49_COLUMN_E) {
		this.R49_COLUMN_E = R49_COLUMN_E;
	}

	public BigDecimal getR49_COLUMN_F() {
		return R49_COLUMN_F;
	}

	public void setR49_COLUMN_F(BigDecimal R49_COLUMN_F) {
		this.R49_COLUMN_F = R49_COLUMN_F;
	}

	public BigDecimal getR49_COLUMN_G() {
		return R49_COLUMN_G;
	}

	public void setR49_COLUMN_G(BigDecimal R49_COLUMN_G) {
		this.R49_COLUMN_G = R49_COLUMN_G;
	}

	public BigDecimal getR49_COLUMN_H() {
		return R49_COLUMN_H;
	}

	public void setR49_COLUMN_H(BigDecimal R49_COLUMN_H) {
		this.R49_COLUMN_H = R49_COLUMN_H;
	}

	public BigDecimal getR49_COLUMN_I() {
		return R49_COLUMN_I;
	}

	public void setR49_COLUMN_I(BigDecimal R49_COLUMN_I) {
		this.R49_COLUMN_I = R49_COLUMN_I;
	}

	public BigDecimal getR49_COLUMN_J() {
		return R49_COLUMN_J;
	}

	public void setR49_COLUMN_J(BigDecimal R49_COLUMN_J) {
		this.R49_COLUMN_J = R49_COLUMN_J;
	}

	public BigDecimal getR49_COLUMN_K() {
		return R49_COLUMN_K;
	}

	public void setR49_COLUMN_K(BigDecimal R49_COLUMN_K) {
		this.R49_COLUMN_K = R49_COLUMN_K;
	}

	public BigDecimal getR49_COLUMN_L() {
		return R49_COLUMN_L;
	}

	public void setR49_COLUMN_L(BigDecimal R49_COLUMN_L) {
		this.R49_COLUMN_L = R49_COLUMN_L;
	}

	public BigDecimal getR49_COLUMN_M() {
		return R49_COLUMN_M;
	}

	public void setR49_COLUMN_M(BigDecimal R49_COLUMN_M) {
		this.R49_COLUMN_M = R49_COLUMN_M;
	}

	public BigDecimal getR49_COLUMN_N() {
		return R49_COLUMN_N;
	}

	public void setR49_COLUMN_N(BigDecimal R49_COLUMN_N) {
		this.R49_COLUMN_N = R49_COLUMN_N;
	}

	public String getR50_COLUMN_A() {
		return R50_COLUMN_A;
	}

	public void setR50_COLUMN_A(String R50_COLUMN_A) {
		this.R50_COLUMN_A = R50_COLUMN_A;
	}

	public BigDecimal getR50_COLUMN_B() {
		return R50_COLUMN_B;
	}

	public void setR50_COLUMN_B(BigDecimal R50_COLUMN_B) {
		this.R50_COLUMN_B = R50_COLUMN_B;
	}

	public BigDecimal getR50_COLUMN_C() {
		return R50_COLUMN_C;
	}

	public void setR50_COLUMN_C(BigDecimal R50_COLUMN_C) {
		this.R50_COLUMN_C = R50_COLUMN_C;
	}

	public BigDecimal getR50_COLUMN_D() {
		return R50_COLUMN_D;
	}

	public void setR50_COLUMN_D(BigDecimal R50_COLUMN_D) {
		this.R50_COLUMN_D = R50_COLUMN_D;
	}

	public BigDecimal getR50_COLUMN_E() {
		return R50_COLUMN_E;
	}

	public void setR50_COLUMN_E(BigDecimal R50_COLUMN_E) {
		this.R50_COLUMN_E = R50_COLUMN_E;
	}

	public BigDecimal getR50_COLUMN_F() {
		return R50_COLUMN_F;
	}

	public void setR50_COLUMN_F(BigDecimal R50_COLUMN_F) {
		this.R50_COLUMN_F = R50_COLUMN_F;
	}

	public BigDecimal getR50_COLUMN_G() {
		return R50_COLUMN_G;
	}

	public void setR50_COLUMN_G(BigDecimal R50_COLUMN_G) {
		this.R50_COLUMN_G = R50_COLUMN_G;
	}

	public BigDecimal getR50_COLUMN_H() {
		return R50_COLUMN_H;
	}

	public void setR50_COLUMN_H(BigDecimal R50_COLUMN_H) {
		this.R50_COLUMN_H = R50_COLUMN_H;
	}

	public BigDecimal getR50_COLUMN_I() {
		return R50_COLUMN_I;
	}

	public void setR50_COLUMN_I(BigDecimal R50_COLUMN_I) {
		this.R50_COLUMN_I = R50_COLUMN_I;
	}

	public BigDecimal getR50_COLUMN_J() {
		return R50_COLUMN_J;
	}

	public void setR50_COLUMN_J(BigDecimal R50_COLUMN_J) {
		this.R50_COLUMN_J = R50_COLUMN_J;
	}

	public BigDecimal getR50_COLUMN_K() {
		return R50_COLUMN_K;
	}

	public void setR50_COLUMN_K(BigDecimal R50_COLUMN_K) {
		this.R50_COLUMN_K = R50_COLUMN_K;
	}

	public BigDecimal getR50_COLUMN_L() {
		return R50_COLUMN_L;
	}

	public void setR50_COLUMN_L(BigDecimal R50_COLUMN_L) {
		this.R50_COLUMN_L = R50_COLUMN_L;
	}

	public BigDecimal getR50_COLUMN_M() {
		return R50_COLUMN_M;
	}

	public void setR50_COLUMN_M(BigDecimal R50_COLUMN_M) {
		this.R50_COLUMN_M = R50_COLUMN_M;
	}

	public BigDecimal getR50_COLUMN_N() {
		return R50_COLUMN_N;
	}

	public void setR50_COLUMN_N(BigDecimal R50_COLUMN_N) {
		this.R50_COLUMN_N = R50_COLUMN_N;
	}

	public String getR51_COLUMN_A() {
		return R51_COLUMN_A;
	}

	public void setR51_COLUMN_A(String R51_COLUMN_A) {
		this.R51_COLUMN_A = R51_COLUMN_A;
	}

	public BigDecimal getR51_COLUMN_B() {
		return R51_COLUMN_B;
	}

	public void setR51_COLUMN_B(BigDecimal R51_COLUMN_B) {
		this.R51_COLUMN_B = R51_COLUMN_B;
	}

	public BigDecimal getR51_COLUMN_C() {
		return R51_COLUMN_C;
	}

	public void setR51_COLUMN_C(BigDecimal R51_COLUMN_C) {
		this.R51_COLUMN_C = R51_COLUMN_C;
	}

	public BigDecimal getR51_COLUMN_D() {
		return R51_COLUMN_D;
	}

	public void setR51_COLUMN_D(BigDecimal R51_COLUMN_D) {
		this.R51_COLUMN_D = R51_COLUMN_D;
	}

	public BigDecimal getR51_COLUMN_E() {
		return R51_COLUMN_E;
	}

	public void setR51_COLUMN_E(BigDecimal R51_COLUMN_E) {
		this.R51_COLUMN_E = R51_COLUMN_E;
	}

	public BigDecimal getR51_COLUMN_F() {
		return R51_COLUMN_F;
	}

	public void setR51_COLUMN_F(BigDecimal R51_COLUMN_F) {
		this.R51_COLUMN_F = R51_COLUMN_F;
	}

	public BigDecimal getR51_COLUMN_G() {
		return R51_COLUMN_G;
	}

	public void setR51_COLUMN_G(BigDecimal R51_COLUMN_G) {
		this.R51_COLUMN_G = R51_COLUMN_G;
	}

	public BigDecimal getR51_COLUMN_H() {
		return R51_COLUMN_H;
	}

	public void setR51_COLUMN_H(BigDecimal R51_COLUMN_H) {
		this.R51_COLUMN_H = R51_COLUMN_H;
	}

	public BigDecimal getR51_COLUMN_I() {
		return R51_COLUMN_I;
	}

	public void setR51_COLUMN_I(BigDecimal R51_COLUMN_I) {
		this.R51_COLUMN_I = R51_COLUMN_I;
	}

	public BigDecimal getR51_COLUMN_J() {
		return R51_COLUMN_J;
	}

	public void setR51_COLUMN_J(BigDecimal R51_COLUMN_J) {
		this.R51_COLUMN_J = R51_COLUMN_J;
	}

	public BigDecimal getR51_COLUMN_K() {
		return R51_COLUMN_K;
	}

	public void setR51_COLUMN_K(BigDecimal R51_COLUMN_K) {
		this.R51_COLUMN_K = R51_COLUMN_K;
	}

	public BigDecimal getR51_COLUMN_L() {
		return R51_COLUMN_L;
	}

	public void setR51_COLUMN_L(BigDecimal R51_COLUMN_L) {
		this.R51_COLUMN_L = R51_COLUMN_L;
	}

	public BigDecimal getR51_COLUMN_M() {
		return R51_COLUMN_M;
	}

	public void setR51_COLUMN_M(BigDecimal R51_COLUMN_M) {
		this.R51_COLUMN_M = R51_COLUMN_M;
	}

	public BigDecimal getR51_COLUMN_N() {
		return R51_COLUMN_N;
	}

	public void setR51_COLUMN_N(BigDecimal R51_COLUMN_N) {
		this.R51_COLUMN_N = R51_COLUMN_N;
	}

	public String getR52_COLUMN_A() {
		return R52_COLUMN_A;
	}

	public void setR52_COLUMN_A(String R52_COLUMN_A) {
		this.R52_COLUMN_A = R52_COLUMN_A;
	}

	public BigDecimal getR52_COLUMN_B() {
		return R52_COLUMN_B;
	}

	public void setR52_COLUMN_B(BigDecimal R52_COLUMN_B) {
		this.R52_COLUMN_B = R52_COLUMN_B;
	}

	public BigDecimal getR52_COLUMN_C() {
		return R52_COLUMN_C;
	}

	public void setR52_COLUMN_C(BigDecimal R52_COLUMN_C) {
		this.R52_COLUMN_C = R52_COLUMN_C;
	}

	public BigDecimal getR52_COLUMN_D() {
		return R52_COLUMN_D;
	}

	public void setR52_COLUMN_D(BigDecimal R52_COLUMN_D) {
		this.R52_COLUMN_D = R52_COLUMN_D;
	}

	public BigDecimal getR52_COLUMN_E() {
		return R52_COLUMN_E;
	}

	public void setR52_COLUMN_E(BigDecimal R52_COLUMN_E) {
		this.R52_COLUMN_E = R52_COLUMN_E;
	}

	public BigDecimal getR52_COLUMN_F() {
		return R52_COLUMN_F;
	}

	public void setR52_COLUMN_F(BigDecimal R52_COLUMN_F) {
		this.R52_COLUMN_F = R52_COLUMN_F;
	}

	public BigDecimal getR52_COLUMN_G() {
		return R52_COLUMN_G;
	}

	public void setR52_COLUMN_G(BigDecimal R52_COLUMN_G) {
		this.R52_COLUMN_G = R52_COLUMN_G;
	}

	public BigDecimal getR52_COLUMN_H() {
		return R52_COLUMN_H;
	}

	public void setR52_COLUMN_H(BigDecimal R52_COLUMN_H) {
		this.R52_COLUMN_H = R52_COLUMN_H;
	}

	public BigDecimal getR52_COLUMN_I() {
		return R52_COLUMN_I;
	}

	public void setR52_COLUMN_I(BigDecimal R52_COLUMN_I) {
		this.R52_COLUMN_I = R52_COLUMN_I;
	}

	public BigDecimal getR52_COLUMN_J() {
		return R52_COLUMN_J;
	}

	public void setR52_COLUMN_J(BigDecimal R52_COLUMN_J) {
		this.R52_COLUMN_J = R52_COLUMN_J;
	}

	public BigDecimal getR52_COLUMN_K() {
		return R52_COLUMN_K;
	}

	public void setR52_COLUMN_K(BigDecimal R52_COLUMN_K) {
		this.R52_COLUMN_K = R52_COLUMN_K;
	}

	public BigDecimal getR52_COLUMN_L() {
		return R52_COLUMN_L;
	}

	public void setR52_COLUMN_L(BigDecimal R52_COLUMN_L) {
		this.R52_COLUMN_L = R52_COLUMN_L;
	}

	public BigDecimal getR52_COLUMN_M() {
		return R52_COLUMN_M;
	}

	public void setR52_COLUMN_M(BigDecimal R52_COLUMN_M) {
		this.R52_COLUMN_M = R52_COLUMN_M;
	}

	public BigDecimal getR52_COLUMN_N() {
		return R52_COLUMN_N;
	}

	public void setR52_COLUMN_N(BigDecimal R52_COLUMN_N) {
		this.R52_COLUMN_N = R52_COLUMN_N;
	}

	public String getR53_COLUMN_A() {
		return R53_COLUMN_A;
	}

	public void setR53_COLUMN_A(String R53_COLUMN_A) {
		this.R53_COLUMN_A = R53_COLUMN_A;
	}

	public BigDecimal getR53_COLUMN_B() {
		return R53_COLUMN_B;
	}

	public void setR53_COLUMN_B(BigDecimal R53_COLUMN_B) {
		this.R53_COLUMN_B = R53_COLUMN_B;
	}

	public BigDecimal getR53_COLUMN_C() {
		return R53_COLUMN_C;
	}

	public void setR53_COLUMN_C(BigDecimal R53_COLUMN_C) {
		this.R53_COLUMN_C = R53_COLUMN_C;
	}

	public BigDecimal getR53_COLUMN_D() {
		return R53_COLUMN_D;
	}

	public void setR53_COLUMN_D(BigDecimal R53_COLUMN_D) {
		this.R53_COLUMN_D = R53_COLUMN_D;
	}

	public BigDecimal getR53_COLUMN_E() {
		return R53_COLUMN_E;
	}

	public void setR53_COLUMN_E(BigDecimal R53_COLUMN_E) {
		this.R53_COLUMN_E = R53_COLUMN_E;
	}

	public BigDecimal getR53_COLUMN_F() {
		return R53_COLUMN_F;
	}

	public void setR53_COLUMN_F(BigDecimal R53_COLUMN_F) {
		this.R53_COLUMN_F = R53_COLUMN_F;
	}

	public BigDecimal getR53_COLUMN_G() {
		return R53_COLUMN_G;
	}

	public void setR53_COLUMN_G(BigDecimal R53_COLUMN_G) {
		this.R53_COLUMN_G = R53_COLUMN_G;
	}

	public BigDecimal getR53_COLUMN_H() {
		return R53_COLUMN_H;
	}

	public void setR53_COLUMN_H(BigDecimal R53_COLUMN_H) {
		this.R53_COLUMN_H = R53_COLUMN_H;
	}

	public BigDecimal getR53_COLUMN_I() {
		return R53_COLUMN_I;
	}

	public void setR53_COLUMN_I(BigDecimal R53_COLUMN_I) {
		this.R53_COLUMN_I = R53_COLUMN_I;
	}

	public BigDecimal getR53_COLUMN_J() {
		return R53_COLUMN_J;
	}

	public void setR53_COLUMN_J(BigDecimal R53_COLUMN_J) {
		this.R53_COLUMN_J = R53_COLUMN_J;
	}

	public BigDecimal getR53_COLUMN_K() {
		return R53_COLUMN_K;
	}

	public void setR53_COLUMN_K(BigDecimal R53_COLUMN_K) {
		this.R53_COLUMN_K = R53_COLUMN_K;
	}

	public BigDecimal getR53_COLUMN_L() {
		return R53_COLUMN_L;
	}

	public void setR53_COLUMN_L(BigDecimal R53_COLUMN_L) {
		this.R53_COLUMN_L = R53_COLUMN_L;
	}

	public BigDecimal getR53_COLUMN_M() {
		return R53_COLUMN_M;
	}

	public void setR53_COLUMN_M(BigDecimal R53_COLUMN_M) {
		this.R53_COLUMN_M = R53_COLUMN_M;
	}

	public BigDecimal getR53_COLUMN_N() {
		return R53_COLUMN_N;
	}

	public void setR53_COLUMN_N(BigDecimal R53_COLUMN_N) {
		this.R53_COLUMN_N = R53_COLUMN_N;
	}

	public String getR54_COLUMN_A() {
		return R54_COLUMN_A;
	}

	public void setR54_COLUMN_A(String R54_COLUMN_A) {
		this.R54_COLUMN_A = R54_COLUMN_A;
	}

	public BigDecimal getR54_COLUMN_B() {
		return R54_COLUMN_B;
	}

	public void setR54_COLUMN_B(BigDecimal R54_COLUMN_B) {
		this.R54_COLUMN_B = R54_COLUMN_B;
	}

	public BigDecimal getR54_COLUMN_C() {
		return R54_COLUMN_C;
	}

	public void setR54_COLUMN_C(BigDecimal R54_COLUMN_C) {
		this.R54_COLUMN_C = R54_COLUMN_C;
	}

	public BigDecimal getR54_COLUMN_D() {
		return R54_COLUMN_D;
	}

	public void setR54_COLUMN_D(BigDecimal R54_COLUMN_D) {
		this.R54_COLUMN_D = R54_COLUMN_D;
	}

	public BigDecimal getR54_COLUMN_E() {
		return R54_COLUMN_E;
	}

	public void setR54_COLUMN_E(BigDecimal R54_COLUMN_E) {
		this.R54_COLUMN_E = R54_COLUMN_E;
	}

	public BigDecimal getR54_COLUMN_F() {
		return R54_COLUMN_F;
	}

	public void setR54_COLUMN_F(BigDecimal R54_COLUMN_F) {
		this.R54_COLUMN_F = R54_COLUMN_F;
	}

	public BigDecimal getR54_COLUMN_G() {
		return R54_COLUMN_G;
	}

	public void setR54_COLUMN_G(BigDecimal R54_COLUMN_G) {
		this.R54_COLUMN_G = R54_COLUMN_G;
	}

	public BigDecimal getR54_COLUMN_H() {
		return R54_COLUMN_H;
	}

	public void setR54_COLUMN_H(BigDecimal R54_COLUMN_H) {
		this.R54_COLUMN_H = R54_COLUMN_H;
	}

	public BigDecimal getR54_COLUMN_I() {
		return R54_COLUMN_I;
	}

	public void setR54_COLUMN_I(BigDecimal R54_COLUMN_I) {
		this.R54_COLUMN_I = R54_COLUMN_I;
	}

	public BigDecimal getR54_COLUMN_J() {
		return R54_COLUMN_J;
	}

	public void setR54_COLUMN_J(BigDecimal R54_COLUMN_J) {
		this.R54_COLUMN_J = R54_COLUMN_J;
	}

	public BigDecimal getR54_COLUMN_K() {
		return R54_COLUMN_K;
	}

	public void setR54_COLUMN_K(BigDecimal R54_COLUMN_K) {
		this.R54_COLUMN_K = R54_COLUMN_K;
	}

	public BigDecimal getR54_COLUMN_L() {
		return R54_COLUMN_L;
	}

	public void setR54_COLUMN_L(BigDecimal R54_COLUMN_L) {
		this.R54_COLUMN_L = R54_COLUMN_L;
	}

	public BigDecimal getR54_COLUMN_M() {
		return R54_COLUMN_M;
	}

	public void setR54_COLUMN_M(BigDecimal R54_COLUMN_M) {
		this.R54_COLUMN_M = R54_COLUMN_M;
	}

	public BigDecimal getR54_COLUMN_N() {
		return R54_COLUMN_N;
	}

	public void setR54_COLUMN_N(BigDecimal R54_COLUMN_N) {
		this.R54_COLUMN_N = R54_COLUMN_N;
	}

	public String getR55_COLUMN_A() {
		return R55_COLUMN_A;
	}

	public void setR55_COLUMN_A(String R55_COLUMN_A) {
		this.R55_COLUMN_A = R55_COLUMN_A;
	}

	public BigDecimal getR55_COLUMN_B() {
		return R55_COLUMN_B;
	}

	public void setR55_COLUMN_B(BigDecimal R55_COLUMN_B) {
		this.R55_COLUMN_B = R55_COLUMN_B;
	}

	public BigDecimal getR55_COLUMN_C() {
		return R55_COLUMN_C;
	}

	public void setR55_COLUMN_C(BigDecimal R55_COLUMN_C) {
		this.R55_COLUMN_C = R55_COLUMN_C;
	}

	public BigDecimal getR55_COLUMN_D() {
		return R55_COLUMN_D;
	}

	public void setR55_COLUMN_D(BigDecimal R55_COLUMN_D) {
		this.R55_COLUMN_D = R55_COLUMN_D;
	}

	public BigDecimal getR55_COLUMN_E() {
		return R55_COLUMN_E;
	}

	public void setR55_COLUMN_E(BigDecimal R55_COLUMN_E) {
		this.R55_COLUMN_E = R55_COLUMN_E;
	}

	public BigDecimal getR55_COLUMN_F() {
		return R55_COLUMN_F;
	}

	public void setR55_COLUMN_F(BigDecimal R55_COLUMN_F) {
		this.R55_COLUMN_F = R55_COLUMN_F;
	}

	public BigDecimal getR55_COLUMN_G() {
		return R55_COLUMN_G;
	}

	public void setR55_COLUMN_G(BigDecimal R55_COLUMN_G) {
		this.R55_COLUMN_G = R55_COLUMN_G;
	}

	public BigDecimal getR55_COLUMN_H() {
		return R55_COLUMN_H;
	}

	public void setR55_COLUMN_H(BigDecimal R55_COLUMN_H) {
		this.R55_COLUMN_H = R55_COLUMN_H;
	}

	public BigDecimal getR55_COLUMN_I() {
		return R55_COLUMN_I;
	}

	public void setR55_COLUMN_I(BigDecimal R55_COLUMN_I) {
		this.R55_COLUMN_I = R55_COLUMN_I;
	}

	public BigDecimal getR55_COLUMN_J() {
		return R55_COLUMN_J;
	}

	public void setR55_COLUMN_J(BigDecimal R55_COLUMN_J) {
		this.R55_COLUMN_J = R55_COLUMN_J;
	}

	public BigDecimal getR55_COLUMN_K() {
		return R55_COLUMN_K;
	}

	public void setR55_COLUMN_K(BigDecimal R55_COLUMN_K) {
		this.R55_COLUMN_K = R55_COLUMN_K;
	}

	public BigDecimal getR55_COLUMN_L() {
		return R55_COLUMN_L;
	}

	public void setR55_COLUMN_L(BigDecimal R55_COLUMN_L) {
		this.R55_COLUMN_L = R55_COLUMN_L;
	}

	public BigDecimal getR55_COLUMN_M() {
		return R55_COLUMN_M;
	}

	public void setR55_COLUMN_M(BigDecimal R55_COLUMN_M) {
		this.R55_COLUMN_M = R55_COLUMN_M;
	}

	public BigDecimal getR55_COLUMN_N() {
		return R55_COLUMN_N;
	}

	public void setR55_COLUMN_N(BigDecimal R55_COLUMN_N) {
		this.R55_COLUMN_N = R55_COLUMN_N;
	}

	public String getR56_COLUMN_A() {
		return R56_COLUMN_A;
	}

	public void setR56_COLUMN_A(String R56_COLUMN_A) {
		this.R56_COLUMN_A = R56_COLUMN_A;
	}

	public BigDecimal getR56_COLUMN_B() {
		return R56_COLUMN_B;
	}

	public void setR56_COLUMN_B(BigDecimal R56_COLUMN_B) {
		this.R56_COLUMN_B = R56_COLUMN_B;
	}

	public BigDecimal getR56_COLUMN_C() {
		return R56_COLUMN_C;
	}

	public void setR56_COLUMN_C(BigDecimal R56_COLUMN_C) {
		this.R56_COLUMN_C = R56_COLUMN_C;
	}

	public BigDecimal getR56_COLUMN_D() {
		return R56_COLUMN_D;
	}

	public void setR56_COLUMN_D(BigDecimal R56_COLUMN_D) {
		this.R56_COLUMN_D = R56_COLUMN_D;
	}

	public BigDecimal getR56_COLUMN_E() {
		return R56_COLUMN_E;
	}

	public void setR56_COLUMN_E(BigDecimal R56_COLUMN_E) {
		this.R56_COLUMN_E = R56_COLUMN_E;
	}

	public BigDecimal getR56_COLUMN_F() {
		return R56_COLUMN_F;
	}

	public void setR56_COLUMN_F(BigDecimal R56_COLUMN_F) {
		this.R56_COLUMN_F = R56_COLUMN_F;
	}

	public BigDecimal getR56_COLUMN_G() {
		return R56_COLUMN_G;
	}

	public void setR56_COLUMN_G(BigDecimal R56_COLUMN_G) {
		this.R56_COLUMN_G = R56_COLUMN_G;
	}

	public BigDecimal getR56_COLUMN_H() {
		return R56_COLUMN_H;
	}

	public void setR56_COLUMN_H(BigDecimal R56_COLUMN_H) {
		this.R56_COLUMN_H = R56_COLUMN_H;
	}

	public BigDecimal getR56_COLUMN_I() {
		return R56_COLUMN_I;
	}

	public void setR56_COLUMN_I(BigDecimal R56_COLUMN_I) {
		this.R56_COLUMN_I = R56_COLUMN_I;
	}

	public BigDecimal getR56_COLUMN_J() {
		return R56_COLUMN_J;
	}

	public void setR56_COLUMN_J(BigDecimal R56_COLUMN_J) {
		this.R56_COLUMN_J = R56_COLUMN_J;
	}

	public BigDecimal getR56_COLUMN_K() {
		return R56_COLUMN_K;
	}

	public void setR56_COLUMN_K(BigDecimal R56_COLUMN_K) {
		this.R56_COLUMN_K = R56_COLUMN_K;
	}

	public BigDecimal getR56_COLUMN_L() {
		return R56_COLUMN_L;
	}

	public void setR56_COLUMN_L(BigDecimal R56_COLUMN_L) {
		this.R56_COLUMN_L = R56_COLUMN_L;
	}

	public BigDecimal getR56_COLUMN_M() {
		return R56_COLUMN_M;
	}

	public void setR56_COLUMN_M(BigDecimal R56_COLUMN_M) {
		this.R56_COLUMN_M = R56_COLUMN_M;
	}

	public BigDecimal getR56_COLUMN_N() {
		return R56_COLUMN_N;
	}

	public void setR56_COLUMN_N(BigDecimal R56_COLUMN_N) {
		this.R56_COLUMN_N = R56_COLUMN_N;
	}

	public String getR57_COLUMN_A() {
		return R57_COLUMN_A;
	}

	public void setR57_COLUMN_A(String R57_COLUMN_A) {
		this.R57_COLUMN_A = R57_COLUMN_A;
	}

	public BigDecimal getR57_COLUMN_B() {
		return R57_COLUMN_B;
	}

	public void setR57_COLUMN_B(BigDecimal R57_COLUMN_B) {
		this.R57_COLUMN_B = R57_COLUMN_B;
	}

	public BigDecimal getR57_COLUMN_C() {
		return R57_COLUMN_C;
	}

	public void setR57_COLUMN_C(BigDecimal R57_COLUMN_C) {
		this.R57_COLUMN_C = R57_COLUMN_C;
	}

	public BigDecimal getR57_COLUMN_D() {
		return R57_COLUMN_D;
	}

	public void setR57_COLUMN_D(BigDecimal R57_COLUMN_D) {
		this.R57_COLUMN_D = R57_COLUMN_D;
	}

	public BigDecimal getR57_COLUMN_E() {
		return R57_COLUMN_E;
	}

	public void setR57_COLUMN_E(BigDecimal R57_COLUMN_E) {
		this.R57_COLUMN_E = R57_COLUMN_E;
	}

	public BigDecimal getR57_COLUMN_F() {
		return R57_COLUMN_F;
	}

	public void setR57_COLUMN_F(BigDecimal R57_COLUMN_F) {
		this.R57_COLUMN_F = R57_COLUMN_F;
	}

	public BigDecimal getR57_COLUMN_G() {
		return R57_COLUMN_G;
	}

	public void setR57_COLUMN_G(BigDecimal R57_COLUMN_G) {
		this.R57_COLUMN_G = R57_COLUMN_G;
	}

	public BigDecimal getR57_COLUMN_H() {
		return R57_COLUMN_H;
	}

	public void setR57_COLUMN_H(BigDecimal R57_COLUMN_H) {
		this.R57_COLUMN_H = R57_COLUMN_H;
	}

	public BigDecimal getR57_COLUMN_I() {
		return R57_COLUMN_I;
	}

	public void setR57_COLUMN_I(BigDecimal R57_COLUMN_I) {
		this.R57_COLUMN_I = R57_COLUMN_I;
	}

	public BigDecimal getR57_COLUMN_J() {
		return R57_COLUMN_J;
	}

	public void setR57_COLUMN_J(BigDecimal R57_COLUMN_J) {
		this.R57_COLUMN_J = R57_COLUMN_J;
	}

	public BigDecimal getR57_COLUMN_K() {
		return R57_COLUMN_K;
	}

	public void setR57_COLUMN_K(BigDecimal R57_COLUMN_K) {
		this.R57_COLUMN_K = R57_COLUMN_K;
	}

	public BigDecimal getR57_COLUMN_L() {
		return R57_COLUMN_L;
	}

	public void setR57_COLUMN_L(BigDecimal R57_COLUMN_L) {
		this.R57_COLUMN_L = R57_COLUMN_L;
	}

	public BigDecimal getR57_COLUMN_M() {
		return R57_COLUMN_M;
	}

	public void setR57_COLUMN_M(BigDecimal R57_COLUMN_M) {
		this.R57_COLUMN_M = R57_COLUMN_M;
	}

	public BigDecimal getR57_COLUMN_N() {
		return R57_COLUMN_N;
	}

	public void setR57_COLUMN_N(BigDecimal R57_COLUMN_N) {
		this.R57_COLUMN_N = R57_COLUMN_N;
	}

	public String getR58_COLUMN_A() {
		return R58_COLUMN_A;
	}

	public void setR58_COLUMN_A(String R58_COLUMN_A) {
		this.R58_COLUMN_A = R58_COLUMN_A;
	}

	public BigDecimal getR58_COLUMN_B() {
		return R58_COLUMN_B;
	}

	public void setR58_COLUMN_B(BigDecimal R58_COLUMN_B) {
		this.R58_COLUMN_B = R58_COLUMN_B;
	}

	public BigDecimal getR58_COLUMN_C() {
		return R58_COLUMN_C;
	}

	public void setR58_COLUMN_C(BigDecimal R58_COLUMN_C) {
		this.R58_COLUMN_C = R58_COLUMN_C;
	}

	public BigDecimal getR58_COLUMN_D() {
		return R58_COLUMN_D;
	}

	public void setR58_COLUMN_D(BigDecimal R58_COLUMN_D) {
		this.R58_COLUMN_D = R58_COLUMN_D;
	}

	public BigDecimal getR58_COLUMN_E() {
		return R58_COLUMN_E;
	}

	public void setR58_COLUMN_E(BigDecimal R58_COLUMN_E) {
		this.R58_COLUMN_E = R58_COLUMN_E;
	}

	public BigDecimal getR58_COLUMN_F() {
		return R58_COLUMN_F;
	}

	public void setR58_COLUMN_F(BigDecimal R58_COLUMN_F) {
		this.R58_COLUMN_F = R58_COLUMN_F;
	}

	public BigDecimal getR58_COLUMN_G() {
		return R58_COLUMN_G;
	}

	public void setR58_COLUMN_G(BigDecimal R58_COLUMN_G) {
		this.R58_COLUMN_G = R58_COLUMN_G;
	}

	public BigDecimal getR58_COLUMN_H() {
		return R58_COLUMN_H;
	}

	public void setR58_COLUMN_H(BigDecimal R58_COLUMN_H) {
		this.R58_COLUMN_H = R58_COLUMN_H;
	}

	public BigDecimal getR58_COLUMN_I() {
		return R58_COLUMN_I;
	}

	public void setR58_COLUMN_I(BigDecimal R58_COLUMN_I) {
		this.R58_COLUMN_I = R58_COLUMN_I;
	}

	public BigDecimal getR58_COLUMN_J() {
		return R58_COLUMN_J;
	}

	public void setR58_COLUMN_J(BigDecimal R58_COLUMN_J) {
		this.R58_COLUMN_J = R58_COLUMN_J;
	}

	public BigDecimal getR58_COLUMN_K() {
		return R58_COLUMN_K;
	}

	public void setR58_COLUMN_K(BigDecimal R58_COLUMN_K) {
		this.R58_COLUMN_K = R58_COLUMN_K;
	}

	public BigDecimal getR58_COLUMN_L() {
		return R58_COLUMN_L;
	}

	public void setR58_COLUMN_L(BigDecimal R58_COLUMN_L) {
		this.R58_COLUMN_L = R58_COLUMN_L;
	}

	public BigDecimal getR58_COLUMN_M() {
		return R58_COLUMN_M;
	}

	public void setR58_COLUMN_M(BigDecimal R58_COLUMN_M) {
		this.R58_COLUMN_M = R58_COLUMN_M;
	}

	public BigDecimal getR58_COLUMN_N() {
		return R58_COLUMN_N;
	}

	public void setR58_COLUMN_N(BigDecimal R58_COLUMN_N) {
		this.R58_COLUMN_N = R58_COLUMN_N;
	}

	public String getR59_COLUMN_A() {
		return R59_COLUMN_A;
	}

	public void setR59_COLUMN_A(String R59_COLUMN_A) {
		this.R59_COLUMN_A = R59_COLUMN_A;
	}

	public BigDecimal getR59_COLUMN_B() {
		return R59_COLUMN_B;
	}

	public void setR59_COLUMN_B(BigDecimal R59_COLUMN_B) {
		this.R59_COLUMN_B = R59_COLUMN_B;
	}

	public BigDecimal getR59_COLUMN_C() {
		return R59_COLUMN_C;
	}

	public void setR59_COLUMN_C(BigDecimal R59_COLUMN_C) {
		this.R59_COLUMN_C = R59_COLUMN_C;
	}

	public BigDecimal getR59_COLUMN_D() {
		return R59_COLUMN_D;
	}

	public void setR59_COLUMN_D(BigDecimal R59_COLUMN_D) {
		this.R59_COLUMN_D = R59_COLUMN_D;
	}

	public BigDecimal getR59_COLUMN_E() {
		return R59_COLUMN_E;
	}

	public void setR59_COLUMN_E(BigDecimal R59_COLUMN_E) {
		this.R59_COLUMN_E = R59_COLUMN_E;
	}

	public BigDecimal getR59_COLUMN_F() {
		return R59_COLUMN_F;
	}

	public void setR59_COLUMN_F(BigDecimal R59_COLUMN_F) {
		this.R59_COLUMN_F = R59_COLUMN_F;
	}

	public BigDecimal getR59_COLUMN_G() {
		return R59_COLUMN_G;
	}

	public void setR59_COLUMN_G(BigDecimal R59_COLUMN_G) {
		this.R59_COLUMN_G = R59_COLUMN_G;
	}

	public BigDecimal getR59_COLUMN_H() {
		return R59_COLUMN_H;
	}

	public void setR59_COLUMN_H(BigDecimal R59_COLUMN_H) {
		this.R59_COLUMN_H = R59_COLUMN_H;
	}

	public BigDecimal getR59_COLUMN_I() {
		return R59_COLUMN_I;
	}

	public void setR59_COLUMN_I(BigDecimal R59_COLUMN_I) {
		this.R59_COLUMN_I = R59_COLUMN_I;
	}

	public BigDecimal getR59_COLUMN_J() {
		return R59_COLUMN_J;
	}

	public void setR59_COLUMN_J(BigDecimal R59_COLUMN_J) {
		this.R59_COLUMN_J = R59_COLUMN_J;
	}

	public BigDecimal getR59_COLUMN_K() {
		return R59_COLUMN_K;
	}

	public void setR59_COLUMN_K(BigDecimal R59_COLUMN_K) {
		this.R59_COLUMN_K = R59_COLUMN_K;
	}

	public BigDecimal getR59_COLUMN_L() {
		return R59_COLUMN_L;
	}

	public void setR59_COLUMN_L(BigDecimal R59_COLUMN_L) {
		this.R59_COLUMN_L = R59_COLUMN_L;
	}

	public BigDecimal getR59_COLUMN_M() {
		return R59_COLUMN_M;
	}

	public void setR59_COLUMN_M(BigDecimal R59_COLUMN_M) {
		this.R59_COLUMN_M = R59_COLUMN_M;
	}

	public BigDecimal getR59_COLUMN_N() {
		return R59_COLUMN_N;
	}

	public void setR59_COLUMN_N(BigDecimal R59_COLUMN_N) {
		this.R59_COLUMN_N = R59_COLUMN_N;
	}

	public String getR60_COLUMN_A() {
		return R60_COLUMN_A;
	}

	public void setR60_COLUMN_A(String R60_COLUMN_A) {
		this.R60_COLUMN_A = R60_COLUMN_A;
	}

	public BigDecimal getR60_COLUMN_B() {
		return R60_COLUMN_B;
	}

	public void setR60_COLUMN_B(BigDecimal R60_COLUMN_B) {
		this.R60_COLUMN_B = R60_COLUMN_B;
	}

	public BigDecimal getR60_COLUMN_C() {
		return R60_COLUMN_C;
	}

	public void setR60_COLUMN_C(BigDecimal R60_COLUMN_C) {
		this.R60_COLUMN_C = R60_COLUMN_C;
	}

	public BigDecimal getR60_COLUMN_D() {
		return R60_COLUMN_D;
	}

	public void setR60_COLUMN_D(BigDecimal R60_COLUMN_D) {
		this.R60_COLUMN_D = R60_COLUMN_D;
	}

	public BigDecimal getR60_COLUMN_E() {
		return R60_COLUMN_E;
	}

	public void setR60_COLUMN_E(BigDecimal R60_COLUMN_E) {
		this.R60_COLUMN_E = R60_COLUMN_E;
	}

	public BigDecimal getR60_COLUMN_F() {
		return R60_COLUMN_F;
	}

	public void setR60_COLUMN_F(BigDecimal R60_COLUMN_F) {
		this.R60_COLUMN_F = R60_COLUMN_F;
	}

	public BigDecimal getR60_COLUMN_G() {
		return R60_COLUMN_G;
	}

	public void setR60_COLUMN_G(BigDecimal R60_COLUMN_G) {
		this.R60_COLUMN_G = R60_COLUMN_G;
	}

	public BigDecimal getR60_COLUMN_H() {
		return R60_COLUMN_H;
	}

	public void setR60_COLUMN_H(BigDecimal R60_COLUMN_H) {
		this.R60_COLUMN_H = R60_COLUMN_H;
	}

	public BigDecimal getR60_COLUMN_I() {
		return R60_COLUMN_I;
	}

	public void setR60_COLUMN_I(BigDecimal R60_COLUMN_I) {
		this.R60_COLUMN_I = R60_COLUMN_I;
	}

	public BigDecimal getR60_COLUMN_J() {
		return R60_COLUMN_J;
	}

	public void setR60_COLUMN_J(BigDecimal R60_COLUMN_J) {
		this.R60_COLUMN_J = R60_COLUMN_J;
	}

	public BigDecimal getR60_COLUMN_K() {
		return R60_COLUMN_K;
	}

	public void setR60_COLUMN_K(BigDecimal R60_COLUMN_K) {
		this.R60_COLUMN_K = R60_COLUMN_K;
	}

	public BigDecimal getR60_COLUMN_L() {
		return R60_COLUMN_L;
	}

	public void setR60_COLUMN_L(BigDecimal R60_COLUMN_L) {
		this.R60_COLUMN_L = R60_COLUMN_L;
	}

	public BigDecimal getR60_COLUMN_M() {
		return R60_COLUMN_M;
	}

	public void setR60_COLUMN_M(BigDecimal R60_COLUMN_M) {
		this.R60_COLUMN_M = R60_COLUMN_M;
	}

	public BigDecimal getR60_COLUMN_N() {
		return R60_COLUMN_N;
	}

	public void setR60_COLUMN_N(BigDecimal R60_COLUMN_N) {
		this.R60_COLUMN_N = R60_COLUMN_N;
	}

	public String getR61_COLUMN_A() {
		return R61_COLUMN_A;
	}

	public void setR61_COLUMN_A(String R61_COLUMN_A) {
		this.R61_COLUMN_A = R61_COLUMN_A;
	}

	public BigDecimal getR61_COLUMN_B() {
		return R61_COLUMN_B;
	}

	public void setR61_COLUMN_B(BigDecimal R61_COLUMN_B) {
		this.R61_COLUMN_B = R61_COLUMN_B;
	}

	public BigDecimal getR61_COLUMN_C() {
		return R61_COLUMN_C;
	}

	public void setR61_COLUMN_C(BigDecimal R61_COLUMN_C) {
		this.R61_COLUMN_C = R61_COLUMN_C;
	}

	public BigDecimal getR61_COLUMN_D() {
		return R61_COLUMN_D;
	}

	public void setR61_COLUMN_D(BigDecimal R61_COLUMN_D) {
		this.R61_COLUMN_D = R61_COLUMN_D;
	}

	public BigDecimal getR61_COLUMN_E() {
		return R61_COLUMN_E;
	}

	public void setR61_COLUMN_E(BigDecimal R61_COLUMN_E) {
		this.R61_COLUMN_E = R61_COLUMN_E;
	}

	public BigDecimal getR61_COLUMN_F() {
		return R61_COLUMN_F;
	}

	public void setR61_COLUMN_F(BigDecimal R61_COLUMN_F) {
		this.R61_COLUMN_F = R61_COLUMN_F;
	}

	public BigDecimal getR61_COLUMN_G() {
		return R61_COLUMN_G;
	}

	public void setR61_COLUMN_G(BigDecimal R61_COLUMN_G) {
		this.R61_COLUMN_G = R61_COLUMN_G;
	}

	public BigDecimal getR61_COLUMN_H() {
		return R61_COLUMN_H;
	}

	public void setR61_COLUMN_H(BigDecimal R61_COLUMN_H) {
		this.R61_COLUMN_H = R61_COLUMN_H;
	}

	public BigDecimal getR61_COLUMN_I() {
		return R61_COLUMN_I;
	}

	public void setR61_COLUMN_I(BigDecimal R61_COLUMN_I) {
		this.R61_COLUMN_I = R61_COLUMN_I;
	}

	public BigDecimal getR61_COLUMN_J() {
		return R61_COLUMN_J;
	}

	public void setR61_COLUMN_J(BigDecimal R61_COLUMN_J) {
		this.R61_COLUMN_J = R61_COLUMN_J;
	}

	public BigDecimal getR61_COLUMN_K() {
		return R61_COLUMN_K;
	}

	public void setR61_COLUMN_K(BigDecimal R61_COLUMN_K) {
		this.R61_COLUMN_K = R61_COLUMN_K;
	}

	public BigDecimal getR61_COLUMN_L() {
		return R61_COLUMN_L;
	}

	public void setR61_COLUMN_L(BigDecimal R61_COLUMN_L) {
		this.R61_COLUMN_L = R61_COLUMN_L;
	}

	public BigDecimal getR61_COLUMN_M() {
		return R61_COLUMN_M;
	}

	public void setR61_COLUMN_M(BigDecimal R61_COLUMN_M) {
		this.R61_COLUMN_M = R61_COLUMN_M;
	}

	public BigDecimal getR61_COLUMN_N() {
		return R61_COLUMN_N;
	}

	public void setR61_COLUMN_N(BigDecimal R61_COLUMN_N) {
		this.R61_COLUMN_N = R61_COLUMN_N;
	}

	public String getR62_COLUMN_A() {
		return R62_COLUMN_A;
	}

	public void setR62_COLUMN_A(String R62_COLUMN_A) {
		this.R62_COLUMN_A = R62_COLUMN_A;
	}

	public BigDecimal getR62_COLUMN_B() {
		return R62_COLUMN_B;
	}

	public void setR62_COLUMN_B(BigDecimal R62_COLUMN_B) {
		this.R62_COLUMN_B = R62_COLUMN_B;
	}

	public BigDecimal getR62_COLUMN_C() {
		return R62_COLUMN_C;
	}

	public void setR62_COLUMN_C(BigDecimal R62_COLUMN_C) {
		this.R62_COLUMN_C = R62_COLUMN_C;
	}

	public BigDecimal getR62_COLUMN_D() {
		return R62_COLUMN_D;
	}

	public void setR62_COLUMN_D(BigDecimal R62_COLUMN_D) {
		this.R62_COLUMN_D = R62_COLUMN_D;
	}

	public BigDecimal getR62_COLUMN_E() {
		return R62_COLUMN_E;
	}

	public void setR62_COLUMN_E(BigDecimal R62_COLUMN_E) {
		this.R62_COLUMN_E = R62_COLUMN_E;
	}

	public BigDecimal getR62_COLUMN_F() {
		return R62_COLUMN_F;
	}

	public void setR62_COLUMN_F(BigDecimal R62_COLUMN_F) {
		this.R62_COLUMN_F = R62_COLUMN_F;
	}

	public BigDecimal getR62_COLUMN_G() {
		return R62_COLUMN_G;
	}

	public void setR62_COLUMN_G(BigDecimal R62_COLUMN_G) {
		this.R62_COLUMN_G = R62_COLUMN_G;
	}

	public BigDecimal getR62_COLUMN_H() {
		return R62_COLUMN_H;
	}

	public void setR62_COLUMN_H(BigDecimal R62_COLUMN_H) {
		this.R62_COLUMN_H = R62_COLUMN_H;
	}

	public BigDecimal getR62_COLUMN_I() {
		return R62_COLUMN_I;
	}

	public void setR62_COLUMN_I(BigDecimal R62_COLUMN_I) {
		this.R62_COLUMN_I = R62_COLUMN_I;
	}

	public BigDecimal getR62_COLUMN_J() {
		return R62_COLUMN_J;
	}

	public void setR62_COLUMN_J(BigDecimal R62_COLUMN_J) {
		this.R62_COLUMN_J = R62_COLUMN_J;
	}

	public BigDecimal getR62_COLUMN_K() {
		return R62_COLUMN_K;
	}

	public void setR62_COLUMN_K(BigDecimal R62_COLUMN_K) {
		this.R62_COLUMN_K = R62_COLUMN_K;
	}

	public BigDecimal getR62_COLUMN_L() {
		return R62_COLUMN_L;
	}

	public void setR62_COLUMN_L(BigDecimal R62_COLUMN_L) {
		this.R62_COLUMN_L = R62_COLUMN_L;
	}

	public BigDecimal getR62_COLUMN_M() {
		return R62_COLUMN_M;
	}

	public void setR62_COLUMN_M(BigDecimal R62_COLUMN_M) {
		this.R62_COLUMN_M = R62_COLUMN_M;
	}

	public BigDecimal getR62_COLUMN_N() {
		return R62_COLUMN_N;
	}

	public void setR62_COLUMN_N(BigDecimal R62_COLUMN_N) {
		this.R62_COLUMN_N = R62_COLUMN_N;
	}

	public String getR63_COLUMN_A() {
		return R63_COLUMN_A;
	}

	public void setR63_COLUMN_A(String R63_COLUMN_A) {
		this.R63_COLUMN_A = R63_COLUMN_A;
	}

	public BigDecimal getR63_COLUMN_B() {
		return R63_COLUMN_B;
	}

	public void setR63_COLUMN_B(BigDecimal R63_COLUMN_B) {
		this.R63_COLUMN_B = R63_COLUMN_B;
	}

	public BigDecimal getR63_COLUMN_C() {
		return R63_COLUMN_C;
	}

	public void setR63_COLUMN_C(BigDecimal R63_COLUMN_C) {
		this.R63_COLUMN_C = R63_COLUMN_C;
	}

	public BigDecimal getR63_COLUMN_D() {
		return R63_COLUMN_D;
	}

	public void setR63_COLUMN_D(BigDecimal R63_COLUMN_D) {
		this.R63_COLUMN_D = R63_COLUMN_D;
	}

	public BigDecimal getR63_COLUMN_E() {
		return R63_COLUMN_E;
	}

	public void setR63_COLUMN_E(BigDecimal R63_COLUMN_E) {
		this.R63_COLUMN_E = R63_COLUMN_E;
	}

	public BigDecimal getR63_COLUMN_F() {
		return R63_COLUMN_F;
	}

	public void setR63_COLUMN_F(BigDecimal R63_COLUMN_F) {
		this.R63_COLUMN_F = R63_COLUMN_F;
	}

	public BigDecimal getR63_COLUMN_G() {
		return R63_COLUMN_G;
	}

	public void setR63_COLUMN_G(BigDecimal R63_COLUMN_G) {
		this.R63_COLUMN_G = R63_COLUMN_G;
	}

	public BigDecimal getR63_COLUMN_H() {
		return R63_COLUMN_H;
	}

	public void setR63_COLUMN_H(BigDecimal R63_COLUMN_H) {
		this.R63_COLUMN_H = R63_COLUMN_H;
	}

	public BigDecimal getR63_COLUMN_I() {
		return R63_COLUMN_I;
	}

	public void setR63_COLUMN_I(BigDecimal R63_COLUMN_I) {
		this.R63_COLUMN_I = R63_COLUMN_I;
	}

	public BigDecimal getR63_COLUMN_J() {
		return R63_COLUMN_J;
	}

	public void setR63_COLUMN_J(BigDecimal R63_COLUMN_J) {
		this.R63_COLUMN_J = R63_COLUMN_J;
	}

	public BigDecimal getR63_COLUMN_K() {
		return R63_COLUMN_K;
	}

	public void setR63_COLUMN_K(BigDecimal R63_COLUMN_K) {
		this.R63_COLUMN_K = R63_COLUMN_K;
	}

	public BigDecimal getR63_COLUMN_L() {
		return R63_COLUMN_L;
	}

	public void setR63_COLUMN_L(BigDecimal R63_COLUMN_L) {
		this.R63_COLUMN_L = R63_COLUMN_L;
	}

	public BigDecimal getR63_COLUMN_M() {
		return R63_COLUMN_M;
	}

	public void setR63_COLUMN_M(BigDecimal R63_COLUMN_M) {
		this.R63_COLUMN_M = R63_COLUMN_M;
	}

	public BigDecimal getR63_COLUMN_N() {
		return R63_COLUMN_N;
	}

	public void setR63_COLUMN_N(BigDecimal R63_COLUMN_N) {
		this.R63_COLUMN_N = R63_COLUMN_N;
	}

	public String getR64_COLUMN_A() {
		return R64_COLUMN_A;
	}

	public void setR64_COLUMN_A(String R64_COLUMN_A) {
		this.R64_COLUMN_A = R64_COLUMN_A;
	}

	public BigDecimal getR64_COLUMN_B() {
		return R64_COLUMN_B;
	}

	public void setR64_COLUMN_B(BigDecimal R64_COLUMN_B) {
		this.R64_COLUMN_B = R64_COLUMN_B;
	}

	public BigDecimal getR64_COLUMN_C() {
		return R64_COLUMN_C;
	}

	public void setR64_COLUMN_C(BigDecimal R64_COLUMN_C) {
		this.R64_COLUMN_C = R64_COLUMN_C;
	}

	public BigDecimal getR64_COLUMN_D() {
		return R64_COLUMN_D;
	}

	public void setR64_COLUMN_D(BigDecimal R64_COLUMN_D) {
		this.R64_COLUMN_D = R64_COLUMN_D;
	}

	public BigDecimal getR64_COLUMN_E() {
		return R64_COLUMN_E;
	}

	public void setR64_COLUMN_E(BigDecimal R64_COLUMN_E) {
		this.R64_COLUMN_E = R64_COLUMN_E;
	}

	public BigDecimal getR64_COLUMN_F() {
		return R64_COLUMN_F;
	}

	public void setR64_COLUMN_F(BigDecimal R64_COLUMN_F) {
		this.R64_COLUMN_F = R64_COLUMN_F;
	}

	public BigDecimal getR64_COLUMN_G() {
		return R64_COLUMN_G;
	}

	public void setR64_COLUMN_G(BigDecimal R64_COLUMN_G) {
		this.R64_COLUMN_G = R64_COLUMN_G;
	}

	public BigDecimal getR64_COLUMN_H() {
		return R64_COLUMN_H;
	}

	public void setR64_COLUMN_H(BigDecimal R64_COLUMN_H) {
		this.R64_COLUMN_H = R64_COLUMN_H;
	}

	public BigDecimal getR64_COLUMN_I() {
		return R64_COLUMN_I;
	}

	public void setR64_COLUMN_I(BigDecimal R64_COLUMN_I) {
		this.R64_COLUMN_I = R64_COLUMN_I;
	}

	public BigDecimal getR64_COLUMN_J() {
		return R64_COLUMN_J;
	}

	public void setR64_COLUMN_J(BigDecimal R64_COLUMN_J) {
		this.R64_COLUMN_J = R64_COLUMN_J;
	}

	public BigDecimal getR64_COLUMN_K() {
		return R64_COLUMN_K;
	}

	public void setR64_COLUMN_K(BigDecimal R64_COLUMN_K) {
		this.R64_COLUMN_K = R64_COLUMN_K;
	}

	public BigDecimal getR64_COLUMN_L() {
		return R64_COLUMN_L;
	}

	public void setR64_COLUMN_L(BigDecimal R64_COLUMN_L) {
		this.R64_COLUMN_L = R64_COLUMN_L;
	}

	public BigDecimal getR64_COLUMN_M() {
		return R64_COLUMN_M;
	}

	public void setR64_COLUMN_M(BigDecimal R64_COLUMN_M) {
		this.R64_COLUMN_M = R64_COLUMN_M;
	}

	public BigDecimal getR64_COLUMN_N() {
		return R64_COLUMN_N;
	}

	public void setR64_COLUMN_N(BigDecimal R64_COLUMN_N) {
		this.R64_COLUMN_N = R64_COLUMN_N;
	}

	public String getR65_COLUMN_A() {
		return R65_COLUMN_A;
	}

	public void setR65_COLUMN_A(String R65_COLUMN_A) {
		this.R65_COLUMN_A = R65_COLUMN_A;
	}

	public BigDecimal getR65_COLUMN_B() {
		return R65_COLUMN_B;
	}

	public void setR65_COLUMN_B(BigDecimal R65_COLUMN_B) {
		this.R65_COLUMN_B = R65_COLUMN_B;
	}

	public BigDecimal getR65_COLUMN_C() {
		return R65_COLUMN_C;
	}

	public void setR65_COLUMN_C(BigDecimal R65_COLUMN_C) {
		this.R65_COLUMN_C = R65_COLUMN_C;
	}

	public BigDecimal getR65_COLUMN_D() {
		return R65_COLUMN_D;
	}

	public void setR65_COLUMN_D(BigDecimal R65_COLUMN_D) {
		this.R65_COLUMN_D = R65_COLUMN_D;
	}

	public BigDecimal getR65_COLUMN_E() {
		return R65_COLUMN_E;
	}

	public void setR65_COLUMN_E(BigDecimal R65_COLUMN_E) {
		this.R65_COLUMN_E = R65_COLUMN_E;
	}

	public BigDecimal getR65_COLUMN_F() {
		return R65_COLUMN_F;
	}

	public void setR65_COLUMN_F(BigDecimal R65_COLUMN_F) {
		this.R65_COLUMN_F = R65_COLUMN_F;
	}

	public BigDecimal getR65_COLUMN_G() {
		return R65_COLUMN_G;
	}

	public void setR65_COLUMN_G(BigDecimal R65_COLUMN_G) {
		this.R65_COLUMN_G = R65_COLUMN_G;
	}

	public BigDecimal getR65_COLUMN_H() {
		return R65_COLUMN_H;
	}

	public void setR65_COLUMN_H(BigDecimal R65_COLUMN_H) {
		this.R65_COLUMN_H = R65_COLUMN_H;
	}

	public BigDecimal getR65_COLUMN_I() {
		return R65_COLUMN_I;
	}

	public void setR65_COLUMN_I(BigDecimal R65_COLUMN_I) {
		this.R65_COLUMN_I = R65_COLUMN_I;
	}

	public BigDecimal getR65_COLUMN_J() {
		return R65_COLUMN_J;
	}

	public void setR65_COLUMN_J(BigDecimal R65_COLUMN_J) {
		this.R65_COLUMN_J = R65_COLUMN_J;
	}

	public BigDecimal getR65_COLUMN_K() {
		return R65_COLUMN_K;
	}

	public void setR65_COLUMN_K(BigDecimal R65_COLUMN_K) {
		this.R65_COLUMN_K = R65_COLUMN_K;
	}

	public BigDecimal getR65_COLUMN_L() {
		return R65_COLUMN_L;
	}

	public void setR65_COLUMN_L(BigDecimal R65_COLUMN_L) {
		this.R65_COLUMN_L = R65_COLUMN_L;
	}

	public BigDecimal getR65_COLUMN_M() {
		return R65_COLUMN_M;
	}

	public void setR65_COLUMN_M(BigDecimal R65_COLUMN_M) {
		this.R65_COLUMN_M = R65_COLUMN_M;
	}

	public BigDecimal getR65_COLUMN_N() {
		return R65_COLUMN_N;
	}

	public void setR65_COLUMN_N(BigDecimal R65_COLUMN_N) {
		this.R65_COLUMN_N = R65_COLUMN_N;
	}

	public String getR66_COLUMN_A() {
		return R66_COLUMN_A;
	}

	public void setR66_COLUMN_A(String R66_COLUMN_A) {
		this.R66_COLUMN_A = R66_COLUMN_A;
	}

	public BigDecimal getR66_COLUMN_B() {
		return R66_COLUMN_B;
	}

	public void setR66_COLUMN_B(BigDecimal R66_COLUMN_B) {
		this.R66_COLUMN_B = R66_COLUMN_B;
	}

	public BigDecimal getR66_COLUMN_C() {
		return R66_COLUMN_C;
	}

	public void setR66_COLUMN_C(BigDecimal R66_COLUMN_C) {
		this.R66_COLUMN_C = R66_COLUMN_C;
	}

	public BigDecimal getR66_COLUMN_D() {
		return R66_COLUMN_D;
	}

	public void setR66_COLUMN_D(BigDecimal R66_COLUMN_D) {
		this.R66_COLUMN_D = R66_COLUMN_D;
	}

	public BigDecimal getR66_COLUMN_E() {
		return R66_COLUMN_E;
	}

	public void setR66_COLUMN_E(BigDecimal R66_COLUMN_E) {
		this.R66_COLUMN_E = R66_COLUMN_E;
	}

	public BigDecimal getR66_COLUMN_F() {
		return R66_COLUMN_F;
	}

	public void setR66_COLUMN_F(BigDecimal R66_COLUMN_F) {
		this.R66_COLUMN_F = R66_COLUMN_F;
	}

	public BigDecimal getR66_COLUMN_G() {
		return R66_COLUMN_G;
	}

	public void setR66_COLUMN_G(BigDecimal R66_COLUMN_G) {
		this.R66_COLUMN_G = R66_COLUMN_G;
	}

	public BigDecimal getR66_COLUMN_H() {
		return R66_COLUMN_H;
	}

	public void setR66_COLUMN_H(BigDecimal R66_COLUMN_H) {
		this.R66_COLUMN_H = R66_COLUMN_H;
	}

	public BigDecimal getR66_COLUMN_I() {
		return R66_COLUMN_I;
	}

	public void setR66_COLUMN_I(BigDecimal R66_COLUMN_I) {
		this.R66_COLUMN_I = R66_COLUMN_I;
	}

	public BigDecimal getR66_COLUMN_J() {
		return R66_COLUMN_J;
	}

	public void setR66_COLUMN_J(BigDecimal R66_COLUMN_J) {
		this.R66_COLUMN_J = R66_COLUMN_J;
	}

	public BigDecimal getR66_COLUMN_K() {
		return R66_COLUMN_K;
	}

	public void setR66_COLUMN_K(BigDecimal R66_COLUMN_K) {
		this.R66_COLUMN_K = R66_COLUMN_K;
	}

	public BigDecimal getR66_COLUMN_L() {
		return R66_COLUMN_L;
	}

	public void setR66_COLUMN_L(BigDecimal R66_COLUMN_L) {
		this.R66_COLUMN_L = R66_COLUMN_L;
	}

	public BigDecimal getR66_COLUMN_M() {
		return R66_COLUMN_M;
	}

	public void setR66_COLUMN_M(BigDecimal R66_COLUMN_M) {
		this.R66_COLUMN_M = R66_COLUMN_M;
	}

	public BigDecimal getR66_COLUMN_N() {
		return R66_COLUMN_N;
	}

	public void setR66_COLUMN_N(BigDecimal R66_COLUMN_N) {
		this.R66_COLUMN_N = R66_COLUMN_N;
	}

	public String getR67_COLUMN_A() {
		return R67_COLUMN_A;
	}

	public void setR67_COLUMN_A(String R67_COLUMN_A) {
		this.R67_COLUMN_A = R67_COLUMN_A;
	}

	public BigDecimal getR67_COLUMN_B() {
		return R67_COLUMN_B;
	}

	public void setR67_COLUMN_B(BigDecimal R67_COLUMN_B) {
		this.R67_COLUMN_B = R67_COLUMN_B;
	}

	public BigDecimal getR67_COLUMN_C() {
		return R67_COLUMN_C;
	}

	public void setR67_COLUMN_C(BigDecimal R67_COLUMN_C) {
		this.R67_COLUMN_C = R67_COLUMN_C;
	}

	public BigDecimal getR67_COLUMN_D() {
		return R67_COLUMN_D;
	}

	public void setR67_COLUMN_D(BigDecimal R67_COLUMN_D) {
		this.R67_COLUMN_D = R67_COLUMN_D;
	}

	public BigDecimal getR67_COLUMN_E() {
		return R67_COLUMN_E;
	}

	public void setR67_COLUMN_E(BigDecimal R67_COLUMN_E) {
		this.R67_COLUMN_E = R67_COLUMN_E;
	}

	public BigDecimal getR67_COLUMN_F() {
		return R67_COLUMN_F;
	}

	public void setR67_COLUMN_F(BigDecimal R67_COLUMN_F) {
		this.R67_COLUMN_F = R67_COLUMN_F;
	}

	public BigDecimal getR67_COLUMN_G() {
		return R67_COLUMN_G;
	}

	public void setR67_COLUMN_G(BigDecimal R67_COLUMN_G) {
		this.R67_COLUMN_G = R67_COLUMN_G;
	}

	public BigDecimal getR67_COLUMN_H() {
		return R67_COLUMN_H;
	}

	public void setR67_COLUMN_H(BigDecimal R67_COLUMN_H) {
		this.R67_COLUMN_H = R67_COLUMN_H;
	}

	public BigDecimal getR67_COLUMN_I() {
		return R67_COLUMN_I;
	}

	public void setR67_COLUMN_I(BigDecimal R67_COLUMN_I) {
		this.R67_COLUMN_I = R67_COLUMN_I;
	}

	public BigDecimal getR67_COLUMN_J() {
		return R67_COLUMN_J;
	}

	public void setR67_COLUMN_J(BigDecimal R67_COLUMN_J) {
		this.R67_COLUMN_J = R67_COLUMN_J;
	}

	public BigDecimal getR67_COLUMN_K() {
		return R67_COLUMN_K;
	}

	public void setR67_COLUMN_K(BigDecimal R67_COLUMN_K) {
		this.R67_COLUMN_K = R67_COLUMN_K;
	}

	public BigDecimal getR67_COLUMN_L() {
		return R67_COLUMN_L;
	}

	public void setR67_COLUMN_L(BigDecimal R67_COLUMN_L) {
		this.R67_COLUMN_L = R67_COLUMN_L;
	}

	public BigDecimal getR67_COLUMN_M() {
		return R67_COLUMN_M;
	}

	public void setR67_COLUMN_M(BigDecimal R67_COLUMN_M) {
		this.R67_COLUMN_M = R67_COLUMN_M;
	}

	public BigDecimal getR67_COLUMN_N() {
		return R67_COLUMN_N;
	}

	public void setR67_COLUMN_N(BigDecimal R67_COLUMN_N) {
		this.R67_COLUMN_N = R67_COLUMN_N;
	}

	public String getR68_COLUMN_A() {
		return R68_COLUMN_A;
	}

	public void setR68_COLUMN_A(String R68_COLUMN_A) {
		this.R68_COLUMN_A = R68_COLUMN_A;
	}

	public BigDecimal getR68_COLUMN_B() {
		return R68_COLUMN_B;
	}

	public void setR68_COLUMN_B(BigDecimal R68_COLUMN_B) {
		this.R68_COLUMN_B = R68_COLUMN_B;
	}

	public BigDecimal getR68_COLUMN_C() {
		return R68_COLUMN_C;
	}

	public void setR68_COLUMN_C(BigDecimal R68_COLUMN_C) {
		this.R68_COLUMN_C = R68_COLUMN_C;
	}

	public BigDecimal getR68_COLUMN_D() {
		return R68_COLUMN_D;
	}

	public void setR68_COLUMN_D(BigDecimal R68_COLUMN_D) {
		this.R68_COLUMN_D = R68_COLUMN_D;
	}

	public BigDecimal getR68_COLUMN_E() {
		return R68_COLUMN_E;
	}

	public void setR68_COLUMN_E(BigDecimal R68_COLUMN_E) {
		this.R68_COLUMN_E = R68_COLUMN_E;
	}

	public BigDecimal getR68_COLUMN_F() {
		return R68_COLUMN_F;
	}

	public void setR68_COLUMN_F(BigDecimal R68_COLUMN_F) {
		this.R68_COLUMN_F = R68_COLUMN_F;
	}

	public BigDecimal getR68_COLUMN_G() {
		return R68_COLUMN_G;
	}

	public void setR68_COLUMN_G(BigDecimal R68_COLUMN_G) {
		this.R68_COLUMN_G = R68_COLUMN_G;
	}

	public BigDecimal getR68_COLUMN_H() {
		return R68_COLUMN_H;
	}

	public void setR68_COLUMN_H(BigDecimal R68_COLUMN_H) {
		this.R68_COLUMN_H = R68_COLUMN_H;
	}

	public BigDecimal getR68_COLUMN_I() {
		return R68_COLUMN_I;
	}

	public void setR68_COLUMN_I(BigDecimal R68_COLUMN_I) {
		this.R68_COLUMN_I = R68_COLUMN_I;
	}

	public BigDecimal getR68_COLUMN_J() {
		return R68_COLUMN_J;
	}

	public void setR68_COLUMN_J(BigDecimal R68_COLUMN_J) {
		this.R68_COLUMN_J = R68_COLUMN_J;
	}

	public BigDecimal getR68_COLUMN_K() {
		return R68_COLUMN_K;
	}

	public void setR68_COLUMN_K(BigDecimal R68_COLUMN_K) {
		this.R68_COLUMN_K = R68_COLUMN_K;
	}

	public BigDecimal getR68_COLUMN_L() {
		return R68_COLUMN_L;
	}

	public void setR68_COLUMN_L(BigDecimal R68_COLUMN_L) {
		this.R68_COLUMN_L = R68_COLUMN_L;
	}

	public BigDecimal getR68_COLUMN_M() {
		return R68_COLUMN_M;
	}

	public void setR68_COLUMN_M(BigDecimal R68_COLUMN_M) {
		this.R68_COLUMN_M = R68_COLUMN_M;
	}

	public BigDecimal getR68_COLUMN_N() {
		return R68_COLUMN_N;
	}

	public void setR68_COLUMN_N(BigDecimal R68_COLUMN_N) {
		this.R68_COLUMN_N = R68_COLUMN_N;
	}

	public String getR69_COLUMN_A() {
		return R69_COLUMN_A;
	}

	public void setR69_COLUMN_A(String R69_COLUMN_A) {
		this.R69_COLUMN_A = R69_COLUMN_A;
	}

	public BigDecimal getR69_COLUMN_B() {
		return R69_COLUMN_B;
	}

	public void setR69_COLUMN_B(BigDecimal R69_COLUMN_B) {
		this.R69_COLUMN_B = R69_COLUMN_B;
	}

	public BigDecimal getR69_COLUMN_C() {
		return R69_COLUMN_C;
	}

	public void setR69_COLUMN_C(BigDecimal R69_COLUMN_C) {
		this.R69_COLUMN_C = R69_COLUMN_C;
	}

	public BigDecimal getR69_COLUMN_D() {
		return R69_COLUMN_D;
	}

	public void setR69_COLUMN_D(BigDecimal R69_COLUMN_D) {
		this.R69_COLUMN_D = R69_COLUMN_D;
	}

	public BigDecimal getR69_COLUMN_E() {
		return R69_COLUMN_E;
	}

	public void setR69_COLUMN_E(BigDecimal R69_COLUMN_E) {
		this.R69_COLUMN_E = R69_COLUMN_E;
	}

	public BigDecimal getR69_COLUMN_F() {
		return R69_COLUMN_F;
	}

	public void setR69_COLUMN_F(BigDecimal R69_COLUMN_F) {
		this.R69_COLUMN_F = R69_COLUMN_F;
	}

	public BigDecimal getR69_COLUMN_G() {
		return R69_COLUMN_G;
	}

	public void setR69_COLUMN_G(BigDecimal R69_COLUMN_G) {
		this.R69_COLUMN_G = R69_COLUMN_G;
	}

	public BigDecimal getR69_COLUMN_H() {
		return R69_COLUMN_H;
	}

	public void setR69_COLUMN_H(BigDecimal R69_COLUMN_H) {
		this.R69_COLUMN_H = R69_COLUMN_H;
	}

	public BigDecimal getR69_COLUMN_I() {
		return R69_COLUMN_I;
	}

	public void setR69_COLUMN_I(BigDecimal R69_COLUMN_I) {
		this.R69_COLUMN_I = R69_COLUMN_I;
	}

	public BigDecimal getR69_COLUMN_J() {
		return R69_COLUMN_J;
	}

	public void setR69_COLUMN_J(BigDecimal R69_COLUMN_J) {
		this.R69_COLUMN_J = R69_COLUMN_J;
	}

	public BigDecimal getR69_COLUMN_K() {
		return R69_COLUMN_K;
	}

	public void setR69_COLUMN_K(BigDecimal R69_COLUMN_K) {
		this.R69_COLUMN_K = R69_COLUMN_K;
	}

	public BigDecimal getR69_COLUMN_L() {
		return R69_COLUMN_L;
	}

	public void setR69_COLUMN_L(BigDecimal R69_COLUMN_L) {
		this.R69_COLUMN_L = R69_COLUMN_L;
	}

	public BigDecimal getR69_COLUMN_M() {
		return R69_COLUMN_M;
	}

	public void setR69_COLUMN_M(BigDecimal R69_COLUMN_M) {
		this.R69_COLUMN_M = R69_COLUMN_M;
	}

	public BigDecimal getR69_COLUMN_N() {
		return R69_COLUMN_N;
	}

	public void setR69_COLUMN_N(BigDecimal R69_COLUMN_N) {
		this.R69_COLUMN_N = R69_COLUMN_N;
	}

	public String getR70_COLUMN_A() {
		return R70_COLUMN_A;
	}

	public void setR70_COLUMN_A(String R70_COLUMN_A) {
		this.R70_COLUMN_A = R70_COLUMN_A;
	}

	public BigDecimal getR70_COLUMN_B() {
		return R70_COLUMN_B;
	}

	public void setR70_COLUMN_B(BigDecimal R70_COLUMN_B) {
		this.R70_COLUMN_B = R70_COLUMN_B;
	}

	public BigDecimal getR70_COLUMN_C() {
		return R70_COLUMN_C;
	}

	public void setR70_COLUMN_C(BigDecimal R70_COLUMN_C) {
		this.R70_COLUMN_C = R70_COLUMN_C;
	}

	public BigDecimal getR70_COLUMN_D() {
		return R70_COLUMN_D;
	}

	public void setR70_COLUMN_D(BigDecimal R70_COLUMN_D) {
		this.R70_COLUMN_D = R70_COLUMN_D;
	}

	public BigDecimal getR70_COLUMN_E() {
		return R70_COLUMN_E;
	}

	public void setR70_COLUMN_E(BigDecimal R70_COLUMN_E) {
		this.R70_COLUMN_E = R70_COLUMN_E;
	}

	public BigDecimal getR70_COLUMN_F() {
		return R70_COLUMN_F;
	}

	public void setR70_COLUMN_F(BigDecimal R70_COLUMN_F) {
		this.R70_COLUMN_F = R70_COLUMN_F;
	}

	public BigDecimal getR70_COLUMN_G() {
		return R70_COLUMN_G;
	}

	public void setR70_COLUMN_G(BigDecimal R70_COLUMN_G) {
		this.R70_COLUMN_G = R70_COLUMN_G;
	}

	public BigDecimal getR70_COLUMN_H() {
		return R70_COLUMN_H;
	}

	public void setR70_COLUMN_H(BigDecimal R70_COLUMN_H) {
		this.R70_COLUMN_H = R70_COLUMN_H;
	}

	public BigDecimal getR70_COLUMN_I() {
		return R70_COLUMN_I;
	}

	public void setR70_COLUMN_I(BigDecimal R70_COLUMN_I) {
		this.R70_COLUMN_I = R70_COLUMN_I;
	}

	public BigDecimal getR70_COLUMN_J() {
		return R70_COLUMN_J;
	}

	public void setR70_COLUMN_J(BigDecimal R70_COLUMN_J) {
		this.R70_COLUMN_J = R70_COLUMN_J;
	}

	public BigDecimal getR70_COLUMN_K() {
		return R70_COLUMN_K;
	}

	public void setR70_COLUMN_K(BigDecimal R70_COLUMN_K) {
		this.R70_COLUMN_K = R70_COLUMN_K;
	}

	public BigDecimal getR70_COLUMN_L() {
		return R70_COLUMN_L;
	}

	public void setR70_COLUMN_L(BigDecimal R70_COLUMN_L) {
		this.R70_COLUMN_L = R70_COLUMN_L;
	}

	public BigDecimal getR70_COLUMN_M() {
		return R70_COLUMN_M;
	}

	public void setR70_COLUMN_M(BigDecimal R70_COLUMN_M) {
		this.R70_COLUMN_M = R70_COLUMN_M;
	}

	public BigDecimal getR70_COLUMN_N() {
		return R70_COLUMN_N;
	}

	public void setR70_COLUMN_N(BigDecimal R70_COLUMN_N) {
		this.R70_COLUMN_N = R70_COLUMN_N;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public BigDecimal getREPORT_VERSION() {
		return REPORT_VERSION;
	}

	public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
		this.REPORT_VERSION = REPORT_VERSION;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
		this.REPORT_FREQUENCY = REPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String REPORT_CODE) {
		this.REPORT_CODE = REPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String REPORT_DESC) {
		this.REPORT_DESC = REPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public SLS_WORKING_Summary_Entity1() {
		super();
	}
	}

	public static class SLS_WORKING_Summary_Entity2 {

	private String R71_COLUMN_A;
	private BigDecimal R71_COLUMN_B;
	private BigDecimal R71_COLUMN_C;
	private BigDecimal R71_COLUMN_D;
	private BigDecimal R71_COLUMN_E;
	private BigDecimal R71_COLUMN_F;
	private BigDecimal R71_COLUMN_G;
	private BigDecimal R71_COLUMN_H;
	private BigDecimal R71_COLUMN_I;
	private BigDecimal R71_COLUMN_J;
	private BigDecimal R71_COLUMN_K;
	private BigDecimal R71_COLUMN_L;
	private BigDecimal R71_COLUMN_M;
	private BigDecimal R71_COLUMN_N;
	private String R72_COLUMN_A;
	private BigDecimal R72_COLUMN_B;
	private BigDecimal R72_COLUMN_C;
	private BigDecimal R72_COLUMN_D;
	private BigDecimal R72_COLUMN_E;
	private BigDecimal R72_COLUMN_F;
	private BigDecimal R72_COLUMN_G;
	private BigDecimal R72_COLUMN_H;
	private BigDecimal R72_COLUMN_I;
	private BigDecimal R72_COLUMN_J;
	private BigDecimal R72_COLUMN_K;
	private BigDecimal R72_COLUMN_L;
	private BigDecimal R72_COLUMN_M;
	private BigDecimal R72_COLUMN_N;
	private String R73_COLUMN_A;
	private BigDecimal R73_COLUMN_B;
	private BigDecimal R73_COLUMN_C;
	private BigDecimal R73_COLUMN_D;
	private BigDecimal R73_COLUMN_E;
	private BigDecimal R73_COLUMN_F;
	private BigDecimal R73_COLUMN_G;
	private BigDecimal R73_COLUMN_H;
	private BigDecimal R73_COLUMN_I;
	private BigDecimal R73_COLUMN_J;
	private BigDecimal R73_COLUMN_K;
	private BigDecimal R73_COLUMN_L;
	private BigDecimal R73_COLUMN_M;
	private BigDecimal R73_COLUMN_N;
	private String R74_COLUMN_A;
	private BigDecimal R74_COLUMN_B;
	private BigDecimal R74_COLUMN_C;
	private BigDecimal R74_COLUMN_D;
	private BigDecimal R74_COLUMN_E;
	private BigDecimal R74_COLUMN_F;
	private BigDecimal R74_COLUMN_G;
	private BigDecimal R74_COLUMN_H;
	private BigDecimal R74_COLUMN_I;
	private BigDecimal R74_COLUMN_J;
	private BigDecimal R74_COLUMN_K;
	private BigDecimal R74_COLUMN_L;
	private BigDecimal R74_COLUMN_M;
	private BigDecimal R74_COLUMN_N;
	private String R75_COLUMN_A;
	private BigDecimal R75_COLUMN_B;
	private BigDecimal R75_COLUMN_C;
	private BigDecimal R75_COLUMN_D;
	private BigDecimal R75_COLUMN_E;
	private BigDecimal R75_COLUMN_F;
	private BigDecimal R75_COLUMN_G;
	private BigDecimal R75_COLUMN_H;
	private BigDecimal R75_COLUMN_I;
	private BigDecimal R75_COLUMN_J;
	private BigDecimal R75_COLUMN_K;
	private BigDecimal R75_COLUMN_L;
	private BigDecimal R75_COLUMN_M;
	private BigDecimal R75_COLUMN_N;
	private String R76_COLUMN_A;
	private BigDecimal R76_COLUMN_B;
	private BigDecimal R76_COLUMN_C;
	private BigDecimal R76_COLUMN_D;
	private BigDecimal R76_COLUMN_E;
	private BigDecimal R76_COLUMN_F;
	private BigDecimal R76_COLUMN_G;
	private BigDecimal R76_COLUMN_H;
	private BigDecimal R76_COLUMN_I;
	private BigDecimal R76_COLUMN_J;
	private BigDecimal R76_COLUMN_K;
	private BigDecimal R76_COLUMN_L;
	private BigDecimal R76_COLUMN_M;
	private BigDecimal R76_COLUMN_N;
	private String R77_COLUMN_A;
	private BigDecimal R77_COLUMN_B;
	private BigDecimal R77_COLUMN_C;
	private BigDecimal R77_COLUMN_D;
	private BigDecimal R77_COLUMN_E;
	private BigDecimal R77_COLUMN_F;
	private BigDecimal R77_COLUMN_G;
	private BigDecimal R77_COLUMN_H;
	private BigDecimal R77_COLUMN_I;
	private BigDecimal R77_COLUMN_J;
	private BigDecimal R77_COLUMN_K;
	private BigDecimal R77_COLUMN_L;
	private BigDecimal R77_COLUMN_M;
	private BigDecimal R77_COLUMN_N;
	private String R78_COLUMN_A;
	private BigDecimal R78_COLUMN_B;
	private BigDecimal R78_COLUMN_C;
	private BigDecimal R78_COLUMN_D;
	private BigDecimal R78_COLUMN_E;
	private BigDecimal R78_COLUMN_F;
	private BigDecimal R78_COLUMN_G;
	private BigDecimal R78_COLUMN_H;
	private BigDecimal R78_COLUMN_I;
	private BigDecimal R78_COLUMN_J;
	private BigDecimal R78_COLUMN_K;
	private BigDecimal R78_COLUMN_L;
	private BigDecimal R78_COLUMN_M;
	private BigDecimal R78_COLUMN_N;
	private String R79_COLUMN_A;
	private BigDecimal R79_COLUMN_B;
	private BigDecimal R79_COLUMN_C;
	private BigDecimal R79_COLUMN_D;
	private BigDecimal R79_COLUMN_E;
	private BigDecimal R79_COLUMN_F;
	private BigDecimal R79_COLUMN_G;
	private BigDecimal R79_COLUMN_H;
	private BigDecimal R79_COLUMN_I;
	private BigDecimal R79_COLUMN_J;
	private BigDecimal R79_COLUMN_K;
	private BigDecimal R79_COLUMN_L;
	private BigDecimal R79_COLUMN_M;
	private BigDecimal R79_COLUMN_N;
	private String R80_COLUMN_A;
	private BigDecimal R80_COLUMN_B;
	private BigDecimal R80_COLUMN_C;
	private BigDecimal R80_COLUMN_D;
	private BigDecimal R80_COLUMN_E;
	private BigDecimal R80_COLUMN_F;
	private BigDecimal R80_COLUMN_G;
	private BigDecimal R80_COLUMN_H;
	private BigDecimal R80_COLUMN_I;
	private BigDecimal R80_COLUMN_J;
	private BigDecimal R80_COLUMN_K;
	private BigDecimal R80_COLUMN_L;
	private BigDecimal R80_COLUMN_M;
	private BigDecimal R80_COLUMN_N;
	private String R81_COLUMN_A;
	private BigDecimal R81_COLUMN_B;
	private BigDecimal R81_COLUMN_C;
	private BigDecimal R81_COLUMN_D;
	private BigDecimal R81_COLUMN_E;
	private BigDecimal R81_COLUMN_F;
	private BigDecimal R81_COLUMN_G;
	private BigDecimal R81_COLUMN_H;
	private BigDecimal R81_COLUMN_I;
	private BigDecimal R81_COLUMN_J;
	private BigDecimal R81_COLUMN_K;
	private BigDecimal R81_COLUMN_L;
	private BigDecimal R81_COLUMN_M;
	private BigDecimal R81_COLUMN_N;
	private String R82_COLUMN_A;
	private BigDecimal R82_COLUMN_B;
	private BigDecimal R82_COLUMN_C;
	private BigDecimal R82_COLUMN_D;
	private BigDecimal R82_COLUMN_E;
	private BigDecimal R82_COLUMN_F;
	private BigDecimal R82_COLUMN_G;
	private BigDecimal R82_COLUMN_H;
	private BigDecimal R82_COLUMN_I;
	private BigDecimal R82_COLUMN_J;
	private BigDecimal R82_COLUMN_K;
	private BigDecimal R82_COLUMN_L;
	private BigDecimal R82_COLUMN_M;
	private BigDecimal R82_COLUMN_N;
	private String R83_COLUMN_A;
	private BigDecimal R83_COLUMN_B;
	private BigDecimal R83_COLUMN_C;
	private BigDecimal R83_COLUMN_D;
	private BigDecimal R83_COLUMN_E;
	private BigDecimal R83_COLUMN_F;
	private BigDecimal R83_COLUMN_G;
	private BigDecimal R83_COLUMN_H;
	private BigDecimal R83_COLUMN_I;
	private BigDecimal R83_COLUMN_J;
	private BigDecimal R83_COLUMN_K;
	private BigDecimal R83_COLUMN_L;
	private BigDecimal R83_COLUMN_M;
	private BigDecimal R83_COLUMN_N;
	private String R84_COLUMN_A;
	private BigDecimal R84_COLUMN_B;
	private BigDecimal R84_COLUMN_C;
	private BigDecimal R84_COLUMN_D;
	private BigDecimal R84_COLUMN_E;
	private BigDecimal R84_COLUMN_F;
	private BigDecimal R84_COLUMN_G;
	private BigDecimal R84_COLUMN_H;
	private BigDecimal R84_COLUMN_I;
	private BigDecimal R84_COLUMN_J;
	private BigDecimal R84_COLUMN_K;
	private BigDecimal R84_COLUMN_L;
	private BigDecimal R84_COLUMN_M;
	private BigDecimal R84_COLUMN_N;
	private String R85_COLUMN_A;
	private BigDecimal R85_COLUMN_B;
	private BigDecimal R85_COLUMN_C;
	private BigDecimal R85_COLUMN_D;
	private BigDecimal R85_COLUMN_E;
	private BigDecimal R85_COLUMN_F;
	private BigDecimal R85_COLUMN_G;
	private BigDecimal R85_COLUMN_H;
	private BigDecimal R85_COLUMN_I;
	private BigDecimal R85_COLUMN_J;
	private BigDecimal R85_COLUMN_K;
	private BigDecimal R85_COLUMN_L;
	private BigDecimal R85_COLUMN_M;
	private BigDecimal R85_COLUMN_N;
	private String R86_COLUMN_A;
	private BigDecimal R86_COLUMN_B;
	private BigDecimal R86_COLUMN_C;
	private BigDecimal R86_COLUMN_D;
	private BigDecimal R86_COLUMN_E;
	private BigDecimal R86_COLUMN_F;
	private BigDecimal R86_COLUMN_G;
	private BigDecimal R86_COLUMN_H;
	private BigDecimal R86_COLUMN_I;
	private BigDecimal R86_COLUMN_J;
	private BigDecimal R86_COLUMN_K;
	private BigDecimal R86_COLUMN_L;
	private BigDecimal R86_COLUMN_M;
	private BigDecimal R86_COLUMN_N;
	private String R87_COLUMN_A;
	private BigDecimal R87_COLUMN_B;
	private BigDecimal R87_COLUMN_C;
	private BigDecimal R87_COLUMN_D;
	private BigDecimal R87_COLUMN_E;
	private BigDecimal R87_COLUMN_F;
	private BigDecimal R87_COLUMN_G;
	private BigDecimal R87_COLUMN_H;
	private BigDecimal R87_COLUMN_I;
	private BigDecimal R87_COLUMN_J;
	private BigDecimal R87_COLUMN_K;
	private BigDecimal R87_COLUMN_L;
	private BigDecimal R87_COLUMN_M;
	private BigDecimal R87_COLUMN_N;
	private String R88_COLUMN_A;
	private BigDecimal R88_COLUMN_B;
	private BigDecimal R88_COLUMN_C;
	private BigDecimal R88_COLUMN_D;
	private BigDecimal R88_COLUMN_E;
	private BigDecimal R88_COLUMN_F;
	private BigDecimal R88_COLUMN_G;
	private BigDecimal R88_COLUMN_H;
	private BigDecimal R88_COLUMN_I;
	private BigDecimal R88_COLUMN_J;
	private BigDecimal R88_COLUMN_K;
	private BigDecimal R88_COLUMN_L;
	private BigDecimal R88_COLUMN_M;
	private BigDecimal R88_COLUMN_N;
	private String R89_COLUMN_A;
	private BigDecimal R89_COLUMN_B;
	private BigDecimal R89_COLUMN_C;
	private BigDecimal R89_COLUMN_D;
	private BigDecimal R89_COLUMN_E;
	private BigDecimal R89_COLUMN_F;
	private BigDecimal R89_COLUMN_G;
	private BigDecimal R89_COLUMN_H;
	private BigDecimal R89_COLUMN_I;
	private BigDecimal R89_COLUMN_J;
	private BigDecimal R89_COLUMN_K;
	private BigDecimal R89_COLUMN_L;
	private BigDecimal R89_COLUMN_M;
	private BigDecimal R89_COLUMN_N;
	private String R90_COLUMN_A;
	private BigDecimal R90_COLUMN_B;
	private BigDecimal R90_COLUMN_C;
	private BigDecimal R90_COLUMN_D;
	private BigDecimal R90_COLUMN_E;
	private BigDecimal R90_COLUMN_F;
	private BigDecimal R90_COLUMN_G;
	private BigDecimal R90_COLUMN_H;
	private BigDecimal R90_COLUMN_I;
	private BigDecimal R90_COLUMN_J;
	private BigDecimal R90_COLUMN_K;
	private BigDecimal R90_COLUMN_L;
	private BigDecimal R90_COLUMN_M;
	private BigDecimal R90_COLUMN_N;
	private String R91_COLUMN_A;
	private BigDecimal R91_COLUMN_B;
	private BigDecimal R91_COLUMN_C;
	private BigDecimal R91_COLUMN_D;
	private BigDecimal R91_COLUMN_E;
	private BigDecimal R91_COLUMN_F;
	private BigDecimal R91_COLUMN_G;
	private BigDecimal R91_COLUMN_H;
	private BigDecimal R91_COLUMN_I;
	private BigDecimal R91_COLUMN_J;
	private BigDecimal R91_COLUMN_K;
	private BigDecimal R91_COLUMN_L;
	private BigDecimal R91_COLUMN_M;
	private BigDecimal R91_COLUMN_N;
	private String R92_COLUMN_A;
	private BigDecimal R92_COLUMN_B;
	private BigDecimal R92_COLUMN_C;
	private BigDecimal R92_COLUMN_D;
	private BigDecimal R92_COLUMN_E;
	private BigDecimal R92_COLUMN_F;
	private BigDecimal R92_COLUMN_G;
	private BigDecimal R92_COLUMN_H;
	private BigDecimal R92_COLUMN_I;
	private BigDecimal R92_COLUMN_J;
	private BigDecimal R92_COLUMN_K;
	private BigDecimal R92_COLUMN_L;
	private BigDecimal R92_COLUMN_M;
	private BigDecimal R92_COLUMN_N;
	private Date REPORT_DATE;
	private BigDecimal REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public String getR71_COLUMN_A() {
		return R71_COLUMN_A;
	}

	public void setR71_COLUMN_A(String R71_COLUMN_A) {
		this.R71_COLUMN_A = R71_COLUMN_A;
	}

	public BigDecimal getR71_COLUMN_B() {
		return R71_COLUMN_B;
	}

	public void setR71_COLUMN_B(BigDecimal R71_COLUMN_B) {
		this.R71_COLUMN_B = R71_COLUMN_B;
	}

	public BigDecimal getR71_COLUMN_C() {
		return R71_COLUMN_C;
	}

	public void setR71_COLUMN_C(BigDecimal R71_COLUMN_C) {
		this.R71_COLUMN_C = R71_COLUMN_C;
	}

	public BigDecimal getR71_COLUMN_D() {
		return R71_COLUMN_D;
	}

	public void setR71_COLUMN_D(BigDecimal R71_COLUMN_D) {
		this.R71_COLUMN_D = R71_COLUMN_D;
	}

	public BigDecimal getR71_COLUMN_E() {
		return R71_COLUMN_E;
	}

	public void setR71_COLUMN_E(BigDecimal R71_COLUMN_E) {
		this.R71_COLUMN_E = R71_COLUMN_E;
	}

	public BigDecimal getR71_COLUMN_F() {
		return R71_COLUMN_F;
	}

	public void setR71_COLUMN_F(BigDecimal R71_COLUMN_F) {
		this.R71_COLUMN_F = R71_COLUMN_F;
	}

	public BigDecimal getR71_COLUMN_G() {
		return R71_COLUMN_G;
	}

	public void setR71_COLUMN_G(BigDecimal R71_COLUMN_G) {
		this.R71_COLUMN_G = R71_COLUMN_G;
	}

	public BigDecimal getR71_COLUMN_H() {
		return R71_COLUMN_H;
	}

	public void setR71_COLUMN_H(BigDecimal R71_COLUMN_H) {
		this.R71_COLUMN_H = R71_COLUMN_H;
	}

	public BigDecimal getR71_COLUMN_I() {
		return R71_COLUMN_I;
	}

	public void setR71_COLUMN_I(BigDecimal R71_COLUMN_I) {
		this.R71_COLUMN_I = R71_COLUMN_I;
	}

	public BigDecimal getR71_COLUMN_J() {
		return R71_COLUMN_J;
	}

	public void setR71_COLUMN_J(BigDecimal R71_COLUMN_J) {
		this.R71_COLUMN_J = R71_COLUMN_J;
	}

	public BigDecimal getR71_COLUMN_K() {
		return R71_COLUMN_K;
	}

	public void setR71_COLUMN_K(BigDecimal R71_COLUMN_K) {
		this.R71_COLUMN_K = R71_COLUMN_K;
	}

	public BigDecimal getR71_COLUMN_L() {
		return R71_COLUMN_L;
	}

	public void setR71_COLUMN_L(BigDecimal R71_COLUMN_L) {
		this.R71_COLUMN_L = R71_COLUMN_L;
	}

	public BigDecimal getR71_COLUMN_M() {
		return R71_COLUMN_M;
	}

	public void setR71_COLUMN_M(BigDecimal R71_COLUMN_M) {
		this.R71_COLUMN_M = R71_COLUMN_M;
	}

	public BigDecimal getR71_COLUMN_N() {
		return R71_COLUMN_N;
	}

	public void setR71_COLUMN_N(BigDecimal R71_COLUMN_N) {
		this.R71_COLUMN_N = R71_COLUMN_N;
	}

	public String getR72_COLUMN_A() {
		return R72_COLUMN_A;
	}

	public void setR72_COLUMN_A(String R72_COLUMN_A) {
		this.R72_COLUMN_A = R72_COLUMN_A;
	}

	public BigDecimal getR72_COLUMN_B() {
		return R72_COLUMN_B;
	}

	public void setR72_COLUMN_B(BigDecimal R72_COLUMN_B) {
		this.R72_COLUMN_B = R72_COLUMN_B;
	}

	public BigDecimal getR72_COLUMN_C() {
		return R72_COLUMN_C;
	}

	public void setR72_COLUMN_C(BigDecimal R72_COLUMN_C) {
		this.R72_COLUMN_C = R72_COLUMN_C;
	}

	public BigDecimal getR72_COLUMN_D() {
		return R72_COLUMN_D;
	}

	public void setR72_COLUMN_D(BigDecimal R72_COLUMN_D) {
		this.R72_COLUMN_D = R72_COLUMN_D;
	}

	public BigDecimal getR72_COLUMN_E() {
		return R72_COLUMN_E;
	}

	public void setR72_COLUMN_E(BigDecimal R72_COLUMN_E) {
		this.R72_COLUMN_E = R72_COLUMN_E;
	}

	public BigDecimal getR72_COLUMN_F() {
		return R72_COLUMN_F;
	}

	public void setR72_COLUMN_F(BigDecimal R72_COLUMN_F) {
		this.R72_COLUMN_F = R72_COLUMN_F;
	}

	public BigDecimal getR72_COLUMN_G() {
		return R72_COLUMN_G;
	}

	public void setR72_COLUMN_G(BigDecimal R72_COLUMN_G) {
		this.R72_COLUMN_G = R72_COLUMN_G;
	}

	public BigDecimal getR72_COLUMN_H() {
		return R72_COLUMN_H;
	}

	public void setR72_COLUMN_H(BigDecimal R72_COLUMN_H) {
		this.R72_COLUMN_H = R72_COLUMN_H;
	}

	public BigDecimal getR72_COLUMN_I() {
		return R72_COLUMN_I;
	}

	public void setR72_COLUMN_I(BigDecimal R72_COLUMN_I) {
		this.R72_COLUMN_I = R72_COLUMN_I;
	}

	public BigDecimal getR72_COLUMN_J() {
		return R72_COLUMN_J;
	}

	public void setR72_COLUMN_J(BigDecimal R72_COLUMN_J) {
		this.R72_COLUMN_J = R72_COLUMN_J;
	}

	public BigDecimal getR72_COLUMN_K() {
		return R72_COLUMN_K;
	}

	public void setR72_COLUMN_K(BigDecimal R72_COLUMN_K) {
		this.R72_COLUMN_K = R72_COLUMN_K;
	}

	public BigDecimal getR72_COLUMN_L() {
		return R72_COLUMN_L;
	}

	public void setR72_COLUMN_L(BigDecimal R72_COLUMN_L) {
		this.R72_COLUMN_L = R72_COLUMN_L;
	}

	public BigDecimal getR72_COLUMN_M() {
		return R72_COLUMN_M;
	}

	public void setR72_COLUMN_M(BigDecimal R72_COLUMN_M) {
		this.R72_COLUMN_M = R72_COLUMN_M;
	}

	public BigDecimal getR72_COLUMN_N() {
		return R72_COLUMN_N;
	}

	public void setR72_COLUMN_N(BigDecimal R72_COLUMN_N) {
		this.R72_COLUMN_N = R72_COLUMN_N;
	}

	public String getR73_COLUMN_A() {
		return R73_COLUMN_A;
	}

	public void setR73_COLUMN_A(String R73_COLUMN_A) {
		this.R73_COLUMN_A = R73_COLUMN_A;
	}

	public BigDecimal getR73_COLUMN_B() {
		return R73_COLUMN_B;
	}

	public void setR73_COLUMN_B(BigDecimal R73_COLUMN_B) {
		this.R73_COLUMN_B = R73_COLUMN_B;
	}

	public BigDecimal getR73_COLUMN_C() {
		return R73_COLUMN_C;
	}

	public void setR73_COLUMN_C(BigDecimal R73_COLUMN_C) {
		this.R73_COLUMN_C = R73_COLUMN_C;
	}

	public BigDecimal getR73_COLUMN_D() {
		return R73_COLUMN_D;
	}

	public void setR73_COLUMN_D(BigDecimal R73_COLUMN_D) {
		this.R73_COLUMN_D = R73_COLUMN_D;
	}

	public BigDecimal getR73_COLUMN_E() {
		return R73_COLUMN_E;
	}

	public void setR73_COLUMN_E(BigDecimal R73_COLUMN_E) {
		this.R73_COLUMN_E = R73_COLUMN_E;
	}

	public BigDecimal getR73_COLUMN_F() {
		return R73_COLUMN_F;
	}

	public void setR73_COLUMN_F(BigDecimal R73_COLUMN_F) {
		this.R73_COLUMN_F = R73_COLUMN_F;
	}

	public BigDecimal getR73_COLUMN_G() {
		return R73_COLUMN_G;
	}

	public void setR73_COLUMN_G(BigDecimal R73_COLUMN_G) {
		this.R73_COLUMN_G = R73_COLUMN_G;
	}

	public BigDecimal getR73_COLUMN_H() {
		return R73_COLUMN_H;
	}

	public void setR73_COLUMN_H(BigDecimal R73_COLUMN_H) {
		this.R73_COLUMN_H = R73_COLUMN_H;
	}

	public BigDecimal getR73_COLUMN_I() {
		return R73_COLUMN_I;
	}

	public void setR73_COLUMN_I(BigDecimal R73_COLUMN_I) {
		this.R73_COLUMN_I = R73_COLUMN_I;
	}

	public BigDecimal getR73_COLUMN_J() {
		return R73_COLUMN_J;
	}

	public void setR73_COLUMN_J(BigDecimal R73_COLUMN_J) {
		this.R73_COLUMN_J = R73_COLUMN_J;
	}

	public BigDecimal getR73_COLUMN_K() {
		return R73_COLUMN_K;
	}

	public void setR73_COLUMN_K(BigDecimal R73_COLUMN_K) {
		this.R73_COLUMN_K = R73_COLUMN_K;
	}

	public BigDecimal getR73_COLUMN_L() {
		return R73_COLUMN_L;
	}

	public void setR73_COLUMN_L(BigDecimal R73_COLUMN_L) {
		this.R73_COLUMN_L = R73_COLUMN_L;
	}

	public BigDecimal getR73_COLUMN_M() {
		return R73_COLUMN_M;
	}

	public void setR73_COLUMN_M(BigDecimal R73_COLUMN_M) {
		this.R73_COLUMN_M = R73_COLUMN_M;
	}

	public BigDecimal getR73_COLUMN_N() {
		return R73_COLUMN_N;
	}

	public void setR73_COLUMN_N(BigDecimal R73_COLUMN_N) {
		this.R73_COLUMN_N = R73_COLUMN_N;
	}

	public String getR74_COLUMN_A() {
		return R74_COLUMN_A;
	}

	public void setR74_COLUMN_A(String R74_COLUMN_A) {
		this.R74_COLUMN_A = R74_COLUMN_A;
	}

	public BigDecimal getR74_COLUMN_B() {
		return R74_COLUMN_B;
	}

	public void setR74_COLUMN_B(BigDecimal R74_COLUMN_B) {
		this.R74_COLUMN_B = R74_COLUMN_B;
	}

	public BigDecimal getR74_COLUMN_C() {
		return R74_COLUMN_C;
	}

	public void setR74_COLUMN_C(BigDecimal R74_COLUMN_C) {
		this.R74_COLUMN_C = R74_COLUMN_C;
	}

	public BigDecimal getR74_COLUMN_D() {
		return R74_COLUMN_D;
	}

	public void setR74_COLUMN_D(BigDecimal R74_COLUMN_D) {
		this.R74_COLUMN_D = R74_COLUMN_D;
	}

	public BigDecimal getR74_COLUMN_E() {
		return R74_COLUMN_E;
	}

	public void setR74_COLUMN_E(BigDecimal R74_COLUMN_E) {
		this.R74_COLUMN_E = R74_COLUMN_E;
	}

	public BigDecimal getR74_COLUMN_F() {
		return R74_COLUMN_F;
	}

	public void setR74_COLUMN_F(BigDecimal R74_COLUMN_F) {
		this.R74_COLUMN_F = R74_COLUMN_F;
	}

	public BigDecimal getR74_COLUMN_G() {
		return R74_COLUMN_G;
	}

	public void setR74_COLUMN_G(BigDecimal R74_COLUMN_G) {
		this.R74_COLUMN_G = R74_COLUMN_G;
	}

	public BigDecimal getR74_COLUMN_H() {
		return R74_COLUMN_H;
	}

	public void setR74_COLUMN_H(BigDecimal R74_COLUMN_H) {
		this.R74_COLUMN_H = R74_COLUMN_H;
	}

	public BigDecimal getR74_COLUMN_I() {
		return R74_COLUMN_I;
	}

	public void setR74_COLUMN_I(BigDecimal R74_COLUMN_I) {
		this.R74_COLUMN_I = R74_COLUMN_I;
	}

	public BigDecimal getR74_COLUMN_J() {
		return R74_COLUMN_J;
	}

	public void setR74_COLUMN_J(BigDecimal R74_COLUMN_J) {
		this.R74_COLUMN_J = R74_COLUMN_J;
	}

	public BigDecimal getR74_COLUMN_K() {
		return R74_COLUMN_K;
	}

	public void setR74_COLUMN_K(BigDecimal R74_COLUMN_K) {
		this.R74_COLUMN_K = R74_COLUMN_K;
	}

	public BigDecimal getR74_COLUMN_L() {
		return R74_COLUMN_L;
	}

	public void setR74_COLUMN_L(BigDecimal R74_COLUMN_L) {
		this.R74_COLUMN_L = R74_COLUMN_L;
	}

	public BigDecimal getR74_COLUMN_M() {
		return R74_COLUMN_M;
	}

	public void setR74_COLUMN_M(BigDecimal R74_COLUMN_M) {
		this.R74_COLUMN_M = R74_COLUMN_M;
	}

	public BigDecimal getR74_COLUMN_N() {
		return R74_COLUMN_N;
	}

	public void setR74_COLUMN_N(BigDecimal R74_COLUMN_N) {
		this.R74_COLUMN_N = R74_COLUMN_N;
	}

	public String getR75_COLUMN_A() {
		return R75_COLUMN_A;
	}

	public void setR75_COLUMN_A(String R75_COLUMN_A) {
		this.R75_COLUMN_A = R75_COLUMN_A;
	}

	public BigDecimal getR75_COLUMN_B() {
		return R75_COLUMN_B;
	}

	public void setR75_COLUMN_B(BigDecimal R75_COLUMN_B) {
		this.R75_COLUMN_B = R75_COLUMN_B;
	}

	public BigDecimal getR75_COLUMN_C() {
		return R75_COLUMN_C;
	}

	public void setR75_COLUMN_C(BigDecimal R75_COLUMN_C) {
		this.R75_COLUMN_C = R75_COLUMN_C;
	}

	public BigDecimal getR75_COLUMN_D() {
		return R75_COLUMN_D;
	}

	public void setR75_COLUMN_D(BigDecimal R75_COLUMN_D) {
		this.R75_COLUMN_D = R75_COLUMN_D;
	}

	public BigDecimal getR75_COLUMN_E() {
		return R75_COLUMN_E;
	}

	public void setR75_COLUMN_E(BigDecimal R75_COLUMN_E) {
		this.R75_COLUMN_E = R75_COLUMN_E;
	}

	public BigDecimal getR75_COLUMN_F() {
		return R75_COLUMN_F;
	}

	public void setR75_COLUMN_F(BigDecimal R75_COLUMN_F) {
		this.R75_COLUMN_F = R75_COLUMN_F;
	}

	public BigDecimal getR75_COLUMN_G() {
		return R75_COLUMN_G;
	}

	public void setR75_COLUMN_G(BigDecimal R75_COLUMN_G) {
		this.R75_COLUMN_G = R75_COLUMN_G;
	}

	public BigDecimal getR75_COLUMN_H() {
		return R75_COLUMN_H;
	}

	public void setR75_COLUMN_H(BigDecimal R75_COLUMN_H) {
		this.R75_COLUMN_H = R75_COLUMN_H;
	}

	public BigDecimal getR75_COLUMN_I() {
		return R75_COLUMN_I;
	}

	public void setR75_COLUMN_I(BigDecimal R75_COLUMN_I) {
		this.R75_COLUMN_I = R75_COLUMN_I;
	}

	public BigDecimal getR75_COLUMN_J() {
		return R75_COLUMN_J;
	}

	public void setR75_COLUMN_J(BigDecimal R75_COLUMN_J) {
		this.R75_COLUMN_J = R75_COLUMN_J;
	}

	public BigDecimal getR75_COLUMN_K() {
		return R75_COLUMN_K;
	}

	public void setR75_COLUMN_K(BigDecimal R75_COLUMN_K) {
		this.R75_COLUMN_K = R75_COLUMN_K;
	}

	public BigDecimal getR75_COLUMN_L() {
		return R75_COLUMN_L;
	}

	public void setR75_COLUMN_L(BigDecimal R75_COLUMN_L) {
		this.R75_COLUMN_L = R75_COLUMN_L;
	}

	public BigDecimal getR75_COLUMN_M() {
		return R75_COLUMN_M;
	}

	public void setR75_COLUMN_M(BigDecimal R75_COLUMN_M) {
		this.R75_COLUMN_M = R75_COLUMN_M;
	}

	public BigDecimal getR75_COLUMN_N() {
		return R75_COLUMN_N;
	}

	public void setR75_COLUMN_N(BigDecimal R75_COLUMN_N) {
		this.R75_COLUMN_N = R75_COLUMN_N;
	}

	public String getR76_COLUMN_A() {
		return R76_COLUMN_A;
	}

	public void setR76_COLUMN_A(String R76_COLUMN_A) {
		this.R76_COLUMN_A = R76_COLUMN_A;
	}

	public BigDecimal getR76_COLUMN_B() {
		return R76_COLUMN_B;
	}

	public void setR76_COLUMN_B(BigDecimal R76_COLUMN_B) {
		this.R76_COLUMN_B = R76_COLUMN_B;
	}

	public BigDecimal getR76_COLUMN_C() {
		return R76_COLUMN_C;
	}

	public void setR76_COLUMN_C(BigDecimal R76_COLUMN_C) {
		this.R76_COLUMN_C = R76_COLUMN_C;
	}

	public BigDecimal getR76_COLUMN_D() {
		return R76_COLUMN_D;
	}

	public void setR76_COLUMN_D(BigDecimal R76_COLUMN_D) {
		this.R76_COLUMN_D = R76_COLUMN_D;
	}

	public BigDecimal getR76_COLUMN_E() {
		return R76_COLUMN_E;
	}

	public void setR76_COLUMN_E(BigDecimal R76_COLUMN_E) {
		this.R76_COLUMN_E = R76_COLUMN_E;
	}

	public BigDecimal getR76_COLUMN_F() {
		return R76_COLUMN_F;
	}

	public void setR76_COLUMN_F(BigDecimal R76_COLUMN_F) {
		this.R76_COLUMN_F = R76_COLUMN_F;
	}

	public BigDecimal getR76_COLUMN_G() {
		return R76_COLUMN_G;
	}

	public void setR76_COLUMN_G(BigDecimal R76_COLUMN_G) {
		this.R76_COLUMN_G = R76_COLUMN_G;
	}

	public BigDecimal getR76_COLUMN_H() {
		return R76_COLUMN_H;
	}

	public void setR76_COLUMN_H(BigDecimal R76_COLUMN_H) {
		this.R76_COLUMN_H = R76_COLUMN_H;
	}

	public BigDecimal getR76_COLUMN_I() {
		return R76_COLUMN_I;
	}

	public void setR76_COLUMN_I(BigDecimal R76_COLUMN_I) {
		this.R76_COLUMN_I = R76_COLUMN_I;
	}

	public BigDecimal getR76_COLUMN_J() {
		return R76_COLUMN_J;
	}

	public void setR76_COLUMN_J(BigDecimal R76_COLUMN_J) {
		this.R76_COLUMN_J = R76_COLUMN_J;
	}

	public BigDecimal getR76_COLUMN_K() {
		return R76_COLUMN_K;
	}

	public void setR76_COLUMN_K(BigDecimal R76_COLUMN_K) {
		this.R76_COLUMN_K = R76_COLUMN_K;
	}

	public BigDecimal getR76_COLUMN_L() {
		return R76_COLUMN_L;
	}

	public void setR76_COLUMN_L(BigDecimal R76_COLUMN_L) {
		this.R76_COLUMN_L = R76_COLUMN_L;
	}

	public BigDecimal getR76_COLUMN_M() {
		return R76_COLUMN_M;
	}

	public void setR76_COLUMN_M(BigDecimal R76_COLUMN_M) {
		this.R76_COLUMN_M = R76_COLUMN_M;
	}

	public BigDecimal getR76_COLUMN_N() {
		return R76_COLUMN_N;
	}

	public void setR76_COLUMN_N(BigDecimal R76_COLUMN_N) {
		this.R76_COLUMN_N = R76_COLUMN_N;
	}

	public String getR77_COLUMN_A() {
		return R77_COLUMN_A;
	}

	public void setR77_COLUMN_A(String R77_COLUMN_A) {
		this.R77_COLUMN_A = R77_COLUMN_A;
	}

	public BigDecimal getR77_COLUMN_B() {
		return R77_COLUMN_B;
	}

	public void setR77_COLUMN_B(BigDecimal R77_COLUMN_B) {
		this.R77_COLUMN_B = R77_COLUMN_B;
	}

	public BigDecimal getR77_COLUMN_C() {
		return R77_COLUMN_C;
	}

	public void setR77_COLUMN_C(BigDecimal R77_COLUMN_C) {
		this.R77_COLUMN_C = R77_COLUMN_C;
	}

	public BigDecimal getR77_COLUMN_D() {
		return R77_COLUMN_D;
	}

	public void setR77_COLUMN_D(BigDecimal R77_COLUMN_D) {
		this.R77_COLUMN_D = R77_COLUMN_D;
	}

	public BigDecimal getR77_COLUMN_E() {
		return R77_COLUMN_E;
	}

	public void setR77_COLUMN_E(BigDecimal R77_COLUMN_E) {
		this.R77_COLUMN_E = R77_COLUMN_E;
	}

	public BigDecimal getR77_COLUMN_F() {
		return R77_COLUMN_F;
	}

	public void setR77_COLUMN_F(BigDecimal R77_COLUMN_F) {
		this.R77_COLUMN_F = R77_COLUMN_F;
	}

	public BigDecimal getR77_COLUMN_G() {
		return R77_COLUMN_G;
	}

	public void setR77_COLUMN_G(BigDecimal R77_COLUMN_G) {
		this.R77_COLUMN_G = R77_COLUMN_G;
	}

	public BigDecimal getR77_COLUMN_H() {
		return R77_COLUMN_H;
	}

	public void setR77_COLUMN_H(BigDecimal R77_COLUMN_H) {
		this.R77_COLUMN_H = R77_COLUMN_H;
	}

	public BigDecimal getR77_COLUMN_I() {
		return R77_COLUMN_I;
	}

	public void setR77_COLUMN_I(BigDecimal R77_COLUMN_I) {
		this.R77_COLUMN_I = R77_COLUMN_I;
	}

	public BigDecimal getR77_COLUMN_J() {
		return R77_COLUMN_J;
	}

	public void setR77_COLUMN_J(BigDecimal R77_COLUMN_J) {
		this.R77_COLUMN_J = R77_COLUMN_J;
	}

	public BigDecimal getR77_COLUMN_K() {
		return R77_COLUMN_K;
	}

	public void setR77_COLUMN_K(BigDecimal R77_COLUMN_K) {
		this.R77_COLUMN_K = R77_COLUMN_K;
	}

	public BigDecimal getR77_COLUMN_L() {
		return R77_COLUMN_L;
	}

	public void setR77_COLUMN_L(BigDecimal R77_COLUMN_L) {
		this.R77_COLUMN_L = R77_COLUMN_L;
	}

	public BigDecimal getR77_COLUMN_M() {
		return R77_COLUMN_M;
	}

	public void setR77_COLUMN_M(BigDecimal R77_COLUMN_M) {
		this.R77_COLUMN_M = R77_COLUMN_M;
	}

	public BigDecimal getR77_COLUMN_N() {
		return R77_COLUMN_N;
	}

	public void setR77_COLUMN_N(BigDecimal R77_COLUMN_N) {
		this.R77_COLUMN_N = R77_COLUMN_N;
	}

	public String getR78_COLUMN_A() {
		return R78_COLUMN_A;
	}

	public void setR78_COLUMN_A(String R78_COLUMN_A) {
		this.R78_COLUMN_A = R78_COLUMN_A;
	}

	public BigDecimal getR78_COLUMN_B() {
		return R78_COLUMN_B;
	}

	public void setR78_COLUMN_B(BigDecimal R78_COLUMN_B) {
		this.R78_COLUMN_B = R78_COLUMN_B;
	}

	public BigDecimal getR78_COLUMN_C() {
		return R78_COLUMN_C;
	}

	public void setR78_COLUMN_C(BigDecimal R78_COLUMN_C) {
		this.R78_COLUMN_C = R78_COLUMN_C;
	}

	public BigDecimal getR78_COLUMN_D() {
		return R78_COLUMN_D;
	}

	public void setR78_COLUMN_D(BigDecimal R78_COLUMN_D) {
		this.R78_COLUMN_D = R78_COLUMN_D;
	}

	public BigDecimal getR78_COLUMN_E() {
		return R78_COLUMN_E;
	}

	public void setR78_COLUMN_E(BigDecimal R78_COLUMN_E) {
		this.R78_COLUMN_E = R78_COLUMN_E;
	}

	public BigDecimal getR78_COLUMN_F() {
		return R78_COLUMN_F;
	}

	public void setR78_COLUMN_F(BigDecimal R78_COLUMN_F) {
		this.R78_COLUMN_F = R78_COLUMN_F;
	}

	public BigDecimal getR78_COLUMN_G() {
		return R78_COLUMN_G;
	}

	public void setR78_COLUMN_G(BigDecimal R78_COLUMN_G) {
		this.R78_COLUMN_G = R78_COLUMN_G;
	}

	public BigDecimal getR78_COLUMN_H() {
		return R78_COLUMN_H;
	}

	public void setR78_COLUMN_H(BigDecimal R78_COLUMN_H) {
		this.R78_COLUMN_H = R78_COLUMN_H;
	}

	public BigDecimal getR78_COLUMN_I() {
		return R78_COLUMN_I;
	}

	public void setR78_COLUMN_I(BigDecimal R78_COLUMN_I) {
		this.R78_COLUMN_I = R78_COLUMN_I;
	}

	public BigDecimal getR78_COLUMN_J() {
		return R78_COLUMN_J;
	}

	public void setR78_COLUMN_J(BigDecimal R78_COLUMN_J) {
		this.R78_COLUMN_J = R78_COLUMN_J;
	}

	public BigDecimal getR78_COLUMN_K() {
		return R78_COLUMN_K;
	}

	public void setR78_COLUMN_K(BigDecimal R78_COLUMN_K) {
		this.R78_COLUMN_K = R78_COLUMN_K;
	}

	public BigDecimal getR78_COLUMN_L() {
		return R78_COLUMN_L;
	}

	public void setR78_COLUMN_L(BigDecimal R78_COLUMN_L) {
		this.R78_COLUMN_L = R78_COLUMN_L;
	}

	public BigDecimal getR78_COLUMN_M() {
		return R78_COLUMN_M;
	}

	public void setR78_COLUMN_M(BigDecimal R78_COLUMN_M) {
		this.R78_COLUMN_M = R78_COLUMN_M;
	}

	public BigDecimal getR78_COLUMN_N() {
		return R78_COLUMN_N;
	}

	public void setR78_COLUMN_N(BigDecimal R78_COLUMN_N) {
		this.R78_COLUMN_N = R78_COLUMN_N;
	}

	public String getR79_COLUMN_A() {
		return R79_COLUMN_A;
	}

	public void setR79_COLUMN_A(String R79_COLUMN_A) {
		this.R79_COLUMN_A = R79_COLUMN_A;
	}

	public BigDecimal getR79_COLUMN_B() {
		return R79_COLUMN_B;
	}

	public void setR79_COLUMN_B(BigDecimal R79_COLUMN_B) {
		this.R79_COLUMN_B = R79_COLUMN_B;
	}

	public BigDecimal getR79_COLUMN_C() {
		return R79_COLUMN_C;
	}

	public void setR79_COLUMN_C(BigDecimal R79_COLUMN_C) {
		this.R79_COLUMN_C = R79_COLUMN_C;
	}

	public BigDecimal getR79_COLUMN_D() {
		return R79_COLUMN_D;
	}

	public void setR79_COLUMN_D(BigDecimal R79_COLUMN_D) {
		this.R79_COLUMN_D = R79_COLUMN_D;
	}

	public BigDecimal getR79_COLUMN_E() {
		return R79_COLUMN_E;
	}

	public void setR79_COLUMN_E(BigDecimal R79_COLUMN_E) {
		this.R79_COLUMN_E = R79_COLUMN_E;
	}

	public BigDecimal getR79_COLUMN_F() {
		return R79_COLUMN_F;
	}

	public void setR79_COLUMN_F(BigDecimal R79_COLUMN_F) {
		this.R79_COLUMN_F = R79_COLUMN_F;
	}

	public BigDecimal getR79_COLUMN_G() {
		return R79_COLUMN_G;
	}

	public void setR79_COLUMN_G(BigDecimal R79_COLUMN_G) {
		this.R79_COLUMN_G = R79_COLUMN_G;
	}

	public BigDecimal getR79_COLUMN_H() {
		return R79_COLUMN_H;
	}

	public void setR79_COLUMN_H(BigDecimal R79_COLUMN_H) {
		this.R79_COLUMN_H = R79_COLUMN_H;
	}

	public BigDecimal getR79_COLUMN_I() {
		return R79_COLUMN_I;
	}

	public void setR79_COLUMN_I(BigDecimal R79_COLUMN_I) {
		this.R79_COLUMN_I = R79_COLUMN_I;
	}

	public BigDecimal getR79_COLUMN_J() {
		return R79_COLUMN_J;
	}

	public void setR79_COLUMN_J(BigDecimal R79_COLUMN_J) {
		this.R79_COLUMN_J = R79_COLUMN_J;
	}

	public BigDecimal getR79_COLUMN_K() {
		return R79_COLUMN_K;
	}

	public void setR79_COLUMN_K(BigDecimal R79_COLUMN_K) {
		this.R79_COLUMN_K = R79_COLUMN_K;
	}

	public BigDecimal getR79_COLUMN_L() {
		return R79_COLUMN_L;
	}

	public void setR79_COLUMN_L(BigDecimal R79_COLUMN_L) {
		this.R79_COLUMN_L = R79_COLUMN_L;
	}

	public BigDecimal getR79_COLUMN_M() {
		return R79_COLUMN_M;
	}

	public void setR79_COLUMN_M(BigDecimal R79_COLUMN_M) {
		this.R79_COLUMN_M = R79_COLUMN_M;
	}

	public BigDecimal getR79_COLUMN_N() {
		return R79_COLUMN_N;
	}

	public void setR79_COLUMN_N(BigDecimal R79_COLUMN_N) {
		this.R79_COLUMN_N = R79_COLUMN_N;
	}

	public String getR80_COLUMN_A() {
		return R80_COLUMN_A;
	}

	public void setR80_COLUMN_A(String R80_COLUMN_A) {
		this.R80_COLUMN_A = R80_COLUMN_A;
	}

	public BigDecimal getR80_COLUMN_B() {
		return R80_COLUMN_B;
	}

	public void setR80_COLUMN_B(BigDecimal R80_COLUMN_B) {
		this.R80_COLUMN_B = R80_COLUMN_B;
	}

	public BigDecimal getR80_COLUMN_C() {
		return R80_COLUMN_C;
	}

	public void setR80_COLUMN_C(BigDecimal R80_COLUMN_C) {
		this.R80_COLUMN_C = R80_COLUMN_C;
	}

	public BigDecimal getR80_COLUMN_D() {
		return R80_COLUMN_D;
	}

	public void setR80_COLUMN_D(BigDecimal R80_COLUMN_D) {
		this.R80_COLUMN_D = R80_COLUMN_D;
	}

	public BigDecimal getR80_COLUMN_E() {
		return R80_COLUMN_E;
	}

	public void setR80_COLUMN_E(BigDecimal R80_COLUMN_E) {
		this.R80_COLUMN_E = R80_COLUMN_E;
	}

	public BigDecimal getR80_COLUMN_F() {
		return R80_COLUMN_F;
	}

	public void setR80_COLUMN_F(BigDecimal R80_COLUMN_F) {
		this.R80_COLUMN_F = R80_COLUMN_F;
	}

	public BigDecimal getR80_COLUMN_G() {
		return R80_COLUMN_G;
	}

	public void setR80_COLUMN_G(BigDecimal R80_COLUMN_G) {
		this.R80_COLUMN_G = R80_COLUMN_G;
	}

	public BigDecimal getR80_COLUMN_H() {
		return R80_COLUMN_H;
	}

	public void setR80_COLUMN_H(BigDecimal R80_COLUMN_H) {
		this.R80_COLUMN_H = R80_COLUMN_H;
	}

	public BigDecimal getR80_COLUMN_I() {
		return R80_COLUMN_I;
	}

	public void setR80_COLUMN_I(BigDecimal R80_COLUMN_I) {
		this.R80_COLUMN_I = R80_COLUMN_I;
	}

	public BigDecimal getR80_COLUMN_J() {
		return R80_COLUMN_J;
	}

	public void setR80_COLUMN_J(BigDecimal R80_COLUMN_J) {
		this.R80_COLUMN_J = R80_COLUMN_J;
	}

	public BigDecimal getR80_COLUMN_K() {
		return R80_COLUMN_K;
	}

	public void setR80_COLUMN_K(BigDecimal R80_COLUMN_K) {
		this.R80_COLUMN_K = R80_COLUMN_K;
	}

	public BigDecimal getR80_COLUMN_L() {
		return R80_COLUMN_L;
	}

	public void setR80_COLUMN_L(BigDecimal R80_COLUMN_L) {
		this.R80_COLUMN_L = R80_COLUMN_L;
	}

	public BigDecimal getR80_COLUMN_M() {
		return R80_COLUMN_M;
	}

	public void setR80_COLUMN_M(BigDecimal R80_COLUMN_M) {
		this.R80_COLUMN_M = R80_COLUMN_M;
	}

	public BigDecimal getR80_COLUMN_N() {
		return R80_COLUMN_N;
	}

	public void setR80_COLUMN_N(BigDecimal R80_COLUMN_N) {
		this.R80_COLUMN_N = R80_COLUMN_N;
	}

	public String getR81_COLUMN_A() {
		return R81_COLUMN_A;
	}

	public void setR81_COLUMN_A(String R81_COLUMN_A) {
		this.R81_COLUMN_A = R81_COLUMN_A;
	}

	public BigDecimal getR81_COLUMN_B() {
		return R81_COLUMN_B;
	}

	public void setR81_COLUMN_B(BigDecimal R81_COLUMN_B) {
		this.R81_COLUMN_B = R81_COLUMN_B;
	}

	public BigDecimal getR81_COLUMN_C() {
		return R81_COLUMN_C;
	}

	public void setR81_COLUMN_C(BigDecimal R81_COLUMN_C) {
		this.R81_COLUMN_C = R81_COLUMN_C;
	}

	public BigDecimal getR81_COLUMN_D() {
		return R81_COLUMN_D;
	}

	public void setR81_COLUMN_D(BigDecimal R81_COLUMN_D) {
		this.R81_COLUMN_D = R81_COLUMN_D;
	}

	public BigDecimal getR81_COLUMN_E() {
		return R81_COLUMN_E;
	}

	public void setR81_COLUMN_E(BigDecimal R81_COLUMN_E) {
		this.R81_COLUMN_E = R81_COLUMN_E;
	}

	public BigDecimal getR81_COLUMN_F() {
		return R81_COLUMN_F;
	}

	public void setR81_COLUMN_F(BigDecimal R81_COLUMN_F) {
		this.R81_COLUMN_F = R81_COLUMN_F;
	}

	public BigDecimal getR81_COLUMN_G() {
		return R81_COLUMN_G;
	}

	public void setR81_COLUMN_G(BigDecimal R81_COLUMN_G) {
		this.R81_COLUMN_G = R81_COLUMN_G;
	}

	public BigDecimal getR81_COLUMN_H() {
		return R81_COLUMN_H;
	}

	public void setR81_COLUMN_H(BigDecimal R81_COLUMN_H) {
		this.R81_COLUMN_H = R81_COLUMN_H;
	}

	public BigDecimal getR81_COLUMN_I() {
		return R81_COLUMN_I;
	}

	public void setR81_COLUMN_I(BigDecimal R81_COLUMN_I) {
		this.R81_COLUMN_I = R81_COLUMN_I;
	}

	public BigDecimal getR81_COLUMN_J() {
		return R81_COLUMN_J;
	}

	public void setR81_COLUMN_J(BigDecimal R81_COLUMN_J) {
		this.R81_COLUMN_J = R81_COLUMN_J;
	}

	public BigDecimal getR81_COLUMN_K() {
		return R81_COLUMN_K;
	}

	public void setR81_COLUMN_K(BigDecimal R81_COLUMN_K) {
		this.R81_COLUMN_K = R81_COLUMN_K;
	}

	public BigDecimal getR81_COLUMN_L() {
		return R81_COLUMN_L;
	}

	public void setR81_COLUMN_L(BigDecimal R81_COLUMN_L) {
		this.R81_COLUMN_L = R81_COLUMN_L;
	}

	public BigDecimal getR81_COLUMN_M() {
		return R81_COLUMN_M;
	}

	public void setR81_COLUMN_M(BigDecimal R81_COLUMN_M) {
		this.R81_COLUMN_M = R81_COLUMN_M;
	}

	public BigDecimal getR81_COLUMN_N() {
		return R81_COLUMN_N;
	}

	public void setR81_COLUMN_N(BigDecimal R81_COLUMN_N) {
		this.R81_COLUMN_N = R81_COLUMN_N;
	}

	public String getR82_COLUMN_A() {
		return R82_COLUMN_A;
	}

	public void setR82_COLUMN_A(String R82_COLUMN_A) {
		this.R82_COLUMN_A = R82_COLUMN_A;
	}

	public BigDecimal getR82_COLUMN_B() {
		return R82_COLUMN_B;
	}

	public void setR82_COLUMN_B(BigDecimal R82_COLUMN_B) {
		this.R82_COLUMN_B = R82_COLUMN_B;
	}

	public BigDecimal getR82_COLUMN_C() {
		return R82_COLUMN_C;
	}

	public void setR82_COLUMN_C(BigDecimal R82_COLUMN_C) {
		this.R82_COLUMN_C = R82_COLUMN_C;
	}

	public BigDecimal getR82_COLUMN_D() {
		return R82_COLUMN_D;
	}

	public void setR82_COLUMN_D(BigDecimal R82_COLUMN_D) {
		this.R82_COLUMN_D = R82_COLUMN_D;
	}

	public BigDecimal getR82_COLUMN_E() {
		return R82_COLUMN_E;
	}

	public void setR82_COLUMN_E(BigDecimal R82_COLUMN_E) {
		this.R82_COLUMN_E = R82_COLUMN_E;
	}

	public BigDecimal getR82_COLUMN_F() {
		return R82_COLUMN_F;
	}

	public void setR82_COLUMN_F(BigDecimal R82_COLUMN_F) {
		this.R82_COLUMN_F = R82_COLUMN_F;
	}

	public BigDecimal getR82_COLUMN_G() {
		return R82_COLUMN_G;
	}

	public void setR82_COLUMN_G(BigDecimal R82_COLUMN_G) {
		this.R82_COLUMN_G = R82_COLUMN_G;
	}

	public BigDecimal getR82_COLUMN_H() {
		return R82_COLUMN_H;
	}

	public void setR82_COLUMN_H(BigDecimal R82_COLUMN_H) {
		this.R82_COLUMN_H = R82_COLUMN_H;
	}

	public BigDecimal getR82_COLUMN_I() {
		return R82_COLUMN_I;
	}

	public void setR82_COLUMN_I(BigDecimal R82_COLUMN_I) {
		this.R82_COLUMN_I = R82_COLUMN_I;
	}

	public BigDecimal getR82_COLUMN_J() {
		return R82_COLUMN_J;
	}

	public void setR82_COLUMN_J(BigDecimal R82_COLUMN_J) {
		this.R82_COLUMN_J = R82_COLUMN_J;
	}

	public BigDecimal getR82_COLUMN_K() {
		return R82_COLUMN_K;
	}

	public void setR82_COLUMN_K(BigDecimal R82_COLUMN_K) {
		this.R82_COLUMN_K = R82_COLUMN_K;
	}

	public BigDecimal getR82_COLUMN_L() {
		return R82_COLUMN_L;
	}

	public void setR82_COLUMN_L(BigDecimal R82_COLUMN_L) {
		this.R82_COLUMN_L = R82_COLUMN_L;
	}

	public BigDecimal getR82_COLUMN_M() {
		return R82_COLUMN_M;
	}

	public void setR82_COLUMN_M(BigDecimal R82_COLUMN_M) {
		this.R82_COLUMN_M = R82_COLUMN_M;
	}

	public BigDecimal getR82_COLUMN_N() {
		return R82_COLUMN_N;
	}

	public void setR82_COLUMN_N(BigDecimal R82_COLUMN_N) {
		this.R82_COLUMN_N = R82_COLUMN_N;
	}

	public String getR83_COLUMN_A() {
		return R83_COLUMN_A;
	}

	public void setR83_COLUMN_A(String R83_COLUMN_A) {
		this.R83_COLUMN_A = R83_COLUMN_A;
	}

	public BigDecimal getR83_COLUMN_B() {
		return R83_COLUMN_B;
	}

	public void setR83_COLUMN_B(BigDecimal R83_COLUMN_B) {
		this.R83_COLUMN_B = R83_COLUMN_B;
	}

	public BigDecimal getR83_COLUMN_C() {
		return R83_COLUMN_C;
	}

	public void setR83_COLUMN_C(BigDecimal R83_COLUMN_C) {
		this.R83_COLUMN_C = R83_COLUMN_C;
	}

	public BigDecimal getR83_COLUMN_D() {
		return R83_COLUMN_D;
	}

	public void setR83_COLUMN_D(BigDecimal R83_COLUMN_D) {
		this.R83_COLUMN_D = R83_COLUMN_D;
	}

	public BigDecimal getR83_COLUMN_E() {
		return R83_COLUMN_E;
	}

	public void setR83_COLUMN_E(BigDecimal R83_COLUMN_E) {
		this.R83_COLUMN_E = R83_COLUMN_E;
	}

	public BigDecimal getR83_COLUMN_F() {
		return R83_COLUMN_F;
	}

	public void setR83_COLUMN_F(BigDecimal R83_COLUMN_F) {
		this.R83_COLUMN_F = R83_COLUMN_F;
	}

	public BigDecimal getR83_COLUMN_G() {
		return R83_COLUMN_G;
	}

	public void setR83_COLUMN_G(BigDecimal R83_COLUMN_G) {
		this.R83_COLUMN_G = R83_COLUMN_G;
	}

	public BigDecimal getR83_COLUMN_H() {
		return R83_COLUMN_H;
	}

	public void setR83_COLUMN_H(BigDecimal R83_COLUMN_H) {
		this.R83_COLUMN_H = R83_COLUMN_H;
	}

	public BigDecimal getR83_COLUMN_I() {
		return R83_COLUMN_I;
	}

	public void setR83_COLUMN_I(BigDecimal R83_COLUMN_I) {
		this.R83_COLUMN_I = R83_COLUMN_I;
	}

	public BigDecimal getR83_COLUMN_J() {
		return R83_COLUMN_J;
	}

	public void setR83_COLUMN_J(BigDecimal R83_COLUMN_J) {
		this.R83_COLUMN_J = R83_COLUMN_J;
	}

	public BigDecimal getR83_COLUMN_K() {
		return R83_COLUMN_K;
	}

	public void setR83_COLUMN_K(BigDecimal R83_COLUMN_K) {
		this.R83_COLUMN_K = R83_COLUMN_K;
	}

	public BigDecimal getR83_COLUMN_L() {
		return R83_COLUMN_L;
	}

	public void setR83_COLUMN_L(BigDecimal R83_COLUMN_L) {
		this.R83_COLUMN_L = R83_COLUMN_L;
	}

	public BigDecimal getR83_COLUMN_M() {
		return R83_COLUMN_M;
	}

	public void setR83_COLUMN_M(BigDecimal R83_COLUMN_M) {
		this.R83_COLUMN_M = R83_COLUMN_M;
	}

	public BigDecimal getR83_COLUMN_N() {
		return R83_COLUMN_N;
	}

	public void setR83_COLUMN_N(BigDecimal R83_COLUMN_N) {
		this.R83_COLUMN_N = R83_COLUMN_N;
	}

	public String getR84_COLUMN_A() {
		return R84_COLUMN_A;
	}

	public void setR84_COLUMN_A(String R84_COLUMN_A) {
		this.R84_COLUMN_A = R84_COLUMN_A;
	}

	public BigDecimal getR84_COLUMN_B() {
		return R84_COLUMN_B;
	}

	public void setR84_COLUMN_B(BigDecimal R84_COLUMN_B) {
		this.R84_COLUMN_B = R84_COLUMN_B;
	}

	public BigDecimal getR84_COLUMN_C() {
		return R84_COLUMN_C;
	}

	public void setR84_COLUMN_C(BigDecimal R84_COLUMN_C) {
		this.R84_COLUMN_C = R84_COLUMN_C;
	}

	public BigDecimal getR84_COLUMN_D() {
		return R84_COLUMN_D;
	}

	public void setR84_COLUMN_D(BigDecimal R84_COLUMN_D) {
		this.R84_COLUMN_D = R84_COLUMN_D;
	}

	public BigDecimal getR84_COLUMN_E() {
		return R84_COLUMN_E;
	}

	public void setR84_COLUMN_E(BigDecimal R84_COLUMN_E) {
		this.R84_COLUMN_E = R84_COLUMN_E;
	}

	public BigDecimal getR84_COLUMN_F() {
		return R84_COLUMN_F;
	}

	public void setR84_COLUMN_F(BigDecimal R84_COLUMN_F) {
		this.R84_COLUMN_F = R84_COLUMN_F;
	}

	public BigDecimal getR84_COLUMN_G() {
		return R84_COLUMN_G;
	}

	public void setR84_COLUMN_G(BigDecimal R84_COLUMN_G) {
		this.R84_COLUMN_G = R84_COLUMN_G;
	}

	public BigDecimal getR84_COLUMN_H() {
		return R84_COLUMN_H;
	}

	public void setR84_COLUMN_H(BigDecimal R84_COLUMN_H) {
		this.R84_COLUMN_H = R84_COLUMN_H;
	}

	public BigDecimal getR84_COLUMN_I() {
		return R84_COLUMN_I;
	}

	public void setR84_COLUMN_I(BigDecimal R84_COLUMN_I) {
		this.R84_COLUMN_I = R84_COLUMN_I;
	}

	public BigDecimal getR84_COLUMN_J() {
		return R84_COLUMN_J;
	}

	public void setR84_COLUMN_J(BigDecimal R84_COLUMN_J) {
		this.R84_COLUMN_J = R84_COLUMN_J;
	}

	public BigDecimal getR84_COLUMN_K() {
		return R84_COLUMN_K;
	}

	public void setR84_COLUMN_K(BigDecimal R84_COLUMN_K) {
		this.R84_COLUMN_K = R84_COLUMN_K;
	}

	public BigDecimal getR84_COLUMN_L() {
		return R84_COLUMN_L;
	}

	public void setR84_COLUMN_L(BigDecimal R84_COLUMN_L) {
		this.R84_COLUMN_L = R84_COLUMN_L;
	}

	public BigDecimal getR84_COLUMN_M() {
		return R84_COLUMN_M;
	}

	public void setR84_COLUMN_M(BigDecimal R84_COLUMN_M) {
		this.R84_COLUMN_M = R84_COLUMN_M;
	}

	public BigDecimal getR84_COLUMN_N() {
		return R84_COLUMN_N;
	}

	public void setR84_COLUMN_N(BigDecimal R84_COLUMN_N) {
		this.R84_COLUMN_N = R84_COLUMN_N;
	}

	public String getR85_COLUMN_A() {
		return R85_COLUMN_A;
	}

	public void setR85_COLUMN_A(String R85_COLUMN_A) {
		this.R85_COLUMN_A = R85_COLUMN_A;
	}

	public BigDecimal getR85_COLUMN_B() {
		return R85_COLUMN_B;
	}

	public void setR85_COLUMN_B(BigDecimal R85_COLUMN_B) {
		this.R85_COLUMN_B = R85_COLUMN_B;
	}

	public BigDecimal getR85_COLUMN_C() {
		return R85_COLUMN_C;
	}

	public void setR85_COLUMN_C(BigDecimal R85_COLUMN_C) {
		this.R85_COLUMN_C = R85_COLUMN_C;
	}

	public BigDecimal getR85_COLUMN_D() {
		return R85_COLUMN_D;
	}

	public void setR85_COLUMN_D(BigDecimal R85_COLUMN_D) {
		this.R85_COLUMN_D = R85_COLUMN_D;
	}

	public BigDecimal getR85_COLUMN_E() {
		return R85_COLUMN_E;
	}

	public void setR85_COLUMN_E(BigDecimal R85_COLUMN_E) {
		this.R85_COLUMN_E = R85_COLUMN_E;
	}

	public BigDecimal getR85_COLUMN_F() {
		return R85_COLUMN_F;
	}

	public void setR85_COLUMN_F(BigDecimal R85_COLUMN_F) {
		this.R85_COLUMN_F = R85_COLUMN_F;
	}

	public BigDecimal getR85_COLUMN_G() {
		return R85_COLUMN_G;
	}

	public void setR85_COLUMN_G(BigDecimal R85_COLUMN_G) {
		this.R85_COLUMN_G = R85_COLUMN_G;
	}

	public BigDecimal getR85_COLUMN_H() {
		return R85_COLUMN_H;
	}

	public void setR85_COLUMN_H(BigDecimal R85_COLUMN_H) {
		this.R85_COLUMN_H = R85_COLUMN_H;
	}

	public BigDecimal getR85_COLUMN_I() {
		return R85_COLUMN_I;
	}

	public void setR85_COLUMN_I(BigDecimal R85_COLUMN_I) {
		this.R85_COLUMN_I = R85_COLUMN_I;
	}

	public BigDecimal getR85_COLUMN_J() {
		return R85_COLUMN_J;
	}

	public void setR85_COLUMN_J(BigDecimal R85_COLUMN_J) {
		this.R85_COLUMN_J = R85_COLUMN_J;
	}

	public BigDecimal getR85_COLUMN_K() {
		return R85_COLUMN_K;
	}

	public void setR85_COLUMN_K(BigDecimal R85_COLUMN_K) {
		this.R85_COLUMN_K = R85_COLUMN_K;
	}

	public BigDecimal getR85_COLUMN_L() {
		return R85_COLUMN_L;
	}

	public void setR85_COLUMN_L(BigDecimal R85_COLUMN_L) {
		this.R85_COLUMN_L = R85_COLUMN_L;
	}

	public BigDecimal getR85_COLUMN_M() {
		return R85_COLUMN_M;
	}

	public void setR85_COLUMN_M(BigDecimal R85_COLUMN_M) {
		this.R85_COLUMN_M = R85_COLUMN_M;
	}

	public BigDecimal getR85_COLUMN_N() {
		return R85_COLUMN_N;
	}

	public void setR85_COLUMN_N(BigDecimal R85_COLUMN_N) {
		this.R85_COLUMN_N = R85_COLUMN_N;
	}

	public String getR86_COLUMN_A() {
		return R86_COLUMN_A;
	}

	public void setR86_COLUMN_A(String R86_COLUMN_A) {
		this.R86_COLUMN_A = R86_COLUMN_A;
	}

	public BigDecimal getR86_COLUMN_B() {
		return R86_COLUMN_B;
	}

	public void setR86_COLUMN_B(BigDecimal R86_COLUMN_B) {
		this.R86_COLUMN_B = R86_COLUMN_B;
	}

	public BigDecimal getR86_COLUMN_C() {
		return R86_COLUMN_C;
	}

	public void setR86_COLUMN_C(BigDecimal R86_COLUMN_C) {
		this.R86_COLUMN_C = R86_COLUMN_C;
	}

	public BigDecimal getR86_COLUMN_D() {
		return R86_COLUMN_D;
	}

	public void setR86_COLUMN_D(BigDecimal R86_COLUMN_D) {
		this.R86_COLUMN_D = R86_COLUMN_D;
	}

	public BigDecimal getR86_COLUMN_E() {
		return R86_COLUMN_E;
	}

	public void setR86_COLUMN_E(BigDecimal R86_COLUMN_E) {
		this.R86_COLUMN_E = R86_COLUMN_E;
	}

	public BigDecimal getR86_COLUMN_F() {
		return R86_COLUMN_F;
	}

	public void setR86_COLUMN_F(BigDecimal R86_COLUMN_F) {
		this.R86_COLUMN_F = R86_COLUMN_F;
	}

	public BigDecimal getR86_COLUMN_G() {
		return R86_COLUMN_G;
	}

	public void setR86_COLUMN_G(BigDecimal R86_COLUMN_G) {
		this.R86_COLUMN_G = R86_COLUMN_G;
	}

	public BigDecimal getR86_COLUMN_H() {
		return R86_COLUMN_H;
	}

	public void setR86_COLUMN_H(BigDecimal R86_COLUMN_H) {
		this.R86_COLUMN_H = R86_COLUMN_H;
	}

	public BigDecimal getR86_COLUMN_I() {
		return R86_COLUMN_I;
	}

	public void setR86_COLUMN_I(BigDecimal R86_COLUMN_I) {
		this.R86_COLUMN_I = R86_COLUMN_I;
	}

	public BigDecimal getR86_COLUMN_J() {
		return R86_COLUMN_J;
	}

	public void setR86_COLUMN_J(BigDecimal R86_COLUMN_J) {
		this.R86_COLUMN_J = R86_COLUMN_J;
	}

	public BigDecimal getR86_COLUMN_K() {
		return R86_COLUMN_K;
	}

	public void setR86_COLUMN_K(BigDecimal R86_COLUMN_K) {
		this.R86_COLUMN_K = R86_COLUMN_K;
	}

	public BigDecimal getR86_COLUMN_L() {
		return R86_COLUMN_L;
	}

	public void setR86_COLUMN_L(BigDecimal R86_COLUMN_L) {
		this.R86_COLUMN_L = R86_COLUMN_L;
	}

	public BigDecimal getR86_COLUMN_M() {
		return R86_COLUMN_M;
	}

	public void setR86_COLUMN_M(BigDecimal R86_COLUMN_M) {
		this.R86_COLUMN_M = R86_COLUMN_M;
	}

	public BigDecimal getR86_COLUMN_N() {
		return R86_COLUMN_N;
	}

	public void setR86_COLUMN_N(BigDecimal R86_COLUMN_N) {
		this.R86_COLUMN_N = R86_COLUMN_N;
	}

	public String getR87_COLUMN_A() {
		return R87_COLUMN_A;
	}

	public void setR87_COLUMN_A(String R87_COLUMN_A) {
		this.R87_COLUMN_A = R87_COLUMN_A;
	}

	public BigDecimal getR87_COLUMN_B() {
		return R87_COLUMN_B;
	}

	public void setR87_COLUMN_B(BigDecimal R87_COLUMN_B) {
		this.R87_COLUMN_B = R87_COLUMN_B;
	}

	public BigDecimal getR87_COLUMN_C() {
		return R87_COLUMN_C;
	}

	public void setR87_COLUMN_C(BigDecimal R87_COLUMN_C) {
		this.R87_COLUMN_C = R87_COLUMN_C;
	}

	public BigDecimal getR87_COLUMN_D() {
		return R87_COLUMN_D;
	}

	public void setR87_COLUMN_D(BigDecimal R87_COLUMN_D) {
		this.R87_COLUMN_D = R87_COLUMN_D;
	}

	public BigDecimal getR87_COLUMN_E() {
		return R87_COLUMN_E;
	}

	public void setR87_COLUMN_E(BigDecimal R87_COLUMN_E) {
		this.R87_COLUMN_E = R87_COLUMN_E;
	}

	public BigDecimal getR87_COLUMN_F() {
		return R87_COLUMN_F;
	}

	public void setR87_COLUMN_F(BigDecimal R87_COLUMN_F) {
		this.R87_COLUMN_F = R87_COLUMN_F;
	}

	public BigDecimal getR87_COLUMN_G() {
		return R87_COLUMN_G;
	}

	public void setR87_COLUMN_G(BigDecimal R87_COLUMN_G) {
		this.R87_COLUMN_G = R87_COLUMN_G;
	}

	public BigDecimal getR87_COLUMN_H() {
		return R87_COLUMN_H;
	}

	public void setR87_COLUMN_H(BigDecimal R87_COLUMN_H) {
		this.R87_COLUMN_H = R87_COLUMN_H;
	}

	public BigDecimal getR87_COLUMN_I() {
		return R87_COLUMN_I;
	}

	public void setR87_COLUMN_I(BigDecimal R87_COLUMN_I) {
		this.R87_COLUMN_I = R87_COLUMN_I;
	}

	public BigDecimal getR87_COLUMN_J() {
		return R87_COLUMN_J;
	}

	public void setR87_COLUMN_J(BigDecimal R87_COLUMN_J) {
		this.R87_COLUMN_J = R87_COLUMN_J;
	}

	public BigDecimal getR87_COLUMN_K() {
		return R87_COLUMN_K;
	}

	public void setR87_COLUMN_K(BigDecimal R87_COLUMN_K) {
		this.R87_COLUMN_K = R87_COLUMN_K;
	}

	public BigDecimal getR87_COLUMN_L() {
		return R87_COLUMN_L;
	}

	public void setR87_COLUMN_L(BigDecimal R87_COLUMN_L) {
		this.R87_COLUMN_L = R87_COLUMN_L;
	}

	public BigDecimal getR87_COLUMN_M() {
		return R87_COLUMN_M;
	}

	public void setR87_COLUMN_M(BigDecimal R87_COLUMN_M) {
		this.R87_COLUMN_M = R87_COLUMN_M;
	}

	public BigDecimal getR87_COLUMN_N() {
		return R87_COLUMN_N;
	}

	public void setR87_COLUMN_N(BigDecimal R87_COLUMN_N) {
		this.R87_COLUMN_N = R87_COLUMN_N;
	}

	public String getR88_COLUMN_A() {
		return R88_COLUMN_A;
	}

	public void setR88_COLUMN_A(String R88_COLUMN_A) {
		this.R88_COLUMN_A = R88_COLUMN_A;
	}

	public BigDecimal getR88_COLUMN_B() {
		return R88_COLUMN_B;
	}

	public void setR88_COLUMN_B(BigDecimal R88_COLUMN_B) {
		this.R88_COLUMN_B = R88_COLUMN_B;
	}

	public BigDecimal getR88_COLUMN_C() {
		return R88_COLUMN_C;
	}

	public void setR88_COLUMN_C(BigDecimal R88_COLUMN_C) {
		this.R88_COLUMN_C = R88_COLUMN_C;
	}

	public BigDecimal getR88_COLUMN_D() {
		return R88_COLUMN_D;
	}

	public void setR88_COLUMN_D(BigDecimal R88_COLUMN_D) {
		this.R88_COLUMN_D = R88_COLUMN_D;
	}

	public BigDecimal getR88_COLUMN_E() {
		return R88_COLUMN_E;
	}

	public void setR88_COLUMN_E(BigDecimal R88_COLUMN_E) {
		this.R88_COLUMN_E = R88_COLUMN_E;
	}

	public BigDecimal getR88_COLUMN_F() {
		return R88_COLUMN_F;
	}

	public void setR88_COLUMN_F(BigDecimal R88_COLUMN_F) {
		this.R88_COLUMN_F = R88_COLUMN_F;
	}

	public BigDecimal getR88_COLUMN_G() {
		return R88_COLUMN_G;
	}

	public void setR88_COLUMN_G(BigDecimal R88_COLUMN_G) {
		this.R88_COLUMN_G = R88_COLUMN_G;
	}

	public BigDecimal getR88_COLUMN_H() {
		return R88_COLUMN_H;
	}

	public void setR88_COLUMN_H(BigDecimal R88_COLUMN_H) {
		this.R88_COLUMN_H = R88_COLUMN_H;
	}

	public BigDecimal getR88_COLUMN_I() {
		return R88_COLUMN_I;
	}

	public void setR88_COLUMN_I(BigDecimal R88_COLUMN_I) {
		this.R88_COLUMN_I = R88_COLUMN_I;
	}

	public BigDecimal getR88_COLUMN_J() {
		return R88_COLUMN_J;
	}

	public void setR88_COLUMN_J(BigDecimal R88_COLUMN_J) {
		this.R88_COLUMN_J = R88_COLUMN_J;
	}

	public BigDecimal getR88_COLUMN_K() {
		return R88_COLUMN_K;
	}

	public void setR88_COLUMN_K(BigDecimal R88_COLUMN_K) {
		this.R88_COLUMN_K = R88_COLUMN_K;
	}

	public BigDecimal getR88_COLUMN_L() {
		return R88_COLUMN_L;
	}

	public void setR88_COLUMN_L(BigDecimal R88_COLUMN_L) {
		this.R88_COLUMN_L = R88_COLUMN_L;
	}

	public BigDecimal getR88_COLUMN_M() {
		return R88_COLUMN_M;
	}

	public void setR88_COLUMN_M(BigDecimal R88_COLUMN_M) {
		this.R88_COLUMN_M = R88_COLUMN_M;
	}

	public BigDecimal getR88_COLUMN_N() {
		return R88_COLUMN_N;
	}

	public void setR88_COLUMN_N(BigDecimal R88_COLUMN_N) {
		this.R88_COLUMN_N = R88_COLUMN_N;
	}

	public String getR89_COLUMN_A() {
		return R89_COLUMN_A;
	}

	public void setR89_COLUMN_A(String R89_COLUMN_A) {
		this.R89_COLUMN_A = R89_COLUMN_A;
	}

	public BigDecimal getR89_COLUMN_B() {
		return R89_COLUMN_B;
	}

	public void setR89_COLUMN_B(BigDecimal R89_COLUMN_B) {
		this.R89_COLUMN_B = R89_COLUMN_B;
	}

	public BigDecimal getR89_COLUMN_C() {
		return R89_COLUMN_C;
	}

	public void setR89_COLUMN_C(BigDecimal R89_COLUMN_C) {
		this.R89_COLUMN_C = R89_COLUMN_C;
	}

	public BigDecimal getR89_COLUMN_D() {
		return R89_COLUMN_D;
	}

	public void setR89_COLUMN_D(BigDecimal R89_COLUMN_D) {
		this.R89_COLUMN_D = R89_COLUMN_D;
	}

	public BigDecimal getR89_COLUMN_E() {
		return R89_COLUMN_E;
	}

	public void setR89_COLUMN_E(BigDecimal R89_COLUMN_E) {
		this.R89_COLUMN_E = R89_COLUMN_E;
	}

	public BigDecimal getR89_COLUMN_F() {
		return R89_COLUMN_F;
	}

	public void setR89_COLUMN_F(BigDecimal R89_COLUMN_F) {
		this.R89_COLUMN_F = R89_COLUMN_F;
	}

	public BigDecimal getR89_COLUMN_G() {
		return R89_COLUMN_G;
	}

	public void setR89_COLUMN_G(BigDecimal R89_COLUMN_G) {
		this.R89_COLUMN_G = R89_COLUMN_G;
	}

	public BigDecimal getR89_COLUMN_H() {
		return R89_COLUMN_H;
	}

	public void setR89_COLUMN_H(BigDecimal R89_COLUMN_H) {
		this.R89_COLUMN_H = R89_COLUMN_H;
	}

	public BigDecimal getR89_COLUMN_I() {
		return R89_COLUMN_I;
	}

	public void setR89_COLUMN_I(BigDecimal R89_COLUMN_I) {
		this.R89_COLUMN_I = R89_COLUMN_I;
	}

	public BigDecimal getR89_COLUMN_J() {
		return R89_COLUMN_J;
	}

	public void setR89_COLUMN_J(BigDecimal R89_COLUMN_J) {
		this.R89_COLUMN_J = R89_COLUMN_J;
	}

	public BigDecimal getR89_COLUMN_K() {
		return R89_COLUMN_K;
	}

	public void setR89_COLUMN_K(BigDecimal R89_COLUMN_K) {
		this.R89_COLUMN_K = R89_COLUMN_K;
	}

	public BigDecimal getR89_COLUMN_L() {
		return R89_COLUMN_L;
	}

	public void setR89_COLUMN_L(BigDecimal R89_COLUMN_L) {
		this.R89_COLUMN_L = R89_COLUMN_L;
	}

	public BigDecimal getR89_COLUMN_M() {
		return R89_COLUMN_M;
	}

	public void setR89_COLUMN_M(BigDecimal R89_COLUMN_M) {
		this.R89_COLUMN_M = R89_COLUMN_M;
	}

	public BigDecimal getR89_COLUMN_N() {
		return R89_COLUMN_N;
	}

	public void setR89_COLUMN_N(BigDecimal R89_COLUMN_N) {
		this.R89_COLUMN_N = R89_COLUMN_N;
	}

	public String getR90_COLUMN_A() {
		return R90_COLUMN_A;
	}

	public void setR90_COLUMN_A(String R90_COLUMN_A) {
		this.R90_COLUMN_A = R90_COLUMN_A;
	}

	public BigDecimal getR90_COLUMN_B() {
		return R90_COLUMN_B;
	}

	public void setR90_COLUMN_B(BigDecimal R90_COLUMN_B) {
		this.R90_COLUMN_B = R90_COLUMN_B;
	}

	public BigDecimal getR90_COLUMN_C() {
		return R90_COLUMN_C;
	}

	public void setR90_COLUMN_C(BigDecimal R90_COLUMN_C) {
		this.R90_COLUMN_C = R90_COLUMN_C;
	}

	public BigDecimal getR90_COLUMN_D() {
		return R90_COLUMN_D;
	}

	public void setR90_COLUMN_D(BigDecimal R90_COLUMN_D) {
		this.R90_COLUMN_D = R90_COLUMN_D;
	}

	public BigDecimal getR90_COLUMN_E() {
		return R90_COLUMN_E;
	}

	public void setR90_COLUMN_E(BigDecimal R90_COLUMN_E) {
		this.R90_COLUMN_E = R90_COLUMN_E;
	}

	public BigDecimal getR90_COLUMN_F() {
		return R90_COLUMN_F;
	}

	public void setR90_COLUMN_F(BigDecimal R90_COLUMN_F) {
		this.R90_COLUMN_F = R90_COLUMN_F;
	}

	public BigDecimal getR90_COLUMN_G() {
		return R90_COLUMN_G;
	}

	public void setR90_COLUMN_G(BigDecimal R90_COLUMN_G) {
		this.R90_COLUMN_G = R90_COLUMN_G;
	}

	public BigDecimal getR90_COLUMN_H() {
		return R90_COLUMN_H;
	}

	public void setR90_COLUMN_H(BigDecimal R90_COLUMN_H) {
		this.R90_COLUMN_H = R90_COLUMN_H;
	}

	public BigDecimal getR90_COLUMN_I() {
		return R90_COLUMN_I;
	}

	public void setR90_COLUMN_I(BigDecimal R90_COLUMN_I) {
		this.R90_COLUMN_I = R90_COLUMN_I;
	}

	public BigDecimal getR90_COLUMN_J() {
		return R90_COLUMN_J;
	}

	public void setR90_COLUMN_J(BigDecimal R90_COLUMN_J) {
		this.R90_COLUMN_J = R90_COLUMN_J;
	}

	public BigDecimal getR90_COLUMN_K() {
		return R90_COLUMN_K;
	}

	public void setR90_COLUMN_K(BigDecimal R90_COLUMN_K) {
		this.R90_COLUMN_K = R90_COLUMN_K;
	}

	public BigDecimal getR90_COLUMN_L() {
		return R90_COLUMN_L;
	}

	public void setR90_COLUMN_L(BigDecimal R90_COLUMN_L) {
		this.R90_COLUMN_L = R90_COLUMN_L;
	}

	public BigDecimal getR90_COLUMN_M() {
		return R90_COLUMN_M;
	}

	public void setR90_COLUMN_M(BigDecimal R90_COLUMN_M) {
		this.R90_COLUMN_M = R90_COLUMN_M;
	}

	public BigDecimal getR90_COLUMN_N() {
		return R90_COLUMN_N;
	}

	public void setR90_COLUMN_N(BigDecimal R90_COLUMN_N) {
		this.R90_COLUMN_N = R90_COLUMN_N;
	}

	public String getR91_COLUMN_A() {
		return R91_COLUMN_A;
	}

	public void setR91_COLUMN_A(String R91_COLUMN_A) {
		this.R91_COLUMN_A = R91_COLUMN_A;
	}

	public BigDecimal getR91_COLUMN_B() {
		return R91_COLUMN_B;
	}

	public void setR91_COLUMN_B(BigDecimal R91_COLUMN_B) {
		this.R91_COLUMN_B = R91_COLUMN_B;
	}

	public BigDecimal getR91_COLUMN_C() {
		return R91_COLUMN_C;
	}

	public void setR91_COLUMN_C(BigDecimal R91_COLUMN_C) {
		this.R91_COLUMN_C = R91_COLUMN_C;
	}

	public BigDecimal getR91_COLUMN_D() {
		return R91_COLUMN_D;
	}

	public void setR91_COLUMN_D(BigDecimal R91_COLUMN_D) {
		this.R91_COLUMN_D = R91_COLUMN_D;
	}

	public BigDecimal getR91_COLUMN_E() {
		return R91_COLUMN_E;
	}

	public void setR91_COLUMN_E(BigDecimal R91_COLUMN_E) {
		this.R91_COLUMN_E = R91_COLUMN_E;
	}

	public BigDecimal getR91_COLUMN_F() {
		return R91_COLUMN_F;
	}

	public void setR91_COLUMN_F(BigDecimal R91_COLUMN_F) {
		this.R91_COLUMN_F = R91_COLUMN_F;
	}

	public BigDecimal getR91_COLUMN_G() {
		return R91_COLUMN_G;
	}

	public void setR91_COLUMN_G(BigDecimal R91_COLUMN_G) {
		this.R91_COLUMN_G = R91_COLUMN_G;
	}

	public BigDecimal getR91_COLUMN_H() {
		return R91_COLUMN_H;
	}

	public void setR91_COLUMN_H(BigDecimal R91_COLUMN_H) {
		this.R91_COLUMN_H = R91_COLUMN_H;
	}

	public BigDecimal getR91_COLUMN_I() {
		return R91_COLUMN_I;
	}

	public void setR91_COLUMN_I(BigDecimal R91_COLUMN_I) {
		this.R91_COLUMN_I = R91_COLUMN_I;
	}

	public BigDecimal getR91_COLUMN_J() {
		return R91_COLUMN_J;
	}

	public void setR91_COLUMN_J(BigDecimal R91_COLUMN_J) {
		this.R91_COLUMN_J = R91_COLUMN_J;
	}

	public BigDecimal getR91_COLUMN_K() {
		return R91_COLUMN_K;
	}

	public void setR91_COLUMN_K(BigDecimal R91_COLUMN_K) {
		this.R91_COLUMN_K = R91_COLUMN_K;
	}

	public BigDecimal getR91_COLUMN_L() {
		return R91_COLUMN_L;
	}

	public void setR91_COLUMN_L(BigDecimal R91_COLUMN_L) {
		this.R91_COLUMN_L = R91_COLUMN_L;
	}

	public BigDecimal getR91_COLUMN_M() {
		return R91_COLUMN_M;
	}

	public void setR91_COLUMN_M(BigDecimal R91_COLUMN_M) {
		this.R91_COLUMN_M = R91_COLUMN_M;
	}

	public BigDecimal getR91_COLUMN_N() {
		return R91_COLUMN_N;
	}

	public void setR91_COLUMN_N(BigDecimal R91_COLUMN_N) {
		this.R91_COLUMN_N = R91_COLUMN_N;
	}

	public String getR92_COLUMN_A() {
		return R92_COLUMN_A;
	}

	public void setR92_COLUMN_A(String R92_COLUMN_A) {
		this.R92_COLUMN_A = R92_COLUMN_A;
	}

	public BigDecimal getR92_COLUMN_B() {
		return R92_COLUMN_B;
	}

	public void setR92_COLUMN_B(BigDecimal R92_COLUMN_B) {
		this.R92_COLUMN_B = R92_COLUMN_B;
	}

	public BigDecimal getR92_COLUMN_C() {
		return R92_COLUMN_C;
	}

	public void setR92_COLUMN_C(BigDecimal R92_COLUMN_C) {
		this.R92_COLUMN_C = R92_COLUMN_C;
	}

	public BigDecimal getR92_COLUMN_D() {
		return R92_COLUMN_D;
	}

	public void setR92_COLUMN_D(BigDecimal R92_COLUMN_D) {
		this.R92_COLUMN_D = R92_COLUMN_D;
	}

	public BigDecimal getR92_COLUMN_E() {
		return R92_COLUMN_E;
	}

	public void setR92_COLUMN_E(BigDecimal R92_COLUMN_E) {
		this.R92_COLUMN_E = R92_COLUMN_E;
	}

	public BigDecimal getR92_COLUMN_F() {
		return R92_COLUMN_F;
	}

	public void setR92_COLUMN_F(BigDecimal R92_COLUMN_F) {
		this.R92_COLUMN_F = R92_COLUMN_F;
	}

	public BigDecimal getR92_COLUMN_G() {
		return R92_COLUMN_G;
	}

	public void setR92_COLUMN_G(BigDecimal R92_COLUMN_G) {
		this.R92_COLUMN_G = R92_COLUMN_G;
	}

	public BigDecimal getR92_COLUMN_H() {
		return R92_COLUMN_H;
	}

	public void setR92_COLUMN_H(BigDecimal R92_COLUMN_H) {
		this.R92_COLUMN_H = R92_COLUMN_H;
	}

	public BigDecimal getR92_COLUMN_I() {
		return R92_COLUMN_I;
	}

	public void setR92_COLUMN_I(BigDecimal R92_COLUMN_I) {
		this.R92_COLUMN_I = R92_COLUMN_I;
	}

	public BigDecimal getR92_COLUMN_J() {
		return R92_COLUMN_J;
	}

	public void setR92_COLUMN_J(BigDecimal R92_COLUMN_J) {
		this.R92_COLUMN_J = R92_COLUMN_J;
	}

	public BigDecimal getR92_COLUMN_K() {
		return R92_COLUMN_K;
	}

	public void setR92_COLUMN_K(BigDecimal R92_COLUMN_K) {
		this.R92_COLUMN_K = R92_COLUMN_K;
	}

	public BigDecimal getR92_COLUMN_L() {
		return R92_COLUMN_L;
	}

	public void setR92_COLUMN_L(BigDecimal R92_COLUMN_L) {
		this.R92_COLUMN_L = R92_COLUMN_L;
	}

	public BigDecimal getR92_COLUMN_M() {
		return R92_COLUMN_M;
	}

	public void setR92_COLUMN_M(BigDecimal R92_COLUMN_M) {
		this.R92_COLUMN_M = R92_COLUMN_M;
	}

	public BigDecimal getR92_COLUMN_N() {
		return R92_COLUMN_N;
	}

	public void setR92_COLUMN_N(BigDecimal R92_COLUMN_N) {
		this.R92_COLUMN_N = R92_COLUMN_N;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public BigDecimal getREPORT_VERSION() {
		return REPORT_VERSION;
	}

	public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
		this.REPORT_VERSION = REPORT_VERSION;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
		this.REPORT_FREQUENCY = REPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String REPORT_CODE) {
		this.REPORT_CODE = REPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String REPORT_DESC) {
		this.REPORT_DESC = REPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public SLS_WORKING_Summary_Entity2() {
		super();
	}
	}

	public static class SLS_WORKING_Archival_Summary_Entity1 {

	private String R1_COLUMN_A;
	private BigDecimal R1_COLUMN_B;
	private BigDecimal R1_COLUMN_C;
	private BigDecimal R1_COLUMN_D;
	private BigDecimal R1_COLUMN_E;
	private BigDecimal R1_COLUMN_F;
	private BigDecimal R1_COLUMN_G;
	private BigDecimal R1_COLUMN_H;
	private BigDecimal R1_COLUMN_I;
	private BigDecimal R1_COLUMN_J;
	private BigDecimal R1_COLUMN_K;
	private BigDecimal R1_COLUMN_L;
	private BigDecimal R1_COLUMN_M;
	private BigDecimal R1_COLUMN_N;
	private String R2_COLUMN_A;
	private BigDecimal R2_COLUMN_B;
	private BigDecimal R2_COLUMN_C;
	private BigDecimal R2_COLUMN_D;
	private BigDecimal R2_COLUMN_E;
	private BigDecimal R2_COLUMN_F;
	private BigDecimal R2_COLUMN_G;
	private BigDecimal R2_COLUMN_H;
	private BigDecimal R2_COLUMN_I;
	private BigDecimal R2_COLUMN_J;
	private BigDecimal R2_COLUMN_K;
	private BigDecimal R2_COLUMN_L;
	private BigDecimal R2_COLUMN_M;
	private BigDecimal R2_COLUMN_N;
	private String R3_COLUMN_A;
	private BigDecimal R3_COLUMN_B;
	private BigDecimal R3_COLUMN_C;
	private BigDecimal R3_COLUMN_D;
	private BigDecimal R3_COLUMN_E;
	private BigDecimal R3_COLUMN_F;
	private BigDecimal R3_COLUMN_G;
	private BigDecimal R3_COLUMN_H;
	private BigDecimal R3_COLUMN_I;
	private BigDecimal R3_COLUMN_J;
	private BigDecimal R3_COLUMN_K;
	private BigDecimal R3_COLUMN_L;
	private BigDecimal R3_COLUMN_M;
	private BigDecimal R3_COLUMN_N;
	private String R4_COLUMN_A;
	private BigDecimal R4_COLUMN_B;
	private BigDecimal R4_COLUMN_C;
	private BigDecimal R4_COLUMN_D;
	private BigDecimal R4_COLUMN_E;
	private BigDecimal R4_COLUMN_F;
	private BigDecimal R4_COLUMN_G;
	private BigDecimal R4_COLUMN_H;
	private BigDecimal R4_COLUMN_I;
	private BigDecimal R4_COLUMN_J;
	private BigDecimal R4_COLUMN_K;
	private BigDecimal R4_COLUMN_L;
	private BigDecimal R4_COLUMN_M;
	private BigDecimal R4_COLUMN_N;
	private String R5_COLUMN_A;
	private BigDecimal R5_COLUMN_B;
	private BigDecimal R5_COLUMN_C;
	private BigDecimal R5_COLUMN_D;
	private BigDecimal R5_COLUMN_E;
	private BigDecimal R5_COLUMN_F;
	private BigDecimal R5_COLUMN_G;
	private BigDecimal R5_COLUMN_H;
	private BigDecimal R5_COLUMN_I;
	private BigDecimal R5_COLUMN_J;
	private BigDecimal R5_COLUMN_K;
	private BigDecimal R5_COLUMN_L;
	private BigDecimal R5_COLUMN_M;
	private BigDecimal R5_COLUMN_N;
	private String R6_COLUMN_A;
	private BigDecimal R6_COLUMN_B;
	private BigDecimal R6_COLUMN_C;
	private BigDecimal R6_COLUMN_D;
	private BigDecimal R6_COLUMN_E;
	private BigDecimal R6_COLUMN_F;
	private BigDecimal R6_COLUMN_G;
	private BigDecimal R6_COLUMN_H;
	private BigDecimal R6_COLUMN_I;
	private BigDecimal R6_COLUMN_J;
	private BigDecimal R6_COLUMN_K;
	private BigDecimal R6_COLUMN_L;
	private BigDecimal R6_COLUMN_M;
	private BigDecimal R6_COLUMN_N;
	private String R7_COLUMN_A;
	private BigDecimal R7_COLUMN_B;
	private BigDecimal R7_COLUMN_C;
	private BigDecimal R7_COLUMN_D;
	private BigDecimal R7_COLUMN_E;
	private BigDecimal R7_COLUMN_F;
	private BigDecimal R7_COLUMN_G;
	private BigDecimal R7_COLUMN_H;
	private BigDecimal R7_COLUMN_I;
	private BigDecimal R7_COLUMN_J;
	private BigDecimal R7_COLUMN_K;
	private BigDecimal R7_COLUMN_L;
	private BigDecimal R7_COLUMN_M;
	private BigDecimal R7_COLUMN_N;
	private String R8_COLUMN_A;
	private BigDecimal R8_COLUMN_B;
	private BigDecimal R8_COLUMN_C;
	private BigDecimal R8_COLUMN_D;
	private BigDecimal R8_COLUMN_E;
	private BigDecimal R8_COLUMN_F;
	private BigDecimal R8_COLUMN_G;
	private BigDecimal R8_COLUMN_H;
	private BigDecimal R8_COLUMN_I;
	private BigDecimal R8_COLUMN_J;
	private BigDecimal R8_COLUMN_K;
	private BigDecimal R8_COLUMN_L;
	private BigDecimal R8_COLUMN_M;
	private BigDecimal R8_COLUMN_N;
	private String R9_COLUMN_A;
	private BigDecimal R9_COLUMN_B;
	private BigDecimal R9_COLUMN_C;
	private BigDecimal R9_COLUMN_D;
	private BigDecimal R9_COLUMN_E;
	private BigDecimal R9_COLUMN_F;
	private BigDecimal R9_COLUMN_G;
	private BigDecimal R9_COLUMN_H;
	private BigDecimal R9_COLUMN_I;
	private BigDecimal R9_COLUMN_J;
	private BigDecimal R9_COLUMN_K;
	private BigDecimal R9_COLUMN_L;
	private BigDecimal R9_COLUMN_M;
	private BigDecimal R9_COLUMN_N;
	private String R10_COLUMN_A;
	private BigDecimal R10_COLUMN_B;
	private BigDecimal R10_COLUMN_C;
	private BigDecimal R10_COLUMN_D;
	private BigDecimal R10_COLUMN_E;
	private BigDecimal R10_COLUMN_F;
	private BigDecimal R10_COLUMN_G;
	private BigDecimal R10_COLUMN_H;
	private BigDecimal R10_COLUMN_I;
	private BigDecimal R10_COLUMN_J;
	private BigDecimal R10_COLUMN_K;
	private BigDecimal R10_COLUMN_L;
	private BigDecimal R10_COLUMN_M;
	private BigDecimal R10_COLUMN_N;
	private String R11_COLUMN_A;
	private BigDecimal R11_COLUMN_B;
	private BigDecimal R11_COLUMN_C;
	private BigDecimal R11_COLUMN_D;
	private BigDecimal R11_COLUMN_E;
	private BigDecimal R11_COLUMN_F;
	private BigDecimal R11_COLUMN_G;
	private BigDecimal R11_COLUMN_H;
	private BigDecimal R11_COLUMN_I;
	private BigDecimal R11_COLUMN_J;
	private BigDecimal R11_COLUMN_K;
	private BigDecimal R11_COLUMN_L;
	private BigDecimal R11_COLUMN_M;
	private BigDecimal R11_COLUMN_N;
	private String R12_COLUMN_A;
	private BigDecimal R12_COLUMN_B;
	private BigDecimal R12_COLUMN_C;
	private BigDecimal R12_COLUMN_D;
	private BigDecimal R12_COLUMN_E;
	private BigDecimal R12_COLUMN_F;
	private BigDecimal R12_COLUMN_G;
	private BigDecimal R12_COLUMN_H;
	private BigDecimal R12_COLUMN_I;
	private BigDecimal R12_COLUMN_J;
	private BigDecimal R12_COLUMN_K;
	private BigDecimal R12_COLUMN_L;
	private BigDecimal R12_COLUMN_M;
	private BigDecimal R12_COLUMN_N;
	private String R13_COLUMN_A;
	private BigDecimal R13_COLUMN_B;
	private BigDecimal R13_COLUMN_C;
	private BigDecimal R13_COLUMN_D;
	private BigDecimal R13_COLUMN_E;
	private BigDecimal R13_COLUMN_F;
	private BigDecimal R13_COLUMN_G;
	private BigDecimal R13_COLUMN_H;
	private BigDecimal R13_COLUMN_I;
	private BigDecimal R13_COLUMN_J;
	private BigDecimal R13_COLUMN_K;
	private BigDecimal R13_COLUMN_L;
	private BigDecimal R13_COLUMN_M;
	private BigDecimal R13_COLUMN_N;
	private String R14_COLUMN_A;
	private BigDecimal R14_COLUMN_B;
	private BigDecimal R14_COLUMN_C;
	private BigDecimal R14_COLUMN_D;
	private BigDecimal R14_COLUMN_E;
	private BigDecimal R14_COLUMN_F;
	private BigDecimal R14_COLUMN_G;
	private BigDecimal R14_COLUMN_H;
	private BigDecimal R14_COLUMN_I;
	private BigDecimal R14_COLUMN_J;
	private BigDecimal R14_COLUMN_K;
	private BigDecimal R14_COLUMN_L;
	private BigDecimal R14_COLUMN_M;
	private BigDecimal R14_COLUMN_N;
	private String R15_COLUMN_A;
	private BigDecimal R15_COLUMN_B;
	private BigDecimal R15_COLUMN_C;
	private BigDecimal R15_COLUMN_D;
	private BigDecimal R15_COLUMN_E;
	private BigDecimal R15_COLUMN_F;
	private BigDecimal R15_COLUMN_G;
	private BigDecimal R15_COLUMN_H;
	private BigDecimal R15_COLUMN_I;
	private BigDecimal R15_COLUMN_J;
	private BigDecimal R15_COLUMN_K;
	private BigDecimal R15_COLUMN_L;
	private BigDecimal R15_COLUMN_M;
	private BigDecimal R15_COLUMN_N;
	private String R16_COLUMN_A;
	private BigDecimal R16_COLUMN_B;
	private BigDecimal R16_COLUMN_C;
	private BigDecimal R16_COLUMN_D;
	private BigDecimal R16_COLUMN_E;
	private BigDecimal R16_COLUMN_F;
	private BigDecimal R16_COLUMN_G;
	private BigDecimal R16_COLUMN_H;
	private BigDecimal R16_COLUMN_I;
	private BigDecimal R16_COLUMN_J;
	private BigDecimal R16_COLUMN_K;
	private BigDecimal R16_COLUMN_L;
	private BigDecimal R16_COLUMN_M;
	private BigDecimal R16_COLUMN_N;
	private String R17_COLUMN_A;
	private BigDecimal R17_COLUMN_B;
	private BigDecimal R17_COLUMN_C;
	private BigDecimal R17_COLUMN_D;
	private BigDecimal R17_COLUMN_E;
	private BigDecimal R17_COLUMN_F;
	private BigDecimal R17_COLUMN_G;
	private BigDecimal R17_COLUMN_H;
	private BigDecimal R17_COLUMN_I;
	private BigDecimal R17_COLUMN_J;
	private BigDecimal R17_COLUMN_K;
	private BigDecimal R17_COLUMN_L;
	private BigDecimal R17_COLUMN_M;
	private BigDecimal R17_COLUMN_N;
	private String R18_COLUMN_A;
	private BigDecimal R18_COLUMN_B;
	private BigDecimal R18_COLUMN_C;
	private BigDecimal R18_COLUMN_D;
	private BigDecimal R18_COLUMN_E;
	private BigDecimal R18_COLUMN_F;
	private BigDecimal R18_COLUMN_G;
	private BigDecimal R18_COLUMN_H;
	private BigDecimal R18_COLUMN_I;
	private BigDecimal R18_COLUMN_J;
	private BigDecimal R18_COLUMN_K;
	private BigDecimal R18_COLUMN_L;
	private BigDecimal R18_COLUMN_M;
	private BigDecimal R18_COLUMN_N;
	private String R19_COLUMN_A;
	private BigDecimal R19_COLUMN_B;
	private BigDecimal R19_COLUMN_C;
	private BigDecimal R19_COLUMN_D;
	private BigDecimal R19_COLUMN_E;
	private BigDecimal R19_COLUMN_F;
	private BigDecimal R19_COLUMN_G;
	private BigDecimal R19_COLUMN_H;
	private BigDecimal R19_COLUMN_I;
	private BigDecimal R19_COLUMN_J;
	private BigDecimal R19_COLUMN_K;
	private BigDecimal R19_COLUMN_L;
	private BigDecimal R19_COLUMN_M;
	private BigDecimal R19_COLUMN_N;
	private String R20_COLUMN_A;
	private BigDecimal R20_COLUMN_B;
	private BigDecimal R20_COLUMN_C;
	private BigDecimal R20_COLUMN_D;
	private BigDecimal R20_COLUMN_E;
	private BigDecimal R20_COLUMN_F;
	private BigDecimal R20_COLUMN_G;
	private BigDecimal R20_COLUMN_H;
	private BigDecimal R20_COLUMN_I;
	private BigDecimal R20_COLUMN_J;
	private BigDecimal R20_COLUMN_K;
	private BigDecimal R20_COLUMN_L;
	private BigDecimal R20_COLUMN_M;
	private BigDecimal R20_COLUMN_N;
	private String R21_COLUMN_A;
	private BigDecimal R21_COLUMN_B;
	private BigDecimal R21_COLUMN_C;
	private BigDecimal R21_COLUMN_D;
	private BigDecimal R21_COLUMN_E;
	private BigDecimal R21_COLUMN_F;
	private BigDecimal R21_COLUMN_G;
	private BigDecimal R21_COLUMN_H;
	private BigDecimal R21_COLUMN_I;
	private BigDecimal R21_COLUMN_J;
	private BigDecimal R21_COLUMN_K;
	private BigDecimal R21_COLUMN_L;
	private BigDecimal R21_COLUMN_M;
	private BigDecimal R21_COLUMN_N;
	private String R22_COLUMN_A;
	private BigDecimal R22_COLUMN_B;
	private BigDecimal R22_COLUMN_C;
	private BigDecimal R22_COLUMN_D;
	private BigDecimal R22_COLUMN_E;
	private BigDecimal R22_COLUMN_F;
	private BigDecimal R22_COLUMN_G;
	private BigDecimal R22_COLUMN_H;
	private BigDecimal R22_COLUMN_I;
	private BigDecimal R22_COLUMN_J;
	private BigDecimal R22_COLUMN_K;
	private BigDecimal R22_COLUMN_L;
	private BigDecimal R22_COLUMN_M;
	private BigDecimal R22_COLUMN_N;
	private String R23_COLUMN_A;
	private BigDecimal R23_COLUMN_B;
	private BigDecimal R23_COLUMN_C;
	private BigDecimal R23_COLUMN_D;
	private BigDecimal R23_COLUMN_E;
	private BigDecimal R23_COLUMN_F;
	private BigDecimal R23_COLUMN_G;
	private BigDecimal R23_COLUMN_H;
	private BigDecimal R23_COLUMN_I;
	private BigDecimal R23_COLUMN_J;
	private BigDecimal R23_COLUMN_K;
	private BigDecimal R23_COLUMN_L;
	private BigDecimal R23_COLUMN_M;
	private BigDecimal R23_COLUMN_N;
	private String R24_COLUMN_A;
	private BigDecimal R24_COLUMN_B;
	private BigDecimal R24_COLUMN_C;
	private BigDecimal R24_COLUMN_D;
	private BigDecimal R24_COLUMN_E;
	private BigDecimal R24_COLUMN_F;
	private BigDecimal R24_COLUMN_G;
	private BigDecimal R24_COLUMN_H;
	private BigDecimal R24_COLUMN_I;
	private BigDecimal R24_COLUMN_J;
	private BigDecimal R24_COLUMN_K;
	private BigDecimal R24_COLUMN_L;
	private BigDecimal R24_COLUMN_M;
	private BigDecimal R24_COLUMN_N;
	private String R25_COLUMN_A;
	private BigDecimal R25_COLUMN_B;
	private BigDecimal R25_COLUMN_C;
	private BigDecimal R25_COLUMN_D;
	private BigDecimal R25_COLUMN_E;
	private BigDecimal R25_COLUMN_F;
	private BigDecimal R25_COLUMN_G;
	private BigDecimal R25_COLUMN_H;
	private BigDecimal R25_COLUMN_I;
	private BigDecimal R25_COLUMN_J;
	private BigDecimal R25_COLUMN_K;
	private BigDecimal R25_COLUMN_L;
	private BigDecimal R25_COLUMN_M;
	private BigDecimal R25_COLUMN_N;
	private String R26_COLUMN_A;
	private BigDecimal R26_COLUMN_B;
	private BigDecimal R26_COLUMN_C;
	private BigDecimal R26_COLUMN_D;
	private BigDecimal R26_COLUMN_E;
	private BigDecimal R26_COLUMN_F;
	private BigDecimal R26_COLUMN_G;
	private BigDecimal R26_COLUMN_H;
	private BigDecimal R26_COLUMN_I;
	private BigDecimal R26_COLUMN_J;
	private BigDecimal R26_COLUMN_K;
	private BigDecimal R26_COLUMN_L;
	private BigDecimal R26_COLUMN_M;
	private BigDecimal R26_COLUMN_N;
	private String R27_COLUMN_A;
	private BigDecimal R27_COLUMN_B;
	private BigDecimal R27_COLUMN_C;
	private BigDecimal R27_COLUMN_D;
	private BigDecimal R27_COLUMN_E;
	private BigDecimal R27_COLUMN_F;
	private BigDecimal R27_COLUMN_G;
	private BigDecimal R27_COLUMN_H;
	private BigDecimal R27_COLUMN_I;
	private BigDecimal R27_COLUMN_J;
	private BigDecimal R27_COLUMN_K;
	private BigDecimal R27_COLUMN_L;
	private BigDecimal R27_COLUMN_M;
	private BigDecimal R27_COLUMN_N;
	private String R28_COLUMN_A;
	private BigDecimal R28_COLUMN_B;
	private BigDecimal R28_COLUMN_C;
	private BigDecimal R28_COLUMN_D;
	private BigDecimal R28_COLUMN_E;
	private BigDecimal R28_COLUMN_F;
	private BigDecimal R28_COLUMN_G;
	private BigDecimal R28_COLUMN_H;
	private BigDecimal R28_COLUMN_I;
	private BigDecimal R28_COLUMN_J;
	private BigDecimal R28_COLUMN_K;
	private BigDecimal R28_COLUMN_L;
	private BigDecimal R28_COLUMN_M;
	private BigDecimal R28_COLUMN_N;
	private String R29_COLUMN_A;
	private BigDecimal R29_COLUMN_B;
	private BigDecimal R29_COLUMN_C;
	private BigDecimal R29_COLUMN_D;
	private BigDecimal R29_COLUMN_E;
	private BigDecimal R29_COLUMN_F;
	private BigDecimal R29_COLUMN_G;
	private BigDecimal R29_COLUMN_H;
	private BigDecimal R29_COLUMN_I;
	private BigDecimal R29_COLUMN_J;
	private BigDecimal R29_COLUMN_K;
	private BigDecimal R29_COLUMN_L;
	private BigDecimal R29_COLUMN_M;
	private BigDecimal R29_COLUMN_N;
	private String R30_COLUMN_A;
	private BigDecimal R30_COLUMN_B;
	private BigDecimal R30_COLUMN_C;
	private BigDecimal R30_COLUMN_D;
	private BigDecimal R30_COLUMN_E;
	private BigDecimal R30_COLUMN_F;
	private BigDecimal R30_COLUMN_G;
	private BigDecimal R30_COLUMN_H;
	private BigDecimal R30_COLUMN_I;
	private BigDecimal R30_COLUMN_J;
	private BigDecimal R30_COLUMN_K;
	private BigDecimal R30_COLUMN_L;
	private BigDecimal R30_COLUMN_M;
	private BigDecimal R30_COLUMN_N;
	private String R31_COLUMN_A;
	private BigDecimal R31_COLUMN_B;
	private BigDecimal R31_COLUMN_C;
	private BigDecimal R31_COLUMN_D;
	private BigDecimal R31_COLUMN_E;
	private BigDecimal R31_COLUMN_F;
	private BigDecimal R31_COLUMN_G;
	private BigDecimal R31_COLUMN_H;
	private BigDecimal R31_COLUMN_I;
	private BigDecimal R31_COLUMN_J;
	private BigDecimal R31_COLUMN_K;
	private BigDecimal R31_COLUMN_L;
	private BigDecimal R31_COLUMN_M;
	private BigDecimal R31_COLUMN_N;
	private String R32_COLUMN_A;
	private BigDecimal R32_COLUMN_B;
	private BigDecimal R32_COLUMN_C;
	private BigDecimal R32_COLUMN_D;
	private BigDecimal R32_COLUMN_E;
	private BigDecimal R32_COLUMN_F;
	private BigDecimal R32_COLUMN_G;
	private BigDecimal R32_COLUMN_H;
	private BigDecimal R32_COLUMN_I;
	private BigDecimal R32_COLUMN_J;
	private BigDecimal R32_COLUMN_K;
	private BigDecimal R32_COLUMN_L;
	private BigDecimal R32_COLUMN_M;
	private BigDecimal R32_COLUMN_N;
	private String R33_COLUMN_A;
	private BigDecimal R33_COLUMN_B;
	private BigDecimal R33_COLUMN_C;
	private BigDecimal R33_COLUMN_D;
	private BigDecimal R33_COLUMN_E;
	private BigDecimal R33_COLUMN_F;
	private BigDecimal R33_COLUMN_G;
	private BigDecimal R33_COLUMN_H;
	private BigDecimal R33_COLUMN_I;
	private BigDecimal R33_COLUMN_J;
	private BigDecimal R33_COLUMN_K;
	private BigDecimal R33_COLUMN_L;
	private BigDecimal R33_COLUMN_M;
	private BigDecimal R33_COLUMN_N;
	private String R34_COLUMN_A;
	private BigDecimal R34_COLUMN_B;
	private BigDecimal R34_COLUMN_C;
	private BigDecimal R34_COLUMN_D;
	private BigDecimal R34_COLUMN_E;
	private BigDecimal R34_COLUMN_F;
	private BigDecimal R34_COLUMN_G;
	private BigDecimal R34_COLUMN_H;
	private BigDecimal R34_COLUMN_I;
	private BigDecimal R34_COLUMN_J;
	private BigDecimal R34_COLUMN_K;
	private BigDecimal R34_COLUMN_L;
	private BigDecimal R34_COLUMN_M;
	private BigDecimal R34_COLUMN_N;
	private String R35_COLUMN_A;
	private BigDecimal R35_COLUMN_B;
	private BigDecimal R35_COLUMN_C;
	private BigDecimal R35_COLUMN_D;
	private BigDecimal R35_COLUMN_E;
	private BigDecimal R35_COLUMN_F;
	private BigDecimal R35_COLUMN_G;
	private BigDecimal R35_COLUMN_H;
	private BigDecimal R35_COLUMN_I;
	private BigDecimal R35_COLUMN_J;
	private BigDecimal R35_COLUMN_K;
	private BigDecimal R35_COLUMN_L;
	private BigDecimal R35_COLUMN_M;
	private BigDecimal R35_COLUMN_N;
	private String R36_COLUMN_A;
	private BigDecimal R36_COLUMN_B;
	private BigDecimal R36_COLUMN_C;
	private BigDecimal R36_COLUMN_D;
	private BigDecimal R36_COLUMN_E;
	private BigDecimal R36_COLUMN_F;
	private BigDecimal R36_COLUMN_G;
	private BigDecimal R36_COLUMN_H;
	private BigDecimal R36_COLUMN_I;
	private BigDecimal R36_COLUMN_J;
	private BigDecimal R36_COLUMN_K;
	private BigDecimal R36_COLUMN_L;
	private BigDecimal R36_COLUMN_M;
	private BigDecimal R36_COLUMN_N;
	private String R37_COLUMN_A;
	private BigDecimal R37_COLUMN_B;
	private BigDecimal R37_COLUMN_C;
	private BigDecimal R37_COLUMN_D;
	private BigDecimal R37_COLUMN_E;
	private BigDecimal R37_COLUMN_F;
	private BigDecimal R37_COLUMN_G;
	private BigDecimal R37_COLUMN_H;
	private BigDecimal R37_COLUMN_I;
	private BigDecimal R37_COLUMN_J;
	private BigDecimal R37_COLUMN_K;
	private BigDecimal R37_COLUMN_L;
	private BigDecimal R37_COLUMN_M;
	private BigDecimal R37_COLUMN_N;
	private String R38_COLUMN_A;
	private BigDecimal R38_COLUMN_B;
	private BigDecimal R38_COLUMN_C;
	private BigDecimal R38_COLUMN_D;
	private BigDecimal R38_COLUMN_E;
	private BigDecimal R38_COLUMN_F;
	private BigDecimal R38_COLUMN_G;
	private BigDecimal R38_COLUMN_H;
	private BigDecimal R38_COLUMN_I;
	private BigDecimal R38_COLUMN_J;
	private BigDecimal R38_COLUMN_K;
	private BigDecimal R38_COLUMN_L;
	private BigDecimal R38_COLUMN_M;
	private BigDecimal R38_COLUMN_N;
	private String R39_COLUMN_A;
	private BigDecimal R39_COLUMN_B;
	private BigDecimal R39_COLUMN_C;
	private BigDecimal R39_COLUMN_D;
	private BigDecimal R39_COLUMN_E;
	private BigDecimal R39_COLUMN_F;
	private BigDecimal R39_COLUMN_G;
	private BigDecimal R39_COLUMN_H;
	private BigDecimal R39_COLUMN_I;
	private BigDecimal R39_COLUMN_J;
	private BigDecimal R39_COLUMN_K;
	private BigDecimal R39_COLUMN_L;
	private BigDecimal R39_COLUMN_M;
	private BigDecimal R39_COLUMN_N;
	private String R40_COLUMN_A;
	private BigDecimal R40_COLUMN_B;
	private BigDecimal R40_COLUMN_C;
	private BigDecimal R40_COLUMN_D;
	private BigDecimal R40_COLUMN_E;
	private BigDecimal R40_COLUMN_F;
	private BigDecimal R40_COLUMN_G;
	private BigDecimal R40_COLUMN_H;
	private BigDecimal R40_COLUMN_I;
	private BigDecimal R40_COLUMN_J;
	private BigDecimal R40_COLUMN_K;
	private BigDecimal R40_COLUMN_L;
	private BigDecimal R40_COLUMN_M;
	private BigDecimal R40_COLUMN_N;
	private String R41_COLUMN_A;
	private BigDecimal R41_COLUMN_B;
	private BigDecimal R41_COLUMN_C;
	private BigDecimal R41_COLUMN_D;
	private BigDecimal R41_COLUMN_E;
	private BigDecimal R41_COLUMN_F;
	private BigDecimal R41_COLUMN_G;
	private BigDecimal R41_COLUMN_H;
	private BigDecimal R41_COLUMN_I;
	private BigDecimal R41_COLUMN_J;
	private BigDecimal R41_COLUMN_K;
	private BigDecimal R41_COLUMN_L;
	private BigDecimal R41_COLUMN_M;
	private BigDecimal R41_COLUMN_N;
	private String R42_COLUMN_A;
	private BigDecimal R42_COLUMN_B;
	private BigDecimal R42_COLUMN_C;
	private BigDecimal R42_COLUMN_D;
	private BigDecimal R42_COLUMN_E;
	private BigDecimal R42_COLUMN_F;
	private BigDecimal R42_COLUMN_G;
	private BigDecimal R42_COLUMN_H;
	private BigDecimal R42_COLUMN_I;
	private BigDecimal R42_COLUMN_J;
	private BigDecimal R42_COLUMN_K;
	private BigDecimal R42_COLUMN_L;
	private BigDecimal R42_COLUMN_M;
	private BigDecimal R42_COLUMN_N;
	private String R43_COLUMN_A;
	private BigDecimal R43_COLUMN_B;
	private BigDecimal R43_COLUMN_C;
	private BigDecimal R43_COLUMN_D;
	private BigDecimal R43_COLUMN_E;
	private BigDecimal R43_COLUMN_F;
	private BigDecimal R43_COLUMN_G;
	private BigDecimal R43_COLUMN_H;
	private BigDecimal R43_COLUMN_I;
	private BigDecimal R43_COLUMN_J;
	private BigDecimal R43_COLUMN_K;
	private BigDecimal R43_COLUMN_L;
	private BigDecimal R43_COLUMN_M;
	private BigDecimal R43_COLUMN_N;
	private String R44_COLUMN_A;
	private BigDecimal R44_COLUMN_B;
	private BigDecimal R44_COLUMN_C;
	private BigDecimal R44_COLUMN_D;
	private BigDecimal R44_COLUMN_E;
	private BigDecimal R44_COLUMN_F;
	private BigDecimal R44_COLUMN_G;
	private BigDecimal R44_COLUMN_H;
	private BigDecimal R44_COLUMN_I;
	private BigDecimal R44_COLUMN_J;
	private BigDecimal R44_COLUMN_K;
	private BigDecimal R44_COLUMN_L;
	private BigDecimal R44_COLUMN_M;
	private BigDecimal R44_COLUMN_N;
	private String R45_COLUMN_A;
	private BigDecimal R45_COLUMN_B;
	private BigDecimal R45_COLUMN_C;
	private BigDecimal R45_COLUMN_D;
	private BigDecimal R45_COLUMN_E;
	private BigDecimal R45_COLUMN_F;
	private BigDecimal R45_COLUMN_G;
	private BigDecimal R45_COLUMN_H;
	private BigDecimal R45_COLUMN_I;
	private BigDecimal R45_COLUMN_J;
	private BigDecimal R45_COLUMN_K;
	private BigDecimal R45_COLUMN_L;
	private BigDecimal R45_COLUMN_M;
	private BigDecimal R45_COLUMN_N;
	private String R46_COLUMN_A;
	private BigDecimal R46_COLUMN_B;
	private BigDecimal R46_COLUMN_C;
	private BigDecimal R46_COLUMN_D;
	private BigDecimal R46_COLUMN_E;
	private BigDecimal R46_COLUMN_F;
	private BigDecimal R46_COLUMN_G;
	private BigDecimal R46_COLUMN_H;
	private BigDecimal R46_COLUMN_I;
	private BigDecimal R46_COLUMN_J;
	private BigDecimal R46_COLUMN_K;
	private BigDecimal R46_COLUMN_L;
	private BigDecimal R46_COLUMN_M;
	private BigDecimal R46_COLUMN_N;
	private String R47_COLUMN_A;
	private BigDecimal R47_COLUMN_B;
	private BigDecimal R47_COLUMN_C;
	private BigDecimal R47_COLUMN_D;
	private BigDecimal R47_COLUMN_E;
	private BigDecimal R47_COLUMN_F;
	private BigDecimal R47_COLUMN_G;
	private BigDecimal R47_COLUMN_H;
	private BigDecimal R47_COLUMN_I;
	private BigDecimal R47_COLUMN_J;
	private BigDecimal R47_COLUMN_K;
	private BigDecimal R47_COLUMN_L;
	private BigDecimal R47_COLUMN_M;
	private BigDecimal R47_COLUMN_N;
	private String R48_COLUMN_A;
	private BigDecimal R48_COLUMN_B;
	private BigDecimal R48_COLUMN_C;
	private BigDecimal R48_COLUMN_D;
	private BigDecimal R48_COLUMN_E;
	private BigDecimal R48_COLUMN_F;
	private BigDecimal R48_COLUMN_G;
	private BigDecimal R48_COLUMN_H;
	private BigDecimal R48_COLUMN_I;
	private BigDecimal R48_COLUMN_J;
	private BigDecimal R48_COLUMN_K;
	private BigDecimal R48_COLUMN_L;
	private BigDecimal R48_COLUMN_M;
	private BigDecimal R48_COLUMN_N;
	private String R49_COLUMN_A;
	private BigDecimal R49_COLUMN_B;
	private BigDecimal R49_COLUMN_C;
	private BigDecimal R49_COLUMN_D;
	private BigDecimal R49_COLUMN_E;
	private BigDecimal R49_COLUMN_F;
	private BigDecimal R49_COLUMN_G;
	private BigDecimal R49_COLUMN_H;
	private BigDecimal R49_COLUMN_I;
	private BigDecimal R49_COLUMN_J;
	private BigDecimal R49_COLUMN_K;
	private BigDecimal R49_COLUMN_L;
	private BigDecimal R49_COLUMN_M;
	private BigDecimal R49_COLUMN_N;
	private String R50_COLUMN_A;
	private BigDecimal R50_COLUMN_B;
	private BigDecimal R50_COLUMN_C;
	private BigDecimal R50_COLUMN_D;
	private BigDecimal R50_COLUMN_E;
	private BigDecimal R50_COLUMN_F;
	private BigDecimal R50_COLUMN_G;
	private BigDecimal R50_COLUMN_H;
	private BigDecimal R50_COLUMN_I;
	private BigDecimal R50_COLUMN_J;
	private BigDecimal R50_COLUMN_K;
	private BigDecimal R50_COLUMN_L;
	private BigDecimal R50_COLUMN_M;
	private BigDecimal R50_COLUMN_N;
	private String R51_COLUMN_A;
	private BigDecimal R51_COLUMN_B;
	private BigDecimal R51_COLUMN_C;
	private BigDecimal R51_COLUMN_D;
	private BigDecimal R51_COLUMN_E;
	private BigDecimal R51_COLUMN_F;
	private BigDecimal R51_COLUMN_G;
	private BigDecimal R51_COLUMN_H;
	private BigDecimal R51_COLUMN_I;
	private BigDecimal R51_COLUMN_J;
	private BigDecimal R51_COLUMN_K;
	private BigDecimal R51_COLUMN_L;
	private BigDecimal R51_COLUMN_M;
	private BigDecimal R51_COLUMN_N;
	private String R52_COLUMN_A;
	private BigDecimal R52_COLUMN_B;
	private BigDecimal R52_COLUMN_C;
	private BigDecimal R52_COLUMN_D;
	private BigDecimal R52_COLUMN_E;
	private BigDecimal R52_COLUMN_F;
	private BigDecimal R52_COLUMN_G;
	private BigDecimal R52_COLUMN_H;
	private BigDecimal R52_COLUMN_I;
	private BigDecimal R52_COLUMN_J;
	private BigDecimal R52_COLUMN_K;
	private BigDecimal R52_COLUMN_L;
	private BigDecimal R52_COLUMN_M;
	private BigDecimal R52_COLUMN_N;
	private String R53_COLUMN_A;
	private BigDecimal R53_COLUMN_B;
	private BigDecimal R53_COLUMN_C;
	private BigDecimal R53_COLUMN_D;
	private BigDecimal R53_COLUMN_E;
	private BigDecimal R53_COLUMN_F;
	private BigDecimal R53_COLUMN_G;
	private BigDecimal R53_COLUMN_H;
	private BigDecimal R53_COLUMN_I;
	private BigDecimal R53_COLUMN_J;
	private BigDecimal R53_COLUMN_K;
	private BigDecimal R53_COLUMN_L;
	private BigDecimal R53_COLUMN_M;
	private BigDecimal R53_COLUMN_N;
	private String R54_COLUMN_A;
	private BigDecimal R54_COLUMN_B;
	private BigDecimal R54_COLUMN_C;
	private BigDecimal R54_COLUMN_D;
	private BigDecimal R54_COLUMN_E;
	private BigDecimal R54_COLUMN_F;
	private BigDecimal R54_COLUMN_G;
	private BigDecimal R54_COLUMN_H;
	private BigDecimal R54_COLUMN_I;
	private BigDecimal R54_COLUMN_J;
	private BigDecimal R54_COLUMN_K;
	private BigDecimal R54_COLUMN_L;
	private BigDecimal R54_COLUMN_M;
	private BigDecimal R54_COLUMN_N;
	private String R55_COLUMN_A;
	private BigDecimal R55_COLUMN_B;
	private BigDecimal R55_COLUMN_C;
	private BigDecimal R55_COLUMN_D;
	private BigDecimal R55_COLUMN_E;
	private BigDecimal R55_COLUMN_F;
	private BigDecimal R55_COLUMN_G;
	private BigDecimal R55_COLUMN_H;
	private BigDecimal R55_COLUMN_I;
	private BigDecimal R55_COLUMN_J;
	private BigDecimal R55_COLUMN_K;
	private BigDecimal R55_COLUMN_L;
	private BigDecimal R55_COLUMN_M;
	private BigDecimal R55_COLUMN_N;
	private String R56_COLUMN_A;
	private BigDecimal R56_COLUMN_B;
	private BigDecimal R56_COLUMN_C;
	private BigDecimal R56_COLUMN_D;
	private BigDecimal R56_COLUMN_E;
	private BigDecimal R56_COLUMN_F;
	private BigDecimal R56_COLUMN_G;
	private BigDecimal R56_COLUMN_H;
	private BigDecimal R56_COLUMN_I;
	private BigDecimal R56_COLUMN_J;
	private BigDecimal R56_COLUMN_K;
	private BigDecimal R56_COLUMN_L;
	private BigDecimal R56_COLUMN_M;
	private BigDecimal R56_COLUMN_N;
	private String R57_COLUMN_A;
	private BigDecimal R57_COLUMN_B;
	private BigDecimal R57_COLUMN_C;
	private BigDecimal R57_COLUMN_D;
	private BigDecimal R57_COLUMN_E;
	private BigDecimal R57_COLUMN_F;
	private BigDecimal R57_COLUMN_G;
	private BigDecimal R57_COLUMN_H;
	private BigDecimal R57_COLUMN_I;
	private BigDecimal R57_COLUMN_J;
	private BigDecimal R57_COLUMN_K;
	private BigDecimal R57_COLUMN_L;
	private BigDecimal R57_COLUMN_M;
	private BigDecimal R57_COLUMN_N;
	private String R58_COLUMN_A;
	private BigDecimal R58_COLUMN_B;
	private BigDecimal R58_COLUMN_C;
	private BigDecimal R58_COLUMN_D;
	private BigDecimal R58_COLUMN_E;
	private BigDecimal R58_COLUMN_F;
	private BigDecimal R58_COLUMN_G;
	private BigDecimal R58_COLUMN_H;
	private BigDecimal R58_COLUMN_I;
	private BigDecimal R58_COLUMN_J;
	private BigDecimal R58_COLUMN_K;
	private BigDecimal R58_COLUMN_L;
	private BigDecimal R58_COLUMN_M;
	private BigDecimal R58_COLUMN_N;
	private String R59_COLUMN_A;
	private BigDecimal R59_COLUMN_B;
	private BigDecimal R59_COLUMN_C;
	private BigDecimal R59_COLUMN_D;
	private BigDecimal R59_COLUMN_E;
	private BigDecimal R59_COLUMN_F;
	private BigDecimal R59_COLUMN_G;
	private BigDecimal R59_COLUMN_H;
	private BigDecimal R59_COLUMN_I;
	private BigDecimal R59_COLUMN_J;
	private BigDecimal R59_COLUMN_K;
	private BigDecimal R59_COLUMN_L;
	private BigDecimal R59_COLUMN_M;
	private BigDecimal R59_COLUMN_N;
	private String R60_COLUMN_A;
	private BigDecimal R60_COLUMN_B;
	private BigDecimal R60_COLUMN_C;
	private BigDecimal R60_COLUMN_D;
	private BigDecimal R60_COLUMN_E;
	private BigDecimal R60_COLUMN_F;
	private BigDecimal R60_COLUMN_G;
	private BigDecimal R60_COLUMN_H;
	private BigDecimal R60_COLUMN_I;
	private BigDecimal R60_COLUMN_J;
	private BigDecimal R60_COLUMN_K;
	private BigDecimal R60_COLUMN_L;
	private BigDecimal R60_COLUMN_M;
	private BigDecimal R60_COLUMN_N;
	private String R61_COLUMN_A;
	private BigDecimal R61_COLUMN_B;
	private BigDecimal R61_COLUMN_C;
	private BigDecimal R61_COLUMN_D;
	private BigDecimal R61_COLUMN_E;
	private BigDecimal R61_COLUMN_F;
	private BigDecimal R61_COLUMN_G;
	private BigDecimal R61_COLUMN_H;
	private BigDecimal R61_COLUMN_I;
	private BigDecimal R61_COLUMN_J;
	private BigDecimal R61_COLUMN_K;
	private BigDecimal R61_COLUMN_L;
	private BigDecimal R61_COLUMN_M;
	private BigDecimal R61_COLUMN_N;
	private String R62_COLUMN_A;
	private BigDecimal R62_COLUMN_B;
	private BigDecimal R62_COLUMN_C;
	private BigDecimal R62_COLUMN_D;
	private BigDecimal R62_COLUMN_E;
	private BigDecimal R62_COLUMN_F;
	private BigDecimal R62_COLUMN_G;
	private BigDecimal R62_COLUMN_H;
	private BigDecimal R62_COLUMN_I;
	private BigDecimal R62_COLUMN_J;
	private BigDecimal R62_COLUMN_K;
	private BigDecimal R62_COLUMN_L;
	private BigDecimal R62_COLUMN_M;
	private BigDecimal R62_COLUMN_N;
	private String R63_COLUMN_A;
	private BigDecimal R63_COLUMN_B;
	private BigDecimal R63_COLUMN_C;
	private BigDecimal R63_COLUMN_D;
	private BigDecimal R63_COLUMN_E;
	private BigDecimal R63_COLUMN_F;
	private BigDecimal R63_COLUMN_G;
	private BigDecimal R63_COLUMN_H;
	private BigDecimal R63_COLUMN_I;
	private BigDecimal R63_COLUMN_J;
	private BigDecimal R63_COLUMN_K;
	private BigDecimal R63_COLUMN_L;
	private BigDecimal R63_COLUMN_M;
	private BigDecimal R63_COLUMN_N;
	private String R64_COLUMN_A;
	private BigDecimal R64_COLUMN_B;
	private BigDecimal R64_COLUMN_C;
	private BigDecimal R64_COLUMN_D;
	private BigDecimal R64_COLUMN_E;
	private BigDecimal R64_COLUMN_F;
	private BigDecimal R64_COLUMN_G;
	private BigDecimal R64_COLUMN_H;
	private BigDecimal R64_COLUMN_I;
	private BigDecimal R64_COLUMN_J;
	private BigDecimal R64_COLUMN_K;
	private BigDecimal R64_COLUMN_L;
	private BigDecimal R64_COLUMN_M;
	private BigDecimal R64_COLUMN_N;
	private String R65_COLUMN_A;
	private BigDecimal R65_COLUMN_B;
	private BigDecimal R65_COLUMN_C;
	private BigDecimal R65_COLUMN_D;
	private BigDecimal R65_COLUMN_E;
	private BigDecimal R65_COLUMN_F;
	private BigDecimal R65_COLUMN_G;
	private BigDecimal R65_COLUMN_H;
	private BigDecimal R65_COLUMN_I;
	private BigDecimal R65_COLUMN_J;
	private BigDecimal R65_COLUMN_K;
	private BigDecimal R65_COLUMN_L;
	private BigDecimal R65_COLUMN_M;
	private BigDecimal R65_COLUMN_N;
	private String R66_COLUMN_A;
	private BigDecimal R66_COLUMN_B;
	private BigDecimal R66_COLUMN_C;
	private BigDecimal R66_COLUMN_D;
	private BigDecimal R66_COLUMN_E;
	private BigDecimal R66_COLUMN_F;
	private BigDecimal R66_COLUMN_G;
	private BigDecimal R66_COLUMN_H;
	private BigDecimal R66_COLUMN_I;
	private BigDecimal R66_COLUMN_J;
	private BigDecimal R66_COLUMN_K;
	private BigDecimal R66_COLUMN_L;
	private BigDecimal R66_COLUMN_M;
	private BigDecimal R66_COLUMN_N;
	private String R67_COLUMN_A;
	private BigDecimal R67_COLUMN_B;
	private BigDecimal R67_COLUMN_C;
	private BigDecimal R67_COLUMN_D;
	private BigDecimal R67_COLUMN_E;
	private BigDecimal R67_COLUMN_F;
	private BigDecimal R67_COLUMN_G;
	private BigDecimal R67_COLUMN_H;
	private BigDecimal R67_COLUMN_I;
	private BigDecimal R67_COLUMN_J;
	private BigDecimal R67_COLUMN_K;
	private BigDecimal R67_COLUMN_L;
	private BigDecimal R67_COLUMN_M;
	private BigDecimal R67_COLUMN_N;
	private String R68_COLUMN_A;
	private BigDecimal R68_COLUMN_B;
	private BigDecimal R68_COLUMN_C;
	private BigDecimal R68_COLUMN_D;
	private BigDecimal R68_COLUMN_E;
	private BigDecimal R68_COLUMN_F;
	private BigDecimal R68_COLUMN_G;
	private BigDecimal R68_COLUMN_H;
	private BigDecimal R68_COLUMN_I;
	private BigDecimal R68_COLUMN_J;
	private BigDecimal R68_COLUMN_K;
	private BigDecimal R68_COLUMN_L;
	private BigDecimal R68_COLUMN_M;
	private BigDecimal R68_COLUMN_N;
	private String R69_COLUMN_A;
	private BigDecimal R69_COLUMN_B;
	private BigDecimal R69_COLUMN_C;
	private BigDecimal R69_COLUMN_D;
	private BigDecimal R69_COLUMN_E;
	private BigDecimal R69_COLUMN_F;
	private BigDecimal R69_COLUMN_G;
	private BigDecimal R69_COLUMN_H;
	private BigDecimal R69_COLUMN_I;
	private BigDecimal R69_COLUMN_J;
	private BigDecimal R69_COLUMN_K;
	private BigDecimal R69_COLUMN_L;
	private BigDecimal R69_COLUMN_M;
	private BigDecimal R69_COLUMN_N;
	private String R70_COLUMN_A;
	private BigDecimal R70_COLUMN_B;
	private BigDecimal R70_COLUMN_C;
	private BigDecimal R70_COLUMN_D;
	private BigDecimal R70_COLUMN_E;
	private BigDecimal R70_COLUMN_F;
	private BigDecimal R70_COLUMN_G;
	private BigDecimal R70_COLUMN_H;
	private BigDecimal R70_COLUMN_I;
	private BigDecimal R70_COLUMN_J;
	private BigDecimal R70_COLUMN_K;
	private BigDecimal R70_COLUMN_L;
	private BigDecimal R70_COLUMN_M;
	private BigDecimal R70_COLUMN_N;
	private Date REPORT_DATE;
	private BigDecimal REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public String getR1_COLUMN_A() {
		return R1_COLUMN_A;
	}

	public void setR1_COLUMN_A(String R1_COLUMN_A) {
		this.R1_COLUMN_A = R1_COLUMN_A;
	}

	public BigDecimal getR1_COLUMN_B() {
		return R1_COLUMN_B;
	}

	public void setR1_COLUMN_B(BigDecimal R1_COLUMN_B) {
		this.R1_COLUMN_B = R1_COLUMN_B;
	}

	public BigDecimal getR1_COLUMN_C() {
		return R1_COLUMN_C;
	}

	public void setR1_COLUMN_C(BigDecimal R1_COLUMN_C) {
		this.R1_COLUMN_C = R1_COLUMN_C;
	}

	public BigDecimal getR1_COLUMN_D() {
		return R1_COLUMN_D;
	}

	public void setR1_COLUMN_D(BigDecimal R1_COLUMN_D) {
		this.R1_COLUMN_D = R1_COLUMN_D;
	}

	public BigDecimal getR1_COLUMN_E() {
		return R1_COLUMN_E;
	}

	public void setR1_COLUMN_E(BigDecimal R1_COLUMN_E) {
		this.R1_COLUMN_E = R1_COLUMN_E;
	}

	public BigDecimal getR1_COLUMN_F() {
		return R1_COLUMN_F;
	}

	public void setR1_COLUMN_F(BigDecimal R1_COLUMN_F) {
		this.R1_COLUMN_F = R1_COLUMN_F;
	}

	public BigDecimal getR1_COLUMN_G() {
		return R1_COLUMN_G;
	}

	public void setR1_COLUMN_G(BigDecimal R1_COLUMN_G) {
		this.R1_COLUMN_G = R1_COLUMN_G;
	}

	public BigDecimal getR1_COLUMN_H() {
		return R1_COLUMN_H;
	}

	public void setR1_COLUMN_H(BigDecimal R1_COLUMN_H) {
		this.R1_COLUMN_H = R1_COLUMN_H;
	}

	public BigDecimal getR1_COLUMN_I() {
		return R1_COLUMN_I;
	}

	public void setR1_COLUMN_I(BigDecimal R1_COLUMN_I) {
		this.R1_COLUMN_I = R1_COLUMN_I;
	}

	public BigDecimal getR1_COLUMN_J() {
		return R1_COLUMN_J;
	}

	public void setR1_COLUMN_J(BigDecimal R1_COLUMN_J) {
		this.R1_COLUMN_J = R1_COLUMN_J;
	}

	public BigDecimal getR1_COLUMN_K() {
		return R1_COLUMN_K;
	}

	public void setR1_COLUMN_K(BigDecimal R1_COLUMN_K) {
		this.R1_COLUMN_K = R1_COLUMN_K;
	}

	public BigDecimal getR1_COLUMN_L() {
		return R1_COLUMN_L;
	}

	public void setR1_COLUMN_L(BigDecimal R1_COLUMN_L) {
		this.R1_COLUMN_L = R1_COLUMN_L;
	}

	public BigDecimal getR1_COLUMN_M() {
		return R1_COLUMN_M;
	}

	public void setR1_COLUMN_M(BigDecimal R1_COLUMN_M) {
		this.R1_COLUMN_M = R1_COLUMN_M;
	}

	public BigDecimal getR1_COLUMN_N() {
		return R1_COLUMN_N;
	}

	public void setR1_COLUMN_N(BigDecimal R1_COLUMN_N) {
		this.R1_COLUMN_N = R1_COLUMN_N;
	}

	public String getR2_COLUMN_A() {
		return R2_COLUMN_A;
	}

	public void setR2_COLUMN_A(String R2_COLUMN_A) {
		this.R2_COLUMN_A = R2_COLUMN_A;
	}

	public BigDecimal getR2_COLUMN_B() {
		return R2_COLUMN_B;
	}

	public void setR2_COLUMN_B(BigDecimal R2_COLUMN_B) {
		this.R2_COLUMN_B = R2_COLUMN_B;
	}

	public BigDecimal getR2_COLUMN_C() {
		return R2_COLUMN_C;
	}

	public void setR2_COLUMN_C(BigDecimal R2_COLUMN_C) {
		this.R2_COLUMN_C = R2_COLUMN_C;
	}

	public BigDecimal getR2_COLUMN_D() {
		return R2_COLUMN_D;
	}

	public void setR2_COLUMN_D(BigDecimal R2_COLUMN_D) {
		this.R2_COLUMN_D = R2_COLUMN_D;
	}

	public BigDecimal getR2_COLUMN_E() {
		return R2_COLUMN_E;
	}

	public void setR2_COLUMN_E(BigDecimal R2_COLUMN_E) {
		this.R2_COLUMN_E = R2_COLUMN_E;
	}

	public BigDecimal getR2_COLUMN_F() {
		return R2_COLUMN_F;
	}

	public void setR2_COLUMN_F(BigDecimal R2_COLUMN_F) {
		this.R2_COLUMN_F = R2_COLUMN_F;
	}

	public BigDecimal getR2_COLUMN_G() {
		return R2_COLUMN_G;
	}

	public void setR2_COLUMN_G(BigDecimal R2_COLUMN_G) {
		this.R2_COLUMN_G = R2_COLUMN_G;
	}

	public BigDecimal getR2_COLUMN_H() {
		return R2_COLUMN_H;
	}

	public void setR2_COLUMN_H(BigDecimal R2_COLUMN_H) {
		this.R2_COLUMN_H = R2_COLUMN_H;
	}

	public BigDecimal getR2_COLUMN_I() {
		return R2_COLUMN_I;
	}

	public void setR2_COLUMN_I(BigDecimal R2_COLUMN_I) {
		this.R2_COLUMN_I = R2_COLUMN_I;
	}

	public BigDecimal getR2_COLUMN_J() {
		return R2_COLUMN_J;
	}

	public void setR2_COLUMN_J(BigDecimal R2_COLUMN_J) {
		this.R2_COLUMN_J = R2_COLUMN_J;
	}

	public BigDecimal getR2_COLUMN_K() {
		return R2_COLUMN_K;
	}

	public void setR2_COLUMN_K(BigDecimal R2_COLUMN_K) {
		this.R2_COLUMN_K = R2_COLUMN_K;
	}

	public BigDecimal getR2_COLUMN_L() {
		return R2_COLUMN_L;
	}

	public void setR2_COLUMN_L(BigDecimal R2_COLUMN_L) {
		this.R2_COLUMN_L = R2_COLUMN_L;
	}

	public BigDecimal getR2_COLUMN_M() {
		return R2_COLUMN_M;
	}

	public void setR2_COLUMN_M(BigDecimal R2_COLUMN_M) {
		this.R2_COLUMN_M = R2_COLUMN_M;
	}

	public BigDecimal getR2_COLUMN_N() {
		return R2_COLUMN_N;
	}

	public void setR2_COLUMN_N(BigDecimal R2_COLUMN_N) {
		this.R2_COLUMN_N = R2_COLUMN_N;
	}

	public String getR3_COLUMN_A() {
		return R3_COLUMN_A;
	}

	public void setR3_COLUMN_A(String R3_COLUMN_A) {
		this.R3_COLUMN_A = R3_COLUMN_A;
	}

	public BigDecimal getR3_COLUMN_B() {
		return R3_COLUMN_B;
	}

	public void setR3_COLUMN_B(BigDecimal R3_COLUMN_B) {
		this.R3_COLUMN_B = R3_COLUMN_B;
	}

	public BigDecimal getR3_COLUMN_C() {
		return R3_COLUMN_C;
	}

	public void setR3_COLUMN_C(BigDecimal R3_COLUMN_C) {
		this.R3_COLUMN_C = R3_COLUMN_C;
	}

	public BigDecimal getR3_COLUMN_D() {
		return R3_COLUMN_D;
	}

	public void setR3_COLUMN_D(BigDecimal R3_COLUMN_D) {
		this.R3_COLUMN_D = R3_COLUMN_D;
	}

	public BigDecimal getR3_COLUMN_E() {
		return R3_COLUMN_E;
	}

	public void setR3_COLUMN_E(BigDecimal R3_COLUMN_E) {
		this.R3_COLUMN_E = R3_COLUMN_E;
	}

	public BigDecimal getR3_COLUMN_F() {
		return R3_COLUMN_F;
	}

	public void setR3_COLUMN_F(BigDecimal R3_COLUMN_F) {
		this.R3_COLUMN_F = R3_COLUMN_F;
	}

	public BigDecimal getR3_COLUMN_G() {
		return R3_COLUMN_G;
	}

	public void setR3_COLUMN_G(BigDecimal R3_COLUMN_G) {
		this.R3_COLUMN_G = R3_COLUMN_G;
	}

	public BigDecimal getR3_COLUMN_H() {
		return R3_COLUMN_H;
	}

	public void setR3_COLUMN_H(BigDecimal R3_COLUMN_H) {
		this.R3_COLUMN_H = R3_COLUMN_H;
	}

	public BigDecimal getR3_COLUMN_I() {
		return R3_COLUMN_I;
	}

	public void setR3_COLUMN_I(BigDecimal R3_COLUMN_I) {
		this.R3_COLUMN_I = R3_COLUMN_I;
	}

	public BigDecimal getR3_COLUMN_J() {
		return R3_COLUMN_J;
	}

	public void setR3_COLUMN_J(BigDecimal R3_COLUMN_J) {
		this.R3_COLUMN_J = R3_COLUMN_J;
	}

	public BigDecimal getR3_COLUMN_K() {
		return R3_COLUMN_K;
	}

	public void setR3_COLUMN_K(BigDecimal R3_COLUMN_K) {
		this.R3_COLUMN_K = R3_COLUMN_K;
	}

	public BigDecimal getR3_COLUMN_L() {
		return R3_COLUMN_L;
	}

	public void setR3_COLUMN_L(BigDecimal R3_COLUMN_L) {
		this.R3_COLUMN_L = R3_COLUMN_L;
	}

	public BigDecimal getR3_COLUMN_M() {
		return R3_COLUMN_M;
	}

	public void setR3_COLUMN_M(BigDecimal R3_COLUMN_M) {
		this.R3_COLUMN_M = R3_COLUMN_M;
	}

	public BigDecimal getR3_COLUMN_N() {
		return R3_COLUMN_N;
	}

	public void setR3_COLUMN_N(BigDecimal R3_COLUMN_N) {
		this.R3_COLUMN_N = R3_COLUMN_N;
	}

	public String getR4_COLUMN_A() {
		return R4_COLUMN_A;
	}

	public void setR4_COLUMN_A(String R4_COLUMN_A) {
		this.R4_COLUMN_A = R4_COLUMN_A;
	}

	public BigDecimal getR4_COLUMN_B() {
		return R4_COLUMN_B;
	}

	public void setR4_COLUMN_B(BigDecimal R4_COLUMN_B) {
		this.R4_COLUMN_B = R4_COLUMN_B;
	}

	public BigDecimal getR4_COLUMN_C() {
		return R4_COLUMN_C;
	}

	public void setR4_COLUMN_C(BigDecimal R4_COLUMN_C) {
		this.R4_COLUMN_C = R4_COLUMN_C;
	}

	public BigDecimal getR4_COLUMN_D() {
		return R4_COLUMN_D;
	}

	public void setR4_COLUMN_D(BigDecimal R4_COLUMN_D) {
		this.R4_COLUMN_D = R4_COLUMN_D;
	}

	public BigDecimal getR4_COLUMN_E() {
		return R4_COLUMN_E;
	}

	public void setR4_COLUMN_E(BigDecimal R4_COLUMN_E) {
		this.R4_COLUMN_E = R4_COLUMN_E;
	}

	public BigDecimal getR4_COLUMN_F() {
		return R4_COLUMN_F;
	}

	public void setR4_COLUMN_F(BigDecimal R4_COLUMN_F) {
		this.R4_COLUMN_F = R4_COLUMN_F;
	}

	public BigDecimal getR4_COLUMN_G() {
		return R4_COLUMN_G;
	}

	public void setR4_COLUMN_G(BigDecimal R4_COLUMN_G) {
		this.R4_COLUMN_G = R4_COLUMN_G;
	}

	public BigDecimal getR4_COLUMN_H() {
		return R4_COLUMN_H;
	}

	public void setR4_COLUMN_H(BigDecimal R4_COLUMN_H) {
		this.R4_COLUMN_H = R4_COLUMN_H;
	}

	public BigDecimal getR4_COLUMN_I() {
		return R4_COLUMN_I;
	}

	public void setR4_COLUMN_I(BigDecimal R4_COLUMN_I) {
		this.R4_COLUMN_I = R4_COLUMN_I;
	}

	public BigDecimal getR4_COLUMN_J() {
		return R4_COLUMN_J;
	}

	public void setR4_COLUMN_J(BigDecimal R4_COLUMN_J) {
		this.R4_COLUMN_J = R4_COLUMN_J;
	}

	public BigDecimal getR4_COLUMN_K() {
		return R4_COLUMN_K;
	}

	public void setR4_COLUMN_K(BigDecimal R4_COLUMN_K) {
		this.R4_COLUMN_K = R4_COLUMN_K;
	}

	public BigDecimal getR4_COLUMN_L() {
		return R4_COLUMN_L;
	}

	public void setR4_COLUMN_L(BigDecimal R4_COLUMN_L) {
		this.R4_COLUMN_L = R4_COLUMN_L;
	}

	public BigDecimal getR4_COLUMN_M() {
		return R4_COLUMN_M;
	}

	public void setR4_COLUMN_M(BigDecimal R4_COLUMN_M) {
		this.R4_COLUMN_M = R4_COLUMN_M;
	}

	public BigDecimal getR4_COLUMN_N() {
		return R4_COLUMN_N;
	}

	public void setR4_COLUMN_N(BigDecimal R4_COLUMN_N) {
		this.R4_COLUMN_N = R4_COLUMN_N;
	}

	public String getR5_COLUMN_A() {
		return R5_COLUMN_A;
	}

	public void setR5_COLUMN_A(String R5_COLUMN_A) {
		this.R5_COLUMN_A = R5_COLUMN_A;
	}

	public BigDecimal getR5_COLUMN_B() {
		return R5_COLUMN_B;
	}

	public void setR5_COLUMN_B(BigDecimal R5_COLUMN_B) {
		this.R5_COLUMN_B = R5_COLUMN_B;
	}

	public BigDecimal getR5_COLUMN_C() {
		return R5_COLUMN_C;
	}

	public void setR5_COLUMN_C(BigDecimal R5_COLUMN_C) {
		this.R5_COLUMN_C = R5_COLUMN_C;
	}

	public BigDecimal getR5_COLUMN_D() {
		return R5_COLUMN_D;
	}

	public void setR5_COLUMN_D(BigDecimal R5_COLUMN_D) {
		this.R5_COLUMN_D = R5_COLUMN_D;
	}

	public BigDecimal getR5_COLUMN_E() {
		return R5_COLUMN_E;
	}

	public void setR5_COLUMN_E(BigDecimal R5_COLUMN_E) {
		this.R5_COLUMN_E = R5_COLUMN_E;
	}

	public BigDecimal getR5_COLUMN_F() {
		return R5_COLUMN_F;
	}

	public void setR5_COLUMN_F(BigDecimal R5_COLUMN_F) {
		this.R5_COLUMN_F = R5_COLUMN_F;
	}

	public BigDecimal getR5_COLUMN_G() {
		return R5_COLUMN_G;
	}

	public void setR5_COLUMN_G(BigDecimal R5_COLUMN_G) {
		this.R5_COLUMN_G = R5_COLUMN_G;
	}

	public BigDecimal getR5_COLUMN_H() {
		return R5_COLUMN_H;
	}

	public void setR5_COLUMN_H(BigDecimal R5_COLUMN_H) {
		this.R5_COLUMN_H = R5_COLUMN_H;
	}

	public BigDecimal getR5_COLUMN_I() {
		return R5_COLUMN_I;
	}

	public void setR5_COLUMN_I(BigDecimal R5_COLUMN_I) {
		this.R5_COLUMN_I = R5_COLUMN_I;
	}

	public BigDecimal getR5_COLUMN_J() {
		return R5_COLUMN_J;
	}

	public void setR5_COLUMN_J(BigDecimal R5_COLUMN_J) {
		this.R5_COLUMN_J = R5_COLUMN_J;
	}

	public BigDecimal getR5_COLUMN_K() {
		return R5_COLUMN_K;
	}

	public void setR5_COLUMN_K(BigDecimal R5_COLUMN_K) {
		this.R5_COLUMN_K = R5_COLUMN_K;
	}

	public BigDecimal getR5_COLUMN_L() {
		return R5_COLUMN_L;
	}

	public void setR5_COLUMN_L(BigDecimal R5_COLUMN_L) {
		this.R5_COLUMN_L = R5_COLUMN_L;
	}

	public BigDecimal getR5_COLUMN_M() {
		return R5_COLUMN_M;
	}

	public void setR5_COLUMN_M(BigDecimal R5_COLUMN_M) {
		this.R5_COLUMN_M = R5_COLUMN_M;
	}

	public BigDecimal getR5_COLUMN_N() {
		return R5_COLUMN_N;
	}

	public void setR5_COLUMN_N(BigDecimal R5_COLUMN_N) {
		this.R5_COLUMN_N = R5_COLUMN_N;
	}

	public String getR6_COLUMN_A() {
		return R6_COLUMN_A;
	}

	public void setR6_COLUMN_A(String R6_COLUMN_A) {
		this.R6_COLUMN_A = R6_COLUMN_A;
	}

	public BigDecimal getR6_COLUMN_B() {
		return R6_COLUMN_B;
	}

	public void setR6_COLUMN_B(BigDecimal R6_COLUMN_B) {
		this.R6_COLUMN_B = R6_COLUMN_B;
	}

	public BigDecimal getR6_COLUMN_C() {
		return R6_COLUMN_C;
	}

	public void setR6_COLUMN_C(BigDecimal R6_COLUMN_C) {
		this.R6_COLUMN_C = R6_COLUMN_C;
	}

	public BigDecimal getR6_COLUMN_D() {
		return R6_COLUMN_D;
	}

	public void setR6_COLUMN_D(BigDecimal R6_COLUMN_D) {
		this.R6_COLUMN_D = R6_COLUMN_D;
	}

	public BigDecimal getR6_COLUMN_E() {
		return R6_COLUMN_E;
	}

	public void setR6_COLUMN_E(BigDecimal R6_COLUMN_E) {
		this.R6_COLUMN_E = R6_COLUMN_E;
	}

	public BigDecimal getR6_COLUMN_F() {
		return R6_COLUMN_F;
	}

	public void setR6_COLUMN_F(BigDecimal R6_COLUMN_F) {
		this.R6_COLUMN_F = R6_COLUMN_F;
	}

	public BigDecimal getR6_COLUMN_G() {
		return R6_COLUMN_G;
	}

	public void setR6_COLUMN_G(BigDecimal R6_COLUMN_G) {
		this.R6_COLUMN_G = R6_COLUMN_G;
	}

	public BigDecimal getR6_COLUMN_H() {
		return R6_COLUMN_H;
	}

	public void setR6_COLUMN_H(BigDecimal R6_COLUMN_H) {
		this.R6_COLUMN_H = R6_COLUMN_H;
	}

	public BigDecimal getR6_COLUMN_I() {
		return R6_COLUMN_I;
	}

	public void setR6_COLUMN_I(BigDecimal R6_COLUMN_I) {
		this.R6_COLUMN_I = R6_COLUMN_I;
	}

	public BigDecimal getR6_COLUMN_J() {
		return R6_COLUMN_J;
	}

	public void setR6_COLUMN_J(BigDecimal R6_COLUMN_J) {
		this.R6_COLUMN_J = R6_COLUMN_J;
	}

	public BigDecimal getR6_COLUMN_K() {
		return R6_COLUMN_K;
	}

	public void setR6_COLUMN_K(BigDecimal R6_COLUMN_K) {
		this.R6_COLUMN_K = R6_COLUMN_K;
	}

	public BigDecimal getR6_COLUMN_L() {
		return R6_COLUMN_L;
	}

	public void setR6_COLUMN_L(BigDecimal R6_COLUMN_L) {
		this.R6_COLUMN_L = R6_COLUMN_L;
	}

	public BigDecimal getR6_COLUMN_M() {
		return R6_COLUMN_M;
	}

	public void setR6_COLUMN_M(BigDecimal R6_COLUMN_M) {
		this.R6_COLUMN_M = R6_COLUMN_M;
	}

	public BigDecimal getR6_COLUMN_N() {
		return R6_COLUMN_N;
	}

	public void setR6_COLUMN_N(BigDecimal R6_COLUMN_N) {
		this.R6_COLUMN_N = R6_COLUMN_N;
	}

	public String getR7_COLUMN_A() {
		return R7_COLUMN_A;
	}

	public void setR7_COLUMN_A(String R7_COLUMN_A) {
		this.R7_COLUMN_A = R7_COLUMN_A;
	}

	public BigDecimal getR7_COLUMN_B() {
		return R7_COLUMN_B;
	}

	public void setR7_COLUMN_B(BigDecimal R7_COLUMN_B) {
		this.R7_COLUMN_B = R7_COLUMN_B;
	}

	public BigDecimal getR7_COLUMN_C() {
		return R7_COLUMN_C;
	}

	public void setR7_COLUMN_C(BigDecimal R7_COLUMN_C) {
		this.R7_COLUMN_C = R7_COLUMN_C;
	}

	public BigDecimal getR7_COLUMN_D() {
		return R7_COLUMN_D;
	}

	public void setR7_COLUMN_D(BigDecimal R7_COLUMN_D) {
		this.R7_COLUMN_D = R7_COLUMN_D;
	}

	public BigDecimal getR7_COLUMN_E() {
		return R7_COLUMN_E;
	}

	public void setR7_COLUMN_E(BigDecimal R7_COLUMN_E) {
		this.R7_COLUMN_E = R7_COLUMN_E;
	}

	public BigDecimal getR7_COLUMN_F() {
		return R7_COLUMN_F;
	}

	public void setR7_COLUMN_F(BigDecimal R7_COLUMN_F) {
		this.R7_COLUMN_F = R7_COLUMN_F;
	}

	public BigDecimal getR7_COLUMN_G() {
		return R7_COLUMN_G;
	}

	public void setR7_COLUMN_G(BigDecimal R7_COLUMN_G) {
		this.R7_COLUMN_G = R7_COLUMN_G;
	}

	public BigDecimal getR7_COLUMN_H() {
		return R7_COLUMN_H;
	}

	public void setR7_COLUMN_H(BigDecimal R7_COLUMN_H) {
		this.R7_COLUMN_H = R7_COLUMN_H;
	}

	public BigDecimal getR7_COLUMN_I() {
		return R7_COLUMN_I;
	}

	public void setR7_COLUMN_I(BigDecimal R7_COLUMN_I) {
		this.R7_COLUMN_I = R7_COLUMN_I;
	}

	public BigDecimal getR7_COLUMN_J() {
		return R7_COLUMN_J;
	}

	public void setR7_COLUMN_J(BigDecimal R7_COLUMN_J) {
		this.R7_COLUMN_J = R7_COLUMN_J;
	}

	public BigDecimal getR7_COLUMN_K() {
		return R7_COLUMN_K;
	}

	public void setR7_COLUMN_K(BigDecimal R7_COLUMN_K) {
		this.R7_COLUMN_K = R7_COLUMN_K;
	}

	public BigDecimal getR7_COLUMN_L() {
		return R7_COLUMN_L;
	}

	public void setR7_COLUMN_L(BigDecimal R7_COLUMN_L) {
		this.R7_COLUMN_L = R7_COLUMN_L;
	}

	public BigDecimal getR7_COLUMN_M() {
		return R7_COLUMN_M;
	}

	public void setR7_COLUMN_M(BigDecimal R7_COLUMN_M) {
		this.R7_COLUMN_M = R7_COLUMN_M;
	}

	public BigDecimal getR7_COLUMN_N() {
		return R7_COLUMN_N;
	}

	public void setR7_COLUMN_N(BigDecimal R7_COLUMN_N) {
		this.R7_COLUMN_N = R7_COLUMN_N;
	}

	public String getR8_COLUMN_A() {
		return R8_COLUMN_A;
	}

	public void setR8_COLUMN_A(String R8_COLUMN_A) {
		this.R8_COLUMN_A = R8_COLUMN_A;
	}

	public BigDecimal getR8_COLUMN_B() {
		return R8_COLUMN_B;
	}

	public void setR8_COLUMN_B(BigDecimal R8_COLUMN_B) {
		this.R8_COLUMN_B = R8_COLUMN_B;
	}

	public BigDecimal getR8_COLUMN_C() {
		return R8_COLUMN_C;
	}

	public void setR8_COLUMN_C(BigDecimal R8_COLUMN_C) {
		this.R8_COLUMN_C = R8_COLUMN_C;
	}

	public BigDecimal getR8_COLUMN_D() {
		return R8_COLUMN_D;
	}

	public void setR8_COLUMN_D(BigDecimal R8_COLUMN_D) {
		this.R8_COLUMN_D = R8_COLUMN_D;
	}

	public BigDecimal getR8_COLUMN_E() {
		return R8_COLUMN_E;
	}

	public void setR8_COLUMN_E(BigDecimal R8_COLUMN_E) {
		this.R8_COLUMN_E = R8_COLUMN_E;
	}

	public BigDecimal getR8_COLUMN_F() {
		return R8_COLUMN_F;
	}

	public void setR8_COLUMN_F(BigDecimal R8_COLUMN_F) {
		this.R8_COLUMN_F = R8_COLUMN_F;
	}

	public BigDecimal getR8_COLUMN_G() {
		return R8_COLUMN_G;
	}

	public void setR8_COLUMN_G(BigDecimal R8_COLUMN_G) {
		this.R8_COLUMN_G = R8_COLUMN_G;
	}

	public BigDecimal getR8_COLUMN_H() {
		return R8_COLUMN_H;
	}

	public void setR8_COLUMN_H(BigDecimal R8_COLUMN_H) {
		this.R8_COLUMN_H = R8_COLUMN_H;
	}

	public BigDecimal getR8_COLUMN_I() {
		return R8_COLUMN_I;
	}

	public void setR8_COLUMN_I(BigDecimal R8_COLUMN_I) {
		this.R8_COLUMN_I = R8_COLUMN_I;
	}

	public BigDecimal getR8_COLUMN_J() {
		return R8_COLUMN_J;
	}

	public void setR8_COLUMN_J(BigDecimal R8_COLUMN_J) {
		this.R8_COLUMN_J = R8_COLUMN_J;
	}

	public BigDecimal getR8_COLUMN_K() {
		return R8_COLUMN_K;
	}

	public void setR8_COLUMN_K(BigDecimal R8_COLUMN_K) {
		this.R8_COLUMN_K = R8_COLUMN_K;
	}

	public BigDecimal getR8_COLUMN_L() {
		return R8_COLUMN_L;
	}

	public void setR8_COLUMN_L(BigDecimal R8_COLUMN_L) {
		this.R8_COLUMN_L = R8_COLUMN_L;
	}

	public BigDecimal getR8_COLUMN_M() {
		return R8_COLUMN_M;
	}

	public void setR8_COLUMN_M(BigDecimal R8_COLUMN_M) {
		this.R8_COLUMN_M = R8_COLUMN_M;
	}

	public BigDecimal getR8_COLUMN_N() {
		return R8_COLUMN_N;
	}

	public void setR8_COLUMN_N(BigDecimal R8_COLUMN_N) {
		this.R8_COLUMN_N = R8_COLUMN_N;
	}

	public String getR9_COLUMN_A() {
		return R9_COLUMN_A;
	}

	public void setR9_COLUMN_A(String R9_COLUMN_A) {
		this.R9_COLUMN_A = R9_COLUMN_A;
	}

	public BigDecimal getR9_COLUMN_B() {
		return R9_COLUMN_B;
	}

	public void setR9_COLUMN_B(BigDecimal R9_COLUMN_B) {
		this.R9_COLUMN_B = R9_COLUMN_B;
	}

	public BigDecimal getR9_COLUMN_C() {
		return R9_COLUMN_C;
	}

	public void setR9_COLUMN_C(BigDecimal R9_COLUMN_C) {
		this.R9_COLUMN_C = R9_COLUMN_C;
	}

	public BigDecimal getR9_COLUMN_D() {
		return R9_COLUMN_D;
	}

	public void setR9_COLUMN_D(BigDecimal R9_COLUMN_D) {
		this.R9_COLUMN_D = R9_COLUMN_D;
	}

	public BigDecimal getR9_COLUMN_E() {
		return R9_COLUMN_E;
	}

	public void setR9_COLUMN_E(BigDecimal R9_COLUMN_E) {
		this.R9_COLUMN_E = R9_COLUMN_E;
	}

	public BigDecimal getR9_COLUMN_F() {
		return R9_COLUMN_F;
	}

	public void setR9_COLUMN_F(BigDecimal R9_COLUMN_F) {
		this.R9_COLUMN_F = R9_COLUMN_F;
	}

	public BigDecimal getR9_COLUMN_G() {
		return R9_COLUMN_G;
	}

	public void setR9_COLUMN_G(BigDecimal R9_COLUMN_G) {
		this.R9_COLUMN_G = R9_COLUMN_G;
	}

	public BigDecimal getR9_COLUMN_H() {
		return R9_COLUMN_H;
	}

	public void setR9_COLUMN_H(BigDecimal R9_COLUMN_H) {
		this.R9_COLUMN_H = R9_COLUMN_H;
	}

	public BigDecimal getR9_COLUMN_I() {
		return R9_COLUMN_I;
	}

	public void setR9_COLUMN_I(BigDecimal R9_COLUMN_I) {
		this.R9_COLUMN_I = R9_COLUMN_I;
	}

	public BigDecimal getR9_COLUMN_J() {
		return R9_COLUMN_J;
	}

	public void setR9_COLUMN_J(BigDecimal R9_COLUMN_J) {
		this.R9_COLUMN_J = R9_COLUMN_J;
	}

	public BigDecimal getR9_COLUMN_K() {
		return R9_COLUMN_K;
	}

	public void setR9_COLUMN_K(BigDecimal R9_COLUMN_K) {
		this.R9_COLUMN_K = R9_COLUMN_K;
	}

	public BigDecimal getR9_COLUMN_L() {
		return R9_COLUMN_L;
	}

	public void setR9_COLUMN_L(BigDecimal R9_COLUMN_L) {
		this.R9_COLUMN_L = R9_COLUMN_L;
	}

	public BigDecimal getR9_COLUMN_M() {
		return R9_COLUMN_M;
	}

	public void setR9_COLUMN_M(BigDecimal R9_COLUMN_M) {
		this.R9_COLUMN_M = R9_COLUMN_M;
	}

	public BigDecimal getR9_COLUMN_N() {
		return R9_COLUMN_N;
	}

	public void setR9_COLUMN_N(BigDecimal R9_COLUMN_N) {
		this.R9_COLUMN_N = R9_COLUMN_N;
	}

	public String getR10_COLUMN_A() {
		return R10_COLUMN_A;
	}

	public void setR10_COLUMN_A(String R10_COLUMN_A) {
		this.R10_COLUMN_A = R10_COLUMN_A;
	}

	public BigDecimal getR10_COLUMN_B() {
		return R10_COLUMN_B;
	}

	public void setR10_COLUMN_B(BigDecimal R10_COLUMN_B) {
		this.R10_COLUMN_B = R10_COLUMN_B;
	}

	public BigDecimal getR10_COLUMN_C() {
		return R10_COLUMN_C;
	}

	public void setR10_COLUMN_C(BigDecimal R10_COLUMN_C) {
		this.R10_COLUMN_C = R10_COLUMN_C;
	}

	public BigDecimal getR10_COLUMN_D() {
		return R10_COLUMN_D;
	}

	public void setR10_COLUMN_D(BigDecimal R10_COLUMN_D) {
		this.R10_COLUMN_D = R10_COLUMN_D;
	}

	public BigDecimal getR10_COLUMN_E() {
		return R10_COLUMN_E;
	}

	public void setR10_COLUMN_E(BigDecimal R10_COLUMN_E) {
		this.R10_COLUMN_E = R10_COLUMN_E;
	}

	public BigDecimal getR10_COLUMN_F() {
		return R10_COLUMN_F;
	}

	public void setR10_COLUMN_F(BigDecimal R10_COLUMN_F) {
		this.R10_COLUMN_F = R10_COLUMN_F;
	}

	public BigDecimal getR10_COLUMN_G() {
		return R10_COLUMN_G;
	}

	public void setR10_COLUMN_G(BigDecimal R10_COLUMN_G) {
		this.R10_COLUMN_G = R10_COLUMN_G;
	}

	public BigDecimal getR10_COLUMN_H() {
		return R10_COLUMN_H;
	}

	public void setR10_COLUMN_H(BigDecimal R10_COLUMN_H) {
		this.R10_COLUMN_H = R10_COLUMN_H;
	}

	public BigDecimal getR10_COLUMN_I() {
		return R10_COLUMN_I;
	}

	public void setR10_COLUMN_I(BigDecimal R10_COLUMN_I) {
		this.R10_COLUMN_I = R10_COLUMN_I;
	}

	public BigDecimal getR10_COLUMN_J() {
		return R10_COLUMN_J;
	}

	public void setR10_COLUMN_J(BigDecimal R10_COLUMN_J) {
		this.R10_COLUMN_J = R10_COLUMN_J;
	}

	public BigDecimal getR10_COLUMN_K() {
		return R10_COLUMN_K;
	}

	public void setR10_COLUMN_K(BigDecimal R10_COLUMN_K) {
		this.R10_COLUMN_K = R10_COLUMN_K;
	}

	public BigDecimal getR10_COLUMN_L() {
		return R10_COLUMN_L;
	}

	public void setR10_COLUMN_L(BigDecimal R10_COLUMN_L) {
		this.R10_COLUMN_L = R10_COLUMN_L;
	}

	public BigDecimal getR10_COLUMN_M() {
		return R10_COLUMN_M;
	}

	public void setR10_COLUMN_M(BigDecimal R10_COLUMN_M) {
		this.R10_COLUMN_M = R10_COLUMN_M;
	}

	public BigDecimal getR10_COLUMN_N() {
		return R10_COLUMN_N;
	}

	public void setR10_COLUMN_N(BigDecimal R10_COLUMN_N) {
		this.R10_COLUMN_N = R10_COLUMN_N;
	}

	public String getR11_COLUMN_A() {
		return R11_COLUMN_A;
	}

	public void setR11_COLUMN_A(String R11_COLUMN_A) {
		this.R11_COLUMN_A = R11_COLUMN_A;
	}

	public BigDecimal getR11_COLUMN_B() {
		return R11_COLUMN_B;
	}

	public void setR11_COLUMN_B(BigDecimal R11_COLUMN_B) {
		this.R11_COLUMN_B = R11_COLUMN_B;
	}

	public BigDecimal getR11_COLUMN_C() {
		return R11_COLUMN_C;
	}

	public void setR11_COLUMN_C(BigDecimal R11_COLUMN_C) {
		this.R11_COLUMN_C = R11_COLUMN_C;
	}

	public BigDecimal getR11_COLUMN_D() {
		return R11_COLUMN_D;
	}

	public void setR11_COLUMN_D(BigDecimal R11_COLUMN_D) {
		this.R11_COLUMN_D = R11_COLUMN_D;
	}

	public BigDecimal getR11_COLUMN_E() {
		return R11_COLUMN_E;
	}

	public void setR11_COLUMN_E(BigDecimal R11_COLUMN_E) {
		this.R11_COLUMN_E = R11_COLUMN_E;
	}

	public BigDecimal getR11_COLUMN_F() {
		return R11_COLUMN_F;
	}

	public void setR11_COLUMN_F(BigDecimal R11_COLUMN_F) {
		this.R11_COLUMN_F = R11_COLUMN_F;
	}

	public BigDecimal getR11_COLUMN_G() {
		return R11_COLUMN_G;
	}

	public void setR11_COLUMN_G(BigDecimal R11_COLUMN_G) {
		this.R11_COLUMN_G = R11_COLUMN_G;
	}

	public BigDecimal getR11_COLUMN_H() {
		return R11_COLUMN_H;
	}

	public void setR11_COLUMN_H(BigDecimal R11_COLUMN_H) {
		this.R11_COLUMN_H = R11_COLUMN_H;
	}

	public BigDecimal getR11_COLUMN_I() {
		return R11_COLUMN_I;
	}

	public void setR11_COLUMN_I(BigDecimal R11_COLUMN_I) {
		this.R11_COLUMN_I = R11_COLUMN_I;
	}

	public BigDecimal getR11_COLUMN_J() {
		return R11_COLUMN_J;
	}

	public void setR11_COLUMN_J(BigDecimal R11_COLUMN_J) {
		this.R11_COLUMN_J = R11_COLUMN_J;
	}

	public BigDecimal getR11_COLUMN_K() {
		return R11_COLUMN_K;
	}

	public void setR11_COLUMN_K(BigDecimal R11_COLUMN_K) {
		this.R11_COLUMN_K = R11_COLUMN_K;
	}

	public BigDecimal getR11_COLUMN_L() {
		return R11_COLUMN_L;
	}

	public void setR11_COLUMN_L(BigDecimal R11_COLUMN_L) {
		this.R11_COLUMN_L = R11_COLUMN_L;
	}

	public BigDecimal getR11_COLUMN_M() {
		return R11_COLUMN_M;
	}

	public void setR11_COLUMN_M(BigDecimal R11_COLUMN_M) {
		this.R11_COLUMN_M = R11_COLUMN_M;
	}

	public BigDecimal getR11_COLUMN_N() {
		return R11_COLUMN_N;
	}

	public void setR11_COLUMN_N(BigDecimal R11_COLUMN_N) {
		this.R11_COLUMN_N = R11_COLUMN_N;
	}

	public String getR12_COLUMN_A() {
		return R12_COLUMN_A;
	}

	public void setR12_COLUMN_A(String R12_COLUMN_A) {
		this.R12_COLUMN_A = R12_COLUMN_A;
	}

	public BigDecimal getR12_COLUMN_B() {
		return R12_COLUMN_B;
	}

	public void setR12_COLUMN_B(BigDecimal R12_COLUMN_B) {
		this.R12_COLUMN_B = R12_COLUMN_B;
	}

	public BigDecimal getR12_COLUMN_C() {
		return R12_COLUMN_C;
	}

	public void setR12_COLUMN_C(BigDecimal R12_COLUMN_C) {
		this.R12_COLUMN_C = R12_COLUMN_C;
	}

	public BigDecimal getR12_COLUMN_D() {
		return R12_COLUMN_D;
	}

	public void setR12_COLUMN_D(BigDecimal R12_COLUMN_D) {
		this.R12_COLUMN_D = R12_COLUMN_D;
	}

	public BigDecimal getR12_COLUMN_E() {
		return R12_COLUMN_E;
	}

	public void setR12_COLUMN_E(BigDecimal R12_COLUMN_E) {
		this.R12_COLUMN_E = R12_COLUMN_E;
	}

	public BigDecimal getR12_COLUMN_F() {
		return R12_COLUMN_F;
	}

	public void setR12_COLUMN_F(BigDecimal R12_COLUMN_F) {
		this.R12_COLUMN_F = R12_COLUMN_F;
	}

	public BigDecimal getR12_COLUMN_G() {
		return R12_COLUMN_G;
	}

	public void setR12_COLUMN_G(BigDecimal R12_COLUMN_G) {
		this.R12_COLUMN_G = R12_COLUMN_G;
	}

	public BigDecimal getR12_COLUMN_H() {
		return R12_COLUMN_H;
	}

	public void setR12_COLUMN_H(BigDecimal R12_COLUMN_H) {
		this.R12_COLUMN_H = R12_COLUMN_H;
	}

	public BigDecimal getR12_COLUMN_I() {
		return R12_COLUMN_I;
	}

	public void setR12_COLUMN_I(BigDecimal R12_COLUMN_I) {
		this.R12_COLUMN_I = R12_COLUMN_I;
	}

	public BigDecimal getR12_COLUMN_J() {
		return R12_COLUMN_J;
	}

	public void setR12_COLUMN_J(BigDecimal R12_COLUMN_J) {
		this.R12_COLUMN_J = R12_COLUMN_J;
	}

	public BigDecimal getR12_COLUMN_K() {
		return R12_COLUMN_K;
	}

	public void setR12_COLUMN_K(BigDecimal R12_COLUMN_K) {
		this.R12_COLUMN_K = R12_COLUMN_K;
	}

	public BigDecimal getR12_COLUMN_L() {
		return R12_COLUMN_L;
	}

	public void setR12_COLUMN_L(BigDecimal R12_COLUMN_L) {
		this.R12_COLUMN_L = R12_COLUMN_L;
	}

	public BigDecimal getR12_COLUMN_M() {
		return R12_COLUMN_M;
	}

	public void setR12_COLUMN_M(BigDecimal R12_COLUMN_M) {
		this.R12_COLUMN_M = R12_COLUMN_M;
	}

	public BigDecimal getR12_COLUMN_N() {
		return R12_COLUMN_N;
	}

	public void setR12_COLUMN_N(BigDecimal R12_COLUMN_N) {
		this.R12_COLUMN_N = R12_COLUMN_N;
	}

	public String getR13_COLUMN_A() {
		return R13_COLUMN_A;
	}

	public void setR13_COLUMN_A(String R13_COLUMN_A) {
		this.R13_COLUMN_A = R13_COLUMN_A;
	}

	public BigDecimal getR13_COLUMN_B() {
		return R13_COLUMN_B;
	}

	public void setR13_COLUMN_B(BigDecimal R13_COLUMN_B) {
		this.R13_COLUMN_B = R13_COLUMN_B;
	}

	public BigDecimal getR13_COLUMN_C() {
		return R13_COLUMN_C;
	}

	public void setR13_COLUMN_C(BigDecimal R13_COLUMN_C) {
		this.R13_COLUMN_C = R13_COLUMN_C;
	}

	public BigDecimal getR13_COLUMN_D() {
		return R13_COLUMN_D;
	}

	public void setR13_COLUMN_D(BigDecimal R13_COLUMN_D) {
		this.R13_COLUMN_D = R13_COLUMN_D;
	}

	public BigDecimal getR13_COLUMN_E() {
		return R13_COLUMN_E;
	}

	public void setR13_COLUMN_E(BigDecimal R13_COLUMN_E) {
		this.R13_COLUMN_E = R13_COLUMN_E;
	}

	public BigDecimal getR13_COLUMN_F() {
		return R13_COLUMN_F;
	}

	public void setR13_COLUMN_F(BigDecimal R13_COLUMN_F) {
		this.R13_COLUMN_F = R13_COLUMN_F;
	}

	public BigDecimal getR13_COLUMN_G() {
		return R13_COLUMN_G;
	}

	public void setR13_COLUMN_G(BigDecimal R13_COLUMN_G) {
		this.R13_COLUMN_G = R13_COLUMN_G;
	}

	public BigDecimal getR13_COLUMN_H() {
		return R13_COLUMN_H;
	}

	public void setR13_COLUMN_H(BigDecimal R13_COLUMN_H) {
		this.R13_COLUMN_H = R13_COLUMN_H;
	}

	public BigDecimal getR13_COLUMN_I() {
		return R13_COLUMN_I;
	}

	public void setR13_COLUMN_I(BigDecimal R13_COLUMN_I) {
		this.R13_COLUMN_I = R13_COLUMN_I;
	}

	public BigDecimal getR13_COLUMN_J() {
		return R13_COLUMN_J;
	}

	public void setR13_COLUMN_J(BigDecimal R13_COLUMN_J) {
		this.R13_COLUMN_J = R13_COLUMN_J;
	}

	public BigDecimal getR13_COLUMN_K() {
		return R13_COLUMN_K;
	}

	public void setR13_COLUMN_K(BigDecimal R13_COLUMN_K) {
		this.R13_COLUMN_K = R13_COLUMN_K;
	}

	public BigDecimal getR13_COLUMN_L() {
		return R13_COLUMN_L;
	}

	public void setR13_COLUMN_L(BigDecimal R13_COLUMN_L) {
		this.R13_COLUMN_L = R13_COLUMN_L;
	}

	public BigDecimal getR13_COLUMN_M() {
		return R13_COLUMN_M;
	}

	public void setR13_COLUMN_M(BigDecimal R13_COLUMN_M) {
		this.R13_COLUMN_M = R13_COLUMN_M;
	}

	public BigDecimal getR13_COLUMN_N() {
		return R13_COLUMN_N;
	}

	public void setR13_COLUMN_N(BigDecimal R13_COLUMN_N) {
		this.R13_COLUMN_N = R13_COLUMN_N;
	}

	public String getR14_COLUMN_A() {
		return R14_COLUMN_A;
	}

	public void setR14_COLUMN_A(String R14_COLUMN_A) {
		this.R14_COLUMN_A = R14_COLUMN_A;
	}

	public BigDecimal getR14_COLUMN_B() {
		return R14_COLUMN_B;
	}

	public void setR14_COLUMN_B(BigDecimal R14_COLUMN_B) {
		this.R14_COLUMN_B = R14_COLUMN_B;
	}

	public BigDecimal getR14_COLUMN_C() {
		return R14_COLUMN_C;
	}

	public void setR14_COLUMN_C(BigDecimal R14_COLUMN_C) {
		this.R14_COLUMN_C = R14_COLUMN_C;
	}

	public BigDecimal getR14_COLUMN_D() {
		return R14_COLUMN_D;
	}

	public void setR14_COLUMN_D(BigDecimal R14_COLUMN_D) {
		this.R14_COLUMN_D = R14_COLUMN_D;
	}

	public BigDecimal getR14_COLUMN_E() {
		return R14_COLUMN_E;
	}

	public void setR14_COLUMN_E(BigDecimal R14_COLUMN_E) {
		this.R14_COLUMN_E = R14_COLUMN_E;
	}

	public BigDecimal getR14_COLUMN_F() {
		return R14_COLUMN_F;
	}

	public void setR14_COLUMN_F(BigDecimal R14_COLUMN_F) {
		this.R14_COLUMN_F = R14_COLUMN_F;
	}

	public BigDecimal getR14_COLUMN_G() {
		return R14_COLUMN_G;
	}

	public void setR14_COLUMN_G(BigDecimal R14_COLUMN_G) {
		this.R14_COLUMN_G = R14_COLUMN_G;
	}

	public BigDecimal getR14_COLUMN_H() {
		return R14_COLUMN_H;
	}

	public void setR14_COLUMN_H(BigDecimal R14_COLUMN_H) {
		this.R14_COLUMN_H = R14_COLUMN_H;
	}

	public BigDecimal getR14_COLUMN_I() {
		return R14_COLUMN_I;
	}

	public void setR14_COLUMN_I(BigDecimal R14_COLUMN_I) {
		this.R14_COLUMN_I = R14_COLUMN_I;
	}

	public BigDecimal getR14_COLUMN_J() {
		return R14_COLUMN_J;
	}

	public void setR14_COLUMN_J(BigDecimal R14_COLUMN_J) {
		this.R14_COLUMN_J = R14_COLUMN_J;
	}

	public BigDecimal getR14_COLUMN_K() {
		return R14_COLUMN_K;
	}

	public void setR14_COLUMN_K(BigDecimal R14_COLUMN_K) {
		this.R14_COLUMN_K = R14_COLUMN_K;
	}

	public BigDecimal getR14_COLUMN_L() {
		return R14_COLUMN_L;
	}

	public void setR14_COLUMN_L(BigDecimal R14_COLUMN_L) {
		this.R14_COLUMN_L = R14_COLUMN_L;
	}

	public BigDecimal getR14_COLUMN_M() {
		return R14_COLUMN_M;
	}

	public void setR14_COLUMN_M(BigDecimal R14_COLUMN_M) {
		this.R14_COLUMN_M = R14_COLUMN_M;
	}

	public BigDecimal getR14_COLUMN_N() {
		return R14_COLUMN_N;
	}

	public void setR14_COLUMN_N(BigDecimal R14_COLUMN_N) {
		this.R14_COLUMN_N = R14_COLUMN_N;
	}

	public String getR15_COLUMN_A() {
		return R15_COLUMN_A;
	}

	public void setR15_COLUMN_A(String R15_COLUMN_A) {
		this.R15_COLUMN_A = R15_COLUMN_A;
	}

	public BigDecimal getR15_COLUMN_B() {
		return R15_COLUMN_B;
	}

	public void setR15_COLUMN_B(BigDecimal R15_COLUMN_B) {
		this.R15_COLUMN_B = R15_COLUMN_B;
	}

	public BigDecimal getR15_COLUMN_C() {
		return R15_COLUMN_C;
	}

	public void setR15_COLUMN_C(BigDecimal R15_COLUMN_C) {
		this.R15_COLUMN_C = R15_COLUMN_C;
	}

	public BigDecimal getR15_COLUMN_D() {
		return R15_COLUMN_D;
	}

	public void setR15_COLUMN_D(BigDecimal R15_COLUMN_D) {
		this.R15_COLUMN_D = R15_COLUMN_D;
	}

	public BigDecimal getR15_COLUMN_E() {
		return R15_COLUMN_E;
	}

	public void setR15_COLUMN_E(BigDecimal R15_COLUMN_E) {
		this.R15_COLUMN_E = R15_COLUMN_E;
	}

	public BigDecimal getR15_COLUMN_F() {
		return R15_COLUMN_F;
	}

	public void setR15_COLUMN_F(BigDecimal R15_COLUMN_F) {
		this.R15_COLUMN_F = R15_COLUMN_F;
	}

	public BigDecimal getR15_COLUMN_G() {
		return R15_COLUMN_G;
	}

	public void setR15_COLUMN_G(BigDecimal R15_COLUMN_G) {
		this.R15_COLUMN_G = R15_COLUMN_G;
	}

	public BigDecimal getR15_COLUMN_H() {
		return R15_COLUMN_H;
	}

	public void setR15_COLUMN_H(BigDecimal R15_COLUMN_H) {
		this.R15_COLUMN_H = R15_COLUMN_H;
	}

	public BigDecimal getR15_COLUMN_I() {
		return R15_COLUMN_I;
	}

	public void setR15_COLUMN_I(BigDecimal R15_COLUMN_I) {
		this.R15_COLUMN_I = R15_COLUMN_I;
	}

	public BigDecimal getR15_COLUMN_J() {
		return R15_COLUMN_J;
	}

	public void setR15_COLUMN_J(BigDecimal R15_COLUMN_J) {
		this.R15_COLUMN_J = R15_COLUMN_J;
	}

	public BigDecimal getR15_COLUMN_K() {
		return R15_COLUMN_K;
	}

	public void setR15_COLUMN_K(BigDecimal R15_COLUMN_K) {
		this.R15_COLUMN_K = R15_COLUMN_K;
	}

	public BigDecimal getR15_COLUMN_L() {
		return R15_COLUMN_L;
	}

	public void setR15_COLUMN_L(BigDecimal R15_COLUMN_L) {
		this.R15_COLUMN_L = R15_COLUMN_L;
	}

	public BigDecimal getR15_COLUMN_M() {
		return R15_COLUMN_M;
	}

	public void setR15_COLUMN_M(BigDecimal R15_COLUMN_M) {
		this.R15_COLUMN_M = R15_COLUMN_M;
	}

	public BigDecimal getR15_COLUMN_N() {
		return R15_COLUMN_N;
	}

	public void setR15_COLUMN_N(BigDecimal R15_COLUMN_N) {
		this.R15_COLUMN_N = R15_COLUMN_N;
	}

	public String getR16_COLUMN_A() {
		return R16_COLUMN_A;
	}

	public void setR16_COLUMN_A(String R16_COLUMN_A) {
		this.R16_COLUMN_A = R16_COLUMN_A;
	}

	public BigDecimal getR16_COLUMN_B() {
		return R16_COLUMN_B;
	}

	public void setR16_COLUMN_B(BigDecimal R16_COLUMN_B) {
		this.R16_COLUMN_B = R16_COLUMN_B;
	}

	public BigDecimal getR16_COLUMN_C() {
		return R16_COLUMN_C;
	}

	public void setR16_COLUMN_C(BigDecimal R16_COLUMN_C) {
		this.R16_COLUMN_C = R16_COLUMN_C;
	}

	public BigDecimal getR16_COLUMN_D() {
		return R16_COLUMN_D;
	}

	public void setR16_COLUMN_D(BigDecimal R16_COLUMN_D) {
		this.R16_COLUMN_D = R16_COLUMN_D;
	}

	public BigDecimal getR16_COLUMN_E() {
		return R16_COLUMN_E;
	}

	public void setR16_COLUMN_E(BigDecimal R16_COLUMN_E) {
		this.R16_COLUMN_E = R16_COLUMN_E;
	}

	public BigDecimal getR16_COLUMN_F() {
		return R16_COLUMN_F;
	}

	public void setR16_COLUMN_F(BigDecimal R16_COLUMN_F) {
		this.R16_COLUMN_F = R16_COLUMN_F;
	}

	public BigDecimal getR16_COLUMN_G() {
		return R16_COLUMN_G;
	}

	public void setR16_COLUMN_G(BigDecimal R16_COLUMN_G) {
		this.R16_COLUMN_G = R16_COLUMN_G;
	}

	public BigDecimal getR16_COLUMN_H() {
		return R16_COLUMN_H;
	}

	public void setR16_COLUMN_H(BigDecimal R16_COLUMN_H) {
		this.R16_COLUMN_H = R16_COLUMN_H;
	}

	public BigDecimal getR16_COLUMN_I() {
		return R16_COLUMN_I;
	}

	public void setR16_COLUMN_I(BigDecimal R16_COLUMN_I) {
		this.R16_COLUMN_I = R16_COLUMN_I;
	}

	public BigDecimal getR16_COLUMN_J() {
		return R16_COLUMN_J;
	}

	public void setR16_COLUMN_J(BigDecimal R16_COLUMN_J) {
		this.R16_COLUMN_J = R16_COLUMN_J;
	}

	public BigDecimal getR16_COLUMN_K() {
		return R16_COLUMN_K;
	}

	public void setR16_COLUMN_K(BigDecimal R16_COLUMN_K) {
		this.R16_COLUMN_K = R16_COLUMN_K;
	}

	public BigDecimal getR16_COLUMN_L() {
		return R16_COLUMN_L;
	}

	public void setR16_COLUMN_L(BigDecimal R16_COLUMN_L) {
		this.R16_COLUMN_L = R16_COLUMN_L;
	}

	public BigDecimal getR16_COLUMN_M() {
		return R16_COLUMN_M;
	}

	public void setR16_COLUMN_M(BigDecimal R16_COLUMN_M) {
		this.R16_COLUMN_M = R16_COLUMN_M;
	}

	public BigDecimal getR16_COLUMN_N() {
		return R16_COLUMN_N;
	}

	public void setR16_COLUMN_N(BigDecimal R16_COLUMN_N) {
		this.R16_COLUMN_N = R16_COLUMN_N;
	}

	public String getR17_COLUMN_A() {
		return R17_COLUMN_A;
	}

	public void setR17_COLUMN_A(String R17_COLUMN_A) {
		this.R17_COLUMN_A = R17_COLUMN_A;
	}

	public BigDecimal getR17_COLUMN_B() {
		return R17_COLUMN_B;
	}

	public void setR17_COLUMN_B(BigDecimal R17_COLUMN_B) {
		this.R17_COLUMN_B = R17_COLUMN_B;
	}

	public BigDecimal getR17_COLUMN_C() {
		return R17_COLUMN_C;
	}

	public void setR17_COLUMN_C(BigDecimal R17_COLUMN_C) {
		this.R17_COLUMN_C = R17_COLUMN_C;
	}

	public BigDecimal getR17_COLUMN_D() {
		return R17_COLUMN_D;
	}

	public void setR17_COLUMN_D(BigDecimal R17_COLUMN_D) {
		this.R17_COLUMN_D = R17_COLUMN_D;
	}

	public BigDecimal getR17_COLUMN_E() {
		return R17_COLUMN_E;
	}

	public void setR17_COLUMN_E(BigDecimal R17_COLUMN_E) {
		this.R17_COLUMN_E = R17_COLUMN_E;
	}

	public BigDecimal getR17_COLUMN_F() {
		return R17_COLUMN_F;
	}

	public void setR17_COLUMN_F(BigDecimal R17_COLUMN_F) {
		this.R17_COLUMN_F = R17_COLUMN_F;
	}

	public BigDecimal getR17_COLUMN_G() {
		return R17_COLUMN_G;
	}

	public void setR17_COLUMN_G(BigDecimal R17_COLUMN_G) {
		this.R17_COLUMN_G = R17_COLUMN_G;
	}

	public BigDecimal getR17_COLUMN_H() {
		return R17_COLUMN_H;
	}

	public void setR17_COLUMN_H(BigDecimal R17_COLUMN_H) {
		this.R17_COLUMN_H = R17_COLUMN_H;
	}

	public BigDecimal getR17_COLUMN_I() {
		return R17_COLUMN_I;
	}

	public void setR17_COLUMN_I(BigDecimal R17_COLUMN_I) {
		this.R17_COLUMN_I = R17_COLUMN_I;
	}

	public BigDecimal getR17_COLUMN_J() {
		return R17_COLUMN_J;
	}

	public void setR17_COLUMN_J(BigDecimal R17_COLUMN_J) {
		this.R17_COLUMN_J = R17_COLUMN_J;
	}

	public BigDecimal getR17_COLUMN_K() {
		return R17_COLUMN_K;
	}

	public void setR17_COLUMN_K(BigDecimal R17_COLUMN_K) {
		this.R17_COLUMN_K = R17_COLUMN_K;
	}

	public BigDecimal getR17_COLUMN_L() {
		return R17_COLUMN_L;
	}

	public void setR17_COLUMN_L(BigDecimal R17_COLUMN_L) {
		this.R17_COLUMN_L = R17_COLUMN_L;
	}

	public BigDecimal getR17_COLUMN_M() {
		return R17_COLUMN_M;
	}

	public void setR17_COLUMN_M(BigDecimal R17_COLUMN_M) {
		this.R17_COLUMN_M = R17_COLUMN_M;
	}

	public BigDecimal getR17_COLUMN_N() {
		return R17_COLUMN_N;
	}

	public void setR17_COLUMN_N(BigDecimal R17_COLUMN_N) {
		this.R17_COLUMN_N = R17_COLUMN_N;
	}

	public String getR18_COLUMN_A() {
		return R18_COLUMN_A;
	}

	public void setR18_COLUMN_A(String R18_COLUMN_A) {
		this.R18_COLUMN_A = R18_COLUMN_A;
	}

	public BigDecimal getR18_COLUMN_B() {
		return R18_COLUMN_B;
	}

	public void setR18_COLUMN_B(BigDecimal R18_COLUMN_B) {
		this.R18_COLUMN_B = R18_COLUMN_B;
	}

	public BigDecimal getR18_COLUMN_C() {
		return R18_COLUMN_C;
	}

	public void setR18_COLUMN_C(BigDecimal R18_COLUMN_C) {
		this.R18_COLUMN_C = R18_COLUMN_C;
	}

	public BigDecimal getR18_COLUMN_D() {
		return R18_COLUMN_D;
	}

	public void setR18_COLUMN_D(BigDecimal R18_COLUMN_D) {
		this.R18_COLUMN_D = R18_COLUMN_D;
	}

	public BigDecimal getR18_COLUMN_E() {
		return R18_COLUMN_E;
	}

	public void setR18_COLUMN_E(BigDecimal R18_COLUMN_E) {
		this.R18_COLUMN_E = R18_COLUMN_E;
	}

	public BigDecimal getR18_COLUMN_F() {
		return R18_COLUMN_F;
	}

	public void setR18_COLUMN_F(BigDecimal R18_COLUMN_F) {
		this.R18_COLUMN_F = R18_COLUMN_F;
	}

	public BigDecimal getR18_COLUMN_G() {
		return R18_COLUMN_G;
	}

	public void setR18_COLUMN_G(BigDecimal R18_COLUMN_G) {
		this.R18_COLUMN_G = R18_COLUMN_G;
	}

	public BigDecimal getR18_COLUMN_H() {
		return R18_COLUMN_H;
	}

	public void setR18_COLUMN_H(BigDecimal R18_COLUMN_H) {
		this.R18_COLUMN_H = R18_COLUMN_H;
	}

	public BigDecimal getR18_COLUMN_I() {
		return R18_COLUMN_I;
	}

	public void setR18_COLUMN_I(BigDecimal R18_COLUMN_I) {
		this.R18_COLUMN_I = R18_COLUMN_I;
	}

	public BigDecimal getR18_COLUMN_J() {
		return R18_COLUMN_J;
	}

	public void setR18_COLUMN_J(BigDecimal R18_COLUMN_J) {
		this.R18_COLUMN_J = R18_COLUMN_J;
	}

	public BigDecimal getR18_COLUMN_K() {
		return R18_COLUMN_K;
	}

	public void setR18_COLUMN_K(BigDecimal R18_COLUMN_K) {
		this.R18_COLUMN_K = R18_COLUMN_K;
	}

	public BigDecimal getR18_COLUMN_L() {
		return R18_COLUMN_L;
	}

	public void setR18_COLUMN_L(BigDecimal R18_COLUMN_L) {
		this.R18_COLUMN_L = R18_COLUMN_L;
	}

	public BigDecimal getR18_COLUMN_M() {
		return R18_COLUMN_M;
	}

	public void setR18_COLUMN_M(BigDecimal R18_COLUMN_M) {
		this.R18_COLUMN_M = R18_COLUMN_M;
	}

	public BigDecimal getR18_COLUMN_N() {
		return R18_COLUMN_N;
	}

	public void setR18_COLUMN_N(BigDecimal R18_COLUMN_N) {
		this.R18_COLUMN_N = R18_COLUMN_N;
	}

	public String getR19_COLUMN_A() {
		return R19_COLUMN_A;
	}

	public void setR19_COLUMN_A(String R19_COLUMN_A) {
		this.R19_COLUMN_A = R19_COLUMN_A;
	}

	public BigDecimal getR19_COLUMN_B() {
		return R19_COLUMN_B;
	}

	public void setR19_COLUMN_B(BigDecimal R19_COLUMN_B) {
		this.R19_COLUMN_B = R19_COLUMN_B;
	}

	public BigDecimal getR19_COLUMN_C() {
		return R19_COLUMN_C;
	}

	public void setR19_COLUMN_C(BigDecimal R19_COLUMN_C) {
		this.R19_COLUMN_C = R19_COLUMN_C;
	}

	public BigDecimal getR19_COLUMN_D() {
		return R19_COLUMN_D;
	}

	public void setR19_COLUMN_D(BigDecimal R19_COLUMN_D) {
		this.R19_COLUMN_D = R19_COLUMN_D;
	}

	public BigDecimal getR19_COLUMN_E() {
		return R19_COLUMN_E;
	}

	public void setR19_COLUMN_E(BigDecimal R19_COLUMN_E) {
		this.R19_COLUMN_E = R19_COLUMN_E;
	}

	public BigDecimal getR19_COLUMN_F() {
		return R19_COLUMN_F;
	}

	public void setR19_COLUMN_F(BigDecimal R19_COLUMN_F) {
		this.R19_COLUMN_F = R19_COLUMN_F;
	}

	public BigDecimal getR19_COLUMN_G() {
		return R19_COLUMN_G;
	}

	public void setR19_COLUMN_G(BigDecimal R19_COLUMN_G) {
		this.R19_COLUMN_G = R19_COLUMN_G;
	}

	public BigDecimal getR19_COLUMN_H() {
		return R19_COLUMN_H;
	}

	public void setR19_COLUMN_H(BigDecimal R19_COLUMN_H) {
		this.R19_COLUMN_H = R19_COLUMN_H;
	}

	public BigDecimal getR19_COLUMN_I() {
		return R19_COLUMN_I;
	}

	public void setR19_COLUMN_I(BigDecimal R19_COLUMN_I) {
		this.R19_COLUMN_I = R19_COLUMN_I;
	}

	public BigDecimal getR19_COLUMN_J() {
		return R19_COLUMN_J;
	}

	public void setR19_COLUMN_J(BigDecimal R19_COLUMN_J) {
		this.R19_COLUMN_J = R19_COLUMN_J;
	}

	public BigDecimal getR19_COLUMN_K() {
		return R19_COLUMN_K;
	}

	public void setR19_COLUMN_K(BigDecimal R19_COLUMN_K) {
		this.R19_COLUMN_K = R19_COLUMN_K;
	}

	public BigDecimal getR19_COLUMN_L() {
		return R19_COLUMN_L;
	}

	public void setR19_COLUMN_L(BigDecimal R19_COLUMN_L) {
		this.R19_COLUMN_L = R19_COLUMN_L;
	}

	public BigDecimal getR19_COLUMN_M() {
		return R19_COLUMN_M;
	}

	public void setR19_COLUMN_M(BigDecimal R19_COLUMN_M) {
		this.R19_COLUMN_M = R19_COLUMN_M;
	}

	public BigDecimal getR19_COLUMN_N() {
		return R19_COLUMN_N;
	}

	public void setR19_COLUMN_N(BigDecimal R19_COLUMN_N) {
		this.R19_COLUMN_N = R19_COLUMN_N;
	}

	public String getR20_COLUMN_A() {
		return R20_COLUMN_A;
	}

	public void setR20_COLUMN_A(String R20_COLUMN_A) {
		this.R20_COLUMN_A = R20_COLUMN_A;
	}

	public BigDecimal getR20_COLUMN_B() {
		return R20_COLUMN_B;
	}

	public void setR20_COLUMN_B(BigDecimal R20_COLUMN_B) {
		this.R20_COLUMN_B = R20_COLUMN_B;
	}

	public BigDecimal getR20_COLUMN_C() {
		return R20_COLUMN_C;
	}

	public void setR20_COLUMN_C(BigDecimal R20_COLUMN_C) {
		this.R20_COLUMN_C = R20_COLUMN_C;
	}

	public BigDecimal getR20_COLUMN_D() {
		return R20_COLUMN_D;
	}

	public void setR20_COLUMN_D(BigDecimal R20_COLUMN_D) {
		this.R20_COLUMN_D = R20_COLUMN_D;
	}

	public BigDecimal getR20_COLUMN_E() {
		return R20_COLUMN_E;
	}

	public void setR20_COLUMN_E(BigDecimal R20_COLUMN_E) {
		this.R20_COLUMN_E = R20_COLUMN_E;
	}

	public BigDecimal getR20_COLUMN_F() {
		return R20_COLUMN_F;
	}

	public void setR20_COLUMN_F(BigDecimal R20_COLUMN_F) {
		this.R20_COLUMN_F = R20_COLUMN_F;
	}

	public BigDecimal getR20_COLUMN_G() {
		return R20_COLUMN_G;
	}

	public void setR20_COLUMN_G(BigDecimal R20_COLUMN_G) {
		this.R20_COLUMN_G = R20_COLUMN_G;
	}

	public BigDecimal getR20_COLUMN_H() {
		return R20_COLUMN_H;
	}

	public void setR20_COLUMN_H(BigDecimal R20_COLUMN_H) {
		this.R20_COLUMN_H = R20_COLUMN_H;
	}

	public BigDecimal getR20_COLUMN_I() {
		return R20_COLUMN_I;
	}

	public void setR20_COLUMN_I(BigDecimal R20_COLUMN_I) {
		this.R20_COLUMN_I = R20_COLUMN_I;
	}

	public BigDecimal getR20_COLUMN_J() {
		return R20_COLUMN_J;
	}

	public void setR20_COLUMN_J(BigDecimal R20_COLUMN_J) {
		this.R20_COLUMN_J = R20_COLUMN_J;
	}

	public BigDecimal getR20_COLUMN_K() {
		return R20_COLUMN_K;
	}

	public void setR20_COLUMN_K(BigDecimal R20_COLUMN_K) {
		this.R20_COLUMN_K = R20_COLUMN_K;
	}

	public BigDecimal getR20_COLUMN_L() {
		return R20_COLUMN_L;
	}

	public void setR20_COLUMN_L(BigDecimal R20_COLUMN_L) {
		this.R20_COLUMN_L = R20_COLUMN_L;
	}

	public BigDecimal getR20_COLUMN_M() {
		return R20_COLUMN_M;
	}

	public void setR20_COLUMN_M(BigDecimal R20_COLUMN_M) {
		this.R20_COLUMN_M = R20_COLUMN_M;
	}

	public BigDecimal getR20_COLUMN_N() {
		return R20_COLUMN_N;
	}

	public void setR20_COLUMN_N(BigDecimal R20_COLUMN_N) {
		this.R20_COLUMN_N = R20_COLUMN_N;
	}

	public String getR21_COLUMN_A() {
		return R21_COLUMN_A;
	}

	public void setR21_COLUMN_A(String R21_COLUMN_A) {
		this.R21_COLUMN_A = R21_COLUMN_A;
	}

	public BigDecimal getR21_COLUMN_B() {
		return R21_COLUMN_B;
	}

	public void setR21_COLUMN_B(BigDecimal R21_COLUMN_B) {
		this.R21_COLUMN_B = R21_COLUMN_B;
	}

	public BigDecimal getR21_COLUMN_C() {
		return R21_COLUMN_C;
	}

	public void setR21_COLUMN_C(BigDecimal R21_COLUMN_C) {
		this.R21_COLUMN_C = R21_COLUMN_C;
	}

	public BigDecimal getR21_COLUMN_D() {
		return R21_COLUMN_D;
	}

	public void setR21_COLUMN_D(BigDecimal R21_COLUMN_D) {
		this.R21_COLUMN_D = R21_COLUMN_D;
	}

	public BigDecimal getR21_COLUMN_E() {
		return R21_COLUMN_E;
	}

	public void setR21_COLUMN_E(BigDecimal R21_COLUMN_E) {
		this.R21_COLUMN_E = R21_COLUMN_E;
	}

	public BigDecimal getR21_COLUMN_F() {
		return R21_COLUMN_F;
	}

	public void setR21_COLUMN_F(BigDecimal R21_COLUMN_F) {
		this.R21_COLUMN_F = R21_COLUMN_F;
	}

	public BigDecimal getR21_COLUMN_G() {
		return R21_COLUMN_G;
	}

	public void setR21_COLUMN_G(BigDecimal R21_COLUMN_G) {
		this.R21_COLUMN_G = R21_COLUMN_G;
	}

	public BigDecimal getR21_COLUMN_H() {
		return R21_COLUMN_H;
	}

	public void setR21_COLUMN_H(BigDecimal R21_COLUMN_H) {
		this.R21_COLUMN_H = R21_COLUMN_H;
	}

	public BigDecimal getR21_COLUMN_I() {
		return R21_COLUMN_I;
	}

	public void setR21_COLUMN_I(BigDecimal R21_COLUMN_I) {
		this.R21_COLUMN_I = R21_COLUMN_I;
	}

	public BigDecimal getR21_COLUMN_J() {
		return R21_COLUMN_J;
	}

	public void setR21_COLUMN_J(BigDecimal R21_COLUMN_J) {
		this.R21_COLUMN_J = R21_COLUMN_J;
	}

	public BigDecimal getR21_COLUMN_K() {
		return R21_COLUMN_K;
	}

	public void setR21_COLUMN_K(BigDecimal R21_COLUMN_K) {
		this.R21_COLUMN_K = R21_COLUMN_K;
	}

	public BigDecimal getR21_COLUMN_L() {
		return R21_COLUMN_L;
	}

	public void setR21_COLUMN_L(BigDecimal R21_COLUMN_L) {
		this.R21_COLUMN_L = R21_COLUMN_L;
	}

	public BigDecimal getR21_COLUMN_M() {
		return R21_COLUMN_M;
	}

	public void setR21_COLUMN_M(BigDecimal R21_COLUMN_M) {
		this.R21_COLUMN_M = R21_COLUMN_M;
	}

	public BigDecimal getR21_COLUMN_N() {
		return R21_COLUMN_N;
	}

	public void setR21_COLUMN_N(BigDecimal R21_COLUMN_N) {
		this.R21_COLUMN_N = R21_COLUMN_N;
	}

	public String getR22_COLUMN_A() {
		return R22_COLUMN_A;
	}

	public void setR22_COLUMN_A(String R22_COLUMN_A) {
		this.R22_COLUMN_A = R22_COLUMN_A;
	}

	public BigDecimal getR22_COLUMN_B() {
		return R22_COLUMN_B;
	}

	public void setR22_COLUMN_B(BigDecimal R22_COLUMN_B) {
		this.R22_COLUMN_B = R22_COLUMN_B;
	}

	public BigDecimal getR22_COLUMN_C() {
		return R22_COLUMN_C;
	}

	public void setR22_COLUMN_C(BigDecimal R22_COLUMN_C) {
		this.R22_COLUMN_C = R22_COLUMN_C;
	}

	public BigDecimal getR22_COLUMN_D() {
		return R22_COLUMN_D;
	}

	public void setR22_COLUMN_D(BigDecimal R22_COLUMN_D) {
		this.R22_COLUMN_D = R22_COLUMN_D;
	}

	public BigDecimal getR22_COLUMN_E() {
		return R22_COLUMN_E;
	}

	public void setR22_COLUMN_E(BigDecimal R22_COLUMN_E) {
		this.R22_COLUMN_E = R22_COLUMN_E;
	}

	public BigDecimal getR22_COLUMN_F() {
		return R22_COLUMN_F;
	}

	public void setR22_COLUMN_F(BigDecimal R22_COLUMN_F) {
		this.R22_COLUMN_F = R22_COLUMN_F;
	}

	public BigDecimal getR22_COLUMN_G() {
		return R22_COLUMN_G;
	}

	public void setR22_COLUMN_G(BigDecimal R22_COLUMN_G) {
		this.R22_COLUMN_G = R22_COLUMN_G;
	}

	public BigDecimal getR22_COLUMN_H() {
		return R22_COLUMN_H;
	}

	public void setR22_COLUMN_H(BigDecimal R22_COLUMN_H) {
		this.R22_COLUMN_H = R22_COLUMN_H;
	}

	public BigDecimal getR22_COLUMN_I() {
		return R22_COLUMN_I;
	}

	public void setR22_COLUMN_I(BigDecimal R22_COLUMN_I) {
		this.R22_COLUMN_I = R22_COLUMN_I;
	}

	public BigDecimal getR22_COLUMN_J() {
		return R22_COLUMN_J;
	}

	public void setR22_COLUMN_J(BigDecimal R22_COLUMN_J) {
		this.R22_COLUMN_J = R22_COLUMN_J;
	}

	public BigDecimal getR22_COLUMN_K() {
		return R22_COLUMN_K;
	}

	public void setR22_COLUMN_K(BigDecimal R22_COLUMN_K) {
		this.R22_COLUMN_K = R22_COLUMN_K;
	}

	public BigDecimal getR22_COLUMN_L() {
		return R22_COLUMN_L;
	}

	public void setR22_COLUMN_L(BigDecimal R22_COLUMN_L) {
		this.R22_COLUMN_L = R22_COLUMN_L;
	}

	public BigDecimal getR22_COLUMN_M() {
		return R22_COLUMN_M;
	}

	public void setR22_COLUMN_M(BigDecimal R22_COLUMN_M) {
		this.R22_COLUMN_M = R22_COLUMN_M;
	}

	public BigDecimal getR22_COLUMN_N() {
		return R22_COLUMN_N;
	}

	public void setR22_COLUMN_N(BigDecimal R22_COLUMN_N) {
		this.R22_COLUMN_N = R22_COLUMN_N;
	}

	public String getR23_COLUMN_A() {
		return R23_COLUMN_A;
	}

	public void setR23_COLUMN_A(String R23_COLUMN_A) {
		this.R23_COLUMN_A = R23_COLUMN_A;
	}

	public BigDecimal getR23_COLUMN_B() {
		return R23_COLUMN_B;
	}

	public void setR23_COLUMN_B(BigDecimal R23_COLUMN_B) {
		this.R23_COLUMN_B = R23_COLUMN_B;
	}

	public BigDecimal getR23_COLUMN_C() {
		return R23_COLUMN_C;
	}

	public void setR23_COLUMN_C(BigDecimal R23_COLUMN_C) {
		this.R23_COLUMN_C = R23_COLUMN_C;
	}

	public BigDecimal getR23_COLUMN_D() {
		return R23_COLUMN_D;
	}

	public void setR23_COLUMN_D(BigDecimal R23_COLUMN_D) {
		this.R23_COLUMN_D = R23_COLUMN_D;
	}

	public BigDecimal getR23_COLUMN_E() {
		return R23_COLUMN_E;
	}

	public void setR23_COLUMN_E(BigDecimal R23_COLUMN_E) {
		this.R23_COLUMN_E = R23_COLUMN_E;
	}

	public BigDecimal getR23_COLUMN_F() {
		return R23_COLUMN_F;
	}

	public void setR23_COLUMN_F(BigDecimal R23_COLUMN_F) {
		this.R23_COLUMN_F = R23_COLUMN_F;
	}

	public BigDecimal getR23_COLUMN_G() {
		return R23_COLUMN_G;
	}

	public void setR23_COLUMN_G(BigDecimal R23_COLUMN_G) {
		this.R23_COLUMN_G = R23_COLUMN_G;
	}

	public BigDecimal getR23_COLUMN_H() {
		return R23_COLUMN_H;
	}

	public void setR23_COLUMN_H(BigDecimal R23_COLUMN_H) {
		this.R23_COLUMN_H = R23_COLUMN_H;
	}

	public BigDecimal getR23_COLUMN_I() {
		return R23_COLUMN_I;
	}

	public void setR23_COLUMN_I(BigDecimal R23_COLUMN_I) {
		this.R23_COLUMN_I = R23_COLUMN_I;
	}

	public BigDecimal getR23_COLUMN_J() {
		return R23_COLUMN_J;
	}

	public void setR23_COLUMN_J(BigDecimal R23_COLUMN_J) {
		this.R23_COLUMN_J = R23_COLUMN_J;
	}

	public BigDecimal getR23_COLUMN_K() {
		return R23_COLUMN_K;
	}

	public void setR23_COLUMN_K(BigDecimal R23_COLUMN_K) {
		this.R23_COLUMN_K = R23_COLUMN_K;
	}

	public BigDecimal getR23_COLUMN_L() {
		return R23_COLUMN_L;
	}

	public void setR23_COLUMN_L(BigDecimal R23_COLUMN_L) {
		this.R23_COLUMN_L = R23_COLUMN_L;
	}

	public BigDecimal getR23_COLUMN_M() {
		return R23_COLUMN_M;
	}

	public void setR23_COLUMN_M(BigDecimal R23_COLUMN_M) {
		this.R23_COLUMN_M = R23_COLUMN_M;
	}

	public BigDecimal getR23_COLUMN_N() {
		return R23_COLUMN_N;
	}

	public void setR23_COLUMN_N(BigDecimal R23_COLUMN_N) {
		this.R23_COLUMN_N = R23_COLUMN_N;
	}

	public String getR24_COLUMN_A() {
		return R24_COLUMN_A;
	}

	public void setR24_COLUMN_A(String R24_COLUMN_A) {
		this.R24_COLUMN_A = R24_COLUMN_A;
	}

	public BigDecimal getR24_COLUMN_B() {
		return R24_COLUMN_B;
	}

	public void setR24_COLUMN_B(BigDecimal R24_COLUMN_B) {
		this.R24_COLUMN_B = R24_COLUMN_B;
	}

	public BigDecimal getR24_COLUMN_C() {
		return R24_COLUMN_C;
	}

	public void setR24_COLUMN_C(BigDecimal R24_COLUMN_C) {
		this.R24_COLUMN_C = R24_COLUMN_C;
	}

	public BigDecimal getR24_COLUMN_D() {
		return R24_COLUMN_D;
	}

	public void setR24_COLUMN_D(BigDecimal R24_COLUMN_D) {
		this.R24_COLUMN_D = R24_COLUMN_D;
	}

	public BigDecimal getR24_COLUMN_E() {
		return R24_COLUMN_E;
	}

	public void setR24_COLUMN_E(BigDecimal R24_COLUMN_E) {
		this.R24_COLUMN_E = R24_COLUMN_E;
	}

	public BigDecimal getR24_COLUMN_F() {
		return R24_COLUMN_F;
	}

	public void setR24_COLUMN_F(BigDecimal R24_COLUMN_F) {
		this.R24_COLUMN_F = R24_COLUMN_F;
	}

	public BigDecimal getR24_COLUMN_G() {
		return R24_COLUMN_G;
	}

	public void setR24_COLUMN_G(BigDecimal R24_COLUMN_G) {
		this.R24_COLUMN_G = R24_COLUMN_G;
	}

	public BigDecimal getR24_COLUMN_H() {
		return R24_COLUMN_H;
	}

	public void setR24_COLUMN_H(BigDecimal R24_COLUMN_H) {
		this.R24_COLUMN_H = R24_COLUMN_H;
	}

	public BigDecimal getR24_COLUMN_I() {
		return R24_COLUMN_I;
	}

	public void setR24_COLUMN_I(BigDecimal R24_COLUMN_I) {
		this.R24_COLUMN_I = R24_COLUMN_I;
	}

	public BigDecimal getR24_COLUMN_J() {
		return R24_COLUMN_J;
	}

	public void setR24_COLUMN_J(BigDecimal R24_COLUMN_J) {
		this.R24_COLUMN_J = R24_COLUMN_J;
	}

	public BigDecimal getR24_COLUMN_K() {
		return R24_COLUMN_K;
	}

	public void setR24_COLUMN_K(BigDecimal R24_COLUMN_K) {
		this.R24_COLUMN_K = R24_COLUMN_K;
	}

	public BigDecimal getR24_COLUMN_L() {
		return R24_COLUMN_L;
	}

	public void setR24_COLUMN_L(BigDecimal R24_COLUMN_L) {
		this.R24_COLUMN_L = R24_COLUMN_L;
	}

	public BigDecimal getR24_COLUMN_M() {
		return R24_COLUMN_M;
	}

	public void setR24_COLUMN_M(BigDecimal R24_COLUMN_M) {
		this.R24_COLUMN_M = R24_COLUMN_M;
	}

	public BigDecimal getR24_COLUMN_N() {
		return R24_COLUMN_N;
	}

	public void setR24_COLUMN_N(BigDecimal R24_COLUMN_N) {
		this.R24_COLUMN_N = R24_COLUMN_N;
	}

	public String getR25_COLUMN_A() {
		return R25_COLUMN_A;
	}

	public void setR25_COLUMN_A(String R25_COLUMN_A) {
		this.R25_COLUMN_A = R25_COLUMN_A;
	}

	public BigDecimal getR25_COLUMN_B() {
		return R25_COLUMN_B;
	}

	public void setR25_COLUMN_B(BigDecimal R25_COLUMN_B) {
		this.R25_COLUMN_B = R25_COLUMN_B;
	}

	public BigDecimal getR25_COLUMN_C() {
		return R25_COLUMN_C;
	}

	public void setR25_COLUMN_C(BigDecimal R25_COLUMN_C) {
		this.R25_COLUMN_C = R25_COLUMN_C;
	}

	public BigDecimal getR25_COLUMN_D() {
		return R25_COLUMN_D;
	}

	public void setR25_COLUMN_D(BigDecimal R25_COLUMN_D) {
		this.R25_COLUMN_D = R25_COLUMN_D;
	}

	public BigDecimal getR25_COLUMN_E() {
		return R25_COLUMN_E;
	}

	public void setR25_COLUMN_E(BigDecimal R25_COLUMN_E) {
		this.R25_COLUMN_E = R25_COLUMN_E;
	}

	public BigDecimal getR25_COLUMN_F() {
		return R25_COLUMN_F;
	}

	public void setR25_COLUMN_F(BigDecimal R25_COLUMN_F) {
		this.R25_COLUMN_F = R25_COLUMN_F;
	}

	public BigDecimal getR25_COLUMN_G() {
		return R25_COLUMN_G;
	}

	public void setR25_COLUMN_G(BigDecimal R25_COLUMN_G) {
		this.R25_COLUMN_G = R25_COLUMN_G;
	}

	public BigDecimal getR25_COLUMN_H() {
		return R25_COLUMN_H;
	}

	public void setR25_COLUMN_H(BigDecimal R25_COLUMN_H) {
		this.R25_COLUMN_H = R25_COLUMN_H;
	}

	public BigDecimal getR25_COLUMN_I() {
		return R25_COLUMN_I;
	}

	public void setR25_COLUMN_I(BigDecimal R25_COLUMN_I) {
		this.R25_COLUMN_I = R25_COLUMN_I;
	}

	public BigDecimal getR25_COLUMN_J() {
		return R25_COLUMN_J;
	}

	public void setR25_COLUMN_J(BigDecimal R25_COLUMN_J) {
		this.R25_COLUMN_J = R25_COLUMN_J;
	}

	public BigDecimal getR25_COLUMN_K() {
		return R25_COLUMN_K;
	}

	public void setR25_COLUMN_K(BigDecimal R25_COLUMN_K) {
		this.R25_COLUMN_K = R25_COLUMN_K;
	}

	public BigDecimal getR25_COLUMN_L() {
		return R25_COLUMN_L;
	}

	public void setR25_COLUMN_L(BigDecimal R25_COLUMN_L) {
		this.R25_COLUMN_L = R25_COLUMN_L;
	}

	public BigDecimal getR25_COLUMN_M() {
		return R25_COLUMN_M;
	}

	public void setR25_COLUMN_M(BigDecimal R25_COLUMN_M) {
		this.R25_COLUMN_M = R25_COLUMN_M;
	}

	public BigDecimal getR25_COLUMN_N() {
		return R25_COLUMN_N;
	}

	public void setR25_COLUMN_N(BigDecimal R25_COLUMN_N) {
		this.R25_COLUMN_N = R25_COLUMN_N;
	}

	public String getR26_COLUMN_A() {
		return R26_COLUMN_A;
	}

	public void setR26_COLUMN_A(String R26_COLUMN_A) {
		this.R26_COLUMN_A = R26_COLUMN_A;
	}

	public BigDecimal getR26_COLUMN_B() {
		return R26_COLUMN_B;
	}

	public void setR26_COLUMN_B(BigDecimal R26_COLUMN_B) {
		this.R26_COLUMN_B = R26_COLUMN_B;
	}

	public BigDecimal getR26_COLUMN_C() {
		return R26_COLUMN_C;
	}

	public void setR26_COLUMN_C(BigDecimal R26_COLUMN_C) {
		this.R26_COLUMN_C = R26_COLUMN_C;
	}

	public BigDecimal getR26_COLUMN_D() {
		return R26_COLUMN_D;
	}

	public void setR26_COLUMN_D(BigDecimal R26_COLUMN_D) {
		this.R26_COLUMN_D = R26_COLUMN_D;
	}

	public BigDecimal getR26_COLUMN_E() {
		return R26_COLUMN_E;
	}

	public void setR26_COLUMN_E(BigDecimal R26_COLUMN_E) {
		this.R26_COLUMN_E = R26_COLUMN_E;
	}

	public BigDecimal getR26_COLUMN_F() {
		return R26_COLUMN_F;
	}

	public void setR26_COLUMN_F(BigDecimal R26_COLUMN_F) {
		this.R26_COLUMN_F = R26_COLUMN_F;
	}

	public BigDecimal getR26_COLUMN_G() {
		return R26_COLUMN_G;
	}

	public void setR26_COLUMN_G(BigDecimal R26_COLUMN_G) {
		this.R26_COLUMN_G = R26_COLUMN_G;
	}

	public BigDecimal getR26_COLUMN_H() {
		return R26_COLUMN_H;
	}

	public void setR26_COLUMN_H(BigDecimal R26_COLUMN_H) {
		this.R26_COLUMN_H = R26_COLUMN_H;
	}

	public BigDecimal getR26_COLUMN_I() {
		return R26_COLUMN_I;
	}

	public void setR26_COLUMN_I(BigDecimal R26_COLUMN_I) {
		this.R26_COLUMN_I = R26_COLUMN_I;
	}

	public BigDecimal getR26_COLUMN_J() {
		return R26_COLUMN_J;
	}

	public void setR26_COLUMN_J(BigDecimal R26_COLUMN_J) {
		this.R26_COLUMN_J = R26_COLUMN_J;
	}

	public BigDecimal getR26_COLUMN_K() {
		return R26_COLUMN_K;
	}

	public void setR26_COLUMN_K(BigDecimal R26_COLUMN_K) {
		this.R26_COLUMN_K = R26_COLUMN_K;
	}

	public BigDecimal getR26_COLUMN_L() {
		return R26_COLUMN_L;
	}

	public void setR26_COLUMN_L(BigDecimal R26_COLUMN_L) {
		this.R26_COLUMN_L = R26_COLUMN_L;
	}

	public BigDecimal getR26_COLUMN_M() {
		return R26_COLUMN_M;
	}

	public void setR26_COLUMN_M(BigDecimal R26_COLUMN_M) {
		this.R26_COLUMN_M = R26_COLUMN_M;
	}

	public BigDecimal getR26_COLUMN_N() {
		return R26_COLUMN_N;
	}

	public void setR26_COLUMN_N(BigDecimal R26_COLUMN_N) {
		this.R26_COLUMN_N = R26_COLUMN_N;
	}

	public String getR27_COLUMN_A() {
		return R27_COLUMN_A;
	}

	public void setR27_COLUMN_A(String R27_COLUMN_A) {
		this.R27_COLUMN_A = R27_COLUMN_A;
	}

	public BigDecimal getR27_COLUMN_B() {
		return R27_COLUMN_B;
	}

	public void setR27_COLUMN_B(BigDecimal R27_COLUMN_B) {
		this.R27_COLUMN_B = R27_COLUMN_B;
	}

	public BigDecimal getR27_COLUMN_C() {
		return R27_COLUMN_C;
	}

	public void setR27_COLUMN_C(BigDecimal R27_COLUMN_C) {
		this.R27_COLUMN_C = R27_COLUMN_C;
	}

	public BigDecimal getR27_COLUMN_D() {
		return R27_COLUMN_D;
	}

	public void setR27_COLUMN_D(BigDecimal R27_COLUMN_D) {
		this.R27_COLUMN_D = R27_COLUMN_D;
	}

	public BigDecimal getR27_COLUMN_E() {
		return R27_COLUMN_E;
	}

	public void setR27_COLUMN_E(BigDecimal R27_COLUMN_E) {
		this.R27_COLUMN_E = R27_COLUMN_E;
	}

	public BigDecimal getR27_COLUMN_F() {
		return R27_COLUMN_F;
	}

	public void setR27_COLUMN_F(BigDecimal R27_COLUMN_F) {
		this.R27_COLUMN_F = R27_COLUMN_F;
	}

	public BigDecimal getR27_COLUMN_G() {
		return R27_COLUMN_G;
	}

	public void setR27_COLUMN_G(BigDecimal R27_COLUMN_G) {
		this.R27_COLUMN_G = R27_COLUMN_G;
	}

	public BigDecimal getR27_COLUMN_H() {
		return R27_COLUMN_H;
	}

	public void setR27_COLUMN_H(BigDecimal R27_COLUMN_H) {
		this.R27_COLUMN_H = R27_COLUMN_H;
	}

	public BigDecimal getR27_COLUMN_I() {
		return R27_COLUMN_I;
	}

	public void setR27_COLUMN_I(BigDecimal R27_COLUMN_I) {
		this.R27_COLUMN_I = R27_COLUMN_I;
	}

	public BigDecimal getR27_COLUMN_J() {
		return R27_COLUMN_J;
	}

	public void setR27_COLUMN_J(BigDecimal R27_COLUMN_J) {
		this.R27_COLUMN_J = R27_COLUMN_J;
	}

	public BigDecimal getR27_COLUMN_K() {
		return R27_COLUMN_K;
	}

	public void setR27_COLUMN_K(BigDecimal R27_COLUMN_K) {
		this.R27_COLUMN_K = R27_COLUMN_K;
	}

	public BigDecimal getR27_COLUMN_L() {
		return R27_COLUMN_L;
	}

	public void setR27_COLUMN_L(BigDecimal R27_COLUMN_L) {
		this.R27_COLUMN_L = R27_COLUMN_L;
	}

	public BigDecimal getR27_COLUMN_M() {
		return R27_COLUMN_M;
	}

	public void setR27_COLUMN_M(BigDecimal R27_COLUMN_M) {
		this.R27_COLUMN_M = R27_COLUMN_M;
	}

	public BigDecimal getR27_COLUMN_N() {
		return R27_COLUMN_N;
	}

	public void setR27_COLUMN_N(BigDecimal R27_COLUMN_N) {
		this.R27_COLUMN_N = R27_COLUMN_N;
	}

	public String getR28_COLUMN_A() {
		return R28_COLUMN_A;
	}

	public void setR28_COLUMN_A(String R28_COLUMN_A) {
		this.R28_COLUMN_A = R28_COLUMN_A;
	}

	public BigDecimal getR28_COLUMN_B() {
		return R28_COLUMN_B;
	}

	public void setR28_COLUMN_B(BigDecimal R28_COLUMN_B) {
		this.R28_COLUMN_B = R28_COLUMN_B;
	}

	public BigDecimal getR28_COLUMN_C() {
		return R28_COLUMN_C;
	}

	public void setR28_COLUMN_C(BigDecimal R28_COLUMN_C) {
		this.R28_COLUMN_C = R28_COLUMN_C;
	}

	public BigDecimal getR28_COLUMN_D() {
		return R28_COLUMN_D;
	}

	public void setR28_COLUMN_D(BigDecimal R28_COLUMN_D) {
		this.R28_COLUMN_D = R28_COLUMN_D;
	}

	public BigDecimal getR28_COLUMN_E() {
		return R28_COLUMN_E;
	}

	public void setR28_COLUMN_E(BigDecimal R28_COLUMN_E) {
		this.R28_COLUMN_E = R28_COLUMN_E;
	}

	public BigDecimal getR28_COLUMN_F() {
		return R28_COLUMN_F;
	}

	public void setR28_COLUMN_F(BigDecimal R28_COLUMN_F) {
		this.R28_COLUMN_F = R28_COLUMN_F;
	}

	public BigDecimal getR28_COLUMN_G() {
		return R28_COLUMN_G;
	}

	public void setR28_COLUMN_G(BigDecimal R28_COLUMN_G) {
		this.R28_COLUMN_G = R28_COLUMN_G;
	}

	public BigDecimal getR28_COLUMN_H() {
		return R28_COLUMN_H;
	}

	public void setR28_COLUMN_H(BigDecimal R28_COLUMN_H) {
		this.R28_COLUMN_H = R28_COLUMN_H;
	}

	public BigDecimal getR28_COLUMN_I() {
		return R28_COLUMN_I;
	}

	public void setR28_COLUMN_I(BigDecimal R28_COLUMN_I) {
		this.R28_COLUMN_I = R28_COLUMN_I;
	}

	public BigDecimal getR28_COLUMN_J() {
		return R28_COLUMN_J;
	}

	public void setR28_COLUMN_J(BigDecimal R28_COLUMN_J) {
		this.R28_COLUMN_J = R28_COLUMN_J;
	}

	public BigDecimal getR28_COLUMN_K() {
		return R28_COLUMN_K;
	}

	public void setR28_COLUMN_K(BigDecimal R28_COLUMN_K) {
		this.R28_COLUMN_K = R28_COLUMN_K;
	}

	public BigDecimal getR28_COLUMN_L() {
		return R28_COLUMN_L;
	}

	public void setR28_COLUMN_L(BigDecimal R28_COLUMN_L) {
		this.R28_COLUMN_L = R28_COLUMN_L;
	}

	public BigDecimal getR28_COLUMN_M() {
		return R28_COLUMN_M;
	}

	public void setR28_COLUMN_M(BigDecimal R28_COLUMN_M) {
		this.R28_COLUMN_M = R28_COLUMN_M;
	}

	public BigDecimal getR28_COLUMN_N() {
		return R28_COLUMN_N;
	}

	public void setR28_COLUMN_N(BigDecimal R28_COLUMN_N) {
		this.R28_COLUMN_N = R28_COLUMN_N;
	}

	public String getR29_COLUMN_A() {
		return R29_COLUMN_A;
	}

	public void setR29_COLUMN_A(String R29_COLUMN_A) {
		this.R29_COLUMN_A = R29_COLUMN_A;
	}

	public BigDecimal getR29_COLUMN_B() {
		return R29_COLUMN_B;
	}

	public void setR29_COLUMN_B(BigDecimal R29_COLUMN_B) {
		this.R29_COLUMN_B = R29_COLUMN_B;
	}

	public BigDecimal getR29_COLUMN_C() {
		return R29_COLUMN_C;
	}

	public void setR29_COLUMN_C(BigDecimal R29_COLUMN_C) {
		this.R29_COLUMN_C = R29_COLUMN_C;
	}

	public BigDecimal getR29_COLUMN_D() {
		return R29_COLUMN_D;
	}

	public void setR29_COLUMN_D(BigDecimal R29_COLUMN_D) {
		this.R29_COLUMN_D = R29_COLUMN_D;
	}

	public BigDecimal getR29_COLUMN_E() {
		return R29_COLUMN_E;
	}

	public void setR29_COLUMN_E(BigDecimal R29_COLUMN_E) {
		this.R29_COLUMN_E = R29_COLUMN_E;
	}

	public BigDecimal getR29_COLUMN_F() {
		return R29_COLUMN_F;
	}

	public void setR29_COLUMN_F(BigDecimal R29_COLUMN_F) {
		this.R29_COLUMN_F = R29_COLUMN_F;
	}

	public BigDecimal getR29_COLUMN_G() {
		return R29_COLUMN_G;
	}

	public void setR29_COLUMN_G(BigDecimal R29_COLUMN_G) {
		this.R29_COLUMN_G = R29_COLUMN_G;
	}

	public BigDecimal getR29_COLUMN_H() {
		return R29_COLUMN_H;
	}

	public void setR29_COLUMN_H(BigDecimal R29_COLUMN_H) {
		this.R29_COLUMN_H = R29_COLUMN_H;
	}

	public BigDecimal getR29_COLUMN_I() {
		return R29_COLUMN_I;
	}

	public void setR29_COLUMN_I(BigDecimal R29_COLUMN_I) {
		this.R29_COLUMN_I = R29_COLUMN_I;
	}

	public BigDecimal getR29_COLUMN_J() {
		return R29_COLUMN_J;
	}

	public void setR29_COLUMN_J(BigDecimal R29_COLUMN_J) {
		this.R29_COLUMN_J = R29_COLUMN_J;
	}

	public BigDecimal getR29_COLUMN_K() {
		return R29_COLUMN_K;
	}

	public void setR29_COLUMN_K(BigDecimal R29_COLUMN_K) {
		this.R29_COLUMN_K = R29_COLUMN_K;
	}

	public BigDecimal getR29_COLUMN_L() {
		return R29_COLUMN_L;
	}

	public void setR29_COLUMN_L(BigDecimal R29_COLUMN_L) {
		this.R29_COLUMN_L = R29_COLUMN_L;
	}

	public BigDecimal getR29_COLUMN_M() {
		return R29_COLUMN_M;
	}

	public void setR29_COLUMN_M(BigDecimal R29_COLUMN_M) {
		this.R29_COLUMN_M = R29_COLUMN_M;
	}

	public BigDecimal getR29_COLUMN_N() {
		return R29_COLUMN_N;
	}

	public void setR29_COLUMN_N(BigDecimal R29_COLUMN_N) {
		this.R29_COLUMN_N = R29_COLUMN_N;
	}

	public String getR30_COLUMN_A() {
		return R30_COLUMN_A;
	}

	public void setR30_COLUMN_A(String R30_COLUMN_A) {
		this.R30_COLUMN_A = R30_COLUMN_A;
	}

	public BigDecimal getR30_COLUMN_B() {
		return R30_COLUMN_B;
	}

	public void setR30_COLUMN_B(BigDecimal R30_COLUMN_B) {
		this.R30_COLUMN_B = R30_COLUMN_B;
	}

	public BigDecimal getR30_COLUMN_C() {
		return R30_COLUMN_C;
	}

	public void setR30_COLUMN_C(BigDecimal R30_COLUMN_C) {
		this.R30_COLUMN_C = R30_COLUMN_C;
	}

	public BigDecimal getR30_COLUMN_D() {
		return R30_COLUMN_D;
	}

	public void setR30_COLUMN_D(BigDecimal R30_COLUMN_D) {
		this.R30_COLUMN_D = R30_COLUMN_D;
	}

	public BigDecimal getR30_COLUMN_E() {
		return R30_COLUMN_E;
	}

	public void setR30_COLUMN_E(BigDecimal R30_COLUMN_E) {
		this.R30_COLUMN_E = R30_COLUMN_E;
	}

	public BigDecimal getR30_COLUMN_F() {
		return R30_COLUMN_F;
	}

	public void setR30_COLUMN_F(BigDecimal R30_COLUMN_F) {
		this.R30_COLUMN_F = R30_COLUMN_F;
	}

	public BigDecimal getR30_COLUMN_G() {
		return R30_COLUMN_G;
	}

	public void setR30_COLUMN_G(BigDecimal R30_COLUMN_G) {
		this.R30_COLUMN_G = R30_COLUMN_G;
	}

	public BigDecimal getR30_COLUMN_H() {
		return R30_COLUMN_H;
	}

	public void setR30_COLUMN_H(BigDecimal R30_COLUMN_H) {
		this.R30_COLUMN_H = R30_COLUMN_H;
	}

	public BigDecimal getR30_COLUMN_I() {
		return R30_COLUMN_I;
	}

	public void setR30_COLUMN_I(BigDecimal R30_COLUMN_I) {
		this.R30_COLUMN_I = R30_COLUMN_I;
	}

	public BigDecimal getR30_COLUMN_J() {
		return R30_COLUMN_J;
	}

	public void setR30_COLUMN_J(BigDecimal R30_COLUMN_J) {
		this.R30_COLUMN_J = R30_COLUMN_J;
	}

	public BigDecimal getR30_COLUMN_K() {
		return R30_COLUMN_K;
	}

	public void setR30_COLUMN_K(BigDecimal R30_COLUMN_K) {
		this.R30_COLUMN_K = R30_COLUMN_K;
	}

	public BigDecimal getR30_COLUMN_L() {
		return R30_COLUMN_L;
	}

	public void setR30_COLUMN_L(BigDecimal R30_COLUMN_L) {
		this.R30_COLUMN_L = R30_COLUMN_L;
	}

	public BigDecimal getR30_COLUMN_M() {
		return R30_COLUMN_M;
	}

	public void setR30_COLUMN_M(BigDecimal R30_COLUMN_M) {
		this.R30_COLUMN_M = R30_COLUMN_M;
	}

	public BigDecimal getR30_COLUMN_N() {
		return R30_COLUMN_N;
	}

	public void setR30_COLUMN_N(BigDecimal R30_COLUMN_N) {
		this.R30_COLUMN_N = R30_COLUMN_N;
	}

	public String getR31_COLUMN_A() {
		return R31_COLUMN_A;
	}

	public void setR31_COLUMN_A(String R31_COLUMN_A) {
		this.R31_COLUMN_A = R31_COLUMN_A;
	}

	public BigDecimal getR31_COLUMN_B() {
		return R31_COLUMN_B;
	}

	public void setR31_COLUMN_B(BigDecimal R31_COLUMN_B) {
		this.R31_COLUMN_B = R31_COLUMN_B;
	}

	public BigDecimal getR31_COLUMN_C() {
		return R31_COLUMN_C;
	}

	public void setR31_COLUMN_C(BigDecimal R31_COLUMN_C) {
		this.R31_COLUMN_C = R31_COLUMN_C;
	}

	public BigDecimal getR31_COLUMN_D() {
		return R31_COLUMN_D;
	}

	public void setR31_COLUMN_D(BigDecimal R31_COLUMN_D) {
		this.R31_COLUMN_D = R31_COLUMN_D;
	}

	public BigDecimal getR31_COLUMN_E() {
		return R31_COLUMN_E;
	}

	public void setR31_COLUMN_E(BigDecimal R31_COLUMN_E) {
		this.R31_COLUMN_E = R31_COLUMN_E;
	}

	public BigDecimal getR31_COLUMN_F() {
		return R31_COLUMN_F;
	}

	public void setR31_COLUMN_F(BigDecimal R31_COLUMN_F) {
		this.R31_COLUMN_F = R31_COLUMN_F;
	}

	public BigDecimal getR31_COLUMN_G() {
		return R31_COLUMN_G;
	}

	public void setR31_COLUMN_G(BigDecimal R31_COLUMN_G) {
		this.R31_COLUMN_G = R31_COLUMN_G;
	}

	public BigDecimal getR31_COLUMN_H() {
		return R31_COLUMN_H;
	}

	public void setR31_COLUMN_H(BigDecimal R31_COLUMN_H) {
		this.R31_COLUMN_H = R31_COLUMN_H;
	}

	public BigDecimal getR31_COLUMN_I() {
		return R31_COLUMN_I;
	}

	public void setR31_COLUMN_I(BigDecimal R31_COLUMN_I) {
		this.R31_COLUMN_I = R31_COLUMN_I;
	}

	public BigDecimal getR31_COLUMN_J() {
		return R31_COLUMN_J;
	}

	public void setR31_COLUMN_J(BigDecimal R31_COLUMN_J) {
		this.R31_COLUMN_J = R31_COLUMN_J;
	}

	public BigDecimal getR31_COLUMN_K() {
		return R31_COLUMN_K;
	}

	public void setR31_COLUMN_K(BigDecimal R31_COLUMN_K) {
		this.R31_COLUMN_K = R31_COLUMN_K;
	}

	public BigDecimal getR31_COLUMN_L() {
		return R31_COLUMN_L;
	}

	public void setR31_COLUMN_L(BigDecimal R31_COLUMN_L) {
		this.R31_COLUMN_L = R31_COLUMN_L;
	}

	public BigDecimal getR31_COLUMN_M() {
		return R31_COLUMN_M;
	}

	public void setR31_COLUMN_M(BigDecimal R31_COLUMN_M) {
		this.R31_COLUMN_M = R31_COLUMN_M;
	}

	public BigDecimal getR31_COLUMN_N() {
		return R31_COLUMN_N;
	}

	public void setR31_COLUMN_N(BigDecimal R31_COLUMN_N) {
		this.R31_COLUMN_N = R31_COLUMN_N;
	}

	public String getR32_COLUMN_A() {
		return R32_COLUMN_A;
	}

	public void setR32_COLUMN_A(String R32_COLUMN_A) {
		this.R32_COLUMN_A = R32_COLUMN_A;
	}

	public BigDecimal getR32_COLUMN_B() {
		return R32_COLUMN_B;
	}

	public void setR32_COLUMN_B(BigDecimal R32_COLUMN_B) {
		this.R32_COLUMN_B = R32_COLUMN_B;
	}

	public BigDecimal getR32_COLUMN_C() {
		return R32_COLUMN_C;
	}

	public void setR32_COLUMN_C(BigDecimal R32_COLUMN_C) {
		this.R32_COLUMN_C = R32_COLUMN_C;
	}

	public BigDecimal getR32_COLUMN_D() {
		return R32_COLUMN_D;
	}

	public void setR32_COLUMN_D(BigDecimal R32_COLUMN_D) {
		this.R32_COLUMN_D = R32_COLUMN_D;
	}

	public BigDecimal getR32_COLUMN_E() {
		return R32_COLUMN_E;
	}

	public void setR32_COLUMN_E(BigDecimal R32_COLUMN_E) {
		this.R32_COLUMN_E = R32_COLUMN_E;
	}

	public BigDecimal getR32_COLUMN_F() {
		return R32_COLUMN_F;
	}

	public void setR32_COLUMN_F(BigDecimal R32_COLUMN_F) {
		this.R32_COLUMN_F = R32_COLUMN_F;
	}

	public BigDecimal getR32_COLUMN_G() {
		return R32_COLUMN_G;
	}

	public void setR32_COLUMN_G(BigDecimal R32_COLUMN_G) {
		this.R32_COLUMN_G = R32_COLUMN_G;
	}

	public BigDecimal getR32_COLUMN_H() {
		return R32_COLUMN_H;
	}

	public void setR32_COLUMN_H(BigDecimal R32_COLUMN_H) {
		this.R32_COLUMN_H = R32_COLUMN_H;
	}

	public BigDecimal getR32_COLUMN_I() {
		return R32_COLUMN_I;
	}

	public void setR32_COLUMN_I(BigDecimal R32_COLUMN_I) {
		this.R32_COLUMN_I = R32_COLUMN_I;
	}

	public BigDecimal getR32_COLUMN_J() {
		return R32_COLUMN_J;
	}

	public void setR32_COLUMN_J(BigDecimal R32_COLUMN_J) {
		this.R32_COLUMN_J = R32_COLUMN_J;
	}

	public BigDecimal getR32_COLUMN_K() {
		return R32_COLUMN_K;
	}

	public void setR32_COLUMN_K(BigDecimal R32_COLUMN_K) {
		this.R32_COLUMN_K = R32_COLUMN_K;
	}

	public BigDecimal getR32_COLUMN_L() {
		return R32_COLUMN_L;
	}

	public void setR32_COLUMN_L(BigDecimal R32_COLUMN_L) {
		this.R32_COLUMN_L = R32_COLUMN_L;
	}

	public BigDecimal getR32_COLUMN_M() {
		return R32_COLUMN_M;
	}

	public void setR32_COLUMN_M(BigDecimal R32_COLUMN_M) {
		this.R32_COLUMN_M = R32_COLUMN_M;
	}

	public BigDecimal getR32_COLUMN_N() {
		return R32_COLUMN_N;
	}

	public void setR32_COLUMN_N(BigDecimal R32_COLUMN_N) {
		this.R32_COLUMN_N = R32_COLUMN_N;
	}

	public String getR33_COLUMN_A() {
		return R33_COLUMN_A;
	}

	public void setR33_COLUMN_A(String R33_COLUMN_A) {
		this.R33_COLUMN_A = R33_COLUMN_A;
	}

	public BigDecimal getR33_COLUMN_B() {
		return R33_COLUMN_B;
	}

	public void setR33_COLUMN_B(BigDecimal R33_COLUMN_B) {
		this.R33_COLUMN_B = R33_COLUMN_B;
	}

	public BigDecimal getR33_COLUMN_C() {
		return R33_COLUMN_C;
	}

	public void setR33_COLUMN_C(BigDecimal R33_COLUMN_C) {
		this.R33_COLUMN_C = R33_COLUMN_C;
	}

	public BigDecimal getR33_COLUMN_D() {
		return R33_COLUMN_D;
	}

	public void setR33_COLUMN_D(BigDecimal R33_COLUMN_D) {
		this.R33_COLUMN_D = R33_COLUMN_D;
	}

	public BigDecimal getR33_COLUMN_E() {
		return R33_COLUMN_E;
	}

	public void setR33_COLUMN_E(BigDecimal R33_COLUMN_E) {
		this.R33_COLUMN_E = R33_COLUMN_E;
	}

	public BigDecimal getR33_COLUMN_F() {
		return R33_COLUMN_F;
	}

	public void setR33_COLUMN_F(BigDecimal R33_COLUMN_F) {
		this.R33_COLUMN_F = R33_COLUMN_F;
	}

	public BigDecimal getR33_COLUMN_G() {
		return R33_COLUMN_G;
	}

	public void setR33_COLUMN_G(BigDecimal R33_COLUMN_G) {
		this.R33_COLUMN_G = R33_COLUMN_G;
	}

	public BigDecimal getR33_COLUMN_H() {
		return R33_COLUMN_H;
	}

	public void setR33_COLUMN_H(BigDecimal R33_COLUMN_H) {
		this.R33_COLUMN_H = R33_COLUMN_H;
	}

	public BigDecimal getR33_COLUMN_I() {
		return R33_COLUMN_I;
	}

	public void setR33_COLUMN_I(BigDecimal R33_COLUMN_I) {
		this.R33_COLUMN_I = R33_COLUMN_I;
	}

	public BigDecimal getR33_COLUMN_J() {
		return R33_COLUMN_J;
	}

	public void setR33_COLUMN_J(BigDecimal R33_COLUMN_J) {
		this.R33_COLUMN_J = R33_COLUMN_J;
	}

	public BigDecimal getR33_COLUMN_K() {
		return R33_COLUMN_K;
	}

	public void setR33_COLUMN_K(BigDecimal R33_COLUMN_K) {
		this.R33_COLUMN_K = R33_COLUMN_K;
	}

	public BigDecimal getR33_COLUMN_L() {
		return R33_COLUMN_L;
	}

	public void setR33_COLUMN_L(BigDecimal R33_COLUMN_L) {
		this.R33_COLUMN_L = R33_COLUMN_L;
	}

	public BigDecimal getR33_COLUMN_M() {
		return R33_COLUMN_M;
	}

	public void setR33_COLUMN_M(BigDecimal R33_COLUMN_M) {
		this.R33_COLUMN_M = R33_COLUMN_M;
	}

	public BigDecimal getR33_COLUMN_N() {
		return R33_COLUMN_N;
	}

	public void setR33_COLUMN_N(BigDecimal R33_COLUMN_N) {
		this.R33_COLUMN_N = R33_COLUMN_N;
	}

	public String getR34_COLUMN_A() {
		return R34_COLUMN_A;
	}

	public void setR34_COLUMN_A(String R34_COLUMN_A) {
		this.R34_COLUMN_A = R34_COLUMN_A;
	}

	public BigDecimal getR34_COLUMN_B() {
		return R34_COLUMN_B;
	}

	public void setR34_COLUMN_B(BigDecimal R34_COLUMN_B) {
		this.R34_COLUMN_B = R34_COLUMN_B;
	}

	public BigDecimal getR34_COLUMN_C() {
		return R34_COLUMN_C;
	}

	public void setR34_COLUMN_C(BigDecimal R34_COLUMN_C) {
		this.R34_COLUMN_C = R34_COLUMN_C;
	}

	public BigDecimal getR34_COLUMN_D() {
		return R34_COLUMN_D;
	}

	public void setR34_COLUMN_D(BigDecimal R34_COLUMN_D) {
		this.R34_COLUMN_D = R34_COLUMN_D;
	}

	public BigDecimal getR34_COLUMN_E() {
		return R34_COLUMN_E;
	}

	public void setR34_COLUMN_E(BigDecimal R34_COLUMN_E) {
		this.R34_COLUMN_E = R34_COLUMN_E;
	}

	public BigDecimal getR34_COLUMN_F() {
		return R34_COLUMN_F;
	}

	public void setR34_COLUMN_F(BigDecimal R34_COLUMN_F) {
		this.R34_COLUMN_F = R34_COLUMN_F;
	}

	public BigDecimal getR34_COLUMN_G() {
		return R34_COLUMN_G;
	}

	public void setR34_COLUMN_G(BigDecimal R34_COLUMN_G) {
		this.R34_COLUMN_G = R34_COLUMN_G;
	}

	public BigDecimal getR34_COLUMN_H() {
		return R34_COLUMN_H;
	}

	public void setR34_COLUMN_H(BigDecimal R34_COLUMN_H) {
		this.R34_COLUMN_H = R34_COLUMN_H;
	}

	public BigDecimal getR34_COLUMN_I() {
		return R34_COLUMN_I;
	}

	public void setR34_COLUMN_I(BigDecimal R34_COLUMN_I) {
		this.R34_COLUMN_I = R34_COLUMN_I;
	}

	public BigDecimal getR34_COLUMN_J() {
		return R34_COLUMN_J;
	}

	public void setR34_COLUMN_J(BigDecimal R34_COLUMN_J) {
		this.R34_COLUMN_J = R34_COLUMN_J;
	}

	public BigDecimal getR34_COLUMN_K() {
		return R34_COLUMN_K;
	}

	public void setR34_COLUMN_K(BigDecimal R34_COLUMN_K) {
		this.R34_COLUMN_K = R34_COLUMN_K;
	}

	public BigDecimal getR34_COLUMN_L() {
		return R34_COLUMN_L;
	}

	public void setR34_COLUMN_L(BigDecimal R34_COLUMN_L) {
		this.R34_COLUMN_L = R34_COLUMN_L;
	}

	public BigDecimal getR34_COLUMN_M() {
		return R34_COLUMN_M;
	}

	public void setR34_COLUMN_M(BigDecimal R34_COLUMN_M) {
		this.R34_COLUMN_M = R34_COLUMN_M;
	}

	public BigDecimal getR34_COLUMN_N() {
		return R34_COLUMN_N;
	}

	public void setR34_COLUMN_N(BigDecimal R34_COLUMN_N) {
		this.R34_COLUMN_N = R34_COLUMN_N;
	}

	public String getR35_COLUMN_A() {
		return R35_COLUMN_A;
	}

	public void setR35_COLUMN_A(String R35_COLUMN_A) {
		this.R35_COLUMN_A = R35_COLUMN_A;
	}

	public BigDecimal getR35_COLUMN_B() {
		return R35_COLUMN_B;
	}

	public void setR35_COLUMN_B(BigDecimal R35_COLUMN_B) {
		this.R35_COLUMN_B = R35_COLUMN_B;
	}

	public BigDecimal getR35_COLUMN_C() {
		return R35_COLUMN_C;
	}

	public void setR35_COLUMN_C(BigDecimal R35_COLUMN_C) {
		this.R35_COLUMN_C = R35_COLUMN_C;
	}

	public BigDecimal getR35_COLUMN_D() {
		return R35_COLUMN_D;
	}

	public void setR35_COLUMN_D(BigDecimal R35_COLUMN_D) {
		this.R35_COLUMN_D = R35_COLUMN_D;
	}

	public BigDecimal getR35_COLUMN_E() {
		return R35_COLUMN_E;
	}

	public void setR35_COLUMN_E(BigDecimal R35_COLUMN_E) {
		this.R35_COLUMN_E = R35_COLUMN_E;
	}

	public BigDecimal getR35_COLUMN_F() {
		return R35_COLUMN_F;
	}

	public void setR35_COLUMN_F(BigDecimal R35_COLUMN_F) {
		this.R35_COLUMN_F = R35_COLUMN_F;
	}

	public BigDecimal getR35_COLUMN_G() {
		return R35_COLUMN_G;
	}

	public void setR35_COLUMN_G(BigDecimal R35_COLUMN_G) {
		this.R35_COLUMN_G = R35_COLUMN_G;
	}

	public BigDecimal getR35_COLUMN_H() {
		return R35_COLUMN_H;
	}

	public void setR35_COLUMN_H(BigDecimal R35_COLUMN_H) {
		this.R35_COLUMN_H = R35_COLUMN_H;
	}

	public BigDecimal getR35_COLUMN_I() {
		return R35_COLUMN_I;
	}

	public void setR35_COLUMN_I(BigDecimal R35_COLUMN_I) {
		this.R35_COLUMN_I = R35_COLUMN_I;
	}

	public BigDecimal getR35_COLUMN_J() {
		return R35_COLUMN_J;
	}

	public void setR35_COLUMN_J(BigDecimal R35_COLUMN_J) {
		this.R35_COLUMN_J = R35_COLUMN_J;
	}

	public BigDecimal getR35_COLUMN_K() {
		return R35_COLUMN_K;
	}

	public void setR35_COLUMN_K(BigDecimal R35_COLUMN_K) {
		this.R35_COLUMN_K = R35_COLUMN_K;
	}

	public BigDecimal getR35_COLUMN_L() {
		return R35_COLUMN_L;
	}

	public void setR35_COLUMN_L(BigDecimal R35_COLUMN_L) {
		this.R35_COLUMN_L = R35_COLUMN_L;
	}

	public BigDecimal getR35_COLUMN_M() {
		return R35_COLUMN_M;
	}

	public void setR35_COLUMN_M(BigDecimal R35_COLUMN_M) {
		this.R35_COLUMN_M = R35_COLUMN_M;
	}

	public BigDecimal getR35_COLUMN_N() {
		return R35_COLUMN_N;
	}

	public void setR35_COLUMN_N(BigDecimal R35_COLUMN_N) {
		this.R35_COLUMN_N = R35_COLUMN_N;
	}

	public String getR36_COLUMN_A() {
		return R36_COLUMN_A;
	}

	public void setR36_COLUMN_A(String R36_COLUMN_A) {
		this.R36_COLUMN_A = R36_COLUMN_A;
	}

	public BigDecimal getR36_COLUMN_B() {
		return R36_COLUMN_B;
	}

	public void setR36_COLUMN_B(BigDecimal R36_COLUMN_B) {
		this.R36_COLUMN_B = R36_COLUMN_B;
	}

	public BigDecimal getR36_COLUMN_C() {
		return R36_COLUMN_C;
	}

	public void setR36_COLUMN_C(BigDecimal R36_COLUMN_C) {
		this.R36_COLUMN_C = R36_COLUMN_C;
	}

	public BigDecimal getR36_COLUMN_D() {
		return R36_COLUMN_D;
	}

	public void setR36_COLUMN_D(BigDecimal R36_COLUMN_D) {
		this.R36_COLUMN_D = R36_COLUMN_D;
	}

	public BigDecimal getR36_COLUMN_E() {
		return R36_COLUMN_E;
	}

	public void setR36_COLUMN_E(BigDecimal R36_COLUMN_E) {
		this.R36_COLUMN_E = R36_COLUMN_E;
	}

	public BigDecimal getR36_COLUMN_F() {
		return R36_COLUMN_F;
	}

	public void setR36_COLUMN_F(BigDecimal R36_COLUMN_F) {
		this.R36_COLUMN_F = R36_COLUMN_F;
	}

	public BigDecimal getR36_COLUMN_G() {
		return R36_COLUMN_G;
	}

	public void setR36_COLUMN_G(BigDecimal R36_COLUMN_G) {
		this.R36_COLUMN_G = R36_COLUMN_G;
	}

	public BigDecimal getR36_COLUMN_H() {
		return R36_COLUMN_H;
	}

	public void setR36_COLUMN_H(BigDecimal R36_COLUMN_H) {
		this.R36_COLUMN_H = R36_COLUMN_H;
	}

	public BigDecimal getR36_COLUMN_I() {
		return R36_COLUMN_I;
	}

	public void setR36_COLUMN_I(BigDecimal R36_COLUMN_I) {
		this.R36_COLUMN_I = R36_COLUMN_I;
	}

	public BigDecimal getR36_COLUMN_J() {
		return R36_COLUMN_J;
	}

	public void setR36_COLUMN_J(BigDecimal R36_COLUMN_J) {
		this.R36_COLUMN_J = R36_COLUMN_J;
	}

	public BigDecimal getR36_COLUMN_K() {
		return R36_COLUMN_K;
	}

	public void setR36_COLUMN_K(BigDecimal R36_COLUMN_K) {
		this.R36_COLUMN_K = R36_COLUMN_K;
	}

	public BigDecimal getR36_COLUMN_L() {
		return R36_COLUMN_L;
	}

	public void setR36_COLUMN_L(BigDecimal R36_COLUMN_L) {
		this.R36_COLUMN_L = R36_COLUMN_L;
	}

	public BigDecimal getR36_COLUMN_M() {
		return R36_COLUMN_M;
	}

	public void setR36_COLUMN_M(BigDecimal R36_COLUMN_M) {
		this.R36_COLUMN_M = R36_COLUMN_M;
	}

	public BigDecimal getR36_COLUMN_N() {
		return R36_COLUMN_N;
	}

	public void setR36_COLUMN_N(BigDecimal R36_COLUMN_N) {
		this.R36_COLUMN_N = R36_COLUMN_N;
	}

	public String getR37_COLUMN_A() {
		return R37_COLUMN_A;
	}

	public void setR37_COLUMN_A(String R37_COLUMN_A) {
		this.R37_COLUMN_A = R37_COLUMN_A;
	}

	public BigDecimal getR37_COLUMN_B() {
		return R37_COLUMN_B;
	}

	public void setR37_COLUMN_B(BigDecimal R37_COLUMN_B) {
		this.R37_COLUMN_B = R37_COLUMN_B;
	}

	public BigDecimal getR37_COLUMN_C() {
		return R37_COLUMN_C;
	}

	public void setR37_COLUMN_C(BigDecimal R37_COLUMN_C) {
		this.R37_COLUMN_C = R37_COLUMN_C;
	}

	public BigDecimal getR37_COLUMN_D() {
		return R37_COLUMN_D;
	}

	public void setR37_COLUMN_D(BigDecimal R37_COLUMN_D) {
		this.R37_COLUMN_D = R37_COLUMN_D;
	}

	public BigDecimal getR37_COLUMN_E() {
		return R37_COLUMN_E;
	}

	public void setR37_COLUMN_E(BigDecimal R37_COLUMN_E) {
		this.R37_COLUMN_E = R37_COLUMN_E;
	}

	public BigDecimal getR37_COLUMN_F() {
		return R37_COLUMN_F;
	}

	public void setR37_COLUMN_F(BigDecimal R37_COLUMN_F) {
		this.R37_COLUMN_F = R37_COLUMN_F;
	}

	public BigDecimal getR37_COLUMN_G() {
		return R37_COLUMN_G;
	}

	public void setR37_COLUMN_G(BigDecimal R37_COLUMN_G) {
		this.R37_COLUMN_G = R37_COLUMN_G;
	}

	public BigDecimal getR37_COLUMN_H() {
		return R37_COLUMN_H;
	}

	public void setR37_COLUMN_H(BigDecimal R37_COLUMN_H) {
		this.R37_COLUMN_H = R37_COLUMN_H;
	}

	public BigDecimal getR37_COLUMN_I() {
		return R37_COLUMN_I;
	}

	public void setR37_COLUMN_I(BigDecimal R37_COLUMN_I) {
		this.R37_COLUMN_I = R37_COLUMN_I;
	}

	public BigDecimal getR37_COLUMN_J() {
		return R37_COLUMN_J;
	}

	public void setR37_COLUMN_J(BigDecimal R37_COLUMN_J) {
		this.R37_COLUMN_J = R37_COLUMN_J;
	}

	public BigDecimal getR37_COLUMN_K() {
		return R37_COLUMN_K;
	}

	public void setR37_COLUMN_K(BigDecimal R37_COLUMN_K) {
		this.R37_COLUMN_K = R37_COLUMN_K;
	}

	public BigDecimal getR37_COLUMN_L() {
		return R37_COLUMN_L;
	}

	public void setR37_COLUMN_L(BigDecimal R37_COLUMN_L) {
		this.R37_COLUMN_L = R37_COLUMN_L;
	}

	public BigDecimal getR37_COLUMN_M() {
		return R37_COLUMN_M;
	}

	public void setR37_COLUMN_M(BigDecimal R37_COLUMN_M) {
		this.R37_COLUMN_M = R37_COLUMN_M;
	}

	public BigDecimal getR37_COLUMN_N() {
		return R37_COLUMN_N;
	}

	public void setR37_COLUMN_N(BigDecimal R37_COLUMN_N) {
		this.R37_COLUMN_N = R37_COLUMN_N;
	}

	public String getR38_COLUMN_A() {
		return R38_COLUMN_A;
	}

	public void setR38_COLUMN_A(String R38_COLUMN_A) {
		this.R38_COLUMN_A = R38_COLUMN_A;
	}

	public BigDecimal getR38_COLUMN_B() {
		return R38_COLUMN_B;
	}

	public void setR38_COLUMN_B(BigDecimal R38_COLUMN_B) {
		this.R38_COLUMN_B = R38_COLUMN_B;
	}

	public BigDecimal getR38_COLUMN_C() {
		return R38_COLUMN_C;
	}

	public void setR38_COLUMN_C(BigDecimal R38_COLUMN_C) {
		this.R38_COLUMN_C = R38_COLUMN_C;
	}

	public BigDecimal getR38_COLUMN_D() {
		return R38_COLUMN_D;
	}

	public void setR38_COLUMN_D(BigDecimal R38_COLUMN_D) {
		this.R38_COLUMN_D = R38_COLUMN_D;
	}

	public BigDecimal getR38_COLUMN_E() {
		return R38_COLUMN_E;
	}

	public void setR38_COLUMN_E(BigDecimal R38_COLUMN_E) {
		this.R38_COLUMN_E = R38_COLUMN_E;
	}

	public BigDecimal getR38_COLUMN_F() {
		return R38_COLUMN_F;
	}

	public void setR38_COLUMN_F(BigDecimal R38_COLUMN_F) {
		this.R38_COLUMN_F = R38_COLUMN_F;
	}

	public BigDecimal getR38_COLUMN_G() {
		return R38_COLUMN_G;
	}

	public void setR38_COLUMN_G(BigDecimal R38_COLUMN_G) {
		this.R38_COLUMN_G = R38_COLUMN_G;
	}

	public BigDecimal getR38_COLUMN_H() {
		return R38_COLUMN_H;
	}

	public void setR38_COLUMN_H(BigDecimal R38_COLUMN_H) {
		this.R38_COLUMN_H = R38_COLUMN_H;
	}

	public BigDecimal getR38_COLUMN_I() {
		return R38_COLUMN_I;
	}

	public void setR38_COLUMN_I(BigDecimal R38_COLUMN_I) {
		this.R38_COLUMN_I = R38_COLUMN_I;
	}

	public BigDecimal getR38_COLUMN_J() {
		return R38_COLUMN_J;
	}

	public void setR38_COLUMN_J(BigDecimal R38_COLUMN_J) {
		this.R38_COLUMN_J = R38_COLUMN_J;
	}

	public BigDecimal getR38_COLUMN_K() {
		return R38_COLUMN_K;
	}

	public void setR38_COLUMN_K(BigDecimal R38_COLUMN_K) {
		this.R38_COLUMN_K = R38_COLUMN_K;
	}

	public BigDecimal getR38_COLUMN_L() {
		return R38_COLUMN_L;
	}

	public void setR38_COLUMN_L(BigDecimal R38_COLUMN_L) {
		this.R38_COLUMN_L = R38_COLUMN_L;
	}

	public BigDecimal getR38_COLUMN_M() {
		return R38_COLUMN_M;
	}

	public void setR38_COLUMN_M(BigDecimal R38_COLUMN_M) {
		this.R38_COLUMN_M = R38_COLUMN_M;
	}

	public BigDecimal getR38_COLUMN_N() {
		return R38_COLUMN_N;
	}

	public void setR38_COLUMN_N(BigDecimal R38_COLUMN_N) {
		this.R38_COLUMN_N = R38_COLUMN_N;
	}

	public String getR39_COLUMN_A() {
		return R39_COLUMN_A;
	}

	public void setR39_COLUMN_A(String R39_COLUMN_A) {
		this.R39_COLUMN_A = R39_COLUMN_A;
	}

	public BigDecimal getR39_COLUMN_B() {
		return R39_COLUMN_B;
	}

	public void setR39_COLUMN_B(BigDecimal R39_COLUMN_B) {
		this.R39_COLUMN_B = R39_COLUMN_B;
	}

	public BigDecimal getR39_COLUMN_C() {
		return R39_COLUMN_C;
	}

	public void setR39_COLUMN_C(BigDecimal R39_COLUMN_C) {
		this.R39_COLUMN_C = R39_COLUMN_C;
	}

	public BigDecimal getR39_COLUMN_D() {
		return R39_COLUMN_D;
	}

	public void setR39_COLUMN_D(BigDecimal R39_COLUMN_D) {
		this.R39_COLUMN_D = R39_COLUMN_D;
	}

	public BigDecimal getR39_COLUMN_E() {
		return R39_COLUMN_E;
	}

	public void setR39_COLUMN_E(BigDecimal R39_COLUMN_E) {
		this.R39_COLUMN_E = R39_COLUMN_E;
	}

	public BigDecimal getR39_COLUMN_F() {
		return R39_COLUMN_F;
	}

	public void setR39_COLUMN_F(BigDecimal R39_COLUMN_F) {
		this.R39_COLUMN_F = R39_COLUMN_F;
	}

	public BigDecimal getR39_COLUMN_G() {
		return R39_COLUMN_G;
	}

	public void setR39_COLUMN_G(BigDecimal R39_COLUMN_G) {
		this.R39_COLUMN_G = R39_COLUMN_G;
	}

	public BigDecimal getR39_COLUMN_H() {
		return R39_COLUMN_H;
	}

	public void setR39_COLUMN_H(BigDecimal R39_COLUMN_H) {
		this.R39_COLUMN_H = R39_COLUMN_H;
	}

	public BigDecimal getR39_COLUMN_I() {
		return R39_COLUMN_I;
	}

	public void setR39_COLUMN_I(BigDecimal R39_COLUMN_I) {
		this.R39_COLUMN_I = R39_COLUMN_I;
	}

	public BigDecimal getR39_COLUMN_J() {
		return R39_COLUMN_J;
	}

	public void setR39_COLUMN_J(BigDecimal R39_COLUMN_J) {
		this.R39_COLUMN_J = R39_COLUMN_J;
	}

	public BigDecimal getR39_COLUMN_K() {
		return R39_COLUMN_K;
	}

	public void setR39_COLUMN_K(BigDecimal R39_COLUMN_K) {
		this.R39_COLUMN_K = R39_COLUMN_K;
	}

	public BigDecimal getR39_COLUMN_L() {
		return R39_COLUMN_L;
	}

	public void setR39_COLUMN_L(BigDecimal R39_COLUMN_L) {
		this.R39_COLUMN_L = R39_COLUMN_L;
	}

	public BigDecimal getR39_COLUMN_M() {
		return R39_COLUMN_M;
	}

	public void setR39_COLUMN_M(BigDecimal R39_COLUMN_M) {
		this.R39_COLUMN_M = R39_COLUMN_M;
	}

	public BigDecimal getR39_COLUMN_N() {
		return R39_COLUMN_N;
	}

	public void setR39_COLUMN_N(BigDecimal R39_COLUMN_N) {
		this.R39_COLUMN_N = R39_COLUMN_N;
	}

	public String getR40_COLUMN_A() {
		return R40_COLUMN_A;
	}

	public void setR40_COLUMN_A(String R40_COLUMN_A) {
		this.R40_COLUMN_A = R40_COLUMN_A;
	}

	public BigDecimal getR40_COLUMN_B() {
		return R40_COLUMN_B;
	}

	public void setR40_COLUMN_B(BigDecimal R40_COLUMN_B) {
		this.R40_COLUMN_B = R40_COLUMN_B;
	}

	public BigDecimal getR40_COLUMN_C() {
		return R40_COLUMN_C;
	}

	public void setR40_COLUMN_C(BigDecimal R40_COLUMN_C) {
		this.R40_COLUMN_C = R40_COLUMN_C;
	}

	public BigDecimal getR40_COLUMN_D() {
		return R40_COLUMN_D;
	}

	public void setR40_COLUMN_D(BigDecimal R40_COLUMN_D) {
		this.R40_COLUMN_D = R40_COLUMN_D;
	}

	public BigDecimal getR40_COLUMN_E() {
		return R40_COLUMN_E;
	}

	public void setR40_COLUMN_E(BigDecimal R40_COLUMN_E) {
		this.R40_COLUMN_E = R40_COLUMN_E;
	}

	public BigDecimal getR40_COLUMN_F() {
		return R40_COLUMN_F;
	}

	public void setR40_COLUMN_F(BigDecimal R40_COLUMN_F) {
		this.R40_COLUMN_F = R40_COLUMN_F;
	}

	public BigDecimal getR40_COLUMN_G() {
		return R40_COLUMN_G;
	}

	public void setR40_COLUMN_G(BigDecimal R40_COLUMN_G) {
		this.R40_COLUMN_G = R40_COLUMN_G;
	}

	public BigDecimal getR40_COLUMN_H() {
		return R40_COLUMN_H;
	}

	public void setR40_COLUMN_H(BigDecimal R40_COLUMN_H) {
		this.R40_COLUMN_H = R40_COLUMN_H;
	}

	public BigDecimal getR40_COLUMN_I() {
		return R40_COLUMN_I;
	}

	public void setR40_COLUMN_I(BigDecimal R40_COLUMN_I) {
		this.R40_COLUMN_I = R40_COLUMN_I;
	}

	public BigDecimal getR40_COLUMN_J() {
		return R40_COLUMN_J;
	}

	public void setR40_COLUMN_J(BigDecimal R40_COLUMN_J) {
		this.R40_COLUMN_J = R40_COLUMN_J;
	}

	public BigDecimal getR40_COLUMN_K() {
		return R40_COLUMN_K;
	}

	public void setR40_COLUMN_K(BigDecimal R40_COLUMN_K) {
		this.R40_COLUMN_K = R40_COLUMN_K;
	}

	public BigDecimal getR40_COLUMN_L() {
		return R40_COLUMN_L;
	}

	public void setR40_COLUMN_L(BigDecimal R40_COLUMN_L) {
		this.R40_COLUMN_L = R40_COLUMN_L;
	}

	public BigDecimal getR40_COLUMN_M() {
		return R40_COLUMN_M;
	}

	public void setR40_COLUMN_M(BigDecimal R40_COLUMN_M) {
		this.R40_COLUMN_M = R40_COLUMN_M;
	}

	public BigDecimal getR40_COLUMN_N() {
		return R40_COLUMN_N;
	}

	public void setR40_COLUMN_N(BigDecimal R40_COLUMN_N) {
		this.R40_COLUMN_N = R40_COLUMN_N;
	}

	public String getR41_COLUMN_A() {
		return R41_COLUMN_A;
	}

	public void setR41_COLUMN_A(String R41_COLUMN_A) {
		this.R41_COLUMN_A = R41_COLUMN_A;
	}

	public BigDecimal getR41_COLUMN_B() {
		return R41_COLUMN_B;
	}

	public void setR41_COLUMN_B(BigDecimal R41_COLUMN_B) {
		this.R41_COLUMN_B = R41_COLUMN_B;
	}

	public BigDecimal getR41_COLUMN_C() {
		return R41_COLUMN_C;
	}

	public void setR41_COLUMN_C(BigDecimal R41_COLUMN_C) {
		this.R41_COLUMN_C = R41_COLUMN_C;
	}

	public BigDecimal getR41_COLUMN_D() {
		return R41_COLUMN_D;
	}

	public void setR41_COLUMN_D(BigDecimal R41_COLUMN_D) {
		this.R41_COLUMN_D = R41_COLUMN_D;
	}

	public BigDecimal getR41_COLUMN_E() {
		return R41_COLUMN_E;
	}

	public void setR41_COLUMN_E(BigDecimal R41_COLUMN_E) {
		this.R41_COLUMN_E = R41_COLUMN_E;
	}

	public BigDecimal getR41_COLUMN_F() {
		return R41_COLUMN_F;
	}

	public void setR41_COLUMN_F(BigDecimal R41_COLUMN_F) {
		this.R41_COLUMN_F = R41_COLUMN_F;
	}

	public BigDecimal getR41_COLUMN_G() {
		return R41_COLUMN_G;
	}

	public void setR41_COLUMN_G(BigDecimal R41_COLUMN_G) {
		this.R41_COLUMN_G = R41_COLUMN_G;
	}

	public BigDecimal getR41_COLUMN_H() {
		return R41_COLUMN_H;
	}

	public void setR41_COLUMN_H(BigDecimal R41_COLUMN_H) {
		this.R41_COLUMN_H = R41_COLUMN_H;
	}

	public BigDecimal getR41_COLUMN_I() {
		return R41_COLUMN_I;
	}

	public void setR41_COLUMN_I(BigDecimal R41_COLUMN_I) {
		this.R41_COLUMN_I = R41_COLUMN_I;
	}

	public BigDecimal getR41_COLUMN_J() {
		return R41_COLUMN_J;
	}

	public void setR41_COLUMN_J(BigDecimal R41_COLUMN_J) {
		this.R41_COLUMN_J = R41_COLUMN_J;
	}

	public BigDecimal getR41_COLUMN_K() {
		return R41_COLUMN_K;
	}

	public void setR41_COLUMN_K(BigDecimal R41_COLUMN_K) {
		this.R41_COLUMN_K = R41_COLUMN_K;
	}

	public BigDecimal getR41_COLUMN_L() {
		return R41_COLUMN_L;
	}

	public void setR41_COLUMN_L(BigDecimal R41_COLUMN_L) {
		this.R41_COLUMN_L = R41_COLUMN_L;
	}

	public BigDecimal getR41_COLUMN_M() {
		return R41_COLUMN_M;
	}

	public void setR41_COLUMN_M(BigDecimal R41_COLUMN_M) {
		this.R41_COLUMN_M = R41_COLUMN_M;
	}

	public BigDecimal getR41_COLUMN_N() {
		return R41_COLUMN_N;
	}

	public void setR41_COLUMN_N(BigDecimal R41_COLUMN_N) {
		this.R41_COLUMN_N = R41_COLUMN_N;
	}

	public String getR42_COLUMN_A() {
		return R42_COLUMN_A;
	}

	public void setR42_COLUMN_A(String R42_COLUMN_A) {
		this.R42_COLUMN_A = R42_COLUMN_A;
	}

	public BigDecimal getR42_COLUMN_B() {
		return R42_COLUMN_B;
	}

	public void setR42_COLUMN_B(BigDecimal R42_COLUMN_B) {
		this.R42_COLUMN_B = R42_COLUMN_B;
	}

	public BigDecimal getR42_COLUMN_C() {
		return R42_COLUMN_C;
	}

	public void setR42_COLUMN_C(BigDecimal R42_COLUMN_C) {
		this.R42_COLUMN_C = R42_COLUMN_C;
	}

	public BigDecimal getR42_COLUMN_D() {
		return R42_COLUMN_D;
	}

	public void setR42_COLUMN_D(BigDecimal R42_COLUMN_D) {
		this.R42_COLUMN_D = R42_COLUMN_D;
	}

	public BigDecimal getR42_COLUMN_E() {
		return R42_COLUMN_E;
	}

	public void setR42_COLUMN_E(BigDecimal R42_COLUMN_E) {
		this.R42_COLUMN_E = R42_COLUMN_E;
	}

	public BigDecimal getR42_COLUMN_F() {
		return R42_COLUMN_F;
	}

	public void setR42_COLUMN_F(BigDecimal R42_COLUMN_F) {
		this.R42_COLUMN_F = R42_COLUMN_F;
	}

	public BigDecimal getR42_COLUMN_G() {
		return R42_COLUMN_G;
	}

	public void setR42_COLUMN_G(BigDecimal R42_COLUMN_G) {
		this.R42_COLUMN_G = R42_COLUMN_G;
	}

	public BigDecimal getR42_COLUMN_H() {
		return R42_COLUMN_H;
	}

	public void setR42_COLUMN_H(BigDecimal R42_COLUMN_H) {
		this.R42_COLUMN_H = R42_COLUMN_H;
	}

	public BigDecimal getR42_COLUMN_I() {
		return R42_COLUMN_I;
	}

	public void setR42_COLUMN_I(BigDecimal R42_COLUMN_I) {
		this.R42_COLUMN_I = R42_COLUMN_I;
	}

	public BigDecimal getR42_COLUMN_J() {
		return R42_COLUMN_J;
	}

	public void setR42_COLUMN_J(BigDecimal R42_COLUMN_J) {
		this.R42_COLUMN_J = R42_COLUMN_J;
	}

	public BigDecimal getR42_COLUMN_K() {
		return R42_COLUMN_K;
	}

	public void setR42_COLUMN_K(BigDecimal R42_COLUMN_K) {
		this.R42_COLUMN_K = R42_COLUMN_K;
	}

	public BigDecimal getR42_COLUMN_L() {
		return R42_COLUMN_L;
	}

	public void setR42_COLUMN_L(BigDecimal R42_COLUMN_L) {
		this.R42_COLUMN_L = R42_COLUMN_L;
	}

	public BigDecimal getR42_COLUMN_M() {
		return R42_COLUMN_M;
	}

	public void setR42_COLUMN_M(BigDecimal R42_COLUMN_M) {
		this.R42_COLUMN_M = R42_COLUMN_M;
	}

	public BigDecimal getR42_COLUMN_N() {
		return R42_COLUMN_N;
	}

	public void setR42_COLUMN_N(BigDecimal R42_COLUMN_N) {
		this.R42_COLUMN_N = R42_COLUMN_N;
	}

	public String getR43_COLUMN_A() {
		return R43_COLUMN_A;
	}

	public void setR43_COLUMN_A(String R43_COLUMN_A) {
		this.R43_COLUMN_A = R43_COLUMN_A;
	}

	public BigDecimal getR43_COLUMN_B() {
		return R43_COLUMN_B;
	}

	public void setR43_COLUMN_B(BigDecimal R43_COLUMN_B) {
		this.R43_COLUMN_B = R43_COLUMN_B;
	}

	public BigDecimal getR43_COLUMN_C() {
		return R43_COLUMN_C;
	}

	public void setR43_COLUMN_C(BigDecimal R43_COLUMN_C) {
		this.R43_COLUMN_C = R43_COLUMN_C;
	}

	public BigDecimal getR43_COLUMN_D() {
		return R43_COLUMN_D;
	}

	public void setR43_COLUMN_D(BigDecimal R43_COLUMN_D) {
		this.R43_COLUMN_D = R43_COLUMN_D;
	}

	public BigDecimal getR43_COLUMN_E() {
		return R43_COLUMN_E;
	}

	public void setR43_COLUMN_E(BigDecimal R43_COLUMN_E) {
		this.R43_COLUMN_E = R43_COLUMN_E;
	}

	public BigDecimal getR43_COLUMN_F() {
		return R43_COLUMN_F;
	}

	public void setR43_COLUMN_F(BigDecimal R43_COLUMN_F) {
		this.R43_COLUMN_F = R43_COLUMN_F;
	}

	public BigDecimal getR43_COLUMN_G() {
		return R43_COLUMN_G;
	}

	public void setR43_COLUMN_G(BigDecimal R43_COLUMN_G) {
		this.R43_COLUMN_G = R43_COLUMN_G;
	}

	public BigDecimal getR43_COLUMN_H() {
		return R43_COLUMN_H;
	}

	public void setR43_COLUMN_H(BigDecimal R43_COLUMN_H) {
		this.R43_COLUMN_H = R43_COLUMN_H;
	}

	public BigDecimal getR43_COLUMN_I() {
		return R43_COLUMN_I;
	}

	public void setR43_COLUMN_I(BigDecimal R43_COLUMN_I) {
		this.R43_COLUMN_I = R43_COLUMN_I;
	}

	public BigDecimal getR43_COLUMN_J() {
		return R43_COLUMN_J;
	}

	public void setR43_COLUMN_J(BigDecimal R43_COLUMN_J) {
		this.R43_COLUMN_J = R43_COLUMN_J;
	}

	public BigDecimal getR43_COLUMN_K() {
		return R43_COLUMN_K;
	}

	public void setR43_COLUMN_K(BigDecimal R43_COLUMN_K) {
		this.R43_COLUMN_K = R43_COLUMN_K;
	}

	public BigDecimal getR43_COLUMN_L() {
		return R43_COLUMN_L;
	}

	public void setR43_COLUMN_L(BigDecimal R43_COLUMN_L) {
		this.R43_COLUMN_L = R43_COLUMN_L;
	}

	public BigDecimal getR43_COLUMN_M() {
		return R43_COLUMN_M;
	}

	public void setR43_COLUMN_M(BigDecimal R43_COLUMN_M) {
		this.R43_COLUMN_M = R43_COLUMN_M;
	}

	public BigDecimal getR43_COLUMN_N() {
		return R43_COLUMN_N;
	}

	public void setR43_COLUMN_N(BigDecimal R43_COLUMN_N) {
		this.R43_COLUMN_N = R43_COLUMN_N;
	}

	public String getR44_COLUMN_A() {
		return R44_COLUMN_A;
	}

	public void setR44_COLUMN_A(String R44_COLUMN_A) {
		this.R44_COLUMN_A = R44_COLUMN_A;
	}

	public BigDecimal getR44_COLUMN_B() {
		return R44_COLUMN_B;
	}

	public void setR44_COLUMN_B(BigDecimal R44_COLUMN_B) {
		this.R44_COLUMN_B = R44_COLUMN_B;
	}

	public BigDecimal getR44_COLUMN_C() {
		return R44_COLUMN_C;
	}

	public void setR44_COLUMN_C(BigDecimal R44_COLUMN_C) {
		this.R44_COLUMN_C = R44_COLUMN_C;
	}

	public BigDecimal getR44_COLUMN_D() {
		return R44_COLUMN_D;
	}

	public void setR44_COLUMN_D(BigDecimal R44_COLUMN_D) {
		this.R44_COLUMN_D = R44_COLUMN_D;
	}

	public BigDecimal getR44_COLUMN_E() {
		return R44_COLUMN_E;
	}

	public void setR44_COLUMN_E(BigDecimal R44_COLUMN_E) {
		this.R44_COLUMN_E = R44_COLUMN_E;
	}

	public BigDecimal getR44_COLUMN_F() {
		return R44_COLUMN_F;
	}

	public void setR44_COLUMN_F(BigDecimal R44_COLUMN_F) {
		this.R44_COLUMN_F = R44_COLUMN_F;
	}

	public BigDecimal getR44_COLUMN_G() {
		return R44_COLUMN_G;
	}

	public void setR44_COLUMN_G(BigDecimal R44_COLUMN_G) {
		this.R44_COLUMN_G = R44_COLUMN_G;
	}

	public BigDecimal getR44_COLUMN_H() {
		return R44_COLUMN_H;
	}

	public void setR44_COLUMN_H(BigDecimal R44_COLUMN_H) {
		this.R44_COLUMN_H = R44_COLUMN_H;
	}

	public BigDecimal getR44_COLUMN_I() {
		return R44_COLUMN_I;
	}

	public void setR44_COLUMN_I(BigDecimal R44_COLUMN_I) {
		this.R44_COLUMN_I = R44_COLUMN_I;
	}

	public BigDecimal getR44_COLUMN_J() {
		return R44_COLUMN_J;
	}

	public void setR44_COLUMN_J(BigDecimal R44_COLUMN_J) {
		this.R44_COLUMN_J = R44_COLUMN_J;
	}

	public BigDecimal getR44_COLUMN_K() {
		return R44_COLUMN_K;
	}

	public void setR44_COLUMN_K(BigDecimal R44_COLUMN_K) {
		this.R44_COLUMN_K = R44_COLUMN_K;
	}

	public BigDecimal getR44_COLUMN_L() {
		return R44_COLUMN_L;
	}

	public void setR44_COLUMN_L(BigDecimal R44_COLUMN_L) {
		this.R44_COLUMN_L = R44_COLUMN_L;
	}

	public BigDecimal getR44_COLUMN_M() {
		return R44_COLUMN_M;
	}

	public void setR44_COLUMN_M(BigDecimal R44_COLUMN_M) {
		this.R44_COLUMN_M = R44_COLUMN_M;
	}

	public BigDecimal getR44_COLUMN_N() {
		return R44_COLUMN_N;
	}

	public void setR44_COLUMN_N(BigDecimal R44_COLUMN_N) {
		this.R44_COLUMN_N = R44_COLUMN_N;
	}

	public String getR45_COLUMN_A() {
		return R45_COLUMN_A;
	}

	public void setR45_COLUMN_A(String R45_COLUMN_A) {
		this.R45_COLUMN_A = R45_COLUMN_A;
	}

	public BigDecimal getR45_COLUMN_B() {
		return R45_COLUMN_B;
	}

	public void setR45_COLUMN_B(BigDecimal R45_COLUMN_B) {
		this.R45_COLUMN_B = R45_COLUMN_B;
	}

	public BigDecimal getR45_COLUMN_C() {
		return R45_COLUMN_C;
	}

	public void setR45_COLUMN_C(BigDecimal R45_COLUMN_C) {
		this.R45_COLUMN_C = R45_COLUMN_C;
	}

	public BigDecimal getR45_COLUMN_D() {
		return R45_COLUMN_D;
	}

	public void setR45_COLUMN_D(BigDecimal R45_COLUMN_D) {
		this.R45_COLUMN_D = R45_COLUMN_D;
	}

	public BigDecimal getR45_COLUMN_E() {
		return R45_COLUMN_E;
	}

	public void setR45_COLUMN_E(BigDecimal R45_COLUMN_E) {
		this.R45_COLUMN_E = R45_COLUMN_E;
	}

	public BigDecimal getR45_COLUMN_F() {
		return R45_COLUMN_F;
	}

	public void setR45_COLUMN_F(BigDecimal R45_COLUMN_F) {
		this.R45_COLUMN_F = R45_COLUMN_F;
	}

	public BigDecimal getR45_COLUMN_G() {
		return R45_COLUMN_G;
	}

	public void setR45_COLUMN_G(BigDecimal R45_COLUMN_G) {
		this.R45_COLUMN_G = R45_COLUMN_G;
	}

	public BigDecimal getR45_COLUMN_H() {
		return R45_COLUMN_H;
	}

	public void setR45_COLUMN_H(BigDecimal R45_COLUMN_H) {
		this.R45_COLUMN_H = R45_COLUMN_H;
	}

	public BigDecimal getR45_COLUMN_I() {
		return R45_COLUMN_I;
	}

	public void setR45_COLUMN_I(BigDecimal R45_COLUMN_I) {
		this.R45_COLUMN_I = R45_COLUMN_I;
	}

	public BigDecimal getR45_COLUMN_J() {
		return R45_COLUMN_J;
	}

	public void setR45_COLUMN_J(BigDecimal R45_COLUMN_J) {
		this.R45_COLUMN_J = R45_COLUMN_J;
	}

	public BigDecimal getR45_COLUMN_K() {
		return R45_COLUMN_K;
	}

	public void setR45_COLUMN_K(BigDecimal R45_COLUMN_K) {
		this.R45_COLUMN_K = R45_COLUMN_K;
	}

	public BigDecimal getR45_COLUMN_L() {
		return R45_COLUMN_L;
	}

	public void setR45_COLUMN_L(BigDecimal R45_COLUMN_L) {
		this.R45_COLUMN_L = R45_COLUMN_L;
	}

	public BigDecimal getR45_COLUMN_M() {
		return R45_COLUMN_M;
	}

	public void setR45_COLUMN_M(BigDecimal R45_COLUMN_M) {
		this.R45_COLUMN_M = R45_COLUMN_M;
	}

	public BigDecimal getR45_COLUMN_N() {
		return R45_COLUMN_N;
	}

	public void setR45_COLUMN_N(BigDecimal R45_COLUMN_N) {
		this.R45_COLUMN_N = R45_COLUMN_N;
	}

	public String getR46_COLUMN_A() {
		return R46_COLUMN_A;
	}

	public void setR46_COLUMN_A(String R46_COLUMN_A) {
		this.R46_COLUMN_A = R46_COLUMN_A;
	}

	public BigDecimal getR46_COLUMN_B() {
		return R46_COLUMN_B;
	}

	public void setR46_COLUMN_B(BigDecimal R46_COLUMN_B) {
		this.R46_COLUMN_B = R46_COLUMN_B;
	}

	public BigDecimal getR46_COLUMN_C() {
		return R46_COLUMN_C;
	}

	public void setR46_COLUMN_C(BigDecimal R46_COLUMN_C) {
		this.R46_COLUMN_C = R46_COLUMN_C;
	}

	public BigDecimal getR46_COLUMN_D() {
		return R46_COLUMN_D;
	}

	public void setR46_COLUMN_D(BigDecimal R46_COLUMN_D) {
		this.R46_COLUMN_D = R46_COLUMN_D;
	}

	public BigDecimal getR46_COLUMN_E() {
		return R46_COLUMN_E;
	}

	public void setR46_COLUMN_E(BigDecimal R46_COLUMN_E) {
		this.R46_COLUMN_E = R46_COLUMN_E;
	}

	public BigDecimal getR46_COLUMN_F() {
		return R46_COLUMN_F;
	}

	public void setR46_COLUMN_F(BigDecimal R46_COLUMN_F) {
		this.R46_COLUMN_F = R46_COLUMN_F;
	}

	public BigDecimal getR46_COLUMN_G() {
		return R46_COLUMN_G;
	}

	public void setR46_COLUMN_G(BigDecimal R46_COLUMN_G) {
		this.R46_COLUMN_G = R46_COLUMN_G;
	}

	public BigDecimal getR46_COLUMN_H() {
		return R46_COLUMN_H;
	}

	public void setR46_COLUMN_H(BigDecimal R46_COLUMN_H) {
		this.R46_COLUMN_H = R46_COLUMN_H;
	}

	public BigDecimal getR46_COLUMN_I() {
		return R46_COLUMN_I;
	}

	public void setR46_COLUMN_I(BigDecimal R46_COLUMN_I) {
		this.R46_COLUMN_I = R46_COLUMN_I;
	}

	public BigDecimal getR46_COLUMN_J() {
		return R46_COLUMN_J;
	}

	public void setR46_COLUMN_J(BigDecimal R46_COLUMN_J) {
		this.R46_COLUMN_J = R46_COLUMN_J;
	}

	public BigDecimal getR46_COLUMN_K() {
		return R46_COLUMN_K;
	}

	public void setR46_COLUMN_K(BigDecimal R46_COLUMN_K) {
		this.R46_COLUMN_K = R46_COLUMN_K;
	}

	public BigDecimal getR46_COLUMN_L() {
		return R46_COLUMN_L;
	}

	public void setR46_COLUMN_L(BigDecimal R46_COLUMN_L) {
		this.R46_COLUMN_L = R46_COLUMN_L;
	}

	public BigDecimal getR46_COLUMN_M() {
		return R46_COLUMN_M;
	}

	public void setR46_COLUMN_M(BigDecimal R46_COLUMN_M) {
		this.R46_COLUMN_M = R46_COLUMN_M;
	}

	public BigDecimal getR46_COLUMN_N() {
		return R46_COLUMN_N;
	}

	public void setR46_COLUMN_N(BigDecimal R46_COLUMN_N) {
		this.R46_COLUMN_N = R46_COLUMN_N;
	}

	public String getR47_COLUMN_A() {
		return R47_COLUMN_A;
	}

	public void setR47_COLUMN_A(String R47_COLUMN_A) {
		this.R47_COLUMN_A = R47_COLUMN_A;
	}

	public BigDecimal getR47_COLUMN_B() {
		return R47_COLUMN_B;
	}

	public void setR47_COLUMN_B(BigDecimal R47_COLUMN_B) {
		this.R47_COLUMN_B = R47_COLUMN_B;
	}

	public BigDecimal getR47_COLUMN_C() {
		return R47_COLUMN_C;
	}

	public void setR47_COLUMN_C(BigDecimal R47_COLUMN_C) {
		this.R47_COLUMN_C = R47_COLUMN_C;
	}

	public BigDecimal getR47_COLUMN_D() {
		return R47_COLUMN_D;
	}

	public void setR47_COLUMN_D(BigDecimal R47_COLUMN_D) {
		this.R47_COLUMN_D = R47_COLUMN_D;
	}

	public BigDecimal getR47_COLUMN_E() {
		return R47_COLUMN_E;
	}

	public void setR47_COLUMN_E(BigDecimal R47_COLUMN_E) {
		this.R47_COLUMN_E = R47_COLUMN_E;
	}

	public BigDecimal getR47_COLUMN_F() {
		return R47_COLUMN_F;
	}

	public void setR47_COLUMN_F(BigDecimal R47_COLUMN_F) {
		this.R47_COLUMN_F = R47_COLUMN_F;
	}

	public BigDecimal getR47_COLUMN_G() {
		return R47_COLUMN_G;
	}

	public void setR47_COLUMN_G(BigDecimal R47_COLUMN_G) {
		this.R47_COLUMN_G = R47_COLUMN_G;
	}

	public BigDecimal getR47_COLUMN_H() {
		return R47_COLUMN_H;
	}

	public void setR47_COLUMN_H(BigDecimal R47_COLUMN_H) {
		this.R47_COLUMN_H = R47_COLUMN_H;
	}

	public BigDecimal getR47_COLUMN_I() {
		return R47_COLUMN_I;
	}

	public void setR47_COLUMN_I(BigDecimal R47_COLUMN_I) {
		this.R47_COLUMN_I = R47_COLUMN_I;
	}

	public BigDecimal getR47_COLUMN_J() {
		return R47_COLUMN_J;
	}

	public void setR47_COLUMN_J(BigDecimal R47_COLUMN_J) {
		this.R47_COLUMN_J = R47_COLUMN_J;
	}

	public BigDecimal getR47_COLUMN_K() {
		return R47_COLUMN_K;
	}

	public void setR47_COLUMN_K(BigDecimal R47_COLUMN_K) {
		this.R47_COLUMN_K = R47_COLUMN_K;
	}

	public BigDecimal getR47_COLUMN_L() {
		return R47_COLUMN_L;
	}

	public void setR47_COLUMN_L(BigDecimal R47_COLUMN_L) {
		this.R47_COLUMN_L = R47_COLUMN_L;
	}

	public BigDecimal getR47_COLUMN_M() {
		return R47_COLUMN_M;
	}

	public void setR47_COLUMN_M(BigDecimal R47_COLUMN_M) {
		this.R47_COLUMN_M = R47_COLUMN_M;
	}

	public BigDecimal getR47_COLUMN_N() {
		return R47_COLUMN_N;
	}

	public void setR47_COLUMN_N(BigDecimal R47_COLUMN_N) {
		this.R47_COLUMN_N = R47_COLUMN_N;
	}

	public String getR48_COLUMN_A() {
		return R48_COLUMN_A;
	}

	public void setR48_COLUMN_A(String R48_COLUMN_A) {
		this.R48_COLUMN_A = R48_COLUMN_A;
	}

	public BigDecimal getR48_COLUMN_B() {
		return R48_COLUMN_B;
	}

	public void setR48_COLUMN_B(BigDecimal R48_COLUMN_B) {
		this.R48_COLUMN_B = R48_COLUMN_B;
	}

	public BigDecimal getR48_COLUMN_C() {
		return R48_COLUMN_C;
	}

	public void setR48_COLUMN_C(BigDecimal R48_COLUMN_C) {
		this.R48_COLUMN_C = R48_COLUMN_C;
	}

	public BigDecimal getR48_COLUMN_D() {
		return R48_COLUMN_D;
	}

	public void setR48_COLUMN_D(BigDecimal R48_COLUMN_D) {
		this.R48_COLUMN_D = R48_COLUMN_D;
	}

	public BigDecimal getR48_COLUMN_E() {
		return R48_COLUMN_E;
	}

	public void setR48_COLUMN_E(BigDecimal R48_COLUMN_E) {
		this.R48_COLUMN_E = R48_COLUMN_E;
	}

	public BigDecimal getR48_COLUMN_F() {
		return R48_COLUMN_F;
	}

	public void setR48_COLUMN_F(BigDecimal R48_COLUMN_F) {
		this.R48_COLUMN_F = R48_COLUMN_F;
	}

	public BigDecimal getR48_COLUMN_G() {
		return R48_COLUMN_G;
	}

	public void setR48_COLUMN_G(BigDecimal R48_COLUMN_G) {
		this.R48_COLUMN_G = R48_COLUMN_G;
	}

	public BigDecimal getR48_COLUMN_H() {
		return R48_COLUMN_H;
	}

	public void setR48_COLUMN_H(BigDecimal R48_COLUMN_H) {
		this.R48_COLUMN_H = R48_COLUMN_H;
	}

	public BigDecimal getR48_COLUMN_I() {
		return R48_COLUMN_I;
	}

	public void setR48_COLUMN_I(BigDecimal R48_COLUMN_I) {
		this.R48_COLUMN_I = R48_COLUMN_I;
	}

	public BigDecimal getR48_COLUMN_J() {
		return R48_COLUMN_J;
	}

	public void setR48_COLUMN_J(BigDecimal R48_COLUMN_J) {
		this.R48_COLUMN_J = R48_COLUMN_J;
	}

	public BigDecimal getR48_COLUMN_K() {
		return R48_COLUMN_K;
	}

	public void setR48_COLUMN_K(BigDecimal R48_COLUMN_K) {
		this.R48_COLUMN_K = R48_COLUMN_K;
	}

	public BigDecimal getR48_COLUMN_L() {
		return R48_COLUMN_L;
	}

	public void setR48_COLUMN_L(BigDecimal R48_COLUMN_L) {
		this.R48_COLUMN_L = R48_COLUMN_L;
	}

	public BigDecimal getR48_COLUMN_M() {
		return R48_COLUMN_M;
	}

	public void setR48_COLUMN_M(BigDecimal R48_COLUMN_M) {
		this.R48_COLUMN_M = R48_COLUMN_M;
	}

	public BigDecimal getR48_COLUMN_N() {
		return R48_COLUMN_N;
	}

	public void setR48_COLUMN_N(BigDecimal R48_COLUMN_N) {
		this.R48_COLUMN_N = R48_COLUMN_N;
	}

	public String getR49_COLUMN_A() {
		return R49_COLUMN_A;
	}

	public void setR49_COLUMN_A(String R49_COLUMN_A) {
		this.R49_COLUMN_A = R49_COLUMN_A;
	}

	public BigDecimal getR49_COLUMN_B() {
		return R49_COLUMN_B;
	}

	public void setR49_COLUMN_B(BigDecimal R49_COLUMN_B) {
		this.R49_COLUMN_B = R49_COLUMN_B;
	}

	public BigDecimal getR49_COLUMN_C() {
		return R49_COLUMN_C;
	}

	public void setR49_COLUMN_C(BigDecimal R49_COLUMN_C) {
		this.R49_COLUMN_C = R49_COLUMN_C;
	}

	public BigDecimal getR49_COLUMN_D() {
		return R49_COLUMN_D;
	}

	public void setR49_COLUMN_D(BigDecimal R49_COLUMN_D) {
		this.R49_COLUMN_D = R49_COLUMN_D;
	}

	public BigDecimal getR49_COLUMN_E() {
		return R49_COLUMN_E;
	}

	public void setR49_COLUMN_E(BigDecimal R49_COLUMN_E) {
		this.R49_COLUMN_E = R49_COLUMN_E;
	}

	public BigDecimal getR49_COLUMN_F() {
		return R49_COLUMN_F;
	}

	public void setR49_COLUMN_F(BigDecimal R49_COLUMN_F) {
		this.R49_COLUMN_F = R49_COLUMN_F;
	}

	public BigDecimal getR49_COLUMN_G() {
		return R49_COLUMN_G;
	}

	public void setR49_COLUMN_G(BigDecimal R49_COLUMN_G) {
		this.R49_COLUMN_G = R49_COLUMN_G;
	}

	public BigDecimal getR49_COLUMN_H() {
		return R49_COLUMN_H;
	}

	public void setR49_COLUMN_H(BigDecimal R49_COLUMN_H) {
		this.R49_COLUMN_H = R49_COLUMN_H;
	}

	public BigDecimal getR49_COLUMN_I() {
		return R49_COLUMN_I;
	}

	public void setR49_COLUMN_I(BigDecimal R49_COLUMN_I) {
		this.R49_COLUMN_I = R49_COLUMN_I;
	}

	public BigDecimal getR49_COLUMN_J() {
		return R49_COLUMN_J;
	}

	public void setR49_COLUMN_J(BigDecimal R49_COLUMN_J) {
		this.R49_COLUMN_J = R49_COLUMN_J;
	}

	public BigDecimal getR49_COLUMN_K() {
		return R49_COLUMN_K;
	}

	public void setR49_COLUMN_K(BigDecimal R49_COLUMN_K) {
		this.R49_COLUMN_K = R49_COLUMN_K;
	}

	public BigDecimal getR49_COLUMN_L() {
		return R49_COLUMN_L;
	}

	public void setR49_COLUMN_L(BigDecimal R49_COLUMN_L) {
		this.R49_COLUMN_L = R49_COLUMN_L;
	}

	public BigDecimal getR49_COLUMN_M() {
		return R49_COLUMN_M;
	}

	public void setR49_COLUMN_M(BigDecimal R49_COLUMN_M) {
		this.R49_COLUMN_M = R49_COLUMN_M;
	}

	public BigDecimal getR49_COLUMN_N() {
		return R49_COLUMN_N;
	}

	public void setR49_COLUMN_N(BigDecimal R49_COLUMN_N) {
		this.R49_COLUMN_N = R49_COLUMN_N;
	}

	public String getR50_COLUMN_A() {
		return R50_COLUMN_A;
	}

	public void setR50_COLUMN_A(String R50_COLUMN_A) {
		this.R50_COLUMN_A = R50_COLUMN_A;
	}

	public BigDecimal getR50_COLUMN_B() {
		return R50_COLUMN_B;
	}

	public void setR50_COLUMN_B(BigDecimal R50_COLUMN_B) {
		this.R50_COLUMN_B = R50_COLUMN_B;
	}

	public BigDecimal getR50_COLUMN_C() {
		return R50_COLUMN_C;
	}

	public void setR50_COLUMN_C(BigDecimal R50_COLUMN_C) {
		this.R50_COLUMN_C = R50_COLUMN_C;
	}

	public BigDecimal getR50_COLUMN_D() {
		return R50_COLUMN_D;
	}

	public void setR50_COLUMN_D(BigDecimal R50_COLUMN_D) {
		this.R50_COLUMN_D = R50_COLUMN_D;
	}

	public BigDecimal getR50_COLUMN_E() {
		return R50_COLUMN_E;
	}

	public void setR50_COLUMN_E(BigDecimal R50_COLUMN_E) {
		this.R50_COLUMN_E = R50_COLUMN_E;
	}

	public BigDecimal getR50_COLUMN_F() {
		return R50_COLUMN_F;
	}

	public void setR50_COLUMN_F(BigDecimal R50_COLUMN_F) {
		this.R50_COLUMN_F = R50_COLUMN_F;
	}

	public BigDecimal getR50_COLUMN_G() {
		return R50_COLUMN_G;
	}

	public void setR50_COLUMN_G(BigDecimal R50_COLUMN_G) {
		this.R50_COLUMN_G = R50_COLUMN_G;
	}

	public BigDecimal getR50_COLUMN_H() {
		return R50_COLUMN_H;
	}

	public void setR50_COLUMN_H(BigDecimal R50_COLUMN_H) {
		this.R50_COLUMN_H = R50_COLUMN_H;
	}

	public BigDecimal getR50_COLUMN_I() {
		return R50_COLUMN_I;
	}

	public void setR50_COLUMN_I(BigDecimal R50_COLUMN_I) {
		this.R50_COLUMN_I = R50_COLUMN_I;
	}

	public BigDecimal getR50_COLUMN_J() {
		return R50_COLUMN_J;
	}

	public void setR50_COLUMN_J(BigDecimal R50_COLUMN_J) {
		this.R50_COLUMN_J = R50_COLUMN_J;
	}

	public BigDecimal getR50_COLUMN_K() {
		return R50_COLUMN_K;
	}

	public void setR50_COLUMN_K(BigDecimal R50_COLUMN_K) {
		this.R50_COLUMN_K = R50_COLUMN_K;
	}

	public BigDecimal getR50_COLUMN_L() {
		return R50_COLUMN_L;
	}

	public void setR50_COLUMN_L(BigDecimal R50_COLUMN_L) {
		this.R50_COLUMN_L = R50_COLUMN_L;
	}

	public BigDecimal getR50_COLUMN_M() {
		return R50_COLUMN_M;
	}

	public void setR50_COLUMN_M(BigDecimal R50_COLUMN_M) {
		this.R50_COLUMN_M = R50_COLUMN_M;
	}

	public BigDecimal getR50_COLUMN_N() {
		return R50_COLUMN_N;
	}

	public void setR50_COLUMN_N(BigDecimal R50_COLUMN_N) {
		this.R50_COLUMN_N = R50_COLUMN_N;
	}

	public String getR51_COLUMN_A() {
		return R51_COLUMN_A;
	}

	public void setR51_COLUMN_A(String R51_COLUMN_A) {
		this.R51_COLUMN_A = R51_COLUMN_A;
	}

	public BigDecimal getR51_COLUMN_B() {
		return R51_COLUMN_B;
	}

	public void setR51_COLUMN_B(BigDecimal R51_COLUMN_B) {
		this.R51_COLUMN_B = R51_COLUMN_B;
	}

	public BigDecimal getR51_COLUMN_C() {
		return R51_COLUMN_C;
	}

	public void setR51_COLUMN_C(BigDecimal R51_COLUMN_C) {
		this.R51_COLUMN_C = R51_COLUMN_C;
	}

	public BigDecimal getR51_COLUMN_D() {
		return R51_COLUMN_D;
	}

	public void setR51_COLUMN_D(BigDecimal R51_COLUMN_D) {
		this.R51_COLUMN_D = R51_COLUMN_D;
	}

	public BigDecimal getR51_COLUMN_E() {
		return R51_COLUMN_E;
	}

	public void setR51_COLUMN_E(BigDecimal R51_COLUMN_E) {
		this.R51_COLUMN_E = R51_COLUMN_E;
	}

	public BigDecimal getR51_COLUMN_F() {
		return R51_COLUMN_F;
	}

	public void setR51_COLUMN_F(BigDecimal R51_COLUMN_F) {
		this.R51_COLUMN_F = R51_COLUMN_F;
	}

	public BigDecimal getR51_COLUMN_G() {
		return R51_COLUMN_G;
	}

	public void setR51_COLUMN_G(BigDecimal R51_COLUMN_G) {
		this.R51_COLUMN_G = R51_COLUMN_G;
	}

	public BigDecimal getR51_COLUMN_H() {
		return R51_COLUMN_H;
	}

	public void setR51_COLUMN_H(BigDecimal R51_COLUMN_H) {
		this.R51_COLUMN_H = R51_COLUMN_H;
	}

	public BigDecimal getR51_COLUMN_I() {
		return R51_COLUMN_I;
	}

	public void setR51_COLUMN_I(BigDecimal R51_COLUMN_I) {
		this.R51_COLUMN_I = R51_COLUMN_I;
	}

	public BigDecimal getR51_COLUMN_J() {
		return R51_COLUMN_J;
	}

	public void setR51_COLUMN_J(BigDecimal R51_COLUMN_J) {
		this.R51_COLUMN_J = R51_COLUMN_J;
	}

	public BigDecimal getR51_COLUMN_K() {
		return R51_COLUMN_K;
	}

	public void setR51_COLUMN_K(BigDecimal R51_COLUMN_K) {
		this.R51_COLUMN_K = R51_COLUMN_K;
	}

	public BigDecimal getR51_COLUMN_L() {
		return R51_COLUMN_L;
	}

	public void setR51_COLUMN_L(BigDecimal R51_COLUMN_L) {
		this.R51_COLUMN_L = R51_COLUMN_L;
	}

	public BigDecimal getR51_COLUMN_M() {
		return R51_COLUMN_M;
	}

	public void setR51_COLUMN_M(BigDecimal R51_COLUMN_M) {
		this.R51_COLUMN_M = R51_COLUMN_M;
	}

	public BigDecimal getR51_COLUMN_N() {
		return R51_COLUMN_N;
	}

	public void setR51_COLUMN_N(BigDecimal R51_COLUMN_N) {
		this.R51_COLUMN_N = R51_COLUMN_N;
	}

	public String getR52_COLUMN_A() {
		return R52_COLUMN_A;
	}

	public void setR52_COLUMN_A(String R52_COLUMN_A) {
		this.R52_COLUMN_A = R52_COLUMN_A;
	}

	public BigDecimal getR52_COLUMN_B() {
		return R52_COLUMN_B;
	}

	public void setR52_COLUMN_B(BigDecimal R52_COLUMN_B) {
		this.R52_COLUMN_B = R52_COLUMN_B;
	}

	public BigDecimal getR52_COLUMN_C() {
		return R52_COLUMN_C;
	}

	public void setR52_COLUMN_C(BigDecimal R52_COLUMN_C) {
		this.R52_COLUMN_C = R52_COLUMN_C;
	}

	public BigDecimal getR52_COLUMN_D() {
		return R52_COLUMN_D;
	}

	public void setR52_COLUMN_D(BigDecimal R52_COLUMN_D) {
		this.R52_COLUMN_D = R52_COLUMN_D;
	}

	public BigDecimal getR52_COLUMN_E() {
		return R52_COLUMN_E;
	}

	public void setR52_COLUMN_E(BigDecimal R52_COLUMN_E) {
		this.R52_COLUMN_E = R52_COLUMN_E;
	}

	public BigDecimal getR52_COLUMN_F() {
		return R52_COLUMN_F;
	}

	public void setR52_COLUMN_F(BigDecimal R52_COLUMN_F) {
		this.R52_COLUMN_F = R52_COLUMN_F;
	}

	public BigDecimal getR52_COLUMN_G() {
		return R52_COLUMN_G;
	}

	public void setR52_COLUMN_G(BigDecimal R52_COLUMN_G) {
		this.R52_COLUMN_G = R52_COLUMN_G;
	}

	public BigDecimal getR52_COLUMN_H() {
		return R52_COLUMN_H;
	}

	public void setR52_COLUMN_H(BigDecimal R52_COLUMN_H) {
		this.R52_COLUMN_H = R52_COLUMN_H;
	}

	public BigDecimal getR52_COLUMN_I() {
		return R52_COLUMN_I;
	}

	public void setR52_COLUMN_I(BigDecimal R52_COLUMN_I) {
		this.R52_COLUMN_I = R52_COLUMN_I;
	}

	public BigDecimal getR52_COLUMN_J() {
		return R52_COLUMN_J;
	}

	public void setR52_COLUMN_J(BigDecimal R52_COLUMN_J) {
		this.R52_COLUMN_J = R52_COLUMN_J;
	}

	public BigDecimal getR52_COLUMN_K() {
		return R52_COLUMN_K;
	}

	public void setR52_COLUMN_K(BigDecimal R52_COLUMN_K) {
		this.R52_COLUMN_K = R52_COLUMN_K;
	}

	public BigDecimal getR52_COLUMN_L() {
		return R52_COLUMN_L;
	}

	public void setR52_COLUMN_L(BigDecimal R52_COLUMN_L) {
		this.R52_COLUMN_L = R52_COLUMN_L;
	}

	public BigDecimal getR52_COLUMN_M() {
		return R52_COLUMN_M;
	}

	public void setR52_COLUMN_M(BigDecimal R52_COLUMN_M) {
		this.R52_COLUMN_M = R52_COLUMN_M;
	}

	public BigDecimal getR52_COLUMN_N() {
		return R52_COLUMN_N;
	}

	public void setR52_COLUMN_N(BigDecimal R52_COLUMN_N) {
		this.R52_COLUMN_N = R52_COLUMN_N;
	}

	public String getR53_COLUMN_A() {
		return R53_COLUMN_A;
	}

	public void setR53_COLUMN_A(String R53_COLUMN_A) {
		this.R53_COLUMN_A = R53_COLUMN_A;
	}

	public BigDecimal getR53_COLUMN_B() {
		return R53_COLUMN_B;
	}

	public void setR53_COLUMN_B(BigDecimal R53_COLUMN_B) {
		this.R53_COLUMN_B = R53_COLUMN_B;
	}

	public BigDecimal getR53_COLUMN_C() {
		return R53_COLUMN_C;
	}

	public void setR53_COLUMN_C(BigDecimal R53_COLUMN_C) {
		this.R53_COLUMN_C = R53_COLUMN_C;
	}

	public BigDecimal getR53_COLUMN_D() {
		return R53_COLUMN_D;
	}

	public void setR53_COLUMN_D(BigDecimal R53_COLUMN_D) {
		this.R53_COLUMN_D = R53_COLUMN_D;
	}

	public BigDecimal getR53_COLUMN_E() {
		return R53_COLUMN_E;
	}

	public void setR53_COLUMN_E(BigDecimal R53_COLUMN_E) {
		this.R53_COLUMN_E = R53_COLUMN_E;
	}

	public BigDecimal getR53_COLUMN_F() {
		return R53_COLUMN_F;
	}

	public void setR53_COLUMN_F(BigDecimal R53_COLUMN_F) {
		this.R53_COLUMN_F = R53_COLUMN_F;
	}

	public BigDecimal getR53_COLUMN_G() {
		return R53_COLUMN_G;
	}

	public void setR53_COLUMN_G(BigDecimal R53_COLUMN_G) {
		this.R53_COLUMN_G = R53_COLUMN_G;
	}

	public BigDecimal getR53_COLUMN_H() {
		return R53_COLUMN_H;
	}

	public void setR53_COLUMN_H(BigDecimal R53_COLUMN_H) {
		this.R53_COLUMN_H = R53_COLUMN_H;
	}

	public BigDecimal getR53_COLUMN_I() {
		return R53_COLUMN_I;
	}

	public void setR53_COLUMN_I(BigDecimal R53_COLUMN_I) {
		this.R53_COLUMN_I = R53_COLUMN_I;
	}

	public BigDecimal getR53_COLUMN_J() {
		return R53_COLUMN_J;
	}

	public void setR53_COLUMN_J(BigDecimal R53_COLUMN_J) {
		this.R53_COLUMN_J = R53_COLUMN_J;
	}

	public BigDecimal getR53_COLUMN_K() {
		return R53_COLUMN_K;
	}

	public void setR53_COLUMN_K(BigDecimal R53_COLUMN_K) {
		this.R53_COLUMN_K = R53_COLUMN_K;
	}

	public BigDecimal getR53_COLUMN_L() {
		return R53_COLUMN_L;
	}

	public void setR53_COLUMN_L(BigDecimal R53_COLUMN_L) {
		this.R53_COLUMN_L = R53_COLUMN_L;
	}

	public BigDecimal getR53_COLUMN_M() {
		return R53_COLUMN_M;
	}

	public void setR53_COLUMN_M(BigDecimal R53_COLUMN_M) {
		this.R53_COLUMN_M = R53_COLUMN_M;
	}

	public BigDecimal getR53_COLUMN_N() {
		return R53_COLUMN_N;
	}

	public void setR53_COLUMN_N(BigDecimal R53_COLUMN_N) {
		this.R53_COLUMN_N = R53_COLUMN_N;
	}

	public String getR54_COLUMN_A() {
		return R54_COLUMN_A;
	}

	public void setR54_COLUMN_A(String R54_COLUMN_A) {
		this.R54_COLUMN_A = R54_COLUMN_A;
	}

	public BigDecimal getR54_COLUMN_B() {
		return R54_COLUMN_B;
	}

	public void setR54_COLUMN_B(BigDecimal R54_COLUMN_B) {
		this.R54_COLUMN_B = R54_COLUMN_B;
	}

	public BigDecimal getR54_COLUMN_C() {
		return R54_COLUMN_C;
	}

	public void setR54_COLUMN_C(BigDecimal R54_COLUMN_C) {
		this.R54_COLUMN_C = R54_COLUMN_C;
	}

	public BigDecimal getR54_COLUMN_D() {
		return R54_COLUMN_D;
	}

	public void setR54_COLUMN_D(BigDecimal R54_COLUMN_D) {
		this.R54_COLUMN_D = R54_COLUMN_D;
	}

	public BigDecimal getR54_COLUMN_E() {
		return R54_COLUMN_E;
	}

	public void setR54_COLUMN_E(BigDecimal R54_COLUMN_E) {
		this.R54_COLUMN_E = R54_COLUMN_E;
	}

	public BigDecimal getR54_COLUMN_F() {
		return R54_COLUMN_F;
	}

	public void setR54_COLUMN_F(BigDecimal R54_COLUMN_F) {
		this.R54_COLUMN_F = R54_COLUMN_F;
	}

	public BigDecimal getR54_COLUMN_G() {
		return R54_COLUMN_G;
	}

	public void setR54_COLUMN_G(BigDecimal R54_COLUMN_G) {
		this.R54_COLUMN_G = R54_COLUMN_G;
	}

	public BigDecimal getR54_COLUMN_H() {
		return R54_COLUMN_H;
	}

	public void setR54_COLUMN_H(BigDecimal R54_COLUMN_H) {
		this.R54_COLUMN_H = R54_COLUMN_H;
	}

	public BigDecimal getR54_COLUMN_I() {
		return R54_COLUMN_I;
	}

	public void setR54_COLUMN_I(BigDecimal R54_COLUMN_I) {
		this.R54_COLUMN_I = R54_COLUMN_I;
	}

	public BigDecimal getR54_COLUMN_J() {
		return R54_COLUMN_J;
	}

	public void setR54_COLUMN_J(BigDecimal R54_COLUMN_J) {
		this.R54_COLUMN_J = R54_COLUMN_J;
	}

	public BigDecimal getR54_COLUMN_K() {
		return R54_COLUMN_K;
	}

	public void setR54_COLUMN_K(BigDecimal R54_COLUMN_K) {
		this.R54_COLUMN_K = R54_COLUMN_K;
	}

	public BigDecimal getR54_COLUMN_L() {
		return R54_COLUMN_L;
	}

	public void setR54_COLUMN_L(BigDecimal R54_COLUMN_L) {
		this.R54_COLUMN_L = R54_COLUMN_L;
	}

	public BigDecimal getR54_COLUMN_M() {
		return R54_COLUMN_M;
	}

	public void setR54_COLUMN_M(BigDecimal R54_COLUMN_M) {
		this.R54_COLUMN_M = R54_COLUMN_M;
	}

	public BigDecimal getR54_COLUMN_N() {
		return R54_COLUMN_N;
	}

	public void setR54_COLUMN_N(BigDecimal R54_COLUMN_N) {
		this.R54_COLUMN_N = R54_COLUMN_N;
	}

	public String getR55_COLUMN_A() {
		return R55_COLUMN_A;
	}

	public void setR55_COLUMN_A(String R55_COLUMN_A) {
		this.R55_COLUMN_A = R55_COLUMN_A;
	}

	public BigDecimal getR55_COLUMN_B() {
		return R55_COLUMN_B;
	}

	public void setR55_COLUMN_B(BigDecimal R55_COLUMN_B) {
		this.R55_COLUMN_B = R55_COLUMN_B;
	}

	public BigDecimal getR55_COLUMN_C() {
		return R55_COLUMN_C;
	}

	public void setR55_COLUMN_C(BigDecimal R55_COLUMN_C) {
		this.R55_COLUMN_C = R55_COLUMN_C;
	}

	public BigDecimal getR55_COLUMN_D() {
		return R55_COLUMN_D;
	}

	public void setR55_COLUMN_D(BigDecimal R55_COLUMN_D) {
		this.R55_COLUMN_D = R55_COLUMN_D;
	}

	public BigDecimal getR55_COLUMN_E() {
		return R55_COLUMN_E;
	}

	public void setR55_COLUMN_E(BigDecimal R55_COLUMN_E) {
		this.R55_COLUMN_E = R55_COLUMN_E;
	}

	public BigDecimal getR55_COLUMN_F() {
		return R55_COLUMN_F;
	}

	public void setR55_COLUMN_F(BigDecimal R55_COLUMN_F) {
		this.R55_COLUMN_F = R55_COLUMN_F;
	}

	public BigDecimal getR55_COLUMN_G() {
		return R55_COLUMN_G;
	}

	public void setR55_COLUMN_G(BigDecimal R55_COLUMN_G) {
		this.R55_COLUMN_G = R55_COLUMN_G;
	}

	public BigDecimal getR55_COLUMN_H() {
		return R55_COLUMN_H;
	}

	public void setR55_COLUMN_H(BigDecimal R55_COLUMN_H) {
		this.R55_COLUMN_H = R55_COLUMN_H;
	}

	public BigDecimal getR55_COLUMN_I() {
		return R55_COLUMN_I;
	}

	public void setR55_COLUMN_I(BigDecimal R55_COLUMN_I) {
		this.R55_COLUMN_I = R55_COLUMN_I;
	}

	public BigDecimal getR55_COLUMN_J() {
		return R55_COLUMN_J;
	}

	public void setR55_COLUMN_J(BigDecimal R55_COLUMN_J) {
		this.R55_COLUMN_J = R55_COLUMN_J;
	}

	public BigDecimal getR55_COLUMN_K() {
		return R55_COLUMN_K;
	}

	public void setR55_COLUMN_K(BigDecimal R55_COLUMN_K) {
		this.R55_COLUMN_K = R55_COLUMN_K;
	}

	public BigDecimal getR55_COLUMN_L() {
		return R55_COLUMN_L;
	}

	public void setR55_COLUMN_L(BigDecimal R55_COLUMN_L) {
		this.R55_COLUMN_L = R55_COLUMN_L;
	}

	public BigDecimal getR55_COLUMN_M() {
		return R55_COLUMN_M;
	}

	public void setR55_COLUMN_M(BigDecimal R55_COLUMN_M) {
		this.R55_COLUMN_M = R55_COLUMN_M;
	}

	public BigDecimal getR55_COLUMN_N() {
		return R55_COLUMN_N;
	}

	public void setR55_COLUMN_N(BigDecimal R55_COLUMN_N) {
		this.R55_COLUMN_N = R55_COLUMN_N;
	}

	public String getR56_COLUMN_A() {
		return R56_COLUMN_A;
	}

	public void setR56_COLUMN_A(String R56_COLUMN_A) {
		this.R56_COLUMN_A = R56_COLUMN_A;
	}

	public BigDecimal getR56_COLUMN_B() {
		return R56_COLUMN_B;
	}

	public void setR56_COLUMN_B(BigDecimal R56_COLUMN_B) {
		this.R56_COLUMN_B = R56_COLUMN_B;
	}

	public BigDecimal getR56_COLUMN_C() {
		return R56_COLUMN_C;
	}

	public void setR56_COLUMN_C(BigDecimal R56_COLUMN_C) {
		this.R56_COLUMN_C = R56_COLUMN_C;
	}

	public BigDecimal getR56_COLUMN_D() {
		return R56_COLUMN_D;
	}

	public void setR56_COLUMN_D(BigDecimal R56_COLUMN_D) {
		this.R56_COLUMN_D = R56_COLUMN_D;
	}

	public BigDecimal getR56_COLUMN_E() {
		return R56_COLUMN_E;
	}

	public void setR56_COLUMN_E(BigDecimal R56_COLUMN_E) {
		this.R56_COLUMN_E = R56_COLUMN_E;
	}

	public BigDecimal getR56_COLUMN_F() {
		return R56_COLUMN_F;
	}

	public void setR56_COLUMN_F(BigDecimal R56_COLUMN_F) {
		this.R56_COLUMN_F = R56_COLUMN_F;
	}

	public BigDecimal getR56_COLUMN_G() {
		return R56_COLUMN_G;
	}

	public void setR56_COLUMN_G(BigDecimal R56_COLUMN_G) {
		this.R56_COLUMN_G = R56_COLUMN_G;
	}

	public BigDecimal getR56_COLUMN_H() {
		return R56_COLUMN_H;
	}

	public void setR56_COLUMN_H(BigDecimal R56_COLUMN_H) {
		this.R56_COLUMN_H = R56_COLUMN_H;
	}

	public BigDecimal getR56_COLUMN_I() {
		return R56_COLUMN_I;
	}

	public void setR56_COLUMN_I(BigDecimal R56_COLUMN_I) {
		this.R56_COLUMN_I = R56_COLUMN_I;
	}

	public BigDecimal getR56_COLUMN_J() {
		return R56_COLUMN_J;
	}

	public void setR56_COLUMN_J(BigDecimal R56_COLUMN_J) {
		this.R56_COLUMN_J = R56_COLUMN_J;
	}

	public BigDecimal getR56_COLUMN_K() {
		return R56_COLUMN_K;
	}

	public void setR56_COLUMN_K(BigDecimal R56_COLUMN_K) {
		this.R56_COLUMN_K = R56_COLUMN_K;
	}

	public BigDecimal getR56_COLUMN_L() {
		return R56_COLUMN_L;
	}

	public void setR56_COLUMN_L(BigDecimal R56_COLUMN_L) {
		this.R56_COLUMN_L = R56_COLUMN_L;
	}

	public BigDecimal getR56_COLUMN_M() {
		return R56_COLUMN_M;
	}

	public void setR56_COLUMN_M(BigDecimal R56_COLUMN_M) {
		this.R56_COLUMN_M = R56_COLUMN_M;
	}

	public BigDecimal getR56_COLUMN_N() {
		return R56_COLUMN_N;
	}

	public void setR56_COLUMN_N(BigDecimal R56_COLUMN_N) {
		this.R56_COLUMN_N = R56_COLUMN_N;
	}

	public String getR57_COLUMN_A() {
		return R57_COLUMN_A;
	}

	public void setR57_COLUMN_A(String R57_COLUMN_A) {
		this.R57_COLUMN_A = R57_COLUMN_A;
	}

	public BigDecimal getR57_COLUMN_B() {
		return R57_COLUMN_B;
	}

	public void setR57_COLUMN_B(BigDecimal R57_COLUMN_B) {
		this.R57_COLUMN_B = R57_COLUMN_B;
	}

	public BigDecimal getR57_COLUMN_C() {
		return R57_COLUMN_C;
	}

	public void setR57_COLUMN_C(BigDecimal R57_COLUMN_C) {
		this.R57_COLUMN_C = R57_COLUMN_C;
	}

	public BigDecimal getR57_COLUMN_D() {
		return R57_COLUMN_D;
	}

	public void setR57_COLUMN_D(BigDecimal R57_COLUMN_D) {
		this.R57_COLUMN_D = R57_COLUMN_D;
	}

	public BigDecimal getR57_COLUMN_E() {
		return R57_COLUMN_E;
	}

	public void setR57_COLUMN_E(BigDecimal R57_COLUMN_E) {
		this.R57_COLUMN_E = R57_COLUMN_E;
	}

	public BigDecimal getR57_COLUMN_F() {
		return R57_COLUMN_F;
	}

	public void setR57_COLUMN_F(BigDecimal R57_COLUMN_F) {
		this.R57_COLUMN_F = R57_COLUMN_F;
	}

	public BigDecimal getR57_COLUMN_G() {
		return R57_COLUMN_G;
	}

	public void setR57_COLUMN_G(BigDecimal R57_COLUMN_G) {
		this.R57_COLUMN_G = R57_COLUMN_G;
	}

	public BigDecimal getR57_COLUMN_H() {
		return R57_COLUMN_H;
	}

	public void setR57_COLUMN_H(BigDecimal R57_COLUMN_H) {
		this.R57_COLUMN_H = R57_COLUMN_H;
	}

	public BigDecimal getR57_COLUMN_I() {
		return R57_COLUMN_I;
	}

	public void setR57_COLUMN_I(BigDecimal R57_COLUMN_I) {
		this.R57_COLUMN_I = R57_COLUMN_I;
	}

	public BigDecimal getR57_COLUMN_J() {
		return R57_COLUMN_J;
	}

	public void setR57_COLUMN_J(BigDecimal R57_COLUMN_J) {
		this.R57_COLUMN_J = R57_COLUMN_J;
	}

	public BigDecimal getR57_COLUMN_K() {
		return R57_COLUMN_K;
	}

	public void setR57_COLUMN_K(BigDecimal R57_COLUMN_K) {
		this.R57_COLUMN_K = R57_COLUMN_K;
	}

	public BigDecimal getR57_COLUMN_L() {
		return R57_COLUMN_L;
	}

	public void setR57_COLUMN_L(BigDecimal R57_COLUMN_L) {
		this.R57_COLUMN_L = R57_COLUMN_L;
	}

	public BigDecimal getR57_COLUMN_M() {
		return R57_COLUMN_M;
	}

	public void setR57_COLUMN_M(BigDecimal R57_COLUMN_M) {
		this.R57_COLUMN_M = R57_COLUMN_M;
	}

	public BigDecimal getR57_COLUMN_N() {
		return R57_COLUMN_N;
	}

	public void setR57_COLUMN_N(BigDecimal R57_COLUMN_N) {
		this.R57_COLUMN_N = R57_COLUMN_N;
	}

	public String getR58_COLUMN_A() {
		return R58_COLUMN_A;
	}

	public void setR58_COLUMN_A(String R58_COLUMN_A) {
		this.R58_COLUMN_A = R58_COLUMN_A;
	}

	public BigDecimal getR58_COLUMN_B() {
		return R58_COLUMN_B;
	}

	public void setR58_COLUMN_B(BigDecimal R58_COLUMN_B) {
		this.R58_COLUMN_B = R58_COLUMN_B;
	}

	public BigDecimal getR58_COLUMN_C() {
		return R58_COLUMN_C;
	}

	public void setR58_COLUMN_C(BigDecimal R58_COLUMN_C) {
		this.R58_COLUMN_C = R58_COLUMN_C;
	}

	public BigDecimal getR58_COLUMN_D() {
		return R58_COLUMN_D;
	}

	public void setR58_COLUMN_D(BigDecimal R58_COLUMN_D) {
		this.R58_COLUMN_D = R58_COLUMN_D;
	}

	public BigDecimal getR58_COLUMN_E() {
		return R58_COLUMN_E;
	}

	public void setR58_COLUMN_E(BigDecimal R58_COLUMN_E) {
		this.R58_COLUMN_E = R58_COLUMN_E;
	}

	public BigDecimal getR58_COLUMN_F() {
		return R58_COLUMN_F;
	}

	public void setR58_COLUMN_F(BigDecimal R58_COLUMN_F) {
		this.R58_COLUMN_F = R58_COLUMN_F;
	}

	public BigDecimal getR58_COLUMN_G() {
		return R58_COLUMN_G;
	}

	public void setR58_COLUMN_G(BigDecimal R58_COLUMN_G) {
		this.R58_COLUMN_G = R58_COLUMN_G;
	}

	public BigDecimal getR58_COLUMN_H() {
		return R58_COLUMN_H;
	}

	public void setR58_COLUMN_H(BigDecimal R58_COLUMN_H) {
		this.R58_COLUMN_H = R58_COLUMN_H;
	}

	public BigDecimal getR58_COLUMN_I() {
		return R58_COLUMN_I;
	}

	public void setR58_COLUMN_I(BigDecimal R58_COLUMN_I) {
		this.R58_COLUMN_I = R58_COLUMN_I;
	}

	public BigDecimal getR58_COLUMN_J() {
		return R58_COLUMN_J;
	}

	public void setR58_COLUMN_J(BigDecimal R58_COLUMN_J) {
		this.R58_COLUMN_J = R58_COLUMN_J;
	}

	public BigDecimal getR58_COLUMN_K() {
		return R58_COLUMN_K;
	}

	public void setR58_COLUMN_K(BigDecimal R58_COLUMN_K) {
		this.R58_COLUMN_K = R58_COLUMN_K;
	}

	public BigDecimal getR58_COLUMN_L() {
		return R58_COLUMN_L;
	}

	public void setR58_COLUMN_L(BigDecimal R58_COLUMN_L) {
		this.R58_COLUMN_L = R58_COLUMN_L;
	}

	public BigDecimal getR58_COLUMN_M() {
		return R58_COLUMN_M;
	}

	public void setR58_COLUMN_M(BigDecimal R58_COLUMN_M) {
		this.R58_COLUMN_M = R58_COLUMN_M;
	}

	public BigDecimal getR58_COLUMN_N() {
		return R58_COLUMN_N;
	}

	public void setR58_COLUMN_N(BigDecimal R58_COLUMN_N) {
		this.R58_COLUMN_N = R58_COLUMN_N;
	}

	public String getR59_COLUMN_A() {
		return R59_COLUMN_A;
	}

	public void setR59_COLUMN_A(String R59_COLUMN_A) {
		this.R59_COLUMN_A = R59_COLUMN_A;
	}

	public BigDecimal getR59_COLUMN_B() {
		return R59_COLUMN_B;
	}

	public void setR59_COLUMN_B(BigDecimal R59_COLUMN_B) {
		this.R59_COLUMN_B = R59_COLUMN_B;
	}

	public BigDecimal getR59_COLUMN_C() {
		return R59_COLUMN_C;
	}

	public void setR59_COLUMN_C(BigDecimal R59_COLUMN_C) {
		this.R59_COLUMN_C = R59_COLUMN_C;
	}

	public BigDecimal getR59_COLUMN_D() {
		return R59_COLUMN_D;
	}

	public void setR59_COLUMN_D(BigDecimal R59_COLUMN_D) {
		this.R59_COLUMN_D = R59_COLUMN_D;
	}

	public BigDecimal getR59_COLUMN_E() {
		return R59_COLUMN_E;
	}

	public void setR59_COLUMN_E(BigDecimal R59_COLUMN_E) {
		this.R59_COLUMN_E = R59_COLUMN_E;
	}

	public BigDecimal getR59_COLUMN_F() {
		return R59_COLUMN_F;
	}

	public void setR59_COLUMN_F(BigDecimal R59_COLUMN_F) {
		this.R59_COLUMN_F = R59_COLUMN_F;
	}

	public BigDecimal getR59_COLUMN_G() {
		return R59_COLUMN_G;
	}

	public void setR59_COLUMN_G(BigDecimal R59_COLUMN_G) {
		this.R59_COLUMN_G = R59_COLUMN_G;
	}

	public BigDecimal getR59_COLUMN_H() {
		return R59_COLUMN_H;
	}

	public void setR59_COLUMN_H(BigDecimal R59_COLUMN_H) {
		this.R59_COLUMN_H = R59_COLUMN_H;
	}

	public BigDecimal getR59_COLUMN_I() {
		return R59_COLUMN_I;
	}

	public void setR59_COLUMN_I(BigDecimal R59_COLUMN_I) {
		this.R59_COLUMN_I = R59_COLUMN_I;
	}

	public BigDecimal getR59_COLUMN_J() {
		return R59_COLUMN_J;
	}

	public void setR59_COLUMN_J(BigDecimal R59_COLUMN_J) {
		this.R59_COLUMN_J = R59_COLUMN_J;
	}

	public BigDecimal getR59_COLUMN_K() {
		return R59_COLUMN_K;
	}

	public void setR59_COLUMN_K(BigDecimal R59_COLUMN_K) {
		this.R59_COLUMN_K = R59_COLUMN_K;
	}

	public BigDecimal getR59_COLUMN_L() {
		return R59_COLUMN_L;
	}

	public void setR59_COLUMN_L(BigDecimal R59_COLUMN_L) {
		this.R59_COLUMN_L = R59_COLUMN_L;
	}

	public BigDecimal getR59_COLUMN_M() {
		return R59_COLUMN_M;
	}

	public void setR59_COLUMN_M(BigDecimal R59_COLUMN_M) {
		this.R59_COLUMN_M = R59_COLUMN_M;
	}

	public BigDecimal getR59_COLUMN_N() {
		return R59_COLUMN_N;
	}

	public void setR59_COLUMN_N(BigDecimal R59_COLUMN_N) {
		this.R59_COLUMN_N = R59_COLUMN_N;
	}

	public String getR60_COLUMN_A() {
		return R60_COLUMN_A;
	}

	public void setR60_COLUMN_A(String R60_COLUMN_A) {
		this.R60_COLUMN_A = R60_COLUMN_A;
	}

	public BigDecimal getR60_COLUMN_B() {
		return R60_COLUMN_B;
	}

	public void setR60_COLUMN_B(BigDecimal R60_COLUMN_B) {
		this.R60_COLUMN_B = R60_COLUMN_B;
	}

	public BigDecimal getR60_COLUMN_C() {
		return R60_COLUMN_C;
	}

	public void setR60_COLUMN_C(BigDecimal R60_COLUMN_C) {
		this.R60_COLUMN_C = R60_COLUMN_C;
	}

	public BigDecimal getR60_COLUMN_D() {
		return R60_COLUMN_D;
	}

	public void setR60_COLUMN_D(BigDecimal R60_COLUMN_D) {
		this.R60_COLUMN_D = R60_COLUMN_D;
	}

	public BigDecimal getR60_COLUMN_E() {
		return R60_COLUMN_E;
	}

	public void setR60_COLUMN_E(BigDecimal R60_COLUMN_E) {
		this.R60_COLUMN_E = R60_COLUMN_E;
	}

	public BigDecimal getR60_COLUMN_F() {
		return R60_COLUMN_F;
	}

	public void setR60_COLUMN_F(BigDecimal R60_COLUMN_F) {
		this.R60_COLUMN_F = R60_COLUMN_F;
	}

	public BigDecimal getR60_COLUMN_G() {
		return R60_COLUMN_G;
	}

	public void setR60_COLUMN_G(BigDecimal R60_COLUMN_G) {
		this.R60_COLUMN_G = R60_COLUMN_G;
	}

	public BigDecimal getR60_COLUMN_H() {
		return R60_COLUMN_H;
	}

	public void setR60_COLUMN_H(BigDecimal R60_COLUMN_H) {
		this.R60_COLUMN_H = R60_COLUMN_H;
	}

	public BigDecimal getR60_COLUMN_I() {
		return R60_COLUMN_I;
	}

	public void setR60_COLUMN_I(BigDecimal R60_COLUMN_I) {
		this.R60_COLUMN_I = R60_COLUMN_I;
	}

	public BigDecimal getR60_COLUMN_J() {
		return R60_COLUMN_J;
	}

	public void setR60_COLUMN_J(BigDecimal R60_COLUMN_J) {
		this.R60_COLUMN_J = R60_COLUMN_J;
	}

	public BigDecimal getR60_COLUMN_K() {
		return R60_COLUMN_K;
	}

	public void setR60_COLUMN_K(BigDecimal R60_COLUMN_K) {
		this.R60_COLUMN_K = R60_COLUMN_K;
	}

	public BigDecimal getR60_COLUMN_L() {
		return R60_COLUMN_L;
	}

	public void setR60_COLUMN_L(BigDecimal R60_COLUMN_L) {
		this.R60_COLUMN_L = R60_COLUMN_L;
	}

	public BigDecimal getR60_COLUMN_M() {
		return R60_COLUMN_M;
	}

	public void setR60_COLUMN_M(BigDecimal R60_COLUMN_M) {
		this.R60_COLUMN_M = R60_COLUMN_M;
	}

	public BigDecimal getR60_COLUMN_N() {
		return R60_COLUMN_N;
	}

	public void setR60_COLUMN_N(BigDecimal R60_COLUMN_N) {
		this.R60_COLUMN_N = R60_COLUMN_N;
	}

	public String getR61_COLUMN_A() {
		return R61_COLUMN_A;
	}

	public void setR61_COLUMN_A(String R61_COLUMN_A) {
		this.R61_COLUMN_A = R61_COLUMN_A;
	}

	public BigDecimal getR61_COLUMN_B() {
		return R61_COLUMN_B;
	}

	public void setR61_COLUMN_B(BigDecimal R61_COLUMN_B) {
		this.R61_COLUMN_B = R61_COLUMN_B;
	}

	public BigDecimal getR61_COLUMN_C() {
		return R61_COLUMN_C;
	}

	public void setR61_COLUMN_C(BigDecimal R61_COLUMN_C) {
		this.R61_COLUMN_C = R61_COLUMN_C;
	}

	public BigDecimal getR61_COLUMN_D() {
		return R61_COLUMN_D;
	}

	public void setR61_COLUMN_D(BigDecimal R61_COLUMN_D) {
		this.R61_COLUMN_D = R61_COLUMN_D;
	}

	public BigDecimal getR61_COLUMN_E() {
		return R61_COLUMN_E;
	}

	public void setR61_COLUMN_E(BigDecimal R61_COLUMN_E) {
		this.R61_COLUMN_E = R61_COLUMN_E;
	}

	public BigDecimal getR61_COLUMN_F() {
		return R61_COLUMN_F;
	}

	public void setR61_COLUMN_F(BigDecimal R61_COLUMN_F) {
		this.R61_COLUMN_F = R61_COLUMN_F;
	}

	public BigDecimal getR61_COLUMN_G() {
		return R61_COLUMN_G;
	}

	public void setR61_COLUMN_G(BigDecimal R61_COLUMN_G) {
		this.R61_COLUMN_G = R61_COLUMN_G;
	}

	public BigDecimal getR61_COLUMN_H() {
		return R61_COLUMN_H;
	}

	public void setR61_COLUMN_H(BigDecimal R61_COLUMN_H) {
		this.R61_COLUMN_H = R61_COLUMN_H;
	}

	public BigDecimal getR61_COLUMN_I() {
		return R61_COLUMN_I;
	}

	public void setR61_COLUMN_I(BigDecimal R61_COLUMN_I) {
		this.R61_COLUMN_I = R61_COLUMN_I;
	}

	public BigDecimal getR61_COLUMN_J() {
		return R61_COLUMN_J;
	}

	public void setR61_COLUMN_J(BigDecimal R61_COLUMN_J) {
		this.R61_COLUMN_J = R61_COLUMN_J;
	}

	public BigDecimal getR61_COLUMN_K() {
		return R61_COLUMN_K;
	}

	public void setR61_COLUMN_K(BigDecimal R61_COLUMN_K) {
		this.R61_COLUMN_K = R61_COLUMN_K;
	}

	public BigDecimal getR61_COLUMN_L() {
		return R61_COLUMN_L;
	}

	public void setR61_COLUMN_L(BigDecimal R61_COLUMN_L) {
		this.R61_COLUMN_L = R61_COLUMN_L;
	}

	public BigDecimal getR61_COLUMN_M() {
		return R61_COLUMN_M;
	}

	public void setR61_COLUMN_M(BigDecimal R61_COLUMN_M) {
		this.R61_COLUMN_M = R61_COLUMN_M;
	}

	public BigDecimal getR61_COLUMN_N() {
		return R61_COLUMN_N;
	}

	public void setR61_COLUMN_N(BigDecimal R61_COLUMN_N) {
		this.R61_COLUMN_N = R61_COLUMN_N;
	}

	public String getR62_COLUMN_A() {
		return R62_COLUMN_A;
	}

	public void setR62_COLUMN_A(String R62_COLUMN_A) {
		this.R62_COLUMN_A = R62_COLUMN_A;
	}

	public BigDecimal getR62_COLUMN_B() {
		return R62_COLUMN_B;
	}

	public void setR62_COLUMN_B(BigDecimal R62_COLUMN_B) {
		this.R62_COLUMN_B = R62_COLUMN_B;
	}

	public BigDecimal getR62_COLUMN_C() {
		return R62_COLUMN_C;
	}

	public void setR62_COLUMN_C(BigDecimal R62_COLUMN_C) {
		this.R62_COLUMN_C = R62_COLUMN_C;
	}

	public BigDecimal getR62_COLUMN_D() {
		return R62_COLUMN_D;
	}

	public void setR62_COLUMN_D(BigDecimal R62_COLUMN_D) {
		this.R62_COLUMN_D = R62_COLUMN_D;
	}

	public BigDecimal getR62_COLUMN_E() {
		return R62_COLUMN_E;
	}

	public void setR62_COLUMN_E(BigDecimal R62_COLUMN_E) {
		this.R62_COLUMN_E = R62_COLUMN_E;
	}

	public BigDecimal getR62_COLUMN_F() {
		return R62_COLUMN_F;
	}

	public void setR62_COLUMN_F(BigDecimal R62_COLUMN_F) {
		this.R62_COLUMN_F = R62_COLUMN_F;
	}

	public BigDecimal getR62_COLUMN_G() {
		return R62_COLUMN_G;
	}

	public void setR62_COLUMN_G(BigDecimal R62_COLUMN_G) {
		this.R62_COLUMN_G = R62_COLUMN_G;
	}

	public BigDecimal getR62_COLUMN_H() {
		return R62_COLUMN_H;
	}

	public void setR62_COLUMN_H(BigDecimal R62_COLUMN_H) {
		this.R62_COLUMN_H = R62_COLUMN_H;
	}

	public BigDecimal getR62_COLUMN_I() {
		return R62_COLUMN_I;
	}

	public void setR62_COLUMN_I(BigDecimal R62_COLUMN_I) {
		this.R62_COLUMN_I = R62_COLUMN_I;
	}

	public BigDecimal getR62_COLUMN_J() {
		return R62_COLUMN_J;
	}

	public void setR62_COLUMN_J(BigDecimal R62_COLUMN_J) {
		this.R62_COLUMN_J = R62_COLUMN_J;
	}

	public BigDecimal getR62_COLUMN_K() {
		return R62_COLUMN_K;
	}

	public void setR62_COLUMN_K(BigDecimal R62_COLUMN_K) {
		this.R62_COLUMN_K = R62_COLUMN_K;
	}

	public BigDecimal getR62_COLUMN_L() {
		return R62_COLUMN_L;
	}

	public void setR62_COLUMN_L(BigDecimal R62_COLUMN_L) {
		this.R62_COLUMN_L = R62_COLUMN_L;
	}

	public BigDecimal getR62_COLUMN_M() {
		return R62_COLUMN_M;
	}

	public void setR62_COLUMN_M(BigDecimal R62_COLUMN_M) {
		this.R62_COLUMN_M = R62_COLUMN_M;
	}

	public BigDecimal getR62_COLUMN_N() {
		return R62_COLUMN_N;
	}

	public void setR62_COLUMN_N(BigDecimal R62_COLUMN_N) {
		this.R62_COLUMN_N = R62_COLUMN_N;
	}

	public String getR63_COLUMN_A() {
		return R63_COLUMN_A;
	}

	public void setR63_COLUMN_A(String R63_COLUMN_A) {
		this.R63_COLUMN_A = R63_COLUMN_A;
	}

	public BigDecimal getR63_COLUMN_B() {
		return R63_COLUMN_B;
	}

	public void setR63_COLUMN_B(BigDecimal R63_COLUMN_B) {
		this.R63_COLUMN_B = R63_COLUMN_B;
	}

	public BigDecimal getR63_COLUMN_C() {
		return R63_COLUMN_C;
	}

	public void setR63_COLUMN_C(BigDecimal R63_COLUMN_C) {
		this.R63_COLUMN_C = R63_COLUMN_C;
	}

	public BigDecimal getR63_COLUMN_D() {
		return R63_COLUMN_D;
	}

	public void setR63_COLUMN_D(BigDecimal R63_COLUMN_D) {
		this.R63_COLUMN_D = R63_COLUMN_D;
	}

	public BigDecimal getR63_COLUMN_E() {
		return R63_COLUMN_E;
	}

	public void setR63_COLUMN_E(BigDecimal R63_COLUMN_E) {
		this.R63_COLUMN_E = R63_COLUMN_E;
	}

	public BigDecimal getR63_COLUMN_F() {
		return R63_COLUMN_F;
	}

	public void setR63_COLUMN_F(BigDecimal R63_COLUMN_F) {
		this.R63_COLUMN_F = R63_COLUMN_F;
	}

	public BigDecimal getR63_COLUMN_G() {
		return R63_COLUMN_G;
	}

	public void setR63_COLUMN_G(BigDecimal R63_COLUMN_G) {
		this.R63_COLUMN_G = R63_COLUMN_G;
	}

	public BigDecimal getR63_COLUMN_H() {
		return R63_COLUMN_H;
	}

	public void setR63_COLUMN_H(BigDecimal R63_COLUMN_H) {
		this.R63_COLUMN_H = R63_COLUMN_H;
	}

	public BigDecimal getR63_COLUMN_I() {
		return R63_COLUMN_I;
	}

	public void setR63_COLUMN_I(BigDecimal R63_COLUMN_I) {
		this.R63_COLUMN_I = R63_COLUMN_I;
	}

	public BigDecimal getR63_COLUMN_J() {
		return R63_COLUMN_J;
	}

	public void setR63_COLUMN_J(BigDecimal R63_COLUMN_J) {
		this.R63_COLUMN_J = R63_COLUMN_J;
	}

	public BigDecimal getR63_COLUMN_K() {
		return R63_COLUMN_K;
	}

	public void setR63_COLUMN_K(BigDecimal R63_COLUMN_K) {
		this.R63_COLUMN_K = R63_COLUMN_K;
	}

	public BigDecimal getR63_COLUMN_L() {
		return R63_COLUMN_L;
	}

	public void setR63_COLUMN_L(BigDecimal R63_COLUMN_L) {
		this.R63_COLUMN_L = R63_COLUMN_L;
	}

	public BigDecimal getR63_COLUMN_M() {
		return R63_COLUMN_M;
	}

	public void setR63_COLUMN_M(BigDecimal R63_COLUMN_M) {
		this.R63_COLUMN_M = R63_COLUMN_M;
	}

	public BigDecimal getR63_COLUMN_N() {
		return R63_COLUMN_N;
	}

	public void setR63_COLUMN_N(BigDecimal R63_COLUMN_N) {
		this.R63_COLUMN_N = R63_COLUMN_N;
	}

	public String getR64_COLUMN_A() {
		return R64_COLUMN_A;
	}

	public void setR64_COLUMN_A(String R64_COLUMN_A) {
		this.R64_COLUMN_A = R64_COLUMN_A;
	}

	public BigDecimal getR64_COLUMN_B() {
		return R64_COLUMN_B;
	}

	public void setR64_COLUMN_B(BigDecimal R64_COLUMN_B) {
		this.R64_COLUMN_B = R64_COLUMN_B;
	}

	public BigDecimal getR64_COLUMN_C() {
		return R64_COLUMN_C;
	}

	public void setR64_COLUMN_C(BigDecimal R64_COLUMN_C) {
		this.R64_COLUMN_C = R64_COLUMN_C;
	}

	public BigDecimal getR64_COLUMN_D() {
		return R64_COLUMN_D;
	}

	public void setR64_COLUMN_D(BigDecimal R64_COLUMN_D) {
		this.R64_COLUMN_D = R64_COLUMN_D;
	}

	public BigDecimal getR64_COLUMN_E() {
		return R64_COLUMN_E;
	}

	public void setR64_COLUMN_E(BigDecimal R64_COLUMN_E) {
		this.R64_COLUMN_E = R64_COLUMN_E;
	}

	public BigDecimal getR64_COLUMN_F() {
		return R64_COLUMN_F;
	}

	public void setR64_COLUMN_F(BigDecimal R64_COLUMN_F) {
		this.R64_COLUMN_F = R64_COLUMN_F;
	}

	public BigDecimal getR64_COLUMN_G() {
		return R64_COLUMN_G;
	}

	public void setR64_COLUMN_G(BigDecimal R64_COLUMN_G) {
		this.R64_COLUMN_G = R64_COLUMN_G;
	}

	public BigDecimal getR64_COLUMN_H() {
		return R64_COLUMN_H;
	}

	public void setR64_COLUMN_H(BigDecimal R64_COLUMN_H) {
		this.R64_COLUMN_H = R64_COLUMN_H;
	}

	public BigDecimal getR64_COLUMN_I() {
		return R64_COLUMN_I;
	}

	public void setR64_COLUMN_I(BigDecimal R64_COLUMN_I) {
		this.R64_COLUMN_I = R64_COLUMN_I;
	}

	public BigDecimal getR64_COLUMN_J() {
		return R64_COLUMN_J;
	}

	public void setR64_COLUMN_J(BigDecimal R64_COLUMN_J) {
		this.R64_COLUMN_J = R64_COLUMN_J;
	}

	public BigDecimal getR64_COLUMN_K() {
		return R64_COLUMN_K;
	}

	public void setR64_COLUMN_K(BigDecimal R64_COLUMN_K) {
		this.R64_COLUMN_K = R64_COLUMN_K;
	}

	public BigDecimal getR64_COLUMN_L() {
		return R64_COLUMN_L;
	}

	public void setR64_COLUMN_L(BigDecimal R64_COLUMN_L) {
		this.R64_COLUMN_L = R64_COLUMN_L;
	}

	public BigDecimal getR64_COLUMN_M() {
		return R64_COLUMN_M;
	}

	public void setR64_COLUMN_M(BigDecimal R64_COLUMN_M) {
		this.R64_COLUMN_M = R64_COLUMN_M;
	}

	public BigDecimal getR64_COLUMN_N() {
		return R64_COLUMN_N;
	}

	public void setR64_COLUMN_N(BigDecimal R64_COLUMN_N) {
		this.R64_COLUMN_N = R64_COLUMN_N;
	}

	public String getR65_COLUMN_A() {
		return R65_COLUMN_A;
	}

	public void setR65_COLUMN_A(String R65_COLUMN_A) {
		this.R65_COLUMN_A = R65_COLUMN_A;
	}

	public BigDecimal getR65_COLUMN_B() {
		return R65_COLUMN_B;
	}

	public void setR65_COLUMN_B(BigDecimal R65_COLUMN_B) {
		this.R65_COLUMN_B = R65_COLUMN_B;
	}

	public BigDecimal getR65_COLUMN_C() {
		return R65_COLUMN_C;
	}

	public void setR65_COLUMN_C(BigDecimal R65_COLUMN_C) {
		this.R65_COLUMN_C = R65_COLUMN_C;
	}

	public BigDecimal getR65_COLUMN_D() {
		return R65_COLUMN_D;
	}

	public void setR65_COLUMN_D(BigDecimal R65_COLUMN_D) {
		this.R65_COLUMN_D = R65_COLUMN_D;
	}

	public BigDecimal getR65_COLUMN_E() {
		return R65_COLUMN_E;
	}

	public void setR65_COLUMN_E(BigDecimal R65_COLUMN_E) {
		this.R65_COLUMN_E = R65_COLUMN_E;
	}

	public BigDecimal getR65_COLUMN_F() {
		return R65_COLUMN_F;
	}

	public void setR65_COLUMN_F(BigDecimal R65_COLUMN_F) {
		this.R65_COLUMN_F = R65_COLUMN_F;
	}

	public BigDecimal getR65_COLUMN_G() {
		return R65_COLUMN_G;
	}

	public void setR65_COLUMN_G(BigDecimal R65_COLUMN_G) {
		this.R65_COLUMN_G = R65_COLUMN_G;
	}

	public BigDecimal getR65_COLUMN_H() {
		return R65_COLUMN_H;
	}

	public void setR65_COLUMN_H(BigDecimal R65_COLUMN_H) {
		this.R65_COLUMN_H = R65_COLUMN_H;
	}

	public BigDecimal getR65_COLUMN_I() {
		return R65_COLUMN_I;
	}

	public void setR65_COLUMN_I(BigDecimal R65_COLUMN_I) {
		this.R65_COLUMN_I = R65_COLUMN_I;
	}

	public BigDecimal getR65_COLUMN_J() {
		return R65_COLUMN_J;
	}

	public void setR65_COLUMN_J(BigDecimal R65_COLUMN_J) {
		this.R65_COLUMN_J = R65_COLUMN_J;
	}

	public BigDecimal getR65_COLUMN_K() {
		return R65_COLUMN_K;
	}

	public void setR65_COLUMN_K(BigDecimal R65_COLUMN_K) {
		this.R65_COLUMN_K = R65_COLUMN_K;
	}

	public BigDecimal getR65_COLUMN_L() {
		return R65_COLUMN_L;
	}

	public void setR65_COLUMN_L(BigDecimal R65_COLUMN_L) {
		this.R65_COLUMN_L = R65_COLUMN_L;
	}

	public BigDecimal getR65_COLUMN_M() {
		return R65_COLUMN_M;
	}

	public void setR65_COLUMN_M(BigDecimal R65_COLUMN_M) {
		this.R65_COLUMN_M = R65_COLUMN_M;
	}

	public BigDecimal getR65_COLUMN_N() {
		return R65_COLUMN_N;
	}

	public void setR65_COLUMN_N(BigDecimal R65_COLUMN_N) {
		this.R65_COLUMN_N = R65_COLUMN_N;
	}

	public String getR66_COLUMN_A() {
		return R66_COLUMN_A;
	}

	public void setR66_COLUMN_A(String R66_COLUMN_A) {
		this.R66_COLUMN_A = R66_COLUMN_A;
	}

	public BigDecimal getR66_COLUMN_B() {
		return R66_COLUMN_B;
	}

	public void setR66_COLUMN_B(BigDecimal R66_COLUMN_B) {
		this.R66_COLUMN_B = R66_COLUMN_B;
	}

	public BigDecimal getR66_COLUMN_C() {
		return R66_COLUMN_C;
	}

	public void setR66_COLUMN_C(BigDecimal R66_COLUMN_C) {
		this.R66_COLUMN_C = R66_COLUMN_C;
	}

	public BigDecimal getR66_COLUMN_D() {
		return R66_COLUMN_D;
	}

	public void setR66_COLUMN_D(BigDecimal R66_COLUMN_D) {
		this.R66_COLUMN_D = R66_COLUMN_D;
	}

	public BigDecimal getR66_COLUMN_E() {
		return R66_COLUMN_E;
	}

	public void setR66_COLUMN_E(BigDecimal R66_COLUMN_E) {
		this.R66_COLUMN_E = R66_COLUMN_E;
	}

	public BigDecimal getR66_COLUMN_F() {
		return R66_COLUMN_F;
	}

	public void setR66_COLUMN_F(BigDecimal R66_COLUMN_F) {
		this.R66_COLUMN_F = R66_COLUMN_F;
	}

	public BigDecimal getR66_COLUMN_G() {
		return R66_COLUMN_G;
	}

	public void setR66_COLUMN_G(BigDecimal R66_COLUMN_G) {
		this.R66_COLUMN_G = R66_COLUMN_G;
	}

	public BigDecimal getR66_COLUMN_H() {
		return R66_COLUMN_H;
	}

	public void setR66_COLUMN_H(BigDecimal R66_COLUMN_H) {
		this.R66_COLUMN_H = R66_COLUMN_H;
	}

	public BigDecimal getR66_COLUMN_I() {
		return R66_COLUMN_I;
	}

	public void setR66_COLUMN_I(BigDecimal R66_COLUMN_I) {
		this.R66_COLUMN_I = R66_COLUMN_I;
	}

	public BigDecimal getR66_COLUMN_J() {
		return R66_COLUMN_J;
	}

	public void setR66_COLUMN_J(BigDecimal R66_COLUMN_J) {
		this.R66_COLUMN_J = R66_COLUMN_J;
	}

	public BigDecimal getR66_COLUMN_K() {
		return R66_COLUMN_K;
	}

	public void setR66_COLUMN_K(BigDecimal R66_COLUMN_K) {
		this.R66_COLUMN_K = R66_COLUMN_K;
	}

	public BigDecimal getR66_COLUMN_L() {
		return R66_COLUMN_L;
	}

	public void setR66_COLUMN_L(BigDecimal R66_COLUMN_L) {
		this.R66_COLUMN_L = R66_COLUMN_L;
	}

	public BigDecimal getR66_COLUMN_M() {
		return R66_COLUMN_M;
	}

	public void setR66_COLUMN_M(BigDecimal R66_COLUMN_M) {
		this.R66_COLUMN_M = R66_COLUMN_M;
	}

	public BigDecimal getR66_COLUMN_N() {
		return R66_COLUMN_N;
	}

	public void setR66_COLUMN_N(BigDecimal R66_COLUMN_N) {
		this.R66_COLUMN_N = R66_COLUMN_N;
	}

	public String getR67_COLUMN_A() {
		return R67_COLUMN_A;
	}

	public void setR67_COLUMN_A(String R67_COLUMN_A) {
		this.R67_COLUMN_A = R67_COLUMN_A;
	}

	public BigDecimal getR67_COLUMN_B() {
		return R67_COLUMN_B;
	}

	public void setR67_COLUMN_B(BigDecimal R67_COLUMN_B) {
		this.R67_COLUMN_B = R67_COLUMN_B;
	}

	public BigDecimal getR67_COLUMN_C() {
		return R67_COLUMN_C;
	}

	public void setR67_COLUMN_C(BigDecimal R67_COLUMN_C) {
		this.R67_COLUMN_C = R67_COLUMN_C;
	}

	public BigDecimal getR67_COLUMN_D() {
		return R67_COLUMN_D;
	}

	public void setR67_COLUMN_D(BigDecimal R67_COLUMN_D) {
		this.R67_COLUMN_D = R67_COLUMN_D;
	}

	public BigDecimal getR67_COLUMN_E() {
		return R67_COLUMN_E;
	}

	public void setR67_COLUMN_E(BigDecimal R67_COLUMN_E) {
		this.R67_COLUMN_E = R67_COLUMN_E;
	}

	public BigDecimal getR67_COLUMN_F() {
		return R67_COLUMN_F;
	}

	public void setR67_COLUMN_F(BigDecimal R67_COLUMN_F) {
		this.R67_COLUMN_F = R67_COLUMN_F;
	}

	public BigDecimal getR67_COLUMN_G() {
		return R67_COLUMN_G;
	}

	public void setR67_COLUMN_G(BigDecimal R67_COLUMN_G) {
		this.R67_COLUMN_G = R67_COLUMN_G;
	}

	public BigDecimal getR67_COLUMN_H() {
		return R67_COLUMN_H;
	}

	public void setR67_COLUMN_H(BigDecimal R67_COLUMN_H) {
		this.R67_COLUMN_H = R67_COLUMN_H;
	}

	public BigDecimal getR67_COLUMN_I() {
		return R67_COLUMN_I;
	}

	public void setR67_COLUMN_I(BigDecimal R67_COLUMN_I) {
		this.R67_COLUMN_I = R67_COLUMN_I;
	}

	public BigDecimal getR67_COLUMN_J() {
		return R67_COLUMN_J;
	}

	public void setR67_COLUMN_J(BigDecimal R67_COLUMN_J) {
		this.R67_COLUMN_J = R67_COLUMN_J;
	}

	public BigDecimal getR67_COLUMN_K() {
		return R67_COLUMN_K;
	}

	public void setR67_COLUMN_K(BigDecimal R67_COLUMN_K) {
		this.R67_COLUMN_K = R67_COLUMN_K;
	}

	public BigDecimal getR67_COLUMN_L() {
		return R67_COLUMN_L;
	}

	public void setR67_COLUMN_L(BigDecimal R67_COLUMN_L) {
		this.R67_COLUMN_L = R67_COLUMN_L;
	}

	public BigDecimal getR67_COLUMN_M() {
		return R67_COLUMN_M;
	}

	public void setR67_COLUMN_M(BigDecimal R67_COLUMN_M) {
		this.R67_COLUMN_M = R67_COLUMN_M;
	}

	public BigDecimal getR67_COLUMN_N() {
		return R67_COLUMN_N;
	}

	public void setR67_COLUMN_N(BigDecimal R67_COLUMN_N) {
		this.R67_COLUMN_N = R67_COLUMN_N;
	}

	public String getR68_COLUMN_A() {
		return R68_COLUMN_A;
	}

	public void setR68_COLUMN_A(String R68_COLUMN_A) {
		this.R68_COLUMN_A = R68_COLUMN_A;
	}

	public BigDecimal getR68_COLUMN_B() {
		return R68_COLUMN_B;
	}

	public void setR68_COLUMN_B(BigDecimal R68_COLUMN_B) {
		this.R68_COLUMN_B = R68_COLUMN_B;
	}

	public BigDecimal getR68_COLUMN_C() {
		return R68_COLUMN_C;
	}

	public void setR68_COLUMN_C(BigDecimal R68_COLUMN_C) {
		this.R68_COLUMN_C = R68_COLUMN_C;
	}

	public BigDecimal getR68_COLUMN_D() {
		return R68_COLUMN_D;
	}

	public void setR68_COLUMN_D(BigDecimal R68_COLUMN_D) {
		this.R68_COLUMN_D = R68_COLUMN_D;
	}

	public BigDecimal getR68_COLUMN_E() {
		return R68_COLUMN_E;
	}

	public void setR68_COLUMN_E(BigDecimal R68_COLUMN_E) {
		this.R68_COLUMN_E = R68_COLUMN_E;
	}

	public BigDecimal getR68_COLUMN_F() {
		return R68_COLUMN_F;
	}

	public void setR68_COLUMN_F(BigDecimal R68_COLUMN_F) {
		this.R68_COLUMN_F = R68_COLUMN_F;
	}

	public BigDecimal getR68_COLUMN_G() {
		return R68_COLUMN_G;
	}

	public void setR68_COLUMN_G(BigDecimal R68_COLUMN_G) {
		this.R68_COLUMN_G = R68_COLUMN_G;
	}

	public BigDecimal getR68_COLUMN_H() {
		return R68_COLUMN_H;
	}

	public void setR68_COLUMN_H(BigDecimal R68_COLUMN_H) {
		this.R68_COLUMN_H = R68_COLUMN_H;
	}

	public BigDecimal getR68_COLUMN_I() {
		return R68_COLUMN_I;
	}

	public void setR68_COLUMN_I(BigDecimal R68_COLUMN_I) {
		this.R68_COLUMN_I = R68_COLUMN_I;
	}

	public BigDecimal getR68_COLUMN_J() {
		return R68_COLUMN_J;
	}

	public void setR68_COLUMN_J(BigDecimal R68_COLUMN_J) {
		this.R68_COLUMN_J = R68_COLUMN_J;
	}

	public BigDecimal getR68_COLUMN_K() {
		return R68_COLUMN_K;
	}

	public void setR68_COLUMN_K(BigDecimal R68_COLUMN_K) {
		this.R68_COLUMN_K = R68_COLUMN_K;
	}

	public BigDecimal getR68_COLUMN_L() {
		return R68_COLUMN_L;
	}

	public void setR68_COLUMN_L(BigDecimal R68_COLUMN_L) {
		this.R68_COLUMN_L = R68_COLUMN_L;
	}

	public BigDecimal getR68_COLUMN_M() {
		return R68_COLUMN_M;
	}

	public void setR68_COLUMN_M(BigDecimal R68_COLUMN_M) {
		this.R68_COLUMN_M = R68_COLUMN_M;
	}

	public BigDecimal getR68_COLUMN_N() {
		return R68_COLUMN_N;
	}

	public void setR68_COLUMN_N(BigDecimal R68_COLUMN_N) {
		this.R68_COLUMN_N = R68_COLUMN_N;
	}

	public String getR69_COLUMN_A() {
		return R69_COLUMN_A;
	}

	public void setR69_COLUMN_A(String R69_COLUMN_A) {
		this.R69_COLUMN_A = R69_COLUMN_A;
	}

	public BigDecimal getR69_COLUMN_B() {
		return R69_COLUMN_B;
	}

	public void setR69_COLUMN_B(BigDecimal R69_COLUMN_B) {
		this.R69_COLUMN_B = R69_COLUMN_B;
	}

	public BigDecimal getR69_COLUMN_C() {
		return R69_COLUMN_C;
	}

	public void setR69_COLUMN_C(BigDecimal R69_COLUMN_C) {
		this.R69_COLUMN_C = R69_COLUMN_C;
	}

	public BigDecimal getR69_COLUMN_D() {
		return R69_COLUMN_D;
	}

	public void setR69_COLUMN_D(BigDecimal R69_COLUMN_D) {
		this.R69_COLUMN_D = R69_COLUMN_D;
	}

	public BigDecimal getR69_COLUMN_E() {
		return R69_COLUMN_E;
	}

	public void setR69_COLUMN_E(BigDecimal R69_COLUMN_E) {
		this.R69_COLUMN_E = R69_COLUMN_E;
	}

	public BigDecimal getR69_COLUMN_F() {
		return R69_COLUMN_F;
	}

	public void setR69_COLUMN_F(BigDecimal R69_COLUMN_F) {
		this.R69_COLUMN_F = R69_COLUMN_F;
	}

	public BigDecimal getR69_COLUMN_G() {
		return R69_COLUMN_G;
	}

	public void setR69_COLUMN_G(BigDecimal R69_COLUMN_G) {
		this.R69_COLUMN_G = R69_COLUMN_G;
	}

	public BigDecimal getR69_COLUMN_H() {
		return R69_COLUMN_H;
	}

	public void setR69_COLUMN_H(BigDecimal R69_COLUMN_H) {
		this.R69_COLUMN_H = R69_COLUMN_H;
	}

	public BigDecimal getR69_COLUMN_I() {
		return R69_COLUMN_I;
	}

	public void setR69_COLUMN_I(BigDecimal R69_COLUMN_I) {
		this.R69_COLUMN_I = R69_COLUMN_I;
	}

	public BigDecimal getR69_COLUMN_J() {
		return R69_COLUMN_J;
	}

	public void setR69_COLUMN_J(BigDecimal R69_COLUMN_J) {
		this.R69_COLUMN_J = R69_COLUMN_J;
	}

	public BigDecimal getR69_COLUMN_K() {
		return R69_COLUMN_K;
	}

	public void setR69_COLUMN_K(BigDecimal R69_COLUMN_K) {
		this.R69_COLUMN_K = R69_COLUMN_K;
	}

	public BigDecimal getR69_COLUMN_L() {
		return R69_COLUMN_L;
	}

	public void setR69_COLUMN_L(BigDecimal R69_COLUMN_L) {
		this.R69_COLUMN_L = R69_COLUMN_L;
	}

	public BigDecimal getR69_COLUMN_M() {
		return R69_COLUMN_M;
	}

	public void setR69_COLUMN_M(BigDecimal R69_COLUMN_M) {
		this.R69_COLUMN_M = R69_COLUMN_M;
	}

	public BigDecimal getR69_COLUMN_N() {
		return R69_COLUMN_N;
	}

	public void setR69_COLUMN_N(BigDecimal R69_COLUMN_N) {
		this.R69_COLUMN_N = R69_COLUMN_N;
	}

	public String getR70_COLUMN_A() {
		return R70_COLUMN_A;
	}

	public void setR70_COLUMN_A(String R70_COLUMN_A) {
		this.R70_COLUMN_A = R70_COLUMN_A;
	}

	public BigDecimal getR70_COLUMN_B() {
		return R70_COLUMN_B;
	}

	public void setR70_COLUMN_B(BigDecimal R70_COLUMN_B) {
		this.R70_COLUMN_B = R70_COLUMN_B;
	}

	public BigDecimal getR70_COLUMN_C() {
		return R70_COLUMN_C;
	}

	public void setR70_COLUMN_C(BigDecimal R70_COLUMN_C) {
		this.R70_COLUMN_C = R70_COLUMN_C;
	}

	public BigDecimal getR70_COLUMN_D() {
		return R70_COLUMN_D;
	}

	public void setR70_COLUMN_D(BigDecimal R70_COLUMN_D) {
		this.R70_COLUMN_D = R70_COLUMN_D;
	}

	public BigDecimal getR70_COLUMN_E() {
		return R70_COLUMN_E;
	}

	public void setR70_COLUMN_E(BigDecimal R70_COLUMN_E) {
		this.R70_COLUMN_E = R70_COLUMN_E;
	}

	public BigDecimal getR70_COLUMN_F() {
		return R70_COLUMN_F;
	}

	public void setR70_COLUMN_F(BigDecimal R70_COLUMN_F) {
		this.R70_COLUMN_F = R70_COLUMN_F;
	}

	public BigDecimal getR70_COLUMN_G() {
		return R70_COLUMN_G;
	}

	public void setR70_COLUMN_G(BigDecimal R70_COLUMN_G) {
		this.R70_COLUMN_G = R70_COLUMN_G;
	}

	public BigDecimal getR70_COLUMN_H() {
		return R70_COLUMN_H;
	}

	public void setR70_COLUMN_H(BigDecimal R70_COLUMN_H) {
		this.R70_COLUMN_H = R70_COLUMN_H;
	}

	public BigDecimal getR70_COLUMN_I() {
		return R70_COLUMN_I;
	}

	public void setR70_COLUMN_I(BigDecimal R70_COLUMN_I) {
		this.R70_COLUMN_I = R70_COLUMN_I;
	}

	public BigDecimal getR70_COLUMN_J() {
		return R70_COLUMN_J;
	}

	public void setR70_COLUMN_J(BigDecimal R70_COLUMN_J) {
		this.R70_COLUMN_J = R70_COLUMN_J;
	}

	public BigDecimal getR70_COLUMN_K() {
		return R70_COLUMN_K;
	}

	public void setR70_COLUMN_K(BigDecimal R70_COLUMN_K) {
		this.R70_COLUMN_K = R70_COLUMN_K;
	}

	public BigDecimal getR70_COLUMN_L() {
		return R70_COLUMN_L;
	}

	public void setR70_COLUMN_L(BigDecimal R70_COLUMN_L) {
		this.R70_COLUMN_L = R70_COLUMN_L;
	}

	public BigDecimal getR70_COLUMN_M() {
		return R70_COLUMN_M;
	}

	public void setR70_COLUMN_M(BigDecimal R70_COLUMN_M) {
		this.R70_COLUMN_M = R70_COLUMN_M;
	}

	public BigDecimal getR70_COLUMN_N() {
		return R70_COLUMN_N;
	}

	public void setR70_COLUMN_N(BigDecimal R70_COLUMN_N) {
		this.R70_COLUMN_N = R70_COLUMN_N;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public BigDecimal getREPORT_VERSION() {
		return REPORT_VERSION;
	}

	public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
		this.REPORT_VERSION = REPORT_VERSION;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
		this.REPORT_FREQUENCY = REPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String REPORT_CODE) {
		this.REPORT_CODE = REPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String REPORT_DESC) {
		this.REPORT_DESC = REPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public SLS_WORKING_Archival_Summary_Entity1() {
		super();
	}
	}

	public static class SLS_WORKING_Archival_Summary_Entity2 {

	private String R71_COLUMN_A;
	private BigDecimal R71_COLUMN_B;
	private BigDecimal R71_COLUMN_C;
	private BigDecimal R71_COLUMN_D;
	private BigDecimal R71_COLUMN_E;
	private BigDecimal R71_COLUMN_F;
	private BigDecimal R71_COLUMN_G;
	private BigDecimal R71_COLUMN_H;
	private BigDecimal R71_COLUMN_I;
	private BigDecimal R71_COLUMN_J;
	private BigDecimal R71_COLUMN_K;
	private BigDecimal R71_COLUMN_L;
	private BigDecimal R71_COLUMN_M;
	private BigDecimal R71_COLUMN_N;
	private String R72_COLUMN_A;
	private BigDecimal R72_COLUMN_B;
	private BigDecimal R72_COLUMN_C;
	private BigDecimal R72_COLUMN_D;
	private BigDecimal R72_COLUMN_E;
	private BigDecimal R72_COLUMN_F;
	private BigDecimal R72_COLUMN_G;
	private BigDecimal R72_COLUMN_H;
	private BigDecimal R72_COLUMN_I;
	private BigDecimal R72_COLUMN_J;
	private BigDecimal R72_COLUMN_K;
	private BigDecimal R72_COLUMN_L;
	private BigDecimal R72_COLUMN_M;
	private BigDecimal R72_COLUMN_N;
	private String R73_COLUMN_A;
	private BigDecimal R73_COLUMN_B;
	private BigDecimal R73_COLUMN_C;
	private BigDecimal R73_COLUMN_D;
	private BigDecimal R73_COLUMN_E;
	private BigDecimal R73_COLUMN_F;
	private BigDecimal R73_COLUMN_G;
	private BigDecimal R73_COLUMN_H;
	private BigDecimal R73_COLUMN_I;
	private BigDecimal R73_COLUMN_J;
	private BigDecimal R73_COLUMN_K;
	private BigDecimal R73_COLUMN_L;
	private BigDecimal R73_COLUMN_M;
	private BigDecimal R73_COLUMN_N;
	private String R74_COLUMN_A;
	private BigDecimal R74_COLUMN_B;
	private BigDecimal R74_COLUMN_C;
	private BigDecimal R74_COLUMN_D;
	private BigDecimal R74_COLUMN_E;
	private BigDecimal R74_COLUMN_F;
	private BigDecimal R74_COLUMN_G;
	private BigDecimal R74_COLUMN_H;
	private BigDecimal R74_COLUMN_I;
	private BigDecimal R74_COLUMN_J;
	private BigDecimal R74_COLUMN_K;
	private BigDecimal R74_COLUMN_L;
	private BigDecimal R74_COLUMN_M;
	private BigDecimal R74_COLUMN_N;
	private String R75_COLUMN_A;
	private BigDecimal R75_COLUMN_B;
	private BigDecimal R75_COLUMN_C;
	private BigDecimal R75_COLUMN_D;
	private BigDecimal R75_COLUMN_E;
	private BigDecimal R75_COLUMN_F;
	private BigDecimal R75_COLUMN_G;
	private BigDecimal R75_COLUMN_H;
	private BigDecimal R75_COLUMN_I;
	private BigDecimal R75_COLUMN_J;
	private BigDecimal R75_COLUMN_K;
	private BigDecimal R75_COLUMN_L;
	private BigDecimal R75_COLUMN_M;
	private BigDecimal R75_COLUMN_N;
	private String R76_COLUMN_A;
	private BigDecimal R76_COLUMN_B;
	private BigDecimal R76_COLUMN_C;
	private BigDecimal R76_COLUMN_D;
	private BigDecimal R76_COLUMN_E;
	private BigDecimal R76_COLUMN_F;
	private BigDecimal R76_COLUMN_G;
	private BigDecimal R76_COLUMN_H;
	private BigDecimal R76_COLUMN_I;
	private BigDecimal R76_COLUMN_J;
	private BigDecimal R76_COLUMN_K;
	private BigDecimal R76_COLUMN_L;
	private BigDecimal R76_COLUMN_M;
	private BigDecimal R76_COLUMN_N;
	private String R77_COLUMN_A;
	private BigDecimal R77_COLUMN_B;
	private BigDecimal R77_COLUMN_C;
	private BigDecimal R77_COLUMN_D;
	private BigDecimal R77_COLUMN_E;
	private BigDecimal R77_COLUMN_F;
	private BigDecimal R77_COLUMN_G;
	private BigDecimal R77_COLUMN_H;
	private BigDecimal R77_COLUMN_I;
	private BigDecimal R77_COLUMN_J;
	private BigDecimal R77_COLUMN_K;
	private BigDecimal R77_COLUMN_L;
	private BigDecimal R77_COLUMN_M;
	private BigDecimal R77_COLUMN_N;
	private String R78_COLUMN_A;
	private BigDecimal R78_COLUMN_B;
	private BigDecimal R78_COLUMN_C;
	private BigDecimal R78_COLUMN_D;
	private BigDecimal R78_COLUMN_E;
	private BigDecimal R78_COLUMN_F;
	private BigDecimal R78_COLUMN_G;
	private BigDecimal R78_COLUMN_H;
	private BigDecimal R78_COLUMN_I;
	private BigDecimal R78_COLUMN_J;
	private BigDecimal R78_COLUMN_K;
	private BigDecimal R78_COLUMN_L;
	private BigDecimal R78_COLUMN_M;
	private BigDecimal R78_COLUMN_N;
	private String R79_COLUMN_A;
	private BigDecimal R79_COLUMN_B;
	private BigDecimal R79_COLUMN_C;
	private BigDecimal R79_COLUMN_D;
	private BigDecimal R79_COLUMN_E;
	private BigDecimal R79_COLUMN_F;
	private BigDecimal R79_COLUMN_G;
	private BigDecimal R79_COLUMN_H;
	private BigDecimal R79_COLUMN_I;
	private BigDecimal R79_COLUMN_J;
	private BigDecimal R79_COLUMN_K;
	private BigDecimal R79_COLUMN_L;
	private BigDecimal R79_COLUMN_M;
	private BigDecimal R79_COLUMN_N;
	private String R80_COLUMN_A;
	private BigDecimal R80_COLUMN_B;
	private BigDecimal R80_COLUMN_C;
	private BigDecimal R80_COLUMN_D;
	private BigDecimal R80_COLUMN_E;
	private BigDecimal R80_COLUMN_F;
	private BigDecimal R80_COLUMN_G;
	private BigDecimal R80_COLUMN_H;
	private BigDecimal R80_COLUMN_I;
	private BigDecimal R80_COLUMN_J;
	private BigDecimal R80_COLUMN_K;
	private BigDecimal R80_COLUMN_L;
	private BigDecimal R80_COLUMN_M;
	private BigDecimal R80_COLUMN_N;
	private String R81_COLUMN_A;
	private BigDecimal R81_COLUMN_B;
	private BigDecimal R81_COLUMN_C;
	private BigDecimal R81_COLUMN_D;
	private BigDecimal R81_COLUMN_E;
	private BigDecimal R81_COLUMN_F;
	private BigDecimal R81_COLUMN_G;
	private BigDecimal R81_COLUMN_H;
	private BigDecimal R81_COLUMN_I;
	private BigDecimal R81_COLUMN_J;
	private BigDecimal R81_COLUMN_K;
	private BigDecimal R81_COLUMN_L;
	private BigDecimal R81_COLUMN_M;
	private BigDecimal R81_COLUMN_N;
	private String R82_COLUMN_A;
	private BigDecimal R82_COLUMN_B;
	private BigDecimal R82_COLUMN_C;
	private BigDecimal R82_COLUMN_D;
	private BigDecimal R82_COLUMN_E;
	private BigDecimal R82_COLUMN_F;
	private BigDecimal R82_COLUMN_G;
	private BigDecimal R82_COLUMN_H;
	private BigDecimal R82_COLUMN_I;
	private BigDecimal R82_COLUMN_J;
	private BigDecimal R82_COLUMN_K;
	private BigDecimal R82_COLUMN_L;
	private BigDecimal R82_COLUMN_M;
	private BigDecimal R82_COLUMN_N;
	private String R83_COLUMN_A;
	private BigDecimal R83_COLUMN_B;
	private BigDecimal R83_COLUMN_C;
	private BigDecimal R83_COLUMN_D;
	private BigDecimal R83_COLUMN_E;
	private BigDecimal R83_COLUMN_F;
	private BigDecimal R83_COLUMN_G;
	private BigDecimal R83_COLUMN_H;
	private BigDecimal R83_COLUMN_I;
	private BigDecimal R83_COLUMN_J;
	private BigDecimal R83_COLUMN_K;
	private BigDecimal R83_COLUMN_L;
	private BigDecimal R83_COLUMN_M;
	private BigDecimal R83_COLUMN_N;
	private String R84_COLUMN_A;
	private BigDecimal R84_COLUMN_B;
	private BigDecimal R84_COLUMN_C;
	private BigDecimal R84_COLUMN_D;
	private BigDecimal R84_COLUMN_E;
	private BigDecimal R84_COLUMN_F;
	private BigDecimal R84_COLUMN_G;
	private BigDecimal R84_COLUMN_H;
	private BigDecimal R84_COLUMN_I;
	private BigDecimal R84_COLUMN_J;
	private BigDecimal R84_COLUMN_K;
	private BigDecimal R84_COLUMN_L;
	private BigDecimal R84_COLUMN_M;
	private BigDecimal R84_COLUMN_N;
	private String R85_COLUMN_A;
	private BigDecimal R85_COLUMN_B;
	private BigDecimal R85_COLUMN_C;
	private BigDecimal R85_COLUMN_D;
	private BigDecimal R85_COLUMN_E;
	private BigDecimal R85_COLUMN_F;
	private BigDecimal R85_COLUMN_G;
	private BigDecimal R85_COLUMN_H;
	private BigDecimal R85_COLUMN_I;
	private BigDecimal R85_COLUMN_J;
	private BigDecimal R85_COLUMN_K;
	private BigDecimal R85_COLUMN_L;
	private BigDecimal R85_COLUMN_M;
	private BigDecimal R85_COLUMN_N;
	private String R86_COLUMN_A;
	private BigDecimal R86_COLUMN_B;
	private BigDecimal R86_COLUMN_C;
	private BigDecimal R86_COLUMN_D;
	private BigDecimal R86_COLUMN_E;
	private BigDecimal R86_COLUMN_F;
	private BigDecimal R86_COLUMN_G;
	private BigDecimal R86_COLUMN_H;
	private BigDecimal R86_COLUMN_I;
	private BigDecimal R86_COLUMN_J;
	private BigDecimal R86_COLUMN_K;
	private BigDecimal R86_COLUMN_L;
	private BigDecimal R86_COLUMN_M;
	private BigDecimal R86_COLUMN_N;
	private String R87_COLUMN_A;
	private BigDecimal R87_COLUMN_B;
	private BigDecimal R87_COLUMN_C;
	private BigDecimal R87_COLUMN_D;
	private BigDecimal R87_COLUMN_E;
	private BigDecimal R87_COLUMN_F;
	private BigDecimal R87_COLUMN_G;
	private BigDecimal R87_COLUMN_H;
	private BigDecimal R87_COLUMN_I;
	private BigDecimal R87_COLUMN_J;
	private BigDecimal R87_COLUMN_K;
	private BigDecimal R87_COLUMN_L;
	private BigDecimal R87_COLUMN_M;
	private BigDecimal R87_COLUMN_N;
	private String R88_COLUMN_A;
	private BigDecimal R88_COLUMN_B;
	private BigDecimal R88_COLUMN_C;
	private BigDecimal R88_COLUMN_D;
	private BigDecimal R88_COLUMN_E;
	private BigDecimal R88_COLUMN_F;
	private BigDecimal R88_COLUMN_G;
	private BigDecimal R88_COLUMN_H;
	private BigDecimal R88_COLUMN_I;
	private BigDecimal R88_COLUMN_J;
	private BigDecimal R88_COLUMN_K;
	private BigDecimal R88_COLUMN_L;
	private BigDecimal R88_COLUMN_M;
	private BigDecimal R88_COLUMN_N;
	private String R89_COLUMN_A;
	private BigDecimal R89_COLUMN_B;
	private BigDecimal R89_COLUMN_C;
	private BigDecimal R89_COLUMN_D;
	private BigDecimal R89_COLUMN_E;
	private BigDecimal R89_COLUMN_F;
	private BigDecimal R89_COLUMN_G;
	private BigDecimal R89_COLUMN_H;
	private BigDecimal R89_COLUMN_I;
	private BigDecimal R89_COLUMN_J;
	private BigDecimal R89_COLUMN_K;
	private BigDecimal R89_COLUMN_L;
	private BigDecimal R89_COLUMN_M;
	private BigDecimal R89_COLUMN_N;
	private String R90_COLUMN_A;
	private BigDecimal R90_COLUMN_B;
	private BigDecimal R90_COLUMN_C;
	private BigDecimal R90_COLUMN_D;
	private BigDecimal R90_COLUMN_E;
	private BigDecimal R90_COLUMN_F;
	private BigDecimal R90_COLUMN_G;
	private BigDecimal R90_COLUMN_H;
	private BigDecimal R90_COLUMN_I;
	private BigDecimal R90_COLUMN_J;
	private BigDecimal R90_COLUMN_K;
	private BigDecimal R90_COLUMN_L;
	private BigDecimal R90_COLUMN_M;
	private BigDecimal R90_COLUMN_N;
	private String R91_COLUMN_A;
	private BigDecimal R91_COLUMN_B;
	private BigDecimal R91_COLUMN_C;
	private BigDecimal R91_COLUMN_D;
	private BigDecimal R91_COLUMN_E;
	private BigDecimal R91_COLUMN_F;
	private BigDecimal R91_COLUMN_G;
	private BigDecimal R91_COLUMN_H;
	private BigDecimal R91_COLUMN_I;
	private BigDecimal R91_COLUMN_J;
	private BigDecimal R91_COLUMN_K;
	private BigDecimal R91_COLUMN_L;
	private BigDecimal R91_COLUMN_M;
	private BigDecimal R91_COLUMN_N;
	private String R92_COLUMN_A;
	private BigDecimal R92_COLUMN_B;
	private BigDecimal R92_COLUMN_C;
	private BigDecimal R92_COLUMN_D;
	private BigDecimal R92_COLUMN_E;
	private BigDecimal R92_COLUMN_F;
	private BigDecimal R92_COLUMN_G;
	private BigDecimal R92_COLUMN_H;
	private BigDecimal R92_COLUMN_I;
	private BigDecimal R92_COLUMN_J;
	private BigDecimal R92_COLUMN_K;
	private BigDecimal R92_COLUMN_L;
	private BigDecimal R92_COLUMN_M;
	private BigDecimal R92_COLUMN_N;
	private Date REPORT_DATE;
	private BigDecimal REPORT_VERSION;
	private String REPORT_FREQUENCY;
	private String REPORT_CODE;
	private String REPORT_DESC;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;

	public String getR71_COLUMN_A() {
		return R71_COLUMN_A;
	}

	public void setR71_COLUMN_A(String R71_COLUMN_A) {
		this.R71_COLUMN_A = R71_COLUMN_A;
	}

	public BigDecimal getR71_COLUMN_B() {
		return R71_COLUMN_B;
	}

	public void setR71_COLUMN_B(BigDecimal R71_COLUMN_B) {
		this.R71_COLUMN_B = R71_COLUMN_B;
	}

	public BigDecimal getR71_COLUMN_C() {
		return R71_COLUMN_C;
	}

	public void setR71_COLUMN_C(BigDecimal R71_COLUMN_C) {
		this.R71_COLUMN_C = R71_COLUMN_C;
	}

	public BigDecimal getR71_COLUMN_D() {
		return R71_COLUMN_D;
	}

	public void setR71_COLUMN_D(BigDecimal R71_COLUMN_D) {
		this.R71_COLUMN_D = R71_COLUMN_D;
	}

	public BigDecimal getR71_COLUMN_E() {
		return R71_COLUMN_E;
	}

	public void setR71_COLUMN_E(BigDecimal R71_COLUMN_E) {
		this.R71_COLUMN_E = R71_COLUMN_E;
	}

	public BigDecimal getR71_COLUMN_F() {
		return R71_COLUMN_F;
	}

	public void setR71_COLUMN_F(BigDecimal R71_COLUMN_F) {
		this.R71_COLUMN_F = R71_COLUMN_F;
	}

	public BigDecimal getR71_COLUMN_G() {
		return R71_COLUMN_G;
	}

	public void setR71_COLUMN_G(BigDecimal R71_COLUMN_G) {
		this.R71_COLUMN_G = R71_COLUMN_G;
	}

	public BigDecimal getR71_COLUMN_H() {
		return R71_COLUMN_H;
	}

	public void setR71_COLUMN_H(BigDecimal R71_COLUMN_H) {
		this.R71_COLUMN_H = R71_COLUMN_H;
	}

	public BigDecimal getR71_COLUMN_I() {
		return R71_COLUMN_I;
	}

	public void setR71_COLUMN_I(BigDecimal R71_COLUMN_I) {
		this.R71_COLUMN_I = R71_COLUMN_I;
	}

	public BigDecimal getR71_COLUMN_J() {
		return R71_COLUMN_J;
	}

	public void setR71_COLUMN_J(BigDecimal R71_COLUMN_J) {
		this.R71_COLUMN_J = R71_COLUMN_J;
	}

	public BigDecimal getR71_COLUMN_K() {
		return R71_COLUMN_K;
	}

	public void setR71_COLUMN_K(BigDecimal R71_COLUMN_K) {
		this.R71_COLUMN_K = R71_COLUMN_K;
	}

	public BigDecimal getR71_COLUMN_L() {
		return R71_COLUMN_L;
	}

	public void setR71_COLUMN_L(BigDecimal R71_COLUMN_L) {
		this.R71_COLUMN_L = R71_COLUMN_L;
	}

	public BigDecimal getR71_COLUMN_M() {
		return R71_COLUMN_M;
	}

	public void setR71_COLUMN_M(BigDecimal R71_COLUMN_M) {
		this.R71_COLUMN_M = R71_COLUMN_M;
	}

	public BigDecimal getR71_COLUMN_N() {
		return R71_COLUMN_N;
	}

	public void setR71_COLUMN_N(BigDecimal R71_COLUMN_N) {
		this.R71_COLUMN_N = R71_COLUMN_N;
	}

	public String getR72_COLUMN_A() {
		return R72_COLUMN_A;
	}

	public void setR72_COLUMN_A(String R72_COLUMN_A) {
		this.R72_COLUMN_A = R72_COLUMN_A;
	}

	public BigDecimal getR72_COLUMN_B() {
		return R72_COLUMN_B;
	}

	public void setR72_COLUMN_B(BigDecimal R72_COLUMN_B) {
		this.R72_COLUMN_B = R72_COLUMN_B;
	}

	public BigDecimal getR72_COLUMN_C() {
		return R72_COLUMN_C;
	}

	public void setR72_COLUMN_C(BigDecimal R72_COLUMN_C) {
		this.R72_COLUMN_C = R72_COLUMN_C;
	}

	public BigDecimal getR72_COLUMN_D() {
		return R72_COLUMN_D;
	}

	public void setR72_COLUMN_D(BigDecimal R72_COLUMN_D) {
		this.R72_COLUMN_D = R72_COLUMN_D;
	}

	public BigDecimal getR72_COLUMN_E() {
		return R72_COLUMN_E;
	}

	public void setR72_COLUMN_E(BigDecimal R72_COLUMN_E) {
		this.R72_COLUMN_E = R72_COLUMN_E;
	}

	public BigDecimal getR72_COLUMN_F() {
		return R72_COLUMN_F;
	}

	public void setR72_COLUMN_F(BigDecimal R72_COLUMN_F) {
		this.R72_COLUMN_F = R72_COLUMN_F;
	}

	public BigDecimal getR72_COLUMN_G() {
		return R72_COLUMN_G;
	}

	public void setR72_COLUMN_G(BigDecimal R72_COLUMN_G) {
		this.R72_COLUMN_G = R72_COLUMN_G;
	}

	public BigDecimal getR72_COLUMN_H() {
		return R72_COLUMN_H;
	}

	public void setR72_COLUMN_H(BigDecimal R72_COLUMN_H) {
		this.R72_COLUMN_H = R72_COLUMN_H;
	}

	public BigDecimal getR72_COLUMN_I() {
		return R72_COLUMN_I;
	}

	public void setR72_COLUMN_I(BigDecimal R72_COLUMN_I) {
		this.R72_COLUMN_I = R72_COLUMN_I;
	}

	public BigDecimal getR72_COLUMN_J() {
		return R72_COLUMN_J;
	}

	public void setR72_COLUMN_J(BigDecimal R72_COLUMN_J) {
		this.R72_COLUMN_J = R72_COLUMN_J;
	}

	public BigDecimal getR72_COLUMN_K() {
		return R72_COLUMN_K;
	}

	public void setR72_COLUMN_K(BigDecimal R72_COLUMN_K) {
		this.R72_COLUMN_K = R72_COLUMN_K;
	}

	public BigDecimal getR72_COLUMN_L() {
		return R72_COLUMN_L;
	}

	public void setR72_COLUMN_L(BigDecimal R72_COLUMN_L) {
		this.R72_COLUMN_L = R72_COLUMN_L;
	}

	public BigDecimal getR72_COLUMN_M() {
		return R72_COLUMN_M;
	}

	public void setR72_COLUMN_M(BigDecimal R72_COLUMN_M) {
		this.R72_COLUMN_M = R72_COLUMN_M;
	}

	public BigDecimal getR72_COLUMN_N() {
		return R72_COLUMN_N;
	}

	public void setR72_COLUMN_N(BigDecimal R72_COLUMN_N) {
		this.R72_COLUMN_N = R72_COLUMN_N;
	}

	public String getR73_COLUMN_A() {
		return R73_COLUMN_A;
	}

	public void setR73_COLUMN_A(String R73_COLUMN_A) {
		this.R73_COLUMN_A = R73_COLUMN_A;
	}

	public BigDecimal getR73_COLUMN_B() {
		return R73_COLUMN_B;
	}

	public void setR73_COLUMN_B(BigDecimal R73_COLUMN_B) {
		this.R73_COLUMN_B = R73_COLUMN_B;
	}

	public BigDecimal getR73_COLUMN_C() {
		return R73_COLUMN_C;
	}

	public void setR73_COLUMN_C(BigDecimal R73_COLUMN_C) {
		this.R73_COLUMN_C = R73_COLUMN_C;
	}

	public BigDecimal getR73_COLUMN_D() {
		return R73_COLUMN_D;
	}

	public void setR73_COLUMN_D(BigDecimal R73_COLUMN_D) {
		this.R73_COLUMN_D = R73_COLUMN_D;
	}

	public BigDecimal getR73_COLUMN_E() {
		return R73_COLUMN_E;
	}

	public void setR73_COLUMN_E(BigDecimal R73_COLUMN_E) {
		this.R73_COLUMN_E = R73_COLUMN_E;
	}

	public BigDecimal getR73_COLUMN_F() {
		return R73_COLUMN_F;
	}

	public void setR73_COLUMN_F(BigDecimal R73_COLUMN_F) {
		this.R73_COLUMN_F = R73_COLUMN_F;
	}

	public BigDecimal getR73_COLUMN_G() {
		return R73_COLUMN_G;
	}

	public void setR73_COLUMN_G(BigDecimal R73_COLUMN_G) {
		this.R73_COLUMN_G = R73_COLUMN_G;
	}

	public BigDecimal getR73_COLUMN_H() {
		return R73_COLUMN_H;
	}

	public void setR73_COLUMN_H(BigDecimal R73_COLUMN_H) {
		this.R73_COLUMN_H = R73_COLUMN_H;
	}

	public BigDecimal getR73_COLUMN_I() {
		return R73_COLUMN_I;
	}

	public void setR73_COLUMN_I(BigDecimal R73_COLUMN_I) {
		this.R73_COLUMN_I = R73_COLUMN_I;
	}

	public BigDecimal getR73_COLUMN_J() {
		return R73_COLUMN_J;
	}

	public void setR73_COLUMN_J(BigDecimal R73_COLUMN_J) {
		this.R73_COLUMN_J = R73_COLUMN_J;
	}

	public BigDecimal getR73_COLUMN_K() {
		return R73_COLUMN_K;
	}

	public void setR73_COLUMN_K(BigDecimal R73_COLUMN_K) {
		this.R73_COLUMN_K = R73_COLUMN_K;
	}

	public BigDecimal getR73_COLUMN_L() {
		return R73_COLUMN_L;
	}

	public void setR73_COLUMN_L(BigDecimal R73_COLUMN_L) {
		this.R73_COLUMN_L = R73_COLUMN_L;
	}

	public BigDecimal getR73_COLUMN_M() {
		return R73_COLUMN_M;
	}

	public void setR73_COLUMN_M(BigDecimal R73_COLUMN_M) {
		this.R73_COLUMN_M = R73_COLUMN_M;
	}

	public BigDecimal getR73_COLUMN_N() {
		return R73_COLUMN_N;
	}

	public void setR73_COLUMN_N(BigDecimal R73_COLUMN_N) {
		this.R73_COLUMN_N = R73_COLUMN_N;
	}

	public String getR74_COLUMN_A() {
		return R74_COLUMN_A;
	}

	public void setR74_COLUMN_A(String R74_COLUMN_A) {
		this.R74_COLUMN_A = R74_COLUMN_A;
	}

	public BigDecimal getR74_COLUMN_B() {
		return R74_COLUMN_B;
	}

	public void setR74_COLUMN_B(BigDecimal R74_COLUMN_B) {
		this.R74_COLUMN_B = R74_COLUMN_B;
	}

	public BigDecimal getR74_COLUMN_C() {
		return R74_COLUMN_C;
	}

	public void setR74_COLUMN_C(BigDecimal R74_COLUMN_C) {
		this.R74_COLUMN_C = R74_COLUMN_C;
	}

	public BigDecimal getR74_COLUMN_D() {
		return R74_COLUMN_D;
	}

	public void setR74_COLUMN_D(BigDecimal R74_COLUMN_D) {
		this.R74_COLUMN_D = R74_COLUMN_D;
	}

	public BigDecimal getR74_COLUMN_E() {
		return R74_COLUMN_E;
	}

	public void setR74_COLUMN_E(BigDecimal R74_COLUMN_E) {
		this.R74_COLUMN_E = R74_COLUMN_E;
	}

	public BigDecimal getR74_COLUMN_F() {
		return R74_COLUMN_F;
	}

	public void setR74_COLUMN_F(BigDecimal R74_COLUMN_F) {
		this.R74_COLUMN_F = R74_COLUMN_F;
	}

	public BigDecimal getR74_COLUMN_G() {
		return R74_COLUMN_G;
	}

	public void setR74_COLUMN_G(BigDecimal R74_COLUMN_G) {
		this.R74_COLUMN_G = R74_COLUMN_G;
	}

	public BigDecimal getR74_COLUMN_H() {
		return R74_COLUMN_H;
	}

	public void setR74_COLUMN_H(BigDecimal R74_COLUMN_H) {
		this.R74_COLUMN_H = R74_COLUMN_H;
	}

	public BigDecimal getR74_COLUMN_I() {
		return R74_COLUMN_I;
	}

	public void setR74_COLUMN_I(BigDecimal R74_COLUMN_I) {
		this.R74_COLUMN_I = R74_COLUMN_I;
	}

	public BigDecimal getR74_COLUMN_J() {
		return R74_COLUMN_J;
	}

	public void setR74_COLUMN_J(BigDecimal R74_COLUMN_J) {
		this.R74_COLUMN_J = R74_COLUMN_J;
	}

	public BigDecimal getR74_COLUMN_K() {
		return R74_COLUMN_K;
	}

	public void setR74_COLUMN_K(BigDecimal R74_COLUMN_K) {
		this.R74_COLUMN_K = R74_COLUMN_K;
	}

	public BigDecimal getR74_COLUMN_L() {
		return R74_COLUMN_L;
	}

	public void setR74_COLUMN_L(BigDecimal R74_COLUMN_L) {
		this.R74_COLUMN_L = R74_COLUMN_L;
	}

	public BigDecimal getR74_COLUMN_M() {
		return R74_COLUMN_M;
	}

	public void setR74_COLUMN_M(BigDecimal R74_COLUMN_M) {
		this.R74_COLUMN_M = R74_COLUMN_M;
	}

	public BigDecimal getR74_COLUMN_N() {
		return R74_COLUMN_N;
	}

	public void setR74_COLUMN_N(BigDecimal R74_COLUMN_N) {
		this.R74_COLUMN_N = R74_COLUMN_N;
	}

	public String getR75_COLUMN_A() {
		return R75_COLUMN_A;
	}

	public void setR75_COLUMN_A(String R75_COLUMN_A) {
		this.R75_COLUMN_A = R75_COLUMN_A;
	}

	public BigDecimal getR75_COLUMN_B() {
		return R75_COLUMN_B;
	}

	public void setR75_COLUMN_B(BigDecimal R75_COLUMN_B) {
		this.R75_COLUMN_B = R75_COLUMN_B;
	}

	public BigDecimal getR75_COLUMN_C() {
		return R75_COLUMN_C;
	}

	public void setR75_COLUMN_C(BigDecimal R75_COLUMN_C) {
		this.R75_COLUMN_C = R75_COLUMN_C;
	}

	public BigDecimal getR75_COLUMN_D() {
		return R75_COLUMN_D;
	}

	public void setR75_COLUMN_D(BigDecimal R75_COLUMN_D) {
		this.R75_COLUMN_D = R75_COLUMN_D;
	}

	public BigDecimal getR75_COLUMN_E() {
		return R75_COLUMN_E;
	}

	public void setR75_COLUMN_E(BigDecimal R75_COLUMN_E) {
		this.R75_COLUMN_E = R75_COLUMN_E;
	}

	public BigDecimal getR75_COLUMN_F() {
		return R75_COLUMN_F;
	}

	public void setR75_COLUMN_F(BigDecimal R75_COLUMN_F) {
		this.R75_COLUMN_F = R75_COLUMN_F;
	}

	public BigDecimal getR75_COLUMN_G() {
		return R75_COLUMN_G;
	}

	public void setR75_COLUMN_G(BigDecimal R75_COLUMN_G) {
		this.R75_COLUMN_G = R75_COLUMN_G;
	}

	public BigDecimal getR75_COLUMN_H() {
		return R75_COLUMN_H;
	}

	public void setR75_COLUMN_H(BigDecimal R75_COLUMN_H) {
		this.R75_COLUMN_H = R75_COLUMN_H;
	}

	public BigDecimal getR75_COLUMN_I() {
		return R75_COLUMN_I;
	}

	public void setR75_COLUMN_I(BigDecimal R75_COLUMN_I) {
		this.R75_COLUMN_I = R75_COLUMN_I;
	}

	public BigDecimal getR75_COLUMN_J() {
		return R75_COLUMN_J;
	}

	public void setR75_COLUMN_J(BigDecimal R75_COLUMN_J) {
		this.R75_COLUMN_J = R75_COLUMN_J;
	}

	public BigDecimal getR75_COLUMN_K() {
		return R75_COLUMN_K;
	}

	public void setR75_COLUMN_K(BigDecimal R75_COLUMN_K) {
		this.R75_COLUMN_K = R75_COLUMN_K;
	}

	public BigDecimal getR75_COLUMN_L() {
		return R75_COLUMN_L;
	}

	public void setR75_COLUMN_L(BigDecimal R75_COLUMN_L) {
		this.R75_COLUMN_L = R75_COLUMN_L;
	}

	public BigDecimal getR75_COLUMN_M() {
		return R75_COLUMN_M;
	}

	public void setR75_COLUMN_M(BigDecimal R75_COLUMN_M) {
		this.R75_COLUMN_M = R75_COLUMN_M;
	}

	public BigDecimal getR75_COLUMN_N() {
		return R75_COLUMN_N;
	}

	public void setR75_COLUMN_N(BigDecimal R75_COLUMN_N) {
		this.R75_COLUMN_N = R75_COLUMN_N;
	}

	public String getR76_COLUMN_A() {
		return R76_COLUMN_A;
	}

	public void setR76_COLUMN_A(String R76_COLUMN_A) {
		this.R76_COLUMN_A = R76_COLUMN_A;
	}

	public BigDecimal getR76_COLUMN_B() {
		return R76_COLUMN_B;
	}

	public void setR76_COLUMN_B(BigDecimal R76_COLUMN_B) {
		this.R76_COLUMN_B = R76_COLUMN_B;
	}

	public BigDecimal getR76_COLUMN_C() {
		return R76_COLUMN_C;
	}

	public void setR76_COLUMN_C(BigDecimal R76_COLUMN_C) {
		this.R76_COLUMN_C = R76_COLUMN_C;
	}

	public BigDecimal getR76_COLUMN_D() {
		return R76_COLUMN_D;
	}

	public void setR76_COLUMN_D(BigDecimal R76_COLUMN_D) {
		this.R76_COLUMN_D = R76_COLUMN_D;
	}

	public BigDecimal getR76_COLUMN_E() {
		return R76_COLUMN_E;
	}

	public void setR76_COLUMN_E(BigDecimal R76_COLUMN_E) {
		this.R76_COLUMN_E = R76_COLUMN_E;
	}

	public BigDecimal getR76_COLUMN_F() {
		return R76_COLUMN_F;
	}

	public void setR76_COLUMN_F(BigDecimal R76_COLUMN_F) {
		this.R76_COLUMN_F = R76_COLUMN_F;
	}

	public BigDecimal getR76_COLUMN_G() {
		return R76_COLUMN_G;
	}

	public void setR76_COLUMN_G(BigDecimal R76_COLUMN_G) {
		this.R76_COLUMN_G = R76_COLUMN_G;
	}

	public BigDecimal getR76_COLUMN_H() {
		return R76_COLUMN_H;
	}

	public void setR76_COLUMN_H(BigDecimal R76_COLUMN_H) {
		this.R76_COLUMN_H = R76_COLUMN_H;
	}

	public BigDecimal getR76_COLUMN_I() {
		return R76_COLUMN_I;
	}

	public void setR76_COLUMN_I(BigDecimal R76_COLUMN_I) {
		this.R76_COLUMN_I = R76_COLUMN_I;
	}

	public BigDecimal getR76_COLUMN_J() {
		return R76_COLUMN_J;
	}

	public void setR76_COLUMN_J(BigDecimal R76_COLUMN_J) {
		this.R76_COLUMN_J = R76_COLUMN_J;
	}

	public BigDecimal getR76_COLUMN_K() {
		return R76_COLUMN_K;
	}

	public void setR76_COLUMN_K(BigDecimal R76_COLUMN_K) {
		this.R76_COLUMN_K = R76_COLUMN_K;
	}

	public BigDecimal getR76_COLUMN_L() {
		return R76_COLUMN_L;
	}

	public void setR76_COLUMN_L(BigDecimal R76_COLUMN_L) {
		this.R76_COLUMN_L = R76_COLUMN_L;
	}

	public BigDecimal getR76_COLUMN_M() {
		return R76_COLUMN_M;
	}

	public void setR76_COLUMN_M(BigDecimal R76_COLUMN_M) {
		this.R76_COLUMN_M = R76_COLUMN_M;
	}

	public BigDecimal getR76_COLUMN_N() {
		return R76_COLUMN_N;
	}

	public void setR76_COLUMN_N(BigDecimal R76_COLUMN_N) {
		this.R76_COLUMN_N = R76_COLUMN_N;
	}

	public String getR77_COLUMN_A() {
		return R77_COLUMN_A;
	}

	public void setR77_COLUMN_A(String R77_COLUMN_A) {
		this.R77_COLUMN_A = R77_COLUMN_A;
	}

	public BigDecimal getR77_COLUMN_B() {
		return R77_COLUMN_B;
	}

	public void setR77_COLUMN_B(BigDecimal R77_COLUMN_B) {
		this.R77_COLUMN_B = R77_COLUMN_B;
	}

	public BigDecimal getR77_COLUMN_C() {
		return R77_COLUMN_C;
	}

	public void setR77_COLUMN_C(BigDecimal R77_COLUMN_C) {
		this.R77_COLUMN_C = R77_COLUMN_C;
	}

	public BigDecimal getR77_COLUMN_D() {
		return R77_COLUMN_D;
	}

	public void setR77_COLUMN_D(BigDecimal R77_COLUMN_D) {
		this.R77_COLUMN_D = R77_COLUMN_D;
	}

	public BigDecimal getR77_COLUMN_E() {
		return R77_COLUMN_E;
	}

	public void setR77_COLUMN_E(BigDecimal R77_COLUMN_E) {
		this.R77_COLUMN_E = R77_COLUMN_E;
	}

	public BigDecimal getR77_COLUMN_F() {
		return R77_COLUMN_F;
	}

	public void setR77_COLUMN_F(BigDecimal R77_COLUMN_F) {
		this.R77_COLUMN_F = R77_COLUMN_F;
	}

	public BigDecimal getR77_COLUMN_G() {
		return R77_COLUMN_G;
	}

	public void setR77_COLUMN_G(BigDecimal R77_COLUMN_G) {
		this.R77_COLUMN_G = R77_COLUMN_G;
	}

	public BigDecimal getR77_COLUMN_H() {
		return R77_COLUMN_H;
	}

	public void setR77_COLUMN_H(BigDecimal R77_COLUMN_H) {
		this.R77_COLUMN_H = R77_COLUMN_H;
	}

	public BigDecimal getR77_COLUMN_I() {
		return R77_COLUMN_I;
	}

	public void setR77_COLUMN_I(BigDecimal R77_COLUMN_I) {
		this.R77_COLUMN_I = R77_COLUMN_I;
	}

	public BigDecimal getR77_COLUMN_J() {
		return R77_COLUMN_J;
	}

	public void setR77_COLUMN_J(BigDecimal R77_COLUMN_J) {
		this.R77_COLUMN_J = R77_COLUMN_J;
	}

	public BigDecimal getR77_COLUMN_K() {
		return R77_COLUMN_K;
	}

	public void setR77_COLUMN_K(BigDecimal R77_COLUMN_K) {
		this.R77_COLUMN_K = R77_COLUMN_K;
	}

	public BigDecimal getR77_COLUMN_L() {
		return R77_COLUMN_L;
	}

	public void setR77_COLUMN_L(BigDecimal R77_COLUMN_L) {
		this.R77_COLUMN_L = R77_COLUMN_L;
	}

	public BigDecimal getR77_COLUMN_M() {
		return R77_COLUMN_M;
	}

	public void setR77_COLUMN_M(BigDecimal R77_COLUMN_M) {
		this.R77_COLUMN_M = R77_COLUMN_M;
	}

	public BigDecimal getR77_COLUMN_N() {
		return R77_COLUMN_N;
	}

	public void setR77_COLUMN_N(BigDecimal R77_COLUMN_N) {
		this.R77_COLUMN_N = R77_COLUMN_N;
	}

	public String getR78_COLUMN_A() {
		return R78_COLUMN_A;
	}

	public void setR78_COLUMN_A(String R78_COLUMN_A) {
		this.R78_COLUMN_A = R78_COLUMN_A;
	}

	public BigDecimal getR78_COLUMN_B() {
		return R78_COLUMN_B;
	}

	public void setR78_COLUMN_B(BigDecimal R78_COLUMN_B) {
		this.R78_COLUMN_B = R78_COLUMN_B;
	}

	public BigDecimal getR78_COLUMN_C() {
		return R78_COLUMN_C;
	}

	public void setR78_COLUMN_C(BigDecimal R78_COLUMN_C) {
		this.R78_COLUMN_C = R78_COLUMN_C;
	}

	public BigDecimal getR78_COLUMN_D() {
		return R78_COLUMN_D;
	}

	public void setR78_COLUMN_D(BigDecimal R78_COLUMN_D) {
		this.R78_COLUMN_D = R78_COLUMN_D;
	}

	public BigDecimal getR78_COLUMN_E() {
		return R78_COLUMN_E;
	}

	public void setR78_COLUMN_E(BigDecimal R78_COLUMN_E) {
		this.R78_COLUMN_E = R78_COLUMN_E;
	}

	public BigDecimal getR78_COLUMN_F() {
		return R78_COLUMN_F;
	}

	public void setR78_COLUMN_F(BigDecimal R78_COLUMN_F) {
		this.R78_COLUMN_F = R78_COLUMN_F;
	}

	public BigDecimal getR78_COLUMN_G() {
		return R78_COLUMN_G;
	}

	public void setR78_COLUMN_G(BigDecimal R78_COLUMN_G) {
		this.R78_COLUMN_G = R78_COLUMN_G;
	}

	public BigDecimal getR78_COLUMN_H() {
		return R78_COLUMN_H;
	}

	public void setR78_COLUMN_H(BigDecimal R78_COLUMN_H) {
		this.R78_COLUMN_H = R78_COLUMN_H;
	}

	public BigDecimal getR78_COLUMN_I() {
		return R78_COLUMN_I;
	}

	public void setR78_COLUMN_I(BigDecimal R78_COLUMN_I) {
		this.R78_COLUMN_I = R78_COLUMN_I;
	}

	public BigDecimal getR78_COLUMN_J() {
		return R78_COLUMN_J;
	}

	public void setR78_COLUMN_J(BigDecimal R78_COLUMN_J) {
		this.R78_COLUMN_J = R78_COLUMN_J;
	}

	public BigDecimal getR78_COLUMN_K() {
		return R78_COLUMN_K;
	}

	public void setR78_COLUMN_K(BigDecimal R78_COLUMN_K) {
		this.R78_COLUMN_K = R78_COLUMN_K;
	}

	public BigDecimal getR78_COLUMN_L() {
		return R78_COLUMN_L;
	}

	public void setR78_COLUMN_L(BigDecimal R78_COLUMN_L) {
		this.R78_COLUMN_L = R78_COLUMN_L;
	}

	public BigDecimal getR78_COLUMN_M() {
		return R78_COLUMN_M;
	}

	public void setR78_COLUMN_M(BigDecimal R78_COLUMN_M) {
		this.R78_COLUMN_M = R78_COLUMN_M;
	}

	public BigDecimal getR78_COLUMN_N() {
		return R78_COLUMN_N;
	}

	public void setR78_COLUMN_N(BigDecimal R78_COLUMN_N) {
		this.R78_COLUMN_N = R78_COLUMN_N;
	}

	public String getR79_COLUMN_A() {
		return R79_COLUMN_A;
	}

	public void setR79_COLUMN_A(String R79_COLUMN_A) {
		this.R79_COLUMN_A = R79_COLUMN_A;
	}

	public BigDecimal getR79_COLUMN_B() {
		return R79_COLUMN_B;
	}

	public void setR79_COLUMN_B(BigDecimal R79_COLUMN_B) {
		this.R79_COLUMN_B = R79_COLUMN_B;
	}

	public BigDecimal getR79_COLUMN_C() {
		return R79_COLUMN_C;
	}

	public void setR79_COLUMN_C(BigDecimal R79_COLUMN_C) {
		this.R79_COLUMN_C = R79_COLUMN_C;
	}

	public BigDecimal getR79_COLUMN_D() {
		return R79_COLUMN_D;
	}

	public void setR79_COLUMN_D(BigDecimal R79_COLUMN_D) {
		this.R79_COLUMN_D = R79_COLUMN_D;
	}

	public BigDecimal getR79_COLUMN_E() {
		return R79_COLUMN_E;
	}

	public void setR79_COLUMN_E(BigDecimal R79_COLUMN_E) {
		this.R79_COLUMN_E = R79_COLUMN_E;
	}

	public BigDecimal getR79_COLUMN_F() {
		return R79_COLUMN_F;
	}

	public void setR79_COLUMN_F(BigDecimal R79_COLUMN_F) {
		this.R79_COLUMN_F = R79_COLUMN_F;
	}

	public BigDecimal getR79_COLUMN_G() {
		return R79_COLUMN_G;
	}

	public void setR79_COLUMN_G(BigDecimal R79_COLUMN_G) {
		this.R79_COLUMN_G = R79_COLUMN_G;
	}

	public BigDecimal getR79_COLUMN_H() {
		return R79_COLUMN_H;
	}

	public void setR79_COLUMN_H(BigDecimal R79_COLUMN_H) {
		this.R79_COLUMN_H = R79_COLUMN_H;
	}

	public BigDecimal getR79_COLUMN_I() {
		return R79_COLUMN_I;
	}

	public void setR79_COLUMN_I(BigDecimal R79_COLUMN_I) {
		this.R79_COLUMN_I = R79_COLUMN_I;
	}

	public BigDecimal getR79_COLUMN_J() {
		return R79_COLUMN_J;
	}

	public void setR79_COLUMN_J(BigDecimal R79_COLUMN_J) {
		this.R79_COLUMN_J = R79_COLUMN_J;
	}

	public BigDecimal getR79_COLUMN_K() {
		return R79_COLUMN_K;
	}

	public void setR79_COLUMN_K(BigDecimal R79_COLUMN_K) {
		this.R79_COLUMN_K = R79_COLUMN_K;
	}

	public BigDecimal getR79_COLUMN_L() {
		return R79_COLUMN_L;
	}

	public void setR79_COLUMN_L(BigDecimal R79_COLUMN_L) {
		this.R79_COLUMN_L = R79_COLUMN_L;
	}

	public BigDecimal getR79_COLUMN_M() {
		return R79_COLUMN_M;
	}

	public void setR79_COLUMN_M(BigDecimal R79_COLUMN_M) {
		this.R79_COLUMN_M = R79_COLUMN_M;
	}

	public BigDecimal getR79_COLUMN_N() {
		return R79_COLUMN_N;
	}

	public void setR79_COLUMN_N(BigDecimal R79_COLUMN_N) {
		this.R79_COLUMN_N = R79_COLUMN_N;
	}

	public String getR80_COLUMN_A() {
		return R80_COLUMN_A;
	}

	public void setR80_COLUMN_A(String R80_COLUMN_A) {
		this.R80_COLUMN_A = R80_COLUMN_A;
	}

	public BigDecimal getR80_COLUMN_B() {
		return R80_COLUMN_B;
	}

	public void setR80_COLUMN_B(BigDecimal R80_COLUMN_B) {
		this.R80_COLUMN_B = R80_COLUMN_B;
	}

	public BigDecimal getR80_COLUMN_C() {
		return R80_COLUMN_C;
	}

	public void setR80_COLUMN_C(BigDecimal R80_COLUMN_C) {
		this.R80_COLUMN_C = R80_COLUMN_C;
	}

	public BigDecimal getR80_COLUMN_D() {
		return R80_COLUMN_D;
	}

	public void setR80_COLUMN_D(BigDecimal R80_COLUMN_D) {
		this.R80_COLUMN_D = R80_COLUMN_D;
	}

	public BigDecimal getR80_COLUMN_E() {
		return R80_COLUMN_E;
	}

	public void setR80_COLUMN_E(BigDecimal R80_COLUMN_E) {
		this.R80_COLUMN_E = R80_COLUMN_E;
	}

	public BigDecimal getR80_COLUMN_F() {
		return R80_COLUMN_F;
	}

	public void setR80_COLUMN_F(BigDecimal R80_COLUMN_F) {
		this.R80_COLUMN_F = R80_COLUMN_F;
	}

	public BigDecimal getR80_COLUMN_G() {
		return R80_COLUMN_G;
	}

	public void setR80_COLUMN_G(BigDecimal R80_COLUMN_G) {
		this.R80_COLUMN_G = R80_COLUMN_G;
	}

	public BigDecimal getR80_COLUMN_H() {
		return R80_COLUMN_H;
	}

	public void setR80_COLUMN_H(BigDecimal R80_COLUMN_H) {
		this.R80_COLUMN_H = R80_COLUMN_H;
	}

	public BigDecimal getR80_COLUMN_I() {
		return R80_COLUMN_I;
	}

	public void setR80_COLUMN_I(BigDecimal R80_COLUMN_I) {
		this.R80_COLUMN_I = R80_COLUMN_I;
	}

	public BigDecimal getR80_COLUMN_J() {
		return R80_COLUMN_J;
	}

	public void setR80_COLUMN_J(BigDecimal R80_COLUMN_J) {
		this.R80_COLUMN_J = R80_COLUMN_J;
	}

	public BigDecimal getR80_COLUMN_K() {
		return R80_COLUMN_K;
	}

	public void setR80_COLUMN_K(BigDecimal R80_COLUMN_K) {
		this.R80_COLUMN_K = R80_COLUMN_K;
	}

	public BigDecimal getR80_COLUMN_L() {
		return R80_COLUMN_L;
	}

	public void setR80_COLUMN_L(BigDecimal R80_COLUMN_L) {
		this.R80_COLUMN_L = R80_COLUMN_L;
	}

	public BigDecimal getR80_COLUMN_M() {
		return R80_COLUMN_M;
	}

	public void setR80_COLUMN_M(BigDecimal R80_COLUMN_M) {
		this.R80_COLUMN_M = R80_COLUMN_M;
	}

	public BigDecimal getR80_COLUMN_N() {
		return R80_COLUMN_N;
	}

	public void setR80_COLUMN_N(BigDecimal R80_COLUMN_N) {
		this.R80_COLUMN_N = R80_COLUMN_N;
	}

	public String getR81_COLUMN_A() {
		return R81_COLUMN_A;
	}

	public void setR81_COLUMN_A(String R81_COLUMN_A) {
		this.R81_COLUMN_A = R81_COLUMN_A;
	}

	public BigDecimal getR81_COLUMN_B() {
		return R81_COLUMN_B;
	}

	public void setR81_COLUMN_B(BigDecimal R81_COLUMN_B) {
		this.R81_COLUMN_B = R81_COLUMN_B;
	}

	public BigDecimal getR81_COLUMN_C() {
		return R81_COLUMN_C;
	}

	public void setR81_COLUMN_C(BigDecimal R81_COLUMN_C) {
		this.R81_COLUMN_C = R81_COLUMN_C;
	}

	public BigDecimal getR81_COLUMN_D() {
		return R81_COLUMN_D;
	}

	public void setR81_COLUMN_D(BigDecimal R81_COLUMN_D) {
		this.R81_COLUMN_D = R81_COLUMN_D;
	}

	public BigDecimal getR81_COLUMN_E() {
		return R81_COLUMN_E;
	}

	public void setR81_COLUMN_E(BigDecimal R81_COLUMN_E) {
		this.R81_COLUMN_E = R81_COLUMN_E;
	}

	public BigDecimal getR81_COLUMN_F() {
		return R81_COLUMN_F;
	}

	public void setR81_COLUMN_F(BigDecimal R81_COLUMN_F) {
		this.R81_COLUMN_F = R81_COLUMN_F;
	}

	public BigDecimal getR81_COLUMN_G() {
		return R81_COLUMN_G;
	}

	public void setR81_COLUMN_G(BigDecimal R81_COLUMN_G) {
		this.R81_COLUMN_G = R81_COLUMN_G;
	}

	public BigDecimal getR81_COLUMN_H() {
		return R81_COLUMN_H;
	}

	public void setR81_COLUMN_H(BigDecimal R81_COLUMN_H) {
		this.R81_COLUMN_H = R81_COLUMN_H;
	}

	public BigDecimal getR81_COLUMN_I() {
		return R81_COLUMN_I;
	}

	public void setR81_COLUMN_I(BigDecimal R81_COLUMN_I) {
		this.R81_COLUMN_I = R81_COLUMN_I;
	}

	public BigDecimal getR81_COLUMN_J() {
		return R81_COLUMN_J;
	}

	public void setR81_COLUMN_J(BigDecimal R81_COLUMN_J) {
		this.R81_COLUMN_J = R81_COLUMN_J;
	}

	public BigDecimal getR81_COLUMN_K() {
		return R81_COLUMN_K;
	}

	public void setR81_COLUMN_K(BigDecimal R81_COLUMN_K) {
		this.R81_COLUMN_K = R81_COLUMN_K;
	}

	public BigDecimal getR81_COLUMN_L() {
		return R81_COLUMN_L;
	}

	public void setR81_COLUMN_L(BigDecimal R81_COLUMN_L) {
		this.R81_COLUMN_L = R81_COLUMN_L;
	}

	public BigDecimal getR81_COLUMN_M() {
		return R81_COLUMN_M;
	}

	public void setR81_COLUMN_M(BigDecimal R81_COLUMN_M) {
		this.R81_COLUMN_M = R81_COLUMN_M;
	}

	public BigDecimal getR81_COLUMN_N() {
		return R81_COLUMN_N;
	}

	public void setR81_COLUMN_N(BigDecimal R81_COLUMN_N) {
		this.R81_COLUMN_N = R81_COLUMN_N;
	}

	public String getR82_COLUMN_A() {
		return R82_COLUMN_A;
	}

	public void setR82_COLUMN_A(String R82_COLUMN_A) {
		this.R82_COLUMN_A = R82_COLUMN_A;
	}

	public BigDecimal getR82_COLUMN_B() {
		return R82_COLUMN_B;
	}

	public void setR82_COLUMN_B(BigDecimal R82_COLUMN_B) {
		this.R82_COLUMN_B = R82_COLUMN_B;
	}

	public BigDecimal getR82_COLUMN_C() {
		return R82_COLUMN_C;
	}

	public void setR82_COLUMN_C(BigDecimal R82_COLUMN_C) {
		this.R82_COLUMN_C = R82_COLUMN_C;
	}

	public BigDecimal getR82_COLUMN_D() {
		return R82_COLUMN_D;
	}

	public void setR82_COLUMN_D(BigDecimal R82_COLUMN_D) {
		this.R82_COLUMN_D = R82_COLUMN_D;
	}

	public BigDecimal getR82_COLUMN_E() {
		return R82_COLUMN_E;
	}

	public void setR82_COLUMN_E(BigDecimal R82_COLUMN_E) {
		this.R82_COLUMN_E = R82_COLUMN_E;
	}

	public BigDecimal getR82_COLUMN_F() {
		return R82_COLUMN_F;
	}

	public void setR82_COLUMN_F(BigDecimal R82_COLUMN_F) {
		this.R82_COLUMN_F = R82_COLUMN_F;
	}

	public BigDecimal getR82_COLUMN_G() {
		return R82_COLUMN_G;
	}

	public void setR82_COLUMN_G(BigDecimal R82_COLUMN_G) {
		this.R82_COLUMN_G = R82_COLUMN_G;
	}

	public BigDecimal getR82_COLUMN_H() {
		return R82_COLUMN_H;
	}

	public void setR82_COLUMN_H(BigDecimal R82_COLUMN_H) {
		this.R82_COLUMN_H = R82_COLUMN_H;
	}

	public BigDecimal getR82_COLUMN_I() {
		return R82_COLUMN_I;
	}

	public void setR82_COLUMN_I(BigDecimal R82_COLUMN_I) {
		this.R82_COLUMN_I = R82_COLUMN_I;
	}

	public BigDecimal getR82_COLUMN_J() {
		return R82_COLUMN_J;
	}

	public void setR82_COLUMN_J(BigDecimal R82_COLUMN_J) {
		this.R82_COLUMN_J = R82_COLUMN_J;
	}

	public BigDecimal getR82_COLUMN_K() {
		return R82_COLUMN_K;
	}

	public void setR82_COLUMN_K(BigDecimal R82_COLUMN_K) {
		this.R82_COLUMN_K = R82_COLUMN_K;
	}

	public BigDecimal getR82_COLUMN_L() {
		return R82_COLUMN_L;
	}

	public void setR82_COLUMN_L(BigDecimal R82_COLUMN_L) {
		this.R82_COLUMN_L = R82_COLUMN_L;
	}

	public BigDecimal getR82_COLUMN_M() {
		return R82_COLUMN_M;
	}

	public void setR82_COLUMN_M(BigDecimal R82_COLUMN_M) {
		this.R82_COLUMN_M = R82_COLUMN_M;
	}

	public BigDecimal getR82_COLUMN_N() {
		return R82_COLUMN_N;
	}

	public void setR82_COLUMN_N(BigDecimal R82_COLUMN_N) {
		this.R82_COLUMN_N = R82_COLUMN_N;
	}

	public String getR83_COLUMN_A() {
		return R83_COLUMN_A;
	}

	public void setR83_COLUMN_A(String R83_COLUMN_A) {
		this.R83_COLUMN_A = R83_COLUMN_A;
	}

	public BigDecimal getR83_COLUMN_B() {
		return R83_COLUMN_B;
	}

	public void setR83_COLUMN_B(BigDecimal R83_COLUMN_B) {
		this.R83_COLUMN_B = R83_COLUMN_B;
	}

	public BigDecimal getR83_COLUMN_C() {
		return R83_COLUMN_C;
	}

	public void setR83_COLUMN_C(BigDecimal R83_COLUMN_C) {
		this.R83_COLUMN_C = R83_COLUMN_C;
	}

	public BigDecimal getR83_COLUMN_D() {
		return R83_COLUMN_D;
	}

	public void setR83_COLUMN_D(BigDecimal R83_COLUMN_D) {
		this.R83_COLUMN_D = R83_COLUMN_D;
	}

	public BigDecimal getR83_COLUMN_E() {
		return R83_COLUMN_E;
	}

	public void setR83_COLUMN_E(BigDecimal R83_COLUMN_E) {
		this.R83_COLUMN_E = R83_COLUMN_E;
	}

	public BigDecimal getR83_COLUMN_F() {
		return R83_COLUMN_F;
	}

	public void setR83_COLUMN_F(BigDecimal R83_COLUMN_F) {
		this.R83_COLUMN_F = R83_COLUMN_F;
	}

	public BigDecimal getR83_COLUMN_G() {
		return R83_COLUMN_G;
	}

	public void setR83_COLUMN_G(BigDecimal R83_COLUMN_G) {
		this.R83_COLUMN_G = R83_COLUMN_G;
	}

	public BigDecimal getR83_COLUMN_H() {
		return R83_COLUMN_H;
	}

	public void setR83_COLUMN_H(BigDecimal R83_COLUMN_H) {
		this.R83_COLUMN_H = R83_COLUMN_H;
	}

	public BigDecimal getR83_COLUMN_I() {
		return R83_COLUMN_I;
	}

	public void setR83_COLUMN_I(BigDecimal R83_COLUMN_I) {
		this.R83_COLUMN_I = R83_COLUMN_I;
	}

	public BigDecimal getR83_COLUMN_J() {
		return R83_COLUMN_J;
	}

	public void setR83_COLUMN_J(BigDecimal R83_COLUMN_J) {
		this.R83_COLUMN_J = R83_COLUMN_J;
	}

	public BigDecimal getR83_COLUMN_K() {
		return R83_COLUMN_K;
	}

	public void setR83_COLUMN_K(BigDecimal R83_COLUMN_K) {
		this.R83_COLUMN_K = R83_COLUMN_K;
	}

	public BigDecimal getR83_COLUMN_L() {
		return R83_COLUMN_L;
	}

	public void setR83_COLUMN_L(BigDecimal R83_COLUMN_L) {
		this.R83_COLUMN_L = R83_COLUMN_L;
	}

	public BigDecimal getR83_COLUMN_M() {
		return R83_COLUMN_M;
	}

	public void setR83_COLUMN_M(BigDecimal R83_COLUMN_M) {
		this.R83_COLUMN_M = R83_COLUMN_M;
	}

	public BigDecimal getR83_COLUMN_N() {
		return R83_COLUMN_N;
	}

	public void setR83_COLUMN_N(BigDecimal R83_COLUMN_N) {
		this.R83_COLUMN_N = R83_COLUMN_N;
	}

	public String getR84_COLUMN_A() {
		return R84_COLUMN_A;
	}

	public void setR84_COLUMN_A(String R84_COLUMN_A) {
		this.R84_COLUMN_A = R84_COLUMN_A;
	}

	public BigDecimal getR84_COLUMN_B() {
		return R84_COLUMN_B;
	}

	public void setR84_COLUMN_B(BigDecimal R84_COLUMN_B) {
		this.R84_COLUMN_B = R84_COLUMN_B;
	}

	public BigDecimal getR84_COLUMN_C() {
		return R84_COLUMN_C;
	}

	public void setR84_COLUMN_C(BigDecimal R84_COLUMN_C) {
		this.R84_COLUMN_C = R84_COLUMN_C;
	}

	public BigDecimal getR84_COLUMN_D() {
		return R84_COLUMN_D;
	}

	public void setR84_COLUMN_D(BigDecimal R84_COLUMN_D) {
		this.R84_COLUMN_D = R84_COLUMN_D;
	}

	public BigDecimal getR84_COLUMN_E() {
		return R84_COLUMN_E;
	}

	public void setR84_COLUMN_E(BigDecimal R84_COLUMN_E) {
		this.R84_COLUMN_E = R84_COLUMN_E;
	}

	public BigDecimal getR84_COLUMN_F() {
		return R84_COLUMN_F;
	}

	public void setR84_COLUMN_F(BigDecimal R84_COLUMN_F) {
		this.R84_COLUMN_F = R84_COLUMN_F;
	}

	public BigDecimal getR84_COLUMN_G() {
		return R84_COLUMN_G;
	}

	public void setR84_COLUMN_G(BigDecimal R84_COLUMN_G) {
		this.R84_COLUMN_G = R84_COLUMN_G;
	}

	public BigDecimal getR84_COLUMN_H() {
		return R84_COLUMN_H;
	}

	public void setR84_COLUMN_H(BigDecimal R84_COLUMN_H) {
		this.R84_COLUMN_H = R84_COLUMN_H;
	}

	public BigDecimal getR84_COLUMN_I() {
		return R84_COLUMN_I;
	}

	public void setR84_COLUMN_I(BigDecimal R84_COLUMN_I) {
		this.R84_COLUMN_I = R84_COLUMN_I;
	}

	public BigDecimal getR84_COLUMN_J() {
		return R84_COLUMN_J;
	}

	public void setR84_COLUMN_J(BigDecimal R84_COLUMN_J) {
		this.R84_COLUMN_J = R84_COLUMN_J;
	}

	public BigDecimal getR84_COLUMN_K() {
		return R84_COLUMN_K;
	}

	public void setR84_COLUMN_K(BigDecimal R84_COLUMN_K) {
		this.R84_COLUMN_K = R84_COLUMN_K;
	}

	public BigDecimal getR84_COLUMN_L() {
		return R84_COLUMN_L;
	}

	public void setR84_COLUMN_L(BigDecimal R84_COLUMN_L) {
		this.R84_COLUMN_L = R84_COLUMN_L;
	}

	public BigDecimal getR84_COLUMN_M() {
		return R84_COLUMN_M;
	}

	public void setR84_COLUMN_M(BigDecimal R84_COLUMN_M) {
		this.R84_COLUMN_M = R84_COLUMN_M;
	}

	public BigDecimal getR84_COLUMN_N() {
		return R84_COLUMN_N;
	}

	public void setR84_COLUMN_N(BigDecimal R84_COLUMN_N) {
		this.R84_COLUMN_N = R84_COLUMN_N;
	}

	public String getR85_COLUMN_A() {
		return R85_COLUMN_A;
	}

	public void setR85_COLUMN_A(String R85_COLUMN_A) {
		this.R85_COLUMN_A = R85_COLUMN_A;
	}

	public BigDecimal getR85_COLUMN_B() {
		return R85_COLUMN_B;
	}

	public void setR85_COLUMN_B(BigDecimal R85_COLUMN_B) {
		this.R85_COLUMN_B = R85_COLUMN_B;
	}

	public BigDecimal getR85_COLUMN_C() {
		return R85_COLUMN_C;
	}

	public void setR85_COLUMN_C(BigDecimal R85_COLUMN_C) {
		this.R85_COLUMN_C = R85_COLUMN_C;
	}

	public BigDecimal getR85_COLUMN_D() {
		return R85_COLUMN_D;
	}

	public void setR85_COLUMN_D(BigDecimal R85_COLUMN_D) {
		this.R85_COLUMN_D = R85_COLUMN_D;
	}

	public BigDecimal getR85_COLUMN_E() {
		return R85_COLUMN_E;
	}

	public void setR85_COLUMN_E(BigDecimal R85_COLUMN_E) {
		this.R85_COLUMN_E = R85_COLUMN_E;
	}

	public BigDecimal getR85_COLUMN_F() {
		return R85_COLUMN_F;
	}

	public void setR85_COLUMN_F(BigDecimal R85_COLUMN_F) {
		this.R85_COLUMN_F = R85_COLUMN_F;
	}

	public BigDecimal getR85_COLUMN_G() {
		return R85_COLUMN_G;
	}

	public void setR85_COLUMN_G(BigDecimal R85_COLUMN_G) {
		this.R85_COLUMN_G = R85_COLUMN_G;
	}

	public BigDecimal getR85_COLUMN_H() {
		return R85_COLUMN_H;
	}

	public void setR85_COLUMN_H(BigDecimal R85_COLUMN_H) {
		this.R85_COLUMN_H = R85_COLUMN_H;
	}

	public BigDecimal getR85_COLUMN_I() {
		return R85_COLUMN_I;
	}

	public void setR85_COLUMN_I(BigDecimal R85_COLUMN_I) {
		this.R85_COLUMN_I = R85_COLUMN_I;
	}

	public BigDecimal getR85_COLUMN_J() {
		return R85_COLUMN_J;
	}

	public void setR85_COLUMN_J(BigDecimal R85_COLUMN_J) {
		this.R85_COLUMN_J = R85_COLUMN_J;
	}

	public BigDecimal getR85_COLUMN_K() {
		return R85_COLUMN_K;
	}

	public void setR85_COLUMN_K(BigDecimal R85_COLUMN_K) {
		this.R85_COLUMN_K = R85_COLUMN_K;
	}

	public BigDecimal getR85_COLUMN_L() {
		return R85_COLUMN_L;
	}

	public void setR85_COLUMN_L(BigDecimal R85_COLUMN_L) {
		this.R85_COLUMN_L = R85_COLUMN_L;
	}

	public BigDecimal getR85_COLUMN_M() {
		return R85_COLUMN_M;
	}

	public void setR85_COLUMN_M(BigDecimal R85_COLUMN_M) {
		this.R85_COLUMN_M = R85_COLUMN_M;
	}

	public BigDecimal getR85_COLUMN_N() {
		return R85_COLUMN_N;
	}

	public void setR85_COLUMN_N(BigDecimal R85_COLUMN_N) {
		this.R85_COLUMN_N = R85_COLUMN_N;
	}

	public String getR86_COLUMN_A() {
		return R86_COLUMN_A;
	}

	public void setR86_COLUMN_A(String R86_COLUMN_A) {
		this.R86_COLUMN_A = R86_COLUMN_A;
	}

	public BigDecimal getR86_COLUMN_B() {
		return R86_COLUMN_B;
	}

	public void setR86_COLUMN_B(BigDecimal R86_COLUMN_B) {
		this.R86_COLUMN_B = R86_COLUMN_B;
	}

	public BigDecimal getR86_COLUMN_C() {
		return R86_COLUMN_C;
	}

	public void setR86_COLUMN_C(BigDecimal R86_COLUMN_C) {
		this.R86_COLUMN_C = R86_COLUMN_C;
	}

	public BigDecimal getR86_COLUMN_D() {
		return R86_COLUMN_D;
	}

	public void setR86_COLUMN_D(BigDecimal R86_COLUMN_D) {
		this.R86_COLUMN_D = R86_COLUMN_D;
	}

	public BigDecimal getR86_COLUMN_E() {
		return R86_COLUMN_E;
	}

	public void setR86_COLUMN_E(BigDecimal R86_COLUMN_E) {
		this.R86_COLUMN_E = R86_COLUMN_E;
	}

	public BigDecimal getR86_COLUMN_F() {
		return R86_COLUMN_F;
	}

	public void setR86_COLUMN_F(BigDecimal R86_COLUMN_F) {
		this.R86_COLUMN_F = R86_COLUMN_F;
	}

	public BigDecimal getR86_COLUMN_G() {
		return R86_COLUMN_G;
	}

	public void setR86_COLUMN_G(BigDecimal R86_COLUMN_G) {
		this.R86_COLUMN_G = R86_COLUMN_G;
	}

	public BigDecimal getR86_COLUMN_H() {
		return R86_COLUMN_H;
	}

	public void setR86_COLUMN_H(BigDecimal R86_COLUMN_H) {
		this.R86_COLUMN_H = R86_COLUMN_H;
	}

	public BigDecimal getR86_COLUMN_I() {
		return R86_COLUMN_I;
	}

	public void setR86_COLUMN_I(BigDecimal R86_COLUMN_I) {
		this.R86_COLUMN_I = R86_COLUMN_I;
	}

	public BigDecimal getR86_COLUMN_J() {
		return R86_COLUMN_J;
	}

	public void setR86_COLUMN_J(BigDecimal R86_COLUMN_J) {
		this.R86_COLUMN_J = R86_COLUMN_J;
	}

	public BigDecimal getR86_COLUMN_K() {
		return R86_COLUMN_K;
	}

	public void setR86_COLUMN_K(BigDecimal R86_COLUMN_K) {
		this.R86_COLUMN_K = R86_COLUMN_K;
	}

	public BigDecimal getR86_COLUMN_L() {
		return R86_COLUMN_L;
	}

	public void setR86_COLUMN_L(BigDecimal R86_COLUMN_L) {
		this.R86_COLUMN_L = R86_COLUMN_L;
	}

	public BigDecimal getR86_COLUMN_M() {
		return R86_COLUMN_M;
	}

	public void setR86_COLUMN_M(BigDecimal R86_COLUMN_M) {
		this.R86_COLUMN_M = R86_COLUMN_M;
	}

	public BigDecimal getR86_COLUMN_N() {
		return R86_COLUMN_N;
	}

	public void setR86_COLUMN_N(BigDecimal R86_COLUMN_N) {
		this.R86_COLUMN_N = R86_COLUMN_N;
	}

	public String getR87_COLUMN_A() {
		return R87_COLUMN_A;
	}

	public void setR87_COLUMN_A(String R87_COLUMN_A) {
		this.R87_COLUMN_A = R87_COLUMN_A;
	}

	public BigDecimal getR87_COLUMN_B() {
		return R87_COLUMN_B;
	}

	public void setR87_COLUMN_B(BigDecimal R87_COLUMN_B) {
		this.R87_COLUMN_B = R87_COLUMN_B;
	}

	public BigDecimal getR87_COLUMN_C() {
		return R87_COLUMN_C;
	}

	public void setR87_COLUMN_C(BigDecimal R87_COLUMN_C) {
		this.R87_COLUMN_C = R87_COLUMN_C;
	}

	public BigDecimal getR87_COLUMN_D() {
		return R87_COLUMN_D;
	}

	public void setR87_COLUMN_D(BigDecimal R87_COLUMN_D) {
		this.R87_COLUMN_D = R87_COLUMN_D;
	}

	public BigDecimal getR87_COLUMN_E() {
		return R87_COLUMN_E;
	}

	public void setR87_COLUMN_E(BigDecimal R87_COLUMN_E) {
		this.R87_COLUMN_E = R87_COLUMN_E;
	}

	public BigDecimal getR87_COLUMN_F() {
		return R87_COLUMN_F;
	}

	public void setR87_COLUMN_F(BigDecimal R87_COLUMN_F) {
		this.R87_COLUMN_F = R87_COLUMN_F;
	}

	public BigDecimal getR87_COLUMN_G() {
		return R87_COLUMN_G;
	}

	public void setR87_COLUMN_G(BigDecimal R87_COLUMN_G) {
		this.R87_COLUMN_G = R87_COLUMN_G;
	}

	public BigDecimal getR87_COLUMN_H() {
		return R87_COLUMN_H;
	}

	public void setR87_COLUMN_H(BigDecimal R87_COLUMN_H) {
		this.R87_COLUMN_H = R87_COLUMN_H;
	}

	public BigDecimal getR87_COLUMN_I() {
		return R87_COLUMN_I;
	}

	public void setR87_COLUMN_I(BigDecimal R87_COLUMN_I) {
		this.R87_COLUMN_I = R87_COLUMN_I;
	}

	public BigDecimal getR87_COLUMN_J() {
		return R87_COLUMN_J;
	}

	public void setR87_COLUMN_J(BigDecimal R87_COLUMN_J) {
		this.R87_COLUMN_J = R87_COLUMN_J;
	}

	public BigDecimal getR87_COLUMN_K() {
		return R87_COLUMN_K;
	}

	public void setR87_COLUMN_K(BigDecimal R87_COLUMN_K) {
		this.R87_COLUMN_K = R87_COLUMN_K;
	}

	public BigDecimal getR87_COLUMN_L() {
		return R87_COLUMN_L;
	}

	public void setR87_COLUMN_L(BigDecimal R87_COLUMN_L) {
		this.R87_COLUMN_L = R87_COLUMN_L;
	}

	public BigDecimal getR87_COLUMN_M() {
		return R87_COLUMN_M;
	}

	public void setR87_COLUMN_M(BigDecimal R87_COLUMN_M) {
		this.R87_COLUMN_M = R87_COLUMN_M;
	}

	public BigDecimal getR87_COLUMN_N() {
		return R87_COLUMN_N;
	}

	public void setR87_COLUMN_N(BigDecimal R87_COLUMN_N) {
		this.R87_COLUMN_N = R87_COLUMN_N;
	}

	public String getR88_COLUMN_A() {
		return R88_COLUMN_A;
	}

	public void setR88_COLUMN_A(String R88_COLUMN_A) {
		this.R88_COLUMN_A = R88_COLUMN_A;
	}

	public BigDecimal getR88_COLUMN_B() {
		return R88_COLUMN_B;
	}

	public void setR88_COLUMN_B(BigDecimal R88_COLUMN_B) {
		this.R88_COLUMN_B = R88_COLUMN_B;
	}

	public BigDecimal getR88_COLUMN_C() {
		return R88_COLUMN_C;
	}

	public void setR88_COLUMN_C(BigDecimal R88_COLUMN_C) {
		this.R88_COLUMN_C = R88_COLUMN_C;
	}

	public BigDecimal getR88_COLUMN_D() {
		return R88_COLUMN_D;
	}

	public void setR88_COLUMN_D(BigDecimal R88_COLUMN_D) {
		this.R88_COLUMN_D = R88_COLUMN_D;
	}

	public BigDecimal getR88_COLUMN_E() {
		return R88_COLUMN_E;
	}

	public void setR88_COLUMN_E(BigDecimal R88_COLUMN_E) {
		this.R88_COLUMN_E = R88_COLUMN_E;
	}

	public BigDecimal getR88_COLUMN_F() {
		return R88_COLUMN_F;
	}

	public void setR88_COLUMN_F(BigDecimal R88_COLUMN_F) {
		this.R88_COLUMN_F = R88_COLUMN_F;
	}

	public BigDecimal getR88_COLUMN_G() {
		return R88_COLUMN_G;
	}

	public void setR88_COLUMN_G(BigDecimal R88_COLUMN_G) {
		this.R88_COLUMN_G = R88_COLUMN_G;
	}

	public BigDecimal getR88_COLUMN_H() {
		return R88_COLUMN_H;
	}

	public void setR88_COLUMN_H(BigDecimal R88_COLUMN_H) {
		this.R88_COLUMN_H = R88_COLUMN_H;
	}

	public BigDecimal getR88_COLUMN_I() {
		return R88_COLUMN_I;
	}

	public void setR88_COLUMN_I(BigDecimal R88_COLUMN_I) {
		this.R88_COLUMN_I = R88_COLUMN_I;
	}

	public BigDecimal getR88_COLUMN_J() {
		return R88_COLUMN_J;
	}

	public void setR88_COLUMN_J(BigDecimal R88_COLUMN_J) {
		this.R88_COLUMN_J = R88_COLUMN_J;
	}

	public BigDecimal getR88_COLUMN_K() {
		return R88_COLUMN_K;
	}

	public void setR88_COLUMN_K(BigDecimal R88_COLUMN_K) {
		this.R88_COLUMN_K = R88_COLUMN_K;
	}

	public BigDecimal getR88_COLUMN_L() {
		return R88_COLUMN_L;
	}

	public void setR88_COLUMN_L(BigDecimal R88_COLUMN_L) {
		this.R88_COLUMN_L = R88_COLUMN_L;
	}

	public BigDecimal getR88_COLUMN_M() {
		return R88_COLUMN_M;
	}

	public void setR88_COLUMN_M(BigDecimal R88_COLUMN_M) {
		this.R88_COLUMN_M = R88_COLUMN_M;
	}

	public BigDecimal getR88_COLUMN_N() {
		return R88_COLUMN_N;
	}

	public void setR88_COLUMN_N(BigDecimal R88_COLUMN_N) {
		this.R88_COLUMN_N = R88_COLUMN_N;
	}

	public String getR89_COLUMN_A() {
		return R89_COLUMN_A;
	}

	public void setR89_COLUMN_A(String R89_COLUMN_A) {
		this.R89_COLUMN_A = R89_COLUMN_A;
	}

	public BigDecimal getR89_COLUMN_B() {
		return R89_COLUMN_B;
	}

	public void setR89_COLUMN_B(BigDecimal R89_COLUMN_B) {
		this.R89_COLUMN_B = R89_COLUMN_B;
	}

	public BigDecimal getR89_COLUMN_C() {
		return R89_COLUMN_C;
	}

	public void setR89_COLUMN_C(BigDecimal R89_COLUMN_C) {
		this.R89_COLUMN_C = R89_COLUMN_C;
	}

	public BigDecimal getR89_COLUMN_D() {
		return R89_COLUMN_D;
	}

	public void setR89_COLUMN_D(BigDecimal R89_COLUMN_D) {
		this.R89_COLUMN_D = R89_COLUMN_D;
	}

	public BigDecimal getR89_COLUMN_E() {
		return R89_COLUMN_E;
	}

	public void setR89_COLUMN_E(BigDecimal R89_COLUMN_E) {
		this.R89_COLUMN_E = R89_COLUMN_E;
	}

	public BigDecimal getR89_COLUMN_F() {
		return R89_COLUMN_F;
	}

	public void setR89_COLUMN_F(BigDecimal R89_COLUMN_F) {
		this.R89_COLUMN_F = R89_COLUMN_F;
	}

	public BigDecimal getR89_COLUMN_G() {
		return R89_COLUMN_G;
	}

	public void setR89_COLUMN_G(BigDecimal R89_COLUMN_G) {
		this.R89_COLUMN_G = R89_COLUMN_G;
	}

	public BigDecimal getR89_COLUMN_H() {
		return R89_COLUMN_H;
	}

	public void setR89_COLUMN_H(BigDecimal R89_COLUMN_H) {
		this.R89_COLUMN_H = R89_COLUMN_H;
	}

	public BigDecimal getR89_COLUMN_I() {
		return R89_COLUMN_I;
	}

	public void setR89_COLUMN_I(BigDecimal R89_COLUMN_I) {
		this.R89_COLUMN_I = R89_COLUMN_I;
	}

	public BigDecimal getR89_COLUMN_J() {
		return R89_COLUMN_J;
	}

	public void setR89_COLUMN_J(BigDecimal R89_COLUMN_J) {
		this.R89_COLUMN_J = R89_COLUMN_J;
	}

	public BigDecimal getR89_COLUMN_K() {
		return R89_COLUMN_K;
	}

	public void setR89_COLUMN_K(BigDecimal R89_COLUMN_K) {
		this.R89_COLUMN_K = R89_COLUMN_K;
	}

	public BigDecimal getR89_COLUMN_L() {
		return R89_COLUMN_L;
	}

	public void setR89_COLUMN_L(BigDecimal R89_COLUMN_L) {
		this.R89_COLUMN_L = R89_COLUMN_L;
	}

	public BigDecimal getR89_COLUMN_M() {
		return R89_COLUMN_M;
	}

	public void setR89_COLUMN_M(BigDecimal R89_COLUMN_M) {
		this.R89_COLUMN_M = R89_COLUMN_M;
	}

	public BigDecimal getR89_COLUMN_N() {
		return R89_COLUMN_N;
	}

	public void setR89_COLUMN_N(BigDecimal R89_COLUMN_N) {
		this.R89_COLUMN_N = R89_COLUMN_N;
	}

	public String getR90_COLUMN_A() {
		return R90_COLUMN_A;
	}

	public void setR90_COLUMN_A(String R90_COLUMN_A) {
		this.R90_COLUMN_A = R90_COLUMN_A;
	}

	public BigDecimal getR90_COLUMN_B() {
		return R90_COLUMN_B;
	}

	public void setR90_COLUMN_B(BigDecimal R90_COLUMN_B) {
		this.R90_COLUMN_B = R90_COLUMN_B;
	}

	public BigDecimal getR90_COLUMN_C() {
		return R90_COLUMN_C;
	}

	public void setR90_COLUMN_C(BigDecimal R90_COLUMN_C) {
		this.R90_COLUMN_C = R90_COLUMN_C;
	}

	public BigDecimal getR90_COLUMN_D() {
		return R90_COLUMN_D;
	}

	public void setR90_COLUMN_D(BigDecimal R90_COLUMN_D) {
		this.R90_COLUMN_D = R90_COLUMN_D;
	}

	public BigDecimal getR90_COLUMN_E() {
		return R90_COLUMN_E;
	}

	public void setR90_COLUMN_E(BigDecimal R90_COLUMN_E) {
		this.R90_COLUMN_E = R90_COLUMN_E;
	}

	public BigDecimal getR90_COLUMN_F() {
		return R90_COLUMN_F;
	}

	public void setR90_COLUMN_F(BigDecimal R90_COLUMN_F) {
		this.R90_COLUMN_F = R90_COLUMN_F;
	}

	public BigDecimal getR90_COLUMN_G() {
		return R90_COLUMN_G;
	}

	public void setR90_COLUMN_G(BigDecimal R90_COLUMN_G) {
		this.R90_COLUMN_G = R90_COLUMN_G;
	}

	public BigDecimal getR90_COLUMN_H() {
		return R90_COLUMN_H;
	}

	public void setR90_COLUMN_H(BigDecimal R90_COLUMN_H) {
		this.R90_COLUMN_H = R90_COLUMN_H;
	}

	public BigDecimal getR90_COLUMN_I() {
		return R90_COLUMN_I;
	}

	public void setR90_COLUMN_I(BigDecimal R90_COLUMN_I) {
		this.R90_COLUMN_I = R90_COLUMN_I;
	}

	public BigDecimal getR90_COLUMN_J() {
		return R90_COLUMN_J;
	}

	public void setR90_COLUMN_J(BigDecimal R90_COLUMN_J) {
		this.R90_COLUMN_J = R90_COLUMN_J;
	}

	public BigDecimal getR90_COLUMN_K() {
		return R90_COLUMN_K;
	}

	public void setR90_COLUMN_K(BigDecimal R90_COLUMN_K) {
		this.R90_COLUMN_K = R90_COLUMN_K;
	}

	public BigDecimal getR90_COLUMN_L() {
		return R90_COLUMN_L;
	}

	public void setR90_COLUMN_L(BigDecimal R90_COLUMN_L) {
		this.R90_COLUMN_L = R90_COLUMN_L;
	}

	public BigDecimal getR90_COLUMN_M() {
		return R90_COLUMN_M;
	}

	public void setR90_COLUMN_M(BigDecimal R90_COLUMN_M) {
		this.R90_COLUMN_M = R90_COLUMN_M;
	}

	public BigDecimal getR90_COLUMN_N() {
		return R90_COLUMN_N;
	}

	public void setR90_COLUMN_N(BigDecimal R90_COLUMN_N) {
		this.R90_COLUMN_N = R90_COLUMN_N;
	}

	public String getR91_COLUMN_A() {
		return R91_COLUMN_A;
	}

	public void setR91_COLUMN_A(String R91_COLUMN_A) {
		this.R91_COLUMN_A = R91_COLUMN_A;
	}

	public BigDecimal getR91_COLUMN_B() {
		return R91_COLUMN_B;
	}

	public void setR91_COLUMN_B(BigDecimal R91_COLUMN_B) {
		this.R91_COLUMN_B = R91_COLUMN_B;
	}

	public BigDecimal getR91_COLUMN_C() {
		return R91_COLUMN_C;
	}

	public void setR91_COLUMN_C(BigDecimal R91_COLUMN_C) {
		this.R91_COLUMN_C = R91_COLUMN_C;
	}

	public BigDecimal getR91_COLUMN_D() {
		return R91_COLUMN_D;
	}

	public void setR91_COLUMN_D(BigDecimal R91_COLUMN_D) {
		this.R91_COLUMN_D = R91_COLUMN_D;
	}

	public BigDecimal getR91_COLUMN_E() {
		return R91_COLUMN_E;
	}

	public void setR91_COLUMN_E(BigDecimal R91_COLUMN_E) {
		this.R91_COLUMN_E = R91_COLUMN_E;
	}

	public BigDecimal getR91_COLUMN_F() {
		return R91_COLUMN_F;
	}

	public void setR91_COLUMN_F(BigDecimal R91_COLUMN_F) {
		this.R91_COLUMN_F = R91_COLUMN_F;
	}

	public BigDecimal getR91_COLUMN_G() {
		return R91_COLUMN_G;
	}

	public void setR91_COLUMN_G(BigDecimal R91_COLUMN_G) {
		this.R91_COLUMN_G = R91_COLUMN_G;
	}

	public BigDecimal getR91_COLUMN_H() {
		return R91_COLUMN_H;
	}

	public void setR91_COLUMN_H(BigDecimal R91_COLUMN_H) {
		this.R91_COLUMN_H = R91_COLUMN_H;
	}

	public BigDecimal getR91_COLUMN_I() {
		return R91_COLUMN_I;
	}

	public void setR91_COLUMN_I(BigDecimal R91_COLUMN_I) {
		this.R91_COLUMN_I = R91_COLUMN_I;
	}

	public BigDecimal getR91_COLUMN_J() {
		return R91_COLUMN_J;
	}

	public void setR91_COLUMN_J(BigDecimal R91_COLUMN_J) {
		this.R91_COLUMN_J = R91_COLUMN_J;
	}

	public BigDecimal getR91_COLUMN_K() {
		return R91_COLUMN_K;
	}

	public void setR91_COLUMN_K(BigDecimal R91_COLUMN_K) {
		this.R91_COLUMN_K = R91_COLUMN_K;
	}

	public BigDecimal getR91_COLUMN_L() {
		return R91_COLUMN_L;
	}

	public void setR91_COLUMN_L(BigDecimal R91_COLUMN_L) {
		this.R91_COLUMN_L = R91_COLUMN_L;
	}

	public BigDecimal getR91_COLUMN_M() {
		return R91_COLUMN_M;
	}

	public void setR91_COLUMN_M(BigDecimal R91_COLUMN_M) {
		this.R91_COLUMN_M = R91_COLUMN_M;
	}

	public BigDecimal getR91_COLUMN_N() {
		return R91_COLUMN_N;
	}

	public void setR91_COLUMN_N(BigDecimal R91_COLUMN_N) {
		this.R91_COLUMN_N = R91_COLUMN_N;
	}

	public String getR92_COLUMN_A() {
		return R92_COLUMN_A;
	}

	public void setR92_COLUMN_A(String R92_COLUMN_A) {
		this.R92_COLUMN_A = R92_COLUMN_A;
	}

	public BigDecimal getR92_COLUMN_B() {
		return R92_COLUMN_B;
	}

	public void setR92_COLUMN_B(BigDecimal R92_COLUMN_B) {
		this.R92_COLUMN_B = R92_COLUMN_B;
	}

	public BigDecimal getR92_COLUMN_C() {
		return R92_COLUMN_C;
	}

	public void setR92_COLUMN_C(BigDecimal R92_COLUMN_C) {
		this.R92_COLUMN_C = R92_COLUMN_C;
	}

	public BigDecimal getR92_COLUMN_D() {
		return R92_COLUMN_D;
	}

	public void setR92_COLUMN_D(BigDecimal R92_COLUMN_D) {
		this.R92_COLUMN_D = R92_COLUMN_D;
	}

	public BigDecimal getR92_COLUMN_E() {
		return R92_COLUMN_E;
	}

	public void setR92_COLUMN_E(BigDecimal R92_COLUMN_E) {
		this.R92_COLUMN_E = R92_COLUMN_E;
	}

	public BigDecimal getR92_COLUMN_F() {
		return R92_COLUMN_F;
	}

	public void setR92_COLUMN_F(BigDecimal R92_COLUMN_F) {
		this.R92_COLUMN_F = R92_COLUMN_F;
	}

	public BigDecimal getR92_COLUMN_G() {
		return R92_COLUMN_G;
	}

	public void setR92_COLUMN_G(BigDecimal R92_COLUMN_G) {
		this.R92_COLUMN_G = R92_COLUMN_G;
	}

	public BigDecimal getR92_COLUMN_H() {
		return R92_COLUMN_H;
	}

	public void setR92_COLUMN_H(BigDecimal R92_COLUMN_H) {
		this.R92_COLUMN_H = R92_COLUMN_H;
	}

	public BigDecimal getR92_COLUMN_I() {
		return R92_COLUMN_I;
	}

	public void setR92_COLUMN_I(BigDecimal R92_COLUMN_I) {
		this.R92_COLUMN_I = R92_COLUMN_I;
	}

	public BigDecimal getR92_COLUMN_J() {
		return R92_COLUMN_J;
	}

	public void setR92_COLUMN_J(BigDecimal R92_COLUMN_J) {
		this.R92_COLUMN_J = R92_COLUMN_J;
	}

	public BigDecimal getR92_COLUMN_K() {
		return R92_COLUMN_K;
	}

	public void setR92_COLUMN_K(BigDecimal R92_COLUMN_K) {
		this.R92_COLUMN_K = R92_COLUMN_K;
	}

	public BigDecimal getR92_COLUMN_L() {
		return R92_COLUMN_L;
	}

	public void setR92_COLUMN_L(BigDecimal R92_COLUMN_L) {
		this.R92_COLUMN_L = R92_COLUMN_L;
	}

	public BigDecimal getR92_COLUMN_M() {
		return R92_COLUMN_M;
	}

	public void setR92_COLUMN_M(BigDecimal R92_COLUMN_M) {
		this.R92_COLUMN_M = R92_COLUMN_M;
	}

	public BigDecimal getR92_COLUMN_N() {
		return R92_COLUMN_N;
	}

	public void setR92_COLUMN_N(BigDecimal R92_COLUMN_N) {
		this.R92_COLUMN_N = R92_COLUMN_N;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public BigDecimal getREPORT_VERSION() {
		return REPORT_VERSION;
	}

	public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
		this.REPORT_VERSION = REPORT_VERSION;
	}

	public String getREPORT_FREQUENCY() {
		return REPORT_FREQUENCY;
	}

	public void setREPORT_FREQUENCY(String REPORT_FREQUENCY) {
		this.REPORT_FREQUENCY = REPORT_FREQUENCY;
	}

	public String getREPORT_CODE() {
		return REPORT_CODE;
	}

	public void setREPORT_CODE(String REPORT_CODE) {
		this.REPORT_CODE = REPORT_CODE;
	}

	public String getREPORT_DESC() {
		return REPORT_DESC;
	}

	public void setREPORT_DESC(String REPORT_DESC) {
		this.REPORT_DESC = REPORT_DESC;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public SLS_WORKING_Archival_Summary_Entity2() {
		super();
	}
	}

	public static class SLS_WORKING_Detail_Entity {

	private String CUST_ID;
	private String ACCT_NUMBER;
	private String ACCT_NAME;
	private String DATA_TYPE;
	private String ROW_ID;
	private String COLUMN_ID;
	private String REPORT_REMARKS;
	private String MODIFICATION_REMARKS;
	private String DATA_ENTRY_VERSION;
	private BigDecimal ACCT_BALANCE_IN_PULA;
	private Date REPORT_DATE;
	private String REPORT_NAME;
	private String CREATE_USER;
	private Date CREATE_TIME;
	private String MODIFY_USER;
	private Date MODIFY_TIME;
	private String VERIFY_USER;
	private Date VERIFY_TIME;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	private String SEGMENT;
	private String PAST_DUE;
	private BigDecimal PROVISION;
	private BigDecimal SNO;
	private String REPORT_LABEL;
	private String REPORT_ADDL_CRITERIA_1;
	private String REPORT_ADDL_CRITERIA_2;
	private String REPORT_ADDL_CRITERIA_3;

	public String getCUST_ID() {
		return CUST_ID;
	}

	public void setCUST_ID(String CUST_ID) {
		this.CUST_ID = CUST_ID;
	}

	public String getACCT_NUMBER() {
		return ACCT_NUMBER;
	}

	public void setACCT_NUMBER(String ACCT_NUMBER) {
		this.ACCT_NUMBER = ACCT_NUMBER;
	}

	public String getACCT_NAME() {
		return ACCT_NAME;
	}

	public void setACCT_NAME(String ACCT_NAME) {
		this.ACCT_NAME = ACCT_NAME;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String DATA_TYPE) {
		this.DATA_TYPE = DATA_TYPE;
	}

	public String getROW_ID() {
		return ROW_ID;
	}

	public void setROW_ID(String ROW_ID) {
		this.ROW_ID = ROW_ID;
	}

	public String getCOLUMN_ID() {
		return COLUMN_ID;
	}

	public void setCOLUMN_ID(String COLUMN_ID) {
		this.COLUMN_ID = COLUMN_ID;
	}

	public String getREPORT_REMARKS() {
		return REPORT_REMARKS;
	}

	public void setREPORT_REMARKS(String REPORT_REMARKS) {
		this.REPORT_REMARKS = REPORT_REMARKS;
	}

	public String getMODIFICATION_REMARKS() {
		return MODIFICATION_REMARKS;
	}

	public void setMODIFICATION_REMARKS(String MODIFICATION_REMARKS) {
		this.MODIFICATION_REMARKS = MODIFICATION_REMARKS;
	}

	public String getDATA_ENTRY_VERSION() {
		return DATA_ENTRY_VERSION;
	}

	public void setDATA_ENTRY_VERSION(String DATA_ENTRY_VERSION) {
		this.DATA_ENTRY_VERSION = DATA_ENTRY_VERSION;
	}

	public BigDecimal getACCT_BALANCE_IN_PULA() {
		return ACCT_BALANCE_IN_PULA;
	}

	public void setACCT_BALANCE_IN_PULA(BigDecimal ACCT_BALANCE_IN_PULA) {
		this.ACCT_BALANCE_IN_PULA = ACCT_BALANCE_IN_PULA;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public String getREPORT_NAME() {
		return REPORT_NAME;
	}

	public void setREPORT_NAME(String REPORT_NAME) {
		this.REPORT_NAME = REPORT_NAME;
	}

	public String getCREATE_USER() {
		return CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}

	public void setCREATE_TIME(Date CREATE_TIME) {
		this.CREATE_TIME = CREATE_TIME;
	}

	public String getMODIFY_USER() {
		return MODIFY_USER;
	}

	public void setMODIFY_USER(String MODIFY_USER) {
		this.MODIFY_USER = MODIFY_USER;
	}

	public Date getMODIFY_TIME() {
		return MODIFY_TIME;
	}

	public void setMODIFY_TIME(Date MODIFY_TIME) {
		this.MODIFY_TIME = MODIFY_TIME;
	}

	public String getVERIFY_USER() {
		return VERIFY_USER;
	}

	public void setVERIFY_USER(String VERIFY_USER) {
		this.VERIFY_USER = VERIFY_USER;
	}

	public Date getVERIFY_TIME() {
		return VERIFY_TIME;
	}

	public void setVERIFY_TIME(Date VERIFY_TIME) {
		this.VERIFY_TIME = VERIFY_TIME;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public String getSEGMENT() {
		return SEGMENT;
	}

	public void setSEGMENT(String SEGMENT) {
		this.SEGMENT = SEGMENT;
	}

	public String getPAST_DUE() {
		return PAST_DUE;
	}

	public void setPAST_DUE(String PAST_DUE) {
		this.PAST_DUE = PAST_DUE;
	}

	public BigDecimal getPROVISION() {
		return PROVISION;
	}

	public void setPROVISION(BigDecimal PROVISION) {
		this.PROVISION = PROVISION;
	}

	public BigDecimal getSNO() {
		return SNO;
	}

	public void setSNO(BigDecimal SNO) {
		this.SNO = SNO;
	}

	public String getREPORT_LABEL() {
		return REPORT_LABEL;
	}

	public void setREPORT_LABEL(String REPORT_LABEL) {
		this.REPORT_LABEL = REPORT_LABEL;
	}

	public String getREPORT_ADDL_CRITERIA_1() {
		return REPORT_ADDL_CRITERIA_1;
	}

	public void setREPORT_ADDL_CRITERIA_1(String REPORT_ADDL_CRITERIA_1) {
		this.REPORT_ADDL_CRITERIA_1 = REPORT_ADDL_CRITERIA_1;
	}

	public String getREPORT_ADDL_CRITERIA_2() {
		return REPORT_ADDL_CRITERIA_2;
	}

	public void setREPORT_ADDL_CRITERIA_2(String REPORT_ADDL_CRITERIA_2) {
		this.REPORT_ADDL_CRITERIA_2 = REPORT_ADDL_CRITERIA_2;
	}

	public String getREPORT_ADDL_CRITERIA_3() {
		return REPORT_ADDL_CRITERIA_3;
	}

	public void setREPORT_ADDL_CRITERIA_3(String REPORT_ADDL_CRITERIA_3) {
		this.REPORT_ADDL_CRITERIA_3 = REPORT_ADDL_CRITERIA_3;
	}

	public SLS_WORKING_Detail_Entity() {
		super();
	}
	}

	public static class SLS_WORKING_Archival_Detail_Entity {

	private String CUST_ID;
	private String ACCT_NUMBER;
	private String ACCT_NAME;
	private String DATA_TYPE;
	private String ROW_ID;
	private String COLUMN_ID;
	private String REPORT_REMARKS;
	private String MODIFICATION_REMARKS;
	private String DATA_ENTRY_VERSION;
	private BigDecimal ACCT_BALANCE_IN_PULA;
	private Date REPORT_DATE;
	private String REPORT_NAME;
	private String CREATE_USER;
	private Date CREATE_TIME;
	private String MODIFY_USER;
	private Date MODIFY_TIME;
	private String VERIFY_USER;
	private Date VERIFY_TIME;
	private String ENTITY_FLG;
	private String MODIFY_FLG;
	private String DEL_FLG;
	private String SEGMENT;
	private String PAST_DUE;
	private BigDecimal PROVISION;
	private BigDecimal SNO;
	private String REPORT_LABEL;
	private String REPORT_ADDL_CRITERIA_1;
	private String REPORT_ADDL_CRITERIA_2;
	private String REPORT_ADDL_CRITERIA_3;

	public String getCUST_ID() {
		return CUST_ID;
	}

	public void setCUST_ID(String CUST_ID) {
		this.CUST_ID = CUST_ID;
	}

	public String getACCT_NUMBER() {
		return ACCT_NUMBER;
	}

	public void setACCT_NUMBER(String ACCT_NUMBER) {
		this.ACCT_NUMBER = ACCT_NUMBER;
	}

	public String getACCT_NAME() {
		return ACCT_NAME;
	}

	public void setACCT_NAME(String ACCT_NAME) {
		this.ACCT_NAME = ACCT_NAME;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String DATA_TYPE) {
		this.DATA_TYPE = DATA_TYPE;
	}

	public String getROW_ID() {
		return ROW_ID;
	}

	public void setROW_ID(String ROW_ID) {
		this.ROW_ID = ROW_ID;
	}

	public String getCOLUMN_ID() {
		return COLUMN_ID;
	}

	public void setCOLUMN_ID(String COLUMN_ID) {
		this.COLUMN_ID = COLUMN_ID;
	}

	public String getREPORT_REMARKS() {
		return REPORT_REMARKS;
	}

	public void setREPORT_REMARKS(String REPORT_REMARKS) {
		this.REPORT_REMARKS = REPORT_REMARKS;
	}

	public String getMODIFICATION_REMARKS() {
		return MODIFICATION_REMARKS;
	}

	public void setMODIFICATION_REMARKS(String MODIFICATION_REMARKS) {
		this.MODIFICATION_REMARKS = MODIFICATION_REMARKS;
	}

	public String getDATA_ENTRY_VERSION() {
		return DATA_ENTRY_VERSION;
	}

	public void setDATA_ENTRY_VERSION(String DATA_ENTRY_VERSION) {
		this.DATA_ENTRY_VERSION = DATA_ENTRY_VERSION;
	}

	public BigDecimal getACCT_BALANCE_IN_PULA() {
		return ACCT_BALANCE_IN_PULA;
	}

	public void setACCT_BALANCE_IN_PULA(BigDecimal ACCT_BALANCE_IN_PULA) {
		this.ACCT_BALANCE_IN_PULA = ACCT_BALANCE_IN_PULA;
	}

	public Date getREPORT_DATE() {
		return REPORT_DATE;
	}

	public void setREPORT_DATE(Date REPORT_DATE) {
		this.REPORT_DATE = REPORT_DATE;
	}

	public String getREPORT_NAME() {
		return REPORT_NAME;
	}

	public void setREPORT_NAME(String REPORT_NAME) {
		this.REPORT_NAME = REPORT_NAME;
	}

	public String getCREATE_USER() {
		return CREATE_USER;
	}

	public void setCREATE_USER(String CREATE_USER) {
		this.CREATE_USER = CREATE_USER;
	}

	public Date getCREATE_TIME() {
		return CREATE_TIME;
	}

	public void setCREATE_TIME(Date CREATE_TIME) {
		this.CREATE_TIME = CREATE_TIME;
	}

	public String getMODIFY_USER() {
		return MODIFY_USER;
	}

	public void setMODIFY_USER(String MODIFY_USER) {
		this.MODIFY_USER = MODIFY_USER;
	}

	public Date getMODIFY_TIME() {
		return MODIFY_TIME;
	}

	public void setMODIFY_TIME(Date MODIFY_TIME) {
		this.MODIFY_TIME = MODIFY_TIME;
	}

	public String getVERIFY_USER() {
		return VERIFY_USER;
	}

	public void setVERIFY_USER(String VERIFY_USER) {
		this.VERIFY_USER = VERIFY_USER;
	}

	public Date getVERIFY_TIME() {
		return VERIFY_TIME;
	}

	public void setVERIFY_TIME(Date VERIFY_TIME) {
		this.VERIFY_TIME = VERIFY_TIME;
	}

	public String getENTITY_FLG() {
		return ENTITY_FLG;
	}

	public void setENTITY_FLG(String ENTITY_FLG) {
		this.ENTITY_FLG = ENTITY_FLG;
	}

	public String getMODIFY_FLG() {
		return MODIFY_FLG;
	}

	public void setMODIFY_FLG(String MODIFY_FLG) {
		this.MODIFY_FLG = MODIFY_FLG;
	}

	public String getDEL_FLG() {
		return DEL_FLG;
	}

	public void setDEL_FLG(String DEL_FLG) {
		this.DEL_FLG = DEL_FLG;
	}

	public String getSEGMENT() {
		return SEGMENT;
	}

	public void setSEGMENT(String SEGMENT) {
		this.SEGMENT = SEGMENT;
	}

	public String getPAST_DUE() {
		return PAST_DUE;
	}

	public void setPAST_DUE(String PAST_DUE) {
		this.PAST_DUE = PAST_DUE;
	}

	public BigDecimal getPROVISION() {
		return PROVISION;
	}

	public void setPROVISION(BigDecimal PROVISION) {
		this.PROVISION = PROVISION;
	}

	public BigDecimal getSNO() {
		return SNO;
	}

	public void setSNO(BigDecimal SNO) {
		this.SNO = SNO;
	}

	public String getREPORT_LABEL() {
		return REPORT_LABEL;
	}

	public void setREPORT_LABEL(String REPORT_LABEL) {
		this.REPORT_LABEL = REPORT_LABEL;
	}

	public String getREPORT_ADDL_CRITERIA_1() {
		return REPORT_ADDL_CRITERIA_1;
	}

	public void setREPORT_ADDL_CRITERIA_1(String REPORT_ADDL_CRITERIA_1) {
		this.REPORT_ADDL_CRITERIA_1 = REPORT_ADDL_CRITERIA_1;
	}

	public String getREPORT_ADDL_CRITERIA_2() {
		return REPORT_ADDL_CRITERIA_2;
	}

	public void setREPORT_ADDL_CRITERIA_2(String REPORT_ADDL_CRITERIA_2) {
		this.REPORT_ADDL_CRITERIA_2 = REPORT_ADDL_CRITERIA_2;
	}

	public String getREPORT_ADDL_CRITERIA_3() {
		return REPORT_ADDL_CRITERIA_3;
	}

	public void setREPORT_ADDL_CRITERIA_3(String REPORT_ADDL_CRITERIA_3) {
		this.REPORT_ADDL_CRITERIA_3 = REPORT_ADDL_CRITERIA_3;
	}

	public SLS_WORKING_Archival_Detail_Entity() {
		super();
	}
	}

}
