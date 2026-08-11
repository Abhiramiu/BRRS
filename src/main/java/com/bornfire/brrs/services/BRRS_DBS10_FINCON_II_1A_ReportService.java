package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
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
public class BRRS_DBS10_FINCON_II_1A_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_DBS10_FINCON_II_1A_ReportService.class);

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
	public List<DBS10_FINCON_II_1A_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new DBS10_FINCON_II_1ARowMapper());
	}

	// Fetch data by report date
	public List<DBS10_FINCON_II_1A_Manual_Summary_Entity> getDataByDateManual(Date reportDate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new DBS10_FINCON_II_1AManualRowMapper());
	}

	public List<DBS10_FINCON_II_1A_Manual_Summary_Entity> getManualDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new DBS10_FINCON_II_1AManualRowMapper());
	}
	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getDBS10_FINCON_II_1AArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<DBS10_FINCON_II_1A_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new DBS10_FINCON_II_1ARowArchivalMapper());
	}

	public List<DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity> getdatabydateListarchivalManual(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_MANUAL_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new ADISB1ManualArchivalRowMapper());
	}
//GET ALL WITH VERSION

	public List<DBS10_FINCON_II_1A_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new DBS10_FINCON_II_1ARowArchivalMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<DBS10_FINCON_II_1A_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new DBS10_FINCON_II_1ARowDetailMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<DBS10_FINCON_II_1A_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new DBS10_FINCON_II_1ARowDetailMapper());
	}

// 3. PAGINATION

	public List<DBS10_FINCON_II_1A_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new DBS10_FINCON_II_1ARowDetailMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<DBS10_FINCON_II_1A_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new DBS10_FINCON_II_1ARowDetailMapper());
	}
// 6. BY ACCOUNT NUMBER

	public DBS10_FINCON_II_1A_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new DBS10_FINCON_II_1ARowDetailMapper());
	}

// 1. GET BY DATE + VERSION

//	public List<DBS10_FINCON_II_1A_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
//			String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL "
//				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
//				new DBS10_FINCON_II_1ARowArchivalDetailMapper());
//	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

//	public List<DBS10_FINCON_II_1A_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
//			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
//				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
//				new DBS10_FINCON_II_1ARowArchivalDetailMapper());
//	}

//For Resubmission
	public DBS10_FINCON_II_1A_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new DBS10_FINCON_II_1ARowDetailMapper());
	}

	public DBS10_FINCON_II_1A_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new DBS10_FINCON_II_1ARowDetailMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	public List<DBS10_FINCON_II_1A_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new DBS10_FINCON_II_1ARowArchivalDetailMapper());
	}

	public List<DBS10_FINCON_II_1A_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new DBS10_FINCON_II_1ARowArchivalDetailMapper());
	}
	// ROW MAPPER

	class DBS10_FINCON_II_1ARowMapper implements RowMapper<DBS10_FINCON_II_1A_Summary_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Summary_Entity obj = new DBS10_FINCON_II_1A_Summary_Entity();

			// R5
			obj.setR5_ENTITY(rs.getString("R5_ENTITY"));
			obj.setR5_PARTICULARS(rs.getString("R5_PARTICULARS"));
			obj.setR5_Y_SERIES_COLUMN(rs.getString("R5_Y_SERIES_COLUMN"));
			obj.setR5_AMOUNT_X010(rs.getBigDecimal("R5_AMOUNT_X010"));

// R6
			obj.setR6_ENTITY(rs.getString("R6_ENTITY"));
			obj.setR6_PARTICULARS(rs.getString("R6_PARTICULARS"));
			obj.setR6_Y_SERIES_COLUMN(rs.getString("R6_Y_SERIES_COLUMN"));
			obj.setR6_AMOUNT_X010(rs.getBigDecimal("R6_AMOUNT_X010"));

// R7
			obj.setR7_ENTITY(rs.getString("R7_ENTITY"));
			obj.setR7_PARTICULARS(rs.getString("R7_PARTICULARS"));
			obj.setR7_Y_SERIES_COLUMN(rs.getString("R7_Y_SERIES_COLUMN"));
			obj.setR7_AMOUNT_X010(rs.getBigDecimal("R7_AMOUNT_X010"));

// R8
			obj.setR8_ENTITY(rs.getString("R8_ENTITY"));
			obj.setR8_PARTICULARS(rs.getString("R8_PARTICULARS"));
			obj.setR8_Y_SERIES_COLUMN(rs.getString("R8_Y_SERIES_COLUMN"));
			obj.setR8_AMOUNT_X010(rs.getBigDecimal("R8_AMOUNT_X010"));

// R9
			obj.setR9_ENTITY(rs.getString("R9_ENTITY"));
			obj.setR9_PARTICULARS(rs.getString("R9_PARTICULARS"));
			obj.setR9_Y_SERIES_COLUMN(rs.getString("R9_Y_SERIES_COLUMN"));
			obj.setR9_AMOUNT_X010(rs.getBigDecimal("R9_AMOUNT_X010"));

// R10
			obj.setR10_ENTITY(rs.getString("R10_ENTITY"));
			obj.setR10_PARTICULARS(rs.getString("R10_PARTICULARS"));
			obj.setR10_Y_SERIES_COLUMN(rs.getString("R10_Y_SERIES_COLUMN"));
			obj.setR10_AMOUNT_X010(rs.getBigDecimal("R10_AMOUNT_X010"));

// R11
			obj.setR11_ENTITY(rs.getString("R11_ENTITY"));
			obj.setR11_PARTICULARS(rs.getString("R11_PARTICULARS"));
			obj.setR11_Y_SERIES_COLUMN(rs.getString("R11_Y_SERIES_COLUMN"));
			obj.setR11_AMOUNT_X010(rs.getBigDecimal("R11_AMOUNT_X010"));

// R12
			obj.setR12_ENTITY(rs.getString("R12_ENTITY"));
			obj.setR12_PARTICULARS(rs.getString("R12_PARTICULARS"));
			obj.setR12_Y_SERIES_COLUMN(rs.getString("R12_Y_SERIES_COLUMN"));
			obj.setR12_AMOUNT_X010(rs.getBigDecimal("R12_AMOUNT_X010"));

// R13
			obj.setR13_ENTITY(rs.getString("R13_ENTITY"));
			obj.setR13_PARTICULARS(rs.getString("R13_PARTICULARS"));
			obj.setR13_Y_SERIES_COLUMN(rs.getString("R13_Y_SERIES_COLUMN"));
			obj.setR13_AMOUNT_X010(rs.getBigDecimal("R13_AMOUNT_X010"));

// R14
			obj.setR14_ENTITY(rs.getString("R14_ENTITY"));
			obj.setR14_PARTICULARS(rs.getString("R14_PARTICULARS"));
			obj.setR14_Y_SERIES_COLUMN(rs.getString("R14_Y_SERIES_COLUMN"));
			obj.setR14_AMOUNT_X010(rs.getBigDecimal("R14_AMOUNT_X010"));

// R15
			obj.setR15_ENTITY(rs.getString("R15_ENTITY"));
			obj.setR15_PARTICULARS(rs.getString("R15_PARTICULARS"));
			obj.setR15_Y_SERIES_COLUMN(rs.getString("R15_Y_SERIES_COLUMN"));
			obj.setR15_AMOUNT_X010(rs.getBigDecimal("R15_AMOUNT_X010"));

// R16
			obj.setR16_ENTITY(rs.getString("R16_ENTITY"));
			obj.setR16_PARTICULARS(rs.getString("R16_PARTICULARS"));
			obj.setR16_Y_SERIES_COLUMN(rs.getString("R16_Y_SERIES_COLUMN"));
			obj.setR16_AMOUNT_X010(rs.getBigDecimal("R16_AMOUNT_X010"));

// R17
			obj.setR17_ENTITY(rs.getString("R17_ENTITY"));
			obj.setR17_PARTICULARS(rs.getString("R17_PARTICULARS"));
			obj.setR17_Y_SERIES_COLUMN(rs.getString("R17_Y_SERIES_COLUMN"));
			obj.setR17_AMOUNT_X010(rs.getBigDecimal("R17_AMOUNT_X010"));

// R18
			obj.setR18_ENTITY(rs.getString("R18_ENTITY"));
			obj.setR18_PARTICULARS(rs.getString("R18_PARTICULARS"));
			obj.setR18_Y_SERIES_COLUMN(rs.getString("R18_Y_SERIES_COLUMN"));
			obj.setR18_AMOUNT_X010(rs.getBigDecimal("R18_AMOUNT_X010"));

// R19
			obj.setR19_ENTITY(rs.getString("R19_ENTITY"));
			obj.setR19_PARTICULARS(rs.getString("R19_PARTICULARS"));
			obj.setR19_Y_SERIES_COLUMN(rs.getString("R19_Y_SERIES_COLUMN"));
			obj.setR19_AMOUNT_X010(rs.getBigDecimal("R19_AMOUNT_X010"));

// R20
			obj.setR20_ENTITY(rs.getString("R20_ENTITY"));
			obj.setR20_PARTICULARS(rs.getString("R20_PARTICULARS"));
			obj.setR20_Y_SERIES_COLUMN(rs.getString("R20_Y_SERIES_COLUMN"));
			obj.setR20_AMOUNT_X010(rs.getBigDecimal("R20_AMOUNT_X010"));

// R21
			obj.setR21_ENTITY(rs.getString("R21_ENTITY"));
			obj.setR21_PARTICULARS(rs.getString("R21_PARTICULARS"));
			obj.setR21_Y_SERIES_COLUMN(rs.getString("R21_Y_SERIES_COLUMN"));
			obj.setR21_AMOUNT_X010(rs.getBigDecimal("R21_AMOUNT_X010"));

// R22
			obj.setR22_ENTITY(rs.getString("R22_ENTITY"));
			obj.setR22_PARTICULARS(rs.getString("R22_PARTICULARS"));
			obj.setR22_Y_SERIES_COLUMN(rs.getString("R22_Y_SERIES_COLUMN"));
			obj.setR22_AMOUNT_X010(rs.getBigDecimal("R22_AMOUNT_X010"));

// R23
			obj.setR23_ENTITY(rs.getString("R23_ENTITY"));
			obj.setR23_PARTICULARS(rs.getString("R23_PARTICULARS"));
			obj.setR23_Y_SERIES_COLUMN(rs.getString("R23_Y_SERIES_COLUMN"));
			obj.setR23_AMOUNT_X010(rs.getBigDecimal("R23_AMOUNT_X010"));

// R24
			obj.setR24_ENTITY(rs.getString("R24_ENTITY"));
			obj.setR24_PARTICULARS(rs.getString("R24_PARTICULARS"));
			obj.setR24_Y_SERIES_COLUMN(rs.getString("R24_Y_SERIES_COLUMN"));
			obj.setR24_AMOUNT_X010(rs.getBigDecimal("R24_AMOUNT_X010"));

// R25
			obj.setR25_ENTITY(rs.getString("R25_ENTITY"));
			obj.setR25_PARTICULARS(rs.getString("R25_PARTICULARS"));
			obj.setR25_Y_SERIES_COLUMN(rs.getString("R25_Y_SERIES_COLUMN"));
			obj.setR25_AMOUNT_X010(rs.getBigDecimal("R25_AMOUNT_X010"));

// R26
			obj.setR26_ENTITY(rs.getString("R26_ENTITY"));
			obj.setR26_PARTICULARS(rs.getString("R26_PARTICULARS"));
			obj.setR26_Y_SERIES_COLUMN(rs.getString("R26_Y_SERIES_COLUMN"));
			obj.setR26_AMOUNT_X010(rs.getBigDecimal("R26_AMOUNT_X010"));

// R27
			obj.setR27_ENTITY(rs.getString("R27_ENTITY"));
			obj.setR27_PARTICULARS(rs.getString("R27_PARTICULARS"));
			obj.setR27_Y_SERIES_COLUMN(rs.getString("R27_Y_SERIES_COLUMN"));
			obj.setR27_AMOUNT_X010(rs.getBigDecimal("R27_AMOUNT_X010"));

// R28
			obj.setR28_ENTITY(rs.getString("R28_ENTITY"));
			obj.setR28_PARTICULARS(rs.getString("R28_PARTICULARS"));
			obj.setR28_Y_SERIES_COLUMN(rs.getString("R28_Y_SERIES_COLUMN"));
			obj.setR28_AMOUNT_X010(rs.getBigDecimal("R28_AMOUNT_X010"));

// R29
			obj.setR29_ENTITY(rs.getString("R29_ENTITY"));
			obj.setR29_PARTICULARS(rs.getString("R29_PARTICULARS"));
			obj.setR29_Y_SERIES_COLUMN(rs.getString("R29_Y_SERIES_COLUMN"));
			obj.setR29_AMOUNT_X010(rs.getBigDecimal("R29_AMOUNT_X010"));

// R30
			obj.setR30_ENTITY(rs.getString("R30_ENTITY"));
			obj.setR30_PARTICULARS(rs.getString("R30_PARTICULARS"));
			obj.setR30_Y_SERIES_COLUMN(rs.getString("R30_Y_SERIES_COLUMN"));
			obj.setR30_AMOUNT_X010(rs.getBigDecimal("R30_AMOUNT_X010"));

// R31
			obj.setR31_ENTITY(rs.getString("R31_ENTITY"));
			obj.setR31_PARTICULARS(rs.getString("R31_PARTICULARS"));
			obj.setR31_Y_SERIES_COLUMN(rs.getString("R31_Y_SERIES_COLUMN"));
			obj.setR31_AMOUNT_X010(rs.getBigDecimal("R31_AMOUNT_X010"));

// R32
			obj.setR32_ENTITY(rs.getString("R32_ENTITY"));
			obj.setR32_PARTICULARS(rs.getString("R32_PARTICULARS"));
			obj.setR32_Y_SERIES_COLUMN(rs.getString("R32_Y_SERIES_COLUMN"));
			obj.setR32_AMOUNT_X010(rs.getBigDecimal("R32_AMOUNT_X010"));

// R33
			obj.setR33_ENTITY(rs.getString("R33_ENTITY"));
			obj.setR33_PARTICULARS(rs.getString("R33_PARTICULARS"));
			obj.setR33_Y_SERIES_COLUMN(rs.getString("R33_Y_SERIES_COLUMN"));
			obj.setR33_AMOUNT_X010(rs.getBigDecimal("R33_AMOUNT_X010"));

// R34
			obj.setR34_ENTITY(rs.getString("R34_ENTITY"));
			obj.setR34_PARTICULARS(rs.getString("R34_PARTICULARS"));
			obj.setR34_Y_SERIES_COLUMN(rs.getString("R34_Y_SERIES_COLUMN"));
			obj.setR34_AMOUNT_X010(rs.getBigDecimal("R34_AMOUNT_X010"));

// R35
			obj.setR35_ENTITY(rs.getString("R35_ENTITY"));
			obj.setR35_PARTICULARS(rs.getString("R35_PARTICULARS"));
			obj.setR35_Y_SERIES_COLUMN(rs.getString("R35_Y_SERIES_COLUMN"));
			obj.setR35_AMOUNT_X010(rs.getBigDecimal("R35_AMOUNT_X010"));

// R36
			obj.setR36_ENTITY(rs.getString("R36_ENTITY"));
			obj.setR36_PARTICULARS(rs.getString("R36_PARTICULARS"));
			obj.setR36_Y_SERIES_COLUMN(rs.getString("R36_Y_SERIES_COLUMN"));
			obj.setR36_AMOUNT_X010(rs.getBigDecimal("R36_AMOUNT_X010"));

// R37
			obj.setR37_ENTITY(rs.getString("R37_ENTITY"));
			obj.setR37_PARTICULARS(rs.getString("R37_PARTICULARS"));
			obj.setR37_Y_SERIES_COLUMN(rs.getString("R37_Y_SERIES_COLUMN"));
			obj.setR37_AMOUNT_X010(rs.getBigDecimal("R37_AMOUNT_X010"));

// R38
			obj.setR38_ENTITY(rs.getString("R38_ENTITY"));
			obj.setR38_PARTICULARS(rs.getString("R38_PARTICULARS"));
			obj.setR38_Y_SERIES_COLUMN(rs.getString("R38_Y_SERIES_COLUMN"));
			obj.setR38_AMOUNT_X010(rs.getBigDecimal("R38_AMOUNT_X010"));

// R39
			obj.setR39_ENTITY(rs.getString("R39_ENTITY"));
			obj.setR39_PARTICULARS(rs.getString("R39_PARTICULARS"));
			obj.setR39_Y_SERIES_COLUMN(rs.getString("R39_Y_SERIES_COLUMN"));
			obj.setR39_AMOUNT_X010(rs.getBigDecimal("R39_AMOUNT_X010"));

// R40
			obj.setR40_ENTITY(rs.getString("R40_ENTITY"));
			obj.setR40_PARTICULARS(rs.getString("R40_PARTICULARS"));
			obj.setR40_Y_SERIES_COLUMN(rs.getString("R40_Y_SERIES_COLUMN"));
			obj.setR40_AMOUNT_X010(rs.getBigDecimal("R40_AMOUNT_X010"));

// R41
			obj.setR41_ENTITY(rs.getString("R41_ENTITY"));
			obj.setR41_PARTICULARS(rs.getString("R41_PARTICULARS"));
			obj.setR41_Y_SERIES_COLUMN(rs.getString("R41_Y_SERIES_COLUMN"));
			obj.setR41_AMOUNT_X010(rs.getBigDecimal("R41_AMOUNT_X010"));

// R42
			obj.setR42_ENTITY(rs.getString("R42_ENTITY"));
			obj.setR42_PARTICULARS(rs.getString("R42_PARTICULARS"));
			obj.setR42_Y_SERIES_COLUMN(rs.getString("R42_Y_SERIES_COLUMN"));
			obj.setR42_AMOUNT_X010(rs.getBigDecimal("R42_AMOUNT_X010"));

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

	public static class DBS10_FINCON_II_1A_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private String R5_ENTITY;
		private String R5_PARTICULARS;
		private String R5_Y_SERIES_COLUMN;
		private BigDecimal R5_AMOUNT_X010;

		private String R6_ENTITY;
		private String R6_PARTICULARS;
		private String R6_Y_SERIES_COLUMN;
		private BigDecimal R6_AMOUNT_X010;

		private String R7_ENTITY;
		private String R7_PARTICULARS;
		private String R7_Y_SERIES_COLUMN;
		private BigDecimal R7_AMOUNT_X010;

		private String R8_ENTITY;
		private String R8_PARTICULARS;
		private String R8_Y_SERIES_COLUMN;
		private BigDecimal R8_AMOUNT_X010;

		private String R9_ENTITY;
		private String R9_PARTICULARS;
		private String R9_Y_SERIES_COLUMN;
		private BigDecimal R9_AMOUNT_X010;

		private String R10_ENTITY;
		private String R10_PARTICULARS;
		private String R10_Y_SERIES_COLUMN;
		private BigDecimal R10_AMOUNT_X010;

		private String R11_ENTITY;
		private String R11_PARTICULARS;
		private String R11_Y_SERIES_COLUMN;
		private BigDecimal R11_AMOUNT_X010;

		private String R12_ENTITY;
		private String R12_PARTICULARS;
		private String R12_Y_SERIES_COLUMN;
		private BigDecimal R12_AMOUNT_X010;

		private String R13_ENTITY;
		private String R13_PARTICULARS;
		private String R13_Y_SERIES_COLUMN;
		private BigDecimal R13_AMOUNT_X010;

		private String R14_ENTITY;
		private String R14_PARTICULARS;
		private String R14_Y_SERIES_COLUMN;
		private BigDecimal R14_AMOUNT_X010;

		private String R15_ENTITY;
		private String R15_PARTICULARS;
		private String R15_Y_SERIES_COLUMN;
		private BigDecimal R15_AMOUNT_X010;

		private String R16_ENTITY;
		private String R16_PARTICULARS;
		private String R16_Y_SERIES_COLUMN;
		private BigDecimal R16_AMOUNT_X010;

		private String R17_ENTITY;
		private String R17_PARTICULARS;
		private String R17_Y_SERIES_COLUMN;
		private BigDecimal R17_AMOUNT_X010;

		private String R18_ENTITY;
		private String R18_PARTICULARS;
		private String R18_Y_SERIES_COLUMN;
		private BigDecimal R18_AMOUNT_X010;

		private String R19_ENTITY;
		private String R19_PARTICULARS;
		private String R19_Y_SERIES_COLUMN;
		private BigDecimal R19_AMOUNT_X010;

		private String R20_ENTITY;
		private String R20_PARTICULARS;
		private String R20_Y_SERIES_COLUMN;
		private BigDecimal R20_AMOUNT_X010;

		private String R21_ENTITY;
		private String R21_PARTICULARS;
		private String R21_Y_SERIES_COLUMN;
		private BigDecimal R21_AMOUNT_X010;

		private String R22_ENTITY;
		private String R22_PARTICULARS;
		private String R22_Y_SERIES_COLUMN;
		private BigDecimal R22_AMOUNT_X010;

		private String R23_ENTITY;
		private String R23_PARTICULARS;
		private String R23_Y_SERIES_COLUMN;
		private BigDecimal R23_AMOUNT_X010;

		private String R24_ENTITY;
		private String R24_PARTICULARS;
		private String R24_Y_SERIES_COLUMN;
		private BigDecimal R24_AMOUNT_X010;

		private String R25_ENTITY;
		private String R25_PARTICULARS;
		private String R25_Y_SERIES_COLUMN;
		private BigDecimal R25_AMOUNT_X010;

		private String R26_ENTITY;
		private String R26_PARTICULARS;
		private String R26_Y_SERIES_COLUMN;
		private BigDecimal R26_AMOUNT_X010;

		private String R27_ENTITY;
		private String R27_PARTICULARS;
		private String R27_Y_SERIES_COLUMN;
		private BigDecimal R27_AMOUNT_X010;

		private String R28_ENTITY;
		private String R28_PARTICULARS;
		private String R28_Y_SERIES_COLUMN;
		private BigDecimal R28_AMOUNT_X010;

		private String R29_ENTITY;
		private String R29_PARTICULARS;
		private String R29_Y_SERIES_COLUMN;
		private BigDecimal R29_AMOUNT_X010;

		private String R30_ENTITY;
		private String R30_PARTICULARS;
		private String R30_Y_SERIES_COLUMN;
		private BigDecimal R30_AMOUNT_X010;

		private String R31_ENTITY;
		private String R31_PARTICULARS;
		private String R31_Y_SERIES_COLUMN;
		private BigDecimal R31_AMOUNT_X010;

		private String R32_ENTITY;
		private String R32_PARTICULARS;
		private String R32_Y_SERIES_COLUMN;
		private BigDecimal R32_AMOUNT_X010;

		private String R33_ENTITY;
		private String R33_PARTICULARS;
		private String R33_Y_SERIES_COLUMN;
		private BigDecimal R33_AMOUNT_X010;

		private String R34_ENTITY;
		private String R34_PARTICULARS;
		private String R34_Y_SERIES_COLUMN;
		private BigDecimal R34_AMOUNT_X010;

		private String R35_ENTITY;
		private String R35_PARTICULARS;
		private String R35_Y_SERIES_COLUMN;
		private BigDecimal R35_AMOUNT_X010;

		private String R36_ENTITY;
		private String R36_PARTICULARS;
		private String R36_Y_SERIES_COLUMN;
		private BigDecimal R36_AMOUNT_X010;

		private String R37_ENTITY;
		private String R37_PARTICULARS;
		private String R37_Y_SERIES_COLUMN;
		private BigDecimal R37_AMOUNT_X010;

		private String R38_ENTITY;
		private String R38_PARTICULARS;
		private String R38_Y_SERIES_COLUMN;
		private BigDecimal R38_AMOUNT_X010;

		private String R39_ENTITY;
		private String R39_PARTICULARS;
		private String R39_Y_SERIES_COLUMN;
		private BigDecimal R39_AMOUNT_X010;

		private String R40_ENTITY;
		private String R40_PARTICULARS;
		private String R40_Y_SERIES_COLUMN;
		private BigDecimal R40_AMOUNT_X010;

		private String R41_ENTITY;
		private String R41_PARTICULARS;
		private String R41_Y_SERIES_COLUMN;
		private BigDecimal R41_AMOUNT_X010;

		private String R42_ENTITY;
		private String R42_PARTICULARS;
		private String R42_Y_SERIES_COLUMN;
		private BigDecimal R42_AMOUNT_X010;

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

		public String getR5_ENTITY() {
			return R5_ENTITY;
		}

		public void setR5_ENTITY(String r5_ENTITY) {
			R5_ENTITY = r5_ENTITY;
		}

		public String getR5_PARTICULARS() {
			return R5_PARTICULARS;
		}

		public void setR5_PARTICULARS(String r5_PARTICULARS) {
			R5_PARTICULARS = r5_PARTICULARS;
		}

		public String getR5_Y_SERIES_COLUMN() {
			return R5_Y_SERIES_COLUMN;
		}

		public void setR5_Y_SERIES_COLUMN(String r5_Y_SERIES_COLUMN) {
			R5_Y_SERIES_COLUMN = r5_Y_SERIES_COLUMN;
		}

		public BigDecimal getR5_AMOUNT_X010() {
			return R5_AMOUNT_X010;
		}

		public void setR5_AMOUNT_X010(BigDecimal r5_AMOUNT_X010) {
			R5_AMOUNT_X010 = r5_AMOUNT_X010;
		}

		public String getR6_ENTITY() {
			return R6_ENTITY;
		}

		public void setR6_ENTITY(String r6_ENTITY) {
			R6_ENTITY = r6_ENTITY;
		}

		public String getR6_PARTICULARS() {
			return R6_PARTICULARS;
		}

		public void setR6_PARTICULARS(String r6_PARTICULARS) {
			R6_PARTICULARS = r6_PARTICULARS;
		}

		public String getR6_Y_SERIES_COLUMN() {
			return R6_Y_SERIES_COLUMN;
		}

		public void setR6_Y_SERIES_COLUMN(String r6_Y_SERIES_COLUMN) {
			R6_Y_SERIES_COLUMN = r6_Y_SERIES_COLUMN;
		}

		public BigDecimal getR6_AMOUNT_X010() {
			return R6_AMOUNT_X010;
		}

		public void setR6_AMOUNT_X010(BigDecimal r6_AMOUNT_X010) {
			R6_AMOUNT_X010 = r6_AMOUNT_X010;
		}

		public String getR7_ENTITY() {
			return R7_ENTITY;
		}

		public void setR7_ENTITY(String r7_ENTITY) {
			R7_ENTITY = r7_ENTITY;
		}

		public String getR7_PARTICULARS() {
			return R7_PARTICULARS;
		}

		public void setR7_PARTICULARS(String r7_PARTICULARS) {
			R7_PARTICULARS = r7_PARTICULARS;
		}

		public String getR7_Y_SERIES_COLUMN() {
			return R7_Y_SERIES_COLUMN;
		}

		public void setR7_Y_SERIES_COLUMN(String r7_Y_SERIES_COLUMN) {
			R7_Y_SERIES_COLUMN = r7_Y_SERIES_COLUMN;
		}

		public BigDecimal getR7_AMOUNT_X010() {
			return R7_AMOUNT_X010;
		}

		public void setR7_AMOUNT_X010(BigDecimal r7_AMOUNT_X010) {
			R7_AMOUNT_X010 = r7_AMOUNT_X010;
		}

		public String getR8_ENTITY() {
			return R8_ENTITY;
		}

		public void setR8_ENTITY(String r8_ENTITY) {
			R8_ENTITY = r8_ENTITY;
		}

		public String getR8_PARTICULARS() {
			return R8_PARTICULARS;
		}

		public void setR8_PARTICULARS(String r8_PARTICULARS) {
			R8_PARTICULARS = r8_PARTICULARS;
		}

		public String getR8_Y_SERIES_COLUMN() {
			return R8_Y_SERIES_COLUMN;
		}

		public void setR8_Y_SERIES_COLUMN(String r8_Y_SERIES_COLUMN) {
			R8_Y_SERIES_COLUMN = r8_Y_SERIES_COLUMN;
		}

		public BigDecimal getR8_AMOUNT_X010() {
			return R8_AMOUNT_X010;
		}

		public void setR8_AMOUNT_X010(BigDecimal r8_AMOUNT_X010) {
			R8_AMOUNT_X010 = r8_AMOUNT_X010;
		}

		public String getR9_ENTITY() {
			return R9_ENTITY;
		}

		public void setR9_ENTITY(String r9_ENTITY) {
			R9_ENTITY = r9_ENTITY;
		}

		public String getR9_PARTICULARS() {
			return R9_PARTICULARS;
		}

		public void setR9_PARTICULARS(String r9_PARTICULARS) {
			R9_PARTICULARS = r9_PARTICULARS;
		}

		public String getR9_Y_SERIES_COLUMN() {
			return R9_Y_SERIES_COLUMN;
		}

		public void setR9_Y_SERIES_COLUMN(String r9_Y_SERIES_COLUMN) {
			R9_Y_SERIES_COLUMN = r9_Y_SERIES_COLUMN;
		}

		public BigDecimal getR9_AMOUNT_X010() {
			return R9_AMOUNT_X010;
		}

		public void setR9_AMOUNT_X010(BigDecimal r9_AMOUNT_X010) {
			R9_AMOUNT_X010 = r9_AMOUNT_X010;
		}

		public String getR10_ENTITY() {
			return R10_ENTITY;
		}

		public void setR10_ENTITY(String r10_ENTITY) {
			R10_ENTITY = r10_ENTITY;
		}

		public String getR10_PARTICULARS() {
			return R10_PARTICULARS;
		}

		public void setR10_PARTICULARS(String r10_PARTICULARS) {
			R10_PARTICULARS = r10_PARTICULARS;
		}

		public String getR10_Y_SERIES_COLUMN() {
			return R10_Y_SERIES_COLUMN;
		}

		public void setR10_Y_SERIES_COLUMN(String r10_Y_SERIES_COLUMN) {
			R10_Y_SERIES_COLUMN = r10_Y_SERIES_COLUMN;
		}

		public BigDecimal getR10_AMOUNT_X010() {
			return R10_AMOUNT_X010;
		}

		public void setR10_AMOUNT_X010(BigDecimal r10_AMOUNT_X010) {
			R10_AMOUNT_X010 = r10_AMOUNT_X010;
		}

		public String getR11_ENTITY() {
			return R11_ENTITY;
		}

		public void setR11_ENTITY(String r11_ENTITY) {
			R11_ENTITY = r11_ENTITY;
		}

		public String getR11_PARTICULARS() {
			return R11_PARTICULARS;
		}

		public void setR11_PARTICULARS(String r11_PARTICULARS) {
			R11_PARTICULARS = r11_PARTICULARS;
		}

		public String getR11_Y_SERIES_COLUMN() {
			return R11_Y_SERIES_COLUMN;
		}

		public void setR11_Y_SERIES_COLUMN(String r11_Y_SERIES_COLUMN) {
			R11_Y_SERIES_COLUMN = r11_Y_SERIES_COLUMN;
		}

		public BigDecimal getR11_AMOUNT_X010() {
			return R11_AMOUNT_X010;
		}

		public void setR11_AMOUNT_X010(BigDecimal r11_AMOUNT_X010) {
			R11_AMOUNT_X010 = r11_AMOUNT_X010;
		}

		public String getR12_ENTITY() {
			return R12_ENTITY;
		}

		public void setR12_ENTITY(String r12_ENTITY) {
			R12_ENTITY = r12_ENTITY;
		}

		public String getR12_PARTICULARS() {
			return R12_PARTICULARS;
		}

		public void setR12_PARTICULARS(String r12_PARTICULARS) {
			R12_PARTICULARS = r12_PARTICULARS;
		}

		public String getR12_Y_SERIES_COLUMN() {
			return R12_Y_SERIES_COLUMN;
		}

		public void setR12_Y_SERIES_COLUMN(String r12_Y_SERIES_COLUMN) {
			R12_Y_SERIES_COLUMN = r12_Y_SERIES_COLUMN;
		}

		public BigDecimal getR12_AMOUNT_X010() {
			return R12_AMOUNT_X010;
		}

		public void setR12_AMOUNT_X010(BigDecimal r12_AMOUNT_X010) {
			R12_AMOUNT_X010 = r12_AMOUNT_X010;
		}

		public String getR13_ENTITY() {
			return R13_ENTITY;
		}

		public void setR13_ENTITY(String r13_ENTITY) {
			R13_ENTITY = r13_ENTITY;
		}

		public String getR13_PARTICULARS() {
			return R13_PARTICULARS;
		}

		public void setR13_PARTICULARS(String r13_PARTICULARS) {
			R13_PARTICULARS = r13_PARTICULARS;
		}

		public String getR13_Y_SERIES_COLUMN() {
			return R13_Y_SERIES_COLUMN;
		}

		public void setR13_Y_SERIES_COLUMN(String r13_Y_SERIES_COLUMN) {
			R13_Y_SERIES_COLUMN = r13_Y_SERIES_COLUMN;
		}

		public BigDecimal getR13_AMOUNT_X010() {
			return R13_AMOUNT_X010;
		}

		public void setR13_AMOUNT_X010(BigDecimal r13_AMOUNT_X010) {
			R13_AMOUNT_X010 = r13_AMOUNT_X010;
		}

		public String getR14_ENTITY() {
			return R14_ENTITY;
		}

		public void setR14_ENTITY(String r14_ENTITY) {
			R14_ENTITY = r14_ENTITY;
		}

		public String getR14_PARTICULARS() {
			return R14_PARTICULARS;
		}

		public void setR14_PARTICULARS(String r14_PARTICULARS) {
			R14_PARTICULARS = r14_PARTICULARS;
		}

		public String getR14_Y_SERIES_COLUMN() {
			return R14_Y_SERIES_COLUMN;
		}

		public void setR14_Y_SERIES_COLUMN(String r14_Y_SERIES_COLUMN) {
			R14_Y_SERIES_COLUMN = r14_Y_SERIES_COLUMN;
		}

		public BigDecimal getR14_AMOUNT_X010() {
			return R14_AMOUNT_X010;
		}

		public void setR14_AMOUNT_X010(BigDecimal r14_AMOUNT_X010) {
			R14_AMOUNT_X010 = r14_AMOUNT_X010;
		}

		public String getR15_ENTITY() {
			return R15_ENTITY;
		}

		public void setR15_ENTITY(String r15_ENTITY) {
			R15_ENTITY = r15_ENTITY;
		}

		public String getR15_PARTICULARS() {
			return R15_PARTICULARS;
		}

		public void setR15_PARTICULARS(String r15_PARTICULARS) {
			R15_PARTICULARS = r15_PARTICULARS;
		}

		public String getR15_Y_SERIES_COLUMN() {
			return R15_Y_SERIES_COLUMN;
		}

		public void setR15_Y_SERIES_COLUMN(String r15_Y_SERIES_COLUMN) {
			R15_Y_SERIES_COLUMN = r15_Y_SERIES_COLUMN;
		}

		public BigDecimal getR15_AMOUNT_X010() {
			return R15_AMOUNT_X010;
		}

		public void setR15_AMOUNT_X010(BigDecimal r15_AMOUNT_X010) {
			R15_AMOUNT_X010 = r15_AMOUNT_X010;
		}

		public String getR16_ENTITY() {
			return R16_ENTITY;
		}

		public void setR16_ENTITY(String r16_ENTITY) {
			R16_ENTITY = r16_ENTITY;
		}

		public String getR16_PARTICULARS() {
			return R16_PARTICULARS;
		}

		public void setR16_PARTICULARS(String r16_PARTICULARS) {
			R16_PARTICULARS = r16_PARTICULARS;
		}

		public String getR16_Y_SERIES_COLUMN() {
			return R16_Y_SERIES_COLUMN;
		}

		public void setR16_Y_SERIES_COLUMN(String r16_Y_SERIES_COLUMN) {
			R16_Y_SERIES_COLUMN = r16_Y_SERIES_COLUMN;
		}

		public BigDecimal getR16_AMOUNT_X010() {
			return R16_AMOUNT_X010;
		}

		public void setR16_AMOUNT_X010(BigDecimal r16_AMOUNT_X010) {
			R16_AMOUNT_X010 = r16_AMOUNT_X010;
		}

		public String getR17_ENTITY() {
			return R17_ENTITY;
		}

		public void setR17_ENTITY(String r17_ENTITY) {
			R17_ENTITY = r17_ENTITY;
		}

		public String getR17_PARTICULARS() {
			return R17_PARTICULARS;
		}

		public void setR17_PARTICULARS(String r17_PARTICULARS) {
			R17_PARTICULARS = r17_PARTICULARS;
		}

		public String getR17_Y_SERIES_COLUMN() {
			return R17_Y_SERIES_COLUMN;
		}

		public void setR17_Y_SERIES_COLUMN(String r17_Y_SERIES_COLUMN) {
			R17_Y_SERIES_COLUMN = r17_Y_SERIES_COLUMN;
		}

		public BigDecimal getR17_AMOUNT_X010() {
			return R17_AMOUNT_X010;
		}

		public void setR17_AMOUNT_X010(BigDecimal r17_AMOUNT_X010) {
			R17_AMOUNT_X010 = r17_AMOUNT_X010;
		}

		public String getR18_ENTITY() {
			return R18_ENTITY;
		}

		public void setR18_ENTITY(String r18_ENTITY) {
			R18_ENTITY = r18_ENTITY;
		}

		public String getR18_PARTICULARS() {
			return R18_PARTICULARS;
		}

		public void setR18_PARTICULARS(String r18_PARTICULARS) {
			R18_PARTICULARS = r18_PARTICULARS;
		}

		public String getR18_Y_SERIES_COLUMN() {
			return R18_Y_SERIES_COLUMN;
		}

		public void setR18_Y_SERIES_COLUMN(String r18_Y_SERIES_COLUMN) {
			R18_Y_SERIES_COLUMN = r18_Y_SERIES_COLUMN;
		}

		public BigDecimal getR18_AMOUNT_X010() {
			return R18_AMOUNT_X010;
		}

		public void setR18_AMOUNT_X010(BigDecimal r18_AMOUNT_X010) {
			R18_AMOUNT_X010 = r18_AMOUNT_X010;
		}

		public String getR19_ENTITY() {
			return R19_ENTITY;
		}

		public void setR19_ENTITY(String r19_ENTITY) {
			R19_ENTITY = r19_ENTITY;
		}

		public String getR19_PARTICULARS() {
			return R19_PARTICULARS;
		}

		public void setR19_PARTICULARS(String r19_PARTICULARS) {
			R19_PARTICULARS = r19_PARTICULARS;
		}

		public String getR19_Y_SERIES_COLUMN() {
			return R19_Y_SERIES_COLUMN;
		}

		public void setR19_Y_SERIES_COLUMN(String r19_Y_SERIES_COLUMN) {
			R19_Y_SERIES_COLUMN = r19_Y_SERIES_COLUMN;
		}

		public BigDecimal getR19_AMOUNT_X010() {
			return R19_AMOUNT_X010;
		}

		public void setR19_AMOUNT_X010(BigDecimal r19_AMOUNT_X010) {
			R19_AMOUNT_X010 = r19_AMOUNT_X010;
		}

		public String getR20_ENTITY() {
			return R20_ENTITY;
		}

		public void setR20_ENTITY(String r20_ENTITY) {
			R20_ENTITY = r20_ENTITY;
		}

		public String getR20_PARTICULARS() {
			return R20_PARTICULARS;
		}

		public void setR20_PARTICULARS(String r20_PARTICULARS) {
			R20_PARTICULARS = r20_PARTICULARS;
		}

		public String getR20_Y_SERIES_COLUMN() {
			return R20_Y_SERIES_COLUMN;
		}

		public void setR20_Y_SERIES_COLUMN(String r20_Y_SERIES_COLUMN) {
			R20_Y_SERIES_COLUMN = r20_Y_SERIES_COLUMN;
		}

		public BigDecimal getR20_AMOUNT_X010() {
			return R20_AMOUNT_X010;
		}

		public void setR20_AMOUNT_X010(BigDecimal r20_AMOUNT_X010) {
			R20_AMOUNT_X010 = r20_AMOUNT_X010;
		}

		public String getR21_ENTITY() {
			return R21_ENTITY;
		}

		public void setR21_ENTITY(String r21_ENTITY) {
			R21_ENTITY = r21_ENTITY;
		}

		public String getR21_PARTICULARS() {
			return R21_PARTICULARS;
		}

		public void setR21_PARTICULARS(String r21_PARTICULARS) {
			R21_PARTICULARS = r21_PARTICULARS;
		}

		public String getR21_Y_SERIES_COLUMN() {
			return R21_Y_SERIES_COLUMN;
		}

		public void setR21_Y_SERIES_COLUMN(String r21_Y_SERIES_COLUMN) {
			R21_Y_SERIES_COLUMN = r21_Y_SERIES_COLUMN;
		}

		public BigDecimal getR21_AMOUNT_X010() {
			return R21_AMOUNT_X010;
		}

		public void setR21_AMOUNT_X010(BigDecimal r21_AMOUNT_X010) {
			R21_AMOUNT_X010 = r21_AMOUNT_X010;
		}

		public String getR22_ENTITY() {
			return R22_ENTITY;
		}

		public void setR22_ENTITY(String r22_ENTITY) {
			R22_ENTITY = r22_ENTITY;
		}

		public String getR22_PARTICULARS() {
			return R22_PARTICULARS;
		}

		public void setR22_PARTICULARS(String r22_PARTICULARS) {
			R22_PARTICULARS = r22_PARTICULARS;
		}

		public String getR22_Y_SERIES_COLUMN() {
			return R22_Y_SERIES_COLUMN;
		}

		public void setR22_Y_SERIES_COLUMN(String r22_Y_SERIES_COLUMN) {
			R22_Y_SERIES_COLUMN = r22_Y_SERIES_COLUMN;
		}

		public BigDecimal getR22_AMOUNT_X010() {
			return R22_AMOUNT_X010;
		}

		public void setR22_AMOUNT_X010(BigDecimal r22_AMOUNT_X010) {
			R22_AMOUNT_X010 = r22_AMOUNT_X010;
		}

		public String getR23_ENTITY() {
			return R23_ENTITY;
		}

		public void setR23_ENTITY(String r23_ENTITY) {
			R23_ENTITY = r23_ENTITY;
		}

		public String getR23_PARTICULARS() {
			return R23_PARTICULARS;
		}

		public void setR23_PARTICULARS(String r23_PARTICULARS) {
			R23_PARTICULARS = r23_PARTICULARS;
		}

		public String getR23_Y_SERIES_COLUMN() {
			return R23_Y_SERIES_COLUMN;
		}

		public void setR23_Y_SERIES_COLUMN(String r23_Y_SERIES_COLUMN) {
			R23_Y_SERIES_COLUMN = r23_Y_SERIES_COLUMN;
		}

		public BigDecimal getR23_AMOUNT_X010() {
			return R23_AMOUNT_X010;
		}

		public void setR23_AMOUNT_X010(BigDecimal r23_AMOUNT_X010) {
			R23_AMOUNT_X010 = r23_AMOUNT_X010;
		}

		public String getR24_ENTITY() {
			return R24_ENTITY;
		}

		public void setR24_ENTITY(String r24_ENTITY) {
			R24_ENTITY = r24_ENTITY;
		}

		public String getR24_PARTICULARS() {
			return R24_PARTICULARS;
		}

		public void setR24_PARTICULARS(String r24_PARTICULARS) {
			R24_PARTICULARS = r24_PARTICULARS;
		}

		public String getR24_Y_SERIES_COLUMN() {
			return R24_Y_SERIES_COLUMN;
		}

		public void setR24_Y_SERIES_COLUMN(String r24_Y_SERIES_COLUMN) {
			R24_Y_SERIES_COLUMN = r24_Y_SERIES_COLUMN;
		}

		public BigDecimal getR24_AMOUNT_X010() {
			return R24_AMOUNT_X010;
		}

		public void setR24_AMOUNT_X010(BigDecimal r24_AMOUNT_X010) {
			R24_AMOUNT_X010 = r24_AMOUNT_X010;
		}

		public String getR25_ENTITY() {
			return R25_ENTITY;
		}

		public void setR25_ENTITY(String r25_ENTITY) {
			R25_ENTITY = r25_ENTITY;
		}

		public String getR25_PARTICULARS() {
			return R25_PARTICULARS;
		}

		public void setR25_PARTICULARS(String r25_PARTICULARS) {
			R25_PARTICULARS = r25_PARTICULARS;
		}

		public String getR25_Y_SERIES_COLUMN() {
			return R25_Y_SERIES_COLUMN;
		}

		public void setR25_Y_SERIES_COLUMN(String r25_Y_SERIES_COLUMN) {
			R25_Y_SERIES_COLUMN = r25_Y_SERIES_COLUMN;
		}

		public BigDecimal getR25_AMOUNT_X010() {
			return R25_AMOUNT_X010;
		}

		public void setR25_AMOUNT_X010(BigDecimal r25_AMOUNT_X010) {
			R25_AMOUNT_X010 = r25_AMOUNT_X010;
		}

		public String getR26_ENTITY() {
			return R26_ENTITY;
		}

		public void setR26_ENTITY(String r26_ENTITY) {
			R26_ENTITY = r26_ENTITY;
		}

		public String getR26_PARTICULARS() {
			return R26_PARTICULARS;
		}

		public void setR26_PARTICULARS(String r26_PARTICULARS) {
			R26_PARTICULARS = r26_PARTICULARS;
		}

		public String getR26_Y_SERIES_COLUMN() {
			return R26_Y_SERIES_COLUMN;
		}

		public void setR26_Y_SERIES_COLUMN(String r26_Y_SERIES_COLUMN) {
			R26_Y_SERIES_COLUMN = r26_Y_SERIES_COLUMN;
		}

		public BigDecimal getR26_AMOUNT_X010() {
			return R26_AMOUNT_X010;
		}

		public void setR26_AMOUNT_X010(BigDecimal r26_AMOUNT_X010) {
			R26_AMOUNT_X010 = r26_AMOUNT_X010;
		}

		public String getR27_ENTITY() {
			return R27_ENTITY;
		}

		public void setR27_ENTITY(String r27_ENTITY) {
			R27_ENTITY = r27_ENTITY;
		}

		public String getR27_PARTICULARS() {
			return R27_PARTICULARS;
		}

		public void setR27_PARTICULARS(String r27_PARTICULARS) {
			R27_PARTICULARS = r27_PARTICULARS;
		}

		public String getR27_Y_SERIES_COLUMN() {
			return R27_Y_SERIES_COLUMN;
		}

		public void setR27_Y_SERIES_COLUMN(String r27_Y_SERIES_COLUMN) {
			R27_Y_SERIES_COLUMN = r27_Y_SERIES_COLUMN;
		}

		public BigDecimal getR27_AMOUNT_X010() {
			return R27_AMOUNT_X010;
		}

		public void setR27_AMOUNT_X010(BigDecimal r27_AMOUNT_X010) {
			R27_AMOUNT_X010 = r27_AMOUNT_X010;
		}

		public String getR28_ENTITY() {
			return R28_ENTITY;
		}

		public void setR28_ENTITY(String r28_ENTITY) {
			R28_ENTITY = r28_ENTITY;
		}

		public String getR28_PARTICULARS() {
			return R28_PARTICULARS;
		}

		public void setR28_PARTICULARS(String r28_PARTICULARS) {
			R28_PARTICULARS = r28_PARTICULARS;
		}

		public String getR28_Y_SERIES_COLUMN() {
			return R28_Y_SERIES_COLUMN;
		}

		public void setR28_Y_SERIES_COLUMN(String r28_Y_SERIES_COLUMN) {
			R28_Y_SERIES_COLUMN = r28_Y_SERIES_COLUMN;
		}

		public BigDecimal getR28_AMOUNT_X010() {
			return R28_AMOUNT_X010;
		}

		public void setR28_AMOUNT_X010(BigDecimal r28_AMOUNT_X010) {
			R28_AMOUNT_X010 = r28_AMOUNT_X010;
		}

		public String getR29_ENTITY() {
			return R29_ENTITY;
		}

		public void setR29_ENTITY(String r29_ENTITY) {
			R29_ENTITY = r29_ENTITY;
		}

		public String getR29_PARTICULARS() {
			return R29_PARTICULARS;
		}

		public void setR29_PARTICULARS(String r29_PARTICULARS) {
			R29_PARTICULARS = r29_PARTICULARS;
		}

		public String getR29_Y_SERIES_COLUMN() {
			return R29_Y_SERIES_COLUMN;
		}

		public void setR29_Y_SERIES_COLUMN(String r29_Y_SERIES_COLUMN) {
			R29_Y_SERIES_COLUMN = r29_Y_SERIES_COLUMN;
		}

		public BigDecimal getR29_AMOUNT_X010() {
			return R29_AMOUNT_X010;
		}

		public void setR29_AMOUNT_X010(BigDecimal r29_AMOUNT_X010) {
			R29_AMOUNT_X010 = r29_AMOUNT_X010;
		}

		public String getR30_ENTITY() {
			return R30_ENTITY;
		}

		public void setR30_ENTITY(String r30_ENTITY) {
			R30_ENTITY = r30_ENTITY;
		}

		public String getR30_PARTICULARS() {
			return R30_PARTICULARS;
		}

		public void setR30_PARTICULARS(String r30_PARTICULARS) {
			R30_PARTICULARS = r30_PARTICULARS;
		}

		public String getR30_Y_SERIES_COLUMN() {
			return R30_Y_SERIES_COLUMN;
		}

		public void setR30_Y_SERIES_COLUMN(String r30_Y_SERIES_COLUMN) {
			R30_Y_SERIES_COLUMN = r30_Y_SERIES_COLUMN;
		}

		public BigDecimal getR30_AMOUNT_X010() {
			return R30_AMOUNT_X010;
		}

		public void setR30_AMOUNT_X010(BigDecimal r30_AMOUNT_X010) {
			R30_AMOUNT_X010 = r30_AMOUNT_X010;
		}

		public String getR31_ENTITY() {
			return R31_ENTITY;
		}

		public void setR31_ENTITY(String r31_ENTITY) {
			R31_ENTITY = r31_ENTITY;
		}

		public String getR31_PARTICULARS() {
			return R31_PARTICULARS;
		}

		public void setR31_PARTICULARS(String r31_PARTICULARS) {
			R31_PARTICULARS = r31_PARTICULARS;
		}

		public String getR31_Y_SERIES_COLUMN() {
			return R31_Y_SERIES_COLUMN;
		}

		public void setR31_Y_SERIES_COLUMN(String r31_Y_SERIES_COLUMN) {
			R31_Y_SERIES_COLUMN = r31_Y_SERIES_COLUMN;
		}

		public BigDecimal getR31_AMOUNT_X010() {
			return R31_AMOUNT_X010;
		}

		public void setR31_AMOUNT_X010(BigDecimal r31_AMOUNT_X010) {
			R31_AMOUNT_X010 = r31_AMOUNT_X010;
		}

		public String getR32_ENTITY() {
			return R32_ENTITY;
		}

		public void setR32_ENTITY(String r32_ENTITY) {
			R32_ENTITY = r32_ENTITY;
		}

		public String getR32_PARTICULARS() {
			return R32_PARTICULARS;
		}

		public void setR32_PARTICULARS(String r32_PARTICULARS) {
			R32_PARTICULARS = r32_PARTICULARS;
		}

		public String getR32_Y_SERIES_COLUMN() {
			return R32_Y_SERIES_COLUMN;
		}

		public void setR32_Y_SERIES_COLUMN(String r32_Y_SERIES_COLUMN) {
			R32_Y_SERIES_COLUMN = r32_Y_SERIES_COLUMN;
		}

		public BigDecimal getR32_AMOUNT_X010() {
			return R32_AMOUNT_X010;
		}

		public void setR32_AMOUNT_X010(BigDecimal r32_AMOUNT_X010) {
			R32_AMOUNT_X010 = r32_AMOUNT_X010;
		}

		public String getR33_ENTITY() {
			return R33_ENTITY;
		}

		public void setR33_ENTITY(String r33_ENTITY) {
			R33_ENTITY = r33_ENTITY;
		}

		public String getR33_PARTICULARS() {
			return R33_PARTICULARS;
		}

		public void setR33_PARTICULARS(String r33_PARTICULARS) {
			R33_PARTICULARS = r33_PARTICULARS;
		}

		public String getR33_Y_SERIES_COLUMN() {
			return R33_Y_SERIES_COLUMN;
		}

		public void setR33_Y_SERIES_COLUMN(String r33_Y_SERIES_COLUMN) {
			R33_Y_SERIES_COLUMN = r33_Y_SERIES_COLUMN;
		}

		public BigDecimal getR33_AMOUNT_X010() {
			return R33_AMOUNT_X010;
		}

		public void setR33_AMOUNT_X010(BigDecimal r33_AMOUNT_X010) {
			R33_AMOUNT_X010 = r33_AMOUNT_X010;
		}

		public String getR34_ENTITY() {
			return R34_ENTITY;
		}

		public void setR34_ENTITY(String r34_ENTITY) {
			R34_ENTITY = r34_ENTITY;
		}

		public String getR34_PARTICULARS() {
			return R34_PARTICULARS;
		}

		public void setR34_PARTICULARS(String r34_PARTICULARS) {
			R34_PARTICULARS = r34_PARTICULARS;
		}

		public String getR34_Y_SERIES_COLUMN() {
			return R34_Y_SERIES_COLUMN;
		}

		public void setR34_Y_SERIES_COLUMN(String r34_Y_SERIES_COLUMN) {
			R34_Y_SERIES_COLUMN = r34_Y_SERIES_COLUMN;
		}

		public BigDecimal getR34_AMOUNT_X010() {
			return R34_AMOUNT_X010;
		}

		public void setR34_AMOUNT_X010(BigDecimal r34_AMOUNT_X010) {
			R34_AMOUNT_X010 = r34_AMOUNT_X010;
		}

		public String getR35_ENTITY() {
			return R35_ENTITY;
		}

		public void setR35_ENTITY(String r35_ENTITY) {
			R35_ENTITY = r35_ENTITY;
		}

		public String getR35_PARTICULARS() {
			return R35_PARTICULARS;
		}

		public void setR35_PARTICULARS(String r35_PARTICULARS) {
			R35_PARTICULARS = r35_PARTICULARS;
		}

		public String getR35_Y_SERIES_COLUMN() {
			return R35_Y_SERIES_COLUMN;
		}

		public void setR35_Y_SERIES_COLUMN(String r35_Y_SERIES_COLUMN) {
			R35_Y_SERIES_COLUMN = r35_Y_SERIES_COLUMN;
		}

		public BigDecimal getR35_AMOUNT_X010() {
			return R35_AMOUNT_X010;
		}

		public void setR35_AMOUNT_X010(BigDecimal r35_AMOUNT_X010) {
			R35_AMOUNT_X010 = r35_AMOUNT_X010;
		}

		public String getR36_ENTITY() {
			return R36_ENTITY;
		}

		public void setR36_ENTITY(String r36_ENTITY) {
			R36_ENTITY = r36_ENTITY;
		}

		public String getR36_PARTICULARS() {
			return R36_PARTICULARS;
		}

		public void setR36_PARTICULARS(String r36_PARTICULARS) {
			R36_PARTICULARS = r36_PARTICULARS;
		}

		public String getR36_Y_SERIES_COLUMN() {
			return R36_Y_SERIES_COLUMN;
		}

		public void setR36_Y_SERIES_COLUMN(String r36_Y_SERIES_COLUMN) {
			R36_Y_SERIES_COLUMN = r36_Y_SERIES_COLUMN;
		}

		public BigDecimal getR36_AMOUNT_X010() {
			return R36_AMOUNT_X010;
		}

		public void setR36_AMOUNT_X010(BigDecimal r36_AMOUNT_X010) {
			R36_AMOUNT_X010 = r36_AMOUNT_X010;
		}

		public String getR37_ENTITY() {
			return R37_ENTITY;
		}

		public void setR37_ENTITY(String r37_ENTITY) {
			R37_ENTITY = r37_ENTITY;
		}

		public String getR37_PARTICULARS() {
			return R37_PARTICULARS;
		}

		public void setR37_PARTICULARS(String r37_PARTICULARS) {
			R37_PARTICULARS = r37_PARTICULARS;
		}

		public String getR37_Y_SERIES_COLUMN() {
			return R37_Y_SERIES_COLUMN;
		}

		public void setR37_Y_SERIES_COLUMN(String r37_Y_SERIES_COLUMN) {
			R37_Y_SERIES_COLUMN = r37_Y_SERIES_COLUMN;
		}

		public BigDecimal getR37_AMOUNT_X010() {
			return R37_AMOUNT_X010;
		}

		public void setR37_AMOUNT_X010(BigDecimal r37_AMOUNT_X010) {
			R37_AMOUNT_X010 = r37_AMOUNT_X010;
		}

		public String getR38_ENTITY() {
			return R38_ENTITY;
		}

		public void setR38_ENTITY(String r38_ENTITY) {
			R38_ENTITY = r38_ENTITY;
		}

		public String getR38_PARTICULARS() {
			return R38_PARTICULARS;
		}

		public void setR38_PARTICULARS(String r38_PARTICULARS) {
			R38_PARTICULARS = r38_PARTICULARS;
		}

		public String getR38_Y_SERIES_COLUMN() {
			return R38_Y_SERIES_COLUMN;
		}

		public void setR38_Y_SERIES_COLUMN(String r38_Y_SERIES_COLUMN) {
			R38_Y_SERIES_COLUMN = r38_Y_SERIES_COLUMN;
		}

		public BigDecimal getR38_AMOUNT_X010() {
			return R38_AMOUNT_X010;
		}

		public void setR38_AMOUNT_X010(BigDecimal r38_AMOUNT_X010) {
			R38_AMOUNT_X010 = r38_AMOUNT_X010;
		}

		public String getR39_ENTITY() {
			return R39_ENTITY;
		}

		public void setR39_ENTITY(String r39_ENTITY) {
			R39_ENTITY = r39_ENTITY;
		}

		public String getR39_PARTICULARS() {
			return R39_PARTICULARS;
		}

		public void setR39_PARTICULARS(String r39_PARTICULARS) {
			R39_PARTICULARS = r39_PARTICULARS;
		}

		public String getR39_Y_SERIES_COLUMN() {
			return R39_Y_SERIES_COLUMN;
		}

		public void setR39_Y_SERIES_COLUMN(String r39_Y_SERIES_COLUMN) {
			R39_Y_SERIES_COLUMN = r39_Y_SERIES_COLUMN;
		}

		public BigDecimal getR39_AMOUNT_X010() {
			return R39_AMOUNT_X010;
		}

		public void setR39_AMOUNT_X010(BigDecimal r39_AMOUNT_X010) {
			R39_AMOUNT_X010 = r39_AMOUNT_X010;
		}

		public String getR40_ENTITY() {
			return R40_ENTITY;
		}

		public void setR40_ENTITY(String r40_ENTITY) {
			R40_ENTITY = r40_ENTITY;
		}

		public String getR40_PARTICULARS() {
			return R40_PARTICULARS;
		}

		public void setR40_PARTICULARS(String r40_PARTICULARS) {
			R40_PARTICULARS = r40_PARTICULARS;
		}

		public String getR40_Y_SERIES_COLUMN() {
			return R40_Y_SERIES_COLUMN;
		}

		public void setR40_Y_SERIES_COLUMN(String r40_Y_SERIES_COLUMN) {
			R40_Y_SERIES_COLUMN = r40_Y_SERIES_COLUMN;
		}

		public BigDecimal getR40_AMOUNT_X010() {
			return R40_AMOUNT_X010;
		}

		public void setR40_AMOUNT_X010(BigDecimal r40_AMOUNT_X010) {
			R40_AMOUNT_X010 = r40_AMOUNT_X010;
		}

		public String getR41_ENTITY() {
			return R41_ENTITY;
		}

		public void setR41_ENTITY(String r41_ENTITY) {
			R41_ENTITY = r41_ENTITY;
		}

		public String getR41_PARTICULARS() {
			return R41_PARTICULARS;
		}

		public void setR41_PARTICULARS(String r41_PARTICULARS) {
			R41_PARTICULARS = r41_PARTICULARS;
		}

		public String getR41_Y_SERIES_COLUMN() {
			return R41_Y_SERIES_COLUMN;
		}

		public void setR41_Y_SERIES_COLUMN(String r41_Y_SERIES_COLUMN) {
			R41_Y_SERIES_COLUMN = r41_Y_SERIES_COLUMN;
		}

		public BigDecimal getR41_AMOUNT_X010() {
			return R41_AMOUNT_X010;
		}

		public void setR41_AMOUNT_X010(BigDecimal r41_AMOUNT_X010) {
			R41_AMOUNT_X010 = r41_AMOUNT_X010;
		}

		public String getR42_ENTITY() {
			return R42_ENTITY;
		}

		public void setR42_ENTITY(String r42_ENTITY) {
			R42_ENTITY = r42_ENTITY;
		}

		public String getR42_PARTICULARS() {
			return R42_PARTICULARS;
		}

		public void setR42_PARTICULARS(String r42_PARTICULARS) {
			R42_PARTICULARS = r42_PARTICULARS;
		}

		public String getR42_Y_SERIES_COLUMN() {
			return R42_Y_SERIES_COLUMN;
		}

		public void setR42_Y_SERIES_COLUMN(String r42_Y_SERIES_COLUMN) {
			R42_Y_SERIES_COLUMN = r42_Y_SERIES_COLUMN;
		}

		public BigDecimal getR42_AMOUNT_X010() {
			return R42_AMOUNT_X010;
		}

		public void setR42_AMOUNT_X010(BigDecimal r42_AMOUNT_X010) {
			R42_AMOUNT_X010 = r42_AMOUNT_X010;
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

//MANUAL 
	class DBS10_FINCON_II_1AManualRowMapper implements RowMapper<DBS10_FINCON_II_1A_Manual_Summary_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Manual_Summary_Entity obj = new DBS10_FINCON_II_1A_Manual_Summary_Entity();

			// R8
			obj.setR8_AMOUNT_X010(rs.getBigDecimal("R8_AMOUNT_X010"));

// R12
			obj.setR12_AMOUNT_X010(rs.getBigDecimal("R12_AMOUNT_X010"));

// R14
			obj.setR14_AMOUNT_X010(rs.getBigDecimal("R14_AMOUNT_X010"));

// R22
			obj.setR22_AMOUNT_X010(rs.getBigDecimal("R22_AMOUNT_X010"));

// R23
			obj.setR23_AMOUNT_X010(rs.getBigDecimal("R23_AMOUNT_X010"));

// R25
			obj.setR25_AMOUNT_X010(rs.getBigDecimal("R25_AMOUNT_X010"));

// R26
			obj.setR26_AMOUNT_X010(rs.getBigDecimal("R26_AMOUNT_X010"));

// R27
			obj.setR27_AMOUNT_X010(rs.getBigDecimal("R27_AMOUNT_X010"));

// R28
			obj.setR28_AMOUNT_X010(rs.getBigDecimal("R28_AMOUNT_X010"));

// R31
			obj.setR31_AMOUNT_X010(rs.getBigDecimal("R31_AMOUNT_X010"));

// R32
			obj.setR32_AMOUNT_X010(rs.getBigDecimal("R32_AMOUNT_X010"));

// R35
			obj.setR35_AMOUNT_X010(rs.getBigDecimal("R35_AMOUNT_X010"));

// R36
			obj.setR36_AMOUNT_X010(rs.getBigDecimal("R36_AMOUNT_X010"));

// R37
			obj.setR37_AMOUNT_X010(rs.getBigDecimal("R37_AMOUNT_X010"));

// R41
			obj.setR41_AMOUNT_X010(rs.getBigDecimal("R41_AMOUNT_X010"));

// R42
			obj.setR42_AMOUNT_X010(rs.getBigDecimal("R42_AMOUNT_X010"));

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

	public static class DBS10_FINCON_II_1A_Manual_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private BigDecimal R8_AMOUNT_X010;
		private BigDecimal R12_AMOUNT_X010;
		private BigDecimal R14_AMOUNT_X010;
		private BigDecimal R22_AMOUNT_X010;
		private BigDecimal R23_AMOUNT_X010;
		private BigDecimal R25_AMOUNT_X010;
		private BigDecimal R26_AMOUNT_X010;
		private BigDecimal R27_AMOUNT_X010;
		private BigDecimal R28_AMOUNT_X010;
		private BigDecimal R31_AMOUNT_X010;
		private BigDecimal R32_AMOUNT_X010;
		private BigDecimal R35_AMOUNT_X010;
		private BigDecimal R36_AMOUNT_X010;
		private BigDecimal R37_AMOUNT_X010;
		private BigDecimal R41_AMOUNT_X010;
		private BigDecimal R42_AMOUNT_X010;

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

		public BigDecimal getR8_AMOUNT_X010() {
			return R8_AMOUNT_X010;
		}

		public void setR8_AMOUNT_X010(BigDecimal r8_AMOUNT_X010) {
			R8_AMOUNT_X010 = r8_AMOUNT_X010;
		}

		public BigDecimal getR12_AMOUNT_X010() {
			return R12_AMOUNT_X010;
		}

		public void setR12_AMOUNT_X010(BigDecimal r12_AMOUNT_X010) {
			R12_AMOUNT_X010 = r12_AMOUNT_X010;
		}

		public BigDecimal getR14_AMOUNT_X010() {
			return R14_AMOUNT_X010;
		}

		public void setR14_AMOUNT_X010(BigDecimal r14_AMOUNT_X010) {
			R14_AMOUNT_X010 = r14_AMOUNT_X010;
		}

		public BigDecimal getR22_AMOUNT_X010() {
			return R22_AMOUNT_X010;
		}

		public void setR22_AMOUNT_X010(BigDecimal r22_AMOUNT_X010) {
			R22_AMOUNT_X010 = r22_AMOUNT_X010;
		}

		public BigDecimal getR23_AMOUNT_X010() {
			return R23_AMOUNT_X010;
		}

		public void setR23_AMOUNT_X010(BigDecimal r23_AMOUNT_X010) {
			R23_AMOUNT_X010 = r23_AMOUNT_X010;
		}

		public BigDecimal getR25_AMOUNT_X010() {
			return R25_AMOUNT_X010;
		}

		public void setR25_AMOUNT_X010(BigDecimal r25_AMOUNT_X010) {
			R25_AMOUNT_X010 = r25_AMOUNT_X010;
		}

		public BigDecimal getR26_AMOUNT_X010() {
			return R26_AMOUNT_X010;
		}

		public void setR26_AMOUNT_X010(BigDecimal r26_AMOUNT_X010) {
			R26_AMOUNT_X010 = r26_AMOUNT_X010;
		}

		public BigDecimal getR27_AMOUNT_X010() {
			return R27_AMOUNT_X010;
		}

		public void setR27_AMOUNT_X010(BigDecimal r27_AMOUNT_X010) {
			R27_AMOUNT_X010 = r27_AMOUNT_X010;
		}

		public BigDecimal getR28_AMOUNT_X010() {
			return R28_AMOUNT_X010;
		}

		public void setR28_AMOUNT_X010(BigDecimal r28_AMOUNT_X010) {
			R28_AMOUNT_X010 = r28_AMOUNT_X010;
		}

		public BigDecimal getR31_AMOUNT_X010() {
			return R31_AMOUNT_X010;
		}

		public void setR31_AMOUNT_X010(BigDecimal r31_AMOUNT_X010) {
			R31_AMOUNT_X010 = r31_AMOUNT_X010;
		}

		public BigDecimal getR32_AMOUNT_X010() {
			return R32_AMOUNT_X010;
		}

		public void setR32_AMOUNT_X010(BigDecimal r32_AMOUNT_X010) {
			R32_AMOUNT_X010 = r32_AMOUNT_X010;
		}

		public BigDecimal getR35_AMOUNT_X010() {
			return R35_AMOUNT_X010;
		}

		public void setR35_AMOUNT_X010(BigDecimal r35_AMOUNT_X010) {
			R35_AMOUNT_X010 = r35_AMOUNT_X010;
		}

		public BigDecimal getR36_AMOUNT_X010() {
			return R36_AMOUNT_X010;
		}

		public void setR36_AMOUNT_X010(BigDecimal r36_AMOUNT_X010) {
			R36_AMOUNT_X010 = r36_AMOUNT_X010;
		}

		public BigDecimal getR37_AMOUNT_X010() {
			return R37_AMOUNT_X010;
		}

		public void setR37_AMOUNT_X010(BigDecimal r37_AMOUNT_X010) {
			R37_AMOUNT_X010 = r37_AMOUNT_X010;
		}

		public BigDecimal getR41_AMOUNT_X010() {
			return R41_AMOUNT_X010;
		}

		public void setR41_AMOUNT_X010(BigDecimal r41_AMOUNT_X010) {
			R41_AMOUNT_X010 = r41_AMOUNT_X010;
		}

		public BigDecimal getR42_AMOUNT_X010() {
			return R42_AMOUNT_X010;
		}

		public void setR42_AMOUNT_X010(BigDecimal r42_AMOUNT_X010) {
			R42_AMOUNT_X010 = r42_AMOUNT_X010;
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

	class DBS10_FINCON_II_1ARowArchivalMapper implements RowMapper<DBS10_FINCON_II_1A_Archival_Summary_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Archival_Summary_Entity obj = new DBS10_FINCON_II_1A_Archival_Summary_Entity();
// R5
			obj.setR5_ENTITY(rs.getString("R5_ENTITY"));
			obj.setR5_PARTICULARS(rs.getString("R5_PARTICULARS"));
			obj.setR5_Y_SERIES_COLUMN(rs.getString("R5_Y_SERIES_COLUMN"));
			obj.setR5_AMOUNT_X010(rs.getBigDecimal("R5_AMOUNT_X010"));

// R6
			obj.setR6_ENTITY(rs.getString("R6_ENTITY"));
			obj.setR6_PARTICULARS(rs.getString("R6_PARTICULARS"));
			obj.setR6_Y_SERIES_COLUMN(rs.getString("R6_Y_SERIES_COLUMN"));
			obj.setR6_AMOUNT_X010(rs.getBigDecimal("R6_AMOUNT_X010"));

// R7
			obj.setR7_ENTITY(rs.getString("R7_ENTITY"));
			obj.setR7_PARTICULARS(rs.getString("R7_PARTICULARS"));
			obj.setR7_Y_SERIES_COLUMN(rs.getString("R7_Y_SERIES_COLUMN"));
			obj.setR7_AMOUNT_X010(rs.getBigDecimal("R7_AMOUNT_X010"));

// R8
			obj.setR8_ENTITY(rs.getString("R8_ENTITY"));
			obj.setR8_PARTICULARS(rs.getString("R8_PARTICULARS"));
			obj.setR8_Y_SERIES_COLUMN(rs.getString("R8_Y_SERIES_COLUMN"));
			obj.setR8_AMOUNT_X010(rs.getBigDecimal("R8_AMOUNT_X010"));

// R9
			obj.setR9_ENTITY(rs.getString("R9_ENTITY"));
			obj.setR9_PARTICULARS(rs.getString("R9_PARTICULARS"));
			obj.setR9_Y_SERIES_COLUMN(rs.getString("R9_Y_SERIES_COLUMN"));
			obj.setR9_AMOUNT_X010(rs.getBigDecimal("R9_AMOUNT_X010"));

// R10
			obj.setR10_ENTITY(rs.getString("R10_ENTITY"));
			obj.setR10_PARTICULARS(rs.getString("R10_PARTICULARS"));
			obj.setR10_Y_SERIES_COLUMN(rs.getString("R10_Y_SERIES_COLUMN"));
			obj.setR10_AMOUNT_X010(rs.getBigDecimal("R10_AMOUNT_X010"));

// R11
			obj.setR11_ENTITY(rs.getString("R11_ENTITY"));
			obj.setR11_PARTICULARS(rs.getString("R11_PARTICULARS"));
			obj.setR11_Y_SERIES_COLUMN(rs.getString("R11_Y_SERIES_COLUMN"));
			obj.setR11_AMOUNT_X010(rs.getBigDecimal("R11_AMOUNT_X010"));

// R12
			obj.setR12_ENTITY(rs.getString("R12_ENTITY"));
			obj.setR12_PARTICULARS(rs.getString("R12_PARTICULARS"));
			obj.setR12_Y_SERIES_COLUMN(rs.getString("R12_Y_SERIES_COLUMN"));
			obj.setR12_AMOUNT_X010(rs.getBigDecimal("R12_AMOUNT_X010"));

// R13
			obj.setR13_ENTITY(rs.getString("R13_ENTITY"));
			obj.setR13_PARTICULARS(rs.getString("R13_PARTICULARS"));
			obj.setR13_Y_SERIES_COLUMN(rs.getString("R13_Y_SERIES_COLUMN"));
			obj.setR13_AMOUNT_X010(rs.getBigDecimal("R13_AMOUNT_X010"));

// R14
			obj.setR14_ENTITY(rs.getString("R14_ENTITY"));
			obj.setR14_PARTICULARS(rs.getString("R14_PARTICULARS"));
			obj.setR14_Y_SERIES_COLUMN(rs.getString("R14_Y_SERIES_COLUMN"));
			obj.setR14_AMOUNT_X010(rs.getBigDecimal("R14_AMOUNT_X010"));

// R15
			obj.setR15_ENTITY(rs.getString("R15_ENTITY"));
			obj.setR15_PARTICULARS(rs.getString("R15_PARTICULARS"));
			obj.setR15_Y_SERIES_COLUMN(rs.getString("R15_Y_SERIES_COLUMN"));
			obj.setR15_AMOUNT_X010(rs.getBigDecimal("R15_AMOUNT_X010"));

// R16
			obj.setR16_ENTITY(rs.getString("R16_ENTITY"));
			obj.setR16_PARTICULARS(rs.getString("R16_PARTICULARS"));
			obj.setR16_Y_SERIES_COLUMN(rs.getString("R16_Y_SERIES_COLUMN"));
			obj.setR16_AMOUNT_X010(rs.getBigDecimal("R16_AMOUNT_X010"));

// R17
			obj.setR17_ENTITY(rs.getString("R17_ENTITY"));
			obj.setR17_PARTICULARS(rs.getString("R17_PARTICULARS"));
			obj.setR17_Y_SERIES_COLUMN(rs.getString("R17_Y_SERIES_COLUMN"));
			obj.setR17_AMOUNT_X010(rs.getBigDecimal("R17_AMOUNT_X010"));

// R18
			obj.setR18_ENTITY(rs.getString("R18_ENTITY"));
			obj.setR18_PARTICULARS(rs.getString("R18_PARTICULARS"));
			obj.setR18_Y_SERIES_COLUMN(rs.getString("R18_Y_SERIES_COLUMN"));
			obj.setR18_AMOUNT_X010(rs.getBigDecimal("R18_AMOUNT_X010"));

// R19
			obj.setR19_ENTITY(rs.getString("R19_ENTITY"));
			obj.setR19_PARTICULARS(rs.getString("R19_PARTICULARS"));
			obj.setR19_Y_SERIES_COLUMN(rs.getString("R19_Y_SERIES_COLUMN"));
			obj.setR19_AMOUNT_X010(rs.getBigDecimal("R19_AMOUNT_X010"));

// R20
			obj.setR20_ENTITY(rs.getString("R20_ENTITY"));
			obj.setR20_PARTICULARS(rs.getString("R20_PARTICULARS"));
			obj.setR20_Y_SERIES_COLUMN(rs.getString("R20_Y_SERIES_COLUMN"));
			obj.setR20_AMOUNT_X010(rs.getBigDecimal("R20_AMOUNT_X010"));

// R21
			obj.setR21_ENTITY(rs.getString("R21_ENTITY"));
			obj.setR21_PARTICULARS(rs.getString("R21_PARTICULARS"));
			obj.setR21_Y_SERIES_COLUMN(rs.getString("R21_Y_SERIES_COLUMN"));
			obj.setR21_AMOUNT_X010(rs.getBigDecimal("R21_AMOUNT_X010"));

// R22
			obj.setR22_ENTITY(rs.getString("R22_ENTITY"));
			obj.setR22_PARTICULARS(rs.getString("R22_PARTICULARS"));
			obj.setR22_Y_SERIES_COLUMN(rs.getString("R22_Y_SERIES_COLUMN"));
			obj.setR22_AMOUNT_X010(rs.getBigDecimal("R22_AMOUNT_X010"));

// R23
			obj.setR23_ENTITY(rs.getString("R23_ENTITY"));
			obj.setR23_PARTICULARS(rs.getString("R23_PARTICULARS"));
			obj.setR23_Y_SERIES_COLUMN(rs.getString("R23_Y_SERIES_COLUMN"));
			obj.setR23_AMOUNT_X010(rs.getBigDecimal("R23_AMOUNT_X010"));

// R24
			obj.setR24_ENTITY(rs.getString("R24_ENTITY"));
			obj.setR24_PARTICULARS(rs.getString("R24_PARTICULARS"));
			obj.setR24_Y_SERIES_COLUMN(rs.getString("R24_Y_SERIES_COLUMN"));
			obj.setR24_AMOUNT_X010(rs.getBigDecimal("R24_AMOUNT_X010"));

// R25
			obj.setR25_ENTITY(rs.getString("R25_ENTITY"));
			obj.setR25_PARTICULARS(rs.getString("R25_PARTICULARS"));
			obj.setR25_Y_SERIES_COLUMN(rs.getString("R25_Y_SERIES_COLUMN"));
			obj.setR25_AMOUNT_X010(rs.getBigDecimal("R25_AMOUNT_X010"));

// R26
			obj.setR26_ENTITY(rs.getString("R26_ENTITY"));
			obj.setR26_PARTICULARS(rs.getString("R26_PARTICULARS"));
			obj.setR26_Y_SERIES_COLUMN(rs.getString("R26_Y_SERIES_COLUMN"));
			obj.setR26_AMOUNT_X010(rs.getBigDecimal("R26_AMOUNT_X010"));

// R27
			obj.setR27_ENTITY(rs.getString("R27_ENTITY"));
			obj.setR27_PARTICULARS(rs.getString("R27_PARTICULARS"));
			obj.setR27_Y_SERIES_COLUMN(rs.getString("R27_Y_SERIES_COLUMN"));
			obj.setR27_AMOUNT_X010(rs.getBigDecimal("R27_AMOUNT_X010"));

// R28
			obj.setR28_ENTITY(rs.getString("R28_ENTITY"));
			obj.setR28_PARTICULARS(rs.getString("R28_PARTICULARS"));
			obj.setR28_Y_SERIES_COLUMN(rs.getString("R28_Y_SERIES_COLUMN"));
			obj.setR28_AMOUNT_X010(rs.getBigDecimal("R28_AMOUNT_X010"));

// R29
			obj.setR29_ENTITY(rs.getString("R29_ENTITY"));
			obj.setR29_PARTICULARS(rs.getString("R29_PARTICULARS"));
			obj.setR29_Y_SERIES_COLUMN(rs.getString("R29_Y_SERIES_COLUMN"));
			obj.setR29_AMOUNT_X010(rs.getBigDecimal("R29_AMOUNT_X010"));

// R30
			obj.setR30_ENTITY(rs.getString("R30_ENTITY"));
			obj.setR30_PARTICULARS(rs.getString("R30_PARTICULARS"));
			obj.setR30_Y_SERIES_COLUMN(rs.getString("R30_Y_SERIES_COLUMN"));
			obj.setR30_AMOUNT_X010(rs.getBigDecimal("R30_AMOUNT_X010"));

// R31
			obj.setR31_ENTITY(rs.getString("R31_ENTITY"));
			obj.setR31_PARTICULARS(rs.getString("R31_PARTICULARS"));
			obj.setR31_Y_SERIES_COLUMN(rs.getString("R31_Y_SERIES_COLUMN"));
			obj.setR31_AMOUNT_X010(rs.getBigDecimal("R31_AMOUNT_X010"));

// R32
			obj.setR32_ENTITY(rs.getString("R32_ENTITY"));
			obj.setR32_PARTICULARS(rs.getString("R32_PARTICULARS"));
			obj.setR32_Y_SERIES_COLUMN(rs.getString("R32_Y_SERIES_COLUMN"));
			obj.setR32_AMOUNT_X010(rs.getBigDecimal("R32_AMOUNT_X010"));

// R33
			obj.setR33_ENTITY(rs.getString("R33_ENTITY"));
			obj.setR33_PARTICULARS(rs.getString("R33_PARTICULARS"));
			obj.setR33_Y_SERIES_COLUMN(rs.getString("R33_Y_SERIES_COLUMN"));
			obj.setR33_AMOUNT_X010(rs.getBigDecimal("R33_AMOUNT_X010"));

// R34
			obj.setR34_ENTITY(rs.getString("R34_ENTITY"));
			obj.setR34_PARTICULARS(rs.getString("R34_PARTICULARS"));
			obj.setR34_Y_SERIES_COLUMN(rs.getString("R34_Y_SERIES_COLUMN"));
			obj.setR34_AMOUNT_X010(rs.getBigDecimal("R34_AMOUNT_X010"));

// R35
			obj.setR35_ENTITY(rs.getString("R35_ENTITY"));
			obj.setR35_PARTICULARS(rs.getString("R35_PARTICULARS"));
			obj.setR35_Y_SERIES_COLUMN(rs.getString("R35_Y_SERIES_COLUMN"));
			obj.setR35_AMOUNT_X010(rs.getBigDecimal("R35_AMOUNT_X010"));

// R36
			obj.setR36_ENTITY(rs.getString("R36_ENTITY"));
			obj.setR36_PARTICULARS(rs.getString("R36_PARTICULARS"));
			obj.setR36_Y_SERIES_COLUMN(rs.getString("R36_Y_SERIES_COLUMN"));
			obj.setR36_AMOUNT_X010(rs.getBigDecimal("R36_AMOUNT_X010"));

// R37
			obj.setR37_ENTITY(rs.getString("R37_ENTITY"));
			obj.setR37_PARTICULARS(rs.getString("R37_PARTICULARS"));
			obj.setR37_Y_SERIES_COLUMN(rs.getString("R37_Y_SERIES_COLUMN"));
			obj.setR37_AMOUNT_X010(rs.getBigDecimal("R37_AMOUNT_X010"));

// R38
			obj.setR38_ENTITY(rs.getString("R38_ENTITY"));
			obj.setR38_PARTICULARS(rs.getString("R38_PARTICULARS"));
			obj.setR38_Y_SERIES_COLUMN(rs.getString("R38_Y_SERIES_COLUMN"));
			obj.setR38_AMOUNT_X010(rs.getBigDecimal("R38_AMOUNT_X010"));

// R39
			obj.setR39_ENTITY(rs.getString("R39_ENTITY"));
			obj.setR39_PARTICULARS(rs.getString("R39_PARTICULARS"));
			obj.setR39_Y_SERIES_COLUMN(rs.getString("R39_Y_SERIES_COLUMN"));
			obj.setR39_AMOUNT_X010(rs.getBigDecimal("R39_AMOUNT_X010"));

// R40
			obj.setR40_ENTITY(rs.getString("R40_ENTITY"));
			obj.setR40_PARTICULARS(rs.getString("R40_PARTICULARS"));
			obj.setR40_Y_SERIES_COLUMN(rs.getString("R40_Y_SERIES_COLUMN"));
			obj.setR40_AMOUNT_X010(rs.getBigDecimal("R40_AMOUNT_X010"));

// R41
			obj.setR41_ENTITY(rs.getString("R41_ENTITY"));
			obj.setR41_PARTICULARS(rs.getString("R41_PARTICULARS"));
			obj.setR41_Y_SERIES_COLUMN(rs.getString("R41_Y_SERIES_COLUMN"));
			obj.setR41_AMOUNT_X010(rs.getBigDecimal("R41_AMOUNT_X010"));

// R42
			obj.setR42_ENTITY(rs.getString("R42_ENTITY"));
			obj.setR42_PARTICULARS(rs.getString("R42_PARTICULARS"));
			obj.setR42_Y_SERIES_COLUMN(rs.getString("R42_Y_SERIES_COLUMN"));
			obj.setR42_AMOUNT_X010(rs.getBigDecimal("R42_AMOUNT_X010"));

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

	@IdClass(DBS10_FINCON_II_1A_PK.class)
	public class DBS10_FINCON_II_1A_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private String R5_ENTITY;
		private String R5_PARTICULARS;
		private String R5_Y_SERIES_COLUMN;
		private BigDecimal R5_AMOUNT_X010;

		private String R6_ENTITY;
		private String R6_PARTICULARS;
		private String R6_Y_SERIES_COLUMN;
		private BigDecimal R6_AMOUNT_X010;

		private String R7_ENTITY;
		private String R7_PARTICULARS;
		private String R7_Y_SERIES_COLUMN;
		private BigDecimal R7_AMOUNT_X010;

		private String R8_ENTITY;
		private String R8_PARTICULARS;
		private String R8_Y_SERIES_COLUMN;
		private BigDecimal R8_AMOUNT_X010;

		private String R9_ENTITY;
		private String R9_PARTICULARS;
		private String R9_Y_SERIES_COLUMN;
		private BigDecimal R9_AMOUNT_X010;

		private String R10_ENTITY;
		private String R10_PARTICULARS;
		private String R10_Y_SERIES_COLUMN;
		private BigDecimal R10_AMOUNT_X010;

		private String R11_ENTITY;
		private String R11_PARTICULARS;
		private String R11_Y_SERIES_COLUMN;
		private BigDecimal R11_AMOUNT_X010;

		private String R12_ENTITY;
		private String R12_PARTICULARS;
		private String R12_Y_SERIES_COLUMN;
		private BigDecimal R12_AMOUNT_X010;

		private String R13_ENTITY;
		private String R13_PARTICULARS;
		private String R13_Y_SERIES_COLUMN;
		private BigDecimal R13_AMOUNT_X010;

		private String R14_ENTITY;
		private String R14_PARTICULARS;
		private String R14_Y_SERIES_COLUMN;
		private BigDecimal R14_AMOUNT_X010;

		private String R15_ENTITY;
		private String R15_PARTICULARS;
		private String R15_Y_SERIES_COLUMN;
		private BigDecimal R15_AMOUNT_X010;

		private String R16_ENTITY;
		private String R16_PARTICULARS;
		private String R16_Y_SERIES_COLUMN;
		private BigDecimal R16_AMOUNT_X010;

		private String R17_ENTITY;
		private String R17_PARTICULARS;
		private String R17_Y_SERIES_COLUMN;
		private BigDecimal R17_AMOUNT_X010;

		private String R18_ENTITY;
		private String R18_PARTICULARS;
		private String R18_Y_SERIES_COLUMN;
		private BigDecimal R18_AMOUNT_X010;

		private String R19_ENTITY;
		private String R19_PARTICULARS;
		private String R19_Y_SERIES_COLUMN;
		private BigDecimal R19_AMOUNT_X010;

		private String R20_ENTITY;
		private String R20_PARTICULARS;
		private String R20_Y_SERIES_COLUMN;
		private BigDecimal R20_AMOUNT_X010;

		private String R21_ENTITY;
		private String R21_PARTICULARS;
		private String R21_Y_SERIES_COLUMN;
		private BigDecimal R21_AMOUNT_X010;

		private String R22_ENTITY;
		private String R22_PARTICULARS;
		private String R22_Y_SERIES_COLUMN;
		private BigDecimal R22_AMOUNT_X010;

		private String R23_ENTITY;
		private String R23_PARTICULARS;
		private String R23_Y_SERIES_COLUMN;
		private BigDecimal R23_AMOUNT_X010;

		private String R24_ENTITY;
		private String R24_PARTICULARS;
		private String R24_Y_SERIES_COLUMN;
		private BigDecimal R24_AMOUNT_X010;

		private String R25_ENTITY;
		private String R25_PARTICULARS;
		private String R25_Y_SERIES_COLUMN;
		private BigDecimal R25_AMOUNT_X010;

		private String R26_ENTITY;
		private String R26_PARTICULARS;
		private String R26_Y_SERIES_COLUMN;
		private BigDecimal R26_AMOUNT_X010;

		private String R27_ENTITY;
		private String R27_PARTICULARS;
		private String R27_Y_SERIES_COLUMN;
		private BigDecimal R27_AMOUNT_X010;

		private String R28_ENTITY;
		private String R28_PARTICULARS;
		private String R28_Y_SERIES_COLUMN;
		private BigDecimal R28_AMOUNT_X010;

		private String R29_ENTITY;
		private String R29_PARTICULARS;
		private String R29_Y_SERIES_COLUMN;
		private BigDecimal R29_AMOUNT_X010;

		private String R30_ENTITY;
		private String R30_PARTICULARS;
		private String R30_Y_SERIES_COLUMN;
		private BigDecimal R30_AMOUNT_X010;

		private String R31_ENTITY;
		private String R31_PARTICULARS;
		private String R31_Y_SERIES_COLUMN;
		private BigDecimal R31_AMOUNT_X010;

		private String R32_ENTITY;
		private String R32_PARTICULARS;
		private String R32_Y_SERIES_COLUMN;
		private BigDecimal R32_AMOUNT_X010;

		private String R33_ENTITY;
		private String R33_PARTICULARS;
		private String R33_Y_SERIES_COLUMN;
		private BigDecimal R33_AMOUNT_X010;

		private String R34_ENTITY;
		private String R34_PARTICULARS;
		private String R34_Y_SERIES_COLUMN;
		private BigDecimal R34_AMOUNT_X010;

		private String R35_ENTITY;
		private String R35_PARTICULARS;
		private String R35_Y_SERIES_COLUMN;
		private BigDecimal R35_AMOUNT_X010;

		private String R36_ENTITY;
		private String R36_PARTICULARS;
		private String R36_Y_SERIES_COLUMN;
		private BigDecimal R36_AMOUNT_X010;

		private String R37_ENTITY;
		private String R37_PARTICULARS;
		private String R37_Y_SERIES_COLUMN;
		private BigDecimal R37_AMOUNT_X010;

		private String R38_ENTITY;
		private String R38_PARTICULARS;
		private String R38_Y_SERIES_COLUMN;
		private BigDecimal R38_AMOUNT_X010;

		private String R39_ENTITY;
		private String R39_PARTICULARS;
		private String R39_Y_SERIES_COLUMN;
		private BigDecimal R39_AMOUNT_X010;

		private String R40_ENTITY;
		private String R40_PARTICULARS;
		private String R40_Y_SERIES_COLUMN;
		private BigDecimal R40_AMOUNT_X010;

		private String R41_ENTITY;
		private String R41_PARTICULARS;
		private String R41_Y_SERIES_COLUMN;
		private BigDecimal R41_AMOUNT_X010;

		private String R42_ENTITY;
		private String R42_PARTICULARS;
		private String R42_Y_SERIES_COLUMN;
		private BigDecimal R42_AMOUNT_X010;

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

		public String getR5_ENTITY() {
			return R5_ENTITY;
		}

		public void setR5_ENTITY(String r5_ENTITY) {
			R5_ENTITY = r5_ENTITY;
		}

		public String getR5_PARTICULARS() {
			return R5_PARTICULARS;
		}

		public void setR5_PARTICULARS(String r5_PARTICULARS) {
			R5_PARTICULARS = r5_PARTICULARS;
		}

		public String getR5_Y_SERIES_COLUMN() {
			return R5_Y_SERIES_COLUMN;
		}

		public void setR5_Y_SERIES_COLUMN(String r5_Y_SERIES_COLUMN) {
			R5_Y_SERIES_COLUMN = r5_Y_SERIES_COLUMN;
		}

		public BigDecimal getR5_AMOUNT_X010() {
			return R5_AMOUNT_X010;
		}

		public void setR5_AMOUNT_X010(BigDecimal r5_AMOUNT_X010) {
			R5_AMOUNT_X010 = r5_AMOUNT_X010;
		}

		public String getR6_ENTITY() {
			return R6_ENTITY;
		}

		public void setR6_ENTITY(String r6_ENTITY) {
			R6_ENTITY = r6_ENTITY;
		}

		public String getR6_PARTICULARS() {
			return R6_PARTICULARS;
		}

		public void setR6_PARTICULARS(String r6_PARTICULARS) {
			R6_PARTICULARS = r6_PARTICULARS;
		}

		public String getR6_Y_SERIES_COLUMN() {
			return R6_Y_SERIES_COLUMN;
		}

		public void setR6_Y_SERIES_COLUMN(String r6_Y_SERIES_COLUMN) {
			R6_Y_SERIES_COLUMN = r6_Y_SERIES_COLUMN;
		}

		public BigDecimal getR6_AMOUNT_X010() {
			return R6_AMOUNT_X010;
		}

		public void setR6_AMOUNT_X010(BigDecimal r6_AMOUNT_X010) {
			R6_AMOUNT_X010 = r6_AMOUNT_X010;
		}

		public String getR7_ENTITY() {
			return R7_ENTITY;
		}

		public void setR7_ENTITY(String r7_ENTITY) {
			R7_ENTITY = r7_ENTITY;
		}

		public String getR7_PARTICULARS() {
			return R7_PARTICULARS;
		}

		public void setR7_PARTICULARS(String r7_PARTICULARS) {
			R7_PARTICULARS = r7_PARTICULARS;
		}

		public String getR7_Y_SERIES_COLUMN() {
			return R7_Y_SERIES_COLUMN;
		}

		public void setR7_Y_SERIES_COLUMN(String r7_Y_SERIES_COLUMN) {
			R7_Y_SERIES_COLUMN = r7_Y_SERIES_COLUMN;
		}

		public BigDecimal getR7_AMOUNT_X010() {
			return R7_AMOUNT_X010;
		}

		public void setR7_AMOUNT_X010(BigDecimal r7_AMOUNT_X010) {
			R7_AMOUNT_X010 = r7_AMOUNT_X010;
		}

		public String getR8_ENTITY() {
			return R8_ENTITY;
		}

		public void setR8_ENTITY(String r8_ENTITY) {
			R8_ENTITY = r8_ENTITY;
		}

		public String getR8_PARTICULARS() {
			return R8_PARTICULARS;
		}

		public void setR8_PARTICULARS(String r8_PARTICULARS) {
			R8_PARTICULARS = r8_PARTICULARS;
		}

		public String getR8_Y_SERIES_COLUMN() {
			return R8_Y_SERIES_COLUMN;
		}

		public void setR8_Y_SERIES_COLUMN(String r8_Y_SERIES_COLUMN) {
			R8_Y_SERIES_COLUMN = r8_Y_SERIES_COLUMN;
		}

		public BigDecimal getR8_AMOUNT_X010() {
			return R8_AMOUNT_X010;
		}

		public void setR8_AMOUNT_X010(BigDecimal r8_AMOUNT_X010) {
			R8_AMOUNT_X010 = r8_AMOUNT_X010;
		}

		public String getR9_ENTITY() {
			return R9_ENTITY;
		}

		public void setR9_ENTITY(String r9_ENTITY) {
			R9_ENTITY = r9_ENTITY;
		}

		public String getR9_PARTICULARS() {
			return R9_PARTICULARS;
		}

		public void setR9_PARTICULARS(String r9_PARTICULARS) {
			R9_PARTICULARS = r9_PARTICULARS;
		}

		public String getR9_Y_SERIES_COLUMN() {
			return R9_Y_SERIES_COLUMN;
		}

		public void setR9_Y_SERIES_COLUMN(String r9_Y_SERIES_COLUMN) {
			R9_Y_SERIES_COLUMN = r9_Y_SERIES_COLUMN;
		}

		public BigDecimal getR9_AMOUNT_X010() {
			return R9_AMOUNT_X010;
		}

		public void setR9_AMOUNT_X010(BigDecimal r9_AMOUNT_X010) {
			R9_AMOUNT_X010 = r9_AMOUNT_X010;
		}

		public String getR10_ENTITY() {
			return R10_ENTITY;
		}

		public void setR10_ENTITY(String r10_ENTITY) {
			R10_ENTITY = r10_ENTITY;
		}

		public String getR10_PARTICULARS() {
			return R10_PARTICULARS;
		}

		public void setR10_PARTICULARS(String r10_PARTICULARS) {
			R10_PARTICULARS = r10_PARTICULARS;
		}

		public String getR10_Y_SERIES_COLUMN() {
			return R10_Y_SERIES_COLUMN;
		}

		public void setR10_Y_SERIES_COLUMN(String r10_Y_SERIES_COLUMN) {
			R10_Y_SERIES_COLUMN = r10_Y_SERIES_COLUMN;
		}

		public BigDecimal getR10_AMOUNT_X010() {
			return R10_AMOUNT_X010;
		}

		public void setR10_AMOUNT_X010(BigDecimal r10_AMOUNT_X010) {
			R10_AMOUNT_X010 = r10_AMOUNT_X010;
		}

		public String getR11_ENTITY() {
			return R11_ENTITY;
		}

		public void setR11_ENTITY(String r11_ENTITY) {
			R11_ENTITY = r11_ENTITY;
		}

		public String getR11_PARTICULARS() {
			return R11_PARTICULARS;
		}

		public void setR11_PARTICULARS(String r11_PARTICULARS) {
			R11_PARTICULARS = r11_PARTICULARS;
		}

		public String getR11_Y_SERIES_COLUMN() {
			return R11_Y_SERIES_COLUMN;
		}

		public void setR11_Y_SERIES_COLUMN(String r11_Y_SERIES_COLUMN) {
			R11_Y_SERIES_COLUMN = r11_Y_SERIES_COLUMN;
		}

		public BigDecimal getR11_AMOUNT_X010() {
			return R11_AMOUNT_X010;
		}

		public void setR11_AMOUNT_X010(BigDecimal r11_AMOUNT_X010) {
			R11_AMOUNT_X010 = r11_AMOUNT_X010;
		}

		public String getR12_ENTITY() {
			return R12_ENTITY;
		}

		public void setR12_ENTITY(String r12_ENTITY) {
			R12_ENTITY = r12_ENTITY;
		}

		public String getR12_PARTICULARS() {
			return R12_PARTICULARS;
		}

		public void setR12_PARTICULARS(String r12_PARTICULARS) {
			R12_PARTICULARS = r12_PARTICULARS;
		}

		public String getR12_Y_SERIES_COLUMN() {
			return R12_Y_SERIES_COLUMN;
		}

		public void setR12_Y_SERIES_COLUMN(String r12_Y_SERIES_COLUMN) {
			R12_Y_SERIES_COLUMN = r12_Y_SERIES_COLUMN;
		}

		public BigDecimal getR12_AMOUNT_X010() {
			return R12_AMOUNT_X010;
		}

		public void setR12_AMOUNT_X010(BigDecimal r12_AMOUNT_X010) {
			R12_AMOUNT_X010 = r12_AMOUNT_X010;
		}

		public String getR13_ENTITY() {
			return R13_ENTITY;
		}

		public void setR13_ENTITY(String r13_ENTITY) {
			R13_ENTITY = r13_ENTITY;
		}

		public String getR13_PARTICULARS() {
			return R13_PARTICULARS;
		}

		public void setR13_PARTICULARS(String r13_PARTICULARS) {
			R13_PARTICULARS = r13_PARTICULARS;
		}

		public String getR13_Y_SERIES_COLUMN() {
			return R13_Y_SERIES_COLUMN;
		}

		public void setR13_Y_SERIES_COLUMN(String r13_Y_SERIES_COLUMN) {
			R13_Y_SERIES_COLUMN = r13_Y_SERIES_COLUMN;
		}

		public BigDecimal getR13_AMOUNT_X010() {
			return R13_AMOUNT_X010;
		}

		public void setR13_AMOUNT_X010(BigDecimal r13_AMOUNT_X010) {
			R13_AMOUNT_X010 = r13_AMOUNT_X010;
		}

		public String getR14_ENTITY() {
			return R14_ENTITY;
		}

		public void setR14_ENTITY(String r14_ENTITY) {
			R14_ENTITY = r14_ENTITY;
		}

		public String getR14_PARTICULARS() {
			return R14_PARTICULARS;
		}

		public void setR14_PARTICULARS(String r14_PARTICULARS) {
			R14_PARTICULARS = r14_PARTICULARS;
		}

		public String getR14_Y_SERIES_COLUMN() {
			return R14_Y_SERIES_COLUMN;
		}

		public void setR14_Y_SERIES_COLUMN(String r14_Y_SERIES_COLUMN) {
			R14_Y_SERIES_COLUMN = r14_Y_SERIES_COLUMN;
		}

		public BigDecimal getR14_AMOUNT_X010() {
			return R14_AMOUNT_X010;
		}

		public void setR14_AMOUNT_X010(BigDecimal r14_AMOUNT_X010) {
			R14_AMOUNT_X010 = r14_AMOUNT_X010;
		}

		public String getR15_ENTITY() {
			return R15_ENTITY;
		}

		public void setR15_ENTITY(String r15_ENTITY) {
			R15_ENTITY = r15_ENTITY;
		}

		public String getR15_PARTICULARS() {
			return R15_PARTICULARS;
		}

		public void setR15_PARTICULARS(String r15_PARTICULARS) {
			R15_PARTICULARS = r15_PARTICULARS;
		}

		public String getR15_Y_SERIES_COLUMN() {
			return R15_Y_SERIES_COLUMN;
		}

		public void setR15_Y_SERIES_COLUMN(String r15_Y_SERIES_COLUMN) {
			R15_Y_SERIES_COLUMN = r15_Y_SERIES_COLUMN;
		}

		public BigDecimal getR15_AMOUNT_X010() {
			return R15_AMOUNT_X010;
		}

		public void setR15_AMOUNT_X010(BigDecimal r15_AMOUNT_X010) {
			R15_AMOUNT_X010 = r15_AMOUNT_X010;
		}

		public String getR16_ENTITY() {
			return R16_ENTITY;
		}

		public void setR16_ENTITY(String r16_ENTITY) {
			R16_ENTITY = r16_ENTITY;
		}

		public String getR16_PARTICULARS() {
			return R16_PARTICULARS;
		}

		public void setR16_PARTICULARS(String r16_PARTICULARS) {
			R16_PARTICULARS = r16_PARTICULARS;
		}

		public String getR16_Y_SERIES_COLUMN() {
			return R16_Y_SERIES_COLUMN;
		}

		public void setR16_Y_SERIES_COLUMN(String r16_Y_SERIES_COLUMN) {
			R16_Y_SERIES_COLUMN = r16_Y_SERIES_COLUMN;
		}

		public BigDecimal getR16_AMOUNT_X010() {
			return R16_AMOUNT_X010;
		}

		public void setR16_AMOUNT_X010(BigDecimal r16_AMOUNT_X010) {
			R16_AMOUNT_X010 = r16_AMOUNT_X010;
		}

		public String getR17_ENTITY() {
			return R17_ENTITY;
		}

		public void setR17_ENTITY(String r17_ENTITY) {
			R17_ENTITY = r17_ENTITY;
		}

		public String getR17_PARTICULARS() {
			return R17_PARTICULARS;
		}

		public void setR17_PARTICULARS(String r17_PARTICULARS) {
			R17_PARTICULARS = r17_PARTICULARS;
		}

		public String getR17_Y_SERIES_COLUMN() {
			return R17_Y_SERIES_COLUMN;
		}

		public void setR17_Y_SERIES_COLUMN(String r17_Y_SERIES_COLUMN) {
			R17_Y_SERIES_COLUMN = r17_Y_SERIES_COLUMN;
		}

		public BigDecimal getR17_AMOUNT_X010() {
			return R17_AMOUNT_X010;
		}

		public void setR17_AMOUNT_X010(BigDecimal r17_AMOUNT_X010) {
			R17_AMOUNT_X010 = r17_AMOUNT_X010;
		}

		public String getR18_ENTITY() {
			return R18_ENTITY;
		}

		public void setR18_ENTITY(String r18_ENTITY) {
			R18_ENTITY = r18_ENTITY;
		}

		public String getR18_PARTICULARS() {
			return R18_PARTICULARS;
		}

		public void setR18_PARTICULARS(String r18_PARTICULARS) {
			R18_PARTICULARS = r18_PARTICULARS;
		}

		public String getR18_Y_SERIES_COLUMN() {
			return R18_Y_SERIES_COLUMN;
		}

		public void setR18_Y_SERIES_COLUMN(String r18_Y_SERIES_COLUMN) {
			R18_Y_SERIES_COLUMN = r18_Y_SERIES_COLUMN;
		}

		public BigDecimal getR18_AMOUNT_X010() {
			return R18_AMOUNT_X010;
		}

		public void setR18_AMOUNT_X010(BigDecimal r18_AMOUNT_X010) {
			R18_AMOUNT_X010 = r18_AMOUNT_X010;
		}

		public String getR19_ENTITY() {
			return R19_ENTITY;
		}

		public void setR19_ENTITY(String r19_ENTITY) {
			R19_ENTITY = r19_ENTITY;
		}

		public String getR19_PARTICULARS() {
			return R19_PARTICULARS;
		}

		public void setR19_PARTICULARS(String r19_PARTICULARS) {
			R19_PARTICULARS = r19_PARTICULARS;
		}

		public String getR19_Y_SERIES_COLUMN() {
			return R19_Y_SERIES_COLUMN;
		}

		public void setR19_Y_SERIES_COLUMN(String r19_Y_SERIES_COLUMN) {
			R19_Y_SERIES_COLUMN = r19_Y_SERIES_COLUMN;
		}

		public BigDecimal getR19_AMOUNT_X010() {
			return R19_AMOUNT_X010;
		}

		public void setR19_AMOUNT_X010(BigDecimal r19_AMOUNT_X010) {
			R19_AMOUNT_X010 = r19_AMOUNT_X010;
		}

		public String getR20_ENTITY() {
			return R20_ENTITY;
		}

		public void setR20_ENTITY(String r20_ENTITY) {
			R20_ENTITY = r20_ENTITY;
		}

		public String getR20_PARTICULARS() {
			return R20_PARTICULARS;
		}

		public void setR20_PARTICULARS(String r20_PARTICULARS) {
			R20_PARTICULARS = r20_PARTICULARS;
		}

		public String getR20_Y_SERIES_COLUMN() {
			return R20_Y_SERIES_COLUMN;
		}

		public void setR20_Y_SERIES_COLUMN(String r20_Y_SERIES_COLUMN) {
			R20_Y_SERIES_COLUMN = r20_Y_SERIES_COLUMN;
		}

		public BigDecimal getR20_AMOUNT_X010() {
			return R20_AMOUNT_X010;
		}

		public void setR20_AMOUNT_X010(BigDecimal r20_AMOUNT_X010) {
			R20_AMOUNT_X010 = r20_AMOUNT_X010;
		}

		public String getR21_ENTITY() {
			return R21_ENTITY;
		}

		public void setR21_ENTITY(String r21_ENTITY) {
			R21_ENTITY = r21_ENTITY;
		}

		public String getR21_PARTICULARS() {
			return R21_PARTICULARS;
		}

		public void setR21_PARTICULARS(String r21_PARTICULARS) {
			R21_PARTICULARS = r21_PARTICULARS;
		}

		public String getR21_Y_SERIES_COLUMN() {
			return R21_Y_SERIES_COLUMN;
		}

		public void setR21_Y_SERIES_COLUMN(String r21_Y_SERIES_COLUMN) {
			R21_Y_SERIES_COLUMN = r21_Y_SERIES_COLUMN;
		}

		public BigDecimal getR21_AMOUNT_X010() {
			return R21_AMOUNT_X010;
		}

		public void setR21_AMOUNT_X010(BigDecimal r21_AMOUNT_X010) {
			R21_AMOUNT_X010 = r21_AMOUNT_X010;
		}

		public String getR22_ENTITY() {
			return R22_ENTITY;
		}

		public void setR22_ENTITY(String r22_ENTITY) {
			R22_ENTITY = r22_ENTITY;
		}

		public String getR22_PARTICULARS() {
			return R22_PARTICULARS;
		}

		public void setR22_PARTICULARS(String r22_PARTICULARS) {
			R22_PARTICULARS = r22_PARTICULARS;
		}

		public String getR22_Y_SERIES_COLUMN() {
			return R22_Y_SERIES_COLUMN;
		}

		public void setR22_Y_SERIES_COLUMN(String r22_Y_SERIES_COLUMN) {
			R22_Y_SERIES_COLUMN = r22_Y_SERIES_COLUMN;
		}

		public BigDecimal getR22_AMOUNT_X010() {
			return R22_AMOUNT_X010;
		}

		public void setR22_AMOUNT_X010(BigDecimal r22_AMOUNT_X010) {
			R22_AMOUNT_X010 = r22_AMOUNT_X010;
		}

		public String getR23_ENTITY() {
			return R23_ENTITY;
		}

		public void setR23_ENTITY(String r23_ENTITY) {
			R23_ENTITY = r23_ENTITY;
		}

		public String getR23_PARTICULARS() {
			return R23_PARTICULARS;
		}

		public void setR23_PARTICULARS(String r23_PARTICULARS) {
			R23_PARTICULARS = r23_PARTICULARS;
		}

		public String getR23_Y_SERIES_COLUMN() {
			return R23_Y_SERIES_COLUMN;
		}

		public void setR23_Y_SERIES_COLUMN(String r23_Y_SERIES_COLUMN) {
			R23_Y_SERIES_COLUMN = r23_Y_SERIES_COLUMN;
		}

		public BigDecimal getR23_AMOUNT_X010() {
			return R23_AMOUNT_X010;
		}

		public void setR23_AMOUNT_X010(BigDecimal r23_AMOUNT_X010) {
			R23_AMOUNT_X010 = r23_AMOUNT_X010;
		}

		public String getR24_ENTITY() {
			return R24_ENTITY;
		}

		public void setR24_ENTITY(String r24_ENTITY) {
			R24_ENTITY = r24_ENTITY;
		}

		public String getR24_PARTICULARS() {
			return R24_PARTICULARS;
		}

		public void setR24_PARTICULARS(String r24_PARTICULARS) {
			R24_PARTICULARS = r24_PARTICULARS;
		}

		public String getR24_Y_SERIES_COLUMN() {
			return R24_Y_SERIES_COLUMN;
		}

		public void setR24_Y_SERIES_COLUMN(String r24_Y_SERIES_COLUMN) {
			R24_Y_SERIES_COLUMN = r24_Y_SERIES_COLUMN;
		}

		public BigDecimal getR24_AMOUNT_X010() {
			return R24_AMOUNT_X010;
		}

		public void setR24_AMOUNT_X010(BigDecimal r24_AMOUNT_X010) {
			R24_AMOUNT_X010 = r24_AMOUNT_X010;
		}

		public String getR25_ENTITY() {
			return R25_ENTITY;
		}

		public void setR25_ENTITY(String r25_ENTITY) {
			R25_ENTITY = r25_ENTITY;
		}

		public String getR25_PARTICULARS() {
			return R25_PARTICULARS;
		}

		public void setR25_PARTICULARS(String r25_PARTICULARS) {
			R25_PARTICULARS = r25_PARTICULARS;
		}

		public String getR25_Y_SERIES_COLUMN() {
			return R25_Y_SERIES_COLUMN;
		}

		public void setR25_Y_SERIES_COLUMN(String r25_Y_SERIES_COLUMN) {
			R25_Y_SERIES_COLUMN = r25_Y_SERIES_COLUMN;
		}

		public BigDecimal getR25_AMOUNT_X010() {
			return R25_AMOUNT_X010;
		}

		public void setR25_AMOUNT_X010(BigDecimal r25_AMOUNT_X010) {
			R25_AMOUNT_X010 = r25_AMOUNT_X010;
		}

		public String getR26_ENTITY() {
			return R26_ENTITY;
		}

		public void setR26_ENTITY(String r26_ENTITY) {
			R26_ENTITY = r26_ENTITY;
		}

		public String getR26_PARTICULARS() {
			return R26_PARTICULARS;
		}

		public void setR26_PARTICULARS(String r26_PARTICULARS) {
			R26_PARTICULARS = r26_PARTICULARS;
		}

		public String getR26_Y_SERIES_COLUMN() {
			return R26_Y_SERIES_COLUMN;
		}

		public void setR26_Y_SERIES_COLUMN(String r26_Y_SERIES_COLUMN) {
			R26_Y_SERIES_COLUMN = r26_Y_SERIES_COLUMN;
		}

		public BigDecimal getR26_AMOUNT_X010() {
			return R26_AMOUNT_X010;
		}

		public void setR26_AMOUNT_X010(BigDecimal r26_AMOUNT_X010) {
			R26_AMOUNT_X010 = r26_AMOUNT_X010;
		}

		public String getR27_ENTITY() {
			return R27_ENTITY;
		}

		public void setR27_ENTITY(String r27_ENTITY) {
			R27_ENTITY = r27_ENTITY;
		}

		public String getR27_PARTICULARS() {
			return R27_PARTICULARS;
		}

		public void setR27_PARTICULARS(String r27_PARTICULARS) {
			R27_PARTICULARS = r27_PARTICULARS;
		}

		public String getR27_Y_SERIES_COLUMN() {
			return R27_Y_SERIES_COLUMN;
		}

		public void setR27_Y_SERIES_COLUMN(String r27_Y_SERIES_COLUMN) {
			R27_Y_SERIES_COLUMN = r27_Y_SERIES_COLUMN;
		}

		public BigDecimal getR27_AMOUNT_X010() {
			return R27_AMOUNT_X010;
		}

		public void setR27_AMOUNT_X010(BigDecimal r27_AMOUNT_X010) {
			R27_AMOUNT_X010 = r27_AMOUNT_X010;
		}

		public String getR28_ENTITY() {
			return R28_ENTITY;
		}

		public void setR28_ENTITY(String r28_ENTITY) {
			R28_ENTITY = r28_ENTITY;
		}

		public String getR28_PARTICULARS() {
			return R28_PARTICULARS;
		}

		public void setR28_PARTICULARS(String r28_PARTICULARS) {
			R28_PARTICULARS = r28_PARTICULARS;
		}

		public String getR28_Y_SERIES_COLUMN() {
			return R28_Y_SERIES_COLUMN;
		}

		public void setR28_Y_SERIES_COLUMN(String r28_Y_SERIES_COLUMN) {
			R28_Y_SERIES_COLUMN = r28_Y_SERIES_COLUMN;
		}

		public BigDecimal getR28_AMOUNT_X010() {
			return R28_AMOUNT_X010;
		}

		public void setR28_AMOUNT_X010(BigDecimal r28_AMOUNT_X010) {
			R28_AMOUNT_X010 = r28_AMOUNT_X010;
		}

		public String getR29_ENTITY() {
			return R29_ENTITY;
		}

		public void setR29_ENTITY(String r29_ENTITY) {
			R29_ENTITY = r29_ENTITY;
		}

		public String getR29_PARTICULARS() {
			return R29_PARTICULARS;
		}

		public void setR29_PARTICULARS(String r29_PARTICULARS) {
			R29_PARTICULARS = r29_PARTICULARS;
		}

		public String getR29_Y_SERIES_COLUMN() {
			return R29_Y_SERIES_COLUMN;
		}

		public void setR29_Y_SERIES_COLUMN(String r29_Y_SERIES_COLUMN) {
			R29_Y_SERIES_COLUMN = r29_Y_SERIES_COLUMN;
		}

		public BigDecimal getR29_AMOUNT_X010() {
			return R29_AMOUNT_X010;
		}

		public void setR29_AMOUNT_X010(BigDecimal r29_AMOUNT_X010) {
			R29_AMOUNT_X010 = r29_AMOUNT_X010;
		}

		public String getR30_ENTITY() {
			return R30_ENTITY;
		}

		public void setR30_ENTITY(String r30_ENTITY) {
			R30_ENTITY = r30_ENTITY;
		}

		public String getR30_PARTICULARS() {
			return R30_PARTICULARS;
		}

		public void setR30_PARTICULARS(String r30_PARTICULARS) {
			R30_PARTICULARS = r30_PARTICULARS;
		}

		public String getR30_Y_SERIES_COLUMN() {
			return R30_Y_SERIES_COLUMN;
		}

		public void setR30_Y_SERIES_COLUMN(String r30_Y_SERIES_COLUMN) {
			R30_Y_SERIES_COLUMN = r30_Y_SERIES_COLUMN;
		}

		public BigDecimal getR30_AMOUNT_X010() {
			return R30_AMOUNT_X010;
		}

		public void setR30_AMOUNT_X010(BigDecimal r30_AMOUNT_X010) {
			R30_AMOUNT_X010 = r30_AMOUNT_X010;
		}

		public String getR31_ENTITY() {
			return R31_ENTITY;
		}

		public void setR31_ENTITY(String r31_ENTITY) {
			R31_ENTITY = r31_ENTITY;
		}

		public String getR31_PARTICULARS() {
			return R31_PARTICULARS;
		}

		public void setR31_PARTICULARS(String r31_PARTICULARS) {
			R31_PARTICULARS = r31_PARTICULARS;
		}

		public String getR31_Y_SERIES_COLUMN() {
			return R31_Y_SERIES_COLUMN;
		}

		public void setR31_Y_SERIES_COLUMN(String r31_Y_SERIES_COLUMN) {
			R31_Y_SERIES_COLUMN = r31_Y_SERIES_COLUMN;
		}

		public BigDecimal getR31_AMOUNT_X010() {
			return R31_AMOUNT_X010;
		}

		public void setR31_AMOUNT_X010(BigDecimal r31_AMOUNT_X010) {
			R31_AMOUNT_X010 = r31_AMOUNT_X010;
		}

		public String getR32_ENTITY() {
			return R32_ENTITY;
		}

		public void setR32_ENTITY(String r32_ENTITY) {
			R32_ENTITY = r32_ENTITY;
		}

		public String getR32_PARTICULARS() {
			return R32_PARTICULARS;
		}

		public void setR32_PARTICULARS(String r32_PARTICULARS) {
			R32_PARTICULARS = r32_PARTICULARS;
		}

		public String getR32_Y_SERIES_COLUMN() {
			return R32_Y_SERIES_COLUMN;
		}

		public void setR32_Y_SERIES_COLUMN(String r32_Y_SERIES_COLUMN) {
			R32_Y_SERIES_COLUMN = r32_Y_SERIES_COLUMN;
		}

		public BigDecimal getR32_AMOUNT_X010() {
			return R32_AMOUNT_X010;
		}

		public void setR32_AMOUNT_X010(BigDecimal r32_AMOUNT_X010) {
			R32_AMOUNT_X010 = r32_AMOUNT_X010;
		}

		public String getR33_ENTITY() {
			return R33_ENTITY;
		}

		public void setR33_ENTITY(String r33_ENTITY) {
			R33_ENTITY = r33_ENTITY;
		}

		public String getR33_PARTICULARS() {
			return R33_PARTICULARS;
		}

		public void setR33_PARTICULARS(String r33_PARTICULARS) {
			R33_PARTICULARS = r33_PARTICULARS;
		}

		public String getR33_Y_SERIES_COLUMN() {
			return R33_Y_SERIES_COLUMN;
		}

		public void setR33_Y_SERIES_COLUMN(String r33_Y_SERIES_COLUMN) {
			R33_Y_SERIES_COLUMN = r33_Y_SERIES_COLUMN;
		}

		public BigDecimal getR33_AMOUNT_X010() {
			return R33_AMOUNT_X010;
		}

		public void setR33_AMOUNT_X010(BigDecimal r33_AMOUNT_X010) {
			R33_AMOUNT_X010 = r33_AMOUNT_X010;
		}

		public String getR34_ENTITY() {
			return R34_ENTITY;
		}

		public void setR34_ENTITY(String r34_ENTITY) {
			R34_ENTITY = r34_ENTITY;
		}

		public String getR34_PARTICULARS() {
			return R34_PARTICULARS;
		}

		public void setR34_PARTICULARS(String r34_PARTICULARS) {
			R34_PARTICULARS = r34_PARTICULARS;
		}

		public String getR34_Y_SERIES_COLUMN() {
			return R34_Y_SERIES_COLUMN;
		}

		public void setR34_Y_SERIES_COLUMN(String r34_Y_SERIES_COLUMN) {
			R34_Y_SERIES_COLUMN = r34_Y_SERIES_COLUMN;
		}

		public BigDecimal getR34_AMOUNT_X010() {
			return R34_AMOUNT_X010;
		}

		public void setR34_AMOUNT_X010(BigDecimal r34_AMOUNT_X010) {
			R34_AMOUNT_X010 = r34_AMOUNT_X010;
		}

		public String getR35_ENTITY() {
			return R35_ENTITY;
		}

		public void setR35_ENTITY(String r35_ENTITY) {
			R35_ENTITY = r35_ENTITY;
		}

		public String getR35_PARTICULARS() {
			return R35_PARTICULARS;
		}

		public void setR35_PARTICULARS(String r35_PARTICULARS) {
			R35_PARTICULARS = r35_PARTICULARS;
		}

		public String getR35_Y_SERIES_COLUMN() {
			return R35_Y_SERIES_COLUMN;
		}

		public void setR35_Y_SERIES_COLUMN(String r35_Y_SERIES_COLUMN) {
			R35_Y_SERIES_COLUMN = r35_Y_SERIES_COLUMN;
		}

		public BigDecimal getR35_AMOUNT_X010() {
			return R35_AMOUNT_X010;
		}

		public void setR35_AMOUNT_X010(BigDecimal r35_AMOUNT_X010) {
			R35_AMOUNT_X010 = r35_AMOUNT_X010;
		}

		public String getR36_ENTITY() {
			return R36_ENTITY;
		}

		public void setR36_ENTITY(String r36_ENTITY) {
			R36_ENTITY = r36_ENTITY;
		}

		public String getR36_PARTICULARS() {
			return R36_PARTICULARS;
		}

		public void setR36_PARTICULARS(String r36_PARTICULARS) {
			R36_PARTICULARS = r36_PARTICULARS;
		}

		public String getR36_Y_SERIES_COLUMN() {
			return R36_Y_SERIES_COLUMN;
		}

		public void setR36_Y_SERIES_COLUMN(String r36_Y_SERIES_COLUMN) {
			R36_Y_SERIES_COLUMN = r36_Y_SERIES_COLUMN;
		}

		public BigDecimal getR36_AMOUNT_X010() {
			return R36_AMOUNT_X010;
		}

		public void setR36_AMOUNT_X010(BigDecimal r36_AMOUNT_X010) {
			R36_AMOUNT_X010 = r36_AMOUNT_X010;
		}

		public String getR37_ENTITY() {
			return R37_ENTITY;
		}

		public void setR37_ENTITY(String r37_ENTITY) {
			R37_ENTITY = r37_ENTITY;
		}

		public String getR37_PARTICULARS() {
			return R37_PARTICULARS;
		}

		public void setR37_PARTICULARS(String r37_PARTICULARS) {
			R37_PARTICULARS = r37_PARTICULARS;
		}

		public String getR37_Y_SERIES_COLUMN() {
			return R37_Y_SERIES_COLUMN;
		}

		public void setR37_Y_SERIES_COLUMN(String r37_Y_SERIES_COLUMN) {
			R37_Y_SERIES_COLUMN = r37_Y_SERIES_COLUMN;
		}

		public BigDecimal getR37_AMOUNT_X010() {
			return R37_AMOUNT_X010;
		}

		public void setR37_AMOUNT_X010(BigDecimal r37_AMOUNT_X010) {
			R37_AMOUNT_X010 = r37_AMOUNT_X010;
		}

		public String getR38_ENTITY() {
			return R38_ENTITY;
		}

		public void setR38_ENTITY(String r38_ENTITY) {
			R38_ENTITY = r38_ENTITY;
		}

		public String getR38_PARTICULARS() {
			return R38_PARTICULARS;
		}

		public void setR38_PARTICULARS(String r38_PARTICULARS) {
			R38_PARTICULARS = r38_PARTICULARS;
		}

		public String getR38_Y_SERIES_COLUMN() {
			return R38_Y_SERIES_COLUMN;
		}

		public void setR38_Y_SERIES_COLUMN(String r38_Y_SERIES_COLUMN) {
			R38_Y_SERIES_COLUMN = r38_Y_SERIES_COLUMN;
		}

		public BigDecimal getR38_AMOUNT_X010() {
			return R38_AMOUNT_X010;
		}

		public void setR38_AMOUNT_X010(BigDecimal r38_AMOUNT_X010) {
			R38_AMOUNT_X010 = r38_AMOUNT_X010;
		}

		public String getR39_ENTITY() {
			return R39_ENTITY;
		}

		public void setR39_ENTITY(String r39_ENTITY) {
			R39_ENTITY = r39_ENTITY;
		}

		public String getR39_PARTICULARS() {
			return R39_PARTICULARS;
		}

		public void setR39_PARTICULARS(String r39_PARTICULARS) {
			R39_PARTICULARS = r39_PARTICULARS;
		}

		public String getR39_Y_SERIES_COLUMN() {
			return R39_Y_SERIES_COLUMN;
		}

		public void setR39_Y_SERIES_COLUMN(String r39_Y_SERIES_COLUMN) {
			R39_Y_SERIES_COLUMN = r39_Y_SERIES_COLUMN;
		}

		public BigDecimal getR39_AMOUNT_X010() {
			return R39_AMOUNT_X010;
		}

		public void setR39_AMOUNT_X010(BigDecimal r39_AMOUNT_X010) {
			R39_AMOUNT_X010 = r39_AMOUNT_X010;
		}

		public String getR40_ENTITY() {
			return R40_ENTITY;
		}

		public void setR40_ENTITY(String r40_ENTITY) {
			R40_ENTITY = r40_ENTITY;
		}

		public String getR40_PARTICULARS() {
			return R40_PARTICULARS;
		}

		public void setR40_PARTICULARS(String r40_PARTICULARS) {
			R40_PARTICULARS = r40_PARTICULARS;
		}

		public String getR40_Y_SERIES_COLUMN() {
			return R40_Y_SERIES_COLUMN;
		}

		public void setR40_Y_SERIES_COLUMN(String r40_Y_SERIES_COLUMN) {
			R40_Y_SERIES_COLUMN = r40_Y_SERIES_COLUMN;
		}

		public BigDecimal getR40_AMOUNT_X010() {
			return R40_AMOUNT_X010;
		}

		public void setR40_AMOUNT_X010(BigDecimal r40_AMOUNT_X010) {
			R40_AMOUNT_X010 = r40_AMOUNT_X010;
		}

		public String getR41_ENTITY() {
			return R41_ENTITY;
		}

		public void setR41_ENTITY(String r41_ENTITY) {
			R41_ENTITY = r41_ENTITY;
		}

		public String getR41_PARTICULARS() {
			return R41_PARTICULARS;
		}

		public void setR41_PARTICULARS(String r41_PARTICULARS) {
			R41_PARTICULARS = r41_PARTICULARS;
		}

		public String getR41_Y_SERIES_COLUMN() {
			return R41_Y_SERIES_COLUMN;
		}

		public void setR41_Y_SERIES_COLUMN(String r41_Y_SERIES_COLUMN) {
			R41_Y_SERIES_COLUMN = r41_Y_SERIES_COLUMN;
		}

		public BigDecimal getR41_AMOUNT_X010() {
			return R41_AMOUNT_X010;
		}

		public void setR41_AMOUNT_X010(BigDecimal r41_AMOUNT_X010) {
			R41_AMOUNT_X010 = r41_AMOUNT_X010;
		}

		public String getR42_ENTITY() {
			return R42_ENTITY;
		}

		public void setR42_ENTITY(String r42_ENTITY) {
			R42_ENTITY = r42_ENTITY;
		}

		public String getR42_PARTICULARS() {
			return R42_PARTICULARS;
		}

		public void setR42_PARTICULARS(String r42_PARTICULARS) {
			R42_PARTICULARS = r42_PARTICULARS;
		}

		public String getR42_Y_SERIES_COLUMN() {
			return R42_Y_SERIES_COLUMN;
		}

		public void setR42_Y_SERIES_COLUMN(String r42_Y_SERIES_COLUMN) {
			R42_Y_SERIES_COLUMN = r42_Y_SERIES_COLUMN;
		}

		public BigDecimal getR42_AMOUNT_X010() {
			return R42_AMOUNT_X010;
		}

		public void setR42_AMOUNT_X010(BigDecimal r42_AMOUNT_X010) {
			R42_AMOUNT_X010 = r42_AMOUNT_X010;
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

	public static class DBS10_FINCON_II_1A_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public DBS10_FINCON_II_1A_PK() {
		}

		public DBS10_FINCON_II_1A_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof DBS10_FINCON_II_1A_PK))
				return false;
			DBS10_FINCON_II_1A_PK that = (DBS10_FINCON_II_1A_PK) o;
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

	class ADISB1ManualArchivalRowMapper implements RowMapper<DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity obj = new DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity();

			// R8
			obj.setR8_AMOUNT_X010(rs.getBigDecimal("R8_AMOUNT_X010"));

// R12
			obj.setR12_AMOUNT_X010(rs.getBigDecimal("R12_AMOUNT_X010"));

// R14
			obj.setR14_AMOUNT_X010(rs.getBigDecimal("R14_AMOUNT_X010"));

// R22
			obj.setR22_AMOUNT_X010(rs.getBigDecimal("R22_AMOUNT_X010"));

// R23
			obj.setR23_AMOUNT_X010(rs.getBigDecimal("R23_AMOUNT_X010"));

// R25
			obj.setR25_AMOUNT_X010(rs.getBigDecimal("R25_AMOUNT_X010"));

// R26
			obj.setR26_AMOUNT_X010(rs.getBigDecimal("R26_AMOUNT_X010"));

// R27
			obj.setR27_AMOUNT_X010(rs.getBigDecimal("R27_AMOUNT_X010"));

// R28
			obj.setR28_AMOUNT_X010(rs.getBigDecimal("R28_AMOUNT_X010"));

// R31
			obj.setR31_AMOUNT_X010(rs.getBigDecimal("R31_AMOUNT_X010"));

// R32
			obj.setR32_AMOUNT_X010(rs.getBigDecimal("R32_AMOUNT_X010"));

// R35
			obj.setR35_AMOUNT_X010(rs.getBigDecimal("R35_AMOUNT_X010"));

// R36
			obj.setR36_AMOUNT_X010(rs.getBigDecimal("R36_AMOUNT_X010"));

// R37
			obj.setR37_AMOUNT_X010(rs.getBigDecimal("R37_AMOUNT_X010"));

// R41
			obj.setR41_AMOUNT_X010(rs.getBigDecimal("R41_AMOUNT_X010"));

// R42
			obj.setR42_AMOUNT_X010(rs.getBigDecimal("R42_AMOUNT_X010"));
			// ================= COMMON =================
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

	public static class DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity {

		private BigDecimal R8_AMOUNT_X010;
		private BigDecimal R12_AMOUNT_X010;
		private BigDecimal R14_AMOUNT_X010;
		private BigDecimal R22_AMOUNT_X010;
		private BigDecimal R23_AMOUNT_X010;
		private BigDecimal R25_AMOUNT_X010;
		private BigDecimal R26_AMOUNT_X010;
		private BigDecimal R27_AMOUNT_X010;
		private BigDecimal R28_AMOUNT_X010;
		private BigDecimal R31_AMOUNT_X010;
		private BigDecimal R32_AMOUNT_X010;
		private BigDecimal R35_AMOUNT_X010;
		private BigDecimal R36_AMOUNT_X010;
		private BigDecimal R37_AMOUNT_X010;
		private BigDecimal R41_AMOUNT_X010;
		private BigDecimal R42_AMOUNT_X010;
		// ================= COMMON =================
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private Date REPORT_RESUBDATE;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
		}

		public BigDecimal getR8_AMOUNT_X010() {
			return R8_AMOUNT_X010;
		}

		public void setR8_AMOUNT_X010(BigDecimal r8_AMOUNT_X010) {
			R8_AMOUNT_X010 = r8_AMOUNT_X010;
		}

		public BigDecimal getR12_AMOUNT_X010() {
			return R12_AMOUNT_X010;
		}

		public void setR12_AMOUNT_X010(BigDecimal r12_AMOUNT_X010) {
			R12_AMOUNT_X010 = r12_AMOUNT_X010;
		}

		public BigDecimal getR14_AMOUNT_X010() {
			return R14_AMOUNT_X010;
		}

		public void setR14_AMOUNT_X010(BigDecimal r14_AMOUNT_X010) {
			R14_AMOUNT_X010 = r14_AMOUNT_X010;
		}

		public BigDecimal getR22_AMOUNT_X010() {
			return R22_AMOUNT_X010;
		}

		public void setR22_AMOUNT_X010(BigDecimal r22_AMOUNT_X010) {
			R22_AMOUNT_X010 = r22_AMOUNT_X010;
		}

		public BigDecimal getR23_AMOUNT_X010() {
			return R23_AMOUNT_X010;
		}

		public void setR23_AMOUNT_X010(BigDecimal r23_AMOUNT_X010) {
			R23_AMOUNT_X010 = r23_AMOUNT_X010;
		}

		public BigDecimal getR25_AMOUNT_X010() {
			return R25_AMOUNT_X010;
		}

		public void setR25_AMOUNT_X010(BigDecimal r25_AMOUNT_X010) {
			R25_AMOUNT_X010 = r25_AMOUNT_X010;
		}

		public BigDecimal getR26_AMOUNT_X010() {
			return R26_AMOUNT_X010;
		}

		public void setR26_AMOUNT_X010(BigDecimal r26_AMOUNT_X010) {
			R26_AMOUNT_X010 = r26_AMOUNT_X010;
		}

		public BigDecimal getR27_AMOUNT_X010() {
			return R27_AMOUNT_X010;
		}

		public void setR27_AMOUNT_X010(BigDecimal r27_AMOUNT_X010) {
			R27_AMOUNT_X010 = r27_AMOUNT_X010;
		}

		public BigDecimal getR28_AMOUNT_X010() {
			return R28_AMOUNT_X010;
		}

		public void setR28_AMOUNT_X010(BigDecimal r28_AMOUNT_X010) {
			R28_AMOUNT_X010 = r28_AMOUNT_X010;
		}

		public BigDecimal getR31_AMOUNT_X010() {
			return R31_AMOUNT_X010;
		}

		public void setR31_AMOUNT_X010(BigDecimal r31_AMOUNT_X010) {
			R31_AMOUNT_X010 = r31_AMOUNT_X010;
		}

		public BigDecimal getR32_AMOUNT_X010() {
			return R32_AMOUNT_X010;
		}

		public void setR32_AMOUNT_X010(BigDecimal r32_AMOUNT_X010) {
			R32_AMOUNT_X010 = r32_AMOUNT_X010;
		}

		public BigDecimal getR35_AMOUNT_X010() {
			return R35_AMOUNT_X010;
		}

		public void setR35_AMOUNT_X010(BigDecimal r35_AMOUNT_X010) {
			R35_AMOUNT_X010 = r35_AMOUNT_X010;
		}

		public BigDecimal getR36_AMOUNT_X010() {
			return R36_AMOUNT_X010;
		}

		public void setR36_AMOUNT_X010(BigDecimal r36_AMOUNT_X010) {
			R36_AMOUNT_X010 = r36_AMOUNT_X010;
		}

		public BigDecimal getR37_AMOUNT_X010() {
			return R37_AMOUNT_X010;
		}

		public void setR37_AMOUNT_X010(BigDecimal r37_AMOUNT_X010) {
			R37_AMOUNT_X010 = r37_AMOUNT_X010;
		}

		public BigDecimal getR41_AMOUNT_X010() {
			return R41_AMOUNT_X010;
		}

		public void setR41_AMOUNT_X010(BigDecimal r41_AMOUNT_X010) {
			R41_AMOUNT_X010 = r41_AMOUNT_X010;
		}

		public BigDecimal getR42_AMOUNT_X010() {
			return R42_AMOUNT_X010;
		}

		public void setR42_AMOUNT_X010(BigDecimal r42_AMOUNT_X010) {
			R42_AMOUNT_X010 = r42_AMOUNT_X010;
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

	public class DBS10_FINCON_II_1A_Detail_Entity {
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

	class DBS10_FINCON_II_1ARowDetailMapper implements RowMapper<DBS10_FINCON_II_1A_Detail_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Detail_Entity obj = new DBS10_FINCON_II_1A_Detail_Entity();
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

	class DBS10_FINCON_II_1ARowArchivalDetailMapper implements RowMapper<DBS10_FINCON_II_1A_Archival_Detail_Entity> {

		@Override
		public DBS10_FINCON_II_1A_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_II_1A_Archival_Detail_Entity obj = new DBS10_FINCON_II_1A_Archival_Detail_Entity();
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

	public class DBS10_FINCON_II_1A_Archival_Detail_Entity {
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

	public ModelAndView getDBS10_FINCON_II_1AView(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("DBS10_FINCON_II_1A View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<DBS10_FINCON_II_1A_Archival_Summary_Entity> T1Master = new ArrayList<>();

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
			List<DBS10_FINCON_II_1A_Summary_Entity> T1Master = new ArrayList<>();
			List<DBS10_FINCON_II_1A_Manual_Summary_Entity> T2Master = new ArrayList<>();
			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY NORMAL
				T1Master = getDataByDate(dt);
				T2Master = getDataByDateManual(dt);
				System.out.println("Summary size = " + T1Master.size());
				System.out.println("Manual Summary size = " + T2Master.size());
				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// VIEW SETTINGS

		mv.setViewName("BRRS/DBS10_FINCON_II_1A");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getDBS10_FINCON_II_1AcurrentDtl(String reportId, String fromdate, String todate,
			String currency, String dtltype, Pageable pageable, String filter, String type, String version) {

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

				List<DBS10_FINCON_II_1A_Archival_Detail_Entity> detailList;

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

				List<DBS10_FINCON_II_1A_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/DBS10_FINCON_II_1A");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getDBS10_FINCON_II_1AArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<DBS10_FINCON_II_1A_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (DBS10_FINCON_II_1A_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				DBS10_FINCON_II_1A_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  DBS10_FINCON_II_1A  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/DBS10_FINCON_II_1A");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				DBS10_FINCON_II_1A_Detail_Entity DBS10_FINCON_II_1AEntity = findBySnoArch(SNO);
				if (DBS10_FINCON_II_1AEntity != null && DBS10_FINCON_II_1AEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(DBS10_FINCON_II_1AEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("DBS10_FINCON_II_1AData", DBS10_FINCON_II_1AEntity);
			} else {
				DBS10_FINCON_II_1A_Detail_Entity DBS10_FINCON_II_1AEntity = findBySno(SNO);
				if (DBS10_FINCON_II_1AEntity != null && DBS10_FINCON_II_1AEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(DBS10_FINCON_II_1AEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("DBS10_FINCON_II_1AData", DBS10_FINCON_II_1AEntity);
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
			DBS10_FINCON_II_1A_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			DBS10_FINCON_II_1A_Detail_Entity oldcopy = new DBS10_FINCON_II_1A_Detail_Entity();
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
					sql = "UPDATE BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?, " + // ✅ comma added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_DBS10_FINCON_II_1A_DETAILTABLE " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?, " + // ✅
							// comma
							// added
							"AVERAGE = ? " + // ✅ proper concatenation
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), existing.getAverage(),
						Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "DBS10_FINCON_II_1A Archival Screen",
							"BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "DBS10_FINCON_II_1A Screen",
							"BRRS_DBS10_FINCON_II_1A_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_DBS10_FINCON_II_1A_Procudure(reportDateStr, type, entry);

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
			Run_DBS10_FINCON_II_1A_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_DBS10_FINCON_II_1A_Procudure(String reportDateStr, String type, String entry) {

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
					// Convert String date to SQL Date once
					java.sql.Date sqlDate = new java.sql.Date(
							new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate).getTime());

					System.out.println("formattedDate = " + formattedDate);
					System.out.println("sqlDate = " + sqlDate);
					if (isResubNoEntry) {
						String bdsql = "DELETE FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, sqlDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_DBS10_FINCON_II_1A_DETAILTABLE "
								+ " (SNO,ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA,REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_NAME, REPORT_DATE,DATA_ENTRY_VERSION, REPORT_REMARKS,ENTITY_FLG,MODIFY_FLG,DEL_FLG) "
								+ "SELECT SNO,ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA,REPORT_LABEL, REPORT_ADDL_CRITERIA_1,REPORT_NAME, REPORT_DATE,DATA_ENTRY_VERSION, REPORT_REMARKS,ENTITY_FLG,MODIFY_FLG,DEL_FLG "
								+ "FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, sqlDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_DBS10_FINCON_II_1A_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_DBS10_FINCON_II_1A_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, sqlDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, sqlDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_DBS10_FINCON_II_1A_ARCHIVALTABLE_SUMMARY ( "
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
								+ "FROM BRRS_DBS10_FINCON_II_1A_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, sqlDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_DBS10_FINCON_II_1A_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, sqlDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] getDBS10_FINCON_II_1ADetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  DBS10_FINCON_II_1A Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_II_1ADetailsDetail");

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
			List<DBS10_FINCON_II_1A_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_II_1A_Detail_Entity item : reportData) {
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
				logger.info("No data found for DBS10_FINCON_II_1A — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_II_1A Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for DBS10_FINCON_II_1A ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_II_1A Detail NEW");

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
			List<DBS10_FINCON_II_1A_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_II_1A_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for DBS10_FINCON_II_1A — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_II_1A NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDBS10_FINCON_II_1AExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.CommonDisclosure");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelDBS10_FINCON_II_1AARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// Fetch data

		List<DBS10_FINCON_II_1A_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		List<DBS10_FINCON_II_1A_Manual_Summary_Entity> dataList1 = getDataByDateManual(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  DBS10_FINCON_II_1A report. Returning empty result.");
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
					DBS10_FINCON_II_1A_Summary_Entity record = dataList.get(i);
					DBS10_FINCON_II_1A_Manual_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R5
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR5_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR5_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R6
					// Column D
					row = sheet.getRow(5);

					cell3 = row.getCell(3);
					if (record.getR6_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR6_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R7
					// Column D
					row = sheet.getRow(6);

					cell3 = row.getCell(3);
					if (record.getR7_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR7_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R8
					// Column D
					row = sheet.getRow(7);

					cell3 = row.getCell(3);
					if (record1.getR8_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR8_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R9
					// Column D
					row = sheet.getRow(8);

					cell3 = row.getCell(3);
					if (record.getR9_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR9_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R10
					// Column D
					row = sheet.getRow(9);

					cell3 = row.getCell(3);
					if (record.getR10_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR10_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R11
					// Column D
					row = sheet.getRow(10);

					cell3 = row.getCell(3);
					if (record.getR11_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR11_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R12
					// Column D
					row = sheet.getRow(11);

					cell3 = row.getCell(3);
					if (record1.getR12_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR12_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R14
					// Column D
					row = sheet.getRow(13);

					cell3 = row.getCell(3);
					if (record1.getR14_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR14_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R15
					// Column D
					row = sheet.getRow(14);

					cell3 = row.getCell(3);
					if (record.getR15_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR15_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R16
					// Column D
					row = sheet.getRow(15);

					cell3 = row.getCell(3);
					if (record.getR16_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR16_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R17
					// Column D
					row = sheet.getRow(16);

					cell3 = row.getCell(3);
					if (record.getR17_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR17_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R18
					// Column D
					row = sheet.getRow(17);

					cell3 = row.getCell(3);
					if (record.getR18_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR18_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R19
					// Column D
					row = sheet.getRow(18);

					cell3 = row.getCell(3);
					if (record.getR19_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR19_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R20
					// Column D
					row = sheet.getRow(19);

					cell3 = row.getCell(3);
					if (record.getR20_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR20_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R21
					// Column D
					row = sheet.getRow(20);

					cell3 = row.getCell(3);
					if (record.getR21_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR21_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R22
					// Column D
					row = sheet.getRow(21);

					cell3 = row.getCell(3);
					if (record1.getR22_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR22_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R23
					// Column D
					row = sheet.getRow(22);

					cell3 = row.getCell(3);
					if (record1.getR23_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR23_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R24
					// Column D
					row = sheet.getRow(23);

					cell3 = row.getCell(3);
					if (record.getR24_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR24_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R25
					// Column D
					row = sheet.getRow(24);

					cell3 = row.getCell(3);
					if (record1.getR25_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR25_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R26
					// Column D
					row = sheet.getRow(25);

					cell3 = row.getCell(3);
					if (record1.getR26_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR26_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R27
					// Column D
					row = sheet.getRow(26);

					cell3 = row.getCell(3);
					if (record1.getR27_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR27_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R28
					// Column D
					row = sheet.getRow(27);

					cell3 = row.getCell(3);
					if (record1.getR28_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR28_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R31
					// Column D
					row = sheet.getRow(30);

					cell3 = row.getCell(3);
					if (record1.getR31_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR31_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R32
					// Column D
					row = sheet.getRow(31);

					cell3 = row.getCell(3);
					if (record1.getR32_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR32_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R34
					// Column D
					row = sheet.getRow(33);

					cell3 = row.getCell(3);
					if (record.getR34_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR34_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R35
					// Column D
					row = sheet.getRow(34);

					cell3 = row.getCell(3);
					if (record1.getR35_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR35_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R36
					// Column D
					row = sheet.getRow(35);

					cell3 = row.getCell(3);
					if (record1.getR36_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR36_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R37
					// Column D
					row = sheet.getRow(36);

					cell3 = row.getCell(3);
					if (record1.getR37_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR37_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R38
					// Column D
					row = sheet.getRow(37);

					cell3 = row.getCell(3);
					if (record.getR38_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR38_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R39
					// Column D
					row = sheet.getRow(38);

					cell3 = row.getCell(3);
					if (record.getR39_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR39_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R40
					// Column D
					row = sheet.getRow(39);

					cell3 = row.getCell(3);
					if (record.getR40_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR40_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R41
					// Column D
					row = sheet.getRow(40);

					cell3 = row.getCell(3);
					if (record1.getR41_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR41_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R42
					// Column D
					row = sheet.getRow(41);

					cell3 = row.getCell(3);
					if (record1.getR42_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR42_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

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

	public byte[] getExcelDBS10_FINCON_II_1AARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<DBS10_FINCON_II_1A_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);
		List<DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity> dataList1 = getdatabydateListarchivalManual(dateformat.parse(todate),
				version);
		
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for DBS10_FINCON_II_1A new report. Returning empty result.");
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
					DBS10_FINCON_II_1A_Archival_Summary_Entity record = dataList.get(i);
					DBS10_FINCON_II_1A_Manual_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R5
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR5_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR5_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R6
					// Column D
					row = sheet.getRow(5);

					cell3 = row.getCell(3);
					if (record.getR6_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR6_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R7
					// Column D
					row = sheet.getRow(6);

					cell3 = row.getCell(3);
					if (record.getR7_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR7_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R8
					// Column D
					row = sheet.getRow(7);

					cell3 = row.getCell(3);
					if (record1.getR8_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR8_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R9
					// Column D
					row = sheet.getRow(8);

					cell3 = row.getCell(3);
					if (record.getR9_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR9_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R10
					// Column D
					row = sheet.getRow(9);

					cell3 = row.getCell(3);
					if (record.getR10_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR10_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R11
					// Column D
					row = sheet.getRow(10);

					cell3 = row.getCell(3);
					if (record.getR11_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR11_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R12
					// Column D
					row = sheet.getRow(11);

					cell3 = row.getCell(3);
					if (record1.getR12_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR12_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R14
					// Column D
					row = sheet.getRow(13);

					cell3 = row.getCell(3);
					if (record1.getR14_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR14_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R15
					// Column D
					row = sheet.getRow(14);

					cell3 = row.getCell(3);
					if (record.getR15_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR15_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R16
					// Column D
					row = sheet.getRow(15);

					cell3 = row.getCell(3);
					if (record.getR16_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR16_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R17
					// Column D
					row = sheet.getRow(16);

					cell3 = row.getCell(3);
					if (record.getR17_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR17_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R18
					// Column D
					row = sheet.getRow(17);

					cell3 = row.getCell(3);
					if (record.getR18_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR18_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R19
					// Column D
					row = sheet.getRow(18);

					cell3 = row.getCell(3);
					if (record.getR19_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR19_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R20
					// Column D
					row = sheet.getRow(19);

					cell3 = row.getCell(3);
					if (record.getR20_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR20_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R21
					// Column D
					row = sheet.getRow(20);

					cell3 = row.getCell(3);
					if (record.getR21_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR21_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R22
					// Column D
					row = sheet.getRow(21);

					cell3 = row.getCell(3);
					if (record1.getR22_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR22_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R23
					// Column D
					row = sheet.getRow(22);

					cell3 = row.getCell(3);
					if (record1.getR23_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR23_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R24
					// Column D
					row = sheet.getRow(23);

					cell3 = row.getCell(3);
					if (record.getR24_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR24_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R25
					// Column D
					row = sheet.getRow(24);

					cell3 = row.getCell(3);
					if (record1.getR25_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR25_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R26
					// Column D
					row = sheet.getRow(25);

					cell3 = row.getCell(3);
					if (record1.getR26_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR26_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R27
					// Column D
					row = sheet.getRow(26);

					cell3 = row.getCell(3);
					if (record1.getR27_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR27_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R28
					// Column D
					row = sheet.getRow(27);

					cell3 = row.getCell(3);
					if (record1.getR28_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR28_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R31
					// Column D
					row = sheet.getRow(30);

					cell3 = row.getCell(3);
					if (record1.getR31_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR31_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R32
					// Column D
					row = sheet.getRow(31);

					cell3 = row.getCell(3);
					if (record1.getR32_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR32_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R34
					// Column D
					row = sheet.getRow(33);

					cell3 = row.getCell(3);
					if (record.getR34_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR34_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R35
					// Column D
					row = sheet.getRow(34);

					cell3 = row.getCell(3);
					if (record1.getR35_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR35_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R36
					// Column D
					row = sheet.getRow(35);

					cell3 = row.getCell(3);
					if (record1.getR36_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR36_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R37
					// Column D
					row = sheet.getRow(36);

					cell3 = row.getCell(3);
					if (record1.getR37_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR37_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R38
					// Column D
					row = sheet.getRow(37);

					cell3 = row.getCell(3);
					if (record.getR38_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR38_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R39
					// Column D
					row = sheet.getRow(38);

					cell3 = row.getCell(3);
					if (record.getR39_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR39_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R40
					// Column D
					row = sheet.getRow(39);

					cell3 = row.getCell(3);
					if (record.getR40_AMOUNT_X010() != null) {
						cell3.setCellValue(record.getR40_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R41
					// Column D
					row = sheet.getRow(40);

					cell3 = row.getCell(3);
					if (record1.getR41_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR41_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// R42
					// Column D
					row = sheet.getRow(41);

					cell3 = row.getCell(3);
					if (record1.getR42_AMOUNT_X010() != null) {
						cell3.setCellValue(record1.getR42_AMOUNT_X010().doubleValue());

					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

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

	// Resubmission
	public List<Object[]> getDBS10_FINCON_II_1AResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<DBS10_FINCON_II_1A_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (DBS10_FINCON_II_1A_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				DBS10_FINCON_II_1A_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  DBS10_FINCON_II_1A  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

	@Transactional
	public void updateReport(Object entity, String type) {

		boolean isResub = "RESUB".equalsIgnoreCase(type);

		System.out.println("Came to DBS10_FINCON_II_1A Manual Update. Type: " + (isResub ? "RESUB" : "NORMAL"));

		String tableName = isResub ? "BRRS_DBS10_FINCON_II_1A_MANUAL_ARCHIVALTABLE_SUMMARY"
				: "BRRS_DBS10_FINCON_II_1A_MANUAL_SUMMARYTABLE";

		int[] rows = {8,12,14,22,23,25,26,27,28,31,32,35,36,37,41,42};

		try {
			// Use the actual runtime class
			Class<?> entityClass = entity.getClass();

			// Get report date
			Method getDateMethod = entityClass.getMethod("getREPORT_DATE");
			Object reportDateObj = getDateMethod.invoke(entity);

			if (reportDateObj == null) {
				throw new RuntimeException("Report Date is NULL");
			}

			Date reportDate = (Date) reportDateObj;

			System.out.println("Report Date : " + reportDate);
			System.out.println("Entity Class : " + entityClass.getName());

			// =====================================================
			// 🔹 AUDIT TRAIL SETUP (DYNAMIC LOG PAYLOAD PREPARATION)
			// =====================================================
			StringBuilder changesBuilder = new StringBuilder();
			java.sql.Date sqlReportDate = new java.sql.Date(reportDate.getTime());

			for (int r : rows) {

				String[] cols = { "AMOUNT_X010"};

				for (String col : cols) {

					String getterName = "getR" + r + "_" + col;
					String columnName = "R" + r + "_" + col;

					try {
						Method getter = entityClass.getMethod(getterName);
						Object newValueObj = getter.invoke(entity);

						System.out.println("Processing -> " + getterName + " = " + newValueObj);

						// Skip processing if the web input value completely lacks data
						if (newValueObj == null) {
							continue;
						}

						// 1. Fetch current value directly from the targeted DB Table before updating
						String selectSql = "SELECT " + columnName + " FROM " + tableName + " WHERE REPORT_DATE = ?";
						Object dbValueObj = null;
						try {
							dbValueObj = jdbcTemplate.queryForObject(selectSql, Object.class, sqlReportDate);
						} catch (Exception e) {
							// Handle if row doesn't exist yet gracefully
							dbValueObj = null;
						}

						// 2. Normalize comparison strings to prevent audit bloat
						String currentValStr = (dbValueObj == null) ? "" : dbValueObj.toString().trim();
						String newValStr = newValueObj.toString().trim();

						// Skip update if value hasn't actually changed
						if (currentValStr.equals(newValStr)) {
							continue;
						}

						// 3. Track changes manually for JDBC tracking
						if (changesBuilder.length() > 0) {
							changesBuilder.append("|||");
						}
						changesBuilder.append(columnName.toUpperCase()).append(": OldValue: ")
								.append(currentValStr.isEmpty() ? "null" : currentValStr).append(", NewValue: ")
								.append(newValStr);

						// 4. Perform live database update
						String updateSql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE REPORT_DATE = ?";
						int count = jdbcTemplate.update(updateSql, newValueObj, sqlReportDate);

						System.out.println("Updated Column : " + columnName + " Rows Affected : " + count);

					} catch (NoSuchMethodException ex) {
						System.out.println("Method not found : " + getterName + " - Skipping");
					}
				}
			}

			// =====================================================
			// 🔹 EXECUTE MANUAL AUDIT LOG INSERTION
			// =====================================================
			String changes = changesBuilder.toString();
			System.out.println("DBS10_FINCON_II_1A Manual Changes Length = " + changes.length());

			if (!changes.isEmpty()) {
				// Enforce character protection thresholds against database column bounds
				if (changes.length() > 1900) {
					changes = changes.substring(0, 1900);
				}

				// Call custom manual audit execution to save directly into your Audit table
				auditService.compareEntitiesmanual(entity, // Old copy placeholder (We pass entity to satisfy signature)
						entity, // Existing copy placeholder
						reportDate.toString(), "DBS10_FINCON_II_1A Manual Screen", tableName);

				// Optional: If your audit trail system relies strictly on a pre-generated
				// string instead of recalculating, you can write a short jdbcTemplate insert
				// here
				// to insert the `changes` string directly into "BRRS_AUDIT"."MODI_DETAILS".
			}

			System.out.println("DBS10_FINCON_II_1A Manual Update Completed Successfully for Type : " + type);

		} catch (Exception e) {
			System.err.println("===== DBS10_FINCON_II_1A UPDATE ERROR =====");
			e.printStackTrace();

			Throwable root = e;
			while (root.getCause() != null) {
				root = root.getCause();
			}

			System.err.println("ROOT CAUSE : " + root.getMessage());

			throw new RuntimeException("Error while updating DBS10_FINCON_II_1A Manual fields for type: " + type, e);
		}
	}

}