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
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;
import com.bornfire.brrs.services.BRRS_Q_SMME_Intrest_Income_ReportService.Q_SMME_Intrest_Income_Archival_Summary_Entity;

@Service
@Transactional
public class BRRS_Q_SMME_loans_Advances_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_SMME_loans_Advances_ReportService.class);

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

	@Autowired
	UserProfileRep userProfileRep;

	// Fetch data by report date
	public List<Q_SMME_loans_Advances_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_SMME_loans_AdvancesRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getQ_SMME_loans_AdvancesArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Q_SMME_loans_Advances_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new Q_SMME_loans_AdvancesArchivalRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
//GET ALL WITH VERSION

	public List<Q_SMME_loans_Advances_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Q_SMME_loans_AdvancesArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Q_SMME_loans_Advances_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new Q_SMME_loans_AdvancesDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Q_SMME_loans_Advances_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Q_SMME_loans_AdvancesDetailRowMapper());
	}

// 3. PAGINATION

	public List<Q_SMME_loans_Advances_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new Q_SMME_loans_AdvancesDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Q_SMME_loans_Advances_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Q_SMME_loans_AdvancesDetailRowMapper());
	}

	public Q_SMME_loans_Advances_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_SMME_loans_AdvancesDetailRowMapper());
	}

	public Q_SMME_loans_Advances_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_SMME_loans_AdvancesDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<Q_SMME_loans_Advances_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Q_SMME_loans_AdvancesArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<Q_SMME_loans_Advances_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Q_SMME_loans_AdvancesArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class Q_SMME_loans_AdvancesRowMapper implements RowMapper<Q_SMME_loans_Advances_Summary_Entity> {

		@Override
		public Q_SMME_loans_Advances_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_loans_Advances_Summary_Entity obj = new Q_SMME_loans_Advances_Summary_Entity();

			obj.setR15Caoa(rs.getString("R15_CAOA"));
			obj.setR15ResAmt(rs.getBigDecimal("R15_RES_AMT"));
			obj.setR15NonResAmt(rs.getBigDecimal("R15_NON_RES_AMT"));

			obj.setR16Caoa(rs.getString("R16_CAOA"));
			obj.setR16ResAmt(rs.getBigDecimal("R16_RES_AMT"));
			obj.setR16NonResAmt(rs.getBigDecimal("R16_NON_RES_AMT"));

			obj.setR17Caoa(rs.getString("R17_CAOA"));
			obj.setR17ResAmt(rs.getBigDecimal("R17_RES_AMT"));
			obj.setR17NonResAmt(rs.getBigDecimal("R17_NON_RES_AMT"));

			obj.setR18Caoa(rs.getString("R18_CAOA"));
			obj.setR18ResAmt(rs.getBigDecimal("R18_RES_AMT"));
			obj.setR18NonResAmt(rs.getBigDecimal("R18_NON_RES_AMT"));

			obj.setR19Caoa(rs.getString("R19_CAOA"));
			obj.setR19ResAmt(rs.getBigDecimal("R19_RES_AMT"));
			obj.setR19NonResAmt(rs.getBigDecimal("R19_NON_RES_AMT"));

			obj.setR20Caoa(rs.getString("R20_CAOA"));
			obj.setR20ResAmt(rs.getBigDecimal("R20_RES_AMT"));
			obj.setR20NonResAmt(rs.getBigDecimal("R20_NON_RES_AMT"));

			obj.setR21Caoa(rs.getString("R21_CAOA"));
			obj.setR21ResAmt(rs.getBigDecimal("R21_RES_AMT"));
			obj.setR21NonResAmt(rs.getBigDecimal("R21_NON_RES_AMT"));

			obj.setR22Caoa(rs.getString("R22_CAOA"));
			obj.setR22ResAmt(rs.getBigDecimal("R22_RES_AMT"));
			obj.setR22NonResAmt(rs.getBigDecimal("R22_NON_RES_AMT"));

			obj.setR23Caoa(rs.getString("R23_CAOA"));
			obj.setR23ResAmt(rs.getBigDecimal("R23_RES_AMT"));
			obj.setR23NonResAmt(rs.getBigDecimal("R23_NON_RES_AMT"));

			obj.setR24Caoa(rs.getString("R24_CAOA"));
			obj.setR24ResAmt(rs.getBigDecimal("R24_RES_AMT"));
			obj.setR24NonResAmt(rs.getBigDecimal("R24_NON_RES_AMT"));

			obj.setR25Caoa(rs.getString("R25_CAOA"));
			obj.setR25ResAmt(rs.getBigDecimal("R25_RES_AMT"));
			obj.setR25NonResAmt(rs.getBigDecimal("R25_NON_RES_AMT"));

			obj.setR26Caoa(rs.getString("R26_CAOA"));
			obj.setR26ResAmt(rs.getBigDecimal("R26_RES_AMT"));
			obj.setR26NonResAmt(rs.getBigDecimal("R26_NON_RES_AMT"));

			obj.setR27Caoa(rs.getString("R27_CAOA"));
			obj.setR27ResAmt(rs.getBigDecimal("R27_RES_AMT"));
			obj.setR27NonResAmt(rs.getBigDecimal("R27_NON_RES_AMT"));

			obj.setR28Caoa(rs.getString("R28_CAOA"));
			obj.setR28ResAmt(rs.getBigDecimal("R28_RES_AMT"));
			obj.setR28NonResAmt(rs.getBigDecimal("R28_NON_RES_AMT"));

			obj.setR29Caoa(rs.getString("R29_CAOA"));
			obj.setR29ResAmt(rs.getBigDecimal("R29_RES_AMT"));
			obj.setR29NonResAmt(rs.getBigDecimal("R29_NON_RES_AMT"));

			obj.setR30Caoa(rs.getString("R30_CAOA"));
			obj.setR30ResAmt(rs.getBigDecimal("R30_RES_AMT"));
			obj.setR30NonResAmt(rs.getBigDecimal("R30_NON_RES_AMT"));

			obj.setR31Caoa(rs.getString("R31_CAOA"));
			obj.setR31ResAmt(rs.getBigDecimal("R31_RES_AMT"));
			obj.setR31NonResAmt(rs.getBigDecimal("R31_NON_RES_AMT"));

			obj.setR32Caoa(rs.getString("R32_CAOA"));
			obj.setR32ResAmt(rs.getBigDecimal("R32_RES_AMT"));
			obj.setR32NonResAmt(rs.getBigDecimal("R32_NON_RES_AMT"));

			obj.setR33Caoa(rs.getString("R33_CAOA"));
			obj.setR33ResAmt(rs.getBigDecimal("R33_RES_AMT"));
			obj.setR33NonResAmt(rs.getBigDecimal("R33_NON_RES_AMT"));

			obj.setR34Caoa(rs.getString("R34_CAOA"));
			obj.setR34ResAmt(rs.getBigDecimal("R34_RES_AMT"));
			obj.setR34NonResAmt(rs.getBigDecimal("R34_NON_RES_AMT"));

			obj.setR35Caoa(rs.getString("R35_CAOA"));
			obj.setR35ResAmt(rs.getBigDecimal("R35_RES_AMT"));
			obj.setR35NonResAmt(rs.getBigDecimal("R35_NON_RES_AMT"));

			obj.setR36Caoa(rs.getString("R36_CAOA"));
			obj.setR36ResAmt(rs.getBigDecimal("R36_RES_AMT"));
			obj.setR36NonResAmt(rs.getBigDecimal("R36_NON_RES_AMT"));

			obj.setR37Caoa(rs.getString("R37_CAOA"));
			obj.setR37ResAmt(rs.getBigDecimal("R37_RES_AMT"));
			obj.setR37NonResAmt(rs.getBigDecimal("R37_NON_RES_AMT"));

			obj.setR38Caoa(rs.getString("R38_CAOA"));
			obj.setR38ResAmt(rs.getBigDecimal("R38_RES_AMT"));
			obj.setR38NonResAmt(rs.getBigDecimal("R38_NON_RES_AMT"));

			obj.setR39Caoa(rs.getString("R39_CAOA"));
			obj.setR39ResAmt(rs.getBigDecimal("R39_RES_AMT"));
			obj.setR39NonResAmt(rs.getBigDecimal("R39_NON_RES_AMT"));

			obj.setR40Caoa(rs.getString("R40_CAOA"));
			obj.setR40ResAmt(rs.getBigDecimal("R40_RES_AMT"));
			obj.setR40NonResAmt(rs.getBigDecimal("R40_NON_RES_AMT"));

			obj.setR41Caoa(rs.getString("R41_CAOA"));
			obj.setR41ResAmt(rs.getBigDecimal("R41_RES_AMT"));
			obj.setR41NonResAmt(rs.getBigDecimal("R41_NON_RES_AMT"));

			obj.setR42Caoa(rs.getString("R42_CAOA"));
			obj.setR42ResAmt(rs.getBigDecimal("R42_RES_AMT"));
			obj.setR42NonResAmt(rs.getBigDecimal("R42_NON_RES_AMT"));

			obj.setR43Caoa(rs.getString("R43_CAOA"));
			obj.setR43ResAmt(rs.getBigDecimal("R43_RES_AMT"));
			obj.setR43NonResAmt(rs.getBigDecimal("R43_NON_RES_AMT"));

			obj.setR44Caoa(rs.getString("R44_CAOA"));
			obj.setR44ResAmt(rs.getBigDecimal("R44_RES_AMT"));
			obj.setR44NonResAmt(rs.getBigDecimal("R44_NON_RES_AMT"));

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

	public static class Q_SMME_loans_Advances_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

// === R15 ===
		@Column(name = "R15_CAOA", length = 100)
		private String r15Caoa;

		@Column(name = "R15_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r15ResAmt;

		@Column(name = "R15_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r15NonResAmt;

		// === R16 ===
		@Column(name = "R16_CAOA", length = 100)
		private String r16Caoa;

		@Column(name = "R16_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r16ResAmt;

		@Column(name = "R16_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r16NonResAmt;

		// === R17 ===
		@Column(name = "R17_CAOA", length = 100)
		private String r17Caoa;

		@Column(name = "R17_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r17ResAmt;

		@Column(name = "R17_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r17NonResAmt;

		// === R18 ===
		@Column(name = "R18_CAOA", length = 100)
		private String r18Caoa;

		@Column(name = "R18_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r18ResAmt;

		@Column(name = "R18_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r18NonResAmt;

		// === R19 ===
		@Column(name = "R19_CAOA", length = 100)
		private String r19Caoa;

		@Column(name = "R19_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r19ResAmt;

		@Column(name = "R19_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r19NonResAmt;

		// === R20 ===
		@Column(name = "R20_CAOA", length = 100)
		private String r20Caoa;

		@Column(name = "R20_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r20ResAmt;

		@Column(name = "R20_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r20NonResAmt;

		// === R21 ===
		@Column(name = "R21_CAOA", length = 100)
		private String r21Caoa;

		@Column(name = "R21_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r21ResAmt;

		@Column(name = "R21_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r21NonResAmt;

		// === R22 ===
		@Column(name = "R22_CAOA", length = 100)
		private String r22Caoa;

		@Column(name = "R22_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r22ResAmt;

		@Column(name = "R22_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r22NonResAmt;

		// === R23 ===
		@Column(name = "R23_CAOA", length = 100)
		private String r23Caoa;

		@Column(name = "R23_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r23ResAmt;

		@Column(name = "R23_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r23NonResAmt;

		// === R24 ===
		@Column(name = "R24_CAOA", length = 100)
		private String r24Caoa;

		@Column(name = "R24_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r24ResAmt;

		@Column(name = "R24_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r24NonResAmt;

		// === R25 ===
		@Column(name = "R25_CAOA", length = 100)
		private String r25Caoa;

		@Column(name = "R25_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r25ResAmt;

		@Column(name = "R25_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r25NonResAmt;

		// === R26 ===
		@Column(name = "R26_CAOA", length = 100)
		private String r26Caoa;

		@Column(name = "R26_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r26ResAmt;

		@Column(name = "R26_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r26NonResAmt;

		// === R27 ===
		@Column(name = "R27_CAOA", length = 100)
		private String r27Caoa;

		@Column(name = "R27_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r27ResAmt;

		@Column(name = "R27_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r27NonResAmt;

		// === R28 ===
		@Column(name = "R28_CAOA", length = 100)
		private String r28Caoa;

		@Column(name = "R28_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r28ResAmt;

		@Column(name = "R28_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r28NonResAmt;

		// === R29 ===
		@Column(name = "R29_CAOA", length = 100)
		private String r29Caoa;

		@Column(name = "R29_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r29ResAmt;

		@Column(name = "R29_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r29NonResAmt;

		// === R30 ===
		@Column(name = "R30_CAOA", length = 100)
		private String r30Caoa;

		@Column(name = "R30_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r30ResAmt;

		@Column(name = "R30_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r30NonResAmt;

		// === R31 ===
		@Column(name = "R31_CAOA", length = 100)
		private String r31Caoa;

		@Column(name = "R31_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r31ResAmt;

		@Column(name = "R31_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r31NonResAmt;

		// === R32 ===
		@Column(name = "R32_CAOA", length = 100)
		private String r32Caoa;

		@Column(name = "R32_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r32ResAmt;

		@Column(name = "R32_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r32NonResAmt;

		// === R33 ===
		@Column(name = "R33_CAOA", length = 100)
		private String r33Caoa;

		@Column(name = "R33_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r33ResAmt;

		@Column(name = "R33_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r33NonResAmt;

		// === R34 ===
		@Column(name = "R34_CAOA", length = 100)
		private String r34Caoa;

		@Column(name = "R34_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r34ResAmt;

		@Column(name = "R34_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r34NonResAmt;

		// === R35 ===
		@Column(name = "R35_CAOA", length = 100)
		private String r35Caoa;

		@Column(name = "R35_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r35ResAmt;

		@Column(name = "R35_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r35NonResAmt;

		// === R36 ===
		@Column(name = "R36_CAOA", length = 100)
		private String r36Caoa;

		@Column(name = "R36_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r36ResAmt;

		@Column(name = "R36_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r36NonResAmt;

		// === R37 ===
		@Column(name = "R37_CAOA", length = 100)
		private String r37Caoa;

		@Column(name = "R37_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r37ResAmt;

		@Column(name = "R37_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r37NonResAmt;

		// === R38 ===
		@Column(name = "R38_CAOA", length = 100)
		private String r38Caoa;

		@Column(name = "R38_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r38ResAmt;

		@Column(name = "R38_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r38NonResAmt;

		// === R39 ===
		@Column(name = "R39_CAOA", length = 100)
		private String r39Caoa;

		@Column(name = "R39_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r39ResAmt;

		@Column(name = "R39_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r39NonResAmt;

		// === R40 ===
		@Column(name = "R40_CAOA", length = 100)
		private String r40Caoa;

		@Column(name = "R40_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r40ResAmt;

		@Column(name = "R40_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r40NonResAmt;

		// === R41 ===
		@Column(name = "R41_CAOA", length = 100)
		private String r41Caoa;

		@Column(name = "R41_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r41ResAmt;

		@Column(name = "R41_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r41NonResAmt;

		// === R42 ===
		@Column(name = "R42_CAOA", length = 100)
		private String r42Caoa;

		@Column(name = "R42_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r42ResAmt;

		@Column(name = "R42_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r42NonResAmt;

		// === R43 ===
		@Column(name = "R43_CAOA", length = 100)
		private String r43Caoa;

		@Column(name = "R43_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r43ResAmt;

		@Column(name = "R43_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r43NonResAmt;

		// === R44 ===
		@Column(name = "R44_CAOA", length = 100)
		private String r44Caoa;

		@Column(name = "R44_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r44ResAmt;

		@Column(name = "R44_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r44NonResAmt;

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

		public String getR15Caoa() {
			return r15Caoa;
		}

		public void setR15Caoa(String r15Caoa) {
			this.r15Caoa = r15Caoa;
		}

		public BigDecimal getR15ResAmt() {
			return r15ResAmt;
		}

		public void setR15ResAmt(BigDecimal r15ResAmt) {
			this.r15ResAmt = r15ResAmt;
		}

		public BigDecimal getR15NonResAmt() {
			return r15NonResAmt;
		}

		public void setR15NonResAmt(BigDecimal r15NonResAmt) {
			this.r15NonResAmt = r15NonResAmt;
		}

		public String getR16Caoa() {
			return r16Caoa;
		}

		public void setR16Caoa(String r16Caoa) {
			this.r16Caoa = r16Caoa;
		}

		public BigDecimal getR16ResAmt() {
			return r16ResAmt;
		}

		public void setR16ResAmt(BigDecimal r16ResAmt) {
			this.r16ResAmt = r16ResAmt;
		}

		public BigDecimal getR16NonResAmt() {
			return r16NonResAmt;
		}

		public void setR16NonResAmt(BigDecimal r16NonResAmt) {
			this.r16NonResAmt = r16NonResAmt;
		}

		public String getR17Caoa() {
			return r17Caoa;
		}

		public void setR17Caoa(String r17Caoa) {
			this.r17Caoa = r17Caoa;
		}

		public BigDecimal getR17ResAmt() {
			return r17ResAmt;
		}

		public void setR17ResAmt(BigDecimal r17ResAmt) {
			this.r17ResAmt = r17ResAmt;
		}

		public BigDecimal getR17NonResAmt() {
			return r17NonResAmt;
		}

		public void setR17NonResAmt(BigDecimal r17NonResAmt) {
			this.r17NonResAmt = r17NonResAmt;
		}

		public String getR18Caoa() {
			return r18Caoa;
		}

		public void setR18Caoa(String r18Caoa) {
			this.r18Caoa = r18Caoa;
		}

		public BigDecimal getR18ResAmt() {
			return r18ResAmt;
		}

		public void setR18ResAmt(BigDecimal r18ResAmt) {
			this.r18ResAmt = r18ResAmt;
		}

		public BigDecimal getR18NonResAmt() {
			return r18NonResAmt;
		}

		public void setR18NonResAmt(BigDecimal r18NonResAmt) {
			this.r18NonResAmt = r18NonResAmt;
		}

		public String getR19Caoa() {
			return r19Caoa;
		}

		public void setR19Caoa(String r19Caoa) {
			this.r19Caoa = r19Caoa;
		}

		public BigDecimal getR19ResAmt() {
			return r19ResAmt;
		}

		public void setR19ResAmt(BigDecimal r19ResAmt) {
			this.r19ResAmt = r19ResAmt;
		}

		public BigDecimal getR19NonResAmt() {
			return r19NonResAmt;
		}

		public void setR19NonResAmt(BigDecimal r19NonResAmt) {
			this.r19NonResAmt = r19NonResAmt;
		}

		public String getR20Caoa() {
			return r20Caoa;
		}

		public void setR20Caoa(String r20Caoa) {
			this.r20Caoa = r20Caoa;
		}

		public BigDecimal getR20ResAmt() {
			return r20ResAmt;
		}

		public void setR20ResAmt(BigDecimal r20ResAmt) {
			this.r20ResAmt = r20ResAmt;
		}

		public BigDecimal getR20NonResAmt() {
			return r20NonResAmt;
		}

		public void setR20NonResAmt(BigDecimal r20NonResAmt) {
			this.r20NonResAmt = r20NonResAmt;
		}

		public String getR21Caoa() {
			return r21Caoa;
		}

		public void setR21Caoa(String r21Caoa) {
			this.r21Caoa = r21Caoa;
		}

		public BigDecimal getR21ResAmt() {
			return r21ResAmt;
		}

		public void setR21ResAmt(BigDecimal r21ResAmt) {
			this.r21ResAmt = r21ResAmt;
		}

		public BigDecimal getR21NonResAmt() {
			return r21NonResAmt;
		}

		public void setR21NonResAmt(BigDecimal r21NonResAmt) {
			this.r21NonResAmt = r21NonResAmt;
		}

		public String getR22Caoa() {
			return r22Caoa;
		}

		public void setR22Caoa(String r22Caoa) {
			this.r22Caoa = r22Caoa;
		}

		public BigDecimal getR22ResAmt() {
			return r22ResAmt;
		}

		public void setR22ResAmt(BigDecimal r22ResAmt) {
			this.r22ResAmt = r22ResAmt;
		}

		public BigDecimal getR22NonResAmt() {
			return r22NonResAmt;
		}

		public void setR22NonResAmt(BigDecimal r22NonResAmt) {
			this.r22NonResAmt = r22NonResAmt;
		}

		public String getR23Caoa() {
			return r23Caoa;
		}

		public void setR23Caoa(String r23Caoa) {
			this.r23Caoa = r23Caoa;
		}

		public BigDecimal getR23ResAmt() {
			return r23ResAmt;
		}

		public void setR23ResAmt(BigDecimal r23ResAmt) {
			this.r23ResAmt = r23ResAmt;
		}

		public BigDecimal getR23NonResAmt() {
			return r23NonResAmt;
		}

		public void setR23NonResAmt(BigDecimal r23NonResAmt) {
			this.r23NonResAmt = r23NonResAmt;
		}

		public String getR24Caoa() {
			return r24Caoa;
		}

		public void setR24Caoa(String r24Caoa) {
			this.r24Caoa = r24Caoa;
		}

		public BigDecimal getR24ResAmt() {
			return r24ResAmt;
		}

		public void setR24ResAmt(BigDecimal r24ResAmt) {
			this.r24ResAmt = r24ResAmt;
		}

		public BigDecimal getR24NonResAmt() {
			return r24NonResAmt;
		}

		public void setR24NonResAmt(BigDecimal r24NonResAmt) {
			this.r24NonResAmt = r24NonResAmt;
		}

		public String getR25Caoa() {
			return r25Caoa;
		}

		public void setR25Caoa(String r25Caoa) {
			this.r25Caoa = r25Caoa;
		}

		public BigDecimal getR25ResAmt() {
			return r25ResAmt;
		}

		public void setR25ResAmt(BigDecimal r25ResAmt) {
			this.r25ResAmt = r25ResAmt;
		}

		public BigDecimal getR25NonResAmt() {
			return r25NonResAmt;
		}

		public void setR25NonResAmt(BigDecimal r25NonResAmt) {
			this.r25NonResAmt = r25NonResAmt;
		}

		public String getR26Caoa() {
			return r26Caoa;
		}

		public void setR26Caoa(String r26Caoa) {
			this.r26Caoa = r26Caoa;
		}

		public BigDecimal getR26ResAmt() {
			return r26ResAmt;
		}

		public void setR26ResAmt(BigDecimal r26ResAmt) {
			this.r26ResAmt = r26ResAmt;
		}

		public BigDecimal getR26NonResAmt() {
			return r26NonResAmt;
		}

		public void setR26NonResAmt(BigDecimal r26NonResAmt) {
			this.r26NonResAmt = r26NonResAmt;
		}

		public String getR27Caoa() {
			return r27Caoa;
		}

		public void setR27Caoa(String r27Caoa) {
			this.r27Caoa = r27Caoa;
		}

		public BigDecimal getR27ResAmt() {
			return r27ResAmt;
		}

		public void setR27ResAmt(BigDecimal r27ResAmt) {
			this.r27ResAmt = r27ResAmt;
		}

		public BigDecimal getR27NonResAmt() {
			return r27NonResAmt;
		}

		public void setR27NonResAmt(BigDecimal r27NonResAmt) {
			this.r27NonResAmt = r27NonResAmt;
		}

		public String getR28Caoa() {
			return r28Caoa;
		}

		public void setR28Caoa(String r28Caoa) {
			this.r28Caoa = r28Caoa;
		}

		public BigDecimal getR28ResAmt() {
			return r28ResAmt;
		}

		public void setR28ResAmt(BigDecimal r28ResAmt) {
			this.r28ResAmt = r28ResAmt;
		}

		public BigDecimal getR28NonResAmt() {
			return r28NonResAmt;
		}

		public void setR28NonResAmt(BigDecimal r28NonResAmt) {
			this.r28NonResAmt = r28NonResAmt;
		}

		public String getR29Caoa() {
			return r29Caoa;
		}

		public void setR29Caoa(String r29Caoa) {
			this.r29Caoa = r29Caoa;
		}

		public BigDecimal getR29ResAmt() {
			return r29ResAmt;
		}

		public void setR29ResAmt(BigDecimal r29ResAmt) {
			this.r29ResAmt = r29ResAmt;
		}

		public BigDecimal getR29NonResAmt() {
			return r29NonResAmt;
		}

		public void setR29NonResAmt(BigDecimal r29NonResAmt) {
			this.r29NonResAmt = r29NonResAmt;
		}

		public String getR30Caoa() {
			return r30Caoa;
		}

		public void setR30Caoa(String r30Caoa) {
			this.r30Caoa = r30Caoa;
		}

		public BigDecimal getR30ResAmt() {
			return r30ResAmt;
		}

		public void setR30ResAmt(BigDecimal r30ResAmt) {
			this.r30ResAmt = r30ResAmt;
		}

		public BigDecimal getR30NonResAmt() {
			return r30NonResAmt;
		}

		public void setR30NonResAmt(BigDecimal r30NonResAmt) {
			this.r30NonResAmt = r30NonResAmt;
		}

		public String getR31Caoa() {
			return r31Caoa;
		}

		public void setR31Caoa(String r31Caoa) {
			this.r31Caoa = r31Caoa;
		}

		public BigDecimal getR31ResAmt() {
			return r31ResAmt;
		}

		public void setR31ResAmt(BigDecimal r31ResAmt) {
			this.r31ResAmt = r31ResAmt;
		}

		public BigDecimal getR31NonResAmt() {
			return r31NonResAmt;
		}

		public void setR31NonResAmt(BigDecimal r31NonResAmt) {
			this.r31NonResAmt = r31NonResAmt;
		}

		public String getR32Caoa() {
			return r32Caoa;
		}

		public void setR32Caoa(String r32Caoa) {
			this.r32Caoa = r32Caoa;
		}

		public BigDecimal getR32ResAmt() {
			return r32ResAmt;
		}

		public void setR32ResAmt(BigDecimal r32ResAmt) {
			this.r32ResAmt = r32ResAmt;
		}

		public BigDecimal getR32NonResAmt() {
			return r32NonResAmt;
		}

		public void setR32NonResAmt(BigDecimal r32NonResAmt) {
			this.r32NonResAmt = r32NonResAmt;
		}

		public String getR33Caoa() {
			return r33Caoa;
		}

		public void setR33Caoa(String r33Caoa) {
			this.r33Caoa = r33Caoa;
		}

		public BigDecimal getR33ResAmt() {
			return r33ResAmt;
		}

		public void setR33ResAmt(BigDecimal r33ResAmt) {
			this.r33ResAmt = r33ResAmt;
		}

		public BigDecimal getR33NonResAmt() {
			return r33NonResAmt;
		}

		public void setR33NonResAmt(BigDecimal r33NonResAmt) {
			this.r33NonResAmt = r33NonResAmt;
		}

		public String getR34Caoa() {
			return r34Caoa;
		}

		public void setR34Caoa(String r34Caoa) {
			this.r34Caoa = r34Caoa;
		}

		public BigDecimal getR34ResAmt() {
			return r34ResAmt;
		}

		public void setR34ResAmt(BigDecimal r34ResAmt) {
			this.r34ResAmt = r34ResAmt;
		}

		public BigDecimal getR34NonResAmt() {
			return r34NonResAmt;
		}

		public void setR34NonResAmt(BigDecimal r34NonResAmt) {
			this.r34NonResAmt = r34NonResAmt;
		}

		public String getR35Caoa() {
			return r35Caoa;
		}

		public void setR35Caoa(String r35Caoa) {
			this.r35Caoa = r35Caoa;
		}

		public BigDecimal getR35ResAmt() {
			return r35ResAmt;
		}

		public void setR35ResAmt(BigDecimal r35ResAmt) {
			this.r35ResAmt = r35ResAmt;
		}

		public BigDecimal getR35NonResAmt() {
			return r35NonResAmt;
		}

		public void setR35NonResAmt(BigDecimal r35NonResAmt) {
			this.r35NonResAmt = r35NonResAmt;
		}

		public String getR36Caoa() {
			return r36Caoa;
		}

		public void setR36Caoa(String r36Caoa) {
			this.r36Caoa = r36Caoa;
		}

		public BigDecimal getR36ResAmt() {
			return r36ResAmt;
		}

		public void setR36ResAmt(BigDecimal r36ResAmt) {
			this.r36ResAmt = r36ResAmt;
		}

		public BigDecimal getR36NonResAmt() {
			return r36NonResAmt;
		}

		public void setR36NonResAmt(BigDecimal r36NonResAmt) {
			this.r36NonResAmt = r36NonResAmt;
		}

		public String getR37Caoa() {
			return r37Caoa;
		}

		public void setR37Caoa(String r37Caoa) {
			this.r37Caoa = r37Caoa;
		}

		public BigDecimal getR37ResAmt() {
			return r37ResAmt;
		}

		public void setR37ResAmt(BigDecimal r37ResAmt) {
			this.r37ResAmt = r37ResAmt;
		}

		public BigDecimal getR37NonResAmt() {
			return r37NonResAmt;
		}

		public void setR37NonResAmt(BigDecimal r37NonResAmt) {
			this.r37NonResAmt = r37NonResAmt;
		}

		public String getR38Caoa() {
			return r38Caoa;
		}

		public void setR38Caoa(String r38Caoa) {
			this.r38Caoa = r38Caoa;
		}

		public BigDecimal getR38ResAmt() {
			return r38ResAmt;
		}

		public void setR38ResAmt(BigDecimal r38ResAmt) {
			this.r38ResAmt = r38ResAmt;
		}

		public BigDecimal getR38NonResAmt() {
			return r38NonResAmt;
		}

		public void setR38NonResAmt(BigDecimal r38NonResAmt) {
			this.r38NonResAmt = r38NonResAmt;
		}

		public String getR39Caoa() {
			return r39Caoa;
		}

		public void setR39Caoa(String r39Caoa) {
			this.r39Caoa = r39Caoa;
		}

		public BigDecimal getR39ResAmt() {
			return r39ResAmt;
		}

		public void setR39ResAmt(BigDecimal r39ResAmt) {
			this.r39ResAmt = r39ResAmt;
		}

		public BigDecimal getR39NonResAmt() {
			return r39NonResAmt;
		}

		public void setR39NonResAmt(BigDecimal r39NonResAmt) {
			this.r39NonResAmt = r39NonResAmt;
		}

		public String getR40Caoa() {
			return r40Caoa;
		}

		public void setR40Caoa(String r40Caoa) {
			this.r40Caoa = r40Caoa;
		}

		public BigDecimal getR40ResAmt() {
			return r40ResAmt;
		}

		public void setR40ResAmt(BigDecimal r40ResAmt) {
			this.r40ResAmt = r40ResAmt;
		}

		public BigDecimal getR40NonResAmt() {
			return r40NonResAmt;
		}

		public void setR40NonResAmt(BigDecimal r40NonResAmt) {
			this.r40NonResAmt = r40NonResAmt;
		}

		public String getR41Caoa() {
			return r41Caoa;
		}

		public void setR41Caoa(String r41Caoa) {
			this.r41Caoa = r41Caoa;
		}

		public BigDecimal getR41ResAmt() {
			return r41ResAmt;
		}

		public void setR41ResAmt(BigDecimal r41ResAmt) {
			this.r41ResAmt = r41ResAmt;
		}

		public BigDecimal getR41NonResAmt() {
			return r41NonResAmt;
		}

		public void setR41NonResAmt(BigDecimal r41NonResAmt) {
			this.r41NonResAmt = r41NonResAmt;
		}

		public String getR42Caoa() {
			return r42Caoa;
		}

		public void setR42Caoa(String r42Caoa) {
			this.r42Caoa = r42Caoa;
		}

		public BigDecimal getR42ResAmt() {
			return r42ResAmt;
		}

		public void setR42ResAmt(BigDecimal r42ResAmt) {
			this.r42ResAmt = r42ResAmt;
		}

		public BigDecimal getR42NonResAmt() {
			return r42NonResAmt;
		}

		public void setR42NonResAmt(BigDecimal r42NonResAmt) {
			this.r42NonResAmt = r42NonResAmt;
		}

		public String getR43Caoa() {
			return r43Caoa;
		}

		public void setR43Caoa(String r43Caoa) {
			this.r43Caoa = r43Caoa;
		}

		public BigDecimal getR43ResAmt() {
			return r43ResAmt;
		}

		public void setR43ResAmt(BigDecimal r43ResAmt) {
			this.r43ResAmt = r43ResAmt;
		}

		public BigDecimal getR43NonResAmt() {
			return r43NonResAmt;
		}

		public void setR43NonResAmt(BigDecimal r43NonResAmt) {
			this.r43NonResAmt = r43NonResAmt;
		}

		public String getR44Caoa() {
			return r44Caoa;
		}

		public void setR44Caoa(String r44Caoa) {
			this.r44Caoa = r44Caoa;
		}

		public BigDecimal getR44ResAmt() {
			return r44ResAmt;
		}

		public void setR44ResAmt(BigDecimal r44ResAmt) {
			this.r44ResAmt = r44ResAmt;
		}

		public BigDecimal getR44NonResAmt() {
			return r44NonResAmt;
		}

		public void setR44NonResAmt(BigDecimal r44NonResAmt) {
			this.r44NonResAmt = r44NonResAmt;
		}

		public Date getReport_date() {
			return REPORT_DATE;
		}

		public void setReport_date(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
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

	}

//ARCHIVAL ROW MAPPER

	class Q_SMME_loans_AdvancesArchivalRowMapper implements RowMapper<Q_SMME_loans_Advances_Archival_Summary_Entity> {

		@Override
		public Q_SMME_loans_Advances_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_loans_Advances_Archival_Summary_Entity obj = new Q_SMME_loans_Advances_Archival_Summary_Entity();
			obj.setR15Caoa(rs.getString("R15_CAOA"));
			obj.setR15ResAmt(rs.getBigDecimal("R15_RES_AMT"));
			obj.setR15NonResAmt(rs.getBigDecimal("R15_NON_RES_AMT"));

			obj.setR16Caoa(rs.getString("R16_CAOA"));
			obj.setR16ResAmt(rs.getBigDecimal("R16_RES_AMT"));
			obj.setR16NonResAmt(rs.getBigDecimal("R16_NON_RES_AMT"));

			obj.setR17Caoa(rs.getString("R17_CAOA"));
			obj.setR17ResAmt(rs.getBigDecimal("R17_RES_AMT"));
			obj.setR17NonResAmt(rs.getBigDecimal("R17_NON_RES_AMT"));

			obj.setR18Caoa(rs.getString("R18_CAOA"));
			obj.setR18ResAmt(rs.getBigDecimal("R18_RES_AMT"));
			obj.setR18NonResAmt(rs.getBigDecimal("R18_NON_RES_AMT"));

			obj.setR19Caoa(rs.getString("R19_CAOA"));
			obj.setR19ResAmt(rs.getBigDecimal("R19_RES_AMT"));
			obj.setR19NonResAmt(rs.getBigDecimal("R19_NON_RES_AMT"));

			obj.setR20Caoa(rs.getString("R20_CAOA"));
			obj.setR20ResAmt(rs.getBigDecimal("R20_RES_AMT"));
			obj.setR20NonResAmt(rs.getBigDecimal("R20_NON_RES_AMT"));

			obj.setR21Caoa(rs.getString("R21_CAOA"));
			obj.setR21ResAmt(rs.getBigDecimal("R21_RES_AMT"));
			obj.setR21NonResAmt(rs.getBigDecimal("R21_NON_RES_AMT"));

			obj.setR22Caoa(rs.getString("R22_CAOA"));
			obj.setR22ResAmt(rs.getBigDecimal("R22_RES_AMT"));
			obj.setR22NonResAmt(rs.getBigDecimal("R22_NON_RES_AMT"));

			obj.setR23Caoa(rs.getString("R23_CAOA"));
			obj.setR23ResAmt(rs.getBigDecimal("R23_RES_AMT"));
			obj.setR23NonResAmt(rs.getBigDecimal("R23_NON_RES_AMT"));

			obj.setR24Caoa(rs.getString("R24_CAOA"));
			obj.setR24ResAmt(rs.getBigDecimal("R24_RES_AMT"));
			obj.setR24NonResAmt(rs.getBigDecimal("R24_NON_RES_AMT"));

			obj.setR25Caoa(rs.getString("R25_CAOA"));
			obj.setR25ResAmt(rs.getBigDecimal("R25_RES_AMT"));
			obj.setR25NonResAmt(rs.getBigDecimal("R25_NON_RES_AMT"));

			obj.setR26Caoa(rs.getString("R26_CAOA"));
			obj.setR26ResAmt(rs.getBigDecimal("R26_RES_AMT"));
			obj.setR26NonResAmt(rs.getBigDecimal("R26_NON_RES_AMT"));

			obj.setR27Caoa(rs.getString("R27_CAOA"));
			obj.setR27ResAmt(rs.getBigDecimal("R27_RES_AMT"));
			obj.setR27NonResAmt(rs.getBigDecimal("R27_NON_RES_AMT"));

			obj.setR28Caoa(rs.getString("R28_CAOA"));
			obj.setR28ResAmt(rs.getBigDecimal("R28_RES_AMT"));
			obj.setR28NonResAmt(rs.getBigDecimal("R28_NON_RES_AMT"));

			obj.setR29Caoa(rs.getString("R29_CAOA"));
			obj.setR29ResAmt(rs.getBigDecimal("R29_RES_AMT"));
			obj.setR29NonResAmt(rs.getBigDecimal("R29_NON_RES_AMT"));

			obj.setR30Caoa(rs.getString("R30_CAOA"));
			obj.setR30ResAmt(rs.getBigDecimal("R30_RES_AMT"));
			obj.setR30NonResAmt(rs.getBigDecimal("R30_NON_RES_AMT"));

			obj.setR31Caoa(rs.getString("R31_CAOA"));
			obj.setR31ResAmt(rs.getBigDecimal("R31_RES_AMT"));
			obj.setR31NonResAmt(rs.getBigDecimal("R31_NON_RES_AMT"));

			obj.setR32Caoa(rs.getString("R32_CAOA"));
			obj.setR32ResAmt(rs.getBigDecimal("R32_RES_AMT"));
			obj.setR32NonResAmt(rs.getBigDecimal("R32_NON_RES_AMT"));

			obj.setR33Caoa(rs.getString("R33_CAOA"));
			obj.setR33ResAmt(rs.getBigDecimal("R33_RES_AMT"));
			obj.setR33NonResAmt(rs.getBigDecimal("R33_NON_RES_AMT"));

			obj.setR34Caoa(rs.getString("R34_CAOA"));
			obj.setR34ResAmt(rs.getBigDecimal("R34_RES_AMT"));
			obj.setR34NonResAmt(rs.getBigDecimal("R34_NON_RES_AMT"));

			obj.setR35Caoa(rs.getString("R35_CAOA"));
			obj.setR35ResAmt(rs.getBigDecimal("R35_RES_AMT"));
			obj.setR35NonResAmt(rs.getBigDecimal("R35_NON_RES_AMT"));

			obj.setR36Caoa(rs.getString("R36_CAOA"));
			obj.setR36ResAmt(rs.getBigDecimal("R36_RES_AMT"));
			obj.setR36NonResAmt(rs.getBigDecimal("R36_NON_RES_AMT"));

			obj.setR37Caoa(rs.getString("R37_CAOA"));
			obj.setR37ResAmt(rs.getBigDecimal("R37_RES_AMT"));
			obj.setR37NonResAmt(rs.getBigDecimal("R37_NON_RES_AMT"));

			obj.setR38Caoa(rs.getString("R38_CAOA"));
			obj.setR38ResAmt(rs.getBigDecimal("R38_RES_AMT"));
			obj.setR38NonResAmt(rs.getBigDecimal("R38_NON_RES_AMT"));

			obj.setR39Caoa(rs.getString("R39_CAOA"));
			obj.setR39ResAmt(rs.getBigDecimal("R39_RES_AMT"));
			obj.setR39NonResAmt(rs.getBigDecimal("R39_NON_RES_AMT"));

			obj.setR40Caoa(rs.getString("R40_CAOA"));
			obj.setR40ResAmt(rs.getBigDecimal("R40_RES_AMT"));
			obj.setR40NonResAmt(rs.getBigDecimal("R40_NON_RES_AMT"));

			obj.setR41Caoa(rs.getString("R41_CAOA"));
			obj.setR41ResAmt(rs.getBigDecimal("R41_RES_AMT"));
			obj.setR41NonResAmt(rs.getBigDecimal("R41_NON_RES_AMT"));

			obj.setR42Caoa(rs.getString("R42_CAOA"));
			obj.setR42ResAmt(rs.getBigDecimal("R42_RES_AMT"));
			obj.setR42NonResAmt(rs.getBigDecimal("R42_NON_RES_AMT"));

			obj.setR43Caoa(rs.getString("R43_CAOA"));
			obj.setR43ResAmt(rs.getBigDecimal("R43_RES_AMT"));
			obj.setR43NonResAmt(rs.getBigDecimal("R43_NON_RES_AMT"));

			obj.setR44Caoa(rs.getString("R44_CAOA"));
			obj.setR44ResAmt(rs.getBigDecimal("R44_RES_AMT"));
			obj.setR44NonResAmt(rs.getBigDecimal("R44_NON_RES_AMT"));

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

	@IdClass(Q_SMME_loans_Advances_PK.class)
	public class Q_SMME_loans_Advances_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		// === R15 ===
		@Column(name = "R15_CAOA", length = 100)
		private String r15Caoa;

		@Column(name = "R15_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r15ResAmt;

		@Column(name = "R15_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r15NonResAmt;

		// === R16 ===
		@Column(name = "R16_CAOA", length = 100)
		private String r16Caoa;

		@Column(name = "R16_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r16ResAmt;

		@Column(name = "R16_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r16NonResAmt;

		// === R17 ===
		@Column(name = "R17_CAOA", length = 100)
		private String r17Caoa;

		@Column(name = "R17_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r17ResAmt;

		@Column(name = "R17_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r17NonResAmt;

		// === R18 ===
		@Column(name = "R18_CAOA", length = 100)
		private String r18Caoa;

		@Column(name = "R18_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r18ResAmt;

		@Column(name = "R18_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r18NonResAmt;

		// === R19 ===
		@Column(name = "R19_CAOA", length = 100)
		private String r19Caoa;

		@Column(name = "R19_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r19ResAmt;

		@Column(name = "R19_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r19NonResAmt;

		// === R20 ===
		@Column(name = "R20_CAOA", length = 100)
		private String r20Caoa;

		@Column(name = "R20_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r20ResAmt;

		@Column(name = "R20_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r20NonResAmt;

		// === R21 ===
		@Column(name = "R21_CAOA", length = 100)
		private String r21Caoa;

		@Column(name = "R21_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r21ResAmt;

		@Column(name = "R21_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r21NonResAmt;

		// === R22 ===
		@Column(name = "R22_CAOA", length = 100)
		private String r22Caoa;

		@Column(name = "R22_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r22ResAmt;

		@Column(name = "R22_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r22NonResAmt;

		// === R23 ===
		@Column(name = "R23_CAOA", length = 100)
		private String r23Caoa;

		@Column(name = "R23_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r23ResAmt;

		@Column(name = "R23_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r23NonResAmt;

		// === R24 ===
		@Column(name = "R24_CAOA", length = 100)
		private String r24Caoa;

		@Column(name = "R24_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r24ResAmt;

		@Column(name = "R24_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r24NonResAmt;

		// === R25 ===
		@Column(name = "R25_CAOA", length = 100)
		private String r25Caoa;

		@Column(name = "R25_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r25ResAmt;

		@Column(name = "R25_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r25NonResAmt;

		// === R26 ===
		@Column(name = "R26_CAOA", length = 100)
		private String r26Caoa;

		@Column(name = "R26_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r26ResAmt;

		@Column(name = "R26_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r26NonResAmt;

		// === R27 ===
		@Column(name = "R27_CAOA", length = 100)
		private String r27Caoa;

		@Column(name = "R27_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r27ResAmt;

		@Column(name = "R27_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r27NonResAmt;

		// === R28 ===
		@Column(name = "R28_CAOA", length = 100)
		private String r28Caoa;

		@Column(name = "R28_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r28ResAmt;

		@Column(name = "R28_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r28NonResAmt;

		// === R29 ===
		@Column(name = "R29_CAOA", length = 100)
		private String r29Caoa;

		@Column(name = "R29_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r29ResAmt;

		@Column(name = "R29_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r29NonResAmt;

		// === R30 ===
		@Column(name = "R30_CAOA", length = 100)
		private String r30Caoa;

		@Column(name = "R30_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r30ResAmt;

		@Column(name = "R30_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r30NonResAmt;

		// === R31 ===
		@Column(name = "R31_CAOA", length = 100)
		private String r31Caoa;

		@Column(name = "R31_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r31ResAmt;

		@Column(name = "R31_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r31NonResAmt;

		// === R32 ===
		@Column(name = "R32_CAOA", length = 100)
		private String r32Caoa;

		@Column(name = "R32_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r32ResAmt;

		@Column(name = "R32_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r32NonResAmt;

		// === R33 ===
		@Column(name = "R33_CAOA", length = 100)
		private String r33Caoa;

		@Column(name = "R33_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r33ResAmt;

		@Column(name = "R33_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r33NonResAmt;

		// === R34 ===
		@Column(name = "R34_CAOA", length = 100)
		private String r34Caoa;

		@Column(name = "R34_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r34ResAmt;

		@Column(name = "R34_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r34NonResAmt;

		// === R35 ===
		@Column(name = "R35_CAOA", length = 100)
		private String r35Caoa;

		@Column(name = "R35_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r35ResAmt;

		@Column(name = "R35_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r35NonResAmt;

		// === R36 ===
		@Column(name = "R36_CAOA", length = 100)
		private String r36Caoa;

		@Column(name = "R36_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r36ResAmt;

		@Column(name = "R36_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r36NonResAmt;

		// === R37 ===
		@Column(name = "R37_CAOA", length = 100)
		private String r37Caoa;

		@Column(name = "R37_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r37ResAmt;

		@Column(name = "R37_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r37NonResAmt;

		// === R38 ===
		@Column(name = "R38_CAOA", length = 100)
		private String r38Caoa;

		@Column(name = "R38_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r38ResAmt;

		@Column(name = "R38_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r38NonResAmt;

		// === R39 ===
		@Column(name = "R39_CAOA", length = 100)
		private String r39Caoa;

		@Column(name = "R39_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r39ResAmt;

		@Column(name = "R39_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r39NonResAmt;

		// === R40 ===
		@Column(name = "R40_CAOA", length = 100)
		private String r40Caoa;

		@Column(name = "R40_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r40ResAmt;

		@Column(name = "R40_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r40NonResAmt;

		// === R41 ===
		@Column(name = "R41_CAOA", length = 100)
		private String r41Caoa;

		@Column(name = "R41_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r41ResAmt;

		@Column(name = "R41_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r41NonResAmt;

		// === R42 ===
		@Column(name = "R42_CAOA", length = 100)
		private String r42Caoa;

		@Column(name = "R42_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r42ResAmt;

		@Column(name = "R42_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r42NonResAmt;

		// === R43 ===
		@Column(name = "R43_CAOA", length = 100)
		private String r43Caoa;

		@Column(name = "R43_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r43ResAmt;

		@Column(name = "R43_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r43NonResAmt;

		// === R44 ===
		@Column(name = "R44_CAOA", length = 100)
		private String r44Caoa;

		@Column(name = "R44_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r44ResAmt;

		@Column(name = "R44_NON_RES_AMT", precision = 24, scale = 3)
		private BigDecimal r44NonResAmt;
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

		public String getR15Caoa() {
			return r15Caoa;
		}

		public void setR15Caoa(String r15Caoa) {
			this.r15Caoa = r15Caoa;
		}

		public BigDecimal getR15ResAmt() {
			return r15ResAmt;
		}

		public void setR15ResAmt(BigDecimal r15ResAmt) {
			this.r15ResAmt = r15ResAmt;
		}

		public BigDecimal getR15NonResAmt() {
			return r15NonResAmt;
		}

		public void setR15NonResAmt(BigDecimal r15NonResAmt) {
			this.r15NonResAmt = r15NonResAmt;
		}

		public String getR16Caoa() {
			return r16Caoa;
		}

		public void setR16Caoa(String r16Caoa) {
			this.r16Caoa = r16Caoa;
		}

		public BigDecimal getR16ResAmt() {
			return r16ResAmt;
		}

		public void setR16ResAmt(BigDecimal r16ResAmt) {
			this.r16ResAmt = r16ResAmt;
		}

		public BigDecimal getR16NonResAmt() {
			return r16NonResAmt;
		}

		public void setR16NonResAmt(BigDecimal r16NonResAmt) {
			this.r16NonResAmt = r16NonResAmt;
		}

		public String getR17Caoa() {
			return r17Caoa;
		}

		public void setR17Caoa(String r17Caoa) {
			this.r17Caoa = r17Caoa;
		}

		public BigDecimal getR17ResAmt() {
			return r17ResAmt;
		}

		public void setR17ResAmt(BigDecimal r17ResAmt) {
			this.r17ResAmt = r17ResAmt;
		}

		public BigDecimal getR17NonResAmt() {
			return r17NonResAmt;
		}

		public void setR17NonResAmt(BigDecimal r17NonResAmt) {
			this.r17NonResAmt = r17NonResAmt;
		}

		public String getR18Caoa() {
			return r18Caoa;
		}

		public void setR18Caoa(String r18Caoa) {
			this.r18Caoa = r18Caoa;
		}

		public BigDecimal getR18ResAmt() {
			return r18ResAmt;
		}

		public void setR18ResAmt(BigDecimal r18ResAmt) {
			this.r18ResAmt = r18ResAmt;
		}

		public BigDecimal getR18NonResAmt() {
			return r18NonResAmt;
		}

		public void setR18NonResAmt(BigDecimal r18NonResAmt) {
			this.r18NonResAmt = r18NonResAmt;
		}

		public String getR19Caoa() {
			return r19Caoa;
		}

		public void setR19Caoa(String r19Caoa) {
			this.r19Caoa = r19Caoa;
		}

		public BigDecimal getR19ResAmt() {
			return r19ResAmt;
		}

		public void setR19ResAmt(BigDecimal r19ResAmt) {
			this.r19ResAmt = r19ResAmt;
		}

		public BigDecimal getR19NonResAmt() {
			return r19NonResAmt;
		}

		public void setR19NonResAmt(BigDecimal r19NonResAmt) {
			this.r19NonResAmt = r19NonResAmt;
		}

		public String getR20Caoa() {
			return r20Caoa;
		}

		public void setR20Caoa(String r20Caoa) {
			this.r20Caoa = r20Caoa;
		}

		public BigDecimal getR20ResAmt() {
			return r20ResAmt;
		}

		public void setR20ResAmt(BigDecimal r20ResAmt) {
			this.r20ResAmt = r20ResAmt;
		}

		public BigDecimal getR20NonResAmt() {
			return r20NonResAmt;
		}

		public void setR20NonResAmt(BigDecimal r20NonResAmt) {
			this.r20NonResAmt = r20NonResAmt;
		}

		public String getR21Caoa() {
			return r21Caoa;
		}

		public void setR21Caoa(String r21Caoa) {
			this.r21Caoa = r21Caoa;
		}

		public BigDecimal getR21ResAmt() {
			return r21ResAmt;
		}

		public void setR21ResAmt(BigDecimal r21ResAmt) {
			this.r21ResAmt = r21ResAmt;
		}

		public BigDecimal getR21NonResAmt() {
			return r21NonResAmt;
		}

		public void setR21NonResAmt(BigDecimal r21NonResAmt) {
			this.r21NonResAmt = r21NonResAmt;
		}

		public String getR22Caoa() {
			return r22Caoa;
		}

		public void setR22Caoa(String r22Caoa) {
			this.r22Caoa = r22Caoa;
		}

		public BigDecimal getR22ResAmt() {
			return r22ResAmt;
		}

		public void setR22ResAmt(BigDecimal r22ResAmt) {
			this.r22ResAmt = r22ResAmt;
		}

		public BigDecimal getR22NonResAmt() {
			return r22NonResAmt;
		}

		public void setR22NonResAmt(BigDecimal r22NonResAmt) {
			this.r22NonResAmt = r22NonResAmt;
		}

		public String getR23Caoa() {
			return r23Caoa;
		}

		public void setR23Caoa(String r23Caoa) {
			this.r23Caoa = r23Caoa;
		}

		public BigDecimal getR23ResAmt() {
			return r23ResAmt;
		}

		public void setR23ResAmt(BigDecimal r23ResAmt) {
			this.r23ResAmt = r23ResAmt;
		}

		public BigDecimal getR23NonResAmt() {
			return r23NonResAmt;
		}

		public void setR23NonResAmt(BigDecimal r23NonResAmt) {
			this.r23NonResAmt = r23NonResAmt;
		}

		public String getR24Caoa() {
			return r24Caoa;
		}

		public void setR24Caoa(String r24Caoa) {
			this.r24Caoa = r24Caoa;
		}

		public BigDecimal getR24ResAmt() {
			return r24ResAmt;
		}

		public void setR24ResAmt(BigDecimal r24ResAmt) {
			this.r24ResAmt = r24ResAmt;
		}

		public BigDecimal getR24NonResAmt() {
			return r24NonResAmt;
		}

		public void setR24NonResAmt(BigDecimal r24NonResAmt) {
			this.r24NonResAmt = r24NonResAmt;
		}

		public String getR25Caoa() {
			return r25Caoa;
		}

		public void setR25Caoa(String r25Caoa) {
			this.r25Caoa = r25Caoa;
		}

		public BigDecimal getR25ResAmt() {
			return r25ResAmt;
		}

		public void setR25ResAmt(BigDecimal r25ResAmt) {
			this.r25ResAmt = r25ResAmt;
		}

		public BigDecimal getR25NonResAmt() {
			return r25NonResAmt;
		}

		public void setR25NonResAmt(BigDecimal r25NonResAmt) {
			this.r25NonResAmt = r25NonResAmt;
		}

		public String getR26Caoa() {
			return r26Caoa;
		}

		public void setR26Caoa(String r26Caoa) {
			this.r26Caoa = r26Caoa;
		}

		public BigDecimal getR26ResAmt() {
			return r26ResAmt;
		}

		public void setR26ResAmt(BigDecimal r26ResAmt) {
			this.r26ResAmt = r26ResAmt;
		}

		public BigDecimal getR26NonResAmt() {
			return r26NonResAmt;
		}

		public void setR26NonResAmt(BigDecimal r26NonResAmt) {
			this.r26NonResAmt = r26NonResAmt;
		}

		public String getR27Caoa() {
			return r27Caoa;
		}

		public void setR27Caoa(String r27Caoa) {
			this.r27Caoa = r27Caoa;
		}

		public BigDecimal getR27ResAmt() {
			return r27ResAmt;
		}

		public void setR27ResAmt(BigDecimal r27ResAmt) {
			this.r27ResAmt = r27ResAmt;
		}

		public BigDecimal getR27NonResAmt() {
			return r27NonResAmt;
		}

		public void setR27NonResAmt(BigDecimal r27NonResAmt) {
			this.r27NonResAmt = r27NonResAmt;
		}

		public String getR28Caoa() {
			return r28Caoa;
		}

		public void setR28Caoa(String r28Caoa) {
			this.r28Caoa = r28Caoa;
		}

		public BigDecimal getR28ResAmt() {
			return r28ResAmt;
		}

		public void setR28ResAmt(BigDecimal r28ResAmt) {
			this.r28ResAmt = r28ResAmt;
		}

		public BigDecimal getR28NonResAmt() {
			return r28NonResAmt;
		}

		public void setR28NonResAmt(BigDecimal r28NonResAmt) {
			this.r28NonResAmt = r28NonResAmt;
		}

		public String getR29Caoa() {
			return r29Caoa;
		}

		public void setR29Caoa(String r29Caoa) {
			this.r29Caoa = r29Caoa;
		}

		public BigDecimal getR29ResAmt() {
			return r29ResAmt;
		}

		public void setR29ResAmt(BigDecimal r29ResAmt) {
			this.r29ResAmt = r29ResAmt;
		}

		public BigDecimal getR29NonResAmt() {
			return r29NonResAmt;
		}

		public void setR29NonResAmt(BigDecimal r29NonResAmt) {
			this.r29NonResAmt = r29NonResAmt;
		}

		public String getR30Caoa() {
			return r30Caoa;
		}

		public void setR30Caoa(String r30Caoa) {
			this.r30Caoa = r30Caoa;
		}

		public BigDecimal getR30ResAmt() {
			return r30ResAmt;
		}

		public void setR30ResAmt(BigDecimal r30ResAmt) {
			this.r30ResAmt = r30ResAmt;
		}

		public BigDecimal getR30NonResAmt() {
			return r30NonResAmt;
		}

		public void setR30NonResAmt(BigDecimal r30NonResAmt) {
			this.r30NonResAmt = r30NonResAmt;
		}

		public String getR31Caoa() {
			return r31Caoa;
		}

		public void setR31Caoa(String r31Caoa) {
			this.r31Caoa = r31Caoa;
		}

		public BigDecimal getR31ResAmt() {
			return r31ResAmt;
		}

		public void setR31ResAmt(BigDecimal r31ResAmt) {
			this.r31ResAmt = r31ResAmt;
		}

		public BigDecimal getR31NonResAmt() {
			return r31NonResAmt;
		}

		public void setR31NonResAmt(BigDecimal r31NonResAmt) {
			this.r31NonResAmt = r31NonResAmt;
		}

		public String getR32Caoa() {
			return r32Caoa;
		}

		public void setR32Caoa(String r32Caoa) {
			this.r32Caoa = r32Caoa;
		}

		public BigDecimal getR32ResAmt() {
			return r32ResAmt;
		}

		public void setR32ResAmt(BigDecimal r32ResAmt) {
			this.r32ResAmt = r32ResAmt;
		}

		public BigDecimal getR32NonResAmt() {
			return r32NonResAmt;
		}

		public void setR32NonResAmt(BigDecimal r32NonResAmt) {
			this.r32NonResAmt = r32NonResAmt;
		}

		public String getR33Caoa() {
			return r33Caoa;
		}

		public void setR33Caoa(String r33Caoa) {
			this.r33Caoa = r33Caoa;
		}

		public BigDecimal getR33ResAmt() {
			return r33ResAmt;
		}

		public void setR33ResAmt(BigDecimal r33ResAmt) {
			this.r33ResAmt = r33ResAmt;
		}

		public BigDecimal getR33NonResAmt() {
			return r33NonResAmt;
		}

		public void setR33NonResAmt(BigDecimal r33NonResAmt) {
			this.r33NonResAmt = r33NonResAmt;
		}

		public String getR34Caoa() {
			return r34Caoa;
		}

		public void setR34Caoa(String r34Caoa) {
			this.r34Caoa = r34Caoa;
		}

		public BigDecimal getR34ResAmt() {
			return r34ResAmt;
		}

		public void setR34ResAmt(BigDecimal r34ResAmt) {
			this.r34ResAmt = r34ResAmt;
		}

		public BigDecimal getR34NonResAmt() {
			return r34NonResAmt;
		}

		public void setR34NonResAmt(BigDecimal r34NonResAmt) {
			this.r34NonResAmt = r34NonResAmt;
		}

		public String getR35Caoa() {
			return r35Caoa;
		}

		public void setR35Caoa(String r35Caoa) {
			this.r35Caoa = r35Caoa;
		}

		public BigDecimal getR35ResAmt() {
			return r35ResAmt;
		}

		public void setR35ResAmt(BigDecimal r35ResAmt) {
			this.r35ResAmt = r35ResAmt;
		}

		public BigDecimal getR35NonResAmt() {
			return r35NonResAmt;
		}

		public void setR35NonResAmt(BigDecimal r35NonResAmt) {
			this.r35NonResAmt = r35NonResAmt;
		}

		public String getR36Caoa() {
			return r36Caoa;
		}

		public void setR36Caoa(String r36Caoa) {
			this.r36Caoa = r36Caoa;
		}

		public BigDecimal getR36ResAmt() {
			return r36ResAmt;
		}

		public void setR36ResAmt(BigDecimal r36ResAmt) {
			this.r36ResAmt = r36ResAmt;
		}

		public BigDecimal getR36NonResAmt() {
			return r36NonResAmt;
		}

		public void setR36NonResAmt(BigDecimal r36NonResAmt) {
			this.r36NonResAmt = r36NonResAmt;
		}

		public String getR37Caoa() {
			return r37Caoa;
		}

		public void setR37Caoa(String r37Caoa) {
			this.r37Caoa = r37Caoa;
		}

		public BigDecimal getR37ResAmt() {
			return r37ResAmt;
		}

		public void setR37ResAmt(BigDecimal r37ResAmt) {
			this.r37ResAmt = r37ResAmt;
		}

		public BigDecimal getR37NonResAmt() {
			return r37NonResAmt;
		}

		public void setR37NonResAmt(BigDecimal r37NonResAmt) {
			this.r37NonResAmt = r37NonResAmt;
		}

		public String getR38Caoa() {
			return r38Caoa;
		}

		public void setR38Caoa(String r38Caoa) {
			this.r38Caoa = r38Caoa;
		}

		public BigDecimal getR38ResAmt() {
			return r38ResAmt;
		}

		public void setR38ResAmt(BigDecimal r38ResAmt) {
			this.r38ResAmt = r38ResAmt;
		}

		public BigDecimal getR38NonResAmt() {
			return r38NonResAmt;
		}

		public void setR38NonResAmt(BigDecimal r38NonResAmt) {
			this.r38NonResAmt = r38NonResAmt;
		}

		public String getR39Caoa() {
			return r39Caoa;
		}

		public void setR39Caoa(String r39Caoa) {
			this.r39Caoa = r39Caoa;
		}

		public BigDecimal getR39ResAmt() {
			return r39ResAmt;
		}

		public void setR39ResAmt(BigDecimal r39ResAmt) {
			this.r39ResAmt = r39ResAmt;
		}

		public BigDecimal getR39NonResAmt() {
			return r39NonResAmt;
		}

		public void setR39NonResAmt(BigDecimal r39NonResAmt) {
			this.r39NonResAmt = r39NonResAmt;
		}

		public String getR40Caoa() {
			return r40Caoa;
		}

		public void setR40Caoa(String r40Caoa) {
			this.r40Caoa = r40Caoa;
		}

		public BigDecimal getR40ResAmt() {
			return r40ResAmt;
		}

		public void setR40ResAmt(BigDecimal r40ResAmt) {
			this.r40ResAmt = r40ResAmt;
		}

		public BigDecimal getR40NonResAmt() {
			return r40NonResAmt;
		}

		public void setR40NonResAmt(BigDecimal r40NonResAmt) {
			this.r40NonResAmt = r40NonResAmt;
		}

		public String getR41Caoa() {
			return r41Caoa;
		}

		public void setR41Caoa(String r41Caoa) {
			this.r41Caoa = r41Caoa;
		}

		public BigDecimal getR41ResAmt() {
			return r41ResAmt;
		}

		public void setR41ResAmt(BigDecimal r41ResAmt) {
			this.r41ResAmt = r41ResAmt;
		}

		public BigDecimal getR41NonResAmt() {
			return r41NonResAmt;
		}

		public void setR41NonResAmt(BigDecimal r41NonResAmt) {
			this.r41NonResAmt = r41NonResAmt;
		}

		public String getR42Caoa() {
			return r42Caoa;
		}

		public void setR42Caoa(String r42Caoa) {
			this.r42Caoa = r42Caoa;
		}

		public BigDecimal getR42ResAmt() {
			return r42ResAmt;
		}

		public void setR42ResAmt(BigDecimal r42ResAmt) {
			this.r42ResAmt = r42ResAmt;
		}

		public BigDecimal getR42NonResAmt() {
			return r42NonResAmt;
		}

		public void setR42NonResAmt(BigDecimal r42NonResAmt) {
			this.r42NonResAmt = r42NonResAmt;
		}

		public String getR43Caoa() {
			return r43Caoa;
		}

		public void setR43Caoa(String r43Caoa) {
			this.r43Caoa = r43Caoa;
		}

		public BigDecimal getR43ResAmt() {
			return r43ResAmt;
		}

		public void setR43ResAmt(BigDecimal r43ResAmt) {
			this.r43ResAmt = r43ResAmt;
		}

		public BigDecimal getR43NonResAmt() {
			return r43NonResAmt;
		}

		public void setR43NonResAmt(BigDecimal r43NonResAmt) {
			this.r43NonResAmt = r43NonResAmt;
		}

		public String getR44Caoa() {
			return r44Caoa;
		}

		public void setR44Caoa(String r44Caoa) {
			this.r44Caoa = r44Caoa;
		}

		public BigDecimal getR44ResAmt() {
			return r44ResAmt;
		}

		public void setR44ResAmt(BigDecimal r44ResAmt) {
			this.r44ResAmt = r44ResAmt;
		}

		public BigDecimal getR44NonResAmt() {
			return r44NonResAmt;
		}

		public void setR44NonResAmt(BigDecimal r44NonResAmt) {
			this.r44NonResAmt = r44NonResAmt;
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

	public static class Q_SMME_loans_Advances_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Q_SMME_loans_Advances_PK() {
		}

		public Q_SMME_loans_Advances_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Q_SMME_loans_Advances_PK))
				return false;
			Q_SMME_loans_Advances_PK that = (Q_SMME_loans_Advances_PK) o;
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

	public class Q_SMME_loans_Advances_Detail_Entity {

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

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

		@Column(name = "REPORT_ADDL_CRITERIA_3")
		private String reportAddlCriteria3;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "SANCTION_LIMIT", precision = 24, scale = 2)
		private BigDecimal sanctionLimit;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInPula;

		@Column(name = "SEGMENT")
		private String segment;

		@Column(name = "CONSTITUTION_CODE")
		private String constitutionCode;

		@Column(name = "SMME")
		private String smme;

		@Column(name = "REPORT_DATE")
		@Temporal(TemporalType.DATE)
		private Date reportDate;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private char entity_flg;
		private char modify_flg;
		private char del_flg;

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

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getConstitutionCode() {
			return constitutionCode;
		}

		public void setConstitutionCode(String constitutionCode) {
			this.constitutionCode = constitutionCode;
		}

		public String getSmme() {
			return smme;
		}

		public void setSmme(String smme) {
			this.smme = smme;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreate_user() {
			return create_user;
		}

		public void setCreate_user(String create_user) {
			this.create_user = create_user;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}

		public String getModify_user() {
			return modify_user;
		}

		public void setModify_user(String modify_user) {
			this.modify_user = modify_user;
		}

		public Date getModify_time() {
			return modify_time;
		}

		public void setModify_time(Date modify_time) {
			this.modify_time = modify_time;
		}

		public String getVerify_user() {
			return verify_user;
		}

		public void setVerify_user(String verify_user) {
			this.verify_user = verify_user;
		}

		public Date getVerify_time() {
			return verify_time;
		}

		public void setVerify_time(Date verify_time) {
			this.verify_time = verify_time;
		}

		public char getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(char entity_flg) {
			this.entity_flg = entity_flg;
		}

		public char getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(char modify_flg) {
			this.modify_flg = modify_flg;
		}

		public char getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(char del_flg) {
			this.del_flg = del_flg;
		}

	}

	class Q_SMME_loans_AdvancesDetailRowMapper implements RowMapper<Q_SMME_loans_Advances_Detail_Entity> {

		@Override
		public Q_SMME_loans_Advances_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_loans_Advances_Detail_Entity obj = new Q_SMME_loans_Advances_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setConstitutionCode(rs.getString("CONSTITUTION_CODE"));
			obj.setSmme(rs.getString("SMME"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreate_user(rs.getString("create_user"));
			obj.setCreate_time(rs.getDate("create_time"));
			obj.setModify_user(rs.getString("modify_user"));
			obj.setModify_time(rs.getDate("modify_time"));
			obj.setVerify_user(rs.getString("verify_user"));
			obj.setVerify_time(rs.getDate("verify_time"));
			obj.setEntity_flg(rs.getString("entity_flg") != null ? rs.getString("entity_flg").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("modify_flg") != null ? rs.getString("modify_flg").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("del_flg") != null ? rs.getString("del_flg").charAt(0) : ' ');

			return obj;
		}
	}

	class Q_SMME_loans_AdvancesArchivalDetailRowMapper
			implements RowMapper<Q_SMME_loans_Advances_Archival_Detail_Entity> {

		@Override
		public Q_SMME_loans_Advances_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_loans_Advances_Archival_Detail_Entity obj = new Q_SMME_loans_Advances_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportName(rs.getString("REPORT_NAME"));
			obj.setReportLabel(rs.getString("REPORT_LABEL"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportAddlCriteria2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReportAddlCriteria3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setSanctionLimit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcctBalanceInPula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setConstitutionCode(rs.getString("CONSTITUTION_CODE"));
			obj.setSmme(rs.getString("SMME"));
			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setCreate_user(rs.getString("create_user"));
			obj.setCreate_time(rs.getDate("create_time"));
			obj.setModify_user(rs.getString("modify_user"));
			obj.setModify_time(rs.getDate("modify_time"));
			obj.setVerify_user(rs.getString("verify_user"));
			obj.setVerify_time(rs.getDate("verify_time"));
			obj.setEntity_flg(rs.getString("entity_flg") != null ? rs.getString("entity_flg").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("modify_flg") != null ? rs.getString("modify_flg").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("del_flg") != null ? rs.getString("del_flg").charAt(0) : ' ');
			return obj;
		}
	}

	public class Q_SMME_loans_Advances_Archival_Detail_Entity {
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

		@Column(name = "REPORT_ADDL_CRITERIA_2")
		private String reportAddlCriteria2;

		@Column(name = "REPORT_ADDL_CRITERIA_3")
		private String reportAddlCriteria3;

		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;

		@Column(name = "SANCTION_LIMIT", precision = 24, scale = 2)
		private BigDecimal sanctionLimit;

		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;

		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInPula;

		@Column(name = "SEGMENT")
		private String segment;

		@Column(name = "CONSTITUTION_CODE")
		private String constitutionCode;

		@Column(name = "SMME")
		private String smme;

		@Column(name = "REPORT_DATE")
		@Temporal(TemporalType.DATE)
		private Date reportDate;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private char entity_flg;
		private char modify_flg;
		private char del_flg;

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

		public String getReportAddlCriteria2() {
			return reportAddlCriteria2;
		}

		public void setReportAddlCriteria2(String reportAddlCriteria2) {
			this.reportAddlCriteria2 = reportAddlCriteria2;
		}

		public String getReportAddlCriteria3() {
			return reportAddlCriteria3;
		}

		public void setReportAddlCriteria3(String reportAddlCriteria3) {
			this.reportAddlCriteria3 = reportAddlCriteria3;
		}

		public String getReportRemarks() {
			return reportRemarks;
		}

		public void setReportRemarks(String reportRemarks) {
			this.reportRemarks = reportRemarks;
		}

		public BigDecimal getSanctionLimit() {
			return sanctionLimit;
		}

		public void setSanctionLimit(BigDecimal sanctionLimit) {
			this.sanctionLimit = sanctionLimit;
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

		public BigDecimal getAcctBalanceInPula() {
			return acctBalanceInPula;
		}

		public void setAcctBalanceInPula(BigDecimal acctBalanceInPula) {
			this.acctBalanceInPula = acctBalanceInPula;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getConstitutionCode() {
			return constitutionCode;
		}

		public void setConstitutionCode(String constitutionCode) {
			this.constitutionCode = constitutionCode;
		}

		public String getSmme() {
			return smme;
		}

		public void setSmme(String smme) {
			this.smme = smme;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public void setReportDate(Date reportDate) {
			this.reportDate = reportDate;
		}

		public String getCreate_user() {
			return create_user;
		}

		public void setCreate_user(String create_user) {
			this.create_user = create_user;
		}

		public Date getCreate_time() {
			return create_time;
		}

		public void setCreate_time(Date create_time) {
			this.create_time = create_time;
		}

		public String getModify_user() {
			return modify_user;
		}

		public void setModify_user(String modify_user) {
			this.modify_user = modify_user;
		}

		public Date getModify_time() {
			return modify_time;
		}

		public void setModify_time(Date modify_time) {
			this.modify_time = modify_time;
		}

		public String getVerify_user() {
			return verify_user;
		}

		public void setVerify_user(String verify_user) {
			this.verify_user = verify_user;
		}

		public Date getVerify_time() {
			return verify_time;
		}

		public void setVerify_time(Date verify_time) {
			this.verify_time = verify_time;
		}

		public char getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(char entity_flg) {
			this.entity_flg = entity_flg;
		}

		public char getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(char modify_flg) {
			this.modify_flg = modify_flg;
		}

		public char getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(char del_flg) {
			this.del_flg = del_flg;
		}

	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_Q_SMMEView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("Q_SMME_loans_Advances View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<Q_SMME_loans_Advances_Archival_Summary_Entity> T1Master = new ArrayList<>();

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

			List<Q_SMME_loans_Advances_Summary_Entity> T1Master = new ArrayList<>();

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

		mv.setViewName("BRRS/Q_SMME_Loans_Advances");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getBRRS_Q_SMMEcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
			Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

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

				List<Q_SMME_loans_Advances_Archival_Detail_Entity> detailList;

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

				List<Q_SMME_loans_Advances_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/Q_SMME_Loans_Advances");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getQ_SMMEArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Q_SMME_loans_Advances_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_loans_Advances_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_SMME_loans_Advances_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Q_SMME_loans_Advances  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/Q_SMME_Loans_Advances");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				Q_SMME_loans_Advances_Detail_Entity Q_SMME_loans_AdvancesEntity = findBySnoArch(SNO);
				if (Q_SMME_loans_AdvancesEntity != null && Q_SMME_loans_AdvancesEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(Q_SMME_loans_AdvancesEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("Q_SMME_loans_AdvancesData", Q_SMME_loans_AdvancesEntity);
			} else {
				Q_SMME_loans_Advances_Detail_Entity Q_SMME_loans_AdvancesEntity = findBySno(SNO);
				if (Q_SMME_loans_AdvancesEntity != null && Q_SMME_loans_AdvancesEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(Q_SMME_loans_AdvancesEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("Q_SMME_loans_AdvancesData", Q_SMME_loans_AdvancesEntity);
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

			String acctBalanceInpula = request.getParameter("acctBalanceInPula");

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			Q_SMME_loans_Advances_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			Q_SMME_loans_Advances_Detail_Entity oldcopy = new Q_SMME_loans_Advances_Detail_Entity();
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

				if (existing.getAcctBalanceInPula() == null
						|| existing.getAcctBalanceInPula().compareTo(newBalance) != 0) {

					existing.setAcctBalanceInPula(newBalance);

					isChanged = true;
				}
			}

			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				if (type == "RESUB" || type.equals("RESUB")) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?" + //
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInPula(), Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Q_SMME_loans_Advances Archival Screen",
							"BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Q_SMME_loans_Advances Screen",
							"BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_Q_SMME_loans_Advances_Procudure(reportDateStr, type, entry);

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
			Run_Q_SMME_loans_Advances_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_Q_SMME_loans_Advances_Procudure(String reportDateStr, String type, String entry) {

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
						String bdsql = "DELETE FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE ("
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_Q_SMME_LOANS_ADVANCES_SUMMARY_PROCEDURE(?); END;",
								formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_Q_SMME_LOANS_ADVANCES_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						StringBuilder columnsPart = new StringBuilder();
						String[] tokens = { "CAOA", "RES_AMT", "NON_RES_AMT	" };

						// Dynamically generate R6 to R62 columns
						for (int i = 15; i <= 44; i++) {
							for (String token : tokens) {
								columnsPart.append("R").append(i).append("_").append(token).append(", ");
							}
						}

						// Build the final query cleanly - Notice the '?' replacing REPORT_VERSION in
						// SELECT
						String finalsql = "INSERT INTO BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY ("
								+ columnsPart.toString()
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT " + columnsPart.toString()
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, formattedDate);
						System.out.println("Deleted from summary " + rowsDeletedSum + " rows after transfering.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public byte[] BRRS_Q_SMMEDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  Q_SMME_loans_Advances Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Q_SMME_loans_AdvancesDetailsDetail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
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
			List<Q_SMME_loans_Advances_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_SMME_loans_Advances_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for Q_SMME_loans_Advances — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Q_SMME_loans_Advances Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Q_SMME_loans_Advances ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Q_SMME_loans_Advances Detail NEW");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABEL",
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
			List<Q_SMME_loans_Advances_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(
					parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_SMME_loans_Advances_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCustId());
					row.createCell(1).setCellValue(item.getAcctNumber());
					row.createCell(2).setCellValue(item.getAcctName());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcctBalanceInPula() != null) {
						balanceCell.setCellValue(item.getAcctBalanceInPula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReportLabel());
					row.createCell(5).setCellValue(item.getReportAddlCriteria1());
					row.createCell(6)
							.setCellValue(item.getReportDate() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReportDate())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for Q_SMME_loans_Advances — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Q_SMME_loans_Advances NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getQ_SMMEExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.Q_SMME_loans_Advances");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
					version);
		}

		if ("email".equalsIgnoreCase(format) && version == null) {
			logger.info("Service: Generating Email report");
			return BRRS_Q_SMME_loans_Advances_EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		else {
			List<Q_SMME_loans_Advances_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

			System.out.println("DATA SIZE IS : " + dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  Q_SMME_loans_Advances report. Returning empty result.");
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
						Q_SMME_loans_Advances_Summary_Entity record = dataList.get(i);
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
						row = sheet.getRow(15);
						// row16
						// Column 1
						Cell R15cell2 = row.createCell(2);
						if (record.getR16ResAmt() != null) {
							R15cell2.setCellValue(record.getR16ResAmt().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R15cell3 = row.createCell(3);
						if (record.getR16NonResAmt() != null) {
							R15cell3.setCellValue(record.getR16NonResAmt().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}

						// row17
						// Column 1
						row = sheet.getRow(16);

						Cell R16cell2 = row.createCell(2);
						if (record.getR17ResAmt() != null) {
							R16cell2.setCellValue(record.getR17ResAmt().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R16cell3 = row.createCell(3);
						if (record.getR17NonResAmt() != null) {
							R16cell3.setCellValue(record.getR17NonResAmt().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}

						// ================== ROW 18 ==================
						// Column 1
						row = sheet.getRow(17);

						Cell R17cell2 = row.createCell(2);
						if (record.getR18ResAmt() != null) {
							R17cell2.setCellValue(record.getR18ResAmt().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);
						}

						// Column 2
						Cell R17cell3 = row.createCell(3);
						if (record.getR18NonResAmt() != null) {
							R17cell3.setCellValue(record.getR18NonResAmt().doubleValue());
							R17cell3.setCellStyle(numberStyle);
						} else {
							R17cell3.setCellValue("");
							R17cell3.setCellStyle(textStyle);
						}

						// ================== ROW 19 ==================
						row = sheet.getRow(18);
						Cell R18cell2 = row.createCell(2);
						if (record.getR19ResAmt() != null) {
							R18cell2.setCellValue(record.getR19ResAmt().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);
						}

						Cell R18cell3 = row.createCell(3);
						if (record.getR19NonResAmt() != null) {
							R18cell3.setCellValue(record.getR19NonResAmt().doubleValue());
							R18cell3.setCellStyle(numberStyle);
						} else {
							R18cell3.setCellValue("");
							R18cell3.setCellStyle(textStyle);
						}
						// ================== ROW 20 ==================
						row = sheet.getRow(19);
						Cell R19cell2 = row.createCell(2);
						if (record.getR20ResAmt() != null) {
							R19cell2.setCellValue(record.getR20ResAmt().doubleValue());
							R19cell2.setCellStyle(numberStyle);
						} else {
							R19cell2.setCellValue("");
							R19cell2.setCellStyle(textStyle);
						}

						Cell R19cell3 = row.createCell(3);
						if (record.getR20NonResAmt() != null) {
							R19cell3.setCellValue(record.getR20NonResAmt().doubleValue());
							R19cell3.setCellStyle(numberStyle);
						} else {
							R19cell3.setCellValue("");
							R19cell3.setCellStyle(textStyle);
						}

						// ================== ROW 21 ==================
						row = sheet.getRow(20);
						Cell R20cell2 = row.createCell(2);
						if (record.getR21ResAmt() != null) {
							R20cell2.setCellValue(record.getR21ResAmt().doubleValue());
							R20cell2.setCellStyle(numberStyle);
						} else {
							R20cell2.setCellValue("");
							R20cell2.setCellStyle(textStyle);
						}

						Cell R20cell3 = row.createCell(3);
						if (record.getR21NonResAmt() != null) {
							R20cell3.setCellValue(record.getR21NonResAmt().doubleValue());
							R20cell3.setCellStyle(numberStyle);
						} else {
							R20cell3.setCellValue("");
							R20cell3.setCellStyle(textStyle);
						}

						// ================== ROW 22 ==================
						row = sheet.getRow(21);
						Cell R21cell2 = row.createCell(2);
						if (record.getR22ResAmt() != null) {
							R21cell2.setCellValue(record.getR22ResAmt().doubleValue());
							R21cell2.setCellStyle(numberStyle);
						} else {
							R21cell2.setCellValue("");
							R21cell2.setCellStyle(textStyle);
						}

						Cell R21cell3 = row.createCell(3);
						if (record.getR22NonResAmt() != null) {
							R21cell3.setCellValue(record.getR22NonResAmt().doubleValue());
							R21cell3.setCellStyle(numberStyle);
						} else {
							R21cell3.setCellValue("");
							R21cell3.setCellStyle(textStyle);
						}

						// ================== ROW 23 ==================
						row = sheet.getRow(22);
						Cell R22cell2 = row.createCell(2);
						if (record.getR23ResAmt() != null) {
							R22cell2.setCellValue(record.getR23ResAmt().doubleValue());
							R22cell2.setCellStyle(numberStyle);
						} else {
							R22cell2.setCellValue("");
							R22cell2.setCellStyle(textStyle);
						}

						Cell R22cell3 = row.createCell(3);
						if (record.getR23NonResAmt() != null) {
							R22cell3.setCellValue(record.getR23NonResAmt().doubleValue());
							R22cell3.setCellStyle(numberStyle);
						} else {
							R22cell3.setCellValue("");
							R22cell3.setCellStyle(textStyle);
						}

						// ================== ROW 24 ==================
						row = sheet.getRow(23);
						Cell R23cell2 = row.createCell(2);
						if (record.getR24ResAmt() != null) {
							R23cell2.setCellValue(record.getR24ResAmt().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);
						}

						Cell R23cell3 = row.createCell(3);
						if (record.getR24NonResAmt() != null) {
							R23cell3.setCellValue(record.getR24NonResAmt().doubleValue());
							R23cell3.setCellStyle(numberStyle);
						} else {
							R23cell3.setCellValue("");
							R23cell3.setCellStyle(textStyle);
						}

						// ================== ROW 25 ==================
						row = sheet.getRow(24);
						Cell R24cell2 = row.createCell(2);
						if (record.getR25ResAmt() != null) {
							R24cell2.setCellValue(record.getR25ResAmt().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);
						}

						Cell R24cell3 = row.createCell(3);
						if (record.getR25NonResAmt() != null) {
							R24cell3.setCellValue(record.getR25NonResAmt().doubleValue());
							R24cell3.setCellStyle(numberStyle);
						} else {
							R24cell3.setCellValue("");
							R24cell3.setCellStyle(textStyle);
						}

						// ================== ROW 26 ==================
						row = sheet.getRow(25);
						Cell R25cell2 = row.createCell(2);
						if (record.getR26ResAmt() != null) {
							R25cell2.setCellValue(record.getR26ResAmt().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						Cell R25cell3 = row.createCell(3);
						if (record.getR26NonResAmt() != null) {
							R25cell3.setCellValue(record.getR26NonResAmt().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}

						// ================== ROW 27 ==================
						row = sheet.getRow(26);
						Cell R26cell2 = row.createCell(2);
						if (record.getR27ResAmt() != null) {
							R26cell2.setCellValue(record.getR27ResAmt().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						Cell R26cell3 = row.createCell(3);
						if (record.getR27NonResAmt() != null) {
							R26cell3.setCellValue(record.getR27NonResAmt().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}

						// ================== ROW 28 ==================
						row = sheet.getRow(27);
						Cell R27cell2 = row.createCell(2);
						if (record.getR28ResAmt() != null) {
							R27cell2.setCellValue(record.getR28ResAmt().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						Cell R27cell3 = row.createCell(3);
						if (record.getR28NonResAmt() != null) {
							R27cell3.setCellValue(record.getR28NonResAmt().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);

						}
						// 29 is Calculation Part
						// ================== ROW 30 ==================
						row = sheet.getRow(29);
						Cell R29cell2 = row.createCell(2);
						if (record.getR30ResAmt() != null) {
							R29cell2.setCellValue(record.getR30ResAmt().doubleValue());
							R29cell2.setCellStyle(numberStyle);
						} else {
							R29cell2.setCellValue("");
							R29cell2.setCellStyle(textStyle);
						}

						Cell R29cell3 = row.createCell(3);
						if (record.getR30NonResAmt() != null) {
							R29cell3.setCellValue(record.getR30NonResAmt().doubleValue());
							R29cell3.setCellStyle(numberStyle);
						} else {
							R29cell3.setCellValue("");
							R29cell3.setCellStyle(textStyle);
						}

						// ================== ROW 31 ==================
						row = sheet.getRow(30);
						Cell R30cell2 = row.createCell(2);
						if (record.getR31ResAmt() != null) {
							R30cell2.setCellValue(record.getR31ResAmt().doubleValue());
							R30cell2.setCellStyle(numberStyle);
						} else {
							R30cell2.setCellValue("");
							R30cell2.setCellStyle(textStyle);
						}

						Cell R30cell3 = row.createCell(3);
						if (record.getR31NonResAmt() != null) {
							R30cell3.setCellValue(record.getR31NonResAmt().doubleValue());
							R30cell3.setCellStyle(numberStyle);
						} else {
							R30cell3.setCellValue("");
							R30cell3.setCellStyle(textStyle);
						}

						// ================== ROW 32 ==================
						row = sheet.getRow(31);
						Cell R31cell2 = row.createCell(2);
						if (record.getR32ResAmt() != null) {
							R31cell2.setCellValue(record.getR32ResAmt().doubleValue());
							R31cell2.setCellStyle(numberStyle);
						} else {
							R31cell2.setCellValue("");
							R31cell2.setCellStyle(textStyle);
						}

						Cell R31cell3 = row.createCell(3);
						if (record.getR32NonResAmt() != null) {
							R31cell3.setCellValue(record.getR32NonResAmt().doubleValue());
							R31cell3.setCellStyle(numberStyle);
						} else {
							R31cell3.setCellValue("");
							R31cell3.setCellStyle(textStyle);
						}
						// 33 is Calculation Part
						// ================== ROW 34 ==================
						row = sheet.getRow(33);
						Cell R33cell2 = row.createCell(2);
						if (record.getR34ResAmt() != null) {
							R33cell2.setCellValue(record.getR34ResAmt().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}

						Cell R33cell3 = row.createCell(3);
						if (record.getR34NonResAmt() != null) {
							R33cell3.setCellValue(record.getR34NonResAmt().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}

						// ================== ROW 35 ==================
						row = sheet.getRow(34);
						Cell R34cell2 = row.createCell(2);
						if (record.getR35ResAmt() != null) {
							R34cell2.setCellValue(record.getR35ResAmt().doubleValue());
							R34cell2.setCellStyle(numberStyle);
						} else {
							R34cell2.setCellValue("");
							R34cell2.setCellStyle(textStyle);
						}

						Cell R34cell3 = row.createCell(3);
						if (record.getR35NonResAmt() != null) {
							R34cell3.setCellValue(record.getR35NonResAmt().doubleValue());
							R34cell3.setCellStyle(numberStyle);
						} else {
							R34cell3.setCellValue("");
							R34cell3.setCellStyle(textStyle);
						}
						// 37 is Calculation Part

						// ================== ROW 36 ==================
						row = sheet.getRow(35);
						Cell R35cell2 = row.createCell(2);
						if (record.getR36ResAmt() != null) {
							R35cell2.setCellValue(record.getR36ResAmt().doubleValue());
							R35cell2.setCellStyle(numberStyle);
						} else {
							R35cell2.setCellValue("");
							R35cell2.setCellStyle(textStyle);
						}

						Cell R35cell3 = row.createCell(3);
						if (record.getR36NonResAmt() != null) {
							R35cell3.setCellValue(record.getR36NonResAmt().doubleValue());
							R35cell3.setCellStyle(numberStyle);
						} else {
							R35cell3.setCellValue("");
							R35cell3.setCellStyle(textStyle);
						}

						// ================== ROW 38 ==================
						row = sheet.getRow(37);
						Cell R37cell2 = row.createCell(2);
						if (record.getR38ResAmt() != null) {
							R37cell2.setCellValue(record.getR38ResAmt().doubleValue());
							R37cell2.setCellStyle(numberStyle);
						} else {
							R37cell2.setCellValue("");
							R37cell2.setCellStyle(textStyle);
						}

						Cell R37cell3 = row.createCell(3);
						if (record.getR38NonResAmt() != null) {
							R37cell3.setCellValue(record.getR38NonResAmt().doubleValue());
							R37cell3.setCellStyle(numberStyle);
						} else {
							R37cell3.setCellValue("");
							R37cell3.setCellStyle(textStyle);
						}

						// ================== ROW 39 ==================
						row = sheet.getRow(38);
						Cell R38cell2 = row.createCell(2);
						if (record.getR39ResAmt() != null) {
							R38cell2.setCellValue(record.getR39ResAmt().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(3);
						if (record.getR39NonResAmt() != null) {
							R38cell3.setCellValue(record.getR39NonResAmt().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ================== ROW 40 ==================
						row = sheet.getRow(39);
						Cell R39cell2 = row.createCell(2);
						if (record.getR40ResAmt() != null) {
							R39cell2.setCellValue(record.getR40ResAmt().doubleValue());
							R39cell2.setCellStyle(numberStyle);
						} else {
							R39cell2.setCellValue("");
							R39cell2.setCellStyle(textStyle);
						}

						Cell R39cell3 = row.createCell(3);
						if (record.getR40NonResAmt() != null) {
							R39cell3.setCellValue(record.getR40NonResAmt().doubleValue());
							R39cell3.setCellStyle(numberStyle);
						} else {
							R39cell3.setCellValue("");
							R39cell3.setCellStyle(textStyle);
						}

						// ================== ROW 41 ==================
						row = sheet.getRow(40);
						Cell R40cell2 = row.createCell(2);
						if (record.getR41ResAmt() != null) {
							R40cell2.setCellValue(record.getR41ResAmt().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(3);
						if (record.getR41NonResAmt() != null) {
							R40cell3.setCellValue(record.getR41NonResAmt().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// Row 42
						row = sheet.getRow(41);
						Cell R41cell2 = row.createCell(2);
						if (record.getR42ResAmt() != null) {
							R41cell2.setCellValue(record.getR42ResAmt().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(3);
						if (record.getR42NonResAmt() != null) {
							R41cell3.setCellValue(record.getR42NonResAmt().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}

						// Row 43
						row = sheet.getRow(42);
						Cell R42cell2 = row.createCell(2);
						if (record.getR43ResAmt() != null) {
							R42cell2.setCellValue(record.getR43ResAmt().doubleValue());
							R42cell2.setCellStyle(numberStyle);
						} else {
							R42cell2.setCellValue("");
							R42cell2.setCellStyle(textStyle);
						}

						Cell R42cell3 = row.createCell(3);
						if (record.getR43NonResAmt() != null) {
							R42cell3.setCellValue(record.getR43NonResAmt().doubleValue());
							R42cell3.setCellStyle(numberStyle);
						} else {
							R42cell3.setCellValue("");
							R42cell3.setCellStyle(textStyle);
						}
						// 44 is Calculation Part

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_loans_Advances SUMMARY", null,
							"BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		logger.info("Email Download");
		if ("email".equalsIgnoreCase(format) && version != null
				&& ("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
			try {
				return BRRS_Q_SMME_loans_AdvancesEmailArchivalExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_SMME_loans_Advances_Archival_Summary_Entity> dataList = getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_loans_Advances new report. Returning empty result.");
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
					Q_SMME_loans_Advances_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(2);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(15);
					// row16
					// Column 1
					Cell R15cell2 = row.createCell(2);
					if (record.getR16ResAmt() != null) {
						R15cell2.setCellValue(record.getR16ResAmt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(3);
					if (record.getR16NonResAmt() != null) {
						R15cell3.setCellValue(record.getR16NonResAmt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					row = sheet.getRow(16);

					Cell R16cell2 = row.createCell(2);
					if (record.getR17ResAmt() != null) {
						R16cell2.setCellValue(record.getR17ResAmt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(3);
					if (record.getR17NonResAmt() != null) {
						R16cell3.setCellValue(record.getR17NonResAmt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					row = sheet.getRow(17);

					Cell R17cell2 = row.createCell(2);
					if (record.getR18ResAmt() != null) {
						R17cell2.setCellValue(record.getR18ResAmt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(3);
					if (record.getR18NonResAmt() != null) {
						R17cell3.setCellValue(record.getR18NonResAmt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					row = sheet.getRow(18);
					Cell R18cell2 = row.createCell(2);
					if (record.getR19ResAmt() != null) {
						R18cell2.setCellValue(record.getR19ResAmt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(3);
					if (record.getR19NonResAmt() != null) {
						R18cell3.setCellValue(record.getR19NonResAmt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					row = sheet.getRow(19);
					Cell R19cell2 = row.createCell(2);
					if (record.getR20ResAmt() != null) {
						R19cell2.setCellValue(record.getR20ResAmt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(3);
					if (record.getR20NonResAmt() != null) {
						R19cell3.setCellValue(record.getR20NonResAmt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(20);
					Cell R20cell2 = row.createCell(2);
					if (record.getR21ResAmt() != null) {
						R20cell2.setCellValue(record.getR21ResAmt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(3);
					if (record.getR21NonResAmt() != null) {
						R20cell3.setCellValue(record.getR21NonResAmt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(21);
					Cell R21cell2 = row.createCell(2);
					if (record.getR22ResAmt() != null) {
						R21cell2.setCellValue(record.getR22ResAmt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(3);
					if (record.getR22NonResAmt() != null) {
						R21cell3.setCellValue(record.getR22NonResAmt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(22);
					Cell R22cell2 = row.createCell(2);
					if (record.getR23ResAmt() != null) {
						R22cell2.setCellValue(record.getR23ResAmt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(3);
					if (record.getR23NonResAmt() != null) {
						R22cell3.setCellValue(record.getR23NonResAmt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(23);
					Cell R23cell2 = row.createCell(2);
					if (record.getR24ResAmt() != null) {
						R23cell2.setCellValue(record.getR24ResAmt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(3);
					if (record.getR24NonResAmt() != null) {
						R23cell3.setCellValue(record.getR24NonResAmt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(24);
					Cell R24cell2 = row.createCell(2);
					if (record.getR25ResAmt() != null) {
						R24cell2.setCellValue(record.getR25ResAmt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(3);
					if (record.getR25NonResAmt() != null) {
						R24cell3.setCellValue(record.getR25NonResAmt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(25);
					Cell R25cell2 = row.createCell(2);
					if (record.getR26ResAmt() != null) {
						R25cell2.setCellValue(record.getR26ResAmt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(3);
					if (record.getR26NonResAmt() != null) {
						R25cell3.setCellValue(record.getR26NonResAmt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(2);
					if (record.getR27ResAmt() != null) {
						R26cell2.setCellValue(record.getR27ResAmt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(3);
					if (record.getR27NonResAmt() != null) {
						R26cell3.setCellValue(record.getR27NonResAmt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(2);
					if (record.getR28ResAmt() != null) {
						R27cell2.setCellValue(record.getR28ResAmt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(3);
					if (record.getR28NonResAmt() != null) {
						R27cell3.setCellValue(record.getR28NonResAmt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(29);
					Cell R29cell2 = row.createCell(2);
					if (record.getR30ResAmt() != null) {
						R29cell2.setCellValue(record.getR30ResAmt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(3);
					if (record.getR30NonResAmt() != null) {
						R29cell3.setCellValue(record.getR30NonResAmt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(30);
					Cell R30cell2 = row.createCell(2);
					if (record.getR31ResAmt() != null) {
						R30cell2.setCellValue(record.getR31ResAmt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(3);
					if (record.getR31NonResAmt() != null) {
						R30cell3.setCellValue(record.getR31NonResAmt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(31);
					Cell R31cell2 = row.createCell(2);
					if (record.getR32ResAmt() != null) {
						R31cell2.setCellValue(record.getR32ResAmt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(3);
					if (record.getR32NonResAmt() != null) {
						R31cell3.setCellValue(record.getR32NonResAmt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(33);
					Cell R33cell2 = row.createCell(2);
					if (record.getR34ResAmt() != null) {
						R33cell2.setCellValue(record.getR34ResAmt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(3);
					if (record.getR34NonResAmt() != null) {
						R33cell3.setCellValue(record.getR34NonResAmt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(34);
					Cell R34cell2 = row.createCell(2);
					if (record.getR35ResAmt() != null) {
						R34cell2.setCellValue(record.getR35ResAmt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(3);
					if (record.getR35NonResAmt() != null) {
						R34cell3.setCellValue(record.getR35NonResAmt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(35);
					Cell R35cell2 = row.createCell(2);
					if (record.getR36ResAmt() != null) {
						R35cell2.setCellValue(record.getR36ResAmt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(3);
					if (record.getR36NonResAmt() != null) {
						R35cell3.setCellValue(record.getR36NonResAmt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(37);
					Cell R37cell2 = row.createCell(2);
					if (record.getR38ResAmt() != null) {
						R37cell2.setCellValue(record.getR38ResAmt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(3);
					if (record.getR38NonResAmt() != null) {
						R37cell3.setCellValue(record.getR38NonResAmt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(38);
					Cell R38cell2 = row.createCell(2);
					if (record.getR39ResAmt() != null) {
						R38cell2.setCellValue(record.getR39ResAmt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(3);
					if (record.getR39NonResAmt() != null) {
						R38cell3.setCellValue(record.getR39NonResAmt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(39);
					Cell R39cell2 = row.createCell(2);
					if (record.getR40ResAmt() != null) {
						R39cell2.setCellValue(record.getR40ResAmt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(3);
					if (record.getR40NonResAmt() != null) {
						R39cell3.setCellValue(record.getR40NonResAmt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(40);
					Cell R40cell2 = row.createCell(2);
					if (record.getR41ResAmt() != null) {
						R40cell2.setCellValue(record.getR41ResAmt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(3);
					if (record.getR41NonResAmt() != null) {
						R40cell3.setCellValue(record.getR41NonResAmt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					Cell R41cell2 = row.createCell(2);
					if (record.getR42ResAmt() != null) {
						R41cell2.setCellValue(record.getR42ResAmt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(3);
					if (record.getR42NonResAmt() != null) {
						R41cell3.setCellValue(record.getR42NonResAmt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					Cell R42cell2 = row.createCell(2);
					if (record.getR43ResAmt() != null) {
						R42cell2.setCellValue(record.getR43ResAmt().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					Cell R42cell3 = row.createCell(3);
					if (record.getR43NonResAmt() != null) {
						R42cell3.setCellValue(record.getR43NonResAmt().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// 44 is Calculation Part

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
	public List<Object[]> getQ_SMME_loans_AdvancesResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Q_SMME_loans_Advances_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_loans_Advances_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				Q_SMME_loans_Advances_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Q_SMME_loans_Advances  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

	// Normal Email Excel
	public byte[] BRRS_Q_SMME_loans_Advances_EmailExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, BigDecimal version) throws Exception {

		List<Q_SMME_loans_Advances_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_SMME_loans_Advances report. Returning empty result.");
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
					Q_SMME_loans_Advances_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(5);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					// AGRI
					// row16
					// Column 1
					Cell R15cell2 = row.createCell(5);
					if (record.getR16ResAmt() != null) {
						R15cell2.setCellValue(record.getR16ResAmt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(6);
					if (record.getR16NonResAmt() != null) {
						R15cell3.setCellValue(record.getR16NonResAmt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					// MINING
					row = sheet.getRow(15);

					Cell R16cell2 = row.createCell(5);
					if (record.getR17ResAmt() != null) {
						R16cell2.setCellValue(record.getR17ResAmt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(6);
					if (record.getR17NonResAmt() != null) {
						R16cell3.setCellValue(record.getR17NonResAmt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					// MANUFACTURING
					row = sheet.getRow(16);

					Cell R17cell2 = row.createCell(5);
					if (record.getR18ResAmt() != null) {
						R17cell2.setCellValue(record.getR18ResAmt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(6);
					if (record.getR18NonResAmt() != null) {
						R17cell3.setCellValue(record.getR18NonResAmt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					// CONSTRUCTION
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(5);
					if (record.getR19ResAmt() != null) {
						R18cell2.setCellValue(record.getR19ResAmt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(6);
					if (record.getR19NonResAmt() != null) {
						R18cell3.setCellValue(record.getR19NonResAmt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					// CRE
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(5);
					if (record.getR20ResAmt() != null) {
						R19cell2.setCellValue(record.getR20ResAmt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(6);
					if (record.getR20NonResAmt() != null) {
						R19cell3.setCellValue(record.getR20NonResAmt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(19);
					// ELECTRICITY
					Cell R20cell2 = row.createCell(5);
					if (record.getR21ResAmt() != null) {
						R20cell2.setCellValue(record.getR21ResAmt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(6);
					if (record.getR21NonResAmt() != null) {
						R20cell3.setCellValue(record.getR21NonResAmt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(20);
					// BUSINESS SERVICE
					Cell R21cell2 = row.createCell(5);
					if (record.getR27ResAmt() != null) {
						R21cell2.setCellValue(record.getR27ResAmt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(6);
					if (record.getR27NonResAmt() != null) {
						R21cell3.setCellValue(record.getR27NonResAmt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(21);
					// TELECOM
					Cell R22cell2 = row.createCell(5);
					if (record.getR23ResAmt() != null) {
						R22cell2.setCellValue(record.getR23ResAmt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(6);
					if (record.getR23NonResAmt() != null) {
						R22cell3.setCellValue(record.getR23NonResAmt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(22);
					// Tourism AND HOTEL
					Cell R23cell2 = row.createCell(5);
					if (record.getR24ResAmt() != null) {
						R23cell2.setCellValue(record.getR24ResAmt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(6);
					if (record.getR24NonResAmt() != null) {
						R23cell3.setCellValue(record.getR24NonResAmt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(23);
					// TRANSPORT
					Cell R24cell2 = row.createCell(5);
					if (record.getR25ResAmt() != null) {
						R24cell2.setCellValue(record.getR25ResAmt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(6);
					if (record.getR25NonResAmt() != null) {
						R24cell3.setCellValue(record.getR25NonResAmt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(24);
					// TRADE,REST,BAR
					Cell R25cell2 = row.createCell(5);
					if (record.getR26ResAmt() != null) {
						R25cell2.setCellValue(record.getR26ResAmt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(6);
					if (record.getR26NonResAmt() != null) {
						R25cell3.setCellValue(record.getR26NonResAmt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);
					Cell R25cell21 = row.createCell(5);
					if (record.getR29ResAmt() != null) {
						R25cell21.setCellValue(record.getR29ResAmt().doubleValue());
						R25cell21.setCellStyle(numberStyle);
					} else {
						R25cell21.setCellValue("");
						R25cell21.setCellStyle(textStyle);
					}

					Cell R2925cell3 = row.createCell(6);
					if (record.getR29NonResAmt() != null) {
						R2925cell3.setCellValue(record.getR29NonResAmt().doubleValue());
						R2925cell3.setCellStyle(numberStyle);
					} else {
						R2925cell3.setCellValue("");
						R2925cell3.setCellStyle(textStyle);
					}
					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(5);
					if (record.getR30ResAmt() != null) {
						R26cell2.setCellValue(record.getR30ResAmt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(6);
					if (record.getR30NonResAmt() != null) {
						R26cell3.setCellValue(record.getR30NonResAmt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(5);
					if (record.getR31ResAmt() != null) {
						R27cell2.setCellValue(record.getR31ResAmt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(6);
					if (record.getR31NonResAmt() != null) {
						R27cell3.setCellValue(record.getR31NonResAmt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(5);
					if (record.getR33ResAmt() != null) {
						R29cell2.setCellValue(record.getR33ResAmt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(6);
					if (record.getR33NonResAmt() != null) {
						R29cell3.setCellValue(record.getR33NonResAmt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(5);
					if (record.getR38ResAmt() != null) {
						R30cell2.setCellValue(record.getR38ResAmt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(6);
					if (record.getR38NonResAmt() != null) {
						R30cell3.setCellValue(record.getR38NonResAmt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(5);
					if (record.getR34ResAmt() != null) {
						R31cell2.setCellValue(record.getR34ResAmt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(6);
					if (record.getR34NonResAmt() != null) {
						R31cell3.setCellValue(record.getR34NonResAmt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(31);
					Cell R33cell2 = row.createCell(5);
					if (record.getR35ResAmt() != null) {
						R33cell2.setCellValue(record.getR35ResAmt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(6);
					if (record.getR35NonResAmt() != null) {
						R33cell3.setCellValue(record.getR35NonResAmt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(32);
					Cell R34cell2 = row.createCell(5);
					if (record.getR36ResAmt() != null) {
						R34cell2.setCellValue(record.getR36ResAmt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(6);
					if (record.getR36NonResAmt() != null) {
						R34cell3.setCellValue(record.getR36NonResAmt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(33);
					Cell R35cell2 = row.createCell(5);
					if (record.getR37ResAmt() != null) {
						R35cell2.setCellValue(record.getR37ResAmt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(6);
					if (record.getR37NonResAmt() != null) {
						R35cell3.setCellValue(record.getR37NonResAmt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(34);
					Cell R37cell2 = row.createCell(5);
					if (record.getR39ResAmt() != null) {
						R37cell2.setCellValue(record.getR39ResAmt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(6);
					if (record.getR39NonResAmt() != null) {
						R37cell3.setCellValue(record.getR39NonResAmt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(35);
					Cell R38cell2 = row.createCell(5);
					if (record.getR40ResAmt() != null) {
						R38cell2.setCellValue(record.getR40ResAmt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(6);
					if (record.getR40NonResAmt() != null) {
						R38cell3.setCellValue(record.getR40NonResAmt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(36);
					Cell R39cell2 = row.createCell(5);
					if (record.getR41ResAmt() != null) {
						R39cell2.setCellValue(record.getR41ResAmt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(6);
					if (record.getR41NonResAmt() != null) {
						R39cell3.setCellValue(record.getR41NonResAmt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(37);
					Cell R40cell2 = row.createCell(5);
					if (record.getR42ResAmt() != null) {
						R40cell2.setCellValue(record.getR42ResAmt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(6);
					if (record.getR42NonResAmt() != null) {
						R40cell3.setCellValue(record.getR42NonResAmt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(38);
					Cell R41cell2 = row.createCell(5);
					if (record.getR43ResAmt() != null) {
						R41cell2.setCellValue(record.getR43ResAmt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(6);
					if (record.getR43NonResAmt() != null) {
						R41cell3.setCellValue(record.getR43NonResAmt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(39);

					Cell R42cell3 = row.createCell(6);
					if (record.getR44NonResAmt() != null) {
						R42cell3.setCellValue(record.getR44NonResAmt().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// 44 is Calculation Part

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_loans_Advances EMAIL SUMMARY", null,
						"BRRS_Q_SMME_LOANS_ADVANCES_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// Archival Email Excel
	public byte[] BRRS_Q_SMME_loans_AdvancesEmailArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_SMME_loans_Advances_Archival_Summary_Entity> dataList = getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_SMME_loans_Advances report. Returning empty result.");
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
					Q_SMME_loans_Advances_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(5);

					if (record.getREPORT_DATE() != null) {

						R12Cell.setCellValue(record.getREPORT_DATE());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					// AGRI
					// row16
					// Column 1
					Cell R15cell2 = row.createCell(5);
					if (record.getR16ResAmt() != null) {
						R15cell2.setCellValue(record.getR16ResAmt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(6);
					if (record.getR16NonResAmt() != null) {
						R15cell3.setCellValue(record.getR16NonResAmt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					// MINING
					row = sheet.getRow(15);

					Cell R16cell2 = row.createCell(5);
					if (record.getR17ResAmt() != null) {
						R16cell2.setCellValue(record.getR17ResAmt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(6);
					if (record.getR17NonResAmt() != null) {
						R16cell3.setCellValue(record.getR17NonResAmt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					// MANUFACTURING
					row = sheet.getRow(16);

					Cell R17cell2 = row.createCell(5);
					if (record.getR18ResAmt() != null) {
						R17cell2.setCellValue(record.getR18ResAmt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(6);
					if (record.getR18NonResAmt() != null) {
						R17cell3.setCellValue(record.getR18NonResAmt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					// CONSTRUCTION
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(5);
					if (record.getR19ResAmt() != null) {
						R18cell2.setCellValue(record.getR19ResAmt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(6);
					if (record.getR19NonResAmt() != null) {
						R18cell3.setCellValue(record.getR19NonResAmt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					// CRE
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(5);
					if (record.getR20ResAmt() != null) {
						R19cell2.setCellValue(record.getR20ResAmt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(6);
					if (record.getR20NonResAmt() != null) {
						R19cell3.setCellValue(record.getR20NonResAmt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(19);
					// ELECTRICITY
					Cell R20cell2 = row.createCell(5);
					if (record.getR21ResAmt() != null) {
						R20cell2.setCellValue(record.getR21ResAmt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(6);
					if (record.getR21NonResAmt() != null) {
						R20cell3.setCellValue(record.getR21NonResAmt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(20);
					// BUSINESS SERVICE
					Cell R21cell2 = row.createCell(5);
					if (record.getR27ResAmt() != null) {
						R21cell2.setCellValue(record.getR27ResAmt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(6);
					if (record.getR27NonResAmt() != null) {
						R21cell3.setCellValue(record.getR27NonResAmt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(21);
					// TELECOM
					Cell R22cell2 = row.createCell(5);
					if (record.getR23ResAmt() != null) {
						R22cell2.setCellValue(record.getR23ResAmt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(6);
					if (record.getR23NonResAmt() != null) {
						R22cell3.setCellValue(record.getR23NonResAmt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(22);
					// Tourism AND HOTEL
					Cell R23cell2 = row.createCell(5);
					if (record.getR24ResAmt() != null) {
						R23cell2.setCellValue(record.getR24ResAmt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(6);
					if (record.getR24NonResAmt() != null) {
						R23cell3.setCellValue(record.getR24NonResAmt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(23);
					// TRANSPORT
					Cell R24cell2 = row.createCell(5);
					if (record.getR25ResAmt() != null) {
						R24cell2.setCellValue(record.getR25ResAmt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(6);
					if (record.getR25NonResAmt() != null) {
						R24cell3.setCellValue(record.getR25NonResAmt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(24);
					// TRADE,REST,BAR
					Cell R25cell2 = row.createCell(5);
					if (record.getR26ResAmt() != null) {
						R25cell2.setCellValue(record.getR26ResAmt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(6);
					if (record.getR26NonResAmt() != null) {
						R25cell3.setCellValue(record.getR26NonResAmt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);
					Cell R25cell21 = row.createCell(5);
					if (record.getR29ResAmt() != null) {
						R25cell21.setCellValue(record.getR29ResAmt().doubleValue());
						R25cell21.setCellStyle(numberStyle);
					} else {
						R25cell21.setCellValue("");
						R25cell21.setCellStyle(textStyle);
					}

					Cell R2925cell3 = row.createCell(6);
					if (record.getR29NonResAmt() != null) {
						R2925cell3.setCellValue(record.getR29NonResAmt().doubleValue());
						R2925cell3.setCellStyle(numberStyle);
					} else {
						R2925cell3.setCellValue("");
						R2925cell3.setCellStyle(textStyle);
					}
					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(5);
					if (record.getR30ResAmt() != null) {
						R26cell2.setCellValue(record.getR30ResAmt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(6);
					if (record.getR30NonResAmt() != null) {
						R26cell3.setCellValue(record.getR30NonResAmt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(5);
					if (record.getR31ResAmt() != null) {
						R27cell2.setCellValue(record.getR31ResAmt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(6);
					if (record.getR31NonResAmt() != null) {
						R27cell3.setCellValue(record.getR31NonResAmt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(5);
					if (record.getR33ResAmt() != null) {
						R29cell2.setCellValue(record.getR33ResAmt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(6);
					if (record.getR33NonResAmt() != null) {
						R29cell3.setCellValue(record.getR33NonResAmt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(5);
					if (record.getR38ResAmt() != null) {
						R30cell2.setCellValue(record.getR38ResAmt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(6);
					if (record.getR38NonResAmt() != null) {
						R30cell3.setCellValue(record.getR38NonResAmt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(5);
					if (record.getR34ResAmt() != null) {
						R31cell2.setCellValue(record.getR34ResAmt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(6);
					if (record.getR34NonResAmt() != null) {
						R31cell3.setCellValue(record.getR34NonResAmt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(31);
					Cell R33cell2 = row.createCell(5);
					if (record.getR35ResAmt() != null) {
						R33cell2.setCellValue(record.getR35ResAmt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(6);
					if (record.getR35NonResAmt() != null) {
						R33cell3.setCellValue(record.getR35NonResAmt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(32);
					Cell R34cell2 = row.createCell(5);
					if (record.getR36ResAmt() != null) {
						R34cell2.setCellValue(record.getR36ResAmt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(6);
					if (record.getR36NonResAmt() != null) {
						R34cell3.setCellValue(record.getR36NonResAmt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(33);
					Cell R35cell2 = row.createCell(5);
					if (record.getR37ResAmt() != null) {
						R35cell2.setCellValue(record.getR37ResAmt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(6);
					if (record.getR37NonResAmt() != null) {
						R35cell3.setCellValue(record.getR37NonResAmt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(34);
					Cell R37cell2 = row.createCell(5);
					if (record.getR39ResAmt() != null) {
						R37cell2.setCellValue(record.getR39ResAmt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(6);
					if (record.getR39NonResAmt() != null) {
						R37cell3.setCellValue(record.getR39NonResAmt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(35);
					Cell R38cell2 = row.createCell(5);
					if (record.getR40ResAmt() != null) {
						R38cell2.setCellValue(record.getR40ResAmt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(6);
					if (record.getR40NonResAmt() != null) {
						R38cell3.setCellValue(record.getR40NonResAmt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(36);
					Cell R39cell2 = row.createCell(5);
					if (record.getR41ResAmt() != null) {
						R39cell2.setCellValue(record.getR41ResAmt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(6);
					if (record.getR41NonResAmt() != null) {
						R39cell3.setCellValue(record.getR41NonResAmt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(37);
					Cell R40cell2 = row.createCell(5);
					if (record.getR42ResAmt() != null) {
						R40cell2.setCellValue(record.getR42ResAmt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(6);
					if (record.getR42NonResAmt() != null) {
						R40cell3.setCellValue(record.getR42NonResAmt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(38);
					Cell R41cell2 = row.createCell(5);
					if (record.getR43ResAmt() != null) {
						R41cell2.setCellValue(record.getR43ResAmt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(6);
					if (record.getR43NonResAmt() != null) {
						R41cell3.setCellValue(record.getR43NonResAmt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(39);

					Cell R42cell3 = row.createCell(6);
					if (record.getR44NonResAmt() != null) {
						R42cell3.setCellValue(record.getR44NonResAmt().doubleValue());
						R42cell3.setCellStyle(numberStyle);
					} else {
						R42cell3.setCellValue("");
						R42cell3.setCellStyle(textStyle);
					}
					// 44 is Calculation Part

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_loans_Advances EMAIL ARCHIVAL SUMMARY",
						null, "BRRS_Q_SMME_LOANS_ADVANCES_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}
}
