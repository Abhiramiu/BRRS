package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
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
import java.util.Locale;
import java.util.Optional;

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

@Service

public class BRRS_M_SRWA_12E_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_M_SRWA_12E_ReportService.class);

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

	@Autowired
	private JdbcTemplate jdbcTemplate;

//==================================
//JDBC REPOSITORIES
//==================================

//====================================
//BRRS_M_SRWA_12E_LTV_Summary_Repo
//====================================

//✅ Fetch record(s) by specific REPORT_DATE
	public List<M_SRWA_12E_LTV_Summary_Entity> getSummaryDataByDate(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_SUMMARYTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new M_SRWA_12E_LTV_Summary_EntityRowMapper());
	}

//====================================
//BRRS_M_SRWA_12E_LTV_Detail_Repo
//====================================

//✅ Fetch record(s) by specific REPORT_DATE
	public List<M_SRWA_12E_LTV_Detail_Entity> getDetailDataByDate(Date rpt_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_DETAILTABLE WHERE TRUNC(REPORT_DATE) = TRUNC(?)";
		return jdbcTemplate.query(sql, new Object[] { rpt_date }, new M_SRWA_12E_LTV_Detail_EntityRowMapper());
	}

//====================================
//BRRS_M_SRWA_12E_LTV_Archival_Summary_Repo
//====================================

//✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getArchivalSummaryReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//✅ Fetch archival summary records by date and version
	public List<M_SRWA_12E_LTV_Archival_Summary_Entity> getArchivalSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12E_LTV_Archival_Summary_EntityRowMapper());
	}

//✅ Get all records with version (ordered by REPORT_VERSION ASC)
	public List<M_SRWA_12E_LTV_Archival_Summary_Entity> getArchivalSummaryDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_SRWA_12E_LTV_Archival_Summary_EntityRowMapper());
	}

//✅ Find max version for a given date
	public BigDecimal findMaxArchivalSummaryVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//====================================
//BRRS_M_SRWA_12E_LTV_Archival_Detail_Repo
//====================================

//✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getArchivalDetailReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//✅ Fetch archival detail records by date and version
	public List<M_SRWA_12E_LTV_Archival_Detail_Entity> getArchivalDetailDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12E_LTV_Archival_Detail_EntityRowMapper());
	}

//✅ Get all records with version (ordered by REPORT_VERSION ASC)
	public List<M_SRWA_12E_LTV_Archival_Detail_Entity> getArchivalDetailDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_SRWA_12E_LTV_Archival_Detail_EntityRowMapper());
	}

//✅ Find max version for a given date
	public BigDecimal findMaxArchivalDetailVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//====================================
//BRRS_M_SRWA_12E_LTV_Resub_Summary_Repo
//====================================

//✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getResubSummaryReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//✅ Fetch RESUB summary records by date and version
	public List<M_SRWA_12E_LTV_Resub_Summary_Entity> getResubSummaryDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12E_LTV_Resub_Summary_EntityRowMapper());
	}

//✅ Get all records with version (ordered by REPORT_VERSION ASC)
	public List<M_SRWA_12E_LTV_Resub_Summary_Entity> getResubSummaryDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_SRWA_12E_LTV_Resub_Summary_EntityRowMapper());
	}

//✅ Find max version for a given date
	public BigDecimal findMaxResubSummaryVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

//====================================
//BRRS_M_SRWA_12E_LTV_Resub_Detail_Repo
//====================================

//✅ Get REPORT_DATE and REPORT_VERSION ordered by REPORT_VERSION
	public List<Object[]> getResubDetailReportDateAndVersion() {
		String sql = "SELECT REPORT_DATE, REPORT_VERSION FROM BRRS_M_SRWA_12E_RESUB_DETAILTABLE ORDER BY REPORT_VERSION";
		return jdbcTemplate.query(sql, new Object[] {},
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//✅ Fetch RESUB detail records by date and version
	public List<M_SRWA_12E_LTV_Resub_Detail_Entity> getResubDetailDataByDateAndVersion(Date report_date,
			BigDecimal report_version) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		return jdbcTemplate.query(sql, new Object[] { report_date, report_version },
				new M_SRWA_12E_LTV_Resub_Detail_EntityRowMapper());
	}

//✅ Get all records with version (ordered by REPORT_VERSION ASC)
	public List<M_SRWA_12E_LTV_Resub_Detail_Entity> getResubDetailDataWithVersionAll() {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_RESUB_DETAILTABLE WHERE REPORT_VERSION IS NOT NULL ORDER BY REPORT_VERSION ASC";
		return jdbcTemplate.query(sql, new M_SRWA_12E_LTV_Resub_Detail_EntityRowMapper());
	}

//✅ Find max version for a given date
	public BigDecimal findMaxResubDetailVersion(Date date) {
		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_M_SRWA_12E_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { date }, BigDecimal.class);
	}

	// ==============================
	// Get Latest Available Date
	// ==============================

	public Date getLatestReportDate() {
		String sql = "SELECT MAX(REPORT_DATE) FROM BRRS_M_SRWA_12E_SUMMARYTABLE";
		try {
			return jdbcTemplate.queryForObject(sql, Date.class);
		} catch (Exception e) {
			logger.warn("No data found in SUMMARYTABLE");
			return null;
		}
	}

	// ==============================
	// Get Latest Archival Summary Version by Date
	// ==============================

	public Optional<M_SRWA_12E_LTV_Archival_Summary_Entity> getLatestArchivalSummaryVersionByDate(Date report_date) {
		String sql = "SELECT * FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION IS NOT NULL ORDER BY TO_NUMBER(REPORT_VERSION) DESC FETCH FIRST 1 ROWS ONLY";
		List<M_SRWA_12E_LTV_Archival_Summary_Entity> results = jdbcTemplate.query(sql, new Object[] { report_date },
				new M_SRWA_12E_LTV_Archival_Summary_EntityRowMapper());
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

//======================
//ENTITY'S
//======================

//====================================
//M_SRWA_12E_LTV_Summary_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Summary_EntityRowMapper implements RowMapper<M_SRWA_12E_LTV_Summary_Entity> {
		@Override
		public M_SRWA_12E_LTV_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Summary_Entity obj = new M_SRWA_12E_LTV_Summary_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

//====================================
//M_SRWA_12E_LTV_Detail_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Detail_EntityRowMapper implements RowMapper<M_SRWA_12E_LTV_Detail_Entity> {
		@Override
		public M_SRWA_12E_LTV_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Detail_Entity obj = new M_SRWA_12E_LTV_Detail_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

//====================================
//M_SRWA_12E_LTV_Archival_Summary_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Archival_Summary_EntityRowMapper
			implements RowMapper<M_SRWA_12E_LTV_Archival_Summary_Entity> {
		@Override
		public M_SRWA_12E_LTV_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Archival_Summary_Entity obj = new M_SRWA_12E_LTV_Archival_Summary_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Archival_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Archival_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

//====================================
//M_SRWA_12E_LTV_Archival_Detail_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Archival_Detail_EntityRowMapper
			implements RowMapper<M_SRWA_12E_LTV_Archival_Detail_Entity> {
		@Override
		public M_SRWA_12E_LTV_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Archival_Detail_Entity obj = new M_SRWA_12E_LTV_Archival_Detail_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Archival_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Archival_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

//====================================
//M_SRWA_12E_LTV_Resub_Summary_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Resub_Summary_EntityRowMapper
			implements RowMapper<M_SRWA_12E_LTV_Resub_Summary_Entity> {
		@Override
		public M_SRWA_12E_LTV_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Resub_Summary_Entity obj = new M_SRWA_12E_LTV_Resub_Summary_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Resub_Summary_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Resub_Summary_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

//====================================
//M_SRWA_12E_LTV_Resub_Detail_Entity - ROW MAPPER
//====================================

	public class M_SRWA_12E_LTV_Resub_Detail_EntityRowMapper implements RowMapper<M_SRWA_12E_LTV_Resub_Detail_Entity> {
		@Override
		public M_SRWA_12E_LTV_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			M_SRWA_12E_LTV_Resub_Detail_Entity obj = new M_SRWA_12E_LTV_Resub_Detail_Entity();

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getTimestamp("report_resubdate"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			// =========================
			// R13
			// =========================
			obj.setR13_PRODUCT(rs.getString("R13_PRODUCT"));
			obj.setR13_PERFORMING_EXPOSURE(rs.getBigDecimal("R13_PERFORMING_EXPOSURE"));
			obj.setR13_NON_PERFORMING(rs.getBigDecimal("R13_NON_PERFORMING"));
			obj.setR13_SPECIFIC_PROV(rs.getBigDecimal("R13_SPECIFIC_PROV"));
			obj.setR13_UNSECURED_PORTION_NPL(rs.getBigDecimal("R13_UNSECURED_PORTION_NPL"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// =========================
			// R14
			// =========================
			obj.setR14_PRODUCT(rs.getString("R14_PRODUCT"));
			obj.setR14_PERFORMING_EXPOSURE(rs.getBigDecimal("R14_PERFORMING_EXPOSURE"));
			obj.setR14_NON_PERFORMING(rs.getBigDecimal("R14_NON_PERFORMING"));
			obj.setR14_SPECIFIC_PROV(rs.getBigDecimal("R14_SPECIFIC_PROV"));
			obj.setR14_UNSECURED_PORTION_NPL(rs.getBigDecimal("R14_UNSECURED_PORTION_NPL"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// =========================
			// R15
			// =========================
			obj.setR15_PRODUCT(rs.getString("R15_PRODUCT"));
			obj.setR15_PERFORMING_EXPOSURE(rs.getBigDecimal("R15_PERFORMING_EXPOSURE"));
			obj.setR15_NON_PERFORMING(rs.getBigDecimal("R15_NON_PERFORMING"));
			obj.setR15_SPECIFIC_PROV(rs.getBigDecimal("R15_SPECIFIC_PROV"));
			obj.setR15_UNSECURED_PORTION_NPL(rs.getBigDecimal("R15_UNSECURED_PORTION_NPL"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// =========================
			// R16
			// =========================
			obj.setR16_PRODUCT(rs.getString("R16_PRODUCT"));
			obj.setR16_PERFORMING_EXPOSURE(rs.getBigDecimal("R16_PERFORMING_EXPOSURE"));
			obj.setR16_NON_PERFORMING(rs.getBigDecimal("R16_NON_PERFORMING"));
			obj.setR16_SPECIFIC_PROV(rs.getBigDecimal("R16_SPECIFIC_PROV"));
			obj.setR16_UNSECURED_PORTION_NPL(rs.getBigDecimal("R16_UNSECURED_PORTION_NPL"));
			obj.setR16_TOTAL(rs.getBigDecimal("R16_TOTAL"));

			// =========================
			// R17
			// =========================
			obj.setR17_PRODUCT(rs.getString("R17_PRODUCT"));
			obj.setR17_PERFORMING_EXPOSURE(rs.getBigDecimal("R17_PERFORMING_EXPOSURE"));
			obj.setR17_NON_PERFORMING(rs.getBigDecimal("R17_NON_PERFORMING"));
			obj.setR17_SPECIFIC_PROV(rs.getBigDecimal("R17_SPECIFIC_PROV"));
			obj.setR17_UNSECURED_PORTION_NPL(rs.getBigDecimal("R17_UNSECURED_PORTION_NPL"));
			obj.setR17_TOTAL(rs.getBigDecimal("R17_TOTAL"));

			// =========================
			// R18
			// =========================
			obj.setR18_PRODUCT(rs.getString("R18_PRODUCT"));
			obj.setR18_PERFORMING_EXPOSURE(rs.getBigDecimal("R18_PERFORMING_EXPOSURE"));
			obj.setR18_NON_PERFORMING(rs.getBigDecimal("R18_NON_PERFORMING"));
			obj.setR18_SPECIFIC_PROV(rs.getBigDecimal("R18_SPECIFIC_PROV"));
			obj.setR18_UNSECURED_PORTION_NPL(rs.getBigDecimal("R18_UNSECURED_PORTION_NPL"));
			obj.setR18_TOTAL(rs.getBigDecimal("R18_TOTAL"));

			// =========================
			// R19
			// =========================
			obj.setR19_PRODUCT(rs.getString("R19_PRODUCT"));
			obj.setR19_PERFORMING_EXPOSURE(rs.getBigDecimal("R19_PERFORMING_EXPOSURE"));
			obj.setR19_NON_PERFORMING(rs.getBigDecimal("R19_NON_PERFORMING"));
			obj.setR19_SPECIFIC_PROV(rs.getBigDecimal("R19_SPECIFIC_PROV"));
			obj.setR19_UNSECURED_PORTION_NPL(rs.getBigDecimal("R19_UNSECURED_PORTION_NPL"));
			obj.setR19_TOTAL(rs.getBigDecimal("R19_TOTAL"));

			return obj;
		}
	}

	public class M_SRWA_12E_LTV_Resub_Detail_Entity {

		// =========================
		// COMMON FIELDS
		// =========================
		@Id
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		private Date report_date;

		@Id
		private BigDecimal report_version;
		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// =========================
		// R13 FIELDS
		// =========================
		private String R13_PRODUCT;
		private BigDecimal R13_PERFORMING_EXPOSURE;
		private BigDecimal R13_NON_PERFORMING;
		private BigDecimal R13_SPECIFIC_PROV;
		private BigDecimal R13_UNSECURED_PORTION_NPL;
		private BigDecimal R13_TOTAL;

		// =========================
		// R14 FIELDS
		// =========================
		private String R14_PRODUCT;
		private BigDecimal R14_PERFORMING_EXPOSURE;
		private BigDecimal R14_NON_PERFORMING;
		private BigDecimal R14_SPECIFIC_PROV;
		private BigDecimal R14_UNSECURED_PORTION_NPL;
		private BigDecimal R14_TOTAL;

		// =========================
		// R15 FIELDS
		// =========================
		private String R15_PRODUCT;
		private BigDecimal R15_PERFORMING_EXPOSURE;
		private BigDecimal R15_NON_PERFORMING;
		private BigDecimal R15_SPECIFIC_PROV;
		private BigDecimal R15_UNSECURED_PORTION_NPL;
		private BigDecimal R15_TOTAL;

		// =========================
		// R16 FIELDS
		// =========================
		private String R16_PRODUCT;
		private BigDecimal R16_PERFORMING_EXPOSURE;
		private BigDecimal R16_NON_PERFORMING;
		private BigDecimal R16_SPECIFIC_PROV;
		private BigDecimal R16_UNSECURED_PORTION_NPL;
		private BigDecimal R16_TOTAL;

		// =========================
		// R17 FIELDS
		// =========================
		private String R17_PRODUCT;
		private BigDecimal R17_PERFORMING_EXPOSURE;
		private BigDecimal R17_NON_PERFORMING;
		private BigDecimal R17_SPECIFIC_PROV;
		private BigDecimal R17_UNSECURED_PORTION_NPL;
		private BigDecimal R17_TOTAL;

		// =========================
		// R18 FIELDS
		// =========================
		private String R18_PRODUCT;
		private BigDecimal R18_PERFORMING_EXPOSURE;
		private BigDecimal R18_NON_PERFORMING;
		private BigDecimal R18_SPECIFIC_PROV;
		private BigDecimal R18_UNSECURED_PORTION_NPL;
		private BigDecimal R18_TOTAL;

		// =========================
		// R19 FIELDS
		// =========================
		private String R19_PRODUCT;
		private BigDecimal R19_PERFORMING_EXPOSURE;
		private BigDecimal R19_NON_PERFORMING;
		private BigDecimal R19_SPECIFIC_PROV;
		private BigDecimal R19_UNSECURED_PORTION_NPL;
		private BigDecimal R19_TOTAL;

		// =========================
		// CONSTRUCTOR
		// =========================
		public M_SRWA_12E_LTV_Resub_Detail_Entity() {
			super();
		}

		// =========================
		// GETTERS AND SETTERS
		// =========================

		// COMMON FIELDS
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
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

		// R13
		public String getR13_PRODUCT() {
			return R13_PRODUCT;
		}

		public void setR13_PRODUCT(String R13_PRODUCT) {
			this.R13_PRODUCT = R13_PRODUCT;
		}

		public BigDecimal getR13_PERFORMING_EXPOSURE() {
			return R13_PERFORMING_EXPOSURE;
		}

		public void setR13_PERFORMING_EXPOSURE(BigDecimal R13_PERFORMING_EXPOSURE) {
			this.R13_PERFORMING_EXPOSURE = R13_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR13_NON_PERFORMING() {
			return R13_NON_PERFORMING;
		}

		public void setR13_NON_PERFORMING(BigDecimal R13_NON_PERFORMING) {
			this.R13_NON_PERFORMING = R13_NON_PERFORMING;
		}

		public BigDecimal getR13_SPECIFIC_PROV() {
			return R13_SPECIFIC_PROV;
		}

		public void setR13_SPECIFIC_PROV(BigDecimal R13_SPECIFIC_PROV) {
			this.R13_SPECIFIC_PROV = R13_SPECIFIC_PROV;
		}

		public BigDecimal getR13_UNSECURED_PORTION_NPL() {
			return R13_UNSECURED_PORTION_NPL;
		}

		public void setR13_UNSECURED_PORTION_NPL(BigDecimal R13_UNSECURED_PORTION_NPL) {
			this.R13_UNSECURED_PORTION_NPL = R13_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal R13_TOTAL) {
			this.R13_TOTAL = R13_TOTAL;
		}

		// R14
		public String getR14_PRODUCT() {
			return R14_PRODUCT;
		}

		public void setR14_PRODUCT(String R14_PRODUCT) {
			this.R14_PRODUCT = R14_PRODUCT;
		}

		public BigDecimal getR14_PERFORMING_EXPOSURE() {
			return R14_PERFORMING_EXPOSURE;
		}

		public void setR14_PERFORMING_EXPOSURE(BigDecimal R14_PERFORMING_EXPOSURE) {
			this.R14_PERFORMING_EXPOSURE = R14_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR14_NON_PERFORMING() {
			return R14_NON_PERFORMING;
		}

		public void setR14_NON_PERFORMING(BigDecimal R14_NON_PERFORMING) {
			this.R14_NON_PERFORMING = R14_NON_PERFORMING;
		}

		public BigDecimal getR14_SPECIFIC_PROV() {
			return R14_SPECIFIC_PROV;
		}

		public void setR14_SPECIFIC_PROV(BigDecimal R14_SPECIFIC_PROV) {
			this.R14_SPECIFIC_PROV = R14_SPECIFIC_PROV;
		}

		public BigDecimal getR14_UNSECURED_PORTION_NPL() {
			return R14_UNSECURED_PORTION_NPL;
		}

		public void setR14_UNSECURED_PORTION_NPL(BigDecimal R14_UNSECURED_PORTION_NPL) {
			this.R14_UNSECURED_PORTION_NPL = R14_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal R14_TOTAL) {
			this.R14_TOTAL = R14_TOTAL;
		}

		// R15
		public String getR15_PRODUCT() {
			return R15_PRODUCT;
		}

		public void setR15_PRODUCT(String R15_PRODUCT) {
			this.R15_PRODUCT = R15_PRODUCT;
		}

		public BigDecimal getR15_PERFORMING_EXPOSURE() {
			return R15_PERFORMING_EXPOSURE;
		}

		public void setR15_PERFORMING_EXPOSURE(BigDecimal R15_PERFORMING_EXPOSURE) {
			this.R15_PERFORMING_EXPOSURE = R15_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR15_NON_PERFORMING() {
			return R15_NON_PERFORMING;
		}

		public void setR15_NON_PERFORMING(BigDecimal R15_NON_PERFORMING) {
			this.R15_NON_PERFORMING = R15_NON_PERFORMING;
		}

		public BigDecimal getR15_SPECIFIC_PROV() {
			return R15_SPECIFIC_PROV;
		}

		public void setR15_SPECIFIC_PROV(BigDecimal R15_SPECIFIC_PROV) {
			this.R15_SPECIFIC_PROV = R15_SPECIFIC_PROV;
		}

		public BigDecimal getR15_UNSECURED_PORTION_NPL() {
			return R15_UNSECURED_PORTION_NPL;
		}

		public void setR15_UNSECURED_PORTION_NPL(BigDecimal R15_UNSECURED_PORTION_NPL) {
			this.R15_UNSECURED_PORTION_NPL = R15_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal R15_TOTAL) {
			this.R15_TOTAL = R15_TOTAL;
		}

		// R16
		public String getR16_PRODUCT() {
			return R16_PRODUCT;
		}

		public void setR16_PRODUCT(String R16_PRODUCT) {
			this.R16_PRODUCT = R16_PRODUCT;
		}

		public BigDecimal getR16_PERFORMING_EXPOSURE() {
			return R16_PERFORMING_EXPOSURE;
		}

		public void setR16_PERFORMING_EXPOSURE(BigDecimal R16_PERFORMING_EXPOSURE) {
			this.R16_PERFORMING_EXPOSURE = R16_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR16_NON_PERFORMING() {
			return R16_NON_PERFORMING;
		}

		public void setR16_NON_PERFORMING(BigDecimal R16_NON_PERFORMING) {
			this.R16_NON_PERFORMING = R16_NON_PERFORMING;
		}

		public BigDecimal getR16_SPECIFIC_PROV() {
			return R16_SPECIFIC_PROV;
		}

		public void setR16_SPECIFIC_PROV(BigDecimal R16_SPECIFIC_PROV) {
			this.R16_SPECIFIC_PROV = R16_SPECIFIC_PROV;
		}

		public BigDecimal getR16_UNSECURED_PORTION_NPL() {
			return R16_UNSECURED_PORTION_NPL;
		}

		public void setR16_UNSECURED_PORTION_NPL(BigDecimal R16_UNSECURED_PORTION_NPL) {
			this.R16_UNSECURED_PORTION_NPL = R16_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR16_TOTAL() {
			return R16_TOTAL;
		}

		public void setR16_TOTAL(BigDecimal R16_TOTAL) {
			this.R16_TOTAL = R16_TOTAL;
		}

		// R17
		public String getR17_PRODUCT() {
			return R17_PRODUCT;
		}

		public void setR17_PRODUCT(String R17_PRODUCT) {
			this.R17_PRODUCT = R17_PRODUCT;
		}

		public BigDecimal getR17_PERFORMING_EXPOSURE() {
			return R17_PERFORMING_EXPOSURE;
		}

		public void setR17_PERFORMING_EXPOSURE(BigDecimal R17_PERFORMING_EXPOSURE) {
			this.R17_PERFORMING_EXPOSURE = R17_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR17_NON_PERFORMING() {
			return R17_NON_PERFORMING;
		}

		public void setR17_NON_PERFORMING(BigDecimal R17_NON_PERFORMING) {
			this.R17_NON_PERFORMING = R17_NON_PERFORMING;
		}

		public BigDecimal getR17_SPECIFIC_PROV() {
			return R17_SPECIFIC_PROV;
		}

		public void setR17_SPECIFIC_PROV(BigDecimal R17_SPECIFIC_PROV) {
			this.R17_SPECIFIC_PROV = R17_SPECIFIC_PROV;
		}

		public BigDecimal getR17_UNSECURED_PORTION_NPL() {
			return R17_UNSECURED_PORTION_NPL;
		}

		public void setR17_UNSECURED_PORTION_NPL(BigDecimal R17_UNSECURED_PORTION_NPL) {
			this.R17_UNSECURED_PORTION_NPL = R17_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR17_TOTAL() {
			return R17_TOTAL;
		}

		public void setR17_TOTAL(BigDecimal R17_TOTAL) {
			this.R17_TOTAL = R17_TOTAL;
		}

		// R18
		public String getR18_PRODUCT() {
			return R18_PRODUCT;
		}

		public void setR18_PRODUCT(String R18_PRODUCT) {
			this.R18_PRODUCT = R18_PRODUCT;
		}

		public BigDecimal getR18_PERFORMING_EXPOSURE() {
			return R18_PERFORMING_EXPOSURE;
		}

		public void setR18_PERFORMING_EXPOSURE(BigDecimal R18_PERFORMING_EXPOSURE) {
			this.R18_PERFORMING_EXPOSURE = R18_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR18_NON_PERFORMING() {
			return R18_NON_PERFORMING;
		}

		public void setR18_NON_PERFORMING(BigDecimal R18_NON_PERFORMING) {
			this.R18_NON_PERFORMING = R18_NON_PERFORMING;
		}

		public BigDecimal getR18_SPECIFIC_PROV() {
			return R18_SPECIFIC_PROV;
		}

		public void setR18_SPECIFIC_PROV(BigDecimal R18_SPECIFIC_PROV) {
			this.R18_SPECIFIC_PROV = R18_SPECIFIC_PROV;
		}

		public BigDecimal getR18_UNSECURED_PORTION_NPL() {
			return R18_UNSECURED_PORTION_NPL;
		}

		public void setR18_UNSECURED_PORTION_NPL(BigDecimal R18_UNSECURED_PORTION_NPL) {
			this.R18_UNSECURED_PORTION_NPL = R18_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR18_TOTAL() {
			return R18_TOTAL;
		}

		public void setR18_TOTAL(BigDecimal R18_TOTAL) {
			this.R18_TOTAL = R18_TOTAL;
		}

		// R19
		public String getR19_PRODUCT() {
			return R19_PRODUCT;
		}

		public void setR19_PRODUCT(String R19_PRODUCT) {
			this.R19_PRODUCT = R19_PRODUCT;
		}

		public BigDecimal getR19_PERFORMING_EXPOSURE() {
			return R19_PERFORMING_EXPOSURE;
		}

		public void setR19_PERFORMING_EXPOSURE(BigDecimal R19_PERFORMING_EXPOSURE) {
			this.R19_PERFORMING_EXPOSURE = R19_PERFORMING_EXPOSURE;
		}

		public BigDecimal getR19_NON_PERFORMING() {
			return R19_NON_PERFORMING;
		}

		public void setR19_NON_PERFORMING(BigDecimal R19_NON_PERFORMING) {
			this.R19_NON_PERFORMING = R19_NON_PERFORMING;
		}

		public BigDecimal getR19_SPECIFIC_PROV() {
			return R19_SPECIFIC_PROV;
		}

		public void setR19_SPECIFIC_PROV(BigDecimal R19_SPECIFIC_PROV) {
			this.R19_SPECIFIC_PROV = R19_SPECIFIC_PROV;
		}

		public BigDecimal getR19_UNSECURED_PORTION_NPL() {
			return R19_UNSECURED_PORTION_NPL;
		}

		public void setR19_UNSECURED_PORTION_NPL(BigDecimal R19_UNSECURED_PORTION_NPL) {
			this.R19_UNSECURED_PORTION_NPL = R19_UNSECURED_PORTION_NPL;
		}

		public BigDecimal getR19_TOTAL() {
			return R19_TOTAL;
		}

		public void setR19_TOTAL(BigDecimal R19_TOTAL) {
			this.R19_TOTAL = R19_TOTAL;
		}
	}

	// ===================
	// MODEL AND VIEW
	// ===================

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_M_SRWA_12E_LTVView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("dtltype...." + dtltype);
		System.out.println("type...." + type);

		try {

			// Parse only once
			Date d1 = dateformat.parse(todate);

			System.out.println("======= VIEW SCREEN =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + d1);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12E_LTV_Archival_Summary_Entity> T1Master = getArchivalSummaryDataByDateAndVersion(d1,
						version);
				mv.addObject("displaymode", "summary");

				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {
				List<M_SRWA_12E_LTV_Resub_Summary_Entity> T1Master = getResubSummaryDataByDateAndVersion(d1, version);

				mv.addObject("displaymode", "resubSummary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 3: NORMAL ----------
			else {
				List<M_SRWA_12E_LTV_Summary_Entity> T1Master = getSummaryDataByDate(d1);
				System.out.println("T1Master Size " + T1Master.size());
				mv.addObject("displaymode", "summary");
				mv.addObject("reportsummary", T1Master);
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12E_LTV_Archival_Detail_Entity> T1Master = getArchivalDetailDataByDateAndVersion(d1,
							version);
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<M_SRWA_12E_LTV_Resub_Detail_Entity> T1Master = getResubDetailDataByDateAndVersion(d1, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
				}
				// DETAIL + NORMAL
				else {

					List<M_SRWA_12E_LTV_Detail_Entity> T1Master = getDetailDataByDate(dateformat.parse(todate));
					System.out.println("Details......T1Master Size " + T1Master.size());
					mv.addObject("displaymode", "detail");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/M_SRWA_12E_LTV");
		System.out.println("View set to: " + mv.getViewName());
		return mv;
	}

	// ========================================
	// UPDATE REPORT METHOD - JDBC VERSION
	// ========================================

	@Transactional
	public void updateReport(M_SRWA_12E_LTV_Summary_Entity updatedEntity) {
		System.out.println("Came to services 1");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		Date reportDate = updatedEntity.getReport_date();

		// 🔹 Fetch existing SUMMARY using JDBC
		List<M_SRWA_12E_LTV_Summary_Entity> existingSummaryList = getSummaryDataByDate(reportDate);
		M_SRWA_12E_LTV_Summary_Entity existingSummary;
		M_SRWA_12E_LTV_Summary_Entity oldcopy = new M_SRWA_12E_LTV_Summary_Entity();

		if (!existingSummaryList.isEmpty()) {
			existingSummary = existingSummaryList.get(0);
			BeanUtils.copyProperties(existingSummary, oldcopy);
			System.out.println(" Existing record found for date: " + reportDate);
		} else {
			existingSummary = new M_SRWA_12E_LTV_Summary_Entity();
			existingSummary.setReport_date(reportDate);
			System.out.println("⚠️ No record found — creating new entry for date: " + reportDate);
		}

		// 🔹 Fetch or create DETAIL using JDBC
		List<M_SRWA_12E_LTV_Detail_Entity> existingDetailList = getDetailDataByDate(reportDate);
		M_SRWA_12E_LTV_Detail_Entity detailEntity;

		if (!existingDetailList.isEmpty()) {
			detailEntity = existingDetailList.get(0);
		} else {
			detailEntity = new M_SRWA_12E_LTV_Detail_Entity();
			detailEntity.setReport_date(reportDate);
		}

		try {
			// 1️⃣ Loop from R13 to R19 and copy fields
			for (int i = 13; i <= 19; i++) {
				String prefix = "R" + i + "_";

				String[] fields = { "PRODUCT", "PERFORMING_EXPOSURE", "NON_PERFORMING", "SPECIFIC_PROV",
						"UNSECURED_PORTION_NPL", "TOTAL" };

				for (String field : fields) {
					String getterName = "get" + prefix + field;
					String setterName = "set" + prefix + field;

					try {
						// Getter from UPDATED entity
						Method getter = M_SRWA_12E_LTV_Summary_Entity.class.getMethod(getterName);

						Object newValue = getter.invoke(updatedEntity);

						// SUMMARY setter
						Method summarySetter = M_SRWA_12E_LTV_Summary_Entity.class.getMethod(setterName,
								getter.getReturnType());

						summarySetter.invoke(existingSummary, newValue);

						// DETAIL setter
						Method detailSetter = M_SRWA_12E_LTV_Detail_Entity.class.getMethod(setterName,
								getter.getReturnType());

						detailSetter.invoke(detailEntity, newValue);

					} catch (NoSuchMethodException e) {
						// Skip missing fields
						continue;
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Error while updating report fields", e);
		}

		// Evaluate the actual changes calculated post-normalization
		String changes = auditService.getChanges(oldcopy, existingSummary);
		System.out.println("M_SRWA_12E Changes Length = " + changes.length());

		System.out.println("Saving Summary & Detail tables");

		// 💾 Save both tables using JDBC
		saveOrUpdateSummary(existingSummary);
		saveOrUpdateDetail(detailEntity);

		// Only invoke audit logger if actual physical modifications exist
		if (changes != null && !changes.isEmpty()) {
			auditService.compareEntitiesmanual(oldcopy, existingSummary, reportDate.toString(),
					"M_SRWA_12E Summary Screen", "BRRS_M_SRWA_12E_SUMMARY");
		}

		System.out.println("Update completed successfully");
	}

	// ============================
	// SAVE/UPDATE METHODS FOR SUMMARY
	// ============================

	private void saveOrUpdateSummary(M_SRWA_12E_LTV_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_SUMMARYTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_SUMMARYTABLE SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? " + "WHERE REPORT_DATE = ?";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Summary updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_SUMMARYTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Summary inserted for date: " + entity.getReport_date());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR DETAIL
	// ============================

	private void saveOrUpdateDetail(M_SRWA_12E_LTV_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_DETAILTABLE WHERE REPORT_DATE = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql, new Object[] { entity.getReport_date() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_DETAILTABLE SET "
					+ "REPORT_VERSION = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? " + "WHERE REPORT_DATE = ?";
			jdbcTemplate.update(sql, entity.getReport_version(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date());
			System.out.println("✅ Detail updated for date: " + entity.getReport_date());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_DETAILTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, "
					+ "REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReport_frequency(),
					entity.getReport_code(), entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(),
					entity.getDel_flg());
			System.out.println("✅ Detail inserted for date: " + entity.getReport_date());
		}
	}

	// ========================================
	// UPDATE RESUB REPORT METHOD - JDBC VERSION
	// ========================================

	public void updateResubReport(M_SRWA_12E_LTV_Resub_Summary_Entity updatedEntity) {

		Date reportDate = updatedEntity.getReport_date();

		// ====================================================
		// 1️⃣ FETCH EXISTING DATA FROM SUMMARY TABLE
		// ====================================================
		List<M_SRWA_12E_LTV_Summary_Entity> existingSummaryList = getSummaryDataByDate(reportDate);
		if (existingSummaryList.isEmpty()) {
			throw new RuntimeException("No summary data found for date: " + reportDate);
		}
		M_SRWA_12E_LTV_Summary_Entity existingSummary = existingSummaryList.get(0);
		System.out.println("✅ Existing Summary data fetched for date: " + reportDate);

		// ====================================================
		// 2️⃣ GET CURRENT VERSION FROM RESUB TABLE
		// ====================================================
		BigDecimal maxResubVer = findMaxResubSummaryVersion(reportDate);
		BigDecimal newVersion = (maxResubVer == null) ? BigDecimal.ONE : maxResubVer.add(BigDecimal.ONE);
		Date now = new Date();
		System.out.println("✅ New Resub Version: " + newVersion);

		// ====================================================
		// 3️⃣ FETCH EXISTING DETAIL DATA
		// ====================================================
		List<M_SRWA_12E_LTV_Detail_Entity> existingDetailList = getDetailDataByDate(reportDate);
		M_SRWA_12E_LTV_Detail_Entity existingDetail = existingDetailList.isEmpty() ? new M_SRWA_12E_LTV_Detail_Entity()
				: existingDetailList.get(0);
		System.out.println("✅ Existing Detail data fetched");

		// ====================================================
		// 4️⃣ CREATE RESUB SUMMARY - MERGE EXISTING + UPDATED
		// ====================================================
		M_SRWA_12E_LTV_Resub_Summary_Entity resubSummary = new M_SRWA_12E_LTV_Resub_Summary_Entity();

		// Start with existing data
		BeanUtils.copyProperties(existingSummary, resubSummary);

		// Override with updated values (if any non-null values from updatedEntity)
		copyNonNullProperties(updatedEntity, resubSummary);

		// Set Resub specific fields
		resubSummary.setReport_date(reportDate);
		resubSummary.setReport_version(newVersion);
		resubSummary.setReportResubDate(now);

		// ====================================================
		// 5️⃣ CREATE RESUB DETAIL - MERGE EXISTING + UPDATED
		// ====================================================
		M_SRWA_12E_LTV_Resub_Detail_Entity resubDetail = new M_SRWA_12E_LTV_Resub_Detail_Entity();

		BeanUtils.copyProperties(existingDetail, resubDetail);
		copyNonNullProperties(updatedEntity, resubDetail);

		resubDetail.setReport_date(reportDate);
		resubDetail.setReport_version(newVersion);
		resubDetail.setReportResubDate(now);

		// ====================================================
		// 6️⃣ CREATE ARCHIVAL SUMMARY - MERGE EXISTING + UPDATED
		// ====================================================
		M_SRWA_12E_LTV_Archival_Summary_Entity archSummary = new M_SRWA_12E_LTV_Archival_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, archSummary);
		copyNonNullProperties(updatedEntity, archSummary);

		archSummary.setReport_date(reportDate);
		archSummary.setReport_version(newVersion);
		archSummary.setReportResubDate(now);

		// ====================================================
		// 7️⃣ CREATE ARCHIVAL DETAIL - MERGE EXISTING + UPDATED
		// ====================================================
		M_SRWA_12E_LTV_Archival_Detail_Entity archDetail = new M_SRWA_12E_LTV_Archival_Detail_Entity();

		BeanUtils.copyProperties(existingDetail, archDetail);
		copyNonNullProperties(updatedEntity, archDetail);

		archDetail.setReport_date(reportDate);
		archDetail.setReport_version(newVersion);
		archDetail.setReportResubDate(now);

		// ====================================================
		// 8️⃣ SAVE ALL WITH JDBC METHODS
		// ====================================================
		System.out.println("💾 Saving Resub Summary...");
		saveOrUpdateResubSummary(resubSummary);

		System.out.println("💾 Saving Resub Detail...");
		saveOrUpdateResubDetail(resubDetail);

		System.out.println("💾 Saving Archival Summary...");
		saveOrUpdateArchivalSummary(archSummary);

		System.out.println("💾 Saving Archival Detail...");
		saveOrUpdateArchivalDetail(archDetail);

		System.out
				.println("✅ Resubmission completed successfully for date: " + reportDate + ", version: " + newVersion);
	}

	// ============================
	// HELPER: Copy only non-null properties
	// ============================

	private void copyNonNullProperties(Object source, Object target) {
		if (source == null || target == null)
			return;

		try {
			// Get all getter methods from source
			Method[] methods = source.getClass().getMethods();
			for (Method getter : methods) {
				String name = getter.getName();

				// Skip these methods
				if (name.equals("getClass") || name.equals("getReport_date") || name.equals("getReport_version")
						|| name.equals("getReportResubDate") || name.startsWith("getReport_")) {
					continue;
				}

				// Only process getters that start with "get"
				if (name.startsWith("get") && !name.startsWith("getReport_")) {
					try {
						Object value = getter.invoke(source);

						// Only copy non-null values
						if (value != null) {
							// Find corresponding setter
							String setterName = "set" + name.substring(3);

							try {
								Method setter = target.getClass().getMethod(setterName, getter.getReturnType());
								setter.invoke(target, value);
							} catch (NoSuchMethodException e) {
								// Setter doesn't exist in target, skip
								// System.out.println("Setter not found: " + setterName);
							}
						}
					} catch (Exception e) {
						// Skip if any error occurs
						// System.out.println("Error copying property: " + name + " - " +
						// e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			logger.warn("Error copying non-null properties: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR RESUB SUMMARY
	// ============================

	private void saveOrUpdateResubSummary(M_SRWA_12E_LTV_Resub_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Summary updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_RESUB_SUMMARYTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Summary inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR RESUB DETAIL
	// ============================

	private void saveOrUpdateResubDetail(M_SRWA_12E_LTV_Resub_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_RESUB_DETAILTABLE WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_RESUB_DETAILTABLE SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Resub Detail updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_RESUB_DETAILTABLE "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Resub Detail inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR ARCHIVAL SUMMARY
	// ============================

	private void saveOrUpdateArchivalSummary(M_SRWA_12E_LTV_Archival_Summary_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Summary updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_ARCHIVALTABLE_SUMMARY "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Summary inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ============================
	// SAVE/UPDATE METHODS FOR ARCHIVAL DETAIL
	// ============================

	private void saveOrUpdateArchivalDetail(M_SRWA_12E_LTV_Archival_Detail_Entity entity) {
		String checkSql = "SELECT COUNT(*) FROM BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
		Integer count = jdbcTemplate.queryForObject(checkSql,
				new Object[] { entity.getReport_date(), entity.getReport_version() }, Integer.class);

		if (count > 0) {
			String sql = "UPDATE BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL SET "
					+ "REPORT_RESUBDATE = ?, REPORT_FREQUENCY = ?, REPORT_CODE = ?, "
					+ "REPORT_DESC = ?, ENTITY_FLG = ?, MODIFY_FLG = ?, DEL_FLG = ? "
					+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";
			jdbcTemplate.update(sql, entity.getReportResubDate(), entity.getReport_frequency(), entity.getReport_code(),
					entity.getReport_desc(), entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg(),
					entity.getReport_date(), entity.getReport_version());
			System.out.println("✅ Archival Detail updated for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		} else {
			String sql = "INSERT INTO BRRS_M_SRWA_12E_ARCHIVALTABLE_DETAIL "
					+ "(REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE, REPORT_FREQUENCY, "
					+ "REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, entity.getReport_date(), entity.getReport_version(), entity.getReportResubDate(),
					entity.getReport_frequency(), entity.getReport_code(), entity.getReport_desc(),
					entity.getEntity_flg(), entity.getModify_flg(), entity.getDel_flg());
			System.out.println("✅ Archival Detail inserted for date: " + entity.getReport_date() + ", version: "
					+ entity.getReport_version());
		}
	}

	// ========================================
	// ARCHIVAL VIEW - JDBC VERSION
	// ========================================

	public List<Object[]> getM_SRWA_12E_LTVArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {
			// ✅ Using JDBC method instead of JPA repository
			List<M_SRWA_12E_LTV_Archival_Summary_Entity> repoData = getArchivalSummaryDataWithVersionAll();

			if (repoData != null && !repoData.isEmpty()) {
				for (M_SRWA_12E_LTV_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				M_SRWA_12E_LTV_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12E_LTV Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	// ========================================
	// RESUB VIEW - JDBC VERSION
	// ========================================

	public List<Object[]> getM_SRWA_12E_LTVResub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			// ✅ Using JDBC method instead of JPA repository
			List<M_SRWA_12E_LTV_Resub_Summary_Entity> latestArchivalList = getResubSummaryDataWithVersionAll();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (M_SRWA_12E_LTV_Resub_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching M_SRWA_12E_LTV Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	// Normal format Excel

	public byte[] BRRS_M_SRWA_12E_LTVExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		System.out.println("======= VIEW SCREEN =======");
		System.out.println("TYPE      : " + type);
		System.out.println("FORMAT      : " + format);
		System.out.println("DTLTYPE   : " + dtltype);
		System.out.println("DATE      : " + dateformat.parse(todate));
		System.out.println("VERSION   : " + version);
		System.out.println("==========================");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return getExcelM_SRWA_12E_LTVARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						format, version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_M_SRWA_12E_LTVEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} else {

				// Fetch data

				// ✅ Fetch data using JDBC method instead of JPA repository
				// Fetch data
				List<M_SRWA_12E_LTV_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				// If no data, try to get latest available date
				if (dataList.isEmpty()) {
					Date latestDate = getLatestReportDate();
					if (latestDate != null) {
						dataList = getSummaryDataByDate(latestDate);
						logger.info("No data for requested date {}. Using latest available date: {}", todate,
								latestDate);
					}
				}

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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
							M_SRWA_12E_LTV_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(12);

							Cell cell2 = row.createCell(2);
							if (record.getR13_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							Cell cell3 = row.createCell(3);
							if (record.getR13_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							Cell cell4 = row.createCell(4);
							if (record.getR13_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(13);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR14_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR14_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR14_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(14);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR15_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR15_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR15_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(15);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR16_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR16_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR16_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

							row = sheet.getRow(16);

							// ====================== R11 ======================
							cell2 = row.createCell(2);
							if (record.getR17_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR17_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR17_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}
							row = sheet.getRow(17);

							// ====================== R18 ======================
							cell2 = row.createCell(2);
							if (record.getR18_PERFORMING_EXPOSURE() != null) {
								cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
								cell2.setCellStyle(numberStyle);
							} else {
								cell2.setCellValue("");
								cell2.setCellStyle(textStyle);
							}

							cell3 = row.createCell(3);
							if (record.getR18_NON_PERFORMING() != null) {
								cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
								cell3.setCellStyle(numberStyle);
							} else {
								cell3.setCellValue("");
								cell3.setCellStyle(textStyle);
							}

							cell4 = row.createCell(4);
							if (record.getR18_SPECIFIC_PROV() != null) {
								cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
								cell4.setCellStyle(numberStyle);
							} else {
								cell4.setCellValue("");
								cell4.setCellStyle(textStyle);
							}

						}

						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());
					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV SUMMARY", null,
								"M_SRWA_12E_LTV_SUMMARYTABLE");
					}
					return out.toByteArray();
				}
			}
		}
	}

	// Normal Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12E_LTVEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<M_SRWA_12E_LTV_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

			// If no data, try to get latest available date
			if (dataList.isEmpty()) {
				Date latestDate = getLatestReportDate();
				if (latestDate != null) {
					dataList = getSummaryDataByDate(latestDate);
					logger.info("No data for requested date {}. Using latest available date: {}", todate, latestDate);
				}
			}

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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

				int startRow = 4;

				if (!dataList.isEmpty()) {
					for (int i = 0; i < dataList.size(); i++) {
						M_SRWA_12E_LTV_Summary_Entity record = dataList.get(i);
						System.out.println("rownumber=" + startRow + i);
						Row row = sheet.getRow(startRow + i);
						if (row == null) {
							row = sheet.createRow(startRow + i);
						}

						Cell R12Cell = row.createCell(3);

						if (record.getReport_date() != null) {

							R12Cell.setCellValue(record.getReport_date());

							R12Cell.setCellStyle(dateStyle);

						} else {

							R12Cell.setCellValue("");

							R12Cell.setCellStyle(textStyle);
						}
						row = sheet.getRow(13);
//EMAIL

						Cell cell2 = row.createCell(2);
						if (record.getR13_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						Cell cell3 = row.createCell(3);
						if (record.getR13_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						Cell cell4 = row.createCell(4);
						if (record.getR13_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						Cell cell5 = row.createCell(5);
						if (record.getR13_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						row = sheet.getRow(14);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR14_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR14_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR14_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						cell5 = row.createCell(5);
						if (record.getR14_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						Cell cell6 = row.createCell(6);
						if (record.getR14_TOTAL() != null) {
							cell6.setCellValue(record.getR14_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						row = sheet.getRow(15);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR15_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR15_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR15_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR15_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}
						row = sheet.getRow(16);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR16_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR16_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR16_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

						cell5 = row.createCell(5);
						if (record.getR16_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR16_TOTAL() != null) {
							cell6.setCellValue(record.getR16_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}

						row = sheet.getRow(17);

						// ====================== R11 ======================
						cell2 = row.createCell(2);
						if (record.getR17_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR17_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR17_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}
						cell5 = row.createCell(5);
						if (record.getR17_UNSECURED_PORTION_NPL() != null) {
							cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
							cell5.setCellStyle(numberStyle);
						} else {
							cell5.setCellValue("");
							cell5.setCellStyle(textStyle);
						}

						cell6 = row.createCell(6);
						if (record.getR17_TOTAL() != null) {
							cell6.setCellValue(record.getR17_TOTAL().doubleValue());
							cell6.setCellStyle(numberStyle);
						} else {
							cell6.setCellValue("");
							cell6.setCellStyle(textStyle);
						}
						row = sheet.getRow(18);

						// ====================== R18 ======================
						cell2 = row.createCell(2);
						if (record.getR19_PERFORMING_EXPOSURE() != null) {
							cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
							cell2.setCellStyle(numberStyle);
						} else {
							cell2.setCellValue("");
							cell2.setCellStyle(textStyle);
						}

						cell3 = row.createCell(3);
						if (record.getR19_NON_PERFORMING() != null) {
							cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
							cell3.setCellStyle(numberStyle);
						} else {
							cell3.setCellValue("");
							cell3.setCellStyle(textStyle);
						}

						cell4 = row.createCell(4);
						if (record.getR19_SPECIFIC_PROV() != null) {
							cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
							cell4.setCellStyle(numberStyle);
						} else {
							cell4.setCellValue("");
							cell4.setCellStyle(textStyle);
						}

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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV EMAIL SUMMARY", null,
							"SRWA_12E_LTV_SUMMARY_TABLE");
				}
				return out.toByteArray();
			}
		}
	}

	// Archival format excel
	public byte[] getExcelM_SRWA_12E_LTVARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_M_SRWA_12E_LTVEmailArchivalExcel(filename, reportId, fromdate, todate, currency, dtltype,
						type, version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12E_LTV_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available date
		if (dataList.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				// Get latest version for this date
				Optional<M_SRWA_12E_LTV_Archival_Summary_Entity> latestOpt = getLatestArchivalSummaryVersionByDate(
						latestDate);
				if (latestOpt.isPresent()) {
					dataList = getArchivalSummaryDataByDateAndVersion(latestDate, latestOpt.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt.get().getReport_version());
				}
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12E_LTV report. Returning empty result.");
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
					M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(12);
//NORMAL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV ARCHIVAL SUMMARY", null,
						"M_SRWA_12E_LTV_ARCHIVATABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Archival Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVEmailArchivalExcel(String filename, String reportId, String fromdate,
			String todate, String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12E_LTV_Archival_Summary_Entity> dataList = getArchivalSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available date
		if (dataList.isEmpty()) {
			Date latestDate = getLatestReportDate();
			if (latestDate != null) {
				// Get latest version for this date
				Optional<M_SRWA_12E_LTV_Archival_Summary_Entity> latestOpt = getLatestArchivalSummaryVersionByDate(
						latestDate);
				if (latestOpt.isPresent()) {
					dataList = getArchivalSummaryDataByDateAndVersion(latestDate, latestOpt.get().getReport_version());
					logger.info("No data for requested date. Using latest available date: {} with version: {}",
							latestDate, latestOpt.get().getReport_version());
				}
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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
					M_SRWA_12E_LTV_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(3);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);
//EMAIL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					Cell cell5 = row.createCell(5);
					if (record.getR13_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR14_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(6);
					if (record.getR14_TOTAL() != null) {
						cell6.setCellValue(record.getR14_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_TOTAL() != null) {
						cell6.setCellValue(record.getR16_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR17_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_TOTAL() != null) {
						cell6.setCellValue(record.getR17_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR19_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV EMAIL ARCHIVAL SUMMARY", null,
						"M_SRWA_12E_LTV_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}

	// Resub Format excel
	public byte[] BRRS_M_SRWA_12E_LTVResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_M_SRWA_12E_LTVResubEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<M_SRWA_12E_LTV_Resub_Summary_Entity> dataList = getResubSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available version
		if (dataList.isEmpty()) {
			BigDecimal maxVersion = findMaxResubSummaryVersion(dateformat.parse(todate));
			if (maxVersion != null) {
				dataList = getResubSummaryDataByDateAndVersion(dateformat.parse(todate), maxVersion);
				logger.info("No data for requested version. Using latest version: {}", maxVersion);
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for M_SRWA_12E_LTV report. Returning empty result.");
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

					M_SRWA_12E_LTV_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
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
					row = sheet.getRow(12);
//NORMAL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(17);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR18_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR18_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR18_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR18_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR18_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR18_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV RESUB SUMMARY", null,
						"M_SRWA_12E_LTV_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	// Resub Email Excel
	public byte[] BRRS_M_SRWA_12E_LTVResubEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<M_SRWA_12E_LTV_Resub_Summary_Entity> dataList = getResubSummaryDataByDateAndVersion(
				dateformat.parse(todate), version);

		// If no data, try to get latest available version
		if (dataList.isEmpty()) {
			BigDecimal maxVersion = findMaxResubSummaryVersion(dateformat.parse(todate));
			if (maxVersion != null) {
				dataList = getResubSummaryDataByDateAndVersion(dateformat.parse(todate), maxVersion);
				logger.info("No data for requested version. Using latest version: {}", maxVersion);
			}
		}

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_M_SRWA_12E_LTV report. Returning empty result.");
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
					M_SRWA_12E_LTV_Resub_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					Cell R12Cell = row.createCell(3);

					if (record.getReport_date() != null) {

						R12Cell.setCellValue(record.getReport_date());

						R12Cell.setCellStyle(dateStyle);

					} else {

						R12Cell.setCellValue("");

						R12Cell.setCellStyle(textStyle);
					}
					row = sheet.getRow(13);
//EMAIL

					Cell cell2 = row.createCell(2);
					if (record.getR13_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR13_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR13_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR13_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					Cell cell4 = row.createCell(4);
					if (record.getR13_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR13_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					Cell cell5 = row.createCell(5);
					if (record.getR13_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR13_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					row = sheet.getRow(14);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR14_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR14_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR14_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR14_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR14_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR14_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR14_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					Cell cell6 = row.createCell(6);
					if (record.getR14_TOTAL() != null) {
						cell6.setCellValue(record.getR14_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(15);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR15_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR15_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR15_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR15_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR15_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR15_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR15_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					row = sheet.getRow(16);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR16_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR16_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR16_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR16_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR16_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR16_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					cell5 = row.createCell(5);
					if (record.getR16_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR16_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR16_TOTAL() != null) {
						cell6.setCellValue(record.getR16_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					row = sheet.getRow(17);

					// ====================== R11 ======================
					cell2 = row.createCell(2);
					if (record.getR17_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR17_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR17_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR17_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR17_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR17_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					cell5 = row.createCell(5);
					if (record.getR17_UNSECURED_PORTION_NPL() != null) {
						cell5.setCellValue(record.getR17_UNSECURED_PORTION_NPL().doubleValue());
						cell5.setCellStyle(numberStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					cell6 = row.createCell(6);
					if (record.getR17_TOTAL() != null) {
						cell6.setCellValue(record.getR17_TOTAL().doubleValue());
						cell6.setCellStyle(numberStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					row = sheet.getRow(18);

					// ====================== R18 ======================
					cell2 = row.createCell(2);
					if (record.getR19_PERFORMING_EXPOSURE() != null) {
						cell2.setCellValue(record.getR19_PERFORMING_EXPOSURE().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR19_NON_PERFORMING() != null) {
						cell3.setCellValue(record.getR19_NON_PERFORMING().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					cell4 = row.createCell(4);
					if (record.getR19_SPECIFIC_PROV() != null) {
						cell4.setCellValue(record.getR19_SPECIFIC_PROV().doubleValue());
						cell4.setCellStyle(numberStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "M_SRWA_12E_LTV EMAIL RESUB SUMMARY", null,
						"M_SRWA_12E_LTV_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

}