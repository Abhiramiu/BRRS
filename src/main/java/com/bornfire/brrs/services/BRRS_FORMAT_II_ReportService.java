package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
@Transactional
public class BRRS_FORMAT_II_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_II_ReportService.class);

	private static final int FIRST_ROW = 13;
	private static final int LAST_ROW = 28;
	private static final String[] AMT_FIELDS = { "amt", "amt_sub_add", "amt_sub_del", "amt_total" };

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// =========================================================
	// Common helpers
	// =========================================================

	/** Parses dd-MMM-yyyy, dd/MM/yyyy, dd-MM-yyyy and yyyy-MM-dd (strict). */
	private Date parseDate(String s) {
		if (s == null || s.trim().isEmpty()) {
			return null;
		}
		String[] patterns = { "dd-MMM-yyyy", "dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd" };
		for (String p : patterns) {
			try {
				SimpleDateFormat f = new SimpleDateFormat(p);
				f.setLenient(false);
				return f.parse(s.trim());
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	private BigDecimal toBigDecimal(String s) {
		try {
			return (s == null || s.trim().isEmpty()) ? null : new BigDecimal(s.trim());
		} catch (Exception e) {
			return null;
		}
	}

	/** R13_PRODUCT, R13_AMT, R13_AMT_SUB_ADD, ... for every row (with trailing ", "). */
	private String summaryColumns() {
		StringBuilder sb = new StringBuilder();
		for (int r = FIRST_ROW; r <= LAST_ROW; r++) {
			sb.append("R").append(r).append("_PRODUCT, ");
			for (String f : AMT_FIELDS) {
				sb.append("R").append(r).append("_").append(f.toUpperCase()).append(", ");
			}
		}
		return sb.toString();
	}

	private BigDecimal getAmt(Object record, int row, String field) {
		try {
			Method m = record.getClass().getMethod("getR" + row + "_" + field);
			return (BigDecimal) m.invoke(record);
		} catch (Exception e) {
			return null;
		}
	}

	// =========================================================
	// SUMMARY queries
	// =========================================================

	// ─── Current summary by date ───────────────────────────────────────────────
	public List<FORMAT_II_Summary_Entity> getDataByDate(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new FORMAT_II_RowMapper());
	}

	// ─── Archival summary by date and version ──────────────────────────────────
	public List<FORMAT_II_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate,
			BigDecimal reportVersion) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?",
				new Object[] { reportDate, reportVersion }, new FORMAT_II_Archival_Summary_RowMapper());
	}

	// ─── All archival summaries with version ───────────────────────────────────
	public List<FORMAT_II_Archival_Summary_Entity> getDataByDateListWithVersion() {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_DATE DESC, REPORT_VERSION DESC",
				new FORMAT_II_Archival_Summary_RowMapper());
	}

	// ─── Resub summary (archival table) by date and version ────────────────────
	public List<FORMAT_II_Summary_Entity> get_ResubSummaryByDate(Date reportDate, BigDecimal reportVersion) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE)=TRUNC(?) AND REPORT_VERSION=?",
				new Object[] { reportDate, reportVersion }, new FORMAT_II_RowMapper());
	}

	// ─── Check if version is the highest ───────────────────────────────────────
	public String getishighestversion(Date reportDate, BigDecimal reportVersion) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.queryForObject(sql, new Object[] { reportVersion, reportDate }, String.class);
	}

	// ─── Archival list (date + version) ────────────────────────────────────────
	public List<Object[]> getFORMAT_II_Archival() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// =========================================================
	// DETAIL queries
	// =========================================================

	// ─── Current detail by date ────────────────────────────────────────────────
	public List<FORMAT_II_Detail_Entity> getDetaildatabydateList(Date reportDate) {
		return jdbcTemplate.query("SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new FORMAT_II_Detail_RowMapper());
	}

	public List<FORMAT_II_Detail_Entity> getDetaildatabydateList(Date reportDate, int offset, int limit) {
		String sql = "SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?) OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { reportDate, offset, limit }, new FORMAT_II_Detail_RowMapper());
	}

	public int getDataCount(Date reportDate) {
		return jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM BRRS_FORMAT_II_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, Integer.class);
	}

	// ─── Current detail by label and criteria ──────────────────────────────────
	public List<FORMAT_II_Detail_Entity> getdetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportLabel, reportAddlCriteria1, reportDate }, new FORMAT_II_Detail_RowMapper());
	}

	// ─── Archival detail by date (archival table has the same columns) ─────────
	public List<FORMAT_II_Detail_Entity> getarchivaldetaildatabydateList(Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportDate }, new FORMAT_II_Detail_RowMapper());
	}

	// ─── Archival detail by label and criteria ─────────────────────────────────
	public List<FORMAT_II_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportDate) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
				new Object[] { reportLabel, reportAddlCriteria1, reportDate }, new FORMAT_II_Detail_RowMapper());
	}

	// ─── Find current detail by ACCT_NUMBER ────────────────────────────────────
	public FORMAT_II_Detail_Entity findByDetailAcctnumber(String acctNumber) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		List<FORMAT_II_Detail_Entity> list = jdbcTemplate.query(
				"SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE ACCT_NUMBER = ?", new Object[] { acctNumber.trim() },
				new FORMAT_II_Detail_RowMapper());
		return list.isEmpty() ? null : list.get(0);
	}

	// ─── Find current detail by ACCT_NUMBER and date ───────────────────────────
	public FORMAT_II_Detail_Entity findByAcctNumberAndDate(String acctNumber, Date reportDate) {
		if (acctNumber == null || acctNumber.trim().isEmpty() || reportDate == null) {
			return null;
		}
		try {
			List<FORMAT_II_Detail_Entity> list = jdbcTemplate.query(
					"SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
					new Object[] { acctNumber.trim(), reportDate }, new FORMAT_II_Detail_RowMapper());
			return list.isEmpty() ? null : list.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	// ─── Find archival detail by ACCT_NUMBER (latest date) ─────────────────────
	public FORMAT_II_Detail_Entity findBySnoArch(String acctNumber) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		try {
			List<FORMAT_II_Detail_Entity> list = jdbcTemplate.query(
					"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? ORDER BY REPORT_DATE DESC",
					new Object[] { acctNumber.trim() }, new FORMAT_II_Detail_RowMapper());
			return list.isEmpty() ? null : list.get(0);
		} catch (Exception e) {
			return null;
		}
	}

	// ─── Find archival detail by ACCT_NUMBER and date ──────────────────────────
	public FORMAT_II_Detail_Entity findByAcctNumberArch(String acctNumber, Date reportDate) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		if (reportDate != null) {
			try {
				List<FORMAT_II_Detail_Entity> list = jdbcTemplate.query(
						"SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
						new Object[] { acctNumber.trim(), reportDate }, new FORMAT_II_Detail_RowMapper());
				if (!list.isEmpty()) {
					return list.get(0);
				}
			} catch (Exception ignored) {
			}
			return null;
		}
		return findBySnoArch(acctNumber);
	}

	// =========================================================
	// Main report view (summary)
	// =========================================================
	public ModelAndView getFORMAT_IIView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("User Id Maker and Checker: " + userid + ", Role: " + role);

		System.out.println("FORMAT_II View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		Date dt = parseDate(todate != null ? todate : fromdate);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<FORMAT_II_Archival_Summary_Entity> T1Master = new ArrayList<>();
			try {
				T1Master = getDataByDateListArchival(dt, version);
				System.out.println(type + " Summary size = " + T1Master.size());
				mv.addObject("report_date", dateformat.format(dt));
				mv.addObject("REPORT_DATE", dateformat.format(dt));
				mv.addObject("allowdetail", getishighestversion(dt, version));
			} catch (Exception e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}
		// NORMAL MODE
		else {
			List<FORMAT_II_Summary_Entity> T1Master = new ArrayList<>();
			try {
				T1Master = getDataByDate(dt);
				System.out.println("Summary size = " + T1Master.size());
				mv.addObject("report_date", dateformat.format(dt));
				mv.addObject("REPORT_DATE", dateformat.format(dt));
			} catch (Exception e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
		}

		String formattedDdmmyyyy = dt != null ? new SimpleDateFormat("dd/MM/yyyy").format(dt) : todate;

		mv.setViewName("BRRS/FORMAT_II");
		mv.addObject("displaymode", "summary");
		mv.addObject("reportId", reportId);
		mv.addObject("currency", currency);
		mv.addObject("type", type);
		mv.addObject("version", version);
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		System.out.println("View Loaded: " + mv.getViewName());
		return mv;
	}

	// =========================================================
	// Detail view
	// =========================================================
	public ModelAndView getFORMAT_IIcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("User Id Maker and Checker: " + userid + ", Role: " + role);

		Date parsedDate = null;

		try {
			parsedDate = parseDate(todate);
			if (parsedDate == null) {
				parsedDate = parseDate(fromdate);
			}

			String reportLabel = null;
			String reportAddlCriteria1 = null;

			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					reportLabel = parts[0];
					reportAddlCriteria1 = parts[1];
				}
			}

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<FORMAT_II_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {
					detailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
				} else {
					detailList = getarchivaldetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				BigDecimal verDecimal = toBigDecimal(version);
				mv.addObject("allowdetail",
						verDecimal != null ? getishighestversion(parsedDate, verDecimal) : "NO");

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}
			// CURRENT MODE
			else {

				List<FORMAT_II_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {
					currentDetailList = getdetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1,
							parsedDate);
				} else {
					currentDetailList = getDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", currentDetailList);
				mv.addObject("reportmaster12", currentDetailList);

				System.out.println("CURRENT DETAIL COUNT: " + currentDetailList.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", e.getMessage());
		}

		String formattedDdmmyyyy = parsedDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(parsedDate)
				: todate;

		mv.setViewName("BRRS/FORMAT_II");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);
		mv.addObject("type", type);
		mv.addObject("version", version);
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		return mv;
	}

	// ─── Archival report list (date, version, resub date) ──────────────────────
	public List<Object[]> getFORMAT_IIArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<FORMAT_II_Archival_Summary_Entity> repoData = getDataByDateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (FORMAT_II_Archival_Summary_Entity entity : repoData) {
					archivalList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + archivalList.size() + " archival records");
				System.out.println("Latest archival version: " + repoData.get(0).getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching FORMAT_II Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ─── Resub report list ─────────────────────────────────────────────────────
	public List<Object[]> getFORMAT_IIResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<FORMAT_II_Archival_Summary_Entity> latestArchivalList = getDataByDateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (FORMAT_II_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching FORMAT_II Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// =========================================================
	// Update summary (manual fields: R21_AMT and R25_AMT_SUB_DEL)
	// =========================================================
	@Transactional
	public ResponseEntity<?> updateReport(FORMAT_II_Summary_Entity updatedEntity) {
		return updateReport(updatedEntity, null, null, null);
	}

	@Transactional
	public ResponseEntity<?> updateReport(FORMAT_II_Summary_Entity updatedEntity, String type, String version,
			String entry) {
		try {
			boolean isResub = "RESUB".equalsIgnoreCase(type);
			Date reportDate = updatedEntity.getReport_date();
			if (reportDate == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report Date is required.");
			}
			java.sql.Date sqlDate = new java.sql.Date(reportDate.getTime());
			String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);

			System.out.println("Updating FORMAT_II Summary: type=" + type + ", version=" + version + ", entry="
					+ entry + ", date=" + formattedDate);

			// =========================================
			// FETCH EXISTING RECORD
			// =========================================
			BigDecimal ver = toBigDecimal(version);
			if (ver == null) {
				ver = updatedEntity.getReport_version();
			}

			FORMAT_II_Summary_Entity existing;

			if (isResub) {
				List<FORMAT_II_Summary_Entity> list = null;
				if (ver != null) {
					list = get_ResubSummaryByDate(reportDate, ver);
				}
				if (list == null || list.isEmpty()) {
					Integer maxV = jdbcTemplate.queryForObject(
							"SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
							Integer.class, sqlDate);
					ver = BigDecimal.valueOf(maxV != null ? maxV : 0);
					list = get_ResubSummaryByDate(reportDate, ver);
				}
				if (list.isEmpty()) {
					existing = new FORMAT_II_Summary_Entity();
					existing.setReport_date(reportDate);
					existing.setReport_version(ver);
				} else {
					existing = list.get(0);
				}
			} else {
				List<FORMAT_II_Summary_Entity> list = getDataByDate(reportDate);
				if (list.isEmpty()) {
					existing = new FORMAT_II_Summary_Entity();
					existing.setReport_date(reportDate);
				} else {
					existing = list.get(0);
				}
			}

			// =========================================
			// AUDIT OLD COPY
			// =========================================
			FORMAT_II_Summary_Entity oldcopy = new FORMAT_II_Summary_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			// =========================================
			// ALLOWED (MANUAL) FIELDS
			// =========================================
			String[][] editable = { { "21", "amt" }, { "25", "amt_sub_del" } };

			for (String[] e : editable) {
				String getterName = "getR" + e[0] + "_" + e[1];
				String setterName = "setR" + e[0] + "_" + e[1];
				try {
					Method getter = FORMAT_II_Summary_Entity.class.getMethod(getterName);
					Method setter = FORMAT_II_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
					Object newValue = getter.invoke(updatedEntity);
					Object oldValue = getter.invoke(existing);

					if (newValue != null && !newValue.equals(oldValue)) {
						setter.invoke(existing, newValue);
						isChanged = true;
					}
				} catch (NoSuchMethodException ex) {
					continue;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// METADATA
			if (updatedEntity.getReport_version() != null) {
				existing.setReport_version(updatedEntity.getReport_version());
			}
			if (updatedEntity.getReport_frequency() != null) {
				existing.setReport_frequency(updatedEntity.getReport_frequency());
			}
			if (updatedEntity.getReport_code() != null) {
				existing.setReport_code(updatedEntity.getReport_code());
			}
			if (updatedEntity.getReport_desc() != null) {
				existing.setReport_desc(updatedEntity.getReport_desc());
			}
			if (updatedEntity.getEntity_flg() != null) {
				existing.setEntity_flg(updatedEntity.getEntity_flg());
			}
			if (updatedEntity.getModify_flg() != null) {
				existing.setModify_flg(updatedEntity.getModify_flg());
			}
			if (updatedEntity.getDel_flg() != null) {
				existing.setDel_flg(updatedEntity.getDel_flg());
			}

			// SAVE CHANGES
			boolean isResubNoEntry = isResub && "NO".equalsIgnoreCase(entry);

			if (isChanged) {
				if (isResub) {
					if (isResubNoEntry) {
						// REGENERATE: do NOT overwrite the old version in archival summary.
						jdbcTemplate.update("DELETE FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
								sqlDate);

						String columnsPart = summaryColumns();

						// Copy base archival row to live summary
						String copyToLiveSql = "INSERT INTO BRRS_FORMAT_II_SUMMARYTABLE (" + columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
								+ "SELECT " + columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
								+ "FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						jdbcTemplate.update(copyToLiveSql, sqlDate, ver);

						// Apply modified manual values so the procedure uses them
						jdbcTemplate.update(
								"UPDATE BRRS_FORMAT_II_SUMMARYTABLE SET R21_AMT=?, R25_AMT_SUB_DEL=? WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
								existing.getR21_amt(), existing.getR25_amt_sub_del(), sqlDate);

						auditService.compareEntitiesmanual(oldcopy, existing, reportDate.toString(),
								"FORMAT II Archival Summary Screen", "BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY");

						System.out.println("Modified values staged into live summary. Old version " + ver
								+ " preserved in archival summary.");

						Run_FORMAT_II_Procedure(formattedDate, "RESUB", "NO");
						return ResponseEntity.ok("Record updated and Report Regenerated successfully!");

					} else {
						// MODIFY ONLY: update current version in archival summary
						jdbcTemplate.update(
								"UPDATE BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY SET R21_AMT=?, R25_AMT_SUB_DEL=? "
										+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?) AND REPORT_VERSION=?",
								existing.getR21_amt(), existing.getR25_amt_sub_del(), sqlDate, ver);

						auditService.compareEntitiesmanual(oldcopy, existing, reportDate.toString(),
								"FORMAT II Archival Summary Screen", "BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY");

						System.out.println("FORMAT_II Archival Summary updated successfully for version " + ver);
						return ResponseEntity.ok("Record updated successfully!");
					}

				} else {
					jdbcTemplate.update(
							"UPDATE BRRS_FORMAT_II_SUMMARYTABLE SET R21_AMT=?, R25_AMT_SUB_DEL=?, "
									+ "REPORT_VERSION=?, REPORT_FREQUENCY=?, REPORT_CODE=?, REPORT_DESC=?, ENTITY_FLG=?, MODIFY_FLG=?, DEL_FLG=? "
									+ "WHERE TRUNC(REPORT_DATE)=TRUNC(?)",
							existing.getR21_amt(), existing.getR25_amt_sub_del(), existing.getReport_version(),
							existing.getReport_frequency(), existing.getReport_code(), existing.getReport_desc(),
							existing.getEntity_flg(), existing.getModify_flg(), existing.getDel_flg(), sqlDate);

					auditService.compareEntitiesmanual(oldcopy, existing, reportDate.toString(),
							"FORMAT II Summary Screen", "BRRS_FORMAT_II_SUMMARYTABLE");

					System.out.println("FORMAT_II Summary updated successfully");
					return ResponseEntity.ok("FORMAT_II Summary updated successfully");
				}
			} else {
				if (isResubNoEntry) {
					Run_FORMAT_II_Procedure(formattedDate, "RESUB", "NO");
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("No changes detected");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating FORMAT_II Summary : " + e.getMessage());
		}
	}

	// =========================================================
	// View / edit detail page
	// =========================================================
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		return getViewOrEditPage(acctNo, formMode, null, null);
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode, String type) {
		return getViewOrEditPage(acctNo, formMode, type, null);
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode, String type, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_II");

		Date reportDate = null;
		String reqAcctNo = null;
		if (request != null) {
			String asondateParam = request.getParameter("asondate");
			if (asondateParam == null) {
				asondateParam = request.getParameter("reportDate");
			}
			if (asondateParam == null) {
				asondateParam = request.getParameter("todate");
			}
			reportDate = parseDate(asondateParam);
			reqAcctNo = request.getParameter("acctNo");
		}
		String effectiveAcct = (reqAcctNo != null && !reqAcctNo.trim().isEmpty()) ? reqAcctNo : acctNo;

		System.out.println("getViewOrEditPage: acctNo=" + effectiveAcct + ", reportDate=" + reportDate + ", type=" + type);

		FORMAT_II_Detail_Entity entity = null;

		if ("RESUB".equals(type) || "ARCHIVAL".equals(type)) {
			entity = findByAcctNumberArch(effectiveAcct, reportDate);
			if (entity == null && acctNo != null) {
				entity = findByAcctNumberArch(acctNo, reportDate);
			}
		} else {
			entity = findByAcctNumberAndDate(effectiveAcct, reportDate);
			if (entity == null) {
				entity = findByDetailAcctnumber(acctNo);
			}
		}

		if (entity != null) {
			if (reportDate != null && (entity.getReportDate() == null || !entity.getReportDate().equals(reportDate))) {
				entity.setReportDate(reportDate);
			}
			Date d = entity.getReportDate() != null ? entity.getReportDate() : reportDate;
			if (d != null) {
				mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(d));
			}
		} else if (reportDate != null) {
			mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(reportDate));
		}

		mv.addObject("formate_IIData", entity);
		mv.addObject("reportId", "FORMAT_II");
		mv.addObject("reportid", "FORMAT_II");
		mv.addObject("menu", "FORMAT_II");
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	// =========================================================
	// Save detail edit
	// =========================================================
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNumber = request.getParameter("acctNumber");
			if (acctNumber == null) {
				acctNumber = request.getParameter("ACCT_NUMBER");
			}
			String acctBalanceInpula = request.getParameter("acctBalanceInpula");
			if (acctBalanceInpula == null) {
				acctBalanceInpula = request.getParameter("acctBalanceInPula");
			}
			String average = request.getParameter("average");
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");
			Date parsedReportDate = parseDate(reportDateStr);

			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			logger.info("Received update for ACCT_NO: {}, type: {}, entry: {}", acctNumber, type, entry);

			// Load existing record
			FORMAT_II_Detail_Entity existing;
			if ("RESUB".equals(type)) {
				existing = findByAcctNumberArch(acctNumber, parsedReportDate);
				if (existing == null) {
					existing = findBySnoArch(acctNumber);
				}
			} else {
				existing = findByAcctNumberAndDate(acctNumber, parsedReportDate);
				if (existing == null) {
					existing = findByDetailAcctnumber(acctNumber);
				}
			}

			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNumber);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Record not found for update (Account: " + acctNumber + ")");
			}

			if (existing.getReportDate() == null && parsedReportDate != null) {
				existing.setReportDate(parsedReportDate);
			}

			FORMAT_II_Detail_Entity oldcopy = new FORMAT_II_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
				}
			}

			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {
					existing.setAcctBalanceInpula(newBalance);
					isChanged = true;
				}
			}

			if (average != null && !average.isEmpty()) {
				BigDecimal newAverage = new BigDecimal(average);
				if (existing.getAverage() == null || existing.getAverage().compareTo(newAverage) != 0) {
					existing.setAverage(newAverage);
					isChanged = true;
				}
			}

			Date effectiveDate = existing.getReportDate() != null ? existing.getReportDate() : parsedReportDate;
			String dateForProc = (effectiveDate != null) ? new SimpleDateFormat("dd-MM-yyyy").format(effectiveDate)
					: reportDateStr;

			if (isChanged) {

				if ("RESUB".equals(type)) {

					int rowsUpdated;
					if (effectiveDate != null) {
						rowsUpdated = jdbcTemplate.update(
								"UPDATE BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ?, AVERAGE = ? "
										+ "WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
								existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
								existing.getAcctNumber(), effectiveDate);
					} else {
						rowsUpdated = jdbcTemplate.update(
								"UPDATE BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ?, AVERAGE = ? WHERE ACCT_NUMBER = ?",
								existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
								existing.getAcctNumber());
					}
					System.out.println("Archival rows updated: " + rowsUpdated);

					try {
						auditService.compareEntitiesmanual(oldcopy, existing, existing.getAcctNumber(),
								"FORMAT_II Archival Screen", "BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}

				} else {

					jdbcTemplate.update(
							"UPDATE BRRS_FORMAT_II_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ?, AVERAGE = ? WHERE ACCT_NUMBER = ?",
							existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
							existing.getAcctNumber());

					try {
						auditService.compareEntitiesmanual(oldcopy, existing, existing.getAcctNumber(),
								"FORMAT_II Detail Screen", "BRRS_FORMAT_II_DETAILTABLE");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}
				}

				System.out.println("Record updated using JDBC");

				Run_FORMAT_II_Procedure(dateForProc, type, entry);

				if ("RESUB".equals(type) && "NO".equals(entry)) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("Record updated successfully!");

			} else {

				if ("RESUB".equals(type) && "NO".equals(entry)) {
					Run_FORMAT_II_Procedure(dateForProc, type, entry);
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			logger.error("Error updating FORMAT_II record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// =========================================================
	// Trigger report regeneration
	// =========================================================
	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_FORMAT_II_Procedure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	private void Run_FORMAT_II_Procedure(String reportDateStr, String type, String entry) {
		Date parsedDate = parseDate(reportDateStr);
		if (parsedDate == null) {
			System.out.println("Error parsing date: " + reportDateStr + ". Post-commit logic aborted.");
			return;
		}

		final Date finalDate = parsedDate;
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					executeProcedureLogic(finalDate, type, entry);
				}
			});
		} else {
			executeProcedureLogic(finalDate, type, entry);
		}
	}

	private void transferArchivalDetailsToLive(java.sql.Date sqlDate) {
		try {
			Integer count = jdbcTemplate.queryForObject(
					"SELECT COUNT(*) FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
					Integer.class, sqlDate);
			if (count == null || count == 0) {
				System.out.println("No archival detail records found for " + sqlDate + "; skipping detail transfer.");
				return;
			}

			final Set<String> archCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE 1=0", rs -> {
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					archCols.add(md.getColumnName(i).toUpperCase());
				}
				return null;
			});

			final Set<String> liveCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_FORMAT_II_DETAILTABLE WHERE 1=0", rs -> {
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					liveCols.add(md.getColumnName(i).toUpperCase());
				}
				return null;
			});

			List<String> commonCols = new ArrayList<>();
			for (String col : archCols) {
				if (liveCols.contains(col) && !"SNO".equalsIgnoreCase(col)) {
					commonCols.add(col);
				}
			}

			if (commonCols.isEmpty()) {
				System.out.println("No common columns found between archival and live detail tables.");
				return;
			}

			int rowsDeleted = jdbcTemplate.update(
					"DELETE FROM BRRS_FORMAT_II_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
			System.out.println("Detail rows deleted before transfer: " + rowsDeleted);

			String colsJoined = String.join(", ", commonCols);
			String insertSql;
			if (liveCols.contains("SNO")) {
				insertSql = "INSERT INTO BRRS_FORMAT_II_DETAILTABLE (SNO, " + colsJoined + ") SELECT ROWNUM AS SNO, "
						+ colsJoined
						+ " FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			} else {
				insertSql = "INSERT INTO BRRS_FORMAT_II_DETAILTABLE (" + colsJoined + ") SELECT " + colsJoined
						+ " FROM BRRS_FORMAT_II_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			}
			int rowsTransferred = jdbcTemplate.update(insertSql, sqlDate);
			System.out.println("Detail rows transferred from archival: " + rowsTransferred);
			jdbcTemplate.update(
					"UPDATE BRRS_FORMAT_II_DETAILTABLE SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
					sqlDate);
		} catch (Exception e) {
			System.err.println("Warning in transferArchivalDetailsToLive: " + e.getMessage());
		}
	}

	private void executeProcedureLogic(Date reportDate, String type, String entry) {
		try {
			boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
			boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

			String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);
			java.sql.Date sqlDate = new java.sql.Date(reportDate.getTime());

			System.out.println("executeProcedureLogic: formattedDate = " + formattedDate + ", type = " + type
					+ ", entry = " + entry);

			String columnsPart = summaryColumns();

			if (isResubNoEntry) {
				// 1. Transfer archival detail records into live detail table
				transferArchivalDetailsToLive(sqlDate);

				// 2. Check if live summary already has staged modified values
				Integer liveSumCount = jdbcTemplate.queryForObject(
						"SELECT COUNT(*) FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						Integer.class, sqlDate);

				if (liveSumCount == null || liveSumCount == 0) {
					// Live summary empty (e.g. triggered from Details view) -> restore from latest archival
					Integer currentMaxVersion = jdbcTemplate.queryForObject(
							"SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
							Integer.class, sqlDate);

					if (currentMaxVersion != null && currentMaxVersion > 0) {
						String copyArchSummarySql = "INSERT INTO BRRS_FORMAT_II_SUMMARYTABLE (" + columnsPart
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
								+ "SELECT " + columnsPart
								+ "TRUNC(REPORT_DATE), REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG "
								+ "FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
						int sumCopied = jdbcTemplate.update(copyArchSummarySql, sqlDate, currentMaxVersion);
						System.out.println("Archival summary restored to live summary: " + sumCopied);
					}
				} else {
					System.out.println("Live summary already contains modified staged values; preserving for procedure.");
				}
				jdbcTemplate.update(
						"UPDATE BRRS_FORMAT_II_SUMMARYTABLE SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						sqlDate);
			}

			// Execute stored procedure
			if (shouldExecuteProcedure) {
				jdbcTemplate.update("BEGIN BRRS_FORMAT_II_SUMMARY_PROCEDURE(?); END;", formattedDate);
				System.out.println("BRRS_FORMAT_II_SUMMARY_PROCEDURE executed successfully for " + formattedDate);
			}

			// Post-execution archival handling
			if (isResubNoEntry) {
				// Delete detail records from live table
				int rowsDeletedAfter = jdbcTemplate.update(
						"DELETE FROM BRRS_FORMAT_II_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
				System.out.println("Live detail records deleted after procedure: " + rowsDeletedAfter);

				// Next version number
				Integer maxVersion = jdbcTemplate.queryForObject(
						"SELECT NVL(MAX(REPORT_VERSION), 0) FROM BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						Integer.class, sqlDate);
				int highestValue = (maxVersion != null ? maxVersion : 0) + 1;
				System.out.println("Archiving new summary with version: " + highestValue);

				// Insert new version into archival summary
				String archiveSummarySql = "INSERT INTO BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY (" + columnsPart
						+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
						+ "SELECT " + columnsPart
						+ "TRUNC(REPORT_DATE), ?, NVL(REPORT_FREQUENCY, 'MONTHLY'), NVL(REPORT_CODE, 'FORMAT_II'), NVL(REPORT_DESC, 'Format II'), NVL(ENTITY_FLG, 'N'), NVL(MODIFY_FLG, 'N'), NVL(DEL_FLG, 'N'), SYSDATE "
						+ "FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				int rowsArchived = jdbcTemplate.update(archiveSummarySql, highestValue, sqlDate);
				System.out.println("Summary rows archived: " + rowsArchived);

				// Delete temporary live summary records
				int rowsDeletedSum = jdbcTemplate.update(
						"DELETE FROM BRRS_FORMAT_II_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
				System.out.println("Live summary rows deleted after archiving: " + rowsDeletedSum);
			}
		} catch (Exception e) {
			System.err.println("Error in executeProcedureLogic: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// =========================================================
	// Detail Excel download
	// =========================================================
	public byte[] getFORMAT_IIDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_II Details...");

			boolean archival = ("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null;

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_II Details");

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

			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "AVERAGE", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle((i == 3 || i == 4) ? rightAlignedHeaderStyle : headerStyle);
				sheet.setColumnWidth(i, 5000);
			}

			Date parsedToDate = parseDate(todate);
			List<FORMAT_II_Detail_Entity> reportData = archival ? getarchivaldetaildatabydateList(parsedToDate)
					: getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_II_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					Cell balanceCell = row.createCell(3);
					balanceCell.setCellValue(item.getAcctBalanceInpula() != null
							? item.getAcctBalanceInpula().doubleValue() : 0);
					balanceCell.setCellStyle(balanceStyle);

					Cell avgCell = row.createCell(4);
					avgCell.setCellValue(item.getAverage() != null ? item.getAverage().doubleValue() : 0);
					avgCell.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(7).setCellValue(item.getReportDate() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate()) : "");

					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for FORMAT_II — only header will be written.");
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_II Excel", e);
			return new byte[0];
		}
	}

	// Kept for backward compatibility with existing controller calls
	public byte[] getFORMAT_IIDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		return getFORMAT_IIDetailExcel(filename, fromdate, todate, currency, dtltype, "ARCHIVAL",
				version != null ? version : "0");
	}

	// =========================================================
	// Summary Excel download
	// =========================================================
	public byte[] getFORMAT_IIExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory. FORMAT_II");

		// ARCHIVAL / RESUB check (resub versions live in the archival summary table)
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating {} report for version {}", type, version);
			return getExcelFORMAT_IIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		List<FORMAT_II_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		System.out.println("DATA SIZE IS : " + dataList.size());

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_II report. Returning empty result.");
			return new byte[0];
		}

		return buildSummaryExcel(filename, dataList.get(0), dataList.get(0).getReport_date(), "FORMAT_II SUMMARY",
				"BRRS_FORMAT_II_SUMMARYTABLE");
	}

	public byte[] getExcelFORMAT_IIARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory (archival).");

		List<FORMAT_II_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_II archival report. Returning empty result.");
			return new byte[0];
		}

		String auditName = "RESUB".equalsIgnoreCase(type) ? "FORMAT_II RESUB SUMMARY" : "FORMAT_II ARCHIVAL SUMMARY";
		return buildSummaryExcel(filename, dataList.get(0), dataList.get(0).getReport_date(), auditName,
				"BRRS_FORMAT_II_ARCHIVALTABLE_SUMMARY");
	}

	/** Shared template filler for current / archival / resub summaries. */
	private byte[] buildSummaryExcel(String filename, Object record, Date reportDate, String auditName,
			String auditTable) throws Exception {

		String templateDir = env.getProperty("output.exportpathtemp");
		Path templatePath = Paths.get(templateDir, filename);
		System.out.println(templatePath);

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

			// --- Style Definitions ---
			CreationHelper createHelper = workbook.getCreationHelper();

			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
			dateStyle.setBorderBottom(BorderStyle.THIN);
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);

			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setBorderBottom(BorderStyle.THIN);
			textStyle.setBorderTop(BorderStyle.THIN);
			textStyle.setBorderLeft(BorderStyle.THIN);
			textStyle.setBorderRight(BorderStyle.THIN);

			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8);
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			// Rows 13..28 -> Excel columns E..H (amt, amt_sub_add, amt_sub_del, amt_total)
			for (int r = FIRST_ROW; r <= LAST_ROW; r++) {
				Row row = sheet.getRow(r - 1);
				if (row == null) {
					row = sheet.createRow(r - 1);
				}
				for (int c = 0; c < AMT_FIELDS.length; c++) {
					BigDecimal v = getAmt(record, r, AMT_FIELDS[c]);
					Cell cell = row.createCell(4 + c);
					if (v != null) {
						cell.setCellValue(v.doubleValue());
						cell.setCellStyle(numberStyle);
					} else {
						cell.setCellValue("");
						cell.setCellStyle(textStyle);
					}
				}
			}

			// Row 34 / Column C = report date
			Row row34 = sheet.getRow(33);
			if (row34 == null) {
				row34 = sheet.createRow(33);
			}
			Cell cellBdate = row34.createCell(2);
			if (reportDate != null) {
				cellBdate.setCellValue(reportDate);
				cellBdate.setCellStyle(dateStyle);
			} else {
				cellBdate.setCellValue("");
				cellBdate.setCellStyle(textStyle);
			}

			workbook.setForceFormulaRecalculation(true);
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", auditName, null, auditTable);
			}

			return out.toByteArray();
		}
	}

	// =========================================================
	// RowMappers
	// =========================================================

	private void mapSummaryRows(ResultSet rs, FORMAT_II_Summary_Entity obj) throws SQLException {
		Class<?> c = FORMAT_II_Summary_Entity.class;
		try {
			for (int r = FIRST_ROW; r <= LAST_ROW; r++) {
				c.getMethod("setR" + r + "_product", String.class).invoke(obj, rs.getString("R" + r + "_PRODUCT"));
				for (String f : AMT_FIELDS) {
					c.getMethod("setR" + r + "_" + f, BigDecimal.class).invoke(obj,
							rs.getBigDecimal("R" + r + "_" + f.toUpperCase()));
				}
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new SQLException("Error mapping FORMAT_II summary row", e);
		}
		obj.setReport_date(rs.getDate("REPORT_DATE"));
		obj.setReport_version(rs.getBigDecimal("REPORT_VERSION"));
		obj.setReport_frequency(rs.getString("REPORT_FREQUENCY"));
		obj.setReport_code(rs.getString("REPORT_CODE"));
		obj.setReport_desc(rs.getString("REPORT_DESC"));
		obj.setEntity_flg(rs.getString("ENTITY_FLG"));
		obj.setModify_flg(rs.getString("MODIFY_FLG"));
		obj.setDel_flg(rs.getString("DEL_FLG"));
	}

	public class FORMAT_II_RowMapper implements RowMapper<FORMAT_II_Summary_Entity> {
		@Override
		public FORMAT_II_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			FORMAT_II_Summary_Entity obj = new FORMAT_II_Summary_Entity();
			mapSummaryRows(rs, obj);
			return obj;
		}
	}

	public class FORMAT_II_Archival_Summary_RowMapper implements RowMapper<FORMAT_II_Archival_Summary_Entity> {
		@Override
		public FORMAT_II_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			FORMAT_II_Archival_Summary_Entity obj = new FORMAT_II_Archival_Summary_Entity();
			mapSummaryRows(rs, obj);
			obj.setReportResubDate(rs.getDate("REPORT_RESUBDATE"));
			return obj;
		}
	}

	public class FORMAT_II_Detail_RowMapper implements RowMapper<FORMAT_II_Detail_Entity> {
		@Override
		public FORMAT_II_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			FORMAT_II_Detail_Entity obj = new FORMAT_II_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));

			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));

			obj.setGlshCode(rs.getString("GLSH_CODE"));
			obj.setGlCode(rs.getString("GL_CODE"));

			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getDate("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getDate("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getDate("VERIFY_TIME"));

			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			return obj;
		}
	}

	// =========================================================
	// Entities
	// =========================================================

	public static class FORMAT_II_Summary_Entity {

		private String r13_product; private BigDecimal r13_amt, r13_amt_sub_add, r13_amt_sub_del, r13_amt_total;
		private String r14_product; private BigDecimal r14_amt, r14_amt_sub_add, r14_amt_sub_del, r14_amt_total;
		private String r15_product; private BigDecimal r15_amt, r15_amt_sub_add, r15_amt_sub_del, r15_amt_total;
		private String r16_product; private BigDecimal r16_amt, r16_amt_sub_add, r16_amt_sub_del, r16_amt_total;
		private String r17_product; private BigDecimal r17_amt, r17_amt_sub_add, r17_amt_sub_del, r17_amt_total;
		private String r18_product; private BigDecimal r18_amt, r18_amt_sub_add, r18_amt_sub_del, r18_amt_total;
		private String r19_product; private BigDecimal r19_amt, r19_amt_sub_add, r19_amt_sub_del, r19_amt_total;
		private String r20_product; private BigDecimal r20_amt, r20_amt_sub_add, r20_amt_sub_del, r20_amt_total;
		private String r21_product; private BigDecimal r21_amt, r21_amt_sub_add, r21_amt_sub_del, r21_amt_total;
		private String r22_product; private BigDecimal r22_amt, r22_amt_sub_add, r22_amt_sub_del, r22_amt_total;
		private String r23_product; private BigDecimal r23_amt, r23_amt_sub_add, r23_amt_sub_del, r23_amt_total;
		private String r24_product; private BigDecimal r24_amt, r24_amt_sub_add, r24_amt_sub_del, r24_amt_total;
		private String r25_product; private BigDecimal r25_amt, r25_amt_sub_add, r25_amt_sub_del, r25_amt_total;
		private String r26_product; private BigDecimal r26_amt, r26_amt_sub_add, r26_amt_sub_del, r26_amt_total;
		private String r27_product; private BigDecimal r27_amt, r27_amt_sub_add, r27_amt_sub_del, r27_amt_total;
		private String r28_product; private BigDecimal r28_amt, r28_amt_sub_add, r28_amt_sub_del, r28_amt_total;

		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// ---- R13 ----
		public String getR13_product() { return r13_product; }
		public void setR13_product(String v) { this.r13_product = v; }
		public BigDecimal getR13_amt() { return r13_amt; }
		public void setR13_amt(BigDecimal v) { this.r13_amt = v; }
		public BigDecimal getR13_amt_sub_add() { return r13_amt_sub_add; }
		public void setR13_amt_sub_add(BigDecimal v) { this.r13_amt_sub_add = v; }
		public BigDecimal getR13_amt_sub_del() { return r13_amt_sub_del; }
		public void setR13_amt_sub_del(BigDecimal v) { this.r13_amt_sub_del = v; }
		public BigDecimal getR13_amt_total() { return r13_amt_total; }
		public void setR13_amt_total(BigDecimal v) { this.r13_amt_total = v; }
		// ---- R14 ----
		public String getR14_product() { return r14_product; }
		public void setR14_product(String v) { this.r14_product = v; }
		public BigDecimal getR14_amt() { return r14_amt; }
		public void setR14_amt(BigDecimal v) { this.r14_amt = v; }
		public BigDecimal getR14_amt_sub_add() { return r14_amt_sub_add; }
		public void setR14_amt_sub_add(BigDecimal v) { this.r14_amt_sub_add = v; }
		public BigDecimal getR14_amt_sub_del() { return r14_amt_sub_del; }
		public void setR14_amt_sub_del(BigDecimal v) { this.r14_amt_sub_del = v; }
		public BigDecimal getR14_amt_total() { return r14_amt_total; }
		public void setR14_amt_total(BigDecimal v) { this.r14_amt_total = v; }
		// ---- R15 ----
		public String getR15_product() { return r15_product; }
		public void setR15_product(String v) { this.r15_product = v; }
		public BigDecimal getR15_amt() { return r15_amt; }
		public void setR15_amt(BigDecimal v) { this.r15_amt = v; }
		public BigDecimal getR15_amt_sub_add() { return r15_amt_sub_add; }
		public void setR15_amt_sub_add(BigDecimal v) { this.r15_amt_sub_add = v; }
		public BigDecimal getR15_amt_sub_del() { return r15_amt_sub_del; }
		public void setR15_amt_sub_del(BigDecimal v) { this.r15_amt_sub_del = v; }
		public BigDecimal getR15_amt_total() { return r15_amt_total; }
		public void setR15_amt_total(BigDecimal v) { this.r15_amt_total = v; }
		// ---- R16 ----
		public String getR16_product() { return r16_product; }
		public void setR16_product(String v) { this.r16_product = v; }
		public BigDecimal getR16_amt() { return r16_amt; }
		public void setR16_amt(BigDecimal v) { this.r16_amt = v; }
		public BigDecimal getR16_amt_sub_add() { return r16_amt_sub_add; }
		public void setR16_amt_sub_add(BigDecimal v) { this.r16_amt_sub_add = v; }
		public BigDecimal getR16_amt_sub_del() { return r16_amt_sub_del; }
		public void setR16_amt_sub_del(BigDecimal v) { this.r16_amt_sub_del = v; }
		public BigDecimal getR16_amt_total() { return r16_amt_total; }
		public void setR16_amt_total(BigDecimal v) { this.r16_amt_total = v; }
		// ---- R17 ----
		public String getR17_product() { return r17_product; }
		public void setR17_product(String v) { this.r17_product = v; }
		public BigDecimal getR17_amt() { return r17_amt; }
		public void setR17_amt(BigDecimal v) { this.r17_amt = v; }
		public BigDecimal getR17_amt_sub_add() { return r17_amt_sub_add; }
		public void setR17_amt_sub_add(BigDecimal v) { this.r17_amt_sub_add = v; }
		public BigDecimal getR17_amt_sub_del() { return r17_amt_sub_del; }
		public void setR17_amt_sub_del(BigDecimal v) { this.r17_amt_sub_del = v; }
		public BigDecimal getR17_amt_total() { return r17_amt_total; }
		public void setR17_amt_total(BigDecimal v) { this.r17_amt_total = v; }
		// ---- R18 ----
		public String getR18_product() { return r18_product; }
		public void setR18_product(String v) { this.r18_product = v; }
		public BigDecimal getR18_amt() { return r18_amt; }
		public void setR18_amt(BigDecimal v) { this.r18_amt = v; }
		public BigDecimal getR18_amt_sub_add() { return r18_amt_sub_add; }
		public void setR18_amt_sub_add(BigDecimal v) { this.r18_amt_sub_add = v; }
		public BigDecimal getR18_amt_sub_del() { return r18_amt_sub_del; }
		public void setR18_amt_sub_del(BigDecimal v) { this.r18_amt_sub_del = v; }
		public BigDecimal getR18_amt_total() { return r18_amt_total; }
		public void setR18_amt_total(BigDecimal v) { this.r18_amt_total = v; }
		// ---- R19 ----
		public String getR19_product() { return r19_product; }
		public void setR19_product(String v) { this.r19_product = v; }
		public BigDecimal getR19_amt() { return r19_amt; }
		public void setR19_amt(BigDecimal v) { this.r19_amt = v; }
		public BigDecimal getR19_amt_sub_add() { return r19_amt_sub_add; }
		public void setR19_amt_sub_add(BigDecimal v) { this.r19_amt_sub_add = v; }
		public BigDecimal getR19_amt_sub_del() { return r19_amt_sub_del; }
		public void setR19_amt_sub_del(BigDecimal v) { this.r19_amt_sub_del = v; }
		public BigDecimal getR19_amt_total() { return r19_amt_total; }
		public void setR19_amt_total(BigDecimal v) { this.r19_amt_total = v; }
		// ---- R20 ----
		public String getR20_product() { return r20_product; }
		public void setR20_product(String v) { this.r20_product = v; }
		public BigDecimal getR20_amt() { return r20_amt; }
		public void setR20_amt(BigDecimal v) { this.r20_amt = v; }
		public BigDecimal getR20_amt_sub_add() { return r20_amt_sub_add; }
		public void setR20_amt_sub_add(BigDecimal v) { this.r20_amt_sub_add = v; }
		public BigDecimal getR20_amt_sub_del() { return r20_amt_sub_del; }
		public void setR20_amt_sub_del(BigDecimal v) { this.r20_amt_sub_del = v; }
		public BigDecimal getR20_amt_total() { return r20_amt_total; }
		public void setR20_amt_total(BigDecimal v) { this.r20_amt_total = v; }
		// ---- R21 ----
		public String getR21_product() { return r21_product; }
		public void setR21_product(String v) { this.r21_product = v; }
		public BigDecimal getR21_amt() { return r21_amt; }
		public void setR21_amt(BigDecimal v) { this.r21_amt = v; }
		public BigDecimal getR21_amt_sub_add() { return r21_amt_sub_add; }
		public void setR21_amt_sub_add(BigDecimal v) { this.r21_amt_sub_add = v; }
		public BigDecimal getR21_amt_sub_del() { return r21_amt_sub_del; }
		public void setR21_amt_sub_del(BigDecimal v) { this.r21_amt_sub_del = v; }
		public BigDecimal getR21_amt_total() { return r21_amt_total; }
		public void setR21_amt_total(BigDecimal v) { this.r21_amt_total = v; }
		// ---- R22 ----
		public String getR22_product() { return r22_product; }
		public void setR22_product(String v) { this.r22_product = v; }
		public BigDecimal getR22_amt() { return r22_amt; }
		public void setR22_amt(BigDecimal v) { this.r22_amt = v; }
		public BigDecimal getR22_amt_sub_add() { return r22_amt_sub_add; }
		public void setR22_amt_sub_add(BigDecimal v) { this.r22_amt_sub_add = v; }
		public BigDecimal getR22_amt_sub_del() { return r22_amt_sub_del; }
		public void setR22_amt_sub_del(BigDecimal v) { this.r22_amt_sub_del = v; }
		public BigDecimal getR22_amt_total() { return r22_amt_total; }
		public void setR22_amt_total(BigDecimal v) { this.r22_amt_total = v; }
		// ---- R23 ----
		public String getR23_product() { return r23_product; }
		public void setR23_product(String v) { this.r23_product = v; }
		public BigDecimal getR23_amt() { return r23_amt; }
		public void setR23_amt(BigDecimal v) { this.r23_amt = v; }
		public BigDecimal getR23_amt_sub_add() { return r23_amt_sub_add; }
		public void setR23_amt_sub_add(BigDecimal v) { this.r23_amt_sub_add = v; }
		public BigDecimal getR23_amt_sub_del() { return r23_amt_sub_del; }
		public void setR23_amt_sub_del(BigDecimal v) { this.r23_amt_sub_del = v; }
		public BigDecimal getR23_amt_total() { return r23_amt_total; }
		public void setR23_amt_total(BigDecimal v) { this.r23_amt_total = v; }
		// ---- R24 ----
		public String getR24_product() { return r24_product; }
		public void setR24_product(String v) { this.r24_product = v; }
		public BigDecimal getR24_amt() { return r24_amt; }
		public void setR24_amt(BigDecimal v) { this.r24_amt = v; }
		public BigDecimal getR24_amt_sub_add() { return r24_amt_sub_add; }
		public void setR24_amt_sub_add(BigDecimal v) { this.r24_amt_sub_add = v; }
		public BigDecimal getR24_amt_sub_del() { return r24_amt_sub_del; }
		public void setR24_amt_sub_del(BigDecimal v) { this.r24_amt_sub_del = v; }
		public BigDecimal getR24_amt_total() { return r24_amt_total; }
		public void setR24_amt_total(BigDecimal v) { this.r24_amt_total = v; }
		// ---- R25 ----
		public String getR25_product() { return r25_product; }
		public void setR25_product(String v) { this.r25_product = v; }
		public BigDecimal getR25_amt() { return r25_amt; }
		public void setR25_amt(BigDecimal v) { this.r25_amt = v; }
		public BigDecimal getR25_amt_sub_add() { return r25_amt_sub_add; }
		public void setR25_amt_sub_add(BigDecimal v) { this.r25_amt_sub_add = v; }
		public BigDecimal getR25_amt_sub_del() { return r25_amt_sub_del; }
		public void setR25_amt_sub_del(BigDecimal v) { this.r25_amt_sub_del = v; }
		public BigDecimal getR25_amt_total() { return r25_amt_total; }
		public void setR25_amt_total(BigDecimal v) { this.r25_amt_total = v; }
		// ---- R26 ----
		public String getR26_product() { return r26_product; }
		public void setR26_product(String v) { this.r26_product = v; }
		public BigDecimal getR26_amt() { return r26_amt; }
		public void setR26_amt(BigDecimal v) { this.r26_amt = v; }
		public BigDecimal getR26_amt_sub_add() { return r26_amt_sub_add; }
		public void setR26_amt_sub_add(BigDecimal v) { this.r26_amt_sub_add = v; }
		public BigDecimal getR26_amt_sub_del() { return r26_amt_sub_del; }
		public void setR26_amt_sub_del(BigDecimal v) { this.r26_amt_sub_del = v; }
		public BigDecimal getR26_amt_total() { return r26_amt_total; }
		public void setR26_amt_total(BigDecimal v) { this.r26_amt_total = v; }
		// ---- R27 ----
		public String getR27_product() { return r27_product; }
		public void setR27_product(String v) { this.r27_product = v; }
		public BigDecimal getR27_amt() { return r27_amt; }
		public void setR27_amt(BigDecimal v) { this.r27_amt = v; }
		public BigDecimal getR27_amt_sub_add() { return r27_amt_sub_add; }
		public void setR27_amt_sub_add(BigDecimal v) { this.r27_amt_sub_add = v; }
		public BigDecimal getR27_amt_sub_del() { return r27_amt_sub_del; }
		public void setR27_amt_sub_del(BigDecimal v) { this.r27_amt_sub_del = v; }
		public BigDecimal getR27_amt_total() { return r27_amt_total; }
		public void setR27_amt_total(BigDecimal v) { this.r27_amt_total = v; }
		// ---- R28 ----
		public String getR28_product() { return r28_product; }
		public void setR28_product(String v) { this.r28_product = v; }
		public BigDecimal getR28_amt() { return r28_amt; }
		public void setR28_amt(BigDecimal v) { this.r28_amt = v; }
		public BigDecimal getR28_amt_sub_add() { return r28_amt_sub_add; }
		public void setR28_amt_sub_add(BigDecimal v) { this.r28_amt_sub_add = v; }
		public BigDecimal getR28_amt_sub_del() { return r28_amt_sub_del; }
		public void setR28_amt_sub_del(BigDecimal v) { this.r28_amt_sub_del = v; }
		public BigDecimal getR28_amt_total() { return r28_amt_total; }
		public void setR28_amt_total(BigDecimal v) { this.r28_amt_total = v; }

		// ---- common ----
		public Date getReport_date() { return report_date; }
		public void setReport_date(Date report_date) { this.report_date = report_date; }
		public BigDecimal getReport_version() { return report_version; }
		public void setReport_version(BigDecimal report_version) { this.report_version = report_version; }
		public String getReport_frequency() { return report_frequency; }
		public void setReport_frequency(String report_frequency) { this.report_frequency = report_frequency; }
		public String getReport_code() { return report_code; }
		public void setReport_code(String report_code) { this.report_code = report_code; }
		public String getReport_desc() { return report_desc; }
		public void setReport_desc(String report_desc) { this.report_desc = report_desc; }
		public String getEntity_flg() { return entity_flg; }
		public void setEntity_flg(String entity_flg) { this.entity_flg = entity_flg; }
		public String getModify_flg() { return modify_flg; }
		public void setModify_flg(String modify_flg) { this.modify_flg = modify_flg; }
		public String getDel_flg() { return del_flg; }
		public void setDel_flg(String del_flg) { this.del_flg = del_flg; }
	}

	/** Same columns as the summary entity + REPORT_RESUBDATE. */
	public static class FORMAT_II_Archival_Summary_Entity extends FORMAT_II_Summary_Entity {

		private Date reportResubDate;

		public Date getReportResubDate() { return reportResubDate; }
		public void setReportResubDate(Date reportResubDate) { this.reportResubDate = reportResubDate; }
	}

	public static class FORMAT_II_Detail_Entity {

		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportName;
		private String reportLabel;
		private String reportAddlCriteria_1;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private BigDecimal average;
		private String glshCode;
		private String glCode;
		private Date reportDate;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		private char entityFlg;
		private char modifyFlg;
		private char delFlg;

		public String getCustId() { return custId; }
		public void setCustId(String custId) { this.custId = custId; }
		public String getAcctNumber() { return acctNumber; }
		public void setAcctNumber(String acctNumber) { this.acctNumber = acctNumber; }
		public String getAcctName() { return acctName; }
		public void setAcctName(String acctName) { this.acctName = acctName; }
		public String getDataType() { return dataType; }
		public void setDataType(String dataType) { this.dataType = dataType; }
		public String getReportName() { return reportName; }
		public void setReportName(String reportName) { this.reportName = reportName; }
		public String getReportLabel() { return reportLabel; }
		public void setReportLabel(String reportLabel) { this.reportLabel = reportLabel; }
		public String getReportAddlCriteria_1() { return reportAddlCriteria_1; }
		public void setReportAddlCriteria_1(String v) { this.reportAddlCriteria_1 = v; }
		public String getReportRemarks() { return reportRemarks; }
		public void setReportRemarks(String reportRemarks) { this.reportRemarks = reportRemarks; }
		public String getModificationRemarks() { return modificationRemarks; }
		public void setModificationRemarks(String v) { this.modificationRemarks = v; }
		public String getDataEntryVersion() { return dataEntryVersion; }
		public void setDataEntryVersion(String v) { this.dataEntryVersion = v; }
		public BigDecimal getAcctBalanceInpula() { return acctBalanceInpula; }
		public void setAcctBalanceInpula(BigDecimal v) { this.acctBalanceInpula = v; }
		public BigDecimal getAverage() { return average; }
		public void setAverage(BigDecimal average) { this.average = average; }
		public String getGlshCode() { return glshCode; }
		public void setGlshCode(String glshCode) { this.glshCode = glshCode; }
		public String getGlCode() { return glCode; }
		public void setGlCode(String glCode) { this.glCode = glCode; }
		public Date getReportDate() { return reportDate; }
		public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
		public String getCreateUser() { return createUser; }
		public void setCreateUser(String createUser) { this.createUser = createUser; }
		public Date getCreateTime() { return createTime; }
		public void setCreateTime(Date createTime) { this.createTime = createTime; }
		public String getModifyUser() { return modifyUser; }
		public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
		public Date getModifyTime() { return modifyTime; }
		public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
		public String getVerifyUser() { return verifyUser; }
		public void setVerifyUser(String verifyUser) { this.verifyUser = verifyUser; }
		public Date getVerifyTime() { return verifyTime; }
		public void setVerifyTime(Date verifyTime) { this.verifyTime = verifyTime; }
		public char getEntityFlg() { return entityFlg; }
		public void setEntityFlg(char entityFlg) { this.entityFlg = entityFlg; }
		public char getModifyFlg() { return modifyFlg; }
		public void setModifyFlg(char modifyFlg) { this.modifyFlg = modifyFlg; }
		public char getDelFlg() { return delFlg; }
		public void setDelFlg(char delFlg) { this.delFlg = delFlg; }
	}
}
