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
public class BRRS_CASH_FLOW_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_CASH_FLOW_ReportService.class);

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
	public List<CASH_FLOW_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new CashFlowRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getCASH_FLOWArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<CASH_FLOW_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new CashFlowRowArchivalMapper());
	}
//GET ALL WITH VERSION

	public List<CASH_FLOW_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new CashFlowRowArchivalMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<CASH_FLOW_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new CashFlowRowDetailMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<CASH_FLOW_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new CashFlowRowDetailMapper());
	}

// 3. PAGINATION

	public List<CASH_FLOW_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new CashFlowRowDetailMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_CASH_FLOW_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<CASH_FLOW_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new CashFlowRowDetailMapper());
	}
// 6. BY ACCOUNT NUMBER

	public CASH_FLOW_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new CashFlowRowDetailMapper());
	}

// 1. GET BY DATE + VERSION

//	public List<CASH_FLOW_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
//			String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL "
//				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
//				new CashFlowRowArchivalDetailMapper());
//	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

//	public List<CASH_FLOW_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
//			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
//				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
//				new CashFlowRowArchivalDetailMapper());
//	}

//For Resubmission
	public CASH_FLOW_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new CashFlowRowDetailMapper());
	}

	public CASH_FLOW_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new CashFlowRowDetailMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	public List<CASH_FLOW_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new CashFlowRowArchivalDetailMapper());
	}

	public List<CASH_FLOW_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new CashFlowRowArchivalDetailMapper());
	}
	// ROW MAPPER

	class CashFlowRowMapper implements RowMapper<CASH_FLOW_Summary_Entity> {

		@Override
		public CASH_FLOW_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			CASH_FLOW_Summary_Entity obj = new CASH_FLOW_Summary_Entity();

			// R9
			obj.setR9_product(rs.getString("R9_PRODUCT"));
			obj.setR9_lc_as_on_mar(rs.getBigDecimal("R9_LC_AS_ON_MAR"));
			obj.setR9_lc_as_on_sep(rs.getBigDecimal("R9_LC_AS_ON_SEP"));

// R10
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_lc_as_on_mar(rs.getBigDecimal("R10_LC_AS_ON_MAR"));
			obj.setR10_lc_as_on_sep(rs.getBigDecimal("R10_LC_AS_ON_SEP"));

// R11
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_lc_as_on_mar(rs.getBigDecimal("R11_LC_AS_ON_MAR"));
			obj.setR11_lc_as_on_sep(rs.getBigDecimal("R11_LC_AS_ON_SEP"));

// R12
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_lc_as_on_mar(rs.getBigDecimal("R12_LC_AS_ON_MAR"));
			obj.setR12_lc_as_on_sep(rs.getBigDecimal("R12_LC_AS_ON_SEP"));

// R13
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_lc_as_on_mar(rs.getBigDecimal("R13_LC_AS_ON_MAR"));
			obj.setR13_lc_as_on_sep(rs.getBigDecimal("R13_LC_AS_ON_SEP"));

// R14
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_lc_as_on_mar(rs.getBigDecimal("R14_LC_AS_ON_MAR"));
			obj.setR14_lc_as_on_sep(rs.getBigDecimal("R14_LC_AS_ON_SEP"));

// R15
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_lc_as_on_mar(rs.getBigDecimal("R15_LC_AS_ON_MAR"));
			obj.setR15_lc_as_on_sep(rs.getBigDecimal("R15_LC_AS_ON_SEP"));

// R16
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_lc_as_on_mar(rs.getBigDecimal("R16_LC_AS_ON_MAR"));
			obj.setR16_lc_as_on_sep(rs.getBigDecimal("R16_LC_AS_ON_SEP"));

// R17
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_lc_as_on_mar(rs.getBigDecimal("R17_LC_AS_ON_MAR"));
			obj.setR17_lc_as_on_sep(rs.getBigDecimal("R17_LC_AS_ON_SEP"));

// R18
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_lc_as_on_mar(rs.getBigDecimal("R18_LC_AS_ON_MAR"));
			obj.setR18_lc_as_on_sep(rs.getBigDecimal("R18_LC_AS_ON_SEP"));

// R19
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_lc_as_on_mar(rs.getBigDecimal("R19_LC_AS_ON_MAR"));
			obj.setR19_lc_as_on_sep(rs.getBigDecimal("R19_LC_AS_ON_SEP"));

// R20
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_lc_as_on_mar(rs.getBigDecimal("R20_LC_AS_ON_MAR"));
			obj.setR20_lc_as_on_sep(rs.getBigDecimal("R20_LC_AS_ON_SEP"));

// R21
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_lc_as_on_mar(rs.getBigDecimal("R21_LC_AS_ON_MAR"));
			obj.setR21_lc_as_on_sep(rs.getBigDecimal("R21_LC_AS_ON_SEP"));

// R22
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_lc_as_on_mar(rs.getBigDecimal("R22_LC_AS_ON_MAR"));
			obj.setR22_lc_as_on_sep(rs.getBigDecimal("R22_LC_AS_ON_SEP"));

// R23
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_lc_as_on_mar(rs.getBigDecimal("R23_LC_AS_ON_MAR"));
			obj.setR23_lc_as_on_sep(rs.getBigDecimal("R23_LC_AS_ON_SEP"));

// R24
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_lc_as_on_mar(rs.getBigDecimal("R24_LC_AS_ON_MAR"));
			obj.setR24_lc_as_on_sep(rs.getBigDecimal("R24_LC_AS_ON_SEP"));

// R25
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_lc_as_on_mar(rs.getBigDecimal("R25_LC_AS_ON_MAR"));
			obj.setR25_lc_as_on_sep(rs.getBigDecimal("R25_LC_AS_ON_SEP"));

// R26
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_lc_as_on_mar(rs.getBigDecimal("R26_LC_AS_ON_MAR"));
			obj.setR26_lc_as_on_sep(rs.getBigDecimal("R26_LC_AS_ON_SEP"));

// R27
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_lc_as_on_mar(rs.getBigDecimal("R27_LC_AS_ON_MAR"));
			obj.setR27_lc_as_on_sep(rs.getBigDecimal("R27_LC_AS_ON_SEP"));

// R28
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_lc_as_on_mar(rs.getBigDecimal("R28_LC_AS_ON_MAR"));
			obj.setR28_lc_as_on_sep(rs.getBigDecimal("R28_LC_AS_ON_SEP"));

// R29
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_lc_as_on_mar(rs.getBigDecimal("R29_LC_AS_ON_MAR"));
			obj.setR29_lc_as_on_sep(rs.getBigDecimal("R29_LC_AS_ON_SEP"));

// R30
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_lc_as_on_mar(rs.getBigDecimal("R30_LC_AS_ON_MAR"));
			obj.setR30_lc_as_on_sep(rs.getBigDecimal("R30_LC_AS_ON_SEP"));

// R31
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_lc_as_on_mar(rs.getBigDecimal("R31_LC_AS_ON_MAR"));
			obj.setR31_lc_as_on_sep(rs.getBigDecimal("R31_LC_AS_ON_SEP"));

// R32
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_lc_as_on_mar(rs.getBigDecimal("R32_LC_AS_ON_MAR"));
			obj.setR32_lc_as_on_sep(rs.getBigDecimal("R32_LC_AS_ON_SEP"));

// R33
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_lc_as_on_mar(rs.getBigDecimal("R33_LC_AS_ON_MAR"));
			obj.setR33_lc_as_on_sep(rs.getBigDecimal("R33_LC_AS_ON_SEP"));

// R34
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_lc_as_on_mar(rs.getBigDecimal("R34_LC_AS_ON_MAR"));
			obj.setR34_lc_as_on_sep(rs.getBigDecimal("R34_LC_AS_ON_SEP"));

// R35
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_lc_as_on_mar(rs.getBigDecimal("R35_LC_AS_ON_MAR"));
			obj.setR35_lc_as_on_sep(rs.getBigDecimal("R35_LC_AS_ON_SEP"));

// R36
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_lc_as_on_mar(rs.getBigDecimal("R36_LC_AS_ON_MAR"));
			obj.setR36_lc_as_on_sep(rs.getBigDecimal("R36_LC_AS_ON_SEP"));

// R37
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_lc_as_on_mar(rs.getBigDecimal("R37_LC_AS_ON_MAR"));
			obj.setR37_lc_as_on_sep(rs.getBigDecimal("R37_LC_AS_ON_SEP"));

// R38
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_lc_as_on_mar(rs.getBigDecimal("R38_LC_AS_ON_MAR"));
			obj.setR38_lc_as_on_sep(rs.getBigDecimal("R38_LC_AS_ON_SEP"));

// R39
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_lc_as_on_mar(rs.getBigDecimal("R39_LC_AS_ON_MAR"));
			obj.setR39_lc_as_on_sep(rs.getBigDecimal("R39_LC_AS_ON_SEP"));

// R40
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_lc_as_on_mar(rs.getBigDecimal("R40_LC_AS_ON_MAR"));
			obj.setR40_lc_as_on_sep(rs.getBigDecimal("R40_LC_AS_ON_SEP"));

// R41
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_lc_as_on_mar(rs.getBigDecimal("R41_LC_AS_ON_MAR"));
			obj.setR41_lc_as_on_sep(rs.getBigDecimal("R41_LC_AS_ON_SEP"));

// R42
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_lc_as_on_mar(rs.getBigDecimal("R42_LC_AS_ON_MAR"));
			obj.setR42_lc_as_on_sep(rs.getBigDecimal("R42_LC_AS_ON_SEP"));

// R43
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_lc_as_on_mar(rs.getBigDecimal("R43_LC_AS_ON_MAR"));
			obj.setR43_lc_as_on_sep(rs.getBigDecimal("R43_LC_AS_ON_SEP"));

// R44
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_lc_as_on_mar(rs.getBigDecimal("R44_LC_AS_ON_MAR"));
			obj.setR44_lc_as_on_sep(rs.getBigDecimal("R44_LC_AS_ON_SEP"));

// R45
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_lc_as_on_mar(rs.getBigDecimal("R45_LC_AS_ON_MAR"));
			obj.setR45_lc_as_on_sep(rs.getBigDecimal("R45_LC_AS_ON_SEP"));

// R46
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_lc_as_on_mar(rs.getBigDecimal("R46_LC_AS_ON_MAR"));
			obj.setR46_lc_as_on_sep(rs.getBigDecimal("R46_LC_AS_ON_SEP"));

// R47
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_lc_as_on_mar(rs.getBigDecimal("R47_LC_AS_ON_MAR"));
			obj.setR47_lc_as_on_sep(rs.getBigDecimal("R47_LC_AS_ON_SEP"));

// R48
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_lc_as_on_mar(rs.getBigDecimal("R48_LC_AS_ON_MAR"));
			obj.setR48_lc_as_on_sep(rs.getBigDecimal("R48_LC_AS_ON_SEP"));

// R49
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_lc_as_on_mar(rs.getBigDecimal("R49_LC_AS_ON_MAR"));
			obj.setR49_lc_as_on_sep(rs.getBigDecimal("R49_LC_AS_ON_SEP"));

// R50
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_lc_as_on_mar(rs.getBigDecimal("R50_LC_AS_ON_MAR"));
			obj.setR50_lc_as_on_sep(rs.getBigDecimal("R50_LC_AS_ON_SEP"));

// R51
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_lc_as_on_mar(rs.getBigDecimal("R51_LC_AS_ON_MAR"));
			obj.setR51_lc_as_on_sep(rs.getBigDecimal("R51_LC_AS_ON_SEP"));

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

	public static class CASH_FLOW_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R9_PRODUCT")
		private String r9_product;
		@Column(name = "R9_LC_AS_ON_MAR")
		private BigDecimal r9_lc_as_on_mar;
		@Column(name = "R9_LC_AS_ON_SEP")
		private BigDecimal r9_lc_as_on_sep;

		@Column(name = "R10_PRODUCT")
		private String r10_product;
		@Column(name = "R10_LC_AS_ON_MAR")
		private BigDecimal r10_lc_as_on_mar;
		@Column(name = "R10_LC_AS_ON_SEP")
		private BigDecimal r10_lc_as_on_sep;

		@Column(name = "R11_PRODUCT")
		private String r11_product;
		@Column(name = "R11_LC_AS_ON_MAR")
		private BigDecimal r11_lc_as_on_mar;
		@Column(name = "R11_LC_AS_ON_SEP")
		private BigDecimal r11_lc_as_on_sep;

		@Column(name = "R12_PRODUCT")
		private String r12_product;
		@Column(name = "R12_LC_AS_ON_MAR")
		private BigDecimal r12_lc_as_on_mar;
		@Column(name = "R12_LC_AS_ON_SEP")
		private BigDecimal r12_lc_as_on_sep;

		@Column(name = "R13_PRODUCT")
		private String r13_product;
		@Column(name = "R13_LC_AS_ON_MAR")
		private BigDecimal r13_lc_as_on_mar;
		@Column(name = "R13_LC_AS_ON_SEP")
		private BigDecimal r13_lc_as_on_sep;

		@Column(name = "R14_PRODUCT")
		private String r14_product;
		@Column(name = "R14_LC_AS_ON_MAR")
		private BigDecimal r14_lc_as_on_mar;
		@Column(name = "R14_LC_AS_ON_SEP")
		private BigDecimal r14_lc_as_on_sep;

		@Column(name = "R15_PRODUCT")
		private String r15_product;
		@Column(name = "R15_LC_AS_ON_MAR")
		private BigDecimal r15_lc_as_on_mar;
		@Column(name = "R15_LC_AS_ON_SEP")
		private BigDecimal r15_lc_as_on_sep;

		@Column(name = "R16_PRODUCT")
		private String r16_product;
		@Column(name = "R16_LC_AS_ON_MAR")
		private BigDecimal r16_lc_as_on_mar;
		@Column(name = "R16_LC_AS_ON_SEP")
		private BigDecimal r16_lc_as_on_sep;

		@Column(name = "R17_PRODUCT")
		private String r17_product;
		@Column(name = "R17_LC_AS_ON_MAR")
		private BigDecimal r17_lc_as_on_mar;
		@Column(name = "R17_LC_AS_ON_SEP")
		private BigDecimal r17_lc_as_on_sep;

		@Column(name = "R18_PRODUCT")
		private String r18_product;
		@Column(name = "R18_LC_AS_ON_MAR")
		private BigDecimal r18_lc_as_on_mar;
		@Column(name = "R18_LC_AS_ON_SEP")
		private BigDecimal r18_lc_as_on_sep;

		@Column(name = "R19_PRODUCT")
		private String r19_product;
		@Column(name = "R19_LC_AS_ON_MAR")
		private BigDecimal r19_lc_as_on_mar;
		@Column(name = "R19_LC_AS_ON_SEP")
		private BigDecimal r19_lc_as_on_sep;

		@Column(name = "R20_PRODUCT")
		private String r20_product;
		@Column(name = "R20_LC_AS_ON_MAR")
		private BigDecimal r20_lc_as_on_mar;
		@Column(name = "R20_LC_AS_ON_SEP")
		private BigDecimal r20_lc_as_on_sep;

		@Column(name = "R21_PRODUCT")
		private String r21_product;
		@Column(name = "R21_LC_AS_ON_MAR")
		private BigDecimal r21_lc_as_on_mar;
		@Column(name = "R21_LC_AS_ON_SEP")
		private BigDecimal r21_lc_as_on_sep;

		@Column(name = "R22_PRODUCT")
		private String r22_product;
		@Column(name = "R22_LC_AS_ON_MAR")
		private BigDecimal r22_lc_as_on_mar;
		@Column(name = "R22_LC_AS_ON_SEP")
		private BigDecimal r22_lc_as_on_sep;

		@Column(name = "R23_PRODUCT")
		private String r23_product;
		@Column(name = "R23_LC_AS_ON_MAR")
		private BigDecimal r23_lc_as_on_mar;
		@Column(name = "R23_LC_AS_ON_SEP")
		private BigDecimal r23_lc_as_on_sep;

		@Column(name = "R24_PRODUCT")
		private String r24_product;
		@Column(name = "R24_LC_AS_ON_MAR")
		private BigDecimal r24_lc_as_on_mar;
		@Column(name = "R24_LC_AS_ON_SEP")
		private BigDecimal r24_lc_as_on_sep;

		@Column(name = "R25_PRODUCT")
		private String r25_product;
		@Column(name = "R25_LC_AS_ON_MAR")
		private BigDecimal r25_lc_as_on_mar;
		@Column(name = "R25_LC_AS_ON_SEP")
		private BigDecimal r25_lc_as_on_sep;

		@Column(name = "R26_PRODUCT")
		private String r26_product;
		@Column(name = "R26_LC_AS_ON_MAR")
		private BigDecimal r26_lc_as_on_mar;
		@Column(name = "R26_LC_AS_ON_SEP")
		private BigDecimal r26_lc_as_on_sep;

		@Column(name = "R27_PRODUCT")
		private String r27_product;
		@Column(name = "R27_LC_AS_ON_MAR")
		private BigDecimal r27_lc_as_on_mar;
		@Column(name = "R27_LC_AS_ON_SEP")
		private BigDecimal r27_lc_as_on_sep;

		@Column(name = "R28_PRODUCT")
		private String r28_product;
		@Column(name = "R28_LC_AS_ON_MAR")
		private BigDecimal r28_lc_as_on_mar;
		@Column(name = "R28_LC_AS_ON_SEP")
		private BigDecimal r28_lc_as_on_sep;

		@Column(name = "R29_PRODUCT")
		private String r29_product;
		@Column(name = "R29_LC_AS_ON_MAR")
		private BigDecimal r29_lc_as_on_mar;
		@Column(name = "R29_LC_AS_ON_SEP")
		private BigDecimal r29_lc_as_on_sep;

		@Column(name = "R30_PRODUCT")
		private String r30_product;
		@Column(name = "R30_LC_AS_ON_MAR")
		private BigDecimal r30_lc_as_on_mar;
		@Column(name = "R30_LC_AS_ON_SEP")
		private BigDecimal r30_lc_as_on_sep;

		@Column(name = "R31_PRODUCT")
		private String r31_product;
		@Column(name = "R31_LC_AS_ON_MAR")
		private BigDecimal r31_lc_as_on_mar;
		@Column(name = "R31_LC_AS_ON_SEP")
		private BigDecimal r31_lc_as_on_sep;

		@Column(name = "R32_PRODUCT")
		private String r32_product;
		@Column(name = "R32_LC_AS_ON_MAR")
		private BigDecimal r32_lc_as_on_mar;
		@Column(name = "R32_LC_AS_ON_SEP")
		private BigDecimal r32_lc_as_on_sep;

		@Column(name = "R33_PRODUCT")
		private String r33_product;
		@Column(name = "R33_LC_AS_ON_MAR")
		private BigDecimal r33_lc_as_on_mar;
		@Column(name = "R33_LC_AS_ON_SEP")
		private BigDecimal r33_lc_as_on_sep;

		@Column(name = "R34_PRODUCT")
		private String r34_product;
		@Column(name = "R34_LC_AS_ON_MAR")
		private BigDecimal r34_lc_as_on_mar;
		@Column(name = "R34_LC_AS_ON_SEP")
		private BigDecimal r34_lc_as_on_sep;

		@Column(name = "R35_PRODUCT")
		private String r35_product;
		@Column(name = "R35_LC_AS_ON_MAR")
		private BigDecimal r35_lc_as_on_mar;
		@Column(name = "R35_LC_AS_ON_SEP")
		private BigDecimal r35_lc_as_on_sep;

		@Column(name = "R36_PRODUCT")
		private String r36_product;
		@Column(name = "R36_LC_AS_ON_MAR")
		private BigDecimal r36_lc_as_on_mar;
		@Column(name = "R36_LC_AS_ON_SEP")
		private BigDecimal r36_lc_as_on_sep;

		@Column(name = "R37_PRODUCT")
		private String r37_product;
		@Column(name = "R37_LC_AS_ON_MAR")
		private BigDecimal r37_lc_as_on_mar;
		@Column(name = "R37_LC_AS_ON_SEP")
		private BigDecimal r37_lc_as_on_sep;

		@Column(name = "R38_PRODUCT")
		private String r38_product;
		@Column(name = "R38_LC_AS_ON_MAR")
		private BigDecimal r38_lc_as_on_mar;
		@Column(name = "R38_LC_AS_ON_SEP")
		private BigDecimal r38_lc_as_on_sep;

		@Column(name = "R39_PRODUCT")
		private String r39_product;
		@Column(name = "R39_LC_AS_ON_MAR")
		private BigDecimal r39_lc_as_on_mar;
		@Column(name = "R39_LC_AS_ON_SEP")
		private BigDecimal r39_lc_as_on_sep;

		@Column(name = "R40_PRODUCT")
		private String r40_product;
		@Column(name = "R40_LC_AS_ON_MAR")
		private BigDecimal r40_lc_as_on_mar;
		@Column(name = "R40_LC_AS_ON_SEP")
		private BigDecimal r40_lc_as_on_sep;

		@Column(name = "R41_PRODUCT")
		private String r41_product;
		@Column(name = "R41_LC_AS_ON_MAR")
		private BigDecimal r41_lc_as_on_mar;
		@Column(name = "R41_LC_AS_ON_SEP")
		private BigDecimal r41_lc_as_on_sep;

		@Column(name = "R42_PRODUCT")
		private String r42_product;
		@Column(name = "R42_LC_AS_ON_MAR")
		private BigDecimal r42_lc_as_on_mar;
		@Column(name = "R42_LC_AS_ON_SEP")
		private BigDecimal r42_lc_as_on_sep;

		@Column(name = "R43_PRODUCT")
		private String r43_product;
		@Column(name = "R43_LC_AS_ON_MAR")
		private BigDecimal r43_lc_as_on_mar;
		@Column(name = "R43_LC_AS_ON_SEP")
		private BigDecimal r43_lc_as_on_sep;

		@Column(name = "R44_PRODUCT")
		private String r44_product;
		@Column(name = "R44_LC_AS_ON_MAR")
		private BigDecimal r44_lc_as_on_mar;
		@Column(name = "R44_LC_AS_ON_SEP")
		private BigDecimal r44_lc_as_on_sep;

		@Column(name = "R45_PRODUCT")
		private String r45_product;
		@Column(name = "R45_LC_AS_ON_MAR")
		private BigDecimal r45_lc_as_on_mar;
		@Column(name = "R45_LC_AS_ON_SEP")
		private BigDecimal r45_lc_as_on_sep;

		@Column(name = "R46_PRODUCT")
		private String r46_product;
		@Column(name = "R46_LC_AS_ON_MAR")
		private BigDecimal r46_lc_as_on_mar;
		@Column(name = "R46_LC_AS_ON_SEP")
		private BigDecimal r46_lc_as_on_sep;

		@Column(name = "R47_PRODUCT")
		private String r47_product;
		@Column(name = "R47_LC_AS_ON_MAR")
		private BigDecimal r47_lc_as_on_mar;
		@Column(name = "R47_LC_AS_ON_SEP")
		private BigDecimal r47_lc_as_on_sep;

		@Column(name = "R48_PRODUCT")
		private String r48_product;
		@Column(name = "R48_LC_AS_ON_MAR")
		private BigDecimal r48_lc_as_on_mar;
		@Column(name = "R48_LC_AS_ON_SEP")
		private BigDecimal r48_lc_as_on_sep;

		@Column(name = "R49_PRODUCT")
		private String r49_product;
		@Column(name = "R49_LC_AS_ON_MAR")
		private BigDecimal r49_lc_as_on_mar;
		@Column(name = "R49_LC_AS_ON_SEP")
		private BigDecimal r49_lc_as_on_sep;

		@Column(name = "R50_PRODUCT")
		private String r50_product;
		@Column(name = "R50_LC_AS_ON_MAR")
		private BigDecimal r50_lc_as_on_mar;
		@Column(name = "R50_LC_AS_ON_SEP")
		private BigDecimal r50_lc_as_on_sep;

		@Column(name = "R51_PRODUCT")
		private String r51_product;
		@Column(name = "R51_LC_AS_ON_MAR")
		private BigDecimal r51_lc_as_on_mar;
		@Column(name = "R51_LC_AS_ON_SEP")
		private BigDecimal r51_lc_as_on_sep;

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

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_lc_as_on_mar() {
			return r9_lc_as_on_mar;
		}

		public void setR9_lc_as_on_mar(BigDecimal r9_lc_as_on_mar) {
			this.r9_lc_as_on_mar = r9_lc_as_on_mar;
		}

		public BigDecimal getR9_lc_as_on_sep() {
			return r9_lc_as_on_sep;
		}

		public void setR9_lc_as_on_sep(BigDecimal r9_lc_as_on_sep) {
			this.r9_lc_as_on_sep = r9_lc_as_on_sep;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_lc_as_on_mar() {
			return r10_lc_as_on_mar;
		}

		public void setR10_lc_as_on_mar(BigDecimal r10_lc_as_on_mar) {
			this.r10_lc_as_on_mar = r10_lc_as_on_mar;
		}

		public BigDecimal getR10_lc_as_on_sep() {
			return r10_lc_as_on_sep;
		}

		public void setR10_lc_as_on_sep(BigDecimal r10_lc_as_on_sep) {
			this.r10_lc_as_on_sep = r10_lc_as_on_sep;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_lc_as_on_mar() {
			return r11_lc_as_on_mar;
		}

		public void setR11_lc_as_on_mar(BigDecimal r11_lc_as_on_mar) {
			this.r11_lc_as_on_mar = r11_lc_as_on_mar;
		}

		public BigDecimal getR11_lc_as_on_sep() {
			return r11_lc_as_on_sep;
		}

		public void setR11_lc_as_on_sep(BigDecimal r11_lc_as_on_sep) {
			this.r11_lc_as_on_sep = r11_lc_as_on_sep;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_lc_as_on_mar() {
			return r12_lc_as_on_mar;
		}

		public void setR12_lc_as_on_mar(BigDecimal r12_lc_as_on_mar) {
			this.r12_lc_as_on_mar = r12_lc_as_on_mar;
		}

		public BigDecimal getR12_lc_as_on_sep() {
			return r12_lc_as_on_sep;
		}

		public void setR12_lc_as_on_sep(BigDecimal r12_lc_as_on_sep) {
			this.r12_lc_as_on_sep = r12_lc_as_on_sep;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_lc_as_on_mar() {
			return r13_lc_as_on_mar;
		}

		public void setR13_lc_as_on_mar(BigDecimal r13_lc_as_on_mar) {
			this.r13_lc_as_on_mar = r13_lc_as_on_mar;
		}

		public BigDecimal getR13_lc_as_on_sep() {
			return r13_lc_as_on_sep;
		}

		public void setR13_lc_as_on_sep(BigDecimal r13_lc_as_on_sep) {
			this.r13_lc_as_on_sep = r13_lc_as_on_sep;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_lc_as_on_mar() {
			return r14_lc_as_on_mar;
		}

		public void setR14_lc_as_on_mar(BigDecimal r14_lc_as_on_mar) {
			this.r14_lc_as_on_mar = r14_lc_as_on_mar;
		}

		public BigDecimal getR14_lc_as_on_sep() {
			return r14_lc_as_on_sep;
		}

		public void setR14_lc_as_on_sep(BigDecimal r14_lc_as_on_sep) {
			this.r14_lc_as_on_sep = r14_lc_as_on_sep;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_lc_as_on_mar() {
			return r15_lc_as_on_mar;
		}

		public void setR15_lc_as_on_mar(BigDecimal r15_lc_as_on_mar) {
			this.r15_lc_as_on_mar = r15_lc_as_on_mar;
		}

		public BigDecimal getR15_lc_as_on_sep() {
			return r15_lc_as_on_sep;
		}

		public void setR15_lc_as_on_sep(BigDecimal r15_lc_as_on_sep) {
			this.r15_lc_as_on_sep = r15_lc_as_on_sep;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_lc_as_on_mar() {
			return r16_lc_as_on_mar;
		}

		public void setR16_lc_as_on_mar(BigDecimal r16_lc_as_on_mar) {
			this.r16_lc_as_on_mar = r16_lc_as_on_mar;
		}

		public BigDecimal getR16_lc_as_on_sep() {
			return r16_lc_as_on_sep;
		}

		public void setR16_lc_as_on_sep(BigDecimal r16_lc_as_on_sep) {
			this.r16_lc_as_on_sep = r16_lc_as_on_sep;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_lc_as_on_mar() {
			return r17_lc_as_on_mar;
		}

		public void setR17_lc_as_on_mar(BigDecimal r17_lc_as_on_mar) {
			this.r17_lc_as_on_mar = r17_lc_as_on_mar;
		}

		public BigDecimal getR17_lc_as_on_sep() {
			return r17_lc_as_on_sep;
		}

		public void setR17_lc_as_on_sep(BigDecimal r17_lc_as_on_sep) {
			this.r17_lc_as_on_sep = r17_lc_as_on_sep;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_lc_as_on_mar() {
			return r18_lc_as_on_mar;
		}

		public void setR18_lc_as_on_mar(BigDecimal r18_lc_as_on_mar) {
			this.r18_lc_as_on_mar = r18_lc_as_on_mar;
		}

		public BigDecimal getR18_lc_as_on_sep() {
			return r18_lc_as_on_sep;
		}

		public void setR18_lc_as_on_sep(BigDecimal r18_lc_as_on_sep) {
			this.r18_lc_as_on_sep = r18_lc_as_on_sep;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_lc_as_on_mar() {
			return r19_lc_as_on_mar;
		}

		public void setR19_lc_as_on_mar(BigDecimal r19_lc_as_on_mar) {
			this.r19_lc_as_on_mar = r19_lc_as_on_mar;
		}

		public BigDecimal getR19_lc_as_on_sep() {
			return r19_lc_as_on_sep;
		}

		public void setR19_lc_as_on_sep(BigDecimal r19_lc_as_on_sep) {
			this.r19_lc_as_on_sep = r19_lc_as_on_sep;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_lc_as_on_mar() {
			return r20_lc_as_on_mar;
		}

		public void setR20_lc_as_on_mar(BigDecimal r20_lc_as_on_mar) {
			this.r20_lc_as_on_mar = r20_lc_as_on_mar;
		}

		public BigDecimal getR20_lc_as_on_sep() {
			return r20_lc_as_on_sep;
		}

		public void setR20_lc_as_on_sep(BigDecimal r20_lc_as_on_sep) {
			this.r20_lc_as_on_sep = r20_lc_as_on_sep;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_lc_as_on_mar() {
			return r21_lc_as_on_mar;
		}

		public void setR21_lc_as_on_mar(BigDecimal r21_lc_as_on_mar) {
			this.r21_lc_as_on_mar = r21_lc_as_on_mar;
		}

		public BigDecimal getR21_lc_as_on_sep() {
			return r21_lc_as_on_sep;
		}

		public void setR21_lc_as_on_sep(BigDecimal r21_lc_as_on_sep) {
			this.r21_lc_as_on_sep = r21_lc_as_on_sep;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_lc_as_on_mar() {
			return r22_lc_as_on_mar;
		}

		public void setR22_lc_as_on_mar(BigDecimal r22_lc_as_on_mar) {
			this.r22_lc_as_on_mar = r22_lc_as_on_mar;
		}

		public BigDecimal getR22_lc_as_on_sep() {
			return r22_lc_as_on_sep;
		}

		public void setR22_lc_as_on_sep(BigDecimal r22_lc_as_on_sep) {
			this.r22_lc_as_on_sep = r22_lc_as_on_sep;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_lc_as_on_mar() {
			return r23_lc_as_on_mar;
		}

		public void setR23_lc_as_on_mar(BigDecimal r23_lc_as_on_mar) {
			this.r23_lc_as_on_mar = r23_lc_as_on_mar;
		}

		public BigDecimal getR23_lc_as_on_sep() {
			return r23_lc_as_on_sep;
		}

		public void setR23_lc_as_on_sep(BigDecimal r23_lc_as_on_sep) {
			this.r23_lc_as_on_sep = r23_lc_as_on_sep;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_lc_as_on_mar() {
			return r24_lc_as_on_mar;
		}

		public void setR24_lc_as_on_mar(BigDecimal r24_lc_as_on_mar) {
			this.r24_lc_as_on_mar = r24_lc_as_on_mar;
		}

		public BigDecimal getR24_lc_as_on_sep() {
			return r24_lc_as_on_sep;
		}

		public void setR24_lc_as_on_sep(BigDecimal r24_lc_as_on_sep) {
			this.r24_lc_as_on_sep = r24_lc_as_on_sep;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_lc_as_on_mar() {
			return r25_lc_as_on_mar;
		}

		public void setR25_lc_as_on_mar(BigDecimal r25_lc_as_on_mar) {
			this.r25_lc_as_on_mar = r25_lc_as_on_mar;
		}

		public BigDecimal getR25_lc_as_on_sep() {
			return r25_lc_as_on_sep;
		}

		public void setR25_lc_as_on_sep(BigDecimal r25_lc_as_on_sep) {
			this.r25_lc_as_on_sep = r25_lc_as_on_sep;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_lc_as_on_mar() {
			return r26_lc_as_on_mar;
		}

		public void setR26_lc_as_on_mar(BigDecimal r26_lc_as_on_mar) {
			this.r26_lc_as_on_mar = r26_lc_as_on_mar;
		}

		public BigDecimal getR26_lc_as_on_sep() {
			return r26_lc_as_on_sep;
		}

		public void setR26_lc_as_on_sep(BigDecimal r26_lc_as_on_sep) {
			this.r26_lc_as_on_sep = r26_lc_as_on_sep;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_lc_as_on_mar() {
			return r27_lc_as_on_mar;
		}

		public void setR27_lc_as_on_mar(BigDecimal r27_lc_as_on_mar) {
			this.r27_lc_as_on_mar = r27_lc_as_on_mar;
		}

		public BigDecimal getR27_lc_as_on_sep() {
			return r27_lc_as_on_sep;
		}

		public void setR27_lc_as_on_sep(BigDecimal r27_lc_as_on_sep) {
			this.r27_lc_as_on_sep = r27_lc_as_on_sep;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_lc_as_on_mar() {
			return r28_lc_as_on_mar;
		}

		public void setR28_lc_as_on_mar(BigDecimal r28_lc_as_on_mar) {
			this.r28_lc_as_on_mar = r28_lc_as_on_mar;
		}

		public BigDecimal getR28_lc_as_on_sep() {
			return r28_lc_as_on_sep;
		}

		public void setR28_lc_as_on_sep(BigDecimal r28_lc_as_on_sep) {
			this.r28_lc_as_on_sep = r28_lc_as_on_sep;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_lc_as_on_mar() {
			return r29_lc_as_on_mar;
		}

		public void setR29_lc_as_on_mar(BigDecimal r29_lc_as_on_mar) {
			this.r29_lc_as_on_mar = r29_lc_as_on_mar;
		}

		public BigDecimal getR29_lc_as_on_sep() {
			return r29_lc_as_on_sep;
		}

		public void setR29_lc_as_on_sep(BigDecimal r29_lc_as_on_sep) {
			this.r29_lc_as_on_sep = r29_lc_as_on_sep;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_lc_as_on_mar() {
			return r30_lc_as_on_mar;
		}

		public void setR30_lc_as_on_mar(BigDecimal r30_lc_as_on_mar) {
			this.r30_lc_as_on_mar = r30_lc_as_on_mar;
		}

		public BigDecimal getR30_lc_as_on_sep() {
			return r30_lc_as_on_sep;
		}

		public void setR30_lc_as_on_sep(BigDecimal r30_lc_as_on_sep) {
			this.r30_lc_as_on_sep = r30_lc_as_on_sep;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_lc_as_on_mar() {
			return r31_lc_as_on_mar;
		}

		public void setR31_lc_as_on_mar(BigDecimal r31_lc_as_on_mar) {
			this.r31_lc_as_on_mar = r31_lc_as_on_mar;
		}

		public BigDecimal getR31_lc_as_on_sep() {
			return r31_lc_as_on_sep;
		}

		public void setR31_lc_as_on_sep(BigDecimal r31_lc_as_on_sep) {
			this.r31_lc_as_on_sep = r31_lc_as_on_sep;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_lc_as_on_mar() {
			return r32_lc_as_on_mar;
		}

		public void setR32_lc_as_on_mar(BigDecimal r32_lc_as_on_mar) {
			this.r32_lc_as_on_mar = r32_lc_as_on_mar;
		}

		public BigDecimal getR32_lc_as_on_sep() {
			return r32_lc_as_on_sep;
		}

		public void setR32_lc_as_on_sep(BigDecimal r32_lc_as_on_sep) {
			this.r32_lc_as_on_sep = r32_lc_as_on_sep;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_lc_as_on_mar() {
			return r33_lc_as_on_mar;
		}

		public void setR33_lc_as_on_mar(BigDecimal r33_lc_as_on_mar) {
			this.r33_lc_as_on_mar = r33_lc_as_on_mar;
		}

		public BigDecimal getR33_lc_as_on_sep() {
			return r33_lc_as_on_sep;
		}

		public void setR33_lc_as_on_sep(BigDecimal r33_lc_as_on_sep) {
			this.r33_lc_as_on_sep = r33_lc_as_on_sep;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_lc_as_on_mar() {
			return r34_lc_as_on_mar;
		}

		public void setR34_lc_as_on_mar(BigDecimal r34_lc_as_on_mar) {
			this.r34_lc_as_on_mar = r34_lc_as_on_mar;
		}

		public BigDecimal getR34_lc_as_on_sep() {
			return r34_lc_as_on_sep;
		}

		public void setR34_lc_as_on_sep(BigDecimal r34_lc_as_on_sep) {
			this.r34_lc_as_on_sep = r34_lc_as_on_sep;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_lc_as_on_mar() {
			return r35_lc_as_on_mar;
		}

		public void setR35_lc_as_on_mar(BigDecimal r35_lc_as_on_mar) {
			this.r35_lc_as_on_mar = r35_lc_as_on_mar;
		}

		public BigDecimal getR35_lc_as_on_sep() {
			return r35_lc_as_on_sep;
		}

		public void setR35_lc_as_on_sep(BigDecimal r35_lc_as_on_sep) {
			this.r35_lc_as_on_sep = r35_lc_as_on_sep;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_lc_as_on_mar() {
			return r36_lc_as_on_mar;
		}

		public void setR36_lc_as_on_mar(BigDecimal r36_lc_as_on_mar) {
			this.r36_lc_as_on_mar = r36_lc_as_on_mar;
		}

		public BigDecimal getR36_lc_as_on_sep() {
			return r36_lc_as_on_sep;
		}

		public void setR36_lc_as_on_sep(BigDecimal r36_lc_as_on_sep) {
			this.r36_lc_as_on_sep = r36_lc_as_on_sep;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_lc_as_on_mar() {
			return r37_lc_as_on_mar;
		}

		public void setR37_lc_as_on_mar(BigDecimal r37_lc_as_on_mar) {
			this.r37_lc_as_on_mar = r37_lc_as_on_mar;
		}

		public BigDecimal getR37_lc_as_on_sep() {
			return r37_lc_as_on_sep;
		}

		public void setR37_lc_as_on_sep(BigDecimal r37_lc_as_on_sep) {
			this.r37_lc_as_on_sep = r37_lc_as_on_sep;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_lc_as_on_mar() {
			return r38_lc_as_on_mar;
		}

		public void setR38_lc_as_on_mar(BigDecimal r38_lc_as_on_mar) {
			this.r38_lc_as_on_mar = r38_lc_as_on_mar;
		}

		public BigDecimal getR38_lc_as_on_sep() {
			return r38_lc_as_on_sep;
		}

		public void setR38_lc_as_on_sep(BigDecimal r38_lc_as_on_sep) {
			this.r38_lc_as_on_sep = r38_lc_as_on_sep;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_lc_as_on_mar() {
			return r39_lc_as_on_mar;
		}

		public void setR39_lc_as_on_mar(BigDecimal r39_lc_as_on_mar) {
			this.r39_lc_as_on_mar = r39_lc_as_on_mar;
		}

		public BigDecimal getR39_lc_as_on_sep() {
			return r39_lc_as_on_sep;
		}

		public void setR39_lc_as_on_sep(BigDecimal r39_lc_as_on_sep) {
			this.r39_lc_as_on_sep = r39_lc_as_on_sep;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_lc_as_on_mar() {
			return r40_lc_as_on_mar;
		}

		public void setR40_lc_as_on_mar(BigDecimal r40_lc_as_on_mar) {
			this.r40_lc_as_on_mar = r40_lc_as_on_mar;
		}

		public BigDecimal getR40_lc_as_on_sep() {
			return r40_lc_as_on_sep;
		}

		public void setR40_lc_as_on_sep(BigDecimal r40_lc_as_on_sep) {
			this.r40_lc_as_on_sep = r40_lc_as_on_sep;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_lc_as_on_mar() {
			return r41_lc_as_on_mar;
		}

		public void setR41_lc_as_on_mar(BigDecimal r41_lc_as_on_mar) {
			this.r41_lc_as_on_mar = r41_lc_as_on_mar;
		}

		public BigDecimal getR41_lc_as_on_sep() {
			return r41_lc_as_on_sep;
		}

		public void setR41_lc_as_on_sep(BigDecimal r41_lc_as_on_sep) {
			this.r41_lc_as_on_sep = r41_lc_as_on_sep;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_lc_as_on_mar() {
			return r42_lc_as_on_mar;
		}

		public void setR42_lc_as_on_mar(BigDecimal r42_lc_as_on_mar) {
			this.r42_lc_as_on_mar = r42_lc_as_on_mar;
		}

		public BigDecimal getR42_lc_as_on_sep() {
			return r42_lc_as_on_sep;
		}

		public void setR42_lc_as_on_sep(BigDecimal r42_lc_as_on_sep) {
			this.r42_lc_as_on_sep = r42_lc_as_on_sep;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_lc_as_on_mar() {
			return r43_lc_as_on_mar;
		}

		public void setR43_lc_as_on_mar(BigDecimal r43_lc_as_on_mar) {
			this.r43_lc_as_on_mar = r43_lc_as_on_mar;
		}

		public BigDecimal getR43_lc_as_on_sep() {
			return r43_lc_as_on_sep;
		}

		public void setR43_lc_as_on_sep(BigDecimal r43_lc_as_on_sep) {
			this.r43_lc_as_on_sep = r43_lc_as_on_sep;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_lc_as_on_mar() {
			return r44_lc_as_on_mar;
		}

		public void setR44_lc_as_on_mar(BigDecimal r44_lc_as_on_mar) {
			this.r44_lc_as_on_mar = r44_lc_as_on_mar;
		}

		public BigDecimal getR44_lc_as_on_sep() {
			return r44_lc_as_on_sep;
		}

		public void setR44_lc_as_on_sep(BigDecimal r44_lc_as_on_sep) {
			this.r44_lc_as_on_sep = r44_lc_as_on_sep;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_lc_as_on_mar() {
			return r45_lc_as_on_mar;
		}

		public void setR45_lc_as_on_mar(BigDecimal r45_lc_as_on_mar) {
			this.r45_lc_as_on_mar = r45_lc_as_on_mar;
		}

		public BigDecimal getR45_lc_as_on_sep() {
			return r45_lc_as_on_sep;
		}

		public void setR45_lc_as_on_sep(BigDecimal r45_lc_as_on_sep) {
			this.r45_lc_as_on_sep = r45_lc_as_on_sep;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_lc_as_on_mar() {
			return r46_lc_as_on_mar;
		}

		public void setR46_lc_as_on_mar(BigDecimal r46_lc_as_on_mar) {
			this.r46_lc_as_on_mar = r46_lc_as_on_mar;
		}

		public BigDecimal getR46_lc_as_on_sep() {
			return r46_lc_as_on_sep;
		}

		public void setR46_lc_as_on_sep(BigDecimal r46_lc_as_on_sep) {
			this.r46_lc_as_on_sep = r46_lc_as_on_sep;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_lc_as_on_mar() {
			return r47_lc_as_on_mar;
		}

		public void setR47_lc_as_on_mar(BigDecimal r47_lc_as_on_mar) {
			this.r47_lc_as_on_mar = r47_lc_as_on_mar;
		}

		public BigDecimal getR47_lc_as_on_sep() {
			return r47_lc_as_on_sep;
		}

		public void setR47_lc_as_on_sep(BigDecimal r47_lc_as_on_sep) {
			this.r47_lc_as_on_sep = r47_lc_as_on_sep;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_lc_as_on_mar() {
			return r48_lc_as_on_mar;
		}

		public void setR48_lc_as_on_mar(BigDecimal r48_lc_as_on_mar) {
			this.r48_lc_as_on_mar = r48_lc_as_on_mar;
		}

		public BigDecimal getR48_lc_as_on_sep() {
			return r48_lc_as_on_sep;
		}

		public void setR48_lc_as_on_sep(BigDecimal r48_lc_as_on_sep) {
			this.r48_lc_as_on_sep = r48_lc_as_on_sep;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_lc_as_on_mar() {
			return r49_lc_as_on_mar;
		}

		public void setR49_lc_as_on_mar(BigDecimal r49_lc_as_on_mar) {
			this.r49_lc_as_on_mar = r49_lc_as_on_mar;
		}

		public BigDecimal getR49_lc_as_on_sep() {
			return r49_lc_as_on_sep;
		}

		public void setR49_lc_as_on_sep(BigDecimal r49_lc_as_on_sep) {
			this.r49_lc_as_on_sep = r49_lc_as_on_sep;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_lc_as_on_mar() {
			return r50_lc_as_on_mar;
		}

		public void setR50_lc_as_on_mar(BigDecimal r50_lc_as_on_mar) {
			this.r50_lc_as_on_mar = r50_lc_as_on_mar;
		}

		public BigDecimal getR50_lc_as_on_sep() {
			return r50_lc_as_on_sep;
		}

		public void setR50_lc_as_on_sep(BigDecimal r50_lc_as_on_sep) {
			this.r50_lc_as_on_sep = r50_lc_as_on_sep;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_lc_as_on_mar() {
			return r51_lc_as_on_mar;
		}

		public void setR51_lc_as_on_mar(BigDecimal r51_lc_as_on_mar) {
			this.r51_lc_as_on_mar = r51_lc_as_on_mar;
		}

		public BigDecimal getR51_lc_as_on_sep() {
			return r51_lc_as_on_sep;
		}

		public void setR51_lc_as_on_sep(BigDecimal r51_lc_as_on_sep) {
			this.r51_lc_as_on_sep = r51_lc_as_on_sep;
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

	class CashFlowRowArchivalMapper implements RowMapper<CASH_FLOW_Archival_Summary_Entity> {

		@Override
		public CASH_FLOW_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			CASH_FLOW_Archival_Summary_Entity obj = new CASH_FLOW_Archival_Summary_Entity();

			// R9
			obj.setR9_product(rs.getString("R9_PRODUCT"));
			obj.setR9_lc_as_on_mar(rs.getBigDecimal("R9_LC_AS_ON_MAR"));
			obj.setR9_lc_as_on_sep(rs.getBigDecimal("R9_LC_AS_ON_SEP"));

// R10
			obj.setR10_product(rs.getString("R10_PRODUCT"));
			obj.setR10_lc_as_on_mar(rs.getBigDecimal("R10_LC_AS_ON_MAR"));
			obj.setR10_lc_as_on_sep(rs.getBigDecimal("R10_LC_AS_ON_SEP"));

// R11
			obj.setR11_product(rs.getString("R11_PRODUCT"));
			obj.setR11_lc_as_on_mar(rs.getBigDecimal("R11_LC_AS_ON_MAR"));
			obj.setR11_lc_as_on_sep(rs.getBigDecimal("R11_LC_AS_ON_SEP"));

// R12
			obj.setR12_product(rs.getString("R12_PRODUCT"));
			obj.setR12_lc_as_on_mar(rs.getBigDecimal("R12_LC_AS_ON_MAR"));
			obj.setR12_lc_as_on_sep(rs.getBigDecimal("R12_LC_AS_ON_SEP"));

// R13
			obj.setR13_product(rs.getString("R13_PRODUCT"));
			obj.setR13_lc_as_on_mar(rs.getBigDecimal("R13_LC_AS_ON_MAR"));
			obj.setR13_lc_as_on_sep(rs.getBigDecimal("R13_LC_AS_ON_SEP"));

// R14
			obj.setR14_product(rs.getString("R14_PRODUCT"));
			obj.setR14_lc_as_on_mar(rs.getBigDecimal("R14_LC_AS_ON_MAR"));
			obj.setR14_lc_as_on_sep(rs.getBigDecimal("R14_LC_AS_ON_SEP"));

// R15
			obj.setR15_product(rs.getString("R15_PRODUCT"));
			obj.setR15_lc_as_on_mar(rs.getBigDecimal("R15_LC_AS_ON_MAR"));
			obj.setR15_lc_as_on_sep(rs.getBigDecimal("R15_LC_AS_ON_SEP"));

// R16
			obj.setR16_product(rs.getString("R16_PRODUCT"));
			obj.setR16_lc_as_on_mar(rs.getBigDecimal("R16_LC_AS_ON_MAR"));
			obj.setR16_lc_as_on_sep(rs.getBigDecimal("R16_LC_AS_ON_SEP"));

// R17
			obj.setR17_product(rs.getString("R17_PRODUCT"));
			obj.setR17_lc_as_on_mar(rs.getBigDecimal("R17_LC_AS_ON_MAR"));
			obj.setR17_lc_as_on_sep(rs.getBigDecimal("R17_LC_AS_ON_SEP"));

// R18
			obj.setR18_product(rs.getString("R18_PRODUCT"));
			obj.setR18_lc_as_on_mar(rs.getBigDecimal("R18_LC_AS_ON_MAR"));
			obj.setR18_lc_as_on_sep(rs.getBigDecimal("R18_LC_AS_ON_SEP"));

// R19
			obj.setR19_product(rs.getString("R19_PRODUCT"));
			obj.setR19_lc_as_on_mar(rs.getBigDecimal("R19_LC_AS_ON_MAR"));
			obj.setR19_lc_as_on_sep(rs.getBigDecimal("R19_LC_AS_ON_SEP"));

// R20
			obj.setR20_product(rs.getString("R20_PRODUCT"));
			obj.setR20_lc_as_on_mar(rs.getBigDecimal("R20_LC_AS_ON_MAR"));
			obj.setR20_lc_as_on_sep(rs.getBigDecimal("R20_LC_AS_ON_SEP"));

// R21
			obj.setR21_product(rs.getString("R21_PRODUCT"));
			obj.setR21_lc_as_on_mar(rs.getBigDecimal("R21_LC_AS_ON_MAR"));
			obj.setR21_lc_as_on_sep(rs.getBigDecimal("R21_LC_AS_ON_SEP"));

// R22
			obj.setR22_product(rs.getString("R22_PRODUCT"));
			obj.setR22_lc_as_on_mar(rs.getBigDecimal("R22_LC_AS_ON_MAR"));
			obj.setR22_lc_as_on_sep(rs.getBigDecimal("R22_LC_AS_ON_SEP"));

// R23
			obj.setR23_product(rs.getString("R23_PRODUCT"));
			obj.setR23_lc_as_on_mar(rs.getBigDecimal("R23_LC_AS_ON_MAR"));
			obj.setR23_lc_as_on_sep(rs.getBigDecimal("R23_LC_AS_ON_SEP"));

// R24
			obj.setR24_product(rs.getString("R24_PRODUCT"));
			obj.setR24_lc_as_on_mar(rs.getBigDecimal("R24_LC_AS_ON_MAR"));
			obj.setR24_lc_as_on_sep(rs.getBigDecimal("R24_LC_AS_ON_SEP"));

// R25
			obj.setR25_product(rs.getString("R25_PRODUCT"));
			obj.setR25_lc_as_on_mar(rs.getBigDecimal("R25_LC_AS_ON_MAR"));
			obj.setR25_lc_as_on_sep(rs.getBigDecimal("R25_LC_AS_ON_SEP"));

// R26
			obj.setR26_product(rs.getString("R26_PRODUCT"));
			obj.setR26_lc_as_on_mar(rs.getBigDecimal("R26_LC_AS_ON_MAR"));
			obj.setR26_lc_as_on_sep(rs.getBigDecimal("R26_LC_AS_ON_SEP"));

// R27
			obj.setR27_product(rs.getString("R27_PRODUCT"));
			obj.setR27_lc_as_on_mar(rs.getBigDecimal("R27_LC_AS_ON_MAR"));
			obj.setR27_lc_as_on_sep(rs.getBigDecimal("R27_LC_AS_ON_SEP"));

// R28
			obj.setR28_product(rs.getString("R28_PRODUCT"));
			obj.setR28_lc_as_on_mar(rs.getBigDecimal("R28_LC_AS_ON_MAR"));
			obj.setR28_lc_as_on_sep(rs.getBigDecimal("R28_LC_AS_ON_SEP"));

// R29
			obj.setR29_product(rs.getString("R29_PRODUCT"));
			obj.setR29_lc_as_on_mar(rs.getBigDecimal("R29_LC_AS_ON_MAR"));
			obj.setR29_lc_as_on_sep(rs.getBigDecimal("R29_LC_AS_ON_SEP"));

// R30
			obj.setR30_product(rs.getString("R30_PRODUCT"));
			obj.setR30_lc_as_on_mar(rs.getBigDecimal("R30_LC_AS_ON_MAR"));
			obj.setR30_lc_as_on_sep(rs.getBigDecimal("R30_LC_AS_ON_SEP"));

// R31
			obj.setR31_product(rs.getString("R31_PRODUCT"));
			obj.setR31_lc_as_on_mar(rs.getBigDecimal("R31_LC_AS_ON_MAR"));
			obj.setR31_lc_as_on_sep(rs.getBigDecimal("R31_LC_AS_ON_SEP"));

// R32
			obj.setR32_product(rs.getString("R32_PRODUCT"));
			obj.setR32_lc_as_on_mar(rs.getBigDecimal("R32_LC_AS_ON_MAR"));
			obj.setR32_lc_as_on_sep(rs.getBigDecimal("R32_LC_AS_ON_SEP"));

// R33
			obj.setR33_product(rs.getString("R33_PRODUCT"));
			obj.setR33_lc_as_on_mar(rs.getBigDecimal("R33_LC_AS_ON_MAR"));
			obj.setR33_lc_as_on_sep(rs.getBigDecimal("R33_LC_AS_ON_SEP"));

// R34
			obj.setR34_product(rs.getString("R34_PRODUCT"));
			obj.setR34_lc_as_on_mar(rs.getBigDecimal("R34_LC_AS_ON_MAR"));
			obj.setR34_lc_as_on_sep(rs.getBigDecimal("R34_LC_AS_ON_SEP"));

// R35
			obj.setR35_product(rs.getString("R35_PRODUCT"));
			obj.setR35_lc_as_on_mar(rs.getBigDecimal("R35_LC_AS_ON_MAR"));
			obj.setR35_lc_as_on_sep(rs.getBigDecimal("R35_LC_AS_ON_SEP"));

// R36
			obj.setR36_product(rs.getString("R36_PRODUCT"));
			obj.setR36_lc_as_on_mar(rs.getBigDecimal("R36_LC_AS_ON_MAR"));
			obj.setR36_lc_as_on_sep(rs.getBigDecimal("R36_LC_AS_ON_SEP"));

// R37
			obj.setR37_product(rs.getString("R37_PRODUCT"));
			obj.setR37_lc_as_on_mar(rs.getBigDecimal("R37_LC_AS_ON_MAR"));
			obj.setR37_lc_as_on_sep(rs.getBigDecimal("R37_LC_AS_ON_SEP"));

// R38
			obj.setR38_product(rs.getString("R38_PRODUCT"));
			obj.setR38_lc_as_on_mar(rs.getBigDecimal("R38_LC_AS_ON_MAR"));
			obj.setR38_lc_as_on_sep(rs.getBigDecimal("R38_LC_AS_ON_SEP"));

// R39
			obj.setR39_product(rs.getString("R39_PRODUCT"));
			obj.setR39_lc_as_on_mar(rs.getBigDecimal("R39_LC_AS_ON_MAR"));
			obj.setR39_lc_as_on_sep(rs.getBigDecimal("R39_LC_AS_ON_SEP"));

// R40
			obj.setR40_product(rs.getString("R40_PRODUCT"));
			obj.setR40_lc_as_on_mar(rs.getBigDecimal("R40_LC_AS_ON_MAR"));
			obj.setR40_lc_as_on_sep(rs.getBigDecimal("R40_LC_AS_ON_SEP"));

// R41
			obj.setR41_product(rs.getString("R41_PRODUCT"));
			obj.setR41_lc_as_on_mar(rs.getBigDecimal("R41_LC_AS_ON_MAR"));
			obj.setR41_lc_as_on_sep(rs.getBigDecimal("R41_LC_AS_ON_SEP"));

// R42
			obj.setR42_product(rs.getString("R42_PRODUCT"));
			obj.setR42_lc_as_on_mar(rs.getBigDecimal("R42_LC_AS_ON_MAR"));
			obj.setR42_lc_as_on_sep(rs.getBigDecimal("R42_LC_AS_ON_SEP"));

// R43
			obj.setR43_product(rs.getString("R43_PRODUCT"));
			obj.setR43_lc_as_on_mar(rs.getBigDecimal("R43_LC_AS_ON_MAR"));
			obj.setR43_lc_as_on_sep(rs.getBigDecimal("R43_LC_AS_ON_SEP"));

// R44
			obj.setR44_product(rs.getString("R44_PRODUCT"));
			obj.setR44_lc_as_on_mar(rs.getBigDecimal("R44_LC_AS_ON_MAR"));
			obj.setR44_lc_as_on_sep(rs.getBigDecimal("R44_LC_AS_ON_SEP"));

// R45
			obj.setR45_product(rs.getString("R45_PRODUCT"));
			obj.setR45_lc_as_on_mar(rs.getBigDecimal("R45_LC_AS_ON_MAR"));
			obj.setR45_lc_as_on_sep(rs.getBigDecimal("R45_LC_AS_ON_SEP"));

// R46
			obj.setR46_product(rs.getString("R46_PRODUCT"));
			obj.setR46_lc_as_on_mar(rs.getBigDecimal("R46_LC_AS_ON_MAR"));
			obj.setR46_lc_as_on_sep(rs.getBigDecimal("R46_LC_AS_ON_SEP"));

// R47
			obj.setR47_product(rs.getString("R47_PRODUCT"));
			obj.setR47_lc_as_on_mar(rs.getBigDecimal("R47_LC_AS_ON_MAR"));
			obj.setR47_lc_as_on_sep(rs.getBigDecimal("R47_LC_AS_ON_SEP"));

// R48
			obj.setR48_product(rs.getString("R48_PRODUCT"));
			obj.setR48_lc_as_on_mar(rs.getBigDecimal("R48_LC_AS_ON_MAR"));
			obj.setR48_lc_as_on_sep(rs.getBigDecimal("R48_LC_AS_ON_SEP"));

// R49
			obj.setR49_product(rs.getString("R49_PRODUCT"));
			obj.setR49_lc_as_on_mar(rs.getBigDecimal("R49_LC_AS_ON_MAR"));
			obj.setR49_lc_as_on_sep(rs.getBigDecimal("R49_LC_AS_ON_SEP"));

// R50
			obj.setR50_product(rs.getString("R50_PRODUCT"));
			obj.setR50_lc_as_on_mar(rs.getBigDecimal("R50_LC_AS_ON_MAR"));
			obj.setR50_lc_as_on_sep(rs.getBigDecimal("R50_LC_AS_ON_SEP"));

// R51
			obj.setR51_product(rs.getString("R51_PRODUCT"));
			obj.setR51_lc_as_on_mar(rs.getBigDecimal("R51_LC_AS_ON_MAR"));
			obj.setR51_lc_as_on_sep(rs.getBigDecimal("R51_LC_AS_ON_SEP"));

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

	@IdClass(CASH_FLOW_PK.class)
	public class CASH_FLOW_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		@Column(name = "R9_PRODUCT")
		private String r9_product;
		@Column(name = "R9_LC_AS_ON_MAR")
		private BigDecimal r9_lc_as_on_mar;
		@Column(name = "R9_LC_AS_ON_SEP")
		private BigDecimal r9_lc_as_on_sep;

		@Column(name = "R10_PRODUCT")
		private String r10_product;
		@Column(name = "R10_LC_AS_ON_MAR")
		private BigDecimal r10_lc_as_on_mar;
		@Column(name = "R10_LC_AS_ON_SEP")
		private BigDecimal r10_lc_as_on_sep;

		@Column(name = "R11_PRODUCT")
		private String r11_product;
		@Column(name = "R11_LC_AS_ON_MAR")
		private BigDecimal r11_lc_as_on_mar;
		@Column(name = "R11_LC_AS_ON_SEP")
		private BigDecimal r11_lc_as_on_sep;

		@Column(name = "R12_PRODUCT")
		private String r12_product;
		@Column(name = "R12_LC_AS_ON_MAR")
		private BigDecimal r12_lc_as_on_mar;
		@Column(name = "R12_LC_AS_ON_SEP")
		private BigDecimal r12_lc_as_on_sep;

		@Column(name = "R13_PRODUCT")
		private String r13_product;
		@Column(name = "R13_LC_AS_ON_MAR")
		private BigDecimal r13_lc_as_on_mar;
		@Column(name = "R13_LC_AS_ON_SEP")
		private BigDecimal r13_lc_as_on_sep;

		@Column(name = "R14_PRODUCT")
		private String r14_product;
		@Column(name = "R14_LC_AS_ON_MAR")
		private BigDecimal r14_lc_as_on_mar;
		@Column(name = "R14_LC_AS_ON_SEP")
		private BigDecimal r14_lc_as_on_sep;

		@Column(name = "R15_PRODUCT")
		private String r15_product;
		@Column(name = "R15_LC_AS_ON_MAR")
		private BigDecimal r15_lc_as_on_mar;
		@Column(name = "R15_LC_AS_ON_SEP")
		private BigDecimal r15_lc_as_on_sep;

		@Column(name = "R16_PRODUCT")
		private String r16_product;
		@Column(name = "R16_LC_AS_ON_MAR")
		private BigDecimal r16_lc_as_on_mar;
		@Column(name = "R16_LC_AS_ON_SEP")
		private BigDecimal r16_lc_as_on_sep;

		@Column(name = "R17_PRODUCT")
		private String r17_product;
		@Column(name = "R17_LC_AS_ON_MAR")
		private BigDecimal r17_lc_as_on_mar;
		@Column(name = "R17_LC_AS_ON_SEP")
		private BigDecimal r17_lc_as_on_sep;

		@Column(name = "R18_PRODUCT")
		private String r18_product;
		@Column(name = "R18_LC_AS_ON_MAR")
		private BigDecimal r18_lc_as_on_mar;
		@Column(name = "R18_LC_AS_ON_SEP")
		private BigDecimal r18_lc_as_on_sep;

		@Column(name = "R19_PRODUCT")
		private String r19_product;
		@Column(name = "R19_LC_AS_ON_MAR")
		private BigDecimal r19_lc_as_on_mar;
		@Column(name = "R19_LC_AS_ON_SEP")
		private BigDecimal r19_lc_as_on_sep;

		@Column(name = "R20_PRODUCT")
		private String r20_product;
		@Column(name = "R20_LC_AS_ON_MAR")
		private BigDecimal r20_lc_as_on_mar;
		@Column(name = "R20_LC_AS_ON_SEP")
		private BigDecimal r20_lc_as_on_sep;

		@Column(name = "R21_PRODUCT")
		private String r21_product;
		@Column(name = "R21_LC_AS_ON_MAR")
		private BigDecimal r21_lc_as_on_mar;
		@Column(name = "R21_LC_AS_ON_SEP")
		private BigDecimal r21_lc_as_on_sep;

		@Column(name = "R22_PRODUCT")
		private String r22_product;
		@Column(name = "R22_LC_AS_ON_MAR")
		private BigDecimal r22_lc_as_on_mar;
		@Column(name = "R22_LC_AS_ON_SEP")
		private BigDecimal r22_lc_as_on_sep;

		@Column(name = "R23_PRODUCT")
		private String r23_product;
		@Column(name = "R23_LC_AS_ON_MAR")
		private BigDecimal r23_lc_as_on_mar;
		@Column(name = "R23_LC_AS_ON_SEP")
		private BigDecimal r23_lc_as_on_sep;

		@Column(name = "R24_PRODUCT")
		private String r24_product;
		@Column(name = "R24_LC_AS_ON_MAR")
		private BigDecimal r24_lc_as_on_mar;
		@Column(name = "R24_LC_AS_ON_SEP")
		private BigDecimal r24_lc_as_on_sep;

		@Column(name = "R25_PRODUCT")
		private String r25_product;
		@Column(name = "R25_LC_AS_ON_MAR")
		private BigDecimal r25_lc_as_on_mar;
		@Column(name = "R25_LC_AS_ON_SEP")
		private BigDecimal r25_lc_as_on_sep;

		@Column(name = "R26_PRODUCT")
		private String r26_product;
		@Column(name = "R26_LC_AS_ON_MAR")
		private BigDecimal r26_lc_as_on_mar;
		@Column(name = "R26_LC_AS_ON_SEP")
		private BigDecimal r26_lc_as_on_sep;

		@Column(name = "R27_PRODUCT")
		private String r27_product;
		@Column(name = "R27_LC_AS_ON_MAR")
		private BigDecimal r27_lc_as_on_mar;
		@Column(name = "R27_LC_AS_ON_SEP")
		private BigDecimal r27_lc_as_on_sep;

		@Column(name = "R28_PRODUCT")
		private String r28_product;
		@Column(name = "R28_LC_AS_ON_MAR")
		private BigDecimal r28_lc_as_on_mar;
		@Column(name = "R28_LC_AS_ON_SEP")
		private BigDecimal r28_lc_as_on_sep;

		@Column(name = "R29_PRODUCT")
		private String r29_product;
		@Column(name = "R29_LC_AS_ON_MAR")
		private BigDecimal r29_lc_as_on_mar;
		@Column(name = "R29_LC_AS_ON_SEP")
		private BigDecimal r29_lc_as_on_sep;

		@Column(name = "R30_PRODUCT")
		private String r30_product;
		@Column(name = "R30_LC_AS_ON_MAR")
		private BigDecimal r30_lc_as_on_mar;
		@Column(name = "R30_LC_AS_ON_SEP")
		private BigDecimal r30_lc_as_on_sep;

		@Column(name = "R31_PRODUCT")
		private String r31_product;
		@Column(name = "R31_LC_AS_ON_MAR")
		private BigDecimal r31_lc_as_on_mar;
		@Column(name = "R31_LC_AS_ON_SEP")
		private BigDecimal r31_lc_as_on_sep;

		@Column(name = "R32_PRODUCT")
		private String r32_product;
		@Column(name = "R32_LC_AS_ON_MAR")
		private BigDecimal r32_lc_as_on_mar;
		@Column(name = "R32_LC_AS_ON_SEP")
		private BigDecimal r32_lc_as_on_sep;

		@Column(name = "R33_PRODUCT")
		private String r33_product;
		@Column(name = "R33_LC_AS_ON_MAR")
		private BigDecimal r33_lc_as_on_mar;
		@Column(name = "R33_LC_AS_ON_SEP")
		private BigDecimal r33_lc_as_on_sep;

		@Column(name = "R34_PRODUCT")
		private String r34_product;
		@Column(name = "R34_LC_AS_ON_MAR")
		private BigDecimal r34_lc_as_on_mar;
		@Column(name = "R34_LC_AS_ON_SEP")
		private BigDecimal r34_lc_as_on_sep;

		@Column(name = "R35_PRODUCT")
		private String r35_product;
		@Column(name = "R35_LC_AS_ON_MAR")
		private BigDecimal r35_lc_as_on_mar;
		@Column(name = "R35_LC_AS_ON_SEP")
		private BigDecimal r35_lc_as_on_sep;

		@Column(name = "R36_PRODUCT")
		private String r36_product;
		@Column(name = "R36_LC_AS_ON_MAR")
		private BigDecimal r36_lc_as_on_mar;
		@Column(name = "R36_LC_AS_ON_SEP")
		private BigDecimal r36_lc_as_on_sep;

		@Column(name = "R37_PRODUCT")
		private String r37_product;
		@Column(name = "R37_LC_AS_ON_MAR")
		private BigDecimal r37_lc_as_on_mar;
		@Column(name = "R37_LC_AS_ON_SEP")
		private BigDecimal r37_lc_as_on_sep;

		@Column(name = "R38_PRODUCT")
		private String r38_product;
		@Column(name = "R38_LC_AS_ON_MAR")
		private BigDecimal r38_lc_as_on_mar;
		@Column(name = "R38_LC_AS_ON_SEP")
		private BigDecimal r38_lc_as_on_sep;

		@Column(name = "R39_PRODUCT")
		private String r39_product;
		@Column(name = "R39_LC_AS_ON_MAR")
		private BigDecimal r39_lc_as_on_mar;
		@Column(name = "R39_LC_AS_ON_SEP")
		private BigDecimal r39_lc_as_on_sep;

		@Column(name = "R40_PRODUCT")
		private String r40_product;
		@Column(name = "R40_LC_AS_ON_MAR")
		private BigDecimal r40_lc_as_on_mar;
		@Column(name = "R40_LC_AS_ON_SEP")
		private BigDecimal r40_lc_as_on_sep;

		@Column(name = "R41_PRODUCT")
		private String r41_product;
		@Column(name = "R41_LC_AS_ON_MAR")
		private BigDecimal r41_lc_as_on_mar;
		@Column(name = "R41_LC_AS_ON_SEP")
		private BigDecimal r41_lc_as_on_sep;

		@Column(name = "R42_PRODUCT")
		private String r42_product;
		@Column(name = "R42_LC_AS_ON_MAR")
		private BigDecimal r42_lc_as_on_mar;
		@Column(name = "R42_LC_AS_ON_SEP")
		private BigDecimal r42_lc_as_on_sep;

		@Column(name = "R43_PRODUCT")
		private String r43_product;
		@Column(name = "R43_LC_AS_ON_MAR")
		private BigDecimal r43_lc_as_on_mar;
		@Column(name = "R43_LC_AS_ON_SEP")
		private BigDecimal r43_lc_as_on_sep;

		@Column(name = "R44_PRODUCT")
		private String r44_product;
		@Column(name = "R44_LC_AS_ON_MAR")
		private BigDecimal r44_lc_as_on_mar;
		@Column(name = "R44_LC_AS_ON_SEP")
		private BigDecimal r44_lc_as_on_sep;

		@Column(name = "R45_PRODUCT")
		private String r45_product;
		@Column(name = "R45_LC_AS_ON_MAR")
		private BigDecimal r45_lc_as_on_mar;
		@Column(name = "R45_LC_AS_ON_SEP")
		private BigDecimal r45_lc_as_on_sep;

		@Column(name = "R46_PRODUCT")
		private String r46_product;
		@Column(name = "R46_LC_AS_ON_MAR")
		private BigDecimal r46_lc_as_on_mar;
		@Column(name = "R46_LC_AS_ON_SEP")
		private BigDecimal r46_lc_as_on_sep;

		@Column(name = "R47_PRODUCT")
		private String r47_product;
		@Column(name = "R47_LC_AS_ON_MAR")
		private BigDecimal r47_lc_as_on_mar;
		@Column(name = "R47_LC_AS_ON_SEP")
		private BigDecimal r47_lc_as_on_sep;

		@Column(name = "R48_PRODUCT")
		private String r48_product;
		@Column(name = "R48_LC_AS_ON_MAR")
		private BigDecimal r48_lc_as_on_mar;
		@Column(name = "R48_LC_AS_ON_SEP")
		private BigDecimal r48_lc_as_on_sep;

		@Column(name = "R49_PRODUCT")
		private String r49_product;
		@Column(name = "R49_LC_AS_ON_MAR")
		private BigDecimal r49_lc_as_on_mar;
		@Column(name = "R49_LC_AS_ON_SEP")
		private BigDecimal r49_lc_as_on_sep;

		@Column(name = "R50_PRODUCT")
		private String r50_product;
		@Column(name = "R50_LC_AS_ON_MAR")
		private BigDecimal r50_lc_as_on_mar;
		@Column(name = "R50_LC_AS_ON_SEP")
		private BigDecimal r50_lc_as_on_sep;

		@Column(name = "R51_PRODUCT")
		private String r51_product;
		@Column(name = "R51_LC_AS_ON_MAR")
		private BigDecimal r51_lc_as_on_mar;
		@Column(name = "R51_LC_AS_ON_SEP")
		private BigDecimal r51_lc_as_on_sep;
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

		public String getR9_product() {
			return r9_product;
		}

		public void setR9_product(String r9_product) {
			this.r9_product = r9_product;
		}

		public BigDecimal getR9_lc_as_on_mar() {
			return r9_lc_as_on_mar;
		}

		public void setR9_lc_as_on_mar(BigDecimal r9_lc_as_on_mar) {
			this.r9_lc_as_on_mar = r9_lc_as_on_mar;
		}

		public BigDecimal getR9_lc_as_on_sep() {
			return r9_lc_as_on_sep;
		}

		public void setR9_lc_as_on_sep(BigDecimal r9_lc_as_on_sep) {
			this.r9_lc_as_on_sep = r9_lc_as_on_sep;
		}

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_lc_as_on_mar() {
			return r10_lc_as_on_mar;
		}

		public void setR10_lc_as_on_mar(BigDecimal r10_lc_as_on_mar) {
			this.r10_lc_as_on_mar = r10_lc_as_on_mar;
		}

		public BigDecimal getR10_lc_as_on_sep() {
			return r10_lc_as_on_sep;
		}

		public void setR10_lc_as_on_sep(BigDecimal r10_lc_as_on_sep) {
			this.r10_lc_as_on_sep = r10_lc_as_on_sep;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_lc_as_on_mar() {
			return r11_lc_as_on_mar;
		}

		public void setR11_lc_as_on_mar(BigDecimal r11_lc_as_on_mar) {
			this.r11_lc_as_on_mar = r11_lc_as_on_mar;
		}

		public BigDecimal getR11_lc_as_on_sep() {
			return r11_lc_as_on_sep;
		}

		public void setR11_lc_as_on_sep(BigDecimal r11_lc_as_on_sep) {
			this.r11_lc_as_on_sep = r11_lc_as_on_sep;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_lc_as_on_mar() {
			return r12_lc_as_on_mar;
		}

		public void setR12_lc_as_on_mar(BigDecimal r12_lc_as_on_mar) {
			this.r12_lc_as_on_mar = r12_lc_as_on_mar;
		}

		public BigDecimal getR12_lc_as_on_sep() {
			return r12_lc_as_on_sep;
		}

		public void setR12_lc_as_on_sep(BigDecimal r12_lc_as_on_sep) {
			this.r12_lc_as_on_sep = r12_lc_as_on_sep;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_lc_as_on_mar() {
			return r13_lc_as_on_mar;
		}

		public void setR13_lc_as_on_mar(BigDecimal r13_lc_as_on_mar) {
			this.r13_lc_as_on_mar = r13_lc_as_on_mar;
		}

		public BigDecimal getR13_lc_as_on_sep() {
			return r13_lc_as_on_sep;
		}

		public void setR13_lc_as_on_sep(BigDecimal r13_lc_as_on_sep) {
			this.r13_lc_as_on_sep = r13_lc_as_on_sep;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_lc_as_on_mar() {
			return r14_lc_as_on_mar;
		}

		public void setR14_lc_as_on_mar(BigDecimal r14_lc_as_on_mar) {
			this.r14_lc_as_on_mar = r14_lc_as_on_mar;
		}

		public BigDecimal getR14_lc_as_on_sep() {
			return r14_lc_as_on_sep;
		}

		public void setR14_lc_as_on_sep(BigDecimal r14_lc_as_on_sep) {
			this.r14_lc_as_on_sep = r14_lc_as_on_sep;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_lc_as_on_mar() {
			return r15_lc_as_on_mar;
		}

		public void setR15_lc_as_on_mar(BigDecimal r15_lc_as_on_mar) {
			this.r15_lc_as_on_mar = r15_lc_as_on_mar;
		}

		public BigDecimal getR15_lc_as_on_sep() {
			return r15_lc_as_on_sep;
		}

		public void setR15_lc_as_on_sep(BigDecimal r15_lc_as_on_sep) {
			this.r15_lc_as_on_sep = r15_lc_as_on_sep;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_lc_as_on_mar() {
			return r16_lc_as_on_mar;
		}

		public void setR16_lc_as_on_mar(BigDecimal r16_lc_as_on_mar) {
			this.r16_lc_as_on_mar = r16_lc_as_on_mar;
		}

		public BigDecimal getR16_lc_as_on_sep() {
			return r16_lc_as_on_sep;
		}

		public void setR16_lc_as_on_sep(BigDecimal r16_lc_as_on_sep) {
			this.r16_lc_as_on_sep = r16_lc_as_on_sep;
		}

		public String getR17_product() {
			return r17_product;
		}

		public void setR17_product(String r17_product) {
			this.r17_product = r17_product;
		}

		public BigDecimal getR17_lc_as_on_mar() {
			return r17_lc_as_on_mar;
		}

		public void setR17_lc_as_on_mar(BigDecimal r17_lc_as_on_mar) {
			this.r17_lc_as_on_mar = r17_lc_as_on_mar;
		}

		public BigDecimal getR17_lc_as_on_sep() {
			return r17_lc_as_on_sep;
		}

		public void setR17_lc_as_on_sep(BigDecimal r17_lc_as_on_sep) {
			this.r17_lc_as_on_sep = r17_lc_as_on_sep;
		}

		public String getR18_product() {
			return r18_product;
		}

		public void setR18_product(String r18_product) {
			this.r18_product = r18_product;
		}

		public BigDecimal getR18_lc_as_on_mar() {
			return r18_lc_as_on_mar;
		}

		public void setR18_lc_as_on_mar(BigDecimal r18_lc_as_on_mar) {
			this.r18_lc_as_on_mar = r18_lc_as_on_mar;
		}

		public BigDecimal getR18_lc_as_on_sep() {
			return r18_lc_as_on_sep;
		}

		public void setR18_lc_as_on_sep(BigDecimal r18_lc_as_on_sep) {
			this.r18_lc_as_on_sep = r18_lc_as_on_sep;
		}

		public String getR19_product() {
			return r19_product;
		}

		public void setR19_product(String r19_product) {
			this.r19_product = r19_product;
		}

		public BigDecimal getR19_lc_as_on_mar() {
			return r19_lc_as_on_mar;
		}

		public void setR19_lc_as_on_mar(BigDecimal r19_lc_as_on_mar) {
			this.r19_lc_as_on_mar = r19_lc_as_on_mar;
		}

		public BigDecimal getR19_lc_as_on_sep() {
			return r19_lc_as_on_sep;
		}

		public void setR19_lc_as_on_sep(BigDecimal r19_lc_as_on_sep) {
			this.r19_lc_as_on_sep = r19_lc_as_on_sep;
		}

		public String getR20_product() {
			return r20_product;
		}

		public void setR20_product(String r20_product) {
			this.r20_product = r20_product;
		}

		public BigDecimal getR20_lc_as_on_mar() {
			return r20_lc_as_on_mar;
		}

		public void setR20_lc_as_on_mar(BigDecimal r20_lc_as_on_mar) {
			this.r20_lc_as_on_mar = r20_lc_as_on_mar;
		}

		public BigDecimal getR20_lc_as_on_sep() {
			return r20_lc_as_on_sep;
		}

		public void setR20_lc_as_on_sep(BigDecimal r20_lc_as_on_sep) {
			this.r20_lc_as_on_sep = r20_lc_as_on_sep;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_lc_as_on_mar() {
			return r21_lc_as_on_mar;
		}

		public void setR21_lc_as_on_mar(BigDecimal r21_lc_as_on_mar) {
			this.r21_lc_as_on_mar = r21_lc_as_on_mar;
		}

		public BigDecimal getR21_lc_as_on_sep() {
			return r21_lc_as_on_sep;
		}

		public void setR21_lc_as_on_sep(BigDecimal r21_lc_as_on_sep) {
			this.r21_lc_as_on_sep = r21_lc_as_on_sep;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_lc_as_on_mar() {
			return r22_lc_as_on_mar;
		}

		public void setR22_lc_as_on_mar(BigDecimal r22_lc_as_on_mar) {
			this.r22_lc_as_on_mar = r22_lc_as_on_mar;
		}

		public BigDecimal getR22_lc_as_on_sep() {
			return r22_lc_as_on_sep;
		}

		public void setR22_lc_as_on_sep(BigDecimal r22_lc_as_on_sep) {
			this.r22_lc_as_on_sep = r22_lc_as_on_sep;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_lc_as_on_mar() {
			return r23_lc_as_on_mar;
		}

		public void setR23_lc_as_on_mar(BigDecimal r23_lc_as_on_mar) {
			this.r23_lc_as_on_mar = r23_lc_as_on_mar;
		}

		public BigDecimal getR23_lc_as_on_sep() {
			return r23_lc_as_on_sep;
		}

		public void setR23_lc_as_on_sep(BigDecimal r23_lc_as_on_sep) {
			this.r23_lc_as_on_sep = r23_lc_as_on_sep;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_lc_as_on_mar() {
			return r24_lc_as_on_mar;
		}

		public void setR24_lc_as_on_mar(BigDecimal r24_lc_as_on_mar) {
			this.r24_lc_as_on_mar = r24_lc_as_on_mar;
		}

		public BigDecimal getR24_lc_as_on_sep() {
			return r24_lc_as_on_sep;
		}

		public void setR24_lc_as_on_sep(BigDecimal r24_lc_as_on_sep) {
			this.r24_lc_as_on_sep = r24_lc_as_on_sep;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_lc_as_on_mar() {
			return r25_lc_as_on_mar;
		}

		public void setR25_lc_as_on_mar(BigDecimal r25_lc_as_on_mar) {
			this.r25_lc_as_on_mar = r25_lc_as_on_mar;
		}

		public BigDecimal getR25_lc_as_on_sep() {
			return r25_lc_as_on_sep;
		}

		public void setR25_lc_as_on_sep(BigDecimal r25_lc_as_on_sep) {
			this.r25_lc_as_on_sep = r25_lc_as_on_sep;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_lc_as_on_mar() {
			return r26_lc_as_on_mar;
		}

		public void setR26_lc_as_on_mar(BigDecimal r26_lc_as_on_mar) {
			this.r26_lc_as_on_mar = r26_lc_as_on_mar;
		}

		public BigDecimal getR26_lc_as_on_sep() {
			return r26_lc_as_on_sep;
		}

		public void setR26_lc_as_on_sep(BigDecimal r26_lc_as_on_sep) {
			this.r26_lc_as_on_sep = r26_lc_as_on_sep;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_lc_as_on_mar() {
			return r27_lc_as_on_mar;
		}

		public void setR27_lc_as_on_mar(BigDecimal r27_lc_as_on_mar) {
			this.r27_lc_as_on_mar = r27_lc_as_on_mar;
		}

		public BigDecimal getR27_lc_as_on_sep() {
			return r27_lc_as_on_sep;
		}

		public void setR27_lc_as_on_sep(BigDecimal r27_lc_as_on_sep) {
			this.r27_lc_as_on_sep = r27_lc_as_on_sep;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_lc_as_on_mar() {
			return r28_lc_as_on_mar;
		}

		public void setR28_lc_as_on_mar(BigDecimal r28_lc_as_on_mar) {
			this.r28_lc_as_on_mar = r28_lc_as_on_mar;
		}

		public BigDecimal getR28_lc_as_on_sep() {
			return r28_lc_as_on_sep;
		}

		public void setR28_lc_as_on_sep(BigDecimal r28_lc_as_on_sep) {
			this.r28_lc_as_on_sep = r28_lc_as_on_sep;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_lc_as_on_mar() {
			return r29_lc_as_on_mar;
		}

		public void setR29_lc_as_on_mar(BigDecimal r29_lc_as_on_mar) {
			this.r29_lc_as_on_mar = r29_lc_as_on_mar;
		}

		public BigDecimal getR29_lc_as_on_sep() {
			return r29_lc_as_on_sep;
		}

		public void setR29_lc_as_on_sep(BigDecimal r29_lc_as_on_sep) {
			this.r29_lc_as_on_sep = r29_lc_as_on_sep;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_lc_as_on_mar() {
			return r30_lc_as_on_mar;
		}

		public void setR30_lc_as_on_mar(BigDecimal r30_lc_as_on_mar) {
			this.r30_lc_as_on_mar = r30_lc_as_on_mar;
		}

		public BigDecimal getR30_lc_as_on_sep() {
			return r30_lc_as_on_sep;
		}

		public void setR30_lc_as_on_sep(BigDecimal r30_lc_as_on_sep) {
			this.r30_lc_as_on_sep = r30_lc_as_on_sep;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_lc_as_on_mar() {
			return r31_lc_as_on_mar;
		}

		public void setR31_lc_as_on_mar(BigDecimal r31_lc_as_on_mar) {
			this.r31_lc_as_on_mar = r31_lc_as_on_mar;
		}

		public BigDecimal getR31_lc_as_on_sep() {
			return r31_lc_as_on_sep;
		}

		public void setR31_lc_as_on_sep(BigDecimal r31_lc_as_on_sep) {
			this.r31_lc_as_on_sep = r31_lc_as_on_sep;
		}

		public String getR32_product() {
			return r32_product;
		}

		public void setR32_product(String r32_product) {
			this.r32_product = r32_product;
		}

		public BigDecimal getR32_lc_as_on_mar() {
			return r32_lc_as_on_mar;
		}

		public void setR32_lc_as_on_mar(BigDecimal r32_lc_as_on_mar) {
			this.r32_lc_as_on_mar = r32_lc_as_on_mar;
		}

		public BigDecimal getR32_lc_as_on_sep() {
			return r32_lc_as_on_sep;
		}

		public void setR32_lc_as_on_sep(BigDecimal r32_lc_as_on_sep) {
			this.r32_lc_as_on_sep = r32_lc_as_on_sep;
		}

		public String getR33_product() {
			return r33_product;
		}

		public void setR33_product(String r33_product) {
			this.r33_product = r33_product;
		}

		public BigDecimal getR33_lc_as_on_mar() {
			return r33_lc_as_on_mar;
		}

		public void setR33_lc_as_on_mar(BigDecimal r33_lc_as_on_mar) {
			this.r33_lc_as_on_mar = r33_lc_as_on_mar;
		}

		public BigDecimal getR33_lc_as_on_sep() {
			return r33_lc_as_on_sep;
		}

		public void setR33_lc_as_on_sep(BigDecimal r33_lc_as_on_sep) {
			this.r33_lc_as_on_sep = r33_lc_as_on_sep;
		}

		public String getR34_product() {
			return r34_product;
		}

		public void setR34_product(String r34_product) {
			this.r34_product = r34_product;
		}

		public BigDecimal getR34_lc_as_on_mar() {
			return r34_lc_as_on_mar;
		}

		public void setR34_lc_as_on_mar(BigDecimal r34_lc_as_on_mar) {
			this.r34_lc_as_on_mar = r34_lc_as_on_mar;
		}

		public BigDecimal getR34_lc_as_on_sep() {
			return r34_lc_as_on_sep;
		}

		public void setR34_lc_as_on_sep(BigDecimal r34_lc_as_on_sep) {
			this.r34_lc_as_on_sep = r34_lc_as_on_sep;
		}

		public String getR35_product() {
			return r35_product;
		}

		public void setR35_product(String r35_product) {
			this.r35_product = r35_product;
		}

		public BigDecimal getR35_lc_as_on_mar() {
			return r35_lc_as_on_mar;
		}

		public void setR35_lc_as_on_mar(BigDecimal r35_lc_as_on_mar) {
			this.r35_lc_as_on_mar = r35_lc_as_on_mar;
		}

		public BigDecimal getR35_lc_as_on_sep() {
			return r35_lc_as_on_sep;
		}

		public void setR35_lc_as_on_sep(BigDecimal r35_lc_as_on_sep) {
			this.r35_lc_as_on_sep = r35_lc_as_on_sep;
		}

		public String getR36_product() {
			return r36_product;
		}

		public void setR36_product(String r36_product) {
			this.r36_product = r36_product;
		}

		public BigDecimal getR36_lc_as_on_mar() {
			return r36_lc_as_on_mar;
		}

		public void setR36_lc_as_on_mar(BigDecimal r36_lc_as_on_mar) {
			this.r36_lc_as_on_mar = r36_lc_as_on_mar;
		}

		public BigDecimal getR36_lc_as_on_sep() {
			return r36_lc_as_on_sep;
		}

		public void setR36_lc_as_on_sep(BigDecimal r36_lc_as_on_sep) {
			this.r36_lc_as_on_sep = r36_lc_as_on_sep;
		}

		public String getR37_product() {
			return r37_product;
		}

		public void setR37_product(String r37_product) {
			this.r37_product = r37_product;
		}

		public BigDecimal getR37_lc_as_on_mar() {
			return r37_lc_as_on_mar;
		}

		public void setR37_lc_as_on_mar(BigDecimal r37_lc_as_on_mar) {
			this.r37_lc_as_on_mar = r37_lc_as_on_mar;
		}

		public BigDecimal getR37_lc_as_on_sep() {
			return r37_lc_as_on_sep;
		}

		public void setR37_lc_as_on_sep(BigDecimal r37_lc_as_on_sep) {
			this.r37_lc_as_on_sep = r37_lc_as_on_sep;
		}

		public String getR38_product() {
			return r38_product;
		}

		public void setR38_product(String r38_product) {
			this.r38_product = r38_product;
		}

		public BigDecimal getR38_lc_as_on_mar() {
			return r38_lc_as_on_mar;
		}

		public void setR38_lc_as_on_mar(BigDecimal r38_lc_as_on_mar) {
			this.r38_lc_as_on_mar = r38_lc_as_on_mar;
		}

		public BigDecimal getR38_lc_as_on_sep() {
			return r38_lc_as_on_sep;
		}

		public void setR38_lc_as_on_sep(BigDecimal r38_lc_as_on_sep) {
			this.r38_lc_as_on_sep = r38_lc_as_on_sep;
		}

		public String getR39_product() {
			return r39_product;
		}

		public void setR39_product(String r39_product) {
			this.r39_product = r39_product;
		}

		public BigDecimal getR39_lc_as_on_mar() {
			return r39_lc_as_on_mar;
		}

		public void setR39_lc_as_on_mar(BigDecimal r39_lc_as_on_mar) {
			this.r39_lc_as_on_mar = r39_lc_as_on_mar;
		}

		public BigDecimal getR39_lc_as_on_sep() {
			return r39_lc_as_on_sep;
		}

		public void setR39_lc_as_on_sep(BigDecimal r39_lc_as_on_sep) {
			this.r39_lc_as_on_sep = r39_lc_as_on_sep;
		}

		public String getR40_product() {
			return r40_product;
		}

		public void setR40_product(String r40_product) {
			this.r40_product = r40_product;
		}

		public BigDecimal getR40_lc_as_on_mar() {
			return r40_lc_as_on_mar;
		}

		public void setR40_lc_as_on_mar(BigDecimal r40_lc_as_on_mar) {
			this.r40_lc_as_on_mar = r40_lc_as_on_mar;
		}

		public BigDecimal getR40_lc_as_on_sep() {
			return r40_lc_as_on_sep;
		}

		public void setR40_lc_as_on_sep(BigDecimal r40_lc_as_on_sep) {
			this.r40_lc_as_on_sep = r40_lc_as_on_sep;
		}

		public String getR41_product() {
			return r41_product;
		}

		public void setR41_product(String r41_product) {
			this.r41_product = r41_product;
		}

		public BigDecimal getR41_lc_as_on_mar() {
			return r41_lc_as_on_mar;
		}

		public void setR41_lc_as_on_mar(BigDecimal r41_lc_as_on_mar) {
			this.r41_lc_as_on_mar = r41_lc_as_on_mar;
		}

		public BigDecimal getR41_lc_as_on_sep() {
			return r41_lc_as_on_sep;
		}

		public void setR41_lc_as_on_sep(BigDecimal r41_lc_as_on_sep) {
			this.r41_lc_as_on_sep = r41_lc_as_on_sep;
		}

		public String getR42_product() {
			return r42_product;
		}

		public void setR42_product(String r42_product) {
			this.r42_product = r42_product;
		}

		public BigDecimal getR42_lc_as_on_mar() {
			return r42_lc_as_on_mar;
		}

		public void setR42_lc_as_on_mar(BigDecimal r42_lc_as_on_mar) {
			this.r42_lc_as_on_mar = r42_lc_as_on_mar;
		}

		public BigDecimal getR42_lc_as_on_sep() {
			return r42_lc_as_on_sep;
		}

		public void setR42_lc_as_on_sep(BigDecimal r42_lc_as_on_sep) {
			this.r42_lc_as_on_sep = r42_lc_as_on_sep;
		}

		public String getR43_product() {
			return r43_product;
		}

		public void setR43_product(String r43_product) {
			this.r43_product = r43_product;
		}

		public BigDecimal getR43_lc_as_on_mar() {
			return r43_lc_as_on_mar;
		}

		public void setR43_lc_as_on_mar(BigDecimal r43_lc_as_on_mar) {
			this.r43_lc_as_on_mar = r43_lc_as_on_mar;
		}

		public BigDecimal getR43_lc_as_on_sep() {
			return r43_lc_as_on_sep;
		}

		public void setR43_lc_as_on_sep(BigDecimal r43_lc_as_on_sep) {
			this.r43_lc_as_on_sep = r43_lc_as_on_sep;
		}

		public String getR44_product() {
			return r44_product;
		}

		public void setR44_product(String r44_product) {
			this.r44_product = r44_product;
		}

		public BigDecimal getR44_lc_as_on_mar() {
			return r44_lc_as_on_mar;
		}

		public void setR44_lc_as_on_mar(BigDecimal r44_lc_as_on_mar) {
			this.r44_lc_as_on_mar = r44_lc_as_on_mar;
		}

		public BigDecimal getR44_lc_as_on_sep() {
			return r44_lc_as_on_sep;
		}

		public void setR44_lc_as_on_sep(BigDecimal r44_lc_as_on_sep) {
			this.r44_lc_as_on_sep = r44_lc_as_on_sep;
		}

		public String getR45_product() {
			return r45_product;
		}

		public void setR45_product(String r45_product) {
			this.r45_product = r45_product;
		}

		public BigDecimal getR45_lc_as_on_mar() {
			return r45_lc_as_on_mar;
		}

		public void setR45_lc_as_on_mar(BigDecimal r45_lc_as_on_mar) {
			this.r45_lc_as_on_mar = r45_lc_as_on_mar;
		}

		public BigDecimal getR45_lc_as_on_sep() {
			return r45_lc_as_on_sep;
		}

		public void setR45_lc_as_on_sep(BigDecimal r45_lc_as_on_sep) {
			this.r45_lc_as_on_sep = r45_lc_as_on_sep;
		}

		public String getR46_product() {
			return r46_product;
		}

		public void setR46_product(String r46_product) {
			this.r46_product = r46_product;
		}

		public BigDecimal getR46_lc_as_on_mar() {
			return r46_lc_as_on_mar;
		}

		public void setR46_lc_as_on_mar(BigDecimal r46_lc_as_on_mar) {
			this.r46_lc_as_on_mar = r46_lc_as_on_mar;
		}

		public BigDecimal getR46_lc_as_on_sep() {
			return r46_lc_as_on_sep;
		}

		public void setR46_lc_as_on_sep(BigDecimal r46_lc_as_on_sep) {
			this.r46_lc_as_on_sep = r46_lc_as_on_sep;
		}

		public String getR47_product() {
			return r47_product;
		}

		public void setR47_product(String r47_product) {
			this.r47_product = r47_product;
		}

		public BigDecimal getR47_lc_as_on_mar() {
			return r47_lc_as_on_mar;
		}

		public void setR47_lc_as_on_mar(BigDecimal r47_lc_as_on_mar) {
			this.r47_lc_as_on_mar = r47_lc_as_on_mar;
		}

		public BigDecimal getR47_lc_as_on_sep() {
			return r47_lc_as_on_sep;
		}

		public void setR47_lc_as_on_sep(BigDecimal r47_lc_as_on_sep) {
			this.r47_lc_as_on_sep = r47_lc_as_on_sep;
		}

		public String getR48_product() {
			return r48_product;
		}

		public void setR48_product(String r48_product) {
			this.r48_product = r48_product;
		}

		public BigDecimal getR48_lc_as_on_mar() {
			return r48_lc_as_on_mar;
		}

		public void setR48_lc_as_on_mar(BigDecimal r48_lc_as_on_mar) {
			this.r48_lc_as_on_mar = r48_lc_as_on_mar;
		}

		public BigDecimal getR48_lc_as_on_sep() {
			return r48_lc_as_on_sep;
		}

		public void setR48_lc_as_on_sep(BigDecimal r48_lc_as_on_sep) {
			this.r48_lc_as_on_sep = r48_lc_as_on_sep;
		}

		public String getR49_product() {
			return r49_product;
		}

		public void setR49_product(String r49_product) {
			this.r49_product = r49_product;
		}

		public BigDecimal getR49_lc_as_on_mar() {
			return r49_lc_as_on_mar;
		}

		public void setR49_lc_as_on_mar(BigDecimal r49_lc_as_on_mar) {
			this.r49_lc_as_on_mar = r49_lc_as_on_mar;
		}

		public BigDecimal getR49_lc_as_on_sep() {
			return r49_lc_as_on_sep;
		}

		public void setR49_lc_as_on_sep(BigDecimal r49_lc_as_on_sep) {
			this.r49_lc_as_on_sep = r49_lc_as_on_sep;
		}

		public String getR50_product() {
			return r50_product;
		}

		public void setR50_product(String r50_product) {
			this.r50_product = r50_product;
		}

		public BigDecimal getR50_lc_as_on_mar() {
			return r50_lc_as_on_mar;
		}

		public void setR50_lc_as_on_mar(BigDecimal r50_lc_as_on_mar) {
			this.r50_lc_as_on_mar = r50_lc_as_on_mar;
		}

		public BigDecimal getR50_lc_as_on_sep() {
			return r50_lc_as_on_sep;
		}

		public void setR50_lc_as_on_sep(BigDecimal r50_lc_as_on_sep) {
			this.r50_lc_as_on_sep = r50_lc_as_on_sep;
		}

		public String getR51_product() {
			return r51_product;
		}

		public void setR51_product(String r51_product) {
			this.r51_product = r51_product;
		}

		public BigDecimal getR51_lc_as_on_mar() {
			return r51_lc_as_on_mar;
		}

		public void setR51_lc_as_on_mar(BigDecimal r51_lc_as_on_mar) {
			this.r51_lc_as_on_mar = r51_lc_as_on_mar;
		}

		public BigDecimal getR51_lc_as_on_sep() {
			return r51_lc_as_on_sep;
		}

		public void setR51_lc_as_on_sep(BigDecimal r51_lc_as_on_sep) {
			this.r51_lc_as_on_sep = r51_lc_as_on_sep;
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

	public static class CASH_FLOW_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public CASH_FLOW_PK() {
		}

		public CASH_FLOW_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof CASH_FLOW_PK))
				return false;
			CASH_FLOW_PK that = (CASH_FLOW_PK) o;
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

	public class CASH_FLOW_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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

	class CashFlowRowDetailMapper implements RowMapper<CASH_FLOW_Detail_Entity> {

		@Override
		public CASH_FLOW_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			CASH_FLOW_Detail_Entity obj = new CASH_FLOW_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
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

	class CashFlowRowArchivalDetailMapper implements RowMapper<CASH_FLOW_Archival_Detail_Entity> {

		@Override
		public CASH_FLOW_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			CASH_FLOW_Archival_Detail_Entity obj = new CASH_FLOW_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
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

	public class CASH_FLOW_Archival_Detail_Entity {
		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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

	public ModelAndView getCASH_FLOWView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("CASH_FLOW View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<CASH_FLOW_Archival_Summary_Entity> T1Master = new ArrayList<>();

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
			List<CASH_FLOW_Summary_Entity> T1Master = new ArrayList<>();
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

		mv.setViewName("BRRS/CASH_FLOW");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getCASH_FLOWcurrentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<CASH_FLOW_Archival_Detail_Entity> detailList;

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

				List<CASH_FLOW_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/CASH_FLOW");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getCASH_FLOWArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<CASH_FLOW_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (CASH_FLOW_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				CASH_FLOW_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  CASH_FLOW  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/CASH_FLOW");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				CASH_FLOW_Detail_Entity CASH_FLOWEntity = findBySnoArch(SNO);
				if (CASH_FLOWEntity != null && CASH_FLOWEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(CASH_FLOWEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("CASH_FLOWData", CASH_FLOWEntity);
			} else {
				CASH_FLOW_Detail_Entity CASH_FLOWEntity = findBySno(SNO);
				if (CASH_FLOWEntity != null && CASH_FLOWEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(CASH_FLOWEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("CASH_FLOWData", CASH_FLOWEntity);
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
			CASH_FLOW_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			CASH_FLOW_Detail_Entity oldcopy = new CASH_FLOW_Detail_Entity();
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
					sql = "UPDATE BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?, " + // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_CASH_FLOW_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ?, " + // ✅
																														// comma
																														// added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "CASH_FLOW Archival Screen",
							"BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "CASH_FLOW Screen",
							"BRRS_CASH_FLOW_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_CASH_FLOW_Procudure(reportDateStr, type, entry);

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
			Run_CASH_FLOW_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_CASH_FLOW_Procudure(String reportDateStr, String type, String entry) {

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
						String bdsql = "DELETE FROM BRRS_CASH_FLOW_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_CASH_FLOW_DETAILTABLE "
								+ " (SNO,ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA,REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_NAME, REPORT_DATE,DATA_ENTRY_VERSION, REPORT_REMARKS,ENTITY_FLG,MODIFY_FLG,DEL_FLG) "
								+ "SELECT SNO,ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA,REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_NAME, REPORT_DATE,DATA_ENTRY_VERSION, REPORT_REMARKS,ENTITY_FLG,MODIFY_FLG,DEL_FLG "
								+ "FROM BRRS_CASH_FLOW_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_CASH_FLOW_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_CASH_FLOW_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_CASH_FLOW_ARCHIVALTABLE_SUMMARY ( "
								+ "R9_PRODUCT, R9_LC_AS_ON_MAR, R9_LC_AS_ON_SEP, R10_PRODUCT, R10_LC_AS_ON_MAR, R10_LC_AS_ON_SEP, "
								+ "R11_PRODUCT, R11_LC_AS_ON_MAR, R11_LC_AS_ON_SEP, R12_PRODUCT, R12_LC_AS_ON_MAR, R12_LC_AS_ON_SEP, "
								+ "R13_PRODUCT, R13_LC_AS_ON_MAR, R13_LC_AS_ON_SEP, R14_PRODUCT, R14_LC_AS_ON_MAR, R14_LC_AS_ON_SEP, "
								+ "R15_PRODUCT, R15_LC_AS_ON_MAR, R15_LC_AS_ON_SEP, R16_PRODUCT, R16_LC_AS_ON_MAR, R16_LC_AS_ON_SEP, "
								+ "R17_PRODUCT, R17_LC_AS_ON_MAR, R17_LC_AS_ON_SEP, R18_PRODUCT, R18_LC_AS_ON_MAR, R18_LC_AS_ON_SEP, "
								+ "R19_PRODUCT, R19_LC_AS_ON_MAR, R19_LC_AS_ON_SEP, R20_PRODUCT, R20_LC_AS_ON_MAR, R20_LC_AS_ON_SEP, "
								+ "R21_PRODUCT, R21_LC_AS_ON_MAR, R21_LC_AS_ON_SEP, R22_PRODUCT, R22_LC_AS_ON_MAR, R22_LC_AS_ON_SEP, "
								+ "R23_PRODUCT, R23_LC_AS_ON_MAR, R23_LC_AS_ON_SEP, R24_PRODUCT, R24_LC_AS_ON_MAR, R24_LC_AS_ON_SEP, "
								+ "R25_PRODUCT, R25_LC_AS_ON_MAR, R25_LC_AS_ON_SEP, R26_PRODUCT, R26_LC_AS_ON_MAR, R26_LC_AS_ON_SEP, "
								+ "R27_PRODUCT, R27_LC_AS_ON_MAR, R27_LC_AS_ON_SEP, R28_PRODUCT, R28_LC_AS_ON_MAR, R28_LC_AS_ON_SEP, "
								+ "R29_PRODUCT, R29_LC_AS_ON_MAR, R29_LC_AS_ON_SEP, R30_PRODUCT, R30_LC_AS_ON_MAR, R30_LC_AS_ON_SEP, "
								+ "R31_PRODUCT, R31_LC_AS_ON_MAR, R31_LC_AS_ON_SEP, R32_PRODUCT, R32_LC_AS_ON_MAR, R32_LC_AS_ON_SEP, "
								+ "R33_PRODUCT, R33_LC_AS_ON_MAR, R33_LC_AS_ON_SEP, R34_PRODUCT, R34_LC_AS_ON_MAR, R34_LC_AS_ON_SEP, "
								+ "R35_PRODUCT, R35_LC_AS_ON_MAR, R35_LC_AS_ON_SEP, R36_PRODUCT, R36_LC_AS_ON_MAR, R36_LC_AS_ON_SEP, "
								+ "R37_PRODUCT, R37_LC_AS_ON_MAR, R37_LC_AS_ON_SEP, R38_PRODUCT, R38_LC_AS_ON_MAR, R38_LC_AS_ON_SEP, "
								+ "R39_PRODUCT, R39_LC_AS_ON_MAR, R39_LC_AS_ON_SEP, R40_PRODUCT, R40_LC_AS_ON_MAR, R40_LC_AS_ON_SEP, "
								+ "R41_PRODUCT, R41_LC_AS_ON_MAR, R41_LC_AS_ON_SEP, R42_PRODUCT, R42_LC_AS_ON_MAR, R42_LC_AS_ON_SEP, "
								+ "R43_PRODUCT, R43_LC_AS_ON_MAR, R43_LC_AS_ON_SEP, R44_PRODUCT, R44_LC_AS_ON_MAR, R44_LC_AS_ON_SEP, "
								+ "R45_PRODUCT, R45_LC_AS_ON_MAR, R45_LC_AS_ON_SEP, R46_PRODUCT, R46_LC_AS_ON_MAR, R46_LC_AS_ON_SEP, "
								+ "R47_PRODUCT, R47_LC_AS_ON_MAR, R47_LC_AS_ON_SEP, R48_PRODUCT, R48_LC_AS_ON_MAR, R48_LC_AS_ON_SEP, "
								+ "R49_PRODUCT, R49_LC_AS_ON_MAR, R49_LC_AS_ON_SEP, R50_PRODUCT, R50_LC_AS_ON_MAR, R50_LC_AS_ON_SEP, "
								+ "R51_PRODUCT, R51_LC_AS_ON_MAR, R51_LC_AS_ON_SEP, REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, "
								+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT "
								+ "R9_PRODUCT, R9_LC_AS_ON_MAR, R9_LC_AS_ON_SEP, R10_PRODUCT, R10_LC_AS_ON_MAR, R10_LC_AS_ON_SEP, "
								+ "R11_PRODUCT, R11_LC_AS_ON_MAR, R11_LC_AS_ON_SEP, R12_PRODUCT, R12_LC_AS_ON_MAR, R12_LC_AS_ON_SEP, "
								+ "R13_PRODUCT, R13_LC_AS_ON_MAR, R13_LC_AS_ON_SEP, R14_PRODUCT, R14_LC_AS_ON_MAR, R14_LC_AS_ON_SEP, "
								+ "R15_PRODUCT, R15_LC_AS_ON_MAR, R15_LC_AS_ON_SEP, R16_PRODUCT, R16_LC_AS_ON_MAR, R16_LC_AS_ON_SEP, "
								+ "R17_PRODUCT, R17_LC_AS_ON_MAR, R17_LC_AS_ON_SEP, R18_PRODUCT, R18_LC_AS_ON_MAR, R18_LC_AS_ON_SEP, "
								+ "R19_PRODUCT, R19_LC_AS_ON_MAR, R19_LC_AS_ON_SEP, R20_PRODUCT, R20_LC_AS_ON_MAR, R20_LC_AS_ON_SEP, "
								+ "R21_PRODUCT, R21_LC_AS_ON_MAR, R21_LC_AS_ON_SEP, R22_PRODUCT, R22_LC_AS_ON_MAR, R22_LC_AS_ON_SEP, "
								+ "R23_PRODUCT, R23_LC_AS_ON_MAR, R23_LC_AS_ON_SEP, R24_PRODUCT, R24_LC_AS_ON_MAR, R24_LC_AS_ON_SEP, "
								+ "R25_PRODUCT, R25_LC_AS_ON_MAR, R25_LC_AS_ON_SEP, R26_PRODUCT, R26_LC_AS_ON_MAR, R26_LC_AS_ON_SEP, "
								+ "R27_PRODUCT, R27_LC_AS_ON_MAR, R27_LC_AS_ON_SEP, R28_PRODUCT, R28_LC_AS_ON_MAR, R28_LC_AS_ON_SEP, "
								+ "R29_PRODUCT, R29_LC_AS_ON_MAR, R29_LC_AS_ON_SEP, R30_PRODUCT, R30_LC_AS_ON_MAR, R30_LC_AS_ON_SEP, "
								+ "R31_PRODUCT, R31_LC_AS_ON_MAR, R31_LC_AS_ON_SEP, R32_PRODUCT, R32_LC_AS_ON_MAR, R32_LC_AS_ON_SEP, "
								+ "R33_PRODUCT, R33_LC_AS_ON_MAR, R33_LC_AS_ON_SEP, R34_PRODUCT, R34_LC_AS_ON_MAR, R34_LC_AS_ON_SEP, "
								+ "R35_PRODUCT, R35_LC_AS_ON_MAR, R35_LC_AS_ON_SEP, R36_PRODUCT, R36_LC_AS_ON_MAR, R36_LC_AS_ON_SEP, "
								+ "R37_PRODUCT, R37_LC_AS_ON_MAR, R37_LC_AS_ON_SEP, R38_PRODUCT, R38_LC_AS_ON_MAR, R38_LC_AS_ON_SEP, "
								+ "R39_PRODUCT, R39_LC_AS_ON_MAR, R39_LC_AS_ON_SEP, R40_PRODUCT, R40_LC_AS_ON_MAR, R40_LC_AS_ON_SEP, "
								+ "R41_PRODUCT, R41_LC_AS_ON_MAR, R41_LC_AS_ON_SEP, R42_PRODUCT, R42_LC_AS_ON_MAR, R42_LC_AS_ON_SEP, "
								+ "R43_PRODUCT, R43_LC_AS_ON_MAR, R43_LC_AS_ON_SEP, R44_PRODUCT, R44_LC_AS_ON_MAR, R44_LC_AS_ON_SEP, "
								+ "R45_PRODUCT, R45_LC_AS_ON_MAR, R45_LC_AS_ON_SEP, R46_PRODUCT, R46_LC_AS_ON_MAR, R46_LC_AS_ON_SEP, "
								+ "R47_PRODUCT, R47_LC_AS_ON_MAR, R47_LC_AS_ON_SEP, R48_PRODUCT, R48_LC_AS_ON_MAR, R48_LC_AS_ON_SEP, "
								+ "R49_PRODUCT, R49_LC_AS_ON_MAR, R49_LC_AS_ON_SEP, R50_PRODUCT, R50_LC_AS_ON_MAR, R50_LC_AS_ON_SEP, "
								+ "R51_PRODUCT, R51_LC_AS_ON_MAR, R51_LC_AS_ON_SEP, "
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_CASH_FLOW_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_CASH_FLOW_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] getCASH_FLOWDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  CASH_FLOW Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)))  {
				byte[] ARCHIVALreport = getCASH_FLOWDetailNewExcelARCHIVAL(filename, fromdate, todate, currency,
						dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("CASH_FLOWDetailsDetail");

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
			List<CASH_FLOW_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CASH_FLOW_Detail_Entity item : reportData) {
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
				logger.info("No data found for CASH_FLOW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating CASH_FLOW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getCASH_FLOWDetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for CASH_FLOW ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)))  {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("CASH_FLOW Detail NEW");

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
			List<CASH_FLOW_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (CASH_FLOW_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for CASH_FLOW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating CASH_FLOW NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getCASH_FLOWExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.CommonDisclosure");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelCASH_FLOWARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<CASH_FLOW_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  CASH_FLOW report. Returning empty result.");
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
			int startRow = 3;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					CASH_FLOW_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(3);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					// R9 Col C
					Cell R9Cell1 = row.createCell(2);
					if (record.getR9_lc_as_on_mar() != null) {
						R9Cell1.setCellValue(record.getR9_lc_as_on_mar().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col D
					Cell R9Cell2 = row.createCell(3);
					if (record.getR9_lc_as_on_sep() != null) {
						R9Cell2.setCellValue(record.getR9_lc_as_on_sep().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(2);
					if (record.getR10_lc_as_on_mar() != null) {
						R10Cell1.setCellValue(record.getR10_lc_as_on_mar().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(3);
					if (record.getR10_lc_as_on_sep() != null) {
						R10Cell2.setCellValue(record.getR10_lc_as_on_sep().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}
					// R11
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(2);
					if (record.getR11_lc_as_on_mar() != null) {
						R11Cell1.setCellValue(record.getR11_lc_as_on_mar().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					Cell R11Cell2 = row.createCell(3);
					if (record.getR11_lc_as_on_sep() != null) {
						R11Cell2.setCellValue(record.getR11_lc_as_on_sep().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

// R12
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(2);
					if (record.getR12_lc_as_on_mar() != null) {
						R12Cell1.setCellValue(record.getR12_lc_as_on_mar().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					Cell R12Cell2 = row.createCell(3);
					if (record.getR12_lc_as_on_sep() != null) {
						R12Cell2.setCellValue(record.getR12_lc_as_on_sep().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

// R13
					row = sheet.getRow(12);
					Cell R13Cell1 = row.createCell(2);
					if (record.getR13_lc_as_on_mar() != null) {
						R13Cell1.setCellValue(record.getR13_lc_as_on_mar().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					Cell R13Cell2 = row.createCell(3);
					if (record.getR13_lc_as_on_sep() != null) {
						R13Cell2.setCellValue(record.getR13_lc_as_on_sep().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

// R14
					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(2);
					if (record.getR14_lc_as_on_mar() != null) {
						R14Cell1.setCellValue(record.getR14_lc_as_on_mar().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					Cell R14Cell2 = row.createCell(3);
					if (record.getR14_lc_as_on_sep() != null) {
						R14Cell2.setCellValue(record.getR14_lc_as_on_sep().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

// R15
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(2);
					if (record.getR15_lc_as_on_mar() != null) {
						R15Cell1.setCellValue(record.getR15_lc_as_on_mar().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					Cell R15Cell2 = row.createCell(3);
					if (record.getR15_lc_as_on_sep() != null) {
						R15Cell2.setCellValue(record.getR15_lc_as_on_sep().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

// R16
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(2);
					if (record.getR16_lc_as_on_mar() != null) {
						R16Cell1.setCellValue(record.getR16_lc_as_on_mar().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}

					Cell R16Cell2 = row.createCell(3);
					if (record.getR16_lc_as_on_sep() != null) {
						R16Cell2.setCellValue(record.getR16_lc_as_on_sep().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}

// R17
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(2);
					if (record.getR17_lc_as_on_mar() != null) {
						R17Cell1.setCellValue(record.getR17_lc_as_on_mar().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					Cell R17Cell2 = row.createCell(3);
					if (record.getR17_lc_as_on_sep() != null) {
						R17Cell2.setCellValue(record.getR17_lc_as_on_sep().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

// R18
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(2);
					if (record.getR18_lc_as_on_mar() != null) {
						R18Cell1.setCellValue(record.getR18_lc_as_on_mar().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					Cell R18Cell2 = row.createCell(3);
					if (record.getR18_lc_as_on_sep() != null) {
						R18Cell2.setCellValue(record.getR18_lc_as_on_sep().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

// R19
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(2);
					if (record.getR19_lc_as_on_mar() != null) {
						R19Cell1.setCellValue(record.getR19_lc_as_on_mar().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					Cell R19Cell2 = row.createCell(3);
					if (record.getR19_lc_as_on_sep() != null) {
						R19Cell2.setCellValue(record.getR19_lc_as_on_sep().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

// R20
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(2);
					if (record.getR20_lc_as_on_mar() != null) {
						R20Cell1.setCellValue(record.getR20_lc_as_on_mar().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					Cell R20Cell2 = row.createCell(3);
					if (record.getR20_lc_as_on_sep() != null) {
						R20Cell2.setCellValue(record.getR20_lc_as_on_sep().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

// R21
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(2);
					if (record.getR21_lc_as_on_mar() != null) {
						R21Cell1.setCellValue(record.getR21_lc_as_on_mar().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					Cell R21Cell2 = row.createCell(3);
					if (record.getR21_lc_as_on_sep() != null) {
						R21Cell2.setCellValue(record.getR21_lc_as_on_sep().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

// R22
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(2);
					if (record.getR22_lc_as_on_mar() != null) {
						R22Cell1.setCellValue(record.getR22_lc_as_on_mar().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					Cell R22Cell2 = row.createCell(3);
					if (record.getR22_lc_as_on_sep() != null) {
						R22Cell2.setCellValue(record.getR22_lc_as_on_sep().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
// R23
//row = sheet.getRow(22);
//Cell R23Cell1 = row.createCell(2);
//if (record.getR23_lc_as_on_mar() != null) {
//    R23Cell1.setCellValue(record.getR23_lc_as_on_mar().doubleValue());
//    R23Cell1.setCellStyle(numberStyle);
//} else {
//    R23Cell1.setCellValue("");
//    R23Cell1.setCellStyle(textStyle);
//}
//
//Cell R23Cell2 = row.createCell(3);
//if (record.getR23_lc_as_on_sep() != null) {
//    R23Cell2.setCellValue(record.getR23_lc_as_on_sep().doubleValue());
//    R23Cell2.setCellStyle(numberStyle);
//} else {
//    R23Cell2.setCellValue("");
//    R23Cell2.setCellStyle(textStyle);
//}

// R24
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(2);
					if (record.getR24_lc_as_on_mar() != null) {
						R24Cell1.setCellValue(record.getR24_lc_as_on_mar().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					Cell R24Cell2 = row.createCell(3);
					if (record.getR24_lc_as_on_sep() != null) {
						R24Cell2.setCellValue(record.getR24_lc_as_on_sep().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

// R25
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(2);
					if (record.getR25_lc_as_on_mar() != null) {
						R25Cell1.setCellValue(record.getR25_lc_as_on_mar().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					Cell R25Cell2 = row.createCell(3);
					if (record.getR25_lc_as_on_sep() != null) {
						R25Cell2.setCellValue(record.getR25_lc_as_on_sep().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

// R26
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(2);
					if (record.getR26_lc_as_on_mar() != null) {
						R26Cell1.setCellValue(record.getR26_lc_as_on_mar().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					Cell R26Cell2 = row.createCell(3);
					if (record.getR26_lc_as_on_sep() != null) {
						R26Cell2.setCellValue(record.getR26_lc_as_on_sep().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

// R27
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(2);
					if (record.getR27_lc_as_on_mar() != null) {
						R27Cell1.setCellValue(record.getR27_lc_as_on_mar().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					Cell R27Cell2 = row.createCell(3);
					if (record.getR27_lc_as_on_sep() != null) {
						R27Cell2.setCellValue(record.getR27_lc_as_on_sep().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

// R28
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(2);
					if (record.getR28_lc_as_on_mar() != null) {
						R28Cell1.setCellValue(record.getR28_lc_as_on_mar().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					Cell R28Cell2 = row.createCell(3);
					if (record.getR28_lc_as_on_sep() != null) {
						R28Cell2.setCellValue(record.getR28_lc_as_on_sep().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

// R29
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(2);
					if (record.getR29_lc_as_on_mar() != null) {
						R29Cell1.setCellValue(record.getR29_lc_as_on_mar().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					Cell R29Cell2 = row.createCell(3);
					if (record.getR29_lc_as_on_sep() != null) {
						R29Cell2.setCellValue(record.getR29_lc_as_on_sep().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

// R30
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(2);
					if (record.getR30_lc_as_on_mar() != null) {
						R30Cell1.setCellValue(record.getR30_lc_as_on_mar().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					Cell R30Cell2 = row.createCell(3);
					if (record.getR30_lc_as_on_sep() != null) {
						R30Cell2.setCellValue(record.getR30_lc_as_on_sep().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

// R31
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(2);
					if (record.getR31_lc_as_on_mar() != null) {
						R31Cell1.setCellValue(record.getR31_lc_as_on_mar().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}

					Cell R31Cell2 = row.createCell(3);
					if (record.getR31_lc_as_on_sep() != null) {
						R31Cell2.setCellValue(record.getR31_lc_as_on_sep().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}

// R32
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(2);
					if (record.getR32_lc_as_on_mar() != null) {
						R32Cell1.setCellValue(record.getR32_lc_as_on_mar().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}

					Cell R32Cell2 = row.createCell(3);
					if (record.getR32_lc_as_on_sep() != null) {
						R32Cell2.setCellValue(record.getR32_lc_as_on_sep().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}

// R33
//row = sheet.getRow(32);
//Cell R33Cell1 = row.createCell(2);
//if (record.getR33_lc_as_on_mar() != null) {
//    R33Cell1.setCellValue(record.getR33_lc_as_on_mar().doubleValue());
//    R33Cell1.setCellStyle(numberStyle);
//} else {
//    R33Cell1.setCellValue("");
//    R33Cell1.setCellStyle(textStyle);
//}
//
//Cell R33Cell2 = row.createCell(3);
//if (record.getR33_lc_as_on_sep() != null) {
//    R33Cell2.setCellValue(record.getR33_lc_as_on_sep().doubleValue());
//    R33Cell2.setCellStyle(numberStyle);
//} else {
//    R33Cell2.setCellValue("");
//    R33Cell2.setCellStyle(textStyle);
//}

// R34
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(2);
					if (record.getR34_lc_as_on_mar() != null) {
						R34Cell1.setCellValue(record.getR34_lc_as_on_mar().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}

					Cell R34Cell2 = row.createCell(3);
					if (record.getR34_lc_as_on_sep() != null) {
						R34Cell2.setCellValue(record.getR34_lc_as_on_sep().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}

// R35
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(2);
					if (record.getR35_lc_as_on_mar() != null) {
						R35Cell1.setCellValue(record.getR35_lc_as_on_mar().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}

					Cell R35Cell2 = row.createCell(3);
					if (record.getR35_lc_as_on_sep() != null) {
						R35Cell2.setCellValue(record.getR35_lc_as_on_sep().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}

// R36
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(2);
					if (record.getR36_lc_as_on_mar() != null) {
						R36Cell1.setCellValue(record.getR36_lc_as_on_mar().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}

					Cell R36Cell2 = row.createCell(3);
					if (record.getR36_lc_as_on_sep() != null) {
						R36Cell2.setCellValue(record.getR36_lc_as_on_sep().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}

// R37
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(2);
					if (record.getR37_lc_as_on_mar() != null) {
						R37Cell1.setCellValue(record.getR37_lc_as_on_mar().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}

					Cell R37Cell2 = row.createCell(3);
					if (record.getR37_lc_as_on_sep() != null) {
						R37Cell2.setCellValue(record.getR37_lc_as_on_sep().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}

// R38
//row = sheet.getRow(37);
//Cell R38Cell1 = row.createCell(2);
//if (record.getR38_lc_as_on_mar() != null) {
//    R38Cell1.setCellValue(record.getR38_lc_as_on_mar().doubleValue());
//    R38Cell1.setCellStyle(numberStyle);
//} else {
//    R38Cell1.setCellValue("");
//    R38Cell1.setCellStyle(textStyle);
//}
//
//Cell R38Cell2 = row.createCell(3);
//if (record.getR38_lc_as_on_sep() != null) {
//    R38Cell2.setCellValue(record.getR38_lc_as_on_sep().doubleValue());
//    R38Cell2.setCellStyle(numberStyle);
//} else {
//    R38Cell2.setCellValue("");
//    R38Cell2.setCellStyle(textStyle);
//}

// R39
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(2);
					if (record.getR39_lc_as_on_mar() != null) {
						R39Cell1.setCellValue(record.getR39_lc_as_on_mar().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}

					Cell R39Cell2 = row.createCell(3);
					if (record.getR39_lc_as_on_sep() != null) {
						R39Cell2.setCellValue(record.getR39_lc_as_on_sep().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}

// R40
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(2);
					if (record.getR40_lc_as_on_mar() != null) {
						R40Cell1.setCellValue(record.getR40_lc_as_on_mar().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					Cell R40Cell2 = row.createCell(3);
					if (record.getR40_lc_as_on_sep() != null) {
						R40Cell2.setCellValue(record.getR40_lc_as_on_sep().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

// R41
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(2);
					if (record.getR41_lc_as_on_mar() != null) {
						R41Cell1.setCellValue(record.getR41_lc_as_on_mar().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					Cell R41Cell2 = row.createCell(3);
					if (record.getR41_lc_as_on_sep() != null) {
						R41Cell2.setCellValue(record.getR41_lc_as_on_sep().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

// R42
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(2);
					if (record.getR42_lc_as_on_mar() != null) {
						R42Cell1.setCellValue(record.getR42_lc_as_on_mar().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					Cell R42Cell2 = row.createCell(3);
					if (record.getR42_lc_as_on_sep() != null) {
						R42Cell2.setCellValue(record.getR42_lc_as_on_sep().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

// R43
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(2);
					if (record.getR43_lc_as_on_mar() != null) {
						R43Cell1.setCellValue(record.getR43_lc_as_on_mar().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}

					Cell R43Cell2 = row.createCell(3);
					if (record.getR43_lc_as_on_sep() != null) {
						R43Cell2.setCellValue(record.getR43_lc_as_on_sep().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
// R44
					row = sheet.getRow(43);
					Cell R44Cell1 = row.createCell(2);
					if (record.getR44_lc_as_on_mar() != null) {
						R44Cell1.setCellValue(record.getR44_lc_as_on_mar().doubleValue());
						R44Cell1.setCellStyle(numberStyle);
					} else {
						R44Cell1.setCellValue("");
						R44Cell1.setCellStyle(textStyle);
					}

					Cell R44Cell2 = row.createCell(3);
					if (record.getR44_lc_as_on_sep() != null) {
						R44Cell2.setCellValue(record.getR44_lc_as_on_sep().doubleValue());
						R44Cell2.setCellStyle(numberStyle);
					} else {
						R44Cell2.setCellValue("");
						R44Cell2.setCellStyle(textStyle);
					}

// R45
//row = sheet.getRow(44);
//Cell R45Cell1 = row.createCell(2);
//if (record.getR45_lc_as_on_mar() != null) {
//    R45Cell1.setCellValue(record.getR45_lc_as_on_mar().doubleValue());
//    R45Cell1.setCellStyle(numberStyle);
//} else {
//    R45Cell1.setCellValue("");
//    R45Cell1.setCellStyle(textStyle);
//}
//
//Cell R45Cell2 = row.createCell(3);
//if (record.getR45_lc_as_on_sep() != null) {
//    R45Cell2.setCellValue(record.getR45_lc_as_on_sep().doubleValue());
//    R45Cell2.setCellStyle(numberStyle);
//} else {
//    R45Cell2.setCellValue("");
//    R45Cell2.setCellStyle(textStyle);
//}

// R46
//row = sheet.getRow(45);
//Cell R46Cell1 = row.createCell(2);
//if (record.getR46_lc_as_on_mar() != null) {
//    R46Cell1.setCellValue(record.getR46_lc_as_on_mar().doubleValue());
//    R46Cell1.setCellStyle(numberStyle);
//} else {
//    R46Cell1.setCellValue("");
//    R46Cell1.setCellStyle(textStyle);
//}
//
//Cell R46Cell2 = row.createCell(3);
//if (record.getR46_lc_as_on_sep() != null) {
//    R46Cell2.setCellValue(record.getR46_lc_as_on_sep().doubleValue());
//    R46Cell2.setCellStyle(numberStyle);
//} else {
//    R46Cell2.setCellValue("");
//    R46Cell2.setCellStyle(textStyle);
//}

// R47
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(2);
					if (record.getR47_lc_as_on_mar() != null) {
						R47Cell1.setCellValue(record.getR47_lc_as_on_mar().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}

					Cell R47Cell2 = row.createCell(3);
					if (record.getR47_lc_as_on_sep() != null) {
						R47Cell2.setCellValue(record.getR47_lc_as_on_sep().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}

// R48
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(2);
					if (record.getR48_lc_as_on_mar() != null) {
						R48Cell1.setCellValue(record.getR48_lc_as_on_mar().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

//Cell R48Cell2 = row.createCell(3);
//if (record.getR48_lc_as_on_sep() != null) {
//    R48Cell2.setCellValue(record.getR48_lc_as_on_sep().doubleValue());
//    R48Cell2.setCellStyle(numberStyle);
//} else {
//    R48Cell2.setCellValue("");
//    R48Cell2.setCellStyle(textStyle);
//}

// R49
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(2);
					if (record.getR49_lc_as_on_mar() != null) {
						R49Cell1.setCellValue(record.getR49_lc_as_on_mar().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

//Cell R49Cell2 = row.createCell(3);
//if (record.getR49_lc_as_on_sep() != null) {
//    R49Cell2.setCellValue(record.getR49_lc_as_on_sep().doubleValue());
//    R49Cell2.setCellStyle(numberStyle);
//} else {
//    R49Cell2.setCellValue("");
//    R49Cell2.setCellStyle(textStyle);
//}

// R50
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(2);
					if (record.getR50_lc_as_on_mar() != null) {
						R50Cell1.setCellValue(record.getR50_lc_as_on_mar().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					Cell R50Cell2 = row.createCell(3);
					if (record.getR50_lc_as_on_sep() != null) {
						R50Cell2.setCellValue(record.getR50_lc_as_on_sep().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

// R51
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(2);
					if (record.getR51_lc_as_on_mar() != null) {
						R51Cell1.setCellValue(record.getR51_lc_as_on_mar().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

//Cell R51Cell2 = row.createCell(3);
//if (record.getR51_lc_as_on_sep() != null) {
//    R51Cell2.setCellValue(record.getR51_lc_as_on_sep().doubleValue());
//    R51Cell2.setCellStyle(numberStyle);
//} else {
//    R51Cell2.setCellValue("");
//    R51Cell2.setCellStyle(textStyle);
//}

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

	public byte[] getExcelCASH_FLOWARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<CASH_FLOW_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for CASH_FLOW new report. Returning empty result.");
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
			int startRow = 3;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					CASH_FLOW_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(3);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
					// R9 Col C
					Cell R9Cell1 = row.createCell(2);
					if (record.getR9_lc_as_on_mar() != null) {
						R9Cell1.setCellValue(record.getR9_lc_as_on_mar().doubleValue());
						R9Cell1.setCellStyle(numberStyle);
					} else {
						R9Cell1.setCellValue("");
						R9Cell1.setCellStyle(textStyle);
					}

					// R9 Col D
					Cell R9Cell2 = row.createCell(3);
					if (record.getR9_lc_as_on_sep() != null) {
						R9Cell2.setCellValue(record.getR9_lc_as_on_sep().doubleValue());
						R9Cell2.setCellStyle(numberStyle);
					} else {
						R9Cell2.setCellValue("");
						R9Cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					Cell R10Cell1 = row.createCell(2);
					if (record.getR10_lc_as_on_mar() != null) {
						R10Cell1.setCellValue(record.getR10_lc_as_on_mar().doubleValue());
						R10Cell1.setCellStyle(numberStyle);
					} else {
						R10Cell1.setCellValue("");
						R10Cell1.setCellStyle(textStyle);
					}

					// R10 Col E
					Cell R10Cell2 = row.createCell(3);
					if (record.getR10_lc_as_on_sep() != null) {
						R10Cell2.setCellValue(record.getR10_lc_as_on_sep().doubleValue());
						R10Cell2.setCellStyle(numberStyle);
					} else {
						R10Cell2.setCellValue("");
						R10Cell2.setCellStyle(textStyle);
					}
					// R11
					row = sheet.getRow(10);
					Cell R11Cell1 = row.createCell(2);
					if (record.getR11_lc_as_on_mar() != null) {
						R11Cell1.setCellValue(record.getR11_lc_as_on_mar().doubleValue());
						R11Cell1.setCellStyle(numberStyle);
					} else {
						R11Cell1.setCellValue("");
						R11Cell1.setCellStyle(textStyle);
					}

					Cell R11Cell2 = row.createCell(3);
					if (record.getR11_lc_as_on_sep() != null) {
						R11Cell2.setCellValue(record.getR11_lc_as_on_sep().doubleValue());
						R11Cell2.setCellStyle(numberStyle);
					} else {
						R11Cell2.setCellValue("");
						R11Cell2.setCellStyle(textStyle);
					}

// R12
					row = sheet.getRow(11);
					Cell R12Cell1 = row.createCell(2);
					if (record.getR12_lc_as_on_mar() != null) {
						R12Cell1.setCellValue(record.getR12_lc_as_on_mar().doubleValue());
						R12Cell1.setCellStyle(numberStyle);
					} else {
						R12Cell1.setCellValue("");
						R12Cell1.setCellStyle(textStyle);
					}

					Cell R12Cell2 = row.createCell(3);
					if (record.getR12_lc_as_on_sep() != null) {
						R12Cell2.setCellValue(record.getR12_lc_as_on_sep().doubleValue());
						R12Cell2.setCellStyle(numberStyle);
					} else {
						R12Cell2.setCellValue("");
						R12Cell2.setCellStyle(textStyle);
					}

// R13
					row = sheet.getRow(12);
					Cell R13Cell1 = row.createCell(2);
					if (record.getR13_lc_as_on_mar() != null) {
						R13Cell1.setCellValue(record.getR13_lc_as_on_mar().doubleValue());
						R13Cell1.setCellStyle(numberStyle);
					} else {
						R13Cell1.setCellValue("");
						R13Cell1.setCellStyle(textStyle);
					}

					Cell R13Cell2 = row.createCell(3);
					if (record.getR13_lc_as_on_sep() != null) {
						R13Cell2.setCellValue(record.getR13_lc_as_on_sep().doubleValue());
						R13Cell2.setCellStyle(numberStyle);
					} else {
						R13Cell2.setCellValue("");
						R13Cell2.setCellStyle(textStyle);
					}

// R14
					row = sheet.getRow(13);
					Cell R14Cell1 = row.createCell(2);
					if (record.getR14_lc_as_on_mar() != null) {
						R14Cell1.setCellValue(record.getR14_lc_as_on_mar().doubleValue());
						R14Cell1.setCellStyle(numberStyle);
					} else {
						R14Cell1.setCellValue("");
						R14Cell1.setCellStyle(textStyle);
					}

					Cell R14Cell2 = row.createCell(3);
					if (record.getR14_lc_as_on_sep() != null) {
						R14Cell2.setCellValue(record.getR14_lc_as_on_sep().doubleValue());
						R14Cell2.setCellStyle(numberStyle);
					} else {
						R14Cell2.setCellValue("");
						R14Cell2.setCellStyle(textStyle);
					}

// R15
					row = sheet.getRow(14);
					Cell R15Cell1 = row.createCell(2);
					if (record.getR15_lc_as_on_mar() != null) {
						R15Cell1.setCellValue(record.getR15_lc_as_on_mar().doubleValue());
						R15Cell1.setCellStyle(numberStyle);
					} else {
						R15Cell1.setCellValue("");
						R15Cell1.setCellStyle(textStyle);
					}

					Cell R15Cell2 = row.createCell(3);
					if (record.getR15_lc_as_on_sep() != null) {
						R15Cell2.setCellValue(record.getR15_lc_as_on_sep().doubleValue());
						R15Cell2.setCellStyle(numberStyle);
					} else {
						R15Cell2.setCellValue("");
						R15Cell2.setCellStyle(textStyle);
					}

// R16
					row = sheet.getRow(15);
					Cell R16Cell1 = row.createCell(2);
					if (record.getR16_lc_as_on_mar() != null) {
						R16Cell1.setCellValue(record.getR16_lc_as_on_mar().doubleValue());
						R16Cell1.setCellStyle(numberStyle);
					} else {
						R16Cell1.setCellValue("");
						R16Cell1.setCellStyle(textStyle);
					}

					Cell R16Cell2 = row.createCell(3);
					if (record.getR16_lc_as_on_sep() != null) {
						R16Cell2.setCellValue(record.getR16_lc_as_on_sep().doubleValue());
						R16Cell2.setCellStyle(numberStyle);
					} else {
						R16Cell2.setCellValue("");
						R16Cell2.setCellStyle(textStyle);
					}

// R17
					row = sheet.getRow(16);
					Cell R17Cell1 = row.createCell(2);
					if (record.getR17_lc_as_on_mar() != null) {
						R17Cell1.setCellValue(record.getR17_lc_as_on_mar().doubleValue());
						R17Cell1.setCellStyle(numberStyle);
					} else {
						R17Cell1.setCellValue("");
						R17Cell1.setCellStyle(textStyle);
					}

					Cell R17Cell2 = row.createCell(3);
					if (record.getR17_lc_as_on_sep() != null) {
						R17Cell2.setCellValue(record.getR17_lc_as_on_sep().doubleValue());
						R17Cell2.setCellStyle(numberStyle);
					} else {
						R17Cell2.setCellValue("");
						R17Cell2.setCellStyle(textStyle);
					}

// R18
					row = sheet.getRow(17);
					Cell R18Cell1 = row.createCell(2);
					if (record.getR18_lc_as_on_mar() != null) {
						R18Cell1.setCellValue(record.getR18_lc_as_on_mar().doubleValue());
						R18Cell1.setCellStyle(numberStyle);
					} else {
						R18Cell1.setCellValue("");
						R18Cell1.setCellStyle(textStyle);
					}

					Cell R18Cell2 = row.createCell(3);
					if (record.getR18_lc_as_on_sep() != null) {
						R18Cell2.setCellValue(record.getR18_lc_as_on_sep().doubleValue());
						R18Cell2.setCellStyle(numberStyle);
					} else {
						R18Cell2.setCellValue("");
						R18Cell2.setCellStyle(textStyle);
					}

// R19
					row = sheet.getRow(18);
					Cell R19Cell1 = row.createCell(2);
					if (record.getR19_lc_as_on_mar() != null) {
						R19Cell1.setCellValue(record.getR19_lc_as_on_mar().doubleValue());
						R19Cell1.setCellStyle(numberStyle);
					} else {
						R19Cell1.setCellValue("");
						R19Cell1.setCellStyle(textStyle);
					}

					Cell R19Cell2 = row.createCell(3);
					if (record.getR19_lc_as_on_sep() != null) {
						R19Cell2.setCellValue(record.getR19_lc_as_on_sep().doubleValue());
						R19Cell2.setCellStyle(numberStyle);
					} else {
						R19Cell2.setCellValue("");
						R19Cell2.setCellStyle(textStyle);
					}

// R20
					row = sheet.getRow(19);
					Cell R20Cell1 = row.createCell(2);
					if (record.getR20_lc_as_on_mar() != null) {
						R20Cell1.setCellValue(record.getR20_lc_as_on_mar().doubleValue());
						R20Cell1.setCellStyle(numberStyle);
					} else {
						R20Cell1.setCellValue("");
						R20Cell1.setCellStyle(textStyle);
					}

					Cell R20Cell2 = row.createCell(3);
					if (record.getR20_lc_as_on_sep() != null) {
						R20Cell2.setCellValue(record.getR20_lc_as_on_sep().doubleValue());
						R20Cell2.setCellStyle(numberStyle);
					} else {
						R20Cell2.setCellValue("");
						R20Cell2.setCellStyle(textStyle);
					}

// R21
					row = sheet.getRow(20);
					Cell R21Cell1 = row.createCell(2);
					if (record.getR21_lc_as_on_mar() != null) {
						R21Cell1.setCellValue(record.getR21_lc_as_on_mar().doubleValue());
						R21Cell1.setCellStyle(numberStyle);
					} else {
						R21Cell1.setCellValue("");
						R21Cell1.setCellStyle(textStyle);
					}

					Cell R21Cell2 = row.createCell(3);
					if (record.getR21_lc_as_on_sep() != null) {
						R21Cell2.setCellValue(record.getR21_lc_as_on_sep().doubleValue());
						R21Cell2.setCellStyle(numberStyle);
					} else {
						R21Cell2.setCellValue("");
						R21Cell2.setCellStyle(textStyle);
					}

// R22
					row = sheet.getRow(21);
					Cell R22Cell1 = row.createCell(2);
					if (record.getR22_lc_as_on_mar() != null) {
						R22Cell1.setCellValue(record.getR22_lc_as_on_mar().doubleValue());
						R22Cell1.setCellStyle(numberStyle);
					} else {
						R22Cell1.setCellValue("");
						R22Cell1.setCellStyle(textStyle);
					}

					Cell R22Cell2 = row.createCell(3);
					if (record.getR22_lc_as_on_sep() != null) {
						R22Cell2.setCellValue(record.getR22_lc_as_on_sep().doubleValue());
						R22Cell2.setCellStyle(numberStyle);
					} else {
						R22Cell2.setCellValue("");
						R22Cell2.setCellStyle(textStyle);
					}
// R23
//row = sheet.getRow(22);
//Cell R23Cell1 = row.createCell(2);
//if (record.getR23_lc_as_on_mar() != null) {
//    R23Cell1.setCellValue(record.getR23_lc_as_on_mar().doubleValue());
//    R23Cell1.setCellStyle(numberStyle);
//} else {
//    R23Cell1.setCellValue("");
//    R23Cell1.setCellStyle(textStyle);
//}
//
//Cell R23Cell2 = row.createCell(3);
//if (record.getR23_lc_as_on_sep() != null) {
//    R23Cell2.setCellValue(record.getR23_lc_as_on_sep().doubleValue());
//    R23Cell2.setCellStyle(numberStyle);
//} else {
//    R23Cell2.setCellValue("");
//    R23Cell2.setCellStyle(textStyle);
//}

// R24
					row = sheet.getRow(23);
					Cell R24Cell1 = row.createCell(2);
					if (record.getR24_lc_as_on_mar() != null) {
						R24Cell1.setCellValue(record.getR24_lc_as_on_mar().doubleValue());
						R24Cell1.setCellStyle(numberStyle);
					} else {
						R24Cell1.setCellValue("");
						R24Cell1.setCellStyle(textStyle);
					}

					Cell R24Cell2 = row.createCell(3);
					if (record.getR24_lc_as_on_sep() != null) {
						R24Cell2.setCellValue(record.getR24_lc_as_on_sep().doubleValue());
						R24Cell2.setCellStyle(numberStyle);
					} else {
						R24Cell2.setCellValue("");
						R24Cell2.setCellStyle(textStyle);
					}

// R25
					row = sheet.getRow(24);
					Cell R25Cell1 = row.createCell(2);
					if (record.getR25_lc_as_on_mar() != null) {
						R25Cell1.setCellValue(record.getR25_lc_as_on_mar().doubleValue());
						R25Cell1.setCellStyle(numberStyle);
					} else {
						R25Cell1.setCellValue("");
						R25Cell1.setCellStyle(textStyle);
					}

					Cell R25Cell2 = row.createCell(3);
					if (record.getR25_lc_as_on_sep() != null) {
						R25Cell2.setCellValue(record.getR25_lc_as_on_sep().doubleValue());
						R25Cell2.setCellStyle(numberStyle);
					} else {
						R25Cell2.setCellValue("");
						R25Cell2.setCellStyle(textStyle);
					}

// R26
					row = sheet.getRow(25);
					Cell R26Cell1 = row.createCell(2);
					if (record.getR26_lc_as_on_mar() != null) {
						R26Cell1.setCellValue(record.getR26_lc_as_on_mar().doubleValue());
						R26Cell1.setCellStyle(numberStyle);
					} else {
						R26Cell1.setCellValue("");
						R26Cell1.setCellStyle(textStyle);
					}

					Cell R26Cell2 = row.createCell(3);
					if (record.getR26_lc_as_on_sep() != null) {
						R26Cell2.setCellValue(record.getR26_lc_as_on_sep().doubleValue());
						R26Cell2.setCellStyle(numberStyle);
					} else {
						R26Cell2.setCellValue("");
						R26Cell2.setCellStyle(textStyle);
					}

// R27
					row = sheet.getRow(26);
					Cell R27Cell1 = row.createCell(2);
					if (record.getR27_lc_as_on_mar() != null) {
						R27Cell1.setCellValue(record.getR27_lc_as_on_mar().doubleValue());
						R27Cell1.setCellStyle(numberStyle);
					} else {
						R27Cell1.setCellValue("");
						R27Cell1.setCellStyle(textStyle);
					}

					Cell R27Cell2 = row.createCell(3);
					if (record.getR27_lc_as_on_sep() != null) {
						R27Cell2.setCellValue(record.getR27_lc_as_on_sep().doubleValue());
						R27Cell2.setCellStyle(numberStyle);
					} else {
						R27Cell2.setCellValue("");
						R27Cell2.setCellStyle(textStyle);
					}

// R28
					row = sheet.getRow(27);
					Cell R28Cell1 = row.createCell(2);
					if (record.getR28_lc_as_on_mar() != null) {
						R28Cell1.setCellValue(record.getR28_lc_as_on_mar().doubleValue());
						R28Cell1.setCellStyle(numberStyle);
					} else {
						R28Cell1.setCellValue("");
						R28Cell1.setCellStyle(textStyle);
					}

					Cell R28Cell2 = row.createCell(3);
					if (record.getR28_lc_as_on_sep() != null) {
						R28Cell2.setCellValue(record.getR28_lc_as_on_sep().doubleValue());
						R28Cell2.setCellStyle(numberStyle);
					} else {
						R28Cell2.setCellValue("");
						R28Cell2.setCellStyle(textStyle);
					}

// R29
					row = sheet.getRow(28);
					Cell R29Cell1 = row.createCell(2);
					if (record.getR29_lc_as_on_mar() != null) {
						R29Cell1.setCellValue(record.getR29_lc_as_on_mar().doubleValue());
						R29Cell1.setCellStyle(numberStyle);
					} else {
						R29Cell1.setCellValue("");
						R29Cell1.setCellStyle(textStyle);
					}

					Cell R29Cell2 = row.createCell(3);
					if (record.getR29_lc_as_on_sep() != null) {
						R29Cell2.setCellValue(record.getR29_lc_as_on_sep().doubleValue());
						R29Cell2.setCellStyle(numberStyle);
					} else {
						R29Cell2.setCellValue("");
						R29Cell2.setCellStyle(textStyle);
					}

// R30
					row = sheet.getRow(29);
					Cell R30Cell1 = row.createCell(2);
					if (record.getR30_lc_as_on_mar() != null) {
						R30Cell1.setCellValue(record.getR30_lc_as_on_mar().doubleValue());
						R30Cell1.setCellStyle(numberStyle);
					} else {
						R30Cell1.setCellValue("");
						R30Cell1.setCellStyle(textStyle);
					}

					Cell R30Cell2 = row.createCell(3);
					if (record.getR30_lc_as_on_sep() != null) {
						R30Cell2.setCellValue(record.getR30_lc_as_on_sep().doubleValue());
						R30Cell2.setCellStyle(numberStyle);
					} else {
						R30Cell2.setCellValue("");
						R30Cell2.setCellStyle(textStyle);
					}

// R31
					row = sheet.getRow(30);
					Cell R31Cell1 = row.createCell(2);
					if (record.getR31_lc_as_on_mar() != null) {
						R31Cell1.setCellValue(record.getR31_lc_as_on_mar().doubleValue());
						R31Cell1.setCellStyle(numberStyle);
					} else {
						R31Cell1.setCellValue("");
						R31Cell1.setCellStyle(textStyle);
					}

					Cell R31Cell2 = row.createCell(3);
					if (record.getR31_lc_as_on_sep() != null) {
						R31Cell2.setCellValue(record.getR31_lc_as_on_sep().doubleValue());
						R31Cell2.setCellStyle(numberStyle);
					} else {
						R31Cell2.setCellValue("");
						R31Cell2.setCellStyle(textStyle);
					}

// R32
					row = sheet.getRow(31);
					Cell R32Cell1 = row.createCell(2);
					if (record.getR32_lc_as_on_mar() != null) {
						R32Cell1.setCellValue(record.getR32_lc_as_on_mar().doubleValue());
						R32Cell1.setCellStyle(numberStyle);
					} else {
						R32Cell1.setCellValue("");
						R32Cell1.setCellStyle(textStyle);
					}

					Cell R32Cell2 = row.createCell(3);
					if (record.getR32_lc_as_on_sep() != null) {
						R32Cell2.setCellValue(record.getR32_lc_as_on_sep().doubleValue());
						R32Cell2.setCellStyle(numberStyle);
					} else {
						R32Cell2.setCellValue("");
						R32Cell2.setCellStyle(textStyle);
					}

// R33
//row = sheet.getRow(32);
//Cell R33Cell1 = row.createCell(2);
//if (record.getR33_lc_as_on_mar() != null) {
//    R33Cell1.setCellValue(record.getR33_lc_as_on_mar().doubleValue());
//    R33Cell1.setCellStyle(numberStyle);
//} else {
//    R33Cell1.setCellValue("");
//    R33Cell1.setCellStyle(textStyle);
//}
//
//Cell R33Cell2 = row.createCell(3);
//if (record.getR33_lc_as_on_sep() != null) {
//    R33Cell2.setCellValue(record.getR33_lc_as_on_sep().doubleValue());
//    R33Cell2.setCellStyle(numberStyle);
//} else {
//    R33Cell2.setCellValue("");
//    R33Cell2.setCellStyle(textStyle);
//}

// R34
					row = sheet.getRow(33);
					Cell R34Cell1 = row.createCell(2);
					if (record.getR34_lc_as_on_mar() != null) {
						R34Cell1.setCellValue(record.getR34_lc_as_on_mar().doubleValue());
						R34Cell1.setCellStyle(numberStyle);
					} else {
						R34Cell1.setCellValue("");
						R34Cell1.setCellStyle(textStyle);
					}

					Cell R34Cell2 = row.createCell(3);
					if (record.getR34_lc_as_on_sep() != null) {
						R34Cell2.setCellValue(record.getR34_lc_as_on_sep().doubleValue());
						R34Cell2.setCellStyle(numberStyle);
					} else {
						R34Cell2.setCellValue("");
						R34Cell2.setCellStyle(textStyle);
					}

// R35
					row = sheet.getRow(34);
					Cell R35Cell1 = row.createCell(2);
					if (record.getR35_lc_as_on_mar() != null) {
						R35Cell1.setCellValue(record.getR35_lc_as_on_mar().doubleValue());
						R35Cell1.setCellStyle(numberStyle);
					} else {
						R35Cell1.setCellValue("");
						R35Cell1.setCellStyle(textStyle);
					}

					Cell R35Cell2 = row.createCell(3);
					if (record.getR35_lc_as_on_sep() != null) {
						R35Cell2.setCellValue(record.getR35_lc_as_on_sep().doubleValue());
						R35Cell2.setCellStyle(numberStyle);
					} else {
						R35Cell2.setCellValue("");
						R35Cell2.setCellStyle(textStyle);
					}

// R36
					row = sheet.getRow(35);
					Cell R36Cell1 = row.createCell(2);
					if (record.getR36_lc_as_on_mar() != null) {
						R36Cell1.setCellValue(record.getR36_lc_as_on_mar().doubleValue());
						R36Cell1.setCellStyle(numberStyle);
					} else {
						R36Cell1.setCellValue("");
						R36Cell1.setCellStyle(textStyle);
					}

					Cell R36Cell2 = row.createCell(3);
					if (record.getR36_lc_as_on_sep() != null) {
						R36Cell2.setCellValue(record.getR36_lc_as_on_sep().doubleValue());
						R36Cell2.setCellStyle(numberStyle);
					} else {
						R36Cell2.setCellValue("");
						R36Cell2.setCellStyle(textStyle);
					}

// R37
					row = sheet.getRow(36);
					Cell R37Cell1 = row.createCell(2);
					if (record.getR37_lc_as_on_mar() != null) {
						R37Cell1.setCellValue(record.getR37_lc_as_on_mar().doubleValue());
						R37Cell1.setCellStyle(numberStyle);
					} else {
						R37Cell1.setCellValue("");
						R37Cell1.setCellStyle(textStyle);
					}

					Cell R37Cell2 = row.createCell(3);
					if (record.getR37_lc_as_on_sep() != null) {
						R37Cell2.setCellValue(record.getR37_lc_as_on_sep().doubleValue());
						R37Cell2.setCellStyle(numberStyle);
					} else {
						R37Cell2.setCellValue("");
						R37Cell2.setCellStyle(textStyle);
					}

// R38
//row = sheet.getRow(37);
//Cell R38Cell1 = row.createCell(2);
//if (record.getR38_lc_as_on_mar() != null) {
//    R38Cell1.setCellValue(record.getR38_lc_as_on_mar().doubleValue());
//    R38Cell1.setCellStyle(numberStyle);
//} else {
//    R38Cell1.setCellValue("");
//    R38Cell1.setCellStyle(textStyle);
//}
//
//Cell R38Cell2 = row.createCell(3);
//if (record.getR38_lc_as_on_sep() != null) {
//    R38Cell2.setCellValue(record.getR38_lc_as_on_sep().doubleValue());
//    R38Cell2.setCellStyle(numberStyle);
//} else {
//    R38Cell2.setCellValue("");
//    R38Cell2.setCellStyle(textStyle);
//}

// R39
					row = sheet.getRow(38);
					Cell R39Cell1 = row.createCell(2);
					if (record.getR39_lc_as_on_mar() != null) {
						R39Cell1.setCellValue(record.getR39_lc_as_on_mar().doubleValue());
						R39Cell1.setCellStyle(numberStyle);
					} else {
						R39Cell1.setCellValue("");
						R39Cell1.setCellStyle(textStyle);
					}

					Cell R39Cell2 = row.createCell(3);
					if (record.getR39_lc_as_on_sep() != null) {
						R39Cell2.setCellValue(record.getR39_lc_as_on_sep().doubleValue());
						R39Cell2.setCellStyle(numberStyle);
					} else {
						R39Cell2.setCellValue("");
						R39Cell2.setCellStyle(textStyle);
					}

// R40
					row = sheet.getRow(39);
					Cell R40Cell1 = row.createCell(2);
					if (record.getR40_lc_as_on_mar() != null) {
						R40Cell1.setCellValue(record.getR40_lc_as_on_mar().doubleValue());
						R40Cell1.setCellStyle(numberStyle);
					} else {
						R40Cell1.setCellValue("");
						R40Cell1.setCellStyle(textStyle);
					}

					Cell R40Cell2 = row.createCell(3);
					if (record.getR40_lc_as_on_sep() != null) {
						R40Cell2.setCellValue(record.getR40_lc_as_on_sep().doubleValue());
						R40Cell2.setCellStyle(numberStyle);
					} else {
						R40Cell2.setCellValue("");
						R40Cell2.setCellStyle(textStyle);
					}

// R41
					row = sheet.getRow(40);
					Cell R41Cell1 = row.createCell(2);
					if (record.getR41_lc_as_on_mar() != null) {
						R41Cell1.setCellValue(record.getR41_lc_as_on_mar().doubleValue());
						R41Cell1.setCellStyle(numberStyle);
					} else {
						R41Cell1.setCellValue("");
						R41Cell1.setCellStyle(textStyle);
					}

					Cell R41Cell2 = row.createCell(3);
					if (record.getR41_lc_as_on_sep() != null) {
						R41Cell2.setCellValue(record.getR41_lc_as_on_sep().doubleValue());
						R41Cell2.setCellStyle(numberStyle);
					} else {
						R41Cell2.setCellValue("");
						R41Cell2.setCellStyle(textStyle);
					}

// R42
					row = sheet.getRow(41);
					Cell R42Cell1 = row.createCell(2);
					if (record.getR42_lc_as_on_mar() != null) {
						R42Cell1.setCellValue(record.getR42_lc_as_on_mar().doubleValue());
						R42Cell1.setCellStyle(numberStyle);
					} else {
						R42Cell1.setCellValue("");
						R42Cell1.setCellStyle(textStyle);
					}

					Cell R42Cell2 = row.createCell(3);
					if (record.getR42_lc_as_on_sep() != null) {
						R42Cell2.setCellValue(record.getR42_lc_as_on_sep().doubleValue());
						R42Cell2.setCellStyle(numberStyle);
					} else {
						R42Cell2.setCellValue("");
						R42Cell2.setCellStyle(textStyle);
					}

// R43
					row = sheet.getRow(42);
					Cell R43Cell1 = row.createCell(2);
					if (record.getR43_lc_as_on_mar() != null) {
						R43Cell1.setCellValue(record.getR43_lc_as_on_mar().doubleValue());
						R43Cell1.setCellStyle(numberStyle);
					} else {
						R43Cell1.setCellValue("");
						R43Cell1.setCellStyle(textStyle);
					}

					Cell R43Cell2 = row.createCell(3);
					if (record.getR43_lc_as_on_sep() != null) {
						R43Cell2.setCellValue(record.getR43_lc_as_on_sep().doubleValue());
						R43Cell2.setCellStyle(numberStyle);
					} else {
						R43Cell2.setCellValue("");
						R43Cell2.setCellStyle(textStyle);
					}
// R44
					row = sheet.getRow(43);
					Cell R44Cell1 = row.createCell(2);
					if (record.getR44_lc_as_on_mar() != null) {
						R44Cell1.setCellValue(record.getR44_lc_as_on_mar().doubleValue());
						R44Cell1.setCellStyle(numberStyle);
					} else {
						R44Cell1.setCellValue("");
						R44Cell1.setCellStyle(textStyle);
					}

					Cell R44Cell2 = row.createCell(3);
					if (record.getR44_lc_as_on_sep() != null) {
						R44Cell2.setCellValue(record.getR44_lc_as_on_sep().doubleValue());
						R44Cell2.setCellStyle(numberStyle);
					} else {
						R44Cell2.setCellValue("");
						R44Cell2.setCellStyle(textStyle);
					}

// R45
//row = sheet.getRow(44);
//Cell R45Cell1 = row.createCell(2);
//if (record.getR45_lc_as_on_mar() != null) {
//    R45Cell1.setCellValue(record.getR45_lc_as_on_mar().doubleValue());
//    R45Cell1.setCellStyle(numberStyle);
//} else {
//    R45Cell1.setCellValue("");
//    R45Cell1.setCellStyle(textStyle);
//}
//
//Cell R45Cell2 = row.createCell(3);
//if (record.getR45_lc_as_on_sep() != null) {
//    R45Cell2.setCellValue(record.getR45_lc_as_on_sep().doubleValue());
//    R45Cell2.setCellStyle(numberStyle);
//} else {
//    R45Cell2.setCellValue("");
//    R45Cell2.setCellStyle(textStyle);
//}

// R46
//row = sheet.getRow(45);
//Cell R46Cell1 = row.createCell(2);
//if (record.getR46_lc_as_on_mar() != null) {
//    R46Cell1.setCellValue(record.getR46_lc_as_on_mar().doubleValue());
//    R46Cell1.setCellStyle(numberStyle);
//} else {
//    R46Cell1.setCellValue("");
//    R46Cell1.setCellStyle(textStyle);
//}
//
//Cell R46Cell2 = row.createCell(3);
//if (record.getR46_lc_as_on_sep() != null) {
//    R46Cell2.setCellValue(record.getR46_lc_as_on_sep().doubleValue());
//    R46Cell2.setCellStyle(numberStyle);
//} else {
//    R46Cell2.setCellValue("");
//    R46Cell2.setCellStyle(textStyle);
//}

// R47
					row = sheet.getRow(46);
					Cell R47Cell1 = row.createCell(2);
					if (record.getR47_lc_as_on_mar() != null) {
						R47Cell1.setCellValue(record.getR47_lc_as_on_mar().doubleValue());
						R47Cell1.setCellStyle(numberStyle);
					} else {
						R47Cell1.setCellValue("");
						R47Cell1.setCellStyle(textStyle);
					}

					Cell R47Cell2 = row.createCell(3);
					if (record.getR47_lc_as_on_sep() != null) {
						R47Cell2.setCellValue(record.getR47_lc_as_on_sep().doubleValue());
						R47Cell2.setCellStyle(numberStyle);
					} else {
						R47Cell2.setCellValue("");
						R47Cell2.setCellStyle(textStyle);
					}

// R48
					row = sheet.getRow(47);
					Cell R48Cell1 = row.createCell(2);
					if (record.getR48_lc_as_on_mar() != null) {
						R48Cell1.setCellValue(record.getR48_lc_as_on_mar().doubleValue());
						R48Cell1.setCellStyle(numberStyle);
					} else {
						R48Cell1.setCellValue("");
						R48Cell1.setCellStyle(textStyle);
					}

//Cell R48Cell2 = row.createCell(3);
//if (record.getR48_lc_as_on_sep() != null) {
//    R48Cell2.setCellValue(record.getR48_lc_as_on_sep().doubleValue());
//    R48Cell2.setCellStyle(numberStyle);
//} else {
//    R48Cell2.setCellValue("");
//    R48Cell2.setCellStyle(textStyle);
//}

// R49
					row = sheet.getRow(48);
					Cell R49Cell1 = row.createCell(2);
					if (record.getR49_lc_as_on_mar() != null) {
						R49Cell1.setCellValue(record.getR49_lc_as_on_mar().doubleValue());
						R49Cell1.setCellStyle(numberStyle);
					} else {
						R49Cell1.setCellValue("");
						R49Cell1.setCellStyle(textStyle);
					}

//Cell R49Cell2 = row.createCell(3);
//if (record.getR49_lc_as_on_sep() != null) {
//    R49Cell2.setCellValue(record.getR49_lc_as_on_sep().doubleValue());
//    R49Cell2.setCellStyle(numberStyle);
//} else {
//    R49Cell2.setCellValue("");
//    R49Cell2.setCellStyle(textStyle);
//}

// R50
					row = sheet.getRow(49);
					Cell R50Cell1 = row.createCell(2);
					if (record.getR50_lc_as_on_mar() != null) {
						R50Cell1.setCellValue(record.getR50_lc_as_on_mar().doubleValue());
						R50Cell1.setCellStyle(numberStyle);
					} else {
						R50Cell1.setCellValue("");
						R50Cell1.setCellStyle(textStyle);
					}

					Cell R50Cell2 = row.createCell(3);
					if (record.getR50_lc_as_on_sep() != null) {
						R50Cell2.setCellValue(record.getR50_lc_as_on_sep().doubleValue());
						R50Cell2.setCellStyle(numberStyle);
					} else {
						R50Cell2.setCellValue("");
						R50Cell2.setCellStyle(textStyle);
					}

// R51
					row = sheet.getRow(50);
					Cell R51Cell1 = row.createCell(2);
					if (record.getR51_lc_as_on_mar() != null) {
						R51Cell1.setCellValue(record.getR51_lc_as_on_mar().doubleValue());
						R51Cell1.setCellStyle(numberStyle);
					} else {
						R51Cell1.setCellValue("");
						R51Cell1.setCellStyle(textStyle);
					}

//Cell R51Cell2 = row.createCell(3);
//if (record.getR51_lc_as_on_sep() != null) {
//    R51Cell2.setCellValue(record.getR51_lc_as_on_sep().doubleValue());
//    R51Cell2.setCellStyle(numberStyle);
//} else {
//    R51Cell2.setCellValue("");
//    R51Cell2.setCellStyle(textStyle);
//}

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

	// Resubmission
	public List<Object[]> getCASH_FLOWResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<CASH_FLOW_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (CASH_FLOW_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				CASH_FLOW_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  CASH_FLOW  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}
}