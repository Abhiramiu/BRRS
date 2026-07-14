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

public class BRRS_M_SRWA_12F_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12F_ReportService.class);

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

	// ===================
	// JDBC REPOSITORIES
	// ===================

	// =====================================================
	// SUMMARY REPO
	// =====================================================

	public List<M_SRWA_12F_Summary_Entity> getSummarydatabydateList(Date rpt_date) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new M_SRWA_12F_Summary_RowMapper());
	}

	// =====================================================
	// DETAIL REPO
	// =====================================================

	public List<M_SRWA_12F_Detail_Entity> getDetaildatabydateList(Date report_date) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { report_date }, new M_SRWA_12F_Detail_RowMapper());
	}

	// =====================================================
	// ARCHIVAL SUMMARY REPO
	// =====================================================

	public List<Object> getArchivalSummaryList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SRWA_12F_Archival_Summary_Entity> getArchivalSummarydatabydateList(Date report_date,
			BigDecimal report_version) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12F_Archival_Summary_RowMapper());
	}

	public List<M_SRWA_12F_Archival_Summary_Entity> getArchivalSummaryListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_SRWA_12F_Archival_Summary_RowMapper());
	}

	public BigDecimal findMaxVersionArchivalSummary(Date date) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	// =====================================================
	// ARCHIVAL DETAIL REPO
	// =====================================================

	public List<Object> getArchivalDetailList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SRWA_12F_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date report_date,
			BigDecimal report_version) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12F_Archival_Detail_RowMapper());
	}

	public List<M_SRWA_12F_Archival_Detail_Entity> getArchivalDetailListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_SRWA_12F_Archival_Detail_RowMapper());
	}

	public BigDecimal findMaxVersionArchivalDetail(Date date) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	// =====================================================
	// RESUB SUMMARY REPO
	// =====================================================

	public List<Object> getResubSummaryList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SRWA_12F_Resub_Summary_Entity> getResubSummarydatabydateList(Date report_date,
			BigDecimal report_version) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12F_Resub_Summary_RowMapper());
	}

	public List<M_SRWA_12F_Resub_Summary_Entity> getResubSummaryListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_SRWA_12F_Resub_Summary_RowMapper());
	}

	public BigDecimal findMaxVersionResubSummary(Date date) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	// =====================================================
	// RESUB DETAIL REPO
	// =====================================================

	public List<Object> getResubDetailList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12F_RESUB_DETAILTABLE ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql, Object.class);
	}

	public List<M_SRWA_12F_Resub_Detail_Entity> getResubDetaildatabydateList(Date report_date,
			BigDecimal report_version) {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12F_Resub_Detail_RowMapper());
	}

	public List<M_SRWA_12F_Resub_Detail_Entity> getResubDetailListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_SRWA_12F_RESUB_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_SRWA_12F_Resub_Detail_RowMapper());
	}

	public BigDecimal findMaxVersionResubDetail(Date date) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12F_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	// ========================
	// ENTITY ROW MAPPERS
	// ========================

	// =====================================================
	// M_SRWA_12F_Summary_RowMapper
	// =====================================================

	public class M_SRWA_12F_Summary_RowMapper implements RowMapper<M_SRWA_12F_Summary_Entity> {

		@Override
		public M_SRWA_12F_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Summary_Entity obj = new M_SRWA_12F_Summary_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

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

	// =====================================================
	// M_SRWA_12F_Summary_Entity
	// =====================================================

	public class M_SRWA_12F_Summary_Entity {

		@Temporal(TemporalType.DATE)
		@Id
		private Date report_date;
		private BigDecimal report_version;
		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Summary_Entity() {
			super();
		}

		// Getters and Setters
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

		// ================== R11 ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}

	}

	// =====================================================
	// M_SRWA_12F_Detail_RowMapper
	// =====================================================

	public class M_SRWA_12F_Detail_RowMapper implements RowMapper<M_SRWA_12F_Detail_Entity> {

		@Override
		public M_SRWA_12F_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Detail_Entity obj = new M_SRWA_12F_Detail_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

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

	// =====================================================
	// M_SRWA_12F_Detail_Entity
	// =====================================================

	public class M_SRWA_12F_Detail_Entity {

		@Temporal(TemporalType.DATE)
		@Id
		private Date report_date;
		private BigDecimal report_version;
		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Detail_Entity() {
			super();
		}

		// Getters and Setters
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

		// Getters and Setters for R11 to R37 (same as Summary Entity)
		// ================== R11 ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}
	}

	// =====================================================
	// M_SRWA_12F_Archival_Summary_RowMapper
	// =====================================================

	public class M_SRWA_12F_Archival_Summary_RowMapper implements RowMapper<M_SRWA_12F_Archival_Summary_Entity> {

		@Override
		public M_SRWA_12F_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Archival_Summary_Entity obj = new M_SRWA_12F_Archival_Summary_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setreport_resubdate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// =====================================================
	// M_SRWA_12F_Archival_Summary_Entity
	// =====================================================

	public class M_SRWA_12F_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Temporal(TemporalType.TIMESTAMP)
		private Date report_resubdate;

		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Archival_Summary_Entity() {
			super();
		}

		// Getters and Setters
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setreport_resubdate(Date report_resubdate) {
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

		// Getters and Setters for R11 to R37 (same as previous entities)
		// ================== R11 ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}

	}

	// =====================================================
	// M_SRWA_12F_Archival_Detail_RowMapper
	// =====================================================

	public class M_SRWA_12F_Archival_Detail_RowMapper implements RowMapper<M_SRWA_12F_Archival_Detail_Entity> {

		@Override
		public M_SRWA_12F_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Archival_Detail_Entity obj = new M_SRWA_12F_Archival_Detail_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setreport_resubdate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// =====================================================
	// M_SRWA_12F_Archival_Detail_Entity
	// =====================================================

	public class M_SRWA_12F_Archival_Detail_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Temporal(TemporalType.TIMESTAMP)
		private Date report_resubdate;

		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Archival_Detail_Entity() {
			super();
		}

		// ================== COMMON FIELDS GETTERS AND SETTERS ==================
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setreport_resubdate(Date report_resubdate) {
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

		// ================== R11 GETTERS AND SETTERS ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 GETTERS AND SETTERS ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 GETTERS AND SETTERS ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 GETTERS AND SETTERS ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 GETTERS AND SETTERS ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 GETTERS AND SETTERS ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 GETTERS AND SETTERS ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 GETTERS AND SETTERS ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 GETTERS AND SETTERS ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 GETTERS AND SETTERS ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 GETTERS AND SETTERS ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 GETTERS AND SETTERS ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 GETTERS AND SETTERS ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 GETTERS AND SETTERS ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 GETTERS AND SETTERS ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 GETTERS AND SETTERS ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 GETTERS AND SETTERS ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 GETTERS AND SETTERS ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 GETTERS AND SETTERS ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 GETTERS AND SETTERS ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 GETTERS AND SETTERS ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 GETTERS AND SETTERS ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 GETTERS AND SETTERS ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 GETTERS AND SETTERS ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 GETTERS AND SETTERS ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 GETTERS AND SETTERS ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 GETTERS AND SETTERS ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}
	}

	// =====================================================
	// M_SRWA_12F_Resub_Summary_RowMapper
	// =====================================================

	public class M_SRWA_12F_Resub_Summary_RowMapper implements RowMapper<M_SRWA_12F_Resub_Summary_Entity> {

		@Override
		public M_SRWA_12F_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Resub_Summary_Entity obj = new M_SRWA_12F_Resub_Summary_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setreport_resubdate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// =====================================================
	// M_SRWA_12F_Resub_Summary_Entity
	// =====================================================

	public class M_SRWA_12F_Resub_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Temporal(TemporalType.TIMESTAMP)
		private Date report_resubdate;

		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Resub_Summary_Entity() {
			super();
		}

		// ================== COMMON FIELDS GETTERS AND SETTERS ==================
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setreport_resubdate(Date report_resubdate) {
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

		// ================== R11 GETTERS AND SETTERS ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 GETTERS AND SETTERS ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 GETTERS AND SETTERS ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 GETTERS AND SETTERS ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 GETTERS AND SETTERS ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 GETTERS AND SETTERS ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 GETTERS AND SETTERS ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 GETTERS AND SETTERS ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 GETTERS AND SETTERS ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 GETTERS AND SETTERS ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 GETTERS AND SETTERS ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 GETTERS AND SETTERS ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 GETTERS AND SETTERS ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 GETTERS AND SETTERS ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 GETTERS AND SETTERS ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 GETTERS AND SETTERS ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 GETTERS AND SETTERS ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 GETTERS AND SETTERS ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 GETTERS AND SETTERS ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 GETTERS AND SETTERS ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 GETTERS AND SETTERS ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 GETTERS AND SETTERS ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 GETTERS AND SETTERS ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 GETTERS AND SETTERS ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 GETTERS AND SETTERS ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 GETTERS AND SETTERS ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 GETTERS AND SETTERS ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}
	}

	// =====================================================
	// M_SRWA_12F_Resub_Detail_RowMapper
	// =====================================================

	public class M_SRWA_12F_Resub_Detail_RowMapper implements RowMapper<M_SRWA_12F_Resub_Detail_Entity> {

		@Override
		public M_SRWA_12F_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12F_Resub_Detail_Entity obj = new M_SRWA_12F_Resub_Detail_Entity();

			// ================== R11 ==================
			obj.setR11_NAME_OF_CORPORATE(rs.getString("R11_NAME_OF_CORPORATE"));
			obj.setR11_CREDIT_RATING(rs.getString("R11_CREDIT_RATING"));
			obj.setR11_RATING_AGENCY(rs.getString("R11_RATING_AGENCY"));
			obj.setR11_EXPOSURE_AMT(rs.getBigDecimal("R11_EXPOSURE_AMT"));
			obj.setR11_RISK_WEIGHT(rs.getBigDecimal("R11_RISK_WEIGHT"));
			obj.setR11_RISK_WEIGHTED_AMT(rs.getBigDecimal("R11_RISK_WEIGHTED_AMT"));

			// ================== R12 ==================
			obj.setR12_NAME_OF_CORPORATE(rs.getString("R12_NAME_OF_CORPORATE"));
			obj.setR12_CREDIT_RATING(rs.getString("R12_CREDIT_RATING"));
			obj.setR12_RATING_AGENCY(rs.getString("R12_RATING_AGENCY"));
			obj.setR12_EXPOSURE_AMT(rs.getBigDecimal("R12_EXPOSURE_AMT"));
			obj.setR12_RISK_WEIGHT(rs.getBigDecimal("R12_RISK_WEIGHT"));
			obj.setR12_RISK_WEIGHTED_AMT(rs.getBigDecimal("R12_RISK_WEIGHTED_AMT"));

			// ================== R13 ==================
			obj.setR13_NAME_OF_CORPORATE(rs.getString("R13_NAME_OF_CORPORATE"));
			obj.setR13_CREDIT_RATING(rs.getString("R13_CREDIT_RATING"));
			obj.setR13_RATING_AGENCY(rs.getString("R13_RATING_AGENCY"));
			obj.setR13_EXPOSURE_AMT(rs.getBigDecimal("R13_EXPOSURE_AMT"));
			obj.setR13_RISK_WEIGHT(rs.getBigDecimal("R13_RISK_WEIGHT"));
			obj.setR13_RISK_WEIGHTED_AMT(rs.getBigDecimal("R13_RISK_WEIGHTED_AMT"));

			// ================== R14 ==================
			obj.setR14_NAME_OF_CORPORATE(rs.getString("R14_NAME_OF_CORPORATE"));
			obj.setR14_CREDIT_RATING(rs.getString("R14_CREDIT_RATING"));
			obj.setR14_RATING_AGENCY(rs.getString("R14_RATING_AGENCY"));
			obj.setR14_EXPOSURE_AMT(rs.getBigDecimal("R14_EXPOSURE_AMT"));
			obj.setR14_RISK_WEIGHT(rs.getBigDecimal("R14_RISK_WEIGHT"));
			obj.setR14_RISK_WEIGHTED_AMT(rs.getBigDecimal("R14_RISK_WEIGHTED_AMT"));

			// ================== R15 ==================
			obj.setR15_NAME_OF_CORPORATE(rs.getString("R15_NAME_OF_CORPORATE"));
			obj.setR15_CREDIT_RATING(rs.getString("R15_CREDIT_RATING"));
			obj.setR15_RATING_AGENCY(rs.getString("R15_RATING_AGENCY"));
			obj.setR15_EXPOSURE_AMT(rs.getBigDecimal("R15_EXPOSURE_AMT"));
			obj.setR15_RISK_WEIGHT(rs.getBigDecimal("R15_RISK_WEIGHT"));
			obj.setR15_RISK_WEIGHTED_AMT(rs.getBigDecimal("R15_RISK_WEIGHTED_AMT"));

			// ================== R16 ==================
			obj.setR16_NAME_OF_CORPORATE(rs.getString("R16_NAME_OF_CORPORATE"));
			obj.setR16_CREDIT_RATING(rs.getString("R16_CREDIT_RATING"));
			obj.setR16_RATING_AGENCY(rs.getString("R16_RATING_AGENCY"));
			obj.setR16_EXPOSURE_AMT(rs.getBigDecimal("R16_EXPOSURE_AMT"));
			obj.setR16_RISK_WEIGHT(rs.getBigDecimal("R16_RISK_WEIGHT"));
			obj.setR16_RISK_WEIGHTED_AMT(rs.getBigDecimal("R16_RISK_WEIGHTED_AMT"));

			// ================== R17 ==================
			obj.setR17_NAME_OF_CORPORATE(rs.getString("R17_NAME_OF_CORPORATE"));
			obj.setR17_CREDIT_RATING(rs.getString("R17_CREDIT_RATING"));
			obj.setR17_RATING_AGENCY(rs.getString("R17_RATING_AGENCY"));
			obj.setR17_EXPOSURE_AMT(rs.getBigDecimal("R17_EXPOSURE_AMT"));
			obj.setR17_RISK_WEIGHT(rs.getBigDecimal("R17_RISK_WEIGHT"));
			obj.setR17_RISK_WEIGHTED_AMT(rs.getBigDecimal("R17_RISK_WEIGHTED_AMT"));

			// ================== R18 ==================
			obj.setR18_NAME_OF_CORPORATE(rs.getString("R18_NAME_OF_CORPORATE"));
			obj.setR18_CREDIT_RATING(rs.getString("R18_CREDIT_RATING"));
			obj.setR18_RATING_AGENCY(rs.getString("R18_RATING_AGENCY"));
			obj.setR18_EXPOSURE_AMT(rs.getBigDecimal("R18_EXPOSURE_AMT"));
			obj.setR18_RISK_WEIGHT(rs.getBigDecimal("R18_RISK_WEIGHT"));
			obj.setR18_RISK_WEIGHTED_AMT(rs.getBigDecimal("R18_RISK_WEIGHTED_AMT"));

			// ================== R19 ==================
			obj.setR19_NAME_OF_CORPORATE(rs.getString("R19_NAME_OF_CORPORATE"));
			obj.setR19_CREDIT_RATING(rs.getString("R19_CREDIT_RATING"));
			obj.setR19_RATING_AGENCY(rs.getString("R19_RATING_AGENCY"));
			obj.setR19_EXPOSURE_AMT(rs.getBigDecimal("R19_EXPOSURE_AMT"));
			obj.setR19_RISK_WEIGHT(rs.getBigDecimal("R19_RISK_WEIGHT"));
			obj.setR19_RISK_WEIGHTED_AMT(rs.getBigDecimal("R19_RISK_WEIGHTED_AMT"));

			// ================== R20 ==================
			obj.setR20_NAME_OF_CORPORATE(rs.getString("R20_NAME_OF_CORPORATE"));
			obj.setR20_CREDIT_RATING(rs.getString("R20_CREDIT_RATING"));
			obj.setR20_RATING_AGENCY(rs.getString("R20_RATING_AGENCY"));
			obj.setR20_EXPOSURE_AMT(rs.getBigDecimal("R20_EXPOSURE_AMT"));
			obj.setR20_RISK_WEIGHT(rs.getBigDecimal("R20_RISK_WEIGHT"));
			obj.setR20_RISK_WEIGHTED_AMT(rs.getBigDecimal("R20_RISK_WEIGHTED_AMT"));

			// ================== R21 ==================
			obj.setR21_NAME_OF_CORPORATE(rs.getString("R21_NAME_OF_CORPORATE"));
			obj.setR21_CREDIT_RATING(rs.getString("R21_CREDIT_RATING"));
			obj.setR21_RATING_AGENCY(rs.getString("R21_RATING_AGENCY"));
			obj.setR21_EXPOSURE_AMT(rs.getBigDecimal("R21_EXPOSURE_AMT"));
			obj.setR21_RISK_WEIGHT(rs.getBigDecimal("R21_RISK_WEIGHT"));
			obj.setR21_RISK_WEIGHTED_AMT(rs.getBigDecimal("R21_RISK_WEIGHTED_AMT"));

			// ================== R22 ==================
			obj.setR22_NAME_OF_CORPORATE(rs.getString("R22_NAME_OF_CORPORATE"));
			obj.setR22_CREDIT_RATING(rs.getString("R22_CREDIT_RATING"));
			obj.setR22_RATING_AGENCY(rs.getString("R22_RATING_AGENCY"));
			obj.setR22_EXPOSURE_AMT(rs.getBigDecimal("R22_EXPOSURE_AMT"));
			obj.setR22_RISK_WEIGHT(rs.getBigDecimal("R22_RISK_WEIGHT"));
			obj.setR22_RISK_WEIGHTED_AMT(rs.getBigDecimal("R22_RISK_WEIGHTED_AMT"));

			// ================== R23 ==================
			obj.setR23_NAME_OF_CORPORATE(rs.getString("R23_NAME_OF_CORPORATE"));
			obj.setR23_CREDIT_RATING(rs.getString("R23_CREDIT_RATING"));
			obj.setR23_RATING_AGENCY(rs.getString("R23_RATING_AGENCY"));
			obj.setR23_EXPOSURE_AMT(rs.getBigDecimal("R23_EXPOSURE_AMT"));
			obj.setR23_RISK_WEIGHT(rs.getBigDecimal("R23_RISK_WEIGHT"));
			obj.setR23_RISK_WEIGHTED_AMT(rs.getBigDecimal("R23_RISK_WEIGHTED_AMT"));

			// ================== R24 ==================
			obj.setR24_NAME_OF_CORPORATE(rs.getString("R24_NAME_OF_CORPORATE"));
			obj.setR24_CREDIT_RATING(rs.getString("R24_CREDIT_RATING"));
			obj.setR24_RATING_AGENCY(rs.getString("R24_RATING_AGENCY"));
			obj.setR24_EXPOSURE_AMT(rs.getBigDecimal("R24_EXPOSURE_AMT"));
			obj.setR24_RISK_WEIGHT(rs.getBigDecimal("R24_RISK_WEIGHT"));
			obj.setR24_RISK_WEIGHTED_AMT(rs.getBigDecimal("R24_RISK_WEIGHTED_AMT"));

			// ================== R25 ==================
			obj.setR25_NAME_OF_CORPORATE(rs.getString("R25_NAME_OF_CORPORATE"));
			obj.setR25_CREDIT_RATING(rs.getString("R25_CREDIT_RATING"));
			obj.setR25_RATING_AGENCY(rs.getString("R25_RATING_AGENCY"));
			obj.setR25_EXPOSURE_AMT(rs.getBigDecimal("R25_EXPOSURE_AMT"));
			obj.setR25_RISK_WEIGHT(rs.getBigDecimal("R25_RISK_WEIGHT"));
			obj.setR25_RISK_WEIGHTED_AMT(rs.getBigDecimal("R25_RISK_WEIGHTED_AMT"));

			// ================== R26 ==================
			obj.setR26_NAME_OF_CORPORATE(rs.getString("R26_NAME_OF_CORPORATE"));
			obj.setR26_CREDIT_RATING(rs.getString("R26_CREDIT_RATING"));
			obj.setR26_RATING_AGENCY(rs.getString("R26_RATING_AGENCY"));
			obj.setR26_EXPOSURE_AMT(rs.getBigDecimal("R26_EXPOSURE_AMT"));
			obj.setR26_RISK_WEIGHT(rs.getBigDecimal("R26_RISK_WEIGHT"));
			obj.setR26_RISK_WEIGHTED_AMT(rs.getBigDecimal("R26_RISK_WEIGHTED_AMT"));

			// ================== R27 ==================
			obj.setR27_NAME_OF_CORPORATE(rs.getString("R27_NAME_OF_CORPORATE"));
			obj.setR27_CREDIT_RATING(rs.getString("R27_CREDIT_RATING"));
			obj.setR27_RATING_AGENCY(rs.getString("R27_RATING_AGENCY"));
			obj.setR27_EXPOSURE_AMT(rs.getBigDecimal("R27_EXPOSURE_AMT"));
			obj.setR27_RISK_WEIGHT(rs.getBigDecimal("R27_RISK_WEIGHT"));
			obj.setR27_RISK_WEIGHTED_AMT(rs.getBigDecimal("R27_RISK_WEIGHTED_AMT"));

			// ================== R28 ==================
			obj.setR28_NAME_OF_CORPORATE(rs.getString("R28_NAME_OF_CORPORATE"));
			obj.setR28_CREDIT_RATING(rs.getString("R28_CREDIT_RATING"));
			obj.setR28_RATING_AGENCY(rs.getString("R28_RATING_AGENCY"));
			obj.setR28_EXPOSURE_AMT(rs.getBigDecimal("R28_EXPOSURE_AMT"));
			obj.setR28_RISK_WEIGHT(rs.getBigDecimal("R28_RISK_WEIGHT"));
			obj.setR28_RISK_WEIGHTED_AMT(rs.getBigDecimal("R28_RISK_WEIGHTED_AMT"));

			// ================== R29 ==================
			obj.setR29_NAME_OF_CORPORATE(rs.getString("R29_NAME_OF_CORPORATE"));
			obj.setR29_CREDIT_RATING(rs.getString("R29_CREDIT_RATING"));
			obj.setR29_RATING_AGENCY(rs.getString("R29_RATING_AGENCY"));
			obj.setR29_EXPOSURE_AMT(rs.getBigDecimal("R29_EXPOSURE_AMT"));
			obj.setR29_RISK_WEIGHT(rs.getBigDecimal("R29_RISK_WEIGHT"));
			obj.setR29_RISK_WEIGHTED_AMT(rs.getBigDecimal("R29_RISK_WEIGHTED_AMT"));

			// ================== R30 ==================
			obj.setR30_NAME_OF_CORPORATE(rs.getString("R30_NAME_OF_CORPORATE"));
			obj.setR30_CREDIT_RATING(rs.getString("R30_CREDIT_RATING"));
			obj.setR30_RATING_AGENCY(rs.getString("R30_RATING_AGENCY"));
			obj.setR30_EXPOSURE_AMT(rs.getBigDecimal("R30_EXPOSURE_AMT"));
			obj.setR30_RISK_WEIGHT(rs.getBigDecimal("R30_RISK_WEIGHT"));
			obj.setR30_RISK_WEIGHTED_AMT(rs.getBigDecimal("R30_RISK_WEIGHTED_AMT"));

			// ================== R31 ==================
			obj.setR31_NAME_OF_CORPORATE(rs.getString("R31_NAME_OF_CORPORATE"));
			obj.setR31_CREDIT_RATING(rs.getString("R31_CREDIT_RATING"));
			obj.setR31_RATING_AGENCY(rs.getString("R31_RATING_AGENCY"));
			obj.setR31_EXPOSURE_AMT(rs.getBigDecimal("R31_EXPOSURE_AMT"));
			obj.setR31_RISK_WEIGHT(rs.getBigDecimal("R31_RISK_WEIGHT"));
			obj.setR31_RISK_WEIGHTED_AMT(rs.getBigDecimal("R31_RISK_WEIGHTED_AMT"));

			// ================== R32 ==================
			obj.setR32_NAME_OF_CORPORATE(rs.getString("R32_NAME_OF_CORPORATE"));
			obj.setR32_CREDIT_RATING(rs.getString("R32_CREDIT_RATING"));
			obj.setR32_RATING_AGENCY(rs.getString("R32_RATING_AGENCY"));
			obj.setR32_EXPOSURE_AMT(rs.getBigDecimal("R32_EXPOSURE_AMT"));
			obj.setR32_RISK_WEIGHT(rs.getBigDecimal("R32_RISK_WEIGHT"));
			obj.setR32_RISK_WEIGHTED_AMT(rs.getBigDecimal("R32_RISK_WEIGHTED_AMT"));

			// ================== R33 ==================
			obj.setR33_NAME_OF_CORPORATE(rs.getString("R33_NAME_OF_CORPORATE"));
			obj.setR33_CREDIT_RATING(rs.getString("R33_CREDIT_RATING"));
			obj.setR33_RATING_AGENCY(rs.getString("R33_RATING_AGENCY"));
			obj.setR33_EXPOSURE_AMT(rs.getBigDecimal("R33_EXPOSURE_AMT"));
			obj.setR33_RISK_WEIGHT(rs.getBigDecimal("R33_RISK_WEIGHT"));
			obj.setR33_RISK_WEIGHTED_AMT(rs.getBigDecimal("R33_RISK_WEIGHTED_AMT"));

			// ================== R34 ==================
			obj.setR34_NAME_OF_CORPORATE(rs.getString("R34_NAME_OF_CORPORATE"));
			obj.setR34_CREDIT_RATING(rs.getString("R34_CREDIT_RATING"));
			obj.setR34_RATING_AGENCY(rs.getString("R34_RATING_AGENCY"));
			obj.setR34_EXPOSURE_AMT(rs.getBigDecimal("R34_EXPOSURE_AMT"));
			obj.setR34_RISK_WEIGHT(rs.getBigDecimal("R34_RISK_WEIGHT"));
			obj.setR34_RISK_WEIGHTED_AMT(rs.getBigDecimal("R34_RISK_WEIGHTED_AMT"));

			// ================== R35 ==================
			obj.setR35_NAME_OF_CORPORATE(rs.getString("R35_NAME_OF_CORPORATE"));
			obj.setR35_CREDIT_RATING(rs.getString("R35_CREDIT_RATING"));
			obj.setR35_RATING_AGENCY(rs.getString("R35_RATING_AGENCY"));
			obj.setR35_EXPOSURE_AMT(rs.getBigDecimal("R35_EXPOSURE_AMT"));
			obj.setR35_RISK_WEIGHT(rs.getBigDecimal("R35_RISK_WEIGHT"));
			obj.setR35_RISK_WEIGHTED_AMT(rs.getBigDecimal("R35_RISK_WEIGHTED_AMT"));

			// ================== R36 ==================
			obj.setR36_NAME_OF_CORPORATE(rs.getString("R36_NAME_OF_CORPORATE"));
			obj.setR36_CREDIT_RATING(rs.getString("R36_CREDIT_RATING"));
			obj.setR36_RATING_AGENCY(rs.getString("R36_RATING_AGENCY"));
			obj.setR36_EXPOSURE_AMT(rs.getBigDecimal("R36_EXPOSURE_AMT"));
			obj.setR36_RISK_WEIGHT(rs.getBigDecimal("R36_RISK_WEIGHT"));
			obj.setR36_RISK_WEIGHTED_AMT(rs.getBigDecimal("R36_RISK_WEIGHTED_AMT"));

			// ================== R37 ==================
			obj.setR37_NAME_OF_CORPORATE(rs.getString("R37_NAME_OF_CORPORATE"));
			obj.setR37_CREDIT_RATING(rs.getString("R37_CREDIT_RATING"));
			obj.setR37_RATING_AGENCY(rs.getString("R37_RATING_AGENCY"));
			obj.setR37_EXPOSURE_AMT(rs.getBigDecimal("R37_EXPOSURE_AMT"));
			obj.setR37_RISK_WEIGHT(rs.getBigDecimal("R37_RISK_WEIGHT"));
			obj.setR37_RISK_WEIGHTED_AMT(rs.getBigDecimal("R37_RISK_WEIGHTED_AMT"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setreport_resubdate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	// =====================================================
	// M_SRWA_12F_Resub_Detail_Entity
	// =====================================================

	public class M_SRWA_12F_Resub_Detail_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Temporal(TemporalType.TIMESTAMP)
		private Date report_resubdate;

		public String report_frequency;
		public String report_code;
		public String report_desc;
		public String entity_flg;
		public String modify_flg;
		public String del_flg;

		// ================== R11 ==================
		private String R11_NAME_OF_CORPORATE;
		private String R11_CREDIT_RATING;
		private String R11_RATING_AGENCY;
		private BigDecimal R11_EXPOSURE_AMT;
		private BigDecimal R11_RISK_WEIGHT;
		private BigDecimal R11_RISK_WEIGHTED_AMT;

		// ================== R12 ==================
		private String R12_NAME_OF_CORPORATE;
		private String R12_CREDIT_RATING;
		private String R12_RATING_AGENCY;
		private BigDecimal R12_EXPOSURE_AMT;
		private BigDecimal R12_RISK_WEIGHT;
		private BigDecimal R12_RISK_WEIGHTED_AMT;

		// ================== R13 ==================
		private String R13_NAME_OF_CORPORATE;
		private String R13_CREDIT_RATING;
		private String R13_RATING_AGENCY;
		private BigDecimal R13_EXPOSURE_AMT;
		private BigDecimal R13_RISK_WEIGHT;
		private BigDecimal R13_RISK_WEIGHTED_AMT;

		// ================== R14 ==================
		private String R14_NAME_OF_CORPORATE;
		private String R14_CREDIT_RATING;
		private String R14_RATING_AGENCY;
		private BigDecimal R14_EXPOSURE_AMT;
		private BigDecimal R14_RISK_WEIGHT;
		private BigDecimal R14_RISK_WEIGHTED_AMT;

		// ================== R15 ==================
		private String R15_NAME_OF_CORPORATE;
		private String R15_CREDIT_RATING;
		private String R15_RATING_AGENCY;
		private BigDecimal R15_EXPOSURE_AMT;
		private BigDecimal R15_RISK_WEIGHT;
		private BigDecimal R15_RISK_WEIGHTED_AMT;

		// ================== R16 ==================
		private String R16_NAME_OF_CORPORATE;
		private String R16_CREDIT_RATING;
		private String R16_RATING_AGENCY;
		private BigDecimal R16_EXPOSURE_AMT;
		private BigDecimal R16_RISK_WEIGHT;
		private BigDecimal R16_RISK_WEIGHTED_AMT;

		// ================== R17 ==================
		private String R17_NAME_OF_CORPORATE;
		private String R17_CREDIT_RATING;
		private String R17_RATING_AGENCY;
		private BigDecimal R17_EXPOSURE_AMT;
		private BigDecimal R17_RISK_WEIGHT;
		private BigDecimal R17_RISK_WEIGHTED_AMT;

		// ================== R18 ==================
		private String R18_NAME_OF_CORPORATE;
		private String R18_CREDIT_RATING;
		private String R18_RATING_AGENCY;
		private BigDecimal R18_EXPOSURE_AMT;
		private BigDecimal R18_RISK_WEIGHT;
		private BigDecimal R18_RISK_WEIGHTED_AMT;

		// ================== R19 ==================
		private String R19_NAME_OF_CORPORATE;
		private String R19_CREDIT_RATING;
		private String R19_RATING_AGENCY;
		private BigDecimal R19_EXPOSURE_AMT;
		private BigDecimal R19_RISK_WEIGHT;
		private BigDecimal R19_RISK_WEIGHTED_AMT;

		// ================== R20 ==================
		private String R20_NAME_OF_CORPORATE;
		private String R20_CREDIT_RATING;
		private String R20_RATING_AGENCY;
		private BigDecimal R20_EXPOSURE_AMT;
		private BigDecimal R20_RISK_WEIGHT;
		private BigDecimal R20_RISK_WEIGHTED_AMT;

		// ================== R21 ==================
		private String R21_NAME_OF_CORPORATE;
		private String R21_CREDIT_RATING;
		private String R21_RATING_AGENCY;
		private BigDecimal R21_EXPOSURE_AMT;
		private BigDecimal R21_RISK_WEIGHT;
		private BigDecimal R21_RISK_WEIGHTED_AMT;

		// ================== R22 ==================
		private String R22_NAME_OF_CORPORATE;
		private String R22_CREDIT_RATING;
		private String R22_RATING_AGENCY;
		private BigDecimal R22_EXPOSURE_AMT;
		private BigDecimal R22_RISK_WEIGHT;
		private BigDecimal R22_RISK_WEIGHTED_AMT;

		// ================== R23 ==================
		private String R23_NAME_OF_CORPORATE;
		private String R23_CREDIT_RATING;
		private String R23_RATING_AGENCY;
		private BigDecimal R23_EXPOSURE_AMT;
		private BigDecimal R23_RISK_WEIGHT;
		private BigDecimal R23_RISK_WEIGHTED_AMT;

		// ================== R24 ==================
		private String R24_NAME_OF_CORPORATE;
		private String R24_CREDIT_RATING;
		private String R24_RATING_AGENCY;
		private BigDecimal R24_EXPOSURE_AMT;
		private BigDecimal R24_RISK_WEIGHT;
		private BigDecimal R24_RISK_WEIGHTED_AMT;

		// ================== R25 ==================
		private String R25_NAME_OF_CORPORATE;
		private String R25_CREDIT_RATING;
		private String R25_RATING_AGENCY;
		private BigDecimal R25_EXPOSURE_AMT;
		private BigDecimal R25_RISK_WEIGHT;
		private BigDecimal R25_RISK_WEIGHTED_AMT;

		// ================== R26 ==================
		private String R26_NAME_OF_CORPORATE;
		private String R26_CREDIT_RATING;
		private String R26_RATING_AGENCY;
		private BigDecimal R26_EXPOSURE_AMT;
		private BigDecimal R26_RISK_WEIGHT;
		private BigDecimal R26_RISK_WEIGHTED_AMT;

		// ================== R27 ==================
		private String R27_NAME_OF_CORPORATE;
		private String R27_CREDIT_RATING;
		private String R27_RATING_AGENCY;
		private BigDecimal R27_EXPOSURE_AMT;
		private BigDecimal R27_RISK_WEIGHT;
		private BigDecimal R27_RISK_WEIGHTED_AMT;

		// ================== R28 ==================
		private String R28_NAME_OF_CORPORATE;
		private String R28_CREDIT_RATING;
		private String R28_RATING_AGENCY;
		private BigDecimal R28_EXPOSURE_AMT;
		private BigDecimal R28_RISK_WEIGHT;
		private BigDecimal R28_RISK_WEIGHTED_AMT;

		// ================== R29 ==================
		private String R29_NAME_OF_CORPORATE;
		private String R29_CREDIT_RATING;
		private String R29_RATING_AGENCY;
		private BigDecimal R29_EXPOSURE_AMT;
		private BigDecimal R29_RISK_WEIGHT;
		private BigDecimal R29_RISK_WEIGHTED_AMT;

		// ================== R30 ==================
		private String R30_NAME_OF_CORPORATE;
		private String R30_CREDIT_RATING;
		private String R30_RATING_AGENCY;
		private BigDecimal R30_EXPOSURE_AMT;
		private BigDecimal R30_RISK_WEIGHT;
		private BigDecimal R30_RISK_WEIGHTED_AMT;

		// ================== R31 ==================
		private String R31_NAME_OF_CORPORATE;
		private String R31_CREDIT_RATING;
		private String R31_RATING_AGENCY;
		private BigDecimal R31_EXPOSURE_AMT;
		private BigDecimal R31_RISK_WEIGHT;
		private BigDecimal R31_RISK_WEIGHTED_AMT;

		// ================== R32 ==================
		private String R32_NAME_OF_CORPORATE;
		private String R32_CREDIT_RATING;
		private String R32_RATING_AGENCY;
		private BigDecimal R32_EXPOSURE_AMT;
		private BigDecimal R32_RISK_WEIGHT;
		private BigDecimal R32_RISK_WEIGHTED_AMT;

		// ================== R33 ==================
		private String R33_NAME_OF_CORPORATE;
		private String R33_CREDIT_RATING;
		private String R33_RATING_AGENCY;
		private BigDecimal R33_EXPOSURE_AMT;
		private BigDecimal R33_RISK_WEIGHT;
		private BigDecimal R33_RISK_WEIGHTED_AMT;

		// ================== R34 ==================
		private String R34_NAME_OF_CORPORATE;
		private String R34_CREDIT_RATING;
		private String R34_RATING_AGENCY;
		private BigDecimal R34_EXPOSURE_AMT;
		private BigDecimal R34_RISK_WEIGHT;
		private BigDecimal R34_RISK_WEIGHTED_AMT;

		// ================== R35 ==================
		private String R35_NAME_OF_CORPORATE;
		private String R35_CREDIT_RATING;
		private String R35_RATING_AGENCY;
		private BigDecimal R35_EXPOSURE_AMT;
		private BigDecimal R35_RISK_WEIGHT;
		private BigDecimal R35_RISK_WEIGHTED_AMT;

		// ================== R36 ==================
		private String R36_NAME_OF_CORPORATE;
		private String R36_CREDIT_RATING;
		private String R36_RATING_AGENCY;
		private BigDecimal R36_EXPOSURE_AMT;
		private BigDecimal R36_RISK_WEIGHT;
		private BigDecimal R36_RISK_WEIGHTED_AMT;

		// ================== R37 ==================
		private String R37_NAME_OF_CORPORATE;
		private String R37_CREDIT_RATING;
		private String R37_RATING_AGENCY;
		private BigDecimal R37_EXPOSURE_AMT;
		private BigDecimal R37_RISK_WEIGHT;
		private BigDecimal R37_RISK_WEIGHTED_AMT;

		// Default Constructor
		public M_SRWA_12F_Resub_Detail_Entity() {
			super();
		}

		// ================== COMMON FIELDS GETTERS AND SETTERS ==================
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

		public Date getReport_resubdate() {
			return report_resubdate;
		}

		public void setreport_resubdate(Date report_resubdate) {
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

		// ================== R11 GETTERS AND SETTERS ==================
		public String getR11_NAME_OF_CORPORATE() {
			return R11_NAME_OF_CORPORATE;
		}

		public void setR11_NAME_OF_CORPORATE(String R11_NAME_OF_CORPORATE) {
			this.R11_NAME_OF_CORPORATE = R11_NAME_OF_CORPORATE;
		}

		public String getR11_CREDIT_RATING() {
			return R11_CREDIT_RATING;
		}

		public void setR11_CREDIT_RATING(String R11_CREDIT_RATING) {
			this.R11_CREDIT_RATING = R11_CREDIT_RATING;
		}

		public String getR11_RATING_AGENCY() {
			return R11_RATING_AGENCY;
		}

		public void setR11_RATING_AGENCY(String R11_RATING_AGENCY) {
			this.R11_RATING_AGENCY = R11_RATING_AGENCY;
		}

		public BigDecimal getR11_EXPOSURE_AMT() {
			return R11_EXPOSURE_AMT;
		}

		public void setR11_EXPOSURE_AMT(BigDecimal R11_EXPOSURE_AMT) {
			this.R11_EXPOSURE_AMT = R11_EXPOSURE_AMT;
		}

		public BigDecimal getR11_RISK_WEIGHT() {
			return R11_RISK_WEIGHT;
		}

		public void setR11_RISK_WEIGHT(BigDecimal R11_RISK_WEIGHT) {
			this.R11_RISK_WEIGHT = R11_RISK_WEIGHT;
		}

		public BigDecimal getR11_RISK_WEIGHTED_AMT() {
			return R11_RISK_WEIGHTED_AMT;
		}

		public void setR11_RISK_WEIGHTED_AMT(BigDecimal R11_RISK_WEIGHTED_AMT) {
			this.R11_RISK_WEIGHTED_AMT = R11_RISK_WEIGHTED_AMT;
		}

		// ================== R12 GETTERS AND SETTERS ==================
		public String getR12_NAME_OF_CORPORATE() {
			return R12_NAME_OF_CORPORATE;
		}

		public void setR12_NAME_OF_CORPORATE(String R12_NAME_OF_CORPORATE) {
			this.R12_NAME_OF_CORPORATE = R12_NAME_OF_CORPORATE;
		}

		public String getR12_CREDIT_RATING() {
			return R12_CREDIT_RATING;
		}

		public void setR12_CREDIT_RATING(String R12_CREDIT_RATING) {
			this.R12_CREDIT_RATING = R12_CREDIT_RATING;
		}

		public String getR12_RATING_AGENCY() {
			return R12_RATING_AGENCY;
		}

		public void setR12_RATING_AGENCY(String R12_RATING_AGENCY) {
			this.R12_RATING_AGENCY = R12_RATING_AGENCY;
		}

		public BigDecimal getR12_EXPOSURE_AMT() {
			return R12_EXPOSURE_AMT;
		}

		public void setR12_EXPOSURE_AMT(BigDecimal R12_EXPOSURE_AMT) {
			this.R12_EXPOSURE_AMT = R12_EXPOSURE_AMT;
		}

		public BigDecimal getR12_RISK_WEIGHT() {
			return R12_RISK_WEIGHT;
		}

		public void setR12_RISK_WEIGHT(BigDecimal R12_RISK_WEIGHT) {
			this.R12_RISK_WEIGHT = R12_RISK_WEIGHT;
		}

		public BigDecimal getR12_RISK_WEIGHTED_AMT() {
			return R12_RISK_WEIGHTED_AMT;
		}

		public void setR12_RISK_WEIGHTED_AMT(BigDecimal R12_RISK_WEIGHTED_AMT) {
			this.R12_RISK_WEIGHTED_AMT = R12_RISK_WEIGHTED_AMT;
		}

		// ================== R13 GETTERS AND SETTERS ==================
		public String getR13_NAME_OF_CORPORATE() {
			return R13_NAME_OF_CORPORATE;
		}

		public void setR13_NAME_OF_CORPORATE(String R13_NAME_OF_CORPORATE) {
			this.R13_NAME_OF_CORPORATE = R13_NAME_OF_CORPORATE;
		}

		public String getR13_CREDIT_RATING() {
			return R13_CREDIT_RATING;
		}

		public void setR13_CREDIT_RATING(String R13_CREDIT_RATING) {
			this.R13_CREDIT_RATING = R13_CREDIT_RATING;
		}

		public String getR13_RATING_AGENCY() {
			return R13_RATING_AGENCY;
		}

		public void setR13_RATING_AGENCY(String R13_RATING_AGENCY) {
			this.R13_RATING_AGENCY = R13_RATING_AGENCY;
		}

		public BigDecimal getR13_EXPOSURE_AMT() {
			return R13_EXPOSURE_AMT;
		}

		public void setR13_EXPOSURE_AMT(BigDecimal R13_EXPOSURE_AMT) {
			this.R13_EXPOSURE_AMT = R13_EXPOSURE_AMT;
		}

		public BigDecimal getR13_RISK_WEIGHT() {
			return R13_RISK_WEIGHT;
		}

		public void setR13_RISK_WEIGHT(BigDecimal R13_RISK_WEIGHT) {
			this.R13_RISK_WEIGHT = R13_RISK_WEIGHT;
		}

		public BigDecimal getR13_RISK_WEIGHTED_AMT() {
			return R13_RISK_WEIGHTED_AMT;
		}

		public void setR13_RISK_WEIGHTED_AMT(BigDecimal R13_RISK_WEIGHTED_AMT) {
			this.R13_RISK_WEIGHTED_AMT = R13_RISK_WEIGHTED_AMT;
		}

		// ================== R14 GETTERS AND SETTERS ==================
		public String getR14_NAME_OF_CORPORATE() {
			return R14_NAME_OF_CORPORATE;
		}

		public void setR14_NAME_OF_CORPORATE(String R14_NAME_OF_CORPORATE) {
			this.R14_NAME_OF_CORPORATE = R14_NAME_OF_CORPORATE;
		}

		public String getR14_CREDIT_RATING() {
			return R14_CREDIT_RATING;
		}

		public void setR14_CREDIT_RATING(String R14_CREDIT_RATING) {
			this.R14_CREDIT_RATING = R14_CREDIT_RATING;
		}

		public String getR14_RATING_AGENCY() {
			return R14_RATING_AGENCY;
		}

		public void setR14_RATING_AGENCY(String R14_RATING_AGENCY) {
			this.R14_RATING_AGENCY = R14_RATING_AGENCY;
		}

		public BigDecimal getR14_EXPOSURE_AMT() {
			return R14_EXPOSURE_AMT;
		}

		public void setR14_EXPOSURE_AMT(BigDecimal R14_EXPOSURE_AMT) {
			this.R14_EXPOSURE_AMT = R14_EXPOSURE_AMT;
		}

		public BigDecimal getR14_RISK_WEIGHT() {
			return R14_RISK_WEIGHT;
		}

		public void setR14_RISK_WEIGHT(BigDecimal R14_RISK_WEIGHT) {
			this.R14_RISK_WEIGHT = R14_RISK_WEIGHT;
		}

		public BigDecimal getR14_RISK_WEIGHTED_AMT() {
			return R14_RISK_WEIGHTED_AMT;
		}

		public void setR14_RISK_WEIGHTED_AMT(BigDecimal R14_RISK_WEIGHTED_AMT) {
			this.R14_RISK_WEIGHTED_AMT = R14_RISK_WEIGHTED_AMT;
		}

		// ================== R15 GETTERS AND SETTERS ==================
		public String getR15_NAME_OF_CORPORATE() {
			return R15_NAME_OF_CORPORATE;
		}

		public void setR15_NAME_OF_CORPORATE(String R15_NAME_OF_CORPORATE) {
			this.R15_NAME_OF_CORPORATE = R15_NAME_OF_CORPORATE;
		}

		public String getR15_CREDIT_RATING() {
			return R15_CREDIT_RATING;
		}

		public void setR15_CREDIT_RATING(String R15_CREDIT_RATING) {
			this.R15_CREDIT_RATING = R15_CREDIT_RATING;
		}

		public String getR15_RATING_AGENCY() {
			return R15_RATING_AGENCY;
		}

		public void setR15_RATING_AGENCY(String R15_RATING_AGENCY) {
			this.R15_RATING_AGENCY = R15_RATING_AGENCY;
		}

		public BigDecimal getR15_EXPOSURE_AMT() {
			return R15_EXPOSURE_AMT;
		}

		public void setR15_EXPOSURE_AMT(BigDecimal R15_EXPOSURE_AMT) {
			this.R15_EXPOSURE_AMT = R15_EXPOSURE_AMT;
		}

		public BigDecimal getR15_RISK_WEIGHT() {
			return R15_RISK_WEIGHT;
		}

		public void setR15_RISK_WEIGHT(BigDecimal R15_RISK_WEIGHT) {
			this.R15_RISK_WEIGHT = R15_RISK_WEIGHT;
		}

		public BigDecimal getR15_RISK_WEIGHTED_AMT() {
			return R15_RISK_WEIGHTED_AMT;
		}

		public void setR15_RISK_WEIGHTED_AMT(BigDecimal R15_RISK_WEIGHTED_AMT) {
			this.R15_RISK_WEIGHTED_AMT = R15_RISK_WEIGHTED_AMT;
		}

		// ================== R16 GETTERS AND SETTERS ==================
		public String getR16_NAME_OF_CORPORATE() {
			return R16_NAME_OF_CORPORATE;
		}

		public void setR16_NAME_OF_CORPORATE(String R16_NAME_OF_CORPORATE) {
			this.R16_NAME_OF_CORPORATE = R16_NAME_OF_CORPORATE;
		}

		public String getR16_CREDIT_RATING() {
			return R16_CREDIT_RATING;
		}

		public void setR16_CREDIT_RATING(String R16_CREDIT_RATING) {
			this.R16_CREDIT_RATING = R16_CREDIT_RATING;
		}

		public String getR16_RATING_AGENCY() {
			return R16_RATING_AGENCY;
		}

		public void setR16_RATING_AGENCY(String R16_RATING_AGENCY) {
			this.R16_RATING_AGENCY = R16_RATING_AGENCY;
		}

		public BigDecimal getR16_EXPOSURE_AMT() {
			return R16_EXPOSURE_AMT;
		}

		public void setR16_EXPOSURE_AMT(BigDecimal R16_EXPOSURE_AMT) {
			this.R16_EXPOSURE_AMT = R16_EXPOSURE_AMT;
		}

		public BigDecimal getR16_RISK_WEIGHT() {
			return R16_RISK_WEIGHT;
		}

		public void setR16_RISK_WEIGHT(BigDecimal R16_RISK_WEIGHT) {
			this.R16_RISK_WEIGHT = R16_RISK_WEIGHT;
		}

		public BigDecimal getR16_RISK_WEIGHTED_AMT() {
			return R16_RISK_WEIGHTED_AMT;
		}

		public void setR16_RISK_WEIGHTED_AMT(BigDecimal R16_RISK_WEIGHTED_AMT) {
			this.R16_RISK_WEIGHTED_AMT = R16_RISK_WEIGHTED_AMT;
		}

		// ================== R17 GETTERS AND SETTERS ==================
		public String getR17_NAME_OF_CORPORATE() {
			return R17_NAME_OF_CORPORATE;
		}

		public void setR17_NAME_OF_CORPORATE(String R17_NAME_OF_CORPORATE) {
			this.R17_NAME_OF_CORPORATE = R17_NAME_OF_CORPORATE;
		}

		public String getR17_CREDIT_RATING() {
			return R17_CREDIT_RATING;
		}

		public void setR17_CREDIT_RATING(String R17_CREDIT_RATING) {
			this.R17_CREDIT_RATING = R17_CREDIT_RATING;
		}

		public String getR17_RATING_AGENCY() {
			return R17_RATING_AGENCY;
		}

		public void setR17_RATING_AGENCY(String R17_RATING_AGENCY) {
			this.R17_RATING_AGENCY = R17_RATING_AGENCY;
		}

		public BigDecimal getR17_EXPOSURE_AMT() {
			return R17_EXPOSURE_AMT;
		}

		public void setR17_EXPOSURE_AMT(BigDecimal R17_EXPOSURE_AMT) {
			this.R17_EXPOSURE_AMT = R17_EXPOSURE_AMT;
		}

		public BigDecimal getR17_RISK_WEIGHT() {
			return R17_RISK_WEIGHT;
		}

		public void setR17_RISK_WEIGHT(BigDecimal R17_RISK_WEIGHT) {
			this.R17_RISK_WEIGHT = R17_RISK_WEIGHT;
		}

		public BigDecimal getR17_RISK_WEIGHTED_AMT() {
			return R17_RISK_WEIGHTED_AMT;
		}

		public void setR17_RISK_WEIGHTED_AMT(BigDecimal R17_RISK_WEIGHTED_AMT) {
			this.R17_RISK_WEIGHTED_AMT = R17_RISK_WEIGHTED_AMT;
		}

		// ================== R18 GETTERS AND SETTERS ==================
		public String getR18_NAME_OF_CORPORATE() {
			return R18_NAME_OF_CORPORATE;
		}

		public void setR18_NAME_OF_CORPORATE(String R18_NAME_OF_CORPORATE) {
			this.R18_NAME_OF_CORPORATE = R18_NAME_OF_CORPORATE;
		}

		public String getR18_CREDIT_RATING() {
			return R18_CREDIT_RATING;
		}

		public void setR18_CREDIT_RATING(String R18_CREDIT_RATING) {
			this.R18_CREDIT_RATING = R18_CREDIT_RATING;
		}

		public String getR18_RATING_AGENCY() {
			return R18_RATING_AGENCY;
		}

		public void setR18_RATING_AGENCY(String R18_RATING_AGENCY) {
			this.R18_RATING_AGENCY = R18_RATING_AGENCY;
		}

		public BigDecimal getR18_EXPOSURE_AMT() {
			return R18_EXPOSURE_AMT;
		}

		public void setR18_EXPOSURE_AMT(BigDecimal R18_EXPOSURE_AMT) {
			this.R18_EXPOSURE_AMT = R18_EXPOSURE_AMT;
		}

		public BigDecimal getR18_RISK_WEIGHT() {
			return R18_RISK_WEIGHT;
		}

		public void setR18_RISK_WEIGHT(BigDecimal R18_RISK_WEIGHT) {
			this.R18_RISK_WEIGHT = R18_RISK_WEIGHT;
		}

		public BigDecimal getR18_RISK_WEIGHTED_AMT() {
			return R18_RISK_WEIGHTED_AMT;
		}

		public void setR18_RISK_WEIGHTED_AMT(BigDecimal R18_RISK_WEIGHTED_AMT) {
			this.R18_RISK_WEIGHTED_AMT = R18_RISK_WEIGHTED_AMT;
		}

		// ================== R19 GETTERS AND SETTERS ==================
		public String getR19_NAME_OF_CORPORATE() {
			return R19_NAME_OF_CORPORATE;
		}

		public void setR19_NAME_OF_CORPORATE(String R19_NAME_OF_CORPORATE) {
			this.R19_NAME_OF_CORPORATE = R19_NAME_OF_CORPORATE;
		}

		public String getR19_CREDIT_RATING() {
			return R19_CREDIT_RATING;
		}

		public void setR19_CREDIT_RATING(String R19_CREDIT_RATING) {
			this.R19_CREDIT_RATING = R19_CREDIT_RATING;
		}

		public String getR19_RATING_AGENCY() {
			return R19_RATING_AGENCY;
		}

		public void setR19_RATING_AGENCY(String R19_RATING_AGENCY) {
			this.R19_RATING_AGENCY = R19_RATING_AGENCY;
		}

		public BigDecimal getR19_EXPOSURE_AMT() {
			return R19_EXPOSURE_AMT;
		}

		public void setR19_EXPOSURE_AMT(BigDecimal R19_EXPOSURE_AMT) {
			this.R19_EXPOSURE_AMT = R19_EXPOSURE_AMT;
		}

		public BigDecimal getR19_RISK_WEIGHT() {
			return R19_RISK_WEIGHT;
		}

		public void setR19_RISK_WEIGHT(BigDecimal R19_RISK_WEIGHT) {
			this.R19_RISK_WEIGHT = R19_RISK_WEIGHT;
		}

		public BigDecimal getR19_RISK_WEIGHTED_AMT() {
			return R19_RISK_WEIGHTED_AMT;
		}

		public void setR19_RISK_WEIGHTED_AMT(BigDecimal R19_RISK_WEIGHTED_AMT) {
			this.R19_RISK_WEIGHTED_AMT = R19_RISK_WEIGHTED_AMT;
		}

		// ================== R20 GETTERS AND SETTERS ==================
		public String getR20_NAME_OF_CORPORATE() {
			return R20_NAME_OF_CORPORATE;
		}

		public void setR20_NAME_OF_CORPORATE(String R20_NAME_OF_CORPORATE) {
			this.R20_NAME_OF_CORPORATE = R20_NAME_OF_CORPORATE;
		}

		public String getR20_CREDIT_RATING() {
			return R20_CREDIT_RATING;
		}

		public void setR20_CREDIT_RATING(String R20_CREDIT_RATING) {
			this.R20_CREDIT_RATING = R20_CREDIT_RATING;
		}

		public String getR20_RATING_AGENCY() {
			return R20_RATING_AGENCY;
		}

		public void setR20_RATING_AGENCY(String R20_RATING_AGENCY) {
			this.R20_RATING_AGENCY = R20_RATING_AGENCY;
		}

		public BigDecimal getR20_EXPOSURE_AMT() {
			return R20_EXPOSURE_AMT;
		}

		public void setR20_EXPOSURE_AMT(BigDecimal R20_EXPOSURE_AMT) {
			this.R20_EXPOSURE_AMT = R20_EXPOSURE_AMT;
		}

		public BigDecimal getR20_RISK_WEIGHT() {
			return R20_RISK_WEIGHT;
		}

		public void setR20_RISK_WEIGHT(BigDecimal R20_RISK_WEIGHT) {
			this.R20_RISK_WEIGHT = R20_RISK_WEIGHT;
		}

		public BigDecimal getR20_RISK_WEIGHTED_AMT() {
			return R20_RISK_WEIGHTED_AMT;
		}

		public void setR20_RISK_WEIGHTED_AMT(BigDecimal R20_RISK_WEIGHTED_AMT) {
			this.R20_RISK_WEIGHTED_AMT = R20_RISK_WEIGHTED_AMT;
		}

		// ================== R21 GETTERS AND SETTERS ==================
		public String getR21_NAME_OF_CORPORATE() {
			return R21_NAME_OF_CORPORATE;
		}

		public void setR21_NAME_OF_CORPORATE(String R21_NAME_OF_CORPORATE) {
			this.R21_NAME_OF_CORPORATE = R21_NAME_OF_CORPORATE;
		}

		public String getR21_CREDIT_RATING() {
			return R21_CREDIT_RATING;
		}

		public void setR21_CREDIT_RATING(String R21_CREDIT_RATING) {
			this.R21_CREDIT_RATING = R21_CREDIT_RATING;
		}

		public String getR21_RATING_AGENCY() {
			return R21_RATING_AGENCY;
		}

		public void setR21_RATING_AGENCY(String R21_RATING_AGENCY) {
			this.R21_RATING_AGENCY = R21_RATING_AGENCY;
		}

		public BigDecimal getR21_EXPOSURE_AMT() {
			return R21_EXPOSURE_AMT;
		}

		public void setR21_EXPOSURE_AMT(BigDecimal R21_EXPOSURE_AMT) {
			this.R21_EXPOSURE_AMT = R21_EXPOSURE_AMT;
		}

		public BigDecimal getR21_RISK_WEIGHT() {
			return R21_RISK_WEIGHT;
		}

		public void setR21_RISK_WEIGHT(BigDecimal R21_RISK_WEIGHT) {
			this.R21_RISK_WEIGHT = R21_RISK_WEIGHT;
		}

		public BigDecimal getR21_RISK_WEIGHTED_AMT() {
			return R21_RISK_WEIGHTED_AMT;
		}

		public void setR21_RISK_WEIGHTED_AMT(BigDecimal R21_RISK_WEIGHTED_AMT) {
			this.R21_RISK_WEIGHTED_AMT = R21_RISK_WEIGHTED_AMT;
		}

		// ================== R22 GETTERS AND SETTERS ==================
		public String getR22_NAME_OF_CORPORATE() {
			return R22_NAME_OF_CORPORATE;
		}

		public void setR22_NAME_OF_CORPORATE(String R22_NAME_OF_CORPORATE) {
			this.R22_NAME_OF_CORPORATE = R22_NAME_OF_CORPORATE;
		}

		public String getR22_CREDIT_RATING() {
			return R22_CREDIT_RATING;
		}

		public void setR22_CREDIT_RATING(String R22_CREDIT_RATING) {
			this.R22_CREDIT_RATING = R22_CREDIT_RATING;
		}

		public String getR22_RATING_AGENCY() {
			return R22_RATING_AGENCY;
		}

		public void setR22_RATING_AGENCY(String R22_RATING_AGENCY) {
			this.R22_RATING_AGENCY = R22_RATING_AGENCY;
		}

		public BigDecimal getR22_EXPOSURE_AMT() {
			return R22_EXPOSURE_AMT;
		}

		public void setR22_EXPOSURE_AMT(BigDecimal R22_EXPOSURE_AMT) {
			this.R22_EXPOSURE_AMT = R22_EXPOSURE_AMT;
		}

		public BigDecimal getR22_RISK_WEIGHT() {
			return R22_RISK_WEIGHT;
		}

		public void setR22_RISK_WEIGHT(BigDecimal R22_RISK_WEIGHT) {
			this.R22_RISK_WEIGHT = R22_RISK_WEIGHT;
		}

		public BigDecimal getR22_RISK_WEIGHTED_AMT() {
			return R22_RISK_WEIGHTED_AMT;
		}

		public void setR22_RISK_WEIGHTED_AMT(BigDecimal R22_RISK_WEIGHTED_AMT) {
			this.R22_RISK_WEIGHTED_AMT = R22_RISK_WEIGHTED_AMT;
		}

		// ================== R23 GETTERS AND SETTERS ==================
		public String getR23_NAME_OF_CORPORATE() {
			return R23_NAME_OF_CORPORATE;
		}

		public void setR23_NAME_OF_CORPORATE(String R23_NAME_OF_CORPORATE) {
			this.R23_NAME_OF_CORPORATE = R23_NAME_OF_CORPORATE;
		}

		public String getR23_CREDIT_RATING() {
			return R23_CREDIT_RATING;
		}

		public void setR23_CREDIT_RATING(String R23_CREDIT_RATING) {
			this.R23_CREDIT_RATING = R23_CREDIT_RATING;
		}

		public String getR23_RATING_AGENCY() {
			return R23_RATING_AGENCY;
		}

		public void setR23_RATING_AGENCY(String R23_RATING_AGENCY) {
			this.R23_RATING_AGENCY = R23_RATING_AGENCY;
		}

		public BigDecimal getR23_EXPOSURE_AMT() {
			return R23_EXPOSURE_AMT;
		}

		public void setR23_EXPOSURE_AMT(BigDecimal R23_EXPOSURE_AMT) {
			this.R23_EXPOSURE_AMT = R23_EXPOSURE_AMT;
		}

		public BigDecimal getR23_RISK_WEIGHT() {
			return R23_RISK_WEIGHT;
		}

		public void setR23_RISK_WEIGHT(BigDecimal R23_RISK_WEIGHT) {
			this.R23_RISK_WEIGHT = R23_RISK_WEIGHT;
		}

		public BigDecimal getR23_RISK_WEIGHTED_AMT() {
			return R23_RISK_WEIGHTED_AMT;
		}

		public void setR23_RISK_WEIGHTED_AMT(BigDecimal R23_RISK_WEIGHTED_AMT) {
			this.R23_RISK_WEIGHTED_AMT = R23_RISK_WEIGHTED_AMT;
		}

		// ================== R24 GETTERS AND SETTERS ==================
		public String getR24_NAME_OF_CORPORATE() {
			return R24_NAME_OF_CORPORATE;
		}

		public void setR24_NAME_OF_CORPORATE(String R24_NAME_OF_CORPORATE) {
			this.R24_NAME_OF_CORPORATE = R24_NAME_OF_CORPORATE;
		}

		public String getR24_CREDIT_RATING() {
			return R24_CREDIT_RATING;
		}

		public void setR24_CREDIT_RATING(String R24_CREDIT_RATING) {
			this.R24_CREDIT_RATING = R24_CREDIT_RATING;
		}

		public String getR24_RATING_AGENCY() {
			return R24_RATING_AGENCY;
		}

		public void setR24_RATING_AGENCY(String R24_RATING_AGENCY) {
			this.R24_RATING_AGENCY = R24_RATING_AGENCY;
		}

		public BigDecimal getR24_EXPOSURE_AMT() {
			return R24_EXPOSURE_AMT;
		}

		public void setR24_EXPOSURE_AMT(BigDecimal R24_EXPOSURE_AMT) {
			this.R24_EXPOSURE_AMT = R24_EXPOSURE_AMT;
		}

		public BigDecimal getR24_RISK_WEIGHT() {
			return R24_RISK_WEIGHT;
		}

		public void setR24_RISK_WEIGHT(BigDecimal R24_RISK_WEIGHT) {
			this.R24_RISK_WEIGHT = R24_RISK_WEIGHT;
		}

		public BigDecimal getR24_RISK_WEIGHTED_AMT() {
			return R24_RISK_WEIGHTED_AMT;
		}

		public void setR24_RISK_WEIGHTED_AMT(BigDecimal R24_RISK_WEIGHTED_AMT) {
			this.R24_RISK_WEIGHTED_AMT = R24_RISK_WEIGHTED_AMT;
		}

		// ================== R25 GETTERS AND SETTERS ==================
		public String getR25_NAME_OF_CORPORATE() {
			return R25_NAME_OF_CORPORATE;
		}

		public void setR25_NAME_OF_CORPORATE(String R25_NAME_OF_CORPORATE) {
			this.R25_NAME_OF_CORPORATE = R25_NAME_OF_CORPORATE;
		}

		public String getR25_CREDIT_RATING() {
			return R25_CREDIT_RATING;
		}

		public void setR25_CREDIT_RATING(String R25_CREDIT_RATING) {
			this.R25_CREDIT_RATING = R25_CREDIT_RATING;
		}

		public String getR25_RATING_AGENCY() {
			return R25_RATING_AGENCY;
		}

		public void setR25_RATING_AGENCY(String R25_RATING_AGENCY) {
			this.R25_RATING_AGENCY = R25_RATING_AGENCY;
		}

		public BigDecimal getR25_EXPOSURE_AMT() {
			return R25_EXPOSURE_AMT;
		}

		public void setR25_EXPOSURE_AMT(BigDecimal R25_EXPOSURE_AMT) {
			this.R25_EXPOSURE_AMT = R25_EXPOSURE_AMT;
		}

		public BigDecimal getR25_RISK_WEIGHT() {
			return R25_RISK_WEIGHT;
		}

		public void setR25_RISK_WEIGHT(BigDecimal R25_RISK_WEIGHT) {
			this.R25_RISK_WEIGHT = R25_RISK_WEIGHT;
		}

		public BigDecimal getR25_RISK_WEIGHTED_AMT() {
			return R25_RISK_WEIGHTED_AMT;
		}

		public void setR25_RISK_WEIGHTED_AMT(BigDecimal R25_RISK_WEIGHTED_AMT) {
			this.R25_RISK_WEIGHTED_AMT = R25_RISK_WEIGHTED_AMT;
		}

		// ================== R26 GETTERS AND SETTERS ==================
		public String getR26_NAME_OF_CORPORATE() {
			return R26_NAME_OF_CORPORATE;
		}

		public void setR26_NAME_OF_CORPORATE(String R26_NAME_OF_CORPORATE) {
			this.R26_NAME_OF_CORPORATE = R26_NAME_OF_CORPORATE;
		}

		public String getR26_CREDIT_RATING() {
			return R26_CREDIT_RATING;
		}

		public void setR26_CREDIT_RATING(String R26_CREDIT_RATING) {
			this.R26_CREDIT_RATING = R26_CREDIT_RATING;
		}

		public String getR26_RATING_AGENCY() {
			return R26_RATING_AGENCY;
		}

		public void setR26_RATING_AGENCY(String R26_RATING_AGENCY) {
			this.R26_RATING_AGENCY = R26_RATING_AGENCY;
		}

		public BigDecimal getR26_EXPOSURE_AMT() {
			return R26_EXPOSURE_AMT;
		}

		public void setR26_EXPOSURE_AMT(BigDecimal R26_EXPOSURE_AMT) {
			this.R26_EXPOSURE_AMT = R26_EXPOSURE_AMT;
		}

		public BigDecimal getR26_RISK_WEIGHT() {
			return R26_RISK_WEIGHT;
		}

		public void setR26_RISK_WEIGHT(BigDecimal R26_RISK_WEIGHT) {
			this.R26_RISK_WEIGHT = R26_RISK_WEIGHT;
		}

		public BigDecimal getR26_RISK_WEIGHTED_AMT() {
			return R26_RISK_WEIGHTED_AMT;
		}

		public void setR26_RISK_WEIGHTED_AMT(BigDecimal R26_RISK_WEIGHTED_AMT) {
			this.R26_RISK_WEIGHTED_AMT = R26_RISK_WEIGHTED_AMT;
		}

		// ================== R27 GETTERS AND SETTERS ==================
		public String getR27_NAME_OF_CORPORATE() {
			return R27_NAME_OF_CORPORATE;
		}

		public void setR27_NAME_OF_CORPORATE(String R27_NAME_OF_CORPORATE) {
			this.R27_NAME_OF_CORPORATE = R27_NAME_OF_CORPORATE;
		}

		public String getR27_CREDIT_RATING() {
			return R27_CREDIT_RATING;
		}

		public void setR27_CREDIT_RATING(String R27_CREDIT_RATING) {
			this.R27_CREDIT_RATING = R27_CREDIT_RATING;
		}

		public String getR27_RATING_AGENCY() {
			return R27_RATING_AGENCY;
		}

		public void setR27_RATING_AGENCY(String R27_RATING_AGENCY) {
			this.R27_RATING_AGENCY = R27_RATING_AGENCY;
		}

		public BigDecimal getR27_EXPOSURE_AMT() {
			return R27_EXPOSURE_AMT;
		}

		public void setR27_EXPOSURE_AMT(BigDecimal R27_EXPOSURE_AMT) {
			this.R27_EXPOSURE_AMT = R27_EXPOSURE_AMT;
		}

		public BigDecimal getR27_RISK_WEIGHT() {
			return R27_RISK_WEIGHT;
		}

		public void setR27_RISK_WEIGHT(BigDecimal R27_RISK_WEIGHT) {
			this.R27_RISK_WEIGHT = R27_RISK_WEIGHT;
		}

		public BigDecimal getR27_RISK_WEIGHTED_AMT() {
			return R27_RISK_WEIGHTED_AMT;
		}

		public void setR27_RISK_WEIGHTED_AMT(BigDecimal R27_RISK_WEIGHTED_AMT) {
			this.R27_RISK_WEIGHTED_AMT = R27_RISK_WEIGHTED_AMT;
		}

		// ================== R28 GETTERS AND SETTERS ==================
		public String getR28_NAME_OF_CORPORATE() {
			return R28_NAME_OF_CORPORATE;
		}

		public void setR28_NAME_OF_CORPORATE(String R28_NAME_OF_CORPORATE) {
			this.R28_NAME_OF_CORPORATE = R28_NAME_OF_CORPORATE;
		}

		public String getR28_CREDIT_RATING() {
			return R28_CREDIT_RATING;
		}

		public void setR28_CREDIT_RATING(String R28_CREDIT_RATING) {
			this.R28_CREDIT_RATING = R28_CREDIT_RATING;
		}

		public String getR28_RATING_AGENCY() {
			return R28_RATING_AGENCY;
		}

		public void setR28_RATING_AGENCY(String R28_RATING_AGENCY) {
			this.R28_RATING_AGENCY = R28_RATING_AGENCY;
		}

		public BigDecimal getR28_EXPOSURE_AMT() {
			return R28_EXPOSURE_AMT;
		}

		public void setR28_EXPOSURE_AMT(BigDecimal R28_EXPOSURE_AMT) {
			this.R28_EXPOSURE_AMT = R28_EXPOSURE_AMT;
		}

		public BigDecimal getR28_RISK_WEIGHT() {
			return R28_RISK_WEIGHT;
		}

		public void setR28_RISK_WEIGHT(BigDecimal R28_RISK_WEIGHT) {
			this.R28_RISK_WEIGHT = R28_RISK_WEIGHT;
		}

		public BigDecimal getR28_RISK_WEIGHTED_AMT() {
			return R28_RISK_WEIGHTED_AMT;
		}

		public void setR28_RISK_WEIGHTED_AMT(BigDecimal R28_RISK_WEIGHTED_AMT) {
			this.R28_RISK_WEIGHTED_AMT = R28_RISK_WEIGHTED_AMT;
		}

		// ================== R29 GETTERS AND SETTERS ==================
		public String getR29_NAME_OF_CORPORATE() {
			return R29_NAME_OF_CORPORATE;
		}

		public void setR29_NAME_OF_CORPORATE(String R29_NAME_OF_CORPORATE) {
			this.R29_NAME_OF_CORPORATE = R29_NAME_OF_CORPORATE;
		}

		public String getR29_CREDIT_RATING() {
			return R29_CREDIT_RATING;
		}

		public void setR29_CREDIT_RATING(String R29_CREDIT_RATING) {
			this.R29_CREDIT_RATING = R29_CREDIT_RATING;
		}

		public String getR29_RATING_AGENCY() {
			return R29_RATING_AGENCY;
		}

		public void setR29_RATING_AGENCY(String R29_RATING_AGENCY) {
			this.R29_RATING_AGENCY = R29_RATING_AGENCY;
		}

		public BigDecimal getR29_EXPOSURE_AMT() {
			return R29_EXPOSURE_AMT;
		}

		public void setR29_EXPOSURE_AMT(BigDecimal R29_EXPOSURE_AMT) {
			this.R29_EXPOSURE_AMT = R29_EXPOSURE_AMT;
		}

		public BigDecimal getR29_RISK_WEIGHT() {
			return R29_RISK_WEIGHT;
		}

		public void setR29_RISK_WEIGHT(BigDecimal R29_RISK_WEIGHT) {
			this.R29_RISK_WEIGHT = R29_RISK_WEIGHT;
		}

		public BigDecimal getR29_RISK_WEIGHTED_AMT() {
			return R29_RISK_WEIGHTED_AMT;
		}

		public void setR29_RISK_WEIGHTED_AMT(BigDecimal R29_RISK_WEIGHTED_AMT) {
			this.R29_RISK_WEIGHTED_AMT = R29_RISK_WEIGHTED_AMT;
		}

		// ================== R30 GETTERS AND SETTERS ==================
		public String getR30_NAME_OF_CORPORATE() {
			return R30_NAME_OF_CORPORATE;
		}

		public void setR30_NAME_OF_CORPORATE(String R30_NAME_OF_CORPORATE) {
			this.R30_NAME_OF_CORPORATE = R30_NAME_OF_CORPORATE;
		}

		public String getR30_CREDIT_RATING() {
			return R30_CREDIT_RATING;
		}

		public void setR30_CREDIT_RATING(String R30_CREDIT_RATING) {
			this.R30_CREDIT_RATING = R30_CREDIT_RATING;
		}

		public String getR30_RATING_AGENCY() {
			return R30_RATING_AGENCY;
		}

		public void setR30_RATING_AGENCY(String R30_RATING_AGENCY) {
			this.R30_RATING_AGENCY = R30_RATING_AGENCY;
		}

		public BigDecimal getR30_EXPOSURE_AMT() {
			return R30_EXPOSURE_AMT;
		}

		public void setR30_EXPOSURE_AMT(BigDecimal R30_EXPOSURE_AMT) {
			this.R30_EXPOSURE_AMT = R30_EXPOSURE_AMT;
		}

		public BigDecimal getR30_RISK_WEIGHT() {
			return R30_RISK_WEIGHT;
		}

		public void setR30_RISK_WEIGHT(BigDecimal R30_RISK_WEIGHT) {
			this.R30_RISK_WEIGHT = R30_RISK_WEIGHT;
		}

		public BigDecimal getR30_RISK_WEIGHTED_AMT() {
			return R30_RISK_WEIGHTED_AMT;
		}

		public void setR30_RISK_WEIGHTED_AMT(BigDecimal R30_RISK_WEIGHTED_AMT) {
			this.R30_RISK_WEIGHTED_AMT = R30_RISK_WEIGHTED_AMT;
		}

		// ================== R31 GETTERS AND SETTERS ==================
		public String getR31_NAME_OF_CORPORATE() {
			return R31_NAME_OF_CORPORATE;
		}

		public void setR31_NAME_OF_CORPORATE(String R31_NAME_OF_CORPORATE) {
			this.R31_NAME_OF_CORPORATE = R31_NAME_OF_CORPORATE;
		}

		public String getR31_CREDIT_RATING() {
			return R31_CREDIT_RATING;
		}

		public void setR31_CREDIT_RATING(String R31_CREDIT_RATING) {
			this.R31_CREDIT_RATING = R31_CREDIT_RATING;
		}

		public String getR31_RATING_AGENCY() {
			return R31_RATING_AGENCY;
		}

		public void setR31_RATING_AGENCY(String R31_RATING_AGENCY) {
			this.R31_RATING_AGENCY = R31_RATING_AGENCY;
		}

		public BigDecimal getR31_EXPOSURE_AMT() {
			return R31_EXPOSURE_AMT;
		}

		public void setR31_EXPOSURE_AMT(BigDecimal R31_EXPOSURE_AMT) {
			this.R31_EXPOSURE_AMT = R31_EXPOSURE_AMT;
		}

		public BigDecimal getR31_RISK_WEIGHT() {
			return R31_RISK_WEIGHT;
		}

		public void setR31_RISK_WEIGHT(BigDecimal R31_RISK_WEIGHT) {
			this.R31_RISK_WEIGHT = R31_RISK_WEIGHT;
		}

		public BigDecimal getR31_RISK_WEIGHTED_AMT() {
			return R31_RISK_WEIGHTED_AMT;
		}

		public void setR31_RISK_WEIGHTED_AMT(BigDecimal R31_RISK_WEIGHTED_AMT) {
			this.R31_RISK_WEIGHTED_AMT = R31_RISK_WEIGHTED_AMT;
		}

		// ================== R32 GETTERS AND SETTERS ==================
		public String getR32_NAME_OF_CORPORATE() {
			return R32_NAME_OF_CORPORATE;
		}

		public void setR32_NAME_OF_CORPORATE(String R32_NAME_OF_CORPORATE) {
			this.R32_NAME_OF_CORPORATE = R32_NAME_OF_CORPORATE;
		}

		public String getR32_CREDIT_RATING() {
			return R32_CREDIT_RATING;
		}

		public void setR32_CREDIT_RATING(String R32_CREDIT_RATING) {
			this.R32_CREDIT_RATING = R32_CREDIT_RATING;
		}

		public String getR32_RATING_AGENCY() {
			return R32_RATING_AGENCY;
		}

		public void setR32_RATING_AGENCY(String R32_RATING_AGENCY) {
			this.R32_RATING_AGENCY = R32_RATING_AGENCY;
		}

		public BigDecimal getR32_EXPOSURE_AMT() {
			return R32_EXPOSURE_AMT;
		}

		public void setR32_EXPOSURE_AMT(BigDecimal R32_EXPOSURE_AMT) {
			this.R32_EXPOSURE_AMT = R32_EXPOSURE_AMT;
		}

		public BigDecimal getR32_RISK_WEIGHT() {
			return R32_RISK_WEIGHT;
		}

		public void setR32_RISK_WEIGHT(BigDecimal R32_RISK_WEIGHT) {
			this.R32_RISK_WEIGHT = R32_RISK_WEIGHT;
		}

		public BigDecimal getR32_RISK_WEIGHTED_AMT() {
			return R32_RISK_WEIGHTED_AMT;
		}

		public void setR32_RISK_WEIGHTED_AMT(BigDecimal R32_RISK_WEIGHTED_AMT) {
			this.R32_RISK_WEIGHTED_AMT = R32_RISK_WEIGHTED_AMT;
		}

		// ================== R33 GETTERS AND SETTERS ==================
		public String getR33_NAME_OF_CORPORATE() {
			return R33_NAME_OF_CORPORATE;
		}

		public void setR33_NAME_OF_CORPORATE(String R33_NAME_OF_CORPORATE) {
			this.R33_NAME_OF_CORPORATE = R33_NAME_OF_CORPORATE;
		}

		public String getR33_CREDIT_RATING() {
			return R33_CREDIT_RATING;
		}

		public void setR33_CREDIT_RATING(String R33_CREDIT_RATING) {
			this.R33_CREDIT_RATING = R33_CREDIT_RATING;
		}

		public String getR33_RATING_AGENCY() {
			return R33_RATING_AGENCY;
		}

		public void setR33_RATING_AGENCY(String R33_RATING_AGENCY) {
			this.R33_RATING_AGENCY = R33_RATING_AGENCY;
		}

		public BigDecimal getR33_EXPOSURE_AMT() {
			return R33_EXPOSURE_AMT;
		}

		public void setR33_EXPOSURE_AMT(BigDecimal R33_EXPOSURE_AMT) {
			this.R33_EXPOSURE_AMT = R33_EXPOSURE_AMT;
		}

		public BigDecimal getR33_RISK_WEIGHT() {
			return R33_RISK_WEIGHT;
		}

		public void setR33_RISK_WEIGHT(BigDecimal R33_RISK_WEIGHT) {
			this.R33_RISK_WEIGHT = R33_RISK_WEIGHT;
		}

		public BigDecimal getR33_RISK_WEIGHTED_AMT() {
			return R33_RISK_WEIGHTED_AMT;
		}

		public void setR33_RISK_WEIGHTED_AMT(BigDecimal R33_RISK_WEIGHTED_AMT) {
			this.R33_RISK_WEIGHTED_AMT = R33_RISK_WEIGHTED_AMT;
		}

		// ================== R34 GETTERS AND SETTERS ==================
		public String getR34_NAME_OF_CORPORATE() {
			return R34_NAME_OF_CORPORATE;
		}

		public void setR34_NAME_OF_CORPORATE(String R34_NAME_OF_CORPORATE) {
			this.R34_NAME_OF_CORPORATE = R34_NAME_OF_CORPORATE;
		}

		public String getR34_CREDIT_RATING() {
			return R34_CREDIT_RATING;
		}

		public void setR34_CREDIT_RATING(String R34_CREDIT_RATING) {
			this.R34_CREDIT_RATING = R34_CREDIT_RATING;
		}

		public String getR34_RATING_AGENCY() {
			return R34_RATING_AGENCY;
		}

		public void setR34_RATING_AGENCY(String R34_RATING_AGENCY) {
			this.R34_RATING_AGENCY = R34_RATING_AGENCY;
		}

		public BigDecimal getR34_EXPOSURE_AMT() {
			return R34_EXPOSURE_AMT;
		}

		public void setR34_EXPOSURE_AMT(BigDecimal R34_EXPOSURE_AMT) {
			this.R34_EXPOSURE_AMT = R34_EXPOSURE_AMT;
		}

		public BigDecimal getR34_RISK_WEIGHT() {
			return R34_RISK_WEIGHT;
		}

		public void setR34_RISK_WEIGHT(BigDecimal R34_RISK_WEIGHT) {
			this.R34_RISK_WEIGHT = R34_RISK_WEIGHT;
		}

		public BigDecimal getR34_RISK_WEIGHTED_AMT() {
			return R34_RISK_WEIGHTED_AMT;
		}

		public void setR34_RISK_WEIGHTED_AMT(BigDecimal R34_RISK_WEIGHTED_AMT) {
			this.R34_RISK_WEIGHTED_AMT = R34_RISK_WEIGHTED_AMT;
		}

		// ================== R35 GETTERS AND SETTERS ==================
		public String getR35_NAME_OF_CORPORATE() {
			return R35_NAME_OF_CORPORATE;
		}

		public void setR35_NAME_OF_CORPORATE(String R35_NAME_OF_CORPORATE) {
			this.R35_NAME_OF_CORPORATE = R35_NAME_OF_CORPORATE;
		}

		public String getR35_CREDIT_RATING() {
			return R35_CREDIT_RATING;
		}

		public void setR35_CREDIT_RATING(String R35_CREDIT_RATING) {
			this.R35_CREDIT_RATING = R35_CREDIT_RATING;
		}

		public String getR35_RATING_AGENCY() {
			return R35_RATING_AGENCY;
		}

		public void setR35_RATING_AGENCY(String R35_RATING_AGENCY) {
			this.R35_RATING_AGENCY = R35_RATING_AGENCY;
		}

		public BigDecimal getR35_EXPOSURE_AMT() {
			return R35_EXPOSURE_AMT;
		}

		public void setR35_EXPOSURE_AMT(BigDecimal R35_EXPOSURE_AMT) {
			this.R35_EXPOSURE_AMT = R35_EXPOSURE_AMT;
		}

		public BigDecimal getR35_RISK_WEIGHT() {
			return R35_RISK_WEIGHT;
		}

		public void setR35_RISK_WEIGHT(BigDecimal R35_RISK_WEIGHT) {
			this.R35_RISK_WEIGHT = R35_RISK_WEIGHT;
		}

		public BigDecimal getR35_RISK_WEIGHTED_AMT() {
			return R35_RISK_WEIGHTED_AMT;
		}

		public void setR35_RISK_WEIGHTED_AMT(BigDecimal R35_RISK_WEIGHTED_AMT) {
			this.R35_RISK_WEIGHTED_AMT = R35_RISK_WEIGHTED_AMT;
		}

		// ================== R36 GETTERS AND SETTERS ==================
		public String getR36_NAME_OF_CORPORATE() {
			return R36_NAME_OF_CORPORATE;
		}

		public void setR36_NAME_OF_CORPORATE(String R36_NAME_OF_CORPORATE) {
			this.R36_NAME_OF_CORPORATE = R36_NAME_OF_CORPORATE;
		}

		public String getR36_CREDIT_RATING() {
			return R36_CREDIT_RATING;
		}

		public void setR36_CREDIT_RATING(String R36_CREDIT_RATING) {
			this.R36_CREDIT_RATING = R36_CREDIT_RATING;
		}

		public String getR36_RATING_AGENCY() {
			return R36_RATING_AGENCY;
		}

		public void setR36_RATING_AGENCY(String R36_RATING_AGENCY) {
			this.R36_RATING_AGENCY = R36_RATING_AGENCY;
		}

		public BigDecimal getR36_EXPOSURE_AMT() {
			return R36_EXPOSURE_AMT;
		}

		public void setR36_EXPOSURE_AMT(BigDecimal R36_EXPOSURE_AMT) {
			this.R36_EXPOSURE_AMT = R36_EXPOSURE_AMT;
		}

		public BigDecimal getR36_RISK_WEIGHT() {
			return R36_RISK_WEIGHT;
		}

		public void setR36_RISK_WEIGHT(BigDecimal R36_RISK_WEIGHT) {
			this.R36_RISK_WEIGHT = R36_RISK_WEIGHT;
		}

		public BigDecimal getR36_RISK_WEIGHTED_AMT() {
			return R36_RISK_WEIGHTED_AMT;
		}

		public void setR36_RISK_WEIGHTED_AMT(BigDecimal R36_RISK_WEIGHTED_AMT) {
			this.R36_RISK_WEIGHTED_AMT = R36_RISK_WEIGHTED_AMT;
		}

		// ================== R37 GETTERS AND SETTERS ==================
		public String getR37_NAME_OF_CORPORATE() {
			return R37_NAME_OF_CORPORATE;
		}

		public void setR37_NAME_OF_CORPORATE(String R37_NAME_OF_CORPORATE) {
			this.R37_NAME_OF_CORPORATE = R37_NAME_OF_CORPORATE;
		}

		public String getR37_CREDIT_RATING() {
			return R37_CREDIT_RATING;
		}

		public void setR37_CREDIT_RATING(String R37_CREDIT_RATING) {
			this.R37_CREDIT_RATING = R37_CREDIT_RATING;
		}

		public String getR37_RATING_AGENCY() {
			return R37_RATING_AGENCY;
		}

		public void setR37_RATING_AGENCY(String R37_RATING_AGENCY) {
			this.R37_RATING_AGENCY = R37_RATING_AGENCY;
		}

		public BigDecimal getR37_EXPOSURE_AMT() {
			return R37_EXPOSURE_AMT;
		}

		public void setR37_EXPOSURE_AMT(BigDecimal R37_EXPOSURE_AMT) {
			this.R37_EXPOSURE_AMT = R37_EXPOSURE_AMT;
		}

		public BigDecimal getR37_RISK_WEIGHT() {
			return R37_RISK_WEIGHT;
		}

		public void setR37_RISK_WEIGHT(BigDecimal R37_RISK_WEIGHT) {
			this.R37_RISK_WEIGHT = R37_RISK_WEIGHT;
		}

		public BigDecimal getR37_RISK_WEIGHTED_AMT() {
			return R37_RISK_WEIGHTED_AMT;
		}

		public void setR37_RISK_WEIGHTED_AMT(BigDecimal R37_RISK_WEIGHTED_AMT) {
			this.R37_RISK_WEIGHTED_AMT = R37_RISK_WEIGHTED_AMT;
		}
	}

	// =================
	// MODEL AND VIEW
	// =================

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_SRWA12FView(String reportId, String fromdate, String todate, String currency,
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

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// 1️⃣ SUMMARY SECTION
			// ===========================================================

			// ---------- ARCHIVAL SUMMARY ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<M_SRWA_12F_Archival_Summary_Entity> T1Master = getArchivalSummarydatabydateList(d1, version);

				System.out.println("Archival Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- RESUB SUMMARY ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<M_SRWA_12F_Resub_Summary_Entity> T1Master = getResubSummarydatabydateList(d1, version);

				System.out.println("Resub Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- NORMAL SUMMARY ----------
			else {

				List<M_SRWA_12F_Summary_Entity> T1Master = getSummarydatabydateList(d1);

				System.out.println("Normal Summary Size : " + T1Master.size());

				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ===========================================================
			// 2️⃣ DETAIL SECTION
			// ===========================================================

			if ("detail".equalsIgnoreCase(dtltype)) {

				// ---------- ARCHIVAL DETAIL ----------
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12F_Archival_Detail_Entity> T1Master = getArchivalDetaildatabydateList(d1, version);

					System.out.println("Archival Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12F_Resub_Detail_Entity> T1Master = getResubDetaildatabydateList(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}

				// ---------- NORMAL DETAIL ----------
				else {

					List<M_SRWA_12F_Detail_Entity> T1Master = getDetaildatabydateList(d1);

					System.out.println("Normal Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12F");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
	public void updateReport(M_SRWA_12F_Summary_Entity updatedEntity) {
		System.out.println("Came to M_SRWA_12F Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Date reportDate = updatedEntity.getReport_date();

		// 🔹 Fetch existing SUMMARY using JDBC
		String selectSummarySql = "SELECT * FROM BRRS_M_SRWA_12F_SUMMARYTABLE WHERE REPORT_DATE = ?";
		List<M_SRWA_12F_Summary_Entity> existingSummaryList = jdbcTemplate.query(selectSummarySql,
				new Object[] { reportDate }, new M_SRWA_12F_Summary_RowMapper());

		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + reportDate);
		}

		M_SRWA_12F_Summary_Entity existingSummary = existingSummaryList.get(0);

		// 🔹 Create Audit Copy before editing
		M_SRWA_12F_Summary_Entity oldcopy = new M_SRWA_12F_Summary_Entity();
		BeanUtils.copyProperties(existingSummary, oldcopy);

		// 🔹 Fetch or create DETAIL using JDBC
		String selectDetailSql = "SELECT * FROM BRRS_M_SRWA_12F_DETAILTABLE WHERE REPORT_DATE = ?";
		List<M_SRWA_12F_Detail_Entity> existingDetailList = jdbcTemplate.query(selectDetailSql,
				new Object[] { reportDate }, new M_SRWA_12F_Detail_RowMapper());

		M_SRWA_12F_Detail_Entity detailEntity;
		if (existingDetailList.isEmpty()) {
			detailEntity = new M_SRWA_12F_Detail_Entity();
			detailEntity.setReport_date(reportDate);
		} else {
			detailEntity = existingDetailList.get(0);
		}

		try {
			// 🔁 Loop R11 to R37
			for (int i = 11; i <= 37; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT",
						"RISK_WEIGHT", "RISK_WEIGHTED_AMT" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SRWA_12F_Summary_Entity.class.getMethod(getterName);
						Object newValue = getter.invoke(updatedEntity);

						// Get current value from existing record to compare
						Object existingValue = getter.invoke(existingSummary);

						// --- FIX: Normalize nulls vs empty strings to prevent audit bloat ---
						String currentValStr = (existingValue == null) ? "" : existingValue.toString().trim();
						String newValStr = (newValue == null) ? "" : newValue.toString().trim();

						// If values are functionally identical, skip updating to avoid dirtying fields
						if (currentValStr.equals(newValStr)) {
							continue;
						}

						// SUMMARY setter
						Method summarySetter = M_SRWA_12F_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());
						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SRWA_12F_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());
						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Field not present in entity – skip safely
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_SRWA_12F Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 UPDATE SUMMARY TABLE using JDBC
		String updateSummarySql = buildUpdateSummaryQuery(existingSummary, reportDate);
		jdbcTemplate.update(updateSummarySql);

		// 💾 UPDATE/INSERT DETAIL TABLE using JDBC
		String updateDetailSql = buildUpdateDetailQuery(detailEntity, reportDate);
		jdbcTemplate.update(updateDetailSql);

		// Audit
		String changes1 = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_SRWA_12F Changes Length = " + changes1.length());

		if (changes1 != null && !changes1.isEmpty() && changes1.length() < 1800) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
					"M_SRWA_12F Summary Screen", "BRRS_M_SRWA_12F_SUMMARYTABLE");
		}

		System.out.println("Update completed successfully");
	}

	// =====================================================
	// BUILD UPDATE SUMMARY QUERY
	// =====================================================

	private String buildUpdateSummaryQuery(M_SRWA_12F_Summary_Entity entity, Date reportDate) {
		StringBuilder sql = new StringBuilder("UPDATE BRRS_M_SRWA_12F_SUMMARYTABLE SET ");

		// Common fields
		sql.append("REPORT_VERSION = ?, ");
		sql.append("REPORT_FREQUENCY = ?, ");
		sql.append("REPORT_CODE = ?, ");
		sql.append("REPORT_DESC = ?, ");
		sql.append("ENTITY_FLG = ?, ");
		sql.append("MODIFY_FLG = ?, ");
		sql.append("DEL_FLG = ? ");

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(", ").append(prefix).append(field).append(" = ? ");
			}
		}

		sql.append("WHERE REPORT_DATE = ?");

		// Build parameters list
		List<Object> params = new ArrayList<>();

		// Common fields
		params.add(entity.getReport_version());
		params.add(entity.getReport_frequency());
		params.add(entity.getReport_code());
		params.add(entity.getReport_desc());
		params.add(entity.getEntity_flg());
		params.add(entity.getModify_flg());
		params.add(entity.getDel_flg());

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			// Use reflection to get values
			try {
				params.add(getFieldValue(entity, prefix + "NAME_OF_CORPORATE"));
				params.add(getFieldValue(entity, prefix + "CREDIT_RATING"));
				params.add(getFieldValue(entity, prefix + "RATING_AGENCY"));
				params.add(getFieldValue(entity, prefix + "EXPOSURE_AMT"));
				params.add(getFieldValue(entity, prefix + "RISK_WEIGHT"));
				params.add(getFieldValue(entity, prefix + "RISK_WEIGHTED_AMT"));
			} catch (Exception e) {
				// Skip if field not found
			}
		}

		params.add(reportDate);

		return sql.toString();
	}

	// =====================================================
	// BUILD UPDATE DETAIL QUERY
	// =====================================================

	private String buildUpdateDetailQuery(M_SRWA_12F_Detail_Entity entity, Date reportDate) {
		// First check if record exists
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12F_DETAILTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { reportDate }, Integer.class);

		if (count > 0) {
			// UPDATE existing record
			return buildUpdateDetailSql(entity, reportDate);
		} else {
			// INSERT new record
			return buildInsertDetailSql(entity, reportDate);
		}
	}

	// =====================================================
	// BUILD UPDATE DETAIL SQL
	// =====================================================

	private String buildUpdateDetailSql(M_SRWA_12F_Detail_Entity entity, Date reportDate) {
		StringBuilder sql = new StringBuilder("UPDATE BRRS_M_SRWA_12F_DETAILTABLE SET ");

		// Common fields
		sql.append("REPORT_VERSION = ?, ");
		sql.append("REPORT_FREQUENCY = ?, ");
		sql.append("REPORT_CODE = ?, ");
		sql.append("REPORT_DESC = ?, ");
		sql.append("ENTITY_FLG = ?, ");
		sql.append("MODIFY_FLG = ?, ");
		sql.append("DEL_FLG = ? ");

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(", ").append(prefix).append(field).append(" = ? ");
			}
		}

		sql.append("WHERE REPORT_DATE = ?");
		return sql.toString();
	}

	// =====================================================
	// BUILD INSERT DETAIL SQL
	// =====================================================

	private String buildInsertDetailSql(M_SRWA_12F_Detail_Entity entity, Date reportDate) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SRWA_12F_DETAILTABLE (");
		StringBuilder values = new StringBuilder(" VALUES (");

		// Common fields
		String[] commonFields = { "REPORT_DATE", "REPORT_VERSION", "REPORT_FREQUENCY", "REPORT_CODE", "REPORT_DESC",
				"ENTITY_FLG", "MODIFY_FLG", "DEL_FLG" };

		for (String field : commonFields) {
			sql.append(field).append(", ");
			values.append("?, ");
		}

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(prefix).append(field).append(", ");
				values.append("?, ");
			}
		}

		// Remove trailing comma and space
		String sqlStr = sql.toString().replaceAll(", $", ")");
		String valuesStr = values.toString().replaceAll(", $", ")");

		return sqlStr + valuesStr;
	}

	// =====================================================
	// GET FIELD VALUE USING REFLECTION
	// =====================================================

	private Object getFieldValue(Object entity, String fieldName) throws Exception {
		String getterName = "get" + fieldName;
		Method getter = entity.getClass().getMethod(getterName);
		return getter.invoke(entity);
	}

	@Transactional
	public void updateResubReport(M_SRWA_12F_Resub_Summary_Entity updatedEntity) {

		Date report_date = updatedEntity.getReport_date();

		// ----------------------------------------------------
		// 1️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ----------------------------------------------------

		BigDecimal maxResubVer = findMaxVersionResubSummary(report_date);

		if (maxResubVer == null) {
			throw new RuntimeException("No record for: " + report_date);
		}

		BigDecimal newVersion = maxResubVer.add(BigDecimal.ONE);
		Date now = new Date();

		// ====================================================
		// 2️⃣ RESUB SUMMARY – FROM UPDATED VALUES
		// ====================================================

		M_SRWA_12F_Resub_Summary_Entity resubSummary = new M_SRWA_12F_Resub_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, resubSummary, "report_date", "report_version", "report_resubdate");

		resubSummary.setReport_date(report_date);
		resubSummary.setReport_version(newVersion);
		resubSummary.setreport_resubdate(now);

		// ====================================================
		// 3️⃣ RESUB DETAIL – SAME UPDATED VALUES
		// ====================================================

		M_SRWA_12F_Resub_Detail_Entity resubDetail = new M_SRWA_12F_Resub_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, resubDetail, "report_date", "report_version", "report_resubdate");

		resubDetail.setReport_date(report_date);
		resubDetail.setReport_version(newVersion);
		resubDetail.setreport_resubdate(now);

		// ====================================================
		// 4️⃣ ARCHIVAL SUMMARY – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12F_Archival_Summary_Entity archSummary = new M_SRWA_12F_Archival_Summary_Entity();

		BeanUtils.copyProperties(updatedEntity, archSummary, "report_date", "report_version", "report_resubdate");

		archSummary.setReport_date(report_date);
		archSummary.setReport_version(newVersion);
		archSummary.setreport_resubdate(now);

		// ====================================================
		// 5️⃣ ARCHIVAL DETAIL – SAME VALUES + SAME VERSION
		// ====================================================

		M_SRWA_12F_Archival_Detail_Entity archDetail = new M_SRWA_12F_Archival_Detail_Entity();

		BeanUtils.copyProperties(updatedEntity, archDetail, "report_date", "report_version", "report_resubdate");

		archDetail.setReport_date(report_date);
		archDetail.setReport_version(newVersion);
		archDetail.setreport_resubdate(now);

		// ====================================================
		// 6️⃣ SAVE ALL USING JDBC
		// ====================================================

		// Save Resub Summary
		String insertResubSummarySql = buildInsertResubSummarySql(resubSummary);
		jdbcTemplate.update(insertResubSummarySql);

		// Save Resub Detail
		String insertResubDetailSql = buildInsertResubDetailSql(resubDetail);
		jdbcTemplate.update(insertResubDetailSql);

		// Save Archival Summary
		String insertArchSummarySql = buildInsertArchivalSummarySql(archSummary);
		jdbcTemplate.update(insertArchSummarySql);

		// Save Archival Detail
		String insertArchDetailSql = buildInsertArchivalDetailSql(archDetail);
		jdbcTemplate.update(insertArchDetailSql);
	}

	// =====================================================
	// BUILD INSERT RESUB SUMMARY SQL
	// =====================================================

	private String buildInsertResubSummarySql(M_SRWA_12F_Resub_Summary_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE (");
		StringBuilder values = new StringBuilder(" VALUES (");

		// Common fields
		String[] commonFields = { "REPORT_DATE", "REPORT_VERSION", "REPORT_RESUBDATE", "REPORT_FREQUENCY",
				"REPORT_CODE", "REPORT_DESC", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG" };

		List<Object> params = new ArrayList<>();

		for (String field : commonFields) {
			sql.append(field).append(", ");
			values.append("?, ");
		}

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(prefix).append(field).append(", ");
				values.append("?, ");
			}
		}

		// Remove trailing comma and space
		String sqlStr = sql.toString().replaceAll(", $", ")");
		String valuesStr = values.toString().replaceAll(", $", ")");

		return sqlStr + valuesStr;
	}

	// =====================================================
	// BUILD INSERT RESUB DETAIL SQL
	// =====================================================

	private String buildInsertResubDetailSql(M_SRWA_12F_Resub_Detail_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SRWA_12F_RESUB_DETAILTABLE (");
		StringBuilder values = new StringBuilder(" VALUES (");

		// Common fields
		String[] commonFields = { "REPORT_DATE", "REPORT_VERSION", "REPORT_RESUBDATE", "REPORT_FREQUENCY",
				"REPORT_CODE", "REPORT_DESC", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG" };

		for (String field : commonFields) {
			sql.append(field).append(", ");
			values.append("?, ");
		}

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(prefix).append(field).append(", ");
				values.append("?, ");
			}
		}

		// Remove trailing comma and space
		String sqlStr = sql.toString().replaceAll(", $", ")");
		String valuesStr = values.toString().replaceAll(", $", ")");

		return sqlStr + valuesStr;
	}

	// =====================================================
	// BUILD INSERT ARCHIVAL SUMMARY SQL
	// =====================================================

	private String buildInsertArchivalSummarySql(M_SRWA_12F_Archival_Summary_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY (");
		StringBuilder values = new StringBuilder(" VALUES (");

		// Common fields
		String[] commonFields = { "REPORT_DATE", "REPORT_VERSION", "REPORT_RESUBDATE", "REPORT_FREQUENCY",
				"REPORT_CODE", "REPORT_DESC", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG" };

		for (String field : commonFields) {
			sql.append(field).append(", ");
			values.append("?, ");
		}

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(prefix).append(field).append(", ");
				values.append("?, ");
			}
		}

		// Remove trailing comma and space
		String sqlStr = sql.toString().replaceAll(", $", ")");
		String valuesStr = values.toString().replaceAll(", $", ")");

		return sqlStr + valuesStr;
	}

	// =====================================================
	// BUILD INSERT ARCHIVAL DETAIL SQL
	// =====================================================

	private String buildInsertArchivalDetailSql(M_SRWA_12F_Archival_Detail_Entity entity) {
		StringBuilder sql = new StringBuilder("INSERT INTO BRRS_M_SRWA_12F_ARCHIVALTABLE_DETAIL (");
		StringBuilder values = new StringBuilder(" VALUES (");

		// Common fields
		String[] commonFields = { "REPORT_DATE", "REPORT_VERSION", "REPORT_RESUBDATE", "REPORT_FREQUENCY",
				"REPORT_CODE", "REPORT_DESC", "ENTITY_FLG", "MODIFY_FLG", "DEL_FLG" };

		for (String field : commonFields) {
			sql.append(field).append(", ");
			values.append("?, ");
		}

		// Add R11 to R37 fields
		for (int i = 11; i <= 37; i++) {
			String prefix = "R" + i + "_";
			String[] fields = { "NAME_OF_CORPORATE", "CREDIT_RATING", "RATING_AGENCY", "EXPOSURE_AMT", "RISK_WEIGHT",
					"RISK_WEIGHTED_AMT" };
			for (String field : fields) {
				sql.append(prefix).append(field).append(", ");
				values.append("?, ");
			}
		}

		// Remove trailing comma and space
		String sqlStr = sql.toString().replaceAll(", $", ")");
		String valuesStr = values.toString().replaceAll(", $", ")");

		return sqlStr + valuesStr;
	}

	public List<Object[]> getM_SRWA_12FResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			// Using JDBC to fetch archival summary data with version
			List<M_SRWA_12F_Archival_Summary_Entity> latestArchivalList = getArchivalSummaryListWithVersion();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12F_Archival_Summary_Entity entity : latestArchivalList) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReport_resubdate() };
					resubList.add(row);
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}
		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12F Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Archival View
	public List<Object[]> getM_SRWA_12FArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			// Using JDBC to fetch archival summary data with version
			List<M_SRWA_12F_Archival_Summary_Entity> repoData = getArchivalSummaryListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12F_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReport_resubdate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12F_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12F Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// Normal format Excel

	public byte[] getM_SRWA_12FExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
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
				return getExcelM_SRWA_12FARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12FEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<M_SRWA_12F_Summary_Entity> dataList = getSummarydatabydateList(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
					// --- End of Style Definitions ---

					int startRow = 5;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							M_SRWA_12F_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell R12Cell = row.createCell(2);

							if (record.getReport_date() != null) {

								R12Cell.setCellValue(record.getReport_date());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							row = sheet.getRow(10);
							Cell cell1 = row.createCell(1);
							if (record.getR11_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							Cell cell2 = row.createCell(2);
							if (record.getR11_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR11_CREDIT_RATING());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR11_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR11_RATING_AGENCY());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// row11
							// Column E
							Cell cell4 = row.createCell(4);
							if (record.getR11_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row11
							// Column F
							Cell cell5 = row.createCell(5);
							if (record.getR11_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);

							cell1 = row.createCell(1);
							if (record.getR12_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
								cell1.setCellStyle(textStyle);
							} else {
								cell1.setCellValue("");
								cell1.setCellStyle(textStyle);
							}

							cell2 = row.createCell(2);
							if (record.getR12_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR12_CREDIT_RATING());
								cell2.setCellStyle(textStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR12_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR12_RATING_AGENCY());
								cell3.setCellStyle(textStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}
							// Column E
							cell4 = row.createCell(4);
							if (record.getR12_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row12
							// Column F
							cell5 = row.createCell(5);
							if (record.getR12_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);
							// R13_NAME_OF_CORPORATE
							cell1 = row.createCell(1);
							if (record.getR13_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
							} else {
								cell1.setCellValue("");
							}
							cell1.setCellStyle(textStyle);

							// R13_CREDIT_RATING
							cell2 = row.createCell(2);
							if (record.getR13_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR13_CREDIT_RATING());
							} else {
								cell2.setCellValue("");
							}
							cell2.setCellStyle(textStyle);

							// R13_RATING_AGENCY
							cell3 = row.createCell(3);
							if (record.getR13_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR13_RATING_AGENCY());
							} else {
								cell3.setCellValue("");
							}
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR13_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row13
							// Column F
							cell5 = row.createCell(5);
							if (record.getR13_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row14
							row = sheet.getRow(13);
							// R14_NAME_OF_CORPORATE
							cell1 = row.createCell(1);
							if (record.getR14_NAME_OF_CORPORATE() != null) {
								cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
							} else {
								cell1.setCellValue("");
							}
							cell1.setCellStyle(textStyle);

							// R14_CREDIT_RATING
							cell2 = row.createCell(2);
							if (record.getR14_CREDIT_RATING() != null) {
								cell2.setCellValue(record.getR14_CREDIT_RATING());
							} else {
								cell2.setCellValue("");
							}
							cell2.setCellStyle(textStyle);

							// R14_RATING_AGENCY
							cell3 = row.createCell(3);
							if (record.getR14_RATING_AGENCY() != null) {
								cell3.setCellValue(record.getR14_RATING_AGENCY());
							} else {
								cell3.setCellValue("");
							}
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR14_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row14
							// Column F
							cell5 = row.createCell(5);
							if (record.getR14_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR15_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row15
							// Column F
							cell5 = row.createCell(5);
							if (record.getR15_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR16_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row16
							// Column F
							cell5 = row.createCell(5);
							if (record.getR16_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR17_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row17
							// Column F
							cell5 = row.createCell(5);
							if (record.getR17_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);
							// R18 Cells
							cell1 = row.createCell(1); // adjust column index as needed
							cell1.setCellValue(
									record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR18_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row18
							// Column F
							cell5 = row.createCell(5);
							if (record.getR18_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR19_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row19
							// Column F
							cell5 = row.createCell(5);
							if (record.getR19_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR20_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row20
							// Column F
							cell5 = row.createCell(5);
							if (record.getR20_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR21_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row21
							// Column F
							cell5 = row.createCell(5);
							if (record.getR21_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR22_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row22
							// Column F
							cell5 = row.createCell(5);
							if (record.getR22_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR23_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row23
							// Column F
							cell5 = row.createCell(5);
							if (record.getR23_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR24_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row24
							// Column F
							cell5 = row.createCell(5);
							if (record.getR24_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR25_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row25
							// Column F
							cell5 = row.createCell(5);
							if (record.getR25_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR26_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row26
							// Column F
							cell5 = row.createCell(5);
							if (record.getR26_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR27_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row27
							// Column F
							cell5 = row.createCell(5);
							if (record.getR27_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row28
							row = sheet.getRow(27);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR28_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row28
							// Column F
							cell5 = row.createCell(5);
							if (record.getR28_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR29_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row29
							// Column F
							cell5 = row.createCell(5);
							if (record.getR29_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR30_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row30
							// Column F
							cell5 = row.createCell(5);
							if (record.getR30_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR31_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row31
							// Column F
							cell5 = row.createCell(5);
							if (record.getR31_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR32_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row32
							// Column F
							cell5 = row.createCell(5);
							if (record.getR32_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR33_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row33
							// Column F
							cell5 = row.createCell(5);
							if (record.getR33_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR34_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row34
							// Column F
							cell5 = row.createCell(5);
							if (record.getR34_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);
							// Column E
							cell4 = row.createCell(4);
							if (record.getR35_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							cell5 = row.createCell(5);
							if (record.getR35_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

							// row35
							// Column F
							row = sheet.getRow(35);
							cell1 = row.createCell(1);
							cell1.setCellValue(
									record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
							cell1.setCellStyle(textStyle);

							cell2 = row.createCell(2);
							cell2.setCellValue(
									record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
							cell2.setCellStyle(textStyle);

							cell3 = row.createCell(3);
							cell3.setCellValue(
									record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
							cell3.setCellStyle(textStyle);

							// Column E
							cell4 = row.createCell(4);
							if (record.getR36_EXPOSURE_AMT() != null) {
								cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							// row36
							// Column F
							cell5 = row.createCell(5);
							if (record.getR36_RISK_WEIGHT() != null) {
								cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
								cell5.setCellStyle(numberStyle);
							} else {
								cell5.setCellValue("");
								cell5.setCellStyle(textStyle);
							}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					// audit service summary format

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F SUMMARY", null,
								"BRRS_M_SRWA_12F_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_SRWA_12FEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12FEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			// ✅ FIX: Using JDBC method
			List<M_SRWA_12F_Summary_Entity> dataList = getSummarydatabydateList(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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
				// --- End of Style Definitions ---

				int startRow = 5;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SRWA_12F_Summary_Entity record1 = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(4);

						if (record1.getReport_date() != null) {

							R12Cell.setCellValue(record1.getReport_date());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(10);
						// row11
						// Column E
						Cell cell1 = row.createCell(1);
						if (record1.getR11_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR11_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR11_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(4);
						if (record1.getR11_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR11_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						// row11
						// Column E
						Cell cell4 = row.createCell(6);
						if (record1.getR11_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row11
						// Column F
						Cell cell5 = row.createCell(7);
						if (record1.getR11_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						// row11
						// Column F
						Cell cell6 = row.createCell(8);
						if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row12
						row = sheet.getRow(11);

						cell1 = row.createCell(1);
						if (record1.getR12_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR12_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR12_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR12_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR12_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}
						// Column E
						cell4 = row.createCell(6);
						if (record1.getR12_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						// row12
						// Column F
						cell5 = row.createCell(7);
						if (record1.getR12_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						cell6 = row.createCell(8);
						if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row13
						row = sheet.getRow(12);

						cell1 = row.createCell(1);
						if (record1.getR13_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR13_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR13_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR13_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR13_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR13_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR13_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row14
						row = sheet.getRow(13);

						cell1 = row.createCell(1);
						if (record1.getR14_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR14_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR14_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR14_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR14_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR14_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR14_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row15
						row = sheet.getRow(14);

						cell1 = row.createCell(1);
						if (record1.getR15_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR15_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR15_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR15_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR15_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR15_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR15_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row16
						row = sheet.getRow(15);

						cell1 = row.createCell(1);
						if (record1.getR16_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR16_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR16_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR16_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR16_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR16_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR16_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row17
						row = sheet.getRow(16);

						cell1 = row.createCell(1);
						if (record1.getR17_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR17_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR17_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR17_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR17_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR17_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR17_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row18
						row = sheet.getRow(17);

						cell1 = row.createCell(1);
						if (record1.getR18_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR18_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR18_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR18_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR18_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR18_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR18_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row19
						row = sheet.getRow(18);

						cell1 = row.createCell(1);
						if (record1.getR19_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR19_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR19_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR19_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR19_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR19_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR19_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row20
						row = sheet.getRow(19);

						cell1 = row.createCell(1);
						if (record1.getR20_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR20_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR20_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR20_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR20_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR20_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR20_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row21
						row = sheet.getRow(20);

						cell1 = row.createCell(1);
						if (record1.getR21_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR21_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR21_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR21_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR21_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR21_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR21_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R22
						// ==========================
						row = sheet.getRow(21);

						cell1 = row.createCell(1);
						if (record1.getR22_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR22_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR22_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR22_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR22_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR22_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR22_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R23
						// ==========================
						row = sheet.getRow(22);

						cell1 = row.createCell(1);
						if (record1.getR23_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR23_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR23_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR23_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR23_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR23_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR23_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// ==========================
						// R24
						// ==========================
						row = sheet.getRow(23);

						cell1 = row.createCell(1);
						if (record1.getR24_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR24_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR24_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR24_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR24_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR24_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR24_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						// row25
						row = sheet.getRow(24);

						cell1 = row.createCell(1);
						if (record1.getR25_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR25_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR25_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR25_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR25_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR25_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR25_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row26
						row = sheet.getRow(25);

						cell1 = row.createCell(1);
						if (record1.getR26_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR26_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR26_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR26_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR26_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR26_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR26_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row27
						row = sheet.getRow(26);

						cell1 = row.createCell(1);
						if (record1.getR27_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR27_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR27_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR27_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR27_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR27_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR27_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row28
						row = sheet.getRow(27);

						cell1 = row.createCell(1);
						if (record1.getR28_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR28_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR28_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR28_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR28_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR28_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR28_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row29
						row = sheet.getRow(28);

						cell1 = row.createCell(1);
						if (record1.getR29_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR29_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR29_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR29_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR29_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR29_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR29_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row30
						row = sheet.getRow(29);

						cell1 = row.createCell(1);
						if (record1.getR30_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR30_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR30_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR30_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR30_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR30_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR30_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row31
						row = sheet.getRow(30);

						cell1 = row.createCell(1);
						if (record1.getR31_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR31_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR31_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR31_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR31_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR31_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR31_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row32
						row = sheet.getRow(31);

						cell1 = row.createCell(1);
						if (record1.getR32_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR32_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR32_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR32_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR32_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR32_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR32_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row33
						row = sheet.getRow(32);

						cell1 = row.createCell(1);
						if (record1.getR33_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR33_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR33_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR33_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR33_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR33_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR33_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row34
						row = sheet.getRow(33);

						cell1 = row.createCell(1);
						if (record1.getR34_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR34_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR34_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR34_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR34_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR34_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR34_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row35
						row = sheet.getRow(34);

						cell1 = row.createCell(1);
						if (record1.getR35_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR35_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR35_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR35_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR35_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR35_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR35_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row36
						row = sheet.getRow(35);

						cell1 = row.createCell(1);
						if (record1.getR36_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR36_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR36_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR36_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR36_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR36_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR36_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						// row37
						row = sheet.getRow(36);

						cell1 = row.createCell(1);
						if (record1.getR37_NAME_OF_CORPORATE() != null) {
							cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
							cell1.setCellStyle(textStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_RATING() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_RATING());
							cell2.setCellStyle(textStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(4);
						if (record1.getR37_RATING_AGENCY() != null) {
							cell3.setCellValue(record1.getR37_RATING_AGENCY());
							cell3.setCellStyle(textStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(6);
						if (record1.getR37_EXPOSURE_AMT() != null) {
							cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(7);
						if (record1.getR37_RISK_WEIGHT() != null) {
							cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(8);
						if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
							cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}

				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				// audit service summary email

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F EMAIL SUMMARY", null,
							"BRRS_M_SRWA_12F_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_SRWA_12FARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12FEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		// ✅ FIX: Using JDBC method
		List<M_SRWA_12F_Archival_Summary_Entity> dataList = getArchivalSummarydatabydateList(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12F report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12F_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					Cell cell1 = row.createCell(1);
					if (record.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// R13_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R13_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR13_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R13_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR13_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// R14_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R14_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR14_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R14_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR14_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// R18 Cells
					cell1 = row.createCell(1); // adjust column index as needed
					cell1.setCellValue(
							record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column F
					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F ARCHIVAL SUMMARY", null,
						"BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_SRWA_12FEmailArchivalExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		// ✅ FIX: Using JDBC method
		List<M_SRWA_12F_Archival_Summary_Entity> dataList = getArchivalSummarydatabydateList(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12F_Archival_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(4);

					if (record1.getReport_date() != null) {

						R12Cell.setCellValue(record1.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					// row11
					// Column E
					Cell cell1 = row.createCell(1);
					if (record1.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(4);
					if (record1.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(6);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(7);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// row11
					// Column F
					Cell cell6 = row.createCell(8);
					if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record1.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(6);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(7);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					cell6 = row.createCell(8);
					if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell1 = row.createCell(1);
					if (record1.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR13_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR13_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell1 = row.createCell(1);
					if (record1.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR14_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR14_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					cell1 = row.createCell(1);
					if (record1.getR15_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR15_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR15_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR15_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR15_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell1 = row.createCell(1);
					if (record1.getR16_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR16_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR16_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR16_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR16_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					cell1 = row.createCell(1);
					if (record1.getR17_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR17_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR17_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR17_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR17_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell1 = row.createCell(1);
					if (record1.getR18_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR18_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR18_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR18_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR18_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					cell1 = row.createCell(1);
					if (record1.getR19_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR19_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR19_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR19_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR19_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell1 = row.createCell(1);
					if (record1.getR20_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR20_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR20_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR20_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR20_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					cell1 = row.createCell(1);
					if (record1.getR21_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR21_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR21_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR21_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR21_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R22
					// ==========================
					row = sheet.getRow(21);

					cell1 = row.createCell(1);
					if (record1.getR22_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR22_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR22_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR22_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR22_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R23
					// ==========================
					row = sheet.getRow(22);

					cell1 = row.createCell(1);
					if (record1.getR23_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR23_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR23_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR23_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR23_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R24
					// ==========================
					row = sheet.getRow(23);

					cell1 = row.createCell(1);
					if (record1.getR24_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR24_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR24_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR24_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR24_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell1 = row.createCell(1);
					if (record1.getR25_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR25_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR25_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR25_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR25_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell1 = row.createCell(1);
					if (record1.getR26_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR26_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR26_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR26_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR26_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell1 = row.createCell(1);
					if (record1.getR27_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR27_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR27_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR27_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR27_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell1 = row.createCell(1);
					if (record1.getR28_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR28_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR28_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR28_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR28_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell1 = row.createCell(1);
					if (record1.getR29_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR29_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR29_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR29_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR29_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell1 = row.createCell(1);
					if (record1.getR30_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR30_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR30_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR30_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR30_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell1 = row.createCell(1);
					if (record1.getR31_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR31_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR31_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR31_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR31_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell1 = row.createCell(1);
					if (record1.getR32_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR32_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR32_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR32_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR32_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell1 = row.createCell(1);
					if (record1.getR33_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR33_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR33_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR33_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell1 = row.createCell(1);
					if (record1.getR34_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR34_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR34_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR34_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR34_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell1 = row.createCell(1);
					if (record1.getR35_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR35_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR35_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR35_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR35_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell1 = row.createCell(1);
					if (record1.getR36_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR36_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR36_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR36_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR36_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell1 = row.createCell(1);
					if (record1.getR37_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR37_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR37_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR37_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR37_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR37_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR37_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service archival summary email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_SRWA_12F_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_SRWA_12FResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12FEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		// ✅ FIX: Using JDBC method
		List<M_SRWA_12F_Resub_Summary_Entity> dataList = getResubSummarydatabydateList(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12F report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_SRWA_12F_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					Cell cell1 = row.createCell(1);
					if (record.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(4);
					if (record.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(5);
					if (record.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(4);
					if (record.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(5);
					if (record.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);
					// R13_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR13_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R13_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR13_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R13_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR13_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row13
					// Column F
					cell5 = row.createCell(5);
					if (record.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);
					// R14_NAME_OF_CORPORATE
					cell1 = row.createCell(1);
					if (record.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record.getR14_NAME_OF_CORPORATE());
					} else {
						cell1.setCellValue("");
					}
					cell1.setCellStyle(textStyle);

					// R14_CREDIT_RATING
					cell2 = row.createCell(2);
					if (record.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record.getR14_CREDIT_RATING());
					} else {
						cell2.setCellValue("");
					}
					cell2.setCellStyle(textStyle);

					// R14_RATING_AGENCY
					cell3 = row.createCell(3);
					if (record.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record.getR14_RATING_AGENCY());
					} else {
						cell3.setCellValue("");
					}
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row14
					// Column F
					cell5 = row.createCell(5);
					if (record.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR15_NAME_OF_CORPORATE() != null ? record.getR15_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR15_CREDIT_RATING() != null ? record.getR15_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR15_RATING_AGENCY() != null ? record.getR15_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row15
					// Column F
					cell5 = row.createCell(5);
					if (record.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR16_NAME_OF_CORPORATE() != null ? record.getR16_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR16_CREDIT_RATING() != null ? record.getR16_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR16_RATING_AGENCY() != null ? record.getR16_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row16
					// Column F
					cell5 = row.createCell(5);
					if (record.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR17_NAME_OF_CORPORATE() != null ? record.getR17_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR17_CREDIT_RATING() != null ? record.getR17_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR17_RATING_AGENCY() != null ? record.getR17_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row17
					// Column F
					cell5 = row.createCell(5);
					if (record.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);
					// R18 Cells
					cell1 = row.createCell(1); // adjust column index as needed
					cell1.setCellValue(
							record.getR18_NAME_OF_CORPORATE() != null ? record.getR18_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR18_CREDIT_RATING() != null ? record.getR18_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR18_RATING_AGENCY() != null ? record.getR18_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row18
					// Column F
					cell5 = row.createCell(5);
					if (record.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR19_NAME_OF_CORPORATE() != null ? record.getR19_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR19_CREDIT_RATING() != null ? record.getR19_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR19_RATING_AGENCY() != null ? record.getR19_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row19
					// Column F
					cell5 = row.createCell(5);
					if (record.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR20_NAME_OF_CORPORATE() != null ? record.getR20_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR20_CREDIT_RATING() != null ? record.getR20_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR20_RATING_AGENCY() != null ? record.getR20_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row20
					// Column F
					cell5 = row.createCell(5);
					if (record.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR21_NAME_OF_CORPORATE() != null ? record.getR21_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR21_CREDIT_RATING() != null ? record.getR21_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR21_RATING_AGENCY() != null ? record.getR21_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row21
					// Column F
					cell5 = row.createCell(5);
					if (record.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR22_NAME_OF_CORPORATE() != null ? record.getR22_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR22_CREDIT_RATING() != null ? record.getR22_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR22_RATING_AGENCY() != null ? record.getR22_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row22
					// Column F
					cell5 = row.createCell(5);
					if (record.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR23_NAME_OF_CORPORATE() != null ? record.getR23_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR23_CREDIT_RATING() != null ? record.getR23_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR23_RATING_AGENCY() != null ? record.getR23_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row23
					// Column F
					cell5 = row.createCell(5);
					if (record.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR24_NAME_OF_CORPORATE() != null ? record.getR24_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR24_CREDIT_RATING() != null ? record.getR24_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR24_RATING_AGENCY() != null ? record.getR24_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row24
					// Column F
					cell5 = row.createCell(5);
					if (record.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR25_NAME_OF_CORPORATE() != null ? record.getR25_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR25_CREDIT_RATING() != null ? record.getR25_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR25_RATING_AGENCY() != null ? record.getR25_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row25
					// Column F
					cell5 = row.createCell(5);
					if (record.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR26_NAME_OF_CORPORATE() != null ? record.getR26_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR26_CREDIT_RATING() != null ? record.getR26_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR26_RATING_AGENCY() != null ? record.getR26_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row26
					// Column F
					cell5 = row.createCell(5);
					if (record.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR27_NAME_OF_CORPORATE() != null ? record.getR27_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR27_CREDIT_RATING() != null ? record.getR27_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR27_RATING_AGENCY() != null ? record.getR27_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row27
					// Column F
					cell5 = row.createCell(5);
					if (record.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR28_NAME_OF_CORPORATE() != null ? record.getR28_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR28_CREDIT_RATING() != null ? record.getR28_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR28_RATING_AGENCY() != null ? record.getR28_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row28
					// Column F
					cell5 = row.createCell(5);
					if (record.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR29_NAME_OF_CORPORATE() != null ? record.getR29_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR29_CREDIT_RATING() != null ? record.getR29_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR29_RATING_AGENCY() != null ? record.getR29_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row29
					// Column F
					cell5 = row.createCell(5);
					if (record.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR30_NAME_OF_CORPORATE() != null ? record.getR30_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR30_CREDIT_RATING() != null ? record.getR30_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR30_RATING_AGENCY() != null ? record.getR30_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row30
					// Column F
					cell5 = row.createCell(5);
					if (record.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR31_NAME_OF_CORPORATE() != null ? record.getR31_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR31_CREDIT_RATING() != null ? record.getR31_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR31_RATING_AGENCY() != null ? record.getR31_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row31
					// Column F
					cell5 = row.createCell(5);
					if (record.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR32_NAME_OF_CORPORATE() != null ? record.getR32_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR32_CREDIT_RATING() != null ? record.getR32_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR32_RATING_AGENCY() != null ? record.getR32_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row32
					// Column F
					cell5 = row.createCell(5);
					if (record.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR33_NAME_OF_CORPORATE() != null ? record.getR33_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR33_CREDIT_RATING() != null ? record.getR33_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR33_RATING_AGENCY() != null ? record.getR33_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row33
					// Column F
					cell5 = row.createCell(5);
					if (record.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR34_NAME_OF_CORPORATE() != null ? record.getR34_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR34_CREDIT_RATING() != null ? record.getR34_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR34_RATING_AGENCY() != null ? record.getR34_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row34
					// Column F
					cell5 = row.createCell(5);
					if (record.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR35_NAME_OF_CORPORATE() != null ? record.getR35_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR35_CREDIT_RATING() != null ? record.getR35_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR35_RATING_AGENCY() != null ? record.getR35_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);
					// Column E
					cell4 = row.createCell(4);
					if (record.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// row35
					// Column F
					row = sheet.getRow(35);
					cell1 = row.createCell(1);
					cell1.setCellValue(
							record.getR36_NAME_OF_CORPORATE() != null ? record.getR36_NAME_OF_CORPORATE() : "");
					cell1.setCellStyle(textStyle);

					cell2 = row.createCell(2);
					cell2.setCellValue(record.getR36_CREDIT_RATING() != null ? record.getR36_CREDIT_RATING() : "");
					cell2.setCellStyle(textStyle);

					cell3 = row.createCell(3);
					cell3.setCellValue(record.getR36_RATING_AGENCY() != null ? record.getR36_RATING_AGENCY() : "");
					cell3.setCellStyle(textStyle);

					// Column E
					cell4 = row.createCell(4);
					if (record.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row36
					// Column F
					cell5 = row.createCell(5);
					if (record.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub format

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F RESUB SUMMARY", null,
						"BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();

		}
	}

	// Resub Email Excel
	public byte[] BRRS_M_SRWA_12FEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		// ✅ FIX: Using JDBC method
		List<M_SRWA_12F_Resub_Summary_Entity> dataList = getResubSummarydatabydateList(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12F report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					M_SRWA_12F_Resub_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(4);

					if (record1.getReport_date() != null) {

						R12Cell.setCellValue(record1.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(10);
					// row11
					// Column E
					Cell cell1 = row.createCell(1);
					if (record1.getR11_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR11_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR11_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR11_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(4);
					if (record1.getR11_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR11_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// row11
					// Column E
					Cell cell4 = row.createCell(6);
					if (record1.getR11_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR11_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row11
					// Column F
					Cell cell5 = row.createCell(7);
					if (record1.getR11_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR11_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					// row11
					// Column F
					Cell cell6 = row.createCell(8);
					if (record1.getR11_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR11_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					cell1 = row.createCell(1);
					if (record1.getR12_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR12_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR12_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR12_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR12_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR12_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					// Column E
					cell4 = row.createCell(6);
					if (record1.getR12_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR12_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// row12
					// Column F
					cell5 = row.createCell(7);
					if (record1.getR12_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR12_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					cell6 = row.createCell(8);
					if (record1.getR12_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR12_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					cell1 = row.createCell(1);
					if (record1.getR13_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR13_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR13_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR13_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR13_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR13_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR13_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR13_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR13_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR13_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR13_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR13_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row14
					row = sheet.getRow(13);

					cell1 = row.createCell(1);
					if (record1.getR14_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR14_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR14_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR14_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR14_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR14_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR14_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR14_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR14_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR14_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR14_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR14_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					cell1 = row.createCell(1);
					if (record1.getR15_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR15_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR15_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR15_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR15_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR15_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR15_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR15_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR15_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR15_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR15_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR15_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					cell1 = row.createCell(1);
					if (record1.getR16_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR16_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR16_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR16_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR16_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR16_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR16_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR16_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR16_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR16_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR16_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR16_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					cell1 = row.createCell(1);
					if (record1.getR17_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR17_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR17_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR17_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR17_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR17_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR17_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR17_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR17_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR17_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR17_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR17_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					cell1 = row.createCell(1);
					if (record1.getR18_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR18_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR18_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR18_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR18_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR18_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR18_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR18_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR18_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR18_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR18_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR18_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					cell1 = row.createCell(1);
					if (record1.getR19_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR19_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR19_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR19_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR19_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR19_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR19_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR19_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR19_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR19_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR19_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR19_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					cell1 = row.createCell(1);
					if (record1.getR20_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR20_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR20_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR20_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR20_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR20_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR20_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR20_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR20_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR20_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR20_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR20_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					cell1 = row.createCell(1);
					if (record1.getR21_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR21_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR21_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR21_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR21_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR21_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR21_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR21_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR21_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR21_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR21_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR21_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R22
					// ==========================
					row = sheet.getRow(21);

					cell1 = row.createCell(1);
					if (record1.getR22_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR22_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR22_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR22_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR22_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR22_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR22_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR22_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR22_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR22_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR22_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR22_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R23
					// ==========================
					row = sheet.getRow(22);

					cell1 = row.createCell(1);
					if (record1.getR23_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR23_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR23_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR23_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR23_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR23_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR23_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR23_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR23_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR23_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR23_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR23_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// ==========================
					// R24
					// ==========================
					row = sheet.getRow(23);

					cell1 = row.createCell(1);
					if (record1.getR24_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR24_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR24_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR24_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR24_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR24_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR24_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR24_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR24_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR24_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR24_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR24_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					// row25
					row = sheet.getRow(24);

					cell1 = row.createCell(1);
					if (record1.getR25_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR25_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR25_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR25_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR25_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR25_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR25_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR25_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR25_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR25_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR25_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR25_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					cell1 = row.createCell(1);
					if (record1.getR26_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR26_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR26_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR26_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR26_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR26_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR26_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR26_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR26_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR26_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR26_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR26_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					cell1 = row.createCell(1);
					if (record1.getR27_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR27_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR27_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR27_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR27_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR27_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR27_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR27_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR27_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR27_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR27_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR27_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row28
					row = sheet.getRow(27);

					cell1 = row.createCell(1);
					if (record1.getR28_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR28_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR28_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR28_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR28_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR28_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR28_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR28_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR28_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR28_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR28_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR28_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					cell1 = row.createCell(1);
					if (record1.getR29_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR29_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR29_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR29_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR29_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR29_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR29_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR29_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR29_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR29_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR29_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR29_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					cell1 = row.createCell(1);
					if (record1.getR30_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR30_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR30_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR30_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR30_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR30_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR30_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR30_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR30_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR30_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR30_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR30_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					cell1 = row.createCell(1);
					if (record1.getR31_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR31_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR31_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR31_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR31_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR31_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR31_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR31_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR31_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR31_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR31_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR31_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					cell1 = row.createCell(1);
					if (record1.getR32_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR32_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR32_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR32_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR32_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR32_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR32_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR32_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR32_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR32_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR32_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR32_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					cell1 = row.createCell(1);
					if (record1.getR33_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR33_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR33_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR33_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR33_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR33_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR33_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR33_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR33_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR33_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR33_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR33_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					cell1 = row.createCell(1);
					if (record1.getR34_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR34_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR34_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR34_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR34_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR34_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR34_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR34_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR34_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR34_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR34_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR34_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					cell1 = row.createCell(1);
					if (record1.getR35_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR35_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR35_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR35_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR35_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR35_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR35_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR35_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR35_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR35_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR35_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR35_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					cell1 = row.createCell(1);
					if (record1.getR36_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR36_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR36_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR36_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR36_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR36_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR36_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR36_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR36_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR36_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR36_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR36_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// row37
					row = sheet.getRow(36);

					cell1 = row.createCell(1);
					if (record1.getR37_NAME_OF_CORPORATE() != null) {
						cell1.setCellValue(record1.getR37_NAME_OF_CORPORATE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR37_CREDIT_RATING() != null) {
						cell2.setCellValue(record1.getR37_CREDIT_RATING());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(4);
					if (record1.getR37_RATING_AGENCY() != null) {
						cell3.setCellValue(record1.getR37_RATING_AGENCY());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(6);
					if (record1.getR37_EXPOSURE_AMT() != null) {
						cell4.setCellValue(record1.getR37_EXPOSURE_AMT().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(7);
					if (record1.getR37_RISK_WEIGHT() != null) {
						cell5.setCellValue(record1.getR37_RISK_WEIGHT().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(8);
					if (record1.getR37_RISK_WEIGHTED_AMT() != null) {
						cell6.setCellValue(record1.getR37_RISK_WEIGHTED_AMT().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			// audit service summary resub email

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12F EMAIL RESUB SUMMARY", null,
						"BRRS_M_SRWA_12F_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

}