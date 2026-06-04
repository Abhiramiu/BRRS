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
import java.text.ParseException;
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
import org.hibernate.Session;
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

public class BRRS_MDISB4_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB4_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;
	
	// ENTITY MANAGER (Acts like Repository)
		@PersistenceContext
		private EntityManager entityManager;

	
		
	
	// Fetch data by report date
		public List<MDISB4_Summary_Entity> getDataByDate(Date reportDate) {

			String sql = "SELECT * FROM BRRS_MDISB4_SUMMARYTABLE WHERE REPORT_DATE = ?";

			return jdbcTemplate.query(sql, new Object[] { reportDate }, new MDISB4RowMapper());
		}

		// GET REPORT_DATE + REPORT_VERSION

		public List<Object[]> getMDISB4Archival1() {

			String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY "
					+ "ORDER BY REPORT_VERSION";

			return jdbcTemplate.query(sql,
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
		}
	
		//GET ARCHIVAL FULL DATA BY DATE + VERSION

		public List<MDISB4_Archival_Summary_Entity> getdatabydateListarchival(Date REPORT_DATE,
				BigDecimal REPORT_VERSION) {

			String sql = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
					+ "AND REPORT_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new MDISB4ArchivalRowMapper());
		}
	//GET ALL WITH VERSION

		public List<MDISB4_Archival_Summary_Entity> getdatabydateListWithVersion() {

			String sql = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
					+ "ORDER BY REPORT_VERSION ASC";

			return jdbcTemplate.query(sql, new MDISB4ArchivalRowMapper());
		}
	
	
		//GET MAX VERSION BY DATE

		public BigDecimal findMaxVersion(Date REPORT_DATE) {

			String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MDISB4_ARCHIVALTABLE_SUMMARY "
					+ "WHERE REPORT_DATE = ?";

			return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
		}

	// 1. BY DATE + LABEL + CRITERIA

		public List<MDISB4_Detail_Entity> findByDetailReportDateAndLabelAndCriteria(Date reportDate, String reportLabel,
				String reportAddlCriteria1) {

			String sql = "SELECT * FROM BRRS_MDISB4_DETAILTABLE "
					+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

			return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
					new MDISB4DetaillRowMapper());
		}

	// 2. GET ALL (BY DATE - simple)

		public List<MDISB4_Detail_Entity> getDetaildatabydateList(Date reportdate) {

			String sql = "SELECT * FROM BRRS_MDISB4_DETAILTABLE WHERE REPORT_DATE = ?";

			return jdbcTemplate.query(sql, new Object[] { reportdate }, new MDISB4DetaillRowMapper());
		}

	// 3. PAGINATION

		public List<MDISB4_Detail_Entity> getDetaildatabydateList(Date reportdate, int offset, int limit) {

			String sql = "SELECT * FROM BRRS_MDISB4_DETAILTABLE "
					+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

			return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new MDISB4DetaillRowMapper());
		}

	// 4. COUNT

		public int getDetaildatacount(Date reportdate) {

			String sql = "SELECT COUNT(*) FROM BRRS_MDISB4_DETAILTABLE WHERE REPORT_DATE = ?";

			return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
		}

	// 5. BY LABEL + CRITERIA

		public List<MDISB4_Detail_Entity> GetDetailDataByRowIdAndColumnId(String reportLabel,
				String reportAddlCriteria1, Date reportdate) {

			String sql = "SELECT * FROM BRRS_MDISB4_DETAILTABLE "
					+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

			return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
					new MDISB4DetaillRowMapper());
		}
	// 6. BY ACCOUNT NUMBER

		public MDISB4_Detail_Entity findByAcctnumber(String acctNumber) {

			String sql = "SELECT * FROM BRRS_MDISB4_DETAILTABLE WHERE ACCT_NUMBER = ?";

			return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new MDISB4DetaillRowMapper());
		}

	// 1. GET BY DATE + VERSION

		public List<MDISB4_Archival_Detail_Entity> getArchivalDetaildatabydateList(Date reportdate,
				String dataEntryVersion) {

			String sql = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_DETAIL "
					+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { reportdate, dataEntryVersion },
					new MDISB4ArchivalDetaillRowMapper());
		}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

		public List<MDISB4_Archival_Detail_Entity> GetArchivalDataByRowIdAndColumnId(String reportLabel,
				String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

			String sql = "SELECT * FROM BRRS_MDISB4_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_LABEL = ? "
					+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
					new MDISB4ArchivalDetaillRowMapper());
		}
	
		// ROW MAPPER

		class MDISB4RowMapper implements RowMapper<MDISB4_Summary_Entity> {

			@Override
			public MDISB4_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB4_Summary_Entity obj = new MDISB4_Summary_Entity();

				// R6
				obj.setR6_EXCLUSIONS(rs.getString("R6_EXCLUSIONS"));
				obj.setR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR6_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R6_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR6_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R6_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR6_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR6_TOTAL_AMOUNT(rs.getBigDecimal("R6_TOTAL_AMOUNT"));

				// R7
				obj.setR7_EXCLUSIONS(rs.getString("R7_EXCLUSIONS"));
				obj.setR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR7_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R7_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR7_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R7_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR7_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR7_TOTAL_AMOUNT(rs.getBigDecimal("R7_TOTAL_AMOUNT"));

				// R8
				obj.setR8_EXCLUSIONS(rs.getString("R8_EXCLUSIONS"));
				obj.setR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR8_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R8_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR8_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R8_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR8_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR8_TOTAL_AMOUNT(rs.getBigDecimal("R8_TOTAL_AMOUNT"));

				// R9
				obj.setR9_EXCLUSIONS(rs.getString("R9_EXCLUSIONS"));
				obj.setR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR9_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R9_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR9_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R9_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR9_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR9_TOTAL_AMOUNT(rs.getBigDecimal("R9_TOTAL_AMOUNT"));

				// R10
				obj.setR10_EXCLUSIONS(rs.getString("R10_EXCLUSIONS"));
				obj.setR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR10_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R10_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR10_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R10_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR10_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR10_TOTAL_AMOUNT(rs.getBigDecimal("R10_TOTAL_AMOUNT"));

				// R11
				obj.setR11_EXCLUSIONS(rs.getString("R11_EXCLUSIONS"));
				obj.setR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR11_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R11_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR11_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R11_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR11_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR11_TOTAL_AMOUNT(rs.getBigDecimal("R11_TOTAL_AMOUNT"));

				// R12
				obj.setR12_EXCLUSIONS(rs.getString("R12_EXCLUSIONS"));
				obj.setR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR12_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R12_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR12_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R12_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR12_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR12_TOTAL_AMOUNT(rs.getBigDecimal("R12_TOTAL_AMOUNT"));
	
	
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
	
		public static class MDISB4_Summary_Entity {

			
		    private String R6_EXCLUSIONS;
		    private BigDecimal R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R6_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R6_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R6_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R6_TOTAL_AMOUNT;

		    
		    private String R7_EXCLUSIONS;
		    private BigDecimal R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R7_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R7_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R7_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R7_TOTAL_AMOUNT;

		   
		    private String R8_EXCLUSIONS;
		    private BigDecimal R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R8_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R8_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R8_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R8_TOTAL_AMOUNT;

		   
		    private String R9_EXCLUSIONS;
		    private BigDecimal R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R9_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R9_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R9_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R9_TOTAL_AMOUNT;

		   
		    private String R10_EXCLUSIONS;
		    private BigDecimal R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R10_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R10_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R10_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R10_TOTAL_AMOUNT;

		    
		    private String R11_EXCLUSIONS;
		    private BigDecimal R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R11_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R11_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R11_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R11_TOTAL_AMOUNT;

		   
		    private String R12_EXCLUSIONS;
		    private BigDecimal R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R12_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R12_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R12_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R12_TOTAL_AMOUNT;
		    
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
	
			
			public String getR6_EXCLUSIONS() {
				return R6_EXCLUSIONS;
			}
			public void setR6_EXCLUSIONS(String r6_EXCLUSIONS) {
				R6_EXCLUSIONS = r6_EXCLUSIONS;
			}
			public BigDecimal getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R6_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR6_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r6_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R6_DEPOSITS_EXCLU_FCA_AMOUNT = r6_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R6_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR6_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r6_FOREIGN_CURR_DEPOS_AMOUNT) {
				R6_FOREIGN_CURR_DEPOS_AMOUNT = r6_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR6_TOTAL_NO_OF_ACCOUNTS() {
				return R6_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR6_TOTAL_NO_OF_ACCOUNTS(BigDecimal r6_TOTAL_NO_OF_ACCOUNTS) {
				R6_TOTAL_NO_OF_ACCOUNTS = r6_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_TOTAL_AMOUNT() {
				return R6_TOTAL_AMOUNT;
			}
			public void setR6_TOTAL_AMOUNT(BigDecimal r6_TOTAL_AMOUNT) {
				R6_TOTAL_AMOUNT = r6_TOTAL_AMOUNT;
			}
			public String getR7_EXCLUSIONS() {
				return R7_EXCLUSIONS;
			}
			public void setR7_EXCLUSIONS(String r7_EXCLUSIONS) {
				R7_EXCLUSIONS = r7_EXCLUSIONS;
			}
			public BigDecimal getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R7_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR7_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r7_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R7_DEPOSITS_EXCLU_FCA_AMOUNT = r7_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R7_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR7_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r7_FOREIGN_CURR_DEPOS_AMOUNT) {
				R7_FOREIGN_CURR_DEPOS_AMOUNT = r7_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR7_TOTAL_NO_OF_ACCOUNTS() {
				return R7_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR7_TOTAL_NO_OF_ACCOUNTS(BigDecimal r7_TOTAL_NO_OF_ACCOUNTS) {
				R7_TOTAL_NO_OF_ACCOUNTS = r7_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_TOTAL_AMOUNT() {
				return R7_TOTAL_AMOUNT;
			}
			public void setR7_TOTAL_AMOUNT(BigDecimal r7_TOTAL_AMOUNT) {
				R7_TOTAL_AMOUNT = r7_TOTAL_AMOUNT;
			}
			public String getR8_EXCLUSIONS() {
				return R8_EXCLUSIONS;
			}
			public void setR8_EXCLUSIONS(String r8_EXCLUSIONS) {
				R8_EXCLUSIONS = r8_EXCLUSIONS;
			}
			public BigDecimal getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R8_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR8_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r8_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R8_DEPOSITS_EXCLU_FCA_AMOUNT = r8_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R8_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR8_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r8_FOREIGN_CURR_DEPOS_AMOUNT) {
				R8_FOREIGN_CURR_DEPOS_AMOUNT = r8_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR8_TOTAL_NO_OF_ACCOUNTS() {
				return R8_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR8_TOTAL_NO_OF_ACCOUNTS(BigDecimal r8_TOTAL_NO_OF_ACCOUNTS) {
				R8_TOTAL_NO_OF_ACCOUNTS = r8_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_TOTAL_AMOUNT() {
				return R8_TOTAL_AMOUNT;
			}
			public void setR8_TOTAL_AMOUNT(BigDecimal r8_TOTAL_AMOUNT) {
				R8_TOTAL_AMOUNT = r8_TOTAL_AMOUNT;
			}
			public String getR9_EXCLUSIONS() {
				return R9_EXCLUSIONS;
			}
			public void setR9_EXCLUSIONS(String r9_EXCLUSIONS) {
				R9_EXCLUSIONS = r9_EXCLUSIONS;
			}
			public BigDecimal getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R9_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR9_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r9_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R9_DEPOSITS_EXCLU_FCA_AMOUNT = r9_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R9_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR9_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r9_FOREIGN_CURR_DEPOS_AMOUNT) {
				R9_FOREIGN_CURR_DEPOS_AMOUNT = r9_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR9_TOTAL_NO_OF_ACCOUNTS() {
				return R9_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR9_TOTAL_NO_OF_ACCOUNTS(BigDecimal r9_TOTAL_NO_OF_ACCOUNTS) {
				R9_TOTAL_NO_OF_ACCOUNTS = r9_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_TOTAL_AMOUNT() {
				return R9_TOTAL_AMOUNT;
			}
			public void setR9_TOTAL_AMOUNT(BigDecimal r9_TOTAL_AMOUNT) {
				R9_TOTAL_AMOUNT = r9_TOTAL_AMOUNT;
			}
			public String getR10_EXCLUSIONS() {
				return R10_EXCLUSIONS;
			}
			public void setR10_EXCLUSIONS(String r10_EXCLUSIONS) {
				R10_EXCLUSIONS = r10_EXCLUSIONS;
			}
			public BigDecimal getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R10_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR10_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r10_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R10_DEPOSITS_EXCLU_FCA_AMOUNT = r10_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R10_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR10_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r10_FOREIGN_CURR_DEPOS_AMOUNT) {
				R10_FOREIGN_CURR_DEPOS_AMOUNT = r10_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR10_TOTAL_NO_OF_ACCOUNTS() {
				return R10_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR10_TOTAL_NO_OF_ACCOUNTS(BigDecimal r10_TOTAL_NO_OF_ACCOUNTS) {
				R10_TOTAL_NO_OF_ACCOUNTS = r10_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_TOTAL_AMOUNT() {
				return R10_TOTAL_AMOUNT;
			}
			public void setR10_TOTAL_AMOUNT(BigDecimal r10_TOTAL_AMOUNT) {
				R10_TOTAL_AMOUNT = r10_TOTAL_AMOUNT;
			}
			public String getR11_EXCLUSIONS() {
				return R11_EXCLUSIONS;
			}
			public void setR11_EXCLUSIONS(String r11_EXCLUSIONS) {
				R11_EXCLUSIONS = r11_EXCLUSIONS;
			}
			public BigDecimal getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R11_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR11_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r11_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R11_DEPOSITS_EXCLU_FCA_AMOUNT = r11_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R11_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR11_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r11_FOREIGN_CURR_DEPOS_AMOUNT) {
				R11_FOREIGN_CURR_DEPOS_AMOUNT = r11_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR11_TOTAL_NO_OF_ACCOUNTS() {
				return R11_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR11_TOTAL_NO_OF_ACCOUNTS(BigDecimal r11_TOTAL_NO_OF_ACCOUNTS) {
				R11_TOTAL_NO_OF_ACCOUNTS = r11_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_TOTAL_AMOUNT() {
				return R11_TOTAL_AMOUNT;
			}
			public void setR11_TOTAL_AMOUNT(BigDecimal r11_TOTAL_AMOUNT) {
				R11_TOTAL_AMOUNT = r11_TOTAL_AMOUNT;
			}
			public String getR12_EXCLUSIONS() {
				return R12_EXCLUSIONS;
			}
			public void setR12_EXCLUSIONS(String r12_EXCLUSIONS) {
				R12_EXCLUSIONS = r12_EXCLUSIONS;
			}
			public BigDecimal getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R12_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR12_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r12_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R12_DEPOSITS_EXCLU_FCA_AMOUNT = r12_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R12_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR12_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r12_FOREIGN_CURR_DEPOS_AMOUNT) {
				R12_FOREIGN_CURR_DEPOS_AMOUNT = r12_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR12_TOTAL_NO_OF_ACCOUNTS() {
				return R12_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR12_TOTAL_NO_OF_ACCOUNTS(BigDecimal r12_TOTAL_NO_OF_ACCOUNTS) {
				R12_TOTAL_NO_OF_ACCOUNTS = r12_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_TOTAL_AMOUNT() {
				return R12_TOTAL_AMOUNT;
			}
			public void setR12_TOTAL_AMOUNT(BigDecimal r12_TOTAL_AMOUNT) {
				R12_TOTAL_AMOUNT = r12_TOTAL_AMOUNT;
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

			
			
		// ROW MAPPER ARCHIVAL

		class MDISB4ArchivalRowMapper implements RowMapper<MDISB4_Archival_Summary_Entity> {

			@Override
			public MDISB4_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB4_Archival_Summary_Entity obj = new MDISB4_Archival_Summary_Entity();

				// R6
				obj.setR6_EXCLUSIONS(rs.getString("R6_EXCLUSIONS"));
				obj.setR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR6_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R6_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR6_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R6_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR6_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R6_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR6_TOTAL_AMOUNT(rs.getBigDecimal("R6_TOTAL_AMOUNT"));

				// R7
				obj.setR7_EXCLUSIONS(rs.getString("R7_EXCLUSIONS"));
				obj.setR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR7_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R7_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR7_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R7_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR7_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R7_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR7_TOTAL_AMOUNT(rs.getBigDecimal("R7_TOTAL_AMOUNT"));

				// R8
				obj.setR8_EXCLUSIONS(rs.getString("R8_EXCLUSIONS"));
				obj.setR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR8_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R8_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR8_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R8_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR8_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R8_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR8_TOTAL_AMOUNT(rs.getBigDecimal("R8_TOTAL_AMOUNT"));

				// R9
				obj.setR9_EXCLUSIONS(rs.getString("R9_EXCLUSIONS"));
				obj.setR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR9_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R9_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR9_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R9_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR9_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R9_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR9_TOTAL_AMOUNT(rs.getBigDecimal("R9_TOTAL_AMOUNT"));

				// R10
				obj.setR10_EXCLUSIONS(rs.getString("R10_EXCLUSIONS"));
				obj.setR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR10_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R10_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR10_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R10_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR10_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R10_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR10_TOTAL_AMOUNT(rs.getBigDecimal("R10_TOTAL_AMOUNT"));

				// R11
				obj.setR11_EXCLUSIONS(rs.getString("R11_EXCLUSIONS"));
				obj.setR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR11_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R11_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR11_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R11_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR11_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R11_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR11_TOTAL_AMOUNT(rs.getBigDecimal("R11_TOTAL_AMOUNT"));

				// R12
				obj.setR12_EXCLUSIONS(rs.getString("R12_EXCLUSIONS"));
				obj.setR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS"));
				obj.setR12_DEPOSITS_EXCLU_FCA_AMOUNT(rs.getBigDecimal("R12_DEPOSITS_EXCLU_FCA_AMOUNT"));
				obj.setR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS"));
				obj.setR12_FOREIGN_CURR_DEPOS_AMOUNT(rs.getBigDecimal("R12_FOREIGN_CURR_DEPOS_AMOUNT"));
				obj.setR12_TOTAL_NO_OF_ACCOUNTS(rs.getBigDecimal("R12_TOTAL_NO_OF_ACCOUNTS"));
				obj.setR12_TOTAL_AMOUNT(rs.getBigDecimal("R12_TOTAL_AMOUNT"));
	
	
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
	
		public static class MDISB4_Archival_Summary_Entity {

			
		    private String R6_EXCLUSIONS;
		    private BigDecimal R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R6_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R6_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R6_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R6_TOTAL_AMOUNT;

		    
		    private String R7_EXCLUSIONS;
		    private BigDecimal R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R7_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R7_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R7_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R7_TOTAL_AMOUNT;

		   
		    private String R8_EXCLUSIONS;
		    private BigDecimal R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R8_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R8_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R8_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R8_TOTAL_AMOUNT;

		   
		    private String R9_EXCLUSIONS;
		    private BigDecimal R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R9_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R9_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R9_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R9_TOTAL_AMOUNT;

		   
		    private String R10_EXCLUSIONS;
		    private BigDecimal R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R10_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R10_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R10_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R10_TOTAL_AMOUNT;

		    
		    private String R11_EXCLUSIONS;
		    private BigDecimal R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R11_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R11_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R11_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R11_TOTAL_AMOUNT;

		   
		    private String R12_EXCLUSIONS;
		    private BigDecimal R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
		    private BigDecimal R12_DEPOSITS_EXCLU_FCA_AMOUNT;
		    private BigDecimal R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
		    private BigDecimal R12_FOREIGN_CURR_DEPOS_AMOUNT;
		    private BigDecimal R12_TOTAL_NO_OF_ACCOUNTS;
		    private BigDecimal R12_TOTAL_AMOUNT;
		    
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
	
			private Date REPORT_RESUBDATE;
			
			public String getR6_EXCLUSIONS() {
				return R6_EXCLUSIONS;
			}
			public void setR6_EXCLUSIONS(String r6_EXCLUSIONS) {
				R6_EXCLUSIONS = r6_EXCLUSIONS;
			}
			public BigDecimal getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R6_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR6_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r6_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R6_DEPOSITS_EXCLU_FCA_AMOUNT = r6_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R6_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR6_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r6_FOREIGN_CURR_DEPOS_AMOUNT) {
				R6_FOREIGN_CURR_DEPOS_AMOUNT = r6_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR6_TOTAL_NO_OF_ACCOUNTS() {
				return R6_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR6_TOTAL_NO_OF_ACCOUNTS(BigDecimal r6_TOTAL_NO_OF_ACCOUNTS) {
				R6_TOTAL_NO_OF_ACCOUNTS = r6_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR6_TOTAL_AMOUNT() {
				return R6_TOTAL_AMOUNT;
			}
			public void setR6_TOTAL_AMOUNT(BigDecimal r6_TOTAL_AMOUNT) {
				R6_TOTAL_AMOUNT = r6_TOTAL_AMOUNT;
			}
			public String getR7_EXCLUSIONS() {
				return R7_EXCLUSIONS;
			}
			public void setR7_EXCLUSIONS(String r7_EXCLUSIONS) {
				R7_EXCLUSIONS = r7_EXCLUSIONS;
			}
			public BigDecimal getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R7_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR7_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r7_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R7_DEPOSITS_EXCLU_FCA_AMOUNT = r7_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R7_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR7_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r7_FOREIGN_CURR_DEPOS_AMOUNT) {
				R7_FOREIGN_CURR_DEPOS_AMOUNT = r7_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR7_TOTAL_NO_OF_ACCOUNTS() {
				return R7_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR7_TOTAL_NO_OF_ACCOUNTS(BigDecimal r7_TOTAL_NO_OF_ACCOUNTS) {
				R7_TOTAL_NO_OF_ACCOUNTS = r7_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR7_TOTAL_AMOUNT() {
				return R7_TOTAL_AMOUNT;
			}
			public void setR7_TOTAL_AMOUNT(BigDecimal r7_TOTAL_AMOUNT) {
				R7_TOTAL_AMOUNT = r7_TOTAL_AMOUNT;
			}
			public String getR8_EXCLUSIONS() {
				return R8_EXCLUSIONS;
			}
			public void setR8_EXCLUSIONS(String r8_EXCLUSIONS) {
				R8_EXCLUSIONS = r8_EXCLUSIONS;
			}
			public BigDecimal getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R8_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR8_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r8_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R8_DEPOSITS_EXCLU_FCA_AMOUNT = r8_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R8_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR8_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r8_FOREIGN_CURR_DEPOS_AMOUNT) {
				R8_FOREIGN_CURR_DEPOS_AMOUNT = r8_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR8_TOTAL_NO_OF_ACCOUNTS() {
				return R8_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR8_TOTAL_NO_OF_ACCOUNTS(BigDecimal r8_TOTAL_NO_OF_ACCOUNTS) {
				R8_TOTAL_NO_OF_ACCOUNTS = r8_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR8_TOTAL_AMOUNT() {
				return R8_TOTAL_AMOUNT;
			}
			public void setR8_TOTAL_AMOUNT(BigDecimal r8_TOTAL_AMOUNT) {
				R8_TOTAL_AMOUNT = r8_TOTAL_AMOUNT;
			}
			public String getR9_EXCLUSIONS() {
				return R9_EXCLUSIONS;
			}
			public void setR9_EXCLUSIONS(String r9_EXCLUSIONS) {
				R9_EXCLUSIONS = r9_EXCLUSIONS;
			}
			public BigDecimal getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R9_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR9_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r9_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R9_DEPOSITS_EXCLU_FCA_AMOUNT = r9_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R9_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR9_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r9_FOREIGN_CURR_DEPOS_AMOUNT) {
				R9_FOREIGN_CURR_DEPOS_AMOUNT = r9_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR9_TOTAL_NO_OF_ACCOUNTS() {
				return R9_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR9_TOTAL_NO_OF_ACCOUNTS(BigDecimal r9_TOTAL_NO_OF_ACCOUNTS) {
				R9_TOTAL_NO_OF_ACCOUNTS = r9_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR9_TOTAL_AMOUNT() {
				return R9_TOTAL_AMOUNT;
			}
			public void setR9_TOTAL_AMOUNT(BigDecimal r9_TOTAL_AMOUNT) {
				R9_TOTAL_AMOUNT = r9_TOTAL_AMOUNT;
			}
			public String getR10_EXCLUSIONS() {
				return R10_EXCLUSIONS;
			}
			public void setR10_EXCLUSIONS(String r10_EXCLUSIONS) {
				R10_EXCLUSIONS = r10_EXCLUSIONS;
			}
			public BigDecimal getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R10_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR10_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r10_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R10_DEPOSITS_EXCLU_FCA_AMOUNT = r10_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R10_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR10_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r10_FOREIGN_CURR_DEPOS_AMOUNT) {
				R10_FOREIGN_CURR_DEPOS_AMOUNT = r10_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR10_TOTAL_NO_OF_ACCOUNTS() {
				return R10_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR10_TOTAL_NO_OF_ACCOUNTS(BigDecimal r10_TOTAL_NO_OF_ACCOUNTS) {
				R10_TOTAL_NO_OF_ACCOUNTS = r10_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR10_TOTAL_AMOUNT() {
				return R10_TOTAL_AMOUNT;
			}
			public void setR10_TOTAL_AMOUNT(BigDecimal r10_TOTAL_AMOUNT) {
				R10_TOTAL_AMOUNT = r10_TOTAL_AMOUNT;
			}
			public String getR11_EXCLUSIONS() {
				return R11_EXCLUSIONS;
			}
			public void setR11_EXCLUSIONS(String r11_EXCLUSIONS) {
				R11_EXCLUSIONS = r11_EXCLUSIONS;
			}
			public BigDecimal getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R11_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR11_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r11_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R11_DEPOSITS_EXCLU_FCA_AMOUNT = r11_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R11_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR11_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r11_FOREIGN_CURR_DEPOS_AMOUNT) {
				R11_FOREIGN_CURR_DEPOS_AMOUNT = r11_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR11_TOTAL_NO_OF_ACCOUNTS() {
				return R11_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR11_TOTAL_NO_OF_ACCOUNTS(BigDecimal r11_TOTAL_NO_OF_ACCOUNTS) {
				R11_TOTAL_NO_OF_ACCOUNTS = r11_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR11_TOTAL_AMOUNT() {
				return R11_TOTAL_AMOUNT;
			}
			public void setR11_TOTAL_AMOUNT(BigDecimal r11_TOTAL_AMOUNT) {
				R11_TOTAL_AMOUNT = r11_TOTAL_AMOUNT;
			}
			public String getR12_EXCLUSIONS() {
				return R12_EXCLUSIONS;
			}
			public void setR12_EXCLUSIONS(String r12_EXCLUSIONS) {
				R12_EXCLUSIONS = r12_EXCLUSIONS;
			}
			public BigDecimal getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() {
				return R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public void setR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS(BigDecimal r12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS) {
				R12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS = r12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_DEPOSITS_EXCLU_FCA_AMOUNT() {
				return R12_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public void setR12_DEPOSITS_EXCLU_FCA_AMOUNT(BigDecimal r12_DEPOSITS_EXCLU_FCA_AMOUNT) {
				R12_DEPOSITS_EXCLU_FCA_AMOUNT = r12_DEPOSITS_EXCLU_FCA_AMOUNT;
			}
			public BigDecimal getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() {
				return R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public void setR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS(BigDecimal r12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS) {
				R12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS = r12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_FOREIGN_CURR_DEPOS_AMOUNT() {
				return R12_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public void setR12_FOREIGN_CURR_DEPOS_AMOUNT(BigDecimal r12_FOREIGN_CURR_DEPOS_AMOUNT) {
				R12_FOREIGN_CURR_DEPOS_AMOUNT = r12_FOREIGN_CURR_DEPOS_AMOUNT;
			}
			public BigDecimal getR12_TOTAL_NO_OF_ACCOUNTS() {
				return R12_TOTAL_NO_OF_ACCOUNTS;
			}
			public void setR12_TOTAL_NO_OF_ACCOUNTS(BigDecimal r12_TOTAL_NO_OF_ACCOUNTS) {
				R12_TOTAL_NO_OF_ACCOUNTS = r12_TOTAL_NO_OF_ACCOUNTS;
			}
			public BigDecimal getR12_TOTAL_AMOUNT() {
				return R12_TOTAL_AMOUNT;
			}
			public void setR12_TOTAL_AMOUNT(BigDecimal r12_TOTAL_AMOUNT) {
				R12_TOTAL_AMOUNT = r12_TOTAL_AMOUNT;
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
			public Date getREPORT_RESUBDATE() {
				return REPORT_RESUBDATE;
			}

			public void setREPORT_RESUBDATE(Date rEPORT_RESUBDATE) {
				REPORT_RESUBDATE = rEPORT_RESUBDATE;
			}

		}
		
			
		// COMPOSITE KEY CLASS INSIDE SERVICE

		public static class MDISB4_PK implements Serializable {

			private Date REPORT_DATE;
			private BigDecimal REPORT_VERSION;

			public MDISB4_PK() {
			}

			public MDISB4_PK(Date REPORT_DATE, BigDecimal REPORT_VERSION) {
				this.REPORT_DATE = REPORT_DATE;
				this.REPORT_VERSION = REPORT_VERSION;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o)
					return true;
				if (!(o instanceof MDISB4_PK))
					return false;
				MDISB4_PK that = (MDISB4_PK) o;
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
			
	//DETAIL ENTITY
		
		public class MDISB4_Detail_Entity {

			@Column(name = "CUST_ID")
			private String custId;
			@Id
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

		class MDISB4DetaillRowMapper implements RowMapper<MDISB4_Detail_Entity> {

			@Override
			public MDISB4_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB4_Detail_Entity obj = new MDISB4_Detail_Entity();

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

		class MDISB4ArchivalDetaillRowMapper implements RowMapper<MDISB4_Archival_Detail_Entity> {

			@Override
			public MDISB4_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB4_Archival_Detail_Entity obj = new MDISB4_Archival_Detail_Entity();

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

		public class MDISB4_Archival_Detail_Entity {

			@Column(name = "CUST_ID")
			private String custId;
			@Id
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
	
	public ModelAndView getMDISB4View(String reportId, String fromdate, String todate,
	        String currency, String dtltype, Pageable pageable,
	        String type, BigDecimal version) {

	    ModelAndView mv = new ModelAndView();

	    System.out.println("Loading MDISB4 View...");
	    System.out.println("Version: " + version);

	   
	    if ("ARCHIVAL".equals(type) && version != null) {
				List<MDISB4_Archival_Summary_Entity> T1Master = new ArrayList<MDISB4_Archival_Summary_Entity>();
				try {
					Date dt = dateformat.parse(todate);

					T1Master = getdatabydateListarchival(dt, version);
					} catch (ParseException e) {
					e.printStackTrace();
				}

				mv.addObject("reportsummary", T1Master);
			
			} else {
				List<MDISB4_Summary_Entity> T1Master = new ArrayList<MDISB4_Summary_Entity>();
				
				try {
					Date dt = dateformat.parse(todate);

					T1Master = getDataByDate(dt);
					
					System.out.println("MDISB4");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				mv.addObject("reportsummary", T1Master);
				
			}
	        
	       
	    // ✅ IMPORTANT: MUST MATCH HTML LOCATION
	    mv.setViewName("BRRS/MDISB4");

	    mv.addObject("displaymode", "summary");

	    System.out.println("scv" + mv.getViewName());
		return mv;
	}
	
	public ModelAndView getMDISB4currentDtl(String reportId, String fromdate, String todate, String currency,
            String dtltype, Pageable pageable, String filter, String type, String version) {

        int pageSize = pageable != null ? pageable.getPageSize() : 10;
        int currentPage = pageable != null ? pageable.getPageNumber() : 0;
        int totalPages = 0;

        ModelAndView mv = new ModelAndView();

        // Session hs = sessionFactory.getCurrentSession();

        try {
            Date parsedDate = null;

            if (todate != null && !todate.isEmpty()) {
                parsedDate = dateformat.parse(todate);
            }

            String reportLabel = null;
            String reportAddlCriteria1 = null;
            // ? Split filter string into rowId & columnId
            if (filter != null && filter.contains(",")) {
                String[] parts = filter.split(",");
                if (parts.length >= 2) {
                    reportLabel = parts[0];
                    reportAddlCriteria1 = parts[1];
                }
            }

            System.out.println(type);
            if ("ARCHIVAL".equals(type) && version != null) {
                System.out.println(type);
                // ?? Archival branch
                List<MDISB4_Archival_Detail_Entity> T1Dt1;
                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = GetArchivalDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate,version);
                } else {
                    T1Dt1 = getArchivalDetaildatabydateList(parsedDate, version);
                }

                mv.addObject("reportdetails", T1Dt1);
                mv.addObject("reportmaster12", T1Dt1);
                System.out.println("ARCHIVAL COUNT: " + (T1Dt1 != null ? T1Dt1.size() : 0));

            } else {
                // ?? Current branch
                List<MDISB4_Detail_Entity> T1Dt1;

                if (reportLabel != null && reportAddlCriteria1 != null) {
                    T1Dt1 = GetDetailDataByRowIdAndColumnId(reportLabel, reportAddlCriteria1, parsedDate);
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

        mv.setViewName("BRRS/MDISB4");
        mv.addObject("displaymode", "Details");
        mv.addObject("currentPage", currentPage);
        System.out.println("totalPages: " + (int) Math.ceil((double) totalPages / 100));
        mv.addObject("totalPages", (int) Math.ceil((double) totalPages / 100));
        mv.addObject("reportsflag", "reportsflag");
        mv.addObject("menu", reportId);
        return mv;
    }


	
	public byte[] getMDISB4Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");

		// ARCHIVAL check
		
		
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB4ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Fetch data
		List<MDISB4_Summary_Entity> dataList = getDataByDate(dateformat.parse(todate));

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB4 report. Returning empty result.");
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

			int startRow = 5;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {
					MDISB4_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// R6
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR6_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR6_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR6_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					Cell cell6 = row.getCell(6);
					if (record.getR6_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR6_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R7
					row = sheet.getRow(6);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR7_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR7_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR7_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR7_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR7_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R8
					row = sheet.getRow(7);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR8_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR8_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR8_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR8_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR8_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R9
					row = sheet.getRow(8);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR9_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR9_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR9_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR9_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R10
					row = sheet.getRow(9);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR10_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR10_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR10_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR10_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R11
					row = sheet.getRow(10);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR11_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR11_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR11_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR11_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					// R12
					row = sheet.getRow(11);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR12_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR12_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR12_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR12_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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

	public byte[] getMDISB4DetailExcel(String filename, String fromdate, String todate, String currency, String dtltype,
			String type, String version) {
		try {
			logger.info("Generating Excel for MDISB4 Details...");
			System.out.println("came to Detail download service");

			if (type.equals("ARCHIVAL") & version != null) {
				byte[] ARCHIVALreport = getDetailExcelARCHIVAL(filename, fromdate, todate, currency, dtltype, type,
						version);
				return ARCHIVALreport;
			}

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB4Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "ACCOUNT BALANCE IN PULA", "ROWID", "COLUMNID",
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
			List<MDISB4_Detail_Entity> reportData = getDetaildatabydateList(parsedToDate);

			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB4_Detail_Entity item : reportData) {
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
				logger.info("No data found for MDISB4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB4 Excel", e);
			return new byte[0];
		}
	}

	public List<Object[]> getMDISB4Archival() {

	    List<Object[]> archivalList = new ArrayList<>();

	    try {

	        // ✅ Use your existing autowired variable (NOT static call)
	        List<MDISB4_Archival_Summary_Entity> repoData = getdatabydateListWithVersion();
	               

	        if (repoData != null && !repoData.isEmpty()) {

	            for (MDISB4_Archival_Summary_Entity entity : repoData) {

	                Object[] row = new Object[] {
	                        entity.getREPORT_DATE(),
	                        entity.getREPORT_VERSION(),
	                        entity.getREPORT_RESUBDATE()
	                };

	                archivalList.add(row);
	            }

	            System.out.println("Fetched " + archivalList.size() + " archival records from MDISB4");

	            MDISB4_Archival_Summary_Entity first = repoData.get(0);
	            System.out.println("Latest archival version: " + first.getREPORT_VERSION());

	        } else {
	            System.out.println("No archival data found in MDISB4.");
	        }

	    } catch (Exception e) {

	        System.err.println("Error fetching MDISB4 Archival data: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return archivalList;
	}
	
	
	public byte[] getExcelMDISB4ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if (type.equals("ARCHIVAL") & version != null) {

		}
		List<MDISB4_Archival_Summary_Entity> dataList = getdatabydateListarchival(dateformat.parse(todate),version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB4 report. Returning empty result.");
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
					MDISB4_Archival_Summary_Entity record = dataList.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					
					// R6
					// Column B
					Cell cell1 = row.getCell(1);
					if (record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					Cell cell2 = row.getCell(2);
					if (record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					Cell cell3 = row.getCell(3);
					if (record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					Cell cell4 = row.getCell(4);
					if (record.getR6_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR6_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					Cell cell5 = row.getCell(5);
					if (record.getR6_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR6_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					Cell cell6 = row.getCell(6);
					if (record.getR6_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR6_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R7
					row = sheet.getRow(6);
					// Column B
					cell1 = row.getCell(1);
					if (record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR7_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR7_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR7_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR7_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR7_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR7_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R8
					row = sheet.getRow(7);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR8_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR8_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR8_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR8_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR8_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR8_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					
					// R9
					row = sheet.getRow(8);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR9_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR9_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR9_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR9_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR9_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR9_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R10
					row = sheet.getRow(9);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR10_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR10_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR10_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR10_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR10_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR10_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					// R11
					row = sheet.getRow(10);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR11_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR11_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR11_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR11_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR11_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR11_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR11_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}
					
					
					
					// R12
					row = sheet.getRow(11);
					
					// Column B
					cell1 = row.getCell(1);
					if (record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS() != null) {
						cell1.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}
					
					// Column C
					cell2 = row.getCell(2);
					if (record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT() != null) {
						cell2.setCellValue(record.getR12_DEPOSITS_EXCLU_FCA_AMOUNT().doubleValue());
						
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}
					
					// Column D
					cell3 = row.getCell(3);
					if (record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS() != null) {
						cell3.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}
					
					// Column E
					cell4 = row.getCell(4);
					if (record.getR12_FOREIGN_CURR_DEPOS_AMOUNT() != null) {
						cell4.setCellValue(record.getR12_FOREIGN_CURR_DEPOS_AMOUNT().doubleValue());
						
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}
					
					// Column F
					cell5 = row.getCell(5);
					if (record.getR12_TOTAL_NO_OF_ACCOUNTS() != null) {
						cell5.setCellValue(record.getR12_TOTAL_NO_OF_ACCOUNTS().doubleValue());
						
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}
					
					// Column G
					cell6 = row.getCell(6);
					if (record.getR12_TOTAL_AMOUNT() != null) {
						cell6.setCellValue(record.getR12_TOTAL_AMOUNT().doubleValue());
						
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
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

	
	public byte[] getDetailExcelARCHIVAL(String filename, String fromdate, String todate, String currency,
			String dtltype, String type, String version) {
		try {
			logger.info("Generating Excel for BRRS_MDISB4 ARCHIVAL Details...");
			System.out.println("came to Detail download service");
			if (type.equals("ARCHIVAL") & version != null) {

			}
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("MDISB4Details");

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
			String[] headers = { "CUST ID", "ACCT NO", "ACCT NAME", "PROVISION AMOUNT", "ROWID", "COLUMNID",
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
			List<MDISB4_Archival_Detail_Entity> reportData = getArchivalDetaildatabydateList(parsedToDate, version);


			if (reportData != null && !reportData.isEmpty()) {
				int rowIndex = 1;
				for (MDISB4_Archival_Detail_Entity item : reportData) {
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
				logger.info("No data found for MDISB4 — only header will be written.");
			}

			// Write to byte[]
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			logger.info("Excel generation completed with {} row(s).", reportData != null ? reportData.size() : 0);
			return bos.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating MDISB4 Excel", e);
			return new byte[0];
		}
	}

	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public ModelAndView getViewOrEditPage(String acctNo, String formMode) {
		ModelAndView mv = new ModelAndView("BRRS/MDISB4"); 

		if (acctNo != null) {
			MDISB4_Detail_Entity mMDISB4Entity = findByAcctnumber(acctNo);
			if (mMDISB4Entity != null && mMDISB4Entity.getReportDate() != null) {
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(mMDISB4Entity.getReportDate());
				mv.addObject("asondate", formattedDate);
			}
			mv.addObject("Data", mMDISB4Entity);
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
	        MDISB4_Detail_Entity existing = findByAcctnumber(acctNo);

	        if (existing == null) {

	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Record not found for update.");
	        }

	        boolean isChanged = false;

	        // ACCOUNT NAME
	        if (acctName != null && !acctName.isEmpty()) {

	            if (existing.getAcctName() == null
	                    || !existing.getAcctName().equals(acctName)) {

	                existing.setAcctName(acctName);

	                isChanged = true;
	            }
	        }

	        // ACCOUNT BALANCE IN PULA
	        if (acctBalanceInpulaStr != null
	                && !acctBalanceInpulaStr.isEmpty()) {

	            BigDecimal newBalance = new BigDecimal(acctBalanceInpulaStr);

	            if (existing.getAcctBalanceInpula() == null
	                    || existing.getAcctBalanceInpula().compareTo(newBalance) != 0) {

	                existing.setAcctBalanceInpula(newBalance);

	                isChanged = true;
	            }
	        }

	        // UPDATE
	        if (isChanged) {

	            String sql = "UPDATE BRRS_MDISB4_DETAILTABLE "
	                    + "SET ACCT_NAME = ?, "
	                    + "ACCT_BALANCE_IN_PULA = ? "
	                    + "WHERE ACCT_NUMBER = ?";

	            jdbcTemplate.update(
	                    sql,
	                    existing.getAcctName(),
	                    existing.getAcctBalanceInpula(),
	                    acctNo);

	            System.out.println("Record updated successfully");

	            // DATE FORMAT
	            String formattedDate = new SimpleDateFormat("dd-MM-yyyy")
	                    .format(new SimpleDateFormat("yyyy-MM-dd")
	                            .parse(reportDateStr));

	            // PROCEDURE CALL
	            TransactionSynchronizationManager.registerSynchronization(
	                    new TransactionSynchronizationAdapter() {

	                        @Override
	                        public void afterCommit() {

	                            try {

	                                jdbcTemplate.update(
	                                        "BEGIN BRRS_MDISB4_SUMMARY_PROCEDURE(?); END;",
	                                        formattedDate);

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


}
