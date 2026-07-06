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
import java.util.Map;

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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.bornfire.brrs.entities.UserProfileRep;

@Service

public class BRRS_Q_STAFF_Report_Service {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_Q_STAFF_Report_Service.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	AuditService auditService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	UserProfileRep userProfileRep;

// =====================================================
// SUMAMRY REPO
// =====================================================

	public List<Q_STAFF_Summary_Entity> getSummaryDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_STAFF_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_STAFF_Summary_RowMapper());
	}

	// findbyreportdate

	public Q_STAFF_Summary_Entity findByReportDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_STAFF_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		List<Q_STAFF_Summary_Entity> list = jdbcTemplate.query(sql, new Object[] { reportDate },
				new Q_STAFF_Summary_RowMapper());

		return list.isEmpty() ? null : list.get(0);
	}

// =====================================================
// ARCHIVAL  SUMAMRY REPO
// =====================================================

	public List<Object[]> get_Q_STAFF_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Q_STAFF_Archival_Summary_Entity> getDataByDateListArchival(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_STAFF_Archival_Summary_RowMapper());
	}

	public List<Q_STAFF_Archival_Summary_Entity> getarchivaldatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new Q_STAFF_Archival_Summary_RowMapper());
	}

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

// =====================================================
// DETAIL REPO
// =====================================================	

	public List<Q_STAFF_Detail_Entity> getDetaildatabydateList(Date reportDate) {

		String sql = "SELECT * FROM BRRS_Q_STAFF_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new Q_STAFF_Detail_RowMapper());
	}

// =====================================================
// ARCHIVAL  DETAIL REPO
// =====================================================

	public List<Map<String, Object>> getQ_STAFF_archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_STAFF_ARCHIVALTABLE_DETAIL "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_STAFF_Archival_Detail_Entity> getDetaildatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_STAFF_Archival_Detail_RowMapper());
	}

	public BigDecimal findDETAILMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_STAFF_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_STAFF_Archival_Detail_Entity getArchivalListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_STAFF_Archival_Detail_RowMapper());
	}

// =====================================================
// RESUB SUMMARY
// =====================================================

	public List<Q_STAFF_Resub_Summary_Entity> getResubSummarydatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_STAFF_RESUB_Summary_RowMapper());
	}

	public BigDecimal findResubSummaryMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_STAFF_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Map<String, Object>> getQ_STAFF_Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_STAFF_RESUB_SUMMARYTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public Q_STAFF_Resub_Summary_Entity getResubSummarydatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_STAFF_RESUB_Summary_RowMapper());
	}

// =====================================================
// RESUB DETAIL
// =====================================================

	public List<Map<String, Object>> get_Q_STAFFArchival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_Q_STAFF_RESUB_DETAILTABLE "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.queryForList(sql);
	}

	public List<Q_STAFF_Resub_Detail_Entity> getResubDetaildatabydateList(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new Q_STAFF_RESUB_Detail_RowMapper());
	}

	public BigDecimal findResubDetailMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_Q_STAFF_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public Q_STAFF_Resub_Detail_Entity getdResubDetailDatabydateListWithVersion() {

		String sql = "SELECT * " + "FROM BRRS_Q_STAFF_RESUB_DETAILTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC " + "FETCH FIRST 1 ROWS ONLY";

		return jdbcTemplate.queryForObject(sql, new Q_STAFF_RESUB_Detail_RowMapper());
	}

// =====================================================
// SUMAMRY ENTITY & ROW MAPPER 
// =====================================================

	public class Q_STAFF_Summary_RowMapper implements RowMapper<Q_STAFF_Summary_Entity> {

		@Override
		public Q_STAFF_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Summary_Entity obj = new Q_STAFF_Summary_Entity();

// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));

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

			return obj;
		}
	}

	public class Q_STAFF_Summary_Entity {

		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

// =====================================================
// ARCHIVAL  SUMAMRY ENTITY 
// =====================================================

	public class Q_STAFF_Archival_Summary_RowMapper implements RowMapper<Q_STAFF_Archival_Summary_Entity> {

		@Override
		public Q_STAFF_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Archival_Summary_Entity obj = new Q_STAFF_Archival_Summary_Entity();

// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));
			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public class Q_STAFF_Archival_Summary_Entity {

		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		@Column(name = "REPORT_VERSION")
		@Id
		private BigDecimal report_version;
		@Column(name = "REPORT_RESUBDATE")

		private Date reportResubDate;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

		public Date getReportResubDate() {
			return reportResubDate;
		}

		public void setReportResubDate(Date reportResubDate) {
			this.reportResubDate = reportResubDate;
		}

	}

// =====================================================
// DETAIL ENTITY  Q_STAFF
// =====================================================	

	public class Q_STAFF_Detail_RowMapper implements RowMapper<Q_STAFF_Detail_Entity> {

		@Override
		public Q_STAFF_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Detail_Entity obj = new Q_STAFF_Detail_Entity();

// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));

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

			return obj;
		}
	}

	public class Q_STAFF_Detail_Entity {
		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		private BigDecimal report_version;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

// =====================================================
// ARCHIVAL  DETAIL ENTITY 
// =====================================================

	public class Q_STAFF_Archival_Detail_RowMapper implements RowMapper<Q_STAFF_Archival_Detail_Entity> {

		@Override
		public Q_STAFF_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Archival_Detail_Entity obj = new Q_STAFF_Archival_Detail_Entity();

			// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public class Q_STAFF_Archival_Detail_Entity {
		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

	}

//====================================================================================================================================
// RESUB summary Q_STAFF
//=====================================================

	public class Q_STAFF_RESUB_Summary_RowMapper implements RowMapper<Q_STAFF_Resub_Summary_Entity> {

		@Override
		public Q_STAFF_Resub_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Resub_Summary_Entity obj = new Q_STAFF_Resub_Summary_Entity();
// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));
			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public class Q_STAFF_Resub_Summary_Entity {
		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

	}

//=====================================================
// RESUB DETAIL Q_STAFF
//=====================================================

	public class Q_STAFF_RESUB_Detail_RowMapper implements RowMapper<Q_STAFF_Resub_Detail_Entity> {

		@Override
		public Q_STAFF_Resub_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			Q_STAFF_Resub_Detail_Entity obj = new Q_STAFF_Resub_Detail_Entity();
// R9
			obj.setR9_STAFF_COMPLEMENT(rs.getString("R9_STAFF_COMPLEMENT"));
			obj.setR9_LOCAL(rs.getBigDecimal("R9_LOCAL"));
			obj.setR9_EXPARIATES(rs.getBigDecimal("R9_EXPARIATES"));
			obj.setR9_TOTAL(rs.getBigDecimal("R9_TOTAL"));

			// R10
			obj.setR10_STAFF_COMPLEMENT(rs.getString("R10_STAFF_COMPLEMENT"));
			obj.setR10_LOCAL(rs.getBigDecimal("R10_LOCAL"));
			obj.setR10_EXPARIATES(rs.getBigDecimal("R10_EXPARIATES"));
			obj.setR10_TOTAL(rs.getBigDecimal("R10_TOTAL"));

			// R11
			obj.setR11_STAFF_COMPLEMENT(rs.getString("R11_STAFF_COMPLEMENT"));
			obj.setR11_LOCAL(rs.getBigDecimal("R11_LOCAL"));
			obj.setR11_EXPARIATES(rs.getBigDecimal("R11_EXPARIATES"));
			obj.setR11_TOTAL(rs.getBigDecimal("R11_TOTAL"));

			// R12
			obj.setR12_STAFF_COMPLEMENT(rs.getString("R12_STAFF_COMPLEMENT"));
			obj.setR12_LOCAL(rs.getBigDecimal("R12_LOCAL"));
			obj.setR12_EXPARIATES(rs.getBigDecimal("R12_EXPARIATES"));
			obj.setR12_TOTAL(rs.getBigDecimal("R12_TOTAL"));

			// R13
			obj.setR13_STAFF_COMPLEMENT(rs.getString("R13_STAFF_COMPLEMENT"));
			obj.setR13_LOCAL(rs.getBigDecimal("R13_LOCAL"));
			obj.setR13_EXPARIATES(rs.getBigDecimal("R13_EXPARIATES"));
			obj.setR13_TOTAL(rs.getBigDecimal("R13_TOTAL"));

			// R14
			obj.setR14_STAFF_COMPLEMENT(rs.getString("R14_STAFF_COMPLEMENT"));
			obj.setR14_LOCAL(rs.getBigDecimal("R14_LOCAL"));
			obj.setR14_EXPARIATES(rs.getBigDecimal("R14_EXPARIATES"));
			obj.setR14_TOTAL(rs.getBigDecimal("R14_TOTAL"));

			// R15
			obj.setR15_STAFF_COMPLEMENT(rs.getString("R15_STAFF_COMPLEMENT"));
			obj.setR15_LOCAL(rs.getBigDecimal("R15_LOCAL"));
			obj.setR15_EXPARIATES(rs.getBigDecimal("R15_EXPARIATES"));
			obj.setR15_TOTAL(rs.getBigDecimal("R15_TOTAL"));

			// R21
			obj.setR21_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R21_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR21_LOCAL(rs.getBigDecimal("R21_LOCAL"));
			obj.setR21_EXPARIATES(rs.getBigDecimal("R21_EXPARIATES"));
			obj.setR21_TOTAL(rs.getBigDecimal("R21_TOTAL"));

			// R22
			obj.setR22_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R22_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR22_LOCAL(rs.getBigDecimal("R22_LOCAL"));
			obj.setR22_EXPARIATES(rs.getBigDecimal("R22_EXPARIATES"));
			obj.setR22_TOTAL(rs.getBigDecimal("R22_TOTAL"));

			// R23
			obj.setR23_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R23_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR23_LOCAL(rs.getBigDecimal("R23_LOCAL"));
			obj.setR23_EXPARIATES(rs.getBigDecimal("R23_EXPARIATES"));
			obj.setR23_TOTAL(rs.getBigDecimal("R23_TOTAL"));

			// R24
			obj.setR24_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R24_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR24_LOCAL(rs.getBigDecimal("R24_LOCAL"));
			obj.setR24_EXPARIATES(rs.getBigDecimal("R24_EXPARIATES"));
			obj.setR24_TOTAL(rs.getBigDecimal("R24_TOTAL"));

			// R25
			obj.setR25_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R25_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR25_LOCAL(rs.getBigDecimal("R25_LOCAL"));
			obj.setR25_EXPARIATES(rs.getBigDecimal("R25_EXPARIATES"));
			obj.setR25_TOTAL(rs.getBigDecimal("R25_TOTAL"));

			// R26
			obj.setR26_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R26_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR26_LOCAL(rs.getBigDecimal("R26_LOCAL"));
			obj.setR26_EXPARIATES(rs.getBigDecimal("R26_EXPARIATES"));
			obj.setR26_TOTAL(rs.getBigDecimal("R26_TOTAL"));

			// R27
			obj.setR27_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R27_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR27_LOCAL(rs.getBigDecimal("R27_LOCAL"));
			obj.setR27_EXPARIATES(rs.getBigDecimal("R27_EXPARIATES"));
			obj.setR27_TOTAL(rs.getBigDecimal("R27_TOTAL"));

			// R28
			obj.setR28_SENIOR_MANAGEMENT_COMPENSATION(rs.getString("R28_SENIOR_MANAGEMENT_COMPENSATION"));
			obj.setR28_LOCAL(rs.getBigDecimal("R28_LOCAL"));
			obj.setR28_EXPARIATES(rs.getBigDecimal("R28_EXPARIATES"));
			obj.setR28_TOTAL(rs.getBigDecimal("R28_TOTAL"));

			// R33
			obj.setR33_STAFF_LOANS(rs.getString("R33_STAFF_LOANS"));
			obj.setR33_ORIGINAL_AMT(rs.getBigDecimal("R33_ORIGINAL_AMT"));
			obj.setR33_BALANCE_OUTSTANDING(rs.getBigDecimal("R33_BALANCE_OUTSTANDING"));
			obj.setR33_NO_OF_ACS(rs.getBigDecimal("R33_NO_OF_ACS"));
			obj.setR33_INTEREST_RATE(rs.getBigDecimal("R33_INTEREST_RATE"));

			// R34
			obj.setR34_STAFF_LOANS(rs.getString("R34_STAFF_LOANS"));
			obj.setR34_ORIGINAL_AMT(rs.getBigDecimal("R34_ORIGINAL_AMT"));
			obj.setR34_BALANCE_OUTSTANDING(rs.getBigDecimal("R34_BALANCE_OUTSTANDING"));
			obj.setR34_NO_OF_ACS(rs.getBigDecimal("R34_NO_OF_ACS"));
			obj.setR34_INTEREST_RATE(rs.getBigDecimal("R34_INTEREST_RATE"));

			// R35
			obj.setR35_STAFF_LOANS(rs.getString("R35_STAFF_LOANS"));
			obj.setR35_ORIGINAL_AMT(rs.getBigDecimal("R35_ORIGINAL_AMT"));
			obj.setR35_BALANCE_OUTSTANDING(rs.getBigDecimal("R35_BALANCE_OUTSTANDING"));
			obj.setR35_NO_OF_ACS(rs.getBigDecimal("R35_NO_OF_ACS"));
			obj.setR35_INTEREST_RATE(rs.getBigDecimal("R35_INTEREST_RATE"));

			// R36
			obj.setR36_STAFF_LOANS(rs.getString("R36_STAFF_LOANS"));
			obj.setR36_ORIGINAL_AMT(rs.getBigDecimal("R36_ORIGINAL_AMT"));
			obj.setR36_BALANCE_OUTSTANDING(rs.getBigDecimal("R36_BALANCE_OUTSTANDING"));
			obj.setR36_NO_OF_ACS(rs.getBigDecimal("R36_NO_OF_ACS"));
			obj.setR36_INTEREST_RATE(rs.getBigDecimal("R36_INTEREST_RATE"));

			// R37
			obj.setR37_STAFF_LOANS(rs.getString("R37_STAFF_LOANS"));
			obj.setR37_ORIGINAL_AMT(rs.getBigDecimal("R37_ORIGINAL_AMT"));
			obj.setR37_BALANCE_OUTSTANDING(rs.getBigDecimal("R37_BALANCE_OUTSTANDING"));
			obj.setR37_NO_OF_ACS(rs.getBigDecimal("R37_NO_OF_ACS"));
			obj.setR37_INTEREST_RATE(rs.getBigDecimal("R37_INTEREST_RATE"));

			// R38
			obj.setR38_STAFF_LOANS(rs.getString("R38_STAFF_LOANS"));
			obj.setR38_ORIGINAL_AMT(rs.getBigDecimal("R38_ORIGINAL_AMT"));
			obj.setR38_BALANCE_OUTSTANDING(rs.getBigDecimal("R38_BALANCE_OUTSTANDING"));
			obj.setR38_NO_OF_ACS(rs.getBigDecimal("R38_NO_OF_ACS"));
			obj.setR38_INTEREST_RATE(rs.getBigDecimal("R38_INTEREST_RATE"));

			// =========================
			// COMMON FIELDS
			// =========================
			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setReportResubDate(rs.getDate("report_resubdate"));

			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));

			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public class Q_STAFF_Resub_Detail_Entity {
		private String R9_STAFF_COMPLEMENT;
		private BigDecimal R9_LOCAL;
		private BigDecimal R9_EXPARIATES;
		private BigDecimal R9_TOTAL;
		private String R10_STAFF_COMPLEMENT;
		private BigDecimal R10_LOCAL;
		private BigDecimal R10_EXPARIATES;
		private BigDecimal R10_TOTAL;
		private String R11_STAFF_COMPLEMENT;
		private BigDecimal R11_LOCAL;
		private BigDecimal R11_EXPARIATES;
		private BigDecimal R11_TOTAL;
		private String R12_STAFF_COMPLEMENT;
		private BigDecimal R12_LOCAL;
		private BigDecimal R12_EXPARIATES;
		private BigDecimal R12_TOTAL;
		private String R13_STAFF_COMPLEMENT;
		private BigDecimal R13_LOCAL;
		private BigDecimal R13_EXPARIATES;
		private BigDecimal R13_TOTAL;
		private String R14_STAFF_COMPLEMENT;
		private BigDecimal R14_LOCAL;
		private BigDecimal R14_EXPARIATES;
		private BigDecimal R14_TOTAL;
		private String R15_STAFF_COMPLEMENT;
		private BigDecimal R15_LOCAL;
		private BigDecimal R15_EXPARIATES;
		private BigDecimal R15_TOTAL;
		private String R21_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R21_LOCAL;
		private BigDecimal R21_EXPARIATES;
		private BigDecimal R21_TOTAL;
		private String R22_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R22_LOCAL;
		private BigDecimal R22_EXPARIATES;
		private BigDecimal R22_TOTAL;
		private String R23_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R23_LOCAL;
		private BigDecimal R23_EXPARIATES;
		private BigDecimal R23_TOTAL;
		private String R24_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R24_LOCAL;
		private BigDecimal R24_EXPARIATES;
		private BigDecimal R24_TOTAL;
		private String R25_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R25_LOCAL;
		private BigDecimal R25_EXPARIATES;
		private BigDecimal R25_TOTAL;
		private String R26_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R26_LOCAL;
		private BigDecimal R26_EXPARIATES;
		private BigDecimal R26_TOTAL;
		private String R27_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R27_LOCAL;
		private BigDecimal R27_EXPARIATES;
		private BigDecimal R27_TOTAL;
		private String R28_SENIOR_MANAGEMENT_COMPENSATION;
		private BigDecimal R28_LOCAL;
		private BigDecimal R28_EXPARIATES;
		private BigDecimal R28_TOTAL;
		private String R33_STAFF_LOANS;
		private BigDecimal R33_ORIGINAL_AMT;
		private BigDecimal R33_BALANCE_OUTSTANDING;
		private BigDecimal R33_NO_OF_ACS;
		private BigDecimal R33_INTEREST_RATE;
		private String R34_STAFF_LOANS;
		private BigDecimal R34_ORIGINAL_AMT;
		private BigDecimal R34_BALANCE_OUTSTANDING;
		private BigDecimal R34_NO_OF_ACS;
		private BigDecimal R34_INTEREST_RATE;
		private String R35_STAFF_LOANS;
		private BigDecimal R35_ORIGINAL_AMT;
		private BigDecimal R35_BALANCE_OUTSTANDING;
		private BigDecimal R35_NO_OF_ACS;
		private BigDecimal R35_INTEREST_RATE;
		private String R36_STAFF_LOANS;
		private BigDecimal R36_ORIGINAL_AMT;
		private BigDecimal R36_BALANCE_OUTSTANDING;
		private BigDecimal R36_NO_OF_ACS;
		private BigDecimal R36_INTEREST_RATE;
		private String R37_STAFF_LOANS;
		private BigDecimal R37_ORIGINAL_AMT;
		private BigDecimal R37_BALANCE_OUTSTANDING;
		private BigDecimal R37_NO_OF_ACS;
		private BigDecimal R37_INTEREST_RATE;
		private String R38_STAFF_LOANS;
		private BigDecimal R38_ORIGINAL_AMT;
		private BigDecimal R38_BALANCE_OUTSTANDING;
		private BigDecimal R38_NO_OF_ACS;
		private BigDecimal R38_INTEREST_RATE;
		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;

		@Id
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private Date reportResubDate;

		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public String getR9_STAFF_COMPLEMENT() {
			return R9_STAFF_COMPLEMENT;
		}

		public void setR9_STAFF_COMPLEMENT(String r9_STAFF_COMPLEMENT) {
			R9_STAFF_COMPLEMENT = r9_STAFF_COMPLEMENT;
		}

		public BigDecimal getR9_LOCAL() {
			return R9_LOCAL;
		}

		public void setR9_LOCAL(BigDecimal r9_LOCAL) {
			R9_LOCAL = r9_LOCAL;
		}

		public BigDecimal getR9_EXPARIATES() {
			return R9_EXPARIATES;
		}

		public void setR9_EXPARIATES(BigDecimal r9_EXPARIATES) {
			R9_EXPARIATES = r9_EXPARIATES;
		}

		public BigDecimal getR9_TOTAL() {
			return R9_TOTAL;
		}

		public void setR9_TOTAL(BigDecimal r9_TOTAL) {
			R9_TOTAL = r9_TOTAL;
		}

		public String getR10_STAFF_COMPLEMENT() {
			return R10_STAFF_COMPLEMENT;
		}

		public void setR10_STAFF_COMPLEMENT(String r10_STAFF_COMPLEMENT) {
			R10_STAFF_COMPLEMENT = r10_STAFF_COMPLEMENT;
		}

		public BigDecimal getR10_LOCAL() {
			return R10_LOCAL;
		}

		public void setR10_LOCAL(BigDecimal r10_LOCAL) {
			R10_LOCAL = r10_LOCAL;
		}

		public BigDecimal getR10_EXPARIATES() {
			return R10_EXPARIATES;
		}

		public void setR10_EXPARIATES(BigDecimal r10_EXPARIATES) {
			R10_EXPARIATES = r10_EXPARIATES;
		}

		public BigDecimal getR10_TOTAL() {
			return R10_TOTAL;
		}

		public void setR10_TOTAL(BigDecimal r10_TOTAL) {
			R10_TOTAL = r10_TOTAL;
		}

		public String getR11_STAFF_COMPLEMENT() {
			return R11_STAFF_COMPLEMENT;
		}

		public void setR11_STAFF_COMPLEMENT(String r11_STAFF_COMPLEMENT) {
			R11_STAFF_COMPLEMENT = r11_STAFF_COMPLEMENT;
		}

		public BigDecimal getR11_LOCAL() {
			return R11_LOCAL;
		}

		public void setR11_LOCAL(BigDecimal r11_LOCAL) {
			R11_LOCAL = r11_LOCAL;
		}

		public BigDecimal getR11_EXPARIATES() {
			return R11_EXPARIATES;
		}

		public void setR11_EXPARIATES(BigDecimal r11_EXPARIATES) {
			R11_EXPARIATES = r11_EXPARIATES;
		}

		public BigDecimal getR11_TOTAL() {
			return R11_TOTAL;
		}

		public void setR11_TOTAL(BigDecimal r11_TOTAL) {
			R11_TOTAL = r11_TOTAL;
		}

		public String getR12_STAFF_COMPLEMENT() {
			return R12_STAFF_COMPLEMENT;
		}

		public void setR12_STAFF_COMPLEMENT(String r12_STAFF_COMPLEMENT) {
			R12_STAFF_COMPLEMENT = r12_STAFF_COMPLEMENT;
		}

		public BigDecimal getR12_LOCAL() {
			return R12_LOCAL;
		}

		public void setR12_LOCAL(BigDecimal r12_LOCAL) {
			R12_LOCAL = r12_LOCAL;
		}

		public BigDecimal getR12_EXPARIATES() {
			return R12_EXPARIATES;
		}

		public void setR12_EXPARIATES(BigDecimal r12_EXPARIATES) {
			R12_EXPARIATES = r12_EXPARIATES;
		}

		public BigDecimal getR12_TOTAL() {
			return R12_TOTAL;
		}

		public void setR12_TOTAL(BigDecimal r12_TOTAL) {
			R12_TOTAL = r12_TOTAL;
		}

		public String getR13_STAFF_COMPLEMENT() {
			return R13_STAFF_COMPLEMENT;
		}

		public void setR13_STAFF_COMPLEMENT(String r13_STAFF_COMPLEMENT) {
			R13_STAFF_COMPLEMENT = r13_STAFF_COMPLEMENT;
		}

		public BigDecimal getR13_LOCAL() {
			return R13_LOCAL;
		}

		public void setR13_LOCAL(BigDecimal r13_LOCAL) {
			R13_LOCAL = r13_LOCAL;
		}

		public BigDecimal getR13_EXPARIATES() {
			return R13_EXPARIATES;
		}

		public void setR13_EXPARIATES(BigDecimal r13_EXPARIATES) {
			R13_EXPARIATES = r13_EXPARIATES;
		}

		public BigDecimal getR13_TOTAL() {
			return R13_TOTAL;
		}

		public void setR13_TOTAL(BigDecimal r13_TOTAL) {
			R13_TOTAL = r13_TOTAL;
		}

		public String getR14_STAFF_COMPLEMENT() {
			return R14_STAFF_COMPLEMENT;
		}

		public void setR14_STAFF_COMPLEMENT(String r14_STAFF_COMPLEMENT) {
			R14_STAFF_COMPLEMENT = r14_STAFF_COMPLEMENT;
		}

		public BigDecimal getR14_LOCAL() {
			return R14_LOCAL;
		}

		public void setR14_LOCAL(BigDecimal r14_LOCAL) {
			R14_LOCAL = r14_LOCAL;
		}

		public BigDecimal getR14_EXPARIATES() {
			return R14_EXPARIATES;
		}

		public void setR14_EXPARIATES(BigDecimal r14_EXPARIATES) {
			R14_EXPARIATES = r14_EXPARIATES;
		}

		public BigDecimal getR14_TOTAL() {
			return R14_TOTAL;
		}

		public void setR14_TOTAL(BigDecimal r14_TOTAL) {
			R14_TOTAL = r14_TOTAL;
		}

		public String getR15_STAFF_COMPLEMENT() {
			return R15_STAFF_COMPLEMENT;
		}

		public void setR15_STAFF_COMPLEMENT(String r15_STAFF_COMPLEMENT) {
			R15_STAFF_COMPLEMENT = r15_STAFF_COMPLEMENT;
		}

		public BigDecimal getR15_LOCAL() {
			return R15_LOCAL;
		}

		public void setR15_LOCAL(BigDecimal r15_LOCAL) {
			R15_LOCAL = r15_LOCAL;
		}

		public BigDecimal getR15_EXPARIATES() {
			return R15_EXPARIATES;
		}

		public void setR15_EXPARIATES(BigDecimal r15_EXPARIATES) {
			R15_EXPARIATES = r15_EXPARIATES;
		}

		public BigDecimal getR15_TOTAL() {
			return R15_TOTAL;
		}

		public void setR15_TOTAL(BigDecimal r15_TOTAL) {
			R15_TOTAL = r15_TOTAL;
		}

		public String getR21_SENIOR_MANAGEMENT_COMPENSATION() {
			return R21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR21_SENIOR_MANAGEMENT_COMPENSATION(String r21_SENIOR_MANAGEMENT_COMPENSATION) {
			R21_SENIOR_MANAGEMENT_COMPENSATION = r21_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR21_LOCAL() {
			return R21_LOCAL;
		}

		public void setR21_LOCAL(BigDecimal r21_LOCAL) {
			R21_LOCAL = r21_LOCAL;
		}

		public BigDecimal getR21_EXPARIATES() {
			return R21_EXPARIATES;
		}

		public void setR21_EXPARIATES(BigDecimal r21_EXPARIATES) {
			R21_EXPARIATES = r21_EXPARIATES;
		}

		public BigDecimal getR21_TOTAL() {
			return R21_TOTAL;
		}

		public void setR21_TOTAL(BigDecimal r21_TOTAL) {
			R21_TOTAL = r21_TOTAL;
		}

		public String getR22_SENIOR_MANAGEMENT_COMPENSATION() {
			return R22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR22_SENIOR_MANAGEMENT_COMPENSATION(String r22_SENIOR_MANAGEMENT_COMPENSATION) {
			R22_SENIOR_MANAGEMENT_COMPENSATION = r22_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR22_LOCAL() {
			return R22_LOCAL;
		}

		public void setR22_LOCAL(BigDecimal r22_LOCAL) {
			R22_LOCAL = r22_LOCAL;
		}

		public BigDecimal getR22_EXPARIATES() {
			return R22_EXPARIATES;
		}

		public void setR22_EXPARIATES(BigDecimal r22_EXPARIATES) {
			R22_EXPARIATES = r22_EXPARIATES;
		}

		public BigDecimal getR22_TOTAL() {
			return R22_TOTAL;
		}

		public void setR22_TOTAL(BigDecimal r22_TOTAL) {
			R22_TOTAL = r22_TOTAL;
		}

		public String getR23_SENIOR_MANAGEMENT_COMPENSATION() {
			return R23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR23_SENIOR_MANAGEMENT_COMPENSATION(String r23_SENIOR_MANAGEMENT_COMPENSATION) {
			R23_SENIOR_MANAGEMENT_COMPENSATION = r23_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR23_LOCAL() {
			return R23_LOCAL;
		}

		public void setR23_LOCAL(BigDecimal r23_LOCAL) {
			R23_LOCAL = r23_LOCAL;
		}

		public BigDecimal getR23_EXPARIATES() {
			return R23_EXPARIATES;
		}

		public void setR23_EXPARIATES(BigDecimal r23_EXPARIATES) {
			R23_EXPARIATES = r23_EXPARIATES;
		}

		public BigDecimal getR23_TOTAL() {
			return R23_TOTAL;
		}

		public void setR23_TOTAL(BigDecimal r23_TOTAL) {
			R23_TOTAL = r23_TOTAL;
		}

		public String getR24_SENIOR_MANAGEMENT_COMPENSATION() {
			return R24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR24_SENIOR_MANAGEMENT_COMPENSATION(String r24_SENIOR_MANAGEMENT_COMPENSATION) {
			R24_SENIOR_MANAGEMENT_COMPENSATION = r24_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR24_LOCAL() {
			return R24_LOCAL;
		}

		public void setR24_LOCAL(BigDecimal r24_LOCAL) {
			R24_LOCAL = r24_LOCAL;
		}

		public BigDecimal getR24_EXPARIATES() {
			return R24_EXPARIATES;
		}

		public void setR24_EXPARIATES(BigDecimal r24_EXPARIATES) {
			R24_EXPARIATES = r24_EXPARIATES;
		}

		public BigDecimal getR24_TOTAL() {
			return R24_TOTAL;
		}

		public void setR24_TOTAL(BigDecimal r24_TOTAL) {
			R24_TOTAL = r24_TOTAL;
		}

		public String getR25_SENIOR_MANAGEMENT_COMPENSATION() {
			return R25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR25_SENIOR_MANAGEMENT_COMPENSATION(String r25_SENIOR_MANAGEMENT_COMPENSATION) {
			R25_SENIOR_MANAGEMENT_COMPENSATION = r25_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR25_LOCAL() {
			return R25_LOCAL;
		}

		public void setR25_LOCAL(BigDecimal r25_LOCAL) {
			R25_LOCAL = r25_LOCAL;
		}

		public BigDecimal getR25_EXPARIATES() {
			return R25_EXPARIATES;
		}

		public void setR25_EXPARIATES(BigDecimal r25_EXPARIATES) {
			R25_EXPARIATES = r25_EXPARIATES;
		}

		public BigDecimal getR25_TOTAL() {
			return R25_TOTAL;
		}

		public void setR25_TOTAL(BigDecimal r25_TOTAL) {
			R25_TOTAL = r25_TOTAL;
		}

		public String getR26_SENIOR_MANAGEMENT_COMPENSATION() {
			return R26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR26_SENIOR_MANAGEMENT_COMPENSATION(String r26_SENIOR_MANAGEMENT_COMPENSATION) {
			R26_SENIOR_MANAGEMENT_COMPENSATION = r26_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR26_LOCAL() {
			return R26_LOCAL;
		}

		public void setR26_LOCAL(BigDecimal r26_LOCAL) {
			R26_LOCAL = r26_LOCAL;
		}

		public BigDecimal getR26_EXPARIATES() {
			return R26_EXPARIATES;
		}

		public void setR26_EXPARIATES(BigDecimal r26_EXPARIATES) {
			R26_EXPARIATES = r26_EXPARIATES;
		}

		public BigDecimal getR26_TOTAL() {
			return R26_TOTAL;
		}

		public void setR26_TOTAL(BigDecimal r26_TOTAL) {
			R26_TOTAL = r26_TOTAL;
		}

		public String getR27_SENIOR_MANAGEMENT_COMPENSATION() {
			return R27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR27_SENIOR_MANAGEMENT_COMPENSATION(String r27_SENIOR_MANAGEMENT_COMPENSATION) {
			R27_SENIOR_MANAGEMENT_COMPENSATION = r27_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR27_LOCAL() {
			return R27_LOCAL;
		}

		public void setR27_LOCAL(BigDecimal r27_LOCAL) {
			R27_LOCAL = r27_LOCAL;
		}

		public BigDecimal getR27_EXPARIATES() {
			return R27_EXPARIATES;
		}

		public void setR27_EXPARIATES(BigDecimal r27_EXPARIATES) {
			R27_EXPARIATES = r27_EXPARIATES;
		}

		public BigDecimal getR27_TOTAL() {
			return R27_TOTAL;
		}

		public void setR27_TOTAL(BigDecimal r27_TOTAL) {
			R27_TOTAL = r27_TOTAL;
		}

		public String getR28_SENIOR_MANAGEMENT_COMPENSATION() {
			return R28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public void setR28_SENIOR_MANAGEMENT_COMPENSATION(String r28_SENIOR_MANAGEMENT_COMPENSATION) {
			R28_SENIOR_MANAGEMENT_COMPENSATION = r28_SENIOR_MANAGEMENT_COMPENSATION;
		}

		public BigDecimal getR28_LOCAL() {
			return R28_LOCAL;
		}

		public void setR28_LOCAL(BigDecimal r28_LOCAL) {
			R28_LOCAL = r28_LOCAL;
		}

		public BigDecimal getR28_EXPARIATES() {
			return R28_EXPARIATES;
		}

		public void setR28_EXPARIATES(BigDecimal r28_EXPARIATES) {
			R28_EXPARIATES = r28_EXPARIATES;
		}

		public BigDecimal getR28_TOTAL() {
			return R28_TOTAL;
		}

		public void setR28_TOTAL(BigDecimal r28_TOTAL) {
			R28_TOTAL = r28_TOTAL;
		}

		public String getR33_STAFF_LOANS() {
			return R33_STAFF_LOANS;
		}

		public void setR33_STAFF_LOANS(String r33_STAFF_LOANS) {
			R33_STAFF_LOANS = r33_STAFF_LOANS;
		}

		public BigDecimal getR33_ORIGINAL_AMT() {
			return R33_ORIGINAL_AMT;
		}

		public void setR33_ORIGINAL_AMT(BigDecimal r33_ORIGINAL_AMT) {
			R33_ORIGINAL_AMT = r33_ORIGINAL_AMT;
		}

		public BigDecimal getR33_BALANCE_OUTSTANDING() {
			return R33_BALANCE_OUTSTANDING;
		}

		public void setR33_BALANCE_OUTSTANDING(BigDecimal r33_BALANCE_OUTSTANDING) {
			R33_BALANCE_OUTSTANDING = r33_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR33_NO_OF_ACS() {
			return R33_NO_OF_ACS;
		}

		public void setR33_NO_OF_ACS(BigDecimal r33_NO_OF_ACS) {
			R33_NO_OF_ACS = r33_NO_OF_ACS;
		}

		public BigDecimal getR33_INTEREST_RATE() {
			return R33_INTEREST_RATE;
		}

		public void setR33_INTEREST_RATE(BigDecimal r33_INTEREST_RATE) {
			R33_INTEREST_RATE = r33_INTEREST_RATE;
		}

		public String getR34_STAFF_LOANS() {
			return R34_STAFF_LOANS;
		}

		public void setR34_STAFF_LOANS(String r34_STAFF_LOANS) {
			R34_STAFF_LOANS = r34_STAFF_LOANS;
		}

		public BigDecimal getR34_ORIGINAL_AMT() {
			return R34_ORIGINAL_AMT;
		}

		public void setR34_ORIGINAL_AMT(BigDecimal r34_ORIGINAL_AMT) {
			R34_ORIGINAL_AMT = r34_ORIGINAL_AMT;
		}

		public BigDecimal getR34_BALANCE_OUTSTANDING() {
			return R34_BALANCE_OUTSTANDING;
		}

		public void setR34_BALANCE_OUTSTANDING(BigDecimal r34_BALANCE_OUTSTANDING) {
			R34_BALANCE_OUTSTANDING = r34_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR34_NO_OF_ACS() {
			return R34_NO_OF_ACS;
		}

		public void setR34_NO_OF_ACS(BigDecimal r34_NO_OF_ACS) {
			R34_NO_OF_ACS = r34_NO_OF_ACS;
		}

		public BigDecimal getR34_INTEREST_RATE() {
			return R34_INTEREST_RATE;
		}

		public void setR34_INTEREST_RATE(BigDecimal r34_INTEREST_RATE) {
			R34_INTEREST_RATE = r34_INTEREST_RATE;
		}

		public String getR35_STAFF_LOANS() {
			return R35_STAFF_LOANS;
		}

		public void setR35_STAFF_LOANS(String r35_STAFF_LOANS) {
			R35_STAFF_LOANS = r35_STAFF_LOANS;
		}

		public BigDecimal getR35_ORIGINAL_AMT() {
			return R35_ORIGINAL_AMT;
		}

		public void setR35_ORIGINAL_AMT(BigDecimal r35_ORIGINAL_AMT) {
			R35_ORIGINAL_AMT = r35_ORIGINAL_AMT;
		}

		public BigDecimal getR35_BALANCE_OUTSTANDING() {
			return R35_BALANCE_OUTSTANDING;
		}

		public void setR35_BALANCE_OUTSTANDING(BigDecimal r35_BALANCE_OUTSTANDING) {
			R35_BALANCE_OUTSTANDING = r35_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR35_NO_OF_ACS() {
			return R35_NO_OF_ACS;
		}

		public void setR35_NO_OF_ACS(BigDecimal r35_NO_OF_ACS) {
			R35_NO_OF_ACS = r35_NO_OF_ACS;
		}

		public BigDecimal getR35_INTEREST_RATE() {
			return R35_INTEREST_RATE;
		}

		public void setR35_INTEREST_RATE(BigDecimal r35_INTEREST_RATE) {
			R35_INTEREST_RATE = r35_INTEREST_RATE;
		}

		public String getR36_STAFF_LOANS() {
			return R36_STAFF_LOANS;
		}

		public void setR36_STAFF_LOANS(String r36_STAFF_LOANS) {
			R36_STAFF_LOANS = r36_STAFF_LOANS;
		}

		public BigDecimal getR36_ORIGINAL_AMT() {
			return R36_ORIGINAL_AMT;
		}

		public void setR36_ORIGINAL_AMT(BigDecimal r36_ORIGINAL_AMT) {
			R36_ORIGINAL_AMT = r36_ORIGINAL_AMT;
		}

		public BigDecimal getR36_BALANCE_OUTSTANDING() {
			return R36_BALANCE_OUTSTANDING;
		}

		public void setR36_BALANCE_OUTSTANDING(BigDecimal r36_BALANCE_OUTSTANDING) {
			R36_BALANCE_OUTSTANDING = r36_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR36_NO_OF_ACS() {
			return R36_NO_OF_ACS;
		}

		public void setR36_NO_OF_ACS(BigDecimal r36_NO_OF_ACS) {
			R36_NO_OF_ACS = r36_NO_OF_ACS;
		}

		public BigDecimal getR36_INTEREST_RATE() {
			return R36_INTEREST_RATE;
		}

		public void setR36_INTEREST_RATE(BigDecimal r36_INTEREST_RATE) {
			R36_INTEREST_RATE = r36_INTEREST_RATE;
		}

		public String getR37_STAFF_LOANS() {
			return R37_STAFF_LOANS;
		}

		public void setR37_STAFF_LOANS(String r37_STAFF_LOANS) {
			R37_STAFF_LOANS = r37_STAFF_LOANS;
		}

		public BigDecimal getR37_ORIGINAL_AMT() {
			return R37_ORIGINAL_AMT;
		}

		public void setR37_ORIGINAL_AMT(BigDecimal r37_ORIGINAL_AMT) {
			R37_ORIGINAL_AMT = r37_ORIGINAL_AMT;
		}

		public BigDecimal getR37_BALANCE_OUTSTANDING() {
			return R37_BALANCE_OUTSTANDING;
		}

		public void setR37_BALANCE_OUTSTANDING(BigDecimal r37_BALANCE_OUTSTANDING) {
			R37_BALANCE_OUTSTANDING = r37_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR37_NO_OF_ACS() {
			return R37_NO_OF_ACS;
		}

		public void setR37_NO_OF_ACS(BigDecimal r37_NO_OF_ACS) {
			R37_NO_OF_ACS = r37_NO_OF_ACS;
		}

		public BigDecimal getR37_INTEREST_RATE() {
			return R37_INTEREST_RATE;
		}

		public void setR37_INTEREST_RATE(BigDecimal r37_INTEREST_RATE) {
			R37_INTEREST_RATE = r37_INTEREST_RATE;
		}

		public String getR38_STAFF_LOANS() {
			return R38_STAFF_LOANS;
		}

		public void setR38_STAFF_LOANS(String r38_STAFF_LOANS) {
			R38_STAFF_LOANS = r38_STAFF_LOANS;
		}

		public BigDecimal getR38_ORIGINAL_AMT() {
			return R38_ORIGINAL_AMT;
		}

		public void setR38_ORIGINAL_AMT(BigDecimal r38_ORIGINAL_AMT) {
			R38_ORIGINAL_AMT = r38_ORIGINAL_AMT;
		}

		public BigDecimal getR38_BALANCE_OUTSTANDING() {
			return R38_BALANCE_OUTSTANDING;
		}

		public void setR38_BALANCE_OUTSTANDING(BigDecimal r38_BALANCE_OUTSTANDING) {
			R38_BALANCE_OUTSTANDING = r38_BALANCE_OUTSTANDING;
		}

		public BigDecimal getR38_NO_OF_ACS() {
			return R38_NO_OF_ACS;
		}

		public void setR38_NO_OF_ACS(BigDecimal r38_NO_OF_ACS) {
			R38_NO_OF_ACS = r38_NO_OF_ACS;
		}

		public BigDecimal getR38_INTEREST_RATE() {
			return R38_INTEREST_RATE;
		}

		public void setR38_INTEREST_RATE(BigDecimal r38_INTEREST_RATE) {
			R38_INTEREST_RATE = r38_INTEREST_RATE;
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

	}

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getQ_STAFFView(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version, HttpServletRequest req1, Model md) {

		ModelAndView mv = new ModelAndView();

		String userid = (String) req1.getSession().getAttribute("USERID");
		System.out.println("User Id Maker and Checker: " + userid);
		String role = userProfileRep.getUserRole(userid);
		md.addAttribute("role", role);
		System.out.println("Role: " + role);

		System.out.println("Q_STAFF View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);
		System.out.println("DtlType = " + dtltype);

		try {

			Date dt = dateformat.parse(todate);
			if ("detail".equalsIgnoreCase(dtltype)) {

				// ARCHIVAL DETAIL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Archival_Detail_Entity> T1Master = getDetaildatabydateListarchival(dt, version);

					System.out.println("Archival Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// RESUB DETAIL
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Resub_Detail_Entity> T1Master = getResubDetaildatabydateList(dt, version);

					System.out.println("Resub Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}

				// NORMAL DETAIL
				else {

					List<Q_STAFF_Detail_Entity> T1Master = getDetaildatabydateList(dt);

					System.out.println("Normal Detail Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
					mv.addObject("displaymode", "detail");
				}
			} else {

				// ARCHIVAL SUMMARY
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Archival_Summary_Entity> T1Master = getDataByDateListArchival(dt, version);

					System.out.println("Archival Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// RESUB SUMMARY
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<Q_STAFF_Resub_Summary_Entity> T1Master = getResubSummarydatabydateListarchival(dt, version);

					System.out.println("Resub Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				// NORMAL SUMMARY
				else {

					List<Q_STAFF_Summary_Entity> T1Master = getSummaryDataByDate(dt);

					System.out.println("Normal Summary Size = " + T1Master.size());

					mv.addObject("reportsummary", T1Master);
				}

				mv.addObject("displaymode", "summary");
			}

			mv.addObject("report_date", dateformat.format(dt));

		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/Q_STAFF");

		System.out.println("View Loaded : " + mv.getViewName());

		return mv;
	}

// Archival View
	public List<Object[]> getQ_STAFFArchival() {

		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<Q_STAFF_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_STAFF_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");

				Q_STAFF_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest archival version: " + first.getReport_version());

			} else {

				System.out.println("No archival data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_STAFF Archival data: " + e.getMessage());

			e.printStackTrace();
		}

		return archivalList;
	}

	@Transactional
	public void updateReport(Q_STAFF_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_STAFF Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_STAFF_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_STAFF_Summary_Entity oldcopy = new Q_STAFF_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "STAFF_COMPLEMENT", "LOCAL", "EXPARIATES", "TOTAL" };

		try {

			for (int i = 9; i <= 15; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_STAFF_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_STAFF_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_STAFF_DETAILTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(detailSql, value, updatedEntity.getReport_date());

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Audit only if changes found
			String changes = auditService.getChanges(oldcopy, existingSummary);

			if (!changes.isEmpty()) {

				auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
						"Q_STAFF Summary Screen", "BRRS_Q_STAFF_SUMMARY");
			}

			System.out.println("Q_STAFF Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_STAFF fields", e);
		}
	}

	@Transactional
	public void updateReport2(Q_STAFF_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_STAFF Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_STAFF_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_STAFF_Summary_Entity oldcopy = new Q_STAFF_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };

		try {

			for (int i = 21; i <= 28; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_STAFF_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_STAFF_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_STAFF_DETAILTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(detailSql, value, updatedEntity.getReport_date());

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Audit only if changes found
			String changes = auditService.getChanges(oldcopy, existingSummary);

			if (!changes.isEmpty()) {

				auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
						"Q_STAFF Summary Screen", "BRRS_Q_STAFF_SUMMARY");
			}

			System.out.println("Q_STAFF Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_STAFF fields", e);
		}
	}

	@Transactional
	public void updateReport3(Q_STAFF_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_STAFF Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Fetch existing summary record for audit
		Q_STAFF_Summary_Entity existingSummary = findByReportDate(updatedEntity.getReport_date());

		if (existingSummary == null) {
			throw new RuntimeException("Record not found for REPORT_DATE : " + updatedEntity.getReport_date());
		}

		// Audit old copy
		Q_STAFF_Summary_Entity oldcopy = new Q_STAFF_Summary_Entity();

		BeanUtils.copyProperties(existingSummary, oldcopy);

		String[] fields = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS", "INTEREST_RATE" };

		try {

			for (int i = 33; i <= 38; i++) {

				for (String field : fields) {

					String getterName = "getR" + i + "_" + field;
					String setterName = "setR" + i + "_" + field;
					String columnName = "R" + i + "_" + field;

					try {

						Method getter = Q_STAFF_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						if (value == null) {
							continue;
						}

						// Update existing object for audit
						Method setter = Q_STAFF_Summary_Entity.class.getMethod(setterName, getter.getReturnType());

						setter.invoke(existingSummary, value);

						String summarySql = "UPDATE BRRS_Q_STAFF_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(summarySql, value, updatedEntity.getReport_date());

						String detailSql = "UPDATE BRRS_Q_STAFF_DETAILTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(detailSql, value, updatedEntity.getReport_date());

					} catch (NoSuchMethodException e) {
						continue;
					}
				}
			}

			// Audit only if changes found
			String changes = auditService.getChanges(oldcopy, existingSummary);

			if (!changes.isEmpty()) {

				auditService.compareEntitiesmanual(oldcopy, existingSummary, updatedEntity.getReport_date().toString(),
						"Q_STAFF Summary Screen", "BRRS_Q_STAFF_SUMMARY");
			}

			System.out.println("Q_STAFF Summary & Detail Update Completed");

		} catch (Exception e) {

			throw new RuntimeException("Error while updating Q_STAFF fields", e);
		}
	}

	public List<Object[]> getQ_STAFFResub() {

		List<Object[]> resubList = new ArrayList<>();

		try {

			List<Q_STAFF_Archival_Summary_Entity> repoData = getarchivaldatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {

				for (Q_STAFF_Archival_Summary_Entity entity : repoData) {

					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getReportResubDate() };

					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " resub records");

				Q_STAFF_Archival_Summary_Entity first = repoData.get(0);

				System.out.println("Latest resub version : " + first.getReport_version());

			} else {

				System.out.println("No resub data found.");
			}

		} catch (Exception e) {

			System.err.println("Error fetching Q_STAFF Resub data : " + e.getMessage());

			e.printStackTrace();
		}

		return resubList;
	}

	@Transactional
	public void updateResubReport(Q_STAFF_Resub_Summary_Entity updatedEntity) {

		System.out.println("Came to Q_STAFF Resub Update");

		Date reportDate = updatedEntity.getReport_date();

		BigDecimal maxVersion = findMaxVersion(reportDate);

		if (maxVersion == null) {
			throw new RuntimeException("No record found for REPORT_DATE : " + reportDate);
		}

		BigDecimal newVersion = maxVersion.add(BigDecimal.ONE);

		Date now = new Date();

		try {

			Q_STAFF_Resub_Summary_Entity resubSummary = new Q_STAFF_Resub_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, resubSummary);

			resubSummary.setReport_date(reportDate);
			resubSummary.setReport_version(newVersion);
			resubSummary.setReportResubDate(now);

			Q_STAFF_Resub_Detail_Entity resubDetail = new Q_STAFF_Resub_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, resubDetail);

			resubDetail.setReport_date(reportDate);
			resubDetail.setReport_version(newVersion);
			resubDetail.setReportResubDate(now);

			Q_STAFF_Archival_Summary_Entity archivalSummary = new Q_STAFF_Archival_Summary_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalSummary);

			archivalSummary.setReport_date(reportDate);
			archivalSummary.setReport_version(newVersion);
			archivalSummary.setReportResubDate(now);

			Q_STAFF_Archival_Detail_Entity archivalDetail = new Q_STAFF_Archival_Detail_Entity();

			BeanUtils.copyProperties(updatedEntity, archivalDetail);

			archivalDetail.setReport_date(reportDate);
			archivalDetail.setReport_version(newVersion);
			archivalDetail.setReportResubDate(now);

			insertResubSummary(resubSummary);
			insertResubDetail(resubDetail);
			insertArchivalSummary(archivalSummary);
			insertArchivalDetail(archivalDetail);
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

			if (attrs != null) {

				HttpServletRequest request = attrs.getRequest();

				String userid = (String) request.getSession().getAttribute("USERID");

				auditService.createBusinessAudit(userid, "RESUBMIT", "Q_STAFF Resub Summary", null,
						"BRRS_Q_STAFF_RESUB_SUMMARYTABLE");
			}

			System.out.println("Q_STAFF Resub Version Created Successfully : " + newVersion);

		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("Error while creating Q_STAFF Resub Version", e);
		}
	}

	private void insertResubSummary(Q_STAFF_Resub_Summary_Entity entity) {

		try {
			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_STAFF_RESUB_SUMMARYTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 9 to 15) ---
			for (int i = 9; i <= 15; i++) {
				columns.append("r").append(i).append("_STAFF_COMPLEMENT,").append("r").append(i).append("_LOCAL,")
						.append("r").append(i).append("_EXPARIATES,").append("r").append(i).append("_TOTAL,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_STAFF_COMPLEMENT"));
				params.add(getValue(entity, "getR" + i + "_LOCAL"));
				params.add(getValue(entity, "getR" + i + "_EXPARIATES"));
				params.add(getValue(entity, "getR" + i + "_TOTAL"));
			}

			// --- 1st New Loop (i = 21 to 28) ---
			String[] fields1 = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };
			for (int i = 21; i <= 28; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 33 to 38) ---
			String[] fields2 = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS", "INTEREST_RATE" };
			for (int i = 33; i <= 38; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_STAFF RESUB SUMMARY", e);
		}
	}

	private void insertResubDetail(Q_STAFF_Resub_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_STAFF_RESUB_DETAILTABLE (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 9 to 15) ---
			for (int i = 9; i <= 15; i++) {

				columns.append("r").append(i).append("_STAFF_COMPLEMENT,").append("r").append(i).append("_LOCAL,")
						.append("r").append(i).append("_EXPARIATES,").append("r").append(i).append("_TOTAL,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_STAFF_COMPLEMENT"));
				params.add(getValue(entity, "getR" + i + "_LOCAL"));
				params.add(getValue(entity, "getR" + i + "_EXPARIATES"));
				params.add(getValue(entity, "getR" + i + "_TOTAL"));
			}

			// --- 1st New Loop (i = 21 to 28) ---
			String[] fields1 = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };
			for (int i = 21; i <= 28; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 33 to 38) ---
			String[] fields2 = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS", "INTEREST_RATE" };
			for (int i = 33; i <= 38; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_STAFF RESUB DETAIL", e);
		}
	}

	private void insertArchivalSummary(Q_STAFF_Archival_Summary_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 9 to 15) ---
			for (int i = 9; i <= 15; i++) {

				columns.append("r").append(i).append("_STAFF_COMPLEMENT,").append("r").append(i).append("_LOCAL,")
						.append("r").append(i).append("_EXPARIATES,").append("r").append(i).append("_TOTAL,");

				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_STAFF_COMPLEMENT"));
				params.add(getValue(entity, "getR" + i + "_LOCAL"));
				params.add(getValue(entity, "getR" + i + "_EXPARIATES"));
				params.add(getValue(entity, "getR" + i + "_TOTAL"));
			}

			// --- 1st New Loop (i = 21 to 28) ---
			String[] fields1 = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };
			for (int i = 21; i <= 28; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 33 to 38) ---
			String[] fields2 = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS", "INTEREST_RATE" };
			for (int i = 33; i <= 38; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_STAFF ARCHIVAL SUMMARY", e);
		}
	}

	private void insertArchivalDetail(Q_STAFF_Archival_Detail_Entity entity) {

		try {

			StringBuilder columns = new StringBuilder(
					"INSERT INTO BRRS_Q_STAFF_ARCHIVALTABLE_DETAIL (REPORT_DATE,REPORT_VERSION,REPORT_RESUBDATE,");

			StringBuilder values = new StringBuilder(" VALUES (?,?,?,");

			List<Object> params = new ArrayList<>();

			params.add(entity.getReport_date());
			params.add(entity.getReport_version());
			params.add(entity.getReportResubDate());

			// --- Original Loop (i = 9 to 15) ---
			for (int i = 9; i <= 15; i++) {

				columns.append("r").append(i).append("_STAFF_COMPLEMENT,").append("r").append(i).append("_LOCAL,")
						.append("r").append(i).append("_EXPARIATES,").append("r").append(i).append("_TOTAL,");
				for (int j = 1; j <= 4; j++) {
					values.append("?,");
				}

				params.add(getValue(entity, "getR" + i + "_STAFF_COMPLEMENT"));
				params.add(getValue(entity, "getR" + i + "_LOCAL"));
				params.add(getValue(entity, "getR" + i + "_EXPARIATES"));
				params.add(getValue(entity, "getR" + i + "_TOTAL"));
			}

			// --- 1st New Loop (i = 21 to 28) ---
			String[] fields1 = { "SENIOR_MANAGEMENT_COMPENSATION", "LOCAL", "EXPARIATES", "TOTAL" };
			for (int i = 21; i <= 28; i++) {
				for (String field : fields1) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// --- 2nd New Loop (i = 33 to 38) ---
			String[] fields2 = { "STAFF_LOANS", "ORIGINAL_AMT", "BALANCE_OUTSTANDING", "NO_OF_ACS", "INTEREST_RATE" };
			for (int i = 33; i <= 38; i++) {
				for (String field : fields2) {
					columns.append("r").append(i).append("_").append(field).append(",");
					values.append("?,");
					params.add(getValue(entity, "getR" + i + "_" + field));
				}
			}

			// Clean up trailing commas and close brackets
			columns.deleteCharAt(columns.length() - 1);
			values.deleteCharAt(values.length() - 1);

			columns.append(")");
			values.append(")");

			jdbcTemplate.update(columns.toString() + values.toString(), params.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error inserting Q_STAFF ARCHIVAL DETAIL", e);
		}
	}

	private Object getValue(Object obj, String methodName) {
		try {
			return obj.getClass().getMethod(methodName).invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

// Summary EXCEL  FORMAT
	public byte[] getBRRS_Q_STAFFExcel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, String format, BigDecimal version) throws Exception {
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
				return getExcelQ_STAFFARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_STAFFResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, format,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {

			if ("email".equalsIgnoreCase(format) && version == null) {
				logger.info("Got format as Email");
				logger.info("Service: Generating Email report for version {}", version);
				return BRRS_Q_STAFFEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
			} else {

				// Fetch data

				List<Q_STAFF_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

				if (dataList.isEmpty()) {
					logger.warn("Service: No data found for BRRS_Q_STAFF report. Returning empty result.");
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
							Q_STAFF_Summary_Entity record = dataList.get(i);
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
							row = sheet.getRow(8);

// R9 Col B
							Cell R9cell1 = row.createCell(1);
							if (record.getR9_LOCAL() != null) {
								R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
								R9cell1.setCellStyle(numberStyle);
							} else {
								R9cell1.setCellValue("");
								R9cell1.setCellStyle(textStyle);
							}

							// R9 Col C
							Cell R9cell2 = row.createCell(2);
							if (record.getR9_EXPARIATES() != null) {
								R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
								R9cell2.setCellStyle(numberStyle);
							} else {
								R9cell2.setCellValue("");
								R9cell2.setCellStyle(textStyle);
							}
							// R10 Col B
							row = sheet.getRow(9);
							// R10 Col B
							Cell R10cell1 = row.createCell(1);
							if (record.getR10_LOCAL() != null) {
								R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
								R10cell1.setCellStyle(numberStyle);
							} else {
								R10cell1.setCellValue("");
								R10cell1.setCellStyle(textStyle);
							}

							// R10 Col C
							Cell R10cell2 = row.createCell(2);
							if (record.getR10_EXPARIATES() != null) {
								R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
								R10cell2.setCellStyle(numberStyle);
							} else {
								R10cell2.setCellValue("");
								R10cell2.setCellStyle(textStyle);
							}
							// R11 Col B
							row = sheet.getRow(10);
							// R11 Col B
							Cell R11cell1 = row.createCell(1);
							if (record.getR11_LOCAL() != null) {
								R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
								R11cell1.setCellStyle(numberStyle);
							} else {
								R11cell1.setCellValue("");
								R11cell1.setCellStyle(textStyle);
							}

							// R11 Col C
							Cell R11cell2 = row.createCell(2);
							if (record.getR11_EXPARIATES() != null) {
								R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
								R11cell2.setCellStyle(numberStyle);
							} else {
								R11cell2.setCellValue("");
								R11cell2.setCellStyle(textStyle);
							}
							// R12 Col B
							row = sheet.getRow(11);

							Cell R12cell1 = row.createCell(1);
							if (record.getR12_LOCAL() != null) {
								R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
								R12cell1.setCellStyle(numberStyle);
							} else {
								R12cell1.setCellValue("");
								R12cell1.setCellStyle(textStyle);
							}

							// R12 Col C
							Cell R12cell2 = row.createCell(2);
							if (record.getR12_EXPARIATES() != null) {
								R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
								R12cell2.setCellStyle(numberStyle);
							} else {
								R12cell2.setCellValue("");
								R12cell2.setCellStyle(textStyle);
							}
							// R13 Col B
							row = sheet.getRow(12);

							Cell R13cell1 = row.createCell(1);
							if (record.getR13_LOCAL() != null) {
								R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
								R13cell1.setCellStyle(numberStyle);
							} else {
								R13cell1.setCellValue("");
								R13cell1.setCellStyle(textStyle);
							}

							// R13 Col C
							Cell R13cell2 = row.createCell(2);
							if (record.getR13_EXPARIATES() != null) {
								R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
								R13cell2.setCellStyle(numberStyle);
							} else {
								R13cell2.setCellValue("");
								R13cell2.setCellStyle(textStyle);
							}
							// R14 Col B
							row = sheet.getRow(13);
							Cell R14cell1 = row.createCell(1);
							if (record.getR14_LOCAL() != null) {
								R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
								R14cell1.setCellStyle(numberStyle);
							} else {
								R14cell1.setCellValue("");
								R14cell1.setCellStyle(textStyle);
							}

							// R14 Col C
							Cell R14cell2 = row.createCell(2);
							if (record.getR14_EXPARIATES() != null) {
								R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
								R14cell2.setCellStyle(numberStyle);
							} else {
								R14cell2.setCellValue("");
								R14cell2.setCellStyle(textStyle);
							}

							// TABLE 2
							// R21 Col B
							row = sheet.getRow(20);
							Cell R21cell1 = row.createCell(1);
							if (record.getR21_LOCAL() != null) {
								R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
								R21cell1.setCellStyle(numberStyle);
							} else {
								R21cell1.setCellValue("");
								R21cell1.setCellStyle(textStyle);
							}
							// R21 COL C
							Cell R21cell2 = row.createCell(2);
							if (record.getR21_EXPARIATES() != null) {
								R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
								R21cell2.setCellStyle(numberStyle);
							} else {
								R21cell2.setCellValue("");
								R21cell2.setCellStyle(textStyle);
							}
							// R22 Col B
							row = sheet.getRow(21);
							Cell R22cell1 = row.createCell(1);
							if (record.getR22_LOCAL() != null) {
								R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
								R22cell1.setCellStyle(numberStyle);
							} else {
								R22cell1.setCellValue("");
								R22cell1.setCellStyle(textStyle);
							}

							// R22 Col C
							Cell R22cell2 = row.createCell(2);
							if (record.getR22_EXPARIATES() != null) {
								R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
								R22cell2.setCellStyle(numberStyle);
							} else {
								R22cell2.setCellValue("");
								R22cell2.setCellStyle(textStyle);
							}
							// R23 Col B
							row = sheet.getRow(22);
							// R23 Col B
							Cell R23cell1 = row.createCell(1);
							if (record.getR23_LOCAL() != null) {
								R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
								R23cell1.setCellStyle(numberStyle);
							} else {
								R23cell1.setCellValue("");
								R23cell1.setCellStyle(textStyle);
							}

							// R23 Col C
							Cell R23cell2 = row.createCell(2);
							if (record.getR23_EXPARIATES() != null) {
								R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
								R23cell2.setCellStyle(numberStyle);
							} else {
								R23cell2.setCellValue("");
								R23cell2.setCellStyle(textStyle);
							}
							// R24 Col B
							row = sheet.getRow(23);
							// R24 Col B
							Cell R24cell1 = row.createCell(1);
							if (record.getR24_LOCAL() != null) {
								R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
								R24cell1.setCellStyle(numberStyle);
							} else {
								R24cell1.setCellValue("");
								R24cell1.setCellStyle(textStyle);
							}

							// R24 Col C
							Cell R24cell2 = row.createCell(2);
							if (record.getR24_EXPARIATES() != null) {
								R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
								R24cell2.setCellStyle(numberStyle);
							} else {
								R24cell2.setCellValue("");
								R24cell2.setCellStyle(textStyle);
							}
							// R25 Col B
							row = sheet.getRow(24);
							// R25 Col B
							Cell R25cell1 = row.createCell(1);
							if (record.getR25_LOCAL() != null) {
								R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
								R25cell1.setCellStyle(numberStyle);
							} else {
								R25cell1.setCellValue("");
								R25cell1.setCellStyle(textStyle);
							}

							// R25 Col C
							Cell R25cell2 = row.createCell(2);
							if (record.getR25_EXPARIATES() != null) {
								R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
								R25cell2.setCellStyle(numberStyle);
							} else {
								R25cell2.setCellValue("");
								R25cell2.setCellStyle(textStyle);
							}
							// R26 Col B
							row = sheet.getRow(25);
							// R26 Col B
							Cell R26cell1 = row.createCell(1);
							if (record.getR26_LOCAL() != null) {
								R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
								R26cell1.setCellStyle(numberStyle);
							} else {
								R26cell1.setCellValue("");
								R26cell1.setCellStyle(textStyle);
							}

							// R26 Col C
							Cell R26cell2 = row.createCell(2);
							if (record.getR26_EXPARIATES() != null) {
								R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
								R26cell2.setCellStyle(numberStyle);
							} else {
								R26cell2.setCellValue("");
								R26cell2.setCellStyle(textStyle);
							}
							// R27 Col B
							row = sheet.getRow(26);
							// R27 Col B
							Cell R27cell1 = row.createCell(1);
							if (record.getR27_LOCAL() != null) {
								R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
								R27cell1.setCellStyle(numberStyle);
							} else {
								R27cell1.setCellValue("");
								R27cell1.setCellStyle(textStyle);
							}

							// R27 Col C
							Cell R27cell2 = row.createCell(2);
							if (record.getR27_EXPARIATES() != null) {
								R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
								R27cell2.setCellStyle(numberStyle);
							} else {
								R27cell2.setCellValue("");
								R27cell2.setCellStyle(textStyle);
							}
							// TABLE 3
							// R33 Col B
							row = sheet.getRow(32);
							Cell R33cell1 = row.createCell(1);
							if (record.getR33_ORIGINAL_AMT() != null) {
								R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
								R33cell1.setCellStyle(numberStyle);
							} else {
								R33cell1.setCellValue("");
								R33cell1.setCellStyle(textStyle);
							}

							// R33 Col C
							Cell R33cell2 = row.createCell(2);
							if (record.getR33_BALANCE_OUTSTANDING() != null) {
								R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
								R33cell2.setCellStyle(numberStyle);
							} else {
								R33cell2.setCellValue("");
								R33cell2.setCellStyle(textStyle);
							}
							// R33 Col D
							Cell R33cell3 = row.createCell(3);
							if (record.getR33_NO_OF_ACS() != null) {
								R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
								R33cell3.setCellStyle(numberStyle);
							} else {
								R33cell3.setCellValue("");
								R33cell3.setCellStyle(textStyle);
							}

							// R33 Col E
							Cell R33cell4 = row.createCell(4);
							if (record.getR33_INTEREST_RATE() != null) {
								R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
								R33cell4.setCellStyle(numberStyle);
							} else {
								R33cell4.setCellValue("");
								R33cell4.setCellStyle(textStyle);
							}
							// R34 Col B
							row = sheet.getRow(33);
							Cell R34cell1 = row.createCell(1);
							if (record.getR34_ORIGINAL_AMT() != null) {
								R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
								R34cell1.setCellStyle(numberStyle);
							} else {
								R34cell1.setCellValue("");
								R34cell1.setCellStyle(textStyle);
							}

							// R34 Col C
							Cell R34cell2 = row.createCell(2);
							if (record.getR34_BALANCE_OUTSTANDING() != null) {
								R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
								R34cell2.setCellStyle(numberStyle);
							} else {
								R34cell2.setCellValue("");
								R34cell2.setCellStyle(textStyle);
							}
							// R34 Col D
							Cell R34cell3 = row.createCell(3);
							if (record.getR34_NO_OF_ACS() != null) {
								R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
								R34cell3.setCellStyle(numberStyle);
							} else {
								R34cell3.setCellValue("");
								R34cell3.setCellStyle(textStyle);
							}

							// R34 Col E
							Cell R34cell4 = row.createCell(4);
							if (record.getR34_INTEREST_RATE() != null) {
								R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
								R34cell4.setCellStyle(numberStyle);
							} else {
								R34cell4.setCellValue("");
								R34cell4.setCellStyle(textStyle);
							}
							// R35 Col B
							row = sheet.getRow(34);

							Cell R35cell1 = row.createCell(1);
							if (record.getR35_ORIGINAL_AMT() != null) {
								R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
								R35cell1.setCellStyle(numberStyle);
							} else {
								R35cell1.setCellValue("");
								R35cell1.setCellStyle(textStyle);
							}

							// R35 Col C
							Cell R35cell2 = row.createCell(2);
							if (record.getR35_BALANCE_OUTSTANDING() != null) {
								R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
								R35cell2.setCellStyle(numberStyle);
							} else {
								R35cell2.setCellValue("");
								R35cell2.setCellStyle(textStyle);
							}
							// R35 Col D
							Cell R35cell3 = row.createCell(3);
							if (record.getR35_NO_OF_ACS() != null) {
								R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
								R35cell3.setCellStyle(numberStyle);
							} else {
								R35cell3.setCellValue("");
								R35cell3.setCellStyle(textStyle);
							}

							// R35 Col E
							Cell R35cell4 = row.createCell(4);
							if (record.getR35_INTEREST_RATE() != null) {
								R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
								R35cell4.setCellStyle(numberStyle);
							} else {
								R35cell4.setCellValue("");
								R35cell4.setCellStyle(textStyle);
							}
							// R36 Col B
							row = sheet.getRow(35);
							Cell R36cell1 = row.createCell(1);
							if (record.getR36_ORIGINAL_AMT() != null) {
								R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
								R36cell1.setCellStyle(numberStyle);
							} else {
								R36cell1.setCellValue("");
								R36cell1.setCellStyle(textStyle);
							}

							// R36 Col C
							Cell R36cell2 = row.createCell(2);
							if (record.getR36_BALANCE_OUTSTANDING() != null) {
								R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
								R36cell2.setCellStyle(numberStyle);
							} else {
								R36cell2.setCellValue("");
								R36cell2.setCellStyle(textStyle);
							}
							// R36 Col D
							Cell R36cell3 = row.createCell(3);
							if (record.getR36_NO_OF_ACS() != null) {
								R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
								R36cell3.setCellStyle(numberStyle);
							} else {
								R36cell3.setCellValue("");
								R36cell3.setCellStyle(textStyle);
							}

							// R36 Col E
							Cell R36cell4 = row.createCell(4);
							if (record.getR36_INTEREST_RATE() != null) {
								R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
								R36cell4.setCellStyle(numberStyle);
							} else {
								R36cell4.setCellValue("");
								R36cell4.setCellStyle(textStyle);
							}
							// R37 Col B
							row = sheet.getRow(36);
							Cell R37cell1 = row.createCell(1);
							if (record.getR37_ORIGINAL_AMT() != null) {
								R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
								R37cell1.setCellStyle(numberStyle);
							} else {
								R37cell1.setCellValue("");
								R37cell1.setCellStyle(textStyle);
							}

							// R37 Col C
							Cell R37cell2 = row.createCell(2);
							if (record.getR37_BALANCE_OUTSTANDING() != null) {
								R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
								R37cell2.setCellStyle(numberStyle);
							} else {
								R37cell2.setCellValue("");
								R37cell2.setCellStyle(textStyle);
							}
							// R37 Col D
							Cell R37cell3 = row.createCell(3);
							if (record.getR37_NO_OF_ACS() != null) {
								R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
								R37cell3.setCellStyle(numberStyle);
							} else {
								R37cell3.setCellValue("");
								R37cell3.setCellStyle(textStyle);
							}

							// R37 Col E
							Cell R37cell4 = row.createCell(4);
							if (record.getR37_INTEREST_RATE() != null) {
								R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
								R37cell4.setCellStyle(numberStyle);
							} else {
								R37cell4.setCellValue("");
								R37cell4.setCellStyle(textStyle);
							}
							row = sheet.getRow(37);
							// R37 Col E
							Cell R38cell4 = row.createCell(4);
							if (record.getR38_INTEREST_RATE() != null) {
								R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
								R38cell4.setCellStyle(numberStyle);
							} else {
								R38cell4.setCellValue("");
								R38cell4.setCellStyle(textStyle);
							}

						}
						workbook.setForceFormulaRecalculation(true);
					} else {

					}

					// Write the final workbook content to the in-memory stream.
					workbook.write(out);

					logger.info("Service: Excel data successfully written to memory buffer ({} bytes).", out.size());

					// audit service

					ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes();
					if (attrs != null) {
						HttpServletRequest request = attrs.getRequest();
						String userid = (String) request.getSession().getAttribute("USERID");
						auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF SUMMARY", null,
								"BRRS_Q_STAFF_SUMMARYTABLE");
					}

					return out.toByteArray();
				}
			}
		}
	}

// Summary EXCEL  EMAIL
// Normal Email Excel
	public byte[] BRRS_Q_STAFFEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Email Excel generation process in memory.");

		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_STAFFARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else if ("RESUB".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_STAFFEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		} else {
			List<Q_STAFF_Summary_Entity> dataList = getSummaryDataByDate(dateformat.parse(todate));

			if (dataList.isEmpty()) {
				logger.warn("Service: No data found for BRRS_Q_STAFF report. Returning empty result.");
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
						Q_STAFF_Summary_Entity record = dataList.get(i);
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
						row = sheet.getRow(8);
						// R9 Col B
						Cell R9cell1 = row.createCell(3);
						if (record.getR9_LOCAL() != null) {
							R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
							R9cell1.setCellStyle(numberStyle);
						} else {
							R9cell1.setCellValue("");
							R9cell1.setCellStyle(textStyle);
						}

						// R9 Col C
						Cell R9cell2 = row.createCell(4);
						if (record.getR9_EXPARIATES() != null) {
							R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
							R9cell2.setCellStyle(numberStyle);
						} else {
							R9cell2.setCellValue("");
							R9cell2.setCellStyle(textStyle);
						}

						// R9 Col C
						Cell R9cell3 = row.createCell(5);
						if (record.getR9_TOTAL() != null) {
							R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
							R9cell3.setCellStyle(numberStyle);
						} else {
							R9cell3.setCellValue("");
							R9cell3.setCellStyle(textStyle);
						}
						// R10 Col B
						row = sheet.getRow(9);
						// R10 Col B
						Cell R10cell1 = row.createCell(3);
						if (record.getR10_LOCAL() != null) {
							R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
							R10cell1.setCellStyle(numberStyle);
						} else {
							R10cell1.setCellValue("");
							R10cell1.setCellStyle(textStyle);
						}

						// R10 Col C
						Cell R10cell2 = row.createCell(4);
						if (record.getR10_EXPARIATES() != null) {
							R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
							R10cell2.setCellStyle(numberStyle);
						} else {
							R10cell2.setCellValue("");
							R10cell2.setCellStyle(textStyle);
						}
						Cell R10cell3 = row.createCell(5);
						if (record.getR10_TOTAL() != null) {
							R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
							R10cell3.setCellStyle(numberStyle);
						} else {
							R10cell3.setCellValue("");
							R10cell3.setCellStyle(textStyle);
						}
						// R11 Col B
						row = sheet.getRow(10);

						// R11 Col B
						Cell R11cell1 = row.createCell(3);
						if (record.getR11_LOCAL() != null) {
							R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
							R11cell1.setCellStyle(numberStyle);
						} else {
							R11cell1.setCellValue("");
							R11cell1.setCellStyle(textStyle);
						}

						// R11 Col C
						Cell R11cell2 = row.createCell(4);
						if (record.getR11_EXPARIATES() != null) {
							R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
							R11cell2.setCellStyle(numberStyle);
						} else {
							R11cell2.setCellValue("");
							R11cell2.setCellStyle(textStyle);
						}

						// R11 TOTAL
						Cell R11cell3 = row.createCell(5);
						if (record.getR11_TOTAL() != null) {
							R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
							R11cell3.setCellStyle(numberStyle);
						} else {
							R11cell3.setCellValue("");
							R11cell3.setCellStyle(textStyle);
						}
						// R12 Col B
						row = sheet.getRow(11);

						// R12 Col B
						Cell R12cell1 = row.createCell(3);
						if (record.getR12_LOCAL() != null) {
							R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
							R12cell1.setCellStyle(numberStyle);
						} else {
							R12cell1.setCellValue("");
							R12cell1.setCellStyle(textStyle);
						}

						// R12 Col C
						Cell R12cell2 = row.createCell(4);
						if (record.getR12_EXPARIATES() != null) {
							R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
							R12cell2.setCellStyle(numberStyle);
						} else {
							R12cell2.setCellValue("");
							R12cell2.setCellStyle(textStyle);
						}

						// R12 TOTAL
						Cell R12cell3 = row.createCell(5);
						if (record.getR12_TOTAL() != null) {
							R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
							R12cell3.setCellStyle(numberStyle);
						} else {
							R12cell3.setCellValue("");
							R12cell3.setCellStyle(textStyle);
						}

						// R13 Col B
						row = sheet.getRow(12);

						// R13 Col B
						Cell R13cell1 = row.createCell(3);
						if (record.getR13_LOCAL() != null) {
							R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
							R13cell1.setCellStyle(numberStyle);
						} else {
							R13cell1.setCellValue("");
							R13cell1.setCellStyle(textStyle);
						}

						// R13 Col C
						Cell R13cell2 = row.createCell(4);
						if (record.getR13_EXPARIATES() != null) {
							R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
							R13cell2.setCellStyle(numberStyle);
						} else {
							R13cell2.setCellValue("");
							R13cell2.setCellStyle(textStyle);
						}

						// R13 TOTAL
						Cell R13cell3 = row.createCell(5);
						if (record.getR13_TOTAL() != null) {
							R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
							R13cell3.setCellStyle(numberStyle);
						} else {
							R13cell3.setCellValue("");
							R13cell3.setCellStyle(textStyle);
						}

						// R14 Col B
						row = sheet.getRow(13);

						// R14 Col B
						Cell R14cell1 = row.createCell(3);
						if (record.getR14_LOCAL() != null) {
							R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
							R14cell1.setCellStyle(numberStyle);
						} else {
							R14cell1.setCellValue("");
							R14cell1.setCellStyle(textStyle);
						}

						// R14 Col C
						Cell R14cell2 = row.createCell(4);
						if (record.getR14_EXPARIATES() != null) {
							R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
							R14cell2.setCellStyle(numberStyle);
						} else {
							R14cell2.setCellValue("");
							R14cell2.setCellStyle(textStyle);
						}

						// R14 TOTAL
						Cell R14cell3 = row.createCell(5);
						if (record.getR14_TOTAL() != null) {
							R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
							R14cell3.setCellStyle(numberStyle);
						} else {
							R14cell3.setCellValue("");
							R14cell3.setCellStyle(textStyle);
						}

						// ================= R21 =================
						row = sheet.getRow(20);

						Cell R21cell1 = row.createCell(3);
						if (record.getR21_LOCAL() != null) {
							R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
							R21cell1.setCellStyle(numberStyle);
						} else {
							R21cell1.setCellValue("");
							R21cell1.setCellStyle(textStyle);
						}

						Cell R21cell2 = row.createCell(4);
						if (record.getR21_EXPARIATES() != null) {
							R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
							R21cell2.setCellStyle(numberStyle);
						} else {
							R21cell2.setCellValue("");
							R21cell2.setCellStyle(textStyle);
						}

						Cell R21cell3 = row.createCell(5);
						if (record.getR21_TOTAL() != null) {
							R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
							R21cell3.setCellStyle(numberStyle);
						} else {
							R21cell3.setCellValue("");
							R21cell3.setCellStyle(textStyle);
						}

						// ================= R22 =================
						row = sheet.getRow(21);

						Cell R22cell1 = row.createCell(3);
						if (record.getR22_LOCAL() != null) {
							R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
							R22cell1.setCellStyle(numberStyle);
						} else {
							R22cell1.setCellValue("");
							R22cell1.setCellStyle(textStyle);
						}

						Cell R22cell2 = row.createCell(4);
						if (record.getR22_EXPARIATES() != null) {
							R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
							R22cell2.setCellStyle(numberStyle);
						} else {
							R22cell2.setCellValue("");
							R22cell2.setCellStyle(textStyle);
						}

						Cell R22cell3 = row.createCell(5);
						if (record.getR22_TOTAL() != null) {
							R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
							R22cell3.setCellStyle(numberStyle);
						} else {
							R22cell3.setCellValue("");
							R22cell3.setCellStyle(textStyle);
						}

						// ================= R23 =================
						row = sheet.getRow(22);

						Cell R23cell1 = row.createCell(3);
						if (record.getR23_LOCAL() != null) {
							R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
							R23cell1.setCellStyle(numberStyle);
						} else {
							R23cell1.setCellValue("");
							R23cell1.setCellStyle(textStyle);
						}

						Cell R23cell2 = row.createCell(4);
						if (record.getR23_EXPARIATES() != null) {
							R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
							R23cell2.setCellStyle(numberStyle);
						} else {
							R23cell2.setCellValue("");
							R23cell2.setCellStyle(textStyle);
						}

						Cell R23cell3 = row.createCell(5);
						if (record.getR23_TOTAL() != null) {
							R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
							R23cell3.setCellStyle(numberStyle);
						} else {
							R23cell3.setCellValue("");
							R23cell3.setCellStyle(textStyle);
						}

						// ================= R24 =================
						row = sheet.getRow(23);

						Cell R24cell1 = row.createCell(3);
						if (record.getR24_LOCAL() != null) {
							R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
							R24cell1.setCellStyle(numberStyle);
						} else {
							R24cell1.setCellValue("");
							R24cell1.setCellStyle(textStyle);
						}

						Cell R24cell2 = row.createCell(4);
						if (record.getR24_EXPARIATES() != null) {
							R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
							R24cell2.setCellStyle(numberStyle);
						} else {
							R24cell2.setCellValue("");
							R24cell2.setCellStyle(textStyle);
						}

						Cell R24cell3 = row.createCell(5);
						if (record.getR24_TOTAL() != null) {
							R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
							R24cell3.setCellStyle(numberStyle);
						} else {
							R24cell3.setCellValue("");
							R24cell3.setCellStyle(textStyle);
						}

						// ================= R25 =================
						row = sheet.getRow(24);

						Cell R25cell1 = row.createCell(3);
						if (record.getR25_LOCAL() != null) {
							R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
							R25cell1.setCellStyle(numberStyle);
						} else {
							R25cell1.setCellValue("");
							R25cell1.setCellStyle(textStyle);
						}

						Cell R25cell2 = row.createCell(4);
						if (record.getR25_EXPARIATES() != null) {
							R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
							R25cell2.setCellStyle(numberStyle);
						} else {
							R25cell2.setCellValue("");
							R25cell2.setCellStyle(textStyle);
						}

						Cell R25cell3 = row.createCell(5);
						if (record.getR25_TOTAL() != null) {
							R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
							R25cell3.setCellStyle(numberStyle);
						} else {
							R25cell3.setCellValue("");
							R25cell3.setCellStyle(textStyle);
						}

						// ================= R26 =================
						row = sheet.getRow(25);

						Cell R26cell1 = row.createCell(3);
						if (record.getR26_LOCAL() != null) {
							R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
							R26cell1.setCellStyle(numberStyle);
						} else {
							R26cell1.setCellValue("");
							R26cell1.setCellStyle(textStyle);
						}

						Cell R26cell2 = row.createCell(4);
						if (record.getR26_EXPARIATES() != null) {
							R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
							R26cell2.setCellStyle(numberStyle);
						} else {
							R26cell2.setCellValue("");
							R26cell2.setCellStyle(textStyle);
						}

						Cell R26cell3 = row.createCell(5);
						if (record.getR26_TOTAL() != null) {
							R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
							R26cell3.setCellStyle(numberStyle);
						} else {
							R26cell3.setCellValue("");
							R26cell3.setCellStyle(textStyle);
						}

						// ================= R27 =================
						row = sheet.getRow(26);

						Cell R27cell1 = row.createCell(3);
						if (record.getR27_LOCAL() != null) {
							R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
							R27cell1.setCellStyle(numberStyle);
						} else {
							R27cell1.setCellValue("");
							R27cell1.setCellStyle(textStyle);
						}

						Cell R27cell2 = row.createCell(4);
						if (record.getR27_EXPARIATES() != null) {
							R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
							R27cell2.setCellStyle(numberStyle);
						} else {
							R27cell2.setCellValue("");
							R27cell2.setCellStyle(textStyle);
						}

						Cell R27cell3 = row.createCell(5);
						if (record.getR27_TOTAL() != null) {
							R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
							R27cell3.setCellStyle(numberStyle);
						} else {
							R27cell3.setCellValue("");
							R27cell3.setCellStyle(textStyle);
						}

						// TABLE 3
						// R33 Col B
						row = sheet.getRow(32);
						Cell R33cell1 = row.createCell(3);
						if (record.getR33_ORIGINAL_AMT() != null) {
							R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
							R33cell1.setCellStyle(numberStyle);
						} else {
							R33cell1.setCellValue("");
							R33cell1.setCellStyle(textStyle);
						}

						// R33 Col C
						Cell R33cell2 = row.createCell(4);
						if (record.getR33_BALANCE_OUTSTANDING() != null) {
							R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
							R33cell2.setCellStyle(numberStyle);
						} else {
							R33cell2.setCellValue("");
							R33cell2.setCellStyle(textStyle);
						}
						// R33 Col D
						Cell R33cell3 = row.createCell(5);
						if (record.getR33_NO_OF_ACS() != null) {
							R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
							R33cell3.setCellStyle(numberStyle);
						} else {
							R33cell3.setCellValue("");
							R33cell3.setCellStyle(textStyle);
						}

						// R33 Col E
						Cell R33cell4 = row.createCell(6);
						if (record.getR33_INTEREST_RATE() != null) {
							R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
							R33cell4.setCellStyle(numberStyle);
						} else {
							R33cell4.setCellValue("");
							R33cell4.setCellStyle(textStyle);
						}
						// R34 Col B
						row = sheet.getRow(33);
						Cell R34cell1 = row.createCell(3);
						if (record.getR34_ORIGINAL_AMT() != null) {
							R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
							R34cell1.setCellStyle(numberStyle);
						} else {
							R34cell1.setCellValue("");
							R34cell1.setCellStyle(textStyle);
						}

						// R34 Col C
						Cell R34cell2 = row.createCell(4);
						if (record.getR34_BALANCE_OUTSTANDING() != null) {
							R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
							R34cell2.setCellStyle(numberStyle);
						} else {
							R34cell2.setCellValue("");
							R34cell2.setCellStyle(textStyle);
						}
						// R34 Col D
						Cell R34cell3 = row.createCell(5);
						if (record.getR34_NO_OF_ACS() != null) {
							R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
							R34cell3.setCellStyle(numberStyle);
						} else {
							R34cell3.setCellValue("");
							R34cell3.setCellStyle(textStyle);
						}

						// R34 Col E
						Cell R34cell4 = row.createCell(6);
						if (record.getR34_INTEREST_RATE() != null) {
							R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
							R34cell4.setCellStyle(numberStyle);
						} else {
							R34cell4.setCellValue("");
							R34cell4.setCellStyle(textStyle);
						}
						// R35 Col B
						row = sheet.getRow(34);

						Cell R35cell1 = row.createCell(3);
						if (record.getR35_ORIGINAL_AMT() != null) {
							R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
							R35cell1.setCellStyle(numberStyle);
						} else {
							R35cell1.setCellValue("");
							R35cell1.setCellStyle(textStyle);
						}

						// R35 Col C
						Cell R35cell2 = row.createCell(4);
						if (record.getR35_BALANCE_OUTSTANDING() != null) {
							R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
							R35cell2.setCellStyle(numberStyle);
						} else {
							R35cell2.setCellValue("");
							R35cell2.setCellStyle(textStyle);
						}
						// R35 Col D
						Cell R35cell3 = row.createCell(5);
						if (record.getR35_NO_OF_ACS() != null) {
							R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
							R35cell3.setCellStyle(numberStyle);
						} else {
							R35cell3.setCellValue("");
							R35cell3.setCellStyle(textStyle);
						}

						// R35 Col E
						Cell R35cell4 = row.createCell(6);
						if (record.getR35_INTEREST_RATE() != null) {
							R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
							R35cell4.setCellStyle(numberStyle);
						} else {
							R35cell4.setCellValue("");
							R35cell4.setCellStyle(textStyle);
						}
						// R36 Col B
						row = sheet.getRow(35);
						Cell R36cell1 = row.createCell(3);
						if (record.getR36_ORIGINAL_AMT() != null) {
							R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
							R36cell1.setCellStyle(numberStyle);
						} else {
							R36cell1.setCellValue("");
							R36cell1.setCellStyle(textStyle);
						}

						// R36 Col C
						Cell R36cell2 = row.createCell(4);
						if (record.getR36_BALANCE_OUTSTANDING() != null) {
							R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
							R36cell2.setCellStyle(numberStyle);
						} else {
							R36cell2.setCellValue("");
							R36cell2.setCellStyle(textStyle);
						}
						// R36 Col D
						Cell R36cell3 = row.createCell(5);
						if (record.getR36_NO_OF_ACS() != null) {
							R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
							R36cell3.setCellStyle(numberStyle);
						} else {
							R36cell3.setCellValue("");
							R36cell3.setCellStyle(textStyle);
						}

						// R36 Col E
						Cell R36cell4 = row.createCell(6);
						if (record.getR36_INTEREST_RATE() != null) {
							R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
							R36cell4.setCellStyle(numberStyle);
						} else {
							R36cell4.setCellValue("");
							R36cell4.setCellStyle(textStyle);
						}
						// R37 Col B
						row = sheet.getRow(36);
						Cell R37cell1 = row.createCell(3);
						if (record.getR37_ORIGINAL_AMT() != null) {
							R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
							R37cell1.setCellStyle(numberStyle);
						} else {
							R37cell1.setCellValue("");
							R37cell1.setCellStyle(textStyle);
						}

						// R37 Col C
						Cell R37cell2 = row.createCell(4);
						if (record.getR37_BALANCE_OUTSTANDING() != null) {
							R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
							R37cell2.setCellStyle(numberStyle);
						} else {
							R37cell2.setCellValue("");
							R37cell2.setCellStyle(textStyle);
						}
						// R37 Col D
						Cell R37cell3 = row.createCell(5);
						if (record.getR37_NO_OF_ACS() != null) {
							R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
							R37cell3.setCellStyle(numberStyle);
						} else {
							R37cell3.setCellValue("");
							R37cell3.setCellStyle(textStyle);
						}

						// R37 Col E
						Cell R37cell4 = row.createCell(6);
						if (record.getR37_INTEREST_RATE() != null) {
							R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
							R37cell4.setCellStyle(numberStyle);
						} else {
							R37cell4.setCellValue("");
							R37cell4.setCellStyle(textStyle);
						}
						row = sheet.getRow(37);
						// R37 Col E
						Cell R38cell4 = row.createCell(6);
						if (record.getR38_INTEREST_RATE() != null) {
							R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
							R38cell4.setCellStyle(numberStyle);
						} else {
							R38cell4.setCellValue("");
							R38cell4.setCellStyle(textStyle);
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
					auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF EMAIL SUMMARY", null,
							"BRRS_Q_STAFF_SUMMARYTABLE");
				}

				return out.toByteArray();
			}
		}
	}

//ARCHIVAL SUMMARY EXCEL  FORMAT
// Archival format excel
	public byte[] getExcelQ_STAFFARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory in Archival.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			try {
				// Redirecting to Archival
				return BRRS_Q_STAFFARCHIVALEmailExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);
			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_STAFF_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_STAFF report. Returning empty result.");
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
					Q_STAFF_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(8);

// R9 Col B
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);
					// R11 Col B
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					Cell R12cell1 = row.createCell(1);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);

					Cell R13cell1 = row.createCell(1);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// TABLE 2
					// R21 Col B
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(1);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}
					// R21 COL C
					Cell R21cell2 = row.createCell(2);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					// R22 Col B
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(1);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					// R22 Col C
					Cell R22cell2 = row.createCell(2);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}
					// R23 Col B
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					// R24 Col B
					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					// R25 Col B
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					// R26 Col B
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					// R27 Col B
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(4);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(3);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(4);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(1);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(3);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(4);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(1);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(2);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(3);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(4);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(1);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(2);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(3);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(4);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(4);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF ARCHIVAL SUMMARY", null,
						"BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}

	}

//ARCHIVAL SUMMARY EXCEL  EMAIL

// Archival Email Excel
	public byte[] BRRS_Q_STAFFARCHIVALEmailExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Archival Email Excel generation process in memory.");

		List<Q_STAFF_Archival_Summary_Entity> dataList = getDataByDateListArchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_STAFF report. Returning empty result.");
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
					Q_STAFF_Archival_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(8);
					// R9 Col B
					Cell R9cell1 = row.createCell(3);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(4);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell3 = row.createCell(5);
					if (record.getR9_TOTAL() != null) {
						R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_TOTAL() != null) {
						R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);

					// R11 Col B
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 TOTAL
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_TOTAL() != null) {
						R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					// R12 Col B
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(4);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 TOTAL
					Cell R12cell3 = row.createCell(5);
					if (record.getR12_TOTAL() != null) {
						R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// R13 Col B
					row = sheet.getRow(12);

					// R13 Col B
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(4);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 TOTAL
					Cell R13cell3 = row.createCell(5);
					if (record.getR13_TOTAL() != null) {
						R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// R14 Col B
					row = sheet.getRow(13);

					// R14 Col B
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(4);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 TOTAL
					Cell R14cell3 = row.createCell(5);
					if (record.getR14_TOTAL() != null) {
						R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);

					Cell R21cell1 = row.createCell(3);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}

					Cell R21cell2 = row.createCell(4);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(5);
					if (record.getR21_TOTAL() != null) {
						R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R22 =================
					row = sheet.getRow(21);

					Cell R22cell1 = row.createCell(3);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					Cell R22cell2 = row.createCell(4);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(5);
					if (record.getR22_TOTAL() != null) {
						R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);

					Cell R23cell1 = row.createCell(3);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					Cell R23cell2 = row.createCell(4);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(5);
					if (record.getR23_TOTAL() != null) {
						R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);

					Cell R24cell1 = row.createCell(3);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					Cell R24cell2 = row.createCell(4);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(5);
					if (record.getR24_TOTAL() != null) {
						R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);

					Cell R25cell1 = row.createCell(3);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					Cell R25cell2 = row.createCell(4);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(5);
					if (record.getR25_TOTAL() != null) {
						R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);

					Cell R26cell1 = row.createCell(3);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					Cell R26cell2 = row.createCell(4);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(5);
					if (record.getR26_TOTAL() != null) {
						R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);

					Cell R27cell1 = row.createCell(3);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					Cell R27cell2 = row.createCell(4);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(5);
					if (record.getR27_TOTAL() != null) {
						R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(3);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(4);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(5);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(6);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(3);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(4);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(5);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(6);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(3);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(4);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(5);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(6);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(3);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(4);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(5);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(6);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(3);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(4);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(5);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(6);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(6);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF EMAIL ARCHIVAL SUMMARY", null,
						"BRRS_Q_STAFF_ARCHIVALTABLE_SUMMARY");
			}

			return out.toByteArray();
		}
	}

// RESUB EXCEL  FORMAT

	// Resub Format excel
	public byte[] BRRS_Q_STAFFResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, String format, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB (Format) Excel.");

		if ("email".equalsIgnoreCase(format) && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			try {
				// ✅ Redirecting to Resub Excel
				return BRRS_Q_STAFFEmailResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type,
						version);

			} catch (ParseException e) {
				logger.error("Invalid report date format: {}", fromdate, e);
				throw new RuntimeException("Date format must be dd-MMM-yyyy (e.g. 31-Jul-2025)");
			}
		}

		List<Q_STAFF_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for Q_STAFF report. Returning empty result.");
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
					Q_STAFF_Resub_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(8);

// R9 Col B
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(1);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(2);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);
					// R11 Col B
					Cell R11cell1 = row.createCell(1);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(2);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					Cell R12cell1 = row.createCell(1);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					// R13 Col B
					row = sheet.getRow(12);

					Cell R13cell1 = row.createCell(1);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}
					// R14 Col B
					row = sheet.getRow(13);
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// TABLE 2
					// R21 Col B
					row = sheet.getRow(20);
					Cell R21cell1 = row.createCell(1);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}
					// R21 COL C
					Cell R21cell2 = row.createCell(2);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}
					// R22 Col B
					row = sheet.getRow(21);
					Cell R22cell1 = row.createCell(1);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					// R22 Col C
					Cell R22cell2 = row.createCell(2);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}
					// R23 Col B
					row = sheet.getRow(22);
					// R23 Col B
					Cell R23cell1 = row.createCell(1);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					// R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}
					// R24 Col B
					row = sheet.getRow(23);
					// R24 Col B
					Cell R24cell1 = row.createCell(1);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					// R24 Col C
					Cell R24cell2 = row.createCell(2);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}
					// R25 Col B
					row = sheet.getRow(24);
					// R25 Col B
					Cell R25cell1 = row.createCell(1);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					// R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}
					// R26 Col B
					row = sheet.getRow(25);
					// R26 Col B
					Cell R26cell1 = row.createCell(1);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					// R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}
					// R27 Col B
					row = sheet.getRow(26);
					// R27 Col B
					Cell R27cell1 = row.createCell(1);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					// R27 Col C
					Cell R27cell2 = row.createCell(2);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}
					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(1);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(2);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(3);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(4);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(3);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(4);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(1);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(3);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(4);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(1);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(2);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(3);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(4);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(1);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(2);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(3);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(4);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(4);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF RESUB SUMMARY", null,
						"BRRS_Q_STAFF_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}

	}

// RESUB  EXCEL EMAIL
	// Resub Email Excel
	public byte[] BRRS_Q_STAFFEmailResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting RESUB Email Excel generation process in memory.");

		List<Q_STAFF_Resub_Summary_Entity> dataList = getResubSummarydatabydateListarchival(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for BRRS_Q_STAFF report. Returning empty result.");
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
					Q_STAFF_Resub_Summary_Entity record = dataList.get(i);
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
					row = sheet.getRow(8);
					// R9 Col B
					Cell R9cell1 = row.createCell(3);
					if (record.getR9_LOCAL() != null) {
						R9cell1.setCellValue(record.getR9_LOCAL().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell2 = row.createCell(4);
					if (record.getR9_EXPARIATES() != null) {
						R9cell2.setCellValue(record.getR9_EXPARIATES().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}

					// R9 Col C
					Cell R9cell3 = row.createCell(5);
					if (record.getR9_TOTAL() != null) {
						R9cell3.setCellValue(record.getR9_TOTAL().doubleValue());
						R9cell3.setCellStyle(numberStyle);
					} else {
						R9cell3.setCellValue("");
						R9cell3.setCellStyle(textStyle);
					}
					// R10 Col B
					row = sheet.getRow(9);
					// R10 Col B
					Cell R10cell1 = row.createCell(3);
					if (record.getR10_LOCAL() != null) {
						R10cell1.setCellValue(record.getR10_LOCAL().doubleValue());
						R10cell1.setCellStyle(numberStyle);
					} else {
						R10cell1.setCellValue("");
						R10cell1.setCellStyle(textStyle);
					}

					// R10 Col C
					Cell R10cell2 = row.createCell(4);
					if (record.getR10_EXPARIATES() != null) {
						R10cell2.setCellValue(record.getR10_EXPARIATES().doubleValue());
						R10cell2.setCellStyle(numberStyle);
					} else {
						R10cell2.setCellValue("");
						R10cell2.setCellStyle(textStyle);
					}
					Cell R10cell3 = row.createCell(5);
					if (record.getR10_TOTAL() != null) {
						R10cell3.setCellValue(record.getR10_TOTAL().doubleValue());
						R10cell3.setCellStyle(numberStyle);
					} else {
						R10cell3.setCellValue("");
						R10cell3.setCellStyle(textStyle);
					}
					// R11 Col B
					row = sheet.getRow(10);

					// R11 Col B
					Cell R11cell1 = row.createCell(3);
					if (record.getR11_LOCAL() != null) {
						R11cell1.setCellValue(record.getR11_LOCAL().doubleValue());
						R11cell1.setCellStyle(numberStyle);
					} else {
						R11cell1.setCellValue("");
						R11cell1.setCellStyle(textStyle);
					}

					// R11 Col C
					Cell R11cell2 = row.createCell(4);
					if (record.getR11_EXPARIATES() != null) {
						R11cell2.setCellValue(record.getR11_EXPARIATES().doubleValue());
						R11cell2.setCellStyle(numberStyle);
					} else {
						R11cell2.setCellValue("");
						R11cell2.setCellStyle(textStyle);
					}

					// R11 TOTAL
					Cell R11cell3 = row.createCell(5);
					if (record.getR11_TOTAL() != null) {
						R11cell3.setCellValue(record.getR11_TOTAL().doubleValue());
						R11cell3.setCellStyle(numberStyle);
					} else {
						R11cell3.setCellValue("");
						R11cell3.setCellStyle(textStyle);
					}
					// R12 Col B
					row = sheet.getRow(11);

					// R12 Col B
					Cell R12cell1 = row.createCell(3);
					if (record.getR12_LOCAL() != null) {
						R12cell1.setCellValue(record.getR12_LOCAL().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

					// R12 Col C
					Cell R12cell2 = row.createCell(4);
					if (record.getR12_EXPARIATES() != null) {
						R12cell2.setCellValue(record.getR12_EXPARIATES().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}

					// R12 TOTAL
					Cell R12cell3 = row.createCell(5);
					if (record.getR12_TOTAL() != null) {
						R12cell3.setCellValue(record.getR12_TOTAL().doubleValue());
						R12cell3.setCellStyle(numberStyle);
					} else {
						R12cell3.setCellValue("");
						R12cell3.setCellStyle(textStyle);
					}

					// R13 Col B
					row = sheet.getRow(12);

					// R13 Col B
					Cell R13cell1 = row.createCell(3);
					if (record.getR13_LOCAL() != null) {
						R13cell1.setCellValue(record.getR13_LOCAL().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

					// R13 Col C
					Cell R13cell2 = row.createCell(4);
					if (record.getR13_EXPARIATES() != null) {
						R13cell2.setCellValue(record.getR13_EXPARIATES().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					// R13 TOTAL
					Cell R13cell3 = row.createCell(5);
					if (record.getR13_TOTAL() != null) {
						R13cell3.setCellValue(record.getR13_TOTAL().doubleValue());
						R13cell3.setCellStyle(numberStyle);
					} else {
						R13cell3.setCellValue("");
						R13cell3.setCellStyle(textStyle);
					}

					// R14 Col B
					row = sheet.getRow(13);

					// R14 Col B
					Cell R14cell1 = row.createCell(3);
					if (record.getR14_LOCAL() != null) {
						R14cell1.setCellValue(record.getR14_LOCAL().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

					// R14 Col C
					Cell R14cell2 = row.createCell(4);
					if (record.getR14_EXPARIATES() != null) {
						R14cell2.setCellValue(record.getR14_EXPARIATES().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					// R14 TOTAL
					Cell R14cell3 = row.createCell(5);
					if (record.getR14_TOTAL() != null) {
						R14cell3.setCellValue(record.getR14_TOTAL().doubleValue());
						R14cell3.setCellStyle(numberStyle);
					} else {
						R14cell3.setCellValue("");
						R14cell3.setCellStyle(textStyle);
					}

					// ================= R21 =================
					row = sheet.getRow(20);

					Cell R21cell1 = row.createCell(3);
					if (record.getR21_LOCAL() != null) {
						R21cell1.setCellValue(record.getR21_LOCAL().doubleValue());
						R21cell1.setCellStyle(numberStyle);
					} else {
						R21cell1.setCellValue("");
						R21cell1.setCellStyle(textStyle);
					}

					Cell R21cell2 = row.createCell(4);
					if (record.getR21_EXPARIATES() != null) {
						R21cell2.setCellValue(record.getR21_EXPARIATES().doubleValue());
						R21cell2.setCellStyle(numberStyle);
					} else {
						R21cell2.setCellValue("");
						R21cell2.setCellStyle(textStyle);
					}

					Cell R21cell3 = row.createCell(5);
					if (record.getR21_TOTAL() != null) {
						R21cell3.setCellValue(record.getR21_TOTAL().doubleValue());
						R21cell3.setCellStyle(numberStyle);
					} else {
						R21cell3.setCellValue("");
						R21cell3.setCellStyle(textStyle);
					}

					// ================= R22 =================
					row = sheet.getRow(21);

					Cell R22cell1 = row.createCell(3);
					if (record.getR22_LOCAL() != null) {
						R22cell1.setCellValue(record.getR22_LOCAL().doubleValue());
						R22cell1.setCellStyle(numberStyle);
					} else {
						R22cell1.setCellValue("");
						R22cell1.setCellStyle(textStyle);
					}

					Cell R22cell2 = row.createCell(4);
					if (record.getR22_EXPARIATES() != null) {
						R22cell2.setCellValue(record.getR22_EXPARIATES().doubleValue());
						R22cell2.setCellStyle(numberStyle);
					} else {
						R22cell2.setCellValue("");
						R22cell2.setCellStyle(textStyle);
					}

					Cell R22cell3 = row.createCell(5);
					if (record.getR22_TOTAL() != null) {
						R22cell3.setCellValue(record.getR22_TOTAL().doubleValue());
						R22cell3.setCellStyle(numberStyle);
					} else {
						R22cell3.setCellValue("");
						R22cell3.setCellStyle(textStyle);
					}

					// ================= R23 =================
					row = sheet.getRow(22);

					Cell R23cell1 = row.createCell(3);
					if (record.getR23_LOCAL() != null) {
						R23cell1.setCellValue(record.getR23_LOCAL().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

					Cell R23cell2 = row.createCell(4);
					if (record.getR23_EXPARIATES() != null) {
						R23cell2.setCellValue(record.getR23_EXPARIATES().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					Cell R23cell3 = row.createCell(5);
					if (record.getR23_TOTAL() != null) {
						R23cell3.setCellValue(record.getR23_TOTAL().doubleValue());
						R23cell3.setCellStyle(numberStyle);
					} else {
						R23cell3.setCellValue("");
						R23cell3.setCellStyle(textStyle);
					}

					// ================= R24 =================
					row = sheet.getRow(23);

					Cell R24cell1 = row.createCell(3);
					if (record.getR24_LOCAL() != null) {
						R24cell1.setCellValue(record.getR24_LOCAL().doubleValue());
						R24cell1.setCellStyle(numberStyle);
					} else {
						R24cell1.setCellValue("");
						R24cell1.setCellStyle(textStyle);
					}

					Cell R24cell2 = row.createCell(4);
					if (record.getR24_EXPARIATES() != null) {
						R24cell2.setCellValue(record.getR24_EXPARIATES().doubleValue());
						R24cell2.setCellStyle(numberStyle);
					} else {
						R24cell2.setCellValue("");
						R24cell2.setCellStyle(textStyle);
					}

					Cell R24cell3 = row.createCell(5);
					if (record.getR24_TOTAL() != null) {
						R24cell3.setCellValue(record.getR24_TOTAL().doubleValue());
						R24cell3.setCellStyle(numberStyle);
					} else {
						R24cell3.setCellValue("");
						R24cell3.setCellStyle(textStyle);
					}

					// ================= R25 =================
					row = sheet.getRow(24);

					Cell R25cell1 = row.createCell(3);
					if (record.getR25_LOCAL() != null) {
						R25cell1.setCellValue(record.getR25_LOCAL().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

					Cell R25cell2 = row.createCell(4);
					if (record.getR25_EXPARIATES() != null) {
						R25cell2.setCellValue(record.getR25_EXPARIATES().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					Cell R25cell3 = row.createCell(5);
					if (record.getR25_TOTAL() != null) {
						R25cell3.setCellValue(record.getR25_TOTAL().doubleValue());
						R25cell3.setCellStyle(numberStyle);
					} else {
						R25cell3.setCellValue("");
						R25cell3.setCellStyle(textStyle);
					}

					// ================= R26 =================
					row = sheet.getRow(25);

					Cell R26cell1 = row.createCell(3);
					if (record.getR26_LOCAL() != null) {
						R26cell1.setCellValue(record.getR26_LOCAL().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

					Cell R26cell2 = row.createCell(4);
					if (record.getR26_EXPARIATES() != null) {
						R26cell2.setCellValue(record.getR26_EXPARIATES().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					Cell R26cell3 = row.createCell(5);
					if (record.getR26_TOTAL() != null) {
						R26cell3.setCellValue(record.getR26_TOTAL().doubleValue());
						R26cell3.setCellStyle(numberStyle);
					} else {
						R26cell3.setCellValue("");
						R26cell3.setCellStyle(textStyle);
					}

					// ================= R27 =================
					row = sheet.getRow(26);

					Cell R27cell1 = row.createCell(3);
					if (record.getR27_LOCAL() != null) {
						R27cell1.setCellValue(record.getR27_LOCAL().doubleValue());
						R27cell1.setCellStyle(numberStyle);
					} else {
						R27cell1.setCellValue("");
						R27cell1.setCellStyle(textStyle);
					}

					Cell R27cell2 = row.createCell(4);
					if (record.getR27_EXPARIATES() != null) {
						R27cell2.setCellValue(record.getR27_EXPARIATES().doubleValue());
						R27cell2.setCellStyle(numberStyle);
					} else {
						R27cell2.setCellValue("");
						R27cell2.setCellStyle(textStyle);
					}

					Cell R27cell3 = row.createCell(5);
					if (record.getR27_TOTAL() != null) {
						R27cell3.setCellValue(record.getR27_TOTAL().doubleValue());
						R27cell3.setCellStyle(numberStyle);
					} else {
						R27cell3.setCellValue("");
						R27cell3.setCellStyle(textStyle);
					}

					// TABLE 3
					// R33 Col B
					row = sheet.getRow(32);
					Cell R33cell1 = row.createCell(3);
					if (record.getR33_ORIGINAL_AMT() != null) {
						R33cell1.setCellValue(record.getR33_ORIGINAL_AMT().doubleValue());
						R33cell1.setCellStyle(numberStyle);
					} else {
						R33cell1.setCellValue("");
						R33cell1.setCellStyle(textStyle);
					}

					// R33 Col C
					Cell R33cell2 = row.createCell(4);
					if (record.getR33_BALANCE_OUTSTANDING() != null) {
						R33cell2.setCellValue(record.getR33_BALANCE_OUTSTANDING().doubleValue());
						R33cell2.setCellStyle(numberStyle);
					} else {
						R33cell2.setCellValue("");
						R33cell2.setCellStyle(textStyle);
					}
					// R33 Col D
					Cell R33cell3 = row.createCell(5);
					if (record.getR33_NO_OF_ACS() != null) {
						R33cell3.setCellValue(record.getR33_NO_OF_ACS().doubleValue());
						R33cell3.setCellStyle(numberStyle);
					} else {
						R33cell3.setCellValue("");
						R33cell3.setCellStyle(textStyle);
					}

					// R33 Col E
					Cell R33cell4 = row.createCell(6);
					if (record.getR33_INTEREST_RATE() != null) {
						R33cell4.setCellValue(record.getR33_INTEREST_RATE().doubleValue());
						R33cell4.setCellStyle(numberStyle);
					} else {
						R33cell4.setCellValue("");
						R33cell4.setCellStyle(textStyle);
					}
					// R34 Col B
					row = sheet.getRow(33);
					Cell R34cell1 = row.createCell(3);
					if (record.getR34_ORIGINAL_AMT() != null) {
						R34cell1.setCellValue(record.getR34_ORIGINAL_AMT().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

					// R34 Col C
					Cell R34cell2 = row.createCell(4);
					if (record.getR34_BALANCE_OUTSTANDING() != null) {
						R34cell2.setCellValue(record.getR34_BALANCE_OUTSTANDING().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}
					// R34 Col D
					Cell R34cell3 = row.createCell(5);
					if (record.getR34_NO_OF_ACS() != null) {
						R34cell3.setCellValue(record.getR34_NO_OF_ACS().doubleValue());
						R34cell3.setCellStyle(numberStyle);
					} else {
						R34cell3.setCellValue("");
						R34cell3.setCellStyle(textStyle);
					}

					// R34 Col E
					Cell R34cell4 = row.createCell(6);
					if (record.getR34_INTEREST_RATE() != null) {
						R34cell4.setCellValue(record.getR34_INTEREST_RATE().doubleValue());
						R34cell4.setCellStyle(numberStyle);
					} else {
						R34cell4.setCellValue("");
						R34cell4.setCellStyle(textStyle);
					}
					// R35 Col B
					row = sheet.getRow(34);

					Cell R35cell1 = row.createCell(3);
					if (record.getR35_ORIGINAL_AMT() != null) {
						R35cell1.setCellValue(record.getR35_ORIGINAL_AMT().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

					// R35 Col C
					Cell R35cell2 = row.createCell(4);
					if (record.getR35_BALANCE_OUTSTANDING() != null) {
						R35cell2.setCellValue(record.getR35_BALANCE_OUTSTANDING().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}
					// R35 Col D
					Cell R35cell3 = row.createCell(5);
					if (record.getR35_NO_OF_ACS() != null) {
						R35cell3.setCellValue(record.getR35_NO_OF_ACS().doubleValue());
						R35cell3.setCellStyle(numberStyle);
					} else {
						R35cell3.setCellValue("");
						R35cell3.setCellStyle(textStyle);
					}

					// R35 Col E
					Cell R35cell4 = row.createCell(6);
					if (record.getR35_INTEREST_RATE() != null) {
						R35cell4.setCellValue(record.getR35_INTEREST_RATE().doubleValue());
						R35cell4.setCellStyle(numberStyle);
					} else {
						R35cell4.setCellValue("");
						R35cell4.setCellStyle(textStyle);
					}
					// R36 Col B
					row = sheet.getRow(35);
					Cell R36cell1 = row.createCell(3);
					if (record.getR36_ORIGINAL_AMT() != null) {
						R36cell1.setCellValue(record.getR36_ORIGINAL_AMT().doubleValue());
						R36cell1.setCellStyle(numberStyle);
					} else {
						R36cell1.setCellValue("");
						R36cell1.setCellStyle(textStyle);
					}

					// R36 Col C
					Cell R36cell2 = row.createCell(4);
					if (record.getR36_BALANCE_OUTSTANDING() != null) {
						R36cell2.setCellValue(record.getR36_BALANCE_OUTSTANDING().doubleValue());
						R36cell2.setCellStyle(numberStyle);
					} else {
						R36cell2.setCellValue("");
						R36cell2.setCellStyle(textStyle);
					}
					// R36 Col D
					Cell R36cell3 = row.createCell(5);
					if (record.getR36_NO_OF_ACS() != null) {
						R36cell3.setCellValue(record.getR36_NO_OF_ACS().doubleValue());
						R36cell3.setCellStyle(numberStyle);
					} else {
						R36cell3.setCellValue("");
						R36cell3.setCellStyle(textStyle);
					}

					// R36 Col E
					Cell R36cell4 = row.createCell(6);
					if (record.getR36_INTEREST_RATE() != null) {
						R36cell4.setCellValue(record.getR36_INTEREST_RATE().doubleValue());
						R36cell4.setCellStyle(numberStyle);
					} else {
						R36cell4.setCellValue("");
						R36cell4.setCellStyle(textStyle);
					}
					// R37 Col B
					row = sheet.getRow(36);
					Cell R37cell1 = row.createCell(3);
					if (record.getR37_ORIGINAL_AMT() != null) {
						R37cell1.setCellValue(record.getR37_ORIGINAL_AMT().doubleValue());
						R37cell1.setCellStyle(numberStyle);
					} else {
						R37cell1.setCellValue("");
						R37cell1.setCellStyle(textStyle);
					}

					// R37 Col C
					Cell R37cell2 = row.createCell(4);
					if (record.getR37_BALANCE_OUTSTANDING() != null) {
						R37cell2.setCellValue(record.getR37_BALANCE_OUTSTANDING().doubleValue());
						R37cell2.setCellStyle(numberStyle);
					} else {
						R37cell2.setCellValue("");
						R37cell2.setCellStyle(textStyle);
					}
					// R37 Col D
					Cell R37cell3 = row.createCell(5);
					if (record.getR37_NO_OF_ACS() != null) {
						R37cell3.setCellValue(record.getR37_NO_OF_ACS().doubleValue());
						R37cell3.setCellStyle(numberStyle);
					} else {
						R37cell3.setCellValue("");
						R37cell3.setCellStyle(textStyle);
					}

					// R37 Col E
					Cell R37cell4 = row.createCell(6);
					if (record.getR37_INTEREST_RATE() != null) {
						R37cell4.setCellValue(record.getR37_INTEREST_RATE().doubleValue());
						R37cell4.setCellStyle(numberStyle);
					} else {
						R37cell4.setCellValue("");
						R37cell4.setCellStyle(textStyle);
					}
					row = sheet.getRow(37);
					// R37 Col E
					Cell R38cell4 = row.createCell(6);
					if (record.getR38_INTEREST_RATE() != null) {
						R38cell4.setCellValue(record.getR38_INTEREST_RATE().doubleValue());
						R38cell4.setCellStyle(numberStyle);
					} else {
						R38cell4.setCellValue("");
						R38cell4.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "Q_STAFF EMAIL RESUB SUMMARY", null,
						"BRRS_Q_STAFF_RESUB_SUMMARYTABLE");
			}

			return out.toByteArray();
		}
	}

}