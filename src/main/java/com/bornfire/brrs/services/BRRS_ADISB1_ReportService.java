package com.bornfire.brrs.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Row;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.hibernate.SessionFactory;
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
public class BRRS_ADISB1_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_ADISB1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<ADISB1_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_ADISB1_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ADISB1RowMapper());
	}

	public List<ADISB1_Manual_Summary_Entity> getManualDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_ADISB1_MANUAL_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ADISB1ManualRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getADISB1Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_ADISB1_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<ADISB1_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate, BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_ADISB1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ADISB1ArchivalRowMapper());
	}

//GET ALL WITH VERSION

	public List<ADISB1_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_ADISB1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new ADISB1ArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_ADISB1_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Object[]> getADISB1ManualArchivalList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_ADISB1_MANUAL_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<ADISB1_Manual_Archival_Summary_Entity> getManualArchivalByDate(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_ADISB1_MANUAL_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion }, new ADISB1ManualArchivalRowMapper());
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<ADISB1_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_ADISB1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new ADISB1DetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<ADISB1_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_ADISB1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new ADISB1DetailRowMapper());
	}

// 3. PAGINATION

	public List<ADISB1_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_ADISB1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new ADISB1DetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_ADISB1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<ADISB1_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_ADISB1_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new ADISB1DetailRowMapper());
	}

// 6. BY ACCOUNT NUMBER

	public ADISB1_Detail_Entity findByAcctnumber(String acct_number) {

		String sql = "SELECT * FROM BRRS_ADISB1_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acct_number }, new ADISB1DetailRowMapper());
	}

// 1. GET BY DATE + VERSION

	public List<ADISB1_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_ADISB1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new SCH17ArchivalDetailRowMapper());
	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<ADISB1_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_ADISB1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new SCH17ArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class ADISB1RowMapper implements RowMapper<ADISB1_Summary_Entity> {

		@Override
		public ADISB1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Summary_Entity obj = new ADISB1_Summary_Entity();

			obj.setR7_total_no_of_acct(rs.getBigDecimal("r7_total_no_of_acct"));
			obj.setR7_total_value(rs.getBigDecimal("r7_total_value"));

			obj.setR8_total_no_of_acct(rs.getBigDecimal("r8_total_no_of_acct"));
			obj.setR8_total_value(rs.getBigDecimal("r8_total_value"));

			obj.setR9_total_no_of_acct(rs.getBigDecimal("r9_total_no_of_acct"));
			obj.setR9_total_value(rs.getBigDecimal("r9_total_value"));

			obj.setR12_total_no_of_acct(rs.getBigDecimal("r12_total_no_of_acct"));
			obj.setR12_total_value(rs.getBigDecimal("r12_total_value"));

			obj.setR13_total_no_of_acct(rs.getBigDecimal("r13_total_no_of_acct"));
			obj.setR13_total_value(rs.getBigDecimal("r13_total_value"));

			obj.setR14_total_no_of_acct(rs.getBigDecimal("r14_total_no_of_acct"));
			obj.setR14_total_value(rs.getBigDecimal("r14_total_value"));

			obj.setR34_total_no_of_acct(rs.getBigDecimal("r34_total_no_of_acct"));
			obj.setR34_total_value(rs.getBigDecimal("r34_total_value"));

			obj.setR38_total_no_of_acct(rs.getBigDecimal("r38_total_no_of_acct"));
			obj.setR38_total_value(rs.getBigDecimal("r38_total_value"));

			obj.setR39_total_no_of_acct(rs.getBigDecimal("r39_total_no_of_acct"));
			obj.setR39_total_value(rs.getBigDecimal("r39_total_value"));

			//
			// COMMON FIELDS
			//
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

	public static class ADISB1_Summary_Entity {

		private BigDecimal r7_total_no_of_acct;
		private BigDecimal r7_total_value;
		private BigDecimal r8_total_no_of_acct;
		private BigDecimal r8_total_value;
		private BigDecimal r9_total_no_of_acct;
		private BigDecimal r9_total_value;
		private BigDecimal r12_total_no_of_acct;
		private BigDecimal r12_total_value;
		private BigDecimal r13_total_no_of_acct;
		private BigDecimal r13_total_value;
		private BigDecimal r14_total_no_of_acct;
		private BigDecimal r14_total_value;

		private BigDecimal r34_total_no_of_acct;
		private BigDecimal r34_total_value;

		private BigDecimal r38_total_no_of_acct;
		private BigDecimal r38_total_value;
		private BigDecimal r39_total_no_of_acct;
		private BigDecimal r39_total_value;

		// ================= COMMON =================
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

		// GETTERS & SETTERS (FULL)

		public BigDecimal getR7_total_no_of_acct() {
			return r7_total_no_of_acct;
		}

		public void setR7_total_no_of_acct(BigDecimal r7_total_no_of_acct) {
			this.r7_total_no_of_acct = r7_total_no_of_acct;
		}

		public BigDecimal getR7_total_value() {
			return r7_total_value;
		}

		public void setR7_total_value(BigDecimal r7_total_value) {
			this.r7_total_value = r7_total_value;
		}

		public BigDecimal getR8_total_no_of_acct() {
			return r8_total_no_of_acct;
		}

		public void setR8_total_no_of_acct(BigDecimal r8_total_no_of_acct) {
			this.r8_total_no_of_acct = r8_total_no_of_acct;
		}

		public BigDecimal getR8_total_value() {
			return r8_total_value;
		}

		public void setR8_total_value(BigDecimal r8_total_value) {
			this.r8_total_value = r8_total_value;
		}

		public BigDecimal getR9_total_no_of_acct() {
			return r9_total_no_of_acct;
		}

		public void setR9_total_no_of_acct(BigDecimal r9_total_no_of_acct) {
			this.r9_total_no_of_acct = r9_total_no_of_acct;
		}

		public BigDecimal getR9_total_value() {
			return r9_total_value;
		}

		public void setR9_total_value(BigDecimal r9_total_value) {
			this.r9_total_value = r9_total_value;
		}

		public BigDecimal getR12_total_no_of_acct() {
			return r12_total_no_of_acct;
		}

		public void setR12_total_no_of_acct(BigDecimal r12_total_no_of_acct) {
			this.r12_total_no_of_acct = r12_total_no_of_acct;
		}

		public BigDecimal getR12_total_value() {
			return r12_total_value;
		}

		public void setR12_total_value(BigDecimal r12_total_value) {
			this.r12_total_value = r12_total_value;
		}

		public BigDecimal getR13_total_no_of_acct() {
			return r13_total_no_of_acct;
		}

		public void setR13_total_no_of_acct(BigDecimal r13_total_no_of_acct) {
			this.r13_total_no_of_acct = r13_total_no_of_acct;
		}

		public BigDecimal getR13_total_value() {
			return r13_total_value;
		}

		public void setR13_total_value(BigDecimal r13_total_value) {
			this.r13_total_value = r13_total_value;
		}

		public BigDecimal getR14_total_no_of_acct() {
			return r14_total_no_of_acct;
		}

		public void setR14_total_no_of_acct(BigDecimal r14_total_no_of_acct) {
			this.r14_total_no_of_acct = r14_total_no_of_acct;
		}

		public BigDecimal getR14_total_value() {
			return r14_total_value;
		}

		public void setR14_total_value(BigDecimal r14_total_value) {
			this.r14_total_value = r14_total_value;
		}

		public BigDecimal getR34_total_no_of_acct() {
			return r34_total_no_of_acct;
		}

		public void setR34_total_no_of_acct(BigDecimal r34_total_no_of_acct) {
			this.r34_total_no_of_acct = r34_total_no_of_acct;
		}

		public BigDecimal getR34_total_value() {
			return r34_total_value;
		}

		public void setR34_total_value(BigDecimal r34_total_value) {
			this.r34_total_value = r34_total_value;
		}

		public BigDecimal getR38_total_no_of_acct() {
			return r38_total_no_of_acct;
		}

		public void setR38_total_no_of_acct(BigDecimal r38_total_no_of_acct) {
			this.r38_total_no_of_acct = r38_total_no_of_acct;
		}

		public BigDecimal getR38_total_value() {
			return r38_total_value;
		}

		public void setR38_total_value(BigDecimal r38_total_value) {
			this.r38_total_value = r38_total_value;
		}

		public BigDecimal getR39_total_no_of_acct() {
			return r39_total_no_of_acct;
		}

		public void setR39_total_no_of_acct(BigDecimal r39_total_no_of_acct) {
			this.r39_total_no_of_acct = r39_total_no_of_acct;
		}

		public BigDecimal getR39_total_value() {
			return r39_total_value;
		}

		public void setR39_total_value(BigDecimal r39_total_value) {
			this.r39_total_value = r39_total_value;
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

	class ADISB1ManualRowMapper implements RowMapper<ADISB1_Manual_Summary_Entity> {

		@Override
		public ADISB1_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Manual_Summary_Entity obj = new ADISB1_Manual_Summary_Entity();

			obj.setR23_total_no_of_acct(rs.getBigDecimal("r23_total_no_of_acct"));
			obj.setR23_total_value(rs.getBigDecimal("r23_total_value"));

			obj.setR25_total_no_of_acct(rs.getBigDecimal("r25_total_no_of_acct"));
			obj.setR25_total_value(rs.getBigDecimal("r25_total_value"));

			obj.setR26_total_no_of_acct(rs.getBigDecimal("r26_total_no_of_acct"));
			obj.setR26_total_value(rs.getBigDecimal("r26_total_value"));

			obj.setR30_total_no_of_acct(rs.getBigDecimal("r30_total_no_of_acct"));
			obj.setR30_total_value(rs.getBigDecimal("r30_total_value"));

			obj.setR35_total_no_of_acct(rs.getBigDecimal("r35_total_no_of_acct"));
			obj.setR35_total_value(rs.getBigDecimal("r35_total_value"));

			obj.setR42_total_no_of_acct(rs.getBigDecimal("r42_total_no_of_acct"));
			obj.setR42_total_value(rs.getBigDecimal("r42_total_value"));

			obj.setR43_total_no_of_acct(rs.getBigDecimal("r43_total_no_of_acct"));
			obj.setR43_total_value(rs.getBigDecimal("r43_total_value"));

			obj.setR44_total_no_of_acct(rs.getBigDecimal("r44_total_no_of_acct"));
			obj.setR44_total_value(rs.getBigDecimal("r44_total_value"));

			obj.setR46_total_no_of_acct(rs.getBigDecimal("r46_total_no_of_acct"));
			obj.setR46_total_value(rs.getBigDecimal("r46_total_value"));

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

	public static class ADISB1_Manual_Summary_Entity {

		private BigDecimal r23_total_no_of_acct;
		private BigDecimal r23_total_value;
		private BigDecimal r25_total_no_of_acct;
		private BigDecimal r25_total_value;
		private BigDecimal r26_total_no_of_acct;
		private BigDecimal r26_total_value;
		private BigDecimal r30_total_no_of_acct;
		private BigDecimal r30_total_value;

		private BigDecimal r35_total_no_of_acct;
		private BigDecimal r35_total_value;

		private BigDecimal r42_total_no_of_acct;
		private BigDecimal r42_total_value;
		private BigDecimal r43_total_no_of_acct;
		private BigDecimal r43_total_value;
		private BigDecimal r44_total_no_of_acct;
		private BigDecimal r44_total_value;
		private BigDecimal r46_total_no_of_acct;
		private BigDecimal r46_total_value;

		// ================= COMMON =================
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

		public BigDecimal getR23_total_no_of_acct() {
			return r23_total_no_of_acct;
		}

		public void setR23_total_no_of_acct(BigDecimal r23_total_no_of_acct) {
			this.r23_total_no_of_acct = r23_total_no_of_acct;
		}

		public BigDecimal getR23_total_value() {
			return r23_total_value;
		}

		public void setR23_total_value(BigDecimal r23_total_value) {
			this.r23_total_value = r23_total_value;
		}

		public BigDecimal getR25_total_no_of_acct() {
			return r25_total_no_of_acct;
		}

		public void setR25_total_no_of_acct(BigDecimal r25_total_no_of_acct) {
			this.r25_total_no_of_acct = r25_total_no_of_acct;
		}

		public BigDecimal getR25_total_value() {
			return r25_total_value;
		}

		public void setR25_total_value(BigDecimal r25_total_value) {
			this.r25_total_value = r25_total_value;
		}

		public BigDecimal getR26_total_no_of_acct() {
			return r26_total_no_of_acct;
		}

		public void setR26_total_no_of_acct(BigDecimal r26_total_no_of_acct) {
			this.r26_total_no_of_acct = r26_total_no_of_acct;
		}

		public BigDecimal getR26_total_value() {
			return r26_total_value;
		}

		public void setR26_total_value(BigDecimal r26_total_value) {
			this.r26_total_value = r26_total_value;
		}

		public BigDecimal getR30_total_no_of_acct() {
			return r30_total_no_of_acct;
		}

		public void setR30_total_no_of_acct(BigDecimal r30_total_no_of_acct) {
			this.r30_total_no_of_acct = r30_total_no_of_acct;
		}

		public BigDecimal getR30_total_value() {
			return r30_total_value;
		}

		public void setR30_total_value(BigDecimal r30_total_value) {
			this.r30_total_value = r30_total_value;
		}

		public BigDecimal getR35_total_no_of_acct() {
			return r35_total_no_of_acct;
		}

		public void setR35_total_no_of_acct(BigDecimal r35_total_no_of_acct) {
			this.r35_total_no_of_acct = r35_total_no_of_acct;
		}

		public BigDecimal getR35_total_value() {
			return r35_total_value;
		}

		public void setR35_total_value(BigDecimal r35_total_value) {
			this.r35_total_value = r35_total_value;
		}

		public BigDecimal getR42_total_no_of_acct() {
			return r42_total_no_of_acct;
		}

		public void setR42_total_no_of_acct(BigDecimal r42_total_no_of_acct) {
			this.r42_total_no_of_acct = r42_total_no_of_acct;
		}

		public BigDecimal getR42_total_value() {
			return r42_total_value;
		}

		public void setR42_total_value(BigDecimal r42_total_value) {
			this.r42_total_value = r42_total_value;
		}

		public BigDecimal getR43_total_no_of_acct() {
			return r43_total_no_of_acct;
		}

		public void setR43_total_no_of_acct(BigDecimal r43_total_no_of_acct) {
			this.r43_total_no_of_acct = r43_total_no_of_acct;
		}

		public BigDecimal getR43_total_value() {
			return r43_total_value;
		}

		public void setR43_total_value(BigDecimal r43_total_value) {
			this.r43_total_value = r43_total_value;
		}

		public BigDecimal getR44_total_no_of_acct() {
			return r44_total_no_of_acct;
		}

		public void setR44_total_no_of_acct(BigDecimal r44_total_no_of_acct) {
			this.r44_total_no_of_acct = r44_total_no_of_acct;
		}

		public BigDecimal getR44_total_value() {
			return r44_total_value;
		}

		public void setR44_total_value(BigDecimal r44_total_value) {
			this.r44_total_value = r44_total_value;
		}

		public BigDecimal getR46_total_no_of_acct() {
			return r46_total_no_of_acct;
		}

		public void setR46_total_no_of_acct(BigDecimal r46_total_no_of_acct) {
			this.r46_total_no_of_acct = r46_total_no_of_acct;
		}

		public BigDecimal getR46_total_value() {
			return r46_total_value;
		}

		public void setR46_total_value(BigDecimal r46_total_value) {
			this.r46_total_value = r46_total_value;
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

//ARCHIVAL ROW MAPPER

	class ADISB1ArchivalRowMapper implements RowMapper<ADISB1_Archival_Summary_Entity> {

		@Override
		public ADISB1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Archival_Summary_Entity obj = new ADISB1_Archival_Summary_Entity();

			obj.setR7_total_no_of_acct(rs.getBigDecimal("r7_total_no_of_acct"));
			obj.setR7_total_value(rs.getBigDecimal("r7_total_value"));

			obj.setR8_total_no_of_acct(rs.getBigDecimal("r8_total_no_of_acct"));
			obj.setR8_total_value(rs.getBigDecimal("r8_total_value"));

			obj.setR9_total_no_of_acct(rs.getBigDecimal("r9_total_no_of_acct"));
			obj.setR9_total_value(rs.getBigDecimal("r9_total_value"));

			obj.setR12_total_no_of_acct(rs.getBigDecimal("r12_total_no_of_acct"));
			obj.setR12_total_value(rs.getBigDecimal("r12_total_value"));

			obj.setR13_total_no_of_acct(rs.getBigDecimal("r13_total_no_of_acct"));
			obj.setR13_total_value(rs.getBigDecimal("r13_total_value"));

			obj.setR14_total_no_of_acct(rs.getBigDecimal("r14_total_no_of_acct"));
			obj.setR14_total_value(rs.getBigDecimal("r14_total_value"));

			obj.setR34_total_no_of_acct(rs.getBigDecimal("r34_total_no_of_acct"));
			obj.setR34_total_value(rs.getBigDecimal("r34_total_value"));

			obj.setR38_total_no_of_acct(rs.getBigDecimal("r38_total_no_of_acct"));
			obj.setR38_total_value(rs.getBigDecimal("r38_total_value"));

			obj.setR39_total_no_of_acct(rs.getBigDecimal("r39_total_no_of_acct"));
			obj.setR39_total_value(rs.getBigDecimal("r39_total_value"));

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setREPORT_RESUBDATE(rs.getBigDecimal("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	@IdClass(ADISB1_PK.class)
	public class ADISB1_Archival_Summary_Entity {

		private BigDecimal r7_total_no_of_acct;
		private BigDecimal r7_total_value;
		private BigDecimal r8_total_no_of_acct;
		private BigDecimal r8_total_value;
		private BigDecimal r9_total_no_of_acct;
		private BigDecimal r9_total_value;
		private BigDecimal r12_total_no_of_acct;
		private BigDecimal r12_total_value;
		private BigDecimal r13_total_no_of_acct;
		private BigDecimal r13_total_value;
		private BigDecimal r14_total_no_of_acct;
		private BigDecimal r14_total_value;

		private BigDecimal r34_total_no_of_acct;
		private BigDecimal r34_total_value;

		private BigDecimal r38_total_no_of_acct;
		private BigDecimal r38_total_value;
		private BigDecimal r39_total_no_of_acct;
		private BigDecimal r39_total_value;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		private BigDecimal report_version;
		@Column(name = "REPORT_RESUBDATE")
		private BigDecimal REPORT_RESUBDATE;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR7_total_no_of_acct() {
			return r7_total_no_of_acct;
		}

		public void setR7_total_no_of_acct(BigDecimal r7_total_no_of_acct) {
			this.r7_total_no_of_acct = r7_total_no_of_acct;
		}

		public BigDecimal getR7_total_value() {
			return r7_total_value;
		}

		public void setR7_total_value(BigDecimal r7_total_value) {
			this.r7_total_value = r7_total_value;
		}

		public BigDecimal getR8_total_no_of_acct() {
			return r8_total_no_of_acct;
		}

		public void setR8_total_no_of_acct(BigDecimal r8_total_no_of_acct) {
			this.r8_total_no_of_acct = r8_total_no_of_acct;
		}

		public BigDecimal getR8_total_value() {
			return r8_total_value;
		}

		public void setR8_total_value(BigDecimal r8_total_value) {
			this.r8_total_value = r8_total_value;
		}

		public BigDecimal getR9_total_no_of_acct() {
			return r9_total_no_of_acct;
		}

		public void setR9_total_no_of_acct(BigDecimal r9_total_no_of_acct) {
			this.r9_total_no_of_acct = r9_total_no_of_acct;
		}

		public BigDecimal getR9_total_value() {
			return r9_total_value;
		}

		public void setR9_total_value(BigDecimal r9_total_value) {
			this.r9_total_value = r9_total_value;
		}

		public BigDecimal getR12_total_no_of_acct() {
			return r12_total_no_of_acct;
		}

		public void setR12_total_no_of_acct(BigDecimal r12_total_no_of_acct) {
			this.r12_total_no_of_acct = r12_total_no_of_acct;
		}

		public BigDecimal getR12_total_value() {
			return r12_total_value;
		}

		public void setR12_total_value(BigDecimal r12_total_value) {
			this.r12_total_value = r12_total_value;
		}

		public BigDecimal getR13_total_no_of_acct() {
			return r13_total_no_of_acct;
		}

		public void setR13_total_no_of_acct(BigDecimal r13_total_no_of_acct) {
			this.r13_total_no_of_acct = r13_total_no_of_acct;
		}

		public BigDecimal getR13_total_value() {
			return r13_total_value;
		}

		public void setR13_total_value(BigDecimal r13_total_value) {
			this.r13_total_value = r13_total_value;
		}

		public BigDecimal getR14_total_no_of_acct() {
			return r14_total_no_of_acct;
		}

		public void setR14_total_no_of_acct(BigDecimal r14_total_no_of_acct) {
			this.r14_total_no_of_acct = r14_total_no_of_acct;
		}

		public BigDecimal getR14_total_value() {
			return r14_total_value;
		}

		public void setR14_total_value(BigDecimal r14_total_value) {
			this.r14_total_value = r14_total_value;
		}

		public BigDecimal getR34_total_no_of_acct() {
			return r34_total_no_of_acct;
		}

		public void setR34_total_no_of_acct(BigDecimal r34_total_no_of_acct) {
			this.r34_total_no_of_acct = r34_total_no_of_acct;
		}

		public BigDecimal getR34_total_value() {
			return r34_total_value;
		}

		public void setR34_total_value(BigDecimal r34_total_value) {
			this.r34_total_value = r34_total_value;
		}

		public BigDecimal getR38_total_no_of_acct() {
			return r38_total_no_of_acct;
		}

		public void setR38_total_no_of_acct(BigDecimal r38_total_no_of_acct) {
			this.r38_total_no_of_acct = r38_total_no_of_acct;
		}

		public BigDecimal getR38_total_value() {
			return r38_total_value;
		}

		public void setR38_total_value(BigDecimal r38_total_value) {
			this.r38_total_value = r38_total_value;
		}

		public BigDecimal getR39_total_no_of_acct() {
			return r39_total_no_of_acct;
		}

		public void setR39_total_no_of_acct(BigDecimal r39_total_no_of_acct) {
			this.r39_total_no_of_acct = r39_total_no_of_acct;
		}

		public BigDecimal getR39_total_value() {
			return r39_total_value;
		}

		public void setR39_total_value(BigDecimal r39_total_value) {
			this.r39_total_value = r39_total_value;
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

		public BigDecimal getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(BigDecimal rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
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

	class ADISB1ManualArchivalRowMapper implements RowMapper<ADISB1_Manual_Archival_Summary_Entity> {

		@Override
		public ADISB1_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Manual_Archival_Summary_Entity obj = new ADISB1_Manual_Archival_Summary_Entity();

			obj.setR23_total_no_of_acct(rs.getBigDecimal("r23_total_no_of_acct"));
			obj.setR23_total_value(rs.getBigDecimal("r23_total_value"));

			obj.setR25_total_no_of_acct(rs.getBigDecimal("r25_total_no_of_acct"));
			obj.setR25_total_value(rs.getBigDecimal("r25_total_value"));

			obj.setR26_total_no_of_acct(rs.getBigDecimal("r26_total_no_of_acct"));
			obj.setR26_total_value(rs.getBigDecimal("r26_total_value"));

			obj.setR30_total_no_of_acct(rs.getBigDecimal("r30_total_no_of_acct"));
			obj.setR30_total_value(rs.getBigDecimal("r30_total_value"));

			obj.setR35_total_no_of_acct(rs.getBigDecimal("r35_total_no_of_acct"));
			obj.setR35_total_value(rs.getBigDecimal("r35_total_value"));

			obj.setR42_total_no_of_acct(rs.getBigDecimal("r42_total_no_of_acct"));
			obj.setR42_total_value(rs.getBigDecimal("r42_total_value"));

			obj.setR43_total_no_of_acct(rs.getBigDecimal("r43_total_no_of_acct"));
			obj.setR43_total_value(rs.getBigDecimal("r43_total_value"));

			obj.setR44_total_no_of_acct(rs.getBigDecimal("r44_total_no_of_acct"));
			obj.setR44_total_value(rs.getBigDecimal("r44_total_value"));

			obj.setR46_total_no_of_acct(rs.getBigDecimal("r46_total_no_of_acct"));
			obj.setR46_total_value(rs.getBigDecimal("r46_total_value"));

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setREPORT_RESUBDATE(rs.getBigDecimal("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public static class ADISB1_PK implements Serializable {

		private Date report_date;
		private BigDecimal report_version;

		public ADISB1_PK() {
		}

		public ADISB1_PK(Date report_date, BigDecimal report_version) {
			this.report_date = report_date;
			this.report_version = report_version;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof ADISB1_PK))
				return false;
			ADISB1_PK that = (ADISB1_PK) o;
			return Objects.equals(report_date, that.report_date) && Objects.equals(report_version, that.report_version);
		}

		@Override
		public int hashCode() {
			return Objects.hash(report_date, report_version);
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
	}

	@IdClass(ADISB1_PK.class)
	public class ADISB1_Manual_Archival_Summary_Entity {

		private BigDecimal r23_total_no_of_acct;
		private BigDecimal r23_total_value;
		private BigDecimal r25_total_no_of_acct;
		private BigDecimal r25_total_value;
		private BigDecimal r26_total_no_of_acct;
		private BigDecimal r26_total_value;
		private BigDecimal r30_total_no_of_acct;
		private BigDecimal r30_total_value;

		private BigDecimal r35_total_no_of_acct;
		private BigDecimal r35_total_value;

		private BigDecimal r42_total_no_of_acct;
		private BigDecimal r42_total_value;
		private BigDecimal r43_total_no_of_acct;
		private BigDecimal r43_total_value;
		private BigDecimal r44_total_no_of_acct;
		private BigDecimal r44_total_value;
		private BigDecimal r46_total_no_of_acct;
		private BigDecimal r46_total_value;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		private BigDecimal report_version;

		@Column(name = "REPORT_RESUBDATE")
		private BigDecimal REPORT_RESUBDATE;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		public BigDecimal getR23_total_no_of_acct() {
			return r23_total_no_of_acct;
		}

		public void setR23_total_no_of_acct(BigDecimal r23_total_no_of_acct) {
			this.r23_total_no_of_acct = r23_total_no_of_acct;
		}

		public BigDecimal getR23_total_value() {
			return r23_total_value;
		}

		public void setR23_total_value(BigDecimal r23_total_value) {
			this.r23_total_value = r23_total_value;
		}

		public BigDecimal getR25_total_no_of_acct() {
			return r25_total_no_of_acct;
		}

		public void setR25_total_no_of_acct(BigDecimal r25_total_no_of_acct) {
			this.r25_total_no_of_acct = r25_total_no_of_acct;
		}

		public BigDecimal getR25_total_value() {
			return r25_total_value;
		}

		public void setR25_total_value(BigDecimal r25_total_value) {
			this.r25_total_value = r25_total_value;
		}

		public BigDecimal getR26_total_no_of_acct() {
			return r26_total_no_of_acct;
		}

		public void setR26_total_no_of_acct(BigDecimal r26_total_no_of_acct) {
			this.r26_total_no_of_acct = r26_total_no_of_acct;
		}

		public BigDecimal getR26_total_value() {
			return r26_total_value;
		}

		public void setR26_total_value(BigDecimal r26_total_value) {
			this.r26_total_value = r26_total_value;
		}

		public BigDecimal getR30_total_no_of_acct() {
			return r30_total_no_of_acct;
		}

		public void setR30_total_no_of_acct(BigDecimal r30_total_no_of_acct) {
			this.r30_total_no_of_acct = r30_total_no_of_acct;
		}

		public BigDecimal getR30_total_value() {
			return r30_total_value;
		}

		public void setR30_total_value(BigDecimal r30_total_value) {
			this.r30_total_value = r30_total_value;
		}

		public BigDecimal getR35_total_no_of_acct() {
			return r35_total_no_of_acct;
		}

		public void setR35_total_no_of_acct(BigDecimal r35_total_no_of_acct) {
			this.r35_total_no_of_acct = r35_total_no_of_acct;
		}

		public BigDecimal getR35_total_value() {
			return r35_total_value;
		}

		public void setR35_total_value(BigDecimal r35_total_value) {
			this.r35_total_value = r35_total_value;
		}

		public BigDecimal getR42_total_no_of_acct() {
			return r42_total_no_of_acct;
		}

		public void setR42_total_no_of_acct(BigDecimal r42_total_no_of_acct) {
			this.r42_total_no_of_acct = r42_total_no_of_acct;
		}

		public BigDecimal getR42_total_value() {
			return r42_total_value;
		}

		public void setR42_total_value(BigDecimal r42_total_value) {
			this.r42_total_value = r42_total_value;
		}

		public BigDecimal getR43_total_no_of_acct() {
			return r43_total_no_of_acct;
		}

		public void setR43_total_no_of_acct(BigDecimal r43_total_no_of_acct) {
			this.r43_total_no_of_acct = r43_total_no_of_acct;
		}

		public BigDecimal getR43_total_value() {
			return r43_total_value;
		}

		public void setR43_total_value(BigDecimal r43_total_value) {
			this.r43_total_value = r43_total_value;
		}

		public BigDecimal getR44_total_no_of_acct() {
			return r44_total_no_of_acct;
		}

		public void setR44_total_no_of_acct(BigDecimal r44_total_no_of_acct) {
			this.r44_total_no_of_acct = r44_total_no_of_acct;
		}

		public BigDecimal getR44_total_value() {
			return r44_total_value;
		}

		public void setR44_total_value(BigDecimal r44_total_value) {
			this.r44_total_value = r44_total_value;
		}

		public BigDecimal getR46_total_no_of_acct() {
			return r46_total_no_of_acct;
		}

		public void setR46_total_no_of_acct(BigDecimal r46_total_no_of_acct) {
			this.r46_total_no_of_acct = r46_total_no_of_acct;
		}

		public BigDecimal getR46_total_value() {
			return r46_total_value;
		}

		public void setR46_total_value(BigDecimal r46_total_value) {
			this.r46_total_value = r46_total_value;
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

		public BigDecimal getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(BigDecimal rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
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

	public class ADISB1_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;
		@Column(name = "ACCT_NAME")
		private String acctName;
		@Column(name = "DATA_TYPE")
		private String dataType;
		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_LABLE")
		private String reportLable;
		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;
		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;
		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;
		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;
		@Column(name = "REPORT_NAME")
		private String reportName;
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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
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
	}

	class ADISB1DetailRowMapper implements RowMapper<ADISB1_Detail_Entity> {

		@Override
		public ADISB1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Detail_Entity obj = new ADISB1_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLable(rs.getString("REPORT_LABLE"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));

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

	class SCH17ArchivalDetailRowMapper implements RowMapper<ADISB1_Archival_Detail_Entity> {

		@Override
		public ADISB1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB1_Archival_Detail_Entity obj = new ADISB1_Archival_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));
			obj.setReportLable(rs.getString("REPORT_LABLE"));
			obj.setReportRemarks(rs.getString("REPORT_REMARKS"));
			obj.setModificationRemarks(rs.getString("MODIFICATION_REMARKS"));
			obj.setDataEntryVersion(rs.getString("DATA_ENTRY_VERSION"));

			obj.setAcctBalanceInpula(rs.getBigDecimal("ACCT_BALANCE_IN_PULA"));

			obj.setReportDate(rs.getDate("REPORT_DATE"));
			obj.setReportName(rs.getString("REPORT_NAME"));

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

	public class ADISB1_Archival_Detail_Entity {

		@Column(name = "CUST_ID")
		private String custId;
		@Id
		@Column(name = "ACCT_NUMBER")
		private String acctNumber;
		@Column(name = "ACCT_NAME")
		private String acctName;
		@Column(name = "DATA_TYPE")
		private String dataType;
		@Column(name = "REPORT_ADDL_CRITERIA_1")
		private String reportAddlCriteria1;

		@Column(name = "REPORT_LABLE")
		private String reportLable;
		@Column(name = "REPORT_REMARKS")
		private String reportRemarks;
		@Column(name = "MODIFICATION_REMARKS")
		private String modificationRemarks;
		@Column(name = "DATA_ENTRY_VERSION")
		private String dataEntryVersion;
		@Column(name = "ACCT_BALANCE_IN_PULA", precision = 24, scale = 2)
		private BigDecimal acctBalanceInpula;

		@Column(name = "REPORT_DATE")
		@DateTimeFormat(pattern = "dd-MM-yyyy")
		private Date reportDate;
		@Column(name = "REPORT_NAME")
		private String reportName;
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

		public String getReportAddlCriteria1() {
			return reportAddlCriteria1;
		}

		public void setReportAddlCriteria1(String reportAddlCriteria1) {
			this.reportAddlCriteria1 = reportAddlCriteria1;
		}

		public String getReportLable() {
			return reportLable;
		}

		public void setReportLable(String reportLable) {
			this.reportLable = reportLable;
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

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
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
	}

	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getADISB1View(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("ADISB1 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<ADISB1_Archival_Summary_Entity> T1Master = new ArrayList<>();
			List<ADISB1_Manual_Archival_Summary_Entity> T2Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY ARCHIVAL

				T1Master = getdatabydateListarchival(dt, version);

				System.out.println("Archival Summary size = " + T1Master.size());

				// MANUAL ARCHIVAL

				T2Master = getManualArchivalByDate(dt, version);

				System.out.println("Archival Manual size = " + T2Master.size());

				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// NORMAL MODE

		else {

			List<ADISB1_Summary_Entity> T1Master = new ArrayList<>();
			List<ADISB1_Manual_Summary_Entity> T2Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);

				// SUMMARY NORMAL
				T1Master = getDataByDate(dt);

				System.out.println("Summary size = " + T1Master.size());

				// MANUAL NORMAL
				T2Master = getManualDataByDate(dt);

				System.out.println("Manual size = " + T2Master.size());

				mv.addObject("report_date", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// VIEW SETTINGS

		mv.setViewName("BRRS/ADISB1");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

// MODEL AND VIEW METHOD detail

	public ModelAndView getADISB1currentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<ADISB1_Archival_Detail_Entity> archivalDetailList;

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

				List<ADISB1_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/ADISB1");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getADISB1Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<ADISB1_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (ADISB1_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				ADISB1_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  ADISB1  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public void updateReport(ADISB1_Manual_Summary_Entity updatedEntity) {

		System.out.println("Came to ADISB1 Manual Update");
		System.out.println("Report Date: " + updatedEntity.getReport_date());

		// Allowed rows
		int[] rows = { 23, 25, 26, 30, 34, 35, 42, 43, 44, 46 };

		try {

			// Loop rows
			for (int r : rows) {

				// Two amount columns
				String[] cols = { "total_no_of_acct", "total_value" };

				for (String col : cols) {

					String getterName = "getR" + r + "_" + col;

					try {

						Method getter = ADISB1_Manual_Summary_Entity.class.getMethod(getterName);

						Object value = getter.invoke(updatedEntity);

						// Skip null values
						if (value == null)
							continue;

						// Column name in DB
						String columnName = "R" + r + "_" + col;

						String sql = "UPDATE BRRS_ADISB1_MANUAL_SUMMARYTABLE " + "SET " + columnName + " = ? "
								+ "WHERE REPORT_DATE = ?";

						jdbcTemplate.update(sql, value, updatedEntity.getReport_date());

					} catch (NoSuchMethodException e) {
						// Skip if method not exists
						continue;
					}
				}
			}

			System.out.println("ADISB1 Manual Update Completed");

		} catch (Exception e) {
			throw new RuntimeException("Error while updating ADISB1 Manual fields", e);
		}
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/ADISB1");

		if (acct_number != null) {
			ADISB1_Detail_Entity ADISB1Entity = findByAcctnumber(acct_number);
			if (ADISB1Entity != null && ADISB1Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(ADISB1Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("ADISB1Data", ADISB1Entity);
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

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			// Existing Record
			ADISB1_Detail_Entity existing = findByAcctnumber(acctNo);

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

			// UPDATE
			if (isChanged) {

				String sql = "UPDATE BRRS_ADISB1_DETAILTABLE " +
			             "SET ACCT_NAME = ?, " +
			             "ACCT_BALANCE_IN_PULA = ? " +   // ✅ no comma here
			             "WHERE ACCT_NUMBER = ?";

				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), acctNo);

				System.out.println("Record updated successfully");

				// DATE FORMAT
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
						.format(new SimpleDateFormat("yyyy-MM-dd").parse(reportDateStr));

				// PROCEDURE CALL
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

					@Override
					public void afterCommit() {

						try {

							jdbcTemplate.update("BEGIN BRRS_ADISB1_SUMMARY_PROCEDURE(?); END;", formattedDate);

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

	public byte[] getADISB1DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  ADISB1NEW Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getADISB1DetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ADISB1 Details New");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABEL",
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

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<ADISB1_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (ADISB1_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for ADISB1 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating ADISB1 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getADISB1DetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for ADISB1NEW ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ADISB1 Detail NEW");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCT BALANCE", "REPORT LABEL",
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

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<ADISB1_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (ADISB1_Archival_Detail_Entity item : reportData) {
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

					row.createCell(4).setCellValue(item.getReportLable());
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
				logger.info("No data found for ADISB1NEW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating ADISB1 NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] BRRS_ADISB1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.sch17");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelADISB1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<ADISB1_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		List<ADISB1_Manual_Summary_Entity> dataList1 = getManualDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  ADISB1new report. Returning empty result.");
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

					ADISB1_Summary_Entity record = dataList.get(i);
					ADISB1_Manual_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
// R7 Col B            
					Cell R7cell1 = row.createCell(1);
					if (record.getR7_total_no_of_acct() != null) {
						R7cell1.setCellValue(record.getR7_total_no_of_acct().doubleValue());
						R7cell1.setCellStyle(numberStyle);
					} else {
						R7cell1.setCellValue("");
						R7cell1.setCellStyle(textStyle);
					}

// R7 Col C
					Cell R7cell2 = row.createCell(2);
					if (record.getR7_total_value() != null) {
						R7cell2.setCellValue(record.getR7_total_value().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(7);
//R8 Col B            
					Cell R8cell1 = row.createCell(1);
					if (record.getR8_total_no_of_acct() != null) {
						R8cell1.setCellValue(record.getR8_total_no_of_acct().doubleValue());
						R8cell1.setCellStyle(numberStyle);
					} else {
						R8cell1.setCellValue("");
						R8cell1.setCellStyle(textStyle);
					}

//R8 Col C
					Cell R8cell2 = row.createCell(2);
					if (record.getR8_total_value() != null) {
						R8cell2.setCellValue(record.getR8_total_value().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
//R9 Col B            
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_total_no_of_acct() != null) {
						R9cell1.setCellValue(record.getR9_total_no_of_acct().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

//R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_total_value() != null) {
						R9cell2.setCellValue(record.getR9_total_value().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
//R12 Col B            
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_total_no_of_acct() != null) {
						R12cell1.setCellValue(record.getR12_total_no_of_acct().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

//R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_total_value() != null) {
						R12cell2.setCellValue(record.getR12_total_value().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(12);
//R13 Col B            
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_total_no_of_acct() != null) {
						R13cell1.setCellValue(record.getR13_total_no_of_acct().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

//R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_total_value() != null) {
						R13cell2.setCellValue(record.getR13_total_value().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
//R14 Col B            
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_total_no_of_acct() != null) {
						R14cell1.setCellValue(record.getR14_total_no_of_acct().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

//R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_total_value() != null) {
						R14cell2.setCellValue(record.getR14_total_value().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
//R23 Col B            
					Cell R23cell1 = row.createCell(1);
					if (record1.getR23_total_no_of_acct() != null) {
						R23cell1.setCellValue(record1.getR23_total_no_of_acct().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

//R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record1.getR23_total_value() != null) {
						R23cell2.setCellValue(record1.getR23_total_value().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
//R25 Col B            
					Cell R25cell1 = row.createCell(1);
					if (record1.getR25_total_no_of_acct() != null) {
						R25cell1.setCellValue(record1.getR25_total_no_of_acct().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

//R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record1.getR25_total_value() != null) {
						R25cell2.setCellValue(record1.getR25_total_value().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
//R26 Col B            
					Cell R26cell1 = row.createCell(1);
					if (record1.getR26_total_no_of_acct() != null) {
						R26cell1.setCellValue(record1.getR26_total_no_of_acct().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

//R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record1.getR26_total_value() != null) {
						R26cell2.setCellValue(record1.getR26_total_value().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
//R30 Col B            
					Cell R30cell1 = row.createCell(1);
					if (record1.getR30_total_no_of_acct() != null) {
						R30cell1.setCellValue(record1.getR30_total_no_of_acct().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

//R30 Col C
					Cell R30cell2 = row.createCell(2);
					if (record1.getR30_total_value() != null) {
						R30cell2.setCellValue(record1.getR30_total_value().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
//R34 Col B            
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_total_no_of_acct() != null) {
						R34cell1.setCellValue(record.getR34_total_no_of_acct().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

//R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_total_value() != null) {
						R34cell2.setCellValue(record.getR34_total_no_of_acct().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
//R35 Col B            
					Cell R35cell1 = row.createCell(1);
					if (record1.getR35_total_no_of_acct() != null) {
						R35cell1.setCellValue(record1.getR35_total_no_of_acct().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

//R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record1.getR35_total_value() != null) {
						R35cell2.setCellValue(record1.getR35_total_value().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
//R38 Col B            
					Cell R38cell1 = row.createCell(1);
					if (record.getR38_total_no_of_acct() != null) {
						R38cell1.setCellValue(record.getR38_total_no_of_acct().doubleValue());
						R38cell1.setCellStyle(numberStyle);
					} else {
						R38cell1.setCellValue("");
						R38cell1.setCellStyle(textStyle);
					}

//R38 Col C
					Cell R38cell2 = row.createCell(2);
					if (record.getR38_total_value() != null) {
						R38cell2.setCellValue(record.getR38_total_value().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
//R39 Col B            
					Cell R39cell1 = row.createCell(1);
					if (record.getR39_total_no_of_acct() != null) {
						R39cell1.setCellValue(record.getR39_total_no_of_acct().doubleValue());
						R39cell1.setCellStyle(numberStyle);
					} else {
						R39cell1.setCellValue("");
						R39cell1.setCellStyle(textStyle);
					}

//R39 Col C
					Cell R39cell2 = row.createCell(2);
					if (record.getR39_total_value() != null) {
						R39cell2.setCellValue(record.getR39_total_value().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
//R42 Col B            
					Cell R42cell1 = row.createCell(1);
					if (record1.getR42_total_no_of_acct() != null) {
						R42cell1.setCellValue(record1.getR42_total_no_of_acct().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

//R42 Col C
					Cell R42cell2 = row.createCell(2);
					if (record1.getR42_total_value() != null) {
						R42cell2.setCellValue(record1.getR42_total_value().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
//R43 Col B            
					Cell R43cell1 = row.createCell(1);
					if (record1.getR43_total_no_of_acct() != null) {
						R43cell1.setCellValue(record1.getR43_total_no_of_acct().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

//R43 Col C
					Cell R43cell2 = row.createCell(2);
					if (record1.getR43_total_value() != null) {
						R43cell2.setCellValue(record1.getR43_total_value().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
//R44 Col B            
					Cell R44cell1 = row.createCell(1);
					if (record1.getR44_total_no_of_acct() != null) {
						R44cell1.setCellValue(record1.getR44_total_no_of_acct().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

//R44 Col C
					Cell R44cell2 = row.createCell(2);
					if (record1.getR44_total_value() != null) {
						R44cell2.setCellValue(record1.getR44_total_value().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
//R46 Col B            
					Cell R46cell1 = row.createCell(1);
					if (record1.getR46_total_no_of_acct() != null) {
						R46cell1.setCellValue(record1.getR46_total_no_of_acct().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

//R46 Col C
					Cell R46cell2 = row.createCell(2);
					if (record1.getR46_total_value() != null) {
						R46cell2.setCellValue(record1.getR46_total_value().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
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

	public byte[] getExcelADISB1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<ADISB1_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		List<ADISB1_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalByDate(dateformat.parse(todate),
				version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for ADISB1 new report. Returning empty result.");
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

					ADISB1_Archival_Summary_Entity record = dataList.get(i);
					ADISB1_Manual_Archival_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
// R7 Col B            
					Cell R7cell1 = row.createCell(1);
					if (record.getR7_total_no_of_acct() != null) {
						R7cell1.setCellValue(record.getR7_total_no_of_acct().doubleValue());
						R7cell1.setCellStyle(numberStyle);
					} else {
						R7cell1.setCellValue("");
						R7cell1.setCellStyle(textStyle);
					}

// R7 Col C
					Cell R7cell2 = row.createCell(2);
					if (record.getR7_total_value() != null) {
						R7cell2.setCellValue(record.getR7_total_value().doubleValue());
						R7cell2.setCellStyle(numberStyle);
					} else {
						R7cell2.setCellValue("");
						R7cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(7);
//R8 Col B            
					Cell R8cell1 = row.createCell(1);
					if (record.getR8_total_no_of_acct() != null) {
						R8cell1.setCellValue(record.getR8_total_no_of_acct().doubleValue());
						R8cell1.setCellStyle(numberStyle);
					} else {
						R8cell1.setCellValue("");
						R8cell1.setCellStyle(textStyle);
					}

//R8 Col C
					Cell R8cell2 = row.createCell(2);
					if (record.getR8_total_value() != null) {
						R8cell2.setCellValue(record.getR8_total_value().doubleValue());
						R8cell2.setCellStyle(numberStyle);
					} else {
						R8cell2.setCellValue("");
						R8cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(8);
//R9 Col B            
					Cell R9cell1 = row.createCell(1);
					if (record.getR9_total_no_of_acct() != null) {
						R9cell1.setCellValue(record.getR9_total_no_of_acct().doubleValue());
						R9cell1.setCellStyle(numberStyle);
					} else {
						R9cell1.setCellValue("");
						R9cell1.setCellStyle(textStyle);
					}

//R9 Col C
					Cell R9cell2 = row.createCell(2);
					if (record.getR9_total_value() != null) {
						R9cell2.setCellValue(record.getR9_total_value().doubleValue());
						R9cell2.setCellStyle(numberStyle);
					} else {
						R9cell2.setCellValue("");
						R9cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(11);
//R12 Col B            
					Cell R12cell1 = row.createCell(1);
					if (record.getR12_total_no_of_acct() != null) {
						R12cell1.setCellValue(record.getR12_total_no_of_acct().doubleValue());
						R12cell1.setCellStyle(numberStyle);
					} else {
						R12cell1.setCellValue("");
						R12cell1.setCellStyle(textStyle);
					}

//R12 Col C
					Cell R12cell2 = row.createCell(2);
					if (record.getR12_total_value() != null) {
						R12cell2.setCellValue(record.getR12_total_value().doubleValue());
						R12cell2.setCellStyle(numberStyle);
					} else {
						R12cell2.setCellValue("");
						R12cell2.setCellStyle(textStyle);
					}
					row = sheet.getRow(12);
//R13 Col B            
					Cell R13cell1 = row.createCell(1);
					if (record.getR13_total_no_of_acct() != null) {
						R13cell1.setCellValue(record.getR13_total_no_of_acct().doubleValue());
						R13cell1.setCellStyle(numberStyle);
					} else {
						R13cell1.setCellValue("");
						R13cell1.setCellStyle(textStyle);
					}

//R13 Col C
					Cell R13cell2 = row.createCell(2);
					if (record.getR13_total_value() != null) {
						R13cell2.setCellValue(record.getR13_total_value().doubleValue());
						R13cell2.setCellStyle(numberStyle);
					} else {
						R13cell2.setCellValue("");
						R13cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(13);
//R14 Col B            
					Cell R14cell1 = row.createCell(1);
					if (record.getR14_total_no_of_acct() != null) {
						R14cell1.setCellValue(record.getR14_total_no_of_acct().doubleValue());
						R14cell1.setCellStyle(numberStyle);
					} else {
						R14cell1.setCellValue("");
						R14cell1.setCellStyle(textStyle);
					}

//R14 Col C
					Cell R14cell2 = row.createCell(2);
					if (record.getR14_total_value() != null) {
						R14cell2.setCellValue(record.getR14_total_value().doubleValue());
						R14cell2.setCellStyle(numberStyle);
					} else {
						R14cell2.setCellValue("");
						R14cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(22);
//R23 Col B            
					Cell R23cell1 = row.createCell(1);
					if (record1.getR23_total_no_of_acct() != null) {
						R23cell1.setCellValue(record1.getR23_total_no_of_acct().doubleValue());
						R23cell1.setCellStyle(numberStyle);
					} else {
						R23cell1.setCellValue("");
						R23cell1.setCellStyle(textStyle);
					}

//R23 Col C
					Cell R23cell2 = row.createCell(2);
					if (record1.getR23_total_value() != null) {
						R23cell2.setCellValue(record1.getR23_total_value().doubleValue());
						R23cell2.setCellStyle(numberStyle);
					} else {
						R23cell2.setCellValue("");
						R23cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(24);
//R25 Col B            
					Cell R25cell1 = row.createCell(1);
					if (record1.getR25_total_no_of_acct() != null) {
						R25cell1.setCellValue(record1.getR25_total_no_of_acct().doubleValue());
						R25cell1.setCellStyle(numberStyle);
					} else {
						R25cell1.setCellValue("");
						R25cell1.setCellStyle(textStyle);
					}

//R25 Col C
					Cell R25cell2 = row.createCell(2);
					if (record1.getR25_total_value() != null) {
						R25cell2.setCellValue(record1.getR25_total_value().doubleValue());
						R25cell2.setCellStyle(numberStyle);
					} else {
						R25cell2.setCellValue("");
						R25cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(25);
//R26 Col B            
					Cell R26cell1 = row.createCell(1);
					if (record1.getR26_total_no_of_acct() != null) {
						R26cell1.setCellValue(record1.getR26_total_no_of_acct().doubleValue());
						R26cell1.setCellStyle(numberStyle);
					} else {
						R26cell1.setCellValue("");
						R26cell1.setCellStyle(textStyle);
					}

//R26 Col C
					Cell R26cell2 = row.createCell(2);
					if (record1.getR26_total_value() != null) {
						R26cell2.setCellValue(record1.getR26_total_value().doubleValue());
						R26cell2.setCellStyle(numberStyle);
					} else {
						R26cell2.setCellValue("");
						R26cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(29);
//R30 Col B            
					Cell R30cell1 = row.createCell(1);
					if (record1.getR30_total_no_of_acct() != null) {
						R30cell1.setCellValue(record1.getR30_total_no_of_acct().doubleValue());
						R30cell1.setCellStyle(numberStyle);
					} else {
						R30cell1.setCellValue("");
						R30cell1.setCellStyle(textStyle);
					}

//R30 Col C
					Cell R30cell2 = row.createCell(2);
					if (record1.getR30_total_value() != null) {
						R30cell2.setCellValue(record1.getR30_total_value().doubleValue());
						R30cell2.setCellStyle(numberStyle);
					} else {
						R30cell2.setCellValue("");
						R30cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(33);
//R34 Col B            
					Cell R34cell1 = row.createCell(1);
					if (record.getR34_total_no_of_acct() != null) {
						R34cell1.setCellValue(record.getR34_total_no_of_acct().doubleValue());
						R34cell1.setCellStyle(numberStyle);
					} else {
						R34cell1.setCellValue("");
						R34cell1.setCellStyle(textStyle);
					}

//R34 Col C
					Cell R34cell2 = row.createCell(2);
					if (record.getR34_total_value() != null) {
						R34cell2.setCellValue(record.getR34_total_no_of_acct().doubleValue());
						R34cell2.setCellStyle(numberStyle);
					} else {
						R34cell2.setCellValue("");
						R34cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(34);
//R35 Col B            
					Cell R35cell1 = row.createCell(1);
					if (record1.getR35_total_no_of_acct() != null) {
						R35cell1.setCellValue(record1.getR35_total_no_of_acct().doubleValue());
						R35cell1.setCellStyle(numberStyle);
					} else {
						R35cell1.setCellValue("");
						R35cell1.setCellStyle(textStyle);
					}

//R35 Col C
					Cell R35cell2 = row.createCell(2);
					if (record1.getR35_total_value() != null) {
						R35cell2.setCellValue(record1.getR35_total_value().doubleValue());
						R35cell2.setCellStyle(numberStyle);
					} else {
						R35cell2.setCellValue("");
						R35cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(37);
//R38 Col B            
					Cell R38cell1 = row.createCell(1);
					if (record.getR38_total_no_of_acct() != null) {
						R38cell1.setCellValue(record.getR38_total_no_of_acct().doubleValue());
						R38cell1.setCellStyle(numberStyle);
					} else {
						R38cell1.setCellValue("");
						R38cell1.setCellStyle(textStyle);
					}

//R38 Col C
					Cell R38cell2 = row.createCell(2);
					if (record.getR38_total_value() != null) {
						R38cell2.setCellValue(record.getR38_total_value().doubleValue());
						R38cell2.setCellStyle(numberStyle);
					} else {
						R38cell2.setCellValue("");
						R38cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(38);
//R39 Col B            
					Cell R39cell1 = row.createCell(1);
					if (record.getR39_total_no_of_acct() != null) {
						R39cell1.setCellValue(record.getR39_total_no_of_acct().doubleValue());
						R39cell1.setCellStyle(numberStyle);
					} else {
						R39cell1.setCellValue("");
						R39cell1.setCellStyle(textStyle);
					}

//R39 Col C
					Cell R39cell2 = row.createCell(2);
					if (record.getR39_total_value() != null) {
						R39cell2.setCellValue(record.getR39_total_value().doubleValue());
						R39cell2.setCellStyle(numberStyle);
					} else {
						R39cell2.setCellValue("");
						R39cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(41);
//R42 Col B            
					Cell R42cell1 = row.createCell(1);
					if (record1.getR42_total_no_of_acct() != null) {
						R42cell1.setCellValue(record1.getR42_total_no_of_acct().doubleValue());
						R42cell1.setCellStyle(numberStyle);
					} else {
						R42cell1.setCellValue("");
						R42cell1.setCellStyle(textStyle);
					}

//R42 Col C
					Cell R42cell2 = row.createCell(2);
					if (record1.getR42_total_value() != null) {
						R42cell2.setCellValue(record1.getR42_total_value().doubleValue());
						R42cell2.setCellStyle(numberStyle);
					} else {
						R42cell2.setCellValue("");
						R42cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(42);
//R43 Col B            
					Cell R43cell1 = row.createCell(1);
					if (record1.getR43_total_no_of_acct() != null) {
						R43cell1.setCellValue(record1.getR43_total_no_of_acct().doubleValue());
						R43cell1.setCellStyle(numberStyle);
					} else {
						R43cell1.setCellValue("");
						R43cell1.setCellStyle(textStyle);
					}

//R43 Col C
					Cell R43cell2 = row.createCell(2);
					if (record1.getR43_total_value() != null) {
						R43cell2.setCellValue(record1.getR43_total_value().doubleValue());
						R43cell2.setCellStyle(numberStyle);
					} else {
						R43cell2.setCellValue("");
						R43cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(43);
//R44 Col B            
					Cell R44cell1 = row.createCell(1);
					if (record1.getR44_total_no_of_acct() != null) {
						R44cell1.setCellValue(record1.getR44_total_no_of_acct().doubleValue());
						R44cell1.setCellStyle(numberStyle);
					} else {
						R44cell1.setCellValue("");
						R44cell1.setCellStyle(textStyle);
					}

//R44 Col C
					Cell R44cell2 = row.createCell(2);
					if (record1.getR44_total_value() != null) {
						R44cell2.setCellValue(record1.getR44_total_value().doubleValue());
						R44cell2.setCellStyle(numberStyle);
					} else {
						R44cell2.setCellValue("");
						R44cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(45);
//R46 Col B            
					Cell R46cell1 = row.createCell(1);
					if (record1.getR46_total_no_of_acct() != null) {
						R46cell1.setCellValue(record1.getR46_total_no_of_acct().doubleValue());
						R46cell1.setCellStyle(numberStyle);
					} else {
						R46cell1.setCellValue("");
						R46cell1.setCellStyle(textStyle);
					}

//R46 Col C
					Cell R46cell2 = row.createCell(2);
					if (record1.getR46_total_value() != null) {
						R46cell2.setCellValue(record1.getR46_total_value().doubleValue());
						R46cell2.setCellStyle(numberStyle);
					} else {
						R46cell2.setCellValue("");
						R46cell2.setCellStyle(textStyle);
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