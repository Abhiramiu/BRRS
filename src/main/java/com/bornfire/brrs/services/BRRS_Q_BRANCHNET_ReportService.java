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

public class BRRS_Q_BRANCHNET_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_BRANCHNET_ReportService.class);

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

	public List<Q_BRANCHNET_Summary_Entity> getSummaryDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_BRANCHNET_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_BRANCHNET_Summary_RowMapper());
	}

	// findbyreportdate

	public Q_BRANCHNET_Summary_Entity findByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_BRANCHNET_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<Q_BRANCHNET_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new Q_BRANCHNET_Summary_RowMapper());

		return list.isEmpty() ? null : list.get(0);
	}

// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================

	public List<Object[]> get_Q_BRANCHNET_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Q_BRANCHNET_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_BRANCHNET_Archival_Summary_RowMapper());
	}

	public List<Q_BRANCHNET_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Q_BRANCHNET_Archival_Summary_RowMapper());
	}

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

// =====================================================
// DETAIL REPO
// =====================================================	

	public List<Q_BRANCHNET_Detail_Entity> getDetaildatabydateList(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_BRANCHNET_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_BRANCHNET_Detail_RowMapper());
	}

// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

	public List<Map<String, Object>> getQ_BRANCHNET_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_DETAIL "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_BRANCHNET_Archival_Detail_Entity> getDetaildatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_BRANCHNET_Archival_Detail_RowMapper());
	}

	public BigDecimal findDETAILMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_BRANCHNET_Archival_Detail_Entity getArchivalListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_BRANCHNET_Archival_Detail_RowMapper());
	}

// =====================================================
// RESUB SUMMARY
// =====================================================

	public List<Q_BRANCHNET_Resub_Summary_Entity> getResubSummarydatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_BRANCHNET_RESUB_Summary_RowMapper());
	}

	public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Map<String, Object>> getQ_BRANCHNET_Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public Q_BRANCHNET_Resub_Summary_Entity getResubSummarydatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_BRANCHNET_RESUB_Summary_RowMapper());
	}

// =====================================================
// RESUB DETAIL
// =====================================================

	public List<Map<String, Object>> get_Q_BRANCHNETArchival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_BRANCHNET_RESUB_DETAILTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_BRANCHNET_Resub_Detail_Entity> getResubDetaildatabydateList(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_BRANCHNET_RESUB_Detail_RowMapper());
	}

	public BigDecimal findResubDetailMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_BRANCHNET_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_BRANCHNET_Resub_Detail_Entity getdResubDetailDatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_BRANCHNET_RESUB_DETAILTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_BRANCHNET_RESUB_Detail_RowMapper());
	}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================

	public class Q_BRANCHNET_Summary_RowMapper implements RowMapper<Q_BRANCHNET_Summary_Entity> {

		@Override
		public Q_BRANCHNET_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Summary_Entity obj = new Q_BRANCHNET_Summary_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));

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

	public class Q_BRANCHNET_Summary_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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

	public class Q_BRANCHNET_Archival_Summary_RowMapper implements RowMapper<Q_BRANCHNET_Archival_Summary_Entity> {

		@Override
		public Q_BRANCHNET_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Archival_Summary_Entity obj = new Q_BRANCHNET_Archival_Summary_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));
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

	public class Q_BRANCHNET_Archival_Summary_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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
// DETAIL ENTITY  Q_BRANCHNET
// =====================================================	

	public class Q_BRANCHNET_Detail_RowMapper implements RowMapper<Q_BRANCHNET_Detail_Entity> {

		@Override
		public Q_BRANCHNET_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Detail_Entity obj = new Q_BRANCHNET_Detail_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));

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

	public class Q_BRANCHNET_Detail_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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

	public class Q_BRANCHNET_Archival_Detail_RowMapper implements RowMapper<Q_BRANCHNET_Archival_Detail_Entity> {

		@Override
		public Q_BRANCHNET_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Archival_Detail_Entity obj = new Q_BRANCHNET_Archival_Detail_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));
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

	public class Q_BRANCHNET_Archival_Detail_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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

//====================================================================================================================================
// RESUB summary Q_BRANCHNET
//=====================================================

	public class Q_BRANCHNET_RESUB_Summary_RowMapper implements RowMapper<Q_BRANCHNET_Resub_Summary_Entity> {

		@Override
		public Q_BRANCHNET_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Resub_Summary_Entity obj = new Q_BRANCHNET_Resub_Summary_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));
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

	public class Q_BRANCHNET_Resub_Summary_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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
// RESUB DETAIL Q_BRANCHNET
//=====================================================

	public class Q_BRANCHNET_RESUB_Detail_RowMapper implements RowMapper<Q_BRANCHNET_Resub_Detail_Entity> {

		@Override
		public Q_BRANCHNET_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_BRANCHNET_Resub_Detail_Entity obj = new Q_BRANCHNET_Resub_Detail_Entity();
// R10
			obj.setR10_bran_sub_bran_district(rs.getString("r10_bran_sub_bran_district"));
			obj.setR10_no1_of_branches(rs.getBigDecimal("r10_no1_of_branches"));
			obj.setR10_no1_of_sub_branches(rs.getBigDecimal("r10_no1_of_sub_branches"));
			obj.setR10_no1_of_agencies(rs.getBigDecimal("r10_no1_of_agencies"));

			// R11
			obj.setR11_bran_sub_bran_district(rs.getString("r11_bran_sub_bran_district"));
			obj.setR11_no1_of_branches(rs.getBigDecimal("r11_no1_of_branches"));
			obj.setR11_no1_of_sub_branches(rs.getBigDecimal("r11_no1_of_sub_branches"));
			obj.setR11_no1_of_agencies(rs.getBigDecimal("r11_no1_of_agencies"));

			// R12
			obj.setR12_bran_sub_bran_district(rs.getString("r12_bran_sub_bran_district"));
			obj.setR12_no1_of_branches(rs.getBigDecimal("r12_no1_of_branches"));
			obj.setR12_no1_of_sub_branches(rs.getBigDecimal("r12_no1_of_sub_branches"));
			obj.setR12_no1_of_agencies(rs.getBigDecimal("r12_no1_of_agencies"));

			// R13
			obj.setR13_bran_sub_bran_district(rs.getString("r13_bran_sub_bran_district"));
			obj.setR13_no1_of_branches(rs.getBigDecimal("r13_no1_of_branches"));
			obj.setR13_no1_of_sub_branches(rs.getBigDecimal("r13_no1_of_sub_branches"));
			obj.setR13_no1_of_agencies(rs.getBigDecimal("r13_no1_of_agencies"));

			// R14
			obj.setR14_bran_sub_bran_district(rs.getString("r14_bran_sub_bran_district"));
			obj.setR14_no1_of_branches(rs.getBigDecimal("r14_no1_of_branches"));
			obj.setR14_no1_of_sub_branches(rs.getBigDecimal("r14_no1_of_sub_branches"));
			obj.setR14_no1_of_agencies(rs.getBigDecimal("r14_no1_of_agencies"));

			// R15
			obj.setR15_bran_sub_bran_district(rs.getString("r15_bran_sub_bran_district"));
			obj.setR15_no1_of_branches(rs.getBigDecimal("r15_no1_of_branches"));
			obj.setR15_no1_of_sub_branches(rs.getBigDecimal("r15_no1_of_sub_branches"));
			obj.setR15_no1_of_agencies(rs.getBigDecimal("r15_no1_of_agencies"));

			// R16
			obj.setR16_bran_sub_bran_district(rs.getString("r16_bran_sub_bran_district"));
			obj.setR16_no1_of_branches(rs.getBigDecimal("r16_no1_of_branches"));
			obj.setR16_no1_of_sub_branches(rs.getBigDecimal("r16_no1_of_sub_branches"));
			obj.setR16_no1_of_agencies(rs.getBigDecimal("r16_no1_of_agencies"));

			// R17
			obj.setR17_bran_sub_bran_district(rs.getString("r17_bran_sub_bran_district"));
			obj.setR17_no1_of_branches(rs.getBigDecimal("r17_no1_of_branches"));
			obj.setR17_no1_of_sub_branches(rs.getBigDecimal("r17_no1_of_sub_branches"));
			obj.setR17_no1_of_agencies(rs.getBigDecimal("r17_no1_of_agencies"));

			// R18
			obj.setR18_bran_sub_bran_district(rs.getString("r18_bran_sub_bran_district"));
			obj.setR18_no1_of_branches(rs.getBigDecimal("r18_no1_of_branches"));
			obj.setR18_no1_of_sub_branches(rs.getBigDecimal("r18_no1_of_sub_branches"));
			obj.setR18_no1_of_agencies(rs.getBigDecimal("r18_no1_of_agencies"));

			// R19
			obj.setR19_bran_sub_bran_district(rs.getString("r19_bran_sub_bran_district"));
			obj.setR19_no1_of_branches(rs.getBigDecimal("r19_no1_of_branches"));
			obj.setR19_no1_of_sub_branches(rs.getBigDecimal("r19_no1_of_sub_branches"));
			obj.setR19_no1_of_agencies(rs.getBigDecimal("r19_no1_of_agencies"));

			// R20
			obj.setR20_bran_sub_bran_district(rs.getString("r20_bran_sub_bran_district"));
			obj.setR20_no1_of_branches(rs.getBigDecimal("r20_no1_of_branches"));
			obj.setR20_no1_of_sub_branches(rs.getBigDecimal("r20_no1_of_sub_branches"));
			obj.setR20_no1_of_agencies(rs.getBigDecimal("r20_no1_of_agencies"));

			// R25
			obj.setR25_atm_mini_atm_district(rs.getString("r25_atm_mini_atm_district"));
			obj.setR25_no_of_atms(rs.getBigDecimal("r25_no_of_atms"));
			obj.setR25_no_of_mini_atms(rs.getBigDecimal("r25_no_of_mini_atms"));
			obj.setR25_encashment_points(rs.getBigDecimal("r25_encashment_points"));

			// R26
			obj.setR26_atm_mini_atm_district(rs.getString("r26_atm_mini_atm_district"));
			obj.setR26_no_of_atms(rs.getBigDecimal("r26_no_of_atms"));
			obj.setR26_no_of_mini_atms(rs.getBigDecimal("r26_no_of_mini_atms"));
			obj.setR26_encashment_points(rs.getBigDecimal("r26_encashment_points"));

			// R27
			obj.setR27_atm_mini_atm_district(rs.getString("r27_atm_mini_atm_district"));
			obj.setR27_no_of_atms(rs.getBigDecimal("r27_no_of_atms"));
			obj.setR27_no_of_mini_atms(rs.getBigDecimal("r27_no_of_mini_atms"));
			obj.setR27_encashment_points(rs.getBigDecimal("r27_encashment_points"));

			// R28
			obj.setR28_atm_mini_atm_district(rs.getString("r28_atm_mini_atm_district"));
			obj.setR28_no_of_atms(rs.getBigDecimal("r28_no_of_atms"));
			obj.setR28_no_of_mini_atms(rs.getBigDecimal("r28_no_of_mini_atms"));
			obj.setR28_encashment_points(rs.getBigDecimal("r28_encashment_points"));

			// R29
			obj.setR29_atm_mini_atm_district(rs.getString("r29_atm_mini_atm_district"));
			obj.setR29_no_of_atms(rs.getBigDecimal("r29_no_of_atms"));
			obj.setR29_no_of_mini_atms(rs.getBigDecimal("r29_no_of_mini_atms"));
			obj.setR29_encashment_points(rs.getBigDecimal("r29_encashment_points"));

			// R30
			obj.setR30_atm_mini_atm_district(rs.getString("r30_atm_mini_atm_district"));
			obj.setR30_no_of_atms(rs.getBigDecimal("r30_no_of_atms"));
			obj.setR30_no_of_mini_atms(rs.getBigDecimal("r30_no_of_mini_atms"));
			obj.setR30_encashment_points(rs.getBigDecimal("r30_encashment_points"));

			// R31
			obj.setR31_atm_mini_atm_district(rs.getString("r31_atm_mini_atm_district"));
			obj.setR31_no_of_atms(rs.getBigDecimal("r31_no_of_atms"));
			obj.setR31_no_of_mini_atms(rs.getBigDecimal("r31_no_of_mini_atms"));
			obj.setR31_encashment_points(rs.getBigDecimal("r31_encashment_points"));

			// R32
			obj.setR32_atm_mini_atm_district(rs.getString("r32_atm_mini_atm_district"));
			obj.setR32_no_of_atms(rs.getBigDecimal("r32_no_of_atms"));
			obj.setR32_no_of_mini_atms(rs.getBigDecimal("r32_no_of_mini_atms"));
			obj.setR32_encashment_points(rs.getBigDecimal("r32_encashment_points"));

			// R33
			obj.setR33_atm_mini_atm_district(rs.getString("r33_atm_mini_atm_district"));
			obj.setR33_no_of_atms(rs.getBigDecimal("r33_no_of_atms"));
			obj.setR33_no_of_mini_atms(rs.getBigDecimal("r33_no_of_mini_atms"));
			obj.setR33_encashment_points(rs.getBigDecimal("r33_encashment_points"));

			// R34
			obj.setR34_atm_mini_atm_district(rs.getString("r34_atm_mini_atm_district"));
			obj.setR34_no_of_atms(rs.getBigDecimal("r34_no_of_atms"));
			obj.setR34_no_of_mini_atms(rs.getBigDecimal("r34_no_of_mini_atms"));
			obj.setR34_encashment_points(rs.getBigDecimal("r34_encashment_points"));

			// R35
			obj.setR35_atm_mini_atm_district(rs.getString("r35_atm_mini_atm_district"));
			obj.setR35_no_of_atms(rs.getBigDecimal("r35_no_of_atms"));
			obj.setR35_no_of_mini_atms(rs.getBigDecimal("r35_no_of_mini_atms"));
			obj.setR35_encashment_points(rs.getBigDecimal("r35_encashment_points"));

			// R40
			obj.setR40_debit_district(rs.getString("r40_debit_district"));
			obj.setR40_opening_no_of_cards(rs.getBigDecimal("r40_opening_no_of_cards"));
			obj.setR40_no_of_cards_issued(rs.getBigDecimal("r40_no_of_cards_issued"));
			obj.setR40_no_cards_of_closed(rs.getBigDecimal("r40_no_cards_of_closed"));
			obj.setR40_closing_bal_of_active_cards(rs.getBigDecimal("r40_closing_bal_of_active_cards"));

			// R41
			obj.setR41_debit_district(rs.getString("r41_debit_district"));
			obj.setR41_opening_no_of_cards(rs.getBigDecimal("r41_opening_no_of_cards"));
			obj.setR41_no_of_cards_issued(rs.getBigDecimal("r41_no_of_cards_issued"));
			obj.setR41_no_cards_of_closed(rs.getBigDecimal("r41_no_cards_of_closed"));
			obj.setR41_closing_bal_of_active_cards(rs.getBigDecimal("r41_closing_bal_of_active_cards"));

			// R42
			obj.setR42_debit_district(rs.getString("r42_debit_district"));
			obj.setR42_opening_no_of_cards(rs.getBigDecimal("r42_opening_no_of_cards"));
			obj.setR42_no_of_cards_issued(rs.getBigDecimal("r42_no_of_cards_issued"));
			obj.setR42_no_cards_of_closed(rs.getBigDecimal("r42_no_cards_of_closed"));
			obj.setR42_closing_bal_of_active_cards(rs.getBigDecimal("r42_closing_bal_of_active_cards"));

			// R43
			obj.setR43_debit_district(rs.getString("r43_debit_district"));
			obj.setR43_opening_no_of_cards(rs.getBigDecimal("r43_opening_no_of_cards"));
			obj.setR43_no_of_cards_issued(rs.getBigDecimal("r43_no_of_cards_issued"));
			obj.setR43_no_cards_of_closed(rs.getBigDecimal("r43_no_cards_of_closed"));
			obj.setR43_closing_bal_of_active_cards(rs.getBigDecimal("r43_closing_bal_of_active_cards"));

			// R44
			obj.setR44_debit_district(rs.getString("r44_debit_district"));
			obj.setR44_opening_no_of_cards(rs.getBigDecimal("r44_opening_no_of_cards"));
			obj.setR44_no_of_cards_issued(rs.getBigDecimal("r44_no_of_cards_issued"));
			obj.setR44_no_cards_of_closed(rs.getBigDecimal("r44_no_cards_of_closed"));
			obj.setR44_closing_bal_of_active_cards(rs.getBigDecimal("r44_closing_bal_of_active_cards"));

			// R45
			obj.setR45_debit_district(rs.getString("r45_debit_district"));
			obj.setR45_opening_no_of_cards(rs.getBigDecimal("r45_opening_no_of_cards"));
			obj.setR45_no_of_cards_issued(rs.getBigDecimal("r45_no_of_cards_issued"));
			obj.setR45_no_cards_of_closed(rs.getBigDecimal("r45_no_cards_of_closed"));
			obj.setR45_closing_bal_of_active_cards(rs.getBigDecimal("r45_closing_bal_of_active_cards"));

			// R46
			obj.setR46_debit_district(rs.getString("r46_debit_district"));
			obj.setR46_opening_no_of_cards(rs.getBigDecimal("r46_opening_no_of_cards"));
			obj.setR46_no_of_cards_issued(rs.getBigDecimal("r46_no_of_cards_issued"));
			obj.setR46_no_cards_of_closed(rs.getBigDecimal("r46_no_cards_of_closed"));
			obj.setR46_closing_bal_of_active_cards(rs.getBigDecimal("r46_closing_bal_of_active_cards"));

			// R47
			obj.setR47_debit_district(rs.getString("r47_debit_district"));
			obj.setR47_opening_no_of_cards(rs.getBigDecimal("r47_opening_no_of_cards"));
			obj.setR47_no_of_cards_issued(rs.getBigDecimal("r47_no_of_cards_issued"));
			obj.setR47_no_cards_of_closed(rs.getBigDecimal("r47_no_cards_of_closed"));
			obj.setR47_closing_bal_of_active_cards(rs.getBigDecimal("r47_closing_bal_of_active_cards"));

			// R48
			obj.setR48_debit_district(rs.getString("r48_debit_district"));
			obj.setR48_opening_no_of_cards(rs.getBigDecimal("r48_opening_no_of_cards"));
			obj.setR48_no_of_cards_issued(rs.getBigDecimal("r48_no_of_cards_issued"));
			obj.setR48_no_cards_of_closed(rs.getBigDecimal("r48_no_cards_of_closed"));
			obj.setR48_closing_bal_of_active_cards(rs.getBigDecimal("r48_closing_bal_of_active_cards"));

			// R49
			obj.setR49_debit_district(rs.getString("r49_debit_district"));
			obj.setR49_opening_no_of_cards(rs.getBigDecimal("r49_opening_no_of_cards"));
			obj.setR49_no_of_cards_issued(rs.getBigDecimal("r49_no_of_cards_issued"));
			obj.setR49_no_cards_of_closed(rs.getBigDecimal("r49_no_cards_of_closed"));
			obj.setR49_closing_bal_of_active_cards(rs.getBigDecimal("r49_closing_bal_of_active_cards"));

			// R50
			obj.setR50_debit_district(rs.getString("r50_debit_district"));
			obj.setR50_opening_no_of_cards(rs.getBigDecimal("r50_opening_no_of_cards"));
			obj.setR50_no_of_cards_issued(rs.getBigDecimal("r50_no_of_cards_issued"));
			obj.setR50_no_cards_of_closed(rs.getBigDecimal("r50_no_cards_of_closed"));
			obj.setR50_closing_bal_of_active_cards(rs.getBigDecimal("r50_closing_bal_of_active_cards"));

			// R55
			obj.setR55_credit_district(rs.getString("r55_credit_district"));
			obj.setR55_opening_no_of_cards(rs.getBigDecimal("r55_opening_no_of_cards"));
			obj.setR55_no_of_cards_issued(rs.getBigDecimal("r55_no_of_cards_issued"));
			obj.setR55_no_cards_of_closed(rs.getBigDecimal("r55_no_cards_of_closed"));
			obj.setR55_closing_bal_of_active_cards(rs.getBigDecimal("r55_closing_bal_of_active_cards"));

			// R56
			obj.setR56_credit_district(rs.getString("r56_credit_district"));
			obj.setR56_opening_no_of_cards(rs.getBigDecimal("r56_opening_no_of_cards"));
			obj.setR56_no_of_cards_issued(rs.getBigDecimal("r56_no_of_cards_issued"));
			obj.setR56_no_cards_of_closed(rs.getBigDecimal("r56_no_cards_of_closed"));
			obj.setR56_closing_bal_of_active_cards(rs.getBigDecimal("r56_closing_bal_of_active_cards"));

			// R57
			obj.setR57_credit_district(rs.getString("r57_credit_district"));
			obj.setR57_opening_no_of_cards(rs.getBigDecimal("r57_opening_no_of_cards"));
			obj.setR57_no_of_cards_issued(rs.getBigDecimal("r57_no_of_cards_issued"));
			obj.setR57_no_cards_of_closed(rs.getBigDecimal("r57_no_cards_of_closed"));
			obj.setR57_closing_bal_of_active_cards(rs.getBigDecimal("r57_closing_bal_of_active_cards"));

			// R58
			obj.setR58_credit_district(rs.getString("r58_credit_district"));
			obj.setR58_opening_no_of_cards(rs.getBigDecimal("r58_opening_no_of_cards"));
			obj.setR58_no_of_cards_issued(rs.getBigDecimal("r58_no_of_cards_issued"));
			obj.setR58_no_cards_of_closed(rs.getBigDecimal("r58_no_cards_of_closed"));
			obj.setR58_closing_bal_of_active_cards(rs.getBigDecimal("r58_closing_bal_of_active_cards"));

			// R59
			obj.setR59_credit_district(rs.getString("r59_credit_district"));
			obj.setR59_opening_no_of_cards(rs.getBigDecimal("r59_opening_no_of_cards"));
			obj.setR59_no_of_cards_issued(rs.getBigDecimal("r59_no_of_cards_issued"));
			obj.setR59_no_cards_of_closed(rs.getBigDecimal("r59_no_cards_of_closed"));
			obj.setR59_closing_bal_of_active_cards(rs.getBigDecimal("r59_closing_bal_of_active_cards"));

			// R60
			obj.setR60_credit_district(rs.getString("r60_credit_district"));
			obj.setR60_opening_no_of_cards(rs.getBigDecimal("r60_opening_no_of_cards"));
			obj.setR60_no_of_cards_issued(rs.getBigDecimal("r60_no_of_cards_issued"));
			obj.setR60_no_cards_of_closed(rs.getBigDecimal("r60_no_cards_of_closed"));
			obj.setR60_closing_bal_of_active_cards(rs.getBigDecimal("r60_closing_bal_of_active_cards"));

			// R61
			obj.setR61_credit_district(rs.getString("r61_credit_district"));
			obj.setR61_opening_no_of_cards(rs.getBigDecimal("r61_opening_no_of_cards"));
			obj.setR61_no_of_cards_issued(rs.getBigDecimal("r61_no_of_cards_issued"));
			obj.setR61_no_cards_of_closed(rs.getBigDecimal("r61_no_cards_of_closed"));
			obj.setR61_closing_bal_of_active_cards(rs.getBigDecimal("r61_closing_bal_of_active_cards"));

			// R62
			obj.setR62_credit_district(rs.getString("r62_credit_district"));
			obj.setR62_opening_no_of_cards(rs.getBigDecimal("r62_opening_no_of_cards"));
			obj.setR62_no_of_cards_issued(rs.getBigDecimal("r62_no_of_cards_issued"));
			obj.setR62_no_cards_of_closed(rs.getBigDecimal("r62_no_cards_of_closed"));
			obj.setR62_closing_bal_of_active_cards(rs.getBigDecimal("r62_closing_bal_of_active_cards"));

			// R63
			obj.setR63_credit_district(rs.getString("r63_credit_district"));
			obj.setR63_opening_no_of_cards(rs.getBigDecimal("r63_opening_no_of_cards"));
			obj.setR63_no_of_cards_issued(rs.getBigDecimal("r63_no_of_cards_issued"));
			obj.setR63_no_cards_of_closed(rs.getBigDecimal("r63_no_cards_of_closed"));
			obj.setR63_closing_bal_of_active_cards(rs.getBigDecimal("r63_closing_bal_of_active_cards"));

			// R64
			obj.setR64_credit_district(rs.getString("r64_credit_district"));
			obj.setR64_opening_no_of_cards(rs.getBigDecimal("r64_opening_no_of_cards"));
			obj.setR64_no_of_cards_issued(rs.getBigDecimal("r64_no_of_cards_issued"));
			obj.setR64_no_cards_of_closed(rs.getBigDecimal("r64_no_cards_of_closed"));
			obj.setR64_closing_bal_of_active_cards(rs.getBigDecimal("r64_closing_bal_of_active_cards"));

			// R65
			obj.setR65_credit_district(rs.getString("r65_credit_district"));
			obj.setR65_opening_no_of_cards(rs.getBigDecimal("r65_opening_no_of_cards"));
			obj.setR65_no_of_cards_issued(rs.getBigDecimal("r65_no_of_cards_issued"));
			obj.setR65_no_cards_of_closed(rs.getBigDecimal("r65_no_cards_of_closed"));
			obj.setR65_closing_bal_of_active_cards(rs.getBigDecimal("r65_closing_bal_of_active_cards"));

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

	public class Q_BRANCHNET_Resub_Detail_Entity {
		private String r10_bran_sub_bran_district;
		private BigDecimal r10_no1_of_branches;
		private BigDecimal r10_no1_of_sub_branches;
		private BigDecimal r10_no1_of_agencies;

		private String r11_bran_sub_bran_district;
		private BigDecimal r11_no1_of_branches;
		private BigDecimal r11_no1_of_sub_branches;
		private BigDecimal r11_no1_of_agencies;

		private String r12_bran_sub_bran_district;
		private BigDecimal r12_no1_of_branches;
		private BigDecimal r12_no1_of_sub_branches;
		private BigDecimal r12_no1_of_agencies;

		private String r13_bran_sub_bran_district;
		private BigDecimal r13_no1_of_branches;
		private BigDecimal r13_no1_of_sub_branches;
		private BigDecimal r13_no1_of_agencies;

		private String r14_bran_sub_bran_district;
		private BigDecimal r14_no1_of_branches;
		private BigDecimal r14_no1_of_sub_branches;
		private BigDecimal r14_no1_of_agencies;

		private String r15_bran_sub_bran_district;
		private BigDecimal r15_no1_of_branches;
		private BigDecimal r15_no1_of_sub_branches;
		private BigDecimal r15_no1_of_agencies;

		private String r16_bran_sub_bran_district;
		private BigDecimal r16_no1_of_branches;
		private BigDecimal r16_no1_of_sub_branches;
		private BigDecimal r16_no1_of_agencies;

		private String r17_bran_sub_bran_district;
		private BigDecimal r17_no1_of_branches;
		private BigDecimal r17_no1_of_sub_branches;
		private BigDecimal r17_no1_of_agencies;

		private String r18_bran_sub_bran_district;
		private BigDecimal r18_no1_of_branches;
		private BigDecimal r18_no1_of_sub_branches;
		private BigDecimal r18_no1_of_agencies;

		private String r19_bran_sub_bran_district;
		private BigDecimal r19_no1_of_branches;
		private BigDecimal r19_no1_of_sub_branches;
		private BigDecimal r19_no1_of_agencies;

		private String r20_bran_sub_bran_district;
		private BigDecimal r20_no1_of_branches;
		private BigDecimal r20_no1_of_sub_branches;
		private BigDecimal r20_no1_of_agencies;
		private String r25_atm_mini_atm_district;
		private BigDecimal r25_no_of_atms;
		private BigDecimal r25_no_of_mini_atms;
		private BigDecimal r25_encashment_points;

		private String r26_atm_mini_atm_district;
		private BigDecimal r26_no_of_atms;
		private BigDecimal r26_no_of_mini_atms;
		private BigDecimal r26_encashment_points;

		private String r27_atm_mini_atm_district;
		private BigDecimal r27_no_of_atms;
		private BigDecimal r27_no_of_mini_atms;
		private BigDecimal r27_encashment_points;

		private String r28_atm_mini_atm_district;
		private BigDecimal r28_no_of_atms;
		private BigDecimal r28_no_of_mini_atms;
		private BigDecimal r28_encashment_points;

		private String r29_atm_mini_atm_district;
		private BigDecimal r29_no_of_atms;
		private BigDecimal r29_no_of_mini_atms;
		private BigDecimal r29_encashment_points;

		private String r30_atm_mini_atm_district;
		private BigDecimal r30_no_of_atms;
		private BigDecimal r30_no_of_mini_atms;
		private BigDecimal r30_encashment_points;

		private String r31_atm_mini_atm_district;
		private BigDecimal r31_no_of_atms;
		private BigDecimal r31_no_of_mini_atms;
		private BigDecimal r31_encashment_points;

		private String r32_atm_mini_atm_district;
		private BigDecimal r32_no_of_atms;
		private BigDecimal r32_no_of_mini_atms;
		private BigDecimal r32_encashment_points;

		private String r33_atm_mini_atm_district;
		private BigDecimal r33_no_of_atms;
		private BigDecimal r33_no_of_mini_atms;
		private BigDecimal r33_encashment_points;

		private String r34_atm_mini_atm_district;
		private BigDecimal r34_no_of_atms;
		private BigDecimal r34_no_of_mini_atms;
		private BigDecimal r34_encashment_points;

		private String r35_atm_mini_atm_district;
		private BigDecimal r35_no_of_atms;
		private BigDecimal r35_no_of_mini_atms;
		private BigDecimal r35_encashment_points;

		private String r40_debit_district;
		private BigDecimal r40_opening_no_of_cards;
		private BigDecimal r40_no_of_cards_issued;
		private BigDecimal r40_no_cards_of_closed;
		private BigDecimal r40_closing_bal_of_active_cards;

		private String r41_debit_district;
		private BigDecimal r41_opening_no_of_cards;
		private BigDecimal r41_no_of_cards_issued;
		private BigDecimal r41_no_cards_of_closed;
		private BigDecimal r41_closing_bal_of_active_cards;

		private String r42_debit_district;
		private BigDecimal r42_opening_no_of_cards;
		private BigDecimal r42_no_of_cards_issued;
		private BigDecimal r42_no_cards_of_closed;
		private BigDecimal r42_closing_bal_of_active_cards;

		private String r43_debit_district;
		private BigDecimal r43_opening_no_of_cards;
		private BigDecimal r43_no_of_cards_issued;
		private BigDecimal r43_no_cards_of_closed;
		private BigDecimal r43_closing_bal_of_active_cards;

		private String r44_debit_district;
		private BigDecimal r44_opening_no_of_cards;
		private BigDecimal r44_no_of_cards_issued;
		private BigDecimal r44_no_cards_of_closed;
		private BigDecimal r44_closing_bal_of_active_cards;

		private String r45_debit_district;
		private BigDecimal r45_opening_no_of_cards;
		private BigDecimal r45_no_of_cards_issued;
		private BigDecimal r45_no_cards_of_closed;
		private BigDecimal r45_closing_bal_of_active_cards;

		private String r46_debit_district;
		private BigDecimal r46_opening_no_of_cards;
		private BigDecimal r46_no_of_cards_issued;
		private BigDecimal r46_no_cards_of_closed;
		private BigDecimal r46_closing_bal_of_active_cards;

		private String r47_debit_district;
		private BigDecimal r47_opening_no_of_cards;
		private BigDecimal r47_no_of_cards_issued;
		private BigDecimal r47_no_cards_of_closed;
		private BigDecimal r47_closing_bal_of_active_cards;

		private String r48_debit_district;
		private BigDecimal r48_opening_no_of_cards;
		private BigDecimal r48_no_of_cards_issued;
		private BigDecimal r48_no_cards_of_closed;
		private BigDecimal r48_closing_bal_of_active_cards;

		private String r49_debit_district;
		private BigDecimal r49_opening_no_of_cards;
		private BigDecimal r49_no_of_cards_issued;
		private BigDecimal r49_no_cards_of_closed;
		private BigDecimal r49_closing_bal_of_active_cards;

		private String r50_debit_district;
		private BigDecimal r50_opening_no_of_cards;
		private BigDecimal r50_no_of_cards_issued;
		private BigDecimal r50_no_cards_of_closed;
		private BigDecimal r50_closing_bal_of_active_cards;

		private String r55_credit_district;
		private BigDecimal r55_opening_no_of_cards;
		private BigDecimal r55_no_of_cards_issued;
		private BigDecimal r55_no_cards_of_closed;
		private BigDecimal r55_closing_bal_of_active_cards;

		private String r56_credit_district;
		private BigDecimal r56_opening_no_of_cards;
		private BigDecimal r56_no_of_cards_issued;
		private BigDecimal r56_no_cards_of_closed;
		private BigDecimal r56_closing_bal_of_active_cards;

		private String r57_credit_district;
		private BigDecimal r57_opening_no_of_cards;
		private BigDecimal r57_no_of_cards_issued;
		private BigDecimal r57_no_cards_of_closed;
		private BigDecimal r57_closing_bal_of_active_cards;

		private String r58_credit_district;
		private BigDecimal r58_opening_no_of_cards;
		private BigDecimal r58_no_of_cards_issued;
		private BigDecimal r58_no_cards_of_closed;
		private BigDecimal r58_closing_bal_of_active_cards;

		private String r59_credit_district;
		private BigDecimal r59_opening_no_of_cards;
		private BigDecimal r59_no_of_cards_issued;
		private BigDecimal r59_no_cards_of_closed;
		private BigDecimal r59_closing_bal_of_active_cards;

		private String r60_credit_district;
		private BigDecimal r60_opening_no_of_cards;
		private BigDecimal r60_no_of_cards_issued;
		private BigDecimal r60_no_cards_of_closed;
		private BigDecimal r60_closing_bal_of_active_cards;

		private String r61_credit_district;
		private BigDecimal r61_opening_no_of_cards;
		private BigDecimal r61_no_of_cards_issued;
		private BigDecimal r61_no_cards_of_closed;
		private BigDecimal r61_closing_bal_of_active_cards;

		private String r62_credit_district;
		private BigDecimal r62_opening_no_of_cards;
		private BigDecimal r62_no_of_cards_issued;
		private BigDecimal r62_no_cards_of_closed;
		private BigDecimal r62_closing_bal_of_active_cards;

		private String r63_credit_district;
		private BigDecimal r63_opening_no_of_cards;
		private BigDecimal r63_no_of_cards_issued;
		private BigDecimal r63_no_cards_of_closed;
		private BigDecimal r63_closing_bal_of_active_cards;

		private String r64_credit_district;
		private BigDecimal r64_opening_no_of_cards;
		private BigDecimal r64_no_of_cards_issued;
		private BigDecimal r64_no_cards_of_closed;
		private BigDecimal r64_closing_bal_of_active_cards;

		private String r65_credit_district;
		private BigDecimal r65_opening_no_of_cards;
		private BigDecimal r65_no_of_cards_issued;
		private BigDecimal r65_no_cards_of_closed;
		private BigDecimal r65_closing_bal_of_active_cards;
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

		public String getR10_bran_sub_bran_district() {
			return r10_bran_sub_bran_district;
		}

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

		public void setR10_bran_sub_bran_district(String r10_bran_sub_bran_district) {
			this.r10_bran_sub_bran_district = r10_bran_sub_bran_district;
		}

		public BigDecimal getR10_no1_of_branches() {
			return r10_no1_of_branches;
		}

		public void setR10_no1_of_branches(BigDecimal r10_no1_of_branches) {
			this.r10_no1_of_branches = r10_no1_of_branches;
		}

		public BigDecimal getR10_no1_of_sub_branches() {
			return r10_no1_of_sub_branches;
		}

		public void setR10_no1_of_sub_branches(BigDecimal r10_no1_of_sub_branches) {
			this.r10_no1_of_sub_branches = r10_no1_of_sub_branches;
		}

		public BigDecimal getR10_no1_of_agencies() {
			return r10_no1_of_agencies;
		}

		public void setR10_no1_of_agencies(BigDecimal r10_no1_of_agencies) {
			this.r10_no1_of_agencies = r10_no1_of_agencies;
		}

		public String getR11_bran_sub_bran_district() {
			return r11_bran_sub_bran_district;
		}

		public void setR11_bran_sub_bran_district(String r11_bran_sub_bran_district) {
			this.r11_bran_sub_bran_district = r11_bran_sub_bran_district;
		}

		public BigDecimal getR11_no1_of_branches() {
			return r11_no1_of_branches;
		}

		public void setR11_no1_of_branches(BigDecimal r11_no1_of_branches) {
			this.r11_no1_of_branches = r11_no1_of_branches;
		}

		public BigDecimal getR11_no1_of_sub_branches() {
			return r11_no1_of_sub_branches;
		}

		public void setR11_no1_of_sub_branches(BigDecimal r11_no1_of_sub_branches) {
			this.r11_no1_of_sub_branches = r11_no1_of_sub_branches;
		}

		public BigDecimal getR11_no1_of_agencies() {
			return r11_no1_of_agencies;
		}

		public void setR11_no1_of_agencies(BigDecimal r11_no1_of_agencies) {
			this.r11_no1_of_agencies = r11_no1_of_agencies;
		}

		public String getR12_bran_sub_bran_district() {
			return r12_bran_sub_bran_district;
		}

		public void setR12_bran_sub_bran_district(String r12_bran_sub_bran_district) {
			this.r12_bran_sub_bran_district = r12_bran_sub_bran_district;
		}

		public BigDecimal getR12_no1_of_branches() {
			return r12_no1_of_branches;
		}

		public void setR12_no1_of_branches(BigDecimal r12_no1_of_branches) {
			this.r12_no1_of_branches = r12_no1_of_branches;
		}

		public BigDecimal getR12_no1_of_sub_branches() {
			return r12_no1_of_sub_branches;
		}

		public void setR12_no1_of_sub_branches(BigDecimal r12_no1_of_sub_branches) {
			this.r12_no1_of_sub_branches = r12_no1_of_sub_branches;
		}

		public BigDecimal getR12_no1_of_agencies() {
			return r12_no1_of_agencies;
		}

		public void setR12_no1_of_agencies(BigDecimal r12_no1_of_agencies) {
			this.r12_no1_of_agencies = r12_no1_of_agencies;
		}

		public String getR13_bran_sub_bran_district() {
			return r13_bran_sub_bran_district;
		}

		public void setR13_bran_sub_bran_district(String r13_bran_sub_bran_district) {
			this.r13_bran_sub_bran_district = r13_bran_sub_bran_district;
		}

		public BigDecimal getR13_no1_of_branches() {
			return r13_no1_of_branches;
		}

		public void setR13_no1_of_branches(BigDecimal r13_no1_of_branches) {
			this.r13_no1_of_branches = r13_no1_of_branches;
		}

		public BigDecimal getR13_no1_of_sub_branches() {
			return r13_no1_of_sub_branches;
		}

		public void setR13_no1_of_sub_branches(BigDecimal r13_no1_of_sub_branches) {
			this.r13_no1_of_sub_branches = r13_no1_of_sub_branches;
		}

		public BigDecimal getR13_no1_of_agencies() {
			return r13_no1_of_agencies;
		}

		public void setR13_no1_of_agencies(BigDecimal r13_no1_of_agencies) {
			this.r13_no1_of_agencies = r13_no1_of_agencies;
		}

		public String getR14_bran_sub_bran_district() {
			return r14_bran_sub_bran_district;
		}

		public void setR14_bran_sub_bran_district(String r14_bran_sub_bran_district) {
			this.r14_bran_sub_bran_district = r14_bran_sub_bran_district;
		}

		public BigDecimal getR14_no1_of_branches() {
			return r14_no1_of_branches;
		}

		public void setR14_no1_of_branches(BigDecimal r14_no1_of_branches) {
			this.r14_no1_of_branches = r14_no1_of_branches;
		}

		public BigDecimal getR14_no1_of_sub_branches() {
			return r14_no1_of_sub_branches;
		}

		public void setR14_no1_of_sub_branches(BigDecimal r14_no1_of_sub_branches) {
			this.r14_no1_of_sub_branches = r14_no1_of_sub_branches;
		}

		public BigDecimal getR14_no1_of_agencies() {
			return r14_no1_of_agencies;
		}

		public void setR14_no1_of_agencies(BigDecimal r14_no1_of_agencies) {
			this.r14_no1_of_agencies = r14_no1_of_agencies;
		}

		public String getR15_bran_sub_bran_district() {
			return r15_bran_sub_bran_district;
		}

		public void setR15_bran_sub_bran_district(String r15_bran_sub_bran_district) {
			this.r15_bran_sub_bran_district = r15_bran_sub_bran_district;
		}

		public BigDecimal getR15_no1_of_branches() {
			return r15_no1_of_branches;
		}

		public void setR15_no1_of_branches(BigDecimal r15_no1_of_branches) {
			this.r15_no1_of_branches = r15_no1_of_branches;
		}

		public BigDecimal getR15_no1_of_sub_branches() {
			return r15_no1_of_sub_branches;
		}

		public void setR15_no1_of_sub_branches(BigDecimal r15_no1_of_sub_branches) {
			this.r15_no1_of_sub_branches = r15_no1_of_sub_branches;
		}

		public BigDecimal getR15_no1_of_agencies() {
			return r15_no1_of_agencies;
		}

		public void setR15_no1_of_agencies(BigDecimal r15_no1_of_agencies) {
			this.r15_no1_of_agencies = r15_no1_of_agencies;
		}

		public String getR16_bran_sub_bran_district() {
			return r16_bran_sub_bran_district;
		}

		public void setR16_bran_sub_bran_district(String r16_bran_sub_bran_district) {
			this.r16_bran_sub_bran_district = r16_bran_sub_bran_district;
		}

		public BigDecimal getR16_no1_of_branches() {
			return r16_no1_of_branches;
		}

		public void setR16_no1_of_branches(BigDecimal r16_no1_of_branches) {
			this.r16_no1_of_branches = r16_no1_of_branches;
		}

		public BigDecimal getR16_no1_of_sub_branches() {
			return r16_no1_of_sub_branches;
		}

		public void setR16_no1_of_sub_branches(BigDecimal r16_no1_of_sub_branches) {
			this.r16_no1_of_sub_branches = r16_no1_of_sub_branches;
		}

		public BigDecimal getR16_no1_of_agencies() {
			return r16_no1_of_agencies;
		}

		public void setR16_no1_of_agencies(BigDecimal r16_no1_of_agencies) {
			this.r16_no1_of_agencies = r16_no1_of_agencies;
		}

		public String getR17_bran_sub_bran_district() {
			return r17_bran_sub_bran_district;
		}

		public void setR17_bran_sub_bran_district(String r17_bran_sub_bran_district) {
			this.r17_bran_sub_bran_district = r17_bran_sub_bran_district;
		}

		public BigDecimal getR17_no1_of_branches() {
			return r17_no1_of_branches;
		}

		public void setR17_no1_of_branches(BigDecimal r17_no1_of_branches) {
			this.r17_no1_of_branches = r17_no1_of_branches;
		}

		public BigDecimal getR17_no1_of_sub_branches() {
			return r17_no1_of_sub_branches;
		}

		public void setR17_no1_of_sub_branches(BigDecimal r17_no1_of_sub_branches) {
			this.r17_no1_of_sub_branches = r17_no1_of_sub_branches;
		}

		public BigDecimal getR17_no1_of_agencies() {
			return r17_no1_of_agencies;
		}

		public void setR17_no1_of_agencies(BigDecimal r17_no1_of_agencies) {
			this.r17_no1_of_agencies = r17_no1_of_agencies;
		}

		public String getR18_bran_sub_bran_district() {
			return r18_bran_sub_bran_district;
		}

		public void setR18_bran_sub_bran_district(String r18_bran_sub_bran_district) {
			this.r18_bran_sub_bran_district = r18_bran_sub_bran_district;
		}

		public BigDecimal getR18_no1_of_branches() {
			return r18_no1_of_branches;
		}

		public void setR18_no1_of_branches(BigDecimal r18_no1_of_branches) {
			this.r18_no1_of_branches = r18_no1_of_branches;
		}

		public BigDecimal getR18_no1_of_sub_branches() {
			return r18_no1_of_sub_branches;
		}

		public void setR18_no1_of_sub_branches(BigDecimal r18_no1_of_sub_branches) {
			this.r18_no1_of_sub_branches = r18_no1_of_sub_branches;
		}

		public BigDecimal getR18_no1_of_agencies() {
			return r18_no1_of_agencies;
		}

		public void setR18_no1_of_agencies(BigDecimal r18_no1_of_agencies) {
			this.r18_no1_of_agencies = r18_no1_of_agencies;
		}

		public String getR19_bran_sub_bran_district() {
			return r19_bran_sub_bran_district;
		}

		public void setR19_bran_sub_bran_district(String r19_bran_sub_bran_district) {
			this.r19_bran_sub_bran_district = r19_bran_sub_bran_district;
		}

		public BigDecimal getR19_no1_of_branches() {
			return r19_no1_of_branches;
		}

		public void setR19_no1_of_branches(BigDecimal r19_no1_of_branches) {
			this.r19_no1_of_branches = r19_no1_of_branches;
		}

		public BigDecimal getR19_no1_of_sub_branches() {
			return r19_no1_of_sub_branches;
		}

		public void setR19_no1_of_sub_branches(BigDecimal r19_no1_of_sub_branches) {
			this.r19_no1_of_sub_branches = r19_no1_of_sub_branches;
		}

		public BigDecimal getR19_no1_of_agencies() {
			return r19_no1_of_agencies;
		}

		public void setR19_no1_of_agencies(BigDecimal r19_no1_of_agencies) {
			this.r19_no1_of_agencies = r19_no1_of_agencies;
		}

		public String getR20_bran_sub_bran_district() {
			return r20_bran_sub_bran_district;
		}

		public void setR20_bran_sub_bran_district(String r20_bran_sub_bran_district) {
			this.r20_bran_sub_bran_district = r20_bran_sub_bran_district;
		}

		public BigDecimal getR20_no1_of_branches() {
			return r20_no1_of_branches;
		}

		public void setR20_no1_of_branches(BigDecimal r20_no1_of_branches) {
			this.r20_no1_of_branches = r20_no1_of_branches;
		}

		public BigDecimal getR20_no1_of_sub_branches() {
			return r20_no1_of_sub_branches;
		}

		public void setR20_no1_of_sub_branches(BigDecimal r20_no1_of_sub_branches) {
			this.r20_no1_of_sub_branches = r20_no1_of_sub_branches;
		}

		public BigDecimal getR20_no1_of_agencies() {
			return r20_no1_of_agencies;
		}

		public void setR20_no1_of_agencies(BigDecimal r20_no1_of_agencies) {
			this.r20_no1_of_agencies = r20_no1_of_agencies;
		}

		public String getR25_atm_mini_atm_district() {
			return r25_atm_mini_atm_district;
		}

		public void setR25_atm_mini_atm_district(String r25_atm_mini_atm_district) {
			this.r25_atm_mini_atm_district = r25_atm_mini_atm_district;
		}

		public BigDecimal getR25_no_of_atms() {
			return r25_no_of_atms;
		}

		public void setR25_no_of_atms(BigDecimal r25_no_of_atms) {
			this.r25_no_of_atms = r25_no_of_atms;
		}

		public BigDecimal getR25_no_of_mini_atms() {
			return r25_no_of_mini_atms;
		}

		public void setR25_no_of_mini_atms(BigDecimal r25_no_of_mini_atms) {
			this.r25_no_of_mini_atms = r25_no_of_mini_atms;
		}

		public BigDecimal getR25_encashment_points() {
			return r25_encashment_points;
		}

		public void setR25_encashment_points(BigDecimal r25_encashment_points) {
			this.r25_encashment_points = r25_encashment_points;
		}

		public String getR26_atm_mini_atm_district() {
			return r26_atm_mini_atm_district;
		}

		public void setR26_atm_mini_atm_district(String r26_atm_mini_atm_district) {
			this.r26_atm_mini_atm_district = r26_atm_mini_atm_district;
		}

		public BigDecimal getR26_no_of_atms() {
			return r26_no_of_atms;
		}

		public void setR26_no_of_atms(BigDecimal r26_no_of_atms) {
			this.r26_no_of_atms = r26_no_of_atms;
		}

		public BigDecimal getR26_no_of_mini_atms() {
			return r26_no_of_mini_atms;
		}

		public void setR26_no_of_mini_atms(BigDecimal r26_no_of_mini_atms) {
			this.r26_no_of_mini_atms = r26_no_of_mini_atms;
		}

		public BigDecimal getR26_encashment_points() {
			return r26_encashment_points;
		}

		public void setR26_encashment_points(BigDecimal r26_encashment_points) {
			this.r26_encashment_points = r26_encashment_points;
		}

		public String getR27_atm_mini_atm_district() {
			return r27_atm_mini_atm_district;
		}

		public void setR27_atm_mini_atm_district(String r27_atm_mini_atm_district) {
			this.r27_atm_mini_atm_district = r27_atm_mini_atm_district;
		}

		public BigDecimal getR27_no_of_atms() {
			return r27_no_of_atms;
		}

		public void setR27_no_of_atms(BigDecimal r27_no_of_atms) {
			this.r27_no_of_atms = r27_no_of_atms;
		}

		public BigDecimal getR27_no_of_mini_atms() {
			return r27_no_of_mini_atms;
		}

		public void setR27_no_of_mini_atms(BigDecimal r27_no_of_mini_atms) {
			this.r27_no_of_mini_atms = r27_no_of_mini_atms;
		}

		public BigDecimal getR27_encashment_points() {
			return r27_encashment_points;
		}

		public void setR27_encashment_points(BigDecimal r27_encashment_points) {
			this.r27_encashment_points = r27_encashment_points;
		}

		public String getR28_atm_mini_atm_district() {
			return r28_atm_mini_atm_district;
		}

		public void setR28_atm_mini_atm_district(String r28_atm_mini_atm_district) {
			this.r28_atm_mini_atm_district = r28_atm_mini_atm_district;
		}

		public BigDecimal getR28_no_of_atms() {
			return r28_no_of_atms;
		}

		public void setR28_no_of_atms(BigDecimal r28_no_of_atms) {
			this.r28_no_of_atms = r28_no_of_atms;
		}

		public BigDecimal getR28_no_of_mini_atms() {
			return r28_no_of_mini_atms;
		}

		public void setR28_no_of_mini_atms(BigDecimal r28_no_of_mini_atms) {
			this.r28_no_of_mini_atms = r28_no_of_mini_atms;
		}

		public BigDecimal getR28_encashment_points() {
			return r28_encashment_points;
		}

		public void setR28_encashment_points(BigDecimal r28_encashment_points) {
			this.r28_encashment_points = r28_encashment_points;
		}

		public String getR29_atm_mini_atm_district() {
			return r29_atm_mini_atm_district;
		}

		public void setR29_atm_mini_atm_district(String r29_atm_mini_atm_district) {
			this.r29_atm_mini_atm_district = r29_atm_mini_atm_district;
		}

		public BigDecimal getR29_no_of_atms() {
			return r29_no_of_atms;
		}

		public void setR29_no_of_atms(BigDecimal r29_no_of_atms) {
			this.r29_no_of_atms = r29_no_of_atms;
		}

		public BigDecimal getR29_no_of_mini_atms() {
			return r29_no_of_mini_atms;
		}

		public void setR29_no_of_mini_atms(BigDecimal r29_no_of_mini_atms) {
			this.r29_no_of_mini_atms = r29_no_of_mini_atms;
		}

		public BigDecimal getR29_encashment_points() {
			return r29_encashment_points;
		}

		public void setR29_encashment_points(BigDecimal r29_encashment_points) {
			this.r29_encashment_points = r29_encashment_points;
		}

		public String getR30_atm_mini_atm_district() {
			return r30_atm_mini_atm_district;
		}

		public void setR30_atm_mini_atm_district(String r30_atm_mini_atm_district) {
			this.r30_atm_mini_atm_district = r30_atm_mini_atm_district;
		}

		public BigDecimal getR30_no_of_atms() {
			return r30_no_of_atms;
		}

		public void setR30_no_of_atms(BigDecimal r30_no_of_atms) {
			this.r30_no_of_atms = r30_no_of_atms;
		}

		public BigDecimal getR30_no_of_mini_atms() {
			return r30_no_of_mini_atms;
		}

		public void setR30_no_of_mini_atms(BigDecimal r30_no_of_mini_atms) {
			this.r30_no_of_mini_atms = r30_no_of_mini_atms;
		}

		public BigDecimal getR30_encashment_points() {
			return r30_encashment_points;
		}

		public void setR30_encashment_points(BigDecimal r30_encashment_points) {
			this.r30_encashment_points = r30_encashment_points;
		}

		public String getR31_atm_mini_atm_district() {
			return r31_atm_mini_atm_district;
		}

		public void setR31_atm_mini_atm_district(String r31_atm_mini_atm_district) {
			this.r31_atm_mini_atm_district = r31_atm_mini_atm_district;
		}

		public BigDecimal getR31_no_of_atms() {
			return r31_no_of_atms;
		}

		public void setR31_no_of_atms(BigDecimal r31_no_of_atms) {
			this.r31_no_of_atms = r31_no_of_atms;
		}

		public BigDecimal getR31_no_of_mini_atms() {
			return r31_no_of_mini_atms;
		}

		public void setR31_no_of_mini_atms(BigDecimal r31_no_of_mini_atms) {
			this.r31_no_of_mini_atms = r31_no_of_mini_atms;
		}

		public BigDecimal getR31_encashment_points() {
			return r31_encashment_points;
		}

		public void setR31_encashment_points(BigDecimal r31_encashment_points) {
			this.r31_encashment_points = r31_encashment_points;
		}

		public String getR32_atm_mini_atm_district() {
			return r32_atm_mini_atm_district;
		}

		public void setR32_atm_mini_atm_district(String r32_atm_mini_atm_district) {
			this.r32_atm_mini_atm_district = r32_atm_mini_atm_district;
		}

		public BigDecimal getR32_no_of_atms() {
			return r32_no_of_atms;
		}

		public void setR32_no_of_atms(BigDecimal r32_no_of_atms) {
			this.r32_no_of_atms = r32_no_of_atms;
		}

		public BigDecimal getR32_no_of_mini_atms() {
			return r32_no_of_mini_atms;
		}

		public void setR32_no_of_mini_atms(BigDecimal r32_no_of_mini_atms) {
			this.r32_no_of_mini_atms = r32_no_of_mini_atms;
		}

		public BigDecimal getR32_encashment_points() {
			return r32_encashment_points;
		}

		public void setR32_encashment_points(BigDecimal r32_encashment_points) {
			this.r32_encashment_points = r32_encashment_points;
		}

		public String getR33_atm_mini_atm_district() {
			return r33_atm_mini_atm_district;
		}

		public void setR33_atm_mini_atm_district(String r33_atm_mini_atm_district) {
			this.r33_atm_mini_atm_district = r33_atm_mini_atm_district;
		}

		public BigDecimal getR33_no_of_atms() {
			return r33_no_of_atms;
		}

		public void setR33_no_of_atms(BigDecimal r33_no_of_atms) {
			this.r33_no_of_atms = r33_no_of_atms;
		}

		public BigDecimal getR33_no_of_mini_atms() {
			return r33_no_of_mini_atms;
		}

		public void setR33_no_of_mini_atms(BigDecimal r33_no_of_mini_atms) {
			this.r33_no_of_mini_atms = r33_no_of_mini_atms;
		}

		public BigDecimal getR33_encashment_points() {
			return r33_encashment_points;
		}

		public void setR33_encashment_points(BigDecimal r33_encashment_points) {
			this.r33_encashment_points = r33_encashment_points;
		}

		public String getR34_atm_mini_atm_district() {
			return r34_atm_mini_atm_district;
		}

		public void setR34_atm_mini_atm_district(String r34_atm_mini_atm_district) {
			this.r34_atm_mini_atm_district = r34_atm_mini_atm_district;
		}

		public BigDecimal getR34_no_of_atms() {
			return r34_no_of_atms;
		}

		public void setR34_no_of_atms(BigDecimal r34_no_of_atms) {
			this.r34_no_of_atms = r34_no_of_atms;
		}

		public BigDecimal getR34_no_of_mini_atms() {
			return r34_no_of_mini_atms;
		}

		public void setR34_no_of_mini_atms(BigDecimal r34_no_of_mini_atms) {
			this.r34_no_of_mini_atms = r34_no_of_mini_atms;
		}

		public BigDecimal getR34_encashment_points() {
			return r34_encashment_points;
		}

		public void setR34_encashment_points(BigDecimal r34_encashment_points) {
			this.r34_encashment_points = r34_encashment_points;
		}

		public String getR35_atm_mini_atm_district() {
			return r35_atm_mini_atm_district;
		}

		public void setR35_atm_mini_atm_district(String r35_atm_mini_atm_district) {
			this.r35_atm_mini_atm_district = r35_atm_mini_atm_district;
		}

		public BigDecimal getR35_no_of_atms() {
			return r35_no_of_atms;
		}

		public void setR35_no_of_atms(BigDecimal r35_no_of_atms) {
			this.r35_no_of_atms = r35_no_of_atms;
		}

		public BigDecimal getR35_no_of_mini_atms() {
			return r35_no_of_mini_atms;
		}

		public void setR35_no_of_mini_atms(BigDecimal r35_no_of_mini_atms) {
			this.r35_no_of_mini_atms = r35_no_of_mini_atms;
		}

		public BigDecimal getR35_encashment_points() {
			return r35_encashment_points;
		}

		public void setR35_encashment_points(BigDecimal r35_encashment_points) {
			this.r35_encashment_points = r35_encashment_points;
		}

		public String getR40_debit_district() {
			return r40_debit_district;
		}

		public void setR40_debit_district(String r40_debit_district) {
			this.r40_debit_district = r40_debit_district;
		}

		public BigDecimal getR40_opening_no_of_cards() {
			return r40_opening_no_of_cards;
		}

		public void setR40_opening_no_of_cards(BigDecimal r40_opening_no_of_cards) {
			this.r40_opening_no_of_cards = r40_opening_no_of_cards;
		}

		public BigDecimal getR40_no_of_cards_issued() {
			return r40_no_of_cards_issued;
		}

		public void setR40_no_of_cards_issued(BigDecimal r40_no_of_cards_issued) {
			this.r40_no_of_cards_issued = r40_no_of_cards_issued;
		}

		public BigDecimal getR40_no_cards_of_closed() {
			return r40_no_cards_of_closed;
		}

		public void setR40_no_cards_of_closed(BigDecimal r40_no_cards_of_closed) {
			this.r40_no_cards_of_closed = r40_no_cards_of_closed;
		}

		public BigDecimal getR40_closing_bal_of_active_cards() {
			return r40_closing_bal_of_active_cards;
		}

		public void setR40_closing_bal_of_active_cards(BigDecimal r40_closing_bal_of_active_cards) {
			this.r40_closing_bal_of_active_cards = r40_closing_bal_of_active_cards;
		}

		public String getR41_debit_district() {
			return r41_debit_district;
		}

		public void setR41_debit_district(String r41_debit_district) {
			this.r41_debit_district = r41_debit_district;
		}

		public BigDecimal getR41_opening_no_of_cards() {
			return r41_opening_no_of_cards;
		}

		public void setR41_opening_no_of_cards(BigDecimal r41_opening_no_of_cards) {
			this.r41_opening_no_of_cards = r41_opening_no_of_cards;
		}

		public BigDecimal getR41_no_of_cards_issued() {
			return r41_no_of_cards_issued;
		}

		public void setR41_no_of_cards_issued(BigDecimal r41_no_of_cards_issued) {
			this.r41_no_of_cards_issued = r41_no_of_cards_issued;
		}

		public BigDecimal getR41_no_cards_of_closed() {
			return r41_no_cards_of_closed;
		}

		public void setR41_no_cards_of_closed(BigDecimal r41_no_cards_of_closed) {
			this.r41_no_cards_of_closed = r41_no_cards_of_closed;
		}

		public BigDecimal getR41_closing_bal_of_active_cards() {
			return r41_closing_bal_of_active_cards;
		}

		public void setR41_closing_bal_of_active_cards(BigDecimal r41_closing_bal_of_active_cards) {
			this.r41_closing_bal_of_active_cards = r41_closing_bal_of_active_cards;
		}

		public String getR42_debit_district() {
			return r42_debit_district;
		}

		public void setR42_debit_district(String r42_debit_district) {
			this.r42_debit_district = r42_debit_district;
		}

		public BigDecimal getR42_opening_no_of_cards() {
			return r42_opening_no_of_cards;
		}

		public void setR42_opening_no_of_cards(BigDecimal r42_opening_no_of_cards) {
			this.r42_opening_no_of_cards = r42_opening_no_of_cards;
		}

		public BigDecimal getR42_no_of_cards_issued() {
			return r42_no_of_cards_issued;
		}

		public void setR42_no_of_cards_issued(BigDecimal r42_no_of_cards_issued) {
			this.r42_no_of_cards_issued = r42_no_of_cards_issued;
		}

		public BigDecimal getR42_no_cards_of_closed() {
			return r42_no_cards_of_closed;
		}

		public void setR42_no_cards_of_closed(BigDecimal r42_no_cards_of_closed) {
			this.r42_no_cards_of_closed = r42_no_cards_of_closed;
		}

		public BigDecimal getR42_closing_bal_of_active_cards() {
			return r42_closing_bal_of_active_cards;
		}

		public void setR42_closing_bal_of_active_cards(BigDecimal r42_closing_bal_of_active_cards) {
			this.r42_closing_bal_of_active_cards = r42_closing_bal_of_active_cards;
		}

		public String getR43_debit_district() {
			return r43_debit_district;
		}

		public void setR43_debit_district(String r43_debit_district) {
			this.r43_debit_district = r43_debit_district;
		}

		public BigDecimal getR43_opening_no_of_cards() {
			return r43_opening_no_of_cards;
		}

		public void setR43_opening_no_of_cards(BigDecimal r43_opening_no_of_cards) {
			this.r43_opening_no_of_cards = r43_opening_no_of_cards;
		}

		public BigDecimal getR43_no_of_cards_issued() {
			return r43_no_of_cards_issued;
		}

		public void setR43_no_of_cards_issued(BigDecimal r43_no_of_cards_issued) {
			this.r43_no_of_cards_issued = r43_no_of_cards_issued;
		}

		public BigDecimal getR43_no_cards_of_closed() {
			return r43_no_cards_of_closed;
		}

		public void setR43_no_cards_of_closed(BigDecimal r43_no_cards_of_closed) {
			this.r43_no_cards_of_closed = r43_no_cards_of_closed;
		}

		public BigDecimal getR43_closing_bal_of_active_cards() {
			return r43_closing_bal_of_active_cards;
		}

		public void setR43_closing_bal_of_active_cards(BigDecimal r43_closing_bal_of_active_cards) {
			this.r43_closing_bal_of_active_cards = r43_closing_bal_of_active_cards;
		}

		public String getR44_debit_district() {
			return r44_debit_district;
		}

		public void setR44_debit_district(String r44_debit_district) {
			this.r44_debit_district = r44_debit_district;
		}

		public BigDecimal getR44_opening_no_of_cards() {
			return r44_opening_no_of_cards;
		}

		public void setR44_opening_no_of_cards(BigDecimal r44_opening_no_of_cards) {
			this.r44_opening_no_of_cards = r44_opening_no_of_cards;
		}

		public BigDecimal getR44_no_of_cards_issued() {
			return r44_no_of_cards_issued;
		}

		public void setR44_no_of_cards_issued(BigDecimal r44_no_of_cards_issued) {
			this.r44_no_of_cards_issued = r44_no_of_cards_issued;
		}

		public BigDecimal getR44_no_cards_of_closed() {
			return r44_no_cards_of_closed;
		}

		public void setR44_no_cards_of_closed(BigDecimal r44_no_cards_of_closed) {
			this.r44_no_cards_of_closed = r44_no_cards_of_closed;
		}

		public BigDecimal getR44_closing_bal_of_active_cards() {
			return r44_closing_bal_of_active_cards;
		}

		public void setR44_closing_bal_of_active_cards(BigDecimal r44_closing_bal_of_active_cards) {
			this.r44_closing_bal_of_active_cards = r44_closing_bal_of_active_cards;
		}

		public String getR45_debit_district() {
			return r45_debit_district;
		}

		public void setR45_debit_district(String r45_debit_district) {
			this.r45_debit_district = r45_debit_district;
		}

		public BigDecimal getR45_opening_no_of_cards() {
			return r45_opening_no_of_cards;
		}

		public void setR45_opening_no_of_cards(BigDecimal r45_opening_no_of_cards) {
			this.r45_opening_no_of_cards = r45_opening_no_of_cards;
		}

		public BigDecimal getR45_no_of_cards_issued() {
			return r45_no_of_cards_issued;
		}

		public void setR45_no_of_cards_issued(BigDecimal r45_no_of_cards_issued) {
			this.r45_no_of_cards_issued = r45_no_of_cards_issued;
		}

		public BigDecimal getR45_no_cards_of_closed() {
			return r45_no_cards_of_closed;
		}

		public void setR45_no_cards_of_closed(BigDecimal r45_no_cards_of_closed) {
			this.r45_no_cards_of_closed = r45_no_cards_of_closed;
		}

		public BigDecimal getR45_closing_bal_of_active_cards() {
			return r45_closing_bal_of_active_cards;
		}

		public void setR45_closing_bal_of_active_cards(BigDecimal r45_closing_bal_of_active_cards) {
			this.r45_closing_bal_of_active_cards = r45_closing_bal_of_active_cards;
		}

		public String getR46_debit_district() {
			return r46_debit_district;
		}

		public void setR46_debit_district(String r46_debit_district) {
			this.r46_debit_district = r46_debit_district;
		}

		public BigDecimal getR46_opening_no_of_cards() {
			return r46_opening_no_of_cards;
		}

		public void setR46_opening_no_of_cards(BigDecimal r46_opening_no_of_cards) {
			this.r46_opening_no_of_cards = r46_opening_no_of_cards;
		}

		public BigDecimal getR46_no_of_cards_issued() {
			return r46_no_of_cards_issued;
		}

		public void setR46_no_of_cards_issued(BigDecimal r46_no_of_cards_issued) {
			this.r46_no_of_cards_issued = r46_no_of_cards_issued;
		}

		public BigDecimal getR46_no_cards_of_closed() {
			return r46_no_cards_of_closed;
		}

		public void setR46_no_cards_of_closed(BigDecimal r46_no_cards_of_closed) {
			this.r46_no_cards_of_closed = r46_no_cards_of_closed;
		}

		public BigDecimal getR46_closing_bal_of_active_cards() {
			return r46_closing_bal_of_active_cards;
		}

		public void setR46_closing_bal_of_active_cards(BigDecimal r46_closing_bal_of_active_cards) {
			this.r46_closing_bal_of_active_cards = r46_closing_bal_of_active_cards;
		}

		public String getR47_debit_district() {
			return r47_debit_district;
		}

		public void setR47_debit_district(String r47_debit_district) {
			this.r47_debit_district = r47_debit_district;
		}

		public BigDecimal getR47_opening_no_of_cards() {
			return r47_opening_no_of_cards;
		}

		public void setR47_opening_no_of_cards(BigDecimal r47_opening_no_of_cards) {
			this.r47_opening_no_of_cards = r47_opening_no_of_cards;
		}

		public BigDecimal getR47_no_of_cards_issued() {
			return r47_no_of_cards_issued;
		}

		public void setR47_no_of_cards_issued(BigDecimal r47_no_of_cards_issued) {
			this.r47_no_of_cards_issued = r47_no_of_cards_issued;
		}

		public BigDecimal getR47_no_cards_of_closed() {
			return r47_no_cards_of_closed;
		}

		public void setR47_no_cards_of_closed(BigDecimal r47_no_cards_of_closed) {
			this.r47_no_cards_of_closed = r47_no_cards_of_closed;
		}

		public BigDecimal getR47_closing_bal_of_active_cards() {
			return r47_closing_bal_of_active_cards;
		}

		public void setR47_closing_bal_of_active_cards(BigDecimal r47_closing_bal_of_active_cards) {
			this.r47_closing_bal_of_active_cards = r47_closing_bal_of_active_cards;
		}

		public String getR48_debit_district() {
			return r48_debit_district;
		}

		public void setR48_debit_district(String r48_debit_district) {
			this.r48_debit_district = r48_debit_district;
		}

		public BigDecimal getR48_opening_no_of_cards() {
			return r48_opening_no_of_cards;
		}

		public void setR48_opening_no_of_cards(BigDecimal r48_opening_no_of_cards) {
			this.r48_opening_no_of_cards = r48_opening_no_of_cards;
		}

		public BigDecimal getR48_no_of_cards_issued() {
			return r48_no_of_cards_issued;
		}

		public void setR48_no_of_cards_issued(BigDecimal r48_no_of_cards_issued) {
			this.r48_no_of_cards_issued = r48_no_of_cards_issued;
		}

		public BigDecimal getR48_no_cards_of_closed() {
			return r48_no_cards_of_closed;
		}

		public void setR48_no_cards_of_closed(BigDecimal r48_no_cards_of_closed) {
			this.r48_no_cards_of_closed = r48_no_cards_of_closed;
		}

		public BigDecimal getR48_closing_bal_of_active_cards() {
			return r48_closing_bal_of_active_cards;
		}

		public void setR48_closing_bal_of_active_cards(BigDecimal r48_closing_bal_of_active_cards) {
			this.r48_closing_bal_of_active_cards = r48_closing_bal_of_active_cards;
		}

		public String getR49_debit_district() {
			return r49_debit_district;
		}

		public void setR49_debit_district(String r49_debit_district) {
			this.r49_debit_district = r49_debit_district;
		}

		public BigDecimal getR49_opening_no_of_cards() {
			return r49_opening_no_of_cards;
		}

		public void setR49_opening_no_of_cards(BigDecimal r49_opening_no_of_cards) {
			this.r49_opening_no_of_cards = r49_opening_no_of_cards;
		}

		public BigDecimal getR49_no_of_cards_issued() {
			return r49_no_of_cards_issued;
		}

		public void setR49_no_of_cards_issued(BigDecimal r49_no_of_cards_issued) {
			this.r49_no_of_cards_issued = r49_no_of_cards_issued;
		}

		public BigDecimal getR49_no_cards_of_closed() {
			return r49_no_cards_of_closed;
		}

		public void setR49_no_cards_of_closed(BigDecimal r49_no_cards_of_closed) {
			this.r49_no_cards_of_closed = r49_no_cards_of_closed;
		}

		public BigDecimal getR49_closing_bal_of_active_cards() {
			return r49_closing_bal_of_active_cards;
		}

		public void setR49_closing_bal_of_active_cards(BigDecimal r49_closing_bal_of_active_cards) {
			this.r49_closing_bal_of_active_cards = r49_closing_bal_of_active_cards;
		}

		public String getR50_debit_district() {
			return r50_debit_district;
		}

		public void setR50_debit_district(String r50_debit_district) {
			this.r50_debit_district = r50_debit_district;
		}

		public BigDecimal getR50_opening_no_of_cards() {
			return r50_opening_no_of_cards;
		}

		public void setR50_opening_no_of_cards(BigDecimal r50_opening_no_of_cards) {
			this.r50_opening_no_of_cards = r50_opening_no_of_cards;
		}

		public BigDecimal getR50_no_of_cards_issued() {
			return r50_no_of_cards_issued;
		}

		public void setR50_no_of_cards_issued(BigDecimal r50_no_of_cards_issued) {
			this.r50_no_of_cards_issued = r50_no_of_cards_issued;
		}

		public BigDecimal getR50_no_cards_of_closed() {
			return r50_no_cards_of_closed;
		}

		public void setR50_no_cards_of_closed(BigDecimal r50_no_cards_of_closed) {
			this.r50_no_cards_of_closed = r50_no_cards_of_closed;
		}

		public BigDecimal getR50_closing_bal_of_active_cards() {
			return r50_closing_bal_of_active_cards;
		}

		public void setR50_closing_bal_of_active_cards(BigDecimal r50_closing_bal_of_active_cards) {
			this.r50_closing_bal_of_active_cards = r50_closing_bal_of_active_cards;
		}

		public String getR55_credit_district() {
			return r55_credit_district;
		}

		public void setR55_credit_district(String r55_credit_district) {
			this.r55_credit_district = r55_credit_district;
		}

		public BigDecimal getR55_opening_no_of_cards() {
			return r55_opening_no_of_cards;
		}

		public void setR55_opening_no_of_cards(BigDecimal r55_opening_no_of_cards) {
			this.r55_opening_no_of_cards = r55_opening_no_of_cards;
		}

		public BigDecimal getR55_no_of_cards_issued() {
			return r55_no_of_cards_issued;
		}

		public void setR55_no_of_cards_issued(BigDecimal r55_no_of_cards_issued) {
			this.r55_no_of_cards_issued = r55_no_of_cards_issued;
		}

		public BigDecimal getR55_no_cards_of_closed() {
			return r55_no_cards_of_closed;
		}

		public void setR55_no_cards_of_closed(BigDecimal r55_no_cards_of_closed) {
			this.r55_no_cards_of_closed = r55_no_cards_of_closed;
		}

		public BigDecimal getR55_closing_bal_of_active_cards() {
			return r55_closing_bal_of_active_cards;
		}

		public void setR55_closing_bal_of_active_cards(BigDecimal r55_closing_bal_of_active_cards) {
			this.r55_closing_bal_of_active_cards = r55_closing_bal_of_active_cards;
		}

		public String getR56_credit_district() {
			return r56_credit_district;
		}

		public void setR56_credit_district(String r56_credit_district) {
			this.r56_credit_district = r56_credit_district;
		}

		public BigDecimal getR56_opening_no_of_cards() {
			return r56_opening_no_of_cards;
		}

		public void setR56_opening_no_of_cards(BigDecimal r56_opening_no_of_cards) {
			this.r56_opening_no_of_cards = r56_opening_no_of_cards;
		}

		public BigDecimal getR56_no_of_cards_issued() {
			return r56_no_of_cards_issued;
		}

		public void setR56_no_of_cards_issued(BigDecimal r56_no_of_cards_issued) {
			this.r56_no_of_cards_issued = r56_no_of_cards_issued;
		}

		public BigDecimal getR56_no_cards_of_closed() {
			return r56_no_cards_of_closed;
		}

		public void setR56_no_cards_of_closed(BigDecimal r56_no_cards_of_closed) {
			this.r56_no_cards_of_closed = r56_no_cards_of_closed;
		}

		public BigDecimal getR56_closing_bal_of_active_cards() {
			return r56_closing_bal_of_active_cards;
		}

		public void setR56_closing_bal_of_active_cards(BigDecimal r56_closing_bal_of_active_cards) {
			this.r56_closing_bal_of_active_cards = r56_closing_bal_of_active_cards;
		}

		public String getR57_credit_district() {
			return r57_credit_district;
		}

		public void setR57_credit_district(String r57_credit_district) {
			this.r57_credit_district = r57_credit_district;
		}

		public BigDecimal getR57_opening_no_of_cards() {
			return r57_opening_no_of_cards;
		}

		public void setR57_opening_no_of_cards(BigDecimal r57_opening_no_of_cards) {
			this.r57_opening_no_of_cards = r57_opening_no_of_cards;
		}

		public BigDecimal getR57_no_of_cards_issued() {
			return r57_no_of_cards_issued;
		}

		public void setR57_no_of_cards_issued(BigDecimal r57_no_of_cards_issued) {
			this.r57_no_of_cards_issued = r57_no_of_cards_issued;
		}

		public BigDecimal getR57_no_cards_of_closed() {
			return r57_no_cards_of_closed;
		}

		public void setR57_no_cards_of_closed(BigDecimal r57_no_cards_of_closed) {
			this.r57_no_cards_of_closed = r57_no_cards_of_closed;
		}

		public BigDecimal getR57_closing_bal_of_active_cards() {
			return r57_closing_bal_of_active_cards;
		}

		public void setR57_closing_bal_of_active_cards(BigDecimal r57_closing_bal_of_active_cards) {
			this.r57_closing_bal_of_active_cards = r57_closing_bal_of_active_cards;
		}

		public String getR58_credit_district() {
			return r58_credit_district;
		}

		public void setR58_credit_district(String r58_credit_district) {
			this.r58_credit_district = r58_credit_district;
		}

		public BigDecimal getR58_opening_no_of_cards() {
			return r58_opening_no_of_cards;
		}

		public void setR58_opening_no_of_cards(BigDecimal r58_opening_no_of_cards) {
			this.r58_opening_no_of_cards = r58_opening_no_of_cards;
		}

		public BigDecimal getR58_no_of_cards_issued() {
			return r58_no_of_cards_issued;
		}

		public void setR58_no_of_cards_issued(BigDecimal r58_no_of_cards_issued) {
			this.r58_no_of_cards_issued = r58_no_of_cards_issued;
		}

		public BigDecimal getR58_no_cards_of_closed() {
			return r58_no_cards_of_closed;
		}

		public void setR58_no_cards_of_closed(BigDecimal r58_no_cards_of_closed) {
			this.r58_no_cards_of_closed = r58_no_cards_of_closed;
		}

		public BigDecimal getR58_closing_bal_of_active_cards() {
			return r58_closing_bal_of_active_cards;
		}

		public void setR58_closing_bal_of_active_cards(BigDecimal r58_closing_bal_of_active_cards) {
			this.r58_closing_bal_of_active_cards = r58_closing_bal_of_active_cards;
		}

		public String getR59_credit_district() {
			return r59_credit_district;
		}

		public void setR59_credit_district(String r59_credit_district) {
			this.r59_credit_district = r59_credit_district;
		}

		public BigDecimal getR59_opening_no_of_cards() {
			return r59_opening_no_of_cards;
		}

		public void setR59_opening_no_of_cards(BigDecimal r59_opening_no_of_cards) {
			this.r59_opening_no_of_cards = r59_opening_no_of_cards;
		}

		public BigDecimal getR59_no_of_cards_issued() {
			return r59_no_of_cards_issued;
		}

		public void setR59_no_of_cards_issued(BigDecimal r59_no_of_cards_issued) {
			this.r59_no_of_cards_issued = r59_no_of_cards_issued;
		}

		public BigDecimal getR59_no_cards_of_closed() {
			return r59_no_cards_of_closed;
		}

		public void setR59_no_cards_of_closed(BigDecimal r59_no_cards_of_closed) {
			this.r59_no_cards_of_closed = r59_no_cards_of_closed;
		}

		public BigDecimal getR59_closing_bal_of_active_cards() {
			return r59_closing_bal_of_active_cards;
		}

		public void setR59_closing_bal_of_active_cards(BigDecimal r59_closing_bal_of_active_cards) {
			this.r59_closing_bal_of_active_cards = r59_closing_bal_of_active_cards;
		}

		public String getR60_credit_district() {
			return r60_credit_district;
		}

		public void setR60_credit_district(String r60_credit_district) {
			this.r60_credit_district = r60_credit_district;
		}

		public BigDecimal getR60_opening_no_of_cards() {
			return r60_opening_no_of_cards;
		}

		public void setR60_opening_no_of_cards(BigDecimal r60_opening_no_of_cards) {
			this.r60_opening_no_of_cards = r60_opening_no_of_cards;
		}

		public BigDecimal getR60_no_of_cards_issued() {
			return r60_no_of_cards_issued;
		}

		public void setR60_no_of_cards_issued(BigDecimal r60_no_of_cards_issued) {
			this.r60_no_of_cards_issued = r60_no_of_cards_issued;
		}

		public BigDecimal getR60_no_cards_of_closed() {
			return r60_no_cards_of_closed;
		}

		public void setR60_no_cards_of_closed(BigDecimal r60_no_cards_of_closed) {
			this.r60_no_cards_of_closed = r60_no_cards_of_closed;
		}

		public BigDecimal getR60_closing_bal_of_active_cards() {
			return r60_closing_bal_of_active_cards;
		}

		public void setR60_closing_bal_of_active_cards(BigDecimal r60_closing_bal_of_active_cards) {
			this.r60_closing_bal_of_active_cards = r60_closing_bal_of_active_cards;
		}

		public String getR61_credit_district() {
			return r61_credit_district;
		}

		public void setR61_credit_district(String r61_credit_district) {
			this.r61_credit_district = r61_credit_district;
		}

		public BigDecimal getR61_opening_no_of_cards() {
			return r61_opening_no_of_cards;
		}

		public void setR61_opening_no_of_cards(BigDecimal r61_opening_no_of_cards) {
			this.r61_opening_no_of_cards = r61_opening_no_of_cards;
		}

		public BigDecimal getR61_no_of_cards_issued() {
			return r61_no_of_cards_issued;
		}

		public void setR61_no_of_cards_issued(BigDecimal r61_no_of_cards_issued) {
			this.r61_no_of_cards_issued = r61_no_of_cards_issued;
		}

		public BigDecimal getR61_no_cards_of_closed() {
			return r61_no_cards_of_closed;
		}

		public void setR61_no_cards_of_closed(BigDecimal r61_no_cards_of_closed) {
			this.r61_no_cards_of_closed = r61_no_cards_of_closed;
		}

		public BigDecimal getR61_closing_bal_of_active_cards() {
			return r61_closing_bal_of_active_cards;
		}

		public void setR61_closing_bal_of_active_cards(BigDecimal r61_closing_bal_of_active_cards) {
			this.r61_closing_bal_of_active_cards = r61_closing_bal_of_active_cards;
		}

		public String getR62_credit_district() {
			return r62_credit_district;
		}

		public void setR62_credit_district(String r62_credit_district) {
			this.r62_credit_district = r62_credit_district;
		}

		public BigDecimal getR62_opening_no_of_cards() {
			return r62_opening_no_of_cards;
		}

		public void setR62_opening_no_of_cards(BigDecimal r62_opening_no_of_cards) {
			this.r62_opening_no_of_cards = r62_opening_no_of_cards;
		}

		public BigDecimal getR62_no_of_cards_issued() {
			return r62_no_of_cards_issued;
		}

		public void setR62_no_of_cards_issued(BigDecimal r62_no_of_cards_issued) {
			this.r62_no_of_cards_issued = r62_no_of_cards_issued;
		}

		public BigDecimal getR62_no_cards_of_closed() {
			return r62_no_cards_of_closed;
		}

		public void setR62_no_cards_of_closed(BigDecimal r62_no_cards_of_closed) {
			this.r62_no_cards_of_closed = r62_no_cards_of_closed;
		}

		public BigDecimal getR62_closing_bal_of_active_cards() {
			return r62_closing_bal_of_active_cards;
		}

		public void setR62_closing_bal_of_active_cards(BigDecimal r62_closing_bal_of_active_cards) {
			this.r62_closing_bal_of_active_cards = r62_closing_bal_of_active_cards;
		}

		public String getR63_credit_district() {
			return r63_credit_district;
		}

		public void setR63_credit_district(String r63_credit_district) {
			this.r63_credit_district = r63_credit_district;
		}

		public BigDecimal getR63_opening_no_of_cards() {
			return r63_opening_no_of_cards;
		}

		public void setR63_opening_no_of_cards(BigDecimal r63_opening_no_of_cards) {
			this.r63_opening_no_of_cards = r63_opening_no_of_cards;
		}

		public BigDecimal getR63_no_of_cards_issued() {
			return r63_no_of_cards_issued;
		}

		public void setR63_no_of_cards_issued(BigDecimal r63_no_of_cards_issued) {
			this.r63_no_of_cards_issued = r63_no_of_cards_issued;
		}

		public BigDecimal getR63_no_cards_of_closed() {
			return r63_no_cards_of_closed;
		}

		public void setR63_no_cards_of_closed(BigDecimal r63_no_cards_of_closed) {
			this.r63_no_cards_of_closed = r63_no_cards_of_closed;
		}

		public BigDecimal getR63_closing_bal_of_active_cards() {
			return r63_closing_bal_of_active_cards;
		}

		public void setR63_closing_bal_of_active_cards(BigDecimal r63_closing_bal_of_active_cards) {
			this.r63_closing_bal_of_active_cards = r63_closing_bal_of_active_cards;
		}

		public String getR64_credit_district() {
			return r64_credit_district;
		}

		public void setR64_credit_district(String r64_credit_district) {
			this.r64_credit_district = r64_credit_district;
		}

		public BigDecimal getR64_opening_no_of_cards() {
			return r64_opening_no_of_cards;
		}

		public void setR64_opening_no_of_cards(BigDecimal r64_opening_no_of_cards) {
			this.r64_opening_no_of_cards = r64_opening_no_of_cards;
		}

		public BigDecimal getR64_no_of_cards_issued() {
			return r64_no_of_cards_issued;
		}

		public void setR64_no_of_cards_issued(BigDecimal r64_no_of_cards_issued) {
			this.r64_no_of_cards_issued = r64_no_of_cards_issued;
		}

		public BigDecimal getR64_no_cards_of_closed() {
			return r64_no_cards_of_closed;
		}

		public void setR64_no_cards_of_closed(BigDecimal r64_no_cards_of_closed) {
			this.r64_no_cards_of_closed = r64_no_cards_of_closed;
		}

		public BigDecimal getR64_closing_bal_of_active_cards() {
			return r64_closing_bal_of_active_cards;
		}

		public void setR64_closing_bal_of_active_cards(BigDecimal r64_closing_bal_of_active_cards) {
			this.r64_closing_bal_of_active_cards = r64_closing_bal_of_active_cards;
		}

		public String getR65_credit_district() {
			return r65_credit_district;
		}

		public void setR65_credit_district(String r65_credit_district) {
			this.r65_credit_district = r65_credit_district;
		}

		public BigDecimal getR65_opening_no_of_cards() {
			return r65_opening_no_of_cards;
		}

		public void setR65_opening_no_of_cards(BigDecimal r65_opening_no_of_cards) {
			this.r65_opening_no_of_cards = r65_opening_no_of_cards;
		}

		public BigDecimal getR65_no_of_cards_issued() {
			return r65_no_of_cards_issued;
		}

		public void setR65_no_of_cards_issued(BigDecimal r65_no_of_cards_issued) {
			this.r65_no_of_cards_issued = r65_no_of_cards_issued;
		}

		public BigDecimal getR65_no_cards_of_closed() {
			return r65_no_cards_of_closed;
		}

		public void setR65_no_cards_of_closed(BigDecimal r65_no_cards_of_closed) {
			this.r65_no_cards_of_closed = r65_no_cards_of_closed;
		}

		public BigDecimal getR65_closing_bal_of_active_cards() {
			return r65_closing_bal_of_active_cards;
		}

		public void setR65_closing_bal_of_active_cards(BigDecimal r65_closing_bal_of_active_cards) {
			this.r65_closing_bal_of_active_cards = r65_closing_bal_of_active_cards;
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

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getQ_BRANCHNETView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("Q_BRANCHNET View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);
		System.out.println("DtlType = " + dtltype);

		try {

			Date dt = dateformat.parse(todate);
			if ("detail".equalsIgnoreCase(dtltype)) {

				// ARCHIVAL DETAIL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_BRANCHNET_Archival_Detail_Entity> T1Master = getDetaildatabydateListarchival(dt, version);

					System.out.println("Archival Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// RESUB DETAIL
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_BRANCHNET_Resub_Detail_Entity> T1Master = getResubDetaildatabydateList(dt, version);

					System.out.println("Resub Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// NORMAL DETAIL
				else {

					List<Q_BRANCHNET_Detail_Entity> T1Master = getDetaildatabydateList(dt);

					System.out.println("Normal Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}
			} else {

				// ARCHIVAL SUMMARY
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_BRANCHNET_Archival_Summary_Entity> T1Master = getDataByDateListArchival(dt, version);

					System.out.println("Archival Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// RESUB SUMMARY
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_BRANCHNET_Resub_Summary_Entity> T1Master = getResubSummarydatabydateListarchival(dt,
							version);

					System.out.println("Resub Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// NORMAL SUMMARY
				else {

					List<Q_BRANCHNET_Summary_Entity> T1Master = getSummaryDataByDate(dt);

					System.out.println("Normal Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				mv.addObject("displaymode", "summary");
			}

			mv.addObject("report_date", dateformat.format(dt));

		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_BRANCHNET");

		System.out.println("View Loaded : " + mv.getViewName());

		return mv;
	}

// Archival View
	public List<Object[]> getQ_BRANCHNETArchival() {

		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Q_BRANCHNET_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_BRANCHNET_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");

				Q_BRANCHNET_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest archival version: " + first.getReport_version());

			} else {

				System.out.println("No archival data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_BRANCHNET Archival data: " + e.getMessage());

			e.printStackTrace();
		}

		return archivalList;
	}

	@Transactional
	public void QBranchnetUpdate1(Q_BRANCHNET_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_BRANCHNET Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_BRANCHNET_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_BRANCHNET_Summary_Entity oldcopy = new Q_BRANCHNET_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "bran_sub_bran_district", "no1_of_branches", "no1_of_sub_branches", "no1_of_agencies" };

		try {

			for (int i = 10; i <= 20; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_BRANCHNET_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_BRANCHNET_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_BRANCHNET_DETAILTABLE " + "SET " + columnName + " = ? "
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
						"Q_BRANCHNET Summary Screen", "BRRS_Q_BRANCHNET_SUMMARY");
			}

			System.out.println("Q_BRANCHNET Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_BRANCHNET fields", e);
		}
	}

	@Transactional
	public void QBranchnetUpdate2(Q_BRANCHNET_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_BRANCHNET Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_BRANCHNET_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_BRANCHNET_Summary_Entity oldcopy = new Q_BRANCHNET_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };

		try {

			for (int i = 25; i <= 35; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_BRANCHNET_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_BRANCHNET_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_BRANCHNET_DETAILTABLE " + "SET " + columnName + " = ? "
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
						"Q_BRANCHNET Summary Screen", "BRRS_Q_BRANCHNET_SUMMARY");
			}

			System.out.println("Q_BRANCHNET Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_BRANCHNET fields", e);
		}
	}

	@Transactional
	public void QBranchnetUpdate3(Q_BRANCHNET_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_BRANCHNET Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_BRANCHNET_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_BRANCHNET_Summary_Entity oldcopy = new Q_BRANCHNET_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
				"closing_bal_of_active_cards" };
		try {

			for (int i = 40; i <= 50; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_BRANCHNET_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_BRANCHNET_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_BRANCHNET_DETAILTABLE " + "SET " + columnName + " = ? "
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
						"Q_BRANCHNET Summary Screen", "BRRS_Q_BRANCHNET_SUMMARY");
			}

			System.out.println("Q_BRANCHNET Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_BRANCHNET fields", e);
		}
	}

	@Transactional
	public void QBranchnetUpdate4(Q_BRANCHNET_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_BRANCHNET Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_BRANCHNET_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_BRANCHNET_Summary_Entity oldcopy = new Q_BRANCHNET_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "credit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
				"closing_bal_of_active_cards" };
		try {

			for (int i = 55; i <= 65; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_BRANCHNET_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_BRANCHNET_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_BRANCHNET_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_BRANCHNET_DETAILTABLE " + "SET " + columnName + " = ? "
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
						"Q_BRANCHNET Summary Screen", "BRRS_Q_BRANCHNET_SUMMARY");
			}

			System.out.println("Q_BRANCHNET Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_BRANCHNET fields", e);
		}
	}

	public List<Object[]> getQ_BRANCHNETResub() {

		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Q_BRANCHNET_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_BRANCHNET_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " resub records");

				Q_BRANCHNET_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest resub version : " + first.getReport_version());

			} else {

				System.out.println("No resub data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_BRANCHNET Resub data : " + e.getMessage());

			e.printStackTrace();
		}

		return resubList;
	}

	@Transactional
	public void updateResubReport(Q_BRANCHNET_Resub_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_BRANCHNET Resub Update");

		Date reportDate = updatedEntity.getReport_date();

		BigDecimal maxVersion = findMaxVersion(reportDate);

		if (maxVersion == null) {
			throw new RuntimeException("No record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = maxVersion.add(BigDecimal.ONE);

		Date now = new Date();

		try {

			Q_BRANCHNET_Resub_Summary_Entity resubSummary = new Q_BRANCHNET_Resub_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, resubSummary);

			resubSummary.setReport_date(reportDate);
			resubSummary.setReport_version(newVersion);
			resubSummary.setReportResubDate(now);

			Q_BRANCHNET_Resub_Detail_Entity resubDetail = new Q_BRANCHNET_Resub_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, resubDetail);

			resubDetail.setReport_date(reportDate);
			resubDetail.setReport_version(newVersion);
			resubDetail.setReportResubDate(now);

			Q_BRANCHNET_Archival_Summary_Entity archivalSummary = new Q_BRANCHNET_Archival_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalSummary);

			archivalSummary.setReport_date(reportDate);
			archivalSummary.setReport_version(newVersion);
			archivalSummary.setReportResubDate(now);

			Q_BRANCHNET_Archival_Detail_Entity archivalDetail = new Q_BRANCHNET_Archival_Detail_Entity();

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

				auditService.createBusinessAudit(userid, "RESUBMIT", "Q_BRANCHNET Resub Summary", null,
						"BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE");
			}

			System.out.println("Q_BRANCHNET Resub Version Created Successfully : " + newVersion);

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("Error while creating Q_BRANCHNET Resub Version", e);
		}
	}

	private void insertResubSummary(Q_BRANCHNET_Resub_Summary_Entity entity) {

		try {
			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 20) ---
			// Removed the trailing comma from the end of the block string
			for (int i = 10; i <= 20; i++) {
				columns.append("r").append(i).append("_bran_sub_bran_district,").append("r").append(i)
						.append("_no1_of_branches,").append("r").append(i).append("_no1_of_sub_branches,").append("r")
						.append(i).append("_no1_of_agencies,"); // Kept as comma-terminated to chain into next loops
																// safely

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_bran_sub_bran_district"));
				params.add(getValue(entity, "getR" + i + "_no1_of_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_sub_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_agencies"));
			}

			// --- 1st New Loop (i = 25 to 35) ---
			String[] fields1 = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };
			for (int i = 25; i <= 35; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 40 to 50) ---
			String[] fields2 = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 40; i <= 50; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 3rd New Loop (i = 55 to 65) ---
			String[] fields3 = { "credit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 55; i <= 65; i++) {
				for (String field : fields3) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas and close brackets safely
			if (columns.charAt(columns.length() - 1) == ',') {
				columns.deleteCharAt(columns.length() - 1);
			}
			if (values.charAt(values.length() - 1) == ',') {
				values.deleteCharAt(values.length() - 1);
			}

			columns.append(")");
			values.append(")");

			String finalSql = columns.toString() + values.toString();
			jdbcTemplate.update(finalSql, params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_BRANCHNET RESUB SUMMARY", e);
		}
	}

	private void insertResubDetail(Q_BRANCHNET_Resub_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_BRANCHNET_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 20) ---
			// Removed the trailing comma from the end of the block string
			for (int i = 10; i <= 20; i++) {
				columns.append("r").append(i).append("_bran_sub_bran_district,").append("r").append(i)
						.append("_no1_of_branches,").append("r").append(i).append("_no1_of_sub_branches,").append("r")
						.append(i).append("_no1_of_agencies,"); // Kept as comma-terminated to chain into next loops
																// safely

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_bran_sub_bran_district"));
				params.add(getValue(entity, "getR" + i + "_no1_of_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_sub_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_agencies"));
			}

			// --- 1st New Loop (i = 25 to 35) ---
			String[] fields1 = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };
			for (int i = 25; i <= 35; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 40 to 50) ---
			String[] fields2 = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 40; i <= 50; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 3rd New Loop (i = 55 to 65) ---
			String[] fields3 = { "credit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 55; i <= 65; i++) {
				for (String field : fields3) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas safely
			if (columns.charAt(columns.length() - 1) == ',') {
				columns.deleteCharAt(columns.length() - 1);
			}
			if (values.charAt(values.length() - 1) == ',') {
				values.deleteCharAt(values.length() - 1);
			}

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_BRANCHNET RESUB DETAIL", e);
		}
	}

	private void insertArchivalSummary(Q_BRANCHNET_Archival_Summary_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 20) ---
			// Removed the trailing comma from the end of the block string
			for (int i = 10; i <= 20; i++) {
				columns.append("r").append(i).append("_bran_sub_bran_district,").append("r").append(i)
						.append("_no1_of_branches,").append("r").append(i).append("_no1_of_sub_branches,").append("r")
						.append(i).append("_no1_of_agencies,"); // Kept as comma-terminated to chain into next loops
																// safely

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_bran_sub_bran_district"));
				params.add(getValue(entity, "getR" + i + "_no1_of_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_sub_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_agencies"));
			}

			// --- 1st New Loop (i = 25 to 35) ---
			String[] fields1 = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };
			for (int i = 25; i <= 35; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 40 to 50) ---
			String[] fields2 = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 40; i <= 50; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 3rd New Loop (i = 55 to 65) ---
			String[] fields3 = { "credit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 55; i <= 65; i++) {
				for (String field : fields3) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas safely
			if (columns.charAt(columns.length() - 1) == ',') {
				columns.deleteCharAt(columns.length() - 1);
			}
			if (values.charAt(values.length() - 1) == ',') {
				values.deleteCharAt(values.length() - 1);
			}

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_BRANCHNET ARCHIVAL SUMMARY", e);
		}
	}

	private void insertArchivalDetail(Q_BRANCHNET_Archival_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_BRANCHNET_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 10 to 20) ---
			// Removed the trailing comma from the end of the block string
			for (int i = 10; i <= 20; i++) {
				columns.append("r").append(i).append("_bran_sub_bran_district,").append("r").append(i)
						.append("_no1_of_branches,").append("r").append(i).append("_no1_of_sub_branches,").append("r")
						.append(i).append("_no1_of_agencies,"); // Kept as comma-terminated to chain into next loops
																// safely

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_bran_sub_bran_district"));
				params.add(getValue(entity, "getR" + i + "_no1_of_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_sub_branches"));
				params.add(getValue(entity, "getR" + i + "_no1_of_agencies"));
			}

			// --- 1st New Loop (i = 25 to 35) ---
			String[] fields1 = { "atm_mini_atm_district", "no_of_atms", "no_of_mini_atms", "encashment_points" };
			for (int i = 25; i <= 35; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 40 to 50) ---
			String[] fields2 = { "debit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 40; i <= 50; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 3rd New Loop (i = 55 to 65) ---
			String[] fields3 = { "credit_district", "opening_no_of_cards", "no_of_cards_issued", "no_cards_of_closed",
					"closing_bal_of_active_cards" };
			for (int i = 55; i <= 65; i++) {
				for (String field : fields3) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas safely
			if (columns.charAt(columns.length() - 1) == ',') {
				columns.deleteCharAt(columns.length() - 1);
			}
			if (values.charAt(values.length() - 1) == ',') {
				values.deleteCharAt(values.length() - 1);
			}

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_BRANCHNET ARCHIVAL DETAIL", e);
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
	public byte[] BRRS_Q_BRANCHNETExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelQ_BRANCHNETARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_BRANCHNETResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_Q_BRANCHNETEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				List<Q_BRANCHNET_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_BRANCHNET report. Returning empty result.");
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
							Q_BRANCHNET_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(9);
//NORMAL
							// R10 Col C

							Cell R10cell1 = row.createCell(2);
							if (record.getR10_no1_of_branches() != null) {
								R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
								R10cell1.setCellStyle(numberStyle);
							} else {
								R10cell1.setCellValue("");
								R10cell1.setCellStyle(textStyle);
							}

							// R10 Col D
							Cell R10cell2 = row.createCell(3);
							if (record.getR10_no1_of_sub_branches() != null) {
								R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
								R10cell2.setCellStyle(numberStyle);
							} else {
								R10cell2.setCellValue("");
								R10cell2.setCellStyle(textStyle);
							}

							// R10 Col E
							Cell R10cell3 = row.createCell(4);
							if (record.getR10_no1_of_agencies() != null) {
								R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
								R10cell3.setCellStyle(numberStyle);
							} else {
								R10cell3.setCellValue("");
								R10cell3.setCellStyle(textStyle);
							}
							// R11 Col C
							row = sheet.getRow(10);
							Cell R11cell1 = row.createCell(2);
							if (record.getR11_no1_of_branches() != null) {
								R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
								R11cell1.setCellStyle(numberStyle);
							} else {
								R11cell1.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

							// R11 Col D
							Cell R11cell2 = row.createCell(3);
							if (record.getR11_no1_of_sub_branches() != null) {
								R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
								R11cell2.setCellStyle(numberStyle);
							} else {
								R11cell2.setCellValue("");
								R11cell2.setCellStyle(textStyle);
							}

							// R11 Col E
							Cell R11cell3 = row.createCell(4);
							if (record.getR11_no1_of_agencies() != null) {
								R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
								R11cell3.setCellStyle(numberStyle);
							} else {
								R11cell3.setCellValue("");
								R11cell3.setCellStyle(textStyle);
							}
							// R12 Col C
							row = sheet.getRow(11);
							Cell R12cell1 = row.createCell(2);
							if (record.getR12_no1_of_branches() != null) {
								R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
								R12cell1.setCellStyle(numberStyle);
							} else {
								R12cell1.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

							// R12 Col D
							Cell R12cell2 = row.createCell(3);
							if (record.getR12_no1_of_sub_branches() != null) {
								R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
								R12cell2.setCellStyle(numberStyle);
							} else {
								R12cell2.setCellValue("");
								R12cell2.setCellStyle(textStyle);
							}

							// R12 Col E
							Cell R12cell3 = row.createCell(4);
							if (record.getR12_no1_of_agencies() != null) {
								R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
								R12cell3.setCellStyle(numberStyle);
							} else {
								R12cell3.setCellValue("");
								R12cell3.setCellStyle(textStyle);
							}
							// R13 Col C
							row = sheet.getRow(12);
							Cell R13cell1 = row.createCell(2);
							if (record.getR13_no1_of_branches() != null) {
								R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
								R13cell1.setCellStyle(numberStyle);
							} else {
								R13cell1.setCellValue("");
								R13cell1.setCellStyle(textStyle);
							}

							// R13 Col D
							Cell R13cell2 = row.createCell(3);
							if (record.getR13_no1_of_sub_branches() != null) {
								R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);
							}

							// R13 Col E
							Cell R13cell3 = row.createCell(4);
							if (record.getR13_no1_of_agencies() != null) {
								R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
								R13cell3.setCellStyle(numberStyle);
							} else {
								R13cell3.setCellValue("");
								R13cell3.setCellStyle(textStyle);
							}
							// R14 Col C
							row = sheet.getRow(13);
							Cell R14cell1 = row.createCell(2);
							if (record.getR14_no1_of_branches() != null) {
								R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
								R14cell1.setCellStyle(numberStyle);
							} else {
								R14cell1.setCellValue("");
								R14cell1.setCellStyle(textStyle);
							}

							// R14 Col D
							Cell R14cell2 = row.createCell(3);
							if (record.getR14_no1_of_sub_branches() != null) {
								R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);
							}

							// R14 Col E
							Cell R14cell3 = row.createCell(4);
							if (record.getR14_no1_of_agencies() != null) {
								R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
								R14cell3.setCellStyle(numberStyle);
							} else {
								R14cell3.setCellValue("");
								R14cell3.setCellStyle(textStyle);
							}
							// R15 Col C
							row = sheet.getRow(14);
							Cell R15cell1 = row.createCell(2);
							if (record.getR15_no1_of_branches() != null) {
								R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
								R15cell1.setCellStyle(numberStyle);
							} else {
								R15cell1.setCellValue("");
								R15cell1.setCellStyle(textStyle);
							}

							// R15 Col D
							Cell R15cell2 = row.createCell(3);
							if (record.getR15_no1_of_sub_branches() != null) {
								R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
								R15cell2.setCellStyle(numberStyle);
							} else {
								R15cell2.setCellValue("");
								R15cell2.setCellStyle(textStyle);
							}

							// R15 Col E
							Cell R15cell3 = row.createCell(4);
							if (record.getR15_no1_of_agencies() != null) {
								R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
								R15cell3.setCellStyle(numberStyle);
							} else {
								R15cell3.setCellValue("");
								R15cell3.setCellStyle(textStyle);
							}
							// R16 Col C
							row = sheet.getRow(15);
							Cell R16cell1 = row.createCell(2);
							if (record.getR16_no1_of_branches() != null) {
								R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
								R16cell1.setCellStyle(numberStyle);
							} else {
								R16cell1.setCellValue("");
								R16cell1.setCellStyle(textStyle);
							}

							// R16 Col D
							Cell R16cell2 = row.createCell(3);
							if (record.getR16_no1_of_sub_branches() != null) {
								R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
								R16cell2.setCellStyle(numberStyle);
							} else {
								R16cell2.setCellValue("");
								R16cell2.setCellStyle(textStyle);
							}

							// R16 Col E
							Cell R16cell3 = row.createCell(4);
							if (record.getR16_no1_of_agencies() != null) {
								R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
								R16cell3.setCellStyle(numberStyle);
							} else {
								R16cell3.setCellValue("");
								R16cell3.setCellStyle(textStyle);
							}
							// R17 Col C
							row = sheet.getRow(16);
							Cell R17cell1 = row.createCell(2);
							if (record.getR17_no1_of_branches() != null) {
								R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
								R17cell1.setCellStyle(numberStyle);
							} else {
								R17cell1.setCellValue("");
								R17cell1.setCellStyle(textStyle);
							}

							// R17 Col D
							Cell R17cell2 = row.createCell(3);
							if (record.getR17_no1_of_sub_branches() != null) {
								R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
								R17cell2.setCellStyle(numberStyle);
							} else {
								R17cell2.setCellValue("");
								R17cell2.setCellStyle(textStyle);
							}

							// R17 Col E
							Cell R17cell3 = row.createCell(4);
							if (record.getR17_no1_of_agencies() != null) {
								R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
								R17cell3.setCellStyle(numberStyle);
							} else {
								R17cell3.setCellValue("");
								R17cell3.setCellStyle(textStyle);
							}
							// R18 Col C
							row = sheet.getRow(17);
							Cell R18cell1 = row.createCell(2);
							if (record.getR18_no1_of_branches() != null) {
								R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
								R18cell1.setCellStyle(numberStyle);
							} else {
								R18cell1.setCellValue("");
								R18cell1.setCellStyle(textStyle);
							}

							// R18 Col D
							Cell R18cell2 = row.createCell(3);
							if (record.getR18_no1_of_sub_branches() != null) {
								R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
								R18cell2.setCellStyle(numberStyle);
							} else {
								R18cell2.setCellValue("");
								R18cell2.setCellStyle(textStyle);
							}

							// R18 Col E
							Cell R18cell3 = row.createCell(4);
							if (record.getR18_no1_of_agencies() != null) {
								R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
								R18cell3.setCellStyle(numberStyle);
							} else {
								R18cell3.setCellValue("");
								R18cell3.setCellStyle(textStyle);
							}
							// R19 Col C
							row = sheet.getRow(18);
							Cell R19cell1 = row.createCell(2);
							if (record.getR19_no1_of_branches() != null) {
								R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
								R19cell1.setCellStyle(numberStyle);
							} else {
								R19cell1.setCellValue("");
								R19cell1.setCellStyle(textStyle);
							}

							// R19 Col D
							Cell R19cell2 = row.createCell(3);
							if (record.getR19_no1_of_sub_branches() != null) {
								R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
								R19cell2.setCellStyle(numberStyle);
							} else {
								R19cell2.setCellValue("");
								R19cell2.setCellStyle(textStyle);
							}

							// R19 Col E
							Cell R19cell3 = row.createCell(4);
							if (record.getR19_no1_of_agencies() != null) {
								R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
								R19cell3.setCellStyle(numberStyle);
							} else {
								R19cell3.setCellValue("");
								R19cell3.setCellStyle(textStyle);
							}
							// TABLE 2
							// R25 Col C
							row = sheet.getRow(24);
							Cell R25cell1 = row.createCell(2);
							if (record.getR25_no_of_atms() != null) {
								R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
								R25cell1.setCellStyle(numberStyle);
							} else {
								R25cell1.setCellValue("");
								R25cell1.setCellStyle(textStyle);
							}

							// R25 Col D
							Cell R25cell2 = row.createCell(3);
							if (record.getR25_no_of_mini_atms() != null) {
								R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);
							}

							// R25 Col E
							Cell R25cell3 = row.createCell(4);
							if (record.getR25_encashment_points() != null) {
								R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
								R25cell3.setCellStyle(numberStyle);
							} else {
								R25cell3.setCellValue("");
								R25cell3.setCellStyle(textStyle);
							}
							// R26 Col C
							row = sheet.getRow(25);
							Cell R26cell1 = row.createCell(2);
							if (record.getR26_no_of_atms() != null) {
								R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
								R26cell1.setCellStyle(numberStyle);
							} else {
								R26cell1.setCellValue("");
								R26cell1.setCellStyle(textStyle);
							}

							// R26 Col D
							Cell R26cell2 = row.createCell(3);
							if (record.getR26_no_of_mini_atms() != null) {
								R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
								R26cell2.setCellStyle(numberStyle);
							} else {
								R26cell2.setCellValue("");
								R26cell2.setCellStyle(textStyle);
							}

							// R26 Col E
							Cell R26cell3 = row.createCell(4);
							if (record.getR26_encashment_points() != null) {
								R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
								R26cell3.setCellStyle(numberStyle);
							} else {
								R26cell3.setCellValue("");
								R26cell3.setCellStyle(textStyle);
							}
							// R27 Col C
							row = sheet.getRow(26);
							Cell R27cell1 = row.createCell(2);
							if (record.getR27_no_of_atms() != null) {
								R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
								R27cell1.setCellStyle(numberStyle);
							} else {
								R27cell1.setCellValue("");
								R27cell1.setCellStyle(textStyle);
							}

							// R27 Col D
							Cell R27cell2 = row.createCell(3);
							if (record.getR27_no_of_mini_atms() != null) {
								R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
								R27cell2.setCellStyle(numberStyle);
							} else {
								R27cell2.setCellValue("");
								R27cell2.setCellStyle(textStyle);
							}

							// R27 Col E
							Cell R27cell3 = row.createCell(4);
							if (record.getR27_encashment_points() != null) {
								R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
								R27cell3.setCellStyle(numberStyle);
							} else {
								R27cell3.setCellValue("");
								R27cell3.setCellStyle(textStyle);
							}
							// R28 Col C
							row = sheet.getRow(27);
							Cell R28cell1 = row.createCell(2);
							if (record.getR28_no_of_atms() != null) {
								R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
								R28cell1.setCellStyle(numberStyle);
							} else {
								R28cell1.setCellValue("");
								R28cell1.setCellStyle(textStyle);
							}

							// R28 Col D
							Cell R28cell2 = row.createCell(3);
							if (record.getR28_no_of_mini_atms() != null) {
								R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
								R28cell2.setCellStyle(numberStyle);
							} else {
								R28cell2.setCellValue("");
								R28cell2.setCellStyle(textStyle);
							}

							// R28 Col E
							Cell R28cell3 = row.createCell(4);
							if (record.getR28_encashment_points() != null) {
								R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
								R28cell3.setCellStyle(numberStyle);
							} else {
								R28cell3.setCellValue("");
								R28cell3.setCellStyle(textStyle);
							}
							// R29 Col C
							row = sheet.getRow(28);
							Cell R29cell1 = row.createCell(2);
							if (record.getR29_no_of_atms() != null) {
								R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
								R29cell1.setCellStyle(numberStyle);
							} else {
								R29cell1.setCellValue("");
								R29cell1.setCellStyle(textStyle);
							}

							// R29 Col D
							Cell R29cell2 = row.createCell(3);
							if (record.getR29_no_of_mini_atms() != null) {
								R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
								R29cell2.setCellStyle(numberStyle);
							} else {
								R29cell2.setCellValue("");
								R29cell2.setCellStyle(textStyle);
							}

							// R29 Col E
							Cell R29cell3 = row.createCell(4);
							if (record.getR29_encashment_points() != null) {
								R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
								R29cell3.setCellStyle(numberStyle);
							} else {
								R29cell3.setCellValue("");
								R29cell3.setCellStyle(textStyle);
							}
							// R30 Col C
							row = sheet.getRow(29);
							Cell R30cell1 = row.createCell(2);
							if (record.getR30_no_of_atms() != null) {
								R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
								R30cell1.setCellStyle(numberStyle);
							} else {
								R30cell1.setCellValue("");
								R30cell1.setCellStyle(textStyle);
							}

							// R30 Col D
							Cell R30cell2 = row.createCell(3);
							if (record.getR30_no_of_mini_atms() != null) {
								R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
								R30cell2.setCellStyle(numberStyle);
							} else {
								R30cell2.setCellValue("");
								R30cell2.setCellStyle(textStyle);
							}

							// R30 Col E
							Cell R30cell3 = row.createCell(4);
							if (record.getR30_encashment_points() != null) {
								R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
								R30cell3.setCellStyle(numberStyle);
							} else {
								R30cell3.setCellValue("");
								R30cell3.setCellStyle(textStyle);
							}
							// R31 Col C
							row = sheet.getRow(30);
							Cell R31cell1 = row.createCell(2);
							if (record.getR31_no_of_atms() != null) {
								R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
								R31cell1.setCellStyle(numberStyle);
							} else {
								R31cell1.setCellValue("");
								R31cell1.setCellStyle(textStyle);
							}

							// R31 Col D
							Cell R31cell2 = row.createCell(3);
							if (record.getR31_no_of_mini_atms() != null) {
								R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
								R31cell2.setCellStyle(numberStyle);
							} else {
								R31cell2.setCellValue("");
								R31cell2.setCellStyle(textStyle);
							}

							// R31 Col E
							Cell R31cell3 = row.createCell(4);
							if (record.getR31_encashment_points() != null) {
								R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
								R31cell3.setCellStyle(numberStyle);
							} else {
								R31cell3.setCellValue("");
								R31cell3.setCellStyle(textStyle);
							}
							// R32 Col C
							row = sheet.getRow(31);
							Cell R32cell1 = row.createCell(2);
							if (record.getR32_no_of_atms() != null) {
								R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
								R32cell1.setCellStyle(numberStyle);
							} else {
								R32cell1.setCellValue("");
								R32cell1.setCellStyle(textStyle);
							}

							// R32 Col D
							Cell R32cell2 = row.createCell(3);
							if (record.getR32_no_of_mini_atms() != null) {
								R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
								R32cell2.setCellStyle(numberStyle);
							} else {
								R32cell2.setCellValue("");
								R32cell2.setCellStyle(textStyle);
							}

							// R32 Col E
							Cell R32cell3 = row.createCell(4);
							if (record.getR32_encashment_points() != null) {
								R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
								R32cell3.setCellStyle(numberStyle);
							} else {
								R32cell3.setCellValue("");
								R32cell3.setCellStyle(textStyle);
							}
							// R33 Col C
							row = sheet.getRow(32);
							Cell R33cell1 = row.createCell(2);
							if (record.getR33_no_of_atms() != null) {
								R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
								R33cell1.setCellStyle(numberStyle);
							} else {
								R33cell1.setCellValue("");
								R33cell1.setCellStyle(textStyle);
							}

							// R33 Col D
							Cell R33cell2 = row.createCell(3);
							if (record.getR33_no_of_mini_atms() != null) {
								R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
								R33cell2.setCellStyle(numberStyle);
							} else {
								R33cell2.setCellValue("");
								R33cell2.setCellStyle(textStyle);
							}

							// R33 Col E
							Cell R33cell3 = row.createCell(4);
							if (record.getR33_encashment_points() != null) {
								R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
								R33cell3.setCellStyle(numberStyle);
							} else {
								R33cell3.setCellValue("");
								R33cell3.setCellStyle(textStyle);
							}
							// R34 Col C
							row = sheet.getRow(33);
							Cell R34cell1 = row.createCell(2);
							if (record.getR34_no_of_atms() != null) {
								R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
								R34cell1.setCellStyle(numberStyle);
							} else {
								R34cell1.setCellValue("");
								R34cell1.setCellStyle(textStyle);
							}

							// R34 Col D
							Cell R34cell2 = row.createCell(3);
							if (record.getR34_no_of_mini_atms() != null) {
								R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
								R34cell2.setCellStyle(numberStyle);
							} else {
								R34cell2.setCellValue("");
								R34cell2.setCellStyle(textStyle);
							}

							// R34 Col E
							Cell R34cell3 = row.createCell(4);
							if (record.getR34_encashment_points() != null) {
								R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
								R34cell3.setCellStyle(numberStyle);
							} else {
								R34cell3.setCellValue("");
								R34cell3.setCellStyle(textStyle);
							}
							// TABLE 3
							// R40 Col C
							row = sheet.getRow(39);
							Cell R40cell1 = row.createCell(2);
							if (record.getR40_opening_no_of_cards() != null) {
								R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
								R40cell1.setCellStyle(numberStyle);
							} else {
								R40cell1.setCellValue("");
								R40cell1.setCellStyle(textStyle);
							}

							// R40 Col D
							Cell R40cell2 = row.createCell(3);
							if (record.getR40_no_of_cards_issued() != null) {
								R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
								R40cell2.setCellStyle(numberStyle);
							} else {
								R40cell2.setCellValue("");
								R40cell2.setCellStyle(textStyle);
							}

							// R40 Col E
							Cell R40cell3 = row.createCell(4);
							if (record.getR40_no_cards_of_closed() != null) {
								R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
								R40cell3.setCellStyle(numberStyle);
							} else {
								R40cell3.setCellValue("");
								R40cell3.setCellStyle(textStyle);
							}

							// R41 Col C
							row = sheet.getRow(40);
							Cell R41cell1 = row.createCell(2);
							if (record.getR41_opening_no_of_cards() != null) {
								R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
								R41cell1.setCellStyle(numberStyle);
							} else {
								R41cell1.setCellValue("");
								R41cell1.setCellStyle(textStyle);
							}

							// R41 Col D
							Cell R41cell2 = row.createCell(3);
							if (record.getR41_no_of_cards_issued() != null) {
								R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
								R41cell2.setCellStyle(numberStyle);
							} else {
								R41cell2.setCellValue("");
								R41cell2.setCellStyle(textStyle);
							}

							// R41 Col E
							Cell R41cell3 = row.createCell(4);
							if (record.getR41_no_cards_of_closed() != null) {
								R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
								R41cell3.setCellStyle(numberStyle);
							} else {
								R41cell3.setCellValue("");
								R41cell3.setCellStyle(textStyle);
							}

							// R42 Col C
							row = sheet.getRow(41);
							Cell R42cell1 = row.createCell(2);
							if (record.getR42_opening_no_of_cards() != null) {
								R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
								R42cell1.setCellStyle(numberStyle);
							} else {
								R42cell1.setCellValue("");
								R42cell1.setCellStyle(textStyle);
							}

							// R42 Col D
							Cell R42cell2 = row.createCell(3);
							if (record.getR42_no_of_cards_issued() != null) {
								R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
								R42cell2.setCellStyle(numberStyle);
							} else {
								R42cell2.setCellValue("");
								R42cell2.setCellStyle(textStyle);
							}

							// R42 Col E
							Cell R42cell3 = row.createCell(4);
							if (record.getR42_no_cards_of_closed() != null) {
								R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
								R42cell3.setCellStyle(numberStyle);
							} else {
								R42cell3.setCellValue("");
								R42cell3.setCellStyle(textStyle);
							}

							// R43 Col C
							row = sheet.getRow(42);
							Cell R43cell1 = row.createCell(2);
							if (record.getR43_opening_no_of_cards() != null) {
								R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
								R43cell1.setCellStyle(numberStyle);
							} else {
								R43cell1.setCellValue("");
								R43cell1.setCellStyle(textStyle);
							}

							// R43 Col D
							Cell R43cell2 = row.createCell(3);
							if (record.getR43_no_of_cards_issued() != null) {
								R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
								R43cell2.setCellStyle(numberStyle);
							} else {
								R43cell2.setCellValue("");
								R43cell2.setCellStyle(textStyle);
							}

							// R43 Col E
							Cell R43cell3 = row.createCell(4);
							if (record.getR43_no_cards_of_closed() != null) {
								R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
								R43cell3.setCellStyle(numberStyle);
							} else {
								R43cell3.setCellValue("");
								R43cell3.setCellStyle(textStyle);
							}
							// R44 Col C
							row = sheet.getRow(43);
							Cell R44cell1 = row.createCell(2);
							if (record.getR44_opening_no_of_cards() != null) {
								R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
								R44cell1.setCellStyle(numberStyle);
							} else {
								R44cell1.setCellValue("");
								R44cell1.setCellStyle(textStyle);
							}

							// R44 Col D
							Cell R44cell2 = row.createCell(3);
							if (record.getR44_no_of_cards_issued() != null) {
								R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
								R44cell2.setCellStyle(numberStyle);
							} else {
								R44cell2.setCellValue("");
								R44cell2.setCellStyle(textStyle);
							}

							// R44 Col E
							Cell R44cell3 = row.createCell(4);
							if (record.getR44_no_cards_of_closed() != null) {
								R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
								R44cell3.setCellStyle(numberStyle);
							} else {
								R44cell3.setCellValue("");
								R44cell3.setCellStyle(textStyle);
							}

							// R45 Col C
							row = sheet.getRow(44);
							Cell R45cell1 = row.createCell(2);
							if (record.getR45_opening_no_of_cards() != null) {
								R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
								R45cell1.setCellStyle(numberStyle);
							} else {
								R45cell1.setCellValue("");
								R45cell1.setCellStyle(textStyle);
							}

							// R45 Col D
							Cell R45cell2 = row.createCell(3);
							if (record.getR45_no_of_cards_issued() != null) {
								R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
								R45cell2.setCellStyle(numberStyle);
							} else {
								R45cell2.setCellValue("");
								R45cell2.setCellStyle(textStyle);
							}

							// R45 Col E
							Cell R45cell3 = row.createCell(4);
							if (record.getR45_no_cards_of_closed() != null) {
								R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
								R45cell3.setCellStyle(numberStyle);
							} else {
								R45cell3.setCellValue("");
								R45cell3.setCellStyle(textStyle);
							}

							// R46 Col C
							row = sheet.getRow(45);
							Cell R46cell1 = row.createCell(2);
							if (record.getR46_opening_no_of_cards() != null) {
								R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
								R46cell1.setCellStyle(numberStyle);
							} else {
								R46cell1.setCellValue("");
								R46cell1.setCellStyle(textStyle);
							}

							// R46 Col D
							Cell R46cell2 = row.createCell(3);
							if (record.getR46_no_of_cards_issued() != null) {
								R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
								R46cell2.setCellStyle(numberStyle);
							} else {
								R46cell2.setCellValue("");
								R46cell2.setCellStyle(textStyle);
							}

							// R46 Col E
							Cell R46cell3 = row.createCell(4);
							if (record.getR46_no_cards_of_closed() != null) {
								R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
								R46cell3.setCellStyle(numberStyle);
							} else {
								R46cell3.setCellValue("");
								R46cell3.setCellStyle(textStyle);
							}

							// R47 Col C
							row = sheet.getRow(46);
							Cell R47cell1 = row.createCell(2);
							if (record.getR47_opening_no_of_cards() != null) {
								R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
								R47cell1.setCellStyle(numberStyle);
							} else {
								R47cell1.setCellValue("");
								R47cell1.setCellStyle(textStyle);
							}

							// R47 Col D
							Cell R47cell2 = row.createCell(3);
							if (record.getR47_no_of_cards_issued() != null) {
								R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
								R47cell2.setCellStyle(numberStyle);
							} else {
								R47cell2.setCellValue("");
								R47cell2.setCellStyle(textStyle);
							}

							// R47 Col E
							Cell R47cell3 = row.createCell(4);
							if (record.getR47_no_cards_of_closed() != null) {
								R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
								R47cell3.setCellStyle(numberStyle);
							} else {
								R47cell3.setCellValue("");
								R47cell3.setCellStyle(textStyle);
							}

							// R48 Col C
							row = sheet.getRow(47);
							Cell R48cell1 = row.createCell(2);
							if (record.getR48_opening_no_of_cards() != null) {
								R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
								R48cell1.setCellStyle(numberStyle);
							} else {
								R48cell1.setCellValue("");
								R48cell1.setCellStyle(textStyle);
							}

							// R48 Col D
							Cell R48cell2 = row.createCell(3);
							if (record.getR48_no_of_cards_issued() != null) {
								R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
								R48cell2.setCellStyle(numberStyle);
							} else {
								R48cell2.setCellValue("");
								R48cell2.setCellStyle(textStyle);
							}

							// R48 Col E
							Cell R48cell3 = row.createCell(4);
							if (record.getR48_no_cards_of_closed() != null) {
								R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
								R48cell3.setCellStyle(numberStyle);
							} else {
								R48cell3.setCellValue("");
								R48cell3.setCellStyle(textStyle);
							}

							// R49 Col C
							row = sheet.getRow(48);
							Cell R49cell1 = row.createCell(2);
							if (record.getR49_opening_no_of_cards() != null) {
								R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
								R49cell1.setCellStyle(numberStyle);
							} else {
								R49cell1.setCellValue("");
								R49cell1.setCellStyle(textStyle);
							}

							// R49 Col D
							Cell R49cell2 = row.createCell(3);
							if (record.getR49_no_of_cards_issued() != null) {
								R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
								R49cell2.setCellStyle(numberStyle);
							} else {
								R49cell2.setCellValue("");
								R49cell2.setCellStyle(textStyle);
							}

							// R49 Col E
							Cell R49cell3 = row.createCell(4);
							if (record.getR49_no_cards_of_closed() != null) {
								R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
								R49cell3.setCellStyle(numberStyle);
							} else {
								R49cell3.setCellValue("");
								R49cell3.setCellStyle(textStyle);
							}

							// TABLE 4
							// R55 Col C
							row = sheet.getRow(54);
							Cell R55cell1 = row.createCell(2);
							if (record.getR55_opening_no_of_cards() != null) {
								R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
								R55cell1.setCellStyle(numberStyle);
							} else {
								R55cell1.setCellValue("");
								R55cell1.setCellStyle(textStyle);
							}

							// R55 Col D
							Cell R55cell2 = row.createCell(3);
							if (record.getR55_no_of_cards_issued() != null) {
								R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
								R55cell2.setCellStyle(numberStyle);
							} else {
								R55cell2.setCellValue("");
								R55cell2.setCellStyle(textStyle);
							}

							// R55 Col E
							Cell R55cell3 = row.createCell(4);
							if (record.getR55_no_cards_of_closed() != null) {
								R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
								R55cell3.setCellStyle(numberStyle);
							} else {
								R55cell3.setCellValue("");
								R55cell3.setCellStyle(textStyle);
							}

							// R56 Col C
							row = sheet.getRow(55);
							Cell R56cell1 = row.createCell(2);
							if (record.getR56_opening_no_of_cards() != null) {
								R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
								R56cell1.setCellStyle(numberStyle);
							} else {
								R56cell1.setCellValue("");
								R56cell1.setCellStyle(textStyle);
							}

							// R56 Col D
							Cell R56cell2 = row.createCell(3);
							if (record.getR56_no_of_cards_issued() != null) {
								R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
								R56cell2.setCellStyle(numberStyle);
							} else {
								R56cell2.setCellValue("");
								R56cell2.setCellStyle(textStyle);
							}

							// R56 Col E
							Cell R56cell3 = row.createCell(4);
							if (record.getR56_no_cards_of_closed() != null) {
								R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
								R56cell3.setCellStyle(numberStyle);
							} else {
								R56cell3.setCellValue("");
								R56cell3.setCellStyle(textStyle);
							}

							// R57 Col C
							row = sheet.getRow(56);
							Cell R57cell1 = row.createCell(2);
							if (record.getR57_opening_no_of_cards() != null) {
								R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
								R57cell1.setCellStyle(numberStyle);
							} else {
								R57cell1.setCellValue("");
								R57cell1.setCellStyle(textStyle);
							}

							// R57 Col D
							Cell R57cell2 = row.createCell(3);
							if (record.getR57_no_of_cards_issued() != null) {
								R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
								R57cell2.setCellStyle(numberStyle);
							} else {
								R57cell2.setCellValue("");
								R57cell2.setCellStyle(textStyle);
							}

							// R57 Col E
							Cell R57cell3 = row.createCell(4);
							if (record.getR57_no_cards_of_closed() != null) {
								R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
								R57cell3.setCellStyle(numberStyle);
							} else {
								R57cell3.setCellValue("");
								R57cell3.setCellStyle(textStyle);
							}

							// R58 Col C
							row = sheet.getRow(57);
							Cell R58cell1 = row.createCell(2);
							if (record.getR58_opening_no_of_cards() != null) {
								R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
								R58cell1.setCellStyle(numberStyle);
							} else {
								R58cell1.setCellValue("");
								R58cell1.setCellStyle(textStyle);
							}

							// R58 Col D
							Cell R58cell2 = row.createCell(3);
							if (record.getR58_no_of_cards_issued() != null) {
								R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
								R58cell2.setCellStyle(numberStyle);
							} else {
								R58cell2.setCellValue("");
								R58cell2.setCellStyle(textStyle);
							}

							// R58 Col E
							Cell R58cell3 = row.createCell(4);
							if (record.getR58_no_cards_of_closed() != null) {
								R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
								R58cell3.setCellStyle(numberStyle);
							} else {
								R58cell3.setCellValue("");
								R58cell3.setCellStyle(textStyle);
							}

							// R59 Col C
							row = sheet.getRow(58);
							Cell R59cell1 = row.createCell(2);
							if (record.getR59_opening_no_of_cards() != null) {
								R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
								R59cell1.setCellStyle(numberStyle);
							} else {
								R59cell1.setCellValue("");
								R59cell1.setCellStyle(textStyle);
							}

							// R59 Col D
							Cell R59cell2 = row.createCell(3);
							if (record.getR59_no_of_cards_issued() != null) {
								R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
								R59cell2.setCellStyle(numberStyle);
							} else {
								R59cell2.setCellValue("");
								R59cell2.setCellStyle(textStyle);
							}

							// R59 Col E
							Cell R59cell3 = row.createCell(4);
							if (record.getR59_no_cards_of_closed() != null) {
								R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
								R59cell3.setCellStyle(numberStyle);
							} else {
								R59cell3.setCellValue("");
								R59cell3.setCellStyle(textStyle);
							}

							// R60 Col C
							row = sheet.getRow(59);
							Cell R60cell1 = row.createCell(2);
							if (record.getR60_opening_no_of_cards() != null) {
								R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
								R60cell1.setCellStyle(numberStyle);
							} else {
								R60cell1.setCellValue("");
								R60cell1.setCellStyle(textStyle);
							}

							// R60 Col D
							Cell R60cell2 = row.createCell(3);
							if (record.getR60_no_of_cards_issued() != null) {
								R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
								R60cell2.setCellStyle(numberStyle);
							} else {
								R60cell2.setCellValue("");
								R60cell2.setCellStyle(textStyle);
							}
							// R60 Col E
							Cell R60cell3 = row.createCell(4);
							if (record.getR60_no_cards_of_closed() != null) {
								R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
								R60cell3.setCellStyle(numberStyle);
							} else {
								R60cell3.setCellValue("");
								R60cell3.setCellStyle(textStyle);
							}

							// R61 Col C
							row = sheet.getRow(60);
							Cell R61cell1 = row.createCell(2);
							if (record.getR61_opening_no_of_cards() != null) {
								R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
								R61cell1.setCellStyle(numberStyle);
							} else {
								R61cell1.setCellValue("");
								R61cell1.setCellStyle(textStyle);
							}

							// R61 Col D
							Cell R61cell2 = row.createCell(3);
							if (record.getR61_no_of_cards_issued() != null) {
								R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
								R61cell2.setCellStyle(numberStyle);
							} else {
								R61cell2.setCellValue("");
								R61cell2.setCellStyle(textStyle);
							}
							// R61 Col E
							Cell R61cell3 = row.createCell(4);
							if (record.getR61_no_cards_of_closed() != null) {
								R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
								R61cell3.setCellStyle(numberStyle);
							} else {
								R61cell3.setCellValue("");
								R61cell3.setCellStyle(textStyle);
							}

							// R62 Col C
							row = sheet.getRow(61);
							Cell R62cell1 = row.createCell(2);
							if (record.getR62_opening_no_of_cards() != null) {
								R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
								R62cell1.setCellStyle(numberStyle);
							} else {
								R62cell1.setCellValue("");
								R62cell1.setCellStyle(textStyle);
							}

							// R62 Col D
							Cell R62cell2 = row.createCell(3);
							if (record.getR62_no_of_cards_issued() != null) {
								R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
								R62cell2.setCellStyle(numberStyle);
							} else {
								R62cell2.setCellValue("");
								R62cell2.setCellStyle(textStyle);
							}
							// R62 Col E
							Cell R62cell3 = row.createCell(4);
							if (record.getR62_no_cards_of_closed() != null) {
								R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
								R62cell3.setCellStyle(numberStyle);
							} else {
								R62cell3.setCellValue("");
								R62cell3.setCellStyle(textStyle);
							}

							// R63 Col C
							row = sheet.getRow(62);
							Cell R63cell1 = row.createCell(2);
							if (record.getR63_opening_no_of_cards() != null) {
								R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
								R63cell1.setCellStyle(numberStyle);
							} else {
								R63cell1.setCellValue("");
								R63cell1.setCellStyle(textStyle);
							}

							// R63 Col D
							Cell R63cell2 = row.createCell(3);
							if (record.getR63_no_of_cards_issued() != null) {
								R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
								R63cell2.setCellStyle(numberStyle);
							} else {
								R63cell2.setCellValue("");
								R63cell2.setCellStyle(textStyle);
							}
							// R63 Col E
							Cell R63cell3 = row.createCell(4);
							if (record.getR63_no_cards_of_closed() != null) {
								R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
								R63cell3.setCellStyle(numberStyle);
							} else {
								R63cell3.setCellValue("");
								R63cell3.setCellStyle(textStyle);
							}

							// R64 Col C
							row = sheet.getRow(63);
							Cell R64cell1 = row.createCell(2);
							if (record.getR64_opening_no_of_cards() != null) {
								R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
								R64cell1.setCellStyle(numberStyle);
							} else {
								R64cell1.setCellValue("");
								R64cell1.setCellStyle(textStyle);
							}

							// R64 Col D
							Cell R64cell2 = row.createCell(3);
							if (record.getR64_no_of_cards_issued() != null) {
								R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
								R64cell2.setCellStyle(numberStyle);
							} else {
								R64cell2.setCellValue("");
								R64cell2.setCellStyle(textStyle);
							}
							// R64 Col E
							Cell R64cell3 = row.createCell(4);
							if (record.getR64_no_cards_of_closed() != null) {
								R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
								R64cell3.setCellStyle(numberStyle);
							} else {
								R64cell3.setCellValue("");
								R64cell3.setCellStyle(textStyle);
							}

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
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET SUMMARY", null,
								"BRRS_Q_BRANCHNET_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

// Summary EXCEL  EMAIL
// Normal Email Excel
	public byte[] BRRS_Q_BRANCHNETEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_BRANCHNETARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_BRANCHNETEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<Q_BRANCHNET_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_Q_BRANCHNET report. Returning empty result.");
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
				// Email Excel
				// --- End of Style Definitions ---

				int startRow = 5;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						Q_BRANCHNET_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(4);

						if (record.getReport_date() != null) {

							R12Cell.setCellValue(record.getReport_date());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(9);
//EMAIL
// R10 Col E

						Cell R10cell1 = row.createCell(4);
						if (record.getR10_no1_of_branches() != null) {
							R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
							R10cell1.setCellStyle(numberStyle);
						} else {
							R10cell1.setCellValue("");
							R10cell1.setCellStyle(textStyle);
						}

						// R10 Col F
						Cell R10cell2 = row.createCell(5);
						if (record.getR10_no1_of_sub_branches() != null) {
							R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
							R10cell2.setCellStyle(numberStyle);
						} else {
							R10cell2.setCellValue("");
							R10cell2.setCellStyle(textStyle);
						}

						// R10 Col G
						Cell R10cell3 = row.createCell(6);
						if (record.getR10_no1_of_agencies() != null) {
							R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
							R10cell3.setCellStyle(numberStyle);
						} else {
							R10cell3.setCellValue("");
							R10cell3.setCellStyle(textStyle);
						}
						// R11 Col E
						row = sheet.getRow(10);
						Cell R11cell1 = row.createCell(4);
						if (record.getR11_no1_of_branches() != null) {
							R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
							R11cell1.setCellStyle(numberStyle);
						} else {
							R11cell1.setCellValue("");
							R11cell1.setCellStyle(textStyle);
						}

						// R11 Col F
						Cell R11cell2 = row.createCell(5);
						if (record.getR11_no1_of_sub_branches() != null) {
							R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
							R11cell2.setCellStyle(numberStyle);
						} else {
							R11cell2.setCellValue("");
							R11cell2.setCellStyle(textStyle);
						}

						// R11 Col G
						Cell R11cell3 = row.createCell(6);
						if (record.getR11_no1_of_agencies() != null) {
							R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
							R11cell3.setCellStyle(numberStyle);
						} else {
							R11cell3.setCellValue("");
							R11cell3.setCellStyle(textStyle);
						}
						// R12 Col E
						row = sheet.getRow(11);
						Cell R12cell1 = row.createCell(4);
						if (record.getR12_no1_of_branches() != null) {
							R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
							R12cell1.setCellStyle(numberStyle);
						} else {
							R12cell1.setCellValue("");
							R12cell1.setCellStyle(textStyle);
						}

						// R12 Col F
						Cell R12cell2 = row.createCell(5);
						if (record.getR12_no1_of_sub_branches() != null) {
							R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
							R12cell2.setCellStyle(numberStyle);
						} else {
							R12cell2.setCellValue("");
							R12cell2.setCellStyle(textStyle);
						}

						// R12 Col G
						Cell R12cell3 = row.createCell(6);
						if (record.getR12_no1_of_agencies() != null) {
							R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
							R12cell3.setCellStyle(numberStyle);
						} else {
							R12cell3.setCellValue("");
							R12cell3.setCellStyle(textStyle);
						}
						// R13 Col E
						row = sheet.getRow(12);
						Cell R13cell1 = row.createCell(4);
						if (record.getR13_no1_of_branches() != null) {
							R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
							R13cell1.setCellStyle(numberStyle);
						} else {
							R13cell1.setCellValue("");
							R13cell1.setCellStyle(textStyle);
						}

						// R13 Col F
						Cell R13cell2 = row.createCell(5);
						if (record.getR13_no1_of_sub_branches() != null) {
							R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
							R13cell2.setCellStyle(numberStyle);
						} else {
							R13cell2.setCellValue("");
							R13cell2.setCellStyle(textStyle);
						}

						// R13 Col G
						Cell R13cell3 = row.createCell(6);
						if (record.getR13_no1_of_agencies() != null) {
							R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
							R13cell3.setCellStyle(numberStyle);
						} else {
							R13cell3.setCellValue("");
							R13cell3.setCellStyle(textStyle);
						}
						// R14 Col E
						row = sheet.getRow(13);
						Cell R14cell1 = row.createCell(4);
						if (record.getR14_no1_of_branches() != null) {
							R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
							R14cell1.setCellStyle(numberStyle);
						} else {
							R14cell1.setCellValue("");
							R14cell1.setCellStyle(textStyle);
						}

						// R14 Col F
						Cell R14cell2 = row.createCell(5);
						if (record.getR14_no1_of_sub_branches() != null) {
							R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
							R14cell2.setCellStyle(numberStyle);
						} else {
							R14cell2.setCellValue("");
							R14cell2.setCellStyle(textStyle);
						}

						// R14 Col G
						Cell R14cell3 = row.createCell(6);
						if (record.getR14_no1_of_agencies() != null) {
							R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
							R14cell3.setCellStyle(numberStyle);
						} else {
							R14cell3.setCellValue("");
							R14cell3.setCellStyle(textStyle);
						}
						// R15 Col E
						row = sheet.getRow(14);
						Cell R15cell1 = row.createCell(4);
						if (record.getR15_no1_of_branches() != null) {
							R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
							R15cell1.setCellStyle(numberStyle);
						} else {
							R15cell1.setCellValue("");
							R15cell1.setCellStyle(textStyle);
						}

						// R15 Col F
						Cell R15cell2 = row.createCell(5);
						if (record.getR15_no1_of_sub_branches() != null) {
							R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}

						// R15 Col G
						Cell R15cell3 = row.createCell(6);
						if (record.getR15_no1_of_agencies() != null) {
							R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}
						// R16 Col E
						row = sheet.getRow(15);
						Cell R16cell1 = row.createCell(4);
						if (record.getR16_no1_of_branches() != null) {
							R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
							R16cell1.setCellStyle(numberStyle);
						} else {
							R16cell1.setCellValue("");
							R16cell1.setCellStyle(textStyle);
						}

						// R16 Col F
						Cell R16cell2 = row.createCell(5);
						if (record.getR16_no1_of_sub_branches() != null) {
							R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}

						// R16 Col G
						Cell R16cell3 = row.createCell(6);
						if (record.getR16_no1_of_agencies() != null) {
							R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}
						// R17 Col E
						row = sheet.getRow(16);
						Cell R17cell1 = row.createCell(4);
						if (record.getR17_no1_of_branches() != null) {
							R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
							R17cell1.setCellStyle(numberStyle);
						} else {
							R17cell1.setCellValue("");
							R17cell1.setCellStyle(textStyle);
						}

						// R17 Col F
						Cell R17cell2 = row.createCell(5);
						if (record.getR17_no1_of_sub_branches() != null) {
							R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);
						}

						// R17 Col G
						Cell R17cell3 = row.createCell(6);
						if (record.getR17_no1_of_agencies() != null) {
							R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
							R17cell3.setCellStyle(numberStyle);
						} else {
							R17cell3.setCellValue("");
							R17cell3.setCellStyle(textStyle);
						}
						// R18 Col E
						row = sheet.getRow(17);
						Cell R18cell1 = row.createCell(4);
						if (record.getR18_no1_of_branches() != null) {
							R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
							R18cell1.setCellStyle(numberStyle);
						} else {
							R18cell1.setCellValue("");
							R18cell1.setCellStyle(textStyle);
						}

						// R18 Col F
						Cell R18cell2 = row.createCell(5);
						if (record.getR18_no1_of_sub_branches() != null) {
							R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);
						}

						// R18 Col G
						Cell R18cell3 = row.createCell(6);
						if (record.getR18_no1_of_agencies() != null) {
							R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
							R18cell3.setCellStyle(numberStyle);
						} else {
							R18cell3.setCellValue("");
							R18cell3.setCellStyle(textStyle);
						}
						// R19 Col E
						row = sheet.getRow(18);
						Cell R19cell1 = row.createCell(4);
						if (record.getR19_no1_of_branches() != null) {
							R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
							R19cell1.setCellStyle(numberStyle);
						} else {
							R19cell1.setCellValue("");
							R19cell1.setCellStyle(textStyle);
						}

						// R19 Col F
						Cell R19cell2 = row.createCell(5);
						if (record.getR19_no1_of_sub_branches() != null) {
							R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
							R19cell2.setCellStyle(numberStyle);
						} else {
							R19cell2.setCellValue("");
							R19cell2.setCellStyle(textStyle);
						}

						// R19 Col G
						Cell R19cell3 = row.createCell(6);
						if (record.getR19_no1_of_agencies() != null) {
							R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
							R19cell3.setCellStyle(numberStyle);
						} else {
							R19cell3.setCellValue("");
							R19cell3.setCellStyle(textStyle);
						}
						// R20 Col E
						row = sheet.getRow(19);
						Cell R20cell1 = row.createCell(4);
						if (record.getR20_no1_of_branches() != null) {
							R20cell1.setCellValue(record.getR20_no1_of_branches().doubleValue());
							R20cell1.setCellStyle(numberStyle);
						} else {
							R20cell1.setCellValue("");
							R20cell1.setCellStyle(textStyle);
						}

						// R20 Col F
						Cell R20cell2 = row.createCell(5);
						if (record.getR20_no1_of_sub_branches() != null) {
							R20cell2.setCellValue(record.getR20_no1_of_sub_branches().doubleValue());
							R20cell2.setCellStyle(numberStyle);
						} else {
							R20cell2.setCellValue("");
							R20cell2.setCellStyle(textStyle);
						}

						// R20 Col G
						Cell R20cell3 = row.createCell(6);
						if (record.getR20_no1_of_agencies() != null) {
							R20cell3.setCellValue(record.getR20_no1_of_agencies().doubleValue());
							R20cell3.setCellStyle(numberStyle);
						} else {
							R20cell3.setCellValue("");
							R20cell3.setCellStyle(textStyle);
						}
						// TABLE 2
						// R25 Col E
						row = sheet.getRow(24);
						Cell R25cell1 = row.createCell(4);
						if (record.getR25_no_of_atms() != null) {
							R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
							R25cell1.setCellStyle(numberStyle);
						} else {
							R25cell1.setCellValue("");
							R25cell1.setCellStyle(textStyle);
						}

						// R25 Col F
						Cell R25cell2 = row.createCell(5);
						if (record.getR25_no_of_mini_atms() != null) {
							R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						// R25 Col G
						Cell R25cell3 = row.createCell(6);
						if (record.getR25_encashment_points() != null) {
							R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}
						// R26 Col E
						row = sheet.getRow(25);
						Cell R26cell1 = row.createCell(4);
						if (record.getR26_no_of_atms() != null) {
							R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
							R26cell1.setCellStyle(numberStyle);
						} else {
							R26cell1.setCellValue("");
							R26cell1.setCellStyle(textStyle);
						}

						// R26 Col F
						Cell R26cell2 = row.createCell(5);
						if (record.getR26_no_of_mini_atms() != null) {
							R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						// R26 Col G
						Cell R26cell3 = row.createCell(6);
						if (record.getR26_encashment_points() != null) {
							R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}
						// R27 Col E
						row = sheet.getRow(26);
						Cell R27cell1 = row.createCell(4);
						if (record.getR27_no_of_atms() != null) {
							R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
							R27cell1.setCellStyle(numberStyle);
						} else {
							R27cell1.setCellValue("");
							R27cell1.setCellStyle(textStyle);
						}

						// R27 Col F
						Cell R27cell2 = row.createCell(5);
						if (record.getR27_no_of_mini_atms() != null) {
							R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						// R27 Col G
						Cell R27cell3 = row.createCell(6);
						if (record.getR27_encashment_points() != null) {
							R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);
						}
						// R28 Col E
						row = sheet.getRow(27);
						Cell R28cell1 = row.createCell(4);
						if (record.getR28_no_of_atms() != null) {
							R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
							R28cell1.setCellStyle(numberStyle);
						} else {
							R28cell1.setCellValue("");
							R28cell1.setCellStyle(textStyle);
						}

						// R28 Col F
						Cell R28cell2 = row.createCell(5);
						if (record.getR28_no_of_mini_atms() != null) {
							R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
							R28cell2.setCellStyle(numberStyle);
						} else {
							R28cell2.setCellValue("");
							R28cell2.setCellStyle(textStyle);
						}

						// R28 Col G
						Cell R28cell3 = row.createCell(6);
						if (record.getR28_encashment_points() != null) {
							R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
							R28cell3.setCellStyle(numberStyle);
						} else {
							R28cell3.setCellValue("");
							R28cell3.setCellStyle(textStyle);
						}
						// R29 Col E
						row = sheet.getRow(28);
						Cell R29cell1 = row.createCell(4);
						if (record.getR29_no_of_atms() != null) {
							R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
							R29cell1.setCellStyle(numberStyle);
						} else {
							R29cell1.setCellValue("");
							R29cell1.setCellStyle(textStyle);
						}

						// R29 Col F
						Cell R29cell2 = row.createCell(5);
						if (record.getR29_no_of_mini_atms() != null) {
							R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
							R29cell2.setCellStyle(numberStyle);
						} else {
							R29cell2.setCellValue("");
							R29cell2.setCellStyle(textStyle);
						}

						// R29 Col G
						Cell R29cell3 = row.createCell(6);
						if (record.getR29_encashment_points() != null) {
							R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
							R29cell3.setCellStyle(numberStyle);
						} else {
							R29cell3.setCellValue("");
							R29cell3.setCellStyle(textStyle);
						}
						// R30 Col E
						row = sheet.getRow(29);
						Cell R30cell1 = row.createCell(4);
						if (record.getR30_no_of_atms() != null) {
							R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
							R30cell1.setCellStyle(numberStyle);
						} else {
							R30cell1.setCellValue("");
							R30cell1.setCellStyle(textStyle);
						}

						// R30 Col F
						Cell R30cell2 = row.createCell(5);
						if (record.getR30_no_of_mini_atms() != null) {
							R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
							R30cell2.setCellStyle(numberStyle);
						} else {
							R30cell2.setCellValue("");
							R30cell2.setCellStyle(textStyle);
						}

						// R30 Col G
						Cell R30cell3 = row.createCell(6);
						if (record.getR30_encashment_points() != null) {
							R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
							R30cell3.setCellStyle(numberStyle);
						} else {
							R30cell3.setCellValue("");
							R30cell3.setCellStyle(textStyle);
						}
						// R31 Col E
						row = sheet.getRow(30);
						Cell R31cell1 = row.createCell(4);
						if (record.getR31_no_of_atms() != null) {
							R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
							R31cell1.setCellStyle(numberStyle);
						} else {
							R31cell1.setCellValue("");
							R31cell1.setCellStyle(textStyle);
						}

						// R31 Col F
						Cell R31cell2 = row.createCell(5);
						if (record.getR31_no_of_mini_atms() != null) {
							R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
							R31cell2.setCellStyle(numberStyle);
						} else {
							R31cell2.setCellValue("");
							R31cell2.setCellStyle(textStyle);
						}

						// R31 Col G
						Cell R31cell3 = row.createCell(6);
						if (record.getR31_encashment_points() != null) {
							R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
							R31cell3.setCellStyle(numberStyle);
						} else {
							R31cell3.setCellValue("");
							R31cell3.setCellStyle(textStyle);
						}
						// R32 Col E
						row = sheet.getRow(31);
						Cell R32cell1 = row.createCell(4);
						if (record.getR32_no_of_atms() != null) {
							R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
							R32cell1.setCellStyle(numberStyle);
						} else {
							R32cell1.setCellValue("");
							R32cell1.setCellStyle(textStyle);
						}

						// R32 Col F
						Cell R32cell2 = row.createCell(5);
						if (record.getR32_no_of_mini_atms() != null) {
							R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
							R32cell2.setCellStyle(numberStyle);
						} else {
							R32cell2.setCellValue("");
							R32cell2.setCellStyle(textStyle);
						}

						// R32 Col G
						Cell R32cell3 = row.createCell(6);
						if (record.getR32_encashment_points() != null) {
							R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
							R32cell3.setCellStyle(numberStyle);
						} else {
							R32cell3.setCellValue("");
							R32cell3.setCellStyle(textStyle);
						}
						// R33 Col E
						row = sheet.getRow(32);
						Cell R33cell1 = row.createCell(4);
						if (record.getR33_no_of_atms() != null) {
							R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
							R33cell1.setCellStyle(numberStyle);
						} else {
							R33cell1.setCellValue("");
							R33cell1.setCellStyle(textStyle);
						}

						// R33 Col F
						Cell R33cell2 = row.createCell(5);
						if (record.getR33_no_of_mini_atms() != null) {
							R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}

						// R33 Col G
						Cell R33cell3 = row.createCell(6);
						if (record.getR33_encashment_points() != null) {
							R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}
						// R34 Col E
						row = sheet.getRow(33);
						Cell R34cell1 = row.createCell(4);
						if (record.getR34_no_of_atms() != null) {
							R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
							R34cell1.setCellStyle(numberStyle);
						} else {
							R34cell1.setCellValue("");
							R34cell1.setCellStyle(textStyle);
						}

						// R34 Col F
						Cell R34cell2 = row.createCell(5);
						if (record.getR34_no_of_mini_atms() != null) {
							R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
							R34cell2.setCellStyle(numberStyle);
						} else {
							R34cell2.setCellValue("");
							R34cell2.setCellStyle(textStyle);
						}

						// R34 Col G
						Cell R34cell3 = row.createCell(6);
						if (record.getR34_encashment_points() != null) {
							R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
							R34cell3.setCellStyle(numberStyle);
						} else {
							R34cell3.setCellValue("");
							R34cell3.setCellStyle(textStyle);
						}
						// R35 Col E
						row = sheet.getRow(34);
						Cell R35cell1 = row.createCell(4);
						if (record.getR35_no_of_atms() != null) {
							R35cell1.setCellValue(record.getR35_no_of_atms().doubleValue());
							R35cell1.setCellStyle(numberStyle);
						} else {
							R35cell1.setCellValue("");
							R35cell1.setCellStyle(textStyle);
						}

						// R35 Col F
						Cell R35cell2 = row.createCell(5);
						if (record.getR35_no_of_mini_atms() != null) {
							R35cell2.setCellValue(record.getR35_no_of_mini_atms().doubleValue());
							R35cell2.setCellStyle(numberStyle);
						} else {
							R35cell2.setCellValue("");
							R35cell2.setCellStyle(textStyle);
						}

						// R35 Col G
						Cell R35cell3 = row.createCell(6);
						if (record.getR35_encashment_points() != null) {
							R35cell3.setCellValue(record.getR35_encashment_points().doubleValue());
							R35cell3.setCellStyle(numberStyle);
						} else {
							R35cell3.setCellValue("");
							R35cell3.setCellStyle(textStyle);
						}
						// TABLE 3
						// R40 Col E
						row = sheet.getRow(39);
						Cell R40cell1 = row.createCell(4);
						if (record.getR40_opening_no_of_cards() != null) {
							R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
							R40cell1.setCellStyle(numberStyle);
						} else {
							R40cell1.setCellValue("");
							R40cell1.setCellStyle(textStyle);
						}

						// R40 Col F
						Cell R40cell2 = row.createCell(5);
						if (record.getR40_no_of_cards_issued() != null) {
							R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						// R40 Col G
						Cell R40cell3 = row.createCell(6);
						if (record.getR40_no_cards_of_closed() != null) {
							R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}
						// R40 Col H
						Cell R40cell4 = row.createCell(7);
						if (record.getR40_closing_bal_of_active_cards() != null) {
							R40cell4.setCellValue(record.getR40_closing_bal_of_active_cards().doubleValue());
							R40cell4.setCellStyle(numberStyle);
						} else {
							R40cell4.setCellValue("");
							R40cell4.setCellStyle(textStyle);
						}

						// R41 Col E
						row = sheet.getRow(40);
						Cell R41cell1 = row.createCell(4);
						if (record.getR41_opening_no_of_cards() != null) {
							R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
							R41cell1.setCellStyle(numberStyle);
						} else {
							R41cell1.setCellValue("");
							R41cell1.setCellStyle(textStyle);
						}

						// R41 Col F
						Cell R41cell2 = row.createCell(5);
						if (record.getR41_no_of_cards_issued() != null) {
							R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						// R41 Col G
						Cell R41cell3 = row.createCell(6);
						if (record.getR41_no_cards_of_closed() != null) {
							R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}
						// R41 Col H
						Cell R41cell4 = row.createCell(7);
						if (record.getR41_closing_bal_of_active_cards() != null) {
							R41cell4.setCellValue(record.getR41_closing_bal_of_active_cards().doubleValue());
							R41cell4.setCellStyle(numberStyle);
						} else {
							R41cell4.setCellValue("");
							R41cell4.setCellStyle(textStyle);
						}
						// R42 Col E
						row = sheet.getRow(41);
						Cell R42cell1 = row.createCell(4);
						if (record.getR42_opening_no_of_cards() != null) {
							R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
							R42cell1.setCellStyle(numberStyle);
						} else {
							R42cell1.setCellValue("");
							R42cell1.setCellStyle(textStyle);
						}

						// R42 Col F
						Cell R42cell2 = row.createCell(5);
						if (record.getR42_no_of_cards_issued() != null) {
							R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
							R42cell2.setCellStyle(numberStyle);
						} else {
							R42cell2.setCellValue("");
							R42cell2.setCellStyle(textStyle);
						}

						// R42 Col G
						Cell R42cell3 = row.createCell(6);
						if (record.getR42_no_cards_of_closed() != null) {
							R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
							R42cell3.setCellStyle(numberStyle);
						} else {
							R42cell3.setCellValue("");
							R42cell3.setCellStyle(textStyle);
						}
						// R42 Col H
						Cell R42cell4 = row.createCell(7);
						if (record.getR42_closing_bal_of_active_cards() != null) {
							R42cell4.setCellValue(record.getR42_closing_bal_of_active_cards().doubleValue());
							R42cell4.setCellStyle(numberStyle);
						} else {
							R42cell4.setCellValue("");
							R42cell4.setCellStyle(textStyle);
						}
						// R43 Col E
						row = sheet.getRow(42);
						Cell R43cell1 = row.createCell(4);
						if (record.getR43_opening_no_of_cards() != null) {
							R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
							R43cell1.setCellStyle(numberStyle);
						} else {
							R43cell1.setCellValue("");
							R43cell1.setCellStyle(textStyle);
						}

						// R43 Col F
						Cell R43cell2 = row.createCell(5);
						if (record.getR43_no_of_cards_issued() != null) {
							R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
							R43cell2.setCellStyle(numberStyle);
						} else {
							R43cell2.setCellValue("");
							R43cell2.setCellStyle(textStyle);
						}

						// R43 Col G
						Cell R43cell3 = row.createCell(6);
						if (record.getR43_no_cards_of_closed() != null) {
							R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
							R43cell3.setCellStyle(numberStyle);
						} else {
							R43cell3.setCellValue("");
							R43cell3.setCellStyle(textStyle);
						}
						// R43 Col H
						Cell R43cell4 = row.createCell(7);
						if (record.getR43_closing_bal_of_active_cards() != null) {
							R43cell4.setCellValue(record.getR43_closing_bal_of_active_cards().doubleValue());
							R43cell4.setCellStyle(numberStyle);
						} else {
							R43cell4.setCellValue("");
							R43cell4.setCellStyle(textStyle);
						}

						// R44 Col E
						row = sheet.getRow(43);
						Cell R44cell1 = row.createCell(4);
						if (record.getR44_opening_no_of_cards() != null) {
							R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
							R44cell1.setCellStyle(numberStyle);
						} else {
							R44cell1.setCellValue("");
							R44cell1.setCellStyle(textStyle);
						}

						// R44 Col F
						Cell R44cell2 = row.createCell(5);
						if (record.getR44_no_of_cards_issued() != null) {
							R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
							R44cell2.setCellStyle(numberStyle);
						} else {
							R44cell2.setCellValue("");
							R44cell2.setCellStyle(textStyle);
						}

						// R44 Col G
						Cell R44cell3 = row.createCell(6);
						if (record.getR44_no_cards_of_closed() != null) {
							R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
							R44cell3.setCellStyle(numberStyle);
						} else {
							R44cell3.setCellValue("");
							R44cell3.setCellStyle(textStyle);
						}
						// R44 Col H
						Cell R44cell4 = row.createCell(7);
						if (record.getR44_closing_bal_of_active_cards() != null) {
							R44cell4.setCellValue(record.getR44_closing_bal_of_active_cards().doubleValue());
							R44cell4.setCellStyle(numberStyle);
						} else {
							R44cell4.setCellValue("");
							R44cell4.setCellStyle(textStyle);
						}
						// R45 Col E
						row = sheet.getRow(44);
						Cell R45cell1 = row.createCell(4);
						if (record.getR45_opening_no_of_cards() != null) {
							R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
							R45cell1.setCellStyle(numberStyle);
						} else {
							R45cell1.setCellValue("");
							R45cell1.setCellStyle(textStyle);
						}

						// R45 Col F
						Cell R45cell2 = row.createCell(5);
						if (record.getR45_no_of_cards_issued() != null) {
							R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
							R45cell2.setCellStyle(numberStyle);
						} else {
							R45cell2.setCellValue("");
							R45cell2.setCellStyle(textStyle);
						}

						// R45 Col G
						Cell R45cell3 = row.createCell(6);
						if (record.getR45_no_cards_of_closed() != null) {
							R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
							R45cell3.setCellStyle(numberStyle);
						} else {
							R45cell3.setCellValue("");
							R45cell3.setCellStyle(textStyle);
						}
						// R45 Col H
						Cell R45cell4 = row.createCell(7);
						if (record.getR45_closing_bal_of_active_cards() != null) {
							R45cell4.setCellValue(record.getR45_closing_bal_of_active_cards().doubleValue());
							R45cell4.setCellStyle(numberStyle);
						} else {
							R45cell4.setCellValue("");
							R45cell4.setCellStyle(textStyle);
						}
						// R46 Col E
						row = sheet.getRow(45);
						Cell R46cell1 = row.createCell(4);
						if (record.getR46_opening_no_of_cards() != null) {
							R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
							R46cell1.setCellStyle(numberStyle);
						} else {
							R46cell1.setCellValue("");
							R46cell1.setCellStyle(textStyle);
						}

						// R46 Col F
						Cell R46cell2 = row.createCell(5);
						if (record.getR46_no_of_cards_issued() != null) {
							R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
							R46cell2.setCellStyle(numberStyle);
						} else {
							R46cell2.setCellValue("");
							R46cell2.setCellStyle(textStyle);
						}

						// R46 Col G
						Cell R46cell3 = row.createCell(6);
						if (record.getR46_no_cards_of_closed() != null) {
							R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
							R46cell3.setCellStyle(numberStyle);
						} else {
							R46cell3.setCellValue("");
							R46cell3.setCellStyle(textStyle);
						}
						// R46 Col H
						Cell R46cell4 = row.createCell(7);
						if (record.getR46_closing_bal_of_active_cards() != null) {
							R46cell4.setCellValue(record.getR46_closing_bal_of_active_cards().doubleValue());
							R46cell4.setCellStyle(numberStyle);
						} else {
							R46cell4.setCellValue("");
							R46cell4.setCellStyle(textStyle);
						}
						// R47 Col E
						row = sheet.getRow(46);
						Cell R47cell1 = row.createCell(4);
						if (record.getR47_opening_no_of_cards() != null) {
							R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
							R47cell1.setCellStyle(numberStyle);
						} else {
							R47cell1.setCellValue("");
							R47cell1.setCellStyle(textStyle);
						}

						// R47 Col F
						Cell R47cell2 = row.createCell(5);
						if (record.getR47_no_of_cards_issued() != null) {
							R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
							R47cell2.setCellStyle(numberStyle);
						} else {
							R47cell2.setCellValue("");
							R47cell2.setCellStyle(textStyle);
						}

						// R47 Col G
						Cell R47cell3 = row.createCell(6);
						if (record.getR47_no_cards_of_closed() != null) {
							R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
							R47cell3.setCellStyle(numberStyle);
						} else {
							R47cell3.setCellValue("");
							R47cell3.setCellStyle(textStyle);
						}

						// R48 Col E
						row = sheet.getRow(47);
						Cell R48cell1 = row.createCell(4);
						if (record.getR48_opening_no_of_cards() != null) {
							R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
							R48cell1.setCellStyle(numberStyle);
						} else {
							R48cell1.setCellValue("");
							R48cell1.setCellStyle(textStyle);
						}

						// R48 Col F
						Cell R48cell2 = row.createCell(5);
						if (record.getR48_no_of_cards_issued() != null) {
							R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
							R48cell2.setCellStyle(numberStyle);
						} else {
							R48cell2.setCellValue("");
							R48cell2.setCellStyle(textStyle);
						}

						// R48 Col G
						Cell R48cell3 = row.createCell(6);
						if (record.getR48_no_cards_of_closed() != null) {
							R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
							R48cell3.setCellStyle(numberStyle);
						} else {
							R48cell3.setCellValue("");
							R48cell3.setCellStyle(textStyle);
						}

						// R49 Col E
						row = sheet.getRow(48);
						Cell R49cell1 = row.createCell(4);
						if (record.getR49_opening_no_of_cards() != null) {
							R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
							R49cell1.setCellStyle(numberStyle);
						} else {
							R49cell1.setCellValue("");
							R49cell1.setCellStyle(textStyle);
						}

						// R49 Col F
						Cell R49cell2 = row.createCell(5);
						if (record.getR49_no_of_cards_issued() != null) {
							R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
							R49cell2.setCellStyle(numberStyle);
						} else {
							R49cell2.setCellValue("");
							R49cell2.setCellStyle(textStyle);
						}

						// R49 Col G
						Cell R49cell3 = row.createCell(6);
						if (record.getR49_no_cards_of_closed() != null) {
							R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
							R49cell3.setCellStyle(numberStyle);
						} else {
							R49cell3.setCellValue("");
							R49cell3.setCellStyle(textStyle);
						}

						// TABLE 4
						// R55 Col E
						row = sheet.getRow(54);
						Cell R55cell1 = row.createCell(4);
						if (record.getR55_opening_no_of_cards() != null) {
							R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
							R55cell1.setCellStyle(numberStyle);
						} else {
							R55cell1.setCellValue("");
							R55cell1.setCellStyle(textStyle);
						}

						// R55 Col F
						Cell R55cell2 = row.createCell(5);
						if (record.getR55_no_of_cards_issued() != null) {
							R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
							R55cell2.setCellStyle(numberStyle);
						} else {
							R55cell2.setCellValue("");
							R55cell2.setCellStyle(textStyle);
						}

						// R55 Col G
						Cell R55cell3 = row.createCell(6);
						if (record.getR55_no_cards_of_closed() != null) {
							R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
							R55cell3.setCellStyle(numberStyle);
						} else {
							R55cell3.setCellValue("");
							R55cell3.setCellStyle(textStyle);
						}
						// R55 Col H
						Cell R55cell4 = row.createCell(7);
						if (record.getR55_closing_bal_of_active_cards() != null) {
							R55cell4.setCellValue(record.getR55_closing_bal_of_active_cards().doubleValue());
							R55cell4.setCellStyle(numberStyle);
						} else {
							R55cell4.setCellValue("");
							R55cell4.setCellStyle(textStyle);
						}
						// R56 Col E
						row = sheet.getRow(55);
						Cell R56cell1 = row.createCell(4);
						if (record.getR56_opening_no_of_cards() != null) {
							R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
							R56cell1.setCellStyle(numberStyle);
						} else {
							R56cell1.setCellValue("");
							R56cell1.setCellStyle(textStyle);
						}

						// R56 Col F
						Cell R56cell2 = row.createCell(5);
						if (record.getR56_no_of_cards_issued() != null) {
							R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
							R56cell2.setCellStyle(numberStyle);
						} else {
							R56cell2.setCellValue("");
							R56cell2.setCellStyle(textStyle);
						}

						// R56 Col G
						Cell R56cell3 = row.createCell(6);
						if (record.getR56_no_cards_of_closed() != null) {
							R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
							R56cell3.setCellStyle(numberStyle);
						} else {
							R56cell3.setCellValue("");
							R56cell3.setCellStyle(textStyle);
						}
						// R56 Col H
						Cell R56cell4 = row.createCell(7);
						if (record.getR56_closing_bal_of_active_cards() != null) {
							R56cell4.setCellValue(record.getR56_closing_bal_of_active_cards().doubleValue());
							R56cell4.setCellStyle(numberStyle);
						} else {
							R56cell4.setCellValue("");
							R56cell4.setCellStyle(textStyle);
						}
						// R57 Col E
						row = sheet.getRow(56);
						Cell R57cell1 = row.createCell(4);
						if (record.getR57_opening_no_of_cards() != null) {
							R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
							R57cell1.setCellStyle(numberStyle);
						} else {
							R57cell1.setCellValue("");
							R57cell1.setCellStyle(textStyle);
						}

						// R57 Col F
						Cell R57cell2 = row.createCell(5);
						if (record.getR57_no_of_cards_issued() != null) {
							R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
							R57cell2.setCellStyle(numberStyle);
						} else {
							R57cell2.setCellValue("");
							R57cell2.setCellStyle(textStyle);
						}

						// R57 Col G
						Cell R57cell3 = row.createCell(6);
						if (record.getR57_no_cards_of_closed() != null) {
							R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
							R57cell3.setCellStyle(numberStyle);
						} else {
							R57cell3.setCellValue("");
							R57cell3.setCellStyle(textStyle);
						}
						// R57 Col H
						Cell R57cell4 = row.createCell(7);
						if (record.getR57_closing_bal_of_active_cards() != null) {
							R57cell4.setCellValue(record.getR57_closing_bal_of_active_cards().doubleValue());
							R57cell4.setCellStyle(numberStyle);
						} else {
							R57cell4.setCellValue("");
							R57cell4.setCellStyle(textStyle);
						}
						// R58 Col E
						row = sheet.getRow(57);
						Cell R58cell1 = row.createCell(4);
						if (record.getR58_opening_no_of_cards() != null) {
							R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
							R58cell1.setCellStyle(numberStyle);
						} else {
							R58cell1.setCellValue("");
							R58cell1.setCellStyle(textStyle);
						}

						// R58 Col F
						Cell R58cell2 = row.createCell(5);
						if (record.getR58_no_of_cards_issued() != null) {
							R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
							R58cell2.setCellStyle(numberStyle);
						} else {
							R58cell2.setCellValue("");
							R58cell2.setCellStyle(textStyle);
						}

						// R58 Col G
						Cell R58cell3 = row.createCell(6);
						if (record.getR58_no_cards_of_closed() != null) {
							R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
							R58cell3.setCellStyle(numberStyle);
						} else {
							R58cell3.setCellValue("");
							R58cell3.setCellStyle(textStyle);
						}
						// R58 Col H
						Cell R58cell4 = row.createCell(7);
						if (record.getR58_closing_bal_of_active_cards() != null) {
							R58cell4.setCellValue(record.getR58_closing_bal_of_active_cards().doubleValue());
							R58cell4.setCellStyle(numberStyle);
						} else {
							R58cell4.setCellValue("");
							R58cell4.setCellStyle(textStyle);
						}
						// // R59 Col E
						// row = sheet.getRow(58);
						// Cell R59cell1 = row.createCell(4);
						// if (record.getR59_opening_no_of_cards() != null) {
						// R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						// R59cell1.setCellStyle(numberStyle);
						// } else {
						// R59cell1.setCellValue("");
						// R59cell1.setCellStyle(textStyle);
						// }

						// // R59 Col F
						// Cell R59cell2 = row.createCell(5);
						// if (record.getR59_no_of_cards_issued() != null) {
						// R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						// R59cell2.setCellStyle(numberStyle);
						// } else {
						// R59cell2.setCellValue("");
						// R59cell2.setCellStyle(textStyle);
						// }

						// // R59 Col G
						// Cell R59cell3 = row.createCell(6);
						// if (record.getR59_no_cards_of_closed() != null) {
						// R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						// R59cell3.setCellStyle(numberStyle);
						// } else {
						// R59cell3.setCellValue("");
						// R59cell3.setCellStyle(textStyle);
						// }
						// // R59 Col H
						// Cell R59cell4 = row.createCell(7);
						// if (record.getR59_closing_bal_of_active_cards() != null) {
						// R59cell4.setCellValue(record.getR59_closing_bal_of_active_cards().doubleValue());
						// R59cell4.setCellStyle(numberStyle);
						// } else {
						// R59cell4.setCellValue("");
						// R59cell4.setCellStyle(textStyle);
						// }

						// R60 Col E
						row = sheet.getRow(59);
						Cell R60cell1 = row.createCell(4);
						if (record.getR60_opening_no_of_cards() != null) {
							R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
							R60cell1.setCellStyle(numberStyle);
						} else {
							R60cell1.setCellValue("");
							R60cell1.setCellStyle(textStyle);
						}

						// R60 Col F
						Cell R60cell2 = row.createCell(5);
						if (record.getR60_no_of_cards_issued() != null) {
							R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
							R60cell2.setCellStyle(numberStyle);
						} else {
							R60cell2.setCellValue("");
							R60cell2.setCellStyle(textStyle);
						}
						// R60 Col G
						Cell R60cell3 = row.createCell(6);
						if (record.getR60_no_cards_of_closed() != null) {
							R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
							R60cell3.setCellStyle(numberStyle);
						} else {
							R60cell3.setCellValue("");
							R60cell3.setCellStyle(textStyle);
						}
						// R60 Col H
						Cell R60cell4 = row.createCell(7);
						if (record.getR60_closing_bal_of_active_cards() != null) {
							R60cell4.setCellValue(record.getR60_closing_bal_of_active_cards().doubleValue());
							R60cell4.setCellStyle(numberStyle);
						} else {
							R60cell4.setCellValue("");
							R60cell4.setCellStyle(textStyle);
						}
						// R61 Col E
						row = sheet.getRow(60);
						Cell R61cell1 = row.createCell(4);
						if (record.getR61_opening_no_of_cards() != null) {
							R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
							R61cell1.setCellStyle(numberStyle);
						} else {
							R61cell1.setCellValue("");
							R61cell1.setCellStyle(textStyle);
						}

						// R61 Col F
						Cell R61cell2 = row.createCell(5);
						if (record.getR61_no_of_cards_issued() != null) {
							R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
							R61cell2.setCellStyle(numberStyle);
						} else {
							R61cell2.setCellValue("");
							R61cell2.setCellStyle(textStyle);
						}
						// R61 Col G
						Cell R61cell3 = row.createCell(6);
						if (record.getR61_no_cards_of_closed() != null) {
							R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
							R61cell3.setCellStyle(numberStyle);
						} else {
							R61cell3.setCellValue("");
							R61cell3.setCellStyle(textStyle);
						}
						// R61 Col H
						Cell R61cell4 = row.createCell(7);
						if (record.getR61_closing_bal_of_active_cards() != null) {
							R61cell4.setCellValue(record.getR61_closing_bal_of_active_cards().doubleValue());
							R61cell4.setCellStyle(numberStyle);
						} else {
							R61cell4.setCellValue("");
							R61cell4.setCellStyle(textStyle);
						}
						// R62 Col E
						row = sheet.getRow(61);
						Cell R62cell1 = row.createCell(4);
						if (record.getR62_opening_no_of_cards() != null) {
							R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
							R62cell1.setCellStyle(numberStyle);
						} else {
							R62cell1.setCellValue("");
							R62cell1.setCellStyle(textStyle);
						}

						// R62 Col F
						Cell R62cell2 = row.createCell(5);
						if (record.getR62_no_of_cards_issued() != null) {
							R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
							R62cell2.setCellStyle(numberStyle);
						} else {
							R62cell2.setCellValue("");
							R62cell2.setCellStyle(textStyle);
						}
						// R62 Col G
						Cell R62cell3 = row.createCell(6);
						if (record.getR62_no_cards_of_closed() != null) {
							R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
							R62cell3.setCellStyle(numberStyle);
						} else {
							R62cell3.setCellValue("");
							R62cell3.setCellStyle(textStyle);
						}
						// R62 Col H
						Cell R62cell4 = row.createCell(7);
						if (record.getR62_closing_bal_of_active_cards() != null) {
							R62cell4.setCellValue(record.getR62_closing_bal_of_active_cards().doubleValue());
							R62cell4.setCellStyle(numberStyle);
						} else {
							R62cell4.setCellValue("");
							R62cell4.setCellStyle(textStyle);
						}
						// R63 Col E
						row = sheet.getRow(62);
						Cell R63cell1 = row.createCell(4);
						if (record.getR63_opening_no_of_cards() != null) {
							R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
							R63cell1.setCellStyle(numberStyle);
						} else {
							R63cell1.setCellValue("");
							R63cell1.setCellStyle(textStyle);
						}

						// R63 Col F
						Cell R63cell2 = row.createCell(5);
						if (record.getR63_no_of_cards_issued() != null) {
							R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
							R63cell2.setCellStyle(numberStyle);
						} else {
							R63cell2.setCellValue("");
							R63cell2.setCellStyle(textStyle);
						}
						// R63 Col G
						Cell R63cell3 = row.createCell(6);
						if (record.getR63_no_cards_of_closed() != null) {
							R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
							R63cell3.setCellStyle(numberStyle);
						} else {
							R63cell3.setCellValue("");
							R63cell3.setCellStyle(textStyle);
						}
						// R63 Col H
						Cell R63cell4 = row.createCell(7);
						if (record.getR63_closing_bal_of_active_cards() != null) {
							R63cell4.setCellValue(record.getR63_closing_bal_of_active_cards().doubleValue());
							R63cell4.setCellStyle(numberStyle);
						} else {
							R63cell4.setCellValue("");
							R63cell4.setCellStyle(textStyle);
						}
						// R64 Col E
						row = sheet.getRow(63);
						Cell R64cell1 = row.createCell(4);
						if (record.getR65_opening_no_of_cards() != null) {
							R64cell1.setCellValue(record.getR65_opening_no_of_cards().doubleValue());
							R64cell1.setCellStyle(numberStyle);
						} else {
							R64cell1.setCellValue("");
							R64cell1.setCellStyle(textStyle);
						}

						// R64 Col F
						Cell R64cell2 = row.createCell(5);
						if (record.getR65_no_of_cards_issued() != null) {
							R64cell2.setCellValue(record.getR65_no_of_cards_issued().doubleValue());
							R64cell2.setCellStyle(numberStyle);
						} else {
							R64cell2.setCellValue("");
							R64cell2.setCellStyle(textStyle);
						}
						// R64 Col G
						Cell R64cell3 = row.createCell(6);
						if (record.getR65_no_cards_of_closed() != null) {
							R64cell3.setCellValue(record.getR65_no_cards_of_closed().doubleValue());
							R64cell3.setCellStyle(numberStyle);
						} else {
							R64cell3.setCellValue("");
							R64cell3.setCellStyle(textStyle);
						}
						// R64 Col H
						Cell R64cell4 = row.createCell(7);
						if (record.getR65_closing_bal_of_active_cards() != null) {
							R64cell4.setCellValue(record.getR65_closing_bal_of_active_cards().doubleValue());
							R64cell4.setCellStyle(numberStyle);
						} else {
							R64cell4.setCellValue("");
							R64cell4.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET EMAIL SUMMARY", null,
							"BRRS_Q_BRANCHNET_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

//ARCHIVAL SUMMARY EXCEL  FORMAT
// Archival format excel
	public byte[] getExcelQ_BRANCHNETARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_BRANCHNETARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_BRANCHNET_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
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
					Q_BRANCHNET_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(9);
//NORMAL
					// R10 Col C

					Cell R10cell1 = row.createCell(2);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col D
					Cell R10cell2 = row.createCell(3);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10cell3 = row.createCell(4);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col C
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(2);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col D
					Cell R11cell2 = row.createCell(3);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11cell3 = row.createCell(4);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col C
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(2);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col D
					Cell R12cell2 = row.createCell(3);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12cell3 = row.createCell(4);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col C
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(2);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col D
					Cell R13cell2 = row.createCell(3);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13cell3 = row.createCell(4);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col C
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(2);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col D
					Cell R14cell2 = row.createCell(3);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14cell3 = row.createCell(4);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col C
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(2);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col D
					Cell R15cell2 = row.createCell(3);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col E
					Cell R15cell3 = row.createCell(4);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col C
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(2);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col D
					Cell R16cell2 = row.createCell(3);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col E
					Cell R16cell3 = row.createCell(4);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col C
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(2);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col D
					Cell R17cell2 = row.createCell(3);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col E
					Cell R17cell3 = row.createCell(4);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col C
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(2);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col D
					Cell R18cell2 = row.createCell(3);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col E
					Cell R18cell3 = row.createCell(4);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col C
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(2);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col D
					Cell R19cell2 = row.createCell(3);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col E
					Cell R19cell3 = row.createCell(4);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col C
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(2);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell2 = row.createCell(3);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell3 = row.createCell(4);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col C
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(2);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell2 = row.createCell(3);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell3 = row.createCell(4);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col C
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(2);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell2 = row.createCell(3);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell3 = row.createCell(4);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col C
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(2);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col D
					Cell R28cell2 = row.createCell(3);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28cell3 = row.createCell(4);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col C
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(2);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col D
					Cell R29cell2 = row.createCell(3);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29cell3 = row.createCell(4);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col C
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(2);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col D
					Cell R30cell2 = row.createCell(3);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30cell3 = row.createCell(4);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col C
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col C
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col C
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(2);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col D
					Cell R33cell2 = row.createCell(3);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell3 = row.createCell(4);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col C
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(2);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col D
					Cell R34cell2 = row.createCell(3);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell3 = row.createCell(4);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col C
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40cell3 = row.createCell(4);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// R41 Col C
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(2);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col D
					Cell R41cell2 = row.createCell(3);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41cell3 = row.createCell(4);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// R42 Col C
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(2);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col D
					Cell R42cell2 = row.createCell(3);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42cell3 = row.createCell(4);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}

					// R43 Col C
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(2);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col D
					Cell R43cell2 = row.createCell(3);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col E
					Cell R43cell3 = row.createCell(4);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R44 Col C
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(2);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col D
					Cell R44cell2 = row.createCell(3);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col E
					Cell R44cell3 = row.createCell(4);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}

					// R45 Col C
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(2);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col D
					Cell R45cell2 = row.createCell(3);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col E
					Cell R45cell3 = row.createCell(4);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}

					// R46 Col C
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(2);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col D
					Cell R46cell2 = row.createCell(3);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col E
					Cell R46cell3 = row.createCell(4);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}

					// R47 Col C
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(2);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col D
					Cell R47cell2 = row.createCell(3);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col E
					Cell R47cell3 = row.createCell(4);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col C
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(2);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col D
					Cell R48cell2 = row.createCell(3);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48cell3 = row.createCell(4);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col C
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(2);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col D
					Cell R49cell2 = row.createCell(3);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49cell3 = row.createCell(4);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col C
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(2);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col D
					Cell R55cell2 = row.createCell(3);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55cell3 = row.createCell(4);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}

					// R56 Col C
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(2);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col D
					Cell R56cell2 = row.createCell(3);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56cell3 = row.createCell(4);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}

					// R57 Col C
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(2);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col D
					Cell R57cell2 = row.createCell(3);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57cell3 = row.createCell(4);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}

					// R58 Col C
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(2);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col D
					Cell R58cell2 = row.createCell(3);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58cell3 = row.createCell(4);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}

					// R59 Col C
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(2);
					if (record.getR59_opening_no_of_cards() != null) {
						R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// R59 Col D
					Cell R59cell2 = row.createCell(3);
					if (record.getR59_no_of_cards_issued() != null) {
						R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59cell3 = row.createCell(4);
					if (record.getR59_no_cards_of_closed() != null) {
						R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}

					// R60 Col C
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(2);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col D
					Cell R60cell2 = row.createCell(3);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col E
					Cell R60cell3 = row.createCell(4);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}

					// R61 Col C
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(2);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col D
					Cell R61cell2 = row.createCell(3);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col E
					Cell R61cell3 = row.createCell(4);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}

					// R62 Col C
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(2);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col D
					Cell R62cell2 = row.createCell(3);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col E
					Cell R62cell3 = row.createCell(4);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}

					// R63 Col C
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(2);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col D
					Cell R63cell2 = row.createCell(3);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col E
					Cell R63cell3 = row.createCell(4);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}

					// R64 Col C
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(2);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col D
					Cell R64cell2 = row.createCell(3);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col E
					Cell R64cell3 = row.createCell(4);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET ARCHIVAL SUMMARY", null,
						"BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

//ARCHIVAL SUMMARY EXCEL  EMAIL

// Archival Email Excel
	public byte[] BRRS_Q_BRANCHNETARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_BRANCHNET_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_BRANCHNET report. Returning empty result.");
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
			// Email Excel
			// --- End of Style Definitions ---

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_BRANCHNET_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(4);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);
//EMAIL
// R10 Col E

					Cell R10cell1 = row.createCell(4);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10cell2 = row.createCell(5);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col G
					Cell R10cell3 = row.createCell(6);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col E
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(4);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11cell2 = row.createCell(5);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col G
					Cell R11cell3 = row.createCell(6);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col E
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(4);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12cell2 = row.createCell(5);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col G
					Cell R12cell3 = row.createCell(6);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col E
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(4);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13cell2 = row.createCell(5);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col G
					Cell R13cell3 = row.createCell(6);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col E
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(4);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14cell2 = row.createCell(5);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col G
					Cell R14cell3 = row.createCell(6);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col E
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(4);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15cell2 = row.createCell(5);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col G
					Cell R15cell3 = row.createCell(6);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col E
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(4);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col F
					Cell R16cell2 = row.createCell(5);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col G
					Cell R16cell3 = row.createCell(6);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col E
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(4);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17cell2 = row.createCell(5);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col G
					Cell R17cell3 = row.createCell(6);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col E
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(4);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18cell2 = row.createCell(5);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col G
					Cell R18cell3 = row.createCell(6);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col E
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(4);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19cell2 = row.createCell(5);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col G
					Cell R19cell3 = row.createCell(6);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// R20 Col E
					row = sheet.getRow(19);
					Cell R20cell1 = row.createCell(4);
					if (record.getR20_no1_of_branches() != null) {
						R20cell1.setCellValue(record.getR20_no1_of_branches().doubleValue());
						R20cell1.setCellStyle(numberStyle);
					} else {
						R20cell1.setCellValue("");
						R20cell1.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20cell2 = row.createCell(5);
					if (record.getR20_no1_of_sub_branches() != null) {
						R20cell2.setCellValue(record.getR20_no1_of_sub_branches().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					// R20 Col G
					Cell R20cell3 = row.createCell(6);
					if (record.getR20_no1_of_agencies() != null) {
						R20cell3.setCellValue(record.getR20_no1_of_agencies().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col E
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(4);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25cell2 = row.createCell(5);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col G
					Cell R25cell3 = row.createCell(6);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col E
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(4);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26cell2 = row.createCell(5);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col G
					Cell R26cell3 = row.createCell(6);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col E
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(4);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27cell2 = row.createCell(5);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col G
					Cell R27cell3 = row.createCell(6);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col E
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(4);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28cell2 = row.createCell(5);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col G
					Cell R28cell3 = row.createCell(6);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col E
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(4);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29cell2 = row.createCell(5);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col G
					Cell R29cell3 = row.createCell(6);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col E
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(4);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30cell2 = row.createCell(5);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col G
					Cell R30cell3 = row.createCell(6);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col E
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(4);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col F
					Cell R31cell2 = row.createCell(5);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col G
					Cell R31cell3 = row.createCell(6);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col E
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(4);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col F
					Cell R32cell2 = row.createCell(5);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col G
					Cell R32cell3 = row.createCell(6);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col E
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(4);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col F
					Cell R33cell2 = row.createCell(5);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col G
					Cell R33cell3 = row.createCell(6);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col E
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(4);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col F
					Cell R34cell2 = row.createCell(5);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col G
					Cell R34cell3 = row.createCell(6);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// R35 Col E
					row = sheet.getRow(34);
					Cell R35cell1 = row.createCell(4);
					if (record.getR35_no_of_atms() != null) {
						R35cell1.setCellValue(record.getR35_no_of_atms().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col F
					Cell R35cell2 = row.createCell(5);
					if (record.getR35_no_of_mini_atms() != null) {
						R35cell2.setCellValue(record.getR35_no_of_mini_atms().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					// R35 Col G
					Cell R35cell3 = row.createCell(6);
					if (record.getR35_encashment_points() != null) {
						R35cell3.setCellValue(record.getR35_encashment_points().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col E
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(4);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40cell2 = row.createCell(5);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col G
					Cell R40cell3 = row.createCell(6);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}
					// R40 Col H
					Cell R40cell4 = row.createCell(7);
					if (record.getR40_closing_bal_of_active_cards() != null) {
						R40cell4.setCellValue(record.getR40_closing_bal_of_active_cards().doubleValue());
						R40cell4.setCellStyle(numberStyle);
					} else {
						R40cell4.setCellValue("");
						R40cell4.setCellStyle(textStyle);
					}

					// R41 Col E
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(4);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41cell2 = row.createCell(5);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col G
					Cell R41cell3 = row.createCell(6);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}
					// R41 Col H
					Cell R41cell4 = row.createCell(7);
					if (record.getR41_closing_bal_of_active_cards() != null) {
						R41cell4.setCellValue(record.getR41_closing_bal_of_active_cards().doubleValue());
						R41cell4.setCellStyle(numberStyle);
					} else {
						R41cell4.setCellValue("");
						R41cell4.setCellStyle(textStyle);
					}
					// R42 Col E
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(4);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42cell2 = row.createCell(5);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col G
					Cell R42cell3 = row.createCell(6);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// R42 Col H
					Cell R42cell4 = row.createCell(7);
					if (record.getR42_closing_bal_of_active_cards() != null) {
						R42cell4.setCellValue(record.getR42_closing_bal_of_active_cards().doubleValue());
						R42cell4.setCellStyle(numberStyle);
					} else {
						R42cell4.setCellValue("");
						R42cell4.setCellStyle(textStyle);
					}
					// R43 Col E
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(4);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col F
					Cell R43cell2 = row.createCell(5);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col G
					Cell R43cell3 = row.createCell(6);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R43 Col H
					Cell R43cell4 = row.createCell(7);
					if (record.getR43_closing_bal_of_active_cards() != null) {
						R43cell4.setCellValue(record.getR43_closing_bal_of_active_cards().doubleValue());
						R43cell4.setCellStyle(numberStyle);
					} else {
						R43cell4.setCellValue("");
						R43cell4.setCellStyle(textStyle);
					}

					// R44 Col E
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(4);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col F
					Cell R44cell2 = row.createCell(5);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col G
					Cell R44cell3 = row.createCell(6);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}
					// R44 Col H
					Cell R44cell4 = row.createCell(7);
					if (record.getR44_closing_bal_of_active_cards() != null) {
						R44cell4.setCellValue(record.getR44_closing_bal_of_active_cards().doubleValue());
						R44cell4.setCellStyle(numberStyle);
					} else {
						R44cell4.setCellValue("");
						R44cell4.setCellStyle(textStyle);
					}
					// R45 Col E
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(4);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col F
					Cell R45cell2 = row.createCell(5);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col G
					Cell R45cell3 = row.createCell(6);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}
					// R45 Col H
					Cell R45cell4 = row.createCell(7);
					if (record.getR45_closing_bal_of_active_cards() != null) {
						R45cell4.setCellValue(record.getR45_closing_bal_of_active_cards().doubleValue());
						R45cell4.setCellStyle(numberStyle);
					} else {
						R45cell4.setCellValue("");
						R45cell4.setCellStyle(textStyle);
					}
					// R46 Col E
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(4);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col F
					Cell R46cell2 = row.createCell(5);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col G
					Cell R46cell3 = row.createCell(6);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}
					// R46 Col H
					Cell R46cell4 = row.createCell(7);
					if (record.getR46_closing_bal_of_active_cards() != null) {
						R46cell4.setCellValue(record.getR46_closing_bal_of_active_cards().doubleValue());
						R46cell4.setCellStyle(numberStyle);
					} else {
						R46cell4.setCellValue("");
						R46cell4.setCellStyle(textStyle);
					}
					// R47 Col E
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(4);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col F
					Cell R47cell2 = row.createCell(5);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col G
					Cell R47cell3 = row.createCell(6);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col E
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(4);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48cell2 = row.createCell(5);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col G
					Cell R48cell3 = row.createCell(6);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col E
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(4);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49cell2 = row.createCell(5);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col G
					Cell R49cell3 = row.createCell(6);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col E
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(4);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55cell2 = row.createCell(5);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col G
					Cell R55cell3 = row.createCell(6);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}
					// R55 Col H
					Cell R55cell4 = row.createCell(7);
					if (record.getR55_closing_bal_of_active_cards() != null) {
						R55cell4.setCellValue(record.getR55_closing_bal_of_active_cards().doubleValue());
						R55cell4.setCellStyle(numberStyle);
					} else {
						R55cell4.setCellValue("");
						R55cell4.setCellStyle(textStyle);
					}
					// R56 Col E
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(4);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56cell2 = row.createCell(5);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col G
					Cell R56cell3 = row.createCell(6);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}
					// R56 Col H
					Cell R56cell4 = row.createCell(7);
					if (record.getR56_closing_bal_of_active_cards() != null) {
						R56cell4.setCellValue(record.getR56_closing_bal_of_active_cards().doubleValue());
						R56cell4.setCellStyle(numberStyle);
					} else {
						R56cell4.setCellValue("");
						R56cell4.setCellStyle(textStyle);
					}
					// R57 Col E
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(4);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57cell2 = row.createCell(5);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col G
					Cell R57cell3 = row.createCell(6);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}
					// R57 Col H
					Cell R57cell4 = row.createCell(7);
					if (record.getR57_closing_bal_of_active_cards() != null) {
						R57cell4.setCellValue(record.getR57_closing_bal_of_active_cards().doubleValue());
						R57cell4.setCellStyle(numberStyle);
					} else {
						R57cell4.setCellValue("");
						R57cell4.setCellStyle(textStyle);
					}
					// R58 Col E
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(4);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58cell2 = row.createCell(5);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col G
					Cell R58cell3 = row.createCell(6);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}
					// R58 Col H
					Cell R58cell4 = row.createCell(7);
					if (record.getR58_closing_bal_of_active_cards() != null) {
						R58cell4.setCellValue(record.getR58_closing_bal_of_active_cards().doubleValue());
						R58cell4.setCellStyle(numberStyle);
					} else {
						R58cell4.setCellValue("");
						R58cell4.setCellStyle(textStyle);
					}
					// // R59 Col E
					// row = sheet.getRow(58);
					// Cell R59cell1 = row.createCell(4);
					// if (record.getR59_opening_no_of_cards() != null) {
					// R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
					// R59cell1.setCellStyle(numberStyle);
					// } else {
					// R59cell1.setCellValue("");
					// R59cell1.setCellStyle(textStyle);
					// }

					// // R59 Col F
					// Cell R59cell2 = row.createCell(5);
					// if (record.getR59_no_of_cards_issued() != null) {
					// R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
					// R59cell2.setCellStyle(numberStyle);
					// } else {
					// R59cell2.setCellValue("");
					// R59cell2.setCellStyle(textStyle);
					// }

					// // R59 Col G
					// Cell R59cell3 = row.createCell(6);
					// if (record.getR59_no_cards_of_closed() != null) {
					// R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
					// R59cell3.setCellStyle(numberStyle);
					// } else {
					// R59cell3.setCellValue("");
					// R59cell3.setCellStyle(textStyle);
					// }
					// // R59 Col H
					// Cell R59cell4 = row.createCell(7);
					// if (record.getR59_closing_bal_of_active_cards() != null) {
					// R59cell4.setCellValue(record.getR59_closing_bal_of_active_cards().doubleValue());
					// R59cell4.setCellStyle(numberStyle);
					// } else {
					// R59cell4.setCellValue("");
					// R59cell4.setCellStyle(textStyle);
					// }

					// R60 Col E
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(4);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60cell2 = row.createCell(5);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60cell3 = row.createCell(6);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}
					// R60 Col H
					Cell R60cell4 = row.createCell(7);
					if (record.getR60_closing_bal_of_active_cards() != null) {
						R60cell4.setCellValue(record.getR60_closing_bal_of_active_cards().doubleValue());
						R60cell4.setCellStyle(numberStyle);
					} else {
						R60cell4.setCellValue("");
						R60cell4.setCellStyle(textStyle);
					}
					// R61 Col E
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(4);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61cell2 = row.createCell(5);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61cell3 = row.createCell(6);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}
					// R61 Col H
					Cell R61cell4 = row.createCell(7);
					if (record.getR61_closing_bal_of_active_cards() != null) {
						R61cell4.setCellValue(record.getR61_closing_bal_of_active_cards().doubleValue());
						R61cell4.setCellStyle(numberStyle);
					} else {
						R61cell4.setCellValue("");
						R61cell4.setCellStyle(textStyle);
					}
					// R62 Col E
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(4);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62cell2 = row.createCell(5);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62cell3 = row.createCell(6);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}
					// R62 Col H
					Cell R62cell4 = row.createCell(7);
					if (record.getR62_closing_bal_of_active_cards() != null) {
						R62cell4.setCellValue(record.getR62_closing_bal_of_active_cards().doubleValue());
						R62cell4.setCellStyle(numberStyle);
					} else {
						R62cell4.setCellValue("");
						R62cell4.setCellStyle(textStyle);
					}
					// R63 Col E
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(4);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col F
					Cell R63cell2 = row.createCell(5);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col G
					Cell R63cell3 = row.createCell(6);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}
					// R63 Col H
					Cell R63cell4 = row.createCell(7);
					if (record.getR63_closing_bal_of_active_cards() != null) {
						R63cell4.setCellValue(record.getR63_closing_bal_of_active_cards().doubleValue());
						R63cell4.setCellStyle(numberStyle);
					} else {
						R63cell4.setCellValue("");
						R63cell4.setCellStyle(textStyle);
					}
					// R64 Col E
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(4);
					if (record.getR65_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR65_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col F
					Cell R64cell2 = row.createCell(5);
					if (record.getR65_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR65_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col G
					Cell R64cell3 = row.createCell(6);
					if (record.getR65_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR65_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
					}
					// R64 Col H
					Cell R64cell4 = row.createCell(7);
					if (record.getR65_closing_bal_of_active_cards() != null) {
						R64cell4.setCellValue(record.getR65_closing_bal_of_active_cards().doubleValue());
						R64cell4.setCellStyle(numberStyle);
					} else {
						R64cell4.setCellValue("");
						R64cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_Q_BRANCHNET_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

// RESUB EXCEL  FORMAT

	// Resub Format excel
	public byte[] BRRS_Q_BRANCHNETResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_BRANCHNETEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_BRANCHNET_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_BRANCHNET report. Returning empty result.");
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
					Q_BRANCHNET_Resub_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(9);
//NORMAL
					// R10 Col C

					Cell R10cell1 = row.createCell(2);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col D
					Cell R10cell2 = row.createCell(3);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10cell3 = row.createCell(4);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col C
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(2);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col D
					Cell R11cell2 = row.createCell(3);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col E
					Cell R11cell3 = row.createCell(4);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col C
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(2);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col D
					Cell R12cell2 = row.createCell(3);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col E
					Cell R12cell3 = row.createCell(4);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col C
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(2);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col D
					Cell R13cell2 = row.createCell(3);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13cell3 = row.createCell(4);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col C
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(2);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col D
					Cell R14cell2 = row.createCell(3);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14cell3 = row.createCell(4);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col C
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(2);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col D
					Cell R15cell2 = row.createCell(3);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col E
					Cell R15cell3 = row.createCell(4);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col C
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(2);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col D
					Cell R16cell2 = row.createCell(3);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col E
					Cell R16cell3 = row.createCell(4);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col C
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(2);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col D
					Cell R17cell2 = row.createCell(3);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col E
					Cell R17cell3 = row.createCell(4);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col C
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(2);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col D
					Cell R18cell2 = row.createCell(3);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col E
					Cell R18cell3 = row.createCell(4);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col C
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(2);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col D
					Cell R19cell2 = row.createCell(3);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col E
					Cell R19cell3 = row.createCell(4);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col C
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(2);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col D
					Cell R25cell2 = row.createCell(3);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col E
					Cell R25cell3 = row.createCell(4);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col C
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(2);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col D
					Cell R26cell2 = row.createCell(3);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col E
					Cell R26cell3 = row.createCell(4);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col C
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(2);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col D
					Cell R27cell2 = row.createCell(3);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col E
					Cell R27cell3 = row.createCell(4);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col C
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(2);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col D
					Cell R28cell2 = row.createCell(3);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col E
					Cell R28cell3 = row.createCell(4);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col C
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(2);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col D
					Cell R29cell2 = row.createCell(3);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col E
					Cell R29cell3 = row.createCell(4);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col C
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(2);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col D
					Cell R30cell2 = row.createCell(3);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col E
					Cell R30cell3 = row.createCell(4);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col C
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(2);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col D
					Cell R31cell2 = row.createCell(3);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col E
					Cell R31cell3 = row.createCell(4);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col C
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(2);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col D
					Cell R32cell2 = row.createCell(3);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col E
					Cell R32cell3 = row.createCell(4);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col C
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(2);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col D
					Cell R33cell2 = row.createCell(3);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell3 = row.createCell(4);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col C
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(2);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col D
					Cell R34cell2 = row.createCell(3);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell3 = row.createCell(4);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col C
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(2);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col D
					Cell R40cell2 = row.createCell(3);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col E
					Cell R40cell3 = row.createCell(4);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// R41 Col C
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(2);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col D
					Cell R41cell2 = row.createCell(3);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col E
					Cell R41cell3 = row.createCell(4);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// R42 Col C
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(2);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col D
					Cell R42cell2 = row.createCell(3);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col E
					Cell R42cell3 = row.createCell(4);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}

					// R43 Col C
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(2);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col D
					Cell R43cell2 = row.createCell(3);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col E
					Cell R43cell3 = row.createCell(4);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R44 Col C
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(2);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col D
					Cell R44cell2 = row.createCell(3);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col E
					Cell R44cell3 = row.createCell(4);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}

					// R45 Col C
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(2);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col D
					Cell R45cell2 = row.createCell(3);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col E
					Cell R45cell3 = row.createCell(4);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}

					// R46 Col C
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(2);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col D
					Cell R46cell2 = row.createCell(3);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col E
					Cell R46cell3 = row.createCell(4);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}

					// R47 Col C
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(2);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col D
					Cell R47cell2 = row.createCell(3);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col E
					Cell R47cell3 = row.createCell(4);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col C
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(2);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col D
					Cell R48cell2 = row.createCell(3);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col E
					Cell R48cell3 = row.createCell(4);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col C
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(2);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col D
					Cell R49cell2 = row.createCell(3);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col E
					Cell R49cell3 = row.createCell(4);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col C
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(2);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col D
					Cell R55cell2 = row.createCell(3);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col E
					Cell R55cell3 = row.createCell(4);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}

					// R56 Col C
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(2);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col D
					Cell R56cell2 = row.createCell(3);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col E
					Cell R56cell3 = row.createCell(4);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}

					// R57 Col C
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(2);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col D
					Cell R57cell2 = row.createCell(3);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col E
					Cell R57cell3 = row.createCell(4);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}

					// R58 Col C
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(2);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col D
					Cell R58cell2 = row.createCell(3);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col E
					Cell R58cell3 = row.createCell(4);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}

					// R59 Col C
					row = sheet.getRow(58);
					Cell R59cell1 = row.createCell(2);
					if (record.getR59_opening_no_of_cards() != null) {
						R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
						R59cell1.setCellStyle(numberStyle);
					} else {
						R59cell1.setCellValue("");
						R59cell1.setCellStyle(textStyle);
					}

					// R59 Col D
					Cell R59cell2 = row.createCell(3);
					if (record.getR59_no_of_cards_issued() != null) {
						R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
						R59cell2.setCellStyle(numberStyle);
					} else {
						R59cell2.setCellValue("");
						R59cell2.setCellStyle(textStyle);
					}

					// R59 Col E
					Cell R59cell3 = row.createCell(4);
					if (record.getR59_no_cards_of_closed() != null) {
						R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
						R59cell3.setCellStyle(numberStyle);
					} else {
						R59cell3.setCellValue("");
						R59cell3.setCellStyle(textStyle);
					}

					// R60 Col C
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(2);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col D
					Cell R60cell2 = row.createCell(3);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col E
					Cell R60cell3 = row.createCell(4);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}

					// R61 Col C
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(2);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col D
					Cell R61cell2 = row.createCell(3);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col E
					Cell R61cell3 = row.createCell(4);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}

					// R62 Col C
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(2);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col D
					Cell R62cell2 = row.createCell(3);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col E
					Cell R62cell3 = row.createCell(4);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}

					// R63 Col C
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(2);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col D
					Cell R63cell2 = row.createCell(3);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col E
					Cell R63cell3 = row.createCell(4);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}

					// R64 Col C
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(2);
					if (record.getR64_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR64_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col D
					Cell R64cell2 = row.createCell(3);
					if (record.getR64_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR64_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col E
					Cell R64cell3 = row.createCell(4);
					if (record.getR64_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR64_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET RESUB SUMMARY", null,
						"BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

// RESUB  EXCEL EMAIL
	// Resub Email Excel
	public byte[] BRRS_Q_BRANCHNETEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB Email Excel generation process in memory.");

		List<Q_BRANCHNET_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_BRANCHNET report. Returning empty result.");
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
			// Email Format
			// --- End of Style Definitions ---

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Q_BRANCHNET_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(4);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(9);
//EMAIL
// R10 Col E

					Cell R10cell1 = row.createCell(4);
					if (record.getR10_no1_of_branches() != null) {
						R10cell1.setCellValue(record.getR10_no1_of_branches().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col F
					Cell R10cell2 = row.createCell(5);
					if (record.getR10_no1_of_sub_branches() != null) {
						R10cell2.setCellValue(record.getR10_no1_of_sub_branches().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}

					// R10 Col G
					Cell R10cell3 = row.createCell(6);
					if (record.getR10_no1_of_agencies() != null) {
						R10cell3.setCellValue(record.getR10_no1_of_agencies().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col E
					row = sheet.getRow(10);
					Cell R11cell1 = row.createCell(4);
					if (record.getR11_no1_of_branches() != null) {
						R11cell1.setCellValue(record.getR11_no1_of_branches().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col F
					Cell R11cell2 = row.createCell(5);
					if (record.getR11_no1_of_sub_branches() != null) {
						R11cell2.setCellValue(record.getR11_no1_of_sub_branches().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 Col G
					Cell R11cell3 = row.createCell(6);
					if (record.getR11_no1_of_agencies() != null) {
						R11cell3.setCellValue(record.getR11_no1_of_agencies().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col E
					row = sheet.getRow(11);
					Cell R12cell1 = row.createCell(4);
					if (record.getR12_no1_of_branches() != null) {
						R12cell1.setCellValue(record.getR12_no1_of_branches().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col F
					Cell R12cell2 = row.createCell(5);
					if (record.getR12_no1_of_sub_branches() != null) {
						R12cell2.setCellValue(record.getR12_no1_of_sub_branches().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 Col G
					Cell R12cell3 = row.createCell(6);
					if (record.getR12_no1_of_agencies() != null) {
						R12cell3.setCellValue(record.getR12_no1_of_agencies().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}
					// R13 Col E
					row = sheet.getRow(12);
					Cell R13cell1 = row.createCell(4);
					if (record.getR13_no1_of_branches() != null) {
						R13cell1.setCellValue(record.getR13_no1_of_branches().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13cell2 = row.createCell(5);
					if (record.getR13_no1_of_sub_branches() != null) {
						R13cell2.setCellValue(record.getR13_no1_of_sub_branches().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 Col G
					Cell R13cell3 = row.createCell(6);
					if (record.getR13_no1_of_agencies() != null) {
						R13cell3.setCellValue(record.getR13_no1_of_agencies().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}
					// R14 Col E
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(4);
					if (record.getR14_no1_of_branches() != null) {
						R14cell1.setCellValue(record.getR14_no1_of_branches().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14cell2 = row.createCell(5);
					if (record.getR14_no1_of_sub_branches() != null) {
						R14cell2.setCellValue(record.getR14_no1_of_sub_branches().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 Col G
					Cell R14cell3 = row.createCell(6);
					if (record.getR14_no1_of_agencies() != null) {
						R14cell3.setCellValue(record.getR14_no1_of_agencies().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}
					// R15 Col E
					row = sheet.getRow(14);
					Cell R15cell1 = row.createCell(4);
					if (record.getR15_no1_of_branches() != null) {
						R15cell1.setCellValue(record.getR15_no1_of_branches().doubleValue());
						R15cell1.setCellStyle(numberStyle);
					} else {
						R15cell1.setCellValue("");
						R15cell1.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15cell2 = row.createCell(5);
					if (record.getR15_no1_of_sub_branches() != null) {
						R15cell2.setCellValue(record.getR15_no1_of_sub_branches().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}

					// R15 Col G
					Cell R15cell3 = row.createCell(6);
					if (record.getR15_no1_of_agencies() != null) {
						R15cell3.setCellValue(record.getR15_no1_of_agencies().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}
					// R16 Col E
					row = sheet.getRow(15);
					Cell R16cell1 = row.createCell(4);
					if (record.getR16_no1_of_branches() != null) {
						R16cell1.setCellValue(record.getR16_no1_of_branches().doubleValue());
						R16cell1.setCellStyle(numberStyle);
					} else {
						R16cell1.setCellValue("");
						R16cell1.setCellStyle(textStyle);
					}

					// R16 Col F
					Cell R16cell2 = row.createCell(5);
					if (record.getR16_no1_of_sub_branches() != null) {
						R16cell2.setCellValue(record.getR16_no1_of_sub_branches().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}

					// R16 Col G
					Cell R16cell3 = row.createCell(6);
					if (record.getR16_no1_of_agencies() != null) {
						R16cell3.setCellValue(record.getR16_no1_of_agencies().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}
					// R17 Col E
					row = sheet.getRow(16);
					Cell R17cell1 = row.createCell(4);
					if (record.getR17_no1_of_branches() != null) {
						R17cell1.setCellValue(record.getR17_no1_of_branches().doubleValue());
						R17cell1.setCellStyle(numberStyle);
					} else {
						R17cell1.setCellValue("");
						R17cell1.setCellStyle(textStyle);
					}

					// R17 Col F
					Cell R17cell2 = row.createCell(5);
					if (record.getR17_no1_of_sub_branches() != null) {
						R17cell2.setCellValue(record.getR17_no1_of_sub_branches().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// R17 Col G
					Cell R17cell3 = row.createCell(6);
					if (record.getR17_no1_of_agencies() != null) {
						R17cell3.setCellValue(record.getR17_no1_of_agencies().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}
					// R18 Col E
					row = sheet.getRow(17);
					Cell R18cell1 = row.createCell(4);
					if (record.getR18_no1_of_branches() != null) {
						R18cell1.setCellValue(record.getR18_no1_of_branches().doubleValue());
						R18cell1.setCellStyle(numberStyle);
					} else {
						R18cell1.setCellValue("");
						R18cell1.setCellStyle(textStyle);
					}

					// R18 Col F
					Cell R18cell2 = row.createCell(5);
					if (record.getR18_no1_of_sub_branches() != null) {
						R18cell2.setCellValue(record.getR18_no1_of_sub_branches().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					// R18 Col G
					Cell R18cell3 = row.createCell(6);
					if (record.getR18_no1_of_agencies() != null) {
						R18cell3.setCellValue(record.getR18_no1_of_agencies().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// R19 Col E
					row = sheet.getRow(18);
					Cell R19cell1 = row.createCell(4);
					if (record.getR19_no1_of_branches() != null) {
						R19cell1.setCellValue(record.getR19_no1_of_branches().doubleValue());
						R19cell1.setCellStyle(numberStyle);
					} else {
						R19cell1.setCellValue("");
						R19cell1.setCellStyle(textStyle);
					}

					// R19 Col F
					Cell R19cell2 = row.createCell(5);
					if (record.getR19_no1_of_sub_branches() != null) {
						R19cell2.setCellValue(record.getR19_no1_of_sub_branches().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					// R19 Col G
					Cell R19cell3 = row.createCell(6);
					if (record.getR19_no1_of_agencies() != null) {
						R19cell3.setCellValue(record.getR19_no1_of_agencies().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}
					// R20 Col E
					row = sheet.getRow(19);
					Cell R20cell1 = row.createCell(4);
					if (record.getR20_no1_of_branches() != null) {
						R20cell1.setCellValue(record.getR20_no1_of_branches().doubleValue());
						R20cell1.setCellStyle(numberStyle);
					} else {
						R20cell1.setCellValue("");
						R20cell1.setCellStyle(textStyle);
					}

					// R20 Col F
					Cell R20cell2 = row.createCell(5);
					if (record.getR20_no1_of_sub_branches() != null) {
						R20cell2.setCellValue(record.getR20_no1_of_sub_branches().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					// R20 Col G
					Cell R20cell3 = row.createCell(6);
					if (record.getR20_no1_of_agencies() != null) {
						R20cell3.setCellValue(record.getR20_no1_of_agencies().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}
					// TABLE 2
					// R25 Col E
					row = sheet.getRow(24);
					Cell R25cell1 = row.createCell(4);
					if (record.getR25_no_of_atms() != null) {
						R25cell1.setCellValue(record.getR25_no_of_atms().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col F
					Cell R25cell2 = row.createCell(5);
					if (record.getR25_no_of_mini_atms() != null) {
						R25cell2.setCellValue(record.getR25_no_of_mini_atms().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					// R25 Col G
					Cell R25cell3 = row.createCell(6);
					if (record.getR25_encashment_points() != null) {
						R25cell3.setCellValue(record.getR25_encashment_points().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					// R26 Col E
					row = sheet.getRow(25);
					Cell R26cell1 = row.createCell(4);
					if (record.getR26_no_of_atms() != null) {
						R26cell1.setCellValue(record.getR26_no_of_atms().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col F
					Cell R26cell2 = row.createCell(5);
					if (record.getR26_no_of_mini_atms() != null) {
						R26cell2.setCellValue(record.getR26_no_of_mini_atms().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					// R26 Col G
					Cell R26cell3 = row.createCell(6);
					if (record.getR26_encashment_points() != null) {
						R26cell3.setCellValue(record.getR26_encashment_points().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}
					// R27 Col E
					row = sheet.getRow(26);
					Cell R27cell1 = row.createCell(4);
					if (record.getR27_no_of_atms() != null) {
						R27cell1.setCellValue(record.getR27_no_of_atms().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col F
					Cell R27cell2 = row.createCell(5);
					if (record.getR27_no_of_mini_atms() != null) {
						R27cell2.setCellValue(record.getR27_no_of_mini_atms().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					// R27 Col G
					Cell R27cell3 = row.createCell(6);
					if (record.getR27_encashment_points() != null) {
						R27cell3.setCellValue(record.getR27_encashment_points().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}
					// R28 Col E
					row = sheet.getRow(27);
					Cell R28cell1 = row.createCell(4);
					if (record.getR28_no_of_atms() != null) {
						R28cell1.setCellValue(record.getR28_no_of_atms().doubleValue());
						R28cell1.setCellStyle(numberStyle);
					} else {
						R28cell1.setCellValue("");
						R28cell1.setCellStyle(textStyle);
					}

					// R28 Col F
					Cell R28cell2 = row.createCell(5);
					if (record.getR28_no_of_mini_atms() != null) {
						R28cell2.setCellValue(record.getR28_no_of_mini_atms().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}

					// R28 Col G
					Cell R28cell3 = row.createCell(6);
					if (record.getR28_encashment_points() != null) {
						R28cell3.setCellValue(record.getR28_encashment_points().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}
					// R29 Col E
					row = sheet.getRow(28);
					Cell R29cell1 = row.createCell(4);
					if (record.getR29_no_of_atms() != null) {
						R29cell1.setCellValue(record.getR29_no_of_atms().doubleValue());
						R29cell1.setCellStyle(numberStyle);
					} else {
						R29cell1.setCellValue("");
						R29cell1.setCellStyle(textStyle);
					}

					// R29 Col F
					Cell R29cell2 = row.createCell(5);
					if (record.getR29_no_of_mini_atms() != null) {
						R29cell2.setCellValue(record.getR29_no_of_mini_atms().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					// R29 Col G
					Cell R29cell3 = row.createCell(6);
					if (record.getR29_encashment_points() != null) {
						R29cell3.setCellValue(record.getR29_encashment_points().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}
					// R30 Col E
					row = sheet.getRow(29);
					Cell R30cell1 = row.createCell(4);
					if (record.getR30_no_of_atms() != null) {
						R30cell1.setCellValue(record.getR30_no_of_atms().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

					// R30 Col F
					Cell R30cell2 = row.createCell(5);
					if (record.getR30_no_of_mini_atms() != null) {
						R30cell2.setCellValue(record.getR30_no_of_mini_atms().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					// R30 Col G
					Cell R30cell3 = row.createCell(6);
					if (record.getR30_encashment_points() != null) {
						R30cell3.setCellValue(record.getR30_encashment_points().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}
					// R31 Col E
					row = sheet.getRow(30);
					Cell R31cell1 = row.createCell(4);
					if (record.getR31_no_of_atms() != null) {
						R31cell1.setCellValue(record.getR31_no_of_atms().doubleValue());
						R31cell1.setCellStyle(numberStyle);
					} else {
						R31cell1.setCellValue("");
						R31cell1.setCellStyle(textStyle);
					}

					// R31 Col F
					Cell R31cell2 = row.createCell(5);
					if (record.getR31_no_of_mini_atms() != null) {
						R31cell2.setCellValue(record.getR31_no_of_mini_atms().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					// R31 Col G
					Cell R31cell3 = row.createCell(6);
					if (record.getR31_encashment_points() != null) {
						R31cell3.setCellValue(record.getR31_encashment_points().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// R32 Col E
					row = sheet.getRow(31);
					Cell R32cell1 = row.createCell(4);
					if (record.getR32_no_of_atms() != null) {
						R32cell1.setCellValue(record.getR32_no_of_atms().doubleValue());
						R32cell1.setCellStyle(numberStyle);
					} else {
						R32cell1.setCellValue("");
						R32cell1.setCellStyle(textStyle);
					}

					// R32 Col F
					Cell R32cell2 = row.createCell(5);
					if (record.getR32_no_of_mini_atms() != null) {
						R32cell2.setCellValue(record.getR32_no_of_mini_atms().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}

					// R32 Col G
					Cell R32cell3 = row.createCell(6);
					if (record.getR32_encashment_points() != null) {
						R32cell3.setCellValue(record.getR32_encashment_points().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}
					// R33 Col E
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(4);
					if (record.getR33_no_of_atms() != null) {
						R33cell1.setCellValue(record.getR33_no_of_atms().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col F
					Cell R33cell2 = row.createCell(5);
					if (record.getR33_no_of_mini_atms() != null) {
						R33cell2.setCellValue(record.getR33_no_of_mini_atms().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					// R33 Col G
					Cell R33cell3 = row.createCell(6);
					if (record.getR33_encashment_points() != null) {
						R33cell3.setCellValue(record.getR33_encashment_points().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}
					// R34 Col E
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(4);
					if (record.getR34_no_of_atms() != null) {
						R34cell1.setCellValue(record.getR34_no_of_atms().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col F
					Cell R34cell2 = row.createCell(5);
					if (record.getR34_no_of_mini_atms() != null) {
						R34cell2.setCellValue(record.getR34_no_of_mini_atms().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					// R34 Col G
					Cell R34cell3 = row.createCell(6);
					if (record.getR34_encashment_points() != null) {
						R34cell3.setCellValue(record.getR34_encashment_points().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// R35 Col E
					row = sheet.getRow(34);
					Cell R35cell1 = row.createCell(4);
					if (record.getR35_no_of_atms() != null) {
						R35cell1.setCellValue(record.getR35_no_of_atms().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col F
					Cell R35cell2 = row.createCell(5);
					if (record.getR35_no_of_mini_atms() != null) {
						R35cell2.setCellValue(record.getR35_no_of_mini_atms().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					// R35 Col G
					Cell R35cell3 = row.createCell(6);
					if (record.getR35_encashment_points() != null) {
						R35cell3.setCellValue(record.getR35_encashment_points().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}
					// TABLE 3
					// R40 Col E
					row = sheet.getRow(39);
					Cell R40cell1 = row.createCell(4);
					if (record.getR40_opening_no_of_cards() != null) {
						R40cell1.setCellValue(record.getR40_opening_no_of_cards().doubleValue());
						R40cell1.setCellStyle(numberStyle);
					} else {
						R40cell1.setCellValue("");
						R40cell1.setCellStyle(textStyle);
					}

					// R40 Col F
					Cell R40cell2 = row.createCell(5);
					if (record.getR40_no_of_cards_issued() != null) {
						R40cell2.setCellValue(record.getR40_no_of_cards_issued().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					// R40 Col G
					Cell R40cell3 = row.createCell(6);
					if (record.getR40_no_cards_of_closed() != null) {
						R40cell3.setCellValue(record.getR40_no_cards_of_closed().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}
					// R40 Col H
					Cell R40cell4 = row.createCell(7);
					if (record.getR40_closing_bal_of_active_cards() != null) {
						R40cell4.setCellValue(record.getR40_closing_bal_of_active_cards().doubleValue());
						R40cell4.setCellStyle(numberStyle);
					} else {
						R40cell4.setCellValue("");
						R40cell4.setCellStyle(textStyle);
					}

					// R41 Col E
					row = sheet.getRow(40);
					Cell R41cell1 = row.createCell(4);
					if (record.getR41_opening_no_of_cards() != null) {
						R41cell1.setCellValue(record.getR41_opening_no_of_cards().doubleValue());
						R41cell1.setCellStyle(numberStyle);
					} else {
						R41cell1.setCellValue("");
						R41cell1.setCellStyle(textStyle);
					}

					// R41 Col F
					Cell R41cell2 = row.createCell(5);
					if (record.getR41_no_of_cards_issued() != null) {
						R41cell2.setCellValue(record.getR41_no_of_cards_issued().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					// R41 Col G
					Cell R41cell3 = row.createCell(6);
					if (record.getR41_no_cards_of_closed() != null) {
						R41cell3.setCellValue(record.getR41_no_cards_of_closed().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}
					// R41 Col H
					Cell R41cell4 = row.createCell(7);
					if (record.getR41_closing_bal_of_active_cards() != null) {
						R41cell4.setCellValue(record.getR41_closing_bal_of_active_cards().doubleValue());
						R41cell4.setCellStyle(numberStyle);
					} else {
						R41cell4.setCellValue("");
						R41cell4.setCellStyle(textStyle);
					}
					// R42 Col E
					row = sheet.getRow(41);
					Cell R42cell1 = row.createCell(4);
					if (record.getR42_opening_no_of_cards() != null) {
						R42cell1.setCellValue(record.getR42_opening_no_of_cards().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

					// R42 Col F
					Cell R42cell2 = row.createCell(5);
					if (record.getR42_no_of_cards_issued() != null) {
						R42cell2.setCellValue(record.getR42_no_of_cards_issued().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					// R42 Col G
					Cell R42cell3 = row.createCell(6);
					if (record.getR42_no_cards_of_closed() != null) {
						R42cell3.setCellValue(record.getR42_no_cards_of_closed().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// R42 Col H
					Cell R42cell4 = row.createCell(7);
					if (record.getR42_closing_bal_of_active_cards() != null) {
						R42cell4.setCellValue(record.getR42_closing_bal_of_active_cards().doubleValue());
						R42cell4.setCellStyle(numberStyle);
					} else {
						R42cell4.setCellValue("");
						R42cell4.setCellStyle(textStyle);
					}
					// R43 Col E
					row = sheet.getRow(42);
					Cell R43cell1 = row.createCell(4);
					if (record.getR43_opening_no_of_cards() != null) {
						R43cell1.setCellValue(record.getR43_opening_no_of_cards().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

					// R43 Col F
					Cell R43cell2 = row.createCell(5);
					if (record.getR43_no_of_cards_issued() != null) {
						R43cell2.setCellValue(record.getR43_no_of_cards_issued().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					// R43 Col G
					Cell R43cell3 = row.createCell(6);
					if (record.getR43_no_cards_of_closed() != null) {
						R43cell3.setCellValue(record.getR43_no_cards_of_closed().doubleValue());
						R43cell3.setCellStyle(numberStyle);
					} else {
						R43cell3.setCellValue("");
						R43cell3.setCellStyle(textStyle);
					}
					// R43 Col H
					Cell R43cell4 = row.createCell(7);
					if (record.getR43_closing_bal_of_active_cards() != null) {
						R43cell4.setCellValue(record.getR43_closing_bal_of_active_cards().doubleValue());
						R43cell4.setCellStyle(numberStyle);
					} else {
						R43cell4.setCellValue("");
						R43cell4.setCellStyle(textStyle);
					}

					// R44 Col E
					row = sheet.getRow(43);
					Cell R44cell1 = row.createCell(4);
					if (record.getR44_opening_no_of_cards() != null) {
						R44cell1.setCellValue(record.getR44_opening_no_of_cards().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

					// R44 Col F
					Cell R44cell2 = row.createCell(5);
					if (record.getR44_no_of_cards_issued() != null) {
						R44cell2.setCellValue(record.getR44_no_of_cards_issued().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					// R44 Col G
					Cell R44cell3 = row.createCell(6);
					if (record.getR44_no_cards_of_closed() != null) {
						R44cell3.setCellValue(record.getR44_no_cards_of_closed().doubleValue());
						R44cell3.setCellStyle(numberStyle);
					} else {
						R44cell3.setCellValue("");
						R44cell3.setCellStyle(textStyle);
					}
					// R44 Col H
					Cell R44cell4 = row.createCell(7);
					if (record.getR44_closing_bal_of_active_cards() != null) {
						R44cell4.setCellValue(record.getR44_closing_bal_of_active_cards().doubleValue());
						R44cell4.setCellStyle(numberStyle);
					} else {
						R44cell4.setCellValue("");
						R44cell4.setCellStyle(textStyle);
					}
					// R45 Col E
					row = sheet.getRow(44);
					Cell R45cell1 = row.createCell(4);
					if (record.getR45_opening_no_of_cards() != null) {
						R45cell1.setCellValue(record.getR45_opening_no_of_cards().doubleValue());
						R45cell1.setCellStyle(numberStyle);
					} else {
						R45cell1.setCellValue("");
						R45cell1.setCellStyle(textStyle);
					}

					// R45 Col F
					Cell R45cell2 = row.createCell(5);
					if (record.getR45_no_of_cards_issued() != null) {
						R45cell2.setCellValue(record.getR45_no_of_cards_issued().doubleValue());
						R45cell2.setCellStyle(numberStyle);
					} else {
						R45cell2.setCellValue("");
						R45cell2.setCellStyle(textStyle);
					}

					// R45 Col G
					Cell R45cell3 = row.createCell(6);
					if (record.getR45_no_cards_of_closed() != null) {
						R45cell3.setCellValue(record.getR45_no_cards_of_closed().doubleValue());
						R45cell3.setCellStyle(numberStyle);
					} else {
						R45cell3.setCellValue("");
						R45cell3.setCellStyle(textStyle);
					}
					// R45 Col H
					Cell R45cell4 = row.createCell(7);
					if (record.getR45_closing_bal_of_active_cards() != null) {
						R45cell4.setCellValue(record.getR45_closing_bal_of_active_cards().doubleValue());
						R45cell4.setCellStyle(numberStyle);
					} else {
						R45cell4.setCellValue("");
						R45cell4.setCellStyle(textStyle);
					}
					// R46 Col E
					row = sheet.getRow(45);
					Cell R46cell1 = row.createCell(4);
					if (record.getR46_opening_no_of_cards() != null) {
						R46cell1.setCellValue(record.getR46_opening_no_of_cards().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

					// R46 Col F
					Cell R46cell2 = row.createCell(5);
					if (record.getR46_no_of_cards_issued() != null) {
						R46cell2.setCellValue(record.getR46_no_of_cards_issued().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
					}

					// R46 Col G
					Cell R46cell3 = row.createCell(6);
					if (record.getR46_no_cards_of_closed() != null) {
						R46cell3.setCellValue(record.getR46_no_cards_of_closed().doubleValue());
						R46cell3.setCellStyle(numberStyle);
					} else {
						R46cell3.setCellValue("");
						R46cell3.setCellStyle(textStyle);
					}
					// R46 Col H
					Cell R46cell4 = row.createCell(7);
					if (record.getR46_closing_bal_of_active_cards() != null) {
						R46cell4.setCellValue(record.getR46_closing_bal_of_active_cards().doubleValue());
						R46cell4.setCellStyle(numberStyle);
					} else {
						R46cell4.setCellValue("");
						R46cell4.setCellStyle(textStyle);
					}
					// R47 Col E
					row = sheet.getRow(46);
					Cell R47cell1 = row.createCell(4);
					if (record.getR47_opening_no_of_cards() != null) {
						R47cell1.setCellValue(record.getR47_opening_no_of_cards().doubleValue());
						R47cell1.setCellStyle(numberStyle);
					} else {
						R47cell1.setCellValue("");
						R47cell1.setCellStyle(textStyle);
					}

					// R47 Col F
					Cell R47cell2 = row.createCell(5);
					if (record.getR47_no_of_cards_issued() != null) {
						R47cell2.setCellValue(record.getR47_no_of_cards_issued().doubleValue());
						R47cell2.setCellStyle(numberStyle);
					} else {
						R47cell2.setCellValue("");
						R47cell2.setCellStyle(textStyle);
					}

					// R47 Col G
					Cell R47cell3 = row.createCell(6);
					if (record.getR47_no_cards_of_closed() != null) {
						R47cell3.setCellValue(record.getR47_no_cards_of_closed().doubleValue());
						R47cell3.setCellStyle(numberStyle);
					} else {
						R47cell3.setCellValue("");
						R47cell3.setCellStyle(textStyle);
					}

					// R48 Col E
					row = sheet.getRow(47);
					Cell R48cell1 = row.createCell(4);
					if (record.getR48_opening_no_of_cards() != null) {
						R48cell1.setCellValue(record.getR48_opening_no_of_cards().doubleValue());
						R48cell1.setCellStyle(numberStyle);
					} else {
						R48cell1.setCellValue("");
						R48cell1.setCellStyle(textStyle);
					}

					// R48 Col F
					Cell R48cell2 = row.createCell(5);
					if (record.getR48_no_of_cards_issued() != null) {
						R48cell2.setCellValue(record.getR48_no_of_cards_issued().doubleValue());
						R48cell2.setCellStyle(numberStyle);
					} else {
						R48cell2.setCellValue("");
						R48cell2.setCellStyle(textStyle);
					}

					// R48 Col G
					Cell R48cell3 = row.createCell(6);
					if (record.getR48_no_cards_of_closed() != null) {
						R48cell3.setCellValue(record.getR48_no_cards_of_closed().doubleValue());
						R48cell3.setCellStyle(numberStyle);
					} else {
						R48cell3.setCellValue("");
						R48cell3.setCellStyle(textStyle);
					}

					// R49 Col E
					row = sheet.getRow(48);
					Cell R49cell1 = row.createCell(4);
					if (record.getR49_opening_no_of_cards() != null) {
						R49cell1.setCellValue(record.getR49_opening_no_of_cards().doubleValue());
						R49cell1.setCellStyle(numberStyle);
					} else {
						R49cell1.setCellValue("");
						R49cell1.setCellStyle(textStyle);
					}

					// R49 Col F
					Cell R49cell2 = row.createCell(5);
					if (record.getR49_no_of_cards_issued() != null) {
						R49cell2.setCellValue(record.getR49_no_of_cards_issued().doubleValue());
						R49cell2.setCellStyle(numberStyle);
					} else {
						R49cell2.setCellValue("");
						R49cell2.setCellStyle(textStyle);
					}

					// R49 Col G
					Cell R49cell3 = row.createCell(6);
					if (record.getR49_no_cards_of_closed() != null) {
						R49cell3.setCellValue(record.getR49_no_cards_of_closed().doubleValue());
						R49cell3.setCellStyle(numberStyle);
					} else {
						R49cell3.setCellValue("");
						R49cell3.setCellStyle(textStyle);
					}

					// TABLE 4
					// R55 Col E
					row = sheet.getRow(54);
					Cell R55cell1 = row.createCell(4);
					if (record.getR55_opening_no_of_cards() != null) {
						R55cell1.setCellValue(record.getR55_opening_no_of_cards().doubleValue());
						R55cell1.setCellStyle(numberStyle);
					} else {
						R55cell1.setCellValue("");
						R55cell1.setCellStyle(textStyle);
					}

					// R55 Col F
					Cell R55cell2 = row.createCell(5);
					if (record.getR55_no_of_cards_issued() != null) {
						R55cell2.setCellValue(record.getR55_no_of_cards_issued().doubleValue());
						R55cell2.setCellStyle(numberStyle);
					} else {
						R55cell2.setCellValue("");
						R55cell2.setCellStyle(textStyle);
					}

					// R55 Col G
					Cell R55cell3 = row.createCell(6);
					if (record.getR55_no_cards_of_closed() != null) {
						R55cell3.setCellValue(record.getR55_no_cards_of_closed().doubleValue());
						R55cell3.setCellStyle(numberStyle);
					} else {
						R55cell3.setCellValue("");
						R55cell3.setCellStyle(textStyle);
					}
					// R55 Col H
					Cell R55cell4 = row.createCell(7);
					if (record.getR55_closing_bal_of_active_cards() != null) {
						R55cell4.setCellValue(record.getR55_closing_bal_of_active_cards().doubleValue());
						R55cell4.setCellStyle(numberStyle);
					} else {
						R55cell4.setCellValue("");
						R55cell4.setCellStyle(textStyle);
					}
					// R56 Col E
					row = sheet.getRow(55);
					Cell R56cell1 = row.createCell(4);
					if (record.getR56_opening_no_of_cards() != null) {
						R56cell1.setCellValue(record.getR56_opening_no_of_cards().doubleValue());
						R56cell1.setCellStyle(numberStyle);
					} else {
						R56cell1.setCellValue("");
						R56cell1.setCellStyle(textStyle);
					}

					// R56 Col F
					Cell R56cell2 = row.createCell(5);
					if (record.getR56_no_of_cards_issued() != null) {
						R56cell2.setCellValue(record.getR56_no_of_cards_issued().doubleValue());
						R56cell2.setCellStyle(numberStyle);
					} else {
						R56cell2.setCellValue("");
						R56cell2.setCellStyle(textStyle);
					}

					// R56 Col G
					Cell R56cell3 = row.createCell(6);
					if (record.getR56_no_cards_of_closed() != null) {
						R56cell3.setCellValue(record.getR56_no_cards_of_closed().doubleValue());
						R56cell3.setCellStyle(numberStyle);
					} else {
						R56cell3.setCellValue("");
						R56cell3.setCellStyle(textStyle);
					}
					// R56 Col H
					Cell R56cell4 = row.createCell(7);
					if (record.getR56_closing_bal_of_active_cards() != null) {
						R56cell4.setCellValue(record.getR56_closing_bal_of_active_cards().doubleValue());
						R56cell4.setCellStyle(numberStyle);
					} else {
						R56cell4.setCellValue("");
						R56cell4.setCellStyle(textStyle);
					}
					// R57 Col E
					row = sheet.getRow(56);
					Cell R57cell1 = row.createCell(4);
					if (record.getR57_opening_no_of_cards() != null) {
						R57cell1.setCellValue(record.getR57_opening_no_of_cards().doubleValue());
						R57cell1.setCellStyle(numberStyle);
					} else {
						R57cell1.setCellValue("");
						R57cell1.setCellStyle(textStyle);
					}

					// R57 Col F
					Cell R57cell2 = row.createCell(5);
					if (record.getR57_no_of_cards_issued() != null) {
						R57cell2.setCellValue(record.getR57_no_of_cards_issued().doubleValue());
						R57cell2.setCellStyle(numberStyle);
					} else {
						R57cell2.setCellValue("");
						R57cell2.setCellStyle(textStyle);
					}

					// R57 Col G
					Cell R57cell3 = row.createCell(6);
					if (record.getR57_no_cards_of_closed() != null) {
						R57cell3.setCellValue(record.getR57_no_cards_of_closed().doubleValue());
						R57cell3.setCellStyle(numberStyle);
					} else {
						R57cell3.setCellValue("");
						R57cell3.setCellStyle(textStyle);
					}
					// R57 Col H
					Cell R57cell4 = row.createCell(7);
					if (record.getR57_closing_bal_of_active_cards() != null) {
						R57cell4.setCellValue(record.getR57_closing_bal_of_active_cards().doubleValue());
						R57cell4.setCellStyle(numberStyle);
					} else {
						R57cell4.setCellValue("");
						R57cell4.setCellStyle(textStyle);
					}
					// R58 Col E
					row = sheet.getRow(57);
					Cell R58cell1 = row.createCell(4);
					if (record.getR58_opening_no_of_cards() != null) {
						R58cell1.setCellValue(record.getR58_opening_no_of_cards().doubleValue());
						R58cell1.setCellStyle(numberStyle);
					} else {
						R58cell1.setCellValue("");
						R58cell1.setCellStyle(textStyle);
					}

					// R58 Col F
					Cell R58cell2 = row.createCell(5);
					if (record.getR58_no_of_cards_issued() != null) {
						R58cell2.setCellValue(record.getR58_no_of_cards_issued().doubleValue());
						R58cell2.setCellStyle(numberStyle);
					} else {
						R58cell2.setCellValue("");
						R58cell2.setCellStyle(textStyle);
					}

					// R58 Col G
					Cell R58cell3 = row.createCell(6);
					if (record.getR58_no_cards_of_closed() != null) {
						R58cell3.setCellValue(record.getR58_no_cards_of_closed().doubleValue());
						R58cell3.setCellStyle(numberStyle);
					} else {
						R58cell3.setCellValue("");
						R58cell3.setCellStyle(textStyle);
					}
					// R58 Col H
					Cell R58cell4 = row.createCell(7);
					if (record.getR58_closing_bal_of_active_cards() != null) {
						R58cell4.setCellValue(record.getR58_closing_bal_of_active_cards().doubleValue());
						R58cell4.setCellStyle(numberStyle);
					} else {
						R58cell4.setCellValue("");
						R58cell4.setCellStyle(textStyle);
					}
					// // R59 Col E
					// row = sheet.getRow(58);
					// Cell R59cell1 = row.createCell(4);
					// if (record.getR59_opening_no_of_cards() != null) {
					// R59cell1.setCellValue(record.getR59_opening_no_of_cards().doubleValue());
					// R59cell1.setCellStyle(numberStyle);
					// } else {
					// R59cell1.setCellValue("");
					// R59cell1.setCellStyle(textStyle);
					// }

					// // R59 Col F
					// Cell R59cell2 = row.createCell(5);
					// if (record.getR59_no_of_cards_issued() != null) {
					// R59cell2.setCellValue(record.getR59_no_of_cards_issued().doubleValue());
					// R59cell2.setCellStyle(numberStyle);
					// } else {
					// R59cell2.setCellValue("");
					// R59cell2.setCellStyle(textStyle);
					// }

					// // R59 Col G
					// Cell R59cell3 = row.createCell(6);
					// if (record.getR59_no_cards_of_closed() != null) {
					// R59cell3.setCellValue(record.getR59_no_cards_of_closed().doubleValue());
					// R59cell3.setCellStyle(numberStyle);
					// } else {
					// R59cell3.setCellValue("");
					// R59cell3.setCellStyle(textStyle);
					// }
					// // R59 Col H
					// Cell R59cell4 = row.createCell(7);
					// if (record.getR59_closing_bal_of_active_cards() != null) {
					// R59cell4.setCellValue(record.getR59_closing_bal_of_active_cards().doubleValue());
					// R59cell4.setCellStyle(numberStyle);
					// } else {
					// R59cell4.setCellValue("");
					// R59cell4.setCellStyle(textStyle);
					// }

					// R60 Col E
					row = sheet.getRow(59);
					Cell R60cell1 = row.createCell(4);
					if (record.getR60_opening_no_of_cards() != null) {
						R60cell1.setCellValue(record.getR60_opening_no_of_cards().doubleValue());
						R60cell1.setCellStyle(numberStyle);
					} else {
						R60cell1.setCellValue("");
						R60cell1.setCellStyle(textStyle);
					}

					// R60 Col F
					Cell R60cell2 = row.createCell(5);
					if (record.getR60_no_of_cards_issued() != null) {
						R60cell2.setCellValue(record.getR60_no_of_cards_issued().doubleValue());
						R60cell2.setCellStyle(numberStyle);
					} else {
						R60cell2.setCellValue("");
						R60cell2.setCellStyle(textStyle);
					}
					// R60 Col G
					Cell R60cell3 = row.createCell(6);
					if (record.getR60_no_cards_of_closed() != null) {
						R60cell3.setCellValue(record.getR60_no_cards_of_closed().doubleValue());
						R60cell3.setCellStyle(numberStyle);
					} else {
						R60cell3.setCellValue("");
						R60cell3.setCellStyle(textStyle);
					}
					// R60 Col H
					Cell R60cell4 = row.createCell(7);
					if (record.getR60_closing_bal_of_active_cards() != null) {
						R60cell4.setCellValue(record.getR60_closing_bal_of_active_cards().doubleValue());
						R60cell4.setCellStyle(numberStyle);
					} else {
						R60cell4.setCellValue("");
						R60cell4.setCellStyle(textStyle);
					}
					// R61 Col E
					row = sheet.getRow(60);
					Cell R61cell1 = row.createCell(4);
					if (record.getR61_opening_no_of_cards() != null) {
						R61cell1.setCellValue(record.getR61_opening_no_of_cards().doubleValue());
						R61cell1.setCellStyle(numberStyle);
					} else {
						R61cell1.setCellValue("");
						R61cell1.setCellStyle(textStyle);
					}

					// R61 Col F
					Cell R61cell2 = row.createCell(5);
					if (record.getR61_no_of_cards_issued() != null) {
						R61cell2.setCellValue(record.getR61_no_of_cards_issued().doubleValue());
						R61cell2.setCellStyle(numberStyle);
					} else {
						R61cell2.setCellValue("");
						R61cell2.setCellStyle(textStyle);
					}
					// R61 Col G
					Cell R61cell3 = row.createCell(6);
					if (record.getR61_no_cards_of_closed() != null) {
						R61cell3.setCellValue(record.getR61_no_cards_of_closed().doubleValue());
						R61cell3.setCellStyle(numberStyle);
					} else {
						R61cell3.setCellValue("");
						R61cell3.setCellStyle(textStyle);
					}
					// R61 Col H
					Cell R61cell4 = row.createCell(7);
					if (record.getR61_closing_bal_of_active_cards() != null) {
						R61cell4.setCellValue(record.getR61_closing_bal_of_active_cards().doubleValue());
						R61cell4.setCellStyle(numberStyle);
					} else {
						R61cell4.setCellValue("");
						R61cell4.setCellStyle(textStyle);
					}
					// R62 Col E
					row = sheet.getRow(61);
					Cell R62cell1 = row.createCell(4);
					if (record.getR62_opening_no_of_cards() != null) {
						R62cell1.setCellValue(record.getR62_opening_no_of_cards().doubleValue());
						R62cell1.setCellStyle(numberStyle);
					} else {
						R62cell1.setCellValue("");
						R62cell1.setCellStyle(textStyle);
					}

					// R62 Col F
					Cell R62cell2 = row.createCell(5);
					if (record.getR62_no_of_cards_issued() != null) {
						R62cell2.setCellValue(record.getR62_no_of_cards_issued().doubleValue());
						R62cell2.setCellStyle(numberStyle);
					} else {
						R62cell2.setCellValue("");
						R62cell2.setCellStyle(textStyle);
					}
					// R62 Col G
					Cell R62cell3 = row.createCell(6);
					if (record.getR62_no_cards_of_closed() != null) {
						R62cell3.setCellValue(record.getR62_no_cards_of_closed().doubleValue());
						R62cell3.setCellStyle(numberStyle);
					} else {
						R62cell3.setCellValue("");
						R62cell3.setCellStyle(textStyle);
					}
					// R62 Col H
					Cell R62cell4 = row.createCell(7);
					if (record.getR62_closing_bal_of_active_cards() != null) {
						R62cell4.setCellValue(record.getR62_closing_bal_of_active_cards().doubleValue());
						R62cell4.setCellStyle(numberStyle);
					} else {
						R62cell4.setCellValue("");
						R62cell4.setCellStyle(textStyle);
					}
					// R63 Col E
					row = sheet.getRow(62);
					Cell R63cell1 = row.createCell(4);
					if (record.getR63_opening_no_of_cards() != null) {
						R63cell1.setCellValue(record.getR63_opening_no_of_cards().doubleValue());
						R63cell1.setCellStyle(numberStyle);
					} else {
						R63cell1.setCellValue("");
						R63cell1.setCellStyle(textStyle);
					}

					// R63 Col F
					Cell R63cell2 = row.createCell(5);
					if (record.getR63_no_of_cards_issued() != null) {
						R63cell2.setCellValue(record.getR63_no_of_cards_issued().doubleValue());
						R63cell2.setCellStyle(numberStyle);
					} else {
						R63cell2.setCellValue("");
						R63cell2.setCellStyle(textStyle);
					}
					// R63 Col G
					Cell R63cell3 = row.createCell(6);
					if (record.getR63_no_cards_of_closed() != null) {
						R63cell3.setCellValue(record.getR63_no_cards_of_closed().doubleValue());
						R63cell3.setCellStyle(numberStyle);
					} else {
						R63cell3.setCellValue("");
						R63cell3.setCellStyle(textStyle);
					}
					// R63 Col H
					Cell R63cell4 = row.createCell(7);
					if (record.getR63_closing_bal_of_active_cards() != null) {
						R63cell4.setCellValue(record.getR63_closing_bal_of_active_cards().doubleValue());
						R63cell4.setCellStyle(numberStyle);
					} else {
						R63cell4.setCellValue("");
						R63cell4.setCellStyle(textStyle);
					}
					// R64 Col E
					row = sheet.getRow(63);
					Cell R64cell1 = row.createCell(4);
					if (record.getR65_opening_no_of_cards() != null) {
						R64cell1.setCellValue(record.getR65_opening_no_of_cards().doubleValue());
						R64cell1.setCellStyle(numberStyle);
					} else {
						R64cell1.setCellValue("");
						R64cell1.setCellStyle(textStyle);
					}

					// R64 Col F
					Cell R64cell2 = row.createCell(5);
					if (record.getR65_no_of_cards_issued() != null) {
						R64cell2.setCellValue(record.getR65_no_of_cards_issued().doubleValue());
						R64cell2.setCellStyle(numberStyle);
					} else {
						R64cell2.setCellValue("");
						R64cell2.setCellStyle(textStyle);
					}
					// R64 Col G
					Cell R64cell3 = row.createCell(6);
					if (record.getR65_no_cards_of_closed() != null) {
						R64cell3.setCellValue(record.getR65_no_cards_of_closed().doubleValue());
						R64cell3.setCellStyle(numberStyle);
					} else {
						R64cell3.setCellValue("");
						R64cell3.setCellStyle(textStyle);
					}
					// R64 Col H
					Cell R64cell4 = row.createCell(7);
					if (record.getR65_closing_bal_of_active_cards() != null) {
						R64cell4.setCellValue(record.getR65_closing_bal_of_active_cards().doubleValue());
						R64cell4.setCellStyle(numberStyle);
					} else {
						R64cell4.setCellValue("");
						R64cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_BRANCHNET EMAIL RESUB SUMMARY", null,
						"BRRS_Q_BRANCHNET_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

}