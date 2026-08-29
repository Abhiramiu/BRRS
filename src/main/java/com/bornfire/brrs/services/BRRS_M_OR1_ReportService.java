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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service

public class BRRS_M_OR1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_OR1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

// =====================================================
// SUMAMRY REPO
// =====================================================

	public List<M_OR1_Summary_Entity> getSummaryDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OR1_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_OR1_Summary_RowMapper());
	}

	// findbyreportdate

	public M_OR1_Summary_Entity findByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OR1_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<M_OR1_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new M_OR1_Summary_RowMapper());

		return list.isEmpty() ? null : list.get(0);
	}

// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================

	public List<Object[]> get_M_OR1_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_OR1_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<M_OR1_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_M_OR1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_OR1_Archival_Summary_RowMapper());
	}

	public List<M_OR1_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_OR1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_OR1_Archival_Summary_RowMapper());
	}

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_OR1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

// =====================================================
// DETAIL REPO
// =====================================================	

	public List<M_OR1_Detail_Entity> getDetaildatabydateList(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_OR1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_OR1_Detail_RowMapper());
	}

// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

	public List<Map<String, Object>> getM_OR1_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_OR1_ARCHIVALTABLE_DETAIL "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<M_OR1_Archival_Detail_Entity> getDetaildatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new M_OR1_Archival_Detail_RowMapper());
	}

	public BigDecimal findDETAILMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_OR1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public M_OR1_Archival_Detail_Entity getArchivalListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new M_OR1_Archival_Detail_RowMapper());
	}

// =====================================================
// RESUB SUMMARY
// =====================================================

	public List<M_OR1_Resub_Summary_Entity> getResubSummarydatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new M_OR1_RESUB_Summary_RowMapper());
	}

	public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_OR1_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Map<String, Object>> getM_OR1_Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_OR1_RESUB_SUMMARYTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public M_OR1_Resub_Summary_Entity getResubSummarydatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new M_OR1_RESUB_Summary_RowMapper());
	}

// =====================================================
// RESUB DETAIL
// =====================================================

	public List<Map<String, Object>> get_M_OR1Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_OR1_RESUB_DETAILTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<M_OR1_Resub_Detail_Entity> getResubDetaildatabydateList(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new M_OR1_RESUB_Detail_RowMapper());
	}

	public BigDecimal findResubDetailMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_OR1_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public M_OR1_Resub_Detail_Entity getdResubDetailDatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_M_OR1_RESUB_DETAILTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new M_OR1_RESUB_Detail_RowMapper());
	}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================

	public class M_OR1_Summary_RowMapper implements RowMapper<M_OR1_Summary_Entity> {

		@Override
		public M_OR1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Summary_Entity obj = new M_OR1_Summary_Entity();

			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

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

	public class M_OR1_Summary_Entity {

		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;
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

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

	}

// =====================================================
// ARCHIVAL  SUMAMRY ENTITY 
// =====================================================

	public class M_OR1_Archival_Summary_RowMapper implements RowMapper<M_OR1_Archival_Summary_Entity> {

		@Override
		public M_OR1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Archival_Summary_Entity obj = new M_OR1_Archival_Summary_Entity();

			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
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

	public class M_OR1_Archival_Summary_Entity {

		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		@Column(name = "REPORT_VERSION")
		@Id
		private BigDecimal report_version;
		@Column(name = "REPORT_RESUBDATE")

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

	}

// =====================================================
// DETAIL ENTITY  M_OR1
// =====================================================	

	public class M_OR1_Detail_RowMapper implements RowMapper<M_OR1_Detail_Entity> {

		@Override
		public M_OR1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Detail_Entity obj = new M_OR1_Detail_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

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

	public class M_OR1_Detail_Entity {
		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;
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

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

	}

// =====================================================
// ARCHIVAL  DETAIL ENTITY 
// =====================================================

	public class M_OR1_Archival_Detail_RowMapper implements RowMapper<M_OR1_Archival_Detail_Entity> {

		@Override
		public M_OR1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Archival_Detail_Entity obj = new M_OR1_Archival_Detail_Entity();

			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
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

	public class M_OR1_Archival_Detail_Entity {
		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

	}

//====================================================================================================================================
// RESUB summary M_OR1
//=====================================================

	public class M_OR1_RESUB_Summary_RowMapper implements RowMapper<M_OR1_Resub_Summary_Entity> {

		@Override
		public M_OR1_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Resub_Summary_Entity obj = new M_OR1_Resub_Summary_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
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

	public class M_OR1_Resub_Summary_Entity {
		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

	}

//=====================================================
// RESUB DETAIL M_OR1
//=====================================================

	public class M_OR1_RESUB_Detail_RowMapper implements RowMapper<M_OR1_Resub_Detail_Entity> {

		@Override
		public M_OR1_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_OR1_Resub_Detail_Entity obj = new M_OR1_Resub_Detail_Entity();
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_month(rs.getString("R10_MONTH"));
			obj.setR10_gross_income(rs.getBigDecimal("R10_GROSS_INCOME"));
			obj.setR10_aggregate_gross_income(rs.getBigDecimal("R10_AGGREGATE_GROSS_INCOME"));
			obj.setR10_risk_weight_factor(rs.getBigDecimal("R10_RISK_WEIGHT_FACTOR"));
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_month(rs.getString("R11_MONTH"));
			obj.setR11_gross_income(rs.getBigDecimal("R11_GROSS_INCOME"));
			obj.setR11_aggregate_gross_income(rs.getBigDecimal("R11_AGGREGATE_GROSS_INCOME"));
			obj.setR11_risk_weight_factor(rs.getBigDecimal("R11_RISK_WEIGHT_FACTOR"));
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_month(rs.getString("R12_MONTH"));
			obj.setR12_gross_income(rs.getBigDecimal("R12_GROSS_INCOME"));
			obj.setR12_aggregate_gross_income(rs.getBigDecimal("R12_AGGREGATE_GROSS_INCOME"));
			obj.setR12_risk_weight_factor(rs.getBigDecimal("R12_RISK_WEIGHT_FACTOR"));
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_month(rs.getString("R13_MONTH"));
			obj.setR13_gross_income(rs.getBigDecimal("R13_GROSS_INCOME"));
			obj.setR13_aggregate_gross_income(rs.getBigDecimal("R13_AGGREGATE_GROSS_INCOME"));
			obj.setR13_risk_weight_factor(rs.getBigDecimal("R13_RISK_WEIGHT_FACTOR"));
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_month(rs.getString("R14_MONTH"));
			obj.setR14_gross_income(rs.getBigDecimal("R14_GROSS_INCOME"));
			obj.setR14_aggregate_gross_income(rs.getBigDecimal("R14_AGGREGATE_GROSS_INCOME"));
			obj.setR14_risk_weight_factor(rs.getBigDecimal("R14_RISK_WEIGHT_FACTOR"));
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_month(rs.getString("R15_MONTH"));
			obj.setR15_gross_income(rs.getBigDecimal("R15_GROSS_INCOME"));
			obj.setR15_aggregate_gross_income(rs.getBigDecimal("R15_AGGREGATE_GROSS_INCOME"));
			obj.setR15_risk_weight_factor(rs.getBigDecimal("R15_RISK_WEIGHT_FACTOR"));
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_month(rs.getString("R16_MONTH"));
			obj.setR16_gross_income(rs.getBigDecimal("R16_GROSS_INCOME"));
			obj.setR16_aggregate_gross_income(rs.getBigDecimal("R16_AGGREGATE_GROSS_INCOME"));
			obj.setR16_risk_weight_factor(rs.getBigDecimal("R16_RISK_WEIGHT_FACTOR"));
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_month(rs.getString("R17_MONTH"));
			obj.setR17_gross_income(rs.getBigDecimal("R17_GROSS_INCOME"));
			obj.setR17_aggregate_gross_income(rs.getBigDecimal("R17_AGGREGATE_GROSS_INCOME"));
			obj.setR17_risk_weight_factor(rs.getBigDecimal("R17_RISK_WEIGHT_FACTOR"));
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_month(rs.getString("R18_MONTH"));
			obj.setR18_gross_income(rs.getBigDecimal("R18_GROSS_INCOME"));
			obj.setR18_aggregate_gross_income(rs.getBigDecimal("R18_AGGREGATE_GROSS_INCOME"));
			obj.setR18_risk_weight_factor(rs.getBigDecimal("R18_RISK_WEIGHT_FACTOR"));
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_month(rs.getString("R19_MONTH"));
			obj.setR19_gross_income(rs.getBigDecimal("R19_GROSS_INCOME"));
			obj.setR19_aggregate_gross_income(rs.getBigDecimal("R19_AGGREGATE_GROSS_INCOME"));
			obj.setR19_risk_weight_factor(rs.getBigDecimal("R19_RISK_WEIGHT_FACTOR"));
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_month(rs.getString("R20_MONTH"));
			obj.setR20_gross_income(rs.getBigDecimal("R20_GROSS_INCOME"));
			obj.setR20_aggregate_gross_income(rs.getBigDecimal("R20_AGGREGATE_GROSS_INCOME"));
			obj.setR20_risk_weight_factor(rs.getBigDecimal("R20_RISK_WEIGHT_FACTOR"));
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_month(rs.getString("R21_MONTH"));
			obj.setR21_gross_income(rs.getBigDecimal("R21_GROSS_INCOME"));
			obj.setR21_aggregate_gross_income(rs.getBigDecimal("R21_AGGREGATE_GROSS_INCOME"));
			obj.setR21_risk_weight_factor(rs.getBigDecimal("R21_RISK_WEIGHT_FACTOR"));
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_month(rs.getString("R22_MONTH"));
			obj.setR22_gross_income(rs.getBigDecimal("R22_GROSS_INCOME"));
			obj.setR22_aggregate_gross_income(rs.getBigDecimal("R22_AGGREGATE_GROSS_INCOME"));
			obj.setR22_risk_weight_factor(rs.getBigDecimal("R22_RISK_WEIGHT_FACTOR"));
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_month(rs.getString("R23_MONTH"));
			obj.setR23_gross_income(rs.getBigDecimal("R23_GROSS_INCOME"));
			obj.setR23_aggregate_gross_income(rs.getBigDecimal("R23_AGGREGATE_GROSS_INCOME"));
			obj.setR23_risk_weight_factor(rs.getBigDecimal("R23_RISK_WEIGHT_FACTOR"));
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_month(rs.getString("R24_MONTH"));
			obj.setR24_gross_income(rs.getBigDecimal("R24_GROSS_INCOME"));
			obj.setR24_aggregate_gross_income(rs.getBigDecimal("R24_AGGREGATE_GROSS_INCOME"));
			obj.setR24_risk_weight_factor(rs.getBigDecimal("R24_RISK_WEIGHT_FACTOR"));
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_month(rs.getString("R25_MONTH"));
			obj.setR25_gross_income(rs.getBigDecimal("R25_GROSS_INCOME"));
			obj.setR25_aggregate_gross_income(rs.getBigDecimal("R25_AGGREGATE_GROSS_INCOME"));
			obj.setR25_risk_weight_factor(rs.getBigDecimal("R25_RISK_WEIGHT_FACTOR"));
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_month(rs.getString("R26_MONTH"));
			obj.setR26_gross_income(rs.getBigDecimal("R26_GROSS_INCOME"));
			obj.setR26_aggregate_gross_income(rs.getBigDecimal("R26_AGGREGATE_GROSS_INCOME"));
			obj.setR26_risk_weight_factor(rs.getBigDecimal("R26_RISK_WEIGHT_FACTOR"));
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_month(rs.getString("R27_MONTH"));
			obj.setR27_gross_income(rs.getBigDecimal("R27_GROSS_INCOME"));
			obj.setR27_aggregate_gross_income(rs.getBigDecimal("R27_AGGREGATE_GROSS_INCOME"));
			obj.setR27_risk_weight_factor(rs.getBigDecimal("R27_RISK_WEIGHT_FACTOR"));
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_month(rs.getString("R28_MONTH"));
			obj.setR28_gross_income(rs.getBigDecimal("R28_GROSS_INCOME"));
			obj.setR28_aggregate_gross_income(rs.getBigDecimal("R28_AGGREGATE_GROSS_INCOME"));
			obj.setR28_risk_weight_factor(rs.getBigDecimal("R28_RISK_WEIGHT_FACTOR"));
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_month(rs.getString("R29_MONTH"));
			obj.setR29_gross_income(rs.getBigDecimal("R29_GROSS_INCOME"));
			obj.setR29_aggregate_gross_income(rs.getBigDecimal("R29_AGGREGATE_GROSS_INCOME"));
			obj.setR29_risk_weight_factor(rs.getBigDecimal("R29_RISK_WEIGHT_FACTOR"));
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_month(rs.getString("R30_MONTH"));
			obj.setR30_gross_income(rs.getBigDecimal("R30_GROSS_INCOME"));
			obj.setR30_aggregate_gross_income(rs.getBigDecimal("R30_AGGREGATE_GROSS_INCOME"));
			obj.setR30_risk_weight_factor(rs.getBigDecimal("R30_RISK_WEIGHT_FACTOR"));
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_month(rs.getString("R31_MONTH"));
			obj.setR31_gross_income(rs.getBigDecimal("R31_GROSS_INCOME"));
			obj.setR31_aggregate_gross_income(rs.getBigDecimal("R31_AGGREGATE_GROSS_INCOME"));
			obj.setR31_risk_weight_factor(rs.getBigDecimal("R31_RISK_WEIGHT_FACTOR"));
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_month(rs.getString("R32_MONTH"));
			obj.setR32_gross_income(rs.getBigDecimal("R32_GROSS_INCOME"));
			obj.setR32_aggregate_gross_income(rs.getBigDecimal("R32_AGGREGATE_GROSS_INCOME"));
			obj.setR32_risk_weight_factor(rs.getBigDecimal("R32_RISK_WEIGHT_FACTOR"));
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_month(rs.getString("R33_MONTH"));
			obj.setR33_gross_income(rs.getBigDecimal("R33_GROSS_INCOME"));
			obj.setR33_aggregate_gross_income(rs.getBigDecimal("R33_AGGREGATE_GROSS_INCOME"));
			obj.setR33_risk_weight_factor(rs.getBigDecimal("R33_RISK_WEIGHT_FACTOR"));
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_month(rs.getString("R34_MONTH"));
			obj.setR34_gross_income(rs.getBigDecimal("R34_GROSS_INCOME"));
			obj.setR34_aggregate_gross_income(rs.getBigDecimal("R34_AGGREGATE_GROSS_INCOME"));
			obj.setR34_risk_weight_factor(rs.getBigDecimal("R34_RISK_WEIGHT_FACTOR"));
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_month(rs.getString("R35_MONTH"));
			obj.setR35_gross_income(rs.getBigDecimal("R35_GROSS_INCOME"));
			obj.setR35_aggregate_gross_income(rs.getBigDecimal("R35_AGGREGATE_GROSS_INCOME"));
			obj.setR35_risk_weight_factor(rs.getBigDecimal("R35_RISK_WEIGHT_FACTOR"));
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_month(rs.getString("R36_MONTH"));
			obj.setR36_gross_income(rs.getBigDecimal("R36_GROSS_INCOME"));
			obj.setR36_aggregate_gross_income(rs.getBigDecimal("R36_AGGREGATE_GROSS_INCOME"));
			obj.setR36_risk_weight_factor(rs.getBigDecimal("R36_RISK_WEIGHT_FACTOR"));
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_month(rs.getString("R37_MONTH"));
			obj.setR37_gross_income(rs.getBigDecimal("R37_GROSS_INCOME"));
			obj.setR37_aggregate_gross_income(rs.getBigDecimal("R37_AGGREGATE_GROSS_INCOME"));
			obj.setR37_risk_weight_factor(rs.getBigDecimal("R37_RISK_WEIGHT_FACTOR"));
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_month(rs.getString("R38_MONTH"));
			obj.setR38_gross_income(rs.getBigDecimal("R38_GROSS_INCOME"));
			obj.setR38_aggregate_gross_income(rs.getBigDecimal("R38_AGGREGATE_GROSS_INCOME"));
			obj.setR38_risk_weight_factor(rs.getBigDecimal("R38_RISK_WEIGHT_FACTOR"));
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_month(rs.getString("R39_MONTH"));
			obj.setR39_gross_income(rs.getBigDecimal("R39_GROSS_INCOME"));
			obj.setR39_aggregate_gross_income(rs.getBigDecimal("R39_AGGREGATE_GROSS_INCOME"));
			obj.setR39_risk_weight_factor(rs.getBigDecimal("R39_RISK_WEIGHT_FACTOR"));
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_month(rs.getString("R40_MONTH"));
			obj.setR40_gross_income(rs.getBigDecimal("R40_GROSS_INCOME"));
			obj.setR40_aggregate_gross_income(rs.getBigDecimal("R40_AGGREGATE_GROSS_INCOME"));
			obj.setR40_risk_weight_factor(rs.getBigDecimal("R40_RISK_WEIGHT_FACTOR"));
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_month(rs.getString("R41_MONTH"));
			obj.setR41_gross_income(rs.getBigDecimal("R41_GROSS_INCOME"));
			obj.setR41_aggregate_gross_income(rs.getBigDecimal("R41_AGGREGATE_GROSS_INCOME"));
			obj.setR41_risk_weight_factor(rs.getBigDecimal("R41_RISK_WEIGHT_FACTOR"));
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_month(rs.getString("R42_MONTH"));
			obj.setR42_gross_income(rs.getBigDecimal("R42_GROSS_INCOME"));
			obj.setR42_aggregate_gross_income(rs.getBigDecimal("R42_AGGREGATE_GROSS_INCOME"));
			obj.setR42_risk_weight_factor(rs.getBigDecimal("R42_RISK_WEIGHT_FACTOR"));
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_month(rs.getString("R43_MONTH"));
			obj.setR43_gross_income(rs.getBigDecimal("R43_GROSS_INCOME"));
			obj.setR43_aggregate_gross_income(rs.getBigDecimal("R43_AGGREGATE_GROSS_INCOME"));
			obj.setR43_risk_weight_factor(rs.getBigDecimal("R43_RISK_WEIGHT_FACTOR"));
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_month(rs.getString("R44_MONTH"));
			obj.setR44_gross_income(rs.getBigDecimal("R44_GROSS_INCOME"));
			obj.setR44_aggregate_gross_income(rs.getBigDecimal("R44_AGGREGATE_GROSS_INCOME"));
			obj.setR44_risk_weight_factor(rs.getBigDecimal("R44_RISK_WEIGHT_FACTOR"));
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_month(rs.getString("R45_MONTH"));
			obj.setR45_gross_income(rs.getBigDecimal("R45_GROSS_INCOME"));
			obj.setR45_aggregate_gross_income(rs.getBigDecimal("R45_AGGREGATE_GROSS_INCOME"));
			obj.setR45_risk_weight_factor(rs.getBigDecimal("R45_RISK_WEIGHT_FACTOR"));
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_month(rs.getString("R46_MONTH"));
			obj.setR46_gross_income(rs.getBigDecimal("R46_GROSS_INCOME"));
			obj.setR46_aggregate_gross_income(rs.getBigDecimal("R46_AGGREGATE_GROSS_INCOME"));
			obj.setR46_risk_weight_factor(rs.getBigDecimal("R46_RISK_WEIGHT_FACTOR"));
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_month(rs.getString("R47_MONTH"));
			obj.setR47_gross_income(rs.getBigDecimal("R47_GROSS_INCOME"));
			obj.setR47_aggregate_gross_income(rs.getBigDecimal("R47_AGGREGATE_GROSS_INCOME"));
			obj.setR47_risk_weight_factor(rs.getBigDecimal("R47_RISK_WEIGHT_FACTOR"));
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_month(rs.getString("R48_MONTH"));
			obj.setR48_gross_income(rs.getBigDecimal("R48_GROSS_INCOME"));
			obj.setR48_aggregate_gross_income(rs.getBigDecimal("R48_AGGREGATE_GROSS_INCOME"));
			obj.setR48_risk_weight_factor(rs.getBigDecimal("R48_RISK_WEIGHT_FACTOR"));
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_month(rs.getString("R49_MONTH"));
			obj.setR49_gross_income(rs.getBigDecimal("R49_GROSS_INCOME"));
			obj.setR49_aggregate_gross_income(rs.getBigDecimal("R49_AGGREGATE_GROSS_INCOME"));
			obj.setR49_risk_weight_factor(rs.getBigDecimal("R49_RISK_WEIGHT_FACTOR"));
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_month(rs.getString("R50_MONTH"));
			obj.setR50_gross_income(rs.getBigDecimal("R50_GROSS_INCOME"));
			obj.setR50_aggregate_gross_income(rs.getBigDecimal("R50_AGGREGATE_GROSS_INCOME"));
			obj.setR50_risk_weight_factor(rs.getBigDecimal("R50_RISK_WEIGHT_FACTOR"));
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_month(rs.getString("R51_MONTH"));
			obj.setR51_gross_income(rs.getBigDecimal("R51_GROSS_INCOME"));
			obj.setR51_aggregate_gross_income(rs.getBigDecimal("R51_AGGREGATE_GROSS_INCOME"));
			obj.setR51_risk_weight_factor(rs.getBigDecimal("R51_RISK_WEIGHT_FACTOR"));
			obj.setR52_product(rs.getString("R52_PRODUCT"));
			obj.setR52_month(rs.getString("R52_MONTH"));
			obj.setR52_gross_income(rs.getBigDecimal("R52_GROSS_INCOME"));
			obj.setR52_aggregate_gross_income(rs.getBigDecimal("R52_AGGREGATE_GROSS_INCOME"));
			obj.setR52_risk_weight_factor(rs.getBigDecimal("R52_RISK_WEIGHT_FACTOR"));
			obj.setR53_product(rs.getString("R53_PRODUCT"));
			obj.setR53_month(rs.getString("R53_MONTH"));
			obj.setR53_gross_income(rs.getBigDecimal("R53_GROSS_INCOME"));
			obj.setR53_aggregate_gross_income(rs.getBigDecimal("R53_AGGREGATE_GROSS_INCOME"));
			obj.setR53_risk_weight_factor(rs.getBigDecimal("R53_RISK_WEIGHT_FACTOR"));
			obj.setR54_product(rs.getString("R54_PRODUCT"));
			obj.setR54_month(rs.getString("R54_MONTH"));
			obj.setR54_gross_income(rs.getBigDecimal("R54_GROSS_INCOME"));
			obj.setR54_aggregate_gross_income(rs.getBigDecimal("R54_AGGREGATE_GROSS_INCOME"));
			obj.setR54_risk_weight_factor(rs.getBigDecimal("R54_RISK_WEIGHT_FACTOR"));
			obj.setR55_product(rs.getString("R55_PRODUCT"));
			obj.setR55_month(rs.getString("R55_MONTH"));
			obj.setR55_gross_income(rs.getBigDecimal("R55_GROSS_INCOME"));
			obj.setR55_aggregate_gross_income(rs.getBigDecimal("R55_AGGREGATE_GROSS_INCOME"));
			obj.setR55_risk_weight_factor(rs.getBigDecimal("R55_RISK_WEIGHT_FACTOR"));
			obj.setR56_product(rs.getString("R56_PRODUCT"));
			obj.setR56_month(rs.getString("R56_MONTH"));
			obj.setR56_gross_income(rs.getBigDecimal("R56_GROSS_INCOME"));
			obj.setR56_aggregate_gross_income(rs.getBigDecimal("R56_AGGREGATE_GROSS_INCOME"));
			obj.setR56_risk_weight_factor(rs.getBigDecimal("R56_RISK_WEIGHT_FACTOR"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
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

	public class M_OR1_Resub_Detail_Entity {
		private String r10_product;
		private String r10_month;
		private BigDecimal r10_gross_income;
		private BigDecimal r10_aggregate_gross_income;
		private BigDecimal r10_risk_weight_factor;
		private String r11_product;
		private String r11_month;
		private BigDecimal r11_gross_income;
		private BigDecimal r11_aggregate_gross_income;
		private BigDecimal r11_risk_weight_factor;
		private String r12_product;
		private String r12_month;
		private BigDecimal r12_gross_income;
		private BigDecimal r12_aggregate_gross_income;
		private BigDecimal r12_risk_weight_factor;
		private String r13_product;
		private String r13_month;
		private BigDecimal r13_gross_income;
		private BigDecimal r13_aggregate_gross_income;
		private BigDecimal r13_risk_weight_factor;
		private String r14_product;
		private String r14_month;
		private BigDecimal r14_gross_income;
		private BigDecimal r14_aggregate_gross_income;
		private BigDecimal r14_risk_weight_factor;
		private String r15_product;
		private String r15_month;
		private BigDecimal r15_gross_income;
		private BigDecimal r15_aggregate_gross_income;
		private BigDecimal r15_risk_weight_factor;
		private String r16_product;
		private String r16_month;
		private BigDecimal r16_gross_income;
		private BigDecimal r16_aggregate_gross_income;
		private BigDecimal r16_risk_weight_factor;
		private String r17_product;
		private String r17_month;
		private BigDecimal r17_gross_income;
		private BigDecimal r17_aggregate_gross_income;
		private BigDecimal r17_risk_weight_factor;
		private String r18_product;
		private String r18_month;
		private BigDecimal r18_gross_income;
		private BigDecimal r18_aggregate_gross_income;
		private BigDecimal r18_risk_weight_factor;
		private String r19_product;
		private String r19_month;
		private BigDecimal r19_gross_income;
		private BigDecimal r19_aggregate_gross_income;
		private BigDecimal r19_risk_weight_factor;
		private String r20_product;
		private String r20_month;
		private BigDecimal r20_gross_income;
		private BigDecimal r20_aggregate_gross_income;
		private BigDecimal r20_risk_weight_factor;
		private String r21_product;
		private String r21_month;
		private BigDecimal r21_gross_income;
		private BigDecimal r21_aggregate_gross_income;
		private BigDecimal r21_risk_weight_factor;
		private String r22_product;
		private String r22_month;
		private BigDecimal r22_gross_income;
		private BigDecimal r22_aggregate_gross_income;
		private BigDecimal r22_risk_weight_factor;
		private String r23_product;
		private String r23_month;
		private BigDecimal r23_gross_income;
		private BigDecimal r23_aggregate_gross_income;
		private BigDecimal r23_risk_weight_factor;
		private String r24_product;
		private String r24_month;
		private BigDecimal r24_gross_income;
		private BigDecimal r24_aggregate_gross_income;
		private BigDecimal r24_risk_weight_factor;
		private String r25_product;
		private String r25_month;
		private BigDecimal r25_gross_income;
		private BigDecimal r25_aggregate_gross_income;
		private BigDecimal r25_risk_weight_factor;
		private String r26_product;
		private String r26_month;
		private BigDecimal r26_gross_income;
		private BigDecimal r26_aggregate_gross_income;
		private BigDecimal r26_risk_weight_factor;
		private String r27_product;
		private String r27_month;
		private BigDecimal r27_gross_income;
		private BigDecimal r27_aggregate_gross_income;
		private BigDecimal r27_risk_weight_factor;
		private String r28_product;
		private String r28_month;
		private BigDecimal r28_gross_income;
		private BigDecimal r28_aggregate_gross_income;
		private BigDecimal r28_risk_weight_factor;
		private String r29_product;
		private String r29_month;
		private BigDecimal r29_gross_income;
		private BigDecimal r29_aggregate_gross_income;
		private BigDecimal r29_risk_weight_factor;
		private String r30_product;
		private String r30_month;
		private BigDecimal r30_gross_income;
		private BigDecimal r30_aggregate_gross_income;
		private BigDecimal r30_risk_weight_factor;
		private String r31_product;
		private String r31_month;
		private BigDecimal r31_gross_income;
		private BigDecimal r31_aggregate_gross_income;
		private BigDecimal r31_risk_weight_factor;
		private String r32_product;
		private String r32_month;
		private BigDecimal r32_gross_income;
		private BigDecimal r32_aggregate_gross_income;
		private BigDecimal r32_risk_weight_factor;
		private String r33_product;
		private String r33_month;
		private BigDecimal r33_gross_income;
		private BigDecimal r33_aggregate_gross_income;
		private BigDecimal r33_risk_weight_factor;
		private String r34_product;
		private String r34_month;
		private BigDecimal r34_gross_income;
		private BigDecimal r34_aggregate_gross_income;
		private BigDecimal r34_risk_weight_factor;
		private String r35_product;
		private String r35_month;
		private BigDecimal r35_gross_income;
		private BigDecimal r35_aggregate_gross_income;
		private BigDecimal r35_risk_weight_factor;
		private String r36_product;
		private String r36_month;
		private BigDecimal r36_gross_income;
		private BigDecimal r36_aggregate_gross_income;
		private BigDecimal r36_risk_weight_factor;
		private String r37_product;
		private String r37_month;
		private BigDecimal r37_gross_income;
		private BigDecimal r37_aggregate_gross_income;
		private BigDecimal r37_risk_weight_factor;
		private String r38_product;
		private String r38_month;
		private BigDecimal r38_gross_income;
		private BigDecimal r38_aggregate_gross_income;
		private BigDecimal r38_risk_weight_factor;
		private String r39_product;
		private String r39_month;
		private BigDecimal r39_gross_income;
		private BigDecimal r39_aggregate_gross_income;
		private BigDecimal r39_risk_weight_factor;
		private String r40_product;
		private String r40_month;
		private BigDecimal r40_gross_income;
		private BigDecimal r40_aggregate_gross_income;
		private BigDecimal r40_risk_weight_factor;
		private String r41_product;
		private String r41_month;
		private BigDecimal r41_gross_income;
		private BigDecimal r41_aggregate_gross_income;
		private BigDecimal r41_risk_weight_factor;
		private String r42_product;
		private String r42_month;
		private BigDecimal r42_gross_income;
		private BigDecimal r42_aggregate_gross_income;
		private BigDecimal r42_risk_weight_factor;
		private String r43_product;
		private String r43_month;
		private BigDecimal r43_gross_income;
		private BigDecimal r43_aggregate_gross_income;
		private BigDecimal r43_risk_weight_factor;
		private String r44_product;
		private String r44_month;
		private BigDecimal r44_gross_income;
		private BigDecimal r44_aggregate_gross_income;
		private BigDecimal r44_risk_weight_factor;
		private String r45_product;
		private String r45_month;
		private BigDecimal r45_gross_income;
		private BigDecimal r45_aggregate_gross_income;
		private BigDecimal r45_risk_weight_factor;
		private String r46_product;
		private String r46_month;
		private BigDecimal r46_gross_income;
		private BigDecimal r46_aggregate_gross_income;
		private BigDecimal r46_risk_weight_factor;
		private String r47_product;
		private String r47_month;
		private BigDecimal r47_gross_income;
		private BigDecimal r47_aggregate_gross_income;
		private BigDecimal r47_risk_weight_factor;
		private String r48_product;
		private String r48_month;
		private BigDecimal r48_gross_income;
		private BigDecimal r48_aggregate_gross_income;
		private BigDecimal r48_risk_weight_factor;
		private String r49_product;
		private String r49_month;
		private BigDecimal r49_gross_income;
		private BigDecimal r49_aggregate_gross_income;
		private BigDecimal r49_risk_weight_factor;
		private String r50_product;
		private String r50_month;
		private BigDecimal r50_gross_income;
		private BigDecimal r50_aggregate_gross_income;
		private BigDecimal r50_risk_weight_factor;
		private String r51_product;
		private String r51_month;
		private BigDecimal r51_gross_income;
		private BigDecimal r51_aggregate_gross_income;
		private BigDecimal r51_risk_weight_factor;
		private String r52_product;
		private String r52_month;
		private BigDecimal r52_gross_income;
		private BigDecimal r52_aggregate_gross_income;
		private BigDecimal r52_risk_weight_factor;
		private String r53_product;
		private String r53_month;
		private BigDecimal r53_gross_income;
		private BigDecimal r53_aggregate_gross_income;
		private BigDecimal r53_risk_weight_factor;
		private String r54_product;
		private String r54_month;
		private BigDecimal r54_gross_income;
		private BigDecimal r54_aggregate_gross_income;
		private BigDecimal r54_risk_weight_factor;
		private String r55_product;
		private String r55_month;
		private BigDecimal r55_gross_income;
		private BigDecimal r55_aggregate_gross_income;
		private BigDecimal r55_risk_weight_factor;
		private String r56_product;
		private String r56_month;
		private BigDecimal r56_gross_income;
		private BigDecimal r56_aggregate_gross_income;
		private BigDecimal r56_risk_weight_factor;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public String getR10_month() {
			return r10_month;
		}

		public void setR10_month(String r10_month) {
			this.r10_month = r10_month;
		}

		public BigDecimal getR10_gross_income() {
			return r10_gross_income;
		}

		public void setR10_gross_income(BigDecimal r10_gross_income) {
			this.r10_gross_income = r10_gross_income;
		}

		public BigDecimal getR10_aggregate_gross_income() {
			return r10_aggregate_gross_income;
		}

		public void setR10_aggregate_gross_income(BigDecimal r10_aggregate_gross_income) {
			this.r10_aggregate_gross_income = r10_aggregate_gross_income;
		}

		public BigDecimal getR10_risk_weight_factor() {
			return r10_risk_weight_factor;
		}

		public void setR10_risk_weight_factor(BigDecimal r10_risk_weight_factor) {
			this.r10_risk_weight_factor = r10_risk_weight_factor;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public String getR11_month() {
			return r11_month;
		}

		public void setR11_month(String r11_month) {
			this.r11_month = r11_month;
		}

		public BigDecimal getR11_gross_income() {
			return r11_gross_income;
		}

		public void setR11_gross_income(BigDecimal r11_gross_income) {
			this.r11_gross_income = r11_gross_income;
		}

		public BigDecimal getR11_aggregate_gross_income() {
			return r11_aggregate_gross_income;
		}

		public void setR11_aggregate_gross_income(BigDecimal r11_aggregate_gross_income) {
			this.r11_aggregate_gross_income = r11_aggregate_gross_income;
		}

		public BigDecimal getR11_risk_weight_factor() {
			return r11_risk_weight_factor;
		}

		public void setR11_risk_weight_factor(BigDecimal r11_risk_weight_factor) {
			this.r11_risk_weight_factor = r11_risk_weight_factor;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public String getR12_month() {
			return r12_month;
		}

		public void setR12_month(String r12_month) {
			this.r12_month = r12_month;
		}

		public BigDecimal getR12_gross_income() {
			return r12_gross_income;
		}

		public void setR12_gross_income(BigDecimal r12_gross_income) {
			this.r12_gross_income = r12_gross_income;
		}

		public BigDecimal getR12_aggregate_gross_income() {
			return r12_aggregate_gross_income;
		}

		public void setR12_aggregate_gross_income(BigDecimal r12_aggregate_gross_income) {
			this.r12_aggregate_gross_income = r12_aggregate_gross_income;
		}

		public BigDecimal getR12_risk_weight_factor() {
			return r12_risk_weight_factor;
		}

		public void setR12_risk_weight_factor(BigDecimal r12_risk_weight_factor) {
			this.r12_risk_weight_factor = r12_risk_weight_factor;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public String getR13_month() {
			return r13_month;
		}

		public void setR13_month(String r13_month) {
			this.r13_month = r13_month;
		}

		public BigDecimal getR13_gross_income() {
			return r13_gross_income;
		}

		public void setR13_gross_income(BigDecimal r13_gross_income) {
			this.r13_gross_income = r13_gross_income;
		}

		public BigDecimal getR13_aggregate_gross_income() {
			return r13_aggregate_gross_income;
		}

		public void setR13_aggregate_gross_income(BigDecimal r13_aggregate_gross_income) {
			this.r13_aggregate_gross_income = r13_aggregate_gross_income;
		}

		public BigDecimal getR13_risk_weight_factor() {
			return r13_risk_weight_factor;
		}

		public void setR13_risk_weight_factor(BigDecimal r13_risk_weight_factor) {
			this.r13_risk_weight_factor = r13_risk_weight_factor;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public String getR14_month() {
			return r14_month;
		}

		public void setR14_month(String r14_month) {
			this.r14_month = r14_month;
		}

		public BigDecimal getR14_gross_income() {
			return r14_gross_income;
		}

		public void setR14_gross_income(BigDecimal r14_gross_income) {
			this.r14_gross_income = r14_gross_income;
		}

		public BigDecimal getR14_aggregate_gross_income() {
			return r14_aggregate_gross_income;
		}

		public void setR14_aggregate_gross_income(BigDecimal r14_aggregate_gross_income) {
			this.r14_aggregate_gross_income = r14_aggregate_gross_income;
		}

		public BigDecimal getR14_risk_weight_factor() {
			return r14_risk_weight_factor;
		}

		public void setR14_risk_weight_factor(BigDecimal r14_risk_weight_factor) {
			this.r14_risk_weight_factor = r14_risk_weight_factor;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public String getR15_month() {
			return r15_month;
		}

		public void setR15_month(String r15_month) {
			this.r15_month = r15_month;
		}

		public BigDecimal getR15_gross_income() {
			return r15_gross_income;
		}

		public void setR15_gross_income(BigDecimal r15_gross_income) {
			this.r15_gross_income = r15_gross_income;
		}

		public BigDecimal getR15_aggregate_gross_income() {
			return r15_aggregate_gross_income;
		}

		public void setR15_aggregate_gross_income(BigDecimal r15_aggregate_gross_income) {
			this.r15_aggregate_gross_income = r15_aggregate_gross_income;
		}

		public BigDecimal getR15_risk_weight_factor() {
			return r15_risk_weight_factor;
		}

		public void setR15_risk_weight_factor(BigDecimal r15_risk_weight_factor) {
			this.r15_risk_weight_factor = r15_risk_weight_factor;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public String getR16_month() {
			return r16_month;
		}

		public void setR16_month(String r16_month) {
			this.r16_month = r16_month;
		}

		public BigDecimal getR16_gross_income() {
			return r16_gross_income;
		}

		public void setR16_gross_income(BigDecimal r16_gross_income) {
			this.r16_gross_income = r16_gross_income;
		}

		public BigDecimal getR16_aggregate_gross_income() {
			return r16_aggregate_gross_income;
		}

		public void setR16_aggregate_gross_income(BigDecimal r16_aggregate_gross_income) {
			this.r16_aggregate_gross_income = r16_aggregate_gross_income;
		}

		public BigDecimal getR16_risk_weight_factor() {
			return r16_risk_weight_factor;
		}

		public void setR16_risk_weight_factor(BigDecimal r16_risk_weight_factor) {
			this.r16_risk_weight_factor = r16_risk_weight_factor;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public String getR17_month() {
			return r17_month;
		}

		public void setR17_month(String r17_month) {
			this.r17_month = r17_month;
		}

		public BigDecimal getR17_gross_income() {
			return r17_gross_income;
		}

		public void setR17_gross_income(BigDecimal r17_gross_income) {
			this.r17_gross_income = r17_gross_income;
		}

		public BigDecimal getR17_aggregate_gross_income() {
			return r17_aggregate_gross_income;
		}

		public void setR17_aggregate_gross_income(BigDecimal r17_aggregate_gross_income) {
			this.r17_aggregate_gross_income = r17_aggregate_gross_income;
		}

		public BigDecimal getR17_risk_weight_factor() {
			return r17_risk_weight_factor;
		}

		public void setR17_risk_weight_factor(BigDecimal r17_risk_weight_factor) {
			this.r17_risk_weight_factor = r17_risk_weight_factor;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public String getR18_month() {
			return r18_month;
		}

		public void setR18_month(String r18_month) {
			this.r18_month = r18_month;
		}

		public BigDecimal getR18_gross_income() {
			return r18_gross_income;
		}

		public void setR18_gross_income(BigDecimal r18_gross_income) {
			this.r18_gross_income = r18_gross_income;
		}

		public BigDecimal getR18_aggregate_gross_income() {
			return r18_aggregate_gross_income;
		}

		public void setR18_aggregate_gross_income(BigDecimal r18_aggregate_gross_income) {
			this.r18_aggregate_gross_income = r18_aggregate_gross_income;
		}

		public BigDecimal getR18_risk_weight_factor() {
			return r18_risk_weight_factor;
		}

		public void setR18_risk_weight_factor(BigDecimal r18_risk_weight_factor) {
			this.r18_risk_weight_factor = r18_risk_weight_factor;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public String getR19_month() {
			return r19_month;
		}

		public void setR19_month(String r19_month) {
			this.r19_month = r19_month;
		}

		public BigDecimal getR19_gross_income() {
			return r19_gross_income;
		}

		public void setR19_gross_income(BigDecimal r19_gross_income) {
			this.r19_gross_income = r19_gross_income;
		}

		public BigDecimal getR19_aggregate_gross_income() {
			return r19_aggregate_gross_income;
		}

		public void setR19_aggregate_gross_income(BigDecimal r19_aggregate_gross_income) {
			this.r19_aggregate_gross_income = r19_aggregate_gross_income;
		}

		public BigDecimal getR19_risk_weight_factor() {
			return r19_risk_weight_factor;
		}

		public void setR19_risk_weight_factor(BigDecimal r19_risk_weight_factor) {
			this.r19_risk_weight_factor = r19_risk_weight_factor;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public String getR20_month() {
			return r20_month;
		}

		public void setR20_month(String r20_month) {
			this.r20_month = r20_month;
		}

		public BigDecimal getR20_gross_income() {
			return r20_gross_income;
		}

		public void setR20_gross_income(BigDecimal r20_gross_income) {
			this.r20_gross_income = r20_gross_income;
		}

		public BigDecimal getR20_aggregate_gross_income() {
			return r20_aggregate_gross_income;
		}

		public void setR20_aggregate_gross_income(BigDecimal r20_aggregate_gross_income) {
			this.r20_aggregate_gross_income = r20_aggregate_gross_income;
		}

		public BigDecimal getR20_risk_weight_factor() {
			return r20_risk_weight_factor;
		}

		public void setR20_risk_weight_factor(BigDecimal r20_risk_weight_factor) {
			this.r20_risk_weight_factor = r20_risk_weight_factor;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public String getR21_month() {
			return r21_month;
		}

		public void setR21_month(String r21_month) {
			this.r21_month = r21_month;
		}

		public BigDecimal getR21_gross_income() {
			return r21_gross_income;
		}

		public void setR21_gross_income(BigDecimal r21_gross_income) {
			this.r21_gross_income = r21_gross_income;
		}

		public BigDecimal getR21_aggregate_gross_income() {
			return r21_aggregate_gross_income;
		}

		public void setR21_aggregate_gross_income(BigDecimal r21_aggregate_gross_income) {
			this.r21_aggregate_gross_income = r21_aggregate_gross_income;
		}

		public BigDecimal getR21_risk_weight_factor() {
			return r21_risk_weight_factor;
		}

		public void setR21_risk_weight_factor(BigDecimal r21_risk_weight_factor) {
			this.r21_risk_weight_factor = r21_risk_weight_factor;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public String getR22_month() {
			return r22_month;
		}

		public void setR22_month(String r22_month) {
			this.r22_month = r22_month;
		}

		public BigDecimal getR22_gross_income() {
			return r22_gross_income;
		}

		public void setR22_gross_income(BigDecimal r22_gross_income) {
			this.r22_gross_income = r22_gross_income;
		}

		public BigDecimal getR22_aggregate_gross_income() {
			return r22_aggregate_gross_income;
		}

		public void setR22_aggregate_gross_income(BigDecimal r22_aggregate_gross_income) {
			this.r22_aggregate_gross_income = r22_aggregate_gross_income;
		}

		public BigDecimal getR22_risk_weight_factor() {
			return r22_risk_weight_factor;
		}

		public void setR22_risk_weight_factor(BigDecimal r22_risk_weight_factor) {
			this.r22_risk_weight_factor = r22_risk_weight_factor;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public String getR23_month() {
			return r23_month;
		}

		public void setR23_month(String r23_month) {
			this.r23_month = r23_month;
		}

		public BigDecimal getR23_gross_income() {
			return r23_gross_income;
		}

		public void setR23_gross_income(BigDecimal r23_gross_income) {
			this.r23_gross_income = r23_gross_income;
		}

		public BigDecimal getR23_aggregate_gross_income() {
			return r23_aggregate_gross_income;
		}

		public void setR23_aggregate_gross_income(BigDecimal r23_aggregate_gross_income) {
			this.r23_aggregate_gross_income = r23_aggregate_gross_income;
		}

		public BigDecimal getR23_risk_weight_factor() {
			return r23_risk_weight_factor;
		}

		public void setR23_risk_weight_factor(BigDecimal r23_risk_weight_factor) {
			this.r23_risk_weight_factor = r23_risk_weight_factor;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public String getR24_month() {
			return r24_month;
		}

		public void setR24_month(String r24_month) {
			this.r24_month = r24_month;
		}

		public BigDecimal getR24_gross_income() {
			return r24_gross_income;
		}

		public void setR24_gross_income(BigDecimal r24_gross_income) {
			this.r24_gross_income = r24_gross_income;
		}

		public BigDecimal getR24_aggregate_gross_income() {
			return r24_aggregate_gross_income;
		}

		public void setR24_aggregate_gross_income(BigDecimal r24_aggregate_gross_income) {
			this.r24_aggregate_gross_income = r24_aggregate_gross_income;
		}

		public BigDecimal getR24_risk_weight_factor() {
			return r24_risk_weight_factor;
		}

		public void setR24_risk_weight_factor(BigDecimal r24_risk_weight_factor) {
			this.r24_risk_weight_factor = r24_risk_weight_factor;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public String getR25_month() {
			return r25_month;
		}

		public void setR25_month(String r25_month) {
			this.r25_month = r25_month;
		}

		public BigDecimal getR25_gross_income() {
			return r25_gross_income;
		}

		public void setR25_gross_income(BigDecimal r25_gross_income) {
			this.r25_gross_income = r25_gross_income;
		}

		public BigDecimal getR25_aggregate_gross_income() {
			return r25_aggregate_gross_income;
		}

		public void setR25_aggregate_gross_income(BigDecimal r25_aggregate_gross_income) {
			this.r25_aggregate_gross_income = r25_aggregate_gross_income;
		}

		public BigDecimal getR25_risk_weight_factor() {
			return r25_risk_weight_factor;
		}

		public void setR25_risk_weight_factor(BigDecimal r25_risk_weight_factor) {
			this.r25_risk_weight_factor = r25_risk_weight_factor;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public String getR26_month() {
			return r26_month;
		}

		public void setR26_month(String r26_month) {
			this.r26_month = r26_month;
		}

		public BigDecimal getR26_gross_income() {
			return r26_gross_income;
		}

		public void setR26_gross_income(BigDecimal r26_gross_income) {
			this.r26_gross_income = r26_gross_income;
		}

		public BigDecimal getR26_aggregate_gross_income() {
			return r26_aggregate_gross_income;
		}

		public void setR26_aggregate_gross_income(BigDecimal r26_aggregate_gross_income) {
			this.r26_aggregate_gross_income = r26_aggregate_gross_income;
		}

		public BigDecimal getR26_risk_weight_factor() {
			return r26_risk_weight_factor;
		}

		public void setR26_risk_weight_factor(BigDecimal r26_risk_weight_factor) {
			this.r26_risk_weight_factor = r26_risk_weight_factor;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public String getR27_month() {
			return r27_month;
		}

		public void setR27_month(String r27_month) {
			this.r27_month = r27_month;
		}

		public BigDecimal getR27_gross_income() {
			return r27_gross_income;
		}

		public void setR27_gross_income(BigDecimal r27_gross_income) {
			this.r27_gross_income = r27_gross_income;
		}

		public BigDecimal getR27_aggregate_gross_income() {
			return r27_aggregate_gross_income;
		}

		public void setR27_aggregate_gross_income(BigDecimal r27_aggregate_gross_income) {
			this.r27_aggregate_gross_income = r27_aggregate_gross_income;
		}

		public BigDecimal getR27_risk_weight_factor() {
			return r27_risk_weight_factor;
		}

		public void setR27_risk_weight_factor(BigDecimal r27_risk_weight_factor) {
			this.r27_risk_weight_factor = r27_risk_weight_factor;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public String getR28_month() {
			return r28_month;
		}

		public void setR28_month(String r28_month) {
			this.r28_month = r28_month;
		}

		public BigDecimal getR28_gross_income() {
			return r28_gross_income;
		}

		public void setR28_gross_income(BigDecimal r28_gross_income) {
			this.r28_gross_income = r28_gross_income;
		}

		public BigDecimal getR28_aggregate_gross_income() {
			return r28_aggregate_gross_income;
		}

		public void setR28_aggregate_gross_income(BigDecimal r28_aggregate_gross_income) {
			this.r28_aggregate_gross_income = r28_aggregate_gross_income;
		}

		public BigDecimal getR28_risk_weight_factor() {
			return r28_risk_weight_factor;
		}

		public void setR28_risk_weight_factor(BigDecimal r28_risk_weight_factor) {
			this.r28_risk_weight_factor = r28_risk_weight_factor;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public String getR29_month() {
			return r29_month;
		}

		public void setR29_month(String r29_month) {
			this.r29_month = r29_month;
		}

		public BigDecimal getR29_gross_income() {
			return r29_gross_income;
		}

		public void setR29_gross_income(BigDecimal r29_gross_income) {
			this.r29_gross_income = r29_gross_income;
		}

		public BigDecimal getR29_aggregate_gross_income() {
			return r29_aggregate_gross_income;
		}

		public void setR29_aggregate_gross_income(BigDecimal r29_aggregate_gross_income) {
			this.r29_aggregate_gross_income = r29_aggregate_gross_income;
		}

		public BigDecimal getR29_risk_weight_factor() {
			return r29_risk_weight_factor;
		}

		public void setR29_risk_weight_factor(BigDecimal r29_risk_weight_factor) {
			this.r29_risk_weight_factor = r29_risk_weight_factor;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public String getR30_month() {
			return r30_month;
		}

		public void setR30_month(String r30_month) {
			this.r30_month = r30_month;
		}

		public BigDecimal getR30_gross_income() {
			return r30_gross_income;
		}

		public void setR30_gross_income(BigDecimal r30_gross_income) {
			this.r30_gross_income = r30_gross_income;
		}

		public BigDecimal getR30_aggregate_gross_income() {
			return r30_aggregate_gross_income;
		}

		public void setR30_aggregate_gross_income(BigDecimal r30_aggregate_gross_income) {
			this.r30_aggregate_gross_income = r30_aggregate_gross_income;
		}

		public BigDecimal getR30_risk_weight_factor() {
			return r30_risk_weight_factor;
		}

		public void setR30_risk_weight_factor(BigDecimal r30_risk_weight_factor) {
			this.r30_risk_weight_factor = r30_risk_weight_factor;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public String getR31_month() {
			return r31_month;
		}

		public void setR31_month(String r31_month) {
			this.r31_month = r31_month;
		}

		public BigDecimal getR31_gross_income() {
			return r31_gross_income;
		}

		public void setR31_gross_income(BigDecimal r31_gross_income) {
			this.r31_gross_income = r31_gross_income;
		}

		public BigDecimal getR31_aggregate_gross_income() {
			return r31_aggregate_gross_income;
		}

		public void setR31_aggregate_gross_income(BigDecimal r31_aggregate_gross_income) {
			this.r31_aggregate_gross_income = r31_aggregate_gross_income;
		}

		public BigDecimal getR31_risk_weight_factor() {
			return r31_risk_weight_factor;
		}

		public void setR31_risk_weight_factor(BigDecimal r31_risk_weight_factor) {
			this.r31_risk_weight_factor = r31_risk_weight_factor;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public String getR32_month() {
			return r32_month;
		}

		public void setR32_month(String r32_month) {
			this.r32_month = r32_month;
		}

		public BigDecimal getR32_gross_income() {
			return r32_gross_income;
		}

		public void setR32_gross_income(BigDecimal r32_gross_income) {
			this.r32_gross_income = r32_gross_income;
		}

		public BigDecimal getR32_aggregate_gross_income() {
			return r32_aggregate_gross_income;
		}

		public void setR32_aggregate_gross_income(BigDecimal r32_aggregate_gross_income) {
			this.r32_aggregate_gross_income = r32_aggregate_gross_income;
		}

		public BigDecimal getR32_risk_weight_factor() {
			return r32_risk_weight_factor;
		}

		public void setR32_risk_weight_factor(BigDecimal r32_risk_weight_factor) {
			this.r32_risk_weight_factor = r32_risk_weight_factor;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public String getR33_month() {
			return r33_month;
		}

		public void setR33_month(String r33_month) {
			this.r33_month = r33_month;
		}

		public BigDecimal getR33_gross_income() {
			return r33_gross_income;
		}

		public void setR33_gross_income(BigDecimal r33_gross_income) {
			this.r33_gross_income = r33_gross_income;
		}

		public BigDecimal getR33_aggregate_gross_income() {
			return r33_aggregate_gross_income;
		}

		public void setR33_aggregate_gross_income(BigDecimal r33_aggregate_gross_income) {
			this.r33_aggregate_gross_income = r33_aggregate_gross_income;
		}

		public BigDecimal getR33_risk_weight_factor() {
			return r33_risk_weight_factor;
		}

		public void setR33_risk_weight_factor(BigDecimal r33_risk_weight_factor) {
			this.r33_risk_weight_factor = r33_risk_weight_factor;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public String getR34_month() {
			return r34_month;
		}

		public void setR34_month(String r34_month) {
			this.r34_month = r34_month;
		}

		public BigDecimal getR34_gross_income() {
			return r34_gross_income;
		}

		public void setR34_gross_income(BigDecimal r34_gross_income) {
			this.r34_gross_income = r34_gross_income;
		}

		public BigDecimal getR34_aggregate_gross_income() {
			return r34_aggregate_gross_income;
		}

		public void setR34_aggregate_gross_income(BigDecimal r34_aggregate_gross_income) {
			this.r34_aggregate_gross_income = r34_aggregate_gross_income;
		}

		public BigDecimal getR34_risk_weight_factor() {
			return r34_risk_weight_factor;
		}

		public void setR34_risk_weight_factor(BigDecimal r34_risk_weight_factor) {
			this.r34_risk_weight_factor = r34_risk_weight_factor;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public String getR35_month() {
			return r35_month;
		}

		public void setR35_month(String r35_month) {
			this.r35_month = r35_month;
		}

		public BigDecimal getR35_gross_income() {
			return r35_gross_income;
		}

		public void setR35_gross_income(BigDecimal r35_gross_income) {
			this.r35_gross_income = r35_gross_income;
		}

		public BigDecimal getR35_aggregate_gross_income() {
			return r35_aggregate_gross_income;
		}

		public void setR35_aggregate_gross_income(BigDecimal r35_aggregate_gross_income) {
			this.r35_aggregate_gross_income = r35_aggregate_gross_income;
		}

		public BigDecimal getR35_risk_weight_factor() {
			return r35_risk_weight_factor;
		}

		public void setR35_risk_weight_factor(BigDecimal r35_risk_weight_factor) {
			this.r35_risk_weight_factor = r35_risk_weight_factor;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public String getR36_month() {
			return r36_month;
		}

		public void setR36_month(String r36_month) {
			this.r36_month = r36_month;
		}

		public BigDecimal getR36_gross_income() {
			return r36_gross_income;
		}

		public void setR36_gross_income(BigDecimal r36_gross_income) {
			this.r36_gross_income = r36_gross_income;
		}

		public BigDecimal getR36_aggregate_gross_income() {
			return r36_aggregate_gross_income;
		}

		public void setR36_aggregate_gross_income(BigDecimal r36_aggregate_gross_income) {
			this.r36_aggregate_gross_income = r36_aggregate_gross_income;
		}

		public BigDecimal getR36_risk_weight_factor() {
			return r36_risk_weight_factor;
		}

		public void setR36_risk_weight_factor(BigDecimal r36_risk_weight_factor) {
			this.r36_risk_weight_factor = r36_risk_weight_factor;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public String getR37_month() {
			return r37_month;
		}

		public void setR37_month(String r37_month) {
			this.r37_month = r37_month;
		}

		public BigDecimal getR37_gross_income() {
			return r37_gross_income;
		}

		public void setR37_gross_income(BigDecimal r37_gross_income) {
			this.r37_gross_income = r37_gross_income;
		}

		public BigDecimal getR37_aggregate_gross_income() {
			return r37_aggregate_gross_income;
		}

		public void setR37_aggregate_gross_income(BigDecimal r37_aggregate_gross_income) {
			this.r37_aggregate_gross_income = r37_aggregate_gross_income;
		}

		public BigDecimal getR37_risk_weight_factor() {
			return r37_risk_weight_factor;
		}

		public void setR37_risk_weight_factor(BigDecimal r37_risk_weight_factor) {
			this.r37_risk_weight_factor = r37_risk_weight_factor;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public String getR38_month() {
			return r38_month;
		}

		public void setR38_month(String r38_month) {
			this.r38_month = r38_month;
		}

		public BigDecimal getR38_gross_income() {
			return r38_gross_income;
		}

		public void setR38_gross_income(BigDecimal r38_gross_income) {
			this.r38_gross_income = r38_gross_income;
		}

		public BigDecimal getR38_aggregate_gross_income() {
			return r38_aggregate_gross_income;
		}

		public void setR38_aggregate_gross_income(BigDecimal r38_aggregate_gross_income) {
			this.r38_aggregate_gross_income = r38_aggregate_gross_income;
		}

		public BigDecimal getR38_risk_weight_factor() {
			return r38_risk_weight_factor;
		}

		public void setR38_risk_weight_factor(BigDecimal r38_risk_weight_factor) {
			this.r38_risk_weight_factor = r38_risk_weight_factor;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public String getR39_month() {
			return r39_month;
		}

		public void setR39_month(String r39_month) {
			this.r39_month = r39_month;
		}

		public BigDecimal getR39_gross_income() {
			return r39_gross_income;
		}

		public void setR39_gross_income(BigDecimal r39_gross_income) {
			this.r39_gross_income = r39_gross_income;
		}

		public BigDecimal getR39_aggregate_gross_income() {
			return r39_aggregate_gross_income;
		}

		public void setR39_aggregate_gross_income(BigDecimal r39_aggregate_gross_income) {
			this.r39_aggregate_gross_income = r39_aggregate_gross_income;
		}

		public BigDecimal getR39_risk_weight_factor() {
			return r39_risk_weight_factor;
		}

		public void setR39_risk_weight_factor(BigDecimal r39_risk_weight_factor) {
			this.r39_risk_weight_factor = r39_risk_weight_factor;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public String getR40_month() {
			return r40_month;
		}

		public void setR40_month(String r40_month) {
			this.r40_month = r40_month;
		}

		public BigDecimal getR40_gross_income() {
			return r40_gross_income;
		}

		public void setR40_gross_income(BigDecimal r40_gross_income) {
			this.r40_gross_income = r40_gross_income;
		}

		public BigDecimal getR40_aggregate_gross_income() {
			return r40_aggregate_gross_income;
		}

		public void setR40_aggregate_gross_income(BigDecimal r40_aggregate_gross_income) {
			this.r40_aggregate_gross_income = r40_aggregate_gross_income;
		}

		public BigDecimal getR40_risk_weight_factor() {
			return r40_risk_weight_factor;
		}

		public void setR40_risk_weight_factor(BigDecimal r40_risk_weight_factor) {
			this.r40_risk_weight_factor = r40_risk_weight_factor;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public String getR41_month() {
			return r41_month;
		}

		public void setR41_month(String r41_month) {
			this.r41_month = r41_month;
		}

		public BigDecimal getR41_gross_income() {
			return r41_gross_income;
		}

		public void setR41_gross_income(BigDecimal r41_gross_income) {
			this.r41_gross_income = r41_gross_income;
		}

		public BigDecimal getR41_aggregate_gross_income() {
			return r41_aggregate_gross_income;
		}

		public void setR41_aggregate_gross_income(BigDecimal r41_aggregate_gross_income) {
			this.r41_aggregate_gross_income = r41_aggregate_gross_income;
		}

		public BigDecimal getR41_risk_weight_factor() {
			return r41_risk_weight_factor;
		}

		public void setR41_risk_weight_factor(BigDecimal r41_risk_weight_factor) {
			this.r41_risk_weight_factor = r41_risk_weight_factor;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public String getR42_month() {
			return r42_month;
		}

		public void setR42_month(String r42_month) {
			this.r42_month = r42_month;
		}

		public BigDecimal getR42_gross_income() {
			return r42_gross_income;
		}

		public void setR42_gross_income(BigDecimal r42_gross_income) {
			this.r42_gross_income = r42_gross_income;
		}

		public BigDecimal getR42_aggregate_gross_income() {
			return r42_aggregate_gross_income;
		}

		public void setR42_aggregate_gross_income(BigDecimal r42_aggregate_gross_income) {
			this.r42_aggregate_gross_income = r42_aggregate_gross_income;
		}

		public BigDecimal getR42_risk_weight_factor() {
			return r42_risk_weight_factor;
		}

		public void setR42_risk_weight_factor(BigDecimal r42_risk_weight_factor) {
			this.r42_risk_weight_factor = r42_risk_weight_factor;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public String getR43_month() {
			return r43_month;
		}

		public void setR43_month(String r43_month) {
			this.r43_month = r43_month;
		}

		public BigDecimal getR43_gross_income() {
			return r43_gross_income;
		}

		public void setR43_gross_income(BigDecimal r43_gross_income) {
			this.r43_gross_income = r43_gross_income;
		}

		public BigDecimal getR43_aggregate_gross_income() {
			return r43_aggregate_gross_income;
		}

		public void setR43_aggregate_gross_income(BigDecimal r43_aggregate_gross_income) {
			this.r43_aggregate_gross_income = r43_aggregate_gross_income;
		}

		public BigDecimal getR43_risk_weight_factor() {
			return r43_risk_weight_factor;
		}

		public void setR43_risk_weight_factor(BigDecimal r43_risk_weight_factor) {
			this.r43_risk_weight_factor = r43_risk_weight_factor;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public String getR44_month() {
			return r44_month;
		}

		public void setR44_month(String r44_month) {
			this.r44_month = r44_month;
		}

		public BigDecimal getR44_gross_income() {
			return r44_gross_income;
		}

		public void setR44_gross_income(BigDecimal r44_gross_income) {
			this.r44_gross_income = r44_gross_income;
		}

		public BigDecimal getR44_aggregate_gross_income() {
			return r44_aggregate_gross_income;
		}

		public void setR44_aggregate_gross_income(BigDecimal r44_aggregate_gross_income) {
			this.r44_aggregate_gross_income = r44_aggregate_gross_income;
		}

		public BigDecimal getR44_risk_weight_factor() {
			return r44_risk_weight_factor;
		}

		public void setR44_risk_weight_factor(BigDecimal r44_risk_weight_factor) {
			this.r44_risk_weight_factor = r44_risk_weight_factor;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public String getR45_month() {
			return r45_month;
		}

		public void setR45_month(String r45_month) {
			this.r45_month = r45_month;
		}

		public BigDecimal getR45_gross_income() {
			return r45_gross_income;
		}

		public void setR45_gross_income(BigDecimal r45_gross_income) {
			this.r45_gross_income = r45_gross_income;
		}

		public BigDecimal getR45_aggregate_gross_income() {
			return r45_aggregate_gross_income;
		}

		public void setR45_aggregate_gross_income(BigDecimal r45_aggregate_gross_income) {
			this.r45_aggregate_gross_income = r45_aggregate_gross_income;
		}

		public BigDecimal getR45_risk_weight_factor() {
			return r45_risk_weight_factor;
		}

		public void setR45_risk_weight_factor(BigDecimal r45_risk_weight_factor) {
			this.r45_risk_weight_factor = r45_risk_weight_factor;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public String getR46_month() {
			return r46_month;
		}

		public void setR46_month(String r46_month) {
			this.r46_month = r46_month;
		}

		public BigDecimal getR46_gross_income() {
			return r46_gross_income;
		}

		public void setR46_gross_income(BigDecimal r46_gross_income) {
			this.r46_gross_income = r46_gross_income;
		}

		public BigDecimal getR46_aggregate_gross_income() {
			return r46_aggregate_gross_income;
		}

		public void setR46_aggregate_gross_income(BigDecimal r46_aggregate_gross_income) {
			this.r46_aggregate_gross_income = r46_aggregate_gross_income;
		}

		public BigDecimal getR46_risk_weight_factor() {
			return r46_risk_weight_factor;
		}

		public void setR46_risk_weight_factor(BigDecimal r46_risk_weight_factor) {
			this.r46_risk_weight_factor = r46_risk_weight_factor;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public String getR47_month() {
			return r47_month;
		}

		public void setR47_month(String r47_month) {
			this.r47_month = r47_month;
		}

		public BigDecimal getR47_gross_income() {
			return r47_gross_income;
		}

		public void setR47_gross_income(BigDecimal r47_gross_income) {
			this.r47_gross_income = r47_gross_income;
		}

		public BigDecimal getR47_aggregate_gross_income() {
			return r47_aggregate_gross_income;
		}

		public void setR47_aggregate_gross_income(BigDecimal r47_aggregate_gross_income) {
			this.r47_aggregate_gross_income = r47_aggregate_gross_income;
		}

		public BigDecimal getR47_risk_weight_factor() {
			return r47_risk_weight_factor;
		}

		public void setR47_risk_weight_factor(BigDecimal r47_risk_weight_factor) {
			this.r47_risk_weight_factor = r47_risk_weight_factor;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public String getR48_month() {
			return r48_month;
		}

		public void setR48_month(String r48_month) {
			this.r48_month = r48_month;
		}

		public BigDecimal getR48_gross_income() {
			return r48_gross_income;
		}

		public void setR48_gross_income(BigDecimal r48_gross_income) {
			this.r48_gross_income = r48_gross_income;
		}

		public BigDecimal getR48_aggregate_gross_income() {
			return r48_aggregate_gross_income;
		}

		public void setR48_aggregate_gross_income(BigDecimal r48_aggregate_gross_income) {
			this.r48_aggregate_gross_income = r48_aggregate_gross_income;
		}

		public BigDecimal getR48_risk_weight_factor() {
			return r48_risk_weight_factor;
		}

		public void setR48_risk_weight_factor(BigDecimal r48_risk_weight_factor) {
			this.r48_risk_weight_factor = r48_risk_weight_factor;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public String getR49_month() {
			return r49_month;
		}

		public void setR49_month(String r49_month) {
			this.r49_month = r49_month;
		}

		public BigDecimal getR49_gross_income() {
			return r49_gross_income;
		}

		public void setR49_gross_income(BigDecimal r49_gross_income) {
			this.r49_gross_income = r49_gross_income;
		}

		public BigDecimal getR49_aggregate_gross_income() {
			return r49_aggregate_gross_income;
		}

		public void setR49_aggregate_gross_income(BigDecimal r49_aggregate_gross_income) {
			this.r49_aggregate_gross_income = r49_aggregate_gross_income;
		}

		public BigDecimal getR49_risk_weight_factor() {
			return r49_risk_weight_factor;
		}

		public void setR49_risk_weight_factor(BigDecimal r49_risk_weight_factor) {
			this.r49_risk_weight_factor = r49_risk_weight_factor;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public String getR50_month() {
			return r50_month;
		}

		public void setR50_month(String r50_month) {
			this.r50_month = r50_month;
		}

		public BigDecimal getR50_gross_income() {
			return r50_gross_income;
		}

		public void setR50_gross_income(BigDecimal r50_gross_income) {
			this.r50_gross_income = r50_gross_income;
		}

		public BigDecimal getR50_aggregate_gross_income() {
			return r50_aggregate_gross_income;
		}

		public void setR50_aggregate_gross_income(BigDecimal r50_aggregate_gross_income) {
			this.r50_aggregate_gross_income = r50_aggregate_gross_income;
		}

		public BigDecimal getR50_risk_weight_factor() {
			return r50_risk_weight_factor;
		}

		public void setR50_risk_weight_factor(BigDecimal r50_risk_weight_factor) {
			this.r50_risk_weight_factor = r50_risk_weight_factor;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public String getR51_month() {
			return r51_month;
		}

		public void setR51_month(String r51_month) {
			this.r51_month = r51_month;
		}

		public BigDecimal getR51_gross_income() {
			return r51_gross_income;
		}

		public void setR51_gross_income(BigDecimal r51_gross_income) {
			this.r51_gross_income = r51_gross_income;
		}

		public BigDecimal getR51_aggregate_gross_income() {
			return r51_aggregate_gross_income;
		}

		public void setR51_aggregate_gross_income(BigDecimal r51_aggregate_gross_income) {
			this.r51_aggregate_gross_income = r51_aggregate_gross_income;
		}

		public BigDecimal getR51_risk_weight_factor() {
			return r51_risk_weight_factor;
		}

		public void setR51_risk_weight_factor(BigDecimal r51_risk_weight_factor) {
			this.r51_risk_weight_factor = r51_risk_weight_factor;
		}

		public String getR52_product() {
			return r52_product;
		}

		public void setR52_product(String r52_product) {
			this.r52_product = r52_product;
		}

		public String getR52_month() {
			return r52_month;
		}

		public void setR52_month(String r52_month) {
			this.r52_month = r52_month;
		}

		public BigDecimal getR52_gross_income() {
			return r52_gross_income;
		}

		public void setR52_gross_income(BigDecimal r52_gross_income) {
			this.r52_gross_income = r52_gross_income;
		}

		public BigDecimal getR52_aggregate_gross_income() {
			return r52_aggregate_gross_income;
		}

		public void setR52_aggregate_gross_income(BigDecimal r52_aggregate_gross_income) {
			this.r52_aggregate_gross_income = r52_aggregate_gross_income;
		}

		public BigDecimal getR52_risk_weight_factor() {
			return r52_risk_weight_factor;
		}

		public void setR52_risk_weight_factor(BigDecimal r52_risk_weight_factor) {
			this.r52_risk_weight_factor = r52_risk_weight_factor;
		}

		public String getR53_product() {
			return r53_product;
		}

		public void setR53_product(String r53_product) {
			this.r53_product = r53_product;
		}

		public String getR53_month() {
			return r53_month;
		}

		public void setR53_month(String r53_month) {
			this.r53_month = r53_month;
		}

		public BigDecimal getR53_gross_income() {
			return r53_gross_income;
		}

		public void setR53_gross_income(BigDecimal r53_gross_income) {
			this.r53_gross_income = r53_gross_income;
		}

		public BigDecimal getR53_aggregate_gross_income() {
			return r53_aggregate_gross_income;
		}

		public void setR53_aggregate_gross_income(BigDecimal r53_aggregate_gross_income) {
			this.r53_aggregate_gross_income = r53_aggregate_gross_income;
		}

		public BigDecimal getR53_risk_weight_factor() {
			return r53_risk_weight_factor;
		}

		public void setR53_risk_weight_factor(BigDecimal r53_risk_weight_factor) {
			this.r53_risk_weight_factor = r53_risk_weight_factor;
		}

		public String getR54_product() {
			return r54_product;
		}

		public void setR54_product(String r54_product) {
			this.r54_product = r54_product;
		}

		public String getR54_month() {
			return r54_month;
		}

		public void setR54_month(String r54_month) {
			this.r54_month = r54_month;
		}

		public BigDecimal getR54_gross_income() {
			return r54_gross_income;
		}

		public void setR54_gross_income(BigDecimal r54_gross_income) {
			this.r54_gross_income = r54_gross_income;
		}

		public BigDecimal getR54_aggregate_gross_income() {
			return r54_aggregate_gross_income;
		}

		public void setR54_aggregate_gross_income(BigDecimal r54_aggregate_gross_income) {
			this.r54_aggregate_gross_income = r54_aggregate_gross_income;
		}

		public BigDecimal getR54_risk_weight_factor() {
			return r54_risk_weight_factor;
		}

		public void setR54_risk_weight_factor(BigDecimal r54_risk_weight_factor) {
			this.r54_risk_weight_factor = r54_risk_weight_factor;
		}

		public String getR55_product() {
			return r55_product;
		}

		public void setR55_product(String r55_product) {
			this.r55_product = r55_product;
		}

		public String getR55_month() {
			return r55_month;
		}

		public void setR55_month(String r55_month) {
			this.r55_month = r55_month;
		}

		public BigDecimal getR55_gross_income() {
			return r55_gross_income;
		}

		public void setR55_gross_income(BigDecimal r55_gross_income) {
			this.r55_gross_income = r55_gross_income;
		}

		public BigDecimal getR55_aggregate_gross_income() {
			return r55_aggregate_gross_income;
		}

		public void setR55_aggregate_gross_income(BigDecimal r55_aggregate_gross_income) {
			this.r55_aggregate_gross_income = r55_aggregate_gross_income;
		}

		public BigDecimal getR55_risk_weight_factor() {
			return r55_risk_weight_factor;
		}

		public void setR55_risk_weight_factor(BigDecimal r55_risk_weight_factor) {
			this.r55_risk_weight_factor = r55_risk_weight_factor;
		}

		public String getR56_product() {
			return r56_product;
		}

		public void setR56_product(String r56_product) {
			this.r56_product = r56_product;
		}

		public String getR56_month() {
			return r56_month;
		}

		public void setR56_month(String r56_month) {
			this.r56_month = r56_month;
		}

		public BigDecimal getR56_gross_income() {
			return r56_gross_income;
		}

		public void setR56_gross_income(BigDecimal r56_gross_income) {
			this.r56_gross_income = r56_gross_income;
		}

		public BigDecimal getR56_aggregate_gross_income() {
			return r56_aggregate_gross_income;
		}

		public void setR56_aggregate_gross_income(BigDecimal r56_aggregate_gross_income) {
			this.r56_aggregate_gross_income = r56_aggregate_gross_income;
		}

		public BigDecimal getR56_risk_weight_factor() {
			return r56_risk_weight_factor;
		}

		public void setR56_risk_weight_factor(BigDecimal r56_risk_weight_factor) {
			this.r56_risk_weight_factor = r56_risk_weight_factor;
		}

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

	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_OR1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("M_OR1 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);
		System.out.println("DtlType = " + dtltype);

		try {

			Date dt = dateformat.parse(todate);
			if ("detail".equalsIgnoreCase(dtltype)) {

				// ARCHIVAL DETAIL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OR1_Archival_Detail_Entity> T1Master = getDetaildatabydateListarchival(dt, version);

					System.out.println("Archival Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// RESUB DETAIL
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OR1_Resub_Detail_Entity> T1Master = getResubDetaildatabydateList(dt, version);

					System.out.println("Resub Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// NORMAL DETAIL
				else {

					List<M_OR1_Detail_Entity> T1Master = getDetaildatabydateList(dt);

					System.out.println("Normal Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}
			} else {

				// ARCHIVAL SUMMARY
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_OR1_Archival_Summary_Entity> T1Master = getDataByDateListArchival(dt, version);

					System.out.println("Archival Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// RESUB SUMMARY
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_OR1_Resub_Summary_Entity> T1Master = getResubSummarydatabydateListarchival(dt, version);

					System.out.println("Resub Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// NORMAL SUMMARY
				else {

					List<M_OR1_Summary_Entity> T1Master = getSummaryDataByDate(dt);

					System.out.println("Normal Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				mv.addObject("displaymode", "summary");
			}

			mv.addObject("report_date", dateformat.format(dt));

		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_OR1");

		System.out.println("View Loaded : " + mv.getViewName());

		return mv;
	}

// Archival View
	public List<Object[]> getM_OR1Archival() {

		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<M_OR1_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (M_OR1_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");

				M_OR1_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest archival version: " + first.getReport_version());

			} else {

				System.out.println("No archival data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching M_OR1 Archival data: " + e.getMessage());

			e.printStackTrace();
		}

		return archivalList;
	}

	@Transactional
	public void updateReport(M_OR1_Summary_Entity updatedEntity) {

		System.out.println("Came to services");
		System.out.println("report_date: " + updatedEntity.getReport_date());

		List<M_OR1_Summary_Entity> existingList = getSummaryDataByDate(updatedEntity.getReport_date());

		if (existingList.isEmpty()) {
			throw new RuntimeException("Record not found for REPORT_DATE: " + updatedEntity.getReport_date());
		}

		M_OR1_Summary_Entity existing = existingList.get(0);

		try {

			// ==========================
			// R10 - R48 (Gross Income)
			// ==========================
			for (int i = 10; i <= 48; i++) {

				// Skip R49
				if (i == 49) {
					continue;
				}

				String prefix = "R" + i + "_";

				try {

					Method getter = M_OR1_Summary_Entity.class.getMethod("get" + prefix + "gross_income");

					Method setter = M_OR1_Summary_Entity.class.getMethod("set" + prefix + "gross_income",
							getter.getReturnType());

					Object value = getter.invoke(updatedEntity);

					setter.invoke(existing, value);

				} catch (NoSuchMethodException e) {
					// Ignore missing methods
				}
			}

			// ==========================
			// R50 - R54 (Aggregate Gross Income)
			// ==========================
			for (int i = 50; i <= 54; i++) {

				String prefix = "R" + i + "_";

				try {

					Method getter = M_OR1_Summary_Entity.class.getMethod("get" + prefix + "aggregate_gross_income");

					Method setter = M_OR1_Summary_Entity.class.getMethod("set" + prefix + "aggregate_gross_income",
							getter.getReturnType());

					Object value = getter.invoke(updatedEntity);

					setter.invoke(existing, value);

				} catch (NoSuchMethodException e) {
					// Ignore missing methods
				}
			}

			// ==========================
			// R55 - R56 (Risk Weight Factor)
			// ==========================
			for (int i = 55; i <= 56; i++) {

				String prefix = "R" + i + "_";

				try {

					Method getter = M_OR1_Summary_Entity.class.getMethod("get" + prefix + "risk_weight_factor");

					Method setter = M_OR1_Summary_Entity.class.getMethod("set" + prefix + "risk_weight_factor",
							getter.getReturnType());

					Object value = getter.invoke(updatedEntity);

					setter.invoke(existing, value);

				} catch (NoSuchMethodException e) {
					// Ignore missing methods
				}
			}

			existing.setModify_flg("Y");

			System.out.println("R47 Value Before Save : " + existing.getR47_gross_income());

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		System.out.println("Updating Summary Record.....");

		updateSummaryRecord(existing);
	}

	@Transactional
	public void updateSummaryRecord(M_OR1_Summary_Entity entity) {

		String sql = "UPDATE BRRS_M_OR1_SUMMARYTABLE SET " + "R10_GROSS_INCOME = ?, " + "R11_GROSS_INCOME = ?, "
				+ "R12_GROSS_INCOME = ?, " + "R13_GROSS_INCOME = ?, " + "R14_GROSS_INCOME = ?, "
				+ "R15_GROSS_INCOME = ?, " + "R16_GROSS_INCOME = ?, " + "R17_GROSS_INCOME = ?, "
				+ "R18_GROSS_INCOME = ?, " + "R19_GROSS_INCOME = ?, " + "R20_GROSS_INCOME = ?, "
				+ "R21_GROSS_INCOME = ?, " + "R22_GROSS_INCOME = ?, " + "R23_GROSS_INCOME = ?, "
				+ "R24_GROSS_INCOME = ?, " + "R25_GROSS_INCOME = ?, " + "R26_GROSS_INCOME = ?, "
				+ "R27_GROSS_INCOME = ?, " + "R28_GROSS_INCOME = ?, " + "R29_GROSS_INCOME = ?, "
				+ "R30_GROSS_INCOME = ?, " + "R31_GROSS_INCOME = ?, " + "R32_GROSS_INCOME = ?, "
				+ "R33_GROSS_INCOME = ?, " + "R34_GROSS_INCOME = ?, " + "R35_GROSS_INCOME = ?, "
				+ "R36_GROSS_INCOME = ?, " + "R37_GROSS_INCOME = ?, " + "R38_GROSS_INCOME = ?, "
				+ "R39_GROSS_INCOME = ?, " + "R40_GROSS_INCOME = ?, " + "R41_GROSS_INCOME = ?, "
				+ "R42_GROSS_INCOME = ?, " + "R43_GROSS_INCOME = ?, " + "R44_GROSS_INCOME = ?, "
				+ "R45_GROSS_INCOME = ?, " + "R46_GROSS_INCOME = ?, " + "R47_GROSS_INCOME = ?, "
				+ "R48_GROSS_INCOME = ?, " + "R50_AGGREGATE_GROSS_INCOME = ?, " + "R51_AGGREGATE_GROSS_INCOME = ?, "
				+ "R52_AGGREGATE_GROSS_INCOME = ?, " + "R53_AGGREGATE_GROSS_INCOME = ?, "
				+ "R54_AGGREGATE_GROSS_INCOME = ?, " + "R55_RISK_WEIGHT_FACTOR = ?, " + "R56_RISK_WEIGHT_FACTOR = ? "
				+ "WHERE REPORT_DATE = ?";

		jdbcTemplate.update(sql,

				entity.getR10_gross_income(), entity.getR11_gross_income(), entity.getR12_gross_income(),
				entity.getR13_gross_income(), entity.getR14_gross_income(), entity.getR15_gross_income(),
				entity.getR16_gross_income(), entity.getR17_gross_income(), entity.getR18_gross_income(),
				entity.getR19_gross_income(), entity.getR20_gross_income(), entity.getR21_gross_income(),
				entity.getR22_gross_income(), entity.getR23_gross_income(), entity.getR24_gross_income(),
				entity.getR25_gross_income(), entity.getR26_gross_income(), entity.getR27_gross_income(),
				entity.getR28_gross_income(), entity.getR29_gross_income(), entity.getR30_gross_income(),
				entity.getR31_gross_income(), entity.getR32_gross_income(), entity.getR33_gross_income(),
				entity.getR34_gross_income(), entity.getR35_gross_income(), entity.getR36_gross_income(),
				entity.getR37_gross_income(), entity.getR38_gross_income(), entity.getR39_gross_income(),
				entity.getR40_gross_income(), entity.getR41_gross_income(), entity.getR42_gross_income(),
				entity.getR43_gross_income(), entity.getR44_gross_income(), entity.getR45_gross_income(),
				entity.getR46_gross_income(), entity.getR47_gross_income(), entity.getR48_gross_income(),

				entity.getR50_aggregate_gross_income(), entity.getR51_aggregate_gross_income(),
				entity.getR52_aggregate_gross_income(), entity.getR53_aggregate_gross_income(),
				entity.getR54_aggregate_gross_income(),

				entity.getR55_risk_weight_factor(), entity.getR56_risk_weight_factor(),

				entity.getReport_date());
	}

	public List<Object[]> getM_OR1Resub() {

		List<Object[]> resubList = new ArrayList<>();

		try {

			List<M_OR1_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (M_OR1_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " resub records");

				M_OR1_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest resub version : " + first.getReport_version());

			} else {

				System.out.println("No resub data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching M_OR1 Resub data : " + e.getMessage());

			e.printStackTrace();
		}

		return resubList;
	}

	@Transactional
	public void updateResubReport(M_OR1_Resub_Summary_Entity updatedEntity) {

		System.out.println("==============================================");
		System.out.println("Came to M_OR1 Resub Update");
		System.out.println("==============================================");

		Date reportDate = updatedEntity.getReport_date();

		System.out.println("Report Date : " + reportDate);

		// =====================================================
		// 1. FIND CURRENT MAX VERSION
		// =====================================================

		BigDecimal maxVersion = findMaxVersion(reportDate);

		if (maxVersion == null) {
			throw new RuntimeException("No record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = maxVersion.add(BigDecimal.ONE);

		Date now = new Date(System.currentTimeMillis());

		System.out.println("Existing Max Version : " + maxVersion);
		System.out.println("New Resub Version    : " + newVersion);
		System.out.println("Resub Date           : " + now);

		try {

			// =====================================================
			// 2. CREATE RESUB SUMMARY ENTITY
			// =====================================================

			M_OR1_Resub_Summary_Entity resubSummary = new M_OR1_Resub_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, resubSummary);

			resubSummary.setReport_date(reportDate);
			resubSummary.setReport_version(newVersion);
			resubSummary.setReportResubDate(now);

			// =====================================================
			// 3. CREATE RESUB DETAIL ENTITY
			// =====================================================

			M_OR1_Resub_Detail_Entity resubDetail = new M_OR1_Resub_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, resubDetail);

			resubDetail.setReport_date(reportDate);
			resubDetail.setReport_version(newVersion);
			resubDetail.setReportResubDate(now);

			// =====================================================
			// 4. CREATE ARCHIVAL SUMMARY ENTITY
			// =====================================================

			M_OR1_Archival_Summary_Entity archivalSummary = new M_OR1_Archival_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalSummary);

			archivalSummary.setReport_date(reportDate);
			archivalSummary.setReport_version(newVersion);
			archivalSummary.setReportResubDate(now);

			// =====================================================
			// 5. CREATE ARCHIVAL DETAIL ENTITY
			// =====================================================

			M_OR1_Archival_Detail_Entity archivalDetail = new M_OR1_Archival_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalDetail);

			archivalDetail.setReport_date(reportDate);
			archivalDetail.setReport_version(newVersion);
			archivalDetail.setReportResubDate(now);

			// =====================================================
			// 6. INSERT RESUB SUMMARY
			// =====================================================

			System.out.println("Starting RESUB SUMMARY insert...");

			insertResubSummary(resubSummary);

			// =====================================================
			// 7. INSERT RESUB DETAIL
			// =====================================================

			System.out.println("Starting RESUB DETAIL insert...");

			insertResubDetail(resubDetail);

			// =====================================================
			// 8. INSERT ARCHIVAL SUMMARY
			// =====================================================

			System.out.println("Starting ARCHIVAL SUMMARY insert...");

			insertArchivalSummary(archivalSummary);

			// =====================================================
			// 9. INSERT ARCHIVAL DETAIL
			// =====================================================

			System.out.println("Starting ARCHIVAL DETAIL insert...");

			insertArchivalDetail(archivalDetail);

			// =====================================================
			// 10. AUDIT
			// =====================================================

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attrs != null) {

				HttpServletRequest request = attrs.getRequest();

				String userid = (String) request.getSession().getAttribute("USERID");

				auditService.createBusinessAudit(userid, "RESUBMIT", "M_OR1 Resub Summary", null,
						"BRRS_M_OR1_RESUB_SUMMARYTABLE");
			}

			// =====================================================
			// SUCCESS
			// =====================================================

			System.out.println("==============================================");

			System.out.println("M_OR1 Resub Version Created Successfully : " + newVersion);

			System.out.println("==============================================");

		} catch (Exception e) {

			System.err.println("==============================================");

			System.err.println("ERROR WHILE CREATING M_OR1 RESUB VERSION");

			System.err.println("Report Date : " + reportDate);

			System.err.println("Version : " + newVersion);

			System.err.println("Error : " + e.getMessage());

			e.printStackTrace();

			System.err.println("==============================================");

			throw new RuntimeException("Error while creating M_OR1 Resub Version", e);
		}
	}

	/*
	 * ========================================================= RESUB SUMMARY
	 * =========================================================
	 */

	private void insertResubSummary(M_OR1_Resub_Summary_Entity entity) {

		insertOR1Data("BRRS_M_OR1_RESUB_SUMMARYTABLE", entity);
	}

	/*
	 * ========================================================= RESUB DETAIL
	 * =========================================================
	 */

	private void insertResubDetail(M_OR1_Resub_Detail_Entity entity) {

		insertOR1Data("BRRS_M_OR1_RESUB_DETAILTABLE", entity);
	}

	/*
	 * ========================================================= ARCHIVAL SUMMARY
	 * =========================================================
	 */

	private void insertArchivalSummary(M_OR1_Archival_Summary_Entity entity) {

		insertOR1Data("BRRS_M_OR1_ARCHIVALTABLE_SUMMARY", entity);
	}

	/*
	 * ========================================================= ARCHIVAL DETAIL
	 * =========================================================
	 */

	private void insertArchivalDetail(M_OR1_Archival_Detail_Entity entity) {

		insertOR1Data("BRRS_M_OR1_ARCHIVALTABLE_DETAIL", entity);
	}

	/*
	 * ========================================================= COMMON OR1 INSERT
	 * METHOD =========================================================
	 */

	private void insertOR1Data(String tableName, Object entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO " + tableName + " (REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			// =====================================================
			// COMMON 3 COLUMNS
			// =====================================================

			if (entity instanceof M_OR1_Resub_Summary_Entity) {

				M_OR1_Resub_Summary_Entity e = (M_OR1_Resub_Summary_Entity) entity;

				params.add(e.getReport_date());
				params.add(e.getReport_version());
				params.add(e.getReportResubDate());

			}

			else if (entity instanceof M_OR1_Resub_Detail_Entity) {

				M_OR1_Resub_Detail_Entity e = (M_OR1_Resub_Detail_Entity) entity;

				params.add(e.getReport_date());
				params.add(e.getReport_version());
				params.add(e.getReportResubDate());

			}

			else if (entity instanceof M_OR1_Archival_Summary_Entity) {

				M_OR1_Archival_Summary_Entity e = (M_OR1_Archival_Summary_Entity) entity;

				params.add(e.getReport_date());
				params.add(e.getReport_version());
				params.add(e.getReportResubDate());

			}

			else if (entity instanceof M_OR1_Archival_Detail_Entity) {

				M_OR1_Archival_Detail_Entity e = (M_OR1_Archival_Detail_Entity) entity;

				params.add(e.getReport_date());
				params.add(e.getReport_version());
				params.add(e.getReportResubDate());

			}

			else {

				throw new RuntimeException("Unsupported OR1 entity : " + entity.getClass().getName());
			}

			// =====================================================
			// R10 - R34
			// =====================================================

			for (int i = 10; i <= 34; i++) {

				columns.append("R").append(i).append("_GROSS_INCOME,");

				values.append("?,");

				params.add(getValue1(entity, "getR" + i + "_gross_income"));
			}

			// =====================================================
			// R36 - R48
			// =====================================================

			for (int i = 36; i <= 48; i++) {

				columns.append("R").append(i).append("_GROSS_INCOME,");

				values.append("?,");

				params.add(getValue1(entity, "getR" + i + "_gross_income"));
			}

			// =====================================================
			// R50 - R54
			// =====================================================

			for (int i = 50; i <= 54; i++) {

				columns.append("R").append(i).append("_AGGREGATE_GROSS_INCOME,");

				values.append("?,");

				params.add(getValue1(entity, "getR" + i + "_aggregate_gross_income"));
			}

			// =====================================================
			// R55 - R56
			// =====================================================

			for (int i = 55; i <= 56; i++) {

				columns.append("R").append(i).append("_RISK_WEIGHT_FACTOR,");

				values.append("?,");

				params.add(getValue1(entity, "getR" + i + "_risk_weight_factor"));
			}

			// =====================================================
			// REMOVE LAST COMMA
			// =====================================================

			columns.deleteCharAt(columns.length() - 1);

			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			String sql = columns.toString() + values.toString();

			// =====================================================
			// VALIDATION
			// =====================================================

			int parameterCount = params.size();

			System.out.println("----------------------------------------------");

			System.out.println("OR1 INSERT TABLE : " + tableName);

			System.out.println("OR1 PARAM COUNT  : " + parameterCount);

			System.out.println("OR1 SQL : " + sql);

			// Expected:
			//
			// 3 common columns
			// R10-R34 = 25
			// R36-R48 = 13
			// R50-R54 = 5
			// R55-R56 = 2
			//
			// TOTAL = 48

			if (parameterCount != 48) {

				throw new RuntimeException(
						"OR1 parameter count mismatch. " + "Expected 48 but found " + parameterCount);
			}

			// =====================================================
			// PRINT PARAMETERS
			// =====================================================

			for (int i = 0; i < params.size(); i++) {

				System.out.println("PARAM[" + (i + 1) + "] = " + params.get(i));
			}

			System.out.println("----------------------------------------------");

			// =====================================================
			// EXECUTE INSERT
			// =====================================================

			int rows = jdbcTemplate.update(sql, params.toArray());

			System.out.println("INSERT SUCCESS");

			System.out.println("Table : " + tableName);

			System.out.println("Rows  : " + rows);

			System.out.println("----------------------------------------------");

		} catch (Exception e) {

			System.err.println("==============================================");

			System.err.println("OR1 INSERT FAILED");

			System.err.println("TABLE : " + tableName);

			System.err.println("ENTITY : " + entity.getClass().getName());

			System.err.println("ERROR : " + e.getMessage());

			e.printStackTrace();

			System.err.println("==============================================");

			throw new RuntimeException("Error inserting into " + tableName + " : " + e.getMessage(), e);
		}
	}

	/*
	 * ========================================================= GET VALUE USING
	 * REFLECTION =========================================================
	 */

	private Object getValue1(Object entity, String getterName) {

		try {

			Method method = entity.getClass().getMethod(getterName);

			Object value = method.invoke(entity);

			return value;

		} catch (NoSuchMethodException e) {

			System.err.println("GETTER NOT FOUND");

			System.err.println("Entity : " + entity.getClass().getSimpleName());

			System.err.println("Getter : " + getterName);

			/*
			 * Returning NULL allows the INSERT to continue if the corresponding entity
			 * field does not exist.
			 *
			 * If the DB column is NOT NULL, Oracle will show the exact NOT NULL error.
			 */

			return null;

		} catch (Exception e) {

			System.err.println("ERROR READING GETTER");

			System.err.println("Entity : " + entity.getClass().getSimpleName());

			System.err.println("Getter : " + getterName);

			System.err.println("Error : " + e.getMessage());

			e.printStackTrace();

			return null;
		}
	}

	// ADD THIS METHOD HERE
	private Object getValue(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException("Error invoking method: " + methodName, e);
		}
	}

// Summary EXCEL  FORMAT
	public byte[] BRRS_M_OR1Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelM_OR1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OR1ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_OR1EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<M_OR1_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_OR1 report. Returning empty result.");
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

					try {

						// Row 6 = Excel row 7
						Row dateRow = sheet.getRow(6);

						if (dateRow == null) {
							dateRow = sheet.createRow(6);
						}

						// Column 4 = Excel column D
						Cell dateCell = dateRow.getCell(3);

						if (dateCell == null) {
							dateCell = dateRow.createCell(3);
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
							M_OR1_Summary_Entity record = dataList.get(i);
							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}

							// row11
							// Column C
							Cell cell3 = row.createCell(3);
							if (record.getR10_gross_income() != null) {
								cell3.setCellValue(record.getR10_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(10);
							cell3 = row.createCell(3);
							if (record.getR11_gross_income() != null) {
								cell3.setCellValue(record.getR11_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(11);
							cell3 = row.createCell(3);
							if (record.getR12_gross_income() != null) {
								cell3.setCellValue(record.getR12_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(12);
							cell3 = row.createCell(3);
							if (record.getR13_gross_income() != null) {
								cell3.setCellValue(record.getR13_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);
							cell3 = row.createCell(3);
							if (record.getR14_gross_income() != null) {
								cell3.setCellValue(record.getR14_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(14);
							cell3 = row.createCell(3);
							if (record.getR15_gross_income() != null) {
								cell3.setCellValue(record.getR15_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(15);
							cell3 = row.createCell(3);
							if (record.getR16_gross_income() != null) {
								cell3.setCellValue(record.getR16_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);
							cell3 = row.createCell(3);
							if (record.getR17_gross_income() != null) {
								cell3.setCellValue(record.getR17_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(17);
							cell3 = row.createCell(3);
							if (record.getR18_gross_income() != null) {
								cell3.setCellValue(record.getR18_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(18);
							cell3 = row.createCell(3);
							if (record.getR19_gross_income() != null) {
								cell3.setCellValue(record.getR19_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(19);
							cell3 = row.createCell(3);
							if (record.getR20_gross_income() != null) {
								cell3.setCellValue(record.getR20_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(20);
							cell3 = row.createCell(3);
							if (record.getR21_gross_income() != null) {
								cell3.setCellValue(record.getR21_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(21);
//							cell3 = row.createCell(3);
//							if (record.getR22_gross_income() != null) {
//								cell3.setCellValue(record.getR22_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(22);
							cell3 = row.createCell(3);
							if (record.getR23_gross_income() != null) {
								cell3.setCellValue(record.getR23_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(23);
							cell3 = row.createCell(3);
							if (record.getR24_gross_income() != null) {
								cell3.setCellValue(record.getR24_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(24);
							cell3 = row.createCell(3);
							if (record.getR25_gross_income() != null) {
								cell3.setCellValue(record.getR25_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(25);
							cell3 = row.createCell(3);
							if (record.getR26_gross_income() != null) {
								cell3.setCellValue(record.getR26_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(26);
							cell3 = row.createCell(3);
							if (record.getR27_gross_income() != null) {
								cell3.setCellValue(record.getR27_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(27);
							cell3 = row.createCell(3);
							if (record.getR28_gross_income() != null) {
								cell3.setCellValue(record.getR28_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(28);
							cell3 = row.createCell(3);
							if (record.getR29_gross_income() != null) {
								cell3.setCellValue(record.getR29_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(29);
							cell3 = row.createCell(3);
							if (record.getR30_gross_income() != null) {
								cell3.setCellValue(record.getR30_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(30);
							cell3 = row.createCell(3);
							if (record.getR31_gross_income() != null) {
								cell3.setCellValue(record.getR31_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(31);
							cell3 = row.createCell(3);
							if (record.getR32_gross_income() != null) {
								cell3.setCellValue(record.getR32_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(32);
							cell3 = row.createCell(3);
							if (record.getR33_gross_income() != null) {
								cell3.setCellValue(record.getR33_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(33);
							cell3 = row.createCell(3);
							if (record.getR34_gross_income() != null) {
								cell3.setCellValue(record.getR34_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(34);
//							cell3 = row.createCell(3);
//							if (record.getR35_gross_income() != null) {
//								cell3.setCellValue(record.getR35_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(35);
							cell3 = row.createCell(3);
							if (record.getR36_gross_income() != null) {
								cell3.setCellValue(record.getR36_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(36);
							cell3 = row.createCell(3);
							if (record.getR37_gross_income() != null) {
								cell3.setCellValue(record.getR37_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(37);
							cell3 = row.createCell(3);
							if (record.getR38_gross_income() != null) {
								cell3.setCellValue(record.getR38_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(38);
							cell3 = row.createCell(3);
							if (record.getR39_gross_income() != null) {
								cell3.setCellValue(record.getR39_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(39);
							cell3 = row.createCell(3);
							if (record.getR40_gross_income() != null) {
								cell3.setCellValue(record.getR40_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(40);
							cell3 = row.createCell(3);
							if (record.getR41_gross_income() != null) {
								cell3.setCellValue(record.getR41_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(41);
							cell3 = row.createCell(3);
							if (record.getR42_gross_income() != null) {
								cell3.setCellValue(record.getR42_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(42);
							cell3 = row.createCell(3);
							if (record.getR43_gross_income() != null) {
								cell3.setCellValue(record.getR43_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(43);
							cell3 = row.createCell(3);
							if (record.getR44_gross_income() != null) {
								cell3.setCellValue(record.getR44_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(44);
							cell3 = row.createCell(3);
							if (record.getR45_gross_income() != null) {
								cell3.setCellValue(record.getR45_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(45);
							cell3 = row.createCell(3);
							if (record.getR46_gross_income() != null) {
								cell3.setCellValue(record.getR46_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							row = sheet.getRow(46);
							cell3 = row.createCell(3);
							if (record.getR47_gross_income() != null) {
								cell3.setCellValue(record.getR47_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(47);
//							cell3 = row.createCell(3);
//							if (record.getR48_gross_income() != null) {
//								cell3.setCellValue(record.getR48_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(48);
							cell3 = row.createCell(3);
							if (record.getR49_gross_income() != null) {
								cell3.setCellValue(record.getR49_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(49);
//							cell3 = row.createCell(4);
//							if (record.getR50_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(50);
							cell3 = row.createCell(4);
							if (record.getR51_aggregate_gross_income() != null) {
								cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(51);
//							cell3 = row.createCell(4);
//							if (record.getR52_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(52);
							cell3 = row.createCell(4);
							if (record.getR53_aggregate_gross_income() != null) {
								cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(53);
//							cell3 = row.createCell(4);
//							if (record.getR54_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

							row = sheet.getRow(54);
							cell3 = row.createCell(5);
							if (record.getR55_risk_weight_factor() != null) {
								cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

//							row = sheet.getRow(55);
//							cell3 = row.createCell(5);
//							if (record.getR56_risk_weight_factor() != null) {
//								cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					// audit service

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 SUMMARY", null,
								"BRRS_M_OR1_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

// Summary EXCEL  EMAIL
// Normal Email Excel
	public byte[] BRRS_M_OR1EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OR1ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OR1EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_OR1_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_OR1 report. Returning empty result.");
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

				try {

					// Row 6 = Excel row 7
					Row dateRow = sheet.getRow(6);

					if (dateRow == null) {
						dateRow = sheet.createRow(6);
					}

					// Column 4 = Excel column D
					Cell dateCell = dateRow.getCell(3);

					if (dateCell == null) {
						dateCell = dateRow.createCell(3);
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
						M_OR1_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// row11
						// Column C
						Cell cell3 = row.createCell(2);
						if (record.getR10_gross_income() != null) {
							cell3.setCellValue(record.getR10_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(10);
						cell3 = row.createCell(2);
						if (record.getR11_gross_income() != null) {
							cell3.setCellValue(record.getR11_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(11);
						cell3 = row.createCell(2);
						if (record.getR12_gross_income() != null) {
							cell3.setCellValue(record.getR12_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(12);
						cell3 = row.createCell(2);
						if (record.getR13_gross_income() != null) {
							cell3.setCellValue(record.getR13_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(13);
						cell3 = row.createCell(2);
						if (record.getR14_gross_income() != null) {
							cell3.setCellValue(record.getR14_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(14);
						cell3 = row.createCell(2);
						if (record.getR15_gross_income() != null) {
							cell3.setCellValue(record.getR15_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(15);
						cell3 = row.createCell(2);
						if (record.getR16_gross_income() != null) {
							cell3.setCellValue(record.getR16_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(16);
						cell3 = row.createCell(2);
						if (record.getR17_gross_income() != null) {
							cell3.setCellValue(record.getR17_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(17);
						cell3 = row.createCell(2);
						if (record.getR18_gross_income() != null) {
							cell3.setCellValue(record.getR18_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(18);
						cell3 = row.createCell(2);
						if (record.getR19_gross_income() != null) {
							cell3.setCellValue(record.getR19_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(19);
						cell3 = row.createCell(2);
						if (record.getR20_gross_income() != null) {
							cell3.setCellValue(record.getR20_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(20);
						cell3 = row.createCell(2);
						if (record.getR21_gross_income() != null) {
							cell3.setCellValue(record.getR21_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

//						row = sheet.getRow(21);
//						cell3 = row.createCell(2);
//						if (record.getR22_gross_income() != null) {
//							cell3.setCellValue(record.getR22_gross_income().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}

						row = sheet.getRow(22);
						cell3 = row.createCell(2);
						if (record.getR23_gross_income() != null) {
							cell3.setCellValue(record.getR23_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(23);
						cell3 = row.createCell(2);
						if (record.getR24_gross_income() != null) {
							cell3.setCellValue(record.getR24_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(24);
						cell3 = row.createCell(2);
						if (record.getR25_gross_income() != null) {
							cell3.setCellValue(record.getR25_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(25);
						cell3 = row.createCell(2);
						if (record.getR26_gross_income() != null) {
							cell3.setCellValue(record.getR26_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(26);
						cell3 = row.createCell(2);
						if (record.getR27_gross_income() != null) {
							cell3.setCellValue(record.getR27_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(27);
						cell3 = row.createCell(2);
						if (record.getR28_gross_income() != null) {
							cell3.setCellValue(record.getR28_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(28);
						cell3 = row.createCell(2);
						if (record.getR29_gross_income() != null) {
							cell3.setCellValue(record.getR29_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(29);
						cell3 = row.createCell(2);
						if (record.getR30_gross_income() != null) {
							cell3.setCellValue(record.getR30_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(30);
						cell3 = row.createCell(2);
						if (record.getR31_gross_income() != null) {
							cell3.setCellValue(record.getR31_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(31);
						cell3 = row.createCell(2);
						if (record.getR32_gross_income() != null) {
							cell3.setCellValue(record.getR32_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(32);
						cell3 = row.createCell(2);
						if (record.getR33_gross_income() != null) {
							cell3.setCellValue(record.getR33_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(33);
						cell3 = row.createCell(2);
						if (record.getR34_gross_income() != null) {
							cell3.setCellValue(record.getR34_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

//						row = sheet.getRow(34);
//						cell3 = row.createCell(2);
//						if (record.getR35_gross_income() != null) {
//							cell3.setCellValue(record.getR35_gross_income().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}

						row = sheet.getRow(35);
						cell3 = row.createCell(2);
						if (record.getR36_gross_income() != null) {
							cell3.setCellValue(record.getR36_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(36);
						cell3 = row.createCell(2);
						if (record.getR37_gross_income() != null) {
							cell3.setCellValue(record.getR37_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(37);
						cell3 = row.createCell(2);
						if (record.getR38_gross_income() != null) {
							cell3.setCellValue(record.getR38_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(38);
						cell3 = row.createCell(2);
						if (record.getR39_gross_income() != null) {
							cell3.setCellValue(record.getR39_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(39);
						cell3 = row.createCell(2);
						if (record.getR40_gross_income() != null) {
							cell3.setCellValue(record.getR40_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(40);
						cell3 = row.createCell(2);
						if (record.getR41_gross_income() != null) {
							cell3.setCellValue(record.getR41_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(41);
						cell3 = row.createCell(2);
						if (record.getR42_gross_income() != null) {
							cell3.setCellValue(record.getR42_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(42);
						cell3 = row.createCell(2);
						if (record.getR43_gross_income() != null) {
							cell3.setCellValue(record.getR43_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(43);
						cell3 = row.createCell(2);
						if (record.getR44_gross_income() != null) {
							cell3.setCellValue(record.getR44_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(44);
						cell3 = row.createCell(2);
						if (record.getR45_gross_income() != null) {
							cell3.setCellValue(record.getR45_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(45);
						cell3 = row.createCell(2);
						if (record.getR46_gross_income() != null) {
							cell3.setCellValue(record.getR46_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(46);
						cell3 = row.createCell(2);
						if (record.getR47_gross_income() != null) {
							cell3.setCellValue(record.getR47_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

//						row = sheet.getRow(47);
//						cell3 = row.createCell(2);
//						if (record.getR48_gross_income() != null) {
//							cell3.setCellValue(record.getR48_gross_income().doubleValue());
//							cell3.setCellStyle(numberStyle);
//						} else {
//							cell3.setCellValue("");
//							cell3.setCellStyle(textStyle);
//						}

						/*
						 * row = sheet.getRow(48); cell3 = row.createCell(2); if
						 * (record.getR49_gross_income() != null) {
						 * cell3.setCellValue(record.getR49_gross_income().doubleValue());
						 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
						 * cell3.setCellStyle(textStyle); }
						 */
						row = sheet.getRow(48);
						cell3 = row.createCell(3);
						if (record.getR50_aggregate_gross_income() != null) {
							cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(49);
						cell3 = row.createCell(3);
						if (record.getR51_aggregate_gross_income() != null) {
							cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(50);
						cell3 = row.createCell(3);
						if (record.getR52_aggregate_gross_income() != null) {
							cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(51);
						cell3 = row.createCell(3);
						if (record.getR53_aggregate_gross_income() != null) {
							cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(52);
						cell3 = row.createCell(3);
						if (record.getR54_aggregate_gross_income() != null) {
							cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(53);
						cell3 = row.createCell(4);
						if (record.getR55_risk_weight_factor() != null) {
							cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						row = sheet.getRow(54);
						cell3 = row.createCell(4);
						if (record.getR56_risk_weight_factor() != null) {
							cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

					}
					workbook.setForceFormulaRecalculation(true);
				} else {

				}
				// Write the final workbook content to the in-memory stream.
				workbook.write(out);

				logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

				ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (attrs != null) {
					HttpServletRequest request = attrs.getRequest();
					String userid = (String) request.getSession().getAttribute("USERID");
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 EMAIL SUMMARY", null,
							"BRRS_M_OR1_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

//ARCHIVAL SUMMARY EXCEL  FORMAT
// Archival format excel
	public byte[] getExcelM_OR1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_OR1ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OR1_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OR1 report. Returning empty result.");
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

				// Column 4 = Excel column D
				Cell dateCell = dateRow.getCell(3);

				if (dateCell == null) {
					dateCell = dateRow.createCell(3);
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
					M_OR1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
						cell3.setCellValue(record.getR11_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
						cell3.setCellValue(record.getR12_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
						cell3.setCellValue(record.getR13_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
						cell3.setCellValue(record.getR14_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
						cell3.setCellValue(record.getR15_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
						cell3.setCellValue(record.getR16_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
						cell3.setCellValue(record.getR17_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
						cell3.setCellValue(record.getR18_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
						cell3.setCellValue(record.getR19_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
						cell3.setCellValue(record.getR20_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
						cell3.setCellValue(record.getR21_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(21);
//							cell3 = row.createCell(3);
//							if (record.getR22_gross_income() != null) {
//								cell3.setCellValue(record.getR22_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
						cell3.setCellValue(record.getR23_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
						cell3.setCellValue(record.getR24_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
						cell3.setCellValue(record.getR25_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
						cell3.setCellValue(record.getR26_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
						cell3.setCellValue(record.getR27_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
						cell3.setCellValue(record.getR28_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
						cell3.setCellValue(record.getR29_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
						cell3.setCellValue(record.getR30_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
						cell3.setCellValue(record.getR31_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
						cell3.setCellValue(record.getR32_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
						cell3.setCellValue(record.getR33_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
						cell3.setCellValue(record.getR34_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(34);
//							cell3 = row.createCell(3);
//							if (record.getR35_gross_income() != null) {
//								cell3.setCellValue(record.getR35_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
						cell3.setCellValue(record.getR36_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
						cell3.setCellValue(record.getR37_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
						cell3.setCellValue(record.getR38_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
						cell3.setCellValue(record.getR39_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
						cell3.setCellValue(record.getR40_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
						cell3.setCellValue(record.getR41_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
						cell3.setCellValue(record.getR42_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
						cell3.setCellValue(record.getR43_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
						cell3.setCellValue(record.getR44_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
						cell3.setCellValue(record.getR45_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
						cell3.setCellValue(record.getR46_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
						cell3.setCellValue(record.getR47_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(47);
//							cell3 = row.createCell(3);
//							if (record.getR48_gross_income() != null) {
//								cell3.setCellValue(record.getR48_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
						cell3.setCellValue(record.getR49_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(49);
//							cell3 = row.createCell(4);
//							if (record.getR50_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(51);
//							cell3 = row.createCell(4);
//							if (record.getR52_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(53);
//							cell3 = row.createCell(4);
//							if (record.getR54_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(55);
//							cell3 = row.createCell(5);
//							if (record.getR56_risk_weight_factor() != null) {
//								cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 ARCHIVAL SUMMARY", null,
						"BRRS_M_OR1_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

//ARCHIVAL SUMMARY EXCEL  EMAIL

// Archival Email Excel
	public byte[] BRRS_M_OR1ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_OR1_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OR1 report. Returning empty result.");
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

				// Column 4 = Excel column D
				Cell dateCell = dateRow.getCell(3);

				if (dateCell == null) {
					dateCell = dateRow.createCell(3);
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
					M_OR1_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column C
					Cell cell3 = row.createCell(2);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					cell3 = row.createCell(2);
					if (record.getR11_gross_income() != null) {
						cell3.setCellValue(record.getR11_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(2);
					if (record.getR12_gross_income() != null) {
						cell3.setCellValue(record.getR12_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(2);
					if (record.getR13_gross_income() != null) {
						cell3.setCellValue(record.getR13_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(2);
					if (record.getR14_gross_income() != null) {
						cell3.setCellValue(record.getR14_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(2);
					if (record.getR15_gross_income() != null) {
						cell3.setCellValue(record.getR15_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(2);
					if (record.getR16_gross_income() != null) {
						cell3.setCellValue(record.getR16_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(2);
					if (record.getR17_gross_income() != null) {
						cell3.setCellValue(record.getR17_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(2);
					if (record.getR18_gross_income() != null) {
						cell3.setCellValue(record.getR18_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(2);
					if (record.getR19_gross_income() != null) {
						cell3.setCellValue(record.getR19_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(2);
					if (record.getR20_gross_income() != null) {
						cell3.setCellValue(record.getR20_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(2);
					if (record.getR21_gross_income() != null) {
						cell3.setCellValue(record.getR21_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(21);
//					cell3 = row.createCell(2);
//					if (record.getR22_gross_income() != null) {
//						cell3.setCellValue(record.getR22_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(22);
					cell3 = row.createCell(2);
					if (record.getR23_gross_income() != null) {
						cell3.setCellValue(record.getR23_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(2);
					if (record.getR24_gross_income() != null) {
						cell3.setCellValue(record.getR24_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(2);
					if (record.getR25_gross_income() != null) {
						cell3.setCellValue(record.getR25_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(2);
					if (record.getR26_gross_income() != null) {
						cell3.setCellValue(record.getR26_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(2);
					if (record.getR27_gross_income() != null) {
						cell3.setCellValue(record.getR27_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(2);
					if (record.getR28_gross_income() != null) {
						cell3.setCellValue(record.getR28_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(2);
					if (record.getR29_gross_income() != null) {
						cell3.setCellValue(record.getR29_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(2);
					if (record.getR30_gross_income() != null) {
						cell3.setCellValue(record.getR30_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(2);
					if (record.getR31_gross_income() != null) {
						cell3.setCellValue(record.getR31_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(2);
					if (record.getR32_gross_income() != null) {
						cell3.setCellValue(record.getR32_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(2);
					if (record.getR33_gross_income() != null) {
						cell3.setCellValue(record.getR33_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(2);
					if (record.getR34_gross_income() != null) {
						cell3.setCellValue(record.getR34_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(34);
//					cell3 = row.createCell(2);
//					if (record.getR35_gross_income() != null) {
//						cell3.setCellValue(record.getR35_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(35);
					cell3 = row.createCell(2);
					if (record.getR36_gross_income() != null) {
						cell3.setCellValue(record.getR36_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(2);
					if (record.getR37_gross_income() != null) {
						cell3.setCellValue(record.getR37_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(2);
					if (record.getR38_gross_income() != null) {
						cell3.setCellValue(record.getR38_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(2);
					if (record.getR39_gross_income() != null) {
						cell3.setCellValue(record.getR39_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(2);
					if (record.getR40_gross_income() != null) {
						cell3.setCellValue(record.getR40_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(2);
					if (record.getR41_gross_income() != null) {
						cell3.setCellValue(record.getR41_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(2);
					if (record.getR42_gross_income() != null) {
						cell3.setCellValue(record.getR42_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(2);
					if (record.getR43_gross_income() != null) {
						cell3.setCellValue(record.getR43_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(2);
					if (record.getR44_gross_income() != null) {
						cell3.setCellValue(record.getR44_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(2);
					if (record.getR45_gross_income() != null) {
						cell3.setCellValue(record.getR45_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(2);
					if (record.getR46_gross_income() != null) {
						cell3.setCellValue(record.getR46_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(2);
					if (record.getR47_gross_income() != null) {
						cell3.setCellValue(record.getR47_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(47);
//					cell3 = row.createCell(2);
//					if (record.getR48_gross_income() != null) {
//						cell3.setCellValue(record.getR48_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					/*
					 * row = sheet.getRow(48); cell3 = row.createCell(2); if
					 * (record.getR49_gross_income() != null) {
					 * cell3.setCellValue(record.getR49_gross_income().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR50_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(49);
					cell3 = row.createCell(3);
					if (record.getR51_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell3 = row.createCell(3);
					if (record.getR52_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell3 = row.createCell(3);
					if (record.getR53_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(52);
//					cell3 = row.createCell(3);
//					if (record.getR54_aggregate_gross_income() != null) {
//						cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR55_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell3 = row.createCell(4);
					if (record.getR56_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_M_OR1_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

// RESUB EXCEL  FORMAT

	// Resub Format excel
	public byte[] BRRS_M_OR1ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_OR1EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_OR1_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_OR1 report. Returning empty result.");
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

				// Column 4 = Excel column D
				Cell dateCell = dateRow.getCell(3);

				if (dateCell == null) {
					dateCell = dateRow.createCell(3);
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
					M_OR1_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column C
					Cell cell3 = row.createCell(3);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					cell3 = row.createCell(3);
					if (record.getR11_gross_income() != null) {
						cell3.setCellValue(record.getR11_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(3);
					if (record.getR12_gross_income() != null) {
						cell3.setCellValue(record.getR12_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(3);
					if (record.getR13_gross_income() != null) {
						cell3.setCellValue(record.getR13_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(3);
					if (record.getR14_gross_income() != null) {
						cell3.setCellValue(record.getR14_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(3);
					if (record.getR15_gross_income() != null) {
						cell3.setCellValue(record.getR15_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(3);
					if (record.getR16_gross_income() != null) {
						cell3.setCellValue(record.getR16_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(3);
					if (record.getR17_gross_income() != null) {
						cell3.setCellValue(record.getR17_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(3);
					if (record.getR18_gross_income() != null) {
						cell3.setCellValue(record.getR18_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(3);
					if (record.getR19_gross_income() != null) {
						cell3.setCellValue(record.getR19_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(3);
					if (record.getR20_gross_income() != null) {
						cell3.setCellValue(record.getR20_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(3);
					if (record.getR21_gross_income() != null) {
						cell3.setCellValue(record.getR21_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(21);
//							cell3 = row.createCell(3);
//							if (record.getR22_gross_income() != null) {
//								cell3.setCellValue(record.getR22_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(22);
					cell3 = row.createCell(3);
					if (record.getR23_gross_income() != null) {
						cell3.setCellValue(record.getR23_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(3);
					if (record.getR24_gross_income() != null) {
						cell3.setCellValue(record.getR24_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(3);
					if (record.getR25_gross_income() != null) {
						cell3.setCellValue(record.getR25_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(3);
					if (record.getR26_gross_income() != null) {
						cell3.setCellValue(record.getR26_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(3);
					if (record.getR27_gross_income() != null) {
						cell3.setCellValue(record.getR27_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(3);
					if (record.getR28_gross_income() != null) {
						cell3.setCellValue(record.getR28_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(3);
					if (record.getR29_gross_income() != null) {
						cell3.setCellValue(record.getR29_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(3);
					if (record.getR30_gross_income() != null) {
						cell3.setCellValue(record.getR30_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(3);
					if (record.getR31_gross_income() != null) {
						cell3.setCellValue(record.getR31_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(3);
					if (record.getR32_gross_income() != null) {
						cell3.setCellValue(record.getR32_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(3);
					if (record.getR33_gross_income() != null) {
						cell3.setCellValue(record.getR33_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(3);
					if (record.getR34_gross_income() != null) {
						cell3.setCellValue(record.getR34_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(34);
//							cell3 = row.createCell(3);
//							if (record.getR35_gross_income() != null) {
//								cell3.setCellValue(record.getR35_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(35);
					cell3 = row.createCell(3);
					if (record.getR36_gross_income() != null) {
						cell3.setCellValue(record.getR36_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(3);
					if (record.getR37_gross_income() != null) {
						cell3.setCellValue(record.getR37_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(3);
					if (record.getR38_gross_income() != null) {
						cell3.setCellValue(record.getR38_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(3);
					if (record.getR39_gross_income() != null) {
						cell3.setCellValue(record.getR39_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(3);
					if (record.getR40_gross_income() != null) {
						cell3.setCellValue(record.getR40_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(3);
					if (record.getR41_gross_income() != null) {
						cell3.setCellValue(record.getR41_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(3);
					if (record.getR42_gross_income() != null) {
						cell3.setCellValue(record.getR42_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(3);
					if (record.getR43_gross_income() != null) {
						cell3.setCellValue(record.getR43_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(3);
					if (record.getR44_gross_income() != null) {
						cell3.setCellValue(record.getR44_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(3);
					if (record.getR45_gross_income() != null) {
						cell3.setCellValue(record.getR45_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(3);
					if (record.getR46_gross_income() != null) {
						cell3.setCellValue(record.getR46_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(3);
					if (record.getR47_gross_income() != null) {
						cell3.setCellValue(record.getR47_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(47);
//							cell3 = row.createCell(3);
//							if (record.getR48_gross_income() != null) {
//								cell3.setCellValue(record.getR48_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR49_gross_income() != null) {
						cell3.setCellValue(record.getR49_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(49);
//							cell3 = row.createCell(4);
//							if (record.getR50_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(50);
					cell3 = row.createCell(4);
					if (record.getR51_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(51);
//							cell3 = row.createCell(4);
//							if (record.getR52_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(52);
					cell3 = row.createCell(4);
					if (record.getR53_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(53);
//							cell3 = row.createCell(4);
//							if (record.getR54_aggregate_gross_income() != null) {
//								cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

					row = sheet.getRow(54);
					cell3 = row.createCell(5);
					if (record.getR55_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//							row = sheet.getRow(55);
//							cell3 = row.createCell(5);
//							if (record.getR56_risk_weight_factor() != null) {
//								cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
//								cell3.setCellStyle(numberStyle);
//							} else {
//								cell3.setCellValue("");
//								cell3.setCellStyle(textStyle);
//							}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 RESUB SUMMARY", null,
						"BRRS_M_OR1_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

// RESUB  EXCEL EMAIL
	// Resub Email Excel
	public byte[] BRRS_M_OR1EmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB Email Excel generation process in memory.");

		List<M_OR1_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_OR1 report. Returning empty result.");
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

				// Column 4 = Excel column D
				Cell dateCell = dateRow.getCell(3);

				if (dateCell == null) {
					dateCell = dateRow.createCell(3);
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
					M_OR1_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// row11
					// Column C
					Cell cell3 = row.createCell(2);
					if (record.getR10_gross_income() != null) {
						cell3.setCellValue(record.getR10_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(10);
					cell3 = row.createCell(2);
					if (record.getR11_gross_income() != null) {
						cell3.setCellValue(record.getR11_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(11);
					cell3 = row.createCell(2);
					if (record.getR12_gross_income() != null) {
						cell3.setCellValue(record.getR12_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(12);
					cell3 = row.createCell(2);
					if (record.getR13_gross_income() != null) {
						cell3.setCellValue(record.getR13_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					cell3 = row.createCell(2);
					if (record.getR14_gross_income() != null) {
						cell3.setCellValue(record.getR14_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);
					cell3 = row.createCell(2);
					if (record.getR15_gross_income() != null) {
						cell3.setCellValue(record.getR15_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);
					cell3 = row.createCell(2);
					if (record.getR16_gross_income() != null) {
						cell3.setCellValue(record.getR16_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);
					cell3 = row.createCell(2);
					if (record.getR17_gross_income() != null) {
						cell3.setCellValue(record.getR17_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);
					cell3 = row.createCell(2);
					if (record.getR18_gross_income() != null) {
						cell3.setCellValue(record.getR18_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(18);
					cell3 = row.createCell(2);
					if (record.getR19_gross_income() != null) {
						cell3.setCellValue(record.getR19_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(19);
					cell3 = row.createCell(2);
					if (record.getR20_gross_income() != null) {
						cell3.setCellValue(record.getR20_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(20);
					cell3 = row.createCell(2);
					if (record.getR21_gross_income() != null) {
						cell3.setCellValue(record.getR21_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(21);
//					cell3 = row.createCell(2);
//					if (record.getR22_gross_income() != null) {
//						cell3.setCellValue(record.getR22_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(22);
					cell3 = row.createCell(2);
					if (record.getR23_gross_income() != null) {
						cell3.setCellValue(record.getR23_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(23);
					cell3 = row.createCell(2);
					if (record.getR24_gross_income() != null) {
						cell3.setCellValue(record.getR24_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
					cell3 = row.createCell(2);
					if (record.getR25_gross_income() != null) {
						cell3.setCellValue(record.getR25_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
					cell3 = row.createCell(2);
					if (record.getR26_gross_income() != null) {
						cell3.setCellValue(record.getR26_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(26);
					cell3 = row.createCell(2);
					if (record.getR27_gross_income() != null) {
						cell3.setCellValue(record.getR27_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(27);
					cell3 = row.createCell(2);
					if (record.getR28_gross_income() != null) {
						cell3.setCellValue(record.getR28_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(28);
					cell3 = row.createCell(2);
					if (record.getR29_gross_income() != null) {
						cell3.setCellValue(record.getR29_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
					cell3 = row.createCell(2);
					if (record.getR30_gross_income() != null) {
						cell3.setCellValue(record.getR30_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(30);
					cell3 = row.createCell(2);
					if (record.getR31_gross_income() != null) {
						cell3.setCellValue(record.getR31_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(31);
					cell3 = row.createCell(2);
					if (record.getR32_gross_income() != null) {
						cell3.setCellValue(record.getR32_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(32);
					cell3 = row.createCell(2);
					if (record.getR33_gross_income() != null) {
						cell3.setCellValue(record.getR33_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
					cell3 = row.createCell(2);
					if (record.getR34_gross_income() != null) {
						cell3.setCellValue(record.getR34_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(34);
//					cell3 = row.createCell(2);
//					if (record.getR35_gross_income() != null) {
//						cell3.setCellValue(record.getR35_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(35);
					cell3 = row.createCell(2);
					if (record.getR36_gross_income() != null) {
						cell3.setCellValue(record.getR36_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(36);
					cell3 = row.createCell(2);
					if (record.getR37_gross_income() != null) {
						cell3.setCellValue(record.getR37_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
					cell3 = row.createCell(2);
					if (record.getR38_gross_income() != null) {
						cell3.setCellValue(record.getR38_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
					cell3 = row.createCell(2);
					if (record.getR39_gross_income() != null) {
						cell3.setCellValue(record.getR39_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(39);
					cell3 = row.createCell(2);
					if (record.getR40_gross_income() != null) {
						cell3.setCellValue(record.getR40_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(40);
					cell3 = row.createCell(2);
					if (record.getR41_gross_income() != null) {
						cell3.setCellValue(record.getR41_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
					cell3 = row.createCell(2);
					if (record.getR42_gross_income() != null) {
						cell3.setCellValue(record.getR42_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
					cell3 = row.createCell(2);
					if (record.getR43_gross_income() != null) {
						cell3.setCellValue(record.getR43_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
					cell3 = row.createCell(2);
					if (record.getR44_gross_income() != null) {
						cell3.setCellValue(record.getR44_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(44);
					cell3 = row.createCell(2);
					if (record.getR45_gross_income() != null) {
						cell3.setCellValue(record.getR45_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
					cell3 = row.createCell(2);
					if (record.getR46_gross_income() != null) {
						cell3.setCellValue(record.getR46_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(46);
					cell3 = row.createCell(2);
					if (record.getR47_gross_income() != null) {
						cell3.setCellValue(record.getR47_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(47);
//					cell3 = row.createCell(2);
//					if (record.getR48_gross_income() != null) {
//						cell3.setCellValue(record.getR48_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					/*
					 * row = sheet.getRow(48); cell3 = row.createCell(2); if
					 * (record.getR49_gross_income() != null) {
					 * cell3.setCellValue(record.getR49_gross_income().doubleValue());
					 * cell3.setCellStyle(numberStyle); } else { cell3.setCellValue("");
					 * cell3.setCellStyle(textStyle); }
					 */
					row = sheet.getRow(48);
					cell3 = row.createCell(3);
					if (record.getR50_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR50_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(49);
					cell3 = row.createCell(3);
					if (record.getR51_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR51_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(50);
					cell3 = row.createCell(3);
					if (record.getR52_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR52_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(51);
					cell3 = row.createCell(3);
					if (record.getR53_aggregate_gross_income() != null) {
						cell3.setCellValue(record.getR53_aggregate_gross_income().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//					row = sheet.getRow(52);
//					cell3 = row.createCell(3);
//					if (record.getR54_aggregate_gross_income() != null) {
//						cell3.setCellValue(record.getR54_aggregate_gross_income().doubleValue());
//						cell3.setCellStyle(numberStyle);
//					} else {
//						cell3.setCellValue("");
//						cell3.setCellStyle(textStyle);
//					}

					row = sheet.getRow(53);
					cell3 = row.createCell(4);
					if (record.getR55_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR55_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(54);
					cell3 = row.createCell(4);
					if (record.getR56_risk_weight_factor() != null) {
						cell3.setCellValue(record.getR56_risk_weight_factor().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_OR1 EMAIL RESUB SUMMARY", null,
						"BRRS_M_OR1_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

}