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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
public class BRRS_ADISB2_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_ADISB2_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Fetch data by report date
	public List<ADISB2_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_ADISB2_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new ADISB2RowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getADISB2Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_ADISB2_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<ADISB2_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE, BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_ADISB2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new ADISB2ArchivalRowMapper());
	}
	// GET ALL WITH VERSION

	public List<ADISB2_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_ADISB2_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new ADISB2ArchivalRowMapper());
	}

	// GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_ADISB2_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// 1. BY DATE + LABEL + CRITERIA

	public List<ADISB2_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String ReportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_ADISB2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, ReportLabel, reportAddlCriteria1 },
				new ADISB2DetailRowMapper());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<ADISB2_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_ADISB2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new ADISB2DetailRowMapper());
	}

	// 3. PAGINATION

	public List<ADISB2_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_ADISB2_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new ADISB2DetailRowMapper());
	}

	// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_ADISB2_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<ADISB2_Detail_Entity> GetDetailDataByRowIdAndColumnId(String ReportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_ADISB2_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { ReportLabel, reportAddlCriteria1, reportdate },
				new ADISB2DetailRowMapper());
	}
	// 6. BY ACCOUNT NUMBER

	public ADISB2_Detail_Entity findByAcctnumber(String acctNumber) {

		String sql = "SELECT * FROM BRRS_ADISB2_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new ADISB2DetailRowMapper());
	}

	// 1. GET BY DATE + VERSION

	public List<ADISB2_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
			String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_ADISB2_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
				new ADISB2ArchivalDetailRowMapper());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<ADISB2_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_ADISB2_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new ADISB2ArchivalDetailRowMapper());
	}

	// ROW MAPPER

	class ADISB2RowMapper implements RowMapper<ADISB2_Summary_Entity> {

		@Override
		public ADISB2_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB2_Summary_Entity obj = new ADISB2_Summary_Entity();

			obj.setR6_COVERAGE_LEVEL(rs.getBigDecimal("R6_COVERAGE_LEVEL"));
			obj.setR6_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R6_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR6_COVERED_DEPOSITORS(rs.getBigDecimal("R6_COVERED_DEPOSITORS"));
			obj.setR6_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R6_COVERED_DEPOSITORS_PCT"));
			obj.setR6_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R6_EXCEEDING_DEPOSITORS"));
			obj.setR6_COVERED_AMOUNT_PCT(rs.getBigDecimal("R6_COVERED_AMOUNT_PCT"));

			obj.setR7_COVERAGE_LEVEL(rs.getBigDecimal("R7_COVERAGE_LEVEL"));
			obj.setR7_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R7_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR7_COVERED_DEPOSITORS(rs.getBigDecimal("R7_COVERED_DEPOSITORS"));
			obj.setR7_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R7_COVERED_DEPOSITORS_PCT"));
			obj.setR7_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R7_EXCEEDING_DEPOSITORS"));
			obj.setR7_COVERED_AMOUNT_PCT(rs.getBigDecimal("R7_COVERED_AMOUNT_PCT"));

			obj.setR8_COVERAGE_LEVEL(rs.getBigDecimal("R8_COVERAGE_LEVEL"));
			obj.setR8_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R8_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR8_COVERED_DEPOSITORS(rs.getBigDecimal("R8_COVERED_DEPOSITORS"));
			obj.setR8_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R8_COVERED_DEPOSITORS_PCT"));
			obj.setR8_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R8_EXCEEDING_DEPOSITORS"));
			obj.setR8_COVERED_AMOUNT_PCT(rs.getBigDecimal("R8_COVERED_AMOUNT_PCT"));

			obj.setR9_COVERAGE_LEVEL(rs.getBigDecimal("R9_COVERAGE_LEVEL"));
			obj.setR9_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R9_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR9_COVERED_DEPOSITORS(rs.getBigDecimal("R9_COVERED_DEPOSITORS"));
			obj.setR9_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R9_COVERED_DEPOSITORS_PCT"));
			obj.setR9_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R9_EXCEEDING_DEPOSITORS"));
			obj.setR9_COVERED_AMOUNT_PCT(rs.getBigDecimal("R9_COVERED_AMOUNT_PCT"));

			obj.setR10_COVERAGE_LEVEL(rs.getBigDecimal("R10_COVERAGE_LEVEL"));
			obj.setR10_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R10_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR10_COVERED_DEPOSITORS(rs.getBigDecimal("R10_COVERED_DEPOSITORS"));
			obj.setR10_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R10_COVERED_DEPOSITORS_PCT"));
			obj.setR10_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R10_EXCEEDING_DEPOSITORS"));
			obj.setR10_COVERED_AMOUNT_PCT(rs.getBigDecimal("R10_COVERED_AMOUNT_PCT"));

			obj.setR11_COVERAGE_LEVEL(rs.getBigDecimal("R11_COVERAGE_LEVEL"));
			obj.setR11_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R11_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR11_COVERED_DEPOSITORS(rs.getBigDecimal("R11_COVERED_DEPOSITORS"));
			obj.setR11_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R11_COVERED_DEPOSITORS_PCT"));
			obj.setR11_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R11_EXCEEDING_DEPOSITORS"));
			obj.setR11_COVERED_AMOUNT_PCT(rs.getBigDecimal("R11_COVERED_AMOUNT_PCT"));

			obj.setR12_COVERAGE_LEVEL(rs.getBigDecimal("R12_COVERAGE_LEVEL"));
			obj.setR12_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R12_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR12_COVERED_DEPOSITORS(rs.getBigDecimal("R12_COVERED_DEPOSITORS"));
			obj.setR12_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R12_COVERED_DEPOSITORS_PCT"));
			obj.setR12_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R12_EXCEEDING_DEPOSITORS"));
			obj.setR12_COVERED_AMOUNT_PCT(rs.getBigDecimal("R12_COVERED_AMOUNT_PCT"));

			obj.setR13_COVERAGE_LEVEL(rs.getBigDecimal("R13_COVERAGE_LEVEL"));
			obj.setR13_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R13_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR13_COVERED_DEPOSITORS(rs.getBigDecimal("R13_COVERED_DEPOSITORS"));
			obj.setR13_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R13_COVERED_DEPOSITORS_PCT"));
			obj.setR13_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R13_EXCEEDING_DEPOSITORS"));
			obj.setR13_COVERED_AMOUNT_PCT(rs.getBigDecimal("R13_COVERED_AMOUNT_PCT"));

			obj.setR14_COVERAGE_LEVEL(rs.getBigDecimal("R14_COVERAGE_LEVEL"));
			obj.setR14_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R14_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR14_COVERED_DEPOSITORS(rs.getBigDecimal("R14_COVERED_DEPOSITORS"));
			obj.setR14_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R14_COVERED_DEPOSITORS_PCT"));
			obj.setR14_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R14_EXCEEDING_DEPOSITORS"));
			obj.setR14_COVERED_AMOUNT_PCT(rs.getBigDecimal("R14_COVERED_AMOUNT_PCT"));

			obj.setR15_COVERAGE_LEVEL(rs.getBigDecimal("R15_COVERAGE_LEVEL"));
			obj.setR15_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R15_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR15_COVERED_DEPOSITORS(rs.getBigDecimal("R15_COVERED_DEPOSITORS"));
			obj.setR15_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R15_COVERED_DEPOSITORS_PCT"));
			obj.setR15_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R15_EXCEEDING_DEPOSITORS"));
			obj.setR15_COVERED_AMOUNT_PCT(rs.getBigDecimal("R15_COVERED_AMOUNT_PCT"));

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

	public static class ADISB2_Summary_Entity {

		// -------- R6 --------
		private BigDecimal R6_COVERAGE_LEVEL;
		private BigDecimal R6_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R6_COVERED_DEPOSITORS;
		private BigDecimal R6_COVERED_DEPOSITORS_PCT;
		private BigDecimal R6_EXCEEDING_DEPOSITORS;
		private BigDecimal R6_COVERED_AMOUNT_PCT;

		// -------- R7 --------
		private BigDecimal R7_COVERAGE_LEVEL;
		private BigDecimal R7_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R7_COVERED_DEPOSITORS;
		private BigDecimal R7_COVERED_DEPOSITORS_PCT;
		private BigDecimal R7_EXCEEDING_DEPOSITORS;
		private BigDecimal R7_COVERED_AMOUNT_PCT;

		// -------- R8 --------
		private BigDecimal R8_COVERAGE_LEVEL;
		private BigDecimal R8_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R8_COVERED_DEPOSITORS;
		private BigDecimal R8_COVERED_DEPOSITORS_PCT;
		private BigDecimal R8_EXCEEDING_DEPOSITORS;
		private BigDecimal R8_COVERED_AMOUNT_PCT;

		// -------- R9 --------
		private BigDecimal R9_COVERAGE_LEVEL;
		private BigDecimal R9_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R9_COVERED_DEPOSITORS;
		private BigDecimal R9_COVERED_DEPOSITORS_PCT;
		private BigDecimal R9_EXCEEDING_DEPOSITORS;
		private BigDecimal R9_COVERED_AMOUNT_PCT;

		// -------- R10 --------
		private BigDecimal R10_COVERAGE_LEVEL;
		private BigDecimal R10_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R10_COVERED_DEPOSITORS;
		private BigDecimal R10_COVERED_DEPOSITORS_PCT;
		private BigDecimal R10_EXCEEDING_DEPOSITORS;
		private BigDecimal R10_COVERED_AMOUNT_PCT;

		// -------- R11 --------
		private BigDecimal R11_COVERAGE_LEVEL;
		private BigDecimal R11_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R11_COVERED_DEPOSITORS;
		private BigDecimal R11_COVERED_DEPOSITORS_PCT;
		private BigDecimal R11_EXCEEDING_DEPOSITORS;
		private BigDecimal R11_COVERED_AMOUNT_PCT;

		// -------- R12 --------
		private BigDecimal R12_COVERAGE_LEVEL;
		private BigDecimal R12_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R12_COVERED_DEPOSITORS;
		private BigDecimal R12_COVERED_DEPOSITORS_PCT;
		private BigDecimal R12_EXCEEDING_DEPOSITORS;
		private BigDecimal R12_COVERED_AMOUNT_PCT;

		// -------- R13 --------
		private BigDecimal R13_COVERAGE_LEVEL;
		private BigDecimal R13_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R13_COVERED_DEPOSITORS;
		private BigDecimal R13_COVERED_DEPOSITORS_PCT;
		private BigDecimal R13_EXCEEDING_DEPOSITORS;
		private BigDecimal R13_COVERED_AMOUNT_PCT;

		// -------- R14 --------
		private BigDecimal R14_COVERAGE_LEVEL;
		private BigDecimal R14_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R14_COVERED_DEPOSITORS;
		private BigDecimal R14_COVERED_DEPOSITORS_PCT;
		private BigDecimal R14_EXCEEDING_DEPOSITORS;
		private BigDecimal R14_COVERED_AMOUNT_PCT;

		// -------- R15 --------
		private BigDecimal R15_COVERAGE_LEVEL;
		private BigDecimal R15_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R15_COVERED_DEPOSITORS;
		private BigDecimal R15_COVERED_DEPOSITORS_PCT;
		private BigDecimal R15_EXCEEDING_DEPOSITORS;
		private BigDecimal R15_COVERED_AMOUNT_PCT;
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

		public BigDecimal getR6_COVERAGE_LEVEL() {
			return R6_COVERAGE_LEVEL;
		}

		public void setR6_COVERAGE_LEVEL(BigDecimal r6_COVERAGE_LEVEL) {
			R6_COVERAGE_LEVEL = r6_COVERAGE_LEVEL;
		}

		public BigDecimal getR6_TOTAL_DEPOSIT_AMOUNT() {
			return R6_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR6_TOTAL_DEPOSIT_AMOUNT(BigDecimal r6_TOTAL_DEPOSIT_AMOUNT) {
			R6_TOTAL_DEPOSIT_AMOUNT = r6_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR6_COVERED_DEPOSITORS() {
			return R6_COVERED_DEPOSITORS;
		}

		public void setR6_COVERED_DEPOSITORS(BigDecimal r6_COVERED_DEPOSITORS) {
			R6_COVERED_DEPOSITORS = r6_COVERED_DEPOSITORS;
		}

		public BigDecimal getR6_COVERED_DEPOSITORS_PCT() {
			return R6_COVERED_DEPOSITORS_PCT;
		}

		public void setR6_COVERED_DEPOSITORS_PCT(BigDecimal r6_COVERED_DEPOSITORS_PCT) {
			R6_COVERED_DEPOSITORS_PCT = r6_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR6_EXCEEDING_DEPOSITORS() {
			return R6_EXCEEDING_DEPOSITORS;
		}

		public void setR6_EXCEEDING_DEPOSITORS(BigDecimal r6_EXCEEDING_DEPOSITORS) {
			R6_EXCEEDING_DEPOSITORS = r6_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR6_COVERED_AMOUNT_PCT() {
			return R6_COVERED_AMOUNT_PCT;
		}

		public void setR6_COVERED_AMOUNT_PCT(BigDecimal r6_COVERED_AMOUNT_PCT) {
			R6_COVERED_AMOUNT_PCT = r6_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR7_COVERAGE_LEVEL() {
			return R7_COVERAGE_LEVEL;
		}

		public void setR7_COVERAGE_LEVEL(BigDecimal r7_COVERAGE_LEVEL) {
			R7_COVERAGE_LEVEL = r7_COVERAGE_LEVEL;
		}

		public BigDecimal getR7_TOTAL_DEPOSIT_AMOUNT() {
			return R7_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR7_TOTAL_DEPOSIT_AMOUNT(BigDecimal r7_TOTAL_DEPOSIT_AMOUNT) {
			R7_TOTAL_DEPOSIT_AMOUNT = r7_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR7_COVERED_DEPOSITORS() {
			return R7_COVERED_DEPOSITORS;
		}

		public void setR7_COVERED_DEPOSITORS(BigDecimal r7_COVERED_DEPOSITORS) {
			R7_COVERED_DEPOSITORS = r7_COVERED_DEPOSITORS;
		}

		public BigDecimal getR7_COVERED_DEPOSITORS_PCT() {
			return R7_COVERED_DEPOSITORS_PCT;
		}

		public void setR7_COVERED_DEPOSITORS_PCT(BigDecimal r7_COVERED_DEPOSITORS_PCT) {
			R7_COVERED_DEPOSITORS_PCT = r7_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR7_EXCEEDING_DEPOSITORS() {
			return R7_EXCEEDING_DEPOSITORS;
		}

		public void setR7_EXCEEDING_DEPOSITORS(BigDecimal r7_EXCEEDING_DEPOSITORS) {
			R7_EXCEEDING_DEPOSITORS = r7_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR7_COVERED_AMOUNT_PCT() {
			return R7_COVERED_AMOUNT_PCT;
		}

		public void setR7_COVERED_AMOUNT_PCT(BigDecimal r7_COVERED_AMOUNT_PCT) {
			R7_COVERED_AMOUNT_PCT = r7_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR8_COVERAGE_LEVEL() {
			return R8_COVERAGE_LEVEL;
		}

		public void setR8_COVERAGE_LEVEL(BigDecimal r8_COVERAGE_LEVEL) {
			R8_COVERAGE_LEVEL = r8_COVERAGE_LEVEL;
		}

		public BigDecimal getR8_TOTAL_DEPOSIT_AMOUNT() {
			return R8_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR8_TOTAL_DEPOSIT_AMOUNT(BigDecimal r8_TOTAL_DEPOSIT_AMOUNT) {
			R8_TOTAL_DEPOSIT_AMOUNT = r8_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR8_COVERED_DEPOSITORS() {
			return R8_COVERED_DEPOSITORS;
		}

		public void setR8_COVERED_DEPOSITORS(BigDecimal r8_COVERED_DEPOSITORS) {
			R8_COVERED_DEPOSITORS = r8_COVERED_DEPOSITORS;
		}

		public BigDecimal getR8_COVERED_DEPOSITORS_PCT() {
			return R8_COVERED_DEPOSITORS_PCT;
		}

		public void setR8_COVERED_DEPOSITORS_PCT(BigDecimal r8_COVERED_DEPOSITORS_PCT) {
			R8_COVERED_DEPOSITORS_PCT = r8_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR8_EXCEEDING_DEPOSITORS() {
			return R8_EXCEEDING_DEPOSITORS;
		}

		public void setR8_EXCEEDING_DEPOSITORS(BigDecimal r8_EXCEEDING_DEPOSITORS) {
			R8_EXCEEDING_DEPOSITORS = r8_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR8_COVERED_AMOUNT_PCT() {
			return R8_COVERED_AMOUNT_PCT;
		}

		public void setR8_COVERED_AMOUNT_PCT(BigDecimal r8_COVERED_AMOUNT_PCT) {
			R8_COVERED_AMOUNT_PCT = r8_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR9_COVERAGE_LEVEL() {
			return R9_COVERAGE_LEVEL;
		}

		public void setR9_COVERAGE_LEVEL(BigDecimal r9_COVERAGE_LEVEL) {
			R9_COVERAGE_LEVEL = r9_COVERAGE_LEVEL;
		}

		public BigDecimal getR9_TOTAL_DEPOSIT_AMOUNT() {
			return R9_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR9_TOTAL_DEPOSIT_AMOUNT(BigDecimal r9_TOTAL_DEPOSIT_AMOUNT) {
			R9_TOTAL_DEPOSIT_AMOUNT = r9_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR9_COVERED_DEPOSITORS() {
			return R9_COVERED_DEPOSITORS;
		}

		public void setR9_COVERED_DEPOSITORS(BigDecimal r9_COVERED_DEPOSITORS) {
			R9_COVERED_DEPOSITORS = r9_COVERED_DEPOSITORS;
		}

		public BigDecimal getR9_COVERED_DEPOSITORS_PCT() {
			return R9_COVERED_DEPOSITORS_PCT;
		}

		public void setR9_COVERED_DEPOSITORS_PCT(BigDecimal r9_COVERED_DEPOSITORS_PCT) {
			R9_COVERED_DEPOSITORS_PCT = r9_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR9_EXCEEDING_DEPOSITORS() {
			return R9_EXCEEDING_DEPOSITORS;
		}

		public void setR9_EXCEEDING_DEPOSITORS(BigDecimal r9_EXCEEDING_DEPOSITORS) {
			R9_EXCEEDING_DEPOSITORS = r9_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR9_COVERED_AMOUNT_PCT() {
			return R9_COVERED_AMOUNT_PCT;
		}

		public void setR9_COVERED_AMOUNT_PCT(BigDecimal r9_COVERED_AMOUNT_PCT) {
			R9_COVERED_AMOUNT_PCT = r9_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR10_COVERAGE_LEVEL() {
			return R10_COVERAGE_LEVEL;
		}

		public void setR10_COVERAGE_LEVEL(BigDecimal r10_COVERAGE_LEVEL) {
			R10_COVERAGE_LEVEL = r10_COVERAGE_LEVEL;
		}

		public BigDecimal getR10_TOTAL_DEPOSIT_AMOUNT() {
			return R10_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR10_TOTAL_DEPOSIT_AMOUNT(BigDecimal r10_TOTAL_DEPOSIT_AMOUNT) {
			R10_TOTAL_DEPOSIT_AMOUNT = r10_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR10_COVERED_DEPOSITORS() {
			return R10_COVERED_DEPOSITORS;
		}

		public void setR10_COVERED_DEPOSITORS(BigDecimal r10_COVERED_DEPOSITORS) {
			R10_COVERED_DEPOSITORS = r10_COVERED_DEPOSITORS;
		}

		public BigDecimal getR10_COVERED_DEPOSITORS_PCT() {
			return R10_COVERED_DEPOSITORS_PCT;
		}

		public void setR10_COVERED_DEPOSITORS_PCT(BigDecimal r10_COVERED_DEPOSITORS_PCT) {
			R10_COVERED_DEPOSITORS_PCT = r10_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR10_EXCEEDING_DEPOSITORS() {
			return R10_EXCEEDING_DEPOSITORS;
		}

		public void setR10_EXCEEDING_DEPOSITORS(BigDecimal r10_EXCEEDING_DEPOSITORS) {
			R10_EXCEEDING_DEPOSITORS = r10_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR10_COVERED_AMOUNT_PCT() {
			return R10_COVERED_AMOUNT_PCT;
		}

		public void setR10_COVERED_AMOUNT_PCT(BigDecimal r10_COVERED_AMOUNT_PCT) {
			R10_COVERED_AMOUNT_PCT = r10_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR11_COVERAGE_LEVEL() {
			return R11_COVERAGE_LEVEL;
		}

		public void setR11_COVERAGE_LEVEL(BigDecimal r11_COVERAGE_LEVEL) {
			R11_COVERAGE_LEVEL = r11_COVERAGE_LEVEL;
		}

		public BigDecimal getR11_TOTAL_DEPOSIT_AMOUNT() {
			return R11_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR11_TOTAL_DEPOSIT_AMOUNT(BigDecimal r11_TOTAL_DEPOSIT_AMOUNT) {
			R11_TOTAL_DEPOSIT_AMOUNT = r11_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR11_COVERED_DEPOSITORS() {
			return R11_COVERED_DEPOSITORS;
		}

		public void setR11_COVERED_DEPOSITORS(BigDecimal r11_COVERED_DEPOSITORS) {
			R11_COVERED_DEPOSITORS = r11_COVERED_DEPOSITORS;
		}

		public BigDecimal getR11_COVERED_DEPOSITORS_PCT() {
			return R11_COVERED_DEPOSITORS_PCT;
		}

		public void setR11_COVERED_DEPOSITORS_PCT(BigDecimal r11_COVERED_DEPOSITORS_PCT) {
			R11_COVERED_DEPOSITORS_PCT = r11_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR11_EXCEEDING_DEPOSITORS() {
			return R11_EXCEEDING_DEPOSITORS;
		}

		public void setR11_EXCEEDING_DEPOSITORS(BigDecimal r11_EXCEEDING_DEPOSITORS) {
			R11_EXCEEDING_DEPOSITORS = r11_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR11_COVERED_AMOUNT_PCT() {
			return R11_COVERED_AMOUNT_PCT;
		}

		public void setR11_COVERED_AMOUNT_PCT(BigDecimal r11_COVERED_AMOUNT_PCT) {
			R11_COVERED_AMOUNT_PCT = r11_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR12_COVERAGE_LEVEL() {
			return R12_COVERAGE_LEVEL;
		}

		public void setR12_COVERAGE_LEVEL(BigDecimal r12_COVERAGE_LEVEL) {
			R12_COVERAGE_LEVEL = r12_COVERAGE_LEVEL;
		}

		public BigDecimal getR12_TOTAL_DEPOSIT_AMOUNT() {
			return R12_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR12_TOTAL_DEPOSIT_AMOUNT(BigDecimal r12_TOTAL_DEPOSIT_AMOUNT) {
			R12_TOTAL_DEPOSIT_AMOUNT = r12_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR12_COVERED_DEPOSITORS() {
			return R12_COVERED_DEPOSITORS;
		}

		public void setR12_COVERED_DEPOSITORS(BigDecimal r12_COVERED_DEPOSITORS) {
			R12_COVERED_DEPOSITORS = r12_COVERED_DEPOSITORS;
		}

		public BigDecimal getR12_COVERED_DEPOSITORS_PCT() {
			return R12_COVERED_DEPOSITORS_PCT;
		}

		public void setR12_COVERED_DEPOSITORS_PCT(BigDecimal r12_COVERED_DEPOSITORS_PCT) {
			R12_COVERED_DEPOSITORS_PCT = r12_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR12_EXCEEDING_DEPOSITORS() {
			return R12_EXCEEDING_DEPOSITORS;
		}

		public void setR12_EXCEEDING_DEPOSITORS(BigDecimal r12_EXCEEDING_DEPOSITORS) {
			R12_EXCEEDING_DEPOSITORS = r12_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR12_COVERED_AMOUNT_PCT() {
			return R12_COVERED_AMOUNT_PCT;
		}

		public void setR12_COVERED_AMOUNT_PCT(BigDecimal r12_COVERED_AMOUNT_PCT) {
			R12_COVERED_AMOUNT_PCT = r12_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR13_COVERAGE_LEVEL() {
			return R13_COVERAGE_LEVEL;
		}

		public void setR13_COVERAGE_LEVEL(BigDecimal r13_COVERAGE_LEVEL) {
			R13_COVERAGE_LEVEL = r13_COVERAGE_LEVEL;
		}

		public BigDecimal getR13_TOTAL_DEPOSIT_AMOUNT() {
			return R13_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR13_TOTAL_DEPOSIT_AMOUNT(BigDecimal r13_TOTAL_DEPOSIT_AMOUNT) {
			R13_TOTAL_DEPOSIT_AMOUNT = r13_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR13_COVERED_DEPOSITORS() {
			return R13_COVERED_DEPOSITORS;
		}

		public void setR13_COVERED_DEPOSITORS(BigDecimal r13_COVERED_DEPOSITORS) {
			R13_COVERED_DEPOSITORS = r13_COVERED_DEPOSITORS;
		}

		public BigDecimal getR13_COVERED_DEPOSITORS_PCT() {
			return R13_COVERED_DEPOSITORS_PCT;
		}

		public void setR13_COVERED_DEPOSITORS_PCT(BigDecimal r13_COVERED_DEPOSITORS_PCT) {
			R13_COVERED_DEPOSITORS_PCT = r13_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR13_EXCEEDING_DEPOSITORS() {
			return R13_EXCEEDING_DEPOSITORS;
		}

		public void setR13_EXCEEDING_DEPOSITORS(BigDecimal r13_EXCEEDING_DEPOSITORS) {
			R13_EXCEEDING_DEPOSITORS = r13_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR13_COVERED_AMOUNT_PCT() {
			return R13_COVERED_AMOUNT_PCT;
		}

		public void setR13_COVERED_AMOUNT_PCT(BigDecimal r13_COVERED_AMOUNT_PCT) {
			R13_COVERED_AMOUNT_PCT = r13_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR14_COVERAGE_LEVEL() {
			return R14_COVERAGE_LEVEL;
		}

		public void setR14_COVERAGE_LEVEL(BigDecimal r14_COVERAGE_LEVEL) {
			R14_COVERAGE_LEVEL = r14_COVERAGE_LEVEL;
		}

		public BigDecimal getR14_TOTAL_DEPOSIT_AMOUNT() {
			return R14_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR14_TOTAL_DEPOSIT_AMOUNT(BigDecimal r14_TOTAL_DEPOSIT_AMOUNT) {
			R14_TOTAL_DEPOSIT_AMOUNT = r14_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR14_COVERED_DEPOSITORS() {
			return R14_COVERED_DEPOSITORS;
		}

		public void setR14_COVERED_DEPOSITORS(BigDecimal r14_COVERED_DEPOSITORS) {
			R14_COVERED_DEPOSITORS = r14_COVERED_DEPOSITORS;
		}

		public BigDecimal getR14_COVERED_DEPOSITORS_PCT() {
			return R14_COVERED_DEPOSITORS_PCT;
		}

		public void setR14_COVERED_DEPOSITORS_PCT(BigDecimal r14_COVERED_DEPOSITORS_PCT) {
			R14_COVERED_DEPOSITORS_PCT = r14_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR14_EXCEEDING_DEPOSITORS() {
			return R14_EXCEEDING_DEPOSITORS;
		}

		public void setR14_EXCEEDING_DEPOSITORS(BigDecimal r14_EXCEEDING_DEPOSITORS) {
			R14_EXCEEDING_DEPOSITORS = r14_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR14_COVERED_AMOUNT_PCT() {
			return R14_COVERED_AMOUNT_PCT;
		}

		public void setR14_COVERED_AMOUNT_PCT(BigDecimal r14_COVERED_AMOUNT_PCT) {
			R14_COVERED_AMOUNT_PCT = r14_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR15_COVERAGE_LEVEL() {
			return R15_COVERAGE_LEVEL;
		}

		public void setR15_COVERAGE_LEVEL(BigDecimal r15_COVERAGE_LEVEL) {
			R15_COVERAGE_LEVEL = r15_COVERAGE_LEVEL;
		}

		public BigDecimal getR15_TOTAL_DEPOSIT_AMOUNT() {
			return R15_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR15_TOTAL_DEPOSIT_AMOUNT(BigDecimal r15_TOTAL_DEPOSIT_AMOUNT) {
			R15_TOTAL_DEPOSIT_AMOUNT = r15_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR15_COVERED_DEPOSITORS() {
			return R15_COVERED_DEPOSITORS;
		}

		public void setR15_COVERED_DEPOSITORS(BigDecimal r15_COVERED_DEPOSITORS) {
			R15_COVERED_DEPOSITORS = r15_COVERED_DEPOSITORS;
		}

		public BigDecimal getR15_COVERED_DEPOSITORS_PCT() {
			return R15_COVERED_DEPOSITORS_PCT;
		}

		public void setR15_COVERED_DEPOSITORS_PCT(BigDecimal r15_COVERED_DEPOSITORS_PCT) {
			R15_COVERED_DEPOSITORS_PCT = r15_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR15_EXCEEDING_DEPOSITORS() {
			return R15_EXCEEDING_DEPOSITORS;
		}

		public void setR15_EXCEEDING_DEPOSITORS(BigDecimal r15_EXCEEDING_DEPOSITORS) {
			R15_EXCEEDING_DEPOSITORS = r15_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR15_COVERED_AMOUNT_PCT() {
			return R15_COVERED_AMOUNT_PCT;
		}

		public void setR15_COVERED_AMOUNT_PCT(BigDecimal r15_COVERED_AMOUNT_PCT) {
			R15_COVERED_AMOUNT_PCT = r15_COVERED_AMOUNT_PCT;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			REPORT_DATE = REPORT_DATE;
		}

		public BigDecimal getREPORT_VERSION() {
			return REPORT_VERSION;
		}

		public void setREPORT_VERSION(BigDecimal REPORT_VERSION) {
			REPORT_VERSION = REPORT_VERSION;
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

	class ADISB2ArchivalRowMapper implements RowMapper<ADISB2_Archival_Summary_Entity> {

		@Override
		public ADISB2_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB2_Archival_Summary_Entity obj = new ADISB2_Archival_Summary_Entity();

			obj.setR6_COVERAGE_LEVEL(rs.getBigDecimal("R6_COVERAGE_LEVEL"));
			obj.setR6_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R6_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR6_COVERED_DEPOSITORS(rs.getBigDecimal("R6_COVERED_DEPOSITORS"));
			obj.setR6_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R6_COVERED_DEPOSITORS_PCT"));
			obj.setR6_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R6_EXCEEDING_DEPOSITORS"));
			obj.setR6_COVERED_AMOUNT_PCT(rs.getBigDecimal("R6_COVERED_AMOUNT_PCT"));

			obj.setR7_COVERAGE_LEVEL(rs.getBigDecimal("R7_COVERAGE_LEVEL"));
			obj.setR7_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R7_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR7_COVERED_DEPOSITORS(rs.getBigDecimal("R7_COVERED_DEPOSITORS"));
			obj.setR7_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R7_COVERED_DEPOSITORS_PCT"));
			obj.setR7_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R7_EXCEEDING_DEPOSITORS"));
			obj.setR7_COVERED_AMOUNT_PCT(rs.getBigDecimal("R7_COVERED_AMOUNT_PCT"));

			obj.setR8_COVERAGE_LEVEL(rs.getBigDecimal("R8_COVERAGE_LEVEL"));
			obj.setR8_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R8_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR8_COVERED_DEPOSITORS(rs.getBigDecimal("R8_COVERED_DEPOSITORS"));
			obj.setR8_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R8_COVERED_DEPOSITORS_PCT"));
			obj.setR8_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R8_EXCEEDING_DEPOSITORS"));
			obj.setR8_COVERED_AMOUNT_PCT(rs.getBigDecimal("R8_COVERED_AMOUNT_PCT"));

			obj.setR9_COVERAGE_LEVEL(rs.getBigDecimal("R9_COVERAGE_LEVEL"));
			obj.setR9_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R9_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR9_COVERED_DEPOSITORS(rs.getBigDecimal("R9_COVERED_DEPOSITORS"));
			obj.setR9_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R9_COVERED_DEPOSITORS_PCT"));
			obj.setR9_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R9_EXCEEDING_DEPOSITORS"));
			obj.setR9_COVERED_AMOUNT_PCT(rs.getBigDecimal("R9_COVERED_AMOUNT_PCT"));

			obj.setR10_COVERAGE_LEVEL(rs.getBigDecimal("R10_COVERAGE_LEVEL"));
			obj.setR10_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R10_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR10_COVERED_DEPOSITORS(rs.getBigDecimal("R10_COVERED_DEPOSITORS"));
			obj.setR10_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R10_COVERED_DEPOSITORS_PCT"));
			obj.setR10_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R10_EXCEEDING_DEPOSITORS"));
			obj.setR10_COVERED_AMOUNT_PCT(rs.getBigDecimal("R10_COVERED_AMOUNT_PCT"));

			obj.setR11_COVERAGE_LEVEL(rs.getBigDecimal("R11_COVERAGE_LEVEL"));
			obj.setR11_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R11_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR11_COVERED_DEPOSITORS(rs.getBigDecimal("R11_COVERED_DEPOSITORS"));
			obj.setR11_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R11_COVERED_DEPOSITORS_PCT"));
			obj.setR11_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R11_EXCEEDING_DEPOSITORS"));
			obj.setR11_COVERED_AMOUNT_PCT(rs.getBigDecimal("R11_COVERED_AMOUNT_PCT"));

			obj.setR12_COVERAGE_LEVEL(rs.getBigDecimal("R12_COVERAGE_LEVEL"));
			obj.setR12_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R12_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR12_COVERED_DEPOSITORS(rs.getBigDecimal("R12_COVERED_DEPOSITORS"));
			obj.setR12_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R12_COVERED_DEPOSITORS_PCT"));
			obj.setR12_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R12_EXCEEDING_DEPOSITORS"));
			obj.setR12_COVERED_AMOUNT_PCT(rs.getBigDecimal("R12_COVERED_AMOUNT_PCT"));

			obj.setR13_COVERAGE_LEVEL(rs.getBigDecimal("R13_COVERAGE_LEVEL"));
			obj.setR13_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R13_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR13_COVERED_DEPOSITORS(rs.getBigDecimal("R13_COVERED_DEPOSITORS"));
			obj.setR13_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R13_COVERED_DEPOSITORS_PCT"));
			obj.setR13_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R13_EXCEEDING_DEPOSITORS"));
			obj.setR13_COVERED_AMOUNT_PCT(rs.getBigDecimal("R13_COVERED_AMOUNT_PCT"));

			obj.setR14_COVERAGE_LEVEL(rs.getBigDecimal("R14_COVERAGE_LEVEL"));
			obj.setR14_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R14_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR14_COVERED_DEPOSITORS(rs.getBigDecimal("R14_COVERED_DEPOSITORS"));
			obj.setR14_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R14_COVERED_DEPOSITORS_PCT"));
			obj.setR14_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R14_EXCEEDING_DEPOSITORS"));
			obj.setR14_COVERED_AMOUNT_PCT(rs.getBigDecimal("R14_COVERED_AMOUNT_PCT"));

			obj.setR15_COVERAGE_LEVEL(rs.getBigDecimal("R15_COVERAGE_LEVEL"));
			obj.setR15_TOTAL_DEPOSIT_AMOUNT(rs.getBigDecimal("R15_TOTAL_DEPOSIT_AMOUNT"));
			obj.setR15_COVERED_DEPOSITORS(rs.getBigDecimal("R15_COVERED_DEPOSITORS"));
			obj.setR15_COVERED_DEPOSITORS_PCT(rs.getBigDecimal("R15_COVERED_DEPOSITORS_PCT"));
			obj.setR15_EXCEEDING_DEPOSITORS(rs.getBigDecimal("R15_EXCEEDING_DEPOSITORS"));
			obj.setR15_COVERED_AMOUNT_PCT(rs.getBigDecimal("R15_COVERED_AMOUNT_PCT"));

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

	@IdClass(ADISB2_PK.class)
	public class ADISB2_Archival_Summary_Entity {
		// -------- R6 --------
		private BigDecimal R6_COVERAGE_LEVEL;
		private BigDecimal R6_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R6_COVERED_DEPOSITORS;
		private BigDecimal R6_COVERED_DEPOSITORS_PCT;
		private BigDecimal R6_EXCEEDING_DEPOSITORS;
		private BigDecimal R6_COVERED_AMOUNT_PCT;

		// -------- R7 --------
		private BigDecimal R7_COVERAGE_LEVEL;
		private BigDecimal R7_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R7_COVERED_DEPOSITORS;
		private BigDecimal R7_COVERED_DEPOSITORS_PCT;
		private BigDecimal R7_EXCEEDING_DEPOSITORS;
		private BigDecimal R7_COVERED_AMOUNT_PCT;

		// -------- R8 --------
		private BigDecimal R8_COVERAGE_LEVEL;
		private BigDecimal R8_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R8_COVERED_DEPOSITORS;
		private BigDecimal R8_COVERED_DEPOSITORS_PCT;
		private BigDecimal R8_EXCEEDING_DEPOSITORS;
		private BigDecimal R8_COVERED_AMOUNT_PCT;

		// -------- R9 --------
		private BigDecimal R9_COVERAGE_LEVEL;
		private BigDecimal R9_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R9_COVERED_DEPOSITORS;
		private BigDecimal R9_COVERED_DEPOSITORS_PCT;
		private BigDecimal R9_EXCEEDING_DEPOSITORS;
		private BigDecimal R9_COVERED_AMOUNT_PCT;

		// -------- R10 --------
		private BigDecimal R10_COVERAGE_LEVEL;
		private BigDecimal R10_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R10_COVERED_DEPOSITORS;
		private BigDecimal R10_COVERED_DEPOSITORS_PCT;
		private BigDecimal R10_EXCEEDING_DEPOSITORS;
		private BigDecimal R10_COVERED_AMOUNT_PCT;

		// -------- R11 --------
		private BigDecimal R11_COVERAGE_LEVEL;
		private BigDecimal R11_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R11_COVERED_DEPOSITORS;
		private BigDecimal R11_COVERED_DEPOSITORS_PCT;
		private BigDecimal R11_EXCEEDING_DEPOSITORS;
		private BigDecimal R11_COVERED_AMOUNT_PCT;

		// -------- R12 --------
		private BigDecimal R12_COVERAGE_LEVEL;
		private BigDecimal R12_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R12_COVERED_DEPOSITORS;
		private BigDecimal R12_COVERED_DEPOSITORS_PCT;
		private BigDecimal R12_EXCEEDING_DEPOSITORS;
		private BigDecimal R12_COVERED_AMOUNT_PCT;

		// -------- R13 --------
		private BigDecimal R13_COVERAGE_LEVEL;
		private BigDecimal R13_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R13_COVERED_DEPOSITORS;
		private BigDecimal R13_COVERED_DEPOSITORS_PCT;
		private BigDecimal R13_EXCEEDING_DEPOSITORS;
		private BigDecimal R13_COVERED_AMOUNT_PCT;

		// -------- R14 --------
		private BigDecimal R14_COVERAGE_LEVEL;
		private BigDecimal R14_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R14_COVERED_DEPOSITORS;
		private BigDecimal R14_COVERED_DEPOSITORS_PCT;
		private BigDecimal R14_EXCEEDING_DEPOSITORS;
		private BigDecimal R14_COVERED_AMOUNT_PCT;

		// -------- R15 --------
		private BigDecimal R15_COVERAGE_LEVEL;
		private BigDecimal R15_TOTAL_DEPOSIT_AMOUNT;
		private BigDecimal R15_COVERED_DEPOSITORS;
		private BigDecimal R15_COVERED_DEPOSITORS_PCT;
		private BigDecimal R15_EXCEEDING_DEPOSITORS;
		private BigDecimal R15_COVERED_AMOUNT_PCT;

		@Id
		@Temporal(TemporalType.DATE)
		@Column(name = "REPORT_DATE")
		private Date REPORT_DATE;
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

		public BigDecimal getR6_COVERAGE_LEVEL() {
			return R6_COVERAGE_LEVEL;
		}

		public void setR6_COVERAGE_LEVEL(BigDecimal r6_COVERAGE_LEVEL) {
			R6_COVERAGE_LEVEL = r6_COVERAGE_LEVEL;
		}

		public BigDecimal getR6_TOTAL_DEPOSIT_AMOUNT() {
			return R6_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR6_TOTAL_DEPOSIT_AMOUNT(BigDecimal r6_TOTAL_DEPOSIT_AMOUNT) {
			R6_TOTAL_DEPOSIT_AMOUNT = r6_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR6_COVERED_DEPOSITORS() {
			return R6_COVERED_DEPOSITORS;
		}

		public void setR6_COVERED_DEPOSITORS(BigDecimal r6_COVERED_DEPOSITORS) {
			R6_COVERED_DEPOSITORS = r6_COVERED_DEPOSITORS;
		}

		public BigDecimal getR6_COVERED_DEPOSITORS_PCT() {
			return R6_COVERED_DEPOSITORS_PCT;
		}

		public void setR6_COVERED_DEPOSITORS_PCT(BigDecimal r6_COVERED_DEPOSITORS_PCT) {
			R6_COVERED_DEPOSITORS_PCT = r6_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR6_EXCEEDING_DEPOSITORS() {
			return R6_EXCEEDING_DEPOSITORS;
		}

		public void setR6_EXCEEDING_DEPOSITORS(BigDecimal r6_EXCEEDING_DEPOSITORS) {
			R6_EXCEEDING_DEPOSITORS = r6_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR6_COVERED_AMOUNT_PCT() {
			return R6_COVERED_AMOUNT_PCT;
		}

		public void setR6_COVERED_AMOUNT_PCT(BigDecimal r6_COVERED_AMOUNT_PCT) {
			R6_COVERED_AMOUNT_PCT = r6_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR7_COVERAGE_LEVEL() {
			return R7_COVERAGE_LEVEL;
		}

		public void setR7_COVERAGE_LEVEL(BigDecimal r7_COVERAGE_LEVEL) {
			R7_COVERAGE_LEVEL = r7_COVERAGE_LEVEL;
		}

		public BigDecimal getR7_TOTAL_DEPOSIT_AMOUNT() {
			return R7_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR7_TOTAL_DEPOSIT_AMOUNT(BigDecimal r7_TOTAL_DEPOSIT_AMOUNT) {
			R7_TOTAL_DEPOSIT_AMOUNT = r7_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR7_COVERED_DEPOSITORS() {
			return R7_COVERED_DEPOSITORS;
		}

		public void setR7_COVERED_DEPOSITORS(BigDecimal r7_COVERED_DEPOSITORS) {
			R7_COVERED_DEPOSITORS = r7_COVERED_DEPOSITORS;
		}

		public BigDecimal getR7_COVERED_DEPOSITORS_PCT() {
			return R7_COVERED_DEPOSITORS_PCT;
		}

		public void setR7_COVERED_DEPOSITORS_PCT(BigDecimal r7_COVERED_DEPOSITORS_PCT) {
			R7_COVERED_DEPOSITORS_PCT = r7_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR7_EXCEEDING_DEPOSITORS() {
			return R7_EXCEEDING_DEPOSITORS;
		}

		public void setR7_EXCEEDING_DEPOSITORS(BigDecimal r7_EXCEEDING_DEPOSITORS) {
			R7_EXCEEDING_DEPOSITORS = r7_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR7_COVERED_AMOUNT_PCT() {
			return R7_COVERED_AMOUNT_PCT;
		}

		public void setR7_COVERED_AMOUNT_PCT(BigDecimal r7_COVERED_AMOUNT_PCT) {
			R7_COVERED_AMOUNT_PCT = r7_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR8_COVERAGE_LEVEL() {
			return R8_COVERAGE_LEVEL;
		}

		public void setR8_COVERAGE_LEVEL(BigDecimal r8_COVERAGE_LEVEL) {
			R8_COVERAGE_LEVEL = r8_COVERAGE_LEVEL;
		}

		public BigDecimal getR8_TOTAL_DEPOSIT_AMOUNT() {
			return R8_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR8_TOTAL_DEPOSIT_AMOUNT(BigDecimal r8_TOTAL_DEPOSIT_AMOUNT) {
			R8_TOTAL_DEPOSIT_AMOUNT = r8_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR8_COVERED_DEPOSITORS() {
			return R8_COVERED_DEPOSITORS;
		}

		public void setR8_COVERED_DEPOSITORS(BigDecimal r8_COVERED_DEPOSITORS) {
			R8_COVERED_DEPOSITORS = r8_COVERED_DEPOSITORS;
		}

		public BigDecimal getR8_COVERED_DEPOSITORS_PCT() {
			return R8_COVERED_DEPOSITORS_PCT;
		}

		public void setR8_COVERED_DEPOSITORS_PCT(BigDecimal r8_COVERED_DEPOSITORS_PCT) {
			R8_COVERED_DEPOSITORS_PCT = r8_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR8_EXCEEDING_DEPOSITORS() {
			return R8_EXCEEDING_DEPOSITORS;
		}

		public void setR8_EXCEEDING_DEPOSITORS(BigDecimal r8_EXCEEDING_DEPOSITORS) {
			R8_EXCEEDING_DEPOSITORS = r8_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR8_COVERED_AMOUNT_PCT() {
			return R8_COVERED_AMOUNT_PCT;
		}

		public void setR8_COVERED_AMOUNT_PCT(BigDecimal r8_COVERED_AMOUNT_PCT) {
			R8_COVERED_AMOUNT_PCT = r8_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR9_COVERAGE_LEVEL() {
			return R9_COVERAGE_LEVEL;
		}

		public void setR9_COVERAGE_LEVEL(BigDecimal r9_COVERAGE_LEVEL) {
			R9_COVERAGE_LEVEL = r9_COVERAGE_LEVEL;
		}

		public BigDecimal getR9_TOTAL_DEPOSIT_AMOUNT() {
			return R9_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR9_TOTAL_DEPOSIT_AMOUNT(BigDecimal r9_TOTAL_DEPOSIT_AMOUNT) {
			R9_TOTAL_DEPOSIT_AMOUNT = r9_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR9_COVERED_DEPOSITORS() {
			return R9_COVERED_DEPOSITORS;
		}

		public void setR9_COVERED_DEPOSITORS(BigDecimal r9_COVERED_DEPOSITORS) {
			R9_COVERED_DEPOSITORS = r9_COVERED_DEPOSITORS;
		}

		public BigDecimal getR9_COVERED_DEPOSITORS_PCT() {
			return R9_COVERED_DEPOSITORS_PCT;
		}

		public void setR9_COVERED_DEPOSITORS_PCT(BigDecimal r9_COVERED_DEPOSITORS_PCT) {
			R9_COVERED_DEPOSITORS_PCT = r9_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR9_EXCEEDING_DEPOSITORS() {
			return R9_EXCEEDING_DEPOSITORS;
		}

		public void setR9_EXCEEDING_DEPOSITORS(BigDecimal r9_EXCEEDING_DEPOSITORS) {
			R9_EXCEEDING_DEPOSITORS = r9_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR9_COVERED_AMOUNT_PCT() {
			return R9_COVERED_AMOUNT_PCT;
		}

		public void setR9_COVERED_AMOUNT_PCT(BigDecimal r9_COVERED_AMOUNT_PCT) {
			R9_COVERED_AMOUNT_PCT = r9_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR10_COVERAGE_LEVEL() {
			return R10_COVERAGE_LEVEL;
		}

		public void setR10_COVERAGE_LEVEL(BigDecimal r10_COVERAGE_LEVEL) {
			R10_COVERAGE_LEVEL = r10_COVERAGE_LEVEL;
		}

		public BigDecimal getR10_TOTAL_DEPOSIT_AMOUNT() {
			return R10_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR10_TOTAL_DEPOSIT_AMOUNT(BigDecimal r10_TOTAL_DEPOSIT_AMOUNT) {
			R10_TOTAL_DEPOSIT_AMOUNT = r10_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR10_COVERED_DEPOSITORS() {
			return R10_COVERED_DEPOSITORS;
		}

		public void setR10_COVERED_DEPOSITORS(BigDecimal r10_COVERED_DEPOSITORS) {
			R10_COVERED_DEPOSITORS = r10_COVERED_DEPOSITORS;
		}

		public BigDecimal getR10_COVERED_DEPOSITORS_PCT() {
			return R10_COVERED_DEPOSITORS_PCT;
		}

		public void setR10_COVERED_DEPOSITORS_PCT(BigDecimal r10_COVERED_DEPOSITORS_PCT) {
			R10_COVERED_DEPOSITORS_PCT = r10_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR10_EXCEEDING_DEPOSITORS() {
			return R10_EXCEEDING_DEPOSITORS;
		}

		public void setR10_EXCEEDING_DEPOSITORS(BigDecimal r10_EXCEEDING_DEPOSITORS) {
			R10_EXCEEDING_DEPOSITORS = r10_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR10_COVERED_AMOUNT_PCT() {
			return R10_COVERED_AMOUNT_PCT;
		}

		public void setR10_COVERED_AMOUNT_PCT(BigDecimal r10_COVERED_AMOUNT_PCT) {
			R10_COVERED_AMOUNT_PCT = r10_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR11_COVERAGE_LEVEL() {
			return R11_COVERAGE_LEVEL;
		}

		public void setR11_COVERAGE_LEVEL(BigDecimal r11_COVERAGE_LEVEL) {
			R11_COVERAGE_LEVEL = r11_COVERAGE_LEVEL;
		}

		public BigDecimal getR11_TOTAL_DEPOSIT_AMOUNT() {
			return R11_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR11_TOTAL_DEPOSIT_AMOUNT(BigDecimal r11_TOTAL_DEPOSIT_AMOUNT) {
			R11_TOTAL_DEPOSIT_AMOUNT = r11_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR11_COVERED_DEPOSITORS() {
			return R11_COVERED_DEPOSITORS;
		}

		public void setR11_COVERED_DEPOSITORS(BigDecimal r11_COVERED_DEPOSITORS) {
			R11_COVERED_DEPOSITORS = r11_COVERED_DEPOSITORS;
		}

		public BigDecimal getR11_COVERED_DEPOSITORS_PCT() {
			return R11_COVERED_DEPOSITORS_PCT;
		}

		public void setR11_COVERED_DEPOSITORS_PCT(BigDecimal r11_COVERED_DEPOSITORS_PCT) {
			R11_COVERED_DEPOSITORS_PCT = r11_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR11_EXCEEDING_DEPOSITORS() {
			return R11_EXCEEDING_DEPOSITORS;
		}

		public void setR11_EXCEEDING_DEPOSITORS(BigDecimal r11_EXCEEDING_DEPOSITORS) {
			R11_EXCEEDING_DEPOSITORS = r11_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR11_COVERED_AMOUNT_PCT() {
			return R11_COVERED_AMOUNT_PCT;
		}

		public void setR11_COVERED_AMOUNT_PCT(BigDecimal r11_COVERED_AMOUNT_PCT) {
			R11_COVERED_AMOUNT_PCT = r11_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR12_COVERAGE_LEVEL() {
			return R12_COVERAGE_LEVEL;
		}

		public void setR12_COVERAGE_LEVEL(BigDecimal r12_COVERAGE_LEVEL) {
			R12_COVERAGE_LEVEL = r12_COVERAGE_LEVEL;
		}

		public BigDecimal getR12_TOTAL_DEPOSIT_AMOUNT() {
			return R12_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR12_TOTAL_DEPOSIT_AMOUNT(BigDecimal r12_TOTAL_DEPOSIT_AMOUNT) {
			R12_TOTAL_DEPOSIT_AMOUNT = r12_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR12_COVERED_DEPOSITORS() {
			return R12_COVERED_DEPOSITORS;
		}

		public void setR12_COVERED_DEPOSITORS(BigDecimal r12_COVERED_DEPOSITORS) {
			R12_COVERED_DEPOSITORS = r12_COVERED_DEPOSITORS;
		}

		public BigDecimal getR12_COVERED_DEPOSITORS_PCT() {
			return R12_COVERED_DEPOSITORS_PCT;
		}

		public void setR12_COVERED_DEPOSITORS_PCT(BigDecimal r12_COVERED_DEPOSITORS_PCT) {
			R12_COVERED_DEPOSITORS_PCT = r12_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR12_EXCEEDING_DEPOSITORS() {
			return R12_EXCEEDING_DEPOSITORS;
		}

		public void setR12_EXCEEDING_DEPOSITORS(BigDecimal r12_EXCEEDING_DEPOSITORS) {
			R12_EXCEEDING_DEPOSITORS = r12_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR12_COVERED_AMOUNT_PCT() {
			return R12_COVERED_AMOUNT_PCT;
		}

		public void setR12_COVERED_AMOUNT_PCT(BigDecimal r12_COVERED_AMOUNT_PCT) {
			R12_COVERED_AMOUNT_PCT = r12_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR13_COVERAGE_LEVEL() {
			return R13_COVERAGE_LEVEL;
		}

		public void setR13_COVERAGE_LEVEL(BigDecimal r13_COVERAGE_LEVEL) {
			R13_COVERAGE_LEVEL = r13_COVERAGE_LEVEL;
		}

		public BigDecimal getR13_TOTAL_DEPOSIT_AMOUNT() {
			return R13_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR13_TOTAL_DEPOSIT_AMOUNT(BigDecimal r13_TOTAL_DEPOSIT_AMOUNT) {
			R13_TOTAL_DEPOSIT_AMOUNT = r13_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR13_COVERED_DEPOSITORS() {
			return R13_COVERED_DEPOSITORS;
		}

		public void setR13_COVERED_DEPOSITORS(BigDecimal r13_COVERED_DEPOSITORS) {
			R13_COVERED_DEPOSITORS = r13_COVERED_DEPOSITORS;
		}

		public BigDecimal getR13_COVERED_DEPOSITORS_PCT() {
			return R13_COVERED_DEPOSITORS_PCT;
		}

		public void setR13_COVERED_DEPOSITORS_PCT(BigDecimal r13_COVERED_DEPOSITORS_PCT) {
			R13_COVERED_DEPOSITORS_PCT = r13_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR13_EXCEEDING_DEPOSITORS() {
			return R13_EXCEEDING_DEPOSITORS;
		}

		public void setR13_EXCEEDING_DEPOSITORS(BigDecimal r13_EXCEEDING_DEPOSITORS) {
			R13_EXCEEDING_DEPOSITORS = r13_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR13_COVERED_AMOUNT_PCT() {
			return R13_COVERED_AMOUNT_PCT;
		}

		public void setR13_COVERED_AMOUNT_PCT(BigDecimal r13_COVERED_AMOUNT_PCT) {
			R13_COVERED_AMOUNT_PCT = r13_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR14_COVERAGE_LEVEL() {
			return R14_COVERAGE_LEVEL;
		}

		public void setR14_COVERAGE_LEVEL(BigDecimal r14_COVERAGE_LEVEL) {
			R14_COVERAGE_LEVEL = r14_COVERAGE_LEVEL;
		}

		public BigDecimal getR14_TOTAL_DEPOSIT_AMOUNT() {
			return R14_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR14_TOTAL_DEPOSIT_AMOUNT(BigDecimal r14_TOTAL_DEPOSIT_AMOUNT) {
			R14_TOTAL_DEPOSIT_AMOUNT = r14_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR14_COVERED_DEPOSITORS() {
			return R14_COVERED_DEPOSITORS;
		}

		public void setR14_COVERED_DEPOSITORS(BigDecimal r14_COVERED_DEPOSITORS) {
			R14_COVERED_DEPOSITORS = r14_COVERED_DEPOSITORS;
		}

		public BigDecimal getR14_COVERED_DEPOSITORS_PCT() {
			return R14_COVERED_DEPOSITORS_PCT;
		}

		public void setR14_COVERED_DEPOSITORS_PCT(BigDecimal r14_COVERED_DEPOSITORS_PCT) {
			R14_COVERED_DEPOSITORS_PCT = r14_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR14_EXCEEDING_DEPOSITORS() {
			return R14_EXCEEDING_DEPOSITORS;
		}

		public void setR14_EXCEEDING_DEPOSITORS(BigDecimal r14_EXCEEDING_DEPOSITORS) {
			R14_EXCEEDING_DEPOSITORS = r14_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR14_COVERED_AMOUNT_PCT() {
			return R14_COVERED_AMOUNT_PCT;
		}

		public void setR14_COVERED_AMOUNT_PCT(BigDecimal r14_COVERED_AMOUNT_PCT) {
			R14_COVERED_AMOUNT_PCT = r14_COVERED_AMOUNT_PCT;
		}

		public BigDecimal getR15_COVERAGE_LEVEL() {
			return R15_COVERAGE_LEVEL;
		}

		public void setR15_COVERAGE_LEVEL(BigDecimal r15_COVERAGE_LEVEL) {
			R15_COVERAGE_LEVEL = r15_COVERAGE_LEVEL;
		}

		public BigDecimal getR15_TOTAL_DEPOSIT_AMOUNT() {
			return R15_TOTAL_DEPOSIT_AMOUNT;
		}

		public void setR15_TOTAL_DEPOSIT_AMOUNT(BigDecimal r15_TOTAL_DEPOSIT_AMOUNT) {
			R15_TOTAL_DEPOSIT_AMOUNT = r15_TOTAL_DEPOSIT_AMOUNT;
		}

		public BigDecimal getR15_COVERED_DEPOSITORS() {
			return R15_COVERED_DEPOSITORS;
		}

		public void setR15_COVERED_DEPOSITORS(BigDecimal r15_COVERED_DEPOSITORS) {
			R15_COVERED_DEPOSITORS = r15_COVERED_DEPOSITORS;
		}

		public BigDecimal getR15_COVERED_DEPOSITORS_PCT() {
			return R15_COVERED_DEPOSITORS_PCT;
		}

		public void setR15_COVERED_DEPOSITORS_PCT(BigDecimal r15_COVERED_DEPOSITORS_PCT) {
			R15_COVERED_DEPOSITORS_PCT = r15_COVERED_DEPOSITORS_PCT;
		}

		public BigDecimal getR15_EXCEEDING_DEPOSITORS() {
			return R15_EXCEEDING_DEPOSITORS;
		}

		public void setR15_EXCEEDING_DEPOSITORS(BigDecimal r15_EXCEEDING_DEPOSITORS) {
			R15_EXCEEDING_DEPOSITORS = r15_EXCEEDING_DEPOSITORS;
		}

		public BigDecimal getR15_COVERED_AMOUNT_PCT() {
			return R15_COVERED_AMOUNT_PCT;
		}

		public void setR15_COVERED_AMOUNT_PCT(BigDecimal r15_COVERED_AMOUNT_PCT) {
			R15_COVERED_AMOUNT_PCT = r15_COVERED_AMOUNT_PCT;
		}

		public Date getREPORT_DATE() {
			return REPORT_DATE;
		}

		public void setREPORT_DATE(Date REPORT_DATE) {
			this.REPORT_DATE = REPORT_DATE;
		}

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
			REPORT_RESUBDATE = rEPORT_RESUBDATE;
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

	// COMPOSITE KEY CLASS INSIDE SERVICE

	public static class ADISB2_PK implements Serializable {

		private Date REPORT_DATE;
		private BigDecimal REPORT_VERSION;

		public ADISB2_PK() {
		}

		public ADISB2_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
			this.REPORT_DATE = REPORT_DATE;
			this.REPORT_VERSION = REPORT_VERSION;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof ADISB2_PK))
				return false;
			ADISB2_PK that = (ADISB2_PK) o;
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

	public class ADISB2_Detail_Entity {

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

		@Column(name = "REPORT_LABEL")
		private String reportLabel;
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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
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

	}

	class ADISB2DetailRowMapper implements RowMapper<ADISB2_Detail_Entity> {

		@Override
		public ADISB2_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB2_Detail_Entity obj = new ADISB2_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));

			obj.setReportLabel(rs.getString("REPORT_LABEL"));
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

	class ADISB2ArchivalDetailRowMapper implements RowMapper<ADISB2_Archival_Detail_Entity> {

		@Override
		public ADISB2_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			ADISB2_Archival_Detail_Entity obj = new ADISB2_Archival_Detail_Entity();

			obj.setCustId(rs.getString("CUST_ID"));
			obj.setAcctNumber(rs.getString("ACCT_NUMBER"));
			obj.setAcctName(rs.getString("ACCT_NAME"));
			obj.setDataType(rs.getString("DATA_TYPE"));
			obj.setReportAddlCriteria1(rs.getString("REPORT_ADDL_CRITERIA_1"));

			obj.setReportLabel(rs.getString("REPORT_LABEL"));
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

	public class ADISB2_Archival_Detail_Entity {

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

		@Column(name = "REPORT_LABEL")
		private String reportLabel;
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

		public String getReportLabel() {
			return reportLabel;
		}

		public void setReportLabel(String reportLabel) {
			this.reportLabel = reportLabel;
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

	}
	// MODEL AND VIEW METHOD summary

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getADISB2View(

			String reportId, String fromdate, String todate, String currency, String dtltype, Pageable pageable,
			String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("ADISB2 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL MODE

		if ("ARCHIVAL".equals(type) && version != null) {

			List<ADISB2_Archival_Summary_Entity> T1Master = new ArrayList<>();

			try {
				Date dt = dateformat.parse(todate);
				// SUMMARY ARCHIVAL
				T1Master = getdatabydateListarchival(dt, version);
				System.out.println("Archival Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
		}
		// NORMAL MODE

		else {
			List<ADISB2_Summary_Entity> T1Master = new ArrayList<>();
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

		mv.setViewName("BRRS/ADISB2");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
	// MODEL AND VIEW METHOD detail
	// =========================

	public ModelAndView getADISB2currentDtl(String reportId, String fromdate, String todate, String currency,
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

				List<ADISB2_Archival_Detail_Entity> archivalDetailList;

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

				List<ADISB2_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/ADISB2");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

	// Archival View
	public List<Object[]> getADISB2Archival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<ADISB2_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (ADISB2_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				ADISB2_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getREPORT_VERSION());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  ADISB2  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

	public ModelAndView getViewOrEditPage(String acct_number, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/ADISB2");

		if (acct_number != null) {
			ADISB2_Detail_Entity ADISB2Entity = findByAcctnumber(acct_number);
			if (ADISB2Entity != null && ADISB2Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(ADISB2Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", ADISB2Entity);
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
			ADISB2_Detail_Entity existing = findByAcctnumber(acctNo);

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

				String sql = "UPDATE BRRS_ADISB2_DETAILTABLE " + "SET ACCT_NAME = ?, " + "ACCT_BALANCE_IN_PULA = ? " + // ✅
																														// no
																														// comma
																														// here
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

							jdbcTemplate.update("BEGIN BRRS_ADISB2_SUMMARY_PROCEDURE(?); END;", formattedDate);

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

	public byte[] getADISB2DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for  ADISB2 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getADISB2DetailNewExcelARCHIVAL(filename, fromdate, todate, currency, dtltype,
						type, version);
				return ARCHIVALreport;
			}

			SXSSFWorkbook workbook = new SXSSFWorkbook(100);
			Sheet sheet = workbook.createSheet("ADISB2DetailsDetail");

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

			Row headerRow = sheet.createRow(0);
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
			List<ADISB2_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (ADISB2_Detail_Entity item : reportData) {
					Row row = sheet.createRow(rowIndex++);

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
				logger.info("No data found for ADISB2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating ADISB2 Excel", e);
			return new byte[0];
		}
	}

	public byte[] getADISB2DetailNewExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for ADISB2 ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);
			Sheet sheet = workbook.createSheet("ADISB2 Detail NEW");

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
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				if (i == 3) {
					cell.setCellStyle(rightAlignedHeaderStyle);
				} else {
					cell.setCellStyle(headerStyle);
				}

				sheet.setColumnWidth(i, 5000);
			}

			// Get data
			Date parsedToDate = new SimpleDateFormat("dd/MM/yyyy").parse(todate);
			List<ADISB2_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (ADISB2_Archival_Detail_Entity item : reportData) {
					Row row = sheet.createRow(rowIndex++);

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
				logger.info("No data found for ADISB2 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating ADISB2 NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] getM_ADISB2Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.ADISB2");

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelADISB2ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data

		List<ADISB2_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  ADISB2 report. Returning empty result.");
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

					ADISB2_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// Cell2 - R5_FIRST_NAME

					// Cell26 - R5_EXCHANGE_RATE
					Cell cell2 = row.createCell(2);
					if (record.getR6_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR6_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR6_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell cell4 = row.createCell(4);
					// if (record.getR6_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR6_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// Cell cell5 = row.createCell(5);
					// if (record.getR6_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR6_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// Cell cell6 = row.createCell(6);
					// if (record.getR6_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR6_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(6);
					cell2 = row.createCell(2);
					if (record.getR7_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR7_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR7_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR7_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR7_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR7_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR7_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR7_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR7_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(7);
					cell2 = row.createCell(2);
					if (record.getR8_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR8_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR8_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR8_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR8_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR8_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR8_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR8_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR8_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(8);
					cell2 = row.createCell(2);
					if (record.getR9_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR9_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR9_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR9_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR9_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR9_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR9_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR9_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR9_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(9);
					cell2 = row.createCell(2);
					if (record.getR10_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR10_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR10_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR10_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR10_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR10_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR10_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR10_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR10_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(10);
					cell2 = row.createCell(2);
					if (record.getR11_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR11_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR11_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR11_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR11_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR11_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR11_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR11_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(11);
					cell2 = row.createCell(2);
					if (record.getR12_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR12_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR12_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR12_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR12_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR12_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR12_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR12_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(12);
					cell2 = row.createCell(2);
					if (record.getR13_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR13_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR13_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR13_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR13_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR13_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR13_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR13_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(13);
					cell2 = row.createCell(2);
					if (record.getR14_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR14_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR14_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR14_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR14_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR14_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR14_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR14_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(14);
					cell2 = row.createCell(2);
					if (record.getR15_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR15_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR15_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR15_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR15_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR15_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR15_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR15_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

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

	public byte[] getExcelADISB2ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (type.equals("ARCHIVAL") & version != null) {

		}

		List<ADISB2_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for ADISB2 new report. Returning empty result.");
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

					ADISB2_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// Cell2 - R5_FIRST_NAME

					// Cell26 - R5_EXCHANGE_RATE
					Cell cell2 = row.createCell(2);
					if (record.getR6_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					Cell cell3 = row.createCell(3);
					if (record.getR6_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR6_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell cell4 = row.createCell(4);
					// if (record.getR6_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR6_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// Cell cell5 = row.createCell(5);
					// if (record.getR6_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR6_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// Cell cell6 = row.createCell(6);
					// if (record.getR6_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR6_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(6);
					cell2 = row.createCell(2);
					if (record.getR7_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR7_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR7_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR7_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR7_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR7_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR7_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR7_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR7_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(7);
					cell2 = row.createCell(2);
					if (record.getR8_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR8_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR8_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR8_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR8_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR8_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR8_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR8_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR8_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(8);
					cell2 = row.createCell(2);
					if (record.getR9_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR9_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR9_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR9_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR9_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR9_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR9_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR9_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR9_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(9);
					cell2 = row.createCell(2);
					if (record.getR10_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR10_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR10_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR10_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR10_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR10_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR10_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR10_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR10_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(10);
					cell2 = row.createCell(2);
					if (record.getR11_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR11_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR11_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR11_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR11_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR11_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR11_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR11_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR11_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(11);
					cell2 = row.createCell(2);
					if (record.getR12_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR12_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR12_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR12_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR12_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR12_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR12_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR12_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR12_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(12);
					cell2 = row.createCell(2);
					if (record.getR13_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR13_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR13_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR13_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR13_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR13_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR13_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR13_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR13_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR13_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(13);
					cell2 = row.createCell(2);
					if (record.getR14_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR14_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR14_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR14_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR14_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR14_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR14_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR14_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR14_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR14_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

					row = sheet.getRow(14);
					cell2 = row.createCell(2);
					if (record.getR15_TOTAL_DEPOSIT_AMOUNT() != null) {
						cell2.setCellValue(record.getR15_TOTAL_DEPOSIT_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					cell3 = row.createCell(3);
					if (record.getR15_COVERED_DEPOSITORS() != null) {
						cell3.setCellValue(record.getR15_COVERED_DEPOSITORS().doubleValue());
						cell3.setCellStyle(numberStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// cell4 = row.createCell(4);
					// if (record.getR15_COVERED_DEPOSITORS_PCT() != null) {
					// cell4.setCellValue(record.getR15_COVERED_DEPOSITORS_PCT().doubleValue());
					// cell4.setCellStyle(numberStyle);
					// } else {
					// cell4.setCellValue("");
					// cell4.setCellStyle(textStyle);
					// }
					//
					// cell5 = row.createCell(5);
					// if (record.getR15_EXCEEDING_DEPOSITORS() != null) {
					// cell5.setCellValue(record.getR15_EXCEEDING_DEPOSITORS().doubleValue());
					// cell5.setCellStyle(numberStyle);
					// } else {
					// cell5.setCellValue("");
					// cell5.setCellStyle(textStyle);
					// }
					//
					// cell6 = row.createCell(6);
					// if (record.getR15_COVERED_AMOUNT_PCT() != null) {
					// cell6.setCellValue(record.getR15_COVERED_AMOUNT_PCT().doubleValue());
					// cell6.setCellStyle(numberStyle);
					// } else {
					// cell6.setCellValue("");
					// cell6.setCellStyle(textStyle);
					// }

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