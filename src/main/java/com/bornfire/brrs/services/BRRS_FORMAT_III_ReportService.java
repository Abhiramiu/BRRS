package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Row;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.hibernate.SessionFactory;
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
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_FORMAT_III_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_FORMAT_III_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<FORMAT_III_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new FORMAT_IIIRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getFORMAT_IIIArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_FORMAT_III_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<FORMAT_III_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new FORMAT_IIIArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<FORMAT_III_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_FORMAT_III_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new FORMAT_IIIArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_FORMAT_III_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<FORMAT_III_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new FORMAT_IIIDetaillRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<FORMAT_III_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new FORMAT_IIIDetaillRowMapper());
	}

// 3. PAGINATION

	public List<FORMAT_III_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new FORMAT_IIIDetaillRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_FORMAT_III_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<FORMAT_III_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new FORMAT_IIIDetaillRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public FORMAT_III_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new FORMAT_IIIDetaillRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<FORMAT_III_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new FORMAT_IIIArchivalDetaillRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<FORMAT_III_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_FORMAT_III_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new FORMAT_IIIArchivalDetaillRowMapper());
	}

	// ROW MAPPER

	class FORMAT_IIIRowMapper implements RowMapper<FORMAT_III_Summary_Entity> {

		@Override
		public FORMAT_III_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_III_Summary_Entity obj = new FORMAT_III_Summary_Entity();

			obj.setR13_brief_bank(rs.getString("R13_BRIEF_BANK"));
			obj.setR13_brief_subdiary(rs.getString("R13_BRIEF_SUBDIARY"));
			obj.setR13_eff_name(rs.getString("R13_EFF_NAME"));
			obj.setR13_eff_increase(rs.getBigDecimal("R13_EFF_INCREASE"));
			obj.setR13_eff_decrease(rs.getBigDecimal("R13_EFF_DECREASE"));
			obj.setR13_bal_name(rs.getString("R13_BAL_NAME"));
			obj.setR13_bal_increase(rs.getBigDecimal("R13_BAL_INCREASE"));
			obj.setR13_bal_decrease(rs.getBigDecimal("R13_BAL_DECREASE"));

			obj.setR14_brief_bank(rs.getString("R14_BRIEF_BANK"));
			obj.setR14_brief_subdiary(rs.getString("R14_BRIEF_SUBDIARY"));
			obj.setR14_eff_name(rs.getString("R14_EFF_NAME"));
			obj.setR14_eff_increase(rs.getBigDecimal("R14_EFF_INCREASE"));
			obj.setR14_eff_decrease(rs.getBigDecimal("R14_EFF_DECREASE"));
			obj.setR14_bal_name(rs.getString("R14_BAL_NAME"));
			obj.setR14_bal_increase(rs.getBigDecimal("R14_BAL_INCREASE"));
			obj.setR14_bal_decrease(rs.getBigDecimal("R14_BAL_DECREASE"));

			obj.setR15_brief_bank(rs.getString("R15_BRIEF_BANK"));
			obj.setR15_brief_subdiary(rs.getString("R15_BRIEF_SUBDIARY"));
			obj.setR15_eff_name(rs.getString("R15_EFF_NAME"));
			obj.setR15_eff_increase(rs.getBigDecimal("R15_EFF_INCREASE"));
			obj.setR15_eff_decrease(rs.getBigDecimal("R15_EFF_DECREASE"));
			obj.setR15_bal_name(rs.getString("R15_BAL_NAME"));
			obj.setR15_bal_increase(rs.getBigDecimal("R15_BAL_INCREASE"));
			obj.setR15_bal_decrease(rs.getBigDecimal("R15_BAL_DECREASE"));

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

	public static class FORMAT_III_Summary_Entity {

		@Column(name = "R13_BRIEF_BANK")
		private String r13_brief_bank;

		@Column(name = "R13_BRIEF_SUBDIARY")
		private String r13_brief_subdiary;

		@Column(name = "R13_EFF_NAME")
		private String r13_eff_name;

		@Column(name = "R13_EFF_INCREASE")
		private BigDecimal r13_eff_increase;

		@Column(name = "R13_EFF_DECREASE")
		private BigDecimal r13_eff_decrease;

		@Column(name = "R13_BAL_NAME")
		private String r13_bal_name;

		@Column(name = "R13_BAL_INCREASE")
		private BigDecimal r13_bal_increase;

		@Column(name = "R13_BAL_DECREASE")
		private BigDecimal r13_bal_decrease;

		@Column(name = "R14_BRIEF_BANK")
		private String r14_brief_bank;

		@Column(name = "R14_BRIEF_SUBDIARY")
		private String r14_brief_subdiary;

		@Column(name = "R14_EFF_NAME")
		private String r14_eff_name;

		@Column(name = "R14_EFF_INCREASE")
		private BigDecimal r14_eff_increase;

		@Column(name = "R14_EFF_DECREASE")
		private BigDecimal r14_eff_decrease;

		@Column(name = "R14_BAL_NAME")
		private String r14_bal_name;

		@Column(name = "R14_BAL_INCREASE")
		private BigDecimal r14_bal_increase;

		@Column(name = "R14_BAL_DECREASE")
		private BigDecimal r14_bal_decrease;

		@Column(name = "R15_BRIEF_BANK")
		private String r15_brief_bank;

		@Column(name = "R15_BRIEF_SUBDIARY")
		private String r15_brief_subdiary;

		@Column(name = "R15_EFF_NAME")
		private String r15_eff_name;

		@Column(name = "R15_EFF_INCREASE")
		private BigDecimal r15_eff_increase;

		@Column(name = "R15_EFF_DECREASE")
		private BigDecimal r15_eff_decrease;

		@Column(name = "R15_BAL_NAME")
		private String r15_bal_name;

		@Column(name = "R15_BAL_INCREASE")
		private BigDecimal r15_bal_increase;

		@Column(name = "R15_BAL_DECREASE")
		private BigDecimal r15_bal_decrease;

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

		public String getR13_brief_bank() {
			return r13_brief_bank;
		}

		public void setR13_brief_bank(String r13_brief_bank) {
			this.r13_brief_bank = r13_brief_bank;
		}

		public String getR13_brief_subdiary() {
			return r13_brief_subdiary;
		}

		public void setR13_brief_subdiary(String r13_brief_subdiary) {
			this.r13_brief_subdiary = r13_brief_subdiary;
		}

		public String getR13_eff_name() {
			return r13_eff_name;
		}

		public void setR13_eff_name(String r13_eff_name) {
			this.r13_eff_name = r13_eff_name;
		}

		public BigDecimal getR13_eff_increase() {
			return r13_eff_increase;
		}

		public void setR13_eff_increase(BigDecimal r13_eff_increase) {
			this.r13_eff_increase = r13_eff_increase;
		}

		public BigDecimal getR13_eff_decrease() {
			return r13_eff_decrease;
		}

		public void setR13_eff_decrease(BigDecimal r13_eff_decrease) {
			this.r13_eff_decrease = r13_eff_decrease;
		}

		public String getR13_bal_name() {
			return r13_bal_name;
		}

		public void setR13_bal_name(String r13_bal_name) {
			this.r13_bal_name = r13_bal_name;
		}

		public BigDecimal getR13_bal_increase() {
			return r13_bal_increase;
		}

		public void setR13_bal_increase(BigDecimal r13_bal_increase) {
			this.r13_bal_increase = r13_bal_increase;
		}

		public BigDecimal getR13_bal_decrease() {
			return r13_bal_decrease;
		}

		public void setR13_bal_decrease(BigDecimal r13_bal_decrease) {
			this.r13_bal_decrease = r13_bal_decrease;
		}

		public String getR14_brief_bank() {
			return r14_brief_bank;
		}

		public void setR14_brief_bank(String r14_brief_bank) {
			this.r14_brief_bank = r14_brief_bank;
		}

		public String getR14_brief_subdiary() {
			return r14_brief_subdiary;
		}

		public void setR14_brief_subdiary(String r14_brief_subdiary) {
			this.r14_brief_subdiary = r14_brief_subdiary;
		}

		public String getR14_eff_name() {
			return r14_eff_name;
		}

		public void setR14_eff_name(String r14_eff_name) {
			this.r14_eff_name = r14_eff_name;
		}

		public BigDecimal getR14_eff_increase() {
			return r14_eff_increase;
		}

		public void setR14_eff_increase(BigDecimal r14_eff_increase) {
			this.r14_eff_increase = r14_eff_increase;
		}

		public BigDecimal getR14_eff_decrease() {
			return r14_eff_decrease;
		}

		public void setR14_eff_decrease(BigDecimal r14_eff_decrease) {
			this.r14_eff_decrease = r14_eff_decrease;
		}

		public String getR14_bal_name() {
			return r14_bal_name;
		}

		public void setR14_bal_name(String r14_bal_name) {
			this.r14_bal_name = r14_bal_name;
		}

		public BigDecimal getR14_bal_increase() {
			return r14_bal_increase;
		}

		public void setR14_bal_increase(BigDecimal r14_bal_increase) {
			this.r14_bal_increase = r14_bal_increase;
		}

		public BigDecimal getR14_bal_decrease() {
			return r14_bal_decrease;
		}

		public void setR14_bal_decrease(BigDecimal r14_bal_decrease) {
			this.r14_bal_decrease = r14_bal_decrease;
		}

		public String getR15_brief_bank() {
			return r15_brief_bank;
		}

		public void setR15_brief_bank(String r15_brief_bank) {
			this.r15_brief_bank = r15_brief_bank;
		}

		public String getR15_brief_subdiary() {
			return r15_brief_subdiary;
		}

		public void setR15_brief_subdiary(String r15_brief_subdiary) {
			this.r15_brief_subdiary = r15_brief_subdiary;
		}

		public String getR15_eff_name() {
			return r15_eff_name;
		}

		public void setR15_eff_name(String r15_eff_name) {
			this.r15_eff_name = r15_eff_name;
		}

		public BigDecimal getR15_eff_increase() {
			return r15_eff_increase;
		}

		public void setR15_eff_increase(BigDecimal r15_eff_increase) {
			this.r15_eff_increase = r15_eff_increase;
		}

		public BigDecimal getR15_eff_decrease() {
			return r15_eff_decrease;
		}

		public void setR15_eff_decrease(BigDecimal r15_eff_decrease) {
			this.r15_eff_decrease = r15_eff_decrease;
		}

		public String getR15_bal_name() {
			return r15_bal_name;
		}

		public void setR15_bal_name(String r15_bal_name) {
			this.r15_bal_name = r15_bal_name;
		}

		public BigDecimal getR15_bal_increase() {
			return r15_bal_increase;
		}

		public void setR15_bal_increase(BigDecimal r15_bal_increase) {
			this.r15_bal_increase = r15_bal_increase;
		}

		public BigDecimal getR15_bal_decrease() {
			return r15_bal_decrease;
		}

		public void setR15_bal_decrease(BigDecimal r15_bal_decrease) {
			this.r15_bal_decrease = r15_bal_decrease;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			REPORT_DATE = REPORT_DATE;
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

	class FORMAT_IIIArchivalRowMapper implements RowMapper<FORMAT_III_Archival_Summary_Entity> {

		@Override
		public FORMAT_III_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_III_Archival_Summary_Entity obj = new FORMAT_III_Archival_Summary_Entity();

			obj.setR13_brief_bank(rs.getString("R13_BRIEF_BANK"));
			obj.setR13_brief_subdiary(rs.getString("R13_BRIEF_SUBDIARY"));
			obj.setR13_eff_name(rs.getString("R13_EFF_NAME"));
			obj.setR13_eff_increase(rs.getBigDecimal("R13_EFF_INCREASE"));
			obj.setR13_eff_decrease(rs.getBigDecimal("R13_EFF_DECREASE"));
			obj.setR13_bal_name(rs.getString("R13_BAL_NAME"));
			obj.setR13_bal_increase(rs.getBigDecimal("R13_BAL_INCREASE"));
			obj.setR13_bal_decrease(rs.getBigDecimal("R13_BAL_DECREASE"));

			obj.setR14_brief_bank(rs.getString("R14_BRIEF_BANK"));
			obj.setR14_brief_subdiary(rs.getString("R14_BRIEF_SUBDIARY"));
			obj.setR14_eff_name(rs.getString("R14_EFF_NAME"));
			obj.setR14_eff_increase(rs.getBigDecimal("R14_EFF_INCREASE"));
			obj.setR14_eff_decrease(rs.getBigDecimal("R14_EFF_DECREASE"));
			obj.setR14_bal_name(rs.getString("R14_BAL_NAME"));
			obj.setR14_bal_increase(rs.getBigDecimal("R14_BAL_INCREASE"));
			obj.setR14_bal_decrease(rs.getBigDecimal("R14_BAL_DECREASE"));

			obj.setR15_brief_bank(rs.getString("R15_BRIEF_BANK"));
			obj.setR15_brief_subdiary(rs.getString("R15_BRIEF_SUBDIARY"));
			obj.setR15_eff_name(rs.getString("R15_EFF_NAME"));
			obj.setR15_eff_increase(rs.getBigDecimal("R15_EFF_INCREASE"));
			obj.setR15_eff_decrease(rs.getBigDecimal("R15_EFF_DECREASE"));
			obj.setR15_bal_name(rs.getString("R15_BAL_NAME"));
			obj.setR15_bal_increase(rs.getBigDecimal("R15_BAL_INCREASE"));
			obj.setR15_bal_decrease(rs.getBigDecimal("R15_BAL_DECREASE"));

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

	@IdClass(FORMAT_III_PK.class)
	public class FORMAT_III_Archival_Summary_Entity {

		@Column(name = "R13_BRIEF_BANK")
		private String r13_brief_bank;

		@Column(name = "R13_BRIEF_SUBDIARY")
		private String r13_brief_subdiary;

		@Column(name = "R13_EFF_NAME")
		private String r13_eff_name;

		@Column(name = "R13_EFF_INCREASE")
		private BigDecimal r13_eff_increase;

		@Column(name = "R13_EFF_DECREASE")
		private BigDecimal r13_eff_decrease;

		@Column(name = "R13_BAL_NAME")
		private String r13_bal_name;

		@Column(name = "R13_BAL_INCREASE")
		private BigDecimal r13_bal_increase;

		@Column(name = "R13_BAL_DECREASE")
		private BigDecimal r13_bal_decrease;

		@Column(name = "R14_BRIEF_BANK")
		private String r14_brief_bank;

		@Column(name = "R14_BRIEF_SUBDIARY")
		private String r14_brief_subdiary;

		@Column(name = "R14_EFF_NAME")
		private String r14_eff_name;

		@Column(name = "R14_EFF_INCREASE")
		private BigDecimal r14_eff_increase;

		@Column(name = "R14_EFF_DECREASE")
		private BigDecimal r14_eff_decrease;

		@Column(name = "R14_BAL_NAME")
		private String r14_bal_name;

		@Column(name = "R14_BAL_INCREASE")
		private BigDecimal r14_bal_increase;

		@Column(name = "R14_BAL_DECREASE")
		private BigDecimal r14_bal_decrease;

		@Column(name = "R15_BRIEF_BANK")
		private String r15_brief_bank;

		@Column(name = "R15_BRIEF_SUBDIARY")
		private String r15_brief_subdiary;

		@Column(name = "R15_EFF_NAME")
		private String r15_eff_name;

		@Column(name = "R15_EFF_INCREASE")
		private BigDecimal r15_eff_increase;

		@Column(name = "R15_EFF_DECREASE")
		private BigDecimal r15_eff_decrease;

		@Column(name = "R15_BAL_NAME")
		private String r15_bal_name;

		@Column(name = "R15_BAL_INCREASE")
		private BigDecimal r15_bal_increase;

		@Column(name = "R15_BAL_DECREASE")
		private BigDecimal r15_bal_decrease;

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

		private Date REPORT_RESUBDATE;

		public String getR13_brief_bank() {
			return r13_brief_bank;
		}

		public void setR13_brief_bank(String r13_brief_bank) {
			this.r13_brief_bank = r13_brief_bank;
		}

		public String getR13_brief_subdiary() {
			return r13_brief_subdiary;
		}

		public void setR13_brief_subdiary(String r13_brief_subdiary) {
			this.r13_brief_subdiary = r13_brief_subdiary;
		}

		public String getR13_eff_name() {
			return r13_eff_name;
		}

		public void setR13_eff_name(String r13_eff_name) {
			this.r13_eff_name = r13_eff_name;
		}

		public BigDecimal getR13_eff_increase() {
			return r13_eff_increase;
		}

		public void setR13_eff_increase(BigDecimal r13_eff_increase) {
			this.r13_eff_increase = r13_eff_increase;
		}

		public BigDecimal getR13_eff_decrease() {
			return r13_eff_decrease;
		}

		public void setR13_eff_decrease(BigDecimal r13_eff_decrease) {
			this.r13_eff_decrease = r13_eff_decrease;
		}

		public String getR13_bal_name() {
			return r13_bal_name;
		}

		public void setR13_bal_name(String r13_bal_name) {
			this.r13_bal_name = r13_bal_name;
		}

		public BigDecimal getR13_bal_increase() {
			return r13_bal_increase;
		}

		public void setR13_bal_increase(BigDecimal r13_bal_increase) {
			this.r13_bal_increase = r13_bal_increase;
		}

		public BigDecimal getR13_bal_decrease() {
			return r13_bal_decrease;
		}

		public void setR13_bal_decrease(BigDecimal r13_bal_decrease) {
			this.r13_bal_decrease = r13_bal_decrease;
		}

		public String getR14_brief_bank() {
			return r14_brief_bank;
		}

		public void setR14_brief_bank(String r14_brief_bank) {
			this.r14_brief_bank = r14_brief_bank;
		}

		public String getR14_brief_subdiary() {
			return r14_brief_subdiary;
		}

		public void setR14_brief_subdiary(String r14_brief_subdiary) {
			this.r14_brief_subdiary = r14_brief_subdiary;
		}

		public String getR14_eff_name() {
			return r14_eff_name;
		}

		public void setR14_eff_name(String r14_eff_name) {
			this.r14_eff_name = r14_eff_name;
		}

		public BigDecimal getR14_eff_increase() {
			return r14_eff_increase;
		}

		public void setR14_eff_increase(BigDecimal r14_eff_increase) {
			this.r14_eff_increase = r14_eff_increase;
		}

		public BigDecimal getR14_eff_decrease() {
			return r14_eff_decrease;
		}

		public void setR14_eff_decrease(BigDecimal r14_eff_decrease) {
			this.r14_eff_decrease = r14_eff_decrease;
		}

		public String getR14_bal_name() {
			return r14_bal_name;
		}

		public void setR14_bal_name(String r14_bal_name) {
			this.r14_bal_name = r14_bal_name;
		}

		public BigDecimal getR14_bal_increase() {
			return r14_bal_increase;
		}

		public void setR14_bal_increase(BigDecimal r14_bal_increase) {
			this.r14_bal_increase = r14_bal_increase;
		}

		public BigDecimal getR14_bal_decrease() {
			return r14_bal_decrease;
		}

		public void setR14_bal_decrease(BigDecimal r14_bal_decrease) {
			this.r14_bal_decrease = r14_bal_decrease;
		}

		public String getR15_brief_bank() {
			return r15_brief_bank;
		}

		public void setR15_brief_bank(String r15_brief_bank) {
			this.r15_brief_bank = r15_brief_bank;
		}

		public String getR15_brief_subdiary() {
			return r15_brief_subdiary;
		}

		public void setR15_brief_subdiary(String r15_brief_subdiary) {
			this.r15_brief_subdiary = r15_brief_subdiary;
		}

		public String getR15_eff_name() {
			return r15_eff_name;
		}

		public void setR15_eff_name(String r15_eff_name) {
			this.r15_eff_name = r15_eff_name;
		}

		public BigDecimal getR15_eff_increase() {
			return r15_eff_increase;
		}

		public void setR15_eff_increase(BigDecimal r15_eff_increase) {
			this.r15_eff_increase = r15_eff_increase;
		}

		public BigDecimal getR15_eff_decrease() {
			return r15_eff_decrease;
		}

		public void setR15_eff_decrease(BigDecimal r15_eff_decrease) {
			this.r15_eff_decrease = r15_eff_decrease;
		}

		public String getR15_bal_name() {
			return r15_bal_name;
		}

		public void setR15_bal_name(String r15_bal_name) {
			this.r15_bal_name = r15_bal_name;
		}

		public BigDecimal getR15_bal_increase() {
			return r15_bal_increase;
		}

		public void setR15_bal_increase(BigDecimal r15_bal_increase) {
			this.r15_bal_increase = r15_bal_increase;
		}

		public BigDecimal getR15_bal_decrease() {
			return r15_bal_decrease;
		}

		public void setR15_bal_decrease(BigDecimal r15_bal_decrease) {
			this.r15_bal_decrease = r15_bal_decrease;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			REPORT_DATE = REPORT_DATE;
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}
	}

// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class FORMAT_III_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public FORMAT_III_PK() {
		}

		public FORMAT_III_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof FORMAT_III_PK))
				return false;
			FORMAT_III_PK that = (FORMAT_III_PK) o;
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

	public class FORMAT_III_Detail_Entity {

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

	class FORMAT_IIIDetaillRowMapper implements RowMapper<FORMAT_III_Detail_Entity> {

		@Override
		public FORMAT_III_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_III_Detail_Entity obj = new FORMAT_III_Detail_Entity();

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

	class FORMAT_IIIArchivalDetaillRowMapper implements RowMapper<FORMAT_III_Archival_Detail_Entity> {

		@Override
		public FORMAT_III_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			FORMAT_III_Archival_Detail_Entity obj = new FORMAT_III_Archival_Detail_Entity();

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

	public class FORMAT_III_Archival_Detail_Entity {

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

	public ModelAndView getFORMAT_IIIView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("FORMAT_III View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<FORMAT_III_Archival_Summary_Entity> T1Master = new ArrayList<>();

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
			List<FORMAT_III_Summary_Entity> T1Master = new ArrayList<>();
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

		mv.setViewName("BRRS/FORMAT_III");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getFORMAT_IIIcurrentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<FORMAT_III_Archival_Detail_Entity> archivalDetailList;

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

				List<FORMAT_III_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/FORMAT_III");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getFORMAT_IIIArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<FORMAT_III_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (FORMAT_III_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				FORMAT_III_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  FORMAT_III  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/FORMAT_III");

		if (acct_number != null) {
			FORMAT_III_Detail_Entity FORMAT_IIIEntity = findByAcctnumber(acct_number);
			if (FORMAT_IIIEntity != null && FORMAT_IIIEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(FORMAT_IIIEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("FORMAT_IIIData", FORMAT_IIIEntity);
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
			FORMAT_III_Detail_Entity existing = findByAcctnumber(acctNo);

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

				String sql = "UPDATE BRRS_FORMAT_III_DETAILTABLE " + "SET ACCT_NAME = ?, "
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

							jdbcTemplate.update("BEGIN BRRS_FORMAT_III_SUMMARY_PROCEDURE(?); END;", formattedDate);

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

	public byte[] getFORMAT_IIIDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  FORMAT_III Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getFORMAT_IIIDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_IIIDetailsDetail");

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
			List<FORMAT_III_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_III_Detail_Entity item : reportData) {
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
				logger.info("No data found for FORMAT_III — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_III Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFORMAT_IIIDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for FORMAT_III ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("FORMAT_III Detail NEW");

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
			List<FORMAT_III_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (FORMAT_III_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for FORMAT_III — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating FORMAT_III NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getFORMAT_IIIExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.sch17");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelFORMAT_IIIARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<FORMAT_III_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  FORMAT_III report. Returning empty result.");
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
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FORMAT_III_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R13Cell1 = row.createCell(4);
					if (record.getR13_eff_increase() != null) {
						R13Cell1.setCellValue(record.getR13_eff_increase().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell2 = row.createCell(5);
					if (record.getR13_eff_decrease() != null) {
						R13Cell2.setCellValue(record.getR13_eff_decrease().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13Cell3 = row.createCell(7);
					if (record.getR13_bal_increase() != null) {
						R13Cell3.setCellValue(record.getR13_bal_increase().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}
					// R13 Col G
					Cell R13Cell4 = row.createCell(8);
					if (record.getR13_bal_decrease() != null) {
						R13Cell4.setCellValue(record.getR13_bal_decrease().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(4);
					if (record.getR14_eff_increase() != null) {
						R14Cell1.setCellValue(record.getR14_eff_increase().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell2 = row.createCell(5);
					if (record.getR14_eff_decrease() != null) {
						R14Cell2.setCellValue(record.getR14_eff_decrease().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14Cell3 = row.createCell(7);
					if (record.getR14_bal_increase() != null) {
						R14Cell3.setCellValue(record.getR14_bal_increase().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}
					// R14 Col G
					Cell R14Cell4 = row.createCell(8);
					if (record.getR14_bal_decrease() != null) {
						R14Cell4.setCellValue(record.getR14_bal_decrease().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(4);
					if (record.getR15_eff_increase() != null) {
						R15Cell1.setCellValue(record.getR15_eff_increase().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					// R15 Col E
					Cell R15Cell2 = row.createCell(5);
					if (record.getR15_eff_decrease() != null) {
						R15Cell2.setCellValue(record.getR15_eff_decrease().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15Cell3 = row.createCell(7);
					if (record.getR15_bal_increase() != null) {
						R15Cell3.setCellValue(record.getR15_bal_increase().doubleValue());
						R15Cell3.setCellStyle(numberStyle);
					} else {
						R15Cell3.setCellValue("");
						R15Cell3.setCellStyle(textStyle);
					}
					// R15 Col G
					Cell R15Cell4 = row.createCell(8);
					if (record.getR15_bal_decrease() != null) {
						R15Cell4.setCellValue(record.getR15_bal_decrease().doubleValue());
						R15Cell4.setCellStyle(numberStyle);
					} else {
						R15Cell4.setCellValue("");
						R15Cell4.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}

			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

	public byte[] getExcelFORMAT_IIIARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<FORMAT_III_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for FORMAT_III new report. Returning empty result.");
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

			// --- End of Style Definitions ---
			int startRow = 12;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					FORMAT_III_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R13Cell1 = row.createCell(4);
					if (record.getR13_eff_increase() != null) {
						R13Cell1.setCellValue(record.getR13_eff_increase().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					// R13 Col E
					Cell R13Cell2 = row.createCell(5);
					if (record.getR13_eff_decrease() != null) {
						R13Cell2.setCellValue(record.getR13_eff_decrease().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

					// R13 Col F
					Cell R13Cell3 = row.createCell(7);
					if (record.getR13_bal_increase() != null) {
						R13Cell3.setCellValue(record.getR13_bal_increase().doubleValue());
						R13Cell3.setCellStyle(numberStyle);
					} else {
						R13Cell3.setCellValue("");
						R13Cell3.setCellStyle(textStyle);
					}
					// R13 Col G
					Cell R13Cell4 = row.createCell(8);
					if (record.getR13_bal_decrease() != null) {
						R13Cell4.setCellValue(record.getR13_bal_decrease().doubleValue());
						R13Cell4.setCellStyle(numberStyle);
					} else {
						R13Cell4.setCellValue("");
						R13Cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(4);
					if (record.getR14_eff_increase() != null) {
						R14Cell1.setCellValue(record.getR14_eff_increase().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					// R14 Col E
					Cell R14Cell2 = row.createCell(5);
					if (record.getR14_eff_decrease() != null) {
						R14Cell2.setCellValue(record.getR14_eff_decrease().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

					// R14 Col F
					Cell R14Cell3 = row.createCell(7);
					if (record.getR14_bal_increase() != null) {
						R14Cell3.setCellValue(record.getR14_bal_increase().doubleValue());
						R14Cell3.setCellStyle(numberStyle);
					} else {
						R14Cell3.setCellValue("");
						R14Cell3.setCellStyle(textStyle);
					}
					// R14 Col G
					Cell R14Cell4 = row.createCell(8);
					if (record.getR14_bal_decrease() != null) {
						R14Cell4.setCellValue(record.getR14_bal_decrease().doubleValue());
						R14Cell4.setCellStyle(numberStyle);
					} else {
						R14Cell4.setCellValue("");
						R14Cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(4);
					if (record.getR15_eff_increase() != null) {
						R15Cell1.setCellValue(record.getR15_eff_increase().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					// R15 Col E
					Cell R15Cell2 = row.createCell(5);
					if (record.getR15_eff_decrease() != null) {
						R15Cell2.setCellValue(record.getR15_eff_decrease().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

					// R15 Col F
					Cell R15Cell3 = row.createCell(7);
					if (record.getR15_bal_increase() != null) {
						R15Cell3.setCellValue(record.getR15_bal_increase().doubleValue());
						R15Cell3.setCellStyle(numberStyle);
					} else {
						R15Cell3.setCellValue("");
						R15Cell3.setCellStyle(textStyle);
					}
					// R15 Col G
					Cell R15Cell4 = row.createCell(8);
					if (record.getR15_bal_decrease() != null) {
						R15Cell4.setCellValue(record.getR15_bal_decrease().doubleValue());
						R15Cell4.setCellStyle(numberStyle);
					} else {
						R15Cell4.setCellValue("");
						R15Cell4.setCellStyle(textStyle);
					}
				}
				workbook.setForceFormulaRecalculation(true);
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			return out.toByteArray();
		}

	}

}