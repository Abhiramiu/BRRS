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
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service
public class BRRS_M_LIQGAP_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LIQGAP_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	UserProfileRep userProfileRep;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ===========================
	// JDBC REPOSITORIES
	// ===========================

	// ===========================
	// M_LIQGAP_Summary_Repo (JDBC Conversion)
	// ===========================

	public List<M_LIQGAP_Summary_Entity> getSummaryDataByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_LIQGAP_Summary_RowMapper());
	}

	// ===========================
	// M_LIQGAP_Detail_Repo (JDBC Conversion)
	// ===========================

	public List<M_LIQGAP_Detail_Entity> getDetailDataByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_LIQGAP_Detail_RowMapper());
	}

	public List<M_LIQGAP_Detail_Entity> getDetailDataByDateWithPagination(Date report_date, int offset, int limit) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_DETAILTABLE WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { report_date, offset, limit }, new M_LIQGAP_Detail_RowMapper());
	}

	public int getDetailDataCount(Date report_date) {
		String sql = "SELECT COUNT(*) FROM BRRS_M_LIQGAP_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { report_date }, Integer.class);
	}

	public List<M_LIQGAP_Detail_Entity> getDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria_1,
			Date report_date) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_DETAILTABLE WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";
		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria_1, report_date },
				new M_LIQGAP_Detail_RowMapper());
	}

	public M_LIQGAP_Detail_Entity findDetailByAcctnumber(String acct_number) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_DETAILTABLE WHERE ACCT_NUMBER = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { acct_number }, new M_LIQGAP_Detail_RowMapper());
	}

	// ===========================
	// M_LIQGAP_Archival_Summary_Repo (JDBC Conversion)
	// ===========================

	public List<Object> getArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_LIQGAP_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_LIQGAP_Archival_Summary_Entity> getArchivalSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_LIQGAP_Archival_Summary_RowMapper());
	}

	public List<M_LIQGAP_Archival_Summary_Entity> getArchivalSummaryDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_LIQGAP_Archival_Summary_RowMapper());
	}

	// ===========================
	// M_LIQGAP_Archival_Detail_Repo (JDBC Conversion)
	// ===========================

	public List<M_LIQGAP_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date report_date,
			String DATA_ENTRY_VERSION) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, DATA_ENTRY_VERSION },
				new M_LIQGAP_Archival_Detail_RowMapper());
	}

	public List<M_LIQGAP_Archival_Detail_Entity> getArchivalDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria_1, Date report_date, String DATA_ENTRY_VERSION) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVALTABLE_DETAIL WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
		return jdbcTemplate.query(sql,
				new Object[] { reportLabel, reportAddlCriteria_1, report_date, DATA_ENTRY_VERSION },
				new M_LIQGAP_Archival_Detail_RowMapper());
	}

	// ===========================
	// M_LIQGAP_Manual_Summary_Repo (JDBC Conversion)
	// ===========================

	public List<M_LIQGAP_Manual_Summary_Entity> getManualSummaryDataByDate(Date rpt_code) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_MANUAL_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { rpt_code }, new M_LIQGAP_Manual_Summary_RowMapper());
	}

	// ===========================
	// M_LIQGAP_Manual_Archival_Summary_Repo (JDBC Conversion)
	// ===========================

	public List<Object> getManualArchivalSummaryList() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_LIQGAP_ARCHIVAL_MANUAL_SUMMARYTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_LIQGAP_Manual_Archival_Summary_Entity> getManualArchivalSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVAL_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_LIQGAP_Manual_Archival_Summary_RowMapper());
	}

	public List<M_LIQGAP_Manual_Archival_Summary_Entity> getManualArchivalSummaryDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_LIQGAP_ARCHIVAL_MANUAL_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_LIQGAP_Manual_Archival_Summary_RowMapper());
	}

	// ===========================
	// ENTITY'S ROW MAPPERS
	// ===========================

	// ===========================
	// M_LIQGAP_Summary_RowMapper
	// ===========================

	public class M_LIQGAP_Summary_RowMapper implements RowMapper<M_LIQGAP_Summary_Entity> {

		@Override
		public M_LIQGAP_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Summary_Entity obj = new M_LIQGAP_Summary_Entity();

			// =========================
			// R10 FIELDS
			// =========================
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_first_month(rs.getBigDecimal("R10_FIRST_MONTH"));
			obj.setR10_third_month(rs.getBigDecimal("R10_THIRD_MONTH"));
			obj.setR10_last_month(rs.getBigDecimal("R10_LAST_MONTH"));
			obj.setR10_first_year(rs.getBigDecimal("R10_FIRST_YEAR"));
			obj.setR10_fifth_year(rs.getBigDecimal("R10_FIFTH_YEAR"));
			obj.setR10_non_interest_bearing(rs.getBigDecimal("R10_NON_INTEREST_BEARING"));
			obj.setR10_cumulative_total(rs.getBigDecimal("R10_CUMULATIVE_TOTAL"));

			// =========================
			// R11 FIELDS
			// =========================
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_first_month(rs.getBigDecimal("R11_FIRST_MONTH"));
			obj.setR11_third_month(rs.getBigDecimal("R11_THIRD_MONTH"));
			obj.setR11_last_month(rs.getBigDecimal("R11_LAST_MONTH"));
			obj.setR11_first_year(rs.getBigDecimal("R11_FIRST_YEAR"));
			obj.setR11_fifth_year(rs.getBigDecimal("R11_FIFTH_YEAR"));
			obj.setR11_non_interest_bearing(rs.getBigDecimal("R11_NON_INTEREST_BEARING"));
			obj.setR11_cumulative_total(rs.getBigDecimal("R11_CUMULATIVE_TOTAL"));

			// =========================
			// R12 FIELDS
			// =========================
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_first_month(rs.getBigDecimal("R12_FIRST_MONTH"));
			obj.setR12_third_month(rs.getBigDecimal("R12_THIRD_MONTH"));
			obj.setR12_last_month(rs.getBigDecimal("R12_LAST_MONTH"));
			obj.setR12_first_year(rs.getBigDecimal("R12_FIRST_YEAR"));
			obj.setR12_fifth_year(rs.getBigDecimal("R12_FIFTH_YEAR"));
			obj.setR12_non_interest_bearing(rs.getBigDecimal("R12_NON_INTEREST_BEARING"));
			obj.setR12_cumulative_total(rs.getBigDecimal("R12_CUMULATIVE_TOTAL"));

			// =========================
			// R13 FIELDS
			// =========================
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_first_month(rs.getBigDecimal("R13_FIRST_MONTH"));
			obj.setR13_third_month(rs.getBigDecimal("R13_THIRD_MONTH"));
			obj.setR13_last_month(rs.getBigDecimal("R13_LAST_MONTH"));
			obj.setR13_first_year(rs.getBigDecimal("R13_FIRST_YEAR"));
			obj.setR13_fifth_year(rs.getBigDecimal("R13_FIFTH_YEAR"));
			obj.setR13_non_interest_bearing(rs.getBigDecimal("R13_NON_INTEREST_BEARING"));
			obj.setR13_cumulative_total(rs.getBigDecimal("R13_CUMULATIVE_TOTAL"));

			// =========================
			// R14 FIELDS
			// =========================
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_first_month(rs.getBigDecimal("R14_FIRST_MONTH"));
			obj.setR14_third_month(rs.getBigDecimal("R14_THIRD_MONTH"));
			obj.setR14_last_month(rs.getBigDecimal("R14_LAST_MONTH"));
			obj.setR14_first_year(rs.getBigDecimal("R14_FIRST_YEAR"));
			obj.setR14_fifth_year(rs.getBigDecimal("R14_FIFTH_YEAR"));
			obj.setR14_non_interest_bearing(rs.getBigDecimal("R14_NON_INTEREST_BEARING"));
			obj.setR14_cumulative_total(rs.getBigDecimal("R14_CUMULATIVE_TOTAL"));

			// =========================
			// R15 FIELDS
			// =========================
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_first_month(rs.getBigDecimal("R15_FIRST_MONTH"));
			obj.setR15_third_month(rs.getBigDecimal("R15_THIRD_MONTH"));
			obj.setR15_last_month(rs.getBigDecimal("R15_LAST_MONTH"));
			obj.setR15_first_year(rs.getBigDecimal("R15_FIRST_YEAR"));
			obj.setR15_fifth_year(rs.getBigDecimal("R15_FIFTH_YEAR"));
			obj.setR15_non_interest_bearing(rs.getBigDecimal("R15_NON_INTEREST_BEARING"));
			obj.setR15_cumulative_total(rs.getBigDecimal("R15_CUMULATIVE_TOTAL"));

			// =========================
			// R16 FIELDS
			// =========================
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_first_month(rs.getBigDecimal("R16_FIRST_MONTH"));
			obj.setR16_third_month(rs.getBigDecimal("R16_THIRD_MONTH"));
			obj.setR16_last_month(rs.getBigDecimal("R16_LAST_MONTH"));
			obj.setR16_first_year(rs.getBigDecimal("R16_FIRST_YEAR"));
			obj.setR16_fifth_year(rs.getBigDecimal("R16_FIFTH_YEAR"));
			obj.setR16_non_interest_bearing(rs.getBigDecimal("R16_NON_INTEREST_BEARING"));
			obj.setR16_cumulative_total(rs.getBigDecimal("R16_CUMULATIVE_TOTAL"));

			// =========================
			// R17 FIELDS
			// =========================
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_first_month(rs.getBigDecimal("R17_FIRST_MONTH"));
			obj.setR17_third_month(rs.getBigDecimal("R17_THIRD_MONTH"));
			obj.setR17_last_month(rs.getBigDecimal("R17_LAST_MONTH"));
			obj.setR17_first_year(rs.getBigDecimal("R17_FIRST_YEAR"));
			obj.setR17_fifth_year(rs.getBigDecimal("R17_FIFTH_YEAR"));
			obj.setR17_non_interest_bearing(rs.getBigDecimal("R17_NON_INTEREST_BEARING"));
			obj.setR17_cumulative_total(rs.getBigDecimal("R17_CUMULATIVE_TOTAL"));

			// =========================
			// R18 FIELDS
			// =========================
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_first_month(rs.getBigDecimal("R18_FIRST_MONTH"));
			obj.setR18_third_month(rs.getBigDecimal("R18_THIRD_MONTH"));
			obj.setR18_last_month(rs.getBigDecimal("R18_LAST_MONTH"));
			obj.setR18_first_year(rs.getBigDecimal("R18_FIRST_YEAR"));
			obj.setR18_fifth_year(rs.getBigDecimal("R18_FIFTH_YEAR"));
			obj.setR18_non_interest_bearing(rs.getBigDecimal("R18_NON_INTEREST_BEARING"));
			obj.setR18_cumulative_total(rs.getBigDecimal("R18_CUMULATIVE_TOTAL"));

			// =========================
			// R19 FIELDS
			// =========================
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_first_month(rs.getBigDecimal("R19_FIRST_MONTH"));
			obj.setR19_third_month(rs.getBigDecimal("R19_THIRD_MONTH"));
			obj.setR19_last_month(rs.getBigDecimal("R19_LAST_MONTH"));
			obj.setR19_first_year(rs.getBigDecimal("R19_FIRST_YEAR"));
			obj.setR19_fifth_year(rs.getBigDecimal("R19_FIFTH_YEAR"));
			obj.setR19_non_interest_bearing(rs.getBigDecimal("R19_NON_INTEREST_BEARING"));
			obj.setR19_cumulative_total(rs.getBigDecimal("R19_CUMULATIVE_TOTAL"));

			// =========================
			// R20 FIELDS
			// =========================
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_first_month(rs.getBigDecimal("R20_FIRST_MONTH"));
			obj.setR20_third_month(rs.getBigDecimal("R20_THIRD_MONTH"));
			obj.setR20_last_month(rs.getBigDecimal("R20_LAST_MONTH"));
			obj.setR20_first_year(rs.getBigDecimal("R20_FIRST_YEAR"));
			obj.setR20_fifth_year(rs.getBigDecimal("R20_FIFTH_YEAR"));
			obj.setR20_non_interest_bearing(rs.getBigDecimal("R20_NON_INTEREST_BEARING"));
			obj.setR20_cumulative_total(rs.getBigDecimal("R20_CUMULATIVE_TOTAL"));

			// =========================
			// R21 FIELDS
			// =========================
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_first_month(rs.getBigDecimal("R21_FIRST_MONTH"));
			obj.setR21_third_month(rs.getBigDecimal("R21_THIRD_MONTH"));
			obj.setR21_last_month(rs.getBigDecimal("R21_LAST_MONTH"));
			obj.setR21_first_year(rs.getBigDecimal("R21_FIRST_YEAR"));
			obj.setR21_fifth_year(rs.getBigDecimal("R21_FIFTH_YEAR"));
			obj.setR21_cumulative_total(rs.getBigDecimal("R21_CUMULATIVE_TOTAL"));

			// =========================
			// R22 FIELDS
			// =========================
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_first_month(rs.getBigDecimal("R22_FIRST_MONTH"));
			obj.setR22_third_month(rs.getBigDecimal("R22_THIRD_MONTH"));
			obj.setR22_last_month(rs.getBigDecimal("R22_LAST_MONTH"));
			obj.setR22_first_year(rs.getBigDecimal("R22_FIRST_YEAR"));
			obj.setR22_fifth_year(rs.getBigDecimal("R22_FIFTH_YEAR"));
			obj.setR22_non_interest_bearing(rs.getBigDecimal("R22_NON_INTEREST_BEARING"));
			obj.setR22_cumulative_total(rs.getBigDecimal("R22_CUMULATIVE_TOTAL"));

			// =========================
			// R23 FIELDS
			// =========================
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_first_month(rs.getBigDecimal("R23_FIRST_MONTH"));
			obj.setR23_third_month(rs.getBigDecimal("R23_THIRD_MONTH"));
			obj.setR23_last_month(rs.getBigDecimal("R23_LAST_MONTH"));
			obj.setR23_first_year(rs.getBigDecimal("R23_FIRST_YEAR"));
			obj.setR23_fifth_year(rs.getBigDecimal("R23_FIFTH_YEAR"));
			obj.setR23_non_interest_bearing(rs.getBigDecimal("R23_NON_INTEREST_BEARING"));
			obj.setR23_cumulative_total(rs.getBigDecimal("R23_CUMULATIVE_TOTAL"));

			// =========================
			// R24 FIELDS
			// =========================
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_first_month(rs.getBigDecimal("R24_FIRST_MONTH"));
			obj.setR24_third_month(rs.getBigDecimal("R24_THIRD_MONTH"));
			obj.setR24_last_month(rs.getBigDecimal("R24_LAST_MONTH"));
			obj.setR24_first_year(rs.getBigDecimal("R24_FIRST_YEAR"));
			obj.setR24_fifth_year(rs.getBigDecimal("R24_FIFTH_YEAR"));
			obj.setR24_non_interest_bearing(rs.getBigDecimal("R24_NON_INTEREST_BEARING"));
			obj.setR24_cumulative_total(rs.getBigDecimal("R24_CUMULATIVE_TOTAL"));

			// =========================
			// R25 FIELDS
			// =========================
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_first_month(rs.getBigDecimal("R25_FIRST_MONTH"));
			obj.setR25_third_month(rs.getBigDecimal("R25_THIRD_MONTH"));
			obj.setR25_last_month(rs.getBigDecimal("R25_LAST_MONTH"));
			obj.setR25_first_year(rs.getBigDecimal("R25_FIRST_YEAR"));
			obj.setR25_fifth_year(rs.getBigDecimal("R25_FIFTH_YEAR"));
			obj.setR25_non_interest_bearing(rs.getBigDecimal("R25_NON_INTEREST_BEARING"));
			obj.setR25_cumulative_total(rs.getBigDecimal("R25_CUMULATIVE_TOTAL"));

			// =========================
			// R26 FIELDS
			// =========================
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_first_month(rs.getBigDecimal("R26_FIRST_MONTH"));
			obj.setR26_third_month(rs.getBigDecimal("R26_THIRD_MONTH"));
			obj.setR26_last_month(rs.getBigDecimal("R26_LAST_MONTH"));
			obj.setR26_first_year(rs.getBigDecimal("R26_FIRST_YEAR"));
			obj.setR26_fifth_year(rs.getBigDecimal("R26_FIFTH_YEAR"));
			obj.setR26_non_interest_bearing(rs.getBigDecimal("R26_NON_INTEREST_BEARING"));
			obj.setR26_cumulative_total(rs.getBigDecimal("R26_CUMULATIVE_TOTAL"));

			// =========================
			// R27 FIELDS
			// =========================
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_first_month(rs.getBigDecimal("R27_FIRST_MONTH"));
			obj.setR27_third_month(rs.getBigDecimal("R27_THIRD_MONTH"));
			obj.setR27_last_month(rs.getBigDecimal("R27_LAST_MONTH"));
			obj.setR27_first_year(rs.getBigDecimal("R27_FIRST_YEAR"));
			obj.setR27_fifth_year(rs.getBigDecimal("R27_FIFTH_YEAR"));
			obj.setR27_non_interest_bearing(rs.getBigDecimal("R27_NON_INTEREST_BEARING"));
			obj.setR27_cumulative_total(rs.getBigDecimal("R27_CUMULATIVE_TOTAL"));

			// =========================
			// R28 FIELDS
			// =========================
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_first_month(rs.getBigDecimal("R28_FIRST_MONTH"));
			obj.setR28_third_month(rs.getBigDecimal("R28_THIRD_MONTH"));
			obj.setR28_last_month(rs.getBigDecimal("R28_LAST_MONTH"));
			obj.setR28_first_year(rs.getBigDecimal("R28_FIRST_YEAR"));
			obj.setR28_fifth_year(rs.getBigDecimal("R28_FIFTH_YEAR"));
			obj.setR28_non_interest_bearing(rs.getBigDecimal("R28_NON_INTEREST_BEARING"));
			obj.setR28_cumulative_total(rs.getBigDecimal("R28_CUMULATIVE_TOTAL"));

			// =========================
			// R29 FIELDS
			// =========================
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_first_month(rs.getBigDecimal("R29_FIRST_MONTH"));
			obj.setR29_third_month(rs.getBigDecimal("R29_THIRD_MONTH"));
			obj.setR29_last_month(rs.getBigDecimal("R29_LAST_MONTH"));
			obj.setR29_first_year(rs.getBigDecimal("R29_FIRST_YEAR"));
			obj.setR29_fifth_year(rs.getBigDecimal("R29_FIFTH_YEAR"));
			obj.setR29_non_interest_bearing(rs.getBigDecimal("R29_NON_INTEREST_BEARING"));
			obj.setR29_cumulative_total(rs.getBigDecimal("R29_CUMULATIVE_TOTAL"));

			// =========================
			// R30 FIELDS
			// =========================
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_first_month(rs.getBigDecimal("R30_FIRST_MONTH"));
			obj.setR30_third_month(rs.getBigDecimal("R30_THIRD_MONTH"));
			obj.setR30_last_month(rs.getBigDecimal("R30_LAST_MONTH"));
			obj.setR30_first_year(rs.getBigDecimal("R30_FIRST_YEAR"));
			obj.setR30_fifth_year(rs.getBigDecimal("R30_FIFTH_YEAR"));
			obj.setR30_non_interest_bearing(rs.getBigDecimal("R30_NON_INTEREST_BEARING"));
			obj.setR30_cumulative_total(rs.getBigDecimal("R30_CUMULATIVE_TOTAL"));

			// =========================
			// R31 FIELDS
			// =========================
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_first_month(rs.getBigDecimal("R31_FIRST_MONTH"));
			obj.setR31_third_month(rs.getBigDecimal("R31_THIRD_MONTH"));
			obj.setR31_last_month(rs.getBigDecimal("R31_LAST_MONTH"));
			obj.setR31_first_year(rs.getBigDecimal("R31_FIRST_YEAR"));
			obj.setR31_fifth_year(rs.getBigDecimal("R31_FIFTH_YEAR"));
			obj.setR31_non_interest_bearing(rs.getBigDecimal("R31_NON_INTEREST_BEARING"));
			obj.setR31_cumulative_total(rs.getBigDecimal("R31_CUMULATIVE_TOTAL"));

			// =========================
			// R32 FIELDS
			// =========================
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_first_month(rs.getBigDecimal("R32_FIRST_MONTH"));
			obj.setR32_third_month(rs.getBigDecimal("R32_THIRD_MONTH"));
			obj.setR32_last_month(rs.getBigDecimal("R32_LAST_MONTH"));
			obj.setR32_first_year(rs.getBigDecimal("R32_FIRST_YEAR"));
			obj.setR32_fifth_year(rs.getBigDecimal("R32_FIFTH_YEAR"));
			obj.setR32_cumulative_total(rs.getBigDecimal("R32_CUMULATIVE_TOTAL"));

			// =========================
			// R33 FIELDS
			// =========================
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_first_month(rs.getBigDecimal("R33_FIRST_MONTH"));
			obj.setR33_cumulative_total(rs.getBigDecimal("R33_CUMULATIVE_TOTAL"));

			// =========================
			// R34 FIELDS
			// =========================
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_third_month(rs.getBigDecimal("R34_THIRD_MONTH"));
			obj.setR34_last_month(rs.getBigDecimal("R34_LAST_MONTH"));
			obj.setR34_first_year(rs.getBigDecimal("R34_FIRST_YEAR"));
			obj.setR34_fifth_year(rs.getBigDecimal("R34_FIFTH_YEAR"));
			obj.setR34_non_interest_bearing(rs.getBigDecimal("R34_NON_INTEREST_BEARING"));
			obj.setR34_cumulative_total(rs.getBigDecimal("R34_CUMULATIVE_TOTAL"));

			// =========================
			// R35 FIELDS
			// =========================
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_first_month(rs.getBigDecimal("R35_FIRST_MONTH"));
			obj.setR35_third_month(rs.getBigDecimal("R35_THIRD_MONTH"));
			obj.setR35_last_month(rs.getBigDecimal("R35_LAST_MONTH"));
			obj.setR35_first_year(rs.getBigDecimal("R35_FIRST_YEAR"));
			obj.setR35_fifth_year(rs.getBigDecimal("R35_FIFTH_YEAR"));
			obj.setR35_non_interest_bearing(rs.getBigDecimal("R35_NON_INTEREST_BEARING"));
			obj.setR35_cumulative_total(rs.getBigDecimal("R35_CUMULATIVE_TOTAL"));

			// =========================
			// R36 FIELDS
			// =========================
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_first_month(rs.getBigDecimal("R36_FIRST_MONTH"));
			obj.setR36_third_month(rs.getBigDecimal("R36_THIRD_MONTH"));
			obj.setR36_last_month(rs.getBigDecimal("R36_LAST_MONTH"));
			obj.setR36_first_year(rs.getBigDecimal("R36_FIRST_YEAR"));
			obj.setR36_fifth_year(rs.getBigDecimal("R36_FIFTH_YEAR"));
			obj.setR36_non_interest_bearing(rs.getBigDecimal("R36_NON_INTEREST_BEARING"));
			obj.setR36_cumulative_total(rs.getBigDecimal("R36_CUMULATIVE_TOTAL"));

			// =========================
			// R37 FIELDS
			// =========================
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_first_month(rs.getBigDecimal("R37_FIRST_MONTH"));
			obj.setR37_third_month(rs.getBigDecimal("R37_THIRD_MONTH"));
			obj.setR37_last_month(rs.getBigDecimal("R37_LAST_MONTH"));
			obj.setR37_first_year(rs.getBigDecimal("R37_FIRST_YEAR"));
			obj.setR37_fifth_year(rs.getBigDecimal("R37_FIFTH_YEAR"));
			obj.setR37_non_interest_bearing(rs.getBigDecimal("R37_NON_INTEREST_BEARING"));
			obj.setR37_cumulative_total(rs.getBigDecimal("R37_CUMULATIVE_TOTAL"));

			// =========================
			// R38 FIELDS
			// =========================
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_first_month(rs.getBigDecimal("R38_FIRST_MONTH"));
			obj.setR38_third_month(rs.getBigDecimal("R38_THIRD_MONTH"));
			obj.setR38_last_month(rs.getBigDecimal("R38_LAST_MONTH"));
			obj.setR38_first_year(rs.getBigDecimal("R38_FIRST_YEAR"));
			obj.setR38_fifth_year(rs.getBigDecimal("R38_FIFTH_YEAR"));
			obj.setR38_non_interest_bearing(rs.getBigDecimal("R38_NON_INTEREST_BEARING"));
			obj.setR38_cumulative_total(rs.getBigDecimal("R38_CUMULATIVE_TOTAL"));

			// =========================
			// R39 FIELDS
			// =========================
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_first_month(rs.getBigDecimal("R39_FIRST_MONTH"));
			obj.setR39_third_month(rs.getBigDecimal("R39_THIRD_MONTH"));
			obj.setR39_last_month(rs.getBigDecimal("R39_LAST_MONTH"));
			obj.setR39_first_year(rs.getBigDecimal("R39_FIRST_YEAR"));
			obj.setR39_fifth_year(rs.getBigDecimal("R39_FIFTH_YEAR"));
			obj.setR39_non_interest_bearing(rs.getBigDecimal("R39_NON_INTEREST_BEARING"));
			obj.setR39_cumulative_total(rs.getBigDecimal("R39_CUMULATIVE_TOTAL"));

			// =========================
			// R40 FIELDS
			// =========================
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_first_month(rs.getBigDecimal("R40_FIRST_MONTH"));
			obj.setR40_third_month(rs.getBigDecimal("R40_THIRD_MONTH"));
			obj.setR40_last_month(rs.getBigDecimal("R40_LAST_MONTH"));
			obj.setR40_first_year(rs.getBigDecimal("R40_FIRST_YEAR"));
			obj.setR40_fifth_year(rs.getBigDecimal("R40_FIFTH_YEAR"));
			obj.setR40_non_interest_bearing(rs.getBigDecimal("R40_NON_INTEREST_BEARING"));
			obj.setR40_cumulative_total(rs.getBigDecimal("R40_CUMULATIVE_TOTAL"));

			// =========================
			// R41 FIELDS
			// =========================
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_first_month(rs.getBigDecimal("R41_FIRST_MONTH"));
			obj.setR41_third_month(rs.getBigDecimal("R41_THIRD_MONTH"));
			obj.setR41_last_month(rs.getBigDecimal("R41_LAST_MONTH"));
			obj.setR41_first_year(rs.getBigDecimal("R41_FIRST_YEAR"));
			obj.setR41_fifth_year(rs.getBigDecimal("R41_FIFTH_YEAR"));
			obj.setR41_non_interest_bearing(rs.getBigDecimal("R41_NON_INTEREST_BEARING"));
			obj.setR41_cumulative_total(rs.getBigDecimal("R41_CUMULATIVE_TOTAL"));

			// =========================
			// R42 FIELDS
			// =========================
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_first_month(rs.getBigDecimal("R42_FIRST_MONTH"));
			obj.setR42_third_month(rs.getBigDecimal("R42_THIRD_MONTH"));
			obj.setR42_last_month(rs.getBigDecimal("R42_LAST_MONTH"));
			obj.setR42_first_year(rs.getBigDecimal("R42_FIRST_YEAR"));
			obj.setR42_fifth_year(rs.getBigDecimal("R42_FIFTH_YEAR"));
			obj.setR42_non_interest_bearing(rs.getBigDecimal("R42_NON_INTEREST_BEARING"));
			obj.setR42_cumulative_total(rs.getBigDecimal("R42_CUMULATIVE_TOTAL"));

			// =========================
			// R43 FIELDS
			// =========================
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_first_month(rs.getBigDecimal("R43_FIRST_MONTH"));
			obj.setR43_third_month(rs.getBigDecimal("R43_THIRD_MONTH"));
			obj.setR43_last_month(rs.getBigDecimal("R43_LAST_MONTH"));
			obj.setR43_first_year(rs.getBigDecimal("R43_FIRST_YEAR"));
			obj.setR43_fifth_year(rs.getBigDecimal("R43_FIFTH_YEAR"));
			obj.setR43_non_interest_bearing(rs.getBigDecimal("R43_NON_INTEREST_BEARING"));
			obj.setR43_cumulative_total(rs.getBigDecimal("R43_CUMULATIVE_TOTAL"));

			// =========================
			// R44 FIELDS
			// =========================
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_first_month(rs.getBigDecimal("R44_FIRST_MONTH"));
			obj.setR44_third_month(rs.getBigDecimal("R44_THIRD_MONTH"));
			obj.setR44_last_month(rs.getBigDecimal("R44_LAST_MONTH"));
			obj.setR44_first_year(rs.getBigDecimal("R44_FIRST_YEAR"));
			obj.setR44_fifth_year(rs.getBigDecimal("R44_FIFTH_YEAR"));
			obj.setR44_non_interest_bearing(rs.getBigDecimal("R44_NON_INTEREST_BEARING"));
			obj.setR44_cumulative_total(rs.getBigDecimal("R44_CUMULATIVE_TOTAL"));

			// =========================
			// R45 FIELDS
			// =========================
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_first_month(rs.getBigDecimal("R45_FIRST_MONTH"));
			obj.setR45_third_month(rs.getBigDecimal("R45_THIRD_MONTH"));
			obj.setR45_last_month(rs.getBigDecimal("R45_LAST_MONTH"));
			obj.setR45_first_year(rs.getBigDecimal("R45_FIRST_YEAR"));
			obj.setR45_fifth_year(rs.getBigDecimal("R45_FIFTH_YEAR"));
			obj.setR45_non_interest_bearing(rs.getBigDecimal("R45_NON_INTEREST_BEARING"));
			obj.setR45_cumulative_total(rs.getBigDecimal("R45_CUMULATIVE_TOTAL"));

			// =========================
			// R46 FIELDS
			// =========================
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_first_month(rs.getBigDecimal("R46_FIRST_MONTH"));
			obj.setR46_third_month(rs.getBigDecimal("R46_THIRD_MONTH"));
			obj.setR46_last_month(rs.getBigDecimal("R46_LAST_MONTH"));
			obj.setR46_first_year(rs.getBigDecimal("R46_FIRST_YEAR"));
			obj.setR46_fifth_year(rs.getBigDecimal("R46_FIFTH_YEAR"));
			obj.setR46_non_interest_bearing(rs.getBigDecimal("R46_NON_INTEREST_BEARING"));
			obj.setR46_cumulative_total(rs.getBigDecimal("R46_CUMULATIVE_TOTAL"));

			// =========================
			// R47 FIELDS
			// =========================
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_first_month(rs.getBigDecimal("R47_FIRST_MONTH"));
			obj.setR47_third_month(rs.getBigDecimal("R47_THIRD_MONTH"));
			obj.setR47_last_month(rs.getBigDecimal("R47_LAST_MONTH"));
			obj.setR47_first_year(rs.getBigDecimal("R47_FIRST_YEAR"));
			obj.setR47_fifth_year(rs.getBigDecimal("R47_FIFTH_YEAR"));
			obj.setR47_non_interest_bearing(rs.getBigDecimal("R47_NON_INTEREST_BEARING"));
			obj.setR47_cumulative_total(rs.getBigDecimal("R47_CUMULATIVE_TOTAL"));

			// =========================
			// R48 FIELDS
			// =========================
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_first_month(rs.getBigDecimal("R48_FIRST_MONTH"));
			obj.setR48_third_month(rs.getBigDecimal("R48_THIRD_MONTH"));
			obj.setR48_last_month(rs.getBigDecimal("R48_LAST_MONTH"));
			obj.setR48_first_year(rs.getBigDecimal("R48_FIRST_YEAR"));
			obj.setR48_fifth_year(rs.getBigDecimal("R48_FIFTH_YEAR"));
			obj.setR48_non_interest_bearing(rs.getBigDecimal("R48_NON_INTEREST_BEARING"));
			obj.setR48_cumulative_total(rs.getBigDecimal("R48_CUMULATIVE_TOTAL"));

			// =========================
			// R49 FIELDS
			// =========================
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_first_month(rs.getBigDecimal("R49_FIRST_MONTH"));
			obj.setR49_third_month(rs.getBigDecimal("R49_THIRD_MONTH"));
			obj.setR49_last_month(rs.getBigDecimal("R49_LAST_MONTH"));
			obj.setR49_first_year(rs.getBigDecimal("R49_FIRST_YEAR"));
			obj.setR49_fifth_year(rs.getBigDecimal("R49_FIFTH_YEAR"));
			obj.setR49_non_interest_bearing(rs.getBigDecimal("R49_NON_INTEREST_BEARING"));
			obj.setR49_cumulative_total(rs.getBigDecimal("R49_CUMULATIVE_TOTAL"));

			// =========================
			// R50 FIELDS
			// =========================
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_first_month(rs.getBigDecimal("R50_FIRST_MONTH"));
			obj.setR50_third_month(rs.getBigDecimal("R50_THIRD_MONTH"));
			obj.setR50_last_month(rs.getBigDecimal("R50_LAST_MONTH"));
			obj.setR50_first_year(rs.getBigDecimal("R50_FIRST_YEAR"));
			obj.setR50_fifth_year(rs.getBigDecimal("R50_FIFTH_YEAR"));
			obj.setR50_non_interest_bearing(rs.getBigDecimal("R50_NON_INTEREST_BEARING"));
			obj.setR50_cumulative_total(rs.getBigDecimal("R50_CUMULATIVE_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Summary_Entity
	// ===========================

	public class M_LIQGAP_Summary_Entity {

		private String r10_product;
		private BigDecimal r10_first_month;
		private BigDecimal r10_third_month;
		private BigDecimal r10_last_month;
		private BigDecimal r10_first_year;
		private BigDecimal r10_fifth_year;
		private BigDecimal r10_non_interest_bearing;
		private BigDecimal r10_cumulative_total;

		private String r11_product;
		private BigDecimal r11_first_month;
		private BigDecimal r11_third_month;
		private BigDecimal r11_last_month;
		private BigDecimal r11_first_year;
		private BigDecimal r11_fifth_year;
		private BigDecimal r11_non_interest_bearing;
		private BigDecimal r11_cumulative_total;

		private String r12_product;
		private BigDecimal r12_first_month;
		private BigDecimal r12_third_month;
		private BigDecimal r12_last_month;
		private BigDecimal r12_first_year;
		private BigDecimal r12_fifth_year;
		private BigDecimal r12_non_interest_bearing;
		private BigDecimal r12_cumulative_total;

		private String r13_product;
		private BigDecimal r13_first_month;
		private BigDecimal r13_third_month;
		private BigDecimal r13_last_month;
		private BigDecimal r13_first_year;
		private BigDecimal r13_fifth_year;
		private BigDecimal r13_non_interest_bearing;
		private BigDecimal r13_cumulative_total;

		private String r14_product;
		private BigDecimal r14_first_month;
		private BigDecimal r14_third_month;
		private BigDecimal r14_last_month;
		private BigDecimal r14_first_year;
		private BigDecimal r14_fifth_year;
		private BigDecimal r14_non_interest_bearing;
		private BigDecimal r14_cumulative_total;

		private String r15_product;
		private BigDecimal r15_first_month;
		private BigDecimal r15_third_month;
		private BigDecimal r15_last_month;
		private BigDecimal r15_first_year;
		private BigDecimal r15_fifth_year;
		private BigDecimal r15_non_interest_bearing;
		private BigDecimal r15_cumulative_total;

		private String r16_product;
		private BigDecimal r16_first_month;
		private BigDecimal r16_third_month;
		private BigDecimal r16_last_month;
		private BigDecimal r16_first_year;
		private BigDecimal r16_fifth_year;
		private BigDecimal r16_non_interest_bearing;
		private BigDecimal r16_cumulative_total;

		private String r17_product;
		private BigDecimal r17_first_month;
		private BigDecimal r17_third_month;
		private BigDecimal r17_last_month;
		private BigDecimal r17_first_year;
		private BigDecimal r17_fifth_year;
		private BigDecimal r17_non_interest_bearing;
		private BigDecimal r17_cumulative_total;

		private String r18_product;
		private BigDecimal r18_first_month;
		private BigDecimal r18_third_month;
		private BigDecimal r18_last_month;
		private BigDecimal r18_first_year;
		private BigDecimal r18_fifth_year;
		private BigDecimal r18_non_interest_bearing;
		private BigDecimal r18_cumulative_total;

		private String r19_product;
		private BigDecimal r19_first_month;
		private BigDecimal r19_third_month;
		private BigDecimal r19_last_month;
		private BigDecimal r19_first_year;
		private BigDecimal r19_fifth_year;
		private BigDecimal r19_non_interest_bearing;
		private BigDecimal r19_cumulative_total;

		private String r20_product;
		private BigDecimal r20_first_month;
		private BigDecimal r20_third_month;
		private BigDecimal r20_last_month;
		private BigDecimal r20_first_year;
		private BigDecimal r20_fifth_year;
		private BigDecimal r20_non_interest_bearing;
		private BigDecimal r20_cumulative_total;

		private String r21_product;
		private BigDecimal r21_first_month;
		private BigDecimal r21_third_month;
		private BigDecimal r21_last_month;
		private BigDecimal r21_first_year;
		private BigDecimal r21_fifth_year;
		private BigDecimal r21_cumulative_total;

		private String r22_product;
		private BigDecimal r22_first_month;
		private BigDecimal r22_third_month;
		private BigDecimal r22_last_month;
		private BigDecimal r22_first_year;
		private BigDecimal r22_fifth_year;
		private BigDecimal r22_non_interest_bearing;
		private BigDecimal r22_cumulative_total;

		private String r23_product;
		private BigDecimal r23_first_month;
		private BigDecimal r23_third_month;
		private BigDecimal r23_last_month;
		private BigDecimal r23_first_year;
		private BigDecimal r23_fifth_year;
		private BigDecimal r23_non_interest_bearing;
		private BigDecimal r23_cumulative_total;

		private String r24_product;
		private BigDecimal r24_first_month;
		private BigDecimal r24_third_month;
		private BigDecimal r24_last_month;
		private BigDecimal r24_first_year;
		private BigDecimal r24_fifth_year;
		private BigDecimal r24_non_interest_bearing;
		private BigDecimal r24_cumulative_total;

		private String r25_product;
		private BigDecimal r25_first_month;
		private BigDecimal r25_third_month;
		private BigDecimal r25_last_month;
		private BigDecimal r25_first_year;
		private BigDecimal r25_fifth_year;
		private BigDecimal r25_non_interest_bearing;
		private BigDecimal r25_cumulative_total;

		private String r26_product;
		private BigDecimal r26_first_month;
		private BigDecimal r26_third_month;
		private BigDecimal r26_last_month;
		private BigDecimal r26_first_year;
		private BigDecimal r26_fifth_year;
		private BigDecimal r26_non_interest_bearing;
		private BigDecimal r26_cumulative_total;

		private String r27_product;
		private BigDecimal r27_first_month;
		private BigDecimal r27_third_month;
		private BigDecimal r27_last_month;
		private BigDecimal r27_first_year;
		private BigDecimal r27_fifth_year;
		private BigDecimal r27_non_interest_bearing;
		private BigDecimal r27_cumulative_total;

		private String r28_product;
		private BigDecimal r28_first_month;
		private BigDecimal r28_third_month;
		private BigDecimal r28_last_month;
		private BigDecimal r28_first_year;
		private BigDecimal r28_fifth_year;
		private BigDecimal r28_non_interest_bearing;
		private BigDecimal r28_cumulative_total;

		private String r29_product;
		private BigDecimal r29_first_month;
		private BigDecimal r29_third_month;
		private BigDecimal r29_last_month;
		private BigDecimal r29_first_year;
		private BigDecimal r29_fifth_year;
		private BigDecimal r29_non_interest_bearing;
		private BigDecimal r29_cumulative_total;

		private String r30_product;
		private BigDecimal r30_first_month;
		private BigDecimal r30_third_month;
		private BigDecimal r30_last_month;
		private BigDecimal r30_first_year;
		private BigDecimal r30_fifth_year;
		private BigDecimal r30_non_interest_bearing;
		private BigDecimal r30_cumulative_total;

		private String r31_product;
		private BigDecimal r31_first_month;
		private BigDecimal r31_third_month;
		private BigDecimal r31_last_month;
		private BigDecimal r31_first_year;
		private BigDecimal r31_fifth_year;
		private BigDecimal r31_non_interest_bearing;
		private BigDecimal r31_cumulative_total;

		private String r32_product;
		private BigDecimal r32_first_month;
		private BigDecimal r32_third_month;
		private BigDecimal r32_last_month;
		private BigDecimal r32_first_year;
		private BigDecimal r32_fifth_year;
		private BigDecimal r32_cumulative_total;

		private String r33_product;
		private BigDecimal r33_first_month;
		private BigDecimal r33_cumulative_total;

		private String r34_product;
		private BigDecimal r34_third_month;
		private BigDecimal r34_last_month;
		private BigDecimal r34_first_year;
		private BigDecimal r34_fifth_year;
		private BigDecimal r34_non_interest_bearing;
		private BigDecimal r34_cumulative_total;

		private String r35_product;
		private BigDecimal r35_first_month;
		private BigDecimal r35_third_month;
		private BigDecimal r35_last_month;
		private BigDecimal r35_first_year;
		private BigDecimal r35_fifth_year;
		private BigDecimal r35_non_interest_bearing;
		private BigDecimal r35_cumulative_total;

		private String r36_product;
		private BigDecimal r36_first_month;
		private BigDecimal r36_third_month;
		private BigDecimal r36_last_month;
		private BigDecimal r36_first_year;
		private BigDecimal r36_fifth_year;
		private BigDecimal r36_non_interest_bearing;
		private BigDecimal r36_cumulative_total;

		private String r37_product;
		private BigDecimal r37_first_month;
		private BigDecimal r37_third_month;
		private BigDecimal r37_last_month;
		private BigDecimal r37_first_year;
		private BigDecimal r37_fifth_year;
		private BigDecimal r37_non_interest_bearing;
		private BigDecimal r37_cumulative_total;

		private String r38_product;
		private BigDecimal r38_first_month;
		private BigDecimal r38_third_month;
		private BigDecimal r38_last_month;
		private BigDecimal r38_first_year;
		private BigDecimal r38_fifth_year;
		private BigDecimal r38_non_interest_bearing;
		private BigDecimal r38_cumulative_total;

		private String r39_product;
		private BigDecimal r39_first_month;
		private BigDecimal r39_third_month;
		private BigDecimal r39_last_month;
		private BigDecimal r39_first_year;
		private BigDecimal r39_fifth_year;
		private BigDecimal r39_non_interest_bearing;
		private BigDecimal r39_cumulative_total;

		private String r40_product;
		private BigDecimal r40_first_month;
		private BigDecimal r40_third_month;
		private BigDecimal r40_last_month;
		private BigDecimal r40_first_year;
		private BigDecimal r40_fifth_year;
		private BigDecimal r40_non_interest_bearing;
		private BigDecimal r40_cumulative_total;

		private String r41_product;
		private BigDecimal r41_first_month;
		private BigDecimal r41_third_month;
		private BigDecimal r41_last_month;
		private BigDecimal r41_first_year;
		private BigDecimal r41_fifth_year;
		private BigDecimal r41_non_interest_bearing;
		private BigDecimal r41_cumulative_total;

		private String r42_product;
		private BigDecimal r42_first_month;
		private BigDecimal r42_third_month;
		private BigDecimal r42_last_month;
		private BigDecimal r42_first_year;
		private BigDecimal r42_fifth_year;
		private BigDecimal r42_non_interest_bearing;
		private BigDecimal r42_cumulative_total;

		private String r43_product;
		private BigDecimal r43_first_month;
		private BigDecimal r43_third_month;
		private BigDecimal r43_last_month;
		private BigDecimal r43_first_year;
		private BigDecimal r43_fifth_year;
		private BigDecimal r43_non_interest_bearing;
		private BigDecimal r43_cumulative_total;

		private String r44_product;
		private BigDecimal r44_first_month;
		private BigDecimal r44_third_month;
		private BigDecimal r44_last_month;
		private BigDecimal r44_first_year;
		private BigDecimal r44_fifth_year;
		private BigDecimal r44_non_interest_bearing;
		private BigDecimal r44_cumulative_total;

		private String r45_product;
		private BigDecimal r45_first_month;
		private BigDecimal r45_third_month;
		private BigDecimal r45_last_month;
		private BigDecimal r45_first_year;
		private BigDecimal r45_fifth_year;
		private BigDecimal r45_non_interest_bearing;
		private BigDecimal r45_cumulative_total;

		private String r46_product;
		private BigDecimal r46_first_month;
		private BigDecimal r46_third_month;
		private BigDecimal r46_last_month;
		private BigDecimal r46_first_year;
		private BigDecimal r46_fifth_year;
		private BigDecimal r46_non_interest_bearing;
		private BigDecimal r46_cumulative_total;

		private String r47_product;
		private BigDecimal r47_first_month;
		private BigDecimal r47_third_month;
		private BigDecimal r47_last_month;
		private BigDecimal r47_first_year;
		private BigDecimal r47_fifth_year;
		private BigDecimal r47_non_interest_bearing;
		private BigDecimal r47_cumulative_total;

		private String r48_product;
		private BigDecimal r48_first_month;
		private BigDecimal r48_third_month;
		private BigDecimal r48_last_month;
		private BigDecimal r48_first_year;
		private BigDecimal r48_fifth_year;
		private BigDecimal r48_non_interest_bearing;
		private BigDecimal r48_cumulative_total;

		private String r49_product;
		private BigDecimal r49_first_month;
		private BigDecimal r49_third_month;
		private BigDecimal r49_last_month;
		private BigDecimal r49_first_year;
		private BigDecimal r49_fifth_year;
		private BigDecimal r49_non_interest_bearing;
		private BigDecimal r49_cumulative_total;

		private String r50_product;
		private BigDecimal r50_first_month;
		private BigDecimal r50_third_month;
		private BigDecimal r50_last_month;
		private BigDecimal r50_first_year;
		private BigDecimal r50_fifth_year;
		private BigDecimal r50_non_interest_bearing;
		private BigDecimal r50_cumulative_total;

		// Common Fields
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default constructor
		public M_LIQGAP_Summary_Entity() {
			super();
		}

		// Getters and Setters for R10
		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_first_month() {
			return r10_first_month;
		}

		public void setR10_first_month(BigDecimal r10_first_month) {
			this.r10_first_month = r10_first_month;
		}

		public BigDecimal getR10_third_month() {
			return r10_third_month;
		}

		public void setR10_third_month(BigDecimal r10_third_month) {
			this.r10_third_month = r10_third_month;
		}

		public BigDecimal getR10_last_month() {
			return r10_last_month;
		}

		public void setR10_last_month(BigDecimal r10_last_month) {
			this.r10_last_month = r10_last_month;
		}

		public BigDecimal getR10_first_year() {
			return r10_first_year;
		}

		public void setR10_first_year(BigDecimal r10_first_year) {
			this.r10_first_year = r10_first_year;
		}

		public BigDecimal getR10_fifth_year() {
			return r10_fifth_year;
		}

		public void setR10_fifth_year(BigDecimal r10_fifth_year) {
			this.r10_fifth_year = r10_fifth_year;
		}

		public BigDecimal getR10_non_interest_bearing() {
			return r10_non_interest_bearing;
		}

		public void setR10_non_interest_bearing(BigDecimal r10_non_interest_bearing) {
			this.r10_non_interest_bearing = r10_non_interest_bearing;
		}

		public BigDecimal getR10_cumulative_total() {
			return r10_cumulative_total;
		}

		public void setR10_cumulative_total(BigDecimal r10_cumulative_total) {
			this.r10_cumulative_total = r10_cumulative_total;
		}

		// Getters and Setters for R11
		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_first_month() {
			return r11_first_month;
		}

		public void setR11_first_month(BigDecimal r11_first_month) {
			this.r11_first_month = r11_first_month;
		}

		public BigDecimal getR11_third_month() {
			return r11_third_month;
		}

		public void setR11_third_month(BigDecimal r11_third_month) {
			this.r11_third_month = r11_third_month;
		}

		public BigDecimal getR11_last_month() {
			return r11_last_month;
		}

		public void setR11_last_month(BigDecimal r11_last_month) {
			this.r11_last_month = r11_last_month;
		}

		public BigDecimal getR11_first_year() {
			return r11_first_year;
		}

		public void setR11_first_year(BigDecimal r11_first_year) {
			this.r11_first_year = r11_first_year;
		}

		public BigDecimal getR11_fifth_year() {
			return r11_fifth_year;
		}

		public void setR11_fifth_year(BigDecimal r11_fifth_year) {
			this.r11_fifth_year = r11_fifth_year;
		}

		public BigDecimal getR11_non_interest_bearing() {
			return r11_non_interest_bearing;
		}

		public void setR11_non_interest_bearing(BigDecimal r11_non_interest_bearing) {
			this.r11_non_interest_bearing = r11_non_interest_bearing;
		}

		public BigDecimal getR11_cumulative_total() {
			return r11_cumulative_total;
		}

		public void setR11_cumulative_total(BigDecimal r11_cumulative_total) {
			this.r11_cumulative_total = r11_cumulative_total;
		}

		// Getters and Setters for R12
		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_first_month() {
			return r12_first_month;
		}

		public void setR12_first_month(BigDecimal r12_first_month) {
			this.r12_first_month = r12_first_month;
		}

		public BigDecimal getR12_third_month() {
			return r12_third_month;
		}

		public void setR12_third_month(BigDecimal r12_third_month) {
			this.r12_third_month = r12_third_month;
		}

		public BigDecimal getR12_last_month() {
			return r12_last_month;
		}

		public void setR12_last_month(BigDecimal r12_last_month) {
			this.r12_last_month = r12_last_month;
		}

		public BigDecimal getR12_first_year() {
			return r12_first_year;
		}

		public void setR12_first_year(BigDecimal r12_first_year) {
			this.r12_first_year = r12_first_year;
		}

		public BigDecimal getR12_fifth_year() {
			return r12_fifth_year;
		}

		public void setR12_fifth_year(BigDecimal r12_fifth_year) {
			this.r12_fifth_year = r12_fifth_year;
		}

		public BigDecimal getR12_non_interest_bearing() {
			return r12_non_interest_bearing;
		}

		public void setR12_non_interest_bearing(BigDecimal r12_non_interest_bearing) {
			this.r12_non_interest_bearing = r12_non_interest_bearing;
		}

		public BigDecimal getR12_cumulative_total() {
			return r12_cumulative_total;
		}

		public void setR12_cumulative_total(BigDecimal r12_cumulative_total) {
			this.r12_cumulative_total = r12_cumulative_total;
		}

		// Getters and Setters for R13
		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_first_month() {
			return r13_first_month;
		}

		public void setR13_first_month(BigDecimal r13_first_month) {
			this.r13_first_month = r13_first_month;
		}

		public BigDecimal getR13_third_month() {
			return r13_third_month;
		}

		public void setR13_third_month(BigDecimal r13_third_month) {
			this.r13_third_month = r13_third_month;
		}

		public BigDecimal getR13_last_month() {
			return r13_last_month;
		}

		public void setR13_last_month(BigDecimal r13_last_month) {
			this.r13_last_month = r13_last_month;
		}

		public BigDecimal getR13_first_year() {
			return r13_first_year;
		}

		public void setR13_first_year(BigDecimal r13_first_year) {
			this.r13_first_year = r13_first_year;
		}

		public BigDecimal getR13_fifth_year() {
			return r13_fifth_year;
		}

		public void setR13_fifth_year(BigDecimal r13_fifth_year) {
			this.r13_fifth_year = r13_fifth_year;
		}

		public BigDecimal getR13_non_interest_bearing() {
			return r13_non_interest_bearing;
		}

		public void setR13_non_interest_bearing(BigDecimal r13_non_interest_bearing) {
			this.r13_non_interest_bearing = r13_non_interest_bearing;
		}

		public BigDecimal getR13_cumulative_total() {
			return r13_cumulative_total;
		}

		public void setR13_cumulative_total(BigDecimal r13_cumulative_total) {
			this.r13_cumulative_total = r13_cumulative_total;
		}

		// Getters and Setters for R14
		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_first_month() {
			return r14_first_month;
		}

		public void setR14_first_month(BigDecimal r14_first_month) {
			this.r14_first_month = r14_first_month;
		}

		public BigDecimal getR14_third_month() {
			return r14_third_month;
		}

		public void setR14_third_month(BigDecimal r14_third_month) {
			this.r14_third_month = r14_third_month;
		}

		public BigDecimal getR14_last_month() {
			return r14_last_month;
		}

		public void setR14_last_month(BigDecimal r14_last_month) {
			this.r14_last_month = r14_last_month;
		}

		public BigDecimal getR14_first_year() {
			return r14_first_year;
		}

		public void setR14_first_year(BigDecimal r14_first_year) {
			this.r14_first_year = r14_first_year;
		}

		public BigDecimal getR14_fifth_year() {
			return r14_fifth_year;
		}

		public void setR14_fifth_year(BigDecimal r14_fifth_year) {
			this.r14_fifth_year = r14_fifth_year;
		}

		public BigDecimal getR14_non_interest_bearing() {
			return r14_non_interest_bearing;
		}

		public void setR14_non_interest_bearing(BigDecimal r14_non_interest_bearing) {
			this.r14_non_interest_bearing = r14_non_interest_bearing;
		}

		public BigDecimal getR14_cumulative_total() {
			return r14_cumulative_total;
		}

		public void setR14_cumulative_total(BigDecimal r14_cumulative_total) {
			this.r14_cumulative_total = r14_cumulative_total;
		}

		// Getters and Setters for R15
		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_first_month() {
			return r15_first_month;
		}

		public void setR15_first_month(BigDecimal r15_first_month) {
			this.r15_first_month = r15_first_month;
		}

		public BigDecimal getR15_third_month() {
			return r15_third_month;
		}

		public void setR15_third_month(BigDecimal r15_third_month) {
			this.r15_third_month = r15_third_month;
		}

		public BigDecimal getR15_last_month() {
			return r15_last_month;
		}

		public void setR15_last_month(BigDecimal r15_last_month) {
			this.r15_last_month = r15_last_month;
		}

		public BigDecimal getR15_first_year() {
			return r15_first_year;
		}

		public void setR15_first_year(BigDecimal r15_first_year) {
			this.r15_first_year = r15_first_year;
		}

		public BigDecimal getR15_fifth_year() {
			return r15_fifth_year;
		}

		public void setR15_fifth_year(BigDecimal r15_fifth_year) {
			this.r15_fifth_year = r15_fifth_year;
		}

		public BigDecimal getR15_non_interest_bearing() {
			return r15_non_interest_bearing;
		}

		public void setR15_non_interest_bearing(BigDecimal r15_non_interest_bearing) {
			this.r15_non_interest_bearing = r15_non_interest_bearing;
		}

		public BigDecimal getR15_cumulative_total() {
			return r15_cumulative_total;
		}

		public void setR15_cumulative_total(BigDecimal r15_cumulative_total) {
			this.r15_cumulative_total = r15_cumulative_total;
		}

		// Getters and Setters for R16
		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_first_month() {
			return r16_first_month;
		}

		public void setR16_first_month(BigDecimal r16_first_month) {
			this.r16_first_month = r16_first_month;
		}

		public BigDecimal getR16_third_month() {
			return r16_third_month;
		}

		public void setR16_third_month(BigDecimal r16_third_month) {
			this.r16_third_month = r16_third_month;
		}

		public BigDecimal getR16_last_month() {
			return r16_last_month;
		}

		public void setR16_last_month(BigDecimal r16_last_month) {
			this.r16_last_month = r16_last_month;
		}

		public BigDecimal getR16_first_year() {
			return r16_first_year;
		}

		public void setR16_first_year(BigDecimal r16_first_year) {
			this.r16_first_year = r16_first_year;
		}

		public BigDecimal getR16_fifth_year() {
			return r16_fifth_year;
		}

		public void setR16_fifth_year(BigDecimal r16_fifth_year) {
			this.r16_fifth_year = r16_fifth_year;
		}

		public BigDecimal getR16_non_interest_bearing() {
			return r16_non_interest_bearing;
		}

		public void setR16_non_interest_bearing(BigDecimal r16_non_interest_bearing) {
			this.r16_non_interest_bearing = r16_non_interest_bearing;
		}

		public BigDecimal getR16_cumulative_total() {
			return r16_cumulative_total;
		}

		public void setR16_cumulative_total(BigDecimal r16_cumulative_total) {
			this.r16_cumulative_total = r16_cumulative_total;
		}

		// Getters and Setters for R17
		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_first_month() {
			return r17_first_month;
		}

		public void setR17_first_month(BigDecimal r17_first_month) {
			this.r17_first_month = r17_first_month;
		}

		public BigDecimal getR17_third_month() {
			return r17_third_month;
		}

		public void setR17_third_month(BigDecimal r17_third_month) {
			this.r17_third_month = r17_third_month;
		}

		public BigDecimal getR17_last_month() {
			return r17_last_month;
		}

		public void setR17_last_month(BigDecimal r17_last_month) {
			this.r17_last_month = r17_last_month;
		}

		public BigDecimal getR17_first_year() {
			return r17_first_year;
		}

		public void setR17_first_year(BigDecimal r17_first_year) {
			this.r17_first_year = r17_first_year;
		}

		public BigDecimal getR17_fifth_year() {
			return r17_fifth_year;
		}

		public void setR17_fifth_year(BigDecimal r17_fifth_year) {
			this.r17_fifth_year = r17_fifth_year;
		}

		public BigDecimal getR17_non_interest_bearing() {
			return r17_non_interest_bearing;
		}

		public void setR17_non_interest_bearing(BigDecimal r17_non_interest_bearing) {
			this.r17_non_interest_bearing = r17_non_interest_bearing;
		}

		public BigDecimal getR17_cumulative_total() {
			return r17_cumulative_total;
		}

		public void setR17_cumulative_total(BigDecimal r17_cumulative_total) {
			this.r17_cumulative_total = r17_cumulative_total;
		}

		// Getters and Setters for R18
		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_first_month() {
			return r18_first_month;
		}

		public void setR18_first_month(BigDecimal r18_first_month) {
			this.r18_first_month = r18_first_month;
		}

		public BigDecimal getR18_third_month() {
			return r18_third_month;
		}

		public void setR18_third_month(BigDecimal r18_third_month) {
			this.r18_third_month = r18_third_month;
		}

		public BigDecimal getR18_last_month() {
			return r18_last_month;
		}

		public void setR18_last_month(BigDecimal r18_last_month) {
			this.r18_last_month = r18_last_month;
		}

		public BigDecimal getR18_first_year() {
			return r18_first_year;
		}

		public void setR18_first_year(BigDecimal r18_first_year) {
			this.r18_first_year = r18_first_year;
		}

		public BigDecimal getR18_fifth_year() {
			return r18_fifth_year;
		}

		public void setR18_fifth_year(BigDecimal r18_fifth_year) {
			this.r18_fifth_year = r18_fifth_year;
		}

		public BigDecimal getR18_non_interest_bearing() {
			return r18_non_interest_bearing;
		}

		public void setR18_non_interest_bearing(BigDecimal r18_non_interest_bearing) {
			this.r18_non_interest_bearing = r18_non_interest_bearing;
		}

		public BigDecimal getR18_cumulative_total() {
			return r18_cumulative_total;
		}

		public void setR18_cumulative_total(BigDecimal r18_cumulative_total) {
			this.r18_cumulative_total = r18_cumulative_total;
		}

		// Getters and Setters for R19
		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_first_month() {
			return r19_first_month;
		}

		public void setR19_first_month(BigDecimal r19_first_month) {
			this.r19_first_month = r19_first_month;
		}

		public BigDecimal getR19_third_month() {
			return r19_third_month;
		}

		public void setR19_third_month(BigDecimal r19_third_month) {
			this.r19_third_month = r19_third_month;
		}

		public BigDecimal getR19_last_month() {
			return r19_last_month;
		}

		public void setR19_last_month(BigDecimal r19_last_month) {
			this.r19_last_month = r19_last_month;
		}

		public BigDecimal getR19_first_year() {
			return r19_first_year;
		}

		public void setR19_first_year(BigDecimal r19_first_year) {
			this.r19_first_year = r19_first_year;
		}

		public BigDecimal getR19_fifth_year() {
			return r19_fifth_year;
		}

		public void setR19_fifth_year(BigDecimal r19_fifth_year) {
			this.r19_fifth_year = r19_fifth_year;
		}

		public BigDecimal getR19_non_interest_bearing() {
			return r19_non_interest_bearing;
		}

		public void setR19_non_interest_bearing(BigDecimal r19_non_interest_bearing) {
			this.r19_non_interest_bearing = r19_non_interest_bearing;
		}

		public BigDecimal getR19_cumulative_total() {
			return r19_cumulative_total;
		}

		public void setR19_cumulative_total(BigDecimal r19_cumulative_total) {
			this.r19_cumulative_total = r19_cumulative_total;
		}

		// Getters and Setters for R20
		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_first_month() {
			return r20_first_month;
		}

		public void setR20_first_month(BigDecimal r20_first_month) {
			this.r20_first_month = r20_first_month;
		}

		public BigDecimal getR20_third_month() {
			return r20_third_month;
		}

		public void setR20_third_month(BigDecimal r20_third_month) {
			this.r20_third_month = r20_third_month;
		}

		public BigDecimal getR20_last_month() {
			return r20_last_month;
		}

		public void setR20_last_month(BigDecimal r20_last_month) {
			this.r20_last_month = r20_last_month;
		}

		public BigDecimal getR20_first_year() {
			return r20_first_year;
		}

		public void setR20_first_year(BigDecimal r20_first_year) {
			this.r20_first_year = r20_first_year;
		}

		public BigDecimal getR20_fifth_year() {
			return r20_fifth_year;
		}

		public void setR20_fifth_year(BigDecimal r20_fifth_year) {
			this.r20_fifth_year = r20_fifth_year;
		}

		public BigDecimal getR20_non_interest_bearing() {
			return r20_non_interest_bearing;
		}

		public void setR20_non_interest_bearing(BigDecimal r20_non_interest_bearing) {
			this.r20_non_interest_bearing = r20_non_interest_bearing;
		}

		public BigDecimal getR20_cumulative_total() {
			return r20_cumulative_total;
		}

		public void setR20_cumulative_total(BigDecimal r20_cumulative_total) {
			this.r20_cumulative_total = r20_cumulative_total;
		}

		// Getters and Setters for R21
		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_first_month() {
			return r21_first_month;
		}

		public void setR21_first_month(BigDecimal r21_first_month) {
			this.r21_first_month = r21_first_month;
		}

		public BigDecimal getR21_third_month() {
			return r21_third_month;
		}

		public void setR21_third_month(BigDecimal r21_third_month) {
			this.r21_third_month = r21_third_month;
		}

		public BigDecimal getR21_last_month() {
			return r21_last_month;
		}

		public void setR21_last_month(BigDecimal r21_last_month) {
			this.r21_last_month = r21_last_month;
		}

		public BigDecimal getR21_first_year() {
			return r21_first_year;
		}

		public void setR21_first_year(BigDecimal r21_first_year) {
			this.r21_first_year = r21_first_year;
		}

		public BigDecimal getR21_fifth_year() {
			return r21_fifth_year;
		}

		public void setR21_fifth_year(BigDecimal r21_fifth_year) {
			this.r21_fifth_year = r21_fifth_year;
		}

		public BigDecimal getR21_cumulative_total() {
			return r21_cumulative_total;
		}

		public void setR21_cumulative_total(BigDecimal r21_cumulative_total) {
			this.r21_cumulative_total = r21_cumulative_total;
		}

		// Getters and Setters for R22
		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_first_month() {
			return r22_first_month;
		}

		public void setR22_first_month(BigDecimal r22_first_month) {
			this.r22_first_month = r22_first_month;
		}

		public BigDecimal getR22_third_month() {
			return r22_third_month;
		}

		public void setR22_third_month(BigDecimal r22_third_month) {
			this.r22_third_month = r22_third_month;
		}

		public BigDecimal getR22_last_month() {
			return r22_last_month;
		}

		public void setR22_last_month(BigDecimal r22_last_month) {
			this.r22_last_month = r22_last_month;
		}

		public BigDecimal getR22_first_year() {
			return r22_first_year;
		}

		public void setR22_first_year(BigDecimal r22_first_year) {
			this.r22_first_year = r22_first_year;
		}

		public BigDecimal getR22_fifth_year() {
			return r22_fifth_year;
		}

		public void setR22_fifth_year(BigDecimal r22_fifth_year) {
			this.r22_fifth_year = r22_fifth_year;
		}

		public BigDecimal getR22_non_interest_bearing() {
			return r22_non_interest_bearing;
		}

		public void setR22_non_interest_bearing(BigDecimal r22_non_interest_bearing) {
			this.r22_non_interest_bearing = r22_non_interest_bearing;
		}

		public BigDecimal getR22_cumulative_total() {
			return r22_cumulative_total;
		}

		public void setR22_cumulative_total(BigDecimal r22_cumulative_total) {
			this.r22_cumulative_total = r22_cumulative_total;
		}

		// Getters and Setters for R23
		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_first_month() {
			return r23_first_month;
		}

		public void setR23_first_month(BigDecimal r23_first_month) {
			this.r23_first_month = r23_first_month;
		}

		public BigDecimal getR23_third_month() {
			return r23_third_month;
		}

		public void setR23_third_month(BigDecimal r23_third_month) {
			this.r23_third_month = r23_third_month;
		}

		public BigDecimal getR23_last_month() {
			return r23_last_month;
		}

		public void setR23_last_month(BigDecimal r23_last_month) {
			this.r23_last_month = r23_last_month;
		}

		public BigDecimal getR23_first_year() {
			return r23_first_year;
		}

		public void setR23_first_year(BigDecimal r23_first_year) {
			this.r23_first_year = r23_first_year;
		}

		public BigDecimal getR23_fifth_year() {
			return r23_fifth_year;
		}

		public void setR23_fifth_year(BigDecimal r23_fifth_year) {
			this.r23_fifth_year = r23_fifth_year;
		}

		public BigDecimal getR23_non_interest_bearing() {
			return r23_non_interest_bearing;
		}

		public void setR23_non_interest_bearing(BigDecimal r23_non_interest_bearing) {
			this.r23_non_interest_bearing = r23_non_interest_bearing;
		}

		public BigDecimal getR23_cumulative_total() {
			return r23_cumulative_total;
		}

		public void setR23_cumulative_total(BigDecimal r23_cumulative_total) {
			this.r23_cumulative_total = r23_cumulative_total;
		}

		// Getters and Setters for R24
		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_first_month() {
			return r24_first_month;
		}

		public void setR24_first_month(BigDecimal r24_first_month) {
			this.r24_first_month = r24_first_month;
		}

		public BigDecimal getR24_third_month() {
			return r24_third_month;
		}

		public void setR24_third_month(BigDecimal r24_third_month) {
			this.r24_third_month = r24_third_month;
		}

		public BigDecimal getR24_last_month() {
			return r24_last_month;
		}

		public void setR24_last_month(BigDecimal r24_last_month) {
			this.r24_last_month = r24_last_month;
		}

		public BigDecimal getR24_first_year() {
			return r24_first_year;
		}

		public void setR24_first_year(BigDecimal r24_first_year) {
			this.r24_first_year = r24_first_year;
		}

		public BigDecimal getR24_fifth_year() {
			return r24_fifth_year;
		}

		public void setR24_fifth_year(BigDecimal r24_fifth_year) {
			this.r24_fifth_year = r24_fifth_year;
		}

		public BigDecimal getR24_non_interest_bearing() {
			return r24_non_interest_bearing;
		}

		public void setR24_non_interest_bearing(BigDecimal r24_non_interest_bearing) {
			this.r24_non_interest_bearing = r24_non_interest_bearing;
		}

		public BigDecimal getR24_cumulative_total() {
			return r24_cumulative_total;
		}

		public void setR24_cumulative_total(BigDecimal r24_cumulative_total) {
			this.r24_cumulative_total = r24_cumulative_total;
		}

		// Getters and Setters for R25
		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_first_month() {
			return r25_first_month;
		}

		public void setR25_first_month(BigDecimal r25_first_month) {
			this.r25_first_month = r25_first_month;
		}

		public BigDecimal getR25_third_month() {
			return r25_third_month;
		}

		public void setR25_third_month(BigDecimal r25_third_month) {
			this.r25_third_month = r25_third_month;
		}

		public BigDecimal getR25_last_month() {
			return r25_last_month;
		}

		public void setR25_last_month(BigDecimal r25_last_month) {
			this.r25_last_month = r25_last_month;
		}

		public BigDecimal getR25_first_year() {
			return r25_first_year;
		}

		public void setR25_first_year(BigDecimal r25_first_year) {
			this.r25_first_year = r25_first_year;
		}

		public BigDecimal getR25_fifth_year() {
			return r25_fifth_year;
		}

		public void setR25_fifth_year(BigDecimal r25_fifth_year) {
			this.r25_fifth_year = r25_fifth_year;
		}

		public BigDecimal getR25_non_interest_bearing() {
			return r25_non_interest_bearing;
		}

		public void setR25_non_interest_bearing(BigDecimal r25_non_interest_bearing) {
			this.r25_non_interest_bearing = r25_non_interest_bearing;
		}

		public BigDecimal getR25_cumulative_total() {
			return r25_cumulative_total;
		}

		public void setR25_cumulative_total(BigDecimal r25_cumulative_total) {
			this.r25_cumulative_total = r25_cumulative_total;
		}

		// Getters and Setters for R26
		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_first_month() {
			return r26_first_month;
		}

		public void setR26_first_month(BigDecimal r26_first_month) {
			this.r26_first_month = r26_first_month;
		}

		public BigDecimal getR26_third_month() {
			return r26_third_month;
		}

		public void setR26_third_month(BigDecimal r26_third_month) {
			this.r26_third_month = r26_third_month;
		}

		public BigDecimal getR26_last_month() {
			return r26_last_month;
		}

		public void setR26_last_month(BigDecimal r26_last_month) {
			this.r26_last_month = r26_last_month;
		}

		public BigDecimal getR26_first_year() {
			return r26_first_year;
		}

		public void setR26_first_year(BigDecimal r26_first_year) {
			this.r26_first_year = r26_first_year;
		}

		public BigDecimal getR26_fifth_year() {
			return r26_fifth_year;
		}

		public void setR26_fifth_year(BigDecimal r26_fifth_year) {
			this.r26_fifth_year = r26_fifth_year;
		}

		public BigDecimal getR26_non_interest_bearing() {
			return r26_non_interest_bearing;
		}

		public void setR26_non_interest_bearing(BigDecimal r26_non_interest_bearing) {
			this.r26_non_interest_bearing = r26_non_interest_bearing;
		}

		public BigDecimal getR26_cumulative_total() {
			return r26_cumulative_total;
		}

		public void setR26_cumulative_total(BigDecimal r26_cumulative_total) {
			this.r26_cumulative_total = r26_cumulative_total;
		}

		// Getters and Setters for R27
		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_first_month() {
			return r27_first_month;
		}

		public void setR27_first_month(BigDecimal r27_first_month) {
			this.r27_first_month = r27_first_month;
		}

		public BigDecimal getR27_third_month() {
			return r27_third_month;
		}

		public void setR27_third_month(BigDecimal r27_third_month) {
			this.r27_third_month = r27_third_month;
		}

		public BigDecimal getR27_last_month() {
			return r27_last_month;
		}

		public void setR27_last_month(BigDecimal r27_last_month) {
			this.r27_last_month = r27_last_month;
		}

		public BigDecimal getR27_first_year() {
			return r27_first_year;
		}

		public void setR27_first_year(BigDecimal r27_first_year) {
			this.r27_first_year = r27_first_year;
		}

		public BigDecimal getR27_fifth_year() {
			return r27_fifth_year;
		}

		public void setR27_fifth_year(BigDecimal r27_fifth_year) {
			this.r27_fifth_year = r27_fifth_year;
		}

		public BigDecimal getR27_non_interest_bearing() {
			return r27_non_interest_bearing;
		}

		public void setR27_non_interest_bearing(BigDecimal r27_non_interest_bearing) {
			this.r27_non_interest_bearing = r27_non_interest_bearing;
		}

		public BigDecimal getR27_cumulative_total() {
			return r27_cumulative_total;
		}

		public void setR27_cumulative_total(BigDecimal r27_cumulative_total) {
			this.r27_cumulative_total = r27_cumulative_total;
		}

		// Getters and Setters for R28
		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_first_month() {
			return r28_first_month;
		}

		public void setR28_first_month(BigDecimal r28_first_month) {
			this.r28_first_month = r28_first_month;
		}

		public BigDecimal getR28_third_month() {
			return r28_third_month;
		}

		public void setR28_third_month(BigDecimal r28_third_month) {
			this.r28_third_month = r28_third_month;
		}

		public BigDecimal getR28_last_month() {
			return r28_last_month;
		}

		public void setR28_last_month(BigDecimal r28_last_month) {
			this.r28_last_month = r28_last_month;
		}

		public BigDecimal getR28_first_year() {
			return r28_first_year;
		}

		public void setR28_first_year(BigDecimal r28_first_year) {
			this.r28_first_year = r28_first_year;
		}

		public BigDecimal getR28_fifth_year() {
			return r28_fifth_year;
		}

		public void setR28_fifth_year(BigDecimal r28_fifth_year) {
			this.r28_fifth_year = r28_fifth_year;
		}

		public BigDecimal getR28_non_interest_bearing() {
			return r28_non_interest_bearing;
		}

		public void setR28_non_interest_bearing(BigDecimal r28_non_interest_bearing) {
			this.r28_non_interest_bearing = r28_non_interest_bearing;
		}

		public BigDecimal getR28_cumulative_total() {
			return r28_cumulative_total;
		}

		public void setR28_cumulative_total(BigDecimal r28_cumulative_total) {
			this.r28_cumulative_total = r28_cumulative_total;
		}

		// Getters and Setters for R29
		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_first_month() {
			return r29_first_month;
		}

		public void setR29_first_month(BigDecimal r29_first_month) {
			this.r29_first_month = r29_first_month;
		}

		public BigDecimal getR29_third_month() {
			return r29_third_month;
		}

		public void setR29_third_month(BigDecimal r29_third_month) {
			this.r29_third_month = r29_third_month;
		}

		public BigDecimal getR29_last_month() {
			return r29_last_month;
		}

		public void setR29_last_month(BigDecimal r29_last_month) {
			this.r29_last_month = r29_last_month;
		}

		public BigDecimal getR29_first_year() {
			return r29_first_year;
		}

		public void setR29_first_year(BigDecimal r29_first_year) {
			this.r29_first_year = r29_first_year;
		}

		public BigDecimal getR29_fifth_year() {
			return r29_fifth_year;
		}

		public void setR29_fifth_year(BigDecimal r29_fifth_year) {
			this.r29_fifth_year = r29_fifth_year;
		}

		public BigDecimal getR29_non_interest_bearing() {
			return r29_non_interest_bearing;
		}

		public void setR29_non_interest_bearing(BigDecimal r29_non_interest_bearing) {
			this.r29_non_interest_bearing = r29_non_interest_bearing;
		}

		public BigDecimal getR29_cumulative_total() {
			return r29_cumulative_total;
		}

		public void setR29_cumulative_total(BigDecimal r29_cumulative_total) {
			this.r29_cumulative_total = r29_cumulative_total;
		}

		// Getters and Setters for R30
		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_first_month() {
			return r30_first_month;
		}

		public void setR30_first_month(BigDecimal r30_first_month) {
			this.r30_first_month = r30_first_month;
		}

		public BigDecimal getR30_third_month() {
			return r30_third_month;
		}

		public void setR30_third_month(BigDecimal r30_third_month) {
			this.r30_third_month = r30_third_month;
		}

		public BigDecimal getR30_last_month() {
			return r30_last_month;
		}

		public void setR30_last_month(BigDecimal r30_last_month) {
			this.r30_last_month = r30_last_month;
		}

		public BigDecimal getR30_first_year() {
			return r30_first_year;
		}

		public void setR30_first_year(BigDecimal r30_first_year) {
			this.r30_first_year = r30_first_year;
		}

		public BigDecimal getR30_fifth_year() {
			return r30_fifth_year;
		}

		public void setR30_fifth_year(BigDecimal r30_fifth_year) {
			this.r30_fifth_year = r30_fifth_year;
		}

		public BigDecimal getR30_non_interest_bearing() {
			return r30_non_interest_bearing;
		}

		public void setR30_non_interest_bearing(BigDecimal r30_non_interest_bearing) {
			this.r30_non_interest_bearing = r30_non_interest_bearing;
		}

		public BigDecimal getR30_cumulative_total() {
			return r30_cumulative_total;
		}

		public void setR30_cumulative_total(BigDecimal r30_cumulative_total) {
			this.r30_cumulative_total = r30_cumulative_total;
		}

		// Getters and Setters for R31
		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_first_month() {
			return r31_first_month;
		}

		public void setR31_first_month(BigDecimal r31_first_month) {
			this.r31_first_month = r31_first_month;
		}

		public BigDecimal getR31_third_month() {
			return r31_third_month;
		}

		public void setR31_third_month(BigDecimal r31_third_month) {
			this.r31_third_month = r31_third_month;
		}

		public BigDecimal getR31_last_month() {
			return r31_last_month;
		}

		public void setR31_last_month(BigDecimal r31_last_month) {
			this.r31_last_month = r31_last_month;
		}

		public BigDecimal getR31_first_year() {
			return r31_first_year;
		}

		public void setR31_first_year(BigDecimal r31_first_year) {
			this.r31_first_year = r31_first_year;
		}

		public BigDecimal getR31_fifth_year() {
			return r31_fifth_year;
		}

		public void setR31_fifth_year(BigDecimal r31_fifth_year) {
			this.r31_fifth_year = r31_fifth_year;
		}

		public BigDecimal getR31_non_interest_bearing() {
			return r31_non_interest_bearing;
		}

		public void setR31_non_interest_bearing(BigDecimal r31_non_interest_bearing) {
			this.r31_non_interest_bearing = r31_non_interest_bearing;
		}

		public BigDecimal getR31_cumulative_total() {
			return r31_cumulative_total;
		}

		public void setR31_cumulative_total(BigDecimal r31_cumulative_total) {
			this.r31_cumulative_total = r31_cumulative_total;
		}

		// Getters and Setters for R32
		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_first_month() {
			return r32_first_month;
		}

		public void setR32_first_month(BigDecimal r32_first_month) {
			this.r32_first_month = r32_first_month;
		}

		public BigDecimal getR32_third_month() {
			return r32_third_month;
		}

		public void setR32_third_month(BigDecimal r32_third_month) {
			this.r32_third_month = r32_third_month;
		}

		public BigDecimal getR32_last_month() {
			return r32_last_month;
		}

		public void setR32_last_month(BigDecimal r32_last_month) {
			this.r32_last_month = r32_last_month;
		}

		public BigDecimal getR32_first_year() {
			return r32_first_year;
		}

		public void setR32_first_year(BigDecimal r32_first_year) {
			this.r32_first_year = r32_first_year;
		}

		public BigDecimal getR32_fifth_year() {
			return r32_fifth_year;
		}

		public void setR32_fifth_year(BigDecimal r32_fifth_year) {
			this.r32_fifth_year = r32_fifth_year;
		}

		public BigDecimal getR32_cumulative_total() {
			return r32_cumulative_total;
		}

		public void setR32_cumulative_total(BigDecimal r32_cumulative_total) {
			this.r32_cumulative_total = r32_cumulative_total;
		}

		// Getters and Setters for R33
		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_first_month() {
			return r33_first_month;
		}

		public void setR33_first_month(BigDecimal r33_first_month) {
			this.r33_first_month = r33_first_month;
		}

		public BigDecimal getR33_cumulative_total() {
			return r33_cumulative_total;
		}

		public void setR33_cumulative_total(BigDecimal r33_cumulative_total) {
			this.r33_cumulative_total = r33_cumulative_total;
		}

		// Getters and Setters for R34
		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_third_month() {
			return r34_third_month;
		}

		public void setR34_third_month(BigDecimal r34_third_month) {
			this.r34_third_month = r34_third_month;
		}

		public BigDecimal getR34_last_month() {
			return r34_last_month;
		}

		public void setR34_last_month(BigDecimal r34_last_month) {
			this.r34_last_month = r34_last_month;
		}

		public BigDecimal getR34_first_year() {
			return r34_first_year;
		}

		public void setR34_first_year(BigDecimal r34_first_year) {
			this.r34_first_year = r34_first_year;
		}

		public BigDecimal getR34_fifth_year() {
			return r34_fifth_year;
		}

		public void setR34_fifth_year(BigDecimal r34_fifth_year) {
			this.r34_fifth_year = r34_fifth_year;
		}

		public BigDecimal getR34_non_interest_bearing() {
			return r34_non_interest_bearing;
		}

		public void setR34_non_interest_bearing(BigDecimal r34_non_interest_bearing) {
			this.r34_non_interest_bearing = r34_non_interest_bearing;
		}

		public BigDecimal getR34_cumulative_total() {
			return r34_cumulative_total;
		}

		public void setR34_cumulative_total(BigDecimal r34_cumulative_total) {
			this.r34_cumulative_total = r34_cumulative_total;
		}

		// Getters and Setters for R35
		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_first_month() {
			return r35_first_month;
		}

		public void setR35_first_month(BigDecimal r35_first_month) {
			this.r35_first_month = r35_first_month;
		}

		public BigDecimal getR35_third_month() {
			return r35_third_month;
		}

		public void setR35_third_month(BigDecimal r35_third_month) {
			this.r35_third_month = r35_third_month;
		}

		public BigDecimal getR35_last_month() {
			return r35_last_month;
		}

		public void setR35_last_month(BigDecimal r35_last_month) {
			this.r35_last_month = r35_last_month;
		}

		public BigDecimal getR35_first_year() {
			return r35_first_year;
		}

		public void setR35_first_year(BigDecimal r35_first_year) {
			this.r35_first_year = r35_first_year;
		}

		public BigDecimal getR35_fifth_year() {
			return r35_fifth_year;
		}

		public void setR35_fifth_year(BigDecimal r35_fifth_year) {
			this.r35_fifth_year = r35_fifth_year;
		}

		public BigDecimal getR35_non_interest_bearing() {
			return r35_non_interest_bearing;
		}

		public void setR35_non_interest_bearing(BigDecimal r35_non_interest_bearing) {
			this.r35_non_interest_bearing = r35_non_interest_bearing;
		}

		public BigDecimal getR35_cumulative_total() {
			return r35_cumulative_total;
		}

		public void setR35_cumulative_total(BigDecimal r35_cumulative_total) {
			this.r35_cumulative_total = r35_cumulative_total;
		}

		// Getters and Setters for R36
		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_first_month() {
			return r36_first_month;
		}

		public void setR36_first_month(BigDecimal r36_first_month) {
			this.r36_first_month = r36_first_month;
		}

		public BigDecimal getR36_third_month() {
			return r36_third_month;
		}

		public void setR36_third_month(BigDecimal r36_third_month) {
			this.r36_third_month = r36_third_month;
		}

		public BigDecimal getR36_last_month() {
			return r36_last_month;
		}

		public void setR36_last_month(BigDecimal r36_last_month) {
			this.r36_last_month = r36_last_month;
		}

		public BigDecimal getR36_first_year() {
			return r36_first_year;
		}

		public void setR36_first_year(BigDecimal r36_first_year) {
			this.r36_first_year = r36_first_year;
		}

		public BigDecimal getR36_fifth_year() {
			return r36_fifth_year;
		}

		public void setR36_fifth_year(BigDecimal r36_fifth_year) {
			this.r36_fifth_year = r36_fifth_year;
		}

		public BigDecimal getR36_non_interest_bearing() {
			return r36_non_interest_bearing;
		}

		public void setR36_non_interest_bearing(BigDecimal r36_non_interest_bearing) {
			this.r36_non_interest_bearing = r36_non_interest_bearing;
		}

		public BigDecimal getR36_cumulative_total() {
			return r36_cumulative_total;
		}

		public void setR36_cumulative_total(BigDecimal r36_cumulative_total) {
			this.r36_cumulative_total = r36_cumulative_total;
		}

		// Getters and Setters for R37
		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_first_month() {
			return r37_first_month;
		}

		public void setR37_first_month(BigDecimal r37_first_month) {
			this.r37_first_month = r37_first_month;
		}

		public BigDecimal getR37_third_month() {
			return r37_third_month;
		}

		public void setR37_third_month(BigDecimal r37_third_month) {
			this.r37_third_month = r37_third_month;
		}

		public BigDecimal getR37_last_month() {
			return r37_last_month;
		}

		public void setR37_last_month(BigDecimal r37_last_month) {
			this.r37_last_month = r37_last_month;
		}

		public BigDecimal getR37_first_year() {
			return r37_first_year;
		}

		public void setR37_first_year(BigDecimal r37_first_year) {
			this.r37_first_year = r37_first_year;
		}

		public BigDecimal getR37_fifth_year() {
			return r37_fifth_year;
		}

		public void setR37_fifth_year(BigDecimal r37_fifth_year) {
			this.r37_fifth_year = r37_fifth_year;
		}

		public BigDecimal getR37_non_interest_bearing() {
			return r37_non_interest_bearing;
		}

		public void setR37_non_interest_bearing(BigDecimal r37_non_interest_bearing) {
			this.r37_non_interest_bearing = r37_non_interest_bearing;
		}

		public BigDecimal getR37_cumulative_total() {
			return r37_cumulative_total;
		}

		public void setR37_cumulative_total(BigDecimal r37_cumulative_total) {
			this.r37_cumulative_total = r37_cumulative_total;
		}

		// Getters and Setters for R38
		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_first_month() {
			return r38_first_month;
		}

		public void setR38_first_month(BigDecimal r38_first_month) {
			this.r38_first_month = r38_first_month;
		}

		public BigDecimal getR38_third_month() {
			return r38_third_month;
		}

		public void setR38_third_month(BigDecimal r38_third_month) {
			this.r38_third_month = r38_third_month;
		}

		public BigDecimal getR38_last_month() {
			return r38_last_month;
		}

		public void setR38_last_month(BigDecimal r38_last_month) {
			this.r38_last_month = r38_last_month;
		}

		public BigDecimal getR38_first_year() {
			return r38_first_year;
		}

		public void setR38_first_year(BigDecimal r38_first_year) {
			this.r38_first_year = r38_first_year;
		}

		public BigDecimal getR38_fifth_year() {
			return r38_fifth_year;
		}

		public void setR38_fifth_year(BigDecimal r38_fifth_year) {
			this.r38_fifth_year = r38_fifth_year;
		}

		public BigDecimal getR38_non_interest_bearing() {
			return r38_non_interest_bearing;
		}

		public void setR38_non_interest_bearing(BigDecimal r38_non_interest_bearing) {
			this.r38_non_interest_bearing = r38_non_interest_bearing;
		}

		public BigDecimal getR38_cumulative_total() {
			return r38_cumulative_total;
		}

		public void setR38_cumulative_total(BigDecimal r38_cumulative_total) {
			this.r38_cumulative_total = r38_cumulative_total;
		}

		// Getters and Setters for R39
		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_first_month() {
			return r39_first_month;
		}

		public void setR39_first_month(BigDecimal r39_first_month) {
			this.r39_first_month = r39_first_month;
		}

		public BigDecimal getR39_third_month() {
			return r39_third_month;
		}

		public void setR39_third_month(BigDecimal r39_third_month) {
			this.r39_third_month = r39_third_month;
		}

		public BigDecimal getR39_last_month() {
			return r39_last_month;
		}

		public void setR39_last_month(BigDecimal r39_last_month) {
			this.r39_last_month = r39_last_month;
		}

		public BigDecimal getR39_first_year() {
			return r39_first_year;
		}

		public void setR39_first_year(BigDecimal r39_first_year) {
			this.r39_first_year = r39_first_year;
		}

		public BigDecimal getR39_fifth_year() {
			return r39_fifth_year;
		}

		public void setR39_fifth_year(BigDecimal r39_fifth_year) {
			this.r39_fifth_year = r39_fifth_year;
		}

		public BigDecimal getR39_non_interest_bearing() {
			return r39_non_interest_bearing;
		}

		public void setR39_non_interest_bearing(BigDecimal r39_non_interest_bearing) {
			this.r39_non_interest_bearing = r39_non_interest_bearing;
		}

		public BigDecimal getR39_cumulative_total() {
			return r39_cumulative_total;
		}

		public void setR39_cumulative_total(BigDecimal r39_cumulative_total) {
			this.r39_cumulative_total = r39_cumulative_total;
		}

		// Getters and Setters for R40
		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_first_month() {
			return r40_first_month;
		}

		public void setR40_first_month(BigDecimal r40_first_month) {
			this.r40_first_month = r40_first_month;
		}

		public BigDecimal getR40_third_month() {
			return r40_third_month;
		}

		public void setR40_third_month(BigDecimal r40_third_month) {
			this.r40_third_month = r40_third_month;
		}

		public BigDecimal getR40_last_month() {
			return r40_last_month;
		}

		public void setR40_last_month(BigDecimal r40_last_month) {
			this.r40_last_month = r40_last_month;
		}

		public BigDecimal getR40_first_year() {
			return r40_first_year;
		}

		public void setR40_first_year(BigDecimal r40_first_year) {
			this.r40_first_year = r40_first_year;
		}

		public BigDecimal getR40_fifth_year() {
			return r40_fifth_year;
		}

		public void setR40_fifth_year(BigDecimal r40_fifth_year) {
			this.r40_fifth_year = r40_fifth_year;
		}

		public BigDecimal getR40_non_interest_bearing() {
			return r40_non_interest_bearing;
		}

		public void setR40_non_interest_bearing(BigDecimal r40_non_interest_bearing) {
			this.r40_non_interest_bearing = r40_non_interest_bearing;
		}

		public BigDecimal getR40_cumulative_total() {
			return r40_cumulative_total;
		}

		public void setR40_cumulative_total(BigDecimal r40_cumulative_total) {
			this.r40_cumulative_total = r40_cumulative_total;
		}

		// Getters and Setters for R41
		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_first_month() {
			return r41_first_month;
		}

		public void setR41_first_month(BigDecimal r41_first_month) {
			this.r41_first_month = r41_first_month;
		}

		public BigDecimal getR41_third_month() {
			return r41_third_month;
		}

		public void setR41_third_month(BigDecimal r41_third_month) {
			this.r41_third_month = r41_third_month;
		}

		public BigDecimal getR41_last_month() {
			return r41_last_month;
		}

		public void setR41_last_month(BigDecimal r41_last_month) {
			this.r41_last_month = r41_last_month;
		}

		public BigDecimal getR41_first_year() {
			return r41_first_year;
		}

		public void setR41_first_year(BigDecimal r41_first_year) {
			this.r41_first_year = r41_first_year;
		}

		public BigDecimal getR41_fifth_year() {
			return r41_fifth_year;
		}

		public void setR41_fifth_year(BigDecimal r41_fifth_year) {
			this.r41_fifth_year = r41_fifth_year;
		}

		public BigDecimal getR41_non_interest_bearing() {
			return r41_non_interest_bearing;
		}

		public void setR41_non_interest_bearing(BigDecimal r41_non_interest_bearing) {
			this.r41_non_interest_bearing = r41_non_interest_bearing;
		}

		public BigDecimal getR41_cumulative_total() {
			return r41_cumulative_total;
		}

		public void setR41_cumulative_total(BigDecimal r41_cumulative_total) {
			this.r41_cumulative_total = r41_cumulative_total;
		}

		// Getters and Setters for R42
		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_first_month() {
			return r42_first_month;
		}

		public void setR42_first_month(BigDecimal r42_first_month) {
			this.r42_first_month = r42_first_month;
		}

		public BigDecimal getR42_third_month() {
			return r42_third_month;
		}

		public void setR42_third_month(BigDecimal r42_third_month) {
			this.r42_third_month = r42_third_month;
		}

		public BigDecimal getR42_last_month() {
			return r42_last_month;
		}

		public void setR42_last_month(BigDecimal r42_last_month) {
			this.r42_last_month = r42_last_month;
		}

		public BigDecimal getR42_first_year() {
			return r42_first_year;
		}

		public void setR42_first_year(BigDecimal r42_first_year) {
			this.r42_first_year = r42_first_year;
		}

		public BigDecimal getR42_fifth_year() {
			return r42_fifth_year;
		}

		public void setR42_fifth_year(BigDecimal r42_fifth_year) {
			this.r42_fifth_year = r42_fifth_year;
		}

		public BigDecimal getR42_non_interest_bearing() {
			return r42_non_interest_bearing;
		}

		public void setR42_non_interest_bearing(BigDecimal r42_non_interest_bearing) {
			this.r42_non_interest_bearing = r42_non_interest_bearing;
		}

		public BigDecimal getR42_cumulative_total() {
			return r42_cumulative_total;
		}

		public void setR42_cumulative_total(BigDecimal r42_cumulative_total) {
			this.r42_cumulative_total = r42_cumulative_total;
		}

		// Getters and Setters for R43
		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_first_month() {
			return r43_first_month;
		}

		public void setR43_first_month(BigDecimal r43_first_month) {
			this.r43_first_month = r43_first_month;
		}

		public BigDecimal getR43_third_month() {
			return r43_third_month;
		}

		public void setR43_third_month(BigDecimal r43_third_month) {
			this.r43_third_month = r43_third_month;
		}

		public BigDecimal getR43_last_month() {
			return r43_last_month;
		}

		public void setR43_last_month(BigDecimal r43_last_month) {
			this.r43_last_month = r43_last_month;
		}

		public BigDecimal getR43_first_year() {
			return r43_first_year;
		}

		public void setR43_first_year(BigDecimal r43_first_year) {
			this.r43_first_year = r43_first_year;
		}

		public BigDecimal getR43_fifth_year() {
			return r43_fifth_year;
		}

		public void setR43_fifth_year(BigDecimal r43_fifth_year) {
			this.r43_fifth_year = r43_fifth_year;
		}

		public BigDecimal getR43_non_interest_bearing() {
			return r43_non_interest_bearing;
		}

		public void setR43_non_interest_bearing(BigDecimal r43_non_interest_bearing) {
			this.r43_non_interest_bearing = r43_non_interest_bearing;
		}

		public BigDecimal getR43_cumulative_total() {
			return r43_cumulative_total;
		}

		public void setR43_cumulative_total(BigDecimal r43_cumulative_total) {
			this.r43_cumulative_total = r43_cumulative_total;
		}

		// Getters and Setters for R44
		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_first_month() {
			return r44_first_month;
		}

		public void setR44_first_month(BigDecimal r44_first_month) {
			this.r44_first_month = r44_first_month;
		}

		public BigDecimal getR44_third_month() {
			return r44_third_month;
		}

		public void setR44_third_month(BigDecimal r44_third_month) {
			this.r44_third_month = r44_third_month;
		}

		public BigDecimal getR44_last_month() {
			return r44_last_month;
		}

		public void setR44_last_month(BigDecimal r44_last_month) {
			this.r44_last_month = r44_last_month;
		}

		public BigDecimal getR44_first_year() {
			return r44_first_year;
		}

		public void setR44_first_year(BigDecimal r44_first_year) {
			this.r44_first_year = r44_first_year;
		}

		public BigDecimal getR44_fifth_year() {
			return r44_fifth_year;
		}

		public void setR44_fifth_year(BigDecimal r44_fifth_year) {
			this.r44_fifth_year = r44_fifth_year;
		}

		public BigDecimal getR44_non_interest_bearing() {
			return r44_non_interest_bearing;
		}

		public void setR44_non_interest_bearing(BigDecimal r44_non_interest_bearing) {
			this.r44_non_interest_bearing = r44_non_interest_bearing;
		}

		public BigDecimal getR44_cumulative_total() {
			return r44_cumulative_total;
		}

		public void setR44_cumulative_total(BigDecimal r44_cumulative_total) {
			this.r44_cumulative_total = r44_cumulative_total;
		}

		// Getters and Setters for R45
		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_first_month() {
			return r45_first_month;
		}

		public void setR45_first_month(BigDecimal r45_first_month) {
			this.r45_first_month = r45_first_month;
		}

		public BigDecimal getR45_third_month() {
			return r45_third_month;
		}

		public void setR45_third_month(BigDecimal r45_third_month) {
			this.r45_third_month = r45_third_month;
		}

		public BigDecimal getR45_last_month() {
			return r45_last_month;
		}

		public void setR45_last_month(BigDecimal r45_last_month) {
			this.r45_last_month = r45_last_month;
		}

		public BigDecimal getR45_first_year() {
			return r45_first_year;
		}

		public void setR45_first_year(BigDecimal r45_first_year) {
			this.r45_first_year = r45_first_year;
		}

		public BigDecimal getR45_fifth_year() {
			return r45_fifth_year;
		}

		public void setR45_fifth_year(BigDecimal r45_fifth_year) {
			this.r45_fifth_year = r45_fifth_year;
		}

		public BigDecimal getR45_non_interest_bearing() {
			return r45_non_interest_bearing;
		}

		public void setR45_non_interest_bearing(BigDecimal r45_non_interest_bearing) {
			this.r45_non_interest_bearing = r45_non_interest_bearing;
		}

		public BigDecimal getR45_cumulative_total() {
			return r45_cumulative_total;
		}

		public void setR45_cumulative_total(BigDecimal r45_cumulative_total) {
			this.r45_cumulative_total = r45_cumulative_total;
		}

		// Getters and Setters for R46
		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_first_month() {
			return r46_first_month;
		}

		public void setR46_first_month(BigDecimal r46_first_month) {
			this.r46_first_month = r46_first_month;
		}

		public BigDecimal getR46_third_month() {
			return r46_third_month;
		}

		public void setR46_third_month(BigDecimal r46_third_month) {
			this.r46_third_month = r46_third_month;
		}

		public BigDecimal getR46_last_month() {
			return r46_last_month;
		}

		public void setR46_last_month(BigDecimal r46_last_month) {
			this.r46_last_month = r46_last_month;
		}

		public BigDecimal getR46_first_year() {
			return r46_first_year;
		}

		public void setR46_first_year(BigDecimal r46_first_year) {
			this.r46_first_year = r46_first_year;
		}

		public BigDecimal getR46_fifth_year() {
			return r46_fifth_year;
		}

		public void setR46_fifth_year(BigDecimal r46_fifth_year) {
			this.r46_fifth_year = r46_fifth_year;
		}

		public BigDecimal getR46_non_interest_bearing() {
			return r46_non_interest_bearing;
		}

		public void setR46_non_interest_bearing(BigDecimal r46_non_interest_bearing) {
			this.r46_non_interest_bearing = r46_non_interest_bearing;
		}

		public BigDecimal getR46_cumulative_total() {
			return r46_cumulative_total;
		}

		public void setR46_cumulative_total(BigDecimal r46_cumulative_total) {
			this.r46_cumulative_total = r46_cumulative_total;
		}

		// Getters and Setters for R47
		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_first_month() {
			return r47_first_month;
		}

		public void setR47_first_month(BigDecimal r47_first_month) {
			this.r47_first_month = r47_first_month;
		}

		public BigDecimal getR47_third_month() {
			return r47_third_month;
		}

		public void setR47_third_month(BigDecimal r47_third_month) {
			this.r47_third_month = r47_third_month;
		}

		public BigDecimal getR47_last_month() {
			return r47_last_month;
		}

		public void setR47_last_month(BigDecimal r47_last_month) {
			this.r47_last_month = r47_last_month;
		}

		public BigDecimal getR47_first_year() {
			return r47_first_year;
		}

		public void setR47_first_year(BigDecimal r47_first_year) {
			this.r47_first_year = r47_first_year;
		}

		public BigDecimal getR47_fifth_year() {
			return r47_fifth_year;
		}

		public void setR47_fifth_year(BigDecimal r47_fifth_year) {
			this.r47_fifth_year = r47_fifth_year;
		}

		public BigDecimal getR47_non_interest_bearing() {
			return r47_non_interest_bearing;
		}

		public void setR47_non_interest_bearing(BigDecimal r47_non_interest_bearing) {
			this.r47_non_interest_bearing = r47_non_interest_bearing;
		}

		public BigDecimal getR47_cumulative_total() {
			return r47_cumulative_total;
		}

		public void setR47_cumulative_total(BigDecimal r47_cumulative_total) {
			this.r47_cumulative_total = r47_cumulative_total;
		}

		// Getters and Setters for R48
		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_first_month() {
			return r48_first_month;
		}

		public void setR48_first_month(BigDecimal r48_first_month) {
			this.r48_first_month = r48_first_month;
		}

		public BigDecimal getR48_third_month() {
			return r48_third_month;
		}

		public void setR48_third_month(BigDecimal r48_third_month) {
			this.r48_third_month = r48_third_month;
		}

		public BigDecimal getR48_last_month() {
			return r48_last_month;
		}

		public void setR48_last_month(BigDecimal r48_last_month) {
			this.r48_last_month = r48_last_month;
		}

		public BigDecimal getR48_first_year() {
			return r48_first_year;
		}

		public void setR48_first_year(BigDecimal r48_first_year) {
			this.r48_first_year = r48_first_year;
		}

		public BigDecimal getR48_fifth_year() {
			return r48_fifth_year;
		}

		public void setR48_fifth_year(BigDecimal r48_fifth_year) {
			this.r48_fifth_year = r48_fifth_year;
		}

		public BigDecimal getR48_non_interest_bearing() {
			return r48_non_interest_bearing;
		}

		public void setR48_non_interest_bearing(BigDecimal r48_non_interest_bearing) {
			this.r48_non_interest_bearing = r48_non_interest_bearing;
		}

		public BigDecimal getR48_cumulative_total() {
			return r48_cumulative_total;
		}

		public void setR48_cumulative_total(BigDecimal r48_cumulative_total) {
			this.r48_cumulative_total = r48_cumulative_total;
		}

		// Getters and Setters for R49
		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_first_month() {
			return r49_first_month;
		}

		public void setR49_first_month(BigDecimal r49_first_month) {
			this.r49_first_month = r49_first_month;
		}

		public BigDecimal getR49_third_month() {
			return r49_third_month;
		}

		public void setR49_third_month(BigDecimal r49_third_month) {
			this.r49_third_month = r49_third_month;
		}

		public BigDecimal getR49_last_month() {
			return r49_last_month;
		}

		public void setR49_last_month(BigDecimal r49_last_month) {
			this.r49_last_month = r49_last_month;
		}

		public BigDecimal getR49_first_year() {
			return r49_first_year;
		}

		public void setR49_first_year(BigDecimal r49_first_year) {
			this.r49_first_year = r49_first_year;
		}

		public BigDecimal getR49_fifth_year() {
			return r49_fifth_year;
		}

		public void setR49_fifth_year(BigDecimal r49_fifth_year) {
			this.r49_fifth_year = r49_fifth_year;
		}

		public BigDecimal getR49_non_interest_bearing() {
			return r49_non_interest_bearing;
		}

		public void setR49_non_interest_bearing(BigDecimal r49_non_interest_bearing) {
			this.r49_non_interest_bearing = r49_non_interest_bearing;
		}

		public BigDecimal getR49_cumulative_total() {
			return r49_cumulative_total;
		}

		public void setR49_cumulative_total(BigDecimal r49_cumulative_total) {
			this.r49_cumulative_total = r49_cumulative_total;
		}

		// Getters and Setters for R50
		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_first_month() {
			return r50_first_month;
		}

		public void setR50_first_month(BigDecimal r50_first_month) {
			this.r50_first_month = r50_first_month;
		}

		public BigDecimal getR50_third_month() {
			return r50_third_month;
		}

		public void setR50_third_month(BigDecimal r50_third_month) {
			this.r50_third_month = r50_third_month;
		}

		public BigDecimal getR50_last_month() {
			return r50_last_month;
		}

		public void setR50_last_month(BigDecimal r50_last_month) {
			this.r50_last_month = r50_last_month;
		}

		public BigDecimal getR50_first_year() {
			return r50_first_year;
		}

		public void setR50_first_year(BigDecimal r50_first_year) {
			this.r50_first_year = r50_first_year;
		}

		public BigDecimal getR50_fifth_year() {
			return r50_fifth_year;
		}

		public void setR50_fifth_year(BigDecimal r50_fifth_year) {
			this.r50_fifth_year = r50_fifth_year;
		}

		public BigDecimal getR50_non_interest_bearing() {
			return r50_non_interest_bearing;
		}

		public void setR50_non_interest_bearing(BigDecimal r50_non_interest_bearing) {
			this.r50_non_interest_bearing = r50_non_interest_bearing;
		}

		public BigDecimal getR50_cumulative_total() {
			return r50_cumulative_total;
		}

		public void setR50_cumulative_total(BigDecimal r50_cumulative_total) {
			this.r50_cumulative_total = r50_cumulative_total;
		}

		// Getters and Setters for Common Fields
		public Date getReportDate() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public BigDecimal getReport_version() {
			return report_version;
		}

		public void setReport_version(BigDecimal report_version) {
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
	}

	// ===========================
	// M_LIQGAP_Detail_RowMapper
	// ===========================

	public class M_LIQGAP_Detail_RowMapper implements RowMapper<M_LIQGAP_Detail_Entity> {

		@Override
		public M_LIQGAP_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Detail_Entity obj = new M_LIQGAP_Detail_Entity();

			// =========================
			// ENTITY FIELDS
			// =========================
			obj.setSno(rs.getLong("sno"));
			obj.setCustId(rs.getString("cust_id"));
			obj.setAcctNumber(rs.getString("acct_number"));
			obj.setAcctName(rs.getString("acct_name"));
			obj.setDataType(rs.getString("data_type"));
			obj.setReportAddlCriteria1(rs.getString("report_addl_criteria_1"));
			obj.setReportLabel(rs.getString("report_label"));
			obj.setReportRemarks(rs.getString("report_remarks"));
			obj.setModificationRemarks(rs.getString("modification_remarks"));
			obj.setDataEntryVersion(rs.getString("data_entry_version"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("acct_balance_in_pula"));
			obj.setReportName(rs.getString("report_name"));
			obj.setCreateUser(rs.getString("create_user"));
			obj.setCreateTime(rs.getDate("create_time"));
			obj.setModifyUser(rs.getString("modify_user"));
			obj.setModifyTime(rs.getDate("modify_time"));
			obj.setVerifyUser(rs.getString("verify_user"));
			obj.setVerifyTime(rs.getDate("verify_time"));
			obj.setReportDate(rs.getDate("report_date"));
			obj.setEntityFlg(rs.getString("entity_flg"));
			obj.setModifyFlg(rs.getString("modify_flg"));
			obj.setDelFlg(rs.getString("del_flg"));

			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Detail_Entity
	// ===========================

	public class M_LIQGAP_Detail_Entity {

		@Id
		private Long sno;
		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportAddlCriteria1;
		private String reportLabel;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;
		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;
		private String entityFlg;
		private String modifyFlg;
		private String delFlg;

		// Default constructor
		public M_LIQGAP_Detail_Entity() {
			super();
		}

		// Getters and Setters
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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}
	}

	// ===========================
	// M_LIQGAP_Archival_Summary_RowMapper
	// ===========================

	public class M_LIQGAP_Archival_Summary_RowMapper implements RowMapper<M_LIQGAP_Archival_Summary_Entity> {

		@Override
		public M_LIQGAP_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Archival_Summary_Entity obj = new M_LIQGAP_Archival_Summary_Entity();

			// =========================
			// R10 FIELDS
			// =========================
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_first_month(rs.getBigDecimal("R10_FIRST_MONTH"));
			obj.setR10_third_month(rs.getBigDecimal("R10_THIRD_MONTH"));
			obj.setR10_last_month(rs.getBigDecimal("R10_LAST_MONTH"));
			obj.setR10_first_year(rs.getBigDecimal("R10_FIRST_YEAR"));
			obj.setR10_fifth_year(rs.getBigDecimal("R10_FIFTH_YEAR"));
			obj.setR10_non_interest_bearing(rs.getBigDecimal("R10_NON_INTEREST_BEARING"));
			obj.setR10_cumulative_total(rs.getBigDecimal("R10_CUMULATIVE_TOTAL"));

			// =========================
			// R11 FIELDS
			// =========================
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_first_month(rs.getBigDecimal("R11_FIRST_MONTH"));
			obj.setR11_third_month(rs.getBigDecimal("R11_THIRD_MONTH"));
			obj.setR11_last_month(rs.getBigDecimal("R11_LAST_MONTH"));
			obj.setR11_first_year(rs.getBigDecimal("R11_FIRST_YEAR"));
			obj.setR11_fifth_year(rs.getBigDecimal("R11_FIFTH_YEAR"));
			obj.setR11_non_interest_bearing(rs.getBigDecimal("R11_NON_INTEREST_BEARING"));
			obj.setR11_cumulative_total(rs.getBigDecimal("R11_CUMULATIVE_TOTAL"));

			// =========================
			// R12 FIELDS
			// =========================
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_first_month(rs.getBigDecimal("R12_FIRST_MONTH"));
			obj.setR12_third_month(rs.getBigDecimal("R12_THIRD_MONTH"));
			obj.setR12_last_month(rs.getBigDecimal("R12_LAST_MONTH"));
			obj.setR12_first_year(rs.getBigDecimal("R12_FIRST_YEAR"));
			obj.setR12_fifth_year(rs.getBigDecimal("R12_FIFTH_YEAR"));
			obj.setR12_non_interest_bearing(rs.getBigDecimal("R12_NON_INTEREST_BEARING"));
			obj.setR12_cumulative_total(rs.getBigDecimal("R12_CUMULATIVE_TOTAL"));

			// =========================
			// R13 FIELDS
			// =========================
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_first_month(rs.getBigDecimal("R13_FIRST_MONTH"));
			obj.setR13_third_month(rs.getBigDecimal("R13_THIRD_MONTH"));
			obj.setR13_last_month(rs.getBigDecimal("R13_LAST_MONTH"));
			obj.setR13_first_year(rs.getBigDecimal("R13_FIRST_YEAR"));
			obj.setR13_fifth_year(rs.getBigDecimal("R13_FIFTH_YEAR"));
			obj.setR13_non_interest_bearing(rs.getBigDecimal("R13_NON_INTEREST_BEARING"));
			obj.setR13_cumulative_total(rs.getBigDecimal("R13_CUMULATIVE_TOTAL"));

			// =========================
			// R14 FIELDS
			// =========================
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_first_month(rs.getBigDecimal("R14_FIRST_MONTH"));
			obj.setR14_third_month(rs.getBigDecimal("R14_THIRD_MONTH"));
			obj.setR14_last_month(rs.getBigDecimal("R14_LAST_MONTH"));
			obj.setR14_first_year(rs.getBigDecimal("R14_FIRST_YEAR"));
			obj.setR14_fifth_year(rs.getBigDecimal("R14_FIFTH_YEAR"));
			obj.setR14_non_interest_bearing(rs.getBigDecimal("R14_NON_INTEREST_BEARING"));
			obj.setR14_cumulative_total(rs.getBigDecimal("R14_CUMULATIVE_TOTAL"));

			// =========================
			// R15 FIELDS
			// =========================
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_first_month(rs.getBigDecimal("R15_FIRST_MONTH"));
			obj.setR15_third_month(rs.getBigDecimal("R15_THIRD_MONTH"));
			obj.setR15_last_month(rs.getBigDecimal("R15_LAST_MONTH"));
			obj.setR15_first_year(rs.getBigDecimal("R15_FIRST_YEAR"));
			obj.setR15_fifth_year(rs.getBigDecimal("R15_FIFTH_YEAR"));
			obj.setR15_non_interest_bearing(rs.getBigDecimal("R15_NON_INTEREST_BEARING"));
			obj.setR15_cumulative_total(rs.getBigDecimal("R15_CUMULATIVE_TOTAL"));

			// =========================
			// R16 FIELDS
			// =========================
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_first_month(rs.getBigDecimal("R16_FIRST_MONTH"));
			obj.setR16_third_month(rs.getBigDecimal("R16_THIRD_MONTH"));
			obj.setR16_last_month(rs.getBigDecimal("R16_LAST_MONTH"));
			obj.setR16_first_year(rs.getBigDecimal("R16_FIRST_YEAR"));
			obj.setR16_fifth_year(rs.getBigDecimal("R16_FIFTH_YEAR"));
			obj.setR16_non_interest_bearing(rs.getBigDecimal("R16_NON_INTEREST_BEARING"));
			obj.setR16_cumulative_total(rs.getBigDecimal("R16_CUMULATIVE_TOTAL"));

			// =========================
			// R17 FIELDS
			// =========================
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_first_month(rs.getBigDecimal("R17_FIRST_MONTH"));
			obj.setR17_third_month(rs.getBigDecimal("R17_THIRD_MONTH"));
			obj.setR17_last_month(rs.getBigDecimal("R17_LAST_MONTH"));
			obj.setR17_first_year(rs.getBigDecimal("R17_FIRST_YEAR"));
			obj.setR17_fifth_year(rs.getBigDecimal("R17_FIFTH_YEAR"));
			obj.setR17_non_interest_bearing(rs.getBigDecimal("R17_NON_INTEREST_BEARING"));
			obj.setR17_cumulative_total(rs.getBigDecimal("R17_CUMULATIVE_TOTAL"));

			// =========================
			// R18 FIELDS
			// =========================
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_first_month(rs.getBigDecimal("R18_FIRST_MONTH"));
			obj.setR18_third_month(rs.getBigDecimal("R18_THIRD_MONTH"));
			obj.setR18_last_month(rs.getBigDecimal("R18_LAST_MONTH"));
			obj.setR18_first_year(rs.getBigDecimal("R18_FIRST_YEAR"));
			obj.setR18_fifth_year(rs.getBigDecimal("R18_FIFTH_YEAR"));
			obj.setR18_non_interest_bearing(rs.getBigDecimal("R18_NON_INTEREST_BEARING"));
			obj.setR18_cumulative_total(rs.getBigDecimal("R18_CUMULATIVE_TOTAL"));

			// =========================
			// R19 FIELDS
			// =========================
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_first_month(rs.getBigDecimal("R19_FIRST_MONTH"));
			obj.setR19_third_month(rs.getBigDecimal("R19_THIRD_MONTH"));
			obj.setR19_last_month(rs.getBigDecimal("R19_LAST_MONTH"));
			obj.setR19_first_year(rs.getBigDecimal("R19_FIRST_YEAR"));
			obj.setR19_fifth_year(rs.getBigDecimal("R19_FIFTH_YEAR"));
			obj.setR19_non_interest_bearing(rs.getBigDecimal("R19_NON_INTEREST_BEARING"));
			obj.setR19_cumulative_total(rs.getBigDecimal("R19_CUMULATIVE_TOTAL"));

			// =========================
			// R20 FIELDS
			// =========================
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_first_month(rs.getBigDecimal("R20_FIRST_MONTH"));
			obj.setR20_third_month(rs.getBigDecimal("R20_THIRD_MONTH"));
			obj.setR20_last_month(rs.getBigDecimal("R20_LAST_MONTH"));
			obj.setR20_first_year(rs.getBigDecimal("R20_FIRST_YEAR"));
			obj.setR20_fifth_year(rs.getBigDecimal("R20_FIFTH_YEAR"));
			obj.setR20_non_interest_bearing(rs.getBigDecimal("R20_NON_INTEREST_BEARING"));
			obj.setR20_cumulative_total(rs.getBigDecimal("R20_CUMULATIVE_TOTAL"));

			// =========================
			// R21 FIELDS
			// =========================
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_first_month(rs.getBigDecimal("R21_FIRST_MONTH"));
			obj.setR21_third_month(rs.getBigDecimal("R21_THIRD_MONTH"));
			obj.setR21_last_month(rs.getBigDecimal("R21_LAST_MONTH"));
			obj.setR21_first_year(rs.getBigDecimal("R21_FIRST_YEAR"));
			obj.setR21_fifth_year(rs.getBigDecimal("R21_FIFTH_YEAR"));
			obj.setR21_cumulative_total(rs.getBigDecimal("R21_CUMULATIVE_TOTAL"));

			// =========================
			// R22 FIELDS
			// =========================
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_first_month(rs.getBigDecimal("R22_FIRST_MONTH"));
			obj.setR22_third_month(rs.getBigDecimal("R22_THIRD_MONTH"));
			obj.setR22_last_month(rs.getBigDecimal("R22_LAST_MONTH"));
			obj.setR22_first_year(rs.getBigDecimal("R22_FIRST_YEAR"));
			obj.setR22_fifth_year(rs.getBigDecimal("R22_FIFTH_YEAR"));
			obj.setR22_non_interest_bearing(rs.getBigDecimal("R22_NON_INTEREST_BEARING"));
			obj.setR22_cumulative_total(rs.getBigDecimal("R22_CUMULATIVE_TOTAL"));

			// =========================
			// R23 FIELDS
			// =========================
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_first_month(rs.getBigDecimal("R23_FIRST_MONTH"));
			obj.setR23_third_month(rs.getBigDecimal("R23_THIRD_MONTH"));
			obj.setR23_last_month(rs.getBigDecimal("R23_LAST_MONTH"));
			obj.setR23_first_year(rs.getBigDecimal("R23_FIRST_YEAR"));
			obj.setR23_fifth_year(rs.getBigDecimal("R23_FIFTH_YEAR"));
			obj.setR23_non_interest_bearing(rs.getBigDecimal("R23_NON_INTEREST_BEARING"));
			obj.setR23_cumulative_total(rs.getBigDecimal("R23_CUMULATIVE_TOTAL"));

			// =========================
			// R24 FIELDS
			// =========================
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_first_month(rs.getBigDecimal("R24_FIRST_MONTH"));
			obj.setR24_third_month(rs.getBigDecimal("R24_THIRD_MONTH"));
			obj.setR24_last_month(rs.getBigDecimal("R24_LAST_MONTH"));
			obj.setR24_first_year(rs.getBigDecimal("R24_FIRST_YEAR"));
			obj.setR24_fifth_year(rs.getBigDecimal("R24_FIFTH_YEAR"));
			obj.setR24_non_interest_bearing(rs.getBigDecimal("R24_NON_INTEREST_BEARING"));
			obj.setR24_cumulative_total(rs.getBigDecimal("R24_CUMULATIVE_TOTAL"));

			// =========================
			// R25 FIELDS
			// =========================
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_first_month(rs.getBigDecimal("R25_FIRST_MONTH"));
			obj.setR25_third_month(rs.getBigDecimal("R25_THIRD_MONTH"));
			obj.setR25_last_month(rs.getBigDecimal("R25_LAST_MONTH"));
			obj.setR25_first_year(rs.getBigDecimal("R25_FIRST_YEAR"));
			obj.setR25_fifth_year(rs.getBigDecimal("R25_FIFTH_YEAR"));
			obj.setR25_non_interest_bearing(rs.getBigDecimal("R25_NON_INTEREST_BEARING"));
			obj.setR25_cumulative_total(rs.getBigDecimal("R25_CUMULATIVE_TOTAL"));

			// =========================
			// R26 FIELDS
			// =========================
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_first_month(rs.getBigDecimal("R26_FIRST_MONTH"));
			obj.setR26_third_month(rs.getBigDecimal("R26_THIRD_MONTH"));
			obj.setR26_last_month(rs.getBigDecimal("R26_LAST_MONTH"));
			obj.setR26_first_year(rs.getBigDecimal("R26_FIRST_YEAR"));
			obj.setR26_fifth_year(rs.getBigDecimal("R26_FIFTH_YEAR"));
			obj.setR26_non_interest_bearing(rs.getBigDecimal("R26_NON_INTEREST_BEARING"));
			obj.setR26_cumulative_total(rs.getBigDecimal("R26_CUMULATIVE_TOTAL"));

			// =========================
			// R27 FIELDS
			// =========================
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_first_month(rs.getBigDecimal("R27_FIRST_MONTH"));
			obj.setR27_third_month(rs.getBigDecimal("R27_THIRD_MONTH"));
			obj.setR27_last_month(rs.getBigDecimal("R27_LAST_MONTH"));
			obj.setR27_first_year(rs.getBigDecimal("R27_FIRST_YEAR"));
			obj.setR27_fifth_year(rs.getBigDecimal("R27_FIFTH_YEAR"));
			obj.setR27_non_interest_bearing(rs.getBigDecimal("R27_NON_INTEREST_BEARING"));
			obj.setR27_cumulative_total(rs.getBigDecimal("R27_CUMULATIVE_TOTAL"));

			// =========================
			// R28 FIELDS
			// =========================
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_first_month(rs.getBigDecimal("R28_FIRST_MONTH"));
			obj.setR28_third_month(rs.getBigDecimal("R28_THIRD_MONTH"));
			obj.setR28_last_month(rs.getBigDecimal("R28_LAST_MONTH"));
			obj.setR28_first_year(rs.getBigDecimal("R28_FIRST_YEAR"));
			obj.setR28_fifth_year(rs.getBigDecimal("R28_FIFTH_YEAR"));
			obj.setR28_non_interest_bearing(rs.getBigDecimal("R28_NON_INTEREST_BEARING"));
			obj.setR28_cumulative_total(rs.getBigDecimal("R28_CUMULATIVE_TOTAL"));

			// =========================
			// R29 FIELDS
			// =========================
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_first_month(rs.getBigDecimal("R29_FIRST_MONTH"));
			obj.setR29_third_month(rs.getBigDecimal("R29_THIRD_MONTH"));
			obj.setR29_last_month(rs.getBigDecimal("R29_LAST_MONTH"));
			obj.setR29_first_year(rs.getBigDecimal("R29_FIRST_YEAR"));
			obj.setR29_fifth_year(rs.getBigDecimal("R29_FIFTH_YEAR"));
			obj.setR29_non_interest_bearing(rs.getBigDecimal("R29_NON_INTEREST_BEARING"));
			obj.setR29_cumulative_total(rs.getBigDecimal("R29_CUMULATIVE_TOTAL"));

			// =========================
			// R30 FIELDS
			// =========================
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_first_month(rs.getBigDecimal("R30_FIRST_MONTH"));
			obj.setR30_third_month(rs.getBigDecimal("R30_THIRD_MONTH"));
			obj.setR30_last_month(rs.getBigDecimal("R30_LAST_MONTH"));
			obj.setR30_first_year(rs.getBigDecimal("R30_FIRST_YEAR"));
			obj.setR30_fifth_year(rs.getBigDecimal("R30_FIFTH_YEAR"));
			obj.setR30_non_interest_bearing(rs.getBigDecimal("R30_NON_INTEREST_BEARING"));
			obj.setR30_cumulative_total(rs.getBigDecimal("R30_CUMULATIVE_TOTAL"));

			// =========================
			// R31 FIELDS
			// =========================
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_first_month(rs.getBigDecimal("R31_FIRST_MONTH"));
			obj.setR31_third_month(rs.getBigDecimal("R31_THIRD_MONTH"));
			obj.setR31_last_month(rs.getBigDecimal("R31_LAST_MONTH"));
			obj.setR31_first_year(rs.getBigDecimal("R31_FIRST_YEAR"));
			obj.setR31_fifth_year(rs.getBigDecimal("R31_FIFTH_YEAR"));
			obj.setR31_non_interest_bearing(rs.getBigDecimal("R31_NON_INTEREST_BEARING"));
			obj.setR31_cumulative_total(rs.getBigDecimal("R31_CUMULATIVE_TOTAL"));

			// =========================
			// R32 FIELDS
			// =========================
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_first_month(rs.getBigDecimal("R32_FIRST_MONTH"));
			obj.setR32_third_month(rs.getBigDecimal("R32_THIRD_MONTH"));
			obj.setR32_last_month(rs.getBigDecimal("R32_LAST_MONTH"));
			obj.setR32_first_year(rs.getBigDecimal("R32_FIRST_YEAR"));
			obj.setR32_fifth_year(rs.getBigDecimal("R32_FIFTH_YEAR"));
			obj.setR32_cumulative_total(rs.getBigDecimal("R32_CUMULATIVE_TOTAL"));

			// =========================
			// R33 FIELDS
			// =========================
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_first_month(rs.getBigDecimal("R33_FIRST_MONTH"));
			obj.setR33_cumulative_total(rs.getBigDecimal("R33_CUMULATIVE_TOTAL"));

			// =========================
			// R34 FIELDS
			// =========================
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_third_month(rs.getBigDecimal("R34_THIRD_MONTH"));
			obj.setR34_last_month(rs.getBigDecimal("R34_LAST_MONTH"));
			obj.setR34_first_year(rs.getBigDecimal("R34_FIRST_YEAR"));
			obj.setR34_fifth_year(rs.getBigDecimal("R34_FIFTH_YEAR"));
			obj.setR34_non_interest_bearing(rs.getBigDecimal("R34_NON_INTEREST_BEARING"));
			obj.setR34_cumulative_total(rs.getBigDecimal("R34_CUMULATIVE_TOTAL"));

			// =========================
			// R35 FIELDS
			// =========================
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_first_month(rs.getBigDecimal("R35_FIRST_MONTH"));
			obj.setR35_third_month(rs.getBigDecimal("R35_THIRD_MONTH"));
			obj.setR35_last_month(rs.getBigDecimal("R35_LAST_MONTH"));
			obj.setR35_first_year(rs.getBigDecimal("R35_FIRST_YEAR"));
			obj.setR35_fifth_year(rs.getBigDecimal("R35_FIFTH_YEAR"));
			obj.setR35_non_interest_bearing(rs.getBigDecimal("R35_NON_INTEREST_BEARING"));
			obj.setR35_cumulative_total(rs.getBigDecimal("R35_CUMULATIVE_TOTAL"));

			// =========================
			// R36 FIELDS
			// =========================
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_first_month(rs.getBigDecimal("R36_FIRST_MONTH"));
			obj.setR36_third_month(rs.getBigDecimal("R36_THIRD_MONTH"));
			obj.setR36_last_month(rs.getBigDecimal("R36_LAST_MONTH"));
			obj.setR36_first_year(rs.getBigDecimal("R36_FIRST_YEAR"));
			obj.setR36_fifth_year(rs.getBigDecimal("R36_FIFTH_YEAR"));
			obj.setR36_non_interest_bearing(rs.getBigDecimal("R36_NON_INTEREST_BEARING"));
			obj.setR36_cumulative_total(rs.getBigDecimal("R36_CUMULATIVE_TOTAL"));

			// =========================
			// R37 FIELDS
			// =========================
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_first_month(rs.getBigDecimal("R37_FIRST_MONTH"));
			obj.setR37_third_month(rs.getBigDecimal("R37_THIRD_MONTH"));
			obj.setR37_last_month(rs.getBigDecimal("R37_LAST_MONTH"));
			obj.setR37_first_year(rs.getBigDecimal("R37_FIRST_YEAR"));
			obj.setR37_fifth_year(rs.getBigDecimal("R37_FIFTH_YEAR"));
			obj.setR37_non_interest_bearing(rs.getBigDecimal("R37_NON_INTEREST_BEARING"));
			obj.setR37_cumulative_total(rs.getBigDecimal("R37_CUMULATIVE_TOTAL"));

			// =========================
			// R38 FIELDS
			// =========================
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_first_month(rs.getBigDecimal("R38_FIRST_MONTH"));
			obj.setR38_third_month(rs.getBigDecimal("R38_THIRD_MONTH"));
			obj.setR38_last_month(rs.getBigDecimal("R38_LAST_MONTH"));
			obj.setR38_first_year(rs.getBigDecimal("R38_FIRST_YEAR"));
			obj.setR38_fifth_year(rs.getBigDecimal("R38_FIFTH_YEAR"));
			obj.setR38_non_interest_bearing(rs.getBigDecimal("R38_NON_INTEREST_BEARING"));
			obj.setR38_cumulative_total(rs.getBigDecimal("R38_CUMULATIVE_TOTAL"));

			// =========================
			// R39 FIELDS
			// =========================
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_first_month(rs.getBigDecimal("R39_FIRST_MONTH"));
			obj.setR39_third_month(rs.getBigDecimal("R39_THIRD_MONTH"));
			obj.setR39_last_month(rs.getBigDecimal("R39_LAST_MONTH"));
			obj.setR39_first_year(rs.getBigDecimal("R39_FIRST_YEAR"));
			obj.setR39_fifth_year(rs.getBigDecimal("R39_FIFTH_YEAR"));
			obj.setR39_non_interest_bearing(rs.getBigDecimal("R39_NON_INTEREST_BEARING"));
			obj.setR39_cumulative_total(rs.getBigDecimal("R39_CUMULATIVE_TOTAL"));

			// =========================
			// R40 FIELDS
			// =========================
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_first_month(rs.getBigDecimal("R40_FIRST_MONTH"));
			obj.setR40_third_month(rs.getBigDecimal("R40_THIRD_MONTH"));
			obj.setR40_last_month(rs.getBigDecimal("R40_LAST_MONTH"));
			obj.setR40_first_year(rs.getBigDecimal("R40_FIRST_YEAR"));
			obj.setR40_fifth_year(rs.getBigDecimal("R40_FIFTH_YEAR"));
			obj.setR40_non_interest_bearing(rs.getBigDecimal("R40_NON_INTEREST_BEARING"));
			obj.setR40_cumulative_total(rs.getBigDecimal("R40_CUMULATIVE_TOTAL"));

			// =========================
			// R41 FIELDS
			// =========================
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_first_month(rs.getBigDecimal("R41_FIRST_MONTH"));
			obj.setR41_third_month(rs.getBigDecimal("R41_THIRD_MONTH"));
			obj.setR41_last_month(rs.getBigDecimal("R41_LAST_MONTH"));
			obj.setR41_first_year(rs.getBigDecimal("R41_FIRST_YEAR"));
			obj.setR41_fifth_year(rs.getBigDecimal("R41_FIFTH_YEAR"));
			obj.setR41_non_interest_bearing(rs.getBigDecimal("R41_NON_INTEREST_BEARING"));
			obj.setR41_cumulative_total(rs.getBigDecimal("R41_CUMULATIVE_TOTAL"));

			// =========================
			// R42 FIELDS
			// =========================
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_first_month(rs.getBigDecimal("R42_FIRST_MONTH"));
			obj.setR42_third_month(rs.getBigDecimal("R42_THIRD_MONTH"));
			obj.setR42_last_month(rs.getBigDecimal("R42_LAST_MONTH"));
			obj.setR42_first_year(rs.getBigDecimal("R42_FIRST_YEAR"));
			obj.setR42_fifth_year(rs.getBigDecimal("R42_FIFTH_YEAR"));
			obj.setR42_non_interest_bearing(rs.getBigDecimal("R42_NON_INTEREST_BEARING"));
			obj.setR42_cumulative_total(rs.getBigDecimal("R42_CUMULATIVE_TOTAL"));

			// =========================
			// R43 FIELDS
			// =========================
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_first_month(rs.getBigDecimal("R43_FIRST_MONTH"));
			obj.setR43_third_month(rs.getBigDecimal("R43_THIRD_MONTH"));
			obj.setR43_last_month(rs.getBigDecimal("R43_LAST_MONTH"));
			obj.setR43_first_year(rs.getBigDecimal("R43_FIRST_YEAR"));
			obj.setR43_fifth_year(rs.getBigDecimal("R43_FIFTH_YEAR"));
			obj.setR43_non_interest_bearing(rs.getBigDecimal("R43_NON_INTEREST_BEARING"));
			obj.setR43_cumulative_total(rs.getBigDecimal("R43_CUMULATIVE_TOTAL"));

			// =========================
			// R44 FIELDS
			// =========================
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_first_month(rs.getBigDecimal("R44_FIRST_MONTH"));
			obj.setR44_third_month(rs.getBigDecimal("R44_THIRD_MONTH"));
			obj.setR44_last_month(rs.getBigDecimal("R44_LAST_MONTH"));
			obj.setR44_first_year(rs.getBigDecimal("R44_FIRST_YEAR"));
			obj.setR44_fifth_year(rs.getBigDecimal("R44_FIFTH_YEAR"));
			obj.setR44_non_interest_bearing(rs.getBigDecimal("R44_NON_INTEREST_BEARING"));
			obj.setR44_cumulative_total(rs.getBigDecimal("R44_CUMULATIVE_TOTAL"));

			// =========================
			// R45 FIELDS
			// =========================
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_first_month(rs.getBigDecimal("R45_FIRST_MONTH"));
			obj.setR45_third_month(rs.getBigDecimal("R45_THIRD_MONTH"));
			obj.setR45_last_month(rs.getBigDecimal("R45_LAST_MONTH"));
			obj.setR45_first_year(rs.getBigDecimal("R45_FIRST_YEAR"));
			obj.setR45_fifth_year(rs.getBigDecimal("R45_FIFTH_YEAR"));
			obj.setR45_non_interest_bearing(rs.getBigDecimal("R45_NON_INTEREST_BEARING"));
			obj.setR45_cumulative_total(rs.getBigDecimal("R45_CUMULATIVE_TOTAL"));

			// =========================
			// R46 FIELDS
			// =========================
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_first_month(rs.getBigDecimal("R46_FIRST_MONTH"));
			obj.setR46_third_month(rs.getBigDecimal("R46_THIRD_MONTH"));
			obj.setR46_last_month(rs.getBigDecimal("R46_LAST_MONTH"));
			obj.setR46_first_year(rs.getBigDecimal("R46_FIRST_YEAR"));
			obj.setR46_fifth_year(rs.getBigDecimal("R46_FIFTH_YEAR"));
			obj.setR46_non_interest_bearing(rs.getBigDecimal("R46_NON_INTEREST_BEARING"));
			obj.setR46_cumulative_total(rs.getBigDecimal("R46_CUMULATIVE_TOTAL"));

			// =========================
			// R47 FIELDS
			// =========================
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_first_month(rs.getBigDecimal("R47_FIRST_MONTH"));
			obj.setR47_third_month(rs.getBigDecimal("R47_THIRD_MONTH"));
			obj.setR47_last_month(rs.getBigDecimal("R47_LAST_MONTH"));
			obj.setR47_first_year(rs.getBigDecimal("R47_FIRST_YEAR"));
			obj.setR47_fifth_year(rs.getBigDecimal("R47_FIFTH_YEAR"));
			obj.setR47_non_interest_bearing(rs.getBigDecimal("R47_NON_INTEREST_BEARING"));
			obj.setR47_cumulative_total(rs.getBigDecimal("R47_CUMULATIVE_TOTAL"));

			// =========================
			// R48 FIELDS
			// =========================
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_first_month(rs.getBigDecimal("R48_FIRST_MONTH"));
			obj.setR48_third_month(rs.getBigDecimal("R48_THIRD_MONTH"));
			obj.setR48_last_month(rs.getBigDecimal("R48_LAST_MONTH"));
			obj.setR48_first_year(rs.getBigDecimal("R48_FIRST_YEAR"));
			obj.setR48_fifth_year(rs.getBigDecimal("R48_FIFTH_YEAR"));
			obj.setR48_non_interest_bearing(rs.getBigDecimal("R48_NON_INTEREST_BEARING"));
			obj.setR48_cumulative_total(rs.getBigDecimal("R48_CUMULATIVE_TOTAL"));

			// =========================
			// R49 FIELDS
			// =========================
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_first_month(rs.getBigDecimal("R49_FIRST_MONTH"));
			obj.setR49_third_month(rs.getBigDecimal("R49_THIRD_MONTH"));
			obj.setR49_last_month(rs.getBigDecimal("R49_LAST_MONTH"));
			obj.setR49_first_year(rs.getBigDecimal("R49_FIRST_YEAR"));
			obj.setR49_fifth_year(rs.getBigDecimal("R49_FIFTH_YEAR"));
			obj.setR49_non_interest_bearing(rs.getBigDecimal("R49_NON_INTEREST_BEARING"));
			obj.setR49_cumulative_total(rs.getBigDecimal("R49_CUMULATIVE_TOTAL"));

			// =========================
			// R50 FIELDS
			// =========================
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_first_month(rs.getBigDecimal("R50_FIRST_MONTH"));
			obj.setR50_third_month(rs.getBigDecimal("R50_THIRD_MONTH"));
			obj.setR50_last_month(rs.getBigDecimal("R50_LAST_MONTH"));
			obj.setR50_first_year(rs.getBigDecimal("R50_FIRST_YEAR"));
			obj.setR50_fifth_year(rs.getBigDecimal("R50_FIFTH_YEAR"));
			obj.setR50_non_interest_bearing(rs.getBigDecimal("R50_NON_INTEREST_BEARING"));
			obj.setR50_cumulative_total(rs.getBigDecimal("R50_CUMULATIVE_TOTAL"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReportDate(rs.getDate("report_date"));
			obj.setReportVersion(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Archival_Summary_Entity
	// ===========================

	public class M_LIQGAP_Archival_Summary_Entity {

		private String r10_product;
		private BigDecimal r10_first_month;
		private BigDecimal r10_third_month;
		private BigDecimal r10_last_month;
		private BigDecimal r10_first_year;
		private BigDecimal r10_fifth_year;
		private BigDecimal r10_non_interest_bearing;
		private BigDecimal r10_cumulative_total;

		private String r11_product;
		private BigDecimal r11_first_month;
		private BigDecimal r11_third_month;
		private BigDecimal r11_last_month;
		private BigDecimal r11_first_year;
		private BigDecimal r11_fifth_year;
		private BigDecimal r11_non_interest_bearing;
		private BigDecimal r11_cumulative_total;

		private String r12_product;
		private BigDecimal r12_first_month;
		private BigDecimal r12_third_month;
		private BigDecimal r12_last_month;
		private BigDecimal r12_first_year;
		private BigDecimal r12_fifth_year;
		private BigDecimal r12_non_interest_bearing;
		private BigDecimal r12_cumulative_total;

		private String r13_product;
		private BigDecimal r13_first_month;
		private BigDecimal r13_third_month;
		private BigDecimal r13_last_month;
		private BigDecimal r13_first_year;
		private BigDecimal r13_fifth_year;
		private BigDecimal r13_non_interest_bearing;
		private BigDecimal r13_cumulative_total;

		private String r14_product;
		private BigDecimal r14_first_month;
		private BigDecimal r14_third_month;
		private BigDecimal r14_last_month;
		private BigDecimal r14_first_year;
		private BigDecimal r14_fifth_year;
		private BigDecimal r14_non_interest_bearing;
		private BigDecimal r14_cumulative_total;

		private String r15_product;
		private BigDecimal r15_first_month;
		private BigDecimal r15_third_month;
		private BigDecimal r15_last_month;
		private BigDecimal r15_first_year;
		private BigDecimal r15_fifth_year;
		private BigDecimal r15_non_interest_bearing;
		private BigDecimal r15_cumulative_total;

		private String r16_product;
		private BigDecimal r16_first_month;
		private BigDecimal r16_third_month;
		private BigDecimal r16_last_month;
		private BigDecimal r16_first_year;
		private BigDecimal r16_fifth_year;
		private BigDecimal r16_non_interest_bearing;
		private BigDecimal r16_cumulative_total;

		private String r17_product;
		private BigDecimal r17_first_month;
		private BigDecimal r17_third_month;
		private BigDecimal r17_last_month;
		private BigDecimal r17_first_year;
		private BigDecimal r17_fifth_year;
		private BigDecimal r17_non_interest_bearing;
		private BigDecimal r17_cumulative_total;

		private String r18_product;
		private BigDecimal r18_first_month;
		private BigDecimal r18_third_month;
		private BigDecimal r18_last_month;
		private BigDecimal r18_first_year;
		private BigDecimal r18_fifth_year;
		private BigDecimal r18_non_interest_bearing;
		private BigDecimal r18_cumulative_total;

		private String r19_product;
		private BigDecimal r19_first_month;
		private BigDecimal r19_third_month;
		private BigDecimal r19_last_month;
		private BigDecimal r19_first_year;
		private BigDecimal r19_fifth_year;
		private BigDecimal r19_non_interest_bearing;
		private BigDecimal r19_cumulative_total;

		private String r20_product;
		private BigDecimal r20_first_month;
		private BigDecimal r20_third_month;
		private BigDecimal r20_last_month;
		private BigDecimal r20_first_year;
		private BigDecimal r20_fifth_year;
		private BigDecimal r20_non_interest_bearing;
		private BigDecimal r20_cumulative_total;

		private String r21_product;
		private BigDecimal r21_first_month;
		private BigDecimal r21_third_month;
		private BigDecimal r21_last_month;
		private BigDecimal r21_first_year;
		private BigDecimal r21_fifth_year;
		private BigDecimal r21_cumulative_total;

		private String r22_product;
		private BigDecimal r22_first_month;
		private BigDecimal r22_third_month;
		private BigDecimal r22_last_month;
		private BigDecimal r22_first_year;
		private BigDecimal r22_fifth_year;
		private BigDecimal r22_non_interest_bearing;
		private BigDecimal r22_cumulative_total;

		private String r23_product;
		private BigDecimal r23_first_month;
		private BigDecimal r23_third_month;
		private BigDecimal r23_last_month;
		private BigDecimal r23_first_year;
		private BigDecimal r23_fifth_year;
		private BigDecimal r23_non_interest_bearing;
		private BigDecimal r23_cumulative_total;

		private String r24_product;
		private BigDecimal r24_first_month;
		private BigDecimal r24_third_month;
		private BigDecimal r24_last_month;
		private BigDecimal r24_first_year;
		private BigDecimal r24_fifth_year;
		private BigDecimal r24_non_interest_bearing;
		private BigDecimal r24_cumulative_total;

		private String r25_product;
		private BigDecimal r25_first_month;
		private BigDecimal r25_third_month;
		private BigDecimal r25_last_month;
		private BigDecimal r25_first_year;
		private BigDecimal r25_fifth_year;
		private BigDecimal r25_non_interest_bearing;
		private BigDecimal r25_cumulative_total;

		private String r26_product;
		private BigDecimal r26_first_month;
		private BigDecimal r26_third_month;
		private BigDecimal r26_last_month;
		private BigDecimal r26_first_year;
		private BigDecimal r26_fifth_year;
		private BigDecimal r26_non_interest_bearing;
		private BigDecimal r26_cumulative_total;

		private String r27_product;
		private BigDecimal r27_first_month;
		private BigDecimal r27_third_month;
		private BigDecimal r27_last_month;
		private BigDecimal r27_first_year;
		private BigDecimal r27_fifth_year;
		private BigDecimal r27_non_interest_bearing;
		private BigDecimal r27_cumulative_total;

		private String r28_product;
		private BigDecimal r28_first_month;
		private BigDecimal r28_third_month;
		private BigDecimal r28_last_month;
		private BigDecimal r28_first_year;
		private BigDecimal r28_fifth_year;
		private BigDecimal r28_non_interest_bearing;
		private BigDecimal r28_cumulative_total;

		private String r29_product;
		private BigDecimal r29_first_month;
		private BigDecimal r29_third_month;
		private BigDecimal r29_last_month;
		private BigDecimal r29_first_year;
		private BigDecimal r29_fifth_year;
		private BigDecimal r29_non_interest_bearing;
		private BigDecimal r29_cumulative_total;

		private String r30_product;
		private BigDecimal r30_first_month;
		private BigDecimal r30_third_month;
		private BigDecimal r30_last_month;
		private BigDecimal r30_first_year;
		private BigDecimal r30_fifth_year;
		private BigDecimal r30_non_interest_bearing;
		private BigDecimal r30_cumulative_total;

		private String r31_product;
		private BigDecimal r31_first_month;
		private BigDecimal r31_third_month;
		private BigDecimal r31_last_month;
		private BigDecimal r31_first_year;
		private BigDecimal r31_fifth_year;
		private BigDecimal r31_non_interest_bearing;
		private BigDecimal r31_cumulative_total;

		private String r32_product;
		private BigDecimal r32_first_month;
		private BigDecimal r32_third_month;
		private BigDecimal r32_last_month;
		private BigDecimal r32_first_year;
		private BigDecimal r32_fifth_year;
		private BigDecimal r32_cumulative_total;

		private String r33_product;
		private BigDecimal r33_first_month;
		private BigDecimal r33_cumulative_total;

		private String r34_product;
		private BigDecimal r34_third_month;
		private BigDecimal r34_last_month;
		private BigDecimal r34_first_year;
		private BigDecimal r34_fifth_year;
		private BigDecimal r34_non_interest_bearing;
		private BigDecimal r34_cumulative_total;

		private String r35_product;
		private BigDecimal r35_first_month;
		private BigDecimal r35_third_month;
		private BigDecimal r35_last_month;
		private BigDecimal r35_first_year;
		private BigDecimal r35_fifth_year;
		private BigDecimal r35_non_interest_bearing;
		private BigDecimal r35_cumulative_total;

		private String r36_product;
		private BigDecimal r36_first_month;
		private BigDecimal r36_third_month;
		private BigDecimal r36_last_month;
		private BigDecimal r36_first_year;
		private BigDecimal r36_fifth_year;
		private BigDecimal r36_non_interest_bearing;
		private BigDecimal r36_cumulative_total;

		private String r37_product;
		private BigDecimal r37_first_month;
		private BigDecimal r37_third_month;
		private BigDecimal r37_last_month;
		private BigDecimal r37_first_year;
		private BigDecimal r37_fifth_year;
		private BigDecimal r37_non_interest_bearing;
		private BigDecimal r37_cumulative_total;

		private String r38_product;
		private BigDecimal r38_first_month;
		private BigDecimal r38_third_month;
		private BigDecimal r38_last_month;
		private BigDecimal r38_first_year;
		private BigDecimal r38_fifth_year;
		private BigDecimal r38_non_interest_bearing;
		private BigDecimal r38_cumulative_total;

		private String r39_product;
		private BigDecimal r39_first_month;
		private BigDecimal r39_third_month;
		private BigDecimal r39_last_month;
		private BigDecimal r39_first_year;
		private BigDecimal r39_fifth_year;
		private BigDecimal r39_non_interest_bearing;
		private BigDecimal r39_cumulative_total;

		private String r40_product;
		private BigDecimal r40_first_month;
		private BigDecimal r40_third_month;
		private BigDecimal r40_last_month;
		private BigDecimal r40_first_year;
		private BigDecimal r40_fifth_year;
		private BigDecimal r40_non_interest_bearing;
		private BigDecimal r40_cumulative_total;

		private String r41_product;
		private BigDecimal r41_first_month;
		private BigDecimal r41_third_month;
		private BigDecimal r41_last_month;
		private BigDecimal r41_first_year;
		private BigDecimal r41_fifth_year;
		private BigDecimal r41_non_interest_bearing;
		private BigDecimal r41_cumulative_total;

		private String r42_product;
		private BigDecimal r42_first_month;
		private BigDecimal r42_third_month;
		private BigDecimal r42_last_month;
		private BigDecimal r42_first_year;
		private BigDecimal r42_fifth_year;
		private BigDecimal r42_non_interest_bearing;
		private BigDecimal r42_cumulative_total;

		private String r43_product;
		private BigDecimal r43_first_month;
		private BigDecimal r43_third_month;
		private BigDecimal r43_last_month;
		private BigDecimal r43_first_year;
		private BigDecimal r43_fifth_year;
		private BigDecimal r43_non_interest_bearing;
		private BigDecimal r43_cumulative_total;

		private String r44_product;
		private BigDecimal r44_first_month;
		private BigDecimal r44_third_month;
		private BigDecimal r44_last_month;
		private BigDecimal r44_first_year;
		private BigDecimal r44_fifth_year;
		private BigDecimal r44_non_interest_bearing;
		private BigDecimal r44_cumulative_total;

		private String r45_product;
		private BigDecimal r45_first_month;
		private BigDecimal r45_third_month;
		private BigDecimal r45_last_month;
		private BigDecimal r45_first_year;
		private BigDecimal r45_fifth_year;
		private BigDecimal r45_non_interest_bearing;
		private BigDecimal r45_cumulative_total;

		private String r46_product;
		private BigDecimal r46_first_month;
		private BigDecimal r46_third_month;
		private BigDecimal r46_last_month;
		private BigDecimal r46_first_year;
		private BigDecimal r46_fifth_year;
		private BigDecimal r46_non_interest_bearing;
		private BigDecimal r46_cumulative_total;

		private String r47_product;
		private BigDecimal r47_first_month;
		private BigDecimal r47_third_month;
		private BigDecimal r47_last_month;
		private BigDecimal r47_first_year;
		private BigDecimal r47_fifth_year;
		private BigDecimal r47_non_interest_bearing;
		private BigDecimal r47_cumulative_total;

		private String r48_product;
		private BigDecimal r48_first_month;
		private BigDecimal r48_third_month;
		private BigDecimal r48_last_month;
		private BigDecimal r48_first_year;
		private BigDecimal r48_fifth_year;
		private BigDecimal r48_non_interest_bearing;
		private BigDecimal r48_cumulative_total;

		private String r49_product;
		private BigDecimal r49_first_month;
		private BigDecimal r49_third_month;
		private BigDecimal r49_last_month;
		private BigDecimal r49_first_year;
		private BigDecimal r49_fifth_year;
		private BigDecimal r49_non_interest_bearing;
		private BigDecimal r49_cumulative_total;

		private String r50_product;
		private BigDecimal r50_first_month;
		private BigDecimal r50_third_month;
		private BigDecimal r50_last_month;
		private BigDecimal r50_first_year;
		private BigDecimal r50_fifth_year;
		private BigDecimal r50_non_interest_bearing;
		private BigDecimal r50_cumulative_total;

		// Common Fields

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date reportDate;

		@Id
		private BigDecimal reportVersion;

		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Default constructor
		public M_LIQGAP_Archival_Summary_Entity() {
			super();
		}

		// Getters and Setters

		// R10 Getters and Setters
		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_first_month() {
			return r10_first_month;
		}

		public void setR10_first_month(BigDecimal r10_first_month) {
			this.r10_first_month = r10_first_month;
		}

		public BigDecimal getR10_third_month() {
			return r10_third_month;
		}

		public void setR10_third_month(BigDecimal r10_third_month) {
			this.r10_third_month = r10_third_month;
		}

		public BigDecimal getR10_last_month() {
			return r10_last_month;
		}

		public void setR10_last_month(BigDecimal r10_last_month) {
			this.r10_last_month = r10_last_month;
		}

		public BigDecimal getR10_first_year() {
			return r10_first_year;
		}

		public void setR10_first_year(BigDecimal r10_first_year) {
			this.r10_first_year = r10_first_year;
		}

		public BigDecimal getR10_fifth_year() {
			return r10_fifth_year;
		}

		public void setR10_fifth_year(BigDecimal r10_fifth_year) {
			this.r10_fifth_year = r10_fifth_year;
		}

		public BigDecimal getR10_non_interest_bearing() {
			return r10_non_interest_bearing;
		}

		public void setR10_non_interest_bearing(BigDecimal r10_non_interest_bearing) {
			this.r10_non_interest_bearing = r10_non_interest_bearing;
		}

		public BigDecimal getR10_cumulative_total() {
			return r10_cumulative_total;
		}

		public void setR10_cumulative_total(BigDecimal r10_cumulative_total) {
			this.r10_cumulative_total = r10_cumulative_total;
		}

		// R11 Getters and Setters
		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_first_month() {
			return r11_first_month;
		}

		public void setR11_first_month(BigDecimal r11_first_month) {
			this.r11_first_month = r11_first_month;
		}

		public BigDecimal getR11_third_month() {
			return r11_third_month;
		}

		public void setR11_third_month(BigDecimal r11_third_month) {
			this.r11_third_month = r11_third_month;
		}

		public BigDecimal getR11_last_month() {
			return r11_last_month;
		}

		public void setR11_last_month(BigDecimal r11_last_month) {
			this.r11_last_month = r11_last_month;
		}

		public BigDecimal getR11_first_year() {
			return r11_first_year;
		}

		public void setR11_first_year(BigDecimal r11_first_year) {
			this.r11_first_year = r11_first_year;
		}

		public BigDecimal getR11_fifth_year() {
			return r11_fifth_year;
		}

		public void setR11_fifth_year(BigDecimal r11_fifth_year) {
			this.r11_fifth_year = r11_fifth_year;
		}

		public BigDecimal getR11_non_interest_bearing() {
			return r11_non_interest_bearing;
		}

		public void setR11_non_interest_bearing(BigDecimal r11_non_interest_bearing) {
			this.r11_non_interest_bearing = r11_non_interest_bearing;
		}

		public BigDecimal getR11_cumulative_total() {
			return r11_cumulative_total;
		}

		public void setR11_cumulative_total(BigDecimal r11_cumulative_total) {
			this.r11_cumulative_total = r11_cumulative_total;
		}

		// R12 Getters and Setters
		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_first_month() {
			return r12_first_month;
		}

		public void setR12_first_month(BigDecimal r12_first_month) {
			this.r12_first_month = r12_first_month;
		}

		public BigDecimal getR12_third_month() {
			return r12_third_month;
		}

		public void setR12_third_month(BigDecimal r12_third_month) {
			this.r12_third_month = r12_third_month;
		}

		public BigDecimal getR12_last_month() {
			return r12_last_month;
		}

		public void setR12_last_month(BigDecimal r12_last_month) {
			this.r12_last_month = r12_last_month;
		}

		public BigDecimal getR12_first_year() {
			return r12_first_year;
		}

		public void setR12_first_year(BigDecimal r12_first_year) {
			this.r12_first_year = r12_first_year;
		}

		public BigDecimal getR12_fifth_year() {
			return r12_fifth_year;
		}

		public void setR12_fifth_year(BigDecimal r12_fifth_year) {
			this.r12_fifth_year = r12_fifth_year;
		}

		public BigDecimal getR12_non_interest_bearing() {
			return r12_non_interest_bearing;
		}

		public void setR12_non_interest_bearing(BigDecimal r12_non_interest_bearing) {
			this.r12_non_interest_bearing = r12_non_interest_bearing;
		}

		public BigDecimal getR12_cumulative_total() {
			return r12_cumulative_total;
		}

		public void setR12_cumulative_total(BigDecimal r12_cumulative_total) {
			this.r12_cumulative_total = r12_cumulative_total;
		}

		// R13 Getters and Setters
		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_first_month() {
			return r13_first_month;
		}

		public void setR13_first_month(BigDecimal r13_first_month) {
			this.r13_first_month = r13_first_month;
		}

		public BigDecimal getR13_third_month() {
			return r13_third_month;
		}

		public void setR13_third_month(BigDecimal r13_third_month) {
			this.r13_third_month = r13_third_month;
		}

		public BigDecimal getR13_last_month() {
			return r13_last_month;
		}

		public void setR13_last_month(BigDecimal r13_last_month) {
			this.r13_last_month = r13_last_month;
		}

		public BigDecimal getR13_first_year() {
			return r13_first_year;
		}

		public void setR13_first_year(BigDecimal r13_first_year) {
			this.r13_first_year = r13_first_year;
		}

		public BigDecimal getR13_fifth_year() {
			return r13_fifth_year;
		}

		public void setR13_fifth_year(BigDecimal r13_fifth_year) {
			this.r13_fifth_year = r13_fifth_year;
		}

		public BigDecimal getR13_non_interest_bearing() {
			return r13_non_interest_bearing;
		}

		public void setR13_non_interest_bearing(BigDecimal r13_non_interest_bearing) {
			this.r13_non_interest_bearing = r13_non_interest_bearing;
		}

		public BigDecimal getR13_cumulative_total() {
			return r13_cumulative_total;
		}

		public void setR13_cumulative_total(BigDecimal r13_cumulative_total) {
			this.r13_cumulative_total = r13_cumulative_total;
		}

		// R14 Getters and Setters
		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_first_month() {
			return r14_first_month;
		}

		public void setR14_first_month(BigDecimal r14_first_month) {
			this.r14_first_month = r14_first_month;
		}

		public BigDecimal getR14_third_month() {
			return r14_third_month;
		}

		public void setR14_third_month(BigDecimal r14_third_month) {
			this.r14_third_month = r14_third_month;
		}

		public BigDecimal getR14_last_month() {
			return r14_last_month;
		}

		public void setR14_last_month(BigDecimal r14_last_month) {
			this.r14_last_month = r14_last_month;
		}

		public BigDecimal getR14_first_year() {
			return r14_first_year;
		}

		public void setR14_first_year(BigDecimal r14_first_year) {
			this.r14_first_year = r14_first_year;
		}

		public BigDecimal getR14_fifth_year() {
			return r14_fifth_year;
		}

		public void setR14_fifth_year(BigDecimal r14_fifth_year) {
			this.r14_fifth_year = r14_fifth_year;
		}

		public BigDecimal getR14_non_interest_bearing() {
			return r14_non_interest_bearing;
		}

		public void setR14_non_interest_bearing(BigDecimal r14_non_interest_bearing) {
			this.r14_non_interest_bearing = r14_non_interest_bearing;
		}

		public BigDecimal getR14_cumulative_total() {
			return r14_cumulative_total;
		}

		public void setR14_cumulative_total(BigDecimal r14_cumulative_total) {
			this.r14_cumulative_total = r14_cumulative_total;
		}

		// R15 Getters and Setters
		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_first_month() {
			return r15_first_month;
		}

		public void setR15_first_month(BigDecimal r15_first_month) {
			this.r15_first_month = r15_first_month;
		}

		public BigDecimal getR15_third_month() {
			return r15_third_month;
		}

		public void setR15_third_month(BigDecimal r15_third_month) {
			this.r15_third_month = r15_third_month;
		}

		public BigDecimal getR15_last_month() {
			return r15_last_month;
		}

		public void setR15_last_month(BigDecimal r15_last_month) {
			this.r15_last_month = r15_last_month;
		}

		public BigDecimal getR15_first_year() {
			return r15_first_year;
		}

		public void setR15_first_year(BigDecimal r15_first_year) {
			this.r15_first_year = r15_first_year;
		}

		public BigDecimal getR15_fifth_year() {
			return r15_fifth_year;
		}

		public void setR15_fifth_year(BigDecimal r15_fifth_year) {
			this.r15_fifth_year = r15_fifth_year;
		}

		public BigDecimal getR15_non_interest_bearing() {
			return r15_non_interest_bearing;
		}

		public void setR15_non_interest_bearing(BigDecimal r15_non_interest_bearing) {
			this.r15_non_interest_bearing = r15_non_interest_bearing;
		}

		public BigDecimal getR15_cumulative_total() {
			return r15_cumulative_total;
		}

		public void setR15_cumulative_total(BigDecimal r15_cumulative_total) {
			this.r15_cumulative_total = r15_cumulative_total;
		}

		// R16 Getters and Setters
		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_first_month() {
			return r16_first_month;
		}

		public void setR16_first_month(BigDecimal r16_first_month) {
			this.r16_first_month = r16_first_month;
		}

		public BigDecimal getR16_third_month() {
			return r16_third_month;
		}

		public void setR16_third_month(BigDecimal r16_third_month) {
			this.r16_third_month = r16_third_month;
		}

		public BigDecimal getR16_last_month() {
			return r16_last_month;
		}

		public void setR16_last_month(BigDecimal r16_last_month) {
			this.r16_last_month = r16_last_month;
		}

		public BigDecimal getR16_first_year() {
			return r16_first_year;
		}

		public void setR16_first_year(BigDecimal r16_first_year) {
			this.r16_first_year = r16_first_year;
		}

		public BigDecimal getR16_fifth_year() {
			return r16_fifth_year;
		}

		public void setR16_fifth_year(BigDecimal r16_fifth_year) {
			this.r16_fifth_year = r16_fifth_year;
		}

		public BigDecimal getR16_non_interest_bearing() {
			return r16_non_interest_bearing;
		}

		public void setR16_non_interest_bearing(BigDecimal r16_non_interest_bearing) {
			this.r16_non_interest_bearing = r16_non_interest_bearing;
		}

		public BigDecimal getR16_cumulative_total() {
			return r16_cumulative_total;
		}

		public void setR16_cumulative_total(BigDecimal r16_cumulative_total) {
			this.r16_cumulative_total = r16_cumulative_total;
		}

		// R17 Getters and Setters
		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_first_month() {
			return r17_first_month;
		}

		public void setR17_first_month(BigDecimal r17_first_month) {
			this.r17_first_month = r17_first_month;
		}

		public BigDecimal getR17_third_month() {
			return r17_third_month;
		}

		public void setR17_third_month(BigDecimal r17_third_month) {
			this.r17_third_month = r17_third_month;
		}

		public BigDecimal getR17_last_month() {
			return r17_last_month;
		}

		public void setR17_last_month(BigDecimal r17_last_month) {
			this.r17_last_month = r17_last_month;
		}

		public BigDecimal getR17_first_year() {
			return r17_first_year;
		}

		public void setR17_first_year(BigDecimal r17_first_year) {
			this.r17_first_year = r17_first_year;
		}

		public BigDecimal getR17_fifth_year() {
			return r17_fifth_year;
		}

		public void setR17_fifth_year(BigDecimal r17_fifth_year) {
			this.r17_fifth_year = r17_fifth_year;
		}

		public BigDecimal getR17_non_interest_bearing() {
			return r17_non_interest_bearing;
		}

		public void setR17_non_interest_bearing(BigDecimal r17_non_interest_bearing) {
			this.r17_non_interest_bearing = r17_non_interest_bearing;
		}

		public BigDecimal getR17_cumulative_total() {
			return r17_cumulative_total;
		}

		public void setR17_cumulative_total(BigDecimal r17_cumulative_total) {
			this.r17_cumulative_total = r17_cumulative_total;
		}

		// R18 Getters and Setters
		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_first_month() {
			return r18_first_month;
		}

		public void setR18_first_month(BigDecimal r18_first_month) {
			this.r18_first_month = r18_first_month;
		}

		public BigDecimal getR18_third_month() {
			return r18_third_month;
		}

		public void setR18_third_month(BigDecimal r18_third_month) {
			this.r18_third_month = r18_third_month;
		}

		public BigDecimal getR18_last_month() {
			return r18_last_month;
		}

		public void setR18_last_month(BigDecimal r18_last_month) {
			this.r18_last_month = r18_last_month;
		}

		public BigDecimal getR18_first_year() {
			return r18_first_year;
		}

		public void setR18_first_year(BigDecimal r18_first_year) {
			this.r18_first_year = r18_first_year;
		}

		public BigDecimal getR18_fifth_year() {
			return r18_fifth_year;
		}

		public void setR18_fifth_year(BigDecimal r18_fifth_year) {
			this.r18_fifth_year = r18_fifth_year;
		}

		public BigDecimal getR18_non_interest_bearing() {
			return r18_non_interest_bearing;
		}

		public void setR18_non_interest_bearing(BigDecimal r18_non_interest_bearing) {
			this.r18_non_interest_bearing = r18_non_interest_bearing;
		}

		public BigDecimal getR18_cumulative_total() {
			return r18_cumulative_total;
		}

		public void setR18_cumulative_total(BigDecimal r18_cumulative_total) {
			this.r18_cumulative_total = r18_cumulative_total;
		}

		// R19 Getters and Setters
		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_first_month() {
			return r19_first_month;
		}

		public void setR19_first_month(BigDecimal r19_first_month) {
			this.r19_first_month = r19_first_month;
		}

		public BigDecimal getR19_third_month() {
			return r19_third_month;
		}

		public void setR19_third_month(BigDecimal r19_third_month) {
			this.r19_third_month = r19_third_month;
		}

		public BigDecimal getR19_last_month() {
			return r19_last_month;
		}

		public void setR19_last_month(BigDecimal r19_last_month) {
			this.r19_last_month = r19_last_month;
		}

		public BigDecimal getR19_first_year() {
			return r19_first_year;
		}

		public void setR19_first_year(BigDecimal r19_first_year) {
			this.r19_first_year = r19_first_year;
		}

		public BigDecimal getR19_fifth_year() {
			return r19_fifth_year;
		}

		public void setR19_fifth_year(BigDecimal r19_fifth_year) {
			this.r19_fifth_year = r19_fifth_year;
		}

		public BigDecimal getR19_non_interest_bearing() {
			return r19_non_interest_bearing;
		}

		public void setR19_non_interest_bearing(BigDecimal r19_non_interest_bearing) {
			this.r19_non_interest_bearing = r19_non_interest_bearing;
		}

		public BigDecimal getR19_cumulative_total() {
			return r19_cumulative_total;
		}

		public void setR19_cumulative_total(BigDecimal r19_cumulative_total) {
			this.r19_cumulative_total = r19_cumulative_total;
		}

		// R20 Getters and Setters
		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_first_month() {
			return r20_first_month;
		}

		public void setR20_first_month(BigDecimal r20_first_month) {
			this.r20_first_month = r20_first_month;
		}

		public BigDecimal getR20_third_month() {
			return r20_third_month;
		}

		public void setR20_third_month(BigDecimal r20_third_month) {
			this.r20_third_month = r20_third_month;
		}

		public BigDecimal getR20_last_month() {
			return r20_last_month;
		}

		public void setR20_last_month(BigDecimal r20_last_month) {
			this.r20_last_month = r20_last_month;
		}

		public BigDecimal getR20_first_year() {
			return r20_first_year;
		}

		public void setR20_first_year(BigDecimal r20_first_year) {
			this.r20_first_year = r20_first_year;
		}

		public BigDecimal getR20_fifth_year() {
			return r20_fifth_year;
		}

		public void setR20_fifth_year(BigDecimal r20_fifth_year) {
			this.r20_fifth_year = r20_fifth_year;
		}

		public BigDecimal getR20_non_interest_bearing() {
			return r20_non_interest_bearing;
		}

		public void setR20_non_interest_bearing(BigDecimal r20_non_interest_bearing) {
			this.r20_non_interest_bearing = r20_non_interest_bearing;
		}

		public BigDecimal getR20_cumulative_total() {
			return r20_cumulative_total;
		}

		public void setR20_cumulative_total(BigDecimal r20_cumulative_total) {
			this.r20_cumulative_total = r20_cumulative_total;
		}

		// R21 Getters and Setters
		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_first_month() {
			return r21_first_month;
		}

		public void setR21_first_month(BigDecimal r21_first_month) {
			this.r21_first_month = r21_first_month;
		}

		public BigDecimal getR21_third_month() {
			return r21_third_month;
		}

		public void setR21_third_month(BigDecimal r21_third_month) {
			this.r21_third_month = r21_third_month;
		}

		public BigDecimal getR21_last_month() {
			return r21_last_month;
		}

		public void setR21_last_month(BigDecimal r21_last_month) {
			this.r21_last_month = r21_last_month;
		}

		public BigDecimal getR21_first_year() {
			return r21_first_year;
		}

		public void setR21_first_year(BigDecimal r21_first_year) {
			this.r21_first_year = r21_first_year;
		}

		public BigDecimal getR21_fifth_year() {
			return r21_fifth_year;
		}

		public void setR21_fifth_year(BigDecimal r21_fifth_year) {
			this.r21_fifth_year = r21_fifth_year;
		}

		public BigDecimal getR21_cumulative_total() {
			return r21_cumulative_total;
		}

		public void setR21_cumulative_total(BigDecimal r21_cumulative_total) {
			this.r21_cumulative_total = r21_cumulative_total;
		}

		// R22 Getters and Setters
		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_first_month() {
			return r22_first_month;
		}

		public void setR22_first_month(BigDecimal r22_first_month) {
			this.r22_first_month = r22_first_month;
		}

		public BigDecimal getR22_third_month() {
			return r22_third_month;
		}

		public void setR22_third_month(BigDecimal r22_third_month) {
			this.r22_third_month = r22_third_month;
		}

		public BigDecimal getR22_last_month() {
			return r22_last_month;
		}

		public void setR22_last_month(BigDecimal r22_last_month) {
			this.r22_last_month = r22_last_month;
		}

		public BigDecimal getR22_first_year() {
			return r22_first_year;
		}

		public void setR22_first_year(BigDecimal r22_first_year) {
			this.r22_first_year = r22_first_year;
		}

		public BigDecimal getR22_fifth_year() {
			return r22_fifth_year;
		}

		public void setR22_fifth_year(BigDecimal r22_fifth_year) {
			this.r22_fifth_year = r22_fifth_year;
		}

		public BigDecimal getR22_non_interest_bearing() {
			return r22_non_interest_bearing;
		}

		public void setR22_non_interest_bearing(BigDecimal r22_non_interest_bearing) {
			this.r22_non_interest_bearing = r22_non_interest_bearing;
		}

		public BigDecimal getR22_cumulative_total() {
			return r22_cumulative_total;
		}

		public void setR22_cumulative_total(BigDecimal r22_cumulative_total) {
			this.r22_cumulative_total = r22_cumulative_total;
		}

		// R23 Getters and Setters
		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_first_month() {
			return r23_first_month;
		}

		public void setR23_first_month(BigDecimal r23_first_month) {
			this.r23_first_month = r23_first_month;
		}

		public BigDecimal getR23_third_month() {
			return r23_third_month;
		}

		public void setR23_third_month(BigDecimal r23_third_month) {
			this.r23_third_month = r23_third_month;
		}

		public BigDecimal getR23_last_month() {
			return r23_last_month;
		}

		public void setR23_last_month(BigDecimal r23_last_month) {
			this.r23_last_month = r23_last_month;
		}

		public BigDecimal getR23_first_year() {
			return r23_first_year;
		}

		public void setR23_first_year(BigDecimal r23_first_year) {
			this.r23_first_year = r23_first_year;
		}

		public BigDecimal getR23_fifth_year() {
			return r23_fifth_year;
		}

		public void setR23_fifth_year(BigDecimal r23_fifth_year) {
			this.r23_fifth_year = r23_fifth_year;
		}

		public BigDecimal getR23_non_interest_bearing() {
			return r23_non_interest_bearing;
		}

		public void setR23_non_interest_bearing(BigDecimal r23_non_interest_bearing) {
			this.r23_non_interest_bearing = r23_non_interest_bearing;
		}

		public BigDecimal getR23_cumulative_total() {
			return r23_cumulative_total;
		}

		public void setR23_cumulative_total(BigDecimal r23_cumulative_total) {
			this.r23_cumulative_total = r23_cumulative_total;
		}

		// R24 Getters and Setters
		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_first_month() {
			return r24_first_month;
		}

		public void setR24_first_month(BigDecimal r24_first_month) {
			this.r24_first_month = r24_first_month;
		}

		public BigDecimal getR24_third_month() {
			return r24_third_month;
		}

		public void setR24_third_month(BigDecimal r24_third_month) {
			this.r24_third_month = r24_third_month;
		}

		public BigDecimal getR24_last_month() {
			return r24_last_month;
		}

		public void setR24_last_month(BigDecimal r24_last_month) {
			this.r24_last_month = r24_last_month;
		}

		public BigDecimal getR24_first_year() {
			return r24_first_year;
		}

		public void setR24_first_year(BigDecimal r24_first_year) {
			this.r24_first_year = r24_first_year;
		}

		public BigDecimal getR24_fifth_year() {
			return r24_fifth_year;
		}

		public void setR24_fifth_year(BigDecimal r24_fifth_year) {
			this.r24_fifth_year = r24_fifth_year;
		}

		public BigDecimal getR24_non_interest_bearing() {
			return r24_non_interest_bearing;
		}

		public void setR24_non_interest_bearing(BigDecimal r24_non_interest_bearing) {
			this.r24_non_interest_bearing = r24_non_interest_bearing;
		}

		public BigDecimal getR24_cumulative_total() {
			return r24_cumulative_total;
		}

		public void setR24_cumulative_total(BigDecimal r24_cumulative_total) {
			this.r24_cumulative_total = r24_cumulative_total;
		}

		// R25 Getters and Setters
		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_first_month() {
			return r25_first_month;
		}

		public void setR25_first_month(BigDecimal r25_first_month) {
			this.r25_first_month = r25_first_month;
		}

		public BigDecimal getR25_third_month() {
			return r25_third_month;
		}

		public void setR25_third_month(BigDecimal r25_third_month) {
			this.r25_third_month = r25_third_month;
		}

		public BigDecimal getR25_last_month() {
			return r25_last_month;
		}

		public void setR25_last_month(BigDecimal r25_last_month) {
			this.r25_last_month = r25_last_month;
		}

		public BigDecimal getR25_first_year() {
			return r25_first_year;
		}

		public void setR25_first_year(BigDecimal r25_first_year) {
			this.r25_first_year = r25_first_year;
		}

		public BigDecimal getR25_fifth_year() {
			return r25_fifth_year;
		}

		public void setR25_fifth_year(BigDecimal r25_fifth_year) {
			this.r25_fifth_year = r25_fifth_year;
		}

		public BigDecimal getR25_non_interest_bearing() {
			return r25_non_interest_bearing;
		}

		public void setR25_non_interest_bearing(BigDecimal r25_non_interest_bearing) {
			this.r25_non_interest_bearing = r25_non_interest_bearing;
		}

		public BigDecimal getR25_cumulative_total() {
			return r25_cumulative_total;
		}

		public void setR25_cumulative_total(BigDecimal r25_cumulative_total) {
			this.r25_cumulative_total = r25_cumulative_total;
		}

		// R26 Getters and Setters
		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_first_month() {
			return r26_first_month;
		}

		public void setR26_first_month(BigDecimal r26_first_month) {
			this.r26_first_month = r26_first_month;
		}

		public BigDecimal getR26_third_month() {
			return r26_third_month;
		}

		public void setR26_third_month(BigDecimal r26_third_month) {
			this.r26_third_month = r26_third_month;
		}

		public BigDecimal getR26_last_month() {
			return r26_last_month;
		}

		public void setR26_last_month(BigDecimal r26_last_month) {
			this.r26_last_month = r26_last_month;
		}

		public BigDecimal getR26_first_year() {
			return r26_first_year;
		}

		public void setR26_first_year(BigDecimal r26_first_year) {
			this.r26_first_year = r26_first_year;
		}

		public BigDecimal getR26_fifth_year() {
			return r26_fifth_year;
		}

		public void setR26_fifth_year(BigDecimal r26_fifth_year) {
			this.r26_fifth_year = r26_fifth_year;
		}

		public BigDecimal getR26_non_interest_bearing() {
			return r26_non_interest_bearing;
		}

		public void setR26_non_interest_bearing(BigDecimal r26_non_interest_bearing) {
			this.r26_non_interest_bearing = r26_non_interest_bearing;
		}

		public BigDecimal getR26_cumulative_total() {
			return r26_cumulative_total;
		}

		public void setR26_cumulative_total(BigDecimal r26_cumulative_total) {
			this.r26_cumulative_total = r26_cumulative_total;
		}

		// R27 Getters and Setters
		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_first_month() {
			return r27_first_month;
		}

		public void setR27_first_month(BigDecimal r27_first_month) {
			this.r27_first_month = r27_first_month;
		}

		public BigDecimal getR27_third_month() {
			return r27_third_month;
		}

		public void setR27_third_month(BigDecimal r27_third_month) {
			this.r27_third_month = r27_third_month;
		}

		public BigDecimal getR27_last_month() {
			return r27_last_month;
		}

		public void setR27_last_month(BigDecimal r27_last_month) {
			this.r27_last_month = r27_last_month;
		}

		public BigDecimal getR27_first_year() {
			return r27_first_year;
		}

		public void setR27_first_year(BigDecimal r27_first_year) {
			this.r27_first_year = r27_first_year;
		}

		public BigDecimal getR27_fifth_year() {
			return r27_fifth_year;
		}

		public void setR27_fifth_year(BigDecimal r27_fifth_year) {
			this.r27_fifth_year = r27_fifth_year;
		}

		public BigDecimal getR27_non_interest_bearing() {
			return r27_non_interest_bearing;
		}

		public void setR27_non_interest_bearing(BigDecimal r27_non_interest_bearing) {
			this.r27_non_interest_bearing = r27_non_interest_bearing;
		}

		public BigDecimal getR27_cumulative_total() {
			return r27_cumulative_total;
		}

		public void setR27_cumulative_total(BigDecimal r27_cumulative_total) {
			this.r27_cumulative_total = r27_cumulative_total;
		}

		// R28 Getters and Setters
		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_first_month() {
			return r28_first_month;
		}

		public void setR28_first_month(BigDecimal r28_first_month) {
			this.r28_first_month = r28_first_month;
		}

		public BigDecimal getR28_third_month() {
			return r28_third_month;
		}

		public void setR28_third_month(BigDecimal r28_third_month) {
			this.r28_third_month = r28_third_month;
		}

		public BigDecimal getR28_last_month() {
			return r28_last_month;
		}

		public void setR28_last_month(BigDecimal r28_last_month) {
			this.r28_last_month = r28_last_month;
		}

		public BigDecimal getR28_first_year() {
			return r28_first_year;
		}

		public void setR28_first_year(BigDecimal r28_first_year) {
			this.r28_first_year = r28_first_year;
		}

		public BigDecimal getR28_fifth_year() {
			return r28_fifth_year;
		}

		public void setR28_fifth_year(BigDecimal r28_fifth_year) {
			this.r28_fifth_year = r28_fifth_year;
		}

		public BigDecimal getR28_non_interest_bearing() {
			return r28_non_interest_bearing;
		}

		public void setR28_non_interest_bearing(BigDecimal r28_non_interest_bearing) {
			this.r28_non_interest_bearing = r28_non_interest_bearing;
		}

		public BigDecimal getR28_cumulative_total() {
			return r28_cumulative_total;
		}

		public void setR28_cumulative_total(BigDecimal r28_cumulative_total) {
			this.r28_cumulative_total = r28_cumulative_total;
		}

		// R29 Getters and Setters
		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_first_month() {
			return r29_first_month;
		}

		public void setR29_first_month(BigDecimal r29_first_month) {
			this.r29_first_month = r29_first_month;
		}

		public BigDecimal getR29_third_month() {
			return r29_third_month;
		}

		public void setR29_third_month(BigDecimal r29_third_month) {
			this.r29_third_month = r29_third_month;
		}

		public BigDecimal getR29_last_month() {
			return r29_last_month;
		}

		public void setR29_last_month(BigDecimal r29_last_month) {
			this.r29_last_month = r29_last_month;
		}

		public BigDecimal getR29_first_year() {
			return r29_first_year;
		}

		public void setR29_first_year(BigDecimal r29_first_year) {
			this.r29_first_year = r29_first_year;
		}

		public BigDecimal getR29_fifth_year() {
			return r29_fifth_year;
		}

		public void setR29_fifth_year(BigDecimal r29_fifth_year) {
			this.r29_fifth_year = r29_fifth_year;
		}

		public BigDecimal getR29_non_interest_bearing() {
			return r29_non_interest_bearing;
		}

		public void setR29_non_interest_bearing(BigDecimal r29_non_interest_bearing) {
			this.r29_non_interest_bearing = r29_non_interest_bearing;
		}

		public BigDecimal getR29_cumulative_total() {
			return r29_cumulative_total;
		}

		public void setR29_cumulative_total(BigDecimal r29_cumulative_total) {
			this.r29_cumulative_total = r29_cumulative_total;
		}

		// R30 Getters and Setters
		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_first_month() {
			return r30_first_month;
		}

		public void setR30_first_month(BigDecimal r30_first_month) {
			this.r30_first_month = r30_first_month;
		}

		public BigDecimal getR30_third_month() {
			return r30_third_month;
		}

		public void setR30_third_month(BigDecimal r30_third_month) {
			this.r30_third_month = r30_third_month;
		}

		public BigDecimal getR30_last_month() {
			return r30_last_month;
		}

		public void setR30_last_month(BigDecimal r30_last_month) {
			this.r30_last_month = r30_last_month;
		}

		public BigDecimal getR30_first_year() {
			return r30_first_year;
		}

		public void setR30_first_year(BigDecimal r30_first_year) {
			this.r30_first_year = r30_first_year;
		}

		public BigDecimal getR30_fifth_year() {
			return r30_fifth_year;
		}

		public void setR30_fifth_year(BigDecimal r30_fifth_year) {
			this.r30_fifth_year = r30_fifth_year;
		}

		public BigDecimal getR30_non_interest_bearing() {
			return r30_non_interest_bearing;
		}

		public void setR30_non_interest_bearing(BigDecimal r30_non_interest_bearing) {
			this.r30_non_interest_bearing = r30_non_interest_bearing;
		}

		public BigDecimal getR30_cumulative_total() {
			return r30_cumulative_total;
		}

		public void setR30_cumulative_total(BigDecimal r30_cumulative_total) {
			this.r30_cumulative_total = r30_cumulative_total;
		}

		// R31 Getters and Setters
		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_first_month() {
			return r31_first_month;
		}

		public void setR31_first_month(BigDecimal r31_first_month) {
			this.r31_first_month = r31_first_month;
		}

		public BigDecimal getR31_third_month() {
			return r31_third_month;
		}

		public void setR31_third_month(BigDecimal r31_third_month) {
			this.r31_third_month = r31_third_month;
		}

		public BigDecimal getR31_last_month() {
			return r31_last_month;
		}

		public void setR31_last_month(BigDecimal r31_last_month) {
			this.r31_last_month = r31_last_month;
		}

		public BigDecimal getR31_first_year() {
			return r31_first_year;
		}

		public void setR31_first_year(BigDecimal r31_first_year) {
			this.r31_first_year = r31_first_year;
		}

		public BigDecimal getR31_fifth_year() {
			return r31_fifth_year;
		}

		public void setR31_fifth_year(BigDecimal r31_fifth_year) {
			this.r31_fifth_year = r31_fifth_year;
		}

		public BigDecimal getR31_non_interest_bearing() {
			return r31_non_interest_bearing;
		}

		public void setR31_non_interest_bearing(BigDecimal r31_non_interest_bearing) {
			this.r31_non_interest_bearing = r31_non_interest_bearing;
		}

		public BigDecimal getR31_cumulative_total() {
			return r31_cumulative_total;
		}

		public void setR31_cumulative_total(BigDecimal r31_cumulative_total) {
			this.r31_cumulative_total = r31_cumulative_total;
		}

		// R32 Getters and Setters
		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_first_month() {
			return r32_first_month;
		}

		public void setR32_first_month(BigDecimal r32_first_month) {
			this.r32_first_month = r32_first_month;
		}

		public BigDecimal getR32_third_month() {
			return r32_third_month;
		}

		public void setR32_third_month(BigDecimal r32_third_month) {
			this.r32_third_month = r32_third_month;
		}

		public BigDecimal getR32_last_month() {
			return r32_last_month;
		}

		public void setR32_last_month(BigDecimal r32_last_month) {
			this.r32_last_month = r32_last_month;
		}

		public BigDecimal getR32_first_year() {
			return r32_first_year;
		}

		public void setR32_first_year(BigDecimal r32_first_year) {
			this.r32_first_year = r32_first_year;
		}

		public BigDecimal getR32_fifth_year() {
			return r32_fifth_year;
		}

		public void setR32_fifth_year(BigDecimal r32_fifth_year) {
			this.r32_fifth_year = r32_fifth_year;
		}

		public BigDecimal getR32_cumulative_total() {
			return r32_cumulative_total;
		}

		public void setR32_cumulative_total(BigDecimal r32_cumulative_total) {
			this.r32_cumulative_total = r32_cumulative_total;
		}

		// R33 Getters and Setters
		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_first_month() {
			return r33_first_month;
		}

		public void setR33_first_month(BigDecimal r33_first_month) {
			this.r33_first_month = r33_first_month;
		}

		public BigDecimal getR33_cumulative_total() {
			return r33_cumulative_total;
		}

		public void setR33_cumulative_total(BigDecimal r33_cumulative_total) {
			this.r33_cumulative_total = r33_cumulative_total;
		}

		// R34 Getters and Setters
		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_third_month() {
			return r34_third_month;
		}

		public void setR34_third_month(BigDecimal r34_third_month) {
			this.r34_third_month = r34_third_month;
		}

		public BigDecimal getR34_last_month() {
			return r34_last_month;
		}

		public void setR34_last_month(BigDecimal r34_last_month) {
			this.r34_last_month = r34_last_month;
		}

		public BigDecimal getR34_first_year() {
			return r34_first_year;
		}

		public void setR34_first_year(BigDecimal r34_first_year) {
			this.r34_first_year = r34_first_year;
		}

		public BigDecimal getR34_fifth_year() {
			return r34_fifth_year;
		}

		public void setR34_fifth_year(BigDecimal r34_fifth_year) {
			this.r34_fifth_year = r34_fifth_year;
		}

		public BigDecimal getR34_non_interest_bearing() {
			return r34_non_interest_bearing;
		}

		public void setR34_non_interest_bearing(BigDecimal r34_non_interest_bearing) {
			this.r34_non_interest_bearing = r34_non_interest_bearing;
		}

		public BigDecimal getR34_cumulative_total() {
			return r34_cumulative_total;
		}

		public void setR34_cumulative_total(BigDecimal r34_cumulative_total) {
			this.r34_cumulative_total = r34_cumulative_total;
		}

		// R35 Getters and Setters
		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_first_month() {
			return r35_first_month;
		}

		public void setR35_first_month(BigDecimal r35_first_month) {
			this.r35_first_month = r35_first_month;
		}

		public BigDecimal getR35_third_month() {
			return r35_third_month;
		}

		public void setR35_third_month(BigDecimal r35_third_month) {
			this.r35_third_month = r35_third_month;
		}

		public BigDecimal getR35_last_month() {
			return r35_last_month;
		}

		public void setR35_last_month(BigDecimal r35_last_month) {
			this.r35_last_month = r35_last_month;
		}

		public BigDecimal getR35_first_year() {
			return r35_first_year;
		}

		public void setR35_first_year(BigDecimal r35_first_year) {
			this.r35_first_year = r35_first_year;
		}

		public BigDecimal getR35_fifth_year() {
			return r35_fifth_year;
		}

		public void setR35_fifth_year(BigDecimal r35_fifth_year) {
			this.r35_fifth_year = r35_fifth_year;
		}

		public BigDecimal getR35_non_interest_bearing() {
			return r35_non_interest_bearing;
		}

		public void setR35_non_interest_bearing(BigDecimal r35_non_interest_bearing) {
			this.r35_non_interest_bearing = r35_non_interest_bearing;
		}

		public BigDecimal getR35_cumulative_total() {
			return r35_cumulative_total;
		}

		public void setR35_cumulative_total(BigDecimal r35_cumulative_total) {
			this.r35_cumulative_total = r35_cumulative_total;
		}

		// R36 Getters and Setters
		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_first_month() {
			return r36_first_month;
		}

		public void setR36_first_month(BigDecimal r36_first_month) {
			this.r36_first_month = r36_first_month;
		}

		public BigDecimal getR36_third_month() {
			return r36_third_month;
		}

		public void setR36_third_month(BigDecimal r36_third_month) {
			this.r36_third_month = r36_third_month;
		}

		public BigDecimal getR36_last_month() {
			return r36_last_month;
		}

		public void setR36_last_month(BigDecimal r36_last_month) {
			this.r36_last_month = r36_last_month;
		}

		public BigDecimal getR36_first_year() {
			return r36_first_year;
		}

		public void setR36_first_year(BigDecimal r36_first_year) {
			this.r36_first_year = r36_first_year;
		}

		public BigDecimal getR36_fifth_year() {
			return r36_fifth_year;
		}

		public void setR36_fifth_year(BigDecimal r36_fifth_year) {
			this.r36_fifth_year = r36_fifth_year;
		}

		public BigDecimal getR36_non_interest_bearing() {
			return r36_non_interest_bearing;
		}

		public void setR36_non_interest_bearing(BigDecimal r36_non_interest_bearing) {
			this.r36_non_interest_bearing = r36_non_interest_bearing;
		}

		public BigDecimal getR36_cumulative_total() {
			return r36_cumulative_total;
		}

		public void setR36_cumulative_total(BigDecimal r36_cumulative_total) {
			this.r36_cumulative_total = r36_cumulative_total;
		}

		// R37 Getters and Setters
		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_first_month() {
			return r37_first_month;
		}

		public void setR37_first_month(BigDecimal r37_first_month) {
			this.r37_first_month = r37_first_month;
		}

		public BigDecimal getR37_third_month() {
			return r37_third_month;
		}

		public void setR37_third_month(BigDecimal r37_third_month) {
			this.r37_third_month = r37_third_month;
		}

		public BigDecimal getR37_last_month() {
			return r37_last_month;
		}

		public void setR37_last_month(BigDecimal r37_last_month) {
			this.r37_last_month = r37_last_month;
		}

		public BigDecimal getR37_first_year() {
			return r37_first_year;
		}

		public void setR37_first_year(BigDecimal r37_first_year) {
			this.r37_first_year = r37_first_year;
		}

		public BigDecimal getR37_fifth_year() {
			return r37_fifth_year;
		}

		public void setR37_fifth_year(BigDecimal r37_fifth_year) {
			this.r37_fifth_year = r37_fifth_year;
		}

		public BigDecimal getR37_non_interest_bearing() {
			return r37_non_interest_bearing;
		}

		public void setR37_non_interest_bearing(BigDecimal r37_non_interest_bearing) {
			this.r37_non_interest_bearing = r37_non_interest_bearing;
		}

		public BigDecimal getR37_cumulative_total() {
			return r37_cumulative_total;
		}

		public void setR37_cumulative_total(BigDecimal r37_cumulative_total) {
			this.r37_cumulative_total = r37_cumulative_total;
		}

		// R38 Getters and Setters
		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_first_month() {
			return r38_first_month;
		}

		public void setR38_first_month(BigDecimal r38_first_month) {
			this.r38_first_month = r38_first_month;
		}

		public BigDecimal getR38_third_month() {
			return r38_third_month;
		}

		public void setR38_third_month(BigDecimal r38_third_month) {
			this.r38_third_month = r38_third_month;
		}

		public BigDecimal getR38_last_month() {
			return r38_last_month;
		}

		public void setR38_last_month(BigDecimal r38_last_month) {
			this.r38_last_month = r38_last_month;
		}

		public BigDecimal getR38_first_year() {
			return r38_first_year;
		}

		public void setR38_first_year(BigDecimal r38_first_year) {
			this.r38_first_year = r38_first_year;
		}

		public BigDecimal getR38_fifth_year() {
			return r38_fifth_year;
		}

		public void setR38_fifth_year(BigDecimal r38_fifth_year) {
			this.r38_fifth_year = r38_fifth_year;
		}

		public BigDecimal getR38_non_interest_bearing() {
			return r38_non_interest_bearing;
		}

		public void setR38_non_interest_bearing(BigDecimal r38_non_interest_bearing) {
			this.r38_non_interest_bearing = r38_non_interest_bearing;
		}

		public BigDecimal getR38_cumulative_total() {
			return r38_cumulative_total;
		}

		public void setR38_cumulative_total(BigDecimal r38_cumulative_total) {
			this.r38_cumulative_total = r38_cumulative_total;
		}

		// R39 Getters and Setters
		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_first_month() {
			return r39_first_month;
		}

		public void setR39_first_month(BigDecimal r39_first_month) {
			this.r39_first_month = r39_first_month;
		}

		public BigDecimal getR39_third_month() {
			return r39_third_month;
		}

		public void setR39_third_month(BigDecimal r39_third_month) {
			this.r39_third_month = r39_third_month;
		}

		public BigDecimal getR39_last_month() {
			return r39_last_month;
		}

		public void setR39_last_month(BigDecimal r39_last_month) {
			this.r39_last_month = r39_last_month;
		}

		public BigDecimal getR39_first_year() {
			return r39_first_year;
		}

		public void setR39_first_year(BigDecimal r39_first_year) {
			this.r39_first_year = r39_first_year;
		}

		public BigDecimal getR39_fifth_year() {
			return r39_fifth_year;
		}

		public void setR39_fifth_year(BigDecimal r39_fifth_year) {
			this.r39_fifth_year = r39_fifth_year;
		}

		public BigDecimal getR39_non_interest_bearing() {
			return r39_non_interest_bearing;
		}

		public void setR39_non_interest_bearing(BigDecimal r39_non_interest_bearing) {
			this.r39_non_interest_bearing = r39_non_interest_bearing;
		}

		public BigDecimal getR39_cumulative_total() {
			return r39_cumulative_total;
		}

		public void setR39_cumulative_total(BigDecimal r39_cumulative_total) {
			this.r39_cumulative_total = r39_cumulative_total;
		}

		// R40 Getters and Setters
		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_first_month() {
			return r40_first_month;
		}

		public void setR40_first_month(BigDecimal r40_first_month) {
			this.r40_first_month = r40_first_month;
		}

		public BigDecimal getR40_third_month() {
			return r40_third_month;
		}

		public void setR40_third_month(BigDecimal r40_third_month) {
			this.r40_third_month = r40_third_month;
		}

		public BigDecimal getR40_last_month() {
			return r40_last_month;
		}

		public void setR40_last_month(BigDecimal r40_last_month) {
			this.r40_last_month = r40_last_month;
		}

		public BigDecimal getR40_first_year() {
			return r40_first_year;
		}

		public void setR40_first_year(BigDecimal r40_first_year) {
			this.r40_first_year = r40_first_year;
		}

		public BigDecimal getR40_fifth_year() {
			return r40_fifth_year;
		}

		public void setR40_fifth_year(BigDecimal r40_fifth_year) {
			this.r40_fifth_year = r40_fifth_year;
		}

		public BigDecimal getR40_non_interest_bearing() {
			return r40_non_interest_bearing;
		}

		public void setR40_non_interest_bearing(BigDecimal r40_non_interest_bearing) {
			this.r40_non_interest_bearing = r40_non_interest_bearing;
		}

		public BigDecimal getR40_cumulative_total() {
			return r40_cumulative_total;
		}

		public void setR40_cumulative_total(BigDecimal r40_cumulative_total) {
			this.r40_cumulative_total = r40_cumulative_total;
		}

		// R41 Getters and Setters
		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_first_month() {
			return r41_first_month;
		}

		public void setR41_first_month(BigDecimal r41_first_month) {
			this.r41_first_month = r41_first_month;
		}

		public BigDecimal getR41_third_month() {
			return r41_third_month;
		}

		public void setR41_third_month(BigDecimal r41_third_month) {
			this.r41_third_month = r41_third_month;
		}

		public BigDecimal getR41_last_month() {
			return r41_last_month;
		}

		public void setR41_last_month(BigDecimal r41_last_month) {
			this.r41_last_month = r41_last_month;
		}

		public BigDecimal getR41_first_year() {
			return r41_first_year;
		}

		public void setR41_first_year(BigDecimal r41_first_year) {
			this.r41_first_year = r41_first_year;
		}

		public BigDecimal getR41_fifth_year() {
			return r41_fifth_year;
		}

		public void setR41_fifth_year(BigDecimal r41_fifth_year) {
			this.r41_fifth_year = r41_fifth_year;
		}

		public BigDecimal getR41_non_interest_bearing() {
			return r41_non_interest_bearing;
		}

		public void setR41_non_interest_bearing(BigDecimal r41_non_interest_bearing) {
			this.r41_non_interest_bearing = r41_non_interest_bearing;
		}

		public BigDecimal getR41_cumulative_total() {
			return r41_cumulative_total;
		}

		public void setR41_cumulative_total(BigDecimal r41_cumulative_total) {
			this.r41_cumulative_total = r41_cumulative_total;
		}

		// R42 Getters and Setters
		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_first_month() {
			return r42_first_month;
		}

		public void setR42_first_month(BigDecimal r42_first_month) {
			this.r42_first_month = r42_first_month;
		}

		public BigDecimal getR42_third_month() {
			return r42_third_month;
		}

		public void setR42_third_month(BigDecimal r42_third_month) {
			this.r42_third_month = r42_third_month;
		}

		public BigDecimal getR42_last_month() {
			return r42_last_month;
		}

		public void setR42_last_month(BigDecimal r42_last_month) {
			this.r42_last_month = r42_last_month;
		}

		public BigDecimal getR42_first_year() {
			return r42_first_year;
		}

		public void setR42_first_year(BigDecimal r42_first_year) {
			this.r42_first_year = r42_first_year;
		}

		public BigDecimal getR42_fifth_year() {
			return r42_fifth_year;
		}

		public void setR42_fifth_year(BigDecimal r42_fifth_year) {
			this.r42_fifth_year = r42_fifth_year;
		}

		public BigDecimal getR42_non_interest_bearing() {
			return r42_non_interest_bearing;
		}

		public void setR42_non_interest_bearing(BigDecimal r42_non_interest_bearing) {
			this.r42_non_interest_bearing = r42_non_interest_bearing;
		}

		public BigDecimal getR42_cumulative_total() {
			return r42_cumulative_total;
		}

		public void setR42_cumulative_total(BigDecimal r42_cumulative_total) {
			this.r42_cumulative_total = r42_cumulative_total;
		}

		// R43 Getters and Setters
		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_first_month() {
			return r43_first_month;
		}

		public void setR43_first_month(BigDecimal r43_first_month) {
			this.r43_first_month = r43_first_month;
		}

		public BigDecimal getR43_third_month() {
			return r43_third_month;
		}

		public void setR43_third_month(BigDecimal r43_third_month) {
			this.r43_third_month = r43_third_month;
		}

		public BigDecimal getR43_last_month() {
			return r43_last_month;
		}

		public void setR43_last_month(BigDecimal r43_last_month) {
			this.r43_last_month = r43_last_month;
		}

		public BigDecimal getR43_first_year() {
			return r43_first_year;
		}

		public void setR43_first_year(BigDecimal r43_first_year) {
			this.r43_first_year = r43_first_year;
		}

		public BigDecimal getR43_fifth_year() {
			return r43_fifth_year;
		}

		public void setR43_fifth_year(BigDecimal r43_fifth_year) {
			this.r43_fifth_year = r43_fifth_year;
		}

		public BigDecimal getR43_non_interest_bearing() {
			return r43_non_interest_bearing;
		}

		public void setR43_non_interest_bearing(BigDecimal r43_non_interest_bearing) {
			this.r43_non_interest_bearing = r43_non_interest_bearing;
		}

		public BigDecimal getR43_cumulative_total() {
			return r43_cumulative_total;
		}

		public void setR43_cumulative_total(BigDecimal r43_cumulative_total) {
			this.r43_cumulative_total = r43_cumulative_total;
		}

		// R44 Getters and Setters
		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_first_month() {
			return r44_first_month;
		}

		public void setR44_first_month(BigDecimal r44_first_month) {
			this.r44_first_month = r44_first_month;
		}

		public BigDecimal getR44_third_month() {
			return r44_third_month;
		}

		public void setR44_third_month(BigDecimal r44_third_month) {
			this.r44_third_month = r44_third_month;
		}

		public BigDecimal getR44_last_month() {
			return r44_last_month;
		}

		public void setR44_last_month(BigDecimal r44_last_month) {
			this.r44_last_month = r44_last_month;
		}

		public BigDecimal getR44_first_year() {
			return r44_first_year;
		}

		public void setR44_first_year(BigDecimal r44_first_year) {
			this.r44_first_year = r44_first_year;
		}

		public BigDecimal getR44_fifth_year() {
			return r44_fifth_year;
		}

		public void setR44_fifth_year(BigDecimal r44_fifth_year) {
			this.r44_fifth_year = r44_fifth_year;
		}

		public BigDecimal getR44_non_interest_bearing() {
			return r44_non_interest_bearing;
		}

		public void setR44_non_interest_bearing(BigDecimal r44_non_interest_bearing) {
			this.r44_non_interest_bearing = r44_non_interest_bearing;
		}

		public BigDecimal getR44_cumulative_total() {
			return r44_cumulative_total;
		}

		public void setR44_cumulative_total(BigDecimal r44_cumulative_total) {
			this.r44_cumulative_total = r44_cumulative_total;
		}

		// R45 Getters and Setters
		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_first_month() {
			return r45_first_month;
		}

		public void setR45_first_month(BigDecimal r45_first_month) {
			this.r45_first_month = r45_first_month;
		}

		public BigDecimal getR45_third_month() {
			return r45_third_month;
		}

		public void setR45_third_month(BigDecimal r45_third_month) {
			this.r45_third_month = r45_third_month;
		}

		public BigDecimal getR45_last_month() {
			return r45_last_month;
		}

		public void setR45_last_month(BigDecimal r45_last_month) {
			this.r45_last_month = r45_last_month;
		}

		public BigDecimal getR45_first_year() {
			return r45_first_year;
		}

		public void setR45_first_year(BigDecimal r45_first_year) {
			this.r45_first_year = r45_first_year;
		}

		public BigDecimal getR45_fifth_year() {
			return r45_fifth_year;
		}

		public void setR45_fifth_year(BigDecimal r45_fifth_year) {
			this.r45_fifth_year = r45_fifth_year;
		}

		public BigDecimal getR45_non_interest_bearing() {
			return r45_non_interest_bearing;
		}

		public void setR45_non_interest_bearing(BigDecimal r45_non_interest_bearing) {
			this.r45_non_interest_bearing = r45_non_interest_bearing;
		}

		public BigDecimal getR45_cumulative_total() {
			return r45_cumulative_total;
		}

		public void setR45_cumulative_total(BigDecimal r45_cumulative_total) {
			this.r45_cumulative_total = r45_cumulative_total;
		}

		// R46 Getters and Setters
		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_first_month() {
			return r46_first_month;
		}

		public void setR46_first_month(BigDecimal r46_first_month) {
			this.r46_first_month = r46_first_month;
		}

		public BigDecimal getR46_third_month() {
			return r46_third_month;
		}

		public void setR46_third_month(BigDecimal r46_third_month) {
			this.r46_third_month = r46_third_month;
		}

		public BigDecimal getR46_last_month() {
			return r46_last_month;
		}

		public void setR46_last_month(BigDecimal r46_last_month) {
			this.r46_last_month = r46_last_month;
		}

		public BigDecimal getR46_first_year() {
			return r46_first_year;
		}

		public void setR46_first_year(BigDecimal r46_first_year) {
			this.r46_first_year = r46_first_year;
		}

		public BigDecimal getR46_fifth_year() {
			return r46_fifth_year;
		}

		public void setR46_fifth_year(BigDecimal r46_fifth_year) {
			this.r46_fifth_year = r46_fifth_year;
		}

		public BigDecimal getR46_non_interest_bearing() {
			return r46_non_interest_bearing;
		}

		public void setR46_non_interest_bearing(BigDecimal r46_non_interest_bearing) {
			this.r46_non_interest_bearing = r46_non_interest_bearing;
		}

		public BigDecimal getR46_cumulative_total() {
			return r46_cumulative_total;
		}

		public void setR46_cumulative_total(BigDecimal r46_cumulative_total) {
			this.r46_cumulative_total = r46_cumulative_total;
		}

		// R47 Getters and Setters
		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_first_month() {
			return r47_first_month;
		}

		public void setR47_first_month(BigDecimal r47_first_month) {
			this.r47_first_month = r47_first_month;
		}

		public BigDecimal getR47_third_month() {
			return r47_third_month;
		}

		public void setR47_third_month(BigDecimal r47_third_month) {
			this.r47_third_month = r47_third_month;
		}

		public BigDecimal getR47_last_month() {
			return r47_last_month;
		}

		public void setR47_last_month(BigDecimal r47_last_month) {
			this.r47_last_month = r47_last_month;
		}

		public BigDecimal getR47_first_year() {
			return r47_first_year;
		}

		public void setR47_first_year(BigDecimal r47_first_year) {
			this.r47_first_year = r47_first_year;
		}

		public BigDecimal getR47_fifth_year() {
			return r47_fifth_year;
		}

		public void setR47_fifth_year(BigDecimal r47_fifth_year) {
			this.r47_fifth_year = r47_fifth_year;
		}

		public BigDecimal getR47_non_interest_bearing() {
			return r47_non_interest_bearing;
		}

		public void setR47_non_interest_bearing(BigDecimal r47_non_interest_bearing) {
			this.r47_non_interest_bearing = r47_non_interest_bearing;
		}

		public BigDecimal getR47_cumulative_total() {
			return r47_cumulative_total;
		}

		public void setR47_cumulative_total(BigDecimal r47_cumulative_total) {
			this.r47_cumulative_total = r47_cumulative_total;
		}

		// R48 Getters and Setters
		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_first_month() {
			return r48_first_month;
		}

		public void setR48_first_month(BigDecimal r48_first_month) {
			this.r48_first_month = r48_first_month;
		}

		public BigDecimal getR48_third_month() {
			return r48_third_month;
		}

		public void setR48_third_month(BigDecimal r48_third_month) {
			this.r48_third_month = r48_third_month;
		}

		public BigDecimal getR48_last_month() {
			return r48_last_month;
		}

		public void setR48_last_month(BigDecimal r48_last_month) {
			this.r48_last_month = r48_last_month;
		}

		public BigDecimal getR48_first_year() {
			return r48_first_year;
		}

		public void setR48_first_year(BigDecimal r48_first_year) {
			this.r48_first_year = r48_first_year;
		}

		public BigDecimal getR48_fifth_year() {
			return r48_fifth_year;
		}

		public void setR48_fifth_year(BigDecimal r48_fifth_year) {
			this.r48_fifth_year = r48_fifth_year;
		}

		public BigDecimal getR48_non_interest_bearing() {
			return r48_non_interest_bearing;
		}

		public void setR48_non_interest_bearing(BigDecimal r48_non_interest_bearing) {
			this.r48_non_interest_bearing = r48_non_interest_bearing;
		}

		public BigDecimal getR48_cumulative_total() {
			return r48_cumulative_total;
		}

		public void setR48_cumulative_total(BigDecimal r48_cumulative_total) {
			this.r48_cumulative_total = r48_cumulative_total;
		}

		// R49 Getters and Setters
		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_first_month() {
			return r49_first_month;
		}

		public void setR49_first_month(BigDecimal r49_first_month) {
			this.r49_first_month = r49_first_month;
		}

		public BigDecimal getR49_third_month() {
			return r49_third_month;
		}

		public void setR49_third_month(BigDecimal r49_third_month) {
			this.r49_third_month = r49_third_month;
		}

		public BigDecimal getR49_last_month() {
			return r49_last_month;
		}

		public void setR49_last_month(BigDecimal r49_last_month) {
			this.r49_last_month = r49_last_month;
		}

		public BigDecimal getR49_first_year() {
			return r49_first_year;
		}

		public void setR49_first_year(BigDecimal r49_first_year) {
			this.r49_first_year = r49_first_year;
		}

		public BigDecimal getR49_fifth_year() {
			return r49_fifth_year;
		}

		public void setR49_fifth_year(BigDecimal r49_fifth_year) {
			this.r49_fifth_year = r49_fifth_year;
		}

		public BigDecimal getR49_non_interest_bearing() {
			return r49_non_interest_bearing;
		}

		public void setR49_non_interest_bearing(BigDecimal r49_non_interest_bearing) {
			this.r49_non_interest_bearing = r49_non_interest_bearing;
		}

		public BigDecimal getR49_cumulative_total() {
			return r49_cumulative_total;
		}

		public void setR49_cumulative_total(BigDecimal r49_cumulative_total) {
			this.r49_cumulative_total = r49_cumulative_total;
		}

		// R50 Getters and Setters
		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_first_month() {
			return r50_first_month;
		}

		public void setR50_first_month(BigDecimal r50_first_month) {
			this.r50_first_month = r50_first_month;
		}

		public BigDecimal getR50_third_month() {
			return r50_third_month;
		}

		public void setR50_third_month(BigDecimal r50_third_month) {
			this.r50_third_month = r50_third_month;
		}

		public BigDecimal getR50_last_month() {
			return r50_last_month;
		}

		public void setR50_last_month(BigDecimal r50_last_month) {
			this.r50_last_month = r50_last_month;
		}

		public BigDecimal getR50_first_year() {
			return r50_first_year;
		}

		public void setR50_first_year(BigDecimal r50_first_year) {
			this.r50_first_year = r50_first_year;
		}

		public BigDecimal getR50_fifth_year() {
			return r50_fifth_year;
		}

		public void setR50_fifth_year(BigDecimal r50_fifth_year) {
			this.r50_fifth_year = r50_fifth_year;
		}

		public BigDecimal getR50_non_interest_bearing() {
			return r50_non_interest_bearing;
		}

		public void setR50_non_interest_bearing(BigDecimal r50_non_interest_bearing) {
			this.r50_non_interest_bearing = r50_non_interest_bearing;
		}

		public BigDecimal getR50_cumulative_total() {
			return r50_cumulative_total;
		}

		public void setR50_cumulative_total(BigDecimal r50_cumulative_total) {
			this.r50_cumulative_total = r50_cumulative_total;
		}

		// Common Fields Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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
	}

	// ===========================
	// M_LIQGAP_Archival_Detail_RowMapper
	// ===========================

	public class M_LIQGAP_Archival_Detail_RowMapper implements RowMapper<M_LIQGAP_Archival_Detail_Entity> {

		@Override
		public M_LIQGAP_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Archival_Detail_Entity obj = new M_LIQGAP_Archival_Detail_Entity();

			// =========================
			// ENTITY FIELDS
			// =========================
			obj.setCustId(rs.getString("cust_id"));
			obj.setAcctNumber(rs.getString("acct_number"));
			obj.setAcctName(rs.getString("acct_name"));
			obj.setDataType(rs.getString("data_type"));
			obj.setReportAddlCriteria1(rs.getString("report_addl_criteria_1"));
			obj.setReportLabel(rs.getString("report_label"));
			obj.setReportRemarks(rs.getString("report_remarks"));
			obj.setModificationRemarks(rs.getString("modification_remarks"));
			obj.setDataEntryVersion(rs.getString("data_entry_version"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("acct_balance_in_pula"));

			obj.setReportName(rs.getString("report_name"));
			obj.setCreateUser(rs.getString("create_user"));
			obj.setCreateTime(rs.getDate("create_time"));
			obj.setModifyUser(rs.getString("modify_user"));
			obj.setModifyTime(rs.getDate("modify_time"));
			obj.setVerifyUser(rs.getString("verify_user"));
			obj.setVerifyTime(rs.getDate("verify_time"));

			obj.setReportDate(rs.getDate("report_date"));
			obj.setEntityFlg(rs.getString("entity_flg"));
			obj.setModifyFlg(rs.getString("modify_flg"));
			obj.setDelFlg(rs.getString("del_flg"));

			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Archival_Detail_Entity
	// ===========================

	public class M_LIQGAP_Archival_Detail_Entity {

		private String custId;
		private String acctNumber;
		private String acctName;
		private String dataType;
		private String reportAddlCriteria1;
		private String reportLabel;
		private String reportRemarks;
		private String modificationRemarks;
		private String dataEntryVersion;
		private BigDecimal acctBalanceInpula;

		private String reportName;
		private String createUser;
		private Date createTime;
		private String modifyUser;
		private Date modifyTime;
		private String verifyUser;
		private Date verifyTime;

		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		private String entityFlg;

		private String modifyFlg;

		private String delFlg;

		// Default constructor
		public M_LIQGAP_Archival_Detail_Entity() {
			super();
		}

		// Getters and Setters
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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(String entityFlg) {
			this.entityFlg = entityFlg;
		}

		public String getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(String modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public String getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(String delFlg) {
			this.delFlg = delFlg;
		}
	}

	// ===========================
	// M_LIQGAP_Manual_Summary_RowMapper
	// ===========================

	public class M_LIQGAP_Manual_Summary_RowMapper implements RowMapper<M_LIQGAP_Manual_Summary_Entity> {

		@Override
		public M_LIQGAP_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Manual_Summary_Entity obj = new M_LIQGAP_Manual_Summary_Entity();

			// =========================
			// ENTITY FIELDS
			// =========================
			obj.setR21_non_interest_bearing(rs.getBigDecimal("R21_NON_INTEREST_BEARING"));
			obj.setR32_non_interest_bearing(rs.getBigDecimal("R32_NON_INTEREST_BEARING"));
			obj.setR33_third_month(rs.getBigDecimal("R33_THIRD_MONTH"));
			obj.setR33_last_month(rs.getBigDecimal("R33_LAST_MONTH"));
			obj.setR33_first_year(rs.getBigDecimal("R33_FIRST_YEAR"));
			obj.setR33_fifth_year(rs.getBigDecimal("R33_FIFTH_YEAR"));
			obj.setR33_non_interest_bearing(rs.getBigDecimal("R33_NON_INTEREST_BEARING"));
			obj.setR34_first_month(rs.getBigDecimal("R34_FIRST_MONTH"));

			// =========================
			// COMMON FIELDS
			// =========================

			obj.setReportDate(rs.getDate("report_date"));
			obj.setReportVersion(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Manual_Summary_Entity
	// ===========================

	public class M_LIQGAP_Manual_Summary_Entity {

		// COMMON FIELDS

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date reportDate;

		private BigDecimal reportVersion;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private BigDecimal r21_non_interest_bearing;
		private BigDecimal r32_non_interest_bearing;
		private BigDecimal r33_third_month;
		private BigDecimal r33_last_month;
		private BigDecimal r33_first_year;
		private BigDecimal r33_fifth_year;
		private BigDecimal r33_non_interest_bearing;
		private BigDecimal r34_first_month;

		// Default constructor
		public M_LIQGAP_Manual_Summary_Entity() {
			super();
		}

		// Getters and Setters

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

		public BigDecimal getR21_non_interest_bearing() {
			return r21_non_interest_bearing;
		}

		public void setR21_non_interest_bearing(BigDecimal r21_non_interest_bearing) {
			this.r21_non_interest_bearing = r21_non_interest_bearing;
		}

		public BigDecimal getR32_non_interest_bearing() {
			return r32_non_interest_bearing;
		}

		public void setR32_non_interest_bearing(BigDecimal r32_non_interest_bearing) {
			this.r32_non_interest_bearing = r32_non_interest_bearing;
		}

		public BigDecimal getR33_third_month() {
			return r33_third_month;
		}

		public void setR33_third_month(BigDecimal r33_third_month) {
			this.r33_third_month = r33_third_month;
		}

		public BigDecimal getR33_last_month() {
			return r33_last_month;
		}

		public void setR33_last_month(BigDecimal r33_last_month) {
			this.r33_last_month = r33_last_month;
		}

		public BigDecimal getR33_first_year() {
			return r33_first_year;
		}

		public void setR33_first_year(BigDecimal r33_first_year) {
			this.r33_first_year = r33_first_year;
		}

		public BigDecimal getR33_fifth_year() {
			return r33_fifth_year;
		}

		public void setR33_fifth_year(BigDecimal r33_fifth_year) {
			this.r33_fifth_year = r33_fifth_year;
		}

		public BigDecimal getR33_non_interest_bearing() {
			return r33_non_interest_bearing;
		}

		public void setR33_non_interest_bearing(BigDecimal r33_non_interest_bearing) {
			this.r33_non_interest_bearing = r33_non_interest_bearing;
		}

		public BigDecimal getR34_first_month() {
			return r34_first_month;
		}

		public void setR34_first_month(BigDecimal r34_first_month) {
			this.r34_first_month = r34_first_month;
		}
	}

	// ===========================
	// M_LIQGAP_Manual_Archival_Summary_RowMapper
	// ===========================

	public class M_LIQGAP_Manual_Archival_Summary_RowMapper
			implements RowMapper<M_LIQGAP_Manual_Archival_Summary_Entity> {

		@Override
		public M_LIQGAP_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LIQGAP_Manual_Archival_Summary_Entity obj = new M_LIQGAP_Manual_Archival_Summary_Entity();

			// =========================
			// ENTITY FIELDS
			// =========================
			obj.setR21_non_interest_bearing(rs.getBigDecimal("R21_NON_INTEREST_BEARING"));
			obj.setR32_non_interest_bearing(rs.getBigDecimal("R32_NON_INTEREST_BEARING"));
			obj.setR33_third_month(rs.getBigDecimal("R33_THIRD_MONTH"));
			obj.setR33_last_month(rs.getBigDecimal("R33_LAST_MONTH"));
			obj.setR33_first_year(rs.getBigDecimal("R33_FIRST_YEAR"));
			obj.setR33_fifth_year(rs.getBigDecimal("R33_FIFTH_YEAR"));
			obj.setR33_non_interest_bearing(rs.getBigDecimal("R33_NON_INTEREST_BEARING"));
			obj.setR34_first_month(rs.getBigDecimal("R34_FIRST_MONTH"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReportDate(rs.getDate("report_date"));
			obj.setReportVersion(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));
			return obj;
		}
	}

	// ===========================
	// M_LIQGAP_Manual_Archival_Summary_Entity
	// ===========================

	public class M_LIQGAP_Manual_Archival_Summary_Entity {

		// COMMON FIELDS
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date reportDate;

		@Id
		private BigDecimal reportVersion;

		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		private BigDecimal r21_non_interest_bearing;
		private BigDecimal r32_non_interest_bearing;
		private BigDecimal r33_third_month;
		private BigDecimal r33_last_month;
		private BigDecimal r33_first_year;
		private BigDecimal r33_fifth_year;
		private BigDecimal r33_non_interest_bearing;
		private BigDecimal r34_first_month;

		// Default constructor
		public M_LIQGAP_Manual_Archival_Summary_Entity() {
			super();
		}

		// Getters and Setters
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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

		public BigDecimal getR21_non_interest_bearing() {
			return r21_non_interest_bearing;
		}

		public void setR21_non_interest_bearing(BigDecimal r21_non_interest_bearing) {
			this.r21_non_interest_bearing = r21_non_interest_bearing;
		}

		public BigDecimal getR32_non_interest_bearing() {
			return r32_non_interest_bearing;
		}

		public void setR32_non_interest_bearing(BigDecimal r32_non_interest_bearing) {
			this.r32_non_interest_bearing = r32_non_interest_bearing;
		}

		public BigDecimal getR33_third_month() {
			return r33_third_month;
		}

		public void setR33_third_month(BigDecimal r33_third_month) {
			this.r33_third_month = r33_third_month;
		}

		public BigDecimal getR33_last_month() {
			return r33_last_month;
		}

		public void setR33_last_month(BigDecimal r33_last_month) {
			this.r33_last_month = r33_last_month;
		}

		public BigDecimal getR33_first_year() {
			return r33_first_year;
		}

		public void setR33_first_year(BigDecimal r33_first_year) {
			this.r33_first_year = r33_first_year;
		}

		public BigDecimal getR33_fifth_year() {
			return r33_fifth_year;
		}

		public void setR33_fifth_year(BigDecimal r33_fifth_year) {
			this.r33_fifth_year = r33_fifth_year;
		}

		public BigDecimal getR33_non_interest_bearing() {
			return r33_non_interest_bearing;
		}

		public void setR33_non_interest_bearing(BigDecimal r33_non_interest_bearing) {
			this.r33_non_interest_bearing = r33_non_interest_bearing;
		}

		public BigDecimal getR34_first_month() {
			return r34_first_month;
		}

		public void setR34_first_month(BigDecimal r34_first_month) {
			this.r34_first_month = r34_first_month;
		}
	}

//=================
// MODEL AND VIEW
//=================

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	// ===========================
	// M_LIQGAP_VIEW
	// ===========================

	public ModelAndView getM_LIQGAPView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (type.equals("ARCHIVAL") && version != null) {
			List<M_LIQGAP_Archival_Summary_Entity> T1Master = new ArrayList<M_LIQGAP_Archival_Summary_Entity>();
			List<M_LIQGAP_Manual_Archival_Summary_Entity> T2Master = new ArrayList<M_LIQGAP_Manual_Archival_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// ✅ USING JDBC METHOD - getArchivalSummaryDataByDateAndVersion
				T1Master = getArchivalSummaryDataByDateAndVersion(d1, version);
				T2Master = getManualArchivalSummaryDataByDateAndVersion(d1, version);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);

		} else {

			List<M_LIQGAP_Summary_Entity> T1Master = new ArrayList<M_LIQGAP_Summary_Entity>();
			List<M_LIQGAP_Manual_Summary_Entity> T2Master = new ArrayList<M_LIQGAP_Manual_Summary_Entity>();
			try {
				Date d1 = dateformat.parse(todate);
				// ✅ USING JDBC METHOD - getSummaryDataByDate
				T1Master = getSummaryDataByDate(d1);
				T2Master = getManualSummaryDataByDate(d1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		mv.setViewName("BRRS/M_LIQGAP");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());

		return mv;
	}

	public ModelAndView getM_LIQGAPcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String Filter, String type, String version, HttpServletRequest req1,
			Model md) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String rowId = null;
			String columnId = null;

			// ✅ Split filter string into rowId & columnId
			if (Filter != null && Filter.contains(",")) {
				String[] parts = Filter.split(",");
				if (parts.length >= 2) {
					rowId = parts[0];
					columnId = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				// 🔹 Archival branch - USING JDBC METHODS
				List<M_LIQGAP_Archival_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					// ✅ JDBC method - getArchivalDetailDataByRowIdAndColumnId
					T1Dt1 = getArchivalDetailDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
				} else {
					// ✅ JDBC method - getArchivalDetailDataByDateAndVersion
					T1Dt1 = getArchivalDetailDataByDateAndVersion(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch - USING JDBC METHODS
				List<M_LIQGAP_Detail_Entity> T1Dt1;
				if (rowId != null && columnId != null) {
					// ✅ JDBC method - getDetailDataByRowIdAndColumnId
					T1Dt1 = getDetailDataByRowIdAndColumnId(rowId, columnId, parsedDate);
				} else {
					// ✅ JDBC method - getDetailDataByDate
					T1Dt1 = getDetailDataByDate(parsedDate);
					// ✅ JDBC method - getDetailDataCount
					totalPages = getDetailDataCount(parsedDate);
					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_LIQGAP");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}

	@Transactional
	public void updateReport(M_LIQGAP_Manual_Summary_Entity request) {

		System.out.println("Entered updateReport service");
		System.out.println("Report Date: " + request.getReportDate());

		// ✅ JDBC method - getManualSummaryDataByDate
		List<M_LIQGAP_Manual_Summary_Entity> list = getManualSummaryDataByDate(request.getReportDate());

		if (list.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + request.getReportDate());
		}

		M_LIQGAP_Manual_Summary_Entity existing = list.get(0);

		// Audit old copy
		M_LIQGAP_Manual_Summary_Entity oldcopy = new M_LIQGAP_Manual_Summary_Entity();
		BeanUtils.copyProperties(existing, oldcopy);

		try {

			int[] rows = { 21, 32, 33, 34 };

			for (int row : rows) {

				String prefix = "r" + row + "_";

				String[] fields;

				if (row == 21 || row == 32) {

					fields = new String[] { "non_interest_bearing" };

				} else if (row == 33) {

					fields = new String[] { "non_interest_bearing", "third_month", "last_month", "first_year",
							"fifth_year" };

				} else if (row == 34) {

					fields = new String[] { "first_month" };

				} else {
					continue;
				}

				for (String field : fields) {

					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {

						Method getter = M_LIQGAP_Manual_Summary_Entity.class.getMethod(getterName);

						Method setter = M_LIQGAP_Manual_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(request);

						System.out.println("Updating: " + prefix + field + " = " + newValue);

						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {

						System.out.println("Field NOT FOUND: " + prefix + field);
					}
				}
			}

		} catch (Exception e) {

			throw new RuntimeException("Error while updating LIQGAP report fields", e);
		}

		// ✅ Audit & Save only if changes exist
		String changes = auditService.getChanges(oldcopy, existing);

		if (!changes.isEmpty()) {

			// ✅ UPDATE using JDBC - Direct SQL update
			String sql = "UPDATE BRRS_M_LIQGAP_MANUAL_SUMMARYTABLE SET " + "R21_NON_INTEREST_BEARING = ?, "
					+ "R32_NON_INTEREST_BEARING = ?, " + "R33_THIRD_MONTH = ?, " + "R33_LAST_MONTH = ?, "
					+ "R33_FIRST_YEAR = ?, " + "R33_FIFTH_YEAR = ?, " + "R33_NON_INTEREST_BEARING = ?, "
					+ "R34_FIRST_MONTH = ? " + "WHERE REPORT_DATE = ?";

			jdbcTemplate.update(sql, existing.getR21_non_interest_bearing(), existing.getR32_non_interest_bearing(),
					existing.getR33_third_month(), existing.getR33_last_month(), existing.getR33_first_year(),
					existing.getR33_fifth_year(), existing.getR33_non_interest_bearing(), existing.getR34_first_month(),
					existing.getReportDate());

			auditService.compareEntitiesmanual(oldcopy, existing, request.getReportDate().toString(),
					"M LIQGAP Manual Summary Screen", "BRRS_M_LIQGAP_MANUAL_SUMMARY");

			System.out.println("LIQGAP report updated successfully.");
		}
	}

	public byte[] getBRRS_M_LIQGAPExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LIQGAPARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
					version);
		}

		List<M_LIQGAP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

		List<M_LIQGAP_Manual_Summary_Entity> dataList1 = getManualSummaryDataByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

			// ===========================
			// STYLE DEFINITIONS
			// ===========================

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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);

			// ===========================

			int startRow = 6;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQGAP_Summary_Entity record = dataList.get(i);
					M_LIQGAP_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 1 ==========

					row = sheet.getRow(10);

					cell1 = row.getCell(1);
					if (cell1 == null)
						cell1 = row.createCell(1);

					if (record.getR11_first_month() != null) {
						cell1.setCellValue(record.getR11_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 2 ==========
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR11_third_month() != null) {
						cell2.setCellValue(record.getR11_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// ========== CELL 3 ==========
					Cell cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR11_last_month() != null) {
						cell3.setCellValue(record.getR11_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ========== CELL 4 ==========
					Cell cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);

					if (record.getR11_first_year() != null) {
						cell4.setCellValue(record.getR11_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// ========== CELL 5 ==========
					Cell cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);

					if (record.getR11_fifth_year() != null) {
						cell5.setCellValue(record.getR11_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ========== CELL 6 ==========
					Cell cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);

					if (record.getR11_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR11_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell1 = row.createCell(1);
					if (record.getR12_first_month() != null) {
						cell1.setCellValue(record.getR12_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_third_month() != null) {
						cell2.setCellValue(record.getR12_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_last_month() != null) {
						cell3.setCellValue(record.getR12_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR12_first_year() != null) {
						cell4.setCellValue(record.getR12_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR12_fifth_year() != null) {
						cell5.setCellValue(record.getR12_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR12_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR12_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_first_month() != null) {
						cell1.setCellValue(record.getR13_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_third_month() != null) {
						cell2.setCellValue(record.getR13_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_last_month() != null) {
						cell3.setCellValue(record.getR13_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR13_first_year() != null) {
						cell4.setCellValue(record.getR13_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_fifth_year() != null) {
						cell5.setCellValue(record.getR13_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR13_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR13_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_first_month() != null) {
						cell1.setCellValue(record.getR14_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_third_month() != null) {
						cell2.setCellValue(record.getR14_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_last_month() != null) {
						cell3.setCellValue(record.getR14_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_first_year() != null) {
						cell4.setCellValue(record.getR14_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_fifth_year() != null) {
						cell5.setCellValue(record.getR14_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR14_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR14_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					if (record.getR15_first_month() != null) {
						cell1.setCellValue(record.getR15_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_third_month() != null) {
						cell2.setCellValue(record.getR15_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_last_month() != null) {
						cell3.setCellValue(record.getR15_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_first_year() != null) {
						cell4.setCellValue(record.getR15_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_fifth_year() != null) {
						cell5.setCellValue(record.getR15_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR15_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR15_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					if (record.getR16_first_month() != null) {
						cell1.setCellValue(record.getR16_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_third_month() != null) {
						cell2.setCellValue(record.getR16_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_last_month() != null) {
						cell3.setCellValue(record.getR16_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_first_year() != null) {
						cell4.setCellValue(record.getR16_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_fifth_year() != null) {
						cell5.setCellValue(record.getR16_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR16_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_first_month() != null) {
						cell1.setCellValue(record.getR17_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_third_month() != null) {
						cell2.setCellValue(record.getR17_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_last_month() != null) {
						cell3.setCellValue(record.getR17_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_first_year() != null) {
						cell4.setCellValue(record.getR17_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR17_fifth_year() != null) {
						cell5.setCellValue(record.getR17_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR17_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_first_month() != null) {
						cell1.setCellValue(record.getR18_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_third_month() != null) {
						cell2.setCellValue(record.getR18_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_last_month() != null) {
						cell3.setCellValue(record.getR18_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_first_year() != null) {
						cell4.setCellValue(record.getR18_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR18_fifth_year() != null) {
						cell5.setCellValue(record.getR18_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR18_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR18_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_first_month() != null) {
						cell1.setCellValue(record.getR19_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_third_month() != null) {
						cell2.setCellValue(record.getR19_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_last_month() != null) {
						cell3.setCellValue(record.getR19_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_first_year() != null) {
						cell4.setCellValue(record.getR19_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR19_fifth_year() != null) {
						cell5.setCellValue(record.getR19_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR19_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR19_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_first_month() != null) {
						cell1.setCellValue(record.getR20_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_third_month() != null) {
						cell2.setCellValue(record.getR20_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR20_last_month() != null) {
						cell3.setCellValue(record.getR20_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR20_first_year() != null) {
						cell4.setCellValue(record.getR20_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR20_fifth_year() != null) {
						cell5.setCellValue(record.getR20_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR20_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR20_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					if (record.getR21_first_month() != null) {
						cell1.setCellValue(record.getR21_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_third_month() != null) {
						cell2.setCellValue(record.getR21_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_last_month() != null) {
						cell3.setCellValue(record.getR21_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR21_first_year() != null) {
						cell4.setCellValue(record.getR21_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR21_fifth_year() != null) {
						cell5.setCellValue(record.getR21_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR21_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR21_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					if (record.getR22_first_month() != null) {
						cell1.setCellValue(record.getR22_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_third_month() != null) {
						cell2.setCellValue(record.getR22_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_last_month() != null) {
						cell3.setCellValue(record.getR22_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR22_first_year() != null) {
						cell4.setCellValue(record.getR22_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR22_fifth_year() != null) {
						cell5.setCellValue(record.getR22_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR22_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR22_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_first_month() != null) {
						cell1.setCellValue(record.getR23_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_third_month() != null) {
						cell2.setCellValue(record.getR23_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_last_month() != null) {
						cell3.setCellValue(record.getR23_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR23_first_year() != null) {
						cell4.setCellValue(record.getR23_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_fifth_year() != null) {
						cell5.setCellValue(record.getR23_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR23_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR23_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_first_month() != null) {
						cell1.setCellValue(record.getR24_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_third_month() != null) {
						cell2.setCellValue(record.getR24_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_last_month() != null) {
						cell3.setCellValue(record.getR24_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR24_first_year() != null) {
						cell4.setCellValue(record.getR24_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR24_fifth_year() != null) {
						cell5.setCellValue(record.getR24_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR24_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR24_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					if (record.getR25_first_month() != null) {
						cell1.setCellValue(record.getR25_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_third_month() != null) {
						cell2.setCellValue(record.getR25_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_last_month() != null) {
						cell3.setCellValue(record.getR25_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR25_first_year() != null) {
						cell4.setCellValue(record.getR25_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR25_fifth_year() != null) {
						cell5.setCellValue(record.getR25_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR25_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR25_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_first_month() != null) {
						cell1.setCellValue(record.getR26_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_third_month() != null) {
						cell2.setCellValue(record.getR26_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_last_month() != null) {
						cell3.setCellValue(record.getR26_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR26_first_year() != null) {
						cell4.setCellValue(record.getR26_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR26_fifth_year() != null) {
						cell5.setCellValue(record.getR26_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR26_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR26_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					if (record.getR28_first_month() != null) {
						cell1.setCellValue(record.getR28_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_third_month() != null) {
						cell2.setCellValue(record.getR28_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_last_month() != null) {
						cell3.setCellValue(record.getR28_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR28_first_year() != null) {
						cell4.setCellValue(record.getR28_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR28_fifth_year() != null) {
						cell5.setCellValue(record.getR28_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR28_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR28_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_first_month() != null) {
						cell1.setCellValue(record.getR29_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_third_month() != null) {
						cell2.setCellValue(record.getR29_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_last_month() != null) {
						cell3.setCellValue(record.getR29_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR29_first_year() != null) {
						cell4.setCellValue(record.getR29_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR29_fifth_year() != null) {
						cell5.setCellValue(record.getR29_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR29_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR29_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_first_month() != null) {
						cell1.setCellValue(record.getR30_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_third_month() != null) {
						cell2.setCellValue(record.getR30_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_last_month() != null) {
						cell3.setCellValue(record.getR30_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR30_first_year() != null) {
						cell4.setCellValue(record.getR30_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR30_fifth_year() != null) {
						cell5.setCellValue(record.getR30_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR30_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR30_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					if (record.getR31_first_month() != null) {
						cell1.setCellValue(record.getR31_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_third_month() != null) {
						cell2.setCellValue(record.getR31_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR31_last_month() != null) {
						cell3.setCellValue(record.getR31_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR31_first_year() != null) {
						cell4.setCellValue(record.getR31_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR31_fifth_year() != null) {
						cell5.setCellValue(record.getR31_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR31_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR31_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_first_month() != null) {
						cell1.setCellValue(record.getR32_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_third_month() != null) {
						cell2.setCellValue(record.getR32_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR32_last_month() != null) {
						cell3.setCellValue(record.getR32_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR32_first_year() != null) {
						cell4.setCellValue(record.getR32_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR32_fifth_year() != null) {
						cell5.setCellValue(record.getR32_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR32_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR32_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					if (record.getR33_first_month() != null) {
						cell1.setCellValue(record.getR33_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_third_month() != null) {
						cell2.setCellValue(record1.getR33_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record1.getR33_last_month() != null) {
						cell3.setCellValue(record1.getR33_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record1.getR33_first_year() != null) {
						cell4.setCellValue(record1.getR33_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record1.getR33_fifth_year() != null) {
						cell5.setCellValue(record1.getR33_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR33_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR33_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					if (record1.getR34_first_month() != null) {
						cell1.setCellValue(record1.getR34_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_third_month() != null) {
						cell2.setCellValue(record.getR34_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR34_last_month() != null) {
						cell3.setCellValue(record.getR34_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR34_first_year() != null) {
						cell4.setCellValue(record.getR34_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR34_fifth_year() != null) {
						cell5.setCellValue(record.getR34_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR34_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR34_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					if (record.getR35_first_month() != null) {
						cell1.setCellValue(record.getR35_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_third_month() != null) {
						cell2.setCellValue(record.getR35_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_last_month() != null) {
						cell3.setCellValue(record.getR35_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR35_first_year() != null) {
						cell4.setCellValue(record.getR35_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR35_fifth_year() != null) {
						cell5.setCellValue(record.getR35_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR35_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR35_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					if (record.getR36_first_month() != null) {
						cell1.setCellValue(record.getR36_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR36_third_month() != null) {
						cell2.setCellValue(record.getR36_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_last_month() != null) {
						cell3.setCellValue(record.getR36_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR36_first_year() != null) {
						cell4.setCellValue(record.getR36_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR36_fifth_year() != null) {
						cell5.setCellValue(record.getR36_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR36_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR36_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell1 = row.createCell(1);
					if (record.getR37_first_month() != null) {
						cell1.setCellValue(record.getR37_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_third_month() != null) {
						cell2.setCellValue(record.getR37_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_last_month() != null) {
						cell3.setCellValue(record.getR37_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR37_first_year() != null) {
						cell4.setCellValue(record.getR37_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR37_fifth_year() != null) {
						cell5.setCellValue(record.getR37_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR37_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR37_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell1 = row.createCell(1);
					if (record.getR38_first_month() != null) {
						cell1.setCellValue(record.getR38_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_third_month() != null) {
						cell2.setCellValue(record.getR38_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_last_month() != null) {
						cell3.setCellValue(record.getR38_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR38_first_year() != null) {
						cell4.setCellValue(record.getR38_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR38_fifth_year() != null) {
						cell5.setCellValue(record.getR38_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR38_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR38_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell1 = row.createCell(1);
					if (record.getR39_first_month() != null) {
						cell1.setCellValue(record.getR39_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR39_third_month() != null) {
						cell2.setCellValue(record.getR39_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_last_month() != null) {
						cell3.setCellValue(record.getR39_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR39_first_year() != null) {
						cell4.setCellValue(record.getR39_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR39_fifth_year() != null) {
						cell5.setCellValue(record.getR39_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR39_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR39_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell1 = row.createCell(1);
					if (record.getR40_first_month() != null) {
						cell1.setCellValue(record.getR40_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_third_month() != null) {
						cell2.setCellValue(record.getR40_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_last_month() != null) {
						cell3.setCellValue(record.getR40_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR40_first_year() != null) {
						cell4.setCellValue(record.getR40_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR40_fifth_year() != null) {
						cell5.setCellValue(record.getR40_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR40_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR40_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell1 = row.createCell(1);
					if (record.getR41_first_month() != null) {
						cell1.setCellValue(record.getR41_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_third_month() != null) {
						cell2.setCellValue(record.getR41_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_last_month() != null) {
						cell3.setCellValue(record.getR41_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR41_first_year() != null) {
						cell4.setCellValue(record.getR41_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR41_fifth_year() != null) {
						cell5.setCellValue(record.getR41_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR41_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR41_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_first_month() != null) {
						cell1.setCellValue(record.getR42_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR42_third_month() != null) {
						cell2.setCellValue(record.getR42_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR42_last_month() != null) {
						cell3.setCellValue(record.getR42_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR42_first_year() != null) {
						cell4.setCellValue(record.getR42_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR42_fifth_year() != null) {
						cell5.setCellValue(record.getR42_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR42_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR42_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell1 = row.createCell(1);
					if (record.getR43_first_month() != null) {
						cell1.setCellValue(record.getR43_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR43_third_month() != null) {
						cell2.setCellValue(record.getR43_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR43_last_month() != null) {
						cell3.setCellValue(record.getR43_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR43_first_year() != null) {
						cell4.setCellValue(record.getR43_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_fifth_year() != null) {
						cell5.setCellValue(record.getR43_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR43_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR43_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_first_month() != null) {
						cell1.setCellValue(record.getR44_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR44_third_month() != null) {
						cell2.setCellValue(record.getR44_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR44_last_month() != null) {
						cell3.setCellValue(record.getR44_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR44_first_year() != null) {
						cell4.setCellValue(record.getR44_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_fifth_year() != null) {
						cell5.setCellValue(record.getR44_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR44_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR44_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell1 = row.createCell(1);
					if (record.getR45_first_month() != null) {
						cell1.setCellValue(record.getR45_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR45_third_month() != null) {
						cell2.setCellValue(record.getR45_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR45_last_month() != null) {
						cell3.setCellValue(record.getR45_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR45_first_year() != null) {
						cell4.setCellValue(record.getR45_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR45_fifth_year() != null) {
						cell5.setCellValue(record.getR45_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR45_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR45_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell1 = row.createCell(1);
					if (record.getR46_first_month() != null) {
						cell1.setCellValue(record.getR46_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR46_third_month() != null) {
						cell2.setCellValue(record.getR46_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR46_last_month() != null) {
						cell3.setCellValue(record.getR46_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR46_first_year() != null) {
						cell4.setCellValue(record.getR46_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR46_fifth_year() != null) {
						cell5.setCellValue(record.getR46_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR46_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR46_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell1 = row.createCell(1);
					if (record.getR47_first_month() != null) {
						cell1.setCellValue(record.getR47_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR47_third_month() != null) {
						cell2.setCellValue(record.getR47_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR47_last_month() != null) {
						cell3.setCellValue(record.getR47_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR47_first_year() != null) {
						cell4.setCellValue(record.getR47_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR47_fifth_year() != null) {
						cell5.setCellValue(record.getR47_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR47_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR47_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_first_month() != null) {
						cell1.setCellValue(record.getR48_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR48_third_month() != null) {
						cell2.setCellValue(record.getR48_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR48_last_month() != null) {
						cell3.setCellValue(record.getR48_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR48_first_year() != null) {
						cell4.setCellValue(record.getR48_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR48_fifth_year() != null) {
						cell5.setCellValue(record.getR48_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR48_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR48_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_first_month() != null) {
						cell1.setCellValue(record.getR49_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR49_third_month() != null) {
						cell2.setCellValue(record.getR49_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR49_last_month() != null) {
						cell3.setCellValue(record.getR49_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR49_first_year() != null) {
						cell4.setCellValue(record.getR49_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR49_fifth_year() != null) {
						cell5.setCellValue(record.getR49_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR49_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR49_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}

				FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				evaluator.setIgnoreMissingWorkbooks(true);
				evaluator.evaluateAll();
			}

			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQGAP SUMMARY", null,
						"BRRS_M_LIQGAP_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] getM_LIQGAPDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {

			logger.info("Generating Excel for M_LIQGAP Details...");

			// ✅ SAFE ARCHIVAL CHECK (Prevents NullPointerException)
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && !version.isEmpty()) {
				return getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type, version);
			}

			// ✅ Validate todate
			if (todate == null || todate.trim().isEmpty()) {
				logger.error("To Date is NULL or Empty!");
				return new byte[0];
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LIQGAPDetail");

			BorderStyle border = BorderStyle.THIN;

			// ================= HEADER STYLE =================
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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.000"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// ================= HEADER ROW =================
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);

			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// ================= FETCH DATA =================
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);

			// ✅ JDBC METHOD - getDetailDataByDate
			List<M_LIQGAP_Detail_Entity> reportData = getDetailDataByDate(parsedToDate);

			logger.info("Fetched {} records", reportData != null ? reportData.size() : 0);

			if (reportData != null && !reportData.isEmpty()) {

				int rowIndex = 1;

				for (M_LIQGAP_Detail_Entity item : reportData) {

					if (item == null)
						continue;

					XSSFRow row = sheet.createRow(rowIndex++);

					// Cust ID
					Cell c0 = row.createCell(0);
					c0.setCellValue(item.getCustId() != null ? item.getCustId() : "");
					c0.setCellStyle(dataStyle);

					// Acct No
					Cell c1 = row.createCell(1);
					c1.setCellValue(item.getAcctNumber() != null ? item.getAcctNumber() : "");
					c1.setCellStyle(dataStyle);

					// Acct Name
					Cell c2 = row.createCell(2);
					c2.setCellValue(item.getAcctName() != null ? item.getAcctName() : "");
					c2.setCellStyle(dataStyle);

					// Balance
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					// Report Label
					Cell c4 = row.createCell(4);
					c4.setCellValue(item.getReportLabel() != null ? item.getReportLabel() : "");
					c4.setCellStyle(dataStyle);

					// Addl Criteria
					Cell c5 = row.createCell(5);
					c5.setCellValue(item.getReportAddlCriteria1() != null ? item.getReportAddlCriteria1() : "");
					c5.setCellStyle(dataStyle);

					// Report Date
					Cell c6 = row.createCell(6);
					c6.setCellValue(item.getReportDate() != null
							? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
							: "");
					c6.setCellStyle(dataStyle);
				}
			} else {
				logger.info("No data found for M_LIQGAP — only header written.");
			}

			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed successfully.");
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQGAP Excel", e);
			return new byte[0];
		}
	}

	public List<Object[]> getM_LIQGAPArchival() {

		List<Object[]> archivalList = new ArrayList<>();

		try {

			// ✅ JDBC METHOD - getArchivalSummaryDataWithVersion
			List<M_LIQGAP_Archival_Summary_Entity> repoData1 = getArchivalSummaryDataWithVersion();

			if (repoData1 != null && !repoData1.isEmpty()) {

				for (M_LIQGAP_Archival_Summary_Entity entity : repoData1) {

					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + repoData1.size() + " records from LIQGAP Repo1");

			} else {
				System.out.println("No archival data found in LIQGAP Repo1");
			}

			// ✅ JDBC METHOD - getManualArchivalSummaryDataWithVersion
			List<M_LIQGAP_Manual_Archival_Summary_Entity> repoData2 = getManualArchivalSummaryDataWithVersion();

			if (repoData2 != null && !repoData2.isEmpty()) {

				for (M_LIQGAP_Manual_Archival_Summary_Entity entity : repoData2) {

					Object[] row = new Object[] { entity.getReportDate(), entity.getReportVersion(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + repoData2.size() + " records from LIQGAP Repo2");

			} else {
				System.out.println("No archival data found in LIQGAP Repo2");
			}

		} catch (Exception e) {

			System.err.println("Error fetching M_LIQGAP Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public byte[] getExcelM_LIQGAPARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LIQGAPArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_LIQGAP_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);
		List<M_LIQGAP_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty() || dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_LIQGAP report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 6;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQGAP_Archival_Summary_Entity record = dataList.get(i);
					M_LIQGAP_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 1 ==========

					row = sheet.getRow(10);
					if (cell1 == null)
						cell1 = row.createCell(1);

					if (record.getR11_first_month() != null) {
						cell1.setCellValue(record.getR11_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 2 ==========
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR11_third_month() != null) {
						cell2.setCellValue(record.getR11_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// ========== CELL 3 ==========
					Cell cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR11_last_month() != null) {
						cell3.setCellValue(record.getR11_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ========== CELL 4 ==========
					Cell cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);

					if (record.getR11_first_year() != null) {
						cell4.setCellValue(record.getR11_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// ========== CELL 5 ==========
					Cell cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);

					if (record.getR11_fifth_year() != null) {
						cell5.setCellValue(record.getR11_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// ========== CELL 6 ==========
					Cell cell6 = row.getCell(6);
					if (cell6 == null)
						cell6 = row.createCell(6);

					if (record.getR11_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR11_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell1 = row.createCell(1);
					if (record.getR12_first_month() != null) {
						cell1.setCellValue(record.getR12_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_third_month() != null) {
						cell2.setCellValue(record.getR12_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_last_month() != null) {
						cell3.setCellValue(record.getR12_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR12_first_year() != null) {
						cell4.setCellValue(record.getR12_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR12_fifth_year() != null) {
						cell5.setCellValue(record.getR12_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR12_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR12_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_first_month() != null) {
						cell1.setCellValue(record.getR13_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_third_month() != null) {
						cell2.setCellValue(record.getR13_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_last_month() != null) {
						cell3.setCellValue(record.getR13_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR13_first_year() != null) {
						cell4.setCellValue(record.getR13_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_fifth_year() != null) {
						cell5.setCellValue(record.getR13_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR13_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR13_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_first_month() != null) {
						cell1.setCellValue(record.getR14_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_third_month() != null) {
						cell2.setCellValue(record.getR14_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_last_month() != null) {
						cell3.setCellValue(record.getR14_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_first_year() != null) {
						cell4.setCellValue(record.getR14_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_fifth_year() != null) {
						cell5.setCellValue(record.getR14_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR14_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR14_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					if (record.getR15_first_month() != null) {
						cell1.setCellValue(record.getR15_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_third_month() != null) {
						cell2.setCellValue(record.getR15_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_last_month() != null) {
						cell3.setCellValue(record.getR15_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_first_year() != null) {
						cell4.setCellValue(record.getR15_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_fifth_year() != null) {
						cell5.setCellValue(record.getR15_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR15_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR15_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					if (record.getR16_first_month() != null) {
						cell1.setCellValue(record.getR16_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_third_month() != null) {
						cell2.setCellValue(record.getR16_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_last_month() != null) {
						cell3.setCellValue(record.getR16_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_first_year() != null) {
						cell4.setCellValue(record.getR16_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_fifth_year() != null) {
						cell5.setCellValue(record.getR16_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR16_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_first_month() != null) {
						cell1.setCellValue(record.getR17_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_third_month() != null) {
						cell2.setCellValue(record.getR17_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_last_month() != null) {
						cell3.setCellValue(record.getR17_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_first_year() != null) {
						cell4.setCellValue(record.getR17_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR17_fifth_year() != null) {
						cell5.setCellValue(record.getR17_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR17_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_first_month() != null) {
						cell1.setCellValue(record.getR18_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_third_month() != null) {
						cell2.setCellValue(record.getR18_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_last_month() != null) {
						cell3.setCellValue(record.getR18_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_first_year() != null) {
						cell4.setCellValue(record.getR18_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR18_fifth_year() != null) {
						cell5.setCellValue(record.getR18_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR18_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR18_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_first_month() != null) {
						cell1.setCellValue(record.getR19_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_third_month() != null) {
						cell2.setCellValue(record.getR19_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_last_month() != null) {
						cell3.setCellValue(record.getR19_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_first_year() != null) {
						cell4.setCellValue(record.getR19_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR19_fifth_year() != null) {
						cell5.setCellValue(record.getR19_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR19_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR19_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_first_month() != null) {
						cell1.setCellValue(record.getR20_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_third_month() != null) {
						cell2.setCellValue(record.getR20_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR20_last_month() != null) {
						cell3.setCellValue(record.getR20_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR20_first_year() != null) {
						cell4.setCellValue(record.getR20_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR20_fifth_year() != null) {
						cell5.setCellValue(record.getR20_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR20_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR20_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					if (record.getR21_first_month() != null) {
						cell1.setCellValue(record.getR21_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_third_month() != null) {
						cell2.setCellValue(record.getR21_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_last_month() != null) {
						cell3.setCellValue(record.getR21_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR21_first_year() != null) {
						cell4.setCellValue(record.getR21_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR21_fifth_year() != null) {
						cell5.setCellValue(record.getR21_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR21_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR21_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					if (record.getR22_first_month() != null) {
						cell1.setCellValue(record.getR22_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_third_month() != null) {
						cell2.setCellValue(record.getR22_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_last_month() != null) {
						cell3.setCellValue(record.getR22_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR22_first_year() != null) {
						cell4.setCellValue(record.getR22_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR22_fifth_year() != null) {
						cell5.setCellValue(record.getR22_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR22_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR22_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_first_month() != null) {
						cell1.setCellValue(record.getR23_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_third_month() != null) {
						cell2.setCellValue(record.getR23_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_last_month() != null) {
						cell3.setCellValue(record.getR23_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR23_first_year() != null) {
						cell4.setCellValue(record.getR23_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_fifth_year() != null) {
						cell5.setCellValue(record.getR23_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR23_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR23_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_first_month() != null) {
						cell1.setCellValue(record.getR24_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_third_month() != null) {
						cell2.setCellValue(record.getR24_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_last_month() != null) {
						cell3.setCellValue(record.getR24_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR24_first_year() != null) {
						cell4.setCellValue(record.getR24_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR24_fifth_year() != null) {
						cell5.setCellValue(record.getR24_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR24_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR24_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					if (record.getR25_first_month() != null) {
						cell1.setCellValue(record.getR25_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_third_month() != null) {
						cell2.setCellValue(record.getR25_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_last_month() != null) {
						cell3.setCellValue(record.getR25_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR25_first_year() != null) {
						cell4.setCellValue(record.getR25_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR25_fifth_year() != null) {
						cell5.setCellValue(record.getR25_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR25_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR25_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_first_month() != null) {
						cell1.setCellValue(record.getR26_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_third_month() != null) {
						cell2.setCellValue(record.getR26_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_last_month() != null) {
						cell3.setCellValue(record.getR26_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR26_first_year() != null) {
						cell4.setCellValue(record.getR26_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR26_fifth_year() != null) {
						cell5.setCellValue(record.getR26_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR26_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR26_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					if (record.getR28_first_month() != null) {
						cell1.setCellValue(record.getR28_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_third_month() != null) {
						cell2.setCellValue(record.getR28_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_last_month() != null) {
						cell3.setCellValue(record.getR28_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR28_first_year() != null) {
						cell4.setCellValue(record.getR28_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR28_fifth_year() != null) {
						cell5.setCellValue(record.getR28_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR28_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR28_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_first_month() != null) {
						cell1.setCellValue(record.getR29_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_third_month() != null) {
						cell2.setCellValue(record.getR29_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_last_month() != null) {
						cell3.setCellValue(record.getR29_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR29_first_year() != null) {
						cell4.setCellValue(record.getR29_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR29_fifth_year() != null) {
						cell5.setCellValue(record.getR29_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR29_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR29_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_first_month() != null) {
						cell1.setCellValue(record.getR30_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_third_month() != null) {
						cell2.setCellValue(record.getR30_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_last_month() != null) {
						cell3.setCellValue(record.getR30_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR30_first_year() != null) {
						cell4.setCellValue(record.getR30_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR30_fifth_year() != null) {
						cell5.setCellValue(record.getR30_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR30_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR30_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					if (record.getR31_first_month() != null) {
						cell1.setCellValue(record.getR31_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_third_month() != null) {
						cell2.setCellValue(record.getR31_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR31_last_month() != null) {
						cell3.setCellValue(record.getR31_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR31_first_year() != null) {
						cell4.setCellValue(record.getR31_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR31_fifth_year() != null) {
						cell5.setCellValue(record.getR31_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR31_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR31_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_first_month() != null) {
						cell1.setCellValue(record.getR32_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_third_month() != null) {
						cell2.setCellValue(record.getR32_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR32_last_month() != null) {
						cell3.setCellValue(record.getR32_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR32_first_year() != null) {
						cell4.setCellValue(record.getR32_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR32_fifth_year() != null) {
						cell5.setCellValue(record.getR32_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR32_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR32_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					if (record.getR33_first_month() != null) {
						cell1.setCellValue(record.getR33_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_third_month() != null) {
						cell2.setCellValue(record1.getR33_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record1.getR33_last_month() != null) {
						cell3.setCellValue(record1.getR33_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record1.getR33_first_year() != null) {
						cell4.setCellValue(record1.getR33_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record1.getR33_fifth_year() != null) {
						cell5.setCellValue(record1.getR33_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record1.getR33_non_interest_bearing() != null) {
						cell6.setCellValue(record1.getR33_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					if (record1.getR34_first_month() != null) {
						cell1.setCellValue(record1.getR34_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_third_month() != null) {
						cell2.setCellValue(record.getR34_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR34_last_month() != null) {
						cell3.setCellValue(record.getR34_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR34_first_year() != null) {
						cell4.setCellValue(record.getR34_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR34_fifth_year() != null) {
						cell5.setCellValue(record.getR34_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR34_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR34_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					if (record.getR35_first_month() != null) {
						cell1.setCellValue(record.getR35_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_third_month() != null) {
						cell2.setCellValue(record.getR35_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_last_month() != null) {
						cell3.setCellValue(record.getR35_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR35_first_year() != null) {
						cell4.setCellValue(record.getR35_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR35_fifth_year() != null) {
						cell5.setCellValue(record.getR35_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR35_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR35_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					if (record.getR36_first_month() != null) {
						cell1.setCellValue(record.getR36_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR36_third_month() != null) {
						cell2.setCellValue(record.getR36_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_last_month() != null) {
						cell3.setCellValue(record.getR36_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR36_first_year() != null) {
						cell4.setCellValue(record.getR36_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR36_fifth_year() != null) {
						cell5.setCellValue(record.getR36_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR36_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR36_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell1 = row.createCell(1);
					if (record.getR37_first_month() != null) {
						cell1.setCellValue(record.getR37_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_third_month() != null) {
						cell2.setCellValue(record.getR37_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_last_month() != null) {
						cell3.setCellValue(record.getR37_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR37_first_year() != null) {
						cell4.setCellValue(record.getR37_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR37_fifth_year() != null) {
						cell5.setCellValue(record.getR37_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR37_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR37_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell1 = row.createCell(1);
					if (record.getR38_first_month() != null) {
						cell1.setCellValue(record.getR38_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_third_month() != null) {
						cell2.setCellValue(record.getR38_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_last_month() != null) {
						cell3.setCellValue(record.getR38_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR38_first_year() != null) {
						cell4.setCellValue(record.getR38_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR38_fifth_year() != null) {
						cell5.setCellValue(record.getR38_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR38_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR38_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell1 = row.createCell(1);
					if (record.getR39_first_month() != null) {
						cell1.setCellValue(record.getR39_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR39_third_month() != null) {
						cell2.setCellValue(record.getR39_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_last_month() != null) {
						cell3.setCellValue(record.getR39_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR39_first_year() != null) {
						cell4.setCellValue(record.getR39_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR39_fifth_year() != null) {
						cell5.setCellValue(record.getR39_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR39_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR39_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell1 = row.createCell(1);
					if (record.getR40_first_month() != null) {
						cell1.setCellValue(record.getR40_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_third_month() != null) {
						cell2.setCellValue(record.getR40_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_last_month() != null) {
						cell3.setCellValue(record.getR40_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR40_first_year() != null) {
						cell4.setCellValue(record.getR40_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR40_fifth_year() != null) {
						cell5.setCellValue(record.getR40_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR40_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR40_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell1 = row.createCell(1);
					if (record.getR41_first_month() != null) {
						cell1.setCellValue(record.getR41_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_third_month() != null) {
						cell2.setCellValue(record.getR41_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_last_month() != null) {
						cell3.setCellValue(record.getR41_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR41_first_year() != null) {
						cell4.setCellValue(record.getR41_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR41_fifth_year() != null) {
						cell5.setCellValue(record.getR41_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR41_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR41_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_first_month() != null) {
						cell1.setCellValue(record.getR42_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR42_third_month() != null) {
						cell2.setCellValue(record.getR42_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR42_last_month() != null) {
						cell3.setCellValue(record.getR42_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR42_first_year() != null) {
						cell4.setCellValue(record.getR42_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR42_fifth_year() != null) {
						cell5.setCellValue(record.getR42_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR42_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR42_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell1 = row.createCell(1);
					if (record.getR43_first_month() != null) {
						cell1.setCellValue(record.getR43_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR43_third_month() != null) {
						cell2.setCellValue(record.getR43_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR43_last_month() != null) {
						cell3.setCellValue(record.getR43_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR43_first_year() != null) {
						cell4.setCellValue(record.getR43_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_fifth_year() != null) {
						cell5.setCellValue(record.getR43_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR43_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR43_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_first_month() != null) {
						cell1.setCellValue(record.getR44_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR44_third_month() != null) {
						cell2.setCellValue(record.getR44_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR44_last_month() != null) {
						cell3.setCellValue(record.getR44_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR44_first_year() != null) {
						cell4.setCellValue(record.getR44_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_fifth_year() != null) {
						cell5.setCellValue(record.getR44_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR44_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR44_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell1 = row.createCell(1);
					if (record.getR45_first_month() != null) {
						cell1.setCellValue(record.getR45_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR45_third_month() != null) {
						cell2.setCellValue(record.getR45_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR45_last_month() != null) {
						cell3.setCellValue(record.getR45_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR45_first_year() != null) {
						cell4.setCellValue(record.getR45_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR45_fifth_year() != null) {
						cell5.setCellValue(record.getR45_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR45_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR45_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell1 = row.createCell(1);
					if (record.getR46_first_month() != null) {
						cell1.setCellValue(record.getR46_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR46_third_month() != null) {
						cell2.setCellValue(record.getR46_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR46_last_month() != null) {
						cell3.setCellValue(record.getR46_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR46_first_year() != null) {
						cell4.setCellValue(record.getR46_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR46_fifth_year() != null) {
						cell5.setCellValue(record.getR46_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR46_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR46_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell1 = row.createCell(1);
					if (record.getR47_first_month() != null) {
						cell1.setCellValue(record.getR47_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR47_third_month() != null) {
						cell2.setCellValue(record.getR47_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR47_last_month() != null) {
						cell3.setCellValue(record.getR47_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR47_first_year() != null) {
						cell4.setCellValue(record.getR47_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR47_fifth_year() != null) {
						cell5.setCellValue(record.getR47_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR47_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR47_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_first_month() != null) {
						cell1.setCellValue(record.getR48_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR48_third_month() != null) {
						cell2.setCellValue(record.getR48_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR48_last_month() != null) {
						cell3.setCellValue(record.getR48_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR48_first_year() != null) {
						cell4.setCellValue(record.getR48_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR48_fifth_year() != null) {
						cell5.setCellValue(record.getR48_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR48_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR48_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_first_month() != null) {
						cell1.setCellValue(record.getR49_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR49_third_month() != null) {
						cell2.setCellValue(record.getR49_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR49_last_month() != null) {
						cell3.setCellValue(record.getR49_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR49_first_year() != null) {
						cell4.setCellValue(record.getR49_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR49_fifth_year() != null) {
						cell5.setCellValue(record.getR49_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR49_non_interest_bearing() != null) {
						cell6.setCellValue(record.getR49_non_interest_bearing().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQGAP ARCHIVAL SUMMARY", null,
						"BRRS_M_LIQGAP_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LIQGAP ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") && version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LIQGAPDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

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

			// ✅ JDBC METHOD - REPLACED JPA REPOSITORY CALL
			List<M_LIQGAP_Archival_Detail_Entity> reportData = getArchivalDetailDataByDateAndVersion(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LIQGAP_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places with comma separator)
					Cell balanceCell = row.createCell(3);

					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}

					// Create style with thousand separator and decimal point
					DataFormat format = workbook.createDataFormat();

					// Format: 1,234,567
					balanceStyle.setDataFormat(format.getFormat("#,##0"));

					// Right alignment (optional)
					balanceStyle.setAlignment(HorizontalAlignment.RIGHT);

					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
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
				logger.info("No data found for M_LIQGAP — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_LIQGAP Excel", e);
			return new byte[0];
		}
	}

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_LIQGAP"); // ✅ match the report name
		System.out.println("Hello");
		if (acctNo != null) {
			// ✅ JDBC METHOD - REPLACED JPA REPOSITORY CALL
			M_LIQGAP_Detail_Entity dep3Entity = findDetailByAcctnumber(acctNo);
			if (dep3Entity != null && dep3Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(dep3Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", dep3Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	public ModelAndView updateDetailEdit(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/M_LIQGAP"); // ✅ match the report name

		if (acctNo != null) {
			// ✅ JDBC METHOD - REPLACED JPA REPOSITORY CALL
			M_LIQGAP_Detail_Entity la1Entity = findDetailByAcctnumber(acctNo);
			if (la1Entity != null && la1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(la1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
				System.out.println(formattedDate);
			}
			mv.addObject("Data", la1Entity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {
		try {
			String acctNo = request.getParameter("acctNumber");
			String provisionStr = request.getParameter("acctBalanceInpula");
			String acctName = request.getParameter("acctName");
			String report_dateStr = request.getParameter("report_date");

			logger.info("Received update for ACCT_NO: {}", acctNo);

			// ✅ JDBC METHOD - findDetailByAcctnumber
			M_LIQGAP_Detail_Entity existing = findDetailByAcctnumber(acctNo);
			if (existing == null) {
				logger.warn("No record found for ACCT_NO: {}", acctNo);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			if (acctName != null && !acctName.isEmpty()) {
				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {
					existing.setAcctName(acctName);
					isChanged = true;
					logger.info("Account name updated to {}", acctName);
				}
			}

			if (provisionStr != null && !provisionStr.isEmpty()) {
				BigDecimal newProvision = new BigDecimal(provisionStr);
				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newProvision) != 0) {
					existing.setAcctBalanceInpula(newProvision);
					isChanged = true;
					logger.info("Balance updated to {}", newProvision);
				}
			}

			if (isChanged) {
				// ✅ JDBC UPDATE - Direct SQL update
				String sql = "UPDATE BRRS_M_LIQGAP_DETAILTABLE SET " + "ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ? "
						+ "WHERE ACCT_NUMBER = ?";

				int updated = jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), acctNo);

				if (updated > 0) {
					logger.info("Record updated successfully for account {}", acctNo);
				}

				// Format date for procedure
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(report_dateStr));

				// Run summary procedure after commit
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						try {
							logger.info("Transaction committed — calling BRRS_M_LIQGAP_SUMMARY_PROCEDURE({})",
									formattedDate);
							jdbcTemplate.update("BEGIN BRRS_M_LIQGAP_SUMMARY_PROCEDURE(?); END;", formattedDate);
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
			logger.error("Error updating M_LIQGAP record", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	// Archival Email Excel
	public byte[] BRRS_M_LIQGAPArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		// ✅ JDBC METHODS - REPLACED JPA REPOSITORY CALLS
		List<M_LIQGAP_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);
		List<M_LIQGAP_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		if (dataList.isEmpty() || dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_LIQGAP report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---
			int startRow = 6;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQGAP_Archival_Summary_Entity record = dataList.get(i);
					M_LIQGAP_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 1 ==========

					row = sheet.getRow(10);
					if (cell1 == null)
						cell1 = row.createCell(1);

					if (record.getR11_first_month() != null) {
						cell1.setCellValue(record.getR11_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 2 ==========
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR11_third_month() != null) {
						cell2.setCellValue(record.getR11_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// ========== CELL 3 ==========
					Cell cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR11_last_month() != null) {
						cell3.setCellValue(record.getR11_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ========== CELL 4 ==========
					Cell cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);

					if (record.getR11_first_year() != null) {
						cell4.setCellValue(record.getR11_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// ========== CELL 5 ==========
					Cell cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);

					if (record.getR11_fifth_year() != null) {
						cell5.setCellValue(record.getR11_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell1 = row.createCell(1);
					if (record.getR12_first_month() != null) {
						cell1.setCellValue(record.getR12_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_third_month() != null) {
						cell2.setCellValue(record.getR12_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_last_month() != null) {
						cell3.setCellValue(record.getR12_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR12_first_year() != null) {
						cell4.setCellValue(record.getR12_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR12_fifth_year() != null) {
						cell5.setCellValue(record.getR12_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_first_month() != null) {
						cell1.setCellValue(record.getR13_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_third_month() != null) {
						cell2.setCellValue(record.getR13_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_last_month() != null) {
						cell3.setCellValue(record.getR13_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR13_first_year() != null) {
						cell4.setCellValue(record.getR13_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_fifth_year() != null) {
						cell5.setCellValue(record.getR13_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_first_month() != null) {
						cell1.setCellValue(record.getR14_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_third_month() != null) {
						cell2.setCellValue(record.getR14_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_last_month() != null) {
						cell3.setCellValue(record.getR14_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_first_year() != null) {
						cell4.setCellValue(record.getR14_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_fifth_year() != null) {
						cell5.setCellValue(record.getR14_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					if (record.getR15_first_month() != null) {
						cell1.setCellValue(record.getR15_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_third_month() != null) {
						cell2.setCellValue(record.getR15_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_last_month() != null) {
						cell3.setCellValue(record.getR15_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_first_year() != null) {
						cell4.setCellValue(record.getR15_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_fifth_year() != null) {
						cell5.setCellValue(record.getR15_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					if (record.getR16_first_month() != null) {
						cell1.setCellValue(record.getR16_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_third_month() != null) {
						cell2.setCellValue(record.getR16_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_last_month() != null) {
						cell3.setCellValue(record.getR16_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_first_year() != null) {
						cell4.setCellValue(record.getR16_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_fifth_year() != null) {
						cell5.setCellValue(record.getR16_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_first_month() != null) {
						cell1.setCellValue(record.getR17_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_third_month() != null) {
						cell2.setCellValue(record.getR17_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_last_month() != null) {
						cell3.setCellValue(record.getR17_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_first_year() != null) {
						cell4.setCellValue(record.getR17_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR17_fifth_year() != null) {
						cell5.setCellValue(record.getR17_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_first_month() != null) {
						cell1.setCellValue(record.getR18_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_third_month() != null) {
						cell2.setCellValue(record.getR18_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_last_month() != null) {
						cell3.setCellValue(record.getR18_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_first_year() != null) {
						cell4.setCellValue(record.getR18_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR18_fifth_year() != null) {
						cell5.setCellValue(record.getR18_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_first_month() != null) {
						cell1.setCellValue(record.getR19_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_third_month() != null) {
						cell2.setCellValue(record.getR19_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_last_month() != null) {
						cell3.setCellValue(record.getR19_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_first_year() != null) {
						cell4.setCellValue(record.getR19_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR19_fifth_year() != null) {
						cell5.setCellValue(record.getR19_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_first_month() != null) {
						cell1.setCellValue(record.getR20_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_third_month() != null) {
						cell2.setCellValue(record.getR20_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR20_last_month() != null) {
						cell3.setCellValue(record.getR20_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR20_first_year() != null) {
						cell4.setCellValue(record.getR20_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR20_fifth_year() != null) {
						cell5.setCellValue(record.getR20_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					if (record.getR21_first_month() != null) {
						cell1.setCellValue(record.getR21_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_third_month() != null) {
						cell2.setCellValue(record.getR21_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_last_month() != null) {
						cell3.setCellValue(record.getR21_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR21_first_year() != null) {
						cell4.setCellValue(record.getR21_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR21_fifth_year() != null) {
						cell5.setCellValue(record.getR21_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					if (record.getR22_first_month() != null) {
						cell1.setCellValue(record.getR22_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_third_month() != null) {
						cell2.setCellValue(record.getR22_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_last_month() != null) {
						cell3.setCellValue(record.getR22_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR22_first_year() != null) {
						cell4.setCellValue(record.getR22_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR22_fifth_year() != null) {
						cell5.setCellValue(record.getR22_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_first_month() != null) {
						cell1.setCellValue(record.getR23_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_third_month() != null) {
						cell2.setCellValue(record.getR23_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_last_month() != null) {
						cell3.setCellValue(record.getR23_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR23_first_year() != null) {
						cell4.setCellValue(record.getR23_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_fifth_year() != null) {
						cell5.setCellValue(record.getR23_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_first_month() != null) {
						cell1.setCellValue(record.getR24_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_third_month() != null) {
						cell2.setCellValue(record.getR24_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_last_month() != null) {
						cell3.setCellValue(record.getR24_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR24_first_year() != null) {
						cell4.setCellValue(record.getR24_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR24_fifth_year() != null) {
						cell5.setCellValue(record.getR24_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					if (record.getR25_first_month() != null) {
						cell1.setCellValue(record.getR25_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_third_month() != null) {
						cell2.setCellValue(record.getR25_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_last_month() != null) {
						cell3.setCellValue(record.getR25_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR25_first_year() != null) {
						cell4.setCellValue(record.getR25_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR25_fifth_year() != null) {
						cell5.setCellValue(record.getR25_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_first_month() != null) {
						cell1.setCellValue(record.getR26_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_third_month() != null) {
						cell2.setCellValue(record.getR26_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_last_month() != null) {
						cell3.setCellValue(record.getR26_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR26_first_year() != null) {
						cell4.setCellValue(record.getR26_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR26_fifth_year() != null) {
						cell5.setCellValue(record.getR26_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					if (record.getR28_first_month() != null) {
						cell1.setCellValue(record.getR28_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_third_month() != null) {
						cell2.setCellValue(record.getR28_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_last_month() != null) {
						cell3.setCellValue(record.getR28_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR28_first_year() != null) {
						cell4.setCellValue(record.getR28_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR28_fifth_year() != null) {
						cell5.setCellValue(record.getR28_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_first_month() != null) {
						cell1.setCellValue(record.getR29_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_third_month() != null) {
						cell2.setCellValue(record.getR29_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_last_month() != null) {
						cell3.setCellValue(record.getR29_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR29_first_year() != null) {
						cell4.setCellValue(record.getR29_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR29_fifth_year() != null) {
						cell5.setCellValue(record.getR29_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_first_month() != null) {
						cell1.setCellValue(record.getR30_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_third_month() != null) {
						cell2.setCellValue(record.getR30_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_last_month() != null) {
						cell3.setCellValue(record.getR30_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR30_first_year() != null) {
						cell4.setCellValue(record.getR30_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR30_fifth_year() != null) {
						cell5.setCellValue(record.getR30_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					if (record.getR31_first_month() != null) {
						cell1.setCellValue(record.getR31_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_third_month() != null) {
						cell2.setCellValue(record.getR31_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR31_last_month() != null) {
						cell3.setCellValue(record.getR31_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR31_first_year() != null) {
						cell4.setCellValue(record.getR31_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR31_fifth_year() != null) {
						cell5.setCellValue(record.getR31_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_first_month() != null) {
						cell1.setCellValue(record.getR32_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_third_month() != null) {
						cell2.setCellValue(record.getR32_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR32_last_month() != null) {
						cell3.setCellValue(record.getR32_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR32_first_year() != null) {
						cell4.setCellValue(record.getR32_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR32_fifth_year() != null) {
						cell5.setCellValue(record.getR32_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					if (record.getR33_first_month() != null) {
						cell1.setCellValue(record.getR33_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_third_month() != null) {
						cell2.setCellValue(record1.getR33_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record1.getR33_last_month() != null) {
						cell3.setCellValue(record1.getR33_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record1.getR33_first_year() != null) {
						cell4.setCellValue(record1.getR33_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record1.getR33_fifth_year() != null) {
						cell5.setCellValue(record1.getR33_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					if (record1.getR34_first_month() != null) {
						cell1.setCellValue(record1.getR34_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_third_month() != null) {
						cell2.setCellValue(record.getR34_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR34_last_month() != null) {
						cell3.setCellValue(record.getR34_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR34_first_year() != null) {
						cell4.setCellValue(record.getR34_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR34_fifth_year() != null) {
						cell5.setCellValue(record.getR34_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					if (record.getR35_first_month() != null) {
						cell1.setCellValue(record.getR35_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_third_month() != null) {
						cell2.setCellValue(record.getR35_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_last_month() != null) {
						cell3.setCellValue(record.getR35_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR35_first_year() != null) {
						cell4.setCellValue(record.getR35_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR35_fifth_year() != null) {
						cell5.setCellValue(record.getR35_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					if (record.getR36_first_month() != null) {
						cell1.setCellValue(record.getR36_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR36_third_month() != null) {
						cell2.setCellValue(record.getR36_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_last_month() != null) {
						cell3.setCellValue(record.getR36_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR36_first_year() != null) {
						cell4.setCellValue(record.getR36_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR36_fifth_year() != null) {
						cell5.setCellValue(record.getR36_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell1 = row.createCell(1);
					if (record.getR37_first_month() != null) {
						cell1.setCellValue(record.getR37_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_third_month() != null) {
						cell2.setCellValue(record.getR37_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_last_month() != null) {
						cell3.setCellValue(record.getR37_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR37_first_year() != null) {
						cell4.setCellValue(record.getR37_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR37_fifth_year() != null) {
						cell5.setCellValue(record.getR37_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell1 = row.createCell(1);
					if (record.getR38_first_month() != null) {
						cell1.setCellValue(record.getR38_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_third_month() != null) {
						cell2.setCellValue(record.getR38_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_last_month() != null) {
						cell3.setCellValue(record.getR38_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR38_first_year() != null) {
						cell4.setCellValue(record.getR38_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR38_fifth_year() != null) {
						cell5.setCellValue(record.getR38_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell1 = row.createCell(1);
					if (record.getR39_first_month() != null) {
						cell1.setCellValue(record.getR39_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR39_third_month() != null) {
						cell2.setCellValue(record.getR39_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_last_month() != null) {
						cell3.setCellValue(record.getR39_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR39_first_year() != null) {
						cell4.setCellValue(record.getR39_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR39_fifth_year() != null) {
						cell5.setCellValue(record.getR39_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell1 = row.createCell(1);
					if (record.getR40_first_month() != null) {
						cell1.setCellValue(record.getR40_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_third_month() != null) {
						cell2.setCellValue(record.getR40_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_last_month() != null) {
						cell3.setCellValue(record.getR40_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR40_first_year() != null) {
						cell4.setCellValue(record.getR40_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR40_fifth_year() != null) {
						cell5.setCellValue(record.getR40_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell1 = row.createCell(1);
					if (record.getR41_first_month() != null) {
						cell1.setCellValue(record.getR41_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_third_month() != null) {
						cell2.setCellValue(record.getR41_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_last_month() != null) {
						cell3.setCellValue(record.getR41_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR41_first_year() != null) {
						cell4.setCellValue(record.getR41_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR41_fifth_year() != null) {
						cell5.setCellValue(record.getR41_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_first_month() != null) {
						cell1.setCellValue(record.getR42_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR42_third_month() != null) {
						cell2.setCellValue(record.getR42_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR42_last_month() != null) {
						cell3.setCellValue(record.getR42_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR42_first_year() != null) {
						cell4.setCellValue(record.getR42_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR42_fifth_year() != null) {
						cell5.setCellValue(record.getR42_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell1 = row.createCell(1);
					if (record.getR43_first_month() != null) {
						cell1.setCellValue(record.getR43_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR43_third_month() != null) {
						cell2.setCellValue(record.getR43_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR43_last_month() != null) {
						cell3.setCellValue(record.getR43_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR43_first_year() != null) {
						cell4.setCellValue(record.getR43_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_fifth_year() != null) {
						cell5.setCellValue(record.getR43_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_first_month() != null) {
						cell1.setCellValue(record.getR44_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR44_third_month() != null) {
						cell2.setCellValue(record.getR44_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR44_last_month() != null) {
						cell3.setCellValue(record.getR44_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR44_first_year() != null) {
						cell4.setCellValue(record.getR44_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_fifth_year() != null) {
						cell5.setCellValue(record.getR44_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell1 = row.createCell(1);
					if (record.getR45_first_month() != null) {
						cell1.setCellValue(record.getR45_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR45_third_month() != null) {
						cell2.setCellValue(record.getR45_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR45_last_month() != null) {
						cell3.setCellValue(record.getR45_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR45_first_year() != null) {
						cell4.setCellValue(record.getR45_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR45_fifth_year() != null) {
						cell5.setCellValue(record.getR45_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell1 = row.createCell(1);
					if (record.getR46_first_month() != null) {
						cell1.setCellValue(record.getR46_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR46_third_month() != null) {
						cell2.setCellValue(record.getR46_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR46_last_month() != null) {
						cell3.setCellValue(record.getR46_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR46_first_year() != null) {
						cell4.setCellValue(record.getR46_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR46_fifth_year() != null) {
						cell5.setCellValue(record.getR46_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell1 = row.createCell(1);
					if (record.getR47_first_month() != null) {
						cell1.setCellValue(record.getR47_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR47_third_month() != null) {
						cell2.setCellValue(record.getR47_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR47_last_month() != null) {
						cell3.setCellValue(record.getR47_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR47_first_year() != null) {
						cell4.setCellValue(record.getR47_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR47_fifth_year() != null) {
						cell5.setCellValue(record.getR47_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_first_month() != null) {
						cell1.setCellValue(record.getR48_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR48_third_month() != null) {
						cell2.setCellValue(record.getR48_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR48_last_month() != null) {
						cell3.setCellValue(record.getR48_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR48_first_year() != null) {
						cell4.setCellValue(record.getR48_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR48_fifth_year() != null) {
						cell5.setCellValue(record.getR48_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_first_month() != null) {
						cell1.setCellValue(record.getR49_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR49_third_month() != null) {
						cell2.setCellValue(record.getR49_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR49_last_month() != null) {
						cell3.setCellValue(record.getR49_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR49_first_year() != null) {
						cell4.setCellValue(record.getR49_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR49_fifth_year() != null) {
						cell5.setCellValue(record.getR49_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQGAP EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_LIQGAP_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_LIQGAPEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return BRRS_M_LIQGAPArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// ✅ JDBC METHODS - REPLACED JPA REPOSITORY CALLS
		List<M_LIQGAP_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));
		List<M_LIQGAP_Manual_Summary_Entity> dataList1 = getManualSummaryDataByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRF2.4 report. Returning empty result.");
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

			CellStyle percentStyle = workbook.createCellStyle();
			percentStyle.cloneStyleFrom(numberStyle);
			percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
			percentStyle.setAlignment(HorizontalAlignment.RIGHT);
			// --- End of Style Definitions ---

			int startRow = 6;

			if (!dataList.isEmpty() || !dataList1.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_LIQGAP_Summary_Entity record = dataList.get(i);
					M_LIQGAP_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getReportDate() != null) {
						cell1.setCellValue(record.getReportDate()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 1 ==========

					row = sheet.getRow(10);
					if (cell1 == null)
						cell1 = row.createCell(1);

					if (record.getR11_first_month() != null) {
						cell1.setCellValue(record.getR11_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// ========== CELL 2 ==========
					Cell cell2 = row.getCell(2);
					if (cell2 == null)
						cell2 = row.createCell(2);

					if (record.getR11_third_month() != null) {
						cell2.setCellValue(record.getR11_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// ========== CELL 3 ==========
					Cell cell3 = row.getCell(3);
					if (cell3 == null)
						cell3 = row.createCell(3);

					if (record.getR11_last_month() != null) {
						cell3.setCellValue(record.getR11_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ========== CELL 4 ==========
					Cell cell4 = row.getCell(4);
					if (cell4 == null)
						cell4 = row.createCell(4);

					if (record.getR11_first_year() != null) {
						cell4.setCellValue(record.getR11_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// ========== CELL 5 ==========
					Cell cell5 = row.getCell(5);
					if (cell5 == null)
						cell5 = row.createCell(5);

					if (record.getR11_fifth_year() != null) {
						cell5.setCellValue(record.getR11_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell1 = row.createCell(1);
					if (record.getR12_first_month() != null) {
						cell1.setCellValue(record.getR12_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_third_month() != null) {
						cell2.setCellValue(record.getR12_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_last_month() != null) {
						cell3.setCellValue(record.getR12_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR12_first_year() != null) {
						cell4.setCellValue(record.getR12_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR12_fifth_year() != null) {
						cell5.setCellValue(record.getR12_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell1 = row.createCell(1);
					if (record.getR13_first_month() != null) {
						cell1.setCellValue(record.getR13_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_third_month() != null) {
						cell2.setCellValue(record.getR13_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_last_month() != null) {
						cell3.setCellValue(record.getR13_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR13_first_year() != null) {
						cell4.setCellValue(record.getR13_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR13_fifth_year() != null) {
						cell5.setCellValue(record.getR13_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell1 = row.createCell(1);
					if (record.getR14_first_month() != null) {
						cell1.setCellValue(record.getR14_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_third_month() != null) {
						cell2.setCellValue(record.getR14_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_last_month() != null) {
						cell3.setCellValue(record.getR14_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_first_year() != null) {
						cell4.setCellValue(record.getR14_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR14_fifth_year() != null) {
						cell5.setCellValue(record.getR14_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					if (record.getR15_first_month() != null) {
						cell1.setCellValue(record.getR15_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_third_month() != null) {
						cell2.setCellValue(record.getR15_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_last_month() != null) {
						cell3.setCellValue(record.getR15_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_first_year() != null) {
						cell4.setCellValue(record.getR15_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_fifth_year() != null) {
						cell5.setCellValue(record.getR15_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					if (record.getR16_first_month() != null) {
						cell1.setCellValue(record.getR16_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR16_third_month() != null) {
						cell2.setCellValue(record.getR16_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_last_month() != null) {
						cell3.setCellValue(record.getR16_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_first_year() != null) {
						cell4.setCellValue(record.getR16_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_fifth_year() != null) {
						cell5.setCellValue(record.getR16_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					if (record.getR17_first_month() != null) {
						cell1.setCellValue(record.getR17_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR17_third_month() != null) {
						cell2.setCellValue(record.getR17_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_last_month() != null) {
						cell3.setCellValue(record.getR17_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_first_year() != null) {
						cell4.setCellValue(record.getR17_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR17_fifth_year() != null) {
						cell5.setCellValue(record.getR17_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell1 = row.createCell(1);
					if (record.getR18_first_month() != null) {
						cell1.setCellValue(record.getR18_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR18_third_month() != null) {
						cell2.setCellValue(record.getR18_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_last_month() != null) {
						cell3.setCellValue(record.getR18_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_first_year() != null) {
						cell4.setCellValue(record.getR18_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR18_fifth_year() != null) {
						cell5.setCellValue(record.getR18_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					if (record.getR19_first_month() != null) {
						cell1.setCellValue(record.getR19_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR19_third_month() != null) {
						cell2.setCellValue(record.getR19_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_last_month() != null) {
						cell3.setCellValue(record.getR19_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_first_year() != null) {
						cell4.setCellValue(record.getR19_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR19_fifth_year() != null) {
						cell5.setCellValue(record.getR19_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					if (record.getR20_first_month() != null) {
						cell1.setCellValue(record.getR20_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR20_third_month() != null) {
						cell2.setCellValue(record.getR20_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR20_last_month() != null) {
						cell3.setCellValue(record.getR20_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR20_first_year() != null) {
						cell4.setCellValue(record.getR20_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR20_fifth_year() != null) {
						cell5.setCellValue(record.getR20_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					if (record.getR21_first_month() != null) {
						cell1.setCellValue(record.getR21_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_third_month() != null) {
						cell2.setCellValue(record.getR21_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_last_month() != null) {
						cell3.setCellValue(record.getR21_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR21_first_year() != null) {
						cell4.setCellValue(record.getR21_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR21_fifth_year() != null) {
						cell5.setCellValue(record.getR21_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					if (record.getR22_first_month() != null) {
						cell1.setCellValue(record.getR22_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_third_month() != null) {
						cell2.setCellValue(record.getR22_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_last_month() != null) {
						cell3.setCellValue(record.getR22_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR22_first_year() != null) {
						cell4.setCellValue(record.getR22_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR22_fifth_year() != null) {
						cell5.setCellValue(record.getR22_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					if (record.getR23_first_month() != null) {
						cell1.setCellValue(record.getR23_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_third_month() != null) {
						cell2.setCellValue(record.getR23_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_last_month() != null) {
						cell3.setCellValue(record.getR23_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR23_first_year() != null) {
						cell4.setCellValue(record.getR23_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR23_fifth_year() != null) {
						cell5.setCellValue(record.getR23_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					if (record.getR24_first_month() != null) {
						cell1.setCellValue(record.getR24_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_third_month() != null) {
						cell2.setCellValue(record.getR24_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_last_month() != null) {
						cell3.setCellValue(record.getR24_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR24_first_year() != null) {
						cell4.setCellValue(record.getR24_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR24_fifth_year() != null) {
						cell5.setCellValue(record.getR24_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					if (record.getR25_first_month() != null) {
						cell1.setCellValue(record.getR25_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_third_month() != null) {
						cell2.setCellValue(record.getR25_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_last_month() != null) {
						cell3.setCellValue(record.getR25_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR25_first_year() != null) {
						cell4.setCellValue(record.getR25_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR25_fifth_year() != null) {
						cell5.setCellValue(record.getR25_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					if (record.getR26_first_month() != null) {
						cell1.setCellValue(record.getR26_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_third_month() != null) {
						cell2.setCellValue(record.getR26_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_last_month() != null) {
						cell3.setCellValue(record.getR26_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR26_first_year() != null) {
						cell4.setCellValue(record.getR26_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR26_fifth_year() != null) {
						cell5.setCellValue(record.getR26_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					if (record.getR28_first_month() != null) {
						cell1.setCellValue(record.getR28_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_third_month() != null) {
						cell2.setCellValue(record.getR28_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_last_month() != null) {
						cell3.setCellValue(record.getR28_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR28_first_year() != null) {
						cell4.setCellValue(record.getR28_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR28_fifth_year() != null) {
						cell5.setCellValue(record.getR28_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					if (record.getR29_first_month() != null) {
						cell1.setCellValue(record.getR29_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_third_month() != null) {
						cell2.setCellValue(record.getR29_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_last_month() != null) {
						cell3.setCellValue(record.getR29_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR29_first_year() != null) {
						cell4.setCellValue(record.getR29_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR29_fifth_year() != null) {
						cell5.setCellValue(record.getR29_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					if (record.getR30_first_month() != null) {
						cell1.setCellValue(record.getR30_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_third_month() != null) {
						cell2.setCellValue(record.getR30_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_last_month() != null) {
						cell3.setCellValue(record.getR30_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR30_first_year() != null) {
						cell4.setCellValue(record.getR30_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR30_fifth_year() != null) {
						cell5.setCellValue(record.getR30_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					if (record.getR31_first_month() != null) {
						cell1.setCellValue(record.getR31_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR31_third_month() != null) {
						cell2.setCellValue(record.getR31_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR31_last_month() != null) {
						cell3.setCellValue(record.getR31_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR31_first_year() != null) {
						cell4.setCellValue(record.getR31_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR31_fifth_year() != null) {
						cell5.setCellValue(record.getR31_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					if (record.getR32_first_month() != null) {
						cell1.setCellValue(record.getR32_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR32_third_month() != null) {
						cell2.setCellValue(record.getR32_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR32_last_month() != null) {
						cell3.setCellValue(record.getR32_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR32_first_year() != null) {
						cell4.setCellValue(record.getR32_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR32_fifth_year() != null) {
						cell5.setCellValue(record.getR32_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					if (record.getR33_first_month() != null) {
						cell1.setCellValue(record.getR33_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_third_month() != null) {
						cell2.setCellValue(record1.getR33_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record1.getR33_last_month() != null) {
						cell3.setCellValue(record1.getR33_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record1.getR33_first_year() != null) {
						cell4.setCellValue(record1.getR33_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record1.getR33_fifth_year() != null) {
						cell5.setCellValue(record1.getR33_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					if (record1.getR34_first_month() != null) {
						cell1.setCellValue(record1.getR34_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR34_third_month() != null) {
						cell2.setCellValue(record.getR34_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR34_last_month() != null) {
						cell3.setCellValue(record.getR34_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR34_first_year() != null) {
						cell4.setCellValue(record.getR34_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR34_fifth_year() != null) {
						cell5.setCellValue(record.getR34_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					if (record.getR35_first_month() != null) {
						cell1.setCellValue(record.getR35_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR35_third_month() != null) {
						cell2.setCellValue(record.getR35_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR35_last_month() != null) {
						cell3.setCellValue(record.getR35_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR35_first_year() != null) {
						cell4.setCellValue(record.getR35_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR35_fifth_year() != null) {
						cell5.setCellValue(record.getR35_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					if (record.getR36_first_month() != null) {
						cell1.setCellValue(record.getR36_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR36_third_month() != null) {
						cell2.setCellValue(record.getR36_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR36_last_month() != null) {
						cell3.setCellValue(record.getR36_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR36_first_year() != null) {
						cell4.setCellValue(record.getR36_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR36_fifth_year() != null) {
						cell5.setCellValue(record.getR36_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell1 = row.createCell(1);
					if (record.getR37_first_month() != null) {
						cell1.setCellValue(record.getR37_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR37_third_month() != null) {
						cell2.setCellValue(record.getR37_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR37_last_month() != null) {
						cell3.setCellValue(record.getR37_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR37_first_year() != null) {
						cell4.setCellValue(record.getR37_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR37_fifth_year() != null) {
						cell5.setCellValue(record.getR37_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell1 = row.createCell(1);
					if (record.getR38_first_month() != null) {
						cell1.setCellValue(record.getR38_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR38_third_month() != null) {
						cell2.setCellValue(record.getR38_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR38_last_month() != null) {
						cell3.setCellValue(record.getR38_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR38_first_year() != null) {
						cell4.setCellValue(record.getR38_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR38_fifth_year() != null) {
						cell5.setCellValue(record.getR38_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell1 = row.createCell(1);
					if (record.getR39_first_month() != null) {
						cell1.setCellValue(record.getR39_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR39_third_month() != null) {
						cell2.setCellValue(record.getR39_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR39_last_month() != null) {
						cell3.setCellValue(record.getR39_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR39_first_year() != null) {
						cell4.setCellValue(record.getR39_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR39_fifth_year() != null) {
						cell5.setCellValue(record.getR39_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell1 = row.createCell(1);
					if (record.getR40_first_month() != null) {
						cell1.setCellValue(record.getR40_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR40_third_month() != null) {
						cell2.setCellValue(record.getR40_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR40_last_month() != null) {
						cell3.setCellValue(record.getR40_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR40_first_year() != null) {
						cell4.setCellValue(record.getR40_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR40_fifth_year() != null) {
						cell5.setCellValue(record.getR40_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell1 = row.createCell(1);
					if (record.getR41_first_month() != null) {
						cell1.setCellValue(record.getR41_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR41_third_month() != null) {
						cell2.setCellValue(record.getR41_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR41_last_month() != null) {
						cell3.setCellValue(record.getR41_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR41_first_year() != null) {
						cell4.setCellValue(record.getR41_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR41_fifth_year() != null) {
						cell5.setCellValue(record.getR41_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell1 = row.createCell(1);
					if (record.getR42_first_month() != null) {
						cell1.setCellValue(record.getR42_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR42_third_month() != null) {
						cell2.setCellValue(record.getR42_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR42_last_month() != null) {
						cell3.setCellValue(record.getR42_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR42_first_year() != null) {
						cell4.setCellValue(record.getR42_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR42_fifth_year() != null) {
						cell5.setCellValue(record.getR42_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell1 = row.createCell(1);
					if (record.getR43_first_month() != null) {
						cell1.setCellValue(record.getR43_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR43_third_month() != null) {
						cell2.setCellValue(record.getR43_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR43_last_month() != null) {
						cell3.setCellValue(record.getR43_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR43_first_year() != null) {
						cell4.setCellValue(record.getR43_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR43_fifth_year() != null) {
						cell5.setCellValue(record.getR43_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell1 = row.createCell(1);
					if (record.getR44_first_month() != null) {
						cell1.setCellValue(record.getR44_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR44_third_month() != null) {
						cell2.setCellValue(record.getR44_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR44_last_month() != null) {
						cell3.setCellValue(record.getR44_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR44_first_year() != null) {
						cell4.setCellValue(record.getR44_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR44_fifth_year() != null) {
						cell5.setCellValue(record.getR44_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell1 = row.createCell(1);
					if (record.getR45_first_month() != null) {
						cell1.setCellValue(record.getR45_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR45_third_month() != null) {
						cell2.setCellValue(record.getR45_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR45_last_month() != null) {
						cell3.setCellValue(record.getR45_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR45_first_year() != null) {
						cell4.setCellValue(record.getR45_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR45_fifth_year() != null) {
						cell5.setCellValue(record.getR45_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell1 = row.createCell(1);
					if (record.getR46_first_month() != null) {
						cell1.setCellValue(record.getR46_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR46_third_month() != null) {
						cell2.setCellValue(record.getR46_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR46_last_month() != null) {
						cell3.setCellValue(record.getR46_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR46_first_year() != null) {
						cell4.setCellValue(record.getR46_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR46_fifth_year() != null) {
						cell5.setCellValue(record.getR46_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell1 = row.createCell(1);
					if (record.getR47_first_month() != null) {
						cell1.setCellValue(record.getR47_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR47_third_month() != null) {
						cell2.setCellValue(record.getR47_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR47_last_month() != null) {
						cell3.setCellValue(record.getR47_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR47_first_year() != null) {
						cell4.setCellValue(record.getR47_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR47_fifth_year() != null) {
						cell5.setCellValue(record.getR47_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(47);
					cell1 = row.createCell(1);
					if (record.getR48_first_month() != null) {
						cell1.setCellValue(record.getR48_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR48_third_month() != null) {
						cell2.setCellValue(record.getR48_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR48_last_month() != null) {
						cell3.setCellValue(record.getR48_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR48_first_year() != null) {
						cell4.setCellValue(record.getR48_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR48_fifth_year() != null) {
						cell5.setCellValue(record.getR48_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(48);
					cell1 = row.createCell(1);
					if (record.getR49_first_month() != null) {
						cell1.setCellValue(record.getR49_first_month().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR49_third_month() != null) {
						cell2.setCellValue(record.getR49_third_month().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR49_last_month() != null) {
						cell3.setCellValue(record.getR49_last_month().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR49_first_year() != null) {
						cell4.setCellValue(record.getR49_first_year().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR49_fifth_year() != null) {
						cell5.setCellValue(record.getR49_fifth_year().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

				}

				try {
					workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
				} catch (RuntimeException e) {
					logger.warn("Skipping formula evaluation due to external references: {}", e.getMessage());
				}
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LIQGAP EMAIL SUMMARY", null,
						"BRRS_M_LIQGAP_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

}
