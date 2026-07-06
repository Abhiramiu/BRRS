
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
import java.util.Locale;
import java.util.Optional;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service

public class BRRS_M_SRWA_12C_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12C_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

//===========================
// JDBC REPO'S
//===========================

// ==============================
// BRRS_M_SRWA_12C_Summary_Repo
// ==============================

// ✅ Fetch record(s) by specific REPORT_DATE
	public List<M_SRWA_12C_Summary_Entity> getSummaryDataByDate(Date rpt_code) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { rpt_code }, new M_SRWA_12C_Summary_EntityRowMapper());
	}

// ✅ Fetch records with version for a specific date
	public List<M_SRWA_12C_Summary_Entity> getSummaryDataByDateWithVersion(String todate) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new M_SRWA_12C_Summary_EntityRowMapper());
	}

// ✅ Find the latest version for a report date
	public Optional<M_SRWA_12C_Summary_Entity> findTopSummaryByReportDateOrderByVersionDesc(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE REPORT_DATE = ? ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_SRWA_12C_Summary_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new M_SRWA_12C_Summary_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Check if a version exists for a report date
	public Optional<M_SRWA_12C_Summary_Entity> findSummaryByReportDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SRWA_12C_Summary_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12C_Summary_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Get all records with version (no parameters)
	public List<M_SRWA_12C_Summary_Entity> getSummaryDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Summary_EntityRowMapper());
	}

// ==============================
// BRRS_M_SRWA_12C_Detail_Repo
// ==============================

// ✅ Fetch record(s) by specific REPORT_DATE
	public List<M_SRWA_12C_Detail_Entity> getDetailDataByDate(Date rpt_Date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { rpt_Date }, new M_SRWA_12C_Detail_EntityRowMapper());
	}

// ✅ Fetch records with version for a specific date
	public List<M_SRWA_12C_Detail_Entity> getDetailDataByDateWithVersion(String todate) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION DESC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new Object[] { todate }, new M_SRWA_12C_Detail_EntityRowMapper());
	}

// ✅ Find the latest version for a report date
	public Optional<M_SRWA_12C_Detail_Entity> findTopDetailByReportDateOrderByVersionDesc(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_DATE = ? ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_SRWA_12C_Detail_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new M_SRWA_12C_Detail_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Check if a version exists for a report date
	public Optional<M_SRWA_12C_Detail_Entity> findDetailByReportDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SRWA_12C_Detail_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12C_Detail_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Get all records with version (no parameters)
	public List<M_SRWA_12C_Detail_Entity> getDetailDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Detail_EntityRowMapper());
	}

// ==============================
// BRRS_M_SRWA_12C_Archival_Summary_Repo
// ==============================

// ✅ Fetch archival records by date and version
	public List<M_SRWA_12C_Archival_Summary_Entity> getArchivalSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE >= TRUNC(?) AND REPORT_DATE < TRUNC(?) + 1 AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_date, report_version },
				new M_SRWA_12C_Archival_Summary_EntityRowMapper());
	}

// ✅ Fetch latest archival version for given date (no version input)
	public Optional<M_SRWA_12C_Archival_Summary_Entity> getLatestArchivalSummaryVersionByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_SRWA_12C_Archival_Summary_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new M_SRWA_12C_Archival_Summary_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Find archival summary by report date and version
	public Optional<M_SRWA_12C_Archival_Summary_Entity> findArchivalSummaryByReportDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SRWA_12C_Archival_Summary_Entity> results = jdbcTemplate.query(sql,
				new Object[] { report_date, report_version }, new M_SRWA_12C_Archival_Summary_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Get single record with version (Current Report Version Only Shown)
	public List<M_SRWA_12C_Archival_Summary_Entity> getArchivalSummaryDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Archival_Summary_EntityRowMapper());
	}

// ✅ Get all records with version
	public List<M_SRWA_12C_Archival_Summary_Entity> getArchivalSummaryDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Archival_Summary_EntityRowMapper());
	}

// ==============================
// BRRS_M_SRWA_12C_Archival_Detail_Repo
// ==============================

// ✅ Fetch archival detail records by date and version
	public List<M_SRWA_12C_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE >= TRUNC(?) AND REPORT_DATE < TRUNC(?) + 1 AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_date, report_version },
				new M_SRWA_12C_Archival_Detail_EntityRowMapper());
	}

// ✅ Fetch latest archival detail version for given date (no version input)
	public Optional<M_SRWA_12C_Archival_Detail_Entity> getLatestArchivalDetailVersionByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_SRWA_12C_Archival_Detail_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new M_SRWA_12C_Archival_Detail_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Find archival detail by report date and version
	public Optional<M_SRWA_12C_Archival_Detail_Entity> findArchivalDetailByReportDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		List<M_SRWA_12C_Archival_Detail_Entity> results = jdbcTemplate.query(sql,
				new Object[] { report_date, report_version }, new M_SRWA_12C_Archival_Detail_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

// ✅ Get single record with version (Current Report Version Only Shown)
	public List<M_SRWA_12C_Archival_Detail_Entity> getArchivalDetailDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Archival_Detail_EntityRowMapper());
	}

// ✅ Get all records with version
	public List<M_SRWA_12C_Archival_Detail_Entity> getArchivalDetailDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL";
		return jdbcTemplate.query(sql, new M_SRWA_12C_Archival_Detail_EntityRowMapper());
	}

/// ==============================
// BRRS_M_SRWA_12C_RESUB_Summary_Repo
// ==============================

// ✅ Fetch RESUB summary records by date and version
	public List<M_SRWA_12C_RESUB_Summary_Entity> getResubSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12C_RESUB_Summary_EntityRowMapper());
	}

// ✅ Find max version for a given date
	public BigDecimal findMaxResubSummaryVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

// ✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getResubSummaryReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

// ✅ Get single record with version (Current Report Version Only Shown)
	public List<M_SRWA_12C_RESUB_Summary_Entity> getResubSummaryDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_RESUB_Summary_EntityRowMapper());
	}

// ✅ Get all records with version
	public List<M_SRWA_12C_RESUB_Summary_Entity> getResubSummaryDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL";
		return jdbcTemplate.query(sql, new M_SRWA_12C_RESUB_Summary_EntityRowMapper());
	}

// ==============================
// BRRS_M_SRWA_12C_RESUB_Detail_Repo
// ==============================

// ✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getResubDetailReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12C_RESUB_DETAILTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

// ✅ Fetch RESUB detail records by date and version
	public List<M_SRWA_12C_RESUB_Detail_Entity> getResubDetailDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12C_RESUB_Detail_EntityRowMapper());
	}

// ✅ Find max version for a given date
	public BigDecimal findMaxResubDetailVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12C_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

// ✅ Get single record with version (Current Report Version Only Shown)
	public List<M_SRWA_12C_RESUB_Detail_Entity> getResubDetailDataWithVersion() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12C_RESUB_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC FETCH FIRST 1 ROWS ONLY";
		return jdbcTemplate.query(sql, new M_SRWA_12C_RESUB_Detail_EntityRowMapper());
	}

	// ==============================
	// Get Latest Available Date
	// ==============================

	public Date getLatestReportDate() {
		String sql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12C_SUMMARYTABLE";
		try {
			return jdbcTemplate.queryForObject(sql, Date.class);
		} catch (Exception e) {
			logger.warn("No data found in SUMMARYTABLE");
			return null;
		}
	}

	public Date getLatestDetailReportDate() {
		String sql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12C_DETAILTABLE";
		try {
			return jdbcTemplate.queryForObject(sql, Date.class);
		} catch (Exception e) {
			logger.warn("No data found in DETAILTABLE");
			return null;
		}
	}

//===========================
// ENTITY'S
//===========================

// ========================================
// M_SRWA_12C_Summary_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_Summary_EntityRowMapper implements RowMapper<M_SRWA_12C_Summary_Entity> {
		@Override
		public M_SRWA_12C_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_Summary_Entity obj = new M_SRWA_12C_Summary_Entity();

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

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

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

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

// ========================================
// M_SRWA_12C_Detail_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_Detail_EntityRowMapper implements RowMapper<M_SRWA_12C_Detail_Entity> {
		@Override
		public M_SRWA_12C_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_Detail_Entity obj = new M_SRWA_12C_Detail_Entity();

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

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

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

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

// ========================================
// M_SRWA_12C_Archival_Summary_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_Archival_Summary_EntityRowMapper implements RowMapper<M_SRWA_12C_Archival_Summary_Entity> {
		@Override
		public M_SRWA_12C_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_Archival_Summary_Entity obj = new M_SRWA_12C_Archival_Summary_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_Archival_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_Archival_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

// ========================================
// M_SRWA_12C_Archival_Detail_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_Archival_Detail_EntityRowMapper implements RowMapper<M_SRWA_12C_Archival_Detail_Entity> {
		@Override
		public M_SRWA_12C_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_Archival_Detail_Entity obj = new M_SRWA_12C_Archival_Detail_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_Archival_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_Archival_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

// ========================================
// M_SRWA_12C_RESUB_Summary_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_RESUB_Summary_EntityRowMapper implements RowMapper<M_SRWA_12C_RESUB_Summary_Entity> {
		@Override
		public M_SRWA_12C_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_RESUB_Summary_Entity obj = new M_SRWA_12C_RESUB_Summary_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_RESUB_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_RESUB_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

// ========================================
// M_SRWA_12C_RESUB_Detail_Entity - ROW MAPPER
// ========================================

	public class M_SRWA_12C_RESUB_Detail_EntityRowMapper implements RowMapper<M_SRWA_12C_RESUB_Detail_Entity> {
		@Override
		public M_SRWA_12C_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12C_RESUB_Detail_Entity obj = new M_SRWA_12C_RESUB_Detail_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R11
			// =========================
			obj.setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R12
			// =========================
			obj.setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR12_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R12_NUMBER_OF_FAILED_TRADES"));
			obj.setR12_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R12_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR12_RISK_MULTIPLIER(rs.getBigDecimal("R12_RISK_MULTIPLIER"));
			obj.setR12_RWA(rs.getBigDecimal("R12_RWA"));

			// =========================
			// R13
			// =========================
			obj.setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR13_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R13_NUMBER_OF_FAILED_TRADES"));
			obj.setR13_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R13_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR13_RISK_MULTIPLIER(rs.getBigDecimal("R13_RISK_MULTIPLIER"));
			obj.setR13_RWA(rs.getBigDecimal("R13_RWA"));

			// =========================
			// R14
			// =========================
			obj.setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR14_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R14_NUMBER_OF_FAILED_TRADES"));
			obj.setR14_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R14_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR14_RISK_MULTIPLIER(rs.getBigDecimal("R14_RISK_MULTIPLIER"));
			obj.setR14_RWA(rs.getBigDecimal("R14_RWA"));

			// =========================
			// R15
			// =========================
			obj.setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR15_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R15_NUMBER_OF_FAILED_TRADES"));
			obj.setR15_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R15_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR15_RISK_MULTIPLIER(rs.getBigDecimal("R15_RISK_MULTIPLIER"));
			obj.setR15_RWA(rs.getBigDecimal("R15_RWA"));

			// =========================
			// R16
			// =========================
			obj.setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR16_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R16_NUMBER_OF_FAILED_TRADES"));
			obj.setR16_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R16_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR16_RWA(rs.getBigDecimal("R16_RWA"));

			// =========================
			// R19
			// =========================
			obj.setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));

			// =========================
			// R20
			// =========================
			obj.setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR20_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R20_NUMBER_OF_FAILED_TRADES"));
			obj.setR20_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R20_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR20_RISK_MULTIPLIER(rs.getBigDecimal("R20_RISK_MULTIPLIER"));
			obj.setR20_RWA(rs.getBigDecimal("R20_RWA"));

			// =========================
			// R21
			// =========================
			obj.setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR21_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R21_NUMBER_OF_FAILED_TRADES"));
			obj.setR21_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R21_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR21_RISK_MULTIPLIER(rs.getBigDecimal("R21_RISK_MULTIPLIER"));
			obj.setR21_RWA(rs.getBigDecimal("R21_RWA"));

			// =========================
			// R22
			// =========================
			obj.setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR22_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R22_NUMBER_OF_FAILED_TRADES"));
			obj.setR22_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R22_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR22_RISK_MULTIPLIER(rs.getBigDecimal("R22_RISK_MULTIPLIER"));
			obj.setR22_RWA(rs.getBigDecimal("R22_RWA"));

			// =========================
			// R23
			// =========================
			obj.setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR23_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R23_NUMBER_OF_FAILED_TRADES"));
			obj.setR23_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R23_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR23_RISK_MULTIPLIER(rs.getBigDecimal("R23_RISK_MULTIPLIER"));
			obj.setR23_RWA(rs.getBigDecimal("R23_RWA"));

			// =========================
			// R24
			// =========================
			obj.setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR24_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R24_NUMBER_OF_FAILED_TRADES"));
			obj.setR24_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R24_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR24_RISK_MULTIPLIER(rs.getBigDecimal("R24_RISK_MULTIPLIER"));
			obj.setR24_RWA(rs.getBigDecimal("R24_RWA"));

			// =========================
			// R25
			// =========================
			obj.setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR25_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R25_NUMBER_OF_FAILED_TRADES"));
			obj.setR25_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R25_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR25_RISK_MULTIPLIER(rs.getBigDecimal("R25_RISK_MULTIPLIER"));
			obj.setR25_RWA(rs.getBigDecimal("R25_RWA"));

			// =========================
			// R26
			// =========================
			obj.setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR26_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R26_NUMBER_OF_FAILED_TRADES"));
			obj.setR26_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R26_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR26_RISK_MULTIPLIER(rs.getBigDecimal("R26_RISK_MULTIPLIER"));
			obj.setR26_RWA(rs.getBigDecimal("R26_RWA"));

			// =========================
			// R27
			// =========================
			obj.setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR27_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R27_NUMBER_OF_FAILED_TRADES"));
			obj.setR27_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R27_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR27_RWA(rs.getBigDecimal("R27_RWA"));

			// =========================
			// R28
			// =========================
			obj.setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
					rs.getString("R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE"));
			obj.setR28_NUMBER_OF_FAILED_TRADES(rs.getBigDecimal("R28_NUMBER_OF_FAILED_TRADES"));
			obj.setR28_POSITIVE_CURRENT_EXPOSURE(rs.getBigDecimal("R28_POSITIVE_CURRENT_EXPOSURE"));
			obj.setR28_RWA(rs.getBigDecimal("R28_RWA"));

			return obj;
		}
	}

	public class M_SRWA_12C_RESUB_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R11
		// =========================
		private String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R12
		// =========================
		private String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R12_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R12_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R12_RISK_MULTIPLIER;
		private BigDecimal R12_RWA;

		// =========================
		// R13
		// =========================
		private String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R13_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R13_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R13_RISK_MULTIPLIER;
		private BigDecimal R13_RWA;

		// =========================
		// R14
		// =========================
		private String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R14_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R14_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R14_RISK_MULTIPLIER;
		private BigDecimal R14_RWA;

		// =========================
		// R15
		// =========================
		private String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R15_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R15_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R15_RISK_MULTIPLIER;
		private BigDecimal R15_RWA;

		// =========================
		// R16
		// =========================
		private String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R16_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R16_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R16_RWA;

		// =========================
		// R19
		// =========================
		private String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;

		// =========================
		// R20
		// =========================
		private String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R20_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R20_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R20_RISK_MULTIPLIER;
		private BigDecimal R20_RWA;

		// =========================
		// R21
		// =========================
		private String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R21_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R21_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R21_RISK_MULTIPLIER;
		private BigDecimal R21_RWA;

		// =========================
		// R22
		// =========================
		private String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R22_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R22_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R22_RISK_MULTIPLIER;
		private BigDecimal R22_RWA;

		// =========================
		// R23
		// =========================
		private String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R23_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R23_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R23_RISK_MULTIPLIER;
		private BigDecimal R23_RWA;

		// =========================
		// R24
		// =========================
		private String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R24_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R24_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R24_RISK_MULTIPLIER;
		private BigDecimal R24_RWA;

		// =========================
		// R25
		// =========================
		private String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R25_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R25_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R25_RISK_MULTIPLIER;
		private BigDecimal R25_RWA;

		// =========================
		// R26
		// =========================
		private String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R26_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R26_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R26_RISK_MULTIPLIER;
		private BigDecimal R26_RWA;

		// =========================
		// R27
		// =========================
		private String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R27_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R27_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R27_RWA;

		// =========================
		// R28
		// =========================
		private String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		private BigDecimal R28_NUMBER_OF_FAILED_TRADES;
		private BigDecimal R28_POSITIVE_CURRENT_EXPOSURE;
		private BigDecimal R28_RWA;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12C_RESUB_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
		public Date getReport_date() {
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

		// R11
		public String getR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R11_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R12
		public String getR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R12_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR12_NUMBER_OF_FAILED_TRADES() {
			return R12_NUMBER_OF_FAILED_TRADES;
		}

		public void setR12_NUMBER_OF_FAILED_TRADES(BigDecimal R12_NUMBER_OF_FAILED_TRADES) {
			this.R12_NUMBER_OF_FAILED_TRADES = R12_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR12_POSITIVE_CURRENT_EXPOSURE() {
			return R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR12_POSITIVE_CURRENT_EXPOSURE(BigDecimal R12_POSITIVE_CURRENT_EXPOSURE) {
			this.R12_POSITIVE_CURRENT_EXPOSURE = R12_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR12_RISK_MULTIPLIER() {
			return R12_RISK_MULTIPLIER;
		}

		public void setR12_RISK_MULTIPLIER(BigDecimal R12_RISK_MULTIPLIER) {
			this.R12_RISK_MULTIPLIER = R12_RISK_MULTIPLIER;
		}

		public BigDecimal getR12_RWA() {
			return R12_RWA;
		}

		public void setR12_RWA(BigDecimal R12_RWA) {
			this.R12_RWA = R12_RWA;
		}

		// R13
		public String getR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R13_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR13_NUMBER_OF_FAILED_TRADES() {
			return R13_NUMBER_OF_FAILED_TRADES;
		}

		public void setR13_NUMBER_OF_FAILED_TRADES(BigDecimal R13_NUMBER_OF_FAILED_TRADES) {
			this.R13_NUMBER_OF_FAILED_TRADES = R13_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR13_POSITIVE_CURRENT_EXPOSURE() {
			return R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR13_POSITIVE_CURRENT_EXPOSURE(BigDecimal R13_POSITIVE_CURRENT_EXPOSURE) {
			this.R13_POSITIVE_CURRENT_EXPOSURE = R13_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR13_RISK_MULTIPLIER() {
			return R13_RISK_MULTIPLIER;
		}

		public void setR13_RISK_MULTIPLIER(BigDecimal R13_RISK_MULTIPLIER) {
			this.R13_RISK_MULTIPLIER = R13_RISK_MULTIPLIER;
		}

		public BigDecimal getR13_RWA() {
			return R13_RWA;
		}

		public void setR13_RWA(BigDecimal R13_RWA) {
			this.R13_RWA = R13_RWA;
		}

		// R14
		public String getR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R14_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR14_NUMBER_OF_FAILED_TRADES() {
			return R14_NUMBER_OF_FAILED_TRADES;
		}

		public void setR14_NUMBER_OF_FAILED_TRADES(BigDecimal R14_NUMBER_OF_FAILED_TRADES) {
			this.R14_NUMBER_OF_FAILED_TRADES = R14_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR14_POSITIVE_CURRENT_EXPOSURE() {
			return R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR14_POSITIVE_CURRENT_EXPOSURE(BigDecimal R14_POSITIVE_CURRENT_EXPOSURE) {
			this.R14_POSITIVE_CURRENT_EXPOSURE = R14_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR14_RISK_MULTIPLIER() {
			return R14_RISK_MULTIPLIER;
		}

		public void setR14_RISK_MULTIPLIER(BigDecimal R14_RISK_MULTIPLIER) {
			this.R14_RISK_MULTIPLIER = R14_RISK_MULTIPLIER;
		}

		public BigDecimal getR14_RWA() {
			return R14_RWA;
		}

		public void setR14_RWA(BigDecimal R14_RWA) {
			this.R14_RWA = R14_RWA;
		}

		// R15
		public String getR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R15_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR15_NUMBER_OF_FAILED_TRADES() {
			return R15_NUMBER_OF_FAILED_TRADES;
		}

		public void setR15_NUMBER_OF_FAILED_TRADES(BigDecimal R15_NUMBER_OF_FAILED_TRADES) {
			this.R15_NUMBER_OF_FAILED_TRADES = R15_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR15_POSITIVE_CURRENT_EXPOSURE() {
			return R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR15_POSITIVE_CURRENT_EXPOSURE(BigDecimal R15_POSITIVE_CURRENT_EXPOSURE) {
			this.R15_POSITIVE_CURRENT_EXPOSURE = R15_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR15_RISK_MULTIPLIER() {
			return R15_RISK_MULTIPLIER;
		}

		public void setR15_RISK_MULTIPLIER(BigDecimal R15_RISK_MULTIPLIER) {
			this.R15_RISK_MULTIPLIER = R15_RISK_MULTIPLIER;
		}

		public BigDecimal getR15_RWA() {
			return R15_RWA;
		}

		public void setR15_RWA(BigDecimal R15_RWA) {
			this.R15_RWA = R15_RWA;
		}

		// R16
		public String getR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R16_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR16_NUMBER_OF_FAILED_TRADES() {
			return R16_NUMBER_OF_FAILED_TRADES;
		}

		public void setR16_NUMBER_OF_FAILED_TRADES(BigDecimal R16_NUMBER_OF_FAILED_TRADES) {
			this.R16_NUMBER_OF_FAILED_TRADES = R16_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR16_POSITIVE_CURRENT_EXPOSURE() {
			return R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR16_POSITIVE_CURRENT_EXPOSURE(BigDecimal R16_POSITIVE_CURRENT_EXPOSURE) {
			this.R16_POSITIVE_CURRENT_EXPOSURE = R16_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR16_RWA() {
			return R16_RWA;
		}

		public void setR16_RWA(BigDecimal R16_RWA) {
			this.R16_RWA = R16_RWA;
		}

		// R19
		public String getR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R19_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		// R20
		public String getR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R20_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR20_NUMBER_OF_FAILED_TRADES() {
			return R20_NUMBER_OF_FAILED_TRADES;
		}

		public void setR20_NUMBER_OF_FAILED_TRADES(BigDecimal R20_NUMBER_OF_FAILED_TRADES) {
			this.R20_NUMBER_OF_FAILED_TRADES = R20_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR20_POSITIVE_CURRENT_EXPOSURE() {
			return R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR20_POSITIVE_CURRENT_EXPOSURE(BigDecimal R20_POSITIVE_CURRENT_EXPOSURE) {
			this.R20_POSITIVE_CURRENT_EXPOSURE = R20_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR20_RISK_MULTIPLIER() {
			return R20_RISK_MULTIPLIER;
		}

		public void setR20_RISK_MULTIPLIER(BigDecimal R20_RISK_MULTIPLIER) {
			this.R20_RISK_MULTIPLIER = R20_RISK_MULTIPLIER;
		}

		public BigDecimal getR20_RWA() {
			return R20_RWA;
		}

		public void setR20_RWA(BigDecimal R20_RWA) {
			this.R20_RWA = R20_RWA;
		}

		// R21
		public String getR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R21_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR21_NUMBER_OF_FAILED_TRADES() {
			return R21_NUMBER_OF_FAILED_TRADES;
		}

		public void setR21_NUMBER_OF_FAILED_TRADES(BigDecimal R21_NUMBER_OF_FAILED_TRADES) {
			this.R21_NUMBER_OF_FAILED_TRADES = R21_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR21_POSITIVE_CURRENT_EXPOSURE() {
			return R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR21_POSITIVE_CURRENT_EXPOSURE(BigDecimal R21_POSITIVE_CURRENT_EXPOSURE) {
			this.R21_POSITIVE_CURRENT_EXPOSURE = R21_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR21_RISK_MULTIPLIER() {
			return R21_RISK_MULTIPLIER;
		}

		public void setR21_RISK_MULTIPLIER(BigDecimal R21_RISK_MULTIPLIER) {
			this.R21_RISK_MULTIPLIER = R21_RISK_MULTIPLIER;
		}

		public BigDecimal getR21_RWA() {
			return R21_RWA;
		}

		public void setR21_RWA(BigDecimal R21_RWA) {
			this.R21_RWA = R21_RWA;
		}

		// R22
		public String getR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R22_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR22_NUMBER_OF_FAILED_TRADES() {
			return R22_NUMBER_OF_FAILED_TRADES;
		}

		public void setR22_NUMBER_OF_FAILED_TRADES(BigDecimal R22_NUMBER_OF_FAILED_TRADES) {
			this.R22_NUMBER_OF_FAILED_TRADES = R22_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR22_POSITIVE_CURRENT_EXPOSURE() {
			return R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR22_POSITIVE_CURRENT_EXPOSURE(BigDecimal R22_POSITIVE_CURRENT_EXPOSURE) {
			this.R22_POSITIVE_CURRENT_EXPOSURE = R22_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR22_RISK_MULTIPLIER() {
			return R22_RISK_MULTIPLIER;
		}

		public void setR22_RISK_MULTIPLIER(BigDecimal R22_RISK_MULTIPLIER) {
			this.R22_RISK_MULTIPLIER = R22_RISK_MULTIPLIER;
		}

		public BigDecimal getR22_RWA() {
			return R22_RWA;
		}

		public void setR22_RWA(BigDecimal R22_RWA) {
			this.R22_RWA = R22_RWA;
		}

		// R23
		public String getR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R23_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR23_NUMBER_OF_FAILED_TRADES() {
			return R23_NUMBER_OF_FAILED_TRADES;
		}

		public void setR23_NUMBER_OF_FAILED_TRADES(BigDecimal R23_NUMBER_OF_FAILED_TRADES) {
			this.R23_NUMBER_OF_FAILED_TRADES = R23_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR23_POSITIVE_CURRENT_EXPOSURE() {
			return R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR23_POSITIVE_CURRENT_EXPOSURE(BigDecimal R23_POSITIVE_CURRENT_EXPOSURE) {
			this.R23_POSITIVE_CURRENT_EXPOSURE = R23_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR23_RISK_MULTIPLIER() {
			return R23_RISK_MULTIPLIER;
		}

		public void setR23_RISK_MULTIPLIER(BigDecimal R23_RISK_MULTIPLIER) {
			this.R23_RISK_MULTIPLIER = R23_RISK_MULTIPLIER;
		}

		public BigDecimal getR23_RWA() {
			return R23_RWA;
		}

		public void setR23_RWA(BigDecimal R23_RWA) {
			this.R23_RWA = R23_RWA;
		}

		// R24
		public String getR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R24_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR24_NUMBER_OF_FAILED_TRADES() {
			return R24_NUMBER_OF_FAILED_TRADES;
		}

		public void setR24_NUMBER_OF_FAILED_TRADES(BigDecimal R24_NUMBER_OF_FAILED_TRADES) {
			this.R24_NUMBER_OF_FAILED_TRADES = R24_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR24_POSITIVE_CURRENT_EXPOSURE() {
			return R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR24_POSITIVE_CURRENT_EXPOSURE(BigDecimal R24_POSITIVE_CURRENT_EXPOSURE) {
			this.R24_POSITIVE_CURRENT_EXPOSURE = R24_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR24_RISK_MULTIPLIER() {
			return R24_RISK_MULTIPLIER;
		}

		public void setR24_RISK_MULTIPLIER(BigDecimal R24_RISK_MULTIPLIER) {
			this.R24_RISK_MULTIPLIER = R24_RISK_MULTIPLIER;
		}

		public BigDecimal getR24_RWA() {
			return R24_RWA;
		}

		public void setR24_RWA(BigDecimal R24_RWA) {
			this.R24_RWA = R24_RWA;
		}

		// R25
		public String getR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R25_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR25_NUMBER_OF_FAILED_TRADES() {
			return R25_NUMBER_OF_FAILED_TRADES;
		}

		public void setR25_NUMBER_OF_FAILED_TRADES(BigDecimal R25_NUMBER_OF_FAILED_TRADES) {
			this.R25_NUMBER_OF_FAILED_TRADES = R25_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR25_POSITIVE_CURRENT_EXPOSURE() {
			return R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR25_POSITIVE_CURRENT_EXPOSURE(BigDecimal R25_POSITIVE_CURRENT_EXPOSURE) {
			this.R25_POSITIVE_CURRENT_EXPOSURE = R25_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR25_RISK_MULTIPLIER() {
			return R25_RISK_MULTIPLIER;
		}

		public void setR25_RISK_MULTIPLIER(BigDecimal R25_RISK_MULTIPLIER) {
			this.R25_RISK_MULTIPLIER = R25_RISK_MULTIPLIER;
		}

		public BigDecimal getR25_RWA() {
			return R25_RWA;
		}

		public void setR25_RWA(BigDecimal R25_RWA) {
			this.R25_RWA = R25_RWA;
		}

		// R26
		public String getR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R26_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR26_NUMBER_OF_FAILED_TRADES() {
			return R26_NUMBER_OF_FAILED_TRADES;
		}

		public void setR26_NUMBER_OF_FAILED_TRADES(BigDecimal R26_NUMBER_OF_FAILED_TRADES) {
			this.R26_NUMBER_OF_FAILED_TRADES = R26_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR26_POSITIVE_CURRENT_EXPOSURE() {
			return R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR26_POSITIVE_CURRENT_EXPOSURE(BigDecimal R26_POSITIVE_CURRENT_EXPOSURE) {
			this.R26_POSITIVE_CURRENT_EXPOSURE = R26_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR26_RISK_MULTIPLIER() {
			return R26_RISK_MULTIPLIER;
		}

		public void setR26_RISK_MULTIPLIER(BigDecimal R26_RISK_MULTIPLIER) {
			this.R26_RISK_MULTIPLIER = R26_RISK_MULTIPLIER;
		}

		public BigDecimal getR26_RWA() {
			return R26_RWA;
		}

		public void setR26_RWA(BigDecimal R26_RWA) {
			this.R26_RWA = R26_RWA;
		}

		// R27
		public String getR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R27_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR27_NUMBER_OF_FAILED_TRADES() {
			return R27_NUMBER_OF_FAILED_TRADES;
		}

		public void setR27_NUMBER_OF_FAILED_TRADES(BigDecimal R27_NUMBER_OF_FAILED_TRADES) {
			this.R27_NUMBER_OF_FAILED_TRADES = R27_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR27_POSITIVE_CURRENT_EXPOSURE() {
			return R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR27_POSITIVE_CURRENT_EXPOSURE(BigDecimal R27_POSITIVE_CURRENT_EXPOSURE) {
			this.R27_POSITIVE_CURRENT_EXPOSURE = R27_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR27_RWA() {
			return R27_RWA;
		}

		public void setR27_RWA(BigDecimal R27_RWA) {
			this.R27_RWA = R27_RWA;
		}

		// R28
		public String getR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE() {
			return R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public void setR28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE(
				String R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE) {
			this.R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE = R28_NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE;
		}

		public BigDecimal getR28_NUMBER_OF_FAILED_TRADES() {
			return R28_NUMBER_OF_FAILED_TRADES;
		}

		public void setR28_NUMBER_OF_FAILED_TRADES(BigDecimal R28_NUMBER_OF_FAILED_TRADES) {
			this.R28_NUMBER_OF_FAILED_TRADES = R28_NUMBER_OF_FAILED_TRADES;
		}

		public BigDecimal getR28_POSITIVE_CURRENT_EXPOSURE() {
			return R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public void setR28_POSITIVE_CURRENT_EXPOSURE(BigDecimal R28_POSITIVE_CURRENT_EXPOSURE) {
			this.R28_POSITIVE_CURRENT_EXPOSURE = R28_POSITIVE_CURRENT_EXPOSURE;
		}

		public BigDecimal getR28_RWA() {
			return R28_RWA;
		}

		public void setR28_RWA(BigDecimal R28_RWA) {
			this.R28_RWA = R28_RWA;
		}
	}

	// =======================
	// MODEL AND VIEW
	// =======================

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SRWA_12CView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();
		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12C_Archival_Summary_Entity> T1Master = getArchivalSummaryDataByDateAndVersion(d1, version);

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12C_RESUB_Summary_Entity> T1Master = getResubSummaryDataByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12C_Summary_Entity> T1Master = new ArrayList<M_SRWA_12C_Summary_Entity>();
				try {
					Date d2 = dateformat.parse(todate);
					T1Master = getSummaryDataByDate(dateformat.parse(todate));
					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "summary");

					mv.addObject("report_date", dateformat.format(d2));

				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12C_Archival_Detail_Entity> T1Master = getArchivalDetailDataByDateAndVersion(d1,
							version);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {
					List<M_SRWA_12C_RESUB_Detail_Entity> T1Master = getResubDetailDataByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SRWA_12C_Detail_Entity> T1Master = getDetailDataByDate(dateformat.parse(todate));

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12C");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	// Archival View
	public List<Object[]> getM_SRWA_12CArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			List<M_SRWA_12C_Archival_Summary_Entity> repoData = getArchivalSummaryDataWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12C_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12C_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12C Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Resub View
	public List<Object[]> getM_SRWA_12CResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<M_SRWA_12C_RESUB_Summary_Entity> latestArchivalList = getResubSummaryDataWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12C_RESUB_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12C Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// ========================================
	// UPDATE REPORT METHOD
	// ========================================

	public void updateReport(M_SRWA_12C_Summary_Entity updatedEntity) {
		System.out.println("Came to services");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Date reportDate = updatedEntity.getReport_date();

		// ✅ Fetch existing summary using JDBC method
		List<M_SRWA_12C_Summary_Entity> existingList = getSummaryDataByDate(reportDate);

		// ✅ Fetch existing detail using JDBC method
		List<M_SRWA_12C_Detail_Entity> existingDetailList = getDetailDataByDate(reportDate);
		M_SRWA_12C_Detail_Entity existingDetail;

		if (!existingDetailList.isEmpty()) {
			existingDetail = existingDetailList.get(0);
		} else {
			existingDetail = new M_SRWA_12C_Detail_Entity();
			existingDetail.setReport_date(reportDate);
		}

		M_SRWA_12C_Summary_Entity existing;

		if (!existingList.isEmpty()) {
			existing = existingList.get(0);
			System.out.println(" Existing record found for date: " + reportDate);
		} else {
			existing = new M_SRWA_12C_Summary_Entity();
			existing.setReport_date(reportDate);
			System.out.println("⚠️ No record found — creating new entry for date: " + reportDate);
		}

		try {
			// -------------------------------
			// ✅ COLUMN C
			// -------------------------------
			int[] cRows = { 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 };
			for (int row : cRows) {
				String prefix = "R" + row + "_";
				String[] fields = { "NUMBER_OF_WORKING_DAYS_AFTER_THE_AGREED_SETTLEMENT_DATE" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12C_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12C_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);

					} catch (NoSuchMethodException e) {
						continue; // Skip missing field
					}
				}
			}

			// -------------------------------
			// ✅ COLUMNS D, E, F, G
			// -------------------------------
			int[] commonRows = { 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 25, 26, 27, 28 };

			for (int row : commonRows) {
				String prefix = "R" + row + "_";

				String[] fields = { "NUMBER_OF_FAILED_TRADES", "POSITIVE_CURRENT_EXPOSURE", "RISK_MULTIPLIER", "RWA" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						Method getter = M_SRWA_12C_Summary_Entity.class.getMethod(getterName);
						Method setter = M_SRWA_12C_Summary_Entity.class.getMethod(setterName, getter.getReturnType());
						Method detailSetter = M_SRWA_12C_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

						Object newValue = getter.invoke(updatedEntity);
						setter.invoke(existing, newValue);
						detailSetter.invoke(existingDetail, newValue);

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// ✅ Save using JDBC methods
		saveOrUpdateSummary(existing);
		saveOrUpdateDetail(existingDetail);
	}

	// ============================
	// SAVE/UPDATE METHODS FOR SUMMARY
	// ============================

	private void saveOrUpdateSummary(M_SRWA_12C_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_SUMMARYTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_SUMMARYTABLE SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? " + "WHERE REPORT_DATE = ?";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_SUMMARYTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary inserted for date: " + entity.getReport_date());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR DETAIL
	// ============================

	private void saveOrUpdateDetail(M_SRWA_12C_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_DETAILTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_DETAILTABLE SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? " + "WHERE REPORT_DATE = ?";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_DETAILTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail inserted for date: " + entity.getReport_date());
		}
	}

	// ========================================
	// UPDATE RESUB REPORT METHOD
	// ========================================

	public void updateResubReport(M_SRWA_12C_RESUB_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE USING JDBC
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxResubSummaryVersion(reportDate);

		if (maxResubVer == null)
			throw new RuntimeException("No record for: " + reportDate);

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);

		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12C_RESUB_Summary_Entity resubSummary = new M_SRWA_12C_RESUB_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "reportDate", "reportVersion", "reportResubDate");

		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12C_RESUB_Detail_Entity resubDetail = new M_SRWA_12C_RESUB_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "reportDate", "reportVersion", "reportResubDate");

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12C_Archival_Summary_Entity archSummary = new M_SRWA_12C_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "reportDate", "reportVersion", "reportResubDate");

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion); // SAME VERSION
		archSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12C_Archival_Detail_Entity archDetail = new M_SRWA_12C_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "reportDate", "reportVersion", "reportResubDate");

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion); // SAME VERSION
		archDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ SAVE ALL WITH JDBC METHODS
		// ====================================================

		saveOrUpdateResubSummary(resubSummary);
		saveOrUpdateResubDetail(resubDetail);
		saveOrUpdateArchivalSummary(archSummary);
		saveOrUpdateArchivalDetail(archDetail);
	}

	// ============================
	// SAVE/UPDATE METHODS FOR RESUB SUMMARY
	// ============================

	private void saveOrUpdateResubSummary(M_SRWA_12C_RESUB_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR RESUB DETAIL
	// ============================

	private void saveOrUpdateResubDetail(M_SRWA_12C_RESUB_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_RESUB_DETAILTABLE SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_RESUB_DETAILTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY
	// ============================

	private void saveOrUpdateArchivalSummary(M_SRWA_12C_Archival_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL
	// ============================

	private void saveOrUpdateArchivalDetail(M_SRWA_12C_Archival_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12C_ARCHIVALTABLE_DETAIL "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ========================================
	// UPDATE REPORT RESUB METHOD
	// ========================================

	public void updateReportResub(M_SRWA_12C_Summary_Entity updatedEntity) {
		System.out.println("Came to Resub Service");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Use entity field directly (same name as in entity)
		Date report_date = updatedEntity.getReport_date();
		BigDecimal newVersion = BigDecimal.ONE;

		try {
			// ✅ Get latest archival version using JDBC method
			Optional<M_SRWA_12C_Archival_Summary_Entity> latestArchivalOpt = getLatestArchivalSummaryVersionByDate(
					report_date);

			// Determine next version
			if (latestArchivalOpt.isPresent()) {
				M_SRWA_12C_Archival_Summary_Entity latestArchival = latestArchivalOpt.get();
				try {
					newVersion = latestArchival.getReport_version().add(BigDecimal.ONE);
				} catch (NumberFormatException e) {
					System.err.println("Invalid version format. Defaulting to version 1");
					newVersion = BigDecimal.ONE;
				}
			} else {
				System.out.println("No previous archival found for date: " + report_date);
			}

			// ✅ Check if version exists using JDBC method
			Optional<M_SRWA_12C_Archival_Summary_Entity> existingOpt = findArchivalSummaryByReportDateAndVersion(
					report_date, newVersion);

			if (existingOpt.isPresent()) {
				throw new RuntimeException("Version " + newVersion + " already exists for report date " + report_date);
			}

			// Copy summary entity to archival entity
			M_SRWA_12C_Archival_Summary_Entity archivalEntity = new M_SRWA_12C_Archival_Summary_Entity();
			org.springframework.beans.BeanUtils.copyProperties(updatedEntity, archivalEntity);

			archivalEntity.setReport_date(report_date);
			archivalEntity.setReport_version(newVersion);
			archivalEntity.setReportResubDate(new Date());

			System.out.println("Saving new archival version: " + newVersion);

			// ✅ Save using JDBC method
			saveOrUpdateArchivalSummary(archivalEntity);

			System.out.println("Saved archival version successfully: " + newVersion);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while creating archival resubmission record", e);
		}
	}

	// NORMAL FORMAT EXCEL
	public byte[] getBRRS_M_SRWA_12CExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= DOWNLOAD DETAILS =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		// ARCHIVAL check

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_SRWA_12CARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12CResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		else {
			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12CEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data
				List<M_SRWA_12C_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				// If no data, try to get latest available date
				if (dataList.isEmpty()) {
					Date latestDate = getLatestReportDate();
					if (latestDate != null) {
						dataList = getSummaryDataByDate(latestDate);
						logger.info("No data for requested date {}. Using latest available date: {}", todate,
								latestDate);
					}
				}

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
					throw new SecurityException("Template file exists but is not readable (check permissions): "
							+ templatePath.toAbsolutePath());
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
					percentStyle.setAlignment(HorizontalAlignment.CENTER);
					// --- End of Style Definitions ---

					try {

						// Row 6 = Excel row 7
						Row dateRow = sheet.getRow(6);

						if (dateRow == null) {
							dateRow = sheet.createRow(6);
						}

						// Column 3 = Excel column D
						Cell dateCell = dateRow.getCell(2);

						if (dateCell == null) {
							dateCell = dateRow.createCell(2);
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

					int startRow = 11;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SRWA_12C_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row12
							// Column D
							Cell cell3 = row.createCell(3);
							if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							// Column D
							cell3 = row.createCell(3);
							if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
								cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
								cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

						}
						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C SUMMARY", null,
								"BRRS_M_SRWA_12C_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_SRWA_12CEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		List<M_SRWA_12C_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

		// If no data, try to get latest available date
		if (dataList.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				dataList = getSummaryDataByDate(latestDate);
				logger.info("No data for requested date {}. Using latest available date: {}", todate, latestDate);
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 3 = Excel column D
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C EMAIL SUMMARY", null,
						"BRRS_M_SRWA_12C_SUMMARYTABLE");
			}
			return out.toByteArray();

		}

	}

	// Archival Format Excel
	public byte[] getExcelM_SRWA_12CARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12CARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		logger.info("Service: Starting Excel generation for Archival Format .");

		List<M_SRWA_12C_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available version
		if (dataList.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				// Get latest version for this date
				Optional<M_SRWA_12C_Archival_Summary_Entity> latestOpt = getLatestArchivalSummaryVersionByDate(
						latestDate);
				if (latestOpt.isPresent()) {
					dataList = getArchivalSummaryDataByDateAndVersion(latestDate, latestOpt.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt.get().getReport_version());
				}
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12C report. Returning empty result.");
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

				// Column 3 = Excel column D
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C ARCHIVAL SUMMARY", null,
						"BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_SRWA_12CARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process for Archival Email.");

		List<M_SRWA_12C_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available version
		if (dataList.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				Optional<M_SRWA_12C_Archival_Summary_Entity> latestOpt = getLatestArchivalSummaryVersionByDate(
						latestDate);
				if (latestOpt.isPresent()) {
					dataList = getArchivalSummaryDataByDateAndVersion(latestDate, latestOpt.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt.get().getReport_version());
				}
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 3 = Excel column D
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_SRWA_12C_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();

		}

	}

	// Resub Format Excel
	public byte[] BRRS_M_SRWA_12CResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12CResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		logger.info("Service: Starting Excel generation process in memory for RESUB Format Excel.");

		List<M_SRWA_12C_RESUB_Summary_Entity> dataList = getResubSummaryDataByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12Creport. Returning empty result.");
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

				// Column 3 = Excel column D
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12C_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C RESUB SUMMARY", null,
						"BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_SRWA_12CResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB EMAIL Excel generation.");

		List<M_SRWA_12C_RESUB_Summary_Entity> dataList = getResubSummaryDataByDateAndVersion(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12C report. Returning empty result.");
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
			percentStyle.setAlignment(HorizontalAlignment.CENTER);
			// --- End of Style Definitions ---

			try {

				// Row 6 = Excel row 7
				Row dateRow = sheet.getRow(6);

				if (dateRow == null) {
					dateRow = sheet.createRow(6);
				}

				// Column 3 = Excel column D
				Cell dateCell = dateRow.getCell(2);

				if (dateCell == null) {
					dateCell = dateRow.createCell(2);
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

			int startRow = 11;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12C_RESUB_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row12
					// Column D
					Cell cell3 = row.createCell(3);
					if (record.getR12_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR12_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR12_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR12_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR13_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR13_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR13_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR14_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR14_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR14_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR15_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR15_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR15_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR20_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR20_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR20_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR21_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR21_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR21_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR22_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR22_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR22_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR23_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR23_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR23_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR24_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR24_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR24_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR25_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR25_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR25_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					// Column D
					cell3 = row.createCell(3);
					if (record.getR26_NUMBER_OF_FAILED_TRADES() != null) {
						cell3.setCellValue(record.getR26_NUMBER_OF_FAILED_TRADES().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_POSITIVE_CURRENT_EXPOSURE() != null) {
						cell4.setCellValue(record.getR26_POSITIVE_CURRENT_EXPOSURE().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12C EMAIL RESUB SUMMARY", null,
						"BRRS_M_SRWA_12C_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();

		}

	}

}
