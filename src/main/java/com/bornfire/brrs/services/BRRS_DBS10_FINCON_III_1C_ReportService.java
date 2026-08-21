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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional
public class BRRS_DBS10_FINCON_III_1C_ReportService {

	private static final Logger logger = LoggerFactory.getLogger(BRRS_DBS10_FINCON_III_1C_ReportService.class);

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

	// Fetch data by report date
	public List<DBS10_FINCON_III_1C_Summary_Entity> getDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new DBS10_FINCON_III_1CRowMapper());
	}

	public List<DBS10_FINCON_III_1C_Manual_Summary_Entity> getManualDataByDate(Date reportDate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new DBS10_FINCON_III_1CManualRowMapper());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getDBS10_FINCON_III_1CArchival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<DBS10_FINCON_III_1C_Archival_Summary_Entity> getdatabydateListarchival(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new DBS10_FINCON_III_1CArchivalRowMapper());
	}

//GET ALL WITH VERSION

	public List<DBS10_FINCON_III_1C_Archival_Summary_Entity> getdatabydateListWithVersion() {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_VERSION IS NOT NULL " + "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new DBS10_FINCON_III_1CArchivalRowMapper());
	}

//GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion(Date reportDate) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportDate }, BigDecimal.class);
	}

	public List<Object[]> getDBS10_FINCON_III_1CManualArchivalList() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION "
				+ "FROM BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY " + "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity> getManualArchivalByDate(Date reportDate,
			BigDecimal reportVersion) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportVersion },
				new DBS10_FINCON_III_1CManualArchivalRowMapper());
	}

// 1. BY DATE + LABEL + CRITERIA

	public List<DBS10_FINCON_III_1C_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate,
			String reportLabel, String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new DBS10_FINCON_III_1CDetailRowMapper());
	}

// 2. GET ALL (BY DATE - simple)

	public List<DBS10_FINCON_III_1C_Detail_Entity> getDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new DBS10_FINCON_III_1CDetailRowMapper());
	}

// 3. PAGINATION

	public List<DBS10_FINCON_III_1C_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new DBS10_FINCON_III_1CDetailRowMapper());
	}

// 4. COUNT

	public int getDetaildatacount(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<DBS10_FINCON_III_1C_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE "
				+ "WHERE REPORT_LABLE = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new DBS10_FINCON_III_1CDetailRowMapper());
	}

// 6. BY ACCOUNT NUMBER

	public DBS10_FINCON_III_1C_Detail_Entity findByAcctnumber(String acct_number) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acct_number }, new DBS10_FINCON_III_1CDetailRowMapper());
	}

// 1. GET BY DATE + VERSION

//	public List<DBS10_FINCON_III_1C_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
//			String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL "
//				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
//				new DBS10_FINCON_III_1CArchivalDetailRowMapper());
//	}

// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

//	public List<DBS10_FINCON_III_1C_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
//			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
//
//		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABLE = ? "
//				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";
//
//		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
//				new DBS10_FINCON_III_1CArchivalDetailRowMapper());
//	}
	public List<DBS10_FINCON_III_1C_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND DATA_ENTRY_VERSION = ? ";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new DBS10_FINCON_III_1CArchivalDetailRowMapper());
	}

	public List<DBS10_FINCON_III_1C_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ?  ";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new DBS10_FINCON_III_1CArchivalDetailRowMapper());
	}

	public String getishighestversion(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
		String sql = "SELECT CASE WHEN ? = MAX(REPORT_VERSION) THEN 'YES' ELSE 'NO' END AS is_highest "
				+ "FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_VERSION, REPORT_DATE }, String.class);

	}

	public DBS10_FINCON_III_1C_Detail_Entity findBysnoArch(String sno) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new DBS10_FINCON_III_1CDetailRowMapper());
	}

	public DBS10_FINCON_III_1C_Detail_Entity findBysno(String sno) {

		String sql = "SELECT * FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE SNO = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { sno }, new DBS10_FINCON_III_1CDetailRowMapper());
	}
	// ROW MAPPER

	class DBS10_FINCON_III_1CRowMapper implements RowMapper<DBS10_FINCON_III_1C_Summary_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Summary_Entity obj = new DBS10_FINCON_III_1C_Summary_Entity();

			// R8 Field Mappings
			obj.setR8_name_of_sfi(rs.getString("R8_NAME_OF_SFI"));
			obj.setR8_name_of_cp(rs.getString("R8_NAME_OF_CP"));
			obj.setR8_loans_amt(rs.getBigDecimal("R8_LOANS_AMT"));
			obj.setR8_deposit_amt(rs.getBigDecimal("R8_DEPOSIT_AMT"));
			obj.setR8_total_amt(rs.getBigDecimal("R8_TOTAL_AMT"));

// R9 Field Mappings
			obj.setR9_name_of_sfi(rs.getString("R9_NAME_OF_SFI"));
			obj.setR9_name_of_cp(rs.getString("R9_NAME_OF_CP"));
			obj.setR9_loans_amt(rs.getBigDecimal("R9_LOANS_AMT"));
			obj.setR9_deposit_amt(rs.getBigDecimal("R9_DEPOSIT_AMT"));
			obj.setR9_total_amt(rs.getBigDecimal("R9_TOTAL_AMT"));

// R10 Field Mappings
			obj.setR10_name_of_sfi(rs.getString("R10_NAME_OF_SFI"));
			obj.setR10_name_of_cp(rs.getString("R10_NAME_OF_CP"));
			obj.setR10_loans_amt(rs.getBigDecimal("R10_LOANS_AMT"));
			obj.setR10_deposit_amt(rs.getBigDecimal("R10_DEPOSIT_AMT"));
			obj.setR10_total_amt(rs.getBigDecimal("R10_TOTAL_AMT"));

// R11 Field Mappings
			obj.setR11_name_of_sfi(rs.getString("R11_NAME_OF_SFI"));
			obj.setR11_name_of_cp(rs.getString("R11_NAME_OF_CP"));
			obj.setR11_loans_amt(rs.getBigDecimal("R11_LOANS_AMT"));
			obj.setR11_deposit_amt(rs.getBigDecimal("R11_DEPOSIT_AMT"));
			obj.setR11_total_amt(rs.getBigDecimal("R11_TOTAL_AMT"));

// R12 Field Mappings
			obj.setR12_name_of_sfi(rs.getString("R12_NAME_OF_SFI"));
			obj.setR12_name_of_cp(rs.getString("R12_NAME_OF_CP"));
			obj.setR12_loans_amt(rs.getBigDecimal("R12_LOANS_AMT"));
			obj.setR12_deposit_amt(rs.getBigDecimal("R12_DEPOSIT_AMT"));
			obj.setR12_total_amt(rs.getBigDecimal("R12_TOTAL_AMT"));

// R13 Field Mappings
			obj.setR13_name_of_sfi(rs.getString("R13_NAME_OF_SFI"));
			obj.setR13_name_of_cp(rs.getString("R13_NAME_OF_CP"));
			obj.setR13_loans_amt(rs.getBigDecimal("R13_LOANS_AMT"));
			obj.setR13_deposit_amt(rs.getBigDecimal("R13_DEPOSIT_AMT"));
			obj.setR13_total_amt(rs.getBigDecimal("R13_TOTAL_AMT"));

// R14 Field Mappings
			obj.setR14_name_of_sfi(rs.getString("R14_NAME_OF_SFI"));
			obj.setR14_name_of_cp(rs.getString("R14_NAME_OF_CP"));
			obj.setR14_loans_amt(rs.getBigDecimal("R14_LOANS_AMT"));
			obj.setR14_deposit_amt(rs.getBigDecimal("R14_DEPOSIT_AMT"));
			obj.setR14_total_amt(rs.getBigDecimal("R14_TOTAL_AMT"));
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

	public static class DBS10_FINCON_III_1C_Summary_Entity {

// r8 fields
		private String r8_name_of_sfi;
		private String r8_name_of_cp;
		private BigDecimal r8_loans_amt;
		private BigDecimal r8_deposit_amt;
		private BigDecimal r8_total_amt;

		// r9 fields
		private String r9_name_of_sfi;
		private String r9_name_of_cp;
		private BigDecimal r9_loans_amt;
		private BigDecimal r9_deposit_amt;
		private BigDecimal r9_total_amt;

		// r10 fields
		private String r10_name_of_sfi;
		private String r10_name_of_cp;
		private BigDecimal r10_loans_amt;
		private BigDecimal r10_deposit_amt;
		private BigDecimal r10_total_amt;

		// r11 fields
		private String r11_name_of_sfi;
		private String r11_name_of_cp;
		private BigDecimal r11_loans_amt;
		private BigDecimal r11_deposit_amt;
		private BigDecimal r11_total_amt;

		// r12 fields
		private String r12_name_of_sfi;
		private String r12_name_of_cp;
		private BigDecimal r12_loans_amt;
		private BigDecimal r12_deposit_amt;
		private BigDecimal r12_total_amt;

		// r13 fields
		private String r13_name_of_sfi;
		private String r13_name_of_cp;
		private BigDecimal r13_loans_amt;
		private BigDecimal r13_deposit_amt;
		private BigDecimal r13_total_amt;

		// r14 fields
		private String r14_name_of_sfi;
		private String r14_name_of_cp;
		private BigDecimal r14_loans_amt;
		private BigDecimal r14_deposit_amt;
		private BigDecimal r14_total_amt;

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

		// Getters and Setters
		public String getR8_name_of_sfi() {
			return r8_name_of_sfi;
		}

		public void setR8_name_of_sfi(String r8_name_of_sfi) {
			this.r8_name_of_sfi = r8_name_of_sfi;
		}

		public String getR8_name_of_cp() {
			return r8_name_of_cp;
		}

		public void setR8_name_of_cp(String r8_name_of_cp) {
			this.r8_name_of_cp = r8_name_of_cp;
		}

		public BigDecimal getR8_loans_amt() {
			return r8_loans_amt;
		}

		public void setR8_loans_amt(BigDecimal r8_loans_amt) {
			this.r8_loans_amt = r8_loans_amt;
		}

		public BigDecimal getR8_deposit_amt() {
			return r8_deposit_amt;
		}

		public void setR8_deposit_amt(BigDecimal r8_deposit_amt) {
			this.r8_deposit_amt = r8_deposit_amt;
		}

		public BigDecimal getR8_total_amt() {
			return r8_total_amt;
		}

		public void setR8_total_amt(BigDecimal r8_total_amt) {
			this.r8_total_amt = r8_total_amt;
		}

		public String getR9_name_of_sfi() {
			return r9_name_of_sfi;
		}

		public void setR9_name_of_sfi(String r9_name_of_sfi) {
			this.r9_name_of_sfi = r9_name_of_sfi;
		}

		public String getR9_name_of_cp() {
			return r9_name_of_cp;
		}

		public void setR9_name_of_cp(String r9_name_of_cp) {
			this.r9_name_of_cp = r9_name_of_cp;
		}

		public BigDecimal getR9_loans_amt() {
			return r9_loans_amt;
		}

		public void setR9_loans_amt(BigDecimal r9_loans_amt) {
			this.r9_loans_amt = r9_loans_amt;
		}

		public BigDecimal getR9_deposit_amt() {
			return r9_deposit_amt;
		}

		public void setR9_deposit_amt(BigDecimal r9_deposit_amt) {
			this.r9_deposit_amt = r9_deposit_amt;
		}

		public BigDecimal getR9_total_amt() {
			return r9_total_amt;
		}

		public void setR9_total_amt(BigDecimal r9_total_amt) {
			this.r9_total_amt = r9_total_amt;
		}

		public String getR10_name_of_sfi() {
			return r10_name_of_sfi;
		}

		public void setR10_name_of_sfi(String r10_name_of_sfi) {
			this.r10_name_of_sfi = r10_name_of_sfi;
		}

		public String getR10_name_of_cp() {
			return r10_name_of_cp;
		}

		public void setR10_name_of_cp(String r10_name_of_cp) {
			this.r10_name_of_cp = r10_name_of_cp;
		}

		public BigDecimal getR10_loans_amt() {
			return r10_loans_amt;
		}

		public void setR10_loans_amt(BigDecimal r10_loans_amt) {
			this.r10_loans_amt = r10_loans_amt;
		}

		public BigDecimal getR10_deposit_amt() {
			return r10_deposit_amt;
		}

		public void setR10_deposit_amt(BigDecimal r10_deposit_amt) {
			this.r10_deposit_amt = r10_deposit_amt;
		}

		public BigDecimal getR10_total_amt() {
			return r10_total_amt;
		}

		public void setR10_total_amt(BigDecimal r10_total_amt) {
			this.r10_total_amt = r10_total_amt;
		}

		public String getR11_name_of_sfi() {
			return r11_name_of_sfi;
		}

		public void setR11_name_of_sfi(String r11_name_of_sfi) {
			this.r11_name_of_sfi = r11_name_of_sfi;
		}

		public String getR11_name_of_cp() {
			return r11_name_of_cp;
		}

		public void setR11_name_of_cp(String r11_name_of_cp) {
			this.r11_name_of_cp = r11_name_of_cp;
		}

		public BigDecimal getR11_loans_amt() {
			return r11_loans_amt;
		}

		public void setR11_loans_amt(BigDecimal r11_loans_amt) {
			this.r11_loans_amt = r11_loans_amt;
		}

		public BigDecimal getR11_deposit_amt() {
			return r11_deposit_amt;
		}

		public void setR11_deposit_amt(BigDecimal r11_deposit_amt) {
			this.r11_deposit_amt = r11_deposit_amt;
		}

		public BigDecimal getR11_total_amt() {
			return r11_total_amt;
		}

		public void setR11_total_amt(BigDecimal r11_total_amt) {
			this.r11_total_amt = r11_total_amt;
		}

		public String getR12_name_of_sfi() {
			return r12_name_of_sfi;
		}

		public void setR12_name_of_sfi(String r12_name_of_sfi) {
			this.r12_name_of_sfi = r12_name_of_sfi;
		}

		public String getR12_name_of_cp() {
			return r12_name_of_cp;
		}

		public void setR12_name_of_cp(String r12_name_of_cp) {
			this.r12_name_of_cp = r12_name_of_cp;
		}

		public BigDecimal getR12_loans_amt() {
			return r12_loans_amt;
		}

		public void setR12_loans_amt(BigDecimal r12_loans_amt) {
			this.r12_loans_amt = r12_loans_amt;
		}

		public BigDecimal getR12_deposit_amt() {
			return r12_deposit_amt;
		}

		public void setR12_deposit_amt(BigDecimal r12_deposit_amt) {
			this.r12_deposit_amt = r12_deposit_amt;
		}

		public BigDecimal getR12_total_amt() {
			return r12_total_amt;
		}

		public void setR12_total_amt(BigDecimal r12_total_amt) {
			this.r12_total_amt = r12_total_amt;
		}

		public String getR13_name_of_sfi() {
			return r13_name_of_sfi;
		}

		public void setR13_name_of_sfi(String r13_name_of_sfi) {
			this.r13_name_of_sfi = r13_name_of_sfi;
		}

		public String getR13_name_of_cp() {
			return r13_name_of_cp;
		}

		public void setR13_name_of_cp(String r13_name_of_cp) {
			this.r13_name_of_cp = r13_name_of_cp;
		}

		public BigDecimal getR13_loans_amt() {
			return r13_loans_amt;
		}

		public void setR13_loans_amt(BigDecimal r13_loans_amt) {
			this.r13_loans_amt = r13_loans_amt;
		}

		public BigDecimal getR13_deposit_amt() {
			return r13_deposit_amt;
		}

		public void setR13_deposit_amt(BigDecimal r13_deposit_amt) {
			this.r13_deposit_amt = r13_deposit_amt;
		}

		public BigDecimal getR13_total_amt() {
			return r13_total_amt;
		}

		public void setR13_total_amt(BigDecimal r13_total_amt) {
			this.r13_total_amt = r13_total_amt;
		}

		public String getR14_name_of_sfi() {
			return r14_name_of_sfi;
		}

		public void setR14_name_of_sfi(String r14_name_of_sfi) {
			this.r14_name_of_sfi = r14_name_of_sfi;
		}

		public String getR14_name_of_cp() {
			return r14_name_of_cp;
		}

		public void setR14_name_of_cp(String r14_name_of_cp) {
			this.r14_name_of_cp = r14_name_of_cp;
		}

		public BigDecimal getR14_loans_amt() {
			return r14_loans_amt;
		}

		public void setR14_loans_amt(BigDecimal r14_loans_amt) {
			this.r14_loans_amt = r14_loans_amt;
		}

		public BigDecimal getR14_deposit_amt() {
			return r14_deposit_amt;
		}

		public void setR14_deposit_amt(BigDecimal r14_deposit_amt) {
			this.r14_deposit_amt = r14_deposit_amt;
		}

		public BigDecimal getR14_total_amt() {
			return r14_total_amt;
		}

		public void setR14_total_amt(BigDecimal r14_total_amt) {
			this.r14_total_amt = r14_total_amt;
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

	class DBS10_FINCON_III_1CManualRowMapper implements RowMapper<DBS10_FINCON_III_1C_Manual_Summary_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Manual_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Manual_Summary_Entity obj = new DBS10_FINCON_III_1C_Manual_Summary_Entity();

			obj.setR8_short_term_amt(rs.getBigDecimal("r8_short_term_amt"));
			obj.setR8_equity(rs.getBigDecimal("r8_equity"));
			obj.setR8_bonds_amt(rs.getBigDecimal("r8_bonds_amt"));
			obj.setR8_cp_amt(rs.getBigDecimal("r8_cp_amt"));
			obj.setR8_cd_amt(rs.getBigDecimal("r8_cd_amt"));
			obj.setR8_tier_amt(rs.getBigDecimal("r8_tier_amt"));
			obj.setR8_unit_amt(rs.getBigDecimal("r8_unit_amt"));
			obj.setR8_venture_amt(rs.getBigDecimal("r8_venture_amt"));
			obj.setR8_ptc_amt(rs.getBigDecimal("r8_ptc_amt"));
			obj.setR8_purchase_amt(rs.getBigDecimal("r8_purchase_amt"));
			obj.setR8_other_amt(rs.getBigDecimal("r8_other_amt"));
			obj.setR9_short_term_amt(rs.getBigDecimal("r9_short_term_amt"));
			obj.setR9_equity(rs.getBigDecimal("r9_equity"));
			obj.setR9_bonds_amt(rs.getBigDecimal("r9_bonds_amt"));
			obj.setR9_cp_amt(rs.getBigDecimal("r9_cp_amt"));
			obj.setR9_cd_amt(rs.getBigDecimal("r9_cd_amt"));
			obj.setR9_tier_amt(rs.getBigDecimal("r9_tier_amt"));
			obj.setR9_unit_amt(rs.getBigDecimal("r9_unit_amt"));
			obj.setR9_venture_amt(rs.getBigDecimal("r9_venture_amt"));
			obj.setR9_ptc_amt(rs.getBigDecimal("r9_ptc_amt"));
			obj.setR9_purchase_amt(rs.getBigDecimal("r9_purchase_amt"));
			obj.setR9_other_amt(rs.getBigDecimal("r9_other_amt"));
			obj.setR10_short_term_amt(rs.getBigDecimal("r10_short_term_amt"));
			obj.setR10_equity(rs.getBigDecimal("r10_equity"));
			obj.setR10_bonds_amt(rs.getBigDecimal("r10_bonds_amt"));
			obj.setR10_cp_amt(rs.getBigDecimal("r10_cp_amt"));
			obj.setR10_cd_amt(rs.getBigDecimal("r10_cd_amt"));
			obj.setR10_tier_amt(rs.getBigDecimal("r10_tier_amt"));
			obj.setR10_unit_amt(rs.getBigDecimal("r10_unit_amt"));
			obj.setR10_venture_amt(rs.getBigDecimal("r10_venture_amt"));
			obj.setR10_ptc_amt(rs.getBigDecimal("r10_ptc_amt"));
			obj.setR10_purchase_amt(rs.getBigDecimal("r10_purchase_amt"));
			obj.setR10_other_amt(rs.getBigDecimal("r10_other_amt"));
			obj.setR11_short_term_amt(rs.getBigDecimal("r11_short_term_amt"));
			obj.setR11_equity(rs.getBigDecimal("r11_equity"));
			obj.setR11_bonds_amt(rs.getBigDecimal("r11_bonds_amt"));
			obj.setR11_cp_amt(rs.getBigDecimal("r11_cp_amt"));
			obj.setR11_cd_amt(rs.getBigDecimal("r11_cd_amt"));
			obj.setR11_tier_amt(rs.getBigDecimal("r11_tier_amt"));
			obj.setR11_unit_amt(rs.getBigDecimal("r11_unit_amt"));
			obj.setR11_venture_amt(rs.getBigDecimal("r11_venture_amt"));
			obj.setR11_ptc_amt(rs.getBigDecimal("r11_ptc_amt"));
			obj.setR11_purchase_amt(rs.getBigDecimal("r11_purchase_amt"));
			obj.setR11_other_amt(rs.getBigDecimal("r11_other_amt"));
			obj.setR12_short_term_amt(rs.getBigDecimal("r12_short_term_amt"));
			obj.setR12_equity(rs.getBigDecimal("r12_equity"));
			obj.setR12_bonds_amt(rs.getBigDecimal("r12_bonds_amt"));
			obj.setR12_cp_amt(rs.getBigDecimal("r12_cp_amt"));
			obj.setR12_cd_amt(rs.getBigDecimal("r12_cd_amt"));
			obj.setR12_tier_amt(rs.getBigDecimal("r12_tier_amt"));
			obj.setR12_unit_amt(rs.getBigDecimal("r12_unit_amt"));
			obj.setR12_venture_amt(rs.getBigDecimal("r12_venture_amt"));
			obj.setR12_ptc_amt(rs.getBigDecimal("r12_ptc_amt"));
			obj.setR12_purchase_amt(rs.getBigDecimal("r12_purchase_amt"));
			obj.setR12_other_amt(rs.getBigDecimal("r12_other_amt"));
			obj.setR13_short_term_amt(rs.getBigDecimal("r13_short_term_amt"));
			obj.setR13_equity(rs.getBigDecimal("r13_equity"));
			obj.setR13_bonds_amt(rs.getBigDecimal("r13_bonds_amt"));
			obj.setR13_cp_amt(rs.getBigDecimal("r13_cp_amt"));
			obj.setR13_cd_amt(rs.getBigDecimal("r13_cd_amt"));
			obj.setR13_tier_amt(rs.getBigDecimal("r13_tier_amt"));
			obj.setR13_unit_amt(rs.getBigDecimal("r13_unit_amt"));
			obj.setR13_venture_amt(rs.getBigDecimal("r13_venture_amt"));
			obj.setR13_ptc_amt(rs.getBigDecimal("r13_ptc_amt"));
			obj.setR13_purchase_amt(rs.getBigDecimal("r13_purchase_amt"));
			obj.setR13_other_amt(rs.getBigDecimal("r13_other_amt"));
			obj.setR14_short_term_amt(rs.getBigDecimal("r14_short_term_amt"));
			obj.setR14_equity(rs.getBigDecimal("r14_equity"));
			obj.setR14_bonds_amt(rs.getBigDecimal("r14_bonds_amt"));
			obj.setR14_cp_amt(rs.getBigDecimal("r14_cp_amt"));
			obj.setR14_cd_amt(rs.getBigDecimal("r14_cd_amt"));
			obj.setR14_tier_amt(rs.getBigDecimal("r14_tier_amt"));
			obj.setR14_unit_amt(rs.getBigDecimal("r14_unit_amt"));
			obj.setR14_venture_amt(rs.getBigDecimal("r14_venture_amt"));
			obj.setR14_ptc_amt(rs.getBigDecimal("r14_ptc_amt"));
			obj.setR14_purchase_amt(rs.getBigDecimal("r14_purchase_amt"));
			obj.setR14_other_amt(rs.getBigDecimal("r14_other_amt"));

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

	public static class DBS10_FINCON_III_1C_Manual_Summary_Entity {

		// r8 fields
		private BigDecimal r8_short_term_amt;
		private BigDecimal r8_equity;
		private BigDecimal r8_bonds_amt;
		private BigDecimal r8_cp_amt;
		private BigDecimal r8_cd_amt;
		private BigDecimal r8_tier_amt;
		private BigDecimal r8_unit_amt;
		private BigDecimal r8_venture_amt;
		private BigDecimal r8_ptc_amt;
		private BigDecimal r8_purchase_amt;
		private BigDecimal r8_other_amt;

		// r9 fields
		private BigDecimal r9_short_term_amt;
		private BigDecimal r9_equity;
		private BigDecimal r9_bonds_amt;
		private BigDecimal r9_cp_amt;
		private BigDecimal r9_cd_amt;
		private BigDecimal r9_tier_amt;
		private BigDecimal r9_unit_amt;
		private BigDecimal r9_venture_amt;
		private BigDecimal r9_ptc_amt;
		private BigDecimal r9_purchase_amt;
		private BigDecimal r9_other_amt;

		// r10 fields
		private BigDecimal r10_short_term_amt;
		private BigDecimal r10_equity;
		private BigDecimal r10_bonds_amt;
		private BigDecimal r10_cp_amt;
		private BigDecimal r10_cd_amt;
		private BigDecimal r10_tier_amt;
		private BigDecimal r10_unit_amt;
		private BigDecimal r10_venture_amt;
		private BigDecimal r10_ptc_amt;
		private BigDecimal r10_purchase_amt;
		private BigDecimal r10_other_amt;

		// r11 fields
		private BigDecimal r11_short_term_amt;
		private BigDecimal r11_equity;
		private BigDecimal r11_bonds_amt;
		private BigDecimal r11_cp_amt;
		private BigDecimal r11_cd_amt;
		private BigDecimal r11_tier_amt;
		private BigDecimal r11_unit_amt;
		private BigDecimal r11_venture_amt;
		private BigDecimal r11_ptc_amt;
		private BigDecimal r11_purchase_amt;
		private BigDecimal r11_other_amt;

		// r12 fields
		private BigDecimal r12_short_term_amt;
		private BigDecimal r12_equity;
		private BigDecimal r12_bonds_amt;
		private BigDecimal r12_cp_amt;
		private BigDecimal r12_cd_amt;
		private BigDecimal r12_tier_amt;
		private BigDecimal r12_unit_amt;
		private BigDecimal r12_venture_amt;
		private BigDecimal r12_ptc_amt;
		private BigDecimal r12_purchase_amt;
		private BigDecimal r12_other_amt;

		// r13 fields
		private BigDecimal r13_short_term_amt;
		private BigDecimal r13_equity;
		private BigDecimal r13_bonds_amt;
		private BigDecimal r13_cp_amt;
		private BigDecimal r13_cd_amt;
		private BigDecimal r13_tier_amt;
		private BigDecimal r13_unit_amt;
		private BigDecimal r13_venture_amt;
		private BigDecimal r13_ptc_amt;
		private BigDecimal r13_purchase_amt;
		private BigDecimal r13_other_amt;

		// r14 fields
		private BigDecimal r14_short_term_amt;
		private BigDecimal r14_equity;
		private BigDecimal r14_bonds_amt;
		private BigDecimal r14_cp_amt;
		private BigDecimal r14_cd_amt;
		private BigDecimal r14_tier_amt;
		private BigDecimal r14_unit_amt;
		private BigDecimal r14_venture_amt;
		private BigDecimal r14_ptc_amt;
		private BigDecimal r14_purchase_amt;
		private BigDecimal r14_other_amt;

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

		// Getters and Setters - R8
		public BigDecimal getR8_short_term_amt() {
			return r8_short_term_amt;
		}

		public void setR8_short_term_amt(BigDecimal r8_short_term_amt) {
			this.r8_short_term_amt = r8_short_term_amt;
		}

		public BigDecimal getR8_equity() {
			return r8_equity;
		}

		public void setR8_equity(BigDecimal r8_equity) {
			this.r8_equity = r8_equity;
		}

		public BigDecimal getR8_bonds_amt() {
			return r8_bonds_amt;
		}

		public void setR8_bonds_amt(BigDecimal r8_bonds_amt) {
			this.r8_bonds_amt = r8_bonds_amt;
		}

		public BigDecimal getR8_cp_amt() {
			return r8_cp_amt;
		}

		public void setR8_cp_amt(BigDecimal r8_cp_amt) {
			this.r8_cp_amt = r8_cp_amt;
		}

		public BigDecimal getR8_cd_amt() {
			return r8_cd_amt;
		}

		public void setR8_cd_amt(BigDecimal r8_cd_amt) {
			this.r8_cd_amt = r8_cd_amt;
		}

		public BigDecimal getR8_tier_amt() {
			return r8_tier_amt;
		}

		public void setR8_tier_amt(BigDecimal r8_tier_amt) {
			this.r8_tier_amt = r8_tier_amt;
		}

		public BigDecimal getR8_unit_amt() {
			return r8_unit_amt;
		}

		public void setR8_unit_amt(BigDecimal r8_unit_amt) {
			this.r8_unit_amt = r8_unit_amt;
		}

		public BigDecimal getR8_venture_amt() {
			return r8_venture_amt;
		}

		public void setR8_venture_amt(BigDecimal r8_venture_amt) {
			this.r8_venture_amt = r8_venture_amt;
		}

		public BigDecimal getR8_ptc_amt() {
			return r8_ptc_amt;
		}

		public void setR8_ptc_amt(BigDecimal r8_ptc_amt) {
			this.r8_ptc_amt = r8_ptc_amt;
		}

		public BigDecimal getR8_purchase_amt() {
			return r8_purchase_amt;
		}

		public void setR8_purchase_amt(BigDecimal r8_purchase_amt) {
			this.r8_purchase_amt = r8_purchase_amt;
		}

		public BigDecimal getR8_other_amt() {
			return r8_other_amt;
		}

		public void setR8_other_amt(BigDecimal r8_other_amt) {
			this.r8_other_amt = r8_other_amt;
		}

		// Getters and Setters - R9
		public BigDecimal getR9_short_term_amt() {
			return r9_short_term_amt;
		}

		public void setR9_short_term_amt(BigDecimal r9_short_term_amt) {
			this.r9_short_term_amt = r9_short_term_amt;
		}

		public BigDecimal getR9_equity() {
			return r9_equity;
		}

		public void setR9_equity(BigDecimal r9_equity) {
			this.r9_equity = r9_equity;
		}

		public BigDecimal getR9_bonds_amt() {
			return r9_bonds_amt;
		}

		public void setR9_bonds_amt(BigDecimal r9_bonds_amt) {
			this.r9_bonds_amt = r9_bonds_amt;
		}

		public BigDecimal getR9_cp_amt() {
			return r9_cp_amt;
		}

		public void setR9_cp_amt(BigDecimal r9_cp_amt) {
			this.r9_cp_amt = r9_cp_amt;
		}

		public BigDecimal getR9_cd_amt() {
			return r9_cd_amt;
		}

		public void setR9_cd_amt(BigDecimal r9_cd_amt) {
			this.r9_cd_amt = r9_cd_amt;
		}

		public BigDecimal getR9_tier_amt() {
			return r9_tier_amt;
		}

		public void setR9_tier_amt(BigDecimal r9_tier_amt) {
			this.r9_tier_amt = r9_tier_amt;
		}

		public BigDecimal getR9_unit_amt() {
			return r9_unit_amt;
		}

		public void setR9_unit_amt(BigDecimal r9_unit_amt) {
			this.r9_unit_amt = r9_unit_amt;
		}

		public BigDecimal getR9_venture_amt() {
			return r9_venture_amt;
		}

		public void setR9_venture_amt(BigDecimal r9_venture_amt) {
			this.r9_venture_amt = r9_venture_amt;
		}

		public BigDecimal getR9_ptc_amt() {
			return r9_ptc_amt;
		}

		public void setR9_ptc_amt(BigDecimal r9_ptc_amt) {
			this.r9_ptc_amt = r9_ptc_amt;
		}

		public BigDecimal getR9_purchase_amt() {
			return r9_purchase_amt;
		}

		public void setR9_purchase_amt(BigDecimal r9_purchase_amt) {
			this.r9_purchase_amt = r9_purchase_amt;
		}

		public BigDecimal getR9_other_amt() {
			return r9_other_amt;
		}

		public void setR9_other_amt(BigDecimal r9_other_amt) {
			this.r9_other_amt = r9_other_amt;
		}

		// Getters and Setters - R10
		public BigDecimal getR10_short_term_amt() {
			return r10_short_term_amt;
		}

		public void setR10_short_term_amt(BigDecimal r10_short_term_amt) {
			this.r10_short_term_amt = r10_short_term_amt;
		}

		public BigDecimal getR10_equity() {
			return r10_equity;
		}

		public void setR10_equity(BigDecimal r10_equity) {
			this.r10_equity = r10_equity;
		}

		public BigDecimal getR10_bonds_amt() {
			return r10_bonds_amt;
		}

		public void setR10_bonds_amt(BigDecimal r10_bonds_amt) {
			this.r10_bonds_amt = r10_bonds_amt;
		}

		public BigDecimal getR10_cp_amt() {
			return r10_cp_amt;
		}

		public void setR10_cp_amt(BigDecimal r10_cp_amt) {
			this.r10_cp_amt = r10_cp_amt;
		}

		public BigDecimal getR10_cd_amt() {
			return r10_cd_amt;
		}

		public void setR10_cd_amt(BigDecimal r10_cd_amt) {
			this.r10_cd_amt = r10_cd_amt;
		}

		public BigDecimal getR10_tier_amt() {
			return r10_tier_amt;
		}

		public void setR10_tier_amt(BigDecimal r10_tier_amt) {
			this.r10_tier_amt = r10_tier_amt;
		}

		public BigDecimal getR10_unit_amt() {
			return r10_unit_amt;
		}

		public void setR10_unit_amt(BigDecimal r10_unit_amt) {
			this.r10_unit_amt = r10_unit_amt;
		}

		public BigDecimal getR10_venture_amt() {
			return r10_venture_amt;
		}

		public void setR10_venture_amt(BigDecimal r10_venture_amt) {
			this.r10_venture_amt = r10_venture_amt;
		}

		public BigDecimal getR10_ptc_amt() {
			return r10_ptc_amt;
		}

		public void setR10_ptc_amt(BigDecimal r10_ptc_amt) {
			this.r10_ptc_amt = r10_ptc_amt;
		}

		public BigDecimal getR10_purchase_amt() {
			return r10_purchase_amt;
		}

		public void setR10_purchase_amt(BigDecimal r10_purchase_amt) {
			this.r10_purchase_amt = r10_purchase_amt;
		}

		public BigDecimal getR10_other_amt() {
			return r10_other_amt;
		}

		public void setR10_other_amt(BigDecimal r10_other_amt) {
			this.r10_other_amt = r10_other_amt;
		}

		// Getters and Setters - R11
		public BigDecimal getR11_short_term_amt() {
			return r11_short_term_amt;
		}

		public void setR11_short_term_amt(BigDecimal r11_short_term_amt) {
			this.r11_short_term_amt = r11_short_term_amt;
		}

		public BigDecimal getR11_equity() {
			return r11_equity;
		}

		public void setR11_equity(BigDecimal r11_equity) {
			this.r11_equity = r11_equity;
		}

		public BigDecimal getR11_bonds_amt() {
			return r11_bonds_amt;
		}

		public void setR11_bonds_amt(BigDecimal r11_bonds_amt) {
			this.r11_bonds_amt = r11_bonds_amt;
		}

		public BigDecimal getR11_cp_amt() {
			return r11_cp_amt;
		}

		public void setR11_cp_amt(BigDecimal r11_cp_amt) {
			this.r11_cp_amt = r11_cp_amt;
		}

		public BigDecimal getR11_cd_amt() {
			return r11_cd_amt;
		}

		public void setR11_cd_amt(BigDecimal r11_cd_amt) {
			this.r11_cd_amt = r11_cd_amt;
		}

		public BigDecimal getR11_tier_amt() {
			return r11_tier_amt;
		}

		public void setR11_tier_amt(BigDecimal r11_tier_amt) {
			this.r11_tier_amt = r11_tier_amt;
		}

		public BigDecimal getR11_unit_amt() {
			return r11_unit_amt;
		}

		public void setR11_unit_amt(BigDecimal r11_unit_amt) {
			this.r11_unit_amt = r11_unit_amt;
		}

		public BigDecimal getR11_venture_amt() {
			return r11_venture_amt;
		}

		public void setR11_venture_amt(BigDecimal r11_venture_amt) {
			this.r11_venture_amt = r11_venture_amt;
		}

		public BigDecimal getR11_ptc_amt() {
			return r11_ptc_amt;
		}

		public void setR11_ptc_amt(BigDecimal r11_ptc_amt) {
			this.r11_ptc_amt = r11_ptc_amt;
		}

		public BigDecimal getR11_purchase_amt() {
			return r11_purchase_amt;
		}

		public void setR11_purchase_amt(BigDecimal r11_purchase_amt) {
			this.r11_purchase_amt = r11_purchase_amt;
		}

		public BigDecimal getR11_other_amt() {
			return r11_other_amt;
		}

		public void setR11_other_amt(BigDecimal r11_other_amt) {
			this.r11_other_amt = r11_other_amt;
		}

		// Getters and Setters - R12
		public BigDecimal getR12_short_term_amt() {
			return r12_short_term_amt;
		}

		public void setR12_short_term_amt(BigDecimal r12_short_term_amt) {
			this.r12_short_term_amt = r12_short_term_amt;
		}

		public BigDecimal getR12_equity() {
			return r12_equity;
		}

		public void setR12_equity(BigDecimal r12_equity) {
			this.r12_equity = r12_equity;
		}

		public BigDecimal getR12_bonds_amt() {
			return r12_bonds_amt;
		}

		public void setR12_bonds_amt(BigDecimal r12_bonds_amt) {
			this.r12_bonds_amt = r12_bonds_amt;
		}

		public BigDecimal getR12_cp_amt() {
			return r12_cp_amt;
		}

		public void setR12_cp_amt(BigDecimal r12_cp_amt) {
			this.r12_cp_amt = r12_cp_amt;
		}

		public BigDecimal getR12_cd_amt() {
			return r12_cd_amt;
		}

		public void setR12_cd_amt(BigDecimal r12_cd_amt) {
			this.r12_cd_amt = r12_cd_amt;
		}

		public BigDecimal getR12_tier_amt() {
			return r12_tier_amt;
		}

		public void setR12_tier_amt(BigDecimal r12_tier_amt) {
			this.r12_tier_amt = r12_tier_amt;
		}

		public BigDecimal getR12_unit_amt() {
			return r12_unit_amt;
		}

		public void setR12_unit_amt(BigDecimal r12_unit_amt) {
			this.r12_unit_amt = r12_unit_amt;
		}

		public BigDecimal getR12_venture_amt() {
			return r12_venture_amt;
		}

		public void setR12_venture_amt(BigDecimal r12_venture_amt) {
			this.r12_venture_amt = r12_venture_amt;
		}

		public BigDecimal getR12_ptc_amt() {
			return r12_ptc_amt;
		}

		public void setR12_ptc_amt(BigDecimal r12_ptc_amt) {
			this.r12_ptc_amt = r12_ptc_amt;
		}

		public BigDecimal getR12_purchase_amt() {
			return r12_purchase_amt;
		}

		public void setR12_purchase_amt(BigDecimal r12_purchase_amt) {
			this.r12_purchase_amt = r12_purchase_amt;
		}

		public BigDecimal getR12_other_amt() {
			return r12_other_amt;
		}

		public void setR12_other_amt(BigDecimal r12_other_amt) {
			this.r12_other_amt = r12_other_amt;
		}

		// Getters and Setters - R13
		public BigDecimal getR13_short_term_amt() {
			return r13_short_term_amt;
		}

		public void setR13_short_term_amt(BigDecimal r13_short_term_amt) {
			this.r13_short_term_amt = r13_short_term_amt;
		}

		public BigDecimal getR13_equity() {
			return r13_equity;
		}

		public void setR13_equity(BigDecimal r13_equity) {
			this.r13_equity = r13_equity;
		}

		public BigDecimal getR13_bonds_amt() {
			return r13_bonds_amt;
		}

		public void setR13_bonds_amt(BigDecimal r13_bonds_amt) {
			this.r13_bonds_amt = r13_bonds_amt;
		}

		public BigDecimal getR13_cp_amt() {
			return r13_cp_amt;
		}

		public void setR13_cp_amt(BigDecimal r13_cp_amt) {
			this.r13_cp_amt = r13_cp_amt;
		}

		public BigDecimal getR13_cd_amt() {
			return r13_cd_amt;
		}

		public void setR13_cd_amt(BigDecimal r13_cd_amt) {
			this.r13_cd_amt = r13_cd_amt;
		}

		public BigDecimal getR13_tier_amt() {
			return r13_tier_amt;
		}

		public void setR13_tier_amt(BigDecimal r13_tier_amt) {
			this.r13_tier_amt = r13_tier_amt;
		}

		public BigDecimal getR13_unit_amt() {
			return r13_unit_amt;
		}

		public void setR13_unit_amt(BigDecimal r13_unit_amt) {
			this.r13_unit_amt = r13_unit_amt;
		}

		public BigDecimal getR13_venture_amt() {
			return r13_venture_amt;
		}

		public void setR13_venture_amt(BigDecimal r13_venture_amt) {
			this.r13_venture_amt = r13_venture_amt;
		}

		public BigDecimal getR13_ptc_amt() {
			return r13_ptc_amt;
		}

		public void setR13_ptc_amt(BigDecimal r13_ptc_amt) {
			this.r13_ptc_amt = r13_ptc_amt;
		}

		public BigDecimal getR13_purchase_amt() {
			return r13_purchase_amt;
		}

		public void setR13_purchase_amt(BigDecimal r13_purchase_amt) {
			this.r13_purchase_amt = r13_purchase_amt;
		}

		public BigDecimal getR13_other_amt() {
			return r13_other_amt;
		}

		public void setR13_other_amt(BigDecimal r13_other_amt) {
			this.r13_other_amt = r13_other_amt;
		}

		// Getters and Setters - R14
		public BigDecimal getR14_short_term_amt() {
			return r14_short_term_amt;
		}

		public void setR14_short_term_amt(BigDecimal r14_short_term_amt) {
			this.r14_short_term_amt = r14_short_term_amt;
		}

		public BigDecimal getR14_equity() {
			return r14_equity;
		}

		public void setR14_equity(BigDecimal r14_equity) {
			this.r14_equity = r14_equity;
		}

		public BigDecimal getR14_bonds_amt() {
			return r14_bonds_amt;
		}

		public void setR14_bonds_amt(BigDecimal r14_bonds_amt) {
			this.r14_bonds_amt = r14_bonds_amt;
		}

		public BigDecimal getR14_cp_amt() {
			return r14_cp_amt;
		}

		public void setR14_cp_amt(BigDecimal r14_cp_amt) {
			this.r14_cp_amt = r14_cp_amt;
		}

		public BigDecimal getR14_cd_amt() {
			return r14_cd_amt;
		}

		public void setR14_cd_amt(BigDecimal r14_cd_amt) {
			this.r14_cd_amt = r14_cd_amt;
		}

		public BigDecimal getR14_tier_amt() {
			return r14_tier_amt;
		}

		public void setR14_tier_amt(BigDecimal r14_tier_amt) {
			this.r14_tier_amt = r14_tier_amt;
		}

		public BigDecimal getR14_unit_amt() {
			return r14_unit_amt;
		}

		public void setR14_unit_amt(BigDecimal r14_unit_amt) {
			this.r14_unit_amt = r14_unit_amt;
		}

		public BigDecimal getR14_venture_amt() {
			return r14_venture_amt;
		}

		public void setR14_venture_amt(BigDecimal r14_venture_amt) {
			this.r14_venture_amt = r14_venture_amt;
		}

		public BigDecimal getR14_ptc_amt() {
			return r14_ptc_amt;
		}

		public void setR14_ptc_amt(BigDecimal r14_ptc_amt) {
			this.r14_ptc_amt = r14_ptc_amt;
		}

		public BigDecimal getR14_purchase_amt() {
			return r14_purchase_amt;
		}

		public void setR14_purchase_amt(BigDecimal r14_purchase_amt) {
			this.r14_purchase_amt = r14_purchase_amt;
		}

		public BigDecimal getR14_other_amt() {
			return r14_other_amt;
		}

		public void setR14_other_amt(BigDecimal r14_other_amt) {
			this.r14_other_amt = r14_other_amt;
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

	class DBS10_FINCON_III_1CArchivalRowMapper implements RowMapper<DBS10_FINCON_III_1C_Archival_Summary_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Archival_Summary_Entity obj = new DBS10_FINCON_III_1C_Archival_Summary_Entity();

			// R8 Field Mappings
			obj.setR8_name_of_sfi(rs.getString("R8_NAME_OF_SFI"));
			obj.setR8_name_of_cp(rs.getString("R8_NAME_OF_CP"));
			obj.setR8_loans_amt(rs.getBigDecimal("R8_LOANS_AMT"));
			obj.setR8_deposit_amt(rs.getBigDecimal("R8_DEPOSIT_AMT"));
			obj.setR8_total_amt(rs.getBigDecimal("R8_TOTAL_AMT"));

// R9 Field Mappings
			obj.setR9_name_of_sfi(rs.getString("R9_NAME_OF_SFI"));
			obj.setR9_name_of_cp(rs.getString("R9_NAME_OF_CP"));
			obj.setR9_loans_amt(rs.getBigDecimal("R9_LOANS_AMT"));
			obj.setR9_deposit_amt(rs.getBigDecimal("R9_DEPOSIT_AMT"));
			obj.setR9_total_amt(rs.getBigDecimal("R9_TOTAL_AMT"));

// R10 Field Mappings
			obj.setR10_name_of_sfi(rs.getString("R10_NAME_OF_SFI"));
			obj.setR10_name_of_cp(rs.getString("R10_NAME_OF_CP"));
			obj.setR10_loans_amt(rs.getBigDecimal("R10_LOANS_AMT"));
			obj.setR10_deposit_amt(rs.getBigDecimal("R10_DEPOSIT_AMT"));
			obj.setR10_total_amt(rs.getBigDecimal("R10_TOTAL_AMT"));

// R11 Field Mappings
			obj.setR11_name_of_sfi(rs.getString("R11_NAME_OF_SFI"));
			obj.setR11_name_of_cp(rs.getString("R11_NAME_OF_CP"));
			obj.setR11_loans_amt(rs.getBigDecimal("R11_LOANS_AMT"));
			obj.setR11_deposit_amt(rs.getBigDecimal("R11_DEPOSIT_AMT"));
			obj.setR11_total_amt(rs.getBigDecimal("R11_TOTAL_AMT"));

// R12 Field Mappings
			obj.setR12_name_of_sfi(rs.getString("R12_NAME_OF_SFI"));
			obj.setR12_name_of_cp(rs.getString("R12_NAME_OF_CP"));
			obj.setR12_loans_amt(rs.getBigDecimal("R12_LOANS_AMT"));
			obj.setR12_deposit_amt(rs.getBigDecimal("R12_DEPOSIT_AMT"));
			obj.setR12_total_amt(rs.getBigDecimal("R12_TOTAL_AMT"));

// R13 Field Mappings
			obj.setR13_name_of_sfi(rs.getString("R13_NAME_OF_SFI"));
			obj.setR13_name_of_cp(rs.getString("R13_NAME_OF_CP"));
			obj.setR13_loans_amt(rs.getBigDecimal("R13_LOANS_AMT"));
			obj.setR13_deposit_amt(rs.getBigDecimal("R13_DEPOSIT_AMT"));
			obj.setR13_total_amt(rs.getBigDecimal("R13_TOTAL_AMT"));

// R14 Field Mappings
			obj.setR14_name_of_sfi(rs.getString("R14_NAME_OF_SFI"));
			obj.setR14_name_of_cp(rs.getString("R14_NAME_OF_CP"));
			obj.setR14_loans_amt(rs.getBigDecimal("R14_LOANS_AMT"));
			obj.setR14_deposit_amt(rs.getBigDecimal("R14_DEPOSIT_AMT"));
			obj.setR14_total_amt(rs.getBigDecimal("R14_TOTAL_AMT"));

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	@IdClass(DBS10_FINCON_III_1C_PK.class)
	public class DBS10_FINCON_III_1C_Archival_Summary_Entity {

		// r8 fields
		private String r8_name_of_sfi;
		private String r8_name_of_cp;
		private BigDecimal r8_loans_amt;
		private BigDecimal r8_deposit_amt;
		private BigDecimal r8_total_amt;

		// r9 fields
		private String r9_name_of_sfi;
		private String r9_name_of_cp;
		private BigDecimal r9_loans_amt;
		private BigDecimal r9_deposit_amt;
		private BigDecimal r9_total_amt;

		// r10 fields
		private String r10_name_of_sfi;
		private String r10_name_of_cp;
		private BigDecimal r10_loans_amt;
		private BigDecimal r10_deposit_amt;
		private BigDecimal r10_total_amt;

		// r11 fields
		private String r11_name_of_sfi;
		private String r11_name_of_cp;
		private BigDecimal r11_loans_amt;
		private BigDecimal r11_deposit_amt;
		private BigDecimal r11_total_amt;

		// r12 fields
		private String r12_name_of_sfi;
		private String r12_name_of_cp;
		private BigDecimal r12_loans_amt;
		private BigDecimal r12_deposit_amt;
		private BigDecimal r12_total_amt;

		// r13 fields
		private String r13_name_of_sfi;
		private String r13_name_of_cp;
		private BigDecimal r13_loans_amt;
		private BigDecimal r13_deposit_amt;
		private BigDecimal r13_total_amt;

		// r14 fields
		private String r14_name_of_sfi;
		private String r14_name_of_cp;
		private BigDecimal r14_loans_amt;
		private BigDecimal r14_deposit_amt;
		private BigDecimal r14_total_amt;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id
		private Date report_date;
		@Id
		private BigDecimal report_version;
		private Date REPORT_RESUBDATE;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Getters and Setters
		public String getR8_name_of_sfi() {
			return r8_name_of_sfi;
		}

		public void setR8_name_of_sfi(String r8_name_of_sfi) {
			this.r8_name_of_sfi = r8_name_of_sfi;
		}

		public String getR8_name_of_cp() {
			return r8_name_of_cp;
		}

		public void setR8_name_of_cp(String r8_name_of_cp) {
			this.r8_name_of_cp = r8_name_of_cp;
		}

		public BigDecimal getR8_loans_amt() {
			return r8_loans_amt;
		}

		public void setR8_loans_amt(BigDecimal r8_loans_amt) {
			this.r8_loans_amt = r8_loans_amt;
		}

		public BigDecimal getR8_deposit_amt() {
			return r8_deposit_amt;
		}

		public void setR8_deposit_amt(BigDecimal r8_deposit_amt) {
			this.r8_deposit_amt = r8_deposit_amt;
		}

		public BigDecimal getR8_total_amt() {
			return r8_total_amt;
		}

		public void setR8_total_amt(BigDecimal r8_total_amt) {
			this.r8_total_amt = r8_total_amt;
		}

		public String getR9_name_of_sfi() {
			return r9_name_of_sfi;
		}

		public void setR9_name_of_sfi(String r9_name_of_sfi) {
			this.r9_name_of_sfi = r9_name_of_sfi;
		}

		public String getR9_name_of_cp() {
			return r9_name_of_cp;
		}

		public void setR9_name_of_cp(String r9_name_of_cp) {
			this.r9_name_of_cp = r9_name_of_cp;
		}

		public BigDecimal getR9_loans_amt() {
			return r9_loans_amt;
		}

		public void setR9_loans_amt(BigDecimal r9_loans_amt) {
			this.r9_loans_amt = r9_loans_amt;
		}

		public BigDecimal getR9_deposit_amt() {
			return r9_deposit_amt;
		}

		public void setR9_deposit_amt(BigDecimal r9_deposit_amt) {
			this.r9_deposit_amt = r9_deposit_amt;
		}

		public BigDecimal getR9_total_amt() {
			return r9_total_amt;
		}

		public void setR9_total_amt(BigDecimal r9_total_amt) {
			this.r9_total_amt = r9_total_amt;
		}

		public String getR10_name_of_sfi() {
			return r10_name_of_sfi;
		}

		public void setR10_name_of_sfi(String r10_name_of_sfi) {
			this.r10_name_of_sfi = r10_name_of_sfi;
		}

		public String getR10_name_of_cp() {
			return r10_name_of_cp;
		}

		public void setR10_name_of_cp(String r10_name_of_cp) {
			this.r10_name_of_cp = r10_name_of_cp;
		}

		public BigDecimal getR10_loans_amt() {
			return r10_loans_amt;
		}

		public void setR10_loans_amt(BigDecimal r10_loans_amt) {
			this.r10_loans_amt = r10_loans_amt;
		}

		public BigDecimal getR10_deposit_amt() {
			return r10_deposit_amt;
		}

		public void setR10_deposit_amt(BigDecimal r10_deposit_amt) {
			this.r10_deposit_amt = r10_deposit_amt;
		}

		public BigDecimal getR10_total_amt() {
			return r10_total_amt;
		}

		public void setR10_total_amt(BigDecimal r10_total_amt) {
			this.r10_total_amt = r10_total_amt;
		}

		public String getR11_name_of_sfi() {
			return r11_name_of_sfi;
		}

		public void setR11_name_of_sfi(String r11_name_of_sfi) {
			this.r11_name_of_sfi = r11_name_of_sfi;
		}

		public String getR11_name_of_cp() {
			return r11_name_of_cp;
		}

		public void setR11_name_of_cp(String r11_name_of_cp) {
			this.r11_name_of_cp = r11_name_of_cp;
		}

		public BigDecimal getR11_loans_amt() {
			return r11_loans_amt;
		}

		public void setR11_loans_amt(BigDecimal r11_loans_amt) {
			this.r11_loans_amt = r11_loans_amt;
		}

		public BigDecimal getR11_deposit_amt() {
			return r11_deposit_amt;
		}

		public void setR11_deposit_amt(BigDecimal r11_deposit_amt) {
			this.r11_deposit_amt = r11_deposit_amt;
		}

		public BigDecimal getR11_total_amt() {
			return r11_total_amt;
		}

		public void setR11_total_amt(BigDecimal r11_total_amt) {
			this.r11_total_amt = r11_total_amt;
		}

		public String getR12_name_of_sfi() {
			return r12_name_of_sfi;
		}

		public void setR12_name_of_sfi(String r12_name_of_sfi) {
			this.r12_name_of_sfi = r12_name_of_sfi;
		}

		public String getR12_name_of_cp() {
			return r12_name_of_cp;
		}

		public void setR12_name_of_cp(String r12_name_of_cp) {
			this.r12_name_of_cp = r12_name_of_cp;
		}

		public BigDecimal getR12_loans_amt() {
			return r12_loans_amt;
		}

		public void setR12_loans_amt(BigDecimal r12_loans_amt) {
			this.r12_loans_amt = r12_loans_amt;
		}

		public BigDecimal getR12_deposit_amt() {
			return r12_deposit_amt;
		}

		public void setR12_deposit_amt(BigDecimal r12_deposit_amt) {
			this.r12_deposit_amt = r12_deposit_amt;
		}

		public BigDecimal getR12_total_amt() {
			return r12_total_amt;
		}

		public void setR12_total_amt(BigDecimal r12_total_amt) {
			this.r12_total_amt = r12_total_amt;
		}

		public String getR13_name_of_sfi() {
			return r13_name_of_sfi;
		}

		public void setR13_name_of_sfi(String r13_name_of_sfi) {
			this.r13_name_of_sfi = r13_name_of_sfi;
		}

		public String getR13_name_of_cp() {
			return r13_name_of_cp;
		}

		public void setR13_name_of_cp(String r13_name_of_cp) {
			this.r13_name_of_cp = r13_name_of_cp;
		}

		public BigDecimal getR13_loans_amt() {
			return r13_loans_amt;
		}

		public void setR13_loans_amt(BigDecimal r13_loans_amt) {
			this.r13_loans_amt = r13_loans_amt;
		}

		public BigDecimal getR13_deposit_amt() {
			return r13_deposit_amt;
		}

		public void setR13_deposit_amt(BigDecimal r13_deposit_amt) {
			this.r13_deposit_amt = r13_deposit_amt;
		}

		public BigDecimal getR13_total_amt() {
			return r13_total_amt;
		}

		public void setR13_total_amt(BigDecimal r13_total_amt) {
			this.r13_total_amt = r13_total_amt;
		}

		public String getR14_name_of_sfi() {
			return r14_name_of_sfi;
		}

		public void setR14_name_of_sfi(String r14_name_of_sfi) {
			this.r14_name_of_sfi = r14_name_of_sfi;
		}

		public String getR14_name_of_cp() {
			return r14_name_of_cp;
		}

		public void setR14_name_of_cp(String r14_name_of_cp) {
			this.r14_name_of_cp = r14_name_of_cp;
		}

		public BigDecimal getR14_loans_amt() {
			return r14_loans_amt;
		}

		public void setR14_loans_amt(BigDecimal r14_loans_amt) {
			this.r14_loans_amt = r14_loans_amt;
		}

		public BigDecimal getR14_deposit_amt() {
			return r14_deposit_amt;
		}

		public void setR14_deposit_amt(BigDecimal r14_deposit_amt) {
			this.r14_deposit_amt = r14_deposit_amt;
		}

		public BigDecimal getR14_total_amt() {
			return r14_total_amt;
		}

		public void setR14_total_amt(BigDecimal r14_total_amt) {
			this.r14_total_amt = r14_total_amt;
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
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

	class DBS10_FINCON_III_1CManualArchivalRowMapper
			implements RowMapper<DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity obj = new DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity();

			obj.setR8_short_term_amt(rs.getBigDecimal("r8_short_term_amt"));
			obj.setR8_equity(rs.getBigDecimal("r8_equity"));
			obj.setR8_bonds_amt(rs.getBigDecimal("r8_bonds_amt"));
			obj.setR8_cp_amt(rs.getBigDecimal("r8_cp_amt"));
			obj.setR8_cd_amt(rs.getBigDecimal("r8_cd_amt"));
			obj.setR8_tier_amt(rs.getBigDecimal("r8_tier_amt"));
			obj.setR8_unit_amt(rs.getBigDecimal("r8_unit_amt"));
			obj.setR8_venture_amt(rs.getBigDecimal("r8_venture_amt"));
			obj.setR8_ptc_amt(rs.getBigDecimal("r8_ptc_amt"));
			obj.setR8_purchase_amt(rs.getBigDecimal("r8_purchase_amt"));
			obj.setR8_other_amt(rs.getBigDecimal("r8_other_amt"));
			obj.setR9_short_term_amt(rs.getBigDecimal("r9_short_term_amt"));
			obj.setR9_equity(rs.getBigDecimal("r9_equity"));
			obj.setR9_bonds_amt(rs.getBigDecimal("r9_bonds_amt"));
			obj.setR9_cp_amt(rs.getBigDecimal("r9_cp_amt"));
			obj.setR9_cd_amt(rs.getBigDecimal("r9_cd_amt"));
			obj.setR9_tier_amt(rs.getBigDecimal("r9_tier_amt"));
			obj.setR9_unit_amt(rs.getBigDecimal("r9_unit_amt"));
			obj.setR9_venture_amt(rs.getBigDecimal("r9_venture_amt"));
			obj.setR9_ptc_amt(rs.getBigDecimal("r9_ptc_amt"));
			obj.setR9_purchase_amt(rs.getBigDecimal("r9_purchase_amt"));
			obj.setR9_other_amt(rs.getBigDecimal("r9_other_amt"));
			obj.setR10_short_term_amt(rs.getBigDecimal("r10_short_term_amt"));
			obj.setR10_equity(rs.getBigDecimal("r10_equity"));
			obj.setR10_bonds_amt(rs.getBigDecimal("r10_bonds_amt"));
			obj.setR10_cp_amt(rs.getBigDecimal("r10_cp_amt"));
			obj.setR10_cd_amt(rs.getBigDecimal("r10_cd_amt"));
			obj.setR10_tier_amt(rs.getBigDecimal("r10_tier_amt"));
			obj.setR10_unit_amt(rs.getBigDecimal("r10_unit_amt"));
			obj.setR10_venture_amt(rs.getBigDecimal("r10_venture_amt"));
			obj.setR10_ptc_amt(rs.getBigDecimal("r10_ptc_amt"));
			obj.setR10_purchase_amt(rs.getBigDecimal("r10_purchase_amt"));
			obj.setR10_other_amt(rs.getBigDecimal("r10_other_amt"));
			obj.setR11_short_term_amt(rs.getBigDecimal("r11_short_term_amt"));
			obj.setR11_equity(rs.getBigDecimal("r11_equity"));
			obj.setR11_bonds_amt(rs.getBigDecimal("r11_bonds_amt"));
			obj.setR11_cp_amt(rs.getBigDecimal("r11_cp_amt"));
			obj.setR11_cd_amt(rs.getBigDecimal("r11_cd_amt"));
			obj.setR11_tier_amt(rs.getBigDecimal("r11_tier_amt"));
			obj.setR11_unit_amt(rs.getBigDecimal("r11_unit_amt"));
			obj.setR11_venture_amt(rs.getBigDecimal("r11_venture_amt"));
			obj.setR11_ptc_amt(rs.getBigDecimal("r11_ptc_amt"));
			obj.setR11_purchase_amt(rs.getBigDecimal("r11_purchase_amt"));
			obj.setR11_other_amt(rs.getBigDecimal("r11_other_amt"));
			obj.setR12_short_term_amt(rs.getBigDecimal("r12_short_term_amt"));
			obj.setR12_equity(rs.getBigDecimal("r12_equity"));
			obj.setR12_bonds_amt(rs.getBigDecimal("r12_bonds_amt"));
			obj.setR12_cp_amt(rs.getBigDecimal("r12_cp_amt"));
			obj.setR12_cd_amt(rs.getBigDecimal("r12_cd_amt"));
			obj.setR12_tier_amt(rs.getBigDecimal("r12_tier_amt"));
			obj.setR12_unit_amt(rs.getBigDecimal("r12_unit_amt"));
			obj.setR12_venture_amt(rs.getBigDecimal("r12_venture_amt"));
			obj.setR12_ptc_amt(rs.getBigDecimal("r12_ptc_amt"));
			obj.setR12_purchase_amt(rs.getBigDecimal("r12_purchase_amt"));
			obj.setR12_other_amt(rs.getBigDecimal("r12_other_amt"));
			obj.setR13_short_term_amt(rs.getBigDecimal("r13_short_term_amt"));
			obj.setR13_equity(rs.getBigDecimal("r13_equity"));
			obj.setR13_bonds_amt(rs.getBigDecimal("r13_bonds_amt"));
			obj.setR13_cp_amt(rs.getBigDecimal("r13_cp_amt"));
			obj.setR13_cd_amt(rs.getBigDecimal("r13_cd_amt"));
			obj.setR13_tier_amt(rs.getBigDecimal("r13_tier_amt"));
			obj.setR13_unit_amt(rs.getBigDecimal("r13_unit_amt"));
			obj.setR13_venture_amt(rs.getBigDecimal("r13_venture_amt"));
			obj.setR13_ptc_amt(rs.getBigDecimal("r13_ptc_amt"));
			obj.setR13_purchase_amt(rs.getBigDecimal("r13_purchase_amt"));
			obj.setR13_other_amt(rs.getBigDecimal("r13_other_amt"));
			obj.setR14_short_term_amt(rs.getBigDecimal("r14_short_term_amt"));
			obj.setR14_equity(rs.getBigDecimal("r14_equity"));
			obj.setR14_bonds_amt(rs.getBigDecimal("r14_bonds_amt"));
			obj.setR14_cp_amt(rs.getBigDecimal("r14_cp_amt"));
			obj.setR14_cd_amt(rs.getBigDecimal("r14_cd_amt"));
			obj.setR14_tier_amt(rs.getBigDecimal("r14_tier_amt"));
			obj.setR14_unit_amt(rs.getBigDecimal("r14_unit_amt"));
			obj.setR14_venture_amt(rs.getBigDecimal("r14_venture_amt"));
			obj.setR14_ptc_amt(rs.getBigDecimal("r14_ptc_amt"));
			obj.setR14_purchase_amt(rs.getBigDecimal("r14_purchase_amt"));
			obj.setR14_other_amt(rs.getBigDecimal("r14_other_amt"));

			obj.setReport_date(rs.getDate("report_date"));
			obj.setReport_version(rs.getBigDecimal("report_version"));
			obj.setREPORT_RESUBDATE(rs.getDate("REPORT_RESUBDATE"));
			obj.setReport_frequency(rs.getString("report_frequency"));
			obj.setReport_code(rs.getString("report_code"));
			obj.setReport_desc(rs.getString("report_desc"));
			obj.setEntity_flg(rs.getString("entity_flg"));
			obj.setModify_flg(rs.getString("modify_flg"));
			obj.setDel_flg(rs.getString("del_flg"));

			return obj;
		}
	}

	public static class DBS10_FINCON_III_1C_PK implements Serializable {

		private Date report_date;
		private BigDecimal report_version;

		public DBS10_FINCON_III_1C_PK() {
		}

		public DBS10_FINCON_III_1C_PK(Date report_date, BigDecimal report_version) {
			this.report_date = report_date;
			this.report_version = report_version;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof DBS10_FINCON_III_1C_PK))
				return false;
			DBS10_FINCON_III_1C_PK that = (DBS10_FINCON_III_1C_PK) o;
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

	@IdClass(DBS10_FINCON_III_1C_PK.class)
	public static class DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity {

		// r8 fields
		private BigDecimal r8_short_term_amt;
		private BigDecimal r8_equity;
		private BigDecimal r8_bonds_amt;
		private BigDecimal r8_cp_amt;
		private BigDecimal r8_cd_amt;
		private BigDecimal r8_tier_amt;
		private BigDecimal r8_unit_amt;
		private BigDecimal r8_venture_amt;
		private BigDecimal r8_ptc_amt;
		private BigDecimal r8_purchase_amt;
		private BigDecimal r8_other_amt;

		// r9 fields
		private BigDecimal r9_short_term_amt;
		private BigDecimal r9_equity;
		private BigDecimal r9_bonds_amt;
		private BigDecimal r9_cp_amt;
		private BigDecimal r9_cd_amt;
		private BigDecimal r9_tier_amt;
		private BigDecimal r9_unit_amt;
		private BigDecimal r9_venture_amt;
		private BigDecimal r9_ptc_amt;
		private BigDecimal r9_purchase_amt;
		private BigDecimal r9_other_amt;

		// r10 fields
		private BigDecimal r10_short_term_amt;
		private BigDecimal r10_equity;
		private BigDecimal r10_bonds_amt;
		private BigDecimal r10_cp_amt;
		private BigDecimal r10_cd_amt;
		private BigDecimal r10_tier_amt;
		private BigDecimal r10_unit_amt;
		private BigDecimal r10_venture_amt;
		private BigDecimal r10_ptc_amt;
		private BigDecimal r10_purchase_amt;
		private BigDecimal r10_other_amt;

		// r11 fields
		private BigDecimal r11_short_term_amt;
		private BigDecimal r11_equity;
		private BigDecimal r11_bonds_amt;
		private BigDecimal r11_cp_amt;
		private BigDecimal r11_cd_amt;
		private BigDecimal r11_tier_amt;
		private BigDecimal r11_unit_amt;
		private BigDecimal r11_venture_amt;
		private BigDecimal r11_ptc_amt;
		private BigDecimal r11_purchase_amt;
		private BigDecimal r11_other_amt;

		// r12 fields
		private BigDecimal r12_short_term_amt;
		private BigDecimal r12_equity;
		private BigDecimal r12_bonds_amt;
		private BigDecimal r12_cp_amt;
		private BigDecimal r12_cd_amt;
		private BigDecimal r12_tier_amt;
		private BigDecimal r12_unit_amt;
		private BigDecimal r12_venture_amt;
		private BigDecimal r12_ptc_amt;
		private BigDecimal r12_purchase_amt;
		private BigDecimal r12_other_amt;

		// r13 fields
		private BigDecimal r13_short_term_amt;
		private BigDecimal r13_equity;
		private BigDecimal r13_bonds_amt;
		private BigDecimal r13_cp_amt;
		private BigDecimal r13_cd_amt;
		private BigDecimal r13_tier_amt;
		private BigDecimal r13_unit_amt;
		private BigDecimal r13_venture_amt;
		private BigDecimal r13_ptc_amt;
		private BigDecimal r13_purchase_amt;
		private BigDecimal r13_other_amt;

		// r14 fields
		private BigDecimal r14_short_term_amt;
		private BigDecimal r14_equity;
		private BigDecimal r14_bonds_amt;
		private BigDecimal r14_cp_amt;
		private BigDecimal r14_cd_amt;
		private BigDecimal r14_tier_amt;
		private BigDecimal r14_unit_amt;
		private BigDecimal r14_venture_amt;
		private BigDecimal r14_ptc_amt;
		private BigDecimal r14_purchase_amt;
		private BigDecimal r14_other_amt;

		@Temporal(TemporalType.DATE)
		@DateTimeFormat(pattern = "dd/MM/yyyy")
		@Id

		private Date report_date;
		private BigDecimal report_version;

		private Date REPORT_RESUBDATE;
		private String report_frequency;
		private String report_code;
		private String report_desc;
		private String entity_flg;
		private String modify_flg;
		private String del_flg;

		// Getters and Setters - R8
		public BigDecimal getR8_short_term_amt() {
			return r8_short_term_amt;
		}

		public void setR8_short_term_amt(BigDecimal r8_short_term_amt) {
			this.r8_short_term_amt = r8_short_term_amt;
		}

		public BigDecimal getR8_equity() {
			return r8_equity;
		}

		public void setR8_equity(BigDecimal r8_equity) {
			this.r8_equity = r8_equity;
		}

		public BigDecimal getR8_bonds_amt() {
			return r8_bonds_amt;
		}

		public void setR8_bonds_amt(BigDecimal r8_bonds_amt) {
			this.r8_bonds_amt = r8_bonds_amt;
		}

		public BigDecimal getR8_cp_amt() {
			return r8_cp_amt;
		}

		public void setR8_cp_amt(BigDecimal r8_cp_amt) {
			this.r8_cp_amt = r8_cp_amt;
		}

		public BigDecimal getR8_cd_amt() {
			return r8_cd_amt;
		}

		public void setR8_cd_amt(BigDecimal r8_cd_amt) {
			this.r8_cd_amt = r8_cd_amt;
		}

		public BigDecimal getR8_tier_amt() {
			return r8_tier_amt;
		}

		public void setR8_tier_amt(BigDecimal r8_tier_amt) {
			this.r8_tier_amt = r8_tier_amt;
		}

		public BigDecimal getR8_unit_amt() {
			return r8_unit_amt;
		}

		public void setR8_unit_amt(BigDecimal r8_unit_amt) {
			this.r8_unit_amt = r8_unit_amt;
		}

		public BigDecimal getR8_venture_amt() {
			return r8_venture_amt;
		}

		public void setR8_venture_amt(BigDecimal r8_venture_amt) {
			this.r8_venture_amt = r8_venture_amt;
		}

		public BigDecimal getR8_ptc_amt() {
			return r8_ptc_amt;
		}

		public void setR8_ptc_amt(BigDecimal r8_ptc_amt) {
			this.r8_ptc_amt = r8_ptc_amt;
		}

		public BigDecimal getR8_purchase_amt() {
			return r8_purchase_amt;
		}

		public void setR8_purchase_amt(BigDecimal r8_purchase_amt) {
			this.r8_purchase_amt = r8_purchase_amt;
		}

		public BigDecimal getR8_other_amt() {
			return r8_other_amt;
		}

		public void setR8_other_amt(BigDecimal r8_other_amt) {
			this.r8_other_amt = r8_other_amt;
		}

		// Getters and Setters - R9
		public BigDecimal getR9_short_term_amt() {
			return r9_short_term_amt;
		}

		public void setR9_short_term_amt(BigDecimal r9_short_term_amt) {
			this.r9_short_term_amt = r9_short_term_amt;
		}

		public BigDecimal getR9_equity() {
			return r9_equity;
		}

		public void setR9_equity(BigDecimal r9_equity) {
			this.r9_equity = r9_equity;
		}

		public BigDecimal getR9_bonds_amt() {
			return r9_bonds_amt;
		}

		public void setR9_bonds_amt(BigDecimal r9_bonds_amt) {
			this.r9_bonds_amt = r9_bonds_amt;
		}

		public BigDecimal getR9_cp_amt() {
			return r9_cp_amt;
		}

		public void setR9_cp_amt(BigDecimal r9_cp_amt) {
			this.r9_cp_amt = r9_cp_amt;
		}

		public BigDecimal getR9_cd_amt() {
			return r9_cd_amt;
		}

		public void setR9_cd_amt(BigDecimal r9_cd_amt) {
			this.r9_cd_amt = r9_cd_amt;
		}

		public BigDecimal getR9_tier_amt() {
			return r9_tier_amt;
		}

		public void setR9_tier_amt(BigDecimal r9_tier_amt) {
			this.r9_tier_amt = r9_tier_amt;
		}

		public BigDecimal getR9_unit_amt() {
			return r9_unit_amt;
		}

		public void setR9_unit_amt(BigDecimal r9_unit_amt) {
			this.r9_unit_amt = r9_unit_amt;
		}

		public BigDecimal getR9_venture_amt() {
			return r9_venture_amt;
		}

		public void setR9_venture_amt(BigDecimal r9_venture_amt) {
			this.r9_venture_amt = r9_venture_amt;
		}

		public BigDecimal getR9_ptc_amt() {
			return r9_ptc_amt;
		}

		public void setR9_ptc_amt(BigDecimal r9_ptc_amt) {
			this.r9_ptc_amt = r9_ptc_amt;
		}

		public BigDecimal getR9_purchase_amt() {
			return r9_purchase_amt;
		}

		public void setR9_purchase_amt(BigDecimal r9_purchase_amt) {
			this.r9_purchase_amt = r9_purchase_amt;
		}

		public BigDecimal getR9_other_amt() {
			return r9_other_amt;
		}

		public void setR9_other_amt(BigDecimal r9_other_amt) {
			this.r9_other_amt = r9_other_amt;
		}

		// Getters and Setters - R10
		public BigDecimal getR10_short_term_amt() {
			return r10_short_term_amt;
		}

		public void setR10_short_term_amt(BigDecimal r10_short_term_amt) {
			this.r10_short_term_amt = r10_short_term_amt;
		}

		public BigDecimal getR10_equity() {
			return r10_equity;
		}

		public void setR10_equity(BigDecimal r10_equity) {
			this.r10_equity = r10_equity;
		}

		public BigDecimal getR10_bonds_amt() {
			return r10_bonds_amt;
		}

		public void setR10_bonds_amt(BigDecimal r10_bonds_amt) {
			this.r10_bonds_amt = r10_bonds_amt;
		}

		public BigDecimal getR10_cp_amt() {
			return r10_cp_amt;
		}

		public void setR10_cp_amt(BigDecimal r10_cp_amt) {
			this.r10_cp_amt = r10_cp_amt;
		}

		public BigDecimal getR10_cd_amt() {
			return r10_cd_amt;
		}

		public void setR10_cd_amt(BigDecimal r10_cd_amt) {
			this.r10_cd_amt = r10_cd_amt;
		}

		public BigDecimal getR10_tier_amt() {
			return r10_tier_amt;
		}

		public void setR10_tier_amt(BigDecimal r10_tier_amt) {
			this.r10_tier_amt = r10_tier_amt;
		}

		public BigDecimal getR10_unit_amt() {
			return r10_unit_amt;
		}

		public void setR10_unit_amt(BigDecimal r10_unit_amt) {
			this.r10_unit_amt = r10_unit_amt;
		}

		public BigDecimal getR10_venture_amt() {
			return r10_venture_amt;
		}

		public void setR10_venture_amt(BigDecimal r10_venture_amt) {
			this.r10_venture_amt = r10_venture_amt;
		}

		public BigDecimal getR10_ptc_amt() {
			return r10_ptc_amt;
		}

		public void setR10_ptc_amt(BigDecimal r10_ptc_amt) {
			this.r10_ptc_amt = r10_ptc_amt;
		}

		public BigDecimal getR10_purchase_amt() {
			return r10_purchase_amt;
		}

		public void setR10_purchase_amt(BigDecimal r10_purchase_amt) {
			this.r10_purchase_amt = r10_purchase_amt;
		}

		public BigDecimal getR10_other_amt() {
			return r10_other_amt;
		}

		public void setR10_other_amt(BigDecimal r10_other_amt) {
			this.r10_other_amt = r10_other_amt;
		}

		// Getters and Setters - R11
		public BigDecimal getR11_short_term_amt() {
			return r11_short_term_amt;
		}

		public void setR11_short_term_amt(BigDecimal r11_short_term_amt) {
			this.r11_short_term_amt = r11_short_term_amt;
		}

		public BigDecimal getR11_equity() {
			return r11_equity;
		}

		public void setR11_equity(BigDecimal r11_equity) {
			this.r11_equity = r11_equity;
		}

		public BigDecimal getR11_bonds_amt() {
			return r11_bonds_amt;
		}

		public void setR11_bonds_amt(BigDecimal r11_bonds_amt) {
			this.r11_bonds_amt = r11_bonds_amt;
		}

		public BigDecimal getR11_cp_amt() {
			return r11_cp_amt;
		}

		public void setR11_cp_amt(BigDecimal r11_cp_amt) {
			this.r11_cp_amt = r11_cp_amt;
		}

		public BigDecimal getR11_cd_amt() {
			return r11_cd_amt;
		}

		public void setR11_cd_amt(BigDecimal r11_cd_amt) {
			this.r11_cd_amt = r11_cd_amt;
		}

		public BigDecimal getR11_tier_amt() {
			return r11_tier_amt;
		}

		public void setR11_tier_amt(BigDecimal r11_tier_amt) {
			this.r11_tier_amt = r11_tier_amt;
		}

		public BigDecimal getR11_unit_amt() {
			return r11_unit_amt;
		}

		public void setR11_unit_amt(BigDecimal r11_unit_amt) {
			this.r11_unit_amt = r11_unit_amt;
		}

		public BigDecimal getR11_venture_amt() {
			return r11_venture_amt;
		}

		public void setR11_venture_amt(BigDecimal r11_venture_amt) {
			this.r11_venture_amt = r11_venture_amt;
		}

		public BigDecimal getR11_ptc_amt() {
			return r11_ptc_amt;
		}

		public void setR11_ptc_amt(BigDecimal r11_ptc_amt) {
			this.r11_ptc_amt = r11_ptc_amt;
		}

		public BigDecimal getR11_purchase_amt() {
			return r11_purchase_amt;
		}

		public void setR11_purchase_amt(BigDecimal r11_purchase_amt) {
			this.r11_purchase_amt = r11_purchase_amt;
		}

		public BigDecimal getR11_other_amt() {
			return r11_other_amt;
		}

		public void setR11_other_amt(BigDecimal r11_other_amt) {
			this.r11_other_amt = r11_other_amt;
		}

		// Getters and Setters - R12
		public BigDecimal getR12_short_term_amt() {
			return r12_short_term_amt;
		}

		public void setR12_short_term_amt(BigDecimal r12_short_term_amt) {
			this.r12_short_term_amt = r12_short_term_amt;
		}

		public BigDecimal getR12_equity() {
			return r12_equity;
		}

		public void setR12_equity(BigDecimal r12_equity) {
			this.r12_equity = r12_equity;
		}

		public BigDecimal getR12_bonds_amt() {
			return r12_bonds_amt;
		}

		public void setR12_bonds_amt(BigDecimal r12_bonds_amt) {
			this.r12_bonds_amt = r12_bonds_amt;
		}

		public BigDecimal getR12_cp_amt() {
			return r12_cp_amt;
		}

		public void setR12_cp_amt(BigDecimal r12_cp_amt) {
			this.r12_cp_amt = r12_cp_amt;
		}

		public BigDecimal getR12_cd_amt() {
			return r12_cd_amt;
		}

		public void setR12_cd_amt(BigDecimal r12_cd_amt) {
			this.r12_cd_amt = r12_cd_amt;
		}

		public BigDecimal getR12_tier_amt() {
			return r12_tier_amt;
		}

		public void setR12_tier_amt(BigDecimal r12_tier_amt) {
			this.r12_tier_amt = r12_tier_amt;
		}

		public BigDecimal getR12_unit_amt() {
			return r12_unit_amt;
		}

		public void setR12_unit_amt(BigDecimal r12_unit_amt) {
			this.r12_unit_amt = r12_unit_amt;
		}

		public BigDecimal getR12_venture_amt() {
			return r12_venture_amt;
		}

		public void setR12_venture_amt(BigDecimal r12_venture_amt) {
			this.r12_venture_amt = r12_venture_amt;
		}

		public BigDecimal getR12_ptc_amt() {
			return r12_ptc_amt;
		}

		public void setR12_ptc_amt(BigDecimal r12_ptc_amt) {
			this.r12_ptc_amt = r12_ptc_amt;
		}

		public BigDecimal getR12_purchase_amt() {
			return r12_purchase_amt;
		}

		public void setR12_purchase_amt(BigDecimal r12_purchase_amt) {
			this.r12_purchase_amt = r12_purchase_amt;
		}

		public BigDecimal getR12_other_amt() {
			return r12_other_amt;
		}

		public void setR12_other_amt(BigDecimal r12_other_amt) {
			this.r12_other_amt = r12_other_amt;
		}

		// Getters and Setters - R13
		public BigDecimal getR13_short_term_amt() {
			return r13_short_term_amt;
		}

		public void setR13_short_term_amt(BigDecimal r13_short_term_amt) {
			this.r13_short_term_amt = r13_short_term_amt;
		}

		public BigDecimal getR13_equity() {
			return r13_equity;
		}

		public void setR13_equity(BigDecimal r13_equity) {
			this.r13_equity = r13_equity;
		}

		public BigDecimal getR13_bonds_amt() {
			return r13_bonds_amt;
		}

		public void setR13_bonds_amt(BigDecimal r13_bonds_amt) {
			this.r13_bonds_amt = r13_bonds_amt;
		}

		public BigDecimal getR13_cp_amt() {
			return r13_cp_amt;
		}

		public void setR13_cp_amt(BigDecimal r13_cp_amt) {
			this.r13_cp_amt = r13_cp_amt;
		}

		public BigDecimal getR13_cd_amt() {
			return r13_cd_amt;
		}

		public void setR13_cd_amt(BigDecimal r13_cd_amt) {
			this.r13_cd_amt = r13_cd_amt;
		}

		public BigDecimal getR13_tier_amt() {
			return r13_tier_amt;
		}

		public void setR13_tier_amt(BigDecimal r13_tier_amt) {
			this.r13_tier_amt = r13_tier_amt;
		}

		public BigDecimal getR13_unit_amt() {
			return r13_unit_amt;
		}

		public void setR13_unit_amt(BigDecimal r13_unit_amt) {
			this.r13_unit_amt = r13_unit_amt;
		}

		public BigDecimal getR13_venture_amt() {
			return r13_venture_amt;
		}

		public void setR13_venture_amt(BigDecimal r13_venture_amt) {
			this.r13_venture_amt = r13_venture_amt;
		}

		public BigDecimal getR13_ptc_amt() {
			return r13_ptc_amt;
		}

		public void setR13_ptc_amt(BigDecimal r13_ptc_amt) {
			this.r13_ptc_amt = r13_ptc_amt;
		}

		public BigDecimal getR13_purchase_amt() {
			return r13_purchase_amt;
		}

		public void setR13_purchase_amt(BigDecimal r13_purchase_amt) {
			this.r13_purchase_amt = r13_purchase_amt;
		}

		public BigDecimal getR13_other_amt() {
			return r13_other_amt;
		}

		public void setR13_other_amt(BigDecimal r13_other_amt) {
			this.r13_other_amt = r13_other_amt;
		}

		// Getters and Setters - R14
		public BigDecimal getR14_short_term_amt() {
			return r14_short_term_amt;
		}

		public void setR14_short_term_amt(BigDecimal r14_short_term_amt) {
			this.r14_short_term_amt = r14_short_term_amt;
		}

		public BigDecimal getR14_equity() {
			return r14_equity;
		}

		public void setR14_equity(BigDecimal r14_equity) {
			this.r14_equity = r14_equity;
		}

		public BigDecimal getR14_bonds_amt() {
			return r14_bonds_amt;
		}

		public void setR14_bonds_amt(BigDecimal r14_bonds_amt) {
			this.r14_bonds_amt = r14_bonds_amt;
		}

		public BigDecimal getR14_cp_amt() {
			return r14_cp_amt;
		}

		public void setR14_cp_amt(BigDecimal r14_cp_amt) {
			this.r14_cp_amt = r14_cp_amt;
		}

		public BigDecimal getR14_cd_amt() {
			return r14_cd_amt;
		}

		public void setR14_cd_amt(BigDecimal r14_cd_amt) {
			this.r14_cd_amt = r14_cd_amt;
		}

		public BigDecimal getR14_tier_amt() {
			return r14_tier_amt;
		}

		public void setR14_tier_amt(BigDecimal r14_tier_amt) {
			this.r14_tier_amt = r14_tier_amt;
		}

		public BigDecimal getR14_unit_amt() {
			return r14_unit_amt;
		}

		public void setR14_unit_amt(BigDecimal r14_unit_amt) {
			this.r14_unit_amt = r14_unit_amt;
		}

		public BigDecimal getR14_venture_amt() {
			return r14_venture_amt;
		}

		public void setR14_venture_amt(BigDecimal r14_venture_amt) {
			this.r14_venture_amt = r14_venture_amt;
		}

		public BigDecimal getR14_ptc_amt() {
			return r14_ptc_amt;
		}

		public void setR14_ptc_amt(BigDecimal r14_ptc_amt) {
			this.r14_ptc_amt = r14_ptc_amt;
		}

		public BigDecimal getR14_purchase_amt() {
			return r14_purchase_amt;
		}

		public void setR14_purchase_amt(BigDecimal r14_purchase_amt) {
			this.r14_purchase_amt = r14_purchase_amt;
		}

		public BigDecimal getR14_other_amt() {
			return r14_other_amt;
		}

		public void setR14_other_amt(BigDecimal r14_other_amt) {
			this.r14_other_amt = r14_other_amt;
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

		public Date getREPORT_RESUBDATE() {
			return REPORT_RESUBDATE;
		}

		public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
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

	public class DBS10_FINCON_III_1C_Detail_Entity {

		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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

	class DBS10_FINCON_III_1CDetailRowMapper implements RowMapper<DBS10_FINCON_III_1C_Detail_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Detail_Entity obj = new DBS10_FINCON_III_1C_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
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

	class DBS10_FINCON_III_1CArchivalDetailRowMapper implements RowMapper<DBS10_FINCON_III_1C_Archival_Detail_Entity> {

		@Override
		public DBS10_FINCON_III_1C_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

			DBS10_FINCON_III_1C_Archival_Detail_Entity obj = new DBS10_FINCON_III_1C_Archival_Detail_Entity();
			obj.setSno(rs.getLong("SNO"));
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

	public class DBS10_FINCON_III_1C_Archival_Detail_Entity {

		private Long sno;
		@Column(name = "CUST_ID")
		private String custId;

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

	public ModelAndView getDBS10_FINCON_III_1CView(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		System.out.println("DBS10_FINCON_III_1C View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);

		// ARCHIVAL + RESUB MODE
		if (("ARCHIVAL".equals(type) || "RESUB".equals(type)) && version != null) {

			List<DBS10_FINCON_III_1C_Archival_Summary_Entity> T1Master = new ArrayList<>();
			List<DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity> T2Master = new ArrayList<>();
			try {

				Date dt = dateformat.parse(todate);

				T1Master = getdatabydateListarchival(dt, version);
				T2Master = getManualArchivalByDate(dt, version);

				System.out.println("Archival Manual size = " + T2Master.size());

				mv.addObject("report_date", dateformat.format(dt));
				System.out.println(type + " Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));
				System.out.println("getishighestversion(dt, version) : " + getishighestversion(dt, version));
				mv.addObject("allowdetail", getishighestversion(dt, version));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		// NORMAL MODE
		else {

			List<DBS10_FINCON_III_1C_Summary_Entity> T1Master = new ArrayList<>();
			List<DBS10_FINCON_III_1C_Manual_Summary_Entity> T2Master = new ArrayList<>();
			try {

				Date dt = dateformat.parse(todate);

				T1Master = getDataByDate(dt);
				T2Master = getManualDataByDate(dt);

				System.out.println("Manual size = " + T2Master.size());

				mv.addObject("report_date", dateformat.format(dt));
				System.out.println("Summary size = " + T1Master.size());

				mv.addObject("REPORT_DATE", dateformat.format(dt));

			} catch (Exception e) {
				e.printStackTrace();
			}

			mv.addObject("reportsummary", T1Master);
			mv.addObject("reportsummary1", T2Master);
		}

		mv.setViewName("BRRS/DBS10_FINCON_III_1C");
		mv.addObject("displaymode", "summary");

		System.out.println("View Loaded: " + mv.getViewName());

		return mv;
	}

	// =========================
// MODEL AND VIEW METHOD detail
//=========================

	public ModelAndView getDBS10_FINCON_III_1CcurrentDtl(String reportId, String fromdate, String todate,
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

				List<DBS10_FINCON_III_1C_Archival_Detail_Entity> detailList;

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

				List<DBS10_FINCON_III_1C_Detail_Entity> currentDetailList;

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

		mv.setViewName("BRRS/DBS10_FINCON_III_1C");
		mv.addObject("displaymode", "Details");
		mv.addObject("menu", reportId);
		mv.addObject("currency", currency);
		mv.addObject("reportId", reportId);

		return mv;
	}

//Archival View
	public List<Object[]> getDBS10_FINCON_III_1CArchival() {
		List<Object[]> archivalList = new ArrayList<>();

		try {

			List<DBS10_FINCON_III_1C_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (DBS10_FINCON_III_1C_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getREPORT_RESUBDATE() };
					archivalList.add(row);
				}

				System.out.println("Fetched " + archivalList.size() + " archival records");
				DBS10_FINCON_III_1C_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest archival version: " + first.getReport_version());
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  DBS10_FINCON_III_1C  Archival data: " + e.getMessage());
			e.printStackTrace();
		}

		return archivalList;
	}

@Transactional
public void updateReport(Object entity, String type) {

    boolean isResub = "RESUB".equalsIgnoreCase(type);

    System.out.println("Came to DBS10_FINCON_III_1C Manual Update. Type: " + (isResub ? "RESUB" : "NORMAL"));

    String tableName = isResub ? "BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY"
            : "BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE_MANUAL";

    // Target rows: r8 to r14
    int[] rows = { 8, 9, 10, 11, 12, 13, 14 };

    // Target columns for each row
    String[] cols = { 
        "short_term_amt", "equity", "bonds_amt", "cp_amt", 
        "cd_amt", "tier_amt", "unit_amt", "venture_amt", 
        "ptc_amt", "purchase_amt", "other_amt" 
    };

    try {
        // Use the actual runtime class
        Class<?> entityClass = entity.getClass();

        // Get report date
        Method getDateMethod = entityClass.getMethod("getReport_date");
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

            for (String col : cols) {

                // Dynamically constructs getter name (e.g., getR8_short_term_amt)
                String getterName = "getR" + r + "_" + col;
                String columnName = "R" + r + "_" + col;

                try {
                    Method getter = entityClass.getMethod(getterName);
                    Object newValueObj = getter.invoke(entity);

                    System.out.println("Processing -> " + getterName + " = " + newValueObj);

                    // Skip processing if the input value is null
                    if (newValueObj == null) {
                        continue;
                    }

                    // 1. Fetch current value directly from DB before updating
                    String selectSql = "SELECT " + columnName + " FROM " + tableName + " WHERE REPORT_DATE = ?";
                    Object dbValueObj = null;
                    try {
                        dbValueObj = jdbcTemplate.queryForObject(selectSql, Object.class, sqlReportDate);
                    } catch (Exception e) {
                        // Handle gracefully if record/column isn't present
                        dbValueObj = null;
                    }

                    // 2. Normalize comparison values
                    String currentValStr = (dbValueObj == null) ? "" : dbValueObj.toString().trim();
                    String newValStr = newValueObj.toString().trim();

                    // Skip update if value hasn't changed
                    if (currentValStr.equals(newValStr)) {
                        continue;
                    }

                    // 3. Track change in audit builder
                    if (changesBuilder.length() > 0) {
                        changesBuilder.append("|||");
                    }
                    changesBuilder.append(columnName.toUpperCase()).append(": OldValue: ")
                            .append(currentValStr.isEmpty() ? "null" : currentValStr).append(", NewValue: ")
                            .append(newValStr);

                    // 4. Update database column
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
        System.out.println("DBS10_FINCON_III_1C Manual Changes Length = " + changes.length());

        if (!changes.isEmpty()) {
            if (changes.length() > 1900) {
                changes = changes.substring(0, 1900);
            }

            auditService.compareEntitiesmanual(entity, entity, reportDate.toString(),
                    "DBS10_FINCON_III_1C Manual Screen", tableName);
        }

        System.out.println("DBS10_FINCON_III_1C Manual Update Completed Successfully for Type : " + type);

    } catch (Exception e) {
        System.err.println("===== DBS10_FINCON_III_1C UPDATE ERROR =====");
        e.printStackTrace();

        Throwable root = e;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        System.err.println("ROOT CAUSE : " + root.getMessage());

        throw new RuntimeException("Error while updating DBS10_FINCON_III_1C Manual fields for type: " + type, e);
    }
}
	public ModelAndView getViewOrEditPage(String SNO, String formMode, String type) {
		ModelAndView mv = new ModelAndView("BRRS/DBS10_FINCON_III_1C");

		System.out.println("sno is : " + SNO);
		System.out.println("Type: " + type);
		if (SNO != null) {
			if (type == "RESUB" || type.equals("RESUB")) {
				System.out.println("Inside RESUB FETCH");
				DBS10_FINCON_III_1C_Detail_Entity DBS10_FINCON_III_1CEntity = findBysnoArch(SNO);
				if (DBS10_FINCON_III_1CEntity != null && DBS10_FINCON_III_1CEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(DBS10_FINCON_III_1CEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("DBS10_FINCON_III_1CData", DBS10_FINCON_III_1CEntity);
			} else {
				DBS10_FINCON_III_1C_Detail_Entity DBS10_FINCON_III_1CEntity = findBysno(SNO);
				if (DBS10_FINCON_III_1CEntity != null && DBS10_FINCON_III_1CEntity.getReportDate() != null) {
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(DBS10_FINCON_III_1CEntity.getReportDate());
					mv.addObject("asondate", formattedDate);
				}
				mv.addObject("DBS10_FINCON_III_1CData", DBS10_FINCON_III_1CEntity);
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

			String acctName = request.getParameter("acctName");

			String reportDateStr = request.getParameter("reportDate");

			System.out.println("Sno is : " + Sno);
			String type = request.getParameter("type");
			String entry = (request.getParameter("entry") != null) ? request.getParameter("entry") : "YES";

			// Load Existing Record
			DBS10_FINCON_III_1C_Detail_Entity existing = null;

			System.out.println("type is : " + type);
			if ((type == "RESUB") || (type.equals("RESUB"))) {
				existing = findBysnoArch(Sno);
			} else {
				existing = findBysno(Sno);
			}
			DBS10_FINCON_III_1C_Detail_Entity oldcopy = new DBS10_FINCON_III_1C_Detail_Entity();
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

			// Save using JDBC
			if (isChanged) {
				String sql;
				System.out.println("Type in update block : " + type);
				// Safe from NullPointerExceptions and reference comparison bugs
				if ("RESUB".equalsIgnoreCase(type)) {
					System.out.println("Inside RESUB UPDATE");
					sql = "UPDATE BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				} else {
					System.out.println("Inside NORMAL UPDATE");
					sql = "UPDATE BRRS_DBS10_FINCON_III_1C_DETAILTABLE " + "SET ACCT_NAME = ?, "
							+ "ACCT_BALANCE_IN_PULA = ? " + "WHERE SNO = ?";
				}

				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), Sno);
				jdbcTemplate.update(sql, existing.getAcctName(), existing.getAcctBalanceInpula(), Sno);
				if ((type == "RESUB") || (type.equals("RESUB"))) {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "DBS10_FINCON_III_1C Archival Screen",
							"BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL");
				} else {
					auditService.compareEntitiesmanual(oldcopy, existing, Sno, "DBS10_FINCON_III_1C Screen",
							"BRRS_DBS10_FINCON_III_1C_DETAILTABLE");
				}
				System.out.println("Record updated using JDBC");

				Run_DBS10_FINCON_III_1C_Procudure(reportDateStr, type, entry);

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
			Run_DBS10_FINCON_III_1C_Procudure(request.getParameter("reportDate"), request.getParameter("type"),
					request.getParameter("entry"));
			return ResponseEntity.ok("Resubmitted successfully!");
		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating record: " + e.getMessage());

		}
	}

	private void Run_DBS10_FINCON_III_1C_Procudure(String reportDateStr, String type, String entry) {

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

					// Convert String date to SQL Date
					java.sql.Date sqlDate = new java.sql.Date(
							new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate).getTime());

					System.out.println("formattedDate = " + formattedDate);
					System.out.println("sqlDate = " + sqlDate);

					if (isResubNoEntry) {
						String bdsql = "DELETE FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(bdsql, sqlDate);
						System.out
								.println("Successfully deleted before executing procedure: " + rowsDeleted + " rows.");

						String sqltransfer = "INSERT INTO BRRS_DBS10_FINCON_III_1C_DETAILTABLE "
								+ " (SNO, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABLE, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION) "
								+ "SELECT SNO, ACCT_NUMBER, CUST_ID, ACCT_BALANCE_IN_PULA, AVERAGE, REPORT_LABLE, REPORT_ADDL_CRITERIA_1, REPORT_NAME, REPORT_DATE, DATA_ENTRY_VERSION "
								+ "FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";
						int rowsInserted = jdbcTemplate.update(sqltransfer, sqlDate);
						System.out.println("Successfully transferred: " + rowsInserted + " rows.");
					}

					if (shouldExecuteProcedure) {
						jdbcTemplate.update("BEGIN BRRS_DBS10_FINCON_III_1C_SUMMARY_PROCEDURE(?); END;", formattedDate);
						System.out.println("Procedure executed");
					}

					if (isResubNoEntry) {
						String adsql = "DELETE FROM BRRS_DBS10_FINCON_III_1C_DETAILTABLE WHERE REPORT_DATE = ?";
						int rowsDeleted = jdbcTemplate.update(adsql, sqlDate);
						System.out.println("Successfully deleted after executing procedure: " + rowsDeleted + " rows.");

						// 1. Handle Archival Summary Table (System Generated: R8 through R14)
						String ins_sum_sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxVersion = jdbcTemplate.queryForObject(ins_sum_sql, Integer.class, sqlDate);
						int highestValue = (maxVersion != null ? maxVersion : 0) + 1;

						String finalsql = "INSERT INTO BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY ("
								+ "R8_NAME_OF_SFI, R8_NAME_OF_CP, R8_LOANS_AMT, R8_DEPOSIT_AMT, R8_TOTAL_AMT, "
								+ "R9_NAME_OF_SFI, R9_NAME_OF_CP, R9_LOANS_AMT, R9_DEPOSIT_AMT, R9_TOTAL_AMT, "
								+ "R10_NAME_OF_SFI, R10_NAME_OF_CP, R10_LOANS_AMT, R10_DEPOSIT_AMT, R10_TOTAL_AMT, "
								+ "R11_NAME_OF_SFI, R11_NAME_OF_CP, R11_LOANS_AMT, R11_DEPOSIT_AMT, R11_TOTAL_AMT, "
								+ "R12_NAME_OF_SFI, R12_NAME_OF_CP, R12_LOANS_AMT, R12_DEPOSIT_AMT, R12_TOTAL_AMT, "
								+ "R13_NAME_OF_SFI, R13_NAME_OF_CP, R13_LOANS_AMT, R13_DEPOSIT_AMT, R13_TOTAL_AMT, "
								+ "R14_NAME_OF_SFI, R14_NAME_OF_CP, R14_LOANS_AMT, R14_DEPOSIT_AMT, R14_TOTAL_AMT, "
								+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
								+ ") " + "SELECT "
								+ "R8_NAME_OF_SFI, R8_NAME_OF_CP, R8_LOANS_AMT, R8_DEPOSIT_AMT, R8_TOTAL_AMT, "
								+ "R9_NAME_OF_SFI, R9_NAME_OF_CP, R9_LOANS_AMT, R9_DEPOSIT_AMT, R9_TOTAL_AMT, "
								+ "R10_NAME_OF_SFI, R10_NAME_OF_CP, R10_LOANS_AMT, R10_DEPOSIT_AMT, R10_TOTAL_AMT, "
								+ "R11_NAME_OF_SFI, R11_NAME_OF_CP, R11_LOANS_AMT, R11_DEPOSIT_AMT, R11_TOTAL_AMT, "
								+ "R12_NAME_OF_SFI, R12_NAME_OF_CP, R12_LOANS_AMT, R12_DEPOSIT_AMT, R12_TOTAL_AMT, "
								+ "R13_NAME_OF_SFI, R13_NAME_OF_CP, R13_LOANS_AMT, R13_DEPOSIT_AMT, R13_TOTAL_AMT, "
								+ "R14_NAME_OF_SFI, R14_NAME_OF_CP, R14_LOANS_AMT, R14_DEPOSIT_AMT, R14_TOTAL_AMT, "
								+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
								+ "FROM BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE WHERE REPORT_DATE = ?";

						int rowsInsertedSum = jdbcTemplate.update(finalsql, highestValue, sqlDate);
						System.out.println("Successfully transferred system summary: " + rowsInsertedSum + " rows.");

						// 2. Handle Manual Archival Summary Table (User Edited: R8 through R14
						// breakdown fields)
						String insManualSql = "SELECT MAX(REPORT_VERSION) FROM BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";
						Integer maxManualVersion = jdbcTemplate.queryForObject(insManualSql, Integer.class, sqlDate);

						int manualVersion = (maxManualVersion != null ? maxManualVersion : 0) + 1;
						int manualRowsInserted = 0;

						if (maxManualVersion != null && maxManualVersion > 0) {
							// Fetch from PREVIOUS VERSION of the MANUAL ARCHIVAL table
							String manualFinalSql = "INSERT INTO BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY ("
									+ "R8_SHORT_TERM_AMT, R8_EQUITY, R8_BONDS_AMT, R8_CP_AMT, R8_CD_AMT, R8_TIER_AMT, R8_UNIT_AMT, R8_VENTURE_AMT, R8_PTC_AMT, R8_PURCHASE_AMT, R8_OTHER_AMT, "
									+ "R9_SHORT_TERM_AMT, R9_EQUITY, R9_BONDS_AMT, R9_CP_AMT, R9_CD_AMT, R9_TIER_AMT, R9_UNIT_AMT, R9_VENTURE_AMT, R9_PTC_AMT, R9_PURCHASE_AMT, R9_OTHER_AMT, "
									+ "R10_SHORT_TERM_AMT, R10_EQUITY, R10_BONDS_AMT, R10_CP_AMT, R10_CD_AMT, R10_TIER_AMT, R10_UNIT_AMT, R10_VENTURE_AMT, R10_PTC_AMT, R10_PURCHASE_AMT, R10_OTHER_AMT, "
									+ "R11_SHORT_TERM_AMT, R11_EQUITY, R11_BONDS_AMT, R11_CP_AMT, R11_CD_AMT, R11_TIER_AMT, R11_UNIT_AMT, R11_VENTURE_AMT, R11_PTC_AMT, R11_PURCHASE_AMT, R11_OTHER_AMT, "
									+ "R12_SHORT_TERM_AMT, R12_EQUITY, R12_BONDS_AMT, R12_CP_AMT, R12_CD_AMT, R12_TIER_AMT, R12_UNIT_AMT, R12_VENTURE_AMT, R12_PTC_AMT, R12_PURCHASE_AMT, R12_OTHER_AMT, "
									+ "R13_SHORT_TERM_AMT, R13_EQUITY, R13_BONDS_AMT, R13_CP_AMT, R13_CD_AMT, R13_TIER_AMT, R13_UNIT_AMT, R13_VENTURE_AMT, R13_PTC_AMT, R13_PURCHASE_AMT, R13_OTHER_AMT, "
									+ "R14_SHORT_TERM_AMT, R14_EQUITY, R14_BONDS_AMT, R14_CP_AMT, R14_CD_AMT, R14_TIER_AMT, R14_UNIT_AMT, R14_VENTURE_AMT, R14_PTC_AMT, R14_PURCHASE_AMT, R14_OTHER_AMT, "
									+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
									+ ") " + "SELECT "
									+ "R8_SHORT_TERM_AMT, R8_EQUITY, R8_BONDS_AMT, R8_CP_AMT, R8_CD_AMT, R8_TIER_AMT, R8_UNIT_AMT, R8_VENTURE_AMT, R8_PTC_AMT, R8_PURCHASE_AMT, R8_OTHER_AMT, "
									+ "R9_SHORT_TERM_AMT, R9_EQUITY, R9_BONDS_AMT, R9_CP_AMT, R9_CD_AMT, R9_TIER_AMT, R9_UNIT_AMT, R9_VENTURE_AMT, R9_PTC_AMT, R9_PURCHASE_AMT, R9_OTHER_AMT, "
									+ "R10_SHORT_TERM_AMT, R10_EQUITY, R10_BONDS_AMT, R10_CP_AMT, R10_CD_AMT, R10_TIER_AMT, R10_UNIT_AMT, R10_VENTURE_AMT, R10_PTC_AMT, R10_PURCHASE_AMT, R10_OTHER_AMT, "
									+ "R11_SHORT_TERM_AMT, R11_EQUITY, R11_BONDS_AMT, R11_CP_AMT, R11_CD_AMT, R11_TIER_AMT, R11_UNIT_AMT, R11_VENTURE_AMT, R11_PTC_AMT, R11_PURCHASE_AMT, R11_OTHER_AMT, "
									+ "R12_SHORT_TERM_AMT, R12_EQUITY, R12_BONDS_AMT, R12_CP_AMT, R12_CD_AMT, R12_TIER_AMT, R12_UNIT_AMT, R12_VENTURE_AMT, R12_PTC_AMT, R12_PURCHASE_AMT, R12_OTHER_AMT, "
									+ "R13_SHORT_TERM_AMT, R13_EQUITY, R13_BONDS_AMT, R13_CP_AMT, R13_CD_AMT, R13_TIER_AMT, R13_UNIT_AMT, R13_VENTURE_AMT, R13_PTC_AMT, R13_PURCHASE_AMT, R13_OTHER_AMT, "
									+ "R14_SHORT_TERM_AMT, R14_EQUITY, R14_BONDS_AMT, R14_CP_AMT, R14_CD_AMT, R14_TIER_AMT, R14_UNIT_AMT, R14_VENTURE_AMT, R14_PTC_AMT, R14_PURCHASE_AMT, R14_OTHER_AMT, "
									+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
									+ "FROM BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY "
									+ "WHERE REPORT_DATE = ? AND REPORT_VERSION = ?";

							manualRowsInserted = jdbcTemplate.update(manualFinalSql, manualVersion, sqlDate,
									maxManualVersion);
						} else {
							// Fallback: Copy from active manual table
							String manualFallbackSql = "INSERT INTO BRRS_DBS10_FINCON_III_1C_MANUAL_ARCHIVALTABLE_SUMMARY ("
									+ "R8_SHORT_TERM_AMT, R8_EQUITY, R8_BONDS_AMT, R8_CP_AMT, R8_CD_AMT, R8_TIER_AMT, R8_UNIT_AMT, R8_VENTURE_AMT, R8_PTC_AMT, R8_PURCHASE_AMT, R8_OTHER_AMT, "
									+ "R9_SHORT_TERM_AMT, R9_EQUITY, R9_BONDS_AMT, R9_CP_AMT, R9_CD_AMT, R9_TIER_AMT, R9_UNIT_AMT, R9_VENTURE_AMT, R9_PTC_AMT, R9_PURCHASE_AMT, R9_OTHER_AMT, "
									+ "R10_SHORT_TERM_AMT, R10_EQUITY, R10_BONDS_AMT, R10_CP_AMT, R10_CD_AMT, R10_TIER_AMT, R10_UNIT_AMT, R10_VENTURE_AMT, R10_PTC_AMT, R10_PURCHASE_AMT, R10_OTHER_AMT, "
									+ "R11_SHORT_TERM_AMT, R11_EQUITY, R11_BONDS_AMT, R11_CP_AMT, R11_CD_AMT, R11_TIER_AMT, R11_UNIT_AMT, R11_VENTURE_AMT, R11_PTC_AMT, R11_PURCHASE_AMT, R11_OTHER_AMT, "
									+ "R12_SHORT_TERM_AMT, R12_EQUITY, R12_BONDS_AMT, R12_CP_AMT, R12_CD_AMT, R12_TIER_AMT, R12_UNIT_AMT, R12_VENTURE_AMT, R12_PTC_AMT, R12_PURCHASE_AMT, R12_OTHER_AMT, "
									+ "R13_SHORT_TERM_AMT, R13_EQUITY, R13_BONDS_AMT, R13_CP_AMT, R13_CD_AMT, R13_TIER_AMT, R13_UNIT_AMT, R13_VENTURE_AMT, R13_PTC_AMT, R13_PURCHASE_AMT, R13_OTHER_AMT, "
									+ "R14_SHORT_TERM_AMT, R14_EQUITY, R14_BONDS_AMT, R14_CP_AMT, R14_CD_AMT, R14_TIER_AMT, R14_UNIT_AMT, R14_VENTURE_AMT, R14_PTC_AMT, R14_PURCHASE_AMT, R14_OTHER_AMT, "
									+ "REPORT_DATE, REPORT_VERSION, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, REPORT_RESUBDATE"
									+ ") " + "SELECT "
									+ "R8_SHORT_TERM_AMT, R8_EQUITY, R8_BONDS_AMT, R8_CP_AMT, R8_CD_AMT, R8_TIER_AMT, R8_UNIT_AMT, R8_VENTURE_AMT, R8_PTC_AMT, R8_PURCHASE_AMT, R8_OTHER_AMT, "
									+ "R9_SHORT_TERM_AMT, R9_EQUITY, R9_BONDS_AMT, R9_CP_AMT, R9_CD_AMT, R9_TIER_AMT, R9_UNIT_AMT, R9_VENTURE_AMT, R9_PTC_AMT, R9_PURCHASE_AMT, R9_OTHER_AMT, "
									+ "R10_SHORT_TERM_AMT, R10_EQUITY, R10_BONDS_AMT, R10_CP_AMT, R10_CD_AMT, R10_TIER_AMT, R10_UNIT_AMT, R10_VENTURE_AMT, R10_PTC_AMT, R10_PURCHASE_AMT, R10_OTHER_AMT, "
									+ "R11_SHORT_TERM_AMT, R11_EQUITY, R11_BONDS_AMT, R11_CP_AMT, R11_CD_AMT, R11_TIER_AMT, R11_UNIT_AMT, R11_VENTURE_AMT, R11_PTC_AMT, R11_PURCHASE_AMT, R11_OTHER_AMT, "
									+ "R12_SHORT_TERM_AMT, R12_EQUITY, R12_BONDS_AMT, R12_CP_AMT, R12_CD_AMT, R12_TIER_AMT, R12_UNIT_AMT, R12_VENTURE_AMT, R12_PTC_AMT, R12_PURCHASE_AMT, R12_OTHER_AMT, "
									+ "R13_SHORT_TERM_AMT, R13_EQUITY, R13_BONDS_AMT, R13_CP_AMT, R13_CD_AMT, R13_TIER_AMT, R13_UNIT_AMT, R13_VENTURE_AMT, R13_PTC_AMT, R13_PURCHASE_AMT, R13_OTHER_AMT, "
									+ "R14_SHORT_TERM_AMT, R14_EQUITY, R14_BONDS_AMT, R14_CP_AMT, R14_CD_AMT, R14_TIER_AMT, R14_UNIT_AMT, R14_VENTURE_AMT, R14_PTC_AMT, R14_PURCHASE_AMT, R14_OTHER_AMT, "
									+ "REPORT_DATE, ?, REPORT_FREQUENCY, REPORT_CODE, REPORT_DESC, ENTITY_FLG, MODIFY_FLG, DEL_FLG, SYSDATE "
									+ "FROM BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE_MANUAL WHERE REPORT_DATE = ?";

							manualRowsInserted = jdbcTemplate.update(manualFallbackSql, manualVersion, sqlDate);
						}

						System.out.println("Manual summary archived successfully into version (" + manualVersion + "): "
								+ manualRowsInserted + " rows.");

						String adsumsql = "DELETE FROM BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE WHERE REPORT_DATE = ?";
						int rowsDeletedSum = jdbcTemplate.update(adsumsql, sqlDate);
						System.out.println("Deleted from summary: " + rowsDeletedSum + " rows after transferring.");
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		});

	}

	public byte[] getDBS10_FINCON_III_1CDetailExcel(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for  DBS10_FINCON_III_1CNEW Details...");
			System.out.println("came to Detail download service");

			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {
				byte[] ARCHIVALreport = getDBS10_FINCON_III_1CDetailNewExcelARCHIVAL(filename, fromdate, todate,
						currency, dtltype, type, version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_III_1C Details New");

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
			List<DBS10_FINCON_III_1C_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_III_1C_Detail_Entity item : reportData) {
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
				logger.info("No data found for DBS10_FINCON_III_1C — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_III_1C Excel", e);
			return new byte[0];
		}
	}

	public byte[] getDBS10_FINCON_III_1CDetailNewExcelARCHIVAL(String filename, String fromdate, String todate,
			String currency, String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for DBS10_FINCON_III_1CNEW ARCHIVAL Details...");
			System.out.println("came to ARCHIVAL Detail download service");
			if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type))) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("DBS10_FINCON_III_1C Detail NEW");

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
			List<DBS10_FINCON_III_1C_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (DBS10_FINCON_III_1C_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for DBS10_FINCON_III_1CNEW — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating DBS10_FINCON_III_1C NEW Excel", e);
			return new byte[0];
		}
	}

	public byte[] BRRS_DBS10_FINCON_III_1CExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.DBS10_FINCON_III_1C");

		// ARCHIVAL check
		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null
				&& version.compareTo(BigDecimal.ZERO) >= 0) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelDBS10_FINCON_III_1CARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type,
					version);
		}

		// Fetch data

		List<DBS10_FINCON_III_1C_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));
		List<DBS10_FINCON_III_1C_Manual_Summary_Entity> dataList1 = getManualDataByDate(dateformat.parse(todate));

		System.out.println("DATA SIZE IS : " + dataList.size());
		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for  DBS10_FINCON_III_1Cnew report. Returning empty result.");
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

			int startRow = 7;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					DBS10_FINCON_III_1C_Summary_Entity record = dataList.get(i);
					DBS10_FINCON_III_1C_Manual_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R8 Col A (Index 0) - String
					Cell cellA = row.createCell(0);
					if (record.getR8_name_of_sfi() != null) {
						cellA.setCellValue(record.getR8_name_of_sfi());
						cellA.setCellStyle(textStyle);
					} else {
						cellA.setCellValue("");
						cellA.setCellStyle(textStyle);
					}

					// R8 Col B (Index 1) - String
					Cell cellB = row.createCell(1);
					if (record.getR8_name_of_cp() != null) {
						cellB.setCellValue(record.getR8_name_of_cp());
						cellB.setCellStyle(textStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// R8 Col C (Index 2) - BigDecimal / Double
					Cell cellC = row.createCell(2);
					if (record.getR8_loans_amt() != null) {
						cellC.setCellValue(record.getR8_loans_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// R8 Col D (Index 3) - BigDecimal / Double
					Cell cellD = row.createCell(3);
					if (record.getR8_deposit_amt() != null) {
						cellD.setCellValue(record.getR8_deposit_amt().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// R8 Col E (Index 4) - Short Term Amt
					Cell cellE = row.createCell(4);
					if (record1.getR8_short_term_amt() != null) {
						cellE.setCellValue(record1.getR8_short_term_amt().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// R8 Col F (Index 5) - Equity
					Cell cellF = row.createCell(5);
					if (record1.getR8_equity() != null) {
						cellF.setCellValue(record1.getR8_equity().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// R8 Col G (Index 6) - Bonds Amt
					Cell cellG = row.createCell(6);
					if (record1.getR8_bonds_amt() != null) {
						cellG.setCellValue(record1.getR8_bonds_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// R8 Col H (Index 7) - CP Amt
					Cell cellH = row.createCell(7);
					if (record1.getR8_cp_amt() != null) {
						cellH.setCellValue(record1.getR8_cp_amt().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// R8 Col I (Index 8) - CD Amt
					Cell cellI = row.createCell(8);
					if (record1.getR8_cd_amt() != null) {
						cellI.setCellValue(record1.getR8_cd_amt().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// R8 Col J (Index 9) - Tier Amt
					Cell cellJ = row.createCell(9);
					if (record1.getR8_tier_amt() != null) {
						cellJ.setCellValue(record1.getR8_tier_amt().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// R8 Col K (Index 10) - Unit Amt
					Cell cellK = row.createCell(10);
					if (record1.getR8_unit_amt() != null) {
						cellK.setCellValue(record1.getR8_unit_amt().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// R8 Col L (Index 11) - Venture Amt
					Cell cellL = row.createCell(11);
					if (record1.getR8_venture_amt() != null) {
						cellL.setCellValue(record1.getR8_venture_amt().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// R8 Col M (Index 12) - PTC Amt
					Cell cellM = row.createCell(12);
					if (record1.getR8_ptc_amt() != null) {
						cellM.setCellValue(record1.getR8_ptc_amt().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// R8 Col N (Index 13) - Purchase Amt
					Cell cellN = row.createCell(13);
					if (record1.getR8_purchase_amt() != null) {
						cellN.setCellValue(record1.getR8_purchase_amt().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// R8 Col O (Index 14) - Other Amt
					Cell cellO = row.createCell(14);
					if (record1.getR8_other_amt() != null) {
						cellO.setCellValue(record1.getR8_other_amt().doubleValue());
						cellO.setCellStyle(numberStyle);
					} else {
						cellO.setCellValue("");
						cellO.setCellStyle(textStyle);
					}

					row = sheet.getRow(8);
					// R9 Col A (Index 0) - String
					Cell cellA1 = row.createCell(0);
					if (record.getR9_name_of_sfi() != null) {
						cellA1.setCellValue(record.getR9_name_of_sfi());
						cellA1.setCellStyle(textStyle);
					} else {
						cellA1.setCellValue("");
						cellA1.setCellStyle(textStyle);
					}

					// R9 Col B (Index 1) - String
					Cell cellB1 = row.createCell(1);
					if (record.getR9_name_of_cp() != null) {
						cellB1.setCellValue(record.getR9_name_of_cp());
						cellB1.setCellStyle(textStyle);
					} else {
						cellB1.setCellValue("");
						cellB1.setCellStyle(textStyle);
					}

					// R9 Col C (Index 2) - BigDecimal / Double
					Cell cellC1 = row.createCell(2);
					if (record.getR9_loans_amt() != null) {
						cellC1.setCellValue(record.getR9_loans_amt().doubleValue());
						cellC1.setCellStyle(numberStyle);
					} else {
						cellC1.setCellValue("");
						cellC1.setCellStyle(textStyle);
					}

					// R9 Col D (Index 3) - BigDecimal / Double
					Cell cellD1 = row.createCell(3);
					if (record.getR9_deposit_amt() != null) {
						cellD1.setCellValue(record.getR9_deposit_amt().doubleValue());
						cellD1.setCellStyle(numberStyle);
					} else {
						cellD1.setCellValue("");
						cellD1.setCellStyle(textStyle);
					}

					// R9 Col E (Index 4) - Short Term Amt
					Cell cellE1 = row.createCell(4);
					if (record1.getR9_short_term_amt() != null) {
						cellE1.setCellValue(record1.getR9_short_term_amt().doubleValue());
						cellE1.setCellStyle(numberStyle);
					} else {
						cellE1.setCellValue("");
						cellE1.setCellStyle(textStyle);
					}

					// R9 Col F (Index 5) - Equity
					Cell cellF1 = row.createCell(5);
					if (record1.getR9_equity() != null) {
						cellF1.setCellValue(record1.getR9_equity().doubleValue());
						cellF1.setCellStyle(numberStyle);
					} else {
						cellF1.setCellValue("");
						cellF1.setCellStyle(textStyle);
					}

					// R9 Col G (Index 6) - Bonds Amt
					Cell cellG1 = row.createCell(6);
					if (record1.getR9_bonds_amt() != null) {
						cellG1.setCellValue(record1.getR9_bonds_amt().doubleValue());
						cellG1.setCellStyle(numberStyle);
					} else {
						cellG1.setCellValue("");
						cellG1.setCellStyle(textStyle);
					}

					// R9 Col H (Index 7) - CP Amt
					Cell cellH1 = row.createCell(7);
					if (record1.getR9_cp_amt() != null) {
						cellH1.setCellValue(record1.getR9_cp_amt().doubleValue());
						cellH1.setCellStyle(numberStyle);
					} else {
						cellH1.setCellValue("");
						cellH1.setCellStyle(textStyle);
					}

					// R9 Col I (Index 8) - CD Amt
					Cell cellI1 = row.createCell(8);
					if (record1.getR9_cd_amt() != null) {
						cellI1.setCellValue(record1.getR9_cd_amt().doubleValue());
						cellI1.setCellStyle(numberStyle);
					} else {
						cellI1.setCellValue("");
						cellI1.setCellStyle(textStyle);
					}

					// R9 Col J (Index 9) - Tier Amt
					Cell cellJ1 = row.createCell(9);
					if (record1.getR9_tier_amt() != null) {
						cellJ1.setCellValue(record1.getR9_tier_amt().doubleValue());
						cellJ1.setCellStyle(numberStyle);
					} else {
						cellJ1.setCellValue("");
						cellJ1.setCellStyle(textStyle);
					}

					// R9 Col K (Index 10) - Unit Amt
					Cell cellK1 = row.createCell(10);
					if (record1.getR9_unit_amt() != null) {
						cellK1.setCellValue(record1.getR9_unit_amt().doubleValue());
						cellK1.setCellStyle(numberStyle);
					} else {
						cellK1.setCellValue("");
						cellK1.setCellStyle(textStyle);
					}

					// R9 Col L (Index 11) - Venture Amt
					Cell cellL1 = row.createCell(11);
					if (record1.getR9_venture_amt() != null) {
						cellL1.setCellValue(record1.getR9_venture_amt().doubleValue());
						cellL1.setCellStyle(numberStyle);
					} else {
						cellL1.setCellValue("");
						cellL1.setCellStyle(textStyle);
					}

					// R9 Col M (Index 12) - PTC Amt
					Cell cellM1 = row.createCell(12);
					if (record1.getR9_ptc_amt() != null) {
						cellM1.setCellValue(record1.getR9_ptc_amt().doubleValue());
						cellM1.setCellStyle(numberStyle);
					} else {
						cellM1.setCellValue("");
						cellM1.setCellStyle(textStyle);
					}

					// R9 Col N (Index 13) - Purchase Amt
					Cell cellN1 = row.createCell(13);
					if (record1.getR9_purchase_amt() != null) {
						cellN1.setCellValue(record1.getR9_purchase_amt().doubleValue());
						cellN1.setCellStyle(numberStyle);
					} else {
						cellN1.setCellValue("");
						cellN1.setCellStyle(textStyle);
					}

					// R9 Col O (Index 14) - Other Amt
					Cell cellO1 = row.createCell(14);
					if (record1.getR9_other_amt() != null) {
						cellO1.setCellValue(record1.getR9_other_amt().doubleValue());
						cellO1.setCellStyle(numberStyle);
					} else {
						cellO1.setCellValue("");
						cellO1.setCellStyle(textStyle);
					}

// ==================== R10 Field Mappings ====================
					row = sheet.getRow(9);
					if (row == null) {
						row = sheet.createRow(9);
					}

// R10 Col A (Index 0) - String
					Cell cellA10 = row.createCell(0);
					if (record.getR10_name_of_sfi() != null) {
						cellA10.setCellValue(record.getR10_name_of_sfi());
						cellA10.setCellStyle(textStyle);
					} else {
						cellA10.setCellValue("");
						cellA10.setCellStyle(textStyle);
					}

// R10 Col B (Index 1) - String
					Cell cellB10 = row.createCell(1);
					if (record.getR10_name_of_cp() != null) {
						cellB10.setCellValue(record.getR10_name_of_cp());
						cellB10.setCellStyle(textStyle);
					} else {
						cellB10.setCellValue("");
						cellB10.setCellStyle(textStyle);
					}

// R10 Col C (Index 2) - BigDecimal / Double
					Cell cellC10 = row.createCell(2);
					if (record.getR10_loans_amt() != null) {
						cellC10.setCellValue(record.getR10_loans_amt().doubleValue());
						cellC10.setCellStyle(numberStyle);
					} else {
						cellC10.setCellValue("");
						cellC10.setCellStyle(textStyle);
					}

// R10 Col D (Index 3) - BigDecimal / Double
					Cell cellD10 = row.createCell(3);
					if (record.getR10_deposit_amt() != null) {
						cellD10.setCellValue(record.getR10_deposit_amt().doubleValue());
						cellD10.setCellStyle(numberStyle);
					} else {
						cellD10.setCellValue("");
						cellD10.setCellStyle(textStyle);
					}

// R10 Col E (Index 4) - Short Term Amt
					Cell cellE10 = row.createCell(4);
					if (record1.getR10_short_term_amt() != null) {
						cellE10.setCellValue(record1.getR10_short_term_amt().doubleValue());
						cellE10.setCellStyle(numberStyle);
					} else {
						cellE10.setCellValue("");
						cellE10.setCellStyle(textStyle);
					}

// R10 Col F (Index 5) - Equity
					Cell cellF10 = row.createCell(5);
					if (record1.getR10_equity() != null) {
						cellF10.setCellValue(record1.getR10_equity().doubleValue());
						cellF10.setCellStyle(numberStyle);
					} else {
						cellF10.setCellValue("");
						cellF10.setCellStyle(textStyle);
					}

// R10 Col G (Index 6) - Bonds Amt
					Cell cellG10 = row.createCell(6);
					if (record1.getR10_bonds_amt() != null) {
						cellG10.setCellValue(record1.getR10_bonds_amt().doubleValue());
						cellG10.setCellStyle(numberStyle);
					} else {
						cellG10.setCellValue("");
						cellG10.setCellStyle(textStyle);
					}

// R10 Col H (Index 7) - CP Amt
					Cell cellH10 = row.createCell(7);
					if (record1.getR10_cp_amt() != null) {
						cellH10.setCellValue(record1.getR10_cp_amt().doubleValue());
						cellH10.setCellStyle(numberStyle);
					} else {
						cellH10.setCellValue("");
						cellH10.setCellStyle(textStyle);
					}

// R10 Col I (Index 8) - CD Amt
					Cell cellI10 = row.createCell(8);
					if (record1.getR10_cd_amt() != null) {
						cellI10.setCellValue(record1.getR10_cd_amt().doubleValue());
						cellI10.setCellStyle(numberStyle);
					} else {
						cellI10.setCellValue("");
						cellI10.setCellStyle(textStyle);
					}

// R10 Col J (Index 9) - Tier Amt
					Cell cellJ10 = row.createCell(9);
					if (record1.getR10_tier_amt() != null) {
						cellJ10.setCellValue(record1.getR10_tier_amt().doubleValue());
						cellJ10.setCellStyle(numberStyle);
					} else {
						cellJ10.setCellValue("");
						cellJ10.setCellStyle(textStyle);
					}

// R10 Col K (Index 10) - Unit Amt
					Cell cellK10 = row.createCell(10);
					if (record1.getR10_unit_amt() != null) {
						cellK10.setCellValue(record1.getR10_unit_amt().doubleValue());
						cellK10.setCellStyle(numberStyle);
					} else {
						cellK10.setCellValue("");
						cellK10.setCellStyle(textStyle);
					}

// R10 Col L (Index 11) - Venture Amt
					Cell cellL10 = row.createCell(11);
					if (record1.getR10_venture_amt() != null) {
						cellL10.setCellValue(record1.getR10_venture_amt().doubleValue());
						cellL10.setCellStyle(numberStyle);
					} else {
						cellL10.setCellValue("");
						cellL10.setCellStyle(textStyle);
					}

// R10 Col M (Index 12) - PTC Amt
					Cell cellM10 = row.createCell(12);
					if (record1.getR10_ptc_amt() != null) {
						cellM10.setCellValue(record1.getR10_ptc_amt().doubleValue());
						cellM10.setCellStyle(numberStyle);
					} else {
						cellM10.setCellValue("");
						cellM10.setCellStyle(textStyle);
					}

// R10 Col N (Index 13) - Purchase Amt
					Cell cellN10 = row.createCell(13);
					if (record1.getR10_purchase_amt() != null) {
						cellN10.setCellValue(record1.getR10_purchase_amt().doubleValue());
						cellN10.setCellStyle(numberStyle);
					} else {
						cellN10.setCellValue("");
						cellN10.setCellStyle(textStyle);
					}

// R10 Col O (Index 14) - Other Amt
					Cell cellO10 = row.createCell(14);
					if (record1.getR10_other_amt() != null) {
						cellO10.setCellValue(record1.getR10_other_amt().doubleValue());
						cellO10.setCellStyle(numberStyle);
					} else {
						cellO10.setCellValue("");
						cellO10.setCellStyle(textStyle);
					}

// ==================== R11 Field Mappings ====================
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}

// R11 Col A (Index 0) - String
					Cell cellA11 = row.createCell(0);
					if (record.getR11_name_of_sfi() != null) {
						cellA11.setCellValue(record.getR11_name_of_sfi());
						cellA11.setCellStyle(textStyle);
					} else {
						cellA11.setCellValue("");
						cellA11.setCellStyle(textStyle);
					}

// R11 Col B (Index 1) - String
					Cell cellB11 = row.createCell(1);
					if (record.getR11_name_of_cp() != null) {
						cellB11.setCellValue(record.getR11_name_of_cp());
						cellB11.setCellStyle(textStyle);
					} else {
						cellB11.setCellValue("");
						cellB11.setCellStyle(textStyle);
					}

// R11 Col C (Index 2) - BigDecimal / Double
					Cell cellC11 = row.createCell(2);
					if (record.getR11_loans_amt() != null) {
						cellC11.setCellValue(record.getR11_loans_amt().doubleValue());
						cellC11.setCellStyle(numberStyle);
					} else {
						cellC11.setCellValue("");
						cellC11.setCellStyle(textStyle);
					}

// R11 Col D (Index 3) - BigDecimal / Double
					Cell cellD11 = row.createCell(3);
					if (record.getR11_deposit_amt() != null) {
						cellD11.setCellValue(record.getR11_deposit_amt().doubleValue());
						cellD11.setCellStyle(numberStyle);
					} else {
						cellD11.setCellValue("");
						cellD11.setCellStyle(textStyle);
					}

// R11 Col E (Index 4) - Short Term Amt
					Cell cellE11 = row.createCell(4);
					if (record1.getR11_short_term_amt() != null) {
						cellE11.setCellValue(record1.getR11_short_term_amt().doubleValue());
						cellE11.setCellStyle(numberStyle);
					} else {
						cellE11.setCellValue("");
						cellE11.setCellStyle(textStyle);
					}

// R11 Col F (Index 5) - Equity
					Cell cellF11 = row.createCell(5);
					if (record1.getR11_equity() != null) {
						cellF11.setCellValue(record1.getR11_equity().doubleValue());
						cellF11.setCellStyle(numberStyle);
					} else {
						cellF11.setCellValue("");
						cellF11.setCellStyle(textStyle);
					}

// R11 Col G (Index 6) - Bonds Amt
					Cell cellG11 = row.createCell(6);
					if (record1.getR11_bonds_amt() != null) {
						cellG11.setCellValue(record1.getR11_bonds_amt().doubleValue());
						cellG11.setCellStyle(numberStyle);
					} else {
						cellG11.setCellValue("");
						cellG11.setCellStyle(textStyle);
					}

// R11 Col H (Index 7) - CP Amt
					Cell cellH11 = row.createCell(7);
					if (record1.getR11_cp_amt() != null) {
						cellH11.setCellValue(record1.getR11_cp_amt().doubleValue());
						cellH11.setCellStyle(numberStyle);
					} else {
						cellH11.setCellValue("");
						cellH11.setCellStyle(textStyle);
					}

// R11 Col I (Index 8) - CD Amt
					Cell cellI11 = row.createCell(8);
					if (record1.getR11_cd_amt() != null) {
						cellI11.setCellValue(record1.getR11_cd_amt().doubleValue());
						cellI11.setCellStyle(numberStyle);
					} else {
						cellI11.setCellValue("");
						cellI11.setCellStyle(textStyle);
					}

// R11 Col J (Index 9) - Tier Amt
					Cell cellJ11 = row.createCell(9);
					if (record1.getR11_tier_amt() != null) {
						cellJ11.setCellValue(record1.getR11_tier_amt().doubleValue());
						cellJ11.setCellStyle(numberStyle);
					} else {
						cellJ11.setCellValue("");
						cellJ11.setCellStyle(textStyle);
					}

// R11 Col K (Index 10) - Unit Amt
					Cell cellK11 = row.createCell(10);
					if (record1.getR11_unit_amt() != null) {
						cellK11.setCellValue(record1.getR11_unit_amt().doubleValue());
						cellK11.setCellStyle(numberStyle);
					} else {
						cellK11.setCellValue("");
						cellK11.setCellStyle(textStyle);
					}

// R11 Col L (Index 11) - Venture Amt
					Cell cellL11 = row.createCell(11);
					if (record1.getR11_venture_amt() != null) {
						cellL11.setCellValue(record1.getR11_venture_amt().doubleValue());
						cellL11.setCellStyle(numberStyle);
					} else {
						cellL11.setCellValue("");
						cellL11.setCellStyle(textStyle);
					}

// R11 Col M (Index 12) - PTC Amt
					Cell cellM11 = row.createCell(12);
					if (record1.getR11_ptc_amt() != null) {
						cellM11.setCellValue(record1.getR11_ptc_amt().doubleValue());
						cellM11.setCellStyle(numberStyle);
					} else {
						cellM11.setCellValue("");
						cellM11.setCellStyle(textStyle);
					}

// R11 Col N (Index 13) - Purchase Amt
					Cell cellN11 = row.createCell(13);
					if (record1.getR11_purchase_amt() != null) {
						cellN11.setCellValue(record1.getR11_purchase_amt().doubleValue());
						cellN11.setCellStyle(numberStyle);
					} else {
						cellN11.setCellValue("");
						cellN11.setCellStyle(textStyle);
					}

// R11 Col O (Index 14) - Other Amt
					Cell cellO11 = row.createCell(14);
					if (record1.getR11_other_amt() != null) {
						cellO11.setCellValue(record1.getR11_other_amt().doubleValue());
						cellO11.setCellStyle(numberStyle);
					} else {
						cellO11.setCellValue("");
						cellO11.setCellStyle(textStyle);
					}

// ==================== R12 Field Mappings ====================
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}

// R12 Col A (Index 0) - String
					Cell cellA12 = row.createCell(0);
					if (record.getR12_name_of_sfi() != null) {
						cellA12.setCellValue(record.getR12_name_of_sfi());
						cellA12.setCellStyle(textStyle);
					} else {
						cellA12.setCellValue("");
						cellA12.setCellStyle(textStyle);
					}

// R12 Col B (Index 1) - String
					Cell cellB12 = row.createCell(1);
					if (record.getR12_name_of_cp() != null) {
						cellB12.setCellValue(record.getR12_name_of_cp());
						cellB12.setCellStyle(textStyle);
					} else {
						cellB12.setCellValue("");
						cellB12.setCellStyle(textStyle);
					}

// R12 Col C (Index 2) - BigDecimal / Double
					Cell cellC12 = row.createCell(2);
					if (record.getR12_loans_amt() != null) {
						cellC12.setCellValue(record.getR12_loans_amt().doubleValue());
						cellC12.setCellStyle(numberStyle);
					} else {
						cellC12.setCellValue("");
						cellC12.setCellStyle(textStyle);
					}

// R12 Col D (Index 3) - BigDecimal / Double
					Cell cellD12 = row.createCell(3);
					if (record.getR12_deposit_amt() != null) {
						cellD12.setCellValue(record.getR12_deposit_amt().doubleValue());
						cellD12.setCellStyle(numberStyle);
					} else {
						cellD12.setCellValue("");
						cellD12.setCellStyle(textStyle);
					}

// R12 Col E (Index 4) - Short Term Amt
					Cell cellE12 = row.createCell(4);
					if (record1.getR12_short_term_amt() != null) {
						cellE12.setCellValue(record1.getR12_short_term_amt().doubleValue());
						cellE12.setCellStyle(numberStyle);
					} else {
						cellE12.setCellValue("");
						cellE12.setCellStyle(textStyle);
					}

// R12 Col F (Index 5) - Equity
					Cell cellF12 = row.createCell(5);
					if (record1.getR12_equity() != null) {
						cellF12.setCellValue(record1.getR12_equity().doubleValue());
						cellF12.setCellStyle(numberStyle);
					} else {
						cellF12.setCellValue("");
						cellF12.setCellStyle(textStyle);
					}

// R12 Col G (Index 6) - Bonds Amt
					Cell cellG12 = row.createCell(6);
					if (record1.getR12_bonds_amt() != null) {
						cellG12.setCellValue(record1.getR12_bonds_amt().doubleValue());
						cellG12.setCellStyle(numberStyle);
					} else {
						cellG12.setCellValue("");
						cellG12.setCellStyle(textStyle);
					}

// R12 Col H (Index 7) - CP Amt
					Cell cellH12 = row.createCell(7);
					if (record1.getR12_cp_amt() != null) {
						cellH12.setCellValue(record1.getR12_cp_amt().doubleValue());
						cellH12.setCellStyle(numberStyle);
					} else {
						cellH12.setCellValue("");
						cellH12.setCellStyle(textStyle);
					}

// R12 Col I (Index 8) - CD Amt
					Cell cellI12 = row.createCell(8);
					if (record1.getR12_cd_amt() != null) {
						cellI12.setCellValue(record1.getR12_cd_amt().doubleValue());
						cellI12.setCellStyle(numberStyle);
					} else {
						cellI12.setCellValue("");
						cellI12.setCellStyle(textStyle);
					}

// R12 Col J (Index 9) - Tier Amt
					Cell cellJ12 = row.createCell(9);
					if (record1.getR12_tier_amt() != null) {
						cellJ12.setCellValue(record1.getR12_tier_amt().doubleValue());
						cellJ12.setCellStyle(numberStyle);
					} else {
						cellJ12.setCellValue("");
						cellJ12.setCellStyle(textStyle);
					}

// R12 Col K (Index 10) - Unit Amt
					Cell cellK12 = row.createCell(10);
					if (record1.getR12_unit_amt() != null) {
						cellK12.setCellValue(record1.getR12_unit_amt().doubleValue());
						cellK12.setCellStyle(numberStyle);
					} else {
						cellK12.setCellValue("");
						cellK12.setCellStyle(textStyle);
					}

// R12 Col L (Index 11) - Venture Amt
					Cell cellL12 = row.createCell(11);
					if (record1.getR12_venture_amt() != null) {
						cellL12.setCellValue(record1.getR12_venture_amt().doubleValue());
						cellL12.setCellStyle(numberStyle);
					} else {
						cellL12.setCellValue("");
						cellL12.setCellStyle(textStyle);
					}

// R12 Col M (Index 12) - PTC Amt
					Cell cellM12 = row.createCell(12);
					if (record1.getR12_ptc_amt() != null) {
						cellM12.setCellValue(record1.getR12_ptc_amt().doubleValue());
						cellM12.setCellStyle(numberStyle);
					} else {
						cellM12.setCellValue("");
						cellM12.setCellStyle(textStyle);
					}

// R12 Col N (Index 13) - Purchase Amt
					Cell cellN12 = row.createCell(13);
					if (record1.getR12_purchase_amt() != null) {
						cellN12.setCellValue(record1.getR12_purchase_amt().doubleValue());
						cellN12.setCellStyle(numberStyle);
					} else {
						cellN12.setCellValue("");
						cellN12.setCellStyle(textStyle);
					}

// R12 Col O (Index 14) - Other Amt
					Cell cellO12 = row.createCell(14);
					if (record1.getR12_other_amt() != null) {
						cellO12.setCellValue(record1.getR12_other_amt().doubleValue());
						cellO12.setCellStyle(numberStyle);
					} else {
						cellO12.setCellValue("");
						cellO12.setCellStyle(textStyle);
					}

// ==================== R13 Field Mappings ====================
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}

// R13 Col A (Index 0) - String
					Cell cellA13 = row.createCell(0);
					if (record.getR13_name_of_sfi() != null) {
						cellA13.setCellValue(record.getR13_name_of_sfi());
						cellA13.setCellStyle(textStyle);
					} else {
						cellA13.setCellValue("");
						cellA13.setCellStyle(textStyle);
					}

// R13 Col B (Index 1) - String
					Cell cellB13 = row.createCell(1);
					if (record.getR13_name_of_cp() != null) {
						cellB13.setCellValue(record.getR13_name_of_cp());
						cellB13.setCellStyle(textStyle);
					} else {
						cellB13.setCellValue("");
						cellB13.setCellStyle(textStyle);
					}

// R13 Col C (Index 2) - BigDecimal / Double
					Cell cellC13 = row.createCell(2);
					if (record.getR13_loans_amt() != null) {
						cellC13.setCellValue(record.getR13_loans_amt().doubleValue());
						cellC13.setCellStyle(numberStyle);
					} else {
						cellC13.setCellValue("");
						cellC13.setCellStyle(textStyle);
					}

// R13 Col D (Index 3) - BigDecimal / Double
					Cell cellD13 = row.createCell(3);
					if (record.getR13_deposit_amt() != null) {
						cellD13.setCellValue(record.getR13_deposit_amt().doubleValue());
						cellD13.setCellStyle(numberStyle);
					} else {
						cellD13.setCellValue("");
						cellD13.setCellStyle(textStyle);
					}

// R13 Col E (Index 4) - Short Term Amt
					Cell cellE13 = row.createCell(4);
					if (record1.getR13_short_term_amt() != null) {
						cellE13.setCellValue(record1.getR13_short_term_amt().doubleValue());
						cellE13.setCellStyle(numberStyle);
					} else {
						cellE13.setCellValue("");
						cellE13.setCellStyle(textStyle);
					}

// R13 Col F (Index 5) - Equity
					Cell cellF13 = row.createCell(5);
					if (record1.getR13_equity() != null) {
						cellF13.setCellValue(record1.getR13_equity().doubleValue());
						cellF13.setCellStyle(numberStyle);
					} else {
						cellF13.setCellValue("");
						cellF13.setCellStyle(textStyle);
					}

// R13 Col G (Index 6) - Bonds Amt
					Cell cellG13 = row.createCell(6);
					if (record1.getR13_bonds_amt() != null) {
						cellG13.setCellValue(record1.getR13_bonds_amt().doubleValue());
						cellG13.setCellStyle(numberStyle);
					} else {
						cellG13.setCellValue("");
						cellG13.setCellStyle(textStyle);
					}

// R13 Col H (Index 7) - CP Amt
					Cell cellH13 = row.createCell(7);
					if (record1.getR13_cp_amt() != null) {
						cellH13.setCellValue(record1.getR13_cp_amt().doubleValue());
						cellH13.setCellStyle(numberStyle);
					} else {
						cellH13.setCellValue("");
						cellH13.setCellStyle(textStyle);
					}

// R13 Col I (Index 8) - CD Amt
					Cell cellI13 = row.createCell(8);
					if (record1.getR13_cd_amt() != null) {
						cellI13.setCellValue(record1.getR13_cd_amt().doubleValue());
						cellI13.setCellStyle(numberStyle);
					} else {
						cellI13.setCellValue("");
						cellI13.setCellStyle(textStyle);
					}

// R13 Col J (Index 9) - Tier Amt
					Cell cellJ13 = row.createCell(9);
					if (record1.getR13_tier_amt() != null) {
						cellJ13.setCellValue(record1.getR13_tier_amt().doubleValue());
						cellJ13.setCellStyle(numberStyle);
					} else {
						cellJ13.setCellValue("");
						cellJ13.setCellStyle(textStyle);
					}

// R13 Col K (Index 10) - Unit Amt
					Cell cellK13 = row.createCell(10);
					if (record1.getR13_unit_amt() != null) {
						cellK13.setCellValue(record1.getR13_unit_amt().doubleValue());
						cellK13.setCellStyle(numberStyle);
					} else {
						cellK13.setCellValue("");
						cellK13.setCellStyle(textStyle);
					}

// R13 Col L (Index 11) - Venture Amt
					Cell cellL13 = row.createCell(11);
					if (record1.getR13_venture_amt() != null) {
						cellL13.setCellValue(record1.getR13_venture_amt().doubleValue());
						cellL13.setCellStyle(numberStyle);
					} else {
						cellL13.setCellValue("");
						cellL13.setCellStyle(textStyle);
					}

// R13 Col M (Index 12) - PTC Amt
					Cell cellM13 = row.createCell(12);
					if (record1.getR13_ptc_amt() != null) {
						cellM13.setCellValue(record1.getR13_ptc_amt().doubleValue());
						cellM13.setCellStyle(numberStyle);
					} else {
						cellM13.setCellValue("");
						cellM13.setCellStyle(textStyle);
					}

// R13 Col N (Index 13) - Purchase Amt
					Cell cellN13 = row.createCell(13);
					if (record1.getR13_purchase_amt() != null) {
						cellN13.setCellValue(record1.getR13_purchase_amt().doubleValue());
						cellN13.setCellStyle(numberStyle);
					} else {
						cellN13.setCellValue("");
						cellN13.setCellStyle(textStyle);
					}

// R13 Col O (Index 14) - Other Amt
					Cell cellO13 = row.createCell(14);
					if (record1.getR13_other_amt() != null) {
						cellO13.setCellValue(record1.getR13_other_amt().doubleValue());
						cellO13.setCellStyle(numberStyle);
					} else {
						cellO13.setCellValue("");
						cellO13.setCellStyle(textStyle);
					}

// ==================== R14 Field Mappings ====================
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}

// R14 Col A (Index 0) - String
					Cell cellA14 = row.createCell(0);
					if (record.getR14_name_of_sfi() != null) {
						cellA14.setCellValue(record.getR14_name_of_sfi());
						cellA14.setCellStyle(textStyle);
					} else {
						cellA14.setCellValue("");
						cellA14.setCellStyle(textStyle);
					}

// R14 Col B (Index 1) - String
					Cell cellB14 = row.createCell(1);
					if (record.getR14_name_of_cp() != null) {
						cellB14.setCellValue(record.getR14_name_of_cp());
						cellB14.setCellStyle(textStyle);
					} else {
						cellB14.setCellValue("");
						cellB14.setCellStyle(textStyle);
					}

// R14 Col C (Index 2) - BigDecimal / Double
					Cell cellC14 = row.createCell(2);
					if (record.getR14_loans_amt() != null) {
						cellC14.setCellValue(record.getR14_loans_amt().doubleValue());
						cellC14.setCellStyle(numberStyle);
					} else {
						cellC14.setCellValue("");
						cellC14.setCellStyle(textStyle);
					}

// R14 Col D (Index 3) - BigDecimal / Double
					Cell cellD14 = row.createCell(3);
					if (record.getR14_deposit_amt() != null) {
						cellD14.setCellValue(record.getR14_deposit_amt().doubleValue());
						cellD14.setCellStyle(numberStyle);
					} else {
						cellD14.setCellValue("");
						cellD14.setCellStyle(textStyle);
					}

// R14 Col E (Index 4) - Short Term Amt
					Cell cellE14 = row.createCell(4);
					if (record1.getR14_short_term_amt() != null) {
						cellE14.setCellValue(record1.getR14_short_term_amt().doubleValue());
						cellE14.setCellStyle(numberStyle);
					} else {
						cellE14.setCellValue("");
						cellE14.setCellStyle(textStyle);
					}

// R14 Col F (Index 5) - Equity
					Cell cellF14 = row.createCell(5);
					if (record1.getR14_equity() != null) {
						cellF14.setCellValue(record1.getR14_equity().doubleValue());
						cellF14.setCellStyle(numberStyle);
					} else {
						cellF14.setCellValue("");
						cellF14.setCellStyle(textStyle);
					}

// R14 Col G (Index 6) - Bonds Amt
					Cell cellG14 = row.createCell(6);
					if (record1.getR14_bonds_amt() != null) {
						cellG14.setCellValue(record1.getR14_bonds_amt().doubleValue());
						cellG14.setCellStyle(numberStyle);
					} else {
						cellG14.setCellValue("");
						cellG14.setCellStyle(textStyle);
					}

// R14 Col H (Index 7) - CP Amt
					Cell cellH14 = row.createCell(7);
					if (record1.getR14_cp_amt() != null) {
						cellH14.setCellValue(record1.getR14_cp_amt().doubleValue());
						cellH14.setCellStyle(numberStyle);
					} else {
						cellH14.setCellValue("");
						cellH14.setCellStyle(textStyle);
					}

// R14 Col I (Index 8) - CD Amt
					Cell cellI14 = row.createCell(8);
					if (record1.getR14_cd_amt() != null) {
						cellI14.setCellValue(record1.getR14_cd_amt().doubleValue());
						cellI14.setCellStyle(numberStyle);
					} else {
						cellI14.setCellValue("");
						cellI14.setCellStyle(textStyle);
					}

// R14 Col J (Index 9) - Tier Amt
					Cell cellJ14 = row.createCell(9);
					if (record1.getR14_tier_amt() != null) {
						cellJ14.setCellValue(record1.getR14_tier_amt().doubleValue());
						cellJ14.setCellStyle(numberStyle);
					} else {
						cellJ14.setCellValue("");
						cellJ14.setCellStyle(textStyle);
					}

// R14 Col K (Index 10) - Unit Amt
					Cell cellK14 = row.createCell(10);
					if (record1.getR14_unit_amt() != null) {
						cellK14.setCellValue(record1.getR14_unit_amt().doubleValue());
						cellK14.setCellStyle(numberStyle);
					} else {
						cellK14.setCellValue("");
						cellK14.setCellStyle(textStyle);
					}

// R14 Col L (Index 11) - Venture Amt
					Cell cellL14 = row.createCell(11);
					if (record1.getR14_venture_amt() != null) {
						cellL14.setCellValue(record1.getR14_venture_amt().doubleValue());
						cellL14.setCellStyle(numberStyle);
					} else {
						cellL14.setCellValue("");
						cellL14.setCellStyle(textStyle);
					}

// R14 Col M (Index 12) - PTC Amt
					Cell cellM14 = row.createCell(12);
					if (record1.getR14_ptc_amt() != null) {
						cellM14.setCellValue(record1.getR14_ptc_amt().doubleValue());
						cellM14.setCellStyle(numberStyle);
					} else {
						cellM14.setCellValue("");
						cellM14.setCellStyle(textStyle);
					}

// R14 Col N (Index 13) - Purchase Amt
					Cell cellN14 = row.createCell(13);
					if (record1.getR14_purchase_amt() != null) {
						cellN14.setCellValue(record1.getR14_purchase_amt().doubleValue());
						cellN14.setCellStyle(numberStyle);
					} else {
						cellN14.setCellValue("");
						cellN14.setCellStyle(textStyle);
					}

// R14 Col O (Index 14) - Other Amt
					Cell cellO14 = row.createCell(14);
					if (record1.getR14_other_amt() != null) {
						cellO14.setCellValue(record1.getR14_other_amt().doubleValue());
						cellO14.setCellStyle(numberStyle);
					} else {
						cellO14.setCellValue("");
						cellO14.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "DBS10_FINCON_III_1C SUMMARY", null,
						"BRRS_DBS10_FINCON_III_1C_SUMMARYTABLE");
			}
			return out.toByteArray();
		}

	}

	public byte[] getExcelDBS10_FINCON_III_1CARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory.");

		if (("ARCHIVAL".equalsIgnoreCase(type) || "RESUB".equalsIgnoreCase(type)) && version != null) {

		}

		List<DBS10_FINCON_III_1C_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),
				version);

		List<DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity> dataList1 = getManualArchivalByDate(
				dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for DBS10_FINCON_III_1C new report. Returning empty result.");
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

			int startRow = 7;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					DBS10_FINCON_III_1C_Archival_Summary_Entity record = dataList.get(i);
					DBS10_FINCON_III_1C_Manual_Archival_Summary_Entity record1 = dataList1.get(i);

					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// R8 Col A (Index 0) - String
					Cell cellA = row.createCell(0);
					if (record.getR8_name_of_sfi() != null) {
						cellA.setCellValue(record.getR8_name_of_sfi());
						cellA.setCellStyle(textStyle);
					} else {
						cellA.setCellValue("");
						cellA.setCellStyle(textStyle);
					}

					// R8 Col B (Index 1) - String
					Cell cellB = row.createCell(1);
					if (record.getR8_name_of_cp() != null) {
						cellB.setCellValue(record.getR8_name_of_cp());
						cellB.setCellStyle(textStyle);
					} else {
						cellB.setCellValue("");
						cellB.setCellStyle(textStyle);
					}

					// R8 Col C (Index 2) - BigDecimal / Double
					Cell cellC = row.createCell(2);
					if (record.getR8_loans_amt() != null) {
						cellC.setCellValue(record.getR8_loans_amt().doubleValue());
						cellC.setCellStyle(numberStyle);
					} else {
						cellC.setCellValue("");
						cellC.setCellStyle(textStyle);
					}

					// R8 Col D (Index 3) - BigDecimal / Double
					Cell cellD = row.createCell(3);
					if (record.getR8_deposit_amt() != null) {
						cellD.setCellValue(record.getR8_deposit_amt().doubleValue());
						cellD.setCellStyle(numberStyle);
					} else {
						cellD.setCellValue("");
						cellD.setCellStyle(textStyle);
					}

					// R8 Col E (Index 4) - Short Term Amt
					Cell cellE = row.createCell(4);
					if (record1.getR8_short_term_amt() != null) {
						cellE.setCellValue(record1.getR8_short_term_amt().doubleValue());
						cellE.setCellStyle(numberStyle);
					} else {
						cellE.setCellValue("");
						cellE.setCellStyle(textStyle);
					}

					// R8 Col F (Index 5) - Equity
					Cell cellF = row.createCell(5);
					if (record1.getR8_equity() != null) {
						cellF.setCellValue(record1.getR8_equity().doubleValue());
						cellF.setCellStyle(numberStyle);
					} else {
						cellF.setCellValue("");
						cellF.setCellStyle(textStyle);
					}

					// R8 Col G (Index 6) - Bonds Amt
					Cell cellG = row.createCell(6);
					if (record1.getR8_bonds_amt() != null) {
						cellG.setCellValue(record1.getR8_bonds_amt().doubleValue());
						cellG.setCellStyle(numberStyle);
					} else {
						cellG.setCellValue("");
						cellG.setCellStyle(textStyle);
					}

					// R8 Col H (Index 7) - CP Amt
					Cell cellH = row.createCell(7);
					if (record1.getR8_cp_amt() != null) {
						cellH.setCellValue(record1.getR8_cp_amt().doubleValue());
						cellH.setCellStyle(numberStyle);
					} else {
						cellH.setCellValue("");
						cellH.setCellStyle(textStyle);
					}

					// R8 Col I (Index 8) - CD Amt
					Cell cellI = row.createCell(8);
					if (record1.getR8_cd_amt() != null) {
						cellI.setCellValue(record1.getR8_cd_amt().doubleValue());
						cellI.setCellStyle(numberStyle);
					} else {
						cellI.setCellValue("");
						cellI.setCellStyle(textStyle);
					}

					// R8 Col J (Index 9) - Tier Amt
					Cell cellJ = row.createCell(9);
					if (record1.getR8_tier_amt() != null) {
						cellJ.setCellValue(record1.getR8_tier_amt().doubleValue());
						cellJ.setCellStyle(numberStyle);
					} else {
						cellJ.setCellValue("");
						cellJ.setCellStyle(textStyle);
					}

					// R8 Col K (Index 10) - Unit Amt
					Cell cellK = row.createCell(10);
					if (record1.getR8_unit_amt() != null) {
						cellK.setCellValue(record1.getR8_unit_amt().doubleValue());
						cellK.setCellStyle(numberStyle);
					} else {
						cellK.setCellValue("");
						cellK.setCellStyle(textStyle);
					}

					// R8 Col L (Index 11) - Venture Amt
					Cell cellL = row.createCell(11);
					if (record1.getR8_venture_amt() != null) {
						cellL.setCellValue(record1.getR8_venture_amt().doubleValue());
						cellL.setCellStyle(numberStyle);
					} else {
						cellL.setCellValue("");
						cellL.setCellStyle(textStyle);
					}

					// R8 Col M (Index 12) - PTC Amt
					Cell cellM = row.createCell(12);
					if (record1.getR8_ptc_amt() != null) {
						cellM.setCellValue(record1.getR8_ptc_amt().doubleValue());
						cellM.setCellStyle(numberStyle);
					} else {
						cellM.setCellValue("");
						cellM.setCellStyle(textStyle);
					}

					// R8 Col N (Index 13) - Purchase Amt
					Cell cellN = row.createCell(13);
					if (record1.getR8_purchase_amt() != null) {
						cellN.setCellValue(record1.getR8_purchase_amt().doubleValue());
						cellN.setCellStyle(numberStyle);
					} else {
						cellN.setCellValue("");
						cellN.setCellStyle(textStyle);
					}

					// R8 Col O (Index 14) - Other Amt
					Cell cellO = row.createCell(14);
					if (record1.getR8_other_amt() != null) {
						cellO.setCellValue(record1.getR8_other_amt().doubleValue());
						cellO.setCellStyle(numberStyle);
					} else {
						cellO.setCellValue("");
						cellO.setCellStyle(textStyle);
					}

					row = sheet.getRow(8);
					// R9 Col A (Index 0) - String
					Cell cellA1 = row.createCell(0);
					if (record.getR9_name_of_sfi() != null) {
						cellA1.setCellValue(record.getR9_name_of_sfi());
						cellA1.setCellStyle(textStyle);
					} else {
						cellA1.setCellValue("");
						cellA1.setCellStyle(textStyle);
					}

					// R9 Col B (Index 1) - String
					Cell cellB1 = row.createCell(1);
					if (record.getR9_name_of_cp() != null) {
						cellB1.setCellValue(record.getR9_name_of_cp());
						cellB1.setCellStyle(textStyle);
					} else {
						cellB1.setCellValue("");
						cellB1.setCellStyle(textStyle);
					}

					// R9 Col C (Index 2) - BigDecimal / Double
					Cell cellC1 = row.createCell(2);
					if (record.getR9_loans_amt() != null) {
						cellC1.setCellValue(record.getR9_loans_amt().doubleValue());
						cellC1.setCellStyle(numberStyle);
					} else {
						cellC1.setCellValue("");
						cellC1.setCellStyle(textStyle);
					}

					// R9 Col D (Index 3) - BigDecimal / Double
					Cell cellD1 = row.createCell(3);
					if (record.getR9_deposit_amt() != null) {
						cellD1.setCellValue(record.getR9_deposit_amt().doubleValue());
						cellD1.setCellStyle(numberStyle);
					} else {
						cellD1.setCellValue("");
						cellD1.setCellStyle(textStyle);
					}

					// R9 Col E (Index 4) - Short Term Amt
					Cell cellE1 = row.createCell(4);
					if (record1.getR9_short_term_amt() != null) {
						cellE1.setCellValue(record1.getR9_short_term_amt().doubleValue());
						cellE1.setCellStyle(numberStyle);
					} else {
						cellE1.setCellValue("");
						cellE1.setCellStyle(textStyle);
					}

					// R9 Col F (Index 5) - Equity
					Cell cellF1 = row.createCell(5);
					if (record1.getR9_equity() != null) {
						cellF1.setCellValue(record1.getR9_equity().doubleValue());
						cellF1.setCellStyle(numberStyle);
					} else {
						cellF1.setCellValue("");
						cellF1.setCellStyle(textStyle);
					}

					// R9 Col G (Index 6) - Bonds Amt
					Cell cellG1 = row.createCell(6);
					if (record1.getR9_bonds_amt() != null) {
						cellG1.setCellValue(record1.getR9_bonds_amt().doubleValue());
						cellG1.setCellStyle(numberStyle);
					} else {
						cellG1.setCellValue("");
						cellG1.setCellStyle(textStyle);
					}

					// R9 Col H (Index 7) - CP Amt
					Cell cellH1 = row.createCell(7);
					if (record1.getR9_cp_amt() != null) {
						cellH1.setCellValue(record1.getR9_cp_amt().doubleValue());
						cellH1.setCellStyle(numberStyle);
					} else {
						cellH1.setCellValue("");
						cellH1.setCellStyle(textStyle);
					}

					// R9 Col I (Index 8) - CD Amt
					Cell cellI1 = row.createCell(8);
					if (record1.getR9_cd_amt() != null) {
						cellI1.setCellValue(record1.getR9_cd_amt().doubleValue());
						cellI1.setCellStyle(numberStyle);
					} else {
						cellI1.setCellValue("");
						cellI1.setCellStyle(textStyle);
					}

					// R9 Col J (Index 9) - Tier Amt
					Cell cellJ1 = row.createCell(9);
					if (record1.getR9_tier_amt() != null) {
						cellJ1.setCellValue(record1.getR9_tier_amt().doubleValue());
						cellJ1.setCellStyle(numberStyle);
					} else {
						cellJ1.setCellValue("");
						cellJ1.setCellStyle(textStyle);
					}

					// R9 Col K (Index 10) - Unit Amt
					Cell cellK1 = row.createCell(10);
					if (record1.getR9_unit_amt() != null) {
						cellK1.setCellValue(record1.getR9_unit_amt().doubleValue());
						cellK1.setCellStyle(numberStyle);
					} else {
						cellK1.setCellValue("");
						cellK1.setCellStyle(textStyle);
					}

					// R9 Col L (Index 11) - Venture Amt
					Cell cellL1 = row.createCell(11);
					if (record1.getR9_venture_amt() != null) {
						cellL1.setCellValue(record1.getR9_venture_amt().doubleValue());
						cellL1.setCellStyle(numberStyle);
					} else {
						cellL1.setCellValue("");
						cellL1.setCellStyle(textStyle);
					}

					// R9 Col M (Index 12) - PTC Amt
					Cell cellM1 = row.createCell(12);
					if (record1.getR9_ptc_amt() != null) {
						cellM1.setCellValue(record1.getR9_ptc_amt().doubleValue());
						cellM1.setCellStyle(numberStyle);
					} else {
						cellM1.setCellValue("");
						cellM1.setCellStyle(textStyle);
					}

					// R9 Col N (Index 13) - Purchase Amt
					Cell cellN1 = row.createCell(13);
					if (record1.getR9_purchase_amt() != null) {
						cellN1.setCellValue(record1.getR9_purchase_amt().doubleValue());
						cellN1.setCellStyle(numberStyle);
					} else {
						cellN1.setCellValue("");
						cellN1.setCellStyle(textStyle);
					}

					// R9 Col O (Index 14) - Other Amt
					Cell cellO1 = row.createCell(14);
					if (record1.getR9_other_amt() != null) {
						cellO1.setCellValue(record1.getR9_other_amt().doubleValue());
						cellO1.setCellStyle(numberStyle);
					} else {
						cellO1.setCellValue("");
						cellO1.setCellStyle(textStyle);
					}

// ==================== R10 Field Mappings ====================
					row = sheet.getRow(9);
					if (row == null) {
						row = sheet.createRow(9);
					}

// R10 Col A (Index 0) - String
					Cell cellA10 = row.createCell(0);
					if (record.getR10_name_of_sfi() != null) {
						cellA10.setCellValue(record.getR10_name_of_sfi());
						cellA10.setCellStyle(textStyle);
					} else {
						cellA10.setCellValue("");
						cellA10.setCellStyle(textStyle);
					}

// R10 Col B (Index 1) - String
					Cell cellB10 = row.createCell(1);
					if (record.getR10_name_of_cp() != null) {
						cellB10.setCellValue(record.getR10_name_of_cp());
						cellB10.setCellStyle(textStyle);
					} else {
						cellB10.setCellValue("");
						cellB10.setCellStyle(textStyle);
					}

// R10 Col C (Index 2) - BigDecimal / Double
					Cell cellC10 = row.createCell(2);
					if (record.getR10_loans_amt() != null) {
						cellC10.setCellValue(record.getR10_loans_amt().doubleValue());
						cellC10.setCellStyle(numberStyle);
					} else {
						cellC10.setCellValue("");
						cellC10.setCellStyle(textStyle);
					}

// R10 Col D (Index 3) - BigDecimal / Double
					Cell cellD10 = row.createCell(3);
					if (record.getR10_deposit_amt() != null) {
						cellD10.setCellValue(record.getR10_deposit_amt().doubleValue());
						cellD10.setCellStyle(numberStyle);
					} else {
						cellD10.setCellValue("");
						cellD10.setCellStyle(textStyle);
					}

// R10 Col E (Index 4) - Short Term Amt
					Cell cellE10 = row.createCell(4);
					if (record1.getR10_short_term_amt() != null) {
						cellE10.setCellValue(record1.getR10_short_term_amt().doubleValue());
						cellE10.setCellStyle(numberStyle);
					} else {
						cellE10.setCellValue("");
						cellE10.setCellStyle(textStyle);
					}

// R10 Col F (Index 5) - Equity
					Cell cellF10 = row.createCell(5);
					if (record1.getR10_equity() != null) {
						cellF10.setCellValue(record1.getR10_equity().doubleValue());
						cellF10.setCellStyle(numberStyle);
					} else {
						cellF10.setCellValue("");
						cellF10.setCellStyle(textStyle);
					}

// R10 Col G (Index 6) - Bonds Amt
					Cell cellG10 = row.createCell(6);
					if (record1.getR10_bonds_amt() != null) {
						cellG10.setCellValue(record1.getR10_bonds_amt().doubleValue());
						cellG10.setCellStyle(numberStyle);
					} else {
						cellG10.setCellValue("");
						cellG10.setCellStyle(textStyle);
					}

// R10 Col H (Index 7) - CP Amt
					Cell cellH10 = row.createCell(7);
					if (record1.getR10_cp_amt() != null) {
						cellH10.setCellValue(record1.getR10_cp_amt().doubleValue());
						cellH10.setCellStyle(numberStyle);
					} else {
						cellH10.setCellValue("");
						cellH10.setCellStyle(textStyle);
					}

// R10 Col I (Index 8) - CD Amt
					Cell cellI10 = row.createCell(8);
					if (record1.getR10_cd_amt() != null) {
						cellI10.setCellValue(record1.getR10_cd_amt().doubleValue());
						cellI10.setCellStyle(numberStyle);
					} else {
						cellI10.setCellValue("");
						cellI10.setCellStyle(textStyle);
					}

// R10 Col J (Index 9) - Tier Amt
					Cell cellJ10 = row.createCell(9);
					if (record1.getR10_tier_amt() != null) {
						cellJ10.setCellValue(record1.getR10_tier_amt().doubleValue());
						cellJ10.setCellStyle(numberStyle);
					} else {
						cellJ10.setCellValue("");
						cellJ10.setCellStyle(textStyle);
					}

// R10 Col K (Index 10) - Unit Amt
					Cell cellK10 = row.createCell(10);
					if (record1.getR10_unit_amt() != null) {
						cellK10.setCellValue(record1.getR10_unit_amt().doubleValue());
						cellK10.setCellStyle(numberStyle);
					} else {
						cellK10.setCellValue("");
						cellK10.setCellStyle(textStyle);
					}

// R10 Col L (Index 11) - Venture Amt
					Cell cellL10 = row.createCell(11);
					if (record1.getR10_venture_amt() != null) {
						cellL10.setCellValue(record1.getR10_venture_amt().doubleValue());
						cellL10.setCellStyle(numberStyle);
					} else {
						cellL10.setCellValue("");
						cellL10.setCellStyle(textStyle);
					}

// R10 Col M (Index 12) - PTC Amt
					Cell cellM10 = row.createCell(12);
					if (record1.getR10_ptc_amt() != null) {
						cellM10.setCellValue(record1.getR10_ptc_amt().doubleValue());
						cellM10.setCellStyle(numberStyle);
					} else {
						cellM10.setCellValue("");
						cellM10.setCellStyle(textStyle);
					}

// R10 Col N (Index 13) - Purchase Amt
					Cell cellN10 = row.createCell(13);
					if (record1.getR10_purchase_amt() != null) {
						cellN10.setCellValue(record1.getR10_purchase_amt().doubleValue());
						cellN10.setCellStyle(numberStyle);
					} else {
						cellN10.setCellValue("");
						cellN10.setCellStyle(textStyle);
					}

// R10 Col O (Index 14) - Other Amt
					Cell cellO10 = row.createCell(14);
					if (record1.getR10_other_amt() != null) {
						cellO10.setCellValue(record1.getR10_other_amt().doubleValue());
						cellO10.setCellStyle(numberStyle);
					} else {
						cellO10.setCellValue("");
						cellO10.setCellStyle(textStyle);
					}

// ==================== R11 Field Mappings ====================
					row = sheet.getRow(10);
					if (row == null) {
						row = sheet.createRow(10);
					}

// R11 Col A (Index 0) - String
					Cell cellA11 = row.createCell(0);
					if (record.getR11_name_of_sfi() != null) {
						cellA11.setCellValue(record.getR11_name_of_sfi());
						cellA11.setCellStyle(textStyle);
					} else {
						cellA11.setCellValue("");
						cellA11.setCellStyle(textStyle);
					}

// R11 Col B (Index 1) - String
					Cell cellB11 = row.createCell(1);
					if (record.getR11_name_of_cp() != null) {
						cellB11.setCellValue(record.getR11_name_of_cp());
						cellB11.setCellStyle(textStyle);
					} else {
						cellB11.setCellValue("");
						cellB11.setCellStyle(textStyle);
					}

// R11 Col C (Index 2) - BigDecimal / Double
					Cell cellC11 = row.createCell(2);
					if (record.getR11_loans_amt() != null) {
						cellC11.setCellValue(record.getR11_loans_amt().doubleValue());
						cellC11.setCellStyle(numberStyle);
					} else {
						cellC11.setCellValue("");
						cellC11.setCellStyle(textStyle);
					}

// R11 Col D (Index 3) - BigDecimal / Double
					Cell cellD11 = row.createCell(3);
					if (record.getR11_deposit_amt() != null) {
						cellD11.setCellValue(record.getR11_deposit_amt().doubleValue());
						cellD11.setCellStyle(numberStyle);
					} else {
						cellD11.setCellValue("");
						cellD11.setCellStyle(textStyle);
					}

// R11 Col E (Index 4) - Short Term Amt
					Cell cellE11 = row.createCell(4);
					if (record1.getR11_short_term_amt() != null) {
						cellE11.setCellValue(record1.getR11_short_term_amt().doubleValue());
						cellE11.setCellStyle(numberStyle);
					} else {
						cellE11.setCellValue("");
						cellE11.setCellStyle(textStyle);
					}

// R11 Col F (Index 5) - Equity
					Cell cellF11 = row.createCell(5);
					if (record1.getR11_equity() != null) {
						cellF11.setCellValue(record1.getR11_equity().doubleValue());
						cellF11.setCellStyle(numberStyle);
					} else {
						cellF11.setCellValue("");
						cellF11.setCellStyle(textStyle);
					}

// R11 Col G (Index 6) - Bonds Amt
					Cell cellG11 = row.createCell(6);
					if (record1.getR11_bonds_amt() != null) {
						cellG11.setCellValue(record1.getR11_bonds_amt().doubleValue());
						cellG11.setCellStyle(numberStyle);
					} else {
						cellG11.setCellValue("");
						cellG11.setCellStyle(textStyle);
					}

// R11 Col H (Index 7) - CP Amt
					Cell cellH11 = row.createCell(7);
					if (record1.getR11_cp_amt() != null) {
						cellH11.setCellValue(record1.getR11_cp_amt().doubleValue());
						cellH11.setCellStyle(numberStyle);
					} else {
						cellH11.setCellValue("");
						cellH11.setCellStyle(textStyle);
					}

// R11 Col I (Index 8) - CD Amt
					Cell cellI11 = row.createCell(8);
					if (record1.getR11_cd_amt() != null) {
						cellI11.setCellValue(record1.getR11_cd_amt().doubleValue());
						cellI11.setCellStyle(numberStyle);
					} else {
						cellI11.setCellValue("");
						cellI11.setCellStyle(textStyle);
					}

// R11 Col J (Index 9) - Tier Amt
					Cell cellJ11 = row.createCell(9);
					if (record1.getR11_tier_amt() != null) {
						cellJ11.setCellValue(record1.getR11_tier_amt().doubleValue());
						cellJ11.setCellStyle(numberStyle);
					} else {
						cellJ11.setCellValue("");
						cellJ11.setCellStyle(textStyle);
					}

// R11 Col K (Index 10) - Unit Amt
					Cell cellK11 = row.createCell(10);
					if (record1.getR11_unit_amt() != null) {
						cellK11.setCellValue(record1.getR11_unit_amt().doubleValue());
						cellK11.setCellStyle(numberStyle);
					} else {
						cellK11.setCellValue("");
						cellK11.setCellStyle(textStyle);
					}

// R11 Col L (Index 11) - Venture Amt
					Cell cellL11 = row.createCell(11);
					if (record1.getR11_venture_amt() != null) {
						cellL11.setCellValue(record1.getR11_venture_amt().doubleValue());
						cellL11.setCellStyle(numberStyle);
					} else {
						cellL11.setCellValue("");
						cellL11.setCellStyle(textStyle);
					}

// R11 Col M (Index 12) - PTC Amt
					Cell cellM11 = row.createCell(12);
					if (record1.getR11_ptc_amt() != null) {
						cellM11.setCellValue(record1.getR11_ptc_amt().doubleValue());
						cellM11.setCellStyle(numberStyle);
					} else {
						cellM11.setCellValue("");
						cellM11.setCellStyle(textStyle);
					}

// R11 Col N (Index 13) - Purchase Amt
					Cell cellN11 = row.createCell(13);
					if (record1.getR11_purchase_amt() != null) {
						cellN11.setCellValue(record1.getR11_purchase_amt().doubleValue());
						cellN11.setCellStyle(numberStyle);
					} else {
						cellN11.setCellValue("");
						cellN11.setCellStyle(textStyle);
					}

// R11 Col O (Index 14) - Other Amt
					Cell cellO11 = row.createCell(14);
					if (record1.getR11_other_amt() != null) {
						cellO11.setCellValue(record1.getR11_other_amt().doubleValue());
						cellO11.setCellStyle(numberStyle);
					} else {
						cellO11.setCellValue("");
						cellO11.setCellStyle(textStyle);
					}

// ==================== R12 Field Mappings ====================
					row = sheet.getRow(11);
					if (row == null) {
						row = sheet.createRow(11);
					}

// R12 Col A (Index 0) - String
					Cell cellA12 = row.createCell(0);
					if (record.getR12_name_of_sfi() != null) {
						cellA12.setCellValue(record.getR12_name_of_sfi());
						cellA12.setCellStyle(textStyle);
					} else {
						cellA12.setCellValue("");
						cellA12.setCellStyle(textStyle);
					}

// R12 Col B (Index 1) - String
					Cell cellB12 = row.createCell(1);
					if (record.getR12_name_of_cp() != null) {
						cellB12.setCellValue(record.getR12_name_of_cp());
						cellB12.setCellStyle(textStyle);
					} else {
						cellB12.setCellValue("");
						cellB12.setCellStyle(textStyle);
					}

// R12 Col C (Index 2) - BigDecimal / Double
					Cell cellC12 = row.createCell(2);
					if (record.getR12_loans_amt() != null) {
						cellC12.setCellValue(record.getR12_loans_amt().doubleValue());
						cellC12.setCellStyle(numberStyle);
					} else {
						cellC12.setCellValue("");
						cellC12.setCellStyle(textStyle);
					}

// R12 Col D (Index 3) - BigDecimal / Double
					Cell cellD12 = row.createCell(3);
					if (record.getR12_deposit_amt() != null) {
						cellD12.setCellValue(record.getR12_deposit_amt().doubleValue());
						cellD12.setCellStyle(numberStyle);
					} else {
						cellD12.setCellValue("");
						cellD12.setCellStyle(textStyle);
					}

// R12 Col E (Index 4) - Short Term Amt
					Cell cellE12 = row.createCell(4);
					if (record1.getR12_short_term_amt() != null) {
						cellE12.setCellValue(record1.getR12_short_term_amt().doubleValue());
						cellE12.setCellStyle(numberStyle);
					} else {
						cellE12.setCellValue("");
						cellE12.setCellStyle(textStyle);
					}

// R12 Col F (Index 5) - Equity
					Cell cellF12 = row.createCell(5);
					if (record1.getR12_equity() != null) {
						cellF12.setCellValue(record1.getR12_equity().doubleValue());
						cellF12.setCellStyle(numberStyle);
					} else {
						cellF12.setCellValue("");
						cellF12.setCellStyle(textStyle);
					}

// R12 Col G (Index 6) - Bonds Amt
					Cell cellG12 = row.createCell(6);
					if (record1.getR12_bonds_amt() != null) {
						cellG12.setCellValue(record1.getR12_bonds_amt().doubleValue());
						cellG12.setCellStyle(numberStyle);
					} else {
						cellG12.setCellValue("");
						cellG12.setCellStyle(textStyle);
					}

// R12 Col H (Index 7) - CP Amt
					Cell cellH12 = row.createCell(7);
					if (record1.getR12_cp_amt() != null) {
						cellH12.setCellValue(record1.getR12_cp_amt().doubleValue());
						cellH12.setCellStyle(numberStyle);
					} else {
						cellH12.setCellValue("");
						cellH12.setCellStyle(textStyle);
					}

// R12 Col I (Index 8) - CD Amt
					Cell cellI12 = row.createCell(8);
					if (record1.getR12_cd_amt() != null) {
						cellI12.setCellValue(record1.getR12_cd_amt().doubleValue());
						cellI12.setCellStyle(numberStyle);
					} else {
						cellI12.setCellValue("");
						cellI12.setCellStyle(textStyle);
					}

// R12 Col J (Index 9) - Tier Amt
					Cell cellJ12 = row.createCell(9);
					if (record1.getR12_tier_amt() != null) {
						cellJ12.setCellValue(record1.getR12_tier_amt().doubleValue());
						cellJ12.setCellStyle(numberStyle);
					} else {
						cellJ12.setCellValue("");
						cellJ12.setCellStyle(textStyle);
					}

// R12 Col K (Index 10) - Unit Amt
					Cell cellK12 = row.createCell(10);
					if (record1.getR12_unit_amt() != null) {
						cellK12.setCellValue(record1.getR12_unit_amt().doubleValue());
						cellK12.setCellStyle(numberStyle);
					} else {
						cellK12.setCellValue("");
						cellK12.setCellStyle(textStyle);
					}

// R12 Col L (Index 11) - Venture Amt
					Cell cellL12 = row.createCell(11);
					if (record1.getR12_venture_amt() != null) {
						cellL12.setCellValue(record1.getR12_venture_amt().doubleValue());
						cellL12.setCellStyle(numberStyle);
					} else {
						cellL12.setCellValue("");
						cellL12.setCellStyle(textStyle);
					}

// R12 Col M (Index 12) - PTC Amt
					Cell cellM12 = row.createCell(12);
					if (record1.getR12_ptc_amt() != null) {
						cellM12.setCellValue(record1.getR12_ptc_amt().doubleValue());
						cellM12.setCellStyle(numberStyle);
					} else {
						cellM12.setCellValue("");
						cellM12.setCellStyle(textStyle);
					}

// R12 Col N (Index 13) - Purchase Amt
					Cell cellN12 = row.createCell(13);
					if (record1.getR12_purchase_amt() != null) {
						cellN12.setCellValue(record1.getR12_purchase_amt().doubleValue());
						cellN12.setCellStyle(numberStyle);
					} else {
						cellN12.setCellValue("");
						cellN12.setCellStyle(textStyle);
					}

// R12 Col O (Index 14) - Other Amt
					Cell cellO12 = row.createCell(14);
					if (record1.getR12_other_amt() != null) {
						cellO12.setCellValue(record1.getR12_other_amt().doubleValue());
						cellO12.setCellStyle(numberStyle);
					} else {
						cellO12.setCellValue("");
						cellO12.setCellStyle(textStyle);
					}

// ==================== R13 Field Mappings ====================
					row = sheet.getRow(12);
					if (row == null) {
						row = sheet.createRow(12);
					}

// R13 Col A (Index 0) - String
					Cell cellA13 = row.createCell(0);
					if (record.getR13_name_of_sfi() != null) {
						cellA13.setCellValue(record.getR13_name_of_sfi());
						cellA13.setCellStyle(textStyle);
					} else {
						cellA13.setCellValue("");
						cellA13.setCellStyle(textStyle);
					}

// R13 Col B (Index 1) - String
					Cell cellB13 = row.createCell(1);
					if (record.getR13_name_of_cp() != null) {
						cellB13.setCellValue(record.getR13_name_of_cp());
						cellB13.setCellStyle(textStyle);
					} else {
						cellB13.setCellValue("");
						cellB13.setCellStyle(textStyle);
					}

// R13 Col C (Index 2) - BigDecimal / Double
					Cell cellC13 = row.createCell(2);
					if (record.getR13_loans_amt() != null) {
						cellC13.setCellValue(record.getR13_loans_amt().doubleValue());
						cellC13.setCellStyle(numberStyle);
					} else {
						cellC13.setCellValue("");
						cellC13.setCellStyle(textStyle);
					}

// R13 Col D (Index 3) - BigDecimal / Double
					Cell cellD13 = row.createCell(3);
					if (record.getR13_deposit_amt() != null) {
						cellD13.setCellValue(record.getR13_deposit_amt().doubleValue());
						cellD13.setCellStyle(numberStyle);
					} else {
						cellD13.setCellValue("");
						cellD13.setCellStyle(textStyle);
					}

// R13 Col E (Index 4) - Short Term Amt
					Cell cellE13 = row.createCell(4);
					if (record1.getR13_short_term_amt() != null) {
						cellE13.setCellValue(record1.getR13_short_term_amt().doubleValue());
						cellE13.setCellStyle(numberStyle);
					} else {
						cellE13.setCellValue("");
						cellE13.setCellStyle(textStyle);
					}

// R13 Col F (Index 5) - Equity
					Cell cellF13 = row.createCell(5);
					if (record1.getR13_equity() != null) {
						cellF13.setCellValue(record1.getR13_equity().doubleValue());
						cellF13.setCellStyle(numberStyle);
					} else {
						cellF13.setCellValue("");
						cellF13.setCellStyle(textStyle);
					}

// R13 Col G (Index 6) - Bonds Amt
					Cell cellG13 = row.createCell(6);
					if (record1.getR13_bonds_amt() != null) {
						cellG13.setCellValue(record1.getR13_bonds_amt().doubleValue());
						cellG13.setCellStyle(numberStyle);
					} else {
						cellG13.setCellValue("");
						cellG13.setCellStyle(textStyle);
					}

// R13 Col H (Index 7) - CP Amt
					Cell cellH13 = row.createCell(7);
					if (record1.getR13_cp_amt() != null) {
						cellH13.setCellValue(record1.getR13_cp_amt().doubleValue());
						cellH13.setCellStyle(numberStyle);
					} else {
						cellH13.setCellValue("");
						cellH13.setCellStyle(textStyle);
					}

// R13 Col I (Index 8) - CD Amt
					Cell cellI13 = row.createCell(8);
					if (record1.getR13_cd_amt() != null) {
						cellI13.setCellValue(record1.getR13_cd_amt().doubleValue());
						cellI13.setCellStyle(numberStyle);
					} else {
						cellI13.setCellValue("");
						cellI13.setCellStyle(textStyle);
					}

// R13 Col J (Index 9) - Tier Amt
					Cell cellJ13 = row.createCell(9);
					if (record1.getR13_tier_amt() != null) {
						cellJ13.setCellValue(record1.getR13_tier_amt().doubleValue());
						cellJ13.setCellStyle(numberStyle);
					} else {
						cellJ13.setCellValue("");
						cellJ13.setCellStyle(textStyle);
					}

// R13 Col K (Index 10) - Unit Amt
					Cell cellK13 = row.createCell(10);
					if (record1.getR13_unit_amt() != null) {
						cellK13.setCellValue(record1.getR13_unit_amt().doubleValue());
						cellK13.setCellStyle(numberStyle);
					} else {
						cellK13.setCellValue("");
						cellK13.setCellStyle(textStyle);
					}

// R13 Col L (Index 11) - Venture Amt
					Cell cellL13 = row.createCell(11);
					if (record1.getR13_venture_amt() != null) {
						cellL13.setCellValue(record1.getR13_venture_amt().doubleValue());
						cellL13.setCellStyle(numberStyle);
					} else {
						cellL13.setCellValue("");
						cellL13.setCellStyle(textStyle);
					}

// R13 Col M (Index 12) - PTC Amt
					Cell cellM13 = row.createCell(12);
					if (record1.getR13_ptc_amt() != null) {
						cellM13.setCellValue(record1.getR13_ptc_amt().doubleValue());
						cellM13.setCellStyle(numberStyle);
					} else {
						cellM13.setCellValue("");
						cellM13.setCellStyle(textStyle);
					}

// R13 Col N (Index 13) - Purchase Amt
					Cell cellN13 = row.createCell(13);
					if (record1.getR13_purchase_amt() != null) {
						cellN13.setCellValue(record1.getR13_purchase_amt().doubleValue());
						cellN13.setCellStyle(numberStyle);
					} else {
						cellN13.setCellValue("");
						cellN13.setCellStyle(textStyle);
					}

// R13 Col O (Index 14) - Other Amt
					Cell cellO13 = row.createCell(14);
					if (record1.getR13_other_amt() != null) {
						cellO13.setCellValue(record1.getR13_other_amt().doubleValue());
						cellO13.setCellStyle(numberStyle);
					} else {
						cellO13.setCellValue("");
						cellO13.setCellStyle(textStyle);
					}

// ==================== R14 Field Mappings ====================
					row = sheet.getRow(13);
					if (row == null) {
						row = sheet.createRow(13);
					}

// R14 Col A (Index 0) - String
					Cell cellA14 = row.createCell(0);
					if (record.getR14_name_of_sfi() != null) {
						cellA14.setCellValue(record.getR14_name_of_sfi());
						cellA14.setCellStyle(textStyle);
					} else {
						cellA14.setCellValue("");
						cellA14.setCellStyle(textStyle);
					}

// R14 Col B (Index 1) - String
					Cell cellB14 = row.createCell(1);
					if (record.getR14_name_of_cp() != null) {
						cellB14.setCellValue(record.getR14_name_of_cp());
						cellB14.setCellStyle(textStyle);
					} else {
						cellB14.setCellValue("");
						cellB14.setCellStyle(textStyle);
					}

// R14 Col C (Index 2) - BigDecimal / Double
					Cell cellC14 = row.createCell(2);
					if (record.getR14_loans_amt() != null) {
						cellC14.setCellValue(record.getR14_loans_amt().doubleValue());
						cellC14.setCellStyle(numberStyle);
					} else {
						cellC14.setCellValue("");
						cellC14.setCellStyle(textStyle);
					}

// R14 Col D (Index 3) - BigDecimal / Double
					Cell cellD14 = row.createCell(3);
					if (record.getR14_deposit_amt() != null) {
						cellD14.setCellValue(record.getR14_deposit_amt().doubleValue());
						cellD14.setCellStyle(numberStyle);
					} else {
						cellD14.setCellValue("");
						cellD14.setCellStyle(textStyle);
					}

// R14 Col E (Index 4) - Short Term Amt
					Cell cellE14 = row.createCell(4);
					if (record1.getR14_short_term_amt() != null) {
						cellE14.setCellValue(record1.getR14_short_term_amt().doubleValue());
						cellE14.setCellStyle(numberStyle);
					} else {
						cellE14.setCellValue("");
						cellE14.setCellStyle(textStyle);
					}

// R14 Col F (Index 5) - Equity
					Cell cellF14 = row.createCell(5);
					if (record1.getR14_equity() != null) {
						cellF14.setCellValue(record1.getR14_equity().doubleValue());
						cellF14.setCellStyle(numberStyle);
					} else {
						cellF14.setCellValue("");
						cellF14.setCellStyle(textStyle);
					}

// R14 Col G (Index 6) - Bonds Amt
					Cell cellG14 = row.createCell(6);
					if (record1.getR14_bonds_amt() != null) {
						cellG14.setCellValue(record1.getR14_bonds_amt().doubleValue());
						cellG14.setCellStyle(numberStyle);
					} else {
						cellG14.setCellValue("");
						cellG14.setCellStyle(textStyle);
					}

// R14 Col H (Index 7) - CP Amt
					Cell cellH14 = row.createCell(7);
					if (record1.getR14_cp_amt() != null) {
						cellH14.setCellValue(record1.getR14_cp_amt().doubleValue());
						cellH14.setCellStyle(numberStyle);
					} else {
						cellH14.setCellValue("");
						cellH14.setCellStyle(textStyle);
					}

// R14 Col I (Index 8) - CD Amt
					Cell cellI14 = row.createCell(8);
					if (record1.getR14_cd_amt() != null) {
						cellI14.setCellValue(record1.getR14_cd_amt().doubleValue());
						cellI14.setCellStyle(numberStyle);
					} else {
						cellI14.setCellValue("");
						cellI14.setCellStyle(textStyle);
					}

// R14 Col J (Index 9) - Tier Amt
					Cell cellJ14 = row.createCell(9);
					if (record1.getR14_tier_amt() != null) {
						cellJ14.setCellValue(record1.getR14_tier_amt().doubleValue());
						cellJ14.setCellStyle(numberStyle);
					} else {
						cellJ14.setCellValue("");
						cellJ14.setCellStyle(textStyle);
					}

// R14 Col K (Index 10) - Unit Amt
					Cell cellK14 = row.createCell(10);
					if (record1.getR14_unit_amt() != null) {
						cellK14.setCellValue(record1.getR14_unit_amt().doubleValue());
						cellK14.setCellStyle(numberStyle);
					} else {
						cellK14.setCellValue("");
						cellK14.setCellStyle(textStyle);
					}

// R14 Col L (Index 11) - Venture Amt
					Cell cellL14 = row.createCell(11);
					if (record1.getR14_venture_amt() != null) {
						cellL14.setCellValue(record1.getR14_venture_amt().doubleValue());
						cellL14.setCellStyle(numberStyle);
					} else {
						cellL14.setCellValue("");
						cellL14.setCellStyle(textStyle);
					}

// R14 Col M (Index 12) - PTC Amt
					Cell cellM14 = row.createCell(12);
					if (record1.getR14_ptc_amt() != null) {
						cellM14.setCellValue(record1.getR14_ptc_amt().doubleValue());
						cellM14.setCellStyle(numberStyle);
					} else {
						cellM14.setCellValue("");
						cellM14.setCellStyle(textStyle);
					}

// R14 Col N (Index 13) - Purchase Amt
					Cell cellN14 = row.createCell(13);
					if (record1.getR14_purchase_amt() != null) {
						cellN14.setCellValue(record1.getR14_purchase_amt().doubleValue());
						cellN14.setCellStyle(numberStyle);
					} else {
						cellN14.setCellValue("");
						cellN14.setCellStyle(textStyle);
					}

// R14 Col O (Index 14) - Other Amt
					Cell cellO14 = row.createCell(14);
					if (record1.getR14_other_amt() != null) {
						cellO14.setCellValue(record1.getR14_other_amt().doubleValue());
						cellO14.setCellStyle(numberStyle);
					} else {
						cellO14.setCellValue("");
						cellO14.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "DBS10_FINCON_III_1C ARCHIVAL SUMMARY", null,
						"BRRS_DBS10_FINCON_III_1C_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}

	}

	// Resubmission
	public List<Object[]> getDBS10_FINCON_III_1CResub() {
		List<Object[]> resubList = new ArrayList<>();

		try {

			List<DBS10_FINCON_III_1C_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();

			if (repoData != null && !repoData.isEmpty()) {
				for (DBS10_FINCON_III_1C_Archival_Summary_Entity entity : repoData) {
					Object[] row = new Object[] { entity.getReport_date(), entity.getReport_version(),
							entity.getREPORT_RESUBDATE() };
					resubList.add(row);
				}

				System.out.println("Fetched " + resubList.size() + " Resub records");
				DBS10_FINCON_III_1C_Archival_Summary_Entity first = repoData.get(0);
				System.out.println("Latest Resub version: " + first.getReport_version());
			} else {
				System.out.println("No Resub data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching  DBS10_FINCON_III_1C  Resub data: " + e.getMessage());
			e.printStackTrace();
		}

		return resubList;
	}

}