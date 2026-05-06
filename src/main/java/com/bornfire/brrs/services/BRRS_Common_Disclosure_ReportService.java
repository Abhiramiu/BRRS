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
public class BRRS_Common_Disclosure_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Common_Disclosure_ReportService.class);

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
	public List<Common_Disclosure_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new CommonDisclosureRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getCommon_DisclosureArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Common_Disclosure_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new CommonDisclosureArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<Common_Disclosure_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new CommonDisclosureArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Common_Disclosure_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new CommonDisclosureDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Common_Disclosure_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new CommonDisclosureDetailRowMapper());
	}

// 3. PAGINATION

	public List<Common_Disclosure_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new CommonDisclosureDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Common_Disclosure_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new CommonDisclosureDetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public Common_Disclosure_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new CommonDisclosureDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<Common_Disclosure_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new CommonDisclosureArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<Common_Disclosure_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_COMMON_DISCLOSURE_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new CommonDisclosureArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class CommonDisclosureRowMapper implements RowMapper<Common_Disclosure_Summary_Entity> {

		@Override
		public Common_Disclosure_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Common_Disclosure_Summary_Entity obj = new Common_Disclosure_Summary_Entity();

			// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_COMPONENT_OF_REGU(rs.getBigDecimal("R7_COMPONENT_OF_REGU"));
			obj.setR7_SOURCE_REF(rs.getString("R7_SOURCE_REF"));

			// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_COMPONENT_OF_REGU(rs.getBigDecimal("R8_COMPONENT_OF_REGU"));
			obj.setR8_SOURCE_REF(rs.getString("R8_SOURCE_REF"));

			// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_COMPONENT_OF_REGU(rs.getBigDecimal("R9_COMPONENT_OF_REGU"));
			obj.setR9_SOURCE_REF(rs.getString("R9_SOURCE_REF"));

			// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_COMPONENT_OF_REGU(rs.getBigDecimal("R10_COMPONENT_OF_REGU"));
			obj.setR10_SOURCE_REF(rs.getString("R10_SOURCE_REF"));

			// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_COMPONENT_OF_REGU(rs.getBigDecimal("R11_COMPONENT_OF_REGU"));
			obj.setR11_SOURCE_REF(rs.getString("R11_SOURCE_REF"));

			// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_COMPONENT_OF_REGU(rs.getBigDecimal("R12_COMPONENT_OF_REGU"));
			obj.setR12_SOURCE_REF(rs.getString("R12_SOURCE_REF"));

			// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_COMPONENT_OF_REGU(rs.getBigDecimal("R13_COMPONENT_OF_REGU"));
			obj.setR13_SOURCE_REF(rs.getString("R13_SOURCE_REF"));

			// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_COMPONENT_OF_REGU(rs.getBigDecimal("R14_COMPONENT_OF_REGU"));
			obj.setR14_SOURCE_REF(rs.getString("R14_SOURCE_REF"));

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

	public static class Common_Disclosure_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R7_PRODUCT", length = 100)
		private String R7_PRODUCT;

		@Column(name = "R7_COMPONENT_OF_REGU")
		private BigDecimal R7_COMPONENT_OF_REGU;

		@Column(name = "R7_SOURCE_REF", length = 200)
		private String R7_SOURCE_REF;

		@Column(name = "R8_PRODUCT", length = 100)
		private String R8_PRODUCT;

		@Column(name = "R8_COMPONENT_OF_REGU")
		private BigDecimal R8_COMPONENT_OF_REGU;

		@Column(name = "R8_SOURCE_REF", length = 100)
		private String R8_SOURCE_REF;

		@Column(name = "R9_PRODUCT", length = 100)
		private String R9_PRODUCT;

		@Column(name = "R9_COMPONENT_OF_REGU")
		private BigDecimal R9_COMPONENT_OF_REGU;

		@Column(name = "R9_SOURCE_REF", length = 100)
		private String R9_SOURCE_REF;

		@Column(name = "R10_PRODUCT", length = 200)
		private String R10_PRODUCT;

		@Column(name = "R10_COMPONENT_OF_REGU")
		private BigDecimal R10_COMPONENT_OF_REGU;

		@Column(name = "R10_SOURCE_REF", length = 100)
		private String R10_SOURCE_REF;

		@Column(name = "R11_PRODUCT", length = 200)
		private String R11_PRODUCT;

		@Column(name = "R11_COMPONENT_OF_REGU")
		private BigDecimal R11_COMPONENT_OF_REGU;

		@Column(name = "R11_SOURCE_REF", length = 100)
		private String R11_SOURCE_REF;

		@Column(name = "R12_PRODUCT", length = 100)
		private String R12_PRODUCT;

		@Column(name = "R12_COMPONENT_OF_REGU")
		private BigDecimal R12_COMPONENT_OF_REGU;

		@Column(name = "R12_SOURCE_REF", length = 100)
		private String R12_SOURCE_REF;

		@Column(name = "R13_PRODUCT", length = 100)
		private String R13_PRODUCT;

		@Column(name = "R13_COMPONENT_OF_REGU")
		private BigDecimal R13_COMPONENT_OF_REGU;

		@Column(name = "R13_SOURCE_REF", length = 100)
		private String R13_SOURCE_REF;

		@Column(name = "R14_PRODUCT", length = 100)
		private String R14_PRODUCT;

		@Column(name = "R14_COMPONENT_OF_REGU")
		private BigDecimal R14_COMPONENT_OF_REGU;

		@Column(name = "R14_SOURCE_REF", length = 100)
		private String R14_SOURCE_REF;

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

		public String getR7_PRODUCT() {
			return R7_PRODUCT;
		}

		public void setR7_PRODUCT(String r7_PRODUCT) {
			R7_PRODUCT = r7_PRODUCT;
		}

		public BigDecimal getR7_COMPONENT_OF_REGU() {
			return R7_COMPONENT_OF_REGU;
		}

		public void setR7_COMPONENT_OF_REGU(BigDecimal r7_COMPONENT_OF_REGU) {
			R7_COMPONENT_OF_REGU = r7_COMPONENT_OF_REGU;
		}

		public String getR7_SOURCE_REF() {
			return R7_SOURCE_REF;
		}

		public void setR7_SOURCE_REF(String r7_SOURCE_REF) {
			R7_SOURCE_REF = r7_SOURCE_REF;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_COMPONENT_OF_REGU() {
			return R8_COMPONENT_OF_REGU;
		}

		public void setR8_COMPONENT_OF_REGU(BigDecimal r8_COMPONENT_OF_REGU) {
			R8_COMPONENT_OF_REGU = r8_COMPONENT_OF_REGU;
		}

		public String getR8_SOURCE_REF() {
			return R8_SOURCE_REF;
		}

		public void setR8_SOURCE_REF(String r8_SOURCE_REF) {
			R8_SOURCE_REF = r8_SOURCE_REF;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_COMPONENT_OF_REGU() {
			return R9_COMPONENT_OF_REGU;
		}

		public void setR9_COMPONENT_OF_REGU(BigDecimal r9_COMPONENT_OF_REGU) {
			R9_COMPONENT_OF_REGU = r9_COMPONENT_OF_REGU;
		}

		public String getR9_SOURCE_REF() {
			return R9_SOURCE_REF;
		}

		public void setR9_SOURCE_REF(String r9_SOURCE_REF) {
			R9_SOURCE_REF = r9_SOURCE_REF;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_COMPONENT_OF_REGU() {
			return R10_COMPONENT_OF_REGU;
		}

		public void setR10_COMPONENT_OF_REGU(BigDecimal r10_COMPONENT_OF_REGU) {
			R10_COMPONENT_OF_REGU = r10_COMPONENT_OF_REGU;
		}

		public String getR10_SOURCE_REF() {
			return R10_SOURCE_REF;
		}

		public void setR10_SOURCE_REF(String r10_SOURCE_REF) {
			R10_SOURCE_REF = r10_SOURCE_REF;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_COMPONENT_OF_REGU() {
			return R11_COMPONENT_OF_REGU;
		}

		public void setR11_COMPONENT_OF_REGU(BigDecimal r11_COMPONENT_OF_REGU) {
			R11_COMPONENT_OF_REGU = r11_COMPONENT_OF_REGU;
		}

		public String getR11_SOURCE_REF() {
			return R11_SOURCE_REF;
		}

		public void setR11_SOURCE_REF(String r11_SOURCE_REF) {
			R11_SOURCE_REF = r11_SOURCE_REF;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_COMPONENT_OF_REGU() {
			return R12_COMPONENT_OF_REGU;
		}

		public void setR12_COMPONENT_OF_REGU(BigDecimal r12_COMPONENT_OF_REGU) {
			R12_COMPONENT_OF_REGU = r12_COMPONENT_OF_REGU;
		}

		public String getR12_SOURCE_REF() {
			return R12_SOURCE_REF;
		}

		public void setR12_SOURCE_REF(String r12_SOURCE_REF) {
			R12_SOURCE_REF = r12_SOURCE_REF;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_COMPONENT_OF_REGU() {
			return R13_COMPONENT_OF_REGU;
		}

		public void setR13_COMPONENT_OF_REGU(BigDecimal r13_COMPONENT_OF_REGU) {
			R13_COMPONENT_OF_REGU = r13_COMPONENT_OF_REGU;
		}

		public String getR13_SOURCE_REF() {
			return R13_SOURCE_REF;
		}

		public void setR13_SOURCE_REF(String r13_SOURCE_REF) {
			R13_SOURCE_REF = r13_SOURCE_REF;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_COMPONENT_OF_REGU() {
			return R14_COMPONENT_OF_REGU;
		}

		public void setR14_COMPONENT_OF_REGU(BigDecimal r14_COMPONENT_OF_REGU) {
			R14_COMPONENT_OF_REGU = r14_COMPONENT_OF_REGU;
		}

		public String getR14_SOURCE_REF() {
			return R14_SOURCE_REF;
		}

		public void setR14_SOURCE_REF(String r14_SOURCE_REF) {
			R14_SOURCE_REF = r14_SOURCE_REF;
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

	class CommonDisclosureArchivalRowMapper implements RowMapper<Common_Disclosure_Archival_Summary_Entity> {

		@Override
		public Common_Disclosure_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Common_Disclosure_Archival_Summary_Entity obj = new Common_Disclosure_Archival_Summary_Entity();

			// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_COMPONENT_OF_REGU(rs.getBigDecimal("R7_COMPONENT_OF_REGU"));
			obj.setR7_SOURCE_REF(rs.getString("R7_SOURCE_REF"));

			// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_COMPONENT_OF_REGU(rs.getBigDecimal("R8_COMPONENT_OF_REGU"));
			obj.setR8_SOURCE_REF(rs.getString("R8_SOURCE_REF"));

			// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_COMPONENT_OF_REGU(rs.getBigDecimal("R9_COMPONENT_OF_REGU"));
			obj.setR9_SOURCE_REF(rs.getString("R9_SOURCE_REF"));

			// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_COMPONENT_OF_REGU(rs.getBigDecimal("R10_COMPONENT_OF_REGU"));
			obj.setR10_SOURCE_REF(rs.getString("R10_SOURCE_REF"));

			// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_COMPONENT_OF_REGU(rs.getBigDecimal("R11_COMPONENT_OF_REGU"));
			obj.setR11_SOURCE_REF(rs.getString("R11_SOURCE_REF"));

			// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_COMPONENT_OF_REGU(rs.getBigDecimal("R12_COMPONENT_OF_REGU"));
			obj.setR12_SOURCE_REF(rs.getString("R12_SOURCE_REF"));

			// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_COMPONENT_OF_REGU(rs.getBigDecimal("R13_COMPONENT_OF_REGU"));
			obj.setR13_SOURCE_REF(rs.getString("R13_SOURCE_REF"));

			// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_COMPONENT_OF_REGU(rs.getBigDecimal("R14_COMPONENT_OF_REGU"));
			obj.setR14_SOURCE_REF(rs.getString("R14_SOURCE_REF"));

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

	@IdClass(Common_Disclosure_PK.class)
	public class Common_Disclosure_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R7_PRODUCT", length = 100)
		private String R7_PRODUCT;

		@Column(name = "R7_COMPONENT_OF_REGU")
		private BigDecimal R7_COMPONENT_OF_REGU;

		@Column(name = "R7_SOURCE_REF", length = 200)
		private String R7_SOURCE_REF;

		@Column(name = "R8_PRODUCT", length = 100)
		private String R8_PRODUCT;

		@Column(name = "R8_COMPONENT_OF_REGU")
		private BigDecimal R8_COMPONENT_OF_REGU;

		@Column(name = "R8_SOURCE_REF", length = 100)
		private String R8_SOURCE_REF;

		@Column(name = "R9_PRODUCT", length = 100)
		private String R9_PRODUCT;

		@Column(name = "R9_COMPONENT_OF_REGU")
		private BigDecimal R9_COMPONENT_OF_REGU;

		@Column(name = "R9_SOURCE_REF", length = 100)
		private String R9_SOURCE_REF;

		@Column(name = "R10_PRODUCT", length = 200)
		private String R10_PRODUCT;

		@Column(name = "R10_COMPONENT_OF_REGU")
		private BigDecimal R10_COMPONENT_OF_REGU;

		@Column(name = "R10_SOURCE_REF", length = 100)
		private String R10_SOURCE_REF;

		@Column(name = "R11_PRODUCT", length = 200)
		private String R11_PRODUCT;

		@Column(name = "R11_COMPONENT_OF_REGU")
		private BigDecimal R11_COMPONENT_OF_REGU;

		@Column(name = "R11_SOURCE_REF", length = 100)
		private String R11_SOURCE_REF;

		@Column(name = "R12_PRODUCT", length = 100)
		private String R12_PRODUCT;

		@Column(name = "R12_COMPONENT_OF_REGU")
		private BigDecimal R12_COMPONENT_OF_REGU;

		@Column(name = "R12_SOURCE_REF", length = 100)
		private String R12_SOURCE_REF;

		@Column(name = "R13_PRODUCT", length = 100)
		private String R13_PRODUCT;

		@Column(name = "R13_COMPONENT_OF_REGU")
		private BigDecimal R13_COMPONENT_OF_REGU;

		@Column(name = "R13_SOURCE_REF", length = 100)
		private String R13_SOURCE_REF;

		@Column(name = "R14_PRODUCT", length = 100)
		private String R14_PRODUCT;

		@Column(name = "R14_COMPONENT_OF_REGU")
		private BigDecimal R14_COMPONENT_OF_REGU;

		@Column(name = "R14_SOURCE_REF", length = 100)
		private String R14_SOURCE_REF;
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

		public String getR7_PRODUCT() {
			return R7_PRODUCT;
		}

		public void setR7_PRODUCT(String r7_PRODUCT) {
			R7_PRODUCT = r7_PRODUCT;
		}

		public BigDecimal getR7_COMPONENT_OF_REGU() {
			return R7_COMPONENT_OF_REGU;
		}

		public void setR7_COMPONENT_OF_REGU(BigDecimal r7_COMPONENT_OF_REGU) {
			R7_COMPONENT_OF_REGU = r7_COMPONENT_OF_REGU;
		}

		public String getR7_SOURCE_REF() {
			return R7_SOURCE_REF;
		}

		public void setR7_SOURCE_REF(String r7_SOURCE_REF) {
			R7_SOURCE_REF = r7_SOURCE_REF;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_COMPONENT_OF_REGU() {
			return R8_COMPONENT_OF_REGU;
		}

		public void setR8_COMPONENT_OF_REGU(BigDecimal r8_COMPONENT_OF_REGU) {
			R8_COMPONENT_OF_REGU = r8_COMPONENT_OF_REGU;
		}

		public String getR8_SOURCE_REF() {
			return R8_SOURCE_REF;
		}

		public void setR8_SOURCE_REF(String r8_SOURCE_REF) {
			R8_SOURCE_REF = r8_SOURCE_REF;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_COMPONENT_OF_REGU() {
			return R9_COMPONENT_OF_REGU;
		}

		public void setR9_COMPONENT_OF_REGU(BigDecimal r9_COMPONENT_OF_REGU) {
			R9_COMPONENT_OF_REGU = r9_COMPONENT_OF_REGU;
		}

		public String getR9_SOURCE_REF() {
			return R9_SOURCE_REF;
		}

		public void setR9_SOURCE_REF(String r9_SOURCE_REF) {
			R9_SOURCE_REF = r9_SOURCE_REF;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_COMPONENT_OF_REGU() {
			return R10_COMPONENT_OF_REGU;
		}

		public void setR10_COMPONENT_OF_REGU(BigDecimal r10_COMPONENT_OF_REGU) {
			R10_COMPONENT_OF_REGU = r10_COMPONENT_OF_REGU;
		}

		public String getR10_SOURCE_REF() {
			return R10_SOURCE_REF;
		}

		public void setR10_SOURCE_REF(String r10_SOURCE_REF) {
			R10_SOURCE_REF = r10_SOURCE_REF;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_COMPONENT_OF_REGU() {
			return R11_COMPONENT_OF_REGU;
		}

		public void setR11_COMPONENT_OF_REGU(BigDecimal r11_COMPONENT_OF_REGU) {
			R11_COMPONENT_OF_REGU = r11_COMPONENT_OF_REGU;
		}

		public String getR11_SOURCE_REF() {
			return R11_SOURCE_REF;
		}

		public void setR11_SOURCE_REF(String r11_SOURCE_REF) {
			R11_SOURCE_REF = r11_SOURCE_REF;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_COMPONENT_OF_REGU() {
			return R12_COMPONENT_OF_REGU;
		}

		public void setR12_COMPONENT_OF_REGU(BigDecimal r12_COMPONENT_OF_REGU) {
			R12_COMPONENT_OF_REGU = r12_COMPONENT_OF_REGU;
		}

		public String getR12_SOURCE_REF() {
			return R12_SOURCE_REF;
		}

		public void setR12_SOURCE_REF(String r12_SOURCE_REF) {
			R12_SOURCE_REF = r12_SOURCE_REF;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_COMPONENT_OF_REGU() {
			return R13_COMPONENT_OF_REGU;
		}

		public void setR13_COMPONENT_OF_REGU(BigDecimal r13_COMPONENT_OF_REGU) {
			R13_COMPONENT_OF_REGU = r13_COMPONENT_OF_REGU;
		}

		public String getR13_SOURCE_REF() {
			return R13_SOURCE_REF;
		}

		public void setR13_SOURCE_REF(String r13_SOURCE_REF) {
			R13_SOURCE_REF = r13_SOURCE_REF;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_COMPONENT_OF_REGU() {
			return R14_COMPONENT_OF_REGU;
		}

		public void setR14_COMPONENT_OF_REGU(BigDecimal r14_COMPONENT_OF_REGU) {
			R14_COMPONENT_OF_REGU = r14_COMPONENT_OF_REGU;
		}

		public String getR14_SOURCE_REF() {
			return R14_SOURCE_REF;
		}

		public void setR14_SOURCE_REF(String r14_SOURCE_REF) {
			R14_SOURCE_REF = r14_SOURCE_REF;
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

	public static class Common_Disclosure_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Common_Disclosure_PK() {
		}

		public Common_Disclosure_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Common_Disclosure_PK))
				return false;
			Common_Disclosure_PK that = (Common_Disclosure_PK) o;
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

	public class Common_Disclosure_Detail_Entity {

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

	class CommonDisclosureDetailRowMapper implements RowMapper<Common_Disclosure_Detail_Entity> {

		@Override
		public Common_Disclosure_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Common_Disclosure_Detail_Entity obj = new Common_Disclosure_Detail_Entity();

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

	class CommonDisclosureArchivalDetailRowMapper implements RowMapper<Common_Disclosure_Archival_Detail_Entity> {

		@Override
		public Common_Disclosure_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Common_Disclosure_Archival_Detail_Entity obj = new Common_Disclosure_Archival_Detail_Entity();

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

	public class Common_Disclosure_Archival_Detail_Entity {

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

	public ModelAndView getCommon_DisclosureView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("Common_Disclosure View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<Common_Disclosure_Archival_Summary_Entity> T1Master = new ArrayList<>();

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
			List<Common_Disclosure_Summary_Entity> T1Master = new ArrayList<>();
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

		mv.setViewName("BRRS/Common_Disclosure");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getCommon_DisclosurecurrentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<Common_Disclosure_Archival_Detail_Entity> archivalDetailList;

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

				List<Common_Disclosure_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/Common_Disclosure");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getCommon_DisclosureArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Common_Disclosure_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Common_Disclosure_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Common_Disclosure_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Common_Disclosure  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/Common_Disclosure");

		if (acct_number != null) {
			Common_Disclosure_Detail_Entity Common_DisclosureEntity = findByAcctnumber(acct_number);
			if (Common_DisclosureEntity != null && Common_DisclosureEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
						.format(Common_DisclosureEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Common_DisclosureData", Common_DisclosureEntity);
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
			Common_Disclosure_Detail_Entity existing = findByAcctnumber(acctNo);

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

				String sql = "UPDATE BRRS_COMMON_DISCLOSURE_DETAILTABLE " + "SET ACCT_NAME = ?, "
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

							jdbcTemplate.update("BEGIN BRRS_COMMON_DISCLOSURE_SUMMARY_PROCEDURE(?); END;",
									formattedDate);

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

	public byte[] getCommon_DisclosureDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  Common_Disclosure Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getCommon_DisclosureDetailNewExcelARCHIVAL(filename, fromdate, todate, currency,
						dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Common_DisclosureDetailsDetail");

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
			List<Common_Disclosure_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Common_Disclosure_Detail_Entity item : reportData) {
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
				logger.info("No data found for Common_Disclosure — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Common_Disclosure Excel", e);
			return new byte[0];
		}
	}

	public byte[] getCommon_DisclosureDetailNewExcelARCHIVAL(String filename, String fromdate, String todate,
			String currency, String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Common_Disclosure ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Common_Disclosure Detail NEW");

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
			List<Common_Disclosure_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Common_Disclosure_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for Common_Disclosure — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Common_Disclosure NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getCommon_DisclosureExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.CommonDisclosure");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelCommon_DisclosureARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// Fetch data

		List<Common_Disclosure_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  Common_Disclosure report. Returning empty result.");
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
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Common_Disclosure_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// A TABLE
					// ---------- R7 (Row 7 -> index 6) ----------
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR7_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR7_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R8 (Row 8 -> index 7) ----------
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR8_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR8_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R9 (Row 9 -> index 8) ----------
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR9_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR9_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R10 (Row 10 -> index 9) ----------
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR10_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR10_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R11 (Row 11 -> index 10) ----------
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR11_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR11_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ROW 12
					// row = sheet.getRow(11);

					// cellC = row.getCell(4);
					// if (cellC == null)
					// cellC = row.createCell(4);
					// if (record.getR12_COMPONENT_OF_REGU() != null) {
					// cellC.setCellValue(record.getR12_COMPONENT_OF_REGU().doubleValue());
					// } else {
					// cellC.setCellValue(0);
					// }

					// ROW 13
					row = sheet.getRow(12);

					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);
					if (record.getR13_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR13_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ROW 14
					row = sheet.getRow(13);

					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);
					if (record.getR14_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR14_COMPONENT_OF_REGU().doubleValue());
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

			return out.toByteArray();
		}

	}

	public byte[] getExcelCommon_DisclosureARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Common_Disclosure_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Common_Disclosure new report. Returning empty result.");
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
					Common_Disclosure_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// A TABLE
					// ---------- R7 (Row 7 -> index 6) ----------
					row = sheet.getRow(6);
					Cell cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR7_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR7_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R8 (Row 8 -> index 7) ----------
					row = sheet.getRow(7);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR8_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR8_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R9 (Row 9 -> index 8) ----------
					row = sheet.getRow(8);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR9_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR9_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R10 (Row 10 -> index 9) ----------
					row = sheet.getRow(9);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR10_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR10_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ---------- R11 (Row 11 -> index 10) ----------
					row = sheet.getRow(10);
					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);

					if (record.getR11_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR11_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ROW 12
					// row = sheet.getRow(11);

					// cellC = row.getCell(4);
					// if (cellC == null)
					// cellC = row.createCell(4);
					// if (record.getR12_COMPONENT_OF_REGU() != null) {
					// cellC.setCellValue(record.getR12_COMPONENT_OF_REGU().doubleValue());
					// } else {
					// cellC.setCellValue(0);
					// }

					// ROW 13
					row = sheet.getRow(12);

					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);
					if (record.getR13_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR13_COMPONENT_OF_REGU().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// ROW 14
					row = sheet.getRow(13);

					cellC = row.getCell(4);
					if (cellC == null)
						cellC = row.createCell(4);
					if (record.getR14_COMPONENT_OF_REGU() != null) {
						cellC.setCellValue(record.getR14_COMPONENT_OF_REGU().doubleValue());
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

			return out.toByteArray();
		}

	}

}