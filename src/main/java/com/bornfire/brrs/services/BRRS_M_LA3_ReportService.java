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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
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
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Component
@Service
public class BRRS_M_LA3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_LA3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@Autowired
	UserProfileRep userProfileRep;

	@PersistenceContext
	private EntityManager entityManager;

	// Fetch data by report date
	public List<M_LA3_Summary_Entity1> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA3_SUMMARYTABLE1 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA3_RowMapper1());
	}

	// Fetch data by report date
	public List<M_LA3_Summary_Entity2> getDataByDate2(Date reportDate) {

		String sql = "SELECT * FROM BRRS_M_LA3_SUMMARYTABLE2 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new M_LA3_RowMapper2());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getM_LA3Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1"
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Object[]> getM_LA3Archival2() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2"
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<M_LA3_Archival_Summary_Entity1> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA3_RowMapper_Archival1());
	}

	public List<M_LA3_Archival_Summary_Entity2> getdatabydateListarchival2(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new M_LA3_RowMapper_Archival2());
	}

	// GET ALL WITH VERSION

	public List<M_LA3_Archival_Summary_Entity1> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA3_RowMapper_Archival1());
	}

	public List<M_LA3_Archival_Summary_Entity2> getdatabydateListWithVersion2() {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new M_LA3_RowMapper_Archival2());
	}

	// GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1" + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal findMaxVersion2(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2" + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// 1. BY DATE + LABEL + CRITERIA

	public List<M_LA3_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new M_LA3DetailRowMapper());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<M_LA3_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new M_LA3DetailRowMapper());
	}

	// 3. PAGINATION

	public List<M_LA3_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new M_LA3DetailRowMapper());
	}

	// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_M_LA3_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<M_LA3_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			String reportAddlCriteria2, String reportAddlCriteria3, Date reportdate) {

		String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_ADDL_CRITERIA_2 = ? AND REPORT_ADDL_CRITERIA_3 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql,
				new Object[] { reportLabel, reportAddlCriteria1, reportAddlCriteria2, reportAddlCriteria3, reportdate },
				new M_LA3DetailRowMapper());
	}
	// 6. BY ACCOUNT NUMBER

	public M_LA3_Detail_Entity findByAcctnumber(String acctNumber) {

		try {

			String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE " + "WHERE ACCT_NUMBER = ?";

			return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new M_LA3DetailRowMapper());

		} catch (EmptyResultDataAccessException e) {

			logger.warn("No record found for ACCT_NUMBER : {}", acctNumber);
			return null;
		}
	}
	
	public M_LA3_Detail_Entity findBySno(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA3_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA3DetailRowMapper());
	}

	public M_LA3_Detail_Entity findBySnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new M_LA3DetailRowMapper());
	}
	
	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}
	
	public String getishighestversion2(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	// 1. GET BY DATE + VERSION

	public List<M_LA3_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new M_LA3ArchivalDetailRowMapper());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<M_LA3_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String report_label,
			String report_addl_criteria_1, String report_addl_criteria_2, String report_addl_criteria_3,
			Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_M_LA3_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_ADDL_CRITERIA_2= ? "
				+ "AND REPORT_ADDL_CRITERIA_3 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate
				.query(sql,
						new Object[] { report_label, report_addl_criteria_1, report_addl_criteria_2,
								report_addl_criteria_3, reportdate, dataEntryVersion },
						new M_LA3ArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class M_LA3_RowMapper1 implements RowMapper<M_LA3_Summary_Entity1> {

		@Override
		public M_LA3_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Summary_Entity1 obj = new M_LA3_Summary_Entity1();

			// ====== R10 ======
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_no_of_ac(rs.getBigDecimal("r10_no_of_ac"));
			obj.setR10_approved_limit(rs.getBigDecimal("r10_approved_limit"));
			obj.setR10_amount_outstanding(rs.getBigDecimal("r10_amount_outstanding"));

			// ====== R11 ======
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_no_of_ac(rs.getBigDecimal("r11_no_of_ac"));
			obj.setR11_approved_limit(rs.getBigDecimal("r11_approved_limit"));
			obj.setR11_amount_outstanding(rs.getBigDecimal("r11_amount_outstanding"));

			// ====== R12 ======
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_no_of_ac(rs.getBigDecimal("r12_no_of_ac"));
			obj.setR12_approved_limit(rs.getBigDecimal("r12_approved_limit"));
			obj.setR12_amount_outstanding(rs.getBigDecimal("r12_amount_outstanding"));

			// ====== R13 ======
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_no_of_ac(rs.getBigDecimal("r13_no_of_ac"));
			obj.setR13_approved_limit(rs.getBigDecimal("r13_approved_limit"));
			obj.setR13_amount_outstanding(rs.getBigDecimal("r13_amount_outstanding"));

			// ====== R14 ======
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_no_of_ac(rs.getBigDecimal("r14_no_of_ac"));
			obj.setR14_approved_limit(rs.getBigDecimal("r14_approved_limit"));
			obj.setR14_amount_outstanding(rs.getBigDecimal("r14_amount_outstanding"));

			// ====== R15 ======
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_no_of_ac(rs.getBigDecimal("r15_no_of_ac"));
			obj.setR15_approved_limit(rs.getBigDecimal("r15_approved_limit"));
			obj.setR15_amount_outstanding(rs.getBigDecimal("r15_amount_outstanding"));

			// ====== R16 ======
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_no_of_ac(rs.getBigDecimal("r16_no_of_ac"));
			obj.setR16_approved_limit(rs.getBigDecimal("r16_approved_limit"));
			obj.setR16_amount_outstanding(rs.getBigDecimal("r16_amount_outstanding"));

			// ====== R21 ======
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_no_of_ac(rs.getBigDecimal("r21_no_of_ac"));
			obj.setR21_approved_limit(rs.getBigDecimal("r21_approved_limit"));
			obj.setR21_amount_outstanding(rs.getBigDecimal("r21_amount_outstanding"));

			// ====== R22 ======
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_no_of_ac(rs.getBigDecimal("r22_no_of_ac"));
			obj.setR22_approved_limit(rs.getBigDecimal("r22_approved_limit"));
			obj.setR22_amount_outstanding(rs.getBigDecimal("r22_amount_outstanding"));

			// ====== R23 ======
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_no_of_ac(rs.getBigDecimal("r23_no_of_ac"));
			obj.setR23_approved_limit(rs.getBigDecimal("r23_approved_limit"));
			obj.setR23_amount_outstanding(rs.getBigDecimal("r23_amount_outstanding"));

			// ====== R24 ======
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_no_of_ac(rs.getBigDecimal("r24_no_of_ac"));
			obj.setR24_approved_limit(rs.getBigDecimal("r24_approved_limit"));
			obj.setR24_amount_outstanding(rs.getBigDecimal("r24_amount_outstanding"));

			// ====== R25 ======
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_no_of_ac(rs.getBigDecimal("r25_no_of_ac"));
			obj.setR25_approved_limit(rs.getBigDecimal("r25_approved_limit"));
			obj.setR25_amount_outstanding(rs.getBigDecimal("r25_amount_outstanding"));

			// ====== R26 ======
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_no_of_ac(rs.getBigDecimal("r26_no_of_ac"));
			obj.setR26_approved_limit(rs.getBigDecimal("r26_approved_limit"));
			obj.setR26_amount_outstanding(rs.getBigDecimal("r26_amount_outstanding"));

			// ====== R27 ======
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_no_of_ac(rs.getBigDecimal("r27_no_of_ac"));
			obj.setR27_approved_limit(rs.getBigDecimal("r27_approved_limit"));
			obj.setR27_amount_outstanding(rs.getBigDecimal("r27_amount_outstanding"));

			// ====== R28 ======
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_no_of_ac(rs.getBigDecimal("r28_no_of_ac"));
			obj.setR28_approved_limit(rs.getBigDecimal("r28_approved_limit"));
			obj.setR28_amount_outstanding(rs.getBigDecimal("r28_amount_outstanding"));

			// ====== R29 ======
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_no_of_ac(rs.getBigDecimal("r29_no_of_ac"));
			obj.setR29_approved_limit(rs.getBigDecimal("r29_approved_limit"));
			obj.setR29_amount_outstanding(rs.getBigDecimal("r29_amount_outstanding"));

			// ====== R30 ======
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_no_of_ac(rs.getBigDecimal("r30_no_of_ac"));
			obj.setR30_approved_limit(rs.getBigDecimal("r30_approved_limit"));
			obj.setR30_amount_outstanding(rs.getBigDecimal("r30_amount_outstanding"));

			// ====== R31 ======
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_no_of_ac(rs.getBigDecimal("r31_no_of_ac"));
			obj.setR31_approved_limit(rs.getBigDecimal("r31_approved_limit"));
			obj.setR31_amount_outstanding(rs.getBigDecimal("r31_amount_outstanding"));

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

	public static class M_LA3_Summary_Entity1 {

		// ====== R10 ======
		private String r10_product;
		private BigDecimal r10_no_of_ac;
		private BigDecimal r10_approved_limit;
		private BigDecimal r10_amount_outstanding;

		// ====== R11 ======
		private String r11_product;
		private BigDecimal r11_no_of_ac;
		private BigDecimal r11_approved_limit;
		private BigDecimal r11_amount_outstanding;

		// ====== R12 ======
		private String r12_product;
		private BigDecimal r12_no_of_ac;
		private BigDecimal r12_approved_limit;
		private BigDecimal r12_amount_outstanding;

		// ====== R13 ======
		private String r13_product;
		private BigDecimal r13_no_of_ac;
		private BigDecimal r13_approved_limit;
		private BigDecimal r13_amount_outstanding;

		// ====== R14 ======
		private String r14_product;
		private BigDecimal r14_no_of_ac;
		private BigDecimal r14_approved_limit;
		private BigDecimal r14_amount_outstanding;

		// ====== R15 ======
		private String r15_product;
		private BigDecimal r15_no_of_ac;
		private BigDecimal r15_approved_limit;
		private BigDecimal r15_amount_outstanding;

		// ====== R16 ======
		private String r16_product;
		private BigDecimal r16_no_of_ac;
		private BigDecimal r16_approved_limit;
		private BigDecimal r16_amount_outstanding;

		// ====== R21 ======
		private String r21_product;
		private BigDecimal r21_no_of_ac;
		private BigDecimal r21_approved_limit;
		private BigDecimal r21_amount_outstanding;

		// ====== R22 ======
		private String r22_product;
		private BigDecimal r22_no_of_ac;
		private BigDecimal r22_approved_limit;
		private BigDecimal r22_amount_outstanding;

		// ====== R23 ======
		private String r23_product;
		private BigDecimal r23_no_of_ac;
		private BigDecimal r23_approved_limit;
		private BigDecimal r23_amount_outstanding;

		// ====== R24 ======
		private String r24_product;
		private BigDecimal r24_no_of_ac;
		private BigDecimal r24_approved_limit;
		private BigDecimal r24_amount_outstanding;

		// ====== R25 ======
		private String r25_product;
		private BigDecimal r25_no_of_ac;
		private BigDecimal r25_approved_limit;
		private BigDecimal r25_amount_outstanding;

		// ====== R26 ======
		private String r26_product;
		private BigDecimal r26_no_of_ac;
		private BigDecimal r26_approved_limit;
		private BigDecimal r26_amount_outstanding;

		// ====== R27 ======
		private String r27_product;
		private BigDecimal r27_no_of_ac;
		private BigDecimal r27_approved_limit;
		private BigDecimal r27_amount_outstanding;

		// ====== R28 ======
		private String r28_product;
		private BigDecimal r28_no_of_ac;
		private BigDecimal r28_approved_limit;
		private BigDecimal r28_amount_outstanding;

		// ====== R29 ======
		private String r29_product;
		private BigDecimal r29_no_of_ac;
		private BigDecimal r29_approved_limit;
		private BigDecimal r29_amount_outstanding;

		// ====== R30 ======
		private String r30_product;
		private BigDecimal r30_no_of_ac;
		private BigDecimal r30_approved_limit;
		private BigDecimal r30_amount_outstanding;

		// ====== R31 ======
		private String r31_product;
		private BigDecimal r31_no_of_ac;
		private BigDecimal r31_approved_limit;
		private BigDecimal r31_amount_outstanding;

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

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_no_of_ac() {
			return r10_no_of_ac;
		}

		public void setR10_no_of_ac(BigDecimal r10_no_of_ac) {
			this.r10_no_of_ac = r10_no_of_ac;
		}

		public BigDecimal getR10_approved_limit() {
			return r10_approved_limit;
		}

		public void setR10_approved_limit(BigDecimal r10_approved_limit) {
			this.r10_approved_limit = r10_approved_limit;
		}

		public BigDecimal getR10_amount_outstanding() {
			return r10_amount_outstanding;
		}

		public void setR10_amount_outstanding(BigDecimal r10_amount_outstanding) {
			this.r10_amount_outstanding = r10_amount_outstanding;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_no_of_ac() {
			return r11_no_of_ac;
		}

		public void setR11_no_of_ac(BigDecimal r11_no_of_ac) {
			this.r11_no_of_ac = r11_no_of_ac;
		}

		public BigDecimal getR11_approved_limit() {
			return r11_approved_limit;
		}

		public void setR11_approved_limit(BigDecimal r11_approved_limit) {
			this.r11_approved_limit = r11_approved_limit;
		}

		public BigDecimal getR11_amount_outstanding() {
			return r11_amount_outstanding;
		}

		public void setR11_amount_outstanding(BigDecimal r11_amount_outstanding) {
			this.r11_amount_outstanding = r11_amount_outstanding;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_no_of_ac() {
			return r12_no_of_ac;
		}

		public void setR12_no_of_ac(BigDecimal r12_no_of_ac) {
			this.r12_no_of_ac = r12_no_of_ac;
		}

		public BigDecimal getR12_approved_limit() {
			return r12_approved_limit;
		}

		public void setR12_approved_limit(BigDecimal r12_approved_limit) {
			this.r12_approved_limit = r12_approved_limit;
		}

		public BigDecimal getR12_amount_outstanding() {
			return r12_amount_outstanding;
		}

		public void setR12_amount_outstanding(BigDecimal r12_amount_outstanding) {
			this.r12_amount_outstanding = r12_amount_outstanding;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_no_of_ac() {
			return r13_no_of_ac;
		}

		public void setR13_no_of_ac(BigDecimal r13_no_of_ac) {
			this.r13_no_of_ac = r13_no_of_ac;
		}

		public BigDecimal getR13_approved_limit() {
			return r13_approved_limit;
		}

		public void setR13_approved_limit(BigDecimal r13_approved_limit) {
			this.r13_approved_limit = r13_approved_limit;
		}

		public BigDecimal getR13_amount_outstanding() {
			return r13_amount_outstanding;
		}

		public void setR13_amount_outstanding(BigDecimal r13_amount_outstanding) {
			this.r13_amount_outstanding = r13_amount_outstanding;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_no_of_ac() {
			return r14_no_of_ac;
		}

		public void setR14_no_of_ac(BigDecimal r14_no_of_ac) {
			this.r14_no_of_ac = r14_no_of_ac;
		}

		public BigDecimal getR14_approved_limit() {
			return r14_approved_limit;
		}

		public void setR14_approved_limit(BigDecimal r14_approved_limit) {
			this.r14_approved_limit = r14_approved_limit;
		}

		public BigDecimal getR14_amount_outstanding() {
			return r14_amount_outstanding;
		}

		public void setR14_amount_outstanding(BigDecimal r14_amount_outstanding) {
			this.r14_amount_outstanding = r14_amount_outstanding;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_no_of_ac() {
			return r15_no_of_ac;
		}

		public void setR15_no_of_ac(BigDecimal r15_no_of_ac) {
			this.r15_no_of_ac = r15_no_of_ac;
		}

		public BigDecimal getR15_approved_limit() {
			return r15_approved_limit;
		}

		public void setR15_approved_limit(BigDecimal r15_approved_limit) {
			this.r15_approved_limit = r15_approved_limit;
		}

		public BigDecimal getR15_amount_outstanding() {
			return r15_amount_outstanding;
		}

		public void setR15_amount_outstanding(BigDecimal r15_amount_outstanding) {
			this.r15_amount_outstanding = r15_amount_outstanding;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_no_of_ac() {
			return r16_no_of_ac;
		}

		public void setR16_no_of_ac(BigDecimal r16_no_of_ac) {
			this.r16_no_of_ac = r16_no_of_ac;
		}

		public BigDecimal getR16_approved_limit() {
			return r16_approved_limit;
		}

		public void setR16_approved_limit(BigDecimal r16_approved_limit) {
			this.r16_approved_limit = r16_approved_limit;
		}

		public BigDecimal getR16_amount_outstanding() {
			return r16_amount_outstanding;
		}

		public void setR16_amount_outstanding(BigDecimal r16_amount_outstanding) {
			this.r16_amount_outstanding = r16_amount_outstanding;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_no_of_ac() {
			return r21_no_of_ac;
		}

		public void setR21_no_of_ac(BigDecimal r21_no_of_ac) {
			this.r21_no_of_ac = r21_no_of_ac;
		}

		public BigDecimal getR21_approved_limit() {
			return r21_approved_limit;
		}

		public void setR21_approved_limit(BigDecimal r21_approved_limit) {
			this.r21_approved_limit = r21_approved_limit;
		}

		public BigDecimal getR21_amount_outstanding() {
			return r21_amount_outstanding;
		}

		public void setR21_amount_outstanding(BigDecimal r21_amount_outstanding) {
			this.r21_amount_outstanding = r21_amount_outstanding;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_no_of_ac() {
			return r22_no_of_ac;
		}

		public void setR22_no_of_ac(BigDecimal r22_no_of_ac) {
			this.r22_no_of_ac = r22_no_of_ac;
		}

		public BigDecimal getR22_approved_limit() {
			return r22_approved_limit;
		}

		public void setR22_approved_limit(BigDecimal r22_approved_limit) {
			this.r22_approved_limit = r22_approved_limit;
		}

		public BigDecimal getR22_amount_outstanding() {
			return r22_amount_outstanding;
		}

		public void setR22_amount_outstanding(BigDecimal r22_amount_outstanding) {
			this.r22_amount_outstanding = r22_amount_outstanding;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_no_of_ac() {
			return r23_no_of_ac;
		}

		public void setR23_no_of_ac(BigDecimal r23_no_of_ac) {
			this.r23_no_of_ac = r23_no_of_ac;
		}

		public BigDecimal getR23_approved_limit() {
			return r23_approved_limit;
		}

		public void setR23_approved_limit(BigDecimal r23_approved_limit) {
			this.r23_approved_limit = r23_approved_limit;
		}

		public BigDecimal getR23_amount_outstanding() {
			return r23_amount_outstanding;
		}

		public void setR23_amount_outstanding(BigDecimal r23_amount_outstanding) {
			this.r23_amount_outstanding = r23_amount_outstanding;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_no_of_ac() {
			return r24_no_of_ac;
		}

		public void setR24_no_of_ac(BigDecimal r24_no_of_ac) {
			this.r24_no_of_ac = r24_no_of_ac;
		}

		public BigDecimal getR24_approved_limit() {
			return r24_approved_limit;
		}

		public void setR24_approved_limit(BigDecimal r24_approved_limit) {
			this.r24_approved_limit = r24_approved_limit;
		}

		public BigDecimal getR24_amount_outstanding() {
			return r24_amount_outstanding;
		}

		public void setR24_amount_outstanding(BigDecimal r24_amount_outstanding) {
			this.r24_amount_outstanding = r24_amount_outstanding;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_no_of_ac() {
			return r25_no_of_ac;
		}

		public void setR25_no_of_ac(BigDecimal r25_no_of_ac) {
			this.r25_no_of_ac = r25_no_of_ac;
		}

		public BigDecimal getR25_approved_limit() {
			return r25_approved_limit;
		}

		public void setR25_approved_limit(BigDecimal r25_approved_limit) {
			this.r25_approved_limit = r25_approved_limit;
		}

		public BigDecimal getR25_amount_outstanding() {
			return r25_amount_outstanding;
		}

		public void setR25_amount_outstanding(BigDecimal r25_amount_outstanding) {
			this.r25_amount_outstanding = r25_amount_outstanding;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_no_of_ac() {
			return r26_no_of_ac;
		}

		public void setR26_no_of_ac(BigDecimal r26_no_of_ac) {
			this.r26_no_of_ac = r26_no_of_ac;
		}

		public BigDecimal getR26_approved_limit() {
			return r26_approved_limit;
		}

		public void setR26_approved_limit(BigDecimal r26_approved_limit) {
			this.r26_approved_limit = r26_approved_limit;
		}

		public BigDecimal getR26_amount_outstanding() {
			return r26_amount_outstanding;
		}

		public void setR26_amount_outstanding(BigDecimal r26_amount_outstanding) {
			this.r26_amount_outstanding = r26_amount_outstanding;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_no_of_ac() {
			return r27_no_of_ac;
		}

		public void setR27_no_of_ac(BigDecimal r27_no_of_ac) {
			this.r27_no_of_ac = r27_no_of_ac;
		}

		public BigDecimal getR27_approved_limit() {
			return r27_approved_limit;
		}

		public void setR27_approved_limit(BigDecimal r27_approved_limit) {
			this.r27_approved_limit = r27_approved_limit;
		}

		public BigDecimal getR27_amount_outstanding() {
			return r27_amount_outstanding;
		}

		public void setR27_amount_outstanding(BigDecimal r27_amount_outstanding) {
			this.r27_amount_outstanding = r27_amount_outstanding;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_no_of_ac() {
			return r28_no_of_ac;
		}

		public void setR28_no_of_ac(BigDecimal r28_no_of_ac) {
			this.r28_no_of_ac = r28_no_of_ac;
		}

		public BigDecimal getR28_approved_limit() {
			return r28_approved_limit;
		}

		public void setR28_approved_limit(BigDecimal r28_approved_limit) {
			this.r28_approved_limit = r28_approved_limit;
		}

		public BigDecimal getR28_amount_outstanding() {
			return r28_amount_outstanding;
		}

		public void setR28_amount_outstanding(BigDecimal r28_amount_outstanding) {
			this.r28_amount_outstanding = r28_amount_outstanding;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_no_of_ac() {
			return r29_no_of_ac;
		}

		public void setR29_no_of_ac(BigDecimal r29_no_of_ac) {
			this.r29_no_of_ac = r29_no_of_ac;
		}

		public BigDecimal getR29_approved_limit() {
			return r29_approved_limit;
		}

		public void setR29_approved_limit(BigDecimal r29_approved_limit) {
			this.r29_approved_limit = r29_approved_limit;
		}

		public BigDecimal getR29_amount_outstanding() {
			return r29_amount_outstanding;
		}

		public void setR29_amount_outstanding(BigDecimal r29_amount_outstanding) {
			this.r29_amount_outstanding = r29_amount_outstanding;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_no_of_ac() {
			return r30_no_of_ac;
		}

		public void setR30_no_of_ac(BigDecimal r30_no_of_ac) {
			this.r30_no_of_ac = r30_no_of_ac;
		}

		public BigDecimal getR30_approved_limit() {
			return r30_approved_limit;
		}

		public void setR30_approved_limit(BigDecimal r30_approved_limit) {
			this.r30_approved_limit = r30_approved_limit;
		}

		public BigDecimal getR30_amount_outstanding() {
			return r30_amount_outstanding;
		}

		public void setR30_amount_outstanding(BigDecimal r30_amount_outstanding) {
			this.r30_amount_outstanding = r30_amount_outstanding;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_no_of_ac() {
			return r31_no_of_ac;
		}

		public void setR31_no_of_ac(BigDecimal r31_no_of_ac) {
			this.r31_no_of_ac = r31_no_of_ac;
		}

		public BigDecimal getR31_approved_limit() {
			return r31_approved_limit;
		}

		public void setR31_approved_limit(BigDecimal r31_approved_limit) {
			this.r31_approved_limit = r31_approved_limit;
		}

		public BigDecimal getR31_amount_outstanding() {
			return r31_amount_outstanding;
		}

		public void setR31_amount_outstanding(BigDecimal r31_amount_outstanding) {
			this.r31_amount_outstanding = r31_amount_outstanding;
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

	// ROW MAPPER

	class M_LA3_RowMapper2 implements RowMapper<M_LA3_Summary_Entity2> {

		@Override
		public M_LA3_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Summary_Entity2 obj = new M_LA3_Summary_Entity2();

			// ====== R36 ======
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_NO_OF_AC(rs.getBigDecimal("R36_NO_OF_AC"));
			obj.setR36_CREDIT_LIMIT(rs.getBigDecimal("R36_CREDIT_LIMIT"));
			obj.setR36_AMOUNT_OUTSTANDING(rs.getBigDecimal("R36_AMOUNT_OUTSTANDING"));

			// ====== R37 ======
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_NO_OF_AC(rs.getBigDecimal("R37_NO_OF_AC"));
			obj.setR37_CREDIT_LIMIT(rs.getBigDecimal("R37_CREDIT_LIMIT"));
			obj.setR37_AMOUNT_OUTSTANDING(rs.getBigDecimal("R37_AMOUNT_OUTSTANDING"));

			// ====== R38 ======
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_NO_OF_AC(rs.getBigDecimal("R38_NO_OF_AC"));
			obj.setR38_CREDIT_LIMIT(rs.getBigDecimal("R38_CREDIT_LIMIT"));
			obj.setR38_AMOUNT_OUTSTANDING(rs.getBigDecimal("R38_AMOUNT_OUTSTANDING"));

			// ====== R39 ======
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_NO_OF_AC(rs.getBigDecimal("R39_NO_OF_AC"));
			obj.setR39_CREDIT_LIMIT(rs.getBigDecimal("R39_CREDIT_LIMIT"));
			obj.setR39_AMOUNT_OUTSTANDING(rs.getBigDecimal("R39_AMOUNT_OUTSTANDING"));

			// ====== R40 ======
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_NO_OF_AC(rs.getBigDecimal("R40_NO_OF_AC"));
			obj.setR40_CREDIT_LIMIT(rs.getBigDecimal("R40_CREDIT_LIMIT"));
			obj.setR40_AMOUNT_OUTSTANDING(rs.getBigDecimal("R40_AMOUNT_OUTSTANDING"));

			// ====== R41 ======
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_NO_OF_AC(rs.getBigDecimal("R41_NO_OF_AC"));
			obj.setR41_CREDIT_LIMIT(rs.getBigDecimal("R41_CREDIT_LIMIT"));
			obj.setR41_AMOUNT_OUTSTANDING(rs.getBigDecimal("R41_AMOUNT_OUTSTANDING"));

			// ====== R42 ======
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_NO_OF_AC(rs.getBigDecimal("R42_NO_OF_AC"));
			obj.setR42_CREDIT_LIMIT(rs.getBigDecimal("R42_CREDIT_LIMIT"));
			obj.setR42_AMOUNT_OUTSTANDING(rs.getBigDecimal("R42_AMOUNT_OUTSTANDING"));

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

	public static class M_LA3_Summary_Entity2 {

		// ====== R36 ======
		private String R36_PRODUCT;
		private BigDecimal R36_NO_OF_AC;
		private BigDecimal R36_CREDIT_LIMIT;
		private BigDecimal R36_AMOUNT_OUTSTANDING;

		// ====== R37 ======
		private String R37_PRODUCT;
		private BigDecimal R37_NO_OF_AC;
		private BigDecimal R37_CREDIT_LIMIT;
		private BigDecimal R37_AMOUNT_OUTSTANDING;

		// ====== R38 ======
		private String R38_PRODUCT;
		private BigDecimal R38_NO_OF_AC;
		private BigDecimal R38_CREDIT_LIMIT;
		private BigDecimal R38_AMOUNT_OUTSTANDING;

		// ====== R39 ======
		private String R39_PRODUCT;
		private BigDecimal R39_NO_OF_AC;
		private BigDecimal R39_CREDIT_LIMIT;
		private BigDecimal R39_AMOUNT_OUTSTANDING;

		// ====== R40 ======
		private String R40_PRODUCT;
		private BigDecimal R40_NO_OF_AC;
		private BigDecimal R40_CREDIT_LIMIT;
		private BigDecimal R40_AMOUNT_OUTSTANDING;

		// ====== R41 ======
		private String R41_PRODUCT;
		private BigDecimal R41_NO_OF_AC;
		private BigDecimal R41_CREDIT_LIMIT;
		private BigDecimal R41_AMOUNT_OUTSTANDING;

		// ====== R42 ======
		private String R42_PRODUCT;
		private BigDecimal R42_NO_OF_AC;
		private BigDecimal R42_CREDIT_LIMIT;
		private BigDecimal R42_AMOUNT_OUTSTANDING;

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

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_NO_OF_AC() {
			return R36_NO_OF_AC;
		}

		public void setR36_NO_OF_AC(BigDecimal r36_NO_OF_AC) {
			R36_NO_OF_AC = r36_NO_OF_AC;
		}

		public BigDecimal getR36_CREDIT_LIMIT() {
			return R36_CREDIT_LIMIT;
		}

		public void setR36_CREDIT_LIMIT(BigDecimal r36_CREDIT_LIMIT) {
			R36_CREDIT_LIMIT = r36_CREDIT_LIMIT;
		}

		public BigDecimal getR36_AMOUNT_OUTSTANDING() {
			return R36_AMOUNT_OUTSTANDING;
		}

		public void setR36_AMOUNT_OUTSTANDING(BigDecimal r36_AMOUNT_OUTSTANDING) {
			R36_AMOUNT_OUTSTANDING = r36_AMOUNT_OUTSTANDING;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_NO_OF_AC() {
			return R37_NO_OF_AC;
		}

		public void setR37_NO_OF_AC(BigDecimal r37_NO_OF_AC) {
			R37_NO_OF_AC = r37_NO_OF_AC;
		}

		public BigDecimal getR37_CREDIT_LIMIT() {
			return R37_CREDIT_LIMIT;
		}

		public void setR37_CREDIT_LIMIT(BigDecimal r37_CREDIT_LIMIT) {
			R37_CREDIT_LIMIT = r37_CREDIT_LIMIT;
		}

		public BigDecimal getR37_AMOUNT_OUTSTANDING() {
			return R37_AMOUNT_OUTSTANDING;
		}

		public void setR37_AMOUNT_OUTSTANDING(BigDecimal r37_AMOUNT_OUTSTANDING) {
			R37_AMOUNT_OUTSTANDING = r37_AMOUNT_OUTSTANDING;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_NO_OF_AC() {
			return R38_NO_OF_AC;
		}

		public void setR38_NO_OF_AC(BigDecimal r38_NO_OF_AC) {
			R38_NO_OF_AC = r38_NO_OF_AC;
		}

		public BigDecimal getR38_CREDIT_LIMIT() {
			return R38_CREDIT_LIMIT;
		}

		public void setR38_CREDIT_LIMIT(BigDecimal r38_CREDIT_LIMIT) {
			R38_CREDIT_LIMIT = r38_CREDIT_LIMIT;
		}

		public BigDecimal getR38_AMOUNT_OUTSTANDING() {
			return R38_AMOUNT_OUTSTANDING;
		}

		public void setR38_AMOUNT_OUTSTANDING(BigDecimal r38_AMOUNT_OUTSTANDING) {
			R38_AMOUNT_OUTSTANDING = r38_AMOUNT_OUTSTANDING;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_NO_OF_AC() {
			return R39_NO_OF_AC;
		}

		public void setR39_NO_OF_AC(BigDecimal r39_NO_OF_AC) {
			R39_NO_OF_AC = r39_NO_OF_AC;
		}

		public BigDecimal getR39_CREDIT_LIMIT() {
			return R39_CREDIT_LIMIT;
		}

		public void setR39_CREDIT_LIMIT(BigDecimal r39_CREDIT_LIMIT) {
			R39_CREDIT_LIMIT = r39_CREDIT_LIMIT;
		}

		public BigDecimal getR39_AMOUNT_OUTSTANDING() {
			return R39_AMOUNT_OUTSTANDING;
		}

		public void setR39_AMOUNT_OUTSTANDING(BigDecimal r39_AMOUNT_OUTSTANDING) {
			R39_AMOUNT_OUTSTANDING = r39_AMOUNT_OUTSTANDING;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_NO_OF_AC() {
			return R40_NO_OF_AC;
		}

		public void setR40_NO_OF_AC(BigDecimal r40_NO_OF_AC) {
			R40_NO_OF_AC = r40_NO_OF_AC;
		}

		public BigDecimal getR40_CREDIT_LIMIT() {
			return R40_CREDIT_LIMIT;
		}

		public void setR40_CREDIT_LIMIT(BigDecimal r40_CREDIT_LIMIT) {
			R40_CREDIT_LIMIT = r40_CREDIT_LIMIT;
		}

		public BigDecimal getR40_AMOUNT_OUTSTANDING() {
			return R40_AMOUNT_OUTSTANDING;
		}

		public void setR40_AMOUNT_OUTSTANDING(BigDecimal r40_AMOUNT_OUTSTANDING) {
			R40_AMOUNT_OUTSTANDING = r40_AMOUNT_OUTSTANDING;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_NO_OF_AC() {
			return R41_NO_OF_AC;
		}

		public void setR41_NO_OF_AC(BigDecimal r41_NO_OF_AC) {
			R41_NO_OF_AC = r41_NO_OF_AC;
		}

		public BigDecimal getR41_CREDIT_LIMIT() {
			return R41_CREDIT_LIMIT;
		}

		public void setR41_CREDIT_LIMIT(BigDecimal r41_CREDIT_LIMIT) {
			R41_CREDIT_LIMIT = r41_CREDIT_LIMIT;
		}

		public BigDecimal getR41_AMOUNT_OUTSTANDING() {
			return R41_AMOUNT_OUTSTANDING;
		}

		public void setR41_AMOUNT_OUTSTANDING(BigDecimal r41_AMOUNT_OUTSTANDING) {
			R41_AMOUNT_OUTSTANDING = r41_AMOUNT_OUTSTANDING;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_NO_OF_AC() {
			return R42_NO_OF_AC;
		}

		public void setR42_NO_OF_AC(BigDecimal r42_NO_OF_AC) {
			R42_NO_OF_AC = r42_NO_OF_AC;
		}

		public BigDecimal getR42_CREDIT_LIMIT() {
			return R42_CREDIT_LIMIT;
		}

		public void setR42_CREDIT_LIMIT(BigDecimal r42_CREDIT_LIMIT) {
			R42_CREDIT_LIMIT = r42_CREDIT_LIMIT;
		}

		public BigDecimal getR42_AMOUNT_OUTSTANDING() {
			return R42_AMOUNT_OUTSTANDING;
		}

		public void setR42_AMOUNT_OUTSTANDING(BigDecimal r42_AMOUNT_OUTSTANDING) {
			R42_AMOUNT_OUTSTANDING = r42_AMOUNT_OUTSTANDING;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
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

	// ARCHIVAL ROW MAPPER

	class M_LA3_RowMapper_Archival1 implements RowMapper<M_LA3_Archival_Summary_Entity1> {

		@Override
		public M_LA3_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Archival_Summary_Entity1 obj = new M_LA3_Archival_Summary_Entity1();

			// ====== R10 ======
			obj.setR10_product(rs.getString("r10_product"));
			obj.setR10_no_of_ac(rs.getBigDecimal("r10_no_of_ac"));
			obj.setR10_approved_limit(rs.getBigDecimal("r10_approved_limit"));
			obj.setR10_amount_outstanding(rs.getBigDecimal("r10_amount_outstanding"));

			// ====== R11 ======
			obj.setR11_product(rs.getString("r11_product"));
			obj.setR11_no_of_ac(rs.getBigDecimal("r11_no_of_ac"));
			obj.setR11_approved_limit(rs.getBigDecimal("r11_approved_limit"));
			obj.setR11_amount_outstanding(rs.getBigDecimal("r11_amount_outstanding"));

			// ====== R12 ======
			obj.setR12_product(rs.getString("r12_product"));
			obj.setR12_no_of_ac(rs.getBigDecimal("r12_no_of_ac"));
			obj.setR12_approved_limit(rs.getBigDecimal("r12_approved_limit"));
			obj.setR12_amount_outstanding(rs.getBigDecimal("r12_amount_outstanding"));

			// ====== R13 ======
			obj.setR13_product(rs.getString("r13_product"));
			obj.setR13_no_of_ac(rs.getBigDecimal("r13_no_of_ac"));
			obj.setR13_approved_limit(rs.getBigDecimal("r13_approved_limit"));
			obj.setR13_amount_outstanding(rs.getBigDecimal("r13_amount_outstanding"));

			// ====== R14 ======
			obj.setR14_product(rs.getString("r14_product"));
			obj.setR14_no_of_ac(rs.getBigDecimal("r14_no_of_ac"));
			obj.setR14_approved_limit(rs.getBigDecimal("r14_approved_limit"));
			obj.setR14_amount_outstanding(rs.getBigDecimal("r14_amount_outstanding"));

			// ====== R15 ======
			obj.setR15_product(rs.getString("r15_product"));
			obj.setR15_no_of_ac(rs.getBigDecimal("r15_no_of_ac"));
			obj.setR15_approved_limit(rs.getBigDecimal("r15_approved_limit"));
			obj.setR15_amount_outstanding(rs.getBigDecimal("r15_amount_outstanding"));

			// ====== R16 ======
			obj.setR16_product(rs.getString("r16_product"));
			obj.setR16_no_of_ac(rs.getBigDecimal("r16_no_of_ac"));
			obj.setR16_approved_limit(rs.getBigDecimal("r16_approved_limit"));
			obj.setR16_amount_outstanding(rs.getBigDecimal("r16_amount_outstanding"));

			// ====== R21 ======
			obj.setR21_product(rs.getString("r21_product"));
			obj.setR21_no_of_ac(rs.getBigDecimal("r21_no_of_ac"));
			obj.setR21_approved_limit(rs.getBigDecimal("r21_approved_limit"));
			obj.setR21_amount_outstanding(rs.getBigDecimal("r21_amount_outstanding"));

			// ====== R22 ======
			obj.setR22_product(rs.getString("r22_product"));
			obj.setR22_no_of_ac(rs.getBigDecimal("r22_no_of_ac"));
			obj.setR22_approved_limit(rs.getBigDecimal("r22_approved_limit"));
			obj.setR22_amount_outstanding(rs.getBigDecimal("r22_amount_outstanding"));

			// ====== R23 ======
			obj.setR23_product(rs.getString("r23_product"));
			obj.setR23_no_of_ac(rs.getBigDecimal("r23_no_of_ac"));
			obj.setR23_approved_limit(rs.getBigDecimal("r23_approved_limit"));
			obj.setR23_amount_outstanding(rs.getBigDecimal("r23_amount_outstanding"));

			// ====== R24 ======
			obj.setR24_product(rs.getString("r24_product"));
			obj.setR24_no_of_ac(rs.getBigDecimal("r24_no_of_ac"));
			obj.setR24_approved_limit(rs.getBigDecimal("r24_approved_limit"));
			obj.setR24_amount_outstanding(rs.getBigDecimal("r24_amount_outstanding"));

			// ====== R25 ======
			obj.setR25_product(rs.getString("r25_product"));
			obj.setR25_no_of_ac(rs.getBigDecimal("r25_no_of_ac"));
			obj.setR25_approved_limit(rs.getBigDecimal("r25_approved_limit"));
			obj.setR25_amount_outstanding(rs.getBigDecimal("r25_amount_outstanding"));

			// ====== R26 ======
			obj.setR26_product(rs.getString("r26_product"));
			obj.setR26_no_of_ac(rs.getBigDecimal("r26_no_of_ac"));
			obj.setR26_approved_limit(rs.getBigDecimal("r26_approved_limit"));
			obj.setR26_amount_outstanding(rs.getBigDecimal("r26_amount_outstanding"));

			// ====== R27 ======
			obj.setR27_product(rs.getString("r27_product"));
			obj.setR27_no_of_ac(rs.getBigDecimal("r27_no_of_ac"));
			obj.setR27_approved_limit(rs.getBigDecimal("r27_approved_limit"));
			obj.setR27_amount_outstanding(rs.getBigDecimal("r27_amount_outstanding"));

			// ====== R28 ======
			obj.setR28_product(rs.getString("r28_product"));
			obj.setR28_no_of_ac(rs.getBigDecimal("r28_no_of_ac"));
			obj.setR28_approved_limit(rs.getBigDecimal("r28_approved_limit"));
			obj.setR28_amount_outstanding(rs.getBigDecimal("r28_amount_outstanding"));

			// ====== R29 ======
			obj.setR29_product(rs.getString("r29_product"));
			obj.setR29_no_of_ac(rs.getBigDecimal("r29_no_of_ac"));
			obj.setR29_approved_limit(rs.getBigDecimal("r29_approved_limit"));
			obj.setR29_amount_outstanding(rs.getBigDecimal("r29_amount_outstanding"));

			// ====== R30 ======
			obj.setR30_product(rs.getString("r30_product"));
			obj.setR30_no_of_ac(rs.getBigDecimal("r30_no_of_ac"));
			obj.setR30_approved_limit(rs.getBigDecimal("r30_approved_limit"));
			obj.setR30_amount_outstanding(rs.getBigDecimal("r30_amount_outstanding"));

			// ====== R31 ======
			obj.setR31_product(rs.getString("r31_product"));
			obj.setR31_no_of_ac(rs.getBigDecimal("r31_no_of_ac"));
			obj.setR31_approved_limit(rs.getBigDecimal("r31_approved_limit"));
			obj.setR31_amount_outstanding(rs.getBigDecimal("r31_amount_outstanding"));

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

	public static class M_LA3_Archival_Summary_Entity1 {

		// ====== R10 ======
		private String r10_product;
		private BigDecimal r10_no_of_ac;
		private BigDecimal r10_approved_limit;
		private BigDecimal r10_amount_outstanding;

		// ====== R11 ======
		private String r11_product;
		private BigDecimal r11_no_of_ac;
		private BigDecimal r11_approved_limit;
		private BigDecimal r11_amount_outstanding;

		// ====== R12 ======
		private String r12_product;
		private BigDecimal r12_no_of_ac;
		private BigDecimal r12_approved_limit;
		private BigDecimal r12_amount_outstanding;

		// ====== R13 ======
		private String r13_product;
		private BigDecimal r13_no_of_ac;
		private BigDecimal r13_approved_limit;
		private BigDecimal r13_amount_outstanding;

		// ====== R14 ======
		private String r14_product;
		private BigDecimal r14_no_of_ac;
		private BigDecimal r14_approved_limit;
		private BigDecimal r14_amount_outstanding;

		// ====== R15 ======
		private String r15_product;
		private BigDecimal r15_no_of_ac;
		private BigDecimal r15_approved_limit;
		private BigDecimal r15_amount_outstanding;

		// ====== R16 ======
		private String r16_product;
		private BigDecimal r16_no_of_ac;
		private BigDecimal r16_approved_limit;
		private BigDecimal r16_amount_outstanding;

		// ====== R21 ======
		private String r21_product;
		private BigDecimal r21_no_of_ac;
		private BigDecimal r21_approved_limit;
		private BigDecimal r21_amount_outstanding;

		// ====== R22 ======
		private String r22_product;
		private BigDecimal r22_no_of_ac;
		private BigDecimal r22_approved_limit;
		private BigDecimal r22_amount_outstanding;

		// ====== R23 ======
		private String r23_product;
		private BigDecimal r23_no_of_ac;
		private BigDecimal r23_approved_limit;
		private BigDecimal r23_amount_outstanding;

		// ====== R24 ======
		private String r24_product;
		private BigDecimal r24_no_of_ac;
		private BigDecimal r24_approved_limit;
		private BigDecimal r24_amount_outstanding;

		// ====== R25 ======
		private String r25_product;
		private BigDecimal r25_no_of_ac;
		private BigDecimal r25_approved_limit;
		private BigDecimal r25_amount_outstanding;

		// ====== R26 ======
		private String r26_product;
		private BigDecimal r26_no_of_ac;
		private BigDecimal r26_approved_limit;
		private BigDecimal r26_amount_outstanding;

		// ====== R27 ======
		private String r27_product;
		private BigDecimal r27_no_of_ac;
		private BigDecimal r27_approved_limit;
		private BigDecimal r27_amount_outstanding;

		// ====== R28 ======
		private String r28_product;
		private BigDecimal r28_no_of_ac;
		private BigDecimal r28_approved_limit;
		private BigDecimal r28_amount_outstanding;

		// ====== R29 ======
		private String r29_product;
		private BigDecimal r29_no_of_ac;
		private BigDecimal r29_approved_limit;
		private BigDecimal r29_amount_outstanding;

		// ====== R30 ======
		private String r30_product;
		private BigDecimal r30_no_of_ac;
		private BigDecimal r30_approved_limit;
		private BigDecimal r30_amount_outstanding;

		// ====== R31 ======
		private String r31_product;
		private BigDecimal r31_no_of_ac;
		private BigDecimal r31_approved_limit;
		private BigDecimal r31_amount_outstanding;

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

		public String getR10_product() {
			return r10_product;
		}

		public void setR10_product(String r10_product) {
			this.r10_product = r10_product;
		}

		public BigDecimal getR10_no_of_ac() {
			return r10_no_of_ac;
		}

		public void setR10_no_of_ac(BigDecimal r10_no_of_ac) {
			this.r10_no_of_ac = r10_no_of_ac;
		}

		public BigDecimal getR10_approved_limit() {
			return r10_approved_limit;
		}

		public void setR10_approved_limit(BigDecimal r10_approved_limit) {
			this.r10_approved_limit = r10_approved_limit;
		}

		public BigDecimal getR10_amount_outstanding() {
			return r10_amount_outstanding;
		}

		public void setR10_amount_outstanding(BigDecimal r10_amount_outstanding) {
			this.r10_amount_outstanding = r10_amount_outstanding;
		}

		public String getR11_product() {
			return r11_product;
		}

		public void setR11_product(String r11_product) {
			this.r11_product = r11_product;
		}

		public BigDecimal getR11_no_of_ac() {
			return r11_no_of_ac;
		}

		public void setR11_no_of_ac(BigDecimal r11_no_of_ac) {
			this.r11_no_of_ac = r11_no_of_ac;
		}

		public BigDecimal getR11_approved_limit() {
			return r11_approved_limit;
		}

		public void setR11_approved_limit(BigDecimal r11_approved_limit) {
			this.r11_approved_limit = r11_approved_limit;
		}

		public BigDecimal getR11_amount_outstanding() {
			return r11_amount_outstanding;
		}

		public void setR11_amount_outstanding(BigDecimal r11_amount_outstanding) {
			this.r11_amount_outstanding = r11_amount_outstanding;
		}

		public String getR12_product() {
			return r12_product;
		}

		public void setR12_product(String r12_product) {
			this.r12_product = r12_product;
		}

		public BigDecimal getR12_no_of_ac() {
			return r12_no_of_ac;
		}

		public void setR12_no_of_ac(BigDecimal r12_no_of_ac) {
			this.r12_no_of_ac = r12_no_of_ac;
		}

		public BigDecimal getR12_approved_limit() {
			return r12_approved_limit;
		}

		public void setR12_approved_limit(BigDecimal r12_approved_limit) {
			this.r12_approved_limit = r12_approved_limit;
		}

		public BigDecimal getR12_amount_outstanding() {
			return r12_amount_outstanding;
		}

		public void setR12_amount_outstanding(BigDecimal r12_amount_outstanding) {
			this.r12_amount_outstanding = r12_amount_outstanding;
		}

		public String getR13_product() {
			return r13_product;
		}

		public void setR13_product(String r13_product) {
			this.r13_product = r13_product;
		}

		public BigDecimal getR13_no_of_ac() {
			return r13_no_of_ac;
		}

		public void setR13_no_of_ac(BigDecimal r13_no_of_ac) {
			this.r13_no_of_ac = r13_no_of_ac;
		}

		public BigDecimal getR13_approved_limit() {
			return r13_approved_limit;
		}

		public void setR13_approved_limit(BigDecimal r13_approved_limit) {
			this.r13_approved_limit = r13_approved_limit;
		}

		public BigDecimal getR13_amount_outstanding() {
			return r13_amount_outstanding;
		}

		public void setR13_amount_outstanding(BigDecimal r13_amount_outstanding) {
			this.r13_amount_outstanding = r13_amount_outstanding;
		}

		public String getR14_product() {
			return r14_product;
		}

		public void setR14_product(String r14_product) {
			this.r14_product = r14_product;
		}

		public BigDecimal getR14_no_of_ac() {
			return r14_no_of_ac;
		}

		public void setR14_no_of_ac(BigDecimal r14_no_of_ac) {
			this.r14_no_of_ac = r14_no_of_ac;
		}

		public BigDecimal getR14_approved_limit() {
			return r14_approved_limit;
		}

		public void setR14_approved_limit(BigDecimal r14_approved_limit) {
			this.r14_approved_limit = r14_approved_limit;
		}

		public BigDecimal getR14_amount_outstanding() {
			return r14_amount_outstanding;
		}

		public void setR14_amount_outstanding(BigDecimal r14_amount_outstanding) {
			this.r14_amount_outstanding = r14_amount_outstanding;
		}

		public String getR15_product() {
			return r15_product;
		}

		public void setR15_product(String r15_product) {
			this.r15_product = r15_product;
		}

		public BigDecimal getR15_no_of_ac() {
			return r15_no_of_ac;
		}

		public void setR15_no_of_ac(BigDecimal r15_no_of_ac) {
			this.r15_no_of_ac = r15_no_of_ac;
		}

		public BigDecimal getR15_approved_limit() {
			return r15_approved_limit;
		}

		public void setR15_approved_limit(BigDecimal r15_approved_limit) {
			this.r15_approved_limit = r15_approved_limit;
		}

		public BigDecimal getR15_amount_outstanding() {
			return r15_amount_outstanding;
		}

		public void setR15_amount_outstanding(BigDecimal r15_amount_outstanding) {
			this.r15_amount_outstanding = r15_amount_outstanding;
		}

		public String getR16_product() {
			return r16_product;
		}

		public void setR16_product(String r16_product) {
			this.r16_product = r16_product;
		}

		public BigDecimal getR16_no_of_ac() {
			return r16_no_of_ac;
		}

		public void setR16_no_of_ac(BigDecimal r16_no_of_ac) {
			this.r16_no_of_ac = r16_no_of_ac;
		}

		public BigDecimal getR16_approved_limit() {
			return r16_approved_limit;
		}

		public void setR16_approved_limit(BigDecimal r16_approved_limit) {
			this.r16_approved_limit = r16_approved_limit;
		}

		public BigDecimal getR16_amount_outstanding() {
			return r16_amount_outstanding;
		}

		public void setR16_amount_outstanding(BigDecimal r16_amount_outstanding) {
			this.r16_amount_outstanding = r16_amount_outstanding;
		}

		public String getR21_product() {
			return r21_product;
		}

		public void setR21_product(String r21_product) {
			this.r21_product = r21_product;
		}

		public BigDecimal getR21_no_of_ac() {
			return r21_no_of_ac;
		}

		public void setR21_no_of_ac(BigDecimal r21_no_of_ac) {
			this.r21_no_of_ac = r21_no_of_ac;
		}

		public BigDecimal getR21_approved_limit() {
			return r21_approved_limit;
		}

		public void setR21_approved_limit(BigDecimal r21_approved_limit) {
			this.r21_approved_limit = r21_approved_limit;
		}

		public BigDecimal getR21_amount_outstanding() {
			return r21_amount_outstanding;
		}

		public void setR21_amount_outstanding(BigDecimal r21_amount_outstanding) {
			this.r21_amount_outstanding = r21_amount_outstanding;
		}

		public String getR22_product() {
			return r22_product;
		}

		public void setR22_product(String r22_product) {
			this.r22_product = r22_product;
		}

		public BigDecimal getR22_no_of_ac() {
			return r22_no_of_ac;
		}

		public void setR22_no_of_ac(BigDecimal r22_no_of_ac) {
			this.r22_no_of_ac = r22_no_of_ac;
		}

		public BigDecimal getR22_approved_limit() {
			return r22_approved_limit;
		}

		public void setR22_approved_limit(BigDecimal r22_approved_limit) {
			this.r22_approved_limit = r22_approved_limit;
		}

		public BigDecimal getR22_amount_outstanding() {
			return r22_amount_outstanding;
		}

		public void setR22_amount_outstanding(BigDecimal r22_amount_outstanding) {
			this.r22_amount_outstanding = r22_amount_outstanding;
		}

		public String getR23_product() {
			return r23_product;
		}

		public void setR23_product(String r23_product) {
			this.r23_product = r23_product;
		}

		public BigDecimal getR23_no_of_ac() {
			return r23_no_of_ac;
		}

		public void setR23_no_of_ac(BigDecimal r23_no_of_ac) {
			this.r23_no_of_ac = r23_no_of_ac;
		}

		public BigDecimal getR23_approved_limit() {
			return r23_approved_limit;
		}

		public void setR23_approved_limit(BigDecimal r23_approved_limit) {
			this.r23_approved_limit = r23_approved_limit;
		}

		public BigDecimal getR23_amount_outstanding() {
			return r23_amount_outstanding;
		}

		public void setR23_amount_outstanding(BigDecimal r23_amount_outstanding) {
			this.r23_amount_outstanding = r23_amount_outstanding;
		}

		public String getR24_product() {
			return r24_product;
		}

		public void setR24_product(String r24_product) {
			this.r24_product = r24_product;
		}

		public BigDecimal getR24_no_of_ac() {
			return r24_no_of_ac;
		}

		public void setR24_no_of_ac(BigDecimal r24_no_of_ac) {
			this.r24_no_of_ac = r24_no_of_ac;
		}

		public BigDecimal getR24_approved_limit() {
			return r24_approved_limit;
		}

		public void setR24_approved_limit(BigDecimal r24_approved_limit) {
			this.r24_approved_limit = r24_approved_limit;
		}

		public BigDecimal getR24_amount_outstanding() {
			return r24_amount_outstanding;
		}

		public void setR24_amount_outstanding(BigDecimal r24_amount_outstanding) {
			this.r24_amount_outstanding = r24_amount_outstanding;
		}

		public String getR25_product() {
			return r25_product;
		}

		public void setR25_product(String r25_product) {
			this.r25_product = r25_product;
		}

		public BigDecimal getR25_no_of_ac() {
			return r25_no_of_ac;
		}

		public void setR25_no_of_ac(BigDecimal r25_no_of_ac) {
			this.r25_no_of_ac = r25_no_of_ac;
		}

		public BigDecimal getR25_approved_limit() {
			return r25_approved_limit;
		}

		public void setR25_approved_limit(BigDecimal r25_approved_limit) {
			this.r25_approved_limit = r25_approved_limit;
		}

		public BigDecimal getR25_amount_outstanding() {
			return r25_amount_outstanding;
		}

		public void setR25_amount_outstanding(BigDecimal r25_amount_outstanding) {
			this.r25_amount_outstanding = r25_amount_outstanding;
		}

		public String getR26_product() {
			return r26_product;
		}

		public void setR26_product(String r26_product) {
			this.r26_product = r26_product;
		}

		public BigDecimal getR26_no_of_ac() {
			return r26_no_of_ac;
		}

		public void setR26_no_of_ac(BigDecimal r26_no_of_ac) {
			this.r26_no_of_ac = r26_no_of_ac;
		}

		public BigDecimal getR26_approved_limit() {
			return r26_approved_limit;
		}

		public void setR26_approved_limit(BigDecimal r26_approved_limit) {
			this.r26_approved_limit = r26_approved_limit;
		}

		public BigDecimal getR26_amount_outstanding() {
			return r26_amount_outstanding;
		}

		public void setR26_amount_outstanding(BigDecimal r26_amount_outstanding) {
			this.r26_amount_outstanding = r26_amount_outstanding;
		}

		public String getR27_product() {
			return r27_product;
		}

		public void setR27_product(String r27_product) {
			this.r27_product = r27_product;
		}

		public BigDecimal getR27_no_of_ac() {
			return r27_no_of_ac;
		}

		public void setR27_no_of_ac(BigDecimal r27_no_of_ac) {
			this.r27_no_of_ac = r27_no_of_ac;
		}

		public BigDecimal getR27_approved_limit() {
			return r27_approved_limit;
		}

		public void setR27_approved_limit(BigDecimal r27_approved_limit) {
			this.r27_approved_limit = r27_approved_limit;
		}

		public BigDecimal getR27_amount_outstanding() {
			return r27_amount_outstanding;
		}

		public void setR27_amount_outstanding(BigDecimal r27_amount_outstanding) {
			this.r27_amount_outstanding = r27_amount_outstanding;
		}

		public String getR28_product() {
			return r28_product;
		}

		public void setR28_product(String r28_product) {
			this.r28_product = r28_product;
		}

		public BigDecimal getR28_no_of_ac() {
			return r28_no_of_ac;
		}

		public void setR28_no_of_ac(BigDecimal r28_no_of_ac) {
			this.r28_no_of_ac = r28_no_of_ac;
		}

		public BigDecimal getR28_approved_limit() {
			return r28_approved_limit;
		}

		public void setR28_approved_limit(BigDecimal r28_approved_limit) {
			this.r28_approved_limit = r28_approved_limit;
		}

		public BigDecimal getR28_amount_outstanding() {
			return r28_amount_outstanding;
		}

		public void setR28_amount_outstanding(BigDecimal r28_amount_outstanding) {
			this.r28_amount_outstanding = r28_amount_outstanding;
		}

		public String getR29_product() {
			return r29_product;
		}

		public void setR29_product(String r29_product) {
			this.r29_product = r29_product;
		}

		public BigDecimal getR29_no_of_ac() {
			return r29_no_of_ac;
		}

		public void setR29_no_of_ac(BigDecimal r29_no_of_ac) {
			this.r29_no_of_ac = r29_no_of_ac;
		}

		public BigDecimal getR29_approved_limit() {
			return r29_approved_limit;
		}

		public void setR29_approved_limit(BigDecimal r29_approved_limit) {
			this.r29_approved_limit = r29_approved_limit;
		}

		public BigDecimal getR29_amount_outstanding() {
			return r29_amount_outstanding;
		}

		public void setR29_amount_outstanding(BigDecimal r29_amount_outstanding) {
			this.r29_amount_outstanding = r29_amount_outstanding;
		}

		public String getR30_product() {
			return r30_product;
		}

		public void setR30_product(String r30_product) {
			this.r30_product = r30_product;
		}

		public BigDecimal getR30_no_of_ac() {
			return r30_no_of_ac;
		}

		public void setR30_no_of_ac(BigDecimal r30_no_of_ac) {
			this.r30_no_of_ac = r30_no_of_ac;
		}

		public BigDecimal getR30_approved_limit() {
			return r30_approved_limit;
		}

		public void setR30_approved_limit(BigDecimal r30_approved_limit) {
			this.r30_approved_limit = r30_approved_limit;
		}

		public BigDecimal getR30_amount_outstanding() {
			return r30_amount_outstanding;
		}

		public void setR30_amount_outstanding(BigDecimal r30_amount_outstanding) {
			this.r30_amount_outstanding = r30_amount_outstanding;
		}

		public String getR31_product() {
			return r31_product;
		}

		public void setR31_product(String r31_product) {
			this.r31_product = r31_product;
		}

		public BigDecimal getR31_no_of_ac() {
			return r31_no_of_ac;
		}

		public void setR31_no_of_ac(BigDecimal r31_no_of_ac) {
			this.r31_no_of_ac = r31_no_of_ac;
		}

		public BigDecimal getR31_approved_limit() {
			return r31_approved_limit;
		}

		public void setR31_approved_limit(BigDecimal r31_approved_limit) {
			this.r31_approved_limit = r31_approved_limit;
		}

		public BigDecimal getR31_amount_outstanding() {
			return r31_amount_outstanding;
		}

		public void setR31_amount_outstanding(BigDecimal r31_amount_outstanding) {
			this.r31_amount_outstanding = r31_amount_outstanding;
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

	// ARCHIVAL ROW MAPPER

	class M_LA3_RowMapper_Archival2 implements RowMapper<M_LA3_Archival_Summary_Entity2> {

		@Override
		public M_LA3_Archival_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Archival_Summary_Entity2 obj = new M_LA3_Archival_Summary_Entity2();

			// ====== R36 ======
			obj.setR36_PRODUCT(rs.getString("R36_PRODUCT"));
			obj.setR36_NO_OF_AC(rs.getBigDecimal("R36_NO_OF_AC"));
			obj.setR36_CREDIT_LIMIT(rs.getBigDecimal("R36_CREDIT_LIMIT"));
			obj.setR36_AMOUNT_OUTSTANDING(rs.getBigDecimal("R36_AMOUNT_OUTSTANDING"));

			// ====== R37 ======
			obj.setR37_PRODUCT(rs.getString("R37_PRODUCT"));
			obj.setR37_NO_OF_AC(rs.getBigDecimal("R37_NO_OF_AC"));
			obj.setR37_CREDIT_LIMIT(rs.getBigDecimal("R37_CREDIT_LIMIT"));
			obj.setR37_AMOUNT_OUTSTANDING(rs.getBigDecimal("R37_AMOUNT_OUTSTANDING"));

			// ====== R38 ======
			obj.setR38_PRODUCT(rs.getString("R38_PRODUCT"));
			obj.setR38_NO_OF_AC(rs.getBigDecimal("R38_NO_OF_AC"));
			obj.setR38_CREDIT_LIMIT(rs.getBigDecimal("R38_CREDIT_LIMIT"));
			obj.setR38_AMOUNT_OUTSTANDING(rs.getBigDecimal("R38_AMOUNT_OUTSTANDING"));

			// ====== R39 ======
			obj.setR39_PRODUCT(rs.getString("R39_PRODUCT"));
			obj.setR39_NO_OF_AC(rs.getBigDecimal("R39_NO_OF_AC"));
			obj.setR39_CREDIT_LIMIT(rs.getBigDecimal("R39_CREDIT_LIMIT"));
			obj.setR39_AMOUNT_OUTSTANDING(rs.getBigDecimal("R39_AMOUNT_OUTSTANDING"));

			// ====== R40 ======
			obj.setR40_PRODUCT(rs.getString("R40_PRODUCT"));
			obj.setR40_NO_OF_AC(rs.getBigDecimal("R40_NO_OF_AC"));
			obj.setR40_CREDIT_LIMIT(rs.getBigDecimal("R40_CREDIT_LIMIT"));
			obj.setR40_AMOUNT_OUTSTANDING(rs.getBigDecimal("R40_AMOUNT_OUTSTANDING"));

			// ====== R41 ======
			obj.setR41_PRODUCT(rs.getString("R41_PRODUCT"));
			obj.setR41_NO_OF_AC(rs.getBigDecimal("R41_NO_OF_AC"));
			obj.setR41_CREDIT_LIMIT(rs.getBigDecimal("R41_CREDIT_LIMIT"));
			obj.setR41_AMOUNT_OUTSTANDING(rs.getBigDecimal("R41_AMOUNT_OUTSTANDING"));

			// ====== R42 ======
			obj.setR42_PRODUCT(rs.getString("R42_PRODUCT"));
			obj.setR42_NO_OF_AC(rs.getBigDecimal("R42_NO_OF_AC"));
			obj.setR42_CREDIT_LIMIT(rs.getBigDecimal("R42_CREDIT_LIMIT"));
			obj.setR42_AMOUNT_OUTSTANDING(rs.getBigDecimal("R42_AMOUNT_OUTSTANDING"));

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

	public static class M_LA3_Archival_Summary_Entity2 {

		// ====== R36 ======
		private String R36_PRODUCT;
		private BigDecimal R36_NO_OF_AC;
		private BigDecimal R36_CREDIT_LIMIT;
		private BigDecimal R36_AMOUNT_OUTSTANDING;

		// ====== R37 ======
		private String R37_PRODUCT;
		private BigDecimal R37_NO_OF_AC;
		private BigDecimal R37_CREDIT_LIMIT;
		private BigDecimal R37_AMOUNT_OUTSTANDING;

		// ====== R38 ======
		private String R38_PRODUCT;
		private BigDecimal R38_NO_OF_AC;
		private BigDecimal R38_CREDIT_LIMIT;
		private BigDecimal R38_AMOUNT_OUTSTANDING;

		// ====== R39 ======
		private String R39_PRODUCT;
		private BigDecimal R39_NO_OF_AC;
		private BigDecimal R39_CREDIT_LIMIT;
		private BigDecimal R39_AMOUNT_OUTSTANDING;

		// ====== R40 ======
		private String R40_PRODUCT;
		private BigDecimal R40_NO_OF_AC;
		private BigDecimal R40_CREDIT_LIMIT;
		private BigDecimal R40_AMOUNT_OUTSTANDING;

		// ====== R41 ======
		private String R41_PRODUCT;
		private BigDecimal R41_NO_OF_AC;
		private BigDecimal R41_CREDIT_LIMIT;
		private BigDecimal R41_AMOUNT_OUTSTANDING;

		// ====== R42 ======
		private String R42_PRODUCT;
		private BigDecimal R42_NO_OF_AC;
		private BigDecimal R42_CREDIT_LIMIT;
		private BigDecimal R42_AMOUNT_OUTSTANDING;

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

		public String getR36_PRODUCT() {
			return R36_PRODUCT;
		}

		public void setR36_PRODUCT(String r36_PRODUCT) {
			R36_PRODUCT = r36_PRODUCT;
		}

		public BigDecimal getR36_NO_OF_AC() {
			return R36_NO_OF_AC;
		}

		public void setR36_NO_OF_AC(BigDecimal r36_NO_OF_AC) {
			R36_NO_OF_AC = r36_NO_OF_AC;
		}

		public BigDecimal getR36_CREDIT_LIMIT() {
			return R36_CREDIT_LIMIT;
		}

		public void setR36_CREDIT_LIMIT(BigDecimal r36_CREDIT_LIMIT) {
			R36_CREDIT_LIMIT = r36_CREDIT_LIMIT;
		}

		public BigDecimal getR36_AMOUNT_OUTSTANDING() {
			return R36_AMOUNT_OUTSTANDING;
		}

		public void setR36_AMOUNT_OUTSTANDING(BigDecimal r36_AMOUNT_OUTSTANDING) {
			R36_AMOUNT_OUTSTANDING = r36_AMOUNT_OUTSTANDING;
		}

		public String getR37_PRODUCT() {
			return R37_PRODUCT;
		}

		public void setR37_PRODUCT(String r37_PRODUCT) {
			R37_PRODUCT = r37_PRODUCT;
		}

		public BigDecimal getR37_NO_OF_AC() {
			return R37_NO_OF_AC;
		}

		public void setR37_NO_OF_AC(BigDecimal r37_NO_OF_AC) {
			R37_NO_OF_AC = r37_NO_OF_AC;
		}

		public BigDecimal getR37_CREDIT_LIMIT() {
			return R37_CREDIT_LIMIT;
		}

		public void setR37_CREDIT_LIMIT(BigDecimal r37_CREDIT_LIMIT) {
			R37_CREDIT_LIMIT = r37_CREDIT_LIMIT;
		}

		public BigDecimal getR37_AMOUNT_OUTSTANDING() {
			return R37_AMOUNT_OUTSTANDING;
		}

		public void setR37_AMOUNT_OUTSTANDING(BigDecimal r37_AMOUNT_OUTSTANDING) {
			R37_AMOUNT_OUTSTANDING = r37_AMOUNT_OUTSTANDING;
		}

		public String getR38_PRODUCT() {
			return R38_PRODUCT;
		}

		public void setR38_PRODUCT(String r38_PRODUCT) {
			R38_PRODUCT = r38_PRODUCT;
		}

		public BigDecimal getR38_NO_OF_AC() {
			return R38_NO_OF_AC;
		}

		public void setR38_NO_OF_AC(BigDecimal r38_NO_OF_AC) {
			R38_NO_OF_AC = r38_NO_OF_AC;
		}

		public BigDecimal getR38_CREDIT_LIMIT() {
			return R38_CREDIT_LIMIT;
		}

		public void setR38_CREDIT_LIMIT(BigDecimal r38_CREDIT_LIMIT) {
			R38_CREDIT_LIMIT = r38_CREDIT_LIMIT;
		}

		public BigDecimal getR38_AMOUNT_OUTSTANDING() {
			return R38_AMOUNT_OUTSTANDING;
		}

		public void setR38_AMOUNT_OUTSTANDING(BigDecimal r38_AMOUNT_OUTSTANDING) {
			R38_AMOUNT_OUTSTANDING = r38_AMOUNT_OUTSTANDING;
		}

		public String getR39_PRODUCT() {
			return R39_PRODUCT;
		}

		public void setR39_PRODUCT(String r39_PRODUCT) {
			R39_PRODUCT = r39_PRODUCT;
		}

		public BigDecimal getR39_NO_OF_AC() {
			return R39_NO_OF_AC;
		}

		public void setR39_NO_OF_AC(BigDecimal r39_NO_OF_AC) {
			R39_NO_OF_AC = r39_NO_OF_AC;
		}

		public BigDecimal getR39_CREDIT_LIMIT() {
			return R39_CREDIT_LIMIT;
		}

		public void setR39_CREDIT_LIMIT(BigDecimal r39_CREDIT_LIMIT) {
			R39_CREDIT_LIMIT = r39_CREDIT_LIMIT;
		}

		public BigDecimal getR39_AMOUNT_OUTSTANDING() {
			return R39_AMOUNT_OUTSTANDING;
		}

		public void setR39_AMOUNT_OUTSTANDING(BigDecimal r39_AMOUNT_OUTSTANDING) {
			R39_AMOUNT_OUTSTANDING = r39_AMOUNT_OUTSTANDING;
		}

		public String getR40_PRODUCT() {
			return R40_PRODUCT;
		}

		public void setR40_PRODUCT(String r40_PRODUCT) {
			R40_PRODUCT = r40_PRODUCT;
		}

		public BigDecimal getR40_NO_OF_AC() {
			return R40_NO_OF_AC;
		}

		public void setR40_NO_OF_AC(BigDecimal r40_NO_OF_AC) {
			R40_NO_OF_AC = r40_NO_OF_AC;
		}

		public BigDecimal getR40_CREDIT_LIMIT() {
			return R40_CREDIT_LIMIT;
		}

		public void setR40_CREDIT_LIMIT(BigDecimal r40_CREDIT_LIMIT) {
			R40_CREDIT_LIMIT = r40_CREDIT_LIMIT;
		}

		public BigDecimal getR40_AMOUNT_OUTSTANDING() {
			return R40_AMOUNT_OUTSTANDING;
		}

		public void setR40_AMOUNT_OUTSTANDING(BigDecimal r40_AMOUNT_OUTSTANDING) {
			R40_AMOUNT_OUTSTANDING = r40_AMOUNT_OUTSTANDING;
		}

		public String getR41_PRODUCT() {
			return R41_PRODUCT;
		}

		public void setR41_PRODUCT(String r41_PRODUCT) {
			R41_PRODUCT = r41_PRODUCT;
		}

		public BigDecimal getR41_NO_OF_AC() {
			return R41_NO_OF_AC;
		}

		public void setR41_NO_OF_AC(BigDecimal r41_NO_OF_AC) {
			R41_NO_OF_AC = r41_NO_OF_AC;
		}

		public BigDecimal getR41_CREDIT_LIMIT() {
			return R41_CREDIT_LIMIT;
		}

		public void setR41_CREDIT_LIMIT(BigDecimal r41_CREDIT_LIMIT) {
			R41_CREDIT_LIMIT = r41_CREDIT_LIMIT;
		}

		public BigDecimal getR41_AMOUNT_OUTSTANDING() {
			return R41_AMOUNT_OUTSTANDING;
		}

		public void setR41_AMOUNT_OUTSTANDING(BigDecimal r41_AMOUNT_OUTSTANDING) {
			R41_AMOUNT_OUTSTANDING = r41_AMOUNT_OUTSTANDING;
		}

		public String getR42_PRODUCT() {
			return R42_PRODUCT;
		}

		public void setR42_PRODUCT(String r42_PRODUCT) {
			R42_PRODUCT = r42_PRODUCT;
		}

		public BigDecimal getR42_NO_OF_AC() {
			return R42_NO_OF_AC;
		}

		public void setR42_NO_OF_AC(BigDecimal r42_NO_OF_AC) {
			R42_NO_OF_AC = r42_NO_OF_AC;
		}

		public BigDecimal getR42_CREDIT_LIMIT() {
			return R42_CREDIT_LIMIT;
		}

		public void setR42_CREDIT_LIMIT(BigDecimal r42_CREDIT_LIMIT) {
			R42_CREDIT_LIMIT = r42_CREDIT_LIMIT;
		}

		public BigDecimal getR42_AMOUNT_OUTSTANDING() {
			return R42_AMOUNT_OUTSTANDING;
		}

		public void setR42_AMOUNT_OUTSTANDING(BigDecimal r42_AMOUNT_OUTSTANDING) {
			R42_AMOUNT_OUTSTANDING = r42_AMOUNT_OUTSTANDING;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date rEPORT_DATE) {
			REPORT_DATE = rEPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal rEPORT_VERSION) {
			REPORT_VERSION = rEPORT_VERSION;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
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

	// =====================================================
	// DETAIL ENTITY
	// =====================================================

	public class M_LA3DetailRowMapper implements RowMapper<M_LA3_Detail_Entity> {

		@Override
		public M_LA3_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Detail_Entity obj = new M_LA3_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getTimestamp("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getTimestamp("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setInt_bucket(rs.getString("INT_BUCKET"));
			obj.setFacility(rs.getString("FACILITY"));
			obj.setMat_bucket(rs.getString("MAT_BUCKET"));
			obj.setSanction_limit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_label_1(rs.getString("REPORT_LABEL_1"));

			return obj;
		}
	}

	public class M_LA3_Detail_Entity {

		private Long sno;
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_addl_criteria_1;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private String segment;
		private String int_bucket;
		private String facility;
		private String mat_bucket;
		private BigDecimal sanction_limit;
		private String report_label;
		private String report_label_1;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
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

		public String getReport_addl_criteria_2() {
			return report_addl_criteria_2;
		}

		public void setReport_addl_criteria_2(String report_addl_criteria_2) {
			this.report_addl_criteria_2 = report_addl_criteria_2;
		}

		public String getReport_addl_criteria_3() {
			return report_addl_criteria_3;
		}

		public void setReport_addl_criteria_3(String report_addl_criteria_3) {
			this.report_addl_criteria_3 = report_addl_criteria_3;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getInt_bucket() {
			return int_bucket;
		}

		public void setInt_bucket(String int_bucket) {
			this.int_bucket = int_bucket;
		}

		public String getFacility() {
			return facility;
		}

		public void setFacility(String facility) {
			this.facility = facility;
		}

		public String getMat_bucket() {
			return mat_bucket;
		}

		public void setMat_bucket(String mat_bucket) {
			this.mat_bucket = mat_bucket;
		}

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_label_1() {
			return report_label_1;
		}

		public void setReport_label_1(String report_label_1) {
			this.report_label_1 = report_label_1;
		}

	}

	// =====================================================
	// ARCHIVAL DETAIL ENTITY
	// =====================================================

	public class M_LA3ArchivalDetailRowMapper implements RowMapper<M_LA3_Archival_Detail_Entity> {

		@Override
		public M_LA3_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			M_LA3_Archival_Detail_Entity obj = new M_LA3_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
			obj.setCust_id(rs.getString("CUST_ID"));
			obj.setAcct_number(rs.getString("ACCT_NUMBER"));
			obj.setAcct_name(rs.getString("ACCT_NAME"));
			obj.setData_type(rs.getString("DATA_TYPE"));
			obj.setReport_addl_criteria_1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReport_remarks(rs.getString("REPORT_REMARKS"));
			obj.setModification_remarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setData_entry_version(rs.getString("DATA_ENTRY_VERSION"));
			obj.setAcct_balance_in_pula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReport_date(rs.getDate("REPORT_DATE"));
			obj.setReport_name(rs.getString("REPORT_NAME"));
			obj.setCreate_user(rs.getString("CREATE_USER"));
			obj.setCreate_time(rs.getTimestamp("CREATE_TIME"));
			obj.setModify_user(rs.getString("MODIFY_USER"));
			obj.setModify_time(rs.getTimestamp("MODIFY_TIME"));
			obj.setVerify_user(rs.getString("VERIFY_USER"));
			obj.setVerify_time(rs.getTimestamp("VERIFY_TIME"));

			obj.setEntity_flg(rs.getString("ENTITY_FLG"));
			obj.setModify_flg(rs.getString("MODIFY_FLG"));
			obj.setDel_flg(rs.getString("DEL_FLG"));
			obj.setReport_addl_criteria_2(rs.getString("REPORT_ADDL_CRITERIA_2"));
			obj.setReport_addl_criteria_3(rs.getString("REPORT_ADDL_CRITERIA_3"));
			obj.setSegment(rs.getString("SEGMENT"));
			obj.setInt_bucket(rs.getString("INT_BUCKET"));
			obj.setFacility(rs.getString("FACILITY"));
			obj.setMat_bucket(rs.getString("MAT_BUCKET"));
			obj.setSanction_limit(rs.getBigDecimal("SANCTION_LIMIT"));
			obj.setReport_label(rs.getString("REPORT_LABEL"));
			obj.setReport_label_1(rs.getString("REPORT_LABEL_1"));

			return obj;
		}
	}

	public class M_LA3_Archival_Detail_Entity {

		private Long sno;
		private String cust_id;
		private String acct_number;
		private String acct_name;
		private String data_type;
		private String report_addl_criteria_1;
		private String report_remarks;
		private String modification_remarks;
		private String data_entry_version;
		private BigDecimal acct_balance_in_pula;

		@DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date report_date;
		private String report_name;
		private String create_user;
		private Date create_time;
		private String modify_user;
		private Date modify_time;
		private String verify_user;
		private Date verify_time;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;
		private String report_addl_criteria_2;
		private String report_addl_criteria_3;
		private String segment;
		private String int_bucket;
		private String facility;
		private String mat_bucket;
		private BigDecimal sanction_limit;
		private String report_label;
		private String report_label_1;

		public Long getSno() {
			return sno;
		}

		public void setSno(Long sno) {
			this.sno = sno;
		}

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

		public String getReport_addl_criteria_1() {
			return report_addl_criteria_1;
		}

		public void setReport_addl_criteria_1(String report_addl_criteria_1) {
			this.report_addl_criteria_1 = report_addl_criteria_1;
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

		public String getReport_addl_criteria_2() {
			return report_addl_criteria_2;
		}

		public void setReport_addl_criteria_2(String report_addl_criteria_2) {
			this.report_addl_criteria_2 = report_addl_criteria_2;
		}

		public String getReport_addl_criteria_3() {
			return report_addl_criteria_3;
		}

		public void setReport_addl_criteria_3(String report_addl_criteria_3) {
			this.report_addl_criteria_3 = report_addl_criteria_3;
		}

		public String getSegment() {
			return segment;
		}

		public void setSegment(String segment) {
			this.segment = segment;
		}

		public String getInt_bucket() {
			return int_bucket;
		}

		public void setInt_bucket(String int_bucket) {
			this.int_bucket = int_bucket;
		}

		public String getFacility() {
			return facility;
		}

		public void setFacility(String facility) {
			this.facility = facility;
		}

		public String getMat_bucket() {
			return mat_bucket;
		}

		public void setMat_bucket(String mat_bucket) {
			this.mat_bucket = mat_bucket;
		}

		public BigDecimal getSanction_limit() {
			return sanction_limit;
		}

		public void setSanction_limit(BigDecimal sanction_limit) {
			this.sanction_limit = sanction_limit;
		}

		public String getReport_label() {
			return report_label;
		}

		public void setReport_label(String report_label) {
			this.report_label = report_label;
		}

		public String getReport_label_1() {
			return report_label_1;
		}

		public void setReport_label_1(String report_label_1) {
			this.report_label_1 = report_label_1;
		}

	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getM_LA3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {
		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);
		
		System.out.println("M_LA3 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		Session hs = sessionFactory.getCurrentSession();
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {
			List<M_LA3_Archival_Summary_Entity1> T1Master = new ArrayList<M_LA3_Archival_Summary_Entity1>();
			List<M_LA3_Archival_Summary_Entity2> T1Master1 = new ArrayList<M_LA3_Archival_Summary_Entity2>();
			try {
				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival1(dt, version);
				T1Master1 = getdatabydateListarchival2(dt, version);
				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion2(dt, version));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
		} else {
			List<M_LA3_Summary_Entity1> T1Master = new ArrayList<M_LA3_Summary_Entity1>();
			List<M_LA3_Summary_Entity2> T1Master1 = new ArrayList<M_LA3_Summary_Entity2>();
			try {
				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate1(dt);
				T1Master1 = getDataByDate2(dt);
				System.out.println("la3 view");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T1Master1);
		}

		// T1rep = t1CurProdServiceRepo.getT1CurProdServices(d1);
		mv.setViewName("BRRS/M_LA3");
		mv.addObject("displaymode", "summary");
		System.out.println("scv" + mv.getViewName());
		return mv;
	}

	public ModelAndView getM_LA3currentDtl(String reportId, String fromdate, String todate, String currency,
	        String dtltype, Pageable pageable, String filter, String type, String version, HttpServletRequest req1,
	        Model md) {

	    ModelAndView mv = new ModelAndView();

	    String userid = (String) req1.getSession().getAttribute("USERID");
	    System.out.println("User Id Maker and Checker: " + userid);

	    String role = userProfileRep.getUserRole(userid);
	    md.addAttribute("role", role);

	    System.out.println("Role : " + role);

	    try {

	        Date parsedDate = null;

	        if (todate != null && !todate.isEmpty()) {
	            parsedDate = dateformat.parse(todate);
	        }

	        String reportLabel = null;
	        String reportAddlCriteria1 = null;
	        String reportAddlCriteria2 = null;
	        String reportAddlCriteria3 = null;

	        if (filter != null && filter.contains(",")) {

	            String[] parts = filter.split(",");

	            if (parts.length >= 4) {
	                reportLabel = parts[0];
	                reportAddlCriteria1 = parts[1];
	                reportAddlCriteria2 = parts[2];
	                reportAddlCriteria3 = parts[3];
	            }
	        }

	        // ==========================
	        // ARCHIVAL / RESUB
	        // ==========================

	        if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

	            System.out.println(type + " DETAIL MODE");

	            List<M_LA3_Archival_Detail_Entity> detailList;

	            if (reportLabel != null) {

	                detailList = GetArchivalDataByRowIdAndColumnId(
	                        reportLabel,
	                        reportAddlCriteria1,
	                        reportAddlCriteria2,
	                        reportAddlCriteria3,
	                        parsedDate,
	                        version);

	            } else {

	                detailList = getArchivalDetaildatabydateList(parsedDate, version);
	            }

	            mv.addObject("reportdetails", detailList);
	            mv.addObject("reportmaster12", detailList);

	            System.out.println(type + " DETAIL COUNT : " + detailList.size());

	        }

	        // ==========================
	        // CURRENT
	        // ==========================

	        else {

	            List<M_LA3_Detail_Entity> currentDetailList;

	            if (reportLabel != null) {

	                currentDetailList = GetDetailDataByRowIdAndColumnId(
	                        reportLabel,
	                        reportAddlCriteria1,
	                        reportAddlCriteria2,
	                        reportAddlCriteria3,
	                        parsedDate);

	            } else {

	                currentDetailList = getDetaildatabydateList(parsedDate);
	            }

	            mv.addObject("reportdetails", currentDetailList);
	            mv.addObject("reportmaster12", currentDetailList);

	            System.out.println("CURRENT DETAIL COUNT : " + currentDetailList.size());
	        }

	    } catch (Exception e) {

	        e.printStackTrace();
	        mv.addObject("errorMessage", e.getMessage());
	    }

	    mv.setViewName("BRRS/M_LA3");
	    mv.addObject("displaymode", "Details");
	    mv.addObject("menu", reportId);
	    mv.addObject("currency", currency);
	    mv.addObject("reportId", reportId);

	    return mv;
	}

//Helper for null/empty check
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public byte[] BRRS_M_LA3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelM_LA3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
					version);
		}

		// Fetch data
		List<M_LA3_Summary_Entity1> dataList = getDataByDate1(dateformat.parse(todate));
		List<M_LA3_Summary_Entity2> dataList1 = getDataByDate2(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					M_LA3_Summary_Entity1 record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(9);
					cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
						cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
						cell2.setCellValue(record.getR11_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
						cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
						cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
						cell2.setCellValue(record.getR12_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
						cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
						cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
						cell2.setCellValue(record.getR13_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
						cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
						cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
						cell2.setCellValue(record.getR14_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
						cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
						row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
						cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
						cell2.setCellValue(record.getR15_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
						cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
						row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
						cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
						cell2.setCellValue(record.getR21_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
						cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R22 ======
					row = sheet.getRow(21);
					if (row == null) {
						row = sheet.createRow(21);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
						cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
						cell2.setCellValue(record.getR22_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
						cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
						row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR23_no_of_ac() != null) {
						cell1.setCellValue(record.getR23_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_approved_limit() != null) {
						cell2.setCellValue(record.getR23_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_amount_outstanding() != null) {
						cell3.setCellValue(record.getR23_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(23);
					if (row == null) {
						row = sheet.createRow(23);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
						cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
						cell2.setCellValue(record.getR24_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
						cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
						row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
						cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
						cell2.setCellValue(record.getR25_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
						cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
						row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
						cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
						cell2.setCellValue(record.getR26_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
						cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
						row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
						cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
						cell2.setCellValue(record.getR27_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
						cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
						row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
						cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
						cell2.setCellValue(record.getR28_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
						cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
						row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
						cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
						cell2.setCellValue(record.getR29_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
						cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
						row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
						cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
						cell2.setCellValue(record.getR30_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
						cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
				}

				int startRow1 = 35;
				// dataList1 for entity 2
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_LA3_Summary_Entity2 record1 = dataList1.get(i);

						System.out.println("rownumber = " + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ====== R37 ======
						row = sheet.getRow(36);
						if (row == null) {
							row = sheet.createRow(36);
						}
						Cell cell1 = row.createCell(1);
						if (record1.getR37_NO_OF_AC() != null) {
							cell1.setCellValue(record1.getR37_NO_OF_AC().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_LIMIT() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_LIMIT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record1.getR37_AMOUNT_OUTSTANDING() != null) {
							cell3.setCellValue(record1.getR37_AMOUNT_OUTSTANDING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// ====== R38 ======
						row = sheet.getRow(37);
						if (row == null) {
							row = sheet.createRow(37);
						}
						Cell R38cell1 = row.createCell(1);
						if (record1.getR38_NO_OF_AC() != null) {
							R38cell1.setCellValue(record1.getR38_NO_OF_AC().doubleValue());
							R38cell1.setCellStyle(numberStyle);
						} else {
							R38cell1.setCellValue("");
							R38cell1.setCellStyle(textStyle);
						}

						Cell R38cell2 = row.createCell(2);
						if (record1.getR38_CREDIT_LIMIT() != null) {
							R38cell2.setCellValue(record1.getR38_CREDIT_LIMIT().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(3);
						if (record1.getR38_AMOUNT_OUTSTANDING() != null) {
							R38cell3.setCellValue(record1.getR38_AMOUNT_OUTSTANDING().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ====== R40 ======
						row = sheet.getRow(39);
						if (row == null) {
							row = sheet.createRow(39);
						}
						Cell R40cell1 = row.createCell(1);
						if (record1.getR40_NO_OF_AC() != null) {
							R40cell1.setCellValue(record1.getR40_NO_OF_AC().doubleValue());
							R40cell1.setCellStyle(numberStyle);
						} else {
							R40cell1.setCellValue("");
							R40cell1.setCellStyle(textStyle);
						}

						Cell R40cell2 = row.createCell(2);
						if (record1.getR40_CREDIT_LIMIT() != null) {
							R40cell2.setCellValue(record1.getR40_CREDIT_LIMIT().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(3);
						if (record1.getR40_AMOUNT_OUTSTANDING() != null) {
							R40cell3.setCellValue(record1.getR40_AMOUNT_OUTSTANDING().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// ====== R41 ======
						row = sheet.getRow(40);
						if (row == null) {
							row = sheet.createRow(40);
						}
						Cell R41cell1 = row.createCell(1);
						if (record1.getR41_NO_OF_AC() != null) {
							R41cell1.setCellValue(record1.getR41_NO_OF_AC().doubleValue());
							R41cell1.setCellStyle(numberStyle);
						} else {
							R41cell1.setCellValue("");
							R41cell1.setCellStyle(textStyle);
						}

						Cell R41cell2 = row.createCell(2);
						if (record1.getR41_CREDIT_LIMIT() != null) {
							R41cell2.setCellValue(record1.getR41_CREDIT_LIMIT().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(3);
						if (record1.getR41_AMOUNT_OUTSTANDING() != null) {
							R41cell3.setCellValue(record1.getR41_AMOUNT_OUTSTANDING().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}
					}
				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			// audit
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA3_SUMMARY", null, "BRRS_M_LA3_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] BRRS_M_LA3DetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {

		try {
			logger.info("Generating Excel for BRRS_M_LA3 Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("M_LA3Detail");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "SANCTION_LIMIT", "REPORT LABEL",
					"REPORT LABEL_1", "REPORT ADDL CRETIRIA_1", "REPORT ADDL CRETIRIA_2", "REPORT ADDL CRETIRIA_3",
					"REPORT_DATE" };
			XSSFRow headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				// Amount columns: ACCT BALANCE (i=3) and average (i=4)
				if (i == 3 || i == 4) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}
			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<M_LA3_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);
			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA3_Detail_Entity item : reportData) {
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
					// Average (right aligned, 3 decimal places)
					balanceCell = row.createCell(4);
					if (item.getSanction_limit() != null) {
						balanceCell.setCellValue(item.getSanction_limit().doubleValue());
					} else {
						balanceCell.setCellValue(0);
					}
					balanceCell.setCellStyle(balanceStyle);
					row.createCell(5).setCellValue(item.getReport_label());
					row.createCell(6).setCellValue(item.getReport_label_1());
					row.createCell(7).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(8).setCellValue(item.getReport_addl_criteria_2());
					row.createCell(9).setCellValue(item.getReport_addl_criteria_3());
					row.createCell(10)
							.setCellValue(item.getReport_date() != null
									? new SimpleDateFormat("dd-MM-yyyy").format(item.getReport_date())
									: "");
					// Apply data style for all other cells
					for (int j = 0; j < 8; j++) {
						if (j != 3 && j != 4) {
							row.getCell(j).setCellStyle(dataStyle);
						}
					}
				}
			} else {
				logger.info("No data found for BRRS_M_LA3 — only header will be written.");
			}
			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();
			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA3 Excel", e);
			return null; // important
		}
	}

	// Archival View
	public List<Object[]> getM_LA3Archival() {

		List<Object[]> archivalList = new ArrayList<>();

		String sql1 = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY1 " + "ORDER BY REPORT_VERSION";

		String sql2 = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE "
				+ "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 " + "ORDER BY REPORT_VERSION";

		archivalList.addAll(jdbcTemplate.query(sql1, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") }));

		archivalList.addAll(jdbcTemplate.query(sql2, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") }));

		return archivalList;
	}

	public byte[] getExcelM_LA3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_LA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}
		List<M_LA3_Archival_Summary_Entity1> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		List<M_LA3_Archival_Summary_Entity2> dataList1 = getdatabydateListarchival2(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
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
					M_LA3_Archival_Summary_Entity1 record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
						cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
						cell2.setCellValue(record.getR11_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
						cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
						cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
						cell2.setCellValue(record.getR12_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
						cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
						cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
						cell2.setCellValue(record.getR13_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
						cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
						cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
						cell2.setCellValue(record.getR14_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
						cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
						row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
						cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
						cell2.setCellValue(record.getR15_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
						cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
						row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
						cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
						cell2.setCellValue(record.getR21_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
						cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R22 ======
					row = sheet.getRow(21);
					if (row == null) {
						row = sheet.createRow(21);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
						cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
						cell2.setCellValue(record.getR22_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
						cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
						row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR23_no_of_ac() != null) {
						cell1.setCellValue(record.getR23_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_approved_limit() != null) {
						cell2.setCellValue(record.getR23_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_amount_outstanding() != null) {
						cell3.setCellValue(record.getR23_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(23);
					if (row == null) {
						row = sheet.createRow(23);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
						cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
						cell2.setCellValue(record.getR24_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
						cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
						row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
						cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
						cell2.setCellValue(record.getR25_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
						cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
						row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
						cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
						cell2.setCellValue(record.getR26_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
						cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
						row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
						cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
						cell2.setCellValue(record.getR27_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
						cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
						row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
						cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
						cell2.setCellValue(record.getR28_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
						cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
						row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
						cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
						cell2.setCellValue(record.getR29_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
						cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
						row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
						cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
						cell2.setCellValue(record.getR30_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
						cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}

				int startRow1 = 35;
				// dataList1 for entity 2
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_LA3_Archival_Summary_Entity2 record1 = dataList1.get(i);

						System.out.println("rownumber = " + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ====== R37 ======
						row = sheet.getRow(36);
						if (row == null) {
							row = sheet.createRow(36);
						}
						Cell cell1 = row.createCell(1);
						if (record1.getR37_NO_OF_AC() != null) {
							cell1.setCellValue(record1.getR37_NO_OF_AC().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_LIMIT() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_LIMIT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record1.getR37_AMOUNT_OUTSTANDING() != null) {
							cell3.setCellValue(record1.getR37_AMOUNT_OUTSTANDING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// ====== R38 ======
						row = sheet.getRow(37);
						if (row == null) {
							row = sheet.createRow(37);
						}
						Cell R38cell1 = row.createCell(1);
						if (record1.getR38_NO_OF_AC() != null) {
							R38cell1.setCellValue(record1.getR38_NO_OF_AC().doubleValue());
							R38cell1.setCellStyle(numberStyle);
						} else {
							R38cell1.setCellValue("");
							R38cell1.setCellStyle(textStyle);
						}

						Cell R38cell2 = row.createCell(2);
						if (record1.getR38_CREDIT_LIMIT() != null) {
							R38cell2.setCellValue(record1.getR38_CREDIT_LIMIT().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(3);
						if (record1.getR38_AMOUNT_OUTSTANDING() != null) {
							R38cell3.setCellValue(record1.getR38_AMOUNT_OUTSTANDING().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ====== R40 ======
						row = sheet.getRow(39);
						if (row == null) {
							row = sheet.createRow(39);
						}
						Cell R40cell1 = row.createCell(1);
						if (record1.getR40_NO_OF_AC() != null) {
							R40cell1.setCellValue(record1.getR40_NO_OF_AC().doubleValue());
							R40cell1.setCellStyle(numberStyle);
						} else {
							R40cell1.setCellValue("");
							R40cell1.setCellStyle(textStyle);
						}

						Cell R40cell2 = row.createCell(2);
						if (record1.getR40_CREDIT_LIMIT() != null) {
							R40cell2.setCellValue(record1.getR40_CREDIT_LIMIT().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(3);
						if (record1.getR40_AMOUNT_OUTSTANDING() != null) {
							R40cell3.setCellValue(record1.getR40_AMOUNT_OUTSTANDING().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// ====== R41 ======
						row = sheet.getRow(40);
						if (row == null) {
							row = sheet.createRow(40);
						}
						Cell R41cell1 = row.createCell(1);
						if (record1.getR41_NO_OF_AC() != null) {
							R41cell1.setCellValue(record1.getR41_NO_OF_AC().doubleValue());
							R41cell1.setCellStyle(numberStyle);
						} else {
							R41cell1.setCellValue("");
							R41cell1.setCellStyle(textStyle);
						}

						Cell R41cell2 = row.createCell(2);
						if (record1.getR41_CREDIT_LIMIT() != null) {
							R41cell2.setCellValue(record1.getR41_CREDIT_LIMIT().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(3);
						if (record1.getR41_AMOUNT_OUTSTANDING() != null) {
							R41cell3.setCellValue(record1.getR41_AMOUNT_OUTSTANDING().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}
					}
				}

				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);

			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA3 ARCHIVAL SUMMARY", null,
						"BRRS_M_LA4_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_M_LA3 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MLA3Detail");

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

			// sanction style (right aligned with 3 decimals)
			CellStyle sanctionStyle = workbook.createCellStyle();
			sanctionStyle.setAlignment(HorizontalAlignment.RIGHT);
			sanctionStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###"));
			sanctionStyle.setBorderTop(border);
			sanctionStyle.setBorderBottom(border);
			sanctionStyle.setBorderLeft(border);
			sanctionStyle.setBorderRight(border);

			// Header row
			// Header row
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "APPROVED LIMIT", "REPORT LABEL",
					"REPORT LABEL_1", "REPORT ADDL CRITERIA 1", "REPORT ADDL CRITERIA 2", "REPORT ADDL CRITERIA 3",
					"REPORT_DATE" };

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
			List<M_LA3_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (M_LA3_Archival_Detail_Entity item : reportData) {
					XSSFRow row = sheet.createRow(rowIndex++);
					row.createCell(0).setCellValue(item.getCust_id());
					row.createCell(1).setCellValue(item.getAcct_number());
					row.createCell(2).setCellValue(item.getAcct_name());

					// ACCT BALANCE (right aligned, 3 decimal places)
					Cell balanceCell = row.createCell(3);
					if (item.getAcct_balance_in_pula() != null) {
						balanceCell.setCellValue(item.getAcct_balance_in_pula().doubleValue());
					} else {
						balanceCell.setCellValue(0.000);
					}
					balanceCell.setCellStyle(balanceStyle);

					// sanction (right aligned, 3 decimal places)
					Cell sanctionCell = row.createCell(4);
					if (item.getSanction_limit() != null) {
						sanctionCell.setCellValue(item.getSanction_limit().doubleValue());
					} else {
						sanctionCell.setCellValue(0.000);
					}
					sanctionCell.setCellStyle(sanctionStyle);

					row.createCell(5).setCellValue(item.getReport_label());
					row.createCell(6).setCellValue(item.getReport_label_1());
					row.createCell(7).setCellValue(item.getReport_addl_criteria_1());
					row.createCell(8).setCellValue(item.getReport_addl_criteria_2());
					row.createCell(9).setCellValue(item.getReport_addl_criteria_3());
					row.createCell(10)
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
				logger.info("No data found for BRRS_M_LA3 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating BRRS_M_LA3Excel", e);
			return new byte[0];
		}
	}

	@Transactional
	public void updateReport(M_LA3_Summary_Entity2 request) {

		try {

			logger.info("Came to services");
			logger.info("Report Date: {}", request.getREPORT_DATE());

			// Fetch existing record
			List<M_LA3_Summary_Entity2> records = getDataByDate2(request.getREPORT_DATE());

			if (records == null || records.isEmpty()) {

				throw new RuntimeException("Record not found for REPORT_DATE: " + request.getREPORT_DATE());
			}

			M_LA3_Summary_Entity2 existing = records.get(0);

			// Audit old copy
			M_LA3_Summary_Entity2 oldcopy = new M_LA3_Summary_Entity2();
			BeanUtils.copyProperties(existing, oldcopy);

			String changes = auditService.getChanges(oldcopy, request);

			if (!changes.isEmpty()) {

				String sql = "UPDATE BRRS_M_LA3_SUMMARYTABLE2 SET " +

						"R36_NO_OF_AC=?, R36_CREDIT_LIMIT=?, R36_AMOUNT_OUTSTANDING=?, " +

						"R37_NO_OF_AC=?, R37_CREDIT_LIMIT=?, R37_AMOUNT_OUTSTANDING=?, " +

						"R38_NO_OF_AC=?, R38_CREDIT_LIMIT=?, R38_AMOUNT_OUTSTANDING=?, " +

						"R39_NO_OF_AC=?, R39_CREDIT_LIMIT=?, R39_AMOUNT_OUTSTANDING=?, " +

						"R40_NO_OF_AC=?, R40_CREDIT_LIMIT=?, R40_AMOUNT_OUTSTANDING=?, " +

						"R41_NO_OF_AC=?, R41_CREDIT_LIMIT=?, R41_AMOUNT_OUTSTANDING=?, " +

						"R42_NO_OF_AC=?, R42_CREDIT_LIMIT=?, R42_AMOUNT_OUTSTANDING=? " +

						"WHERE REPORT_DATE=?";

				int count = jdbcTemplate.update(

						sql,

						request.getR36_NO_OF_AC(), request.getR36_CREDIT_LIMIT(), request.getR36_AMOUNT_OUTSTANDING(),

						request.getR37_NO_OF_AC(), request.getR37_CREDIT_LIMIT(), request.getR37_AMOUNT_OUTSTANDING(),

						request.getR38_NO_OF_AC(), request.getR38_CREDIT_LIMIT(), request.getR38_AMOUNT_OUTSTANDING(),

						request.getR39_NO_OF_AC(), request.getR39_CREDIT_LIMIT(), request.getR39_AMOUNT_OUTSTANDING(),

						request.getR40_NO_OF_AC(), request.getR40_CREDIT_LIMIT(), request.getR40_AMOUNT_OUTSTANDING(),

						request.getR41_NO_OF_AC(), request.getR41_CREDIT_LIMIT(), request.getR41_AMOUNT_OUTSTANDING(),

						request.getR42_NO_OF_AC(), request.getR42_CREDIT_LIMIT(), request.getR42_AMOUNT_OUTSTANDING(),

						request.getREPORT_DATE());

				if (count > 0) {

					auditService.compareEntitiesmanual(oldcopy, request, request.getREPORT_DATE().toString(),
							"M LA3 Summary Screen", "BRRS_M_LA3_SUMMARYTABLE2");

					logger.info("Audit completed for REPORT_DATE {}", request.getREPORT_DATE());

					logger.info("M LA3 Summary Updated Successfully. Rows Updated: {}", count);
				}

			} else {

				logger.info("No changes detected for REPORT_DATE {}", request.getREPORT_DATE());
			}

		} catch (Exception e) {

			logger.error("Error while updating BRRS_M_LA3 Report", e);

			throw new RuntimeException("Error while updating BRRS_M_LA3 Report", e);
		}
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {

	    ModelAndView mv = new ModelAndView("BRRS/M_LA3");

	    System.out.println("SNO : " + SNO);
	    System.out.println("Type : " + type);

	    if (SNO != null) {

	    	
	    	if (type == "RESUB" || type.equals("RESUB")) {

	            System.out.println("Inside RESUB FETCH");

	            M_LA3_Detail_Entity la3Entity = findBySnoArch(SNO);

	            if (la3Entity != null && la3Entity.getReport_date() != null) {
	                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
	                        .format(la3Entity.getReport_date());
	                mv.addObject("asondate", formattedDate);
	            }

	            mv.addObject("Data", la3Entity);

	        } else {

	            System.out.println("Inside CURRENT FETCH");

	            M_LA3_Detail_Entity la3Entity = findBySno(SNO);

	            if (la3Entity != null && la3Entity.getReport_date() != null) {
	                String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
	                        .format(la3Entity.getReport_date());
	                mv.addObject("asondate", formattedDate);
	            }

	            mv.addObject("Data", la3Entity);
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

	        String sno = request.getParameter("sno");
	        String acctName = request.getParameter("acct_name");
	        String acctBalanceInpula = request.getParameter("acct_balance_in_pula");
	        String sanctionLimit = request.getParameter("sanction_limit");
	        String reportDateStr = request.getParameter("report_date");

	        System.out.println("Sno is : " + sno);

	        String type = request.getParameter("type");
	        String entry = request.getParameter("entry") != null ? request.getParameter("entry") : "YES";

	        System.out.println("Type is : " + type);

	        M_LA3_Detail_Entity existing = null;

	        if ("RESUB".equals(type)) {
	            existing = findBySnoArch(sno);
	           
	        } else {
	        	 System.out.println("Sno is correct");
	            existing = findBySno(sno);
	        }
	        System.out.println("Sno is correct2");
	        if (existing == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Record not found for update.");
	        }

	        M_LA3_Detail_Entity oldcopy = new M_LA3_Detail_Entity();
	        BeanUtils.copyProperties(existing, oldcopy);

	        boolean isChanged = false;

	        // Account Name
	        if (acctName != null && !acctName.isEmpty()) {

	            if (existing.getAcct_name() == null ||
	                    !existing.getAcct_name().equals(acctName)) {

	                existing.setAcct_name(acctName);
	                isChanged = true;
	            }
	        }

	        // Account Balance
	        if (acctBalanceInpula != null && !acctBalanceInpula.isEmpty()) {

	            BigDecimal newBalance = new BigDecimal(acctBalanceInpula);

	            if (existing.getAcct_balance_in_pula() == null ||
	                    existing.getAcct_balance_in_pula().compareTo(newBalance) != 0) {

	                existing.setAcct_balance_in_pula(newBalance);
	                isChanged = true;
	            }
	        }

	        // Sanction Limit
	        if (sanctionLimit != null && !sanctionLimit.isEmpty()) {

	            BigDecimal newLimit = new BigDecimal(sanctionLimit);

	            if (existing.getSanction_limit() == null ||
	                    existing.getSanction_limit().compareTo(newLimit) != 0) {

	                existing.setSanction_limit(newLimit);
	                isChanged = true;
	            }
	        }

	        if (isChanged) {

	            String sql;

	            if ("RESUB".equals(type)) {

	                System.out.println("Inside RESUB UPDATE");

	                sql = "UPDATE BRRS_M_LA3_ARCHIVALTABLE_DETAIL "
	                        + "SET ACCT_NAME = ?, "
	                        + "ACCT_BALANCE_IN_PULA = ?, "
	                        + "SANCTION_LIMIT = ? "
	                        + "WHERE SNO = ?";

	            } else {

	                sql = "UPDATE BRRS_M_LA3_DETAILTABLE "
	                        + "SET ACCT_NAME = ?, "
	                        + "ACCT_BALANCE_IN_PULA = ?, "
	                        + "SANCTION_LIMIT = ? "
	                        + "WHERE SNO = ?";
	            }

	            jdbcTemplate.update(sql,
	                    existing.getAcct_name(),
	                    existing.getAcct_balance_in_pula(),
	                    existing.getSanction_limit(),
	                    sno);

	            if ("RESUB".equals(type)) {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        existing,
	                        sno,
	                        "M_LA3 Archival Screen",
	                        "BRRS_M_LA3_ARCHIVALTABLE_DETAIL");

	            } else {

	                auditService.compareEntitiesmanual(
	                        oldcopy,
	                        existing,
	                        sno,
	                        "M_LA3 Screen",
	                        "BRRS_M_LA3_DETAILTABLE");
	            }

	            System.out.println("Record updated using JDBC");

	            Run_M_LA3_Procedure(reportDateStr, type, entry);

	            if ("RESUB".equals(type) && "NO".equals(entry)) {
	                return ResponseEntity.ok("Record updated and Report Regenerated successfully!");
	            }

	            return ResponseEntity.ok("Record updated successfully!");
	        }

	        return ResponseEntity.ok("No changes were made.");

	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating record: " + e.getMessage());
	    }
	}

	@Transactional
	public ResponseEntity<?> callregenprocedure(HttpServletRequest request) {

		try {

			Run_M_LA3_Procedure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));

			return ResponseEntity.ok("Resubmitted successfully!");

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());
		}
	}

	private void Run_M_LA3_Procedure(String reportDateStr, String type, String entry) {

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

					/*-----------------------------------------
					 * Restore Detail Table
					 *-----------------------------------------*/
					if (isResubNoEntry) {

						String deleteSql = "DELETE FROM BRRS_M_LA3_DETAILTABLE " + "WHERE REPORT_DATE=?";

						int rowsDeleted = jdbcTemplate.update(deleteSql, formattedDate);

						System.out.println("Deleted " + rowsDeleted + " rows.");

						String transferSql = "INSERT INTO BRRS_M_LA3_DETAILTABLE ("
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, " + "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, " + "MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) " + "SELECT "
								+ "SNO, CUST_ID, ACCT_NUMBER, ACCT_NAME, " + "ACCT_BALANCE_IN_PULA, SANCTION_LIMIT, "
								+ "REPORT_LABEL, REPORT_ADDL_CRITERIA_1, " + "MODIFICATION_REMARKS, REPORT_REMARKS, "
								+ "REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_M_LA3_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE=?";

						int rowsInserted = jdbcTemplate.update(transferSql, formattedDate);

						System.out.println("Transferred " + rowsInserted + " rows.");
					}

					/*-----------------------------------------
					 * Execute Procedure
					 *-----------------------------------------*/
					if (shouldExecuteProcedure) {

						jdbcTemplate.update("BEGIN BRRS_M_LA3_SUMMARY_PROCEDURE(?); END;", formattedDate);

						System.out.println("Procedure executed.");
					}

					/*-----------------------------------------
					 * Archive Summary
					 *-----------------------------------------*/
					if (isResubNoEntry) {

					    String adsql = "DELETE FROM BRRS_M_LA3_DETAILTABLE WHERE REPORT_DATE = ?";
					    int rowsDeleted = jdbcTemplate.update(adsql, formattedDate);
					    System.out.println("Successfully deleted after executing procedure " + rowsDeleted + " rows.");

					    String ins_sum_sql =
					            "SELECT MAX(REPORT_VERSION) " +
					            "FROM BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 " +
					            "WHERE REPORT_DATE = ?";

					    Integer maxVersion =
					            jdbcTemplate.queryForObject(
					                    ins_sum_sql,
					                    Integer.class,
					                    formattedDate);

					    int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

					    StringBuilder columnsPart = new StringBuilder();

					    String[] tokens = {
					            "PRODUCT",
					            "NO_OF_AC",
					            "CREDIT_LIMIT",
					            "AMOUNT_OUTSTANDING"
					    };

					    // Generate R36 to R42 columns
					    for (int i = 10; i <= 42; i++) {
					        for (String token : tokens) {
					            columnsPart.append("R")
					                       .append(i)
					                       .append("_")
					                       .append(token)
					                       .append(", ");
					        }
					    }

					    String finalsql =
					            "INSERT INTO BRRS_M_LA3_ARCHIVALTABLE_SUMMARY2 ("
					            + columnsPart.toString()
					            + "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					            + "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE) "
					            + "SELECT "
					            + columnsPart.toString()
					            + "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, "
					            + "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
					            + "FROM BRRS_M_LA3_SUMMARYTABLE "
					            + "WHERE REPORT_DATE = ?";

					    int rowsInsertedSum =
					            jdbcTemplate.update(
					                    finalsql,
					                    highestValue,
					                    formattedDate);

					    System.out.println(
					            "Successfully transferred "
					            + rowsInsertedSum
					            + " rows.");

					    String adsumsql =
					            "DELETE FROM BRRS_M_LA3_SUMMARYTABLE WHERE REPORT_DATE = ?";

					    int rowsDeletedSum =
					            jdbcTemplate.update(
					                    adsumsql,
					                    formattedDate);

					    System.out.println(
					            "Deleted from summary "
					            + rowsDeletedSum
					            + " rows after transferring.");
					}
					} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
	}

	// Normal Email Excel
	public byte[] BRRS_M_LA3EmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return BRRS_M_LA3ArchivalEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<M_LA3_Summary_Entity1> dataList = getDataByDate1(dateformat.parse(todate));
		List<M_LA3_Summary_Entity2> dataList1 = getDataByDate2(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
			return new byte[0];
		}

		String templateDir = env.getProperty("output.exportpathtemp");
		String templateFileName = filename;
		System.out.println(filename);
		Path templatePath = Paths.get(templateDir, templateFileName);
		System.out.println(templatePath);

		logger.info("Service: Attempting to load template from path: {}", templatePath.toAbsolutePath());

		if (!Files.exists(templatePath)) {
			throw new FileNotFoundException("Template file not found at: " + templatePath.toAbsolutePath());
		}

		if (!Files.isReadable(templatePath)) {
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
					M_LA3_Summary_Entity1 record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(9);
					cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
						cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
						cell2.setCellValue(record.getR11_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
						cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
						cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
						cell2.setCellValue(record.getR12_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
						cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
						cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
						cell2.setCellValue(record.getR13_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
						cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
						cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
						cell2.setCellValue(record.getR14_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
						cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
						row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
						cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
						cell2.setCellValue(record.getR15_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
						cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
						row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
						cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
						cell2.setCellValue(record.getR21_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
						cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
						row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
						cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
						cell2.setCellValue(record.getR22_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
						cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(24);
					if (row == null) {
						row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
						cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
						cell2.setCellValue(record.getR24_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
						cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
						row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
						cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
						cell2.setCellValue(record.getR24_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
						cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
						row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
						cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
						cell2.setCellValue(record.getR25_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
						cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
						row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
						cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
						cell2.setCellValue(record.getR26_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
						cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
						row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
						cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
						cell2.setCellValue(record.getR27_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
						cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
						row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
						cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
						cell2.setCellValue(record.getR28_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
						cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
						row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
						cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
						cell2.setCellValue(record.getR29_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
						cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R31 ======
					row = sheet.getRow(30);
					if (row == null) {
						row = sheet.createRow(30);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
						cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
						cell2.setCellValue(record.getR30_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
						cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
				}

				int startRow1 = 37;
				// dataList1 for entity 2
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_LA3_Summary_Entity2 record1 = dataList1.get(i);

						System.out.println("rownumber = " + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ====== R38 ======
						row = sheet.getRow(37);
						if (row == null) {
							row = sheet.createRow(37);
						}
						Cell cell1 = row.createCell(1);
						if (record1.getR37_NO_OF_AC() != null) {
							cell1.setCellValue(record1.getR37_NO_OF_AC().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_LIMIT() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_LIMIT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record1.getR37_AMOUNT_OUTSTANDING() != null) {
							cell3.setCellValue(record1.getR37_AMOUNT_OUTSTANDING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// ====== R39 ======
						row = sheet.getRow(38);
						if (row == null) {
							row = sheet.createRow(38);
						}
						Cell R38cell1 = row.createCell(1);
						if (record1.getR38_NO_OF_AC() != null) {
							R38cell1.setCellValue(record1.getR38_NO_OF_AC().doubleValue());
							R38cell1.setCellStyle(numberStyle);
						} else {
							R38cell1.setCellValue("");
							R38cell1.setCellStyle(textStyle);
						}

						Cell R38cell2 = row.createCell(2);
						if (record1.getR38_CREDIT_LIMIT() != null) {
							R38cell2.setCellValue(record1.getR38_CREDIT_LIMIT().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(3);
						if (record1.getR38_AMOUNT_OUTSTANDING() != null) {
							R38cell3.setCellValue(record1.getR38_AMOUNT_OUTSTANDING().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ====== R41 ======
						row = sheet.getRow(40);
						if (row == null) {
							row = sheet.createRow(40);
						}
						Cell R40cell1 = row.createCell(1);
						if (record1.getR40_NO_OF_AC() != null) {
							R40cell1.setCellValue(record1.getR40_NO_OF_AC().doubleValue());
							R40cell1.setCellStyle(numberStyle);
						} else {
							R40cell1.setCellValue("");
							R40cell1.setCellStyle(textStyle);
						}

						Cell R40cell2 = row.createCell(2);
						if (record1.getR40_CREDIT_LIMIT() != null) {
							R40cell2.setCellValue(record1.getR40_CREDIT_LIMIT().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(3);
						if (record1.getR40_AMOUNT_OUTSTANDING() != null) {
							R40cell3.setCellValue(record1.getR40_AMOUNT_OUTSTANDING().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// ====== R42 ======
						row = sheet.getRow(41);
						if (row == null) {
							row = sheet.createRow(41);
						}
						Cell R41cell1 = row.createCell(1);
						if (record1.getR41_NO_OF_AC() != null) {
							R41cell1.setCellValue(record1.getR41_NO_OF_AC().doubleValue());
							R41cell1.setCellStyle(numberStyle);
						} else {
							R41cell1.setCellValue("");
							R41cell1.setCellStyle(textStyle);
						}

						Cell R41cell2 = row.createCell(2);
						if (record1.getR41_CREDIT_LIMIT() != null) {
							R41cell2.setCellValue(record1.getR41_CREDIT_LIMIT().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(3);
						if (record1.getR41_AMOUNT_OUTSTANDING() != null) {
							R41cell3.setCellValue(record1.getR41_AMOUNT_OUTSTANDING().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}
					}
				}
				workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
			} else {

			}
			// Write the final workbook content to the in-memory stream.
			workbook.write(out);
			logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
			// audit
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				HttpServletRequest request = attrs.getRequest();
				String userid = (String) request.getSession().getAttribute("USERID");
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_LA3_SUMMARY", null, "BRRS_M_LA3_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	// Archival Email Excel
	public byte[] BRRS_M_LA3ArchivalEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_LA3_Archival_Summary_Entity1> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);

		List<M_LA3_Archival_Summary_Entity2> dataList1 = getdatabydateListarchival2(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_LA3 report. Returning empty result.");
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
					M_LA3_Archival_Summary_Entity1 record = dataList.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// REPORT_DATE
					row = sheet.getRow(6);
					Cell cell1 = row.getCell(1);
					if (cell1 == null) {
						cell1 = row.createCell(1);
					}

					if (record.getREPORT_DATE() != null) {
						cell1.setCellValue(record.getREPORT_DATE()); // java.util.Date
						cell1.setCellStyle(dateStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// row12
					// Column B
					row = sheet.getRow(9);
					cell1 = row.createCell(1);
					if (record.getR10_no_of_ac() != null) {
						cell1.setCellValue(record.getR10_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR10_approved_limit() != null) {
						cell2.setCellValue(record.getR10_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR10_amount_outstanding() != null) {
						cell3.setCellValue(record.getR10_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R11 ======
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}
					cell1 = row.createCell(1);
					if (record.getR11_no_of_ac() != null) {
						cell1.setCellValue(record.getR11_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR11_approved_limit() != null) {
						cell2.setCellValue(record.getR11_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_amount_outstanding() != null) {
						cell3.setCellValue(record.getR11_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R12 ======
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}
					cell1 = row.createCell(1);
					if (record.getR12_no_of_ac() != null) {
						cell1.setCellValue(record.getR12_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR12_approved_limit() != null) {
						cell2.setCellValue(record.getR12_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_amount_outstanding() != null) {
						cell3.setCellValue(record.getR12_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R13 ======
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}
					cell1 = row.createCell(1);
					if (record.getR13_no_of_ac() != null) {
						cell1.setCellValue(record.getR13_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR13_approved_limit() != null) {
						cell2.setCellValue(record.getR13_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_amount_outstanding() != null) {
						cell3.setCellValue(record.getR13_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R14 ======
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}
					cell1 = row.createCell(1);
					if (record.getR14_no_of_ac() != null) {
						cell1.setCellValue(record.getR14_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR14_approved_limit() != null) {
						cell2.setCellValue(record.getR14_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_amount_outstanding() != null) {
						cell3.setCellValue(record.getR14_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R15 ======
					row = sheet.getRow(14);
					if (row == null) {
						row = sheet.createRow(14);
					}
					cell1 = row.createCell(1);
					if (record.getR15_no_of_ac() != null) {
						cell1.setCellValue(record.getR15_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR15_approved_limit() != null) {
						cell2.setCellValue(record.getR15_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_amount_outstanding() != null) {
						cell3.setCellValue(record.getR15_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R21 ======
					row = sheet.getRow(20);
					if (row == null) {
						row = sheet.createRow(20);
					}
					cell1 = row.createCell(1);
					if (record.getR21_no_of_ac() != null) {
						cell1.setCellValue(record.getR21_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR21_approved_limit() != null) {
						cell2.setCellValue(record.getR21_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR21_amount_outstanding() != null) {
						cell3.setCellValue(record.getR21_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R22 ======
					row = sheet.getRow(21);
					if (row == null) {
						row = sheet.createRow(21);
					}
					cell1 = row.createCell(1);
					if (record.getR22_no_of_ac() != null) {
						cell1.setCellValue(record.getR22_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR22_approved_limit() != null) {
						cell2.setCellValue(record.getR22_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR22_amount_outstanding() != null) {
						cell3.setCellValue(record.getR22_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R23 ======
					row = sheet.getRow(22);
					if (row == null) {
						row = sheet.createRow(22);
					}
					cell1 = row.createCell(1);
					if (record.getR23_no_of_ac() != null) {
						cell1.setCellValue(record.getR23_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR23_approved_limit() != null) {
						cell2.setCellValue(record.getR23_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR23_amount_outstanding() != null) {
						cell3.setCellValue(record.getR23_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R24 ======
					row = sheet.getRow(23);
					if (row == null) {
						row = sheet.createRow(23);
					}
					cell1 = row.createCell(1);
					if (record.getR24_no_of_ac() != null) {
						cell1.setCellValue(record.getR24_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR24_approved_limit() != null) {
						cell2.setCellValue(record.getR24_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR24_amount_outstanding() != null) {
						cell3.setCellValue(record.getR24_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R25 ======
					row = sheet.getRow(24);
					if (row == null) {
						row = sheet.createRow(24);
					}
					cell1 = row.createCell(1);
					if (record.getR25_no_of_ac() != null) {
						cell1.setCellValue(record.getR25_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR25_approved_limit() != null) {
						cell2.setCellValue(record.getR25_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR25_amount_outstanding() != null) {
						cell3.setCellValue(record.getR25_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R26 ======
					row = sheet.getRow(25);
					if (row == null) {
						row = sheet.createRow(25);
					}
					cell1 = row.createCell(1);
					if (record.getR26_no_of_ac() != null) {
						cell1.setCellValue(record.getR26_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR26_approved_limit() != null) {
						cell2.setCellValue(record.getR26_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR26_amount_outstanding() != null) {
						cell3.setCellValue(record.getR26_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R27 ======
					row = sheet.getRow(26);
					if (row == null) {
						row = sheet.createRow(26);
					}
					cell1 = row.createCell(1);
					if (record.getR27_no_of_ac() != null) {
						cell1.setCellValue(record.getR27_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR27_approved_limit() != null) {
						cell2.setCellValue(record.getR27_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR27_amount_outstanding() != null) {
						cell3.setCellValue(record.getR27_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R28 ======
					row = sheet.getRow(27);
					if (row == null) {
						row = sheet.createRow(27);
					}
					cell1 = row.createCell(1);
					if (record.getR28_no_of_ac() != null) {
						cell1.setCellValue(record.getR28_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR28_approved_limit() != null) {
						cell2.setCellValue(record.getR28_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR28_amount_outstanding() != null) {
						cell3.setCellValue(record.getR28_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R29 ======
					row = sheet.getRow(28);
					if (row == null) {
						row = sheet.createRow(28);
					}
					cell1 = row.createCell(1);
					if (record.getR29_no_of_ac() != null) {
						cell1.setCellValue(record.getR29_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR29_approved_limit() != null) {
						cell2.setCellValue(record.getR29_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR29_amount_outstanding() != null) {
						cell3.setCellValue(record.getR29_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// ====== R30 ======
					row = sheet.getRow(29);
					if (row == null) {
						row = sheet.createRow(29);
					}
					cell1 = row.createCell(1);
					if (record.getR30_no_of_ac() != null) {
						cell1.setCellValue(record.getR30_no_of_ac().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR30_approved_limit() != null) {
						cell2.setCellValue(record.getR30_approved_limit().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR30_amount_outstanding() != null) {
						cell3.setCellValue(record.getR30_amount_outstanding().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

				}

				int startRow1 = 37;
				// dataList1 for entity 2
				if (!dataList1.isEmpty()) {
					for (int i = 0; i < dataList1.size(); i++) {
						M_LA3_Archival_Summary_Entity2 record1 = dataList1.get(i);

						System.out.println("rownumber = " + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						// ====== R37 ======
						row = sheet.getRow(36);
						if (row == null) {
							row = sheet.createRow(36);
						}
						Cell cell1 = row.createCell(1);
						if (record1.getR37_NO_OF_AC() != null) {
							cell1.setCellValue(record1.getR37_NO_OF_AC().doubleValue());
							cell1.setCellStyle(numberStyle);
						} else {
							cell1.setCellValue("");
							cell1.setCellStyle(textStyle);
						}

						Cell cell2 = row.createCell(2);
						if (record1.getR37_CREDIT_LIMIT() != null) {
							cell2.setCellValue(record1.getR37_CREDIT_LIMIT().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record1.getR37_AMOUNT_OUTSTANDING() != null) {
							cell3.setCellValue(record1.getR37_AMOUNT_OUTSTANDING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						// ====== R38 ======
						row = sheet.getRow(37);
						if (row == null) {
							row = sheet.createRow(37);
						}
						Cell R38cell1 = row.createCell(1);
						if (record1.getR38_NO_OF_AC() != null) {
							R38cell1.setCellValue(record1.getR38_NO_OF_AC().doubleValue());
							R38cell1.setCellStyle(numberStyle);
						} else {
							R38cell1.setCellValue("");
							R38cell1.setCellStyle(textStyle);
						}

						Cell R38cell2 = row.createCell(2);
						if (record1.getR38_CREDIT_LIMIT() != null) {
							R38cell2.setCellValue(record1.getR38_CREDIT_LIMIT().doubleValue());
							R38cell2.setCellStyle(numberStyle);
						} else {
							R38cell2.setCellValue("");
							R38cell2.setCellStyle(textStyle);
						}

						Cell R38cell3 = row.createCell(3);
						if (record1.getR38_AMOUNT_OUTSTANDING() != null) {
							R38cell3.setCellValue(record1.getR38_AMOUNT_OUTSTANDING().doubleValue());
							R38cell3.setCellStyle(numberStyle);
						} else {
							R38cell3.setCellValue("");
							R38cell3.setCellStyle(textStyle);
						}

						// ====== R40 ======
						row = sheet.getRow(39);
						if (row == null) {
							row = sheet.createRow(39);
						}
						Cell R40cell1 = row.createCell(1);
						if (record1.getR40_NO_OF_AC() != null) {
							R40cell1.setCellValue(record1.getR40_NO_OF_AC().doubleValue());
							R40cell1.setCellStyle(numberStyle);
						} else {
							R40cell1.setCellValue("");
							R40cell1.setCellStyle(textStyle);
						}

						Cell R40cell2 = row.createCell(2);
						if (record1.getR40_CREDIT_LIMIT() != null) {
							R40cell2.setCellValue(record1.getR40_CREDIT_LIMIT().doubleValue());
							R40cell2.setCellStyle(numberStyle);
						} else {
							R40cell2.setCellValue("");
							R40cell2.setCellStyle(textStyle);
						}

						Cell R40cell3 = row.createCell(3);
						if (record1.getR40_AMOUNT_OUTSTANDING() != null) {
							R40cell3.setCellValue(record1.getR40_AMOUNT_OUTSTANDING().doubleValue());
							R40cell3.setCellStyle(numberStyle);
						} else {
							R40cell3.setCellValue("");
							R40cell3.setCellStyle(textStyle);
						}

						// ====== R41 ======
						row = sheet.getRow(40);
						if (row == null) {
							row = sheet.createRow(40);
						}
						Cell R41cell1 = row.createCell(1);
						if (record1.getR41_NO_OF_AC() != null) {
							R41cell1.setCellValue(record1.getR41_NO_OF_AC().doubleValue());
							R41cell1.setCellStyle(numberStyle);
						} else {
							R41cell1.setCellValue("");
							R41cell1.setCellStyle(textStyle);
						}

						Cell R41cell2 = row.createCell(2);
						if (record1.getR41_CREDIT_LIMIT() != null) {
							R41cell2.setCellValue(record1.getR41_CREDIT_LIMIT().doubleValue());
							R41cell2.setCellStyle(numberStyle);
						} else {
							R41cell2.setCellValue("");
							R41cell2.setCellStyle(textStyle);
						}

						Cell R41cell3 = row.createCell(3);
						if (record1.getR41_AMOUNT_OUTSTANDING() != null) {
							R41cell3.setCellValue(record1.getR41_AMOUNT_OUTSTANDING().doubleValue());
							R41cell3.setCellStyle(numberStyle);
						} else {
							R41cell3.setCellValue("");
							R41cell3.setCellStyle(textStyle);
						}
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
}
