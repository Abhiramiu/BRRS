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

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;


@Component
@Service

public class BRRS_BDISB3_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB3_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	// ENTITY MANAGER (Acts like Repository)
    @PersistenceContext
	private EntityManager entityManager;

    
 // SUMMARY
 	// Fetch data by report date
 	public List<BDISB3_Summary_Entity> getDataByDate1(Date reportDate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_SUMMARYTABLE WHERE REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB3_RowMapper_Summary());
 	}			

 	
 	// ARCHIVAL

 	// Fetch data by report date
 	public List<BDISB3_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB3_RowMapper_Archival());
 	}
     
 	// RESUB

 	// Fetch data by report date
 	public List<BDISB3_RESUB_Summary_Entity> ResubgetDataByDate1(Date reportDate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB3_RowMapper_Resub());
 	}
     
 	/*
 	 * // ARCHIVAL // GET REPORT_DATE + REPORT_VERSION
 	 * 
 	 * public List<Object[]> getBDISB3Archival() {
 	 * 
 	 * String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
 	 * "FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY" + "ORDER BY REPORT_VERSION";
 	 * 
 	 * return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
 	 * rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") }); }
 	 */
 	
 	//GET ARCHIVAL FULL DATA BY DATE + VERSION

 	public List<BDISB3_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE,
 			BigDecimal REPORT_VERSION) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
 				+ "AND REPORT_VERSION = ?";

 		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB3_RowMapper_Archival());
 	}
     
 	//GET RESUB FULL DATA BY DATE + VERSION

 	public List<BDISB3_RESUB_Summary_Entity> getdatabydateListresub1(Date REPORT_DATE,
 			BigDecimal REPORT_VERSION) {

 		String sql = "SELECT * FROM BRRS_BDISB3_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
 				+ "AND REPORT_VERSION = ?";

 		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB3_RowMapper_Resub());
 	}
 	
 	//GET DETAIL FULL DATA BY DATE + VERSION

 	public List<BDISB3_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE,
 			BigDecimal REPORT_VERSION) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE" + "WHERE REPORT_DATE = ? "
 				+ "AND REPORT_VERSION = ?";

 		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB3RowMapper_Detail());
 	}
 	
 	//GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

 	public List<BDISB3_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
 			BigDecimal REPORT_VERSION) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
 				+ "AND REPORT_VERSION = ?";

 		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB3RowMapper_ArchivalDetail());
 	}
 	
 	
 	//GET RESUB DETAIL FULL DATA BY DATE + VERSION

 	public List<BDISB3_RESUB_Detail_Entity> getdatabydateListResubDetail1(Date REPORT_DATE,
 			BigDecimal REPORT_VERSION) {

 		String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
 				+ "AND REPORT_VERSION = ?";

 		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB3RowMapper_ResubDetail());
 	}
 	
 	//GET ALL WITH VERSION

 	public List<BDISB3_Archival_Summary_Entity> getdatabydateListWithVersion1() {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
 				+ "ORDER BY REPORT_VERSION ASC";

 		return jdbcTemplate.query(sql, new BDISB3_RowMapper_Archival());
 	}
 	
 	//GET RESUB ALL WITH VERSION

 	public List<BDISB3_RESUB_Summary_Entity> ResubgetdatabydateListWithVersion1() {

 		String sql = "SELECT * FROM BRRS_BDISB3_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
 				+ "ORDER BY REPORT_VERSION ASC";

 		return jdbcTemplate.query(sql, new BDISB3_RowMapper_Resub());
 	}
 	
 	//GET ARCHIVAL MAX VERSION BY DATE

 	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

 		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY"
 				+ "WHERE REPORT_DATE = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
 	}
 	
 	// GET RESUB MAX VERSION BY DATE

 	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {

 		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB3_RESUBTABLE_SUMMARY "
 				+ "WHERE REPORT_DATE = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
 	}
 	
 	
 	//DETAIL TABLE 1
 	// 1. BY DATE + LABEL + CRITERIA

 	public List<BDISB3_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
 			String reportAddlCriteria1) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE "
 				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
 				new BDISB3RowMapper_Detail());
 	}	
 	
 	// 2. GET ALL (BY DATE - simple)

 	public List<BDISB3_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE WHERE REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB3RowMapper_Detail());
 	}

 // 3. PAGINATION

 	public List<BDISB3_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE "
 				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

 		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB3RowMapper_Detail());
 	}

 	// 4. COUNT

 	public int getDetaildatacount1(Date reportdate) {

 		String sql = "SELECT COUNT(*) FROM BRRS_BDISB3_DETAILTABLE WHERE REPORT_DATE = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
 	}

 // 5. BY LABEL + CRITERIA

 	public List<BDISB3_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel,
 			String reportAddlCriteria1, Date reportdate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE "
 				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
 				new BDISB3RowMapper_Detail());
 	}
 	
 // 6. BY ACCOUNT NUMBER

 	public BDISB3_Detail_Entity findByAcctnumber1(String acctNumber) {

 		String sql = "SELECT * FROM BRRS_BDISB3_DETAILTABLE WHERE ACCT_NUMBER = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB3RowMapper_Detail());
 	}	
 	
 	
 	
 	//ARCHIVALTABLE_DETAIL 
 	// 1. BY DATE + LABEL + CRITERIA

 	public List<BDISB3_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
 			String reportAddlCriteria1) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL "
 				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
 				new BDISB3RowMapper_ArchivalDetail());
 	}	
 	
 	// 2. GET ALL (BY DATE - simple)

 	public List<BDISB3_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB3RowMapper_ArchivalDetail());
 	}

 // 3. PAGINATION

 	public List<BDISB3_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset, int limit) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL "
 				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

 		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB3RowMapper_ArchivalDetail());
 	}

 	// 4. COUNT

 	public int getArchivalDetaildatacount1(Date reportdate) {

 		String sql = "SELECT COUNT(*) FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
 	}

 // 5. BY LABEL + CRITERIA

 	public List<BDISB3_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
 			String reportAddlCriteria1, Date reportdate) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL "
 				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

 		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
 				new BDISB3RowMapper_ArchivalDetail());
 	}
 // 6. BY ACCOUNT NUMBER

 	public BDISB3_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

 		String sql = "SELECT * FROM BRRS_BDISB3_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

 		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB3RowMapper_ArchivalDetail());
 	}	
 	
 	
 	
 	//RESUBTABLE_DETAIL 
 				// 1. BY DATE + LABEL + CRITERIA

 				public List<BDISB3_RESUB_Detail_Entity> findByResubReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
 						String reportAddlCriteria1) {

 					String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE "
 							+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

 					return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
 							new BDISB3RowMapper_ResubDetail());
 				}	
 				
 				// 2. GET ALL (BY DATE - simple)

 				public List<BDISB3_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate) {

 					String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

 					return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB3RowMapper_ResubDetail());
 				}

 			// 3. PAGINATION

 				public List<BDISB3_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate, int offset, int limit) {

 					String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE "
 							+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

 					return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB3RowMapper_ResubDetail());
 				}
 			
 				// 4. COUNT

 				public int getResubdatacount1(Date reportdate) {

 					String sql = "SELECT COUNT(*) FROM BRRS_BDISB3_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

 					return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
 				}

 			// 5. BY LABEL + CRITERIA

 				public List<BDISB3_RESUB_Detail_Entity> GetResubDataByRowIdAndColumnId1(String reportLabel,
 						String reportAddlCriteria1, Date reportdate) {

 					String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE "
 							+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

 					return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
 							new BDISB3RowMapper_ResubDetail());
 				}
 			// 6. BY ACCOUNT NUMBER

 				public BDISB3_RESUB_Detail_Entity ResubfindByAcctnumber1(String acctNumber) {

 					String sql = "SELECT * FROM BRRS_BDISB3_RESUB_DETAILTABLE WHERE ACCT_NUMBER = ?";

 					return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB3RowMapper_ResubDetail());
 				}	
 				
 				
 				//findSummaryByReportDate
 				
 				@Transactional(readOnly = true)
 				public BDISB3_Summary_Entity findSummaryByReportDate(Date reportDate) {

 				    String sql =
 				            "SELECT * FROM BRRS_BDISB3_SUMMARYTABLE " +
 				            "WHERE REPORT_DATE = ?";

 				    List<BDISB3_Summary_Entity> list =
 				            jdbcTemplate.query(
 				                    sql,
 				                    new Object[] { reportDate },
 				                    new BDISB3_RowMapper_Summary());

 				    return list.isEmpty() ? null : list.get(0);
 				}
 				
 				@Transactional(readOnly = true)
 				public BDISB3_Detail_Entity findDetailByReportDate(Date reportDate) {

 				    String sql =
 				            "SELECT * FROM BRRS_BDISB3_DETAILTABLE " +
 				            "WHERE REPORT_DATE = ?";

 				    List<BDISB3_Detail_Entity> list =
 				            jdbcTemplate.query(
 				                    sql,
 				                    new Object[] { reportDate },
 				                    new BDISB3RowMapper_Detail());

 				    return list.isEmpty() ? null : list.get(0);
 				}
 							
 			// ROW MAPPER SUMMARY

				class BDISB3_RowMapper_Summary implements RowMapper<BDISB3_Summary_Entity> {

					@Override
					public BDISB3_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB3_Summary_Entity obj = new BDISB3_Summary_Entity();	
						
						// -------- R5 --------
						obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
						obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
						obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

						// -------- R6 --------
						obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
						obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
						obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

						// -------- R7 --------
						obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
						obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
						obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

						// -------- R8 --------
						obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
						obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
						obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

						// -------- R9 --------
						obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
						obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
						obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

						// -------- R10 --------
						obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
						obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
						obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
						
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
				
				public static class BDISB3_Summary_Entity {
					
					public String R5_SCVRN;
				    public BigDecimal R5_AGGREGATE_BALANCE;
				    public BigDecimal R5_COMPENSATABLE_AMOUNT;

				    // -------- R6 --------
				    public String R6_SCVRN;
				    public BigDecimal R6_AGGREGATE_BALANCE;
				    public BigDecimal R6_COMPENSATABLE_AMOUNT;

				    // -------- R7 --------
				    public String R7_SCVRN;
				    public BigDecimal R7_AGGREGATE_BALANCE;
				    public BigDecimal R7_COMPENSATABLE_AMOUNT;

				    // -------- R8 --------
				    public String R8_SCVRN;
				    public BigDecimal R8_AGGREGATE_BALANCE;
				    public BigDecimal R8_COMPENSATABLE_AMOUNT;

				    // -------- R9 --------
				    public String R9_SCVRN;
				    public BigDecimal R9_AGGREGATE_BALANCE;
				    public BigDecimal R9_COMPENSATABLE_AMOUNT;

				    // -------- R10 --------
				    public String R10_SCVRN;
				    public BigDecimal R10_AGGREGATE_BALANCE;
				    public BigDecimal R10_COMPENSATABLE_AMOUNT;
				    
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
					
					public String getR5_SCVRN() {
						return R5_SCVRN;
					}
					public void setR5_SCVRN(String r5_SCVRN) {
						R5_SCVRN = r5_SCVRN;
					}
					public BigDecimal getR5_AGGREGATE_BALANCE() {
						return R5_AGGREGATE_BALANCE;
					}
					public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
						R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
					}
					public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
						return R5_COMPENSATABLE_AMOUNT;
					}
					public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
						R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
					}
					public String getR6_SCVRN() {
						return R6_SCVRN;
					}
					public void setR6_SCVRN(String r6_SCVRN) {
						R6_SCVRN = r6_SCVRN;
					}
					public BigDecimal getR6_AGGREGATE_BALANCE() {
						return R6_AGGREGATE_BALANCE;
					}
					public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
						R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
					}
					public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
						return R6_COMPENSATABLE_AMOUNT;
					}
					public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
						R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
					}
					public String getR7_SCVRN() {
						return R7_SCVRN;
					}
					public void setR7_SCVRN(String r7_SCVRN) {
						R7_SCVRN = r7_SCVRN;
					}
					public BigDecimal getR7_AGGREGATE_BALANCE() {
						return R7_AGGREGATE_BALANCE;
					}
					public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
						R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
					}
					public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
						return R7_COMPENSATABLE_AMOUNT;
					}
					public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
						R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
					}
					public String getR8_SCVRN() {
						return R8_SCVRN;
					}
					public void setR8_SCVRN(String r8_SCVRN) {
						R8_SCVRN = r8_SCVRN;
					}
					public BigDecimal getR8_AGGREGATE_BALANCE() {
						return R8_AGGREGATE_BALANCE;
					}
					public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
						R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
					}
					public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
						return R8_COMPENSATABLE_AMOUNT;
					}
					public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
						R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
					}
					public String getR9_SCVRN() {
						return R9_SCVRN;
					}
					public void setR9_SCVRN(String r9_SCVRN) {
						R9_SCVRN = r9_SCVRN;
					}
					public BigDecimal getR9_AGGREGATE_BALANCE() {
						return R9_AGGREGATE_BALANCE;
					}
					public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
						R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
					}
					public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
						return R9_COMPENSATABLE_AMOUNT;
					}
					public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
						R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
					}
					public String getR10_SCVRN() {
						return R10_SCVRN;
					}
					public void setR10_SCVRN(String r10_SCVRN) {
						R10_SCVRN = r10_SCVRN;
					}
					public BigDecimal getR10_AGGREGATE_BALANCE() {
						return R10_AGGREGATE_BALANCE;
					}
					public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
						R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
					}
					public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
						return R10_COMPENSATABLE_AMOUNT;
					}
					public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
						R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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

					class BDISB3RowMapper_Detail implements RowMapper<BDISB3_Detail_Entity> {

						@Override
						public BDISB3_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

							BDISB3_Detail_Entity obj = new BDISB3_Detail_Entity();	
							
							// -------- R5 --------
							obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
							obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
							obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

							// -------- R6 --------
							obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
							obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
							obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

							// -------- R7 --------
							obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
							obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
							obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

							// -------- R8 --------
							obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
							obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
							obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

							// -------- R9 --------
							obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
							obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
							obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

							// -------- R10 --------
							obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
							obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
							obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
							
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
					
					public static class BDISB3_Detail_Entity {
						
						public String R5_SCVRN;
					    public BigDecimal R5_AGGREGATE_BALANCE;
					    public BigDecimal R5_COMPENSATABLE_AMOUNT;

					    // -------- R6 --------
					    public String R6_SCVRN;
					    public BigDecimal R6_AGGREGATE_BALANCE;
					    public BigDecimal R6_COMPENSATABLE_AMOUNT;

					    // -------- R7 --------
					    public String R7_SCVRN;
					    public BigDecimal R7_AGGREGATE_BALANCE;
					    public BigDecimal R7_COMPENSATABLE_AMOUNT;

					    // -------- R8 --------
					    public String R8_SCVRN;
					    public BigDecimal R8_AGGREGATE_BALANCE;
					    public BigDecimal R8_COMPENSATABLE_AMOUNT;

					    // -------- R9 --------
					    public String R9_SCVRN;
					    public BigDecimal R9_AGGREGATE_BALANCE;
					    public BigDecimal R9_COMPENSATABLE_AMOUNT;

					    // -------- R10 --------
					    public String R10_SCVRN;
					    public BigDecimal R10_AGGREGATE_BALANCE;
					    public BigDecimal R10_COMPENSATABLE_AMOUNT;
					    
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
						
						public String getR5_SCVRN() {
							return R5_SCVRN;
						}
						public void setR5_SCVRN(String r5_SCVRN) {
							R5_SCVRN = r5_SCVRN;
						}
						
						public BigDecimal getR5_AGGREGATE_BALANCE() {
							return R5_AGGREGATE_BALANCE;
						}
						public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
							R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
						}
						public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
							return R5_COMPENSATABLE_AMOUNT;
						}
						public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
							R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
						}
						public String getR6_SCVRN() {
							return R6_SCVRN;
						}
						public void setR6_SCVRN(String r6_SCVRN) {
							R6_SCVRN = r6_SCVRN;
						}
						public BigDecimal getR6_AGGREGATE_BALANCE() {
							return R6_AGGREGATE_BALANCE;
						}
						public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
							R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
						}
						public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
							return R6_COMPENSATABLE_AMOUNT;
						}
						public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
							R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
						}
						public String getR7_SCVRN() {
							return R7_SCVRN;
						}
						public void setR7_SCVRN(String r7_SCVRN) {
							R7_SCVRN = r7_SCVRN;
						}
						public BigDecimal getR7_AGGREGATE_BALANCE() {
							return R7_AGGREGATE_BALANCE;
						}
						public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
							R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
						}
						public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
							return R7_COMPENSATABLE_AMOUNT;
						}
						public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
							R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
						}
						public String getR8_SCVRN() {
							return R8_SCVRN;
						}
						public void setR8_SCVRN(String r8_SCVRN) {
							R8_SCVRN = r8_SCVRN;
						}
						public BigDecimal getR8_AGGREGATE_BALANCE() {
							return R8_AGGREGATE_BALANCE;
						}
						public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
							R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
						}
						public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
							return R8_COMPENSATABLE_AMOUNT;
						}
						public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
							R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
						}
						public String getR9_SCVRN() {
							return R9_SCVRN;
						}
						public void setR9_SCVRN(String r9_SCVRN) {
							R9_SCVRN = r9_SCVRN;
						}
						public BigDecimal getR9_AGGREGATE_BALANCE() {
							return R9_AGGREGATE_BALANCE;
						}
						public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
							R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
						}
						public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
							return R9_COMPENSATABLE_AMOUNT;
						}
						public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
							R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
						}
						public String getR10_SCVRN() {
							return R10_SCVRN;
						}
						public void setR10_SCVRN(String r10_SCVRN) {
							R10_SCVRN = r10_SCVRN;
						}
						public BigDecimal getR10_AGGREGATE_BALANCE() {
							return R10_AGGREGATE_BALANCE;
						}
						public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
							R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
						}
						public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
							return R10_COMPENSATABLE_AMOUNT;
						}
						public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
							R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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
					
					
		 			// ROW MAPPER ARCHIVAL SUMMARY

					class BDISB3_RowMapper_Archival implements RowMapper<BDISB3_Archival_Summary_Entity> {

						@Override
						public BDISB3_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

							BDISB3_Archival_Summary_Entity obj = new BDISB3_Archival_Summary_Entity();	
							
							// -------- R5 --------
							obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
							obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
							obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

							// -------- R6 --------
							obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
							obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
							obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

							// -------- R7 --------
							obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
							obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
							obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

							// -------- R8 --------
							obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
							obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
							obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

							// -------- R9 --------
							obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
							obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
							obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

							// -------- R10 --------
							obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
							obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
							obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
							
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
					
					public static class BDISB3_Archival_Summary_Entity {
						
						public String R5_SCVRN;
					    public BigDecimal R5_AGGREGATE_BALANCE;
					    public BigDecimal R5_COMPENSATABLE_AMOUNT;

					    // -------- R6 --------
					    public String R6_SCVRN;
					    public BigDecimal R6_AGGREGATE_BALANCE;
					    public BigDecimal R6_COMPENSATABLE_AMOUNT;

					    // -------- R7 --------
					    public String R7_SCVRN;
					    public BigDecimal R7_AGGREGATE_BALANCE;
					    public BigDecimal R7_COMPENSATABLE_AMOUNT;

					    // -------- R8 --------
					    public String R8_SCVRN;
					    public BigDecimal R8_AGGREGATE_BALANCE;
					    public BigDecimal R8_COMPENSATABLE_AMOUNT;

					    // -------- R9 --------
					    public String R9_SCVRN;
					    public BigDecimal R9_AGGREGATE_BALANCE;
					    public BigDecimal R9_COMPENSATABLE_AMOUNT;

					    // -------- R10 --------
					    public String R10_SCVRN;
					    public BigDecimal R10_AGGREGATE_BALANCE;
					    public BigDecimal R10_COMPENSATABLE_AMOUNT;
					    
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
						
						
						public String getR5_SCVRN() {
							return R5_SCVRN;
						}
						public void setR5_SCVRN(String r5_SCVRN) {
							R5_SCVRN = r5_SCVRN;
						}
						public BigDecimal getR5_AGGREGATE_BALANCE() {
							return R5_AGGREGATE_BALANCE;
						}
						public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
							R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
						}
						public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
							return R5_COMPENSATABLE_AMOUNT;
						}
						public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
							R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
						}
						public String getR6_SCVRN() {
							return R6_SCVRN;
						}
						public void setR6_SCVRN(String r6_SCVRN) {
							R6_SCVRN = r6_SCVRN;
						}
						public BigDecimal getR6_AGGREGATE_BALANCE() {
							return R6_AGGREGATE_BALANCE;
						}
						public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
							R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
						}
						public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
							return R6_COMPENSATABLE_AMOUNT;
						}
						public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
							R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
						}
						public String getR7_SCVRN() {
							return R7_SCVRN;
						}
						public void setR7_SCVRN(String r7_SCVRN) {
							R7_SCVRN = r7_SCVRN;
						}
						public BigDecimal getR7_AGGREGATE_BALANCE() {
							return R7_AGGREGATE_BALANCE;
						}
						public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
							R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
						}
						public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
							return R7_COMPENSATABLE_AMOUNT;
						}
						public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
							R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
						}
						public String getR8_SCVRN() {
							return R8_SCVRN;
						}
						public void setR8_SCVRN(String r8_SCVRN) {
							R8_SCVRN = r8_SCVRN;
						}
						public BigDecimal getR8_AGGREGATE_BALANCE() {
							return R8_AGGREGATE_BALANCE;
						}
						public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
							R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
						}
						public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
							return R8_COMPENSATABLE_AMOUNT;
						}
						public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
							R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
						}
						public String getR9_SCVRN() {
							return R9_SCVRN;
						}
						public void setR9_SCVRN(String r9_SCVRN) {
							R9_SCVRN = r9_SCVRN;
						}
						public BigDecimal getR9_AGGREGATE_BALANCE() {
							return R9_AGGREGATE_BALANCE;
						}
						public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
							R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
						}
						public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
							return R9_COMPENSATABLE_AMOUNT;
						}
						public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
							R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
						}
						public String getR10_SCVRN() {
							return R10_SCVRN;
						}
						public void setR10_SCVRN(String r10_SCVRN) {
							R10_SCVRN = r10_SCVRN;
						}
						public BigDecimal getR10_AGGREGATE_BALANCE() {
							return R10_AGGREGATE_BALANCE;
						}
						public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
							R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
						}
						public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
							return R10_COMPENSATABLE_AMOUNT;
						}
						public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
							R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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

					class BDISB3RowMapper_ArchivalDetail implements RowMapper<BDISB3_Archival_Detail_Entity> {

						@Override
						public BDISB3_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

							BDISB3_Archival_Detail_Entity obj = new BDISB3_Archival_Detail_Entity();	
							
							// -------- R5 --------
							obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
							obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
							obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

							// -------- R6 --------
							obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
							obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
							obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

							// -------- R7 --------
							obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
							obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
							obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

							// -------- R8 --------
							obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
							obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
							obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

							// -------- R9 --------
							obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
							obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
							obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

							// -------- R10 --------
							obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
							obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
							obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
							
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
					
					public static class BDISB3_Archival_Detail_Entity {
						
						public String R5_SCVRN;
					    public BigDecimal R5_AGGREGATE_BALANCE;
					    public BigDecimal R5_COMPENSATABLE_AMOUNT;

					    // -------- R6 --------
					    public String R6_SCVRN;
					    public BigDecimal R6_AGGREGATE_BALANCE;
					    public BigDecimal R6_COMPENSATABLE_AMOUNT;

					    // -------- R7 --------
					    public String R7_SCVRN;
					    public BigDecimal R7_AGGREGATE_BALANCE;
					    public BigDecimal R7_COMPENSATABLE_AMOUNT;

					    // -------- R8 --------
					    public String R8_SCVRN;
					    public BigDecimal R8_AGGREGATE_BALANCE;
					    public BigDecimal R8_COMPENSATABLE_AMOUNT;

					    // -------- R9 --------
					    public String R9_SCVRN;
					    public BigDecimal R9_AGGREGATE_BALANCE;
					    public BigDecimal R9_COMPENSATABLE_AMOUNT;

					    // -------- R10 --------
					    public String R10_SCVRN;
					    public BigDecimal R10_AGGREGATE_BALANCE;
					    public BigDecimal R10_COMPENSATABLE_AMOUNT;
					    
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
						
						public String getR5_SCVRN() {
							return R5_SCVRN;
						}
						public void setR5_SCVRN(String r5_SCVRN) {
							R5_SCVRN = r5_SCVRN;
						}
						
						public BigDecimal getR5_AGGREGATE_BALANCE() {
							return R5_AGGREGATE_BALANCE;
						}
						public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
							R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
						}
						public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
							return R5_COMPENSATABLE_AMOUNT;
						}
						public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
							R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
						}
						public String getR6_SCVRN() {
							return R6_SCVRN;
						}
						public void setR6_SCVRN(String r6_SCVRN) {
							R6_SCVRN = r6_SCVRN;
						}
						public BigDecimal getR6_AGGREGATE_BALANCE() {
							return R6_AGGREGATE_BALANCE;
						}
						public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
							R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
						}
						public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
							return R6_COMPENSATABLE_AMOUNT;
						}
						public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
							R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
						}
						public String getR7_SCVRN() {
							return R7_SCVRN;
						}
						public void setR7_SCVRN(String r7_SCVRN) {
							R7_SCVRN = r7_SCVRN;
						}
						public BigDecimal getR7_AGGREGATE_BALANCE() {
							return R7_AGGREGATE_BALANCE;
						}
						public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
							R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
						}
						public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
							return R7_COMPENSATABLE_AMOUNT;
						}
						public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
							R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
						}
						public String getR8_SCVRN() {
							return R8_SCVRN;
						}
						public void setR8_SCVRN(String r8_SCVRN) {
							R8_SCVRN = r8_SCVRN;
						}
						public BigDecimal getR8_AGGREGATE_BALANCE() {
							return R8_AGGREGATE_BALANCE;
						}
						public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
							R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
						}
						public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
							return R8_COMPENSATABLE_AMOUNT;
						}
						public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
							R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
						}
						public String getR9_SCVRN() {
							return R9_SCVRN;
						}
						public void setR9_SCVRN(String r9_SCVRN) {
							R9_SCVRN = r9_SCVRN;
						}
						public BigDecimal getR9_AGGREGATE_BALANCE() {
							return R9_AGGREGATE_BALANCE;
						}
						public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
							R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
						}
						public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
							return R9_COMPENSATABLE_AMOUNT;
						}
						public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
							R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
						}
						public String getR10_SCVRN() {
							return R10_SCVRN;
						}
						public void setR10_SCVRN(String r10_SCVRN) {
							R10_SCVRN = r10_SCVRN;
						}
						public BigDecimal getR10_AGGREGATE_BALANCE() {
							return R10_AGGREGATE_BALANCE;
						}
						public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
							R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
						}
						public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
							return R10_COMPENSATABLE_AMOUNT;
						}
						public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
							R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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
					
					
		 			// ROW MAPPER RESUB SUMMARY

					class BDISB3_RowMapper_Resub implements RowMapper<BDISB3_RESUB_Summary_Entity> {

						@Override
						public BDISB3_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

							BDISB3_RESUB_Summary_Entity obj = new BDISB3_RESUB_Summary_Entity();	
							
							// -------- R5 --------
							obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
							obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
							obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

							// -------- R6 --------
							obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
							obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
							obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

							// -------- R7 --------
							obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
							obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
							obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

							// -------- R8 --------
							obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
							obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
							obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

							// -------- R9 --------
							obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
							obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
							obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

							// -------- R10 --------
							obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
							obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
							obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
							
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
					
					public static class BDISB3_RESUB_Summary_Entity {
						
						public String R5_SCVRN;
					    public BigDecimal R5_AGGREGATE_BALANCE;
					    public BigDecimal R5_COMPENSATABLE_AMOUNT;

					    // -------- R6 --------
					    public String R6_SCVRN;
					    public BigDecimal R6_AGGREGATE_BALANCE;
					    public BigDecimal R6_COMPENSATABLE_AMOUNT;

					    // -------- R7 --------
					    public String R7_SCVRN;
					    public BigDecimal R7_AGGREGATE_BALANCE;
					    public BigDecimal R7_COMPENSATABLE_AMOUNT;

					    // -------- R8 --------
					    public String R8_SCVRN;
					    public BigDecimal R8_AGGREGATE_BALANCE;
					    public BigDecimal R8_COMPENSATABLE_AMOUNT;

					    // -------- R9 --------
					    public String R9_SCVRN;
					    public BigDecimal R9_AGGREGATE_BALANCE;
					    public BigDecimal R9_COMPENSATABLE_AMOUNT;

					    // -------- R10 --------
					    public String R10_SCVRN;
					    public BigDecimal R10_AGGREGATE_BALANCE;
					    public BigDecimal R10_COMPENSATABLE_AMOUNT;
					    
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
						
						public String getR5_SCVRN() {
							return R5_SCVRN;
						}
						public void setR5_SCVRN(String r5_SCVRN) {
							R5_SCVRN = r5_SCVRN;
						}
						
						public BigDecimal getR5_AGGREGATE_BALANCE() {
							return R5_AGGREGATE_BALANCE;
						}
						public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
							R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
						}
						public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
							return R5_COMPENSATABLE_AMOUNT;
						}
						public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
							R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
						}
						public String getR6_SCVRN() {
							return R6_SCVRN;
						}
						public void setR6_SCVRN(String r6_SCVRN) {
							R6_SCVRN = r6_SCVRN;
						}
						public BigDecimal getR6_AGGREGATE_BALANCE() {
							return R6_AGGREGATE_BALANCE;
						}
						public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
							R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
						}
						public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
							return R6_COMPENSATABLE_AMOUNT;
						}
						public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
							R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
						}
						public String getR7_SCVRN() {
							return R7_SCVRN;
						}
						public void setR7_SCVRN(String r7_SCVRN) {
							R7_SCVRN = r7_SCVRN;
						}
						public BigDecimal getR7_AGGREGATE_BALANCE() {
							return R7_AGGREGATE_BALANCE;
						}
						public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
							R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
						}
						public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
							return R7_COMPENSATABLE_AMOUNT;
						}
						public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
							R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
						}
						public String getR8_SCVRN() {
							return R8_SCVRN;
						}
						public void setR8_SCVRN(String r8_SCVRN) {
							R8_SCVRN = r8_SCVRN;
						}
						public BigDecimal getR8_AGGREGATE_BALANCE() {
							return R8_AGGREGATE_BALANCE;
						}
						public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
							R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
						}
						public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
							return R8_COMPENSATABLE_AMOUNT;
						}
						public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
							R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
						}
						public String getR9_SCVRN() {
							return R9_SCVRN;
						}
						public void setR9_SCVRN(String r9_SCVRN) {
							R9_SCVRN = r9_SCVRN;
						}
						public BigDecimal getR9_AGGREGATE_BALANCE() {
							return R9_AGGREGATE_BALANCE;
						}
						public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
							R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
						}
						public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
							return R9_COMPENSATABLE_AMOUNT;
						}
						public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
							R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
						}
						public String getR10_SCVRN() {
							return R10_SCVRN;
						}
						public void setR10_SCVRN(String r10_SCVRN) {
							R10_SCVRN = r10_SCVRN;
						}
						public BigDecimal getR10_AGGREGATE_BALANCE() {
							return R10_AGGREGATE_BALANCE;
						}
						public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
							R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
						}
						public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
							return R10_COMPENSATABLE_AMOUNT;
						}
						public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
							R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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
					
					
		 			// ROW MAPPER RESUB DETAIL

					class BDISB3RowMapper_ResubDetail implements RowMapper<BDISB3_RESUB_Detail_Entity> {

						@Override
						public BDISB3_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

							BDISB3_RESUB_Detail_Entity obj = new BDISB3_RESUB_Detail_Entity();	
							
							// -------- R5 --------
							obj.setR5_SCVRN(rs.getString("R5_SCVRN"));
							obj.setR5_AGGREGATE_BALANCE(rs.getBigDecimal("R5_AGGREGATE_BALANCE"));
							obj.setR5_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R5_COMPENSATABLE_AMOUNT"));

							// -------- R6 --------
							obj.setR6_SCVRN(rs.getString("R6_SCVRN"));
							obj.setR6_AGGREGATE_BALANCE(rs.getBigDecimal("R6_AGGREGATE_BALANCE"));
							obj.setR6_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R6_COMPENSATABLE_AMOUNT"));

							// -------- R7 --------
							obj.setR7_SCVRN(rs.getString("R7_SCVRN"));
							obj.setR7_AGGREGATE_BALANCE(rs.getBigDecimal("R7_AGGREGATE_BALANCE"));
							obj.setR7_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R7_COMPENSATABLE_AMOUNT"));

							// -------- R8 --------
							obj.setR8_SCVRN(rs.getString("R8_SCVRN"));
							obj.setR8_AGGREGATE_BALANCE(rs.getBigDecimal("R8_AGGREGATE_BALANCE"));
							obj.setR8_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R8_COMPENSATABLE_AMOUNT"));

							// -------- R9 --------
							obj.setR9_SCVRN(rs.getString("R9_SCVRN"));
							obj.setR9_AGGREGATE_BALANCE(rs.getBigDecimal("R9_AGGREGATE_BALANCE"));
							obj.setR9_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R9_COMPENSATABLE_AMOUNT"));

							// -------- R10 --------
							obj.setR10_SCVRN(rs.getString("R10_SCVRN"));
							obj.setR10_AGGREGATE_BALANCE(rs.getBigDecimal("R10_AGGREGATE_BALANCE"));
							obj.setR10_COMPENSATABLE_AMOUNT(rs.getBigDecimal("R10_COMPENSATABLE_AMOUNT"));
							
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
					
					public static class BDISB3_RESUB_Detail_Entity {
						
						public String R5_SCVRN;
					    public BigDecimal R5_AGGREGATE_BALANCE;
					    public BigDecimal R5_COMPENSATABLE_AMOUNT;

					    // -------- R6 --------
					    public String R6_SCVRN;
					    public BigDecimal R6_AGGREGATE_BALANCE;
					    public BigDecimal R6_COMPENSATABLE_AMOUNT;

					    // -------- R7 --------
					    public String R7_SCVRN;
					    public BigDecimal R7_AGGREGATE_BALANCE;
					    public BigDecimal R7_COMPENSATABLE_AMOUNT;

					    // -------- R8 --------
					    public String R8_SCVRN;
					    public BigDecimal R8_AGGREGATE_BALANCE;
					    public BigDecimal R8_COMPENSATABLE_AMOUNT;

					    // -------- R9 --------
					    public String R9_SCVRN;
					    public BigDecimal R9_AGGREGATE_BALANCE;
					    public BigDecimal R9_COMPENSATABLE_AMOUNT;

					    // -------- R10 --------
					    public String R10_SCVRN;
					    public BigDecimal R10_AGGREGATE_BALANCE;
					    public BigDecimal R10_COMPENSATABLE_AMOUNT;
					    
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
						
						public String getR5_SCVRN() {
							return R5_SCVRN;
						}
						public void setR5_SCVRN(String r5_SCVRN) {
							R5_SCVRN = r5_SCVRN;
						}
						
						public BigDecimal getR5_AGGREGATE_BALANCE() {
							return R5_AGGREGATE_BALANCE;
						}
						public void setR5_AGGREGATE_BALANCE(BigDecimal r5_AGGREGATE_BALANCE) {
							R5_AGGREGATE_BALANCE = r5_AGGREGATE_BALANCE;
						}
						public BigDecimal getR5_COMPENSATABLE_AMOUNT() {
							return R5_COMPENSATABLE_AMOUNT;
						}
						public void setR5_COMPENSATABLE_AMOUNT(BigDecimal r5_COMPENSATABLE_AMOUNT) {
							R5_COMPENSATABLE_AMOUNT = r5_COMPENSATABLE_AMOUNT;
						}
						public String getR6_SCVRN() {
							return R6_SCVRN;
						}
						public void setR6_SCVRN(String r6_SCVRN) {
							R6_SCVRN = r6_SCVRN;
						}
						public BigDecimal getR6_AGGREGATE_BALANCE() {
							return R6_AGGREGATE_BALANCE;
						}
						public void setR6_AGGREGATE_BALANCE(BigDecimal r6_AGGREGATE_BALANCE) {
							R6_AGGREGATE_BALANCE = r6_AGGREGATE_BALANCE;
						}
						public BigDecimal getR6_COMPENSATABLE_AMOUNT() {
							return R6_COMPENSATABLE_AMOUNT;
						}
						public void setR6_COMPENSATABLE_AMOUNT(BigDecimal r6_COMPENSATABLE_AMOUNT) {
							R6_COMPENSATABLE_AMOUNT = r6_COMPENSATABLE_AMOUNT;
						}
						public String getR7_SCVRN() {
							return R7_SCVRN;
						}
						public void setR7_SCVRN(String r7_SCVRN) {
							R7_SCVRN = r7_SCVRN;
						}
						public BigDecimal getR7_AGGREGATE_BALANCE() {
							return R7_AGGREGATE_BALANCE;
						}
						public void setR7_AGGREGATE_BALANCE(BigDecimal r7_AGGREGATE_BALANCE) {
							R7_AGGREGATE_BALANCE = r7_AGGREGATE_BALANCE;
						}
						public BigDecimal getR7_COMPENSATABLE_AMOUNT() {
							return R7_COMPENSATABLE_AMOUNT;
						}
						public void setR7_COMPENSATABLE_AMOUNT(BigDecimal r7_COMPENSATABLE_AMOUNT) {
							R7_COMPENSATABLE_AMOUNT = r7_COMPENSATABLE_AMOUNT;
						}
						public String getR8_SCVRN() {
							return R8_SCVRN;
						}
						public void setR8_SCVRN(String r8_SCVRN) {
							R8_SCVRN = r8_SCVRN;
						}
						public BigDecimal getR8_AGGREGATE_BALANCE() {
							return R8_AGGREGATE_BALANCE;
						}
						public void setR8_AGGREGATE_BALANCE(BigDecimal r8_AGGREGATE_BALANCE) {
							R8_AGGREGATE_BALANCE = r8_AGGREGATE_BALANCE;
						}
						public BigDecimal getR8_COMPENSATABLE_AMOUNT() {
							return R8_COMPENSATABLE_AMOUNT;
						}
						public void setR8_COMPENSATABLE_AMOUNT(BigDecimal r8_COMPENSATABLE_AMOUNT) {
							R8_COMPENSATABLE_AMOUNT = r8_COMPENSATABLE_AMOUNT;
						}
						public String getR9_SCVRN() {
							return R9_SCVRN;
						}
						public void setR9_SCVRN(String r9_SCVRN) {
							R9_SCVRN = r9_SCVRN;
						}
						public BigDecimal getR9_AGGREGATE_BALANCE() {
							return R9_AGGREGATE_BALANCE;
						}
						public void setR9_AGGREGATE_BALANCE(BigDecimal r9_AGGREGATE_BALANCE) {
							R9_AGGREGATE_BALANCE = r9_AGGREGATE_BALANCE;
						}
						public BigDecimal getR9_COMPENSATABLE_AMOUNT() {
							return R9_COMPENSATABLE_AMOUNT;
						}
						public void setR9_COMPENSATABLE_AMOUNT(BigDecimal r9_COMPENSATABLE_AMOUNT) {
							R9_COMPENSATABLE_AMOUNT = r9_COMPENSATABLE_AMOUNT;
						}
						public String getR10_SCVRN() {
							return R10_SCVRN;
						}
						public void setR10_SCVRN(String r10_SCVRN) {
							R10_SCVRN = r10_SCVRN;
						}
						public BigDecimal getR10_AGGREGATE_BALANCE() {
							return R10_AGGREGATE_BALANCE;
						}
						public void setR10_AGGREGATE_BALANCE(BigDecimal r10_AGGREGATE_BALANCE) {
							R10_AGGREGATE_BALANCE = r10_AGGREGATE_BALANCE;
						}
						public BigDecimal getR10_COMPENSATABLE_AMOUNT() {
							return R10_COMPENSATABLE_AMOUNT;
						}
						public void setR10_COMPENSATABLE_AMOUNT(BigDecimal r10_COMPENSATABLE_AMOUNT) {
							R10_COMPENSATABLE_AMOUNT = r10_COMPENSATABLE_AMOUNT;
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
						
     
    
    
    
    
	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	public ModelAndView getBRRS_BDISB3View(String reportId, String fromdate, String todate, String currency, String dtltype,
			Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();
		Session hs = sessionFactory.getCurrentSession();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		try {
			Date dt = dateformat.parse(todate);

			System.out.println("======= VIEW DEBUG =======");
			System.out.println("TYPE      : " + type);
			System.out.println("DTLTYPE   : " + dtltype);
			System.out.println("DATE      : " + dt);
			System.out.println("VERSION   : " + version);
			System.out.println("==========================");

			// ===========================================================
			// SUMMARY SECTION
			// ===========================================================

			// ---------- CASE 1: ARCHIVAL ----------
			if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

				List<BDISB3_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<BDISB3_RESUB_Summary_Entity> T1Master = getdatabydateListresub1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<BDISB3_Summary_Entity> T1Master = getDataByDate1(dt);
				
				System.out.println("T1Master Size: " + T1Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<BDISB3_Archival_Detail_Entity> T1Master = getdatabydateListArchivalDetail1(dt, version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<BDISB3_RESUB_Detail_Entity> T1Master = getdatabydateListResubDetail1(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);

				}
				// DETAIL + NORMAL
				else {

					List<BDISB3_Detail_Entity> T1Master = getDetaildatabydateList1(dt);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB3");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}
	

	
	public void updateResubReport(
	        BDISB3_RESUB_Summary_Entity updatedEntity1) {

	    // ====================================================
	    // 1️⃣ GET REPORT DATE
	    // ====================================================

	    Date reportDate1 = updatedEntity1.getREPORT_DATE();

	    if (reportDate1 == null ) {
	        throw new RuntimeException("Report date cannot be null");
	    }

	    // ====================================================
	    // 2️⃣ FETCH MAX VERSION
	    // ====================================================

	    BigDecimal maxVer1 = RESUBfindMaxVersion1(reportDate1);

	    if (maxVer1 == null)
	        maxVer1 = BigDecimal.ZERO;

	   

	    BigDecimal currentMax = maxVer1;
	    BigDecimal newVersion = currentMax.add(BigDecimal.ONE);

	    Date now = new Date();

	    // ====================================================
	    // 3️⃣ RESUB SUMMARY
	    // ====================================================

	    BDISB3_RESUB_Summary_Entity resubSummary1 = new BDISB3_RESUB_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubSummary1);

	    resubSummary1.setREPORT_DATE(reportDate1);
	    resubSummary1.setREPORT_VERSION(newVersion);
	    resubSummary1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 4️⃣ RESUB DETAIL
	    // ====================================================

	    BDISB3_RESUB_Detail_Entity resubDetail1 = new BDISB3_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubDetail1);

	    resubDetail1.setREPORT_DATE(reportDate1);
	    resubDetail1.setREPORT_VERSION(newVersion);
	    resubDetail1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 5️⃣ ARCHIVAL SUMMARY
	    // ====================================================

	    BDISB3_Archival_Summary_Entity archSummary1 = new BDISB3_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, archSummary1);

	    archSummary1.setREPORT_DATE(reportDate1);
	    archSummary1.setREPORT_VERSION(newVersion);
	    archSummary1.setREPORT_RESUBDATE(now);


	    // ====================================================
	    // 6️⃣ ARCHIVAL DETAIL
	    // ====================================================

	    BDISB3_Archival_Detail_Entity archDetail1 = new BDISB3_Archival_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity1, archDetail1);

	    archDetail1.setREPORT_DATE(reportDate1);
	    archDetail1.setREPORT_VERSION(newVersion);
	    archDetail1.setREPORT_RESUBDATE(now);

	   

	    // ====================================================
	    // 7️⃣ SAVE ALL
	    // ====================================================

	    sessionFactory.getCurrentSession().merge(resubSummary1);

	    sessionFactory.getCurrentSession().merge(resubDetail1);

	    sessionFactory.getCurrentSession().merge(archSummary1);

	    sessionFactory.getCurrentSession().merge(archDetail1);
	}
	
	@Transactional
	public void updateReport(BDISB3_Summary_Entity request) {

	    try {

	        StringBuilder sql = new StringBuilder(
	                "UPDATE BRRS_BDISB3_SUMMARYTABLE SET ");

	        List<Object> params = new ArrayList<>();

	        for (int i = 5; i <= 10; i++) {

	            sql.append("R").append(i).append("_SCVRN=?,")
	               .append("R").append(i).append("_AGGREGATE_BALANCE=?,")
	               .append("R").append(i).append("_COMPENSATABLE_AMOUNT=?,");

	            params.add(getValue(request, "getR" + i + "_SCVRN"));
	            params.add(getValue(request, "getR" + i + "_AGGREGATE_BALANCE"));
	            params.add(getValue(request, "getR" + i + "_COMPENSATABLE_AMOUNT"));
	        }

	        // Remove last comma
	        sql.deleteCharAt(sql.length() - 1);

	        sql.append(" WHERE REPORT_DATE=?");

	        params.add(request.getREPORT_DATE());

	        int count = jdbcTemplate.update(sql.toString(), params.toArray());

	        System.out.println("Rows Updated = " + count);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while updating BRRS_BDISB3 Report", e);
	    }
	}

	private Object getValue(Object obj, String methodName) {
	    try {
	        return obj.getClass().getMethod(methodName).invoke(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
//////////////////////////////////////////RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getBRRS_BDISB3Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<BDISB3_Archival_Summary_Entity> latestArchivalList = getdatabydateListWithVersion1();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (BDISB3_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BRRS_BDISB3 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getBRRS_BDISB3Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " + "FROM BRRS_BDISB3_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}
	
	
	public byte[] getBDISB3Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null ) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelBDISB3ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null ) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<BDISB3_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dateformat.parse(todate), version);


// Generate Excel for RESUB
			return BRRS_BDISB3ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

// Default (LIVE) case
		List<BDISB3_Summary_Entity> dataList1 = getDataByDate1(reportDate);

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
//numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("0.000"));
			numberStyle.setBorderBottom(BorderStyle.THIN);
			numberStyle.setBorderTop(BorderStyle.THIN);
			numberStyle.setBorderLeft(BorderStyle.THIN);
			numberStyle.setBorderRight(BorderStyle.THIN);
			numberStyle.setFont(font);
// --- End of Style Definitions ---

			int startRow = 4;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					BDISB3_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//Cell0 - R5_RECORD_NUMBER
					Cell cell1 = row.createCell(1);
					if (record.getR5_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR5_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record.getR5_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR5_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
//------------------------- R6 -------------------------
					cell1 = row.createCell(1);
					if (record.getR6_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR6_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR6_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR6_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
//------------------------- R7 -------------------------
					cell1 = row.createCell(1);
					if (record.getR7_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR7_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR7_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR7_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
//------------------------- R8 -------------------------
					cell1 = row.createCell(1);
					if (record.getR8_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR8_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR8_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR8_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(8);
//------------------------- R9 -------------------------
					cell1 = row.createCell(1);
					if (record.getR9_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR9_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR9_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR9_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
//------------------------- R10 -------------------------
					cell1 = row.createCell(1);
					if (record.getR10_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record.getR10_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record.getR10_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record.getR10_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
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

	public byte[] getExcelBDISB3ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<BDISB3_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB3 report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					BDISB3_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// Cell0 - R5_record1_NUMBER
					Cell cell1 = row.createCell(1);
					if (record1.getR5_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR5_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR5_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR5_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
					// ------------------------- R6 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR6_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR6_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR6_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR6_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
					// ------------------------- R7 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR7_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR7_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR7_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR7_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
					// ------------------------- R8 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR8_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR8_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR8_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR8_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(8);
					// ------------------------- R9 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR9_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR9_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR9_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR9_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					// ------------------------- R10 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR10_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR10_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR10_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR10_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
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



/// Downloaded for Archival & Resub
	public byte[] BRRS_BDISB3ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<BDISB3_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for BDISB3 report. Returning empty result.");
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

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					BDISB3_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//Cell0 - R5_record1_NUMBER
					Cell cell1 = row.createCell(1);
					if (record1.getR5_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR5_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					Cell cell2 = row.createCell(2);
					if (record1.getR5_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR5_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
//------------------------- R6 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR6_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR6_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR6_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR6_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
//------------------------- R7 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR7_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR7_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR7_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR7_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
//------------------------- R8 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR8_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR8_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR8_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR8_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(8);
//------------------------- R9 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR9_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR9_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR9_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR9_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
//------------------------- R10 -------------------------
					cell1 = row.createCell(1);
					if (record1.getR10_AGGREGATE_BALANCE() != null) {
						cell1.setCellValue(record1.getR10_AGGREGATE_BALANCE().doubleValue());
						cell1.setCellStyle(numberStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					cell2 = row.createCell(2);
					if (record1.getR10_COMPENSATABLE_AMOUNT() != null) {
						cell2.setCellValue(record1.getR10_COMPENSATABLE_AMOUNT().doubleValue());
						cell2.setCellStyle(numberStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
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
