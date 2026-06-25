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

@Service
@Transactional
public class BRRS_Q_SMME_Intrest_Income_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_SMME_Intrest_Income_ReportService.class);

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
	public List<Q_SMME_Intrest_Income_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_SMME_Intrest_IncomeRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getQ_SMME_Intrest_IncomeArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<Q_SMME_Intrest_Income_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new Q_SMME_Intrest_IncomeArchivalRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
//GET ALL WITH VERSION

	public List<Q_SMME_Intrest_Income_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Q_SMME_Intrest_IncomeArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<Q_SMME_Intrest_Income_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<Q_SMME_Intrest_Income_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

// 3. PAGINATION

	public List<Q_SMME_Intrest_Income_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<Q_SMME_Intrest_Income_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

	public Q_SMME_Intrest_Income_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

	public Q_SMME_Intrest_Income_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new Q_SMME_Intrest_IncomeDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<Q_SMME_Intrest_Income_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new Q_SMME_Intrest_IncomeArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<Q_SMME_Intrest_Income_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new Q_SMME_Intrest_IncomeArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class Q_SMME_Intrest_IncomeRowMapper implements RowMapper<Q_SMME_Intrest_Income_Summary_Entity> {

		@Override
		public Q_SMME_Intrest_Income_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_Intrest_Income_Summary_Entity obj = new Q_SMME_Intrest_Income_Summary_Entity();

			obj.setR15_caoin(rs.getString("R15_CAOIN"));
			obj.setR15_res_carry_amt(rs.getBigDecimal("R15_RES_CARRY_AMT"));
			obj.setR15_non_res_carry_amt(rs.getBigDecimal("R15_NON_RES_CARRY_AMT"));

			obj.setR16_caoin(rs.getString("R16_CAOIN"));
			obj.setR16_res_carry_amt(rs.getBigDecimal("R16_RES_CARRY_AMT"));
			obj.setR16_non_res_carry_amt(rs.getBigDecimal("R16_NON_RES_CARRY_AMT"));

			obj.setR17_caoin(rs.getString("R17_CAOIN"));
			obj.setR17_res_carry_amt(rs.getBigDecimal("R17_RES_CARRY_AMT"));
			obj.setR17_non_res_carry_amt(rs.getBigDecimal("R17_NON_RES_CARRY_AMT"));

			obj.setR18_caoin(rs.getString("R18_CAOIN"));
			obj.setR18_res_carry_amt(rs.getBigDecimal("R18_RES_CARRY_AMT"));
			obj.setR18_non_res_carry_amt(rs.getBigDecimal("R18_NON_RES_CARRY_AMT"));

			obj.setR19_caoin(rs.getString("R19_CAOIN"));
			obj.setR19_res_carry_amt(rs.getBigDecimal("R19_RES_CARRY_AMT"));
			obj.setR19_non_res_carry_amt(rs.getBigDecimal("R19_NON_RES_CARRY_AMT"));

			obj.setR20_caoin(rs.getString("R20_CAOIN"));
			obj.setR20_res_carry_amt(rs.getBigDecimal("R20_RES_CARRY_AMT"));
			obj.setR20_non_res_carry_amt(rs.getBigDecimal("R20_NON_RES_CARRY_AMT"));

			obj.setR21_caoin(rs.getString("R21_CAOIN"));
			obj.setR21_res_carry_amt(rs.getBigDecimal("R21_RES_CARRY_AMT"));
			obj.setR21_non_res_carry_amt(rs.getBigDecimal("R21_NON_RES_CARRY_AMT"));

			obj.setR22_caoin(rs.getString("R22_CAOIN"));
			obj.setR22_res_carry_amt(rs.getBigDecimal("R22_RES_CARRY_AMT"));
			obj.setR22_non_res_carry_amt(rs.getBigDecimal("R22_NON_RES_CARRY_AMT"));

			obj.setR23_caoin(rs.getString("R23_CAOIN"));
			obj.setR23_res_carry_amt(rs.getBigDecimal("R23_RES_CARRY_AMT"));
			obj.setR23_non_res_carry_amt(rs.getBigDecimal("R23_NON_RES_CARRY_AMT"));

			obj.setR24_caoin(rs.getString("R24_CAOIN"));
			obj.setR24_res_carry_amt(rs.getBigDecimal("R24_RES_CARRY_AMT"));
			obj.setR24_non_res_carry_amt(rs.getBigDecimal("R24_NON_RES_CARRY_AMT"));

			obj.setR25_caoin(rs.getString("R25_CAOIN"));
			obj.setR25_res_carry_amt(rs.getBigDecimal("R25_RES_CARRY_AMT"));
			obj.setR25_non_res_carry_amt(rs.getBigDecimal("R25_NON_RES_CARRY_AMT"));

			obj.setR26_caoin(rs.getString("R26_CAOIN"));
			obj.setR26_res_carry_amt(rs.getBigDecimal("R26_RES_CARRY_AMT"));
			obj.setR26_non_res_carry_amt(rs.getBigDecimal("R26_NON_RES_CARRY_AMT"));

			obj.setR27_caoin(rs.getString("R27_CAOIN"));
			obj.setR27_res_carry_amt(rs.getBigDecimal("R27_RES_CARRY_AMT"));
			obj.setR27_non_res_carry_amt(rs.getBigDecimal("R27_NON_RES_CARRY_AMT"));

			obj.setR28_caoin(rs.getString("R28_CAOIN"));
			obj.setR28_res_carry_amt(rs.getBigDecimal("R28_RES_CARRY_AMT"));
			obj.setR28_non_res_carry_amt(rs.getBigDecimal("R28_NON_RES_CARRY_AMT"));

			obj.setR29_caoin(rs.getString("R29_CAOIN"));
			obj.setR29_res_carry_amt(rs.getBigDecimal("R29_RES_CARRY_AMT"));
			obj.setR29_non_res_carry_amt(rs.getBigDecimal("R29_NON_RES_CARRY_AMT"));

			obj.setR30_caoin(rs.getString("R30_CAOIN"));
			obj.setR30_res_carry_amt(rs.getBigDecimal("R30_RES_CARRY_AMT"));
			obj.setR30_non_res_carry_amt(rs.getBigDecimal("R30_NON_RES_CARRY_AMT"));

			obj.setR31_caoin(rs.getString("R31_CAOIN"));
			obj.setR31_res_carry_amt(rs.getBigDecimal("R31_RES_CARRY_AMT"));
			obj.setR31_non_res_carry_amt(rs.getBigDecimal("R31_NON_RES_CARRY_AMT"));

			obj.setR32_caoin(rs.getString("R32_CAOIN"));
			obj.setR32_res_carry_amt(rs.getBigDecimal("R32_RES_CARRY_AMT"));
			obj.setR32_non_res_carry_amt(rs.getBigDecimal("R32_NON_RES_CARRY_AMT"));

			obj.setR33_caoin(rs.getString("R33_CAOIN"));
			obj.setR33_res_carry_amt(rs.getBigDecimal("R33_RES_CARRY_AMT"));
			obj.setR33_non_res_carry_amt(rs.getBigDecimal("R33_NON_RES_CARRY_AMT"));

			obj.setR34_caoin(rs.getString("R34_CAOIN"));
			obj.setR34_res_carry_amt(rs.getBigDecimal("R34_RES_CARRY_AMT"));
			obj.setR34_non_res_carry_amt(rs.getBigDecimal("R34_NON_RES_CARRY_AMT"));

			obj.setR35_caoin(rs.getString("R35_CAOIN"));
			obj.setR35_res_carry_amt(rs.getBigDecimal("R35_RES_CARRY_AMT"));
			obj.setR35_non_res_carry_amt(rs.getBigDecimal("R35_NON_RES_CARRY_AMT"));

			obj.setR36_caoin(rs.getString("R36_CAOIN"));
			obj.setR36_res_carry_amt(rs.getBigDecimal("R36_RES_CARRY_AMT"));
			obj.setR36_non_res_carry_amt(rs.getBigDecimal("R36_NON_RES_CARRY_AMT"));

			obj.setR37_caoin(rs.getString("R37_CAOIN"));
			obj.setR37_res_carry_amt(rs.getBigDecimal("R37_RES_CARRY_AMT"));
			obj.setR37_non_res_carry_amt(rs.getBigDecimal("R37_NON_RES_CARRY_AMT"));

			obj.setR38_caoin(rs.getString("R38_CAOIN"));
			obj.setR38_res_carry_amt(rs.getBigDecimal("R38_RES_CARRY_AMT"));
			obj.setR38_non_res_carry_amt(rs.getBigDecimal("R38_NON_RES_CARRY_AMT"));

			obj.setR39_caoin(rs.getString("R39_CAOIN"));
			obj.setR39_res_carry_amt(rs.getBigDecimal("R39_RES_CARRY_AMT"));
			obj.setR39_non_res_carry_amt(rs.getBigDecimal("R39_NON_RES_CARRY_AMT"));

			obj.setR40_caoin(rs.getString("R40_CAOIN"));
			obj.setR40_res_carry_amt(rs.getBigDecimal("R40_RES_CARRY_AMT"));
			obj.setR40_non_res_carry_amt(rs.getBigDecimal("R40_NON_RES_CARRY_AMT"));

			obj.setR41_caoin(rs.getString("R41_CAOIN"));
			obj.setR41_res_carry_amt(rs.getBigDecimal("R41_RES_CARRY_AMT"));
			obj.setR41_non_res_carry_amt(rs.getBigDecimal("R41_NON_RES_CARRY_AMT"));

			obj.setR42_caoin(rs.getString("R42_CAOIN"));
			obj.setR42_res_carry_amt(rs.getBigDecimal("R42_RES_CARRY_AMT"));
			obj.setR42_non_res_carry_amt(rs.getBigDecimal("R42_NON_RES_CARRY_AMT"));

			obj.setR43_caoin(rs.getString("R43_CAOIN"));
			obj.setR43_res_carry_amt(rs.getBigDecimal("R43_RES_CARRY_AMT"));
			obj.setR43_non_res_carry_amt(rs.getBigDecimal("R43_NON_RES_CARRY_AMT"));

			obj.setR44_caoin(rs.getString("R44_CAOIN"));
			obj.setR44_res_carry_amt(rs.getBigDecimal("R44_RES_CARRY_AMT"));
			obj.setR44_non_res_carry_amt(rs.getBigDecimal("R44_NON_RES_CARRY_AMT"));

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

	public static class Q_SMME_Intrest_Income_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private String r15_caoin;
		private BigDecimal r15_res_carry_amt;
		private BigDecimal r15_non_res_carry_amt;
		private String r16_caoin;
		private BigDecimal r16_res_carry_amt;
		private BigDecimal r16_non_res_carry_amt;
		private String r17_caoin;
		private BigDecimal r17_res_carry_amt;
		private BigDecimal r17_non_res_carry_amt;
		private String r18_caoin;
		private BigDecimal r18_res_carry_amt;
		private BigDecimal r18_non_res_carry_amt;
		private String r19_caoin;
		private BigDecimal r19_res_carry_amt;
		private BigDecimal r19_non_res_carry_amt;
		private String r20_caoin;
		private BigDecimal r20_res_carry_amt;
		private BigDecimal r20_non_res_carry_amt;
		private String r21_caoin;
		private BigDecimal r21_res_carry_amt;
		private BigDecimal r21_non_res_carry_amt;
		private String r22_caoin;
		private BigDecimal r22_res_carry_amt;
		private BigDecimal r22_non_res_carry_amt;
		private String r23_caoin;
		private BigDecimal r23_res_carry_amt;
		private BigDecimal r23_non_res_carry_amt;
		private String r24_caoin;
		private BigDecimal r24_res_carry_amt;
		private BigDecimal r24_non_res_carry_amt;
		private String r25_caoin;
		private BigDecimal r25_res_carry_amt;
		private BigDecimal r25_non_res_carry_amt;
		private String r26_caoin;
		private BigDecimal r26_res_carry_amt;
		private BigDecimal r26_non_res_carry_amt;
		private String r27_caoin;
		private BigDecimal r27_res_carry_amt;
		private BigDecimal r27_non_res_carry_amt;
		private String r28_caoin;
		private BigDecimal r28_res_carry_amt;
		private BigDecimal r28_non_res_carry_amt;
		private String r29_caoin;
		private BigDecimal r29_res_carry_amt;
		private BigDecimal r29_non_res_carry_amt;
		private String r30_caoin;
		private BigDecimal r30_res_carry_amt;
		private BigDecimal r30_non_res_carry_amt;
		private String r31_caoin;
		private BigDecimal r31_res_carry_amt;
		private BigDecimal r31_non_res_carry_amt;
		private String r32_caoin;
		private BigDecimal r32_res_carry_amt;
		private BigDecimal r32_non_res_carry_amt;
		private String r33_caoin;
		private BigDecimal r33_res_carry_amt;
		private BigDecimal r33_non_res_carry_amt;
		private String r34_caoin;
		private BigDecimal r34_res_carry_amt;
		private BigDecimal r34_non_res_carry_amt;
		private String r35_caoin;
		private BigDecimal r35_res_carry_amt;
		private BigDecimal r35_non_res_carry_amt;
		private String r36_caoin;
		private BigDecimal r36_res_carry_amt;
		private BigDecimal r36_non_res_carry_amt;
		private String r37_caoin;
		private BigDecimal r37_res_carry_amt;
		private BigDecimal r37_non_res_carry_amt;
		private String r38_caoin;
		private BigDecimal r38_res_carry_amt;
		private BigDecimal r38_non_res_carry_amt;
		private String r39_caoin;
		private BigDecimal r39_res_carry_amt;
		private BigDecimal r39_non_res_carry_amt;
		private String r40_caoin;
		private BigDecimal r40_res_carry_amt;
		private BigDecimal r40_non_res_carry_amt;
		private String r41_caoin;
		private BigDecimal r41_res_carry_amt;
		private BigDecimal r41_non_res_carry_amt;
		private String r42_caoin;
		private BigDecimal r42_res_carry_amt;
		private BigDecimal r42_non_res_carry_amt;
		private String r43_caoin;
		private BigDecimal r43_res_carry_amt;
		private BigDecimal r43_non_res_carry_amt;
		private String r44_caoin;
		private BigDecimal r44_res_carry_amt;
		private BigDecimal r44_non_res_carry_amt;

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

		public String getR15_caoin() {
			return r15_caoin;
		}

		public void setR15_caoin(String r15_caoin) {
			this.r15_caoin = r15_caoin;
		}

		public BigDecimal getR15_res_carry_amt() {
			return r15_res_carry_amt;
		}

		public void setR15_res_carry_amt(BigDecimal r15_res_carry_amt) {
			this.r15_res_carry_amt = r15_res_carry_amt;
		}

		public BigDecimal getR15_non_res_carry_amt() {
			return r15_non_res_carry_amt;
		}

		public void setR15_non_res_carry_amt(BigDecimal r15_non_res_carry_amt) {
			this.r15_non_res_carry_amt = r15_non_res_carry_amt;
		}

		public String getR16_caoin() {
			return r16_caoin;
		}

		public void setR16_caoin(String r16_caoin) {
			this.r16_caoin = r16_caoin;
		}

		public BigDecimal getR16_res_carry_amt() {
			return r16_res_carry_amt;
		}

		public void setR16_res_carry_amt(BigDecimal r16_res_carry_amt) {
			this.r16_res_carry_amt = r16_res_carry_amt;
		}

		public BigDecimal getR16_non_res_carry_amt() {
			return r16_non_res_carry_amt;
		}

		public void setR16_non_res_carry_amt(BigDecimal r16_non_res_carry_amt) {
			this.r16_non_res_carry_amt = r16_non_res_carry_amt;
		}

		public String getR17_caoin() {
			return r17_caoin;
		}

		public void setR17_caoin(String r17_caoin) {
			this.r17_caoin = r17_caoin;
		}

		public BigDecimal getR17_res_carry_amt() {
			return r17_res_carry_amt;
		}

		public void setR17_res_carry_amt(BigDecimal r17_res_carry_amt) {
			this.r17_res_carry_amt = r17_res_carry_amt;
		}

		public BigDecimal getR17_non_res_carry_amt() {
			return r17_non_res_carry_amt;
		}

		public void setR17_non_res_carry_amt(BigDecimal r17_non_res_carry_amt) {
			this.r17_non_res_carry_amt = r17_non_res_carry_amt;
		}

		public String getR18_caoin() {
			return r18_caoin;
		}

		public void setR18_caoin(String r18_caoin) {
			this.r18_caoin = r18_caoin;
		}

		public BigDecimal getR18_res_carry_amt() {
			return r18_res_carry_amt;
		}

		public void setR18_res_carry_amt(BigDecimal r18_res_carry_amt) {
			this.r18_res_carry_amt = r18_res_carry_amt;
		}

		public BigDecimal getR18_non_res_carry_amt() {
			return r18_non_res_carry_amt;
		}

		public void setR18_non_res_carry_amt(BigDecimal r18_non_res_carry_amt) {
			this.r18_non_res_carry_amt = r18_non_res_carry_amt;
		}

		public String getR19_caoin() {
			return r19_caoin;
		}

		public void setR19_caoin(String r19_caoin) {
			this.r19_caoin = r19_caoin;
		}

		public BigDecimal getR19_res_carry_amt() {
			return r19_res_carry_amt;
		}

		public void setR19_res_carry_amt(BigDecimal r19_res_carry_amt) {
			this.r19_res_carry_amt = r19_res_carry_amt;
		}

		public BigDecimal getR19_non_res_carry_amt() {
			return r19_non_res_carry_amt;
		}

		public void setR19_non_res_carry_amt(BigDecimal r19_non_res_carry_amt) {
			this.r19_non_res_carry_amt = r19_non_res_carry_amt;
		}

		public String getR20_caoin() {
			return r20_caoin;
		}

		public void setR20_caoin(String r20_caoin) {
			this.r20_caoin = r20_caoin;
		}

		public BigDecimal getR20_res_carry_amt() {
			return r20_res_carry_amt;
		}

		public void setR20_res_carry_amt(BigDecimal r20_res_carry_amt) {
			this.r20_res_carry_amt = r20_res_carry_amt;
		}

		public BigDecimal getR20_non_res_carry_amt() {
			return r20_non_res_carry_amt;
		}

		public void setR20_non_res_carry_amt(BigDecimal r20_non_res_carry_amt) {
			this.r20_non_res_carry_amt = r20_non_res_carry_amt;
		}

		public String getR21_caoin() {
			return r21_caoin;
		}

		public void setR21_caoin(String r21_caoin) {
			this.r21_caoin = r21_caoin;
		}

		public BigDecimal getR21_res_carry_amt() {
			return r21_res_carry_amt;
		}

		public void setR21_res_carry_amt(BigDecimal r21_res_carry_amt) {
			this.r21_res_carry_amt = r21_res_carry_amt;
		}

		public BigDecimal getR21_non_res_carry_amt() {
			return r21_non_res_carry_amt;
		}

		public void setR21_non_res_carry_amt(BigDecimal r21_non_res_carry_amt) {
			this.r21_non_res_carry_amt = r21_non_res_carry_amt;
		}

		public String getR22_caoin() {
			return r22_caoin;
		}

		public void setR22_caoin(String r22_caoin) {
			this.r22_caoin = r22_caoin;
		}

		public BigDecimal getR22_res_carry_amt() {
			return r22_res_carry_amt;
		}

		public void setR22_res_carry_amt(BigDecimal r22_res_carry_amt) {
			this.r22_res_carry_amt = r22_res_carry_amt;
		}

		public BigDecimal getR22_non_res_carry_amt() {
			return r22_non_res_carry_amt;
		}

		public void setR22_non_res_carry_amt(BigDecimal r22_non_res_carry_amt) {
			this.r22_non_res_carry_amt = r22_non_res_carry_amt;
		}

		public String getR23_caoin() {
			return r23_caoin;
		}

		public void setR23_caoin(String r23_caoin) {
			this.r23_caoin = r23_caoin;
		}

		public BigDecimal getR23_res_carry_amt() {
			return r23_res_carry_amt;
		}

		public void setR23_res_carry_amt(BigDecimal r23_res_carry_amt) {
			this.r23_res_carry_amt = r23_res_carry_amt;
		}

		public BigDecimal getR23_non_res_carry_amt() {
			return r23_non_res_carry_amt;
		}

		public void setR23_non_res_carry_amt(BigDecimal r23_non_res_carry_amt) {
			this.r23_non_res_carry_amt = r23_non_res_carry_amt;
		}

		public String getR24_caoin() {
			return r24_caoin;
		}

		public void setR24_caoin(String r24_caoin) {
			this.r24_caoin = r24_caoin;
		}

		public BigDecimal getR24_res_carry_amt() {
			return r24_res_carry_amt;
		}

		public void setR24_res_carry_amt(BigDecimal r24_res_carry_amt) {
			this.r24_res_carry_amt = r24_res_carry_amt;
		}

		public BigDecimal getR24_non_res_carry_amt() {
			return r24_non_res_carry_amt;
		}

		public void setR24_non_res_carry_amt(BigDecimal r24_non_res_carry_amt) {
			this.r24_non_res_carry_amt = r24_non_res_carry_amt;
		}

		public String getR25_caoin() {
			return r25_caoin;
		}

		public void setR25_caoin(String r25_caoin) {
			this.r25_caoin = r25_caoin;
		}

		public BigDecimal getR25_res_carry_amt() {
			return r25_res_carry_amt;
		}

		public void setR25_res_carry_amt(BigDecimal r25_res_carry_amt) {
			this.r25_res_carry_amt = r25_res_carry_amt;
		}

		public BigDecimal getR25_non_res_carry_amt() {
			return r25_non_res_carry_amt;
		}

		public void setR25_non_res_carry_amt(BigDecimal r25_non_res_carry_amt) {
			this.r25_non_res_carry_amt = r25_non_res_carry_amt;
		}

		public String getR26_caoin() {
			return r26_caoin;
		}

		public void setR26_caoin(String r26_caoin) {
			this.r26_caoin = r26_caoin;
		}

		public BigDecimal getR26_res_carry_amt() {
			return r26_res_carry_amt;
		}

		public void setR26_res_carry_amt(BigDecimal r26_res_carry_amt) {
			this.r26_res_carry_amt = r26_res_carry_amt;
		}

		public BigDecimal getR26_non_res_carry_amt() {
			return r26_non_res_carry_amt;
		}

		public void setR26_non_res_carry_amt(BigDecimal r26_non_res_carry_amt) {
			this.r26_non_res_carry_amt = r26_non_res_carry_amt;
		}

		public String getR27_caoin() {
			return r27_caoin;
		}

		public void setR27_caoin(String r27_caoin) {
			this.r27_caoin = r27_caoin;
		}

		public BigDecimal getR27_res_carry_amt() {
			return r27_res_carry_amt;
		}

		public void setR27_res_carry_amt(BigDecimal r27_res_carry_amt) {
			this.r27_res_carry_amt = r27_res_carry_amt;
		}

		public BigDecimal getR27_non_res_carry_amt() {
			return r27_non_res_carry_amt;
		}

		public void setR27_non_res_carry_amt(BigDecimal r27_non_res_carry_amt) {
			this.r27_non_res_carry_amt = r27_non_res_carry_amt;
		}

		public String getR28_caoin() {
			return r28_caoin;
		}

		public void setR28_caoin(String r28_caoin) {
			this.r28_caoin = r28_caoin;
		}

		public BigDecimal getR28_res_carry_amt() {
			return r28_res_carry_amt;
		}

		public void setR28_res_carry_amt(BigDecimal r28_res_carry_amt) {
			this.r28_res_carry_amt = r28_res_carry_amt;
		}

		public BigDecimal getR28_non_res_carry_amt() {
			return r28_non_res_carry_amt;
		}

		public void setR28_non_res_carry_amt(BigDecimal r28_non_res_carry_amt) {
			this.r28_non_res_carry_amt = r28_non_res_carry_amt;
		}

		public String getR29_caoin() {
			return r29_caoin;
		}

		public void setR29_caoin(String r29_caoin) {
			this.r29_caoin = r29_caoin;
		}

		public BigDecimal getR29_res_carry_amt() {
			return r29_res_carry_amt;
		}

		public void setR29_res_carry_amt(BigDecimal r29_res_carry_amt) {
			this.r29_res_carry_amt = r29_res_carry_amt;
		}

		public BigDecimal getR29_non_res_carry_amt() {
			return r29_non_res_carry_amt;
		}

		public void setR29_non_res_carry_amt(BigDecimal r29_non_res_carry_amt) {
			this.r29_non_res_carry_amt = r29_non_res_carry_amt;
		}

		public String getR30_caoin() {
			return r30_caoin;
		}

		public void setR30_caoin(String r30_caoin) {
			this.r30_caoin = r30_caoin;
		}

		public BigDecimal getR30_res_carry_amt() {
			return r30_res_carry_amt;
		}

		public void setR30_res_carry_amt(BigDecimal r30_res_carry_amt) {
			this.r30_res_carry_amt = r30_res_carry_amt;
		}

		public BigDecimal getR30_non_res_carry_amt() {
			return r30_non_res_carry_amt;
		}

		public void setR30_non_res_carry_amt(BigDecimal r30_non_res_carry_amt) {
			this.r30_non_res_carry_amt = r30_non_res_carry_amt;
		}

		public String getR31_caoin() {
			return r31_caoin;
		}

		public void setR31_caoin(String r31_caoin) {
			this.r31_caoin = r31_caoin;
		}

		public BigDecimal getR31_res_carry_amt() {
			return r31_res_carry_amt;
		}

		public void setR31_res_carry_amt(BigDecimal r31_res_carry_amt) {
			this.r31_res_carry_amt = r31_res_carry_amt;
		}

		public BigDecimal getR31_non_res_carry_amt() {
			return r31_non_res_carry_amt;
		}

		public void setR31_non_res_carry_amt(BigDecimal r31_non_res_carry_amt) {
			this.r31_non_res_carry_amt = r31_non_res_carry_amt;
		}

		public String getR32_caoin() {
			return r32_caoin;
		}

		public void setR32_caoin(String r32_caoin) {
			this.r32_caoin = r32_caoin;
		}

		public BigDecimal getR32_res_carry_amt() {
			return r32_res_carry_amt;
		}

		public void setR32_res_carry_amt(BigDecimal r32_res_carry_amt) {
			this.r32_res_carry_amt = r32_res_carry_amt;
		}

		public BigDecimal getR32_non_res_carry_amt() {
			return r32_non_res_carry_amt;
		}

		public void setR32_non_res_carry_amt(BigDecimal r32_non_res_carry_amt) {
			this.r32_non_res_carry_amt = r32_non_res_carry_amt;
		}

		public String getR33_caoin() {
			return r33_caoin;
		}

		public void setR33_caoin(String r33_caoin) {
			this.r33_caoin = r33_caoin;
		}

		public BigDecimal getR33_res_carry_amt() {
			return r33_res_carry_amt;
		}

		public void setR33_res_carry_amt(BigDecimal r33_res_carry_amt) {
			this.r33_res_carry_amt = r33_res_carry_amt;
		}

		public BigDecimal getR33_non_res_carry_amt() {
			return r33_non_res_carry_amt;
		}

		public void setR33_non_res_carry_amt(BigDecimal r33_non_res_carry_amt) {
			this.r33_non_res_carry_amt = r33_non_res_carry_amt;
		}

		public String getR34_caoin() {
			return r34_caoin;
		}

		public void setR34_caoin(String r34_caoin) {
			this.r34_caoin = r34_caoin;
		}

		public BigDecimal getR34_res_carry_amt() {
			return r34_res_carry_amt;
		}

		public void setR34_res_carry_amt(BigDecimal r34_res_carry_amt) {
			this.r34_res_carry_amt = r34_res_carry_amt;
		}

		public BigDecimal getR34_non_res_carry_amt() {
			return r34_non_res_carry_amt;
		}

		public void setR34_non_res_carry_amt(BigDecimal r34_non_res_carry_amt) {
			this.r34_non_res_carry_amt = r34_non_res_carry_amt;
		}

		public String getR35_caoin() {
			return r35_caoin;
		}

		public void setR35_caoin(String r35_caoin) {
			this.r35_caoin = r35_caoin;
		}

		public BigDecimal getR35_res_carry_amt() {
			return r35_res_carry_amt;
		}

		public void setR35_res_carry_amt(BigDecimal r35_res_carry_amt) {
			this.r35_res_carry_amt = r35_res_carry_amt;
		}

		public BigDecimal getR35_non_res_carry_amt() {
			return r35_non_res_carry_amt;
		}

		public void setR35_non_res_carry_amt(BigDecimal r35_non_res_carry_amt) {
			this.r35_non_res_carry_amt = r35_non_res_carry_amt;
		}

		public String getR36_caoin() {
			return r36_caoin;
		}

		public void setR36_caoin(String r36_caoin) {
			this.r36_caoin = r36_caoin;
		}

		public BigDecimal getR36_res_carry_amt() {
			return r36_res_carry_amt;
		}

		public void setR36_res_carry_amt(BigDecimal r36_res_carry_amt) {
			this.r36_res_carry_amt = r36_res_carry_amt;
		}

		public BigDecimal getR36_non_res_carry_amt() {
			return r36_non_res_carry_amt;
		}

		public void setR36_non_res_carry_amt(BigDecimal r36_non_res_carry_amt) {
			this.r36_non_res_carry_amt = r36_non_res_carry_amt;
		}

		public String getR37_caoin() {
			return r37_caoin;
		}

		public void setR37_caoin(String r37_caoin) {
			this.r37_caoin = r37_caoin;
		}

		public BigDecimal getR37_res_carry_amt() {
			return r37_res_carry_amt;
		}

		public void setR37_res_carry_amt(BigDecimal r37_res_carry_amt) {
			this.r37_res_carry_amt = r37_res_carry_amt;
		}

		public BigDecimal getR37_non_res_carry_amt() {
			return r37_non_res_carry_amt;
		}

		public void setR37_non_res_carry_amt(BigDecimal r37_non_res_carry_amt) {
			this.r37_non_res_carry_amt = r37_non_res_carry_amt;
		}

		public String getR38_caoin() {
			return r38_caoin;
		}

		public void setR38_caoin(String r38_caoin) {
			this.r38_caoin = r38_caoin;
		}

		public BigDecimal getR38_res_carry_amt() {
			return r38_res_carry_amt;
		}

		public void setR38_res_carry_amt(BigDecimal r38_res_carry_amt) {
			this.r38_res_carry_amt = r38_res_carry_amt;
		}

		public BigDecimal getR38_non_res_carry_amt() {
			return r38_non_res_carry_amt;
		}

		public void setR38_non_res_carry_amt(BigDecimal r38_non_res_carry_amt) {
			this.r38_non_res_carry_amt = r38_non_res_carry_amt;
		}

		public String getR39_caoin() {
			return r39_caoin;
		}

		public void setR39_caoin(String r39_caoin) {
			this.r39_caoin = r39_caoin;
		}

		public BigDecimal getR39_res_carry_amt() {
			return r39_res_carry_amt;
		}

		public void setR39_res_carry_amt(BigDecimal r39_res_carry_amt) {
			this.r39_res_carry_amt = r39_res_carry_amt;
		}

		public BigDecimal getR39_non_res_carry_amt() {
			return r39_non_res_carry_amt;
		}

		public void setR39_non_res_carry_amt(BigDecimal r39_non_res_carry_amt) {
			this.r39_non_res_carry_amt = r39_non_res_carry_amt;
		}

		public String getR40_caoin() {
			return r40_caoin;
		}

		public void setR40_caoin(String r40_caoin) {
			this.r40_caoin = r40_caoin;
		}

		public BigDecimal getR40_res_carry_amt() {
			return r40_res_carry_amt;
		}

		public void setR40_res_carry_amt(BigDecimal r40_res_carry_amt) {
			this.r40_res_carry_amt = r40_res_carry_amt;
		}

		public BigDecimal getR40_non_res_carry_amt() {
			return r40_non_res_carry_amt;
		}

		public void setR40_non_res_carry_amt(BigDecimal r40_non_res_carry_amt) {
			this.r40_non_res_carry_amt = r40_non_res_carry_amt;
		}

		public String getR41_caoin() {
			return r41_caoin;
		}

		public void setR41_caoin(String r41_caoin) {
			this.r41_caoin = r41_caoin;
		}

		public BigDecimal getR41_res_carry_amt() {
			return r41_res_carry_amt;
		}

		public void setR41_res_carry_amt(BigDecimal r41_res_carry_amt) {
			this.r41_res_carry_amt = r41_res_carry_amt;
		}

		public BigDecimal getR41_non_res_carry_amt() {
			return r41_non_res_carry_amt;
		}

		public void setR41_non_res_carry_amt(BigDecimal r41_non_res_carry_amt) {
			this.r41_non_res_carry_amt = r41_non_res_carry_amt;
		}

		public String getR42_caoin() {
			return r42_caoin;
		}

		public void setR42_caoin(String r42_caoin) {
			this.r42_caoin = r42_caoin;
		}

		public BigDecimal getR42_res_carry_amt() {
			return r42_res_carry_amt;
		}

		public void setR42_res_carry_amt(BigDecimal r42_res_carry_amt) {
			this.r42_res_carry_amt = r42_res_carry_amt;
		}

		public BigDecimal getR42_non_res_carry_amt() {
			return r42_non_res_carry_amt;
		}

		public void setR42_non_res_carry_amt(BigDecimal r42_non_res_carry_amt) {
			this.r42_non_res_carry_amt = r42_non_res_carry_amt;
		}

		public String getR43_caoin() {
			return r43_caoin;
		}

		public void setR43_caoin(String r43_caoin) {
			this.r43_caoin = r43_caoin;
		}

		public BigDecimal getR43_res_carry_amt() {
			return r43_res_carry_amt;
		}

		public void setR43_res_carry_amt(BigDecimal r43_res_carry_amt) {
			this.r43_res_carry_amt = r43_res_carry_amt;
		}

		public BigDecimal getR43_non_res_carry_amt() {
			return r43_non_res_carry_amt;
		}

		public void setR43_non_res_carry_amt(BigDecimal r43_non_res_carry_amt) {
			this.r43_non_res_carry_amt = r43_non_res_carry_amt;
		}

		public String getR44_caoin() {
			return r44_caoin;
		}

		public void setR44_caoin(String r44_caoin) {
			this.r44_caoin = r44_caoin;
		}

		public BigDecimal getR44_res_carry_amt() {
			return r44_res_carry_amt;
		}

		public void setR44_res_carry_amt(BigDecimal r44_res_carry_amt) {
			this.r44_res_carry_amt = r44_res_carry_amt;
		}

		public BigDecimal getR44_non_res_carry_amt() {
			return r44_non_res_carry_amt;
		}

		public void setR44_non_res_carry_amt(BigDecimal r44_non_res_carry_amt) {
			this.r44_non_res_carry_amt = r44_non_res_carry_amt;
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

	class Q_SMME_Intrest_IncomeArchivalRowMapper implements RowMapper<Q_SMME_Intrest_Income_Archival_Summary_Entity> {

		@Override
		public Q_SMME_Intrest_Income_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_Intrest_Income_Archival_Summary_Entity obj = new Q_SMME_Intrest_Income_Archival_Summary_Entity();
			obj.setR15_caoin(rs.getString("R15_CAOIN"));
			obj.setR15_res_carry_amt(rs.getBigDecimal("R15_RES_CARRY_AMT"));
			obj.setR15_non_res_carry_amt(rs.getBigDecimal("R15_NON_RES_CARRY_AMT"));

			obj.setR16_caoin(rs.getString("R16_CAOIN"));
			obj.setR16_res_carry_amt(rs.getBigDecimal("R16_RES_CARRY_AMT"));
			obj.setR16_non_res_carry_amt(rs.getBigDecimal("R16_NON_RES_CARRY_AMT"));

			obj.setR17_caoin(rs.getString("R17_CAOIN"));
			obj.setR17_res_carry_amt(rs.getBigDecimal("R17_RES_CARRY_AMT"));
			obj.setR17_non_res_carry_amt(rs.getBigDecimal("R17_NON_RES_CARRY_AMT"));

			obj.setR18_caoin(rs.getString("R18_CAOIN"));
			obj.setR18_res_carry_amt(rs.getBigDecimal("R18_RES_CARRY_AMT"));
			obj.setR18_non_res_carry_amt(rs.getBigDecimal("R18_NON_RES_CARRY_AMT"));

			obj.setR19_caoin(rs.getString("R19_CAOIN"));
			obj.setR19_res_carry_amt(rs.getBigDecimal("R19_RES_CARRY_AMT"));
			obj.setR19_non_res_carry_amt(rs.getBigDecimal("R19_NON_RES_CARRY_AMT"));

			obj.setR20_caoin(rs.getString("R20_CAOIN"));
			obj.setR20_res_carry_amt(rs.getBigDecimal("R20_RES_CARRY_AMT"));
			obj.setR20_non_res_carry_amt(rs.getBigDecimal("R20_NON_RES_CARRY_AMT"));

			obj.setR21_caoin(rs.getString("R21_CAOIN"));
			obj.setR21_res_carry_amt(rs.getBigDecimal("R21_RES_CARRY_AMT"));
			obj.setR21_non_res_carry_amt(rs.getBigDecimal("R21_NON_RES_CARRY_AMT"));

			obj.setR22_caoin(rs.getString("R22_CAOIN"));
			obj.setR22_res_carry_amt(rs.getBigDecimal("R22_RES_CARRY_AMT"));
			obj.setR22_non_res_carry_amt(rs.getBigDecimal("R22_NON_RES_CARRY_AMT"));

			obj.setR23_caoin(rs.getString("R23_CAOIN"));
			obj.setR23_res_carry_amt(rs.getBigDecimal("R23_RES_CARRY_AMT"));
			obj.setR23_non_res_carry_amt(rs.getBigDecimal("R23_NON_RES_CARRY_AMT"));

			obj.setR24_caoin(rs.getString("R24_CAOIN"));
			obj.setR24_res_carry_amt(rs.getBigDecimal("R24_RES_CARRY_AMT"));
			obj.setR24_non_res_carry_amt(rs.getBigDecimal("R24_NON_RES_CARRY_AMT"));

			obj.setR25_caoin(rs.getString("R25_CAOIN"));
			obj.setR25_res_carry_amt(rs.getBigDecimal("R25_RES_CARRY_AMT"));
			obj.setR25_non_res_carry_amt(rs.getBigDecimal("R25_NON_RES_CARRY_AMT"));

			obj.setR26_caoin(rs.getString("R26_CAOIN"));
			obj.setR26_res_carry_amt(rs.getBigDecimal("R26_RES_CARRY_AMT"));
			obj.setR26_non_res_carry_amt(rs.getBigDecimal("R26_NON_RES_CARRY_AMT"));

			obj.setR27_caoin(rs.getString("R27_CAOIN"));
			obj.setR27_res_carry_amt(rs.getBigDecimal("R27_RES_CARRY_AMT"));
			obj.setR27_non_res_carry_amt(rs.getBigDecimal("R27_NON_RES_CARRY_AMT"));

			obj.setR28_caoin(rs.getString("R28_CAOIN"));
			obj.setR28_res_carry_amt(rs.getBigDecimal("R28_RES_CARRY_AMT"));
			obj.setR28_non_res_carry_amt(rs.getBigDecimal("R28_NON_RES_CARRY_AMT"));

			obj.setR29_caoin(rs.getString("R29_CAOIN"));
			obj.setR29_res_carry_amt(rs.getBigDecimal("R29_RES_CARRY_AMT"));
			obj.setR29_non_res_carry_amt(rs.getBigDecimal("R29_NON_RES_CARRY_AMT"));

			obj.setR30_caoin(rs.getString("R30_CAOIN"));
			obj.setR30_res_carry_amt(rs.getBigDecimal("R30_RES_CARRY_AMT"));
			obj.setR30_non_res_carry_amt(rs.getBigDecimal("R30_NON_RES_CARRY_AMT"));

			obj.setR31_caoin(rs.getString("R31_CAOIN"));
			obj.setR31_res_carry_amt(rs.getBigDecimal("R31_RES_CARRY_AMT"));
			obj.setR31_non_res_carry_amt(rs.getBigDecimal("R31_NON_RES_CARRY_AMT"));

			obj.setR32_caoin(rs.getString("R32_CAOIN"));
			obj.setR32_res_carry_amt(rs.getBigDecimal("R32_RES_CARRY_AMT"));
			obj.setR32_non_res_carry_amt(rs.getBigDecimal("R32_NON_RES_CARRY_AMT"));

			obj.setR33_caoin(rs.getString("R33_CAOIN"));
			obj.setR33_res_carry_amt(rs.getBigDecimal("R33_RES_CARRY_AMT"));
			obj.setR33_non_res_carry_amt(rs.getBigDecimal("R33_NON_RES_CARRY_AMT"));

			obj.setR34_caoin(rs.getString("R34_CAOIN"));
			obj.setR34_res_carry_amt(rs.getBigDecimal("R34_RES_CARRY_AMT"));
			obj.setR34_non_res_carry_amt(rs.getBigDecimal("R34_NON_RES_CARRY_AMT"));

			obj.setR35_caoin(rs.getString("R35_CAOIN"));
			obj.setR35_res_carry_amt(rs.getBigDecimal("R35_RES_CARRY_AMT"));
			obj.setR35_non_res_carry_amt(rs.getBigDecimal("R35_NON_RES_CARRY_AMT"));

			obj.setR36_caoin(rs.getString("R36_CAOIN"));
			obj.setR36_res_carry_amt(rs.getBigDecimal("R36_RES_CARRY_AMT"));
			obj.setR36_non_res_carry_amt(rs.getBigDecimal("R36_NON_RES_CARRY_AMT"));

			obj.setR37_caoin(rs.getString("R37_CAOIN"));
			obj.setR37_res_carry_amt(rs.getBigDecimal("R37_RES_CARRY_AMT"));
			obj.setR37_non_res_carry_amt(rs.getBigDecimal("R37_NON_RES_CARRY_AMT"));

			obj.setR38_caoin(rs.getString("R38_CAOIN"));
			obj.setR38_res_carry_amt(rs.getBigDecimal("R38_RES_CARRY_AMT"));
			obj.setR38_non_res_carry_amt(rs.getBigDecimal("R38_NON_RES_CARRY_AMT"));

			obj.setR39_caoin(rs.getString("R39_CAOIN"));
			obj.setR39_res_carry_amt(rs.getBigDecimal("R39_RES_CARRY_AMT"));
			obj.setR39_non_res_carry_amt(rs.getBigDecimal("R39_NON_RES_CARRY_AMT"));

			obj.setR40_caoin(rs.getString("R40_CAOIN"));
			obj.setR40_res_carry_amt(rs.getBigDecimal("R40_RES_CARRY_AMT"));
			obj.setR40_non_res_carry_amt(rs.getBigDecimal("R40_NON_RES_CARRY_AMT"));

			obj.setR41_caoin(rs.getString("R41_CAOIN"));
			obj.setR41_res_carry_amt(rs.getBigDecimal("R41_RES_CARRY_AMT"));
			obj.setR41_non_res_carry_amt(rs.getBigDecimal("R41_NON_RES_CARRY_AMT"));

			obj.setR42_caoin(rs.getString("R42_CAOIN"));
			obj.setR42_res_carry_amt(rs.getBigDecimal("R42_RES_CARRY_AMT"));
			obj.setR42_non_res_carry_amt(rs.getBigDecimal("R42_NON_RES_CARRY_AMT"));

			obj.setR43_caoin(rs.getString("R43_CAOIN"));
			obj.setR43_res_carry_amt(rs.getBigDecimal("R43_RES_CARRY_AMT"));
			obj.setR43_non_res_carry_amt(rs.getBigDecimal("R43_NON_RES_CARRY_AMT"));

			obj.setR44_caoin(rs.getString("R44_CAOIN"));
			obj.setR44_res_carry_amt(rs.getBigDecimal("R44_RES_CARRY_AMT"));
			obj.setR44_non_res_carry_amt(rs.getBigDecimal("R44_NON_RES_CARRY_AMT"));

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

	@IdClass(Q_SMME_Intrest_Income_PK.class)
	public class Q_SMME_Intrest_Income_Archival_Summary_Entity {

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;

		private String r15_caoin;
		private BigDecimal r15_res_carry_amt;
		private BigDecimal r15_non_res_carry_amt;
		private String r16_caoin;
		private BigDecimal r16_res_carry_amt;
		private BigDecimal r16_non_res_carry_amt;
		private String r17_caoin;
		private BigDecimal r17_res_carry_amt;
		private BigDecimal r17_non_res_carry_amt;
		private String r18_caoin;
		private BigDecimal r18_res_carry_amt;
		private BigDecimal r18_non_res_carry_amt;
		private String r19_caoin;
		private BigDecimal r19_res_carry_amt;
		private BigDecimal r19_non_res_carry_amt;
		private String r20_caoin;
		private BigDecimal r20_res_carry_amt;
		private BigDecimal r20_non_res_carry_amt;
		private String r21_caoin;
		private BigDecimal r21_res_carry_amt;
		private BigDecimal r21_non_res_carry_amt;
		private String r22_caoin;
		private BigDecimal r22_res_carry_amt;
		private BigDecimal r22_non_res_carry_amt;
		private String r23_caoin;
		private BigDecimal r23_res_carry_amt;
		private BigDecimal r23_non_res_carry_amt;
		private String r24_caoin;
		private BigDecimal r24_res_carry_amt;
		private BigDecimal r24_non_res_carry_amt;
		private String r25_caoin;
		private BigDecimal r25_res_carry_amt;
		private BigDecimal r25_non_res_carry_amt;
		private String r26_caoin;
		private BigDecimal r26_res_carry_amt;
		private BigDecimal r26_non_res_carry_amt;
		private String r27_caoin;
		private BigDecimal r27_res_carry_amt;
		private BigDecimal r27_non_res_carry_amt;
		private String r28_caoin;
		private BigDecimal r28_res_carry_amt;
		private BigDecimal r28_non_res_carry_amt;
		private String r29_caoin;
		private BigDecimal r29_res_carry_amt;
		private BigDecimal r29_non_res_carry_amt;
		private String r30_caoin;
		private BigDecimal r30_res_carry_amt;
		private BigDecimal r30_non_res_carry_amt;
		private String r31_caoin;
		private BigDecimal r31_res_carry_amt;
		private BigDecimal r31_non_res_carry_amt;
		private String r32_caoin;
		private BigDecimal r32_res_carry_amt;
		private BigDecimal r32_non_res_carry_amt;
		private String r33_caoin;
		private BigDecimal r33_res_carry_amt;
		private BigDecimal r33_non_res_carry_amt;
		private String r34_caoin;
		private BigDecimal r34_res_carry_amt;
		private BigDecimal r34_non_res_carry_amt;
		private String r35_caoin;
		private BigDecimal r35_res_carry_amt;
		private BigDecimal r35_non_res_carry_amt;
		private String r36_caoin;
		private BigDecimal r36_res_carry_amt;
		private BigDecimal r36_non_res_carry_amt;
		private String r37_caoin;
		private BigDecimal r37_res_carry_amt;
		private BigDecimal r37_non_res_carry_amt;
		private String r38_caoin;
		private BigDecimal r38_res_carry_amt;
		private BigDecimal r38_non_res_carry_amt;
		private String r39_caoin;
		private BigDecimal r39_res_carry_amt;
		private BigDecimal r39_non_res_carry_amt;
		private String r40_caoin;
		private BigDecimal r40_res_carry_amt;
		private BigDecimal r40_non_res_carry_amt;
		private String r41_caoin;
		private BigDecimal r41_res_carry_amt;
		private BigDecimal r41_non_res_carry_amt;
		private String r42_caoin;
		private BigDecimal r42_res_carry_amt;
		private BigDecimal r42_non_res_carry_amt;
		private String r43_caoin;
		private BigDecimal r43_res_carry_amt;
		private BigDecimal r43_non_res_carry_amt;
		private String r44_caoin;
		private BigDecimal r44_res_carry_amt;
		private BigDecimal r44_non_res_carry_amt;
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

		public String getR15_caoin() {
			return r15_caoin;
		}

		public void setR15_caoin(String r15_caoin) {
			this.r15_caoin = r15_caoin;
		}

		public BigDecimal getR15_res_carry_amt() {
			return r15_res_carry_amt;
		}

		public void setR15_res_carry_amt(BigDecimal r15_res_carry_amt) {
			this.r15_res_carry_amt = r15_res_carry_amt;
		}

		public BigDecimal getR15_non_res_carry_amt() {
			return r15_non_res_carry_amt;
		}

		public void setR15_non_res_carry_amt(BigDecimal r15_non_res_carry_amt) {
			this.r15_non_res_carry_amt = r15_non_res_carry_amt;
		}

		public String getR16_caoin() {
			return r16_caoin;
		}

		public void setR16_caoin(String r16_caoin) {
			this.r16_caoin = r16_caoin;
		}

		public BigDecimal getR16_res_carry_amt() {
			return r16_res_carry_amt;
		}

		public void setR16_res_carry_amt(BigDecimal r16_res_carry_amt) {
			this.r16_res_carry_amt = r16_res_carry_amt;
		}

		public BigDecimal getR16_non_res_carry_amt() {
			return r16_non_res_carry_amt;
		}

		public void setR16_non_res_carry_amt(BigDecimal r16_non_res_carry_amt) {
			this.r16_non_res_carry_amt = r16_non_res_carry_amt;
		}

		public String getR17_caoin() {
			return r17_caoin;
		}

		public void setR17_caoin(String r17_caoin) {
			this.r17_caoin = r17_caoin;
		}

		public BigDecimal getR17_res_carry_amt() {
			return r17_res_carry_amt;
		}

		public void setR17_res_carry_amt(BigDecimal r17_res_carry_amt) {
			this.r17_res_carry_amt = r17_res_carry_amt;
		}

		public BigDecimal getR17_non_res_carry_amt() {
			return r17_non_res_carry_amt;
		}

		public void setR17_non_res_carry_amt(BigDecimal r17_non_res_carry_amt) {
			this.r17_non_res_carry_amt = r17_non_res_carry_amt;
		}

		public String getR18_caoin() {
			return r18_caoin;
		}

		public void setR18_caoin(String r18_caoin) {
			this.r18_caoin = r18_caoin;
		}

		public BigDecimal getR18_res_carry_amt() {
			return r18_res_carry_amt;
		}

		public void setR18_res_carry_amt(BigDecimal r18_res_carry_amt) {
			this.r18_res_carry_amt = r18_res_carry_amt;
		}

		public BigDecimal getR18_non_res_carry_amt() {
			return r18_non_res_carry_amt;
		}

		public void setR18_non_res_carry_amt(BigDecimal r18_non_res_carry_amt) {
			this.r18_non_res_carry_amt = r18_non_res_carry_amt;
		}

		public String getR19_caoin() {
			return r19_caoin;
		}

		public void setR19_caoin(String r19_caoin) {
			this.r19_caoin = r19_caoin;
		}

		public BigDecimal getR19_res_carry_amt() {
			return r19_res_carry_amt;
		}

		public void setR19_res_carry_amt(BigDecimal r19_res_carry_amt) {
			this.r19_res_carry_amt = r19_res_carry_amt;
		}

		public BigDecimal getR19_non_res_carry_amt() {
			return r19_non_res_carry_amt;
		}

		public void setR19_non_res_carry_amt(BigDecimal r19_non_res_carry_amt) {
			this.r19_non_res_carry_amt = r19_non_res_carry_amt;
		}

		public String getR20_caoin() {
			return r20_caoin;
		}

		public void setR20_caoin(String r20_caoin) {
			this.r20_caoin = r20_caoin;
		}

		public BigDecimal getR20_res_carry_amt() {
			return r20_res_carry_amt;
		}

		public void setR20_res_carry_amt(BigDecimal r20_res_carry_amt) {
			this.r20_res_carry_amt = r20_res_carry_amt;
		}

		public BigDecimal getR20_non_res_carry_amt() {
			return r20_non_res_carry_amt;
		}

		public void setR20_non_res_carry_amt(BigDecimal r20_non_res_carry_amt) {
			this.r20_non_res_carry_amt = r20_non_res_carry_amt;
		}

		public String getR21_caoin() {
			return r21_caoin;
		}

		public void setR21_caoin(String r21_caoin) {
			this.r21_caoin = r21_caoin;
		}

		public BigDecimal getR21_res_carry_amt() {
			return r21_res_carry_amt;
		}

		public void setR21_res_carry_amt(BigDecimal r21_res_carry_amt) {
			this.r21_res_carry_amt = r21_res_carry_amt;
		}

		public BigDecimal getR21_non_res_carry_amt() {
			return r21_non_res_carry_amt;
		}

		public void setR21_non_res_carry_amt(BigDecimal r21_non_res_carry_amt) {
			this.r21_non_res_carry_amt = r21_non_res_carry_amt;
		}

		public String getR22_caoin() {
			return r22_caoin;
		}

		public void setR22_caoin(String r22_caoin) {
			this.r22_caoin = r22_caoin;
		}

		public BigDecimal getR22_res_carry_amt() {
			return r22_res_carry_amt;
		}

		public void setR22_res_carry_amt(BigDecimal r22_res_carry_amt) {
			this.r22_res_carry_amt = r22_res_carry_amt;
		}

		public BigDecimal getR22_non_res_carry_amt() {
			return r22_non_res_carry_amt;
		}

		public void setR22_non_res_carry_amt(BigDecimal r22_non_res_carry_amt) {
			this.r22_non_res_carry_amt = r22_non_res_carry_amt;
		}

		public String getR23_caoin() {
			return r23_caoin;
		}

		public void setR23_caoin(String r23_caoin) {
			this.r23_caoin = r23_caoin;
		}

		public BigDecimal getR23_res_carry_amt() {
			return r23_res_carry_amt;
		}

		public void setR23_res_carry_amt(BigDecimal r23_res_carry_amt) {
			this.r23_res_carry_amt = r23_res_carry_amt;
		}

		public BigDecimal getR23_non_res_carry_amt() {
			return r23_non_res_carry_amt;
		}

		public void setR23_non_res_carry_amt(BigDecimal r23_non_res_carry_amt) {
			this.r23_non_res_carry_amt = r23_non_res_carry_amt;
		}

		public String getR24_caoin() {
			return r24_caoin;
		}

		public void setR24_caoin(String r24_caoin) {
			this.r24_caoin = r24_caoin;
		}

		public BigDecimal getR24_res_carry_amt() {
			return r24_res_carry_amt;
		}

		public void setR24_res_carry_amt(BigDecimal r24_res_carry_amt) {
			this.r24_res_carry_amt = r24_res_carry_amt;
		}

		public BigDecimal getR24_non_res_carry_amt() {
			return r24_non_res_carry_amt;
		}

		public void setR24_non_res_carry_amt(BigDecimal r24_non_res_carry_amt) {
			this.r24_non_res_carry_amt = r24_non_res_carry_amt;
		}

		public String getR25_caoin() {
			return r25_caoin;
		}

		public void setR25_caoin(String r25_caoin) {
			this.r25_caoin = r25_caoin;
		}

		public BigDecimal getR25_res_carry_amt() {
			return r25_res_carry_amt;
		}

		public void setR25_res_carry_amt(BigDecimal r25_res_carry_amt) {
			this.r25_res_carry_amt = r25_res_carry_amt;
		}

		public BigDecimal getR25_non_res_carry_amt() {
			return r25_non_res_carry_amt;
		}

		public void setR25_non_res_carry_amt(BigDecimal r25_non_res_carry_amt) {
			this.r25_non_res_carry_amt = r25_non_res_carry_amt;
		}

		public String getR26_caoin() {
			return r26_caoin;
		}

		public void setR26_caoin(String r26_caoin) {
			this.r26_caoin = r26_caoin;
		}

		public BigDecimal getR26_res_carry_amt() {
			return r26_res_carry_amt;
		}

		public void setR26_res_carry_amt(BigDecimal r26_res_carry_amt) {
			this.r26_res_carry_amt = r26_res_carry_amt;
		}

		public BigDecimal getR26_non_res_carry_amt() {
			return r26_non_res_carry_amt;
		}

		public void setR26_non_res_carry_amt(BigDecimal r26_non_res_carry_amt) {
			this.r26_non_res_carry_amt = r26_non_res_carry_amt;
		}

		public String getR27_caoin() {
			return r27_caoin;
		}

		public void setR27_caoin(String r27_caoin) {
			this.r27_caoin = r27_caoin;
		}

		public BigDecimal getR27_res_carry_amt() {
			return r27_res_carry_amt;
		}

		public void setR27_res_carry_amt(BigDecimal r27_res_carry_amt) {
			this.r27_res_carry_amt = r27_res_carry_amt;
		}

		public BigDecimal getR27_non_res_carry_amt() {
			return r27_non_res_carry_amt;
		}

		public void setR27_non_res_carry_amt(BigDecimal r27_non_res_carry_amt) {
			this.r27_non_res_carry_amt = r27_non_res_carry_amt;
		}

		public String getR28_caoin() {
			return r28_caoin;
		}

		public void setR28_caoin(String r28_caoin) {
			this.r28_caoin = r28_caoin;
		}

		public BigDecimal getR28_res_carry_amt() {
			return r28_res_carry_amt;
		}

		public void setR28_res_carry_amt(BigDecimal r28_res_carry_amt) {
			this.r28_res_carry_amt = r28_res_carry_amt;
		}

		public BigDecimal getR28_non_res_carry_amt() {
			return r28_non_res_carry_amt;
		}

		public void setR28_non_res_carry_amt(BigDecimal r28_non_res_carry_amt) {
			this.r28_non_res_carry_amt = r28_non_res_carry_amt;
		}

		public String getR29_caoin() {
			return r29_caoin;
		}

		public void setR29_caoin(String r29_caoin) {
			this.r29_caoin = r29_caoin;
		}

		public BigDecimal getR29_res_carry_amt() {
			return r29_res_carry_amt;
		}

		public void setR29_res_carry_amt(BigDecimal r29_res_carry_amt) {
			this.r29_res_carry_amt = r29_res_carry_amt;
		}

		public BigDecimal getR29_non_res_carry_amt() {
			return r29_non_res_carry_amt;
		}

		public void setR29_non_res_carry_amt(BigDecimal r29_non_res_carry_amt) {
			this.r29_non_res_carry_amt = r29_non_res_carry_amt;
		}

		public String getR30_caoin() {
			return r30_caoin;
		}

		public void setR30_caoin(String r30_caoin) {
			this.r30_caoin = r30_caoin;
		}

		public BigDecimal getR30_res_carry_amt() {
			return r30_res_carry_amt;
		}

		public void setR30_res_carry_amt(BigDecimal r30_res_carry_amt) {
			this.r30_res_carry_amt = r30_res_carry_amt;
		}

		public BigDecimal getR30_non_res_carry_amt() {
			return r30_non_res_carry_amt;
		}

		public void setR30_non_res_carry_amt(BigDecimal r30_non_res_carry_amt) {
			this.r30_non_res_carry_amt = r30_non_res_carry_amt;
		}

		public String getR31_caoin() {
			return r31_caoin;
		}

		public void setR31_caoin(String r31_caoin) {
			this.r31_caoin = r31_caoin;
		}

		public BigDecimal getR31_res_carry_amt() {
			return r31_res_carry_amt;
		}

		public void setR31_res_carry_amt(BigDecimal r31_res_carry_amt) {
			this.r31_res_carry_amt = r31_res_carry_amt;
		}

		public BigDecimal getR31_non_res_carry_amt() {
			return r31_non_res_carry_amt;
		}

		public void setR31_non_res_carry_amt(BigDecimal r31_non_res_carry_amt) {
			this.r31_non_res_carry_amt = r31_non_res_carry_amt;
		}

		public String getR32_caoin() {
			return r32_caoin;
		}

		public void setR32_caoin(String r32_caoin) {
			this.r32_caoin = r32_caoin;
		}

		public BigDecimal getR32_res_carry_amt() {
			return r32_res_carry_amt;
		}

		public void setR32_res_carry_amt(BigDecimal r32_res_carry_amt) {
			this.r32_res_carry_amt = r32_res_carry_amt;
		}

		public BigDecimal getR32_non_res_carry_amt() {
			return r32_non_res_carry_amt;
		}

		public void setR32_non_res_carry_amt(BigDecimal r32_non_res_carry_amt) {
			this.r32_non_res_carry_amt = r32_non_res_carry_amt;
		}

		public String getR33_caoin() {
			return r33_caoin;
		}

		public void setR33_caoin(String r33_caoin) {
			this.r33_caoin = r33_caoin;
		}

		public BigDecimal getR33_res_carry_amt() {
			return r33_res_carry_amt;
		}

		public void setR33_res_carry_amt(BigDecimal r33_res_carry_amt) {
			this.r33_res_carry_amt = r33_res_carry_amt;
		}

		public BigDecimal getR33_non_res_carry_amt() {
			return r33_non_res_carry_amt;
		}

		public void setR33_non_res_carry_amt(BigDecimal r33_non_res_carry_amt) {
			this.r33_non_res_carry_amt = r33_non_res_carry_amt;
		}

		public String getR34_caoin() {
			return r34_caoin;
		}

		public void setR34_caoin(String r34_caoin) {
			this.r34_caoin = r34_caoin;
		}

		public BigDecimal getR34_res_carry_amt() {
			return r34_res_carry_amt;
		}

		public void setR34_res_carry_amt(BigDecimal r34_res_carry_amt) {
			this.r34_res_carry_amt = r34_res_carry_amt;
		}

		public BigDecimal getR34_non_res_carry_amt() {
			return r34_non_res_carry_amt;
		}

		public void setR34_non_res_carry_amt(BigDecimal r34_non_res_carry_amt) {
			this.r34_non_res_carry_amt = r34_non_res_carry_amt;
		}

		public String getR35_caoin() {
			return r35_caoin;
		}

		public void setR35_caoin(String r35_caoin) {
			this.r35_caoin = r35_caoin;
		}

		public BigDecimal getR35_res_carry_amt() {
			return r35_res_carry_amt;
		}

		public void setR35_res_carry_amt(BigDecimal r35_res_carry_amt) {
			this.r35_res_carry_amt = r35_res_carry_amt;
		}

		public BigDecimal getR35_non_res_carry_amt() {
			return r35_non_res_carry_amt;
		}

		public void setR35_non_res_carry_amt(BigDecimal r35_non_res_carry_amt) {
			this.r35_non_res_carry_amt = r35_non_res_carry_amt;
		}

		public String getR36_caoin() {
			return r36_caoin;
		}

		public void setR36_caoin(String r36_caoin) {
			this.r36_caoin = r36_caoin;
		}

		public BigDecimal getR36_res_carry_amt() {
			return r36_res_carry_amt;
		}

		public void setR36_res_carry_amt(BigDecimal r36_res_carry_amt) {
			this.r36_res_carry_amt = r36_res_carry_amt;
		}

		public BigDecimal getR36_non_res_carry_amt() {
			return r36_non_res_carry_amt;
		}

		public void setR36_non_res_carry_amt(BigDecimal r36_non_res_carry_amt) {
			this.r36_non_res_carry_amt = r36_non_res_carry_amt;
		}

		public String getR37_caoin() {
			return r37_caoin;
		}

		public void setR37_caoin(String r37_caoin) {
			this.r37_caoin = r37_caoin;
		}

		public BigDecimal getR37_res_carry_amt() {
			return r37_res_carry_amt;
		}

		public void setR37_res_carry_amt(BigDecimal r37_res_carry_amt) {
			this.r37_res_carry_amt = r37_res_carry_amt;
		}

		public BigDecimal getR37_non_res_carry_amt() {
			return r37_non_res_carry_amt;
		}

		public void setR37_non_res_carry_amt(BigDecimal r37_non_res_carry_amt) {
			this.r37_non_res_carry_amt = r37_non_res_carry_amt;
		}

		public String getR38_caoin() {
			return r38_caoin;
		}

		public void setR38_caoin(String r38_caoin) {
			this.r38_caoin = r38_caoin;
		}

		public BigDecimal getR38_res_carry_amt() {
			return r38_res_carry_amt;
		}

		public void setR38_res_carry_amt(BigDecimal r38_res_carry_amt) {
			this.r38_res_carry_amt = r38_res_carry_amt;
		}

		public BigDecimal getR38_non_res_carry_amt() {
			return r38_non_res_carry_amt;
		}

		public void setR38_non_res_carry_amt(BigDecimal r38_non_res_carry_amt) {
			this.r38_non_res_carry_amt = r38_non_res_carry_amt;
		}

		public String getR39_caoin() {
			return r39_caoin;
		}

		public void setR39_caoin(String r39_caoin) {
			this.r39_caoin = r39_caoin;
		}

		public BigDecimal getR39_res_carry_amt() {
			return r39_res_carry_amt;
		}

		public void setR39_res_carry_amt(BigDecimal r39_res_carry_amt) {
			this.r39_res_carry_amt = r39_res_carry_amt;
		}

		public BigDecimal getR39_non_res_carry_amt() {
			return r39_non_res_carry_amt;
		}

		public void setR39_non_res_carry_amt(BigDecimal r39_non_res_carry_amt) {
			this.r39_non_res_carry_amt = r39_non_res_carry_amt;
		}

		public String getR40_caoin() {
			return r40_caoin;
		}

		public void setR40_caoin(String r40_caoin) {
			this.r40_caoin = r40_caoin;
		}

		public BigDecimal getR40_res_carry_amt() {
			return r40_res_carry_amt;
		}

		public void setR40_res_carry_amt(BigDecimal r40_res_carry_amt) {
			this.r40_res_carry_amt = r40_res_carry_amt;
		}

		public BigDecimal getR40_non_res_carry_amt() {
			return r40_non_res_carry_amt;
		}

		public void setR40_non_res_carry_amt(BigDecimal r40_non_res_carry_amt) {
			this.r40_non_res_carry_amt = r40_non_res_carry_amt;
		}

		public String getR41_caoin() {
			return r41_caoin;
		}

		public void setR41_caoin(String r41_caoin) {
			this.r41_caoin = r41_caoin;
		}

		public BigDecimal getR41_res_carry_amt() {
			return r41_res_carry_amt;
		}

		public void setR41_res_carry_amt(BigDecimal r41_res_carry_amt) {
			this.r41_res_carry_amt = r41_res_carry_amt;
		}

		public BigDecimal getR41_non_res_carry_amt() {
			return r41_non_res_carry_amt;
		}

		public void setR41_non_res_carry_amt(BigDecimal r41_non_res_carry_amt) {
			this.r41_non_res_carry_amt = r41_non_res_carry_amt;
		}

		public String getR42_caoin() {
			return r42_caoin;
		}

		public void setR42_caoin(String r42_caoin) {
			this.r42_caoin = r42_caoin;
		}

		public BigDecimal getR42_res_carry_amt() {
			return r42_res_carry_amt;
		}

		public void setR42_res_carry_amt(BigDecimal r42_res_carry_amt) {
			this.r42_res_carry_amt = r42_res_carry_amt;
		}

		public BigDecimal getR42_non_res_carry_amt() {
			return r42_non_res_carry_amt;
		}

		public void setR42_non_res_carry_amt(BigDecimal r42_non_res_carry_amt) {
			this.r42_non_res_carry_amt = r42_non_res_carry_amt;
		}

		public String getR43_caoin() {
			return r43_caoin;
		}

		public void setR43_caoin(String r43_caoin) {
			this.r43_caoin = r43_caoin;
		}

		public BigDecimal getR43_res_carry_amt() {
			return r43_res_carry_amt;
		}

		public void setR43_res_carry_amt(BigDecimal r43_res_carry_amt) {
			this.r43_res_carry_amt = r43_res_carry_amt;
		}

		public BigDecimal getR43_non_res_carry_amt() {
			return r43_non_res_carry_amt;
		}

		public void setR43_non_res_carry_amt(BigDecimal r43_non_res_carry_amt) {
			this.r43_non_res_carry_amt = r43_non_res_carry_amt;
		}

		public String getR44_caoin() {
			return r44_caoin;
		}

		public void setR44_caoin(String r44_caoin) {
			this.r44_caoin = r44_caoin;
		}

		public BigDecimal getR44_res_carry_amt() {
			return r44_res_carry_amt;
		}

		public void setR44_res_carry_amt(BigDecimal r44_res_carry_amt) {
			this.r44_res_carry_amt = r44_res_carry_amt;
		}

		public BigDecimal getR44_non_res_carry_amt() {
			return r44_non_res_carry_amt;
		}

		public void setR44_non_res_carry_amt(BigDecimal r44_non_res_carry_amt) {
			this.r44_non_res_carry_amt = r44_non_res_carry_amt;
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

	public static class Q_SMME_Intrest_Income_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public Q_SMME_Intrest_Income_PK() {
		}

		public Q_SMME_Intrest_Income_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Q_SMME_Intrest_Income_PK))
				return false;
			Q_SMME_Intrest_Income_PK that = (Q_SMME_Intrest_Income_PK) o;
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

	public class Q_SMME_Intrest_Income_Detail_Entity {

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

		@Column(name = "MONTHLY_INTEREST", precision = 24, scale = 2)
		private BigDecimal monthlyInterest;

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

		public BigDecimal getMonthlyInterest() {
			return monthlyInterest;
		}

		public void setMonthlyInterest(BigDecimal monthlyInterest) {
			this.monthlyInterest = monthlyInterest;
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

	class Q_SMME_Intrest_IncomeDetailRowMapper implements RowMapper<Q_SMME_Intrest_Income_Detail_Entity> {

		@Override
		public Q_SMME_Intrest_Income_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_Intrest_Income_Detail_Entity obj = new Q_SMME_Intrest_Income_Detail_Entity();
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
			obj.setMonthlyInterest(rs.getBigDecimal("MONTHLY_INTEREST"));
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

	class Q_SMME_Intrest_IncomeArchivalDetailRowMapper
			implements RowMapper<Q_SMME_Intrest_Income_Archival_Detail_Entity> {

		@Override
		public Q_SMME_Intrest_Income_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_SMME_Intrest_Income_Archival_Detail_Entity obj = new Q_SMME_Intrest_Income_Archival_Detail_Entity();
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
			obj.setMonthlyInterest(rs.getBigDecimal("MONTHLY_INTEREST"));
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

	public class Q_SMME_Intrest_Income_Archival_Detail_Entity {
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

		@Column(name = "MONTHLY_INTEREST", precision = 24, scale = 2)
		private BigDecimal monthlyInterest;

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

		public BigDecimal getMonthlyInterest() {
			return monthlyInterest;
		}

		public void setMonthlyInterest(BigDecimal monthlyInterest) {
			this.monthlyInterest = monthlyInterest;
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

		System.out.println("Q_SMME_Intrest_Income View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<Q_SMME_Intrest_Income_Archival_Summary_Entity> T1Master = new ArrayList<>();

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

			List<Q_SMME_Intrest_Income_Summary_Entity> T1Master = new ArrayList<>();

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

		mv.setViewName("BRRS/Q_SMME");
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

				List<Q_SMME_Intrest_Income_Archival_Detail_Entity> detailList;

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

				List<Q_SMME_Intrest_Income_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/Q_SMME");
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

			List<Q_SMME_Intrest_Income_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_Intrest_Income_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				Q_SMME_Intrest_Income_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Q_SMME_Intrest_Income  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/Q_SMME");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				Q_SMME_Intrest_Income_Detail_Entity Q_SMME_Intrest_IncomeEntity = findBySnoArch(SNO);
				if (Q_SMME_Intrest_IncomeEntity != null && Q_SMME_Intrest_IncomeEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(Q_SMME_Intrest_IncomeEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("Q_SMME_Intrest_IncomeData", Q_SMME_Intrest_IncomeEntity);
			} else {
				Q_SMME_Intrest_Income_Detail_Entity Q_SMME_Intrest_IncomeEntity = findBySno(SNO);
				if (Q_SMME_Intrest_IncomeEntity != null && Q_SMME_Intrest_IncomeEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(Q_SMME_Intrest_IncomeEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("Q_SMME_Intrest_IncomeData", Q_SMME_Intrest_IncomeEntity);
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
			Q_SMME_Intrest_Income_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBySnoArch(Sno);
			} else {
				existing = findBySno(Sno);
			}
			Q_SMME_Intrest_Income_Detail_Entity oldcopy = new Q_SMME_Intrest_Income_Detail_Entity();
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
					sql = "UPDATE BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				} else {
					sql = "UPDATE BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ?" + //
							"WHERE SNO = ?";
				}
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInPula(), Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Q_SMME_Intrest_Income Archival Screen",
							"BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "Q_SMME_Intrest_Income Screen",
							"BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_Q_SMME_Intrest_Income_Procudure(reportDateStr, type, entry);

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
			Run_Q_SMME_Intrest_Income_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_Q_SMME_Intrest_Income_Procudure(String reportDateStr, String type, String entry) {

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
						String bdsql = "DELETE FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, formattedDate);
						System.out.println("Successfully deleted before executing procedure " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE ("
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, CUST_ID, ACCT_NUMBER, ACCT_BALANCE_IN_PULA, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

						int rowsInserted = jdbcTemplate.update(sqltransfer, formattedDate);
						System.out.println("Successfully transferred " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_Q_SMME_INTREST_INCOME_SUMMARY_PROCEDURE(?); END;",
								formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_Q_SMME_INTREST_INCOME_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
						System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, formattedDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						StringBuilder columnsPart = new StringBuilder();
						String[] tokens = { "CAOIN", "RES_CARRY_AMT", "NON_RES_CARRY_AMT" };

						// Dynamically generate R6 to R62 columns
						for (int i = 15; i <= 44; i++) {
							for (String token : tokens) {
								columnsPart.append("R").append(i).append("_").append(token).append(", ");
							}
						}

						// Build the final query cleanly - Notice the '?' replacing REPORT_VERSION in
						// SELECT
						String finalsql = "INSERT INTO BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY ("
								+ columnsPart.toString()
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
								+ "SELECT " + columnsPart.toString()
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, formattedDate);
						System.out.println("Successfully transferred " + rowsInsertedSum + " rows.");

						String adsumsql = "DELETE FROM BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE WHERE REPORT_DATE = ?";
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
			logger.info("Generating Excel for  Q_SMME_Intrest_Income Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Q_SMME_Intrest_IncomeDetailsDetail");

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
			List<Q_SMME_Intrest_Income_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_SMME_Intrest_Income_Detail_Entity item : reportData) {
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
				logger.info("No data found for Q_SMME_Intrest_Income — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Q_SMME_Intrest_Income Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for Q_SMME_Intrest_Income ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Q_SMME_Intrest_Income Detail NEW");

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
			List<Q_SMME_Intrest_Income_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(
					parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (Q_SMME_Intrest_Income_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for Q_SMME_Intrest_Income — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating Q_SMME_Intrest_Income NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getQ_SMMEExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.Q_SMME_Intrest_Income");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getSummaryExcelARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		if ("email".equalsIgnoreCase(format) && version == null) {
			logger.info("Got format as Email");
			logger.info("Service: Generating Email report for version {}", version);
			return BRRS_Q_SMME_Intrest_Income_EmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
					format, version);
		} else {
			List<Q_SMME_Intrest_Income_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

			System.out.println("DATA SIZE IS : " + dataList.size());
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for  Q_SMME_Intrest_Income report. Returning empty result.");
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
						Q_SMME_Intrest_Income_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(1);

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
						Cell R15cell2 = row.createCell(1);
						if (record.getR16_res_carry_amt() != null) {
							R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R15cell3 = row.createCell(2);
						if (record.getR16_non_res_carry_amt() != null) {
							R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}

						// row17
						// Column 1
						row = sheet.getRow(16);

						Cell R16cell2 = row.createCell(1);
						if (record.getR17_res_carry_amt() != null) {
							R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R16cell3 = row.createCell(2);
						if (record.getR17_non_res_carry_amt() != null) {
							R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}

						// ================== ROW 18 ==================
						// Column 1
						row = sheet.getRow(17);

						Cell R17cell2 = row.createCell(1);
						if (record.getR18_res_carry_amt() != null) {
							R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);
						}

						// Column 2
						Cell R17cell3 = row.createCell(2);
						if (record.getR18_non_res_carry_amt() != null) {
							R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
							R17cell3.setCellStyle(numberStyle);
						} else {
							R17cell3.setCellValue("");
							R17cell3.setCellStyle(textStyle);
						}

						// ================== ROW 19 ==================
						row = sheet.getRow(18);
						Cell R18cell2 = row.createCell(1);
						if (record.getR19_res_carry_amt() != null) {
							R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);
						}

						Cell R18cell3 = row.createCell(2);
						if (record.getR19_non_res_carry_amt() != null) {
							R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
							R18cell3.setCellStyle(numberStyle);
						} else {
							R18cell3.setCellValue("");
							R18cell3.setCellStyle(textStyle);
						}
						// ================== ROW 20 ==================
						row = sheet.getRow(19);
						Cell R19cell2 = row.createCell(1);
						if (record.getR20_res_carry_amt() != null) {
							R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
							R19cell2.setCellStyle(numberStyle);
						} else {
							R19cell2.setCellValue("");
							R19cell2.setCellStyle(textStyle);
						}

						Cell R19cell3 = row.createCell(2);
						if (record.getR20_non_res_carry_amt() != null) {
							R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
							R19cell3.setCellStyle(numberStyle);
						} else {
							R19cell3.setCellValue("");
							R19cell3.setCellStyle(textStyle);
						}

						// ================== ROW 21 ==================
						row = sheet.getRow(20);
						Cell R20cell2 = row.createCell(1);
						if (record.getR21_res_carry_amt() != null) {
							R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
							R20cell2.setCellStyle(numberStyle);
						} else {
							R20cell2.setCellValue("");
							R20cell2.setCellStyle(textStyle);
						}

						Cell R20cell3 = row.createCell(2);
						if (record.getR21_non_res_carry_amt() != null) {
							R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
							R20cell3.setCellStyle(numberStyle);
						} else {
							R20cell3.setCellValue("");
							R20cell3.setCellStyle(textStyle);
						}

						// ================== ROW 22 ==================
						row = sheet.getRow(21);
						Cell R21cell2 = row.createCell(1);
						if (record.getR22_res_carry_amt() != null) {
							R21cell2.setCellValue(record.getR22_res_carry_amt().doubleValue());
							R21cell2.setCellStyle(numberStyle);
						} else {
							R21cell2.setCellValue("");
							R21cell2.setCellStyle(textStyle);
						}

						Cell R21cell3 = row.createCell(2);
						if (record.getR22_non_res_carry_amt() != null) {
							R21cell3.setCellValue(record.getR22_non_res_carry_amt().doubleValue());
							R21cell3.setCellStyle(numberStyle);
						} else {
							R21cell3.setCellValue("");
							R21cell3.setCellStyle(textStyle);
						}

						// ================== ROW 23 ==================
						row = sheet.getRow(22);
						Cell R22cell2 = row.createCell(1);
						if (record.getR23_res_carry_amt() != null) {
							R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
							R22cell2.setCellStyle(numberStyle);
						} else {
							R22cell2.setCellValue("");
							R22cell2.setCellStyle(textStyle);
						}

						Cell R22cell3 = row.createCell(2);
						if (record.getR23_non_res_carry_amt() != null) {
							R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
							R22cell3.setCellStyle(numberStyle);
						} else {
							R22cell3.setCellValue("");
							R22cell3.setCellStyle(textStyle);
						}

						// ================== ROW 24 ==================
						row = sheet.getRow(23);
						Cell R23cell2 = row.createCell(1);
						if (record.getR24_res_carry_amt() != null) {
							R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);
						}

						Cell R23cell3 = row.createCell(2);
						if (record.getR24_non_res_carry_amt() != null) {
							R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
							R23cell3.setCellStyle(numberStyle);
						} else {
							R23cell3.setCellValue("");
							R23cell3.setCellStyle(textStyle);
						}

						// ================== ROW 25 ==================
						row = sheet.getRow(24);
						Cell R24cell2 = row.createCell(1);
						if (record.getR25_res_carry_amt() != null) {
							R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);
						}

						Cell R24cell3 = row.createCell(2);
						if (record.getR25_non_res_carry_amt() != null) {
							R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
							R24cell3.setCellStyle(numberStyle);
						} else {
							R24cell3.setCellValue("");
							R24cell3.setCellStyle(textStyle);
						}

						// ================== ROW 26 ==================
						row = sheet.getRow(25);
						Cell R25cell2 = row.createCell(1);
						if (record.getR26_res_carry_amt() != null) {
							R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						Cell R25cell3 = row.createCell(2);
						if (record.getR26_non_res_carry_amt() != null) {
							R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}

						// ================== ROW 27 ==================
						row = sheet.getRow(26);
						Cell R26cell2 = row.createCell(1);
						if (record.getR27_res_carry_amt() != null) {
							R26cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						Cell R26cell3 = row.createCell(2);
						if (record.getR27_non_res_carry_amt() != null) {
							R26cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}

						// ================== ROW 28 ==================
						row = sheet.getRow(27);
						Cell R27cell2 = row.createCell(1);
						if (record.getR28_res_carry_amt() != null) {
							R27cell2.setCellValue(record.getR28_res_carry_amt().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						Cell R27cell3 = row.createCell(2);
						if (record.getR28_non_res_carry_amt() != null) {
							R27cell3.setCellValue(record.getR28_non_res_carry_amt().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);

						}
						// 29 is Calculation Part
						// ================== ROW 30 ==================
						row = sheet.getRow(29);
						Cell R29cell2 = row.createCell(1);
						if (record.getR30_res_carry_amt() != null) {
							R29cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
							R29cell2.setCellStyle(numberStyle);
						} else {
							R29cell2.setCellValue("");
							R29cell2.setCellStyle(textStyle);
						}

						Cell R29cell3 = row.createCell(2);
						if (record.getR30_non_res_carry_amt() != null) {
							R29cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
							R29cell3.setCellStyle(numberStyle);
						} else {
							R29cell3.setCellValue("");
							R29cell3.setCellStyle(textStyle);
						}

						// ================== ROW 31 ==================
						row = sheet.getRow(30);
						Cell R30cell2 = row.createCell(1);
						if (record.getR31_res_carry_amt() != null) {
							R30cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
							R30cell2.setCellStyle(numberStyle);
						} else {
							R30cell2.setCellValue("");
							R30cell2.setCellStyle(textStyle);
						}

						Cell R30cell3 = row.createCell(2);
						if (record.getR31_non_res_carry_amt() != null) {
							R30cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
							R30cell3.setCellStyle(numberStyle);
						} else {
							R30cell3.setCellValue("");
							R30cell3.setCellStyle(textStyle);
						}

						// ================== ROW 32 ==================
						row = sheet.getRow(31);
						Cell R31cell2 = row.createCell(1);
						if (record.getR32_res_carry_amt() != null) {
							R31cell2.setCellValue(record.getR32_res_carry_amt().doubleValue());
							R31cell2.setCellStyle(numberStyle);
						} else {
							R31cell2.setCellValue("");
							R31cell2.setCellStyle(textStyle);
						}

						Cell R31cell3 = row.createCell(2);
						if (record.getR32_non_res_carry_amt() != null) {
							R31cell3.setCellValue(record.getR32_non_res_carry_amt().doubleValue());
							R31cell3.setCellStyle(numberStyle);
						} else {
							R31cell3.setCellValue("");
							R31cell3.setCellStyle(textStyle);
						}
						// 33 is Calculation Part
						// ================== ROW 34 ==================
						row = sheet.getRow(33);
						Cell R33cell2 = row.createCell(1);
						if (record.getR34_res_carry_amt() != null) {
							R33cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}

						Cell R33cell3 = row.createCell(2);
						if (record.getR34_non_res_carry_amt() != null) {
							R33cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}

						// ================== ROW 35 ==================
						row = sheet.getRow(34);
						Cell R34cell2 = row.createCell(1);
						if (record.getR35_res_carry_amt() != null) {
							R34cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
							R34cell2.setCellStyle(numberStyle);
						} else {
							R34cell2.setCellValue("");
							R34cell2.setCellStyle(textStyle);
						}

						Cell R34cell3 = row.createCell(2);
						if (record.getR35_non_res_carry_amt() != null) {
							R34cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
							R34cell3.setCellStyle(numberStyle);
						} else {
							R34cell3.setCellValue("");
							R34cell3.setCellStyle(textStyle);
						}
						// 37 is Calculation Part

						// ================== ROW 36 ==================
						row = sheet.getRow(35);
						Cell R35cell2 = row.createCell(1);
						if (record.getR36_res_carry_amt() != null) {
							R35cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
							R35cell2.setCellStyle(numberStyle);
						} else {
							R35cell2.setCellValue("");
							R35cell2.setCellStyle(textStyle);
						}

						Cell R35cell3 = row.createCell(2);
						if (record.getR36_non_res_carry_amt() != null) {
							R35cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
							R35cell3.setCellStyle(numberStyle);
						} else {
							R35cell3.setCellValue("");
							R35cell3.setCellStyle(textStyle);
						}

						// ================== ROW 38 ==================
						row = sheet.getRow(37);
						Cell R37cell2 = row.createCell(1);
						if (record.getR38_res_carry_amt() != null) {
							R37cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
							R37cell2.setCellStyle(numberStyle);
						} else {
							R37cell2.setCellValue("");
							R37cell2.setCellStyle(textStyle);
						}

						Cell R37cell3 = row.createCell(2);
						if (record.getR38_non_res_carry_amt() != null) {
							R37cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
							R37cell3.setCellStyle(numberStyle);
						} else {
							R37cell3.setCellValue("");
							R37cell3.setCellStyle(textStyle);
						}

						// ================== ROW 39 ==================
						row = sheet.getRow(38);
						Cell R38cell2 = row.createCell(1);
						if (record.getR39_res_carry_amt() != null) {
							R38cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(2);
						if (record.getR39_non_res_carry_amt() != null) {
							R38cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ================== ROW 40 ==================
						row = sheet.getRow(39);
						Cell R39cell2 = row.createCell(1);
						if (record.getR40_res_carry_amt() != null) {
							R39cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
							R39cell2.setCellStyle(numberStyle);
						} else {
							R39cell2.setCellValue("");
							R39cell2.setCellStyle(textStyle);
						}

						Cell R39cell3 = row.createCell(2);
						if (record.getR40_non_res_carry_amt() != null) {
							R39cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
							R39cell3.setCellStyle(numberStyle);
						} else {
							R39cell3.setCellValue("");
							R39cell3.setCellStyle(textStyle);
						}

						// ================== ROW 41 ==================
						row = sheet.getRow(40);
						Cell R40cell2 = row.createCell(1);
						if (record.getR41_res_carry_amt() != null) {
							R40cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(2);
						if (record.getR41_non_res_carry_amt() != null) {
							R40cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// Row 42
						row = sheet.getRow(41);
						Cell R41cell2 = row.createCell(1);
						if (record.getR42_res_carry_amt() != null) {
							R41cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(2);
						if (record.getR42_non_res_carry_amt() != null) {
							R41cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}

						// Row 43
						row = sheet.getRow(42);
						Cell R42cell2 = row.createCell(1);
						if (record.getR43_res_carry_amt() != null) {
							R42cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
							R42cell2.setCellStyle(numberStyle);
						} else {
							R42cell2.setCellValue("");
							R42cell2.setCellStyle(textStyle);
						}

						Cell R42cell3 = row.createCell(2);
						if (record.getR43_non_res_carry_amt() != null) {
							R42cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_Intrest_Income SUMMARY", null,
							"BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	public byte[] getSummaryExcelARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<Q_SMME_Intrest_Income_Archival_Summary_Entity> dataList = getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_SMME_Intrest_Income new report. Returning empty result.");
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
					Q_SMME_Intrest_Income_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(1);

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
					Cell R15cell2 = row.createCell(1);
					if (record.getR16_res_carry_amt() != null) {
						R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(2);
					if (record.getR16_non_res_carry_amt() != null) {
						R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					row = sheet.getRow(16);

					Cell R16cell2 = row.createCell(1);
					if (record.getR17_res_carry_amt() != null) {
						R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(2);
					if (record.getR17_non_res_carry_amt() != null) {
						R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					row = sheet.getRow(17);

					Cell R17cell2 = row.createCell(1);
					if (record.getR18_res_carry_amt() != null) {
						R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(2);
					if (record.getR18_non_res_carry_amt() != null) {
						R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					row = sheet.getRow(18);
					Cell R18cell2 = row.createCell(1);
					if (record.getR19_res_carry_amt() != null) {
						R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(2);
					if (record.getR19_non_res_carry_amt() != null) {
						R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					row = sheet.getRow(19);
					Cell R19cell2 = row.createCell(1);
					if (record.getR20_res_carry_amt() != null) {
						R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(2);
					if (record.getR20_non_res_carry_amt() != null) {
						R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(20);
					Cell R20cell2 = row.createCell(1);
					if (record.getR21_res_carry_amt() != null) {
						R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(2);
					if (record.getR21_non_res_carry_amt() != null) {
						R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(21);
					Cell R21cell2 = row.createCell(1);
					if (record.getR22_res_carry_amt() != null) {
						R21cell2.setCellValue(record.getR22_res_carry_amt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(2);
					if (record.getR22_non_res_carry_amt() != null) {
						R21cell3.setCellValue(record.getR22_non_res_carry_amt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(22);
					Cell R22cell2 = row.createCell(1);
					if (record.getR23_res_carry_amt() != null) {
						R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(2);
					if (record.getR23_non_res_carry_amt() != null) {
						R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(23);
					Cell R23cell2 = row.createCell(1);
					if (record.getR24_res_carry_amt() != null) {
						R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(2);
					if (record.getR24_non_res_carry_amt() != null) {
						R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(24);
					Cell R24cell2 = row.createCell(1);
					if (record.getR25_res_carry_amt() != null) {
						R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(2);
					if (record.getR25_non_res_carry_amt() != null) {
						R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(25);
					Cell R25cell2 = row.createCell(1);
					if (record.getR26_res_carry_amt() != null) {
						R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(2);
					if (record.getR26_non_res_carry_amt() != null) {
						R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(1);
					if (record.getR27_res_carry_amt() != null) {
						R26cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(2);
					if (record.getR27_non_res_carry_amt() != null) {
						R26cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(1);
					if (record.getR28_res_carry_amt() != null) {
						R27cell2.setCellValue(record.getR28_res_carry_amt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(2);
					if (record.getR28_non_res_carry_amt() != null) {
						R27cell3.setCellValue(record.getR28_non_res_carry_amt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(29);
					Cell R29cell2 = row.createCell(1);
					if (record.getR30_res_carry_amt() != null) {
						R29cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(2);
					if (record.getR30_non_res_carry_amt() != null) {
						R29cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(30);
					Cell R30cell2 = row.createCell(1);
					if (record.getR31_res_carry_amt() != null) {
						R30cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(2);
					if (record.getR31_non_res_carry_amt() != null) {
						R30cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(31);
					Cell R31cell2 = row.createCell(1);
					if (record.getR32_res_carry_amt() != null) {
						R31cell2.setCellValue(record.getR32_res_carry_amt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(2);
					if (record.getR32_non_res_carry_amt() != null) {
						R31cell3.setCellValue(record.getR32_non_res_carry_amt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(33);
					Cell R33cell2 = row.createCell(1);
					if (record.getR34_res_carry_amt() != null) {
						R33cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(2);
					if (record.getR34_non_res_carry_amt() != null) {
						R33cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(34);
					Cell R34cell2 = row.createCell(1);
					if (record.getR35_res_carry_amt() != null) {
						R34cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(2);
					if (record.getR35_non_res_carry_amt() != null) {
						R34cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(35);
					Cell R35cell2 = row.createCell(1);
					if (record.getR36_res_carry_amt() != null) {
						R35cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(2);
					if (record.getR36_non_res_carry_amt() != null) {
						R35cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(37);
					Cell R37cell2 = row.createCell(1);
					if (record.getR38_res_carry_amt() != null) {
						R37cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(2);
					if (record.getR38_non_res_carry_amt() != null) {
						R37cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(38);
					Cell R38cell2 = row.createCell(1);
					if (record.getR39_res_carry_amt() != null) {
						R38cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(2);
					if (record.getR39_non_res_carry_amt() != null) {
						R38cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(39);
					Cell R39cell2 = row.createCell(1);
					if (record.getR40_res_carry_amt() != null) {
						R39cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(2);
					if (record.getR40_non_res_carry_amt() != null) {
						R39cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(40);
					Cell R40cell2 = row.createCell(1);
					if (record.getR41_res_carry_amt() != null) {
						R40cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(2);
					if (record.getR41_non_res_carry_amt() != null) {
						R40cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(41);
					Cell R41cell2 = row.createCell(1);
					if (record.getR42_res_carry_amt() != null) {
						R41cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(2);
					if (record.getR42_non_res_carry_amt() != null) {
						R41cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(42);
					Cell R42cell2 = row.createCell(1);
					if (record.getR43_res_carry_amt() != null) {
						R42cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					Cell R42cell3 = row.createCell(2);
					if (record.getR43_non_res_carry_amt() != null) {
						R42cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
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
	public List<Object[]> getQ_SMME_Intrest_IncomeResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Q_SMME_Intrest_Income_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (Q_SMME_Intrest_Income_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				Q_SMME_Intrest_Income_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  Q_SMME_Intrest_Income  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

	// Normal Email Excel
	public byte[] BRRS_Q_SMME_Intrest_Income_EmailExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, String format, BigDecimal version)
			throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_SMME_Intrest_IncomeEmailArchivalExcel(filename, reportId, fromdate, todate, currency,
						dtltype, type, format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
//			} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
//				logger.info("Service: Generating RESUB report for version {}", version);
			//
//				try {
//					// ✅ Redirecting to Resub Excel
//					return BRRS_Q_SMME_Intrest_IncomeResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
//							version);
			//
//				} catch (ParseException e) {
//					logger.error("Invalid report date format: {}", fromdate, e);
//					throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
//				}
		} else {
			List<Q_SMME_Intrest_Income_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_Q_SMME_Intrest_Income report. Returning empty result.");
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
						Q_SMME_Intrest_Income_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}
						Cell R12Cell = row.createCell(6);

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
						Cell R15cell2 = row.createCell(6);
						if (record.getR16_res_carry_amt() != null) {
							R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
							R15cell2.setCellStyle(numberStyle);
						} else {
							R15cell2.setCellValue("");
							R15cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R15cell3 = row.createCell(7);
						if (record.getR16_non_res_carry_amt() != null) {
							R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
							R15cell3.setCellStyle(numberStyle);
						} else {
							R15cell3.setCellValue("");
							R15cell3.setCellStyle(textStyle);
						}

						// row17
						// Column 1
						// MINING
						row = sheet.getRow(15);

						Cell R16cell2 = row.createCell(6);
						if (record.getR17_res_carry_amt() != null) {
							R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
							R16cell2.setCellStyle(numberStyle);
						} else {
							R16cell2.setCellValue("");
							R16cell2.setCellStyle(textStyle);
						}
						// Column 2
						Cell R16cell3 = row.createCell(7);
						if (record.getR17_non_res_carry_amt() != null) {
							R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
							R16cell3.setCellStyle(numberStyle);
						} else {
							R16cell3.setCellValue("");
							R16cell3.setCellStyle(textStyle);
						}

						// ================== ROW 18 ==================
						// Column 1
						// MANUFACTURING
						row = sheet.getRow(16);

						Cell R17cell2 = row.createCell(6);
						if (record.getR18_res_carry_amt() != null) {
							R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
							R17cell2.setCellStyle(numberStyle);
						} else {
							R17cell2.setCellValue("");
							R17cell2.setCellStyle(textStyle);
						}

						// Column 2
						Cell R17cell3 = row.createCell(7);
						if (record.getR18_non_res_carry_amt() != null) {
							R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
							R17cell3.setCellStyle(numberStyle);
						} else {
							R17cell3.setCellValue("");
							R17cell3.setCellStyle(textStyle);
						}

						// ================== ROW 19 ==================
						// CONSTRUCTION
						row = sheet.getRow(17);
						Cell R18cell2 = row.createCell(6);
						if (record.getR19_res_carry_amt() != null) {
							R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
							R18cell2.setCellStyle(numberStyle);
						} else {
							R18cell2.setCellValue("");
							R18cell2.setCellStyle(textStyle);
						}

						Cell R18cell3 = row.createCell(7);
						if (record.getR19_non_res_carry_amt() != null) {
							R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
							R18cell3.setCellStyle(numberStyle);
						} else {
							R18cell3.setCellValue("");
							R18cell3.setCellStyle(textStyle);
						}
						// ================== ROW 20 ==================
						// CRE
						row = sheet.getRow(18);
						Cell R19cell2 = row.createCell(6);
						if (record.getR20_res_carry_amt() != null) {
							R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
							R19cell2.setCellStyle(numberStyle);
						} else {
							R19cell2.setCellValue("");
							R19cell2.setCellStyle(textStyle);
						}

						Cell R19cell3 = row.createCell(7);
						if (record.getR20_non_res_carry_amt() != null) {
							R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
							R19cell3.setCellStyle(numberStyle);
						} else {
							R19cell3.setCellValue("");
							R19cell3.setCellStyle(textStyle);
						}

						// ================== ROW 21 ==================
						row = sheet.getRow(19);
						// ELECTRICITY
						Cell R20cell2 = row.createCell(6);
						if (record.getR21_res_carry_amt() != null) {
							R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
							R20cell2.setCellStyle(numberStyle);
						} else {
							R20cell2.setCellValue("");
							R20cell2.setCellStyle(textStyle);
						}

						Cell R20cell3 = row.createCell(7);
						if (record.getR21_non_res_carry_amt() != null) {
							R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
							R20cell3.setCellStyle(numberStyle);
						} else {
							R20cell3.setCellValue("");
							R20cell3.setCellStyle(textStyle);
						}

						// ================== ROW 22 ==================
						row = sheet.getRow(20);
						// BUSINESS SERVICE
						Cell R21cell2 = row.createCell(6);
						if (record.getR27_res_carry_amt() != null) {
							R21cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
							R21cell2.setCellStyle(numberStyle);
						} else {
							R21cell2.setCellValue("");
							R21cell2.setCellStyle(textStyle);
						}

						Cell R21cell3 = row.createCell(7);
						if (record.getR27_non_res_carry_amt() != null) {
							R21cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
							R21cell3.setCellStyle(numberStyle);
						} else {
							R21cell3.setCellValue("");
							R21cell3.setCellStyle(textStyle);
						}

						// ================== ROW 23 ==================
						row = sheet.getRow(21);
						// TELECOM
						Cell R22cell2 = row.createCell(6);
						if (record.getR23_res_carry_amt() != null) {
							R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
							R22cell2.setCellStyle(numberStyle);
						} else {
							R22cell2.setCellValue("");
							R22cell2.setCellStyle(textStyle);
						}

						Cell R22cell3 = row.createCell(7);
						if (record.getR23_non_res_carry_amt() != null) {
							R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
							R22cell3.setCellStyle(numberStyle);
						} else {
							R22cell3.setCellValue("");
							R22cell3.setCellStyle(textStyle);
						}

						// ================== ROW 24 ==================
						row = sheet.getRow(22);
						// Tourism AND HOTEL
						Cell R23cell2 = row.createCell(6);
						if (record.getR24_res_carry_amt() != null) {
							R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);
						}

						Cell R23cell3 = row.createCell(7);
						if (record.getR24_non_res_carry_amt() != null) {
							R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
							R23cell3.setCellStyle(numberStyle);
						} else {
							R23cell3.setCellValue("");
							R23cell3.setCellStyle(textStyle);
						}

						// ================== ROW 25 ==================
						row = sheet.getRow(23);
						// TRANSPORT
						Cell R24cell2 = row.createCell(6);
						if (record.getR25_res_carry_amt() != null) {
							R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);
						}

						Cell R24cell3 = row.createCell(7);
						if (record.getR25_non_res_carry_amt() != null) {
							R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
							R24cell3.setCellStyle(numberStyle);
						} else {
							R24cell3.setCellValue("");
							R24cell3.setCellStyle(textStyle);
						}

						// ================== ROW 26 ==================
						row = sheet.getRow(24);
						// TRADE,REST,BAR
						Cell R25cell2 = row.createCell(6);
						if (record.getR26_res_carry_amt() != null) {
							R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						Cell R25cell3 = row.createCell(7);
						if (record.getR26_non_res_carry_amt() != null) {
							R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}
						row = sheet.getRow(25);
						Cell R25cell21 = row.createCell(6);
						if (record.getR29_res_carry_amt() != null) {
							R25cell21.setCellValue(record.getR29_res_carry_amt().doubleValue());
							R25cell21.setCellStyle(numberStyle);
						} else {
							R25cell21.setCellValue("");
							R25cell21.setCellStyle(textStyle);
						}

						Cell R2925cell3 = row.createCell(7);
						if (record.getR29_non_res_carry_amt() != null) {
							R2925cell3.setCellValue(record.getR29_non_res_carry_amt().doubleValue());
							R2925cell3.setCellStyle(numberStyle);
						} else {
							R2925cell3.setCellValue("");
							R2925cell3.setCellStyle(textStyle);
						}
						// ================== ROW 27 ==================
						row = sheet.getRow(26);
						Cell R26cell2 = row.createCell(6);
						if (record.getR30_res_carry_amt() != null) {
							R26cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						Cell R26cell3 = row.createCell(7);
						if (record.getR30_non_res_carry_amt() != null) {
							R26cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}

						// ================== ROW 28 ==================
						row = sheet.getRow(27);
						Cell R27cell2 = row.createCell(6);
						if (record.getR31_res_carry_amt() != null) {
							R27cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						Cell R27cell3 = row.createCell(7);
						if (record.getR31_non_res_carry_amt() != null) {
							R27cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);

						}
						// 29 is Calculation Part
						// ================== ROW 30 ==================
						row = sheet.getRow(28);
						Cell R29cell2 = row.createCell(6);
						if (record.getR33_res_carry_amt() != null) {
							R29cell2.setCellValue(record.getR33_res_carry_amt().doubleValue());
							R29cell2.setCellStyle(numberStyle);
						} else {
							R29cell2.setCellValue("");
							R29cell2.setCellStyle(textStyle);
						}

						Cell R29cell3 = row.createCell(7);
						if (record.getR33_non_res_carry_amt() != null) {
							R29cell3.setCellValue(record.getR33_non_res_carry_amt().doubleValue());
							R29cell3.setCellStyle(numberStyle);
						} else {
							R29cell3.setCellValue("");
							R29cell3.setCellStyle(textStyle);
						}

						// ================== ROW 31 ==================
						row = sheet.getRow(29);
						Cell R30cell2 = row.createCell(6);
						if (record.getR38_res_carry_amt() != null) {
							R30cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
							R30cell2.setCellStyle(numberStyle);
						} else {
							R30cell2.setCellValue("");
							R30cell2.setCellStyle(textStyle);
						}

						Cell R30cell3 = row.createCell(7);
						if (record.getR38_non_res_carry_amt() != null) {
							R30cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
							R30cell3.setCellStyle(numberStyle);
						} else {
							R30cell3.setCellValue("");
							R30cell3.setCellStyle(textStyle);
						}

						// ================== ROW 32 ==================
						row = sheet.getRow(30);
						Cell R31cell2 = row.createCell(6);
						if (record.getR34_res_carry_amt() != null) {
							R31cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
							R31cell2.setCellStyle(numberStyle);
						} else {
							R31cell2.setCellValue("");
							R31cell2.setCellStyle(textStyle);
						}

						Cell R31cell3 = row.createCell(7);
						if (record.getR34_non_res_carry_amt() != null) {
							R31cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
							R31cell3.setCellStyle(numberStyle);
						} else {
							R31cell3.setCellValue("");
							R31cell3.setCellStyle(textStyle);
						}
						// 33 is Calculation Part
						// ================== ROW 34 ==================
						row = sheet.getRow(31);
						Cell R33cell2 = row.createCell(6);
						if (record.getR35_res_carry_amt() != null) {
							R33cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}

						Cell R33cell3 = row.createCell(7);
						if (record.getR35_non_res_carry_amt() != null) {
							R33cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}

						// ================== ROW 35 ==================
						row = sheet.getRow(32);
						Cell R34cell2 = row.createCell(6);
						if (record.getR36_res_carry_amt() != null) {
							R34cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
							R34cell2.setCellStyle(numberStyle);
						} else {
							R34cell2.setCellValue("");
							R34cell2.setCellStyle(textStyle);
						}

						Cell R34cell3 = row.createCell(7);
						if (record.getR36_non_res_carry_amt() != null) {
							R34cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
							R34cell3.setCellStyle(numberStyle);
						} else {
							R34cell3.setCellValue("");
							R34cell3.setCellStyle(textStyle);
						}
						// 37 is Calculation Part

						// ================== ROW 36 ==================
						row = sheet.getRow(33);
						Cell R35cell2 = row.createCell(6);
						if (record.getR37_res_carry_amt() != null) {
							R35cell2.setCellValue(record.getR37_res_carry_amt().doubleValue());
							R35cell2.setCellStyle(numberStyle);
						} else {
							R35cell2.setCellValue("");
							R35cell2.setCellStyle(textStyle);
						}

						Cell R35cell3 = row.createCell(7);
						if (record.getR37_non_res_carry_amt() != null) {
							R35cell3.setCellValue(record.getR37_non_res_carry_amt().doubleValue());
							R35cell3.setCellStyle(numberStyle);
						} else {
							R35cell3.setCellValue("");
							R35cell3.setCellStyle(textStyle);
						}

						// ================== ROW 38 ==================
						row = sheet.getRow(34);
						Cell R37cell2 = row.createCell(6);
						if (record.getR39_res_carry_amt() != null) {
							R37cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
							R37cell2.setCellStyle(numberStyle);
						} else {
							R37cell2.setCellValue("");
							R37cell2.setCellStyle(textStyle);
						}

						Cell R37cell3 = row.createCell(7);
						if (record.getR39_non_res_carry_amt() != null) {
							R37cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
							R37cell3.setCellStyle(numberStyle);
						} else {
							R37cell3.setCellValue("");
							R37cell3.setCellStyle(textStyle);
						}

						// ================== ROW 39 ==================
						row = sheet.getRow(35);
						Cell R38cell2 = row.createCell(6);
						if (record.getR40_res_carry_amt() != null) {
							R38cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(7);
						if (record.getR40_non_res_carry_amt() != null) {
							R38cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ================== ROW 40 ==================
						row = sheet.getRow(36);
						Cell R39cell2 = row.createCell(6);
						if (record.getR41_res_carry_amt() != null) {
							R39cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
							R39cell2.setCellStyle(numberStyle);
						} else {
							R39cell2.setCellValue("");
							R39cell2.setCellStyle(textStyle);
						}

						Cell R39cell3 = row.createCell(7);
						if (record.getR41_non_res_carry_amt() != null) {
							R39cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
							R39cell3.setCellStyle(numberStyle);
						} else {
							R39cell3.setCellValue("");
							R39cell3.setCellStyle(textStyle);
						}

						// ================== ROW 41 ==================
						row = sheet.getRow(37);
						Cell R40cell2 = row.createCell(6);
						if (record.getR42_res_carry_amt() != null) {
							R40cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(7);
						if (record.getR42_non_res_carry_amt() != null) {
							R40cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// Row 42
						row = sheet.getRow(38);
						Cell R41cell2 = row.createCell(6);
						if (record.getR43_res_carry_amt() != null) {
							R41cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(7);
						if (record.getR43_non_res_carry_amt() != null) {
							R41cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}

						// Row 43
						row = sheet.getRow(39);

						Cell R42cell3 = row.createCell(7);
						if (record.getR44_non_res_carry_amt() != null) {
							R42cell3.setCellValue(record.getR44_non_res_carry_amt().doubleValue());
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_Intrest_Income EMAIL SUMMARY", null,
							"BRRS_Q_SMME_INTREST_INCOME_SUMMARYTABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival Email Excel
	public byte[] BRRS_Q_SMME_Intrest_IncomeEmailArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, String format, BigDecimal version)
			throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_SMME_Intrest_Income_Archival_Summary_Entity> dataList = getdatabydateListarchival(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_SMME_Intrest_Income report. Returning empty result.");
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
					Q_SMME_Intrest_Income_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(6);

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
					Cell R15cell2 = row.createCell(6);
					if (record.getR16_res_carry_amt() != null) {
						R15cell2.setCellValue(record.getR16_res_carry_amt().doubleValue());
						R15cell2.setCellStyle(numberStyle);
					} else {
						R15cell2.setCellValue("");
						R15cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R15cell3 = row.createCell(7);
					if (record.getR16_non_res_carry_amt() != null) {
						R15cell3.setCellValue(record.getR16_non_res_carry_amt().doubleValue());
						R15cell3.setCellStyle(numberStyle);
					} else {
						R15cell3.setCellValue("");
						R15cell3.setCellStyle(textStyle);
					}

					// row17
					// Column 1
					// MINING
					row = sheet.getRow(15);

					Cell R16cell2 = row.createCell(6);
					if (record.getR17_res_carry_amt() != null) {
						R16cell2.setCellValue(record.getR17_res_carry_amt().doubleValue());
						R16cell2.setCellStyle(numberStyle);
					} else {
						R16cell2.setCellValue("");
						R16cell2.setCellStyle(textStyle);
					}
					// Column 2
					Cell R16cell3 = row.createCell(7);
					if (record.getR17_non_res_carry_amt() != null) {
						R16cell3.setCellValue(record.getR17_non_res_carry_amt().doubleValue());
						R16cell3.setCellStyle(numberStyle);
					} else {
						R16cell3.setCellValue("");
						R16cell3.setCellStyle(textStyle);
					}

					// ================== ROW 18 ==================
					// Column 1
					// MANUFACTURING
					row = sheet.getRow(16);

					Cell R17cell2 = row.createCell(6);
					if (record.getR18_res_carry_amt() != null) {
						R17cell2.setCellValue(record.getR18_res_carry_amt().doubleValue());
						R17cell2.setCellStyle(numberStyle);
					} else {
						R17cell2.setCellValue("");
						R17cell2.setCellStyle(textStyle);
					}

					// Column 2
					Cell R17cell3 = row.createCell(7);
					if (record.getR18_non_res_carry_amt() != null) {
						R17cell3.setCellValue(record.getR18_non_res_carry_amt().doubleValue());
						R17cell3.setCellStyle(numberStyle);
					} else {
						R17cell3.setCellValue("");
						R17cell3.setCellStyle(textStyle);
					}

					// ================== ROW 19 ==================
					// CONSTRUCTION
					row = sheet.getRow(17);
					Cell R18cell2 = row.createCell(6);
					if (record.getR19_res_carry_amt() != null) {
						R18cell2.setCellValue(record.getR19_res_carry_amt().doubleValue());
						R18cell2.setCellStyle(numberStyle);
					} else {
						R18cell2.setCellValue("");
						R18cell2.setCellStyle(textStyle);
					}

					Cell R18cell3 = row.createCell(7);
					if (record.getR19_non_res_carry_amt() != null) {
						R18cell3.setCellValue(record.getR19_non_res_carry_amt().doubleValue());
						R18cell3.setCellStyle(numberStyle);
					} else {
						R18cell3.setCellValue("");
						R18cell3.setCellStyle(textStyle);
					}
					// ================== ROW 20 ==================
					// CRE
					row = sheet.getRow(18);
					Cell R19cell2 = row.createCell(6);
					if (record.getR20_res_carry_amt() != null) {
						R19cell2.setCellValue(record.getR20_res_carry_amt().doubleValue());
						R19cell2.setCellStyle(numberStyle);
					} else {
						R19cell2.setCellValue("");
						R19cell2.setCellStyle(textStyle);
					}

					Cell R19cell3 = row.createCell(7);
					if (record.getR20_non_res_carry_amt() != null) {
						R19cell3.setCellValue(record.getR20_non_res_carry_amt().doubleValue());
						R19cell3.setCellStyle(numberStyle);
					} else {
						R19cell3.setCellValue("");
						R19cell3.setCellStyle(textStyle);
					}

					// ================== ROW 21 ==================
					row = sheet.getRow(19);
					// ELECTRICITY
					Cell R20cell2 = row.createCell(6);
					if (record.getR21_res_carry_amt() != null) {
						R20cell2.setCellValue(record.getR21_res_carry_amt().doubleValue());
						R20cell2.setCellStyle(numberStyle);
					} else {
						R20cell2.setCellValue("");
						R20cell2.setCellStyle(textStyle);
					}

					Cell R20cell3 = row.createCell(7);
					if (record.getR21_non_res_carry_amt() != null) {
						R20cell3.setCellValue(record.getR21_non_res_carry_amt().doubleValue());
						R20cell3.setCellStyle(numberStyle);
					} else {
						R20cell3.setCellValue("");
						R20cell3.setCellStyle(textStyle);
					}

					// ================== ROW 22 ==================
					row = sheet.getRow(20);
					// BUSINESS SERVICE
					Cell R21cell2 = row.createCell(6);
					if (record.getR27_res_carry_amt() != null) {
						R21cell2.setCellValue(record.getR27_res_carry_amt().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(7);
					if (record.getR27_non_res_carry_amt() != null) {
						R21cell3.setCellValue(record.getR27_non_res_carry_amt().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================== ROW 23 ==================
					row = sheet.getRow(21);
					// TELECOM
					Cell R22cell2 = row.createCell(6);
					if (record.getR23_res_carry_amt() != null) {
						R22cell2.setCellValue(record.getR23_res_carry_amt().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(7);
					if (record.getR23_non_res_carry_amt() != null) {
						R22cell3.setCellValue(record.getR23_non_res_carry_amt().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================== ROW 24 ==================
					row = sheet.getRow(22);
					// Tourism AND HOTEL
					Cell R23cell2 = row.createCell(6);
					if (record.getR24_res_carry_amt() != null) {
						R23cell2.setCellValue(record.getR24_res_carry_amt().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(7);
					if (record.getR24_non_res_carry_amt() != null) {
						R23cell3.setCellValue(record.getR24_non_res_carry_amt().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================== ROW 25 ==================
					row = sheet.getRow(23);
					// TRANSPORT
					Cell R24cell2 = row.createCell(6);
					if (record.getR25_res_carry_amt() != null) {
						R24cell2.setCellValue(record.getR25_res_carry_amt().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(7);
					if (record.getR25_non_res_carry_amt() != null) {
						R24cell3.setCellValue(record.getR25_non_res_carry_amt().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================== ROW 26 ==================
					row = sheet.getRow(24);
					// TRADE,REST,BAR
					Cell R25cell2 = row.createCell(6);
					if (record.getR26_res_carry_amt() != null) {
						R25cell2.setCellValue(record.getR26_res_carry_amt().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(7);
					if (record.getR26_non_res_carry_amt() != null) {
						R25cell3.setCellValue(record.getR26_non_res_carry_amt().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(25);
					Cell R25cell21 = row.createCell(6);
					if (record.getR29_res_carry_amt() != null) {
						R25cell21.setCellValue(record.getR29_res_carry_amt().doubleValue());
						R25cell21.setCellStyle(numberStyle);
					} else {
						R25cell21.setCellValue("");
						R25cell21.setCellStyle(textStyle);
					}

					Cell R2925cell3 = row.createCell(7);
					if (record.getR29_non_res_carry_amt() != null) {
						R2925cell3.setCellValue(record.getR29_non_res_carry_amt().doubleValue());
						R2925cell3.setCellStyle(numberStyle);
					} else {
						R2925cell3.setCellValue("");
						R2925cell3.setCellStyle(textStyle);
					}
					// ================== ROW 27 ==================
					row = sheet.getRow(26);
					Cell R26cell2 = row.createCell(6);
					if (record.getR30_res_carry_amt() != null) {
						R26cell2.setCellValue(record.getR30_res_carry_amt().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(7);
					if (record.getR30_non_res_carry_amt() != null) {
						R26cell3.setCellValue(record.getR30_non_res_carry_amt().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================== ROW 28 ==================
					row = sheet.getRow(27);
					Cell R27cell2 = row.createCell(6);
					if (record.getR31_res_carry_amt() != null) {
						R27cell2.setCellValue(record.getR31_res_carry_amt().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(7);
					if (record.getR31_non_res_carry_amt() != null) {
						R27cell3.setCellValue(record.getR31_non_res_carry_amt().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);

					}
					// 29 is Calculation Part
					// ================== ROW 30 ==================
					row = sheet.getRow(28);
					Cell R29cell2 = row.createCell(6);
					if (record.getR33_res_carry_amt() != null) {
						R29cell2.setCellValue(record.getR33_res_carry_amt().doubleValue());
						R29cell2.setCellStyle(numberStyle);
					} else {
						R29cell2.setCellValue("");
						R29cell2.setCellStyle(textStyle);
					}

					Cell R29cell3 = row.createCell(7);
					if (record.getR33_non_res_carry_amt() != null) {
						R29cell3.setCellValue(record.getR33_non_res_carry_amt().doubleValue());
						R29cell3.setCellStyle(numberStyle);
					} else {
						R29cell3.setCellValue("");
						R29cell3.setCellStyle(textStyle);
					}

					// ================== ROW 31 ==================
					row = sheet.getRow(29);
					Cell R30cell2 = row.createCell(6);
					if (record.getR38_res_carry_amt() != null) {
						R30cell2.setCellValue(record.getR38_res_carry_amt().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					Cell R30cell3 = row.createCell(7);
					if (record.getR38_non_res_carry_amt() != null) {
						R30cell3.setCellValue(record.getR38_non_res_carry_amt().doubleValue());
						R30cell3.setCellStyle(numberStyle);
					} else {
						R30cell3.setCellValue("");
						R30cell3.setCellStyle(textStyle);
					}

					// ================== ROW 32 ==================
					row = sheet.getRow(30);
					Cell R31cell2 = row.createCell(6);
					if (record.getR34_res_carry_amt() != null) {
						R31cell2.setCellValue(record.getR34_res_carry_amt().doubleValue());
						R31cell2.setCellStyle(numberStyle);
					} else {
						R31cell2.setCellValue("");
						R31cell2.setCellStyle(textStyle);
					}

					Cell R31cell3 = row.createCell(7);
					if (record.getR34_non_res_carry_amt() != null) {
						R31cell3.setCellValue(record.getR34_non_res_carry_amt().doubleValue());
						R31cell3.setCellStyle(numberStyle);
					} else {
						R31cell3.setCellValue("");
						R31cell3.setCellStyle(textStyle);
					}
					// 33 is Calculation Part
					// ================== ROW 34 ==================
					row = sheet.getRow(31);
					Cell R33cell2 = row.createCell(6);
					if (record.getR35_res_carry_amt() != null) {
						R33cell2.setCellValue(record.getR35_res_carry_amt().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}

					Cell R33cell3 = row.createCell(7);
					if (record.getR35_non_res_carry_amt() != null) {
						R33cell3.setCellValue(record.getR35_non_res_carry_amt().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// ================== ROW 35 ==================
					row = sheet.getRow(32);
					Cell R34cell2 = row.createCell(6);
					if (record.getR36_res_carry_amt() != null) {
						R34cell2.setCellValue(record.getR36_res_carry_amt().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					Cell R34cell3 = row.createCell(7);
					if (record.getR36_non_res_carry_amt() != null) {
						R34cell3.setCellValue(record.getR36_non_res_carry_amt().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}
					// 37 is Calculation Part

					// ================== ROW 36 ==================
					row = sheet.getRow(33);
					Cell R35cell2 = row.createCell(6);
					if (record.getR37_res_carry_amt() != null) {
						R35cell2.setCellValue(record.getR37_res_carry_amt().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					Cell R35cell3 = row.createCell(7);
					if (record.getR37_non_res_carry_amt() != null) {
						R35cell3.setCellValue(record.getR37_non_res_carry_amt().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// ================== ROW 38 ==================
					row = sheet.getRow(34);
					Cell R37cell2 = row.createCell(6);
					if (record.getR39_res_carry_amt() != null) {
						R37cell2.setCellValue(record.getR39_res_carry_amt().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}

					Cell R37cell3 = row.createCell(7);
					if (record.getR39_non_res_carry_amt() != null) {
						R37cell3.setCellValue(record.getR39_non_res_carry_amt().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// ================== ROW 39 ==================
					row = sheet.getRow(35);
					Cell R38cell2 = row.createCell(6);
					if (record.getR40_res_carry_amt() != null) {
						R38cell2.setCellValue(record.getR40_res_carry_amt().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					Cell R38cell3 = row.createCell(7);
					if (record.getR40_non_res_carry_amt() != null) {
						R38cell3.setCellValue(record.getR40_non_res_carry_amt().doubleValue());
						R38cell3.setCellStyle(numberStyle);
					} else {
						R38cell3.setCellValue("");
						R38cell3.setCellStyle(textStyle);
					}

					// ================== ROW 40 ==================
					row = sheet.getRow(36);
					Cell R39cell2 = row.createCell(6);
					if (record.getR41_res_carry_amt() != null) {
						R39cell2.setCellValue(record.getR41_res_carry_amt().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					Cell R39cell3 = row.createCell(7);
					if (record.getR41_non_res_carry_amt() != null) {
						R39cell3.setCellValue(record.getR41_non_res_carry_amt().doubleValue());
						R39cell3.setCellStyle(numberStyle);
					} else {
						R39cell3.setCellValue("");
						R39cell3.setCellStyle(textStyle);
					}

					// ================== ROW 41 ==================
					row = sheet.getRow(37);
					Cell R40cell2 = row.createCell(6);
					if (record.getR42_res_carry_amt() != null) {
						R40cell2.setCellValue(record.getR42_res_carry_amt().doubleValue());
						R40cell2.setCellStyle(numberStyle);
					} else {
						R40cell2.setCellValue("");
						R40cell2.setCellStyle(textStyle);
					}

					Cell R40cell3 = row.createCell(7);
					if (record.getR42_non_res_carry_amt() != null) {
						R40cell3.setCellValue(record.getR42_non_res_carry_amt().doubleValue());
						R40cell3.setCellStyle(numberStyle);
					} else {
						R40cell3.setCellValue("");
						R40cell3.setCellStyle(textStyle);
					}

					// Row 42
					row = sheet.getRow(38);
					Cell R41cell2 = row.createCell(6);
					if (record.getR43_res_carry_amt() != null) {
						R41cell2.setCellValue(record.getR43_res_carry_amt().doubleValue());
						R41cell2.setCellStyle(numberStyle);
					} else {
						R41cell2.setCellValue("");
						R41cell2.setCellStyle(textStyle);
					}

					Cell R41cell3 = row.createCell(7);
					if (record.getR43_non_res_carry_amt() != null) {
						R41cell3.setCellValue(record.getR43_non_res_carry_amt().doubleValue());
						R41cell3.setCellStyle(numberStyle);
					} else {
						R41cell3.setCellValue("");
						R41cell3.setCellStyle(textStyle);
					}

					// Row 43
					row = sheet.getRow(39);

					Cell R42cell3 = row.createCell(7);
					if (record.getR44_non_res_carry_amt() != null) {
						R42cell3.setCellValue(record.getR44_non_res_carry_amt().doubleValue());
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_SMME_Intrest_Income EMAIL ARCHIVAL SUMMARY",
						null, "BRRS_Q_SMME_INTREST_INCOME_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}
}
