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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Component
@Service

public class BRRS_BDISB1_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_BDISB1_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	AuditService auditService;

	@Autowired
	SessionFactory sessionFactory;
	
	// ENTITY MANAGER (Acts like Repository)
    @PersistenceContext
	private EntityManager entityManager;
				
				
	// SUMMARY
	// Fetch data by report date
	public List<BDISB1_Summary_Entity> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB1_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB1_RowMapper_Summary());
	}			

	
	// ARCHIVAL

	// Fetch data by report date
	public List<BDISB1_Archival_Summary_Entity> ArchivalgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB1_RowMapper_Archival());
	}
    
	// RESUB

	// Fetch data by report date
	public List<BDISB1_RESUB_Summary_Entity> ResubgetDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_BDISB1_RESUB_SUMMARYTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new BDISB1_RowMapper_Resub());
	}
    
	/*
	 * // ARCHIVAL // GET REPORT_DATE + REPORT_VERSION
	 * 
	 * public List<Object[]> getBDISB1Archival() {
	 * 
	 * String sql = "SELECT REPORT_DATE, REPORT_VERSION " +
	 * "FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY" + "ORDER BY REPORT_VERSION";
	 * 
	 * return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] {
	 * rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") }); }
	 */
	
	//GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<BDISB1_Archival_Summary_Entity> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB1_RowMapper_Archival());
	}
    
	//GET RESUB FULL DATA BY DATE + VERSION

	public List<BDISB1_RESUB_Summary_Entity> getdatabydateListresub1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB1_RESUB_SUMMARYTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB1_RowMapper_Resub());
	}
	
	//GET DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB1_Detail_Entity> getdatabydateListDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE" + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB1RowMapper_Detail());
	}
	
	//GET ARCHIVAL DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB1_Archival_Detail_Entity> getdatabydateListArchivalDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB1RowMapper_ArchivalDetail());
	}
	
	
	//GET RESUB DETAIL FULL DATA BY DATE + VERSION

	public List<BDISB1_RESUB_Detail_Entity> getdatabydateListResubDetail1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION }, new BDISB1RowMapper_ResubDetail());
	}
	
	//GET ALL WITH VERSION

	public List<BDISB1_Archival_Summary_Entity> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new BDISB1_RowMapper_Archival());
	}
	
	//GET RESUB ALL WITH VERSION

	public List<BDISB1_RESUB_Summary_Entity> ResubgetdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_BDISB1_RESUB_SUMMARYTABLE " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new BDISB1_RowMapper_Resub());
	}
	
	//GET ARCHIVAL MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY"
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}
	
	// GET RESUB MAX VERSION BY DATE

	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_BDISB1_RESUBTABLE_SUMMARY "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}
	
	
	//DETAIL TABLE 1
	// 1. BY DATE + LABEL + CRITERIA

	public List<BDISB1_Detail_Entity> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new BDISB1RowMapper_Detail());
	}	
	
	// 2. GET ALL (BY DATE - simple)

	public List<BDISB1_Detail_Entity> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB1RowMapper_Detail());
	}

// 3. PAGINATION

	public List<BDISB1_Detail_Entity> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB1RowMapper_Detail());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_BDISB1_DETAILTABLE WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<BDISB1_Detail_Entity> GetDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new BDISB1RowMapper_Detail());
	}
	
// 6. BY ACCOUNT NUMBER

	public BDISB1_Detail_Entity findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_BDISB1_DETAILTABLE WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB1RowMapper_Detail());
	}	
	
	
	
	//ARCHIVALTABLE_DETAIL 
	// 1. BY DATE + LABEL + CRITERIA

	public List<BDISB1_Archival_Detail_Entity> findByArchivalDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new BDISB1RowMapper_ArchivalDetail());
	}	
	
	// 2. GET ALL (BY DATE - simple)

	public List<BDISB1_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB1RowMapper_ArchivalDetail());
	}

// 3. PAGINATION

	public List<BDISB1_Archival_Detail_Entity> getArchivalDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB1RowMapper_ArchivalDetail());
	}

	// 4. COUNT

	public int getArchivalDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

// 5. BY LABEL + CRITERIA

	public List<BDISB1_Archival_Detail_Entity> GetArchivalDetailDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new BDISB1RowMapper_ArchivalDetail());
	}
// 6. BY ACCOUNT NUMBER

	public BDISB1_Archival_Detail_Entity ArchivalfindByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_BDISB1_ARCHIVALTABLE_DETAIL WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB1RowMapper_ArchivalDetail());
	}	
	
	
	
	//RESUBTABLE_DETAIL 
				// 1. BY DATE + LABEL + CRITERIA

				public List<BDISB1_RESUB_Detail_Entity> findByResubReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
						String reportAddlCriteria1) {

					String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE "
							+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

					return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
							new BDISB1RowMapper_ResubDetail());
				}	
				
				// 2. GET ALL (BY DATE - simple)

				public List<BDISB1_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate) {

					String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

					return jdbcTemplate.query(sql, new Object[] { reportdate }, new BDISB1RowMapper_ResubDetail());
				}

			// 3. PAGINATION

				public List<BDISB1_RESUB_Detail_Entity> getResubdatabydateList1(Date reportdate, int offset, int limit) {

					String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE "
							+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

					return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit }, new BDISB1RowMapper_ResubDetail());
				}
			
				// 4. COUNT

				public int getResubdatacount1(Date reportdate) {

					String sql = "SELECT COUNT(*) FROM BRRS_BDISB1_RESUB_DETAILTABLE WHERE REPORT_DATE = ?";

					return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
				}

			// 5. BY LABEL + CRITERIA

				public List<BDISB1_RESUB_Detail_Entity> GetResubDataByRowIdAndColumnId1(String reportLabel,
						String reportAddlCriteria1, Date reportdate) {

					String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE "
							+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

					return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
							new BDISB1RowMapper_ResubDetail());
				}
			// 6. BY ACCOUNT NUMBER

				public BDISB1_RESUB_Detail_Entity ResubfindByAcctnumber1(String acctNumber) {

					String sql = "SELECT * FROM BRRS_BDISB1_RESUB_DETAILTABLE WHERE ACCT_NUMBER = ?";

					return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new BDISB1RowMapper_ResubDetail());
				}	
				
				
				//findSummaryByReportDate
				
				@Transactional(readOnly = true)
				public BDISB1_Summary_Entity findSummaryByReportDate(Date reportDate) {

				    String sql =
				            "SELECT * FROM BRRS_BDISB1_SUMMARYTABLE " +
				            "WHERE REPORT_DATE = ?";

				    List<BDISB1_Summary_Entity> list =
				            jdbcTemplate.query(
				                    sql,
				                    new Object[] { reportDate },
				                    new BDISB1_RowMapper_Summary());

				    return list.isEmpty() ? null : list.get(0);
				}
				
				@Transactional(readOnly = true)
				public BDISB1_Detail_Entity findDetailByReportDate(Date reportDate) {

				    String sql =
				            "SELECT * FROM BRRS_BDISB1_DETAILTABLE " +
				            "WHERE REPORT_DATE = ?";

				    List<BDISB1_Detail_Entity> list =
				            jdbcTemplate.query(
				                    sql,
				                    new Object[] { reportDate },
				                    new BDISB1RowMapper_Detail());

				    return list.isEmpty() ? null : list.get(0);
				}
							
	
				// ROW MAPPER SUMMARY

				class BDISB1_RowMapper_Summary implements RowMapper<BDISB1_Summary_Entity> {

					@Override
					public BDISB1_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_Summary_Entity obj = new BDISB1_Summary_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_Summary_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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

				class BDISB1RowMapper_Detail implements RowMapper<BDISB1_Detail_Entity> {

					@Override
					public BDISB1_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_Detail_Entity obj = new BDISB1_Detail_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_Detail_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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

				class BDISB1_RowMapper_Archival implements RowMapper<BDISB1_Archival_Summary_Entity> {

					@Override
					public BDISB1_Archival_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_Archival_Summary_Entity obj = new BDISB1_Archival_Summary_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_Archival_Summary_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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

				class BDISB1RowMapper_ArchivalDetail implements RowMapper<BDISB1_Archival_Detail_Entity> {

					@Override
					public BDISB1_Archival_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_Archival_Detail_Entity obj = new BDISB1_Archival_Detail_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_Archival_Detail_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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
					
					
				// ROW MAPPER RESUB

				class BDISB1_RowMapper_Resub implements RowMapper<BDISB1_RESUB_Summary_Entity> {

					@Override
					public BDISB1_RESUB_Summary_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_RESUB_Summary_Entity obj = new BDISB1_RESUB_Summary_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_RESUB_Summary_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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
					
					
				// ROW MAPPER RESUB Detail

				class BDISB1RowMapper_ResubDetail implements RowMapper<BDISB1_RESUB_Detail_Entity> {

					@Override
					public BDISB1_RESUB_Detail_Entity mapRow(ResultSet rs, int rowNum) throws SQLException {

						BDISB1_RESUB_Detail_Entity obj = new BDISB1_RESUB_Detail_Entity();	
					
	
						obj.setR5_RECORD_NUMBER(rs.getString("R5_RECORD_NUMBER"));
						obj.setR5_TITLE(rs.getString("R5_TITLE"));
						obj.setR5_FIRST_NAME(rs.getString("R5_FIRST_NAME"));
						obj.setR5_MIDDLE_NAME(rs.getString("R5_MIDDLE_NAME"));
						obj.setR5_SURNAME(rs.getString("R5_SURNAME"));
						obj.setR5_PREVIOUS_NAME(rs.getString("R5_PREVIOUS_NAME"));
						obj.setR5_GENDER(rs.getString("R5_GENDER"));
						obj.setR5_IDENTIFICATION_TYPE(rs.getString("R5_IDENTIFICATION_TYPE"));
						obj.setR5_PASSPORT_NUMBER(rs.getString("R5_PASSPORT_NUMBER"));
						obj.setR5_DATE_OF_BIRTH(rs.getDate("R5_DATE_OF_BIRTH"));
						obj.setR5_HOME_ADDRESS(rs.getString("R5_HOME_ADDRESS"));
						obj.setR5_POSTAL_ADDRESS(rs.getString("R5_POSTAL_ADDRESS"));
						obj.setR5_RESIDENCE(rs.getString("R5_RESIDENCE"));
						obj.setR5_EMAIL(rs.getString("R5_EMAIL"));
						obj.setR5_LANDLINE(rs.getString("R5_LANDLINE"));
						obj.setR5_MOBILE_PHONE_NUMBER(rs.getString("R5_MOBILE_PHONE_NUMBER"));
						obj.setR5_MOBILE_MONEY_NUMBER(rs.getString("R5_MOBILE_MONEY_NUMBER"));
						obj.setR5_PRODUCT_TYPE(rs.getString("R5_PRODUCT_TYPE"));
						obj.setR5_ACCOUNT_BY_OWNERSHIP(rs.getString("R5_ACCOUNT_BY_OWNERSHIP"));
						obj.setR5_ACCOUNT_NUMBER(rs.getString("R5_ACCOUNT_NUMBER"));
						obj.setR5_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R5_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR5_STATUS_OF_ACCOUNT(rs.getString("R5_STATUS_OF_ACCOUNT"));
						obj.setR5_NOT_FIT_FOR_STP(rs.getString("R5_NOT_FIT_FOR_STP"));
						obj.setR5_BRANCH_CODE_AND_NAME(rs.getString("R5_BRANCH_CODE_AND_NAME"));
						obj.setR5_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R5_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR5_CURRENCY_OF_ACCOUNT(rs.getString("R5_CURRENCY_OF_ACCOUNT"));
						obj.setR5_EXCHANGE_RATE(rs.getBigDecimal("R5_EXCHANGE_RATE"));
						
						obj.setR6_RECORD_NUMBER(rs.getString("R6_RECORD_NUMBER"));
						obj.setR6_TITLE(rs.getString("R6_TITLE"));
						obj.setR6_FIRST_NAME(rs.getString("R6_FIRST_NAME"));
						obj.setR6_MIDDLE_NAME(rs.getString("R6_MIDDLE_NAME"));
						obj.setR6_SURNAME(rs.getString("R6_SURNAME"));
						obj.setR6_PREVIOUS_NAME(rs.getString("R6_PREVIOUS_NAME"));
						obj.setR6_GENDER(rs.getString("R6_GENDER"));
						obj.setR6_IDENTIFICATION_TYPE(rs.getString("R6_IDENTIFICATION_TYPE"));
						obj.setR6_PASSPORT_NUMBER(rs.getString("R6_PASSPORT_NUMBER"));
						obj.setR6_DATE_OF_BIRTH(rs.getDate("R6_DATE_OF_BIRTH"));
						obj.setR6_HOME_ADDRESS(rs.getString("R6_HOME_ADDRESS"));
						obj.setR6_POSTAL_ADDRESS(rs.getString("R6_POSTAL_ADDRESS"));
						obj.setR6_RESIDENCE(rs.getString("R6_RESIDENCE"));
						obj.setR6_EMAIL(rs.getString("R6_EMAIL"));
						obj.setR6_LANDLINE(rs.getString("R6_LANDLINE"));
						obj.setR6_MOBILE_PHONE_NUMBER(rs.getString("R6_MOBILE_PHONE_NUMBER"));
						obj.setR6_MOBILE_MONEY_NUMBER(rs.getString("R6_MOBILE_MONEY_NUMBER"));
						obj.setR6_PRODUCT_TYPE(rs.getString("R6_PRODUCT_TYPE"));
						obj.setR6_ACCOUNT_BY_OWNERSHIP(rs.getString("R6_ACCOUNT_BY_OWNERSHIP"));
						obj.setR6_ACCOUNT_NUMBER(rs.getString("R6_ACCOUNT_NUMBER"));
						obj.setR6_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R6_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR6_STATUS_OF_ACCOUNT(rs.getString("R6_STATUS_OF_ACCOUNT"));
						obj.setR6_NOT_FIT_FOR_STP(rs.getString("R6_NOT_FIT_FOR_STP"));
						obj.setR6_BRANCH_CODE_AND_NAME(rs.getString("R6_BRANCH_CODE_AND_NAME"));
						obj.setR6_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R6_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR6_CURRENCY_OF_ACCOUNT(rs.getString("R6_CURRENCY_OF_ACCOUNT"));
						obj.setR6_EXCHANGE_RATE(rs.getBigDecimal("R6_EXCHANGE_RATE"));
						
						obj.setR7_RECORD_NUMBER(rs.getString("R7_RECORD_NUMBER"));
						obj.setR7_TITLE(rs.getString("R7_TITLE"));
						obj.setR7_FIRST_NAME(rs.getString("R7_FIRST_NAME"));
						obj.setR7_MIDDLE_NAME(rs.getString("R7_MIDDLE_NAME"));
						obj.setR7_SURNAME(rs.getString("R7_SURNAME"));
						obj.setR7_PREVIOUS_NAME(rs.getString("R7_PREVIOUS_NAME"));
						obj.setR7_GENDER(rs.getString("R7_GENDER"));
						obj.setR7_IDENTIFICATION_TYPE(rs.getString("R7_IDENTIFICATION_TYPE"));
						obj.setR7_PASSPORT_NUMBER(rs.getString("R7_PASSPORT_NUMBER"));
						obj.setR7_DATE_OF_BIRTH(rs.getDate("R7_DATE_OF_BIRTH"));
						obj.setR7_HOME_ADDRESS(rs.getString("R7_HOME_ADDRESS"));
						obj.setR7_POSTAL_ADDRESS(rs.getString("R7_POSTAL_ADDRESS"));
						obj.setR7_RESIDENCE(rs.getString("R7_RESIDENCE"));
						obj.setR7_EMAIL(rs.getString("R7_EMAIL"));
						obj.setR7_LANDLINE(rs.getString("R7_LANDLINE"));
						obj.setR7_MOBILE_PHONE_NUMBER(rs.getString("R7_MOBILE_PHONE_NUMBER"));
						obj.setR7_MOBILE_MONEY_NUMBER(rs.getString("R7_MOBILE_MONEY_NUMBER"));
						obj.setR7_PRODUCT_TYPE(rs.getString("R7_PRODUCT_TYPE"));
						obj.setR7_ACCOUNT_BY_OWNERSHIP(rs.getString("R7_ACCOUNT_BY_OWNERSHIP"));
						obj.setR7_ACCOUNT_NUMBER(rs.getString("R7_ACCOUNT_NUMBER"));
						obj.setR7_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R7_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR7_STATUS_OF_ACCOUNT(rs.getString("R7_STATUS_OF_ACCOUNT"));
						obj.setR7_NOT_FIT_FOR_STP(rs.getString("R7_NOT_FIT_FOR_STP"));
						obj.setR7_BRANCH_CODE_AND_NAME(rs.getString("R7_BRANCH_CODE_AND_NAME"));
						obj.setR7_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R7_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR7_CURRENCY_OF_ACCOUNT(rs.getString("R7_CURRENCY_OF_ACCOUNT"));
						obj.setR7_EXCHANGE_RATE(rs.getBigDecimal("R7_EXCHANGE_RATE"));
						
						obj.setR8_RECORD_NUMBER(rs.getString("R8_RECORD_NUMBER"));
						obj.setR8_TITLE(rs.getString("R8_TITLE"));
						obj.setR8_FIRST_NAME(rs.getString("R8_FIRST_NAME"));
						obj.setR8_MIDDLE_NAME(rs.getString("R8_MIDDLE_NAME"));
						obj.setR8_SURNAME(rs.getString("R8_SURNAME"));
						obj.setR8_PREVIOUS_NAME(rs.getString("R8_PREVIOUS_NAME"));
						obj.setR8_GENDER(rs.getString("R8_GENDER"));
						obj.setR8_IDENTIFICATION_TYPE(rs.getString("R8_IDENTIFICATION_TYPE"));
						obj.setR8_PASSPORT_NUMBER(rs.getString("R8_PASSPORT_NUMBER"));
						obj.setR8_DATE_OF_BIRTH(rs.getDate("R8_DATE_OF_BIRTH"));
						obj.setR8_HOME_ADDRESS(rs.getString("R8_HOME_ADDRESS"));
						obj.setR8_POSTAL_ADDRESS(rs.getString("R8_POSTAL_ADDRESS"));
						obj.setR8_RESIDENCE(rs.getString("R8_RESIDENCE"));
						obj.setR8_EMAIL(rs.getString("R8_EMAIL"));
						obj.setR8_LANDLINE(rs.getString("R8_LANDLINE"));
						obj.setR8_MOBILE_PHONE_NUMBER(rs.getString("R8_MOBILE_PHONE_NUMBER"));
						obj.setR8_MOBILE_MONEY_NUMBER(rs.getString("R8_MOBILE_MONEY_NUMBER"));
						obj.setR8_PRODUCT_TYPE(rs.getString("R8_PRODUCT_TYPE"));
						obj.setR8_ACCOUNT_BY_OWNERSHIP(rs.getString("R8_ACCOUNT_BY_OWNERSHIP"));
						obj.setR8_ACCOUNT_NUMBER(rs.getString("R8_ACCOUNT_NUMBER"));
						obj.setR8_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R8_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR8_STATUS_OF_ACCOUNT(rs.getString("R8_STATUS_OF_ACCOUNT"));
						obj.setR8_NOT_FIT_FOR_STP(rs.getString("R8_NOT_FIT_FOR_STP"));
						obj.setR8_BRANCH_CODE_AND_NAME(rs.getString("R8_BRANCH_CODE_AND_NAME"));
						obj.setR8_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R8_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR8_CURRENCY_OF_ACCOUNT(rs.getString("R8_CURRENCY_OF_ACCOUNT"));
						obj.setR8_EXCHANGE_RATE(rs.getBigDecimal("R8_EXCHANGE_RATE"));
						
						obj.setR9_RECORD_NUMBER(rs.getString("R9_RECORD_NUMBER"));
						obj.setR9_TITLE(rs.getString("R9_TITLE"));
						obj.setR9_FIRST_NAME(rs.getString("R9_FIRST_NAME"));
						obj.setR9_MIDDLE_NAME(rs.getString("R9_MIDDLE_NAME"));
						obj.setR9_SURNAME(rs.getString("R9_SURNAME"));
						obj.setR9_PREVIOUS_NAME(rs.getString("R9_PREVIOUS_NAME"));
						obj.setR9_GENDER(rs.getString("R9_GENDER"));
						obj.setR9_IDENTIFICATION_TYPE(rs.getString("R9_IDENTIFICATION_TYPE"));
						obj.setR9_PASSPORT_NUMBER(rs.getString("R9_PASSPORT_NUMBER"));
						obj.setR9_DATE_OF_BIRTH(rs.getDate("R9_DATE_OF_BIRTH"));
						obj.setR9_HOME_ADDRESS(rs.getString("R9_HOME_ADDRESS"));
						obj.setR9_POSTAL_ADDRESS(rs.getString("R9_POSTAL_ADDRESS"));
						obj.setR9_RESIDENCE(rs.getString("R9_RESIDENCE"));
						obj.setR9_EMAIL(rs.getString("R9_EMAIL"));
						obj.setR9_LANDLINE(rs.getString("R9_LANDLINE"));
						obj.setR9_MOBILE_PHONE_NUMBER(rs.getString("R9_MOBILE_PHONE_NUMBER"));
						obj.setR9_MOBILE_MONEY_NUMBER(rs.getString("R9_MOBILE_MONEY_NUMBER"));
						obj.setR9_PRODUCT_TYPE(rs.getString("R9_PRODUCT_TYPE"));
						obj.setR9_ACCOUNT_BY_OWNERSHIP(rs.getString("R9_ACCOUNT_BY_OWNERSHIP"));
						obj.setR9_ACCOUNT_NUMBER(rs.getString("R9_ACCOUNT_NUMBER"));
						obj.setR9_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R9_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR9_STATUS_OF_ACCOUNT(rs.getString("R9_STATUS_OF_ACCOUNT"));
						obj.setR9_NOT_FIT_FOR_STP(rs.getString("R9_NOT_FIT_FOR_STP"));
						obj.setR9_BRANCH_CODE_AND_NAME(rs.getString("R9_BRANCH_CODE_AND_NAME"));
						obj.setR9_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R9_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR9_CURRENCY_OF_ACCOUNT(rs.getString("R9_CURRENCY_OF_ACCOUNT"));
						obj.setR9_EXCHANGE_RATE(rs.getBigDecimal("R9_EXCHANGE_RATE"));
						
						obj.setR10_RECORD_NUMBER(rs.getString("R10_RECORD_NUMBER"));
						obj.setR10_TITLE(rs.getString("R10_TITLE"));
						obj.setR10_FIRST_NAME(rs.getString("R10_FIRST_NAME"));
						obj.setR10_MIDDLE_NAME(rs.getString("R10_MIDDLE_NAME"));
						obj.setR10_SURNAME(rs.getString("R10_SURNAME"));
						obj.setR10_PREVIOUS_NAME(rs.getString("R10_PREVIOUS_NAME"));
						obj.setR10_GENDER(rs.getString("R10_GENDER"));
						obj.setR10_IDENTIFICATION_TYPE(rs.getString("R10_IDENTIFICATION_TYPE"));
						obj.setR10_PASSPORT_NUMBER(rs.getString("R10_PASSPORT_NUMBER"));
						obj.setR10_DATE_OF_BIRTH(rs.getDate("R10_DATE_OF_BIRTH"));
						obj.setR10_HOME_ADDRESS(rs.getString("R10_HOME_ADDRESS"));
						obj.setR10_POSTAL_ADDRESS(rs.getString("R10_POSTAL_ADDRESS"));
						obj.setR10_RESIDENCE(rs.getString("R10_RESIDENCE"));
						obj.setR10_EMAIL(rs.getString("R10_EMAIL"));
						obj.setR10_LANDLINE(rs.getString("R10_LANDLINE"));
						obj.setR10_MOBILE_PHONE_NUMBER(rs.getString("R10_MOBILE_PHONE_NUMBER"));
						obj.setR10_MOBILE_MONEY_NUMBER(rs.getString("R10_MOBILE_MONEY_NUMBER"));
						obj.setR10_PRODUCT_TYPE(rs.getString("R10_PRODUCT_TYPE"));
						obj.setR10_ACCOUNT_BY_OWNERSHIP(rs.getString("R10_ACCOUNT_BY_OWNERSHIP"));
						obj.setR10_ACCOUNT_NUMBER(rs.getString("R10_ACCOUNT_NUMBER"));
						obj.setR10_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R10_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR10_STATUS_OF_ACCOUNT(rs.getString("R10_STATUS_OF_ACCOUNT"));
						obj.setR10_NOT_FIT_FOR_STP(rs.getString("R10_NOT_FIT_FOR_STP"));
						obj.setR10_BRANCH_CODE_AND_NAME(rs.getString("R10_BRANCH_CODE_AND_NAME"));
						obj.setR10_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R10_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR10_CURRENCY_OF_ACCOUNT(rs.getString("R10_CURRENCY_OF_ACCOUNT"));
						obj.setR10_EXCHANGE_RATE(rs.getBigDecimal("R10_EXCHANGE_RATE"));
						
						obj.setR11_RECORD_NUMBER(rs.getString("R11_RECORD_NUMBER"));
						obj.setR11_TITLE(rs.getString("R11_TITLE"));
						obj.setR11_FIRST_NAME(rs.getString("R11_FIRST_NAME"));
						obj.setR11_MIDDLE_NAME(rs.getString("R11_MIDDLE_NAME"));
						obj.setR11_SURNAME(rs.getString("R11_SURNAME"));
						obj.setR11_PREVIOUS_NAME(rs.getString("R11_PREVIOUS_NAME"));
						obj.setR11_GENDER(rs.getString("R11_GENDER"));
						obj.setR11_IDENTIFICATION_TYPE(rs.getString("R11_IDENTIFICATION_TYPE"));
						obj.setR11_PASSPORT_NUMBER(rs.getString("R11_PASSPORT_NUMBER"));
						obj.setR11_DATE_OF_BIRTH(rs.getDate("R11_DATE_OF_BIRTH"));
						obj.setR11_HOME_ADDRESS(rs.getString("R11_HOME_ADDRESS"));
						obj.setR11_POSTAL_ADDRESS(rs.getString("R11_POSTAL_ADDRESS"));
						obj.setR11_RESIDENCE(rs.getString("R11_RESIDENCE"));
						obj.setR11_EMAIL(rs.getString("R11_EMAIL"));
						obj.setR11_LANDLINE(rs.getString("R11_LANDLINE"));
						obj.setR11_MOBILE_PHONE_NUMBER(rs.getString("R11_MOBILE_PHONE_NUMBER"));
						obj.setR11_MOBILE_MONEY_NUMBER(rs.getString("R11_MOBILE_MONEY_NUMBER"));
						obj.setR11_PRODUCT_TYPE(rs.getString("R11_PRODUCT_TYPE"));
						obj.setR11_ACCOUNT_BY_OWNERSHIP(rs.getString("R11_ACCOUNT_BY_OWNERSHIP"));
						obj.setR11_ACCOUNT_NUMBER(rs.getString("R11_ACCOUNT_NUMBER"));
						obj.setR11_ACCOUNT_HOLDER_INDICATOR(rs.getBigDecimal("R11_ACCOUNT_HOLDER_INDICATOR"));
						obj.setR11_STATUS_OF_ACCOUNT(rs.getString("R11_STATUS_OF_ACCOUNT"));
						obj.setR11_NOT_FIT_FOR_STP(rs.getString("R11_NOT_FIT_FOR_STP"));
						obj.setR11_BRANCH_CODE_AND_NAME(rs.getString("R11_BRANCH_CODE_AND_NAME"));
						obj.setR11_ACCOUNT_BALANCE_IN_PULA(rs.getBigDecimal("R11_ACCOUNT_BALANCE_IN_PULA"));
						obj.setR11_CURRENCY_OF_ACCOUNT(rs.getString("R11_CURRENCY_OF_ACCOUNT"));
						obj.setR11_EXCHANGE_RATE(rs.getBigDecimal("R11_EXCHANGE_RATE"));
						
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
				
				public static class BDISB1_RESUB_Detail_Entity {
					
					 private String R5_RECORD_NUMBER;
					    private String R5_TITLE;
					    private String R5_FIRST_NAME;
					    private String R5_MIDDLE_NAME;
					    private String R5_SURNAME;
					    private String R5_PREVIOUS_NAME;
					    private String R5_GENDER;
					    private String R5_IDENTIFICATION_TYPE;
					    private String R5_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R5_DATE_OF_BIRTH;
					    private String R5_HOME_ADDRESS;
					    private String R5_POSTAL_ADDRESS;
					    private String R5_RESIDENCE;
					    private String R5_EMAIL;
					    private String R5_LANDLINE;
					    private String R5_MOBILE_PHONE_NUMBER;
					    private String R5_MOBILE_MONEY_NUMBER;
					    private String R5_PRODUCT_TYPE;
					    private String R5_ACCOUNT_BY_OWNERSHIP;
					    private String R5_ACCOUNT_NUMBER;
					    private BigDecimal R5_ACCOUNT_HOLDER_INDICATOR;
					    private String R5_STATUS_OF_ACCOUNT;
					    private String R5_NOT_FIT_FOR_STP;
					    private String R5_BRANCH_CODE_AND_NAME;
					    private BigDecimal R5_ACCOUNT_BALANCE_IN_PULA;
					    private String R5_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R5_EXCHANGE_RATE;

					    // ===================== R6 =====================
					    private String R6_RECORD_NUMBER;
					    private String R6_TITLE;
					    private String R6_FIRST_NAME;
					    private String R6_MIDDLE_NAME;
					    private String R6_SURNAME;
					    private String R6_PREVIOUS_NAME;
					    private String R6_GENDER;
					    private String R6_IDENTIFICATION_TYPE;
					    private String R6_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R6_DATE_OF_BIRTH;
					    private String R6_HOME_ADDRESS;
					    private String R6_POSTAL_ADDRESS;
					    private String R6_RESIDENCE;
					    private String R6_EMAIL;
					    private String R6_LANDLINE;
					    private String R6_MOBILE_PHONE_NUMBER;
					    private String R6_MOBILE_MONEY_NUMBER;
					    private String R6_PRODUCT_TYPE;
					    private String R6_ACCOUNT_BY_OWNERSHIP;
					    private String R6_ACCOUNT_NUMBER;
					    private BigDecimal R6_ACCOUNT_HOLDER_INDICATOR;
					    private String R6_STATUS_OF_ACCOUNT;
					    private String R6_NOT_FIT_FOR_STP;
					    private String R6_BRANCH_CODE_AND_NAME;
					    private BigDecimal R6_ACCOUNT_BALANCE_IN_PULA;
					    private String R6_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R6_EXCHANGE_RATE;

					    // ===================== R7 =====================
					    private String R7_RECORD_NUMBER;
					    private String R7_TITLE;
					    private String R7_FIRST_NAME;
					    private String R7_MIDDLE_NAME;
					    private String R7_SURNAME;
					    private String R7_PREVIOUS_NAME;
					    private String R7_GENDER;
					    private String R7_IDENTIFICATION_TYPE;
					    private String R7_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R7_DATE_OF_BIRTH;
					    private String R7_HOME_ADDRESS;
					    private String R7_POSTAL_ADDRESS;
					    private String R7_RESIDENCE;
					    private String R7_EMAIL;
					    private String R7_LANDLINE;
					    private String R7_MOBILE_PHONE_NUMBER;
					    private String R7_MOBILE_MONEY_NUMBER;
					    private String R7_PRODUCT_TYPE;
					    private String R7_ACCOUNT_BY_OWNERSHIP;
					    private String R7_ACCOUNT_NUMBER;
					    private BigDecimal R7_ACCOUNT_HOLDER_INDICATOR;
					    private String R7_STATUS_OF_ACCOUNT;
					    private String R7_NOT_FIT_FOR_STP;
					    private String R7_BRANCH_CODE_AND_NAME;
					    private BigDecimal R7_ACCOUNT_BALANCE_IN_PULA;
					    private String R7_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R7_EXCHANGE_RATE;

					    // ===================== R8 =====================
					    private String R8_RECORD_NUMBER;
					    private String R8_TITLE;
					    private String R8_FIRST_NAME;
					    private String R8_MIDDLE_NAME;
					    private String R8_SURNAME;
					    private String R8_PREVIOUS_NAME;
					    private String R8_GENDER;
					    private String R8_IDENTIFICATION_TYPE;
					    private String R8_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R8_DATE_OF_BIRTH;
					    private String R8_HOME_ADDRESS;
					    private String R8_POSTAL_ADDRESS;
					    private String R8_RESIDENCE;
					    private String R8_EMAIL;
					    private String R8_LANDLINE;
					    private String R8_MOBILE_PHONE_NUMBER;
					    private String R8_MOBILE_MONEY_NUMBER;
					    private String R8_PRODUCT_TYPE;
					    private String R8_ACCOUNT_BY_OWNERSHIP;
					    private String R8_ACCOUNT_NUMBER;
					    private BigDecimal R8_ACCOUNT_HOLDER_INDICATOR;
					    private String R8_STATUS_OF_ACCOUNT;
					    private String R8_NOT_FIT_FOR_STP;
					    private String R8_BRANCH_CODE_AND_NAME;
					    private BigDecimal R8_ACCOUNT_BALANCE_IN_PULA;
					    private String R8_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R8_EXCHANGE_RATE;

					    // ===================== R9 =====================
					    private String R9_RECORD_NUMBER;
					    private String R9_TITLE;
					    private String R9_FIRST_NAME;
					    private String R9_MIDDLE_NAME;
					    private String R9_SURNAME;
					    private String R9_PREVIOUS_NAME;
					    private String R9_GENDER;
					    private String R9_IDENTIFICATION_TYPE;
					    private String R9_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R9_DATE_OF_BIRTH;
					    private String R9_HOME_ADDRESS;
					    private String R9_POSTAL_ADDRESS;
					    private String R9_RESIDENCE;
					    private String R9_EMAIL;
					    private String R9_LANDLINE;
					    private String R9_MOBILE_PHONE_NUMBER;
					    private String R9_MOBILE_MONEY_NUMBER;
					    private String R9_PRODUCT_TYPE;
					    private String R9_ACCOUNT_BY_OWNERSHIP;
					    private String R9_ACCOUNT_NUMBER;
					    private BigDecimal R9_ACCOUNT_HOLDER_INDICATOR;
					    private String R9_STATUS_OF_ACCOUNT;
					    private String R9_NOT_FIT_FOR_STP;
					    private String R9_BRANCH_CODE_AND_NAME;
					    private BigDecimal R9_ACCOUNT_BALANCE_IN_PULA;
					    private String R9_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R9_EXCHANGE_RATE;

					    // ===================== R10 =====================
					    private String R10_RECORD_NUMBER;
					    private String R10_TITLE;
					    private String R10_FIRST_NAME;
					    private String R10_MIDDLE_NAME;
					    private String R10_SURNAME;
					    private String R10_PREVIOUS_NAME;
					    private String R10_GENDER;
					    private String R10_IDENTIFICATION_TYPE;
					    private String R10_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R10_DATE_OF_BIRTH;
					    private String R10_HOME_ADDRESS;
					    private String R10_POSTAL_ADDRESS;
					    private String R10_RESIDENCE;
					    private String R10_EMAIL;
					    private String R10_LANDLINE;
					    private String R10_MOBILE_PHONE_NUMBER;
					    private String R10_MOBILE_MONEY_NUMBER;
					    private String R10_PRODUCT_TYPE;
					    private String R10_ACCOUNT_BY_OWNERSHIP;
					    private String R10_ACCOUNT_NUMBER;
					    private BigDecimal R10_ACCOUNT_HOLDER_INDICATOR;
					    private String R10_STATUS_OF_ACCOUNT;
					    private String R10_NOT_FIT_FOR_STP;
					    private String R10_BRANCH_CODE_AND_NAME;
					    private BigDecimal R10_ACCOUNT_BALANCE_IN_PULA;
					    private String R10_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R10_EXCHANGE_RATE;

					    // ===================== R11 =====================
					    private String R11_RECORD_NUMBER;
					    private String R11_TITLE;
					    private String R11_FIRST_NAME;
					    private String R11_MIDDLE_NAME;
					    private String R11_SURNAME;
					    private String R11_PREVIOUS_NAME;
					    private String R11_GENDER;
					    private String R11_IDENTIFICATION_TYPE;
					    private String R11_PASSPORT_NUMBER;
					    @Temporal(TemporalType.DATE)
					    @DateTimeFormat(pattern = "yyyy-MM-dd")
					    private Date R11_DATE_OF_BIRTH;
					    private String R11_HOME_ADDRESS;
					    private String R11_POSTAL_ADDRESS;
					    private String R11_RESIDENCE;
					    private String R11_EMAIL;
					    private String R11_LANDLINE;
					    private String R11_MOBILE_PHONE_NUMBER;
					    private String R11_MOBILE_MONEY_NUMBER;
					    private String R11_PRODUCT_TYPE;
					    private String R11_ACCOUNT_BY_OWNERSHIP;
					    private String R11_ACCOUNT_NUMBER;
					    private BigDecimal R11_ACCOUNT_HOLDER_INDICATOR;
					    private String R11_STATUS_OF_ACCOUNT;
					    private String R11_NOT_FIT_FOR_STP;
					    private String R11_BRANCH_CODE_AND_NAME;
					    private BigDecimal R11_ACCOUNT_BALANCE_IN_PULA;
					    private String R11_CURRENCY_OF_ACCOUNT;
					    private BigDecimal R11_EXCHANGE_RATE;
					    
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
						
						public String getR5_RECORD_NUMBER() {
							return R5_RECORD_NUMBER;
						}
						public void setR5_RECORD_NUMBER(String r5_RECORD_NUMBER) {
							R5_RECORD_NUMBER = r5_RECORD_NUMBER;
						}
						public String getR5_TITLE() {
							return R5_TITLE;
						}
						public void setR5_TITLE(String r5_TITLE) {
							R5_TITLE = r5_TITLE;
						}
						public String getR5_FIRST_NAME() {
							return R5_FIRST_NAME;
						}
						public void setR5_FIRST_NAME(String r5_FIRST_NAME) {
							R5_FIRST_NAME = r5_FIRST_NAME;
						}
						public String getR5_MIDDLE_NAME() {
							return R5_MIDDLE_NAME;
						}
						public void setR5_MIDDLE_NAME(String r5_MIDDLE_NAME) {
							R5_MIDDLE_NAME = r5_MIDDLE_NAME;
						}
						public String getR5_SURNAME() {
							return R5_SURNAME;
						}
						public void setR5_SURNAME(String r5_SURNAME) {
							R5_SURNAME = r5_SURNAME;
						}
						public String getR5_PREVIOUS_NAME() {
							return R5_PREVIOUS_NAME;
						}
						public void setR5_PREVIOUS_NAME(String r5_PREVIOUS_NAME) {
							R5_PREVIOUS_NAME = r5_PREVIOUS_NAME;
						}
						public String getR5_GENDER() {
							return R5_GENDER;
						}
						public void setR5_GENDER(String r5_GENDER) {
							R5_GENDER = r5_GENDER;
						}
						public String getR5_IDENTIFICATION_TYPE() {
							return R5_IDENTIFICATION_TYPE;
						}
						public void setR5_IDENTIFICATION_TYPE(String r5_IDENTIFICATION_TYPE) {
							R5_IDENTIFICATION_TYPE = r5_IDENTIFICATION_TYPE;
						}
						public String getR5_PASSPORT_NUMBER() {
							return R5_PASSPORT_NUMBER;
						}
						public void setR5_PASSPORT_NUMBER(String r5_PASSPORT_NUMBER) {
							R5_PASSPORT_NUMBER = r5_PASSPORT_NUMBER;
						}
						public Date getR5_DATE_OF_BIRTH() {
							return R5_DATE_OF_BIRTH;
						}
						public void setR5_DATE_OF_BIRTH(Date r5_DATE_OF_BIRTH) {
							R5_DATE_OF_BIRTH = r5_DATE_OF_BIRTH;
						}
						public String getR5_HOME_ADDRESS() {
							return R5_HOME_ADDRESS;
						}
						public void setR5_HOME_ADDRESS(String r5_HOME_ADDRESS) {
							R5_HOME_ADDRESS = r5_HOME_ADDRESS;
						}
						public String getR5_POSTAL_ADDRESS() {
							return R5_POSTAL_ADDRESS;
						}
						public void setR5_POSTAL_ADDRESS(String r5_POSTAL_ADDRESS) {
							R5_POSTAL_ADDRESS = r5_POSTAL_ADDRESS;
						}
						public String getR5_RESIDENCE() {
							return R5_RESIDENCE;
						}
						public void setR5_RESIDENCE(String r5_RESIDENCE) {
							R5_RESIDENCE = r5_RESIDENCE;
						}
						public String getR5_EMAIL() {
							return R5_EMAIL;
						}
						public void setR5_EMAIL(String r5_EMAIL) {
							R5_EMAIL = r5_EMAIL;
						}
						public String getR5_LANDLINE() {
							return R5_LANDLINE;
						}
						public void setR5_LANDLINE(String r5_LANDLINE) {
							R5_LANDLINE = r5_LANDLINE;
						}
						public String getR5_MOBILE_PHONE_NUMBER() {
							return R5_MOBILE_PHONE_NUMBER;
						}
						public void setR5_MOBILE_PHONE_NUMBER(String r5_MOBILE_PHONE_NUMBER) {
							R5_MOBILE_PHONE_NUMBER = r5_MOBILE_PHONE_NUMBER;
						}
						public String getR5_MOBILE_MONEY_NUMBER() {
							return R5_MOBILE_MONEY_NUMBER;
						}
						public void setR5_MOBILE_MONEY_NUMBER(String r5_MOBILE_MONEY_NUMBER) {
							R5_MOBILE_MONEY_NUMBER = r5_MOBILE_MONEY_NUMBER;
						}
						public String getR5_PRODUCT_TYPE() {
							return R5_PRODUCT_TYPE;
						}
						public void setR5_PRODUCT_TYPE(String r5_PRODUCT_TYPE) {
							R5_PRODUCT_TYPE = r5_PRODUCT_TYPE;
						}
						public String getR5_ACCOUNT_BY_OWNERSHIP() {
							return R5_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR5_ACCOUNT_BY_OWNERSHIP(String r5_ACCOUNT_BY_OWNERSHIP) {
							R5_ACCOUNT_BY_OWNERSHIP = r5_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR5_ACCOUNT_NUMBER() {
							return R5_ACCOUNT_NUMBER;
						}
						public void setR5_ACCOUNT_NUMBER(String r5_ACCOUNT_NUMBER) {
							R5_ACCOUNT_NUMBER = r5_ACCOUNT_NUMBER;
						}
						public BigDecimal getR5_ACCOUNT_HOLDER_INDICATOR() {
							return R5_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR5_ACCOUNT_HOLDER_INDICATOR(BigDecimal r5_ACCOUNT_HOLDER_INDICATOR) {
							R5_ACCOUNT_HOLDER_INDICATOR = r5_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR5_STATUS_OF_ACCOUNT() {
							return R5_STATUS_OF_ACCOUNT;
						}
						public void setR5_STATUS_OF_ACCOUNT(String r5_STATUS_OF_ACCOUNT) {
							R5_STATUS_OF_ACCOUNT = r5_STATUS_OF_ACCOUNT;
						}
						public String getR5_NOT_FIT_FOR_STP() {
							return R5_NOT_FIT_FOR_STP;
						}
						public void setR5_NOT_FIT_FOR_STP(String r5_NOT_FIT_FOR_STP) {
							R5_NOT_FIT_FOR_STP = r5_NOT_FIT_FOR_STP;
						}
						public String getR5_BRANCH_CODE_AND_NAME() {
							return R5_BRANCH_CODE_AND_NAME;
						}
						public void setR5_BRANCH_CODE_AND_NAME(String r5_BRANCH_CODE_AND_NAME) {
							R5_BRANCH_CODE_AND_NAME = r5_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR5_ACCOUNT_BALANCE_IN_PULA() {
							return R5_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR5_ACCOUNT_BALANCE_IN_PULA(BigDecimal r5_ACCOUNT_BALANCE_IN_PULA) {
							R5_ACCOUNT_BALANCE_IN_PULA = r5_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR5_CURRENCY_OF_ACCOUNT() {
							return R5_CURRENCY_OF_ACCOUNT;
						}
						public void setR5_CURRENCY_OF_ACCOUNT(String r5_CURRENCY_OF_ACCOUNT) {
							R5_CURRENCY_OF_ACCOUNT = r5_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR5_EXCHANGE_RATE() {
							return R5_EXCHANGE_RATE;
						}
						public void setR5_EXCHANGE_RATE(BigDecimal r5_EXCHANGE_RATE) {
							R5_EXCHANGE_RATE = r5_EXCHANGE_RATE;
						}
						public String getR6_RECORD_NUMBER() {
							return R6_RECORD_NUMBER;
						}
						public void setR6_RECORD_NUMBER(String r6_RECORD_NUMBER) {
							R6_RECORD_NUMBER = r6_RECORD_NUMBER;
						}
						public String getR6_TITLE() {
							return R6_TITLE;
						}
						public void setR6_TITLE(String r6_TITLE) {
							R6_TITLE = r6_TITLE;
						}
						public String getR6_FIRST_NAME() {
							return R6_FIRST_NAME;
						}
						public void setR6_FIRST_NAME(String r6_FIRST_NAME) {
							R6_FIRST_NAME = r6_FIRST_NAME;
						}
						public String getR6_MIDDLE_NAME() {
							return R6_MIDDLE_NAME;
						}
						public void setR6_MIDDLE_NAME(String r6_MIDDLE_NAME) {
							R6_MIDDLE_NAME = r6_MIDDLE_NAME;
						}
						public String getR6_SURNAME() {
							return R6_SURNAME;
						}
						public void setR6_SURNAME(String r6_SURNAME) {
							R6_SURNAME = r6_SURNAME;
						}
						public String getR6_PREVIOUS_NAME() {
							return R6_PREVIOUS_NAME;
						}
						public void setR6_PREVIOUS_NAME(String r6_PREVIOUS_NAME) {
							R6_PREVIOUS_NAME = r6_PREVIOUS_NAME;
						}
						public String getR6_GENDER() {
							return R6_GENDER;
						}
						public void setR6_GENDER(String r6_GENDER) {
							R6_GENDER = r6_GENDER;
						}
						public String getR6_IDENTIFICATION_TYPE() {
							return R6_IDENTIFICATION_TYPE;
						}
						public void setR6_IDENTIFICATION_TYPE(String r6_IDENTIFICATION_TYPE) {
							R6_IDENTIFICATION_TYPE = r6_IDENTIFICATION_TYPE;
						}
						public String getR6_PASSPORT_NUMBER() {
							return R6_PASSPORT_NUMBER;
						}
						public void setR6_PASSPORT_NUMBER(String r6_PASSPORT_NUMBER) {
							R6_PASSPORT_NUMBER = r6_PASSPORT_NUMBER;
						}
						public Date getR6_DATE_OF_BIRTH() {
							return R6_DATE_OF_BIRTH;
						}
						public void setR6_DATE_OF_BIRTH(Date r6_DATE_OF_BIRTH) {
							R6_DATE_OF_BIRTH = r6_DATE_OF_BIRTH;
						}
						public String getR6_HOME_ADDRESS() {
							return R6_HOME_ADDRESS;
						}
						public void setR6_HOME_ADDRESS(String r6_HOME_ADDRESS) {
							R6_HOME_ADDRESS = r6_HOME_ADDRESS;
						}
						public String getR6_POSTAL_ADDRESS() {
							return R6_POSTAL_ADDRESS;
						}
						public void setR6_POSTAL_ADDRESS(String r6_POSTAL_ADDRESS) {
							R6_POSTAL_ADDRESS = r6_POSTAL_ADDRESS;
						}
						public String getR6_RESIDENCE() {
							return R6_RESIDENCE;
						}
						public void setR6_RESIDENCE(String r6_RESIDENCE) {
							R6_RESIDENCE = r6_RESIDENCE;
						}
						public String getR6_EMAIL() {
							return R6_EMAIL;
						}
						public void setR6_EMAIL(String r6_EMAIL) {
							R6_EMAIL = r6_EMAIL;
						}
						public String getR6_LANDLINE() {
							return R6_LANDLINE;
						}
						public void setR6_LANDLINE(String r6_LANDLINE) {
							R6_LANDLINE = r6_LANDLINE;
						}
						public String getR6_MOBILE_PHONE_NUMBER() {
							return R6_MOBILE_PHONE_NUMBER;
						}
						public void setR6_MOBILE_PHONE_NUMBER(String r6_MOBILE_PHONE_NUMBER) {
							R6_MOBILE_PHONE_NUMBER = r6_MOBILE_PHONE_NUMBER;
						}
						public String getR6_MOBILE_MONEY_NUMBER() {
							return R6_MOBILE_MONEY_NUMBER;
						}
						public void setR6_MOBILE_MONEY_NUMBER(String r6_MOBILE_MONEY_NUMBER) {
							R6_MOBILE_MONEY_NUMBER = r6_MOBILE_MONEY_NUMBER;
						}
						public String getR6_PRODUCT_TYPE() {
							return R6_PRODUCT_TYPE;
						}
						public void setR6_PRODUCT_TYPE(String r6_PRODUCT_TYPE) {
							R6_PRODUCT_TYPE = r6_PRODUCT_TYPE;
						}
						public String getR6_ACCOUNT_BY_OWNERSHIP() {
							return R6_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR6_ACCOUNT_BY_OWNERSHIP(String r6_ACCOUNT_BY_OWNERSHIP) {
							R6_ACCOUNT_BY_OWNERSHIP = r6_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR6_ACCOUNT_NUMBER() {
							return R6_ACCOUNT_NUMBER;
						}
						public void setR6_ACCOUNT_NUMBER(String r6_ACCOUNT_NUMBER) {
							R6_ACCOUNT_NUMBER = r6_ACCOUNT_NUMBER;
						}
						public BigDecimal getR6_ACCOUNT_HOLDER_INDICATOR() {
							return R6_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR6_ACCOUNT_HOLDER_INDICATOR(BigDecimal r6_ACCOUNT_HOLDER_INDICATOR) {
							R6_ACCOUNT_HOLDER_INDICATOR = r6_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR6_STATUS_OF_ACCOUNT() {
							return R6_STATUS_OF_ACCOUNT;
						}
						public void setR6_STATUS_OF_ACCOUNT(String r6_STATUS_OF_ACCOUNT) {
							R6_STATUS_OF_ACCOUNT = r6_STATUS_OF_ACCOUNT;
						}
						public String getR6_NOT_FIT_FOR_STP() {
							return R6_NOT_FIT_FOR_STP;
						}
						public void setR6_NOT_FIT_FOR_STP(String r6_NOT_FIT_FOR_STP) {
							R6_NOT_FIT_FOR_STP = r6_NOT_FIT_FOR_STP;
						}
						public String getR6_BRANCH_CODE_AND_NAME() {
							return R6_BRANCH_CODE_AND_NAME;
						}
						public void setR6_BRANCH_CODE_AND_NAME(String r6_BRANCH_CODE_AND_NAME) {
							R6_BRANCH_CODE_AND_NAME = r6_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR6_ACCOUNT_BALANCE_IN_PULA() {
							return R6_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR6_ACCOUNT_BALANCE_IN_PULA(BigDecimal r6_ACCOUNT_BALANCE_IN_PULA) {
							R6_ACCOUNT_BALANCE_IN_PULA = r6_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR6_CURRENCY_OF_ACCOUNT() {
							return R6_CURRENCY_OF_ACCOUNT;
						}
						public void setR6_CURRENCY_OF_ACCOUNT(String r6_CURRENCY_OF_ACCOUNT) {
							R6_CURRENCY_OF_ACCOUNT = r6_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR6_EXCHANGE_RATE() {
							return R6_EXCHANGE_RATE;
						}
						public void setR6_EXCHANGE_RATE(BigDecimal r6_EXCHANGE_RATE) {
							R6_EXCHANGE_RATE = r6_EXCHANGE_RATE;
						}
						public String getR7_RECORD_NUMBER() {
							return R7_RECORD_NUMBER;
						}
						public void setR7_RECORD_NUMBER(String r7_RECORD_NUMBER) {
							R7_RECORD_NUMBER = r7_RECORD_NUMBER;
						}
						public String getR7_TITLE() {
							return R7_TITLE;
						}
						public void setR7_TITLE(String r7_TITLE) {
							R7_TITLE = r7_TITLE;
						}
						public String getR7_FIRST_NAME() {
							return R7_FIRST_NAME;
						}
						public void setR7_FIRST_NAME(String r7_FIRST_NAME) {
							R7_FIRST_NAME = r7_FIRST_NAME;
						}
						public String getR7_MIDDLE_NAME() {
							return R7_MIDDLE_NAME;
						}
						public void setR7_MIDDLE_NAME(String r7_MIDDLE_NAME) {
							R7_MIDDLE_NAME = r7_MIDDLE_NAME;
						}
						public String getR7_SURNAME() {
							return R7_SURNAME;
						}
						public void setR7_SURNAME(String r7_SURNAME) {
							R7_SURNAME = r7_SURNAME;
						}
						public String getR7_PREVIOUS_NAME() {
							return R7_PREVIOUS_NAME;
						}
						public void setR7_PREVIOUS_NAME(String r7_PREVIOUS_NAME) {
							R7_PREVIOUS_NAME = r7_PREVIOUS_NAME;
						}
						public String getR7_GENDER() {
							return R7_GENDER;
						}
						public void setR7_GENDER(String r7_GENDER) {
							R7_GENDER = r7_GENDER;
						}
						public String getR7_IDENTIFICATION_TYPE() {
							return R7_IDENTIFICATION_TYPE;
						}
						public void setR7_IDENTIFICATION_TYPE(String r7_IDENTIFICATION_TYPE) {
							R7_IDENTIFICATION_TYPE = r7_IDENTIFICATION_TYPE;
						}
						public String getR7_PASSPORT_NUMBER() {
							return R7_PASSPORT_NUMBER;
						}
						public void setR7_PASSPORT_NUMBER(String r7_PASSPORT_NUMBER) {
							R7_PASSPORT_NUMBER = r7_PASSPORT_NUMBER;
						}
						public Date getR7_DATE_OF_BIRTH() {
							return R7_DATE_OF_BIRTH;
						}
						public void setR7_DATE_OF_BIRTH(Date r7_DATE_OF_BIRTH) {
							R7_DATE_OF_BIRTH = r7_DATE_OF_BIRTH;
						}
						public String getR7_HOME_ADDRESS() {
							return R7_HOME_ADDRESS;
						}
						public void setR7_HOME_ADDRESS(String r7_HOME_ADDRESS) {
							R7_HOME_ADDRESS = r7_HOME_ADDRESS;
						}
						public String getR7_POSTAL_ADDRESS() {
							return R7_POSTAL_ADDRESS;
						}
						public void setR7_POSTAL_ADDRESS(String r7_POSTAL_ADDRESS) {
							R7_POSTAL_ADDRESS = r7_POSTAL_ADDRESS;
						}
						public String getR7_RESIDENCE() {
							return R7_RESIDENCE;
						}
						public void setR7_RESIDENCE(String r7_RESIDENCE) {
							R7_RESIDENCE = r7_RESIDENCE;
						}
						public String getR7_EMAIL() {
							return R7_EMAIL;
						}
						public void setR7_EMAIL(String r7_EMAIL) {
							R7_EMAIL = r7_EMAIL;
						}
						public String getR7_LANDLINE() {
							return R7_LANDLINE;
						}
						public void setR7_LANDLINE(String r7_LANDLINE) {
							R7_LANDLINE = r7_LANDLINE;
						}
						public String getR7_MOBILE_PHONE_NUMBER() {
							return R7_MOBILE_PHONE_NUMBER;
						}
						public void setR7_MOBILE_PHONE_NUMBER(String r7_MOBILE_PHONE_NUMBER) {
							R7_MOBILE_PHONE_NUMBER = r7_MOBILE_PHONE_NUMBER;
						}
						public String getR7_MOBILE_MONEY_NUMBER() {
							return R7_MOBILE_MONEY_NUMBER;
						}
						public void setR7_MOBILE_MONEY_NUMBER(String r7_MOBILE_MONEY_NUMBER) {
							R7_MOBILE_MONEY_NUMBER = r7_MOBILE_MONEY_NUMBER;
						}
						public String getR7_PRODUCT_TYPE() {
							return R7_PRODUCT_TYPE;
						}
						public void setR7_PRODUCT_TYPE(String r7_PRODUCT_TYPE) {
							R7_PRODUCT_TYPE = r7_PRODUCT_TYPE;
						}
						public String getR7_ACCOUNT_BY_OWNERSHIP() {
							return R7_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR7_ACCOUNT_BY_OWNERSHIP(String r7_ACCOUNT_BY_OWNERSHIP) {
							R7_ACCOUNT_BY_OWNERSHIP = r7_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR7_ACCOUNT_NUMBER() {
							return R7_ACCOUNT_NUMBER;
						}
						public void setR7_ACCOUNT_NUMBER(String r7_ACCOUNT_NUMBER) {
							R7_ACCOUNT_NUMBER = r7_ACCOUNT_NUMBER;
						}
						public BigDecimal getR7_ACCOUNT_HOLDER_INDICATOR() {
							return R7_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR7_ACCOUNT_HOLDER_INDICATOR(BigDecimal r7_ACCOUNT_HOLDER_INDICATOR) {
							R7_ACCOUNT_HOLDER_INDICATOR = r7_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR7_STATUS_OF_ACCOUNT() {
							return R7_STATUS_OF_ACCOUNT;
						}
						public void setR7_STATUS_OF_ACCOUNT(String r7_STATUS_OF_ACCOUNT) {
							R7_STATUS_OF_ACCOUNT = r7_STATUS_OF_ACCOUNT;
						}
						public String getR7_NOT_FIT_FOR_STP() {
							return R7_NOT_FIT_FOR_STP;
						}
						public void setR7_NOT_FIT_FOR_STP(String r7_NOT_FIT_FOR_STP) {
							R7_NOT_FIT_FOR_STP = r7_NOT_FIT_FOR_STP;
						}
						public String getR7_BRANCH_CODE_AND_NAME() {
							return R7_BRANCH_CODE_AND_NAME;
						}
						public void setR7_BRANCH_CODE_AND_NAME(String r7_BRANCH_CODE_AND_NAME) {
							R7_BRANCH_CODE_AND_NAME = r7_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR7_ACCOUNT_BALANCE_IN_PULA() {
							return R7_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR7_ACCOUNT_BALANCE_IN_PULA(BigDecimal r7_ACCOUNT_BALANCE_IN_PULA) {
							R7_ACCOUNT_BALANCE_IN_PULA = r7_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR7_CURRENCY_OF_ACCOUNT() {
							return R7_CURRENCY_OF_ACCOUNT;
						}
						public void setR7_CURRENCY_OF_ACCOUNT(String r7_CURRENCY_OF_ACCOUNT) {
							R7_CURRENCY_OF_ACCOUNT = r7_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR7_EXCHANGE_RATE() {
							return R7_EXCHANGE_RATE;
						}
						public void setR7_EXCHANGE_RATE(BigDecimal r7_EXCHANGE_RATE) {
							R7_EXCHANGE_RATE = r7_EXCHANGE_RATE;
						}
						public String getR8_RECORD_NUMBER() {
							return R8_RECORD_NUMBER;
						}
						public void setR8_RECORD_NUMBER(String r8_RECORD_NUMBER) {
							R8_RECORD_NUMBER = r8_RECORD_NUMBER;
						}
						public String getR8_TITLE() {
							return R8_TITLE;
						}
						public void setR8_TITLE(String r8_TITLE) {
							R8_TITLE = r8_TITLE;
						}
						public String getR8_FIRST_NAME() {
							return R8_FIRST_NAME;
						}
						public void setR8_FIRST_NAME(String r8_FIRST_NAME) {
							R8_FIRST_NAME = r8_FIRST_NAME;
						}
						public String getR8_MIDDLE_NAME() {
							return R8_MIDDLE_NAME;
						}
						public void setR8_MIDDLE_NAME(String r8_MIDDLE_NAME) {
							R8_MIDDLE_NAME = r8_MIDDLE_NAME;
						}
						public String getR8_SURNAME() {
							return R8_SURNAME;
						}
						public void setR8_SURNAME(String r8_SURNAME) {
							R8_SURNAME = r8_SURNAME;
						}
						public String getR8_PREVIOUS_NAME() {
							return R8_PREVIOUS_NAME;
						}
						public void setR8_PREVIOUS_NAME(String r8_PREVIOUS_NAME) {
							R8_PREVIOUS_NAME = r8_PREVIOUS_NAME;
						}
						public String getR8_GENDER() {
							return R8_GENDER;
						}
						public void setR8_GENDER(String r8_GENDER) {
							R8_GENDER = r8_GENDER;
						}
						public String getR8_IDENTIFICATION_TYPE() {
							return R8_IDENTIFICATION_TYPE;
						}
						public void setR8_IDENTIFICATION_TYPE(String r8_IDENTIFICATION_TYPE) {
							R8_IDENTIFICATION_TYPE = r8_IDENTIFICATION_TYPE;
						}
						public String getR8_PASSPORT_NUMBER() {
							return R8_PASSPORT_NUMBER;
						}
						public void setR8_PASSPORT_NUMBER(String r8_PASSPORT_NUMBER) {
							R8_PASSPORT_NUMBER = r8_PASSPORT_NUMBER;
						}
						public Date getR8_DATE_OF_BIRTH() {
							return R8_DATE_OF_BIRTH;
						}
						public void setR8_DATE_OF_BIRTH(Date r8_DATE_OF_BIRTH) {
							R8_DATE_OF_BIRTH = r8_DATE_OF_BIRTH;
						}
						public String getR8_HOME_ADDRESS() {
							return R8_HOME_ADDRESS;
						}
						public void setR8_HOME_ADDRESS(String r8_HOME_ADDRESS) {
							R8_HOME_ADDRESS = r8_HOME_ADDRESS;
						}
						public String getR8_POSTAL_ADDRESS() {
							return R8_POSTAL_ADDRESS;
						}
						public void setR8_POSTAL_ADDRESS(String r8_POSTAL_ADDRESS) {
							R8_POSTAL_ADDRESS = r8_POSTAL_ADDRESS;
						}
						public String getR8_RESIDENCE() {
							return R8_RESIDENCE;
						}
						public void setR8_RESIDENCE(String r8_RESIDENCE) {
							R8_RESIDENCE = r8_RESIDENCE;
						}
						public String getR8_EMAIL() {
							return R8_EMAIL;
						}
						public void setR8_EMAIL(String r8_EMAIL) {
							R8_EMAIL = r8_EMAIL;
						}
						public String getR8_LANDLINE() {
							return R8_LANDLINE;
						}
						public void setR8_LANDLINE(String r8_LANDLINE) {
							R8_LANDLINE = r8_LANDLINE;
						}
						public String getR8_MOBILE_PHONE_NUMBER() {
							return R8_MOBILE_PHONE_NUMBER;
						}
						public void setR8_MOBILE_PHONE_NUMBER(String r8_MOBILE_PHONE_NUMBER) {
							R8_MOBILE_PHONE_NUMBER = r8_MOBILE_PHONE_NUMBER;
						}
						public String getR8_MOBILE_MONEY_NUMBER() {
							return R8_MOBILE_MONEY_NUMBER;
						}
						public void setR8_MOBILE_MONEY_NUMBER(String r8_MOBILE_MONEY_NUMBER) {
							R8_MOBILE_MONEY_NUMBER = r8_MOBILE_MONEY_NUMBER;
						}
						public String getR8_PRODUCT_TYPE() {
							return R8_PRODUCT_TYPE;
						}
						public void setR8_PRODUCT_TYPE(String r8_PRODUCT_TYPE) {
							R8_PRODUCT_TYPE = r8_PRODUCT_TYPE;
						}
						public String getR8_ACCOUNT_BY_OWNERSHIP() {
							return R8_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR8_ACCOUNT_BY_OWNERSHIP(String r8_ACCOUNT_BY_OWNERSHIP) {
							R8_ACCOUNT_BY_OWNERSHIP = r8_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR8_ACCOUNT_NUMBER() {
							return R8_ACCOUNT_NUMBER;
						}
						public void setR8_ACCOUNT_NUMBER(String r8_ACCOUNT_NUMBER) {
							R8_ACCOUNT_NUMBER = r8_ACCOUNT_NUMBER;
						}
						public BigDecimal getR8_ACCOUNT_HOLDER_INDICATOR() {
							return R8_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR8_ACCOUNT_HOLDER_INDICATOR(BigDecimal r8_ACCOUNT_HOLDER_INDICATOR) {
							R8_ACCOUNT_HOLDER_INDICATOR = r8_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR8_STATUS_OF_ACCOUNT() {
							return R8_STATUS_OF_ACCOUNT;
						}
						public void setR8_STATUS_OF_ACCOUNT(String r8_STATUS_OF_ACCOUNT) {
							R8_STATUS_OF_ACCOUNT = r8_STATUS_OF_ACCOUNT;
						}
						public String getR8_NOT_FIT_FOR_STP() {
							return R8_NOT_FIT_FOR_STP;
						}
						public void setR8_NOT_FIT_FOR_STP(String r8_NOT_FIT_FOR_STP) {
							R8_NOT_FIT_FOR_STP = r8_NOT_FIT_FOR_STP;
						}
						public String getR8_BRANCH_CODE_AND_NAME() {
							return R8_BRANCH_CODE_AND_NAME;
						}
						public void setR8_BRANCH_CODE_AND_NAME(String r8_BRANCH_CODE_AND_NAME) {
							R8_BRANCH_CODE_AND_NAME = r8_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR8_ACCOUNT_BALANCE_IN_PULA() {
							return R8_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR8_ACCOUNT_BALANCE_IN_PULA(BigDecimal r8_ACCOUNT_BALANCE_IN_PULA) {
							R8_ACCOUNT_BALANCE_IN_PULA = r8_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR8_CURRENCY_OF_ACCOUNT() {
							return R8_CURRENCY_OF_ACCOUNT;
						}
						public void setR8_CURRENCY_OF_ACCOUNT(String r8_CURRENCY_OF_ACCOUNT) {
							R8_CURRENCY_OF_ACCOUNT = r8_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR8_EXCHANGE_RATE() {
							return R8_EXCHANGE_RATE;
						}
						public void setR8_EXCHANGE_RATE(BigDecimal r8_EXCHANGE_RATE) {
							R8_EXCHANGE_RATE = r8_EXCHANGE_RATE;
						}
						public String getR9_RECORD_NUMBER() {
							return R9_RECORD_NUMBER;
						}
						public void setR9_RECORD_NUMBER(String r9_RECORD_NUMBER) {
							R9_RECORD_NUMBER = r9_RECORD_NUMBER;
						}
						public String getR9_TITLE() {
							return R9_TITLE;
						}
						public void setR9_TITLE(String r9_TITLE) {
							R9_TITLE = r9_TITLE;
						}
						public String getR9_FIRST_NAME() {
							return R9_FIRST_NAME;
						}
						public void setR9_FIRST_NAME(String r9_FIRST_NAME) {
							R9_FIRST_NAME = r9_FIRST_NAME;
						}
						public String getR9_MIDDLE_NAME() {
							return R9_MIDDLE_NAME;
						}
						public void setR9_MIDDLE_NAME(String r9_MIDDLE_NAME) {
							R9_MIDDLE_NAME = r9_MIDDLE_NAME;
						}
						public String getR9_SURNAME() {
							return R9_SURNAME;
						}
						public void setR9_SURNAME(String r9_SURNAME) {
							R9_SURNAME = r9_SURNAME;
						}
						public String getR9_PREVIOUS_NAME() {
							return R9_PREVIOUS_NAME;
						}
						public void setR9_PREVIOUS_NAME(String r9_PREVIOUS_NAME) {
							R9_PREVIOUS_NAME = r9_PREVIOUS_NAME;
						}
						public String getR9_GENDER() {
							return R9_GENDER;
						}
						public void setR9_GENDER(String r9_GENDER) {
							R9_GENDER = r9_GENDER;
						}
						public String getR9_IDENTIFICATION_TYPE() {
							return R9_IDENTIFICATION_TYPE;
						}
						public void setR9_IDENTIFICATION_TYPE(String r9_IDENTIFICATION_TYPE) {
							R9_IDENTIFICATION_TYPE = r9_IDENTIFICATION_TYPE;
						}
						public String getR9_PASSPORT_NUMBER() {
							return R9_PASSPORT_NUMBER;
						}
						public void setR9_PASSPORT_NUMBER(String r9_PASSPORT_NUMBER) {
							R9_PASSPORT_NUMBER = r9_PASSPORT_NUMBER;
						}
						public Date getR9_DATE_OF_BIRTH() {
							return R9_DATE_OF_BIRTH;
						}
						public void setR9_DATE_OF_BIRTH(Date r9_DATE_OF_BIRTH) {
							R9_DATE_OF_BIRTH = r9_DATE_OF_BIRTH;
						}
						public String getR9_HOME_ADDRESS() {
							return R9_HOME_ADDRESS;
						}
						public void setR9_HOME_ADDRESS(String r9_HOME_ADDRESS) {
							R9_HOME_ADDRESS = r9_HOME_ADDRESS;
						}
						public String getR9_POSTAL_ADDRESS() {
							return R9_POSTAL_ADDRESS;
						}
						public void setR9_POSTAL_ADDRESS(String r9_POSTAL_ADDRESS) {
							R9_POSTAL_ADDRESS = r9_POSTAL_ADDRESS;
						}
						public String getR9_RESIDENCE() {
							return R9_RESIDENCE;
						}
						public void setR9_RESIDENCE(String r9_RESIDENCE) {
							R9_RESIDENCE = r9_RESIDENCE;
						}
						public String getR9_EMAIL() {
							return R9_EMAIL;
						}
						public void setR9_EMAIL(String r9_EMAIL) {
							R9_EMAIL = r9_EMAIL;
						}
						public String getR9_LANDLINE() {
							return R9_LANDLINE;
						}
						public void setR9_LANDLINE(String r9_LANDLINE) {
							R9_LANDLINE = r9_LANDLINE;
						}
						public String getR9_MOBILE_PHONE_NUMBER() {
							return R9_MOBILE_PHONE_NUMBER;
						}
						public void setR9_MOBILE_PHONE_NUMBER(String r9_MOBILE_PHONE_NUMBER) {
							R9_MOBILE_PHONE_NUMBER = r9_MOBILE_PHONE_NUMBER;
						}
						public String getR9_MOBILE_MONEY_NUMBER() {
							return R9_MOBILE_MONEY_NUMBER;
						}
						public void setR9_MOBILE_MONEY_NUMBER(String r9_MOBILE_MONEY_NUMBER) {
							R9_MOBILE_MONEY_NUMBER = r9_MOBILE_MONEY_NUMBER;
						}
						public String getR9_PRODUCT_TYPE() {
							return R9_PRODUCT_TYPE;
						}
						public void setR9_PRODUCT_TYPE(String r9_PRODUCT_TYPE) {
							R9_PRODUCT_TYPE = r9_PRODUCT_TYPE;
						}
						public String getR9_ACCOUNT_BY_OWNERSHIP() {
							return R9_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR9_ACCOUNT_BY_OWNERSHIP(String r9_ACCOUNT_BY_OWNERSHIP) {
							R9_ACCOUNT_BY_OWNERSHIP = r9_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR9_ACCOUNT_NUMBER() {
							return R9_ACCOUNT_NUMBER;
						}
						public void setR9_ACCOUNT_NUMBER(String r9_ACCOUNT_NUMBER) {
							R9_ACCOUNT_NUMBER = r9_ACCOUNT_NUMBER;
						}
						public BigDecimal getR9_ACCOUNT_HOLDER_INDICATOR() {
							return R9_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR9_ACCOUNT_HOLDER_INDICATOR(BigDecimal r9_ACCOUNT_HOLDER_INDICATOR) {
							R9_ACCOUNT_HOLDER_INDICATOR = r9_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR9_STATUS_OF_ACCOUNT() {
							return R9_STATUS_OF_ACCOUNT;
						}
						public void setR9_STATUS_OF_ACCOUNT(String r9_STATUS_OF_ACCOUNT) {
							R9_STATUS_OF_ACCOUNT = r9_STATUS_OF_ACCOUNT;
						}
						public String getR9_NOT_FIT_FOR_STP() {
							return R9_NOT_FIT_FOR_STP;
						}
						public void setR9_NOT_FIT_FOR_STP(String r9_NOT_FIT_FOR_STP) {
							R9_NOT_FIT_FOR_STP = r9_NOT_FIT_FOR_STP;
						}
						public String getR9_BRANCH_CODE_AND_NAME() {
							return R9_BRANCH_CODE_AND_NAME;
						}
						public void setR9_BRANCH_CODE_AND_NAME(String r9_BRANCH_CODE_AND_NAME) {
							R9_BRANCH_CODE_AND_NAME = r9_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR9_ACCOUNT_BALANCE_IN_PULA() {
							return R9_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR9_ACCOUNT_BALANCE_IN_PULA(BigDecimal r9_ACCOUNT_BALANCE_IN_PULA) {
							R9_ACCOUNT_BALANCE_IN_PULA = r9_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR9_CURRENCY_OF_ACCOUNT() {
							return R9_CURRENCY_OF_ACCOUNT;
						}
						public void setR9_CURRENCY_OF_ACCOUNT(String r9_CURRENCY_OF_ACCOUNT) {
							R9_CURRENCY_OF_ACCOUNT = r9_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR9_EXCHANGE_RATE() {
							return R9_EXCHANGE_RATE;
						}
						public void setR9_EXCHANGE_RATE(BigDecimal r9_EXCHANGE_RATE) {
							R9_EXCHANGE_RATE = r9_EXCHANGE_RATE;
						}
						public String getR10_RECORD_NUMBER() {
							return R10_RECORD_NUMBER;
						}
						public void setR10_RECORD_NUMBER(String r10_RECORD_NUMBER) {
							R10_RECORD_NUMBER = r10_RECORD_NUMBER;
						}
						public String getR10_TITLE() {
							return R10_TITLE;
						}
						public void setR10_TITLE(String r10_TITLE) {
							R10_TITLE = r10_TITLE;
						}
						public String getR10_FIRST_NAME() {
							return R10_FIRST_NAME;
						}
						public void setR10_FIRST_NAME(String r10_FIRST_NAME) {
							R10_FIRST_NAME = r10_FIRST_NAME;
						}
						public String getR10_MIDDLE_NAME() {
							return R10_MIDDLE_NAME;
						}
						public void setR10_MIDDLE_NAME(String r10_MIDDLE_NAME) {
							R10_MIDDLE_NAME = r10_MIDDLE_NAME;
						}
						public String getR10_SURNAME() {
							return R10_SURNAME;
						}
						public void setR10_SURNAME(String r10_SURNAME) {
							R10_SURNAME = r10_SURNAME;
						}
						public String getR10_PREVIOUS_NAME() {
							return R10_PREVIOUS_NAME;
						}
						public void setR10_PREVIOUS_NAME(String r10_PREVIOUS_NAME) {
							R10_PREVIOUS_NAME = r10_PREVIOUS_NAME;
						}
						public String getR10_GENDER() {
							return R10_GENDER;
						}
						public void setR10_GENDER(String r10_GENDER) {
							R10_GENDER = r10_GENDER;
						}
						public String getR10_IDENTIFICATION_TYPE() {
							return R10_IDENTIFICATION_TYPE;
						}
						public void setR10_IDENTIFICATION_TYPE(String r10_IDENTIFICATION_TYPE) {
							R10_IDENTIFICATION_TYPE = r10_IDENTIFICATION_TYPE;
						}
						public String getR10_PASSPORT_NUMBER() {
							return R10_PASSPORT_NUMBER;
						}
						public void setR10_PASSPORT_NUMBER(String r10_PASSPORT_NUMBER) {
							R10_PASSPORT_NUMBER = r10_PASSPORT_NUMBER;
						}
						public Date getR10_DATE_OF_BIRTH() {
							return R10_DATE_OF_BIRTH;
						}
						public void setR10_DATE_OF_BIRTH(Date r10_DATE_OF_BIRTH) {
							R10_DATE_OF_BIRTH = r10_DATE_OF_BIRTH;
						}
						public String getR10_HOME_ADDRESS() {
							return R10_HOME_ADDRESS;
						}
						public void setR10_HOME_ADDRESS(String r10_HOME_ADDRESS) {
							R10_HOME_ADDRESS = r10_HOME_ADDRESS;
						}
						public String getR10_POSTAL_ADDRESS() {
							return R10_POSTAL_ADDRESS;
						}
						public void setR10_POSTAL_ADDRESS(String r10_POSTAL_ADDRESS) {
							R10_POSTAL_ADDRESS = r10_POSTAL_ADDRESS;
						}
						public String getR10_RESIDENCE() {
							return R10_RESIDENCE;
						}
						public void setR10_RESIDENCE(String r10_RESIDENCE) {
							R10_RESIDENCE = r10_RESIDENCE;
						}
						public String getR10_EMAIL() {
							return R10_EMAIL;
						}
						public void setR10_EMAIL(String r10_EMAIL) {
							R10_EMAIL = r10_EMAIL;
						}
						public String getR10_LANDLINE() {
							return R10_LANDLINE;
						}
						public void setR10_LANDLINE(String r10_LANDLINE) {
							R10_LANDLINE = r10_LANDLINE;
						}
						public String getR10_MOBILE_PHONE_NUMBER() {
							return R10_MOBILE_PHONE_NUMBER;
						}
						public void setR10_MOBILE_PHONE_NUMBER(String r10_MOBILE_PHONE_NUMBER) {
							R10_MOBILE_PHONE_NUMBER = r10_MOBILE_PHONE_NUMBER;
						}
						public String getR10_MOBILE_MONEY_NUMBER() {
							return R10_MOBILE_MONEY_NUMBER;
						}
						public void setR10_MOBILE_MONEY_NUMBER(String r10_MOBILE_MONEY_NUMBER) {
							R10_MOBILE_MONEY_NUMBER = r10_MOBILE_MONEY_NUMBER;
						}
						public String getR10_PRODUCT_TYPE() {
							return R10_PRODUCT_TYPE;
						}
						public void setR10_PRODUCT_TYPE(String r10_PRODUCT_TYPE) {
							R10_PRODUCT_TYPE = r10_PRODUCT_TYPE;
						}
						public String getR10_ACCOUNT_BY_OWNERSHIP() {
							return R10_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR10_ACCOUNT_BY_OWNERSHIP(String r10_ACCOUNT_BY_OWNERSHIP) {
							R10_ACCOUNT_BY_OWNERSHIP = r10_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR10_ACCOUNT_NUMBER() {
							return R10_ACCOUNT_NUMBER;
						}
						public void setR10_ACCOUNT_NUMBER(String r10_ACCOUNT_NUMBER) {
							R10_ACCOUNT_NUMBER = r10_ACCOUNT_NUMBER;
						}
						public BigDecimal getR10_ACCOUNT_HOLDER_INDICATOR() {
							return R10_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR10_ACCOUNT_HOLDER_INDICATOR(BigDecimal r10_ACCOUNT_HOLDER_INDICATOR) {
							R10_ACCOUNT_HOLDER_INDICATOR = r10_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR10_STATUS_OF_ACCOUNT() {
							return R10_STATUS_OF_ACCOUNT;
						}
						public void setR10_STATUS_OF_ACCOUNT(String r10_STATUS_OF_ACCOUNT) {
							R10_STATUS_OF_ACCOUNT = r10_STATUS_OF_ACCOUNT;
						}
						public String getR10_NOT_FIT_FOR_STP() {
							return R10_NOT_FIT_FOR_STP;
						}
						public void setR10_NOT_FIT_FOR_STP(String r10_NOT_FIT_FOR_STP) {
							R10_NOT_FIT_FOR_STP = r10_NOT_FIT_FOR_STP;
						}
						public String getR10_BRANCH_CODE_AND_NAME() {
							return R10_BRANCH_CODE_AND_NAME;
						}
						public void setR10_BRANCH_CODE_AND_NAME(String r10_BRANCH_CODE_AND_NAME) {
							R10_BRANCH_CODE_AND_NAME = r10_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR10_ACCOUNT_BALANCE_IN_PULA() {
							return R10_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR10_ACCOUNT_BALANCE_IN_PULA(BigDecimal r10_ACCOUNT_BALANCE_IN_PULA) {
							R10_ACCOUNT_BALANCE_IN_PULA = r10_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR10_CURRENCY_OF_ACCOUNT() {
							return R10_CURRENCY_OF_ACCOUNT;
						}
						public void setR10_CURRENCY_OF_ACCOUNT(String r10_CURRENCY_OF_ACCOUNT) {
							R10_CURRENCY_OF_ACCOUNT = r10_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR10_EXCHANGE_RATE() {
							return R10_EXCHANGE_RATE;
						}
						public void setR10_EXCHANGE_RATE(BigDecimal r10_EXCHANGE_RATE) {
							R10_EXCHANGE_RATE = r10_EXCHANGE_RATE;
						}
						public String getR11_RECORD_NUMBER() {
							return R11_RECORD_NUMBER;
						}
						public void setR11_RECORD_NUMBER(String r11_RECORD_NUMBER) {
							R11_RECORD_NUMBER = r11_RECORD_NUMBER;
						}
						public String getR11_TITLE() {
							return R11_TITLE;
						}
						public void setR11_TITLE(String r11_TITLE) {
							R11_TITLE = r11_TITLE;
						}
						public String getR11_FIRST_NAME() {
							return R11_FIRST_NAME;
						}
						public void setR11_FIRST_NAME(String r11_FIRST_NAME) {
							R11_FIRST_NAME = r11_FIRST_NAME;
						}
						public String getR11_MIDDLE_NAME() {
							return R11_MIDDLE_NAME;
						}
						public void setR11_MIDDLE_NAME(String r11_MIDDLE_NAME) {
							R11_MIDDLE_NAME = r11_MIDDLE_NAME;
						}
						public String getR11_SURNAME() {
							return R11_SURNAME;
						}
						public void setR11_SURNAME(String r11_SURNAME) {
							R11_SURNAME = r11_SURNAME;
						}
						public String getR11_PREVIOUS_NAME() {
							return R11_PREVIOUS_NAME;
						}
						public void setR11_PREVIOUS_NAME(String r11_PREVIOUS_NAME) {
							R11_PREVIOUS_NAME = r11_PREVIOUS_NAME;
						}
						public String getR11_GENDER() {
							return R11_GENDER;
						}
						public void setR11_GENDER(String r11_GENDER) {
							R11_GENDER = r11_GENDER;
						}
						public String getR11_IDENTIFICATION_TYPE() {
							return R11_IDENTIFICATION_TYPE;
						}
						public void setR11_IDENTIFICATION_TYPE(String r11_IDENTIFICATION_TYPE) {
							R11_IDENTIFICATION_TYPE = r11_IDENTIFICATION_TYPE;
						}
						public String getR11_PASSPORT_NUMBER() {
							return R11_PASSPORT_NUMBER;
						}
						public void setR11_PASSPORT_NUMBER(String r11_PASSPORT_NUMBER) {
							R11_PASSPORT_NUMBER = r11_PASSPORT_NUMBER;
						}
						public Date getR11_DATE_OF_BIRTH() {
							return R11_DATE_OF_BIRTH;
						}
						public void setR11_DATE_OF_BIRTH(Date r11_DATE_OF_BIRTH) {
							R11_DATE_OF_BIRTH = r11_DATE_OF_BIRTH;
						}
						public String getR11_HOME_ADDRESS() {
							return R11_HOME_ADDRESS;
						}
						public void setR11_HOME_ADDRESS(String r11_HOME_ADDRESS) {
							R11_HOME_ADDRESS = r11_HOME_ADDRESS;
						}
						public String getR11_POSTAL_ADDRESS() {
							return R11_POSTAL_ADDRESS;
						}
						public void setR11_POSTAL_ADDRESS(String r11_POSTAL_ADDRESS) {
							R11_POSTAL_ADDRESS = r11_POSTAL_ADDRESS;
						}
						public String getR11_RESIDENCE() {
							return R11_RESIDENCE;
						}
						public void setR11_RESIDENCE(String r11_RESIDENCE) {
							R11_RESIDENCE = r11_RESIDENCE;
						}
						public String getR11_EMAIL() {
							return R11_EMAIL;
						}
						public void setR11_EMAIL(String r11_EMAIL) {
							R11_EMAIL = r11_EMAIL;
						}
						public String getR11_LANDLINE() {
							return R11_LANDLINE;
						}
						public void setR11_LANDLINE(String r11_LANDLINE) {
							R11_LANDLINE = r11_LANDLINE;
						}
						public String getR11_MOBILE_PHONE_NUMBER() {
							return R11_MOBILE_PHONE_NUMBER;
						}
						public void setR11_MOBILE_PHONE_NUMBER(String r11_MOBILE_PHONE_NUMBER) {
							R11_MOBILE_PHONE_NUMBER = r11_MOBILE_PHONE_NUMBER;
						}
						public String getR11_MOBILE_MONEY_NUMBER() {
							return R11_MOBILE_MONEY_NUMBER;
						}
						public void setR11_MOBILE_MONEY_NUMBER(String r11_MOBILE_MONEY_NUMBER) {
							R11_MOBILE_MONEY_NUMBER = r11_MOBILE_MONEY_NUMBER;
						}
						public String getR11_PRODUCT_TYPE() {
							return R11_PRODUCT_TYPE;
						}
						public void setR11_PRODUCT_TYPE(String r11_PRODUCT_TYPE) {
							R11_PRODUCT_TYPE = r11_PRODUCT_TYPE;
						}
						public String getR11_ACCOUNT_BY_OWNERSHIP() {
							return R11_ACCOUNT_BY_OWNERSHIP;
						}
						public void setR11_ACCOUNT_BY_OWNERSHIP(String r11_ACCOUNT_BY_OWNERSHIP) {
							R11_ACCOUNT_BY_OWNERSHIP = r11_ACCOUNT_BY_OWNERSHIP;
						}
						public String getR11_ACCOUNT_NUMBER() {
							return R11_ACCOUNT_NUMBER;
						}
						public void setR11_ACCOUNT_NUMBER(String r11_ACCOUNT_NUMBER) {
							R11_ACCOUNT_NUMBER = r11_ACCOUNT_NUMBER;
						}
						public BigDecimal getR11_ACCOUNT_HOLDER_INDICATOR() {
							return R11_ACCOUNT_HOLDER_INDICATOR;
						}
						public void setR11_ACCOUNT_HOLDER_INDICATOR(BigDecimal r11_ACCOUNT_HOLDER_INDICATOR) {
							R11_ACCOUNT_HOLDER_INDICATOR = r11_ACCOUNT_HOLDER_INDICATOR;
						}
						public String getR11_STATUS_OF_ACCOUNT() {
							return R11_STATUS_OF_ACCOUNT;
						}
						public void setR11_STATUS_OF_ACCOUNT(String r11_STATUS_OF_ACCOUNT) {
							R11_STATUS_OF_ACCOUNT = r11_STATUS_OF_ACCOUNT;
						}
						public String getR11_NOT_FIT_FOR_STP() {
							return R11_NOT_FIT_FOR_STP;
						}
						public void setR11_NOT_FIT_FOR_STP(String r11_NOT_FIT_FOR_STP) {
							R11_NOT_FIT_FOR_STP = r11_NOT_FIT_FOR_STP;
						}
						public String getR11_BRANCH_CODE_AND_NAME() {
							return R11_BRANCH_CODE_AND_NAME;
						}
						public void setR11_BRANCH_CODE_AND_NAME(String r11_BRANCH_CODE_AND_NAME) {
							R11_BRANCH_CODE_AND_NAME = r11_BRANCH_CODE_AND_NAME;
						}
						public BigDecimal getR11_ACCOUNT_BALANCE_IN_PULA() {
							return R11_ACCOUNT_BALANCE_IN_PULA;
						}
						public void setR11_ACCOUNT_BALANCE_IN_PULA(BigDecimal r11_ACCOUNT_BALANCE_IN_PULA) {
							R11_ACCOUNT_BALANCE_IN_PULA = r11_ACCOUNT_BALANCE_IN_PULA;
						}
						public String getR11_CURRENCY_OF_ACCOUNT() {
							return R11_CURRENCY_OF_ACCOUNT;
						}
						public void setR11_CURRENCY_OF_ACCOUNT(String r11_CURRENCY_OF_ACCOUNT) {
							R11_CURRENCY_OF_ACCOUNT = r11_CURRENCY_OF_ACCOUNT;
						}
						public BigDecimal getR11_EXCHANGE_RATE() {
							return R11_EXCHANGE_RATE;
						}
						public void setR11_EXCHANGE_RATE(BigDecimal r11_EXCHANGE_RATE) {
							R11_EXCHANGE_RATE = r11_EXCHANGE_RATE;
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

	public ModelAndView getBRRS_BDISB1View(String reportId, String fromdate, String todate, String currency, String dtltype,
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

				List<BDISB1_Archival_Summary_Entity> T1Master = getdatabydateListarchival1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<BDISB1_RESUB_Summary_Entity> T1Master = getdatabydateListresub1(dt, version);
				
				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<BDISB1_Summary_Entity> T1Master = getDataByDate1(dt);
				
				System.out.println("T1Master Size: " + T1Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {

				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					List<BDISB1_Archival_Detail_Entity> T1Master = getdatabydateListArchivalDetail1(dt, version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<BDISB1_RESUB_Detail_Entity> T1Master = getdatabydateListResubDetail1(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);

				}
				// DETAIL + NORMAL
				else {

					List<BDISB1_Detail_Entity> T1Master = getDetaildatabydateList1(dt);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/BDISB1");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}
	

	
	public void updateResubReport(
	        BDISB1_RESUB_Summary_Entity updatedEntity1) {

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

	    BDISB1_RESUB_Summary_Entity resubSummary1 = new BDISB1_RESUB_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubSummary1);

	    resubSummary1.setREPORT_DATE(reportDate1);
	    resubSummary1.setREPORT_VERSION(newVersion);
	    resubSummary1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 4️⃣ RESUB DETAIL
	    // ====================================================

	    BDISB1_RESUB_Detail_Entity resubDetail1 = new BDISB1_RESUB_Detail_Entity();

	    BeanUtils.copyProperties(updatedEntity1, resubDetail1);

	    resubDetail1.setREPORT_DATE(reportDate1);
	    resubDetail1.setREPORT_VERSION(newVersion);
	    resubDetail1.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 5️⃣ ARCHIVAL SUMMARY
	    // ====================================================

	    BDISB1_Archival_Summary_Entity archSummary1 = new BDISB1_Archival_Summary_Entity();

	    BeanUtils.copyProperties(updatedEntity1, archSummary1);

	    archSummary1.setREPORT_DATE(reportDate1);
	    archSummary1.setREPORT_VERSION(newVersion);
	    archSummary1.setREPORT_RESUBDATE(now);


	    // ====================================================
	    // 6️⃣ ARCHIVAL DETAIL
	    // ====================================================

	    BDISB1_Archival_Detail_Entity archDetail1 = new BDISB1_Archival_Detail_Entity();

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
	public void updateReport(BDISB1_Summary_Entity request1) {

	    try {

	        StringBuilder sql = new StringBuilder(
	            "UPDATE BRRS_BDISB1_SUMMARYTABLE SET ");

	        List<Object> params = new ArrayList<>();

	        for (int i = 5; i <= 11; i++) {

	            sql.append("R").append(i).append("_RECORD_NUMBER=?,")
	               .append("R").append(i).append("_TITLE=?,")
	               .append("R").append(i).append("_FIRST_NAME=?,")
	               .append("R").append(i).append("_MIDDLE_NAME=?,")
	               .append("R").append(i).append("_SURNAME=?,")
	               .append("R").append(i).append("_PREVIOUS_NAME=?,")
	               .append("R").append(i).append("_GENDER=?,")
	               .append("R").append(i).append("_IDENTIFICATION_TYPE=?,")
	               .append("R").append(i).append("_PASSPORT_NUMBER=?,")
	               .append("R").append(i).append("_DATE_OF_BIRTH=?,")
	               .append("R").append(i).append("_HOME_ADDRESS=?,")
	               .append("R").append(i).append("_POSTAL_ADDRESS=?,")
	               .append("R").append(i).append("_RESIDENCE=?,")
	               .append("R").append(i).append("_EMAIL=?,")
	               .append("R").append(i).append("_LANDLINE=?,")
	               .append("R").append(i).append("_MOBILE_PHONE_NUMBER=?,")
	               .append("R").append(i).append("_MOBILE_MONEY_NUMBER=?,")
	               .append("R").append(i).append("_PRODUCT_TYPE=?,")
	               .append("R").append(i).append("_ACCOUNT_BY_OWNERSHIP=?,")
	               .append("R").append(i).append("_ACCOUNT_NUMBER=?,")
	               .append("R").append(i).append("_ACCOUNT_HOLDER_INDICATOR=?,")
	               .append("R").append(i).append("_STATUS_OF_ACCOUNT=?,")
	               .append("R").append(i).append("_NOT_FIT_FOR_STP=?,")
	               .append("R").append(i).append("_BRANCH_CODE_AND_NAME=?,")
	               .append("R").append(i).append("_ACCOUNT_BALANCE_IN_PULA=?,")
	               .append("R").append(i).append("_CURRENCY_OF_ACCOUNT=?,")
	               .append("R").append(i).append("_EXCHANGE_RATE=?,");

	            params.add(getValue(request1, "getR" + i + "_RECORD_NUMBER"));
	            params.add(getValue(request1, "getR" + i + "_TITLE"));
	            params.add(getValue(request1, "getR" + i + "_FIRST_NAME"));
	            params.add(getValue(request1, "getR" + i + "_MIDDLE_NAME"));
	            params.add(getValue(request1, "getR" + i + "_SURNAME"));
	            params.add(getValue(request1, "getR" + i + "_PREVIOUS_NAME"));
	            params.add(getValue(request1, "getR" + i + "_GENDER"));
	            params.add(getValue(request1, "getR" + i + "_IDENTIFICATION_TYPE"));
	            params.add(getValue(request1, "getR" + i + "_PASSPORT_NUMBER"));
	            params.add(getValue(request1, "getR" + i + "_DATE_OF_BIRTH"));
	            params.add(getValue(request1, "getR" + i + "_HOME_ADDRESS"));
	            params.add(getValue(request1, "getR" + i + "_POSTAL_ADDRESS"));
	            params.add(getValue(request1, "getR" + i + "_RESIDENCE"));
	            params.add(getValue(request1, "getR" + i + "_EMAIL"));
	            params.add(getValue(request1, "getR" + i + "_LANDLINE"));
	            params.add(getValue(request1, "getR" + i + "_MOBILE_PHONE_NUMBER"));
	            params.add(getValue(request1, "getR" + i + "_MOBILE_MONEY_NUMBER"));
	            params.add(getValue(request1, "getR" + i + "_PRODUCT_TYPE"));
	            params.add(getValue(request1, "getR" + i + "_ACCOUNT_BY_OWNERSHIP"));
	            params.add(getValue(request1, "getR" + i + "_ACCOUNT_NUMBER"));
	            params.add(getValue(request1, "getR" + i + "_ACCOUNT_HOLDER_INDICATOR"));
	            params.add(getValue(request1, "getR" + i + "_STATUS_OF_ACCOUNT"));
	            params.add(getValue(request1, "getR" + i + "_NOT_FIT_FOR_STP"));
	            params.add(getValue(request1, "getR" + i + "_BRANCH_CODE_AND_NAME"));
	            params.add(getValue(request1, "getR" + i + "_ACCOUNT_BALANCE_IN_PULA"));
	            params.add(getValue(request1, "getR" + i + "_CURRENCY_OF_ACCOUNT"));
	            params.add(getValue(request1, "getR" + i + "_EXCHANGE_RATE"));
	        }

	        sql.deleteCharAt(sql.length() - 1);

	        sql.append(" WHERE REPORT_DATE=?");

	        params.add(request1.getREPORT_DATE());

	        int count = jdbcTemplate.update(sql.toString(), params.toArray());

	        System.out.println("Rows Updated = " + count);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Error while updating BRRS_BDISB1 Report", e);
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

	public List<Object[]> getBRRS_BDISB1Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<BDISB1_Archival_Summary_Entity> latestArchivalList = getdatabydateListWithVersion1();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (BDISB1_Archival_Summary_Entity entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getREPORT_DATE(), entity.getREPORT_VERSION(),
							entity.getREPORT_RESUBDATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BRRS_BDISB1 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getBRRS_BDISB1Archival() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " + "FROM BRRS_BDISB1_ARCHIVALTABLE_SUMMARY "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"),
				rs.getBigDecimal("REPORT_VERSION"), rs.getDate("REPORT_RESUBDATE") });
	}

	//normal excel
	
	public byte[] getBDISB1Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

// Convert string to Date
		Date reportDate = dateformat.parse(todate);

// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelBDISB1ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null ) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<BDISB1_Archival_Summary_Entity> T1Master =getdatabydateListarchival1(dateformat.parse(todate), version);

// Generate Excel for RESUB
			return BRRSBDISB1ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

// Default (LIVE) case
		List<BDISB1_Summary_Entity> dataList1 = getDataByDate1(reportDate);

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

					BDISB1_Summary_Entity record = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

//Cell1 - R5_TITLE
					Cell cell0 = row.createCell(0);
					if (record.getR5_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR5_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R5_TITLE
					Cell cell1 = row.createCell(1);
					if (record.getR5_TITLE() != null) {
						cell1.setCellValue(record.getR5_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R5_FIRST_NAME
					Cell cell2 = row.createCell(2);
					if (record.getR5_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR5_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R5_MIDDLE_NAME
					Cell cell3 = row.createCell(3);
					if (record.getR5_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR5_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R5_SURNAME
					Cell cell4 = row.createCell(4);
					if (record.getR5_SURNAME() != null) {
						cell4.setCellValue(record.getR5_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R5_PREVIOUS_NAME
					Cell cell5 = row.createCell(5);
					if (record.getR5_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR5_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R5_GENDER
					Cell cell6 = row.createCell(6);
					if (record.getR5_GENDER() != null) {
						cell6.setCellValue(record.getR5_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R5_IDENTIFICATION_TYPE
					Cell cell7 = row.createCell(7);
					if (record.getR5_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR5_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R5_PASSPORT_NUMBER
					Cell cell8 = row.createCell(8);
					if (record.getR5_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR5_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R5_DATE_OF_BIRTH
					Cell cell9 = row.createCell(9);
					if (record.getR5_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR5_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R5_HOME_ADDRESS
					Cell cell10 = row.createCell(10);
					if (record.getR5_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR5_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R5_POSTAL_ADDRESS
					Cell cell11 = row.createCell(11);
					if (record.getR5_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR5_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R5_RESIDENCE
					Cell cell12 = row.createCell(12);
					if (record.getR5_RESIDENCE() != null) {
						cell12.setCellValue(record.getR5_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R5_EMAIL
					Cell cell13 = row.createCell(13);
					if (record.getR5_EMAIL() != null) {
						cell13.setCellValue(record.getR5_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R5_LANDLINE
					Cell cell14 = row.createCell(14);
					if (record.getR5_LANDLINE() != null) {
						cell14.setCellValue(record.getR5_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R5_MOBILE_PHONE_NUMBER
					Cell cell15 = row.createCell(15);
					if (record.getR5_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR5_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R5_MOBILE_MONEY_NUMBER
					Cell cell16 = row.createCell(16);
					if (record.getR5_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR5_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R5_PRODUCT_TYPE
					Cell cell17 = row.createCell(17);
					if (record.getR5_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR5_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R5_ACCOUNT_BY_OWNERSHIP
					Cell cell18 = row.createCell(18);
					if (record.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR5_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R5_ACCOUNT_NUMBER
					Cell cell19 = row.createCell(19);
					if (record.getR5_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR5_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
					Cell cell20 = row.createCell(20);
					if (record.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R5_STATUS_OF_ACCOUNT
					Cell cell21 = row.createCell(21);
					if (record.getR5_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR5_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R5_NOT_FIT_FOR_STP
					Cell cell22 = row.createCell(22);
					if (record.getR5_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR5_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R5_BRANCH_CODE_AND_NAME
					Cell cell23 = row.createCell(23);
					if (record.getR5_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR5_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
					Cell cell24 = row.createCell(24);
					if (record.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R5_CURRENCY_OF_ACCOUNT
					Cell cell25 = row.createCell(25);
					if (record.getR5_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR5_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R5_EXCHANGE_RATE
					Cell cell26 = row.createCell(26);
					if (record.getR5_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR5_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
//====================== R6 ======================

//Cell1 - R5_TITLE
					cell0 = row.createCell(0);
					if (record.getR6_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR6_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R6_TITLE
					cell1 = row.createCell(1);
					if (record.getR6_TITLE() != null) {
						cell1.setCellValue(record.getR6_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R6_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR6_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR6_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R6_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR6_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR6_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R6_SURNAME
					cell4 = row.createCell(4);
					if (record.getR6_SURNAME() != null) {
						cell4.setCellValue(record.getR6_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R6_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR6_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR6_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R6_GENDER
					cell6 = row.createCell(6);
					if (record.getR6_GENDER() != null) {
						cell6.setCellValue(record.getR6_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R6_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR6_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR6_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R6_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR6_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR6_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R6_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR6_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR6_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R6_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR6_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR6_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R6_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR6_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR6_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R6_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR6_RESIDENCE() != null) {
						cell12.setCellValue(record.getR6_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R6_EMAIL
					cell13 = row.createCell(13);
					if (record.getR6_EMAIL() != null) {
						cell13.setCellValue(record.getR6_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R6_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR6_LANDLINE() != null) {
						cell14.setCellValue(record.getR6_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R6_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR6_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR6_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R6_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR6_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR6_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R6_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR6_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR6_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R6_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR6_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R6_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR6_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR6_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R6_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR6_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR6_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R6_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR6_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR6_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R6_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR6_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR6_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R6_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR6_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR6_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R6_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR6_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR6_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
//====================== R7 ======================

//Cell0 - R7_RECORD_NUMBER
					cell0 = row.createCell(0);
					if (record.getR7_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR7_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R7_TITLE
					cell1 = row.createCell(1);
					if (record.getR7_TITLE() != null) {
						cell1.setCellValue(record.getR7_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R7_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR7_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR7_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R7_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR7_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR7_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R7_SURNAME
					cell4 = row.createCell(4);
					if (record.getR7_SURNAME() != null) {
						cell4.setCellValue(record.getR7_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R7_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR7_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR7_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R7_GENDER
					cell6 = row.createCell(6);
					if (record.getR7_GENDER() != null) {
						cell6.setCellValue(record.getR7_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R7_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR7_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR7_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R7_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR7_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR7_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R7_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR7_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR7_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R7_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR7_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR7_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R7_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR7_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR7_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R7_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR7_RESIDENCE() != null) {
						cell12.setCellValue(record.getR7_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R7_EMAIL
					cell13 = row.createCell(13);
					if (record.getR7_EMAIL() != null) {
						cell13.setCellValue(record.getR7_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R7_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR7_LANDLINE() != null) {
						cell14.setCellValue(record.getR7_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R7_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR7_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR7_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R7_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR7_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR7_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R7_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR7_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR7_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R7_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR7_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R7_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR7_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR7_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R7_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR7_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR7_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R7_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR7_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR7_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R7_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR7_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR7_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R7_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR7_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR7_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R7_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR7_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR7_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
//====================== R8 ======================

//Cell0 - R8_RECORD_NUMBER
					cell0 = row.createCell(0);
					if (record.getR8_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR8_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R8_TITLE
					cell1 = row.createCell(1);
					if (record.getR8_TITLE() != null) {
						cell1.setCellValue(record.getR8_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R8_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR8_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR8_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R8_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR8_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR8_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R8_SURNAME
					cell4 = row.createCell(4);
					if (record.getR8_SURNAME() != null) {
						cell4.setCellValue(record.getR8_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R8_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR8_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR8_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R8_GENDER
					cell6 = row.createCell(6);
					if (record.getR8_GENDER() != null) {
						cell6.setCellValue(record.getR8_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R8_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR8_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR8_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R8_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR8_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR8_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R8_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR8_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR8_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R8_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR8_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR8_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R8_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR8_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR8_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R8_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR8_RESIDENCE() != null) {
						cell12.setCellValue(record.getR8_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R8_EMAIL
					cell13 = row.createCell(13);
					if (record.getR8_EMAIL() != null) {
						cell13.setCellValue(record.getR8_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R8_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR8_LANDLINE() != null) {
						cell14.setCellValue(record.getR8_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R8_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR8_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR8_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R8_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR8_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR8_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R8_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR8_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR8_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R8_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR8_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R8_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR8_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR8_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R8_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR8_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR8_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R8_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR8_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR8_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R8_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR8_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR8_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R8_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR8_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR8_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R8_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR8_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR8_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

//====================== R9 ======================
					row = sheet.getRow(8);
//Cell0 - R9_RECORD_NUMBER
					cell0 = row.createCell(0);
					if (record.getR9_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR9_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R9_TITLE
					cell1 = row.createCell(1);
					if (record.getR9_TITLE() != null) {
						cell1.setCellValue(record.getR9_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R9_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR9_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR9_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R9_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR9_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR9_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R9_SURNAME
					cell4 = row.createCell(4);
					if (record.getR9_SURNAME() != null) {
						cell4.setCellValue(record.getR9_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R9_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR9_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR9_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R9_GENDER
					cell6 = row.createCell(6);
					if (record.getR9_GENDER() != null) {
						cell6.setCellValue(record.getR9_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R9_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR9_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR9_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R9_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR9_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR9_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R9_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR9_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR9_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R9_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR9_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR9_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R9_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR9_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR9_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R9_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR9_RESIDENCE() != null) {
						cell12.setCellValue(record.getR9_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R9_EMAIL
					cell13 = row.createCell(13);
					if (record.getR9_EMAIL() != null) {
						cell13.setCellValue(record.getR9_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R9_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR9_LANDLINE() != null) {
						cell14.setCellValue(record.getR9_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R9_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR9_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR9_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R9_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR9_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR9_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R9_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR9_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR9_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R9_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR9_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R9_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR9_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR9_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R9_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR9_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR9_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R9_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR9_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR9_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R9_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR9_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR9_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R9_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR9_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR9_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R9_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR9_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR9_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
//====================== R10 ======================

//Cell0 - R10_RECORD_NUMBER
					cell0 = row.createCell(0);
					if (record.getR10_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR10_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R10_TITLE
					cell1 = row.createCell(1);
					if (record.getR10_TITLE() != null) {
						cell1.setCellValue(record.getR10_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R10_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR10_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR10_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R10_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR10_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR10_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R10_SURNAME
					cell4 = row.createCell(4);
					if (record.getR10_SURNAME() != null) {
						cell4.setCellValue(record.getR10_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R10_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR10_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR10_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R10_GENDER
					cell6 = row.createCell(6);
					if (record.getR10_GENDER() != null) {
						cell6.setCellValue(record.getR10_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R10_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR10_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR10_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R10_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR10_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR10_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R10_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR10_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR10_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R10_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR10_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR10_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R10_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR10_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR10_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R10_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR10_RESIDENCE() != null) {
						cell12.setCellValue(record.getR10_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R10_EMAIL
					cell13 = row.createCell(13);
					if (record.getR10_EMAIL() != null) {
						cell13.setCellValue(record.getR10_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R10_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR10_LANDLINE() != null) {
						cell14.setCellValue(record.getR10_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R10_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR10_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR10_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R10_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR10_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR10_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R10_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR10_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR10_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R10_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR10_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R10_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR10_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR10_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R10_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR10_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR10_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R10_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR10_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR10_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R10_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR10_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR10_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R10_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR10_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR10_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R10_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR10_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR10_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

//====================== R11 ======================
					row = sheet.getRow(10);
//Cell0 - R11_RECORD_NUMBER
					cell0 = row.createCell(0);
					if (record.getR11_RECORD_NUMBER() != null) {
						cell0.setCellValue(record.getR11_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

//Cell1 - R11_TITLE
					cell1 = row.createCell(1);
					if (record.getR11_TITLE() != null) {
						cell1.setCellValue(record.getR11_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

//Cell2 - R11_FIRST_NAME
					cell2 = row.createCell(2);
					if (record.getR11_FIRST_NAME() != null) {
						cell2.setCellValue(record.getR11_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

//Cell3 - R11_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record.getR11_MIDDLE_NAME() != null) {
						cell3.setCellValue(record.getR11_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

//Cell4 - R11_SURNAME
					cell4 = row.createCell(4);
					if (record.getR11_SURNAME() != null) {
						cell4.setCellValue(record.getR11_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

//Cell5 - R11_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record.getR11_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record.getR11_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

//Cell6 - R11_GENDER
					cell6 = row.createCell(6);
					if (record.getR11_GENDER() != null) {
						cell6.setCellValue(record.getR11_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

//Cell7 - R11_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record.getR11_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record.getR11_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

//Cell8 - R11_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record.getR11_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record.getR11_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

//Cell9 - R11_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record.getR11_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record.getR11_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

//Cell10 - R11_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record.getR11_HOME_ADDRESS() != null) {
						cell10.setCellValue(record.getR11_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

//Cell11 - R11_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record.getR11_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record.getR11_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

//Cell12 - R11_RESIDENCE
					cell12 = row.createCell(12);
					if (record.getR11_RESIDENCE() != null) {
						cell12.setCellValue(record.getR11_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

//Cell13 - R11_EMAIL
					cell13 = row.createCell(13);
					if (record.getR11_EMAIL() != null) {
						cell13.setCellValue(record.getR11_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

//Cell14 - R11_LANDLINE
					cell14 = row.createCell(14);
					if (record.getR11_LANDLINE() != null) {
						cell14.setCellValue(record.getR11_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

//Cell15 - R11_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record.getR11_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record.getR11_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

//Cell16 - R11_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record.getR11_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record.getR11_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

//Cell17 - R11_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record.getR11_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record.getR11_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

//Cell18 - R11_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record.getR11_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

//Cell19 - R11_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record.getR11_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record.getR11_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

//Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

					cell20 = row.createCell(20);
					if (record.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

//Cell21 - R11_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record.getR11_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record.getR11_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

//Cell22 - R11_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record.getR11_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record.getR11_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

//Cell23 - R11_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record.getR11_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record.getR11_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

//Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

//Cell25 - R11_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record.getR11_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record.getR11_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

//Cell26 - R11_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record.getR11_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record.getR11_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "BDISB1 SUMMARY", null, "BRRS_BDISB1_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}

	public byte[] getExcelBDISB1ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<BDISB1_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_BDISB1 report. Returning empty result.");
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

					BDISB1_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}

					// Cell1 - R5_TITLE
					Cell cell0 = row.createCell(0);
					if (record1.getR5_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR5_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R5_TITLE
					Cell cell1 = row.createCell(1);
					if (record1.getR5_TITLE() != null) {
						cell1.setCellValue(record1.getR5_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R5_FIRST_NAME
					Cell cell2 = row.createCell(2);
					if (record1.getR5_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR5_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R5_MIDDLE_NAME
					Cell cell3 = row.createCell(3);
					if (record1.getR5_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR5_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R5_SURNAME
					Cell cell4 = row.createCell(4);
					if (record1.getR5_SURNAME() != null) {
						cell4.setCellValue(record1.getR5_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R5_PREVIOUS_NAME
					Cell cell5 = row.createCell(5);
					if (record1.getR5_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR5_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R5_GENDER
					Cell cell6 = row.createCell(6);
					if (record1.getR5_GENDER() != null) {
						cell6.setCellValue(record1.getR5_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R5_IDENTIFICATION_TYPE
					Cell cell7 = row.createCell(7);
					if (record1.getR5_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR5_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R5_PASSPORT_NUMBER
					Cell cell8 = row.createCell(8);
					if (record1.getR5_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR5_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R5_DATE_OF_BIRTH
					Cell cell9 = row.createCell(9);
					if (record1.getR5_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR5_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R5_HOME_ADDRESS
					Cell cell10 = row.createCell(10);
					if (record1.getR5_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR5_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R5_POSTAL_ADDRESS
					Cell cell11 = row.createCell(11);
					if (record1.getR5_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR5_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R5_RESIDENCE
					Cell cell12 = row.createCell(12);
					if (record1.getR5_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR5_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R5_EMAIL
					Cell cell13 = row.createCell(13);
					if (record1.getR5_EMAIL() != null) {
						cell13.setCellValue(record1.getR5_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R5_LANDLINE
					Cell cell14 = row.createCell(14);
					if (record1.getR5_LANDLINE() != null) {
						cell14.setCellValue(record1.getR5_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R5_MOBILE_PHONE_NUMBER
					Cell cell15 = row.createCell(15);
					if (record1.getR5_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR5_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R5_MOBILE_MONEY_NUMBER
					Cell cell16 = row.createCell(16);
					if (record1.getR5_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR5_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R5_PRODUCT_TYPE
					Cell cell17 = row.createCell(17);
					if (record1.getR5_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR5_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R5_ACCOUNT_BY_OWNERSHIP
					Cell cell18 = row.createCell(18);
					if (record1.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR5_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R5_ACCOUNT_NUMBER
					Cell cell19 = row.createCell(19);
					if (record1.getR5_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR5_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
					Cell cell20 = row.createCell(20);
					if (record1.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R5_STATUS_OF_ACCOUNT
					Cell cell21 = row.createCell(21);
					if (record1.getR5_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR5_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R5_NOT_FIT_FOR_STP
					Cell cell22 = row.createCell(22);
					if (record1.getR5_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR5_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R5_BRANCH_CODE_AND_NAME
					Cell cell23 = row.createCell(23);
					if (record1.getR5_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR5_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
					Cell cell24 = row.createCell(24);
					if (record1.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R5_CURRENCY_OF_ACCOUNT
					Cell cell25 = row.createCell(25);
					if (record1.getR5_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR5_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R5_EXCHANGE_RATE
					Cell cell26 = row.createCell(26);
					if (record1.getR5_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR5_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
					// ====================== R6 ======================

					// Cell1 - R5_TITLE
					cell0 = row.createCell(0);
					if (record1.getR6_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR6_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R6_TITLE
					cell1 = row.createCell(1);
					if (record1.getR6_TITLE() != null) {
						cell1.setCellValue(record1.getR6_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R6_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR6_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR6_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R6_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR6_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR6_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R6_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR6_SURNAME() != null) {
						cell4.setCellValue(record1.getR6_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R6_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR6_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR6_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R6_GENDER
					cell6 = row.createCell(6);
					if (record1.getR6_GENDER() != null) {
						cell6.setCellValue(record1.getR6_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R6_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR6_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR6_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R6_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR6_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR6_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R6_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR6_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR6_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R6_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR6_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR6_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R6_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR6_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR6_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R6_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR6_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR6_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R6_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR6_EMAIL() != null) {
						cell13.setCellValue(record1.getR6_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R6_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR6_LANDLINE() != null) {
						cell14.setCellValue(record1.getR6_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R6_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR6_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR6_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R6_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR6_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR6_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R6_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR6_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR6_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R6_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR6_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R6_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR6_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR6_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R6_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR6_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR6_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R6_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR6_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR6_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R6_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR6_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR6_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R6_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR6_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR6_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R6_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR6_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
					// ====================== R7 ======================

					// Cell0 - R7_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR7_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR7_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R7_TITLE
					cell1 = row.createCell(1);
					if (record1.getR7_TITLE() != null) {
						cell1.setCellValue(record1.getR7_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R7_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR7_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR7_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R7_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR7_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR7_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R7_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR7_SURNAME() != null) {
						cell4.setCellValue(record1.getR7_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R7_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR7_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR7_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R7_GENDER
					cell6 = row.createCell(6);
					if (record1.getR7_GENDER() != null) {
						cell6.setCellValue(record1.getR7_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R7_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR7_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR7_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R7_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR7_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR7_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R7_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR7_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR7_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R7_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR7_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR7_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R7_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR7_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR7_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R7_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR7_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR7_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R7_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR7_EMAIL() != null) {
						cell13.setCellValue(record1.getR7_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R7_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR7_LANDLINE() != null) {
						cell14.setCellValue(record1.getR7_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R7_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR7_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR7_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R7_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR7_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR7_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R7_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR7_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR7_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R7_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR7_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R7_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR7_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR7_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R7_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR7_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR7_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R7_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR7_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR7_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R7_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR7_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR7_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R7_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR7_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR7_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R7_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR7_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
					// ====================== R8 ======================

					// Cell0 - R8_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR8_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR8_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R8_TITLE
					cell1 = row.createCell(1);
					if (record1.getR8_TITLE() != null) {
						cell1.setCellValue(record1.getR8_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R8_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR8_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR8_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R8_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR8_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR8_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R8_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR8_SURNAME() != null) {
						cell4.setCellValue(record1.getR8_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R8_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR8_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR8_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R8_GENDER
					cell6 = row.createCell(6);
					if (record1.getR8_GENDER() != null) {
						cell6.setCellValue(record1.getR8_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R8_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR8_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR8_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R8_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR8_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR8_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R8_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR8_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR8_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R8_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR8_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR8_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R8_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR8_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR8_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R8_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR8_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR8_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R8_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR8_EMAIL() != null) {
						cell13.setCellValue(record1.getR8_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R8_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR8_LANDLINE() != null) {
						cell14.setCellValue(record1.getR8_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R8_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR8_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR8_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R8_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR8_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR8_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R8_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR8_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR8_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R8_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR8_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R8_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR8_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR8_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R8_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR8_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR8_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R8_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR8_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR8_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R8_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR8_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR8_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R8_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR8_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR8_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R8_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR8_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					// ====================== R9 ======================
					row = sheet.getRow(8);
					// Cell0 - R9_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR9_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR9_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R9_TITLE
					cell1 = row.createCell(1);
					if (record1.getR9_TITLE() != null) {
						cell1.setCellValue(record1.getR9_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R9_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR9_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR9_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R9_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR9_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR9_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R9_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR9_SURNAME() != null) {
						cell4.setCellValue(record1.getR9_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R9_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR9_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR9_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R9_GENDER
					cell6 = row.createCell(6);
					if (record1.getR9_GENDER() != null) {
						cell6.setCellValue(record1.getR9_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R9_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR9_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR9_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R9_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR9_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR9_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R9_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR9_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR9_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R9_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR9_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR9_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R9_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR9_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR9_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R9_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR9_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR9_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R9_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR9_EMAIL() != null) {
						cell13.setCellValue(record1.getR9_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R9_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR9_LANDLINE() != null) {
						cell14.setCellValue(record1.getR9_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R9_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR9_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR9_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R9_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR9_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR9_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R9_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR9_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR9_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R9_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR9_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R9_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR9_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR9_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R9_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR9_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR9_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R9_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR9_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR9_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R9_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR9_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR9_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R9_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR9_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR9_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R9_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR9_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					// ====================== R10 ======================

					// Cell0 - R10_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR10_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR10_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R10_TITLE
					cell1 = row.createCell(1);
					if (record1.getR10_TITLE() != null) {
						cell1.setCellValue(record1.getR10_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R10_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR10_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR10_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R10_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR10_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR10_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R10_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR10_SURNAME() != null) {
						cell4.setCellValue(record1.getR10_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R10_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR10_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR10_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R10_GENDER
					cell6 = row.createCell(6);
					if (record1.getR10_GENDER() != null) {
						cell6.setCellValue(record1.getR10_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R10_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR10_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR10_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R10_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR10_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR10_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R10_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR10_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR10_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R10_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR10_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR10_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R10_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR10_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR10_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R10_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR10_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR10_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R10_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR10_EMAIL() != null) {
						cell13.setCellValue(record1.getR10_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R10_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR10_LANDLINE() != null) {
						cell14.setCellValue(record1.getR10_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R10_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR10_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR10_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R10_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR10_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR10_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R10_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR10_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR10_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R10_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR10_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R10_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR10_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR10_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R10_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR10_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR10_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R10_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR10_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR10_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R10_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR10_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR10_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R10_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR10_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR10_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R10_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR10_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					// ====================== R11 ======================
					row = sheet.getRow(10);
					// Cell0 - R11_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR11_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR11_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R11_TITLE
					cell1 = row.createCell(1);
					if (record1.getR11_TITLE() != null) {
						cell1.setCellValue(record1.getR11_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R11_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR11_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR11_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R11_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR11_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR11_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R11_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR11_SURNAME() != null) {
						cell4.setCellValue(record1.getR11_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R11_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR11_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR11_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R11_GENDER
					cell6 = row.createCell(6);
					if (record1.getR11_GENDER() != null) {
						cell6.setCellValue(record1.getR11_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R11_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR11_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR11_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R11_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR11_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR11_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R11_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR11_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR11_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R11_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR11_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR11_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R11_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR11_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR11_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R11_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR11_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR11_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R11_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR11_EMAIL() != null) {
						cell13.setCellValue(record1.getR11_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R11_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR11_LANDLINE() != null) {
						cell14.setCellValue(record1.getR11_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R11_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR11_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR11_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R11_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR11_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR11_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R11_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR11_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR11_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R11_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR11_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R11_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR11_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR11_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R11_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR11_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR11_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R11_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR11_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR11_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R11_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR11_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR11_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R11_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR11_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR11_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R11_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR11_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "BDISB1 ARCHIVAL SUMMARY", null, "BRRS_BDISB1_ARCHIVALTABLE_SUMMARY");
			}
			return out.toByteArray();
		}
	}





/// Downloaded for Archival & Resub
	public byte[] BRRSBDISB1ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<BDISB1_Archival_Summary_Entity> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for M_BDISB1 report. Returning empty result.");
			return new byte[0];
		}

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
			int startRow = 4;

			if (!dataList1.isEmpty()) {
				for (int i = 0; i < dataList1.size(); i++) {

					BDISB1_Archival_Summary_Entity record1 = dataList1.get(i);
					System.out.println("rownumber=" + startRow + i);
					Row row = sheet.getRow(startRow + i);
					if (row == null) {
						row = sheet.createRow(startRow + i);
					}
					// Cell1 - R5_TITLE
					Cell cell0 = row.createCell(0);
					if (record1.getR5_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR5_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R5_TITLE
					Cell cell1 = row.createCell(1);
					if (record1.getR5_TITLE() != null) {
						cell1.setCellValue(record1.getR5_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R5_FIRST_NAME
					Cell cell2 = row.createCell(2);
					if (record1.getR5_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR5_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R5_MIDDLE_NAME
					Cell cell3 = row.createCell(3);
					if (record1.getR5_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR5_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R5_SURNAME
					Cell cell4 = row.createCell(4);
					if (record1.getR5_SURNAME() != null) {
						cell4.setCellValue(record1.getR5_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R5_PREVIOUS_NAME
					Cell cell5 = row.createCell(5);
					if (record1.getR5_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR5_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R5_GENDER
					Cell cell6 = row.createCell(6);
					if (record1.getR5_GENDER() != null) {
						cell6.setCellValue(record1.getR5_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R5_IDENTIFICATION_TYPE
					Cell cell7 = row.createCell(7);
					if (record1.getR5_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR5_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R5_PASSPORT_NUMBER
					Cell cell8 = row.createCell(8);
					if (record1.getR5_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR5_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R5_DATE_OF_BIRTH
					Cell cell9 = row.createCell(9);
					if (record1.getR5_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR5_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R5_HOME_ADDRESS
					Cell cell10 = row.createCell(10);
					if (record1.getR5_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR5_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R5_POSTAL_ADDRESS
					Cell cell11 = row.createCell(11);
					if (record1.getR5_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR5_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R5_RESIDENCE
					Cell cell12 = row.createCell(12);
					if (record1.getR5_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR5_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R5_EMAIL
					Cell cell13 = row.createCell(13);
					if (record1.getR5_EMAIL() != null) {
						cell13.setCellValue(record1.getR5_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R5_LANDLINE
					Cell cell14 = row.createCell(14);
					if (record1.getR5_LANDLINE() != null) {
						cell14.setCellValue(record1.getR5_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R5_MOBILE_PHONE_NUMBER
					Cell cell15 = row.createCell(15);
					if (record1.getR5_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR5_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R5_MOBILE_MONEY_NUMBER
					Cell cell16 = row.createCell(16);
					if (record1.getR5_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR5_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R5_PRODUCT_TYPE
					Cell cell17 = row.createCell(17);
					if (record1.getR5_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR5_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R5_ACCOUNT_BY_OWNERSHIP
					Cell cell18 = row.createCell(18);
					if (record1.getR5_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR5_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R5_ACCOUNT_NUMBER
					Cell cell19 = row.createCell(19);
					if (record1.getR5_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR5_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R5_ACCOUNT_HOLDER_INDICATOR
					Cell cell20 = row.createCell(20);
					if (record1.getR5_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR5_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R5_STATUS_OF_ACCOUNT
					Cell cell21 = row.createCell(21);
					if (record1.getR5_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR5_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R5_NOT_FIT_FOR_STP
					Cell cell22 = row.createCell(22);
					if (record1.getR5_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR5_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R5_BRANCH_CODE_AND_NAME
					Cell cell23 = row.createCell(23);
					if (record1.getR5_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR5_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R5_ACCOUNT_BALANCE_IN_PULA
					Cell cell24 = row.createCell(24);
					if (record1.getR5_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR5_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R5_CURRENCY_OF_ACCOUNT
					Cell cell25 = row.createCell(25);
					if (record1.getR5_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR5_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R5_EXCHANGE_RATE
					Cell cell26 = row.createCell(26);
					if (record1.getR5_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR5_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(5);
					// ====================== R6 ======================

					// Cell1 - R5_TITLE
					cell0 = row.createCell(0);
					if (record1.getR6_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR6_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R6_TITLE
					cell1 = row.createCell(1);
					if (record1.getR6_TITLE() != null) {
						cell1.setCellValue(record1.getR6_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R6_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR6_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR6_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R6_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR6_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR6_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R6_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR6_SURNAME() != null) {
						cell4.setCellValue(record1.getR6_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R6_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR6_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR6_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R6_GENDER
					cell6 = row.createCell(6);
					if (record1.getR6_GENDER() != null) {
						cell6.setCellValue(record1.getR6_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R6_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR6_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR6_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R6_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR6_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR6_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R6_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR6_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR6_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R6_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR6_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR6_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R6_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR6_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR6_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R6_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR6_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR6_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R6_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR6_EMAIL() != null) {
						cell13.setCellValue(record1.getR6_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R6_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR6_LANDLINE() != null) {
						cell14.setCellValue(record1.getR6_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R6_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR6_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR6_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R6_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR6_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR6_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R6_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR6_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR6_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R6_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR6_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR6_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R6_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR6_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR6_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R6_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR6_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR6_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R6_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR6_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR6_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R6_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR6_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR6_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R6_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR6_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR6_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R6_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR6_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR6_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R6_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR6_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR6_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R6_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR6_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR6_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(6);
					// ====================== R7 ======================

					// Cell0 - R7_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR7_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR7_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R7_TITLE
					cell1 = row.createCell(1);
					if (record1.getR7_TITLE() != null) {
						cell1.setCellValue(record1.getR7_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R7_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR7_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR7_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R7_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR7_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR7_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R7_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR7_SURNAME() != null) {
						cell4.setCellValue(record1.getR7_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R7_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR7_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR7_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R7_GENDER
					cell6 = row.createCell(6);
					if (record1.getR7_GENDER() != null) {
						cell6.setCellValue(record1.getR7_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R7_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR7_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR7_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R7_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR7_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR7_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R7_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR7_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR7_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R7_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR7_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR7_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R7_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR7_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR7_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R7_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR7_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR7_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R7_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR7_EMAIL() != null) {
						cell13.setCellValue(record1.getR7_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R7_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR7_LANDLINE() != null) {
						cell14.setCellValue(record1.getR7_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R7_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR7_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR7_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R7_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR7_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR7_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R7_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR7_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR7_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R7_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR7_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR7_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R7_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR7_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR7_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R7_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R7_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR7_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR7_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R7_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR7_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR7_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R7_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR7_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR7_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R7_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR7_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR7_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R7_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR7_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR7_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R7_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR7_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR7_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(7);
					// ====================== R8 ======================

					// Cell0 - R8_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR8_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR8_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R8_TITLE
					cell1 = row.createCell(1);
					if (record1.getR8_TITLE() != null) {
						cell1.setCellValue(record1.getR8_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R8_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR8_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR8_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R8_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR8_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR8_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R8_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR8_SURNAME() != null) {
						cell4.setCellValue(record1.getR8_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R8_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR8_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR8_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R8_GENDER
					cell6 = row.createCell(6);
					if (record1.getR8_GENDER() != null) {
						cell6.setCellValue(record1.getR8_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R8_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR8_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR8_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R8_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR8_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR8_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R8_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR8_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR8_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R8_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR8_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR8_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R8_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR8_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR8_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R8_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR8_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR8_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R8_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR8_EMAIL() != null) {
						cell13.setCellValue(record1.getR8_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R8_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR8_LANDLINE() != null) {
						cell14.setCellValue(record1.getR8_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R8_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR8_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR8_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R8_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR8_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR8_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R8_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR8_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR8_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R8_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR8_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR8_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R8_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR8_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR8_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R8_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR8_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR8_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R8_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR8_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR8_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R8_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR8_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR8_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R8_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR8_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR8_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R8_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR8_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR8_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R8_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR8_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR8_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R8_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR8_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR8_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					// ====================== R9 ======================
					row = sheet.getRow(8);
					// Cell0 - R9_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR9_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR9_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R9_TITLE
					cell1 = row.createCell(1);
					if (record1.getR9_TITLE() != null) {
						cell1.setCellValue(record1.getR9_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R9_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR9_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR9_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R9_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR9_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR9_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R9_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR9_SURNAME() != null) {
						cell4.setCellValue(record1.getR9_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R9_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR9_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR9_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R9_GENDER
					cell6 = row.createCell(6);
					if (record1.getR9_GENDER() != null) {
						cell6.setCellValue(record1.getR9_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R9_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR9_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR9_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R9_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR9_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR9_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R9_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR9_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR9_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R9_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR9_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR9_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R9_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR9_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR9_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R9_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR9_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR9_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R9_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR9_EMAIL() != null) {
						cell13.setCellValue(record1.getR9_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R9_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR9_LANDLINE() != null) {
						cell14.setCellValue(record1.getR9_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R9_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR9_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR9_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R9_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR9_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR9_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R9_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR9_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR9_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R9_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR9_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR9_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R9_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR9_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR9_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R9_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR9_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR9_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R9_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR9_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR9_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R9_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR9_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR9_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R9_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR9_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR9_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R9_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR9_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR9_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R9_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR9_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR9_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R9_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR9_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR9_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					row = sheet.getRow(9);
					// ====================== R10 ======================

					// Cell0 - R10_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR10_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR10_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R10_TITLE
					cell1 = row.createCell(1);
					if (record1.getR10_TITLE() != null) {
						cell1.setCellValue(record1.getR10_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R10_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR10_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR10_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R10_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR10_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR10_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R10_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR10_SURNAME() != null) {
						cell4.setCellValue(record1.getR10_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R10_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR10_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR10_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R10_GENDER
					cell6 = row.createCell(6);
					if (record1.getR10_GENDER() != null) {
						cell6.setCellValue(record1.getR10_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R10_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR10_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR10_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R10_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR10_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR10_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R10_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR10_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR10_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R10_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR10_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR10_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R10_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR10_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR10_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R10_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR10_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR10_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R10_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR10_EMAIL() != null) {
						cell13.setCellValue(record1.getR10_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R10_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR10_LANDLINE() != null) {
						cell14.setCellValue(record1.getR10_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R10_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR10_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR10_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R10_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR10_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR10_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R10_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR10_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR10_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R10_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR10_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR10_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R10_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR10_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR10_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R10_ACCOUNT_HOLDER_INDICATOR
					cell20 = row.createCell(20);
					if (record1.getR10_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR10_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R10_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR10_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR10_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R10_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR10_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR10_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R10_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR10_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR10_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R10_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR10_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR10_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R10_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR10_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR10_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R10_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR10_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR10_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
					}

					// ====================== R11 ======================
					row = sheet.getRow(10);
					// Cell0 - R11_record1_NUMBER
					cell0 = row.createCell(0);
					if (record1.getR11_RECORD_NUMBER() != null) {
						cell0.setCellValue(record1.getR11_RECORD_NUMBER());
						cell0.setCellStyle(textStyle);
					} else {
						cell0.setCellValue("");
						cell0.setCellStyle(textStyle);
					}

					// Cell1 - R11_TITLE
					cell1 = row.createCell(1);
					if (record1.getR11_TITLE() != null) {
						cell1.setCellValue(record1.getR11_TITLE());
						cell1.setCellStyle(textStyle);
					} else {
						cell1.setCellValue("");
						cell1.setCellStyle(textStyle);
					}

					// Cell2 - R11_FIRST_NAME
					cell2 = row.createCell(2);
					if (record1.getR11_FIRST_NAME() != null) {
						cell2.setCellValue(record1.getR11_FIRST_NAME());
						cell2.setCellStyle(textStyle);
					} else {
						cell2.setCellValue("");
						cell2.setCellStyle(textStyle);
					}

					// Cell3 - R11_MIDDLE_NAME
					cell3 = row.createCell(3);
					if (record1.getR11_MIDDLE_NAME() != null) {
						cell3.setCellValue(record1.getR11_MIDDLE_NAME());
						cell3.setCellStyle(textStyle);
					} else {
						cell3.setCellValue("");
						cell3.setCellStyle(textStyle);
					}

					// Cell4 - R11_SURNAME
					cell4 = row.createCell(4);
					if (record1.getR11_SURNAME() != null) {
						cell4.setCellValue(record1.getR11_SURNAME());
						cell4.setCellStyle(textStyle);
					} else {
						cell4.setCellValue("");
						cell4.setCellStyle(textStyle);
					}

					// Cell5 - R11_PREVIOUS_NAME
					cell5 = row.createCell(5);
					if (record1.getR11_PREVIOUS_NAME() != null) {
						cell5.setCellValue(record1.getR11_PREVIOUS_NAME());
						cell5.setCellStyle(textStyle);
					} else {
						cell5.setCellValue("");
						cell5.setCellStyle(textStyle);
					}

					// Cell6 - R11_GENDER
					cell6 = row.createCell(6);
					if (record1.getR11_GENDER() != null) {
						cell6.setCellValue(record1.getR11_GENDER());
						cell6.setCellStyle(textStyle);
					} else {
						cell6.setCellValue("");
						cell6.setCellStyle(textStyle);
					}

					// Cell7 - R11_IDENTIFICATION_TYPE
					cell7 = row.createCell(7);
					if (record1.getR11_IDENTIFICATION_TYPE() != null) {
						cell7.setCellValue(record1.getR11_IDENTIFICATION_TYPE());
						cell7.setCellStyle(textStyle);
					} else {
						cell7.setCellValue("");
						cell7.setCellStyle(textStyle);
					}

					// Cell8 - R11_PASSPORT_NUMBER
					cell8 = row.createCell(8);
					if (record1.getR11_PASSPORT_NUMBER() != null) {
						cell8.setCellValue(record1.getR11_PASSPORT_NUMBER());
						cell8.setCellStyle(textStyle);
					} else {
						cell8.setCellValue("");
						cell8.setCellStyle(textStyle);
					}

					// Cell9 - R11_DATE_OF_BIRTH
					cell9 = row.createCell(9);
					if (record1.getR11_DATE_OF_BIRTH() != null) {
						cell9.setCellValue(record1.getR11_DATE_OF_BIRTH());
						cell9.setCellStyle(dateStyle);
					} else {
						cell9.setCellValue("");
						cell9.setCellStyle(textStyle);
					}

					// Cell10 - R11_HOME_ADDRESS
					cell10 = row.createCell(10);
					if (record1.getR11_HOME_ADDRESS() != null) {
						cell10.setCellValue(record1.getR11_HOME_ADDRESS());
						cell10.setCellStyle(textStyle);
					} else {
						cell10.setCellValue("");
						cell10.setCellStyle(textStyle);
					}

					// Cell11 - R11_POSTAL_ADDRESS
					cell11 = row.createCell(11);
					if (record1.getR11_POSTAL_ADDRESS() != null) {
						cell11.setCellValue(record1.getR11_POSTAL_ADDRESS());
						cell11.setCellStyle(textStyle);
					} else {
						cell11.setCellValue("");
						cell11.setCellStyle(textStyle);
					}

					// Cell12 - R11_RESIDENCE
					cell12 = row.createCell(12);
					if (record1.getR11_RESIDENCE() != null) {
						cell12.setCellValue(record1.getR11_RESIDENCE());
						cell12.setCellStyle(textStyle);
					} else {
						cell12.setCellValue("");
						cell12.setCellStyle(textStyle);
					}

					// Cell13 - R11_EMAIL
					cell13 = row.createCell(13);
					if (record1.getR11_EMAIL() != null) {
						cell13.setCellValue(record1.getR11_EMAIL());
						cell13.setCellStyle(textStyle);
					} else {
						cell13.setCellValue("");
						cell13.setCellStyle(textStyle);
					}

					// Cell14 - R11_LANDLINE
					cell14 = row.createCell(14);
					if (record1.getR11_LANDLINE() != null) {
						cell14.setCellValue(record1.getR11_LANDLINE());
						cell14.setCellStyle(textStyle);
					} else {
						cell14.setCellValue("");
						cell14.setCellStyle(textStyle);
					}

					// Cell15 - R11_MOBILE_PHONE_NUMBER
					cell15 = row.createCell(15);
					if (record1.getR11_MOBILE_PHONE_NUMBER() != null) {
						cell15.setCellValue(record1.getR11_MOBILE_PHONE_NUMBER());
						cell15.setCellStyle(textStyle);
					} else {
						cell15.setCellValue("");
						cell15.setCellStyle(textStyle);
					}

					// Cell16 - R11_MOBILE_MONEY_NUMBER
					cell16 = row.createCell(16);
					if (record1.getR11_MOBILE_MONEY_NUMBER() != null) {
						cell16.setCellValue(record1.getR11_MOBILE_MONEY_NUMBER());
						cell16.setCellStyle(textStyle);
					} else {
						cell16.setCellValue("");
						cell16.setCellStyle(textStyle);
					}

					// Cell17 - R11_PRODUCT_TYPE
					cell17 = row.createCell(17);
					if (record1.getR11_PRODUCT_TYPE() != null) {
						cell17.setCellValue(record1.getR11_PRODUCT_TYPE());
						cell17.setCellStyle(textStyle);
					} else {
						cell17.setCellValue("");
						cell17.setCellStyle(textStyle);
					}

					// Cell18 - R11_ACCOUNT_BY_OWNERSHIP
					cell18 = row.createCell(18);
					if (record1.getR11_ACCOUNT_BY_OWNERSHIP() != null) {
						cell18.setCellValue(record1.getR11_ACCOUNT_BY_OWNERSHIP());
						cell18.setCellStyle(textStyle);
					} else {
						cell18.setCellValue("");
						cell18.setCellStyle(textStyle);
					}

					// Cell19 - R11_ACCOUNT_NUMBER
					cell19 = row.createCell(19);
					if (record1.getR11_ACCOUNT_NUMBER() != null) {
						cell19.setCellValue(record1.getR11_ACCOUNT_NUMBER());
						cell19.setCellStyle(textStyle);
					} else {
						cell19.setCellValue("");
						cell19.setCellStyle(textStyle);
					}

					// Cell20 - R11_ACCOUNT_HOLDER_INDICATOR

					cell20 = row.createCell(20);
					if (record1.getR11_ACCOUNT_HOLDER_INDICATOR() != null) {
						cell20.setCellValue(record1.getR11_ACCOUNT_HOLDER_INDICATOR().doubleValue());
						cell20.setCellStyle(numberStyle);
					} else {
						cell20.setCellValue("");
						cell20.setCellStyle(textStyle);
					}

					// Cell21 - R11_STATUS_OF_ACCOUNT
					cell21 = row.createCell(21);
					if (record1.getR11_STATUS_OF_ACCOUNT() != null) {
						cell21.setCellValue(record1.getR11_STATUS_OF_ACCOUNT());
						cell21.setCellStyle(textStyle);
					} else {
						cell21.setCellValue("");
						cell21.setCellStyle(textStyle);
					}

					// Cell22 - R11_NOT_FIT_FOR_STP
					cell22 = row.createCell(22);
					if (record1.getR11_NOT_FIT_FOR_STP() != null) {
						cell22.setCellValue(record1.getR11_NOT_FIT_FOR_STP());
						cell22.setCellStyle(textStyle);
					} else {
						cell22.setCellValue("");
						cell22.setCellStyle(textStyle);
					}

					// Cell23 - R11_BRANCH_CODE_AND_NAME
					cell23 = row.createCell(23);
					if (record1.getR11_BRANCH_CODE_AND_NAME() != null) {
						cell23.setCellValue(record1.getR11_BRANCH_CODE_AND_NAME());
						cell23.setCellStyle(textStyle);
					} else {
						cell23.setCellValue("");
						cell23.setCellStyle(textStyle);
					}

					// Cell24 - R11_ACCOUNT_BALANCE_IN_PULA
					cell24 = row.createCell(24);
					if (record1.getR11_ACCOUNT_BALANCE_IN_PULA() != null) {
						cell24.setCellValue(record1.getR11_ACCOUNT_BALANCE_IN_PULA().doubleValue());
						cell24.setCellStyle(numberStyle);
					} else {
						cell24.setCellValue("");
						cell24.setCellStyle(textStyle);
					}

					// Cell25 - R11_CURRENCY_OF_ACCOUNT
					cell25 = row.createCell(25);
					if (record1.getR11_CURRENCY_OF_ACCOUNT() != null) {
						cell25.setCellValue(record1.getR11_CURRENCY_OF_ACCOUNT());
						cell25.setCellStyle(textStyle);
					} else {
						cell25.setCellValue("");
						cell25.setCellStyle(textStyle);
					}

					// Cell26 - R11_EXCHANGE_RATE
					cell26 = row.createCell(26);
					if (record1.getR11_EXCHANGE_RATE() != null) {
						cell26.setCellValue(record1.getR11_EXCHANGE_RATE().doubleValue());
						cell26.setCellStyle(numberStyle);
					} else {
						cell26.setCellValue("");
						cell26.setCellStyle(textStyle);
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
				auditService.createBusinessAudit(userid, "DOWNLOAD", "BDISB1 RESUB SUMMARY", null, "BRRS_BDISB1_RESUB_SUMMARYTABLE");
			}
			return out.toByteArray();
		}
	}


	


}
