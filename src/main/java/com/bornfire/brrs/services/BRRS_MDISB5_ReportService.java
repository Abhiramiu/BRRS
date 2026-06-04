
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

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional

public class BRRS_MDISB5_ReportService {
	private static final Logger logger = LoggerFactory.getLogger(BRRS_MDISB5_ReportService.class);

	@Autowired
	private Environment env;

	@Autowired
	SessionFactory sessionFactory;

	// ENTITY MANAGER (Acts like Repository)
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	

	SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");

	
	// Fetch data by report date
	public List<MDISB5_Summary_Entity1> getDataByDate1(Date reportDate) {

		String sql = "SELECT * FROM BRRS_MDISB5_SUMMARYTABLE1 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new MDISB5RowMapper_SUMMARY1());
	}

	// Fetch data by report date
	public List<MDISB5_Summary_Entity2> getDataByDate2(Date reportDate) {

		String sql = "SELECT * FROM BRRS_MDISB5_SUMMARYTABLE2 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new MDISB5RowMapper_SUMMARY2());
	}

	// Fetch data by report date
	public List<MDISB5_Summary_Entity3> getDataByDate3(Date reportDate) {

		String sql = "SELECT * FROM BRRS_MDISB5_SUMMARYTABLE3 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate }, new MDISB5RowMapper_SUMMARY3());
	}

	// GET REPORT_DATE + REPORT_VERSION

	public List<Object[]> getMDISB5Archival1() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1 "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Object[]> getMDISB5Archival2() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY2 "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	public List<Object[]> getMDISB5Archival3() {

		String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY3 "
				+ "ORDER BY REPORT_VERSION";

		return jdbcTemplate.query(sql,
				(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
	}

	
	///RESUB 
	
	// GET REPORT_DATE + REPORT_VERSION

		public List<Object[]> getMDISB5RESUB1() {

			String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_RESUBTABLE_SUMMARY1 "
					+ "ORDER BY REPORT_VERSION";

			return jdbcTemplate.query(sql,
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
		}

		public List<Object[]> getMDISB5RESUB2() {

			String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_RESUBTABLE_SUMMARY2 "
					+ "ORDER BY REPORT_VERSION";

			return jdbcTemplate.query(sql,
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
		}

		public List<Object[]> getMDISB5RESUB3() {

			String sql = "SELECT REPORT_DATE, REPORT_VERSION " + "FROM BRRS_MDISB5_RESUBTABLE_SUMMARY3 "
					+ "ORDER BY REPORT_VERSION";

			return jdbcTemplate.query(sql,
					(rs, rowNum) -> new Object[] { rs.getDate("REPORT_DATE"), rs.getBigDecimal("REPORT_VERSION") });
		}

		
	
	// GET ARCHIVAL FULL DATA BY DATE + VERSION

	public List<MDISB5_Archival_Summary_Entity1> getdatabydateListarchival1(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new MDISB5ArchivalRowMapper_SUMMARY1());
	}

	public List<MDISB5_Archival_Summary_Entity2> getdatabydateListarchival2(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new MDISB5ArchivalRowMapper_SUMMARY2());
	}

	public List<MDISB5_Archival_Summary_Entity3> getdatabydateListarchival3(Date REPORT_DATE,
			BigDecimal REPORT_VERSION) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY3 " + "WHERE REPORT_DATE = ? "
				+ "AND REPORT_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
				new MDISB5ArchivalRowMapper_SUMMARY3());
	}

	// GET RESUB FULL DATA BY DATE + VERSION

		public List<MDISB5_RESUB_Summary_Entity1> RESUBgetdatabydateListarchival1(Date REPORT_DATE,
				BigDecimal REPORT_VERSION) {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY1 " + "WHERE REPORT_DATE = ? "
					+ "AND REPORT_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
					new MDISB5RESUBRowMapper_SUMMARY1());
		}

		public List<MDISB5_RESUB_Summary_Entity2> RESUBgetdatabydateListarchival2(Date REPORT_DATE,
				BigDecimal REPORT_VERSION) {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY2 " + "WHERE REPORT_DATE = ? "
					+ "AND REPORT_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
					new MDISB5RESUBRowMapper_SUMMARY2());
		}

		public List<MDISB5_RESUB_Summary_Entity3> RESUBgetdatabydateListarchival3(Date REPORT_DATE,
				BigDecimal REPORT_VERSION) {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY3 " + "WHERE REPORT_DATE = ? "
					+ "AND REPORT_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { REPORT_DATE, REPORT_VERSION },
					new MDISB5RESUBRowMapper_SUMMARY3());
		}
	
	
	// GET ALL WITH VERSION

	public List<MDISB5_Archival_Summary_Entity1> getdatabydateListWithVersion1() {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new MDISB5ArchivalRowMapper_SUMMARY1());
	}

	public List<MDISB5_Archival_Summary_Entity2> getdatabydateListWithVersion2() {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY2 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new MDISB5ArchivalRowMapper_SUMMARY2());
	}

	public List<MDISB5_Archival_Summary_Entity3> getdatabydateListWithVersion3() {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY3 " + "WHERE REPORT_VERSION IS NOT NULL "
				+ "ORDER BY REPORT_VERSION ASC";

		return jdbcTemplate.query(sql, new MDISB5ArchivalRowMapper_SUMMARY3());
	}

	
	// GET ALL WITH VERSION

		public List<MDISB5_RESUB_Summary_Entity1> RESUBgetdatabydateListWithVersion1() {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY1 " + "WHERE REPORT_VERSION IS NOT NULL "
					+ "ORDER BY REPORT_VERSION ASC";

			return jdbcTemplate.query(sql, new MDISB5RESUBRowMapper_SUMMARY1());
		}

		public List<MDISB5_RESUB_Summary_Entity2> RESUBgetdatabydateListWithVersion2() {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY2 " + "WHERE REPORT_VERSION IS NOT NULL "
					+ "ORDER BY REPORT_VERSION ASC";

			return jdbcTemplate.query(sql, new MDISB5RESUBRowMapper_SUMMARY2());
		}

		public List<MDISB5_RESUB_Summary_Entity3> RESUBgetdatabydateListWithVersion3() {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_SUMMARY3 " + "WHERE REPORT_VERSION IS NOT NULL "
					+ "ORDER BY REPORT_VERSION ASC";

			return jdbcTemplate.query(sql, new MDISB5RESUBRowMapper_SUMMARY3());
		}
		
	// GET MAX VERSION BY DATE

	public BigDecimal findMaxVersion1(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1 "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal findMaxVersion2(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY2 "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal findMaxVersion3(Date REPORT_DATE) {

		String sql = "SELECT MAX(REPORT_VERSION) " + "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY3 "
				+ "WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	// GET MAX VERSION BY DATE

	public BigDecimal RESUBfindMaxVersion1(Date REPORT_DATE) {
	    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_MDISB5_RESUBTABLE_SUMMARY1 WHERE REPORT_DATE = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal RESUBfindMaxVersion2(Date REPORT_DATE) {
	    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_MDISB5_RESUBTABLE_SUMMARY2 WHERE REPORT_DATE = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}

	public BigDecimal RESUBfindMaxVersion3(Date REPORT_DATE) {
	    String sql = "SELECT MAX(REPORT_VERSION) FROM BRRS_MDISB5_RESUBTABLE_SUMMARY3 WHERE REPORT_DATE = ?";
	    return jdbcTemplate.queryForObject(sql, new Object[] { REPORT_DATE }, BigDecimal.class);
	}
	
	// 1. BY DATE + LABEL + CRITERIA

	public List<MDISB5_Detail_Entity1> findByDetailReportDateAndLabelAndCriteria1(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new MDISB5DetaillRowMapper_DETAIL1());
	}
	
	// 1. BY DATE 
	public MDISB5_Detail_Entity1 findByReportDate(Date reportDate) {

	    String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 WHERE REPORT_DATE = ?";

	    return jdbcTemplate.queryForObject(
	            sql,
	            new Object[] { reportDate },
	            new MDISB5DetaillRowMapper_DETAIL1());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<MDISB5_Detail_Entity1> getDetaildatabydateList1(Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new MDISB5DetaillRowMapper_DETAIL1());
	}

	// 3. PAGINATION

	public List<MDISB5_Detail_Entity1> getDetaildatabydateList1(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new MDISB5DetaillRowMapper_DETAIL1());
	}

	// 4. COUNT

	public int getDetaildatacount1(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_MDISB5_DETAILTABLE1 WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<MDISB5_Detail_Entity1> GetDetailDataByRowIdAndColumnId1(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new MDISB5DetaillRowMapper_DETAIL1());
	}
	// 6. BY ACCOUNT NUMBER

	public MDISB5_Detail_Entity1 findByAcctnumber1(String acctNumber) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new MDISB5DetaillRowMapper_DETAIL1());
	}

	// 1. GET BY DATE + VERSION

	public List<MDISB5_Archival_Detail_Entity1> getArchivalDetaildatabydateList1(Date reportdate,
			BigDecimal version) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL1 "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, version },
				new MDISB5ArchivalDetaillRowMapper_ARCHIVAL1());
	}

	
	
	 // 2. FILTER BY LABEL + CRITERIA + DATE + VERSION
	  
	  public List<MDISB5_Archival_Detail_Entity1>
	  GetArchivalDataByRowIdAndColumnId1(String reportLabel, String
	  reportAddlCriteria1, Date reportdate, String dataEntryVersion) {
	  
	  String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL1 " +
	  "WHERE REPORT_LABEL = ? " + "AND REPORT_ADDL_CRITERIA_1 = ? " +
	  "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";
	  
	  return jdbcTemplate.query(sql, new Object[] { reportLabel,
	  reportAddlCriteria1, reportdate, dataEntryVersion }, new
	  MDISB5ArchivalDetaillRowMapper_ARCHIVAL1()); }
	  
	 

	// 1. GET BY DATE + VERSION

	public List<MDISB5_RESUB_Detail_Entity1> getRESUBDetaildatabydateList1(Date reportdate, BigDecimal version) {

		String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL1 "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, version },
				new MDISB5RESUBDetaillRowMapper_RESUB1());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<MDISB5_RESUB_Detail_Entity1> GetRESUBDataByRowIdAndColumnId1(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL1 " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new MDISB5RESUBDetaillRowMapper_RESUB1());
	}

	//////// DETAIL & ARCHIVAL 2

	// 1. BY DATE + LABEL + CRITERIA

	public List<MDISB5_Detail_Entity2> findByDetailReportDateAndLabelAndCriteria2(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE2 "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new MDISB5DetaillRowMapper_DETAIL2());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<MDISB5_Detail_Entity2> getDetaildatabydateList2(Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE2 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new MDISB5DetaillRowMapper_DETAIL2());
	}

	// 3. PAGINATION

	public List<MDISB5_Detail_Entity2> getDetaildatabydateList2(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE2 "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new MDISB5DetaillRowMapper_DETAIL2());
	}

	// 4. COUNT

	public int getDetaildatacount2(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_MDISB5_DETAILTABLE2 WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<MDISB5_Detail_Entity2> GetDetailDataByRowIdAndColumnId2(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE2 "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new MDISB5DetaillRowMapper_DETAIL2());
	}
	// 6. BY ACCOUNT NUMBER

	public MDISB5_Detail_Entity2 findByAcctnumber2(String acctNumber) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE2 WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new MDISB5DetaillRowMapper_DETAIL2());
	}

	// 1. GET BY DATE + VERSION

	public List<MDISB5_Archival_Detail_Entity2> getArchivalDetaildatabydateList2(Date reportdate,
			BigDecimal version) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL2 "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, version },
				new MDISB5ArchivalDetaillRowMapper_ARCHIVAL2());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<MDISB5_Archival_Detail_Entity2> GetArchivalDataByRowIdAndColumnId2(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL2 " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new MDISB5ArchivalDetaillRowMapper_ARCHIVAL2());
	}

	// 1. GET BY DATE + VERSION

	public List<MDISB5_RESUB_Detail_Entity2> getRESUBDetaildatabydateList2(Date reportdate, BigDecimal version) {

		String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL2 "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, version },
				new MDISB5RESUBDetaillRowMapper_RESUB2());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<MDISB5_RESUB_Detail_Entity2> GetRESUBDataByRowIdAndColumnId2(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL2 " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new MDISB5RESUBDetaillRowMapper_RESUB2());
	}

	//// DETAIL & ARCHIVAL 3

	// 1. BY DATE + LABEL + CRITERIA

	public List<MDISB5_Detail_Entity3> findByDetailReportDateAndLabelAndCriteria3(Date reportDate, String reportLabel,
			String reportAddlCriteria1) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE3 "
				+ "WHERE REPORT_DATE = ? AND REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ?";

		return jdbcTemplate.query(sql, new Object[] { reportDate, reportLabel, reportAddlCriteria1 },
				new MDISB5DetaillRowMapper_DETAIL3());
	}

	// 2. GET ALL (BY DATE - simple)

	public List<MDISB5_Detail_Entity3> getDetaildatabydateList3(Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE3 WHERE REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate }, new MDISB5DetaillRowMapper_DETAIL3());
	}

	// 3. PAGINATION

	public List<MDISB5_Detail_Entity3> getDetaildatabydateList3(Date reportdate, int offset, int limit) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE3 "
				+ "WHERE REPORT_DATE = ? OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

		return jdbcTemplate.query(sql, new Object[] { reportdate, offset, limit },
				new MDISB5DetaillRowMapper_DETAIL3());
	}

	// 4. COUNT

	public int getDetaildatacount3(Date reportdate) {

		String sql = "SELECT COUNT(*) FROM BRRS_MDISB5_DETAILTABLE3 WHERE REPORT_DATE = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { reportdate }, Integer.class);
	}

	// 5. BY LABEL + CRITERIA

	public List<MDISB5_Detail_Entity3> GetDetailDataByRowIdAndColumnId3(String reportLabel, String reportAddlCriteria1,
			Date reportdate) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE3 "
				+ "WHERE REPORT_LABEL = ? AND REPORT_ADDL_CRITERIA_1 = ? AND REPORT_DATE = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate },
				new MDISB5DetaillRowMapper_DETAIL3());
	}
	// 6. BY ACCOUNT NUMBER

	public MDISB5_Detail_Entity3 findByAcctnumber3(String acctNumber) {

		String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE3 WHERE ACCT_NUMBER = ?";

		return jdbcTemplate.queryForObject(sql, new Object[] { acctNumber }, new MDISB5DetaillRowMapper_DETAIL3());
	}

	
	// 7. BY REPORT DATE detail1
	public MDISB5_Detail_Entity1 findDetailByReportDate(Date reportDate) {

	    String sql = "SELECT * FROM BRRS_MDISB5_DETAILTABLE1 WHERE REPORT_DATE = ?";

	    List<MDISB5_Detail_Entity1> result = jdbcTemplate.query(
	            sql,
	            new Object[] { reportDate },
	            new MDISB5DetaillRowMapper_DETAIL1());

	    return result.isEmpty() ? null : result.get(0);
	}
	
	//  BY REPORT DATE summary1
	public MDISB5_Summary_Entity1 findSummaryByReportDate(Date reportDate) {

	    String sql = "SELECT * FROM BRRS_MDISB5_SUMMARYTABLE1 WHERE REPORT_DATE = ?";

	    List<MDISB5_Summary_Entity1> result = jdbcTemplate.query(
	            sql,
	            new Object[] { reportDate },
	            new MDISB5RowMapper_SUMMARY1());

	    return result.isEmpty() ? null : result.get(0);
	}
	
	public MDISB5_Summary_Entity2 findSummary2ByReportDate(Date reportDate) {

	    return (MDISB5_Summary_Entity2) sessionFactory
	            .getCurrentSession()
	            .createQuery(
	                    "FROM MDISB5_Summary_Entity2 WHERE reportDate = :reportDate")
	            .setParameter("reportDate", reportDate)
	            .uniqueResult();
	}

	public MDISB5_Detail_Entity2 findDetail2ByReportDate(Date reportDate) {

	    return (MDISB5_Detail_Entity2) sessionFactory
	            .getCurrentSession()
	            .createQuery(
	                    "FROM MDISB5_Detail_Entity2 WHERE reportDate = :reportDate")
	            .setParameter("reportDate", reportDate)
	            .uniqueResult();
	}
	
	public MDISB5_Summary_Entity3 findSummary3ByReportDate(Date reportDate) {

	    return (MDISB5_Summary_Entity3) sessionFactory
	            .getCurrentSession()
	            .createQuery(
	                    "FROM MDISB5_Summary_Entity3 WHERE reportDate = :reportDate")
	            .setParameter("reportDate", reportDate)
	            .uniqueResult();
	}

	public MDISB5_Detail_Entity3 findDetail3ByReportDate(Date reportDate) {

	    return (MDISB5_Detail_Entity3) sessionFactory
	            .getCurrentSession()
	            .createQuery(
	                    "FROM MDISB5_Detail_Entity3 WHERE reportDate = :reportDate")
	            .setParameter("reportDate", reportDate)
	            .uniqueResult();
	}
	
	// 1. GET BY DATE + VERSION

	public List<MDISB5_Archival_Detail_Entity3> getArchivalDetaildatabydateList3(Date reportdate,
			BigDecimal version) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL3 "
				+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportdate, version },
				new MDISB5ArchivalDetaillRowMapper_ARCHIVAL3());
	}

	// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

	public List<MDISB5_Archival_Detail_Entity3> GetArchivalDataByRowIdAndColumnId3(String reportLabel,
			String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

		String sql = "SELECT * FROM BRRS_MDISB5_ARCHIVALTABLE_DETAIL3 " + "WHERE REPORT_LABEL = ? "
				+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

		return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
				new MDISB5ArchivalDetaillRowMapper_ARCHIVAL3());
	}
	
	
	// 1. GET BY DATE + VERSION

		public List<MDISB5_RESUB_Detail_Entity3> getRESUBDetaildatabydateList3(Date reportdate,
				BigDecimal version) {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL3 "
					+ "WHERE REPORT_DATE = ? AND DATA_ENTRY_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { reportdate, version },
					new MDISB5RESUBDetaillRowMapper_RESUB3());
		}

		// 2. FILTER BY LABEL + CRITERIA + DATE + VERSION

		public List<MDISB5_RESUB_Detail_Entity3> GetRESUBDataByRowIdAndColumnId3(String reportLabel,
				String reportAddlCriteria1, Date reportdate, String dataEntryVersion) {

			String sql = "SELECT * FROM BRRS_MDISB5_RESUBTABLE_DETAIL3 " + "WHERE REPORT_LABEL = ? "
					+ "AND REPORT_ADDL_CRITERIA_1 = ? " + "AND REPORT_DATE = ? " + "AND DATA_ENTRY_VERSION = ?";

			return jdbcTemplate.query(sql, new Object[] { reportLabel, reportAddlCriteria1, reportdate, dataEntryVersion },
					new MDISB5RESUBDetaillRowMapper_RESUB3());
		}
		
		
		public MDISB5_Summary_Entity1 saveEntity(MDISB5_Summary_Entity1 entity) {
		    return (MDISB5_Summary_Entity1)
		        sessionFactory.getCurrentSession().merge(entity);
		}
		
		

	// ROW MAPPER SUMMARY1

	class MDISB5RowMapper_SUMMARY1 implements RowMapper<MDISB5_Summary_Entity1> {

		@Override
		public MDISB5_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Summary_Entity1 obj = new MDISB5_Summary_Entity1();

			obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
			obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
			obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
			obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

			obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
			obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
			obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
			obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

			obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
			obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
			obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
			obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

			obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
			obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
			obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
			obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

			obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
			obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
			obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

			obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
			obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
			obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

			obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
			obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
			obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

			obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
			obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
			obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

			obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
			obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
			obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

			obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
			obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
			obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

			obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
			obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
			obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

	public static class MDISB5_Summary_Entity1 {

		private String R5_NAME_OF_SHAREHOLDER;
		private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R5_NUMBER_OF_ACCOUNTS;
		private BigDecimal R5_AMOUNT;

		private String R6_NAME_OF_SHAREHOLDER;
		private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R6_NUMBER_OF_ACCOUNTS;
		private BigDecimal R6_AMOUNT;

		private String R7_NAME_OF_SHAREHOLDER;
		private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R7_NUMBER_OF_ACCOUNTS;
		private BigDecimal R7_AMOUNT;

		private String R8_NAME_OF_SHAREHOLDER;
		private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R8_NUMBER_OF_ACCOUNTS;
		private BigDecimal R8_AMOUNT;

		private String R9_NAME_OF_SHAREHOLDER;
		private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R9_NUMBER_OF_ACCOUNTS;
		private BigDecimal R9_AMOUNT;

		private String R10_NAME_OF_SHAREHOLDER;
		private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R10_NUMBER_OF_ACCOUNTS;
		private BigDecimal R10_AMOUNT;

		private String R11_NAME_OF_SHAREHOLDER;
		private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R11_NUMBER_OF_ACCOUNTS;
		private BigDecimal R11_AMOUNT;

		private String R12_NAME_OF_SHAREHOLDER;
		private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R12_NUMBER_OF_ACCOUNTS;
		private BigDecimal R12_AMOUNT;

		private String R13_NAME_OF_SHAREHOLDER;
		private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R13_NUMBER_OF_ACCOUNTS;
		private BigDecimal R13_AMOUNT;

		private String R14_NAME_OF_SHAREHOLDER;
		private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R14_NUMBER_OF_ACCOUNTS;
		private BigDecimal R14_AMOUNT;

		private String R15_NAME_OF_SHAREHOLDER;
		private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R15_NUMBER_OF_ACCOUNTS;
		private BigDecimal R15_AMOUNT;

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

		public String getR5_NAME_OF_SHAREHOLDER() {
			return R5_NAME_OF_SHAREHOLDER;
		}

		public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
			R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
			return R5_PERCENTAGE_SHAREHOLDING;
		}

		public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
			R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
			return R5_NUMBER_OF_ACCOUNTS;
		}

		public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
			R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR5_AMOUNT() {
			return R5_AMOUNT;
		}

		public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
			R5_AMOUNT = r5_AMOUNT;
		}

		public String getR6_NAME_OF_SHAREHOLDER() {
			return R6_NAME_OF_SHAREHOLDER;
		}

		public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
			R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
			return R6_PERCENTAGE_SHAREHOLDING;
		}

		public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
			R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
			return R6_NUMBER_OF_ACCOUNTS;
		}

		public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
			R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR6_AMOUNT() {
			return R6_AMOUNT;
		}

		public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
			R6_AMOUNT = r6_AMOUNT;
		}

		public String getR7_NAME_OF_SHAREHOLDER() {
			return R7_NAME_OF_SHAREHOLDER;
		}

		public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
			R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
			return R7_PERCENTAGE_SHAREHOLDING;
		}

		public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
			R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
			return R7_NUMBER_OF_ACCOUNTS;
		}

		public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
			R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR7_AMOUNT() {
			return R7_AMOUNT;
		}

		public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
			R7_AMOUNT = r7_AMOUNT;
		}

		public String getR8_NAME_OF_SHAREHOLDER() {
			return R8_NAME_OF_SHAREHOLDER;
		}

		public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
			R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
			return R8_PERCENTAGE_SHAREHOLDING;
		}

		public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
			R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
			return R8_NUMBER_OF_ACCOUNTS;
		}

		public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
			R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR8_AMOUNT() {
			return R8_AMOUNT;
		}

		public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
			R8_AMOUNT = r8_AMOUNT;
		}

		public String getR9_NAME_OF_SHAREHOLDER() {
			return R9_NAME_OF_SHAREHOLDER;
		}

		public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
			R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
			return R9_PERCENTAGE_SHAREHOLDING;
		}

		public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
			R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
			return R9_NUMBER_OF_ACCOUNTS;
		}

		public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
			R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR9_AMOUNT() {
			return R9_AMOUNT;
		}

		public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
			R9_AMOUNT = r9_AMOUNT;
		}

		public String getR10_NAME_OF_SHAREHOLDER() {
			return R10_NAME_OF_SHAREHOLDER;
		}

		public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
			R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
			return R10_PERCENTAGE_SHAREHOLDING;
		}

		public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
			R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
			return R10_NUMBER_OF_ACCOUNTS;
		}

		public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
			R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR10_AMOUNT() {
			return R10_AMOUNT;
		}

		public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
			R10_AMOUNT = r10_AMOUNT;
		}

		public String getR11_NAME_OF_SHAREHOLDER() {
			return R11_NAME_OF_SHAREHOLDER;
		}

		public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
			R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
			return R11_PERCENTAGE_SHAREHOLDING;
		}

		public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
			R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
			return R11_NUMBER_OF_ACCOUNTS;
		}

		public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
			R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public String getR12_NAME_OF_SHAREHOLDER() {
			return R12_NAME_OF_SHAREHOLDER;
		}

		public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
			R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
			return R12_PERCENTAGE_SHAREHOLDING;
		}

		public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
			R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
			return R12_NUMBER_OF_ACCOUNTS;
		}

		public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
			R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public String getR13_NAME_OF_SHAREHOLDER() {
			return R13_NAME_OF_SHAREHOLDER;
		}

		public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
			R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
			return R13_PERCENTAGE_SHAREHOLDING;
		}

		public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
			R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
			return R13_NUMBER_OF_ACCOUNTS;
		}

		public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
			R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public String getR14_NAME_OF_SHAREHOLDER() {
			return R14_NAME_OF_SHAREHOLDER;
		}

		public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
			R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
			return R14_PERCENTAGE_SHAREHOLDING;
		}

		public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
			R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
			return R14_NUMBER_OF_ACCOUNTS;
		}

		public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
			R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public String getR15_NAME_OF_SHAREHOLDER() {
			return R15_NAME_OF_SHAREHOLDER;
		}

		public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
			R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
			return R15_PERCENTAGE_SHAREHOLDING;
		}

		public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
			R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
			return R15_NUMBER_OF_ACCOUNTS;
		}

		public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
			R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
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

	//// NORMAL SUMMARY ENTITY 2

	class MDISB5RowMapper_SUMMARY2 implements RowMapper<MDISB5_Summary_Entity2> {

		@Override
		public MDISB5_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Summary_Entity2 obj = new MDISB5_Summary_Entity2();

			obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
			obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

			obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
			obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

			obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
			obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

			obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
			obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

			obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
			obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

			obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
			obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

			obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
			obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

			obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
			obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

			obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
			obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

			obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
			obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

			obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
			obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

			obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
			obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

			obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
			obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

			obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
			obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

	public static class MDISB5_Summary_Entity2 {

		private String R20_NAME_OF_BOARD_MEMBERS;
		private String R20_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R20_NUMBER_OF_ACCOUNTS;
		private BigDecimal R20_AMOUNT;

		private String R21_NAME_OF_BOARD_MEMBERS;
		private String R21_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R21_NUMBER_OF_ACCOUNTS;
		private BigDecimal R21_AMOUNT;

		private String R22_NAME_OF_BOARD_MEMBERS;
		private String R22_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R22_NUMBER_OF_ACCOUNTS;
		private BigDecimal R22_AMOUNT;

		private String R23_NAME_OF_BOARD_MEMBERS;
		private String R23_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R23_NUMBER_OF_ACCOUNTS;
		private BigDecimal R23_AMOUNT;

		private String R24_NAME_OF_BOARD_MEMBERS;
		private String R24_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R24_NUMBER_OF_ACCOUNTS;
		private BigDecimal R24_AMOUNT;

		private String R25_NAME_OF_BOARD_MEMBERS;
		private String R25_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R25_NUMBER_OF_ACCOUNTS;
		private BigDecimal R25_AMOUNT;

		private String R26_NAME_OF_BOARD_MEMBERS;
		private String R26_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R26_NUMBER_OF_ACCOUNTS;
		private BigDecimal R26_AMOUNT;

		private String R27_NAME_OF_BOARD_MEMBERS;
		private String R27_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R27_NUMBER_OF_ACCOUNTS;
		private BigDecimal R27_AMOUNT;

		private String R28_NAME_OF_BOARD_MEMBERS;
		private String R28_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R28_NUMBER_OF_ACCOUNTS;
		private BigDecimal R28_AMOUNT;

		private String R29_NAME_OF_BOARD_MEMBERS;
		private String R29_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R29_NUMBER_OF_ACCOUNTS;
		private BigDecimal R29_AMOUNT;

		private String R30_NAME_OF_BOARD_MEMBERS;
		private String R30_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R30_NUMBER_OF_ACCOUNTS;
		private BigDecimal R30_AMOUNT;

		private String R31_NAME_OF_BOARD_MEMBERS;
		private String R31_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R31_NUMBER_OF_ACCOUNTS;
		private BigDecimal R31_AMOUNT;

		private String R32_NAME_OF_BOARD_MEMBERS;
		private String R32_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R32_NUMBER_OF_ACCOUNTS;
		private BigDecimal R32_AMOUNT;

		private String R33_NAME_OF_BOARD_MEMBERS;
		private String R33_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R33_NUMBER_OF_ACCOUNTS;
		private BigDecimal R33_AMOUNT;

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

		public String getR20_NAME_OF_BOARD_MEMBERS() {
			return R20_NAME_OF_BOARD_MEMBERS;
		}

		public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
			R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
		}

		public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
			return R20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
			R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
			return R20_NUMBER_OF_ACCOUNTS;
		}

		public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
			R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public String getR21_NAME_OF_BOARD_MEMBERS() {
			return R21_NAME_OF_BOARD_MEMBERS;
		}

		public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
			R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
		}

		public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
			return R21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
			R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
			return R21_NUMBER_OF_ACCOUNTS;
		}

		public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
			R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public String getR22_NAME_OF_BOARD_MEMBERS() {
			return R22_NAME_OF_BOARD_MEMBERS;
		}

		public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
			R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
		}

		public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
			return R22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
			R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
			return R22_NUMBER_OF_ACCOUNTS;
		}

		public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
			R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public String getR23_NAME_OF_BOARD_MEMBERS() {
			return R23_NAME_OF_BOARD_MEMBERS;
		}

		public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
			R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
		}

		public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
			return R23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
			R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
			return R23_NUMBER_OF_ACCOUNTS;
		}

		public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
			R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public String getR24_NAME_OF_BOARD_MEMBERS() {
			return R24_NAME_OF_BOARD_MEMBERS;
		}

		public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
			R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
		}

		public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
			return R24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
			R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
			return R24_NUMBER_OF_ACCOUNTS;
		}

		public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
			R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public String getR25_NAME_OF_BOARD_MEMBERS() {
			return R25_NAME_OF_BOARD_MEMBERS;
		}

		public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
			R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
		}

		public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
			return R25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
			R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
			return R25_NUMBER_OF_ACCOUNTS;
		}

		public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
			R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public String getR26_NAME_OF_BOARD_MEMBERS() {
			return R26_NAME_OF_BOARD_MEMBERS;
		}

		public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
			R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
		}

		public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
			return R26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
			R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
			return R26_NUMBER_OF_ACCOUNTS;
		}

		public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
			R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public String getR27_NAME_OF_BOARD_MEMBERS() {
			return R27_NAME_OF_BOARD_MEMBERS;
		}

		public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
			R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
		}

		public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
			return R27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
			R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
			return R27_NUMBER_OF_ACCOUNTS;
		}

		public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
			R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public String getR28_NAME_OF_BOARD_MEMBERS() {
			return R28_NAME_OF_BOARD_MEMBERS;
		}

		public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
			R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
		}

		public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
			return R28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
			R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
			return R28_NUMBER_OF_ACCOUNTS;
		}

		public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
			R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public String getR29_NAME_OF_BOARD_MEMBERS() {
			return R29_NAME_OF_BOARD_MEMBERS;
		}

		public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
			R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
		}

		public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
			return R29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
			R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
			return R29_NUMBER_OF_ACCOUNTS;
		}

		public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
			R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public String getR30_NAME_OF_BOARD_MEMBERS() {
			return R30_NAME_OF_BOARD_MEMBERS;
		}

		public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
			R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
		}

		public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
			return R30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
			R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
			return R30_NUMBER_OF_ACCOUNTS;
		}

		public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
			R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public String getR31_NAME_OF_BOARD_MEMBERS() {
			return R31_NAME_OF_BOARD_MEMBERS;
		}

		public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
			R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
		}

		public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
			return R31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
			R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
			return R31_NUMBER_OF_ACCOUNTS;
		}

		public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
			R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public String getR32_NAME_OF_BOARD_MEMBERS() {
			return R32_NAME_OF_BOARD_MEMBERS;
		}

		public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
			R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
		}

		public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
			return R32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
			R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
			return R32_NUMBER_OF_ACCOUNTS;
		}

		public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
			R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public String getR33_NAME_OF_BOARD_MEMBERS() {
			return R33_NAME_OF_BOARD_MEMBERS;
		}

		public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
			R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
		}

		public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
			return R33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
			R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
			return R33_NUMBER_OF_ACCOUNTS;
		}

		public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
			R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
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

	//// ROWMAPPER NORMAL SUMMARY3

	class MDISB5RowMapper_SUMMARY3 implements RowMapper<MDISB5_Summary_Entity3> {

		@Override
		public MDISB5_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Summary_Entity3 obj = new MDISB5_Summary_Entity3();

			obj.setR37_NAME(rs.getString("R37_NAME"));
			obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
			obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
			obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

			obj.setR38_NAME(rs.getString("R38_NAME"));
			obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
			obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
			obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

			obj.setR39_NAME(rs.getString("R39_NAME"));
			obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
			obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

			obj.setR40_NAME(rs.getString("R40_NAME"));
			obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
			obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

			obj.setR41_NAME(rs.getString("R41_NAME"));
			obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
			obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

			obj.setR42_NAME(rs.getString("R42_NAME"));
			obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
			obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

			obj.setR43_NAME(rs.getString("R43_NAME"));
			obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
			obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

			obj.setR44_NAME(rs.getString("R44_NAME"));
			obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
			obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

	public static class MDISB5_Summary_Entity3 {

		private String R37_NAME;
		private String R37_DESIGNATION_OR_POSITION;
		private BigDecimal R37_NUMBER_OF_ACCOUNTS;
		private BigDecimal R37_AMOUNT;

		private String R38_NAME;
		private String R38_DESIGNATION_OR_POSITION;
		private BigDecimal R38_NUMBER_OF_ACCOUNTS;
		private BigDecimal R38_AMOUNT;

		private String R39_NAME;
		private String R39_DESIGNATION_OR_POSITION;
		private BigDecimal R39_NUMBER_OF_ACCOUNTS;
		private BigDecimal R39_AMOUNT;

		private String R40_NAME;
		private String R40_DESIGNATION_OR_POSITION;
		private BigDecimal R40_NUMBER_OF_ACCOUNTS;
		private BigDecimal R40_AMOUNT;

		private String R41_NAME;
		private String R41_DESIGNATION_OR_POSITION;
		private BigDecimal R41_NUMBER_OF_ACCOUNTS;
		private BigDecimal R41_AMOUNT;

		private String R42_NAME;
		private String R42_DESIGNATION_OR_POSITION;
		private BigDecimal R42_NUMBER_OF_ACCOUNTS;
		private BigDecimal R42_AMOUNT;

		private String R43_NAME;
		private String R43_DESIGNATION_OR_POSITION;
		private BigDecimal R43_NUMBER_OF_ACCOUNTS;
		private BigDecimal R43_AMOUNT;

		private String R44_NAME;
		private String R44_DESIGNATION_OR_POSITION;
		private BigDecimal R44_NUMBER_OF_ACCOUNTS;
		private BigDecimal R44_AMOUNT;

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

		public String getR37_NAME() {
			return R37_NAME;
		}

		public void setR37_NAME(String r37_NAME) {
			R37_NAME = r37_NAME;
		}

		public String getR37_DESIGNATION_OR_POSITION() {
			return R37_DESIGNATION_OR_POSITION;
		}

		public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
			R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
			return R37_NUMBER_OF_ACCOUNTS;
		}

		public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
			R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR37_AMOUNT() {
			return R37_AMOUNT;
		}

		public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
			R37_AMOUNT = r37_AMOUNT;
		}

		public String getR38_NAME() {
			return R38_NAME;
		}

		public void setR38_NAME(String r38_NAME) {
			R38_NAME = r38_NAME;
		}

		public String getR38_DESIGNATION_OR_POSITION() {
			return R38_DESIGNATION_OR_POSITION;
		}

		public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
			R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
			return R38_NUMBER_OF_ACCOUNTS;
		}

		public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
			R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR38_AMOUNT() {
			return R38_AMOUNT;
		}

		public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
			R38_AMOUNT = r38_AMOUNT;
		}

		public String getR39_NAME() {
			return R39_NAME;
		}

		public void setR39_NAME(String r39_NAME) {
			R39_NAME = r39_NAME;
		}

		public String getR39_DESIGNATION_OR_POSITION() {
			return R39_DESIGNATION_OR_POSITION;
		}

		public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
			R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
			return R39_NUMBER_OF_ACCOUNTS;
		}

		public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
			R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public String getR40_NAME() {
			return R40_NAME;
		}

		public void setR40_NAME(String r40_NAME) {
			R40_NAME = r40_NAME;
		}

		public String getR40_DESIGNATION_OR_POSITION() {
			return R40_DESIGNATION_OR_POSITION;
		}

		public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
			R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
			return R40_NUMBER_OF_ACCOUNTS;
		}

		public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
			R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public String getR41_NAME() {
			return R41_NAME;
		}

		public void setR41_NAME(String r41_NAME) {
			R41_NAME = r41_NAME;
		}

		public String getR41_DESIGNATION_OR_POSITION() {
			return R41_DESIGNATION_OR_POSITION;
		}

		public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
			R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
			return R41_NUMBER_OF_ACCOUNTS;
		}

		public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
			R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public String getR42_NAME() {
			return R42_NAME;
		}

		public void setR42_NAME(String r42_NAME) {
			R42_NAME = r42_NAME;
		}

		public String getR42_DESIGNATION_OR_POSITION() {
			return R42_DESIGNATION_OR_POSITION;
		}

		public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
			R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
			return R42_NUMBER_OF_ACCOUNTS;
		}

		public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
			R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public String getR43_NAME() {
			return R43_NAME;
		}

		public void setR43_NAME(String r43_NAME) {
			R43_NAME = r43_NAME;
		}

		public String getR43_DESIGNATION_OR_POSITION() {
			return R43_DESIGNATION_OR_POSITION;
		}

		public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
			R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
			return R43_NUMBER_OF_ACCOUNTS;
		}

		public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
			R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public String getR44_NAME() {
			return R44_NAME;
		}

		public void setR44_NAME(String r44_NAME) {
			R44_NAME = r44_NAME;
		}

		public String getR44_DESIGNATION_OR_POSITION() {
			return R44_DESIGNATION_OR_POSITION;
		}

		public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
			R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
			return R44_NUMBER_OF_ACCOUNTS;
		}

		public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
			R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
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

	// ROW MAPPER ARCHIVAL1

	class MDISB5ArchivalRowMapper_SUMMARY1 implements RowMapper<MDISB5_Archival_Summary_Entity1> {

		@Override
		public MDISB5_Archival_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Summary_Entity1 obj = new MDISB5_Archival_Summary_Entity1();

			obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
			obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
			obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
			obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

			obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
			obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
			obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
			obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

			obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
			obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
			obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
			obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

			obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
			obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
			obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
			obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

			obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
			obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
			obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

			obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
			obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
			obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

			obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
			obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
			obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

			obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
			obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
			obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

			obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
			obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
			obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

			obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
			obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
			obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

			obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
			obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
			obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

	public static class MDISB5_Archival_Summary_Entity1 {

		private String R5_NAME_OF_SHAREHOLDER;
		private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R5_NUMBER_OF_ACCOUNTS;
		private BigDecimal R5_AMOUNT;

		private String R6_NAME_OF_SHAREHOLDER;
		private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R6_NUMBER_OF_ACCOUNTS;
		private BigDecimal R6_AMOUNT;

		private String R7_NAME_OF_SHAREHOLDER;
		private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R7_NUMBER_OF_ACCOUNTS;
		private BigDecimal R7_AMOUNT;

		private String R8_NAME_OF_SHAREHOLDER;
		private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R8_NUMBER_OF_ACCOUNTS;
		private BigDecimal R8_AMOUNT;

		private String R9_NAME_OF_SHAREHOLDER;
		private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R9_NUMBER_OF_ACCOUNTS;
		private BigDecimal R9_AMOUNT;

		private String R10_NAME_OF_SHAREHOLDER;
		private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R10_NUMBER_OF_ACCOUNTS;
		private BigDecimal R10_AMOUNT;

		private String R11_NAME_OF_SHAREHOLDER;
		private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R11_NUMBER_OF_ACCOUNTS;
		private BigDecimal R11_AMOUNT;

		private String R12_NAME_OF_SHAREHOLDER;
		private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R12_NUMBER_OF_ACCOUNTS;
		private BigDecimal R12_AMOUNT;

		private String R13_NAME_OF_SHAREHOLDER;
		private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R13_NUMBER_OF_ACCOUNTS;
		private BigDecimal R13_AMOUNT;

		private String R14_NAME_OF_SHAREHOLDER;
		private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R14_NUMBER_OF_ACCOUNTS;
		private BigDecimal R14_AMOUNT;

		private String R15_NAME_OF_SHAREHOLDER;
		private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R15_NUMBER_OF_ACCOUNTS;
		private BigDecimal R15_AMOUNT;

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

		public String getR5_NAME_OF_SHAREHOLDER() {
			return R5_NAME_OF_SHAREHOLDER;
		}

		public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
			R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
			return R5_PERCENTAGE_SHAREHOLDING;
		}

		public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
			R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
			return R5_NUMBER_OF_ACCOUNTS;
		}

		public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
			R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR5_AMOUNT() {
			return R5_AMOUNT;
		}

		public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
			R5_AMOUNT = r5_AMOUNT;
		}

		public String getR6_NAME_OF_SHAREHOLDER() {
			return R6_NAME_OF_SHAREHOLDER;
		}

		public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
			R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
			return R6_PERCENTAGE_SHAREHOLDING;
		}

		public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
			R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
			return R6_NUMBER_OF_ACCOUNTS;
		}

		public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
			R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR6_AMOUNT() {
			return R6_AMOUNT;
		}

		public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
			R6_AMOUNT = r6_AMOUNT;
		}

		public String getR7_NAME_OF_SHAREHOLDER() {
			return R7_NAME_OF_SHAREHOLDER;
		}

		public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
			R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
			return R7_PERCENTAGE_SHAREHOLDING;
		}

		public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
			R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
			return R7_NUMBER_OF_ACCOUNTS;
		}

		public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
			R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR7_AMOUNT() {
			return R7_AMOUNT;
		}

		public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
			R7_AMOUNT = r7_AMOUNT;
		}

		public String getR8_NAME_OF_SHAREHOLDER() {
			return R8_NAME_OF_SHAREHOLDER;
		}

		public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
			R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
			return R8_PERCENTAGE_SHAREHOLDING;
		}

		public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
			R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
			return R8_NUMBER_OF_ACCOUNTS;
		}

		public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
			R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR8_AMOUNT() {
			return R8_AMOUNT;
		}

		public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
			R8_AMOUNT = r8_AMOUNT;
		}

		public String getR9_NAME_OF_SHAREHOLDER() {
			return R9_NAME_OF_SHAREHOLDER;
		}

		public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
			R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
			return R9_PERCENTAGE_SHAREHOLDING;
		}

		public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
			R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
			return R9_NUMBER_OF_ACCOUNTS;
		}

		public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
			R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR9_AMOUNT() {
			return R9_AMOUNT;
		}

		public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
			R9_AMOUNT = r9_AMOUNT;
		}

		public String getR10_NAME_OF_SHAREHOLDER() {
			return R10_NAME_OF_SHAREHOLDER;
		}

		public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
			R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
			return R10_PERCENTAGE_SHAREHOLDING;
		}

		public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
			R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
			return R10_NUMBER_OF_ACCOUNTS;
		}

		public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
			R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR10_AMOUNT() {
			return R10_AMOUNT;
		}

		public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
			R10_AMOUNT = r10_AMOUNT;
		}

		public String getR11_NAME_OF_SHAREHOLDER() {
			return R11_NAME_OF_SHAREHOLDER;
		}

		public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
			R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
			return R11_PERCENTAGE_SHAREHOLDING;
		}

		public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
			R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
			return R11_NUMBER_OF_ACCOUNTS;
		}

		public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
			R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public String getR12_NAME_OF_SHAREHOLDER() {
			return R12_NAME_OF_SHAREHOLDER;
		}

		public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
			R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
			return R12_PERCENTAGE_SHAREHOLDING;
		}

		public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
			R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
			return R12_NUMBER_OF_ACCOUNTS;
		}

		public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
			R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public String getR13_NAME_OF_SHAREHOLDER() {
			return R13_NAME_OF_SHAREHOLDER;
		}

		public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
			R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
			return R13_PERCENTAGE_SHAREHOLDING;
		}

		public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
			R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
			return R13_NUMBER_OF_ACCOUNTS;
		}

		public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
			R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public String getR14_NAME_OF_SHAREHOLDER() {
			return R14_NAME_OF_SHAREHOLDER;
		}

		public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
			R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
			return R14_PERCENTAGE_SHAREHOLDING;
		}

		public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
			R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
			return R14_NUMBER_OF_ACCOUNTS;
		}

		public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
			R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public String getR15_NAME_OF_SHAREHOLDER() {
			return R15_NAME_OF_SHAREHOLDER;
		}

		public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
			R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
			return R15_PERCENTAGE_SHAREHOLDING;
		}

		public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
			R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
			return R15_NUMBER_OF_ACCOUNTS;
		}

		public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
			R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
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

	//// NORMAL ARCHIVAL ENTITY 2

	class MDISB5ArchivalRowMapper_SUMMARY2 implements RowMapper<MDISB5_Archival_Summary_Entity2> {

		@Override
		public MDISB5_Archival_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Summary_Entity2 obj = new MDISB5_Archival_Summary_Entity2();

			obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
			obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

			obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
			obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

			obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
			obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

			obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
			obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

			obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
			obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

			obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
			obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

			obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
			obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

			obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
			obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

			obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
			obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

			obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
			obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

			obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
			obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

			obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
			obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

			obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
			obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

			obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
			obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

	public static class MDISB5_Archival_Summary_Entity2 {

		private String R20_NAME_OF_BOARD_MEMBERS;
		private String R20_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R20_NUMBER_OF_ACCOUNTS;
		private BigDecimal R20_AMOUNT;

		private String R21_NAME_OF_BOARD_MEMBERS;
		private String R21_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R21_NUMBER_OF_ACCOUNTS;
		private BigDecimal R21_AMOUNT;

		private String R22_NAME_OF_BOARD_MEMBERS;
		private String R22_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R22_NUMBER_OF_ACCOUNTS;
		private BigDecimal R22_AMOUNT;

		private String R23_NAME_OF_BOARD_MEMBERS;
		private String R23_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R23_NUMBER_OF_ACCOUNTS;
		private BigDecimal R23_AMOUNT;

		private String R24_NAME_OF_BOARD_MEMBERS;
		private String R24_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R24_NUMBER_OF_ACCOUNTS;
		private BigDecimal R24_AMOUNT;

		private String R25_NAME_OF_BOARD_MEMBERS;
		private String R25_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R25_NUMBER_OF_ACCOUNTS;
		private BigDecimal R25_AMOUNT;

		private String R26_NAME_OF_BOARD_MEMBERS;
		private String R26_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R26_NUMBER_OF_ACCOUNTS;
		private BigDecimal R26_AMOUNT;

		private String R27_NAME_OF_BOARD_MEMBERS;
		private String R27_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R27_NUMBER_OF_ACCOUNTS;
		private BigDecimal R27_AMOUNT;

		private String R28_NAME_OF_BOARD_MEMBERS;
		private String R28_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R28_NUMBER_OF_ACCOUNTS;
		private BigDecimal R28_AMOUNT;

		private String R29_NAME_OF_BOARD_MEMBERS;
		private String R29_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R29_NUMBER_OF_ACCOUNTS;
		private BigDecimal R29_AMOUNT;

		private String R30_NAME_OF_BOARD_MEMBERS;
		private String R30_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R30_NUMBER_OF_ACCOUNTS;
		private BigDecimal R30_AMOUNT;

		private String R31_NAME_OF_BOARD_MEMBERS;
		private String R31_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R31_NUMBER_OF_ACCOUNTS;
		private BigDecimal R31_AMOUNT;

		private String R32_NAME_OF_BOARD_MEMBERS;
		private String R32_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R32_NUMBER_OF_ACCOUNTS;
		private BigDecimal R32_AMOUNT;

		private String R33_NAME_OF_BOARD_MEMBERS;
		private String R33_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R33_NUMBER_OF_ACCOUNTS;
		private BigDecimal R33_AMOUNT;

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

		public String getR20_NAME_OF_BOARD_MEMBERS() {
			return R20_NAME_OF_BOARD_MEMBERS;
		}

		public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
			R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
		}

		public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
			return R20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
			R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
			return R20_NUMBER_OF_ACCOUNTS;
		}

		public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
			R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public String getR21_NAME_OF_BOARD_MEMBERS() {
			return R21_NAME_OF_BOARD_MEMBERS;
		}

		public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
			R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
		}

		public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
			return R21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
			R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
			return R21_NUMBER_OF_ACCOUNTS;
		}

		public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
			R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public String getR22_NAME_OF_BOARD_MEMBERS() {
			return R22_NAME_OF_BOARD_MEMBERS;
		}

		public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
			R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
		}

		public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
			return R22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
			R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
			return R22_NUMBER_OF_ACCOUNTS;
		}

		public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
			R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public String getR23_NAME_OF_BOARD_MEMBERS() {
			return R23_NAME_OF_BOARD_MEMBERS;
		}

		public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
			R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
		}

		public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
			return R23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
			R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
			return R23_NUMBER_OF_ACCOUNTS;
		}

		public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
			R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public String getR24_NAME_OF_BOARD_MEMBERS() {
			return R24_NAME_OF_BOARD_MEMBERS;
		}

		public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
			R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
		}

		public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
			return R24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
			R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
			return R24_NUMBER_OF_ACCOUNTS;
		}

		public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
			R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public String getR25_NAME_OF_BOARD_MEMBERS() {
			return R25_NAME_OF_BOARD_MEMBERS;
		}

		public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
			R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
		}

		public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
			return R25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
			R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
			return R25_NUMBER_OF_ACCOUNTS;
		}

		public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
			R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public String getR26_NAME_OF_BOARD_MEMBERS() {
			return R26_NAME_OF_BOARD_MEMBERS;
		}

		public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
			R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
		}

		public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
			return R26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
			R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
			return R26_NUMBER_OF_ACCOUNTS;
		}

		public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
			R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public String getR27_NAME_OF_BOARD_MEMBERS() {
			return R27_NAME_OF_BOARD_MEMBERS;
		}

		public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
			R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
		}

		public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
			return R27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
			R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
			return R27_NUMBER_OF_ACCOUNTS;
		}

		public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
			R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public String getR28_NAME_OF_BOARD_MEMBERS() {
			return R28_NAME_OF_BOARD_MEMBERS;
		}

		public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
			R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
		}

		public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
			return R28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
			R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
			return R28_NUMBER_OF_ACCOUNTS;
		}

		public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
			R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public String getR29_NAME_OF_BOARD_MEMBERS() {
			return R29_NAME_OF_BOARD_MEMBERS;
		}

		public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
			R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
		}

		public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
			return R29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
			R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
			return R29_NUMBER_OF_ACCOUNTS;
		}

		public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
			R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public String getR30_NAME_OF_BOARD_MEMBERS() {
			return R30_NAME_OF_BOARD_MEMBERS;
		}

		public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
			R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
		}

		public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
			return R30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
			R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
			return R30_NUMBER_OF_ACCOUNTS;
		}

		public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
			R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public String getR31_NAME_OF_BOARD_MEMBERS() {
			return R31_NAME_OF_BOARD_MEMBERS;
		}

		public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
			R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
		}

		public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
			return R31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
			R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
			return R31_NUMBER_OF_ACCOUNTS;
		}

		public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
			R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public String getR32_NAME_OF_BOARD_MEMBERS() {
			return R32_NAME_OF_BOARD_MEMBERS;
		}

		public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
			R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
		}

		public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
			return R32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
			R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
			return R32_NUMBER_OF_ACCOUNTS;
		}

		public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
			R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public String getR33_NAME_OF_BOARD_MEMBERS() {
			return R33_NAME_OF_BOARD_MEMBERS;
		}

		public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
			R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
		}

		public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
			return R33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
			R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
			return R33_NUMBER_OF_ACCOUNTS;
		}

		public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
			R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
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

	//// ROWMAPPER NORMAL ARCHIVAL3

	class MDISB5ArchivalRowMapper_SUMMARY3 implements RowMapper<MDISB5_Archival_Summary_Entity3> {

		@Override
		public MDISB5_Archival_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Summary_Entity3 obj = new MDISB5_Archival_Summary_Entity3();

			obj.setR37_NAME(rs.getString("R37_NAME"));
			obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
			obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
			obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

			obj.setR38_NAME(rs.getString("R38_NAME"));
			obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
			obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
			obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

			obj.setR39_NAME(rs.getString("R39_NAME"));
			obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
			obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

			obj.setR40_NAME(rs.getString("R40_NAME"));
			obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
			obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

			obj.setR41_NAME(rs.getString("R41_NAME"));
			obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
			obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

			obj.setR42_NAME(rs.getString("R42_NAME"));
			obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
			obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

			obj.setR43_NAME(rs.getString("R43_NAME"));
			obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
			obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

			obj.setR44_NAME(rs.getString("R44_NAME"));
			obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
			obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

	public static class MDISB5_Archival_Summary_Entity3 {

		private String R37_NAME;
		private String R37_DESIGNATION_OR_POSITION;
		private BigDecimal R37_NUMBER_OF_ACCOUNTS;
		private BigDecimal R37_AMOUNT;

		private String R38_NAME;
		private String R38_DESIGNATION_OR_POSITION;
		private BigDecimal R38_NUMBER_OF_ACCOUNTS;
		private BigDecimal R38_AMOUNT;

		private String R39_NAME;
		private String R39_DESIGNATION_OR_POSITION;
		private BigDecimal R39_NUMBER_OF_ACCOUNTS;
		private BigDecimal R39_AMOUNT;

		private String R40_NAME;
		private String R40_DESIGNATION_OR_POSITION;
		private BigDecimal R40_NUMBER_OF_ACCOUNTS;
		private BigDecimal R40_AMOUNT;

		private String R41_NAME;
		private String R41_DESIGNATION_OR_POSITION;
		private BigDecimal R41_NUMBER_OF_ACCOUNTS;
		private BigDecimal R41_AMOUNT;

		private String R42_NAME;
		private String R42_DESIGNATION_OR_POSITION;
		private BigDecimal R42_NUMBER_OF_ACCOUNTS;
		private BigDecimal R42_AMOUNT;

		private String R43_NAME;
		private String R43_DESIGNATION_OR_POSITION;
		private BigDecimal R43_NUMBER_OF_ACCOUNTS;
		private BigDecimal R43_AMOUNT;

		private String R44_NAME;
		private String R44_DESIGNATION_OR_POSITION;
		private BigDecimal R44_NUMBER_OF_ACCOUNTS;
		private BigDecimal R44_AMOUNT;

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

		public String getR37_NAME() {
			return R37_NAME;
		}

		public void setR37_NAME(String r37_NAME) {
			R37_NAME = r37_NAME;
		}

		public String getR37_DESIGNATION_OR_POSITION() {
			return R37_DESIGNATION_OR_POSITION;
		}

		public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
			R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
			return R37_NUMBER_OF_ACCOUNTS;
		}

		public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
			R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR37_AMOUNT() {
			return R37_AMOUNT;
		}

		public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
			R37_AMOUNT = r37_AMOUNT;
		}

		public String getR38_NAME() {
			return R38_NAME;
		}

		public void setR38_NAME(String r38_NAME) {
			R38_NAME = r38_NAME;
		}

		public String getR38_DESIGNATION_OR_POSITION() {
			return R38_DESIGNATION_OR_POSITION;
		}

		public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
			R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
			return R38_NUMBER_OF_ACCOUNTS;
		}

		public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
			R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR38_AMOUNT() {
			return R38_AMOUNT;
		}

		public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
			R38_AMOUNT = r38_AMOUNT;
		}

		public String getR39_NAME() {
			return R39_NAME;
		}

		public void setR39_NAME(String r39_NAME) {
			R39_NAME = r39_NAME;
		}

		public String getR39_DESIGNATION_OR_POSITION() {
			return R39_DESIGNATION_OR_POSITION;
		}

		public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
			R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
			return R39_NUMBER_OF_ACCOUNTS;
		}

		public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
			R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public String getR40_NAME() {
			return R40_NAME;
		}

		public void setR40_NAME(String r40_NAME) {
			R40_NAME = r40_NAME;
		}

		public String getR40_DESIGNATION_OR_POSITION() {
			return R40_DESIGNATION_OR_POSITION;
		}

		public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
			R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
			return R40_NUMBER_OF_ACCOUNTS;
		}

		public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
			R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public String getR41_NAME() {
			return R41_NAME;
		}

		public void setR41_NAME(String r41_NAME) {
			R41_NAME = r41_NAME;
		}

		public String getR41_DESIGNATION_OR_POSITION() {
			return R41_DESIGNATION_OR_POSITION;
		}

		public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
			R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
			return R41_NUMBER_OF_ACCOUNTS;
		}

		public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
			R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public String getR42_NAME() {
			return R42_NAME;
		}

		public void setR42_NAME(String r42_NAME) {
			R42_NAME = r42_NAME;
		}

		public String getR42_DESIGNATION_OR_POSITION() {
			return R42_DESIGNATION_OR_POSITION;
		}

		public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
			R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
			return R42_NUMBER_OF_ACCOUNTS;
		}

		public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
			R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public String getR43_NAME() {
			return R43_NAME;
		}

		public void setR43_NAME(String r43_NAME) {
			R43_NAME = r43_NAME;
		}

		public String getR43_DESIGNATION_OR_POSITION() {
			return R43_DESIGNATION_OR_POSITION;
		}

		public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
			R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
			return R43_NUMBER_OF_ACCOUNTS;
		}

		public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
			R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public String getR44_NAME() {
			return R44_NAME;
		}

		public void setR44_NAME(String r44_NAME) {
			R44_NAME = r44_NAME;
		}

		public String getR44_DESIGNATION_OR_POSITION() {
			return R44_DESIGNATION_OR_POSITION;
		}

		public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
			R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
			return R44_NUMBER_OF_ACCOUNTS;
		}

		public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
			R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
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
	
	/// ROW MAPPER RESUB
	
	// ROW MAPPER RESUB1

		class MDISB5RESUBRowMapper_SUMMARY1 implements RowMapper<MDISB5_RESUB_Summary_Entity1> {

			@Override
			public MDISB5_RESUB_Summary_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Summary_Entity1 obj = new MDISB5_RESUB_Summary_Entity1();

				obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
				obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
				obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
				obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

				obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
				obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
				obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
				obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

				obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
				obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
				obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
				obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

				obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
				obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
				obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
				obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

				obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
				obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
				obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
				obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

				obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
				obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
				obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
				obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

				obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
				obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
				obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
				obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

				obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
				obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
				obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
				obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

				obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
				obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
				obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
				obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

				obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
				obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
				obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
				obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

				obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
				obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
				obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
				obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

		public static class MDISB5_RESUB_Summary_Entity1 {

			private String R5_NAME_OF_SHAREHOLDER;
			private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R5_NUMBER_OF_ACCOUNTS;
			private BigDecimal R5_AMOUNT;

			private String R6_NAME_OF_SHAREHOLDER;
			private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R6_NUMBER_OF_ACCOUNTS;
			private BigDecimal R6_AMOUNT;

			private String R7_NAME_OF_SHAREHOLDER;
			private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R7_NUMBER_OF_ACCOUNTS;
			private BigDecimal R7_AMOUNT;

			private String R8_NAME_OF_SHAREHOLDER;
			private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R8_NUMBER_OF_ACCOUNTS;
			private BigDecimal R8_AMOUNT;

			private String R9_NAME_OF_SHAREHOLDER;
			private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R9_NUMBER_OF_ACCOUNTS;
			private BigDecimal R9_AMOUNT;

			private String R10_NAME_OF_SHAREHOLDER;
			private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R10_NUMBER_OF_ACCOUNTS;
			private BigDecimal R10_AMOUNT;

			private String R11_NAME_OF_SHAREHOLDER;
			private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R11_NUMBER_OF_ACCOUNTS;
			private BigDecimal R11_AMOUNT;

			private String R12_NAME_OF_SHAREHOLDER;
			private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R12_NUMBER_OF_ACCOUNTS;
			private BigDecimal R12_AMOUNT;

			private String R13_NAME_OF_SHAREHOLDER;
			private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R13_NUMBER_OF_ACCOUNTS;
			private BigDecimal R13_AMOUNT;

			private String R14_NAME_OF_SHAREHOLDER;
			private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R14_NUMBER_OF_ACCOUNTS;
			private BigDecimal R14_AMOUNT;

			private String R15_NAME_OF_SHAREHOLDER;
			private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R15_NUMBER_OF_ACCOUNTS;
			private BigDecimal R15_AMOUNT;

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

			public String getR5_NAME_OF_SHAREHOLDER() {
				return R5_NAME_OF_SHAREHOLDER;
			}

			public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
				R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
				return R5_PERCENTAGE_SHAREHOLDING;
			}

			public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
				R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
				return R5_NUMBER_OF_ACCOUNTS;
			}

			public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
				R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR5_AMOUNT() {
				return R5_AMOUNT;
			}

			public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
				R5_AMOUNT = r5_AMOUNT;
			}

			public String getR6_NAME_OF_SHAREHOLDER() {
				return R6_NAME_OF_SHAREHOLDER;
			}

			public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
				R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
				return R6_PERCENTAGE_SHAREHOLDING;
			}

			public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
				R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
				return R6_NUMBER_OF_ACCOUNTS;
			}

			public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
				R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR6_AMOUNT() {
				return R6_AMOUNT;
			}

			public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
				R6_AMOUNT = r6_AMOUNT;
			}

			public String getR7_NAME_OF_SHAREHOLDER() {
				return R7_NAME_OF_SHAREHOLDER;
			}

			public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
				R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
				return R7_PERCENTAGE_SHAREHOLDING;
			}

			public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
				R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
				return R7_NUMBER_OF_ACCOUNTS;
			}

			public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
				R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR7_AMOUNT() {
				return R7_AMOUNT;
			}

			public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
				R7_AMOUNT = r7_AMOUNT;
			}

			public String getR8_NAME_OF_SHAREHOLDER() {
				return R8_NAME_OF_SHAREHOLDER;
			}

			public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
				R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
				return R8_PERCENTAGE_SHAREHOLDING;
			}

			public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
				R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
				return R8_NUMBER_OF_ACCOUNTS;
			}

			public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
				R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR8_AMOUNT() {
				return R8_AMOUNT;
			}

			public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
				R8_AMOUNT = r8_AMOUNT;
			}

			public String getR9_NAME_OF_SHAREHOLDER() {
				return R9_NAME_OF_SHAREHOLDER;
			}

			public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
				R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
				return R9_PERCENTAGE_SHAREHOLDING;
			}

			public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
				R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
				return R9_NUMBER_OF_ACCOUNTS;
			}

			public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
				R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR9_AMOUNT() {
				return R9_AMOUNT;
			}

			public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
				R9_AMOUNT = r9_AMOUNT;
			}

			public String getR10_NAME_OF_SHAREHOLDER() {
				return R10_NAME_OF_SHAREHOLDER;
			}

			public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
				R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
				return R10_PERCENTAGE_SHAREHOLDING;
			}

			public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
				R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
				return R10_NUMBER_OF_ACCOUNTS;
			}

			public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
				R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR10_AMOUNT() {
				return R10_AMOUNT;
			}

			public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
				R10_AMOUNT = r10_AMOUNT;
			}

			public String getR11_NAME_OF_SHAREHOLDER() {
				return R11_NAME_OF_SHAREHOLDER;
			}

			public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
				R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
				return R11_PERCENTAGE_SHAREHOLDING;
			}

			public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
				R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
				return R11_NUMBER_OF_ACCOUNTS;
			}

			public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
				R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR11_AMOUNT() {
				return R11_AMOUNT;
			}

			public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
				R11_AMOUNT = r11_AMOUNT;
			}

			public String getR12_NAME_OF_SHAREHOLDER() {
				return R12_NAME_OF_SHAREHOLDER;
			}

			public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
				R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
				return R12_PERCENTAGE_SHAREHOLDING;
			}

			public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
				R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
				return R12_NUMBER_OF_ACCOUNTS;
			}

			public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
				R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR12_AMOUNT() {
				return R12_AMOUNT;
			}

			public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
				R12_AMOUNT = r12_AMOUNT;
			}

			public String getR13_NAME_OF_SHAREHOLDER() {
				return R13_NAME_OF_SHAREHOLDER;
			}

			public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
				R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
				return R13_PERCENTAGE_SHAREHOLDING;
			}

			public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
				R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
				return R13_NUMBER_OF_ACCOUNTS;
			}

			public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
				R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR13_AMOUNT() {
				return R13_AMOUNT;
			}

			public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
				R13_AMOUNT = r13_AMOUNT;
			}

			public String getR14_NAME_OF_SHAREHOLDER() {
				return R14_NAME_OF_SHAREHOLDER;
			}

			public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
				R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
				return R14_PERCENTAGE_SHAREHOLDING;
			}

			public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
				R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
				return R14_NUMBER_OF_ACCOUNTS;
			}

			public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
				R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR14_AMOUNT() {
				return R14_AMOUNT;
			}

			public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
				R14_AMOUNT = r14_AMOUNT;
			}

			public String getR15_NAME_OF_SHAREHOLDER() {
				return R15_NAME_OF_SHAREHOLDER;
			}

			public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
				R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
				return R15_PERCENTAGE_SHAREHOLDING;
			}

			public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
				R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
				return R15_NUMBER_OF_ACCOUNTS;
			}

			public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
				R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR15_AMOUNT() {
				return R15_AMOUNT;
			}

			public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
				R15_AMOUNT = r15_AMOUNT;
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

		//// NORMAL RESUB ENTITY 2

		class MDISB5RESUBRowMapper_SUMMARY2 implements RowMapper<MDISB5_RESUB_Summary_Entity2> {

			@Override
			public MDISB5_RESUB_Summary_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Summary_Entity2 obj = new MDISB5_RESUB_Summary_Entity2();

				obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
				obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
				obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

				obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
				obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
				obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

				obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
				obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
				obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

				obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
				obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
				obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

				obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
				obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
				obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

				obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
				obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
				obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

				obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
				obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
				obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

				obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
				obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
				obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

				obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
				obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
				obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

				obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
				obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
				obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

				obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
				obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
				obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

				obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
				obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
				obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

				obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
				obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
				obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

				obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
				obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
				obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

		public static class MDISB5_RESUB_Summary_Entity2 {

			private String R20_NAME_OF_BOARD_MEMBERS;
			private String R20_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R20_NUMBER_OF_ACCOUNTS;
			private BigDecimal R20_AMOUNT;

			private String R21_NAME_OF_BOARD_MEMBERS;
			private String R21_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R21_NUMBER_OF_ACCOUNTS;
			private BigDecimal R21_AMOUNT;

			private String R22_NAME_OF_BOARD_MEMBERS;
			private String R22_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R22_NUMBER_OF_ACCOUNTS;
			private BigDecimal R22_AMOUNT;

			private String R23_NAME_OF_BOARD_MEMBERS;
			private String R23_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R23_NUMBER_OF_ACCOUNTS;
			private BigDecimal R23_AMOUNT;

			private String R24_NAME_OF_BOARD_MEMBERS;
			private String R24_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R24_NUMBER_OF_ACCOUNTS;
			private BigDecimal R24_AMOUNT;

			private String R25_NAME_OF_BOARD_MEMBERS;
			private String R25_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R25_NUMBER_OF_ACCOUNTS;
			private BigDecimal R25_AMOUNT;

			private String R26_NAME_OF_BOARD_MEMBERS;
			private String R26_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R26_NUMBER_OF_ACCOUNTS;
			private BigDecimal R26_AMOUNT;

			private String R27_NAME_OF_BOARD_MEMBERS;
			private String R27_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R27_NUMBER_OF_ACCOUNTS;
			private BigDecimal R27_AMOUNT;

			private String R28_NAME_OF_BOARD_MEMBERS;
			private String R28_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R28_NUMBER_OF_ACCOUNTS;
			private BigDecimal R28_AMOUNT;

			private String R29_NAME_OF_BOARD_MEMBERS;
			private String R29_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R29_NUMBER_OF_ACCOUNTS;
			private BigDecimal R29_AMOUNT;

			private String R30_NAME_OF_BOARD_MEMBERS;
			private String R30_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R30_NUMBER_OF_ACCOUNTS;
			private BigDecimal R30_AMOUNT;

			private String R31_NAME_OF_BOARD_MEMBERS;
			private String R31_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R31_NUMBER_OF_ACCOUNTS;
			private BigDecimal R31_AMOUNT;

			private String R32_NAME_OF_BOARD_MEMBERS;
			private String R32_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R32_NUMBER_OF_ACCOUNTS;
			private BigDecimal R32_AMOUNT;

			private String R33_NAME_OF_BOARD_MEMBERS;
			private String R33_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R33_NUMBER_OF_ACCOUNTS;
			private BigDecimal R33_AMOUNT;

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

			public String getR20_NAME_OF_BOARD_MEMBERS() {
				return R20_NAME_OF_BOARD_MEMBERS;
			}

			public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
				R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
			}

			public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
				return R20_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
				R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
				return R20_NUMBER_OF_ACCOUNTS;
			}

			public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
				R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR20_AMOUNT() {
				return R20_AMOUNT;
			}

			public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
				R20_AMOUNT = r20_AMOUNT;
			}

			public String getR21_NAME_OF_BOARD_MEMBERS() {
				return R21_NAME_OF_BOARD_MEMBERS;
			}

			public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
				R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
			}

			public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
				return R21_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
				R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
				return R21_NUMBER_OF_ACCOUNTS;
			}

			public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
				R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR21_AMOUNT() {
				return R21_AMOUNT;
			}

			public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
				R21_AMOUNT = r21_AMOUNT;
			}

			public String getR22_NAME_OF_BOARD_MEMBERS() {
				return R22_NAME_OF_BOARD_MEMBERS;
			}

			public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
				R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
			}

			public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
				return R22_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
				R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
				return R22_NUMBER_OF_ACCOUNTS;
			}

			public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
				R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR22_AMOUNT() {
				return R22_AMOUNT;
			}

			public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
				R22_AMOUNT = r22_AMOUNT;
			}

			public String getR23_NAME_OF_BOARD_MEMBERS() {
				return R23_NAME_OF_BOARD_MEMBERS;
			}

			public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
				R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
			}

			public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
				return R23_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
				R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
				return R23_NUMBER_OF_ACCOUNTS;
			}

			public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
				R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR23_AMOUNT() {
				return R23_AMOUNT;
			}

			public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
				R23_AMOUNT = r23_AMOUNT;
			}

			public String getR24_NAME_OF_BOARD_MEMBERS() {
				return R24_NAME_OF_BOARD_MEMBERS;
			}

			public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
				R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
			}

			public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
				return R24_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
				R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
				return R24_NUMBER_OF_ACCOUNTS;
			}

			public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
				R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR24_AMOUNT() {
				return R24_AMOUNT;
			}

			public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
				R24_AMOUNT = r24_AMOUNT;
			}

			public String getR25_NAME_OF_BOARD_MEMBERS() {
				return R25_NAME_OF_BOARD_MEMBERS;
			}

			public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
				R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
			}

			public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
				return R25_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
				R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
				return R25_NUMBER_OF_ACCOUNTS;
			}

			public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
				R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR25_AMOUNT() {
				return R25_AMOUNT;
			}

			public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
				R25_AMOUNT = r25_AMOUNT;
			}

			public String getR26_NAME_OF_BOARD_MEMBERS() {
				return R26_NAME_OF_BOARD_MEMBERS;
			}

			public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
				R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
			}

			public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
				return R26_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
				R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
				return R26_NUMBER_OF_ACCOUNTS;
			}

			public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
				R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR26_AMOUNT() {
				return R26_AMOUNT;
			}

			public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
				R26_AMOUNT = r26_AMOUNT;
			}

			public String getR27_NAME_OF_BOARD_MEMBERS() {
				return R27_NAME_OF_BOARD_MEMBERS;
			}

			public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
				R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
			}

			public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
				return R27_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
				R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
				return R27_NUMBER_OF_ACCOUNTS;
			}

			public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
				R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR27_AMOUNT() {
				return R27_AMOUNT;
			}

			public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
				R27_AMOUNT = r27_AMOUNT;
			}

			public String getR28_NAME_OF_BOARD_MEMBERS() {
				return R28_NAME_OF_BOARD_MEMBERS;
			}

			public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
				R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
			}

			public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
				return R28_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
				R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
				return R28_NUMBER_OF_ACCOUNTS;
			}

			public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
				R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR28_AMOUNT() {
				return R28_AMOUNT;
			}

			public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
				R28_AMOUNT = r28_AMOUNT;
			}

			public String getR29_NAME_OF_BOARD_MEMBERS() {
				return R29_NAME_OF_BOARD_MEMBERS;
			}

			public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
				R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
			}

			public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
				return R29_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
				R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
				return R29_NUMBER_OF_ACCOUNTS;
			}

			public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
				R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR29_AMOUNT() {
				return R29_AMOUNT;
			}

			public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
				R29_AMOUNT = r29_AMOUNT;
			}

			public String getR30_NAME_OF_BOARD_MEMBERS() {
				return R30_NAME_OF_BOARD_MEMBERS;
			}

			public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
				R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
			}

			public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
				return R30_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
				R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
				return R30_NUMBER_OF_ACCOUNTS;
			}

			public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
				R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR30_AMOUNT() {
				return R30_AMOUNT;
			}

			public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
				R30_AMOUNT = r30_AMOUNT;
			}

			public String getR31_NAME_OF_BOARD_MEMBERS() {
				return R31_NAME_OF_BOARD_MEMBERS;
			}

			public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
				R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
			}

			public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
				return R31_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
				R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
				return R31_NUMBER_OF_ACCOUNTS;
			}

			public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
				R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR31_AMOUNT() {
				return R31_AMOUNT;
			}

			public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
				R31_AMOUNT = r31_AMOUNT;
			}

			public String getR32_NAME_OF_BOARD_MEMBERS() {
				return R32_NAME_OF_BOARD_MEMBERS;
			}

			public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
				R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
			}

			public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
				return R32_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
				R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
				return R32_NUMBER_OF_ACCOUNTS;
			}

			public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
				R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR32_AMOUNT() {
				return R32_AMOUNT;
			}

			public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
				R32_AMOUNT = r32_AMOUNT;
			}

			public String getR33_NAME_OF_BOARD_MEMBERS() {
				return R33_NAME_OF_BOARD_MEMBERS;
			}

			public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
				R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
			}

			public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
				return R33_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
				R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
				return R33_NUMBER_OF_ACCOUNTS;
			}

			public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
				R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR33_AMOUNT() {
				return R33_AMOUNT;
			}

			public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
				R33_AMOUNT = r33_AMOUNT;
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

		//// ROWMAPPER NORMAL RESUB3

		class MDISB5RESUBRowMapper_SUMMARY3 implements RowMapper<MDISB5_RESUB_Summary_Entity3> {

			@Override
			public MDISB5_RESUB_Summary_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Summary_Entity3 obj = new MDISB5_RESUB_Summary_Entity3();

				obj.setR37_NAME(rs.getString("R37_NAME"));
				obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
				obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
				obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

				obj.setR38_NAME(rs.getString("R38_NAME"));
				obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
				obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
				obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

				obj.setR39_NAME(rs.getString("R39_NAME"));
				obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
				obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
				obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

				obj.setR40_NAME(rs.getString("R40_NAME"));
				obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
				obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
				obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

				obj.setR41_NAME(rs.getString("R41_NAME"));
				obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
				obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
				obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

				obj.setR42_NAME(rs.getString("R42_NAME"));
				obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
				obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
				obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

				obj.setR43_NAME(rs.getString("R43_NAME"));
				obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
				obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
				obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

				obj.setR44_NAME(rs.getString("R44_NAME"));
				obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
				obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
				obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

		public static class MDISB5_RESUB_Summary_Entity3 {

			private String R37_NAME;
			private String R37_DESIGNATION_OR_POSITION;
			private BigDecimal R37_NUMBER_OF_ACCOUNTS;
			private BigDecimal R37_AMOUNT;

			private String R38_NAME;
			private String R38_DESIGNATION_OR_POSITION;
			private BigDecimal R38_NUMBER_OF_ACCOUNTS;
			private BigDecimal R38_AMOUNT;

			private String R39_NAME;
			private String R39_DESIGNATION_OR_POSITION;
			private BigDecimal R39_NUMBER_OF_ACCOUNTS;
			private BigDecimal R39_AMOUNT;

			private String R40_NAME;
			private String R40_DESIGNATION_OR_POSITION;
			private BigDecimal R40_NUMBER_OF_ACCOUNTS;
			private BigDecimal R40_AMOUNT;

			private String R41_NAME;
			private String R41_DESIGNATION_OR_POSITION;
			private BigDecimal R41_NUMBER_OF_ACCOUNTS;
			private BigDecimal R41_AMOUNT;

			private String R42_NAME;
			private String R42_DESIGNATION_OR_POSITION;
			private BigDecimal R42_NUMBER_OF_ACCOUNTS;
			private BigDecimal R42_AMOUNT;

			private String R43_NAME;
			private String R43_DESIGNATION_OR_POSITION;
			private BigDecimal R43_NUMBER_OF_ACCOUNTS;
			private BigDecimal R43_AMOUNT;

			private String R44_NAME;
			private String R44_DESIGNATION_OR_POSITION;
			private BigDecimal R44_NUMBER_OF_ACCOUNTS;
			private BigDecimal R44_AMOUNT;

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

			public String getR37_NAME() {
				return R37_NAME;
			}

			public void setR37_NAME(String r37_NAME) {
				R37_NAME = r37_NAME;
			}

			public String getR37_DESIGNATION_OR_POSITION() {
				return R37_DESIGNATION_OR_POSITION;
			}

			public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
				R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
				return R37_NUMBER_OF_ACCOUNTS;
			}

			public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
				R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR37_AMOUNT() {
				return R37_AMOUNT;
			}

			public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
				R37_AMOUNT = r37_AMOUNT;
			}

			public String getR38_NAME() {
				return R38_NAME;
			}

			public void setR38_NAME(String r38_NAME) {
				R38_NAME = r38_NAME;
			}

			public String getR38_DESIGNATION_OR_POSITION() {
				return R38_DESIGNATION_OR_POSITION;
			}

			public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
				R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
				return R38_NUMBER_OF_ACCOUNTS;
			}

			public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
				R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR38_AMOUNT() {
				return R38_AMOUNT;
			}

			public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
				R38_AMOUNT = r38_AMOUNT;
			}

			public String getR39_NAME() {
				return R39_NAME;
			}

			public void setR39_NAME(String r39_NAME) {
				R39_NAME = r39_NAME;
			}

			public String getR39_DESIGNATION_OR_POSITION() {
				return R39_DESIGNATION_OR_POSITION;
			}

			public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
				R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
				return R39_NUMBER_OF_ACCOUNTS;
			}

			public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
				R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR39_AMOUNT() {
				return R39_AMOUNT;
			}

			public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
				R39_AMOUNT = r39_AMOUNT;
			}

			public String getR40_NAME() {
				return R40_NAME;
			}

			public void setR40_NAME(String r40_NAME) {
				R40_NAME = r40_NAME;
			}

			public String getR40_DESIGNATION_OR_POSITION() {
				return R40_DESIGNATION_OR_POSITION;
			}

			public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
				R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
				return R40_NUMBER_OF_ACCOUNTS;
			}

			public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
				R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR40_AMOUNT() {
				return R40_AMOUNT;
			}

			public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
				R40_AMOUNT = r40_AMOUNT;
			}

			public String getR41_NAME() {
				return R41_NAME;
			}

			public void setR41_NAME(String r41_NAME) {
				R41_NAME = r41_NAME;
			}

			public String getR41_DESIGNATION_OR_POSITION() {
				return R41_DESIGNATION_OR_POSITION;
			}

			public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
				R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
				return R41_NUMBER_OF_ACCOUNTS;
			}

			public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
				R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR41_AMOUNT() {
				return R41_AMOUNT;
			}

			public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
				R41_AMOUNT = r41_AMOUNT;
			}

			public String getR42_NAME() {
				return R42_NAME;
			}

			public void setR42_NAME(String r42_NAME) {
				R42_NAME = r42_NAME;
			}

			public String getR42_DESIGNATION_OR_POSITION() {
				return R42_DESIGNATION_OR_POSITION;
			}

			public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
				R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
				return R42_NUMBER_OF_ACCOUNTS;
			}

			public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
				R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR42_AMOUNT() {
				return R42_AMOUNT;
			}

			public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
				R42_AMOUNT = r42_AMOUNT;
			}

			public String getR43_NAME() {
				return R43_NAME;
			}

			public void setR43_NAME(String r43_NAME) {
				R43_NAME = r43_NAME;
			}

			public String getR43_DESIGNATION_OR_POSITION() {
				return R43_DESIGNATION_OR_POSITION;
			}

			public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
				R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
				return R43_NUMBER_OF_ACCOUNTS;
			}

			public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
				R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR43_AMOUNT() {
				return R43_AMOUNT;
			}

			public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
				R43_AMOUNT = r43_AMOUNT;
			}

			public String getR44_NAME() {
				return R44_NAME;
			}

			public void setR44_NAME(String r44_NAME) {
				R44_NAME = r44_NAME;
			}

			public String getR44_DESIGNATION_OR_POSITION() {
				return R44_DESIGNATION_OR_POSITION;
			}

			public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
				R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
				return R44_NUMBER_OF_ACCOUNTS;
			}

			public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
				R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR44_AMOUNT() {
				return R44_AMOUNT;
			}

			public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
				R44_AMOUNT = r44_AMOUNT;
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
		
		
	// ROW MAPPER DETAIL1

	class MDISB5DetaillRowMapper_DETAIL1 implements RowMapper<MDISB5_Detail_Entity1> {

		@Override
		public MDISB5_Detail_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Detail_Entity1 obj = new MDISB5_Detail_Entity1();

			obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
			obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
			obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
			obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

			obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
			obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
			obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
			obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

			obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
			obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
			obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
			obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

			obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
			obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
			obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
			obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

			obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
			obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
			obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

			obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
			obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
			obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

			obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
			obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
			obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

			obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
			obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
			obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

			obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
			obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
			obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

			obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
			obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
			obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

			obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
			obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
			obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

	public static class MDISB5_Detail_Entity1 {

		private String R5_NAME_OF_SHAREHOLDER;
		private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R5_NUMBER_OF_ACCOUNTS;
		private BigDecimal R5_AMOUNT;

		private String R6_NAME_OF_SHAREHOLDER;
		private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R6_NUMBER_OF_ACCOUNTS;
		private BigDecimal R6_AMOUNT;

		private String R7_NAME_OF_SHAREHOLDER;
		private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R7_NUMBER_OF_ACCOUNTS;
		private BigDecimal R7_AMOUNT;

		private String R8_NAME_OF_SHAREHOLDER;
		private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R8_NUMBER_OF_ACCOUNTS;
		private BigDecimal R8_AMOUNT;

		private String R9_NAME_OF_SHAREHOLDER;
		private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R9_NUMBER_OF_ACCOUNTS;
		private BigDecimal R9_AMOUNT;

		private String R10_NAME_OF_SHAREHOLDER;
		private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R10_NUMBER_OF_ACCOUNTS;
		private BigDecimal R10_AMOUNT;

		private String R11_NAME_OF_SHAREHOLDER;
		private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R11_NUMBER_OF_ACCOUNTS;
		private BigDecimal R11_AMOUNT;

		private String R12_NAME_OF_SHAREHOLDER;
		private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R12_NUMBER_OF_ACCOUNTS;
		private BigDecimal R12_AMOUNT;

		private String R13_NAME_OF_SHAREHOLDER;
		private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R13_NUMBER_OF_ACCOUNTS;
		private BigDecimal R13_AMOUNT;

		private String R14_NAME_OF_SHAREHOLDER;
		private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R14_NUMBER_OF_ACCOUNTS;
		private BigDecimal R14_AMOUNT;

		private String R15_NAME_OF_SHAREHOLDER;
		private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R15_NUMBER_OF_ACCOUNTS;
		private BigDecimal R15_AMOUNT;

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

		public String getR5_NAME_OF_SHAREHOLDER() {
			return R5_NAME_OF_SHAREHOLDER;
		}

		public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
			R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
			return R5_PERCENTAGE_SHAREHOLDING;
		}

		public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
			R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
			return R5_NUMBER_OF_ACCOUNTS;
		}

		public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
			R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR5_AMOUNT() {
			return R5_AMOUNT;
		}

		public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
			R5_AMOUNT = r5_AMOUNT;
		}

		public String getR6_NAME_OF_SHAREHOLDER() {
			return R6_NAME_OF_SHAREHOLDER;
		}

		public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
			R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
			return R6_PERCENTAGE_SHAREHOLDING;
		}

		public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
			R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
			return R6_NUMBER_OF_ACCOUNTS;
		}

		public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
			R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR6_AMOUNT() {
			return R6_AMOUNT;
		}

		public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
			R6_AMOUNT = r6_AMOUNT;
		}

		public String getR7_NAME_OF_SHAREHOLDER() {
			return R7_NAME_OF_SHAREHOLDER;
		}

		public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
			R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
			return R7_PERCENTAGE_SHAREHOLDING;
		}

		public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
			R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
			return R7_NUMBER_OF_ACCOUNTS;
		}

		public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
			R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR7_AMOUNT() {
			return R7_AMOUNT;
		}

		public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
			R7_AMOUNT = r7_AMOUNT;
		}

		public String getR8_NAME_OF_SHAREHOLDER() {
			return R8_NAME_OF_SHAREHOLDER;
		}

		public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
			R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
			return R8_PERCENTAGE_SHAREHOLDING;
		}

		public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
			R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
			return R8_NUMBER_OF_ACCOUNTS;
		}

		public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
			R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR8_AMOUNT() {
			return R8_AMOUNT;
		}

		public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
			R8_AMOUNT = r8_AMOUNT;
		}

		public String getR9_NAME_OF_SHAREHOLDER() {
			return R9_NAME_OF_SHAREHOLDER;
		}

		public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
			R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
			return R9_PERCENTAGE_SHAREHOLDING;
		}

		public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
			R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
			return R9_NUMBER_OF_ACCOUNTS;
		}

		public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
			R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR9_AMOUNT() {
			return R9_AMOUNT;
		}

		public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
			R9_AMOUNT = r9_AMOUNT;
		}

		public String getR10_NAME_OF_SHAREHOLDER() {
			return R10_NAME_OF_SHAREHOLDER;
		}

		public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
			R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
			return R10_PERCENTAGE_SHAREHOLDING;
		}

		public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
			R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
			return R10_NUMBER_OF_ACCOUNTS;
		}

		public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
			R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR10_AMOUNT() {
			return R10_AMOUNT;
		}

		public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
			R10_AMOUNT = r10_AMOUNT;
		}

		public String getR11_NAME_OF_SHAREHOLDER() {
			return R11_NAME_OF_SHAREHOLDER;
		}

		public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
			R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
			return R11_PERCENTAGE_SHAREHOLDING;
		}

		public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
			R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
			return R11_NUMBER_OF_ACCOUNTS;
		}

		public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
			R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public String getR12_NAME_OF_SHAREHOLDER() {
			return R12_NAME_OF_SHAREHOLDER;
		}

		public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
			R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
			return R12_PERCENTAGE_SHAREHOLDING;
		}

		public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
			R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
			return R12_NUMBER_OF_ACCOUNTS;
		}

		public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
			R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public String getR13_NAME_OF_SHAREHOLDER() {
			return R13_NAME_OF_SHAREHOLDER;
		}

		public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
			R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
			return R13_PERCENTAGE_SHAREHOLDING;
		}

		public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
			R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
			return R13_NUMBER_OF_ACCOUNTS;
		}

		public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
			R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public String getR14_NAME_OF_SHAREHOLDER() {
			return R14_NAME_OF_SHAREHOLDER;
		}

		public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
			R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
			return R14_PERCENTAGE_SHAREHOLDING;
		}

		public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
			R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
			return R14_NUMBER_OF_ACCOUNTS;
		}

		public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
			R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public String getR15_NAME_OF_SHAREHOLDER() {
			return R15_NAME_OF_SHAREHOLDER;
		}

		public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
			R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
			return R15_PERCENTAGE_SHAREHOLDING;
		}

		public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
			R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
			return R15_NUMBER_OF_ACCOUNTS;
		}

		public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
			R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
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

	//// NORMAL DETAIL ENTITY 2

	class MDISB5DetaillRowMapper_DETAIL2 implements RowMapper<MDISB5_Detail_Entity2> {

		@Override
		public MDISB5_Detail_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Detail_Entity2 obj = new MDISB5_Detail_Entity2();

			obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
			obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

			obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
			obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

			obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
			obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

			obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
			obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

			obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
			obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

			obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
			obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

			obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
			obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

			obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
			obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

			obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
			obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

			obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
			obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

			obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
			obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

			obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
			obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

			obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
			obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

			obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
			obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

	public static class MDISB5_Detail_Entity2 {

		private String R20_NAME_OF_BOARD_MEMBERS;
		private String R20_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R20_NUMBER_OF_ACCOUNTS;
		private BigDecimal R20_AMOUNT;

		private String R21_NAME_OF_BOARD_MEMBERS;
		private String R21_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R21_NUMBER_OF_ACCOUNTS;
		private BigDecimal R21_AMOUNT;

		private String R22_NAME_OF_BOARD_MEMBERS;
		private String R22_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R22_NUMBER_OF_ACCOUNTS;
		private BigDecimal R22_AMOUNT;

		private String R23_NAME_OF_BOARD_MEMBERS;
		private String R23_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R23_NUMBER_OF_ACCOUNTS;
		private BigDecimal R23_AMOUNT;

		private String R24_NAME_OF_BOARD_MEMBERS;
		private String R24_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R24_NUMBER_OF_ACCOUNTS;
		private BigDecimal R24_AMOUNT;

		private String R25_NAME_OF_BOARD_MEMBERS;
		private String R25_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R25_NUMBER_OF_ACCOUNTS;
		private BigDecimal R25_AMOUNT;

		private String R26_NAME_OF_BOARD_MEMBERS;
		private String R26_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R26_NUMBER_OF_ACCOUNTS;
		private BigDecimal R26_AMOUNT;

		private String R27_NAME_OF_BOARD_MEMBERS;
		private String R27_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R27_NUMBER_OF_ACCOUNTS;
		private BigDecimal R27_AMOUNT;

		private String R28_NAME_OF_BOARD_MEMBERS;
		private String R28_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R28_NUMBER_OF_ACCOUNTS;
		private BigDecimal R28_AMOUNT;

		private String R29_NAME_OF_BOARD_MEMBERS;
		private String R29_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R29_NUMBER_OF_ACCOUNTS;
		private BigDecimal R29_AMOUNT;

		private String R30_NAME_OF_BOARD_MEMBERS;
		private String R30_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R30_NUMBER_OF_ACCOUNTS;
		private BigDecimal R30_AMOUNT;

		private String R31_NAME_OF_BOARD_MEMBERS;
		private String R31_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R31_NUMBER_OF_ACCOUNTS;
		private BigDecimal R31_AMOUNT;

		private String R32_NAME_OF_BOARD_MEMBERS;
		private String R32_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R32_NUMBER_OF_ACCOUNTS;
		private BigDecimal R32_AMOUNT;

		private String R33_NAME_OF_BOARD_MEMBERS;
		private String R33_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R33_NUMBER_OF_ACCOUNTS;
		private BigDecimal R33_AMOUNT;

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

		public String getR20_NAME_OF_BOARD_MEMBERS() {
			return R20_NAME_OF_BOARD_MEMBERS;
		}

		public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
			R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
		}

		public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
			return R20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
			R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
			return R20_NUMBER_OF_ACCOUNTS;
		}

		public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
			R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public String getR21_NAME_OF_BOARD_MEMBERS() {
			return R21_NAME_OF_BOARD_MEMBERS;
		}

		public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
			R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
		}

		public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
			return R21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
			R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
			return R21_NUMBER_OF_ACCOUNTS;
		}

		public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
			R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public String getR22_NAME_OF_BOARD_MEMBERS() {
			return R22_NAME_OF_BOARD_MEMBERS;
		}

		public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
			R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
		}

		public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
			return R22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
			R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
			return R22_NUMBER_OF_ACCOUNTS;
		}

		public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
			R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public String getR23_NAME_OF_BOARD_MEMBERS() {
			return R23_NAME_OF_BOARD_MEMBERS;
		}

		public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
			R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
		}

		public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
			return R23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
			R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
			return R23_NUMBER_OF_ACCOUNTS;
		}

		public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
			R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public String getR24_NAME_OF_BOARD_MEMBERS() {
			return R24_NAME_OF_BOARD_MEMBERS;
		}

		public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
			R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
		}

		public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
			return R24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
			R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
			return R24_NUMBER_OF_ACCOUNTS;
		}

		public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
			R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public String getR25_NAME_OF_BOARD_MEMBERS() {
			return R25_NAME_OF_BOARD_MEMBERS;
		}

		public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
			R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
		}

		public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
			return R25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
			R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
			return R25_NUMBER_OF_ACCOUNTS;
		}

		public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
			R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public String getR26_NAME_OF_BOARD_MEMBERS() {
			return R26_NAME_OF_BOARD_MEMBERS;
		}

		public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
			R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
		}

		public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
			return R26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
			R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
			return R26_NUMBER_OF_ACCOUNTS;
		}

		public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
			R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public String getR27_NAME_OF_BOARD_MEMBERS() {
			return R27_NAME_OF_BOARD_MEMBERS;
		}

		public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
			R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
		}

		public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
			return R27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
			R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
			return R27_NUMBER_OF_ACCOUNTS;
		}

		public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
			R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public String getR28_NAME_OF_BOARD_MEMBERS() {
			return R28_NAME_OF_BOARD_MEMBERS;
		}

		public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
			R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
		}

		public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
			return R28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
			R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
			return R28_NUMBER_OF_ACCOUNTS;
		}

		public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
			R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public String getR29_NAME_OF_BOARD_MEMBERS() {
			return R29_NAME_OF_BOARD_MEMBERS;
		}

		public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
			R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
		}

		public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
			return R29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
			R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
			return R29_NUMBER_OF_ACCOUNTS;
		}

		public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
			R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public String getR30_NAME_OF_BOARD_MEMBERS() {
			return R30_NAME_OF_BOARD_MEMBERS;
		}

		public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
			R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
		}

		public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
			return R30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
			R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
			return R30_NUMBER_OF_ACCOUNTS;
		}

		public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
			R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public String getR31_NAME_OF_BOARD_MEMBERS() {
			return R31_NAME_OF_BOARD_MEMBERS;
		}

		public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
			R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
		}

		public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
			return R31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
			R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
			return R31_NUMBER_OF_ACCOUNTS;
		}

		public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
			R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public String getR32_NAME_OF_BOARD_MEMBERS() {
			return R32_NAME_OF_BOARD_MEMBERS;
		}

		public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
			R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
		}

		public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
			return R32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
			R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
			return R32_NUMBER_OF_ACCOUNTS;
		}

		public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
			R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public String getR33_NAME_OF_BOARD_MEMBERS() {
			return R33_NAME_OF_BOARD_MEMBERS;
		}

		public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
			R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
		}

		public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
			return R33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
			R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
			return R33_NUMBER_OF_ACCOUNTS;
		}

		public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
			R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
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

	//// ROWMAPPER NORMAL DETAIL3

	class MDISB5DetaillRowMapper_DETAIL3 implements RowMapper<MDISB5_Detail_Entity3> {

		@Override
		public MDISB5_Detail_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Detail_Entity3 obj = new MDISB5_Detail_Entity3();

			obj.setR37_NAME(rs.getString("R37_NAME"));
			obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
			obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
			obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

			obj.setR38_NAME(rs.getString("R38_NAME"));
			obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
			obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
			obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

			obj.setR39_NAME(rs.getString("R39_NAME"));
			obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
			obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

			obj.setR40_NAME(rs.getString("R40_NAME"));
			obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
			obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

			obj.setR41_NAME(rs.getString("R41_NAME"));
			obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
			obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

			obj.setR42_NAME(rs.getString("R42_NAME"));
			obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
			obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

			obj.setR43_NAME(rs.getString("R43_NAME"));
			obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
			obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

			obj.setR44_NAME(rs.getString("R44_NAME"));
			obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
			obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

	public static class MDISB5_Detail_Entity3 {

		private String R37_NAME;
		private String R37_DESIGNATION_OR_POSITION;
		private BigDecimal R37_NUMBER_OF_ACCOUNTS;
		private BigDecimal R37_AMOUNT;

		private String R38_NAME;
		private String R38_DESIGNATION_OR_POSITION;
		private BigDecimal R38_NUMBER_OF_ACCOUNTS;
		private BigDecimal R38_AMOUNT;

		private String R39_NAME;
		private String R39_DESIGNATION_OR_POSITION;
		private BigDecimal R39_NUMBER_OF_ACCOUNTS;
		private BigDecimal R39_AMOUNT;

		private String R40_NAME;
		private String R40_DESIGNATION_OR_POSITION;
		private BigDecimal R40_NUMBER_OF_ACCOUNTS;
		private BigDecimal R40_AMOUNT;

		private String R41_NAME;
		private String R41_DESIGNATION_OR_POSITION;
		private BigDecimal R41_NUMBER_OF_ACCOUNTS;
		private BigDecimal R41_AMOUNT;

		private String R42_NAME;
		private String R42_DESIGNATION_OR_POSITION;
		private BigDecimal R42_NUMBER_OF_ACCOUNTS;
		private BigDecimal R42_AMOUNT;

		private String R43_NAME;
		private String R43_DESIGNATION_OR_POSITION;
		private BigDecimal R43_NUMBER_OF_ACCOUNTS;
		private BigDecimal R43_AMOUNT;

		private String R44_NAME;
		private String R44_DESIGNATION_OR_POSITION;
		private BigDecimal R44_NUMBER_OF_ACCOUNTS;
		private BigDecimal R44_AMOUNT;

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

		public String getR37_NAME() {
			return R37_NAME;
		}

		public void setR37_NAME(String r37_NAME) {
			R37_NAME = r37_NAME;
		}

		public String getR37_DESIGNATION_OR_POSITION() {
			return R37_DESIGNATION_OR_POSITION;
		}

		public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
			R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
			return R37_NUMBER_OF_ACCOUNTS;
		}

		public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
			R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR37_AMOUNT() {
			return R37_AMOUNT;
		}

		public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
			R37_AMOUNT = r37_AMOUNT;
		}

		public String getR38_NAME() {
			return R38_NAME;
		}

		public void setR38_NAME(String r38_NAME) {
			R38_NAME = r38_NAME;
		}

		public String getR38_DESIGNATION_OR_POSITION() {
			return R38_DESIGNATION_OR_POSITION;
		}

		public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
			R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
			return R38_NUMBER_OF_ACCOUNTS;
		}

		public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
			R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR38_AMOUNT() {
			return R38_AMOUNT;
		}

		public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
			R38_AMOUNT = r38_AMOUNT;
		}

		public String getR39_NAME() {
			return R39_NAME;
		}

		public void setR39_NAME(String r39_NAME) {
			R39_NAME = r39_NAME;
		}

		public String getR39_DESIGNATION_OR_POSITION() {
			return R39_DESIGNATION_OR_POSITION;
		}

		public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
			R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
			return R39_NUMBER_OF_ACCOUNTS;
		}

		public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
			R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public String getR40_NAME() {
			return R40_NAME;
		}

		public void setR40_NAME(String r40_NAME) {
			R40_NAME = r40_NAME;
		}

		public String getR40_DESIGNATION_OR_POSITION() {
			return R40_DESIGNATION_OR_POSITION;
		}

		public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
			R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
			return R40_NUMBER_OF_ACCOUNTS;
		}

		public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
			R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public String getR41_NAME() {
			return R41_NAME;
		}

		public void setR41_NAME(String r41_NAME) {
			R41_NAME = r41_NAME;
		}

		public String getR41_DESIGNATION_OR_POSITION() {
			return R41_DESIGNATION_OR_POSITION;
		}

		public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
			R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
			return R41_NUMBER_OF_ACCOUNTS;
		}

		public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
			R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public String getR42_NAME() {
			return R42_NAME;
		}

		public void setR42_NAME(String r42_NAME) {
			R42_NAME = r42_NAME;
		}

		public String getR42_DESIGNATION_OR_POSITION() {
			return R42_DESIGNATION_OR_POSITION;
		}

		public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
			R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
			return R42_NUMBER_OF_ACCOUNTS;
		}

		public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
			R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public String getR43_NAME() {
			return R43_NAME;
		}

		public void setR43_NAME(String r43_NAME) {
			R43_NAME = r43_NAME;
		}

		public String getR43_DESIGNATION_OR_POSITION() {
			return R43_DESIGNATION_OR_POSITION;
		}

		public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
			R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
			return R43_NUMBER_OF_ACCOUNTS;
		}

		public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
			R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public String getR44_NAME() {
			return R44_NAME;
		}

		public void setR44_NAME(String r44_NAME) {
			R44_NAME = r44_NAME;
		}

		public String getR44_DESIGNATION_OR_POSITION() {
			return R44_DESIGNATION_OR_POSITION;
		}

		public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
			R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
			return R44_NUMBER_OF_ACCOUNTS;
		}

		public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
			R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
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

	// ROW MAPPER ARCHIVAL1

	class MDISB5ArchivalDetaillRowMapper_ARCHIVAL1 implements RowMapper<MDISB5_Archival_Detail_Entity1> {

		@Override
		public MDISB5_Archival_Detail_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Detail_Entity1 obj = new MDISB5_Archival_Detail_Entity1();

			obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
			obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
			obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
			obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

			obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
			obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
			obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
			obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

			obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
			obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
			obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
			obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

			obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
			obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
			obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
			obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

			obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
			obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
			obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
			obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

			obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
			obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
			obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
			obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

			obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
			obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
			obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
			obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

			obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
			obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
			obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
			obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

			obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
			obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
			obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
			obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

			obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
			obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
			obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
			obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

			obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
			obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
			obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
			obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

	public static class MDISB5_Archival_Detail_Entity1 {

		private String R5_NAME_OF_SHAREHOLDER;
		private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R5_NUMBER_OF_ACCOUNTS;
		private BigDecimal R5_AMOUNT;

		private String R6_NAME_OF_SHAREHOLDER;
		private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R6_NUMBER_OF_ACCOUNTS;
		private BigDecimal R6_AMOUNT;

		private String R7_NAME_OF_SHAREHOLDER;
		private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R7_NUMBER_OF_ACCOUNTS;
		private BigDecimal R7_AMOUNT;

		private String R8_NAME_OF_SHAREHOLDER;
		private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R8_NUMBER_OF_ACCOUNTS;
		private BigDecimal R8_AMOUNT;

		private String R9_NAME_OF_SHAREHOLDER;
		private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R9_NUMBER_OF_ACCOUNTS;
		private BigDecimal R9_AMOUNT;

		private String R10_NAME_OF_SHAREHOLDER;
		private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R10_NUMBER_OF_ACCOUNTS;
		private BigDecimal R10_AMOUNT;

		private String R11_NAME_OF_SHAREHOLDER;
		private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R11_NUMBER_OF_ACCOUNTS;
		private BigDecimal R11_AMOUNT;

		private String R12_NAME_OF_SHAREHOLDER;
		private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R12_NUMBER_OF_ACCOUNTS;
		private BigDecimal R12_AMOUNT;

		private String R13_NAME_OF_SHAREHOLDER;
		private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R13_NUMBER_OF_ACCOUNTS;
		private BigDecimal R13_AMOUNT;

		private String R14_NAME_OF_SHAREHOLDER;
		private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R14_NUMBER_OF_ACCOUNTS;
		private BigDecimal R14_AMOUNT;

		private String R15_NAME_OF_SHAREHOLDER;
		private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
		private BigDecimal R15_NUMBER_OF_ACCOUNTS;
		private BigDecimal R15_AMOUNT;

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

		public String getR5_NAME_OF_SHAREHOLDER() {
			return R5_NAME_OF_SHAREHOLDER;
		}

		public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
			R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
			return R5_PERCENTAGE_SHAREHOLDING;
		}

		public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
			R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
			return R5_NUMBER_OF_ACCOUNTS;
		}

		public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
			R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR5_AMOUNT() {
			return R5_AMOUNT;
		}

		public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
			R5_AMOUNT = r5_AMOUNT;
		}

		public String getR6_NAME_OF_SHAREHOLDER() {
			return R6_NAME_OF_SHAREHOLDER;
		}

		public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
			R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
			return R6_PERCENTAGE_SHAREHOLDING;
		}

		public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
			R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
			return R6_NUMBER_OF_ACCOUNTS;
		}

		public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
			R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR6_AMOUNT() {
			return R6_AMOUNT;
		}

		public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
			R6_AMOUNT = r6_AMOUNT;
		}

		public String getR7_NAME_OF_SHAREHOLDER() {
			return R7_NAME_OF_SHAREHOLDER;
		}

		public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
			R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
			return R7_PERCENTAGE_SHAREHOLDING;
		}

		public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
			R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
			return R7_NUMBER_OF_ACCOUNTS;
		}

		public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
			R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR7_AMOUNT() {
			return R7_AMOUNT;
		}

		public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
			R7_AMOUNT = r7_AMOUNT;
		}

		public String getR8_NAME_OF_SHAREHOLDER() {
			return R8_NAME_OF_SHAREHOLDER;
		}

		public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
			R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
			return R8_PERCENTAGE_SHAREHOLDING;
		}

		public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
			R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
			return R8_NUMBER_OF_ACCOUNTS;
		}

		public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
			R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR8_AMOUNT() {
			return R8_AMOUNT;
		}

		public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
			R8_AMOUNT = r8_AMOUNT;
		}

		public String getR9_NAME_OF_SHAREHOLDER() {
			return R9_NAME_OF_SHAREHOLDER;
		}

		public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
			R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
			return R9_PERCENTAGE_SHAREHOLDING;
		}

		public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
			R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
			return R9_NUMBER_OF_ACCOUNTS;
		}

		public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
			R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR9_AMOUNT() {
			return R9_AMOUNT;
		}

		public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
			R9_AMOUNT = r9_AMOUNT;
		}

		public String getR10_NAME_OF_SHAREHOLDER() {
			return R10_NAME_OF_SHAREHOLDER;
		}

		public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
			R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
			return R10_PERCENTAGE_SHAREHOLDING;
		}

		public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
			R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
			return R10_NUMBER_OF_ACCOUNTS;
		}

		public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
			R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR10_AMOUNT() {
			return R10_AMOUNT;
		}

		public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
			R10_AMOUNT = r10_AMOUNT;
		}

		public String getR11_NAME_OF_SHAREHOLDER() {
			return R11_NAME_OF_SHAREHOLDER;
		}

		public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
			R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
			return R11_PERCENTAGE_SHAREHOLDING;
		}

		public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
			R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
			return R11_NUMBER_OF_ACCOUNTS;
		}

		public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
			R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR11_AMOUNT() {
			return R11_AMOUNT;
		}

		public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
			R11_AMOUNT = r11_AMOUNT;
		}

		public String getR12_NAME_OF_SHAREHOLDER() {
			return R12_NAME_OF_SHAREHOLDER;
		}

		public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
			R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
			return R12_PERCENTAGE_SHAREHOLDING;
		}

		public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
			R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
			return R12_NUMBER_OF_ACCOUNTS;
		}

		public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
			R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR12_AMOUNT() {
			return R12_AMOUNT;
		}

		public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
			R12_AMOUNT = r12_AMOUNT;
		}

		public String getR13_NAME_OF_SHAREHOLDER() {
			return R13_NAME_OF_SHAREHOLDER;
		}

		public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
			R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
			return R13_PERCENTAGE_SHAREHOLDING;
		}

		public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
			R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
			return R13_NUMBER_OF_ACCOUNTS;
		}

		public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
			R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR13_AMOUNT() {
			return R13_AMOUNT;
		}

		public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
			R13_AMOUNT = r13_AMOUNT;
		}

		public String getR14_NAME_OF_SHAREHOLDER() {
			return R14_NAME_OF_SHAREHOLDER;
		}

		public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
			R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
			return R14_PERCENTAGE_SHAREHOLDING;
		}

		public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
			R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
			return R14_NUMBER_OF_ACCOUNTS;
		}

		public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
			R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR14_AMOUNT() {
			return R14_AMOUNT;
		}

		public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
			R14_AMOUNT = r14_AMOUNT;
		}

		public String getR15_NAME_OF_SHAREHOLDER() {
			return R15_NAME_OF_SHAREHOLDER;
		}

		public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
			R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
		}

		public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
			return R15_PERCENTAGE_SHAREHOLDING;
		}

		public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
			R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
		}

		public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
			return R15_NUMBER_OF_ACCOUNTS;
		}

		public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
			R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR15_AMOUNT() {
			return R15_AMOUNT;
		}

		public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
			R15_AMOUNT = r15_AMOUNT;
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

	//// NORMAL ARCHIVAL ENTITY 2

	class MDISB5ArchivalDetaillRowMapper_ARCHIVAL2 implements RowMapper<MDISB5_Archival_Detail_Entity2> {

		@Override
		public MDISB5_Archival_Detail_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Detail_Entity2 obj = new MDISB5_Archival_Detail_Entity2();

			obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
			obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
			obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

			obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
			obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
			obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

			obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
			obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
			obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

			obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
			obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
			obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

			obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
			obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
			obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

			obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
			obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
			obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

			obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
			obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
			obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

			obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
			obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
			obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

			obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
			obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
			obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

			obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
			obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
			obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

			obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
			obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
			obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

			obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
			obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
			obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

			obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
			obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
			obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

			obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
			obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
			obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
			obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

	public static class MDISB5_Archival_Detail_Entity2 {

		private String R20_NAME_OF_BOARD_MEMBERS;
		private String R20_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R20_NUMBER_OF_ACCOUNTS;
		private BigDecimal R20_AMOUNT;

		private String R21_NAME_OF_BOARD_MEMBERS;
		private String R21_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R21_NUMBER_OF_ACCOUNTS;
		private BigDecimal R21_AMOUNT;

		private String R22_NAME_OF_BOARD_MEMBERS;
		private String R22_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R22_NUMBER_OF_ACCOUNTS;
		private BigDecimal R22_AMOUNT;

		private String R23_NAME_OF_BOARD_MEMBERS;
		private String R23_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R23_NUMBER_OF_ACCOUNTS;
		private BigDecimal R23_AMOUNT;

		private String R24_NAME_OF_BOARD_MEMBERS;
		private String R24_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R24_NUMBER_OF_ACCOUNTS;
		private BigDecimal R24_AMOUNT;

		private String R25_NAME_OF_BOARD_MEMBERS;
		private String R25_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R25_NUMBER_OF_ACCOUNTS;
		private BigDecimal R25_AMOUNT;

		private String R26_NAME_OF_BOARD_MEMBERS;
		private String R26_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R26_NUMBER_OF_ACCOUNTS;
		private BigDecimal R26_AMOUNT;

		private String R27_NAME_OF_BOARD_MEMBERS;
		private String R27_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R27_NUMBER_OF_ACCOUNTS;
		private BigDecimal R27_AMOUNT;

		private String R28_NAME_OF_BOARD_MEMBERS;
		private String R28_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R28_NUMBER_OF_ACCOUNTS;
		private BigDecimal R28_AMOUNT;

		private String R29_NAME_OF_BOARD_MEMBERS;
		private String R29_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R29_NUMBER_OF_ACCOUNTS;
		private BigDecimal R29_AMOUNT;

		private String R30_NAME_OF_BOARD_MEMBERS;
		private String R30_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R30_NUMBER_OF_ACCOUNTS;
		private BigDecimal R30_AMOUNT;

		private String R31_NAME_OF_BOARD_MEMBERS;
		private String R31_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R31_NUMBER_OF_ACCOUNTS;
		private BigDecimal R31_AMOUNT;

		private String R32_NAME_OF_BOARD_MEMBERS;
		private String R32_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R32_NUMBER_OF_ACCOUNTS;
		private BigDecimal R32_AMOUNT;

		private String R33_NAME_OF_BOARD_MEMBERS;
		private String R33_EXECUTIVE_OR_NONEXECUTIVE;
		private BigDecimal R33_NUMBER_OF_ACCOUNTS;
		private BigDecimal R33_AMOUNT;

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

		public String getR20_NAME_OF_BOARD_MEMBERS() {
			return R20_NAME_OF_BOARD_MEMBERS;
		}

		public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
			R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
		}

		public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
			return R20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
			R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
			return R20_NUMBER_OF_ACCOUNTS;
		}

		public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
			R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR20_AMOUNT() {
			return R20_AMOUNT;
		}

		public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
			R20_AMOUNT = r20_AMOUNT;
		}

		public String getR21_NAME_OF_BOARD_MEMBERS() {
			return R21_NAME_OF_BOARD_MEMBERS;
		}

		public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
			R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
		}

		public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
			return R21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
			R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
			return R21_NUMBER_OF_ACCOUNTS;
		}

		public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
			R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR21_AMOUNT() {
			return R21_AMOUNT;
		}

		public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
			R21_AMOUNT = r21_AMOUNT;
		}

		public String getR22_NAME_OF_BOARD_MEMBERS() {
			return R22_NAME_OF_BOARD_MEMBERS;
		}

		public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
			R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
		}

		public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
			return R22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
			R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
			return R22_NUMBER_OF_ACCOUNTS;
		}

		public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
			R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR22_AMOUNT() {
			return R22_AMOUNT;
		}

		public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
			R22_AMOUNT = r22_AMOUNT;
		}

		public String getR23_NAME_OF_BOARD_MEMBERS() {
			return R23_NAME_OF_BOARD_MEMBERS;
		}

		public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
			R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
		}

		public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
			return R23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
			R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
			return R23_NUMBER_OF_ACCOUNTS;
		}

		public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
			R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR23_AMOUNT() {
			return R23_AMOUNT;
		}

		public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
			R23_AMOUNT = r23_AMOUNT;
		}

		public String getR24_NAME_OF_BOARD_MEMBERS() {
			return R24_NAME_OF_BOARD_MEMBERS;
		}

		public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
			R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
		}

		public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
			return R24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
			R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
			return R24_NUMBER_OF_ACCOUNTS;
		}

		public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
			R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR24_AMOUNT() {
			return R24_AMOUNT;
		}

		public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
			R24_AMOUNT = r24_AMOUNT;
		}

		public String getR25_NAME_OF_BOARD_MEMBERS() {
			return R25_NAME_OF_BOARD_MEMBERS;
		}

		public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
			R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
		}

		public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
			return R25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
			R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
			return R25_NUMBER_OF_ACCOUNTS;
		}

		public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
			R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR25_AMOUNT() {
			return R25_AMOUNT;
		}

		public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
			R25_AMOUNT = r25_AMOUNT;
		}

		public String getR26_NAME_OF_BOARD_MEMBERS() {
			return R26_NAME_OF_BOARD_MEMBERS;
		}

		public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
			R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
		}

		public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
			return R26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
			R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
			return R26_NUMBER_OF_ACCOUNTS;
		}

		public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
			R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR26_AMOUNT() {
			return R26_AMOUNT;
		}

		public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
			R26_AMOUNT = r26_AMOUNT;
		}

		public String getR27_NAME_OF_BOARD_MEMBERS() {
			return R27_NAME_OF_BOARD_MEMBERS;
		}

		public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
			R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
		}

		public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
			return R27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
			R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
			return R27_NUMBER_OF_ACCOUNTS;
		}

		public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
			R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR27_AMOUNT() {
			return R27_AMOUNT;
		}

		public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
			R27_AMOUNT = r27_AMOUNT;
		}

		public String getR28_NAME_OF_BOARD_MEMBERS() {
			return R28_NAME_OF_BOARD_MEMBERS;
		}

		public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
			R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
		}

		public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
			return R28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
			R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
			return R28_NUMBER_OF_ACCOUNTS;
		}

		public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
			R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR28_AMOUNT() {
			return R28_AMOUNT;
		}

		public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
			R28_AMOUNT = r28_AMOUNT;
		}

		public String getR29_NAME_OF_BOARD_MEMBERS() {
			return R29_NAME_OF_BOARD_MEMBERS;
		}

		public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
			R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
		}

		public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
			return R29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
			R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
			return R29_NUMBER_OF_ACCOUNTS;
		}

		public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
			R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR29_AMOUNT() {
			return R29_AMOUNT;
		}

		public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
			R29_AMOUNT = r29_AMOUNT;
		}

		public String getR30_NAME_OF_BOARD_MEMBERS() {
			return R30_NAME_OF_BOARD_MEMBERS;
		}

		public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
			R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
		}

		public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
			return R30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
			R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
			return R30_NUMBER_OF_ACCOUNTS;
		}

		public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
			R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR30_AMOUNT() {
			return R30_AMOUNT;
		}

		public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
			R30_AMOUNT = r30_AMOUNT;
		}

		public String getR31_NAME_OF_BOARD_MEMBERS() {
			return R31_NAME_OF_BOARD_MEMBERS;
		}

		public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
			R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
		}

		public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
			return R31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
			R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
			return R31_NUMBER_OF_ACCOUNTS;
		}

		public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
			R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR31_AMOUNT() {
			return R31_AMOUNT;
		}

		public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
			R31_AMOUNT = r31_AMOUNT;
		}

		public String getR32_NAME_OF_BOARD_MEMBERS() {
			return R32_NAME_OF_BOARD_MEMBERS;
		}

		public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
			R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
		}

		public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
			return R32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
			R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
			return R32_NUMBER_OF_ACCOUNTS;
		}

		public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
			R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR32_AMOUNT() {
			return R32_AMOUNT;
		}

		public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
			R32_AMOUNT = r32_AMOUNT;
		}

		public String getR33_NAME_OF_BOARD_MEMBERS() {
			return R33_NAME_OF_BOARD_MEMBERS;
		}

		public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
			R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
		}

		public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
			return R33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
			R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
		}

		public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
			return R33_NUMBER_OF_ACCOUNTS;
		}

		public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
			R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR33_AMOUNT() {
			return R33_AMOUNT;
		}

		public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
			R33_AMOUNT = r33_AMOUNT;
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

	//// ROWMAPPER NORMAL ARCHIVAL3

	class MDISB5ArchivalDetaillRowMapper_ARCHIVAL3 implements RowMapper<MDISB5_Archival_Detail_Entity3> {

		@Override
		public MDISB5_Archival_Detail_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

			MDISB5_Archival_Detail_Entity3 obj = new MDISB5_Archival_Detail_Entity3();

			obj.setR37_NAME(rs.getString("R37_NAME"));
			obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
			obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
			obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

			obj.setR38_NAME(rs.getString("R38_NAME"));
			obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
			obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
			obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

			obj.setR39_NAME(rs.getString("R39_NAME"));
			obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
			obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
			obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

			obj.setR40_NAME(rs.getString("R40_NAME"));
			obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
			obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
			obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

			obj.setR41_NAME(rs.getString("R41_NAME"));
			obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
			obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
			obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

			obj.setR42_NAME(rs.getString("R42_NAME"));
			obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
			obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
			obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

			obj.setR43_NAME(rs.getString("R43_NAME"));
			obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
			obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
			obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

			obj.setR44_NAME(rs.getString("R44_NAME"));
			obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
			obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
			obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

	public static class MDISB5_Archival_Detail_Entity3 {

		private String R37_NAME;
		private String R37_DESIGNATION_OR_POSITION;
		private BigDecimal R37_NUMBER_OF_ACCOUNTS;
		private BigDecimal R37_AMOUNT;

		private String R38_NAME;
		private String R38_DESIGNATION_OR_POSITION;
		private BigDecimal R38_NUMBER_OF_ACCOUNTS;
		private BigDecimal R38_AMOUNT;

		private String R39_NAME;
		private String R39_DESIGNATION_OR_POSITION;
		private BigDecimal R39_NUMBER_OF_ACCOUNTS;
		private BigDecimal R39_AMOUNT;

		private String R40_NAME;
		private String R40_DESIGNATION_OR_POSITION;
		private BigDecimal R40_NUMBER_OF_ACCOUNTS;
		private BigDecimal R40_AMOUNT;

		private String R41_NAME;
		private String R41_DESIGNATION_OR_POSITION;
		private BigDecimal R41_NUMBER_OF_ACCOUNTS;
		private BigDecimal R41_AMOUNT;

		private String R42_NAME;
		private String R42_DESIGNATION_OR_POSITION;
		private BigDecimal R42_NUMBER_OF_ACCOUNTS;
		private BigDecimal R42_AMOUNT;

		private String R43_NAME;
		private String R43_DESIGNATION_OR_POSITION;
		private BigDecimal R43_NUMBER_OF_ACCOUNTS;
		private BigDecimal R43_AMOUNT;

		private String R44_NAME;
		private String R44_DESIGNATION_OR_POSITION;
		private BigDecimal R44_NUMBER_OF_ACCOUNTS;
		private BigDecimal R44_AMOUNT;

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

		public String getR37_NAME() {
			return R37_NAME;
		}

		public void setR37_NAME(String r37_NAME) {
			R37_NAME = r37_NAME;
		}

		public String getR37_DESIGNATION_OR_POSITION() {
			return R37_DESIGNATION_OR_POSITION;
		}

		public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
			R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
			return R37_NUMBER_OF_ACCOUNTS;
		}

		public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
			R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR37_AMOUNT() {
			return R37_AMOUNT;
		}

		public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
			R37_AMOUNT = r37_AMOUNT;
		}

		public String getR38_NAME() {
			return R38_NAME;
		}

		public void setR38_NAME(String r38_NAME) {
			R38_NAME = r38_NAME;
		}

		public String getR38_DESIGNATION_OR_POSITION() {
			return R38_DESIGNATION_OR_POSITION;
		}

		public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
			R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
			return R38_NUMBER_OF_ACCOUNTS;
		}

		public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
			R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR38_AMOUNT() {
			return R38_AMOUNT;
		}

		public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
			R38_AMOUNT = r38_AMOUNT;
		}

		public String getR39_NAME() {
			return R39_NAME;
		}

		public void setR39_NAME(String r39_NAME) {
			R39_NAME = r39_NAME;
		}

		public String getR39_DESIGNATION_OR_POSITION() {
			return R39_DESIGNATION_OR_POSITION;
		}

		public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
			R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
			return R39_NUMBER_OF_ACCOUNTS;
		}

		public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
			R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR39_AMOUNT() {
			return R39_AMOUNT;
		}

		public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
			R39_AMOUNT = r39_AMOUNT;
		}

		public String getR40_NAME() {
			return R40_NAME;
		}

		public void setR40_NAME(String r40_NAME) {
			R40_NAME = r40_NAME;
		}

		public String getR40_DESIGNATION_OR_POSITION() {
			return R40_DESIGNATION_OR_POSITION;
		}

		public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
			R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
			return R40_NUMBER_OF_ACCOUNTS;
		}

		public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
			R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR40_AMOUNT() {
			return R40_AMOUNT;
		}

		public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
			R40_AMOUNT = r40_AMOUNT;
		}

		public String getR41_NAME() {
			return R41_NAME;
		}

		public void setR41_NAME(String r41_NAME) {
			R41_NAME = r41_NAME;
		}

		public String getR41_DESIGNATION_OR_POSITION() {
			return R41_DESIGNATION_OR_POSITION;
		}

		public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
			R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
			return R41_NUMBER_OF_ACCOUNTS;
		}

		public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
			R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR41_AMOUNT() {
			return R41_AMOUNT;
		}

		public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
			R41_AMOUNT = r41_AMOUNT;
		}

		public String getR42_NAME() {
			return R42_NAME;
		}

		public void setR42_NAME(String r42_NAME) {
			R42_NAME = r42_NAME;
		}

		public String getR42_DESIGNATION_OR_POSITION() {
			return R42_DESIGNATION_OR_POSITION;
		}

		public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
			R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
			return R42_NUMBER_OF_ACCOUNTS;
		}

		public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
			R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR42_AMOUNT() {
			return R42_AMOUNT;
		}

		public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
			R42_AMOUNT = r42_AMOUNT;
		}

		public String getR43_NAME() {
			return R43_NAME;
		}

		public void setR43_NAME(String r43_NAME) {
			R43_NAME = r43_NAME;
		}

		public String getR43_DESIGNATION_OR_POSITION() {
			return R43_DESIGNATION_OR_POSITION;
		}

		public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
			R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
			return R43_NUMBER_OF_ACCOUNTS;
		}

		public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
			R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR43_AMOUNT() {
			return R43_AMOUNT;
		}

		public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
			R43_AMOUNT = r43_AMOUNT;
		}

		public String getR44_NAME() {
			return R44_NAME;
		}

		public void setR44_NAME(String r44_NAME) {
			R44_NAME = r44_NAME;
		}

		public String getR44_DESIGNATION_OR_POSITION() {
			return R44_DESIGNATION_OR_POSITION;
		}

		public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
			R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
		}

		public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
			return R44_NUMBER_OF_ACCOUNTS;
		}

		public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
			R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
		}

		public BigDecimal getR44_AMOUNT() {
			return R44_AMOUNT;
		}

		public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
			R44_AMOUNT = r44_AMOUNT;
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
	
	
	
	
	
	// ROW MAPPER RESUB1

		class MDISB5RESUBDetaillRowMapper_RESUB1 implements RowMapper<MDISB5_RESUB_Detail_Entity1> {

			@Override
			public MDISB5_RESUB_Detail_Entity1 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Detail_Entity1 obj = new MDISB5_RESUB_Detail_Entity1();

				obj.setR5_NAME_OF_SHAREHOLDER(rs.getString("R5_NAME_OF_SHAREHOLDER"));
				obj.setR5_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R5_PERCENTAGE_SHAREHOLDING"));
				obj.setR5_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R5_NUMBER_OF_ACCOUNTS"));
				obj.setR5_AMOUNT(rs.getBigDecimal("R5_AMOUNT"));

				obj.setR6_NAME_OF_SHAREHOLDER(rs.getString("R6_NAME_OF_SHAREHOLDER"));
				obj.setR6_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R6_PERCENTAGE_SHAREHOLDING"));
				obj.setR6_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R6_NUMBER_OF_ACCOUNTS"));
				obj.setR6_AMOUNT(rs.getBigDecimal("R6_AMOUNT"));

				obj.setR7_NAME_OF_SHAREHOLDER(rs.getString("R7_NAME_OF_SHAREHOLDER"));
				obj.setR7_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R7_PERCENTAGE_SHAREHOLDING"));
				obj.setR7_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R7_NUMBER_OF_ACCOUNTS"));
				obj.setR7_AMOUNT(rs.getBigDecimal("R7_AMOUNT"));

				obj.setR8_NAME_OF_SHAREHOLDER(rs.getString("R8_NAME_OF_SHAREHOLDER"));
				obj.setR8_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R8_PERCENTAGE_SHAREHOLDING"));
				obj.setR8_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R8_NUMBER_OF_ACCOUNTS"));
				obj.setR8_AMOUNT(rs.getBigDecimal("R8_AMOUNT"));

				obj.setR9_NAME_OF_SHAREHOLDER(rs.getString("R9_NAME_OF_SHAREHOLDER"));
				obj.setR9_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R9_PERCENTAGE_SHAREHOLDING"));
				obj.setR9_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R9_NUMBER_OF_ACCOUNTS"));
				obj.setR9_AMOUNT(rs.getBigDecimal("R9_AMOUNT"));

				obj.setR10_NAME_OF_SHAREHOLDER(rs.getString("R10_NAME_OF_SHAREHOLDER"));
				obj.setR10_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R10_PERCENTAGE_SHAREHOLDING"));
				obj.setR10_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R10_NUMBER_OF_ACCOUNTS"));
				obj.setR10_AMOUNT(rs.getBigDecimal("R10_AMOUNT"));

				obj.setR11_NAME_OF_SHAREHOLDER(rs.getString("R11_NAME_OF_SHAREHOLDER"));
				obj.setR11_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R11_PERCENTAGE_SHAREHOLDING"));
				obj.setR11_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R11_NUMBER_OF_ACCOUNTS"));
				obj.setR11_AMOUNT(rs.getBigDecimal("R11_AMOUNT"));

				obj.setR12_NAME_OF_SHAREHOLDER(rs.getString("R12_NAME_OF_SHAREHOLDER"));
				obj.setR12_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R12_PERCENTAGE_SHAREHOLDING"));
				obj.setR12_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R12_NUMBER_OF_ACCOUNTS"));
				obj.setR12_AMOUNT(rs.getBigDecimal("R12_AMOUNT"));

				obj.setR13_NAME_OF_SHAREHOLDER(rs.getString("R13_NAME_OF_SHAREHOLDER"));
				obj.setR13_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R13_PERCENTAGE_SHAREHOLDING"));
				obj.setR13_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R13_NUMBER_OF_ACCOUNTS"));
				obj.setR13_AMOUNT(rs.getBigDecimal("R13_AMOUNT"));

				obj.setR14_NAME_OF_SHAREHOLDER(rs.getString("R14_NAME_OF_SHAREHOLDER"));
				obj.setR14_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R14_PERCENTAGE_SHAREHOLDING"));
				obj.setR14_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R14_NUMBER_OF_ACCOUNTS"));
				obj.setR14_AMOUNT(rs.getBigDecimal("R14_AMOUNT"));

				obj.setR15_NAME_OF_SHAREHOLDER(rs.getString("R15_NAME_OF_SHAREHOLDER"));
				obj.setR15_PERCENTAGE_SHAREHOLDING(rs.getBigDecimal("R15_PERCENTAGE_SHAREHOLDING"));
				obj.setR15_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R15_NUMBER_OF_ACCOUNTS"));
				obj.setR15_AMOUNT(rs.getBigDecimal("R15_AMOUNT"));

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

		public static class MDISB5_RESUB_Detail_Entity1 {

			private String R5_NAME_OF_SHAREHOLDER;
			private BigDecimal R5_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R5_NUMBER_OF_ACCOUNTS;
			private BigDecimal R5_AMOUNT;

			private String R6_NAME_OF_SHAREHOLDER;
			private BigDecimal R6_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R6_NUMBER_OF_ACCOUNTS;
			private BigDecimal R6_AMOUNT;

			private String R7_NAME_OF_SHAREHOLDER;
			private BigDecimal R7_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R7_NUMBER_OF_ACCOUNTS;
			private BigDecimal R7_AMOUNT;

			private String R8_NAME_OF_SHAREHOLDER;
			private BigDecimal R8_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R8_NUMBER_OF_ACCOUNTS;
			private BigDecimal R8_AMOUNT;

			private String R9_NAME_OF_SHAREHOLDER;
			private BigDecimal R9_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R9_NUMBER_OF_ACCOUNTS;
			private BigDecimal R9_AMOUNT;

			private String R10_NAME_OF_SHAREHOLDER;
			private BigDecimal R10_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R10_NUMBER_OF_ACCOUNTS;
			private BigDecimal R10_AMOUNT;

			private String R11_NAME_OF_SHAREHOLDER;
			private BigDecimal R11_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R11_NUMBER_OF_ACCOUNTS;
			private BigDecimal R11_AMOUNT;

			private String R12_NAME_OF_SHAREHOLDER;
			private BigDecimal R12_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R12_NUMBER_OF_ACCOUNTS;
			private BigDecimal R12_AMOUNT;

			private String R13_NAME_OF_SHAREHOLDER;
			private BigDecimal R13_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R13_NUMBER_OF_ACCOUNTS;
			private BigDecimal R13_AMOUNT;

			private String R14_NAME_OF_SHAREHOLDER;
			private BigDecimal R14_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R14_NUMBER_OF_ACCOUNTS;
			private BigDecimal R14_AMOUNT;

			private String R15_NAME_OF_SHAREHOLDER;
			private BigDecimal R15_PERCENTAGE_SHAREHOLDING;
			private BigDecimal R15_NUMBER_OF_ACCOUNTS;
			private BigDecimal R15_AMOUNT;

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

			public String getR5_NAME_OF_SHAREHOLDER() {
				return R5_NAME_OF_SHAREHOLDER;
			}

			public void setR5_NAME_OF_SHAREHOLDER(String r5_NAME_OF_SHAREHOLDER) {
				R5_NAME_OF_SHAREHOLDER = r5_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR5_PERCENTAGE_SHAREHOLDING() {
				return R5_PERCENTAGE_SHAREHOLDING;
			}

			public void setR5_PERCENTAGE_SHAREHOLDING(BigDecimal r5_PERCENTAGE_SHAREHOLDING) {
				R5_PERCENTAGE_SHAREHOLDING = r5_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR5_NUMBER_OF_ACCOUNTS() {
				return R5_NUMBER_OF_ACCOUNTS;
			}

			public void setR5_NUMBER_OF_ACCOUNTS(BigDecimal r5_NUMBER_OF_ACCOUNTS) {
				R5_NUMBER_OF_ACCOUNTS = r5_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR5_AMOUNT() {
				return R5_AMOUNT;
			}

			public void setR5_AMOUNT(BigDecimal r5_AMOUNT) {
				R5_AMOUNT = r5_AMOUNT;
			}

			public String getR6_NAME_OF_SHAREHOLDER() {
				return R6_NAME_OF_SHAREHOLDER;
			}

			public void setR6_NAME_OF_SHAREHOLDER(String r6_NAME_OF_SHAREHOLDER) {
				R6_NAME_OF_SHAREHOLDER = r6_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR6_PERCENTAGE_SHAREHOLDING() {
				return R6_PERCENTAGE_SHAREHOLDING;
			}

			public void setR6_PERCENTAGE_SHAREHOLDING(BigDecimal r6_PERCENTAGE_SHAREHOLDING) {
				R6_PERCENTAGE_SHAREHOLDING = r6_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR6_NUMBER_OF_ACCOUNTS() {
				return R6_NUMBER_OF_ACCOUNTS;
			}

			public void setR6_NUMBER_OF_ACCOUNTS(BigDecimal r6_NUMBER_OF_ACCOUNTS) {
				R6_NUMBER_OF_ACCOUNTS = r6_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR6_AMOUNT() {
				return R6_AMOUNT;
			}

			public void setR6_AMOUNT(BigDecimal r6_AMOUNT) {
				R6_AMOUNT = r6_AMOUNT;
			}

			public String getR7_NAME_OF_SHAREHOLDER() {
				return R7_NAME_OF_SHAREHOLDER;
			}

			public void setR7_NAME_OF_SHAREHOLDER(String r7_NAME_OF_SHAREHOLDER) {
				R7_NAME_OF_SHAREHOLDER = r7_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR7_PERCENTAGE_SHAREHOLDING() {
				return R7_PERCENTAGE_SHAREHOLDING;
			}

			public void setR7_PERCENTAGE_SHAREHOLDING(BigDecimal r7_PERCENTAGE_SHAREHOLDING) {
				R7_PERCENTAGE_SHAREHOLDING = r7_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR7_NUMBER_OF_ACCOUNTS() {
				return R7_NUMBER_OF_ACCOUNTS;
			}

			public void setR7_NUMBER_OF_ACCOUNTS(BigDecimal r7_NUMBER_OF_ACCOUNTS) {
				R7_NUMBER_OF_ACCOUNTS = r7_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR7_AMOUNT() {
				return R7_AMOUNT;
			}

			public void setR7_AMOUNT(BigDecimal r7_AMOUNT) {
				R7_AMOUNT = r7_AMOUNT;
			}

			public String getR8_NAME_OF_SHAREHOLDER() {
				return R8_NAME_OF_SHAREHOLDER;
			}

			public void setR8_NAME_OF_SHAREHOLDER(String r8_NAME_OF_SHAREHOLDER) {
				R8_NAME_OF_SHAREHOLDER = r8_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR8_PERCENTAGE_SHAREHOLDING() {
				return R8_PERCENTAGE_SHAREHOLDING;
			}

			public void setR8_PERCENTAGE_SHAREHOLDING(BigDecimal r8_PERCENTAGE_SHAREHOLDING) {
				R8_PERCENTAGE_SHAREHOLDING = r8_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR8_NUMBER_OF_ACCOUNTS() {
				return R8_NUMBER_OF_ACCOUNTS;
			}

			public void setR8_NUMBER_OF_ACCOUNTS(BigDecimal r8_NUMBER_OF_ACCOUNTS) {
				R8_NUMBER_OF_ACCOUNTS = r8_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR8_AMOUNT() {
				return R8_AMOUNT;
			}

			public void setR8_AMOUNT(BigDecimal r8_AMOUNT) {
				R8_AMOUNT = r8_AMOUNT;
			}

			public String getR9_NAME_OF_SHAREHOLDER() {
				return R9_NAME_OF_SHAREHOLDER;
			}

			public void setR9_NAME_OF_SHAREHOLDER(String r9_NAME_OF_SHAREHOLDER) {
				R9_NAME_OF_SHAREHOLDER = r9_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR9_PERCENTAGE_SHAREHOLDING() {
				return R9_PERCENTAGE_SHAREHOLDING;
			}

			public void setR9_PERCENTAGE_SHAREHOLDING(BigDecimal r9_PERCENTAGE_SHAREHOLDING) {
				R9_PERCENTAGE_SHAREHOLDING = r9_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR9_NUMBER_OF_ACCOUNTS() {
				return R9_NUMBER_OF_ACCOUNTS;
			}

			public void setR9_NUMBER_OF_ACCOUNTS(BigDecimal r9_NUMBER_OF_ACCOUNTS) {
				R9_NUMBER_OF_ACCOUNTS = r9_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR9_AMOUNT() {
				return R9_AMOUNT;
			}

			public void setR9_AMOUNT(BigDecimal r9_AMOUNT) {
				R9_AMOUNT = r9_AMOUNT;
			}

			public String getR10_NAME_OF_SHAREHOLDER() {
				return R10_NAME_OF_SHAREHOLDER;
			}

			public void setR10_NAME_OF_SHAREHOLDER(String r10_NAME_OF_SHAREHOLDER) {
				R10_NAME_OF_SHAREHOLDER = r10_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR10_PERCENTAGE_SHAREHOLDING() {
				return R10_PERCENTAGE_SHAREHOLDING;
			}

			public void setR10_PERCENTAGE_SHAREHOLDING(BigDecimal r10_PERCENTAGE_SHAREHOLDING) {
				R10_PERCENTAGE_SHAREHOLDING = r10_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR10_NUMBER_OF_ACCOUNTS() {
				return R10_NUMBER_OF_ACCOUNTS;
			}

			public void setR10_NUMBER_OF_ACCOUNTS(BigDecimal r10_NUMBER_OF_ACCOUNTS) {
				R10_NUMBER_OF_ACCOUNTS = r10_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR10_AMOUNT() {
				return R10_AMOUNT;
			}

			public void setR10_AMOUNT(BigDecimal r10_AMOUNT) {
				R10_AMOUNT = r10_AMOUNT;
			}

			public String getR11_NAME_OF_SHAREHOLDER() {
				return R11_NAME_OF_SHAREHOLDER;
			}

			public void setR11_NAME_OF_SHAREHOLDER(String r11_NAME_OF_SHAREHOLDER) {
				R11_NAME_OF_SHAREHOLDER = r11_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR11_PERCENTAGE_SHAREHOLDING() {
				return R11_PERCENTAGE_SHAREHOLDING;
			}

			public void setR11_PERCENTAGE_SHAREHOLDING(BigDecimal r11_PERCENTAGE_SHAREHOLDING) {
				R11_PERCENTAGE_SHAREHOLDING = r11_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR11_NUMBER_OF_ACCOUNTS() {
				return R11_NUMBER_OF_ACCOUNTS;
			}

			public void setR11_NUMBER_OF_ACCOUNTS(BigDecimal r11_NUMBER_OF_ACCOUNTS) {
				R11_NUMBER_OF_ACCOUNTS = r11_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR11_AMOUNT() {
				return R11_AMOUNT;
			}

			public void setR11_AMOUNT(BigDecimal r11_AMOUNT) {
				R11_AMOUNT = r11_AMOUNT;
			}

			public String getR12_NAME_OF_SHAREHOLDER() {
				return R12_NAME_OF_SHAREHOLDER;
			}

			public void setR12_NAME_OF_SHAREHOLDER(String r12_NAME_OF_SHAREHOLDER) {
				R12_NAME_OF_SHAREHOLDER = r12_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR12_PERCENTAGE_SHAREHOLDING() {
				return R12_PERCENTAGE_SHAREHOLDING;
			}

			public void setR12_PERCENTAGE_SHAREHOLDING(BigDecimal r12_PERCENTAGE_SHAREHOLDING) {
				R12_PERCENTAGE_SHAREHOLDING = r12_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR12_NUMBER_OF_ACCOUNTS() {
				return R12_NUMBER_OF_ACCOUNTS;
			}

			public void setR12_NUMBER_OF_ACCOUNTS(BigDecimal r12_NUMBER_OF_ACCOUNTS) {
				R12_NUMBER_OF_ACCOUNTS = r12_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR12_AMOUNT() {
				return R12_AMOUNT;
			}

			public void setR12_AMOUNT(BigDecimal r12_AMOUNT) {
				R12_AMOUNT = r12_AMOUNT;
			}

			public String getR13_NAME_OF_SHAREHOLDER() {
				return R13_NAME_OF_SHAREHOLDER;
			}

			public void setR13_NAME_OF_SHAREHOLDER(String r13_NAME_OF_SHAREHOLDER) {
				R13_NAME_OF_SHAREHOLDER = r13_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR13_PERCENTAGE_SHAREHOLDING() {
				return R13_PERCENTAGE_SHAREHOLDING;
			}

			public void setR13_PERCENTAGE_SHAREHOLDING(BigDecimal r13_PERCENTAGE_SHAREHOLDING) {
				R13_PERCENTAGE_SHAREHOLDING = r13_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR13_NUMBER_OF_ACCOUNTS() {
				return R13_NUMBER_OF_ACCOUNTS;
			}

			public void setR13_NUMBER_OF_ACCOUNTS(BigDecimal r13_NUMBER_OF_ACCOUNTS) {
				R13_NUMBER_OF_ACCOUNTS = r13_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR13_AMOUNT() {
				return R13_AMOUNT;
			}

			public void setR13_AMOUNT(BigDecimal r13_AMOUNT) {
				R13_AMOUNT = r13_AMOUNT;
			}

			public String getR14_NAME_OF_SHAREHOLDER() {
				return R14_NAME_OF_SHAREHOLDER;
			}

			public void setR14_NAME_OF_SHAREHOLDER(String r14_NAME_OF_SHAREHOLDER) {
				R14_NAME_OF_SHAREHOLDER = r14_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR14_PERCENTAGE_SHAREHOLDING() {
				return R14_PERCENTAGE_SHAREHOLDING;
			}

			public void setR14_PERCENTAGE_SHAREHOLDING(BigDecimal r14_PERCENTAGE_SHAREHOLDING) {
				R14_PERCENTAGE_SHAREHOLDING = r14_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR14_NUMBER_OF_ACCOUNTS() {
				return R14_NUMBER_OF_ACCOUNTS;
			}

			public void setR14_NUMBER_OF_ACCOUNTS(BigDecimal r14_NUMBER_OF_ACCOUNTS) {
				R14_NUMBER_OF_ACCOUNTS = r14_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR14_AMOUNT() {
				return R14_AMOUNT;
			}

			public void setR14_AMOUNT(BigDecimal r14_AMOUNT) {
				R14_AMOUNT = r14_AMOUNT;
			}

			public String getR15_NAME_OF_SHAREHOLDER() {
				return R15_NAME_OF_SHAREHOLDER;
			}

			public void setR15_NAME_OF_SHAREHOLDER(String r15_NAME_OF_SHAREHOLDER) {
				R15_NAME_OF_SHAREHOLDER = r15_NAME_OF_SHAREHOLDER;
			}

			public BigDecimal getR15_PERCENTAGE_SHAREHOLDING() {
				return R15_PERCENTAGE_SHAREHOLDING;
			}

			public void setR15_PERCENTAGE_SHAREHOLDING(BigDecimal r15_PERCENTAGE_SHAREHOLDING) {
				R15_PERCENTAGE_SHAREHOLDING = r15_PERCENTAGE_SHAREHOLDING;
			}

			public BigDecimal getR15_NUMBER_OF_ACCOUNTS() {
				return R15_NUMBER_OF_ACCOUNTS;
			}

			public void setR15_NUMBER_OF_ACCOUNTS(BigDecimal r15_NUMBER_OF_ACCOUNTS) {
				R15_NUMBER_OF_ACCOUNTS = r15_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR15_AMOUNT() {
				return R15_AMOUNT;
			}

			public void setR15_AMOUNT(BigDecimal r15_AMOUNT) {
				R15_AMOUNT = r15_AMOUNT;
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

		//// NORMAL RESUB ENTITY 2

		class MDISB5RESUBDetaillRowMapper_RESUB2 implements RowMapper<MDISB5_RESUB_Detail_Entity2> {

			@Override
			public MDISB5_RESUB_Detail_Entity2 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Detail_Entity2 obj = new MDISB5_RESUB_Detail_Entity2();

				obj.setR20_NAME_OF_BOARD_MEMBERS(rs.getString("R20_NAME_OF_BOARD_MEMBERS"));
				obj.setR20_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R20_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR20_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R20_NUMBER_OF_ACCOUNTS"));
				obj.setR20_AMOUNT(rs.getBigDecimal("R20_AMOUNT"));

				obj.setR21_NAME_OF_BOARD_MEMBERS(rs.getString("R21_NAME_OF_BOARD_MEMBERS"));
				obj.setR21_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R21_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR21_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R21_NUMBER_OF_ACCOUNTS"));
				obj.setR21_AMOUNT(rs.getBigDecimal("R21_AMOUNT"));

				obj.setR22_NAME_OF_BOARD_MEMBERS(rs.getString("R22_NAME_OF_BOARD_MEMBERS"));
				obj.setR22_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R22_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR22_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R22_NUMBER_OF_ACCOUNTS"));
				obj.setR22_AMOUNT(rs.getBigDecimal("R22_AMOUNT"));

				obj.setR23_NAME_OF_BOARD_MEMBERS(rs.getString("R23_NAME_OF_BOARD_MEMBERS"));
				obj.setR23_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R23_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR23_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R23_NUMBER_OF_ACCOUNTS"));
				obj.setR23_AMOUNT(rs.getBigDecimal("R23_AMOUNT"));

				obj.setR24_NAME_OF_BOARD_MEMBERS(rs.getString("R24_NAME_OF_BOARD_MEMBERS"));
				obj.setR24_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R24_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR24_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R24_NUMBER_OF_ACCOUNTS"));
				obj.setR24_AMOUNT(rs.getBigDecimal("R24_AMOUNT"));

				obj.setR25_NAME_OF_BOARD_MEMBERS(rs.getString("R25_NAME_OF_BOARD_MEMBERS"));
				obj.setR25_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R25_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR25_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R25_NUMBER_OF_ACCOUNTS"));
				obj.setR25_AMOUNT(rs.getBigDecimal("R25_AMOUNT"));

				obj.setR26_NAME_OF_BOARD_MEMBERS(rs.getString("R26_NAME_OF_BOARD_MEMBERS"));
				obj.setR26_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R26_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR26_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R26_NUMBER_OF_ACCOUNTS"));
				obj.setR26_AMOUNT(rs.getBigDecimal("R26_AMOUNT"));

				obj.setR27_NAME_OF_BOARD_MEMBERS(rs.getString("R27_NAME_OF_BOARD_MEMBERS"));
				obj.setR27_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R27_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR27_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R27_NUMBER_OF_ACCOUNTS"));
				obj.setR27_AMOUNT(rs.getBigDecimal("R27_AMOUNT"));

				obj.setR28_NAME_OF_BOARD_MEMBERS(rs.getString("R28_NAME_OF_BOARD_MEMBERS"));
				obj.setR28_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R28_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR28_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R28_NUMBER_OF_ACCOUNTS"));
				obj.setR28_AMOUNT(rs.getBigDecimal("R28_AMOUNT"));

				obj.setR29_NAME_OF_BOARD_MEMBERS(rs.getString("R29_NAME_OF_BOARD_MEMBERS"));
				obj.setR29_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R29_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR29_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R29_NUMBER_OF_ACCOUNTS"));
				obj.setR29_AMOUNT(rs.getBigDecimal("R29_AMOUNT"));

				obj.setR30_NAME_OF_BOARD_MEMBERS(rs.getString("R30_NAME_OF_BOARD_MEMBERS"));
				obj.setR30_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R30_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR30_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R30_NUMBER_OF_ACCOUNTS"));
				obj.setR30_AMOUNT(rs.getBigDecimal("R30_AMOUNT"));

				obj.setR31_NAME_OF_BOARD_MEMBERS(rs.getString("R31_NAME_OF_BOARD_MEMBERS"));
				obj.setR31_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R31_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR31_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R31_NUMBER_OF_ACCOUNTS"));
				obj.setR31_AMOUNT(rs.getBigDecimal("R31_AMOUNT"));

				obj.setR32_NAME_OF_BOARD_MEMBERS(rs.getString("R32_NAME_OF_BOARD_MEMBERS"));
				obj.setR32_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R32_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR32_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R32_NUMBER_OF_ACCOUNTS"));
				obj.setR32_AMOUNT(rs.getBigDecimal("R32_AMOUNT"));

				obj.setR33_NAME_OF_BOARD_MEMBERS(rs.getString("R33_NAME_OF_BOARD_MEMBERS"));
				obj.setR33_EXECUTIVE_OR_NONEXECUTIVE(rs.getString("R33_EXECUTIVE_OR_NONEXECUTIVE"));
				obj.setR33_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R33_NUMBER_OF_ACCOUNTS"));
				obj.setR33_AMOUNT(rs.getBigDecimal("R33_AMOUNT"));

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

		public static class MDISB5_RESUB_Detail_Entity2 {

			private String R20_NAME_OF_BOARD_MEMBERS;
			private String R20_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R20_NUMBER_OF_ACCOUNTS;
			private BigDecimal R20_AMOUNT;

			private String R21_NAME_OF_BOARD_MEMBERS;
			private String R21_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R21_NUMBER_OF_ACCOUNTS;
			private BigDecimal R21_AMOUNT;

			private String R22_NAME_OF_BOARD_MEMBERS;
			private String R22_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R22_NUMBER_OF_ACCOUNTS;
			private BigDecimal R22_AMOUNT;

			private String R23_NAME_OF_BOARD_MEMBERS;
			private String R23_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R23_NUMBER_OF_ACCOUNTS;
			private BigDecimal R23_AMOUNT;

			private String R24_NAME_OF_BOARD_MEMBERS;
			private String R24_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R24_NUMBER_OF_ACCOUNTS;
			private BigDecimal R24_AMOUNT;

			private String R25_NAME_OF_BOARD_MEMBERS;
			private String R25_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R25_NUMBER_OF_ACCOUNTS;
			private BigDecimal R25_AMOUNT;

			private String R26_NAME_OF_BOARD_MEMBERS;
			private String R26_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R26_NUMBER_OF_ACCOUNTS;
			private BigDecimal R26_AMOUNT;

			private String R27_NAME_OF_BOARD_MEMBERS;
			private String R27_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R27_NUMBER_OF_ACCOUNTS;
			private BigDecimal R27_AMOUNT;

			private String R28_NAME_OF_BOARD_MEMBERS;
			private String R28_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R28_NUMBER_OF_ACCOUNTS;
			private BigDecimal R28_AMOUNT;

			private String R29_NAME_OF_BOARD_MEMBERS;
			private String R29_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R29_NUMBER_OF_ACCOUNTS;
			private BigDecimal R29_AMOUNT;

			private String R30_NAME_OF_BOARD_MEMBERS;
			private String R30_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R30_NUMBER_OF_ACCOUNTS;
			private BigDecimal R30_AMOUNT;

			private String R31_NAME_OF_BOARD_MEMBERS;
			private String R31_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R31_NUMBER_OF_ACCOUNTS;
			private BigDecimal R31_AMOUNT;

			private String R32_NAME_OF_BOARD_MEMBERS;
			private String R32_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R32_NUMBER_OF_ACCOUNTS;
			private BigDecimal R32_AMOUNT;

			private String R33_NAME_OF_BOARD_MEMBERS;
			private String R33_EXECUTIVE_OR_NONEXECUTIVE;
			private BigDecimal R33_NUMBER_OF_ACCOUNTS;
			private BigDecimal R33_AMOUNT;

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

			public String getR20_NAME_OF_BOARD_MEMBERS() {
				return R20_NAME_OF_BOARD_MEMBERS;
			}

			public void setR20_NAME_OF_BOARD_MEMBERS(String r20_NAME_OF_BOARD_MEMBERS) {
				R20_NAME_OF_BOARD_MEMBERS = r20_NAME_OF_BOARD_MEMBERS;
			}

			public String getR20_EXECUTIVE_OR_NONEXECUTIVE() {
				return R20_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR20_EXECUTIVE_OR_NONEXECUTIVE(String r20_EXECUTIVE_OR_NONEXECUTIVE) {
				R20_EXECUTIVE_OR_NONEXECUTIVE = r20_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR20_NUMBER_OF_ACCOUNTS() {
				return R20_NUMBER_OF_ACCOUNTS;
			}

			public void setR20_NUMBER_OF_ACCOUNTS(BigDecimal r20_NUMBER_OF_ACCOUNTS) {
				R20_NUMBER_OF_ACCOUNTS = r20_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR20_AMOUNT() {
				return R20_AMOUNT;
			}

			public void setR20_AMOUNT(BigDecimal r20_AMOUNT) {
				R20_AMOUNT = r20_AMOUNT;
			}

			public String getR21_NAME_OF_BOARD_MEMBERS() {
				return R21_NAME_OF_BOARD_MEMBERS;
			}

			public void setR21_NAME_OF_BOARD_MEMBERS(String r21_NAME_OF_BOARD_MEMBERS) {
				R21_NAME_OF_BOARD_MEMBERS = r21_NAME_OF_BOARD_MEMBERS;
			}

			public String getR21_EXECUTIVE_OR_NONEXECUTIVE() {
				return R21_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR21_EXECUTIVE_OR_NONEXECUTIVE(String r21_EXECUTIVE_OR_NONEXECUTIVE) {
				R21_EXECUTIVE_OR_NONEXECUTIVE = r21_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR21_NUMBER_OF_ACCOUNTS() {
				return R21_NUMBER_OF_ACCOUNTS;
			}

			public void setR21_NUMBER_OF_ACCOUNTS(BigDecimal r21_NUMBER_OF_ACCOUNTS) {
				R21_NUMBER_OF_ACCOUNTS = r21_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR21_AMOUNT() {
				return R21_AMOUNT;
			}

			public void setR21_AMOUNT(BigDecimal r21_AMOUNT) {
				R21_AMOUNT = r21_AMOUNT;
			}

			public String getR22_NAME_OF_BOARD_MEMBERS() {
				return R22_NAME_OF_BOARD_MEMBERS;
			}

			public void setR22_NAME_OF_BOARD_MEMBERS(String r22_NAME_OF_BOARD_MEMBERS) {
				R22_NAME_OF_BOARD_MEMBERS = r22_NAME_OF_BOARD_MEMBERS;
			}

			public String getR22_EXECUTIVE_OR_NONEXECUTIVE() {
				return R22_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR22_EXECUTIVE_OR_NONEXECUTIVE(String r22_EXECUTIVE_OR_NONEXECUTIVE) {
				R22_EXECUTIVE_OR_NONEXECUTIVE = r22_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR22_NUMBER_OF_ACCOUNTS() {
				return R22_NUMBER_OF_ACCOUNTS;
			}

			public void setR22_NUMBER_OF_ACCOUNTS(BigDecimal r22_NUMBER_OF_ACCOUNTS) {
				R22_NUMBER_OF_ACCOUNTS = r22_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR22_AMOUNT() {
				return R22_AMOUNT;
			}

			public void setR22_AMOUNT(BigDecimal r22_AMOUNT) {
				R22_AMOUNT = r22_AMOUNT;
			}

			public String getR23_NAME_OF_BOARD_MEMBERS() {
				return R23_NAME_OF_BOARD_MEMBERS;
			}

			public void setR23_NAME_OF_BOARD_MEMBERS(String r23_NAME_OF_BOARD_MEMBERS) {
				R23_NAME_OF_BOARD_MEMBERS = r23_NAME_OF_BOARD_MEMBERS;
			}

			public String getR23_EXECUTIVE_OR_NONEXECUTIVE() {
				return R23_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR23_EXECUTIVE_OR_NONEXECUTIVE(String r23_EXECUTIVE_OR_NONEXECUTIVE) {
				R23_EXECUTIVE_OR_NONEXECUTIVE = r23_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR23_NUMBER_OF_ACCOUNTS() {
				return R23_NUMBER_OF_ACCOUNTS;
			}

			public void setR23_NUMBER_OF_ACCOUNTS(BigDecimal r23_NUMBER_OF_ACCOUNTS) {
				R23_NUMBER_OF_ACCOUNTS = r23_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR23_AMOUNT() {
				return R23_AMOUNT;
			}

			public void setR23_AMOUNT(BigDecimal r23_AMOUNT) {
				R23_AMOUNT = r23_AMOUNT;
			}

			public String getR24_NAME_OF_BOARD_MEMBERS() {
				return R24_NAME_OF_BOARD_MEMBERS;
			}

			public void setR24_NAME_OF_BOARD_MEMBERS(String r24_NAME_OF_BOARD_MEMBERS) {
				R24_NAME_OF_BOARD_MEMBERS = r24_NAME_OF_BOARD_MEMBERS;
			}

			public String getR24_EXECUTIVE_OR_NONEXECUTIVE() {
				return R24_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR24_EXECUTIVE_OR_NONEXECUTIVE(String r24_EXECUTIVE_OR_NONEXECUTIVE) {
				R24_EXECUTIVE_OR_NONEXECUTIVE = r24_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR24_NUMBER_OF_ACCOUNTS() {
				return R24_NUMBER_OF_ACCOUNTS;
			}

			public void setR24_NUMBER_OF_ACCOUNTS(BigDecimal r24_NUMBER_OF_ACCOUNTS) {
				R24_NUMBER_OF_ACCOUNTS = r24_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR24_AMOUNT() {
				return R24_AMOUNT;
			}

			public void setR24_AMOUNT(BigDecimal r24_AMOUNT) {
				R24_AMOUNT = r24_AMOUNT;
			}

			public String getR25_NAME_OF_BOARD_MEMBERS() {
				return R25_NAME_OF_BOARD_MEMBERS;
			}

			public void setR25_NAME_OF_BOARD_MEMBERS(String r25_NAME_OF_BOARD_MEMBERS) {
				R25_NAME_OF_BOARD_MEMBERS = r25_NAME_OF_BOARD_MEMBERS;
			}

			public String getR25_EXECUTIVE_OR_NONEXECUTIVE() {
				return R25_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR25_EXECUTIVE_OR_NONEXECUTIVE(String r25_EXECUTIVE_OR_NONEXECUTIVE) {
				R25_EXECUTIVE_OR_NONEXECUTIVE = r25_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR25_NUMBER_OF_ACCOUNTS() {
				return R25_NUMBER_OF_ACCOUNTS;
			}

			public void setR25_NUMBER_OF_ACCOUNTS(BigDecimal r25_NUMBER_OF_ACCOUNTS) {
				R25_NUMBER_OF_ACCOUNTS = r25_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR25_AMOUNT() {
				return R25_AMOUNT;
			}

			public void setR25_AMOUNT(BigDecimal r25_AMOUNT) {
				R25_AMOUNT = r25_AMOUNT;
			}

			public String getR26_NAME_OF_BOARD_MEMBERS() {
				return R26_NAME_OF_BOARD_MEMBERS;
			}

			public void setR26_NAME_OF_BOARD_MEMBERS(String r26_NAME_OF_BOARD_MEMBERS) {
				R26_NAME_OF_BOARD_MEMBERS = r26_NAME_OF_BOARD_MEMBERS;
			}

			public String getR26_EXECUTIVE_OR_NONEXECUTIVE() {
				return R26_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR26_EXECUTIVE_OR_NONEXECUTIVE(String r26_EXECUTIVE_OR_NONEXECUTIVE) {
				R26_EXECUTIVE_OR_NONEXECUTIVE = r26_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR26_NUMBER_OF_ACCOUNTS() {
				return R26_NUMBER_OF_ACCOUNTS;
			}

			public void setR26_NUMBER_OF_ACCOUNTS(BigDecimal r26_NUMBER_OF_ACCOUNTS) {
				R26_NUMBER_OF_ACCOUNTS = r26_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR26_AMOUNT() {
				return R26_AMOUNT;
			}

			public void setR26_AMOUNT(BigDecimal r26_AMOUNT) {
				R26_AMOUNT = r26_AMOUNT;
			}

			public String getR27_NAME_OF_BOARD_MEMBERS() {
				return R27_NAME_OF_BOARD_MEMBERS;
			}

			public void setR27_NAME_OF_BOARD_MEMBERS(String r27_NAME_OF_BOARD_MEMBERS) {
				R27_NAME_OF_BOARD_MEMBERS = r27_NAME_OF_BOARD_MEMBERS;
			}

			public String getR27_EXECUTIVE_OR_NONEXECUTIVE() {
				return R27_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR27_EXECUTIVE_OR_NONEXECUTIVE(String r27_EXECUTIVE_OR_NONEXECUTIVE) {
				R27_EXECUTIVE_OR_NONEXECUTIVE = r27_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR27_NUMBER_OF_ACCOUNTS() {
				return R27_NUMBER_OF_ACCOUNTS;
			}

			public void setR27_NUMBER_OF_ACCOUNTS(BigDecimal r27_NUMBER_OF_ACCOUNTS) {
				R27_NUMBER_OF_ACCOUNTS = r27_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR27_AMOUNT() {
				return R27_AMOUNT;
			}

			public void setR27_AMOUNT(BigDecimal r27_AMOUNT) {
				R27_AMOUNT = r27_AMOUNT;
			}

			public String getR28_NAME_OF_BOARD_MEMBERS() {
				return R28_NAME_OF_BOARD_MEMBERS;
			}

			public void setR28_NAME_OF_BOARD_MEMBERS(String r28_NAME_OF_BOARD_MEMBERS) {
				R28_NAME_OF_BOARD_MEMBERS = r28_NAME_OF_BOARD_MEMBERS;
			}

			public String getR28_EXECUTIVE_OR_NONEXECUTIVE() {
				return R28_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR28_EXECUTIVE_OR_NONEXECUTIVE(String r28_EXECUTIVE_OR_NONEXECUTIVE) {
				R28_EXECUTIVE_OR_NONEXECUTIVE = r28_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR28_NUMBER_OF_ACCOUNTS() {
				return R28_NUMBER_OF_ACCOUNTS;
			}

			public void setR28_NUMBER_OF_ACCOUNTS(BigDecimal r28_NUMBER_OF_ACCOUNTS) {
				R28_NUMBER_OF_ACCOUNTS = r28_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR28_AMOUNT() {
				return R28_AMOUNT;
			}

			public void setR28_AMOUNT(BigDecimal r28_AMOUNT) {
				R28_AMOUNT = r28_AMOUNT;
			}

			public String getR29_NAME_OF_BOARD_MEMBERS() {
				return R29_NAME_OF_BOARD_MEMBERS;
			}

			public void setR29_NAME_OF_BOARD_MEMBERS(String r29_NAME_OF_BOARD_MEMBERS) {
				R29_NAME_OF_BOARD_MEMBERS = r29_NAME_OF_BOARD_MEMBERS;
			}

			public String getR29_EXECUTIVE_OR_NONEXECUTIVE() {
				return R29_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR29_EXECUTIVE_OR_NONEXECUTIVE(String r29_EXECUTIVE_OR_NONEXECUTIVE) {
				R29_EXECUTIVE_OR_NONEXECUTIVE = r29_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR29_NUMBER_OF_ACCOUNTS() {
				return R29_NUMBER_OF_ACCOUNTS;
			}

			public void setR29_NUMBER_OF_ACCOUNTS(BigDecimal r29_NUMBER_OF_ACCOUNTS) {
				R29_NUMBER_OF_ACCOUNTS = r29_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR29_AMOUNT() {
				return R29_AMOUNT;
			}

			public void setR29_AMOUNT(BigDecimal r29_AMOUNT) {
				R29_AMOUNT = r29_AMOUNT;
			}

			public String getR30_NAME_OF_BOARD_MEMBERS() {
				return R30_NAME_OF_BOARD_MEMBERS;
			}

			public void setR30_NAME_OF_BOARD_MEMBERS(String r30_NAME_OF_BOARD_MEMBERS) {
				R30_NAME_OF_BOARD_MEMBERS = r30_NAME_OF_BOARD_MEMBERS;
			}

			public String getR30_EXECUTIVE_OR_NONEXECUTIVE() {
				return R30_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR30_EXECUTIVE_OR_NONEXECUTIVE(String r30_EXECUTIVE_OR_NONEXECUTIVE) {
				R30_EXECUTIVE_OR_NONEXECUTIVE = r30_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR30_NUMBER_OF_ACCOUNTS() {
				return R30_NUMBER_OF_ACCOUNTS;
			}

			public void setR30_NUMBER_OF_ACCOUNTS(BigDecimal r30_NUMBER_OF_ACCOUNTS) {
				R30_NUMBER_OF_ACCOUNTS = r30_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR30_AMOUNT() {
				return R30_AMOUNT;
			}

			public void setR30_AMOUNT(BigDecimal r30_AMOUNT) {
				R30_AMOUNT = r30_AMOUNT;
			}

			public String getR31_NAME_OF_BOARD_MEMBERS() {
				return R31_NAME_OF_BOARD_MEMBERS;
			}

			public void setR31_NAME_OF_BOARD_MEMBERS(String r31_NAME_OF_BOARD_MEMBERS) {
				R31_NAME_OF_BOARD_MEMBERS = r31_NAME_OF_BOARD_MEMBERS;
			}

			public String getR31_EXECUTIVE_OR_NONEXECUTIVE() {
				return R31_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR31_EXECUTIVE_OR_NONEXECUTIVE(String r31_EXECUTIVE_OR_NONEXECUTIVE) {
				R31_EXECUTIVE_OR_NONEXECUTIVE = r31_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR31_NUMBER_OF_ACCOUNTS() {
				return R31_NUMBER_OF_ACCOUNTS;
			}

			public void setR31_NUMBER_OF_ACCOUNTS(BigDecimal r31_NUMBER_OF_ACCOUNTS) {
				R31_NUMBER_OF_ACCOUNTS = r31_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR31_AMOUNT() {
				return R31_AMOUNT;
			}

			public void setR31_AMOUNT(BigDecimal r31_AMOUNT) {
				R31_AMOUNT = r31_AMOUNT;
			}

			public String getR32_NAME_OF_BOARD_MEMBERS() {
				return R32_NAME_OF_BOARD_MEMBERS;
			}

			public void setR32_NAME_OF_BOARD_MEMBERS(String r32_NAME_OF_BOARD_MEMBERS) {
				R32_NAME_OF_BOARD_MEMBERS = r32_NAME_OF_BOARD_MEMBERS;
			}

			public String getR32_EXECUTIVE_OR_NONEXECUTIVE() {
				return R32_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR32_EXECUTIVE_OR_NONEXECUTIVE(String r32_EXECUTIVE_OR_NONEXECUTIVE) {
				R32_EXECUTIVE_OR_NONEXECUTIVE = r32_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR32_NUMBER_OF_ACCOUNTS() {
				return R32_NUMBER_OF_ACCOUNTS;
			}

			public void setR32_NUMBER_OF_ACCOUNTS(BigDecimal r32_NUMBER_OF_ACCOUNTS) {
				R32_NUMBER_OF_ACCOUNTS = r32_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR32_AMOUNT() {
				return R32_AMOUNT;
			}

			public void setR32_AMOUNT(BigDecimal r32_AMOUNT) {
				R32_AMOUNT = r32_AMOUNT;
			}

			public String getR33_NAME_OF_BOARD_MEMBERS() {
				return R33_NAME_OF_BOARD_MEMBERS;
			}

			public void setR33_NAME_OF_BOARD_MEMBERS(String r33_NAME_OF_BOARD_MEMBERS) {
				R33_NAME_OF_BOARD_MEMBERS = r33_NAME_OF_BOARD_MEMBERS;
			}

			public String getR33_EXECUTIVE_OR_NONEXECUTIVE() {
				return R33_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public void setR33_EXECUTIVE_OR_NONEXECUTIVE(String r33_EXECUTIVE_OR_NONEXECUTIVE) {
				R33_EXECUTIVE_OR_NONEXECUTIVE = r33_EXECUTIVE_OR_NONEXECUTIVE;
			}

			public BigDecimal getR33_NUMBER_OF_ACCOUNTS() {
				return R33_NUMBER_OF_ACCOUNTS;
			}

			public void setR33_NUMBER_OF_ACCOUNTS(BigDecimal r33_NUMBER_OF_ACCOUNTS) {
				R33_NUMBER_OF_ACCOUNTS = r33_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR33_AMOUNT() {
				return R33_AMOUNT;
			}

			public void setR33_AMOUNT(BigDecimal r33_AMOUNT) {
				R33_AMOUNT = r33_AMOUNT;
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

		//// ROWMAPPER NORMAL RESUB3

		class MDISB5RESUBDetaillRowMapper_RESUB3 implements RowMapper<MDISB5_RESUB_Detail_Entity3> {

			@Override
			public MDISB5_RESUB_Detail_Entity3 mapRow(ResultSet rs, int rowNum) throws SQLException {

				MDISB5_RESUB_Detail_Entity3 obj = new MDISB5_RESUB_Detail_Entity3();

				obj.setR37_NAME(rs.getString("R37_NAME"));
				obj.setR37_DESIGNATION_OR_POSITION(rs.getString("R37_DESIGNATION_OR_POSITION"));
				obj.setR37_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R37_NUMBER_OF_ACCOUNTS"));
				obj.setR37_AMOUNT(rs.getBigDecimal("R37_AMOUNT"));

				obj.setR38_NAME(rs.getString("R38_NAME"));
				obj.setR38_DESIGNATION_OR_POSITION(rs.getString("R38_DESIGNATION_OR_POSITION"));
				obj.setR38_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R38_NUMBER_OF_ACCOUNTS"));
				obj.setR38_AMOUNT(rs.getBigDecimal("R38_AMOUNT"));

				obj.setR39_NAME(rs.getString("R39_NAME"));
				obj.setR39_DESIGNATION_OR_POSITION(rs.getString("R39_DESIGNATION_OR_POSITION"));
				obj.setR39_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R39_NUMBER_OF_ACCOUNTS"));
				obj.setR39_AMOUNT(rs.getBigDecimal("R39_AMOUNT"));

				obj.setR40_NAME(rs.getString("R40_NAME"));
				obj.setR40_DESIGNATION_OR_POSITION(rs.getString("R40_DESIGNATION_OR_POSITION"));
				obj.setR40_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R40_NUMBER_OF_ACCOUNTS"));
				obj.setR40_AMOUNT(rs.getBigDecimal("R40_AMOUNT"));

				obj.setR41_NAME(rs.getString("R41_NAME"));
				obj.setR41_DESIGNATION_OR_POSITION(rs.getString("R41_DESIGNATION_OR_POSITION"));
				obj.setR41_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R41_NUMBER_OF_ACCOUNTS"));
				obj.setR41_AMOUNT(rs.getBigDecimal("R41_AMOUNT"));

				obj.setR42_NAME(rs.getString("R42_NAME"));
				obj.setR42_DESIGNATION_OR_POSITION(rs.getString("R42_DESIGNATION_OR_POSITION"));
				obj.setR42_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R42_NUMBER_OF_ACCOUNTS"));
				obj.setR42_AMOUNT(rs.getBigDecimal("R42_AMOUNT"));

				obj.setR43_NAME(rs.getString("R43_NAME"));
				obj.setR43_DESIGNATION_OR_POSITION(rs.getString("R43_DESIGNATION_OR_POSITION"));
				obj.setR43_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R43_NUMBER_OF_ACCOUNTS"));
				obj.setR43_AMOUNT(rs.getBigDecimal("R43_AMOUNT"));

				obj.setR44_NAME(rs.getString("R44_NAME"));
				obj.setR44_DESIGNATION_OR_POSITION(rs.getString("R44_DESIGNATION_OR_POSITION"));
				obj.setR44_NUMBER_OF_ACCOUNTS(rs.getBigDecimal("R44_NUMBER_OF_ACCOUNTS"));
				obj.setR44_AMOUNT(rs.getBigDecimal("R44_AMOUNT"));

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

		public static class MDISB5_RESUB_Detail_Entity3 {

			private String R37_NAME;
			private String R37_DESIGNATION_OR_POSITION;
			private BigDecimal R37_NUMBER_OF_ACCOUNTS;
			private BigDecimal R37_AMOUNT;

			private String R38_NAME;
			private String R38_DESIGNATION_OR_POSITION;
			private BigDecimal R38_NUMBER_OF_ACCOUNTS;
			private BigDecimal R38_AMOUNT;

			private String R39_NAME;
			private String R39_DESIGNATION_OR_POSITION;
			private BigDecimal R39_NUMBER_OF_ACCOUNTS;
			private BigDecimal R39_AMOUNT;

			private String R40_NAME;
			private String R40_DESIGNATION_OR_POSITION;
			private BigDecimal R40_NUMBER_OF_ACCOUNTS;
			private BigDecimal R40_AMOUNT;

			private String R41_NAME;
			private String R41_DESIGNATION_OR_POSITION;
			private BigDecimal R41_NUMBER_OF_ACCOUNTS;
			private BigDecimal R41_AMOUNT;

			private String R42_NAME;
			private String R42_DESIGNATION_OR_POSITION;
			private BigDecimal R42_NUMBER_OF_ACCOUNTS;
			private BigDecimal R42_AMOUNT;

			private String R43_NAME;
			private String R43_DESIGNATION_OR_POSITION;
			private BigDecimal R43_NUMBER_OF_ACCOUNTS;
			private BigDecimal R43_AMOUNT;

			private String R44_NAME;
			private String R44_DESIGNATION_OR_POSITION;
			private BigDecimal R44_NUMBER_OF_ACCOUNTS;
			private BigDecimal R44_AMOUNT;

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

			public String getR37_NAME() {
				return R37_NAME;
			}

			public void setR37_NAME(String r37_NAME) {
				R37_NAME = r37_NAME;
			}

			public String getR37_DESIGNATION_OR_POSITION() {
				return R37_DESIGNATION_OR_POSITION;
			}

			public void setR37_DESIGNATION_OR_POSITION(String r37_DESIGNATION_OR_POSITION) {
				R37_DESIGNATION_OR_POSITION = r37_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR37_NUMBER_OF_ACCOUNTS() {
				return R37_NUMBER_OF_ACCOUNTS;
			}

			public void setR37_NUMBER_OF_ACCOUNTS(BigDecimal r37_NUMBER_OF_ACCOUNTS) {
				R37_NUMBER_OF_ACCOUNTS = r37_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR37_AMOUNT() {
				return R37_AMOUNT;
			}

			public void setR37_AMOUNT(BigDecimal r37_AMOUNT) {
				R37_AMOUNT = r37_AMOUNT;
			}

			public String getR38_NAME() {
				return R38_NAME;
			}

			public void setR38_NAME(String r38_NAME) {
				R38_NAME = r38_NAME;
			}

			public String getR38_DESIGNATION_OR_POSITION() {
				return R38_DESIGNATION_OR_POSITION;
			}

			public void setR38_DESIGNATION_OR_POSITION(String r38_DESIGNATION_OR_POSITION) {
				R38_DESIGNATION_OR_POSITION = r38_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR38_NUMBER_OF_ACCOUNTS() {
				return R38_NUMBER_OF_ACCOUNTS;
			}

			public void setR38_NUMBER_OF_ACCOUNTS(BigDecimal r38_NUMBER_OF_ACCOUNTS) {
				R38_NUMBER_OF_ACCOUNTS = r38_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR38_AMOUNT() {
				return R38_AMOUNT;
			}

			public void setR38_AMOUNT(BigDecimal r38_AMOUNT) {
				R38_AMOUNT = r38_AMOUNT;
			}

			public String getR39_NAME() {
				return R39_NAME;
			}

			public void setR39_NAME(String r39_NAME) {
				R39_NAME = r39_NAME;
			}

			public String getR39_DESIGNATION_OR_POSITION() {
				return R39_DESIGNATION_OR_POSITION;
			}

			public void setR39_DESIGNATION_OR_POSITION(String r39_DESIGNATION_OR_POSITION) {
				R39_DESIGNATION_OR_POSITION = r39_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR39_NUMBER_OF_ACCOUNTS() {
				return R39_NUMBER_OF_ACCOUNTS;
			}

			public void setR39_NUMBER_OF_ACCOUNTS(BigDecimal r39_NUMBER_OF_ACCOUNTS) {
				R39_NUMBER_OF_ACCOUNTS = r39_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR39_AMOUNT() {
				return R39_AMOUNT;
			}

			public void setR39_AMOUNT(BigDecimal r39_AMOUNT) {
				R39_AMOUNT = r39_AMOUNT;
			}

			public String getR40_NAME() {
				return R40_NAME;
			}

			public void setR40_NAME(String r40_NAME) {
				R40_NAME = r40_NAME;
			}

			public String getR40_DESIGNATION_OR_POSITION() {
				return R40_DESIGNATION_OR_POSITION;
			}

			public void setR40_DESIGNATION_OR_POSITION(String r40_DESIGNATION_OR_POSITION) {
				R40_DESIGNATION_OR_POSITION = r40_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR40_NUMBER_OF_ACCOUNTS() {
				return R40_NUMBER_OF_ACCOUNTS;
			}

			public void setR40_NUMBER_OF_ACCOUNTS(BigDecimal r40_NUMBER_OF_ACCOUNTS) {
				R40_NUMBER_OF_ACCOUNTS = r40_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR40_AMOUNT() {
				return R40_AMOUNT;
			}

			public void setR40_AMOUNT(BigDecimal r40_AMOUNT) {
				R40_AMOUNT = r40_AMOUNT;
			}

			public String getR41_NAME() {
				return R41_NAME;
			}

			public void setR41_NAME(String r41_NAME) {
				R41_NAME = r41_NAME;
			}

			public String getR41_DESIGNATION_OR_POSITION() {
				return R41_DESIGNATION_OR_POSITION;
			}

			public void setR41_DESIGNATION_OR_POSITION(String r41_DESIGNATION_OR_POSITION) {
				R41_DESIGNATION_OR_POSITION = r41_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR41_NUMBER_OF_ACCOUNTS() {
				return R41_NUMBER_OF_ACCOUNTS;
			}

			public void setR41_NUMBER_OF_ACCOUNTS(BigDecimal r41_NUMBER_OF_ACCOUNTS) {
				R41_NUMBER_OF_ACCOUNTS = r41_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR41_AMOUNT() {
				return R41_AMOUNT;
			}

			public void setR41_AMOUNT(BigDecimal r41_AMOUNT) {
				R41_AMOUNT = r41_AMOUNT;
			}

			public String getR42_NAME() {
				return R42_NAME;
			}

			public void setR42_NAME(String r42_NAME) {
				R42_NAME = r42_NAME;
			}

			public String getR42_DESIGNATION_OR_POSITION() {
				return R42_DESIGNATION_OR_POSITION;
			}

			public void setR42_DESIGNATION_OR_POSITION(String r42_DESIGNATION_OR_POSITION) {
				R42_DESIGNATION_OR_POSITION = r42_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR42_NUMBER_OF_ACCOUNTS() {
				return R42_NUMBER_OF_ACCOUNTS;
			}

			public void setR42_NUMBER_OF_ACCOUNTS(BigDecimal r42_NUMBER_OF_ACCOUNTS) {
				R42_NUMBER_OF_ACCOUNTS = r42_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR42_AMOUNT() {
				return R42_AMOUNT;
			}

			public void setR42_AMOUNT(BigDecimal r42_AMOUNT) {
				R42_AMOUNT = r42_AMOUNT;
			}

			public String getR43_NAME() {
				return R43_NAME;
			}

			public void setR43_NAME(String r43_NAME) {
				R43_NAME = r43_NAME;
			}

			public String getR43_DESIGNATION_OR_POSITION() {
				return R43_DESIGNATION_OR_POSITION;
			}

			public void setR43_DESIGNATION_OR_POSITION(String r43_DESIGNATION_OR_POSITION) {
				R43_DESIGNATION_OR_POSITION = r43_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR43_NUMBER_OF_ACCOUNTS() {
				return R43_NUMBER_OF_ACCOUNTS;
			}

			public void setR43_NUMBER_OF_ACCOUNTS(BigDecimal r43_NUMBER_OF_ACCOUNTS) {
				R43_NUMBER_OF_ACCOUNTS = r43_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR43_AMOUNT() {
				return R43_AMOUNT;
			}

			public void setR43_AMOUNT(BigDecimal r43_AMOUNT) {
				R43_AMOUNT = r43_AMOUNT;
			}

			public String getR44_NAME() {
				return R44_NAME;
			}

			public void setR44_NAME(String r44_NAME) {
				R44_NAME = r44_NAME;
			}

			public String getR44_DESIGNATION_OR_POSITION() {
				return R44_DESIGNATION_OR_POSITION;
			}

			public void setR44_DESIGNATION_OR_POSITION(String r44_DESIGNATION_OR_POSITION) {
				R44_DESIGNATION_OR_POSITION = r44_DESIGNATION_OR_POSITION;
			}

			public BigDecimal getR44_NUMBER_OF_ACCOUNTS() {
				return R44_NUMBER_OF_ACCOUNTS;
			}

			public void setR44_NUMBER_OF_ACCOUNTS(BigDecimal r44_NUMBER_OF_ACCOUNTS) {
				R44_NUMBER_OF_ACCOUNTS = r44_NUMBER_OF_ACCOUNTS;
			}

			public BigDecimal getR44_AMOUNT() {
				return R44_AMOUNT;
			}

			public void setR44_AMOUNT(BigDecimal r44_AMOUNT) {
				R44_AMOUNT = r44_AMOUNT;
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
		
		
		// MODEL AND VIEW METHOD summary

	public ModelAndView getBRRS_MDISB5View(String reportId, String fromdate, String todate, String currency,
			String dtltype, Pageable pageable, String type, BigDecimal version) {

		ModelAndView mv = new ModelAndView();

		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		System.out.println("MDISB5 View Called");
		System.out.println("Type = " + type);
		System.out.println("Version = " + version);
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

				List<MDISB5_Archival_Summary_Entity1> T1Master = getdatabydateListarchival1(dt, version);
				List<MDISB5_Archival_Summary_Entity2> T2Master = getdatabydateListarchival2(dt, version);
				List<MDISB5_Archival_Summary_Entity3> T3Master = getdatabydateListarchival3(dt, version);

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("reportsummary2", T3Master);
				mv.addObject("displaymode", "summary");

			}
			// ---------- CASE 2: RESUB ----------
			else if ("RESUB".equalsIgnoreCase(type) && version != null) {

				List<MDISB5_RESUB_Summary_Entity1> T1Master = RESUBgetdatabydateListarchival1(dt, version);
				List<MDISB5_RESUB_Summary_Entity2> T2Master = RESUBgetdatabydateListarchival2(dt, version);
				List<MDISB5_RESUB_Summary_Entity3> T3Master = RESUBgetdatabydateListarchival3(dt, version);
				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("reportsummary2", T3Master);
				mv.addObject("displaymode", "resubSummary");
			}

			// ---------- CASE 3: NORMAL ----------
			else {

				List<MDISB5_Summary_Entity1> T1Master = getDataByDate1(dt);
				List<MDISB5_Summary_Entity2> T2Master = getDataByDate2(dt);
				List<MDISB5_Summary_Entity3> T3Master = getDataByDate3(dt);
				System.out.println("T1Master Size: " + T1Master.size());
				System.out.println("T2Master Size: " + T2Master.size());
				System.out.println("T2Master Size: " + T3Master.size());

				mv.addObject("reportsummary", T1Master);
				mv.addObject("reportsummary1", T2Master);
				mv.addObject("reportsummary2", T3Master);
				mv.addObject("displaymode", "summary");
			}

			// ---------- CASE 4: DETAIL (NEW, ONLY ADDITION) ----------
			if ("detail".equalsIgnoreCase(dtltype)) {
				
				// DETAIL + ARCHIVAL
				if ("ARCHIVAL".equalsIgnoreCase(type) && version != null) {

					
					List<MDISB5_Archival_Detail_Entity1> T1Master = getArchivalDetaildatabydateList1(dt, version);
					List<MDISB5_Archival_Detail_Entity2> T2Master = getArchivalDetaildatabydateList2(dt, version);
					List<MDISB5_Archival_Detail_Entity3> T3Master = getArchivalDetaildatabydateList3(dt, version);

					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
					mv.addObject("reportsummary2", T3Master);
				}
				// ---------- RESUB DETAIL ----------
				else if ("RESUB".equalsIgnoreCase(type) && version != null) {

					List<MDISB5_RESUB_Detail_Entity1> T1Master = getRESUBDetaildatabydateList1(dt, version);
					List<MDISB5_RESUB_Detail_Entity2> T2Master = getRESUBDetaildatabydateList2(dt, version);
					List<MDISB5_RESUB_Detail_Entity3> T3Master = getRESUBDetaildatabydateList3(dt, version);

					System.out.println("Resub Detail Size : " + T1Master.size());

					mv.addObject("displaymode", "resubDetail");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
					mv.addObject("reportsummary2", T3Master);

				}
				// DETAIL + NORMAL
				else {

					List<MDISB5_Detail_Entity1> T1Master = getDetaildatabydateList1(dt);
					List<MDISB5_Detail_Entity2> T2Master = getDetaildatabydateList2(dt);
					List<MDISB5_Detail_Entity3> T3Master = getDetaildatabydateList3(dt);
					mv.addObject("displaymode", "Details");
					mv.addObject("reportsummary", T1Master);
					mv.addObject("reportsummary1", T2Master);
					mv.addObject("reportsummary2", T3Master);
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		mv.setViewName("BRRS/MDISB5");

		System.out.println("View set to: " + mv.getViewName());

		return mv;
	}

	@Transactional
	public void updateResubReport(
	        MDISB5_RESUB_Summary_Entity1 updatedEntity1,
	        MDISB5_RESUB_Summary_Entity2 updatedEntity2,
	        MDISB5_RESUB_Summary_Entity3 updatedEntity3) {

	    // ====================================================
	    // 1️⃣ GET REPORT DATE
	    // ====================================================

	    Date reportDate1 = updatedEntity1.getREPORT_DATE();
	    Date reportDate2 = updatedEntity2.getREPORT_DATE();
	    Date reportDate3 = updatedEntity3.getREPORT_DATE();

	    if (reportDate1 == null || reportDate2 == null || reportDate3 == null) {
	        throw new RuntimeException("Report date cannot be null");
	    }

	    // ====================================================
	    // 2️⃣ FETCH MAX VERSION
	    // ====================================================

	    BigDecimal maxVer1 = RESUBfindMaxVersion1(reportDate1);
	    BigDecimal maxVer2 = RESUBfindMaxVersion2(reportDate2);
	    BigDecimal maxVer3 = RESUBfindMaxVersion3(reportDate3);

	    if (maxVer1 == null)
	        maxVer1 = BigDecimal.ZERO;

	    if (maxVer2 == null)
	        maxVer2 = BigDecimal.ZERO;

	    if (maxVer3 == null)
	        maxVer3 = BigDecimal.ZERO;

	    BigDecimal currentMax = maxVer1.max(maxVer2).max(maxVer3);
	    BigDecimal newVersion = currentMax.add(BigDecimal.ONE);

	    Date now = new Date();

	    // ====================================================
	    // 3️⃣ RESUB SUMMARY
	    // ====================================================

	    MDISB5_RESUB_Summary_Entity1 resubSummary1 = new MDISB5_RESUB_Summary_Entity1();
	    MDISB5_RESUB_Summary_Entity2 resubSummary2 = new MDISB5_RESUB_Summary_Entity2();
	    MDISB5_RESUB_Summary_Entity3 resubSummary3 = new MDISB5_RESUB_Summary_Entity3();

	    BeanUtils.copyProperties(updatedEntity1, resubSummary1);
	    BeanUtils.copyProperties(updatedEntity2, resubSummary2);
	    BeanUtils.copyProperties(updatedEntity3, resubSummary3);

	    resubSummary1.setREPORT_DATE(reportDate1);
	    resubSummary1.setREPORT_VERSION(newVersion);
	    resubSummary1.setREPORT_RESUBDATE(now);

	    resubSummary2.setREPORT_DATE(reportDate2);
	    resubSummary2.setREPORT_VERSION(newVersion);
	    resubSummary2.setREPORT_RESUBDATE(now);

	    resubSummary3.setREPORT_DATE(reportDate3);
	    resubSummary3.setREPORT_VERSION(newVersion);
	    resubSummary3.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 4️⃣ RESUB DETAIL
	    // ====================================================

	    MDISB5_RESUB_Detail_Entity1 resubDetail1 = new MDISB5_RESUB_Detail_Entity1();
	    MDISB5_RESUB_Detail_Entity2 resubDetail2 = new MDISB5_RESUB_Detail_Entity2();
	    MDISB5_RESUB_Detail_Entity3 resubDetail3 = new MDISB5_RESUB_Detail_Entity3();

	    BeanUtils.copyProperties(updatedEntity1, resubDetail1);
	    BeanUtils.copyProperties(updatedEntity2, resubDetail2);
	    BeanUtils.copyProperties(updatedEntity3, resubDetail3);

	    resubDetail1.setREPORT_DATE(reportDate1);
	    resubDetail1.setREPORT_VERSION(newVersion);
	    resubDetail1.setREPORT_RESUBDATE(now);

	    resubDetail2.setREPORT_DATE(reportDate2);
	    resubDetail2.setREPORT_VERSION(newVersion);
	    resubDetail2.setREPORT_RESUBDATE(now);

	    resubDetail3.setREPORT_DATE(reportDate3);
	    resubDetail3.setREPORT_VERSION(newVersion);
	    resubDetail3.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 5️⃣ ARCHIVAL SUMMARY
	    // ====================================================

	    MDISB5_Archival_Summary_Entity1 archSummary1 = new MDISB5_Archival_Summary_Entity1();
	    MDISB5_Archival_Summary_Entity2 archSummary2 = new MDISB5_Archival_Summary_Entity2();
	    MDISB5_Archival_Summary_Entity3 archSummary3 = new MDISB5_Archival_Summary_Entity3();

	    BeanUtils.copyProperties(updatedEntity1, archSummary1);
	    BeanUtils.copyProperties(updatedEntity2, archSummary2);
	    BeanUtils.copyProperties(updatedEntity3, archSummary3);

	    archSummary1.setREPORT_DATE(reportDate1);
	    archSummary1.setREPORT_VERSION(newVersion);
	    archSummary1.setREPORT_RESUBDATE(now);

	    archSummary2.setREPORT_DATE(reportDate2);
	    archSummary2.setREPORT_VERSION(newVersion);
	    archSummary2.setREPORT_RESUBDATE(now);

	    archSummary3.setREPORT_DATE(reportDate3);
	    archSummary3.setREPORT_VERSION(newVersion);
	    archSummary3.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 6️⃣ ARCHIVAL DETAIL
	    // ====================================================

	    MDISB5_Archival_Detail_Entity1 archDetail1 = new MDISB5_Archival_Detail_Entity1();
	    MDISB5_Archival_Detail_Entity2 archDetail2 = new MDISB5_Archival_Detail_Entity2();
	    MDISB5_Archival_Detail_Entity3 archDetail3 = new MDISB5_Archival_Detail_Entity3();

	    BeanUtils.copyProperties(updatedEntity1, archDetail1);
	    BeanUtils.copyProperties(updatedEntity2, archDetail2);
	    BeanUtils.copyProperties(updatedEntity3, archDetail3);

	    archDetail1.setREPORT_DATE(reportDate1);
	    archDetail1.setREPORT_VERSION(newVersion);
	    archDetail1.setREPORT_RESUBDATE(now);

	    archDetail2.setREPORT_DATE(reportDate2);
	    archDetail2.setREPORT_VERSION(newVersion);
	    archDetail2.setREPORT_RESUBDATE(now);

	    archDetail3.setREPORT_DATE(reportDate3);
	    archDetail3.setREPORT_VERSION(newVersion);
	    archDetail3.setREPORT_RESUBDATE(now);

	    // ====================================================
	    // 7️⃣ SAVE ALL
	    // ====================================================

	    sessionFactory.getCurrentSession().merge(resubSummary1);
	    sessionFactory.getCurrentSession().merge(resubSummary2);
	    sessionFactory.getCurrentSession().merge(resubSummary3);

	    sessionFactory.getCurrentSession().merge(resubDetail1);
	    sessionFactory.getCurrentSession().merge(resubDetail2);
	    sessionFactory.getCurrentSession().merge(resubDetail3);

	    sessionFactory.getCurrentSession().merge(archSummary1);
	    sessionFactory.getCurrentSession().merge(archSummary2);
	    sessionFactory.getCurrentSession().merge(archSummary3);

	    sessionFactory.getCurrentSession().merge(archDetail1);
	    sessionFactory.getCurrentSession().merge(archDetail2);
	    sessionFactory.getCurrentSession().merge(archDetail3);
	}
	
	@Transactional
	public void updateReport(MDISB5_Summary_Entity1 request1) {

	    System.out.println("Came to BRRS_MDISB5 Service");
	    System.out.println("Report Date: " + request1.getREPORT_DATE());

	    // Fetch existing Summary
	    MDISB5_Summary_Entity1 existingSummary =
	            findSummaryByReportDate(request1.getREPORT_DATE());

	    if (existingSummary == null) {
	        throw new RuntimeException(
	                "Summary record not found for REPORT_DATE : "
	                        + request1.getREPORT_DATE());
	    }

	    // Fetch existing Detail
	    MDISB5_Detail_Entity1 existingDetail =
	            findDetailByReportDate(request1.getREPORT_DATE());

	    if (existingDetail == null) {
	        existingDetail = new MDISB5_Detail_Entity1();
	        existingDetail.setREPORT_DATE(request1.getREPORT_DATE());
	    }

	    try {

	        for (int i = 5; i <= 15; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                    "NAME_OF_SHAREHOLDER",
	                    "PERCENTAGE_SHAREHOLDING",
	                    "NUMBER_OF_ACCOUNTS",
	                    "AMOUNT"
	            };

	            for (String field : fields) {

	                try {

	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter =
	                            MDISB5_Summary_Entity1.class.getMethod(getterName);

	                    Method summarySetter =
	                            MDISB5_Summary_Entity1.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Method detailSetter =
	                            MDISB5_Detail_Entity1.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Object value = getter.invoke(request1);

	                    summarySetter.invoke(existingSummary, value);
	                    detailSetter.invoke(existingDetail, value);

	                } catch (NoSuchMethodException e) {
	                    // Skip missing fields
	                    continue;
	                }
	            }
	        }

	        existingSummary.setREPORT_DATE(request1.getREPORT_DATE());
	        existingDetail.setREPORT_DATE(request1.getREPORT_DATE());

	        // Save Summary
	        sessionFactory.getCurrentSession().merge(existingSummary);

	        // Save Detail
	        sessionFactory.getCurrentSession().merge(existingDetail);

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Error while updating BRRS_MDISB5 Report", e);
	    }
	}
	
	
	@Transactional
	public void updateReport2(MDISB5_Summary_Entity2 updatedSummary) {

	    System.out.println("Came to BRRS_MDISB5 Service - Part 2");
	    System.out.println("Report Date: " + updatedSummary.getREPORT_DATE());

	    // Fetch existing Summary
	    MDISB5_Summary_Entity2 existingSummary =
	            findSummary2ByReportDate(updatedSummary.getREPORT_DATE());

	    if (existingSummary == null) {
	        throw new RuntimeException(
	                "Summary record not found for REPORT_DATE: "
	                        + updatedSummary.getREPORT_DATE());
	    }

	    // Fetch existing Detail
	    MDISB5_Detail_Entity2 existingDetail =
	            findDetail2ByReportDate(updatedSummary.getREPORT_DATE());

	    if (existingDetail == null) {
	        existingDetail = new MDISB5_Detail_Entity2();
	        existingDetail.setREPORT_DATE(updatedSummary.getREPORT_DATE());
	    }

	    try {

	        for (int i = 20; i <= 33; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                    "NAME_OF_BOARD_MEMBERS",
	                    "EXECUTIVE_OR_NONEXECUTIVE",
	                    "NUMBER_OF_ACCOUNTS",
	                    "AMOUNT"
	            };

	            for (String field : fields) {

	                try {

	                    String getterName = "get" + prefix + field;
	                    String setterName = "set" + prefix + field;

	                    Method getter =
	                            MDISB5_Summary_Entity2.class.getMethod(getterName);

	                    Method summarySetter =
	                            MDISB5_Summary_Entity2.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Method detailSetter =
	                            MDISB5_Detail_Entity2.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Object newValue = getter.invoke(updatedSummary);

	                    summarySetter.invoke(existingSummary, newValue);
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Error while updating BRRS_MDISB5 Report 2", e);
	    }

	    // Save using Hibernate Session
	    sessionFactory.getCurrentSession().merge(existingSummary);
	    sessionFactory.getCurrentSession().merge(existingDetail);
	}
	
	
	@Transactional
	public void updateReport3(MDISB5_Summary_Entity3 updatedSummary) {

	    System.out.println("Came to BRRS_MDISB5 Service - Part 3");
	    System.out.println("Report Date: " + updatedSummary.getREPORT_DATE());

	    // 1️⃣ Fetch existing SUMMARY
	    MDISB5_Summary_Entity3 existingSummary =
	            findSummary3ByReportDate(updatedSummary.getREPORT_DATE());

	    if (existingSummary == null) {
	        throw new RuntimeException(
	                "Summary record not found for REPORT_DATE: "
	                        + updatedSummary.getREPORT_DATE());
	    }

	    // 2️⃣ Fetch existing DETAIL or CREATE
	    MDISB5_Detail_Entity3 existingDetail =
	            findDetail3ByReportDate(updatedSummary.getREPORT_DATE());

	    if (existingDetail == null) {
	        existingDetail = new MDISB5_Detail_Entity3();
	        existingDetail.setREPORT_DATE(updatedSummary.getREPORT_DATE());
	    }

	    try {

	        // 🔁 Loop R37 → R44
	        for (int i = 37; i <= 44; i++) {

	            String prefix = "R" + i + "_";

	            String[] fields = {
	                    "NAME",
	                    "DESIGNATION_OR_POSITION",
	                    "NUMBER_OF_ACCOUNTS",
	                    "AMOUNT"
	            };

	            for (String field : fields) {

	                String getterName = "get" + prefix + field;
	                String setterName = "set" + prefix + field;

	                try {

	                    Method getter =
	                            MDISB5_Summary_Entity3.class.getMethod(getterName);

	                    Method summarySetter =
	                            MDISB5_Summary_Entity3.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Method detailSetter =
	                            MDISB5_Detail_Entity3.class.getMethod(
	                                    setterName,
	                                    getter.getReturnType());

	                    Object newValue = getter.invoke(updatedSummary);

	                    // Update Summary
	                    summarySetter.invoke(existingSummary, newValue);

	                    // Update Detail
	                    detailSetter.invoke(existingDetail, newValue);

	                } catch (NoSuchMethodException e) {
	                    continue;
	                }
	            }
	        }

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Error while updating BRRS_MDISB5 Report 3", e);
	    }

	    // 3️⃣ Save BOTH using Hibernate Session
	    sessionFactory.getCurrentSession().merge(existingSummary);
	    sessionFactory.getCurrentSession().merge(existingDetail);
	}

////////////////////////////////////////// RESUBMISSION///////////////////////////////////////////////////////////////////
/// Report Date | Report Version | Domain
/// RESUB VIEW

	public List<Object[]> getBRRS_MDISB5Resub() {
		List<Object[]> resubList = new ArrayList<>();
		try {
			List<MDISB5_Archival_Summary_Entity1> latestArchivalList = getdatabydateListWithVersion1();

			if (latestArchivalList != null && !latestArchivalList.isEmpty()) {
				for (MDISB5_Archival_Summary_Entity1 entity : latestArchivalList) {
					resubList.add(new Object[] { entity.getREPORT_DATE(), entity.getREPORT_DATE(),
							entity.getREPORT_RESUBDATE() });
				}
				System.out.println("Fetched " + resubList.size() + " record(s)");
			} else {
				System.out.println("No archival data found.");
			}

		} catch (Exception e) {
			System.err.println("Error fetching BRRS_MDISB5 Resub data: " + e.getMessage());
			e.printStackTrace();
		}
		return resubList;
	}

	public List<Object[]> getBRRS_MDISB5Archival() {

	    String sql = "SELECT REPORT_DATE, REPORT_VERSION, REPORT_RESUBDATE " +
	                 "FROM BRRS_MDISB5_ARCHIVALTABLE_SUMMARY1 " +
	                 "ORDER BY REPORT_VERSION";

	    return jdbcTemplate.query(
	            sql,
	            (rs, rowNum) -> new Object[] {
	                    rs.getDate("REPORT_DATE"),
	                    rs.getBigDecimal("REPORT_VERSION"),
	                    rs.getDate("REPORT_RESUBDATE")
	            }
	    );
	}

	// Normal Format Excel
	public byte[] getMDISB5Excel(String filename, String reportId, String fromdate, String todate, String currency,
			String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		logger.info("DownloadFile: reportId={}, filename={}", reportId, filename, type, version);

		// Convert string to Date
		Date reportDate = dateformat.parse(todate);

		// ARCHIVAL check
		if ("ARCHIVAL".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating ARCHIVAL report for version {}", version);
			return getExcelMDISB5ARCHIVAL(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}
		// RESUB check
		else if ("RESUB".equalsIgnoreCase(type) && version != null && version != null) {
			logger.info("Service: Generating RESUB report for version {}", version);

			List<MDISB5_Archival_Summary_Entity1> T1Master = getdatabydateListarchival1(dateformat.parse(todate), version);

			List<MDISB5_Archival_Summary_Entity2> T2Master = getdatabydateListarchival2(dateformat.parse(todate), version);

			List<MDISB5_Archival_Summary_Entity3> T3Master = getdatabydateListarchival3(dateformat.parse(todate), version);

			// Generate Excel for RESUB
			return BRRS_MDISB5ResubExcel(filename, reportId, fromdate, todate, currency, dtltype, type, version);
		}

		// Default (LIVE) case
		List<MDISB5_Summary_Entity1> dataList1 = getDataByDate1(dateformat.parse(todate));
		List<MDISB5_Summary_Entity2> dataList2 = getDataByDate2(dateformat.parse(todate));
		List<MDISB5_Summary_Entity3> dataList3 = getDataByDate3(dateformat.parse(todate));

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
					MDISB5_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Summary_Entity3 record3 = dataList3.get(i);

					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

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

	public byte[] getExcelMDISB5ARCHIVAL(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {
		logger.info("Service: Starting Excel generation process in memory.");
		if ("ARCHIVAL".equals(type) && version != null) {
		}
		List<MDISB5_Archival_Summary_Entity1> dataList1 = getdatabydateListarchival1(dateformat.parse(todate), version);

		List<MDISB5_Archival_Summary_Entity2> dataList2 = getdatabydateListarchival2(dateformat.parse(todate), version);

		List<MDISB5_Archival_Summary_Entity3> dataList3 = getdatabydateListarchival3(dateformat.parse(todate), version);

		if (dataList1.isEmpty()) {
			logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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
					MDISB5_Archival_Summary_Entity1 record1 = dataList1.get(i);
					MDISB5_Archival_Summary_Entity2 record2 = dataList2.get(i);
					MDISB5_Archival_Summary_Entity3 record3 = dataList3.get(i);

					System.out.println("rownumber=" + (startRow + i));

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

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

	// Resub Format Excel
	public byte[] BRRS_MDISB5ResubExcel(String filename, String reportId, String fromdate, String todate,
			String currency, String dtltype, String type, BigDecimal version) throws Exception {

		logger.info("Service: Starting Excel generation process in memory for RESUB Excel.");

		if (type.equals("RESUB") & version != null) {

		}

		List<MDISB5_Archival_Summary_Entity1> dataList = getdatabydateListarchival1(dateformat.parse(todate), version);
		List<MDISB5_Archival_Summary_Entity2> dataList1 = getdatabydateListarchival2(dateformat.parse(todate), version);
		List<MDISB5_Archival_Summary_Entity3> dataList2 = getdatabydateListarchival3(dateformat.parse(todate), version);

		if (dataList.isEmpty()) {
			logger.warn("Service: No data found for MDISB5 report. Returning empty result.");
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
			int startRow = 9;

			if (!dataList.isEmpty()) {
				for (int i = 0; i < dataList.size(); i++) {

					MDISB5_Archival_Summary_Entity1 record1 = dataList.get(i);
					MDISB5_Archival_Summary_Entity2 record2 = dataList1.get(i);
					MDISB5_Archival_Summary_Entity3 record3 = dataList2.get(i);
					System.out.println("rownumber=" + startRow + i);

					Row row;
					Cell cellA, cellB, cellC, cellD;
					CellStyle originalStyle;

					// ===== Row 5 / Col A =====
					row = sheet.getRow(4);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR5_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR5_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// ===== Row 5 / Col B =====
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR5_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR5_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// ===== R11 / Col C =====

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR5_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR5_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// ===== R11 / Col D =====

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR5_AMOUNT() != null)
						cellD.setCellValue(record1.getR5_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 6 / Col A =====
					row = sheet.getRow(5);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR6_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR6_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR6_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR6_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR6_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR6_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR6_AMOUNT() != null)
						cellD.setCellValue(record1.getR6_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 7 / Col A =====
					row = sheet.getRow(6);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR7_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR7_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR7_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR7_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR7_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR7_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR7_AMOUNT() != null)
						cellD.setCellValue(record1.getR7_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 8 / Col A =====
					row = sheet.getRow(7);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR8_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR8_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR8_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR8_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR8_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR8_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR8_AMOUNT() != null)
						cellD.setCellValue(record1.getR8_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 9 / Col A =====
					row = sheet.getRow(8);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR9_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR9_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR9_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR9_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR9_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR9_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR9_AMOUNT() != null)
						cellD.setCellValue(record1.getR9_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 10 / Col A =====
					row = sheet.getRow(9);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR10_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR10_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR10_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR10_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR10_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR10_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR10_AMOUNT() != null)
						cellD.setCellValue(record1.getR10_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 11 / Col A =====
					row = sheet.getRow(10);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR11_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR11_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR11_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR11_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR11_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR11_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR11_AMOUNT() != null)
						cellD.setCellValue(record1.getR11_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 12 / Col A =====
					row = sheet.getRow(11);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR12_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR12_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR12_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR12_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR12_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR12_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR12_AMOUNT() != null)
						cellD.setCellValue(record1.getR12_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 13 / Col A =====
					row = sheet.getRow(12);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR13_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR13_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR13_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR13_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR13_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR13_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR13_AMOUNT() != null)
						cellD.setCellValue(record1.getR13_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 14 / Col A =====
					row = sheet.getRow(13);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR14_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR14_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR14_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR14_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR14_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR14_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR14_AMOUNT() != null)
						cellD.setCellValue(record1.getR14_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 15 / Col A =====
					row = sheet.getRow(14);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record1.getR15_NAME_OF_SHAREHOLDER() != null)
						cellA.setCellValue(record1.getR15_NAME_OF_SHAREHOLDER()); // String directly
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					if (record1.getR15_PERCENTAGE_SHAREHOLDING() != null)
						cellB.setCellValue(record1.getR15_PERCENTAGE_SHAREHOLDING().doubleValue());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record1.getR15_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record1.getR15_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record1.getR15_AMOUNT() != null)
						cellD.setCellValue(record1.getR15_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 20 / Col A =====
					row = sheet.getRow(19);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR20_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR20_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR20_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR20_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR20_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR20_AMOUNT() != null)
						cellD.setCellValue(record2.getR20_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 21 / Col A =====
					row = sheet.getRow(20);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR21_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR21_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR21_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR21_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR21_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR21_AMOUNT() != null)
						cellD.setCellValue(record2.getR21_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 22 / Col A =====
					row = sheet.getRow(21);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR22_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR22_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR22_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR22_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR22_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR22_AMOUNT() != null)
						cellD.setCellValue(record2.getR22_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 23 / Col A =====
					row = sheet.getRow(22);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR23_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR23_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR23_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR23_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR23_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR23_AMOUNT() != null)
						cellD.setCellValue(record2.getR23_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 24 / Col A =====
					row = sheet.getRow(23);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR24_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR24_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR24_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR24_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR24_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR24_AMOUNT() != null)
						cellD.setCellValue(record2.getR24_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 25 / Col A =====
					row = sheet.getRow(24);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR25_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR25_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR25_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR25_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR25_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR25_AMOUNT() != null)
						cellD.setCellValue(record2.getR25_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 26 / Col A =====
					row = sheet.getRow(25);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR26_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR26_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR26_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR26_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR26_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR26_AMOUNT() != null)
						cellD.setCellValue(record2.getR26_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 27 / Col A =====
					row = sheet.getRow(26);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR27_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR27_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR27_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR27_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR27_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR27_AMOUNT() != null)
						cellD.setCellValue(record2.getR27_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 28 / Col A =====
					row = sheet.getRow(27);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR28_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR28_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR28_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR28_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR28_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR28_AMOUNT() != null)
						cellD.setCellValue(record2.getR28_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 29 / Col A =====
					row = sheet.getRow(28);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR29_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR29_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR29_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR29_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR29_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR29_AMOUNT() != null)
						cellD.setCellValue(record2.getR29_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 30 / Col A =====
					row = sheet.getRow(29);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR30_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR30_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR30_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR30_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR30_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR30_AMOUNT() != null)
						cellD.setCellValue(record2.getR30_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 31 / Col A =====
					row = sheet.getRow(30);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR31_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR31_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR31_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR31_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR31_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR31_AMOUNT() != null)
						cellD.setCellValue(record2.getR31_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 32 / Col A =====
					row = sheet.getRow(31);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR32_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR32_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR32_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR32_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR32_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR32_AMOUNT() != null)
						cellD.setCellValue(record2.getR32_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 33 / Col A =====
					row = sheet.getRow(32);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_NAME_OF_BOARD_MEMBERS() != null)
						cellA.setCellValue(record2.getR33_NAME_OF_BOARD_MEMBERS());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record2.getR33_EXECUTIVE_OR_NONEXECUTIVE() != null)
						cellB.setCellValue(record2.getR33_EXECUTIVE_OR_NONEXECUTIVE());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record2.getR33_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record2.getR33_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record2.getR33_AMOUNT() != null)
						cellD.setCellValue(record2.getR33_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 37 / Col A =====
					row = sheet.getRow(36);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_NAME() != null)
						cellA.setCellValue(record3.getR37_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR37_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR37_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR37_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR37_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR37_AMOUNT() != null)
						cellD.setCellValue(record3.getR37_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 38 / Col A =====
					row = sheet.getRow(37);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_NAME() != null)
						cellA.setCellValue(record3.getR38_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR38_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR38_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR38_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR38_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR38_AMOUNT() != null)
						cellD.setCellValue(record3.getR38_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 39 / Col A =====
					row = sheet.getRow(38);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_NAME() != null)
						cellA.setCellValue(record3.getR39_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR39_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR39_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR39_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR39_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR39_AMOUNT() != null)
						cellD.setCellValue(record3.getR39_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 40 / Col A =====
					row = sheet.getRow(39);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_NAME() != null)
						cellA.setCellValue(record3.getR40_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR40_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR40_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR40_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR40_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR40_AMOUNT() != null)
						cellD.setCellValue(record3.getR40_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 41 / Col A =====
					row = sheet.getRow(40);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_NAME() != null)
						cellA.setCellValue(record3.getR41_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR41_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR41_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR41_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR41_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR41_AMOUNT() != null)
						cellD.setCellValue(record3.getR41_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 42 / Col A =====
					row = sheet.getRow(41);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_NAME() != null)
						cellA.setCellValue(record3.getR42_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR42_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR42_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR42_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR42_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR42_AMOUNT() != null)
						cellD.setCellValue(record3.getR42_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 43 / Col A =====
					row = sheet.getRow(42);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_NAME() != null)
						cellA.setCellValue(record3.getR43_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR43_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR43_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR43_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR43_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR43_AMOUNT() != null)
						cellD.setCellValue(record3.getR43_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

					// ===== Row 44 / Col A =====
					row = sheet.getRow(43);
					cellA = row.getCell(0);
					if (cellA == null)
						cellA = row.createCell(1);
					originalStyle = cellA.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_NAME() != null)
						cellA.setCellValue(record3.getR44_NAME());
					else
						cellA.setCellValue("");
					cellA.setCellStyle(originalStyle);

					// Col B
					cellB = row.getCell(1);
					if (cellB == null)
						cellB = row.createCell(1);
					originalStyle = cellB.getCellStyle();
					// ✅ Handle String value
					if (record3.getR44_DESIGNATION_OR_POSITION() != null)
						cellB.setCellValue(record3.getR44_DESIGNATION_OR_POSITION());
					else
						cellB.setCellValue("");
					cellB.setCellStyle(originalStyle);

					// Col C

					cellC = row.getCell(2);
					if (cellC == null)
						cellC = row.createCell(2);
					originalStyle = cellC.getCellStyle();
					if (record3.getR44_NUMBER_OF_ACCOUNTS() != null)
						cellC.setCellValue(record3.getR44_NUMBER_OF_ACCOUNTS().doubleValue());
					else
						cellC.setCellValue("");
					cellC.setCellStyle(originalStyle);

					// Col D

					cellD = row.getCell(3);
					if (cellD == null)
						cellD = row.createCell(3);
					originalStyle = cellD.getCellStyle();
					if (record3.getR44_AMOUNT() != null)
						cellD.setCellValue(record3.getR44_AMOUNT().doubleValue());
					else
						cellD.setCellValue("");
					cellD.setCellStyle(originalStyle);

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
