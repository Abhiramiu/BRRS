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

@Service

public class BRRS_Q_RLFA1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_RLFA1_ReportService.class);

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

	public List<Q_RLFA1_Summary_Entity> getSummaryDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_RLFA1_SUMMARY_TABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_RLFA1_Summary_RowMapper());
	}

	// findbyreportdate

	public Q_RLFA1_Summary_Entity findByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_RLFA1_SUMMARY_TABLE " + "WHERE REPORT_DATE = ?";

		List<Q_RLFA1_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new Q_RLFA1_Summary_RowMapper());

		return list.isEmpty() ? null : list.get(0);
	}

// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================

	public List<Object[]> get_Q_RLFA1_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Q_RLFA1_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_RLFA1_Archival_Summary_RowMapper());
	}

	public List<Q_RLFA1_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Q_RLFA1_Archival_Summary_RowMapper());
	}

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

// =====================================================
// DETAIL REPO
// =====================================================	

	public List<Q_RLFA1_Detail_Entity> getDetaildatabydateList(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_RLFA1_DETAIL_TABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_RLFA1_Detail_RowMapper());
	}

// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

	public List<Map<String, Object>> getQ_RLFA1_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_RLFA1_Archival_Detail_Entity> getDetaildatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_RLFA1_Archival_Detail_RowMapper());
	}

	public BigDecimal findDETAILMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_RLFA1_Archival_Detail_Entity getArchivalListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_RLFA1_Archival_Detail_RowMapper());
	}

// =====================================================
// RESUB SUMMARY
// =====================================================

	public List<Q_RLFA1_Resub_Summary_Entity> getResubSummarydatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_RESUB_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_RLFA1_RESUB_Summary_RowMapper());
	}

	public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_RLFA1_RESUB_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Map<String, Object>> getQ_RLFA1_Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_RLFA1_RESUB_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public Q_RLFA1_Resub_Summary_Entity getResubSummarydatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_RESUB_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_RLFA1_RESUB_Summary_RowMapper());
	}

// =====================================================
// RESUB DETAIL
// =====================================================

	public List<Map<String, Object>> get_Q_RLFA1Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_RLFA1_RESUB_DETAIL "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_RLFA1_Resub_Detail_Entity> getResubDetaildatabydateList(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_RESUB_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_RLFA1_RESUB_Detail_RowMapper());
	}

	public BigDecimal findResubDetailMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_RLFA1_RESUB_DETAIL " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_RLFA1_Resub_Detail_Entity getdResubDetailDatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_RLFA1_RESUB_DETAIL " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_RLFA1_RESUB_Detail_RowMapper());
	}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================

	public class Q_RLFA1_Summary_RowMapper implements RowMapper<Q_RLFA1_Summary_Entity> {

		@Override
		public Q_RLFA1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Summary_Entity obj = new Q_RLFA1_Summary_Entity();
			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));

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

	public class Q_RLFA1_Summary_Entity {

		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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

	public class Q_RLFA1_Archival_Summary_RowMapper implements RowMapper<Q_RLFA1_Archival_Summary_Entity> {

		@Override
		public Q_RLFA1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Archival_Summary_Entity obj = new Q_RLFA1_Archival_Summary_Entity();

			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));
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

	public class Q_RLFA1_Archival_Summary_Entity {
		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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
// DETAIL ENTITY  Q_RLFA1
// =====================================================	

	public class Q_RLFA1_Detail_RowMapper implements RowMapper<Q_RLFA1_Detail_Entity> {

		@Override
		public Q_RLFA1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Detail_Entity obj = new Q_RLFA1_Detail_Entity();
			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));

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

	public class Q_RLFA1_Detail_Entity {
		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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

	public class Q_RLFA1_Archival_Detail_RowMapper implements RowMapper<Q_RLFA1_Archival_Detail_Entity> {

		@Override
		public Q_RLFA1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Archival_Detail_Entity obj = new Q_RLFA1_Archival_Detail_Entity();

			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));

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

	public class Q_RLFA1_Archival_Detail_Entity {
		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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
// RESUB summary Q_RLFA1
//=====================================================

	public class Q_RLFA1_RESUB_Summary_RowMapper implements RowMapper<Q_RLFA1_Resub_Summary_Entity> {

		@Override
		public Q_RLFA1_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Resub_Summary_Entity obj = new Q_RLFA1_Resub_Summary_Entity();
			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));

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

	public class Q_RLFA1_Resub_Summary_Entity {
		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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
// RESUB DETAIL Q_RLFA1
//=====================================================

	public class Q_RLFA1_RESUB_Detail_RowMapper implements RowMapper<Q_RLFA1_Resub_Detail_Entity> {

		@Override
		public Q_RLFA1_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_RLFA1_Resub_Detail_Entity obj = new Q_RLFA1_Resub_Detail_Entity();
			obj.setR10_rene_loans(rs.getString("r10_rene_loans"));
			obj.setR10_collateral_amount(rs.getBigDecimal("r10_collateral_amount"));
			obj.setR10_carrying_amount(rs.getBigDecimal("r10_carrying_amount"));
			obj.setR10_no_of_accts(rs.getBigDecimal("r10_no_of_accts"));

			obj.setR11_rene_loans(rs.getString("r11_rene_loans"));
			obj.setR11_collateral_amount(rs.getBigDecimal("r11_collateral_amount"));
			obj.setR11_carrying_amount(rs.getBigDecimal("r11_carrying_amount"));
			obj.setR11_no_of_accts(rs.getBigDecimal("r11_no_of_accts"));

			obj.setR12_rene_loans(rs.getString("r12_rene_loans"));
			obj.setR12_collateral_amount(rs.getBigDecimal("r12_collateral_amount"));
			obj.setR12_carrying_amount(rs.getBigDecimal("r12_carrying_amount"));
			obj.setR12_no_of_accts(rs.getBigDecimal("r12_no_of_accts"));

			obj.setR13_rene_loans(rs.getString("r13_rene_loans"));
			obj.setR13_collateral_amount(rs.getBigDecimal("r13_collateral_amount"));
			obj.setR13_carrying_amount(rs.getBigDecimal("r13_carrying_amount"));
			obj.setR13_no_of_accts(rs.getBigDecimal("r13_no_of_accts"));

			obj.setR14_rene_loans(rs.getString("r14_rene_loans"));
			obj.setR14_collateral_amount(rs.getBigDecimal("r14_collateral_amount"));
			obj.setR14_carrying_amount(rs.getBigDecimal("r14_carrying_amount"));
			obj.setR14_no_of_accts(rs.getBigDecimal("r14_no_of_accts"));

			obj.setR15_rene_loans(rs.getString("r15_rene_loans"));
			obj.setR15_collateral_amount(rs.getBigDecimal("r15_collateral_amount"));
			obj.setR15_carrying_amount(rs.getBigDecimal("r15_carrying_amount"));
			obj.setR15_no_of_accts(rs.getBigDecimal("r15_no_of_accts"));

			obj.setR16_rene_loans(rs.getString("r16_rene_loans"));
			obj.setR16_collateral_amount(rs.getBigDecimal("r16_collateral_amount"));
			obj.setR16_carrying_amount(rs.getBigDecimal("r16_carrying_amount"));
			obj.setR16_no_of_accts(rs.getBigDecimal("r16_no_of_accts"));

			obj.setR17_rene_loans(rs.getString("r17_rene_loans"));
			obj.setR17_collateral_amount(rs.getBigDecimal("r17_collateral_amount"));
			obj.setR17_carrying_amount(rs.getBigDecimal("r17_carrying_amount"));
			obj.setR17_no_of_accts(rs.getBigDecimal("r17_no_of_accts"));

			obj.setR18_rene_loans(rs.getString("r18_rene_loans"));
			obj.setR18_collateral_amount(rs.getBigDecimal("r18_collateral_amount"));
			obj.setR18_carrying_amount(rs.getBigDecimal("r18_carrying_amount"));
			obj.setR18_no_of_accts(rs.getBigDecimal("r18_no_of_accts"));

			obj.setR19_rene_loans(rs.getString("r19_rene_loans"));
			obj.setR19_collateral_amount(rs.getBigDecimal("r19_collateral_amount"));
			obj.setR19_carrying_amount(rs.getBigDecimal("r19_carrying_amount"));
			obj.setR19_no_of_accts(rs.getBigDecimal("r19_no_of_accts"));

			obj.setR20_rene_loans(rs.getString("r20_rene_loans"));
			obj.setR20_collateral_amount(rs.getBigDecimal("r20_collateral_amount"));
			obj.setR20_carrying_amount(rs.getBigDecimal("r20_carrying_amount"));
			obj.setR20_no_of_accts(rs.getBigDecimal("r20_no_of_accts"));

			obj.setR21_rene_loans(rs.getString("r21_rene_loans"));
			obj.setR21_collateral_amount(rs.getBigDecimal("r21_collateral_amount"));
			obj.setR21_carrying_amount(rs.getBigDecimal("r21_carrying_amount"));
			obj.setR21_no_of_accts(rs.getBigDecimal("r21_no_of_accts"));

			obj.setR22_rene_loans(rs.getString("r22_rene_loans"));
			obj.setR22_collateral_amount(rs.getBigDecimal("r22_collateral_amount"));
			obj.setR22_carrying_amount(rs.getBigDecimal("r22_carrying_amount"));
			obj.setR22_no_of_accts(rs.getBigDecimal("r22_no_of_accts"));

			obj.setR23_rene_loans(rs.getString("r23_rene_loans"));
			obj.setR23_collateral_amount(rs.getBigDecimal("r23_collateral_amount"));
			obj.setR23_carrying_amount(rs.getBigDecimal("r23_carrying_amount"));
			obj.setR23_no_of_accts(rs.getBigDecimal("r23_no_of_accts"));

			obj.setR24_rene_loans(rs.getString("r24_rene_loans"));
			obj.setR24_collateral_amount(rs.getBigDecimal("r24_collateral_amount"));
			obj.setR24_carrying_amount(rs.getBigDecimal("r24_carrying_amount"));
			obj.setR24_no_of_accts(rs.getBigDecimal("r24_no_of_accts"));

			obj.setR25_rene_loans(rs.getString("r25_rene_loans"));
			obj.setR25_collateral_amount(rs.getBigDecimal("r25_collateral_amount"));
			obj.setR25_carrying_amount(rs.getBigDecimal("r25_carrying_amount"));
			obj.setR25_no_of_accts(rs.getBigDecimal("r25_no_of_accts"));

			obj.setR26_rene_loans(rs.getString("r26_rene_loans"));
			obj.setR26_collateral_amount(rs.getBigDecimal("r26_collateral_amount"));
			obj.setR26_carrying_amount(rs.getBigDecimal("r26_carrying_amount"));
			obj.setR26_no_of_accts(rs.getBigDecimal("r26_no_of_accts"));

			obj.setR27_rene_loans(rs.getString("r27_rene_loans"));
			obj.setR27_collateral_amount(rs.getBigDecimal("r27_collateral_amount"));
			obj.setR27_carrying_amount(rs.getBigDecimal("r27_carrying_amount"));
			obj.setR27_no_of_accts(rs.getBigDecimal("r27_no_of_accts"));

			obj.setR28_rene_loans(rs.getString("r28_rene_loans"));
			obj.setR28_collateral_amount(rs.getBigDecimal("r28_collateral_amount"));
			obj.setR28_carrying_amount(rs.getBigDecimal("r28_carrying_amount"));
			obj.setR28_no_of_accts(rs.getBigDecimal("r28_no_of_accts"));

			obj.setR29_rene_loans(rs.getString("r29_rene_loans"));
			obj.setR29_collateral_amount(rs.getBigDecimal("r29_collateral_amount"));
			obj.setR29_carrying_amount(rs.getBigDecimal("r29_carrying_amount"));
			obj.setR29_no_of_accts(rs.getBigDecimal("r29_no_of_accts"));

			obj.setR30_rene_loans(rs.getString("r30_rene_loans"));
			obj.setR30_collateral_amount(rs.getBigDecimal("r30_collateral_amount"));
			obj.setR30_carrying_amount(rs.getBigDecimal("r30_carrying_amount"));
			obj.setR30_no_of_accts(rs.getBigDecimal("r30_no_of_accts"));

			obj.setR31_rene_loans(rs.getString("r31_rene_loans"));
			obj.setR31_collateral_amount(rs.getBigDecimal("r31_collateral_amount"));
			obj.setR31_carrying_amount(rs.getBigDecimal("r31_carrying_amount"));
			obj.setR31_no_of_accts(rs.getBigDecimal("r31_no_of_accts"));

			obj.setR32_rene_loans(rs.getString("r32_rene_loans"));
			obj.setR32_collateral_amount(rs.getBigDecimal("r32_collateral_amount"));
			obj.setR32_carrying_amount(rs.getBigDecimal("r32_carrying_amount"));
			obj.setR32_no_of_accts(rs.getBigDecimal("r32_no_of_accts"));

			obj.setR33_rene_loans(rs.getString("r33_rene_loans"));
			obj.setR33_collateral_amount(rs.getBigDecimal("r33_collateral_amount"));
			obj.setR33_carrying_amount(rs.getBigDecimal("r33_carrying_amount"));
			obj.setR33_no_of_accts(rs.getBigDecimal("r33_no_of_accts"));

			obj.setR34_rene_loans(rs.getString("r34_rene_loans"));
			obj.setR34_collateral_amount(rs.getBigDecimal("r34_collateral_amount"));
			obj.setR34_carrying_amount(rs.getBigDecimal("r34_carrying_amount"));
			obj.setR34_no_of_accts(rs.getBigDecimal("r34_no_of_accts"));

			obj.setR35_rene_loans(rs.getString("r35_rene_loans"));
			obj.setR35_collateral_amount(rs.getBigDecimal("r35_collateral_amount"));
			obj.setR35_carrying_amount(rs.getBigDecimal("r35_carrying_amount"));
			obj.setR35_no_of_accts(rs.getBigDecimal("r35_no_of_accts"));

			obj.setR36_rene_loans(rs.getString("r36_rene_loans"));
			obj.setR36_collateral_amount(rs.getBigDecimal("r36_collateral_amount"));
			obj.setR36_carrying_amount(rs.getBigDecimal("r36_carrying_amount"));
			obj.setR36_no_of_accts(rs.getBigDecimal("r36_no_of_accts"));

			obj.setR37_rene_loans(rs.getString("r37_rene_loans"));
			obj.setR37_collateral_amount(rs.getBigDecimal("r37_collateral_amount"));
			obj.setR37_carrying_amount(rs.getBigDecimal("r37_carrying_amount"));
			obj.setR37_no_of_accts(rs.getBigDecimal("r37_no_of_accts"));

			obj.setR38_rene_loans(rs.getString("r38_rene_loans"));
			obj.setR38_collateral_amount(rs.getBigDecimal("r38_collateral_amount"));
			obj.setR38_carrying_amount(rs.getBigDecimal("r38_carrying_amount"));
			obj.setR38_no_of_accts(rs.getBigDecimal("r38_no_of_accts"));

			obj.setR39_rene_loans(rs.getString("r39_rene_loans"));
			obj.setR39_collateral_amount(rs.getBigDecimal("r39_collateral_amount"));
			obj.setR39_carrying_amount(rs.getBigDecimal("r39_carrying_amount"));
			obj.setR39_no_of_accts(rs.getBigDecimal("r39_no_of_accts"));

			obj.setR40_rene_loans(rs.getString("r40_rene_loans"));
			obj.setR40_collateral_amount(rs.getBigDecimal("r40_collateral_amount"));
			obj.setR40_carrying_amount(rs.getBigDecimal("r40_carrying_amount"));
			obj.setR40_no_of_accts(rs.getBigDecimal("r40_no_of_accts"));

			obj.setR41_rene_loans(rs.getString("r41_rene_loans"));
			obj.setR41_collateral_amount(rs.getBigDecimal("r41_collateral_amount"));
			obj.setR41_carrying_amount(rs.getBigDecimal("r41_carrying_amount"));
			obj.setR41_no_of_accts(rs.getBigDecimal("r41_no_of_accts"));

			obj.setR42_rene_loans(rs.getString("r42_rene_loans"));
			obj.setR42_collateral_amount(rs.getBigDecimal("r42_collateral_amount"));
			obj.setR42_carrying_amount(rs.getBigDecimal("r42_carrying_amount"));
			obj.setR42_no_of_accts(rs.getBigDecimal("r42_no_of_accts"));

			obj.setR43_rene_loans(rs.getString("r43_rene_loans"));
			obj.setR43_collateral_amount(rs.getBigDecimal("r43_collateral_amount"));
			obj.setR43_carrying_amount(rs.getBigDecimal("r43_carrying_amount"));
			obj.setR43_no_of_accts(rs.getBigDecimal("r43_no_of_accts"));

			obj.setR44_rene_loans(rs.getString("r44_rene_loans"));
			obj.setR44_collateral_amount(rs.getBigDecimal("r44_collateral_amount"));
			obj.setR44_carrying_amount(rs.getBigDecimal("r44_carrying_amount"));
			obj.setR44_no_of_accts(rs.getBigDecimal("r44_no_of_accts"));

			obj.setR45_rene_loans(rs.getString("r45_rene_loans"));
			obj.setR45_collateral_amount(rs.getBigDecimal("r45_collateral_amount"));
			obj.setR45_carrying_amount(rs.getBigDecimal("r45_carrying_amount"));
			obj.setR45_no_of_accts(rs.getBigDecimal("r45_no_of_accts"));

			obj.setR46_rene_loans(rs.getString("r46_rene_loans"));
			obj.setR46_collateral_amount(rs.getBigDecimal("r46_collateral_amount"));
			obj.setR46_carrying_amount(rs.getBigDecimal("r46_carrying_amount"));
			obj.setR46_no_of_accts(rs.getBigDecimal("r46_no_of_accts"));

			obj.setR47_rene_loans(rs.getString("r47_rene_loans"));
			obj.setR47_collateral_amount(rs.getBigDecimal("r47_collateral_amount"));
			obj.setR47_carrying_amount(rs.getBigDecimal("r47_carrying_amount"));
			obj.setR47_no_of_accts(rs.getBigDecimal("r47_no_of_accts"));

			obj.setR48_rene_loans(rs.getString("r48_rene_loans"));
			obj.setR48_collateral_amount(rs.getBigDecimal("r48_collateral_amount"));
			obj.setR48_carrying_amount(rs.getBigDecimal("r48_carrying_amount"));
			obj.setR48_no_of_accts(rs.getBigDecimal("r48_no_of_accts"));

			obj.setR49_rene_loans(rs.getString("r49_rene_loans"));
			obj.setR49_collateral_amount(rs.getBigDecimal("r49_collateral_amount"));
			obj.setR49_carrying_amount(rs.getBigDecimal("r49_carrying_amount"));
			obj.setR49_no_of_accts(rs.getBigDecimal("r49_no_of_accts"));

			obj.setR50_rene_loans(rs.getString("r50_rene_loans"));
			obj.setR50_collateral_amount(rs.getBigDecimal("r50_collateral_amount"));
			obj.setR50_carrying_amount(rs.getBigDecimal("r50_carrying_amount"));
			obj.setR50_no_of_accts(rs.getBigDecimal("r50_no_of_accts"));

			obj.setR51_rene_loans(rs.getString("r51_rene_loans"));
			obj.setR51_collateral_amount(rs.getBigDecimal("r51_collateral_amount"));
			obj.setR51_carrying_amount(rs.getBigDecimal("r51_carrying_amount"));
			obj.setR51_no_of_accts(rs.getBigDecimal("r51_no_of_accts"));

			obj.setR52_rene_loans(rs.getString("r52_rene_loans"));
			obj.setR52_collateral_amount(rs.getBigDecimal("r52_collateral_amount"));
			obj.setR52_carrying_amount(rs.getBigDecimal("r52_carrying_amount"));
			obj.setR52_no_of_accts(rs.getBigDecimal("r52_no_of_accts"));

			obj.setR53_rene_loans(rs.getString("r53_rene_loans"));
			obj.setR53_collateral_amount(rs.getBigDecimal("r53_collateral_amount"));
			obj.setR53_carrying_amount(rs.getBigDecimal("r53_carrying_amount"));
			obj.setR53_no_of_accts(rs.getBigDecimal("r53_no_of_accts"));

			obj.setR54_rene_loans(rs.getString("r54_rene_loans"));
			obj.setR54_collateral_amount(rs.getBigDecimal("r54_collateral_amount"));
			obj.setR54_carrying_amount(rs.getBigDecimal("r54_carrying_amount"));
			obj.setR54_no_of_accts(rs.getBigDecimal("r54_no_of_accts"));

			obj.setR55_rene_loans(rs.getString("r55_rene_loans"));
			obj.setR55_collateral_amount(rs.getBigDecimal("r55_collateral_amount"));
			obj.setR55_carrying_amount(rs.getBigDecimal("r55_carrying_amount"));
			obj.setR55_no_of_accts(rs.getBigDecimal("r55_no_of_accts"));

			obj.setR56_rene_loans(rs.getString("r56_rene_loans"));
			obj.setR56_collateral_amount(rs.getBigDecimal("r56_collateral_amount"));
			obj.setR56_carrying_amount(rs.getBigDecimal("r56_carrying_amount"));
			obj.setR56_no_of_accts(rs.getBigDecimal("r56_no_of_accts"));

			obj.setR57_rene_loans(rs.getString("r57_rene_loans"));
			obj.setR57_collateral_amount(rs.getBigDecimal("r57_collateral_amount"));
			obj.setR57_carrying_amount(rs.getBigDecimal("r57_carrying_amount"));
			obj.setR57_no_of_accts(rs.getBigDecimal("r57_no_of_accts"));

			obj.setR58_rene_loans(rs.getString("r58_rene_loans"));
			obj.setR58_collateral_amount(rs.getBigDecimal("r58_collateral_amount"));
			obj.setR58_carrying_amount(rs.getBigDecimal("r58_carrying_amount"));
			obj.setR58_no_of_accts(rs.getBigDecimal("r58_no_of_accts"));

			obj.setR59_rene_loans(rs.getString("r59_rene_loans"));
			obj.setR59_collateral_amount(rs.getBigDecimal("r59_collateral_amount"));
			obj.setR59_carrying_amount(rs.getBigDecimal("r59_carrying_amount"));
			obj.setR59_no_of_accts(rs.getBigDecimal("r59_no_of_accts"));

			obj.setR60_rene_loans(rs.getString("r60_rene_loans"));
			obj.setR60_collateral_amount(rs.getBigDecimal("r60_collateral_amount"));
			obj.setR60_carrying_amount(rs.getBigDecimal("r60_carrying_amount"));
			obj.setR60_no_of_accts(rs.getBigDecimal("r60_no_of_accts"));

			obj.setR61_rene_loans(rs.getString("r61_rene_loans"));
			obj.setR61_collateral_amount(rs.getBigDecimal("r61_collateral_amount"));
			obj.setR61_carrying_amount(rs.getBigDecimal("r61_carrying_amount"));
			obj.setR61_no_of_accts(rs.getBigDecimal("r61_no_of_accts"));

			obj.setR62_rene_loans(rs.getString("r62_rene_loans"));
			obj.setR62_collateral_amount(rs.getBigDecimal("r62_collateral_amount"));
			obj.setR62_carrying_amount(rs.getBigDecimal("r62_carrying_amount"));
			obj.setR62_no_of_accts(rs.getBigDecimal("r62_no_of_accts"));

			obj.setR63_rene_loans(rs.getString("r63_rene_loans"));
			obj.setR63_collateral_amount(rs.getBigDecimal("r63_collateral_amount"));
			obj.setR63_carrying_amount(rs.getBigDecimal("r63_carrying_amount"));
			obj.setR63_no_of_accts(rs.getBigDecimal("r63_no_of_accts"));

// Special columns
			obj.setR27_new_column_rene_loans(rs.getString("r27_new_column_rene_loans"));
			obj.setR27_new_column_collateral_amount(rs.getBigDecimal("r27_new_column_collateral_amount"));
			obj.setR27_new_column_carrying_amount(rs.getBigDecimal("r27_new_column_carrying_amount"));
			obj.setR27_new_column_no_of_accts(rs.getBigDecimal("r27_new_column_no_of_accts"));

			obj.setR42_new_column_rene_loans(rs.getString("r42_new_column_rene_loans"));
			obj.setR42_new_column_collateral_amount(rs.getBigDecimal("r42_new_column_collateral_amount"));
			obj.setR42_new_column_carrying_amount(rs.getBigDecimal("r42_new_column_carrying_amount"));
			obj.setR42_new_column_no_of_accts(rs.getBigDecimal("r42_new_column_no_of_accts"));

			obj.setR48_new_column_rene_loans(rs.getString("r48_new_column_rene_loans"));
			obj.setR48_new_column_collateral_amount(rs.getBigDecimal("r48_new_column_collateral_amount"));
			obj.setR48_new_column_carrying_amount(rs.getBigDecimal("r48_new_column_carrying_amount"));
			obj.setR48_new_column_no_of_accts(rs.getBigDecimal("r48_new_column_no_of_accts"));

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

	public class Q_RLFA1_Resub_Detail_Entity {
		private String r10_rene_loans;
		private BigDecimal r10_collateral_amount;
		private BigDecimal r10_carrying_amount;
		private BigDecimal r10_no_of_accts;
		private String r11_rene_loans;
		private BigDecimal r11_collateral_amount;
		private BigDecimal r11_carrying_amount;
		private BigDecimal r11_no_of_accts;
		private String r12_rene_loans;
		private BigDecimal r12_collateral_amount;
		private BigDecimal r12_carrying_amount;
		private BigDecimal r12_no_of_accts;
		private String r13_rene_loans;
		private BigDecimal r13_collateral_amount;
		private BigDecimal r13_carrying_amount;
		private BigDecimal r13_no_of_accts;
		private String r14_rene_loans;
		private BigDecimal r14_collateral_amount;
		private BigDecimal r14_carrying_amount;
		private BigDecimal r14_no_of_accts;
		private String r15_rene_loans;
		private BigDecimal r15_collateral_amount;
		private BigDecimal r15_carrying_amount;
		private BigDecimal r15_no_of_accts;
		private String r16_rene_loans;
		private BigDecimal r16_collateral_amount;
		private BigDecimal r16_carrying_amount;
		private BigDecimal r16_no_of_accts;
		private String r17_rene_loans;
		private BigDecimal r17_collateral_amount;
		private BigDecimal r17_carrying_amount;
		private BigDecimal r17_no_of_accts;
		private String r18_rene_loans;
		private BigDecimal r18_collateral_amount;
		private BigDecimal r18_carrying_amount;
		private BigDecimal r18_no_of_accts;
		private String r19_rene_loans;
		private BigDecimal r19_collateral_amount;
		private BigDecimal r19_carrying_amount;
		private BigDecimal r19_no_of_accts;
		private String r20_rene_loans;
		private BigDecimal r20_collateral_amount;
		private BigDecimal r20_carrying_amount;
		private BigDecimal r20_no_of_accts;
		private String r21_rene_loans;
		private BigDecimal r21_collateral_amount;
		private BigDecimal r21_carrying_amount;
		private BigDecimal r21_no_of_accts;
		private String r22_rene_loans;
		private BigDecimal r22_collateral_amount;
		private BigDecimal r22_carrying_amount;
		private BigDecimal r22_no_of_accts;
		private String r23_rene_loans;
		private BigDecimal r23_collateral_amount;
		private BigDecimal r23_carrying_amount;
		private BigDecimal r23_no_of_accts;
		private String r24_rene_loans;
		private BigDecimal r24_collateral_amount;
		private BigDecimal r24_carrying_amount;
		private BigDecimal r24_no_of_accts;
		private String r25_rene_loans;
		private BigDecimal r25_collateral_amount;
		private BigDecimal r25_carrying_amount;
		private BigDecimal r25_no_of_accts;
		private String r26_rene_loans;
		private BigDecimal r26_collateral_amount;
		private BigDecimal r26_carrying_amount;
		private BigDecimal r26_no_of_accts;
		private String r27_rene_loans;
		private BigDecimal r27_collateral_amount;
		private BigDecimal r27_carrying_amount;
		private BigDecimal r27_no_of_accts;
		private String r28_rene_loans;
		private BigDecimal r28_collateral_amount;
		private BigDecimal r28_carrying_amount;
		private BigDecimal r28_no_of_accts;
		private String r29_rene_loans;
		private BigDecimal r29_collateral_amount;
		private BigDecimal r29_carrying_amount;
		private BigDecimal r29_no_of_accts;
		private String r30_rene_loans;
		private BigDecimal r30_collateral_amount;
		private BigDecimal r30_carrying_amount;
		private BigDecimal r30_no_of_accts;
		private String r31_rene_loans;
		private BigDecimal r31_collateral_amount;
		private BigDecimal r31_carrying_amount;
		private BigDecimal r31_no_of_accts;
		private String r32_rene_loans;
		private BigDecimal r32_collateral_amount;
		private BigDecimal r32_carrying_amount;
		private BigDecimal r32_no_of_accts;
		private String r33_rene_loans;
		private BigDecimal r33_collateral_amount;
		private BigDecimal r33_carrying_amount;
		private BigDecimal r33_no_of_accts;
		private String r34_rene_loans;
		private BigDecimal r34_collateral_amount;
		private BigDecimal r34_carrying_amount;
		private BigDecimal r34_no_of_accts;
		private String r35_rene_loans;
		private BigDecimal r35_collateral_amount;
		private BigDecimal r35_carrying_amount;
		private BigDecimal r35_no_of_accts;
		private String r36_rene_loans;
		private BigDecimal r36_collateral_amount;
		private BigDecimal r36_carrying_amount;
		private BigDecimal r36_no_of_accts;
		private String r37_rene_loans;
		private BigDecimal r37_collateral_amount;
		private BigDecimal r37_carrying_amount;
		private BigDecimal r37_no_of_accts;
		private String r38_rene_loans;
		private BigDecimal r38_collateral_amount;
		private BigDecimal r38_carrying_amount;
		private BigDecimal r38_no_of_accts;
		private String r39_rene_loans;
		private BigDecimal r39_collateral_amount;
		private BigDecimal r39_carrying_amount;
		private BigDecimal r39_no_of_accts;
		private String r40_rene_loans;
		private BigDecimal r40_collateral_amount;
		private BigDecimal r40_carrying_amount;
		private BigDecimal r40_no_of_accts;
		private String r41_rene_loans;
		private BigDecimal r41_collateral_amount;
		private BigDecimal r41_carrying_amount;
		private BigDecimal r41_no_of_accts;
		private String r42_rene_loans;
		private BigDecimal r42_collateral_amount;
		private BigDecimal r42_carrying_amount;
		private BigDecimal r42_no_of_accts;
		private String r43_rene_loans;
		private BigDecimal r43_collateral_amount;
		private BigDecimal r43_carrying_amount;
		private BigDecimal r43_no_of_accts;
		private String r44_rene_loans;
		private BigDecimal r44_collateral_amount;
		private BigDecimal r44_carrying_amount;
		private BigDecimal r44_no_of_accts;
		private String r45_rene_loans;
		private BigDecimal r45_collateral_amount;
		private BigDecimal r45_carrying_amount;
		private BigDecimal r45_no_of_accts;
		private String r46_rene_loans;
		private BigDecimal r46_collateral_amount;
		private BigDecimal r46_carrying_amount;
		private BigDecimal r46_no_of_accts;
		private String r47_rene_loans;
		private BigDecimal r47_collateral_amount;
		private BigDecimal r47_carrying_amount;
		private BigDecimal r47_no_of_accts;
		private String r48_rene_loans;
		private BigDecimal r48_collateral_amount;
		private BigDecimal r48_carrying_amount;
		private BigDecimal r48_no_of_accts;
		private String r49_rene_loans;
		private BigDecimal r49_collateral_amount;
		private BigDecimal r49_carrying_amount;
		private BigDecimal r49_no_of_accts;
		private String r50_rene_loans;
		private BigDecimal r50_collateral_amount;
		private BigDecimal r50_carrying_amount;
		private BigDecimal r50_no_of_accts;
		private String r51_rene_loans;
		private BigDecimal r51_collateral_amount;
		private BigDecimal r51_carrying_amount;
		private BigDecimal r51_no_of_accts;
		private String r52_rene_loans;
		private BigDecimal r52_collateral_amount;
		private BigDecimal r52_carrying_amount;
		private BigDecimal r52_no_of_accts;
		private String r53_rene_loans;
		private BigDecimal r53_collateral_amount;
		private BigDecimal r53_carrying_amount;
		private BigDecimal r53_no_of_accts;
		private String r54_rene_loans;
		private BigDecimal r54_collateral_amount;
		private BigDecimal r54_carrying_amount;
		private BigDecimal r54_no_of_accts;
		private String r55_rene_loans;
		private BigDecimal r55_collateral_amount;
		private BigDecimal r55_carrying_amount;
		private BigDecimal r55_no_of_accts;
		private String r56_rene_loans;
		private BigDecimal r56_collateral_amount;
		private BigDecimal r56_carrying_amount;
		private BigDecimal r56_no_of_accts;
		private String r57_rene_loans;
		private BigDecimal r57_collateral_amount;
		private BigDecimal r57_carrying_amount;
		private BigDecimal r57_no_of_accts;
		private String r58_rene_loans;
		private BigDecimal r58_collateral_amount;
		private BigDecimal r58_carrying_amount;
		private BigDecimal r58_no_of_accts;
		private String r59_rene_loans;
		private BigDecimal r59_collateral_amount;
		private BigDecimal r59_carrying_amount;
		private BigDecimal r59_no_of_accts;
		private String r60_rene_loans;
		private BigDecimal r60_collateral_amount;
		private BigDecimal r60_carrying_amount;
		private BigDecimal r60_no_of_accts;
		private String r61_rene_loans;
		private BigDecimal r61_collateral_amount;
		private BigDecimal r61_carrying_amount;
		private BigDecimal r61_no_of_accts;
		private String r62_rene_loans;
		private BigDecimal r62_collateral_amount;
		private BigDecimal r62_carrying_amount;
		private BigDecimal r62_no_of_accts;
		private String r63_rene_loans;
		private BigDecimal r63_collateral_amount;
		private BigDecimal r63_carrying_amount;
		private BigDecimal r63_no_of_accts;

		private String r27_new_column_rene_loans;
		private BigDecimal r27_new_column_collateral_amount;
		private BigDecimal r27_new_column_carrying_amount;
		private BigDecimal r27_new_column_no_of_accts;

		private String r42_new_column_rene_loans;
		private BigDecimal r42_new_column_collateral_amount;
		private BigDecimal r42_new_column_carrying_amount;
		private BigDecimal r42_new_column_no_of_accts;

		private String r48_new_column_rene_loans;
		private BigDecimal r48_new_column_collateral_amount;
		private BigDecimal r48_new_column_carrying_amount;
		private BigDecimal r48_new_column_no_of_accts;

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

		public String getR27_new_column_rene_loans() {
			return r27_new_column_rene_loans;
		}

		public BigDecimal getR27_new_column_collateral_amount() {
			return r27_new_column_collateral_amount;
		}

		public BigDecimal getR27_new_column_carrying_amount() {
			return r27_new_column_carrying_amount;
		}

		public BigDecimal getR27_new_column_no_of_accts() {
			return r27_new_column_no_of_accts;
		}

		public String getR42_new_column_rene_loans() {
			return r42_new_column_rene_loans;
		}

		public BigDecimal getR42_new_column_collateral_amount() {
			return r42_new_column_collateral_amount;
		}

		public BigDecimal getR42_new_column_carrying_amount() {
			return r42_new_column_carrying_amount;
		}

		public BigDecimal getR42_new_column_no_of_accts() {
			return r42_new_column_no_of_accts;
		}

		public String getR48_new_column_rene_loans() {
			return r48_new_column_rene_loans;
		}

		public BigDecimal getR48_new_column_collateral_amount() {
			return r48_new_column_collateral_amount;
		}

		public BigDecimal getR48_new_column_carrying_amount() {
			return r48_new_column_carrying_amount;
		}

		public BigDecimal getR48_new_column_no_of_accts() {
			return r48_new_column_no_of_accts;
		}

		public void setR27_new_column_rene_loans(String r27_new_column_rene_loans) {
			this.r27_new_column_rene_loans = r27_new_column_rene_loans;
		}

		public void setR27_new_column_collateral_amount(BigDecimal r27_new_column_collateral_amount) {
			this.r27_new_column_collateral_amount = r27_new_column_collateral_amount;
		}

		public void setR27_new_column_carrying_amount(BigDecimal r27_new_column_carrying_amount) {
			this.r27_new_column_carrying_amount = r27_new_column_carrying_amount;
		}

		public void setR27_new_column_no_of_accts(BigDecimal r27_new_column_no_of_accts) {
			this.r27_new_column_no_of_accts = r27_new_column_no_of_accts;
		}

		public void setR42_new_column_rene_loans(String r42_new_column_rene_loans) {
			this.r42_new_column_rene_loans = r42_new_column_rene_loans;
		}

		public void setR42_new_column_collateral_amount(BigDecimal r42_new_column_collateral_amount) {
			this.r42_new_column_collateral_amount = r42_new_column_collateral_amount;
		}

		public void setR42_new_column_carrying_amount(BigDecimal r42_new_column_carrying_amount) {
			this.r42_new_column_carrying_amount = r42_new_column_carrying_amount;
		}

		public void setR42_new_column_no_of_accts(BigDecimal r42_new_column_no_of_accts) {
			this.r42_new_column_no_of_accts = r42_new_column_no_of_accts;
		}

		public void setR48_new_column_rene_loans(String r48_new_column_rene_loans) {
			this.r48_new_column_rene_loans = r48_new_column_rene_loans;
		}

		public void setR48_new_column_collateral_amount(BigDecimal r48_new_column_collateral_amount) {
			this.r48_new_column_collateral_amount = r48_new_column_collateral_amount;
		}

		public void setR48_new_column_carrying_amount(BigDecimal r48_new_column_carrying_amount) {
			this.r48_new_column_carrying_amount = r48_new_column_carrying_amount;
		}

		public void setR48_new_column_no_of_accts(BigDecimal r48_new_column_no_of_accts) {
			this.r48_new_column_no_of_accts = r48_new_column_no_of_accts;
		}

		public String getR10_rene_loans() {
			return r10_rene_loans;
		}

		public void setR10_rene_loans(String r10_rene_loans) {
			this.r10_rene_loans = r10_rene_loans;
		}

		public BigDecimal getR10_collateral_amount() {
			return r10_collateral_amount;
		}

		public void setR10_collateral_amount(BigDecimal r10_collateral_amount) {
			this.r10_collateral_amount = r10_collateral_amount;
		}

		public BigDecimal getR10_carrying_amount() {
			return r10_carrying_amount;
		}

		public void setR10_carrying_amount(BigDecimal r10_carrying_amount) {
			this.r10_carrying_amount = r10_carrying_amount;
		}

		public BigDecimal getR10_no_of_accts() {
			return r10_no_of_accts;
		}

		public void setR10_no_of_accts(BigDecimal r10_no_of_accts) {
			this.r10_no_of_accts = r10_no_of_accts;
		}

		public String getR11_rene_loans() {
			return r11_rene_loans;
		}

		public void setR11_rene_loans(String r11_rene_loans) {
			this.r11_rene_loans = r11_rene_loans;
		}

		public BigDecimal getR11_collateral_amount() {
			return r11_collateral_amount;
		}

		public void setR11_collateral_amount(BigDecimal r11_collateral_amount) {
			this.r11_collateral_amount = r11_collateral_amount;
		}

		public BigDecimal getR11_carrying_amount() {
			return r11_carrying_amount;
		}

		public void setR11_carrying_amount(BigDecimal r11_carrying_amount) {
			this.r11_carrying_amount = r11_carrying_amount;
		}

		public BigDecimal getR11_no_of_accts() {
			return r11_no_of_accts;
		}

		public void setR11_no_of_accts(BigDecimal r11_no_of_accts) {
			this.r11_no_of_accts = r11_no_of_accts;
		}

		public String getR12_rene_loans() {
			return r12_rene_loans;
		}

		public void setR12_rene_loans(String r12_rene_loans) {
			this.r12_rene_loans = r12_rene_loans;
		}

		public BigDecimal getR12_collateral_amount() {
			return r12_collateral_amount;
		}

		public void setR12_collateral_amount(BigDecimal r12_collateral_amount) {
			this.r12_collateral_amount = r12_collateral_amount;
		}

		public BigDecimal getR12_carrying_amount() {
			return r12_carrying_amount;
		}

		public void setR12_carrying_amount(BigDecimal r12_carrying_amount) {
			this.r12_carrying_amount = r12_carrying_amount;
		}

		public BigDecimal getR12_no_of_accts() {
			return r12_no_of_accts;
		}

		public void setR12_no_of_accts(BigDecimal r12_no_of_accts) {
			this.r12_no_of_accts = r12_no_of_accts;
		}

		public String getR13_rene_loans() {
			return r13_rene_loans;
		}

		public void setR13_rene_loans(String r13_rene_loans) {
			this.r13_rene_loans = r13_rene_loans;
		}

		public BigDecimal getR13_collateral_amount() {
			return r13_collateral_amount;
		}

		public void setR13_collateral_amount(BigDecimal r13_collateral_amount) {
			this.r13_collateral_amount = r13_collateral_amount;
		}

		public BigDecimal getR13_carrying_amount() {
			return r13_carrying_amount;
		}

		public void setR13_carrying_amount(BigDecimal r13_carrying_amount) {
			this.r13_carrying_amount = r13_carrying_amount;
		}

		public BigDecimal getR13_no_of_accts() {
			return r13_no_of_accts;
		}

		public void setR13_no_of_accts(BigDecimal r13_no_of_accts) {
			this.r13_no_of_accts = r13_no_of_accts;
		}

		public String getR14_rene_loans() {
			return r14_rene_loans;
		}

		public void setR14_rene_loans(String r14_rene_loans) {
			this.r14_rene_loans = r14_rene_loans;
		}

		public BigDecimal getR14_collateral_amount() {
			return r14_collateral_amount;
		}

		public void setR14_collateral_amount(BigDecimal r14_collateral_amount) {
			this.r14_collateral_amount = r14_collateral_amount;
		}

		public BigDecimal getR14_carrying_amount() {
			return r14_carrying_amount;
		}

		public void setR14_carrying_amount(BigDecimal r14_carrying_amount) {
			this.r14_carrying_amount = r14_carrying_amount;
		}

		public BigDecimal getR14_no_of_accts() {
			return r14_no_of_accts;
		}

		public void setR14_no_of_accts(BigDecimal r14_no_of_accts) {
			this.r14_no_of_accts = r14_no_of_accts;
		}

		public String getR15_rene_loans() {
			return r15_rene_loans;
		}

		public void setR15_rene_loans(String r15_rene_loans) {
			this.r15_rene_loans = r15_rene_loans;
		}

		public BigDecimal getR15_collateral_amount() {
			return r15_collateral_amount;
		}

		public void setR15_collateral_amount(BigDecimal r15_collateral_amount) {
			this.r15_collateral_amount = r15_collateral_amount;
		}

		public BigDecimal getR15_carrying_amount() {
			return r15_carrying_amount;
		}

		public void setR15_carrying_amount(BigDecimal r15_carrying_amount) {
			this.r15_carrying_amount = r15_carrying_amount;
		}

		public BigDecimal getR15_no_of_accts() {
			return r15_no_of_accts;
		}

		public void setR15_no_of_accts(BigDecimal r15_no_of_accts) {
			this.r15_no_of_accts = r15_no_of_accts;
		}

		public String getR16_rene_loans() {
			return r16_rene_loans;
		}

		public void setR16_rene_loans(String r16_rene_loans) {
			this.r16_rene_loans = r16_rene_loans;
		}

		public BigDecimal getR16_collateral_amount() {
			return r16_collateral_amount;
		}

		public void setR16_collateral_amount(BigDecimal r16_collateral_amount) {
			this.r16_collateral_amount = r16_collateral_amount;
		}

		public BigDecimal getR16_carrying_amount() {
			return r16_carrying_amount;
		}

		public void setR16_carrying_amount(BigDecimal r16_carrying_amount) {
			this.r16_carrying_amount = r16_carrying_amount;
		}

		public BigDecimal getR16_no_of_accts() {
			return r16_no_of_accts;
		}

		public void setR16_no_of_accts(BigDecimal r16_no_of_accts) {
			this.r16_no_of_accts = r16_no_of_accts;
		}

		public String getR17_rene_loans() {
			return r17_rene_loans;
		}

		public void setR17_rene_loans(String r17_rene_loans) {
			this.r17_rene_loans = r17_rene_loans;
		}

		public BigDecimal getR17_collateral_amount() {
			return r17_collateral_amount;
		}

		public void setR17_collateral_amount(BigDecimal r17_collateral_amount) {
			this.r17_collateral_amount = r17_collateral_amount;
		}

		public BigDecimal getR17_carrying_amount() {
			return r17_carrying_amount;
		}

		public void setR17_carrying_amount(BigDecimal r17_carrying_amount) {
			this.r17_carrying_amount = r17_carrying_amount;
		}

		public BigDecimal getR17_no_of_accts() {
			return r17_no_of_accts;
		}

		public void setR17_no_of_accts(BigDecimal r17_no_of_accts) {
			this.r17_no_of_accts = r17_no_of_accts;
		}

		public String getR18_rene_loans() {
			return r18_rene_loans;
		}

		public void setR18_rene_loans(String r18_rene_loans) {
			this.r18_rene_loans = r18_rene_loans;
		}

		public BigDecimal getR18_collateral_amount() {
			return r18_collateral_amount;
		}

		public void setR18_collateral_amount(BigDecimal r18_collateral_amount) {
			this.r18_collateral_amount = r18_collateral_amount;
		}

		public BigDecimal getR18_carrying_amount() {
			return r18_carrying_amount;
		}

		public void setR18_carrying_amount(BigDecimal r18_carrying_amount) {
			this.r18_carrying_amount = r18_carrying_amount;
		}

		public BigDecimal getR18_no_of_accts() {
			return r18_no_of_accts;
		}

		public void setR18_no_of_accts(BigDecimal r18_no_of_accts) {
			this.r18_no_of_accts = r18_no_of_accts;
		}

		public String getR19_rene_loans() {
			return r19_rene_loans;
		}

		public void setR19_rene_loans(String r19_rene_loans) {
			this.r19_rene_loans = r19_rene_loans;
		}

		public BigDecimal getR19_collateral_amount() {
			return r19_collateral_amount;
		}

		public void setR19_collateral_amount(BigDecimal r19_collateral_amount) {
			this.r19_collateral_amount = r19_collateral_amount;
		}

		public BigDecimal getR19_carrying_amount() {
			return r19_carrying_amount;
		}

		public void setR19_carrying_amount(BigDecimal r19_carrying_amount) {
			this.r19_carrying_amount = r19_carrying_amount;
		}

		public BigDecimal getR19_no_of_accts() {
			return r19_no_of_accts;
		}

		public void setR19_no_of_accts(BigDecimal r19_no_of_accts) {
			this.r19_no_of_accts = r19_no_of_accts;
		}

		public String getR20_rene_loans() {
			return r20_rene_loans;
		}

		public void setR20_rene_loans(String r20_rene_loans) {
			this.r20_rene_loans = r20_rene_loans;
		}

		public BigDecimal getR20_collateral_amount() {
			return r20_collateral_amount;
		}

		public void setR20_collateral_amount(BigDecimal r20_collateral_amount) {
			this.r20_collateral_amount = r20_collateral_amount;
		}

		public BigDecimal getR20_carrying_amount() {
			return r20_carrying_amount;
		}

		public void setR20_carrying_amount(BigDecimal r20_carrying_amount) {
			this.r20_carrying_amount = r20_carrying_amount;
		}

		public BigDecimal getR20_no_of_accts() {
			return r20_no_of_accts;
		}

		public void setR20_no_of_accts(BigDecimal r20_no_of_accts) {
			this.r20_no_of_accts = r20_no_of_accts;
		}

		public String getR21_rene_loans() {
			return r21_rene_loans;
		}

		public void setR21_rene_loans(String r21_rene_loans) {
			this.r21_rene_loans = r21_rene_loans;
		}

		public BigDecimal getR21_collateral_amount() {
			return r21_collateral_amount;
		}

		public void setR21_collateral_amount(BigDecimal r21_collateral_amount) {
			this.r21_collateral_amount = r21_collateral_amount;
		}

		public BigDecimal getR21_carrying_amount() {
			return r21_carrying_amount;
		}

		public void setR21_carrying_amount(BigDecimal r21_carrying_amount) {
			this.r21_carrying_amount = r21_carrying_amount;
		}

		public BigDecimal getR21_no_of_accts() {
			return r21_no_of_accts;
		}

		public void setR21_no_of_accts(BigDecimal r21_no_of_accts) {
			this.r21_no_of_accts = r21_no_of_accts;
		}

		public String getR22_rene_loans() {
			return r22_rene_loans;
		}

		public void setR22_rene_loans(String r22_rene_loans) {
			this.r22_rene_loans = r22_rene_loans;
		}

		public BigDecimal getR22_collateral_amount() {
			return r22_collateral_amount;
		}

		public void setR22_collateral_amount(BigDecimal r22_collateral_amount) {
			this.r22_collateral_amount = r22_collateral_amount;
		}

		public BigDecimal getR22_carrying_amount() {
			return r22_carrying_amount;
		}

		public void setR22_carrying_amount(BigDecimal r22_carrying_amount) {
			this.r22_carrying_amount = r22_carrying_amount;
		}

		public BigDecimal getR22_no_of_accts() {
			return r22_no_of_accts;
		}

		public void setR22_no_of_accts(BigDecimal r22_no_of_accts) {
			this.r22_no_of_accts = r22_no_of_accts;
		}

		public String getR23_rene_loans() {
			return r23_rene_loans;
		}

		public void setR23_rene_loans(String r23_rene_loans) {
			this.r23_rene_loans = r23_rene_loans;
		}

		public BigDecimal getR23_collateral_amount() {
			return r23_collateral_amount;
		}

		public void setR23_collateral_amount(BigDecimal r23_collateral_amount) {
			this.r23_collateral_amount = r23_collateral_amount;
		}

		public BigDecimal getR23_carrying_amount() {
			return r23_carrying_amount;
		}

		public void setR23_carrying_amount(BigDecimal r23_carrying_amount) {
			this.r23_carrying_amount = r23_carrying_amount;
		}

		public BigDecimal getR23_no_of_accts() {
			return r23_no_of_accts;
		}

		public void setR23_no_of_accts(BigDecimal r23_no_of_accts) {
			this.r23_no_of_accts = r23_no_of_accts;
		}

		public String getR24_rene_loans() {
			return r24_rene_loans;
		}

		public void setR24_rene_loans(String r24_rene_loans) {
			this.r24_rene_loans = r24_rene_loans;
		}

		public BigDecimal getR24_collateral_amount() {
			return r24_collateral_amount;
		}

		public void setR24_collateral_amount(BigDecimal r24_collateral_amount) {
			this.r24_collateral_amount = r24_collateral_amount;
		}

		public BigDecimal getR24_carrying_amount() {
			return r24_carrying_amount;
		}

		public void setR24_carrying_amount(BigDecimal r24_carrying_amount) {
			this.r24_carrying_amount = r24_carrying_amount;
		}

		public BigDecimal getR24_no_of_accts() {
			return r24_no_of_accts;
		}

		public void setR24_no_of_accts(BigDecimal r24_no_of_accts) {
			this.r24_no_of_accts = r24_no_of_accts;
		}

		public String getR25_rene_loans() {
			return r25_rene_loans;
		}

		public void setR25_rene_loans(String r25_rene_loans) {
			this.r25_rene_loans = r25_rene_loans;
		}

		public BigDecimal getR25_collateral_amount() {
			return r25_collateral_amount;
		}

		public void setR25_collateral_amount(BigDecimal r25_collateral_amount) {
			this.r25_collateral_amount = r25_collateral_amount;
		}

		public BigDecimal getR25_carrying_amount() {
			return r25_carrying_amount;
		}

		public void setR25_carrying_amount(BigDecimal r25_carrying_amount) {
			this.r25_carrying_amount = r25_carrying_amount;
		}

		public BigDecimal getR25_no_of_accts() {
			return r25_no_of_accts;
		}

		public void setR25_no_of_accts(BigDecimal r25_no_of_accts) {
			this.r25_no_of_accts = r25_no_of_accts;
		}

		public String getR26_rene_loans() {
			return r26_rene_loans;
		}

		public void setR26_rene_loans(String r26_rene_loans) {
			this.r26_rene_loans = r26_rene_loans;
		}

		public BigDecimal getR26_collateral_amount() {
			return r26_collateral_amount;
		}

		public void setR26_collateral_amount(BigDecimal r26_collateral_amount) {
			this.r26_collateral_amount = r26_collateral_amount;
		}

		public BigDecimal getR26_carrying_amount() {
			return r26_carrying_amount;
		}

		public void setR26_carrying_amount(BigDecimal r26_carrying_amount) {
			this.r26_carrying_amount = r26_carrying_amount;
		}

		public BigDecimal getR26_no_of_accts() {
			return r26_no_of_accts;
		}

		public void setR26_no_of_accts(BigDecimal r26_no_of_accts) {
			this.r26_no_of_accts = r26_no_of_accts;
		}

		public String getR27_rene_loans() {
			return r27_rene_loans;
		}

		public void setR27_rene_loans(String r27_rene_loans) {
			this.r27_rene_loans = r27_rene_loans;
		}

		public BigDecimal getR27_collateral_amount() {
			return r27_collateral_amount;
		}

		public void setR27_collateral_amount(BigDecimal r27_collateral_amount) {
			this.r27_collateral_amount = r27_collateral_amount;
		}

		public BigDecimal getR27_carrying_amount() {
			return r27_carrying_amount;
		}

		public void setR27_carrying_amount(BigDecimal r27_carrying_amount) {
			this.r27_carrying_amount = r27_carrying_amount;
		}

		public BigDecimal getR27_no_of_accts() {
			return r27_no_of_accts;
		}

		public void setR27_no_of_accts(BigDecimal r27_no_of_accts) {
			this.r27_no_of_accts = r27_no_of_accts;
		}

		public String getR28_rene_loans() {
			return r28_rene_loans;
		}

		public void setR28_rene_loans(String r28_rene_loans) {
			this.r28_rene_loans = r28_rene_loans;
		}

		public BigDecimal getR28_collateral_amount() {
			return r28_collateral_amount;
		}

		public void setR28_collateral_amount(BigDecimal r28_collateral_amount) {
			this.r28_collateral_amount = r28_collateral_amount;
		}

		public BigDecimal getR28_carrying_amount() {
			return r28_carrying_amount;
		}

		public void setR28_carrying_amount(BigDecimal r28_carrying_amount) {
			this.r28_carrying_amount = r28_carrying_amount;
		}

		public BigDecimal getR28_no_of_accts() {
			return r28_no_of_accts;
		}

		public void setR28_no_of_accts(BigDecimal r28_no_of_accts) {
			this.r28_no_of_accts = r28_no_of_accts;
		}

		public String getR29_rene_loans() {
			return r29_rene_loans;
		}

		public void setR29_rene_loans(String r29_rene_loans) {
			this.r29_rene_loans = r29_rene_loans;
		}

		public BigDecimal getR29_collateral_amount() {
			return r29_collateral_amount;
		}

		public void setR29_collateral_amount(BigDecimal r29_collateral_amount) {
			this.r29_collateral_amount = r29_collateral_amount;
		}

		public BigDecimal getR29_carrying_amount() {
			return r29_carrying_amount;
		}

		public void setR29_carrying_amount(BigDecimal r29_carrying_amount) {
			this.r29_carrying_amount = r29_carrying_amount;
		}

		public BigDecimal getR29_no_of_accts() {
			return r29_no_of_accts;
		}

		public void setR29_no_of_accts(BigDecimal r29_no_of_accts) {
			this.r29_no_of_accts = r29_no_of_accts;
		}

		public String getR30_rene_loans() {
			return r30_rene_loans;
		}

		public void setR30_rene_loans(String r30_rene_loans) {
			this.r30_rene_loans = r30_rene_loans;
		}

		public BigDecimal getR30_collateral_amount() {
			return r30_collateral_amount;
		}

		public void setR30_collateral_amount(BigDecimal r30_collateral_amount) {
			this.r30_collateral_amount = r30_collateral_amount;
		}

		public BigDecimal getR30_carrying_amount() {
			return r30_carrying_amount;
		}

		public void setR30_carrying_amount(BigDecimal r30_carrying_amount) {
			this.r30_carrying_amount = r30_carrying_amount;
		}

		public BigDecimal getR30_no_of_accts() {
			return r30_no_of_accts;
		}

		public void setR30_no_of_accts(BigDecimal r30_no_of_accts) {
			this.r30_no_of_accts = r30_no_of_accts;
		}

		public String getR31_rene_loans() {
			return r31_rene_loans;
		}

		public void setR31_rene_loans(String r31_rene_loans) {
			this.r31_rene_loans = r31_rene_loans;
		}

		public BigDecimal getR31_collateral_amount() {
			return r31_collateral_amount;
		}

		public void setR31_collateral_amount(BigDecimal r31_collateral_amount) {
			this.r31_collateral_amount = r31_collateral_amount;
		}

		public BigDecimal getR31_carrying_amount() {
			return r31_carrying_amount;
		}

		public void setR31_carrying_amount(BigDecimal r31_carrying_amount) {
			this.r31_carrying_amount = r31_carrying_amount;
		}

		public BigDecimal getR31_no_of_accts() {
			return r31_no_of_accts;
		}

		public void setR31_no_of_accts(BigDecimal r31_no_of_accts) {
			this.r31_no_of_accts = r31_no_of_accts;
		}

		public String getR32_rene_loans() {
			return r32_rene_loans;
		}

		public void setR32_rene_loans(String r32_rene_loans) {
			this.r32_rene_loans = r32_rene_loans;
		}

		public BigDecimal getR32_collateral_amount() {
			return r32_collateral_amount;
		}

		public void setR32_collateral_amount(BigDecimal r32_collateral_amount) {
			this.r32_collateral_amount = r32_collateral_amount;
		}

		public BigDecimal getR32_carrying_amount() {
			return r32_carrying_amount;
		}

		public void setR32_carrying_amount(BigDecimal r32_carrying_amount) {
			this.r32_carrying_amount = r32_carrying_amount;
		}

		public BigDecimal getR32_no_of_accts() {
			return r32_no_of_accts;
		}

		public void setR32_no_of_accts(BigDecimal r32_no_of_accts) {
			this.r32_no_of_accts = r32_no_of_accts;
		}

		public String getR33_rene_loans() {
			return r33_rene_loans;
		}

		public void setR33_rene_loans(String r33_rene_loans) {
			this.r33_rene_loans = r33_rene_loans;
		}

		public BigDecimal getR33_collateral_amount() {
			return r33_collateral_amount;
		}

		public void setR33_collateral_amount(BigDecimal r33_collateral_amount) {
			this.r33_collateral_amount = r33_collateral_amount;
		}

		public BigDecimal getR33_carrying_amount() {
			return r33_carrying_amount;
		}

		public void setR33_carrying_amount(BigDecimal r33_carrying_amount) {
			this.r33_carrying_amount = r33_carrying_amount;
		}

		public BigDecimal getR33_no_of_accts() {
			return r33_no_of_accts;
		}

		public void setR33_no_of_accts(BigDecimal r33_no_of_accts) {
			this.r33_no_of_accts = r33_no_of_accts;
		}

		public String getR34_rene_loans() {
			return r34_rene_loans;
		}

		public void setR34_rene_loans(String r34_rene_loans) {
			this.r34_rene_loans = r34_rene_loans;
		}

		public BigDecimal getR34_collateral_amount() {
			return r34_collateral_amount;
		}

		public void setR34_collateral_amount(BigDecimal r34_collateral_amount) {
			this.r34_collateral_amount = r34_collateral_amount;
		}

		public BigDecimal getR34_carrying_amount() {
			return r34_carrying_amount;
		}

		public void setR34_carrying_amount(BigDecimal r34_carrying_amount) {
			this.r34_carrying_amount = r34_carrying_amount;
		}

		public BigDecimal getR34_no_of_accts() {
			return r34_no_of_accts;
		}

		public void setR34_no_of_accts(BigDecimal r34_no_of_accts) {
			this.r34_no_of_accts = r34_no_of_accts;
		}

		public String getR35_rene_loans() {
			return r35_rene_loans;
		}

		public void setR35_rene_loans(String r35_rene_loans) {
			this.r35_rene_loans = r35_rene_loans;
		}

		public BigDecimal getR35_collateral_amount() {
			return r35_collateral_amount;
		}

		public void setR35_collateral_amount(BigDecimal r35_collateral_amount) {
			this.r35_collateral_amount = r35_collateral_amount;
		}

		public BigDecimal getR35_carrying_amount() {
			return r35_carrying_amount;
		}

		public void setR35_carrying_amount(BigDecimal r35_carrying_amount) {
			this.r35_carrying_amount = r35_carrying_amount;
		}

		public BigDecimal getR35_no_of_accts() {
			return r35_no_of_accts;
		}

		public void setR35_no_of_accts(BigDecimal r35_no_of_accts) {
			this.r35_no_of_accts = r35_no_of_accts;
		}

		public String getR36_rene_loans() {
			return r36_rene_loans;
		}

		public void setR36_rene_loans(String r36_rene_loans) {
			this.r36_rene_loans = r36_rene_loans;
		}

		public BigDecimal getR36_collateral_amount() {
			return r36_collateral_amount;
		}

		public void setR36_collateral_amount(BigDecimal r36_collateral_amount) {
			this.r36_collateral_amount = r36_collateral_amount;
		}

		public BigDecimal getR36_carrying_amount() {
			return r36_carrying_amount;
		}

		public void setR36_carrying_amount(BigDecimal r36_carrying_amount) {
			this.r36_carrying_amount = r36_carrying_amount;
		}

		public BigDecimal getR36_no_of_accts() {
			return r36_no_of_accts;
		}

		public void setR36_no_of_accts(BigDecimal r36_no_of_accts) {
			this.r36_no_of_accts = r36_no_of_accts;
		}

		public String getR37_rene_loans() {
			return r37_rene_loans;
		}

		public void setR37_rene_loans(String r37_rene_loans) {
			this.r37_rene_loans = r37_rene_loans;
		}

		public BigDecimal getR37_collateral_amount() {
			return r37_collateral_amount;
		}

		public void setR37_collateral_amount(BigDecimal r37_collateral_amount) {
			this.r37_collateral_amount = r37_collateral_amount;
		}

		public BigDecimal getR37_carrying_amount() {
			return r37_carrying_amount;
		}

		public void setR37_carrying_amount(BigDecimal r37_carrying_amount) {
			this.r37_carrying_amount = r37_carrying_amount;
		}

		public BigDecimal getR37_no_of_accts() {
			return r37_no_of_accts;
		}

		public void setR37_no_of_accts(BigDecimal r37_no_of_accts) {
			this.r37_no_of_accts = r37_no_of_accts;
		}

		public String getR38_rene_loans() {
			return r38_rene_loans;
		}

		public void setR38_rene_loans(String r38_rene_loans) {
			this.r38_rene_loans = r38_rene_loans;
		}

		public BigDecimal getR38_collateral_amount() {
			return r38_collateral_amount;
		}

		public void setR38_collateral_amount(BigDecimal r38_collateral_amount) {
			this.r38_collateral_amount = r38_collateral_amount;
		}

		public BigDecimal getR38_carrying_amount() {
			return r38_carrying_amount;
		}

		public void setR38_carrying_amount(BigDecimal r38_carrying_amount) {
			this.r38_carrying_amount = r38_carrying_amount;
		}

		public BigDecimal getR38_no_of_accts() {
			return r38_no_of_accts;
		}

		public void setR38_no_of_accts(BigDecimal r38_no_of_accts) {
			this.r38_no_of_accts = r38_no_of_accts;
		}

		public String getR39_rene_loans() {
			return r39_rene_loans;
		}

		public void setR39_rene_loans(String r39_rene_loans) {
			this.r39_rene_loans = r39_rene_loans;
		}

		public BigDecimal getR39_collateral_amount() {
			return r39_collateral_amount;
		}

		public void setR39_collateral_amount(BigDecimal r39_collateral_amount) {
			this.r39_collateral_amount = r39_collateral_amount;
		}

		public BigDecimal getR39_carrying_amount() {
			return r39_carrying_amount;
		}

		public void setR39_carrying_amount(BigDecimal r39_carrying_amount) {
			this.r39_carrying_amount = r39_carrying_amount;
		}

		public BigDecimal getR39_no_of_accts() {
			return r39_no_of_accts;
		}

		public void setR39_no_of_accts(BigDecimal r39_no_of_accts) {
			this.r39_no_of_accts = r39_no_of_accts;
		}

		public String getR40_rene_loans() {
			return r40_rene_loans;
		}

		public void setR40_rene_loans(String r40_rene_loans) {
			this.r40_rene_loans = r40_rene_loans;
		}

		public BigDecimal getR40_collateral_amount() {
			return r40_collateral_amount;
		}

		public void setR40_collateral_amount(BigDecimal r40_collateral_amount) {
			this.r40_collateral_amount = r40_collateral_amount;
		}

		public BigDecimal getR40_carrying_amount() {
			return r40_carrying_amount;
		}

		public void setR40_carrying_amount(BigDecimal r40_carrying_amount) {
			this.r40_carrying_amount = r40_carrying_amount;
		}

		public BigDecimal getR40_no_of_accts() {
			return r40_no_of_accts;
		}

		public void setR40_no_of_accts(BigDecimal r40_no_of_accts) {
			this.r40_no_of_accts = r40_no_of_accts;
		}

		public String getR41_rene_loans() {
			return r41_rene_loans;
		}

		public void setR41_rene_loans(String r41_rene_loans) {
			this.r41_rene_loans = r41_rene_loans;
		}

		public BigDecimal getR41_collateral_amount() {
			return r41_collateral_amount;
		}

		public void setR41_collateral_amount(BigDecimal r41_collateral_amount) {
			this.r41_collateral_amount = r41_collateral_amount;
		}

		public BigDecimal getR41_carrying_amount() {
			return r41_carrying_amount;
		}

		public void setR41_carrying_amount(BigDecimal r41_carrying_amount) {
			this.r41_carrying_amount = r41_carrying_amount;
		}

		public BigDecimal getR41_no_of_accts() {
			return r41_no_of_accts;
		}

		public void setR41_no_of_accts(BigDecimal r41_no_of_accts) {
			this.r41_no_of_accts = r41_no_of_accts;
		}

		public String getR42_rene_loans() {
			return r42_rene_loans;
		}

		public void setR42_rene_loans(String r42_rene_loans) {
			this.r42_rene_loans = r42_rene_loans;
		}

		public BigDecimal getR42_collateral_amount() {
			return r42_collateral_amount;
		}

		public void setR42_collateral_amount(BigDecimal r42_collateral_amount) {
			this.r42_collateral_amount = r42_collateral_amount;
		}

		public BigDecimal getR42_carrying_amount() {
			return r42_carrying_amount;
		}

		public void setR42_carrying_amount(BigDecimal r42_carrying_amount) {
			this.r42_carrying_amount = r42_carrying_amount;
		}

		public BigDecimal getR42_no_of_accts() {
			return r42_no_of_accts;
		}

		public void setR42_no_of_accts(BigDecimal r42_no_of_accts) {
			this.r42_no_of_accts = r42_no_of_accts;
		}

		public String getR43_rene_loans() {
			return r43_rene_loans;
		}

		public void setR43_rene_loans(String r43_rene_loans) {
			this.r43_rene_loans = r43_rene_loans;
		}

		public BigDecimal getR43_collateral_amount() {
			return r43_collateral_amount;
		}

		public void setR43_collateral_amount(BigDecimal r43_collateral_amount) {
			this.r43_collateral_amount = r43_collateral_amount;
		}

		public BigDecimal getR43_carrying_amount() {
			return r43_carrying_amount;
		}

		public void setR43_carrying_amount(BigDecimal r43_carrying_amount) {
			this.r43_carrying_amount = r43_carrying_amount;
		}

		public BigDecimal getR43_no_of_accts() {
			return r43_no_of_accts;
		}

		public void setR43_no_of_accts(BigDecimal r43_no_of_accts) {
			this.r43_no_of_accts = r43_no_of_accts;
		}

		public String getR44_rene_loans() {
			return r44_rene_loans;
		}

		public void setR44_rene_loans(String r44_rene_loans) {
			this.r44_rene_loans = r44_rene_loans;
		}

		public BigDecimal getR44_collateral_amount() {
			return r44_collateral_amount;
		}

		public void setR44_collateral_amount(BigDecimal r44_collateral_amount) {
			this.r44_collateral_amount = r44_collateral_amount;
		}

		public BigDecimal getR44_carrying_amount() {
			return r44_carrying_amount;
		}

		public void setR44_carrying_amount(BigDecimal r44_carrying_amount) {
			this.r44_carrying_amount = r44_carrying_amount;
		}

		public BigDecimal getR44_no_of_accts() {
			return r44_no_of_accts;
		}

		public void setR44_no_of_accts(BigDecimal r44_no_of_accts) {
			this.r44_no_of_accts = r44_no_of_accts;
		}

		public String getR45_rene_loans() {
			return r45_rene_loans;
		}

		public void setR45_rene_loans(String r45_rene_loans) {
			this.r45_rene_loans = r45_rene_loans;
		}

		public BigDecimal getR45_collateral_amount() {
			return r45_collateral_amount;
		}

		public void setR45_collateral_amount(BigDecimal r45_collateral_amount) {
			this.r45_collateral_amount = r45_collateral_amount;
		}

		public BigDecimal getR45_carrying_amount() {
			return r45_carrying_amount;
		}

		public void setR45_carrying_amount(BigDecimal r45_carrying_amount) {
			this.r45_carrying_amount = r45_carrying_amount;
		}

		public BigDecimal getR45_no_of_accts() {
			return r45_no_of_accts;
		}

		public void setR45_no_of_accts(BigDecimal r45_no_of_accts) {
			this.r45_no_of_accts = r45_no_of_accts;
		}

		public String getR46_rene_loans() {
			return r46_rene_loans;
		}

		public void setR46_rene_loans(String r46_rene_loans) {
			this.r46_rene_loans = r46_rene_loans;
		}

		public BigDecimal getR46_collateral_amount() {
			return r46_collateral_amount;
		}

		public void setR46_collateral_amount(BigDecimal r46_collateral_amount) {
			this.r46_collateral_amount = r46_collateral_amount;
		}

		public BigDecimal getR46_carrying_amount() {
			return r46_carrying_amount;
		}

		public void setR46_carrying_amount(BigDecimal r46_carrying_amount) {
			this.r46_carrying_amount = r46_carrying_amount;
		}

		public BigDecimal getR46_no_of_accts() {
			return r46_no_of_accts;
		}

		public void setR46_no_of_accts(BigDecimal r46_no_of_accts) {
			this.r46_no_of_accts = r46_no_of_accts;
		}

		public String getR47_rene_loans() {
			return r47_rene_loans;
		}

		public void setR47_rene_loans(String r47_rene_loans) {
			this.r47_rene_loans = r47_rene_loans;
		}

		public BigDecimal getR47_collateral_amount() {
			return r47_collateral_amount;
		}

		public void setR47_collateral_amount(BigDecimal r47_collateral_amount) {
			this.r47_collateral_amount = r47_collateral_amount;
		}

		public BigDecimal getR47_carrying_amount() {
			return r47_carrying_amount;
		}

		public void setR47_carrying_amount(BigDecimal r47_carrying_amount) {
			this.r47_carrying_amount = r47_carrying_amount;
		}

		public BigDecimal getR47_no_of_accts() {
			return r47_no_of_accts;
		}

		public void setR47_no_of_accts(BigDecimal r47_no_of_accts) {
			this.r47_no_of_accts = r47_no_of_accts;
		}

		public String getR48_rene_loans() {
			return r48_rene_loans;
		}

		public void setR48_rene_loans(String r48_rene_loans) {
			this.r48_rene_loans = r48_rene_loans;
		}

		public BigDecimal getR48_collateral_amount() {
			return r48_collateral_amount;
		}

		public void setR48_collateral_amount(BigDecimal r48_collateral_amount) {
			this.r48_collateral_amount = r48_collateral_amount;
		}

		public BigDecimal getR48_carrying_amount() {
			return r48_carrying_amount;
		}

		public void setR48_carrying_amount(BigDecimal r48_carrying_amount) {
			this.r48_carrying_amount = r48_carrying_amount;
		}

		public BigDecimal getR48_no_of_accts() {
			return r48_no_of_accts;
		}

		public void setR48_no_of_accts(BigDecimal r48_no_of_accts) {
			this.r48_no_of_accts = r48_no_of_accts;
		}

		public String getR49_rene_loans() {
			return r49_rene_loans;
		}

		public void setR49_rene_loans(String r49_rene_loans) {
			this.r49_rene_loans = r49_rene_loans;
		}

		public BigDecimal getR49_collateral_amount() {
			return r49_collateral_amount;
		}

		public void setR49_collateral_amount(BigDecimal r49_collateral_amount) {
			this.r49_collateral_amount = r49_collateral_amount;
		}

		public BigDecimal getR49_carrying_amount() {
			return r49_carrying_amount;
		}

		public void setR49_carrying_amount(BigDecimal r49_carrying_amount) {
			this.r49_carrying_amount = r49_carrying_amount;
		}

		public BigDecimal getR49_no_of_accts() {
			return r49_no_of_accts;
		}

		public void setR49_no_of_accts(BigDecimal r49_no_of_accts) {
			this.r49_no_of_accts = r49_no_of_accts;
		}

		public String getR50_rene_loans() {
			return r50_rene_loans;
		}

		public void setR50_rene_loans(String r50_rene_loans) {
			this.r50_rene_loans = r50_rene_loans;
		}

		public BigDecimal getR50_collateral_amount() {
			return r50_collateral_amount;
		}

		public void setR50_collateral_amount(BigDecimal r50_collateral_amount) {
			this.r50_collateral_amount = r50_collateral_amount;
		}

		public BigDecimal getR50_carrying_amount() {
			return r50_carrying_amount;
		}

		public void setR50_carrying_amount(BigDecimal r50_carrying_amount) {
			this.r50_carrying_amount = r50_carrying_amount;
		}

		public BigDecimal getR50_no_of_accts() {
			return r50_no_of_accts;
		}

		public void setR50_no_of_accts(BigDecimal r50_no_of_accts) {
			this.r50_no_of_accts = r50_no_of_accts;
		}

		public String getR51_rene_loans() {
			return r51_rene_loans;
		}

		public void setR51_rene_loans(String r51_rene_loans) {
			this.r51_rene_loans = r51_rene_loans;
		}

		public BigDecimal getR51_collateral_amount() {
			return r51_collateral_amount;
		}

		public void setR51_collateral_amount(BigDecimal r51_collateral_amount) {
			this.r51_collateral_amount = r51_collateral_amount;
		}

		public BigDecimal getR51_carrying_amount() {
			return r51_carrying_amount;
		}

		public void setR51_carrying_amount(BigDecimal r51_carrying_amount) {
			this.r51_carrying_amount = r51_carrying_amount;
		}

		public BigDecimal getR51_no_of_accts() {
			return r51_no_of_accts;
		}

		public void setR51_no_of_accts(BigDecimal r51_no_of_accts) {
			this.r51_no_of_accts = r51_no_of_accts;
		}

		public String getR52_rene_loans() {
			return r52_rene_loans;
		}

		public void setR52_rene_loans(String r52_rene_loans) {
			this.r52_rene_loans = r52_rene_loans;
		}

		public BigDecimal getR52_collateral_amount() {
			return r52_collateral_amount;
		}

		public void setR52_collateral_amount(BigDecimal r52_collateral_amount) {
			this.r52_collateral_amount = r52_collateral_amount;
		}

		public BigDecimal getR52_carrying_amount() {
			return r52_carrying_amount;
		}

		public void setR52_carrying_amount(BigDecimal r52_carrying_amount) {
			this.r52_carrying_amount = r52_carrying_amount;
		}

		public BigDecimal getR52_no_of_accts() {
			return r52_no_of_accts;
		}

		public void setR52_no_of_accts(BigDecimal r52_no_of_accts) {
			this.r52_no_of_accts = r52_no_of_accts;
		}

		public String getR53_rene_loans() {
			return r53_rene_loans;
		}

		public void setR53_rene_loans(String r53_rene_loans) {
			this.r53_rene_loans = r53_rene_loans;
		}

		public BigDecimal getR53_collateral_amount() {
			return r53_collateral_amount;
		}

		public void setR53_collateral_amount(BigDecimal r53_collateral_amount) {
			this.r53_collateral_amount = r53_collateral_amount;
		}

		public BigDecimal getR53_carrying_amount() {
			return r53_carrying_amount;
		}

		public void setR53_carrying_amount(BigDecimal r53_carrying_amount) {
			this.r53_carrying_amount = r53_carrying_amount;
		}

		public BigDecimal getR53_no_of_accts() {
			return r53_no_of_accts;
		}

		public void setR53_no_of_accts(BigDecimal r53_no_of_accts) {
			this.r53_no_of_accts = r53_no_of_accts;
		}

		public String getR54_rene_loans() {
			return r54_rene_loans;
		}

		public void setR54_rene_loans(String r54_rene_loans) {
			this.r54_rene_loans = r54_rene_loans;
		}

		public BigDecimal getR54_collateral_amount() {
			return r54_collateral_amount;
		}

		public void setR54_collateral_amount(BigDecimal r54_collateral_amount) {
			this.r54_collateral_amount = r54_collateral_amount;
		}

		public BigDecimal getR54_carrying_amount() {
			return r54_carrying_amount;
		}

		public void setR54_carrying_amount(BigDecimal r54_carrying_amount) {
			this.r54_carrying_amount = r54_carrying_amount;
		}

		public BigDecimal getR54_no_of_accts() {
			return r54_no_of_accts;
		}

		public void setR54_no_of_accts(BigDecimal r54_no_of_accts) {
			this.r54_no_of_accts = r54_no_of_accts;
		}

		public String getR55_rene_loans() {
			return r55_rene_loans;
		}

		public void setR55_rene_loans(String r55_rene_loans) {
			this.r55_rene_loans = r55_rene_loans;
		}

		public BigDecimal getR55_collateral_amount() {
			return r55_collateral_amount;
		}

		public void setR55_collateral_amount(BigDecimal r55_collateral_amount) {
			this.r55_collateral_amount = r55_collateral_amount;
		}

		public BigDecimal getR55_carrying_amount() {
			return r55_carrying_amount;
		}

		public void setR55_carrying_amount(BigDecimal r55_carrying_amount) {
			this.r55_carrying_amount = r55_carrying_amount;
		}

		public BigDecimal getR55_no_of_accts() {
			return r55_no_of_accts;
		}

		public void setR55_no_of_accts(BigDecimal r55_no_of_accts) {
			this.r55_no_of_accts = r55_no_of_accts;
		}

		public String getR56_rene_loans() {
			return r56_rene_loans;
		}

		public void setR56_rene_loans(String r56_rene_loans) {
			this.r56_rene_loans = r56_rene_loans;
		}

		public BigDecimal getR56_collateral_amount() {
			return r56_collateral_amount;
		}

		public void setR56_collateral_amount(BigDecimal r56_collateral_amount) {
			this.r56_collateral_amount = r56_collateral_amount;
		}

		public BigDecimal getR56_carrying_amount() {
			return r56_carrying_amount;
		}

		public void setR56_carrying_amount(BigDecimal r56_carrying_amount) {
			this.r56_carrying_amount = r56_carrying_amount;
		}

		public BigDecimal getR56_no_of_accts() {
			return r56_no_of_accts;
		}

		public void setR56_no_of_accts(BigDecimal r56_no_of_accts) {
			this.r56_no_of_accts = r56_no_of_accts;
		}

		public String getR57_rene_loans() {
			return r57_rene_loans;
		}

		public void setR57_rene_loans(String r57_rene_loans) {
			this.r57_rene_loans = r57_rene_loans;
		}

		public BigDecimal getR57_collateral_amount() {
			return r57_collateral_amount;
		}

		public void setR57_collateral_amount(BigDecimal r57_collateral_amount) {
			this.r57_collateral_amount = r57_collateral_amount;
		}

		public BigDecimal getR57_carrying_amount() {
			return r57_carrying_amount;
		}

		public void setR57_carrying_amount(BigDecimal r57_carrying_amount) {
			this.r57_carrying_amount = r57_carrying_amount;
		}

		public BigDecimal getR57_no_of_accts() {
			return r57_no_of_accts;
		}

		public void setR57_no_of_accts(BigDecimal r57_no_of_accts) {
			this.r57_no_of_accts = r57_no_of_accts;
		}

		public String getR58_rene_loans() {
			return r58_rene_loans;
		}

		public void setR58_rene_loans(String r58_rene_loans) {
			this.r58_rene_loans = r58_rene_loans;
		}

		public BigDecimal getR58_collateral_amount() {
			return r58_collateral_amount;
		}

		public void setR58_collateral_amount(BigDecimal r58_collateral_amount) {
			this.r58_collateral_amount = r58_collateral_amount;
		}

		public BigDecimal getR58_carrying_amount() {
			return r58_carrying_amount;
		}

		public void setR58_carrying_amount(BigDecimal r58_carrying_amount) {
			this.r58_carrying_amount = r58_carrying_amount;
		}

		public BigDecimal getR58_no_of_accts() {
			return r58_no_of_accts;
		}

		public void setR58_no_of_accts(BigDecimal r58_no_of_accts) {
			this.r58_no_of_accts = r58_no_of_accts;
		}

		public String getR59_rene_loans() {
			return r59_rene_loans;
		}

		public void setR59_rene_loans(String r59_rene_loans) {
			this.r59_rene_loans = r59_rene_loans;
		}

		public BigDecimal getR59_collateral_amount() {
			return r59_collateral_amount;
		}

		public void setR59_collateral_amount(BigDecimal r59_collateral_amount) {
			this.r59_collateral_amount = r59_collateral_amount;
		}

		public BigDecimal getR59_carrying_amount() {
			return r59_carrying_amount;
		}

		public void setR59_carrying_amount(BigDecimal r59_carrying_amount) {
			this.r59_carrying_amount = r59_carrying_amount;
		}

		public BigDecimal getR59_no_of_accts() {
			return r59_no_of_accts;
		}

		public void setR59_no_of_accts(BigDecimal r59_no_of_accts) {
			this.r59_no_of_accts = r59_no_of_accts;
		}

		public String getR60_rene_loans() {
			return r60_rene_loans;
		}

		public void setR60_rene_loans(String r60_rene_loans) {
			this.r60_rene_loans = r60_rene_loans;
		}

		public BigDecimal getR60_collateral_amount() {
			return r60_collateral_amount;
		}

		public void setR60_collateral_amount(BigDecimal r60_collateral_amount) {
			this.r60_collateral_amount = r60_collateral_amount;
		}

		public BigDecimal getR60_carrying_amount() {
			return r60_carrying_amount;
		}

		public void setR60_carrying_amount(BigDecimal r60_carrying_amount) {
			this.r60_carrying_amount = r60_carrying_amount;
		}

		public BigDecimal getR60_no_of_accts() {
			return r60_no_of_accts;
		}

		public void setR60_no_of_accts(BigDecimal r60_no_of_accts) {
			this.r60_no_of_accts = r60_no_of_accts;
		}

		public String getR61_rene_loans() {
			return r61_rene_loans;
		}

		public void setR61_rene_loans(String r61_rene_loans) {
			this.r61_rene_loans = r61_rene_loans;
		}

		public BigDecimal getR61_collateral_amount() {
			return r61_collateral_amount;
		}

		public void setR61_collateral_amount(BigDecimal r61_collateral_amount) {
			this.r61_collateral_amount = r61_collateral_amount;
		}

		public BigDecimal getR61_carrying_amount() {
			return r61_carrying_amount;
		}

		public void setR61_carrying_amount(BigDecimal r61_carrying_amount) {
			this.r61_carrying_amount = r61_carrying_amount;
		}

		public BigDecimal getR61_no_of_accts() {
			return r61_no_of_accts;
		}

		public void setR61_no_of_accts(BigDecimal r61_no_of_accts) {
			this.r61_no_of_accts = r61_no_of_accts;
		}

		public String getR62_rene_loans() {
			return r62_rene_loans;
		}

		public void setR62_rene_loans(String r62_rene_loans) {
			this.r62_rene_loans = r62_rene_loans;
		}

		public BigDecimal getR62_collateral_amount() {
			return r62_collateral_amount;
		}

		public void setR62_collateral_amount(BigDecimal r62_collateral_amount) {
			this.r62_collateral_amount = r62_collateral_amount;
		}

		public BigDecimal getR62_carrying_amount() {
			return r62_carrying_amount;
		}

		public void setR62_carrying_amount(BigDecimal r62_carrying_amount) {
			this.r62_carrying_amount = r62_carrying_amount;
		}

		public BigDecimal getR62_no_of_accts() {
			return r62_no_of_accts;
		}

		public void setR62_no_of_accts(BigDecimal r62_no_of_accts) {
			this.r62_no_of_accts = r62_no_of_accts;
		}

		public String getR63_rene_loans() {
			return r63_rene_loans;
		}

		public void setR63_rene_loans(String r63_rene_loans) {
			this.r63_rene_loans = r63_rene_loans;
		}

		public BigDecimal getR63_collateral_amount() {
			return r63_collateral_amount;
		}

		public void setR63_collateral_amount(BigDecimal r63_collateral_amount) {
			this.r63_collateral_amount = r63_collateral_amount;
		}

		public BigDecimal getR63_carrying_amount() {
			return r63_carrying_amount;
		}

		public void setR63_carrying_amount(BigDecimal r63_carrying_amount) {
			this.r63_carrying_amount = r63_carrying_amount;
		}

		public BigDecimal getR63_no_of_accts() {
			return r63_no_of_accts;
		}

		public void setR63_no_of_accts(BigDecimal r63_no_of_accts) {
			this.r63_no_of_accts = r63_no_of_accts;
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

	public ModelAndView getQ_RLFA1View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("Q_RLFA1 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);
		System.out.println("DtlType = " + dtltype);

		try {

			Date dt = dateformat.parse(todate);
			if ("detail".equalsIgnoreCase(dtltype)) {

				// ARCHIVAL DETAIL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_RLFA1_Archival_Detail_Entity> T1Master = getDetaildatabydateListarchival(dt, version);

					System.out.println("Archival Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// RESUB DETAIL
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_RLFA1_Resub_Detail_Entity> T1Master = getResubDetaildatabydateList(dt, version);

					System.out.println("Resub Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// NORMAL DETAIL
				else {

					List<Q_RLFA1_Detail_Entity> T1Master = getDetaildatabydateList(dt);

					System.out.println("Normal Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}
			} else {

				// ARCHIVAL SUMMARY
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_RLFA1_Archival_Summary_Entity> T1Master = getDataByDateListArchival(dt, version);

					System.out.println("Archival Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// RESUB SUMMARY
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_RLFA1_Resub_Summary_Entity> T1Master = getResubSummarydatabydateListarchival(dt, version);

					System.out.println("Resub Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// NORMAL SUMMARY
				else {

					List<Q_RLFA1_Summary_Entity> T1Master = getSummaryDataByDate(dt);

					System.out.println("Normal Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				mv.addObject("displaymode", "summary");
			}

			mv.addObject("report_date", dateformat.format(dt));

		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_RLFA1");

		System.out.println("View Loaded : " + mv.getViewName());

		return mv;
	}

// Archival View
	public List<Object[]> getQ_RLFA1Archival() {

		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Q_RLFA1_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_RLFA1_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");

				Q_RLFA1_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest archival version: " + first.getReport_version());

			} else {

				System.out.println("No archival data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_RLFA1 Archival data: " + e.getMessage());

			e.printStackTrace();
		}

		return archivalList;
	}

	@Transactional
	public void updateReport(Q_RLFA1_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_RLFA1 Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_RLFA1_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_RLFA1_Summary_Entity oldcopy = new Q_RLFA1_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "rene_loans", "collateral_amount", "carrying_amount", "no_of_accts" };

		try {

			for (int i = 10; i <= 63; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_RLFA1_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_RLFA1_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_RLFA1_SUMMARY_TABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_RLFA1_DETAIL_TABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(detailSql, value, updatedEntity.getReport_date());

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Audit only if changes found
			String changes = auditService.getChanges(oldcopy, existingSummary);

			if (!changes.isEmpty()) {

				auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
						"Q_RLFA1 Summary Screen", "BRRS_Q_RLFA1_SUMMARY");
			}

			System.out.println("Q_RLFA1 Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_RLFA1 fields", e);
		}
	}

	public List<Object[]> getQ_RLFA1Resub() {

		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Q_RLFA1_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_RLFA1_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " resub records");

				Q_RLFA1_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest resub version : " + first.getReport_version());

			} else {

				System.out.println("No resub data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_RLFA1 Resub data : " + e.getMessage());

			e.printStackTrace();
		}

		return resubList;
	}

	@Transactional
	public void updateResubReport(Q_RLFA1_Resub_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_RLFA1 Resub Update");

		Date reportDate = updatedEntity.getReport_date();

		BigDecimal maxVersion = findMaxVersion(reportDate);

		if (maxVersion == null) {
			throw new RuntimeException("No record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = maxVersion.add(BigDecimal.ONE);

		Date now = new Date();

		try {

			Q_RLFA1_Resub_Summary_Entity resubSummary = new Q_RLFA1_Resub_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, resubSummary);

			resubSummary.setReport_date(reportDate);
			resubSummary.setReport_version(newVersion);
			resubSummary.setReportResubDate(now);

			Q_RLFA1_Resub_Detail_Entity resubDetail = new Q_RLFA1_Resub_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, resubDetail);

			resubDetail.setReport_date(reportDate);
			resubDetail.setReport_version(newVersion);
			resubDetail.setReportResubDate(now);

			Q_RLFA1_Archival_Summary_Entity archivalSummary = new Q_RLFA1_Archival_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalSummary);

			archivalSummary.setReport_date(reportDate);
			archivalSummary.setReport_version(newVersion);
			archivalSummary.setReportResubDate(now);

			Q_RLFA1_Archival_Detail_Entity archivalDetail = new Q_RLFA1_Archival_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalDetail);

			archivalDetail.setReport_date(reportDate);
			archivalDetail.setReport_version(newVersion);
			archivalDetail.setReportResubDate(now);

			insertResubSummary(resubSummary);
			insertResubDetail(resubDetail);
			insertArchivalSummary(archivalSummary);
			insertArchivalDetail(archivalDetail);
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attrs != null) {

				HttpServletRequest request = attrs.getRequest();

				String userid = (String) request.getSession().getAttribute("USERID");

				auditService.createBusinessAudit(userid, "RESUBMIT", "Q_RLFA1 Resub Summary", null,
						"BRRS_Q_RLFA1_RESUB_SUMMARY");
			}

			System.out.println("Q_RLFA1 Resub Version Created Successfully : " + newVersion);

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("Error while creating Q_RLFA1 Resub Version", e);
		}
	}

	private void insertResubSummary(Q_RLFA1_Resub_Summary_Entity entity) {

		try {
			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_RLFA1_RESUB_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 63) ---
			for (int i = 10; i <= 63; i++) {
				columns.append("r").append(i).append("_rene_loans,").append("r").append(i).append("_collateral_amount,")
						.append("r").append(i).append("_carrying_amount,").append("r").append(i)
						.append("_no_of_accts,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_rene_loans"));
				params.add(getValue(entity, "getR" + i + "_collateral_amount"));
				params.add(getValue(entity, "getR" + i + "_carrying_amount"));
				params.add(getValue(entity, "getR" + i + "_no_of_accts"));
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_RLFA1 RESUB SUMMARY", e);
		}
	}

	private void insertResubDetail(Q_RLFA1_Resub_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_RLFA1_RESUB_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 63) ---
			for (int i = 10; i <= 63; i++) {

				columns.append("r").append(i).append("_rene_loans,").append("r").append(i).append("_collateral_amount,")
						.append("r").append(i).append("_carrying_amount,").append("r").append(i)
						.append("_no_of_accts,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_rene_loans"));
				params.add(getValue(entity, "getR" + i + "_collateral_amount"));
				params.add(getValue(entity, "getR" + i + "_carrying_amount"));
				params.add(getValue(entity, "getR" + i + "_no_of_accts"));
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_RLFA1 RESUB DETAIL", e);
		}
	}

	private void insertArchivalSummary(Q_RLFA1_Archival_Summary_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 63) ---
			for (int i = 10; i <= 63; i++) {

				columns.append("r").append(i).append("_rene_loans,").append("r").append(i).append("_collateral_amount,")
						.append("r").append(i).append("_carrying_amount,").append("r").append(i)
						.append("_no_of_accts,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_rene_loans"));
				params.add(getValue(entity, "getR" + i + "_collateral_amount"));
				params.add(getValue(entity, "getR" + i + "_carrying_amount"));
				params.add(getValue(entity, "getR" + i + "_no_of_accts"));
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_RLFA1 ARCHIVAL SUMMARY", e);
		}
	}

	private void insertArchivalDetail(Q_RLFA1_Archival_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_RLFA1_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 63) ---
			for (int i = 10; i <= 63; i++) {

				columns.append("r").append(i).append("_rene_loans,").append("r").append(i).append("_collateral_amount,")
						.append("r").append(i).append("_carrying_amount,").append("r").append(i)
						.append("_no_of_accts,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_rene_loans"));
				params.add(getValue(entity, "getR" + i + "_collateral_amount"));
				params.add(getValue(entity, "getR" + i + "_carrying_amount"));
				params.add(getValue(entity, "getR" + i + "_no_of_accts"));
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_RLFA1 ARCHIVAL DETAIL", e);
		}
	}

	private Object getValue(Object obj, String methodName) {
		try {
			return obj.getClass().getMethod(methodName).invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

// Summary EXCEL  FORMAT
	public byte[] getQ_RLFA1Excel(String filename, String reportId, String fromdate, String todate, String currency,
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
				return getExcelQ_RLFA1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_RLFA1ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_Q_RLFA1EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<Q_RLFA1_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_RLFA1 report. Returning empty result.");
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

					int startRow = 6;

					if (!dataList.isEmpty()) {
						for (int i = 0; i < dataList.size(); i++) {
							Q_RLFA1_Summary_Entity record = dataList.get(i);

							System.out.println("rownumber=" + startRow + i);
							Row row = sheet.getRow(startRow + i);
							if (row == null) {
								row = sheet.createRow(startRow + i);
							}
							Cell R12Cell = row.createCell(1);

							if (record.getReport_date() != null) {

								R12Cell.setCellValue(record.getReport_date());

								R12Cell.setCellStyle(dateStyle);

							} else {

								R12Cell.setCellValue("");

								R12Cell.setCellStyle(textStyle);
							}
							// row11
							row = sheet.getRow(10);

							// Column 2 -B

							Cell cellB = row.createCell(1);
							if (record.getR11_collateral_amount() != null) {
								cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							Cell cellC = row.createCell(2);
							if (record.getR11_carrying_amount() != null) {
								cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4-D
							Cell cellD = row.createCell(3);
							if (record.getR11_no_of_accts() != null) {
								cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row12
							row = sheet.getRow(11);

							// Column 2 -B

							cellB = row.createCell(1);
							if (record.getR12_collateral_amount() != null) {
								cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR12_carrying_amount() != null) {
								cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4-D
							cellD = row.createCell(3);
							if (record.getR12_no_of_accts() != null) {
								cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row13
							row = sheet.getRow(12);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR13_collateral_amount() != null) {
								cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR13_carrying_amount() != null) {
								cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR13_no_of_accts() != null) {
								cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row15
							row = sheet.getRow(14);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR15_collateral_amount() != null) {
								cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR15_carrying_amount() != null) {
								cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR15_no_of_accts() != null) {
								cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row16
							row = sheet.getRow(15);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR16_collateral_amount() != null) {
								cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR16_carrying_amount() != null) {
								cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR16_no_of_accts() != null) {
								cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row17
							row = sheet.getRow(16);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR17_collateral_amount() != null) {
								cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR17_carrying_amount() != null) {
								cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR17_no_of_accts() != null) {
								cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row18
							row = sheet.getRow(17);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR18_collateral_amount() != null) {
								cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR18_carrying_amount() != null) {
								cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR18_no_of_accts() != null) {
								cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row19
							row = sheet.getRow(18);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR19_collateral_amount() != null) {
								cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR19_carrying_amount() != null) {
								cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR19_no_of_accts() != null) {
								cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row20
							row = sheet.getRow(19);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR20_collateral_amount() != null) {
								cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR20_carrying_amount() != null) {
								cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR20_no_of_accts() != null) {
								cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row21
							row = sheet.getRow(20);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR21_collateral_amount() != null) {
								cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR21_carrying_amount() != null) {
								cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR21_no_of_accts() != null) {
								cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row22
							row = sheet.getRow(21);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR22_collateral_amount() != null) {
								cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR22_carrying_amount() != null) {
								cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR22_no_of_accts() != null) {
								cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row23
							row = sheet.getRow(22);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR23_collateral_amount() != null) {
								cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR23_carrying_amount() != null) {
								cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR23_no_of_accts() != null) {
								cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row24
							row = sheet.getRow(23);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR24_collateral_amount() != null) {
								cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR24_carrying_amount() != null) {
								cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR24_no_of_accts() != null) {
								cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row25
							row = sheet.getRow(24);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR25_collateral_amount() != null) {
								cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR25_carrying_amount() != null) {
								cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR25_no_of_accts() != null) {
								cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row26
							row = sheet.getRow(25);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR26_collateral_amount() != null) {
								cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR26_carrying_amount() != null) {
								cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR26_no_of_accts() != null) {
								cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row27
							row = sheet.getRow(26);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR27_collateral_amount() != null) {
								cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR27_carrying_amount() != null) {
								cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR27_no_of_accts() != null) {
								cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row29
							row = sheet.getRow(28);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR29_collateral_amount() != null) {
								cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR29_carrying_amount() != null) {
								cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR29_no_of_accts() != null) {
								cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row30
							row = sheet.getRow(29);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR30_collateral_amount() != null) {
								cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR30_carrying_amount() != null) {
								cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR30_no_of_accts() != null) {
								cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row31
							row = sheet.getRow(30);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR31_collateral_amount() != null) {
								cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR31_carrying_amount() != null) {
								cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR31_no_of_accts() != null) {
								cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row32
							row = sheet.getRow(31);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR32_collateral_amount() != null) {
								cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR32_carrying_amount() != null) {
								cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR32_no_of_accts() != null) {
								cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row33
							row = sheet.getRow(32);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR33_collateral_amount() != null) {
								cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR33_carrying_amount() != null) {
								cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR33_no_of_accts() != null) {
								cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row34
							row = sheet.getRow(33);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR34_collateral_amount() != null) {
								cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR34_carrying_amount() != null) {
								cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR34_no_of_accts() != null) {
								cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row35
							row = sheet.getRow(34);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR35_collateral_amount() != null) {
								cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR35_carrying_amount() != null) {
								cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR35_no_of_accts() != null) {
								cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row36
							row = sheet.getRow(35);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR36_collateral_amount() != null) {
								cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR36_carrying_amount() != null) {
								cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR36_no_of_accts() != null) {
								cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row38
							row = sheet.getRow(37);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR38_collateral_amount() != null) {
								cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR38_carrying_amount() != null) {
								cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR38_no_of_accts() != null) {
								cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row39
							row = sheet.getRow(38);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR39_collateral_amount() != null) {
								cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR39_carrying_amount() != null) {
								cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR39_no_of_accts() != null) {
								cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row41
							row = sheet.getRow(40);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR41_collateral_amount() != null) {
								cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR41_carrying_amount() != null) {
								cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR41_no_of_accts() != null) {
								cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row42
							row = sheet.getRow(41);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR42_collateral_amount() != null) {
								cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR42_carrying_amount() != null) {
								cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR42_no_of_accts() != null) {
								cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row44
							row = sheet.getRow(43);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR44_collateral_amount() != null) {
								cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR44_carrying_amount() != null) {
								cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR44_no_of_accts() != null) {
								cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row45
							row = sheet.getRow(44);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR45_collateral_amount() != null) {
								cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR45_carrying_amount() != null) {
								cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR45_no_of_accts() != null) {
								cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row46
							row = sheet.getRow(45);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR46_collateral_amount() != null) {
								cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR46_carrying_amount() != null) {
								cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR46_no_of_accts() != null) {
								cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row47
							row = sheet.getRow(46);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR47_collateral_amount() != null) {
								cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR47_carrying_amount() != null) {
								cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR47_no_of_accts() != null) {
								cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row49
							row = sheet.getRow(48);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR49_collateral_amount() != null) {
								cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR49_carrying_amount() != null) {
								cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR49_no_of_accts() != null) {
								cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row50
							row = sheet.getRow(49);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR50_collateral_amount() != null) {
								cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR50_carrying_amount() != null) {
								cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR50_no_of_accts() != null) {
								cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row51
							row = sheet.getRow(50);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR51_collateral_amount() != null) {
								cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR51_carrying_amount() != null) {
								cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR51_no_of_accts() != null) {
								cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row53
							row = sheet.getRow(52);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR53_collateral_amount() != null) {
								cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR53_carrying_amount() != null) {
								cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR53_no_of_accts() != null) {
								cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row54
							row = sheet.getRow(53);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR54_collateral_amount() != null) {
								cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR54_carrying_amount() != null) {
								cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR54_no_of_accts() != null) {
								cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row55
							row = sheet.getRow(54);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR55_collateral_amount() != null) {
								cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR55_carrying_amount() != null) {
								cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR55_no_of_accts() != null) {
								cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row57
							row = sheet.getRow(56);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR57_collateral_amount() != null) {
								cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR57_carrying_amount() != null) {
								cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR57_no_of_accts() != null) {
								cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row58
							row = sheet.getRow(57);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR58_collateral_amount() != null) {
								cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR58_carrying_amount() != null) {
								cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR58_no_of_accts() != null) {
								cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row59
							row = sheet.getRow(58);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR59_collateral_amount() != null) {
								cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR59_carrying_amount() != null) {
								cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR59_no_of_accts() != null) {
								cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row60
							row = sheet.getRow(59);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR60_collateral_amount() != null) {
								cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR60_carrying_amount() != null) {
								cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR60_no_of_accts() != null) {
								cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row61
							row = sheet.getRow(60);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR61_collateral_amount() != null) {
								cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR61_carrying_amount() != null) {
								cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR61_no_of_accts() != null) {
								cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

							// row62
							row = sheet.getRow(61);

							// Column 2 - B
							cellB = row.createCell(1);
							if (record.getR62_collateral_amount() != null) {
								cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
								cellB.setCellStyle(numberStyle);
							} else {
								cellB.setCellValue("");
								cellB.setCellStyle(textStyle);
							}

							// Column 3 - C
							cellC = row.createCell(2);
							if (record.getR62_carrying_amount() != null) {
								cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
								cellC.setCellStyle(numberStyle);
							} else {
								cellC.setCellValue("");
								cellC.setCellStyle(textStyle);
							}

							// Column 4 - D
							cellD = row.createCell(3);
							if (record.getR62_no_of_accts() != null) {
								cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
								cellD.setCellStyle(numberStyle);
							} else {
								cellD.setCellValue("");
								cellD.setCellStyle(textStyle);
							}

						}
						workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 SUMMARY", null,
								"BRRS_Q_RLFA1_SUMMARY_TABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

// Summary EXCEL  EMAIL
// Normal Email Excel
	public byte[] BRRS_Q_RLFA1EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_RLFA1ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_RLFA1EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<Q_RLFA1_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_Q_RLFA1 report. Returning empty result.");
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
						Q_RLFA1_Summary_Entity record1 = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(2);

						if (record1.getReport_date() != null) {

							R12Cell.setCellValue(record1.getReport_date());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
						Cell cell2 = row.createCell(2);

						if (record1.getR10_collateral_amount() != null) {
							cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);

						if (record1.getR10_carrying_amount() != null) {
							cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(5);

						if (record1.getR10_no_of_accts() != null) {
							cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
						Cell cell5 = row.createCell(2);

						if (record1.getR11_collateral_amount() != null) {
							cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(3);

						if (record1.getR11_carrying_amount() != null) {
							cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						Cell cell7 = row.createCell(5);

						if (record1.getR11_no_of_accts() != null) {
							cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
							cell7.setCellStyle(numberStyle);
						} else {
							cell7.setCellValue("");
							cell7.setCellStyle(textStyle);
						}

						row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
						Cell cell8 = row.createCell(2);

						if (record1.getR12_collateral_amount() != null) {
							cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
							cell8.setCellStyle(numberStyle);
						} else {
							cell8.setCellValue("");
							cell8.setCellStyle(textStyle);
						}

						Cell cell9 = row.createCell(3);

						if (record1.getR12_carrying_amount() != null) {
							cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
							cell9.setCellStyle(numberStyle);
						} else {
							cell9.setCellValue("");
							cell9.setCellStyle(textStyle);
						}

						Cell cell10 = row.createCell(5);

						if (record1.getR12_no_of_accts() != null) {
							cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
							cell10.setCellStyle(numberStyle);
						} else {
							cell10.setCellValue("");
							cell10.setCellStyle(textStyle);
						}

						row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
						Cell cell11 = row.createCell(2);

						if (record1.getR13_collateral_amount() != null) {
							cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
							cell11.setCellStyle(numberStyle);
						} else {
							cell11.setCellValue("");
							cell11.setCellStyle(textStyle);
						}

						Cell cell12 = row.createCell(3);

						if (record1.getR13_carrying_amount() != null) {
							cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
							cell12.setCellStyle(numberStyle);
						} else {
							cell12.setCellValue("");
							cell12.setCellStyle(textStyle);
						}

						Cell cell13 = row.createCell(5);

						if (record1.getR13_no_of_accts() != null) {
							cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
							cell13.setCellStyle(numberStyle);
						} else {
							cell13.setCellValue("");
							cell13.setCellStyle(textStyle);
						}

						row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
						Cell cell14 = row.createCell(2);

						if (record1.getR14_collateral_amount() != null) {
							cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
							cell14.setCellStyle(numberStyle);
						} else {
							cell14.setCellValue("");
							cell14.setCellStyle(textStyle);
						}

						Cell cell15 = row.createCell(3);

						if (record1.getR14_carrying_amount() != null) {
							cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
							cell15.setCellStyle(numberStyle);
						} else {
							cell15.setCellValue("");
							cell15.setCellStyle(textStyle);
						}

						Cell cell16 = row.createCell(5);

						if (record1.getR14_no_of_accts() != null) {
							cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
							cell16.setCellStyle(numberStyle);
						} else {
							cell16.setCellValue("");
							cell16.setCellStyle(textStyle);
						}

						row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
						Cell cell17 = row.createCell(2);

						if (record1.getR15_collateral_amount() != null) {
							cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
							cell17.setCellStyle(numberStyle);
						} else {
							cell17.setCellValue("");
							cell17.setCellStyle(textStyle);
						}

						Cell cell18 = row.createCell(3);

						if (record1.getR15_carrying_amount() != null) {
							cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
							cell18.setCellStyle(numberStyle);
						} else {
							cell18.setCellValue("");
							cell18.setCellStyle(textStyle);
						}

						Cell cell19 = row.createCell(5);

						if (record1.getR15_no_of_accts() != null) {
							cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
							cell19.setCellStyle(numberStyle);
						} else {
							cell19.setCellValue("");
							cell19.setCellStyle(textStyle);
						}

						row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
						Cell cell20 = row.createCell(2);

						if (record1.getR16_collateral_amount() != null) {
							cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
							cell20.setCellStyle(numberStyle);
						} else {
							cell20.setCellValue("");
							cell20.setCellStyle(textStyle);
						}

						Cell cell21 = row.createCell(3);

						if (record1.getR16_carrying_amount() != null) {
							cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
							cell21.setCellStyle(numberStyle);
						} else {
							cell21.setCellValue("");
							cell21.setCellStyle(textStyle);
						}

						Cell cell22 = row.createCell(5);

						if (record1.getR16_no_of_accts() != null) {
							cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
							cell22.setCellStyle(numberStyle);
						} else {
							cell22.setCellValue("");
							cell22.setCellStyle(textStyle);
						}

						row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
						Cell cell23 = row.createCell(2);

						if (record1.getR17_collateral_amount() != null) {
							cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
							cell23.setCellStyle(numberStyle);
						} else {
							cell23.setCellValue("");
							cell23.setCellStyle(textStyle);
						}

						Cell cell24 = row.createCell(3);

						if (record1.getR17_carrying_amount() != null) {
							cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
							cell24.setCellStyle(numberStyle);
						} else {
							cell24.setCellValue("");
							cell24.setCellStyle(textStyle);
						}

						Cell cell25 = row.createCell(5);

						if (record1.getR17_no_of_accts() != null) {
							cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
							cell25.setCellStyle(numberStyle);
						} else {
							cell25.setCellValue("");
							cell25.setCellStyle(textStyle);
						}

						row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
						Cell cell26 = row.createCell(2);

						if (record1.getR18_collateral_amount() != null) {
							cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
							cell26.setCellStyle(numberStyle);
						} else {
							cell26.setCellValue("");
							cell26.setCellStyle(textStyle);
						}

						Cell cell27 = row.createCell(3);

						if (record1.getR18_carrying_amount() != null) {
							cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
							cell27.setCellStyle(numberStyle);
						} else {
							cell27.setCellValue("");
							cell27.setCellStyle(textStyle);
						}

						Cell cell28 = row.createCell(5);

						if (record1.getR18_no_of_accts() != null) {
							cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
							cell28.setCellStyle(numberStyle);
						} else {
							cell28.setCellValue("");
							cell28.setCellStyle(textStyle);
						}

						row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
						Cell cell29 = row.createCell(2);

						if (record1.getR19_collateral_amount() != null) {
							cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
							cell29.setCellStyle(numberStyle);
						} else {
							cell29.setCellValue("");
							cell29.setCellStyle(textStyle);
						}

						Cell cell30 = row.createCell(3);

						if (record1.getR19_carrying_amount() != null) {
							cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
							cell30.setCellStyle(numberStyle);
						} else {
							cell30.setCellValue("");
							cell30.setCellStyle(textStyle);
						}

						Cell cell31 = row.createCell(4);

						if (record1.getR19_no_of_accts() != null) {
							cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
							cell31.setCellStyle(numberStyle);
						} else {
							cell31.setCellValue("");
							cell31.setCellStyle(textStyle);
						}

						row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
						Cell cell32 = row.createCell(2);

						if (record1.getR20_collateral_amount() != null) {
							cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
							cell32.setCellStyle(numberStyle);
						} else {
							cell32.setCellValue("");
							cell32.setCellStyle(textStyle);
						}

						Cell cell33 = row.createCell(3);

						if (record1.getR20_carrying_amount() != null) {
							cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
							cell33.setCellStyle(numberStyle);
						} else {
							cell33.setCellValue("");
							cell33.setCellStyle(textStyle);
						}

						Cell cell34 = row.createCell(4);

						if (record1.getR20_no_of_accts() != null) {
							cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
							cell34.setCellStyle(numberStyle);
						} else {
							cell34.setCellValue("");
							cell34.setCellStyle(textStyle);
						}

						row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
						Cell cell35 = row.createCell(2);

						if (record1.getR21_collateral_amount() != null) {
							cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
							cell35.setCellStyle(numberStyle);
						} else {
							cell35.setCellValue("");
							cell35.setCellStyle(textStyle);
						}

						Cell cell36 = row.createCell(3);

						if (record1.getR21_carrying_amount() != null) {
							cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
							cell36.setCellStyle(numberStyle);
						} else {
							cell36.setCellValue("");
							cell36.setCellStyle(textStyle);
						}

						Cell cell37 = row.createCell(4);

						if (record1.getR21_no_of_accts() != null) {
							cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
							cell37.setCellStyle(numberStyle);
						} else {
							cell37.setCellValue("");
							cell37.setCellStyle(textStyle);
						}

						row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
						Cell cell38 = row.createCell(2);

						if (record1.getR22_collateral_amount() != null) {
							cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
							cell38.setCellStyle(numberStyle);
						} else {
							cell38.setCellValue("");
							cell38.setCellStyle(textStyle);
						}

						Cell cell39 = row.createCell(3);

						if (record1.getR22_carrying_amount() != null) {
							cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
							cell39.setCellStyle(numberStyle);
						} else {
							cell39.setCellValue("");
							cell39.setCellStyle(textStyle);
						}

						Cell cell40 = row.createCell(4);

						if (record1.getR22_no_of_accts() != null) {
							cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
							cell40.setCellStyle(numberStyle);
						} else {
							cell40.setCellValue("");
							cell40.setCellStyle(textStyle);
						}

						row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
						Cell cell41 = row.createCell(2);

						if (record1.getR23_collateral_amount() != null) {
							cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
							cell41.setCellStyle(numberStyle);
						} else {
							cell41.setCellValue("");
							cell41.setCellStyle(textStyle);
						}

						Cell cell42 = row.createCell(3);

						if (record1.getR23_carrying_amount() != null) {
							cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
							cell42.setCellStyle(numberStyle);
						} else {
							cell42.setCellValue("");
							cell42.setCellStyle(textStyle);
						}

						Cell cell43 = row.createCell(4);

						if (record1.getR23_no_of_accts() != null) {
							cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
							cell43.setCellStyle(numberStyle);
						} else {
							cell43.setCellValue("");
							cell43.setCellStyle(textStyle);
						}

						row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
						Cell cell44 = row.createCell(2);

						if (record1.getR24_collateral_amount() != null) {
							cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
							cell44.setCellStyle(numberStyle);
						} else {
							cell44.setCellValue("");
							cell44.setCellStyle(textStyle);
						}

						Cell cell45 = row.createCell(3);

						if (record1.getR24_carrying_amount() != null) {
							cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
							cell45.setCellStyle(numberStyle);
						} else {
							cell45.setCellValue("");
							cell45.setCellStyle(textStyle);
						}

						Cell cell46 = row.createCell(4);

						if (record1.getR24_no_of_accts() != null) {
							cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
							cell46.setCellStyle(numberStyle);
						} else {
							cell46.setCellValue("");
							cell46.setCellStyle(textStyle);
						}

						row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
						Cell cell47 = row.createCell(2);

						if (record1.getR25_collateral_amount() != null) {
							cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
							cell47.setCellStyle(numberStyle);
						} else {
							cell47.setCellValue("");
							cell47.setCellStyle(textStyle);
						}

						Cell cell48 = row.createCell(3);

						if (record1.getR25_carrying_amount() != null) {
							cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
							cell48.setCellStyle(numberStyle);
						} else {
							cell48.setCellValue("");
							cell48.setCellStyle(textStyle);
						}

						Cell cell49 = row.createCell(4);

						if (record1.getR25_no_of_accts() != null) {
							cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
							cell49.setCellStyle(numberStyle);
						} else {
							cell49.setCellValue("");
							cell49.setCellStyle(textStyle);
						}

						row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
						Cell cell50 = row.createCell(2);

						if (record1.getR28_collateral_amount() != null) {
							cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
							cell50.setCellStyle(numberStyle);
						} else {
							cell50.setCellValue("");
							cell50.setCellStyle(textStyle);
						}

						Cell cell51 = row.createCell(3);

						if (record1.getR28_carrying_amount() != null) {
							cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
							cell51.setCellStyle(numberStyle);
						} else {
							cell51.setCellValue("");
							cell51.setCellStyle(textStyle);
						}

						Cell cell52 = row.createCell(4);

						if (record1.getR28_no_of_accts() != null) {
							cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
							cell52.setCellStyle(numberStyle);
						} else {
							cell52.setCellValue("");
							cell52.setCellStyle(textStyle);
						}

						row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
						Cell cell53 = row.createCell(2);

						if (record1.getR29_collateral_amount() != null) {
							cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
							cell53.setCellStyle(numberStyle);
						} else {
							cell53.setCellValue("");
							cell53.setCellStyle(textStyle);
						}

						Cell cell54 = row.createCell(3);

						if (record1.getR29_carrying_amount() != null) {
							cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
							cell54.setCellStyle(numberStyle);
						} else {
							cell54.setCellValue("");
							cell54.setCellStyle(textStyle);
						}

						Cell cell55 = row.createCell(4);

						if (record1.getR29_no_of_accts() != null) {
							cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
							cell55.setCellStyle(numberStyle);
						} else {
							cell55.setCellValue("");
							cell55.setCellStyle(textStyle);
						}

						row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
						Cell cell56 = row.createCell(2);

						if (record1.getR30_collateral_amount() != null) {
							cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
							cell56.setCellStyle(numberStyle);
						} else {
							cell56.setCellValue("");
							cell56.setCellStyle(textStyle);
						}

						Cell cell57 = row.createCell(3);

						if (record1.getR30_carrying_amount() != null) {
							cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
							cell57.setCellStyle(numberStyle);
						} else {
							cell57.setCellValue("");
							cell57.setCellStyle(textStyle);
						}

						Cell cell58 = row.createCell(4);

						if (record1.getR30_no_of_accts() != null) {
							cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
							cell58.setCellStyle(numberStyle);
						} else {
							cell58.setCellValue("");
							cell58.setCellStyle(textStyle);
						}

						row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
						Cell cell59 = row.createCell(2);

						if (record1.getR32_collateral_amount() != null) {
							cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
							cell59.setCellStyle(numberStyle);
						} else {
							cell59.setCellValue("");
							cell59.setCellStyle(textStyle);
						}

						Cell cell60 = row.createCell(3);

						if (record1.getR32_carrying_amount() != null) {
							cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
							cell60.setCellStyle(numberStyle);
						} else {
							cell60.setCellValue("");
							cell60.setCellStyle(textStyle);
						}

						Cell cell61 = row.createCell(4);

						if (record1.getR32_no_of_accts() != null) {
							cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
							cell61.setCellStyle(numberStyle);
						} else {
							cell61.setCellValue("");
							cell61.setCellStyle(textStyle);
						}

						row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
						Cell cell62 = row.createCell(2);

						if (record1.getR33_collateral_amount() != null) {
							cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
							cell62.setCellStyle(numberStyle);
						} else {
							cell62.setCellValue("");
							cell62.setCellStyle(textStyle);
						}

						Cell cell63 = row.createCell(3);

						if (record1.getR33_carrying_amount() != null) {
							cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
							cell63.setCellStyle(numberStyle);
						} else {
							cell63.setCellValue("");
							cell63.setCellStyle(textStyle);
						}

						Cell cell64 = row.createCell(4);

						if (record1.getR33_no_of_accts() != null) {
							cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
							cell64.setCellStyle(numberStyle);
						} else {
							cell64.setCellValue("");
							cell64.setCellStyle(textStyle);
						}

						row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
						Cell cell65 = row.createCell(2);

						if (record1.getR31_collateral_amount() != null) {
							cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
							cell65.setCellStyle(numberStyle);
						} else {
							cell65.setCellValue("");
							cell65.setCellStyle(textStyle);
						}

						Cell cell66 = row.createCell(3);

						if (record1.getR31_carrying_amount() != null) {
							cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
							cell66.setCellStyle(numberStyle);
						} else {
							cell66.setCellValue("");
							cell66.setCellStyle(textStyle);
						}

						Cell cell67 = row.createCell(4);

						if (record1.getR31_no_of_accts() != null) {
							cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
							cell67.setCellStyle(numberStyle);
						} else {
							cell67.setCellValue("");
							cell67.setCellStyle(textStyle);
						}

						row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
						Cell cell68 = row.createCell(2);

						if (record1.getR34_collateral_amount() != null) {
							cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
							cell68.setCellStyle(numberStyle);
						} else {
							cell68.setCellValue("");
							cell68.setCellStyle(textStyle);
						}

						Cell cell69 = row.createCell(3);

						if (record1.getR34_carrying_amount() != null) {
							cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
							cell69.setCellStyle(numberStyle);
						} else {
							cell69.setCellValue("");
							cell69.setCellStyle(textStyle);
						}

						Cell cell70 = row.createCell(4);

						if (record1.getR34_no_of_accts() != null) {
							cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
							cell70.setCellStyle(numberStyle);
						} else {
							cell70.setCellValue("");
							cell70.setCellStyle(textStyle);
						}

						row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
						Cell cell71 = row.createCell(2);

						if (record1.getR36_collateral_amount() != null) {
							cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
							cell71.setCellStyle(numberStyle);
						} else {
							cell71.setCellValue("");
							cell71.setCellStyle(textStyle);
						}

						Cell cell72 = row.createCell(3);

						if (record1.getR36_carrying_amount() != null) {
							cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
							cell72.setCellStyle(numberStyle);
						} else {
							cell72.setCellValue("");
							cell72.setCellStyle(textStyle);
						}

						Cell cell73 = row.createCell(4);

						if (record1.getR36_no_of_accts() != null) {
							cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
							cell73.setCellStyle(numberStyle);
						} else {
							cell73.setCellValue("");
							cell73.setCellStyle(textStyle);
						}

						row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
						Cell cell74 = row.createCell(2);

						if (record1.getR35_collateral_amount() != null) {
							cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
							cell74.setCellStyle(numberStyle);
						} else {
							cell74.setCellValue("");
							cell74.setCellStyle(textStyle);
						}

						Cell cell75 = row.createCell(3);

						if (record1.getR35_carrying_amount() != null) {
							cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
							cell75.setCellStyle(numberStyle);
						} else {
							cell75.setCellValue("");
							cell75.setCellStyle(textStyle);
						}

						Cell cell76 = row.createCell(4);

						if (record1.getR35_no_of_accts() != null) {
							cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
							cell76.setCellStyle(numberStyle);
						} else {
							cell76.setCellValue("");
							cell76.setCellStyle(textStyle);
						}

						row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
						Cell cell77 = row.createCell(2);

						if (record1.getR37_collateral_amount() != null) {
							cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
							cell77.setCellStyle(numberStyle);
						} else {
							cell77.setCellValue("");
							cell77.setCellStyle(textStyle);
						}

						Cell cell78 = row.createCell(3);

						if (record1.getR37_carrying_amount() != null) {
							cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
							cell78.setCellStyle(numberStyle);
						} else {
							cell78.setCellValue("");
							cell78.setCellStyle(textStyle);
						}

						Cell cell79 = row.createCell(4);

						if (record1.getR37_no_of_accts() != null) {
							cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
							cell79.setCellStyle(numberStyle);
						} else {
							cell79.setCellValue("");
							cell79.setCellStyle(textStyle);
						}

						row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
						Cell cell80 = row.createCell(2);

						if (record1.getR38_collateral_amount() != null) {
							cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
							cell80.setCellStyle(numberStyle);
						} else {
							cell80.setCellValue("");
							cell80.setCellStyle(textStyle);
						}

						Cell cell81 = row.createCell(3);

						if (record1.getR38_carrying_amount() != null) {
							cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
							cell81.setCellStyle(numberStyle);
						} else {
							cell81.setCellValue("");
							cell81.setCellStyle(textStyle);
						}

						Cell cell82 = row.createCell(4);

						if (record1.getR38_no_of_accts() != null) {
							cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
							cell82.setCellStyle(numberStyle);
						} else {
							cell82.setCellValue("");
							cell82.setCellStyle(textStyle);
						}

						row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
						Cell cell83 = row.createCell(2);

						if (record1.getR39_collateral_amount() != null) {
							cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
							cell83.setCellStyle(numberStyle);
						} else {
							cell83.setCellValue("");
							cell83.setCellStyle(textStyle);
						}

						Cell cell84 = row.createCell(3);

						if (record1.getR39_carrying_amount() != null) {
							cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
							cell84.setCellStyle(numberStyle);
						} else {
							cell84.setCellValue("");
							cell84.setCellStyle(textStyle);
						}

						Cell cell85 = row.createCell(4);

						if (record1.getR39_no_of_accts() != null) {
							cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
							cell85.setCellStyle(numberStyle);
						} else {
							cell85.setCellValue("");
							cell85.setCellStyle(textStyle);
						}

						row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
						Cell cell86 = row.createCell(2);

						if (record1.getR40_collateral_amount() != null) {
							cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
							cell86.setCellStyle(numberStyle);
						} else {
							cell86.setCellValue("");
							cell86.setCellStyle(textStyle);
						}

						Cell cell87 = row.createCell(3);

						if (record1.getR40_carrying_amount() != null) {
							cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
							cell87.setCellStyle(numberStyle);
						} else {
							cell87.setCellValue("");
							cell87.setCellStyle(textStyle);
						}

						Cell cell88 = row.createCell(4);

						if (record1.getR40_no_of_accts() != null) {
							cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
							cell88.setCellStyle(numberStyle);
						} else {
							cell88.setCellValue("");
							cell88.setCellStyle(textStyle);
						}

						row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
						Cell cell89 = row.createCell(2);

						if (record1.getR41_collateral_amount() != null) {
							cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
							cell89.setCellStyle(numberStyle);
						} else {
							cell89.setCellValue("");
							cell89.setCellStyle(textStyle);
						}

						Cell cell90 = row.createCell(3);

						if (record1.getR41_carrying_amount() != null) {
							cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
							cell90.setCellStyle(numberStyle);
						} else {
							cell90.setCellValue("");
							cell90.setCellStyle(textStyle);
						}

						Cell cell91 = row.createCell(4);

						if (record1.getR41_no_of_accts() != null) {
							cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
							cell91.setCellStyle(numberStyle);
						} else {
							cell91.setCellValue("");
							cell91.setCellStyle(textStyle);
						}

						row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
						Cell cell92 = row.createCell(2);

						if (record1.getR42_collateral_amount() != null) {
							cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
							cell92.setCellStyle(numberStyle);
						} else {
							cell92.setCellValue("");
							cell92.setCellStyle(textStyle);
						}

						Cell cell93 = row.createCell(3);

						if (record1.getR42_carrying_amount() != null) {
							cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
							cell93.setCellStyle(numberStyle);
						} else {
							cell93.setCellValue("");
							cell93.setCellStyle(textStyle);
						}

						Cell cell94 = row.createCell(4);

						if (record1.getR42_no_of_accts() != null) {
							cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
							cell94.setCellStyle(numberStyle);
						} else {
							cell94.setCellValue("");
							cell94.setCellStyle(textStyle);
						}

						row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
						Cell cell95 = row.createCell(2);

						if (record1.getR43_collateral_amount() != null) {
							cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
							cell95.setCellStyle(numberStyle);
						} else {
							cell95.setCellValue("");
							cell95.setCellStyle(textStyle);
						}

						Cell cell96 = row.createCell(3);

						if (record1.getR43_carrying_amount() != null) {
							cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
							cell96.setCellStyle(numberStyle);
						} else {
							cell96.setCellValue("");
							cell96.setCellStyle(textStyle);
						}

						Cell cell97 = row.createCell(5);

						if (record1.getR43_no_of_accts() != null) {
							cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
							cell97.setCellStyle(numberStyle);
						} else {
							cell97.setCellValue("");
							cell97.setCellStyle(textStyle);
						}

						row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
						Cell cell98 = row.createCell(2);

						if (record1.getR44_collateral_amount() != null) {
							cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
							cell98.setCellStyle(numberStyle);
						} else {
							cell98.setCellValue("");
							cell98.setCellStyle(textStyle);
						}

						Cell cell99 = row.createCell(3);

						if (record1.getR44_carrying_amount() != null) {
							cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
							cell99.setCellStyle(numberStyle);
						} else {
							cell99.setCellValue("");
							cell99.setCellStyle(textStyle);
						}

						Cell cell100 = row.createCell(5);

						if (record1.getR44_no_of_accts() != null) {
							cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
							cell100.setCellStyle(numberStyle);
						} else {
							cell100.setCellValue("");
							cell100.setCellStyle(textStyle);
						}

						row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
						Cell cell101 = row.createCell(2);

						if (record1.getR45_collateral_amount() != null) {
							cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
							cell101.setCellStyle(numberStyle);
						} else {
							cell101.setCellValue("");
							cell101.setCellStyle(textStyle);
						}

						Cell cell102 = row.createCell(3);

						if (record1.getR45_carrying_amount() != null) {
							cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
							cell102.setCellStyle(numberStyle);
						} else {
							cell102.setCellValue("");
							cell102.setCellStyle(textStyle);
						}

						Cell cell103 = row.createCell(5);

						if (record1.getR45_no_of_accts() != null) {
							cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
							cell103.setCellStyle(numberStyle);
						} else {
							cell103.setCellValue("");
							cell103.setCellStyle(textStyle);
						}

						row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
						Cell cell104 = row.createCell(2);

						if (record1.getR47_collateral_amount() != null) {
							cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
							cell104.setCellStyle(numberStyle);
						} else {
							cell104.setCellValue("");
							cell104.setCellStyle(textStyle);
						}

						Cell cell105 = row.createCell(3);

						if (record1.getR47_carrying_amount() != null) {
							cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
							cell105.setCellStyle(numberStyle);
						} else {
							cell105.setCellValue("");
							cell105.setCellStyle(textStyle);
						}

						Cell cell106 = row.createCell(5);

						if (record1.getR47_no_of_accts() != null) {
							cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
							cell106.setCellStyle(numberStyle);
						} else {
							cell106.setCellValue("");
							cell106.setCellStyle(textStyle);
						}

						row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
						Cell cell107 = row.createCell(2);

						if (record1.getR48_collateral_amount() != null) {
							cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
							cell107.setCellStyle(numberStyle);
						} else {
							cell107.setCellValue("");
							cell107.setCellStyle(textStyle);
						}

						Cell cell108 = row.createCell(3);

						if (record1.getR48_carrying_amount() != null) {
							cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
							cell108.setCellStyle(numberStyle);
						} else {
							cell108.setCellValue("");
							cell108.setCellStyle(textStyle);
						}

						Cell cell109 = row.createCell(5);

						if (record1.getR48_no_of_accts() != null) {
							cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
							cell109.setCellStyle(numberStyle);
						} else {
							cell109.setCellValue("");
							cell109.setCellStyle(textStyle);
						}

						row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
						Cell cell110 = row.createCell(2);

						if (record1.getR49_collateral_amount() != null) {
							cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
							cell110.setCellStyle(numberStyle);
						} else {
							cell110.setCellValue("");
							cell110.setCellStyle(textStyle);
						}

						Cell cell111 = row.createCell(3);

						if (record1.getR49_carrying_amount() != null) {
							cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
							cell111.setCellStyle(numberStyle);
						} else {
							cell111.setCellValue("");
							cell111.setCellStyle(textStyle);
						}

						Cell cell112 = row.createCell(5);

						if (record1.getR49_no_of_accts() != null) {
							cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
							cell112.setCellStyle(numberStyle);
						} else {
							cell112.setCellValue("");
							cell112.setCellStyle(textStyle);
						}

						row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
						Cell cell113 = row.createCell(2);

						if (record1.getR50_collateral_amount() != null) {
							cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
							cell113.setCellStyle(numberStyle);
						} else {
							cell113.setCellValue("");
							cell113.setCellStyle(textStyle);
						}

						Cell cell114 = row.createCell(3);

						if (record1.getR50_carrying_amount() != null) {
							cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
							cell114.setCellStyle(numberStyle);
						} else {
							cell114.setCellValue("");
							cell114.setCellStyle(textStyle);
						}

						Cell cell115 = row.createCell(5);

						if (record1.getR50_no_of_accts() != null) {
							cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
							cell115.setCellStyle(numberStyle);
						} else {
							cell115.setCellValue("");
							cell115.setCellStyle(textStyle);
						}

						row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
						Cell cell116 = row.createCell(2);

						if (record1.getR52_collateral_amount() != null) {
							cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
							cell116.setCellStyle(numberStyle);
						} else {
							cell116.setCellValue("");
							cell116.setCellStyle(textStyle);
						}

						Cell cell117 = row.createCell(3);

						if (record1.getR52_carrying_amount() != null) {
							cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
							cell117.setCellStyle(numberStyle);
						} else {
							cell117.setCellValue("");
							cell117.setCellStyle(textStyle);
						}

						Cell cell118 = row.createCell(5);

						if (record1.getR52_no_of_accts() != null) {
							cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
							cell118.setCellStyle(numberStyle);
						} else {
							cell118.setCellValue("");
							cell118.setCellStyle(textStyle);
						}

						row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
						Cell cell119 = row.createCell(2);

						if (record1.getR53_collateral_amount() != null) {
							cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
							cell119.setCellStyle(numberStyle);
						} else {
							cell119.setCellValue("");
							cell119.setCellStyle(textStyle);
						}

						Cell cell120 = row.createCell(3);

						if (record1.getR53_carrying_amount() != null) {
							cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
							cell120.setCellStyle(numberStyle);
						} else {
							cell120.setCellValue("");
							cell120.setCellStyle(textStyle);
						}

						Cell cell121 = row.createCell(5);

						if (record1.getR53_no_of_accts() != null) {
							cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
							cell121.setCellStyle(numberStyle);
						} else {
							cell121.setCellValue("");
							cell121.setCellStyle(textStyle);
						}

						row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
						Cell cell122 = row.createCell(2);

						if (record1.getR54_collateral_amount() != null) {
							cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
							cell122.setCellStyle(numberStyle);
						} else {
							cell122.setCellValue("");
							cell122.setCellStyle(textStyle);
						}

						Cell cell123 = row.createCell(3);

						if (record1.getR54_carrying_amount() != null) {
							cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
							cell123.setCellStyle(numberStyle);
						} else {
							cell123.setCellValue("");
							cell123.setCellStyle(textStyle);
						}

						Cell cell124 = row.createCell(5);

						if (record1.getR54_no_of_accts() != null) {
							cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
							cell124.setCellStyle(numberStyle);
						} else {
							cell124.setCellValue("");
							cell124.setCellStyle(textStyle);
						}

						row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
						Cell cell125 = row.createCell(2);

						if (record1.getR55_collateral_amount() != null) {
							cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
							cell125.setCellStyle(numberStyle);
						} else {
							cell125.setCellValue("");
							cell125.setCellStyle(textStyle);
						}

						Cell cell126 = row.createCell(3);

						if (record1.getR55_carrying_amount() != null) {
							cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
							cell126.setCellStyle(numberStyle);
						} else {
							cell126.setCellValue("");
							cell126.setCellStyle(textStyle);
						}

						Cell cell127 = row.createCell(5);

						if (record1.getR55_no_of_accts() != null) {
							cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
							cell127.setCellStyle(numberStyle);
						} else {
							cell127.setCellValue("");
							cell127.setCellStyle(textStyle);
						}

						row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
						Cell cell128 = row.createCell(2);

						if (record1.getR56_collateral_amount() != null) {
							cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
							cell128.setCellStyle(numberStyle);
						} else {
							cell128.setCellValue("");
							cell128.setCellStyle(textStyle);
						}

						Cell cell129 = row.createCell(3);

						if (record1.getR56_carrying_amount() != null) {
							cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
							cell129.setCellStyle(numberStyle);
						} else {
							cell129.setCellValue("");
							cell129.setCellStyle(textStyle);
						}

						Cell cell130 = row.createCell(5);

						if (record1.getR56_no_of_accts() != null) {
							cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
							cell130.setCellStyle(numberStyle);
						} else {
							cell130.setCellValue("");
							cell130.setCellStyle(textStyle);
						}

						row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
						Cell cell131 = row.createCell(2);

						if (record1.getR58_collateral_amount() != null) {
							cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
							cell131.setCellStyle(numberStyle);
						} else {
							cell131.setCellValue("");
							cell131.setCellStyle(textStyle);
						}

						Cell cell132 = row.createCell(3);

						if (record1.getR58_carrying_amount() != null) {
							cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
							cell132.setCellStyle(numberStyle);
						} else {
							cell132.setCellValue("");
							cell132.setCellStyle(textStyle);
						}

						Cell cell133 = row.createCell(5);

						if (record1.getR58_no_of_accts() != null) {
							cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
							cell133.setCellStyle(numberStyle);
						} else {
							cell133.setCellValue("");
							cell133.setCellStyle(textStyle);
						}

						row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
						Cell cell134 = row.createCell(2);

						if (record1.getR59_collateral_amount() != null) {
							cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
							cell134.setCellStyle(numberStyle);
						} else {
							cell134.setCellValue("");
							cell134.setCellStyle(textStyle);
						}

						Cell cell135 = row.createCell(3);

						if (record1.getR59_carrying_amount() != null) {
							cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
							cell135.setCellStyle(numberStyle);
						} else {
							cell135.setCellValue("");
							cell135.setCellStyle(textStyle);
						}

						Cell cell136 = row.createCell(5);

						if (record1.getR59_no_of_accts() != null) {
							cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
							cell136.setCellStyle(numberStyle);
						} else {
							cell136.setCellValue("");
							cell136.setCellStyle(textStyle);
						}

						row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
						Cell cell137 = row.createCell(2);

						if (record1.getR60_collateral_amount() != null) {
							cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
							cell137.setCellStyle(numberStyle);
						} else {
							cell137.setCellValue("");
							cell137.setCellStyle(textStyle);
						}

						Cell cell138 = row.createCell(3);

						if (record1.getR60_carrying_amount() != null) {
							cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
							cell138.setCellStyle(numberStyle);
						} else {
							cell138.setCellValue("");
							cell138.setCellStyle(textStyle);
						}

						Cell cell139 = row.createCell(5);

						if (record1.getR60_no_of_accts() != null) {
							cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
							cell139.setCellStyle(numberStyle);
						} else {
							cell139.setCellValue("");
							cell139.setCellStyle(textStyle);
						}

						row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
						Cell cell140 = row.createCell(2);

						if (record1.getR61_collateral_amount() != null) {
							cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
							cell140.setCellStyle(numberStyle);
						} else {
							cell140.setCellValue("");
							cell140.setCellStyle(textStyle);
						}

						Cell cell141 = row.createCell(3);

						if (record1.getR61_carrying_amount() != null) {
							cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
							cell141.setCellStyle(numberStyle);
						} else {
							cell141.setCellValue("");
							cell141.setCellStyle(textStyle);
						}

						Cell cell142 = row.createCell(5);

						if (record1.getR61_no_of_accts() != null) {
							cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
							cell142.setCellStyle(numberStyle);
						} else {
							cell142.setCellValue("");
							cell142.setCellStyle(textStyle);
						}

						row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
						Cell cell143 = row.createCell(2);

						if (record1.getR62_collateral_amount() != null) {
							cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
							cell143.setCellStyle(numberStyle);
						} else {
							cell143.setCellValue("");
							cell143.setCellStyle(textStyle);
						}

						Cell cell144 = row.createCell(3);

						if (record1.getR62_carrying_amount() != null) {
							cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
							cell144.setCellStyle(numberStyle);
						} else {
							cell144.setCellValue("");
							cell144.setCellStyle(textStyle);
						}

						Cell cell145 = row.createCell(5);

						if (record1.getR62_no_of_accts() != null) {
							cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
							cell145.setCellStyle(numberStyle);
						} else {
							cell145.setCellValue("");
							cell145.setCellStyle(textStyle);
						}

						row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
						Cell cell146 = row.createCell(2);

						if (record1.getR63_collateral_amount() != null) {
							cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
							cell146.setCellStyle(numberStyle);
						} else {
							cell146.setCellValue("");
							cell146.setCellStyle(textStyle);
						}

						Cell cell147 = row.createCell(3);

						if (record1.getR63_carrying_amount() != null) {
							cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
							cell147.setCellStyle(numberStyle);
						} else {
							cell147.setCellValue("");
							cell147.setCellStyle(textStyle);
						}

						Cell cell148 = row.createCell(5);

						if (record1.getR63_no_of_accts() != null) {
							cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
							cell148.setCellStyle(numberStyle);
						} else {
							cell148.setCellValue("");
							cell148.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 EMAIL SUMMARY", null,
							"BRRS_Q_RLFA1_SUMMARY_TABLE");
				}

				return out.toByteArray();
			}
		}
	}

//ARCHIVAL SUMMARY EXCEL  FORMAT
// Archival format excel
	public byte[] getExcelQ_RLFA1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_RLFA1ARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_RLFA1_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_RLFA1 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(1);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					// row11
					row = sheet.getRow(10);

					// Column 2 -B

					Cell cellB = row.createCell(1);
					if (record.getR11_collateral_amount() != null) {
						cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					Cell cellC = row.createCell(2);
					if (record.getR11_carrying_amount() != null) {
						cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4-D
					Cell cellD = row.createCell(3);
					if (record.getR11_no_of_accts() != null) {
						cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 -B

					cellB = row.createCell(1);
					if (record.getR12_collateral_amount() != null) {
						cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR12_carrying_amount() != null) {
						cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4-D
					cellD = row.createCell(3);
					if (record.getR12_no_of_accts() != null) {
						cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR13_collateral_amount() != null) {
						cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR13_carrying_amount() != null) {
						cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR13_no_of_accts() != null) {
						cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR15_collateral_amount() != null) {
						cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR15_carrying_amount() != null) {
						cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR15_no_of_accts() != null) {
						cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR16_collateral_amount() != null) {
						cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR16_carrying_amount() != null) {
						cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR16_no_of_accts() != null) {
						cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR17_collateral_amount() != null) {
						cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR17_carrying_amount() != null) {
						cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR17_no_of_accts() != null) {
						cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR18_collateral_amount() != null) {
						cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR18_carrying_amount() != null) {
						cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR18_no_of_accts() != null) {
						cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR19_collateral_amount() != null) {
						cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR19_carrying_amount() != null) {
						cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR19_no_of_accts() != null) {
						cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR20_collateral_amount() != null) {
						cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR20_carrying_amount() != null) {
						cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR20_no_of_accts() != null) {
						cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR21_collateral_amount() != null) {
						cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR21_carrying_amount() != null) {
						cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR21_no_of_accts() != null) {
						cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR22_collateral_amount() != null) {
						cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR22_carrying_amount() != null) {
						cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR22_no_of_accts() != null) {
						cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR23_collateral_amount() != null) {
						cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR23_carrying_amount() != null) {
						cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR23_no_of_accts() != null) {
						cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR24_collateral_amount() != null) {
						cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR24_carrying_amount() != null) {
						cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR24_no_of_accts() != null) {
						cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR25_collateral_amount() != null) {
						cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR25_carrying_amount() != null) {
						cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR25_no_of_accts() != null) {
						cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR26_collateral_amount() != null) {
						cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR26_carrying_amount() != null) {
						cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR26_no_of_accts() != null) {
						cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR27_collateral_amount() != null) {
						cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR27_carrying_amount() != null) {
						cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR27_no_of_accts() != null) {
						cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR29_collateral_amount() != null) {
						cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR29_carrying_amount() != null) {
						cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR29_no_of_accts() != null) {
						cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR30_collateral_amount() != null) {
						cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR30_carrying_amount() != null) {
						cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR30_no_of_accts() != null) {
						cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR31_collateral_amount() != null) {
						cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR31_carrying_amount() != null) {
						cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR31_no_of_accts() != null) {
						cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR32_collateral_amount() != null) {
						cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR32_carrying_amount() != null) {
						cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR32_no_of_accts() != null) {
						cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR33_collateral_amount() != null) {
						cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR33_carrying_amount() != null) {
						cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR33_no_of_accts() != null) {
						cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR34_collateral_amount() != null) {
						cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR34_carrying_amount() != null) {
						cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR34_no_of_accts() != null) {
						cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR35_collateral_amount() != null) {
						cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR35_carrying_amount() != null) {
						cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR35_no_of_accts() != null) {
						cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR36_collateral_amount() != null) {
						cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR36_carrying_amount() != null) {
						cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR36_no_of_accts() != null) {
						cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR38_collateral_amount() != null) {
						cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR38_carrying_amount() != null) {
						cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR38_no_of_accts() != null) {
						cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR39_collateral_amount() != null) {
						cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR39_carrying_amount() != null) {
						cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR39_no_of_accts() != null) {
						cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR41_collateral_amount() != null) {
						cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR41_carrying_amount() != null) {
						cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR41_no_of_accts() != null) {
						cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR42_collateral_amount() != null) {
						cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR42_carrying_amount() != null) {
						cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR42_no_of_accts() != null) {
						cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR44_collateral_amount() != null) {
						cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR44_carrying_amount() != null) {
						cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR44_no_of_accts() != null) {
						cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR45_collateral_amount() != null) {
						cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR45_carrying_amount() != null) {
						cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR45_no_of_accts() != null) {
						cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR46_collateral_amount() != null) {
						cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR46_carrying_amount() != null) {
						cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR46_no_of_accts() != null) {
						cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR47_collateral_amount() != null) {
						cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR47_carrying_amount() != null) {
						cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR47_no_of_accts() != null) {
						cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR49_collateral_amount() != null) {
						cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR49_carrying_amount() != null) {
						cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR49_no_of_accts() != null) {
						cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR50_collateral_amount() != null) {
						cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR50_carrying_amount() != null) {
						cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR50_no_of_accts() != null) {
						cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR51_collateral_amount() != null) {
						cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR51_carrying_amount() != null) {
						cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR51_no_of_accts() != null) {
						cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR53_collateral_amount() != null) {
						cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR53_carrying_amount() != null) {
						cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR53_no_of_accts() != null) {
						cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR54_collateral_amount() != null) {
						cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR54_carrying_amount() != null) {
						cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR54_no_of_accts() != null) {
						cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR55_collateral_amount() != null) {
						cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR55_carrying_amount() != null) {
						cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR55_no_of_accts() != null) {
						cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR57_collateral_amount() != null) {
						cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR57_carrying_amount() != null) {
						cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR57_no_of_accts() != null) {
						cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR58_collateral_amount() != null) {
						cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR58_carrying_amount() != null) {
						cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR58_no_of_accts() != null) {
						cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR59_collateral_amount() != null) {
						cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR59_carrying_amount() != null) {
						cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR59_no_of_accts() != null) {
						cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR60_collateral_amount() != null) {
						cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR60_carrying_amount() != null) {
						cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR60_no_of_accts() != null) {
						cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR61_collateral_amount() != null) {
						cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR61_carrying_amount() != null) {
						cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR61_no_of_accts() != null) {
						cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR62_collateral_amount() != null) {
						cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR62_carrying_amount() != null) {
						cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR62_no_of_accts() != null) {
						cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 ARCHIVAL SUMMARY", null,
						"BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

//ARCHIVAL SUMMARY EXCEL  EMAIL

// Archival Email Excel
	public byte[] BRRS_Q_RLFA1ARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_RLFA1_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_RLFA1 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Archival_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record1.getReport_date() != null) {

						R12Cell.setCellValue(record1.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
					Cell cell2 = row.createCell(2);

					if (record1.getR10_collateral_amount() != null) {
						cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);

					if (record1.getR10_carrying_amount() != null) {
						cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(5);

					if (record1.getR10_no_of_accts() != null) {
						cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
					Cell cell5 = row.createCell(2);

					if (record1.getR11_collateral_amount() != null) {
						cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(3);

					if (record1.getR11_carrying_amount() != null) {
						cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell7 = row.createCell(5);

					if (record1.getR11_no_of_accts() != null) {
						cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
					Cell cell8 = row.createCell(2);

					if (record1.getR12_collateral_amount() != null) {
						cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					Cell cell9 = row.createCell(3);

					if (record1.getR12_carrying_amount() != null) {
						cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					Cell cell10 = row.createCell(5);

					if (record1.getR12_no_of_accts() != null) {
						cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
					Cell cell11 = row.createCell(2);

					if (record1.getR13_collateral_amount() != null) {
						cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					Cell cell12 = row.createCell(3);

					if (record1.getR13_carrying_amount() != null) {
						cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					Cell cell13 = row.createCell(5);

					if (record1.getR13_no_of_accts() != null) {
						cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
					Cell cell14 = row.createCell(2);

					if (record1.getR14_collateral_amount() != null) {
						cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					Cell cell15 = row.createCell(3);

					if (record1.getR14_carrying_amount() != null) {
						cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					Cell cell16 = row.createCell(5);

					if (record1.getR14_no_of_accts() != null) {
						cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
					Cell cell17 = row.createCell(2);

					if (record1.getR15_collateral_amount() != null) {
						cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					Cell cell18 = row.createCell(3);

					if (record1.getR15_carrying_amount() != null) {
						cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
						cell18.setCellStyle(numberStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					Cell cell19 = row.createCell(5);

					if (record1.getR15_no_of_accts() != null) {
						cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
						cell19.setCellStyle(numberStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
					Cell cell20 = row.createCell(2);

					if (record1.getR16_collateral_amount() != null) {
						cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					Cell cell21 = row.createCell(3);

					if (record1.getR16_carrying_amount() != null) {
						cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					Cell cell22 = row.createCell(5);

					if (record1.getR16_no_of_accts() != null) {
						cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
					Cell cell23 = row.createCell(2);

					if (record1.getR17_collateral_amount() != null) {
						cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					Cell cell24 = row.createCell(3);

					if (record1.getR17_carrying_amount() != null) {
						cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					Cell cell25 = row.createCell(5);

					if (record1.getR17_no_of_accts() != null) {
						cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
						cell25.setCellStyle(numberStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
					Cell cell26 = row.createCell(2);

					if (record1.getR18_collateral_amount() != null) {
						cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					Cell cell27 = row.createCell(3);

					if (record1.getR18_carrying_amount() != null) {
						cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
						cell27.setCellStyle(numberStyle);
					} else {
						cell27.setCellValue("");
						cell27.setCellStyle(textStyle);
					}

					Cell cell28 = row.createCell(5);

					if (record1.getR18_no_of_accts() != null) {
						cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
						cell28.setCellStyle(numberStyle);
					} else {
						cell28.setCellValue("");
						cell28.setCellStyle(textStyle);
					}

					row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
					Cell cell29 = row.createCell(2);

					if (record1.getR19_collateral_amount() != null) {
						cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
						cell29.setCellStyle(numberStyle);
					} else {
						cell29.setCellValue("");
						cell29.setCellStyle(textStyle);
					}

					Cell cell30 = row.createCell(3);

					if (record1.getR19_carrying_amount() != null) {
						cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
						cell30.setCellStyle(numberStyle);
					} else {
						cell30.setCellValue("");
						cell30.setCellStyle(textStyle);
					}

					Cell cell31 = row.createCell(4);

					if (record1.getR19_no_of_accts() != null) {
						cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
						cell31.setCellStyle(numberStyle);
					} else {
						cell31.setCellValue("");
						cell31.setCellStyle(textStyle);
					}

					row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
					Cell cell32 = row.createCell(2);

					if (record1.getR20_collateral_amount() != null) {
						cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
						cell32.setCellStyle(numberStyle);
					} else {
						cell32.setCellValue("");
						cell32.setCellStyle(textStyle);
					}

					Cell cell33 = row.createCell(3);

					if (record1.getR20_carrying_amount() != null) {
						cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
						cell33.setCellStyle(numberStyle);
					} else {
						cell33.setCellValue("");
						cell33.setCellStyle(textStyle);
					}

					Cell cell34 = row.createCell(4);

					if (record1.getR20_no_of_accts() != null) {
						cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
						cell34.setCellStyle(numberStyle);
					} else {
						cell34.setCellValue("");
						cell34.setCellStyle(textStyle);
					}

					row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
					Cell cell35 = row.createCell(2);

					if (record1.getR21_collateral_amount() != null) {
						cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
						cell35.setCellStyle(numberStyle);
					} else {
						cell35.setCellValue("");
						cell35.setCellStyle(textStyle);
					}

					Cell cell36 = row.createCell(3);

					if (record1.getR21_carrying_amount() != null) {
						cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
						cell36.setCellStyle(numberStyle);
					} else {
						cell36.setCellValue("");
						cell36.setCellStyle(textStyle);
					}

					Cell cell37 = row.createCell(4);

					if (record1.getR21_no_of_accts() != null) {
						cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
						cell37.setCellStyle(numberStyle);
					} else {
						cell37.setCellValue("");
						cell37.setCellStyle(textStyle);
					}

					row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
					Cell cell38 = row.createCell(2);

					if (record1.getR22_collateral_amount() != null) {
						cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
						cell38.setCellStyle(numberStyle);
					} else {
						cell38.setCellValue("");
						cell38.setCellStyle(textStyle);
					}

					Cell cell39 = row.createCell(3);

					if (record1.getR22_carrying_amount() != null) {
						cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
						cell39.setCellStyle(numberStyle);
					} else {
						cell39.setCellValue("");
						cell39.setCellStyle(textStyle);
					}

					Cell cell40 = row.createCell(4);

					if (record1.getR22_no_of_accts() != null) {
						cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
						cell40.setCellStyle(numberStyle);
					} else {
						cell40.setCellValue("");
						cell40.setCellStyle(textStyle);
					}

					row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
					Cell cell41 = row.createCell(2);

					if (record1.getR23_collateral_amount() != null) {
						cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
						cell41.setCellStyle(numberStyle);
					} else {
						cell41.setCellValue("");
						cell41.setCellStyle(textStyle);
					}

					Cell cell42 = row.createCell(3);

					if (record1.getR23_carrying_amount() != null) {
						cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
						cell42.setCellStyle(numberStyle);
					} else {
						cell42.setCellValue("");
						cell42.setCellStyle(textStyle);
					}

					Cell cell43 = row.createCell(4);

					if (record1.getR23_no_of_accts() != null) {
						cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
						cell43.setCellStyle(numberStyle);
					} else {
						cell43.setCellValue("");
						cell43.setCellStyle(textStyle);
					}

					row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
					Cell cell44 = row.createCell(2);

					if (record1.getR24_collateral_amount() != null) {
						cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
						cell44.setCellStyle(numberStyle);
					} else {
						cell44.setCellValue("");
						cell44.setCellStyle(textStyle);
					}

					Cell cell45 = row.createCell(3);

					if (record1.getR24_carrying_amount() != null) {
						cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
						cell45.setCellStyle(numberStyle);
					} else {
						cell45.setCellValue("");
						cell45.setCellStyle(textStyle);
					}

					Cell cell46 = row.createCell(4);

					if (record1.getR24_no_of_accts() != null) {
						cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
						cell46.setCellStyle(numberStyle);
					} else {
						cell46.setCellValue("");
						cell46.setCellStyle(textStyle);
					}

					row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
					Cell cell47 = row.createCell(2);

					if (record1.getR25_collateral_amount() != null) {
						cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
						cell47.setCellStyle(numberStyle);
					} else {
						cell47.setCellValue("");
						cell47.setCellStyle(textStyle);
					}

					Cell cell48 = row.createCell(3);

					if (record1.getR25_carrying_amount() != null) {
						cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
						cell48.setCellStyle(numberStyle);
					} else {
						cell48.setCellValue("");
						cell48.setCellStyle(textStyle);
					}

					Cell cell49 = row.createCell(4);

					if (record1.getR25_no_of_accts() != null) {
						cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
						cell49.setCellStyle(numberStyle);
					} else {
						cell49.setCellValue("");
						cell49.setCellStyle(textStyle);
					}

					row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
					Cell cell50 = row.createCell(2);

					if (record1.getR28_collateral_amount() != null) {
						cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
						cell50.setCellStyle(numberStyle);
					} else {
						cell50.setCellValue("");
						cell50.setCellStyle(textStyle);
					}

					Cell cell51 = row.createCell(3);

					if (record1.getR28_carrying_amount() != null) {
						cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
						cell51.setCellStyle(numberStyle);
					} else {
						cell51.setCellValue("");
						cell51.setCellStyle(textStyle);
					}

					Cell cell52 = row.createCell(4);

					if (record1.getR28_no_of_accts() != null) {
						cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
						cell52.setCellStyle(numberStyle);
					} else {
						cell52.setCellValue("");
						cell52.setCellStyle(textStyle);
					}

					row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
					Cell cell53 = row.createCell(2);

					if (record1.getR29_collateral_amount() != null) {
						cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
						cell53.setCellStyle(numberStyle);
					} else {
						cell53.setCellValue("");
						cell53.setCellStyle(textStyle);
					}

					Cell cell54 = row.createCell(3);

					if (record1.getR29_carrying_amount() != null) {
						cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
						cell54.setCellStyle(numberStyle);
					} else {
						cell54.setCellValue("");
						cell54.setCellStyle(textStyle);
					}

					Cell cell55 = row.createCell(4);

					if (record1.getR29_no_of_accts() != null) {
						cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
						cell55.setCellStyle(numberStyle);
					} else {
						cell55.setCellValue("");
						cell55.setCellStyle(textStyle);
					}

					row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
					Cell cell56 = row.createCell(2);

					if (record1.getR30_collateral_amount() != null) {
						cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
						cell56.setCellStyle(numberStyle);
					} else {
						cell56.setCellValue("");
						cell56.setCellStyle(textStyle);
					}

					Cell cell57 = row.createCell(3);

					if (record1.getR30_carrying_amount() != null) {
						cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
						cell57.setCellStyle(numberStyle);
					} else {
						cell57.setCellValue("");
						cell57.setCellStyle(textStyle);
					}

					Cell cell58 = row.createCell(4);

					if (record1.getR30_no_of_accts() != null) {
						cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
						cell58.setCellStyle(numberStyle);
					} else {
						cell58.setCellValue("");
						cell58.setCellStyle(textStyle);
					}

					row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
					Cell cell59 = row.createCell(2);

					if (record1.getR32_collateral_amount() != null) {
						cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
						cell59.setCellStyle(numberStyle);
					} else {
						cell59.setCellValue("");
						cell59.setCellStyle(textStyle);
					}

					Cell cell60 = row.createCell(3);

					if (record1.getR32_carrying_amount() != null) {
						cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
						cell60.setCellStyle(numberStyle);
					} else {
						cell60.setCellValue("");
						cell60.setCellStyle(textStyle);
					}

					Cell cell61 = row.createCell(4);

					if (record1.getR32_no_of_accts() != null) {
						cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
						cell61.setCellStyle(numberStyle);
					} else {
						cell61.setCellValue("");
						cell61.setCellStyle(textStyle);
					}

					row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
					Cell cell62 = row.createCell(2);

					if (record1.getR33_collateral_amount() != null) {
						cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
						cell62.setCellStyle(numberStyle);
					} else {
						cell62.setCellValue("");
						cell62.setCellStyle(textStyle);
					}

					Cell cell63 = row.createCell(3);

					if (record1.getR33_carrying_amount() != null) {
						cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
						cell63.setCellStyle(numberStyle);
					} else {
						cell63.setCellValue("");
						cell63.setCellStyle(textStyle);
					}

					Cell cell64 = row.createCell(4);

					if (record1.getR33_no_of_accts() != null) {
						cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
						cell64.setCellStyle(numberStyle);
					} else {
						cell64.setCellValue("");
						cell64.setCellStyle(textStyle);
					}

					row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
					Cell cell65 = row.createCell(2);

					if (record1.getR31_collateral_amount() != null) {
						cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
						cell65.setCellStyle(numberStyle);
					} else {
						cell65.setCellValue("");
						cell65.setCellStyle(textStyle);
					}

					Cell cell66 = row.createCell(3);

					if (record1.getR31_carrying_amount() != null) {
						cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
						cell66.setCellStyle(numberStyle);
					} else {
						cell66.setCellValue("");
						cell66.setCellStyle(textStyle);
					}

					Cell cell67 = row.createCell(4);

					if (record1.getR31_no_of_accts() != null) {
						cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
						cell67.setCellStyle(numberStyle);
					} else {
						cell67.setCellValue("");
						cell67.setCellStyle(textStyle);
					}

					row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
					Cell cell68 = row.createCell(2);

					if (record1.getR34_collateral_amount() != null) {
						cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
						cell68.setCellStyle(numberStyle);
					} else {
						cell68.setCellValue("");
						cell68.setCellStyle(textStyle);
					}

					Cell cell69 = row.createCell(3);

					if (record1.getR34_carrying_amount() != null) {
						cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
						cell69.setCellStyle(numberStyle);
					} else {
						cell69.setCellValue("");
						cell69.setCellStyle(textStyle);
					}

					Cell cell70 = row.createCell(4);

					if (record1.getR34_no_of_accts() != null) {
						cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
						cell70.setCellStyle(numberStyle);
					} else {
						cell70.setCellValue("");
						cell70.setCellStyle(textStyle);
					}

					row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
					Cell cell71 = row.createCell(2);

					if (record1.getR36_collateral_amount() != null) {
						cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
						cell71.setCellStyle(numberStyle);
					} else {
						cell71.setCellValue("");
						cell71.setCellStyle(textStyle);
					}

					Cell cell72 = row.createCell(3);

					if (record1.getR36_carrying_amount() != null) {
						cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
						cell72.setCellStyle(numberStyle);
					} else {
						cell72.setCellValue("");
						cell72.setCellStyle(textStyle);
					}

					Cell cell73 = row.createCell(4);

					if (record1.getR36_no_of_accts() != null) {
						cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
						cell73.setCellStyle(numberStyle);
					} else {
						cell73.setCellValue("");
						cell73.setCellStyle(textStyle);
					}

					row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
					Cell cell74 = row.createCell(2);

					if (record1.getR35_collateral_amount() != null) {
						cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
						cell74.setCellStyle(numberStyle);
					} else {
						cell74.setCellValue("");
						cell74.setCellStyle(textStyle);
					}

					Cell cell75 = row.createCell(3);

					if (record1.getR35_carrying_amount() != null) {
						cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
						cell75.setCellStyle(numberStyle);
					} else {
						cell75.setCellValue("");
						cell75.setCellStyle(textStyle);
					}

					Cell cell76 = row.createCell(4);

					if (record1.getR35_no_of_accts() != null) {
						cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
						cell76.setCellStyle(numberStyle);
					} else {
						cell76.setCellValue("");
						cell76.setCellStyle(textStyle);
					}

					row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
					Cell cell77 = row.createCell(2);

					if (record1.getR37_collateral_amount() != null) {
						cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
						cell77.setCellStyle(numberStyle);
					} else {
						cell77.setCellValue("");
						cell77.setCellStyle(textStyle);
					}

					Cell cell78 = row.createCell(3);

					if (record1.getR37_carrying_amount() != null) {
						cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
						cell78.setCellStyle(numberStyle);
					} else {
						cell78.setCellValue("");
						cell78.setCellStyle(textStyle);
					}

					Cell cell79 = row.createCell(4);

					if (record1.getR37_no_of_accts() != null) {
						cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
						cell79.setCellStyle(numberStyle);
					} else {
						cell79.setCellValue("");
						cell79.setCellStyle(textStyle);
					}

					row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
					Cell cell80 = row.createCell(2);

					if (record1.getR38_collateral_amount() != null) {
						cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
						cell80.setCellStyle(numberStyle);
					} else {
						cell80.setCellValue("");
						cell80.setCellStyle(textStyle);
					}

					Cell cell81 = row.createCell(3);

					if (record1.getR38_carrying_amount() != null) {
						cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
						cell81.setCellStyle(numberStyle);
					} else {
						cell81.setCellValue("");
						cell81.setCellStyle(textStyle);
					}

					Cell cell82 = row.createCell(4);

					if (record1.getR38_no_of_accts() != null) {
						cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
						cell82.setCellStyle(numberStyle);
					} else {
						cell82.setCellValue("");
						cell82.setCellStyle(textStyle);
					}

					row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
					Cell cell83 = row.createCell(2);

					if (record1.getR39_collateral_amount() != null) {
						cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
						cell83.setCellStyle(numberStyle);
					} else {
						cell83.setCellValue("");
						cell83.setCellStyle(textStyle);
					}

					Cell cell84 = row.createCell(3);

					if (record1.getR39_carrying_amount() != null) {
						cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
						cell84.setCellStyle(numberStyle);
					} else {
						cell84.setCellValue("");
						cell84.setCellStyle(textStyle);
					}

					Cell cell85 = row.createCell(4);

					if (record1.getR39_no_of_accts() != null) {
						cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
						cell85.setCellStyle(numberStyle);
					} else {
						cell85.setCellValue("");
						cell85.setCellStyle(textStyle);
					}

					row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
					Cell cell86 = row.createCell(2);

					if (record1.getR40_collateral_amount() != null) {
						cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
						cell86.setCellStyle(numberStyle);
					} else {
						cell86.setCellValue("");
						cell86.setCellStyle(textStyle);
					}

					Cell cell87 = row.createCell(3);

					if (record1.getR40_carrying_amount() != null) {
						cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
						cell87.setCellStyle(numberStyle);
					} else {
						cell87.setCellValue("");
						cell87.setCellStyle(textStyle);
					}

					Cell cell88 = row.createCell(4);

					if (record1.getR40_no_of_accts() != null) {
						cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
						cell88.setCellStyle(numberStyle);
					} else {
						cell88.setCellValue("");
						cell88.setCellStyle(textStyle);
					}

					row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
					Cell cell89 = row.createCell(2);

					if (record1.getR41_collateral_amount() != null) {
						cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
						cell89.setCellStyle(numberStyle);
					} else {
						cell89.setCellValue("");
						cell89.setCellStyle(textStyle);
					}

					Cell cell90 = row.createCell(3);

					if (record1.getR41_carrying_amount() != null) {
						cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
						cell90.setCellStyle(numberStyle);
					} else {
						cell90.setCellValue("");
						cell90.setCellStyle(textStyle);
					}

					Cell cell91 = row.createCell(4);

					if (record1.getR41_no_of_accts() != null) {
						cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
						cell91.setCellStyle(numberStyle);
					} else {
						cell91.setCellValue("");
						cell91.setCellStyle(textStyle);
					}

					row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
					Cell cell92 = row.createCell(2);

					if (record1.getR42_collateral_amount() != null) {
						cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
						cell92.setCellStyle(numberStyle);
					} else {
						cell92.setCellValue("");
						cell92.setCellStyle(textStyle);
					}

					Cell cell93 = row.createCell(3);

					if (record1.getR42_carrying_amount() != null) {
						cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
						cell93.setCellStyle(numberStyle);
					} else {
						cell93.setCellValue("");
						cell93.setCellStyle(textStyle);
					}

					Cell cell94 = row.createCell(4);

					if (record1.getR42_no_of_accts() != null) {
						cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
						cell94.setCellStyle(numberStyle);
					} else {
						cell94.setCellValue("");
						cell94.setCellStyle(textStyle);
					}

					row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
					Cell cell95 = row.createCell(2);

					if (record1.getR43_collateral_amount() != null) {
						cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
						cell95.setCellStyle(numberStyle);
					} else {
						cell95.setCellValue("");
						cell95.setCellStyle(textStyle);
					}

					Cell cell96 = row.createCell(3);

					if (record1.getR43_carrying_amount() != null) {
						cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
						cell96.setCellStyle(numberStyle);
					} else {
						cell96.setCellValue("");
						cell96.setCellStyle(textStyle);
					}

					Cell cell97 = row.createCell(5);

					if (record1.getR43_no_of_accts() != null) {
						cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
						cell97.setCellStyle(numberStyle);
					} else {
						cell97.setCellValue("");
						cell97.setCellStyle(textStyle);
					}

					row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
					Cell cell98 = row.createCell(2);

					if (record1.getR44_collateral_amount() != null) {
						cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
						cell98.setCellStyle(numberStyle);
					} else {
						cell98.setCellValue("");
						cell98.setCellStyle(textStyle);
					}

					Cell cell99 = row.createCell(3);

					if (record1.getR44_carrying_amount() != null) {
						cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
						cell99.setCellStyle(numberStyle);
					} else {
						cell99.setCellValue("");
						cell99.setCellStyle(textStyle);
					}

					Cell cell100 = row.createCell(5);

					if (record1.getR44_no_of_accts() != null) {
						cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
						cell100.setCellStyle(numberStyle);
					} else {
						cell100.setCellValue("");
						cell100.setCellStyle(textStyle);
					}

					row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
					Cell cell101 = row.createCell(2);

					if (record1.getR45_collateral_amount() != null) {
						cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
						cell101.setCellStyle(numberStyle);
					} else {
						cell101.setCellValue("");
						cell101.setCellStyle(textStyle);
					}

					Cell cell102 = row.createCell(3);

					if (record1.getR45_carrying_amount() != null) {
						cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
						cell102.setCellStyle(numberStyle);
					} else {
						cell102.setCellValue("");
						cell102.setCellStyle(textStyle);
					}

					Cell cell103 = row.createCell(5);

					if (record1.getR45_no_of_accts() != null) {
						cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
						cell103.setCellStyle(numberStyle);
					} else {
						cell103.setCellValue("");
						cell103.setCellStyle(textStyle);
					}

					row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
					Cell cell104 = row.createCell(2);

					if (record1.getR47_collateral_amount() != null) {
						cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
						cell104.setCellStyle(numberStyle);
					} else {
						cell104.setCellValue("");
						cell104.setCellStyle(textStyle);
					}

					Cell cell105 = row.createCell(3);

					if (record1.getR47_carrying_amount() != null) {
						cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
						cell105.setCellStyle(numberStyle);
					} else {
						cell105.setCellValue("");
						cell105.setCellStyle(textStyle);
					}

					Cell cell106 = row.createCell(5);

					if (record1.getR47_no_of_accts() != null) {
						cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
						cell106.setCellStyle(numberStyle);
					} else {
						cell106.setCellValue("");
						cell106.setCellStyle(textStyle);
					}

					row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
					Cell cell107 = row.createCell(2);

					if (record1.getR48_collateral_amount() != null) {
						cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
						cell107.setCellStyle(numberStyle);
					} else {
						cell107.setCellValue("");
						cell107.setCellStyle(textStyle);
					}

					Cell cell108 = row.createCell(3);

					if (record1.getR48_carrying_amount() != null) {
						cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
						cell108.setCellStyle(numberStyle);
					} else {
						cell108.setCellValue("");
						cell108.setCellStyle(textStyle);
					}

					Cell cell109 = row.createCell(5);

					if (record1.getR48_no_of_accts() != null) {
						cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
						cell109.setCellStyle(numberStyle);
					} else {
						cell109.setCellValue("");
						cell109.setCellStyle(textStyle);
					}

					row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
					Cell cell110 = row.createCell(2);

					if (record1.getR49_collateral_amount() != null) {
						cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
						cell110.setCellStyle(numberStyle);
					} else {
						cell110.setCellValue("");
						cell110.setCellStyle(textStyle);
					}

					Cell cell111 = row.createCell(3);

					if (record1.getR49_carrying_amount() != null) {
						cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
						cell111.setCellStyle(numberStyle);
					} else {
						cell111.setCellValue("");
						cell111.setCellStyle(textStyle);
					}

					Cell cell112 = row.createCell(5);

					if (record1.getR49_no_of_accts() != null) {
						cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
						cell112.setCellStyle(numberStyle);
					} else {
						cell112.setCellValue("");
						cell112.setCellStyle(textStyle);
					}

					row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
					Cell cell113 = row.createCell(2);

					if (record1.getR50_collateral_amount() != null) {
						cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
						cell113.setCellStyle(numberStyle);
					} else {
						cell113.setCellValue("");
						cell113.setCellStyle(textStyle);
					}

					Cell cell114 = row.createCell(3);

					if (record1.getR50_carrying_amount() != null) {
						cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
						cell114.setCellStyle(numberStyle);
					} else {
						cell114.setCellValue("");
						cell114.setCellStyle(textStyle);
					}

					Cell cell115 = row.createCell(5);

					if (record1.getR50_no_of_accts() != null) {
						cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
						cell115.setCellStyle(numberStyle);
					} else {
						cell115.setCellValue("");
						cell115.setCellStyle(textStyle);
					}

					row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
					Cell cell116 = row.createCell(2);

					if (record1.getR52_collateral_amount() != null) {
						cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
						cell116.setCellStyle(numberStyle);
					} else {
						cell116.setCellValue("");
						cell116.setCellStyle(textStyle);
					}

					Cell cell117 = row.createCell(3);

					if (record1.getR52_carrying_amount() != null) {
						cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
						cell117.setCellStyle(numberStyle);
					} else {
						cell117.setCellValue("");
						cell117.setCellStyle(textStyle);
					}

					Cell cell118 = row.createCell(5);

					if (record1.getR52_no_of_accts() != null) {
						cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
						cell118.setCellStyle(numberStyle);
					} else {
						cell118.setCellValue("");
						cell118.setCellStyle(textStyle);
					}

					row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
					Cell cell119 = row.createCell(2);

					if (record1.getR53_collateral_amount() != null) {
						cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
						cell119.setCellStyle(numberStyle);
					} else {
						cell119.setCellValue("");
						cell119.setCellStyle(textStyle);
					}

					Cell cell120 = row.createCell(3);

					if (record1.getR53_carrying_amount() != null) {
						cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
						cell120.setCellStyle(numberStyle);
					} else {
						cell120.setCellValue("");
						cell120.setCellStyle(textStyle);
					}

					Cell cell121 = row.createCell(5);

					if (record1.getR53_no_of_accts() != null) {
						cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
						cell121.setCellStyle(numberStyle);
					} else {
						cell121.setCellValue("");
						cell121.setCellStyle(textStyle);
					}

					row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
					Cell cell122 = row.createCell(2);

					if (record1.getR54_collateral_amount() != null) {
						cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
						cell122.setCellStyle(numberStyle);
					} else {
						cell122.setCellValue("");
						cell122.setCellStyle(textStyle);
					}

					Cell cell123 = row.createCell(3);

					if (record1.getR54_carrying_amount() != null) {
						cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
						cell123.setCellStyle(numberStyle);
					} else {
						cell123.setCellValue("");
						cell123.setCellStyle(textStyle);
					}

					Cell cell124 = row.createCell(5);

					if (record1.getR54_no_of_accts() != null) {
						cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
						cell124.setCellStyle(numberStyle);
					} else {
						cell124.setCellValue("");
						cell124.setCellStyle(textStyle);
					}

					row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
					Cell cell125 = row.createCell(2);

					if (record1.getR55_collateral_amount() != null) {
						cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
						cell125.setCellStyle(numberStyle);
					} else {
						cell125.setCellValue("");
						cell125.setCellStyle(textStyle);
					}

					Cell cell126 = row.createCell(3);

					if (record1.getR55_carrying_amount() != null) {
						cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
						cell126.setCellStyle(numberStyle);
					} else {
						cell126.setCellValue("");
						cell126.setCellStyle(textStyle);
					}

					Cell cell127 = row.createCell(5);

					if (record1.getR55_no_of_accts() != null) {
						cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
						cell127.setCellStyle(numberStyle);
					} else {
						cell127.setCellValue("");
						cell127.setCellStyle(textStyle);
					}

					row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
					Cell cell128 = row.createCell(2);

					if (record1.getR56_collateral_amount() != null) {
						cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
						cell128.setCellStyle(numberStyle);
					} else {
						cell128.setCellValue("");
						cell128.setCellStyle(textStyle);
					}

					Cell cell129 = row.createCell(3);

					if (record1.getR56_carrying_amount() != null) {
						cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
						cell129.setCellStyle(numberStyle);
					} else {
						cell129.setCellValue("");
						cell129.setCellStyle(textStyle);
					}

					Cell cell130 = row.createCell(5);

					if (record1.getR56_no_of_accts() != null) {
						cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
						cell130.setCellStyle(numberStyle);
					} else {
						cell130.setCellValue("");
						cell130.setCellStyle(textStyle);
					}

					row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
					Cell cell131 = row.createCell(2);

					if (record1.getR58_collateral_amount() != null) {
						cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
						cell131.setCellStyle(numberStyle);
					} else {
						cell131.setCellValue("");
						cell131.setCellStyle(textStyle);
					}

					Cell cell132 = row.createCell(3);

					if (record1.getR58_carrying_amount() != null) {
						cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
						cell132.setCellStyle(numberStyle);
					} else {
						cell132.setCellValue("");
						cell132.setCellStyle(textStyle);
					}

					Cell cell133 = row.createCell(5);

					if (record1.getR58_no_of_accts() != null) {
						cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
						cell133.setCellStyle(numberStyle);
					} else {
						cell133.setCellValue("");
						cell133.setCellStyle(textStyle);
					}

					row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
					Cell cell134 = row.createCell(2);

					if (record1.getR59_collateral_amount() != null) {
						cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
						cell134.setCellStyle(numberStyle);
					} else {
						cell134.setCellValue("");
						cell134.setCellStyle(textStyle);
					}

					Cell cell135 = row.createCell(3);

					if (record1.getR59_carrying_amount() != null) {
						cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
						cell135.setCellStyle(numberStyle);
					} else {
						cell135.setCellValue("");
						cell135.setCellStyle(textStyle);
					}

					Cell cell136 = row.createCell(5);

					if (record1.getR59_no_of_accts() != null) {
						cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
						cell136.setCellStyle(numberStyle);
					} else {
						cell136.setCellValue("");
						cell136.setCellStyle(textStyle);
					}

					row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
					Cell cell137 = row.createCell(2);

					if (record1.getR60_collateral_amount() != null) {
						cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
						cell137.setCellStyle(numberStyle);
					} else {
						cell137.setCellValue("");
						cell137.setCellStyle(textStyle);
					}

					Cell cell138 = row.createCell(3);

					if (record1.getR60_carrying_amount() != null) {
						cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
						cell138.setCellStyle(numberStyle);
					} else {
						cell138.setCellValue("");
						cell138.setCellStyle(textStyle);
					}

					Cell cell139 = row.createCell(5);

					if (record1.getR60_no_of_accts() != null) {
						cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
						cell139.setCellStyle(numberStyle);
					} else {
						cell139.setCellValue("");
						cell139.setCellStyle(textStyle);
					}

					row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
					Cell cell140 = row.createCell(2);

					if (record1.getR61_collateral_amount() != null) {
						cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
						cell140.setCellStyle(numberStyle);
					} else {
						cell140.setCellValue("");
						cell140.setCellStyle(textStyle);
					}

					Cell cell141 = row.createCell(3);

					if (record1.getR61_carrying_amount() != null) {
						cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
						cell141.setCellStyle(numberStyle);
					} else {
						cell141.setCellValue("");
						cell141.setCellStyle(textStyle);
					}

					Cell cell142 = row.createCell(5);

					if (record1.getR61_no_of_accts() != null) {
						cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
						cell142.setCellStyle(numberStyle);
					} else {
						cell142.setCellValue("");
						cell142.setCellStyle(textStyle);
					}

					row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
					Cell cell143 = row.createCell(2);

					if (record1.getR62_collateral_amount() != null) {
						cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
						cell143.setCellStyle(numberStyle);
					} else {
						cell143.setCellValue("");
						cell143.setCellStyle(textStyle);
					}

					Cell cell144 = row.createCell(3);

					if (record1.getR62_carrying_amount() != null) {
						cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
						cell144.setCellStyle(numberStyle);
					} else {
						cell144.setCellValue("");
						cell144.setCellStyle(textStyle);
					}

					Cell cell145 = row.createCell(5);

					if (record1.getR62_no_of_accts() != null) {
						cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
						cell145.setCellStyle(numberStyle);
					} else {
						cell145.setCellValue("");
						cell145.setCellStyle(textStyle);
					}

					row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
					Cell cell146 = row.createCell(2);

					if (record1.getR63_collateral_amount() != null) {
						cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
						cell146.setCellStyle(numberStyle);
					} else {
						cell146.setCellValue("");
						cell146.setCellStyle(textStyle);
					}

					Cell cell147 = row.createCell(3);

					if (record1.getR63_carrying_amount() != null) {
						cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
						cell147.setCellStyle(numberStyle);
					} else {
						cell147.setCellValue("");
						cell147.setCellStyle(textStyle);
					}

					Cell cell148 = row.createCell(5);

					if (record1.getR63_no_of_accts() != null) {
						cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
						cell148.setCellStyle(numberStyle);
					} else {
						cell148.setCellValue("");
						cell148.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_Q_RLFA1_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

// RESUB EXCEL  FORMAT

	// Resub Format excel
	public byte[] BRRS_Q_RLFA1ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_RLFA1EmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_RLFA1_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_RLFA1 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Resub_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(1);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					// row11
					row = sheet.getRow(10);

					// Column 2 -B

					Cell cellB = row.createCell(1);
					if (record.getR11_collateral_amount() != null) {
						cellB.setCellValue(record.getR11_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					Cell cellC = row.createCell(2);
					if (record.getR11_carrying_amount() != null) {
						cellC.setCellValue(record.getR11_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4-D
					Cell cellD = row.createCell(3);
					if (record.getR11_no_of_accts() != null) {
						cellD.setCellValue(record.getR11_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row12
					row = sheet.getRow(11);

					// Column 2 -B

					cellB = row.createCell(1);
					if (record.getR12_collateral_amount() != null) {
						cellB.setCellValue(record.getR12_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR12_carrying_amount() != null) {
						cellC.setCellValue(record.getR12_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4-D
					cellD = row.createCell(3);
					if (record.getR12_no_of_accts() != null) {
						cellD.setCellValue(record.getR12_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row13
					row = sheet.getRow(12);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR13_collateral_amount() != null) {
						cellB.setCellValue(record.getR13_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR13_carrying_amount() != null) {
						cellC.setCellValue(record.getR13_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR13_no_of_accts() != null) {
						cellD.setCellValue(record.getR13_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row15
					row = sheet.getRow(14);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR15_collateral_amount() != null) {
						cellB.setCellValue(record.getR15_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR15_carrying_amount() != null) {
						cellC.setCellValue(record.getR15_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR15_no_of_accts() != null) {
						cellD.setCellValue(record.getR15_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row16
					row = sheet.getRow(15);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR16_collateral_amount() != null) {
						cellB.setCellValue(record.getR16_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR16_carrying_amount() != null) {
						cellC.setCellValue(record.getR16_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR16_no_of_accts() != null) {
						cellD.setCellValue(record.getR16_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row17
					row = sheet.getRow(16);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR17_collateral_amount() != null) {
						cellB.setCellValue(record.getR17_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR17_carrying_amount() != null) {
						cellC.setCellValue(record.getR17_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR17_no_of_accts() != null) {
						cellD.setCellValue(record.getR17_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row18
					row = sheet.getRow(17);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR18_collateral_amount() != null) {
						cellB.setCellValue(record.getR18_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR18_carrying_amount() != null) {
						cellC.setCellValue(record.getR18_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR18_no_of_accts() != null) {
						cellD.setCellValue(record.getR18_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row19
					row = sheet.getRow(18);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR19_collateral_amount() != null) {
						cellB.setCellValue(record.getR19_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR19_carrying_amount() != null) {
						cellC.setCellValue(record.getR19_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR19_no_of_accts() != null) {
						cellD.setCellValue(record.getR19_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row20
					row = sheet.getRow(19);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR20_collateral_amount() != null) {
						cellB.setCellValue(record.getR20_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR20_carrying_amount() != null) {
						cellC.setCellValue(record.getR20_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR20_no_of_accts() != null) {
						cellD.setCellValue(record.getR20_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row21
					row = sheet.getRow(20);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR21_collateral_amount() != null) {
						cellB.setCellValue(record.getR21_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR21_carrying_amount() != null) {
						cellC.setCellValue(record.getR21_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR21_no_of_accts() != null) {
						cellD.setCellValue(record.getR21_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row22
					row = sheet.getRow(21);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR22_collateral_amount() != null) {
						cellB.setCellValue(record.getR22_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR22_carrying_amount() != null) {
						cellC.setCellValue(record.getR22_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR22_no_of_accts() != null) {
						cellD.setCellValue(record.getR22_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row23
					row = sheet.getRow(22);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR23_collateral_amount() != null) {
						cellB.setCellValue(record.getR23_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR23_carrying_amount() != null) {
						cellC.setCellValue(record.getR23_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR23_no_of_accts() != null) {
						cellD.setCellValue(record.getR23_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row24
					row = sheet.getRow(23);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR24_collateral_amount() != null) {
						cellB.setCellValue(record.getR24_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR24_carrying_amount() != null) {
						cellC.setCellValue(record.getR24_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR24_no_of_accts() != null) {
						cellD.setCellValue(record.getR24_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row25
					row = sheet.getRow(24);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR25_collateral_amount() != null) {
						cellB.setCellValue(record.getR25_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR25_carrying_amount() != null) {
						cellC.setCellValue(record.getR25_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR25_no_of_accts() != null) {
						cellD.setCellValue(record.getR25_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row26
					row = sheet.getRow(25);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR26_collateral_amount() != null) {
						cellB.setCellValue(record.getR26_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR26_carrying_amount() != null) {
						cellC.setCellValue(record.getR26_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR26_no_of_accts() != null) {
						cellD.setCellValue(record.getR26_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row27
					row = sheet.getRow(26);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR27_collateral_amount() != null) {
						cellB.setCellValue(record.getR27_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR27_carrying_amount() != null) {
						cellC.setCellValue(record.getR27_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR27_no_of_accts() != null) {
						cellD.setCellValue(record.getR27_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row29
					row = sheet.getRow(28);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR29_collateral_amount() != null) {
						cellB.setCellValue(record.getR29_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR29_carrying_amount() != null) {
						cellC.setCellValue(record.getR29_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR29_no_of_accts() != null) {
						cellD.setCellValue(record.getR29_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row30
					row = sheet.getRow(29);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR30_collateral_amount() != null) {
						cellB.setCellValue(record.getR30_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR30_carrying_amount() != null) {
						cellC.setCellValue(record.getR30_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR30_no_of_accts() != null) {
						cellD.setCellValue(record.getR30_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row31
					row = sheet.getRow(30);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR31_collateral_amount() != null) {
						cellB.setCellValue(record.getR31_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR31_carrying_amount() != null) {
						cellC.setCellValue(record.getR31_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR31_no_of_accts() != null) {
						cellD.setCellValue(record.getR31_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row32
					row = sheet.getRow(31);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR32_collateral_amount() != null) {
						cellB.setCellValue(record.getR32_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR32_carrying_amount() != null) {
						cellC.setCellValue(record.getR32_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR32_no_of_accts() != null) {
						cellD.setCellValue(record.getR32_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row33
					row = sheet.getRow(32);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR33_collateral_amount() != null) {
						cellB.setCellValue(record.getR33_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR33_carrying_amount() != null) {
						cellC.setCellValue(record.getR33_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR33_no_of_accts() != null) {
						cellD.setCellValue(record.getR33_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row34
					row = sheet.getRow(33);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR34_collateral_amount() != null) {
						cellB.setCellValue(record.getR34_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR34_carrying_amount() != null) {
						cellC.setCellValue(record.getR34_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR34_no_of_accts() != null) {
						cellD.setCellValue(record.getR34_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row35
					row = sheet.getRow(34);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR35_collateral_amount() != null) {
						cellB.setCellValue(record.getR35_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR35_carrying_amount() != null) {
						cellC.setCellValue(record.getR35_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR35_no_of_accts() != null) {
						cellD.setCellValue(record.getR35_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row36
					row = sheet.getRow(35);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR36_collateral_amount() != null) {
						cellB.setCellValue(record.getR36_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR36_carrying_amount() != null) {
						cellC.setCellValue(record.getR36_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR36_no_of_accts() != null) {
						cellD.setCellValue(record.getR36_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row38
					row = sheet.getRow(37);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR38_collateral_amount() != null) {
						cellB.setCellValue(record.getR38_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR38_carrying_amount() != null) {
						cellC.setCellValue(record.getR38_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR38_no_of_accts() != null) {
						cellD.setCellValue(record.getR38_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row39
					row = sheet.getRow(38);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR39_collateral_amount() != null) {
						cellB.setCellValue(record.getR39_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR39_carrying_amount() != null) {
						cellC.setCellValue(record.getR39_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR39_no_of_accts() != null) {
						cellD.setCellValue(record.getR39_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row41
					row = sheet.getRow(40);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR41_collateral_amount() != null) {
						cellB.setCellValue(record.getR41_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR41_carrying_amount() != null) {
						cellC.setCellValue(record.getR41_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR41_no_of_accts() != null) {
						cellD.setCellValue(record.getR41_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row42
					row = sheet.getRow(41);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR42_collateral_amount() != null) {
						cellB.setCellValue(record.getR42_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR42_carrying_amount() != null) {
						cellC.setCellValue(record.getR42_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR42_no_of_accts() != null) {
						cellD.setCellValue(record.getR42_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row44
					row = sheet.getRow(43);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR44_collateral_amount() != null) {
						cellB.setCellValue(record.getR44_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR44_carrying_amount() != null) {
						cellC.setCellValue(record.getR44_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR44_no_of_accts() != null) {
						cellD.setCellValue(record.getR44_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row45
					row = sheet.getRow(44);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR45_collateral_amount() != null) {
						cellB.setCellValue(record.getR45_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR45_carrying_amount() != null) {
						cellC.setCellValue(record.getR45_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR45_no_of_accts() != null) {
						cellD.setCellValue(record.getR45_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row46
					row = sheet.getRow(45);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR46_collateral_amount() != null) {
						cellB.setCellValue(record.getR46_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR46_carrying_amount() != null) {
						cellC.setCellValue(record.getR46_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR46_no_of_accts() != null) {
						cellD.setCellValue(record.getR46_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row47
					row = sheet.getRow(46);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR47_collateral_amount() != null) {
						cellB.setCellValue(record.getR47_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR47_carrying_amount() != null) {
						cellC.setCellValue(record.getR47_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR47_no_of_accts() != null) {
						cellD.setCellValue(record.getR47_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row49
					row = sheet.getRow(48);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR49_collateral_amount() != null) {
						cellB.setCellValue(record.getR49_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR49_carrying_amount() != null) {
						cellC.setCellValue(record.getR49_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR49_no_of_accts() != null) {
						cellD.setCellValue(record.getR49_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row50
					row = sheet.getRow(49);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR50_collateral_amount() != null) {
						cellB.setCellValue(record.getR50_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR50_carrying_amount() != null) {
						cellC.setCellValue(record.getR50_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR50_no_of_accts() != null) {
						cellD.setCellValue(record.getR50_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row51
					row = sheet.getRow(50);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR51_collateral_amount() != null) {
						cellB.setCellValue(record.getR51_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR51_carrying_amount() != null) {
						cellC.setCellValue(record.getR51_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR51_no_of_accts() != null) {
						cellD.setCellValue(record.getR51_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row53
					row = sheet.getRow(52);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR53_collateral_amount() != null) {
						cellB.setCellValue(record.getR53_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR53_carrying_amount() != null) {
						cellC.setCellValue(record.getR53_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR53_no_of_accts() != null) {
						cellD.setCellValue(record.getR53_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row54
					row = sheet.getRow(53);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR54_collateral_amount() != null) {
						cellB.setCellValue(record.getR54_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR54_carrying_amount() != null) {
						cellC.setCellValue(record.getR54_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR54_no_of_accts() != null) {
						cellD.setCellValue(record.getR54_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row55
					row = sheet.getRow(54);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR55_collateral_amount() != null) {
						cellB.setCellValue(record.getR55_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR55_carrying_amount() != null) {
						cellC.setCellValue(record.getR55_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR55_no_of_accts() != null) {
						cellD.setCellValue(record.getR55_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row57
					row = sheet.getRow(56);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR57_collateral_amount() != null) {
						cellB.setCellValue(record.getR57_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR57_carrying_amount() != null) {
						cellC.setCellValue(record.getR57_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR57_no_of_accts() != null) {
						cellD.setCellValue(record.getR57_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row58
					row = sheet.getRow(57);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR58_collateral_amount() != null) {
						cellB.setCellValue(record.getR58_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR58_carrying_amount() != null) {
						cellC.setCellValue(record.getR58_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR58_no_of_accts() != null) {
						cellD.setCellValue(record.getR58_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row59
					row = sheet.getRow(58);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR59_collateral_amount() != null) {
						cellB.setCellValue(record.getR59_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR59_carrying_amount() != null) {
						cellC.setCellValue(record.getR59_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR59_no_of_accts() != null) {
						cellD.setCellValue(record.getR59_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row60
					row = sheet.getRow(59);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR60_collateral_amount() != null) {
						cellB.setCellValue(record.getR60_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR60_carrying_amount() != null) {
						cellC.setCellValue(record.getR60_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR60_no_of_accts() != null) {
						cellD.setCellValue(record.getR60_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row61
					row = sheet.getRow(60);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR61_collateral_amount() != null) {
						cellB.setCellValue(record.getR61_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR61_carrying_amount() != null) {
						cellC.setCellValue(record.getR61_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR61_no_of_accts() != null) {
						cellD.setCellValue(record.getR61_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// row62
					row = sheet.getRow(61);

					// Column 2 - B
					cellB = row.createCell(1);
					if (record.getR62_collateral_amount() != null) {
						cellB.setCellValue(record.getR62_collateral_amount().doubleValue());
						cellB.setCellStyle(numberStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// Column 3 - C
					cellC = row.createCell(2);
					if (record.getR62_carrying_amount() != null) {
						cellC.setCellValue(record.getR62_carrying_amount().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// Column 4 - D
					cellD = row.createCell(3);
					if (record.getR62_no_of_accts() != null) {
						cellD.setCellValue(record.getR62_no_of_accts().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 RESUB SUMMARY", null,
						"BRRS_Q_RLFA1_RESUB_SUMMARY");
			}

			return out.toByteArray();
		}

	}

// RESUB  EXCEL EMAIL
	// Resub Email Excel
	public byte[] BRRS_Q_RLFA1EmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB Email Excel generation process in memory.");

		List<Q_RLFA1_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_RLFA1 report. Returning empty result.");
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

			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_RLFA1_Resub_Summary_Entity record1 = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record1.getReport_date() != null) {

						R12Cell.setCellValue(record1.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(7) != null ? sheet.getRow(7) : sheet.createRow(7);
					Cell cell2 = row.createCell(2);

					if (record1.getR10_collateral_amount() != null) {
						cell2.setCellValue(record1.getR10_collateral_amount().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);

					if (record1.getR10_carrying_amount() != null) {
						cell3.setCellValue(record1.getR10_carrying_amount().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(5);

					if (record1.getR10_no_of_accts() != null) {
						cell4.setCellValue(record1.getR10_no_of_accts().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(8) != null ? sheet.getRow(8) : sheet.createRow(8);
					Cell cell5 = row.createCell(2);

					if (record1.getR11_collateral_amount() != null) {
						cell5.setCellValue(record1.getR11_collateral_amount().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(3);

					if (record1.getR11_carrying_amount() != null) {
						cell6.setCellValue(record1.getR11_carrying_amount().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					Cell cell7 = row.createCell(5);

					if (record1.getR11_no_of_accts() != null) {
						cell7.setCellValue(record1.getR11_no_of_accts().doubleValue());
						cell7.setCellStyle(numberStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					row = sheet.getRow(9) != null ? sheet.getRow(9) : sheet.createRow(9);
					Cell cell8 = row.createCell(2);

					if (record1.getR12_collateral_amount() != null) {
						cell8.setCellValue(record1.getR12_collateral_amount().doubleValue());
						cell8.setCellStyle(numberStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					Cell cell9 = row.createCell(3);

					if (record1.getR12_carrying_amount() != null) {
						cell9.setCellValue(record1.getR12_carrying_amount().doubleValue());
						cell9.setCellStyle(numberStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					Cell cell10 = row.createCell(5);

					if (record1.getR12_no_of_accts() != null) {
						cell10.setCellValue(record1.getR12_no_of_accts().doubleValue());
						cell10.setCellStyle(numberStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					row = sheet.getRow(10) != null ? sheet.getRow(10) : sheet.createRow(10);
					Cell cell11 = row.createCell(2);

					if (record1.getR13_collateral_amount() != null) {
						cell11.setCellValue(record1.getR13_collateral_amount().doubleValue());
						cell11.setCellStyle(numberStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					Cell cell12 = row.createCell(3);

					if (record1.getR13_carrying_amount() != null) {
						cell12.setCellValue(record1.getR13_carrying_amount().doubleValue());
						cell12.setCellStyle(numberStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					Cell cell13 = row.createCell(5);

					if (record1.getR13_no_of_accts() != null) {
						cell13.setCellValue(record1.getR13_no_of_accts().doubleValue());
						cell13.setCellStyle(numberStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					row = sheet.getRow(11) != null ? sheet.getRow(11) : sheet.createRow(11);
					Cell cell14 = row.createCell(2);

					if (record1.getR14_collateral_amount() != null) {
						cell14.setCellValue(record1.getR14_collateral_amount().doubleValue());
						cell14.setCellStyle(numberStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					Cell cell15 = row.createCell(3);

					if (record1.getR14_carrying_amount() != null) {
						cell15.setCellValue(record1.getR14_carrying_amount().doubleValue());
						cell15.setCellStyle(numberStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					Cell cell16 = row.createCell(5);

					if (record1.getR14_no_of_accts() != null) {
						cell16.setCellValue(record1.getR14_no_of_accts().doubleValue());
						cell16.setCellStyle(numberStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					row = sheet.getRow(12) != null ? sheet.getRow(12) : sheet.createRow(12);
					Cell cell17 = row.createCell(2);

					if (record1.getR15_collateral_amount() != null) {
						cell17.setCellValue(record1.getR15_collateral_amount().doubleValue());
						cell17.setCellStyle(numberStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					Cell cell18 = row.createCell(3);

					if (record1.getR15_carrying_amount() != null) {
						cell18.setCellValue(record1.getR15_carrying_amount().doubleValue());
						cell18.setCellStyle(numberStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					Cell cell19 = row.createCell(5);

					if (record1.getR15_no_of_accts() != null) {
						cell19.setCellValue(record1.getR15_no_of_accts().doubleValue());
						cell19.setCellStyle(numberStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					row = sheet.getRow(13) != null ? sheet.getRow(13) : sheet.createRow(13);
					Cell cell20 = row.createCell(2);

					if (record1.getR16_collateral_amount() != null) {
						cell20.setCellValue(record1.getR16_collateral_amount().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					Cell cell21 = row.createCell(3);

					if (record1.getR16_carrying_amount() != null) {
						cell21.setCellValue(record1.getR16_carrying_amount().doubleValue());
						cell21.setCellStyle(numberStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					Cell cell22 = row.createCell(5);

					if (record1.getR16_no_of_accts() != null) {
						cell22.setCellValue(record1.getR16_no_of_accts().doubleValue());
						cell22.setCellStyle(numberStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					row = sheet.getRow(14) != null ? sheet.getRow(14) : sheet.createRow(14);
					Cell cell23 = row.createCell(2);

					if (record1.getR17_collateral_amount() != null) {
						cell23.setCellValue(record1.getR17_collateral_amount().doubleValue());
						cell23.setCellStyle(numberStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					Cell cell24 = row.createCell(3);

					if (record1.getR17_carrying_amount() != null) {
						cell24.setCellValue(record1.getR17_carrying_amount().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					Cell cell25 = row.createCell(5);

					if (record1.getR17_no_of_accts() != null) {
						cell25.setCellValue(record1.getR17_no_of_accts().doubleValue());
						cell25.setCellStyle(numberStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					row = sheet.getRow(15) != null ? sheet.getRow(15) : sheet.createRow(15);
					Cell cell26 = row.createCell(2);

					if (record1.getR18_collateral_amount() != null) {
						cell26.setCellValue(record1.getR18_collateral_amount().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					Cell cell27 = row.createCell(3);

					if (record1.getR18_carrying_amount() != null) {
						cell27.setCellValue(record1.getR18_carrying_amount().doubleValue());
						cell27.setCellStyle(numberStyle);
					} else {
						cell27.setCellValue("");
						cell27.setCellStyle(textStyle);
					}

					Cell cell28 = row.createCell(5);

					if (record1.getR18_no_of_accts() != null) {
						cell28.setCellValue(record1.getR18_no_of_accts().doubleValue());
						cell28.setCellStyle(numberStyle);
					} else {
						cell28.setCellValue("");
						cell28.setCellStyle(textStyle);
					}

					row = sheet.getRow(16) != null ? sheet.getRow(16) : sheet.createRow(16);
					Cell cell29 = row.createCell(2);

					if (record1.getR19_collateral_amount() != null) {
						cell29.setCellValue(record1.getR19_collateral_amount().doubleValue());
						cell29.setCellStyle(numberStyle);
					} else {
						cell29.setCellValue("");
						cell29.setCellStyle(textStyle);
					}

					Cell cell30 = row.createCell(3);

					if (record1.getR19_carrying_amount() != null) {
						cell30.setCellValue(record1.getR19_carrying_amount().doubleValue());
						cell30.setCellStyle(numberStyle);
					} else {
						cell30.setCellValue("");
						cell30.setCellStyle(textStyle);
					}

					Cell cell31 = row.createCell(4);

					if (record1.getR19_no_of_accts() != null) {
						cell31.setCellValue(record1.getR19_no_of_accts().doubleValue());
						cell31.setCellStyle(numberStyle);
					} else {
						cell31.setCellValue("");
						cell31.setCellStyle(textStyle);
					}

					row = sheet.getRow(17) != null ? sheet.getRow(17) : sheet.createRow(17);
					Cell cell32 = row.createCell(2);

					if (record1.getR20_collateral_amount() != null) {
						cell32.setCellValue(record1.getR20_collateral_amount().doubleValue());
						cell32.setCellStyle(numberStyle);
					} else {
						cell32.setCellValue("");
						cell32.setCellStyle(textStyle);
					}

					Cell cell33 = row.createCell(3);

					if (record1.getR20_carrying_amount() != null) {
						cell33.setCellValue(record1.getR20_carrying_amount().doubleValue());
						cell33.setCellStyle(numberStyle);
					} else {
						cell33.setCellValue("");
						cell33.setCellStyle(textStyle);
					}

					Cell cell34 = row.createCell(4);

					if (record1.getR20_no_of_accts() != null) {
						cell34.setCellValue(record1.getR20_no_of_accts().doubleValue());
						cell34.setCellStyle(numberStyle);
					} else {
						cell34.setCellValue("");
						cell34.setCellStyle(textStyle);
					}

					row = sheet.getRow(18) != null ? sheet.getRow(18) : sheet.createRow(18);
					Cell cell35 = row.createCell(2);

					if (record1.getR21_collateral_amount() != null) {
						cell35.setCellValue(record1.getR21_collateral_amount().doubleValue());
						cell35.setCellStyle(numberStyle);
					} else {
						cell35.setCellValue("");
						cell35.setCellStyle(textStyle);
					}

					Cell cell36 = row.createCell(3);

					if (record1.getR21_carrying_amount() != null) {
						cell36.setCellValue(record1.getR21_carrying_amount().doubleValue());
						cell36.setCellStyle(numberStyle);
					} else {
						cell36.setCellValue("");
						cell36.setCellStyle(textStyle);
					}

					Cell cell37 = row.createCell(4);

					if (record1.getR21_no_of_accts() != null) {
						cell37.setCellValue(record1.getR21_no_of_accts().doubleValue());
						cell37.setCellStyle(numberStyle);
					} else {
						cell37.setCellValue("");
						cell37.setCellStyle(textStyle);
					}

					row = sheet.getRow(19) != null ? sheet.getRow(19) : sheet.createRow(19);
					Cell cell38 = row.createCell(2);

					if (record1.getR22_collateral_amount() != null) {
						cell38.setCellValue(record1.getR22_collateral_amount().doubleValue());
						cell38.setCellStyle(numberStyle);
					} else {
						cell38.setCellValue("");
						cell38.setCellStyle(textStyle);
					}

					Cell cell39 = row.createCell(3);

					if (record1.getR22_carrying_amount() != null) {
						cell39.setCellValue(record1.getR22_carrying_amount().doubleValue());
						cell39.setCellStyle(numberStyle);
					} else {
						cell39.setCellValue("");
						cell39.setCellStyle(textStyle);
					}

					Cell cell40 = row.createCell(4);

					if (record1.getR22_no_of_accts() != null) {
						cell40.setCellValue(record1.getR22_no_of_accts().doubleValue());
						cell40.setCellStyle(numberStyle);
					} else {
						cell40.setCellValue("");
						cell40.setCellStyle(textStyle);
					}

					row = sheet.getRow(20) != null ? sheet.getRow(20) : sheet.createRow(20);
					Cell cell41 = row.createCell(2);

					if (record1.getR23_collateral_amount() != null) {
						cell41.setCellValue(record1.getR23_collateral_amount().doubleValue());
						cell41.setCellStyle(numberStyle);
					} else {
						cell41.setCellValue("");
						cell41.setCellStyle(textStyle);
					}

					Cell cell42 = row.createCell(3);

					if (record1.getR23_carrying_amount() != null) {
						cell42.setCellValue(record1.getR23_carrying_amount().doubleValue());
						cell42.setCellStyle(numberStyle);
					} else {
						cell42.setCellValue("");
						cell42.setCellStyle(textStyle);
					}

					Cell cell43 = row.createCell(4);

					if (record1.getR23_no_of_accts() != null) {
						cell43.setCellValue(record1.getR23_no_of_accts().doubleValue());
						cell43.setCellStyle(numberStyle);
					} else {
						cell43.setCellValue("");
						cell43.setCellStyle(textStyle);
					}

					row = sheet.getRow(21) != null ? sheet.getRow(21) : sheet.createRow(21);
					Cell cell44 = row.createCell(2);

					if (record1.getR24_collateral_amount() != null) {
						cell44.setCellValue(record1.getR24_collateral_amount().doubleValue());
						cell44.setCellStyle(numberStyle);
					} else {
						cell44.setCellValue("");
						cell44.setCellStyle(textStyle);
					}

					Cell cell45 = row.createCell(3);

					if (record1.getR24_carrying_amount() != null) {
						cell45.setCellValue(record1.getR24_carrying_amount().doubleValue());
						cell45.setCellStyle(numberStyle);
					} else {
						cell45.setCellValue("");
						cell45.setCellStyle(textStyle);
					}

					Cell cell46 = row.createCell(4);

					if (record1.getR24_no_of_accts() != null) {
						cell46.setCellValue(record1.getR24_no_of_accts().doubleValue());
						cell46.setCellStyle(numberStyle);
					} else {
						cell46.setCellValue("");
						cell46.setCellStyle(textStyle);
					}

					row = sheet.getRow(22) != null ? sheet.getRow(22) : sheet.createRow(22);
					Cell cell47 = row.createCell(2);

					if (record1.getR25_collateral_amount() != null) {
						cell47.setCellValue(record1.getR25_collateral_amount().doubleValue());
						cell47.setCellStyle(numberStyle);
					} else {
						cell47.setCellValue("");
						cell47.setCellStyle(textStyle);
					}

					Cell cell48 = row.createCell(3);

					if (record1.getR25_carrying_amount() != null) {
						cell48.setCellValue(record1.getR25_carrying_amount().doubleValue());
						cell48.setCellStyle(numberStyle);
					} else {
						cell48.setCellValue("");
						cell48.setCellStyle(textStyle);
					}

					Cell cell49 = row.createCell(4);

					if (record1.getR25_no_of_accts() != null) {
						cell49.setCellValue(record1.getR25_no_of_accts().doubleValue());
						cell49.setCellStyle(numberStyle);
					} else {
						cell49.setCellValue("");
						cell49.setCellStyle(textStyle);
					}

					row = sheet.getRow(23) != null ? sheet.getRow(23) : sheet.createRow(23);
					Cell cell50 = row.createCell(2);

					if (record1.getR28_collateral_amount() != null) {
						cell50.setCellValue(record1.getR28_collateral_amount().doubleValue());
						cell50.setCellStyle(numberStyle);
					} else {
						cell50.setCellValue("");
						cell50.setCellStyle(textStyle);
					}

					Cell cell51 = row.createCell(3);

					if (record1.getR28_carrying_amount() != null) {
						cell51.setCellValue(record1.getR28_carrying_amount().doubleValue());
						cell51.setCellStyle(numberStyle);
					} else {
						cell51.setCellValue("");
						cell51.setCellStyle(textStyle);
					}

					Cell cell52 = row.createCell(4);

					if (record1.getR28_no_of_accts() != null) {
						cell52.setCellValue(record1.getR28_no_of_accts().doubleValue());
						cell52.setCellStyle(numberStyle);
					} else {
						cell52.setCellValue("");
						cell52.setCellStyle(textStyle);
					}

					row = sheet.getRow(24) != null ? sheet.getRow(24) : sheet.createRow(24);
					Cell cell53 = row.createCell(2);

					if (record1.getR29_collateral_amount() != null) {
						cell53.setCellValue(record1.getR29_collateral_amount().doubleValue());
						cell53.setCellStyle(numberStyle);
					} else {
						cell53.setCellValue("");
						cell53.setCellStyle(textStyle);
					}

					Cell cell54 = row.createCell(3);

					if (record1.getR29_carrying_amount() != null) {
						cell54.setCellValue(record1.getR29_carrying_amount().doubleValue());
						cell54.setCellStyle(numberStyle);
					} else {
						cell54.setCellValue("");
						cell54.setCellStyle(textStyle);
					}

					Cell cell55 = row.createCell(4);

					if (record1.getR29_no_of_accts() != null) {
						cell55.setCellValue(record1.getR29_no_of_accts().doubleValue());
						cell55.setCellStyle(numberStyle);
					} else {
						cell55.setCellValue("");
						cell55.setCellStyle(textStyle);
					}

					row = sheet.getRow(25) != null ? sheet.getRow(25) : sheet.createRow(25);
					Cell cell56 = row.createCell(2);

					if (record1.getR30_collateral_amount() != null) {
						cell56.setCellValue(record1.getR30_collateral_amount().doubleValue());
						cell56.setCellStyle(numberStyle);
					} else {
						cell56.setCellValue("");
						cell56.setCellStyle(textStyle);
					}

					Cell cell57 = row.createCell(3);

					if (record1.getR30_carrying_amount() != null) {
						cell57.setCellValue(record1.getR30_carrying_amount().doubleValue());
						cell57.setCellStyle(numberStyle);
					} else {
						cell57.setCellValue("");
						cell57.setCellStyle(textStyle);
					}

					Cell cell58 = row.createCell(4);

					if (record1.getR30_no_of_accts() != null) {
						cell58.setCellValue(record1.getR30_no_of_accts().doubleValue());
						cell58.setCellStyle(numberStyle);
					} else {
						cell58.setCellValue("");
						cell58.setCellStyle(textStyle);
					}

					row = sheet.getRow(27) != null ? sheet.getRow(27) : sheet.createRow(27);
					Cell cell59 = row.createCell(2);

					if (record1.getR32_collateral_amount() != null) {
						cell59.setCellValue(record1.getR32_collateral_amount().doubleValue());
						cell59.setCellStyle(numberStyle);
					} else {
						cell59.setCellValue("");
						cell59.setCellStyle(textStyle);
					}

					Cell cell60 = row.createCell(3);

					if (record1.getR32_carrying_amount() != null) {
						cell60.setCellValue(record1.getR32_carrying_amount().doubleValue());
						cell60.setCellStyle(numberStyle);
					} else {
						cell60.setCellValue("");
						cell60.setCellStyle(textStyle);
					}

					Cell cell61 = row.createCell(4);

					if (record1.getR32_no_of_accts() != null) {
						cell61.setCellValue(record1.getR32_no_of_accts().doubleValue());
						cell61.setCellStyle(numberStyle);
					} else {
						cell61.setCellValue("");
						cell61.setCellStyle(textStyle);
					}

					row = sheet.getRow(28) != null ? sheet.getRow(28) : sheet.createRow(28);
					Cell cell62 = row.createCell(2);

					if (record1.getR33_collateral_amount() != null) {
						cell62.setCellValue(record1.getR33_collateral_amount().doubleValue());
						cell62.setCellStyle(numberStyle);
					} else {
						cell62.setCellValue("");
						cell62.setCellStyle(textStyle);
					}

					Cell cell63 = row.createCell(3);

					if (record1.getR33_carrying_amount() != null) {
						cell63.setCellValue(record1.getR33_carrying_amount().doubleValue());
						cell63.setCellStyle(numberStyle);
					} else {
						cell63.setCellValue("");
						cell63.setCellStyle(textStyle);
					}

					Cell cell64 = row.createCell(4);

					if (record1.getR33_no_of_accts() != null) {
						cell64.setCellValue(record1.getR33_no_of_accts().doubleValue());
						cell64.setCellStyle(numberStyle);
					} else {
						cell64.setCellValue("");
						cell64.setCellStyle(textStyle);
					}

					row = sheet.getRow(29) != null ? sheet.getRow(29) : sheet.createRow(29);
					Cell cell65 = row.createCell(2);

					if (record1.getR31_collateral_amount() != null) {
						cell65.setCellValue(record1.getR31_collateral_amount().doubleValue());
						cell65.setCellStyle(numberStyle);
					} else {
						cell65.setCellValue("");
						cell65.setCellStyle(textStyle);
					}

					Cell cell66 = row.createCell(3);

					if (record1.getR31_carrying_amount() != null) {
						cell66.setCellValue(record1.getR31_carrying_amount().doubleValue());
						cell66.setCellStyle(numberStyle);
					} else {
						cell66.setCellValue("");
						cell66.setCellStyle(textStyle);
					}

					Cell cell67 = row.createCell(4);

					if (record1.getR31_no_of_accts() != null) {
						cell67.setCellValue(record1.getR31_no_of_accts().doubleValue());
						cell67.setCellStyle(numberStyle);
					} else {
						cell67.setCellValue("");
						cell67.setCellStyle(textStyle);
					}

					row = sheet.getRow(30) != null ? sheet.getRow(30) : sheet.createRow(30);
					Cell cell68 = row.createCell(2);

					if (record1.getR34_collateral_amount() != null) {
						cell68.setCellValue(record1.getR34_collateral_amount().doubleValue());
						cell68.setCellStyle(numberStyle);
					} else {
						cell68.setCellValue("");
						cell68.setCellStyle(textStyle);
					}

					Cell cell69 = row.createCell(3);

					if (record1.getR34_carrying_amount() != null) {
						cell69.setCellValue(record1.getR34_carrying_amount().doubleValue());
						cell69.setCellStyle(numberStyle);
					} else {
						cell69.setCellValue("");
						cell69.setCellStyle(textStyle);
					}

					Cell cell70 = row.createCell(4);

					if (record1.getR34_no_of_accts() != null) {
						cell70.setCellValue(record1.getR34_no_of_accts().doubleValue());
						cell70.setCellStyle(numberStyle);
					} else {
						cell70.setCellValue("");
						cell70.setCellStyle(textStyle);
					}

					row = sheet.getRow(31) != null ? sheet.getRow(31) : sheet.createRow(31);
					Cell cell71 = row.createCell(2);

					if (record1.getR36_collateral_amount() != null) {
						cell71.setCellValue(record1.getR36_collateral_amount().doubleValue());
						cell71.setCellStyle(numberStyle);
					} else {
						cell71.setCellValue("");
						cell71.setCellStyle(textStyle);
					}

					Cell cell72 = row.createCell(3);

					if (record1.getR36_carrying_amount() != null) {
						cell72.setCellValue(record1.getR36_carrying_amount().doubleValue());
						cell72.setCellStyle(numberStyle);
					} else {
						cell72.setCellValue("");
						cell72.setCellStyle(textStyle);
					}

					Cell cell73 = row.createCell(4);

					if (record1.getR36_no_of_accts() != null) {
						cell73.setCellValue(record1.getR36_no_of_accts().doubleValue());
						cell73.setCellStyle(numberStyle);
					} else {
						cell73.setCellValue("");
						cell73.setCellStyle(textStyle);
					}

					row = sheet.getRow(32) != null ? sheet.getRow(32) : sheet.createRow(32);
					Cell cell74 = row.createCell(2);

					if (record1.getR35_collateral_amount() != null) {
						cell74.setCellValue(record1.getR35_collateral_amount().doubleValue());
						cell74.setCellStyle(numberStyle);
					} else {
						cell74.setCellValue("");
						cell74.setCellStyle(textStyle);
					}

					Cell cell75 = row.createCell(3);

					if (record1.getR35_carrying_amount() != null) {
						cell75.setCellValue(record1.getR35_carrying_amount().doubleValue());
						cell75.setCellStyle(numberStyle);
					} else {
						cell75.setCellValue("");
						cell75.setCellStyle(textStyle);
					}

					Cell cell76 = row.createCell(4);

					if (record1.getR35_no_of_accts() != null) {
						cell76.setCellValue(record1.getR35_no_of_accts().doubleValue());
						cell76.setCellStyle(numberStyle);
					} else {
						cell76.setCellValue("");
						cell76.setCellStyle(textStyle);
					}

					row = sheet.getRow(33) != null ? sheet.getRow(33) : sheet.createRow(33);
					Cell cell77 = row.createCell(2);

					if (record1.getR37_collateral_amount() != null) {
						cell77.setCellValue(record1.getR37_collateral_amount().doubleValue());
						cell77.setCellStyle(numberStyle);
					} else {
						cell77.setCellValue("");
						cell77.setCellStyle(textStyle);
					}

					Cell cell78 = row.createCell(3);

					if (record1.getR37_carrying_amount() != null) {
						cell78.setCellValue(record1.getR37_carrying_amount().doubleValue());
						cell78.setCellStyle(numberStyle);
					} else {
						cell78.setCellValue("");
						cell78.setCellStyle(textStyle);
					}

					Cell cell79 = row.createCell(4);

					if (record1.getR37_no_of_accts() != null) {
						cell79.setCellValue(record1.getR37_no_of_accts().doubleValue());
						cell79.setCellStyle(numberStyle);
					} else {
						cell79.setCellValue("");
						cell79.setCellStyle(textStyle);
					}

					row = sheet.getRow(34) != null ? sheet.getRow(34) : sheet.createRow(34);
					Cell cell80 = row.createCell(2);

					if (record1.getR38_collateral_amount() != null) {
						cell80.setCellValue(record1.getR38_collateral_amount().doubleValue());
						cell80.setCellStyle(numberStyle);
					} else {
						cell80.setCellValue("");
						cell80.setCellStyle(textStyle);
					}

					Cell cell81 = row.createCell(3);

					if (record1.getR38_carrying_amount() != null) {
						cell81.setCellValue(record1.getR38_carrying_amount().doubleValue());
						cell81.setCellStyle(numberStyle);
					} else {
						cell81.setCellValue("");
						cell81.setCellStyle(textStyle);
					}

					Cell cell82 = row.createCell(4);

					if (record1.getR38_no_of_accts() != null) {
						cell82.setCellValue(record1.getR38_no_of_accts().doubleValue());
						cell82.setCellStyle(numberStyle);
					} else {
						cell82.setCellValue("");
						cell82.setCellStyle(textStyle);
					}

					row = sheet.getRow(35) != null ? sheet.getRow(35) : sheet.createRow(35);
					Cell cell83 = row.createCell(2);

					if (record1.getR39_collateral_amount() != null) {
						cell83.setCellValue(record1.getR39_collateral_amount().doubleValue());
						cell83.setCellStyle(numberStyle);
					} else {
						cell83.setCellValue("");
						cell83.setCellStyle(textStyle);
					}

					Cell cell84 = row.createCell(3);

					if (record1.getR39_carrying_amount() != null) {
						cell84.setCellValue(record1.getR39_carrying_amount().doubleValue());
						cell84.setCellStyle(numberStyle);
					} else {
						cell84.setCellValue("");
						cell84.setCellStyle(textStyle);
					}

					Cell cell85 = row.createCell(4);

					if (record1.getR39_no_of_accts() != null) {
						cell85.setCellValue(record1.getR39_no_of_accts().doubleValue());
						cell85.setCellStyle(numberStyle);
					} else {
						cell85.setCellValue("");
						cell85.setCellStyle(textStyle);
					}

					row = sheet.getRow(36) != null ? sheet.getRow(36) : sheet.createRow(36);
					Cell cell86 = row.createCell(2);

					if (record1.getR40_collateral_amount() != null) {
						cell86.setCellValue(record1.getR40_collateral_amount().doubleValue());
						cell86.setCellStyle(numberStyle);
					} else {
						cell86.setCellValue("");
						cell86.setCellStyle(textStyle);
					}

					Cell cell87 = row.createCell(3);

					if (record1.getR40_carrying_amount() != null) {
						cell87.setCellValue(record1.getR40_carrying_amount().doubleValue());
						cell87.setCellStyle(numberStyle);
					} else {
						cell87.setCellValue("");
						cell87.setCellStyle(textStyle);
					}

					Cell cell88 = row.createCell(4);

					if (record1.getR40_no_of_accts() != null) {
						cell88.setCellValue(record1.getR40_no_of_accts().doubleValue());
						cell88.setCellStyle(numberStyle);
					} else {
						cell88.setCellValue("");
						cell88.setCellStyle(textStyle);
					}

					row = sheet.getRow(37) != null ? sheet.getRow(37) : sheet.createRow(37);
					Cell cell89 = row.createCell(2);

					if (record1.getR41_collateral_amount() != null) {
						cell89.setCellValue(record1.getR41_collateral_amount().doubleValue());
						cell89.setCellStyle(numberStyle);
					} else {
						cell89.setCellValue("");
						cell89.setCellStyle(textStyle);
					}

					Cell cell90 = row.createCell(3);

					if (record1.getR41_carrying_amount() != null) {
						cell90.setCellValue(record1.getR41_carrying_amount().doubleValue());
						cell90.setCellStyle(numberStyle);
					} else {
						cell90.setCellValue("");
						cell90.setCellStyle(textStyle);
					}

					Cell cell91 = row.createCell(4);

					if (record1.getR41_no_of_accts() != null) {
						cell91.setCellValue(record1.getR41_no_of_accts().doubleValue());
						cell91.setCellStyle(numberStyle);
					} else {
						cell91.setCellValue("");
						cell91.setCellStyle(textStyle);
					}

					row = sheet.getRow(38) != null ? sheet.getRow(38) : sheet.createRow(38);
					Cell cell92 = row.createCell(2);

					if (record1.getR42_collateral_amount() != null) {
						cell92.setCellValue(record1.getR42_collateral_amount().doubleValue());
						cell92.setCellStyle(numberStyle);
					} else {
						cell92.setCellValue("");
						cell92.setCellStyle(textStyle);
					}

					Cell cell93 = row.createCell(3);

					if (record1.getR42_carrying_amount() != null) {
						cell93.setCellValue(record1.getR42_carrying_amount().doubleValue());
						cell93.setCellStyle(numberStyle);
					} else {
						cell93.setCellValue("");
						cell93.setCellStyle(textStyle);
					}

					Cell cell94 = row.createCell(4);

					if (record1.getR42_no_of_accts() != null) {
						cell94.setCellValue(record1.getR42_no_of_accts().doubleValue());
						cell94.setCellStyle(numberStyle);
					} else {
						cell94.setCellValue("");
						cell94.setCellStyle(textStyle);
					}

					row = sheet.getRow(39) != null ? sheet.getRow(39) : sheet.createRow(39);
					Cell cell95 = row.createCell(2);

					if (record1.getR43_collateral_amount() != null) {
						cell95.setCellValue(record1.getR43_collateral_amount().doubleValue());
						cell95.setCellStyle(numberStyle);
					} else {
						cell95.setCellValue("");
						cell95.setCellStyle(textStyle);
					}

					Cell cell96 = row.createCell(3);

					if (record1.getR43_carrying_amount() != null) {
						cell96.setCellValue(record1.getR43_carrying_amount().doubleValue());
						cell96.setCellStyle(numberStyle);
					} else {
						cell96.setCellValue("");
						cell96.setCellStyle(textStyle);
					}

					Cell cell97 = row.createCell(5);

					if (record1.getR43_no_of_accts() != null) {
						cell97.setCellValue(record1.getR43_no_of_accts().doubleValue());
						cell97.setCellStyle(numberStyle);
					} else {
						cell97.setCellValue("");
						cell97.setCellStyle(textStyle);
					}

					row = sheet.getRow(40) != null ? sheet.getRow(40) : sheet.createRow(40);
					Cell cell98 = row.createCell(2);

					if (record1.getR44_collateral_amount() != null) {
						cell98.setCellValue(record1.getR44_collateral_amount().doubleValue());
						cell98.setCellStyle(numberStyle);
					} else {
						cell98.setCellValue("");
						cell98.setCellStyle(textStyle);
					}

					Cell cell99 = row.createCell(3);

					if (record1.getR44_carrying_amount() != null) {
						cell99.setCellValue(record1.getR44_carrying_amount().doubleValue());
						cell99.setCellStyle(numberStyle);
					} else {
						cell99.setCellValue("");
						cell99.setCellStyle(textStyle);
					}

					Cell cell100 = row.createCell(5);

					if (record1.getR44_no_of_accts() != null) {
						cell100.setCellValue(record1.getR44_no_of_accts().doubleValue());
						cell100.setCellStyle(numberStyle);
					} else {
						cell100.setCellValue("");
						cell100.setCellStyle(textStyle);
					}

					row = sheet.getRow(41) != null ? sheet.getRow(41) : sheet.createRow(41);
					Cell cell101 = row.createCell(2);

					if (record1.getR45_collateral_amount() != null) {
						cell101.setCellValue(record1.getR45_collateral_amount().doubleValue());
						cell101.setCellStyle(numberStyle);
					} else {
						cell101.setCellValue("");
						cell101.setCellStyle(textStyle);
					}

					Cell cell102 = row.createCell(3);

					if (record1.getR45_carrying_amount() != null) {
						cell102.setCellValue(record1.getR45_carrying_amount().doubleValue());
						cell102.setCellStyle(numberStyle);
					} else {
						cell102.setCellValue("");
						cell102.setCellStyle(textStyle);
					}

					Cell cell103 = row.createCell(5);

					if (record1.getR45_no_of_accts() != null) {
						cell103.setCellValue(record1.getR45_no_of_accts().doubleValue());
						cell103.setCellStyle(numberStyle);
					} else {
						cell103.setCellValue("");
						cell103.setCellStyle(textStyle);
					}

					row = sheet.getRow(43) != null ? sheet.getRow(43) : sheet.createRow(43);
					Cell cell104 = row.createCell(2);

					if (record1.getR47_collateral_amount() != null) {
						cell104.setCellValue(record1.getR47_collateral_amount().doubleValue());
						cell104.setCellStyle(numberStyle);
					} else {
						cell104.setCellValue("");
						cell104.setCellStyle(textStyle);
					}

					Cell cell105 = row.createCell(3);

					if (record1.getR47_carrying_amount() != null) {
						cell105.setCellValue(record1.getR47_carrying_amount().doubleValue());
						cell105.setCellStyle(numberStyle);
					} else {
						cell105.setCellValue("");
						cell105.setCellStyle(textStyle);
					}

					Cell cell106 = row.createCell(5);

					if (record1.getR47_no_of_accts() != null) {
						cell106.setCellValue(record1.getR47_no_of_accts().doubleValue());
						cell106.setCellStyle(numberStyle);
					} else {
						cell106.setCellValue("");
						cell106.setCellStyle(textStyle);
					}

					row = sheet.getRow(44) != null ? sheet.getRow(44) : sheet.createRow(44);
					Cell cell107 = row.createCell(2);

					if (record1.getR48_collateral_amount() != null) {
						cell107.setCellValue(record1.getR48_collateral_amount().doubleValue());
						cell107.setCellStyle(numberStyle);
					} else {
						cell107.setCellValue("");
						cell107.setCellStyle(textStyle);
					}

					Cell cell108 = row.createCell(3);

					if (record1.getR48_carrying_amount() != null) {
						cell108.setCellValue(record1.getR48_carrying_amount().doubleValue());
						cell108.setCellStyle(numberStyle);
					} else {
						cell108.setCellValue("");
						cell108.setCellStyle(textStyle);
					}

					Cell cell109 = row.createCell(5);

					if (record1.getR48_no_of_accts() != null) {
						cell109.setCellValue(record1.getR48_no_of_accts().doubleValue());
						cell109.setCellStyle(numberStyle);
					} else {
						cell109.setCellValue("");
						cell109.setCellStyle(textStyle);
					}

					row = sheet.getRow(45) != null ? sheet.getRow(45) : sheet.createRow(45);
					Cell cell110 = row.createCell(2);

					if (record1.getR49_collateral_amount() != null) {
						cell110.setCellValue(record1.getR49_collateral_amount().doubleValue());
						cell110.setCellStyle(numberStyle);
					} else {
						cell110.setCellValue("");
						cell110.setCellStyle(textStyle);
					}

					Cell cell111 = row.createCell(3);

					if (record1.getR49_carrying_amount() != null) {
						cell111.setCellValue(record1.getR49_carrying_amount().doubleValue());
						cell111.setCellStyle(numberStyle);
					} else {
						cell111.setCellValue("");
						cell111.setCellStyle(textStyle);
					}

					Cell cell112 = row.createCell(5);

					if (record1.getR49_no_of_accts() != null) {
						cell112.setCellValue(record1.getR49_no_of_accts().doubleValue());
						cell112.setCellStyle(numberStyle);
					} else {
						cell112.setCellValue("");
						cell112.setCellStyle(textStyle);
					}

					row = sheet.getRow(46) != null ? sheet.getRow(46) : sheet.createRow(46);
					Cell cell113 = row.createCell(2);

					if (record1.getR50_collateral_amount() != null) {
						cell113.setCellValue(record1.getR50_collateral_amount().doubleValue());
						cell113.setCellStyle(numberStyle);
					} else {
						cell113.setCellValue("");
						cell113.setCellStyle(textStyle);
					}

					Cell cell114 = row.createCell(3);

					if (record1.getR50_carrying_amount() != null) {
						cell114.setCellValue(record1.getR50_carrying_amount().doubleValue());
						cell114.setCellStyle(numberStyle);
					} else {
						cell114.setCellValue("");
						cell114.setCellStyle(textStyle);
					}

					Cell cell115 = row.createCell(5);

					if (record1.getR50_no_of_accts() != null) {
						cell115.setCellValue(record1.getR50_no_of_accts().doubleValue());
						cell115.setCellStyle(numberStyle);
					} else {
						cell115.setCellValue("");
						cell115.setCellStyle(textStyle);
					}

					row = sheet.getRow(47) != null ? sheet.getRow(47) : sheet.createRow(47);
					Cell cell116 = row.createCell(2);

					if (record1.getR52_collateral_amount() != null) {
						cell116.setCellValue(record1.getR52_collateral_amount().doubleValue());
						cell116.setCellStyle(numberStyle);
					} else {
						cell116.setCellValue("");
						cell116.setCellStyle(textStyle);
					}

					Cell cell117 = row.createCell(3);

					if (record1.getR52_carrying_amount() != null) {
						cell117.setCellValue(record1.getR52_carrying_amount().doubleValue());
						cell117.setCellStyle(numberStyle);
					} else {
						cell117.setCellValue("");
						cell117.setCellStyle(textStyle);
					}

					Cell cell118 = row.createCell(5);

					if (record1.getR52_no_of_accts() != null) {
						cell118.setCellValue(record1.getR52_no_of_accts().doubleValue());
						cell118.setCellStyle(numberStyle);
					} else {
						cell118.setCellValue("");
						cell118.setCellStyle(textStyle);
					}

					row = sheet.getRow(49) != null ? sheet.getRow(49) : sheet.createRow(49);
					Cell cell119 = row.createCell(2);

					if (record1.getR53_collateral_amount() != null) {
						cell119.setCellValue(record1.getR53_collateral_amount().doubleValue());
						cell119.setCellStyle(numberStyle);
					} else {
						cell119.setCellValue("");
						cell119.setCellStyle(textStyle);
					}

					Cell cell120 = row.createCell(3);

					if (record1.getR53_carrying_amount() != null) {
						cell120.setCellValue(record1.getR53_carrying_amount().doubleValue());
						cell120.setCellStyle(numberStyle);
					} else {
						cell120.setCellValue("");
						cell120.setCellStyle(textStyle);
					}

					Cell cell121 = row.createCell(5);

					if (record1.getR53_no_of_accts() != null) {
						cell121.setCellValue(record1.getR53_no_of_accts().doubleValue());
						cell121.setCellStyle(numberStyle);
					} else {
						cell121.setCellValue("");
						cell121.setCellStyle(textStyle);
					}

					row = sheet.getRow(50) != null ? sheet.getRow(50) : sheet.createRow(50);
					Cell cell122 = row.createCell(2);

					if (record1.getR54_collateral_amount() != null) {
						cell122.setCellValue(record1.getR54_collateral_amount().doubleValue());
						cell122.setCellStyle(numberStyle);
					} else {
						cell122.setCellValue("");
						cell122.setCellStyle(textStyle);
					}

					Cell cell123 = row.createCell(3);

					if (record1.getR54_carrying_amount() != null) {
						cell123.setCellValue(record1.getR54_carrying_amount().doubleValue());
						cell123.setCellStyle(numberStyle);
					} else {
						cell123.setCellValue("");
						cell123.setCellStyle(textStyle);
					}

					Cell cell124 = row.createCell(5);

					if (record1.getR54_no_of_accts() != null) {
						cell124.setCellValue(record1.getR54_no_of_accts().doubleValue());
						cell124.setCellStyle(numberStyle);
					} else {
						cell124.setCellValue("");
						cell124.setCellStyle(textStyle);
					}

					row = sheet.getRow(51) != null ? sheet.getRow(51) : sheet.createRow(51);
					Cell cell125 = row.createCell(2);

					if (record1.getR55_collateral_amount() != null) {
						cell125.setCellValue(record1.getR55_collateral_amount().doubleValue());
						cell125.setCellStyle(numberStyle);
					} else {
						cell125.setCellValue("");
						cell125.setCellStyle(textStyle);
					}

					Cell cell126 = row.createCell(3);

					if (record1.getR55_carrying_amount() != null) {
						cell126.setCellValue(record1.getR55_carrying_amount().doubleValue());
						cell126.setCellStyle(numberStyle);
					} else {
						cell126.setCellValue("");
						cell126.setCellStyle(textStyle);
					}

					Cell cell127 = row.createCell(5);

					if (record1.getR55_no_of_accts() != null) {
						cell127.setCellValue(record1.getR55_no_of_accts().doubleValue());
						cell127.setCellStyle(numberStyle);
					} else {
						cell127.setCellValue("");
						cell127.setCellStyle(textStyle);
					}

					row = sheet.getRow(52) != null ? sheet.getRow(52) : sheet.createRow(52);
					Cell cell128 = row.createCell(2);

					if (record1.getR56_collateral_amount() != null) {
						cell128.setCellValue(record1.getR56_collateral_amount().doubleValue());
						cell128.setCellStyle(numberStyle);
					} else {
						cell128.setCellValue("");
						cell128.setCellStyle(textStyle);
					}

					Cell cell129 = row.createCell(3);

					if (record1.getR56_carrying_amount() != null) {
						cell129.setCellValue(record1.getR56_carrying_amount().doubleValue());
						cell129.setCellStyle(numberStyle);
					} else {
						cell129.setCellValue("");
						cell129.setCellStyle(textStyle);
					}

					Cell cell130 = row.createCell(5);

					if (record1.getR56_no_of_accts() != null) {
						cell130.setCellValue(record1.getR56_no_of_accts().doubleValue());
						cell130.setCellStyle(numberStyle);
					} else {
						cell130.setCellValue("");
						cell130.setCellStyle(textStyle);
					}

					row = sheet.getRow(53) != null ? sheet.getRow(53) : sheet.createRow(53);
					Cell cell131 = row.createCell(2);

					if (record1.getR58_collateral_amount() != null) {
						cell131.setCellValue(record1.getR58_collateral_amount().doubleValue());
						cell131.setCellStyle(numberStyle);
					} else {
						cell131.setCellValue("");
						cell131.setCellStyle(textStyle);
					}

					Cell cell132 = row.createCell(3);

					if (record1.getR58_carrying_amount() != null) {
						cell132.setCellValue(record1.getR58_carrying_amount().doubleValue());
						cell132.setCellStyle(numberStyle);
					} else {
						cell132.setCellValue("");
						cell132.setCellStyle(textStyle);
					}

					Cell cell133 = row.createCell(5);

					if (record1.getR58_no_of_accts() != null) {
						cell133.setCellValue(record1.getR58_no_of_accts().doubleValue());
						cell133.setCellStyle(numberStyle);
					} else {
						cell133.setCellValue("");
						cell133.setCellStyle(textStyle);
					}

					row = sheet.getRow(54) != null ? sheet.getRow(54) : sheet.createRow(54);
					Cell cell134 = row.createCell(2);

					if (record1.getR59_collateral_amount() != null) {
						cell134.setCellValue(record1.getR59_collateral_amount().doubleValue());
						cell134.setCellStyle(numberStyle);
					} else {
						cell134.setCellValue("");
						cell134.setCellStyle(textStyle);
					}

					Cell cell135 = row.createCell(3);

					if (record1.getR59_carrying_amount() != null) {
						cell135.setCellValue(record1.getR59_carrying_amount().doubleValue());
						cell135.setCellStyle(numberStyle);
					} else {
						cell135.setCellValue("");
						cell135.setCellStyle(textStyle);
					}

					Cell cell136 = row.createCell(5);

					if (record1.getR59_no_of_accts() != null) {
						cell136.setCellValue(record1.getR59_no_of_accts().doubleValue());
						cell136.setCellStyle(numberStyle);
					} else {
						cell136.setCellValue("");
						cell136.setCellStyle(textStyle);
					}

					row = sheet.getRow(55) != null ? sheet.getRow(55) : sheet.createRow(55);
					Cell cell137 = row.createCell(2);

					if (record1.getR60_collateral_amount() != null) {
						cell137.setCellValue(record1.getR60_collateral_amount().doubleValue());
						cell137.setCellStyle(numberStyle);
					} else {
						cell137.setCellValue("");
						cell137.setCellStyle(textStyle);
					}

					Cell cell138 = row.createCell(3);

					if (record1.getR60_carrying_amount() != null) {
						cell138.setCellValue(record1.getR60_carrying_amount().doubleValue());
						cell138.setCellStyle(numberStyle);
					} else {
						cell138.setCellValue("");
						cell138.setCellStyle(textStyle);
					}

					Cell cell139 = row.createCell(5);

					if (record1.getR60_no_of_accts() != null) {
						cell139.setCellValue(record1.getR60_no_of_accts().doubleValue());
						cell139.setCellStyle(numberStyle);
					} else {
						cell139.setCellValue("");
						cell139.setCellStyle(textStyle);
					}

					row = sheet.getRow(56) != null ? sheet.getRow(56) : sheet.createRow(56);
					Cell cell140 = row.createCell(2);

					if (record1.getR61_collateral_amount() != null) {
						cell140.setCellValue(record1.getR61_collateral_amount().doubleValue());
						cell140.setCellStyle(numberStyle);
					} else {
						cell140.setCellValue("");
						cell140.setCellStyle(textStyle);
					}

					Cell cell141 = row.createCell(3);

					if (record1.getR61_carrying_amount() != null) {
						cell141.setCellValue(record1.getR61_carrying_amount().doubleValue());
						cell141.setCellStyle(numberStyle);
					} else {
						cell141.setCellValue("");
						cell141.setCellStyle(textStyle);
					}

					Cell cell142 = row.createCell(5);

					if (record1.getR61_no_of_accts() != null) {
						cell142.setCellValue(record1.getR61_no_of_accts().doubleValue());
						cell142.setCellStyle(numberStyle);
					} else {
						cell142.setCellValue("");
						cell142.setCellStyle(textStyle);
					}

					row = sheet.getRow(57) != null ? sheet.getRow(57) : sheet.createRow(57);
					Cell cell143 = row.createCell(2);

					if (record1.getR62_collateral_amount() != null) {
						cell143.setCellValue(record1.getR62_collateral_amount().doubleValue());
						cell143.setCellStyle(numberStyle);
					} else {
						cell143.setCellValue("");
						cell143.setCellStyle(textStyle);
					}

					Cell cell144 = row.createCell(3);

					if (record1.getR62_carrying_amount() != null) {
						cell144.setCellValue(record1.getR62_carrying_amount().doubleValue());
						cell144.setCellStyle(numberStyle);
					} else {
						cell144.setCellValue("");
						cell144.setCellStyle(textStyle);
					}

					Cell cell145 = row.createCell(5);

					if (record1.getR62_no_of_accts() != null) {
						cell145.setCellValue(record1.getR62_no_of_accts().doubleValue());
						cell145.setCellStyle(numberStyle);
					} else {
						cell145.setCellValue("");
						cell145.setCellStyle(textStyle);
					}

					row = sheet.getRow(58) != null ? sheet.getRow(58) : sheet.createRow(58);
					Cell cell146 = row.createCell(2);

					if (record1.getR63_collateral_amount() != null) {
						cell146.setCellValue(record1.getR63_collateral_amount().doubleValue());
						cell146.setCellStyle(numberStyle);
					} else {
						cell146.setCellValue("");
						cell146.setCellStyle(textStyle);
					}

					Cell cell147 = row.createCell(3);

					if (record1.getR63_carrying_amount() != null) {
						cell147.setCellValue(record1.getR63_carrying_amount().doubleValue());
						cell147.setCellStyle(numberStyle);
					} else {
						cell147.setCellValue("");
						cell147.setCellStyle(textStyle);
					}

					Cell cell148 = row.createCell(5);

					if (record1.getR63_no_of_accts() != null) {
						cell148.setCellValue(record1.getR63_no_of_accts().doubleValue());
						cell148.setCellStyle(numberStyle);
					} else {
						cell148.setCellValue("");
						cell148.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_RLFA1 EMAIL RESUB SUMMARY", null,
						"BRRS_Q_RLFA1_RESUB_SUMMARY");
			}

			return out.toByteArray();
		}
	}

}