package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service
public class BRRS_M_IS_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_IS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	AuditService auditService;

	@Autowired
	SessionFactory sessionFactory;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ========================
	// JDBC QUERY METHODS
	// ========================

	// =====================================================
	// SUMAMRY REPO'S
	// =====================================================

	// =====================================================
	// BRRS_M_IS_Summary_Repo 1
	// =====================================================

	public List<M_IS_Summary_Entity1> summary1_getdatabydateList(Date rpt_date) {
		// ======
		// Retrieve all summary records matching the given report date using JDBC.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new M_IS_Summary_Entity1RowMapper());
	}

	public List<M_IS_Summary_Entity1> summary1_getdatabydateListWithVersion(String todate) {
		// ======
		// Retrieve the latest versioned summary record for a given report date string.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new M_IS_Summary_Entity1RowMapper());
	}

	public Optional<M_IS_Summary_Entity1> summary1_findTopByReportDateOrderByReportVersionDesc(Date reportDate) {
		// ======
		// Fetch the single most recent report version for the specified report date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_DATE = ? "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		List<M_IS_Summary_Entity1> result = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_IS_Summary_Entity1RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public Optional<M_IS_Summary_Entity1> summary1_findByReportDateAndReportVersion(Date reportDate,
			String reportVersion) {
		// ======
		// Check whether a specific report version already exists for the given date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_IS_Summary_Entity1> result = jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_IS_Summary_Entity1RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public List<M_IS_Summary_Entity1> summary1_getdatabydateListWithVersion() {
		// ======
		// Retrieve the most recent versioned summary record across all report dates.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE1 WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_IS_Summary_Entity1RowMapper());
	}

	// =====================================================
	// BRRS_M_IS_Summary_Repo 2
	// =====================================================

	public List<M_IS_Summary_Entity2> summary2_getdatabydateList(Date rpt_date) {
		// ======
		// Retrieve all summary records matching the given report date using JDBC.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new M_IS_Summary_Entity2RowMapper());
	}

	public List<M_IS_Summary_Entity2> summary2_getdatabydateListWithVersion(String todate) {
		// ======
		// Retrieve the latest versioned summary record for a given report date string.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new M_IS_Summary_Entity2RowMapper());
	}

	public Optional<M_IS_Summary_Entity2> summary2_findTopByReportDateOrderByReportVersionDesc(Date reportDate) {
		// ======
		// Fetch the single most recent report version for the specified report date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_DATE = ? "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		List<M_IS_Summary_Entity2> result = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_IS_Summary_Entity2RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public Optional<M_IS_Summary_Entity2> summary2_findByReportDateAndReportVersion(Date reportDate,
			String reportVersion) {
		// ======
		// Check whether a specific report version already exists for the given date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_IS_Summary_Entity2> result = jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_IS_Summary_Entity2RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public List<M_IS_Summary_Entity2> summary2_getdatabydateListWithVersion() {
		// ======
		// Retrieve the most recent versioned summary record across all report dates.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_SUMMARYTABLE2 WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_IS_Summary_Entity2RowMapper());
	}

	// =====================================================
	// BRRS_M_IS_Archival_Summary_Repo 1
	// =====================================================

	public List<M_IS_Archival_Summary_Entity1> archivalSummary1_getdatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {
		// ======
		// Fetch specific archival summary records matching report date and version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_IS_Archival_Summary_Entity1RowMapper());
	}

	public Optional<M_IS_Archival_Summary_Entity1> archivalSummary1_getLatestArchivalVersionByDate(Date reportDate) {
		// ======
		// Fetch the latest archival version for the given report date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL "
				+ "ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_IS_Archival_Summary_Entity1> result = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_IS_Archival_Summary_Entity1RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public Optional<M_IS_Summary_Entity1> archivalSummary1_findByReportDateAndReportVersion(Date reportDate,
			BigDecimal reportVersion) {
		// ======
		// Fetch record by composite primary key of report date and version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_IS_Summary_Entity1> result = jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_IS_Summary_Entity1RowMapper());
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}

	public List<M_IS_Archival_Summary_Entity1> archivalSummary1_getdatabydateListWithVersionAll() {
		// ======
		// Retrieve the most recent versioned archival summary record overall.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 "
				+ "WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_IS_Archival_Summary_Entity1RowMapper());
	}

	public List<M_IS_Archival_Summary_Entity1> archivalSummary1_getdatabydateListWithVersion() {
		// ======
		// Retrieve all versioned archival summary records ordered descending by date and version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 "
				+ "WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_DATE DESC, TO_NUMBER(REPORT_VERSION) DESC";
		return jdbcTemplate.query(sql, new M_IS_Archival_Summary_Entity1RowMapper());
	}

	// =====================================================
	// BRRS_M_IS_Archival_Summary_Repo 2
	// =====================================================

	public List<Object[]> archivalSummary2_getM_ISarchival() {
		// ======
		// Retrieve report date and version pairs ordered by version for dropdowns.
		// ======
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<M_IS_Archival_Summary_Entity2> archivalSummary2_getdatabydateListarchival(Date report_date,
			BigDecimal report_version) {
		// ======
		// Fetch specific archival summary records matching report date and version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_IS_Archival_Summary_Entity2RowMapper());
	}

	// =====================================================
	// BRRS_M_IS_Detail_Repo
	// =====================================================

	public List<M_IS_Detail_Entity> detail_getdatabydateList(Date reportdate) {
		// ======
		// Retrieve all detail records matching the given report date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_IS_Detail_EntityRowMapper());
	}

	public List<M_IS_Detail_Entity> detail_getdatabydateList(Date reportdate, int offset, int limit) {
		// ======
		// Retrieve a paginated subset of detail records using offset and limit.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE REPORT_DATE = ? "
				+ "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_IS_Detail_EntityRowMapper());
	}

	public int detail_getdatacount(Date reportdate) {
		// ======
		// Count the total number of detail records for the given report date.
		// ======
		String sql = "SELECT COUNT(*) FROM BRRS_M_IS_DETAILTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
		return count != null ? count : 0;
	}

	public List<M_IS_Detail_Entity> detail_GetDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria_1,
			Date reportdate) {
		// ======
		// Fetch detail records matching report label, additional criteria, and date.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria_1, reportdate },
				new M_IS_Detail_EntityRowMapper());
	}

	public M_IS_Detail_Entity detail_findBySno(String Sno) {
		// ======
		// Fetch a single detail record by its unique serial number identifier.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE SNO = ?";
		List<M_IS_Detail_Entity> result = jdbcTemplate.query(sql, new Object[] { Sno },
				new M_IS_Detail_EntityRowMapper());
		return result.isEmpty() ? null : result.get(0);
	}

	// =====================================================
	// BRRS_M_IS_Archival_Detail_Repo
	// =====================================================

	public List<M_IS_Archival_Detail_Entity> archivalDetail_getdatabydateList(Date reportdate,
			String DATA_ENTRY_VERSION) {
		// ======
		// Retrieve archival detail records matching report date and entry version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { reportdate, DATA_ENTRY_VERSION },
				new M_IS_Archival_Detail_EntityRowMapper());
	}

	public List<M_IS_Archival_Detail_Entity> archivalDetail_GetDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria_1, Date reportdate, String DATA_ENTRY_VERSION) {
		// ======
		// Fetch archival detail records matching label, criteria, date, and version.
		// ======
		String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql,
				new Object[] { reportLabel, reportAddlCriteria_1, reportdate, DATA_ENTRY_VERSION },
				new M_IS_Archival_Detail_EntityRowMapper());
	}

	public String getishighestversion(Date reportDate, BigDecimal reportVersion) {
		if (reportDate == null || reportVersion == null) {
			return "NO";
		}
		try {
			String sql = "SELECT CASE WHEN ? = MAX(TO_NUMBER(REPORT_VERSION)) THEN 'YES' ELSE 'NO' END AS is_highest "
					+ "FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			String result = jdbcTemplate.queryForObject(sql, new Object[] { reportVersion, reportDate }, String.class);
			return result != null ? result : "NO";
		} catch (Exception e) {
			return "NO";
		}
	}

	public List<M_IS_Summary_Entity1> get_ResubSummary1ByDate(Date reportDate, String reportVersion) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?",
				new Object[] { reportDate, reportVersion }, new M_IS_Summary_Entity1RowMapper());
	}

	public List<M_IS_Summary_Entity2> get_ResubSummary2ByDate(Date reportDate, String reportVersion) {
		return jdbcTemplate.query(
				"SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?",
				new Object[] { reportDate, reportVersion }, new M_IS_Summary_Entity2RowMapper());
	}

	private Date parseFlexibleDate(String rawDate) {
		if (rawDate == null || rawDate.trim().isEmpty()) {
			return null;
		}
		String clean = rawDate.trim();
		String[] patterns = { "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd-MMM-yyyy", "yyyy/MM/dd" };
		for (String pattern : patterns) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
				sdf.setLenient(false);
				return sdf.parse(clean);
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	public M_IS_Detail_Entity findBySnoArch(String acctNumber) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		try {
			String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? ORDER BY REPORT_DATE DESC";
			List<M_IS_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { acctNumber.trim() },
					new M_IS_Detail_EntityRowMapper());
			return (list != null && !list.isEmpty()) ? list.get(0) : null;
		} catch (Exception e) {
			return null;
		}
	}

	public M_IS_Detail_Entity findByAcctNumberArch(String acctNumber, Date reportDate) {
		if (acctNumber == null || acctNumber.trim().isEmpty()) {
			return null;
		}
		if (reportDate != null) {
			try {
				String sql = "SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)";
				List<M_IS_Detail_Entity> list = jdbcTemplate.query(sql,
						new Object[] { acctNumber.trim(), reportDate }, new M_IS_Detail_EntityRowMapper());
				if (list != null && !list.isEmpty()) {
					return list.get(0);
				}
			} catch (Exception ignored) {
			}
			return null;
		}
		return findBySnoArch(acctNumber);
	}

	private Set<String> getTableColumns(String tableName) {
		Set<String> cols = new HashSet<>();
		jdbcTemplate.query("SELECT * FROM " + tableName + " WHERE 1=0", rs -> {
			ResultSetMetaData md = rs.getMetaData();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				cols.add(md.getColumnName(i).toUpperCase());
			}
			return null;
		});
		return cols;
	}

	private List<String> getCommonColumns(String sourceTable, String targetTable, Set<String> exclude) {
		Set<String> sourceCols = getTableColumns(sourceTable);
		Set<String> targetCols = getTableColumns(targetTable);
		List<String> common = new ArrayList<>();
		for (String col : sourceCols) {
			if (targetCols.contains(col) && (exclude == null || !exclude.contains(col.toUpperCase()))) {
				common.add(col);
			}
		}
		return common;
	}

	private void copyTableByVersion(String sourceTable, String targetTable, java.sql.Date sqlDate, String version) {
		try {
			Set<String> exclude = new HashSet<>(Arrays.asList("REPORT_RESUBDATE"));
			List<String> commonCols = getCommonColumns(sourceTable, targetTable, exclude);
			if (commonCols.isEmpty()) {
				System.out.println("No common columns between " + sourceTable + " and " + targetTable);
				return;
			}
			String colsJoined = String.join(", ", commonCols);
			if (version != null && !version.isEmpty()) {
				String insertSql = "INSERT INTO " + targetTable + " (" + colsJoined + ") SELECT " + colsJoined
						+ " FROM " + sourceTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
				jdbcTemplate.update(insertSql, sqlDate, version);
			} else {
				String insertSql = "INSERT INTO " + targetTable + " (" + colsJoined + ") SELECT " + colsJoined
						+ " FROM " + sourceTable + " WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				jdbcTemplate.update(insertSql, sqlDate);
			}
			jdbcTemplate.update(
					"UPDATE " + targetTable + " SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
					sqlDate);
		} catch (Exception e) {
			System.err.println("Error copying from " + sourceTable + " to " + targetTable + ": " + e.getMessage());
		}
	}

	// ========================================
	// M_IS - ROW MAPPERS
	// ==========================================

	// ========================================
	// M_IS_Summary_Entity1 - ROW MAPPER
//==========================================	

	public class M_IS_Summary_Entity1RowMapper implements RowMapper<M_IS_Summary_Entity1> {
		@Override
		public M_IS_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Summary_Entity1 obj = new M_IS_Summary_Entity1();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getString("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// R10

			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R10_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR10_HELD_TO_MATURITY(rs.getBigDecimal("R10_HELD_TO_MATURITY"));
			obj.setR10_AVAILABLE_FOR_SALE(rs.getBigDecimal("R10_AVAILABLE_FOR_SALE"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11

			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R11_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR11_HELD_TO_MATURITY(rs.getBigDecimal("R11_HELD_TO_MATURITY"));
			obj.setR11_AVAILABLE_FOR_SALE(rs.getBigDecimal("R11_AVAILABLE_FOR_SALE"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12

			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R12_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR12_HELD_TO_MATURITY(rs.getBigDecimal("R12_HELD_TO_MATURITY"));
			obj.setR12_AVAILABLE_FOR_SALE(rs.getBigDecimal("R12_AVAILABLE_FOR_SALE"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13

			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R13_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR13_HELD_TO_MATURITY(rs.getBigDecimal("R13_HELD_TO_MATURITY"));
			obj.setR13_AVAILABLE_FOR_SALE(rs.getBigDecimal("R13_AVAILABLE_FOR_SALE"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14

			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R14_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR14_HELD_TO_MATURITY(rs.getBigDecimal("R14_HELD_TO_MATURITY"));
			obj.setR14_AVAILABLE_FOR_SALE(rs.getBigDecimal("R14_AVAILABLE_FOR_SALE"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15

			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R15_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR15_HELD_TO_MATURITY(rs.getBigDecimal("R15_HELD_TO_MATURITY"));
			obj.setR15_AVAILABLE_FOR_SALE(rs.getBigDecimal("R15_AVAILABLE_FOR_SALE"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R16

			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R16_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR16_HELD_TO_MATURITY(rs.getBigDecimal("R16_HELD_TO_MATURITY"));
			obj.setR16_AVAILABLE_FOR_SALE(rs.getBigDecimal("R16_AVAILABLE_FOR_SALE"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			return obj;

		}
	}

	// ENTITY CLASS

	public class M_IS_Summary_Entity1 {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String R10_PRODUCT;
		private BigDecimal R10_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R10_HELD_TO_MATURITY;
		private BigDecimal R10_AVAILABLE_FOR_SALE;
		private BigDecimal R10_TOTAL;

		private String R11_PRODUCT;
		private BigDecimal R11_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R11_HELD_TO_MATURITY;
		private BigDecimal R11_AVAILABLE_FOR_SALE;
		private BigDecimal R11_TOTAL;

		private String R12_PRODUCT;
		private BigDecimal R12_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R12_HELD_TO_MATURITY;
		private BigDecimal R12_AVAILABLE_FOR_SALE;
		private BigDecimal R12_TOTAL;

		private String R13_PRODUCT;
		private BigDecimal R13_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R13_HELD_TO_MATURITY;
		private BigDecimal R13_AVAILABLE_FOR_SALE;
		private BigDecimal R13_TOTAL;

		private String R14_PRODUCT;
		private BigDecimal R14_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R14_HELD_TO_MATURITY;
		private BigDecimal R14_AVAILABLE_FOR_SALE;
		private BigDecimal R14_TOTAL;

		private String R15_PRODUCT;
		private BigDecimal R15_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R15_HELD_TO_MATURITY;
		private BigDecimal R15_AVAILABLE_FOR_SALE;
		private BigDecimal R15_TOTAL;

		private String R16_PRODUCT;
		private BigDecimal R16_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R16_HELD_TO_MATURITY;
		private BigDecimal R16_AVAILABLE_FOR_SALE;
		private BigDecimal R16_TOTAL;

		public M_IS_Summary_Entity1() {
			super();
		}

		// GETTERS AND SETTERS

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			this.R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R10_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR10_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r10_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R10_FAIR_VALUE_PROFIT_AND_LOSS = r10_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR10_HELD_TO_MATURITY() {
			return R10_HELD_TO_MATURITY;
		}

		public void setR10_HELD_TO_MATURITY(BigDecimal r10_HELD_TO_MATURITY) {
			this.R10_HELD_TO_MATURITY = r10_HELD_TO_MATURITY;
		}

		public BigDecimal getR10_AVAILABLE_FOR_SALE() {
			return R10_AVAILABLE_FOR_SALE;
		}

		public void setR10_AVAILABLE_FOR_SALE(BigDecimal r10_AVAILABLE_FOR_SALE) {
			this.R10_AVAILABLE_FOR_SALE = r10_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			this.R10_TOTAL = r10_TOTAL;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			this.R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R11_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR11_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r11_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R11_FAIR_VALUE_PROFIT_AND_LOSS = r11_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR11_HELD_TO_MATURITY() {
			return R11_HELD_TO_MATURITY;
		}

		public void setR11_HELD_TO_MATURITY(BigDecimal r11_HELD_TO_MATURITY) {
			this.R11_HELD_TO_MATURITY = r11_HELD_TO_MATURITY;
		}

		public BigDecimal getR11_AVAILABLE_FOR_SALE() {
			return R11_AVAILABLE_FOR_SALE;
		}

		public void setR11_AVAILABLE_FOR_SALE(BigDecimal r11_AVAILABLE_FOR_SALE) {
			this.R11_AVAILABLE_FOR_SALE = r11_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			this.R11_TOTAL = r11_TOTAL;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			this.R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R12_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR12_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r12_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R12_FAIR_VALUE_PROFIT_AND_LOSS = r12_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR12_HELD_TO_MATURITY() {
			return R12_HELD_TO_MATURITY;
		}

		public void setR12_HELD_TO_MATURITY(BigDecimal r12_HELD_TO_MATURITY) {
			this.R12_HELD_TO_MATURITY = r12_HELD_TO_MATURITY;
		}

		public BigDecimal getR12_AVAILABLE_FOR_SALE() {
			return R12_AVAILABLE_FOR_SALE;
		}

		public void setR12_AVAILABLE_FOR_SALE(BigDecimal r12_AVAILABLE_FOR_SALE) {
			this.R12_AVAILABLE_FOR_SALE = r12_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			this.R12_TOTAL = r12_TOTAL;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			this.R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R13_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR13_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r13_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R13_FAIR_VALUE_PROFIT_AND_LOSS = r13_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR13_HELD_TO_MATURITY() {
			return R13_HELD_TO_MATURITY;
		}

		public void setR13_HELD_TO_MATURITY(BigDecimal r13_HELD_TO_MATURITY) {
			this.R13_HELD_TO_MATURITY = r13_HELD_TO_MATURITY;
		}

		public BigDecimal getR13_AVAILABLE_FOR_SALE() {
			return R13_AVAILABLE_FOR_SALE;
		}

		public void setR13_AVAILABLE_FOR_SALE(BigDecimal r13_AVAILABLE_FOR_SALE) {
			this.R13_AVAILABLE_FOR_SALE = r13_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			this.R13_TOTAL = r13_TOTAL;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			this.R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R14_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR14_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r14_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R14_FAIR_VALUE_PROFIT_AND_LOSS = r14_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR14_HELD_TO_MATURITY() {
			return R14_HELD_TO_MATURITY;
		}

		public void setR14_HELD_TO_MATURITY(BigDecimal r14_HELD_TO_MATURITY) {
			this.R14_HELD_TO_MATURITY = r14_HELD_TO_MATURITY;
		}

		public BigDecimal getR14_AVAILABLE_FOR_SALE() {
			return R14_AVAILABLE_FOR_SALE;
		}

		public void setR14_AVAILABLE_FOR_SALE(BigDecimal r14_AVAILABLE_FOR_SALE) {
			this.R14_AVAILABLE_FOR_SALE = r14_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			this.R14_TOTAL = r14_TOTAL;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			this.R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R15_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR15_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r15_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R15_FAIR_VALUE_PROFIT_AND_LOSS = r15_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR15_HELD_TO_MATURITY() {
			return R15_HELD_TO_MATURITY;
		}

		public void setR15_HELD_TO_MATURITY(BigDecimal r15_HELD_TO_MATURITY) {
			this.R15_HELD_TO_MATURITY = r15_HELD_TO_MATURITY;
		}

		public BigDecimal getR15_AVAILABLE_FOR_SALE() {
			return R15_AVAILABLE_FOR_SALE;
		}

		public void setR15_AVAILABLE_FOR_SALE(BigDecimal r15_AVAILABLE_FOR_SALE) {
			this.R15_AVAILABLE_FOR_SALE = r15_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			this.R15_TOTAL = r15_TOTAL;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			this.R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R16_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR16_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r16_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R16_FAIR_VALUE_PROFIT_AND_LOSS = r16_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR16_HELD_TO_MATURITY() {
			return R16_HELD_TO_MATURITY;
		}

		public void setR16_HELD_TO_MATURITY(BigDecimal r16_HELD_TO_MATURITY) {
			this.R16_HELD_TO_MATURITY = r16_HELD_TO_MATURITY;
		}

		public BigDecimal getR16_AVAILABLE_FOR_SALE() {
			return R16_AVAILABLE_FOR_SALE;
		}

		public void setR16_AVAILABLE_FOR_SALE(BigDecimal r16_AVAILABLE_FOR_SALE) {
			this.R16_AVAILABLE_FOR_SALE = r16_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal r16_TOTAL) {
			this.R16_TOTAL = r16_TOTAL;
		}

	}

	// ========================================
	// M_IS_Summary_Entity2 - ROW MAPPER
	// ==========================================

	public class M_IS_Summary_Entity2RowMapper implements RowMapper<M_IS_Summary_Entity2> {
		@Override
		public M_IS_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Summary_Entity2 obj = new M_IS_Summary_Entity2();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getString("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_HELD_FOR_TRADING(rs.getBigDecimal("R21_HELD_FOR_TRADING"));
			obj.setR21_AMORTISED_COST(rs.getBigDecimal("R21_AMORTISED_COST"));
			obj.setR21_AVAILABLE_FOR_SALE(rs.getBigDecimal("R21_AVAILABLE_FOR_SALE"));
			obj.setR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR21_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R21_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_HELD_FOR_TRADING(rs.getBigDecimal("R22_HELD_FOR_TRADING"));
			obj.setR22_AMORTISED_COST(rs.getBigDecimal("R22_AMORTISED_COST"));
			obj.setR22_AVAILABLE_FOR_SALE(rs.getBigDecimal("R22_AVAILABLE_FOR_SALE"));
			obj.setR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR22_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R22_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_HELD_FOR_TRADING(rs.getBigDecimal("R23_HELD_FOR_TRADING"));
			obj.setR23_AMORTISED_COST(rs.getBigDecimal("R23_AMORTISED_COST"));
			obj.setR23_AVAILABLE_FOR_SALE(rs.getBigDecimal("R23_AVAILABLE_FOR_SALE"));
			obj.setR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR23_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R23_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_HELD_FOR_TRADING(rs.getBigDecimal("R24_HELD_FOR_TRADING"));
			obj.setR24_AMORTISED_COST(rs.getBigDecimal("R24_AMORTISED_COST"));
			obj.setR24_AVAILABLE_FOR_SALE(rs.getBigDecimal("R24_AVAILABLE_FOR_SALE"));
			obj.setR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR24_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R24_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_HELD_FOR_TRADING(rs.getBigDecimal("R25_HELD_FOR_TRADING"));
			obj.setR25_AMORTISED_COST(rs.getBigDecimal("R25_AMORTISED_COST"));
			obj.setR25_AVAILABLE_FOR_SALE(rs.getBigDecimal("R25_AVAILABLE_FOR_SALE"));
			obj.setR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR25_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R25_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_HELD_FOR_TRADING(rs.getBigDecimal("R26_HELD_FOR_TRADING"));
			obj.setR26_AMORTISED_COST(rs.getBigDecimal("R26_AMORTISED_COST"));
			obj.setR26_AVAILABLE_FOR_SALE(rs.getBigDecimal("R26_AVAILABLE_FOR_SALE"));
			obj.setR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR26_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R26_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_HELD_FOR_TRADING(rs.getBigDecimal("R27_HELD_FOR_TRADING"));
			obj.setR27_AMORTISED_COST(rs.getBigDecimal("R27_AMORTISED_COST"));
			obj.setR27_AVAILABLE_FOR_SALE(rs.getBigDecimal("R27_AVAILABLE_FOR_SALE"));
			obj.setR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR27_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R27_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_HELD_FOR_TRADING(rs.getBigDecimal("R28_HELD_FOR_TRADING"));
			obj.setR28_AMORTISED_COST(rs.getBigDecimal("R28_AMORTISED_COST"));
			obj.setR28_AVAILABLE_FOR_SALE(rs.getBigDecimal("R28_AVAILABLE_FOR_SALE"));
			obj.setR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR28_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R28_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_HELD_FOR_TRADING(rs.getBigDecimal("R29_HELD_FOR_TRADING"));
			obj.setR29_AMORTISED_COST(rs.getBigDecimal("R29_AMORTISED_COST"));
			obj.setR29_AVAILABLE_FOR_SALE(rs.getBigDecimal("R29_AVAILABLE_FOR_SALE"));
			obj.setR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR29_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R29_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));

			// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_HELD_FOR_TRADING(rs.getBigDecimal("R30_HELD_FOR_TRADING"));
			obj.setR30_AMORTISED_COST(rs.getBigDecimal("R30_AMORTISED_COST"));
			obj.setR30_AVAILABLE_FOR_SALE(rs.getBigDecimal("R30_AVAILABLE_FOR_SALE"));
			obj.setR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR30_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R30_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));

			// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_HELD_FOR_TRADING(rs.getBigDecimal("R31_HELD_FOR_TRADING"));
			obj.setR31_AMORTISED_COST(rs.getBigDecimal("R31_AMORTISED_COST"));
			obj.setR31_AVAILABLE_FOR_SALE(rs.getBigDecimal("R31_AVAILABLE_FOR_SALE"));
			obj.setR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR31_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R31_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));

			// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_HELD_FOR_TRADING(rs.getBigDecimal("R32_HELD_FOR_TRADING"));
			obj.setR32_AMORTISED_COST(rs.getBigDecimal("R32_AMORTISED_COST"));
			obj.setR32_AVAILABLE_FOR_SALE(rs.getBigDecimal("R32_AVAILABLE_FOR_SALE"));
			obj.setR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR32_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R32_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));

			// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_HELD_FOR_TRADING(rs.getBigDecimal("R33_HELD_FOR_TRADING"));
			obj.setR33_AMORTISED_COST(rs.getBigDecimal("R33_AMORTISED_COST"));
			obj.setR33_AVAILABLE_FOR_SALE(rs.getBigDecimal("R33_AVAILABLE_FOR_SALE"));
			obj.setR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR33_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R33_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));

			// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_HELD_FOR_TRADING(rs.getBigDecimal("R34_HELD_FOR_TRADING"));
			obj.setR34_AMORTISED_COST(rs.getBigDecimal("R34_AMORTISED_COST"));
			obj.setR34_AVAILABLE_FOR_SALE(rs.getBigDecimal("R34_AVAILABLE_FOR_SALE"));
			obj.setR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR34_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R34_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));

			// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_HELD_FOR_TRADING(rs.getBigDecimal("R35_HELD_FOR_TRADING"));
			obj.setR35_AMORTISED_COST(rs.getBigDecimal("R35_AMORTISED_COST"));
			obj.setR35_AVAILABLE_FOR_SALE(rs.getBigDecimal("R35_AVAILABLE_FOR_SALE"));
			obj.setR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR35_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R35_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));

			return obj;
		}
	}

	// ========================================
	// M_IS_Summary_Entity2 - ENTITY CLASS
	// ========================================

	public class M_IS_Summary_Entity2 {

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		private String report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String R21_PRODUCT;
		private BigDecimal R21_HELD_FOR_TRADING;
		private BigDecimal R21_AMORTISED_COST;
		private BigDecimal R21_AVAILABLE_FOR_SALE;
		private BigDecimal R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R21_TOTAL;

		private String R22_PRODUCT;
		private BigDecimal R22_HELD_FOR_TRADING;
		private BigDecimal R22_AMORTISED_COST;
		private BigDecimal R22_AVAILABLE_FOR_SALE;
		private BigDecimal R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R22_TOTAL;

		private String R23_PRODUCT;
		private BigDecimal R23_HELD_FOR_TRADING;
		private BigDecimal R23_AMORTISED_COST;
		private BigDecimal R23_AVAILABLE_FOR_SALE;
		private BigDecimal R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R23_TOTAL;

		private String R24_PRODUCT;
		private BigDecimal R24_HELD_FOR_TRADING;
		private BigDecimal R24_AMORTISED_COST;
		private BigDecimal R24_AVAILABLE_FOR_SALE;
		private BigDecimal R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R24_TOTAL;

		private String R25_PRODUCT;
		private BigDecimal R25_HELD_FOR_TRADING;
		private BigDecimal R25_AMORTISED_COST;
		private BigDecimal R25_AVAILABLE_FOR_SALE;
		private BigDecimal R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R25_TOTAL;

		private String R26_PRODUCT;
		private BigDecimal R26_HELD_FOR_TRADING;
		private BigDecimal R26_AMORTISED_COST;
		private BigDecimal R26_AVAILABLE_FOR_SALE;
		private BigDecimal R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R26_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_HELD_FOR_TRADING;
		private BigDecimal R27_AMORTISED_COST;
		private BigDecimal R27_AVAILABLE_FOR_SALE;
		private BigDecimal R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R27_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_HELD_FOR_TRADING;
		private BigDecimal R28_AMORTISED_COST;
		private BigDecimal R28_AVAILABLE_FOR_SALE;
		private BigDecimal R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R28_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_HELD_FOR_TRADING;
		private BigDecimal R29_AMORTISED_COST;
		private BigDecimal R29_AVAILABLE_FOR_SALE;
		private BigDecimal R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R29_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_HELD_FOR_TRADING;
		private BigDecimal R30_AMORTISED_COST;
		private BigDecimal R30_AVAILABLE_FOR_SALE;
		private BigDecimal R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R30_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_HELD_FOR_TRADING;
		private BigDecimal R31_AMORTISED_COST;
		private BigDecimal R31_AVAILABLE_FOR_SALE;
		private BigDecimal R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R31_TOTAL;

		private String R32_PRODUCT;
		private BigDecimal R32_HELD_FOR_TRADING;
		private BigDecimal R32_AMORTISED_COST;
		private BigDecimal R32_AVAILABLE_FOR_SALE;
		private BigDecimal R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R32_TOTAL;

		private String R33_PRODUCT;
		private BigDecimal R33_HELD_FOR_TRADING;
		private BigDecimal R33_AMORTISED_COST;
		private BigDecimal R33_AVAILABLE_FOR_SALE;
		private BigDecimal R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R33_TOTAL;

		private String R34_PRODUCT;
		private BigDecimal R34_HELD_FOR_TRADING;
		private BigDecimal R34_AMORTISED_COST;
		private BigDecimal R34_AVAILABLE_FOR_SALE;
		private BigDecimal R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R34_TOTAL;

		private String R35_PRODUCT;
		private BigDecimal R35_HELD_FOR_TRADING;
		private BigDecimal R35_AMORTISED_COST;
		private BigDecimal R35_AVAILABLE_FOR_SALE;
		private BigDecimal R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R35_TOTAL;

		public M_IS_Summary_Entity2() {
			super();
		}

		// GETTERS AND SETTERS

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			this.R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_HELD_FOR_TRADING() {
			return R21_HELD_FOR_TRADING;
		}

		public void setR21_HELD_FOR_TRADING(BigDecimal r21_HELD_FOR_TRADING) {
			this.R21_HELD_FOR_TRADING = r21_HELD_FOR_TRADING;
		}

		public BigDecimal getR21_AMORTISED_COST() {
			return R21_AMORTISED_COST;
		}

		public void setR21_AMORTISED_COST(BigDecimal r21_AMORTISED_COST) {
			this.R21_AMORTISED_COST = r21_AMORTISED_COST;
		}

		public BigDecimal getR21_AVAILABLE_FOR_SALE() {
			return R21_AVAILABLE_FOR_SALE;
		}

		public void setR21_AVAILABLE_FOR_SALE(BigDecimal r21_AVAILABLE_FOR_SALE) {
			this.R21_AVAILABLE_FOR_SALE = r21_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR21_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r21_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R21_QUALIFYING_FOR_HEDGE_ACCOUNTING = r21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			this.R21_TOTAL = r21_TOTAL;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			this.R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_HELD_FOR_TRADING() {
			return R22_HELD_FOR_TRADING;
		}

		public void setR22_HELD_FOR_TRADING(BigDecimal r22_HELD_FOR_TRADING) {
			this.R22_HELD_FOR_TRADING = r22_HELD_FOR_TRADING;
		}

		public BigDecimal getR22_AMORTISED_COST() {
			return R22_AMORTISED_COST;
		}

		public void setR22_AMORTISED_COST(BigDecimal r22_AMORTISED_COST) {
			this.R22_AMORTISED_COST = r22_AMORTISED_COST;
		}

		public BigDecimal getR22_AVAILABLE_FOR_SALE() {
			return R22_AVAILABLE_FOR_SALE;
		}

		public void setR22_AVAILABLE_FOR_SALE(BigDecimal r22_AVAILABLE_FOR_SALE) {
			this.R22_AVAILABLE_FOR_SALE = r22_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR22_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r22_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R22_QUALIFYING_FOR_HEDGE_ACCOUNTING = r22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			this.R22_TOTAL = r22_TOTAL;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			this.R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_HELD_FOR_TRADING() {
			return R23_HELD_FOR_TRADING;
		}

		public void setR23_HELD_FOR_TRADING(BigDecimal r23_HELD_FOR_TRADING) {
			this.R23_HELD_FOR_TRADING = r23_HELD_FOR_TRADING;
		}

		public BigDecimal getR23_AMORTISED_COST() {
			return R23_AMORTISED_COST;
		}

		public void setR23_AMORTISED_COST(BigDecimal r23_AMORTISED_COST) {
			this.R23_AMORTISED_COST = r23_AMORTISED_COST;
		}

		public BigDecimal getR23_AVAILABLE_FOR_SALE() {
			return R23_AVAILABLE_FOR_SALE;
		}

		public void setR23_AVAILABLE_FOR_SALE(BigDecimal r23_AVAILABLE_FOR_SALE) {
			this.R23_AVAILABLE_FOR_SALE = r23_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR23_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r23_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R23_QUALIFYING_FOR_HEDGE_ACCOUNTING = r23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			this.R23_TOTAL = r23_TOTAL;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			this.R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_HELD_FOR_TRADING() {
			return R24_HELD_FOR_TRADING;
		}

		public void setR24_HELD_FOR_TRADING(BigDecimal r24_HELD_FOR_TRADING) {
			this.R24_HELD_FOR_TRADING = r24_HELD_FOR_TRADING;
		}

		public BigDecimal getR24_AMORTISED_COST() {
			return R24_AMORTISED_COST;
		}

		public void setR24_AMORTISED_COST(BigDecimal r24_AMORTISED_COST) {
			this.R24_AMORTISED_COST = r24_AMORTISED_COST;
		}

		public BigDecimal getR24_AVAILABLE_FOR_SALE() {
			return R24_AVAILABLE_FOR_SALE;
		}

		public void setR24_AVAILABLE_FOR_SALE(BigDecimal r24_AVAILABLE_FOR_SALE) {
			this.R24_AVAILABLE_FOR_SALE = r24_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR24_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r24_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R24_QUALIFYING_FOR_HEDGE_ACCOUNTING = r24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			this.R24_TOTAL = r24_TOTAL;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			this.R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_HELD_FOR_TRADING() {
			return R25_HELD_FOR_TRADING;
		}

		public void setR25_HELD_FOR_TRADING(BigDecimal r25_HELD_FOR_TRADING) {
			this.R25_HELD_FOR_TRADING = r25_HELD_FOR_TRADING;
		}

		public BigDecimal getR25_AMORTISED_COST() {
			return R25_AMORTISED_COST;
		}

		public void setR25_AMORTISED_COST(BigDecimal r25_AMORTISED_COST) {
			this.R25_AMORTISED_COST = r25_AMORTISED_COST;
		}

		public BigDecimal getR25_AVAILABLE_FOR_SALE() {
			return R25_AVAILABLE_FOR_SALE;
		}

		public void setR25_AVAILABLE_FOR_SALE(BigDecimal r25_AVAILABLE_FOR_SALE) {
			this.R25_AVAILABLE_FOR_SALE = r25_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR25_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r25_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R25_QUALIFYING_FOR_HEDGE_ACCOUNTING = r25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			this.R25_TOTAL = r25_TOTAL;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			this.R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_HELD_FOR_TRADING() {
			return R26_HELD_FOR_TRADING;
		}

		public void setR26_HELD_FOR_TRADING(BigDecimal r26_HELD_FOR_TRADING) {
			this.R26_HELD_FOR_TRADING = r26_HELD_FOR_TRADING;
		}

		public BigDecimal getR26_AMORTISED_COST() {
			return R26_AMORTISED_COST;
		}

		public void setR26_AMORTISED_COST(BigDecimal r26_AMORTISED_COST) {
			this.R26_AMORTISED_COST = r26_AMORTISED_COST;
		}

		public BigDecimal getR26_AVAILABLE_FOR_SALE() {
			return R26_AVAILABLE_FOR_SALE;
		}

		public void setR26_AVAILABLE_FOR_SALE(BigDecimal r26_AVAILABLE_FOR_SALE) {
			this.R26_AVAILABLE_FOR_SALE = r26_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR26_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r26_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R26_QUALIFYING_FOR_HEDGE_ACCOUNTING = r26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			this.R26_TOTAL = r26_TOTAL;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			this.R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_HELD_FOR_TRADING() {
			return R27_HELD_FOR_TRADING;
		}

		public void setR27_HELD_FOR_TRADING(BigDecimal r27_HELD_FOR_TRADING) {
			this.R27_HELD_FOR_TRADING = r27_HELD_FOR_TRADING;
		}

		public BigDecimal getR27_AMORTISED_COST() {
			return R27_AMORTISED_COST;
		}

		public void setR27_AMORTISED_COST(BigDecimal r27_AMORTISED_COST) {
			this.R27_AMORTISED_COST = r27_AMORTISED_COST;
		}

		public BigDecimal getR27_AVAILABLE_FOR_SALE() {
			return R27_AVAILABLE_FOR_SALE;
		}

		public void setR27_AVAILABLE_FOR_SALE(BigDecimal r27_AVAILABLE_FOR_SALE) {
			this.R27_AVAILABLE_FOR_SALE = r27_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR27_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r27_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R27_QUALIFYING_FOR_HEDGE_ACCOUNTING = r27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			this.R27_TOTAL = r27_TOTAL;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			this.R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_HELD_FOR_TRADING() {
			return R28_HELD_FOR_TRADING;
		}

		public void setR28_HELD_FOR_TRADING(BigDecimal r28_HELD_FOR_TRADING) {
			this.R28_HELD_FOR_TRADING = r28_HELD_FOR_TRADING;
		}

		public BigDecimal getR28_AMORTISED_COST() {
			return R28_AMORTISED_COST;
		}

		public void setR28_AMORTISED_COST(BigDecimal r28_AMORTISED_COST) {
			this.R28_AMORTISED_COST = r28_AMORTISED_COST;
		}

		public BigDecimal getR28_AVAILABLE_FOR_SALE() {
			return R28_AVAILABLE_FOR_SALE;
		}

		public void setR28_AVAILABLE_FOR_SALE(BigDecimal r28_AVAILABLE_FOR_SALE) {
			this.R28_AVAILABLE_FOR_SALE = r28_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR28_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r28_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R28_QUALIFYING_FOR_HEDGE_ACCOUNTING = r28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			this.R28_TOTAL = r28_TOTAL;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			this.R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_HELD_FOR_TRADING() {
			return R29_HELD_FOR_TRADING;
		}

		public void setR29_HELD_FOR_TRADING(BigDecimal r29_HELD_FOR_TRADING) {
			this.R29_HELD_FOR_TRADING = r29_HELD_FOR_TRADING;
		}

		public BigDecimal getR29_AMORTISED_COST() {
			return R29_AMORTISED_COST;
		}

		public void setR29_AMORTISED_COST(BigDecimal r29_AMORTISED_COST) {
			this.R29_AMORTISED_COST = r29_AMORTISED_COST;
		}

		public BigDecimal getR29_AVAILABLE_FOR_SALE() {
			return R29_AVAILABLE_FOR_SALE;
		}

		public void setR29_AVAILABLE_FOR_SALE(BigDecimal r29_AVAILABLE_FOR_SALE) {
			this.R29_AVAILABLE_FOR_SALE = r29_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR29_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r29_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R29_QUALIFYING_FOR_HEDGE_ACCOUNTING = r29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR29_TOTAL() {
			return R29_TOTAL;
		}

		public void setR29_TOTAL(BigDecimal r29_TOTAL) {
			this.R29_TOTAL = r29_TOTAL;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			this.R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_HELD_FOR_TRADING() {
			return R30_HELD_FOR_TRADING;
		}

		public void setR30_HELD_FOR_TRADING(BigDecimal r30_HELD_FOR_TRADING) {
			this.R30_HELD_FOR_TRADING = r30_HELD_FOR_TRADING;
		}

		public BigDecimal getR30_AMORTISED_COST() {
			return R30_AMORTISED_COST;
		}

		public void setR30_AMORTISED_COST(BigDecimal r30_AMORTISED_COST) {
			this.R30_AMORTISED_COST = r30_AMORTISED_COST;
		}

		public BigDecimal getR30_AVAILABLE_FOR_SALE() {
			return R30_AVAILABLE_FOR_SALE;
		}

		public void setR30_AVAILABLE_FOR_SALE(BigDecimal r30_AVAILABLE_FOR_SALE) {
			this.R30_AVAILABLE_FOR_SALE = r30_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR30_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r30_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R30_QUALIFYING_FOR_HEDGE_ACCOUNTING = r30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR30_TOTAL() {
			return R30_TOTAL;
		}

		public void setR30_TOTAL(BigDecimal r30_TOTAL) {
			this.R30_TOTAL = r30_TOTAL;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			this.R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_HELD_FOR_TRADING() {
			return R31_HELD_FOR_TRADING;
		}

		public void setR31_HELD_FOR_TRADING(BigDecimal r31_HELD_FOR_TRADING) {
			this.R31_HELD_FOR_TRADING = r31_HELD_FOR_TRADING;
		}

		public BigDecimal getR31_AMORTISED_COST() {
			return R31_AMORTISED_COST;
		}

		public void setR31_AMORTISED_COST(BigDecimal r31_AMORTISED_COST) {
			this.R31_AMORTISED_COST = r31_AMORTISED_COST;
		}

		public BigDecimal getR31_AVAILABLE_FOR_SALE() {
			return R31_AVAILABLE_FOR_SALE;
		}

		public void setR31_AVAILABLE_FOR_SALE(BigDecimal r31_AVAILABLE_FOR_SALE) {
			this.R31_AVAILABLE_FOR_SALE = r31_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR31_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR31_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r31_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R31_QUALIFYING_FOR_HEDGE_ACCOUNTING = r31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR31_TOTAL() {
			return R31_TOTAL;
		}

		public void setR31_TOTAL(BigDecimal r31_TOTAL) {
			this.R31_TOTAL = r31_TOTAL;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			this.R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_HELD_FOR_TRADING() {
			return R32_HELD_FOR_TRADING;
		}

		public void setR32_HELD_FOR_TRADING(BigDecimal r32_HELD_FOR_TRADING) {
			this.R32_HELD_FOR_TRADING = r32_HELD_FOR_TRADING;
		}

		public BigDecimal getR32_AMORTISED_COST() {
			return R32_AMORTISED_COST;
		}

		public void setR32_AMORTISED_COST(BigDecimal r32_AMORTISED_COST) {
			this.R32_AMORTISED_COST = r32_AMORTISED_COST;
		}

		public BigDecimal getR32_AVAILABLE_FOR_SALE() {
			return R32_AVAILABLE_FOR_SALE;
		}

		public void setR32_AVAILABLE_FOR_SALE(BigDecimal r32_AVAILABLE_FOR_SALE) {
			this.R32_AVAILABLE_FOR_SALE = r32_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR32_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r32_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R32_QUALIFYING_FOR_HEDGE_ACCOUNTING = r32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR32_TOTAL() {
			return R32_TOTAL;
		}

		public void setR32_TOTAL(BigDecimal r32_TOTAL) {
			this.R32_TOTAL = r32_TOTAL;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			this.R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_HELD_FOR_TRADING() {
			return R33_HELD_FOR_TRADING;
		}

		public void setR33_HELD_FOR_TRADING(BigDecimal r33_HELD_FOR_TRADING) {
			this.R33_HELD_FOR_TRADING = r33_HELD_FOR_TRADING;
		}

		public BigDecimal getR33_AMORTISED_COST() {
			return R33_AMORTISED_COST;
		}

		public void setR33_AMORTISED_COST(BigDecimal r33_AMORTISED_COST) {
			this.R33_AMORTISED_COST = r33_AMORTISED_COST;
		}

		public BigDecimal getR33_AVAILABLE_FOR_SALE() {
			return R33_AVAILABLE_FOR_SALE;
		}

		public void setR33_AVAILABLE_FOR_SALE(BigDecimal r33_AVAILABLE_FOR_SALE) {
			this.R33_AVAILABLE_FOR_SALE = r33_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR33_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r33_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R33_QUALIFYING_FOR_HEDGE_ACCOUNTING = r33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR33_TOTAL() {
			return R33_TOTAL;
		}

		public void setR33_TOTAL(BigDecimal r33_TOTAL) {
			this.R33_TOTAL = r33_TOTAL;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			this.R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_HELD_FOR_TRADING() {
			return R34_HELD_FOR_TRADING;
		}

		public void setR34_HELD_FOR_TRADING(BigDecimal r34_HELD_FOR_TRADING) {
			this.R34_HELD_FOR_TRADING = r34_HELD_FOR_TRADING;
		}

		public BigDecimal getR34_AMORTISED_COST() {
			return R34_AMORTISED_COST;
		}

		public void setR34_AMORTISED_COST(BigDecimal r34_AMORTISED_COST) {
			this.R34_AMORTISED_COST = r34_AMORTISED_COST;
		}

		public BigDecimal getR34_AVAILABLE_FOR_SALE() {
			return R34_AVAILABLE_FOR_SALE;
		}

		public void setR34_AVAILABLE_FOR_SALE(BigDecimal r34_AVAILABLE_FOR_SALE) {
			this.R34_AVAILABLE_FOR_SALE = r34_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR34_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r34_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R34_QUALIFYING_FOR_HEDGE_ACCOUNTING = r34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR34_TOTAL() {
			return R34_TOTAL;
		}

		public void setR34_TOTAL(BigDecimal r34_TOTAL) {
			this.R34_TOTAL = r34_TOTAL;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			this.R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_HELD_FOR_TRADING() {
			return R35_HELD_FOR_TRADING;
		}

		public void setR35_HELD_FOR_TRADING(BigDecimal r35_HELD_FOR_TRADING) {
			this.R35_HELD_FOR_TRADING = r35_HELD_FOR_TRADING;
		}

		public BigDecimal getR35_AMORTISED_COST() {
			return R35_AMORTISED_COST;
		}

		public void setR35_AMORTISED_COST(BigDecimal r35_AMORTISED_COST) {
			this.R35_AMORTISED_COST = r35_AMORTISED_COST;
		}

		public BigDecimal getR35_AVAILABLE_FOR_SALE() {
			return R35_AVAILABLE_FOR_SALE;
		}

		public void setR35_AVAILABLE_FOR_SALE(BigDecimal r35_AVAILABLE_FOR_SALE) {
			this.R35_AVAILABLE_FOR_SALE = r35_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR35_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR35_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r35_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R35_QUALIFYING_FOR_HEDGE_ACCOUNTING = r35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR35_TOTAL() {
			return R35_TOTAL;
		}

		public void setR35_TOTAL(BigDecimal r35_TOTAL) {
			this.R35_TOTAL = r35_TOTAL;
		}

	}

	// ========================================
	// M_IS_Archival_Summary_Entity1 - ROW MAPPER
	// ==========================================

	public class M_IS_Archival_Summary_Entity1RowMapper implements RowMapper<M_IS_Archival_Summary_Entity1> {
		@Override
		public M_IS_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Archival_Summary_Entity1 obj = new M_IS_Archival_Summary_Entity1();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getString("report_version"));
			obj.setReport_resubdate(rs.getDate("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R10_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR10_HELD_TO_MATURITY(rs.getBigDecimal("R10_HELD_TO_MATURITY"));
			obj.setR10_AVAILABLE_FOR_SALE(rs.getBigDecimal("R10_AVAILABLE_FOR_SALE"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R11_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR11_HELD_TO_MATURITY(rs.getBigDecimal("R11_HELD_TO_MATURITY"));
			obj.setR11_AVAILABLE_FOR_SALE(rs.getBigDecimal("R11_AVAILABLE_FOR_SALE"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R12_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR12_HELD_TO_MATURITY(rs.getBigDecimal("R12_HELD_TO_MATURITY"));
			obj.setR12_AVAILABLE_FOR_SALE(rs.getBigDecimal("R12_AVAILABLE_FOR_SALE"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R13_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR13_HELD_TO_MATURITY(rs.getBigDecimal("R13_HELD_TO_MATURITY"));
			obj.setR13_AVAILABLE_FOR_SALE(rs.getBigDecimal("R13_AVAILABLE_FOR_SALE"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R14_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR14_HELD_TO_MATURITY(rs.getBigDecimal("R14_HELD_TO_MATURITY"));
			obj.setR14_AVAILABLE_FOR_SALE(rs.getBigDecimal("R14_AVAILABLE_FOR_SALE"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R15_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR15_HELD_TO_MATURITY(rs.getBigDecimal("R15_HELD_TO_MATURITY"));
			obj.setR15_AVAILABLE_FOR_SALE(rs.getBigDecimal("R15_AVAILABLE_FOR_SALE"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R16
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_FAIR_VALUE_PROFIT_AND_LOSS(rs.getBigDecimal("R16_FAIR_VALUE_PROFIT_AND_LOSS"));
			obj.setR16_HELD_TO_MATURITY(rs.getBigDecimal("R16_HELD_TO_MATURITY"));
			obj.setR16_AVAILABLE_FOR_SALE(rs.getBigDecimal("R16_AVAILABLE_FOR_SALE"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			return obj;
		}
	}

	// ========================================
	// M_IS_Archival_Summary_Entity1 - ENTITY CLASS
	// ========================================

	public class M_IS_Archival_Summary_Entity1 {

		@Id
		@Temporal(TemporalType.DATE)
		private Date report_date;
		private String report_version;
		private Date report_resubdate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String R10_PRODUCT;
		private BigDecimal R10_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R10_HELD_TO_MATURITY;
		private BigDecimal R10_AVAILABLE_FOR_SALE;
		private BigDecimal R10_TOTAL;

		private String R11_PRODUCT;
		private BigDecimal R11_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R11_HELD_TO_MATURITY;
		private BigDecimal R11_AVAILABLE_FOR_SALE;
		private BigDecimal R11_TOTAL;

		private String R12_PRODUCT;
		private BigDecimal R12_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R12_HELD_TO_MATURITY;
		private BigDecimal R12_AVAILABLE_FOR_SALE;
		private BigDecimal R12_TOTAL;

		private String R13_PRODUCT;
		private BigDecimal R13_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R13_HELD_TO_MATURITY;
		private BigDecimal R13_AVAILABLE_FOR_SALE;
		private BigDecimal R13_TOTAL;

		private String R14_PRODUCT;
		private BigDecimal R14_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R14_HELD_TO_MATURITY;
		private BigDecimal R14_AVAILABLE_FOR_SALE;
		private BigDecimal R14_TOTAL;

		private String R15_PRODUCT;
		private BigDecimal R15_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R15_HELD_TO_MATURITY;
		private BigDecimal R15_AVAILABLE_FOR_SALE;
		private BigDecimal R15_TOTAL;

		private String R16_PRODUCT;
		private BigDecimal R16_FAIR_VALUE_PROFIT_AND_LOSS;
		private BigDecimal R16_HELD_TO_MATURITY;
		private BigDecimal R16_AVAILABLE_FOR_SALE;
		private BigDecimal R16_TOTAL;

		public M_IS_Archival_Summary_Entity1() {
			super();
		}

		// GETTERS AND SETTERS

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			this.R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R10_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR10_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r10_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R10_FAIR_VALUE_PROFIT_AND_LOSS = r10_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR10_HELD_TO_MATURITY() {
			return R10_HELD_TO_MATURITY;
		}

		public void setR10_HELD_TO_MATURITY(BigDecimal r10_HELD_TO_MATURITY) {
			this.R10_HELD_TO_MATURITY = r10_HELD_TO_MATURITY;
		}

		public BigDecimal getR10_AVAILABLE_FOR_SALE() {
			return R10_AVAILABLE_FOR_SALE;
		}

		public void setR10_AVAILABLE_FOR_SALE(BigDecimal r10_AVAILABLE_FOR_SALE) {
			this.R10_AVAILABLE_FOR_SALE = r10_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			this.R10_TOTAL = r10_TOTAL;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			this.R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R11_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR11_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r11_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R11_FAIR_VALUE_PROFIT_AND_LOSS = r11_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR11_HELD_TO_MATURITY() {
			return R11_HELD_TO_MATURITY;
		}

		public void setR11_HELD_TO_MATURITY(BigDecimal r11_HELD_TO_MATURITY) {
			this.R11_HELD_TO_MATURITY = r11_HELD_TO_MATURITY;
		}

		public BigDecimal getR11_AVAILABLE_FOR_SALE() {
			return R11_AVAILABLE_FOR_SALE;
		}

		public void setR11_AVAILABLE_FOR_SALE(BigDecimal r11_AVAILABLE_FOR_SALE) {
			this.R11_AVAILABLE_FOR_SALE = r11_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			this.R11_TOTAL = r11_TOTAL;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			this.R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R12_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR12_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r12_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R12_FAIR_VALUE_PROFIT_AND_LOSS = r12_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR12_HELD_TO_MATURITY() {
			return R12_HELD_TO_MATURITY;
		}

		public void setR12_HELD_TO_MATURITY(BigDecimal r12_HELD_TO_MATURITY) {
			this.R12_HELD_TO_MATURITY = r12_HELD_TO_MATURITY;
		}

		public BigDecimal getR12_AVAILABLE_FOR_SALE() {
			return R12_AVAILABLE_FOR_SALE;
		}

		public void setR12_AVAILABLE_FOR_SALE(BigDecimal r12_AVAILABLE_FOR_SALE) {
			this.R12_AVAILABLE_FOR_SALE = r12_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			this.R12_TOTAL = r12_TOTAL;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			this.R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R13_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR13_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r13_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R13_FAIR_VALUE_PROFIT_AND_LOSS = r13_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR13_HELD_TO_MATURITY() {
			return R13_HELD_TO_MATURITY;
		}

		public void setR13_HELD_TO_MATURITY(BigDecimal r13_HELD_TO_MATURITY) {
			this.R13_HELD_TO_MATURITY = r13_HELD_TO_MATURITY;
		}

		public BigDecimal getR13_AVAILABLE_FOR_SALE() {
			return R13_AVAILABLE_FOR_SALE;
		}

		public void setR13_AVAILABLE_FOR_SALE(BigDecimal r13_AVAILABLE_FOR_SALE) {
			this.R13_AVAILABLE_FOR_SALE = r13_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			this.R13_TOTAL = r13_TOTAL;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			this.R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R14_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR14_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r14_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R14_FAIR_VALUE_PROFIT_AND_LOSS = r14_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR14_HELD_TO_MATURITY() {
			return R14_HELD_TO_MATURITY;
		}

		public void setR14_HELD_TO_MATURITY(BigDecimal r14_HELD_TO_MATURITY) {
			this.R14_HELD_TO_MATURITY = r14_HELD_TO_MATURITY;
		}

		public BigDecimal getR14_AVAILABLE_FOR_SALE() {
			return R14_AVAILABLE_FOR_SALE;
		}

		public void setR14_AVAILABLE_FOR_SALE(BigDecimal r14_AVAILABLE_FOR_SALE) {
			this.R14_AVAILABLE_FOR_SALE = r14_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			this.R14_TOTAL = r14_TOTAL;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			this.R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R15_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR15_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r15_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R15_FAIR_VALUE_PROFIT_AND_LOSS = r15_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR15_HELD_TO_MATURITY() {
			return R15_HELD_TO_MATURITY;
		}

		public void setR15_HELD_TO_MATURITY(BigDecimal r15_HELD_TO_MATURITY) {
			this.R15_HELD_TO_MATURITY = r15_HELD_TO_MATURITY;
		}

		public BigDecimal getR15_AVAILABLE_FOR_SALE() {
			return R15_AVAILABLE_FOR_SALE;
		}

		public void setR15_AVAILABLE_FOR_SALE(BigDecimal r15_AVAILABLE_FOR_SALE) {
			this.R15_AVAILABLE_FOR_SALE = r15_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			this.R15_TOTAL = r15_TOTAL;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			this.R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_FAIR_VALUE_PROFIT_AND_LOSS() {
			return R16_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public void setR16_FAIR_VALUE_PROFIT_AND_LOSS(BigDecimal r16_FAIR_VALUE_PROFIT_AND_LOSS) {
			this.R16_FAIR_VALUE_PROFIT_AND_LOSS = r16_FAIR_VALUE_PROFIT_AND_LOSS;
		}

		public BigDecimal getR16_HELD_TO_MATURITY() {
			return R16_HELD_TO_MATURITY;
		}

		public void setR16_HELD_TO_MATURITY(BigDecimal r16_HELD_TO_MATURITY) {
			this.R16_HELD_TO_MATURITY = r16_HELD_TO_MATURITY;
		}

		public BigDecimal getR16_AVAILABLE_FOR_SALE() {
			return R16_AVAILABLE_FOR_SALE;
		}

		public void setR16_AVAILABLE_FOR_SALE(BigDecimal r16_AVAILABLE_FOR_SALE) {
			this.R16_AVAILABLE_FOR_SALE = r16_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal r16_TOTAL) {
			this.R16_TOTAL = r16_TOTAL;
		}

	}

// ========================================
// M_IS_Archival_Summary_Entity2 - ROW MAPPER
// ==========================================

	public class M_IS_Archival_Summary_Entity2RowMapper implements RowMapper<M_IS_Archival_Summary_Entity2> {
		@Override
		public M_IS_Archival_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Archival_Summary_Entity2 obj = new M_IS_Archival_Summary_Entity2();

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getString("report_version"));
			obj.setReport_resubdate(rs.getDate("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_HELD_FOR_TRADING(rs.getBigDecimal("R21_HELD_FOR_TRADING"));
			obj.setR21_AMORTISED_COST(rs.getBigDecimal("R21_AMORTISED_COST"));
			obj.setR21_AVAILABLE_FOR_SALE(rs.getBigDecimal("R21_AVAILABLE_FOR_SALE"));
			obj.setR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR21_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R21_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_HELD_FOR_TRADING(rs.getBigDecimal("R22_HELD_FOR_TRADING"));
			obj.setR22_AMORTISED_COST(rs.getBigDecimal("R22_AMORTISED_COST"));
			obj.setR22_AVAILABLE_FOR_SALE(rs.getBigDecimal("R22_AVAILABLE_FOR_SALE"));
			obj.setR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR22_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R22_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_HELD_FOR_TRADING(rs.getBigDecimal("R23_HELD_FOR_TRADING"));
			obj.setR23_AMORTISED_COST(rs.getBigDecimal("R23_AMORTISED_COST"));
			obj.setR23_AVAILABLE_FOR_SALE(rs.getBigDecimal("R23_AVAILABLE_FOR_SALE"));
			obj.setR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR23_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R23_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_HELD_FOR_TRADING(rs.getBigDecimal("R24_HELD_FOR_TRADING"));
			obj.setR24_AMORTISED_COST(rs.getBigDecimal("R24_AMORTISED_COST"));
			obj.setR24_AVAILABLE_FOR_SALE(rs.getBigDecimal("R24_AVAILABLE_FOR_SALE"));
			obj.setR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR24_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R24_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_HELD_FOR_TRADING(rs.getBigDecimal("R25_HELD_FOR_TRADING"));
			obj.setR25_AMORTISED_COST(rs.getBigDecimal("R25_AMORTISED_COST"));
			obj.setR25_AVAILABLE_FOR_SALE(rs.getBigDecimal("R25_AVAILABLE_FOR_SALE"));
			obj.setR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR25_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R25_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_HELD_FOR_TRADING(rs.getBigDecimal("R26_HELD_FOR_TRADING"));
			obj.setR26_AMORTISED_COST(rs.getBigDecimal("R26_AMORTISED_COST"));
			obj.setR26_AVAILABLE_FOR_SALE(rs.getBigDecimal("R26_AVAILABLE_FOR_SALE"));
			obj.setR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR26_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R26_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_HELD_FOR_TRADING(rs.getBigDecimal("R27_HELD_FOR_TRADING"));
			obj.setR27_AMORTISED_COST(rs.getBigDecimal("R27_AMORTISED_COST"));
			obj.setR27_AVAILABLE_FOR_SALE(rs.getBigDecimal("R27_AVAILABLE_FOR_SALE"));
			obj.setR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR27_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R27_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_HELD_FOR_TRADING(rs.getBigDecimal("R28_HELD_FOR_TRADING"));
			obj.setR28_AMORTISED_COST(rs.getBigDecimal("R28_AMORTISED_COST"));
			obj.setR28_AVAILABLE_FOR_SALE(rs.getBigDecimal("R28_AVAILABLE_FOR_SALE"));
			obj.setR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR28_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R28_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_HELD_FOR_TRADING(rs.getBigDecimal("R29_HELD_FOR_TRADING"));
			obj.setR29_AMORTISED_COST(rs.getBigDecimal("R29_AMORTISED_COST"));
			obj.setR29_AVAILABLE_FOR_SALE(rs.getBigDecimal("R29_AVAILABLE_FOR_SALE"));
			obj.setR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR29_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R29_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR29_TOTAL(rs.getBigDecimal("R29_TOTAL"));

			// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_HELD_FOR_TRADING(rs.getBigDecimal("R30_HELD_FOR_TRADING"));
			obj.setR30_AMORTISED_COST(rs.getBigDecimal("R30_AMORTISED_COST"));
			obj.setR30_AVAILABLE_FOR_SALE(rs.getBigDecimal("R30_AVAILABLE_FOR_SALE"));
			obj.setR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR30_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R30_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR30_TOTAL(rs.getBigDecimal("R30_TOTAL"));

			// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_HELD_FOR_TRADING(rs.getBigDecimal("R31_HELD_FOR_TRADING"));
			obj.setR31_AMORTISED_COST(rs.getBigDecimal("R31_AMORTISED_COST"));
			obj.setR31_AVAILABLE_FOR_SALE(rs.getBigDecimal("R31_AVAILABLE_FOR_SALE"));
			obj.setR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR31_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R31_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR31_TOTAL(rs.getBigDecimal("R31_TOTAL"));

			// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_HELD_FOR_TRADING(rs.getBigDecimal("R32_HELD_FOR_TRADING"));
			obj.setR32_AMORTISED_COST(rs.getBigDecimal("R32_AMORTISED_COST"));
			obj.setR32_AVAILABLE_FOR_SALE(rs.getBigDecimal("R32_AVAILABLE_FOR_SALE"));
			obj.setR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR32_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R32_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR32_TOTAL(rs.getBigDecimal("R32_TOTAL"));

			// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_HELD_FOR_TRADING(rs.getBigDecimal("R33_HELD_FOR_TRADING"));
			obj.setR33_AMORTISED_COST(rs.getBigDecimal("R33_AMORTISED_COST"));
			obj.setR33_AVAILABLE_FOR_SALE(rs.getBigDecimal("R33_AVAILABLE_FOR_SALE"));
			obj.setR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR33_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R33_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR33_TOTAL(rs.getBigDecimal("R33_TOTAL"));

			// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_HELD_FOR_TRADING(rs.getBigDecimal("R34_HELD_FOR_TRADING"));
			obj.setR34_AMORTISED_COST(rs.getBigDecimal("R34_AMORTISED_COST"));
			obj.setR34_AVAILABLE_FOR_SALE(rs.getBigDecimal("R34_AVAILABLE_FOR_SALE"));
			obj.setR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR34_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R34_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR34_TOTAL(rs.getBigDecimal("R34_TOTAL"));

			// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_HELD_FOR_TRADING(rs.getBigDecimal("R35_HELD_FOR_TRADING"));
			obj.setR35_AMORTISED_COST(rs.getBigDecimal("R35_AMORTISED_COST"));
			obj.setR35_AVAILABLE_FOR_SALE(rs.getBigDecimal("R35_AVAILABLE_FOR_SALE"));
			obj.setR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(rs.getBigDecimal("R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS"));
			obj.setR35_QUALIFYING_FOR_HEDGE_ACCOUNTING(rs.getBigDecimal("R35_QUALIFYING_FOR_HEDGE_ACCOUNTING"));
			obj.setR35_TOTAL(rs.getBigDecimal("R35_TOTAL"));

			return obj;
		}
	}

// ========================================
// M_IS_Archival_Summary_Entity2 - ENTITY CLASS
// ========================================

	public class M_IS_Archival_Summary_Entity2 {

		@Id
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Temporal(TemporalType.DATE)
		private Date report_date;
		private String report_version;
		private Date report_resubdate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private String R21_PRODUCT;
		private BigDecimal R21_HELD_FOR_TRADING;
		private BigDecimal R21_AMORTISED_COST;
		private BigDecimal R21_AVAILABLE_FOR_SALE;
		private BigDecimal R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R21_TOTAL;

		private String R22_PRODUCT;
		private BigDecimal R22_HELD_FOR_TRADING;
		private BigDecimal R22_AMORTISED_COST;
		private BigDecimal R22_AVAILABLE_FOR_SALE;
		private BigDecimal R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R22_TOTAL;

		private String R23_PRODUCT;
		private BigDecimal R23_HELD_FOR_TRADING;
		private BigDecimal R23_AMORTISED_COST;
		private BigDecimal R23_AVAILABLE_FOR_SALE;
		private BigDecimal R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R23_TOTAL;

		private String R24_PRODUCT;
		private BigDecimal R24_HELD_FOR_TRADING;
		private BigDecimal R24_AMORTISED_COST;
		private BigDecimal R24_AVAILABLE_FOR_SALE;
		private BigDecimal R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R24_TOTAL;

		private String R25_PRODUCT;
		private BigDecimal R25_HELD_FOR_TRADING;
		private BigDecimal R25_AMORTISED_COST;
		private BigDecimal R25_AVAILABLE_FOR_SALE;
		private BigDecimal R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R25_TOTAL;

		private String R26_PRODUCT;
		private BigDecimal R26_HELD_FOR_TRADING;
		private BigDecimal R26_AMORTISED_COST;
		private BigDecimal R26_AVAILABLE_FOR_SALE;
		private BigDecimal R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R26_TOTAL;

		private String R27_PRODUCT;
		private BigDecimal R27_HELD_FOR_TRADING;
		private BigDecimal R27_AMORTISED_COST;
		private BigDecimal R27_AVAILABLE_FOR_SALE;
		private BigDecimal R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R27_TOTAL;

		private String R28_PRODUCT;
		private BigDecimal R28_HELD_FOR_TRADING;
		private BigDecimal R28_AMORTISED_COST;
		private BigDecimal R28_AVAILABLE_FOR_SALE;
		private BigDecimal R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R28_TOTAL;

		private String R29_PRODUCT;
		private BigDecimal R29_HELD_FOR_TRADING;
		private BigDecimal R29_AMORTISED_COST;
		private BigDecimal R29_AVAILABLE_FOR_SALE;
		private BigDecimal R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R29_TOTAL;

		private String R30_PRODUCT;
		private BigDecimal R30_HELD_FOR_TRADING;
		private BigDecimal R30_AMORTISED_COST;
		private BigDecimal R30_AVAILABLE_FOR_SALE;
		private BigDecimal R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R30_TOTAL;

		private String R31_PRODUCT;
		private BigDecimal R31_HELD_FOR_TRADING;
		private BigDecimal R31_AMORTISED_COST;
		private BigDecimal R31_AVAILABLE_FOR_SALE;
		private BigDecimal R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R31_TOTAL;

		private String R32_PRODUCT;
		private BigDecimal R32_HELD_FOR_TRADING;
		private BigDecimal R32_AMORTISED_COST;
		private BigDecimal R32_AVAILABLE_FOR_SALE;
		private BigDecimal R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R32_TOTAL;

		private String R33_PRODUCT;
		private BigDecimal R33_HELD_FOR_TRADING;
		private BigDecimal R33_AMORTISED_COST;
		private BigDecimal R33_AVAILABLE_FOR_SALE;
		private BigDecimal R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R33_TOTAL;

		private String R34_PRODUCT;
		private BigDecimal R34_HELD_FOR_TRADING;
		private BigDecimal R34_AMORTISED_COST;
		private BigDecimal R34_AVAILABLE_FOR_SALE;
		private BigDecimal R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R34_TOTAL;

		private String R35_PRODUCT;
		private BigDecimal R35_HELD_FOR_TRADING;
		private BigDecimal R35_AMORTISED_COST;
		private BigDecimal R35_AVAILABLE_FOR_SALE;
		private BigDecimal R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		private BigDecimal R35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		private BigDecimal R35_TOTAL;

		public M_IS_Archival_Summary_Entity2() {
			super();
		}

		// GETTERS AND SETTERS

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_version() {
			return report_version;
		}

		public void setReport_version(String report_version) {
			this.report_version = report_version;
		}

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setReport_resubdate(Date report_resubdate) {
			this.report_resubdate = report_resubdate;
		}

		public String getReport_frequency() {
			return report_frequency;
		}

		public void setReport_frequency(String report_frequency) {
			this.report_frequency = report_frequency;
		}

		public String getReport_code() {
			return report_code;
		}

		public void setReport_code(String report_code) {
			this.report_code = report_code;
		}

		public String getReport_desc() {
			return report_desc;
		}

		public void setReport_desc(String report_desc) {
			this.report_desc = report_desc;
		}

		public String getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(String entity_flg) {
			this.entity_flg = entity_flg;
		}

		public String getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(String modify_flg) {
			this.modify_flg = modify_flg;
		}

		public String getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(String del_flg) {
			this.del_flg = del_flg;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			this.R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_HELD_FOR_TRADING() {
			return R21_HELD_FOR_TRADING;
		}

		public void setR21_HELD_FOR_TRADING(BigDecimal r21_HELD_FOR_TRADING) {
			this.R21_HELD_FOR_TRADING = r21_HELD_FOR_TRADING;
		}

		public BigDecimal getR21_AMORTISED_COST() {
			return R21_AMORTISED_COST;
		}

		public void setR21_AMORTISED_COST(BigDecimal r21_AMORTISED_COST) {
			this.R21_AMORTISED_COST = r21_AMORTISED_COST;
		}

		public BigDecimal getR21_AVAILABLE_FOR_SALE() {
			return R21_AVAILABLE_FOR_SALE;
		}

		public void setR21_AVAILABLE_FOR_SALE(BigDecimal r21_AVAILABLE_FOR_SALE) {
			this.R21_AVAILABLE_FOR_SALE = r21_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR21_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r21_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R21_QUALIFYING_FOR_HEDGE_ACCOUNTING = r21_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			this.R21_TOTAL = r21_TOTAL;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			this.R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_HELD_FOR_TRADING() {
			return R22_HELD_FOR_TRADING;
		}

		public void setR22_HELD_FOR_TRADING(BigDecimal r22_HELD_FOR_TRADING) {
			this.R22_HELD_FOR_TRADING = r22_HELD_FOR_TRADING;
		}

		public BigDecimal getR22_AMORTISED_COST() {
			return R22_AMORTISED_COST;
		}

		public void setR22_AMORTISED_COST(BigDecimal r22_AMORTISED_COST) {
			this.R22_AMORTISED_COST = r22_AMORTISED_COST;
		}

		public BigDecimal getR22_AVAILABLE_FOR_SALE() {
			return R22_AVAILABLE_FOR_SALE;
		}

		public void setR22_AVAILABLE_FOR_SALE(BigDecimal r22_AVAILABLE_FOR_SALE) {
			this.R22_AVAILABLE_FOR_SALE = r22_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR22_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r22_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R22_QUALIFYING_FOR_HEDGE_ACCOUNTING = r22_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			this.R22_TOTAL = r22_TOTAL;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			this.R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_HELD_FOR_TRADING() {
			return R23_HELD_FOR_TRADING;
		}

		public void setR23_HELD_FOR_TRADING(BigDecimal r23_HELD_FOR_TRADING) {
			this.R23_HELD_FOR_TRADING = r23_HELD_FOR_TRADING;
		}

		public BigDecimal getR23_AMORTISED_COST() {
			return R23_AMORTISED_COST;
		}

		public void setR23_AMORTISED_COST(BigDecimal r23_AMORTISED_COST) {
			this.R23_AMORTISED_COST = r23_AMORTISED_COST;
		}

		public BigDecimal getR23_AVAILABLE_FOR_SALE() {
			return R23_AVAILABLE_FOR_SALE;
		}

		public void setR23_AVAILABLE_FOR_SALE(BigDecimal r23_AVAILABLE_FOR_SALE) {
			this.R23_AVAILABLE_FOR_SALE = r23_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR23_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r23_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R23_QUALIFYING_FOR_HEDGE_ACCOUNTING = r23_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			this.R23_TOTAL = r23_TOTAL;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			this.R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_HELD_FOR_TRADING() {
			return R24_HELD_FOR_TRADING;
		}

		public void setR24_HELD_FOR_TRADING(BigDecimal r24_HELD_FOR_TRADING) {
			this.R24_HELD_FOR_TRADING = r24_HELD_FOR_TRADING;
		}

		public BigDecimal getR24_AMORTISED_COST() {
			return R24_AMORTISED_COST;
		}

		public void setR24_AMORTISED_COST(BigDecimal r24_AMORTISED_COST) {
			this.R24_AMORTISED_COST = r24_AMORTISED_COST;
		}

		public BigDecimal getR24_AVAILABLE_FOR_SALE() {
			return R24_AVAILABLE_FOR_SALE;
		}

		public void setR24_AVAILABLE_FOR_SALE(BigDecimal r24_AVAILABLE_FOR_SALE) {
			this.R24_AVAILABLE_FOR_SALE = r24_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR24_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r24_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R24_QUALIFYING_FOR_HEDGE_ACCOUNTING = r24_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			this.R24_TOTAL = r24_TOTAL;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			this.R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_HELD_FOR_TRADING() {
			return R25_HELD_FOR_TRADING;
		}

		public void setR25_HELD_FOR_TRADING(BigDecimal r25_HELD_FOR_TRADING) {
			this.R25_HELD_FOR_TRADING = r25_HELD_FOR_TRADING;
		}

		public BigDecimal getR25_AMORTISED_COST() {
			return R25_AMORTISED_COST;
		}

		public void setR25_AMORTISED_COST(BigDecimal r25_AMORTISED_COST) {
			this.R25_AMORTISED_COST = r25_AMORTISED_COST;
		}

		public BigDecimal getR25_AVAILABLE_FOR_SALE() {
			return R25_AVAILABLE_FOR_SALE;
		}

		public void setR25_AVAILABLE_FOR_SALE(BigDecimal r25_AVAILABLE_FOR_SALE) {
			this.R25_AVAILABLE_FOR_SALE = r25_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR25_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r25_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R25_QUALIFYING_FOR_HEDGE_ACCOUNTING = r25_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			this.R25_TOTAL = r25_TOTAL;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			this.R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_HELD_FOR_TRADING() {
			return R26_HELD_FOR_TRADING;
		}

		public void setR26_HELD_FOR_TRADING(BigDecimal r26_HELD_FOR_TRADING) {
			this.R26_HELD_FOR_TRADING = r26_HELD_FOR_TRADING;
		}

		public BigDecimal getR26_AMORTISED_COST() {
			return R26_AMORTISED_COST;
		}

		public void setR26_AMORTISED_COST(BigDecimal r26_AMORTISED_COST) {
			this.R26_AMORTISED_COST = r26_AMORTISED_COST;
		}

		public BigDecimal getR26_AVAILABLE_FOR_SALE() {
			return R26_AVAILABLE_FOR_SALE;
		}

		public void setR26_AVAILABLE_FOR_SALE(BigDecimal r26_AVAILABLE_FOR_SALE) {
			this.R26_AVAILABLE_FOR_SALE = r26_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR26_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r26_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R26_QUALIFYING_FOR_HEDGE_ACCOUNTING = r26_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			this.R26_TOTAL = r26_TOTAL;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			this.R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_HELD_FOR_TRADING() {
			return R27_HELD_FOR_TRADING;
		}

		public void setR27_HELD_FOR_TRADING(BigDecimal r27_HELD_FOR_TRADING) {
			this.R27_HELD_FOR_TRADING = r27_HELD_FOR_TRADING;
		}

		public BigDecimal getR27_AMORTISED_COST() {
			return R27_AMORTISED_COST;
		}

		public void setR27_AMORTISED_COST(BigDecimal r27_AMORTISED_COST) {
			this.R27_AMORTISED_COST = r27_AMORTISED_COST;
		}

		public BigDecimal getR27_AVAILABLE_FOR_SALE() {
			return R27_AVAILABLE_FOR_SALE;
		}

		public void setR27_AVAILABLE_FOR_SALE(BigDecimal r27_AVAILABLE_FOR_SALE) {
			this.R27_AVAILABLE_FOR_SALE = r27_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR27_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r27_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R27_QUALIFYING_FOR_HEDGE_ACCOUNTING = r27_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			this.R27_TOTAL = r27_TOTAL;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			this.R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_HELD_FOR_TRADING() {
			return R28_HELD_FOR_TRADING;
		}

		public void setR28_HELD_FOR_TRADING(BigDecimal r28_HELD_FOR_TRADING) {
			this.R28_HELD_FOR_TRADING = r28_HELD_FOR_TRADING;
		}

		public BigDecimal getR28_AMORTISED_COST() {
			return R28_AMORTISED_COST;
		}

		public void setR28_AMORTISED_COST(BigDecimal r28_AMORTISED_COST) {
			this.R28_AMORTISED_COST = r28_AMORTISED_COST;
		}

		public BigDecimal getR28_AVAILABLE_FOR_SALE() {
			return R28_AVAILABLE_FOR_SALE;
		}

		public void setR28_AVAILABLE_FOR_SALE(BigDecimal r28_AVAILABLE_FOR_SALE) {
			this.R28_AVAILABLE_FOR_SALE = r28_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR28_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r28_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R28_QUALIFYING_FOR_HEDGE_ACCOUNTING = r28_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			this.R28_TOTAL = r28_TOTAL;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			this.R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_HELD_FOR_TRADING() {
			return R29_HELD_FOR_TRADING;
		}

		public void setR29_HELD_FOR_TRADING(BigDecimal r29_HELD_FOR_TRADING) {
			this.R29_HELD_FOR_TRADING = r29_HELD_FOR_TRADING;
		}

		public BigDecimal getR29_AMORTISED_COST() {
			return R29_AMORTISED_COST;
		}

		public void setR29_AMORTISED_COST(BigDecimal r29_AMORTISED_COST) {
			this.R29_AMORTISED_COST = r29_AMORTISED_COST;
		}

		public BigDecimal getR29_AVAILABLE_FOR_SALE() {
			return R29_AVAILABLE_FOR_SALE;
		}

		public void setR29_AVAILABLE_FOR_SALE(BigDecimal r29_AVAILABLE_FOR_SALE) {
			this.R29_AVAILABLE_FOR_SALE = r29_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR29_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r29_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R29_QUALIFYING_FOR_HEDGE_ACCOUNTING = r29_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR29_TOTAL() {
			return R29_TOTAL;
		}

		public void setR29_TOTAL(BigDecimal r29_TOTAL) {
			this.R29_TOTAL = r29_TOTAL;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			this.R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_HELD_FOR_TRADING() {
			return R30_HELD_FOR_TRADING;
		}

		public void setR30_HELD_FOR_TRADING(BigDecimal r30_HELD_FOR_TRADING) {
			this.R30_HELD_FOR_TRADING = r30_HELD_FOR_TRADING;
		}

		public BigDecimal getR30_AMORTISED_COST() {
			return R30_AMORTISED_COST;
		}

		public void setR30_AMORTISED_COST(BigDecimal r30_AMORTISED_COST) {
			this.R30_AMORTISED_COST = r30_AMORTISED_COST;
		}

		public BigDecimal getR30_AVAILABLE_FOR_SALE() {
			return R30_AVAILABLE_FOR_SALE;
		}

		public void setR30_AVAILABLE_FOR_SALE(BigDecimal r30_AVAILABLE_FOR_SALE) {
			this.R30_AVAILABLE_FOR_SALE = r30_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR30_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r30_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R30_QUALIFYING_FOR_HEDGE_ACCOUNTING = r30_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR30_TOTAL() {
			return R30_TOTAL;
		}

		public void setR30_TOTAL(BigDecimal r30_TOTAL) {
			this.R30_TOTAL = r30_TOTAL;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			this.R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_HELD_FOR_TRADING() {
			return R31_HELD_FOR_TRADING;
		}

		public void setR31_HELD_FOR_TRADING(BigDecimal r31_HELD_FOR_TRADING) {
			this.R31_HELD_FOR_TRADING = r31_HELD_FOR_TRADING;
		}

		public BigDecimal getR31_AMORTISED_COST() {
			return R31_AMORTISED_COST;
		}

		public void setR31_AMORTISED_COST(BigDecimal r31_AMORTISED_COST) {
			this.R31_AMORTISED_COST = r31_AMORTISED_COST;
		}

		public BigDecimal getR31_AVAILABLE_FOR_SALE() {
			return R31_AVAILABLE_FOR_SALE;
		}

		public void setR31_AVAILABLE_FOR_SALE(BigDecimal r31_AVAILABLE_FOR_SALE) {
			this.R31_AVAILABLE_FOR_SALE = r31_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r31_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR31_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR31_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r31_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R31_QUALIFYING_FOR_HEDGE_ACCOUNTING = r31_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR31_TOTAL() {
			return R31_TOTAL;
		}

		public void setR31_TOTAL(BigDecimal r31_TOTAL) {
			this.R31_TOTAL = r31_TOTAL;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			this.R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_HELD_FOR_TRADING() {
			return R32_HELD_FOR_TRADING;
		}

		public void setR32_HELD_FOR_TRADING(BigDecimal r32_HELD_FOR_TRADING) {
			this.R32_HELD_FOR_TRADING = r32_HELD_FOR_TRADING;
		}

		public BigDecimal getR32_AMORTISED_COST() {
			return R32_AMORTISED_COST;
		}

		public void setR32_AMORTISED_COST(BigDecimal r32_AMORTISED_COST) {
			this.R32_AMORTISED_COST = r32_AMORTISED_COST;
		}

		public BigDecimal getR32_AVAILABLE_FOR_SALE() {
			return R32_AVAILABLE_FOR_SALE;
		}

		public void setR32_AVAILABLE_FOR_SALE(BigDecimal r32_AVAILABLE_FOR_SALE) {
			this.R32_AVAILABLE_FOR_SALE = r32_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR32_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r32_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R32_QUALIFYING_FOR_HEDGE_ACCOUNTING = r32_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR32_TOTAL() {
			return R32_TOTAL;
		}

		public void setR32_TOTAL(BigDecimal r32_TOTAL) {
			this.R32_TOTAL = r32_TOTAL;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			this.R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_HELD_FOR_TRADING() {
			return R33_HELD_FOR_TRADING;
		}

		public void setR33_HELD_FOR_TRADING(BigDecimal r33_HELD_FOR_TRADING) {
			this.R33_HELD_FOR_TRADING = r33_HELD_FOR_TRADING;
		}

		public BigDecimal getR33_AMORTISED_COST() {
			return R33_AMORTISED_COST;
		}

		public void setR33_AMORTISED_COST(BigDecimal r33_AMORTISED_COST) {
			this.R33_AMORTISED_COST = r33_AMORTISED_COST;
		}

		public BigDecimal getR33_AVAILABLE_FOR_SALE() {
			return R33_AVAILABLE_FOR_SALE;
		}

		public void setR33_AVAILABLE_FOR_SALE(BigDecimal r33_AVAILABLE_FOR_SALE) {
			this.R33_AVAILABLE_FOR_SALE = r33_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR33_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r33_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R33_QUALIFYING_FOR_HEDGE_ACCOUNTING = r33_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR33_TOTAL() {
			return R33_TOTAL;
		}

		public void setR33_TOTAL(BigDecimal r33_TOTAL) {
			this.R33_TOTAL = r33_TOTAL;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			this.R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_HELD_FOR_TRADING() {
			return R34_HELD_FOR_TRADING;
		}

		public void setR34_HELD_FOR_TRADING(BigDecimal r34_HELD_FOR_TRADING) {
			this.R34_HELD_FOR_TRADING = r34_HELD_FOR_TRADING;
		}

		public BigDecimal getR34_AMORTISED_COST() {
			return R34_AMORTISED_COST;
		}

		public void setR34_AMORTISED_COST(BigDecimal r34_AMORTISED_COST) {
			this.R34_AMORTISED_COST = r34_AMORTISED_COST;
		}

		public BigDecimal getR34_AVAILABLE_FOR_SALE() {
			return R34_AVAILABLE_FOR_SALE;
		}

		public void setR34_AVAILABLE_FOR_SALE(BigDecimal r34_AVAILABLE_FOR_SALE) {
			this.R34_AVAILABLE_FOR_SALE = r34_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR34_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r34_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R34_QUALIFYING_FOR_HEDGE_ACCOUNTING = r34_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR34_TOTAL() {
			return R34_TOTAL;
		}

		public void setR34_TOTAL(BigDecimal r34_TOTAL) {
			this.R34_TOTAL = r34_TOTAL;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			this.R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_HELD_FOR_TRADING() {
			return R35_HELD_FOR_TRADING;
		}

		public void setR35_HELD_FOR_TRADING(BigDecimal r35_HELD_FOR_TRADING) {
			this.R35_HELD_FOR_TRADING = r35_HELD_FOR_TRADING;
		}

		public BigDecimal getR35_AMORTISED_COST() {
			return R35_AMORTISED_COST;
		}

		public void setR35_AMORTISED_COST(BigDecimal r35_AMORTISED_COST) {
			this.R35_AMORTISED_COST = r35_AMORTISED_COST;
		}

		public BigDecimal getR35_AVAILABLE_FOR_SALE() {
			return R35_AVAILABLE_FOR_SALE;
		}

		public void setR35_AVAILABLE_FOR_SALE(BigDecimal r35_AVAILABLE_FOR_SALE) {
			this.R35_AVAILABLE_FOR_SALE = r35_AVAILABLE_FOR_SALE;
		}

		public BigDecimal getR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() {
			return R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public void setR35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS(BigDecimal r35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS) {
			this.R35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS = r35_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS;
		}

		public BigDecimal getR35_QUALIFYING_FOR_HEDGE_ACCOUNTING() {
			return R35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public void setR35_QUALIFYING_FOR_HEDGE_ACCOUNTING(BigDecimal r35_QUALIFYING_FOR_HEDGE_ACCOUNTING) {
			this.R35_QUALIFYING_FOR_HEDGE_ACCOUNTING = r35_QUALIFYING_FOR_HEDGE_ACCOUNTING;
		}

		public BigDecimal getR35_TOTAL() {
			return R35_TOTAL;
		}

		public void setR35_TOTAL(BigDecimal r35_TOTAL) {
			this.R35_TOTAL = r35_TOTAL;
		}

	}

// ========================================
// M_IS_Detail_Entity - ROW MAPPER
// ==========================================

	public class M_IS_Detail_EntityRowMapper implements RowMapper<M_IS_Detail_Entity> {
		@Override
		public M_IS_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Detail_Entity obj = new M_IS_Detail_Entity();

			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : null);
			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : null);
			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : null);

			return obj;
		}
	}

// ========================================
// M_IS_Detail_Entity - ENTITY CLASS
// ========================================

	public class M_IS_Detail_Entity {

		@Id

		private Long sno;

		private String custId;
		private String acctNumber;

		private String acctName;

		private String dataType;

		private String reportAddlCriteria_1;

		private String reportLabel;

		private String reportRemarks;

		private String modificationRemarks;

		private String dataEntryVersion;

		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		private String reportName;

		private String createUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		private String modifyUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		private String verifyUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		private Character entityFlg;

		private Character modifyFlg;

		private Character delFlg;

		public M_IS_Detail_Entity() {
			super();
		}

		// GETTERS AND SETTERS

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria_1() {
			return reportAddlCriteria_1;
		}

		public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
			this.reportAddlCriteria_1 = reportAddlCriteria_1;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public Character getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(Character entityFlg) {
			this.entityFlg = entityFlg;
		}

		public Character getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(Character modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public Character getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(Character delFlg) {
			this.delFlg = delFlg;
		}
	}

// ========================================
// M_IS_Archival_Detail_Entity - ROW MAPPER
// ==========================================

	public class M_IS_Archival_Detail_EntityRowMapper implements RowMapper<M_IS_Archival_Detail_Entity> {
		@Override
		public M_IS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_IS_Archival_Detail_Entity obj = new M_IS_Archival_Detail_Entity();

			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setCreateUser(rs.getString("CREATE_USER"));
			obj.setCreateTime(rs.getTimestamp("CREATE_TIME"));
			obj.setModifyUser(rs.getString("MODIFY_USER"));
			obj.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerifyUser(rs.getString("VERIFY_USER"));
			obj.setVerifyTime(rs.getTimestamp("VERIFY_TIME"));
			obj.setEntityFlg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : null);
			obj.setModifyFlg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : null);
			obj.setDelFlg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : null);

			return obj;
		}
	}

// ========================================
// M_IS_Archival_Detail_Entity - ENTITY CLASS
// ========================================

	public class M_IS_Archival_Detail_Entity {

		@Id
		private Long sno;

		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportAddlCriteria_1;
		private String reportLabel;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		private String reportName;
		private String createUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		private String modifyUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		private String verifyUser;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		private Character entityFlg;
		private Character modifyFlg;
		private Character delFlg;

		public M_IS_Archival_Detail_Entity() {
			super();
		}

		// GETTERS AND SETTERS

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

		public String getCustId() {
			return custId;
		}

		public void setCustId(String custId) {
			this.custId = custId;
		}

		public String getAcctNumber() {
			return acctNumber;
		}

		public void setAcctNumber(String acctNumber) {
			this.acctNumber = acctNumber;
		}

		public String getAcctName() {
			return acctName;
		}

		public void setAcctName(String acctName) {
			this.acctName = acctName;
		}

		public String getDataType() {
			return dataType;
		}

		public void setDataType(String dataType) {
			this.dataType = dataType;
		}

		public String getReportAddlCriteria_1() {
			return reportAddlCriteria_1;
		}

		public void setReportAddlCriteria_1(String reportAddlCriteria_1) {
			this.reportAddlCriteria_1 = reportAddlCriteria_1;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public String getModificationRemarks() {
			return modificationRemarks;
		}

		public void setModificationRemarks(String modificationRemarks) {
			this.modificationRemarks = modificationRemarks;
		}

		public String getDataEntryVersion() {
			return dataEntryVersion;
		}

		public void setDataEntryVersion(String dataEntryVersion) {
			this.dataEntryVersion = dataEntryVersion;
		}

		public BigDecimal getAcctBalanceInpula() {
			return acctBalanceInpula;
		}

		public void setAcctBalanceInpula(BigDecimal acctBalanceInpula) {
			this.acctBalanceInpula = acctBalanceInpula;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getCreateUser() {
			return createUser;
		}

		public void setCreateUser(String createUser) {
			this.createUser = createUser;
		}

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		public String getModifyUser() {
			return modifyUser;
		}

		public void setModifyUser(String modifyUser) {
			this.modifyUser = modifyUser;
		}

		public Date getModifyTime() {
			return modifyTime;
		}

		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}

		public String getVerifyUser() {
			return verifyUser;
		}

		public void setVerifyUser(String verifyUser) {
			this.verifyUser = verifyUser;
		}

		public Date getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(Date verifyTime) {
			this.verifyTime = verifyTime;
		}

		public Character getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(Character entityFlg) {
			this.entityFlg = entityFlg;
		}

		public Character getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(Character modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public Character getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(Character delFlg) {
			this.delFlg = delFlg;
		}
	}

	// =============================================================================
	// =================================== SEE NEXT ==============================
	// ====================================================================

	public ModelAndView getM_ISView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Date d1 = parseFlexibleDate(todate != null && !todate.isEmpty() ? todate : fromdate);
		String formattedDdmmyyyy = d1 != null ? new SimpleDateFormat("dd/MM/yyyy").format(d1) : todate;

		try {
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {
				List<M_IS_Archival_Summary_Entity1> T1Master = archivalSummary1_getdatabydateListarchival(d1, version);
				List<M_IS_Archival_Summary_Entity2> T2Master = archivalSummary2_getdatabydateListarchival(d1, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("allowdetail", getishighestversion(d1, version));
			} else {
				List<M_IS_Summary_Entity1> T1Master = summary1_getdatabydateList(d1);
				List<M_IS_Summary_Entity2> T2Master = summary2_getdatabydateList(d1);

				System.out.println("T1Master Size: " + T1Master.size());
				System.out.println("T2Master Size: " + T2Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("allowdetail", "YES");
			}

			mv.setViewName("BRRS/M_IS");
			System.out.println("View set: " + mv.getViewName());

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("error", "An error occurred while fetching M_IS data.");
		}

		mv.addObject("displaymode", "summary");
		mv.addObject("reportId", reportId);
		mv.addObject("reportid", reportId);
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version != null ? version.toString() : "");
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		return mv;
	}

	public ModelAndView getM_IScurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {
		int pageSize = 10;
		int currentPage = 0;
		if (pageable != null) {
			pageSize = pageable.getPageSize();
			currentPage = pageable.getPageNumber();
		}

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Date parsedDate = parseFlexibleDate(todate != null && !todate.isEmpty() ? todate : fromdate);
		String formattedDdmmyyyy = parsedDate != null ? new SimpleDateFormat("dd/MM/yyyy").format(parsedDate) : todate;

		try {
			String rowId = null;
			String columnId = null;

			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {
				List<M_IS_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
							new Object[] { rowId, columnId, parsedDate }, new M_IS_Archival_Detail_EntityRowMapper());
				} else {
					T1Dt1 = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
							new Object[] { parsedDate }, new M_IS_Archival_Detail_EntityRowMapper());
				}

				if (T1Dt1 == null || T1Dt1.isEmpty()) {
					if (rowId != null && columnId != null) {
						T1Dt1 = jdbcTemplate.query(
								"SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
								new Object[] { rowId, columnId, parsedDate },
								new M_IS_Archival_Detail_EntityRowMapper());
					} else {
						T1Dt1 = jdbcTemplate.query(
								"SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
								new Object[] { parsedDate }, new M_IS_Archival_Detail_EntityRowMapper());
					}
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				try {
					mv.addObject("allowdetail", getishighestversion(parsedDate, new BigDecimal(version)));
				} catch (Exception ex) {
					mv.addObject("allowdetail", "NO");
				}
				System.out.println(type + " DETAIL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			} else {
				List<M_IS_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					T1Dt1 = detail_GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					T1Dt1 = detail_getdatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				mv.addObject("allowdetail", "YES");
				System.out.println("DETAIL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		mv.setViewName("BRRS/M_IS");
		mv.addObject("displaymode", "Details");
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);
		mv.addObject("reportId", reportId);
		mv.addObject("reportid", reportId);
		mv.addObject("currency", currency);
		mv.addObject("dtltype", dtltype);
		mv.addObject("type", type);
		mv.addObject("version", version != null ? version : "");
		mv.addObject("asondate", formattedDdmmyyyy);
		mv.addObject("fromdate", formattedDdmmyyyy);
		mv.addObject("todate", formattedDdmmyyyy);

		return mv;
	}

	public void MISUpdate1(M_IS_Summary_Entity1 updatedEntity) {

		List<M_IS_Summary_Entity1> results = summary1_getdatabydateList(updatedEntity.getReport_date());
		if (results.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		String[] fields = { "PRODUCT", "FAIR_VALUE_PROFIT_AND_LOSS", "HELD_TO_MATURITY", "AVAILABLE_FOR_SALE",
				"TOTAL" };

		StringBuilder sql = new StringBuilder("UPDATE BRRS_M_IS_SUMMARYTABLE1 SET ");
		List<Object> params = new ArrayList<>();

		for (int i = 10; i <= 16; i++) {
			for (String field : fields) {
				String column = "R" + i + "_" + field;
				String getterName = "getR" + i + "_" + field;

				try {
					Method getter = M_IS_Summary_Entity1.class.getMethod(getterName);
					Object value = getter.invoke(updatedEntity);

					sql.append(column).append(" = ?, ");
					params.add(value);
				} catch (Exception ex) {
					// Skip missing fields
				}
			}
		}

		// remove trailing ", "
		sql.setLength(sql.length() - 2);
		sql.append(" WHERE REPORT_DATE = ?");
		params.add(updatedEntity.getReport_date());

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	public void MISUpdate2(M_IS_Summary_Entity2 updatedEntity) {
		// optional existence check
		List<M_IS_Summary_Entity2> results = summary2_getdatabydateList(updatedEntity.getReport_date());
		if (results.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		String[] fields = { "PRODUCT", "HELD_FOR_TRADING", "AMORTISED_COST", "AVAILABLE_FOR_SALE",
				"FAIR_VALUE_THROUGH_PROFIT_AND_LOSS", "QUALIFYING_FOR_HEDGE_ACCOUNTING", "TOTAL" };

		StringBuilder sql = new StringBuilder("UPDATE BRRS_M_IS_SUMMARYTABLE2 SET ");
		List<Object> params = new ArrayList<>();

		for (int i = 21; i <= 35; i++) {
			for (String field : fields) {
				if (i == 28 && field.equals("HELD_FOR_TRADING")) {
					continue;
				}

				String column = "R" + i + "_" + field;
				String getterName = "getR" + i + "_" + field;

				try {
					Method getter = M_IS_Summary_Entity2.class.getMethod(getterName);
					Object value = getter.invoke(updatedEntity);

					sql.append(column).append(" = ?, ");
					params.add(value);
				} catch (Exception ex) {
					// Skip missing fields
				}
			}
		}

		sql.setLength(sql.length() - 2);
		sql.append(" WHERE REPORT_DATE = ?");
		params.add(updatedEntity.getReport_date());

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode) {
		return getViewOrEditPage(SNO, formMode, null, null);
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		return getViewOrEditPage(SNO, formMode, type, null);
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("BRRS/M_IS");

		Date reportDate = null;
		String acctNo = null;
		if (request != null) {
			String asondateParam = request.getParameter("asondate");
			if (asondateParam == null) asondateParam = request.getParameter("reportDate");
			if (asondateParam == null) asondateParam = request.getParameter("todate");
			if (asondateParam != null && !asondateParam.trim().isEmpty()) {
				reportDate = parseFlexibleDate(asondateParam.trim());
			}
			acctNo = request.getParameter("acctNo");
		}
		if (acctNo == null || acctNo.trim().isEmpty()) {
			acctNo = SNO;
		}

		System.out.println("getViewOrEditPage: SNO=" + SNO + ", acctNo=" + acctNo + ", reportDate=" + reportDate + ", type=" + type);

		M_IS_Detail_Entity la1Entity = null;

		if ("RESUB".equals(type) || "ARCHIVAL".equals(type)) {
			System.out.println("Inside RESUB/ARCHIVAL FETCH for date: " + reportDate);
			if (acctNo != null && !acctNo.trim().isEmpty()) {
				la1Entity = findByAcctNumberArch(acctNo.trim(), reportDate);
			}
			if (la1Entity == null && SNO != null && !SNO.trim().isEmpty()) {
				la1Entity = findByAcctNumberArch(SNO.trim(), reportDate);
			}
		} else {
			if (reportDate != null && acctNo != null && !acctNo.trim().isEmpty()) {
				try {
					List<M_IS_Detail_Entity> list = jdbcTemplate.query(
							"SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
							new Object[] { acctNo.trim(), reportDate }, new M_IS_Detail_EntityRowMapper());
					if (list != null && !list.isEmpty()) {
						la1Entity = list.get(0);
					}
				} catch (Exception ignored) {
				}
			}
			if (la1Entity == null && SNO != null && !SNO.trim().isEmpty()) {
				la1Entity = detail_findBySno(SNO);
			}
		}

		if (la1Entity != null) {
			if (reportDate != null && (la1Entity.getReportDate() == null || !la1Entity.getReportDate().equals(reportDate))) {
				la1Entity.setReportDate(reportDate);
			}
			Date d = la1Entity.getReportDate() != null ? la1Entity.getReportDate() : reportDate;
			if (d != null) {
				mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(d));
			}
		} else if (reportDate != null) {
			mv.addObject("asondate", new SimpleDateFormat("dd/MM/yyyy").format(reportDate));
		}

		mv.addObject("Data", la1Entity);
		mv.addObject("reportId", "M_IS");
		mv.addObject("reportid", "M_IS");
		mv.addObject("menu", "M_IS");
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public byte[] BRRS_M_ISDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_ISDetails...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("BRRS_M_ISDetails");

			// Common border style
			BorderStyle border = BorderStyle.THIN;
			// Header style (left aligned)
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

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);
			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
					"REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}
				sheet.setColumnWidth(i, 5000);
			}
			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_IS_Detail_Entity> reportData = detail_getdatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IS_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());
					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_IS— only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_ISExcel", e);
			return new byte[0];
		}
	}

	public byte[] BRRS_M_ISExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		System.out.println(type);
		System.out.println(version);
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_ISARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);

		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<M_IS_Archival_Summary_Entity1> dataList = archivalSummary1_getdatabydateListarchival(
					dateformat.parse(todate), version);
			List<M_IS_Archival_Summary_Entity2> dataList1 = archivalSummary2_getdatabydateListarchival(
					dateformat.parse(todate), version);
			// Generate Excel for RESUB
			return BRRS_M_ISResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		List<M_IS_Summary_Entity1> dataList = summary1_getdatabydateList(dateformat.parse(todate));
		List<M_IS_Summary_Entity2> dataList1 = summary2_getdatabydateList(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for brrs2.4 report. Returning empty result.");
			return new byte[0];
		}
		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
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

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");
			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 2 = Excel column B
				Cell dateCell = dateRow.getCell(1);

				if (dateCell == null) {
					dateCell = dateRow.createCell(1);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_IS_Summary_Entity1 record = dataList.get(i);
					M_IS_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row10
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR10_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR10_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row10
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR10_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR10_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row10
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR10_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR10_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR11_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR11_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR11_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR11_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR11_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR11_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR12_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR12_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR12_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR13_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR13_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR13_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR14_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR14_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR14_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR15_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR15_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR15_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR21_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR21_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR21_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR21_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR21_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR21_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					Cell cell6 = row.createCell(6);
					if (record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					// Column H
					Cell cell7 = row.createCell(7);
					if (record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR22_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR22_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR22_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR22_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR22_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR22_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR23_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR23_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR23_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR23_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR23_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR23_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR24_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR24_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR24_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR24_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR24_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR24_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR25_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR25_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR25_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR25_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR25_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR25_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR26_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR26_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR26_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR26_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR26_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR26_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR27_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR27_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR27_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR27_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR27_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR27_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// // row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR28_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR28_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR28_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR28_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR28_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR28_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR29_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR29_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR29_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR29_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR29_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR29_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR30_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR30_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR30_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR30_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR30_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR30_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR32_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR32_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR32_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR32_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR32_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR32_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR33_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR33_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR33_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR33_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR33_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR33_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR34_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR34_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR34_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR34_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR34_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR34_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_IS ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_ISDetail");

			// Common border style
			BorderStyle border = BorderStyle.THIN;

			// Header style (left aligned)
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

			// Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

			// Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

			// ACCT BALANCE style (right aligned with 3 decimals)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "ROWID", "COLUMNID",
					"REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_IS_Archival_Detail_Entity> reportData = archivalDetail_getdatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_IS_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria_1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_IS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_ISExcel", e);
			return new byte[0];
		}
	}

	// public List<Object> getM_ISArchival() {
	// List<Object> M_ISArchivallist = new ArrayList<>();
	// List<Object> M_ISArchivallist1 = new ArrayList<>();
	// try {
	// M_ISArchivallist = M_IS_Archival_Summary_Repo1.getM_ISarchival();
	// M_ISArchivallist1 = M_IS_Archival_Summary_Repo2.getM_ISarchival();
	// System.out.println("countser" + M_ISArchivallist.size());
	// System.out.println("countser" + M_ISArchivallist1.size());
	// } catch (Exception e) {
	// // Log the exception
	// System.err.println("Error fetching M_IS Archival data: " + e.getMessage());
	// e.printStackTrace();

	// // Optionally, you can rethrow it or return empty list
	// // throw new RuntimeException("Failed to fetch data", e);
	// }
	// return M_ISArchivallist;
	// }

	public byte[] getExcelM_ISARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_IS_Archival_Summary_Entity1> dataList = archivalSummary1_getdatabydateListarchival(
				dateformat.parse(todate), version);
		List<M_IS_Archival_Summary_Entity2> dataList1 = archivalSummary2_getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_IS report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
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

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");
			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 2 = Excel column B
				Cell dateCell = dateRow.getCell(1);

				if (dateCell == null) {
					dateCell = dateRow.createCell(1);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_IS_Archival_Summary_Entity1 record = dataList.get(i);
					M_IS_Archival_Summary_Entity2 record2 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row10
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR10_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR10_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row10
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR10_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR10_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row10
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR10_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR10_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR11_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR11_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR11_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR11_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR11_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR11_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR12_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR12_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR12_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR13_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR13_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR13_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR14_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR14_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR14_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR15_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR15_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR15_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR21_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR21_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR21_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR21_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR21_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR21_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					Cell cell6 = row.createCell(6);
					if (record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					// Column H
					Cell cell7 = row.createCell(7);
					if (record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR22_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR22_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR22_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR22_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR22_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR22_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR23_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR23_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR23_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR23_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR23_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR23_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR24_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR24_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR24_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR24_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR24_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR24_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR25_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR25_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR25_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR25_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR25_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR25_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR26_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR26_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR26_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR26_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR26_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR26_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR27_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR27_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR27_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR27_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR27_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR27_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR28_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR28_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR28_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR28_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR28_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR28_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR29_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR29_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR29_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR29_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR29_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR29_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR30_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR30_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR30_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR30_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR30_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR30_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR32_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR32_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR32_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR32_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR32_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR32_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR33_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR33_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR33_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR33_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR33_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR33_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR34_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR34_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR34_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR34_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR34_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR34_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
	/// Report Date | Report Version | Domain
	/// RESUB VIEW

	public List<Object[]> getM_ISResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_IS_Archival_Summary_Entity1> latestArchivalList = archivalSummary1_getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_IS_Archival_Summary_Entity1 entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReport_resubdate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12H Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getM_ISArchival() {
		List<Object[]> archivalList = new ArrayList<>();
		try {
			List<M_IS_Archival_Summary_Entity1> latestArchivalList = archivalSummary1_getdatabydateListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_IS_Archival_Summary_Entity1 entity : latestArchivalList) {
					archivalList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReport_resubdate() });
				}
				System.out.println("Fetched " + archivalList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_IS Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return archivalList;
	}

	public void updateReportReSub(M_IS_Summary_Entity1 updatedEntity1, M_IS_Summary_Entity2 updatedEntity2) {

		System.out.println("Came to M_IS Resub Service");
		System.out.println("Report Date: " + updatedEntity1.getReport_date());

		Date reportDate = updatedEntity1.getReport_date();
		BigDecimal newVersion = BigDecimal.ONE;

		try {
			// Get latest archival version
			Optional<M_IS_Archival_Summary_Entity1> latestArchivalOpt1 = archivalSummary1_getLatestArchivalVersionByDate(
					reportDate);

			if (latestArchivalOpt1.isPresent()) {
				M_IS_Archival_Summary_Entity1 latestArchival = latestArchivalOpt1.get();
				String latestVersionStr = latestArchival.getReport_version();
				if (latestVersionStr != null) {
					newVersion = new BigDecimal(latestVersionStr).add(BigDecimal.ONE);
				}
			} else {
				System.out.println("No previous archival found for date: " + reportDate);
			}

			// Check if version already exists
			boolean exists = archivalSummary1_findByReportDateAndReportVersion(reportDate, newVersion).isPresent();

			if (exists) {
				throw new RuntimeException("⚠ Version " + newVersion + " already exists for report date " + reportDate);
			}

			String versionStr = newVersion.toString();
			Date now = new Date();

			// Insert into Archival Summary 1
			String sql1 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql1, reportDate, versionStr, now, updatedEntity1.getReport_frequency(),
					updatedEntity1.getReport_code(), updatedEntity1.getReport_desc(), updatedEntity1.getEntity_flg(),
					updatedEntity1.getModify_flg(), updatedEntity1.getDel_flg());

			// Insert into Archival Summary 2
			String sql2 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql2, reportDate, versionStr, now, updatedEntity2.getReport_frequency(),
					updatedEntity2.getReport_code(), updatedEntity2.getReport_desc(), updatedEntity2.getEntity_flg(),
					updatedEntity2.getModify_flg(), updatedEntity2.getDel_flg());

			System.out.println("✅ Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating M_IS archival resubmission record", e);
		}
	}

	public byte[] BRRS_M_ISResubExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<M_IS_Archival_Summary_Entity1> dataList = archivalSummary1_getdatabydateListarchival(
				dateformat.parse(todate), version);
		List<M_IS_Archival_Summary_Entity2> dataList1 = archivalSummary2_getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_IS report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			// This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
			// A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

		// This try-with-resources block is perfect. It guarantees all resources are
		// closed automatically.
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

			// Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
			// numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 2 = Excel column B
				Cell dateCell = dateRow.getCell(1);

				if (dateCell == null) {
					dateCell = dateRow.createCell(1);
				}

				// Date conversion
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");

				SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

				Date reportDateValue = inputFormat.parse(todate);

				// Set formatted date
				dateCell.setCellValue(outputFormat.format(reportDateValue));

				dateCell.setCellStyle(textStyle);

			} catch (ParseException e) {

				logger.error("Error parsing todate: {}", todate, e);
			}

			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_IS_Archival_Summary_Entity1 record = dataList.get(i);
					M_IS_Archival_Summary_Entity2 record2 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// row10
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR10_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR10_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row10
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR10_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR10_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row10
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR10_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR10_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row11
					row = sheet.getRow(10);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR11_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR11_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR11_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR11_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR11_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR11_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR12_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR12_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row12
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR12_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR12_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR13_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row13
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR13_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR13_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR14_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row14
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR14_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR14_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_FAIR_VALUE_PROFIT_AND_LOSS() != null) {
						cell3.setCellValue(record.getR15_FAIR_VALUE_PROFIT_AND_LOSS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row11
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_HELD_TO_MATURITY() != null) {
						cell4.setCellValue(record.getR15_HELD_TO_MATURITY().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record.getR15_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR21_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR21_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row21
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR21_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR21_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR21_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR21_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					// Column G
					Cell cell6 = row.createCell(6);
					if (record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR21_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					// Column H
					Cell cell7 = row.createCell(7);
					if (record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR21_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR22_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR22_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row22
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR22_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR22_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR22_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR22_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR22_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row22
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR22_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR23_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR23_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row23
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR23_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR23_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR23_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR23_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR23_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row23
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR23_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR24_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR24_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row24
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR24_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR24_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR24_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR24_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR24_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row24
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR24_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR25_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR25_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row25
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR25_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR25_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR25_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR25_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR25_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row25
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR25_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR26_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR26_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row26
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR26_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR26_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR26_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR26_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR26_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR26_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR27_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR27_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row27
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR27_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR27_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR27_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR27_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR27_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR27_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR28_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR28_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row28
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR28_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR28_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR28_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR28_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR28_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR28_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR29_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR29_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row29
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR29_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR29_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR29_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR29_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR29_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR29_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR30_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR30_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row30
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR30_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR30_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR30_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR30_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR30_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR30_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR32_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR32_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row32
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR32_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR32_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR32_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR32_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR32_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR32_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR33_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR33_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row33
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR33_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR33_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR33_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR33_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR33_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR33_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					// Column D
					cell3 = row.createCell(3);
					if (record2.getR34_HELD_FOR_TRADING() != null) {
						cell3.setCellValue(record2.getR34_HELD_FOR_TRADING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// row34
					// Column E
					cell4 = row.createCell(4);
					if (record2.getR34_AMORTISED_COST() != null) {
						cell4.setCellValue(record2.getR34_AMORTISED_COST().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record2.getR34_AVAILABLE_FOR_SALE() != null) {
						cell5.setCellValue(record2.getR34_AVAILABLE_FOR_SALE().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					// Column G
					cell6 = row.createCell(6);
					if (record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS() != null) {
						cell6.setCellValue(record2.getR34_FAIR_VALUE_THROUGH_PROFIT_AND_LOSS().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					// Column H
					cell7 = row.createCell(7);
					if (record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING() != null) {
						cell7.setCellValue(record2.getR34_QUALIFYING_FOR_HEDGE_ACCOUNTING().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}
				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			return out.toByteArray();
		}
	}

	// =====================================================
	// SUMMARY UPDATE & RESUB WORKFLOW FOR M_IS
	// =====================================================
	@Transactional
	public ResponseEntity<?> updateReport(M_IS_Summary_Entity1 updatedEntity1, M_IS_Summary_Entity2 updatedEntity2) {
		return updateReport(updatedEntity1, updatedEntity2, null, null, null);
	}

	@Transactional
	public ResponseEntity<?> updateReport(M_IS_Summary_Entity1 updatedEntity1, M_IS_Summary_Entity2 updatedEntity2,
			String type, String version, String entry) {
		try {
			boolean isResub = "RESUB".equalsIgnoreCase(type);
			Date reportDate = updatedEntity1 != null && updatedEntity1.getReport_date() != null ? updatedEntity1.getReport_date()
					: (updatedEntity2 != null ? updatedEntity2.getReport_date() : null);
			if (reportDate == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Report Date is required.");
			}
			java.sql.Date sqlDate = new java.sql.Date(reportDate.getTime());
			String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(reportDate);

			System.out.println("Updating M_IS Summary: type=" + type + ", version=" + version + ", entry=" + entry + ", date=" + formattedDate);

			// =========================================
			// FETCH EXISTING RECORD(S)
			// =========================================
			M_IS_Summary_Entity1 existing1 = null;
			M_IS_Summary_Entity2 existing2 = null;

			String ver = (version != null && !version.isEmpty()) ? version
					: (updatedEntity1 != null && updatedEntity1.getReport_version() != null ? updatedEntity1.getReport_version()
							: (updatedEntity2 != null && updatedEntity2.getReport_version() != null ? updatedEntity2.getReport_version() : null));

			if (isResub) {
				List<M_IS_Summary_Entity1> list1 = null;
				List<M_IS_Summary_Entity2> list2 = null;
				if (ver != null && !ver.isEmpty()) {
					list1 = get_ResubSummary1ByDate(reportDate, ver);
					list2 = get_ResubSummary2ByDate(reportDate, ver);
				}
				if (list1 == null || list1.isEmpty()) {
					String maxVerSql = "SELECT NVL(MAX(TO_NUMBER(REPORT_VERSION)), 0) FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
					Integer maxV = jdbcTemplate.queryForObject(maxVerSql, Integer.class, sqlDate);
					ver = String.valueOf(maxV != null ? maxV : 0);
					list1 = get_ResubSummary1ByDate(reportDate, ver);
					list2 = get_ResubSummary2ByDate(reportDate, ver);
				}

				if (list1 != null && !list1.isEmpty()) {
					existing1 = list1.get(0);
				} else {
					existing1 = new M_IS_Summary_Entity1();
					existing1.setReport_date(reportDate);
					if (ver != null) existing1.setReport_version(ver);
				}

				if (list2 != null && !list2.isEmpty()) {
					existing2 = list2.get(0);
				} else {
					existing2 = new M_IS_Summary_Entity2();
					existing2.setReport_date(reportDate);
					if (ver != null) existing2.setReport_version(ver);
				}
			} else {
				List<M_IS_Summary_Entity1> list1 = summary1_getdatabydateList(reportDate);
				if (list1 != null && !list1.isEmpty()) {
					existing1 = list1.get(0);
				} else {
					existing1 = new M_IS_Summary_Entity1();
					existing1.setReport_date(reportDate);
				}

				List<M_IS_Summary_Entity2> list2 = summary2_getdatabydateList(reportDate);
				if (list2 != null && !list2.isEmpty()) {
					existing2 = list2.get(0);
				} else {
					existing2 = new M_IS_Summary_Entity2();
					existing2.setReport_date(reportDate);
				}
			}

			// =========================================
			// AUDIT OLD COPIES
			// =========================================
			M_IS_Summary_Entity1 oldcopy1 = new M_IS_Summary_Entity1();
			BeanUtils.copyProperties(existing1, oldcopy1);

			M_IS_Summary_Entity2 oldcopy2 = new M_IS_Summary_Entity2();
			BeanUtils.copyProperties(existing2, oldcopy2);

			boolean isChanged = false;

			// =========================================
			// UPDATE TABLE 1 FIELDS (Rows 10 to 15)
			// =========================================
			String[] t1Fields = { "FAIR_VALUE_PROFIT_AND_LOSS", "HELD_TO_MATURITY", "AVAILABLE_FOR_SALE" };
			if (updatedEntity1 != null) {
				for (int i = 10; i <= 15; i++) {
					for (String field : t1Fields) {
						String getterName = "getR" + i + "_" + field;
						String setterName = "setR" + i + "_" + field;
						try {
							Method getter = M_IS_Summary_Entity1.class.getMethod(getterName);
							Method setter = M_IS_Summary_Entity1.class.getMethod(setterName, getter.getReturnType());
							Object newValue = getter.invoke(updatedEntity1);
							Object oldValue = getter.invoke(existing1);

							if (newValue != null && !newValue.equals(oldValue)) {
								setter.invoke(existing1, newValue);
								isChanged = true;
							}
						} catch (NoSuchMethodException ignored) {
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (updatedEntity1.getReport_version() != null) existing1.setReport_version(updatedEntity1.getReport_version());
				if (updatedEntity1.getReport_frequency() != null) existing1.setReport_frequency(updatedEntity1.getReport_frequency());
				if (updatedEntity1.getReport_code() != null) existing1.setReport_code(updatedEntity1.getReport_code());
				if (updatedEntity1.getReport_desc() != null) existing1.setReport_desc(updatedEntity1.getReport_desc());
				if (updatedEntity1.getEntity_flg() != null) existing1.setEntity_flg(updatedEntity1.getEntity_flg());
				if (updatedEntity1.getModify_flg() != null) existing1.setModify_flg(updatedEntity1.getModify_flg());
				if (updatedEntity1.getDel_flg() != null) existing1.setDel_flg(updatedEntity1.getDel_flg());
			}

			// =========================================
			// UPDATE TABLE 2 FIELDS (Rows 21 to 30, 32 to 34)
			// =========================================
			String[] t2Fields = { "HELD_FOR_TRADING", "AMORTISED_COST", "AVAILABLE_FOR_SALE", "FAIR_VALUE_THROUGH_PROFIT_AND_LOSS", "QUALIFYING_FOR_HEDGE_ACCOUNTING" };
			if (updatedEntity2 != null) {
				List<Integer> t2Rows = Arrays.asList(21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34);
				for (int i : t2Rows) {
					for (String field : t2Fields) {
						if (i == 28 && "HELD_FOR_TRADING".equals(field)) continue;

						String getterName = "getR" + i + "_" + field;
						String setterName = "setR" + i + "_" + field;
						try {
							Method getter = M_IS_Summary_Entity2.class.getMethod(getterName);
							Method setter = M_IS_Summary_Entity2.class.getMethod(setterName, getter.getReturnType());
							Object newValue = getter.invoke(updatedEntity2);
							Object oldValue = getter.invoke(existing2);

							if (newValue != null && !newValue.equals(oldValue)) {
								setter.invoke(existing2, newValue);
								isChanged = true;
							}
						} catch (NoSuchMethodException ignored) {
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (updatedEntity2.getReport_version() != null) existing2.setReport_version(updatedEntity2.getReport_version());
				if (updatedEntity2.getReport_frequency() != null) existing2.setReport_frequency(updatedEntity2.getReport_frequency());
				if (updatedEntity2.getReport_code() != null) existing2.setReport_code(updatedEntity2.getReport_code());
				if (updatedEntity2.getReport_desc() != null) existing2.setReport_desc(updatedEntity2.getReport_desc());
				if (updatedEntity2.getEntity_flg() != null) existing2.setEntity_flg(updatedEntity2.getEntity_flg());
				if (updatedEntity2.getModify_flg() != null) existing2.setModify_flg(updatedEntity2.getModify_flg());
				if (updatedEntity2.getDel_flg() != null) existing2.setDel_flg(updatedEntity2.getDel_flg());
			}

			// =========================================
			// SAVE CHANGES
			// =========================================
			boolean isResubNoEntry = isResub && "NO".equalsIgnoreCase(entry);

			if (isChanged) {
				if (isResub) {
					if (isResubNoEntry) {
						// REGENERATE: Do NOT overwrite the old version in archival summary!
						// Clean live summary tables first:
						jdbcTemplate.update("DELETE FROM BRRS_M_IS_SUMMARYTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
						jdbcTemplate.update("DELETE FROM BRRS_M_IS_SUMMARYTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);

						// Copy base archival rows to live summary
						copyTableByVersion("BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", "BRRS_M_IS_SUMMARYTABLE1", sqlDate, ver);
						copyTableByVersion("BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", "BRRS_M_IS_SUMMARYTABLE2", sqlDate, ver);

						// Apply modified manual values to live summary tables
						updateSummary1TableDirect("BRRS_M_IS_SUMMARYTABLE1", existing1, sqlDate, null);
						updateSummary2TableDirect("BRRS_M_IS_SUMMARYTABLE2", existing2, sqlDate, null);

						auditService.compareEntitiesmanual(oldcopy1, existing1, reportDate.toString(), "M IS Archival Summary Screen 1", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY1");
						auditService.compareEntitiesmanual(oldcopy2, existing2, reportDate.toString(), "M IS Archival Summary Screen 2", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY2");

						System.out.println("Modified values staged into live summary. Old version " + ver + " preserved in archival summary.");

						Run_M_IS_Procedure(formattedDate, "RESUB", "NO");
						return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
					} else {
						// MODIFY ONLY: Save as new incremented version in archival summary tables
						String versionSql = "SELECT NVL(MAX(TO_NUMBER(REPORT_VERSION)), 0) FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
						Integer maxV = jdbcTemplate.queryForObject(versionSql, Integer.class, sqlDate);
						int nextVersion = (maxV != null ? maxV : 0) + 1;
						String nextVerStr = String.valueOf(nextVersion);
						System.out.println("Creating new version " + nextVersion + " for RESUB summary");

						// 1. Copy old version row to new version row in archival summary 1
						Set<String> excludeCols = new HashSet<>(Arrays.asList("REPORT_RESUBDATE", "REPORT_VERSION"));
						List<String> commonCols1 = getCommonColumns("BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", excludeCols);
						if (!commonCols1.isEmpty()) {
							String cols1 = String.join(", ", commonCols1);
							String copySql1 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 (" + cols1 + ", REPORT_VERSION, REPORT_RESUBDATE) "
									+ "SELECT " + cols1 + ", " + nextVersion + ", SYSDATE FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 "
									+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
							int rowsCopied1 = jdbcTemplate.update(copySql1, sqlDate, ver);
							if (rowsCopied1 == 0) {
								jdbcTemplate.update("INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 (REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE) VALUES (?, " + nextVersion + ", SYSDATE)", sqlDate);
							}
						}

						// 2. Copy old version row to new version row in archival summary 2
						List<String> commonCols2 = getCommonColumns("BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", excludeCols);
						if (!commonCols2.isEmpty()) {
							String cols2 = String.join(", ", commonCols2);
							String copySql2 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 (" + cols2 + ", REPORT_VERSION, REPORT_RESUBDATE) "
									+ "SELECT " + cols2 + ", " + nextVersion + ", SYSDATE FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 "
									+ "WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = ?";
							int rowsCopied2 = jdbcTemplate.update(copySql2, sqlDate, ver);
							if (rowsCopied2 == 0) {
								jdbcTemplate.update("INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 (REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE) VALUES (?, " + nextVersion + ", SYSDATE)", sqlDate);
							}
						}

						// 3. Apply modified values to the new version row
						existing1.setReport_version(nextVerStr);
						existing2.setReport_version(nextVerStr);
						updateSummary1TableDirect("BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", existing1, sqlDate, nextVerStr);
						updateSummary2TableDirect("BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", existing2, sqlDate, nextVerStr);

						jdbcTemplate.update("UPDATE BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = " + nextVersion, sqlDate);
						jdbcTemplate.update("UPDATE BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = " + nextVersion, sqlDate);

						auditService.compareEntitiesmanual(oldcopy1, existing1, reportDate.toString(), "M IS Archival Summary Screen 1", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY1");
						auditService.compareEntitiesmanual(oldcopy2, existing2, reportDate.toString(), "M IS Archival Summary Screen 2", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY2");

						System.out.println("M_IS Archival Summary created successfully with new version " + nextVersion);
						return ResponseEntity.ok("Record updated successfully with Version " + nextVersion);
					}
				} else {
					updateSummary1TableDirect("BRRS_M_IS_SUMMARYTABLE1", existing1, sqlDate, null);
					updateSummary2TableDirect("BRRS_M_IS_SUMMARYTABLE2", existing2, sqlDate, null);

					auditService.compareEntitiesmanual(oldcopy1, existing1, reportDate.toString(), "M IS Summary Screen 1", "BRRS_M_IS_SUMMARYTABLE1");
					auditService.compareEntitiesmanual(oldcopy2, existing2, reportDate.toString(), "M IS Summary Screen 2", "BRRS_M_IS_SUMMARYTABLE2");

					System.out.println("M_IS Summary updated successfully");

					return ResponseEntity.ok("M_IS Summary updated successfully");
				}
			} else {
				if (isResubNoEntry) {
					Run_M_IS_Procedure(formattedDate, "RESUB", "NO");
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("No changes detected");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating M_IS Summary : " + e.getMessage());
		}
	}

	private void updateSummary1TableDirect(String tableName, M_IS_Summary_Entity1 entity, java.sql.Date sqlDate, String version) {
		String[] fields = { "PRODUCT", "FAIR_VALUE_PROFIT_AND_LOSS", "HELD_TO_MATURITY", "AVAILABLE_FOR_SALE", "TOTAL" };
		StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
		List<Object> params = new ArrayList<>();

		for (int i = 10; i <= 16; i++) {
			for (String field : fields) {
				String column = "R" + i + "_" + field;
				String getterName = "getR" + i + "_" + field;
				try {
					Method getter = M_IS_Summary_Entity1.class.getMethod(getterName);
					Object value = getter.invoke(entity);
					sql.append(column).append(" = ?, ");
					params.add(value);
				} catch (Exception ignored) {}
			}
		}
		if (entity.getReport_frequency() != null) {
			sql.append("REPORT_FREQUENCY = ?, ");
			params.add(entity.getReport_frequency());
		}
		if (entity.getReport_code() != null) {
			sql.append("REPORT_CODE = ?, ");
			params.add(entity.getReport_code());
		}
		if (entity.getReport_desc() != null) {
			sql.append("REPORT_DESC = ?, ");
			params.add(entity.getReport_desc());
		}
		if (entity.getEntity_flg() != null) {
			sql.append("ENTITY_FLG = ?, ");
			params.add(entity.getEntity_flg());
		}
		if (entity.getModify_flg() != null) {
			sql.append("MODIFY_FLG = ?, ");
			params.add(entity.getModify_flg());
		}
		if (entity.getDel_flg() != null) {
			sql.append("DEL_FLG = ?, ");
			params.add(entity.getDel_flg());
		}

		sql.setLength(sql.length() - 2);
		sql.append(" WHERE TRUNC(REPORT_DATE) = TRUNC(?)");
		params.add(sqlDate);

		if (version != null && !version.isEmpty()) {
			sql.append(" AND REPORT_VERSION = ?");
			params.add(version);
		}

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	private void updateSummary2TableDirect(String tableName, M_IS_Summary_Entity2 entity, java.sql.Date sqlDate, String version) {
		String[] fields = { "PRODUCT", "HELD_FOR_TRADING", "AMORTISED_COST", "AVAILABLE_FOR_SALE",
				"FAIR_VALUE_THROUGH_PROFIT_AND_LOSS", "QUALIFYING_FOR_HEDGE_ACCOUNTING", "TOTAL" };
		StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
		List<Object> params = new ArrayList<>();

		for (int i = 21; i <= 35; i++) {
			for (String field : fields) {
				if (i == 28 && "HELD_FOR_TRADING".equals(field)) continue;

				String column = "R" + i + "_" + field;
				String getterName = "getR" + i + "_" + field;
				try {
					Method getter = M_IS_Summary_Entity2.class.getMethod(getterName);
					Object value = getter.invoke(entity);
					sql.append(column).append(" = ?, ");
					params.add(value);
				} catch (Exception ignored) {}
			}
		}
		if (entity.getReport_frequency() != null) {
			sql.append("REPORT_FREQUENCY = ?, ");
			params.add(entity.getReport_frequency());
		}
		if (entity.getReport_code() != null) {
			sql.append("REPORT_CODE = ?, ");
			params.add(entity.getReport_code());
		}
		if (entity.getReport_desc() != null) {
			sql.append("REPORT_DESC = ?, ");
			params.add(entity.getReport_desc());
		}
		if (entity.getEntity_flg() != null) {
			sql.append("ENTITY_FLG = ?, ");
			params.add(entity.getEntity_flg());
		}
		if (entity.getModify_flg() != null) {
			sql.append("MODIFY_FLG = ?, ");
			params.add(entity.getModify_flg());
		}
		if (entity.getDel_flg() != null) {
			sql.append("DEL_FLG = ?, ");
			params.add(entity.getDel_flg());
		}

		sql.setLength(sql.length() - 2);
		sql.append(" WHERE TRUNC(REPORT_DATE) = TRUNC(?)");
		params.add(sqlDate);

		if (version != null && !version.isEmpty()) {
			sql.append(" AND REPORT_VERSION = ?");
			params.add(version);
		}

		jdbcTemplate.update(sql.toString(), params.toArray());
	}

	// =====================================================
	// UPDATEDETAIL FOR M_IS
	// =====================================================
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String Sno = request.getParameter("sno");
			if (Sno == null) {
				Sno = request.getParameter("SNO");
			}
			String acctNumber = request.getParameter("acctNumber");
			if (acctNumber == null) {
				acctNumber = request.getParameter("ACCT_NUMBER");
			}
			String acctBalanceInpula = request.getParameter("acctBalanceInPula");
			if (acctBalanceInpula == null) {
				acctBalanceInpula = request.getParameter("acctBalanceInpula");
			}
			String acctName = request.getParameter("acctName");
			String reportDateStr = request.getParameter("reportDate");
			Date parsedReportDate = parseFlexibleDate(reportDateStr);

			System.out.println("updateDetailEdit: Sno=" + Sno + ", acctNumber=" + acctNumber + ", date=" + reportDateStr);

			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			M_IS_Detail_Entity existing = null;

			System.out.println("type is : " + type);

			if ("RESUB".equals(type)) {
				if (acctNumber != null && !acctNumber.trim().isEmpty()) {
					existing = findByAcctNumberArch(acctNumber.trim(), parsedReportDate);
				}
				if (existing == null && Sno != null && !Sno.trim().isEmpty()) {
					existing = findByAcctNumberArch(Sno.trim(), parsedReportDate);
				}
				if (existing == null && acctNumber != null && !acctNumber.trim().isEmpty()) {
					existing = findBySnoArch(acctNumber.trim());
				}
			} else {
				if (Sno != null && !Sno.trim().isEmpty()) {
					try {
						existing = detail_findBySno(Sno.trim());
					} catch (Exception ignored) {
					}
				}
				if (existing == null && acctNumber != null && !acctNumber.trim().isEmpty()) {
					if (parsedReportDate != null) {
						try {
							List<M_IS_Detail_Entity> list = jdbcTemplate.query(
									"SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
									new Object[] { acctNumber.trim(), parsedReportDate }, new M_IS_Detail_EntityRowMapper());
							if (list != null && !list.isEmpty()) {
								existing = list.get(0);
							}
						} catch (Exception ignored) {}
					}
					if (existing == null) {
						try {
							List<M_IS_Detail_Entity> list = jdbcTemplate.query(
									"SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE ACCT_NUMBER = ?",
									new Object[] { acctNumber.trim() }, new M_IS_Detail_EntityRowMapper());
							if (list != null && !list.isEmpty()) {
								existing = list.get(0);
							}
						} catch (Exception ignored) {}
					}
				}
			}

			if (existing == null) {
				System.err.println("updateDetailEdit: Record not found for Sno=" + Sno + ", acctNumber=" + acctNumber);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Record not found for update (Account: " + (acctNumber != null ? acctNumber : Sno) + ")");
			}

			if (existing.getReportDate() == null && parsedReportDate != null) {
				existing.setReportDate(parsedReportDate);
			}

			M_IS_Detail_Entity oldcopy = new M_IS_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			boolean isChanged = false;

			// Update Account Name
			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {
				BigDecimal newBalance = new BigDecimal(acctBalanceInpula.replace(",", ""));
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {
					existing.setAcctBalanceInpula(newBalance);
					isChanged = true;
				}
			}

			// Save using JDBC
			if (isChanged) {
				System.out.println("Type in update block : " + type);

				if ("RESUB".equals(type)) {
					System.out.println("Inside RESUB UPDATE for acct: " + existing.getAcctNumber());
					int rowsUpdated = 0;
					Date dt = existing.getReportDate() != null ? existing.getReportDate() : parsedReportDate;
					if (dt != null) {
						try {
							rowsUpdated = jdbcTemplate.update(
									"UPDATE BRRS_M_IS_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ? AND TRUNC(REPORT_DATE) = TRUNC(?)",
									existing.getAcctName(),
									existing.getAcctBalanceInpula(),
									existing.getAcctNumber(),
									dt);
						} catch (Exception ex) {
							System.err.println("Warning updating archival detail with date: " + ex.getMessage());
						}
					} else {
						rowsUpdated = jdbcTemplate.update(
								"UPDATE BRRS_M_IS_ARCHIVALTABLE_DETAIL SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?",
								existing.getAcctName(),
								existing.getAcctBalanceInpula(),
								existing.getAcctNumber());
					}
					System.out.println("Archival rows updated: " + rowsUpdated);

					String auditId = (existing.getAcctNumber() != null) ? existing.getAcctNumber() : (Sno != null ? Sno : "0");
					try {
						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								auditId,
								"M_IS Archival Screen",
								"BRRS_M_IS_ARCHIVALTABLE_DETAIL");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}

				} else {
					if (existing.getSno() != null) {
						jdbcTemplate.update(
								"UPDATE BRRS_M_IS_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE SNO = ?",
								existing.getAcctName(),
								existing.getAcctBalanceInpula(),
								existing.getSno());
					} else {
						jdbcTemplate.update(
								"UPDATE BRRS_M_IS_DETAILTABLE SET ACCT_NAME = ?, ACCT_BALANCE_IN_PULA = ? WHERE ACCT_NUMBER = ?",
								existing.getAcctName(),
								existing.getAcctBalanceInpula(),
								existing.getAcctNumber());
					}

					String auditId = (existing.getSno() != null) ? String.valueOf(existing.getSno()) : existing.getAcctNumber();
					try {
						auditService.compareEntitiesmanual(
								oldcopy,
								existing,
								auditId,
								"M_IS Screen",
								"BRRS_M_IS_DETAILTABLE");
					} catch (Exception auditEx) {
						System.err.println("Audit warning: " + auditEx.getMessage());
					}
				}

				System.out.println("Record updated using JDBC");

				Date effectiveDate = (existing != null && existing.getReportDate() != null) ? existing.getReportDate() : parsedReportDate;
				String dateForProc = (effectiveDate != null) ? new SimpleDateFormat("dd-MM-yyyy").format(effectiveDate) : reportDateStr;
				Run_M_IS_Procedure(dateForProc, type, entry);

				if ("RESUB".equals(type) && "NO".equals(entry)) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}

				return ResponseEntity.ok("Record updated successfully!");

			} else {
				if ("RESUB".equals(type) && "NO".equals(entry)) {
					Date effectiveDate = (existing != null && existing.getReportDate() != null) ? existing.getReportDate() : parsedReportDate;
					String dateForProc = (effectiveDate != null) ? new SimpleDateFormat("dd-MM-yyyy").format(effectiveDate) : reportDateStr;
					Run_M_IS_Procedure(dateForProc, type, entry);
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}

				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// =====================================================
	// FOR RESUB - TRIGGER REGEN PROCEDURE
	// =====================================================
	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_M_IS_Procedure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	private void Run_M_IS_Procedure(String reportDateStr, String type, String entry) {
		Date parsedDate = parseFlexibleDate(reportDateStr);
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
					"SELECT COUNT(*) FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
					Integer.class, sqlDate);
			if (count == null || count == 0) {
				System.out.println("No archival detail records found for " + sqlDate + "; skipping detail transfer.");
				return;
			}

			final Set<String> archCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE 1=0", rs -> {
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					archCols.add(md.getColumnName(i).toUpperCase());
				}
				return null;
			});

			final Set<String> liveCols = new HashSet<>();
			jdbcTemplate.query("SELECT * FROM BRRS_M_IS_DETAILTABLE WHERE 1=0", rs -> {
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

			int rowsDeleted = jdbcTemplate.update("DELETE FROM BRRS_M_IS_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
			System.out.println("Detail rows deleted before transfer: " + rowsDeleted);

			String colsJoined = String.join(", ", commonCols);
			boolean liveHasSno = liveCols.contains("SNO");
			String insertSql;
			if (liveHasSno) {
				insertSql = "INSERT INTO BRRS_M_IS_DETAILTABLE (SNO, " + colsJoined + ") "
						+ "SELECT ROWNUM AS SNO, " + colsJoined + " FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			} else {
				insertSql = "INSERT INTO BRRS_M_IS_DETAILTABLE (" + colsJoined + ") "
						+ "SELECT " + colsJoined + " FROM BRRS_M_IS_ARCHIVALTABLE_DETAIL WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
			}
			int rowsTransferred = jdbcTemplate.update(insertSql, sqlDate);
			System.out.println("Detail rows transferred from archival: " + rowsTransferred);
			jdbcTemplate.update("UPDATE BRRS_M_IS_DETAILTABLE SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
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

			System.out.println("executeProcedureLogic: formattedDate = " + formattedDate + ", type = " + type + ", entry = " + entry);

			if (isResubNoEntry) {
				// 1. Safely transfer archival detail records into live detail table
				transferArchivalDetailsToLive(sqlDate);

				// 2. Check if live summary 1 & 2 already have staged modified values
				Integer liveSumCount1 = jdbcTemplate.queryForObject(
						"SELECT COUNT(*) FROM BRRS_M_IS_SUMMARYTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)",
						Integer.class, sqlDate);

				if (liveSumCount1 == null || liveSumCount1 == 0) {
					// Live summary is empty (e.g. triggered from Details view) -> restore from latest archival summary
					String maxVerSql = "SELECT NVL(MAX(TO_NUMBER(REPORT_VERSION)), 0) FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
					Integer currentMaxVersion = jdbcTemplate.queryForObject(maxVerSql, Integer.class, sqlDate);

					if (currentMaxVersion != null && currentMaxVersion > 0) {
						copyTableByVersion("BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", "BRRS_M_IS_SUMMARYTABLE1", sqlDate, String.valueOf(currentMaxVersion));
						copyTableByVersion("BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", "BRRS_M_IS_SUMMARYTABLE2", sqlDate, String.valueOf(currentMaxVersion));
						System.out.println("Archival summary restored to live summary");
					}
				} else {
					System.out.println("Live summary already contains modified staged values; preserving for procedure.");
				}
				jdbcTemplate.update("UPDATE BRRS_M_IS_SUMMARYTABLE1 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
				jdbcTemplate.update("UPDATE BRRS_M_IS_SUMMARYTABLE2 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
			}

			// 6. Execute stored procedure
			if (shouldExecuteProcedure) {
				jdbcTemplate.update("BEGIN BRRS_M_IS_SUMMARY_PROCEDURE(?); END;", formattedDate);
				System.out.println("BRRS_M_IS_SUMMARY_PROCEDURE executed successfully for " + formattedDate);
			}

			// 7. Post-execution archival handling
			if (isResubNoEntry) {
				// Delete detail records from live table
				String deleteDetailAfterSql = "DELETE FROM BRRS_M_IS_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				int rowsDeletedAfter = jdbcTemplate.update(deleteDetailAfterSql, sqlDate);
				System.out.println("Live detail records deleted after procedure: " + rowsDeletedAfter);

				// Query max version again
				String versionSql = "SELECT NVL(MAX(TO_NUMBER(REPORT_VERSION)), 0) FROM BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
				Integer maxVersion = jdbcTemplate.queryForObject(versionSql, Integer.class, sqlDate);
				int highestValue = (maxVersion != null ? maxVersion : 0) + 1;
				System.out.println("Archiving new summary with version: " + highestValue);

				// Insert new version into archival summary 1 & 2
				Set<String> excludeCols = new HashSet<>(Arrays.asList("REPORT_RESUBDATE", "REPORT_VERSION"));
				List<String> commonCols1 = getCommonColumns("BRRS_M_IS_SUMMARYTABLE1", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY1", excludeCols);
				if (!commonCols1.isEmpty()) {
					try {
						String cols1 = String.join(", ", commonCols1);
						String archiveSql1 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 (" + cols1 + ", REPORT_VERSION, REPORT_RESUBDATE) "
								+ "SELECT " + cols1 + ", " + highestValue + ", SYSDATE FROM BRRS_M_IS_SUMMARYTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
						int rowsArchived1 = jdbcTemplate.update(archiveSql1, sqlDate);
						System.out.println("Archival summary 1 rows inserted: " + rowsArchived1 + " with version: " + highestValue);
						jdbcTemplate.update("UPDATE BRRS_M_IS_ARCHIVALTABLE_SUMMARY1 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = " + highestValue, sqlDate);
					} catch (Exception ex1) {
						System.err.println("Error archiving summary 1: " + ex1.getMessage());
						ex1.printStackTrace();
					}
				}

				List<String> commonCols2 = getCommonColumns("BRRS_M_IS_SUMMARYTABLE2", "BRRS_M_IS_ARCHIVALTABLE_SUMMARY2", excludeCols);
				if (!commonCols2.isEmpty()) {
					try {
						String cols2 = String.join(", ", commonCols2);
						String archiveSql2 = "INSERT INTO BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 (" + cols2 + ", REPORT_VERSION, REPORT_RESUBDATE) "
								+ "SELECT " + cols2 + ", " + highestValue + ", SYSDATE FROM BRRS_M_IS_SUMMARYTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
						int rowsArchived2 = jdbcTemplate.update(archiveSql2, sqlDate);
						System.out.println("Archival summary 2 rows inserted: " + rowsArchived2 + " with version: " + highestValue);
						jdbcTemplate.update("UPDATE BRRS_M_IS_ARCHIVALTABLE_SUMMARY2 SET REPORT_DATE = TRUNC(REPORT_DATE) WHERE TRUNC(REPORT_DATE) = TRUNC(?) AND REPORT_VERSION = " + highestValue, sqlDate);
					} catch (Exception ex2) {
						System.err.println("Error archiving summary 2: " + ex2.getMessage());
						ex2.printStackTrace();
					}
				}

				// Delete temporary live summary records
				try {
					int del1 = jdbcTemplate.update("DELETE FROM BRRS_M_IS_SUMMARYTABLE1 WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
					int del2 = jdbcTemplate.update("DELETE FROM BRRS_M_IS_SUMMARYTABLE2 WHERE TRUNC(REPORT_DATE) = TRUNC(?)", sqlDate);
					System.out.println("Live summary rows deleted after archiving: summary1=" + del1 + ", summary2=" + del2);
				} catch (Exception exDel) {
					System.err.println("Warning deleting live summary records: " + exDel.getMessage());
				}
			}
		} catch (Exception e) {
			System.err.println("Error in executeProcedureLogic: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public class M_IS_PK implements Serializable {
		private Date reportDate;
		private BigDecimal reportVersion;

		public M_IS_PK() {
		}

		public M_IS_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_IS_PK))
				return false;
			M_IS_PK that = (M_IS_PK) o;
			return Objects.equals(reportDate, that.reportDate) && Objects.equals(reportVersion, that.reportVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(reportDate, reportVersion);
		}

		// getters & setters
		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}
	}

	public class M_IS_Archival_Summary1_PK implements Serializable {

		private Date reportDate;
		private BigDecimal reportVersion;

		// default constructor
		public M_IS_Archival_Summary1_PK() {
		}

		// parameterized constructor
		public M_IS_Archival_Summary1_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		// equals and hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_IS_Archival_Summary1_PK))
				return false;
			M_IS_Archival_Summary1_PK that = (M_IS_Archival_Summary1_PK) o;
			return Objects.equals(reportDate, that.reportDate) && Objects.equals(reportVersion, that.reportVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(reportDate, reportVersion);
		}

		// getters & setters
		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}
	}

	public class M_IS_Archival_Summary2_PK implements Serializable {

		private Date reportDate;
		private BigDecimal reportVersion;

		// default constructor
		public M_IS_Archival_Summary2_PK() {
		}

		// parameterized constructor
		public M_IS_Archival_Summary2_PK(Date reportDate, BigDecimal reportVersion) {
			this.reportDate = reportDate;
			this.reportVersion = reportVersion;
		}

		// equals and hashCode
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof M_IS_Archival_Summary2_PK))
				return false;
			M_IS_Archival_Summary2_PK that = (M_IS_Archival_Summary2_PK) o;
			return Objects.equals(reportDate, that.reportDate) && Objects.equals(reportVersion, that.reportVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(reportDate, reportVersion);
		}

		// getters & setters
		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public BigDecimal getReportVersion() {
			return reportVersion;
		}

		public void setReportVersion(BigDecimal reportVersion) {
			this.reportVersion = reportVersion;
		}
	}

}