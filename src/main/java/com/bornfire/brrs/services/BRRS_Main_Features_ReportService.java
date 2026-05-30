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
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_Main_Features_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Main_Features_ReportService.class);

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
	public List<Main_Features_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Main_FeaturesRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getMain_FeaturesArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Main_Features_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new Main_FeaturesArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<Main_Features_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Main_FeaturesArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Main_Features_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new Main_FeaturesDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Main_Features_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Main_FeaturesDetailRowMapper());
	}

// 3. PAGINATION

	public List<Main_Features_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new Main_FeaturesDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_MAIN_FEATURES_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Main_Features_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Main_FeaturesDetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public Main_Features_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new Main_FeaturesDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<Main_Features_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new Main_FeaturesArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<Main_Features_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MAIN_FEATURES_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new Main_FeaturesArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class Main_FeaturesRowMapper implements RowMapper<Main_Features_Summary_Entity> {

		@Override
		public Main_Features_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Main_Features_Summary_Entity obj = new Main_Features_Summary_Entity();

			// R3
			obj.setR3_PRODUCT(rs.getString("R3_PRODUCT"));
			obj.setR3_AMT(rs.getBigDecimal("R3_AMT"));

// R4
			obj.setR4_PRODUCT(rs.getString("R4_PRODUCT"));
			obj.setR4_AMT(rs.getBigDecimal("R4_AMT"));

// R5
			obj.setR5_PRODUCT(rs.getString("R5_PRODUCT"));
			obj.setR5_AMT(rs.getBigDecimal("R5_AMT"));

// R6
			obj.setR6_PRODUCT(rs.getString("R6_PRODUCT"));
			obj.setR6_AMT(rs.getBigDecimal("R6_AMT"));

// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_AMT(rs.getBigDecimal("R7_AMT"));

// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_AMT(rs.getBigDecimal("R8_AMT"));

// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_AMT(rs.getBigDecimal("R9_AMT"));

// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_AMT(rs.getBigDecimal("R10_AMT"));

// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_AMT(rs.getBigDecimal("R11_AMT"));

// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_AMT(rs.getBigDecimal("R12_AMT"));

// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_AMT(rs.getBigDecimal("R13_AMT"));

// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_AMT(rs.getBigDecimal("R14_AMT"));

// R15
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_AMT(rs.getBigDecimal("R15_AMT"));

// R16
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_AMT(rs.getBigDecimal("R16_AMT"));

// R17
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_AMT(rs.getBigDecimal("R17_AMT"));

// R18
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_AMT(rs.getBigDecimal("R18_AMT"));

// R19
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_AMT(rs.getBigDecimal("R19_AMT"));

// R20
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_AMT(rs.getBigDecimal("R20_AMT"));

// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_AMT(rs.getBigDecimal("R21_AMT"));

// R22
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_AMT(rs.getBigDecimal("R22_AMT"));

// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_AMT(rs.getBigDecimal("R23_AMT"));

// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_AMT(rs.getBigDecimal("R24_AMT"));

// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_AMT(rs.getBigDecimal("R25_AMT"));

// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_AMT(rs.getBigDecimal("R26_AMT"));

// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_AMT(rs.getBigDecimal("R27_AMT"));

// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_AMT(rs.getBigDecimal("R28_AMT"));

// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_AMT(rs.getBigDecimal("R29_AMT"));

// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_AMT(rs.getBigDecimal("R30_AMT"));

// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_AMT(rs.getBigDecimal("R31_AMT"));

// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_AMT(rs.getBigDecimal("R32_AMT"));

// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_AMT(rs.getBigDecimal("R33_AMT"));

// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_AMT(rs.getBigDecimal("R34_AMT"));

// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_AMT(rs.getBigDecimal("R35_AMT"));

// R36
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_AMT(rs.getBigDecimal("R36_AMT"));

// R37
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_AMT(rs.getBigDecimal("R37_AMT"));

// R38
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_AMT(rs.getBigDecimal("R38_AMT"));

// R39
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_AMT(rs.getBigDecimal("R39_AMT"));

// R40
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_AMT(rs.getBigDecimal("R40_AMT"));

// R41
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_AMT(rs.getBigDecimal("R41_AMT"));

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

	public static class Main_Features_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R3_PRODUCT")
		private String R3_PRODUCT;

		@Column(name = "R3_AMT")
		private BigDecimal R3_AMT;

		@Column(name = "R4_PRODUCT")
		private String R4_PRODUCT;

		@Column(name = "R4_AMT")
		private BigDecimal R4_AMT;

		@Column(name = "R5_PRODUCT")
		private String R5_PRODUCT;

		@Column(name = "R5_AMT")
		private BigDecimal R5_AMT;

		@Column(name = "R6_PRODUCT")
		private String R6_PRODUCT;

		@Column(name = "R6_AMT")
		private BigDecimal R6_AMT;

		@Column(name = "R7_PRODUCT")
		private String R7_PRODUCT;

		@Column(name = "R7_AMT")
		private BigDecimal R7_AMT;

		@Column(name = "R8_PRODUCT")
		private String R8_PRODUCT;

		@Column(name = "R8_AMT")
		private BigDecimal R8_AMT;

		@Column(name = "R9_PRODUCT")
		private String R9_PRODUCT;

		@Column(name = "R9_AMT")
		private BigDecimal R9_AMT;

		@Column(name = "R10_PRODUCT")
		private String R10_PRODUCT;

		@Column(name = "R10_AMT")
		private BigDecimal R10_AMT;

		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;

		@Column(name = "R11_AMT")
		private BigDecimal R11_AMT;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;

		@Column(name = "R12_AMT")
		private BigDecimal R12_AMT;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;

		@Column(name = "R13_AMT")
		private BigDecimal R13_AMT;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;

		@Column(name = "R14_AMT")
		private BigDecimal R14_AMT;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;

		@Column(name = "R15_AMT")
		private BigDecimal R15_AMT;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;

		@Column(name = "R16_AMT")
		private BigDecimal R16_AMT;

		@Column(name = "R17_PRODUCT")
		private String R17_PRODUCT;

		@Column(name = "R17_AMT")
		private BigDecimal R17_AMT;

		@Column(name = "R18_PRODUCT")
		private String R18_PRODUCT;

		@Column(name = "R18_AMT")
		private BigDecimal R18_AMT;

		@Column(name = "R19_PRODUCT")
		private String R19_PRODUCT;

		@Column(name = "R19_AMT")
		private BigDecimal R19_AMT;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;

		@Column(name = "R20_AMT")
		private BigDecimal R20_AMT;

		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;

		@Column(name = "R21_AMT")
		private BigDecimal R21_AMT;

		@Column(name = "R22_PRODUCT")
		private String R22_PRODUCT;

		@Column(name = "R22_AMT")
		private BigDecimal R22_AMT;

		@Column(name = "R23_PRODUCT")
		private String R23_PRODUCT;

		@Column(name = "R23_AMT")
		private BigDecimal R23_AMT;

		@Column(name = "R24_PRODUCT")
		private String R24_PRODUCT;

		@Column(name = "R24_AMT")
		private BigDecimal R24_AMT;

		@Column(name = "R25_PRODUCT")
		private String R25_PRODUCT;

		@Column(name = "R25_AMT")
		private BigDecimal R25_AMT;

		@Column(name = "R26_PRODUCT")
		private String R26_PRODUCT;

		@Column(name = "R26_AMT")
		private BigDecimal R26_AMT;

		@Column(name = "R27_PRODUCT")
		private String R27_PRODUCT;

		@Column(name = "R27_AMT")
		private BigDecimal R27_AMT;

		@Column(name = "R28_PRODUCT")
		private String R28_PRODUCT;

		@Column(name = "R28_AMT")
		private BigDecimal R28_AMT;

		@Column(name = "R29_PRODUCT")
		private String R29_PRODUCT;

		@Column(name = "R29_AMT")
		private BigDecimal R29_AMT;

		@Column(name = "R30_PRODUCT")
		private String R30_PRODUCT;

		@Column(name = "R30_AMT")
		private BigDecimal R30_AMT;

		@Column(name = "R31_PRODUCT")
		private String R31_PRODUCT;

		@Column(name = "R31_AMT")
		private BigDecimal R31_AMT;

		@Column(name = "R32_PRODUCT")
		private String R32_PRODUCT;

		@Column(name = "R32_AMT")
		private BigDecimal R32_AMT;

		@Column(name = "R33_PRODUCT")
		private String R33_PRODUCT;

		@Column(name = "R33_AMT")
		private BigDecimal R33_AMT;

		@Column(name = "R34_PRODUCT")
		private String R34_PRODUCT;

		@Column(name = "R34_AMT")
		private BigDecimal R34_AMT;

		@Column(name = "R35_PRODUCT")
		private String R35_PRODUCT;

		@Column(name = "R35_AMT")
		private BigDecimal R35_AMT;

		@Column(name = "R36_PRODUCT")
		private String R36_PRODUCT;

		@Column(name = "R36_AMT")
		private BigDecimal R36_AMT;

		@Column(name = "R37_PRODUCT")
		private String R37_PRODUCT;

		@Column(name = "R37_AMT")
		private BigDecimal R37_AMT;

		@Column(name = "R38_PRODUCT")
		private String R38_PRODUCT;

		@Column(name = "R38_AMT")
		private BigDecimal R38_AMT;

		@Column(name = "R39_PRODUCT")
		private String R39_PRODUCT;

		@Column(name = "R39_AMT")
		private BigDecimal R39_AMT;

		@Column(name = "R40_PRODUCT")
		private String R40_PRODUCT;

		@Column(name = "R40_AMT")
		private BigDecimal R40_AMT;

		@Column(name = "R41_PRODUCT")
		private String R41_PRODUCT;

		@Column(name = "R41_AMT")
		private BigDecimal R41_AMT;

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

		public String getR3_PRODUCT() {
			return R3_PRODUCT;
		}

		public void setR3_PRODUCT(String r3_PRODUCT) {
			R3_PRODUCT = r3_PRODUCT;
		}

		public BigDecimal getR3_AMT() {
			return R3_AMT;
		}

		public void setR3_AMT(BigDecimal r3_AMT) {
			R3_AMT = r3_AMT;
		}

		public String getR4_PRODUCT() {
			return R4_PRODUCT;
		}

		public void setR4_PRODUCT(String r4_PRODUCT) {
			R4_PRODUCT = r4_PRODUCT;
		}

		public BigDecimal getR4_AMT() {
			return R4_AMT;
		}

		public void setR4_AMT(BigDecimal r4_AMT) {
			R4_AMT = r4_AMT;
		}

		public String getR5_PRODUCT() {
			return R5_PRODUCT;
		}

		public void setR5_PRODUCT(String r5_PRODUCT) {
			R5_PRODUCT = r5_PRODUCT;
		}

		public BigDecimal getR5_AMT() {
			return R5_AMT;
		}

		public void setR5_AMT(BigDecimal r5_AMT) {
			R5_AMT = r5_AMT;
		}

		public String getR6_PRODUCT() {
			return R6_PRODUCT;
		}

		public void setR6_PRODUCT(String r6_PRODUCT) {
			R6_PRODUCT = r6_PRODUCT;
		}

		public BigDecimal getR6_AMT() {
			return R6_AMT;
		}

		public void setR6_AMT(BigDecimal r6_AMT) {
			R6_AMT = r6_AMT;
		}

		public String getR7_PRODUCT() {
			return R7_PRODUCT;
		}

		public void setR7_PRODUCT(String r7_PRODUCT) {
			R7_PRODUCT = r7_PRODUCT;
		}

		public BigDecimal getR7_AMT() {
			return R7_AMT;
		}

		public void setR7_AMT(BigDecimal r7_AMT) {
			R7_AMT = r7_AMT;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_AMT() {
			return R8_AMT;
		}

		public void setR8_AMT(BigDecimal r8_AMT) {
			R8_AMT = r8_AMT;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_AMT() {
			return R9_AMT;
		}

		public void setR9_AMT(BigDecimal r9_AMT) {
			R9_AMT = r9_AMT;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_AMT() {
			return R10_AMT;
		}

		public void setR10_AMT(BigDecimal r10_AMT) {
			R10_AMT = r10_AMT;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_AMT() {
			return R11_AMT;
		}

		public void setR11_AMT(BigDecimal r11_AMT) {
			R11_AMT = r11_AMT;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_AMT() {
			return R12_AMT;
		}

		public void setR12_AMT(BigDecimal r12_AMT) {
			R12_AMT = r12_AMT;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_AMT() {
			return R13_AMT;
		}

		public void setR13_AMT(BigDecimal r13_AMT) {
			R13_AMT = r13_AMT;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_AMT() {
			return R14_AMT;
		}

		public void setR14_AMT(BigDecimal r14_AMT) {
			R14_AMT = r14_AMT;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_AMT() {
			return R15_AMT;
		}

		public void setR15_AMT(BigDecimal r15_AMT) {
			R15_AMT = r15_AMT;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_AMT() {
			return R16_AMT;
		}

		public void setR16_AMT(BigDecimal r16_AMT) {
			R16_AMT = r16_AMT;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_AMT() {
			return R17_AMT;
		}

		public void setR17_AMT(BigDecimal r17_AMT) {
			R17_AMT = r17_AMT;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_AMT() {
			return R18_AMT;
		}

		public void setR18_AMT(BigDecimal r18_AMT) {
			R18_AMT = r18_AMT;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_AMT() {
			return R19_AMT;
		}

		public void setR19_AMT(BigDecimal r19_AMT) {
			R19_AMT = r19_AMT;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_AMT() {
			return R20_AMT;
		}

		public void setR20_AMT(BigDecimal r20_AMT) {
			R20_AMT = r20_AMT;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_AMT() {
			return R21_AMT;
		}

		public void setR21_AMT(BigDecimal r21_AMT) {
			R21_AMT = r21_AMT;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_AMT() {
			return R22_AMT;
		}

		public void setR22_AMT(BigDecimal r22_AMT) {
			R22_AMT = r22_AMT;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_AMT() {
			return R23_AMT;
		}

		public void setR23_AMT(BigDecimal r23_AMT) {
			R23_AMT = r23_AMT;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_AMT() {
			return R24_AMT;
		}

		public void setR24_AMT(BigDecimal r24_AMT) {
			R24_AMT = r24_AMT;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_AMT() {
			return R25_AMT;
		}

		public void setR25_AMT(BigDecimal r25_AMT) {
			R25_AMT = r25_AMT;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_AMT() {
			return R26_AMT;
		}

		public void setR26_AMT(BigDecimal r26_AMT) {
			R26_AMT = r26_AMT;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_AMT() {
			return R27_AMT;
		}

		public void setR27_AMT(BigDecimal r27_AMT) {
			R27_AMT = r27_AMT;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_AMT() {
			return R28_AMT;
		}

		public void setR28_AMT(BigDecimal r28_AMT) {
			R28_AMT = r28_AMT;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_AMT() {
			return R29_AMT;
		}

		public void setR29_AMT(BigDecimal r29_AMT) {
			R29_AMT = r29_AMT;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_AMT() {
			return R30_AMT;
		}

		public void setR30_AMT(BigDecimal r30_AMT) {
			R30_AMT = r30_AMT;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_AMT() {
			return R31_AMT;
		}

		public void setR31_AMT(BigDecimal r31_AMT) {
			R31_AMT = r31_AMT;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_AMT() {
			return R32_AMT;
		}

		public void setR32_AMT(BigDecimal r32_AMT) {
			R32_AMT = r32_AMT;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_AMT() {
			return R33_AMT;
		}

		public void setR33_AMT(BigDecimal r33_AMT) {
			R33_AMT = r33_AMT;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_AMT() {
			return R34_AMT;
		}

		public void setR34_AMT(BigDecimal r34_AMT) {
			R34_AMT = r34_AMT;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_AMT() {
			return R35_AMT;
		}

		public void setR35_AMT(BigDecimal r35_AMT) {
			R35_AMT = r35_AMT;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_AMT() {
			return R36_AMT;
		}

		public void setR36_AMT(BigDecimal r36_AMT) {
			R36_AMT = r36_AMT;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_AMT() {
			return R37_AMT;
		}

		public void setR37_AMT(BigDecimal r37_AMT) {
			R37_AMT = r37_AMT;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_AMT() {
			return R38_AMT;
		}

		public void setR38_AMT(BigDecimal r38_AMT) {
			R38_AMT = r38_AMT;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_AMT() {
			return R39_AMT;
		}

		public void setR39_AMT(BigDecimal r39_AMT) {
			R39_AMT = r39_AMT;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_AMT() {
			return R40_AMT;
		}

		public void setR40_AMT(BigDecimal r40_AMT) {
			R40_AMT = r40_AMT;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_AMT() {
			return R41_AMT;
		}

		public void setR41_AMT(BigDecimal r41_AMT) {
			R41_AMT = r41_AMT;
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

	class Main_FeaturesArchivalRowMapper implements RowMapper<Main_Features_Archival_Summary_Entity> {

		@Override
		public Main_Features_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Main_Features_Archival_Summary_Entity obj = new Main_Features_Archival_Summary_Entity();

			// R3
			obj.setR3_PRODUCT(rs.getString("R3_PRODUCT"));
			obj.setR3_AMT(rs.getBigDecimal("R3_AMT"));

// R4
			obj.setR4_PRODUCT(rs.getString("R4_PRODUCT"));
			obj.setR4_AMT(rs.getBigDecimal("R4_AMT"));

// R5
			obj.setR5_PRODUCT(rs.getString("R5_PRODUCT"));
			obj.setR5_AMT(rs.getBigDecimal("R5_AMT"));

// R6
			obj.setR6_PRODUCT(rs.getString("R6_PRODUCT"));
			obj.setR6_AMT(rs.getBigDecimal("R6_AMT"));

// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_AMT(rs.getBigDecimal("R7_AMT"));

// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_AMT(rs.getBigDecimal("R8_AMT"));

// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_AMT(rs.getBigDecimal("R9_AMT"));

// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_AMT(rs.getBigDecimal("R10_AMT"));

// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_AMT(rs.getBigDecimal("R11_AMT"));

// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_AMT(rs.getBigDecimal("R12_AMT"));

// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_AMT(rs.getBigDecimal("R13_AMT"));

// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_AMT(rs.getBigDecimal("R14_AMT"));

// R15
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_AMT(rs.getBigDecimal("R15_AMT"));

// R16
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_AMT(rs.getBigDecimal("R16_AMT"));

// R17
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_AMT(rs.getBigDecimal("R17_AMT"));

// R18
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_AMT(rs.getBigDecimal("R18_AMT"));

// R19
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_AMT(rs.getBigDecimal("R19_AMT"));

// R20
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_AMT(rs.getBigDecimal("R20_AMT"));

// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_AMT(rs.getBigDecimal("R21_AMT"));

// R22
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_AMT(rs.getBigDecimal("R22_AMT"));

// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_AMT(rs.getBigDecimal("R23_AMT"));

// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_AMT(rs.getBigDecimal("R24_AMT"));

// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_AMT(rs.getBigDecimal("R25_AMT"));

// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_AMT(rs.getBigDecimal("R26_AMT"));

// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_AMT(rs.getBigDecimal("R27_AMT"));

// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_AMT(rs.getBigDecimal("R28_AMT"));

// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_AMT(rs.getBigDecimal("R29_AMT"));

// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_AMT(rs.getBigDecimal("R30_AMT"));

// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_AMT(rs.getBigDecimal("R31_AMT"));

// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_AMT(rs.getBigDecimal("R32_AMT"));

// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_AMT(rs.getBigDecimal("R33_AMT"));

// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_AMT(rs.getBigDecimal("R34_AMT"));

// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_AMT(rs.getBigDecimal("R35_AMT"));

// R36
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_AMT(rs.getBigDecimal("R36_AMT"));

// R37
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_AMT(rs.getBigDecimal("R37_AMT"));

// R38
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_AMT(rs.getBigDecimal("R38_AMT"));

// R39
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_AMT(rs.getBigDecimal("R39_AMT"));

// R40
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_AMT(rs.getBigDecimal("R40_AMT"));

// R41
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_AMT(rs.getBigDecimal("R41_AMT"));

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

	@IdClass(Main_Features_PK.class)
	public class Main_Features_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R3_PRODUCT")
		private String R3_PRODUCT;

		@Column(name = "R3_AMT")
		private BigDecimal R3_AMT;

		@Column(name = "R4_PRODUCT")
		private String R4_PRODUCT;

		@Column(name = "R4_AMT")
		private BigDecimal R4_AMT;

		@Column(name = "R5_PRODUCT")
		private String R5_PRODUCT;

		@Column(name = "R5_AMT")
		private BigDecimal R5_AMT;

		@Column(name = "R6_PRODUCT")
		private String R6_PRODUCT;

		@Column(name = "R6_AMT")
		private BigDecimal R6_AMT;

		@Column(name = "R7_PRODUCT")
		private String R7_PRODUCT;

		@Column(name = "R7_AMT")
		private BigDecimal R7_AMT;

		@Column(name = "R8_PRODUCT")
		private String R8_PRODUCT;

		@Column(name = "R8_AMT")
		private BigDecimal R8_AMT;

		@Column(name = "R9_PRODUCT")
		private String R9_PRODUCT;

		@Column(name = "R9_AMT")
		private BigDecimal R9_AMT;

		@Column(name = "R10_PRODUCT")
		private String R10_PRODUCT;

		@Column(name = "R10_AMT")
		private BigDecimal R10_AMT;

		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;

		@Column(name = "R11_AMT")
		private BigDecimal R11_AMT;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;

		@Column(name = "R12_AMT")
		private BigDecimal R12_AMT;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;

		@Column(name = "R13_AMT")
		private BigDecimal R13_AMT;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;

		@Column(name = "R14_AMT")
		private BigDecimal R14_AMT;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;

		@Column(name = "R15_AMT")
		private BigDecimal R15_AMT;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;

		@Column(name = "R16_AMT")
		private BigDecimal R16_AMT;

		@Column(name = "R17_PRODUCT")
		private String R17_PRODUCT;

		@Column(name = "R17_AMT")
		private BigDecimal R17_AMT;

		@Column(name = "R18_PRODUCT")
		private String R18_PRODUCT;

		@Column(name = "R18_AMT")
		private BigDecimal R18_AMT;

		@Column(name = "R19_PRODUCT")
		private String R19_PRODUCT;

		@Column(name = "R19_AMT")
		private BigDecimal R19_AMT;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;

		@Column(name = "R20_AMT")
		private BigDecimal R20_AMT;

		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;

		@Column(name = "R21_AMT")
		private BigDecimal R21_AMT;

		@Column(name = "R22_PRODUCT")
		private String R22_PRODUCT;

		@Column(name = "R22_AMT")
		private BigDecimal R22_AMT;

		@Column(name = "R23_PRODUCT")
		private String R23_PRODUCT;

		@Column(name = "R23_AMT")
		private BigDecimal R23_AMT;

		@Column(name = "R24_PRODUCT")
		private String R24_PRODUCT;

		@Column(name = "R24_AMT")
		private BigDecimal R24_AMT;

		@Column(name = "R25_PRODUCT")
		private String R25_PRODUCT;

		@Column(name = "R25_AMT")
		private BigDecimal R25_AMT;

		@Column(name = "R26_PRODUCT")
		private String R26_PRODUCT;

		@Column(name = "R26_AMT")
		private BigDecimal R26_AMT;

		@Column(name = "R27_PRODUCT")
		private String R27_PRODUCT;

		@Column(name = "R27_AMT")
		private BigDecimal R27_AMT;

		@Column(name = "R28_PRODUCT")
		private String R28_PRODUCT;

		@Column(name = "R28_AMT")
		private BigDecimal R28_AMT;

		@Column(name = "R29_PRODUCT")
		private String R29_PRODUCT;

		@Column(name = "R29_AMT")
		private BigDecimal R29_AMT;

		@Column(name = "R30_PRODUCT")
		private String R30_PRODUCT;

		@Column(name = "R30_AMT")
		private BigDecimal R30_AMT;

		@Column(name = "R31_PRODUCT")
		private String R31_PRODUCT;

		@Column(name = "R31_AMT")
		private BigDecimal R31_AMT;

		@Column(name = "R32_PRODUCT")
		private String R32_PRODUCT;

		@Column(name = "R32_AMT")
		private BigDecimal R32_AMT;

		@Column(name = "R33_PRODUCT")
		private String R33_PRODUCT;

		@Column(name = "R33_AMT")
		private BigDecimal R33_AMT;

		@Column(name = "R34_PRODUCT")
		private String R34_PRODUCT;

		@Column(name = "R34_AMT")
		private BigDecimal R34_AMT;

		@Column(name = "R35_PRODUCT")
		private String R35_PRODUCT;

		@Column(name = "R35_AMT")
		private BigDecimal R35_AMT;

		@Column(name = "R36_PRODUCT")
		private String R36_PRODUCT;

		@Column(name = "R36_AMT")
		private BigDecimal R36_AMT;

		@Column(name = "R37_PRODUCT")
		private String R37_PRODUCT;

		@Column(name = "R37_AMT")
		private BigDecimal R37_AMT;

		@Column(name = "R38_PRODUCT")
		private String R38_PRODUCT;

		@Column(name = "R38_AMT")
		private BigDecimal R38_AMT;

		@Column(name = "R39_PRODUCT")
		private String R39_PRODUCT;

		@Column(name = "R39_AMT")
		private BigDecimal R39_AMT;

		@Column(name = "R40_PRODUCT")
		private String R40_PRODUCT;

		@Column(name = "R40_AMT")
		private BigDecimal R40_AMT;

		@Column(name = "R41_PRODUCT")
		private String R41_PRODUCT;

		@Column(name = "R41_AMT")
		private BigDecimal R41_AMT;
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

		public String getR3_PRODUCT() {
			return R3_PRODUCT;
		}

		public void setR3_PRODUCT(String r3_PRODUCT) {
			R3_PRODUCT = r3_PRODUCT;
		}

		public BigDecimal getR3_AMT() {
			return R3_AMT;
		}

		public void setR3_AMT(BigDecimal r3_AMT) {
			R3_AMT = r3_AMT;
		}

		public String getR4_PRODUCT() {
			return R4_PRODUCT;
		}

		public void setR4_PRODUCT(String r4_PRODUCT) {
			R4_PRODUCT = r4_PRODUCT;
		}

		public BigDecimal getR4_AMT() {
			return R4_AMT;
		}

		public void setR4_AMT(BigDecimal r4_AMT) {
			R4_AMT = r4_AMT;
		}

		public String getR5_PRODUCT() {
			return R5_PRODUCT;
		}

		public void setR5_PRODUCT(String r5_PRODUCT) {
			R5_PRODUCT = r5_PRODUCT;
		}

		public BigDecimal getR5_AMT() {
			return R5_AMT;
		}

		public void setR5_AMT(BigDecimal r5_AMT) {
			R5_AMT = r5_AMT;
		}

		public String getR6_PRODUCT() {
			return R6_PRODUCT;
		}

		public void setR6_PRODUCT(String r6_PRODUCT) {
			R6_PRODUCT = r6_PRODUCT;
		}

		public BigDecimal getR6_AMT() {
			return R6_AMT;
		}

		public void setR6_AMT(BigDecimal r6_AMT) {
			R6_AMT = r6_AMT;
		}

		public String getR7_PRODUCT() {
			return R7_PRODUCT;
		}

		public void setR7_PRODUCT(String r7_PRODUCT) {
			R7_PRODUCT = r7_PRODUCT;
		}

		public BigDecimal getR7_AMT() {
			return R7_AMT;
		}

		public void setR7_AMT(BigDecimal r7_AMT) {
			R7_AMT = r7_AMT;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_AMT() {
			return R8_AMT;
		}

		public void setR8_AMT(BigDecimal r8_AMT) {
			R8_AMT = r8_AMT;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_AMT() {
			return R9_AMT;
		}

		public void setR9_AMT(BigDecimal r9_AMT) {
			R9_AMT = r9_AMT;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_AMT() {
			return R10_AMT;
		}

		public void setR10_AMT(BigDecimal r10_AMT) {
			R10_AMT = r10_AMT;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_AMT() {
			return R11_AMT;
		}

		public void setR11_AMT(BigDecimal r11_AMT) {
			R11_AMT = r11_AMT;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_AMT() {
			return R12_AMT;
		}

		public void setR12_AMT(BigDecimal r12_AMT) {
			R12_AMT = r12_AMT;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_AMT() {
			return R13_AMT;
		}

		public void setR13_AMT(BigDecimal r13_AMT) {
			R13_AMT = r13_AMT;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_AMT() {
			return R14_AMT;
		}

		public void setR14_AMT(BigDecimal r14_AMT) {
			R14_AMT = r14_AMT;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_AMT() {
			return R15_AMT;
		}

		public void setR15_AMT(BigDecimal r15_AMT) {
			R15_AMT = r15_AMT;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_AMT() {
			return R16_AMT;
		}

		public void setR16_AMT(BigDecimal r16_AMT) {
			R16_AMT = r16_AMT;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_AMT() {
			return R17_AMT;
		}

		public void setR17_AMT(BigDecimal r17_AMT) {
			R17_AMT = r17_AMT;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_AMT() {
			return R18_AMT;
		}

		public void setR18_AMT(BigDecimal r18_AMT) {
			R18_AMT = r18_AMT;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_AMT() {
			return R19_AMT;
		}

		public void setR19_AMT(BigDecimal r19_AMT) {
			R19_AMT = r19_AMT;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_AMT() {
			return R20_AMT;
		}

		public void setR20_AMT(BigDecimal r20_AMT) {
			R20_AMT = r20_AMT;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_AMT() {
			return R21_AMT;
		}

		public void setR21_AMT(BigDecimal r21_AMT) {
			R21_AMT = r21_AMT;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_AMT() {
			return R22_AMT;
		}

		public void setR22_AMT(BigDecimal r22_AMT) {
			R22_AMT = r22_AMT;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_AMT() {
			return R23_AMT;
		}

		public void setR23_AMT(BigDecimal r23_AMT) {
			R23_AMT = r23_AMT;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_AMT() {
			return R24_AMT;
		}

		public void setR24_AMT(BigDecimal r24_AMT) {
			R24_AMT = r24_AMT;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_AMT() {
			return R25_AMT;
		}

		public void setR25_AMT(BigDecimal r25_AMT) {
			R25_AMT = r25_AMT;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_AMT() {
			return R26_AMT;
		}

		public void setR26_AMT(BigDecimal r26_AMT) {
			R26_AMT = r26_AMT;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_AMT() {
			return R27_AMT;
		}

		public void setR27_AMT(BigDecimal r27_AMT) {
			R27_AMT = r27_AMT;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_AMT() {
			return R28_AMT;
		}

		public void setR28_AMT(BigDecimal r28_AMT) {
			R28_AMT = r28_AMT;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_AMT() {
			return R29_AMT;
		}

		public void setR29_AMT(BigDecimal r29_AMT) {
			R29_AMT = r29_AMT;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_AMT() {
			return R30_AMT;
		}

		public void setR30_AMT(BigDecimal r30_AMT) {
			R30_AMT = r30_AMT;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_AMT() {
			return R31_AMT;
		}

		public void setR31_AMT(BigDecimal r31_AMT) {
			R31_AMT = r31_AMT;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_AMT() {
			return R32_AMT;
		}

		public void setR32_AMT(BigDecimal r32_AMT) {
			R32_AMT = r32_AMT;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_AMT() {
			return R33_AMT;
		}

		public void setR33_AMT(BigDecimal r33_AMT) {
			R33_AMT = r33_AMT;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_AMT() {
			return R34_AMT;
		}

		public void setR34_AMT(BigDecimal r34_AMT) {
			R34_AMT = r34_AMT;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_AMT() {
			return R35_AMT;
		}

		public void setR35_AMT(BigDecimal r35_AMT) {
			R35_AMT = r35_AMT;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_AMT() {
			return R36_AMT;
		}

		public void setR36_AMT(BigDecimal r36_AMT) {
			R36_AMT = r36_AMT;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_AMT() {
			return R37_AMT;
		}

		public void setR37_AMT(BigDecimal r37_AMT) {
			R37_AMT = r37_AMT;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_AMT() {
			return R38_AMT;
		}

		public void setR38_AMT(BigDecimal r38_AMT) {
			R38_AMT = r38_AMT;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_AMT() {
			return R39_AMT;
		}

		public void setR39_AMT(BigDecimal r39_AMT) {
			R39_AMT = r39_AMT;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_AMT() {
			return R40_AMT;
		}

		public void setR40_AMT(BigDecimal r40_AMT) {
			R40_AMT = r40_AMT;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_AMT() {
			return R41_AMT;
		}

		public void setR41_AMT(BigDecimal r41_AMT) {
			R41_AMT = r41_AMT;
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

	public static class Main_Features_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Main_Features_PK() {
		}

		public Main_Features_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Main_Features_PK))
				return false;
			Main_Features_PK that = (Main_Features_PK) o;
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

	public class Main_Features_Detail_Entity {

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

	class Main_FeaturesDetailRowMapper implements RowMapper<Main_Features_Detail_Entity> {

		@Override
		public Main_Features_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Main_Features_Detail_Entity obj = new Main_Features_Detail_Entity();

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

	class Main_FeaturesArchivalDetailRowMapper implements RowMapper<Main_Features_Archival_Detail_Entity> {

		@Override
		public Main_Features_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Main_Features_Archival_Detail_Entity obj = new Main_Features_Archival_Detail_Entity();

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

	public class Main_Features_Archival_Detail_Entity {

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

	public ModelAndView getMain_FeaturesView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("Main_Features View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<Main_Features_Archival_Summary_Entity> T1Master = new ArrayList<>();

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
			List<Main_Features_Summary_Entity> T1Master = new ArrayList<>();
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

		mv.setViewName("BRRS/Main_Features");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getMain_FeaturescurrentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<Main_Features_Archival_Detail_Entity> archivalDetailList;

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

				List<Main_Features_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/Main_Features");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getMain_FeaturesArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Main_Features_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Main_Features_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Main_Features_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Main_Features  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/Main_Features");

		if (acct_number != null) {
			Main_Features_Detail_Entity Main_FeaturesEntity = findByAcctnumber(acct_number);
			if (Main_FeaturesEntity != null && Main_FeaturesEntity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(Main_FeaturesEntity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Main_FeaturesData", Main_FeaturesEntity);
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
			Main_Features_Detail_Entity existing = findByAcctnumber(acctNo);

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

				String sql = "UPDATE BRRS_MAIN_FEATURES_DETAILTABLE " + "SET ACCT_NAME = ?, "
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

							jdbcTemplate.update("BEGIN BRRS_MAIN_FEATURES_SUMMARY_PROCEDURE(?); END;", formattedDate);

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

	public byte[] getMain_FeaturesDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  Main_Features Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getMain_FeaturesDetailNewExcelARCHIVAL(filename, fromdate, todate, currency,
						dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Main_FeaturesDetailsDetail");

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
			List<Main_Features_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Main_Features_Detail_Entity item : reportData) {
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
				logger.info("No data found for Main_Features — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Main_Features Excel", e);
			return new byte[0];
		}
	}

	public byte[] getMain_FeaturesDetailNewExcelARCHIVAL(String filename, String fromdate, String todate,
			String currency, String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Main_Features ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Main_Features Detail NEW");

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
			List<Main_Features_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate,
					version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Main_Features_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for Main_Features — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Main_Features NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getMain_FeaturesExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.Main_Features");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMain_FeaturesARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// Fetch data

		List<Main_Features_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  Main_Features report. Returning empty result.");
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
					Main_Features_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R3
					row = sheet.getRow(2);
					Cell cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR3_AMT() != null) {
						cellC.setCellValue(record.getR3_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R4
					row = sheet.getRow(3);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR4_AMT() != null) {
						cellC.setCellValue(record.getR4_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R5
					row = sheet.getRow(4);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR5_AMT() != null) {
						cellC.setCellValue(record.getR5_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR6_AMT() != null) {
						cellC.setCellValue(record.getR6_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R7
					row = sheet.getRow(6);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR7_AMT() != null) {
						cellC.setCellValue(record.getR7_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR8_AMT() != null) {
						cellC.setCellValue(record.getR8_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR9_AMT() != null) {
						cellC.setCellValue(record.getR9_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR10_AMT() != null) {
						cellC.setCellValue(record.getR10_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}
					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR11_AMT() != null) {
						cellC.setCellValue(record.getR11_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR12_AMT() != null) {
						cellC.setCellValue(record.getR12_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR13_AMT() != null) {
						cellC.setCellValue(record.getR13_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR14_AMT() != null) {
						cellC.setCellValue(record.getR14_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR15_AMT() != null) {
						cellC.setCellValue(record.getR15_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR16_AMT() != null) {
						cellC.setCellValue(record.getR16_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR17_AMT() != null) {
						cellC.setCellValue(record.getR17_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR18_AMT() != null) {
						cellC.setCellValue(record.getR18_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR19_AMT() != null) {
						cellC.setCellValue(record.getR19_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR20_AMT() != null) {
						cellC.setCellValue(record.getR20_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR21_AMT() != null) {
						cellC.setCellValue(record.getR21_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR22_AMT() != null) {
						cellC.setCellValue(record.getR22_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR23_AMT() != null) {
						cellC.setCellValue(record.getR23_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR24_AMT() != null) {
						cellC.setCellValue(record.getR24_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR25_AMT() != null) {
						cellC.setCellValue(record.getR25_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR26_AMT() != null) {
						cellC.setCellValue(record.getR26_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR27_AMT() != null) {
						cellC.setCellValue(record.getR27_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR28_AMT() != null) {
						cellC.setCellValue(record.getR28_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR29_AMT() != null) {
						cellC.setCellValue(record.getR29_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR30_AMT() != null) {
						cellC.setCellValue(record.getR30_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}
					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR31_AMT() != null) {
						cellC.setCellValue(record.getR31_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR32_AMT() != null) {
						cellC.setCellValue(record.getR32_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR33_AMT() != null) {
						cellC.setCellValue(record.getR33_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR34_AMT() != null) {
						cellC.setCellValue(record.getR34_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR35_AMT() != null) {
						cellC.setCellValue(record.getR35_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R36
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR36_AMT() != null) {
						cellC.setCellValue(record.getR36_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R37
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR37_AMT() != null) {
						cellC.setCellValue(record.getR37_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR38_AMT() != null) {
						cellC.setCellValue(record.getR38_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR39_AMT() != null) {
						cellC.setCellValue(record.getR39_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR40_AMT() != null) {
						cellC.setCellValue(record.getR40_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR41_AMT() != null) {
						cellC.setCellValue(record.getR41_AMT().doubleValue());
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

	public byte[] getExcelMain_FeaturesARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<Main_Features_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Main_Features new report. Returning empty result.");
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
					Main_Features_Archival_Summary_Entity record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R3
					row = sheet.getRow(2);
					Cell cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR3_AMT() != null) {
						cellC.setCellValue(record.getR3_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R4
					row = sheet.getRow(3);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR4_AMT() != null) {
						cellC.setCellValue(record.getR4_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R5
					row = sheet.getRow(4);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR5_AMT() != null) {
						cellC.setCellValue(record.getR5_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R6
					row = sheet.getRow(5);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR6_AMT() != null) {
						cellC.setCellValue(record.getR6_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R7
					row = sheet.getRow(6);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR7_AMT() != null) {
						cellC.setCellValue(record.getR7_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R8
					row = sheet.getRow(7);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR8_AMT() != null) {
						cellC.setCellValue(record.getR8_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R9
					row = sheet.getRow(8);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR9_AMT() != null) {
						cellC.setCellValue(record.getR9_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R10
					row = sheet.getRow(9);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR10_AMT() != null) {
						cellC.setCellValue(record.getR10_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}
					// R11
					row = sheet.getRow(10);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR11_AMT() != null) {
						cellC.setCellValue(record.getR11_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R12
					row = sheet.getRow(11);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR12_AMT() != null) {
						cellC.setCellValue(record.getR12_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R13
					row = sheet.getRow(12);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR13_AMT() != null) {
						cellC.setCellValue(record.getR13_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R14
					row = sheet.getRow(13);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR14_AMT() != null) {
						cellC.setCellValue(record.getR14_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R15
					row = sheet.getRow(14);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR15_AMT() != null) {
						cellC.setCellValue(record.getR15_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R16
					row = sheet.getRow(15);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR16_AMT() != null) {
						cellC.setCellValue(record.getR16_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R17
					row = sheet.getRow(16);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR17_AMT() != null) {
						cellC.setCellValue(record.getR17_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R18
					row = sheet.getRow(17);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR18_AMT() != null) {
						cellC.setCellValue(record.getR18_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R19
					row = sheet.getRow(18);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR19_AMT() != null) {
						cellC.setCellValue(record.getR19_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R20
					row = sheet.getRow(19);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR20_AMT() != null) {
						cellC.setCellValue(record.getR20_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R21
					row = sheet.getRow(20);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR21_AMT() != null) {
						cellC.setCellValue(record.getR21_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R22
					row = sheet.getRow(21);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR22_AMT() != null) {
						cellC.setCellValue(record.getR22_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R23
					row = sheet.getRow(22);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR23_AMT() != null) {
						cellC.setCellValue(record.getR23_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R24
					row = sheet.getRow(23);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR24_AMT() != null) {
						cellC.setCellValue(record.getR24_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R25
					row = sheet.getRow(24);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR25_AMT() != null) {
						cellC.setCellValue(record.getR25_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R26
					row = sheet.getRow(25);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR26_AMT() != null) {
						cellC.setCellValue(record.getR26_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R27
					row = sheet.getRow(26);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR27_AMT() != null) {
						cellC.setCellValue(record.getR27_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R28
					row = sheet.getRow(27);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR28_AMT() != null) {
						cellC.setCellValue(record.getR28_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R29
					row = sheet.getRow(28);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR29_AMT() != null) {
						cellC.setCellValue(record.getR29_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R30
					row = sheet.getRow(29);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR30_AMT() != null) {
						cellC.setCellValue(record.getR30_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}
					// R31
					row = sheet.getRow(30);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR31_AMT() != null) {
						cellC.setCellValue(record.getR31_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R32
					row = sheet.getRow(31);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR32_AMT() != null) {
						cellC.setCellValue(record.getR32_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R33
					row = sheet.getRow(32);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR33_AMT() != null) {
						cellC.setCellValue(record.getR33_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R34
					row = sheet.getRow(33);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR34_AMT() != null) {
						cellC.setCellValue(record.getR34_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R35
					row = sheet.getRow(34);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR35_AMT() != null) {
						cellC.setCellValue(record.getR35_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R36
					row = sheet.getRow(35);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR36_AMT() != null) {
						cellC.setCellValue(record.getR36_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R37
					row = sheet.getRow(36);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR37_AMT() != null) {
						cellC.setCellValue(record.getR37_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R38
					row = sheet.getRow(37);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR38_AMT() != null) {
						cellC.setCellValue(record.getR38_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R39
					row = sheet.getRow(38);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR39_AMT() != null) {
						cellC.setCellValue(record.getR39_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R40
					row = sheet.getRow(39);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR40_AMT() != null) {
						cellC.setCellValue(record.getR40_AMT().doubleValue());
					} else {
						cellC.setCellValue(0);
					}

					// R41
					row = sheet.getRow(40);
					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);

					if (record.getR41_AMT() != null) {
						cellC.setCellValue(record.getR41_AMT().doubleValue());
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