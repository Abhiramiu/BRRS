package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_Market_Risk_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Market_Risk_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	AuditService auditService;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<Market_Risk_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Market_RiskRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getMarket_RiskArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MARKET_RISK_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Market_Risk_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new Market_RiskArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<Market_Risk_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Market_RiskArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MARKET_RISK_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Market_Risk_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new Market_RiskDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Market_Risk_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Market_RiskDetailRowMapper());
	}

// 3. PAGINATION

	public List<Market_Risk_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new Market_RiskDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_MARKET_RISK_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Market_Risk_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Market_RiskDetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public Market_Risk_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new Market_RiskDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<Market_Risk_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new Market_RiskArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<Market_Risk_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MARKET_RISK_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new Market_RiskArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class Market_RiskRowMapper implements RowMapper<Market_Risk_Summary_Entity> {

		@Override
		public Market_Risk_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Market_Risk_Summary_Entity obj = new Market_Risk_Summary_Entity();

			// ================= R5 =================
			obj.setR5_QUALITATIVE_DISCLOSURES(rs.getString("R5_QUALITATIVE_DISCLOSURES"));
			obj.setR5_PRODUCT(rs.getString("R5_PRODUCT"));
			obj.setR5_QUAN_DIS(rs.getBigDecimal("R5_QUAN_DIS"));

// ================= R6 =================
			obj.setR6_QUALITATIVE_DISCLOSURES(rs.getString("R6_QUALITATIVE_DISCLOSURES"));
			obj.setR6_PRODUCT(rs.getString("R6_PRODUCT"));
			obj.setR6_QUAN_DIS(rs.getBigDecimal("R6_QUAN_DIS"));

// ================= R11 =================
			obj.setR11_QUALITATIVE_DISCLOSURES(rs.getString("R11_QUALITATIVE_DISCLOSURES"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_QUAN_DIS(rs.getBigDecimal("R11_QUAN_DIS"));

// ================= R12 =================
			obj.setR12_QUALITATIVE_DISCLOSURES(rs.getString("R12_QUALITATIVE_DISCLOSURES"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_QUAN_DIS(rs.getBigDecimal("R12_QUAN_DIS"));

// ================= R13 =================
			obj.setR13_QUALITATIVE_DISCLOSURES(rs.getString("R13_QUALITATIVE_DISCLOSURES"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_QUAN_DIS(rs.getBigDecimal("R13_QUAN_DIS"));

// ================= R14 =================
			obj.setR14_QUALITATIVE_DISCLOSURES(rs.getString("R14_QUALITATIVE_DISCLOSURES"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_QUAN_DIS(rs.getBigDecimal("R14_QUAN_DIS"));

// ================= R15 =================
			obj.setR15_QUALITATIVE_DISCLOSURES(rs.getString("R15_QUALITATIVE_DISCLOSURES"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_QUAN_DIS(rs.getBigDecimal("R15_QUAN_DIS"));

// ================= R16 =================
			obj.setR16_QUALITATIVE_DISCLOSURES(rs.getString("R16_QUALITATIVE_DISCLOSURES"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_QUAN_DIS(rs.getBigDecimal("R16_QUAN_DIS"));

// ================= R20 =================
			obj.setR20_QUALITATIVE_DISCLOSURES(rs.getString("R20_QUALITATIVE_DISCLOSURES"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_QUAN_DIS(rs.getBigDecimal("R20_QUAN_DIS"));

// ================= R21 =================
			obj.setR21_QUALITATIVE_DISCLOSURES(rs.getString("R21_QUALITATIVE_DISCLOSURES"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_QUAN_DIS(rs.getBigDecimal("R21_QUAN_DIS"));

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

	public static class Market_Risk_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		/* ================= R5 ================= */

		@Column(name = "R5_QUALITATIVE_DISCLOSURES")
		private String R5_QUALITATIVE_DISCLOSURES;

		@Column(name = "R5_PRODUCT")
		private String R5_PRODUCT;

		@Column(name = "R5_QUAN_DIS")
		private BigDecimal R5_QUAN_DIS;

		/* ================= R6 ================= */

		@Column(name = "R6_QUALITATIVE_DISCLOSURES")
		private String R6_QUALITATIVE_DISCLOSURES;

		@Column(name = "R6_PRODUCT")
		private String R6_PRODUCT;

		@Column(name = "R6_QUAN_DIS")
		private BigDecimal R6_QUAN_DIS;

		/* ================= R11 ================= */

		@Column(name = "R11_QUALITATIVE_DISCLOSURES")
		private String R11_QUALITATIVE_DISCLOSURES;

		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;

		@Column(name = "R11_QUAN_DIS")
		private BigDecimal R11_QUAN_DIS;

		/* ================= R12 ================= */

		@Column(name = "R12_QUALITATIVE_DISCLOSURES")
		private String R12_QUALITATIVE_DISCLOSURES;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;

		@Column(name = "R12_QUAN_DIS")
		private BigDecimal R12_QUAN_DIS;

		/* ================= R13 ================= */

		@Column(name = "R13_QUALITATIVE_DISCLOSURES")
		private String R13_QUALITATIVE_DISCLOSURES;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;

		@Column(name = "R13_QUAN_DIS")
		private BigDecimal R13_QUAN_DIS;

		/* ================= R14 ================= */

		@Column(name = "R14_QUALITATIVE_DISCLOSURES")
		private String R14_QUALITATIVE_DISCLOSURES;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;

		@Column(name = "R14_QUAN_DIS")
		private BigDecimal R14_QUAN_DIS;

		/* ================= R15 ================= */

		@Column(name = "R15_QUALITATIVE_DISCLOSURES")
		private String R15_QUALITATIVE_DISCLOSURES;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;

		@Column(name = "R15_QUAN_DIS")
		private BigDecimal R15_QUAN_DIS;

		/* ================= R16 ================= */

		@Column(name = "R16_QUALITATIVE_DISCLOSURES")
		private String R16_QUALITATIVE_DISCLOSURES;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;

		@Column(name = "R16_QUAN_DIS")
		private BigDecimal R16_QUAN_DIS;

		/* ================= R20 ================= */

		@Column(name = "R20_QUALITATIVE_DISCLOSURES")
		private String R20_QUALITATIVE_DISCLOSURES;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;

		@Column(name = "R20_QUAN_DIS")
		private BigDecimal R20_QUAN_DIS;

		/* ================= R21 ================= */
		@Column(name = "R21_QUALITATIVE_DISCLOSURES")
		private String R21_QUALITATIVE_DISCLOSURES;

		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;

		@Column(name = "R21_QUAN_DIS")
		private BigDecimal R21_QUAN_DIS;

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

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			REPORT_DATE = REPORT_DATE;
		}

		public String getR5_QUALITATIVE_DISCLOSURES() {
			return R5_QUALITATIVE_DISCLOSURES;
		}

		public void setR5_QUALITATIVE_DISCLOSURES(String r5_QUALITATIVE_DISCLOSURES) {
			R5_QUALITATIVE_DISCLOSURES = r5_QUALITATIVE_DISCLOSURES;
		}

		public String getR5_PRODUCT() {
			return R5_PRODUCT;
		}

		public void setR5_PRODUCT(String r5_PRODUCT) {
			R5_PRODUCT = r5_PRODUCT;
		}

		public BigDecimal getR5_QUAN_DIS() {
			return R5_QUAN_DIS;
		}

		public void setR5_QUAN_DIS(BigDecimal r5_QUAN_DIS) {
			R5_QUAN_DIS = r5_QUAN_DIS;
		}

		public String getR6_QUALITATIVE_DISCLOSURES() {
			return R6_QUALITATIVE_DISCLOSURES;
		}

		public void setR6_QUALITATIVE_DISCLOSURES(String r6_QUALITATIVE_DISCLOSURES) {
			R6_QUALITATIVE_DISCLOSURES = r6_QUALITATIVE_DISCLOSURES;
		}

		public String getR6_PRODUCT() {
			return R6_PRODUCT;
		}

		public void setR6_PRODUCT(String r6_PRODUCT) {
			R6_PRODUCT = r6_PRODUCT;
		}

		public BigDecimal getR6_QUAN_DIS() {
			return R6_QUAN_DIS;
		}

		public void setR6_QUAN_DIS(BigDecimal r6_QUAN_DIS) {
			R6_QUAN_DIS = r6_QUAN_DIS;
		}

		public String getR11_QUALITATIVE_DISCLOSURES() {
			return R11_QUALITATIVE_DISCLOSURES;
		}

		public void setR11_QUALITATIVE_DISCLOSURES(String r11_QUALITATIVE_DISCLOSURES) {
			R11_QUALITATIVE_DISCLOSURES = r11_QUALITATIVE_DISCLOSURES;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_QUAN_DIS() {
			return R11_QUAN_DIS;
		}

		public void setR11_QUAN_DIS(BigDecimal r11_QUAN_DIS) {
			R11_QUAN_DIS = r11_QUAN_DIS;
		}

		public String getR12_QUALITATIVE_DISCLOSURES() {
			return R12_QUALITATIVE_DISCLOSURES;
		}

		public void setR12_QUALITATIVE_DISCLOSURES(String r12_QUALITATIVE_DISCLOSURES) {
			R12_QUALITATIVE_DISCLOSURES = r12_QUALITATIVE_DISCLOSURES;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_QUAN_DIS() {
			return R12_QUAN_DIS;
		}

		public void setR12_QUAN_DIS(BigDecimal r12_QUAN_DIS) {
			R12_QUAN_DIS = r12_QUAN_DIS;
		}

		public String getR13_QUALITATIVE_DISCLOSURES() {
			return R13_QUALITATIVE_DISCLOSURES;
		}

		public void setR13_QUALITATIVE_DISCLOSURES(String r13_QUALITATIVE_DISCLOSURES) {
			R13_QUALITATIVE_DISCLOSURES = r13_QUALITATIVE_DISCLOSURES;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_QUAN_DIS() {
			return R13_QUAN_DIS;
		}

		public void setR13_QUAN_DIS(BigDecimal r13_QUAN_DIS) {
			R13_QUAN_DIS = r13_QUAN_DIS;
		}

		public String getR14_QUALITATIVE_DISCLOSURES() {
			return R14_QUALITATIVE_DISCLOSURES;
		}

		public void setR14_QUALITATIVE_DISCLOSURES(String r14_QUALITATIVE_DISCLOSURES) {
			R14_QUALITATIVE_DISCLOSURES = r14_QUALITATIVE_DISCLOSURES;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_QUAN_DIS() {
			return R14_QUAN_DIS;
		}

		public void setR14_QUAN_DIS(BigDecimal r14_QUAN_DIS) {
			R14_QUAN_DIS = r14_QUAN_DIS;
		}

		public String getR15_QUALITATIVE_DISCLOSURES() {
			return R15_QUALITATIVE_DISCLOSURES;
		}

		public void setR15_QUALITATIVE_DISCLOSURES(String r15_QUALITATIVE_DISCLOSURES) {
			R15_QUALITATIVE_DISCLOSURES = r15_QUALITATIVE_DISCLOSURES;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_QUAN_DIS() {
			return R15_QUAN_DIS;
		}

		public void setR15_QUAN_DIS(BigDecimal r15_QUAN_DIS) {
			R15_QUAN_DIS = r15_QUAN_DIS;
		}

		public String getR16_QUALITATIVE_DISCLOSURES() {
			return R16_QUALITATIVE_DISCLOSURES;
		}

		public void setR16_QUALITATIVE_DISCLOSURES(String r16_QUALITATIVE_DISCLOSURES) {
			R16_QUALITATIVE_DISCLOSURES = r16_QUALITATIVE_DISCLOSURES;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_QUAN_DIS() {
			return R16_QUAN_DIS;
		}

		public void setR16_QUAN_DIS(BigDecimal r16_QUAN_DIS) {
			R16_QUAN_DIS = r16_QUAN_DIS;
		}

		public String getR20_QUALITATIVE_DISCLOSURES() {
			return R20_QUALITATIVE_DISCLOSURES;
		}

		public void setR20_QUALITATIVE_DISCLOSURES(String r20_QUALITATIVE_DISCLOSURES) {
			R20_QUALITATIVE_DISCLOSURES = r20_QUALITATIVE_DISCLOSURES;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_QUAN_DIS() {
			return R20_QUAN_DIS;
		}

		public void setR20_QUAN_DIS(BigDecimal r20_QUAN_DIS) {
			R20_QUAN_DIS = r20_QUAN_DIS;
		}

		public String getR21_QUALITATIVE_DISCLOSURES() {
			return R21_QUALITATIVE_DISCLOSURES;
		}

		public void setR21_QUALITATIVE_DISCLOSURES(String r21_QUALITATIVE_DISCLOSURES) {
			R21_QUALITATIVE_DISCLOSURES = r21_QUALITATIVE_DISCLOSURES;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_QUAN_DIS() {
			return R21_QUAN_DIS;
		}

		public void setR21_QUAN_DIS(BigDecimal r21_QUAN_DIS) {
			R21_QUAN_DIS = r21_QUAN_DIS;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			REPORT_VERSION = REPORT_VERSION;
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

//ARCHIVAL ROW MAPPER

	class Market_RiskArchivalRowMapper implements RowMapper<Market_Risk_Archival_Summary_Entity> {

		@Override
		public Market_Risk_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Market_Risk_Archival_Summary_Entity obj = new Market_Risk_Archival_Summary_Entity();

			// ================= R5 =================
			obj.setR5_QUALITATIVE_DISCLOSURES(rs.getString("R5_QUALITATIVE_DISCLOSURES"));
			obj.setR5_PRODUCT(rs.getString("R5_PRODUCT"));
			obj.setR5_QUAN_DIS(rs.getBigDecimal("R5_QUAN_DIS"));

// ================= R6 =================
			obj.setR6_QUALITATIVE_DISCLOSURES(rs.getString("R6_QUALITATIVE_DISCLOSURES"));
			obj.setR6_PRODUCT(rs.getString("R6_PRODUCT"));
			obj.setR6_QUAN_DIS(rs.getBigDecimal("R6_QUAN_DIS"));

// ================= R11 =================
			obj.setR11_QUALITATIVE_DISCLOSURES(rs.getString("R11_QUALITATIVE_DISCLOSURES"));
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_QUAN_DIS(rs.getBigDecimal("R11_QUAN_DIS"));

// ================= R12 =================
			obj.setR12_QUALITATIVE_DISCLOSURES(rs.getString("R12_QUALITATIVE_DISCLOSURES"));
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_QUAN_DIS(rs.getBigDecimal("R12_QUAN_DIS"));

// ================= R13 =================
			obj.setR13_QUALITATIVE_DISCLOSURES(rs.getString("R13_QUALITATIVE_DISCLOSURES"));
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_QUAN_DIS(rs.getBigDecimal("R13_QUAN_DIS"));

// ================= R14 =================
			obj.setR14_QUALITATIVE_DISCLOSURES(rs.getString("R14_QUALITATIVE_DISCLOSURES"));
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_QUAN_DIS(rs.getBigDecimal("R14_QUAN_DIS"));

// ================= R15 =================
			obj.setR15_QUALITATIVE_DISCLOSURES(rs.getString("R15_QUALITATIVE_DISCLOSURES"));
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_QUAN_DIS(rs.getBigDecimal("R15_QUAN_DIS"));

// ================= R16 =================
			obj.setR16_QUALITATIVE_DISCLOSURES(rs.getString("R16_QUALITATIVE_DISCLOSURES"));
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_QUAN_DIS(rs.getBigDecimal("R16_QUAN_DIS"));

// ================= R20 =================
			obj.setR20_QUALITATIVE_DISCLOSURES(rs.getString("R20_QUALITATIVE_DISCLOSURES"));
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_QUAN_DIS(rs.getBigDecimal("R20_QUAN_DIS"));

// ================= R21 =================
			obj.setR21_QUALITATIVE_DISCLOSURES(rs.getString("R21_QUALITATIVE_DISCLOSURES"));
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_QUAN_DIS(rs.getBigDecimal("R21_QUAN_DIS"));

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

	@IdClass(Market_Risk_PK.class)
	public class Market_Risk_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		/* ================= R5 ================= */

		@Column(name = "R5_QUALITATIVE_DISCLOSURES")
		private String R5_QUALITATIVE_DISCLOSURES;

		@Column(name = "R5_PRODUCT")
		private String R5_PRODUCT;

		@Column(name = "R5_QUAN_DIS")
		private BigDecimal R5_QUAN_DIS;

		/* ================= R6 ================= */

		@Column(name = "R6_QUALITATIVE_DISCLOSURES")
		private String R6_QUALITATIVE_DISCLOSURES;

		@Column(name = "R6_PRODUCT")
		private String R6_PRODUCT;

		@Column(name = "R6_QUAN_DIS")
		private BigDecimal R6_QUAN_DIS;

		/* ================= R11 ================= */

		@Column(name = "R11_QUALITATIVE_DISCLOSURES")
		private String R11_QUALITATIVE_DISCLOSURES;

		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;

		@Column(name = "R11_QUAN_DIS")
		private BigDecimal R11_QUAN_DIS;

		/* ================= R12 ================= */

		@Column(name = "R12_QUALITATIVE_DISCLOSURES")
		private String R12_QUALITATIVE_DISCLOSURES;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;

		@Column(name = "R12_QUAN_DIS")
		private BigDecimal R12_QUAN_DIS;

		/* ================= R13 ================= */

		@Column(name = "R13_QUALITATIVE_DISCLOSURES")
		private String R13_QUALITATIVE_DISCLOSURES;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;

		@Column(name = "R13_QUAN_DIS")
		private BigDecimal R13_QUAN_DIS;

		/* ================= R14 ================= */

		@Column(name = "R14_QUALITATIVE_DISCLOSURES")
		private String R14_QUALITATIVE_DISCLOSURES;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;

		@Column(name = "R14_QUAN_DIS")
		private BigDecimal R14_QUAN_DIS;

		/* ================= R15 ================= */

		@Column(name = "R15_QUALITATIVE_DISCLOSURES")
		private String R15_QUALITATIVE_DISCLOSURES;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;

		@Column(name = "R15_QUAN_DIS")
		private BigDecimal R15_QUAN_DIS;

		/* ================= R16 ================= */

		@Column(name = "R16_QUALITATIVE_DISCLOSURES")
		private String R16_QUALITATIVE_DISCLOSURES;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;

		@Column(name = "R16_QUAN_DIS")
		private BigDecimal R16_QUAN_DIS;

		/* ================= R20 ================= */

		@Column(name = "R20_QUALITATIVE_DISCLOSURES")
		private String R20_QUALITATIVE_DISCLOSURES;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;

		@Column(name = "R20_QUAN_DIS")
		private BigDecimal R20_QUAN_DIS;

		/* ================= R21 ================= */
		@Column(name = "R21_QUALITATIVE_DISCLOSURES")
		private String R21_QUALITATIVE_DISCLOSURES;

		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;

		@Column(name = "R21_QUAN_DIS")
		private BigDecimal R21_QUAN_DIS;

		@Id
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

		private Date REPORT_RESUBDATE;

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public String getR5_QUALITATIVE_DISCLOSURES() {
			return R5_QUALITATIVE_DISCLOSURES;
		}

		public void setR5_QUALITATIVE_DISCLOSURES(String r5_QUALITATIVE_DISCLOSURES) {
			R5_QUALITATIVE_DISCLOSURES = r5_QUALITATIVE_DISCLOSURES;
		}

		public String getR5_PRODUCT() {
			return R5_PRODUCT;
		}

		public void setR5_PRODUCT(String r5_PRODUCT) {
			R5_PRODUCT = r5_PRODUCT;
		}

		public BigDecimal getR5_QUAN_DIS() {
			return R5_QUAN_DIS;
		}

		public void setR5_QUAN_DIS(BigDecimal r5_QUAN_DIS) {
			R5_QUAN_DIS = r5_QUAN_DIS;
		}

		public String getR6_QUALITATIVE_DISCLOSURES() {
			return R6_QUALITATIVE_DISCLOSURES;
		}

		public void setR6_QUALITATIVE_DISCLOSURES(String r6_QUALITATIVE_DISCLOSURES) {
			R6_QUALITATIVE_DISCLOSURES = r6_QUALITATIVE_DISCLOSURES;
		}

		public String getR6_PRODUCT() {
			return R6_PRODUCT;
		}

		public void setR6_PRODUCT(String r6_PRODUCT) {
			R6_PRODUCT = r6_PRODUCT;
		}

		public BigDecimal getR6_QUAN_DIS() {
			return R6_QUAN_DIS;
		}

		public void setR6_QUAN_DIS(BigDecimal r6_QUAN_DIS) {
			R6_QUAN_DIS = r6_QUAN_DIS;
		}

		public String getR11_QUALITATIVE_DISCLOSURES() {
			return R11_QUALITATIVE_DISCLOSURES;
		}

		public void setR11_QUALITATIVE_DISCLOSURES(String r11_QUALITATIVE_DISCLOSURES) {
			R11_QUALITATIVE_DISCLOSURES = r11_QUALITATIVE_DISCLOSURES;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_QUAN_DIS() {
			return R11_QUAN_DIS;
		}

		public void setR11_QUAN_DIS(BigDecimal r11_QUAN_DIS) {
			R11_QUAN_DIS = r11_QUAN_DIS;
		}

		public String getR12_QUALITATIVE_DISCLOSURES() {
			return R12_QUALITATIVE_DISCLOSURES;
		}

		public void setR12_QUALITATIVE_DISCLOSURES(String r12_QUALITATIVE_DISCLOSURES) {
			R12_QUALITATIVE_DISCLOSURES = r12_QUALITATIVE_DISCLOSURES;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_QUAN_DIS() {
			return R12_QUAN_DIS;
		}

		public void setR12_QUAN_DIS(BigDecimal r12_QUAN_DIS) {
			R12_QUAN_DIS = r12_QUAN_DIS;
		}

		public String getR13_QUALITATIVE_DISCLOSURES() {
			return R13_QUALITATIVE_DISCLOSURES;
		}

		public void setR13_QUALITATIVE_DISCLOSURES(String r13_QUALITATIVE_DISCLOSURES) {
			R13_QUALITATIVE_DISCLOSURES = r13_QUALITATIVE_DISCLOSURES;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_QUAN_DIS() {
			return R13_QUAN_DIS;
		}

		public void setR13_QUAN_DIS(BigDecimal r13_QUAN_DIS) {
			R13_QUAN_DIS = r13_QUAN_DIS;
		}

		public String getR14_QUALITATIVE_DISCLOSURES() {
			return R14_QUALITATIVE_DISCLOSURES;
		}

		public void setR14_QUALITATIVE_DISCLOSURES(String r14_QUALITATIVE_DISCLOSURES) {
			R14_QUALITATIVE_DISCLOSURES = r14_QUALITATIVE_DISCLOSURES;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_QUAN_DIS() {
			return R14_QUAN_DIS;
		}

		public void setR14_QUAN_DIS(BigDecimal r14_QUAN_DIS) {
			R14_QUAN_DIS = r14_QUAN_DIS;
		}

		public String getR15_QUALITATIVE_DISCLOSURES() {
			return R15_QUALITATIVE_DISCLOSURES;
		}

		public void setR15_QUALITATIVE_DISCLOSURES(String r15_QUALITATIVE_DISCLOSURES) {
			R15_QUALITATIVE_DISCLOSURES = r15_QUALITATIVE_DISCLOSURES;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_QUAN_DIS() {
			return R15_QUAN_DIS;
		}

		public void setR15_QUAN_DIS(BigDecimal r15_QUAN_DIS) {
			R15_QUAN_DIS = r15_QUAN_DIS;
		}

		public String getR16_QUALITATIVE_DISCLOSURES() {
			return R16_QUALITATIVE_DISCLOSURES;
		}

		public void setR16_QUALITATIVE_DISCLOSURES(String r16_QUALITATIVE_DISCLOSURES) {
			R16_QUALITATIVE_DISCLOSURES = r16_QUALITATIVE_DISCLOSURES;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_QUAN_DIS() {
			return R16_QUAN_DIS;
		}

		public void setR16_QUAN_DIS(BigDecimal r16_QUAN_DIS) {
			R16_QUAN_DIS = r16_QUAN_DIS;
		}

		public String getR20_QUALITATIVE_DISCLOSURES() {
			return R20_QUALITATIVE_DISCLOSURES;
		}

		public void setR20_QUALITATIVE_DISCLOSURES(String r20_QUALITATIVE_DISCLOSURES) {
			R20_QUALITATIVE_DISCLOSURES = r20_QUALITATIVE_DISCLOSURES;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_QUAN_DIS() {
			return R20_QUAN_DIS;
		}

		public void setR20_QUAN_DIS(BigDecimal r20_QUAN_DIS) {
			R20_QUAN_DIS = r20_QUAN_DIS;
		}

		public String getR21_QUALITATIVE_DISCLOSURES() {
			return R21_QUALITATIVE_DISCLOSURES;
		}

		public void setR21_QUALITATIVE_DISCLOSURES(String r21_QUALITATIVE_DISCLOSURES) {
			R21_QUALITATIVE_DISCLOSURES = r21_QUALITATIVE_DISCLOSURES;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_QUAN_DIS() {
			return R21_QUAN_DIS;
		}

		public void setR21_QUAN_DIS(BigDecimal r21_QUAN_DIS) {
			R21_QUAN_DIS = r21_QUAN_DIS;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}

// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class Market_Risk_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Market_Risk_PK() {
		}

		public Market_Risk_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Market_Risk_PK))
				return false;
			Market_Risk_PK that = (Market_Risk_PK) o;
			return Objects.equals(REPORT_DATE, that.REPORT_DATE) && Objects.equals(REPORT_VERSION, that.REPORT_VERSION);
		}

		@Override
		public int hashCode() {
			return Objects.hash(REPORT_DATE, REPORT_VERSION);
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
	}

	public class Market_Risk_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	class Market_RiskDetailRowMapper implements RowMapper<Market_Risk_Detail_Entity> {

		@Override
		public Market_Risk_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Market_Risk_Detail_Entity obj = new Market_Risk_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
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

	class Market_RiskArchivalDetailRowMapper implements RowMapper<Market_Risk_Archival_Detail_Entity> {

		@Override
		public Market_Risk_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Market_Risk_Archival_Detail_Entity obj = new Market_Risk_Archival_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setAverage(rs.getBigDecimal("AVERAGE"));
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

	public class Market_Risk_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;

		@Column(name = "ACCT_NAME")
		private String acctName;

		@Column(name = "DATA_TYPE")
		private String dataType;

		@Column(name = "REPORT_NAME")
		private String reportName;

		@Column(name = "REPORT_LABEL")
		private String reportLabel;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 3)
		private BigDecimal acctBalanceInpula;

		@Column(name = "AVERAGE", precision = 24, scale = 3)
		private BigDecimal average;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;

		@Column(name = "CREATE_USER")
		private String createUser;

		@Column(name = "CREATE_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date createTime;

		@Column(name = "MODIFY_USER")
		private String modifyUser;

		@Column(name = "MODIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date modifyTime;

		@Column(name = "VERIFY_USER")
		private String verifyUser;

		@Column(name = "VERIFY_TIME")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date verifyTime;

		@Column(name = "ENTITY_FLG")
		private char entityFlg;

		@Column(name = "MODIFY_FLG")
		private char modifyFlg;

		@Column(name = "DEL_FLG")
		private char delFlg;

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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
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

		public char getEntityFlg() {
			return entityFlg;
		}

		public void setEntityFlg(char entityFlg) {
			this.entityFlg = entityFlg;
		}

		public char getModifyFlg() {
			return modifyFlg;
		}

		public void setModifyFlg(char modifyFlg) {
			this.modifyFlg = modifyFlg;
		}

		public char getDelFlg() {
			return delFlg;
		}

		public void setDelFlg(char delFlg) {
			this.delFlg = delFlg;
		}

		public BigDecimal getAverage() {
			return average;
		}

		public void setAverage(BigDecimal average) {
			this.average = average;
		}
	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getMarket_RiskView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("Market_Risk View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<Market_Risk_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);
				// SUMMARY ARCHIVAL
				T1Master = getdatabydateListarchival(dt, version);
				System.out.println("Archival Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}
		// NORMAL MODE

		else {
			List<Market_Risk_Summary_Entity> T1Master = new ArrayList<>();
			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY NORMAL
				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// VIEW SETTINGS

		mv.setViewName("BRRS/Market_Risk");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getMarket_RiskcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		ModelAndView mv = new ModelAndView();

		try {

			Date parsedDate = null;

			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
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

			// ARCHIVAL MODE

			if ("ARCHIVAL".equals(type) && version != null) {

				System.out.println("ARCHIVAL DETAIL MODE");

				List<Market_Risk_Archival_Detail_Entity> archivalDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					archivalDetailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate,
							version);

				} else {

					archivalDetailList = getArchivalDetaildatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", archivalDetailList);
				mv.addObject("reportmaster12", archivalDetailList);

				System.out.println("ARCHIVAL DETAIL COUNT: " + archivalDetailList.size());

			}

			// CURRENT MODE

			else {

				List<Market_Risk_Detail_Entity> currentDetailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					currentDetailList = GetDetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

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

		mv.setViewName("BRRS/Market_Risk");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getMarket_RiskArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Market_Risk_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Market_Risk_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Market_Risk_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Market_Risk  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/Market_Risk");

		if (acct_number != null) {
			Market_Risk_Detail_Entity Market_RiskEntity = findByAcctnumber(acct_number);
			if (Market_RiskEntity != null && Market_RiskEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Market_RiskEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("MarketRiskData", Market_RiskEntity);
		}

		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String acctNo = request.getParameter("acctNumber");

			String acctBalanceInpulaStr = request.getParameter("acctBalanceInpula");

			String averageStr = request.getParameter("average");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			// Existing Record
			Market_Risk_Detail_Entity existing = findByAcctnumber(acctNo);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// ACCOUNT NAME
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);

					isChanged = true;
				}
			}

			// ACCOUNT BALANCE
			if (acctBalanceInpulaStr != null && !acctBalanceInpulaStr.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpulaStr);

				if (existing.getAcctBalanceInpula() == null
						|| existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInpula(newBalance);

					isChanged = true;
				}
			}

			// AVERAGE
			if (averageStr != null && !averageStr.isEmpty()) {

				BigDecimal newAverage = new BigDecimal(averageStr);

				if (existing.getAverage() == null || existing.getAverage().compareTo(newAverage) != 0) {

					existing.setAverage(newAverage);

					isChanged = true;
				}
			}

			// UPDATE
			if (isChanged) {

				String sql = "UPDATE BRRS_MARKET_RISK_DETAILTABLE " + "SET ACCT_NAME = ?, "
						+ "ACCT_BALANCE_IN_PULA = ?, " + "AVERAGE = ? " + "WHERE ACCT_NUMBER = ?";

				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						acctNo);

				System.out.println("Record updated successfully");

				// DATE FORMAT
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// PROCEDURE CALL
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {

						try {

							jdbcTemplate.update("BEGIN BRRS_Market_Risk_SUMMARY_PROCEDURE(?); END;", formattedDate);

							System.out.println("Procedure executed");

						} catch (Exception e) {

							e.printStackTrace();
						}
					}
				});

				return ResponseEntity.ok("Record updated successfully!");
			}

			else {

				return ResponseEntity.ok("No changes were made.");
			}

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	public byte[] getMarket_RiskDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  Market_Risk Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getMarket_RiskDetailNewExcelARCHIVAL(filename, fromdate, todate, currency,
						dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Market_RiskDetailsDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<Market_Risk_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Market_Risk_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for Market_Risk — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Market_Risk Excel", e);
			return new byte[0];
		}
	}

	public byte[] getMarket_RiskDetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Market_Risk ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Market_Risk Detail NEW");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "AVERAGE", "REPORT LABEL",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<Market_Risk_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Market_Risk_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInpula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInpula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					// AVERAGE (right aligned, 3 decimal places)
					Cell balanceCell1 = row.createCell(4);
					if (item.getAverage() != null) {
						balanceCell1.setCellValue(item.getAverage().doubleValue());
					} else {
						balanceCell1.setCellValue(0);
					}
					balanceCell1.setCellStyle(balanceStyle);

					row.createCell(5).setCellValue(item.getReportLabel());
					row.createCell(6).setCellValue(item.getReportAddlCriteria1());
					row.createCell(7)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for Market_Risk — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Market_Risk NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getMarket_RiskExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.Market_Risk");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMarket_RiskARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<Market_Risk_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  Market_Risk report. Returning empty result.");
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

			int startRow = 4;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Market_Risk_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R5
					row = sheet.getRow(4);
					Cell cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR5_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR5_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR6_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR6_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR11_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR11_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR12_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR12_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR13_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR13_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR14_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR14_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR15_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR15_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR16_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR16_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR20_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR20_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR21_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR21_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "BRRS_MARKET_RISK SUMMARY", null, "BRRS_MARKET_RISK_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	public byte[] getExcelMarket_RiskARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Market_Risk_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Market_Risk new report. Returning empty result.");
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

			int startRow = 4;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Market_Risk_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R5
					row = sheet.getRow(4);
					Cell cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR5_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR5_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR6_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR6_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR11_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR11_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR12_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR12_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR13_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR13_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR14_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR14_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR15_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR15_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR16_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR16_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR20_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR20_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR21_QUAN_DIS() != null) {
						cellC.setCellValue(record.getR21_QUAN_DIS().doubleValue());
					} else {
						cellC.setCellValue(0);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "BRRS_MARKET_RISK EMAIL SUMMARY", null, "BRRS_BRRS_MARKET_RISK_SUMMARYTABLE");
			}
			
			return out.toByteArray();
		}

	}

}