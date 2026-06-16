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
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_RECON_OF_FS_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_RECON_OF_FS_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	AuditService auditService;

	// Fetch data by report date
	public List<Recon_Of_FS_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Recon_Of_FSRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getRecon_Of_FSArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Recon_Of_FS_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new Recon_Of_FSArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<Recon_Of_FS_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Recon_Of_FSArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Recon_Of_FS_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new Recon_Of_FSDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Recon_Of_FS_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Recon_Of_FSDetailRowMapper());
	}

// 3. PAGINATION

	public List<Recon_Of_FS_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new Recon_Of_FSDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Recon_Of_FS_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Recon_Of_FSDetailRowMapper());
	}
// 6. BY ACCOUNT NUMBER

	public Recon_Of_FS_Archival_Detail_Entity findArchivalByAcctnumber(String acctNumber, String reportDate,
			String version) {

		String sql = "SELECT * " + "FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL " + "WHERE ACCT_NUMBER = ? "
				+ "AND REPORT_DATE = TO_DATE(?,'DD-MM-YYYY') " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber, reportDate, Integer.parseInt(version) },
				new Recon_Of_FSArchivalDetailRowMapper());
	}

//	public Recon_Of_FS_Detail_Entity findByAcctnumber(String acctNumber) {
//
//		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE ACCT_NUMBER = ?";
//
//		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new Recon_Of_FSDetailRowMapper());
//	}

// 1. GET BY DATE + VERSION
//
//	public List<Recon_Of_FS_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
//			String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL "
//				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
//				new Recon_Of_FSArchivalDetailRowMapper());
//	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

//	public List<Recon_Of_FS_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
//			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
//				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
//				new Recon_Of_FSArchivalDetailRowMapper());
//	}

//For Resubmission
	public Recon_Of_FS_Detail_Entity findBysnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Recon_Of_FSDetailRowMapper());
	}

	public Recon_Of_FS_Detail_Entity findBysno(String sno) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Recon_Of_FSDetailRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	public List<Recon_Of_FS_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Recon_Of_FSArchivalDetailRowMapper());
	}

	public List<Recon_Of_FS_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Recon_Of_FSArchivalDetailRowMapper());
	}
	// ROW MAPPER

	class Recon_Of_FSRowMapper implements RowMapper<Recon_Of_FS_Summary_Entity> {

		@Override
		public Recon_Of_FS_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Recon_Of_FS_Summary_Entity obj = new Recon_Of_FS_Summary_Entity();

			// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_BAL_SHEET_PUB_FS(rs.getBigDecimal("R7_BAL_SHEET_PUB_FS"));
			obj.setR7_UNDER_REG_SOC(rs.getBigDecimal("R7_UNDER_REG_SOC"));

// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_BAL_SHEET_PUB_FS(rs.getBigDecimal("R8_BAL_SHEET_PUB_FS"));
			obj.setR8_UNDER_REG_SOC(rs.getBigDecimal("R8_UNDER_REG_SOC"));

// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_BAL_SHEET_PUB_FS(rs.getBigDecimal("R9_BAL_SHEET_PUB_FS"));
			obj.setR9_UNDER_REG_SOC(rs.getBigDecimal("R9_UNDER_REG_SOC"));

// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_BAL_SHEET_PUB_FS(rs.getBigDecimal("R10_BAL_SHEET_PUB_FS"));
			obj.setR10_UNDER_REG_SOC(rs.getBigDecimal("R10_UNDER_REG_SOC"));

// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_BAL_SHEET_PUB_FS(rs.getBigDecimal("R11_BAL_SHEET_PUB_FS"));
			obj.setR11_UNDER_REG_SOC(rs.getBigDecimal("R11_UNDER_REG_SOC"));

// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_BAL_SHEET_PUB_FS(rs.getBigDecimal("R12_BAL_SHEET_PUB_FS"));
			obj.setR12_UNDER_REG_SOC(rs.getBigDecimal("R12_UNDER_REG_SOC"));

// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_BAL_SHEET_PUB_FS(rs.getBigDecimal("R13_BAL_SHEET_PUB_FS"));
			obj.setR13_UNDER_REG_SOC(rs.getBigDecimal("R13_UNDER_REG_SOC"));

// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_BAL_SHEET_PUB_FS(rs.getBigDecimal("R14_BAL_SHEET_PUB_FS"));
			obj.setR14_UNDER_REG_SOC(rs.getBigDecimal("R14_UNDER_REG_SOC"));

// R15
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_BAL_SHEET_PUB_FS(rs.getBigDecimal("R15_BAL_SHEET_PUB_FS"));
			obj.setR15_UNDER_REG_SOC(rs.getBigDecimal("R15_UNDER_REG_SOC"));

// R16
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_BAL_SHEET_PUB_FS(rs.getBigDecimal("R16_BAL_SHEET_PUB_FS"));
			obj.setR16_UNDER_REG_SOC(rs.getBigDecimal("R16_UNDER_REG_SOC"));

// R17
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_BAL_SHEET_PUB_FS(rs.getBigDecimal("R17_BAL_SHEET_PUB_FS"));
			obj.setR17_UNDER_REG_SOC(rs.getBigDecimal("R17_UNDER_REG_SOC"));

// R18
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_BAL_SHEET_PUB_FS(rs.getBigDecimal("R18_BAL_SHEET_PUB_FS"));
			obj.setR18_UNDER_REG_SOC(rs.getBigDecimal("R18_UNDER_REG_SOC"));

// R19
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_BAL_SHEET_PUB_FS(rs.getBigDecimal("R19_BAL_SHEET_PUB_FS"));
			obj.setR19_UNDER_REG_SOC(rs.getBigDecimal("R19_UNDER_REG_SOC"));

// R20
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_BAL_SHEET_PUB_FS(rs.getBigDecimal("R20_BAL_SHEET_PUB_FS"));
			obj.setR20_UNDER_REG_SOC(rs.getBigDecimal("R20_UNDER_REG_SOC"));

// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_BAL_SHEET_PUB_FS(rs.getBigDecimal("R21_BAL_SHEET_PUB_FS"));
			obj.setR21_UNDER_REG_SOC(rs.getBigDecimal("R21_UNDER_REG_SOC"));

// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_BAL_SHEET_PUB_FS(rs.getBigDecimal("R23_BAL_SHEET_PUB_FS"));
			obj.setR23_UNDER_REG_SOC(rs.getBigDecimal("R23_UNDER_REG_SOC"));

// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_BAL_SHEET_PUB_FS(rs.getBigDecimal("R24_BAL_SHEET_PUB_FS"));
			obj.setR24_UNDER_REG_SOC(rs.getBigDecimal("R24_UNDER_REG_SOC"));

// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_BAL_SHEET_PUB_FS(rs.getBigDecimal("R25_BAL_SHEET_PUB_FS"));
			obj.setR25_UNDER_REG_SOC(rs.getBigDecimal("R25_UNDER_REG_SOC"));

// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_BAL_SHEET_PUB_FS(rs.getBigDecimal("R26_BAL_SHEET_PUB_FS"));
			obj.setR26_UNDER_REG_SOC(rs.getBigDecimal("R26_UNDER_REG_SOC"));

// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_BAL_SHEET_PUB_FS(rs.getBigDecimal("R27_BAL_SHEET_PUB_FS"));
			obj.setR27_UNDER_REG_SOC(rs.getBigDecimal("R27_UNDER_REG_SOC"));

// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_BAL_SHEET_PUB_FS(rs.getBigDecimal("R28_BAL_SHEET_PUB_FS"));
			obj.setR28_UNDER_REG_SOC(rs.getBigDecimal("R28_UNDER_REG_SOC"));

// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_BAL_SHEET_PUB_FS(rs.getBigDecimal("R29_BAL_SHEET_PUB_FS"));
			obj.setR29_UNDER_REG_SOC(rs.getBigDecimal("R29_UNDER_REG_SOC"));

// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_BAL_SHEET_PUB_FS(rs.getBigDecimal("R30_BAL_SHEET_PUB_FS"));
			obj.setR30_UNDER_REG_SOC(rs.getBigDecimal("R30_UNDER_REG_SOC"));

// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_BAL_SHEET_PUB_FS(rs.getBigDecimal("R31_BAL_SHEET_PUB_FS"));
			obj.setR31_UNDER_REG_SOC(rs.getBigDecimal("R31_UNDER_REG_SOC"));

// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_BAL_SHEET_PUB_FS(rs.getBigDecimal("R32_BAL_SHEET_PUB_FS"));
			obj.setR32_UNDER_REG_SOC(rs.getBigDecimal("R32_UNDER_REG_SOC"));

// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_BAL_SHEET_PUB_FS(rs.getBigDecimal("R33_BAL_SHEET_PUB_FS"));
			obj.setR33_UNDER_REG_SOC(rs.getBigDecimal("R33_UNDER_REG_SOC"));

// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_BAL_SHEET_PUB_FS(rs.getBigDecimal("R34_BAL_SHEET_PUB_FS"));
			obj.setR34_UNDER_REG_SOC(rs.getBigDecimal("R34_UNDER_REG_SOC"));

// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_BAL_SHEET_PUB_FS(rs.getBigDecimal("R35_BAL_SHEET_PUB_FS"));
			obj.setR35_UNDER_REG_SOC(rs.getBigDecimal("R35_UNDER_REG_SOC"));

// R36
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_BAL_SHEET_PUB_FS(rs.getBigDecimal("R36_BAL_SHEET_PUB_FS"));
			obj.setR36_UNDER_REG_SOC(rs.getBigDecimal("R36_UNDER_REG_SOC"));

// R38
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_BAL_SHEET_PUB_FS(rs.getBigDecimal("R38_BAL_SHEET_PUB_FS"));
			obj.setR38_UNDER_REG_SOC(rs.getBigDecimal("R38_UNDER_REG_SOC"));

// R39
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_BAL_SHEET_PUB_FS(rs.getBigDecimal("R39_BAL_SHEET_PUB_FS"));
			obj.setR39_UNDER_REG_SOC(rs.getBigDecimal("R39_UNDER_REG_SOC"));

// R40
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_BAL_SHEET_PUB_FS(rs.getBigDecimal("R40_BAL_SHEET_PUB_FS"));
			obj.setR40_UNDER_REG_SOC(rs.getBigDecimal("R40_UNDER_REG_SOC"));

// R41
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_BAL_SHEET_PUB_FS(rs.getBigDecimal("R41_BAL_SHEET_PUB_FS"));
			obj.setR41_UNDER_REG_SOC(rs.getBigDecimal("R41_UNDER_REG_SOC"));

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

	public static class Recon_Of_FS_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R7_PRODUCT")
		private String R7_PRODUCT;
		@Column(name = "R7_BAL_SHEET_PUB_FS")
		private BigDecimal R7_BAL_SHEET_PUB_FS;
		@Column(name = "R7_UNDER_REG_SOC")
		private BigDecimal R7_UNDER_REG_SOC;

		@Column(name = "R8_PRODUCT")
		private String R8_PRODUCT;
		@Column(name = "R8_BAL_SHEET_PUB_FS")
		private BigDecimal R8_BAL_SHEET_PUB_FS;
		@Column(name = "R8_UNDER_REG_SOC")
		private BigDecimal R8_UNDER_REG_SOC;

		@Column(name = "R9_PRODUCT")
		private String R9_PRODUCT;
		@Column(name = "R9_BAL_SHEET_PUB_FS")
		private BigDecimal R9_BAL_SHEET_PUB_FS;
		@Column(name = "R9_UNDER_REG_SOC")
		private BigDecimal R9_UNDER_REG_SOC;

		@Column(name = "R10_PRODUCT")
		private String R10_PRODUCT;
		@Column(name = "R10_BAL_SHEET_PUB_FS")
		private BigDecimal R10_BAL_SHEET_PUB_FS;
		@Column(name = "R10_UNDER_REG_SOC")
		private BigDecimal R10_UNDER_REG_SOC;

		/*
		 * ========================= R11 – R20 =========================
		 */
		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;
		@Column(name = "R11_BAL_SHEET_PUB_FS")
		private BigDecimal R11_BAL_SHEET_PUB_FS;
		@Column(name = "R11_UNDER_REG_SOC")
		private BigDecimal R11_UNDER_REG_SOC;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;
		@Column(name = "R12_BAL_SHEET_PUB_FS")
		private BigDecimal R12_BAL_SHEET_PUB_FS;
		@Column(name = "R12_UNDER_REG_SOC")
		private BigDecimal R12_UNDER_REG_SOC;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;
		@Column(name = "R13_BAL_SHEET_PUB_FS")
		private BigDecimal R13_BAL_SHEET_PUB_FS;
		@Column(name = "R13_UNDER_REG_SOC")
		private BigDecimal R13_UNDER_REG_SOC;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;
		@Column(name = "R14_BAL_SHEET_PUB_FS")
		private BigDecimal R14_BAL_SHEET_PUB_FS;
		@Column(name = "R14_UNDER_REG_SOC")
		private BigDecimal R14_UNDER_REG_SOC;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;
		@Column(name = "R15_BAL_SHEET_PUB_FS")
		private BigDecimal R15_BAL_SHEET_PUB_FS;
		@Column(name = "R15_UNDER_REG_SOC")
		private BigDecimal R15_UNDER_REG_SOC;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;
		@Column(name = "R16_BAL_SHEET_PUB_FS")
		private BigDecimal R16_BAL_SHEET_PUB_FS;
		@Column(name = "R16_UNDER_REG_SOC")
		private BigDecimal R16_UNDER_REG_SOC;

		@Column(name = "R17_PRODUCT")
		private String R17_PRODUCT;
		@Column(name = "R17_BAL_SHEET_PUB_FS")
		private BigDecimal R17_BAL_SHEET_PUB_FS;
		@Column(name = "R17_UNDER_REG_SOC")
		private BigDecimal R17_UNDER_REG_SOC;

		@Column(name = "R18_PRODUCT")
		private String R18_PRODUCT;
		@Column(name = "R18_BAL_SHEET_PUB_FS")
		private BigDecimal R18_BAL_SHEET_PUB_FS;
		@Column(name = "R18_UNDER_REG_SOC")
		private BigDecimal R18_UNDER_REG_SOC;

		@Column(name = "R19_PRODUCT")
		private String R19_PRODUCT;
		@Column(name = "R19_BAL_SHEET_PUB_FS")
		private BigDecimal R19_BAL_SHEET_PUB_FS;
		@Column(name = "R19_UNDER_REG_SOC")
		private BigDecimal R19_UNDER_REG_SOC;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;
		@Column(name = "R20_BAL_SHEET_PUB_FS")
		private BigDecimal R20_BAL_SHEET_PUB_FS;
		@Column(name = "R20_UNDER_REG_SOC")
		private BigDecimal R20_UNDER_REG_SOC;

		/*
		 * ========================= R21 – R41 =========================
		 */
		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;
		@Column(name = "R21_BAL_SHEET_PUB_FS")
		private BigDecimal R21_BAL_SHEET_PUB_FS;
		@Column(name = "R21_UNDER_REG_SOC")
		private BigDecimal R21_UNDER_REG_SOC;

		@Column(name = "R23_PRODUCT")
		private String R23_PRODUCT;
		@Column(name = "R23_BAL_SHEET_PUB_FS")
		private BigDecimal R23_BAL_SHEET_PUB_FS;
		@Column(name = "R23_UNDER_REG_SOC")
		private BigDecimal R23_UNDER_REG_SOC;

		@Column(name = "R24_PRODUCT")
		private String R24_PRODUCT;
		@Column(name = "R24_BAL_SHEET_PUB_FS")
		private BigDecimal R24_BAL_SHEET_PUB_FS;
		@Column(name = "R24_UNDER_REG_SOC")
		private BigDecimal R24_UNDER_REG_SOC;

		@Column(name = "R25_PRODUCT")
		private String R25_PRODUCT;
		@Column(name = "R25_BAL_SHEET_PUB_FS")
		private BigDecimal R25_BAL_SHEET_PUB_FS;
		@Column(name = "R25_UNDER_REG_SOC")
		private BigDecimal R25_UNDER_REG_SOC;

		@Column(name = "R26_PRODUCT")
		private String R26_PRODUCT;
		@Column(name = "R26_BAL_SHEET_PUB_FS")
		private BigDecimal R26_BAL_SHEET_PUB_FS;
		@Column(name = "R26_UNDER_REG_SOC")
		private BigDecimal R26_UNDER_REG_SOC;

		@Column(name = "R27_PRODUCT")
		private String R27_PRODUCT;
		@Column(name = "R27_BAL_SHEET_PUB_FS")
		private BigDecimal R27_BAL_SHEET_PUB_FS;
		@Column(name = "R27_UNDER_REG_SOC")
		private BigDecimal R27_UNDER_REG_SOC;

		@Column(name = "R28_PRODUCT")
		private String R28_PRODUCT;
		@Column(name = "R28_BAL_SHEET_PUB_FS")
		private BigDecimal R28_BAL_SHEET_PUB_FS;
		@Column(name = "R28_UNDER_REG_SOC")
		private BigDecimal R28_UNDER_REG_SOC;

		@Column(name = "R29_PRODUCT")
		private String R29_PRODUCT;
		@Column(name = "R29_BAL_SHEET_PUB_FS")
		private BigDecimal R29_BAL_SHEET_PUB_FS;
		@Column(name = "R29_UNDER_REG_SOC")
		private BigDecimal R29_UNDER_REG_SOC;

		@Column(name = "R30_PRODUCT")
		private String R30_PRODUCT;
		@Column(name = "R30_BAL_SHEET_PUB_FS")
		private BigDecimal R30_BAL_SHEET_PUB_FS;
		@Column(name = "R30_UNDER_REG_SOC")
		private BigDecimal R30_UNDER_REG_SOC;

		@Column(name = "R31_PRODUCT")
		private String R31_PRODUCT;
		@Column(name = "R31_BAL_SHEET_PUB_FS")
		private BigDecimal R31_BAL_SHEET_PUB_FS;
		@Column(name = "R31_UNDER_REG_SOC")
		private BigDecimal R31_UNDER_REG_SOC;

		@Column(name = "R32_PRODUCT")
		private String R32_PRODUCT;
		@Column(name = "R32_BAL_SHEET_PUB_FS")
		private BigDecimal R32_BAL_SHEET_PUB_FS;
		@Column(name = "R32_UNDER_REG_SOC")
		private BigDecimal R32_UNDER_REG_SOC;

		@Column(name = "R33_PRODUCT")
		private String R33_PRODUCT;
		@Column(name = "R33_BAL_SHEET_PUB_FS")
		private BigDecimal R33_BAL_SHEET_PUB_FS;
		@Column(name = "R33_UNDER_REG_SOC")
		private BigDecimal R33_UNDER_REG_SOC;

		@Column(name = "R34_PRODUCT")
		private String R34_PRODUCT;
		@Column(name = "R34_BAL_SHEET_PUB_FS")
		private BigDecimal R34_BAL_SHEET_PUB_FS;
		@Column(name = "R34_UNDER_REG_SOC")
		private BigDecimal R34_UNDER_REG_SOC;

		@Column(name = "R35_PRODUCT")
		private String R35_PRODUCT;
		@Column(name = "R35_BAL_SHEET_PUB_FS")
		private BigDecimal R35_BAL_SHEET_PUB_FS;
		@Column(name = "R35_UNDER_REG_SOC")
		private BigDecimal R35_UNDER_REG_SOC;

		@Column(name = "R36_PRODUCT")
		private String R36_PRODUCT;
		@Column(name = "R36_BAL_SHEET_PUB_FS")
		private BigDecimal R36_BAL_SHEET_PUB_FS;
		@Column(name = "R36_UNDER_REG_SOC")
		private BigDecimal R36_UNDER_REG_SOC;

		@Column(name = "R38_PRODUCT")
		private String R38_PRODUCT;
		@Column(name = "R38_BAL_SHEET_PUB_FS")
		private BigDecimal R38_BAL_SHEET_PUB_FS;
		@Column(name = "R38_UNDER_REG_SOC")
		private BigDecimal R38_UNDER_REG_SOC;

		@Column(name = "R39_PRODUCT")
		private String R39_PRODUCT;
		@Column(name = "R39_BAL_SHEET_PUB_FS")
		private BigDecimal R39_BAL_SHEET_PUB_FS;
		@Column(name = "R39_UNDER_REG_SOC")
		private BigDecimal R39_UNDER_REG_SOC;

		@Column(name = "R40_PRODUCT")
		private String R40_PRODUCT;
		@Column(name = "R40_BAL_SHEET_PUB_FS")
		private BigDecimal R40_BAL_SHEET_PUB_FS;
		@Column(name = "R40_UNDER_REG_SOC")
		private BigDecimal R40_UNDER_REG_SOC;

		@Column(name = "R41_PRODUCT")
		private String R41_PRODUCT;
		@Column(name = "R41_BAL_SHEET_PUB_FS")
		private BigDecimal R41_BAL_SHEET_PUB_FS;
		@Column(name = "R41_UNDER_REG_SOC")
		private BigDecimal R41_UNDER_REG_SOC;

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
			this.REPORT_DATE = REPORT_DATE;
		}

		public String getR7_PRODUCT() {
			return R7_PRODUCT;
		}

		public void setR7_PRODUCT(String r7_PRODUCT) {
			R7_PRODUCT = r7_PRODUCT;
		}

		public BigDecimal getR7_BAL_SHEET_PUB_FS() {
			return R7_BAL_SHEET_PUB_FS;
		}

		public void setR7_BAL_SHEET_PUB_FS(BigDecimal r7_BAL_SHEET_PUB_FS) {
			R7_BAL_SHEET_PUB_FS = r7_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR7_UNDER_REG_SOC() {
			return R7_UNDER_REG_SOC;
		}

		public void setR7_UNDER_REG_SOC(BigDecimal r7_UNDER_REG_SOC) {
			R7_UNDER_REG_SOC = r7_UNDER_REG_SOC;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_BAL_SHEET_PUB_FS() {
			return R8_BAL_SHEET_PUB_FS;
		}

		public void setR8_BAL_SHEET_PUB_FS(BigDecimal r8_BAL_SHEET_PUB_FS) {
			R8_BAL_SHEET_PUB_FS = r8_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR8_UNDER_REG_SOC() {
			return R8_UNDER_REG_SOC;
		}

		public void setR8_UNDER_REG_SOC(BigDecimal r8_UNDER_REG_SOC) {
			R8_UNDER_REG_SOC = r8_UNDER_REG_SOC;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_BAL_SHEET_PUB_FS() {
			return R9_BAL_SHEET_PUB_FS;
		}

		public void setR9_BAL_SHEET_PUB_FS(BigDecimal r9_BAL_SHEET_PUB_FS) {
			R9_BAL_SHEET_PUB_FS = r9_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR9_UNDER_REG_SOC() {
			return R9_UNDER_REG_SOC;
		}

		public void setR9_UNDER_REG_SOC(BigDecimal r9_UNDER_REG_SOC) {
			R9_UNDER_REG_SOC = r9_UNDER_REG_SOC;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_BAL_SHEET_PUB_FS() {
			return R10_BAL_SHEET_PUB_FS;
		}

		public void setR10_BAL_SHEET_PUB_FS(BigDecimal r10_BAL_SHEET_PUB_FS) {
			R10_BAL_SHEET_PUB_FS = r10_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR10_UNDER_REG_SOC() {
			return R10_UNDER_REG_SOC;
		}

		public void setR10_UNDER_REG_SOC(BigDecimal r10_UNDER_REG_SOC) {
			R10_UNDER_REG_SOC = r10_UNDER_REG_SOC;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_BAL_SHEET_PUB_FS() {
			return R11_BAL_SHEET_PUB_FS;
		}

		public void setR11_BAL_SHEET_PUB_FS(BigDecimal r11_BAL_SHEET_PUB_FS) {
			R11_BAL_SHEET_PUB_FS = r11_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR11_UNDER_REG_SOC() {
			return R11_UNDER_REG_SOC;
		}

		public void setR11_UNDER_REG_SOC(BigDecimal r11_UNDER_REG_SOC) {
			R11_UNDER_REG_SOC = r11_UNDER_REG_SOC;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_BAL_SHEET_PUB_FS() {
			return R12_BAL_SHEET_PUB_FS;
		}

		public void setR12_BAL_SHEET_PUB_FS(BigDecimal r12_BAL_SHEET_PUB_FS) {
			R12_BAL_SHEET_PUB_FS = r12_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR12_UNDER_REG_SOC() {
			return R12_UNDER_REG_SOC;
		}

		public void setR12_UNDER_REG_SOC(BigDecimal r12_UNDER_REG_SOC) {
			R12_UNDER_REG_SOC = r12_UNDER_REG_SOC;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_BAL_SHEET_PUB_FS() {
			return R13_BAL_SHEET_PUB_FS;
		}

		public void setR13_BAL_SHEET_PUB_FS(BigDecimal r13_BAL_SHEET_PUB_FS) {
			R13_BAL_SHEET_PUB_FS = r13_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR13_UNDER_REG_SOC() {
			return R13_UNDER_REG_SOC;
		}

		public void setR13_UNDER_REG_SOC(BigDecimal r13_UNDER_REG_SOC) {
			R13_UNDER_REG_SOC = r13_UNDER_REG_SOC;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_BAL_SHEET_PUB_FS() {
			return R14_BAL_SHEET_PUB_FS;
		}

		public void setR14_BAL_SHEET_PUB_FS(BigDecimal r14_BAL_SHEET_PUB_FS) {
			R14_BAL_SHEET_PUB_FS = r14_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR14_UNDER_REG_SOC() {
			return R14_UNDER_REG_SOC;
		}

		public void setR14_UNDER_REG_SOC(BigDecimal r14_UNDER_REG_SOC) {
			R14_UNDER_REG_SOC = r14_UNDER_REG_SOC;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_BAL_SHEET_PUB_FS() {
			return R15_BAL_SHEET_PUB_FS;
		}

		public void setR15_BAL_SHEET_PUB_FS(BigDecimal r15_BAL_SHEET_PUB_FS) {
			R15_BAL_SHEET_PUB_FS = r15_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR15_UNDER_REG_SOC() {
			return R15_UNDER_REG_SOC;
		}

		public void setR15_UNDER_REG_SOC(BigDecimal r15_UNDER_REG_SOC) {
			R15_UNDER_REG_SOC = r15_UNDER_REG_SOC;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_BAL_SHEET_PUB_FS() {
			return R16_BAL_SHEET_PUB_FS;
		}

		public void setR16_BAL_SHEET_PUB_FS(BigDecimal r16_BAL_SHEET_PUB_FS) {
			R16_BAL_SHEET_PUB_FS = r16_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR16_UNDER_REG_SOC() {
			return R16_UNDER_REG_SOC;
		}

		public void setR16_UNDER_REG_SOC(BigDecimal r16_UNDER_REG_SOC) {
			R16_UNDER_REG_SOC = r16_UNDER_REG_SOC;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_BAL_SHEET_PUB_FS() {
			return R17_BAL_SHEET_PUB_FS;
		}

		public void setR17_BAL_SHEET_PUB_FS(BigDecimal r17_BAL_SHEET_PUB_FS) {
			R17_BAL_SHEET_PUB_FS = r17_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR17_UNDER_REG_SOC() {
			return R17_UNDER_REG_SOC;
		}

		public void setR17_UNDER_REG_SOC(BigDecimal r17_UNDER_REG_SOC) {
			R17_UNDER_REG_SOC = r17_UNDER_REG_SOC;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_BAL_SHEET_PUB_FS() {
			return R18_BAL_SHEET_PUB_FS;
		}

		public void setR18_BAL_SHEET_PUB_FS(BigDecimal r18_BAL_SHEET_PUB_FS) {
			R18_BAL_SHEET_PUB_FS = r18_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR18_UNDER_REG_SOC() {
			return R18_UNDER_REG_SOC;
		}

		public void setR18_UNDER_REG_SOC(BigDecimal r18_UNDER_REG_SOC) {
			R18_UNDER_REG_SOC = r18_UNDER_REG_SOC;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_BAL_SHEET_PUB_FS() {
			return R19_BAL_SHEET_PUB_FS;
		}

		public void setR19_BAL_SHEET_PUB_FS(BigDecimal r19_BAL_SHEET_PUB_FS) {
			R19_BAL_SHEET_PUB_FS = r19_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR19_UNDER_REG_SOC() {
			return R19_UNDER_REG_SOC;
		}

		public void setR19_UNDER_REG_SOC(BigDecimal r19_UNDER_REG_SOC) {
			R19_UNDER_REG_SOC = r19_UNDER_REG_SOC;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_BAL_SHEET_PUB_FS() {
			return R20_BAL_SHEET_PUB_FS;
		}

		public void setR20_BAL_SHEET_PUB_FS(BigDecimal r20_BAL_SHEET_PUB_FS) {
			R20_BAL_SHEET_PUB_FS = r20_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR20_UNDER_REG_SOC() {
			return R20_UNDER_REG_SOC;
		}

		public void setR20_UNDER_REG_SOC(BigDecimal r20_UNDER_REG_SOC) {
			R20_UNDER_REG_SOC = r20_UNDER_REG_SOC;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_BAL_SHEET_PUB_FS() {
			return R21_BAL_SHEET_PUB_FS;
		}

		public void setR21_BAL_SHEET_PUB_FS(BigDecimal r21_BAL_SHEET_PUB_FS) {
			R21_BAL_SHEET_PUB_FS = r21_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR21_UNDER_REG_SOC() {
			return R21_UNDER_REG_SOC;
		}

		public void setR21_UNDER_REG_SOC(BigDecimal r21_UNDER_REG_SOC) {
			R21_UNDER_REG_SOC = r21_UNDER_REG_SOC;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_BAL_SHEET_PUB_FS() {
			return R23_BAL_SHEET_PUB_FS;
		}

		public void setR23_BAL_SHEET_PUB_FS(BigDecimal r23_BAL_SHEET_PUB_FS) {
			R23_BAL_SHEET_PUB_FS = r23_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR23_UNDER_REG_SOC() {
			return R23_UNDER_REG_SOC;
		}

		public void setR23_UNDER_REG_SOC(BigDecimal r23_UNDER_REG_SOC) {
			R23_UNDER_REG_SOC = r23_UNDER_REG_SOC;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_BAL_SHEET_PUB_FS() {
			return R24_BAL_SHEET_PUB_FS;
		}

		public void setR24_BAL_SHEET_PUB_FS(BigDecimal r24_BAL_SHEET_PUB_FS) {
			R24_BAL_SHEET_PUB_FS = r24_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR24_UNDER_REG_SOC() {
			return R24_UNDER_REG_SOC;
		}

		public void setR24_UNDER_REG_SOC(BigDecimal r24_UNDER_REG_SOC) {
			R24_UNDER_REG_SOC = r24_UNDER_REG_SOC;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_BAL_SHEET_PUB_FS() {
			return R25_BAL_SHEET_PUB_FS;
		}

		public void setR25_BAL_SHEET_PUB_FS(BigDecimal r25_BAL_SHEET_PUB_FS) {
			R25_BAL_SHEET_PUB_FS = r25_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR25_UNDER_REG_SOC() {
			return R25_UNDER_REG_SOC;
		}

		public void setR25_UNDER_REG_SOC(BigDecimal r25_UNDER_REG_SOC) {
			R25_UNDER_REG_SOC = r25_UNDER_REG_SOC;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_BAL_SHEET_PUB_FS() {
			return R26_BAL_SHEET_PUB_FS;
		}

		public void setR26_BAL_SHEET_PUB_FS(BigDecimal r26_BAL_SHEET_PUB_FS) {
			R26_BAL_SHEET_PUB_FS = r26_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR26_UNDER_REG_SOC() {
			return R26_UNDER_REG_SOC;
		}

		public void setR26_UNDER_REG_SOC(BigDecimal r26_UNDER_REG_SOC) {
			R26_UNDER_REG_SOC = r26_UNDER_REG_SOC;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_BAL_SHEET_PUB_FS() {
			return R27_BAL_SHEET_PUB_FS;
		}

		public void setR27_BAL_SHEET_PUB_FS(BigDecimal r27_BAL_SHEET_PUB_FS) {
			R27_BAL_SHEET_PUB_FS = r27_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR27_UNDER_REG_SOC() {
			return R27_UNDER_REG_SOC;
		}

		public void setR27_UNDER_REG_SOC(BigDecimal r27_UNDER_REG_SOC) {
			R27_UNDER_REG_SOC = r27_UNDER_REG_SOC;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_BAL_SHEET_PUB_FS() {
			return R28_BAL_SHEET_PUB_FS;
		}

		public void setR28_BAL_SHEET_PUB_FS(BigDecimal r28_BAL_SHEET_PUB_FS) {
			R28_BAL_SHEET_PUB_FS = r28_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR28_UNDER_REG_SOC() {
			return R28_UNDER_REG_SOC;
		}

		public void setR28_UNDER_REG_SOC(BigDecimal r28_UNDER_REG_SOC) {
			R28_UNDER_REG_SOC = r28_UNDER_REG_SOC;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_BAL_SHEET_PUB_FS() {
			return R29_BAL_SHEET_PUB_FS;
		}

		public void setR29_BAL_SHEET_PUB_FS(BigDecimal r29_BAL_SHEET_PUB_FS) {
			R29_BAL_SHEET_PUB_FS = r29_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR29_UNDER_REG_SOC() {
			return R29_UNDER_REG_SOC;
		}

		public void setR29_UNDER_REG_SOC(BigDecimal r29_UNDER_REG_SOC) {
			R29_UNDER_REG_SOC = r29_UNDER_REG_SOC;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_BAL_SHEET_PUB_FS() {
			return R30_BAL_SHEET_PUB_FS;
		}

		public void setR30_BAL_SHEET_PUB_FS(BigDecimal r30_BAL_SHEET_PUB_FS) {
			R30_BAL_SHEET_PUB_FS = r30_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR30_UNDER_REG_SOC() {
			return R30_UNDER_REG_SOC;
		}

		public void setR30_UNDER_REG_SOC(BigDecimal r30_UNDER_REG_SOC) {
			R30_UNDER_REG_SOC = r30_UNDER_REG_SOC;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_BAL_SHEET_PUB_FS() {
			return R31_BAL_SHEET_PUB_FS;
		}

		public void setR31_BAL_SHEET_PUB_FS(BigDecimal r31_BAL_SHEET_PUB_FS) {
			R31_BAL_SHEET_PUB_FS = r31_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR31_UNDER_REG_SOC() {
			return R31_UNDER_REG_SOC;
		}

		public void setR31_UNDER_REG_SOC(BigDecimal r31_UNDER_REG_SOC) {
			R31_UNDER_REG_SOC = r31_UNDER_REG_SOC;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_BAL_SHEET_PUB_FS() {
			return R32_BAL_SHEET_PUB_FS;
		}

		public void setR32_BAL_SHEET_PUB_FS(BigDecimal r32_BAL_SHEET_PUB_FS) {
			R32_BAL_SHEET_PUB_FS = r32_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR32_UNDER_REG_SOC() {
			return R32_UNDER_REG_SOC;
		}

		public void setR32_UNDER_REG_SOC(BigDecimal r32_UNDER_REG_SOC) {
			R32_UNDER_REG_SOC = r32_UNDER_REG_SOC;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_BAL_SHEET_PUB_FS() {
			return R33_BAL_SHEET_PUB_FS;
		}

		public void setR33_BAL_SHEET_PUB_FS(BigDecimal r33_BAL_SHEET_PUB_FS) {
			R33_BAL_SHEET_PUB_FS = r33_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR33_UNDER_REG_SOC() {
			return R33_UNDER_REG_SOC;
		}

		public void setR33_UNDER_REG_SOC(BigDecimal r33_UNDER_REG_SOC) {
			R33_UNDER_REG_SOC = r33_UNDER_REG_SOC;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_BAL_SHEET_PUB_FS() {
			return R34_BAL_SHEET_PUB_FS;
		}

		public void setR34_BAL_SHEET_PUB_FS(BigDecimal r34_BAL_SHEET_PUB_FS) {
			R34_BAL_SHEET_PUB_FS = r34_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR34_UNDER_REG_SOC() {
			return R34_UNDER_REG_SOC;
		}

		public void setR34_UNDER_REG_SOC(BigDecimal r34_UNDER_REG_SOC) {
			R34_UNDER_REG_SOC = r34_UNDER_REG_SOC;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_BAL_SHEET_PUB_FS() {
			return R35_BAL_SHEET_PUB_FS;
		}

		public void setR35_BAL_SHEET_PUB_FS(BigDecimal r35_BAL_SHEET_PUB_FS) {
			R35_BAL_SHEET_PUB_FS = r35_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR35_UNDER_REG_SOC() {
			return R35_UNDER_REG_SOC;
		}

		public void setR35_UNDER_REG_SOC(BigDecimal r35_UNDER_REG_SOC) {
			R35_UNDER_REG_SOC = r35_UNDER_REG_SOC;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_BAL_SHEET_PUB_FS() {
			return R36_BAL_SHEET_PUB_FS;
		}

		public void setR36_BAL_SHEET_PUB_FS(BigDecimal r36_BAL_SHEET_PUB_FS) {
			R36_BAL_SHEET_PUB_FS = r36_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR36_UNDER_REG_SOC() {
			return R36_UNDER_REG_SOC;
		}

		public void setR36_UNDER_REG_SOC(BigDecimal r36_UNDER_REG_SOC) {
			R36_UNDER_REG_SOC = r36_UNDER_REG_SOC;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_BAL_SHEET_PUB_FS() {
			return R38_BAL_SHEET_PUB_FS;
		}

		public void setR38_BAL_SHEET_PUB_FS(BigDecimal r38_BAL_SHEET_PUB_FS) {
			R38_BAL_SHEET_PUB_FS = r38_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR38_UNDER_REG_SOC() {
			return R38_UNDER_REG_SOC;
		}

		public void setR38_UNDER_REG_SOC(BigDecimal r38_UNDER_REG_SOC) {
			R38_UNDER_REG_SOC = r38_UNDER_REG_SOC;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_BAL_SHEET_PUB_FS() {
			return R39_BAL_SHEET_PUB_FS;
		}

		public void setR39_BAL_SHEET_PUB_FS(BigDecimal r39_BAL_SHEET_PUB_FS) {
			R39_BAL_SHEET_PUB_FS = r39_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR39_UNDER_REG_SOC() {
			return R39_UNDER_REG_SOC;
		}

		public void setR39_UNDER_REG_SOC(BigDecimal r39_UNDER_REG_SOC) {
			R39_UNDER_REG_SOC = r39_UNDER_REG_SOC;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_BAL_SHEET_PUB_FS() {
			return R40_BAL_SHEET_PUB_FS;
		}

		public void setR40_BAL_SHEET_PUB_FS(BigDecimal r40_BAL_SHEET_PUB_FS) {
			R40_BAL_SHEET_PUB_FS = r40_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR40_UNDER_REG_SOC() {
			return R40_UNDER_REG_SOC;
		}

		public void setR40_UNDER_REG_SOC(BigDecimal r40_UNDER_REG_SOC) {
			R40_UNDER_REG_SOC = r40_UNDER_REG_SOC;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_BAL_SHEET_PUB_FS() {
			return R41_BAL_SHEET_PUB_FS;
		}

		public void setR41_BAL_SHEET_PUB_FS(BigDecimal r41_BAL_SHEET_PUB_FS) {
			R41_BAL_SHEET_PUB_FS = r41_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR41_UNDER_REG_SOC() {
			return R41_UNDER_REG_SOC;
		}

		public void setR41_UNDER_REG_SOC(BigDecimal r41_UNDER_REG_SOC) {
			R41_UNDER_REG_SOC = r41_UNDER_REG_SOC;
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

//ARCHIVAL ROW MAPPER

	class Recon_Of_FSArchivalRowMapper implements RowMapper<Recon_Of_FS_Archival_Summary_Entity> {

		@Override
		public Recon_Of_FS_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Recon_Of_FS_Archival_Summary_Entity obj = new Recon_Of_FS_Archival_Summary_Entity();

// R7
			obj.setR7_PRODUCT(rs.getString("R7_PRODUCT"));
			obj.setR7_BAL_SHEET_PUB_FS(rs.getBigDecimal("R7_BAL_SHEET_PUB_FS"));
			obj.setR7_UNDER_REG_SOC(rs.getBigDecimal("R7_UNDER_REG_SOC"));

// R8
			obj.setR8_PRODUCT(rs.getString("R8_PRODUCT"));
			obj.setR8_BAL_SHEET_PUB_FS(rs.getBigDecimal("R8_BAL_SHEET_PUB_FS"));
			obj.setR8_UNDER_REG_SOC(rs.getBigDecimal("R8_UNDER_REG_SOC"));

// R9
			obj.setR9_PRODUCT(rs.getString("R9_PRODUCT"));
			obj.setR9_BAL_SHEET_PUB_FS(rs.getBigDecimal("R9_BAL_SHEET_PUB_FS"));
			obj.setR9_UNDER_REG_SOC(rs.getBigDecimal("R9_UNDER_REG_SOC"));

// R10
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_BAL_SHEET_PUB_FS(rs.getBigDecimal("R10_BAL_SHEET_PUB_FS"));
			obj.setR10_UNDER_REG_SOC(rs.getBigDecimal("R10_UNDER_REG_SOC"));

// R11
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_BAL_SHEET_PUB_FS(rs.getBigDecimal("R11_BAL_SHEET_PUB_FS"));
			obj.setR11_UNDER_REG_SOC(rs.getBigDecimal("R11_UNDER_REG_SOC"));

// R12
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_BAL_SHEET_PUB_FS(rs.getBigDecimal("R12_BAL_SHEET_PUB_FS"));
			obj.setR12_UNDER_REG_SOC(rs.getBigDecimal("R12_UNDER_REG_SOC"));

// R13
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_BAL_SHEET_PUB_FS(rs.getBigDecimal("R13_BAL_SHEET_PUB_FS"));
			obj.setR13_UNDER_REG_SOC(rs.getBigDecimal("R13_UNDER_REG_SOC"));

// R14
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_BAL_SHEET_PUB_FS(rs.getBigDecimal("R14_BAL_SHEET_PUB_FS"));
			obj.setR14_UNDER_REG_SOC(rs.getBigDecimal("R14_UNDER_REG_SOC"));

// R15
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_BAL_SHEET_PUB_FS(rs.getBigDecimal("R15_BAL_SHEET_PUB_FS"));
			obj.setR15_UNDER_REG_SOC(rs.getBigDecimal("R15_UNDER_REG_SOC"));

// R16
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_BAL_SHEET_PUB_FS(rs.getBigDecimal("R16_BAL_SHEET_PUB_FS"));
			obj.setR16_UNDER_REG_SOC(rs.getBigDecimal("R16_UNDER_REG_SOC"));

// R17
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_BAL_SHEET_PUB_FS(rs.getBigDecimal("R17_BAL_SHEET_PUB_FS"));
			obj.setR17_UNDER_REG_SOC(rs.getBigDecimal("R17_UNDER_REG_SOC"));

// R18
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_BAL_SHEET_PUB_FS(rs.getBigDecimal("R18_BAL_SHEET_PUB_FS"));
			obj.setR18_UNDER_REG_SOC(rs.getBigDecimal("R18_UNDER_REG_SOC"));

// R19
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_BAL_SHEET_PUB_FS(rs.getBigDecimal("R19_BAL_SHEET_PUB_FS"));
			obj.setR19_UNDER_REG_SOC(rs.getBigDecimal("R19_UNDER_REG_SOC"));

// R20
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_BAL_SHEET_PUB_FS(rs.getBigDecimal("R20_BAL_SHEET_PUB_FS"));
			obj.setR20_UNDER_REG_SOC(rs.getBigDecimal("R20_UNDER_REG_SOC"));

// R21
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_BAL_SHEET_PUB_FS(rs.getBigDecimal("R21_BAL_SHEET_PUB_FS"));
			obj.setR21_UNDER_REG_SOC(rs.getBigDecimal("R21_UNDER_REG_SOC"));

// R23
			obj.setR23_PRODUCT(rs.getString("R23_PRODUCT"));
			obj.setR23_BAL_SHEET_PUB_FS(rs.getBigDecimal("R23_BAL_SHEET_PUB_FS"));
			obj.setR23_UNDER_REG_SOC(rs.getBigDecimal("R23_UNDER_REG_SOC"));

// R24
			obj.setR24_PRODUCT(rs.getString("R24_PRODUCT"));
			obj.setR24_BAL_SHEET_PUB_FS(rs.getBigDecimal("R24_BAL_SHEET_PUB_FS"));
			obj.setR24_UNDER_REG_SOC(rs.getBigDecimal("R24_UNDER_REG_SOC"));

// R25
			obj.setR25_PRODUCT(rs.getString("R25_PRODUCT"));
			obj.setR25_BAL_SHEET_PUB_FS(rs.getBigDecimal("R25_BAL_SHEET_PUB_FS"));
			obj.setR25_UNDER_REG_SOC(rs.getBigDecimal("R25_UNDER_REG_SOC"));

// R26
			obj.setR26_PRODUCT(rs.getString("R26_PRODUCT"));
			obj.setR26_BAL_SHEET_PUB_FS(rs.getBigDecimal("R26_BAL_SHEET_PUB_FS"));
			obj.setR26_UNDER_REG_SOC(rs.getBigDecimal("R26_UNDER_REG_SOC"));

// R27
			obj.setR27_PRODUCT(rs.getString("R27_PRODUCT"));
			obj.setR27_BAL_SHEET_PUB_FS(rs.getBigDecimal("R27_BAL_SHEET_PUB_FS"));
			obj.setR27_UNDER_REG_SOC(rs.getBigDecimal("R27_UNDER_REG_SOC"));

// R28
			obj.setR28_PRODUCT(rs.getString("R28_PRODUCT"));
			obj.setR28_BAL_SHEET_PUB_FS(rs.getBigDecimal("R28_BAL_SHEET_PUB_FS"));
			obj.setR28_UNDER_REG_SOC(rs.getBigDecimal("R28_UNDER_REG_SOC"));

// R29
			obj.setR29_PRODUCT(rs.getString("R29_PRODUCT"));
			obj.setR29_BAL_SHEET_PUB_FS(rs.getBigDecimal("R29_BAL_SHEET_PUB_FS"));
			obj.setR29_UNDER_REG_SOC(rs.getBigDecimal("R29_UNDER_REG_SOC"));

// R30
			obj.setR30_PRODUCT(rs.getString("R30_PRODUCT"));
			obj.setR30_BAL_SHEET_PUB_FS(rs.getBigDecimal("R30_BAL_SHEET_PUB_FS"));
			obj.setR30_UNDER_REG_SOC(rs.getBigDecimal("R30_UNDER_REG_SOC"));

// R31
			obj.setR31_PRODUCT(rs.getString("R31_PRODUCT"));
			obj.setR31_BAL_SHEET_PUB_FS(rs.getBigDecimal("R31_BAL_SHEET_PUB_FS"));
			obj.setR31_UNDER_REG_SOC(rs.getBigDecimal("R31_UNDER_REG_SOC"));

// R32
			obj.setR32_PRODUCT(rs.getString("R32_PRODUCT"));
			obj.setR32_BAL_SHEET_PUB_FS(rs.getBigDecimal("R32_BAL_SHEET_PUB_FS"));
			obj.setR32_UNDER_REG_SOC(rs.getBigDecimal("R32_UNDER_REG_SOC"));

// R33
			obj.setR33_PRODUCT(rs.getString("R33_PRODUCT"));
			obj.setR33_BAL_SHEET_PUB_FS(rs.getBigDecimal("R33_BAL_SHEET_PUB_FS"));
			obj.setR33_UNDER_REG_SOC(rs.getBigDecimal("R33_UNDER_REG_SOC"));

// R34
			obj.setR34_PRODUCT(rs.getString("R34_PRODUCT"));
			obj.setR34_BAL_SHEET_PUB_FS(rs.getBigDecimal("R34_BAL_SHEET_PUB_FS"));
			obj.setR34_UNDER_REG_SOC(rs.getBigDecimal("R34_UNDER_REG_SOC"));

// R35
			obj.setR35_PRODUCT(rs.getString("R35_PRODUCT"));
			obj.setR35_BAL_SHEET_PUB_FS(rs.getBigDecimal("R35_BAL_SHEET_PUB_FS"));
			obj.setR35_UNDER_REG_SOC(rs.getBigDecimal("R35_UNDER_REG_SOC"));

// R36
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_BAL_SHEET_PUB_FS(rs.getBigDecimal("R36_BAL_SHEET_PUB_FS"));
			obj.setR36_UNDER_REG_SOC(rs.getBigDecimal("R36_UNDER_REG_SOC"));

// R38
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_BAL_SHEET_PUB_FS(rs.getBigDecimal("R38_BAL_SHEET_PUB_FS"));
			obj.setR38_UNDER_REG_SOC(rs.getBigDecimal("R38_UNDER_REG_SOC"));

// R39
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_BAL_SHEET_PUB_FS(rs.getBigDecimal("R39_BAL_SHEET_PUB_FS"));
			obj.setR39_UNDER_REG_SOC(rs.getBigDecimal("R39_UNDER_REG_SOC"));

// R40
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_BAL_SHEET_PUB_FS(rs.getBigDecimal("R40_BAL_SHEET_PUB_FS"));
			obj.setR40_UNDER_REG_SOC(rs.getBigDecimal("R40_UNDER_REG_SOC"));

// R41
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_BAL_SHEET_PUB_FS(rs.getBigDecimal("R41_BAL_SHEET_PUB_FS"));
			obj.setR41_UNDER_REG_SOC(rs.getBigDecimal("R41_UNDER_REG_SOC"));

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

	@IdClass(Recon_Of_FS_PK.class)
	public class Recon_Of_FS_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R7_PRODUCT")
		private String R7_PRODUCT;
		@Column(name = "R7_BAL_SHEET_PUB_FS")
		private BigDecimal R7_BAL_SHEET_PUB_FS;
		@Column(name = "R7_UNDER_REG_SOC")
		private BigDecimal R7_UNDER_REG_SOC;

		@Column(name = "R8_PRODUCT")
		private String R8_PRODUCT;
		@Column(name = "R8_BAL_SHEET_PUB_FS")
		private BigDecimal R8_BAL_SHEET_PUB_FS;
		@Column(name = "R8_UNDER_REG_SOC")
		private BigDecimal R8_UNDER_REG_SOC;

		@Column(name = "R9_PRODUCT")
		private String R9_PRODUCT;
		@Column(name = "R9_BAL_SHEET_PUB_FS")
		private BigDecimal R9_BAL_SHEET_PUB_FS;
		@Column(name = "R9_UNDER_REG_SOC")
		private BigDecimal R9_UNDER_REG_SOC;

		@Column(name = "R10_PRODUCT")
		private String R10_PRODUCT;
		@Column(name = "R10_BAL_SHEET_PUB_FS")
		private BigDecimal R10_BAL_SHEET_PUB_FS;
		@Column(name = "R10_UNDER_REG_SOC")
		private BigDecimal R10_UNDER_REG_SOC;

		/*
		 * ========================= R11 – R20 =========================
		 */
		@Column(name = "R11_PRODUCT")
		private String R11_PRODUCT;
		@Column(name = "R11_BAL_SHEET_PUB_FS")
		private BigDecimal R11_BAL_SHEET_PUB_FS;
		@Column(name = "R11_UNDER_REG_SOC")
		private BigDecimal R11_UNDER_REG_SOC;

		@Column(name = "R12_PRODUCT")
		private String R12_PRODUCT;
		@Column(name = "R12_BAL_SHEET_PUB_FS")
		private BigDecimal R12_BAL_SHEET_PUB_FS;
		@Column(name = "R12_UNDER_REG_SOC")
		private BigDecimal R12_UNDER_REG_SOC;

		@Column(name = "R13_PRODUCT")
		private String R13_PRODUCT;
		@Column(name = "R13_BAL_SHEET_PUB_FS")
		private BigDecimal R13_BAL_SHEET_PUB_FS;
		@Column(name = "R13_UNDER_REG_SOC")
		private BigDecimal R13_UNDER_REG_SOC;

		@Column(name = "R14_PRODUCT")
		private String R14_PRODUCT;
		@Column(name = "R14_BAL_SHEET_PUB_FS")
		private BigDecimal R14_BAL_SHEET_PUB_FS;
		@Column(name = "R14_UNDER_REG_SOC")
		private BigDecimal R14_UNDER_REG_SOC;

		@Column(name = "R15_PRODUCT")
		private String R15_PRODUCT;
		@Column(name = "R15_BAL_SHEET_PUB_FS")
		private BigDecimal R15_BAL_SHEET_PUB_FS;
		@Column(name = "R15_UNDER_REG_SOC")
		private BigDecimal R15_UNDER_REG_SOC;

		@Column(name = "R16_PRODUCT")
		private String R16_PRODUCT;
		@Column(name = "R16_BAL_SHEET_PUB_FS")
		private BigDecimal R16_BAL_SHEET_PUB_FS;
		@Column(name = "R16_UNDER_REG_SOC")
		private BigDecimal R16_UNDER_REG_SOC;

		@Column(name = "R17_PRODUCT")
		private String R17_PRODUCT;
		@Column(name = "R17_BAL_SHEET_PUB_FS")
		private BigDecimal R17_BAL_SHEET_PUB_FS;
		@Column(name = "R17_UNDER_REG_SOC")
		private BigDecimal R17_UNDER_REG_SOC;

		@Column(name = "R18_PRODUCT")
		private String R18_PRODUCT;
		@Column(name = "R18_BAL_SHEET_PUB_FS")
		private BigDecimal R18_BAL_SHEET_PUB_FS;
		@Column(name = "R18_UNDER_REG_SOC")
		private BigDecimal R18_UNDER_REG_SOC;

		@Column(name = "R19_PRODUCT")
		private String R19_PRODUCT;
		@Column(name = "R19_BAL_SHEET_PUB_FS")
		private BigDecimal R19_BAL_SHEET_PUB_FS;
		@Column(name = "R19_UNDER_REG_SOC")
		private BigDecimal R19_UNDER_REG_SOC;

		@Column(name = "R20_PRODUCT")
		private String R20_PRODUCT;
		@Column(name = "R20_BAL_SHEET_PUB_FS")
		private BigDecimal R20_BAL_SHEET_PUB_FS;
		@Column(name = "R20_UNDER_REG_SOC")
		private BigDecimal R20_UNDER_REG_SOC;

		/*
		 * ========================= R21 – R41 =========================
		 */
		@Column(name = "R21_PRODUCT")
		private String R21_PRODUCT;
		@Column(name = "R21_BAL_SHEET_PUB_FS")
		private BigDecimal R21_BAL_SHEET_PUB_FS;
		@Column(name = "R21_UNDER_REG_SOC")
		private BigDecimal R21_UNDER_REG_SOC;

		@Column(name = "R23_PRODUCT")
		private String R23_PRODUCT;
		@Column(name = "R23_BAL_SHEET_PUB_FS")
		private BigDecimal R23_BAL_SHEET_PUB_FS;
		@Column(name = "R23_UNDER_REG_SOC")
		private BigDecimal R23_UNDER_REG_SOC;

		@Column(name = "R24_PRODUCT")
		private String R24_PRODUCT;
		@Column(name = "R24_BAL_SHEET_PUB_FS")
		private BigDecimal R24_BAL_SHEET_PUB_FS;
		@Column(name = "R24_UNDER_REG_SOC")
		private BigDecimal R24_UNDER_REG_SOC;

		@Column(name = "R25_PRODUCT")
		private String R25_PRODUCT;
		@Column(name = "R25_BAL_SHEET_PUB_FS")
		private BigDecimal R25_BAL_SHEET_PUB_FS;
		@Column(name = "R25_UNDER_REG_SOC")
		private BigDecimal R25_UNDER_REG_SOC;

		@Column(name = "R26_PRODUCT")
		private String R26_PRODUCT;
		@Column(name = "R26_BAL_SHEET_PUB_FS")
		private BigDecimal R26_BAL_SHEET_PUB_FS;
		@Column(name = "R26_UNDER_REG_SOC")
		private BigDecimal R26_UNDER_REG_SOC;

		@Column(name = "R27_PRODUCT")
		private String R27_PRODUCT;
		@Column(name = "R27_BAL_SHEET_PUB_FS")
		private BigDecimal R27_BAL_SHEET_PUB_FS;
		@Column(name = "R27_UNDER_REG_SOC")
		private BigDecimal R27_UNDER_REG_SOC;

		@Column(name = "R28_PRODUCT")
		private String R28_PRODUCT;
		@Column(name = "R28_BAL_SHEET_PUB_FS")
		private BigDecimal R28_BAL_SHEET_PUB_FS;
		@Column(name = "R28_UNDER_REG_SOC")
		private BigDecimal R28_UNDER_REG_SOC;

		@Column(name = "R29_PRODUCT")
		private String R29_PRODUCT;
		@Column(name = "R29_BAL_SHEET_PUB_FS")
		private BigDecimal R29_BAL_SHEET_PUB_FS;
		@Column(name = "R29_UNDER_REG_SOC")
		private BigDecimal R29_UNDER_REG_SOC;

		@Column(name = "R30_PRODUCT")
		private String R30_PRODUCT;
		@Column(name = "R30_BAL_SHEET_PUB_FS")
		private BigDecimal R30_BAL_SHEET_PUB_FS;
		@Column(name = "R30_UNDER_REG_SOC")
		private BigDecimal R30_UNDER_REG_SOC;

		@Column(name = "R31_PRODUCT")
		private String R31_PRODUCT;
		@Column(name = "R31_BAL_SHEET_PUB_FS")
		private BigDecimal R31_BAL_SHEET_PUB_FS;
		@Column(name = "R31_UNDER_REG_SOC")
		private BigDecimal R31_UNDER_REG_SOC;

		@Column(name = "R32_PRODUCT")
		private String R32_PRODUCT;
		@Column(name = "R32_BAL_SHEET_PUB_FS")
		private BigDecimal R32_BAL_SHEET_PUB_FS;
		@Column(name = "R32_UNDER_REG_SOC")
		private BigDecimal R32_UNDER_REG_SOC;

		@Column(name = "R33_PRODUCT")
		private String R33_PRODUCT;
		@Column(name = "R33_BAL_SHEET_PUB_FS")
		private BigDecimal R33_BAL_SHEET_PUB_FS;
		@Column(name = "R33_UNDER_REG_SOC")
		private BigDecimal R33_UNDER_REG_SOC;

		@Column(name = "R34_PRODUCT")
		private String R34_PRODUCT;
		@Column(name = "R34_BAL_SHEET_PUB_FS")
		private BigDecimal R34_BAL_SHEET_PUB_FS;
		@Column(name = "R34_UNDER_REG_SOC")
		private BigDecimal R34_UNDER_REG_SOC;

		@Column(name = "R35_PRODUCT")
		private String R35_PRODUCT;
		@Column(name = "R35_BAL_SHEET_PUB_FS")
		private BigDecimal R35_BAL_SHEET_PUB_FS;
		@Column(name = "R35_UNDER_REG_SOC")
		private BigDecimal R35_UNDER_REG_SOC;

		@Column(name = "R36_PRODUCT")
		private String R36_PRODUCT;
		@Column(name = "R36_BAL_SHEET_PUB_FS")
		private BigDecimal R36_BAL_SHEET_PUB_FS;
		@Column(name = "R36_UNDER_REG_SOC")
		private BigDecimal R36_UNDER_REG_SOC;

		@Column(name = "R38_PRODUCT")
		private String R38_PRODUCT;
		@Column(name = "R38_BAL_SHEET_PUB_FS")
		private BigDecimal R38_BAL_SHEET_PUB_FS;
		@Column(name = "R38_UNDER_REG_SOC")
		private BigDecimal R38_UNDER_REG_SOC;

		@Column(name = "R39_PRODUCT")
		private String R39_PRODUCT;
		@Column(name = "R39_BAL_SHEET_PUB_FS")
		private BigDecimal R39_BAL_SHEET_PUB_FS;
		@Column(name = "R39_UNDER_REG_SOC")
		private BigDecimal R39_UNDER_REG_SOC;

		@Column(name = "R40_PRODUCT")
		private String R40_PRODUCT;
		@Column(name = "R40_BAL_SHEET_PUB_FS")
		private BigDecimal R40_BAL_SHEET_PUB_FS;
		@Column(name = "R40_UNDER_REG_SOC")
		private BigDecimal R40_UNDER_REG_SOC;

		@Column(name = "R41_PRODUCT")
		private String R41_PRODUCT;
		@Column(name = "R41_BAL_SHEET_PUB_FS")
		private BigDecimal R41_BAL_SHEET_PUB_FS;
		@Column(name = "R41_UNDER_REG_SOC")
		private BigDecimal R41_UNDER_REG_SOC;
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

		public BigDecimal getR7_BAL_SHEET_PUB_FS() {
			return R7_BAL_SHEET_PUB_FS;
		}

		public void setR7_BAL_SHEET_PUB_FS(BigDecimal r7_BAL_SHEET_PUB_FS) {
			R7_BAL_SHEET_PUB_FS = r7_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR7_UNDER_REG_SOC() {
			return R7_UNDER_REG_SOC;
		}

		public void setR7_UNDER_REG_SOC(BigDecimal r7_UNDER_REG_SOC) {
			R7_UNDER_REG_SOC = r7_UNDER_REG_SOC;
		}

		public String getR8_PRODUCT() {
			return R8_PRODUCT;
		}

		public void setR8_PRODUCT(String r8_PRODUCT) {
			R8_PRODUCT = r8_PRODUCT;
		}

		public BigDecimal getR8_BAL_SHEET_PUB_FS() {
			return R8_BAL_SHEET_PUB_FS;
		}

		public void setR8_BAL_SHEET_PUB_FS(BigDecimal r8_BAL_SHEET_PUB_FS) {
			R8_BAL_SHEET_PUB_FS = r8_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR8_UNDER_REG_SOC() {
			return R8_UNDER_REG_SOC;
		}

		public void setR8_UNDER_REG_SOC(BigDecimal r8_UNDER_REG_SOC) {
			R8_UNDER_REG_SOC = r8_UNDER_REG_SOC;
		}

		public String getR9_PRODUCT() {
			return R9_PRODUCT;
		}

		public void setR9_PRODUCT(String r9_PRODUCT) {
			R9_PRODUCT = r9_PRODUCT;
		}

		public BigDecimal getR9_BAL_SHEET_PUB_FS() {
			return R9_BAL_SHEET_PUB_FS;
		}

		public void setR9_BAL_SHEET_PUB_FS(BigDecimal r9_BAL_SHEET_PUB_FS) {
			R9_BAL_SHEET_PUB_FS = r9_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR9_UNDER_REG_SOC() {
			return R9_UNDER_REG_SOC;
		}

		public void setR9_UNDER_REG_SOC(BigDecimal r9_UNDER_REG_SOC) {
			R9_UNDER_REG_SOC = r9_UNDER_REG_SOC;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_BAL_SHEET_PUB_FS() {
			return R10_BAL_SHEET_PUB_FS;
		}

		public void setR10_BAL_SHEET_PUB_FS(BigDecimal r10_BAL_SHEET_PUB_FS) {
			R10_BAL_SHEET_PUB_FS = r10_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR10_UNDER_REG_SOC() {
			return R10_UNDER_REG_SOC;
		}

		public void setR10_UNDER_REG_SOC(BigDecimal r10_UNDER_REG_SOC) {
			R10_UNDER_REG_SOC = r10_UNDER_REG_SOC;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_BAL_SHEET_PUB_FS() {
			return R11_BAL_SHEET_PUB_FS;
		}

		public void setR11_BAL_SHEET_PUB_FS(BigDecimal r11_BAL_SHEET_PUB_FS) {
			R11_BAL_SHEET_PUB_FS = r11_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR11_UNDER_REG_SOC() {
			return R11_UNDER_REG_SOC;
		}

		public void setR11_UNDER_REG_SOC(BigDecimal r11_UNDER_REG_SOC) {
			R11_UNDER_REG_SOC = r11_UNDER_REG_SOC;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_BAL_SHEET_PUB_FS() {
			return R12_BAL_SHEET_PUB_FS;
		}

		public void setR12_BAL_SHEET_PUB_FS(BigDecimal r12_BAL_SHEET_PUB_FS) {
			R12_BAL_SHEET_PUB_FS = r12_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR12_UNDER_REG_SOC() {
			return R12_UNDER_REG_SOC;
		}

		public void setR12_UNDER_REG_SOC(BigDecimal r12_UNDER_REG_SOC) {
			R12_UNDER_REG_SOC = r12_UNDER_REG_SOC;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_BAL_SHEET_PUB_FS() {
			return R13_BAL_SHEET_PUB_FS;
		}

		public void setR13_BAL_SHEET_PUB_FS(BigDecimal r13_BAL_SHEET_PUB_FS) {
			R13_BAL_SHEET_PUB_FS = r13_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR13_UNDER_REG_SOC() {
			return R13_UNDER_REG_SOC;
		}

		public void setR13_UNDER_REG_SOC(BigDecimal r13_UNDER_REG_SOC) {
			R13_UNDER_REG_SOC = r13_UNDER_REG_SOC;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_BAL_SHEET_PUB_FS() {
			return R14_BAL_SHEET_PUB_FS;
		}

		public void setR14_BAL_SHEET_PUB_FS(BigDecimal r14_BAL_SHEET_PUB_FS) {
			R14_BAL_SHEET_PUB_FS = r14_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR14_UNDER_REG_SOC() {
			return R14_UNDER_REG_SOC;
		}

		public void setR14_UNDER_REG_SOC(BigDecimal r14_UNDER_REG_SOC) {
			R14_UNDER_REG_SOC = r14_UNDER_REG_SOC;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_BAL_SHEET_PUB_FS() {
			return R15_BAL_SHEET_PUB_FS;
		}

		public void setR15_BAL_SHEET_PUB_FS(BigDecimal r15_BAL_SHEET_PUB_FS) {
			R15_BAL_SHEET_PUB_FS = r15_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR15_UNDER_REG_SOC() {
			return R15_UNDER_REG_SOC;
		}

		public void setR15_UNDER_REG_SOC(BigDecimal r15_UNDER_REG_SOC) {
			R15_UNDER_REG_SOC = r15_UNDER_REG_SOC;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_BAL_SHEET_PUB_FS() {
			return R16_BAL_SHEET_PUB_FS;
		}

		public void setR16_BAL_SHEET_PUB_FS(BigDecimal r16_BAL_SHEET_PUB_FS) {
			R16_BAL_SHEET_PUB_FS = r16_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR16_UNDER_REG_SOC() {
			return R16_UNDER_REG_SOC;
		}

		public void setR16_UNDER_REG_SOC(BigDecimal r16_UNDER_REG_SOC) {
			R16_UNDER_REG_SOC = r16_UNDER_REG_SOC;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_BAL_SHEET_PUB_FS() {
			return R17_BAL_SHEET_PUB_FS;
		}

		public void setR17_BAL_SHEET_PUB_FS(BigDecimal r17_BAL_SHEET_PUB_FS) {
			R17_BAL_SHEET_PUB_FS = r17_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR17_UNDER_REG_SOC() {
			return R17_UNDER_REG_SOC;
		}

		public void setR17_UNDER_REG_SOC(BigDecimal r17_UNDER_REG_SOC) {
			R17_UNDER_REG_SOC = r17_UNDER_REG_SOC;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_BAL_SHEET_PUB_FS() {
			return R18_BAL_SHEET_PUB_FS;
		}

		public void setR18_BAL_SHEET_PUB_FS(BigDecimal r18_BAL_SHEET_PUB_FS) {
			R18_BAL_SHEET_PUB_FS = r18_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR18_UNDER_REG_SOC() {
			return R18_UNDER_REG_SOC;
		}

		public void setR18_UNDER_REG_SOC(BigDecimal r18_UNDER_REG_SOC) {
			R18_UNDER_REG_SOC = r18_UNDER_REG_SOC;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_BAL_SHEET_PUB_FS() {
			return R19_BAL_SHEET_PUB_FS;
		}

		public void setR19_BAL_SHEET_PUB_FS(BigDecimal r19_BAL_SHEET_PUB_FS) {
			R19_BAL_SHEET_PUB_FS = r19_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR19_UNDER_REG_SOC() {
			return R19_UNDER_REG_SOC;
		}

		public void setR19_UNDER_REG_SOC(BigDecimal r19_UNDER_REG_SOC) {
			R19_UNDER_REG_SOC = r19_UNDER_REG_SOC;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_BAL_SHEET_PUB_FS() {
			return R20_BAL_SHEET_PUB_FS;
		}

		public void setR20_BAL_SHEET_PUB_FS(BigDecimal r20_BAL_SHEET_PUB_FS) {
			R20_BAL_SHEET_PUB_FS = r20_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR20_UNDER_REG_SOC() {
			return R20_UNDER_REG_SOC;
		}

		public void setR20_UNDER_REG_SOC(BigDecimal r20_UNDER_REG_SOC) {
			R20_UNDER_REG_SOC = r20_UNDER_REG_SOC;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_BAL_SHEET_PUB_FS() {
			return R21_BAL_SHEET_PUB_FS;
		}

		public void setR21_BAL_SHEET_PUB_FS(BigDecimal r21_BAL_SHEET_PUB_FS) {
			R21_BAL_SHEET_PUB_FS = r21_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR21_UNDER_REG_SOC() {
			return R21_UNDER_REG_SOC;
		}

		public void setR21_UNDER_REG_SOC(BigDecimal r21_UNDER_REG_SOC) {
			R21_UNDER_REG_SOC = r21_UNDER_REG_SOC;
		}

		public String getR23_PRODUCT() {
			return R23_PRODUCT;
		}

		public void setR23_PRODUCT(String r23_PRODUCT) {
			R23_PRODUCT = r23_PRODUCT;
		}

		public BigDecimal getR23_BAL_SHEET_PUB_FS() {
			return R23_BAL_SHEET_PUB_FS;
		}

		public void setR23_BAL_SHEET_PUB_FS(BigDecimal r23_BAL_SHEET_PUB_FS) {
			R23_BAL_SHEET_PUB_FS = r23_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR23_UNDER_REG_SOC() {
			return R23_UNDER_REG_SOC;
		}

		public void setR23_UNDER_REG_SOC(BigDecimal r23_UNDER_REG_SOC) {
			R23_UNDER_REG_SOC = r23_UNDER_REG_SOC;
		}

		public String getR24_PRODUCT() {
			return R24_PRODUCT;
		}

		public void setR24_PRODUCT(String r24_PRODUCT) {
			R24_PRODUCT = r24_PRODUCT;
		}

		public BigDecimal getR24_BAL_SHEET_PUB_FS() {
			return R24_BAL_SHEET_PUB_FS;
		}

		public void setR24_BAL_SHEET_PUB_FS(BigDecimal r24_BAL_SHEET_PUB_FS) {
			R24_BAL_SHEET_PUB_FS = r24_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR24_UNDER_REG_SOC() {
			return R24_UNDER_REG_SOC;
		}

		public void setR24_UNDER_REG_SOC(BigDecimal r24_UNDER_REG_SOC) {
			R24_UNDER_REG_SOC = r24_UNDER_REG_SOC;
		}

		public String getR25_PRODUCT() {
			return R25_PRODUCT;
		}

		public void setR25_PRODUCT(String r25_PRODUCT) {
			R25_PRODUCT = r25_PRODUCT;
		}

		public BigDecimal getR25_BAL_SHEET_PUB_FS() {
			return R25_BAL_SHEET_PUB_FS;
		}

		public void setR25_BAL_SHEET_PUB_FS(BigDecimal r25_BAL_SHEET_PUB_FS) {
			R25_BAL_SHEET_PUB_FS = r25_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR25_UNDER_REG_SOC() {
			return R25_UNDER_REG_SOC;
		}

		public void setR25_UNDER_REG_SOC(BigDecimal r25_UNDER_REG_SOC) {
			R25_UNDER_REG_SOC = r25_UNDER_REG_SOC;
		}

		public String getR26_PRODUCT() {
			return R26_PRODUCT;
		}

		public void setR26_PRODUCT(String r26_PRODUCT) {
			R26_PRODUCT = r26_PRODUCT;
		}

		public BigDecimal getR26_BAL_SHEET_PUB_FS() {
			return R26_BAL_SHEET_PUB_FS;
		}

		public void setR26_BAL_SHEET_PUB_FS(BigDecimal r26_BAL_SHEET_PUB_FS) {
			R26_BAL_SHEET_PUB_FS = r26_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR26_UNDER_REG_SOC() {
			return R26_UNDER_REG_SOC;
		}

		public void setR26_UNDER_REG_SOC(BigDecimal r26_UNDER_REG_SOC) {
			R26_UNDER_REG_SOC = r26_UNDER_REG_SOC;
		}

		public String getR27_PRODUCT() {
			return R27_PRODUCT;
		}

		public void setR27_PRODUCT(String r27_PRODUCT) {
			R27_PRODUCT = r27_PRODUCT;
		}

		public BigDecimal getR27_BAL_SHEET_PUB_FS() {
			return R27_BAL_SHEET_PUB_FS;
		}

		public void setR27_BAL_SHEET_PUB_FS(BigDecimal r27_BAL_SHEET_PUB_FS) {
			R27_BAL_SHEET_PUB_FS = r27_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR27_UNDER_REG_SOC() {
			return R27_UNDER_REG_SOC;
		}

		public void setR27_UNDER_REG_SOC(BigDecimal r27_UNDER_REG_SOC) {
			R27_UNDER_REG_SOC = r27_UNDER_REG_SOC;
		}

		public String getR28_PRODUCT() {
			return R28_PRODUCT;
		}

		public void setR28_PRODUCT(String r28_PRODUCT) {
			R28_PRODUCT = r28_PRODUCT;
		}

		public BigDecimal getR28_BAL_SHEET_PUB_FS() {
			return R28_BAL_SHEET_PUB_FS;
		}

		public void setR28_BAL_SHEET_PUB_FS(BigDecimal r28_BAL_SHEET_PUB_FS) {
			R28_BAL_SHEET_PUB_FS = r28_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR28_UNDER_REG_SOC() {
			return R28_UNDER_REG_SOC;
		}

		public void setR28_UNDER_REG_SOC(BigDecimal r28_UNDER_REG_SOC) {
			R28_UNDER_REG_SOC = r28_UNDER_REG_SOC;
		}

		public String getR29_PRODUCT() {
			return R29_PRODUCT;
		}

		public void setR29_PRODUCT(String r29_PRODUCT) {
			R29_PRODUCT = r29_PRODUCT;
		}

		public BigDecimal getR29_BAL_SHEET_PUB_FS() {
			return R29_BAL_SHEET_PUB_FS;
		}

		public void setR29_BAL_SHEET_PUB_FS(BigDecimal r29_BAL_SHEET_PUB_FS) {
			R29_BAL_SHEET_PUB_FS = r29_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR29_UNDER_REG_SOC() {
			return R29_UNDER_REG_SOC;
		}

		public void setR29_UNDER_REG_SOC(BigDecimal r29_UNDER_REG_SOC) {
			R29_UNDER_REG_SOC = r29_UNDER_REG_SOC;
		}

		public String getR30_PRODUCT() {
			return R30_PRODUCT;
		}

		public void setR30_PRODUCT(String r30_PRODUCT) {
			R30_PRODUCT = r30_PRODUCT;
		}

		public BigDecimal getR30_BAL_SHEET_PUB_FS() {
			return R30_BAL_SHEET_PUB_FS;
		}

		public void setR30_BAL_SHEET_PUB_FS(BigDecimal r30_BAL_SHEET_PUB_FS) {
			R30_BAL_SHEET_PUB_FS = r30_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR30_UNDER_REG_SOC() {
			return R30_UNDER_REG_SOC;
		}

		public void setR30_UNDER_REG_SOC(BigDecimal r30_UNDER_REG_SOC) {
			R30_UNDER_REG_SOC = r30_UNDER_REG_SOC;
		}

		public String getR31_PRODUCT() {
			return R31_PRODUCT;
		}

		public void setR31_PRODUCT(String r31_PRODUCT) {
			R31_PRODUCT = r31_PRODUCT;
		}

		public BigDecimal getR31_BAL_SHEET_PUB_FS() {
			return R31_BAL_SHEET_PUB_FS;
		}

		public void setR31_BAL_SHEET_PUB_FS(BigDecimal r31_BAL_SHEET_PUB_FS) {
			R31_BAL_SHEET_PUB_FS = r31_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR31_UNDER_REG_SOC() {
			return R31_UNDER_REG_SOC;
		}

		public void setR31_UNDER_REG_SOC(BigDecimal r31_UNDER_REG_SOC) {
			R31_UNDER_REG_SOC = r31_UNDER_REG_SOC;
		}

		public String getR32_PRODUCT() {
			return R32_PRODUCT;
		}

		public void setR32_PRODUCT(String r32_PRODUCT) {
			R32_PRODUCT = r32_PRODUCT;
		}

		public BigDecimal getR32_BAL_SHEET_PUB_FS() {
			return R32_BAL_SHEET_PUB_FS;
		}

		public void setR32_BAL_SHEET_PUB_FS(BigDecimal r32_BAL_SHEET_PUB_FS) {
			R32_BAL_SHEET_PUB_FS = r32_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR32_UNDER_REG_SOC() {
			return R32_UNDER_REG_SOC;
		}

		public void setR32_UNDER_REG_SOC(BigDecimal r32_UNDER_REG_SOC) {
			R32_UNDER_REG_SOC = r32_UNDER_REG_SOC;
		}

		public String getR33_PRODUCT() {
			return R33_PRODUCT;
		}

		public void setR33_PRODUCT(String r33_PRODUCT) {
			R33_PRODUCT = r33_PRODUCT;
		}

		public BigDecimal getR33_BAL_SHEET_PUB_FS() {
			return R33_BAL_SHEET_PUB_FS;
		}

		public void setR33_BAL_SHEET_PUB_FS(BigDecimal r33_BAL_SHEET_PUB_FS) {
			R33_BAL_SHEET_PUB_FS = r33_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR33_UNDER_REG_SOC() {
			return R33_UNDER_REG_SOC;
		}

		public void setR33_UNDER_REG_SOC(BigDecimal r33_UNDER_REG_SOC) {
			R33_UNDER_REG_SOC = r33_UNDER_REG_SOC;
		}

		public String getR34_PRODUCT() {
			return R34_PRODUCT;
		}

		public void setR34_PRODUCT(String r34_PRODUCT) {
			R34_PRODUCT = r34_PRODUCT;
		}

		public BigDecimal getR34_BAL_SHEET_PUB_FS() {
			return R34_BAL_SHEET_PUB_FS;
		}

		public void setR34_BAL_SHEET_PUB_FS(BigDecimal r34_BAL_SHEET_PUB_FS) {
			R34_BAL_SHEET_PUB_FS = r34_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR34_UNDER_REG_SOC() {
			return R34_UNDER_REG_SOC;
		}

		public void setR34_UNDER_REG_SOC(BigDecimal r34_UNDER_REG_SOC) {
			R34_UNDER_REG_SOC = r34_UNDER_REG_SOC;
		}

		public String getR35_PRODUCT() {
			return R35_PRODUCT;
		}

		public void setR35_PRODUCT(String r35_PRODUCT) {
			R35_PRODUCT = r35_PRODUCT;
		}

		public BigDecimal getR35_BAL_SHEET_PUB_FS() {
			return R35_BAL_SHEET_PUB_FS;
		}

		public void setR35_BAL_SHEET_PUB_FS(BigDecimal r35_BAL_SHEET_PUB_FS) {
			R35_BAL_SHEET_PUB_FS = r35_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR35_UNDER_REG_SOC() {
			return R35_UNDER_REG_SOC;
		}

		public void setR35_UNDER_REG_SOC(BigDecimal r35_UNDER_REG_SOC) {
			R35_UNDER_REG_SOC = r35_UNDER_REG_SOC;
		}

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_BAL_SHEET_PUB_FS() {
			return R36_BAL_SHEET_PUB_FS;
		}

		public void setR36_BAL_SHEET_PUB_FS(BigDecimal r36_BAL_SHEET_PUB_FS) {
			R36_BAL_SHEET_PUB_FS = r36_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR36_UNDER_REG_SOC() {
			return R36_UNDER_REG_SOC;
		}

		public void setR36_UNDER_REG_SOC(BigDecimal r36_UNDER_REG_SOC) {
			R36_UNDER_REG_SOC = r36_UNDER_REG_SOC;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_BAL_SHEET_PUB_FS() {
			return R38_BAL_SHEET_PUB_FS;
		}

		public void setR38_BAL_SHEET_PUB_FS(BigDecimal r38_BAL_SHEET_PUB_FS) {
			R38_BAL_SHEET_PUB_FS = r38_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR38_UNDER_REG_SOC() {
			return R38_UNDER_REG_SOC;
		}

		public void setR38_UNDER_REG_SOC(BigDecimal r38_UNDER_REG_SOC) {
			R38_UNDER_REG_SOC = r38_UNDER_REG_SOC;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_BAL_SHEET_PUB_FS() {
			return R39_BAL_SHEET_PUB_FS;
		}

		public void setR39_BAL_SHEET_PUB_FS(BigDecimal r39_BAL_SHEET_PUB_FS) {
			R39_BAL_SHEET_PUB_FS = r39_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR39_UNDER_REG_SOC() {
			return R39_UNDER_REG_SOC;
		}

		public void setR39_UNDER_REG_SOC(BigDecimal r39_UNDER_REG_SOC) {
			R39_UNDER_REG_SOC = r39_UNDER_REG_SOC;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_BAL_SHEET_PUB_FS() {
			return R40_BAL_SHEET_PUB_FS;
		}

		public void setR40_BAL_SHEET_PUB_FS(BigDecimal r40_BAL_SHEET_PUB_FS) {
			R40_BAL_SHEET_PUB_FS = r40_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR40_UNDER_REG_SOC() {
			return R40_UNDER_REG_SOC;
		}

		public void setR40_UNDER_REG_SOC(BigDecimal r40_UNDER_REG_SOC) {
			R40_UNDER_REG_SOC = r40_UNDER_REG_SOC;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_BAL_SHEET_PUB_FS() {
			return R41_BAL_SHEET_PUB_FS;
		}

		public void setR41_BAL_SHEET_PUB_FS(BigDecimal r41_BAL_SHEET_PUB_FS) {
			R41_BAL_SHEET_PUB_FS = r41_BAL_SHEET_PUB_FS;
		}

		public BigDecimal getR41_UNDER_REG_SOC() {
			return R41_UNDER_REG_SOC;
		}

		public void setR41_UNDER_REG_SOC(BigDecimal r41_UNDER_REG_SOC) {
			R41_UNDER_REG_SOC = r41_UNDER_REG_SOC;
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

	public static class Recon_Of_FS_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Recon_Of_FS_PK() {
		}

		public Recon_Of_FS_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Recon_Of_FS_PK))
				return false;
			Recon_Of_FS_PK that = (Recon_Of_FS_PK) o;
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

	public class Recon_Of_FS_Detail_Entity {
		private Long sno;
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

		@Column(name = "REPORT_LABEL_1")
		private String reportLabel1;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

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

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportLabel1() {
			return reportLabel1;
		}

		public void setReportLabel1(String reportLabel1) {
			this.reportLabel1 = reportLabel1;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
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

	class Recon_Of_FSDetailRowMapper implements RowMapper<Recon_Of_FS_Detail_Entity> {

		@Override
		public Recon_Of_FS_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Recon_Of_FS_Detail_Entity obj = new Recon_Of_FS_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportLabel1(rs.getString("REPORT_LABEL_1"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
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

	class Recon_Of_FSArchivalDetailRowMapper implements RowMapper<Recon_Of_FS_Archival_Detail_Entity> {

		@Override
		public Recon_Of_FS_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Recon_Of_FS_Archival_Detail_Entity obj = new Recon_Of_FS_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportLabel1(rs.getString("REPORT_LABEL_1"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
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

	public class Recon_Of_FS_Archival_Detail_Entity {
		private Long sno;
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

		@Column(name = "REPORT_LABEL_1")
		private String reportLabel1;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

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

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReportLabel1() {
			return reportLabel1;
		}

		public void setReportLabel1(String reportLabel1) {
			this.reportLabel1 = reportLabel1;
		}

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
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

	public ModelAndView getBRRS_Recon_Of_FS_View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("Recon_Of_FS View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<Recon_Of_FS_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival(dt, version);

				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		// NORMAL MODE
		else {

			List<Recon_Of_FS_Summary_Entity> T1Master = new ArrayList<>();

			try {

				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}

		mv.setViewName("BRRS/Recon_Of_FS");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getBRRS_Recon_Of_FScurrentDtl(String reportId, String fromdate, String todate, String currency,
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

			// ARCHIVAL / RESUB MODE
			if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

				System.out.println(type + " DETAIL MODE");

				List<Recon_Of_FS_Archival_Detail_Entity> detailList;

				if (reportLabel != null && reportAddlCriteria1 != null) {

					detailList = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);

				} else {

					detailList = getArchivalDetaildatabydateList(parsedDate);
				}

				mv.addObject("reportdetails", detailList);
				mv.addObject("reportmaster12", detailList);

				System.out.println(type + " DETAIL COUNT: " + detailList.size());
			}

			// CURRENT MODE
			else {

				List<Recon_Of_FS_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/Recon_Of_FS");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getRecon_Of_FSArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Recon_Of_FS_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Recon_Of_FS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Recon_Of_FS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Recon_Of_FS  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/Recon_Of_FS");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				Recon_Of_FS_Detail_Entity RECON_OF_FSEntity = findBysnoArch(SNO);
				if (RECON_OF_FSEntity != null && RECON_OF_FSEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(RECON_OF_FSEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("RECON_OF_FSData", RECON_OF_FSEntity);
			} else {
				Recon_Of_FS_Detail_Entity RECON_OF_FSEntity = findBysno(SNO);
				if (RECON_OF_FSEntity != null && RECON_OF_FSEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(RECON_OF_FSEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("RECON_OF_FSData", RECON_OF_FSEntity);
			}
		}
		mv.addObject("type", type);
		mv.addObject("displaymode", "edit");
		mv.addObject("formmode", formMode != null ? formMode : "edit");
		return mv;
	}

	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

		try {

			String Sno = request.getParameter("sno");

			String acctBalanceInpula = request.getParameter("acctBalanceInpula");

			String averageStr = request.getParameter("average");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			Recon_Of_FS_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBysnoArch(Sno);
			} else {
				existing = findBysno(Sno);
			}
			Recon_Of_FS_Detail_Entity oldcopy = new Recon_Of_FS_Detail_Entity();
			BeanUtils.copyProperties(existing, oldcopy);

			if (existing == null) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found for update.");
			}

			boolean isChanged = false;

			// Update Name
			if (acctName != null && !acctName.isEmpty()) {

				if (existing.getAcctName() == null || !existing.getAcctName().equals(acctName)) {

					existing.setAcctName(acctName);

					isChanged = true;
				}
			}

			// Update Balance
			if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

				BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

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
			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				if (type == "RESUB" || type.equals("RESUB")) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?, " + // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_RECON_OF_FS_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?, "
							+ // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Recon OF FS Archival Screen",
							"BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Recon OF FS Screen",
							"BRRS_RECON_OF_FS_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_RECON_OF_FS_Procudure(reportDateStr, type, entry);

				if ((type == "RESUB" || type.equals("RESUB")) && (entry == "NO" || entry.equals("NO"))) {
					return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
				}
				return ResponseEntity.ok("Record updated successfully!");
			} else {
				return ResponseEntity.ok("No changes were made.");
			}

		}

		catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {
		try {
			Run_RECON_OF_FS_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_RECON_OF_FS_Procudure(String reportDateStr, String type, String entry) {

		String formattedDate;
		try {
			formattedDate = new SimpleDateFormat("dd-MM-yyyy")
					.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));
		} catch (Exception e) {
			System.out.println("Error parsing date. Post-commit logic aborted.");
			e.printStackTrace();
			return;
		}

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

			@Override
			public void afterCommit() {
				try {
					boolean isResubNoEntry = "RESUB".equals(type) && "NO".equals(entry);
					boolean shouldExecuteProcedure = !"RESUB".equals(type) || isResubNoEntry;

					if (isResubNoEntry) {
						String bdsql = "DELETE FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_RECON_OF_FS_DETAILTABLE "
								+ " (SNO, GL_CODE, GLSH_CODE, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_LABEL_1,REPORT_ADDL_CRITERIA_2, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, GL_CODE, GLSH_CODE, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_LABEL_1,REPORT_ADDL_CRITERIA_2, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_RECON_OF_FS_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_RECON_OF_FS_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_RECON_OF_FS_ARCHIVALTABLE_SUMMARY ( "
								+ "R7_PRODUCT, R7_BAL_SHEET_PUB_FS, R7_UNDER_REG_SOC, "
								+ "R8_PRODUCT, R8_BAL_SHEET_PUB_FS, R8_UNDER_REG_SOC, "
								+ "R9_PRODUCT, R9_BAL_SHEET_PUB_FS, R9_UNDER_REG_SOC, "
								+ "R10_PRODUCT, R10_BAL_SHEET_PUB_FS, R10_UNDER_REG_SOC, "
								+ "R11_PRODUCT, R11_BAL_SHEET_PUB_FS, R11_UNDER_REG_SOC, "
								+ "R12_PRODUCT, R12_BAL_SHEET_PUB_FS, R12_UNDER_REG_SOC, "
								+ "R13_PRODUCT, R13_BAL_SHEET_PUB_FS, R13_UNDER_REG_SOC, "
								+ "R14_PRODUCT, R14_BAL_SHEET_PUB_FS, R14_UNDER_REG_SOC, "
								+ "R15_PRODUCT, R15_BAL_SHEET_PUB_FS, R15_UNDER_REG_SOC, "
								+ "R16_PRODUCT, R16_BAL_SHEET_PUB_FS, R16_UNDER_REG_SOC, "
								+ "R17_PRODUCT, R17_BAL_SHEET_PUB_FS, R17_UNDER_REG_SOC, "
								+ "R18_PRODUCT, R18_BAL_SHEET_PUB_FS, R18_UNDER_REG_SOC, "
								+ "R19_PRODUCT, R19_BAL_SHEET_PUB_FS, R19_UNDER_REG_SOC, "
								+ "R20_PRODUCT, R20_BAL_SHEET_PUB_FS, R20_UNDER_REG_SOC, "
								+ "R21_PRODUCT, R21_BAL_SHEET_PUB_FS, R21_UNDER_REG_SOC, "
								+ "R23_PRODUCT, R23_BAL_SHEET_PUB_FS, R23_UNDER_REG_SOC, "
								+ "R24_PRODUCT, R24_BAL_SHEET_PUB_FS, R24_UNDER_REG_SOC, "
								+ "R25_PRODUCT, R25_BAL_SHEET_PUB_FS, R25_UNDER_REG_SOC, "
								+ "R26_PRODUCT, R26_BAL_SHEET_PUB_FS, R26_UNDER_REG_SOC, "
								+ "R27_PRODUCT, R27_BAL_SHEET_PUB_FS, R27_UNDER_REG_SOC, "
								+ "R28_PRODUCT, R28_BAL_SHEET_PUB_FS, R28_UNDER_REG_SOC, "
								+ "R29_PRODUCT, R29_BAL_SHEET_PUB_FS, R29_UNDER_REG_SOC, "
								+ "R30_PRODUCT, R30_BAL_SHEET_PUB_FS, R30_UNDER_REG_SOC, "
								+ "R31_PRODUCT, R31_BAL_SHEET_PUB_FS, R31_UNDER_REG_SOC, "
								+ "R32_PRODUCT, R32_BAL_SHEET_PUB_FS, R32_UNDER_REG_SOC, "
								+ "R33_PRODUCT, R33_BAL_SHEET_PUB_FS, R33_UNDER_REG_SOC, "
								+ "R34_PRODUCT, R34_BAL_SHEET_PUB_FS, R34_UNDER_REG_SOC, "
								+ "R35_PRODUCT, R35_BAL_SHEET_PUB_FS, R35_UNDER_REG_SOC, "
								+ "R36_PRODUCT, R36_BAL_SHEET_PUB_FS, R36_UNDER_REG_SOC, "
								+ "R38_PRODUCT, R38_BAL_SHEET_PUB_FS, R38_UNDER_REG_SOC, "
								+ "R39_PRODUCT, R39_BAL_SHEET_PUB_FS, R39_UNDER_REG_SOC, "
								+ "R40_PRODUCT, R40_BAL_SHEET_PUB_FS, R40_UNDER_REG_SOC, "
								+ "R41_PRODUCT, R41_BAL_SHEET_PUB_FS, R41_UNDER_REG_SOC, "
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
								+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE ) "

								+ "SELECT " + "R7_PRODUCT, R7_BAL_SHEET_PUB_FS, R7_UNDER_REG_SOC, "
								+ "R8_PRODUCT, R8_BAL_SHEET_PUB_FS, R8_UNDER_REG_SOC, "
								+ "R9_PRODUCT, R9_BAL_SHEET_PUB_FS, R9_UNDER_REG_SOC, "
								+ "R10_PRODUCT, R10_BAL_SHEET_PUB_FS, R10_UNDER_REG_SOC, "
								+ "R11_PRODUCT, R11_BAL_SHEET_PUB_FS, R11_UNDER_REG_SOC, "
								+ "R12_PRODUCT, R12_BAL_SHEET_PUB_FS, R12_UNDER_REG_SOC, "
								+ "R13_PRODUCT, R13_BAL_SHEET_PUB_FS, R13_UNDER_REG_SOC, "
								+ "R14_PRODUCT, R14_BAL_SHEET_PUB_FS, R14_UNDER_REG_SOC, "
								+ "R15_PRODUCT, R15_BAL_SHEET_PUB_FS, R15_UNDER_REG_SOC, "
								+ "R16_PRODUCT, R16_BAL_SHEET_PUB_FS, R16_UNDER_REG_SOC, "
								+ "R17_PRODUCT, R17_BAL_SHEET_PUB_FS, R17_UNDER_REG_SOC, "
								+ "R18_PRODUCT, R18_BAL_SHEET_PUB_FS, R18_UNDER_REG_SOC, "
								+ "R19_PRODUCT, R19_BAL_SHEET_PUB_FS, R19_UNDER_REG_SOC, "
								+ "R20_PRODUCT, R20_BAL_SHEET_PUB_FS, R20_UNDER_REG_SOC, "
								+ "R21_PRODUCT, R21_BAL_SHEET_PUB_FS, R21_UNDER_REG_SOC, "
								+ "R23_PRODUCT, R23_BAL_SHEET_PUB_FS, R23_UNDER_REG_SOC, "
								+ "R24_PRODUCT, R24_BAL_SHEET_PUB_FS, R24_UNDER_REG_SOC, "
								+ "R25_PRODUCT, R25_BAL_SHEET_PUB_FS, R25_UNDER_REG_SOC, "
								+ "R26_PRODUCT, R26_BAL_SHEET_PUB_FS, R26_UNDER_REG_SOC, "
								+ "R27_PRODUCT, R27_BAL_SHEET_PUB_FS, R27_UNDER_REG_SOC, "
								+ "R28_PRODUCT, R28_BAL_SHEET_PUB_FS, R28_UNDER_REG_SOC, "
								+ "R29_PRODUCT, R29_BAL_SHEET_PUB_FS, R29_UNDER_REG_SOC, "
								+ "R30_PRODUCT, R30_BAL_SHEET_PUB_FS, R30_UNDER_REG_SOC, "
								+ "R31_PRODUCT, R31_BAL_SHEET_PUB_FS, R31_UNDER_REG_SOC, "
								+ "R32_PRODUCT, R32_BAL_SHEET_PUB_FS, R32_UNDER_REG_SOC, "
								+ "R33_PRODUCT, R33_BAL_SHEET_PUB_FS, R33_UNDER_REG_SOC, "
								+ "R34_PRODUCT, R34_BAL_SHEET_PUB_FS, R34_UNDER_REG_SOC, "
								+ "R35_PRODUCT, R35_BAL_SHEET_PUB_FS, R35_UNDER_REG_SOC, "
								+ "R36_PRODUCT, R36_BAL_SHEET_PUB_FS, R36_UNDER_REG_SOC, "
								+ "R38_PRODUCT, R38_BAL_SHEET_PUB_FS, R38_UNDER_REG_SOC, "
								+ "R39_PRODUCT, R39_BAL_SHEET_PUB_FS, R39_UNDER_REG_SOC, "
								+ "R40_PRODUCT, R40_BAL_SHEET_PUB_FS, R40_UNDER_REG_SOC, "
								+ "R41_PRODUCT, R41_BAL_SHEET_PUB_FS, R41_UNDER_REG_SOC, "
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, "
								+ "ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE " + "FROM BRRS_RECON_OF_FS_SUMMARYTABLE "
								+ "WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_RECON_OF_FS_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] BRRS_Recon_Of_FSDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  Recon_Of_FS Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getRecon_Of_FSDetailNewExcelARCHIVAL(filename, fromdate, todate, currency,
						dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Recon_Of_FSDetailsDetail");

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
			List<Recon_Of_FS_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Recon_Of_FS_Detail_Entity item : reportData) {
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
				logger.info("No data found for Recon_Of_FS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Recon_Of_FS Excel", e);
			return new byte[0];
		}
	}

	public byte[] getRecon_Of_FSDetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Recon_Of_FS ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Recon_Of_FS Detail NEW");

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
			List<Recon_Of_FS_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Recon_Of_FS_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for Recon_Of_FS — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Recon_Of_FS NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getRecon_Of_FSExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.Recon_Of_FS");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelRecon_Of_FSARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<Recon_Of_FS_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  Recon_Of_FS report. Returning empty result.");
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
					Recon_Of_FS_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(6);
					// ================= R7 =================

					Cell R7cell2 = row.createCell(1);
					if (record.getR7_BAL_SHEET_PUB_FS() != null) {
						R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					Cell R7cell3 = row.createCell(2);
					if (record.getR7_UNDER_REG_SOC() != null) {
						R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
						R7cell3.setCellStyle(numberStyle);
					} else {
						R7cell3.setCellValue("");
						R7cell3.setCellStyle(textStyle);
					}

					// ================= R8 =================
					row = sheet.getRow(7);
					Cell R8cell2 = row.createCell(1);
					if (record.getR8_BAL_SHEET_PUB_FS() != null) {
						R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					Cell R8cell3 = row.createCell(2);
					if (record.getR8_UNDER_REG_SOC() != null) {
						R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
						R8cell3.setCellStyle(numberStyle);
					} else {
						R8cell3.setCellValue("");
						R8cell3.setCellStyle(textStyle);
					}

					// ================= R9 =================
					row = sheet.getRow(8);
					Cell R9cell2 = row.createCell(1);
					if (record.getR9_BAL_SHEET_PUB_FS() != null) {
						R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					Cell R9cell3 = row.createCell(2);
					if (record.getR9_UNDER_REG_SOC() != null) {
						R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}

					// ================= R10 =================
					row = sheet.getRow(9);
					Cell R10cell2 = row.createCell(1);
					if (record.getR10_BAL_SHEET_PUB_FS() != null) {
						R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(2);
					if (record.getR10_UNDER_REG_SOC() != null) {
						R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// ================= R11 =================
					row = sheet.getRow(10);
					Cell R11cell2 = row.createCell(1);
					if (record.getR11_BAL_SHEET_PUB_FS() != null) {
						R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					Cell R11cell3 = row.createCell(2);
					if (record.getR11_UNDER_REG_SOC() != null) {
						R11cell3.setCellValue(record.getR11_UNDER_REG_SOC().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// ================= R12 =================
					row = sheet.getRow(11);
					Cell R12cell2 = row.createCell(1);
					if (record.getR12_BAL_SHEET_PUB_FS() != null) {
						R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					Cell R12cell3 = row.createCell(2);
					if (record.getR12_UNDER_REG_SOC() != null) {
						R12cell3.setCellValue(record.getR12_UNDER_REG_SOC().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// ================= R13 =================
					row = sheet.getRow(12);
					Cell R13cell2 = row.createCell(1);
					if (record.getR13_BAL_SHEET_PUB_FS() != null) {
						R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					Cell R13cell3 = row.createCell(2);
					if (record.getR13_UNDER_REG_SOC() != null) {
						R13cell3.setCellValue(record.getR13_UNDER_REG_SOC().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// ================= R14 =================
					row = sheet.getRow(13);
					Cell R14cell2 = row.createCell(1);
					if (record.getR14_BAL_SHEET_PUB_FS() != null) {
						R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}
					Cell R14cell3 = row.createCell(2);
					if (record.getR14_UNDER_REG_SOC() != null) {
						R14cell3.setCellValue(record.getR14_UNDER_REG_SOC().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R15 =================
					row = sheet.getRow(14);
					Cell R15cell2 = row.createCell(1);
					if (record.getR15_BAL_SHEET_PUB_FS() != null) {
						R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					Cell R15cell3 = row.createCell(2);
					if (record.getR15_UNDER_REG_SOC() != null) {
						R15cell3.setCellValue(record.getR15_UNDER_REG_SOC().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// ================= R16 =================
					row = sheet.getRow(15);
					Cell R16cell2 = row.createCell(1);
					if (record.getR16_BAL_SHEET_PUB_FS() != null) {
						R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					Cell R16cell3 = row.createCell(2);
					if (record.getR16_UNDER_REG_SOC() != null) {
						R16cell3.setCellValue(record.getR16_UNDER_REG_SOC().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================= R17 =================
					row = sheet.getRow(16);
					Cell R17cell2 = row.createCell(1);
					if (record.getR17_BAL_SHEET_PUB_FS() != null) {
						R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}
					Cell R17cell3 = row.createCell(2);
					if (record.getR17_UNDER_REG_SOC() != null) {
						R17cell3.setCellValue(record.getR17_UNDER_REG_SOC().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================= R18 =================
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(1);
					if (record.getR18_BAL_SHEET_PUB_FS() != null) {
						R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}
					Cell R18cell3 = row.createCell(2);
					if (record.getR18_UNDER_REG_SOC() != null) {
						R18cell3.setCellValue(record.getR18_UNDER_REG_SOC().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

					// ================= R19 =================
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(1);
					if (record.getR19_BAL_SHEET_PUB_FS() != null) {
						R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}
					Cell R19cell3 = row.createCell(2);
					if (record.getR19_UNDER_REG_SOC() != null) {
						R19cell3.setCellValue(record.getR19_UNDER_REG_SOC().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================= R20 =================
					row = sheet.getRow(19);
					Cell R20cell2 = row.createCell(1);
					if (record.getR20_BAL_SHEET_PUB_FS() != null) {
						R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}
					Cell R20cell3 = row.createCell(2);
					if (record.getR20_UNDER_REG_SOC() != null) {
						R20cell3.setCellValue(record.getR20_UNDER_REG_SOC().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);
					Cell R21cell2 = row.createCell(1);
					if (record.getR21_BAL_SHEET_PUB_FS() != null) {
						R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					Cell R21cell3 = row.createCell(2);
					if (record.getR21_UNDER_REG_SOC() != null) {
						R21cell3.setCellValue(record.getR21_UNDER_REG_SOC().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);
					Cell R23cell2 = row.createCell(1);
					if (record.getR23_BAL_SHEET_PUB_FS() != null) {
						R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					Cell R23cell3 = row.createCell(2);
					if (record.getR23_UNDER_REG_SOC() != null) {
						R23cell3.setCellValue(record.getR23_UNDER_REG_SOC().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);
					Cell R24cell2 = row.createCell(1);
					if (record.getR24_BAL_SHEET_PUB_FS() != null) {
						R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					Cell R24cell3 = row.createCell(2);
					if (record.getR24_UNDER_REG_SOC() != null) {
						R24cell3.setCellValue(record.getR24_UNDER_REG_SOC().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);
					Cell R25cell2 = row.createCell(1);
					if (record.getR25_BAL_SHEET_PUB_FS() != null) {
						R25cell2.setCellValue(record.getR25_BAL_SHEET_PUB_FS().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					Cell R25cell3 = row.createCell(2);
					if (record.getR25_UNDER_REG_SOC() != null) {
						R25cell3.setCellValue(record.getR25_UNDER_REG_SOC().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);
					Cell R26cell2 = row.createCell(1);
					if (record.getR26_BAL_SHEET_PUB_FS() != null) {
						R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					Cell R26cell3 = row.createCell(2);
					if (record.getR26_UNDER_REG_SOC() != null) {
						R26cell3.setCellValue(record.getR26_UNDER_REG_SOC().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);
					Cell R27cell2 = row.createCell(1);
					if (record.getR27_BAL_SHEET_PUB_FS() != null) {
						R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					Cell R27cell3 = row.createCell(2);
					if (record.getR27_UNDER_REG_SOC() != null) {
						R27cell3.setCellValue(record.getR27_UNDER_REG_SOC().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// ================= R28 =================
					row = sheet.getRow(27);
					Cell R28cell2 = row.createCell(1);
					if (record.getR28_BAL_SHEET_PUB_FS() != null) {
						R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
					Cell R28cell3 = row.createCell(2);
					if (record.getR28_UNDER_REG_SOC() != null) {
						R28cell3.setCellValue(record.getR28_UNDER_REG_SOC().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ================= R29 =================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(1);
					if (record.getR29_BAL_SHEET_PUB_FS() != null) {
						R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
					Cell R29cell3 = row.createCell(2);
					if (record.getR29_UNDER_REG_SOC() != null) {
						R29cell3.setCellValue(record.getR29_UNDER_REG_SOC().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================= R30 =================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(1);
					if (record.getR30_BAL_SHEET_PUB_FS() != null) {
						R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
					Cell R30cell3 = row.createCell(2);
					if (record.getR30_UNDER_REG_SOC() != null) {
						R30cell3.setCellValue(record.getR30_UNDER_REG_SOC().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================= R31 =================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(1);
					if (record.getR31_BAL_SHEET_PUB_FS() != null) {
						R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
					Cell R31cell3 = row.createCell(2);
					if (record.getR31_UNDER_REG_SOC() != null) {
						R31cell3.setCellValue(record.getR31_UNDER_REG_SOC().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}

					// ================= R32 =================
					row = sheet.getRow(31);
					Cell R32cell2 = row.createCell(1);
					if (record.getR32_BAL_SHEET_PUB_FS() != null) {
						R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
					Cell R32cell3 = row.createCell(2);
					if (record.getR32_UNDER_REG_SOC() != null) {
						R32cell3.setCellValue(record.getR32_UNDER_REG_SOC().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}

					// ================= R33 =================
					row = sheet.getRow(32);
					Cell R33cell2 = row.createCell(1);
					if (record.getR33_BAL_SHEET_PUB_FS() != null) {
						R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					Cell R33cell3 = row.createCell(2);
					if (record.getR33_UNDER_REG_SOC() != null) {
						R33cell3.setCellValue(record.getR33_UNDER_REG_SOC().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================= R34 =================
					row = sheet.getRow(33);
					Cell R34cell2 = row.createCell(1);
					if (record.getR34_BAL_SHEET_PUB_FS() != null) {
						R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					Cell R34cell3 = row.createCell(2);
					if (record.getR34_UNDER_REG_SOC() != null) {
						R34cell3.setCellValue(record.getR34_UNDER_REG_SOC().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// ================= R35 =================
					row = sheet.getRow(34);
					Cell R35cell2 = row.createCell(1);
					if (record.getR35_BAL_SHEET_PUB_FS() != null) {
						R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					Cell R35cell3 = row.createCell(2);
					if (record.getR35_UNDER_REG_SOC() != null) {
						R35cell3.setCellValue(record.getR35_UNDER_REG_SOC().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================= R36 =================
					row = sheet.getRow(35);
					Cell R36cell2 = row.createCell(1);
					if (record.getR36_BAL_SHEET_PUB_FS() != null) {
						R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					Cell R36cell3 = row.createCell(2);
					if (record.getR36_UNDER_REG_SOC() != null) {
						R36cell3.setCellValue(record.getR36_UNDER_REG_SOC().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// ================= R38 =================
					row = sheet.getRow(37);
					Cell R38cell2 = row.createCell(1);
					if (record.getR38_BAL_SHEET_PUB_FS() != null) {
						R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}
					Cell R38cell3 = row.createCell(2);
					if (record.getR38_UNDER_REG_SOC() != null) {
						R38cell3.setCellValue(record.getR38_UNDER_REG_SOC().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================= R39 =================
					row = sheet.getRow(38);
					Cell R39cell2 = row.createCell(1);
					if (record.getR39_BAL_SHEET_PUB_FS() != null) {
						R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}
					Cell R39cell3 = row.createCell(2);
					if (record.getR39_UNDER_REG_SOC() != null) {
						R39cell3.setCellValue(record.getR39_UNDER_REG_SOC().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================= R40 =================
					row = sheet.getRow(39);
					Cell R40cell2 = row.createCell(1);
					if (record.getR40_BAL_SHEET_PUB_FS() != null) {
						R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}
					Cell R40cell3 = row.createCell(2);
					if (record.getR40_UNDER_REG_SOC() != null) {
						R40cell3.setCellValue(record.getR40_UNDER_REG_SOC().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// ================= R41 =================
					row = sheet.getRow(40);
					Cell R41cell2 = row.createCell(1);
					if (record.getR41_BAL_SHEET_PUB_FS() != null) {
						R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}
					Cell R41cell3 = row.createCell(2);
					if (record.getR41_UNDER_REG_SOC() != null) {
						R41cell3.setCellValue(record.getR41_UNDER_REG_SOC().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
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

	public byte[] getExcelRecon_Of_FSARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<Recon_Of_FS_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Recon_Of_FS new report. Returning empty result.");
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
			int startRow = 6;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					Recon_Of_FS_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					row = sheet.getRow(6);
					// ================= R7 =================

					Cell R7cell2 = row.createCell(1);
					if (record.getR7_BAL_SHEET_PUB_FS() != null) {
						R7cell2.setCellValue(record.getR7_BAL_SHEET_PUB_FS().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					Cell R7cell3 = row.createCell(2);
					if (record.getR7_UNDER_REG_SOC() != null) {
						R7cell3.setCellValue(record.getR7_UNDER_REG_SOC().doubleValue());
						R7cell3.setCellStyle(numberStyle);
					} else {
						R7cell3.setCellValue("");
						R7cell3.setCellStyle(textStyle);
					}

					// ================= R8 =================
					row = sheet.getRow(7);
					Cell R8cell2 = row.createCell(1);
					if (record.getR8_BAL_SHEET_PUB_FS() != null) {
						R8cell2.setCellValue(record.getR8_BAL_SHEET_PUB_FS().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					Cell R8cell3 = row.createCell(2);
					if (record.getR8_UNDER_REG_SOC() != null) {
						R8cell3.setCellValue(record.getR8_UNDER_REG_SOC().doubleValue());
						R8cell3.setCellStyle(numberStyle);
					} else {
						R8cell3.setCellValue("");
						R8cell3.setCellStyle(textStyle);
					}

					// ================= R9 =================
					row = sheet.getRow(8);
					Cell R9cell2 = row.createCell(1);
					if (record.getR9_BAL_SHEET_PUB_FS() != null) {
						R9cell2.setCellValue(record.getR9_BAL_SHEET_PUB_FS().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					Cell R9cell3 = row.createCell(2);
					if (record.getR9_UNDER_REG_SOC() != null) {
						R9cell3.setCellValue(record.getR9_UNDER_REG_SOC().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}

					// ================= R10 =================
					row = sheet.getRow(9);
					Cell R10cell2 = row.createCell(1);
					if (record.getR10_BAL_SHEET_PUB_FS() != null) {
						R10cell2.setCellValue(record.getR10_BAL_SHEET_PUB_FS().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(2);
					if (record.getR10_UNDER_REG_SOC() != null) {
						R10cell3.setCellValue(record.getR10_UNDER_REG_SOC().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}

					// ================= R11 =================
					row = sheet.getRow(10);
					Cell R11cell2 = row.createCell(1);
					if (record.getR11_BAL_SHEET_PUB_FS() != null) {
						R11cell2.setCellValue(record.getR11_BAL_SHEET_PUB_FS().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					Cell R11cell3 = row.createCell(2);
					if (record.getR11_UNDER_REG_SOC() != null) {
						R11cell3.setCellValue(record.getR11_UNDER_REG_SOC().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}

					// ================= R12 =================
					row = sheet.getRow(11);
					Cell R12cell2 = row.createCell(1);
					if (record.getR12_BAL_SHEET_PUB_FS() != null) {
						R12cell2.setCellValue(record.getR12_BAL_SHEET_PUB_FS().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					Cell R12cell3 = row.createCell(2);
					if (record.getR12_UNDER_REG_SOC() != null) {
						R12cell3.setCellValue(record.getR12_UNDER_REG_SOC().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// ================= R13 =================
					row = sheet.getRow(12);
					Cell R13cell2 = row.createCell(1);
					if (record.getR13_BAL_SHEET_PUB_FS() != null) {
						R13cell2.setCellValue(record.getR13_BAL_SHEET_PUB_FS().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					Cell R13cell3 = row.createCell(2);
					if (record.getR13_UNDER_REG_SOC() != null) {
						R13cell3.setCellValue(record.getR13_UNDER_REG_SOC().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// ================= R14 =================
					row = sheet.getRow(13);
					Cell R14cell2 = row.createCell(1);
					if (record.getR14_BAL_SHEET_PUB_FS() != null) {
						R14cell2.setCellValue(record.getR14_BAL_SHEET_PUB_FS().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}
					Cell R14cell3 = row.createCell(2);
					if (record.getR14_UNDER_REG_SOC() != null) {
						R14cell3.setCellValue(record.getR14_UNDER_REG_SOC().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R15 =================
					row = sheet.getRow(14);
					Cell R15cell2 = row.createCell(1);
					if (record.getR15_BAL_SHEET_PUB_FS() != null) {
						R15cell2.setCellValue(record.getR15_BAL_SHEET_PUB_FS().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					Cell R15cell3 = row.createCell(2);
					if (record.getR15_UNDER_REG_SOC() != null) {
						R15cell3.setCellValue(record.getR15_UNDER_REG_SOC().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// ================= R16 =================
					row = sheet.getRow(15);
					Cell R16cell2 = row.createCell(1);
					if (record.getR16_BAL_SHEET_PUB_FS() != null) {
						R16cell2.setCellValue(record.getR16_BAL_SHEET_PUB_FS().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					Cell R16cell3 = row.createCell(2);
					if (record.getR16_UNDER_REG_SOC() != null) {
						R16cell3.setCellValue(record.getR16_UNDER_REG_SOC().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================= R17 =================
					row = sheet.getRow(16);
					Cell R17cell2 = row.createCell(1);
					if (record.getR17_BAL_SHEET_PUB_FS() != null) {
						R17cell2.setCellValue(record.getR17_BAL_SHEET_PUB_FS().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}
					Cell R17cell3 = row.createCell(2);
					if (record.getR17_UNDER_REG_SOC() != null) {
						R17cell3.setCellValue(record.getR17_UNDER_REG_SOC().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================= R18 =================
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(1);
					if (record.getR18_BAL_SHEET_PUB_FS() != null) {
						R18cell2.setCellValue(record.getR18_BAL_SHEET_PUB_FS().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}
					Cell R18cell3 = row.createCell(2);
					if (record.getR18_UNDER_REG_SOC() != null) {
						R18cell3.setCellValue(record.getR18_UNDER_REG_SOC().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}

					// ================= R19 =================
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(1);
					if (record.getR19_BAL_SHEET_PUB_FS() != null) {
						R19cell2.setCellValue(record.getR19_BAL_SHEET_PUB_FS().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}
					Cell R19cell3 = row.createCell(2);
					if (record.getR19_UNDER_REG_SOC() != null) {
						R19cell3.setCellValue(record.getR19_UNDER_REG_SOC().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================= R20 =================
					row = sheet.getRow(19);
					Cell R20cell2 = row.createCell(1);
					if (record.getR20_BAL_SHEET_PUB_FS() != null) {
						R20cell2.setCellValue(record.getR20_BAL_SHEET_PUB_FS().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}
					Cell R20cell3 = row.createCell(2);
					if (record.getR20_UNDER_REG_SOC() != null) {
						R20cell3.setCellValue(record.getR20_UNDER_REG_SOC().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);
					Cell R21cell2 = row.createCell(1);
					if (record.getR21_BAL_SHEET_PUB_FS() != null) {
						R21cell2.setCellValue(record.getR21_BAL_SHEET_PUB_FS().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					Cell R21cell3 = row.createCell(2);
					if (record.getR21_UNDER_REG_SOC() != null) {
						R21cell3.setCellValue(record.getR21_UNDER_REG_SOC().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);
					Cell R23cell2 = row.createCell(1);
					if (record.getR23_BAL_SHEET_PUB_FS() != null) {
						R23cell2.setCellValue(record.getR23_BAL_SHEET_PUB_FS().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					Cell R23cell3 = row.createCell(2);
					if (record.getR23_UNDER_REG_SOC() != null) {
						R23cell3.setCellValue(record.getR23_UNDER_REG_SOC().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);
					Cell R24cell2 = row.createCell(1);
					if (record.getR24_BAL_SHEET_PUB_FS() != null) {
						R24cell2.setCellValue(record.getR24_BAL_SHEET_PUB_FS().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					Cell R24cell3 = row.createCell(2);
					if (record.getR24_UNDER_REG_SOC() != null) {
						R24cell3.setCellValue(record.getR24_UNDER_REG_SOC().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);
					Cell R25cell2 = row.createCell(1);
					if (record.getR25_BAL_SHEET_PUB_FS() != null) {
						R25cell2.setCellValue(record.getR25_BAL_SHEET_PUB_FS().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					Cell R25cell3 = row.createCell(2);
					if (record.getR25_UNDER_REG_SOC() != null) {
						R25cell3.setCellValue(record.getR25_UNDER_REG_SOC().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);
					Cell R26cell2 = row.createCell(1);
					if (record.getR26_BAL_SHEET_PUB_FS() != null) {
						R26cell2.setCellValue(record.getR26_BAL_SHEET_PUB_FS().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					Cell R26cell3 = row.createCell(2);
					if (record.getR26_UNDER_REG_SOC() != null) {
						R26cell3.setCellValue(record.getR26_UNDER_REG_SOC().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);
					Cell R27cell2 = row.createCell(1);
					if (record.getR27_BAL_SHEET_PUB_FS() != null) {
						R27cell2.setCellValue(record.getR27_BAL_SHEET_PUB_FS().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					Cell R27cell3 = row.createCell(2);
					if (record.getR27_UNDER_REG_SOC() != null) {
						R27cell3.setCellValue(record.getR27_UNDER_REG_SOC().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// ================= R28 =================
					row = sheet.getRow(27);
					Cell R28cell2 = row.createCell(1);
					if (record.getR28_BAL_SHEET_PUB_FS() != null) {
						R28cell2.setCellValue(record.getR28_BAL_SHEET_PUB_FS().doubleValue());
						R28cell2.setCellStyle(numberStyle);
					} else {
						R28cell2.setCellValue("");
						R28cell2.setCellStyle(textStyle);
					}
					Cell R28cell3 = row.createCell(2);
					if (record.getR28_UNDER_REG_SOC() != null) {
						R28cell3.setCellValue(record.getR28_UNDER_REG_SOC().doubleValue());
						R28cell3.setCellStyle(numberStyle);
					} else {
						R28cell3.setCellValue("");
						R28cell3.setCellStyle(textStyle);
					}

					// ================= R29 =================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(1);
					if (record.getR29_BAL_SHEET_PUB_FS() != null) {
						R29cell2.setCellValue(record.getR29_BAL_SHEET_PUB_FS().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}
					Cell R29cell3 = row.createCell(2);
					if (record.getR29_UNDER_REG_SOC() != null) {
						R29cell3.setCellValue(record.getR29_UNDER_REG_SOC().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================= R30 =================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(1);
					if (record.getR30_BAL_SHEET_PUB_FS() != null) {
						R30cell2.setCellValue(record.getR30_BAL_SHEET_PUB_FS().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}
					Cell R30cell3 = row.createCell(2);
					if (record.getR30_UNDER_REG_SOC() != null) {
						R30cell3.setCellValue(record.getR30_UNDER_REG_SOC().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================= R31 =================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(1);
					if (record.getR31_BAL_SHEET_PUB_FS() != null) {
						R31cell2.setCellValue(record.getR31_BAL_SHEET_PUB_FS().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}
					Cell R31cell3 = row.createCell(2);
					if (record.getR31_UNDER_REG_SOC() != null) {
						R31cell3.setCellValue(record.getR31_UNDER_REG_SOC().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}

					// ================= R32 =================
					row = sheet.getRow(31);
					Cell R32cell2 = row.createCell(1);
					if (record.getR32_BAL_SHEET_PUB_FS() != null) {
						R32cell2.setCellValue(record.getR32_BAL_SHEET_PUB_FS().doubleValue());
						R32cell2.setCellStyle(numberStyle);
					} else {
						R32cell2.setCellValue("");
						R32cell2.setCellStyle(textStyle);
					}
					Cell R32cell3 = row.createCell(2);
					if (record.getR32_UNDER_REG_SOC() != null) {
						R32cell3.setCellValue(record.getR32_UNDER_REG_SOC().doubleValue());
						R32cell3.setCellStyle(numberStyle);
					} else {
						R32cell3.setCellValue("");
						R32cell3.setCellStyle(textStyle);
					}

					// ================= R33 =================
					row = sheet.getRow(32);
					Cell R33cell2 = row.createCell(1);
					if (record.getR33_BAL_SHEET_PUB_FS() != null) {
						R33cell2.setCellValue(record.getR33_BAL_SHEET_PUB_FS().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					Cell R33cell3 = row.createCell(2);
					if (record.getR33_UNDER_REG_SOC() != null) {
						R33cell3.setCellValue(record.getR33_UNDER_REG_SOC().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================= R34 =================
					row = sheet.getRow(33);
					Cell R34cell2 = row.createCell(1);
					if (record.getR34_BAL_SHEET_PUB_FS() != null) {
						R34cell2.setCellValue(record.getR34_BAL_SHEET_PUB_FS().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					Cell R34cell3 = row.createCell(2);
					if (record.getR34_UNDER_REG_SOC() != null) {
						R34cell3.setCellValue(record.getR34_UNDER_REG_SOC().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// ================= R35 =================
					row = sheet.getRow(34);
					Cell R35cell2 = row.createCell(1);
					if (record.getR35_BAL_SHEET_PUB_FS() != null) {
						R35cell2.setCellValue(record.getR35_BAL_SHEET_PUB_FS().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					Cell R35cell3 = row.createCell(2);
					if (record.getR35_UNDER_REG_SOC() != null) {
						R35cell3.setCellValue(record.getR35_UNDER_REG_SOC().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================= R36 =================
					row = sheet.getRow(35);
					Cell R36cell2 = row.createCell(1);
					if (record.getR36_BAL_SHEET_PUB_FS() != null) {
						R36cell2.setCellValue(record.getR36_BAL_SHEET_PUB_FS().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					Cell R36cell3 = row.createCell(2);
					if (record.getR36_UNDER_REG_SOC() != null) {
						R36cell3.setCellValue(record.getR36_UNDER_REG_SOC().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// ================= R38 =================
					row = sheet.getRow(37);
					Cell R38cell2 = row.createCell(1);
					if (record.getR38_BAL_SHEET_PUB_FS() != null) {
						R38cell2.setCellValue(record.getR38_BAL_SHEET_PUB_FS().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}
					Cell R38cell3 = row.createCell(2);
					if (record.getR38_UNDER_REG_SOC() != null) {
						R38cell3.setCellValue(record.getR38_UNDER_REG_SOC().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================= R39 =================
					row = sheet.getRow(38);
					Cell R39cell2 = row.createCell(1);
					if (record.getR39_BAL_SHEET_PUB_FS() != null) {
						R39cell2.setCellValue(record.getR39_BAL_SHEET_PUB_FS().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}
					Cell R39cell3 = row.createCell(2);
					if (record.getR39_UNDER_REG_SOC() != null) {
						R39cell3.setCellValue(record.getR39_UNDER_REG_SOC().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================= R40 =================
					row = sheet.getRow(39);
					Cell R40cell2 = row.createCell(1);
					if (record.getR40_BAL_SHEET_PUB_FS() != null) {
						R40cell2.setCellValue(record.getR40_BAL_SHEET_PUB_FS().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}
					Cell R40cell3 = row.createCell(2);
					if (record.getR40_UNDER_REG_SOC() != null) {
						R40cell3.setCellValue(record.getR40_UNDER_REG_SOC().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// ================= R41 =================
					row = sheet.getRow(40);
					Cell R41cell2 = row.createCell(1);
					if (record.getR41_BAL_SHEET_PUB_FS() != null) {
						R41cell2.setCellValue(record.getR41_BAL_SHEET_PUB_FS().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}
					Cell R41cell3 = row.createCell(2);
					if (record.getR41_UNDER_REG_SOC() != null) {
						R41cell3.setCellValue(record.getR41_UNDER_REG_SOC().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
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

//Resubmission
	public List<Object[]> getRecon_Of_FSResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Recon_Of_FS_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Recon_Of_FS_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				Recon_Of_FS_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Recon_Of_FS  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

}