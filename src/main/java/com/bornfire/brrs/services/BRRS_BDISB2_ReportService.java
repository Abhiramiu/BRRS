package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service
public class BRRS_BDISB2_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	// SUMMARY
	// Fetch data by report date
	public List<BDISB2_Summary_Entity> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB2_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB2_RowMapper_Summary());
	}

	// ARCHIVAL

	// Fetch data by report date
	public List<BDISB2_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB2_RowMapper_Archival());
	}

	// RESUB

	// Fetch data by report date
	public List<BDISB2_RESUB_Summary_Entity> ResubgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB2_RowMapper_Resub());
	}

	/*
	 * // ARCHIVAL // GET REPORT_DATE + REPORT_VERSION
	 * 
	 * public List<Object[]> getBDISB2Archival() {
	 * 
	 * String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
	 * "FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY" + "ORDER BY REPORT_VERSION";
	 * 
	 * return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
	 * rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") }); }
	 */

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<BDISB2_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB2_RowMapper_Archival());
	}

	// GET RESUB FULL DATA BY DATE + VERSION

	public List<BDISB2_RESUB_Summary_Entity> getdatabydateListresub1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB2_RowMapper_Resub());
	}

	// GET DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB2_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE" + "WHERE REPORT_DATE = ? " + "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB2RowMapper_Detail());
	}

	// GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB2_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new BDISB2RowMapper_ArchivalDetail());
	}

	// GET RESUB DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB2_RESUB_Detail_Entity> getdatabydateListResubDetail1(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB2RowMapper_ResubDetail());
	}

	// GET ALL WITH VERSION

	public List<BDISB2_Archival_Summary_Entity> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new BDISB2_RowMapper_Archival());
	}

	// GET RESUB ALL WITH VERSION

	public List<BDISB2_RESUB_Summary_Entity> ResubgetdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new BDISB2_RowMapper_Resub());
	}

	// GET ARCHIVAL MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY" + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// GET RESUB MAX VERSION BY DATE

	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB2_RESUBTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// DETAIL TABLE 1
	// 1. BY DATE + LABEL + CRITERIA

	public List<BDISB2_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new BDISB2RowMapper_Detail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<BDISB2_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB2RowMapper_Detail());
	}

// 3. PAGINATION

	public List<BDISB2_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB2RowMapper_Detail());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_BDISB2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<BDISB2_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new BDISB2RowMapper_Detail());
	}

// 6. BY ACCOUNT NUMBER

	public BDISB2_Detail_Entity findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB2RowMapper_Detail());
	}

	// ARCHIVALTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<BDISB2_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new BDISB2RowMapper_ArchivalDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<BDISB2_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB2RowMapper_ArchivalDetail());
	}

// 3. PAGINATION

	public List<BDISB2_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset,
			int limit) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new BDISB2RowMapper_ArchivalDetail());
	}

	// 4. COUNT

	public int getArchivalDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<BDISB2_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new BDISB2RowMapper_ArchivalDetail());
	}
// 6. BY ACCOUNT NUMBER

	public BDISB2_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_BDISB2_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB2RowMapper_ArchivalDetail());
	}

	// RESUBTABLE_DETAIL
	// 1. BY DATE + LABEL + CRITERIA

	public List<BDISB2_RESUB_Detail_Entity> findByResubReportDateAndLabelAndCriteria1(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new BDISB2RowMapper_ResubDetail());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<BDISB2_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB2RowMapper_ResubDetail());
	}

	// 3. PAGINATION

	public List<BDISB2_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB2RowMapper_ResubDetail());
	}

	// 4. COUNT

	public int getResubdatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_BDISB2_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<BDISB2_RESUB_Detail_Entity> GetResubDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new BDISB2RowMapper_ResubDetail());
	}
	// 6. BY ACCOUNT NUMBER

	public BDISB2_RESUB_Detail_Entity ResubfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_BDISB2_RESUB_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB2RowMapper_ResubDetail());
	}

	// findSummaryByReportDate

	@Transactional(readOnly = true)
	public BDISB2_Summary_Entity findSummaryByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB2_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<BDISB2_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new BDISB2_RowMapper_Summary());

		return list.isEmpty() ? null : list.get(0);
	}

	@Transactional(readOnly = true)
	public BDISB2_Detail_Entity findDetailByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB2_DETAILTABLE " + "WHERE REPORT_DATE = ?";

		List<BDISB2_Detail_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new BDISB2RowMapper_Detail());

		return list.isEmpty() ? null : list.get(0);
	}

	// ROW MAPPER SUMMARY

	class BDISB2_RowMapper_Summary implements RowMapper<BDISB2_Summary_Entity> {

		@Override
		public BDISB2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			BDISB2_Summary_Entity obj = new BDISB2_Summary_Entity();

			// ===================== R6 =====================
			obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
			obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
			obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
			obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
			obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
			obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
			obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
			obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
			obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
			obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
			obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
			obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
			obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
			obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
			obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

			// ===================== R7 =====================
			obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
			obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
			obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
			obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
			obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
			obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
			obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
			obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
			obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
			obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
			obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
			obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
			obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
			obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
			obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

			// ===================== R8 =====================
			obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
			obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
			obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
			obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
			obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
			obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
			obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
			obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
			obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
			obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
			obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
			obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
			obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
			obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
			obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

			// ===================== R9 =====================
			obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
			obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
			obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
			obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
			obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
			obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
			obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
			obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
			obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
			obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
			obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
			obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
			obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
			obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
			obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

			// ===================== R10 =====================
			obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
			obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
			obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
			obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
			obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
			obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
			obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
			obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
			obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
			obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
			obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
			obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
			obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
			obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
			obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

			// ===================== R11 =====================
			obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
			obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
			obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
			obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
			obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
			obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
			obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
			obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
			obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
			obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
			obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
			obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
			obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
			obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
			obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

			// ===================== R12 =====================
			obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
			obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
			obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
			obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
			obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
			obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
			obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
			obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
			obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
			obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
			obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
			obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
			obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
					rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
			obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
			obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
			obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
			obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

			// COMMON FIELDS
			obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
			obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
			obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
			obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
			obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
			obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
			obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
			obj.setDEL_FLG(rs.getString("DEL_FLG"));

			return obj;
		}
	}

	public static class BDISB2_Summary_Entity {
				
					private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R6_COMPANY_NAME;
					private String R6_COMPANY_REG_NUM;
					private String R6_BUSINEES_PHY_ADDRESS;
					private String R6_POSTAL_ADDRESS;
					private String R6_COUNTRY_OF_REG;
					private String R6_COMPANY_EMAIL;
					private String R6_COMPANY_LANDLINE;
					private String R6_COMPANY_MOB_PHONE_NUM;
					private String R6_PRODUCT_TYPE;
					private BigDecimal R6_ACCT_NUM;
					private String R6_STATUS_OF_ACCT;
					private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R6_ACCT_BRANCH;
					private BigDecimal R6_ACCT_BALANCE_PULA;
					private String R6_CURRENCY_OF_ACCT;
					private BigDecimal R6_EXCHANGE_RATE;
					private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R7_COMPANY_NAME;
					private String R7_COMPANY_REG_NUM;
					private String R7_BUSINEES_PHY_ADDRESS;
					private String R7_POSTAL_ADDRESS;
					private String R7_COUNTRY_OF_REG;
					private String R7_COMPANY_EMAIL;
					private String R7_COMPANY_LANDLINE;
					private String R7_COMPANY_MOB_PHONE_NUM;
					private String R7_PRODUCT_TYPE;
					private BigDecimal R7_ACCT_NUM;
					private String R7_STATUS_OF_ACCT;
					private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R7_ACCT_BRANCH;
					private BigDecimal R7_ACCT_BALANCE_PULA;
					private String R7_CURRENCY_OF_ACCT;
					private BigDecimal R7_EXCHANGE_RATE;
					private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R8_COMPANY_NAME;
					private String R8_COMPANY_REG_NUM;
					private String R8_BUSINEES_PHY_ADDRESS;
					private String R8_POSTAL_ADDRESS;
					private String R8_COUNTRY_OF_REG;
					private String R8_COMPANY_EMAIL;
					private String R8_COMPANY_LANDLINE;
					private String R8_COMPANY_MOB_PHONE_NUM;
					private String R8_PRODUCT_TYPE;
					private BigDecimal R8_ACCT_NUM;
					private String R8_STATUS_OF_ACCT;
					private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R8_ACCT_BRANCH;
					private BigDecimal R8_ACCT_BALANCE_PULA;
					private String R8_CURRENCY_OF_ACCT;
					private BigDecimal R8_EXCHANGE_RATE;
					private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R9_COMPANY_NAME;
					private String R9_COMPANY_REG_NUM;
					private String R9_BUSINEES_PHY_ADDRESS;
					private String R9_POSTAL_ADDRESS;
					private String R9_COUNTRY_OF_REG;
					private String R9_COMPANY_EMAIL;
					private String R9_COMPANY_LANDLINE;
					private String R9_COMPANY_MOB_PHONE_NUM;
					private String R9_PRODUCT_TYPE;
					private BigDecimal R9_ACCT_NUM;
					private String R9_STATUS_OF_ACCT;
					private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R9_ACCT_BRANCH;
					private BigDecimal R9_ACCT_BALANCE_PULA;
					private String R9_CURRENCY_OF_ACCT;
					private BigDecimal R9_EXCHANGE_RATE;
					private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R10_COMPANY_NAME;
					private String R10_COMPANY_REG_NUM;
					private String R10_BUSINEES_PHY_ADDRESS;
					private String R10_POSTAL_ADDRESS;
					private String R10_COUNTRY_OF_REG;
					private String R10_COMPANY_EMAIL;
					private String R10_COMPANY_LANDLINE;
					private String R10_COMPANY_MOB_PHONE_NUM;
					private String R10_PRODUCT_TYPE;
					private BigDecimal R10_ACCT_NUM;
					private String R10_STATUS_OF_ACCT;
					private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R10_ACCT_BRANCH;
					private BigDecimal R10_ACCT_BALANCE_PULA;
					private String R10_CURRENCY_OF_ACCT;
					private BigDecimal R10_EXCHANGE_RATE;
					private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R11_COMPANY_NAME;
					private String R11_COMPANY_REG_NUM;
					private String R11_BUSINEES_PHY_ADDRESS;
					private String R11_POSTAL_ADDRESS;
					private String R11_COUNTRY_OF_REG;
					private String R11_COMPANY_EMAIL;
					private String R11_COMPANY_LANDLINE;
					private String R11_COMPANY_MOB_PHONE_NUM;
					private String R11_PRODUCT_TYPE;
					private BigDecimal R11_ACCT_NUM;
					private String R11_STATUS_OF_ACCT;
					private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R11_ACCT_BRANCH;
					private BigDecimal R11_ACCT_BALANCE_PULA;
					private String R11_CURRENCY_OF_ACCT;
					private BigDecimal R11_EXCHANGE_RATE;
					private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
					private String R12_COMPANY_NAME;
					private String R12_COMPANY_REG_NUM;
					private String R12_BUSINEES_PHY_ADDRESS;
					private String R12_POSTAL_ADDRESS;
					private String R12_COUNTRY_OF_REG;
					private String R12_COMPANY_EMAIL;
					private String R12_COMPANY_LANDLINE;
					private String R12_COMPANY_MOB_PHONE_NUM;
					private String R12_PRODUCT_TYPE;
					private BigDecimal R12_ACCT_NUM;
					private String R12_STATUS_OF_ACCT;
					private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
					private String R12_ACCT_BRANCH;
					private BigDecimal R12_ACCT_BALANCE_PULA;
					private String R12_CURRENCY_OF_ACCT;
					private BigDecimal R12_EXCHANGE_RATE;

					 @Id
						@Temporal(TemporalType.DATE)
						@Column(name = "REPORT_DATE")
						private Date REPORT_DATE;

						@Column(name = "REPORT_VERSION", length = 100)
						private BigDecimal REPORT_VERSION;

						@Column(name = "REPORT_FREQUENCY", length = 100)
						private String REPORT_FREQUENCY;

						@Column(name = "REPORT_CODE", length = 100)
						private String REPORT_CODE;

						@Column(name = "REPORT_DESC", length = 100)
						private String REPORT_DESC;

						@Column(name = "ENTITY_FLG", length = 1)
						private String ENTITY_FLG;

						@Column(name = "MODIFY_FLG", length = 1)
						private String MODIFY_FLG;

						@Column(name = "DEL_FLG", length = 1)
						private String DEL_FLG;
						



						public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR6_COMPANY_NAME() {
							return R6_COMPANY_NAME;
						}




						public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
							R6_COMPANY_NAME = r6_COMPANY_NAME;
						}




						public String getR6_COMPANY_REG_NUM() {
							return R6_COMPANY_REG_NUM;
						}




						public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
							R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
						}




						public String getR6_BUSINEES_PHY_ADDRESS() {
							return R6_BUSINEES_PHY_ADDRESS;
						}




						public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
							R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
						}




						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}




						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}




						public String getR6_COUNTRY_OF_REG() {
							return R6_COUNTRY_OF_REG;
						}




						public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
							R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
						}




						public String getR6_COMPANY_EMAIL() {
							return R6_COMPANY_EMAIL;
						}




						public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
							R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
						}




						public String getR6_COMPANY_LANDLINE() {
							return R6_COMPANY_LANDLINE;
						}




						public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
							R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
						}




						public String getR6_COMPANY_MOB_PHONE_NUM() {
							return R6_COMPANY_MOB_PHONE_NUM;
						}




						public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
							R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
						}




						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}




						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}




						public BigDecimal getR6_ACCT_NUM() {
							return R6_ACCT_NUM;
						}




						public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
							R6_ACCT_NUM = r6_ACCT_NUM;
						}




						public String getR6_STATUS_OF_ACCT() {
							return R6_STATUS_OF_ACCT;
						}




						public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
							R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
						}




						public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR6_ACCT_BRANCH() {
							return R6_ACCT_BRANCH;
						}




						public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
							R6_ACCT_BRANCH = r6_ACCT_BRANCH;
						}




						public BigDecimal getR6_ACCT_BALANCE_PULA() {
							return R6_ACCT_BALANCE_PULA;
						}




						public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
							R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
						}




						public String getR6_CURRENCY_OF_ACCT() {
							return R6_CURRENCY_OF_ACCT;
						}




						public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
							R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}




						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}




						public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR7_COMPANY_NAME() {
							return R7_COMPANY_NAME;
						}




						public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
							R7_COMPANY_NAME = r7_COMPANY_NAME;
						}




						public String getR7_COMPANY_REG_NUM() {
							return R7_COMPANY_REG_NUM;
						}




						public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
							R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
						}




						public String getR7_BUSINEES_PHY_ADDRESS() {
							return R7_BUSINEES_PHY_ADDRESS;
						}




						public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
							R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
						}




						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}




						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}




						public String getR7_COUNTRY_OF_REG() {
							return R7_COUNTRY_OF_REG;
						}




						public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
							R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
						}




						public String getR7_COMPANY_EMAIL() {
							return R7_COMPANY_EMAIL;
						}




						public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
							R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
						}




						public String getR7_COMPANY_LANDLINE() {
							return R7_COMPANY_LANDLINE;
						}




						public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
							R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
						}




						public String getR7_COMPANY_MOB_PHONE_NUM() {
							return R7_COMPANY_MOB_PHONE_NUM;
						}




						public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
							R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
						}




						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}




						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}




						public BigDecimal getR7_ACCT_NUM() {
							return R7_ACCT_NUM;
						}




						public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
							R7_ACCT_NUM = r7_ACCT_NUM;
						}




						public String getR7_STATUS_OF_ACCT() {
							return R7_STATUS_OF_ACCT;
						}




						public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
							R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
						}




						public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR7_ACCT_BRANCH() {
							return R7_ACCT_BRANCH;
						}




						public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
							R7_ACCT_BRANCH = r7_ACCT_BRANCH;
						}




						public BigDecimal getR7_ACCT_BALANCE_PULA() {
							return R7_ACCT_BALANCE_PULA;
						}




						public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
							R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
						}




						public String getR7_CURRENCY_OF_ACCT() {
							return R7_CURRENCY_OF_ACCT;
						}




						public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
							R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}




						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}




						public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR8_COMPANY_NAME() {
							return R8_COMPANY_NAME;
						}




						public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
							R8_COMPANY_NAME = r8_COMPANY_NAME;
						}




						public String getR8_COMPANY_REG_NUM() {
							return R8_COMPANY_REG_NUM;
						}




						public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
							R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
						}




						public String getR8_BUSINEES_PHY_ADDRESS() {
							return R8_BUSINEES_PHY_ADDRESS;
						}




						public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
							R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
						}




						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}




						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}




						public String getR8_COUNTRY_OF_REG() {
							return R8_COUNTRY_OF_REG;
						}




						public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
							R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
						}




						public String getR8_COMPANY_EMAIL() {
							return R8_COMPANY_EMAIL;
						}




						public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
							R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
						}




						public String getR8_COMPANY_LANDLINE() {
							return R8_COMPANY_LANDLINE;
						}




						public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
							R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
						}




						public String getR8_COMPANY_MOB_PHONE_NUM() {
							return R8_COMPANY_MOB_PHONE_NUM;
						}




						public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
							R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
						}




						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}




						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}




						public BigDecimal getR8_ACCT_NUM() {
							return R8_ACCT_NUM;
						}




						public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
							R8_ACCT_NUM = r8_ACCT_NUM;
						}




						public String getR8_STATUS_OF_ACCT() {
							return R8_STATUS_OF_ACCT;
						}




						public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
							R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
						}




						public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR8_ACCT_BRANCH() {
							return R8_ACCT_BRANCH;
						}




						public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
							R8_ACCT_BRANCH = r8_ACCT_BRANCH;
						}




						public BigDecimal getR8_ACCT_BALANCE_PULA() {
							return R8_ACCT_BALANCE_PULA;
						}




						public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
							R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
						}




						public String getR8_CURRENCY_OF_ACCT() {
							return R8_CURRENCY_OF_ACCT;
						}




						public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
							R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}




						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}




						public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR9_COMPANY_NAME() {
							return R9_COMPANY_NAME;
						}




						public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
							R9_COMPANY_NAME = r9_COMPANY_NAME;
						}




						public String getR9_COMPANY_REG_NUM() {
							return R9_COMPANY_REG_NUM;
						}




						public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
							R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
						}




						public String getR9_BUSINEES_PHY_ADDRESS() {
							return R9_BUSINEES_PHY_ADDRESS;
						}




						public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
							R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
						}




						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}




						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}




						public String getR9_COUNTRY_OF_REG() {
							return R9_COUNTRY_OF_REG;
						}




						public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
							R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
						}




						public String getR9_COMPANY_EMAIL() {
							return R9_COMPANY_EMAIL;
						}




						public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
							R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
						}




						public String getR9_COMPANY_LANDLINE() {
							return R9_COMPANY_LANDLINE;
						}




						public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
							R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
						}




						public String getR9_COMPANY_MOB_PHONE_NUM() {
							return R9_COMPANY_MOB_PHONE_NUM;
						}




						public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
							R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
						}




						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}




						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}




						public BigDecimal getR9_ACCT_NUM() {
							return R9_ACCT_NUM;
						}




						public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
							R9_ACCT_NUM = r9_ACCT_NUM;
						}




						public String getR9_STATUS_OF_ACCT() {
							return R9_STATUS_OF_ACCT;
						}




						public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
							R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
						}




						public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR9_ACCT_BRANCH() {
							return R9_ACCT_BRANCH;
						}




						public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
							R9_ACCT_BRANCH = r9_ACCT_BRANCH;
						}




						public BigDecimal getR9_ACCT_BALANCE_PULA() {
							return R9_ACCT_BALANCE_PULA;
						}




						public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
							R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
						}




						public String getR9_CURRENCY_OF_ACCT() {
							return R9_CURRENCY_OF_ACCT;
						}




						public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
							R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}




						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}




						public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR10_COMPANY_NAME() {
							return R10_COMPANY_NAME;
						}




						public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
							R10_COMPANY_NAME = r10_COMPANY_NAME;
						}




						public String getR10_COMPANY_REG_NUM() {
							return R10_COMPANY_REG_NUM;
						}




						public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
							R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
						}




						public String getR10_BUSINEES_PHY_ADDRESS() {
							return R10_BUSINEES_PHY_ADDRESS;
						}




						public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
							R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
						}




						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}




						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}




						public String getR10_COUNTRY_OF_REG() {
							return R10_COUNTRY_OF_REG;
						}




						public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
							R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
						}




						public String getR10_COMPANY_EMAIL() {
							return R10_COMPANY_EMAIL;
						}




						public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
							R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
						}




						public String getR10_COMPANY_LANDLINE() {
							return R10_COMPANY_LANDLINE;
						}




						public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
							R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
						}




						public String getR10_COMPANY_MOB_PHONE_NUM() {
							return R10_COMPANY_MOB_PHONE_NUM;
						}




						public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
							R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
						}




						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}




						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}




						public BigDecimal getR10_ACCT_NUM() {
							return R10_ACCT_NUM;
						}




						public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
							R10_ACCT_NUM = r10_ACCT_NUM;
						}




						public String getR10_STATUS_OF_ACCT() {
							return R10_STATUS_OF_ACCT;
						}




						public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
							R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
						}




						public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR10_ACCT_BRANCH() {
							return R10_ACCT_BRANCH;
						}




						public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
							R10_ACCT_BRANCH = r10_ACCT_BRANCH;
						}




						public BigDecimal getR10_ACCT_BALANCE_PULA() {
							return R10_ACCT_BALANCE_PULA;
						}




						public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
							R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
						}




						public String getR10_CURRENCY_OF_ACCT() {
							return R10_CURRENCY_OF_ACCT;
						}




						public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
							R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}




						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}




						public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR11_COMPANY_NAME() {
							return R11_COMPANY_NAME;
						}




						public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
							R11_COMPANY_NAME = r11_COMPANY_NAME;
						}




						public String getR11_COMPANY_REG_NUM() {
							return R11_COMPANY_REG_NUM;
						}




						public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
							R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
						}




						public String getR11_BUSINEES_PHY_ADDRESS() {
							return R11_BUSINEES_PHY_ADDRESS;
						}




						public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
							R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
						}




						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}




						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}




						public String getR11_COUNTRY_OF_REG() {
							return R11_COUNTRY_OF_REG;
						}




						public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
							R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
						}




						public String getR11_COMPANY_EMAIL() {
							return R11_COMPANY_EMAIL;
						}




						public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
							R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
						}




						public String getR11_COMPANY_LANDLINE() {
							return R11_COMPANY_LANDLINE;
						}




						public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
							R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
						}




						public String getR11_COMPANY_MOB_PHONE_NUM() {
							return R11_COMPANY_MOB_PHONE_NUM;
						}




						public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
							R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
						}




						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}




						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}




						public BigDecimal getR11_ACCT_NUM() {
							return R11_ACCT_NUM;
						}




						public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
							R11_ACCT_NUM = r11_ACCT_NUM;
						}




						public String getR11_STATUS_OF_ACCT() {
							return R11_STATUS_OF_ACCT;
						}




						public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
							R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
						}




						public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR11_ACCT_BRANCH() {
							return R11_ACCT_BRANCH;
						}




						public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
							R11_ACCT_BRANCH = r11_ACCT_BRANCH;
						}




						public BigDecimal getR11_ACCT_BALANCE_PULA() {
							return R11_ACCT_BALANCE_PULA;
						}




						public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
							R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
						}




						public String getR11_CURRENCY_OF_ACCT() {
							return R11_CURRENCY_OF_ACCT;
						}




						public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
							R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}




						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
						}




						public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
							return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
							R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						}




						public String getR12_COMPANY_NAME() {
							return R12_COMPANY_NAME;
						}




						public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
							R12_COMPANY_NAME = r12_COMPANY_NAME;
						}




						public String getR12_COMPANY_REG_NUM() {
							return R12_COMPANY_REG_NUM;
						}




						public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
							R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
						}




						public String getR12_BUSINEES_PHY_ADDRESS() {
							return R12_BUSINEES_PHY_ADDRESS;
						}




						public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
							R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
						}




						public String getR12_POSTAL_ADDRESS() {
							return R12_POSTAL_ADDRESS;
						}




						public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
							R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
						}




						public String getR12_COUNTRY_OF_REG() {
							return R12_COUNTRY_OF_REG;
						}




						public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
							R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
						}




						public String getR12_COMPANY_EMAIL() {
							return R12_COMPANY_EMAIL;
						}




						public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
							R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
						}




						public String getR12_COMPANY_LANDLINE() {
							return R12_COMPANY_LANDLINE;
						}




						public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
							R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
						}




						public String getR12_COMPANY_MOB_PHONE_NUM() {
							return R12_COMPANY_MOB_PHONE_NUM;
						}




						public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
							R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
						}




						public String getR12_PRODUCT_TYPE() {
							return R12_PRODUCT_TYPE;
						}




						public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
							R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
						}




						public BigDecimal getR12_ACCT_NUM() {
							return R12_ACCT_NUM;
						}




						public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
							R12_ACCT_NUM = r12_ACCT_NUM;
						}




						public String getR12_STATUS_OF_ACCT() {
							return R12_STATUS_OF_ACCT;
						}




						public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
							R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
						}




						public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
							return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
								String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
							R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						}




						public String getR12_ACCT_BRANCH() {
							return R12_ACCT_BRANCH;
						}




						public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
							R12_ACCT_BRANCH = r12_ACCT_BRANCH;
						}




						public BigDecimal getR12_ACCT_BALANCE_PULA() {
							return R12_ACCT_BALANCE_PULA;
						}




						public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
							R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
						}




						public String getR12_CURRENCY_OF_ACCT() {
							return R12_CURRENCY_OF_ACCT;
						}




						public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
							R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
						}




						public BigDecimal getR12_EXCHANGE_RATE() {
							return R12_EXCHANGE_RATE;
						}




						public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
							R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

						public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
							REPORT_FREQUENCY = rEPORT_FREQUENCY;
						}

						public String getREPORT_CODE() {
							return REPORT_CODE;
						}

						public void setREPORT_CODE(String rEPORT_CODE) {
							REPORT_CODE = rEPORT_CODE;
						}

						public String getREPORT_DESC() {
							return REPORT_DESC;
						}

						public void setREPORT_DESC(String rEPORT_DESC) {
							REPORT_DESC = rEPORT_DESC;
						}

						public String getENTITY_FLG() {
							return ENTITY_FLG;
						}

						public void setENTITY_FLG(String eNTITY_FLG) {
							ENTITY_FLG = eNTITY_FLG;
						}

						public String getMODIFY_FLG() {
							return MODIFY_FLG;
						}

						public void setMODIFY_FLG(String mODIFY_FLG) {
							MODIFY_FLG = mODIFY_FLG;
						}

						public String getDEL_FLG() {
							return DEL_FLG;
						}

						public void setDEL_FLG(String dEL_FLG) {
							DEL_FLG = dEL_FLG;
						}

					}
	
	
	
	// ROW MAPPER DETAIL

		class BDISB2RowMapper_Detail implements RowMapper<BDISB2_Detail_Entity> {

			@Override
			public BDISB2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				BDISB2_Detail_Entity obj = new BDISB2_Detail_Entity();

				// ===================== R6 =====================
				obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
				obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
				obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
				obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
				obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
				obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
				obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
				obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
				obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
				obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
				obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
				obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
				obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
				obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
				obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

				// ===================== R7 =====================
				obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
				obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
				obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
				obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
				obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
				obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
				obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
				obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
				obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
				obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
				obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
				obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
				obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
				obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
				obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

				// ===================== R8 =====================
				obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
				obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
				obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
				obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
				obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
				obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
				obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
				obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
				obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
				obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
				obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
				obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
				obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
				obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
				obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

				// ===================== R9 =====================
				obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
				obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
				obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
				obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
				obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
				obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
				obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
				obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
				obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
				obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
				obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
				obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
				obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
				obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
				obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

				// ===================== R10 =====================
				obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
				obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
				obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
				obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
				obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
				obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
				obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
				obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
				obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
				obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
				obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
				obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
				obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
				obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
				obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

				// ===================== R11 =====================
				obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
				obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
				obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
				obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
				obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
				obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
				obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
				obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
				obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
				obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
				obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
				obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
				obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
				obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
				obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

				// ===================== R12 =====================
				obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
				obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
				obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
				obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
				obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
				obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
				obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
				obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
				obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
				obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
				obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
				obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
				obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
				obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
				obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

				// COMMON FIELDS
				obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
				obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
				obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
				obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
				obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
				obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
				obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
				obj.setDEL_FLG(rs.getString("DEL_FLG"));

				return obj;
			}
		}

		public static class BDISB2_Detail_Entity {
					
						private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R6_COMPANY_NAME;
						private String R6_COMPANY_REG_NUM;
						private String R6_BUSINEES_PHY_ADDRESS;
						private String R6_POSTAL_ADDRESS;
						private String R6_COUNTRY_OF_REG;
						private String R6_COMPANY_EMAIL;
						private String R6_COMPANY_LANDLINE;
						private String R6_COMPANY_MOB_PHONE_NUM;
						private String R6_PRODUCT_TYPE;
						private BigDecimal R6_ACCT_NUM;
						private String R6_STATUS_OF_ACCT;
						private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R6_ACCT_BRANCH;
						private BigDecimal R6_ACCT_BALANCE_PULA;
						private String R6_CURRENCY_OF_ACCT;
						private BigDecimal R6_EXCHANGE_RATE;
						private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R7_COMPANY_NAME;
						private String R7_COMPANY_REG_NUM;
						private String R7_BUSINEES_PHY_ADDRESS;
						private String R7_POSTAL_ADDRESS;
						private String R7_COUNTRY_OF_REG;
						private String R7_COMPANY_EMAIL;
						private String R7_COMPANY_LANDLINE;
						private String R7_COMPANY_MOB_PHONE_NUM;
						private String R7_PRODUCT_TYPE;
						private BigDecimal R7_ACCT_NUM;
						private String R7_STATUS_OF_ACCT;
						private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R7_ACCT_BRANCH;
						private BigDecimal R7_ACCT_BALANCE_PULA;
						private String R7_CURRENCY_OF_ACCT;
						private BigDecimal R7_EXCHANGE_RATE;
						private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R8_COMPANY_NAME;
						private String R8_COMPANY_REG_NUM;
						private String R8_BUSINEES_PHY_ADDRESS;
						private String R8_POSTAL_ADDRESS;
						private String R8_COUNTRY_OF_REG;
						private String R8_COMPANY_EMAIL;
						private String R8_COMPANY_LANDLINE;
						private String R8_COMPANY_MOB_PHONE_NUM;
						private String R8_PRODUCT_TYPE;
						private BigDecimal R8_ACCT_NUM;
						private String R8_STATUS_OF_ACCT;
						private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R8_ACCT_BRANCH;
						private BigDecimal R8_ACCT_BALANCE_PULA;
						private String R8_CURRENCY_OF_ACCT;
						private BigDecimal R8_EXCHANGE_RATE;
						private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R9_COMPANY_NAME;
						private String R9_COMPANY_REG_NUM;
						private String R9_BUSINEES_PHY_ADDRESS;
						private String R9_POSTAL_ADDRESS;
						private String R9_COUNTRY_OF_REG;
						private String R9_COMPANY_EMAIL;
						private String R9_COMPANY_LANDLINE;
						private String R9_COMPANY_MOB_PHONE_NUM;
						private String R9_PRODUCT_TYPE;
						private BigDecimal R9_ACCT_NUM;
						private String R9_STATUS_OF_ACCT;
						private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R9_ACCT_BRANCH;
						private BigDecimal R9_ACCT_BALANCE_PULA;
						private String R9_CURRENCY_OF_ACCT;
						private BigDecimal R9_EXCHANGE_RATE;
						private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R10_COMPANY_NAME;
						private String R10_COMPANY_REG_NUM;
						private String R10_BUSINEES_PHY_ADDRESS;
						private String R10_POSTAL_ADDRESS;
						private String R10_COUNTRY_OF_REG;
						private String R10_COMPANY_EMAIL;
						private String R10_COMPANY_LANDLINE;
						private String R10_COMPANY_MOB_PHONE_NUM;
						private String R10_PRODUCT_TYPE;
						private BigDecimal R10_ACCT_NUM;
						private String R10_STATUS_OF_ACCT;
						private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R10_ACCT_BRANCH;
						private BigDecimal R10_ACCT_BALANCE_PULA;
						private String R10_CURRENCY_OF_ACCT;
						private BigDecimal R10_EXCHANGE_RATE;
						private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R11_COMPANY_NAME;
						private String R11_COMPANY_REG_NUM;
						private String R11_BUSINEES_PHY_ADDRESS;
						private String R11_POSTAL_ADDRESS;
						private String R11_COUNTRY_OF_REG;
						private String R11_COMPANY_EMAIL;
						private String R11_COMPANY_LANDLINE;
						private String R11_COMPANY_MOB_PHONE_NUM;
						private String R11_PRODUCT_TYPE;
						private BigDecimal R11_ACCT_NUM;
						private String R11_STATUS_OF_ACCT;
						private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R11_ACCT_BRANCH;
						private BigDecimal R11_ACCT_BALANCE_PULA;
						private String R11_CURRENCY_OF_ACCT;
						private BigDecimal R11_EXCHANGE_RATE;
						private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R12_COMPANY_NAME;
						private String R12_COMPANY_REG_NUM;
						private String R12_BUSINEES_PHY_ADDRESS;
						private String R12_POSTAL_ADDRESS;
						private String R12_COUNTRY_OF_REG;
						private String R12_COMPANY_EMAIL;
						private String R12_COMPANY_LANDLINE;
						private String R12_COMPANY_MOB_PHONE_NUM;
						private String R12_PRODUCT_TYPE;
						private BigDecimal R12_ACCT_NUM;
						private String R12_STATUS_OF_ACCT;
						private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R12_ACCT_BRANCH;
						private BigDecimal R12_ACCT_BALANCE_PULA;
						private String R12_CURRENCY_OF_ACCT;
						private BigDecimal R12_EXCHANGE_RATE;

						 @Id
							@Temporal(TemporalType.DATE)
							@Column(name = "REPORT_DATE")
							private Date REPORT_DATE;

							@Column(name = "REPORT_VERSION", length = 100)
							private BigDecimal REPORT_VERSION;

							@Column(name = "REPORT_FREQUENCY", length = 100)
							private String REPORT_FREQUENCY;

							@Column(name = "REPORT_CODE", length = 100)
							private String REPORT_CODE;

							@Column(name = "REPORT_DESC", length = 100)
							private String REPORT_DESC;

							@Column(name = "ENTITY_FLG", length = 1)
							private String ENTITY_FLG;

							@Column(name = "MODIFY_FLG", length = 1)
							private String MODIFY_FLG;

							@Column(name = "DEL_FLG", length = 1)
							private String DEL_FLG;
							



							public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR6_COMPANY_NAME() {
								return R6_COMPANY_NAME;
							}




							public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
								R6_COMPANY_NAME = r6_COMPANY_NAME;
							}




							public String getR6_COMPANY_REG_NUM() {
								return R6_COMPANY_REG_NUM;
							}




							public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
								R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
							}




							public String getR6_BUSINEES_PHY_ADDRESS() {
								return R6_BUSINEES_PHY_ADDRESS;
							}




							public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
								R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
							}




							public String getR6_POSTAL_ADDRESS() {
								return R6_POSTAL_ADDRESS;
							}




							public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
								R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
							}




							public String getR6_COUNTRY_OF_REG() {
								return R6_COUNTRY_OF_REG;
							}




							public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
								R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
							}




							public String getR6_COMPANY_EMAIL() {
								return R6_COMPANY_EMAIL;
							}




							public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
								R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
							}




							public String getR6_COMPANY_LANDLINE() {
								return R6_COMPANY_LANDLINE;
							}




							public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
								R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
							}




							public String getR6_COMPANY_MOB_PHONE_NUM() {
								return R6_COMPANY_MOB_PHONE_NUM;
							}




							public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
								R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
							}




							public String getR6_PRODUCT_TYPE() {
								return R6_PRODUCT_TYPE;
							}




							public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
								R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
							}




							public BigDecimal getR6_ACCT_NUM() {
								return R6_ACCT_NUM;
							}




							public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
								R6_ACCT_NUM = r6_ACCT_NUM;
							}




							public String getR6_STATUS_OF_ACCT() {
								return R6_STATUS_OF_ACCT;
							}




							public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
								R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
							}




							public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR6_ACCT_BRANCH() {
								return R6_ACCT_BRANCH;
							}




							public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
								R6_ACCT_BRANCH = r6_ACCT_BRANCH;
							}




							public BigDecimal getR6_ACCT_BALANCE_PULA() {
								return R6_ACCT_BALANCE_PULA;
							}




							public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
								R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
							}




							public String getR6_CURRENCY_OF_ACCT() {
								return R6_CURRENCY_OF_ACCT;
							}




							public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
								R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR6_EXCHANGE_RATE() {
								return R6_EXCHANGE_RATE;
							}




							public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
								R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
							}




							public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR7_COMPANY_NAME() {
								return R7_COMPANY_NAME;
							}




							public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
								R7_COMPANY_NAME = r7_COMPANY_NAME;
							}




							public String getR7_COMPANY_REG_NUM() {
								return R7_COMPANY_REG_NUM;
							}




							public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
								R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
							}




							public String getR7_BUSINEES_PHY_ADDRESS() {
								return R7_BUSINEES_PHY_ADDRESS;
							}




							public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
								R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
							}




							public String getR7_POSTAL_ADDRESS() {
								return R7_POSTAL_ADDRESS;
							}




							public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
								R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
							}




							public String getR7_COUNTRY_OF_REG() {
								return R7_COUNTRY_OF_REG;
							}




							public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
								R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
							}




							public String getR7_COMPANY_EMAIL() {
								return R7_COMPANY_EMAIL;
							}




							public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
								R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
							}




							public String getR7_COMPANY_LANDLINE() {
								return R7_COMPANY_LANDLINE;
							}




							public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
								R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
							}




							public String getR7_COMPANY_MOB_PHONE_NUM() {
								return R7_COMPANY_MOB_PHONE_NUM;
							}




							public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
								R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
							}




							public String getR7_PRODUCT_TYPE() {
								return R7_PRODUCT_TYPE;
							}




							public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
								R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
							}




							public BigDecimal getR7_ACCT_NUM() {
								return R7_ACCT_NUM;
							}




							public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
								R7_ACCT_NUM = r7_ACCT_NUM;
							}




							public String getR7_STATUS_OF_ACCT() {
								return R7_STATUS_OF_ACCT;
							}




							public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
								R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
							}




							public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR7_ACCT_BRANCH() {
								return R7_ACCT_BRANCH;
							}




							public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
								R7_ACCT_BRANCH = r7_ACCT_BRANCH;
							}




							public BigDecimal getR7_ACCT_BALANCE_PULA() {
								return R7_ACCT_BALANCE_PULA;
							}




							public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
								R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
							}




							public String getR7_CURRENCY_OF_ACCT() {
								return R7_CURRENCY_OF_ACCT;
							}




							public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
								R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR7_EXCHANGE_RATE() {
								return R7_EXCHANGE_RATE;
							}




							public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
								R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
							}




							public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR8_COMPANY_NAME() {
								return R8_COMPANY_NAME;
							}




							public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
								R8_COMPANY_NAME = r8_COMPANY_NAME;
							}




							public String getR8_COMPANY_REG_NUM() {
								return R8_COMPANY_REG_NUM;
							}




							public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
								R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
							}




							public String getR8_BUSINEES_PHY_ADDRESS() {
								return R8_BUSINEES_PHY_ADDRESS;
							}




							public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
								R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
							}




							public String getR8_POSTAL_ADDRESS() {
								return R8_POSTAL_ADDRESS;
							}




							public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
								R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
							}




							public String getR8_COUNTRY_OF_REG() {
								return R8_COUNTRY_OF_REG;
							}




							public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
								R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
							}




							public String getR8_COMPANY_EMAIL() {
								return R8_COMPANY_EMAIL;
							}




							public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
								R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
							}




							public String getR8_COMPANY_LANDLINE() {
								return R8_COMPANY_LANDLINE;
							}




							public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
								R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
							}




							public String getR8_COMPANY_MOB_PHONE_NUM() {
								return R8_COMPANY_MOB_PHONE_NUM;
							}




							public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
								R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
							}




							public String getR8_PRODUCT_TYPE() {
								return R8_PRODUCT_TYPE;
							}




							public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
								R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
							}




							public BigDecimal getR8_ACCT_NUM() {
								return R8_ACCT_NUM;
							}




							public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
								R8_ACCT_NUM = r8_ACCT_NUM;
							}




							public String getR8_STATUS_OF_ACCT() {
								return R8_STATUS_OF_ACCT;
							}




							public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
								R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
							}




							public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR8_ACCT_BRANCH() {
								return R8_ACCT_BRANCH;
							}




							public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
								R8_ACCT_BRANCH = r8_ACCT_BRANCH;
							}




							public BigDecimal getR8_ACCT_BALANCE_PULA() {
								return R8_ACCT_BALANCE_PULA;
							}




							public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
								R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
							}




							public String getR8_CURRENCY_OF_ACCT() {
								return R8_CURRENCY_OF_ACCT;
							}




							public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
								R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR8_EXCHANGE_RATE() {
								return R8_EXCHANGE_RATE;
							}




							public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
								R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
							}




							public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR9_COMPANY_NAME() {
								return R9_COMPANY_NAME;
							}




							public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
								R9_COMPANY_NAME = r9_COMPANY_NAME;
							}




							public String getR9_COMPANY_REG_NUM() {
								return R9_COMPANY_REG_NUM;
							}




							public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
								R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
							}




							public String getR9_BUSINEES_PHY_ADDRESS() {
								return R9_BUSINEES_PHY_ADDRESS;
							}




							public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
								R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
							}




							public String getR9_POSTAL_ADDRESS() {
								return R9_POSTAL_ADDRESS;
							}




							public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
								R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
							}




							public String getR9_COUNTRY_OF_REG() {
								return R9_COUNTRY_OF_REG;
							}




							public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
								R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
							}




							public String getR9_COMPANY_EMAIL() {
								return R9_COMPANY_EMAIL;
							}




							public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
								R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
							}




							public String getR9_COMPANY_LANDLINE() {
								return R9_COMPANY_LANDLINE;
							}




							public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
								R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
							}




							public String getR9_COMPANY_MOB_PHONE_NUM() {
								return R9_COMPANY_MOB_PHONE_NUM;
							}




							public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
								R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
							}




							public String getR9_PRODUCT_TYPE() {
								return R9_PRODUCT_TYPE;
							}




							public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
								R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
							}




							public BigDecimal getR9_ACCT_NUM() {
								return R9_ACCT_NUM;
							}




							public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
								R9_ACCT_NUM = r9_ACCT_NUM;
							}




							public String getR9_STATUS_OF_ACCT() {
								return R9_STATUS_OF_ACCT;
							}




							public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
								R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
							}




							public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR9_ACCT_BRANCH() {
								return R9_ACCT_BRANCH;
							}




							public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
								R9_ACCT_BRANCH = r9_ACCT_BRANCH;
							}




							public BigDecimal getR9_ACCT_BALANCE_PULA() {
								return R9_ACCT_BALANCE_PULA;
							}




							public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
								R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
							}




							public String getR9_CURRENCY_OF_ACCT() {
								return R9_CURRENCY_OF_ACCT;
							}




							public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
								R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR9_EXCHANGE_RATE() {
								return R9_EXCHANGE_RATE;
							}




							public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
								R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
							}




							public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR10_COMPANY_NAME() {
								return R10_COMPANY_NAME;
							}




							public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
								R10_COMPANY_NAME = r10_COMPANY_NAME;
							}




							public String getR10_COMPANY_REG_NUM() {
								return R10_COMPANY_REG_NUM;
							}




							public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
								R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
							}




							public String getR10_BUSINEES_PHY_ADDRESS() {
								return R10_BUSINEES_PHY_ADDRESS;
							}




							public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
								R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
							}




							public String getR10_POSTAL_ADDRESS() {
								return R10_POSTAL_ADDRESS;
							}




							public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
								R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
							}




							public String getR10_COUNTRY_OF_REG() {
								return R10_COUNTRY_OF_REG;
							}




							public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
								R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
							}




							public String getR10_COMPANY_EMAIL() {
								return R10_COMPANY_EMAIL;
							}




							public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
								R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
							}




							public String getR10_COMPANY_LANDLINE() {
								return R10_COMPANY_LANDLINE;
							}




							public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
								R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
							}




							public String getR10_COMPANY_MOB_PHONE_NUM() {
								return R10_COMPANY_MOB_PHONE_NUM;
							}




							public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
								R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
							}




							public String getR10_PRODUCT_TYPE() {
								return R10_PRODUCT_TYPE;
							}




							public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
								R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
							}




							public BigDecimal getR10_ACCT_NUM() {
								return R10_ACCT_NUM;
							}




							public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
								R10_ACCT_NUM = r10_ACCT_NUM;
							}




							public String getR10_STATUS_OF_ACCT() {
								return R10_STATUS_OF_ACCT;
							}




							public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
								R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
							}




							public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR10_ACCT_BRANCH() {
								return R10_ACCT_BRANCH;
							}




							public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
								R10_ACCT_BRANCH = r10_ACCT_BRANCH;
							}




							public BigDecimal getR10_ACCT_BALANCE_PULA() {
								return R10_ACCT_BALANCE_PULA;
							}




							public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
								R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
							}




							public String getR10_CURRENCY_OF_ACCT() {
								return R10_CURRENCY_OF_ACCT;
							}




							public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
								R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR10_EXCHANGE_RATE() {
								return R10_EXCHANGE_RATE;
							}




							public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
								R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
							}




							public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR11_COMPANY_NAME() {
								return R11_COMPANY_NAME;
							}




							public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
								R11_COMPANY_NAME = r11_COMPANY_NAME;
							}




							public String getR11_COMPANY_REG_NUM() {
								return R11_COMPANY_REG_NUM;
							}




							public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
								R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
							}




							public String getR11_BUSINEES_PHY_ADDRESS() {
								return R11_BUSINEES_PHY_ADDRESS;
							}




							public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
								R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
							}




							public String getR11_POSTAL_ADDRESS() {
								return R11_POSTAL_ADDRESS;
							}




							public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
								R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
							}




							public String getR11_COUNTRY_OF_REG() {
								return R11_COUNTRY_OF_REG;
							}




							public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
								R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
							}




							public String getR11_COMPANY_EMAIL() {
								return R11_COMPANY_EMAIL;
							}




							public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
								R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
							}




							public String getR11_COMPANY_LANDLINE() {
								return R11_COMPANY_LANDLINE;
							}




							public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
								R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
							}




							public String getR11_COMPANY_MOB_PHONE_NUM() {
								return R11_COMPANY_MOB_PHONE_NUM;
							}




							public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
								R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
							}




							public String getR11_PRODUCT_TYPE() {
								return R11_PRODUCT_TYPE;
							}




							public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
								R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
							}




							public BigDecimal getR11_ACCT_NUM() {
								return R11_ACCT_NUM;
							}




							public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
								R11_ACCT_NUM = r11_ACCT_NUM;
							}




							public String getR11_STATUS_OF_ACCT() {
								return R11_STATUS_OF_ACCT;
							}




							public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
								R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
							}




							public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR11_ACCT_BRANCH() {
								return R11_ACCT_BRANCH;
							}




							public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
								R11_ACCT_BRANCH = r11_ACCT_BRANCH;
							}




							public BigDecimal getR11_ACCT_BALANCE_PULA() {
								return R11_ACCT_BALANCE_PULA;
							}




							public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
								R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
							}




							public String getR11_CURRENCY_OF_ACCT() {
								return R11_CURRENCY_OF_ACCT;
							}




							public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
								R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR11_EXCHANGE_RATE() {
								return R11_EXCHANGE_RATE;
							}




							public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
								R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
							}




							public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR12_COMPANY_NAME() {
								return R12_COMPANY_NAME;
							}




							public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
								R12_COMPANY_NAME = r12_COMPANY_NAME;
							}




							public String getR12_COMPANY_REG_NUM() {
								return R12_COMPANY_REG_NUM;
							}




							public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
								R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
							}




							public String getR12_BUSINEES_PHY_ADDRESS() {
								return R12_BUSINEES_PHY_ADDRESS;
							}




							public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
								R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
							}




							public String getR12_POSTAL_ADDRESS() {
								return R12_POSTAL_ADDRESS;
							}




							public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
								R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
							}




							public String getR12_COUNTRY_OF_REG() {
								return R12_COUNTRY_OF_REG;
							}




							public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
								R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
							}




							public String getR12_COMPANY_EMAIL() {
								return R12_COMPANY_EMAIL;
							}




							public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
								R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
							}




							public String getR12_COMPANY_LANDLINE() {
								return R12_COMPANY_LANDLINE;
							}




							public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
								R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
							}




							public String getR12_COMPANY_MOB_PHONE_NUM() {
								return R12_COMPANY_MOB_PHONE_NUM;
							}




							public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
								R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
							}




							public String getR12_PRODUCT_TYPE() {
								return R12_PRODUCT_TYPE;
							}




							public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
								R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
							}




							public BigDecimal getR12_ACCT_NUM() {
								return R12_ACCT_NUM;
							}




							public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
								R12_ACCT_NUM = r12_ACCT_NUM;
							}




							public String getR12_STATUS_OF_ACCT() {
								return R12_STATUS_OF_ACCT;
							}




							public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
								R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
							}




							public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR12_ACCT_BRANCH() {
								return R12_ACCT_BRANCH;
							}




							public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
								R12_ACCT_BRANCH = r12_ACCT_BRANCH;
							}




							public BigDecimal getR12_ACCT_BALANCE_PULA() {
								return R12_ACCT_BALANCE_PULA;
							}




							public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
								R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
							}




							public String getR12_CURRENCY_OF_ACCT() {
								return R12_CURRENCY_OF_ACCT;
							}




							public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
								R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR12_EXCHANGE_RATE() {
								return R12_EXCHANGE_RATE;
							}




							public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
								R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

							public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
								REPORT_FREQUENCY = rEPORT_FREQUENCY;
							}

							public String getREPORT_CODE() {
								return REPORT_CODE;
							}

							public void setREPORT_CODE(String rEPORT_CODE) {
								REPORT_CODE = rEPORT_CODE;
							}

							public String getREPORT_DESC() {
								return REPORT_DESC;
							}

							public void setREPORT_DESC(String rEPORT_DESC) {
								REPORT_DESC = rEPORT_DESC;
							}

							public String getENTITY_FLG() {
								return ENTITY_FLG;
							}

							public void setENTITY_FLG(String eNTITY_FLG) {
								ENTITY_FLG = eNTITY_FLG;
							}

							public String getMODIFY_FLG() {
								return MODIFY_FLG;
							}

							public void setMODIFY_FLG(String mODIFY_FLG) {
								MODIFY_FLG = mODIFY_FLG;
							}

							public String getDEL_FLG() {
								return DEL_FLG;
							}

							public void setDEL_FLG(String dEL_FLG) {
								DEL_FLG = dEL_FLG;
							}

						}
		

		
		// ROW MAPPER ARCHIVAL SUMMARY

		class BDISB2_RowMapper_Archival implements RowMapper<BDISB2_Archival_Summary_Entity> {

			@Override
			public BDISB2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				BDISB2_Archival_Summary_Entity obj = new BDISB2_Archival_Summary_Entity();

				// ===================== R6 =====================
				obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
				obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
				obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
				obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
				obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
				obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
				obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
				obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
				obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
				obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
				obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
				obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
				obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
				obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
				obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

				// ===================== R7 =====================
				obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
				obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
				obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
				obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
				obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
				obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
				obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
				obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
				obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
				obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
				obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
				obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
				obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
				obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
				obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

				// ===================== R8 =====================
				obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
				obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
				obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
				obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
				obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
				obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
				obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
				obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
				obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
				obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
				obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
				obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
				obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
				obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
				obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

				// ===================== R9 =====================
				obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
				obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
				obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
				obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
				obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
				obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
				obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
				obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
				obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
				obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
				obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
				obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
				obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
				obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
				obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

				// ===================== R10 =====================
				obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
				obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
				obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
				obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
				obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
				obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
				obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
				obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
				obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
				obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
				obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
				obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
				obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
				obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
				obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

				// ===================== R11 =====================
				obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
				obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
				obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
				obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
				obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
				obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
				obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
				obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
				obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
				obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
				obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
				obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
				obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
				obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
				obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

				// ===================== R12 =====================
				obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
				obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
				obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
				obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
				obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
				obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
				obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
				obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
				obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
				obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
				obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
				obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
				obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
				obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
				obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

				// COMMON FIELDS
				obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
				obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
				obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
				obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
				obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
				obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
				obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
				obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
				obj.setDEL_FLG(rs.getString("DEL_FLG"));

				return obj;
			}
		}

		public static class BDISB2_Archival_Summary_Entity {
					
						private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R6_COMPANY_NAME;
						private String R6_COMPANY_REG_NUM;
						private String R6_BUSINEES_PHY_ADDRESS;
						private String R6_POSTAL_ADDRESS;
						private String R6_COUNTRY_OF_REG;
						private String R6_COMPANY_EMAIL;
						private String R6_COMPANY_LANDLINE;
						private String R6_COMPANY_MOB_PHONE_NUM;
						private String R6_PRODUCT_TYPE;
						private BigDecimal R6_ACCT_NUM;
						private String R6_STATUS_OF_ACCT;
						private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R6_ACCT_BRANCH;
						private BigDecimal R6_ACCT_BALANCE_PULA;
						private String R6_CURRENCY_OF_ACCT;
						private BigDecimal R6_EXCHANGE_RATE;
						private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R7_COMPANY_NAME;
						private String R7_COMPANY_REG_NUM;
						private String R7_BUSINEES_PHY_ADDRESS;
						private String R7_POSTAL_ADDRESS;
						private String R7_COUNTRY_OF_REG;
						private String R7_COMPANY_EMAIL;
						private String R7_COMPANY_LANDLINE;
						private String R7_COMPANY_MOB_PHONE_NUM;
						private String R7_PRODUCT_TYPE;
						private BigDecimal R7_ACCT_NUM;
						private String R7_STATUS_OF_ACCT;
						private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R7_ACCT_BRANCH;
						private BigDecimal R7_ACCT_BALANCE_PULA;
						private String R7_CURRENCY_OF_ACCT;
						private BigDecimal R7_EXCHANGE_RATE;
						private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R8_COMPANY_NAME;
						private String R8_COMPANY_REG_NUM;
						private String R8_BUSINEES_PHY_ADDRESS;
						private String R8_POSTAL_ADDRESS;
						private String R8_COUNTRY_OF_REG;
						private String R8_COMPANY_EMAIL;
						private String R8_COMPANY_LANDLINE;
						private String R8_COMPANY_MOB_PHONE_NUM;
						private String R8_PRODUCT_TYPE;
						private BigDecimal R8_ACCT_NUM;
						private String R8_STATUS_OF_ACCT;
						private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R8_ACCT_BRANCH;
						private BigDecimal R8_ACCT_BALANCE_PULA;
						private String R8_CURRENCY_OF_ACCT;
						private BigDecimal R8_EXCHANGE_RATE;
						private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R9_COMPANY_NAME;
						private String R9_COMPANY_REG_NUM;
						private String R9_BUSINEES_PHY_ADDRESS;
						private String R9_POSTAL_ADDRESS;
						private String R9_COUNTRY_OF_REG;
						private String R9_COMPANY_EMAIL;
						private String R9_COMPANY_LANDLINE;
						private String R9_COMPANY_MOB_PHONE_NUM;
						private String R9_PRODUCT_TYPE;
						private BigDecimal R9_ACCT_NUM;
						private String R9_STATUS_OF_ACCT;
						private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R9_ACCT_BRANCH;
						private BigDecimal R9_ACCT_BALANCE_PULA;
						private String R9_CURRENCY_OF_ACCT;
						private BigDecimal R9_EXCHANGE_RATE;
						private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R10_COMPANY_NAME;
						private String R10_COMPANY_REG_NUM;
						private String R10_BUSINEES_PHY_ADDRESS;
						private String R10_POSTAL_ADDRESS;
						private String R10_COUNTRY_OF_REG;
						private String R10_COMPANY_EMAIL;
						private String R10_COMPANY_LANDLINE;
						private String R10_COMPANY_MOB_PHONE_NUM;
						private String R10_PRODUCT_TYPE;
						private BigDecimal R10_ACCT_NUM;
						private String R10_STATUS_OF_ACCT;
						private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R10_ACCT_BRANCH;
						private BigDecimal R10_ACCT_BALANCE_PULA;
						private String R10_CURRENCY_OF_ACCT;
						private BigDecimal R10_EXCHANGE_RATE;
						private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R11_COMPANY_NAME;
						private String R11_COMPANY_REG_NUM;
						private String R11_BUSINEES_PHY_ADDRESS;
						private String R11_POSTAL_ADDRESS;
						private String R11_COUNTRY_OF_REG;
						private String R11_COMPANY_EMAIL;
						private String R11_COMPANY_LANDLINE;
						private String R11_COMPANY_MOB_PHONE_NUM;
						private String R11_PRODUCT_TYPE;
						private BigDecimal R11_ACCT_NUM;
						private String R11_STATUS_OF_ACCT;
						private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R11_ACCT_BRANCH;
						private BigDecimal R11_ACCT_BALANCE_PULA;
						private String R11_CURRENCY_OF_ACCT;
						private BigDecimal R11_EXCHANGE_RATE;
						private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R12_COMPANY_NAME;
						private String R12_COMPANY_REG_NUM;
						private String R12_BUSINEES_PHY_ADDRESS;
						private String R12_POSTAL_ADDRESS;
						private String R12_COUNTRY_OF_REG;
						private String R12_COMPANY_EMAIL;
						private String R12_COMPANY_LANDLINE;
						private String R12_COMPANY_MOB_PHONE_NUM;
						private String R12_PRODUCT_TYPE;
						private BigDecimal R12_ACCT_NUM;
						private String R12_STATUS_OF_ACCT;
						private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R12_ACCT_BRANCH;
						private BigDecimal R12_ACCT_BALANCE_PULA;
						private String R12_CURRENCY_OF_ACCT;
						private BigDecimal R12_EXCHANGE_RATE;

						 @Id
							@Temporal(TemporalType.DATE)
							@Column(name = "REPORT_DATE")
							private Date REPORT_DATE;

							@Column(name = "REPORT_VERSION", length = 100)
							private BigDecimal REPORT_VERSION;
							
							@Column(name = "REPORT_RESUBDATE")
							private Date REPORT_RESUBDATE;

							@Column(name = "REPORT_FREQUENCY", length = 100)
							private String REPORT_FREQUENCY;

							@Column(name = "REPORT_CODE", length = 100)
							private String REPORT_CODE;

							@Column(name = "REPORT_DESC", length = 100)
							private String REPORT_DESC;

							@Column(name = "ENTITY_FLG", length = 1)
							private String ENTITY_FLG;

							@Column(name = "MODIFY_FLG", length = 1)
							private String MODIFY_FLG;

							@Column(name = "DEL_FLG", length = 1)
							private String DEL_FLG;
							



							public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR6_COMPANY_NAME() {
								return R6_COMPANY_NAME;
							}




							public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
								R6_COMPANY_NAME = r6_COMPANY_NAME;
							}




							public String getR6_COMPANY_REG_NUM() {
								return R6_COMPANY_REG_NUM;
							}




							public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
								R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
							}




							public String getR6_BUSINEES_PHY_ADDRESS() {
								return R6_BUSINEES_PHY_ADDRESS;
							}




							public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
								R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
							}




							public String getR6_POSTAL_ADDRESS() {
								return R6_POSTAL_ADDRESS;
							}




							public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
								R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
							}




							public String getR6_COUNTRY_OF_REG() {
								return R6_COUNTRY_OF_REG;
							}




							public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
								R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
							}




							public String getR6_COMPANY_EMAIL() {
								return R6_COMPANY_EMAIL;
							}




							public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
								R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
							}




							public String getR6_COMPANY_LANDLINE() {
								return R6_COMPANY_LANDLINE;
							}




							public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
								R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
							}




							public String getR6_COMPANY_MOB_PHONE_NUM() {
								return R6_COMPANY_MOB_PHONE_NUM;
							}




							public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
								R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
							}




							public String getR6_PRODUCT_TYPE() {
								return R6_PRODUCT_TYPE;
							}




							public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
								R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
							}




							public BigDecimal getR6_ACCT_NUM() {
								return R6_ACCT_NUM;
							}




							public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
								R6_ACCT_NUM = r6_ACCT_NUM;
							}




							public String getR6_STATUS_OF_ACCT() {
								return R6_STATUS_OF_ACCT;
							}




							public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
								R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
							}




							public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR6_ACCT_BRANCH() {
								return R6_ACCT_BRANCH;
							}




							public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
								R6_ACCT_BRANCH = r6_ACCT_BRANCH;
							}




							public BigDecimal getR6_ACCT_BALANCE_PULA() {
								return R6_ACCT_BALANCE_PULA;
							}




							public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
								R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
							}




							public String getR6_CURRENCY_OF_ACCT() {
								return R6_CURRENCY_OF_ACCT;
							}




							public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
								R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR6_EXCHANGE_RATE() {
								return R6_EXCHANGE_RATE;
							}




							public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
								R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
							}




							public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR7_COMPANY_NAME() {
								return R7_COMPANY_NAME;
							}




							public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
								R7_COMPANY_NAME = r7_COMPANY_NAME;
							}




							public String getR7_COMPANY_REG_NUM() {
								return R7_COMPANY_REG_NUM;
							}




							public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
								R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
							}




							public String getR7_BUSINEES_PHY_ADDRESS() {
								return R7_BUSINEES_PHY_ADDRESS;
							}




							public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
								R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
							}




							public String getR7_POSTAL_ADDRESS() {
								return R7_POSTAL_ADDRESS;
							}




							public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
								R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
							}




							public String getR7_COUNTRY_OF_REG() {
								return R7_COUNTRY_OF_REG;
							}




							public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
								R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
							}




							public String getR7_COMPANY_EMAIL() {
								return R7_COMPANY_EMAIL;
							}




							public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
								R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
							}




							public String getR7_COMPANY_LANDLINE() {
								return R7_COMPANY_LANDLINE;
							}




							public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
								R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
							}




							public String getR7_COMPANY_MOB_PHONE_NUM() {
								return R7_COMPANY_MOB_PHONE_NUM;
							}




							public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
								R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
							}




							public String getR7_PRODUCT_TYPE() {
								return R7_PRODUCT_TYPE;
							}




							public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
								R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
							}




							public BigDecimal getR7_ACCT_NUM() {
								return R7_ACCT_NUM;
							}




							public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
								R7_ACCT_NUM = r7_ACCT_NUM;
							}




							public String getR7_STATUS_OF_ACCT() {
								return R7_STATUS_OF_ACCT;
							}




							public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
								R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
							}




							public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR7_ACCT_BRANCH() {
								return R7_ACCT_BRANCH;
							}




							public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
								R7_ACCT_BRANCH = r7_ACCT_BRANCH;
							}




							public BigDecimal getR7_ACCT_BALANCE_PULA() {
								return R7_ACCT_BALANCE_PULA;
							}




							public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
								R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
							}




							public String getR7_CURRENCY_OF_ACCT() {
								return R7_CURRENCY_OF_ACCT;
							}




							public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
								R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR7_EXCHANGE_RATE() {
								return R7_EXCHANGE_RATE;
							}




							public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
								R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
							}




							public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR8_COMPANY_NAME() {
								return R8_COMPANY_NAME;
							}




							public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
								R8_COMPANY_NAME = r8_COMPANY_NAME;
							}




							public String getR8_COMPANY_REG_NUM() {
								return R8_COMPANY_REG_NUM;
							}




							public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
								R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
							}




							public String getR8_BUSINEES_PHY_ADDRESS() {
								return R8_BUSINEES_PHY_ADDRESS;
							}




							public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
								R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
							}




							public String getR8_POSTAL_ADDRESS() {
								return R8_POSTAL_ADDRESS;
							}




							public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
								R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
							}




							public String getR8_COUNTRY_OF_REG() {
								return R8_COUNTRY_OF_REG;
							}




							public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
								R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
							}




							public String getR8_COMPANY_EMAIL() {
								return R8_COMPANY_EMAIL;
							}




							public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
								R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
							}




							public String getR8_COMPANY_LANDLINE() {
								return R8_COMPANY_LANDLINE;
							}




							public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
								R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
							}




							public String getR8_COMPANY_MOB_PHONE_NUM() {
								return R8_COMPANY_MOB_PHONE_NUM;
							}




							public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
								R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
							}




							public String getR8_PRODUCT_TYPE() {
								return R8_PRODUCT_TYPE;
							}




							public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
								R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
							}




							public BigDecimal getR8_ACCT_NUM() {
								return R8_ACCT_NUM;
							}




							public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
								R8_ACCT_NUM = r8_ACCT_NUM;
							}




							public String getR8_STATUS_OF_ACCT() {
								return R8_STATUS_OF_ACCT;
							}




							public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
								R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
							}




							public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR8_ACCT_BRANCH() {
								return R8_ACCT_BRANCH;
							}




							public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
								R8_ACCT_BRANCH = r8_ACCT_BRANCH;
							}




							public BigDecimal getR8_ACCT_BALANCE_PULA() {
								return R8_ACCT_BALANCE_PULA;
							}




							public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
								R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
							}




							public String getR8_CURRENCY_OF_ACCT() {
								return R8_CURRENCY_OF_ACCT;
							}




							public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
								R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR8_EXCHANGE_RATE() {
								return R8_EXCHANGE_RATE;
							}




							public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
								R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
							}




							public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR9_COMPANY_NAME() {
								return R9_COMPANY_NAME;
							}




							public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
								R9_COMPANY_NAME = r9_COMPANY_NAME;
							}




							public String getR9_COMPANY_REG_NUM() {
								return R9_COMPANY_REG_NUM;
							}




							public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
								R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
							}




							public String getR9_BUSINEES_PHY_ADDRESS() {
								return R9_BUSINEES_PHY_ADDRESS;
							}




							public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
								R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
							}




							public String getR9_POSTAL_ADDRESS() {
								return R9_POSTAL_ADDRESS;
							}




							public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
								R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
							}




							public String getR9_COUNTRY_OF_REG() {
								return R9_COUNTRY_OF_REG;
							}




							public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
								R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
							}




							public String getR9_COMPANY_EMAIL() {
								return R9_COMPANY_EMAIL;
							}




							public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
								R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
							}




							public String getR9_COMPANY_LANDLINE() {
								return R9_COMPANY_LANDLINE;
							}




							public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
								R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
							}




							public String getR9_COMPANY_MOB_PHONE_NUM() {
								return R9_COMPANY_MOB_PHONE_NUM;
							}




							public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
								R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
							}




							public String getR9_PRODUCT_TYPE() {
								return R9_PRODUCT_TYPE;
							}




							public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
								R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
							}




							public BigDecimal getR9_ACCT_NUM() {
								return R9_ACCT_NUM;
							}




							public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
								R9_ACCT_NUM = r9_ACCT_NUM;
							}




							public String getR9_STATUS_OF_ACCT() {
								return R9_STATUS_OF_ACCT;
							}




							public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
								R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
							}




							public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR9_ACCT_BRANCH() {
								return R9_ACCT_BRANCH;
							}




							public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
								R9_ACCT_BRANCH = r9_ACCT_BRANCH;
							}




							public BigDecimal getR9_ACCT_BALANCE_PULA() {
								return R9_ACCT_BALANCE_PULA;
							}




							public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
								R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
							}




							public String getR9_CURRENCY_OF_ACCT() {
								return R9_CURRENCY_OF_ACCT;
							}




							public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
								R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR9_EXCHANGE_RATE() {
								return R9_EXCHANGE_RATE;
							}




							public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
								R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
							}




							public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR10_COMPANY_NAME() {
								return R10_COMPANY_NAME;
							}




							public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
								R10_COMPANY_NAME = r10_COMPANY_NAME;
							}




							public String getR10_COMPANY_REG_NUM() {
								return R10_COMPANY_REG_NUM;
							}




							public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
								R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
							}




							public String getR10_BUSINEES_PHY_ADDRESS() {
								return R10_BUSINEES_PHY_ADDRESS;
							}




							public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
								R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
							}




							public String getR10_POSTAL_ADDRESS() {
								return R10_POSTAL_ADDRESS;
							}




							public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
								R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
							}




							public String getR10_COUNTRY_OF_REG() {
								return R10_COUNTRY_OF_REG;
							}




							public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
								R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
							}




							public String getR10_COMPANY_EMAIL() {
								return R10_COMPANY_EMAIL;
							}




							public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
								R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
							}




							public String getR10_COMPANY_LANDLINE() {
								return R10_COMPANY_LANDLINE;
							}




							public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
								R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
							}




							public String getR10_COMPANY_MOB_PHONE_NUM() {
								return R10_COMPANY_MOB_PHONE_NUM;
							}




							public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
								R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
							}




							public String getR10_PRODUCT_TYPE() {
								return R10_PRODUCT_TYPE;
							}




							public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
								R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
							}




							public BigDecimal getR10_ACCT_NUM() {
								return R10_ACCT_NUM;
							}




							public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
								R10_ACCT_NUM = r10_ACCT_NUM;
							}




							public String getR10_STATUS_OF_ACCT() {
								return R10_STATUS_OF_ACCT;
							}




							public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
								R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
							}




							public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR10_ACCT_BRANCH() {
								return R10_ACCT_BRANCH;
							}




							public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
								R10_ACCT_BRANCH = r10_ACCT_BRANCH;
							}




							public BigDecimal getR10_ACCT_BALANCE_PULA() {
								return R10_ACCT_BALANCE_PULA;
							}




							public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
								R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
							}




							public String getR10_CURRENCY_OF_ACCT() {
								return R10_CURRENCY_OF_ACCT;
							}




							public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
								R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR10_EXCHANGE_RATE() {
								return R10_EXCHANGE_RATE;
							}




							public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
								R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
							}




							public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR11_COMPANY_NAME() {
								return R11_COMPANY_NAME;
							}




							public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
								R11_COMPANY_NAME = r11_COMPANY_NAME;
							}




							public String getR11_COMPANY_REG_NUM() {
								return R11_COMPANY_REG_NUM;
							}




							public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
								R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
							}




							public String getR11_BUSINEES_PHY_ADDRESS() {
								return R11_BUSINEES_PHY_ADDRESS;
							}




							public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
								R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
							}




							public String getR11_POSTAL_ADDRESS() {
								return R11_POSTAL_ADDRESS;
							}




							public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
								R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
							}




							public String getR11_COUNTRY_OF_REG() {
								return R11_COUNTRY_OF_REG;
							}




							public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
								R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
							}




							public String getR11_COMPANY_EMAIL() {
								return R11_COMPANY_EMAIL;
							}




							public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
								R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
							}




							public String getR11_COMPANY_LANDLINE() {
								return R11_COMPANY_LANDLINE;
							}




							public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
								R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
							}




							public String getR11_COMPANY_MOB_PHONE_NUM() {
								return R11_COMPANY_MOB_PHONE_NUM;
							}




							public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
								R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
							}




							public String getR11_PRODUCT_TYPE() {
								return R11_PRODUCT_TYPE;
							}




							public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
								R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
							}




							public BigDecimal getR11_ACCT_NUM() {
								return R11_ACCT_NUM;
							}




							public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
								R11_ACCT_NUM = r11_ACCT_NUM;
							}




							public String getR11_STATUS_OF_ACCT() {
								return R11_STATUS_OF_ACCT;
							}




							public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
								R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
							}




							public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR11_ACCT_BRANCH() {
								return R11_ACCT_BRANCH;
							}




							public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
								R11_ACCT_BRANCH = r11_ACCT_BRANCH;
							}




							public BigDecimal getR11_ACCT_BALANCE_PULA() {
								return R11_ACCT_BALANCE_PULA;
							}




							public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
								R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
							}




							public String getR11_CURRENCY_OF_ACCT() {
								return R11_CURRENCY_OF_ACCT;
							}




							public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
								R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR11_EXCHANGE_RATE() {
								return R11_EXCHANGE_RATE;
							}




							public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
								R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
							}




							public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR12_COMPANY_NAME() {
								return R12_COMPANY_NAME;
							}




							public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
								R12_COMPANY_NAME = r12_COMPANY_NAME;
							}




							public String getR12_COMPANY_REG_NUM() {
								return R12_COMPANY_REG_NUM;
							}




							public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
								R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
							}




							public String getR12_BUSINEES_PHY_ADDRESS() {
								return R12_BUSINEES_PHY_ADDRESS;
							}




							public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
								R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
							}




							public String getR12_POSTAL_ADDRESS() {
								return R12_POSTAL_ADDRESS;
							}




							public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
								R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
							}




							public String getR12_COUNTRY_OF_REG() {
								return R12_COUNTRY_OF_REG;
							}




							public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
								R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
							}




							public String getR12_COMPANY_EMAIL() {
								return R12_COMPANY_EMAIL;
							}




							public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
								R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
							}




							public String getR12_COMPANY_LANDLINE() {
								return R12_COMPANY_LANDLINE;
							}




							public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
								R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
							}




							public String getR12_COMPANY_MOB_PHONE_NUM() {
								return R12_COMPANY_MOB_PHONE_NUM;
							}




							public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
								R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
							}




							public String getR12_PRODUCT_TYPE() {
								return R12_PRODUCT_TYPE;
							}




							public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
								R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
							}




							public BigDecimal getR12_ACCT_NUM() {
								return R12_ACCT_NUM;
							}




							public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
								R12_ACCT_NUM = r12_ACCT_NUM;
							}




							public String getR12_STATUS_OF_ACCT() {
								return R12_STATUS_OF_ACCT;
							}




							public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
								R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
							}




							public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR12_ACCT_BRANCH() {
								return R12_ACCT_BRANCH;
							}




							public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
								R12_ACCT_BRANCH = r12_ACCT_BRANCH;
							}




							public BigDecimal getR12_ACCT_BALANCE_PULA() {
								return R12_ACCT_BALANCE_PULA;
							}




							public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
								R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
							}




							public String getR12_CURRENCY_OF_ACCT() {
								return R12_CURRENCY_OF_ACCT;
							}




							public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
								R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR12_EXCHANGE_RATE() {
								return R12_EXCHANGE_RATE;
							}




							public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
								R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

	                        public Date getREPORT_RESUBDATE() {
								return REPORT_RESUBDATE;
							}

							public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
								this.REPORT_RESUBDATE = REPORT_RESUBDATE;
							}
							public String getREPORT_FREQUENCY() {
								return REPORT_FREQUENCY;
							}

							public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
								REPORT_FREQUENCY = rEPORT_FREQUENCY;
							}

							public String getREPORT_CODE() {
								return REPORT_CODE;
							}

							public void setREPORT_CODE(String rEPORT_CODE) {
								REPORT_CODE = rEPORT_CODE;
							}

							public String getREPORT_DESC() {
								return REPORT_DESC;
							}

							public void setREPORT_DESC(String rEPORT_DESC) {
								REPORT_DESC = rEPORT_DESC;
							}

							public String getENTITY_FLG() {
								return ENTITY_FLG;
							}

							public void setENTITY_FLG(String eNTITY_FLG) {
								ENTITY_FLG = eNTITY_FLG;
							}

							public String getMODIFY_FLG() {
								return MODIFY_FLG;
							}

							public void setMODIFY_FLG(String mODIFY_FLG) {
								MODIFY_FLG = mODIFY_FLG;
							}

							public String getDEL_FLG() {
								return DEL_FLG;
							}

							public void setDEL_FLG(String dEL_FLG) {
								DEL_FLG = dEL_FLG;
							}

						}
		

	
		// ROW MAPPER ARCHIVAL DETAIL

		class BDISB2RowMapper_ArchivalDetail implements RowMapper<BDISB2_Archival_Detail_Entity> {

			@Override
			public BDISB2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				BDISB2_Archival_Detail_Entity obj = new BDISB2_Archival_Detail_Entity();

				// ===================== R6 =====================
				obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
				obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
				obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
				obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
				obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
				obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
				obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
				obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
				obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
				obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
				obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
				obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
				obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
				obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
				obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

				// ===================== R7 =====================
				obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
				obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
				obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
				obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
				obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
				obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
				obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
				obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
				obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
				obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
				obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
				obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
				obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
				obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
				obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

				// ===================== R8 =====================
				obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
				obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
				obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
				obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
				obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
				obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
				obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
				obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
				obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
				obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
				obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
				obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
				obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
				obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
				obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

				// ===================== R9 =====================
				obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
				obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
				obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
				obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
				obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
				obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
				obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
				obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
				obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
				obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
				obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
				obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
				obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
				obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
				obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

				// ===================== R10 =====================
				obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
				obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
				obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
				obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
				obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
				obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
				obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
				obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
				obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
				obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
				obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
				obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
				obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
				obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
				obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

				// ===================== R11 =====================
				obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
				obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
				obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
				obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
				obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
				obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
				obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
				obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
				obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
				obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
				obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
				obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
				obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
				obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
				obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

				// ===================== R12 =====================
				obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
				obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
				obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
				obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
				obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
				obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
				obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
				obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
				obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
				obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
				obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
				obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
				obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
				obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
				obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

				// COMMON FIELDS
				obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
				obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
				obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
				obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
				obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
				obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
				obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
				obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
				obj.setDEL_FLG(rs.getString("DEL_FLG"));

				return obj;
			}
		}

		public static class BDISB2_Archival_Detail_Entity {
					
						private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R6_COMPANY_NAME;
						private String R6_COMPANY_REG_NUM;
						private String R6_BUSINEES_PHY_ADDRESS;
						private String R6_POSTAL_ADDRESS;
						private String R6_COUNTRY_OF_REG;
						private String R6_COMPANY_EMAIL;
						private String R6_COMPANY_LANDLINE;
						private String R6_COMPANY_MOB_PHONE_NUM;
						private String R6_PRODUCT_TYPE;
						private BigDecimal R6_ACCT_NUM;
						private String R6_STATUS_OF_ACCT;
						private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R6_ACCT_BRANCH;
						private BigDecimal R6_ACCT_BALANCE_PULA;
						private String R6_CURRENCY_OF_ACCT;
						private BigDecimal R6_EXCHANGE_RATE;
						private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R7_COMPANY_NAME;
						private String R7_COMPANY_REG_NUM;
						private String R7_BUSINEES_PHY_ADDRESS;
						private String R7_POSTAL_ADDRESS;
						private String R7_COUNTRY_OF_REG;
						private String R7_COMPANY_EMAIL;
						private String R7_COMPANY_LANDLINE;
						private String R7_COMPANY_MOB_PHONE_NUM;
						private String R7_PRODUCT_TYPE;
						private BigDecimal R7_ACCT_NUM;
						private String R7_STATUS_OF_ACCT;
						private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R7_ACCT_BRANCH;
						private BigDecimal R7_ACCT_BALANCE_PULA;
						private String R7_CURRENCY_OF_ACCT;
						private BigDecimal R7_EXCHANGE_RATE;
						private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R8_COMPANY_NAME;
						private String R8_COMPANY_REG_NUM;
						private String R8_BUSINEES_PHY_ADDRESS;
						private String R8_POSTAL_ADDRESS;
						private String R8_COUNTRY_OF_REG;
						private String R8_COMPANY_EMAIL;
						private String R8_COMPANY_LANDLINE;
						private String R8_COMPANY_MOB_PHONE_NUM;
						private String R8_PRODUCT_TYPE;
						private BigDecimal R8_ACCT_NUM;
						private String R8_STATUS_OF_ACCT;
						private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R8_ACCT_BRANCH;
						private BigDecimal R8_ACCT_BALANCE_PULA;
						private String R8_CURRENCY_OF_ACCT;
						private BigDecimal R8_EXCHANGE_RATE;
						private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R9_COMPANY_NAME;
						private String R9_COMPANY_REG_NUM;
						private String R9_BUSINEES_PHY_ADDRESS;
						private String R9_POSTAL_ADDRESS;
						private String R9_COUNTRY_OF_REG;
						private String R9_COMPANY_EMAIL;
						private String R9_COMPANY_LANDLINE;
						private String R9_COMPANY_MOB_PHONE_NUM;
						private String R9_PRODUCT_TYPE;
						private BigDecimal R9_ACCT_NUM;
						private String R9_STATUS_OF_ACCT;
						private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R9_ACCT_BRANCH;
						private BigDecimal R9_ACCT_BALANCE_PULA;
						private String R9_CURRENCY_OF_ACCT;
						private BigDecimal R9_EXCHANGE_RATE;
						private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R10_COMPANY_NAME;
						private String R10_COMPANY_REG_NUM;
						private String R10_BUSINEES_PHY_ADDRESS;
						private String R10_POSTAL_ADDRESS;
						private String R10_COUNTRY_OF_REG;
						private String R10_COMPANY_EMAIL;
						private String R10_COMPANY_LANDLINE;
						private String R10_COMPANY_MOB_PHONE_NUM;
						private String R10_PRODUCT_TYPE;
						private BigDecimal R10_ACCT_NUM;
						private String R10_STATUS_OF_ACCT;
						private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R10_ACCT_BRANCH;
						private BigDecimal R10_ACCT_BALANCE_PULA;
						private String R10_CURRENCY_OF_ACCT;
						private BigDecimal R10_EXCHANGE_RATE;
						private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R11_COMPANY_NAME;
						private String R11_COMPANY_REG_NUM;
						private String R11_BUSINEES_PHY_ADDRESS;
						private String R11_POSTAL_ADDRESS;
						private String R11_COUNTRY_OF_REG;
						private String R11_COMPANY_EMAIL;
						private String R11_COMPANY_LANDLINE;
						private String R11_COMPANY_MOB_PHONE_NUM;
						private String R11_PRODUCT_TYPE;
						private BigDecimal R11_ACCT_NUM;
						private String R11_STATUS_OF_ACCT;
						private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R11_ACCT_BRANCH;
						private BigDecimal R11_ACCT_BALANCE_PULA;
						private String R11_CURRENCY_OF_ACCT;
						private BigDecimal R11_EXCHANGE_RATE;
						private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R12_COMPANY_NAME;
						private String R12_COMPANY_REG_NUM;
						private String R12_BUSINEES_PHY_ADDRESS;
						private String R12_POSTAL_ADDRESS;
						private String R12_COUNTRY_OF_REG;
						private String R12_COMPANY_EMAIL;
						private String R12_COMPANY_LANDLINE;
						private String R12_COMPANY_MOB_PHONE_NUM;
						private String R12_PRODUCT_TYPE;
						private BigDecimal R12_ACCT_NUM;
						private String R12_STATUS_OF_ACCT;
						private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R12_ACCT_BRANCH;
						private BigDecimal R12_ACCT_BALANCE_PULA;
						private String R12_CURRENCY_OF_ACCT;
						private BigDecimal R12_EXCHANGE_RATE;

						 @Id
							@Temporal(TemporalType.DATE)
							@Column(name = "REPORT_DATE")
							private Date REPORT_DATE;

							@Column(name = "REPORT_VERSION", length = 100)
							private BigDecimal REPORT_VERSION;
							
							@Column(name = "REPORT_RESUBDATE")
							private Date REPORT_RESUBDATE;

							@Column(name = "REPORT_FREQUENCY", length = 100)
							private String REPORT_FREQUENCY;

							@Column(name = "REPORT_CODE", length = 100)
							private String REPORT_CODE;

							@Column(name = "REPORT_DESC", length = 100)
							private String REPORT_DESC;

							@Column(name = "ENTITY_FLG", length = 1)
							private String ENTITY_FLG;

							@Column(name = "MODIFY_FLG", length = 1)
							private String MODIFY_FLG;

							@Column(name = "DEL_FLG", length = 1)
							private String DEL_FLG;
							



							public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR6_COMPANY_NAME() {
								return R6_COMPANY_NAME;
							}




							public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
								R6_COMPANY_NAME = r6_COMPANY_NAME;
							}




							public String getR6_COMPANY_REG_NUM() {
								return R6_COMPANY_REG_NUM;
							}




							public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
								R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
							}




							public String getR6_BUSINEES_PHY_ADDRESS() {
								return R6_BUSINEES_PHY_ADDRESS;
							}




							public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
								R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
							}




							public String getR6_POSTAL_ADDRESS() {
								return R6_POSTAL_ADDRESS;
							}




							public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
								R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
							}




							public String getR6_COUNTRY_OF_REG() {
								return R6_COUNTRY_OF_REG;
							}




							public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
								R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
							}




							public String getR6_COMPANY_EMAIL() {
								return R6_COMPANY_EMAIL;
							}




							public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
								R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
							}




							public String getR6_COMPANY_LANDLINE() {
								return R6_COMPANY_LANDLINE;
							}




							public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
								R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
							}




							public String getR6_COMPANY_MOB_PHONE_NUM() {
								return R6_COMPANY_MOB_PHONE_NUM;
							}




							public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
								R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
							}




							public String getR6_PRODUCT_TYPE() {
								return R6_PRODUCT_TYPE;
							}




							public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
								R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
							}




							public BigDecimal getR6_ACCT_NUM() {
								return R6_ACCT_NUM;
							}




							public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
								R6_ACCT_NUM = r6_ACCT_NUM;
							}




							public String getR6_STATUS_OF_ACCT() {
								return R6_STATUS_OF_ACCT;
							}




							public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
								R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
							}




							public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR6_ACCT_BRANCH() {
								return R6_ACCT_BRANCH;
							}




							public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
								R6_ACCT_BRANCH = r6_ACCT_BRANCH;
							}




							public BigDecimal getR6_ACCT_BALANCE_PULA() {
								return R6_ACCT_BALANCE_PULA;
							}




							public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
								R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
							}




							public String getR6_CURRENCY_OF_ACCT() {
								return R6_CURRENCY_OF_ACCT;
							}




							public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
								R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR6_EXCHANGE_RATE() {
								return R6_EXCHANGE_RATE;
							}




							public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
								R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
							}




							public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR7_COMPANY_NAME() {
								return R7_COMPANY_NAME;
							}




							public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
								R7_COMPANY_NAME = r7_COMPANY_NAME;
							}




							public String getR7_COMPANY_REG_NUM() {
								return R7_COMPANY_REG_NUM;
							}




							public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
								R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
							}




							public String getR7_BUSINEES_PHY_ADDRESS() {
								return R7_BUSINEES_PHY_ADDRESS;
							}




							public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
								R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
							}




							public String getR7_POSTAL_ADDRESS() {
								return R7_POSTAL_ADDRESS;
							}




							public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
								R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
							}




							public String getR7_COUNTRY_OF_REG() {
								return R7_COUNTRY_OF_REG;
							}




							public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
								R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
							}




							public String getR7_COMPANY_EMAIL() {
								return R7_COMPANY_EMAIL;
							}




							public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
								R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
							}




							public String getR7_COMPANY_LANDLINE() {
								return R7_COMPANY_LANDLINE;
							}




							public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
								R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
							}




							public String getR7_COMPANY_MOB_PHONE_NUM() {
								return R7_COMPANY_MOB_PHONE_NUM;
							}




							public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
								R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
							}




							public String getR7_PRODUCT_TYPE() {
								return R7_PRODUCT_TYPE;
							}




							public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
								R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
							}




							public BigDecimal getR7_ACCT_NUM() {
								return R7_ACCT_NUM;
							}




							public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
								R7_ACCT_NUM = r7_ACCT_NUM;
							}




							public String getR7_STATUS_OF_ACCT() {
								return R7_STATUS_OF_ACCT;
							}




							public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
								R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
							}




							public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR7_ACCT_BRANCH() {
								return R7_ACCT_BRANCH;
							}




							public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
								R7_ACCT_BRANCH = r7_ACCT_BRANCH;
							}




							public BigDecimal getR7_ACCT_BALANCE_PULA() {
								return R7_ACCT_BALANCE_PULA;
							}




							public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
								R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
							}




							public String getR7_CURRENCY_OF_ACCT() {
								return R7_CURRENCY_OF_ACCT;
							}




							public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
								R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR7_EXCHANGE_RATE() {
								return R7_EXCHANGE_RATE;
							}




							public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
								R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
							}




							public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR8_COMPANY_NAME() {
								return R8_COMPANY_NAME;
							}




							public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
								R8_COMPANY_NAME = r8_COMPANY_NAME;
							}




							public String getR8_COMPANY_REG_NUM() {
								return R8_COMPANY_REG_NUM;
							}




							public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
								R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
							}




							public String getR8_BUSINEES_PHY_ADDRESS() {
								return R8_BUSINEES_PHY_ADDRESS;
							}




							public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
								R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
							}




							public String getR8_POSTAL_ADDRESS() {
								return R8_POSTAL_ADDRESS;
							}




							public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
								R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
							}




							public String getR8_COUNTRY_OF_REG() {
								return R8_COUNTRY_OF_REG;
							}




							public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
								R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
							}




							public String getR8_COMPANY_EMAIL() {
								return R8_COMPANY_EMAIL;
							}




							public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
								R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
							}




							public String getR8_COMPANY_LANDLINE() {
								return R8_COMPANY_LANDLINE;
							}




							public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
								R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
							}




							public String getR8_COMPANY_MOB_PHONE_NUM() {
								return R8_COMPANY_MOB_PHONE_NUM;
							}




							public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
								R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
							}




							public String getR8_PRODUCT_TYPE() {
								return R8_PRODUCT_TYPE;
							}




							public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
								R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
							}




							public BigDecimal getR8_ACCT_NUM() {
								return R8_ACCT_NUM;
							}




							public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
								R8_ACCT_NUM = r8_ACCT_NUM;
							}




							public String getR8_STATUS_OF_ACCT() {
								return R8_STATUS_OF_ACCT;
							}




							public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
								R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
							}




							public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR8_ACCT_BRANCH() {
								return R8_ACCT_BRANCH;
							}




							public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
								R8_ACCT_BRANCH = r8_ACCT_BRANCH;
							}




							public BigDecimal getR8_ACCT_BALANCE_PULA() {
								return R8_ACCT_BALANCE_PULA;
							}




							public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
								R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
							}




							public String getR8_CURRENCY_OF_ACCT() {
								return R8_CURRENCY_OF_ACCT;
							}




							public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
								R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR8_EXCHANGE_RATE() {
								return R8_EXCHANGE_RATE;
							}




							public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
								R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
							}




							public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR9_COMPANY_NAME() {
								return R9_COMPANY_NAME;
							}




							public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
								R9_COMPANY_NAME = r9_COMPANY_NAME;
							}




							public String getR9_COMPANY_REG_NUM() {
								return R9_COMPANY_REG_NUM;
							}




							public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
								R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
							}




							public String getR9_BUSINEES_PHY_ADDRESS() {
								return R9_BUSINEES_PHY_ADDRESS;
							}




							public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
								R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
							}




							public String getR9_POSTAL_ADDRESS() {
								return R9_POSTAL_ADDRESS;
							}




							public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
								R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
							}




							public String getR9_COUNTRY_OF_REG() {
								return R9_COUNTRY_OF_REG;
							}




							public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
								R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
							}




							public String getR9_COMPANY_EMAIL() {
								return R9_COMPANY_EMAIL;
							}




							public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
								R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
							}




							public String getR9_COMPANY_LANDLINE() {
								return R9_COMPANY_LANDLINE;
							}




							public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
								R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
							}




							public String getR9_COMPANY_MOB_PHONE_NUM() {
								return R9_COMPANY_MOB_PHONE_NUM;
							}




							public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
								R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
							}




							public String getR9_PRODUCT_TYPE() {
								return R9_PRODUCT_TYPE;
							}




							public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
								R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
							}




							public BigDecimal getR9_ACCT_NUM() {
								return R9_ACCT_NUM;
							}




							public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
								R9_ACCT_NUM = r9_ACCT_NUM;
							}




							public String getR9_STATUS_OF_ACCT() {
								return R9_STATUS_OF_ACCT;
							}




							public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
								R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
							}




							public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR9_ACCT_BRANCH() {
								return R9_ACCT_BRANCH;
							}




							public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
								R9_ACCT_BRANCH = r9_ACCT_BRANCH;
							}




							public BigDecimal getR9_ACCT_BALANCE_PULA() {
								return R9_ACCT_BALANCE_PULA;
							}




							public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
								R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
							}




							public String getR9_CURRENCY_OF_ACCT() {
								return R9_CURRENCY_OF_ACCT;
							}




							public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
								R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR9_EXCHANGE_RATE() {
								return R9_EXCHANGE_RATE;
							}




							public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
								R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
							}




							public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR10_COMPANY_NAME() {
								return R10_COMPANY_NAME;
							}




							public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
								R10_COMPANY_NAME = r10_COMPANY_NAME;
							}




							public String getR10_COMPANY_REG_NUM() {
								return R10_COMPANY_REG_NUM;
							}




							public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
								R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
							}




							public String getR10_BUSINEES_PHY_ADDRESS() {
								return R10_BUSINEES_PHY_ADDRESS;
							}




							public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
								R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
							}




							public String getR10_POSTAL_ADDRESS() {
								return R10_POSTAL_ADDRESS;
							}




							public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
								R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
							}




							public String getR10_COUNTRY_OF_REG() {
								return R10_COUNTRY_OF_REG;
							}




							public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
								R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
							}




							public String getR10_COMPANY_EMAIL() {
								return R10_COMPANY_EMAIL;
							}




							public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
								R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
							}




							public String getR10_COMPANY_LANDLINE() {
								return R10_COMPANY_LANDLINE;
							}




							public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
								R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
							}




							public String getR10_COMPANY_MOB_PHONE_NUM() {
								return R10_COMPANY_MOB_PHONE_NUM;
							}




							public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
								R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
							}




							public String getR10_PRODUCT_TYPE() {
								return R10_PRODUCT_TYPE;
							}




							public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
								R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
							}




							public BigDecimal getR10_ACCT_NUM() {
								return R10_ACCT_NUM;
							}




							public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
								R10_ACCT_NUM = r10_ACCT_NUM;
							}




							public String getR10_STATUS_OF_ACCT() {
								return R10_STATUS_OF_ACCT;
							}




							public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
								R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
							}




							public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR10_ACCT_BRANCH() {
								return R10_ACCT_BRANCH;
							}




							public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
								R10_ACCT_BRANCH = r10_ACCT_BRANCH;
							}




							public BigDecimal getR10_ACCT_BALANCE_PULA() {
								return R10_ACCT_BALANCE_PULA;
							}




							public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
								R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
							}




							public String getR10_CURRENCY_OF_ACCT() {
								return R10_CURRENCY_OF_ACCT;
							}




							public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
								R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR10_EXCHANGE_RATE() {
								return R10_EXCHANGE_RATE;
							}




							public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
								R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
							}




							public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR11_COMPANY_NAME() {
								return R11_COMPANY_NAME;
							}




							public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
								R11_COMPANY_NAME = r11_COMPANY_NAME;
							}




							public String getR11_COMPANY_REG_NUM() {
								return R11_COMPANY_REG_NUM;
							}




							public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
								R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
							}




							public String getR11_BUSINEES_PHY_ADDRESS() {
								return R11_BUSINEES_PHY_ADDRESS;
							}




							public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
								R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
							}




							public String getR11_POSTAL_ADDRESS() {
								return R11_POSTAL_ADDRESS;
							}




							public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
								R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
							}




							public String getR11_COUNTRY_OF_REG() {
								return R11_COUNTRY_OF_REG;
							}




							public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
								R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
							}




							public String getR11_COMPANY_EMAIL() {
								return R11_COMPANY_EMAIL;
							}




							public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
								R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
							}




							public String getR11_COMPANY_LANDLINE() {
								return R11_COMPANY_LANDLINE;
							}




							public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
								R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
							}




							public String getR11_COMPANY_MOB_PHONE_NUM() {
								return R11_COMPANY_MOB_PHONE_NUM;
							}




							public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
								R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
							}




							public String getR11_PRODUCT_TYPE() {
								return R11_PRODUCT_TYPE;
							}




							public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
								R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
							}




							public BigDecimal getR11_ACCT_NUM() {
								return R11_ACCT_NUM;
							}




							public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
								R11_ACCT_NUM = r11_ACCT_NUM;
							}




							public String getR11_STATUS_OF_ACCT() {
								return R11_STATUS_OF_ACCT;
							}




							public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
								R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
							}




							public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR11_ACCT_BRANCH() {
								return R11_ACCT_BRANCH;
							}




							public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
								R11_ACCT_BRANCH = r11_ACCT_BRANCH;
							}




							public BigDecimal getR11_ACCT_BALANCE_PULA() {
								return R11_ACCT_BALANCE_PULA;
							}




							public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
								R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
							}




							public String getR11_CURRENCY_OF_ACCT() {
								return R11_CURRENCY_OF_ACCT;
							}




							public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
								R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR11_EXCHANGE_RATE() {
								return R11_EXCHANGE_RATE;
							}




							public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
								R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
							}




							public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR12_COMPANY_NAME() {
								return R12_COMPANY_NAME;
							}




							public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
								R12_COMPANY_NAME = r12_COMPANY_NAME;
							}




							public String getR12_COMPANY_REG_NUM() {
								return R12_COMPANY_REG_NUM;
							}




							public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
								R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
							}




							public String getR12_BUSINEES_PHY_ADDRESS() {
								return R12_BUSINEES_PHY_ADDRESS;
							}




							public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
								R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
							}




							public String getR12_POSTAL_ADDRESS() {
								return R12_POSTAL_ADDRESS;
							}




							public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
								R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
							}




							public String getR12_COUNTRY_OF_REG() {
								return R12_COUNTRY_OF_REG;
							}




							public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
								R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
							}




							public String getR12_COMPANY_EMAIL() {
								return R12_COMPANY_EMAIL;
							}




							public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
								R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
							}




							public String getR12_COMPANY_LANDLINE() {
								return R12_COMPANY_LANDLINE;
							}




							public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
								R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
							}




							public String getR12_COMPANY_MOB_PHONE_NUM() {
								return R12_COMPANY_MOB_PHONE_NUM;
							}




							public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
								R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
							}




							public String getR12_PRODUCT_TYPE() {
								return R12_PRODUCT_TYPE;
							}




							public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
								R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
							}




							public BigDecimal getR12_ACCT_NUM() {
								return R12_ACCT_NUM;
							}




							public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
								R12_ACCT_NUM = r12_ACCT_NUM;
							}




							public String getR12_STATUS_OF_ACCT() {
								return R12_STATUS_OF_ACCT;
							}




							public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
								R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
							}




							public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR12_ACCT_BRANCH() {
								return R12_ACCT_BRANCH;
							}




							public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
								R12_ACCT_BRANCH = r12_ACCT_BRANCH;
							}




							public BigDecimal getR12_ACCT_BALANCE_PULA() {
								return R12_ACCT_BALANCE_PULA;
							}




							public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
								R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
							}




							public String getR12_CURRENCY_OF_ACCT() {
								return R12_CURRENCY_OF_ACCT;
							}




							public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
								R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR12_EXCHANGE_RATE() {
								return R12_EXCHANGE_RATE;
							}




							public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
								R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

	                        public Date getREPORT_RESUBDATE() {
								return REPORT_RESUBDATE;
							}

							public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
								this.REPORT_RESUBDATE = REPORT_RESUBDATE;
							}
							public String getREPORT_FREQUENCY() {
								return REPORT_FREQUENCY;
							}

							public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
								REPORT_FREQUENCY = rEPORT_FREQUENCY;
							}

							public String getREPORT_CODE() {
								return REPORT_CODE;
							}

							public void setREPORT_CODE(String rEPORT_CODE) {
								REPORT_CODE = rEPORT_CODE;
							}

							public String getREPORT_DESC() {
								return REPORT_DESC;
							}

							public void setREPORT_DESC(String rEPORT_DESC) {
								REPORT_DESC = rEPORT_DESC;
							}

							public String getENTITY_FLG() {
								return ENTITY_FLG;
							}

							public void setENTITY_FLG(String eNTITY_FLG) {
								ENTITY_FLG = eNTITY_FLG;
							}

							public String getMODIFY_FLG() {
								return MODIFY_FLG;
							}

							public void setMODIFY_FLG(String mODIFY_FLG) {
								MODIFY_FLG = mODIFY_FLG;
							}

							public String getDEL_FLG() {
								return DEL_FLG;
							}

							public void setDEL_FLG(String dEL_FLG) {
								DEL_FLG = dEL_FLG;
							}

						}
		

		// ROW MAPPER RESUB SUMMARY

		class BDISB2_RowMapper_Resub implements RowMapper<BDISB2_RESUB_Summary_Entity> {

			@Override
			public BDISB2_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				BDISB2_RESUB_Summary_Entity obj = new BDISB2_RESUB_Summary_Entity();

				// ===================== R6 =====================
				obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
				obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
				obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
				obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
				obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
				obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
				obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
				obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
				obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
				obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
				obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
				obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
				obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
				obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
				obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

				// ===================== R7 =====================
				obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
				obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
				obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
				obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
				obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
				obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
				obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
				obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
				obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
				obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
				obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
				obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
				obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
				obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
				obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

				// ===================== R8 =====================
				obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
				obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
				obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
				obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
				obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
				obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
				obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
				obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
				obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
				obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
				obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
				obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
				obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
				obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
				obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

				// ===================== R9 =====================
				obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
				obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
				obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
				obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
				obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
				obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
				obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
				obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
				obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
				obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
				obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
				obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
				obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
				obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
				obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

				// ===================== R10 =====================
				obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
				obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
				obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
				obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
				obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
				obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
				obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
				obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
				obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
				obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
				obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
				obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
				obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
				obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
				obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

				// ===================== R11 =====================
				obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
				obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
				obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
				obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
				obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
				obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
				obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
				obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
				obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
				obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
				obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
				obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
				obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
				obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
				obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

				// ===================== R12 =====================
				obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
				obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
				obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
				obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
				obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
				obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
				obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
				obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
				obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
				obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
				obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
				obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
				obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
				obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
				obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

				// COMMON FIELDS
				obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
				obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
				obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
				obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
				obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
				obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
				obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
				obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
				obj.setDEL_FLG(rs.getString("DEL_FLG"));

				return obj;
			}
		}

		public static class BDISB2_RESUB_Summary_Entity {
					
						private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R6_COMPANY_NAME;
						private String R6_COMPANY_REG_NUM;
						private String R6_BUSINEES_PHY_ADDRESS;
						private String R6_POSTAL_ADDRESS;
						private String R6_COUNTRY_OF_REG;
						private String R6_COMPANY_EMAIL;
						private String R6_COMPANY_LANDLINE;
						private String R6_COMPANY_MOB_PHONE_NUM;
						private String R6_PRODUCT_TYPE;
						private BigDecimal R6_ACCT_NUM;
						private String R6_STATUS_OF_ACCT;
						private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R6_ACCT_BRANCH;
						private BigDecimal R6_ACCT_BALANCE_PULA;
						private String R6_CURRENCY_OF_ACCT;
						private BigDecimal R6_EXCHANGE_RATE;
						private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R7_COMPANY_NAME;
						private String R7_COMPANY_REG_NUM;
						private String R7_BUSINEES_PHY_ADDRESS;
						private String R7_POSTAL_ADDRESS;
						private String R7_COUNTRY_OF_REG;
						private String R7_COMPANY_EMAIL;
						private String R7_COMPANY_LANDLINE;
						private String R7_COMPANY_MOB_PHONE_NUM;
						private String R7_PRODUCT_TYPE;
						private BigDecimal R7_ACCT_NUM;
						private String R7_STATUS_OF_ACCT;
						private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R7_ACCT_BRANCH;
						private BigDecimal R7_ACCT_BALANCE_PULA;
						private String R7_CURRENCY_OF_ACCT;
						private BigDecimal R7_EXCHANGE_RATE;
						private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R8_COMPANY_NAME;
						private String R8_COMPANY_REG_NUM;
						private String R8_BUSINEES_PHY_ADDRESS;
						private String R8_POSTAL_ADDRESS;
						private String R8_COUNTRY_OF_REG;
						private String R8_COMPANY_EMAIL;
						private String R8_COMPANY_LANDLINE;
						private String R8_COMPANY_MOB_PHONE_NUM;
						private String R8_PRODUCT_TYPE;
						private BigDecimal R8_ACCT_NUM;
						private String R8_STATUS_OF_ACCT;
						private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R8_ACCT_BRANCH;
						private BigDecimal R8_ACCT_BALANCE_PULA;
						private String R8_CURRENCY_OF_ACCT;
						private BigDecimal R8_EXCHANGE_RATE;
						private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R9_COMPANY_NAME;
						private String R9_COMPANY_REG_NUM;
						private String R9_BUSINEES_PHY_ADDRESS;
						private String R9_POSTAL_ADDRESS;
						private String R9_COUNTRY_OF_REG;
						private String R9_COMPANY_EMAIL;
						private String R9_COMPANY_LANDLINE;
						private String R9_COMPANY_MOB_PHONE_NUM;
						private String R9_PRODUCT_TYPE;
						private BigDecimal R9_ACCT_NUM;
						private String R9_STATUS_OF_ACCT;
						private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R9_ACCT_BRANCH;
						private BigDecimal R9_ACCT_BALANCE_PULA;
						private String R9_CURRENCY_OF_ACCT;
						private BigDecimal R9_EXCHANGE_RATE;
						private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R10_COMPANY_NAME;
						private String R10_COMPANY_REG_NUM;
						private String R10_BUSINEES_PHY_ADDRESS;
						private String R10_POSTAL_ADDRESS;
						private String R10_COUNTRY_OF_REG;
						private String R10_COMPANY_EMAIL;
						private String R10_COMPANY_LANDLINE;
						private String R10_COMPANY_MOB_PHONE_NUM;
						private String R10_PRODUCT_TYPE;
						private BigDecimal R10_ACCT_NUM;
						private String R10_STATUS_OF_ACCT;
						private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R10_ACCT_BRANCH;
						private BigDecimal R10_ACCT_BALANCE_PULA;
						private String R10_CURRENCY_OF_ACCT;
						private BigDecimal R10_EXCHANGE_RATE;
						private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R11_COMPANY_NAME;
						private String R11_COMPANY_REG_NUM;
						private String R11_BUSINEES_PHY_ADDRESS;
						private String R11_POSTAL_ADDRESS;
						private String R11_COUNTRY_OF_REG;
						private String R11_COMPANY_EMAIL;
						private String R11_COMPANY_LANDLINE;
						private String R11_COMPANY_MOB_PHONE_NUM;
						private String R11_PRODUCT_TYPE;
						private BigDecimal R11_ACCT_NUM;
						private String R11_STATUS_OF_ACCT;
						private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R11_ACCT_BRANCH;
						private BigDecimal R11_ACCT_BALANCE_PULA;
						private String R11_CURRENCY_OF_ACCT;
						private BigDecimal R11_EXCHANGE_RATE;
						private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R12_COMPANY_NAME;
						private String R12_COMPANY_REG_NUM;
						private String R12_BUSINEES_PHY_ADDRESS;
						private String R12_POSTAL_ADDRESS;
						private String R12_COUNTRY_OF_REG;
						private String R12_COMPANY_EMAIL;
						private String R12_COMPANY_LANDLINE;
						private String R12_COMPANY_MOB_PHONE_NUM;
						private String R12_PRODUCT_TYPE;
						private BigDecimal R12_ACCT_NUM;
						private String R12_STATUS_OF_ACCT;
						private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R12_ACCT_BRANCH;
						private BigDecimal R12_ACCT_BALANCE_PULA;
						private String R12_CURRENCY_OF_ACCT;
						private BigDecimal R12_EXCHANGE_RATE;

						 @Id
							@Temporal(TemporalType.DATE)
							@Column(name = "REPORT_DATE")
							private Date REPORT_DATE;

							@Column(name = "REPORT_VERSION", length = 100)
							private BigDecimal REPORT_VERSION;
							
							@Column(name = "REPORT_RESUBDATE")
							private Date REPORT_RESUBDATE;

							@Column(name = "REPORT_FREQUENCY", length = 100)
							private String REPORT_FREQUENCY;

							@Column(name = "REPORT_CODE", length = 100)
							private String REPORT_CODE;

							@Column(name = "REPORT_DESC", length = 100)
							private String REPORT_DESC;

							@Column(name = "ENTITY_FLG", length = 1)
							private String ENTITY_FLG;

							@Column(name = "MODIFY_FLG", length = 1)
							private String MODIFY_FLG;

							@Column(name = "DEL_FLG", length = 1)
							private String DEL_FLG;
							



							public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR6_COMPANY_NAME() {
								return R6_COMPANY_NAME;
							}




							public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
								R6_COMPANY_NAME = r6_COMPANY_NAME;
							}




							public String getR6_COMPANY_REG_NUM() {
								return R6_COMPANY_REG_NUM;
							}




							public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
								R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
							}




							public String getR6_BUSINEES_PHY_ADDRESS() {
								return R6_BUSINEES_PHY_ADDRESS;
							}




							public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
								R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
							}




							public String getR6_POSTAL_ADDRESS() {
								return R6_POSTAL_ADDRESS;
							}




							public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
								R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
							}




							public String getR6_COUNTRY_OF_REG() {
								return R6_COUNTRY_OF_REG;
							}




							public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
								R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
							}




							public String getR6_COMPANY_EMAIL() {
								return R6_COMPANY_EMAIL;
							}




							public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
								R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
							}




							public String getR6_COMPANY_LANDLINE() {
								return R6_COMPANY_LANDLINE;
							}




							public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
								R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
							}




							public String getR6_COMPANY_MOB_PHONE_NUM() {
								return R6_COMPANY_MOB_PHONE_NUM;
							}




							public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
								R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
							}




							public String getR6_PRODUCT_TYPE() {
								return R6_PRODUCT_TYPE;
							}




							public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
								R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
							}




							public BigDecimal getR6_ACCT_NUM() {
								return R6_ACCT_NUM;
							}




							public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
								R6_ACCT_NUM = r6_ACCT_NUM;
							}




							public String getR6_STATUS_OF_ACCT() {
								return R6_STATUS_OF_ACCT;
							}




							public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
								R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
							}




							public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR6_ACCT_BRANCH() {
								return R6_ACCT_BRANCH;
							}




							public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
								R6_ACCT_BRANCH = r6_ACCT_BRANCH;
							}




							public BigDecimal getR6_ACCT_BALANCE_PULA() {
								return R6_ACCT_BALANCE_PULA;
							}




							public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
								R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
							}




							public String getR6_CURRENCY_OF_ACCT() {
								return R6_CURRENCY_OF_ACCT;
							}




							public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
								R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR6_EXCHANGE_RATE() {
								return R6_EXCHANGE_RATE;
							}




							public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
								R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
							}




							public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR7_COMPANY_NAME() {
								return R7_COMPANY_NAME;
							}




							public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
								R7_COMPANY_NAME = r7_COMPANY_NAME;
							}




							public String getR7_COMPANY_REG_NUM() {
								return R7_COMPANY_REG_NUM;
							}




							public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
								R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
							}




							public String getR7_BUSINEES_PHY_ADDRESS() {
								return R7_BUSINEES_PHY_ADDRESS;
							}




							public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
								R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
							}




							public String getR7_POSTAL_ADDRESS() {
								return R7_POSTAL_ADDRESS;
							}




							public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
								R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
							}




							public String getR7_COUNTRY_OF_REG() {
								return R7_COUNTRY_OF_REG;
							}




							public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
								R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
							}




							public String getR7_COMPANY_EMAIL() {
								return R7_COMPANY_EMAIL;
							}




							public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
								R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
							}




							public String getR7_COMPANY_LANDLINE() {
								return R7_COMPANY_LANDLINE;
							}




							public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
								R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
							}




							public String getR7_COMPANY_MOB_PHONE_NUM() {
								return R7_COMPANY_MOB_PHONE_NUM;
							}




							public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
								R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
							}




							public String getR7_PRODUCT_TYPE() {
								return R7_PRODUCT_TYPE;
							}




							public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
								R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
							}




							public BigDecimal getR7_ACCT_NUM() {
								return R7_ACCT_NUM;
							}




							public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
								R7_ACCT_NUM = r7_ACCT_NUM;
							}




							public String getR7_STATUS_OF_ACCT() {
								return R7_STATUS_OF_ACCT;
							}




							public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
								R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
							}




							public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR7_ACCT_BRANCH() {
								return R7_ACCT_BRANCH;
							}




							public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
								R7_ACCT_BRANCH = r7_ACCT_BRANCH;
							}




							public BigDecimal getR7_ACCT_BALANCE_PULA() {
								return R7_ACCT_BALANCE_PULA;
							}




							public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
								R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
							}




							public String getR7_CURRENCY_OF_ACCT() {
								return R7_CURRENCY_OF_ACCT;
							}




							public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
								R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR7_EXCHANGE_RATE() {
								return R7_EXCHANGE_RATE;
							}




							public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
								R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
							}




							public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR8_COMPANY_NAME() {
								return R8_COMPANY_NAME;
							}




							public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
								R8_COMPANY_NAME = r8_COMPANY_NAME;
							}




							public String getR8_COMPANY_REG_NUM() {
								return R8_COMPANY_REG_NUM;
							}




							public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
								R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
							}




							public String getR8_BUSINEES_PHY_ADDRESS() {
								return R8_BUSINEES_PHY_ADDRESS;
							}




							public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
								R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
							}




							public String getR8_POSTAL_ADDRESS() {
								return R8_POSTAL_ADDRESS;
							}




							public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
								R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
							}




							public String getR8_COUNTRY_OF_REG() {
								return R8_COUNTRY_OF_REG;
							}




							public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
								R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
							}




							public String getR8_COMPANY_EMAIL() {
								return R8_COMPANY_EMAIL;
							}




							public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
								R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
							}




							public String getR8_COMPANY_LANDLINE() {
								return R8_COMPANY_LANDLINE;
							}




							public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
								R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
							}




							public String getR8_COMPANY_MOB_PHONE_NUM() {
								return R8_COMPANY_MOB_PHONE_NUM;
							}




							public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
								R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
							}




							public String getR8_PRODUCT_TYPE() {
								return R8_PRODUCT_TYPE;
							}




							public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
								R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
							}




							public BigDecimal getR8_ACCT_NUM() {
								return R8_ACCT_NUM;
							}




							public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
								R8_ACCT_NUM = r8_ACCT_NUM;
							}




							public String getR8_STATUS_OF_ACCT() {
								return R8_STATUS_OF_ACCT;
							}




							public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
								R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
							}




							public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR8_ACCT_BRANCH() {
								return R8_ACCT_BRANCH;
							}




							public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
								R8_ACCT_BRANCH = r8_ACCT_BRANCH;
							}




							public BigDecimal getR8_ACCT_BALANCE_PULA() {
								return R8_ACCT_BALANCE_PULA;
							}




							public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
								R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
							}




							public String getR8_CURRENCY_OF_ACCT() {
								return R8_CURRENCY_OF_ACCT;
							}




							public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
								R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR8_EXCHANGE_RATE() {
								return R8_EXCHANGE_RATE;
							}




							public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
								R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
							}




							public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR9_COMPANY_NAME() {
								return R9_COMPANY_NAME;
							}




							public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
								R9_COMPANY_NAME = r9_COMPANY_NAME;
							}




							public String getR9_COMPANY_REG_NUM() {
								return R9_COMPANY_REG_NUM;
							}




							public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
								R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
							}




							public String getR9_BUSINEES_PHY_ADDRESS() {
								return R9_BUSINEES_PHY_ADDRESS;
							}




							public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
								R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
							}




							public String getR9_POSTAL_ADDRESS() {
								return R9_POSTAL_ADDRESS;
							}




							public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
								R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
							}




							public String getR9_COUNTRY_OF_REG() {
								return R9_COUNTRY_OF_REG;
							}




							public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
								R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
							}




							public String getR9_COMPANY_EMAIL() {
								return R9_COMPANY_EMAIL;
							}




							public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
								R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
							}




							public String getR9_COMPANY_LANDLINE() {
								return R9_COMPANY_LANDLINE;
							}




							public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
								R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
							}




							public String getR9_COMPANY_MOB_PHONE_NUM() {
								return R9_COMPANY_MOB_PHONE_NUM;
							}




							public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
								R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
							}




							public String getR9_PRODUCT_TYPE() {
								return R9_PRODUCT_TYPE;
							}




							public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
								R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
							}




							public BigDecimal getR9_ACCT_NUM() {
								return R9_ACCT_NUM;
							}




							public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
								R9_ACCT_NUM = r9_ACCT_NUM;
							}




							public String getR9_STATUS_OF_ACCT() {
								return R9_STATUS_OF_ACCT;
							}




							public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
								R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
							}




							public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR9_ACCT_BRANCH() {
								return R9_ACCT_BRANCH;
							}




							public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
								R9_ACCT_BRANCH = r9_ACCT_BRANCH;
							}




							public BigDecimal getR9_ACCT_BALANCE_PULA() {
								return R9_ACCT_BALANCE_PULA;
							}




							public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
								R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
							}




							public String getR9_CURRENCY_OF_ACCT() {
								return R9_CURRENCY_OF_ACCT;
							}




							public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
								R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR9_EXCHANGE_RATE() {
								return R9_EXCHANGE_RATE;
							}




							public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
								R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
							}




							public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR10_COMPANY_NAME() {
								return R10_COMPANY_NAME;
							}




							public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
								R10_COMPANY_NAME = r10_COMPANY_NAME;
							}




							public String getR10_COMPANY_REG_NUM() {
								return R10_COMPANY_REG_NUM;
							}




							public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
								R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
							}




							public String getR10_BUSINEES_PHY_ADDRESS() {
								return R10_BUSINEES_PHY_ADDRESS;
							}




							public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
								R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
							}




							public String getR10_POSTAL_ADDRESS() {
								return R10_POSTAL_ADDRESS;
							}




							public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
								R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
							}




							public String getR10_COUNTRY_OF_REG() {
								return R10_COUNTRY_OF_REG;
							}




							public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
								R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
							}




							public String getR10_COMPANY_EMAIL() {
								return R10_COMPANY_EMAIL;
							}




							public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
								R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
							}




							public String getR10_COMPANY_LANDLINE() {
								return R10_COMPANY_LANDLINE;
							}




							public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
								R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
							}




							public String getR10_COMPANY_MOB_PHONE_NUM() {
								return R10_COMPANY_MOB_PHONE_NUM;
							}




							public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
								R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
							}




							public String getR10_PRODUCT_TYPE() {
								return R10_PRODUCT_TYPE;
							}




							public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
								R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
							}




							public BigDecimal getR10_ACCT_NUM() {
								return R10_ACCT_NUM;
							}




							public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
								R10_ACCT_NUM = r10_ACCT_NUM;
							}




							public String getR10_STATUS_OF_ACCT() {
								return R10_STATUS_OF_ACCT;
							}




							public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
								R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
							}




							public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR10_ACCT_BRANCH() {
								return R10_ACCT_BRANCH;
							}




							public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
								R10_ACCT_BRANCH = r10_ACCT_BRANCH;
							}




							public BigDecimal getR10_ACCT_BALANCE_PULA() {
								return R10_ACCT_BALANCE_PULA;
							}




							public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
								R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
							}




							public String getR10_CURRENCY_OF_ACCT() {
								return R10_CURRENCY_OF_ACCT;
							}




							public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
								R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR10_EXCHANGE_RATE() {
								return R10_EXCHANGE_RATE;
							}




							public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
								R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
							}




							public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR11_COMPANY_NAME() {
								return R11_COMPANY_NAME;
							}




							public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
								R11_COMPANY_NAME = r11_COMPANY_NAME;
							}




							public String getR11_COMPANY_REG_NUM() {
								return R11_COMPANY_REG_NUM;
							}




							public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
								R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
							}




							public String getR11_BUSINEES_PHY_ADDRESS() {
								return R11_BUSINEES_PHY_ADDRESS;
							}




							public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
								R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
							}




							public String getR11_POSTAL_ADDRESS() {
								return R11_POSTAL_ADDRESS;
							}




							public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
								R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
							}




							public String getR11_COUNTRY_OF_REG() {
								return R11_COUNTRY_OF_REG;
							}




							public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
								R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
							}




							public String getR11_COMPANY_EMAIL() {
								return R11_COMPANY_EMAIL;
							}




							public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
								R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
							}




							public String getR11_COMPANY_LANDLINE() {
								return R11_COMPANY_LANDLINE;
							}




							public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
								R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
							}




							public String getR11_COMPANY_MOB_PHONE_NUM() {
								return R11_COMPANY_MOB_PHONE_NUM;
							}




							public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
								R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
							}




							public String getR11_PRODUCT_TYPE() {
								return R11_PRODUCT_TYPE;
							}




							public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
								R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
							}




							public BigDecimal getR11_ACCT_NUM() {
								return R11_ACCT_NUM;
							}




							public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
								R11_ACCT_NUM = r11_ACCT_NUM;
							}




							public String getR11_STATUS_OF_ACCT() {
								return R11_STATUS_OF_ACCT;
							}




							public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
								R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
							}




							public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR11_ACCT_BRANCH() {
								return R11_ACCT_BRANCH;
							}




							public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
								R11_ACCT_BRANCH = r11_ACCT_BRANCH;
							}




							public BigDecimal getR11_ACCT_BALANCE_PULA() {
								return R11_ACCT_BALANCE_PULA;
							}




							public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
								R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
							}




							public String getR11_CURRENCY_OF_ACCT() {
								return R11_CURRENCY_OF_ACCT;
							}




							public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
								R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR11_EXCHANGE_RATE() {
								return R11_EXCHANGE_RATE;
							}




							public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
								R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
							}




							public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR12_COMPANY_NAME() {
								return R12_COMPANY_NAME;
							}




							public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
								R12_COMPANY_NAME = r12_COMPANY_NAME;
							}




							public String getR12_COMPANY_REG_NUM() {
								return R12_COMPANY_REG_NUM;
							}




							public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
								R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
							}




							public String getR12_BUSINEES_PHY_ADDRESS() {
								return R12_BUSINEES_PHY_ADDRESS;
							}




							public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
								R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
							}




							public String getR12_POSTAL_ADDRESS() {
								return R12_POSTAL_ADDRESS;
							}




							public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
								R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
							}




							public String getR12_COUNTRY_OF_REG() {
								return R12_COUNTRY_OF_REG;
							}




							public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
								R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
							}




							public String getR12_COMPANY_EMAIL() {
								return R12_COMPANY_EMAIL;
							}




							public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
								R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
							}




							public String getR12_COMPANY_LANDLINE() {
								return R12_COMPANY_LANDLINE;
							}




							public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
								R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
							}




							public String getR12_COMPANY_MOB_PHONE_NUM() {
								return R12_COMPANY_MOB_PHONE_NUM;
							}




							public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
								R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
							}




							public String getR12_PRODUCT_TYPE() {
								return R12_PRODUCT_TYPE;
							}




							public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
								R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
							}




							public BigDecimal getR12_ACCT_NUM() {
								return R12_ACCT_NUM;
							}




							public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
								R12_ACCT_NUM = r12_ACCT_NUM;
							}




							public String getR12_STATUS_OF_ACCT() {
								return R12_STATUS_OF_ACCT;
							}




							public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
								R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
							}




							public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR12_ACCT_BRANCH() {
								return R12_ACCT_BRANCH;
							}




							public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
								R12_ACCT_BRANCH = r12_ACCT_BRANCH;
							}




							public BigDecimal getR12_ACCT_BALANCE_PULA() {
								return R12_ACCT_BALANCE_PULA;
							}




							public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
								R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
							}




							public String getR12_CURRENCY_OF_ACCT() {
								return R12_CURRENCY_OF_ACCT;
							}




							public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
								R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR12_EXCHANGE_RATE() {
								return R12_EXCHANGE_RATE;
							}




							public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
								R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

	                        public Date getREPORT_RESUBDATE() {
								return REPORT_RESUBDATE;
							}

							public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
								this.REPORT_RESUBDATE = REPORT_RESUBDATE;
							}
							public String getREPORT_FREQUENCY() {
								return REPORT_FREQUENCY;
							}

							public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
								REPORT_FREQUENCY = rEPORT_FREQUENCY;
							}

							public String getREPORT_CODE() {
								return REPORT_CODE;
							}

							public void setREPORT_CODE(String rEPORT_CODE) {
								REPORT_CODE = rEPORT_CODE;
							}

							public String getREPORT_DESC() {
								return REPORT_DESC;
							}

							public void setREPORT_DESC(String rEPORT_DESC) {
								REPORT_DESC = rEPORT_DESC;
							}

							public String getENTITY_FLG() {
								return ENTITY_FLG;
							}

							public void setENTITY_FLG(String eNTITY_FLG) {
								ENTITY_FLG = eNTITY_FLG;
							}

							public String getMODIFY_FLG() {
								return MODIFY_FLG;
							}

							public void setMODIFY_FLG(String mODIFY_FLG) {
								MODIFY_FLG = mODIFY_FLG;
							}

							public String getDEL_FLG() {
								return DEL_FLG;
							}

							public void setDEL_FLG(String dEL_FLG) {
								DEL_FLG = dEL_FLG;
							}

						}
		

		// ROW MAPPER RESUB DETAIL

		class BDISB2RowMapper_ResubDetail implements RowMapper<BDISB2_RESUB_Detail_Entity> {

			@Override
			public BDISB2_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				BDISB2_RESUB_Detail_Entity obj = new BDISB2_RESUB_Detail_Entity();

				// ===================== R6 =====================
				obj.setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R6_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR6_COMPANY_NAME(rs.getString("R6_COMPANY_NAME"));
				obj.setR6_COMPANY_REG_NUM(rs.getString("R6_COMPANY_REG_NUM"));
				obj.setR6_BUSINEES_PHY_ADDRESS(rs.getString("R6_BUSINEES_PHY_ADDRESS"));
				obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
				obj.setR6_COUNTRY_OF_REG(rs.getString("R6_COUNTRY_OF_REG"));
				obj.setR6_COMPANY_EMAIL(rs.getString("R6_COMPANY_EMAIL"));
				obj.setR6_COMPANY_LANDLINE(rs.getString("R6_COMPANY_LANDLINE"));
				obj.setR6_COMPANY_MOB_PHONE_NUM(rs.getString("R6_COMPANY_MOB_PHONE_NUM"));
				obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
				obj.setR6_ACCT_NUM(rs.getBigDecimal("R6_ACCT_NUM"));
				obj.setR6_STATUS_OF_ACCT(rs.getString("R6_STATUS_OF_ACCT"));
				obj.setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR6_ACCT_BRANCH(rs.getString("R6_ACCT_BRANCH"));
				obj.setR6_ACCT_BALANCE_PULA(rs.getBigDecimal("R6_ACCT_BALANCE_PULA"));
				obj.setR6_CURRENCY_OF_ACCT(rs.getString("R6_CURRENCY_OF_ACCT"));
				obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));

				// ===================== R7 =====================
				obj.setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R7_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR7_COMPANY_NAME(rs.getString("R7_COMPANY_NAME"));
				obj.setR7_COMPANY_REG_NUM(rs.getString("R7_COMPANY_REG_NUM"));
				obj.setR7_BUSINEES_PHY_ADDRESS(rs.getString("R7_BUSINEES_PHY_ADDRESS"));
				obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
				obj.setR7_COUNTRY_OF_REG(rs.getString("R7_COUNTRY_OF_REG"));
				obj.setR7_COMPANY_EMAIL(rs.getString("R7_COMPANY_EMAIL"));
				obj.setR7_COMPANY_LANDLINE(rs.getString("R7_COMPANY_LANDLINE"));
				obj.setR7_COMPANY_MOB_PHONE_NUM(rs.getString("R7_COMPANY_MOB_PHONE_NUM"));
				obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
				obj.setR7_ACCT_NUM(rs.getBigDecimal("R7_ACCT_NUM"));
				obj.setR7_STATUS_OF_ACCT(rs.getString("R7_STATUS_OF_ACCT"));
				obj.setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR7_ACCT_BRANCH(rs.getString("R7_ACCT_BRANCH"));
				obj.setR7_ACCT_BALANCE_PULA(rs.getBigDecimal("R7_ACCT_BALANCE_PULA"));
				obj.setR7_CURRENCY_OF_ACCT(rs.getString("R7_CURRENCY_OF_ACCT"));
				obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));

				// ===================== R8 =====================
				obj.setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R8_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR8_COMPANY_NAME(rs.getString("R8_COMPANY_NAME"));
				obj.setR8_COMPANY_REG_NUM(rs.getString("R8_COMPANY_REG_NUM"));
				obj.setR8_BUSINEES_PHY_ADDRESS(rs.getString("R8_BUSINEES_PHY_ADDRESS"));
				obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
				obj.setR8_COUNTRY_OF_REG(rs.getString("R8_COUNTRY_OF_REG"));
				obj.setR8_COMPANY_EMAIL(rs.getString("R8_COMPANY_EMAIL"));
				obj.setR8_COMPANY_LANDLINE(rs.getString("R8_COMPANY_LANDLINE"));
				obj.setR8_COMPANY_MOB_PHONE_NUM(rs.getString("R8_COMPANY_MOB_PHONE_NUM"));
				obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
				obj.setR8_ACCT_NUM(rs.getBigDecimal("R8_ACCT_NUM"));
				obj.setR8_STATUS_OF_ACCT(rs.getString("R8_STATUS_OF_ACCT"));
				obj.setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR8_ACCT_BRANCH(rs.getString("R8_ACCT_BRANCH"));
				obj.setR8_ACCT_BALANCE_PULA(rs.getBigDecimal("R8_ACCT_BALANCE_PULA"));
				obj.setR8_CURRENCY_OF_ACCT(rs.getString("R8_CURRENCY_OF_ACCT"));
				obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));

				// ===================== R9 =====================
				obj.setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R9_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR9_COMPANY_NAME(rs.getString("R9_COMPANY_NAME"));
				obj.setR9_COMPANY_REG_NUM(rs.getString("R9_COMPANY_REG_NUM"));
				obj.setR9_BUSINEES_PHY_ADDRESS(rs.getString("R9_BUSINEES_PHY_ADDRESS"));
				obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
				obj.setR9_COUNTRY_OF_REG(rs.getString("R9_COUNTRY_OF_REG"));
				obj.setR9_COMPANY_EMAIL(rs.getString("R9_COMPANY_EMAIL"));
				obj.setR9_COMPANY_LANDLINE(rs.getString("R9_COMPANY_LANDLINE"));
				obj.setR9_COMPANY_MOB_PHONE_NUM(rs.getString("R9_COMPANY_MOB_PHONE_NUM"));
				obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
				obj.setR9_ACCT_NUM(rs.getBigDecimal("R9_ACCT_NUM"));
				obj.setR9_STATUS_OF_ACCT(rs.getString("R9_STATUS_OF_ACCT"));
				obj.setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR9_ACCT_BRANCH(rs.getString("R9_ACCT_BRANCH"));
				obj.setR9_ACCT_BALANCE_PULA(rs.getBigDecimal("R9_ACCT_BALANCE_PULA"));
				obj.setR9_CURRENCY_OF_ACCT(rs.getString("R9_CURRENCY_OF_ACCT"));
				obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));

				// ===================== R10 =====================
				obj.setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R10_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR10_COMPANY_NAME(rs.getString("R10_COMPANY_NAME"));
				obj.setR10_COMPANY_REG_NUM(rs.getString("R10_COMPANY_REG_NUM"));
				obj.setR10_BUSINEES_PHY_ADDRESS(rs.getString("R10_BUSINEES_PHY_ADDRESS"));
				obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
				obj.setR10_COUNTRY_OF_REG(rs.getString("R10_COUNTRY_OF_REG"));
				obj.setR10_COMPANY_EMAIL(rs.getString("R10_COMPANY_EMAIL"));
				obj.setR10_COMPANY_LANDLINE(rs.getString("R10_COMPANY_LANDLINE"));
				obj.setR10_COMPANY_MOB_PHONE_NUM(rs.getString("R10_COMPANY_MOB_PHONE_NUM"));
				obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
				obj.setR10_ACCT_NUM(rs.getBigDecimal("R10_ACCT_NUM"));
				obj.setR10_STATUS_OF_ACCT(rs.getString("R10_STATUS_OF_ACCT"));
				obj.setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR10_ACCT_BRANCH(rs.getString("R10_ACCT_BRANCH"));
				obj.setR10_ACCT_BALANCE_PULA(rs.getBigDecimal("R10_ACCT_BALANCE_PULA"));
				obj.setR10_CURRENCY_OF_ACCT(rs.getString("R10_CURRENCY_OF_ACCT"));
				obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));

				// ===================== R11 =====================
				obj.setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R11_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR11_COMPANY_NAME(rs.getString("R11_COMPANY_NAME"));
				obj.setR11_COMPANY_REG_NUM(rs.getString("R11_COMPANY_REG_NUM"));
				obj.setR11_BUSINEES_PHY_ADDRESS(rs.getString("R11_BUSINEES_PHY_ADDRESS"));
				obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
				obj.setR11_COUNTRY_OF_REG(rs.getString("R11_COUNTRY_OF_REG"));
				obj.setR11_COMPANY_EMAIL(rs.getString("R11_COMPANY_EMAIL"));
				obj.setR11_COMPANY_LANDLINE(rs.getString("R11_COMPANY_LANDLINE"));
				obj.setR11_COMPANY_MOB_PHONE_NUM(rs.getString("R11_COMPANY_MOB_PHONE_NUM"));
				obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
				obj.setR11_ACCT_NUM(rs.getBigDecimal("R11_ACCT_NUM"));
				obj.setR11_STATUS_OF_ACCT(rs.getString("R11_STATUS_OF_ACCT"));
				obj.setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR11_ACCT_BRANCH(rs.getString("R11_ACCT_BRANCH"));
				obj.setR11_ACCT_BALANCE_PULA(rs.getBigDecimal("R11_ACCT_BALANCE_PULA"));
				obj.setR11_CURRENCY_OF_ACCT(rs.getString("R11_CURRENCY_OF_ACCT"));
				obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));

				// ===================== R12 =====================
				obj.setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(rs.getString("R12_BANK_SPEC_SINGLE_CUST_REC_NUM"));
				obj.setR12_COMPANY_NAME(rs.getString("R12_COMPANY_NAME"));
				obj.setR12_COMPANY_REG_NUM(rs.getString("R12_COMPANY_REG_NUM"));
				obj.setR12_BUSINEES_PHY_ADDRESS(rs.getString("R12_BUSINEES_PHY_ADDRESS"));
				obj.setR12_POSTAL_ADDRESS(rs.getString("R12_POSTAL_ADDRESS"));
				obj.setR12_COUNTRY_OF_REG(rs.getString("R12_COUNTRY_OF_REG"));
				obj.setR12_COMPANY_EMAIL(rs.getString("R12_COMPANY_EMAIL"));
				obj.setR12_COMPANY_LANDLINE(rs.getString("R12_COMPANY_LANDLINE"));
				obj.setR12_COMPANY_MOB_PHONE_NUM(rs.getString("R12_COMPANY_MOB_PHONE_NUM"));
				obj.setR12_PRODUCT_TYPE(rs.getString("R12_PRODUCT_TYPE"));
				obj.setR12_ACCT_NUM(rs.getBigDecimal("R12_ACCT_NUM"));
				obj.setR12_STATUS_OF_ACCT(rs.getString("R12_STATUS_OF_ACCT"));
				obj.setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
						rs.getString("R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
				obj.setR12_ACCT_BRANCH(rs.getString("R12_ACCT_BRANCH"));
				obj.setR12_ACCT_BALANCE_PULA(rs.getBigDecimal("R12_ACCT_BALANCE_PULA"));
				obj.setR12_CURRENCY_OF_ACCT(rs.getString("R12_CURRENCY_OF_ACCT"));
				obj.setR12_EXCHANGE_RATE(rs.getBigDecimal("R12_EXCHANGE_RATE"));

				// COMMON FIELDS
				obj.setREPORT_DATE(rs.getDate("REPORT_DATE"));
				obj.setREPORT_VERSION(rs.getBigDecimal("REPORT_VERSION"));
				obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
				obj.setREPORT_FREQUENCY(rs.getString("REPORT_FREQUENCY"));
				obj.setREPORT_CODE(rs.getString("REPORT_CODE"));
				obj.setREPORT_DESC(rs.getString("REPORT_DESC"));
				obj.setENTITY_FLG(rs.getString("ENTITY_FLG"));
				obj.setMODIFY_FLG(rs.getString("MODIFY_FLG"));
				obj.setDEL_FLG(rs.getString("DEL_FLG"));

				return obj;
			}
		}

		public static class BDISB2_RESUB_Detail_Entity {
					
						private String R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R6_COMPANY_NAME;
						private String R6_COMPANY_REG_NUM;
						private String R6_BUSINEES_PHY_ADDRESS;
						private String R6_POSTAL_ADDRESS;
						private String R6_COUNTRY_OF_REG;
						private String R6_COMPANY_EMAIL;
						private String R6_COMPANY_LANDLINE;
						private String R6_COMPANY_MOB_PHONE_NUM;
						private String R6_PRODUCT_TYPE;
						private BigDecimal R6_ACCT_NUM;
						private String R6_STATUS_OF_ACCT;
						private String R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R6_ACCT_BRANCH;
						private BigDecimal R6_ACCT_BALANCE_PULA;
						private String R6_CURRENCY_OF_ACCT;
						private BigDecimal R6_EXCHANGE_RATE;
						private String R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R7_COMPANY_NAME;
						private String R7_COMPANY_REG_NUM;
						private String R7_BUSINEES_PHY_ADDRESS;
						private String R7_POSTAL_ADDRESS;
						private String R7_COUNTRY_OF_REG;
						private String R7_COMPANY_EMAIL;
						private String R7_COMPANY_LANDLINE;
						private String R7_COMPANY_MOB_PHONE_NUM;
						private String R7_PRODUCT_TYPE;
						private BigDecimal R7_ACCT_NUM;
						private String R7_STATUS_OF_ACCT;
						private String R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R7_ACCT_BRANCH;
						private BigDecimal R7_ACCT_BALANCE_PULA;
						private String R7_CURRENCY_OF_ACCT;
						private BigDecimal R7_EXCHANGE_RATE;
						private String R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R8_COMPANY_NAME;
						private String R8_COMPANY_REG_NUM;
						private String R8_BUSINEES_PHY_ADDRESS;
						private String R8_POSTAL_ADDRESS;
						private String R8_COUNTRY_OF_REG;
						private String R8_COMPANY_EMAIL;
						private String R8_COMPANY_LANDLINE;
						private String R8_COMPANY_MOB_PHONE_NUM;
						private String R8_PRODUCT_TYPE;
						private BigDecimal R8_ACCT_NUM;
						private String R8_STATUS_OF_ACCT;
						private String R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R8_ACCT_BRANCH;
						private BigDecimal R8_ACCT_BALANCE_PULA;
						private String R8_CURRENCY_OF_ACCT;
						private BigDecimal R8_EXCHANGE_RATE;
						private String R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R9_COMPANY_NAME;
						private String R9_COMPANY_REG_NUM;
						private String R9_BUSINEES_PHY_ADDRESS;
						private String R9_POSTAL_ADDRESS;
						private String R9_COUNTRY_OF_REG;
						private String R9_COMPANY_EMAIL;
						private String R9_COMPANY_LANDLINE;
						private String R9_COMPANY_MOB_PHONE_NUM;
						private String R9_PRODUCT_TYPE;
						private BigDecimal R9_ACCT_NUM;
						private String R9_STATUS_OF_ACCT;
						private String R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R9_ACCT_BRANCH;
						private BigDecimal R9_ACCT_BALANCE_PULA;
						private String R9_CURRENCY_OF_ACCT;
						private BigDecimal R9_EXCHANGE_RATE;
						private String R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R10_COMPANY_NAME;
						private String R10_COMPANY_REG_NUM;
						private String R10_BUSINEES_PHY_ADDRESS;
						private String R10_POSTAL_ADDRESS;
						private String R10_COUNTRY_OF_REG;
						private String R10_COMPANY_EMAIL;
						private String R10_COMPANY_LANDLINE;
						private String R10_COMPANY_MOB_PHONE_NUM;
						private String R10_PRODUCT_TYPE;
						private BigDecimal R10_ACCT_NUM;
						private String R10_STATUS_OF_ACCT;
						private String R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R10_ACCT_BRANCH;
						private BigDecimal R10_ACCT_BALANCE_PULA;
						private String R10_CURRENCY_OF_ACCT;
						private BigDecimal R10_EXCHANGE_RATE;
						private String R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R11_COMPANY_NAME;
						private String R11_COMPANY_REG_NUM;
						private String R11_BUSINEES_PHY_ADDRESS;
						private String R11_POSTAL_ADDRESS;
						private String R11_COUNTRY_OF_REG;
						private String R11_COMPANY_EMAIL;
						private String R11_COMPANY_LANDLINE;
						private String R11_COMPANY_MOB_PHONE_NUM;
						private String R11_PRODUCT_TYPE;
						private BigDecimal R11_ACCT_NUM;
						private String R11_STATUS_OF_ACCT;
						private String R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R11_ACCT_BRANCH;
						private BigDecimal R11_ACCT_BALANCE_PULA;
						private String R11_CURRENCY_OF_ACCT;
						private BigDecimal R11_EXCHANGE_RATE;
						private String R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
						private String R12_COMPANY_NAME;
						private String R12_COMPANY_REG_NUM;
						private String R12_BUSINEES_PHY_ADDRESS;
						private String R12_POSTAL_ADDRESS;
						private String R12_COUNTRY_OF_REG;
						private String R12_COMPANY_EMAIL;
						private String R12_COMPANY_LANDLINE;
						private String R12_COMPANY_MOB_PHONE_NUM;
						private String R12_PRODUCT_TYPE;
						private BigDecimal R12_ACCT_NUM;
						private String R12_STATUS_OF_ACCT;
						private String R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
						private String R12_ACCT_BRANCH;
						private BigDecimal R12_ACCT_BALANCE_PULA;
						private String R12_CURRENCY_OF_ACCT;
						private BigDecimal R12_EXCHANGE_RATE;

						 @Id
							@Temporal(TemporalType.DATE)
							@Column(name = "REPORT_DATE")
							private Date REPORT_DATE;

							@Column(name = "REPORT_VERSION", length = 100)
							private BigDecimal REPORT_VERSION;
							
							@Column(name = "REPORT_RESUBDATE")
							private Date REPORT_RESUBDATE;

							@Column(name = "REPORT_FREQUENCY", length = 100)
							private String REPORT_FREQUENCY;

							@Column(name = "REPORT_CODE", length = 100)
							private String REPORT_CODE;

							@Column(name = "REPORT_DESC", length = 100)
							private String REPORT_DESC;

							@Column(name = "ENTITY_FLG", length = 1)
							private String ENTITY_FLG;

							@Column(name = "MODIFY_FLG", length = 1)
							private String MODIFY_FLG;

							@Column(name = "DEL_FLG", length = 1)
							private String DEL_FLG;
							



							public String getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR6_BANK_SPEC_SINGLE_CUST_REC_NUM(String r6_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R6_BANK_SPEC_SINGLE_CUST_REC_NUM = r6_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR6_COMPANY_NAME() {
								return R6_COMPANY_NAME;
							}




							public void setR6_COMPANY_NAME(String r6_COMPANY_NAME) {
								R6_COMPANY_NAME = r6_COMPANY_NAME;
							}




							public String getR6_COMPANY_REG_NUM() {
								return R6_COMPANY_REG_NUM;
							}




							public void setR6_COMPANY_REG_NUM(String r6_COMPANY_REG_NUM) {
								R6_COMPANY_REG_NUM = r6_COMPANY_REG_NUM;
							}




							public String getR6_BUSINEES_PHY_ADDRESS() {
								return R6_BUSINEES_PHY_ADDRESS;
							}




							public void setR6_BUSINEES_PHY_ADDRESS(String r6_BUSINEES_PHY_ADDRESS) {
								R6_BUSINEES_PHY_ADDRESS = r6_BUSINEES_PHY_ADDRESS;
							}




							public String getR6_POSTAL_ADDRESS() {
								return R6_POSTAL_ADDRESS;
							}




							public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
								R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
							}




							public String getR6_COUNTRY_OF_REG() {
								return R6_COUNTRY_OF_REG;
							}




							public void setR6_COUNTRY_OF_REG(String r6_COUNTRY_OF_REG) {
								R6_COUNTRY_OF_REG = r6_COUNTRY_OF_REG;
							}




							public String getR6_COMPANY_EMAIL() {
								return R6_COMPANY_EMAIL;
							}




							public void setR6_COMPANY_EMAIL(String r6_COMPANY_EMAIL) {
								R6_COMPANY_EMAIL = r6_COMPANY_EMAIL;
							}




							public String getR6_COMPANY_LANDLINE() {
								return R6_COMPANY_LANDLINE;
							}




							public void setR6_COMPANY_LANDLINE(String r6_COMPANY_LANDLINE) {
								R6_COMPANY_LANDLINE = r6_COMPANY_LANDLINE;
							}




							public String getR6_COMPANY_MOB_PHONE_NUM() {
								return R6_COMPANY_MOB_PHONE_NUM;
							}




							public void setR6_COMPANY_MOB_PHONE_NUM(String r6_COMPANY_MOB_PHONE_NUM) {
								R6_COMPANY_MOB_PHONE_NUM = r6_COMPANY_MOB_PHONE_NUM;
							}




							public String getR6_PRODUCT_TYPE() {
								return R6_PRODUCT_TYPE;
							}




							public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
								R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
							}




							public BigDecimal getR6_ACCT_NUM() {
								return R6_ACCT_NUM;
							}




							public void setR6_ACCT_NUM(BigDecimal r6_ACCT_NUM) {
								R6_ACCT_NUM = r6_ACCT_NUM;
							}




							public String getR6_STATUS_OF_ACCT() {
								return R6_STATUS_OF_ACCT;
							}




							public void setR6_STATUS_OF_ACCT(String r6_STATUS_OF_ACCT) {
								R6_STATUS_OF_ACCT = r6_STATUS_OF_ACCT;
							}




							public String getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR6_ACCT_BRANCH() {
								return R6_ACCT_BRANCH;
							}




							public void setR6_ACCT_BRANCH(String r6_ACCT_BRANCH) {
								R6_ACCT_BRANCH = r6_ACCT_BRANCH;
							}




							public BigDecimal getR6_ACCT_BALANCE_PULA() {
								return R6_ACCT_BALANCE_PULA;
							}




							public void setR6_ACCT_BALANCE_PULA(BigDecimal r6_ACCT_BALANCE_PULA) {
								R6_ACCT_BALANCE_PULA = r6_ACCT_BALANCE_PULA;
							}




							public String getR6_CURRENCY_OF_ACCT() {
								return R6_CURRENCY_OF_ACCT;
							}




							public void setR6_CURRENCY_OF_ACCT(String r6_CURRENCY_OF_ACCT) {
								R6_CURRENCY_OF_ACCT = r6_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR6_EXCHANGE_RATE() {
								return R6_EXCHANGE_RATE;
							}




							public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
								R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
							}




							public String getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR7_BANK_SPEC_SINGLE_CUST_REC_NUM(String r7_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R7_BANK_SPEC_SINGLE_CUST_REC_NUM = r7_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR7_COMPANY_NAME() {
								return R7_COMPANY_NAME;
							}




							public void setR7_COMPANY_NAME(String r7_COMPANY_NAME) {
								R7_COMPANY_NAME = r7_COMPANY_NAME;
							}




							public String getR7_COMPANY_REG_NUM() {
								return R7_COMPANY_REG_NUM;
							}




							public void setR7_COMPANY_REG_NUM(String r7_COMPANY_REG_NUM) {
								R7_COMPANY_REG_NUM = r7_COMPANY_REG_NUM;
							}




							public String getR7_BUSINEES_PHY_ADDRESS() {
								return R7_BUSINEES_PHY_ADDRESS;
							}




							public void setR7_BUSINEES_PHY_ADDRESS(String r7_BUSINEES_PHY_ADDRESS) {
								R7_BUSINEES_PHY_ADDRESS = r7_BUSINEES_PHY_ADDRESS;
							}




							public String getR7_POSTAL_ADDRESS() {
								return R7_POSTAL_ADDRESS;
							}




							public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
								R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
							}




							public String getR7_COUNTRY_OF_REG() {
								return R7_COUNTRY_OF_REG;
							}




							public void setR7_COUNTRY_OF_REG(String r7_COUNTRY_OF_REG) {
								R7_COUNTRY_OF_REG = r7_COUNTRY_OF_REG;
							}




							public String getR7_COMPANY_EMAIL() {
								return R7_COMPANY_EMAIL;
							}




							public void setR7_COMPANY_EMAIL(String r7_COMPANY_EMAIL) {
								R7_COMPANY_EMAIL = r7_COMPANY_EMAIL;
							}




							public String getR7_COMPANY_LANDLINE() {
								return R7_COMPANY_LANDLINE;
							}




							public void setR7_COMPANY_LANDLINE(String r7_COMPANY_LANDLINE) {
								R7_COMPANY_LANDLINE = r7_COMPANY_LANDLINE;
							}




							public String getR7_COMPANY_MOB_PHONE_NUM() {
								return R7_COMPANY_MOB_PHONE_NUM;
							}




							public void setR7_COMPANY_MOB_PHONE_NUM(String r7_COMPANY_MOB_PHONE_NUM) {
								R7_COMPANY_MOB_PHONE_NUM = r7_COMPANY_MOB_PHONE_NUM;
							}




							public String getR7_PRODUCT_TYPE() {
								return R7_PRODUCT_TYPE;
							}




							public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
								R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
							}




							public BigDecimal getR7_ACCT_NUM() {
								return R7_ACCT_NUM;
							}




							public void setR7_ACCT_NUM(BigDecimal r7_ACCT_NUM) {
								R7_ACCT_NUM = r7_ACCT_NUM;
							}




							public String getR7_STATUS_OF_ACCT() {
								return R7_STATUS_OF_ACCT;
							}




							public void setR7_STATUS_OF_ACCT(String r7_STATUS_OF_ACCT) {
								R7_STATUS_OF_ACCT = r7_STATUS_OF_ACCT;
							}




							public String getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR7_ACCT_BRANCH() {
								return R7_ACCT_BRANCH;
							}




							public void setR7_ACCT_BRANCH(String r7_ACCT_BRANCH) {
								R7_ACCT_BRANCH = r7_ACCT_BRANCH;
							}




							public BigDecimal getR7_ACCT_BALANCE_PULA() {
								return R7_ACCT_BALANCE_PULA;
							}




							public void setR7_ACCT_BALANCE_PULA(BigDecimal r7_ACCT_BALANCE_PULA) {
								R7_ACCT_BALANCE_PULA = r7_ACCT_BALANCE_PULA;
							}




							public String getR7_CURRENCY_OF_ACCT() {
								return R7_CURRENCY_OF_ACCT;
							}




							public void setR7_CURRENCY_OF_ACCT(String r7_CURRENCY_OF_ACCT) {
								R7_CURRENCY_OF_ACCT = r7_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR7_EXCHANGE_RATE() {
								return R7_EXCHANGE_RATE;
							}




							public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
								R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
							}




							public String getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR8_BANK_SPEC_SINGLE_CUST_REC_NUM(String r8_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R8_BANK_SPEC_SINGLE_CUST_REC_NUM = r8_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR8_COMPANY_NAME() {
								return R8_COMPANY_NAME;
							}




							public void setR8_COMPANY_NAME(String r8_COMPANY_NAME) {
								R8_COMPANY_NAME = r8_COMPANY_NAME;
							}




							public String getR8_COMPANY_REG_NUM() {
								return R8_COMPANY_REG_NUM;
							}




							public void setR8_COMPANY_REG_NUM(String r8_COMPANY_REG_NUM) {
								R8_COMPANY_REG_NUM = r8_COMPANY_REG_NUM;
							}




							public String getR8_BUSINEES_PHY_ADDRESS() {
								return R8_BUSINEES_PHY_ADDRESS;
							}




							public void setR8_BUSINEES_PHY_ADDRESS(String r8_BUSINEES_PHY_ADDRESS) {
								R8_BUSINEES_PHY_ADDRESS = r8_BUSINEES_PHY_ADDRESS;
							}




							public String getR8_POSTAL_ADDRESS() {
								return R8_POSTAL_ADDRESS;
							}




							public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
								R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
							}




							public String getR8_COUNTRY_OF_REG() {
								return R8_COUNTRY_OF_REG;
							}




							public void setR8_COUNTRY_OF_REG(String r8_COUNTRY_OF_REG) {
								R8_COUNTRY_OF_REG = r8_COUNTRY_OF_REG;
							}




							public String getR8_COMPANY_EMAIL() {
								return R8_COMPANY_EMAIL;
							}




							public void setR8_COMPANY_EMAIL(String r8_COMPANY_EMAIL) {
								R8_COMPANY_EMAIL = r8_COMPANY_EMAIL;
							}




							public String getR8_COMPANY_LANDLINE() {
								return R8_COMPANY_LANDLINE;
							}




							public void setR8_COMPANY_LANDLINE(String r8_COMPANY_LANDLINE) {
								R8_COMPANY_LANDLINE = r8_COMPANY_LANDLINE;
							}




							public String getR8_COMPANY_MOB_PHONE_NUM() {
								return R8_COMPANY_MOB_PHONE_NUM;
							}




							public void setR8_COMPANY_MOB_PHONE_NUM(String r8_COMPANY_MOB_PHONE_NUM) {
								R8_COMPANY_MOB_PHONE_NUM = r8_COMPANY_MOB_PHONE_NUM;
							}




							public String getR8_PRODUCT_TYPE() {
								return R8_PRODUCT_TYPE;
							}




							public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
								R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
							}




							public BigDecimal getR8_ACCT_NUM() {
								return R8_ACCT_NUM;
							}




							public void setR8_ACCT_NUM(BigDecimal r8_ACCT_NUM) {
								R8_ACCT_NUM = r8_ACCT_NUM;
							}




							public String getR8_STATUS_OF_ACCT() {
								return R8_STATUS_OF_ACCT;
							}




							public void setR8_STATUS_OF_ACCT(String r8_STATUS_OF_ACCT) {
								R8_STATUS_OF_ACCT = r8_STATUS_OF_ACCT;
							}




							public String getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR8_ACCT_BRANCH() {
								return R8_ACCT_BRANCH;
							}




							public void setR8_ACCT_BRANCH(String r8_ACCT_BRANCH) {
								R8_ACCT_BRANCH = r8_ACCT_BRANCH;
							}




							public BigDecimal getR8_ACCT_BALANCE_PULA() {
								return R8_ACCT_BALANCE_PULA;
							}




							public void setR8_ACCT_BALANCE_PULA(BigDecimal r8_ACCT_BALANCE_PULA) {
								R8_ACCT_BALANCE_PULA = r8_ACCT_BALANCE_PULA;
							}




							public String getR8_CURRENCY_OF_ACCT() {
								return R8_CURRENCY_OF_ACCT;
							}




							public void setR8_CURRENCY_OF_ACCT(String r8_CURRENCY_OF_ACCT) {
								R8_CURRENCY_OF_ACCT = r8_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR8_EXCHANGE_RATE() {
								return R8_EXCHANGE_RATE;
							}




							public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
								R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
							}




							public String getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR9_BANK_SPEC_SINGLE_CUST_REC_NUM(String r9_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R9_BANK_SPEC_SINGLE_CUST_REC_NUM = r9_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR9_COMPANY_NAME() {
								return R9_COMPANY_NAME;
							}




							public void setR9_COMPANY_NAME(String r9_COMPANY_NAME) {
								R9_COMPANY_NAME = r9_COMPANY_NAME;
							}




							public String getR9_COMPANY_REG_NUM() {
								return R9_COMPANY_REG_NUM;
							}




							public void setR9_COMPANY_REG_NUM(String r9_COMPANY_REG_NUM) {
								R9_COMPANY_REG_NUM = r9_COMPANY_REG_NUM;
							}




							public String getR9_BUSINEES_PHY_ADDRESS() {
								return R9_BUSINEES_PHY_ADDRESS;
							}




							public void setR9_BUSINEES_PHY_ADDRESS(String r9_BUSINEES_PHY_ADDRESS) {
								R9_BUSINEES_PHY_ADDRESS = r9_BUSINEES_PHY_ADDRESS;
							}




							public String getR9_POSTAL_ADDRESS() {
								return R9_POSTAL_ADDRESS;
							}




							public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
								R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
							}




							public String getR9_COUNTRY_OF_REG() {
								return R9_COUNTRY_OF_REG;
							}




							public void setR9_COUNTRY_OF_REG(String r9_COUNTRY_OF_REG) {
								R9_COUNTRY_OF_REG = r9_COUNTRY_OF_REG;
							}




							public String getR9_COMPANY_EMAIL() {
								return R9_COMPANY_EMAIL;
							}




							public void setR9_COMPANY_EMAIL(String r9_COMPANY_EMAIL) {
								R9_COMPANY_EMAIL = r9_COMPANY_EMAIL;
							}




							public String getR9_COMPANY_LANDLINE() {
								return R9_COMPANY_LANDLINE;
							}




							public void setR9_COMPANY_LANDLINE(String r9_COMPANY_LANDLINE) {
								R9_COMPANY_LANDLINE = r9_COMPANY_LANDLINE;
							}




							public String getR9_COMPANY_MOB_PHONE_NUM() {
								return R9_COMPANY_MOB_PHONE_NUM;
							}




							public void setR9_COMPANY_MOB_PHONE_NUM(String r9_COMPANY_MOB_PHONE_NUM) {
								R9_COMPANY_MOB_PHONE_NUM = r9_COMPANY_MOB_PHONE_NUM;
							}




							public String getR9_PRODUCT_TYPE() {
								return R9_PRODUCT_TYPE;
							}




							public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
								R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
							}




							public BigDecimal getR9_ACCT_NUM() {
								return R9_ACCT_NUM;
							}




							public void setR9_ACCT_NUM(BigDecimal r9_ACCT_NUM) {
								R9_ACCT_NUM = r9_ACCT_NUM;
							}




							public String getR9_STATUS_OF_ACCT() {
								return R9_STATUS_OF_ACCT;
							}




							public void setR9_STATUS_OF_ACCT(String r9_STATUS_OF_ACCT) {
								R9_STATUS_OF_ACCT = r9_STATUS_OF_ACCT;
							}




							public String getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR9_ACCT_BRANCH() {
								return R9_ACCT_BRANCH;
							}




							public void setR9_ACCT_BRANCH(String r9_ACCT_BRANCH) {
								R9_ACCT_BRANCH = r9_ACCT_BRANCH;
							}




							public BigDecimal getR9_ACCT_BALANCE_PULA() {
								return R9_ACCT_BALANCE_PULA;
							}




							public void setR9_ACCT_BALANCE_PULA(BigDecimal r9_ACCT_BALANCE_PULA) {
								R9_ACCT_BALANCE_PULA = r9_ACCT_BALANCE_PULA;
							}




							public String getR9_CURRENCY_OF_ACCT() {
								return R9_CURRENCY_OF_ACCT;
							}




							public void setR9_CURRENCY_OF_ACCT(String r9_CURRENCY_OF_ACCT) {
								R9_CURRENCY_OF_ACCT = r9_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR9_EXCHANGE_RATE() {
								return R9_EXCHANGE_RATE;
							}




							public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
								R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
							}




							public String getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR10_BANK_SPEC_SINGLE_CUST_REC_NUM(String r10_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R10_BANK_SPEC_SINGLE_CUST_REC_NUM = r10_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR10_COMPANY_NAME() {
								return R10_COMPANY_NAME;
							}




							public void setR10_COMPANY_NAME(String r10_COMPANY_NAME) {
								R10_COMPANY_NAME = r10_COMPANY_NAME;
							}




							public String getR10_COMPANY_REG_NUM() {
								return R10_COMPANY_REG_NUM;
							}




							public void setR10_COMPANY_REG_NUM(String r10_COMPANY_REG_NUM) {
								R10_COMPANY_REG_NUM = r10_COMPANY_REG_NUM;
							}




							public String getR10_BUSINEES_PHY_ADDRESS() {
								return R10_BUSINEES_PHY_ADDRESS;
							}




							public void setR10_BUSINEES_PHY_ADDRESS(String r10_BUSINEES_PHY_ADDRESS) {
								R10_BUSINEES_PHY_ADDRESS = r10_BUSINEES_PHY_ADDRESS;
							}




							public String getR10_POSTAL_ADDRESS() {
								return R10_POSTAL_ADDRESS;
							}




							public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
								R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
							}




							public String getR10_COUNTRY_OF_REG() {
								return R10_COUNTRY_OF_REG;
							}




							public void setR10_COUNTRY_OF_REG(String r10_COUNTRY_OF_REG) {
								R10_COUNTRY_OF_REG = r10_COUNTRY_OF_REG;
							}




							public String getR10_COMPANY_EMAIL() {
								return R10_COMPANY_EMAIL;
							}




							public void setR10_COMPANY_EMAIL(String r10_COMPANY_EMAIL) {
								R10_COMPANY_EMAIL = r10_COMPANY_EMAIL;
							}




							public String getR10_COMPANY_LANDLINE() {
								return R10_COMPANY_LANDLINE;
							}




							public void setR10_COMPANY_LANDLINE(String r10_COMPANY_LANDLINE) {
								R10_COMPANY_LANDLINE = r10_COMPANY_LANDLINE;
							}




							public String getR10_COMPANY_MOB_PHONE_NUM() {
								return R10_COMPANY_MOB_PHONE_NUM;
							}




							public void setR10_COMPANY_MOB_PHONE_NUM(String r10_COMPANY_MOB_PHONE_NUM) {
								R10_COMPANY_MOB_PHONE_NUM = r10_COMPANY_MOB_PHONE_NUM;
							}




							public String getR10_PRODUCT_TYPE() {
								return R10_PRODUCT_TYPE;
							}




							public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
								R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
							}




							public BigDecimal getR10_ACCT_NUM() {
								return R10_ACCT_NUM;
							}




							public void setR10_ACCT_NUM(BigDecimal r10_ACCT_NUM) {
								R10_ACCT_NUM = r10_ACCT_NUM;
							}




							public String getR10_STATUS_OF_ACCT() {
								return R10_STATUS_OF_ACCT;
							}




							public void setR10_STATUS_OF_ACCT(String r10_STATUS_OF_ACCT) {
								R10_STATUS_OF_ACCT = r10_STATUS_OF_ACCT;
							}




							public String getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR10_ACCT_BRANCH() {
								return R10_ACCT_BRANCH;
							}




							public void setR10_ACCT_BRANCH(String r10_ACCT_BRANCH) {
								R10_ACCT_BRANCH = r10_ACCT_BRANCH;
							}




							public BigDecimal getR10_ACCT_BALANCE_PULA() {
								return R10_ACCT_BALANCE_PULA;
							}




							public void setR10_ACCT_BALANCE_PULA(BigDecimal r10_ACCT_BALANCE_PULA) {
								R10_ACCT_BALANCE_PULA = r10_ACCT_BALANCE_PULA;
							}




							public String getR10_CURRENCY_OF_ACCT() {
								return R10_CURRENCY_OF_ACCT;
							}




							public void setR10_CURRENCY_OF_ACCT(String r10_CURRENCY_OF_ACCT) {
								R10_CURRENCY_OF_ACCT = r10_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR10_EXCHANGE_RATE() {
								return R10_EXCHANGE_RATE;
							}




							public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
								R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
							}




							public String getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR11_BANK_SPEC_SINGLE_CUST_REC_NUM(String r11_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R11_BANK_SPEC_SINGLE_CUST_REC_NUM = r11_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR11_COMPANY_NAME() {
								return R11_COMPANY_NAME;
							}




							public void setR11_COMPANY_NAME(String r11_COMPANY_NAME) {
								R11_COMPANY_NAME = r11_COMPANY_NAME;
							}




							public String getR11_COMPANY_REG_NUM() {
								return R11_COMPANY_REG_NUM;
							}




							public void setR11_COMPANY_REG_NUM(String r11_COMPANY_REG_NUM) {
								R11_COMPANY_REG_NUM = r11_COMPANY_REG_NUM;
							}




							public String getR11_BUSINEES_PHY_ADDRESS() {
								return R11_BUSINEES_PHY_ADDRESS;
							}




							public void setR11_BUSINEES_PHY_ADDRESS(String r11_BUSINEES_PHY_ADDRESS) {
								R11_BUSINEES_PHY_ADDRESS = r11_BUSINEES_PHY_ADDRESS;
							}




							public String getR11_POSTAL_ADDRESS() {
								return R11_POSTAL_ADDRESS;
							}




							public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
								R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
							}




							public String getR11_COUNTRY_OF_REG() {
								return R11_COUNTRY_OF_REG;
							}




							public void setR11_COUNTRY_OF_REG(String r11_COUNTRY_OF_REG) {
								R11_COUNTRY_OF_REG = r11_COUNTRY_OF_REG;
							}




							public String getR11_COMPANY_EMAIL() {
								return R11_COMPANY_EMAIL;
							}




							public void setR11_COMPANY_EMAIL(String r11_COMPANY_EMAIL) {
								R11_COMPANY_EMAIL = r11_COMPANY_EMAIL;
							}




							public String getR11_COMPANY_LANDLINE() {
								return R11_COMPANY_LANDLINE;
							}




							public void setR11_COMPANY_LANDLINE(String r11_COMPANY_LANDLINE) {
								R11_COMPANY_LANDLINE = r11_COMPANY_LANDLINE;
							}




							public String getR11_COMPANY_MOB_PHONE_NUM() {
								return R11_COMPANY_MOB_PHONE_NUM;
							}




							public void setR11_COMPANY_MOB_PHONE_NUM(String r11_COMPANY_MOB_PHONE_NUM) {
								R11_COMPANY_MOB_PHONE_NUM = r11_COMPANY_MOB_PHONE_NUM;
							}




							public String getR11_PRODUCT_TYPE() {
								return R11_PRODUCT_TYPE;
							}




							public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
								R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
							}




							public BigDecimal getR11_ACCT_NUM() {
								return R11_ACCT_NUM;
							}




							public void setR11_ACCT_NUM(BigDecimal r11_ACCT_NUM) {
								R11_ACCT_NUM = r11_ACCT_NUM;
							}




							public String getR11_STATUS_OF_ACCT() {
								return R11_STATUS_OF_ACCT;
							}




							public void setR11_STATUS_OF_ACCT(String r11_STATUS_OF_ACCT) {
								R11_STATUS_OF_ACCT = r11_STATUS_OF_ACCT;
							}




							public String getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR11_ACCT_BRANCH() {
								return R11_ACCT_BRANCH;
							}




							public void setR11_ACCT_BRANCH(String r11_ACCT_BRANCH) {
								R11_ACCT_BRANCH = r11_ACCT_BRANCH;
							}




							public BigDecimal getR11_ACCT_BALANCE_PULA() {
								return R11_ACCT_BALANCE_PULA;
							}




							public void setR11_ACCT_BALANCE_PULA(BigDecimal r11_ACCT_BALANCE_PULA) {
								R11_ACCT_BALANCE_PULA = r11_ACCT_BALANCE_PULA;
							}




							public String getR11_CURRENCY_OF_ACCT() {
								return R11_CURRENCY_OF_ACCT;
							}




							public void setR11_CURRENCY_OF_ACCT(String r11_CURRENCY_OF_ACCT) {
								R11_CURRENCY_OF_ACCT = r11_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR11_EXCHANGE_RATE() {
								return R11_EXCHANGE_RATE;
							}




							public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
								R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
							}




							public String getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() {
								return R12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public void setR12_BANK_SPEC_SINGLE_CUST_REC_NUM(String r12_BANK_SPEC_SINGLE_CUST_REC_NUM) {
								R12_BANK_SPEC_SINGLE_CUST_REC_NUM = r12_BANK_SPEC_SINGLE_CUST_REC_NUM;
							}




							public String getR12_COMPANY_NAME() {
								return R12_COMPANY_NAME;
							}




							public void setR12_COMPANY_NAME(String r12_COMPANY_NAME) {
								R12_COMPANY_NAME = r12_COMPANY_NAME;
							}




							public String getR12_COMPANY_REG_NUM() {
								return R12_COMPANY_REG_NUM;
							}




							public void setR12_COMPANY_REG_NUM(String r12_COMPANY_REG_NUM) {
								R12_COMPANY_REG_NUM = r12_COMPANY_REG_NUM;
							}




							public String getR12_BUSINEES_PHY_ADDRESS() {
								return R12_BUSINEES_PHY_ADDRESS;
							}




							public void setR12_BUSINEES_PHY_ADDRESS(String r12_BUSINEES_PHY_ADDRESS) {
								R12_BUSINEES_PHY_ADDRESS = r12_BUSINEES_PHY_ADDRESS;
							}




							public String getR12_POSTAL_ADDRESS() {
								return R12_POSTAL_ADDRESS;
							}




							public void setR12_POSTAL_ADDRESS(String r12_POSTAL_ADDRESS) {
								R12_POSTAL_ADDRESS = r12_POSTAL_ADDRESS;
							}




							public String getR12_COUNTRY_OF_REG() {
								return R12_COUNTRY_OF_REG;
							}




							public void setR12_COUNTRY_OF_REG(String r12_COUNTRY_OF_REG) {
								R12_COUNTRY_OF_REG = r12_COUNTRY_OF_REG;
							}




							public String getR12_COMPANY_EMAIL() {
								return R12_COMPANY_EMAIL;
							}




							public void setR12_COMPANY_EMAIL(String r12_COMPANY_EMAIL) {
								R12_COMPANY_EMAIL = r12_COMPANY_EMAIL;
							}




							public String getR12_COMPANY_LANDLINE() {
								return R12_COMPANY_LANDLINE;
							}




							public void setR12_COMPANY_LANDLINE(String r12_COMPANY_LANDLINE) {
								R12_COMPANY_LANDLINE = r12_COMPANY_LANDLINE;
							}




							public String getR12_COMPANY_MOB_PHONE_NUM() {
								return R12_COMPANY_MOB_PHONE_NUM;
							}




							public void setR12_COMPANY_MOB_PHONE_NUM(String r12_COMPANY_MOB_PHONE_NUM) {
								R12_COMPANY_MOB_PHONE_NUM = r12_COMPANY_MOB_PHONE_NUM;
							}




							public String getR12_PRODUCT_TYPE() {
								return R12_PRODUCT_TYPE;
							}




							public void setR12_PRODUCT_TYPE(String r12_PRODUCT_TYPE) {
								R12_PRODUCT_TYPE = r12_PRODUCT_TYPE;
							}




							public BigDecimal getR12_ACCT_NUM() {
								return R12_ACCT_NUM;
							}




							public void setR12_ACCT_NUM(BigDecimal r12_ACCT_NUM) {
								R12_ACCT_NUM = r12_ACCT_NUM;
							}




							public String getR12_STATUS_OF_ACCT() {
								return R12_STATUS_OF_ACCT;
							}




							public void setR12_STATUS_OF_ACCT(String r12_STATUS_OF_ACCT) {
								R12_STATUS_OF_ACCT = r12_STATUS_OF_ACCT;
							}




							public String getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() {
								return R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public void setR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT(
									String r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT) {
								R12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT = r12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT;
							}




							public String getR12_ACCT_BRANCH() {
								return R12_ACCT_BRANCH;
							}




							public void setR12_ACCT_BRANCH(String r12_ACCT_BRANCH) {
								R12_ACCT_BRANCH = r12_ACCT_BRANCH;
							}




							public BigDecimal getR12_ACCT_BALANCE_PULA() {
								return R12_ACCT_BALANCE_PULA;
							}




							public void setR12_ACCT_BALANCE_PULA(BigDecimal r12_ACCT_BALANCE_PULA) {
								R12_ACCT_BALANCE_PULA = r12_ACCT_BALANCE_PULA;
							}




							public String getR12_CURRENCY_OF_ACCT() {
								return R12_CURRENCY_OF_ACCT;
							}




							public void setR12_CURRENCY_OF_ACCT(String r12_CURRENCY_OF_ACCT) {
								R12_CURRENCY_OF_ACCT = r12_CURRENCY_OF_ACCT;
							}




							public BigDecimal getR12_EXCHANGE_RATE() {
								return R12_EXCHANGE_RATE;
							}




							public void setR12_EXCHANGE_RATE(BigDecimal r12_EXCHANGE_RATE) {
								R12_EXCHANGE_RATE = r12_EXCHANGE_RATE;
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

	                        public Date getREPORT_RESUBDATE() {
								return REPORT_RESUBDATE;
							}

							public void setREPORT_RESUBDATE(Date REPORT_RESUBDATE) {
								this.REPORT_RESUBDATE = REPORT_RESUBDATE;
							}
							public String getREPORT_FREQUENCY() {
								return REPORT_FREQUENCY;
							}

							public void setREPORT_FREQUENCY(String rEPORT_FREQUENCY) {
								REPORT_FREQUENCY = rEPORT_FREQUENCY;
							}

							public String getREPORT_CODE() {
								return REPORT_CODE;
							}

							public void setREPORT_CODE(String rEPORT_CODE) {
								REPORT_CODE = rEPORT_CODE;
							}

							public String getREPORT_DESC() {
								return REPORT_DESC;
							}

							public void setREPORT_DESC(String rEPORT_DESC) {
								REPORT_DESC = rEPORT_DESC;
							}

							public String getENTITY_FLG() {
								return ENTITY_FLG;
							}

							public void setENTITY_FLG(String eNTITY_FLG) {
								ENTITY_FLG = eNTITY_FLG;
							}

							public String getMODIFY_FLG() {
								return MODIFY_FLG;
							}

							public void setMODIFY_FLG(String mODIFY_FLG) {
								MODIFY_FLG = mODIFY_FLG;
							}

							public String getDEL_FLG() {
								return DEL_FLG;
							}

							public void setDEL_FLG(String dEL_FLG) {
								DEL_FLG = dEL_FLG;
							}

						}
		

		

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_BDISB2View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date dt = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + dt);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<BDISB2_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<BDISB2_RESUB_Summary_Entity> T1Master = getdatabydateListresub1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<BDISB2_Summary_Entity> T1Master = getDataByDate1(dt);
				
				System.out.println("T1Master Size: " + T1Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<BDISB2_Archival_Detail_Entity> T1Master = getdatabydateListArchivalDetail1(dt, version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<BDISB2_RESUB_Detail_Entity> T1Master = getdatabydateListResubDetail1(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);

				}
				// DETAIL + NORMAL
				else {

					List<BDISB2_Detail_Entity> T1Master = getDetaildatabydateList1(dt);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB2");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}
	

	
	public void updateResubReport(
	        BDISB2_RESUB_Summary_Entity updatedEntity1) {

	    // ====================================================
	    // 1️⃣ GET REPORT DATE
	    // ====================================================

	    Date reportDate1 = updatedEntity1.getREPORT_DATE();

	    if (reportDate1 == null ) {
	        throw new RuntimeException("Report date cannot be null");
	    }

	    // ====================================================
	    // 2️⃣ FETCH MAX VERSION
	    // ====================================================

	    BigDecimal maxVer1 = RESUBfindMaxVersion1(reportDate1);

	    if (maxVer1 == null)
	        maxVer1 = BigDecimal.ZERO;

	   

	    BigDecimal currentMax = maxVer1;
	    BigDecimal newVersion = currentMax.add(BigDecimal.ONE);

	    Date now = new Date();

	    // ====================================================
	    // 3️⃣ RESUB SUMMARY
	    // ====================================================

	    BDISB2_RESUB_Summary_Entity resubSummary1 = new BDISB2_RESUB_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubSummary1);

	    resubSummary1.setREPORT_DATE(reportDate1);
	    resubSummary1.setREPORT_VERSION(newVersion);
	    resubSummary1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 4️⃣ RESUB DETAIL
	    // ====================================================

	    BDISB2_RESUB_Detail_Entity resubDetail1 = new BDISB2_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubDetail1);

	    resubDetail1.setREPORT_DATE(reportDate1);
	    resubDetail1.setREPORT_VERSION(newVersion);
	    resubDetail1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 5️⃣ ARCHIVAL SUMMARY
	    // ====================================================

	    BDISB2_Archival_Summary_Entity archSummary1 = new BDISB2_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, archSummary1);

	    archSummary1.setREPORT_DATE(reportDate1);
	    archSummary1.setREPORT_VERSION(newVersion);
	    archSummary1.setREPORT_RESUBDATE(now);


	    // ====================================================
	    // 6️⃣ ARCHIVAL DETAIL
	    // ====================================================

	    BDISB2_Archival_Detail_Entity archDetail1 = new BDISB2_Archival_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity1, archDetail1);

	    archDetail1.setREPORT_DATE(reportDate1);
	    archDetail1.setREPORT_VERSION(newVersion);
	    archDetail1.setREPORT_RESUBDATE(now);

	   

	    // ====================================================
	    // 7️⃣ SAVE ALL
	    // ====================================================

	    sessionFactory.getCurrentSession().merge(resubSummary1);

	    sessionFactory.getCurrentSession().merge(resubDetail1);

	    sessionFactory.getCurrentSession().merge(archSummary1);

	    sessionFactory.getCurrentSession().merge(archDetail1);
	}


	@Transactional
	public void updateReport(BDISB2_Summary_Entity request1) {

	    try {

	        StringBuilder sql = new StringBuilder(
	                "UPDATE BRRS_BDISB2_SUMMARYTABLE SET ");

	        List<Object> params = new ArrayList<>();

	        for (int i = 6; i <= 12; i++) {

	            sql.append("R").append(i).append("_BANK_SPEC_SINGLE_CUST_REC_NUM=?,")
	               .append("R").append(i).append("_COMPANY_NAME=?,")
	               .append("R").append(i).append("_COMPANY_REG_NUM=?,")
	               .append("R").append(i).append("_BUSINEES_PHY_ADDRESS=?,")
	               .append("R").append(i).append("_POSTAL_ADDRESS=?,")
	               .append("R").append(i).append("_COUNTRY_OF_REG=?,")
	               .append("R").append(i).append("_COMPANY_EMAIL=?,")
	               .append("R").append(i).append("_COMPANY_LANDLINE=?,")
	               .append("R").append(i).append("_COMPANY_MOB_PHONE_NUM=?,")
	               .append("R").append(i).append("_PRODUCT_TYPE=?,")
	               .append("R").append(i).append("_ACCT_NUM=?,")
	               .append("R").append(i).append("_STATUS_OF_ACCT=?,")
	               .append("R").append(i).append("_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT=?,")
	               .append("R").append(i).append("_ACCT_BRANCH=?,")
	               .append("R").append(i).append("_ACCT_BALANCE_PULA=?,")
	               .append("R").append(i).append("_CURRENCY_OF_ACCT=?,")
	               .append("R").append(i).append("_EXCHANGE_RATE=?,");

	            params.add(getValue(request1, "getR" + i + "_BANK_SPEC_SINGLE_CUST_REC_NUM"));
	            params.add(getValue(request1, "getR" + i + "_COMPANY_NAME"));
	            params.add(getValue(request1, "getR" + i + "_COMPANY_REG_NUM"));
	            params.add(getValue(request1, "getR" + i + "_BUSINEES_PHY_ADDRESS"));
	            params.add(getValue(request1, "getR" + i + "_POSTAL_ADDRESS"));
	            params.add(getValue(request1, "getR" + i + "_COUNTRY_OF_REG"));
	            params.add(getValue(request1, "getR" + i + "_COMPANY_EMAIL"));
	            params.add(getValue(request1, "getR" + i + "_COMPANY_LANDLINE"));
	            params.add(getValue(request1, "getR" + i + "_COMPANY_MOB_PHONE_NUM"));
	            params.add(getValue(request1, "getR" + i + "_PRODUCT_TYPE"));
	            params.add(getValue(request1, "getR" + i + "_ACCT_NUM"));
	            params.add(getValue(request1, "getR" + i + "_STATUS_OF_ACCT"));
	            params.add(getValue(request1, "getR" + i + "_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT"));
	            params.add(getValue(request1, "getR" + i + "_ACCT_BRANCH"));
	            params.add(getValue(request1, "getR" + i + "_ACCT_BALANCE_PULA"));
	            params.add(getValue(request1, "getR" + i + "_CURRENCY_OF_ACCT"));
	            params.add(getValue(request1, "getR" + i + "_EXCHANGE_RATE"));
	        }

	        // Remove last comma
	        sql.deleteCharAt(sql.length() - 1);

	        sql.append(" WHERE REPORT_DATE=?");

	        params.add(request1.getREPORT_DATE());

	        int count = jdbcTemplate.update(sql.toString(), params.toArray());

	        System.out.println("=================================");
	        System.out.println("Rows Updated = " + count);
	        System.out.println("BDISB2 Summary Updated Successfully");
	        System.out.println("=================================");

	    } catch (Exception e) {

	        e.printStackTrace();

	        throw new RuntimeException(
	                "Error while updating BRRS_BDISB2 Report", e);
	    }
	}

	private Object getValue(Object obj, String methodName) {
	    try {
	        return obj.getClass()
	                  .getMethod(methodName)
	                  .invoke(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getBRRS_BDISB2Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<BDISB2_Archival_Summary_Entity> latestArchivalList = getdatabydateListWithVersion1();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (BDISB2_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BRRS_BDISB2 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getBRRS_BDISB2Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " + "FROM BRRS_BDISB2_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	
	//NORMAL EXCEL
	public byte[] getBDISB2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null ) {
			
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelBDISB2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null ) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<BDISB2_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dateformat.parse(todate), version);

			// Generate Excel for RESUB
			return BRRS_BDISB2ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<BDISB2_Summary_Entity> dataList1 = getDataByDate1(reportDate);

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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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


	//ARCHIVAL EXCEL

	public byte[] getExcelBDISB2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<BDISB2_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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
			int startRow = 10;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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


	
	//RESUB EXCEL


	public byte[] BRRS_BDISB2ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<BDISB2_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB2 report. Returning empty result.");
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
			int startRow = 10;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {
					BDISB2_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD, cellE, cellF, cellG, cellH, cellI, cellJ, cellK, cellL, cellM,
							cellN, cellO, cellP, cellQ;
					CellStyle originalStyle;

					// ===== R6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR6_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R6 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR6_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R6 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR6_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R6 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR6_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R6 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR6_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R6 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR6_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R6 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR6_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R6 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR6_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R6 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR6_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R6 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR6_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R6 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR6_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR6_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R6 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR6_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R6 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR6_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R6 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR6_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R6 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR6_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR6_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R6 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR6_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R6 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR6_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR7_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R7 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR7_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R7 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR7_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R7 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR7_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R7 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR7_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R7 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR7_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R7 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR7_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R7 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR7_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R7 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR7_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R7 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR7_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R7 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR7_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR7_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R7 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR7_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R7 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR7_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R7 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR7_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R7 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR7_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR7_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R7 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR7_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R7 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR7_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR8_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R8 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR8_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R8 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR8_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R8 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR8_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R8 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR8_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R8 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR8_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R8 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR8_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R8 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR8_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R8 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR8_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R8 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR8_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R8 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR8_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR8_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R8 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR8_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R8 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR8_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R8 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR8_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R8 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR8_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR8_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R8 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR8_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R8 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR8_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR9_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R9 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR9_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R9 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR9_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R9 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR9_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R9 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR9_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R9 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR9_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R9 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR9_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R9 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR9_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R9 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR9_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R9 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR9_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R9 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR9_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR9_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R9 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR9_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R9 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR9_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R9 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR9_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R9 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR9_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR9_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R9 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR9_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R9 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR9_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR10_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R10 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR10_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R10 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR10_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R10 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR10_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R10 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR10_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R10 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR10_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R10 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR10_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R10 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR10_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R10 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR10_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R10 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR10_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R10 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR10_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR10_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R10 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR10_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R10 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR10_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R10 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR10_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R10 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR10_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR10_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R10 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR10_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R10 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR10_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR11_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R11 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR11_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR11_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR11_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R11 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR11_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R11 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR11_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R11 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR11_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R11 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR11_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R11 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR11_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R11 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR11_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R11 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR11_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR11_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R11 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR11_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R11 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR11_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R11 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR11_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R11 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR11_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR11_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R11 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR11_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R11 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR11_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

					// ===== R12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(0);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM() != null)
						cellA.setCellValue(record1.getR12_BANK_SPEC_SINGLE_CUST_REC_NUM()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== R12 / Col B =====

					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_NAME() != null)
						cellB.setCellValue(record1.getR12_COMPANY_NAME()); // String directly
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R12 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_REG_NUM() != null)
						cellC.setCellValue(record1.getR12_COMPANY_REG_NUM()); // String directly
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R12 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_BUSINEES_PHY_ADDRESS() != null)
						cellD.setCellValue(record1.getR12_BUSINEES_PHY_ADDRESS()); // String directly
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== R12 / Col E =====

					cellE = row.getCell(4);
					if (cellE == null)
						cellE = row.createCell(4);
					originalStyle = cellE.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_POSTAL_ADDRESS() != null)
						cellE.setCellValue(record1.getR12_POSTAL_ADDRESS()); // String directly
					else
						cellE.setCellValue("");
					cellE.setCellStyle(originalStyle);

					// ===== R12 / Col F =====

					cellF = row.getCell(5);
					if (cellF == null)
						cellF = row.createCell(5);
					originalStyle = cellF.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COUNTRY_OF_REG() != null)
						cellF.setCellValue(record1.getR12_COUNTRY_OF_REG()); // String directly
					else
						cellF.setCellValue("");
					cellF.setCellStyle(originalStyle);

					// ===== R12 / Col G =====

					cellG = row.getCell(6);
					if (cellG == null)
						cellG = row.createCell(6);
					originalStyle = cellG.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_EMAIL() != null)
						cellG.setCellValue(record1.getR12_COMPANY_EMAIL()); // String directly
					else
						cellG.setCellValue("");
					cellG.setCellStyle(originalStyle);

					// ===== R12 / Col H =====

					cellH = row.getCell(7);
					if (cellH == null)
						cellH = row.createCell(7);
					originalStyle = cellH.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_LANDLINE() != null)
						cellH.setCellValue(record1.getR12_COMPANY_LANDLINE()); // String directly
					else
						cellH.setCellValue("");
					cellH.setCellStyle(originalStyle);

					// ===== R12 / Col I =====

					cellI = row.getCell(8);
					if (cellI == null)
						cellI = row.createCell(8);
					originalStyle = cellI.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_COMPANY_MOB_PHONE_NUM() != null)
						cellI.setCellValue(record1.getR12_COMPANY_MOB_PHONE_NUM()); // String directly
					else
						cellI.setCellValue("");
					cellI.setCellStyle(originalStyle);

					// ===== R12 / Col J =====

					cellJ = row.getCell(9);
					if (cellJ == null)
						cellJ = row.createCell(9);
					originalStyle = cellJ.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_PRODUCT_TYPE() != null)
						cellJ.setCellValue(record1.getR12_PRODUCT_TYPE()); // String directly
					else
						cellJ.setCellValue("");
					cellJ.setCellStyle(originalStyle);

					// ===== R12 / Col K =====

					cellK = row.getCell(10);
					if (cellK == null)
						cellK = row.createCell(10);
					originalStyle = cellK.getCellStyle();
					if (record1.getR12_ACCT_NUM() != null)
						cellK.setCellValue(record1.getR12_ACCT_NUM().doubleValue());
					else
						cellK.setCellValue("");
					cellK.setCellStyle(originalStyle);

					// ===== R12 / Col L =====

					cellL = row.getCell(11);
					if (cellL == null)
						cellL = row.createCell(11);
					originalStyle = cellL.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_STATUS_OF_ACCT() != null)
						cellL.setCellValue(record1.getR12_STATUS_OF_ACCT()); // String directly
					else
						cellL.setCellValue("");
					cellL.setCellStyle(originalStyle);

					// ===== R12 / Col M =====

					cellM = row.getCell(12);
					if (cellM == null)
						cellM = row.createCell(12);
					originalStyle = cellM.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT() != null)
						cellM.setCellValue(record1.getR12_ACCT_STATUS_FIT_OR_NOT_FIT_FOR_STRAIGHT_THROU_PAYOUT()); // String
																													// directly
					else
						cellM.setCellValue("");
					cellM.setCellStyle(originalStyle);

					// ===== R12 / Col N =====

					cellN = row.getCell(13);
					if (cellN == null)
						cellN = row.createCell(13);
					originalStyle = cellN.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_ACCT_BRANCH() != null)
						cellN.setCellValue(record1.getR12_ACCT_BRANCH()); // String directly
					else
						cellN.setCellValue("");
					cellN.setCellStyle(originalStyle);

					// ===== R12 / Col O =====

					cellO = row.getCell(14);
					if (cellO == null)
						cellO = row.createCell(14);
					originalStyle = cellO.getCellStyle();
					if (record1.getR12_ACCT_BALANCE_PULA() != null)
						cellO.setCellValue(record1.getR12_ACCT_BALANCE_PULA().doubleValue());
					else
						cellO.setCellValue("");
					cellO.setCellStyle(originalStyle);

					// ===== R12 / Col P =====

					cellP = row.getCell(15);
					if (cellP == null)
						cellP = row.createCell(15);
					originalStyle = cellP.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_CURRENCY_OF_ACCT() != null)
						cellP.setCellValue(record1.getR12_CURRENCY_OF_ACCT()); // String directly
					else
						cellP.setCellValue("");
					cellP.setCellStyle(originalStyle);

					// ===== R12 / Col Q =====

					cellQ = row.getCell(16);
					if (cellQ == null)
						cellQ = row.createCell(16);
					originalStyle = cellQ.getCellStyle();
					if (record1.getR12_EXCHANGE_RATE() != null)
						cellQ.setCellValue(record1.getR12_EXCHANGE_RATE().doubleValue());
					else
						cellQ.setCellValue("");
					cellQ.setCellStyle(originalStyle);

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

	

}
