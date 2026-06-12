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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service
public class BRRS_M_P_L_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_P_L_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// =====================================================
	// ENTITY MANAGER (Acts like Repository)
	// =====================================================

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<M_P_L_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_P_L_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_P_LRowMapper());
	}

	// =========================================================
	// GET REPORT_DATE + REPORT_VERSION
	// =========================================================

	public List<Object[]> getM_P_L_Newarchival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_P_L_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// =========================================================
	// GET ARCHIVAL FULL DATA BY DATE + VERSION
	// =========================================================
	public List<M_P_L_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_M_P_L_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new M_P_LArchivalRowMapper());
	}

	// =========================================================
	// GET ALL WITH VERSION
	// =========================================================

	public List<M_P_L_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_M_P_L_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_P_LArchivalRowMapper());
	}

	// =========================================================
	// GET MAX VERSION BY DATE
	// =========================================================

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_P_L_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	// =========================================================
	// 1. BY DATE + LABEL + CRITERIA
	// =========================================================

	public List<M_P_L_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String ReportLable,
			String reportAddlCriteria_1) {

		String sql = "SELECT * FROM BRRS_M_P_L_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, ReportLable, reportAddlCriteria_1 },
				new M_P_LDetailRowMapper());
	}

	// =========================================================
	// 2. GET ALL (BY DATE - simple)
	// =========================================================
	public List<M_P_L_Detail_Entity> getDetaildatabydateList(Date reportDate) {

	    String sql =
	        "SELECT * FROM BRRS_M_P_L_DETAILTABLE " +
	        "WHERE TRUNC(REPORT_DATE) = TRUNC(?)";

	    return jdbcTemplate.query(
	            sql,
	            new Object[] { new java.sql.Date(reportDate.getTime()) },
	            new M_P_LDetailRowMapper());
	}

	// =========================================================
	// 3. PAGINATION
	// =========================================================
	public List<M_P_L_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_P_L_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_P_LDetailRowMapper());
	}

	// =========================================================
	// 4. COUNT
	// =========================================================
	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_P_L_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// =========================================================
	// 5. BY LABEL + CRITERIA
	// =========================================================
	public List<M_P_L_Detail_Entity> GetDetailDataByRowIdAndColumnId(String ReportLable, String reportAddlCriteria_1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_P_L_DETAILTABLE "
				+ "WHERE REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { ReportLable, reportAddlCriteria_1, reportdate },
				new M_P_LDetailRowMapper());
	}

	// =========================================================
	// 6. BY ACCOUNT NUMBER
	// =========================================================
	public M_P_L_Detail_Entity findByAcctnumber(String acct_number) {

		String sql = "SELECT * FROM BRRS_M_P_L_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acct_number }, new M_P_LDetailRowMapper());
	}

	// =========================================================
	// 1. GET BY DATE + VERSION
	// =========================================================
	public List<M_P_L_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_P_L_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new M_P_LArchivalDetailRowMapper());
	}

	// =========================================================
	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION
	// =========================================================
	public List<M_P_L_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String ReportLable,
			String reportAddlCriteria_1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_P_L_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABLE = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { ReportLable, reportAddlCriteria_1, reportdate, dataEntryVersion },
				new M_P_LArchivalDetailRowMapper());
	}

	// =========================================================
	// ROW MAPPER
	// =========================================================

	class M_P_LRowMapper implements RowMapper<M_P_L_Summary_Entity> {

		@Override
		public M_P_L_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_P_L_Summary_Entity obj = new M_P_L_Summary_Entity();

			// ---------- R08 ----------
			obj.setR08_SCH_NO(rs.getBigDecimal("R08_SCH_NO"));
			obj.setR08_NET_AMT(rs.getBigDecimal("R08_NET_AMT"));
			obj.setR08_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R08_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R09 ----------
			obj.setR09_PRODUCT(rs.getString("R09_PRODUCT"));
			obj.setR09_SCH_NO(rs.getBigDecimal("R09_SCH_NO"));
			obj.setR09_NET_AMT(rs.getBigDecimal("R09_NET_AMT"));
			obj.setR09_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R09_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R10 ----------
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_SCH_NO(rs.getBigDecimal("R10_SCH_NO"));
			obj.setR10_NET_AMT(rs.getBigDecimal("R10_NET_AMT"));
			obj.setR10_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R10_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R11 ----------
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_SCH_NO(rs.getBigDecimal("R11_SCH_NO"));
			obj.setR11_NET_AMT(rs.getBigDecimal("R11_NET_AMT"));
			obj.setR11_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R11_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R12 ----------
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_SCH_NO(rs.getBigDecimal("R12_SCH_NO"));
			obj.setR12_NET_AMT(rs.getBigDecimal("R12_NET_AMT"));
			obj.setR12_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R12_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R13 ----------
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_SCH_NO(rs.getBigDecimal("R13_SCH_NO"));
			obj.setR13_NET_AMT(rs.getBigDecimal("R13_NET_AMT"));
			obj.setR13_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R13_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R14 ----------
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_SCH_NO(rs.getBigDecimal("R14_SCH_NO"));
			obj.setR14_NET_AMT(rs.getBigDecimal("R14_NET_AMT"));
			obj.setR14_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R14_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R15 ----------
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_SCH_NO(rs.getBigDecimal("R15_SCH_NO"));
			obj.setR15_NET_AMT(rs.getBigDecimal("R15_NET_AMT"));
			obj.setR15_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R15_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R16 ----------
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_SCH_NO(rs.getBigDecimal("R16_SCH_NO"));
			obj.setR16_NET_AMT(rs.getBigDecimal("R16_NET_AMT"));
			obj.setR16_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R16_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R17 ----------
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_SCH_NO(rs.getBigDecimal("R17_SCH_NO"));
			obj.setR17_NET_AMT(rs.getBigDecimal("R17_NET_AMT"));
			obj.setR17_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R17_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R18 ----------
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_SCH_NO(rs.getBigDecimal("R18_SCH_NO"));
			obj.setR18_NET_AMT(rs.getBigDecimal("R18_NET_AMT"));
			obj.setR18_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R18_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R19 ----------
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_SCH_NO(rs.getBigDecimal("R19_SCH_NO"));
			obj.setR19_NET_AMT(rs.getBigDecimal("R19_NET_AMT"));
			obj.setR19_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R19_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R20 ----------
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_SCH_NO(rs.getBigDecimal("R20_SCH_NO"));
			obj.setR20_NET_AMT(rs.getBigDecimal("R20_NET_AMT"));
			obj.setR20_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R20_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R21 ----------
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_SCH_NO(rs.getBigDecimal("R21_SCH_NO"));
			obj.setR21_NET_AMT(rs.getBigDecimal("R21_NET_AMT"));
			obj.setR21_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R21_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R22 ----------
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_SCH_NO(rs.getBigDecimal("R22_SCH_NO"));
			obj.setR22_NET_AMT(rs.getBigDecimal("R22_NET_AMT"));
			obj.setR22_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R22_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

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

	public static class M_P_L_Summary_Entity {

		// ---------- R08 ----------
		private BigDecimal R08_SCH_NO;
		private BigDecimal R08_NET_AMT;
		private BigDecimal R08_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R09 ----------
		private String R09_PRODUCT;
		private BigDecimal R09_SCH_NO;
		private BigDecimal R09_NET_AMT;
		private BigDecimal R09_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R10 ----------
		private String R10_PRODUCT;
		private BigDecimal R10_SCH_NO;
		private BigDecimal R10_NET_AMT;
		private BigDecimal R10_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R11 ----------
		private String R11_PRODUCT;
		private BigDecimal R11_SCH_NO;
		private BigDecimal R11_NET_AMT;
		private BigDecimal R11_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R12 ----------
		private String R12_PRODUCT;
		private BigDecimal R12_SCH_NO;
		private BigDecimal R12_NET_AMT;
		private BigDecimal R12_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R13 ----------
		private String R13_PRODUCT;
		private BigDecimal R13_SCH_NO;
		private BigDecimal R13_NET_AMT;
		private BigDecimal R13_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R14 ----------
		private String R14_PRODUCT;
		private BigDecimal R14_SCH_NO;
		private BigDecimal R14_NET_AMT;
		private BigDecimal R14_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R15 ----------
		private String R15_PRODUCT;
		private BigDecimal R15_SCH_NO;
		private BigDecimal R15_NET_AMT;
		private BigDecimal R15_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R16 ----------
		private String R16_PRODUCT;
		private BigDecimal R16_SCH_NO;
		private BigDecimal R16_NET_AMT;
		private BigDecimal R16_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R17 ----------
		private String R17_PRODUCT;
		private BigDecimal R17_SCH_NO;
		private BigDecimal R17_NET_AMT;
		private BigDecimal R17_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R18 ----------
		private String R18_PRODUCT;
		private BigDecimal R18_SCH_NO;
		private BigDecimal R18_NET_AMT;
		private BigDecimal R18_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R19 ----------
		private String R19_PRODUCT;
		private BigDecimal R19_SCH_NO;
		private BigDecimal R19_NET_AMT;
		private BigDecimal R19_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R20 ----------
		private String R20_PRODUCT;
		private BigDecimal R20_SCH_NO;
		private BigDecimal R20_NET_AMT;
		private BigDecimal R20_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R21 ----------
		private String R21_PRODUCT;
		private BigDecimal R21_SCH_NO;
		private BigDecimal R21_NET_AMT;
		private BigDecimal R21_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R22 ----------
		private String R22_PRODUCT;
		private BigDecimal R22_SCH_NO;
		private BigDecimal R22_NET_AMT;
		private BigDecimal R22_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

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

		public BigDecimal getR08_SCH_NO() {
			return R08_SCH_NO;
		}

		public void setR08_SCH_NO(BigDecimal r08_SCH_NO) {
			R08_SCH_NO = r08_SCH_NO;
		}

		public BigDecimal getR08_NET_AMT() {
			return R08_NET_AMT;
		}

		public void setR08_NET_AMT(BigDecimal r08_NET_AMT) {
			R08_NET_AMT = r08_NET_AMT;
		}

		public BigDecimal getR08_BAL_W_BOB_AND_OTHR_SUBS() {
			return R08_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR08_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r08_BAL_W_BOB_AND_OTHR_SUBS) {
			R08_BAL_W_BOB_AND_OTHR_SUBS = r08_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR09_PRODUCT() {
			return R09_PRODUCT;
		}

		public void setR09_PRODUCT(String r09_PRODUCT) {
			R09_PRODUCT = r09_PRODUCT;
		}

		public BigDecimal getR09_SCH_NO() {
			return R09_SCH_NO;
		}

		public void setR09_SCH_NO(BigDecimal r09_SCH_NO) {
			R09_SCH_NO = r09_SCH_NO;
		}

		public BigDecimal getR09_NET_AMT() {
			return R09_NET_AMT;
		}

		public void setR09_NET_AMT(BigDecimal r09_NET_AMT) {
			R09_NET_AMT = r09_NET_AMT;
		}

		public BigDecimal getR09_BAL_W_BOB_AND_OTHR_SUBS() {
			return R09_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR09_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r09_BAL_W_BOB_AND_OTHR_SUBS) {
			R09_BAL_W_BOB_AND_OTHR_SUBS = r09_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_SCH_NO() {
			return R10_SCH_NO;
		}

		public void setR10_SCH_NO(BigDecimal r10_SCH_NO) {
			R10_SCH_NO = r10_SCH_NO;
		}

		public BigDecimal getR10_NET_AMT() {
			return R10_NET_AMT;
		}

		public void setR10_NET_AMT(BigDecimal r10_NET_AMT) {
			R10_NET_AMT = r10_NET_AMT;
		}

		public BigDecimal getR10_BAL_W_BOB_AND_OTHR_SUBS() {
			return R10_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR10_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r10_BAL_W_BOB_AND_OTHR_SUBS) {
			R10_BAL_W_BOB_AND_OTHR_SUBS = r10_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_SCH_NO() {
			return R11_SCH_NO;
		}

		public void setR11_SCH_NO(BigDecimal r11_SCH_NO) {
			R11_SCH_NO = r11_SCH_NO;
		}

		public BigDecimal getR11_NET_AMT() {
			return R11_NET_AMT;
		}

		public void setR11_NET_AMT(BigDecimal r11_NET_AMT) {
			R11_NET_AMT = r11_NET_AMT;
		}

		public BigDecimal getR11_BAL_W_BOB_AND_OTHR_SUBS() {
			return R11_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR11_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r11_BAL_W_BOB_AND_OTHR_SUBS) {
			R11_BAL_W_BOB_AND_OTHR_SUBS = r11_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_SCH_NO() {
			return R12_SCH_NO;
		}

		public void setR12_SCH_NO(BigDecimal r12_SCH_NO) {
			R12_SCH_NO = r12_SCH_NO;
		}

		public BigDecimal getR12_NET_AMT() {
			return R12_NET_AMT;
		}

		public void setR12_NET_AMT(BigDecimal r12_NET_AMT) {
			R12_NET_AMT = r12_NET_AMT;
		}

		public BigDecimal getR12_BAL_W_BOB_AND_OTHR_SUBS() {
			return R12_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR12_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r12_BAL_W_BOB_AND_OTHR_SUBS) {
			R12_BAL_W_BOB_AND_OTHR_SUBS = r12_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_SCH_NO() {
			return R13_SCH_NO;
		}

		public void setR13_SCH_NO(BigDecimal r13_SCH_NO) {
			R13_SCH_NO = r13_SCH_NO;
		}

		public BigDecimal getR13_NET_AMT() {
			return R13_NET_AMT;
		}

		public void setR13_NET_AMT(BigDecimal r13_NET_AMT) {
			R13_NET_AMT = r13_NET_AMT;
		}

		public BigDecimal getR13_BAL_W_BOB_AND_OTHR_SUBS() {
			return R13_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR13_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r13_BAL_W_BOB_AND_OTHR_SUBS) {
			R13_BAL_W_BOB_AND_OTHR_SUBS = r13_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_SCH_NO() {
			return R14_SCH_NO;
		}

		public void setR14_SCH_NO(BigDecimal r14_SCH_NO) {
			R14_SCH_NO = r14_SCH_NO;
		}

		public BigDecimal getR14_NET_AMT() {
			return R14_NET_AMT;
		}

		public void setR14_NET_AMT(BigDecimal r14_NET_AMT) {
			R14_NET_AMT = r14_NET_AMT;
		}

		public BigDecimal getR14_BAL_W_BOB_AND_OTHR_SUBS() {
			return R14_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR14_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r14_BAL_W_BOB_AND_OTHR_SUBS) {
			R14_BAL_W_BOB_AND_OTHR_SUBS = r14_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_SCH_NO() {
			return R15_SCH_NO;
		}

		public void setR15_SCH_NO(BigDecimal r15_SCH_NO) {
			R15_SCH_NO = r15_SCH_NO;
		}

		public BigDecimal getR15_NET_AMT() {
			return R15_NET_AMT;
		}

		public void setR15_NET_AMT(BigDecimal r15_NET_AMT) {
			R15_NET_AMT = r15_NET_AMT;
		}

		public BigDecimal getR15_BAL_W_BOB_AND_OTHR_SUBS() {
			return R15_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR15_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r15_BAL_W_BOB_AND_OTHR_SUBS) {
			R15_BAL_W_BOB_AND_OTHR_SUBS = r15_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_SCH_NO() {
			return R16_SCH_NO;
		}

		public void setR16_SCH_NO(BigDecimal r16_SCH_NO) {
			R16_SCH_NO = r16_SCH_NO;
		}

		public BigDecimal getR16_NET_AMT() {
			return R16_NET_AMT;
		}

		public void setR16_NET_AMT(BigDecimal r16_NET_AMT) {
			R16_NET_AMT = r16_NET_AMT;
		}

		public BigDecimal getR16_BAL_W_BOB_AND_OTHR_SUBS() {
			return R16_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR16_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r16_BAL_W_BOB_AND_OTHR_SUBS) {
			R16_BAL_W_BOB_AND_OTHR_SUBS = r16_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_SCH_NO() {
			return R17_SCH_NO;
		}

		public void setR17_SCH_NO(BigDecimal r17_SCH_NO) {
			R17_SCH_NO = r17_SCH_NO;
		}

		public BigDecimal getR17_NET_AMT() {
			return R17_NET_AMT;
		}

		public void setR17_NET_AMT(BigDecimal r17_NET_AMT) {
			R17_NET_AMT = r17_NET_AMT;
		}

		public BigDecimal getR17_BAL_W_BOB_AND_OTHR_SUBS() {
			return R17_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR17_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r17_BAL_W_BOB_AND_OTHR_SUBS) {
			R17_BAL_W_BOB_AND_OTHR_SUBS = r17_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_SCH_NO() {
			return R18_SCH_NO;
		}

		public void setR18_SCH_NO(BigDecimal r18_SCH_NO) {
			R18_SCH_NO = r18_SCH_NO;
		}

		public BigDecimal getR18_NET_AMT() {
			return R18_NET_AMT;
		}

		public void setR18_NET_AMT(BigDecimal r18_NET_AMT) {
			R18_NET_AMT = r18_NET_AMT;
		}

		public BigDecimal getR18_BAL_W_BOB_AND_OTHR_SUBS() {
			return R18_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR18_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r18_BAL_W_BOB_AND_OTHR_SUBS) {
			R18_BAL_W_BOB_AND_OTHR_SUBS = r18_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_SCH_NO() {
			return R19_SCH_NO;
		}

		public void setR19_SCH_NO(BigDecimal r19_SCH_NO) {
			R19_SCH_NO = r19_SCH_NO;
		}

		public BigDecimal getR19_NET_AMT() {
			return R19_NET_AMT;
		}

		public void setR19_NET_AMT(BigDecimal r19_NET_AMT) {
			R19_NET_AMT = r19_NET_AMT;
		}

		public BigDecimal getR19_BAL_W_BOB_AND_OTHR_SUBS() {
			return R19_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR19_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r19_BAL_W_BOB_AND_OTHR_SUBS) {
			R19_BAL_W_BOB_AND_OTHR_SUBS = r19_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_SCH_NO() {
			return R20_SCH_NO;
		}

		public void setR20_SCH_NO(BigDecimal r20_SCH_NO) {
			R20_SCH_NO = r20_SCH_NO;
		}

		public BigDecimal getR20_NET_AMT() {
			return R20_NET_AMT;
		}

		public void setR20_NET_AMT(BigDecimal r20_NET_AMT) {
			R20_NET_AMT = r20_NET_AMT;
		}

		public BigDecimal getR20_BAL_W_BOB_AND_OTHR_SUBS() {
			return R20_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR20_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r20_BAL_W_BOB_AND_OTHR_SUBS) {
			R20_BAL_W_BOB_AND_OTHR_SUBS = r20_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_SCH_NO() {
			return R21_SCH_NO;
		}

		public void setR21_SCH_NO(BigDecimal r21_SCH_NO) {
			R21_SCH_NO = r21_SCH_NO;
		}

		public BigDecimal getR21_NET_AMT() {
			return R21_NET_AMT;
		}

		public void setR21_NET_AMT(BigDecimal r21_NET_AMT) {
			R21_NET_AMT = r21_NET_AMT;
		}

		public BigDecimal getR21_BAL_W_BOB_AND_OTHR_SUBS() {
			return R21_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR21_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r21_BAL_W_BOB_AND_OTHR_SUBS) {
			R21_BAL_W_BOB_AND_OTHR_SUBS = r21_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_SCH_NO() {
			return R22_SCH_NO;
		}

		public void setR22_SCH_NO(BigDecimal r22_SCH_NO) {
			R22_SCH_NO = r22_SCH_NO;
		}

		public BigDecimal getR22_NET_AMT() {
			return R22_NET_AMT;
		}

		public void setR22_NET_AMT(BigDecimal r22_NET_AMT) {
			R22_NET_AMT = r22_NET_AMT;
		}

		public BigDecimal getR22_BAL_W_BOB_AND_OTHR_SUBS() {
			return R22_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR22_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r22_BAL_W_BOB_AND_OTHR_SUBS) {
			R22_BAL_W_BOB_AND_OTHR_SUBS = r22_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
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

	public class M_P_LDetailRowMapper implements RowMapper<M_P_L_Detail_Entity> {

		@Override
		public M_P_L_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_P_L_Detail_Entity obj = new M_P_L_Detail_Entity();

			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			return obj;
		}
	}

	public class M_P_L_Detail_Entity {

		@Column(name = "CUST_ID")
		private String cust_id;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acct_number;

		@Column(name = "ACCT_NAME")
		private String acct_name;

		@Column(name = "DATA_TYPE")
		private String data_type;

		@Column(name = "REPORT_LABEL")
		private String report_label;

		@Column(name = "REPORT_REMARKS")
		private String report_remarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modification_remarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String data_entry_version;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acct_balance_in_pula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;

		@Column(name = "REPORT_NAME")
		private String report_name;

		@Column(name = "CREATE_USER")
		private String create_user;

		@Column(name = "CREATE_TIME")
		private Date create_time;

		@Column(name = "MODIFY_USER")
		private String modify_user;

		@Column(name = "MODIFY_TIME")
		private Date modify_time;

		@Column(name = "VERIFY_USER")
		private String verify_user;

		@Column(name = "VERIFY_TIME")
		private Date verify_time;

		@Column(name = "ENTITY_FLG")
		private Character entity_flg;

		@Column(name = "MODIFY_FLG")
		private Character modify_flg;

		@Column(name = "DEL_FLG")
		private Character del_flg;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String report_addl_criteria_1;

		public String getCust_id() {
			return cust_id;
		}

		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}

		public String getAcct_number() {
			return acct_number;
		}

		public void setAcct_number(String acct_number) {
			this.acct_number = acct_number;
		}

		public String getAcct_name() {
			return acct_name;
		}

		public void setAcct_name(String acct_name) {
			this.acct_name = acct_name;
		}

		public String getData_type() {
			return data_type;
		}

		public void setData_type(String data_type) {
			this.data_type = data_type;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
		}

		public String getModification_remarks() {
			return modification_remarks;
		}

		public void setModification_remarks(String modification_remarks) {
			this.modification_remarks = modification_remarks;
		}

		public String getData_entry_version() {
			return data_entry_version;
		}

		public void setData_entry_version(String data_entry_version) {
			this.data_entry_version = data_entry_version;
		}

		public BigDecimal getAcct_balance_in_pula() {
			return acct_balance_in_pula;
		}

		public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
			this.acct_balance_in_pula = acct_balance_in_pula;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
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

		public Character getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(Character entity_flg) {
			this.entity_flg = entity_flg;
		}

		public Character getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(Character modify_flg) {
			this.modify_flg = modify_flg;
		}

		public Character getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(Character del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
		}

	}

	// =========================================================
	// ROW MAPPER ARCHIVAL SUMMARY
	// =========================================================

	class M_P_LArchivalRowMapper implements RowMapper<M_P_L_Archival_Summary_Entity> {

		@Override
		public M_P_L_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_P_L_Archival_Summary_Entity obj = new M_P_L_Archival_Summary_Entity();

			// ---------- R08 ----------
			obj.setR08_SCH_NO(rs.getBigDecimal("R08_SCH_NO"));
			obj.setR08_NET_AMT(rs.getBigDecimal("R08_NET_AMT"));
			obj.setR08_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R08_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R09 ----------
			obj.setR09_PRODUCT(rs.getString("R09_PRODUCT"));
			obj.setR09_SCH_NO(rs.getBigDecimal("R09_SCH_NO"));
			obj.setR09_NET_AMT(rs.getBigDecimal("R09_NET_AMT"));
			obj.setR09_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R09_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R10 ----------
			obj.setR10_PRODUCT(rs.getString("R10_PRODUCT"));
			obj.setR10_SCH_NO(rs.getBigDecimal("R10_SCH_NO"));
			obj.setR10_NET_AMT(rs.getBigDecimal("R10_NET_AMT"));
			obj.setR10_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R10_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R11 ----------
			obj.setR11_PRODUCT(rs.getString("R11_PRODUCT"));
			obj.setR11_SCH_NO(rs.getBigDecimal("R11_SCH_NO"));
			obj.setR11_NET_AMT(rs.getBigDecimal("R11_NET_AMT"));
			obj.setR11_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R11_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R12 ----------
			obj.setR12_PRODUCT(rs.getString("R12_PRODUCT"));
			obj.setR12_SCH_NO(rs.getBigDecimal("R12_SCH_NO"));
			obj.setR12_NET_AMT(rs.getBigDecimal("R12_NET_AMT"));
			obj.setR12_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R12_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R13 ----------
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_SCH_NO(rs.getBigDecimal("R13_SCH_NO"));
			obj.setR13_NET_AMT(rs.getBigDecimal("R13_NET_AMT"));
			obj.setR13_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R13_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R14 ----------
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_SCH_NO(rs.getBigDecimal("R14_SCH_NO"));
			obj.setR14_NET_AMT(rs.getBigDecimal("R14_NET_AMT"));
			obj.setR14_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R14_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R15 ----------
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_SCH_NO(rs.getBigDecimal("R15_SCH_NO"));
			obj.setR15_NET_AMT(rs.getBigDecimal("R15_NET_AMT"));
			obj.setR15_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R15_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R16 ----------
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_SCH_NO(rs.getBigDecimal("R16_SCH_NO"));
			obj.setR16_NET_AMT(rs.getBigDecimal("R16_NET_AMT"));
			obj.setR16_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R16_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R17 ----------
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_SCH_NO(rs.getBigDecimal("R17_SCH_NO"));
			obj.setR17_NET_AMT(rs.getBigDecimal("R17_NET_AMT"));
			obj.setR17_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R17_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R18 ----------
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_SCH_NO(rs.getBigDecimal("R18_SCH_NO"));
			obj.setR18_NET_AMT(rs.getBigDecimal("R18_NET_AMT"));
			obj.setR18_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R18_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R19 ----------
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_SCH_NO(rs.getBigDecimal("R19_SCH_NO"));
			obj.setR19_NET_AMT(rs.getBigDecimal("R19_NET_AMT"));
			obj.setR19_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R19_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R20 ----------
			obj.setR20_PRODUCT(rs.getString("R20_PRODUCT"));
			obj.setR20_SCH_NO(rs.getBigDecimal("R20_SCH_NO"));
			obj.setR20_NET_AMT(rs.getBigDecimal("R20_NET_AMT"));
			obj.setR20_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R20_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R21 ----------
			obj.setR21_PRODUCT(rs.getString("R21_PRODUCT"));
			obj.setR21_SCH_NO(rs.getBigDecimal("R21_SCH_NO"));
			obj.setR21_NET_AMT(rs.getBigDecimal("R21_NET_AMT"));
			obj.setR21_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R21_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

			// ---------- R22 ----------
			obj.setR22_PRODUCT(rs.getString("R22_PRODUCT"));
			obj.setR22_SCH_NO(rs.getBigDecimal("R22_SCH_NO"));
			obj.setR22_NET_AMT(rs.getBigDecimal("R22_NET_AMT"));
			obj.setR22_BAL_W_BOB_AND_OTHR_SUBS(rs.getBigDecimal("R22_BAL_W_BOB_AND_OTHR_SUBS"));
			obj.setR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS(rs.getBigDecimal("R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS"));

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

	public static class M_P_L_Archival_Summary_Entity {

		// ---------- R08 ----------
		private BigDecimal R08_SCH_NO;
		private BigDecimal R08_NET_AMT;
		private BigDecimal R08_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R09 ----------
		private String R09_PRODUCT;
		private BigDecimal R09_SCH_NO;
		private BigDecimal R09_NET_AMT;
		private BigDecimal R09_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R10 ----------
		private String R10_PRODUCT;
		private BigDecimal R10_SCH_NO;
		private BigDecimal R10_NET_AMT;
		private BigDecimal R10_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R11 ----------
		private String R11_PRODUCT;
		private BigDecimal R11_SCH_NO;
		private BigDecimal R11_NET_AMT;
		private BigDecimal R11_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R12 ----------
		private String R12_PRODUCT;
		private BigDecimal R12_SCH_NO;
		private BigDecimal R12_NET_AMT;
		private BigDecimal R12_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R13 ----------
		private String R13_PRODUCT;
		private BigDecimal R13_SCH_NO;
		private BigDecimal R13_NET_AMT;
		private BigDecimal R13_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R14 ----------
		private String R14_PRODUCT;
		private BigDecimal R14_SCH_NO;
		private BigDecimal R14_NET_AMT;
		private BigDecimal R14_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R15 ----------
		private String R15_PRODUCT;
		private BigDecimal R15_SCH_NO;
		private BigDecimal R15_NET_AMT;
		private BigDecimal R15_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R16 ----------
		private String R16_PRODUCT;
		private BigDecimal R16_SCH_NO;
		private BigDecimal R16_NET_AMT;
		private BigDecimal R16_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R17 ----------
		private String R17_PRODUCT;
		private BigDecimal R17_SCH_NO;
		private BigDecimal R17_NET_AMT;
		private BigDecimal R17_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R18 ----------
		private String R18_PRODUCT;
		private BigDecimal R18_SCH_NO;
		private BigDecimal R18_NET_AMT;
		private BigDecimal R18_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R19 ----------
		private String R19_PRODUCT;
		private BigDecimal R19_SCH_NO;
		private BigDecimal R19_NET_AMT;
		private BigDecimal R19_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R20 ----------
		private String R20_PRODUCT;
		private BigDecimal R20_SCH_NO;
		private BigDecimal R20_NET_AMT;
		private BigDecimal R20_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R21 ----------
		private String R21_PRODUCT;
		private BigDecimal R21_SCH_NO;
		private BigDecimal R21_NET_AMT;
		private BigDecimal R21_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

		// ---------- R22 ----------
		private String R22_PRODUCT;
		private BigDecimal R22_SCH_NO;
		private BigDecimal R22_NET_AMT;
		private BigDecimal R22_BAL_W_BOB_AND_OTHR_SUBS;
		private BigDecimal R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;

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

		public BigDecimal getR08_SCH_NO() {
			return R08_SCH_NO;
		}

		public void setR08_SCH_NO(BigDecimal r08_SCH_NO) {
			R08_SCH_NO = r08_SCH_NO;
		}

		public BigDecimal getR08_NET_AMT() {
			return R08_NET_AMT;
		}

		public void setR08_NET_AMT(BigDecimal r08_NET_AMT) {
			R08_NET_AMT = r08_NET_AMT;
		}

		public BigDecimal getR08_BAL_W_BOB_AND_OTHR_SUBS() {
			return R08_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR08_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r08_BAL_W_BOB_AND_OTHR_SUBS) {
			R08_BAL_W_BOB_AND_OTHR_SUBS = r08_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR08_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R08_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r08_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR09_PRODUCT() {
			return R09_PRODUCT;
		}

		public void setR09_PRODUCT(String r09_PRODUCT) {
			R09_PRODUCT = r09_PRODUCT;
		}

		public BigDecimal getR09_SCH_NO() {
			return R09_SCH_NO;
		}

		public void setR09_SCH_NO(BigDecimal r09_SCH_NO) {
			R09_SCH_NO = r09_SCH_NO;
		}

		public BigDecimal getR09_NET_AMT() {
			return R09_NET_AMT;
		}

		public void setR09_NET_AMT(BigDecimal r09_NET_AMT) {
			R09_NET_AMT = r09_NET_AMT;
		}

		public BigDecimal getR09_BAL_W_BOB_AND_OTHR_SUBS() {
			return R09_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR09_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r09_BAL_W_BOB_AND_OTHR_SUBS) {
			R09_BAL_W_BOB_AND_OTHR_SUBS = r09_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R09_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r09_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR10_PRODUCT() {
			return R10_PRODUCT;
		}

		public void setR10_PRODUCT(String r10_PRODUCT) {
			R10_PRODUCT = r10_PRODUCT;
		}

		public BigDecimal getR10_SCH_NO() {
			return R10_SCH_NO;
		}

		public void setR10_SCH_NO(BigDecimal r10_SCH_NO) {
			R10_SCH_NO = r10_SCH_NO;
		}

		public BigDecimal getR10_NET_AMT() {
			return R10_NET_AMT;
		}

		public void setR10_NET_AMT(BigDecimal r10_NET_AMT) {
			R10_NET_AMT = r10_NET_AMT;
		}

		public BigDecimal getR10_BAL_W_BOB_AND_OTHR_SUBS() {
			return R10_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR10_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r10_BAL_W_BOB_AND_OTHR_SUBS) {
			R10_BAL_W_BOB_AND_OTHR_SUBS = r10_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R10_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r10_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR11_PRODUCT() {
			return R11_PRODUCT;
		}

		public void setR11_PRODUCT(String r11_PRODUCT) {
			R11_PRODUCT = r11_PRODUCT;
		}

		public BigDecimal getR11_SCH_NO() {
			return R11_SCH_NO;
		}

		public void setR11_SCH_NO(BigDecimal r11_SCH_NO) {
			R11_SCH_NO = r11_SCH_NO;
		}

		public BigDecimal getR11_NET_AMT() {
			return R11_NET_AMT;
		}

		public void setR11_NET_AMT(BigDecimal r11_NET_AMT) {
			R11_NET_AMT = r11_NET_AMT;
		}

		public BigDecimal getR11_BAL_W_BOB_AND_OTHR_SUBS() {
			return R11_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR11_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r11_BAL_W_BOB_AND_OTHR_SUBS) {
			R11_BAL_W_BOB_AND_OTHR_SUBS = r11_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR11_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R11_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r11_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR12_PRODUCT() {
			return R12_PRODUCT;
		}

		public void setR12_PRODUCT(String r12_PRODUCT) {
			R12_PRODUCT = r12_PRODUCT;
		}

		public BigDecimal getR12_SCH_NO() {
			return R12_SCH_NO;
		}

		public void setR12_SCH_NO(BigDecimal r12_SCH_NO) {
			R12_SCH_NO = r12_SCH_NO;
		}

		public BigDecimal getR12_NET_AMT() {
			return R12_NET_AMT;
		}

		public void setR12_NET_AMT(BigDecimal r12_NET_AMT) {
			R12_NET_AMT = r12_NET_AMT;
		}

		public BigDecimal getR12_BAL_W_BOB_AND_OTHR_SUBS() {
			return R12_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR12_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r12_BAL_W_BOB_AND_OTHR_SUBS) {
			R12_BAL_W_BOB_AND_OTHR_SUBS = r12_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR12_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R12_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r12_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String r13_PRODUCT) {
			R13_PRODUCT = r13_PRODUCT;
		}

		public BigDecimal getR13_SCH_NO() {
			return R13_SCH_NO;
		}

		public void setR13_SCH_NO(BigDecimal r13_SCH_NO) {
			R13_SCH_NO = r13_SCH_NO;
		}

		public BigDecimal getR13_NET_AMT() {
			return R13_NET_AMT;
		}

		public void setR13_NET_AMT(BigDecimal r13_NET_AMT) {
			R13_NET_AMT = r13_NET_AMT;
		}

		public BigDecimal getR13_BAL_W_BOB_AND_OTHR_SUBS() {
			return R13_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR13_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r13_BAL_W_BOB_AND_OTHR_SUBS) {
			R13_BAL_W_BOB_AND_OTHR_SUBS = r13_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R13_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r13_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String r14_PRODUCT) {
			R14_PRODUCT = r14_PRODUCT;
		}

		public BigDecimal getR14_SCH_NO() {
			return R14_SCH_NO;
		}

		public void setR14_SCH_NO(BigDecimal r14_SCH_NO) {
			R14_SCH_NO = r14_SCH_NO;
		}

		public BigDecimal getR14_NET_AMT() {
			return R14_NET_AMT;
		}

		public void setR14_NET_AMT(BigDecimal r14_NET_AMT) {
			R14_NET_AMT = r14_NET_AMT;
		}

		public BigDecimal getR14_BAL_W_BOB_AND_OTHR_SUBS() {
			return R14_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR14_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r14_BAL_W_BOB_AND_OTHR_SUBS) {
			R14_BAL_W_BOB_AND_OTHR_SUBS = r14_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R14_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r14_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String r15_PRODUCT) {
			R15_PRODUCT = r15_PRODUCT;
		}

		public BigDecimal getR15_SCH_NO() {
			return R15_SCH_NO;
		}

		public void setR15_SCH_NO(BigDecimal r15_SCH_NO) {
			R15_SCH_NO = r15_SCH_NO;
		}

		public BigDecimal getR15_NET_AMT() {
			return R15_NET_AMT;
		}

		public void setR15_NET_AMT(BigDecimal r15_NET_AMT) {
			R15_NET_AMT = r15_NET_AMT;
		}

		public BigDecimal getR15_BAL_W_BOB_AND_OTHR_SUBS() {
			return R15_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR15_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r15_BAL_W_BOB_AND_OTHR_SUBS) {
			R15_BAL_W_BOB_AND_OTHR_SUBS = r15_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R15_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r15_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String r16_PRODUCT) {
			R16_PRODUCT = r16_PRODUCT;
		}

		public BigDecimal getR16_SCH_NO() {
			return R16_SCH_NO;
		}

		public void setR16_SCH_NO(BigDecimal r16_SCH_NO) {
			R16_SCH_NO = r16_SCH_NO;
		}

		public BigDecimal getR16_NET_AMT() {
			return R16_NET_AMT;
		}

		public void setR16_NET_AMT(BigDecimal r16_NET_AMT) {
			R16_NET_AMT = r16_NET_AMT;
		}

		public BigDecimal getR16_BAL_W_BOB_AND_OTHR_SUBS() {
			return R16_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR16_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r16_BAL_W_BOB_AND_OTHR_SUBS) {
			R16_BAL_W_BOB_AND_OTHR_SUBS = r16_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR16_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R16_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r16_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String r17_PRODUCT) {
			R17_PRODUCT = r17_PRODUCT;
		}

		public BigDecimal getR17_SCH_NO() {
			return R17_SCH_NO;
		}

		public void setR17_SCH_NO(BigDecimal r17_SCH_NO) {
			R17_SCH_NO = r17_SCH_NO;
		}

		public BigDecimal getR17_NET_AMT() {
			return R17_NET_AMT;
		}

		public void setR17_NET_AMT(BigDecimal r17_NET_AMT) {
			R17_NET_AMT = r17_NET_AMT;
		}

		public BigDecimal getR17_BAL_W_BOB_AND_OTHR_SUBS() {
			return R17_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR17_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r17_BAL_W_BOB_AND_OTHR_SUBS) {
			R17_BAL_W_BOB_AND_OTHR_SUBS = r17_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR17_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R17_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r17_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String r18_PRODUCT) {
			R18_PRODUCT = r18_PRODUCT;
		}

		public BigDecimal getR18_SCH_NO() {
			return R18_SCH_NO;
		}

		public void setR18_SCH_NO(BigDecimal r18_SCH_NO) {
			R18_SCH_NO = r18_SCH_NO;
		}

		public BigDecimal getR18_NET_AMT() {
			return R18_NET_AMT;
		}

		public void setR18_NET_AMT(BigDecimal r18_NET_AMT) {
			R18_NET_AMT = r18_NET_AMT;
		}

		public BigDecimal getR18_BAL_W_BOB_AND_OTHR_SUBS() {
			return R18_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR18_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r18_BAL_W_BOB_AND_OTHR_SUBS) {
			R18_BAL_W_BOB_AND_OTHR_SUBS = r18_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR18_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R18_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r18_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String r19_PRODUCT) {
			R19_PRODUCT = r19_PRODUCT;
		}

		public BigDecimal getR19_SCH_NO() {
			return R19_SCH_NO;
		}

		public void setR19_SCH_NO(BigDecimal r19_SCH_NO) {
			R19_SCH_NO = r19_SCH_NO;
		}

		public BigDecimal getR19_NET_AMT() {
			return R19_NET_AMT;
		}

		public void setR19_NET_AMT(BigDecimal r19_NET_AMT) {
			R19_NET_AMT = r19_NET_AMT;
		}

		public BigDecimal getR19_BAL_W_BOB_AND_OTHR_SUBS() {
			return R19_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR19_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r19_BAL_W_BOB_AND_OTHR_SUBS) {
			R19_BAL_W_BOB_AND_OTHR_SUBS = r19_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R19_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r19_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR20_PRODUCT() {
			return R20_PRODUCT;
		}

		public void setR20_PRODUCT(String r20_PRODUCT) {
			R20_PRODUCT = r20_PRODUCT;
		}

		public BigDecimal getR20_SCH_NO() {
			return R20_SCH_NO;
		}

		public void setR20_SCH_NO(BigDecimal r20_SCH_NO) {
			R20_SCH_NO = r20_SCH_NO;
		}

		public BigDecimal getR20_NET_AMT() {
			return R20_NET_AMT;
		}

		public void setR20_NET_AMT(BigDecimal r20_NET_AMT) {
			R20_NET_AMT = r20_NET_AMT;
		}

		public BigDecimal getR20_BAL_W_BOB_AND_OTHR_SUBS() {
			return R20_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR20_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r20_BAL_W_BOB_AND_OTHR_SUBS) {
			R20_BAL_W_BOB_AND_OTHR_SUBS = r20_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R20_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r20_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR21_PRODUCT() {
			return R21_PRODUCT;
		}

		public void setR21_PRODUCT(String r21_PRODUCT) {
			R21_PRODUCT = r21_PRODUCT;
		}

		public BigDecimal getR21_SCH_NO() {
			return R21_SCH_NO;
		}

		public void setR21_SCH_NO(BigDecimal r21_SCH_NO) {
			R21_SCH_NO = r21_SCH_NO;
		}

		public BigDecimal getR21_NET_AMT() {
			return R21_NET_AMT;
		}

		public void setR21_NET_AMT(BigDecimal r21_NET_AMT) {
			R21_NET_AMT = r21_NET_AMT;
		}

		public BigDecimal getR21_BAL_W_BOB_AND_OTHR_SUBS() {
			return R21_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR21_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r21_BAL_W_BOB_AND_OTHR_SUBS) {
			R21_BAL_W_BOB_AND_OTHR_SUBS = r21_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R21_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r21_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public String getR22_PRODUCT() {
			return R22_PRODUCT;
		}

		public void setR22_PRODUCT(String r22_PRODUCT) {
			R22_PRODUCT = r22_PRODUCT;
		}

		public BigDecimal getR22_SCH_NO() {
			return R22_SCH_NO;
		}

		public void setR22_SCH_NO(BigDecimal r22_SCH_NO) {
			R22_SCH_NO = r22_SCH_NO;
		}

		public BigDecimal getR22_NET_AMT() {
			return R22_NET_AMT;
		}

		public void setR22_NET_AMT(BigDecimal r22_NET_AMT) {
			R22_NET_AMT = r22_NET_AMT;
		}

		public BigDecimal getR22_BAL_W_BOB_AND_OTHR_SUBS() {
			return R22_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public void setR22_BAL_W_BOB_AND_OTHR_SUBS(BigDecimal r22_BAL_W_BOB_AND_OTHR_SUBS) {
			R22_BAL_W_BOB_AND_OTHR_SUBS = r22_BAL_W_BOB_AND_OTHR_SUBS;
		}

		public BigDecimal getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS() {
			return R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
		}

		public void setR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS(BigDecimal r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS) {
			R22_BAL_AS_PER_STMT_OF_BOB_BRNCHS = r22_BAL_AS_PER_STMT_OF_BOB_BRNCHS;
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

	public class M_P_LArchivalDetailRowMapper implements RowMapper<M_P_L_Archival_Detail_Entity> {

		@Override
		public M_P_L_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_P_L_Archival_Detail_Entity obj = new M_P_L_Archival_Detail_Entity();

			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));
			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getDate("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getDate("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getDate("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG") != null ? rs.getString("ENTITY_FLG").charAt(0) : ' ');
			obj.setModify_flg(rs.getString("MODIFY_FLG") != null ? rs.getString("MODIFY_FLG").charAt(0) : ' ');
			obj.setDel_flg(rs.getString("DEL_FLG") != null ? rs.getString("DEL_FLG").charAt(0) : ' ');

			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			return obj;
		}
	}

	public class M_P_L_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String cust_id;

		@Id
		@Column(name = "ACCT_NUMBER")
		private String acct_number;

		@Column(name = "ACCT_NAME")
		private String acct_name;

		@Column(name = "DATA_TYPE")
		private String data_type;

		@Column(name = "REPORT_LABEL")
		private String report_label;

		@Column(name = "REPORT_REMARKS")
		private String report_remarks;

		@Column(name = "MODIFICATION_REMARKS")
		private String modification_remarks;

		@Column(name = "DATA_ENTRY_VERSION")
		private String data_entry_version;

		@Column(name = "ACCT_BALANCE_IN_PULA")
		private BigDecimal acct_balance_in_pula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;

		@Column(name = "REPORT_NAME")
		private String report_name;

		@Column(name = "CREATE_USER")
		private String create_user;

		@Column(name = "CREATE_TIME")
		private Date create_time;

		@Column(name = "MODIFY_USER")
		private String modify_user;

		@Column(name = "MODIFY_TIME")
		private Date modify_time;

		@Column(name = "VERIFY_USER")
		private String verify_user;

		@Column(name = "VERIFY_TIME")
		private Date verify_time;

		@Column(name = "ENTITY_FLG")
		private Character entity_flg;

		@Column(name = "MODIFY_FLG")
		private Character modify_flg;

		@Column(name = "DEL_FLG")
		private Character del_flg;

		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String report_addl_criteria_1;

		public String getCust_id() {
			return cust_id;
		}

		public void setCust_id(String cust_id) {
			this.cust_id = cust_id;
		}

		public String getAcct_number() {
			return acct_number;
		}

		public void setAcct_number(String acct_number) {
			this.acct_number = acct_number;
		}

		public String getAcct_name() {
			return acct_name;
		}

		public void setAcct_name(String acct_name) {
			this.acct_name = acct_name;
		}

		public String getData_type() {
			return data_type;
		}

		public void setData_type(String data_type) {
			this.data_type = data_type;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_remarks() {
			return report_remarks;
		}

		public void setReport_remarks(String report_remarks) {
			this.report_remarks = report_remarks;
		}

		public String getModification_remarks() {
			return modification_remarks;
		}

		public void setModification_remarks(String modification_remarks) {
			this.modification_remarks = modification_remarks;
		}

		public String getData_entry_version() {
			return data_entry_version;
		}

		public void setData_entry_version(String data_entry_version) {
			this.data_entry_version = data_entry_version;
		}

		public BigDecimal getAcct_balance_in_pula() {
			return acct_balance_in_pula;
		}

		public void setAcct_balance_in_pula(BigDecimal acct_balance_in_pula) {
			this.acct_balance_in_pula = acct_balance_in_pula;
		}

		public Date getReport_date() {
			return report_date;
		}

		public void setReport_date(Date report_date) {
			this.report_date = report_date;
		}

		public String getReport_name() {
			return report_name;
		}

		public void setReport_name(String report_name) {
			this.report_name = report_name;
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

		public Character getEntity_flg() {
			return entity_flg;
		}

		public void setEntity_flg(Character entity_flg) {
			this.entity_flg = entity_flg;
		}

		public Character getModify_flg() {
			return modify_flg;
		}

		public void setModify_flg(Character modify_flg) {
			this.modify_flg = modify_flg;
		}

		public Character getDel_flg() {
			return del_flg;
		}

		public void setDel_flg(Character del_flg) {
			this.del_flg = del_flg;
		}

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
		}

	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_P_LView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {
		ModelAndView mv = new ModelAndView();

		if (type.equals("ARCHIVAL") & version != null) {
			List<M_P_L_Archival_Summary_Entity> T1Master = new ArrayList<M_P_L_Archival_Summary_Entity>();

			try {
				Date dt = dateformat.parse(todate);
				T1Master = getdatabydateListarchival(dt, version);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);

		} else {
			List<M_P_L_Summary_Entity> T1Master = new ArrayList<M_P_L_Summary_Entity>();

			try {
				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate(dt);

			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
//			mv.addObject("reportsummary1", T2Master);

		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_P_L");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

//	public ModelAndView getM_P_LcurrentDtl(String reportId, String fromdate, String todate, String currency,
//			  String dtltype, Pageable pageable, String Filter, String type, String version) {
//
//	int pageSize = pageable != null ? pageable.getPageSize() : 10;
//	int currentPage = pageable != null ? pageable.getPageNumber() : 0;
//	int totalPages = 0;
//
//	ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();
//
//	try {
//		Date parsedDate = null;
//		if (todate != null && !todate.isEmpty()) {
//			parsedDate = dateformat.parse(todate);
//		}
//
//		String rowId = null;
//		String columnId = null;
//
//		// ✅ Split filter string into rowId & columnId
//		if (Filter != null && Filter.contains(",")) {
//			String[] parts = Filter.split(",");
//			if (parts.length >= 2) {
//				rowId = parts[0];
//				columnId = parts[1];
//			}
//		}
//	
//		if ("ARCHIVAL".equals(type) && version != null) {
//			// 🔹 Archival branch
//			List<M_P_L_Archival_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_M_P_L_Archival_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate, version);
//			} else {
//				T1Dt1 = BRRS_M_P_L_Archival_Detail_Repo.getdatabydateList(parsedDate, version);					
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//
//		} else {
//			// 🔹 Current branch
//			List<M_P_L_Detail_Entity> T1Dt1;
//			if (rowId != null && columnId != null) {
//				T1Dt1 = BRRS_M_P_L_Detail_Repo.GetDataByRowIdAndColumnId(rowId, columnId, parsedDate);
//			} else {
//				T1Dt1 = BRRS_M_P_L_Detail_Repo.getdatabydateList(parsedDate);
//				totalPages = BRRS_M_P_L_Detail_Repo.getdatacount(parsedDate);
//				mv.addObject("pagination", "YES");
//			}
//
//			mv.addObject("reportdetails", T1Dt1);
//			mv.addObject("reportmaster12", T1Dt1);
//			System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
//		}
//
//	} catch (ParseException e) {
//		e.printStackTrace();
//		mv.addObject("errorMessage", "Invalid date format: " + todate);
//	} catch (Exception e) {
//		e.printStackTrace();
//		mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
//	}
//
//	// ✅ Common attributes
//	mv.setViewName("BRRS/M_P_L");
//	mv.addObject("displaymode", "Details");
//	mv.addObject("currentPage", currentPage);
//	System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
//	mv.addObject("reportsflag", "reportsflag");
//	mv.addObject("menu", reportId);
//
//	return mv;
//}

	public ModelAndView getM_P_LcurrentDtl(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String filter, String type, String version) {

		int pageSize = pageable != null ? pageable.getPageSize() : 10;
		int currentPage = pageable != null ? pageable.getPageNumber() : 0;
		int totalPages = 0;

		ModelAndView mv = new ModelAndView();
//	Session hs = sessionFactory.getCurrentSession();

		try {
			Date parsedDate = null;
			if (todate != null && !todate.isEmpty()) {
				parsedDate = dateformat.parse(todate);
			}

			String report_label = null;
			String report_addl_criteria_1 = null;

			// ✅ Split filter string into rowId & columnId
			if (filter != null && filter.contains(",")) {
				String[] parts = filter.split(",");
				if (parts.length >= 2) {
					report_label = parts[0];
					report_addl_criteria_1 = parts[1];
				}
			}

			if ("ARCHIVAL".equals(type) && version != null) {
				// 🔹 Archival branch
				List<M_P_L_Archival_Detail_Entity> T1Dt1;
				if (report_label != null && report_addl_criteria_1 != null) {
					T1Dt1 = GetArchivalDataByRowIdAndColumnId(report_label, report_addl_criteria_1, parsedDate,
							version);

				} else {
					T1Dt1 = getArchivalDetaildatabydateList(parsedDate, version);
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

			} else {
				// 🔹 Current branch
				List<M_P_L_Detail_Entity> T1Dt1;
				if (report_label != null && report_addl_criteria_1 != null) {
					T1Dt1 = GetDetailDataByRowIdAndColumnId(report_label, report_addl_criteria_1, parsedDate);
				} else {
					T1Dt1 = getDetaildatabydateList(parsedDate);

					mv.addObject("pagination", "YES");
				}

				mv.addObject("reportdetails", T1Dt1);
				mv.addObject("reportmaster12", T1Dt1);
				System.out.println("LISTCOUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Invalid date format: " + todate);
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errorMessage", "Unexpected error: " + e.getMessage());
		}

		// ✅ Common attributes
		mv.setViewName("BRRS/M_P_L");
		mv.addObject("displaymode", "Details");
		mv.addObject("currentPage", currentPage);
		System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
		mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
		mv.addObject("reportsflag", "reportsflag");
		mv.addObject("menu", reportId);

		return mv;
	}


	public byte[] getM_P_LDetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for M_P_L Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_P_LDetail");

//Common border style
			BorderStyle border = BorderStyle.THIN;

//Header style (left aligned)
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

//Right-aligned header style for ACCT BALANCE
			CellStyle rightAlignedHeaderStyle = workbook.createCellStyle();
			rightAlignedHeaderStyle.cloneStyleFrom(headerStyle);
			rightAlignedHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);

//Default data style (left aligned)
			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setAlignment(HorizontalAlignment.LEFT);
			dataStyle.setBorderTop(border);
			dataStyle.setBorderBottom(border);
			dataStyle.setBorderLeft(border);
			dataStyle.setBorderRight(border);

//ACCT BALANCE style (right aligned with thousand separator)
			CellStyle balanceStyle = workbook.createCellStyle();
			balanceStyle.setAlignment(HorizontalAlignment.RIGHT);
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

//Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA1", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

//Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_P_L_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_P_L_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_P_L — only header will be written.");
			}

//Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_P_L Excel", e);
			return new byte[0];
		}
	}

	public byte[] getM_P_LExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

//Convert string to Date
		Date reportDate = dateformat.parse(todate);

//ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_P_LARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
//RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<M_P_L_Archival_Summary_Entity> T1Master = getdatabydateListarchival(dateformat.parse(todate), version);

		}

//Default (LIVE) case
		List<M_P_L_Summary_Entity> dataList1 = getDataByDate(dateformat.parse(todate));

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
//This specific exception will be caught by the controller.
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}
		if (!Files.isReadable(templatePath)) {
//A specific exception for permission errors.
			throw new SecurityException(
					"Template file exists but is not readable (check permissions): " + templatePath.toAbsolutePath());
		}

//This try-with-resources block is perfect. It guarantees all resources are
//closed automatically.
		try (InputStream templateInputStream = Files.newInputStream(templatePath);
				Workbook workbook = WorkbookFactory.create(templateInputStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

//--- Style Definitions ---
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

//Create the font
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) 8); // size 8
			font.setFontName("Arial");

			CellStyle numberStyle = workbook.createCellStyle();
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
//--- End of Style Definitions ---

			int startRow = 8;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					M_P_L_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(2);
					if (record.getR09_NET_AMT() != null) {
						cell1.setCellValue(record.getR09_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(3);
					if (record.getR09_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR09_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					Cell cell3 = row.createCell(4);
					if (record.getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					cell1 = row.createCell(2);
					if (record.getR10_NET_AMT() != null) {
						cell1.setCellValue(record.getR10_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR10_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR10_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(12);
					cell1 = row.createCell(2);
					if (record.getR13_NET_AMT() != null) {
						cell1.setCellValue(record.getR13_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR13_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR13_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);
					cell1 = row.createCell(2);
					if (record.getR14_NET_AMT() != null) {
						cell1.setCellValue(record.getR14_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR14_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR14_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					cell1 = row.createCell(2);
					if (record.getR15_NET_AMT() != null) {
						cell1.setCellValue(record.getR15_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR15_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR15_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);
					cell1 = row.createCell(2);
					if (record.getR19_NET_AMT() != null) {
						cell1.setCellValue(record.getR19_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR19_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR19_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(19);
					cell1 = row.createCell(2);
					if (record.getR20_NET_AMT() != null) {
						cell1.setCellValue(record.getR20_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR20_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR20_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(20);

					cell2 = row.createCell(3);
					if (record.getR21_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR21_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);
					cell1 = row.createCell(2);
					if (record.getR22_NET_AMT() != null) {
						cell1.setCellValue(record.getR22_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR22_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR22_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
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

	public List<Object> getM_P_LArchival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_P_L_ARCHIVALTABLE_SUMMARY"
				+ "ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_P_L ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_P_LDetail");

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
			balanceStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			balanceStyle.setBorderTop(border);
			balanceStyle.setBorderBottom(border);
			balanceStyle.setBorderLeft(border);
			balanceStyle.setBorderRight(border);

// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE IN PULA", "REPORT LABLE",
					"REPORT ADDL CRITERIA", "REPORT_DATE" };

			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) { // ACCT BALANCE
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_P_L_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_P_L_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);

					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);

					row.createCell(4).setCellValue(item.getReport_label());
					row.createCell(5).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(6)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");

					// Apply data style for all other cells
					for (int j = 0; j < 7; j++) {
						if (j != 3) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for M_P_L — only header will be written.");
			}
// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating M_P_L Excel", e);
			return new byte[0];
		}
	}

	public byte[] getExcelM_P_LARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<M_P_L_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for ADISB1 report. Returning empty result.");
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
			// --- End of Style Definitions --
			int startRow = 8;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					M_P_L_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					Cell cell1 = row.createCell(2);
					if (record.getR09_NET_AMT() != null) {
						cell1.setCellValue(record.getR09_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(3);
					if (record.getR09_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR09_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					Cell cell3 = row.createCell(4);
					if (record.getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR09_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					cell1 = row.createCell(2);
					if (record.getR10_NET_AMT() != null) {
						cell1.setCellValue(record.getR10_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR10_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR10_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR10_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(12);
					cell1 = row.createCell(2);
					if (record.getR13_NET_AMT() != null) {
						cell1.setCellValue(record.getR13_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR13_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR13_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR13_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);
					cell1 = row.createCell(2);
					if (record.getR14_NET_AMT() != null) {
						cell1.setCellValue(record.getR14_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR14_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR14_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR14_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(14);
					cell1 = row.createCell(2);
					if (record.getR15_NET_AMT() != null) {
						cell1.setCellValue(record.getR15_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR15_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR15_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR15_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);
					cell1 = row.createCell(2);
					if (record.getR19_NET_AMT() != null) {
						cell1.setCellValue(record.getR19_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR19_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR19_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR19_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(19);
					cell1 = row.createCell(2);
					if (record.getR20_NET_AMT() != null) {
						cell1.setCellValue(record.getR20_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR20_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR20_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR20_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(20);

					cell2 = row.createCell(3);
					if (record.getR21_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR21_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR21_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					row = sheet.getRow(21);
					cell1 = row.createCell(2);
					if (record.getR22_NET_AMT() != null) {
						cell1.setCellValue(record.getR22_NET_AMT().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					cell2 = row.createCell(3);
					if (record.getR22_BAL_W_BOB_AND_OTHR_SUBS() != null) {
						cell2.setCellValue(record.getR22_BAL_W_BOB_AND_OTHR_SUBS().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					cell3 = row.createCell(4);
					if (record.getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS() != null) {
						cell3.setCellValue(record.getR22_BAL_AS_PER_STMT_OF_BOB_BRNCHS().doubleValue());
						cell3.setCellStyle(numberStyle);
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

	
	
	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {

	    ModelAndView mv = new ModelAndView("BRRS/M_P_L");

	    System.out.println("Came to view method");

	    if (acctNo != null) {

	        M_P_L_Detail_Entity entity = findByAcctnumber(acctNo);

	        if (entity != null && entity.getReport_date() != null) {

	            String formattedDate =
	                    new SimpleDateFormat("dd/MM/yyyy")
	                            .format(entity.getReport_date());

	            mv.addObject("asondate", formattedDate);
	        }

	        mv.addObject("Data", entity);

	    } else {

	        System.out.println(acctNo);
	    }

	    mv.addObject("displaymode", "edit");
	    mv.addObject("formmode",
	            formMode != null ? formMode : "edit");

	    return mv;
	}
	
	
	@Transactional
	public ResponseEntity<?> updateDetailEdit(HttpServletRequest request) {

	    try {

	        String acctNo = request.getParameter("acctNumber");
	        String acctName = request.getParameter("acctName");
	        String acctBalanceInpula = request.getParameter("acctBalanceInpula");
	        String reportDateStr = request.getParameter("reportDate");

	        logger.info("Received update for ACCT_NO: {}", acctNo);

	        M_P_L_Detail_Entity existing = findByAcctnumber(acctNo);

	        if (existing == null) {

	            logger.warn("No record found for ACCT_NO: {}", acctNo);

	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        // Update Account Name
	        if (acctName != null && !acctName.isEmpty()) {

	            if (existing.getAcct_name() == null ||
	                    !existing.getAcct_name().equals(acctName)) {

	                existing.setAcct_name(acctName);

	                isChanged = true;

	                logger.info("Account Name updated to {}", acctName);
	            }
	        }

	        // Update Account Balance
	        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

	            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

	            if (existing.getAcct_balance_in_pula() == null ||
	                    existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {

	                existing.setAcct_balance_in_pula(newBalance);

	                isChanged = true;

	                logger.info("Account Balance updated to {}", newBalance);
	            }
	        }

	        if (isChanged) {

	            String sql =
	                    "UPDATE BRRS_M_P_L_DETAILTABLE " +
	                    "SET ACCT_NAME = ?, " +
	                    "ACCT_BALANCE_IN_PULA = ? " +
	                    "WHERE ACCT_NUMBER = ?";

	            jdbcTemplate.update(
	                    sql,
	                    existing.getAcct_name(),
	                    existing.getAcct_balance_in_pula(),
	                    existing.getAcct_number()
	            );

	            logger.info("Record updated successfully for account {}", acctNo);

	            // Format date for procedure
	            String formattedDate = null;

	            if (reportDateStr != null && !reportDateStr.isEmpty()) {

	                formattedDate =
	                        new SimpleDateFormat("dd-MM-yyyy")
	                                .format(
	                                        new SimpleDateFormat("yyyy-MM-dd")
	                                                .parse(reportDateStr));

	            } else if (existing.getReport_date() != null) {

	                formattedDate =
	                        new SimpleDateFormat("dd-MM-yyyy")
	                                .format(existing.getReport_date());
	            }

	            final String procDate = formattedDate;

	            // Run summary procedure after commit
	            if (procDate != null) {

	                TransactionSynchronizationManager.registerSynchronization(
	                        new TransactionSynchronizationAdapter() {

	                            @Override
	                            public void afterCommit() {

	                                try {

	                                    logger.info(
	                                            "Transaction committed — calling BRRS_M_P_L_SUMMARY_PROCEDURE({})",
	                                            procDate);

	                                    jdbcTemplate.update(
	                                            "BEGIN BRRS_M_P_L_SUMMARY_PROCEDURE(?); END;",
	                                            procDate);

	                                    logger.info(
	                                            "Procedure executed successfully after commit.");

	                                } catch (Exception e) {

	                                    logger.error(
	                                            "Error executing procedure after commit",
	                                            e);
	                                }
	                            }
	                        });
	            }

	            return ResponseEntity.ok(
	                    "Record updated successfully!");

	        } else {

	            logger.info(
	                    "No changes detected for ACCT_NO: {}",
	                    acctNo);

	            return ResponseEntity.ok(
	                    "No changes were made.");
	        }

	    } catch (Exception e) {

	        logger.error(
	                "Error updating M_P_L record",
	                e);

	        return ResponseEntity.status(
	                HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: "
	                        + e.getMessage());
	    }
	}
}